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
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
  
@Entity
@Table(name="vehicle_trip_route_seat_sections")  
public class VehicleTripRouteSeatSection implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleTripRouteSeatSectionId;
	@OneToOne  
    @JoinColumn
    VehicleSeatSection vehicleSeatSection;
	@OneToOne  
    @JoinColumn
    VehicleTripRouting vehicleTripRouting;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@OneToOne  
    @JoinColumn
	Client client;
	Integer maximumNumberOfCabins;
	
	
	
	
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

	public VehicleSeatSection getVehicleSeatSection() {
		return vehicleSeatSection;
	}

	public void setVehicleSeatSection(VehicleSeatSection vehicleSeatSection) {
		this.vehicleSeatSection = vehicleSeatSection;
	}

	public Integer getMaximumNumberOfCabins() {
		return maximumNumberOfCabins;
	}

	public void setMaximumNumberOfCabins(Integer maximumNumberOfCabins) {
		this.maximumNumberOfCabins = maximumNumberOfCabins;
	}

	public Long getVehicleTripRouteSeatSectionId() {
		return vehicleTripRouteSeatSectionId;
	}

	public VehicleTripRouting getVehicleTripRouting() {
		return vehicleTripRouting;
	}

	public void setVehicleTripRouting(VehicleTripRouting vehicleTripRouting) {
		this.vehicleTripRouting = vehicleTripRouting;
	}


	
}
