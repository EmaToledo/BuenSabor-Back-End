package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.Enum.OrderStatus;
import com.elbuensabor.api.dto.OrderDetailDTO;
import com.elbuensabor.api.dto.StockDTO;
import com.elbuensabor.api.entity.*;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.StockMapper;
import com.elbuensabor.api.repository.*;
import com.elbuensabor.api.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockServiceImpl extends GenericServiceImpl<Stock, StockDTO, Long> implements StockService {

    @Autowired
    IStockRepository iStockRepository;

    @Autowired
    IProductRepository iProductRepository;

    @Autowired
    IIngredientRepository iIngredientRepository;

    @Autowired
    IManufacturedProductRepository iManufacturedProductRepository;

    private final StockMapper stockMapper = StockMapper.getInstance();

    private final char STOCK_RELATION_TYPE_INGREDIENT = 'M';
    private final char STOCK_RELATION_TYPE_PRODUCT = 'P';

    private final char STOCK_REDUCE_TYPE = 'R';
    private final char STOCK_ADD_TYPE = 'A';

    /**
     * Constructor de la clase.
     *
     * @param IGenericRepository Repositorio genérico utilizado para acceder a los datos de la entidad Stock.
     * @param genericMapper      Mapeador genérico utilizado para convertir entre la entidad Stock y el DTO StockDTO.
     */
    public StockServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Stock, Long> IGenericRepository, GenericMapper<Stock, StockDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    @Override
    @Transactional
    public Stock saveStock(StockDTO dto, Character type, Long relationId) throws Exception {
        try {
            Stock stock = stockMapper.toEntity(dto);
            setIdRelationsIfExists(stock, type, relationId);
            return iStockRepository.save(stock);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Stock update(Long id, StockDTO dto) throws Exception {
        try {
            Stock stock = iStockRepository.findById(id).orElseThrow(() -> new Exception("No se ha encontrado el stock con id: " + id));
            stock.setMinStock(dto.getMinStock());
            stock.setActualStock(dto.getActualStock());
            return iStockRepository.save(stock);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void setIdRelationsIfExists(Stock stock, Character relationType, Long relationId) throws Exception {
        try {
            if (relationType.equals(STOCK_RELATION_TYPE_INGREDIENT)) {
                Ingredient ingredient = iIngredientRepository.findById(relationId).orElseThrow(() -> new Exception("El ingrediente con id " + relationId + " no existe"));
                stock.setIngredientStock(ingredient);
                stock.setProductStock(null);
            } else if (relationType.equals(STOCK_RELATION_TYPE_PRODUCT)) {
                Product product = iProductRepository.findById(relationId).orElseThrow(() -> new Exception("El producto con id " + relationId + " no existe"));
                stock.setProductStock(product);
                stock.setIngredientStock(null);
            } else {
                throw new Exception("Error al relacionar el stock con un producto o ingrediente");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    // verifica si es posible la orden segun el stock actual y si es posible lo reduce
    public boolean verifAndDiscountStock(List<OrderDetailDTO> orderDetailsDtos) throws Exception {
        boolean verifIngredientStock, verifProductStock;

        // se distinguen los order details por productos y manufacturados para hacer su respectivo descuento de stock
        List<OrderDetailDTO> productsDetailsList = distinctOrderDetailsByProductsAndManufactured(orderDetailsDtos, 'P');
        List<OrderDetailDTO> manufacturedDetailsList = distinctOrderDetailsByProductsAndManufactured(orderDetailsDtos, 'M');

        // map que contendra el id ingrediente y la cantidad
        Map<Long, Long> ingredientQuantities = setMapManufacturedProduct(manufacturedDetailsList);
        // map que contendra el id producto y la cantidad
        Map<Long, Long> productQuantities = setMapProducts(productsDetailsList);

        // se verifica que haya el stock necesario para la orden
        verifIngredientStock = verifActualStockAndOrderQuantity(ingredientQuantities, 'M');
        verifProductStock = verifActualStockAndOrderQuantity(productQuantities, 'P');

        if (verifIngredientStock && verifProductStock) {
            reduceOrAddStock(ingredientQuantities, STOCK_RELATION_TYPE_INGREDIENT, STOCK_REDUCE_TYPE); // se reduce el stock de los ingredientes del manufacturado
            reduceOrAddStock(productQuantities, STOCK_RELATION_TYPE_PRODUCT, STOCK_REDUCE_TYPE); // se redude el stock de los productos
        } else {
            throw new Exception("No hay suficiente stock para la orden");
        }
        return true;
    }

    // settea los keys y values del map de manufacturados con sus ids de ingrediente y sus cantidades
    public Map<Long, Long> setMapManufacturedProduct(List<OrderDetailDTO> manufacturedOrderDetails) {
        Map<Long, Long> ingredientQuantities = new HashMap<>();

        // fixme -> se podria usar stream para la carga de datos y mapearlos para mas rendimiento pero esta bien por el momento
        // setting de los valores de cada map, ingredienteID / cantidad
        for (OrderDetailDTO manufacturedOrderDetail : manufacturedOrderDetails) {
            List<IngredientRecipeLink> ingredientRecipeLinks = iManufacturedProductRepository.findManufacturedProductIngredients(manufacturedOrderDetail.getItemManufacturedProduct().getId());
            for (IngredientRecipeLink recipeLink : ingredientRecipeLinks) {
                Long ingredientID = recipeLink.getIngredient().getId();
                // se obtiene la cantidad del recipe link y se multiplica por la cantidad de productos manufacturados pedidos
                Long quantity = recipeLink.getQuantity() * manufacturedOrderDetail.getQuantity();
                ingredientQuantities.put(ingredientID, ingredientQuantities.getOrDefault(ingredientID, 0L) + quantity);
            }
        }

        return ingredientQuantities;
    }

    // settea los keys y values del map de productos con sus ids y sus cantidades
    public Map<Long, Long> setMapProducts(List<OrderDetailDTO> productOrderDetails) {
        Map<Long, Long> productQuantities = new HashMap<>();

        // setting de los valores de cada map, ProductID / cantidad
        for (OrderDetailDTO productOrderDetail : productOrderDetails) {
            Long productID = productOrderDetail.getItemProduct().getId();
            // se obtiene la cantidad del order y se multiplica por la cantidad de productos pedidos
            Long quantity = Long.parseLong(productOrderDetail.getQuantity().toString());

            productQuantities.put(productID, productQuantities.getOrDefault(productID, 0L) + quantity);
        }

        return productQuantities;
    }

    private List<OrderDetailDTO> distinctOrderDetailsByProductsAndManufactured(List<OrderDetailDTO> orderDetails, Character type) throws Exception {
        List<OrderDetailDTO> tempOrderDetailsList = new ArrayList<>();
        System.out.println("Total Order Details: " + orderDetails.size());

        for (OrderDetailDTO orderDetail : orderDetails) {
            {// verifica los order detail y los agrupa segun los ids

                if (orderDetail.getItemManufacturedProduct() != null && orderDetail.getItemProduct() != null) {
                    throw new Exception("Error inesperado, hay dos relaciones en el mismo order detail");
                } else if (orderDetail.getItemProduct() != null && type.equals(STOCK_RELATION_TYPE_PRODUCT)) {
                    tempOrderDetailsList.add(orderDetail);
                } else if (orderDetail.getItemManufacturedProduct() != null && type.equals(STOCK_RELATION_TYPE_INGREDIENT)) {
                    tempOrderDetailsList.add(orderDetail);
                }
            }
        }
        return tempOrderDetailsList;
    }

    // verifica el stock actual y lo pedido en la orden para evitar errores de falta de stock
    public boolean verifActualStockAndOrderQuantity(Map<Long, Long> quantities, char type) throws Exception {
        for (Map.Entry<Long, Long> entryTest : quantities.entrySet()) {
            Long relationID = entryTest.getKey();
            Long totalQuantity = entryTest.getValue();

            if (relationID == null) {
                throw new Exception("Error inesperado: el ID de la relacion no se encuentra en la base de datos");
            }

            Stock stock = (type == STOCK_RELATION_TYPE_INGREDIENT)
                    ? iStockRepository.findIngredientStock(relationID)
                    : iStockRepository.findProductStock(relationID);


            if (stock == null || stock.getActualStock() <= totalQuantity) {
                return false;
            }
        }
        return true;
    }

    // todo -> Metodo que verifica si todos los ingredientes se encuentran arriba del stock minimo, sino que se de de baja luego de utilizarlos en una orden
    public Stock verifMinStock(Long id) throws Exception {
        try {
            return new Stock();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // reduce el stock mediante un map que contiene ids de productos o ingredientes de manufacturado y sus respectivas cantidades de la orden
    public void reduceOrAddStock(Map<Long, Long> orderQuantities, char type, char reduceOrAddType) throws Exception {
        try {
            // se obtienen las entrys del map y consecutivamente se reduce el stock
            for (Map.Entry<Long, Long> entry : orderQuantities.entrySet()) {
                Long relationID = entry.getKey();
                Long totalQuantity = entry.getValue();

                // verificacion por si la key del map quedo en null
                if (relationID == null) {
                    throw new Exception("El id de la relacion con el stock es nulo");
                }
                System.out.println("antes de crear stock");
                Stock stock = (type == STOCK_RELATION_TYPE_INGREDIENT)
                        ? iStockRepository.findIngredientStock(relationID)
                        : iStockRepository.findProductStock(relationID);
                System.out.println("despues de crear stock");
                // verificacion por si el stock quedo en null
                if (stock == null) {
                    System.out.println("El id de la relacion con el stock es nulo");
                    throw new Exception("El id de la relacion con el stock es nulo");
                }

                System.out.println("antes de entrar a la verificacion, reduceoraddtype: " + reduceOrAddType + " - STOCK_ADD_TYPE: " + STOCK_ADD_TYPE);
                if (reduceOrAddType == STOCK_REDUCE_TYPE) {
                    stock.setActualStock(stock.getActualStock() - totalQuantity);
                } else if (reduceOrAddType == STOCK_ADD_TYPE) {
                    stock.setActualStock(stock.getActualStock() + totalQuantity);
                    System.out.println("se agrego al stock id: " + stock.getId() + " - La cantidad: " + totalQuantity);
                }
                iStockRepository.save(stock);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean reStock(List<OrderDetailDTO> orderDetailDTOList) throws Exception {
        try {
            List<OrderDetailDTO> manufacturedProductOrderDetailList = new ArrayList<>();
            List<OrderDetailDTO> productOrderDetailList = new ArrayList<>();

            // Listas filtradas de detalles de la orden
            manufacturedProductOrderDetailList = distinctOrderDetailsByProductsAndManufactured(orderDetailDTOList, STOCK_RELATION_TYPE_INGREDIENT);
            productOrderDetailList = distinctOrderDetailsByProductsAndManufactured(orderDetailDTOList, STOCK_RELATION_TYPE_PRODUCT);

            Map<Long, Long> manufacturedQuantities = setMapManufacturedProduct(manufacturedProductOrderDetailList);
            Map<Long, Long> productQuantities = setMapProducts(productOrderDetailList);

            // Agrega denuevo los stocks utilizados por la orden cancelada
            if (OrderStatus.PENDING.equals(OrderStatus.PENDING)) {
                reduceOrAddStock(manufacturedQuantities, STOCK_RELATION_TYPE_INGREDIENT, STOCK_ADD_TYPE);
            } else {
                System.out.println("El stock no puede ser devuelto, los productos ya fueron usados");
            }

            reduceOrAddStock(productQuantities, STOCK_RELATION_TYPE_PRODUCT, STOCK_ADD_TYPE);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return true;
    }

}
