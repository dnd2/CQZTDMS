package com.infodms.dms.dao.crm.invite;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrComplaintsAuditPO;
import com.infodms.dms.po.TtFleetEditLogPO;
import com.infoservice.dms.chana.common.DEConstant;
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
public class InviteManageDao extends BaseDao<PO>{
	
	private static final InviteManageDao dao = new InviteManageDao();
	
	public static final InviteManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * FUNCTION		:	查询经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getInviteQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String telephone=map.get("telephone");
		String name=map.get("name");
		String inviteDate=map.get("inviteDate");
		String inviteDateEnd=map.get("inviteDateEnd");
		String inviteType=map.get("inviteType");
		String dealerId=map.get("dealerId");
		String logonId=map.get("logonId");
		String adviser=map.get("adviser");
		String groupId=map.get("groupId");
		String ctmRank=map.get("ctmRank");
		String finishStatus=map.get("finishStatus");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TPI.INVITE_ID,\n" );
		sql.append("       TPc.CUSTOMER_NAME,\n" );
		sql.append("       tpc.telephone,\n" );
		sql.append("       to_char(TPI.CREATE_DATE,'yyyy-mm-dd') CREATE_DATE ,\n" );  
		sql.append("       TPI.INVITE_TYPE,\n" );
		sql.append("       TPI.INVITE_WAY,\n" );
		sql.append("       TPI.director_audit,\n" );
		sql.append("      tu.name,\n" );
		sql.append(" tpc.ctm_rank,");
		sql.append("       TPI.IF_INVITE\n" );
		sql.append("  FROM T_PC_INVITE TPI, t_pc_customer tpc,tc_user tu\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("   and tpc.customer_id = tpi.customer_id");
		sql.append("   and tu.user_id (+)= tpc.adviser");
		if(null!=name&&!"".equals(name)){
			sql.append(" and tpc.customer_name like '%"+name+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.TELEPHONE  like'%"+telephone+"%'");
		}
		if(null!=inviteType&&!"".equals(inviteType)){
			sql.append(" and tpi.INVITE_TYPE ="+inviteType);
		}
		if(null!=ctmRank&&!"".equals(ctmRank)){
			sql.append(" and tpc.ctm_rank ="+ctmRank);
		}
		if(null!=finishStatus&&!"".equals(finishStatus)){
			sql.append("  and tpi.task_status="+finishStatus);
			
		}
		if(null!=inviteDate&&!"".equals(inviteDate)){
			sql.append("                           AND TPI.plan_invite_date >=\n" );
			sql.append("                               TO_DATE('"+inviteDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=inviteDateEnd&&!"".equals(inviteDateEnd)){
			sql.append("                           AND TPI.plan_invite_date <=\n" );
			sql.append("                               TO_DATE('"+inviteDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append("  and tpc.dealer_id="+dealerId);
			if(logonId!=null&&!"".equals(logonId)){
				sql.append("  and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
				sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
			}
		
		}
		if(null!=adviser&&!"".equals(adviser)){
			sql.append(" and tpc.adviser="+adviser+"\n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		sql.append("   order by tpi.create_date desc");
		
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}


	
	
	
	
}
