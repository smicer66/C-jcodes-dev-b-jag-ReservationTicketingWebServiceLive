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
@Table(name="schedule_station_courier_sections")  
public class ScheduleStationCourierSection implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long schedStatCourSectId;
	@OneToOne  
    @JoinColumn
    VehicleSeatSection vehicleSeatSection;
	@OneToOne  
    @JoinColumn
    ScheduleStation originScheduleStation;
	@OneToOne  
    @JoinColumn
    ScheduleStation arrivalScheduleStation;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@OneToOne  
    @JoinColumn
	Client client;
	//Double currentAvailableVolume;
	//Double currentAvailableTonnage;
	
	
	
	
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

	public ScheduleStation getOriginScheduleStation() {
		return originScheduleStation;
	}

	public void setOriginScheduleStation(ScheduleStation originScheduleStation) {
		this.originScheduleStation = originScheduleStation;
	}

	public Long getSchedStatCourSectId() {
		return schedStatCourSectId;
	}

	/*public Double getCurrentAvailableVolume() {
		return currentAvailableVolume;
	}

	public void setCurrentAvailableVolume(Double currentAvailableVolume) {
		this.currentAvailableVolume = currentAvailableVolume;
	}

	public Double getCurrentAvailableTonnage() {
		return currentAvailableTonnage;
	}

	public void setCurrentAvailableTonnage(Double currentAvailableTonnage) {
		this.currentAvailableTonnage = currentAvailableTonnage;
	}*/

	public ScheduleStation getArrivalScheduleStation() {
		return arrivalScheduleStation;
	}

	public void setArrivalScheduleStation(ScheduleStation arrivalScheduleStation) {
		this.arrivalScheduleStation = arrivalScheduleStation;
	}


	
}
