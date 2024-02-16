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

import com.probase.reservationticketingwebservice.enumerations.TripCardChargeMode;
import com.probase.reservationticketingwebservice.enumerations.TripCardType;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
  
@Entity
@Table(name="card_schemes")  
public class CardScheme  implements Serializable{  
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long cardSchemeId; 
	@Column(nullable = false)
	String schemeName;
	Integer lowerAgeBoundary;
	Integer upperAgeBoundary;
	Double fixedCharge;
	Double transactionCharge;
	Boolean dayMondayApplicable;
	Boolean dayTuesdayApplicable;
	Boolean dayWednesdayApplicable;
	Boolean dayThursdayApplicable;
	Boolean dayFridayApplicable;
	Boolean daySaturdayApplicable;
	Boolean daySundayApplicable;
	Double discountRate;
	String schemeCode;
	Date expiryDate;
	Integer maxTripCount;
	Integer validityPeriod;
	String schemeDetail;
	@Column(nullable = false)
	Double vendorCardPrice;
	@Column(nullable = false)
	Double cardPrice;
	@Column(nullable = false)
	Double yearlyPrice;
	@OneToOne  
    @JoinColumn
	Client client;
	@Column(nullable = false)
	Boolean cardSchemeStatus;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	TripCardChargeMode tripCardChargeMode; 
	Boolean openToVendor; 
	TripCardType tripCardType;
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}


	public Double getYearlyPrice() {
		return yearlyPrice;
	}

	public void setYearlyPrice(Double yearlyPrice) {
		this.yearlyPrice = yearlyPrice;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}


	public String getSchemeDetail() {
		return schemeDetail;
	}

	public void setSchemeDetail(String schemeDetail) {
		this.schemeDetail = schemeDetail;
	}

	public Boolean getCardSchemeStatus() {
		return cardSchemeStatus;
	}

	public void setCardSchemeStatus(Boolean cardSchemeStatus) {
		this.cardSchemeStatus = cardSchemeStatus;
	}

	public Long getCardSchemeId() {
		return cardSchemeId;
	}

	public Integer getLowerAgeBoundary() {
		return lowerAgeBoundary;
	}

	public void setLowerAgeBoundary(Integer lowerAgeBoundary) {
		this.lowerAgeBoundary = lowerAgeBoundary;
	}

	public Integer getUpperAgeBoundary() {
		return upperAgeBoundary;
	}

	public void setUpperAgeBoundary(Integer upperAgeBoundary) {
		this.upperAgeBoundary = upperAgeBoundary;
	}


	public Boolean getDayMondayApplicable() {
		return dayMondayApplicable;
	}

	public void setDayMondayApplicable(Boolean dayMondayApplicable) {
		this.dayMondayApplicable = dayMondayApplicable;
	}

	public Boolean getDayTuesdayApplicable() {
		return dayTuesdayApplicable;
	}

	public void setDayTuesdayApplicable(Boolean dayTuesdayApplicable) {
		this.dayTuesdayApplicable = dayTuesdayApplicable;
	}

	public Boolean getDayWednesdayApplicable() {
		return dayWednesdayApplicable;
	}

	public void setDayWednesdayApplicable(Boolean dayWednesdayApplicable) {
		this.dayWednesdayApplicable = dayWednesdayApplicable;
	}

	public Boolean getDayThursdayApplicable() {
		return dayThursdayApplicable;
	}

	public void setDayThursdayApplicable(Boolean dayThursdayApplicable) {
		this.dayThursdayApplicable = dayThursdayApplicable;
	}

	public Boolean getDayFridayApplicable() {
		return dayFridayApplicable;
	}

	public void setDayFridayApplicable(Boolean dayFridayApplicable) {
		this.dayFridayApplicable = dayFridayApplicable;
	}

	public Boolean getDaySaturdayApplicable() {
		return daySaturdayApplicable;
	}

	public void setDaySaturdayApplicable(Boolean daySaturdayApplicable) {
		this.daySaturdayApplicable = daySaturdayApplicable;
	}

	public Boolean getDaySundayApplicable() {
		return daySundayApplicable;
	}

	public void setDaySundayApplicable(Boolean daySundayApplicable) {
		this.daySundayApplicable = daySundayApplicable;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getMaxTripCount() {
		return maxTripCount;
	}

	public void setMaxTripCount(Integer maxTripCount) {
		this.maxTripCount = maxTripCount;
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

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Double getFixedCharge() {
		return fixedCharge;
	}

	public void setFixedCharge(Double fixedCharge) {
		this.fixedCharge = fixedCharge;
	}

	public Double getTransactionCharge() {
		return transactionCharge;
	}

	public void setTransactionCharge(Double transactionCharge) {
		this.transactionCharge = transactionCharge;
	}

	public TripCardChargeMode getTripCardChargeMode() {
		return tripCardChargeMode;
	}

	public void setTripCardChargeMode(TripCardChargeMode tripCardChargeMode) {
		this.tripCardChargeMode = tripCardChargeMode;
	}

	public Integer getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(Integer validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public Double getCardPrice() {
		return cardPrice;
	}

	public void setCardPrice(Double cardPrice) {
		this.cardPrice = cardPrice;
	}

	public Double getVendorCardPrice() {
		return vendorCardPrice;
	}

	public void setVendorCardPrice(Double vendorCardPrice) {
		this.vendorCardPrice = vendorCardPrice;
	}

	public Boolean getOpenToVendor() {
		return openToVendor;
	}

	public void setOpenToVendor(Boolean openToVendor) {
		this.openToVendor = openToVendor;
	}

	@Enumerated(EnumType.STRING)
	public TripCardType getTripCardType() {
		return tripCardType;
	}

	public void setTripCardType(TripCardType tripCardType) {
		this.tripCardType = tripCardType;
	}
}
