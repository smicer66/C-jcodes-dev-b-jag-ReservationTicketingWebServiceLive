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
import com.probase.reservationticketingwebservice.enumerations.PaymentMeans;
import com.probase.reservationticketingwebservice.enumerations.RequestType;
  
@Entity
@Table(name="audit_trail")  
public class AuditTrail implements Serializable {  
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long auditTrailId;
	@Column(nullable = false)
	String ipAddress;
	@Column(nullable = false)
	RequestType requestType;
	String requestId;
	String details;
	String username;
	Long primaryObjectIdHandled;
	String primaryObjectType;
	@OneToOne  
    @JoinColumn
	Client client;
	

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

	public Long getAuditTrailId() {
		return auditTrailId;
	}

	@Enumerated(EnumType.STRING)
	public RequestType getRequestType() {
	    return requestType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Date getUpdateAt() {
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

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getPrimaryObjectIdHandled() {
		return primaryObjectIdHandled;
	}

	public void setPrimaryObjectIdHandled(Long primaryObjectIdHandled) {
		this.primaryObjectIdHandled = primaryObjectIdHandled;
	}

	public String getPrimaryObjectType() {
		return primaryObjectType;
	}

	public void setPrimaryObjectType(String primaryObjectType) {
		this.primaryObjectType = primaryObjectType;
	}
}
