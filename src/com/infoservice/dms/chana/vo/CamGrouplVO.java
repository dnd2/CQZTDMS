package com.infoservice.dms.chana.vo;

import com.infoservice.de.convertor.f2.VO;

public class CamGrouplVO implements VO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupCode;       //物料组CODE
	private String groupName;       //物料组名称
	private String treeCode;        //物料树代码
	private Integer groupLevel;      //物料组级别
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTreeCode() {
		return treeCode;
	}
	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}
	public Integer getGroupLevel() {
		return groupLevel;
	}
	public void setGroupLevel(Integer groupLevel) {
		this.groupLevel = groupLevel;
	}
	public String toXMLString() {
		// TODO Auto-generated method stub
		return null;
	}
}
