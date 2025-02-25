package com.elbuensabor.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_bill"))
public class Bill  extends GenericEntity {
    @Column(name = "generation_date")
    private LocalDateTime generationDate;

    @Column(name = "low_date")
    private LocalDateTime lowDate;

    @OneToOne
    @JoinColumn(name = "id_order")
    private Order order;

    @Column(name = "pdf_base64", columnDefinition = "LONGTEXT")
    private String pdf;


}
