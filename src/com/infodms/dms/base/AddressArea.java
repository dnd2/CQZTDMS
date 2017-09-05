package com.infodms.dms.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.base.AddressAreaDAO;

public class AddressArea {
	public Logger logger = Logger.getLogger(AddressArea.class) ;
	
	AddressAreaDAO dao = AddressAreaDAO.getInstance() ;
	
	public String addressAreaStrGet(String addressId) {
		StringBuffer areaStr = new StringBuffer("") ;
		
		List<Map<String, Object>> areaNameList = dao.addressAreaInfoQuery(addressId) ;
		
		int len = areaNameList.size() ;
		
		for(int i=0; i<len; i++) {
			if(areaStr.length() == 0) {
				areaStr.append(areaNameList.get(i).get("AREA_NAME").toString()) ;
			} else {
				areaStr.append(",").append(areaNameList.get(i).get("AREA_NAME").toString()) ;
			}
		}
		
		return areaStr.toString() ;
	}
}
