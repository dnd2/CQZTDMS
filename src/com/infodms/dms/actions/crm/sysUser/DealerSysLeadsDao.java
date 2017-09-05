package com.infodms.dms.actions.crm.sysUser;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infodms.dms.dao.crm.dealerleadsmanage.DlrLeadsManageDao;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerSysLeadsDao extends BaseDao {
	public static Logger logger = Logger.getLogger(DealerSysLeadsDao.class);
	private static final DealerSysLeadsDao dao = new DealerSysLeadsDao();

	public static final DealerSysLeadsDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	

	/*
	 * 获取线索来源类型配置
	 */
	public List<DynaBean> getLeadsOriginType() {
		StringBuffer sql = new StringBuffer("");
		sql.append("select tc.code_id,tc.code_desc from tc_code tc where tc.type='6015' and  tc.is_down='60150000' order by tc.can_modify "); 
	    
		System.out.println("语句:"+sql);
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取线索来源类型配置
	 */
	public List<DynaBean> getAdviserInfo(String CompanyId){
		StringBuffer sql = new StringBuffer("");
		sql.append("select tu.user_id,tu.name from tc_user tu where tu.company_id='"+CompanyId+"' and tu.pose_rank='60281004' and tu.user_status='10011001' and tu.is_lock='0' order by user_id "); 
	    
		System.out.println("顾问语句:"+sql);
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取线索来源类型配置
	 */
	public List<DynaBean> getSelectAdviserInfo(String CompanyId,String originType){
		StringBuffer sql = new StringBuffer("");
		sql.append("select tu.user_id,tu.name,lor.leads_origin from tc_user tu left join LEADS_ORIGIN_USER_REALTION lor " );
		sql.append(" on tu.user_id=lor.user_id and lor.leads_origin='"+originType+"' where tu.company_id='"+CompanyId+"' and tu.pose_rank='60281004' and tu.user_status='10011001' and tu.is_lock='0' order by user_id "); 
	    
		System.out.println("顾问语句:"+sql);
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	

	/*
	 * 
	 */
	public void deleteUserOriginRelation(Map<String, String> map) {
		System.out.println("执行了方法deleteUserOriginRelation");
		StringBuffer sql = new StringBuffer("\n") ;
	
	    String dealerId=map.get("dealerId");
	    String originType=map.get("originType");
	   // String userId=map.get("userId");
		sql.append("DELETE FROM  LEADS_ORIGIN_USER_REALTION LOR WHERE LOR.DEALER_ID='"+dealerId+"' AND LOR.LEADS_ORIGIN='"+originType+"' \n");
	
		System.out.println("语句是InsertUserOriginRelation:"+sql);
		dao.insert(sql.toString());
		
	}
	
	
	/*
	 * 
	 */
	public void InsertUserOriginRelation(Map<String, String> map) {
		System.out.println("执行了方法InsertUserOriginRelation");
		StringBuffer sql = new StringBuffer("\n") ;
	
	    String dealerId=map.get("dealerId");
	    String originType=map.get("originType");
	    String userId=map.get("userId");
		sql.append("INSERT INTO LEADS_ORIGIN_USER_REALTION \n");
		sql.append("( DEALER_ID,USER_ID,USER_NAME,LEADS_ORIGIN,LEADS_ORIGIN_NAME ) ");
		sql.append(" VALUES ");
		sql.append(" ( '"+dealerId+"','"+userId+"','','"+originType+"','' ) ");
		System.out.println("语句是InsertUserOriginRelation:"+sql);
		dao.insert(sql.toString()) ;
		
	}
	
	
	
	
}