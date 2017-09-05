package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmBusinessChngCodePO;
import com.infodms.dms.po.TmPtFailureModePO;
import com.infodms.dms.po.TmPtPartBaseExtPO;
import com.infodms.dms.po.TtAsWrFaultLegalPO;
import com.infodms.dms.po.TtAsWrFaultModeDetailPO;
import com.infodms.dms.po.TtAsWrFaultPartsPO;
import com.infodms.dms.po.TtAsWrFaultPartsTempPO;
import com.infodms.dms.po.TtAsWrFaultTypePO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrRuleListTmpPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class FailureMaintainDao extends BaseDao {
	public static Logger logger = Logger.getLogger(FailureMaintainDao.class);
	private static final FailureMaintainDao dao = new FailureMaintainDao ();
	public static final FailureMaintainDao getInstance() {
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
	
	//分页查询
	public PageResult<Map<String, Object>> failureMaintainQuery(int pageSize, int curPage, String whereSql) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from TT_AS_WR_Fault_type a where 1=1 ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by a.fault_type_id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	//失效模式查询
	public PageResult<Map<String, Object>> failureModeQuery(String modeCode,String modeName,int pageSize, int curPage){
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select *\n");
		sql.append("  from tm_pt_failure_mode t where 1=1 \n");
		if(modeCode!=null && !modeCode.equals("")){
			sql.append(" and t.Failure_Code like '%"+modeCode+"%'\n");
		}
		if(modeName!=null && !modeName.equals("")){
			sql.append("   and t.Failure_Name like '%"+modeName+"%'"); 
		}
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	//判断新增代码是否在数据库中存在
	public List isExist(String failureCode){
		List list = null;
		TtAsWrFaultTypePO po = new TtAsWrFaultTypePO();
		po.setFaultTypeCode(failureCode);
		list = dao.select(po);
		return list;
	}
	
	public PageResult<TtAsWrFaultTypePO> queryFailureChange(String failureCode,String failureName, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.fault_type_id,a.fault_type_code,a.fault_type_name FROM TT_AS_WR_Fault_type A WHERE a.status=10011001");

		if (Utility.testString(failureCode)) {
			sql.append(" AND A.fault_type_CODE LIKE '%" + failureCode+ "%' ");
		}
		if (Utility.testString(failureName)) {
			sql.append(" AND A.fault_type_name LIKE '%" + failureName+ "%' ");
		}
		PageResult<TtAsWrFaultTypePO> ps = pageQuery(TtAsWrFaultTypePO.class,sql.toString(), null, pageSize, curPage);
		return ps;
	}
	
	public Map<String,Object> queryNameByCode(AclUserBean user,String faultCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.fault_type_id,a.fault_type_code,a.fault_type_name\n");
		sql.append("  FROM TT_AS_WR_Fault_type A\n");
		sql.append(" WHERE 1=1\n");
		if(Utility.testString(faultCode)){
			sql.append(" AND a.fault_type_code LIKE '%"+faultCode+"%'\n");
		}
		
		Map<String,Object> ps = this.pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	//检查故障法定名称是否存在
	public List isExistLegal(String legalCode){
		List list = null;
		TtAsWrFaultLegalPO po = new TtAsWrFaultLegalPO();
		po.setFaultCode(legalCode);
		list = dao.select(po);
		return list;
	}
	
	//分页查询
	public PageResult<Map<String, Object>> faultLegalQuery(int pageSize, int curPage, String whereSql,String failureName) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select a.fault_id,a.fault_code,a.fault_name,b.fault_type_name,a.status from TT_AS_WR_Fault_legal a,TT_AS_WR_Fault_type b where a.fault_type_id=b.fault_type_id and a.status=10011001 ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		if (Utility.testString(failureName)) {
			sb.append(" AND b.fault_type_name LIKE '%" + failureName+ "%' ");
		}
		sb.append(" order by a.fault_type_id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	public PageResult<TtAsWrFaultPartsPO> queryFaultPart(String faultId,String partCode,String partName, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.id,a.part_code,a.part_name FROM Tt_As_Wr_Fault_Parts A WHERE A.fault_id = "+faultId+" ");

		if (Utility.testString(partCode)) {
			sql.append(" AND A.part_code LIKE '%" + partCode+ "%' ");
		}
		if (Utility.testString(partName)) {
			sql.append(" AND A.part_name LIKE '%" + partName+ "%' ");
		}
		sql.append(" AND A.Status = ").append(Constant.STATUS_ENABLE);
		PageResult<TtAsWrFaultPartsPO> ps = pageQuery(TtAsWrFaultPartsPO.class,sql.toString(), null, pageSize, curPage);
		return ps;
	}
	
	public void claimRulePartsImportAdd(TtAsWrFaultPartsTempPO RuleListPO){
		dao.insert(RuleListPO);              
	}
	public void claimRulePartsImportAddMerge(TtAsWrFaultPartsTempPO RuleListPO){
		StringBuffer sql= new StringBuffer();
		sql.append("MERGE INTO tt_as_wr_fault_parts r\n" );
		sql.append("USING\n" );
		sql.append("  (select fault_id,part_code, part_name, status, is_de\n" );
		sql.append("    from tt_as_wr_fault_parts_temp where fault_id="+RuleListPO.getFaultId()+") l ON ( r.fault_id=l.fault_id and r.part_code = l.part_code and\n" );
		sql.append("                                 r.part_name = l.part_name) WHEN\n" );
		sql.append("   MATCHED THEN\n" );
		sql.append("    UPDATE\n" );
		sql.append("       SET \n" );
		sql.append("           \n" );
		sql.append("           r.update_by    = "+RuleListPO.getUpdateBy()+",\n" );
		sql.append("           r.update_date  = sysdate\n" );
		sql.append("  WHEN NOT MATCHED THEN\n" );
		sql.append("    INSERT\n" );
		sql.append("      (r.id,\n" );
		sql.append("       r.fault_id,\n" );
		sql.append("       r.part_code,\n" );
		sql.append("       r.part_name,\n" );
		sql.append("       r.status,\n" );
		sql.append("       r.is_de,\n" );
		sql.append("       r.create_by,\n" );
		sql.append("       r.create_date)\n" );
		sql.append("    VALUES\n" );
		sql.append("      (f_getid(),\n" );
		sql.append("       "+RuleListPO.getFaultId()+",\n" );
		sql.append("       '"+RuleListPO.getPartCode()+"',\n" );
		sql.append("       '"+RuleListPO.getPartName()+"',\n" );
		sql.append("       "+RuleListPO.getStatus()+",\n" );
		sql.append("       "+RuleListPO.getIsDe()+",\n" );
		sql.append("       "+RuleListPO.getCreateBy()+",\n" );
		sql.append("       sysdate)");

		dao.update(sql.toString(), null);
	}
	
	public void claimRulePartsImportDelete(TtAsWrFaultPartsTempPO RuleListPO){
		dao.delete(RuleListPO);
	}
	
	public PageResult<TtAsWrFaultModeDetailPO> queryFailMode(String faultId,String failCode,String failName, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.id,a.FAILURE_MODE_CODE,a.FAILURE_MODE_NAME FROM tt_as_WR_Fault_mode_detail A WHERE A.fault_id = "+faultId+" ");

		if (Utility.testString(failCode)) {
			sql.append(" AND a.FAILURE_MODE_CODE LIKE '%" + failCode+ "%' ");
		}
		if (Utility.testString(failName)) {
			sql.append(" AND a.FAILURE_MODE_NAME LIKE '%" + failName+ "%' ");
		}
		sql.append(" AND A.Status = ").append(Constant.STATUS_ENABLE);
		PageResult<TtAsWrFaultModeDetailPO> ps = pageQuery(TtAsWrFaultModeDetailPO.class,sql.toString(), null, pageSize, curPage);
		return ps;
	}
	
	public  PageResult<Map<String, Object>> failModeDetailQuery(TmPtFailureModePO RuleListPO, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select rl.id,rl.failure_code,rl.failure_name\n");
		sql.append("from tm_pt_failure_mode rl  where 1=1 \n");
		if(!"".equals(RuleListPO.getFailureCode())&&!(null==RuleListPO.getFailureCode())){
			sql.append("AND rl.failure_code like '%"+RuleListPO.getFailureCode()+"%'\n");
		}
		if(!"".equals(RuleListPO.getFailureName())&&!(null==RuleListPO.getFailureName())){
			sql.append("AND rl.failure_name like '%"+RuleListPO.getFailureName()+"%'\n");
		}
		sql.append("ORDER BY  rl.id  desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
}
