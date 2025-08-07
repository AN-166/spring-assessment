package com.assement.customerbatch.controller;

import com.assement.customerbatch.entity.Customer;
import com.assement.customerbatch.entity.CustomerUpdateDTO;
import com.assement.customerbatch.entity.SearchRequest;
import com.assement.customerbatch.repository.CustomerRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/search")
    public Page<Customer> search(@RequestBody SearchRequest request) {
        Specification<Customer> spec = Specification.where(null);

        if (request.getCustomerId() != null && !request.getCustomerId().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("customerID")), "%" + request.getCustomerId().toLowerCase() + "%"));
        }

        if (request.getAccountNumber() != null && !request.getAccountNumber().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("accountNumber")), "%" + request.getAccountNumber().toLowerCase() + "%"));
        }

        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("description")), "%" + request.getDescription().toLowerCase() + "%"));
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return customerRepository.findAll(spec, pageable);
    }

    // Update description field only, with optimistic locking
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdateDTO dto) {
        return customerRepository.findById(id).map(existingCustomer -> {
            // Check version for optimistic locking
            if (!existingCustomer.getVersion().equals(dto.getVersion())) {
                return ResponseEntity.status(409).body("Conflict: Version mismatch. Data may have been updated by another user.");
            }

            existingCustomer.setDescription(dto.getDescription());

            try {
                Customer saved = customerRepository.save(existingCustomer);
                return ResponseEntity.ok(saved);
            } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                return ResponseEntity.status(409).body("Conflict: Concurrent modification detected.");
            }
        }).orElse(ResponseEntity.status(404).body("Customer not found"));
    }
}
