package com.infodms.dms.base;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;

public class OrgBase {
	public Logger logger = Logger.getLogger(OrgBase.class) ;

	public String getOrgIdByDuty(AclUserBean logonUser) {
		String orgId = null ;
		String dutyType = logonUser.getDutyType() ;
		
		if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
			
		} else if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			orgId = logonUser.getOrgId().toString() ;
		}
		
		return orgId ;
	}
}
