package com.infodms.dms.dao.report.jcafterservicereport;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class DeMonitorDataDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(DeMonitorDataDao.class);
	private static final DeMonitorDataDao dao = new DeMonitorDataDao();

	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public static final DeMonitorDataDao getInstance() {
		return dao;
	}
 
	public PageResult<Map<String, Object>> queryDemonitorStatus(String beginTime,String endTime,String process,String msg_id,String biz_type,String msg_from ,String msg_to,int curPage, int pageSize){
		       StringBuffer sql= new StringBuffer();
				sql.append("select de.msg_id,\n" );
				sql.append("       dr.create_by msg_from,\n" );
				sql.append("       dr2.create_by msg_to,\n" );
				sql.append("       de.msg_priority,\n" );
				sql.append("       de.app_name,\n" );
				sql.append("       de.adapter_name,\n" );
				sql.append("       de.biz_type,\n" );
				sql.append("       de.msg_type,\n" );
				sql.append("       de.msg_file_id,\n" );
				sql.append("       de.process,\n" );
				sql.append("       de.try_times,\n" );
				sql.append("       de.last_try_time,\n" );
				sql.append("       de.create_date\n" );
				sql.append("  from de_msg_info de,ti_dealer_relation dr,ti_dealer_relation dr2 where 1=1 and de.msg_from = dr.dms_code and de.msg_to = dr2.dms_code ");
				
				if(Utility.testString(process)){
					sql.append("  and de.process="+process+"  \n");	
				}
				if(Utility.testString(msg_id)){
					sql.append("  and de.msg_id="+msg_id+"  \n");	
				}
 				if(Utility.testString(biz_type)){
					sql.append("  and de.biz_type='"+biz_type+"'  \n");	
				}
 				if(Utility.testString(msg_from)){
					sql.append("   and de.msg_from='"+msg_from+"'  \n");	
				} 
 				if(Utility.testString(beginTime)){
					sql.append("     and de.create_date>=to_date('"+beginTime+"','YYYY-MM-DD HH24:MI:SS')  \n");	
				}  
				if(Utility.testString(endTime)){
					sql.append("     and de.create_date<=to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')  \n");	
				}  
 				if(Utility.testString(msg_to)){
					sql.append("   and de.msg_to='"+msg_to+"'  \n");	
				}
 				sql.append("  order by de.create_date desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);		
		return ps;
	}
}
