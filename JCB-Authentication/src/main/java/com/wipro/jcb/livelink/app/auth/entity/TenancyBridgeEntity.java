package com.wipro.jcb.livelink.app.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:11-07-2024
 * project: JCB-Common-API-New
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenancyBridgeEntity {

    int parentId;
    int childId;
    int level;
    String bottomFlag;
    String topFlag;
    
    
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getChildId() {
		return childId;
	}
	public void setChildId(int childId) {
		this.childId = childId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getBottomFlag() {
		return bottomFlag;
	}
	public void setBottomFlag(String bottomFlag) {
		this.bottomFlag = bottomFlag;
	}
	public String getTopFlag() {
		return topFlag;
	}
	public void setTopFlag(String topFlag) {
		this.topFlag = topFlag;
	}
    
    

}
