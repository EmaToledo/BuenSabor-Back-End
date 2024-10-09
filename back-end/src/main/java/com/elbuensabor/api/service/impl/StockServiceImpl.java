package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.StockDTO;
import com.elbuensabor.api.entity.*;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.StockMapper;
import com.elbuensabor.api.mapper.ManufacturedProductMapper;
import com.elbuensabor.api.repository.IIngredientRepository;
import com.elbuensabor.api.repository.IManufacturedProductRepository;
import com.elbuensabor.api.repository.IProductRepository;
import com.elbuensabor.api.repository.IStockRepository;
import com.elbuensabor.api.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final ManufacturedProductMapper manufacturedProductMapper = ManufacturedProductMapper.getInstance();

    private static final char STOCK_RELATION_TYPE_PRODUCT = 'p';
    private static final char STOCK_RELATION_TYPE_INGREDIENT = 'I';

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
    public StockDTO getStock(Long id) throws Exception {
        try {
            Stock stock = iStockRepository.findById(id).orElseThrow(() -> new Exception("No se ha encontrado el stock individual, id: " + id));
            return stockMapper.toDTO(stock);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<StockDTO> getAllStock() throws Exception {
        try {
            List<Stock> stocks = iStockRepository.findAll();
            List<StockDTO> stockDTOs = stockMapper.toDTOsList(stocks);
            return stockDTOs;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
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
    public Stock update(Long id, Long minStock) throws Exception {
        try {
            Stock stock = iStockRepository.findById(id).orElseThrow(() -> new Exception("No se ha encontrado el stock con id: " + id));
            stock.setMinStock(minStock);
            return iStockRepository.save(stock);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Stock addStock(Long id, StockDTO dto) throws Exception {
        try {
            Stock stock = iStockRepository.findById(dto.getId()).orElseThrow(() -> new Exception("No se ha encontrado el stock con id: " + id));
            stock.setActualStock(dto.getActualStock());
            return iStockRepository.save(stock);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private boolean reduceStockByManufacturedProduct(Long id) throws Exception {
        try {
            ManufacturedProduct manufacturedProduct = iManufacturedProductRepository.findById(id).orElseThrow(() -> new Exception("No se ha encontrado el producto manufacturado con id: " + id));
            List<IngredientRecipeLink> ingredientRecipeLinks = iManufacturedProductRepository.findManufacturedProductIngredients(manufacturedProduct.getId());

            for (IngredientRecipeLink recipeLink : ingredientRecipeLinks) {
                Long ingredientID = recipeLink.getIngredient().getId();
                Stock stock = iStockRepository.findIngredientStock(ingredientID);
                // reducir el stock y guradarlo denuevo
                // verificar si el stock es menor al minimo con una funcion que se utilizara luego en otros lados
                // la funcion mencionada arriba se va a utilizar para hacer peticiones de stock que se deberan aceptar o no
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        // para que funcione el proyecto esto es temporal
        return true;
    }

    // se deberia pedir un id de un producto manufacturado, a este se le deberia llamar con la query
    // todos los ingredientes de dicho PM y se deberia revisar el stock de cada uno, esto se llamaria cada
    // vez que se pida un producto manufacturado o nose que otra logica podria tener para que se revise siempre el stock
    // se deberia crear una peticion si el stock es bajo y nose si deberia ser otro modulo pero mas sencillo
    // tambien hay que hacer que si no se ha aceptado la peticion y es menor que el stock minimo, se deberia dar de baja el PM

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

}
