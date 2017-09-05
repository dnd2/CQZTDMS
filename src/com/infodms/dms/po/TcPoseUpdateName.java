package com.infodms.dms.po;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.sql.DATE;

public class TcPoseUpdateName extends TcPosePO{
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 
	private String updateName;
	private String updateDates;
	
	public String getUpdateDates() {
		return updateDates;
	}

	public void setUpdateDates(String updateDates) {
		this.updateDates = updateDates;
	}

	public String getUpdateName() {
		
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
}
