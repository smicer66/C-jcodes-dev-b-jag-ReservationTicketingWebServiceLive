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
import com.probase.reservationticketingwebservice.enumerations.ClientServiceType;
import com.probase.reservationticketingwebservice.enumerations.ClientStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
  
@Entity
@Table(name="clients")  
public class Client implements Serializable {  
	
	private static final long serialVersionUID = -4978223012667661805L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long clientId;
	@Column(nullable = false)
	String clientName;
	@Column(nullable = false)
	String privateKey;
	@Column(nullable = false)
	String publicKey;
	String clientAddressLine1;
	String clientAddressLine2;
	String clientCode;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	Boolean clientStatus;
	Boolean returnTripsAvailable;
	Boolean fixedPassengerCountPerTrip;
	Integer lockDownInterval;
	Double bookingFee;
	VehicleType vehicleType;
	Integer summarizeAfterDays;
	
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

	public Long getClientId() {
		return clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}


	public String getClientAddressLine1() {
		return clientAddressLine1;
	}

	public void setClientAddressLine1(String clientAddressLine1) {
		this.clientAddressLine1 = clientAddressLine1;
	}

	public String getClientAddressLine2() {
		return clientAddressLine2;
	}

	public void setClientAddressLine2(String clientAddressLine2) {
		this.clientAddressLine2 = clientAddressLine2;
	}


	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public Boolean getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(Boolean clientStatus) {
		this.clientStatus = clientStatus;
	}

	public Boolean getReturnTripsAvailable() {
		return returnTripsAvailable;
	}

	public void setReturnTripsAvailable(Boolean returnTripsAvailable) {
		this.returnTripsAvailable = returnTripsAvailable;
	}

	public Integer getLockDownInterval() {
		return lockDownInterval;
	}

	public void setLockDownInterval(Integer lockDownInterval) {
		this.lockDownInterval = lockDownInterval;
	}

	public Boolean getFixedPassengerCountPerTrip() {
		return fixedPassengerCountPerTrip;
	}

	public void setFixedPassengerCountPerTrip(Boolean fixedPassengerCountPerTrip) {
		this.fixedPassengerCountPerTrip = fixedPassengerCountPerTrip;
	}

	public Double getBookingFee() {
		return bookingFee;
	}

	public void setBookingFee(Double bookingFee) {
		this.bookingFee = bookingFee;
	}

	@Enumerated(EnumType.STRING)
	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Integer getSummarizeAfterDays() {
		return summarizeAfterDays;
	}

	public void setSummarizeAfterDays(Integer summarizeAfterDays) {
		this.summarizeAfterDays = summarizeAfterDays;
	}
	
    
}

