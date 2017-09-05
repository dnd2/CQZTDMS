package com.infodms.dms.po;


import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TrVehicleModelPackagePO extends PO{

	private Long groupId;
	private Long vehicleModelId;
	private Long id;

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setVehicleModelId(Long vehicleModelId){
		this.vehicleModelId=vehicleModelId;
	}

	public Long getVehicleModelId(){
		return this.vehicleModelId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}