package com.ktds.batch.pcbs.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "KSI_USER")
@SequenceGenerator(name = "USER_SEQ_GEN", sequenceName = "USER_SEQ", initialValue = 1, allocationSize = 1)
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GEN")
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_LEVEL")
    @Enumerated(EnumType.STRING)
    private Level level = Level.NORMAL;

//    private int totalAmount;

//    @OneToMany(cascade = CascadeType.PERSIST)
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Orders> orders;

    private LocalDate updatedDate;

    @Builder
    private User(String userName
//            , int totalAmount
                 , List<Orders> orders
    ) {
        this.userName = userName;
//        this.totalAmount = totalAmount;
        this.orders = orders;
    }

    public boolean availableLevelUp() {
        return Level.availableLevelUp(this.getLevel(), this.getTotalAmount());
    }

    private int getTotalAmount() {
        return this.orders.stream()
                .mapToInt(Orders::getAmount)
                .sum();
    }

    public Level levelUp(){
        Level nextLevel = Level.getNextLevel(this.getTotalAmount());

        this.level = nextLevel;
        this.updatedDate = LocalDate.now();

        return nextLevel;
    }

    public enum Level {
        VIP(500000, null),
        GOLD(500000, VIP),
        SILVER(300000, GOLD),
        NORMAL(200000, SILVER);

        private final int nextAmount;
        private final Level nextLevel;

        Level(int nextAmount, Level nextLevel) {
            this.nextAmount = nextAmount;
            this.nextLevel = nextLevel;
        }

        private static boolean availableLevelUp(Level level, int totalAmount) {
            if (Objects.isNull(level)) {
                return false;
            }
            if (Objects.isNull(level.nextLevel)) {
                return false;
            }
            return totalAmount >= level.nextAmount;
        }

        private static Level getNextLevel(int totalAmount) {
            if (totalAmount >= Level.VIP.nextAmount) {
                return VIP;
            }
            if (totalAmount >= Level.GOLD.nextAmount) {
                return GOLD.nextLevel;
            }
            if (totalAmount >= Level.SILVER.nextAmount) {
                return SILVER.nextLevel;
            }
            if (totalAmount >= Level.NORMAL.nextAmount) {
                return NORMAL.nextLevel;
            }
            return NORMAL;
        }
    }
}
