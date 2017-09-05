package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class VehicleDispatchQueryDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(VehicleDispatchQueryDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	
	/**
	 * FUNCTION		:	调拨查询:查询展示(DLR)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-27
	 */
	public static PageResult <Map<String,Object>> getDispatchHistory_DLR(String dealer_Id,String startDate ,String endDate,String materialCode ,String inDealerCode,String outDealerCode, String vin,String TRANSFER_NO ,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMD.DEALER_SHORTNAME, --调入经销商\n");
		sql.append("       TMD1.DEALER_SHORTNAME OUT_DEALER_NAME,\n");
		sql.append("       TMV.VIN, --VIN\n");  
		sql.append("       TTVT.TRANSFER_REASON, --调拨原因\n");  
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");  
		sql.append("       TO_CHAR(TTVT.TRANSFER_DATE, 'yyyy-MM-dd') AS APP_DATE, --申请日期\n");  
		sql.append("       TTVT.CHECK_STATUS, --审批状态\n");
		sql.append("       (SELECT TCO.CODE_DESC FROM TC_CODE TCO WHERE TCO.CODE_ID=TTVT.CHECK_STATUS) AS CHECK_STATUS_DESC, --审批状态描述\n"); 
		sql.append("       CHK.CHECK_STATUS AS CHECK_RES, --审核结果\n");  
		sql.append("       TTVT.TRANSFER_NO, --批发号\n"); 
		sql.append("       CHK.CHECK_DESC --审批意见\n");  
		sql.append("  FROM TT_VS_VEHICLE_TRANSFER     TTVT,\n");  
		sql.append("       TM_DEALER               TMD,\n");  
		sql.append("       TM_DEALER               TMD1,\n");  
		sql.append("       TM_VEHICLE              TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL        TMVM,\n");  
		sql.append("       TT_VS_VEHICLE_TRANSFER_CHK CHK,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP  G\n");  
		sql.append(" WHERE TTVT.IN_DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTVT.OUT_DEALER_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TTVT.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");  
		sql.append("   AND TTVT.TRANSFER_ID = CHK.TRANSFER_ID(+)\n");  
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");  
		sql.append("   AND (TTVT.OUT_DEALER_ID IN ("+dealer_Id+") OR TTVT.IN_DEALER_ID IN ("+dealer_Id+"))\n");  
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTVT.TRANSFER_DATE >= TO_DATE('"+startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTVT.TRANSFER_DATE <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != TRANSFER_NO && !"".equals(TRANSFER_NO)) {
			sql.append("   AND TTVT.TRANSFER_NO ='"+TRANSFER_NO+"' \n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length>0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
				System.out.println(buffer);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR  SUBSTR(G.TREE_CODE, 0, 3) IN\n");
				sql.append("       (SELECT G.TREE_CODE\n");  
				sql.append("           FROM TM_VHCL_MATERIAL_GROUP G\n");  
				sql.append("          WHERE G.GROUP_CODE IN ("+buffer.toString()+")))\n");
			}
			
		}
		//调入经销商
		if (null != inDealerCode && !"".equals(inDealerCode)) {
			String[] inDealerCodes = inDealerCode.split(",");
			if (null != inDealerCodes && inDealerCodes.length>0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < inDealerCodes.length; i++) {
					buffer.append("'").append(inDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
				sql.append("   AND TMD.DEALER_CODE  IN ("+buffer.toString()+") \n");
			}
		}
		//调出经销商
		if (null != outDealerCode && !"".equals(outDealerCode)) {
			String[] outDealerCodes = outDealerCode.split(",");
			if (null != outDealerCodes && outDealerCodes.length>0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < outDealerCodes.length; i++) {
					buffer.append("'").append(outDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
				sql.append("   AND TMD1.DEALER_CODE  IN ("+buffer.toString()+") \n");
			}
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		sql.append(" ORDER BY TTVT.CREATE_DATE DESC,TTVT.TRANSFER_ID\n");
		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.VehicleDispatchQueryDAO.getDispatchHistory_DLR",pageSize ,curPage);
	}
	/**
	 * FUNCTION		:	调拨查询:查询展示(OEM)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-2
	 */
	public static PageResult <Map<String,Object>> getDispatchHistory_OEM(String dutyType,String orgId, String startDate ,String endDate,String materialCode ,String outDealerCode,String inDealerCode, String areaIds,String TRANSFER_NO, String vin, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMD1.DEALER_SHORTNAME OUT_DEALER, --调出经销商\n");
		sql.append("       TMD2.DEALER_SHORTNAME IN_DEALER, --调入经销商\n");  
		sql.append("       TTVT.TRANSFER_REASON, --调拨原因\n");  
		sql.append("       TMV.VIN, --VIN\n");  
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");  
		sql.append("       TO_CHAR(TTVT.TRANSFER_DATE, 'yyyy-MM-dd hh24:mi') AS APP_DATE, --申请日期\n");  
		sql.append("       TTVT.CHECK_STATUS, --审批状态\n");  
		sql.append("       CHK.CHECK_STATUS AS CHECK_RES, --审核结果\n"); 
		sql.append("       TTVT.TRANSFER_NO, --批发号\n"); 
		sql.append("       CHK.CHECK_DESC --审批意见\n");
		sql.append("  FROM TT_VS_VEHICLE_TRANSFER_CHK CHK,\n");  
		sql.append("       TT_VS_VEHICLE_TRANSFER     TTVT,\n");  
		sql.append("       TM_DEALER                  TMD1,\n");  
		sql.append("       TM_DEALER                  TMD2,\n");  
		sql.append("       TM_VEHICLE                 TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL           TMVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP     G\n");  
		sql.append(" WHERE CHK.TRANSFER_ID(+) = TTVT.TRANSFER_ID\n");  
		sql.append("   AND TTVT.OUT_DEALER_ID = TMD1.DEALER_ID\n");  
		sql.append("   AND TTVT.IN_DEALER_ID = TMD2.DEALER_ID\n");  
		sql.append("   AND TTVT.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTVT.TRANSFER_DATE >= TO_DATE('"+startDate+"','yyyy-MM-dd')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTVT.TRANSFER_DATE <= (TO_DATE('"+endDate+"','yyyy-MM-dd')+1)\n");
		}
		if (null != TRANSFER_NO && !"".equals(TRANSFER_NO)) {
			sql.append("   AND TTVT.TRANSFER_NO ='"+TRANSFER_NO+"' \n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin,"TMV")); 
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length>0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
				sql.append("       OR  SUBSTR(G.TREE_CODE, 0, 3) IN\n");
				sql.append("       (SELECT G.TREE_CODE\n");  
				sql.append("           FROM TM_VHCL_MATERIAL_GROUP G\n");  
				sql.append("          WHERE G.GROUP_CODE IN ("+buffer.toString()+")))\n");
				
			}
			
		}
		//调出经销商
		if (null != outDealerCode && !"".equals(outDealerCode)) {
			String[] outDealerCodes = outDealerCode.split(",");
			if (null != outDealerCodes && outDealerCodes.length>0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < outDealerCodes.length; i++) {
					buffer.append("'").append(outDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
				sql.append("   AND TMD1.DEALER_CODE  IN ("+buffer.toString()+") \n");
			}
		}
		//调入经销商
		if (null != inDealerCode && !"".equals(inDealerCode)) {
			String[] inDealerCodes = inDealerCode.split(",");
			if (null != inDealerCodes && inDealerCodes.length>0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < inDealerCodes.length; i++) {
					buffer.append("'").append(inDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
				sql.append("   AND TMD2.DEALER_CODE  IN ("+buffer.toString()+") \n");
			}
		}
		
        //如果是大区用户 只显示大区下边所有经销商
        if(dutyType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())){
            sql.append("AND (TMD1.DEALER_ID IN(\n");
            sql.append("    SELECT VOD.DEALER_ID FROM VW_ORG_DEALER VOD\n" );
            sql.append("    WHERE VOD.ROOT_ORG_ID="+orgId+")\n");
            sql.append("    OR TMD2.DEALER_ID IN(\n");
            sql.append("    SELECT VOD.DEALER_ID FROM VW_ORG_DEALER VOD\n" );
            sql.append("    WHERE VOD.ROOT_ORG_ID="+orgId+"))\n");
        //如果是小区用户 只显示小区下边所有经销商
        }else if(dutyType.equals(Constant.DUTY_TYPE_SMALLREGION.toString())){
            sql.append("AND (TMD1.DEALER_ID IN(\n" );
            sql.append("    SELECT TDOR.DEALER_ID FROM TM_DEALER_ORG_RELATION TDOR\n" );
            sql.append("    WHERE TDOR.ORG_ID="+orgId+")\n");
            sql.append("    OR TMD2.DEALER_ID IN(\n" );
            sql.append("    SELECT TDOR.DEALER_ID FROM TM_DEALER_ORG_RELATION TDOR\n" );
            sql.append("    WHERE TDOR.ORG_ID="+orgId+"))\n");
        }
		
		//业务范围
		if (null != areaIds && !"".equals(areaIds)) {
			sql.append("and exists\n");
			sql.append(" (select 1\n");  
			sql.append("          from tm_dealer_business_area tdba\n");  
			sql.append("         where 1 = 1\n");  
			sql.append("           and (tdba.dealer_id = tmd1.dealer_id or\n");  
			sql.append("               tdba.dealer_id = tmd2.dealer_id)\n");  
			sql.append("           and tdba.area_id in (").append(areaIds).append("))\n");

		}
		//sql.append(" ORDER BY CHK.CREATE_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.VehicleDispatchQueryDAO.getDispatchHistory_OEM",pageSize ,curPage);
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
