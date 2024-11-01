package com.user.nosql.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OrderMetadata {
    @Id
    private Long orderId;
    private Map<String, Object> preferences;
    private String notes;
    private boolean doorBellFlag;
    private boolean customFlag;

    
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Map<String, Object> getPreferences() {
		return preferences;
	}
	public void setPreferences(Map<String, Object> preferences) {
		this.preferences = preferences;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public boolean isDoorBellFlag() {
		return doorBellFlag;
	}
	public void setDoorBellFlag(boolean doorBellFlag) {
		this.doorBellFlag = doorBellFlag;
	}
	public boolean isCustomFlag() {
		return customFlag;
	}
	public void setCustomFlag(boolean customFlag) {
		this.customFlag = customFlag;
	}
    
}