package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.*;
import com.elbuensabor.api.entity.Ingredient;
import com.elbuensabor.api.entity.ManufacturedProduct;
import com.elbuensabor.api.entity.Order;
import com.elbuensabor.api.entity.Product;
import com.elbuensabor.api.service.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Getter
public class MultipleEntitiesServiceImpl implements MultipleEntitiesService {

    @Autowired
    private ManufacturedProductService manufacturedProductService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductService productService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BillService billService;

    @Autowired
    private PaymentMarketService paymentMarketService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Transactional
    public ManufacturedProduct saveManufacturedWithRecipe(ManufacturedRecipeDTO dto) throws Exception {
        try {
            ManufacturedProduct manufacturedProduct = manufacturedProductService.saveManufacturedProduct(dto.getManufacturedProduct());
            dto.getRecipe().setManufacturedProductId(manufacturedProduct.getId());
            recipeService.saveRecipe(dto.getRecipe());
            return manufacturedProduct;
        } catch (Exception e) {
            throw new Exception("Error al guardar Producto Manufacturado Completo: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Ingredient saveIngredientWithStock(IngredientDTO dto, StockDTO stockDTO) throws Exception {
        try {
            Ingredient ingredient = ingredientService.saveIngredient(dto);
            stockDTO.setIngredientStockID(ingredient.getId());
            stockService.saveStock(stockDTO, 'M', ingredient.getId());
            return ingredient;
        } catch (Exception e) {
            throw new Exception("Error al guardar Ingrediente con stock Completo: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Product saveProductWithStock(ProductDTO dto, StockDTO stockDTO) throws Exception {
        try {
            Product product = productService.saveProduct(dto);
            stockDTO.setProductStockID(product.getId());
            stockService.saveStock(stockDTO, 'P', product.getId());
            return product;
        } catch (Exception e) {
            throw new Exception("Error al guardar Producto con stock Completo: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ItemPaymentMarketDTO saveOrderWithBill(OrderDTO dto) throws Exception {
        try {
            dto = orderService.saveOrder(dto);
            BillDTO billDTO = new BillDTO();
            billDTO.setOrderId(dto.getId());
            billService.saveBill(billDTO);
            if (dto.getPaymentType().equals("mp")) {
                return paymentMarketService.savePreferenceID(dto);
            } else {
                billService.sendBillByMail(dto.getId());
                return null;
            }
        } catch (Exception e) {
            throw new Exception("Error al guardar Producto con stock Completo: " + e.getMessage(), e);
        }
    }
}
