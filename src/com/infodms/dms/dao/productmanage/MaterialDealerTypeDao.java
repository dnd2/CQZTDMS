/**
 * @Title: ProductManageDao.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-1
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.dao.productmanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmpVsPriceDtlPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class MaterialDealerTypeDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(MaterialDealerTypeDao.class);
	private static final MaterialDealerTypeDao dao = new MaterialDealerTypeDao();

	public static final MaterialDealerTypeDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 物料维护查询列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getMaterialDealerTypeQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		
		String dealerCode = (String) map.get("dealerCode");
		String dealerName = (String) map.get("dealerName");
		String status = (String) map.get("status");
		String is_rule_seach = (String) map.get("is_rule_seach");
		String is_export_seach = (String) map.get("is_export_seach");
		String is_Insale_seach = (String) map.get("is_Insale_seach");
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer("\n");

		sbSql.append("SELECT A.DEALER_CODE,\n");
		sbSql.append("       A.DEALER_ID,\n");
		sbSql.append("       A.DEALER_NAME,B.ID,\n");
		sbSql.append("       B.EXPORT_SALES_FLAG,\n");
		sbSql.append("       B.IS_INSALE,\n");
		sbSql.append("       B.IS_RULE_MAT\n");
		sbSql.append("  FROM TM_DEALER A, TM_DEALER_MAT_TYPE B\n");
		sbSql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+)\n");
		sbSql.append("   AND A.STATUS = 10011001\n");//如需查出无效的就去掉该状态
		sbSql.append("   AND A.OEM_COMPANY_ID = 2010010100070674\n");
		sbSql.append("   AND A.DEALER_LEVEL = 10851001 --一级经销商\n");
		sbSql.append("   AND A.DEALER_TYPE = 10771001 --销售经销商\n");
		if (!CommonUtils.isNullString(dealerCode)) {
			sbSql.append("   AND A.DEALER_CODE LIKE ?\n");  
			params.add("%"+dealerCode+"%");
		}
		if (!CommonUtils.isNullString(dealerName)) {
			sbSql.append("   AND A.DEALER_NAME LIKE ?\n"); 
			params.add("%"+dealerName+"%");
		}
		if (!CommonUtils.isNullString(status)) {
			sbSql.append("   AND A.STATUS=?\n");  
			params.add(status);
		}
		if (!CommonUtils.isNullString(is_rule_seach)) {
			sbSql.append("   AND B.IS_RULE_MAT=?\n"); 
			params.add(is_rule_seach);
		}
		if (!CommonUtils.isNullString(is_export_seach)) {
			sbSql.append("   AND B.EXPORT_SALES_FLAG=?\n"); 
			params.add(is_export_seach); 
		}
		if (!CommonUtils.isNullString(is_Insale_seach)) {
			sbSql.append("   AND B.IS_INSALE=?\n"); 
			params.add(is_Insale_seach);  
		}
		sbSql.append(" ORDER BY A.DEALER_CODE, A.DEALER_NAME"); 
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(),params,getFunName(),pageSize, curPage);
		return ps;
	}
	public void setMaterialStatusOne(Map<String, Object> dataMap) throws Exception
	{
		String isRuleMat = CommonUtils.checkNull(dataMap.get("isRuleMat"));
		String exportSalesFlag = CommonUtils.checkNull(dataMap.get("exportSalesFlag"));
		String isInsale = CommonUtils.checkNull(dataMap.get("isInsale"));
		String dealerId = CommonUtils.checkNull(dataMap.get("dealerId"));
		String id = CommonUtils.checkNull(dataMap.get("id"));
		String userId = CommonUtils.checkNull(dataMap.get("userId"));
		if(id.equals("null") || id.equals("")){//非空就修改
			StringBuffer insertSql = new StringBuffer("\n");
			insertSql.append("INSERT INTO TM_DEALER_MAT_TYPE(ID,DEALER_ID,IS_RULE_MAT,EXPORT_SALES_FLAG,IS_INSALE,CREATE_BY,CREATE_DATE)\n");
			insertSql.append(" VALUES(f_getid,'"+dealerId+"','"+isRuleMat+"','"+exportSalesFlag+"','"+isInsale+"','"+userId+"',SYSDATE)"); 
			dao.insert(insertSql.toString());
		}else{//空就添加
			StringBuffer updateSql = new StringBuffer(); 
			List<Object> params = new ArrayList<Object>();
			updateSql.append("UPDATE TM_DEALER_MAT_TYPE SET\n");
			if(!isRuleMat.equals("")) {
				params.add(isRuleMat);
				updateSql.append("IS_RULE_MAT = ?\n");
			}
			if(!exportSalesFlag.equals("")) {
				params.add(exportSalesFlag);
				if(!isRuleMat.equals("")){
					updateSql.append(",");
				}
				updateSql.append("EXPORT_SALES_FLAG = ?\n");
			}
			if(!isInsale.equals("")) {
				params.add(isInsale);
				if(!exportSalesFlag.equals("")){
					updateSql.append(",");
				}
				updateSql.append("IS_INSALE = ?\n");
			}
			if(!userId.equals("")) {
				params.add(userId);
				updateSql.append(",UPDATE_BY = ?,UPDATE_DATE=SYSDATE\n");
			}
			updateSql.append("WHERE ID =? \n");
			params.add(id);
			dao.update(updateSql.toString(), params);
		}		
	}
}
