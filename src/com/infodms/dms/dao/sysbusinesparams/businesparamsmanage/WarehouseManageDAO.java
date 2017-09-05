package com.infodms.dms.dao.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysbusinesparams.businesparamsmanage.WarehouseManage;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : WarehouseManageDAO.java
 * @Package: com.infodms.dms.dao.sysbusinesparams.businesparamsmanage
 * @Description: TODO(用一句话描述该文件做什么)
 * @date   : 2010-7-5 
 * @version: V1.0   
 */
public class WarehouseManageDAO extends BaseDao{
	public Logger logger = Logger.getLogger(WarehouseManageDAO.class);
	private ActionContext act = ActionContext.getContext();
	private static final WarehouseManageDAO dao = new WarehouseManageDAO ();
	public static final WarehouseManageDAO getInstance() {
		return dao;
	}
	
	/** 
	* @Title	  : getwarehouseManageList 
	* @Description: 仓库信息列表
	* @return     : PageResult<Map<String,Object>>返回类型 
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public static PageResult <Map<String,Object>> getwarehouseManageList(Long companyId, String warehouseName,String warehouseType,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMW.WAREHOUSE_ID,\n");
		sql.append("       TMW.WAREHOUSE_NAME,\n");  
		sql.append("       TMW.WAREHOUSE_CODE,\n");  
		sql.append("       TMW.WAREHOUSE_TYPE,\n");  
		sql.append("       TMW.WAREHOUSE_LEVEL,\n");  
		sql.append("       TMW.STATUS\n");  
		sql.append("  FROM TM_WAREHOUSE TMW\n");  
		sql.append(" WHERE 1 = 1\n");  
		if (null != warehouseName && !"".equals(warehouseName)) {
			sql.append("  AND TMW.WAREHOUSE_NAME LIKE '%"+warehouseName.trim()+"%'\n");
		}
		if (null != warehouseType && !"".equals(warehouseType)) {
			sql.append("  AND TMW.WAREHOUSE_TYPE = "+warehouseType+"\n");
		}
		if (null != companyId && !"".equals(companyId)) {
			sql.append("  AND TMW.COMPANY_ID = "+companyId+"\n");
		}
		sql.append(" ORDER BY TMW.UPDATE_DATE DESC,TMW.CREATE_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	
	public static Map<String,Object> getWarehouseInfo(String warehouse_id){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMW.WAREHOUSE_ID,\n");
		sql.append("       TMW.WAREHOUSE_NAME,\n");  
		sql.append("       TMW.WAREHOUSE_CODE,\n");  
		sql.append("       TMW.WAREHOUSE_TYPE,\n");  
		sql.append("       TMW.WAREHOUSE_LEVEL,\n");  
		sql.append("       TD.DEALER_CODE,\n"); 
		sql.append("       TMW.STATUS\n");  
		sql.append("  FROM TM_WAREHOUSE TMW,\n"); 
		sql.append("  	   TM_DEALER    TD\n"); 
		sql.append(" WHERE TMW.WAREHOUSE_ID = "+warehouse_id+"\n");  
		sql.append("  	   AND TMW.DEALER_ID = TD.DEALER_ID(+)\n"); 
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	public static Map<String,Object> getAddressByDealerId(String dealerId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TVA.ADDRESS\n");
		sql.append("  FROM TM_VS_ADDRESS TVA\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TVA.STATUS = 10011001\n");  
		sql.append("   AND TVA.DEALER_ID = ");
		sql.append(dealerId);
		sql.append("\n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
