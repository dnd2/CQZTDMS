package com.infodms.dms.dao.crm.basedata;

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
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class PcVechileDao extends BaseDao<PO>{
	
	private static final PcVechileDao dao = new PcVechileDao();
	
	public static final PcVechileDao getInstance() {
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
	public PageResult<Map<String, Object>> getVechileQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String competName=map.get("competName");
		String status=map.get("status");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpv.compet_id, tpv.compet_code, tpv.compet_name, tpv.status,tpv.par_id,\n" );
		sql.append("  TPV.COMPET_LEVEL from t_pc_compet_vechile tpv\n" );
		sql.append(" where 1=1\n" );
		
		if(null!=competName&&!"".equals(competName)){
			sql.append(" and tpv.compet_name like '%"+competName+"%'\n");
		}
		if(null!=status&&!"".equals(status)){
			sql.append(" and tpv.status ="+status);
		}
		sql.append(" order by tpv.par_id");
		
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> getCompetVechileList(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		String competLevel=map.get("competLevel");
		String competCode=map.get("competCode");
		String competName=map.get("competName");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpv.compet_id, tpv.compet_code, tpv.compet_name, tpv.status,tpv.par_id\n" );
		sql.append("  from t_pc_compet_vechile tpv\n" );
		sql.append(" where tpv.compet_level ="+competLevel+"\n" );
		sql.append("  and tpv.status=10011001 \n" );
		if(competCode!=null&&!"".equals(competCode)){
			sql.append("  and tpv.compet_code like '%"+competCode+"%'\n" );
		}
		if(competName!=null&&!"".equals(competName)){
			sql.append("  and tpv.compet_name like '%"+competName+"%'\n" );
		}
		sql.append(" order by tpv.par_id");

		PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return ps ;
	}


	
	
	
	
}
