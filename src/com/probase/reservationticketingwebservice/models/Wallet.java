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
@Table(name="wallets")  
public class Wallet implements Serializable {  
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long walletId;
	@Column(nullable = false)
	String walletCode;
	@Column(nullable = false)
	Double currentBalance;
	@OneToOne  
    @JoinColumn
	User lastFundedByUser;
	@OneToOne  
    @JoinColumn
	Vendor vendor;
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

	public String getWalletCode() {
		return walletCode;
	}

	public void setWalletCode(String walletCode) {
		this.walletCode = walletCode;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public User getLastFundedByUser() {
		return lastFundedByUser;
	}

	public void setLastFundedByUser(User lastFundedByUser) {
		this.lastFundedByUser = lastFundedByUser;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Long getWalletId() {
		return walletId;
	}


}
