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
  
@Entity
@Table(name="courier_services")  
public class CourierService implements Serializable{  
	private static final long serialVersionUID = 5457558421999610410L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long courierServiceId;
	@Column(nullable = false)
	String courierServiceName; 
	@Column(nullable = false)
	String courierServiceCode; 
	@Column(nullable = false)
	Integer maxDeliveryPeriod; 
	String details; 
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
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

	public String getCourierServiceName() {
		return courierServiceName;
	}

	public void setCourierServiceName(String courierServiceName) {
		this.courierServiceName = courierServiceName;
	}

	public Integer getMaxDeliveryPeriod() {
		return maxDeliveryPeriod;
	}

	public void setMaxDeliveryPeriod(Integer maxDeliveryPeriod) {
		this.maxDeliveryPeriod = maxDeliveryPeriod;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getCourierServiceId() {
		return courierServiceId;
	}

	public String getCourierServiceCode() {
		return courierServiceCode;
	}

	public void setCourierServiceCode(String courierServiceCode) {
		this.courierServiceCode = courierServiceCode;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	
	
    
}

