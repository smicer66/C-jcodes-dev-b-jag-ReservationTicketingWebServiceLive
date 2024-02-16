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
import com.probase.reservationticketingwebservice.enumerations.PriceType;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.ShipmentStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
  
@Entity
@Table(name="shipments")  
public class Shipment implements Serializable{  
	private static final long serialVersionUID = -6700990760991590860L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long shipmentId;
	@OneToOne  
    @JoinColumn
    ProductCategory productCategory;
	@OneToOne  
    @JoinColumn
    CourierService courierService;
	//@OneToOne  
    //@JoinColumn
    //TicketCollectionPoint sourceCollectionPoint;
	//@OneToOne  
    //@JoinColumn
    //TicketCollectionPoint destinationCollectionPoint;
	@OneToOne  
    @JoinColumn
    User registeredByUser;
	@OneToOne  
    @JoinColumn
    User deliveredByUser;
	String senderName; 
	@OneToOne  
    @JoinColumn
	District senderDistrict; 
	String senderCity; 
	String senderAddress; 
	String senderMobileNumber; 
	String senderEmailAddress; 
	String receiverName; 
	@OneToOne  
    @JoinColumn
	District receiverDistrict; 
	String receiverCity; 
	String receiverAddress; 
	String receiverMobileNumber;
	String receiverEmailNumber;  
	Double weight;  
	@Column(nullable = false)
	ShipmentStatus shipmentStatus; 
	//@Column(nullable = false)
	//String parcelName; 
	//@Column(nullable = false)
	//Integer parcelQuantity; 
	/*@Column(nullable = false)
	Double parcelWidth;
	@Column(nullable = false) 
	Double parcelHeight;  
	@Column(nullable = false)
	Double parcelWeight; 
	Double productValuation; 
	Double advicedDeliveryCharge;
	Double parcelVolume; */
	@Column(nullable = false)
	String trackingId; 
	String description; 
	Double deliveryCharge; 
	Double bookingFee; 
	//Double appliedPricingIndex;
	//String appliedPricingIndexDescription;
	Double serviceTax; 
	String finalReceiverName; 
	String senderSignatureImage;
	String receiverSignatureImage; 
	String productImage; 
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	Transaction transaction;
	@OneToOne  
    @JoinColumn
	VehicleSchedule vehicleSchedule;
	@OneToOne  
    @JoinColumn
	ScheduleStation departureScheduleStation;
	@OneToOne  
    @JoinColumn
	ScheduleStation arrivalScheduleStation;
	String receiptNo; 
	
	
	
	
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}


	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public CourierService getCourierService() {
		return courierService;
	}

	public void setCourierService(CourierService courierService) {
		this.courierService = courierService;
	}

	/*public TicketCollectionPoint getSourceCollectionPoint() {
		return sourceCollectionPoint;
	}

	public void setSourceCollectionPoint(TicketCollectionPoint sourceCollectionPoint) {
		this.sourceCollectionPoint = sourceCollectionPoint;
	}

	public TicketCollectionPoint getDestinationCollectionPoint() {
		return destinationCollectionPoint;
	}

	public void setDestinationCollectionPoint(
			TicketCollectionPoint destinationCollectionPoint) {
		this.destinationCollectionPoint = destinationCollectionPoint;
	}*/

	public User getRegisteredByUser() {
		return registeredByUser;
	}

	public void setRegisteredByUser(User registeredByUser) {
		this.registeredByUser = registeredByUser;
	}

	public User getDeliveredByUser() {
		return deliveredByUser;
	}

	public void setDeliveredByUser(User deliveredByUser) {
		this.deliveredByUser = deliveredByUser;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public District getSenderDistrict() {
		return senderDistrict;
	}

	public void setSenderDistrict(District senderDistrict) {
		this.senderDistrict = senderDistrict;
	}

	public String getSenderCity() {
		return senderCity;
	}

	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getSenderMobileNumber() {
		return senderMobileNumber;
	}

	public void setSenderMobileNumber(String senderMobileNumber) {
		this.senderMobileNumber = senderMobileNumber;
	}

	public String getSenderEmailAddress() {
		return senderEmailAddress;
	}

	public void setSenderEmailAddress(String senderEmailAddress) {
		this.senderEmailAddress = senderEmailAddress;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public District getReceiverDistrict() {
		return receiverDistrict;
	}

	public void setReceiverDistrict(District receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverMobileNumber() {
		return receiverMobileNumber;
	}

	public void setReceiverMobileNumber(String receiverMobileNumber) {
		this.receiverMobileNumber = receiverMobileNumber;
	}

	public String getReceiverEmailNumber() {
		return receiverEmailNumber;
	}

	public void setReceiverEmailNumber(String receiverEmailNumber) {
		this.receiverEmailNumber = receiverEmailNumber;
	}

	@Enumerated(EnumType.STRING)
	public ShipmentStatus getShipmentStatus() {
		return shipmentStatus;
	}

	public void setShipmentStatus(ShipmentStatus shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}

	/*public String getParcelName() {
		return parcelName;
	}

	public void setParcelName(String parcelName) {
		this.parcelName = parcelName;
	}

	public Integer getParcelQuantity() {
		return parcelQuantity;
	}

	public void setParcelQuantity(Integer parcelQuantity) {
		this.parcelQuantity = parcelQuantity;
	}*/


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(Double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	/*public Double getAppliedPricingIndex() {
		return appliedPricingIndex;
	}

	public void setAppliedPricingIndex(Double appliedPricingIndex) {
		this.appliedPricingIndex = appliedPricingIndex;
	}

	public String getAppliedPricingIndexDescription() {
		return appliedPricingIndexDescription;
	}

	public void setAppliedPricingIndexDescription(
			String appliedPricingIndexDescription) {
		this.appliedPricingIndexDescription = appliedPricingIndexDescription;
	}*/

	public Double getServiceTax() {
		return serviceTax;
	}

	public void setServiceTax(Double serviceTax) {
		this.serviceTax = serviceTax;
	}

	public String getFinalReceiverName() {
		return finalReceiverName;
	}

	public void setFinalReceiverName(String finalReceiverName) {
		this.finalReceiverName = finalReceiverName;
	}

	public String getSenderSignatureImage() {
		return senderSignatureImage;
	}

	public void setSenderSignatureImage(String senderSignatureImage) {
		this.senderSignatureImage = senderSignatureImage;
	}

	public String getReceiverSignatureImage() {
		return receiverSignatureImage;
	}

	public void setReceiverSignatureImage(String receiverSignatureImage) {
		this.receiverSignatureImage = receiverSignatureImage;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getShipmentId() {
		return shipmentId;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public VehicleSchedule getVehicleSchedule() {
		return vehicleSchedule;
	}

	public void setVehicleSchedule(VehicleSchedule vehicleSchedule) {
		this.vehicleSchedule = vehicleSchedule;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public ScheduleStation getDepartureScheduleStation() {
		return departureScheduleStation;
	}

	public void setDepartureScheduleStation(ScheduleStation departureScheduleStation) {
		this.departureScheduleStation = departureScheduleStation;
	}

	public ScheduleStation getArrivalScheduleStation() {
		return arrivalScheduleStation;
	}

	public void setArrivalScheduleStation(ScheduleStation arrivalScheduleStation) {
		this.arrivalScheduleStation = arrivalScheduleStation;
	}

	public Double getBookingFee() {
		return bookingFee;
	}

	public void setBookingFee(Double bookingFee) {
		this.bookingFee = bookingFee;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

}
