package com.assement.customerbatch.config;

import com.assement.customerbatch.entity.Customer;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.item.file.mapping.FieldSetMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Bean
	public FlatFileItemReader<Customer> reader() {
	    FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
	    reader.setResource(new ClassPathResource("input/dataSource.txt"));
	    reader.setLinesToSkip(1); // skip header

	    
	    // Tokenizer setup
	    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer("|");
	    tokenizer.setNames("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId");
	    tokenizer.setStrict(false);

	    // FieldSetMapper with manual conversion
	    FieldSetMapper<Customer> fieldSetMapper = fieldSet -> {
	        Customer customer = new Customer();

	        customer.setAccountNumber(fieldSet.readString("accountNumber"));

	        // Safe parsing of BigDecimal
	        String amount = fieldSet.readString("trxAmount");
	        customer.setTrxAmount(amount == null || amount.isBlank() ? null : new BigDecimal(amount));

	        customer.setDescription(fieldSet.readString("description"));

	        // LocalDate
	        String date = fieldSet.readString("trxDate");
	        customer.setTrxDate(date == null || date.isBlank() ? null : LocalDate.parse(date));

	        // LocalTime
	        String time = fieldSet.readString("trxTime");
	        customer.setTrxTime(time == null || time.isBlank() ? null : LocalTime.parse(time));

	        // Long
	        String id = fieldSet.readString("customerId");
	        customer.setCustomerId(id == null || id.isBlank() ? null : Long.parseLong(id));
	        
	        customer.setVersion(1);

	        return customer;
	    };

	    DefaultLineMapper<Customer> delegate = new DefaultLineMapper<>();
	    delegate.setLineTokenizer(tokenizer);
	    delegate.setFieldSetMapper(fieldSetMapper);
	    delegate.afterPropertiesSet();

	    // Custom line mapper with blank line and error handling
	    reader.setLineMapper((line, lineNumber) -> {
	        if (line == null || line.trim().isEmpty()) {
	            // throw exception for Spring Batch to skip or handle blank lines
	            throw new FlatFileParseException("Skipping blank line", line, lineNumber);
	        }
	        return delegate.mapLine(line, lineNumber);
	    });

	    return reader;
	}

    @Bean
    public CustomerItemProcessor processor() {
        return new CustomerItemProcessor();
    }

    @Bean
    public JpaItemWriter<Customer> writer(EntityManagerFactory emf) {
        JpaItemWriter<Customer> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     FlatFileItemReader<Customer> reader,
                     CustomerItemProcessor processor,
                     JpaItemWriter<Customer> writer) {

        return new StepBuilder("customerStep", jobRepository)
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()                        // Enable fault tolerance
                .skip(FlatFileParseException.class)   // Skip parsing errors like blank lines
                .skipLimit(Integer.MAX_VALUE)          // Skip unlimited lines (or set a max limit)
                .build();
    }

    @Bean
    public Job importCustomerJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("importCustomerJob", jobRepository)
                .start(step)
                .build();
    }
}
