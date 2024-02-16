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

import com.probase.reservationticketingwebservice.enumerations.CardType;
import com.probase.reservationticketingwebservice.enumerations.CustomerStatus;
import com.probase.reservationticketingwebservice.enumerations.CustomerType;
  
@Entity
@Table(name="customers")  
public class Customer implements Serializable {  
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long customerId;
	@Column(nullable = false)
	String verificationNumber;
	@Column(nullable = false)
	String meansOfId;
	String firstName;
	String lastName;
	String otherName;
	@OneToOne  
    @JoinColumn
	User user;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	PurchasedTrip purchasedTrip;
	@Column(nullable = false)
	String mobileNumber;
	String emailAddress;
	CustomerStatus customerStatus;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	Boolean isLeadPassenger;
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}

	
	
	@Enumerated(EnumType.STRING)
	public CustomerStatus getCustomerStatus() {
	    return customerStatus;
	}
	
	public String getVerificationNumber() {
		return verificationNumber;
	}

	public void setVerificationNumber(String verificationNumber) {
		this.verificationNumber = verificationNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	
	public void setCustomerStatus(CustomerStatus customerStatus) {
		this.customerStatus = customerStatus;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public String getMeansOfId() {
		return meansOfId;
	}

	public void setMeansOfId(String meansOfId) {
		this.meansOfId = meansOfId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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

	public PurchasedTrip getPurchasedTrip() {
		return purchasedTrip;
	}

	public void setPurchasedTrip(PurchasedTrip purchasedTrip) {
		this.purchasedTrip = purchasedTrip;
	}

	public Boolean getIsLeadPassenger() {
		return isLeadPassenger;
	}

	public void setIsLeadPassenger(Boolean isLeadPassenger) {
		this.isLeadPassenger = isLeadPassenger;
	}

}
