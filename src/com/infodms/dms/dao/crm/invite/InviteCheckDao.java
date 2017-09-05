package com.infodms.dms.dao.crm.invite;

import java.sql.ResultSet;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**
 * 
 * <p>ComplaintAuditDao.java</p>
 *
 * <p>Description: 客户投诉处理明细持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-2</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class InviteCheckDao extends BaseDao<PO>{
	
	private static final InviteCheckDao dao = new InviteCheckDao();
	
	public static final InviteCheckDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 查询计划邀约审核的数据
	 * @param map
	 * @param parseInt
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> getInviteCheckQueryList(
			Map<String, String> map, int pageSize, Integer curPage) {
		String telephone=map.get("telephone");
		String name=map.get("name");
		String planInviteDate=map.get("planInviteDate");
		String planInviteDateEnd=map.get("planInviteDateEnd");
		String planMeetDate=map.get("planMeetDate");
		String planMeetDateEnd=map.get("planMeetDateEnd");
		String dealerId=map.get("dealerId");
		String logonId=map.get("logonId");
		String adviser=map.get("adviser");
		String groupId=map.get("groupId");
		StringBuilder sql= new StringBuilder();
		sql.append("select TU.NAME ,TPc.customer_code,\n" );
		sql.append("       TPc.customer_name,\n" );
		sql.append("       TPc.telephone,\n" );
		sql.append("       tpiv.series_name,\n" );
		sql.append("       TPI.invite_id,\n" );
		sql.append("       TPI.old_level,\n" );
		sql.append("       to_char(TPI.plan_invite_date,'yyyy-mm-dd')plan_invite_date  ,\n" );
		sql.append("       to_char(TPI.plan_meet_date,'yyyy-mm-dd')plan_meet_date\n" );
		sql.append("  from t_pc_invite TPI,t_pc_customer tpc,t_pc_intent_vehicle tpiv , tc_user tu  \n" );
		sql.append(" WHERE TPI.if_plan = 10041001\n" );
		sql.append(" and tpiv.series_id=tpc.intent_vehicle and tpc.adviser =tu.user_id \n" );
		sql.append(" and tpc.customer_id=tpi.customer_id");
		sql.append(" and tpi.director_audit="+Constant.DIRECTOR_AUDIT_01);
		
		if(null!=name&&!"".equals(name)){
			sql.append(" and tpc.customer_name like '%"+name+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.TELEPHONE  like'%"+telephone+"%'");
		}
		if(null!=planInviteDate&&!"".equals(planInviteDate)){
			sql.append("                           AND TPI.plan_invite_date >=\n" );
			sql.append("                               TO_DATE('"+planInviteDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND TPI.plan_invite_date <=\n" );
			sql.append("                               TO_DATE('"+planInviteDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=planMeetDate&&!"".equals(planMeetDate)){
			sql.append("                           AND TPI.plan_meet_date >=\n" );
			sql.append("                               TO_DATE('"+planMeetDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND TPI.plan_meet_date <=\n" );
			sql.append("                               TO_DATE('"+planMeetDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			
		}
		
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
			sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
		}
		if(Utility.testString(adviser)){
			sql.append(" and tu.user_id ="+adviser);
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		sql.append("  ORDER BY TPI.create_date DESC");

		
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}


	
	
	
	
}
