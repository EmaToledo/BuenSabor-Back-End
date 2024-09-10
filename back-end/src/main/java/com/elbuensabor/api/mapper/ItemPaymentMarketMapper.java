package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.ItemPaymentMarketDTO;
import com.elbuensabor.api.dto.PriceDTO;
import com.elbuensabor.api.entity.ItemPaymentMarket;
import com.elbuensabor.api.entity.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItemPaymentMarketMapper extends GenericMapper<ItemPaymentMarket,ItemPaymentMarketDTO>{
    static ItemPaymentMarketMapper getInstance() {
        return Mappers.getMapper(ItemPaymentMarketMapper.class);
    }

    // mapea una entidad ItemPaymentMarket a un DTO ItemPaymentMarketDTO
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(target = "status", ignore = true)
    ItemPaymentMarketDTO toDTO(ItemPaymentMarket source);

    // mapea un DTO ItemPaymentMarketDTO a una entidad ItemPaymentMarket
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "mountRefund", ignore = true)
    ItemPaymentMarket toEntity(ItemPaymentMarketDTO source);
}
