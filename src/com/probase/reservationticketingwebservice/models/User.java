package com.probase.reservationticketingwebservice.models;

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

import com.probase.reservationticketingwebservice.enumerations.RoleType;
import com.probase.reservationticketingwebservice.enumerations.UserStatus;

  
@Entity
@Table(name="user_accounts")  
public class User {  
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long userId; 

	@Column(nullable = false)
	String username;
	String email;
	@Column(nullable = false)
	UserStatus userStatus;
	@Column(nullable = false)
	String mobileNumber;
	String details;
	@Column(nullable = false)
	String firstName;
	@Column(nullable = false)
	String lastName;
	String uniqueId;
	String otherName;
	@Column(nullable = false)
	String password;
	String externalId;
	//RoleType roleCode;
	@Column(columnDefinition = "BIT", length = 1)
	Boolean lockOut;
	@Column(columnDefinition = "BIT", length = 1)
	Boolean requireOtp;
	Integer failedLoginCount;
	String salt;
	String privileges;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	Date lastLoginDate;
	String otp;
	String webActivationCode;
	Boolean courierEnabled;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	Station currentStation;
	@OneToOne  
    @JoinColumn
	Vendor vendor;

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
	public UserStatus getUserStatus() {
	    return userStatus;
	}
	

	/*@Enumerated(EnumType.STRING)
	public RoleType getRoleCode() {
	    return roleCode;
	}*/


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public void setUniqueId(String uniqueId)
	{
		this.uniqueId = uniqueId;
	}
	
	
	public String getUniqueId()
	{
		return this.uniqueId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getLockOut() {
		return lockOut;
	}

	public void setLockOut(Boolean lockOut) {
		this.lockOut = lockOut;
	}

	public Integer getFailedLoginCount() {
		return failedLoginCount;
	}

	public void setFailedLoginCount(Integer failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPrivileges() {
		return privileges;
	}

	public void setPrivileges(String privileges) {
		this.privileges = privileges;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getWebActivationCode() {
		return webActivationCode;
	}

	public void setWebActivationCode(String webActivationCode) {
		this.webActivationCode = webActivationCode;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	/*public void setRoleCode(RoleType roleCode) {
		this.roleCode = roleCode;
	}*/

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Boolean getRequireOtp() {
		return requireOtp;
	}

	public void setRequireOtp(Boolean requireOtp) {
		this.requireOtp = requireOtp;
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

	public Long getUserId() {
		return userId;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Station getCurrentStation() {
		return currentStation;
	}

	public void setCurrentStation(Station currentStation) {
		this.currentStation = currentStation;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Boolean getCourierEnabled() {
		return courierEnabled;
	}

	public void setCourierEnabled(Boolean courierEnabled) {
		this.courierEnabled = courierEnabled;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	
}