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
@Table(name="vehicle_trip_routings")  
public class VehicleTripRouting implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleTripRoutingId;
	@OneToOne  
    @JoinColumn
    VehicleTrip vehicleTrip;
	@OneToOne  
    @JoinColumn
    ScheduleStation vehicleTripRoutingOriginSchedule;
	@OneToOne  
    @JoinColumn
    ScheduleStation vehicleTripRoutingDestinationSchedule;
	@Column(nullable = false)
	Date createdAt;
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	String routeCode; 
	@Column(nullable = false)
	Boolean status; 
	@OneToOne  
    @JoinColumn
	Client client;
	@Column(nullable = false)
	Integer routeOrder; 
	
	
	
	
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getVehicleTripRoutingId() {
		return vehicleTripRoutingId;
	}

	public VehicleTrip getVehicleTrip() {
		return vehicleTrip;
	}

	public void setVehicleTrip(VehicleTrip vehicleTrip) {
		this.vehicleTrip = vehicleTrip;
	}

	public String getRouteCode() {
		return routeCode;
	}

	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Integer getRouteOrder() {
		return routeOrder;
	}

	public void setRouteOrder(Integer routeOrder) {
		this.routeOrder = routeOrder;
	}

	public ScheduleStation getVehicleTripRoutingOriginSchedule() {
		return vehicleTripRoutingOriginSchedule;
	}

	public void setVehicleTripRoutingOriginSchedule(
			ScheduleStation vehicleTripRoutingOriginSchedule) {
		this.vehicleTripRoutingOriginSchedule = vehicleTripRoutingOriginSchedule;
	}

	public ScheduleStation getVehicleTripRoutingDestinationSchedule() {
		return vehicleTripRoutingDestinationSchedule;
	}

	public void setVehicleTripRoutingDestinationSchedule(
			ScheduleStation vehicleTripRoutingDestinationSchedule) {
		this.vehicleTripRoutingDestinationSchedule = vehicleTripRoutingDestinationSchedule;
	}


	
}
