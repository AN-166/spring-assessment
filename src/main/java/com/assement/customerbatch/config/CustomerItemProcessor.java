package com.assement.customerbatch.config;

import java.math.RoundingMode;

import org.springframework.batch.item.ItemProcessor;
import com.assement.customerbatch.entity.Customer;

public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(final Customer customer) throws Exception {
        // Example: Trim and uppercase the description field
        if (customer.getDescription() != null) {
            customer.setDescription(customer.getDescription().trim().toUpperCase());
        }

        if (customer.getDescription() != null)
            customer.setDescription(customer.getDescription().trim().toUpperCase());

        if (customer.getAccountNumber() != null)
            customer.setAccountNumber(customer.getAccountNumber().trim());

        if (customer.getTrxAmount() != null)
            customer.setTrxAmount(customer.getTrxAmount().setScale(2, RoundingMode.HALF_UP));


        if (customer.getCustomerId() != null)
            customer.setCustomerId(customer.getCustomerId());


        return customer;
    }

}
