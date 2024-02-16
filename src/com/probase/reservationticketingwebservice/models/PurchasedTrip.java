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
@Table(name="purchased_trips")  
public class PurchasedTrip implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long purchasedTripId;
	@OneToOne
    @JoinColumn
    TicketCollectionPoint ticketCollectionPoint;
	Double amountPayable;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	PurchasedTripStatus purchasedTripStatus; 
	@OneToOne  
    @JoinColumn
	Transaction transaction;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	VehicleSchedule vehicleSchedule;
	@Column(nullable = false)
	String receiptNo;
	Integer passengerCount;
	Double virtualAmountPayable;
	Double bookingFee;
	String messageResponse;
	@OneToOne  
    @JoinColumn
	Station departureStation;
	Date departureTime;
	@OneToOne  
    @JoinColumn
	Station arrivalStation;
	@OneToOne  
    @JoinColumn
	GroupTripRequest groupTripRequest;
	Date arrivalTime;
	Double surchargedAmount;
	Integer adultPassengers;
	Integer childPassengers;
	Integer seniorPassengers;
	Integer disabledPassengers;
	Integer totalUpgradedAdultPassengers;
	Integer totalUpgradedChildPassengers;
	Integer totalUpgradedSeniorPassengers;
	Integer totalUpgradedDisabledPassengers;
	Double totalUpgradedAmount;
	Boolean isUpgradeAmountRefund;
	PurchasePoint purchasePoint;
	@OneToOne  
    @JoinColumn
	Transaction surchargeTransaction;
	
	
	
	
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

	public Long getPurchasedTripId() {
		return purchasedTripId;
	}


	public TicketCollectionPoint getTicketCollectionPoint() {
		return ticketCollectionPoint;
	}

	public void setTicketCollectionPoint(TicketCollectionPoint ticketCollectionPoint) {
		this.ticketCollectionPoint = ticketCollectionPoint;
	}

	public Double getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(Double amountPayable) {
		this.amountPayable = amountPayable;
	}
	
	@Enumerated(EnumType.STRING)
	public PurchasedTripStatus getPurchasedTripStatus() {
		return purchasedTripStatus;
	}

	public void setPurchasedTripStatus(PurchasedTripStatus purchasedTripStatus) {
		this.purchasedTripStatus = purchasedTripStatus;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Integer getPassengerCount() {
		return passengerCount;
	}

	public void setPassengerCount(Integer passengerCount) {
		this.passengerCount = passengerCount;
	}

	public Double getVirtualAmountPayable() {
		return virtualAmountPayable;
	}

	public void setVirtualAmountPayable(Double virtualAmountPayable) {
		this.virtualAmountPayable = virtualAmountPayable;
	}

	public String getMessageResponse() {
		return messageResponse;
	}

	public void setMessageResponse(String messageResponse) {
		this.messageResponse = messageResponse;
	}

	public VehicleSchedule getVehicleSchedule() {
		return vehicleSchedule;
	}

	public void setVehicleSchedule(VehicleSchedule vehicleSchedule) {
		this.vehicleSchedule = vehicleSchedule;
	}

	public Station getDepartureStation() {
		return departureStation;
	}

	public void setDepartureStation(Station departureStation) {
		this.departureStation = departureStation;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Station getArrivalStation() {
		return arrivalStation;
	}

	public void setArrivalStation(Station arrivalStation) {
		this.arrivalStation = arrivalStation;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Double getBookingFee() {
		return bookingFee;
	}

	public void setBookingFee(Double bookingFee) {
		this.bookingFee = bookingFee;
	}

	public GroupTripRequest getGroupTripRequest() {
		return groupTripRequest;
	}

	public void setGroupTripRequest(GroupTripRequest groupTripRequest) {
		this.groupTripRequest = groupTripRequest;
	}

	public Double getSurchargedAmount() {
		return surchargedAmount;
	}

	public void setSurchargedAmount(Double surchargedAmount) {
		this.surchargedAmount = surchargedAmount;
	}

	public Integer getAdultPassengers() {
		return adultPassengers;
	}

	public void setAdultPassengers(Integer adultPassengers) {
		this.adultPassengers = adultPassengers;
	}

	public Integer getChildPassengers() {
		return childPassengers;
	}

	public void setChildPassengers(Integer childPassengers) {
		this.childPassengers = childPassengers;
	}

	public Integer getSeniorPassengers() {
		return seniorPassengers;
	}

	public void setSeniorPassengers(Integer seniorPassengers) {
		this.seniorPassengers = seniorPassengers;
	}

	public Integer getDisabledPassengers() {
		return disabledPassengers;
	}

	public void setDisabledPassengers(Integer disabledPassengers) {
		this.disabledPassengers = disabledPassengers;
	}

	public Transaction getSurchargeTransaction() {
		return surchargeTransaction;
	}

	public void setSurchargeTransaction(Transaction surchargeTransaction) {
		this.surchargeTransaction = surchargeTransaction;
	}

	@Enumerated(EnumType.STRING)
	public PurchasePoint getPurchasePoint() {
		return purchasePoint;
	}

	public void setPurchasePoint(PurchasePoint purchasePoint) {
		this.purchasePoint = purchasePoint;
	}

	public Integer getTotalUpgradedAdultPassengers() {
		return totalUpgradedAdultPassengers;
	}

	public void setTotalUpgradedAdultPassengers(Integer totalUpgradedAdultPassengers) {
		this.totalUpgradedAdultPassengers = totalUpgradedAdultPassengers;
	}

	public Integer getTotalUpgradedChildPassengers() {
		return totalUpgradedChildPassengers;
	}

	public void setTotalUpgradedChildPassengers(Integer totalUpgradedChildPassengers) {
		this.totalUpgradedChildPassengers = totalUpgradedChildPassengers;
	}

	public Integer getTotalUpgradedSeniorPassengers() {
		return totalUpgradedSeniorPassengers;
	}

	public void setTotalUpgradedSeniorPassengers(
			Integer totalUpgradedSeniorPassengers) {
		this.totalUpgradedSeniorPassengers = totalUpgradedSeniorPassengers;
	}

	public Integer getTotalUpgradedDisabledPassengers() {
		return totalUpgradedDisabledPassengers;
	}

	public void setTotalUpgradedDisabledPassengers(
			Integer totalUpgradedDisabledPassengers) {
		this.totalUpgradedDisabledPassengers = totalUpgradedDisabledPassengers;
	}

	public Double getTotalUpgradedAmount() {
		return totalUpgradedAmount;
	}

	public void setTotalUpgradedAmount(Double totalUpgradedAmount) {
		this.totalUpgradedAmount = totalUpgradedAmount;
	}

	public Boolean getIsUpgradeAmountRefund() {
		return isUpgradeAmountRefund;
	}

	public void setIsUpgradeAmountRefund(Boolean isUpgradeAmountRefund) {
		this.isUpgradeAmountRefund = isUpgradeAmountRefund;
	}
	
}
