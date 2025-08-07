package com.assement.customerbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerBatchApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(JobLauncher jobLauncher, Job importCustomerJob) {
        return args -> {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // to ensure unique job instance
                    .toJobParameters();
            jobLauncher.run(importCustomerJob, params);
        };
    }
}
