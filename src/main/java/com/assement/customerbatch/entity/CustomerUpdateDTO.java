package com.assement.customerbatch.entity;
public class CustomerUpdateDTO {
    private String description;
    private Integer version;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

}
