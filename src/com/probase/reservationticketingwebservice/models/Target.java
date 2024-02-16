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
import com.probase.reservationticketingwebservice.enumerations.RegionType;
import com.probase.reservationticketingwebservice.enumerations.TargetMonth;
  
@Entity
@Table(name="targets")  
public class Target implements Serializable{  
	private static final long serialVersionUID = 5457558421999610410L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long targetId;
	@Column(nullable = false)
	TargetMonth targetMonth; 
	@Column(nullable = false)
	Integer targetYear; 
	@OneToOne  
    @JoinColumn
	Client client;
	@OneToOne  
    @JoinColumn
	Line line;
	@Column(nullable = false)
	Double ticketRevenue;
	@Column(nullable = false)
	Double courierRevenue;
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
	

	
	@Enumerated(EnumType.STRING)
	public TargetMonth getTargetMonth() {
		return targetMonth;
	}

	public void setTargetMonth(TargetMonth targetMonth) {
		this.targetMonth = targetMonth;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Integer getTargetYear() {
		return targetYear;
	}

	public void setTargetYear(Integer targetYear) {
		this.targetYear = targetYear;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Double getTicketRevenue() {
		return ticketRevenue;
	}

	public void setTicketRevenue(Double ticketRevenue) {
		this.ticketRevenue = ticketRevenue;
	}

	public Double getCourierRevenue() {
		return courierRevenue;
	}

	public void setCourierRevenue(Double courierRevenue) {
		this.courierRevenue = courierRevenue;
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

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}
    
}

