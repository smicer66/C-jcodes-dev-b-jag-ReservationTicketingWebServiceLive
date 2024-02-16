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
import com.probase.reservationticketingwebservice.enumerations.VendorStatus;
  
@Entity
@Table(name="vendors")  
public class Vendor implements Serializable {  
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long vendorId;
	@Column(nullable = false)
	String vendorCode;
	@Column(nullable = false)
	String companyName;
	String contactPerson;
	String contactMobile;
	String contactEmail;
	VendorStatus vendorStatus;
	@OneToOne  
    @JoinColumn
	Coupon couponApplied;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
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

	
	
	@Enumerated(EnumType.STRING)
	public VendorStatus getVendorStatus() {
	    return vendorStatus;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
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

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorStatus(VendorStatus vendorStatus) {
		this.vendorStatus = vendorStatus;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Coupon getCouponApplied() {
		return couponApplied;
	}

	public void setCouponApplied(Coupon couponApplied) {
		this.couponApplied = couponApplied;
	}
	

}
