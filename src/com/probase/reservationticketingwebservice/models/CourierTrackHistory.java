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

import com.probase.reservationticketingwebservice.enumerations.DeviceStatus;
import com.probase.reservationticketingwebservice.enumerations.DeviceType;
import com.probase.reservationticketingwebservice.enumerations.ShipmentStatus;
  
@Entity
@Table(name="courier_track_history")  
public class CourierTrackHistory implements Serializable{  
	private static final long serialVersionUID = 5457558421999610410L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long courierTrackHistoryId;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	Shipment shipment;
	@OneToOne  
    @JoinColumn
	User updateByUser;
	ShipmentStatus shipmentStatus;
	@OneToOne  
    @JoinColumn
	TicketCollectionPoint ticketCollectionPoint;
	String trackingDetails;
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


	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeleted_at(Date deletedAt) {
		this.deletedAt = deletedAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public TicketCollectionPoint getTicketCollectionPoint() {
		return ticketCollectionPoint;
	}

	public void setTicketCollectionPoint(TicketCollectionPoint ticketCollectionPoint) {
		this.ticketCollectionPoint = ticketCollectionPoint;
	}

	public Long getCourierTrackHistoryId() {
		return courierTrackHistoryId;
	}

	public ShipmentStatus getShipmentStatus() {
		return shipmentStatus;
	}

	public void setShipmentStatus(ShipmentStatus shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}

	public String getTrackingDetails() {
		return trackingDetails;
	}

	public void setTrackingDetails(String trackingDetails) {
		this.trackingDetails = trackingDetails;
	}

	public User getUpdateByUser() {
		return updateByUser;
	}

	public void setUpdateByUser(User updateByUser) {
		this.updateByUser = updateByUser;
	}
    
}

