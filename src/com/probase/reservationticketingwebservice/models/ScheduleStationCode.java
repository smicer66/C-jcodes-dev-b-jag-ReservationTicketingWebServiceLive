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

import com.probase.reservationticketingwebservice.enumerations.CardStatus;
import com.probase.reservationticketingwebservice.enumerations.CardType;
import com.probase.reservationticketingwebservice.enumerations.ClientStatus;
  
@Entity
@Table(name="schedule_station_codes")  
public class ScheduleStationCode implements Serializable {  
	
	private static final long serialVersionUID = -4978223012667661805L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long scheduleStationId;
	@Column(nullable = false)
	String scheduleStationCode;
	@Column(nullable = false)
	Date departureDate;
	@OneToOne  
    @JoinColumn
	Client client;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	Boolean passengerEnabled;
	Boolean courierEnabled;
	String trainNumber;

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.createdAt = new Date();
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

	public String getScheduleStationCode() {
		return scheduleStationCode;
	}

	public void setScheduleStationCode(String scheduleStationCode) {
		this.scheduleStationCode = scheduleStationCode;
	}

	public Long getScheduleStationId() {
		return scheduleStationId;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
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

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}


    
}

