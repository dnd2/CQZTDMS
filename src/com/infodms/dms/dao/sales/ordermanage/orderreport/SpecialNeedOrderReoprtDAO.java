package com.infodms.dms.dao.sales.ordermanage.orderreport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SpecialNeedOrderReoprtDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(SpecialNeedOrderReoprtDAO.class);
	private static final SpecialNeedOrderReoprtDAO dao = new SpecialNeedOrderReoprtDAO ();
	public static final SpecialNeedOrderReoprtDAO getInstance() {
		return dao;
	}
	
	/**
	 * 查询可提报的订做车需求列表
	 * */
	public static PageResult <Map<String,Object>> queryCanReportSpecialList (String dealerId, String startDate,String endDate, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TVSR.REQ_ID,\n");
		sql.append("       TMBA.AREA_NAME, --业务范围\n");  
		sql.append("	   TMBA.AREA_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");  
		sql.append("       TMD.DEALER_ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");  
		sql.append("       TO_CHAR(TVSR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE, --需求提报日期\n");  
		sql.append("       TO_CHAR(TVSR.REQ_CONFIRM_DATE, 'YYYY-MM-DD') REQ_CONFIRM_DATE, --需求确认日期\n");  
		sql.append("       replace(TVSR.REFIT_DESC, chr(13) || chr(10), '') REFIT_DESC, --订做车改装说明\n");  
		sql.append("       TVSR.REQ_STATUS,\n");  
		sql.append("       nvl(SUM(tvod.order_amount), 0) AMOUNT --提报数量\n");  
		sql.append("  FROM TT_VS_SPECIAL_REQ     TVSR,\n");  
		/*sql.append("       TT_VS_SPECIAL_REQ_DTL TVSRD,\n");*/  
		sql.append("       TM_DEALER             TMD,\n");  
		sql.append("       TM_BUSINESS_AREA      TMBA,\n");  
		sql.append("	   (select tvo.order_id, TVO.SPECIAL_REQ_ID from TT_VS_ORDER tvo where tvo.order_STATUS not in (" + Constant.ORDER_STATUS_06 + "))           TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL    TVOD\n");
		/*sql.append(" WHERE TVSR.REQ_ID = TVSRD.REQ_ID\n");  */
		sql.append(" WHERE TVSR.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TVSR.AREA_ID = TMBA.AREA_ID\n");  
		sql.append("   AND TVSR.REQ_STATUS = "+Constant.SPECIAL_NEED_STATUS_08+"\n"); 
		//sql.append("   AND tvo.order_STATUS not in (" + Constant.ORDER_STATUS_04 + "," + Constant.ORDER_STATUS_06 + ")\n"); 
		sql.append("AND TVO.SPECIAL_REQ_ID(+) = TVSR.REQ_ID\n");
		sql.append("   AND TVOD.ORDER_ID(+) = TVO.ORDER_ID\n");

		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("   AND TVSR.DEALER_ID IN (").append(dealerId).append(")\n") ;
		}
		
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TVSR.REQ_DATE >=\n");  
			sql.append("       TO_DATE('"+startDate+"', 'YYYY-MM-DD HH24:MI:SS')\n"); 
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TVSR.REQ_DATE <=\n");  
			sql.append("       TO_DATE('"+endDate+"', 'YYYY-MM-DD HH24:MI:SS')\n");  
		} 
		/*sql.append("AND NVL(TVSRD.ORDER_AMOUNT,0) < TVSRD.AMOUNT\n");	//2012-06-01 新增提报数量大于或等于需求数量时不显示
*/		sql.append("GROUP BY TVSR.REQ_ID,\n");
		sql.append("         TMBA.AREA_NAME,\n");  
		sql.append("         TMD.DEALER_CODE,\n");  
		sql.append("         TMD.DEALER_SHORTNAME,\n");  
		sql.append("         TVSR.REQ_DATE,\n");  
		sql.append("         TVSR.REQ_CONFIRM_DATE,\n");  
		sql.append("         TVSR.REFIT_DESC,\n");  
		sql.append("         TVSR.REQ_STATUS,\n");  
		sql.append("         TMBA.AREA_ID,\n");  
		sql.append("         TMD.DEALER_ID\n");

		sql.append(" ORDER BY TVSR.REQ_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> getMaterialGroupInfo(String materialCode, String reqId, Long companyId, String addedMaterialId) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TVMG3.GROUP_NAME SERIES_CODE,\n");
		sql.append("       TVMG.GROUP_ID,\n");  
		sql.append("       TVM.MATERIAL_ID,\n");  
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVMG2.GROUP_CODE,\n");  
		sql.append("       T.SALES_PRICE,\n");  
		sql.append("       T.CHANGE_PRICE,\n");  
		sql.append("	   T.RESOURCE_AMOUNT,\n");
		sql.append("	   T.SPECIAL_BATCH_NO,\n");
		sql.append("	   T.AMOUNT,\n");
		sql.append("	   T.APPLY_AMOUNT,\n");
		sql.append("       T.ORDER_AMOUNT\n");
		sql.append("FROM   TM_VHCL_MATERIAL_GROUP TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");  
		sql.append("       TM_VHCL_MATERIAL TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");  
		sql.append("       ( SELECT A.SERIES_NAME,\n");  
		sql.append("       A.GROUP_ID,\n");  
		sql.append("       A.GROUP_CODE,\n");  
		sql.append("       A.GROUP_NAME,\n");  
		sql.append("     A.MODEL_CODE,\n");  
		sql.append("       A.SPECIAL_BATCH_NO,\n");  
		sql.append("       A.APPLY_AMOUNT,\n");  
		sql.append("       A.AMOUNT,\n");  
		sql.append("       A.DTL_ID,\n");  
		sql.append("       A.ORDER_AMOUNT,\n");  
		sql.append("       A.SALES_PRICE,\n");  
		sql.append("     A.CHANGE_PRICE,\n");  
		sql.append("       CASE\n");  
		sql.append("         WHEN NVL(B.AVA_STOCK, 0) - NVL(C.ORDER_AMOUNT, 0) <= 0 THEN\n");  
		sql.append("          '无'\n");  
		sql.append("         ELSE\n");  
		sql.append("          '有'\n");  
		sql.append("       END RESOURCE_AMOUNT\n");  
		sql.append("  FROM (SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");  
		sql.append("               TVMG.GROUP_ID,\n");  
		sql.append("               TVMG.GROUP_CODE,\n");  
		sql.append("               TVMG.GROUP_NAME,\n");  
		sql.append("         TVMG.MODEL_CODE,\n");  
		sql.append("         TVDRD.CHANGE_PRICE,\n");  
		sql.append("               NVL(TVDRD.SPECIAL_BATCH_NO, '') SPECIAL_BATCH_NO,\n");  
		sql.append("               TVDRD.AMOUNT APPLY_AMOUNT,\n");  
		sql.append("               TVDRD.AMOUNT - NVL(TVDRD.ORDER_AMOUNT, 0) AMOUNT,\n");  
		sql.append("               TVDRD.DTL_ID,\n");  
		sql.append("               NVL(TVDRD.ORDER_AMOUNT, 0) ORDER_AMOUNT,\n");  
		sql.append("               TVDRD.SALES_PRICE\n");  
		sql.append("          FROM TT_VS_SPECIAL_REQ_DTL    TVDRD,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG2,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG3\n");  
		sql.append("         WHERE TVDRD.MATERIAL_ID = TVMG.GROUP_ID\n");  
		sql.append("           AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("           AND TVDRD.REQ_ID = " + reqId + ") A,\n");  
		sql.append("       (SELECT TVMGR.GROUP_ID,\n");  
		sql.append("               NVL(VVR.SPECIAL_BATCH_NO, '') SPECIAL_BATCH_NO,\n");  
		sql.append("               SUM(VVR.AVA_STOCK) AVA_STOCK\n");  
		sql.append("          FROM VW_VS_RESOURCE VVR,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR\n");  
		sql.append("         WHERE VVR.COMPANY_ID = " + companyId + "\n");  
		sql.append("         AND VVR.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("         GROUP BY TVMGR.GROUP_ID, VVR.SPECIAL_BATCH_NO) B,\n");  
		sql.append("       ( SELECT TVMGR.GROUP_ID,\n");  
		sql.append("               NVL(TVDRD.PATCH_NO, '') PATCH_NO,\n");  
		sql.append("               SUM(TVDRD.REQ_AMOUNT) ORDER_AMOUNT\n");  
		sql.append("          FROM TT_VS_ORDER         TVO,\n");  
		sql.append("               TT_VS_DLVRY_REQ     TVDR,\n");  
		sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR\n");  
		sql.append("         WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");  
		sql.append("           AND TVDR.REQ_ID = TVDRD.REQ_ID\n");  
		sql.append("           AND TVDR.REQ_STATUS = 11571001\n");  
		sql.append("           AND TVO.COMPANY_ID = " + companyId + "\n");  
		sql.append("           AND TVDRD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("         GROUP BY TVMGR.GROUP_ID, TVDRD.PATCH_NO) C\n");  
		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");  
		sql.append("   AND A.SPECIAL_BATCH_NO = B.SPECIAL_BATCH_NO(+)\n");  
		sql.append("   AND A.GROUP_ID = C.GROUP_ID(+)\n");  
		sql.append("   AND A.SPECIAL_BATCH_NO = C.PATCH_NO(+)) T\n");  
		sql.append("WHERE 1=1\n");  
		if(materialCode != null && !"".equals(materialCode)) {
			sql.append("AND TVM.MATERIAL_CODE IN ('");
			String[] materialCodes = materialCode.split(","); 
			for(int i=0; i<materialCodes.length; i++) {
				if(i != materialCodes.length-1) {
					sql.append(materialCodes[i]);
					sql.append("','");
				} else {
					sql.append(materialCodes[i]);
				}
			}
			sql.append("')\n");
		} 
		sql.append("      AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("      AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("      AND TVMG2.GROUP_ID = TVMG.PARENT_GROUP_ID\n");  
		sql.append("      AND TVMG3.GROUP_ID = TVMG2.PARENT_GROUP_ID\n");
		if(addedMaterialId != null && !"".equals(addedMaterialId)){
			sql.append("AND TVM.MATERIAL_ID not in (");
			sql.append(addedMaterialId);
			sql.append(")\n");
		}
		sql.append("      AND T.GROUP_ID(+) = TVMG.GROUP_ID\n");
		return dao.pageQuery(sql.toString(), null,  dao.getFunName());
	}
	
}
