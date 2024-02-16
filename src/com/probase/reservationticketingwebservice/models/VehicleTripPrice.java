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
import com.probase.reservationticketingwebservice.enumerations.PriceType;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
  
@Entity
@Table(name="vehicle_trip_prices")  
public class VehicleTripPrice implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vehicleTripPriceId;
	@OneToOne  
    @JoinColumn
    TripZone finalDestinationTripZone;
	@OneToOne  
    @JoinColumn
    VehicleSeatClass vehicleSeatClass;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	PriceType priceType; 
	@Column(nullable = false)
	Double amount; 
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public TripZone getFinalDestinationTripZone() {
		return finalDestinationTripZone;
	}

	public void setFinalDestinationTripZone(TripZone finalDestinationTripZone) {
		this.finalDestinationTripZone = finalDestinationTripZone;
	}

	public PriceType getPriceType() {
		return priceType;
	}

	public void setPriceType(PriceType priceType) {
		this.priceType = priceType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getRouteOrder() {
		return routeOrder;
	}

	public void setRouteOrder(Integer routeOrder) {
		this.routeOrder = routeOrder;
	}

	public Long getVehicleTripPriceId() {
		return vehicleTripPriceId;
	}

	public VehicleSeatClass getVehicleSeatClass() {
		return vehicleSeatClass;
	}

	public void setVehicleSeatClass(VehicleSeatClass vehicleSeatClass) {
		this.vehicleSeatClass = vehicleSeatClass;
	}

	
}
