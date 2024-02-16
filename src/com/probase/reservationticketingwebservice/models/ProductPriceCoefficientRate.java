package com.probase.reservationticketingwebservice.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;  
import javax.persistence.Entity;  
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;  
import javax.persistence.GenerationType;
import javax.persistence.Id;  
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Enumerated;

import com.probase.reservationticketingwebservice.enumerations.DeviceStatus;
import com.probase.reservationticketingwebservice.enumerations.DeviceType;
import com.probase.reservationticketingwebservice.enumerations.ProductPriceCoefficient;
  
@Entity
@Table(name="product_price_coefficient_rates")  
public class ProductPriceCoefficientRate implements Serializable{  
	private static final long serialVersionUID = 5457558421999610410L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long productPriceCoefficientRateId;
	@Column(nullable = false)
	ProductPriceCoefficient productPriceCoefficient; 
	@Column(nullable = false)
	Double productPriceCoefficientValue; 
	@OneToOne  
    @JoinColumn
	Client client;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}


	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeleted_at(Date deletedAt) {
		this.deletedAt = deletedAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public ProductPriceCoefficient getProductPriceCoefficient() {
		return productPriceCoefficient;
	}

	public void setProductPriceCoefficient(
			ProductPriceCoefficient productPriceCoefficient) {
		this.productPriceCoefficient = productPriceCoefficient;
	}

	public Double getProductPriceCoefficientValue() {
		return productPriceCoefficientValue;
	}

	public void setProductPriceCoefficientValue(Double productPriceCoefficientValue) {
		this.productPriceCoefficientValue = productPriceCoefficientValue;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Long getProductPriceCoefficientRateId() {
		return productPriceCoefficientRateId;
	}
	
    
}

