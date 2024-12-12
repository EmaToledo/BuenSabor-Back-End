package com.elbuensabor.api.service.impl;
import com.elbuensabor.api.dto.*;
import com.elbuensabor.api.entity.Role;
import com.elbuensabor.api.entity.User;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.UserMapper;
import com.elbuensabor.api.repository.IUserRepository;
import com.elbuensabor.api.service.Auth0TokenService;
import com.elbuensabor.api.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.squareup.okhttp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserDTO, Long> implements UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private Auth0TokenService serviceToken;

    private final UserMapper userMapper = UserMapper.getInstance();


    public UserServiceImpl(com.elbuensabor.api.repository.IGenericRepository<User, Long> IGenericRepository, GenericMapper<User, UserDTO> genericMapper, IUserRepository userRepository) {
        super(IGenericRepository, genericMapper);
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public ResponseEntity<User> saveUser(Auth0UserDTO dto) {
        try {
            String token = serviceToken.getAuth0Token();
            String url = "https://" + serviceToken.getDomain() + "/api/v2/users";

            JsonObject userObject = new JsonObject();
            userObject.addProperty("connection", "Username-Password-Authentication");
            userObject.addProperty("email", dto.getEmail());
            userObject.addProperty("password", dto.getPassword());
            userObject.addProperty("blocked", dto.isBlocked());
            JsonObject appMetadata = new JsonObject();
            appMetadata.addProperty("isManualCreation", true);
            userObject.add("app_metadata", appMetadata);
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), userObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            HttpStatus httpStatus = HttpStatus.valueOf(response.code());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            User user = userMapper.Auth0toEntity(dto);
            user.setIdAuth0User(jsonNode.get("user_id").asText());
            userRepository.save(user);
            return ResponseEntity.status(httpStatus).body(assignUserToRole(user.getIdAuth0User(),user.getRole()));
        } catch (Exception e) {
            System.out.println("Error creating new user: " + e );
            e.printStackTrace();
            return null;
        }
    };



    @Override
    @Transactional
    public User blockedStatus(String id,boolean blocked) {
        try {
            String token = serviceToken.getAuth0Token();
            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            String url = "https://" + serviceToken.getDomain() + "/api/v2/users/" + encodedUserId;
            OkHttpClient client = new OkHttpClient();
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("blocked", blocked);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .patch(body)
                    .build();

            client.newCall(request).execute();
             User user = userRepository.findByIdAuth0User(id).orElseThrow(() -> new Exception("User not found"));
             user.setBlocked(blocked);

            return userRepository.save(user);
        } catch (Exception e) {

            System.out.println("Error update status user: " + e );
            e.printStackTrace();
            return null;
        }
    };
    @Transactional(readOnly = true)
    public Long getLoginsCount(String id) {
        try {
            String token = serviceToken.getAuth0Token();
            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            String url = "https://" + serviceToken.getDomain() + "/api/v2/users/" + encodedUserId;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            JsonNode loginsCountNode = jsonNode.path("logins_count");
            Long count = loginsCountNode.isMissingNode() || loginsCountNode.isNull() ? 0 : loginsCountNode.asLong();
            return count;
        } catch (Exception e) {
            System.out.println("Error get Logins Count user: " + e );
            e.printStackTrace();
            return null;
        }
    };
    @Transactional
    public User changeUserPassword(String id,String password) {
        try {
            String token = serviceToken.getAuth0Token();
            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            String url = "https://" + serviceToken.getDomain() + "/api/v2/users/" + encodedUserId;

            OkHttpClient client = new OkHttpClient();

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("password", password);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .patch(body)
                    .build();

            client.newCall(request).execute();
            User user = userRepository.findByIdAuth0User(id).orElseThrow(() -> new Exception("User not found"));
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error get Logins Count user: " + e );
            e.printStackTrace();
            return null;
        }
    };
    @Transactional
    public User assignUserToRole(String id, Role role) {
        try {
            String token = serviceToken.getAuth0Token();
            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            String url = "https://" + serviceToken.getDomain() + "/api/v2/users/" + encodedUserId + "/roles";
            OkHttpClient client = new OkHttpClient();
            JsonObject requestBody = new JsonObject();
            JsonArray rolesArray = new JsonArray();
            rolesArray.add(role.getIdAuth0Role());
            requestBody.add("roles", rolesArray);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            HttpStatus httpStatus = HttpStatus.valueOf(response.code());
            User user = userRepository.findByIdAuth0User(id).orElseThrow(() -> new Exception("User not found"));
            user.setRole(role);
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error assigning user to role = "+ e );
            e.printStackTrace();
            return null;
        }
    };

    @Transactional
    public User deleteRolesFromUser(String id, String[] rolesId) {
        try {
            String token = serviceToken.getAuth0Token();
            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            String url = "https://" + serviceToken.getDomain() + "/api/v2/users/" + encodedUserId + "/roles";

            OkHttpClient client = new OkHttpClient();
            JsonObject requestBody = new JsonObject();

            JsonArray rolesArray = new JsonArray();
            for (String role : rolesId) {
                rolesArray.add(role);
            }
            requestBody.add("roles", rolesArray);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .delete(body)
                    .build();

            client.newCall(request).execute();

            User user = userRepository.findByIdAuth0User(id).orElseThrow(() -> new Exception("User not found"));
            user.getRole().setIdAuth0Role("");
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error DELETE role from user = "+ e );
            e.printStackTrace();
            return null;
        }
    };

    @Transactional(readOnly = true)
    public String getUserApiAuth0(String id ) {
        try {
            String token = serviceToken.getAuth0Token();
            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            String url = "https://" + serviceToken.getDomain() + "/api/v2/users/" + encodedUserId;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            System.out.println("Error get User from Auth0 = "+ e );
            e.printStackTrace();
            return null;
        }
    };

    @Transactional
    public void logIn(String id) throws Exception {
        try {
//            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            User user = userRepository.findByIdAuth0User(id).orElseThrow(() -> new Exception("User not found"));
            user.setLogged(true);
            userRepository.save(user);
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }
    @Transactional
    public void logOut(String id) throws Exception {
        try {
//            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            User user = userRepository.findByIdAuth0User(id).orElseThrow(() -> new Exception("User not found"));
            user.setLogged(false);
            userRepository.save(user);
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }
    @Override
    public boolean checkEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


    @Transactional(readOnly = true)
    public Long getLastUserId() throws Exception {
        try {
            return userRepository.findLastUserId();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Role getUserRolByAuth0Id(String userId) throws Exception {
        try {
            User user = userRepository.findByIdAuth0User(userId).orElseThrow(() -> new Exception("User not found"));
            return user.getRole();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserbyAuth0Id(String userId) throws Exception {
        try {
            User user = userRepository.findByIdAuth0User(userId).orElseThrow(() -> new Exception("User not found"));
            return userMapper.toDTO(user);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public String updateUserPicture(String id ,String picture) {
        try {
            String token = serviceToken.getAuth0Token();
            String encodedUserId = URLEncoder.encode(id, StandardCharsets.UTF_8) .replaceAll("\\|", "%7C");
            String url = "https://" + serviceToken.getDomain() + "/api/v2/users/" + encodedUserId;

            OkHttpClient client = new OkHttpClient();
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("picture", picture);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .patch(body)
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            System.out.println("Error update User picture Auth0 "+ e );
            e.printStackTrace();
            return null;
        }
    };



    @Override
    public OrderDTO updateOrderState(Long id, String newState) throws Exception {
        return null;
    }
}
