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
import com.probase.reservationticketingwebservice.enumerations.TripSeatDirection;
import com.probase.reservationticketingwebservice.enumerations.VehicleSeatLocation;
  
@Entity
@Table(name="vehicle_seats")  
public class VehicleSeat implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleSeatId;
	@Column(nullable = false)
	String seatNumber;
	@OneToOne  
    @JoinColumn
    VehicleSeatSection vehicleSeatSection;
	@OneToOne  
    @JoinColumn
    Client client;
	@Column(nullable = false)
	Boolean tripSeatFacing;
	@Column(nullable = false)
	Integer seatOrder;
	@Column(nullable = false)
	VehicleSeatLocation vehicleSeatLocation;
	@Column(nullable = false)
	String cabinName;
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

	public Long getVehicleSeatId() {
		return vehicleSeatId;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public Boolean getTripSeatFacing() {
		return tripSeatFacing;
	}

	public void setTripSeatFacing(Boolean tripSeatFacing) {
		this.tripSeatFacing = tripSeatFacing;
	}

	public VehicleSeatLocation getVehicleSeatLocation() {
		return vehicleSeatLocation;
	}

	public void setVehicleSeatLocation(VehicleSeatLocation vehicleSeatLocation) {
		this.vehicleSeatLocation = vehicleSeatLocation;
	}

	public String getCabinName() {
		return cabinName;
	}

	public void setCabinName(String cabinName) {
		this.cabinName = cabinName;
	}

	public VehicleSeatSection getVehicleSeatSection() {
		return vehicleSeatSection;
	}

	public void setVehicleSeatSection(VehicleSeatSection vehicleSeatSection) {
		this.vehicleSeatSection = vehicleSeatSection;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Integer getSeatOrder() {
		return seatOrder;
	}

	public void setSeatOrder(Integer seatOrder) {
		this.seatOrder = seatOrder;
	}



	
}
