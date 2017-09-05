package com.infodms.dms.dao.base;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCampaignExecutePO;
import com.infodms.dms.po.TtCampaignSpaceExecutePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

public class ExecuteDAO extends BaseDao<PO> {
	public Logger logger = Logger.getLogger(ExecuteDAO.class);
	private ExecuteDAO() {
		
	}
	
	private static class ExecuteDAOSingleton {
		private static ExecuteDAO dao = new ExecuteDAO() ;
	}
	
	public static ExecuteDAO getInstance() {
		return ExecuteDAOSingleton.dao ;
	}
	
	public int executeModify(Map<String, String> map) {
		String isPrint = map.get("isPrint") ;
		String printBy = map.get("printBy") ;
		String printDate = map.get("printDate") ;
		String executeId = map.get("executeId") ;
		
		TtCampaignExecutePO oldTce = new TtCampaignExecutePO() ;
		oldTce.setExecuteId(Long.parseLong(executeId)) ;
		TtCampaignExecutePO newTce = new TtCampaignExecutePO() ;
		
		if(!CommonUtils.isNullString(isPrint)) {
			newTce.setIsPrint(Long.parseLong(isPrint)) ;
		}
		
		if(!CommonUtils.isNullString(printBy)) {
			newTce.setPrintBy(Long.parseLong(printBy)) ;
		}
		
		if(!CommonUtils.isNullString(printDate)) {
			newTce.setPrintDate(new Date(Long.parseLong(printDate))) ;
		}
		
		return super.update(oldTce, newTce) ;
	}
	
	public int spaceExecuteModify(Map<String, String> map) {
		String isPrint = map.get("isPrint") ;
		String printBy = map.get("printBy") ;
		String printDate = map.get("printDate") ;
		String executeId = map.get("executeId") ;
		
		TtCampaignSpaceExecutePO oldTsce = new TtCampaignSpaceExecutePO() ;
		oldTsce.setSpaceId(Long.parseLong(executeId)) ;
		TtCampaignSpaceExecutePO newTsce = new TtCampaignSpaceExecutePO() ;
		
		if(!CommonUtils.isNullString(isPrint)) {
			newTsce.setIsPrint(Long.parseLong(isPrint)) ;
		}
		
		if(!CommonUtils.isNullString(printBy)) {
			newTsce.setPrintBy(Long.parseLong(printBy)) ;
		}
		
		if(!CommonUtils.isNullString(printDate)) {
			newTsce.setPrintDate(new Date(Long.parseLong(printDate))) ;
		}
		
		return super.update(oldTsce, newTsce) ;
	}
	
	public Map<String, Object> executeByIdQuery(String executeId) {
		List<Object> params = new ArrayList<Object>() ;
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select nvl(tce.is_print, ").append(Constant.IF_TYPE_NO).append(") is_print from tt_campaign_execute tce where tce.execute_id = ?\n");
		params.add(executeId) ;
		
		return super.pageQueryMap(sql.toString(), params, super.getFunName()) ;
	}
	
	public Map<String, Object> spaceExecuteByIdQuery(String executeId) {
		List<Object> params = new ArrayList<Object>() ;
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select nvl(tcse.is_print, ").append(Constant.IF_TYPE_NO).append(") is_print from tt_campaign_space_execute tcse where tcse.space_id = ?\n");
		params.add(executeId) ;
		
		return super.pageQueryMap(sql.toString(), params, super.getFunName()) ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
