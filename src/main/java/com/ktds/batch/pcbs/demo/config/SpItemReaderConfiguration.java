package com.ktds.batch.pcbs.demo.config;

import com.ktds.batch.pcbs.demo.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.StoredProcedureItemReader;
import org.springframework.batch.item.database.builder.StoredProcedureItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.SqlParameter;

import javax.sql.DataSource;
import java.sql.Types;

@Configuration
@Slf4j
public class SpItemReaderConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    public SpItemReaderConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean
    public Job spItemReaderJob() throws Exception {
        return this.jobBuilderFactory.get("spItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(this.spItemReaderStep())
                .build();
    }

    private Step spItemReaderStep() throws Exception {
        return this.stepBuilderFactory.get("spItemReaderStep")
                .<Person, Person>chunk(10)
                .reader(this.spItemReader())
                .writer(this.spItemWriter())
                .build();
    }

    private ItemReader<? extends Person> spItemReader() throws Exception {
        StoredProcedureItemReader itemReader = new StoredProcedureItemReaderBuilder<>()
                .name("spItemReader")
                .dataSource(dataSource)
                .procedureName("SP_KSI_TEST2")
                .parameters(new SqlParameter[]{
                        new SqlParameter("I_INV_YYYYMM", Types.VARCHAR)
//                        new SqlOutParameter("@OUT_RETURN_STATUS", Types.VARCHAR),
//                        new SqlOutParameter("@OUT_ERR_MSG", Types.VARCHAR)
                })
                .preparedStatementSetter(new CustomSPParamSetter())
                .rowMapper(new SpCallRowMapper())
                .build();

        itemReader.afterPropertiesSet();

        System.out.println(itemReader.getSql());

        return itemReader;
    }

    private ItemWriter<? super Person> spItemWriter() {
        return items -> items.forEach(System.out::println);
    }
}
