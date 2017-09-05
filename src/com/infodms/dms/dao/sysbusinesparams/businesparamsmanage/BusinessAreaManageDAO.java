package com.infodms.dms.dao.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : BusinessAreaManageDAO.java
 * @Package: com.infodms.dms.dao.sysbusinesparams.businesparamsmanage
 * @Description: TODO(用一句话描述该文件做什么)
 * @date   : 2010-7-5 
 * @version: V1.0   
 */
public class BusinessAreaManageDAO extends BaseDao{
	public Logger logger = Logger.getLogger(BusinessAreaManageDAO.class);
	private ActionContext act = ActionContext.getContext();
	private static final BusinessAreaManageDAO dao = new BusinessAreaManageDAO ();
	public static final BusinessAreaManageDAO getInstance() {
		return dao;
	}
	
	/** 
	* @Title	  : getwarehouseManageList 
	* @Description: 业务范围列表
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public static PageResult <Map<String,Object>> getbusinessAreaList(String produce_base,String area_code,String area_name,int pageSize,int curPage,Long companyId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT BA.PRODUCE_BASE,BA.AREA_ID, BA.AREA_CODE, BA.AREA_NAME, BA.STATUS, nvl(BA.AREA_SHORTCODE,'　') AREA_SHORTCODE\n");
		sql.append("  FROM TM_BUSINESS_AREA BA\n");  
//		sql.append(" WHERE BA.COMPANY_ID="+companyId+"\n");
		sql.append(" where 1 = 1 ");
		if (null != area_code && !"".equals(area_code)) {
			sql.append("  AND BA.AREA_CODE LIKE '%"+area_code.trim()+"%'\n");
		}
		if (null != area_name && !"".equals(area_name)) {
			sql.append("  AND BA.AREA_NAME LIKE '%"+area_name+"%'\n");
		}
		if(null !=produce_base&&!"".equals(produce_base)){
			sql.append(" AND BA.PRODUCE_BASE LIKE '%"+produce_base+"%'\n");
			
		}
		sql.append(" ORDER BY BA.Area_Code \n"); //YH 2011.8.10

		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	
	/** 
	* @Title	  : getbusinessAreaDetailList 
	* @Description: 业务范围详细列表
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public static PageResult <Map<String,Object>> getbusinessAreaDetailList(String area_id,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT AG.RELATION_ID ,TG.GROUP_CODE, TG.GROUP_NAME\n");
		sql.append("  FROM TM_AREA_GROUP AG, TM_VHCL_MATERIAL_GROUP TG\n");  
		sql.append(" WHERE AG.MATERIAL_GROUP_ID = TG.GROUP_ID\n");  
		sql.append("   AND AG.AREA_ID ="+area_id+"\n");  
		sql.append(" ORDER BY AG.CREATE_DATE DESC, TG.CREATE_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	
	/** 
	* @Title	  : getMaterialGroupList 
	* @Description: 得到物料组列表
	* @return     : PageResult<Map<String,Object>>返回类型 
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public static PageResult <Map<String,Object>> getMaterialGroupList(String area_id,String groupCode ,String groupName,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G.GROUP_ID, G.GROUP_CODE, G.GROUP_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G\n");  
		sql.append(" WHERE NOT EXISTS (SELECT TG.GROUP_ID\n");  
		sql.append("          FROM TM_AREA_GROUP AG, TM_VHCL_MATERIAL_GROUP TG\n");  
		sql.append("         WHERE AG.MATERIAL_GROUP_ID = TG.GROUP_ID\n");  
		sql.append("           AND AG.AREA_ID = "+area_id+"\n");  
		sql.append("           AND TG.GROUP_ID = G.GROUP_ID)\n");  
		if (null != groupCode && !"".equals(groupCode)) {
			sql.append("    AND G.GROUP_CODE LIKE '%"+groupCode.trim()+"%'\n");
		}
		if (null != groupName && !"".equals(groupName)) {
			sql.append("    AND G.GROUP_NAME LIKE '%"+groupName.trim()+"%'\n");
		}
		sql.append(" ORDER BY G.CREATE_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	
	public static PageResult <Map<String,Object>> getAllGroupList(String group_code ,String group_name,String groupIds ,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TG.GROUP_ID, TG.GROUP_CODE, TG.GROUP_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TG\n");  
		sql.append(" WHERE TG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		if (null != group_code && !"".equals(group_code)) {
			sql.append("   AND TG.GROUP_CODE LIKE '%"+group_code.trim()+"%'\n");
		}
		if (null != group_name && !"".equals(group_name)) {
			sql.append("   AND TG.GROUP_NAME LIKE '%"+group_name.trim()+"%'\n");
		}
		if (null != groupIds && !"".equals(groupIds)) {
			String[] array = groupIds.split(",");
			sql.append("   AND TG.GROUP_ID IN ( \n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'"+array[i]+"'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append(" ORDER BY TG.CREATE_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
