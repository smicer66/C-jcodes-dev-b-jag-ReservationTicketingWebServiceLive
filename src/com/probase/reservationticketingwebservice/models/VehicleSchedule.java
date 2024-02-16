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
@Table(name="vehicle_schedules")  
public class VehicleSchedule implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleScheduleId;
	@OneToOne  
    @JoinColumn
	Vehicle vehicle;
	@OneToOne  
    @JoinColumn
    ScheduleStationCode scheduleStationCode;
	@Column(nullable = false)
	Date createdAt;
	Date updatedAt;
	Date deletedAt;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	Line line;
	Date electronicPurchaseStartDate;
	@OneToOne  
    @JoinColumn
	ScheduleStation departureScheduleStation;
	@OneToOne  
    @JoinColumn
	ScheduleStation arrivalScheduleStation;
	Boolean passengerEnabled;
	Boolean courierEnabled;
	Double refundIndex;
	
	
	
	
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

	public Long getVehicleScheduleId() {
		return vehicleScheduleId;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ScheduleStationCode getScheduleStationCode() {
		return scheduleStationCode;
	}

	public void setScheduleStationCode(ScheduleStationCode scheduleStationCode) {
		this.scheduleStationCode = scheduleStationCode;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Date getElectronicPurchaseStartDate() {
		return electronicPurchaseStartDate;
	}

	public void setElectronicPurchaseStartDate(Date electronicPurchaseStartDate) {
		this.electronicPurchaseStartDate = electronicPurchaseStartDate;
	}

	public ScheduleStation getDepartureScheduleStation() {
		return departureScheduleStation;
	}

	public void setDepartureScheduleStation(
			ScheduleStation departureScheduleStation) {
		this.departureScheduleStation = departureScheduleStation;
	}

	public ScheduleStation getArrivalScheduleStation() {
		return arrivalScheduleStation;
	}

	public void setArrivalScheduleStation(
			ScheduleStation arrivalScheduleStation) {
		this.arrivalScheduleStation = arrivalScheduleStation;
	}

	public Boolean getPassengerEnabled() {
		return passengerEnabled;
	}

	public void setPassengerEnabled(Boolean passengerEnabled) {
		this.passengerEnabled = passengerEnabled;
	}

	public Boolean getCourierEnabled() {
		return courierEnabled;
	}

	public void setCourierEnabled(Boolean courierEnabled) {
		this.courierEnabled = courierEnabled;
	}

	public Double getRefundIndex() {
		return refundIndex;
	}

	public void setRefundIndex(Double refundIndex) {
		this.refundIndex = refundIndex;
	}


	
}
