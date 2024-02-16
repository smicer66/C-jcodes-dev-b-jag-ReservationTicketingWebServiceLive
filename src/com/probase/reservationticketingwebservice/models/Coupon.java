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
import com.probase.reservationticketingwebservice.enumerations.CouponStatus;
  
@Entity
@Table(name="coupons")  
public class Coupon implements Serializable {  
	
	private static final long serialVersionUID = -4978223012667661805L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long couponId;
	@Column(nullable = false, unique = true)
	String couponCode;
	@GeneratedValue(strategy=GenerationType.AUTO)
	String batchNumber;
	CouponStatus couponStatus;
	@OneToOne  
    @JoinColumn
	Customer customer;
	@OneToOne  
    @JoinColumn
	Client client;
	Date dateUsed;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	Double discountRate;
	@OneToOne  
    @JoinColumn
	Vendor assignedToVendor;
	Date expiryDate;
	
	
	
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
	public CouponStatus getCouponStatus() {
	    return couponStatus;
	}

	public void setAssignedToVendor(Vendor assignedToVendor) {
		this.assignedToVendor = assignedToVendor;
	}

	public Vendor getAssignedToVendor() {
		return assignedToVendor;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Date getDateUsed() {
		return dateUsed;
	}

	public void setDateUsed(Date dateUsed) {
		this.dateUsed = dateUsed;
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

	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public void setCouponStatus(CouponStatus couponStatus) {
		this.couponStatus = couponStatus;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
    
}

