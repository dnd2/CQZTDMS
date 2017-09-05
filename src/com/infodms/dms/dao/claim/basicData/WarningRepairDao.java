package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWarningRepairPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class WarningRepairDao extends BaseDao {
	public static Logger logger = Logger.getLogger(WarningRepairDao.class);
	private static final WarningRepairDao dao = new WarningRepairDao ();
	public static final WarningRepairDao getInstance() {
		return dao;
	}
	/* (非 Javadoc) 
	 * <p>Title: wrapperPO</p> 
	 * <p>Description: </p> 
	 * @param rs
	 * @param idx
	 * @return 
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int) 
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成方法存根
		return null;
	}
	
	public PageResult<Map<String, Object>> WarningRepairQuery(int pageSize, int curPage, String whereSql) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select a.WARNING_REPAIR_ID,a.WARNING_CODE,a.WAINING_REMARK,a.WAINING_LEVEL,a.WARNING_TYPE,a.is_Accumulative,a.WARNING_NUM_START,a.WARNING_NUM_END,a.VALID_DATE,a.VALID_MILEAGE,a.CLAUSE_STATUTE,a.status from TT_AS_WARNING_REPAIR a where 1=1 ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by a.warning_repair_id,a.warning_type,a.waining_level  desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	public List<Map<String, Object>> getAssemblyDetail(){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from TT_AS_WR_Parts_assembly a where a.status=10011001\n");
		System.out.println(":sql"+sql);
		List<Map<String, Object>> listDetail = pageQuery(sql.toString(),null,getFunName());
		return listDetail;
	}
	
	public List<Map<String, Object>> getFaultDetail(){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from TT_AS_WR_Fault_type a where a.status=10011001\n");
		System.out.println(":sql"+sql);
		List<Map<String, Object>> listDetail = pageQuery(sql.toString(),null,getFunName());
		return listDetail;
	}
	
	public List getLevelList(Long oemCompanyId,String type){
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawa.approval_level_code,tawa.approval_level_name,tawa.approval_level_tier from tt_as_wr_authinfo tawa  ");
		sb.append(" where tawa.approval_level_code<>100 ");
		sb.append(" and tawa.oem_company_id = "+oemCompanyId+" \n");
		sb.append(" and tawa.type = ").append(type).append("\n");
		sb.append(" order by tawa.approval_level_code ");
		List<Map<String, Object>> list = pageQuery(sb.toString(), null,getFunName());
		return list;
	}
	
	//检查故障法定名称是否存在
	public List isExistRepair(String warningCode){
		List list = null;
		TtAsWarningRepairPO po = new TtAsWarningRepairPO();
		po.setWarningCode(warningCode);
		list = dao.select(po);
		return list;
	}
	
	public HashMap warningRepairQueryById(String id,Long oemCompanyId) throws Exception {
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		HashMap map = new HashMap();
		StringBuffer sb = new StringBuffer();
		sb.append(" select a.WARNING_REPAIR_ID,a.WARNING_TYPE,b.id,b.change_name,a.WAINING_LEVEL,a.IS_ACCUMULATIVE,a.WARNING_CODE,a.WAINING_REMARK, ");
		sb.append(" a.WARNING_NUM_START,a.WARNING_NUM_END,a.VALID_DATE,a.VALID_MILEAGE,A.VALID_START_DATE,A.VALID_START_MILEAGE,a.CLAUSE_STATUTE,a.status ");
		sb.append("  ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(a.PPROVAL_LEVER_CODE,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end \""+tcpo.get("APPROVAL_LEVEL_CODE")+"\"");
		}		
		sb.append(" from TT_AS_WARNING_REPAIR a, TT_AS_WARNING_REPAIR_detail b ");
		sb.append(" where a.warning_repair_id= b.warning_repair_id(+) ");
		sb.append(" and a.warning_repair_id="+id+" ");
		List<Map<String, Object>> relist = pageQuery(sb.toString(), null,getFunName());
		if(relist != null && relist.size() > 0){
			map = (HashMap)relist.get(0);
		}
		return map;
	}
}
