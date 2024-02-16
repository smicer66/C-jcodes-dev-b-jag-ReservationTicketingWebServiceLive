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

import com.probase.reservationticketingwebservice.enumerations.CardStatus;
import com.probase.reservationticketingwebservice.enumerations.Channel;
import com.probase.reservationticketingwebservice.enumerations.PassengerType;
import com.probase.reservationticketingwebservice.enumerations.SeatAvailabilityStatus;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
import com.probase.reservationticketingwebservice.enumerations.TripSeatDirection;
import com.probase.reservationticketingwebservice.enumerations.VehicleSeatLocation;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
  
@Entity
@Table(name="vehicle_seat_availability")  
public class VehicleSeatAvailability implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleSeatAvailabilityId;
	@OneToOne  
    @JoinColumn
    VehicleSeat vehicleSeat;
	@OneToOne  
    @JoinColumn
	VehicleTripRouteSeatSection vehicleTripRouteSeatSection;
	@Column(nullable = false)
	Boolean lockedDown;
	Date lockedDownExpiryDate;
	@Column(nullable = false)
	SeatAvailabilityStatus seatAvailabilityStatus;
	@OneToOne  
    @JoinColumn
    Customer boughtByCustomer;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	String lockedDownBy;
	PassengerType passengerType;
	
	
	
	
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

	public Long getVehicleSeatAvailabilityId() {
		return vehicleSeatAvailabilityId;
	}

	public VehicleSeat getVehicleSeat() {
		return vehicleSeat;
	}

	public void setVehicleSeat(VehicleSeat vehicleSeat) {
		this.vehicleSeat = vehicleSeat;
	}

	public Boolean getLockedDown() {
		return lockedDown;
	}

	public void setLockedDown(Boolean lockedDown) {
		this.lockedDown = lockedDown;
	}

	public Date getLockedDownExpiryDate() {
		return lockedDownExpiryDate;
	}

	public void setLockedDownExpiryDate(Date lockedDownExpiryDate) {
		this.lockedDownExpiryDate = lockedDownExpiryDate;
	}



	@Enumerated(EnumType.STRING)
	public SeatAvailabilityStatus getSeatAvailabilityStatus() {
		return seatAvailabilityStatus;
	}

	public void setSeatAvailabilityStatus(
			SeatAvailabilityStatus seatAvailabilityStatus) {
		this.seatAvailabilityStatus = seatAvailabilityStatus;
	}

	public Customer getBoughtByCustomer() {
		return boughtByCustomer;
	}

	public void setBoughtByCustomer(Customer boughtByCustomer) {
		this.boughtByCustomer = boughtByCustomer;
	}

	public String getLockedDownBy() {
		return lockedDownBy;
	}

	public void setLockedDownBy(String lockedDownBy) {
		this.lockedDownBy = lockedDownBy;
	}

	public VehicleTripRouteSeatSection getVehicleTripRouteSeatSection() {
		return vehicleTripRouteSeatSection;
	}

	public void setVehicleTripRouteSeatSection(
			VehicleTripRouteSeatSection vehicleTripRouteSeatSection) {
		this.vehicleTripRouteSeatSection = vehicleTripRouteSeatSection;
	}

	@Enumerated(EnumType.STRING)
	public PassengerType getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(PassengerType passengerType) {
		this.passengerType = passengerType;
	}



	
}
