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

import com.probase.reservationticketingwebservice.enumerations.CardStatus;
import com.probase.reservationticketingwebservice.enumerations.CardType;
import com.probase.reservationticketingwebservice.enumerations.ClientStatus;
  
@Entity
@Table(name="lines_")  
public class Line implements Serializable {  
	
	private static final long serialVersionUID = -4978223012667661805L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long lineId;
	@Column(nullable = false)
	String lineName;
	@Column(nullable = false)
	String lineCode;
	@OneToOne  
    @JoinColumn
	Client client;
	@Column(nullable = false)
	Date createdAt;
	@Column(nullable = false)
	Date updatedAt;
	Date deletedAt;
	@Column(nullable = false)
	Boolean lineStatus;
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.createdAt = new Date();
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

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Boolean getLineStatus() {
		return lineStatus;
	}

	public void setLineStatus(Boolean lineStatus) {
		this.lineStatus = lineStatus;
	}

	public Long getLineId() {
		return lineId;
	}
	
    
}

