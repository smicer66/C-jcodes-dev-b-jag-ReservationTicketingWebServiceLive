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
import com.probase.reservationticketingwebservice.enumerations.ShipmentStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
  
@Entity
@Table(name="vendor_shipments")  
public class VendorShipment implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vendorShipmentId;
	@OneToOne  
    @JoinColumn
    Vendor vendor;
	@OneToOne  
    @JoinColumn
    Shipment shipment;
	@OneToOne  
    @JoinColumn
    TicketCollectionPoint sourceCollectionPoint;
	@OneToOne  
    @JoinColumn
    TicketCollectionPoint destinationCollectionPoint;
	@OneToOne  
    @JoinColumn
    User assignedByClientOperator;
	@OneToOne  
    @JoinColumn
    User receivedByVendorUser;
	@OneToOne  
    @JoinColumn
    User deliveredByVendorUser;
	@OneToOne  
    @JoinColumn
    User receivedByClientOperator;
	@Column(nullable = false)
	Double totalAmount;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@OneToOne  
    @JoinColumn
	Client client;
	ShipmentStatus shipmentStatus;
	
	
	
	
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


	public TicketCollectionPoint getSourceCollectionPoint() {
		return sourceCollectionPoint;
	}

	public void setSourceCollectionPoint(TicketCollectionPoint sourceCollectionPoint) {
		this.sourceCollectionPoint = sourceCollectionPoint;
	}

	public TicketCollectionPoint getDestinationCollectionPoint() {
		return destinationCollectionPoint;
	}

	public void setDestinationCollectionPoint(
			TicketCollectionPoint destinationCollectionPoint) {
		this.destinationCollectionPoint = destinationCollectionPoint;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public User getAssignedByClientOperator() {
		return assignedByClientOperator;
	}

	public void setAssignedByClientOperator(User assignedByClientOperator) {
		this.assignedByClientOperator = assignedByClientOperator;
	}

	public User getReceivedByVendorUser() {
		return receivedByVendorUser;
	}

	public void setReceivedByVendorUser(User receivedByVendorUser) {
		this.receivedByVendorUser = receivedByVendorUser;
	}

	public User getDeliveredByVendorUser() {
		return deliveredByVendorUser;
	}

	public void setDeliveredByVendorUser(User deliveredByVendorUser) {
		this.deliveredByVendorUser = deliveredByVendorUser;
	}

	public User getReceivedByClientOperator() {
		return receivedByClientOperator;
	}

	public void setReceivedByClientOperator(User receivedByClientOperator) {
		this.receivedByClientOperator = receivedByClientOperator;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getVendorShipmentId() {
		return vendorShipmentId;
	}

	@Enumerated(EnumType.STRING)
	public ShipmentStatus getShipmentStatus() {
		return shipmentStatus;
	}

	public void setShipmentStatus(ShipmentStatus shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}


}
