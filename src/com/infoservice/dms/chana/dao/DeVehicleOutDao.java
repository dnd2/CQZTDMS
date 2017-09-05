package com.infoservice.dms.chana.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.dms.chana.common.DEConstant;

public class DeVehicleOutDao extends AbstractIFDao implements DEConstant{
	public static Logger logger = Logger.getLogger(DeVehicleOutDao.class);
	private static final DeVehicleOutDao dao = new DeVehicleOutDao ();
	public static final DeVehicleOutDao getInstance() {
		return dao;
	}
	
	public Map<String, Object> getDealer(Long dealerId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.DEALER_CODE,A.DEALER_NAME FROM TM_DEALER A WHERE A.DEALER_ID =\n");
		sql.append(dealerId);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	public HashMap<Integer, String> transType (List<String> vins){
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		for (int i = 0; i < vins.size(); i++) {
			map.put(i, vins.get(i));
		}
		return map;
	}
}
