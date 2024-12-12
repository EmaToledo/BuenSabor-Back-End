package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.Auth0RoleDTO;
import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.dto.RoleDTO;
import com.elbuensabor.api.entity.RecipeStep;
import com.elbuensabor.api.entity.Role;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.RoleMapper;
import com.elbuensabor.api.repository.IGenericRepository;
import com.elbuensabor.api.repository.IRoleRepository;
import com.elbuensabor.api.service.Auth0TokenService;
import com.elbuensabor.api.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, RoleDTO, Long> implements RoleService {
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private Auth0TokenService serviceToken;
    private final RoleMapper roleMapper = RoleMapper.getInstance();

    public RoleServiceImpl(IGenericRepository<Role, Long> genericRepository, GenericMapper<Role, RoleDTO> genericMapper) {
        super(genericRepository, genericMapper);
    }

    @Override
    public OrderDTO updateOrderState(Long id, String newState) throws Exception {
        return null;
    }



    @Transactional(readOnly = true)
    public  Auth0RoleDTO[] getUserRolesAuth0(String id) {
        try {
            String token = serviceToken.getAuth0Token();
            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            String url = "https://" + serviceToken.getDomain() + "/api/v2/users/" + encodedUserId + "/roles";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(responseBody, Auth0RoleDTO[].class);
        } catch (Exception e) {
            System.out.println("Error get User Roles: " + e );
            e.printStackTrace();
            return null;
        }
    }


}
