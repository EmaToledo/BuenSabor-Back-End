package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.Enum.OrderStatus;
import com.elbuensabor.api.Enum.PaymentStatus;
import com.elbuensabor.api.dto.*;
import com.elbuensabor.api.entity.*;
import com.elbuensabor.api.mapper.*;
import com.elbuensabor.api.repository.*;
import com.elbuensabor.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private StockService stockService;
    @Autowired
    private BillService billService;
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
    public List<OrderDTO> getAllOrders() throws Exception {
        try {
            List<Order> orders = orderRepository.findAll();
            List<OrderDTO> ordersDTOList = new ArrayList<>();
            for (Order order : orders) {
                OrderDTO orderDTO = this.orderMapper.toDTO(order);

                orderDTO.setOrderDetails(getOrderDetailDTOList(order));
                ordersDTOList.add(orderDTO);
            }
            return ordersDTOList;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrdersByUserId(Long id) throws Exception {
        try {
            List<Order> orders = orderRepository.findAllByUserId(id);
            List<OrderDTO> ordersDTOList = new ArrayList<>();
            for (Order order : orders) {
                OrderDTO orderDTO = this.orderMapper.toDTO(order);
                orderDTO.setOrderDetails(orderDetailService.mapperToDtoListComplete(order.getOrderDetails()));
                ordersDTOList.add(orderDTO);
            }
            return ordersDTOList;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long id) throws Exception {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new Exception("Orden no encontrado con el ID: " + id));

            OrderDTO ordersDTO = orderMapper.toDTO(order);

            ordersDTO.setOrderDetails(getOrderDetailDTOList(order));
            return ordersDTO;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // todo -> tiempo estimado
    @Override
    @Transactional
    public OrderDTO saveOrder(OrderDTO dto) throws Exception {
        try {
            System.out.println();
            List<OrderDetailDTO> orderDetails = dto.getOrderDetails();

            Order order = orderMapper.toEntity(dto);
            if (dto.getUserId() != null) {
                order.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new Exception("User not found")));
            }
            order.setDateTime(LocalDateTime.now());

            // se verifica que la cantidad pedida se corresponda a la cantidad de stock actual y se reduce si es posible
            if (stockService.verifAndDiscountOrAddStock(dto.getOrderDetails(), 'R')) {
                Order savedOrder = orderRepository.save(order);

                savedOrder.setOrderDetails(orderDetailService.saveOrderDetails(orderDetails, savedOrder));
                dto = orderMapper.toDTO(savedOrder);

                dto.setOrderDetails(orderDetailService.mapperToDtoListComplete(savedOrder.getOrderDetails()));
            } else {
                throw new Exception("Error, no hay suficiente stock");
            }
            return dto;
        } catch (Exception e) {
            throw new Exception("Error saving order: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO dto) throws Exception {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new Exception("Orden no encontrada con el ID: " + id));
            order.setDiscount(dto.getDiscount());
            order.setEstimatedTime(dto.getEstimatedTime());
            order.setPaymentType(dto.getPaymentType());
            order.setTotal(dto.getTotal());
            order.setDeliveryMethod(dto.getDeliveryMethod());
            order.setPaymentType(dto.getPaymentType());
            order = orderRepository.save(order);
            // Maps the Order entity to DTO
            dto = orderMapper.toDTO(order);
            dto.setOrderDetails(orderDetailService.mapperToDtoListComplete(order.getOrderDetails()));

            return dto;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // todo -> Testing
    @Override
    @Transactional
    public OrderDTO cancelOrder(Long id) throws Exception {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new Exception("Orden no encontrado con el ID: " + id));
//            if (order.getPaymentType().equals("mp")) {
//                if (order.getPaid() == PaymentStatus.approved) {
//                    paymentMarketService.fullRefundPayment(id);
//                } else if (order.getPaid() == PaymentStatus.in_process) {
//                    paymentMarketService.cancelPayment(id);
//                }
//            }
            billService.cancelBill(order.getId());
            order.setCanceled(true);
            // devuelve el stock de la orden cancelada
            stockService.verifAndDiscountOrAddStock(getOrderDetailDTOList(order), 'A');
            order.setState(OrderStatus.CANCELED);
            return orderMapper.toDTO(orderRepository.save(order));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private List<OrderDetailDTO> getOrderDetailDTOList(Order order) {
        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();

        if (order != null && order.getOrderDetails() != null) {
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (orderDetail != null) { // Verifica que orderDetail no sea nulo
                    OrderDetailDTO orderDetailDTO = this.orderDetailMapper.toDTO(orderDetail);

                    // Verificacion de si el producto es nulo
                    if ((orderDetail.getProduct() != null)) {
                        orderDetailDTO.setItemProduct(productMapper.toDTO(orderDetail.getProduct()));
                    }

                    // Verificacion de si el manufacturado es nulo
                    if (orderDetail.getManufacturedProduct() != null) {
                        orderDetailDTO.setItemManufacturedProduct(manufacturedProductMapper.toDTO(orderDetail.getManufacturedProduct()));
                    }

                    orderDetailDTOList.add(orderDetailDTO);
                }
            }
        } else {
            System.out.println("La orden es nula o no tiene detalles.");
        }

        return orderDetailDTOList;
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
