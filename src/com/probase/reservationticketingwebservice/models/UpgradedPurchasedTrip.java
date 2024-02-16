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
import com.probase.reservationticketingwebservice.enumerations.PurchasePoint;
import com.probase.reservationticketingwebservice.enumerations.PurchasedTripStatus;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
  
@Entity
@Table(name="upgraded_purchased_trips")  
public class UpgradedPurchasedTrip implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long upgradedPurchasedTripId;
	@OneToOne
    @JoinColumn
    PurchasedTrip oldPurchasedTrip;
	@OneToOne
    @JoinColumn
    PurchasedTrip newPurchasedTrip;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	Double oldAmount;
	@Column(nullable = false)
	Double newAmount;
	@OneToOne  
    @JoinColumn
	Client client;
	
	
	
	
	
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

	public PurchasedTrip getOldPurchasedTrip() {
		return oldPurchasedTrip;
	}

	public void setOldPurchasedTrip(PurchasedTrip oldPurchasedTrip) {
		this.oldPurchasedTrip = oldPurchasedTrip;
	}

	public PurchasedTrip getNewPurchasedTrip() {
		return newPurchasedTrip;
	}

	public void setNewPurchasedTrip(PurchasedTrip newPurchasedTrip) {
		this.newPurchasedTrip = newPurchasedTrip;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Long getUpgradedPurchasedTripId() {
		return upgradedPurchasedTripId;
	}

	public Double getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(Double oldAmount) {
		this.oldAmount = oldAmount;
	}

	public Double getNewAmount() {
		return newAmount;
	}

	public void setNewAmount(Double newAmount) {
		this.newAmount = newAmount;
	}

}
