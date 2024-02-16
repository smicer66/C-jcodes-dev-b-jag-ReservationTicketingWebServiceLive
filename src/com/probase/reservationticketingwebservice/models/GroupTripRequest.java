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

import com.probase.reservationticketingwebservice.enumerations.GroupTripRequestStatus;
import com.probase.reservationticketingwebservice.enumerations.TripType;
import com.probase.reservationticketingwebservice.enumerations.VehicleStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
  
@Entity
@Table(name="group_trip_requests")  
public class GroupTripRequest implements Serializable {  
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long groupTripRequestId; 
	GroupTripRequestStatus groupTripRequestStatus;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@OneToOne  
    @JoinColumn
	Station departingStation;
	@OneToOne  
    @JoinColumn
	Station arrivingStation;
	@OneToOne  
    @JoinColumn
	VehicleSeatClass stationCabinType;
	TripType tripType;
	@Column(nullable = false)
	Date departureDate;
	Date returnDate;
	@Column(nullable = false)
	String details;
	@Column(nullable = false)
	String firstName;
	@Column(nullable = false)
	String lastName;
	@Column(nullable = false)
	String emailAddress;
	String altMobileNumber;
	@Column(nullable = false)
	String mobileNumber;
	String nationalId;
	@Column(nullable = false)
	String requestCode;
	@Column(nullable = false)
	Integer childPassengerCount;
	@Column(nullable = false)
	Integer adultPassengerCount;
	@Column(nullable = false)
	Integer seniorPassengerCount;
	@Column(nullable = false)
	Integer disabledPassengerCount;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	User requestByUser;
	@OneToOne  
    @JoinColumn
	User approvedByUser;
	
	
	

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
	public GroupTripRequestStatus getGroupTripRequestStatus() {
		return groupTripRequestStatus;
	}

	public void setGroupTripRequestStatus(
			GroupTripRequestStatus groupTripRequestStatus) {
		this.groupTripRequestStatus = groupTripRequestStatus;
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

	public Station getDepartingStation() {
		return departingStation;
	}

	public void setDepartingStation(Station departingStation) {
		this.departingStation = departingStation;
	}

	public Station getArrivingStation() {
		return arrivingStation;
	}

	public void setArrivingStation(Station arrivingStation) {
		this.arrivingStation = arrivingStation;
	}

	public VehicleSeatClass getStationCabinType() {
		return stationCabinType;
	}

	public void setStationCabinType(VehicleSeatClass stationCabinType) {
		this.stationCabinType = stationCabinType;
	}

	@Enumerated(EnumType.STRING)
	public TripType getTripType() {
		return tripType;
	}

	public void setTripType(TripType tripType) {
		this.tripType = tripType;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public User getRequestByUser() {
		return requestByUser;
	}

	public void setRequestByUser(User requestByUser) {
		this.requestByUser = requestByUser;
	}

	public Long getGroupTripRequestId() {
		return groupTripRequestId;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getAltMobileNumber() {
		return altMobileNumber;
	}

	public void setAltMobileNumber(String altMobileNumber) {
		this.altMobileNumber = altMobileNumber;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	public Integer getChildPassengerCount() {
		return childPassengerCount;
	}

	public void setChildPassengerCount(Integer childPassengerCount) {
		this.childPassengerCount = childPassengerCount;
	}

	public Integer getAdultPassengerCount() {
		return adultPassengerCount;
	}

	public void setAdultPassengerCount(Integer adultPassengerCount) {
		this.adultPassengerCount = adultPassengerCount;
	}

	public Integer getSeniorPassengerCount() {
		return seniorPassengerCount;
	}

	public void setSeniorPassengerCount(Integer seniorPassengerCount) {
		this.seniorPassengerCount = seniorPassengerCount;
	}

	public Integer getDisabledPassengerCount() {
		return disabledPassengerCount;
	}

	public void setDisabledPassengerCount(Integer disabledPassengerCount) {
		this.disabledPassengerCount = disabledPassengerCount;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public User getApprovedByUser() {
		return approvedByUser;
	}

	public void setApprovedByUser(User approvedByUser) {
		this.approvedByUser = approvedByUser;
	}



	
}
