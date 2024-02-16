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
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
  
@Entity
@Table(name="purchased_trip_seats")  
public class PurchasedTripSeat implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long purchasedTripSeatId;
	@OneToOne  
    @JoinColumn
    PurchasedTrip purchasedTrip;
	@OneToOne  
    @JoinColumn
    VehicleSeat vehicleSeat;
	Date createdAt;
	Date updatedAt;
	Date deletedAt;
	@OneToOne
    @JoinColumn
    ScheduleStationSeatAvailability scheduleStationSeatAvailability;
	@Column(nullable = true)
	PassengerType passengerType;
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

	public Long getPurchasedTripSeatId() {
		return purchasedTripSeatId;
	}

	public PurchasedTrip getPurchasedTrip() {
		return purchasedTrip;
	}

	public void setPurchasedTrip(PurchasedTrip purchasedTrip) {
		this.purchasedTrip = purchasedTrip;
	}

	public VehicleSeat getVehicleSeat() {
		return vehicleSeat;
	}

	public void setVehicleSeat(VehicleSeat vehicleSeat) {
		this.vehicleSeat = vehicleSeat;
	}


	@Enumerated(EnumType.STRING)
	public PassengerType getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(PassengerType passengerType) {
		this.passengerType = passengerType;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ScheduleStationSeatAvailability getScheduleStationSeatAvailability() {
		return scheduleStationSeatAvailability;
	}

	public void setScheduleStationSeatAvailability(
			ScheduleStationSeatAvailability scheduleStationSeatAvailability) {
		this.scheduleStationSeatAvailability = scheduleStationSeatAvailability;
	}



	
}
