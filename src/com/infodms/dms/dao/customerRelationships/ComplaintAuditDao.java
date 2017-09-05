package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrComplaintsAuditPO;
import com.infodms.dms.po.TtFleetEditLogPO;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.po3.bean.PO;


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
public class ComplaintAuditDao extends BaseDao<PO>{
	
	private static final ComplaintAuditDao dao = new ComplaintAuditDao();
	
	public static final ComplaintAuditDao getInstance() {
		return dao;
	}
	protected TtCrComplaintsAuditPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 插入客户投诉处理明细表
	 * @param po
	 */
	public void inserLog(TtCrComplaintsAuditPO po){
		dao.insert(po);
	}
	
	
	/**
	 * 更新客户投诉处理明细表
	 * @param po1
	 * @param po2
	 */
	public void updateAudit(TtCrComplaintsAuditPO po1,TtCrComplaintsAuditPO po2){
		dao.update(po1, po2);
	}
	
	
	/**
	 * 根据条件查询客户投诉处理明细表用于回显
	 * @param compId
	 * @param status
	 * @return
	 */
	public Map<String, Object> getAuditDetailByCompId(String compId,String status){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.ID,\n" );
		sql.append("       A.AUDIT_ACTION,\n" );
		sql.append("       A.AUDIT_RESULT,\n" );
		sql.append("       A.PART_CODE,\n" );
		sql.append("       A.SUPPLIER,\n" );
		sql.append("       A.AUDIT_CONTENT,\n" );
		sql.append("       A.AUDIT_STATUS,\n" );
		sql.append("       A.NAME,\n" );
		sql.append("       A.AUDIT_DATE\n" );
		sql.append("  FROM (SELECT TCA.ID,\n" );
		sql.append("               TCA.AUDIT_ACTION,\n" );
		sql.append("               TCA.AUDIT_RESULT,\n" );
		sql.append("               TCA.PART_CODE,\n" );
		sql.append("               TCA.SUPPLIER,\n" );
		sql.append("               TCA.AUDIT_CONTENT,\n" );
		sql.append("               TCA.AUDIT_STATUS,\n" );
		sql.append("               TU.NAME,\n" );
		sql.append("               TO_CHAR(TCA.AUDIT_DATE, 'YYYY-MM-DD HH24:MI') AUDIT_DATE\n" );
		sql.append("          FROM TT_CR_COMPLAINTS_AUDIT TCA, TC_USER TU\n" );
		sql.append("         WHERE TCA.CREATE_BY = TU.USER_ID\n" );
		sql.append("           AND TCA.COMP_ID = ").append(compId).append("\n");
		sql.append("           AND TCA.AUDIT_STATUS = ").append(status).append("\n");
		sql.append("         ORDER BY TCA.AUDIT_DATE DESC) A\n" );
		sql.append(" WHERE ROWNUM = 1\n");

		Map<String, Object> detailMap = pageQueryMap(sql.toString(),null,getFunName());
		return detailMap;
	}
	
	/**
	 * 根据投诉Id查询处理明细
	 * @param compId
	 * @return
	 */
	public List<Map<String, Object>> getAllDetailByCompId(String compId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM,A.ID,A.COMP_ID,A.ORG_NAME,A.DEALER_NAME,A.NAME,A.AUDIT_DATE,A.AUDIT_ACTION,A.AUDIT_RESULT,\n" );
		sql.append("       A.AUDIT_CONTENT,A.PART_CODE,A.SUPPLIER\n" );
		sql.append("  FROM (\n" );
		sql.append("  SELECT A.ID,A.COMP_ID,A.PART_CODE,A.SUPPLIER,B.ORG_NAME,D.DEALER_NAME,C.NAME,\n" );
		sql.append("         TO_CHAR(A.AUDIT_DATE,'YYYY-MM-DD hh24:mi')AS AUDIT_DATE,A.AUDIT_ACTION,\n" );
		sql.append("         A.AUDIT_RESULT,A.AUDIT_CONTENT\n" );
		sql.append("    FROM TT_CR_COMPLAINTS_AUDIT A, TM_ORG B, TC_USER C, TM_DEALER D\n" );
		sql.append("   WHERE A.ORG_ID = B.ORG_ID(+)\n" );
		sql.append("     AND A.CREATE_BY = C.USER_ID(+)\n" );
		sql.append("     AND A.DEALER_ID = D.DEALER_ID(+)\n" );
		sql.append("     AND A.COMP_ID = ").append(compId).append("\n");
		sql.append("ORDER BY A.AUDIT_DATE\n" );
		sql.append(") A");

		List<Map<String, Object>> detailList = pageQuery(sql.toString(),null,getFunName());
		
		return detailList;
	}
	
	/*
	 * 
	 */
	public Map<String, Object> getContentMap(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.AUDIT_CONTENT\n" );
		sql.append("FROM TT_CR_COMPLAINTS_AUDIT A\n");
		sql.append("WHERE A.ID = ").append(id).append("\n");

		Map<String, Object> detailList = pageQueryMap(sql.toString(),null,getFunName());
		
		return detailList;
	}
	
	/**
	 * 
	* @Title: delByComId 
	* @Description: TODO(根据投诉主表Id删除下端上报的投诉信息,全量插入) 
	* @param @param comId    投诉信息主键Id
	* @return void    返回类型 
	* @throws
	 */
	public void delByComId(Long comId) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM TT_CR_COMPLAINTS_AUDIT \n");
		sql.append("WHERE COMP_ID = ").append(comId);
		sql.append("AND IF_STATUS = ").append(DEConstant.IF_STATUS_1);
		delete(sql.toString(), null);
	}
	
}
