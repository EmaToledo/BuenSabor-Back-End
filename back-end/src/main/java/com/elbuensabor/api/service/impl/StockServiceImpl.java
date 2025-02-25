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

import java.util.*;

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

    @Autowired
    IIngredientRecipeLinkRepository iIngredientRecipeLinkRepository;

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

            if (stock.getIngredientStock().getId() != null) {
                List<IngredientRecipeLink> ingredientRecipeLinksRelatedWithIngredient = iIngredientRecipeLinkRepository.findIngredientsByIngredientId(stock.getIngredientStock().getId());

                for (IngredientRecipeLink ingredientRecipeLink : ingredientRecipeLinksRelatedWithIngredient) {
                    Long manufacturedProductID = ingredientRecipeLink.getRecipe().getManufacturedProduct().getId();
                    verifAndDisableByStock(manufacturedProductID, 'M');
                }
            } else if (stock.getProductStock().getId() != null) {
                verifAndDisableByStock(stock.getProductStock().getId(), 'P');
            }


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

    public boolean bulkTransactionalChangeStock(Long categoryID, char reduceOrAddType, Long value) {
        boolean allVerif = false;

        List<Stock> stockList = new ArrayList<>();
        if (categoryID != null) {
            stockList = iStockRepository.findStockByIngredientCategory(categoryID);
            stockList.addAll(iStockRepository.findStockByProductCategory(categoryID));
        } else {
            stockList = iStockRepository.findStockByIngredientCategoryAll();
            stockList.addAll(iStockRepository.findStockByProductCategoryAll());
        }

        for (Stock actualStock : stockList) {

            if (reduceOrAddType == STOCK_REDUCE_TYPE) {
                actualStock.setActualStock(actualStock.getActualStock() - value);
                System.out.println("se quito al stock id: " + actualStock.getId() + " - " + value);
                allVerif = true;
            } else if (reduceOrAddType == STOCK_ADD_TYPE) {
                actualStock.setActualStock(actualStock.getActualStock() + value);
                System.out.println("se agrego al stock id: " + actualStock.getId() + " + " + value);
                allVerif = true;
            }
            iStockRepository.save(actualStock);
        }
        return allVerif;
    }

    // verifica si es posible la orden segun el stock actual y si es posible lo reduce
    public boolean verifAndDiscountOrAddStock(List<OrderDetailDTO> orderDetailsDtos, char reduceOrAddType) throws Exception {

        // se distinguen los order details por productos y manufacturados para hacer su respectivo descuento de stock
        List<OrderDetailDTO> productsDetailsList = distinctOrderDetailsByProductsAndManufactured(orderDetailsDtos, STOCK_RELATION_TYPE_PRODUCT);
        List<OrderDetailDTO> manufacturedDetailsList = distinctOrderDetailsByProductsAndManufactured(orderDetailsDtos, STOCK_RELATION_TYPE_INGREDIENT);

        // map que contendra el id ingrediente y la cantidad
        Map<Long, Long> ingredientQuantities = setMapManufacturedProduct(manufacturedDetailsList);
        // map que contendra el id producto y la cantidad
        Map<Long, Long> productQuantities = setMapProducts(productsDetailsList);

        if (reduceOrAddType == STOCK_REDUCE_TYPE) { // si el stock es suficiente se reduce
            reduceOrAddStock(ingredientQuantities, STOCK_RELATION_TYPE_INGREDIENT, STOCK_REDUCE_TYPE); // se reduce el stock de los ingredientes del manufacturado
            reduceOrAddStock(productQuantities, STOCK_RELATION_TYPE_PRODUCT, STOCK_REDUCE_TYPE); // se redude el stock de los productos
            verifAndDisableProductOrManufactured(manufacturedDetailsList, productQuantities); // verifica stock y habilita o deshabilita segun el stock minimo
        } else if (reduceOrAddType == STOCK_ADD_TYPE) { // si hay que devolver stock
            reduceOrAddStock(ingredientQuantities, STOCK_RELATION_TYPE_INGREDIENT, STOCK_ADD_TYPE); // se agrega el stock de los ingredientes del manufacturado
            reduceOrAddStock(productQuantities, STOCK_RELATION_TYPE_PRODUCT, STOCK_ADD_TYPE); // se agrega el stock de los productos
            verifAndDisableProductOrManufactured(manufacturedDetailsList, productQuantities); // verifica stock y habilita o deshabilita segun el stock minimo
        } else {
            throw new Exception("Ha ocurrido un error");
        }
        return true;
    }

    public Map<Long, Long> setMapForBulkTransactionall(List<Stock> stockList, Long value) {
        Map<Long, Long> resultStockList = new HashMap<>();

        for (Stock individualStock : stockList) {
            Long stockID = individualStock.getId();
            // se obtiene la cantidad del recipe link y se multiplica por la cantidad de productos manufacturados pedidos
            Long quantity = value;
            resultStockList.put(stockID, quantity);
        }

        return resultStockList;
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

    public boolean verifActualStockAndQuantity(List<OrderDetailDTO> orderDetailDTOList) throws Exception {
        boolean ingredientQuantity;
        boolean productQuantity;
        // se distinguen los order details por productos y manufacturados para hacer su respectivo descuento de stock
        List<OrderDetailDTO> productsDetailsList = distinctOrderDetailsByProductsAndManufactured(orderDetailDTOList, STOCK_RELATION_TYPE_PRODUCT);
        List<OrderDetailDTO> manufacturedDetailsList = distinctOrderDetailsByProductsAndManufactured(orderDetailDTOList, STOCK_RELATION_TYPE_INGREDIENT);

        // map que contendra el id ingrediente y la cantidad
        Map<Long, Long> ingredientQuantities = setMapManufacturedProduct(manufacturedDetailsList);
        // map que contendra el id producto y la cantidad
        Map<Long, Long> productQuantities = setMapProducts(productsDetailsList);

        ingredientQuantity = verifActualStockAndOrderQuantity(ingredientQuantities, STOCK_RELATION_TYPE_INGREDIENT);
        productQuantity = verifActualStockAndOrderQuantity(productQuantities, STOCK_RELATION_TYPE_PRODUCT);

        return (ingredientQuantity && productQuantity);
    }

    // todo --> testing
    public void verifAndDisableProductOrManufactured(List<OrderDetailDTO> manufacturedProductList, Map<Long, Long> prodcutQuantities) throws Exception {
        if (manufacturedProductList != null) {
            for (OrderDetailDTO orderDetailDTO : manufacturedProductList) {
                verifAndDisableByStock(orderDetailDTO.getItemManufacturedProduct().getId(), 'M');
            }
        }

        if (prodcutQuantities != null) {
            for (Map.Entry<Long, Long> entryProducts : prodcutQuantities.entrySet()) {
                Long productID = entryProducts.getKey();
                verifAndDisableByStock(productID, 'P');
            }
        }
    }

    public boolean verifAndDisableByStock(Long id, char type) throws Exception {
        try {
            Stock stock;
            boolean isMoreThanMinStock = true;

            if (type == STOCK_RELATION_TYPE_INGREDIENT) {
                ManufacturedProduct manufacturedProduct = iManufacturedProductRepository.findById(id).orElseThrow(() -> new Exception("No se encontro el producto manufacturado"));
                List<IngredientRecipeLink> ingredientRecipeLinks = iManufacturedProductRepository.findManufacturedProductIngredients(id);
                for (IngredientRecipeLink recipeLink : ingredientRecipeLinks) {
                    Long ingredientID = recipeLink.getIngredient().getId();

                    stock = iStockRepository.findIngredientStock(ingredientID);

                    if (stock.getActualStock() <= stock.getMinStock()) {
                        isMoreThanMinStock = false;
                        // todo -> se podria poner para que en vez de mostrar un stock como retorno, que verifique y  mande a un log los ingredientes que tengan bajo stock
                    }
                }

                manufacturedProduct.setAvailability(isMoreThanMinStock);
                iManufacturedProductRepository.save(manufacturedProduct);
            } else if (type == STOCK_RELATION_TYPE_PRODUCT) {
                stock = iStockRepository.findProductStock(id);
                Product product = iProductRepository.findById(id).orElseThrow(() -> new Exception("No se encontro el producto"));

                if (stock.getActualStock() <= stock.getMinStock()) {
                    isMoreThanMinStock = false;
                }

                product.setAvailability(isMoreThanMinStock);
                iProductRepository.save(product);
            }
            return isMoreThanMinStock;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /*
    @Transactional
    public void verifMinStockAndDisableList() {
        try {
            List<ManufacturedProduct> manufacturedProducts = iManufacturedProductRepository.findAll();

            for (ManufacturedProduct manufacturedProduct : manufacturedProducts) {
                boolean isMoreThanMinStock = true;

                // Se obtienen los recipeLinks para la verificacion de stock de cada ingrediente
                List<IngredientRecipeLink> ingredientRecipeLinks = iManufacturedProductRepository.findManufacturedProductIngredients(manufacturedProduct.getId());

                for (IngredientRecipeLink recipeLink : ingredientRecipeLinks) {
                    Long ingredientId = recipeLink.getIngredient().getId();
                    Stock stock = iStockRepository.findIngredientStock(ingredientId);

                    if (stock.getActualStock() <= stock.getMinStock()) {
                        isMoreThanMinStock = false;
                    }
                }

                manufacturedProduct.setAvailability(isMoreThanMinStock);
                iManufacturedProductRepository.save(manufacturedProduct);
            }

            // Verificacion de todos los productos
            List<Product> products = iProductRepository.findAll();
            for (Product product : products) {
                Stock stock = iStockRepository.findProductStock(product.getId());

                if (stock.getActualStock() <= stock.getMinStock()) {
                    product.setAvailability(false);
                } else {
                    product.setAvailability(true);
                }

                iProductRepository.save(product);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el stock y deshabilitar el producto: " + e.getMessage(), e);
        }
    }
    */

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
                Stock stock = (type == STOCK_RELATION_TYPE_INGREDIENT)
                        ? iStockRepository.findIngredientStock(relationID)
                        : iStockRepository.findProductStock(relationID);
                // verificacion por si el stock quedo en null
                if (stock == null) {
                    throw new Exception("El id de la relacion con el stock es nulo");
                }

                if (reduceOrAddType == STOCK_REDUCE_TYPE) {
                    stock.setActualStock(stock.getActualStock() - totalQuantity);
                    System.out.println("llega hasta aca y la cuenta des tock es: " + stock.getActualStock() + "-" + totalQuantity);
                } else if (reduceOrAddType == STOCK_ADD_TYPE) {
                    stock.setActualStock(stock.getActualStock() + totalQuantity);
                }
                iStockRepository.save(stock);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}
