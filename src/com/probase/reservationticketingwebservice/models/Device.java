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

import com.probase.reservationticketingwebservice.enumerations.DeviceStatus;
import com.probase.reservationticketingwebservice.enumerations.DeviceType;
  
@Entity
@Table(name="devices")  
public class Device implements Serializable{  
	private static final long serialVersionUID = 5457558421999610410L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long deviceId;
	@Column(nullable = false)
	DeviceType deviceType;
	@Column(nullable = false)
	DeviceStatus status;
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	User setupByUser;
	@Column(nullable = false)
	String deviceCode; 
	String deviceSerialNo;
	@Column(nullable = false)
	Date createdAt;
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = true)
	String terminalApiKey;
	
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
	public DeviceStatus getStatus() {
	    return status;
	}
	
	@Enumerated(EnumType.STRING)
	public DeviceType getDeviceType() {
	    return deviceType;
	}
	
	public Long getDeviceId() {
		return deviceId;
	}

	

	public Date getCreatedAt() {
		return createdAt;
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

	public void setDeleted_at(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getDeviceSerialNo() {
		return deviceSerialNo;
	}

	public void setDeviceSerialNo(String deviceSerialNo) {
		this.deviceSerialNo = deviceSerialNo;
	}
	
	public User getSetupByUser() {
		return setupByUser;
	}

	public void setSetupByUser(User setupByUser) {
		this.setupByUser = setupByUser;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public void setStatus(DeviceStatus status) {
		this.status = status;
	}

	public String getTerminalApiKey() {
		return terminalApiKey;
	}

	public void setTerminalApiKey(String terminalApiKey) {
		this.terminalApiKey = terminalApiKey;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	
	
    
}

