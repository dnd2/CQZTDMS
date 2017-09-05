package com.infodms.dms.base;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.base.ExecuteDAO;

public class ExecuteBase {
	public Logger logger = Logger.getLogger(ExecuteBase.class);
	ExecuteDAO dao = ExecuteDAO.getInstance() ;
	
	public void ExecuteByType(String type, Map<String, String> map) {
		if(Constant.CAMPAIGN_TYPE_01.toString().equals(type) || Constant.CAMPAIGN_TYPE_02.toString().equals(type)) {
			dao.executeModify(map) ;
		} else if(Constant.CAMPAIGN_TYPE_03.toString().equals(type)) {
			dao.spaceExecuteModify(map) ;
		}
	}
	
	public boolean checkPrint(String type, String executeId) {
		Map<String, Object> executeMap = null ;
		
		if(Constant.CAMPAIGN_TYPE_01.toString().equals(type) || Constant.CAMPAIGN_TYPE_02.toString().equals(type)) {
			executeMap = dao.executeByIdQuery(executeId) ;
		} else if(Constant.CAMPAIGN_TYPE_03.toString().equals(type)) {
			executeMap = dao.spaceExecuteByIdQuery(executeId) ;
		}
		
		String isPrint = executeMap.get("IS_PRINT").toString() ;
		
		if(Constant.IF_TYPE_YES.toString().equals(isPrint)) {
			return true ;
		} else {
			return false ;
		}
	}
}
