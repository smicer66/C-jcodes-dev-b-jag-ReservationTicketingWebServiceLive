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

import com.probase.reservationticketingwebservice.enumerations.VehicleStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
  
@Entity
@Table(name="vehicles")  
public class Vehicle implements Serializable {  
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleId; 
	@Column(nullable = false)
	VehicleStatus vehicleStatus;
	@Column(nullable = false)
	Date createdAt;
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	String speed;
	@Column(nullable = false)
	String vehicleName;
	@Column(nullable = false)
	String manufacturer;
	@Column(nullable = false)
	VehicleType vehicleType;
	@OneToOne  
    @JoinColumn
	Client client;
	@Column(nullable = false)
	String vehicleCode;
	String inventoryNumber;
	Double maximumFuelCapacity;
	
	
	

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}

	@Enumerated(EnumType.STRING)
	public VehicleType getVehicleType() {
	    return vehicleType;
	}
	
	@Enumerated(EnumType.STRING)
	public VehicleStatus getVehicleStatus() {
	    return vehicleStatus;
	}

	public Long getVehicleId() {
		return vehicleId;
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

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setTrainStatus(VehicleStatus vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public void setVehicleStatus(VehicleStatus vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getVehicleCode() {
		return vehicleCode;
	}

	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}

	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public Double getMaximumFuelCapacity() {
		return maximumFuelCapacity;
	}

	public void setMaximumFuelCapacity(Double maximumFuelCapacity) {
		this.maximumFuelCapacity = maximumFuelCapacity;
	}


	
}
