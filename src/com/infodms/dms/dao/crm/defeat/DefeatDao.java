package com.infodms.dms.dao.crm.defeat;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
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
public class DefeatDao extends BaseDao<PO>{
	
	private static final DefeatDao dao = new DefeatDao();
	
	public static final DefeatDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * FUNCTION		:	查询所有待审核的战败记录
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getDefeatQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String ctmName=map.get("ctmName");
		String telephone=map.get("telephone");
		String defeatDate=map.get("defeatDate");
		String defeatDateEnd=map.get("defeatDateEnd");
		String ctmRank=map.get("ctmRank");
		String salesProgress=map.get("salesProgress");
		String adviser=map.get("adviser");
		String dealerId=map.get("dealerId");
		String opType=map.get("opType");
		String logonId=map.get("logonId");
		String poseRank=map.get("poseRank");
		String groupId=map.get("groupId");
		StringBuilder sql= new StringBuilder();
		
		sql.append("select * from (select 60161002 LEADS_STATUS,tpd.defeatfailure_id,\n" );
		sql.append("      tpc.create_date,\n" );
		sql.append("       tpc.customer_id,\n" );
		sql.append("       tpd.defeatfailure_type,\n" );
		sql.append("       tpc.customer_name,\n" );
		sql.append("       tpc.telephone,\n" );
		sql.append("       tpiv.series_name intent_vehicle,\n" );
		sql.append("       tu.name adviser,\n" );
		sql.append("       null life_cycle,\n" );
		sql.append("      TO_CHAR( tpd.defeat_END_date,'YYYY-mm-dd') defeat_END_date ,\n" );
		sql.append("       null,\n" );
		sql.append("       tc.code_desc ctm_rank,\n" );
		sql.append("      tpd.audit_status,\n" );
		sql.append("   "+poseRank+" pose_rank,\n");
		sql.append("       null  FAILURE_DATE,\n" );
		sql.append("         tpdv.series_name defeat_model,\n");
		sql.append("       tc1.code_desc sales_progress,\n" );
		sql.append("       tc2.code_desc status, 10011001 IS_DIRECT_DEFEAT\n" );
		sql.append("  from t_pc_customer tpc, t_pc_defeatfailure tpd,tc_user tu,tc_code tc,tc_code tc1,tc_code tc2,t_pc_intent_vehicle tpiv,t_pc_defeat_vehicle tpdv\n" );
		sql.append(" where tpc.customer_id = tpd.customer_id(+)\n" );
		sql.append(" and tu.user_id(+)=tpc.adviser\n" );
		sql.append(" and tc.code_id(+)=tpc.ctm_rank\n" );
		sql.append(" and tc1.code_id(+)=tpc.sales_progress\n" );
		sql.append(" and tpiv.series_id(+)=tpc.intent_vehicle\n");
		sql.append(" and tpd.AUDIT_STATUS=tc2.code_id(+)\n");
		sql.append(" and tpdv.series_id(+)=tpd.defeat_model");
		sql.append(" and tpd.defeatfailure_type="+Constant.FAILURE_TYPE_01 );
		sql.append("  and tpd.status="+Constant.STATUS_ENABLE);
//		sql.append("  and (tpd.audit_status ="+Constant.FAILURE_AUDIT_01);
//		sql.append("  or tpd.audit_status ="+Constant.FAILURE_AUDIT_05+")");
		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone like '%"+telephone+"%'\n");
		}
		if(null!=opType&&!"".endsWith(opType)){
			sql.append(" and tpd.defeatfailure_type="+opType);
		}
		if(null!=defeatDate&&!"".equals(defeatDate)){
			sql.append("                           AND TPd.defeat_END_date >=\n" );
			sql.append("                               TO_DATE('"+defeatDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			
		}
		if(null!=defeatDateEnd&&!"".equals(defeatDateEnd)){
			sql.append("                           AND Tpd.defeat_END_date <=\n" );
			sql.append("                               TO_DATE('"+defeatDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=ctmRank&&!"".equals(ctmRank)){
			sql.append(" and tpc.ctm_rank = '"+ctmRank+"'\n");
		}
		if(null!=salesProgress&&!"".equals(salesProgress)){
			sql.append(" and tpc.sales_Progress = '"+salesProgress+"'\n");
		}
		if(null!=adviser&&!"".equals(adviser)){
			sql.append(" and tu.user_id = '"+adviser+"'\n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
			sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
		}
	/*//	sql.append(" order by tpc.create_date desc ");
		sql.append("  union all\n" );
		sql.append("select tpd.defeatfailure_id,\n" );
		sql.append("      tpc.create_date,\n" );
		sql.append("      tpc.customer_id,\n" );
		sql.append("      tpd.defeatfailure_type,\n" );
		sql.append("      tpc.customer_name,\n" );
		sql.append("      tpc.telephone,\n" );
		sql.append("      tpiv.series_name intent_vehicle,\n" );
		sql.append("      tu.name adviser ,\n" );
		sql.append("      null life_cycle,\n" );
		sql.append("      null, \n" );
		sql.append("      tpd.create_date,\n" );
		sql.append("      tc.code_desc ctm_rank,\n" );
		sql.append("      tpd.audit_status,\n" );
		sql.append("     "+poseRank+"pose_rank,\n");
		sql.append("       TO_CHAR(tpd.FAILURE_DATE,'YYYY-MM-DD') FAILURE_DATE ,\n" );
		sql.append("      null, \n" );
		sql.append("      tc1.code_desc sales_progress,\n" );
		sql.append("       tc2.code_desc STATUS,10011001 IS_DIRECT_DEFEAT \n" );
		sql.append(" from t_pc_customer tpc, t_pc_defeatfailure tpd,tc_user tu,tc_code tc,tc_code tc1,tc_code tc2,t_pc_intent_vehicle tpiv\n" );
		sql.append("where tpc.customer_id = tpd.customer_id(+)\n" );
		sql.append("and tu.user_id(+)=tpc.adviser\n" );
		sql.append("and tc.code_id(+)=tpc.ctm_rank\n" );
		sql.append("and tc1.code_id(+)=tpc.sales_progress\n" );
		sql.append(" and tpiv.series_id(+)=tpc.intent_vehicle\n");
		sql.append(" and tpd.AUDIT_STATUS=tc2.code_id(+)\n");
		sql.append(" and tpd.status="+Constant.STATUS_ENABLE);
		sql.append(" and tpd.defeatfailure_type= "+Constant.FAILURE_TYPE_02);
//		sql.append("  and (tpd.audit_status ="+Constant.FAILURE_AUDIT_01);
//		sql.append("  or tpd.audit_status ="+Constant.FAILURE_AUDIT_05+")");
		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone like '%"+telephone+"%'\n");
		}
		if(null!=defeatDate&&!"".equals(defeatDate)){
			sql.append("                           AND TPd.FAILURE_DATE >=\n" );
			sql.append("                               TO_DATE('"+defeatDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=defeatDateEnd&&!"".equals(defeatDateEnd)){
			sql.append("                           AND Tpd.FAILURE_DATE <=\n" );
			sql.append("                               TO_DATE('"+defeatDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=opType&&!"".endsWith(opType)){
			sql.append(" and tpd.defeatfailure_type="+opType);
		}
		if(null!=ctmRank&&!"".equals(ctmRank)){
			sql.append(" and tpc.ctm_rank = '"+ctmRank+"'\n");
		}
		if(null!=salesProgress&&!"".equals(salesProgress)){
			sql.append(" and tpc.sales_Progress = '"+salesProgress+"'\n");
		}
		if(null!=adviser&&!"".equals(adviser)){
			sql.append(" and tu.user_id = '"+adviser+"'\n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
			sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
		}*/
		//当销售流程进度条件没哟选择时，才查询这条sql
		if (CommonUtils.isEmpty(salesProgress)) {
			sql.append(" union all \n");
			sql.append(" SELECT tl.LEADS_STATUS,TL.LEADS_CODE DEFEATFAILURE_ID,TL.CREATE_DATE,NULL CUSTOMER_ID,60391001 DEFEATFAILURE_TYPE, \n");
			sql.append(" TL.CUSTOMER_NAME,TL.TELEPHONE,TPIV.SERIES_NAME INTENT_VEHICLE,TU.NAME ADVISER,NULL LIFE_CYCLE, \n");
			sql.append(" TO_CHAR(tl.update_date,'YYYY-mm-dd') DEFEAT_END_DATE , \n");
			sql.append(" NULL,NULL, tpd.AUDIT_STATUS,"+poseRank+" POSE_RANK,NULL,TPIV2.SERIES_NAME DEFEAT_MODEL,NULL,tc2.CODE_DESC STATUS,tl.IS_DIRECT_DEFEAT \n");
			sql.append(" from t_pc_leads tl,t_pc_leads_allot tla,T_PC_DEFEATFAILURE tpd,t_pc_intent_vehicle tpiv,tc_user tu,T_PC_DEFEAT_VEHICLE tpiv2,tc_code tc2 \n");
			sql.append(" where tl.leads_code=tla.leads_code and tl.leads_code=tpd.leads_code  and tl.intent_vehicle=tpiv.series_id(+) \n");
			sql.append(" and tla.adviser=tu.user_id(+) and tpd.defeat_model=tpiv2.series_id(+)  and tpd.audit_status=tc2.code_id(+)  \n");
			if(null!=ctmName&&!"".equals(ctmName)){
				sql.append(" and tl.customer_name like '%"+ctmName+"%'\n");
			}
			if(null!=telephone&&!"".equals(telephone)){
				sql.append(" and tl.telephone like '%"+telephone+"%'\n");
			}
			if(null!=defeatDate&&!"".equals(defeatDate)){
				sql.append("                           AND tl.update_date >=\n" );
				sql.append("                               TO_DATE('"+defeatDate+"' || '00:00:00',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			}
			if(null!=defeatDateEnd&&!"".equals(defeatDateEnd)){
				sql.append("                           AND tl.update_date <=\n" );
				sql.append("                               TO_DATE('"+defeatDateEnd+"' || '23:59:59',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			}
			if(null!=ctmRank&&!"".equals(ctmRank)){
				sql.append(" and tl.intent_type = '"+ctmRank+"'\n");
			}
			if(null!=adviser&&!"".equals(adviser)){
				sql.append(" and tu.user_id = '"+adviser+"'\n");
			}
			if(Utility.testString(groupId)){
				sql.append(" and tu.group_id = '"+groupId+"' \n");
			}
			if(null!=dealerId&&!"".equals(dealerId)){
				sql.append(" and tla.dealer_id="+dealerId);
				sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
				sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
			}
		}
		sql.append(" ) ts order by ts.audit_status,ts.defeat_end_date desc ");
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * FUNCTION		:	查询所有待审核的战败记录
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getDcrcQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String ctmName=map.get("ctmName");
		String telephone=map.get("telephone");
		String defeatDate=map.get("defeatDate");
		String defeatDateEnd=map.get("defeatDateEnd");
		String ctmRank=map.get("ctmRank");
		String salesProgress=map.get("salesProgress");
		String adviser=map.get("adviser");
		String logonId=map.get("logonId");
		String dealerId=map.get("dealerId");
		String groupId=map.get("groupId");
		String opType=map.get("opType");
		StringBuilder sql= new StringBuilder();
	
		sql.append("select * from (select tpd.defeatfailure_id,\n" );
		sql.append("       tpc.customer_id,\n" );
		sql.append("       tpd.defeatfailure_type,\n" );
		sql.append("       tpc.customer_name,\n" );
		sql.append("       tpc.telephone,\n" );
		sql.append("       tpiv.series_name intent_vehicle,\n" );
		sql.append("       tu.name adviser,\n" );
		sql.append("       null life_cycle,\n" );
		sql.append("      TO_CHAR( tpd.defeat_END_date,'YYYY-mm-dd') defeat_END_date ,\n" );
		sql.append("       null,\n" );
		sql.append("       tc.code_desc ctm_rank,\n" );
		sql.append("      tpd.audit_status,\n" );
		sql.append("       null  FAILURE_DATE,\n" );
		sql.append("         tpdv.series_name defeat_model,\n");
		sql.append("       tc1.code_desc sales_progress\n" );
		sql.append("  from t_pc_customer tpc, t_pc_defeatfailure tpd,tc_user tu,tc_code tc,tc_code tc1,t_pc_intent_vehicle tpiv,t_pc_defeat_vehicle tpdv\n" );
		sql.append(" where tpc.customer_id = tpd.customer_id(+)\n" );
		sql.append(" and tu.user_id(+)=tpc.adviser\n" );
		sql.append(" and tc.code_id(+)=tpc.ctm_rank\n" );
		sql.append(" and tc1.code_id(+)=tpc.sales_progress\n" );
		sql.append(" and tpiv.series_id(+)=tpc.intent_vehicle");
		sql.append(" and tpdv.series_id(+)=tpd.defeat_model");
		sql.append(" and tpd.defeatfailure_type="+Constant.FAILURE_TYPE_01 );
		sql.append("  and tpd.status="+Constant.STATUS_ENABLE);
		sql.append("  and tpd.audit_status ="+Constant.FAILURE_AUDIT_02);
//		sql.append("  or tpd.audit_status ="+Constant.FAILURE_AUDIT_05+")");
		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone like '%"+telephone+"%'\n");
		}
		if(null!=dealerId&&!"".endsWith(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
		}
		if(null!=opType&&!"".endsWith(opType)){
			sql.append(" and tpd.defeatfailure_type="+opType);
		}
		if(null!=defeatDate&&!"".equals(defeatDate)){
			sql.append("                           AND TPd.defeat_END_date >=\n" );
			sql.append("                               TO_DATE('"+defeatDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			
		}
		if(null!=defeatDateEnd&&!"".equals(defeatDateEnd)){
			sql.append("                           AND Tpd.defeat_END_date <=\n" );
			sql.append("                               TO_DATE('"+defeatDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=ctmRank&&!"".equals(ctmRank)){
			sql.append(" and tpc.ctm_rank = '"+ctmRank+"'\n");
		}
		if(null!=salesProgress&&!"".equals(salesProgress)){
			sql.append(" and tpc.sales_Progress = '"+salesProgress+"'\n");
		}
		if(null!=adviser&&!"".equals(adviser)){
			sql.append(" and tu.user_id = '"+adviser+"'\n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
//			sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
//			sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
		}
	//	sql.append(" order by tpc.create_date desc ");
		sql.append("  union\n" );
		sql.append("select tpd.defeatfailure_id,\n" );
		sql.append("      tpc.customer_id,\n" );
		sql.append("      tpd.defeatfailure_type,\n" );
		sql.append("      tpc.customer_name,\n" );
		sql.append("      tpc.telephone,\n" );
		sql.append("      tpiv.series_name intent_vehicle,\n" );
		sql.append("      tu.name adviser ,\n" );
		sql.append("      null life_cycle,\n" );
		sql.append("      null, \n" );
		sql.append("      tpd.create_date,\n" );
		sql.append("      tc.code_desc ctm_rank,\n" );
		sql.append("      tpd.audit_status,\n" );
		sql.append("       TO_CHAR(tpd.FAILURE_DATE,'YYYY-MM-DD') FAILURE_DATE ,\n" );
		sql.append("      null, \n" );
		sql.append("      tc1.code_desc sales_progress\n" );
		sql.append(" from t_pc_customer tpc, t_pc_defeatfailure tpd,tc_user tu,tc_code tc,tc_code tc1,t_pc_intent_vehicle tpiv\n" );
		sql.append("where tpc.customer_id = tpd.customer_id(+)\n" );
		sql.append("and tu.user_id(+)=tpc.adviser\n" );
		sql.append("and tc.code_id(+)=tpc.ctm_rank\n" );
		sql.append("and tc1.code_id(+)=tpc.sales_progress\n" );
		sql.append(" and tpiv.series_id(+)=tpc.intent_vehicle");
		sql.append(" and tpd.status="+Constant.STATUS_ENABLE);
		sql.append(" and tpd.defeatfailure_type= "+Constant.FAILURE_TYPE_02);
		sql.append("  and tpd.audit_status ="+Constant.FAILURE_AUDIT_02);
//		sql.append("  or tpd.audit_status ="+Constant.FAILURE_AUDIT_05+")");
		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone like '%"+telephone+"%'\n");
		}
		if(null!=defeatDate&&!"".equals(defeatDate)){
			sql.append("                           AND TPd.FAILURE_DATE >=\n" );
			sql.append("                               TO_DATE('"+defeatDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=defeatDateEnd&&!"".equals(defeatDateEnd)){
			sql.append("                           AND Tpd.FAILURE_DATE <=\n" );
			sql.append("                               TO_DATE('"+defeatDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=opType&&!"".endsWith(opType)){
			sql.append(" and tpd.defeatfailure_type="+opType);
		}
		if(null!=dealerId&&!"".endsWith(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
		}
		if(null!=ctmRank&&!"".equals(ctmRank)){
			sql.append(" and tpc.ctm_rank = '"+ctmRank+"'\n");
		}
		if(null!=salesProgress&&!"".equals(salesProgress)){
			sql.append(" and tpc.sales_Progress = '"+salesProgress+"'\n");
		}
		if(null!=adviser&&!"".equals(adviser)){
			sql.append(" and tu.user_id = '"+adviser+"'\n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
//			sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
//			sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
		}
		sql.append(")ts order by ts.defeatfailure_id desc  ");
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 获取审核的意见
	 * @param map
	 * @return
	 */
	public String queryAudit(Map<String, String> map) {
	    String defeatId=map.get("defeatId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tdfa.audit_remark\n" );
		sql.append("  from t_pc_defeatfailure_audit tdfa\n" );
		sql.append(" where 1= 1\n" );
		sql.append("   and tdfa.defeatfailure_id = "+defeatId );
		sql.append(" order by tdfa.create_date desc");
		String remark="";
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		if(list.size()>0){
			if(list.get(0).get("AUDIT_REMARK")!=null){
				remark=list.get(0).get("AUDIT_REMARK").toString();
			}
			
		}
	    
		return remark;
	}
	/**
	 * 获取审核的历史记录
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAuditList(Map<String, String> map) {
		String defeatId=map.get("defeatId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tc.code_desc audit_type,\n" );
		sql.append("       tu.name,\n" );
		sql.append("       tc1.code_desc audit_result,\n" );
		sql.append("       t.audit_remark,\n" );
		sql.append("       to_char(t.create_date, 'yyyy-mm-dd') audit_time\n" );
		sql.append("  from t_pc_defeatfailure_audit t, tc_code tc, tc_user tu, tc_code tc1\n" );
		sql.append(" where tc.code_id(+) = t.audit_type\n" );
		sql.append("   and tu.user_id = t.create_by\n" );
		sql.append("   and tc1.code_id = t.after_audit\n" );
		sql.append("  and t.defeatfailure_id="+defeatId);
		sql.append("   order by t.create_date desc");

		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	
}
