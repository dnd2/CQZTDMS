package com.infodms.dms.dao.crm.revisit;

import java.sql.ResultSet;
import java.util.List;
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
public class ReVisitDao extends BaseDao<PO>{
	
	private static final ReVisitDao dao = new ReVisitDao();
	
	public static final ReVisitDao getInstance() {
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
	public PageResult<Map<String, Object>> getRevisitQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String ctmName=map.get("ctmName");
		String telephone=map.get("telephone");
		String startTime=map.get("startTime");
		String endTime=map.get("endTime");
		String adviser=map.get("adviser");
		String revisitType=map.get("revisitType");
		String dealerId=map.get("dealerId");
		String logonId=map.get("logonId");
		String groupId=map.get("groupId");
		StringBuilder sql= new StringBuilder();
		
		sql.append("select tpr.revisit_id,\n" );
		sql.append("       tpc.customer_id,\n" );
		sql.append("       tpc.customer_name,\n" );
		sql.append("       tpc.telephone,\n" );
		sql.append("      tpr.revist_type_name  revisit_type,\n" );
		sql.append("       to_char(tpr.revisit_date,'yyyy-mm-dd') revisit_date,\n" );
		sql.append("       to_char(tpr.buy_date,'yyyy-mm-dd')buy_date ,\n" );
		sql.append("       tvmg3.group_name,\n" );
		sql.append("       td.dealer_shortname,\n" );
		sql.append("       tu.name,\n" );
		sql.append("       tpr.task_status\n" );
		sql.append("  from t_pc_revisit             tpr,\n" );
		sql.append("       t_pc_customer            tpc,\n" );
		sql.append("       tm_vehicle               tv,\n" );
		sql.append("       tm_vhcl_material         tvm,\n" );
		sql.append("       tm_vhcl_material_group   tvmg1,\n" );
		sql.append("       tm_vhcl_material_group   tvmg2,\n" );
		sql.append("       tm_vhcl_material_group   tvmg3,\n" );
		sql.append("       tm_vhcl_material_group_r tvmgr,\n" );
		sql.append("       tc_user tu,\n" );
		sql.append("       tm_dealer td ,\n" );
		sql.append("       tc_code tc\n" );
		sql.append(" where tpr.customer_id = tpc.customer_id\n" );
		sql.append("   and tv.vehicle_id = tpr.vin_id\n" );
		sql.append("   and tv.material_id=tvm.material_id\n" );
		sql.append("   and tvm.material_id=tvmgr.material_id\n" );
		sql.append("   and tvmgr.group_id=tvmg1.group_id\n" );
		sql.append("   and tvmg1.parent_group_id=tvmg2.group_id\n" );
		sql.append("   and tvmg3.group_id=tvmg2.parent_group_id\n" );
		sql.append("   and tpr.revisit_adviser=tu.user_id\n" );
		sql.append("   and td.dealer_id=tpr.revisit_dealer\n" );
		sql.append("   and tc.code_id=tpr.revisit_type");
		sql.append("   and tpr.task_status<>'60171003'  ");
		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone  like '%"+telephone+"%'");
		}
		if(null!=startTime&&!"".equals(startTime)){
			sql.append("                           AND TPr.revisit_date >=\n" );
			sql.append("                               TO_DATE('"+startTime+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			
		}
		if(null!=endTime&&!"".equals(endTime)){
			sql.append("                           AND Tpr.revisit_date <=\n" );
			sql.append("                               TO_DATE('"+endTime+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=adviser&&!"".equals(adviser)){
			sql.append(" and tu.user_id ="+adviser);
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=revisitType&&!"".equals(revisitType)){
			sql.append(" and tpr.revisit_type ="+revisitType);
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			if(logonId!=null&&!"".equals(logonId)){
				sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
				sql.append("  CONNECT BY nocycle PRIOR tcu.user_id = tcu.par_user_iD)");
			}
		}
		
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 获取数据列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryDataList(Map<String, String> map) {
		String revisitId=map.get("revisitId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpr.revisit_id,\n" );
		sql.append("       tpc.customer_id,\n" );
		sql.append("       td.dealer_shortname,\n" );
		sql.append("       tu.name adviser,\n" );
		sql.append("       to_char(tpr.revisit_date, 'yyyy-mm-dd') revisit_date,\n" );
		sql.append("       tpr.revisit_date,\n" );
		sql.append("       tpr.revist_type_name  revisit_type,\n" );
		sql.append("      tpr.revisit_type r_type,\n" );
		sql.append("       tpc.customer_name,\n" );
		sql.append("       tpc.telephone,\n" );
		sql.append("       tpc.provice_id,\n" );
		sql.append("       tpc.city_id,\n" );
		sql.append("       tpc.town_id,\n" );
		sql.append("       to_char(tpr.buy_date, 'yyyy-mm-dd') buy_date,\n" );
		sql.append("       tpc.address,\n" );
		sql.append("       tv.vin,\n" );
		sql.append("       tvmg3.group_name seriesName,\n" );
		sql.append("       tvmg1.group_code modelCode,\n" );
		sql.append("       tvmg1.group_name modelName,\n" );
		sql.append("       tv.engine_no,\n" );
		sql.append("       tpr.prologue,\n" );
		sql.append("       tpr.conclusions,\n" );
		sql.append("      tc1.code_desc if_revisit,\n" );
		sql.append("       tc2.code_desc fail_reason,\n" );
		sql.append("        tc3.code_desc evaluate,\n" );
		sql.append("       tpr.tracks,\n" );
		sql.append("       tpr.tracks_result,\n" );
		sql.append("       tc4.code_desc tracks_finish\n" );
		sql.append("  from t_pc_revisit             tpr,\n" );
		sql.append("       t_pc_customer            tpc,\n" );
		sql.append("       tm_vehicle               tv,\n" );
		sql.append("       tm_vhcl_material         tvm,\n" );
		sql.append("       tm_vhcl_material_group   tvmg1,\n" );
		sql.append("       tm_vhcl_material_group   tvmg2,\n" );
		sql.append("       tm_vhcl_material_group   tvmg3,\n" );
		sql.append("       tm_vhcl_material_group_r tvmgr,\n" );
		sql.append("       tc_user                  tu,\n" );
		sql.append("       tm_dealer                td,\n" );
		sql.append("       tc_code                  tc,\n" );
		sql.append("       tc_code                  tc1,\n" );
		sql.append("       tc_code                  tc2,\n" );
		sql.append("       tc_code                   tc3,\n" );
		sql.append("       tc_code                     tc4\n" );
		sql.append(" where tpr.customer_id = tpc.customer_id\n" );
		sql.append("   and tv.vehicle_id = tpr.vin_id\n" );
		sql.append("   and tv.material_id = tvm.material_id\n" );
		sql.append("   and tvm.material_id = tvmgr.material_id\n" );
		sql.append("   and tvmgr.group_id = tvmg1.group_id\n" );
		sql.append("   and tvmg1.parent_group_id = tvmg2.group_id\n" );
		sql.append("   and tvmg3.group_id = tvmg2.parent_group_id\n" );
		sql.append("   and tpr.revisit_adviser = tu.user_id\n" );
		sql.append("   and td.dealer_id = tpr.revisit_dealer\n" );
		sql.append("   and tc.code_id (+)= tpr.revisit_type\n" );
		sql.append("   and tc1.code_id(+)=tpr.if_revisit\n" );
		sql.append("   and tc2.code_id(+)=tpr.fail_reason\n" );
		sql.append("   and tc3.code_id(+)=tpr.evaluate\n" );
		sql.append("   and tc4.code_id(+)=tpr.tracks_finish");
		sql.append("   and tpr.revisit_id="+revisitId);
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	//明细回访记录
	public List<Map<String, Object>> queryDetailList(Map<String, String> map) {
		String revisitId=map.get("revisitId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tprd.revisit_id,tprd.detail_id,\n" );
		sql.append(" tc.code_desc COMMENTS,tprd.CLIENT_TIP\n" );
		sql.append("  from t_pc_revist_detail tprd,tc_code tc\n" );
		sql.append("   where tprd.revisit_id="+revisitId );
		sql.append("   and tc.code_id=tprd.comments");
		sql.append("  order by tprd.detail_id asc ");
	    return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}


	
	
	
	
}
