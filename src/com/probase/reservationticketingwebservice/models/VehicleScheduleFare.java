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
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
  
@Entity
@Table(name="vehicle_schedule_fares")  
public class VehicleScheduleFare implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleScheduleFareId;
	@OneToOne  
    @JoinColumn
    VehicleTripRouteSeatSection vehicleTripRouteSeatSection;
	@Column(nullable = false)
	Date createdAt;
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	Double baseChildFare; 
	@Column(nullable = false)
	Double baseAdultFare; 
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

	public Long getVehicleScheduleFareId() {
		return vehicleScheduleFareId;
	}

	public Double getBaseChildFare() {
		return baseChildFare;
	}

	public void setBaseChildFare(Double baseChildFare) {
		this.baseChildFare = baseChildFare;
	}

	public Double getBaseAdultFare() {
		return baseAdultFare;
	}

	public void setBaseAdultFare(Double baseAdultFare) {
		this.baseAdultFare = baseAdultFare;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public VehicleTripRouteSeatSection getVehicleTripRouteSeatSection() {
		return vehicleTripRouteSeatSection;
	}

	public void setVehicleTripRouteSeatSection(
			VehicleTripRouteSeatSection vehicleTripRouteSeatSection) {
		this.vehicleTripRouteSeatSection = vehicleTripRouteSeatSection;
	}


	
}
