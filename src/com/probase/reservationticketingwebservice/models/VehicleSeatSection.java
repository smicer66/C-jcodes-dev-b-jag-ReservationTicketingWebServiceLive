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

import com.probase.reservationticketingwebservice.enumerations.CabinType;
import com.probase.reservationticketingwebservice.enumerations.VehicleStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
  
@Entity
@Table(name="vehicle_seat_sections")  
public class VehicleSeatSection implements Serializable {  
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleSeatSectionId; 
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	String sectionName;
	@Column(nullable = false)
	String sectionCode;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	Station currentStation;
	@OneToOne  
    @JoinColumn
	Station destinationStation;
	@OneToOne  
    @JoinColumn
    VehicleSeatClass vehicleSeatClass;
	Boolean standingAllowed;
	Integer maxSeatingCapacity;
	Integer maxStandingCapacity;
	CabinType cabinType;
	Double maxTonnage;
	Double maxVolume;
	
	

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

	public Long getVehicleSeatSectionId() {
		return vehicleSeatSectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Station getCurrentStation() {
		return currentStation;
	}

	public void setCurrentStation(Station currentStation) {
		this.currentStation = currentStation;
	}

	public Station getDestinationStation() {
		return destinationStation;
	}

	public void setDestinationStation(Station destinationStation) {
		this.destinationStation = destinationStation;
	}

	public VehicleSeatClass getVehicleSeatClass() {
		return vehicleSeatClass;
	}

	public void setVehicleSeatClass(VehicleSeatClass vehicleSeatClass) {
		this.vehicleSeatClass = vehicleSeatClass;
	}

	public Boolean getStandingAllowed() {
		return standingAllowed;
	}

	public void setStandingAllowed(Boolean standingAllowed) {
		this.standingAllowed = standingAllowed;
	}

	public Integer getMaxSeatingCapacity() {
		return maxSeatingCapacity;
	}

	public void setMaxSeatingCapacity(Integer maxSeatingCapacity) {
		this.maxSeatingCapacity = maxSeatingCapacity;
	}

	public Integer getMaxStandingCapacity() {
		return maxStandingCapacity;
	}

	public void setMaxStandingCapacity(Integer maxStandingCapacity) {
		this.maxStandingCapacity = maxStandingCapacity;
	}

	public CabinType getCabinType() {
		return cabinType;
	}

	public void setCabinType(CabinType cabinType) {
		this.cabinType = cabinType;
	}

	public Double getMaxTonnage() {
		return maxTonnage;
	}

	public void setMaxTonnage(Double maxTonnage) {
		this.maxTonnage = maxTonnage;
	}

	public Double getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(Double maxVolume) {
		this.maxVolume = maxVolume;
	}


	
}
