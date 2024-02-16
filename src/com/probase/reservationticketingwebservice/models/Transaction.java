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
import com.probase.reservationticketingwebservice.enumerations.TransactionCurrency;
import com.probase.reservationticketingwebservice.enumerations.PaymentMeans;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
  
@Entity
@Table(name="transactions")  
public class Transaction{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long transactionId;
	@Column(nullable = false)
	String transactionRef; 
	@Column(nullable = false)
	String orderRef;
	@Column(nullable = false)
	Channel channel;
	@Column(nullable = false)
	String transactionCurrency;
	@Column(nullable = false)
	PaymentMeans paymentMeans;
	@Column(nullable = false)
	Date transactionDate;
	@Column(nullable = false)
	ServiceType serviceType;
	@OneToOne 
    @JoinColumn 
	User transactingUser;
	@OneToOne 
    @JoinColumn 
	Station purchasingStation;
	@OneToOne 
    @JoinColumn 
	Vendor vendor;
	@OneToOne 
    @JoinColumn 
	Device device;
	String transactingUserName;
	Long userId;
	@Column(nullable = false)
	TransactionStatus status;
	@OneToOne  
    @JoinColumn
	TripCard card;
	String messageRequest;
	String messageResponse;
	@Column(nullable = false)
	Double fixedCharge;
	@Column(nullable = false)
	Double transactionFee;
	@Column(nullable = false)
	Double transactionAmount;
	Double rebateAmount;
	Integer responseCode;
	Long crCardId;
	Long refundedTransactionId;
	Long drCardId;
	String crCardPan;
	String drCardPan;
	Long crVendorId;
	Long drVendorId;
	Long crBankId;
	Long drBankId;
	String crBankName;
	String drBankName;
	@Column(nullable = false)
	String narration;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne 
    @JoinColumn 
	Wallet wallet;
	@OneToOne 
    @JoinColumn 
	Coupon appliedCoupon;
	Double concessionApplied;
	Integer retrievalCount;


	@Column(nullable = false)
	Date createdAt;
	Date updatedAt;
	Date deletedAt;
	Boolean isUpgradeAmountRefund;
	
	
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
	public ServiceType getServiceType() {
	    return serviceType;
	}
	
	@Enumerated(EnumType.STRING)
	public TransactionStatus getStatus() {
	    return status;
	}
	
	@Enumerated(EnumType.STRING)
	public Channel getChannel() {
	    return channel;
	}
	
	@Enumerated(EnumType.STRING)
	public PaymentMeans getPaymentMeans() {
	    return paymentMeans;
	}
	
	public String getTransactionCurrency() {
	    return transactionCurrency;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public String getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(String orderRef) {
		this.orderRef = orderRef;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public User getTransactingUser() {
		return transactingUser;
	}

	public void setTransactingUser(User transactingUser) {
		this.transactingUser = transactingUser;
	}

	public String getTransactingUserName() {
		return transactingUserName;
	}

	public void setTransactingUserName(String transactingUserName) {
		this.transactingUserName = transactingUserName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public TripCard getCard() {
		return card;
	}

	public void setCard(TripCard card) {
		this.card = card;
	}

	public String getMessageRequest() {
		return messageRequest;
	}

	public void setMessageRequest(String messageRequest) {
		this.messageRequest = messageRequest;
	}

	public String getMessageResponse() {
		return messageResponse;
	}

	public void setMessageResponse(String messageResponse) {
		this.messageResponse = messageResponse;
	}

	public Double getFixedCharge() {
		return fixedCharge;
	}

	public void setFixedCharge(Double fixedCharge) {
		this.fixedCharge = fixedCharge;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public Long getCrCardId() {
		return crCardId;
	}

	public void setCrCardId(Long crCardId) {
		this.crCardId = crCardId;
	}

	public Long getDrCardId() {
		return drCardId;
	}

	public void setDrCardId(Long drCardId) {
		this.drCardId = drCardId;
	}

	public String getCrCardPan() {
		return crCardPan;
	}

	public void setCrCardPan(String crCardPan) {
		this.crCardPan = crCardPan;
	}

	public String getDrCardPan() {
		return drCardPan;
	}

	public void setDrCardPan(String drCardPan) {
		this.drCardPan = drCardPan;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setPaymentMeans(PaymentMeans paymentMeans) {
		this.paymentMeans = paymentMeans;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Double getTransactionFee() {
		return transactionFee;
	}
	
	public void setTransactionFee(Double transactionFee) {
		this.transactionFee = transactionFee;
	}


	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
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

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Double getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(Double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Long getCrVendorId() {
		return crVendorId;
	}

	public void setCrVendorId(Long crVendorId) {
		this.crVendorId = crVendorId;
	}

	public Long getDrVendorId() {
		return drVendorId;
	}

	public void setDrVendorId(Long drVendorId) {
		this.drVendorId = drVendorId;
	}

	public Station getPurchasingStation() {
		return purchasingStation;
	}

	public void setPurchasingStation(Station purchasingStation) {
		this.purchasingStation = purchasingStation;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public Long getRefundedTransactionId() {
		return refundedTransactionId;
	}

	public void setRefundedTransactionId(Long refundedTransactionId) {
		this.refundedTransactionId = refundedTransactionId;
	}

	public Coupon getAppliedCoupon() {
		return appliedCoupon;
	}

	public void setAppliedCoupon(Coupon appliedCoupon) {
		this.appliedCoupon = appliedCoupon;
	}

	public Double getConcessionApplied() {
		return concessionApplied;
	}

	public void setConcessionApplied(Double concessionApplied) {
		this.concessionApplied = concessionApplied;
	}

	public Long getCrBankId() {
		return crBankId;
	}

	public void setCrBankId(Long crBankId) {
		this.crBankId = crBankId;
	}

	public Long getDrBankId() {
		return drBankId;
	}

	public void setDrBankId(Long drBankId) {
		this.drBankId = drBankId;
	}

	public String getCrBankName() {
		return crBankName;
	}

	public void setCrBankName(String crBankName) {
		this.crBankName = crBankName;
	}

	public String getDrBankName() {
		return drBankName;
	}

	public void setDrBankName(String drBankName) {
		this.drBankName = drBankName;
	}

	public Integer getRetrievalCount() {
		return retrievalCount;
	}

	public void setRetrievalCount(Integer retrievalCount) {
		this.retrievalCount = retrievalCount;
	}

	public Boolean getIsUpgradeAmountRefund() {
		return isUpgradeAmountRefund;
	}

	public void setIsUpgradeAmountRefund(Boolean isUpgradeAmountRefund) {
		this.isUpgradeAmountRefund = isUpgradeAmountRefund;
	}


	
	
}
