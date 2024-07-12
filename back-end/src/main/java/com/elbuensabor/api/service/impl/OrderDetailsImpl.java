package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.ManufacturedProductDTO;
import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.dto.OrderDetailDTO;
import com.elbuensabor.api.dto.ProductDTO;
import com.elbuensabor.api.entity.Order;
import com.elbuensabor.api.entity.OrderDetail;
import com.elbuensabor.api.entity.Product;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.ManufacturedProductMapper;
import com.elbuensabor.api.mapper.OrderDetailMapper;
import com.elbuensabor.api.mapper.ProductMapper;
import com.elbuensabor.api.repository.IManufacturedProductRepository;
import com.elbuensabor.api.repository.IOrderDetailRepository;
import com.elbuensabor.api.repository.IProductRepository;
import com.elbuensabor.api.service.OrderDetailService;
import com.elbuensabor.api.service.OrderService;
import com.elbuensabor.api.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailsImpl  extends GenericServiceImpl<OrderDetail, OrderDetailDTO, Long> implements OrderDetailService {
    public OrderDetailsImpl(com.elbuensabor.api.repository.IGenericRepository<OrderDetail, Long> IGenericRepository, GenericMapper<OrderDetail, OrderDetailDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }
    private final OrderDetailMapper orderDetailMapper = OrderDetailMapper.getInstance();
    private final ProductMapper productMapper = ProductMapper.getInstance();
    private final ManufacturedProductMapper manufacturedProductMapper = ManufacturedProductMapper.getInstance();

    @Autowired
    private IOrderDetailRepository orderDetailRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IManufacturedProductRepository manufacturedProductRepository;
    @Autowired
    private PriceService priceService;

    @Transactional
    @Override
    public List<OrderDetail> saveOrderDetails(List<OrderDetailDTO> listDto, Order order) throws Exception {
        try {
            List<OrderDetail> orderDetailList = new ArrayList<>();

            for (OrderDetailDTO orderDetailDTO : listDto) {
                OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);

                if (orderDetailDTO.getItemProduct() != null && productRepository.existsById(orderDetailDTO.getItemProduct().getId())) {
                    orderDetail.setProduct(productMapper.toEntity(orderDetailDTO.getItemProduct()));
                }

                if (orderDetailDTO.getItemManufacturedProduct() != null && manufacturedProductRepository.existsById(orderDetailDTO.getItemManufacturedProduct().getId())) {
                    orderDetail.setManufacturedProduct(manufacturedProductMapper.toEntity(orderDetailDTO.getItemManufacturedProduct()));
                }

                orderDetail.setOrder(order);
                orderDetailList.add(orderDetail);
            }
            return orderDetailRepository.saveAll(orderDetailList);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDetailDTO> mapperToDtoListComplete(List<OrderDetail> list) throws Exception {
        try{
            List<OrderDetailDTO> detailDTOList = new ArrayList<>();
            for (OrderDetail orderDetail: list) {
                OrderDetailDTO od = orderDetailMapper.toDTO(orderDetail);
                if(orderDetail.getProduct() != null){
                    ProductDTO productDTO = productMapper.toDTO(orderDetail.getProduct());
                    productDTO.setPrice(priceService.getOnlySellPrice(orderDetail.getProduct().getId(),1));
                    od.setItemProduct(productDTO);
                } else if (orderDetail.getManufacturedProduct() != null) {
                    ManufacturedProductDTO manufacturedProductDTO = manufacturedProductMapper.toDTO(orderDetail.getManufacturedProduct());
                    manufacturedProductDTO.setPrice(priceService.getOnlySellPrice(orderDetail.getManufacturedProduct().getId(),2));
                    od.setItemManufacturedProduct(manufacturedProductDTO);
                }
                detailDTOList.add(od);
            }
            return detailDTOList;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    @Override
    public List<OrderDetail> updateOrderDetails(List<OrderDetailDTO> listDto, Order order) throws Exception {
        try {
            List<OrderDetail> updatedOrderDetailList = new ArrayList<>();

            for (OrderDetailDTO orderDetailDTO : listDto) {
                OrderDetail orderDetail;
                // Si el detalle de la orden ya existe, lo obtenemos del repositorio
                if (orderDetailDTO.getId() != null && orderDetailRepository.existsById(orderDetailDTO.getId())) {
                    orderDetail = orderDetailRepository.findById(orderDetailDTO.getId())
                            .orElseThrow(() -> new Exception("Order detail not found"));
                } else {
                    // Si no existe, creamos uno nuevo
                    orderDetail = new OrderDetail();
                }
                // Actualizamos las propiedades del detalle de la orden
                orderDetail.setQuantity(orderDetailDTO.getQuantity());
                orderDetail.setSubtotal(orderDetailDTO.getSubtotal());

                if (orderDetailDTO.getItemProduct() != null && productRepository.existsById(orderDetailDTO.getItemProduct().getId())) {
                    orderDetail.setProduct(productMapper.toEntity(orderDetailDTO.getItemProduct()));
                }

                if (orderDetailDTO.getItemManufacturedProduct() != null && manufacturedProductRepository.existsById(orderDetailDTO.getItemManufacturedProduct().getId())) {
                    orderDetail.setManufacturedProduct(manufacturedProductMapper.toEntity(orderDetailDTO.getItemManufacturedProduct()));
                }

                orderDetail.setOrder(order);
                updatedOrderDetailList.add(orderDetail);
            }
            return  orderDetailRepository.saveAll(updatedOrderDetailList);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }



}
