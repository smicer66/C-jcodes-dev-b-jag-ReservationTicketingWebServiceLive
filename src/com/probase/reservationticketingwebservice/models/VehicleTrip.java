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
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
  
@Entity
@Table(name="vehicle_trips")  
public class VehicleTrip implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleTripId;
	@OneToOne  
    @JoinColumn
    ScheduleStation originScheduleStation;
	@OneToOne  
    @JoinColumn
    ScheduleStation finalDestinationScheduleStation;
	@Column(nullable = false)
	Date createdAt;
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	VehicleTripStatus vehicleTripStatus; 
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	Line line;
	Date purchaseStartDate;
	Boolean limitPassengerCount;
	
	
	
	
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

	public Long getVehicleTripId() {
		return vehicleTripId;
	}

	public VehicleTripStatus getVehicleTripStatus() {
		return vehicleTripStatus;
	}

	public void setVehicleTripStatus(VehicleTripStatus vehicleTripStatus) {
		this.vehicleTripStatus = vehicleTripStatus;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Date getPurchaseStartDate() {
		return purchaseStartDate;
	}

	public void setPurchaseStartDate(Date purchaseStartDate) {
		this.purchaseStartDate = purchaseStartDate;
	}

	public Boolean getLimitPassengerCount() {
		return limitPassengerCount;
	}

	public void setLimitPassengerCount(Boolean limitPassengerCount) {
		this.limitPassengerCount = limitPassengerCount;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public ScheduleStation getOriginScheduleStation() {
		return originScheduleStation;
	}

	public void setOriginScheduleStation(ScheduleStation originScheduleStation) {
		this.originScheduleStation = originScheduleStation;
	}

	public ScheduleStation getFinalDestinationScheduleStation() {
		return finalDestinationScheduleStation;
	}

	public void setFinalDestinationScheduleStation(
			ScheduleStation finalDestinationScheduleStation) {
		this.finalDestinationScheduleStation = finalDestinationScheduleStation;
	}

	
}
