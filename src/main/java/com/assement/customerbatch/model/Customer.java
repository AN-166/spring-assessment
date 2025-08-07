package com.assement.customerbatch.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private BigDecimal trxAmount;
    private String description;
    private LocalDate trxDate;
    private LocalTime trxTime;
    private Long customerId;
    
    @Version
    private Integer version;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Add Getters and Setters

	
	@PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getTrxAmount() {
		return trxAmount;
	}

	public void setTrxAmount(BigDecimal trxAmount) {
		this.trxAmount = trxAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(LocalDate trxDate) {
		this.trxDate = trxDate;
	}

	public LocalTime getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(LocalTime trxTime) {
		this.trxTime = trxTime;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "accountNumber='" + accountNumber + '\'' +
                ", trxAmount='" + trxAmount + '\'' +
                ", description='" + description + '\'' +
                ", trxDate='" + trxDate + '\'' +
                ", trxTime='" + trxTime + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
