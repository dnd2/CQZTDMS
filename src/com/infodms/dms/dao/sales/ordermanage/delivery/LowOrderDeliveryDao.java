package com.infodms.dms.dao.sales.ordermanage.delivery;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

public class LowOrderDeliveryDao extends BaseDao {
	public static Logger logger = Logger.getLogger(LowOrderDeliveryDao.class);
	private static final LowOrderDeliveryDao dao = new LowOrderDeliveryDao ();
	public static final LowOrderDeliveryDao getInstance() {
		return dao;
	}
	private LowOrderDeliveryDao() {}
	
	public static String getFirstDealer(String dealerId) {
		String firstDlr = "" ;
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select vod.root_dealer_id from vw_org_dealer vod where vod.dealer_id = ?\n");

		param.add(dealerId) ;
		
		Map<String, Object> dlrMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(dlrMap)) {
			firstDlr = dlrMap.get("ROOT_DEALER_ID").toString() ;
		}
		
		return firstDlr ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
