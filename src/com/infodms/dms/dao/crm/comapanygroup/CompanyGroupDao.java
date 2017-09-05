package com.infodms.dms.dao.crm.comapanygroup;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
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
 * @author yinshunhui
 * @version 1.0
 * @remark
 */
public class CompanyGroupDao extends BaseDao<PO>{
	
	private static final CompanyGroupDao dao = new CompanyGroupDao();
	
	public static final CompanyGroupDao getInstance() {
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
	public PageResult<Map<String, Object>> getGroupQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String groupName=map.get("groupName");
		String status=map.get("status");
		String dealerId=map.get("dealerId");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT distinct TPG.group_id,\n" );
		sql.append("       TPG.group_name,\n" );
		sql.append("  --     TPG.company_group_id,\n" );
		sql.append("       TPG.status,\n" );
		sql.append("       TPG.create_date,\n" );
		sql.append("       TPG.create_by,\n" );
		sql.append("       TD.dealer_id,\n" );
		sql.append("       TD.dealer_code,\n" );
		sql.append("       TD.dealer_name\n" );
		sql.append("  FROM t_pc_company_group TPG, tm_dealer TD\n" );
		sql.append(" WHERE TPG.par_dealer_id = TD.dealer_id \n");
	//	sql.append("  AND TPG.STATUS="+Constant.STATUS_ENABLE );
		if(null!=groupName&&!"".equals(groupName)){
			sql.append(" and tpg.group_name like '%"+groupName+"%'\n");
		}
		if(null!=status&&!"".equals(status)){
			sql.append(" and tpg.status ="+status);
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpg.dealer_id ="+dealerId);
		}
		sql.append("   order by tpg.create_date desc");
		
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	//获取该名字的数目
	public int checkGroupName(Map<String, String> map) {
		//String dealerId=map.get("dealerId");
		String groupName=map.get("groupName");
		String groupId=map.get("groupId");
		int counts=0;
		StringBuilder sql= new StringBuilder();
		sql.append("select count(1) counts\n" );
		sql.append("  from t_pc_COMPANY_GROUP tpc\n" );
		sql.append(" where 1=1 and tpc.group_name = '"+groupName+"'");
		if(groupId!=null&&!"".equals(groupId)){
			sql.append(" and tpc.company_group_id!="+groupId);
		}
		sql.append(" and tpc.status=10011001");
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		String count=list.get(0).get("COUNTS").toString();
		counts=Integer.parseInt(count);
		return counts;
	}
}
