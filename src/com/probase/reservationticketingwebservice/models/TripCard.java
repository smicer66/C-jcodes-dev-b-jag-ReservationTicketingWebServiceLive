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
  
@Entity
@Table(name="trip_cards")  
public class TripCard implements Serializable {  
	
	private static final long serialVersionUID = -4978223012667661805L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long tripCardId;
	@Column(nullable = false, unique = true)
	String tripCardNumber;
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long serialNo;
	String uniqueId;
	@Column(nullable = false)
	String batchId;
	
	@OneToOne  
    @JoinColumn
	CardScheme cardScheme;
	@Column(nullable = false)
	CardStatus cardStatus;
	@OneToOne  
    @JoinColumn
	Customer customer;
	@OneToOne  
    @JoinColumn
	Client client;
	Date datePurchased;
	@Column(nullable = false)
	Double cardPrice;
	Integer currentAvailableTrips;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	Double currentBalance;
	@Column(nullable = false)
	Double totalCredited;
	@Column(nullable = false)
	Double totalDebited;
	Date lastCreditDate;
	Double lastDebitDate;
	Date subsciptionExpiryDate;
	@OneToOne  
    @JoinColumn
	Vendor assignedToVendor;
	
	
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.createdAt = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}

	
	
	@Enumerated(EnumType.STRING)
	public CardStatus getCardStatus() {
	    return cardStatus;
	}

	public Long getTripCardId() {
		return tripCardId;
	}

	public String getTripCardNumber() {
		return tripCardNumber;
	}

	public void setTripCardNumber(String tripCardNumber) {
		this.tripCardNumber = tripCardNumber;
	}

	public Long getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Long serialNo) {
		this.serialNo = serialNo;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public CardScheme getCardScheme() {
		return cardScheme;
	}

	public void setCardScheme(CardScheme cardScheme) {
		this.cardScheme = cardScheme;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = datePurchased;
	}

	public Double getCardPrice() {
		return cardPrice;
	}

	public void setCardPrice(Double cardPrice) {
		this.cardPrice = cardPrice;
	}

	public Integer getCurrentAvailableTrips() {
		return currentAvailableTrips;
	}

	public void setCurrentAvailableTrips(Integer currentAvailableTrips) {
		this.currentAvailableTrips = currentAvailableTrips;
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

	public void setCardStatus(CardStatus cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Double getTotalCredited() {
		return totalCredited;
	}

	public void setTotalCredited(Double totalCredited) {
		this.totalCredited = totalCredited;
	}

	public Double getTotalDebited() {
		return totalDebited;
	}

	public void setTotalDebited(Double totalDebited) {
		this.totalDebited = totalDebited;
	}

	public Date getLastCreditDate() {
		return lastCreditDate;
	}

	public void setLastCreditDate(Date lastCreditDate) {
		this.lastCreditDate = lastCreditDate;
	}

	public Double getLastDebitDate() {
		return lastDebitDate;
	}

	public void setLastDebitDate(Double lastDebitDate) {
		this.lastDebitDate = lastDebitDate;
	}


	public Date getSubsciptionExpiryDate() {
		return subsciptionExpiryDate;
	}

	public void setSubsciptionExpiryDate(Date subsciptionExpiryDate) {
		this.subsciptionExpiryDate = subsciptionExpiryDate;
	}

	public void setAssignedToVendor(Vendor assignedToVendor) {
		this.assignedToVendor = assignedToVendor;
	}

	public Vendor getAssignedToVendor() {
		return assignedToVendor;
	}
	
    
}

