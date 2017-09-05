package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import antlr.collections.List;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerStorageDetailQueryDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(BillDetailTicketDao.class);
	public static DealerStorageDetailQueryDao dao = new DealerStorageDetailQueryDao();
	public static DealerStorageDetailQueryDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
public PageResult<Map<String, Object>> getDealerStorageDetailInfo(Map<String, Object> map,Integer pageSize,Integer curPage){
	String areaId = (String)map.get("areaId");
	String vin = (String)map.get("vin");
	String series = (String)map.get("series");
	String orgId = (String)map.get("orgId");
	String dealerId = (String)map.get("dealerId");
	String haveCon = (String)map.get("haveCon"); //有无经销商合同
	AclUserBean logonUser = (AclUserBean)map.get("logonUser");
	StringBuffer sbSql=new StringBuffer();
	java.util.List par=new ArrayList();
	Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
	if(chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue()){
		sbSql.append("WITH AA AS (SELECT DISTINCT DEALER_ID FROM ( SELECT   TPD.DEALER_ID \n"); 
		sbSql.append("  FROM   TR_POSE_DEALER TPD\n"); 
		sbSql.append(" WHERE   TPD.POSE_ID = "+logonUser.getPoseId()+"\n"); 
		sbSql.append("UNION ALL\n"); 
		sbSql.append("SELECT   TMDE.DEALER_ID\n"); 
		sbSql.append("  FROM   TM_DEALER TMDE, TR_POSE_DEALER TPPD\n"); 
		sbSql.append(" WHERE       TMDE.PARENT_DEALER_D = TPPD.DEALER_ID\n"); 
		sbSql.append("         AND TMDE.DEALER_TYPE = 10771001\n"); 
		sbSql.append("         AND TMDE.DEALER_LEVEL = 10851002\n"); 
		sbSql.append("         AND TPPD.POSE_ID = "+logonUser.getPoseId()+"))\n");
	}else{
		sbSql.append("WITH AA AS (SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DEALER  TRD WHERE  TRD.POSE_ID="+logonUser.getPoseId()+")\n");
		
	}
	sbSql.append("SELECT VOD.ROOT_ORG_NAME ORG_NAME,               --大区 \n"); 
	sbSql.append("       VOD.ORG_NAME REGION_NAME,             --省份\n"); 
	sbSql.append("       MAT.MODEL_NAME,             --车型\n"); 
	sbSql.append("       MAT.PACKAGE_NAME,           --配置\n"); 
	sbSql.append("       \n"); 
	sbSql.append("       VOD.DEALER_NAME,              --分销商\n"); 
	sbSql.append("       MAT.SERIES_NAME,            --车种\n"); 
	sbSql.append("       TV.VIN,                     --底盘号\n"); 
	sbSql.append("       TO_CHAR(TV.PRODUCT_DATE,'yyyy-MM-dd') PRODUCT_DATE, --生产日期\n"); 
	sbSql.append("       VOD.ROOT_DEALER_NAME P_DEALER_NAME,            --一级商家\n"); 
	sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TV.LIFE_CYCLE) CODE_DESC, --库存类别\n"); 
	sbSql.append("       CASE WHEN TV.STORAGE_DATE > ADD_MONTHS(SYSDATE,-3) AND TV.STORAGE_DATE <= SYSDATE THEN '3个月内'\n"); 
	sbSql.append("            WHEN TV.STORAGE_DATE < ADD_MONTHS(SYSDATE,-6) THEN '6个月以上'\n"); 
	sbSql.append("            ELSE '3-6个月' END STORAGE_AGE, --库存时间\n"); 
	sbSql.append("       CASE WHEN TBA.AREA_NAME = '合肥' THEN '商用车'\n"); 
	sbSql.append("            ELSE '乘用车' END VEHICLE_KIND --车辆类别\n"); 
	sbSql.append("  FROM TM_VEHICLE TV, AA AA,\n"); 
	sbSql.append("       VW_MATERIAL_GROUP_MAT MAT,\n"); 
	sbSql.append("       VW_ORG_DEALER VOD,\n"); 
	sbSql.append("       TM_BUSINESS_AREA TBA\n"); 
	sbSql.append(" WHERE  ((TV.LIFE_CYCLE = 10321002 AND TV.LOCK_STATUS=10241008) OR (TV.LIFE_CYCLE = 10321005 AND TV.LOCK_STATUS=10241001) OR (TV.LIFE_CYCLE = 10321003))\n"); 
	sbSql.append("   AND  TV.MATERIAL_ID = MAT.MATERIAL_ID\n"); 
	sbSql.append("   AND  TV.DEALER_ID = VOD.DEALER_ID AND TV.DEALER_ID = AA.DEALER_ID \n"); 
	sbSql.append("   AND  TV.YIELDLY = TBA.AREA_ID \n");
	
	if(areaId != null && !"".equals(areaId)){
		sbSql.append("   AND TV.YIELDLY = ?\n");
		par.add(areaId);
	}
	if(vin != null && !"".equals(vin)){
		sbSql.append("   AND TV.VIN LIKE ? \n");
		par.add("%"+vin+"%");
	}
	if(series != null && !"".equals(series)){
		sbSql.append("   AND MAT.SERIES_ID = ?\n");
		par.add(series);
	}
	if(orgId != null && !"".equals(orgId)){
		//sbSql.append("   AND ORG.ORG_ID IN ("+orgId+")\n");
		sbSql.append(Utility.getConSqlByParamForEqual(orgId, par,"vod", "org_id"));
	}
	if(dealerId != null && !"".equals(dealerId)){
		//sbSql.append("   AND TV.DEALER_ID IN ("+dealerId+")\n");
		sbSql.append(Utility.getConSqlByParamForEqual(dealerId, par,"tv", "dealer_Id"));
	}
	if("yes".equals(haveCon)){
		sbSql.append("   AND EXISTS (SELECT T.CONTRACT_ID FROM TT_SALES_CONTRACT T WHERE T.STATUS = "+Constant.STATUS_ENABLE+" AND T.DEALER_ID = TV.DEALER_ID )\n");
	}
	if("no".equals(haveCon)){
		sbSql.append("   AND NOT EXISTS (SELECT T.CONTRACT_ID FROM TT_SALES_CONTRACT T WHERE T.STATUS = "+Constant.STATUS_ENABLE+" AND T.DEALER_ID = TV.DEALER_ID )\n");
	}
	return pageQuery(sbSql.toString(), par,getFunName(), pageSize, curPage);
}
}
