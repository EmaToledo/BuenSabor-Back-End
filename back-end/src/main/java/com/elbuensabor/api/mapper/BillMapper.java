package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.BillDTO;
import com.elbuensabor.api.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BillMapper extends GenericMapper<Bill, BillDTO>{
    static BillMapper getInstance() {
        return Mappers.getMapper(BillMapper.class);
    }


    // mapea una entidad Bill a un DTO BillDTO
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "pdf", target = "base64")
    BillDTO toDTO(Bill source);

    @Mapping(target = "generationDate", ignore = true)
    @Mapping(target = "lowDate", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "pdf", ignore = true)
    Bill toEntity(BillDTO source);

}
