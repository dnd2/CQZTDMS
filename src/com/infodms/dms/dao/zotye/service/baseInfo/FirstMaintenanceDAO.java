package com.infodms.dms.dao.zotye.service.baseInfo;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrQamaintainPO;
import com.infodms.dms.po.TtSalesConsultantPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class FirstMaintenanceDAO extends BaseDao {
		public Logger logger = Logger.getLogger(FirstMaintenanceDAO.class);
		
		private static final FirstMaintenanceDAO dao = new FirstMaintenanceDAO();

		public static final FirstMaintenanceDAO getInstance() {
			return dao;
		}
		private FirstMaintenanceDAO() {}
		
		public void insert(Map<String, String> map) throws ParseException {
			String textMainMile = map.get("textMainMile") ;
			String textMainDate = map.get("textMainDate") ;
			String userId = map.get("userId") ;
			
			TtAsWrQamaintainPO tq = new TtAsWrQamaintainPO() ;
			
			String id = SequenceManager.getSequence("") ;
			
			tq.setId(Long.parseLong(id)) ;
			tq.setFreeTimes(1);
			tq.setStartMileage(0.00);
			tq.setEndMileage(Double.parseDouble(textMainMile));
			tq.setMinDays(0);
			tq.setMaxDays(Integer.parseInt(textMainDate));
			tq.setCreateDate(new Date()) ;
			tq.setCreateBy(Long.parseLong(userId)) ;
			
			dao.insert(tq) ;
		}
		
		public void update(Map<String, String> map) throws ParseException {
			String id = map.get("id") ;
			String textMainMile = map.get("textMainMile") ;
			String textMainDate = map.get("textMainDate") ;
			String userId = map.get("userId") ;
			
			TtAsWrQamaintainPO tq = new TtAsWrQamaintainPO() ;
			tq.setId(Long.parseLong(id)) ;
			
			TtAsWrQamaintainPO newTq = new TtAsWrQamaintainPO() ;
			newTq.setEndMileage(Double.parseDouble(textMainMile));
			newTq.setMaxDays(Integer.parseInt(textMainDate));
			newTq.setUpdateDate(new Date()) ;
			newTq.setUpdateBy(Long.parseLong(userId)) ;
			
			dao.update(tq,newTq) ;
		}
		
		public PageResult<Map<String, Object>> query(Map<String, String> map,int pageSize,int curPage) {
			StringBuffer sql = new StringBuffer("\n") ;
			sql.append("select twq.id, twq.end_mileage, twq.max_days, twq.create_date, tcu.name\n") ;
			sql.append("  from tt_as_wr_qamaintain twq, tc_user tcu\n") ;
			sql.append(" where twq.create_by = tcu.user_id\n") ;
			
			return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage) ;
		}
		
		public List<Map<String, Object>> firstMainQuery(Map<String, String> map) {
			String id = map.get("id") ;
			
			StringBuffer sql = new StringBuffer("\n") ;
			sql.append("select twq.id, twq.end_mileage, twq.max_days, twq.create_date, tcu.name\n") ;
			sql.append("  from tt_as_wr_qamaintain twq, tc_user tcu\n") ;
			sql.append(" where twq.create_by = tcu.user_id\n") ;
			
			if(!CommonUtils.isNullString(id)) 
				sql.append("   and twq.id = ").append(id).append("\n") ;


			return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		}
		
		@Override
		protected PO wrapperPO(ResultSet rs, int idx) {
			// TODO Auto-generated method stub
			return null;
		}
		
	
}
