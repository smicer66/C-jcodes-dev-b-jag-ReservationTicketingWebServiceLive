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
import com.probase.reservationticketingwebservice.enumerations.TripSeatDirection;
import com.probase.reservationticketingwebservice.enumerations.VehicleSeatLocation;
  
@Entity
@Table(name="ticket_collection_points")  
public class TicketCollectionPoint implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long ticketCollectionPointId;
	@Column(nullable = false)
	String collectionPointName;
	@Column(nullable = false)
	String collectionPointCode;
	String courierEnabled;
	@OneToOne  
    @JoinColumn
    District district;
	@OneToOne  
    @JoinColumn
    Client client;
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

	public Long getTicketCollectionPointId() {
		return ticketCollectionPointId;
	}

	public String getCollectionPointName() {
		return collectionPointName;
	}

	public void setCollectionPointName(String collectionPointName) {
		this.collectionPointName = collectionPointName;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getCollectionPointCode() {
		return collectionPointCode;
	}

	public void setCollectionPointCode(String collectionPointCode) {
		this.collectionPointCode = collectionPointCode;
	}

	public String getCourierEnabled() {
		return courierEnabled;
	}

	public void setCourierEnabled(String courierEnabled) {
		this.courierEnabled = courierEnabled;
	}


	
}
