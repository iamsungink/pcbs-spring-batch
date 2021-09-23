package com.ktds.batch.pcbs.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "KSI_ORDERS")
@Getter
@NoArgsConstructor
public class Orders {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private int amount;

    private LocalDate createdDate;

    @Builder
    private Orders(String itemName, int amount, LocalDate createdDate) {
        this.itemName = itemName;
        this.amount = amount;
        this.createdDate = createdDate;
    }
}
