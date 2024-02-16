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
@Table(name="schedule_stations")  
public class ScheduleStation implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long scheduleStationId;
	Date departureTime;
	Date arrivalTime;
	@Column(nullable = false)
	Date createdAt;
	Date updatedAt;
	Date deletedAt;
	@OneToOne  
    @JoinColumn
	Station station;
	@OneToOne  
    @JoinColumn
	ScheduleDay scheduleDay;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	ScheduleStationCode scheduleStationCode;
	@OneToOne  
    @JoinColumn
	Vehicle vehicle;
	@OneToOne  
    @JoinColumn
	User pilotAssigned;
	Double openingFuelCapacity;
	Double closingFuelCapacity;
	String openingFuelCapacityUnits;
	String closingFuelCapacityUnits;
	Date datePilotSignedIn;
	Date datePilotSignedOut;
	String incidentReasons;
	
	
	
	
	
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

	public Long getScheduleStationId() {
		return scheduleStationId;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public ScheduleDay getScheduleDay() {
		return scheduleDay;
	}

	public void setScheduleDay(ScheduleDay scheduleDay) {
		this.scheduleDay = scheduleDay;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public ScheduleStationCode getScheduleStationCode() {
		return scheduleStationCode;
	}

	public void setScheduleStationCode(ScheduleStationCode scheduleStationCode) {
		this.scheduleStationCode = scheduleStationCode;
	}

	public User getPilotAssigned() {
		return pilotAssigned;
	}

	public void setPilotAssigned(User pilotAssigned) {
		this.pilotAssigned = pilotAssigned;
	}

	public Double getOpeningFuelCapacity() {
		return openingFuelCapacity;
	}

	public void setOpeningFuelCapacity(Double openingFuelCapacity) {
		this.openingFuelCapacity = openingFuelCapacity;
	}

	public Double getClosingFuelCapacity() {
		return closingFuelCapacity;
	}

	public void setClosingFuelCapacity(Double closingFuelCapacity) {
		this.closingFuelCapacity = closingFuelCapacity;
	}

	public String getOpeningFuelCapacityUnits() {
		return openingFuelCapacityUnits;
	}

	public void setOpeningFuelCapacityUnits(String openingFuelCapacityUnits) {
		this.openingFuelCapacityUnits = openingFuelCapacityUnits;
	}

	public String getClosingFuelCapacityUnits() {
		return closingFuelCapacityUnits;
	}

	public void setClosingFuelCapacityUnits(String closingFuelCapacityUnits) {
		this.closingFuelCapacityUnits = closingFuelCapacityUnits;
	}

	public Date getDatePilotSignedIn() {
		return datePilotSignedIn;
	}

	public void setDatePilotSignedIn(Date datePilotSignedIn) {
		this.datePilotSignedIn = datePilotSignedIn;
	}

	public Date getDatePilotSignedOut() {
		return datePilotSignedOut;
	}

	public void setDatePilotSignedOut(Date datePilotSignedOut) {
		this.datePilotSignedOut = datePilotSignedOut;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getIncidentReasons() {
		return incidentReasons;
	}

	public void setIncidentReasons(String incidentReasons) {
		this.incidentReasons = incidentReasons;
	}


	
}
