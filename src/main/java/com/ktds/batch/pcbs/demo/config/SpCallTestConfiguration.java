package com.ktds.batch.pcbs.demo.config;

import com.ktds.batch.pcbs.demo.model.Person;
import com.ktds.batch.pcbs.demo.step.CustomItemReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.StoredProcedureItemReader;
import org.springframework.batch.item.database.builder.StoredProcedureItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class SpCallTestConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    public SpCallTestConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource, EntityManagerFactory entityManagerFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job spCallTestJob() throws Exception {
        return this.jobBuilderFactory.get("spCallTestJob")
                .incrementer(new RunIdIncrementer())
                .start(this.customItemReaderStep())
                .next(this.spCallTestStep())
                .build();
    }

    @Bean
    public Step customItemReaderStep() {
        return this.stepBuilderFactory.get("customItemReaderStep")
                .<Person, Person>chunk(10)
                .reader(new CustomItemReader<>(getItems()))
                .writer(this.itemWriter())
                .build();
    }

    @Bean
    public Step spCallTestStep() throws Exception {
        return this.stepBuilderFactory.get("spCallTestStep")
                .chunk(10)
                .reader(spCallReader())
                .writer(spCallWriter())
                .build();
    }

    @Bean
    @StepScope
    public StoredProcedureItemReader<?> spCallReader() throws Exception {

        List<SqlParameter> parameters = new ArrayList<>();
        parameters.add(new SqlParameter("@I_INV_YYYYMM", Types.VARCHAR));
        parameters.add(new SqlOutParameter("OUT_RETURN_STATUS", Types.VARCHAR));
        parameters.add(new SqlOutParameter("OUT_ERR_MSG", Types.VARCHAR));

        StoredProcedureItemReader itemReader = new StoredProcedureItemReaderBuilder<>()
                .name("spCallReader")
                .dataSource(dataSource)
                .procedureName("SP_KSI_TEST2")
//                .parameters(new SqlParameters[])
                .parameters(new SqlParameter[]{
                        new SqlParameter("I_INV_YYYYMM", Types.VARCHAR)
//                        new SqlOutParameter("@OUT_RETURN_STATUS", Types.VARCHAR),
//                        new SqlOutParameter("@OUT_ERR_MSG", Types.VARCHAR)
                })
//                .preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[] {null,null,null}))
//                .preparedStatementSetter(new PreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps) throws SQLException {
//                        ps.setString(1,"aaa");
//                        ps.setString(2, "");
//                        ps.setString(3, "");
//                    }
//                })
                .preparedStatementSetter(new CustomSPParamSetter())
//                .preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{null, new CustomSPParamSetter()}))
                .rowMapper(new SpCallRowMapper())
                .build();

        itemReader.afterPropertiesSet();

        System.out.println(itemReader.getSql());

        return itemReader;
    }

    private ItemWriter<? super Object> spCallWriter() {
        return items -> items.forEach(System.out::println);
    }

    private ItemWriter<Person> itemWriter() {
        return items -> log.info(items.stream()
                .map(Person::getName)
                .collect(Collectors.joining(", ")));
    }

    private List<Person> getItems() {
        List<Person> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            items.add(new Person(i + 1, "test name" + i, "test age", "test address"));
        }

        return items;
    }
}
