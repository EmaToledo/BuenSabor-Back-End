package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.Enum.OrderStatus;
import com.elbuensabor.api.Enum.PaymentStatus;
import com.elbuensabor.api.dto.*;
import com.elbuensabor.api.entity.*;
import com.elbuensabor.api.mapper.*;
import com.elbuensabor.api.repository.*;
import com.elbuensabor.api.service.OrderDetailService;
import com.elbuensabor.api.service.OrderService;
import com.elbuensabor.api.service.PaymentMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl extends GenericServiceImpl<Order, OrderDTO, Long> implements OrderService {
    public OrderServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Order, Long> IGenericRepository, GenericMapper<Order, OrderDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }
    private final OrderMapper orderMapper = OrderMapper.getInstance();
    private final OrderDetailMapper orderDetailMapper = OrderDetailMapper.getInstance();
    private final ProductMapper productMapper = ProductMapper.getInstance();
    private final ManufacturedProductMapper manufacturedProductMapper = ManufacturedProductMapper.getInstance();
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IOrderDetailRepository orderDetailRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IManufacturedProductRepository manufacturedProductRepository;
    @Autowired
    private IPriceRepository priceRepository;
    @Autowired
    private PaymentMarketService paymentMarketService;
    @Override
    @Transactional(readOnly = true)
    public  List<OrderDTO> getAllOrders() throws Exception{
        try {
            List<Order> orders = orderRepository.findAll();
            List<OrderDTO> ordersDTOList = new ArrayList<>();
            for (Order order: orders) {
                OrderDTO orderDTO = this.orderMapper.toDTO(order);
                List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
                for (OrderDetail orderDetail: order.getOrderDetails()) {
                    OrderDetailDTO orderDetailDTO = this.orderDetailMapper.toDTO(orderDetail);
                    orderDetailDTO.setItemProduct(productMapper.toDTO(orderDetail.getProduct()));
                    orderDetailDTO.setItemManufacturedProduct(manufacturedProductMapper.toDTO(orderDetail.getManufacturedProduct()));
                    orderDetailDTOList.add(orderDetailDTO);
                }
                orderDTO.setOrderDetails(orderDetailDTOList);
                ordersDTOList.add(orderDTO);
            }
            return ordersDTOList;
        }catch (Exception e){
        throw new Exception(e.getMessage());
        }
    }
    @Override
    @Transactional(readOnly = true)
    public  List<OrderDTO> getAllOrdersByUserId(Long id) throws Exception{
        try {
            List<Order> orders = orderRepository.findAllByUserId(id);
            List<OrderDTO> ordersDTOList = new ArrayList<>();
            for (Order order: orders) {
                OrderDTO orderDTO = this.orderMapper.toDTO(order);
                orderDTO.setOrderDetails(orderDetailService.mapperToDtoListComplete(order.getOrderDetails()));
                ordersDTOList.add(orderDTO);
            }
            return ordersDTOList;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    @Override
    @Transactional(readOnly = true)
    public  OrderDTO getOrder(Long id) throws Exception{
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new Exception("Orden no encontrado con el ID: " + id));

            OrderDTO ordersDTO = orderMapper.toDTO(order);
            List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                OrderDetailDTO orderDetailDTO = this.orderDetailMapper.toDTO(orderDetail);
                orderDetailDTO.setItemProduct(productMapper.toDTO(orderDetail.getProduct()));
                orderDetailDTO.setItemManufacturedProduct(manufacturedProductMapper.toDTO(orderDetail.getManufacturedProduct()));
                orderDetailDTOList.add(orderDetailDTO);
            }
            ordersDTO.setOrderDetails(orderDetailDTOList);
            return  ordersDTO;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    /*
    En el metodo  saveOrder falta Calulculo de Stock y de Tiempo estimado
    **/
    @Override
    @Transactional
    public OrderDTO saveOrder(OrderDTO dto) throws Exception {
        try {
            Order order = orderMapper.toEntity(dto);
            if (dto.getUserId() != null) {
                order.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new Exception("User not found")));
            }
            order.setDateTime(LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);
            savedOrder.setOrderDetails(orderDetailService.saveOrderDetails(dto.getOrderDetails(), savedOrder));
            dto = orderMapper.toDTO(savedOrder);
            dto.setOrderDetails(orderDetailService.mapperToDtoListComplete(savedOrder.getOrderDetails()));
            return dto;
        } catch (Exception e) {
            throw new Exception("Error saving order: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public  OrderDTO updateOrder(Long id, OrderDTO dto) throws Exception{
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new Exception("Orden no encontrada con el ID: " + id));
            order.setDiscount(dto.getDiscount());
            order.setEstimatedTime(dto.getEstimatedTime());
            order.setPaymentType(dto.getPaymentType());
            order.setTotal(dto.getTotal());
            order.setDeliveryMethod(dto.getDeliveryMethod());
            order.setPaymentType(dto.getPaymentType());
            // Update the order details collection
            List<OrderDetail> newtOrderDetails = new ArrayList<>(orderDetailService.updateOrderDetails(dto.getOrderDetails(),order));
            order.getOrderDetails().clear();
            order.getOrderDetails().addAll(newtOrderDetails);
            //Save the Order entity
            order = orderRepository.save(order);
            // Maps the Order entity to DTO
            dto = orderMapper.toDTO(order);
            dto.setOrderDetails(orderDetailService.mapperToDtoListComplete(order.getOrderDetails()));
            return dto;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    /*Falta Calulo del Stock*/
    @Override
    @Transactional
    public  OrderDTO cancelOrder(Long id) throws Exception{
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new Exception("Orden no encontrado con el ID: " + id));
if (order.getPaymentType().equals("mp")){
    if (order.getPaid() == PaymentStatus.approved){
     paymentMarketService.fullRefundPayment(id);
    }
}
if (order.getPaid() == PaymentStatus.in_process){
            paymentMarketService.cancelPayment(id);
}
            order.setCanceled(true);
            order.setState(OrderStatus.CANCELED);
            return orderMapper.toDTO(orderRepository.save(order));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDTO> getCartItemsList() {
        List<ManufacturedProduct> manufacturedProductList = manufacturedProductRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        Long fatherCategoryId = null;
        for (ManufacturedProduct manufacturedProduct : manufacturedProductList) {
            Double price = priceRepository.findPriceIdByIdManufacturedProduct(manufacturedProduct.getId()).getSellPrice();
            if (manufacturedProduct.getManufacturedProductCategory().getFatherCategory() != null){
                fatherCategoryId = manufacturedProduct.getManufacturedProductCategory().getFatherCategory().getId();
            };
            CartItemDTO cartItemDTO = new CartItemDTO(
                    manufacturedProduct.getDenomination(),
                    manufacturedProduct.getCookingTime(),
                    fatherCategoryId,
                    price,
                    manufacturedProduct.getManufacturedProductCategory().getId()
            );
            cartItemDTO.setId(manufacturedProduct.getId());
            cartItemDTOS.add(cartItemDTO);
            fatherCategoryId = null;
        }

        for (Product product : products) {
            if (product.getProductCategory().getFatherCategory() != null){
                fatherCategoryId = product.getProductCategory().getFatherCategory().getId();
            };
            Double price = priceRepository.findPriceIdByIdProduct(product.getId()).getSellPrice();
            CartItemDTO cartItemDTO = new CartItemDTO(
                    product.getDenomination(),
                    null,
                    fatherCategoryId,
                    price,
                    product.getProductCategory().getId()
            );
            cartItemDTO.setId(product.getId());
            cartItemDTOS.add(cartItemDTO);
            fatherCategoryId=null;
        }
        return cartItemDTOS;
    }
    @Override
    @Transactional(readOnly = true)
    public ItemListDTO getItemsList() {
        ItemListDTO itemListDTO = new ItemListDTO();
        itemListDTO.setManufacturedProductDTOList(manufacturedProductMapper.toDTOsList(manufacturedProductRepository.findAvailableManufacturedProducts()));
        itemListDTO.setProductDTOList(productMapper.toDTOsList(productRepository.findAvailableProducts()));
        return itemListDTO;
    }


}
