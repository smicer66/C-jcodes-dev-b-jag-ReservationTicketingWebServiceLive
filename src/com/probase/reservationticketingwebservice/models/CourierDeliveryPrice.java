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
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Enumerated;

import com.probase.reservationticketingwebservice.enumerations.Channel;
import com.probase.reservationticketingwebservice.enumerations.PassengerType;
import com.probase.reservationticketingwebservice.enumerations.PriceType;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
  
@Entity
@Table(name="courier_delivery_prices")  
public class CourierDeliveryPrice implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long courierDeliveryPriceId;
	@OneToOne  
    @JoinColumn
    ProductCategory productCategory;
	@OneToOne  
    @JoinColumn
    CourierService courierService;
	Double minDistance; 
	Double maxDistance; 
	Double minWeight; 
	Double maxWeight; 
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	Double amount; 
	@OneToOne  
    @JoinColumn
	Client client;
	
	
	
	
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

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public CourierService getCourierService() {
		return courierService;
	}

	public void setCourierService(CourierService courierService) {
		this.courierService = courierService;
	}

	/*public Double getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Double getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(Double minHeight) {
		this.minHeight = minHeight;
	}

	public Double getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(Double maxHeight) {
		this.maxHeight = maxHeight;
	}

	public Double getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(Double minWidth) {
		this.minWidth = minWidth;
	}

	public Double getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(Double maxWidth) {
		this.maxWidth = maxWidth;
	}

	public Double getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(Double minVolume) {
		this.minVolume = minVolume;
	}

	public Double getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(Double maxVolume) {
		this.maxVolume = maxVolume;
	}*/

	public Long getCourierDeliveryPriceId() {
		return courierDeliveryPriceId;
	}

	public Double getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(Double minDistance) {
		this.minDistance = minDistance;
	}

	public Double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(Double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public Double getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}
	
}
