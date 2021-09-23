package com.ktds.batch.pcbs;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class PcbsSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcbsSpringBatchApplication.class, args);
    }

}
