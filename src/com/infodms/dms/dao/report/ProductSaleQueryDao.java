package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class ProductSaleQueryDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(BillDetailTicketDao.class);
	public static ProductSaleQueryDao dao = new ProductSaleQueryDao();
	public static ProductSaleQueryDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
public List<Map<String, Object>> getProductSaleReportInfo(Map<String, Object> map,Integer pageSize,Integer curPage){
	
	/** add by wangsw 2013-11-24*/
	// 需要排除的省份站点
	StringBuffer orgExp = new StringBuffer();
	
	orgExp.append("2013080510045491").append(",");
	orgExp.append("2013082525987270").append(",");
	orgExp.append("2013082525987271").append(",");
	orgExp.append("2013082525987272").append(",");
	orgExp.append("2013082525987273").append(",");
	orgExp.append("2013082525987274").append(",");
	orgExp.append("2013082525987275").append(",");
	orgExp.append("2013082525987276").append(",");
	orgExp.append("2013082525987277");
	
	StringBuffer sbSql = new StringBuffer();
	sbSql.append("select * from (\n");
	sbSql.append("WITH YEAR_STORAGE AS ( SELECT COUNT(1) AMOUNT,TV.MODEL_ID\n");
	sbSql.append("                        FROM  TM_VEHICLE TV\n");
	sbSql.append("                        WHERE TV.PROV_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                        AND TO_CHAR(TV.ORG_STORAGE_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy')\n");
	sbSql.append("                       -- UNION ALL\n");
	sbSql.append("                        GROUP BY TV.MODEL_ID ),--当年入库\n");
	sbSql.append("MONTH_STORAGE AS ( SELECT COUNT(1) AMOUNT, TV.MODEL_ID\n");
	sbSql.append("                     FROM TM_VEHICLE TV\n");
	sbSql.append("                    WHERE TV.PROV_ID   NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                      AND TO_CHAR(TV.ORG_STORAGE_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM')\n");
	sbSql.append("                    GROUP BY TV.MODEL_ID ),--当月入库\n");
	sbSql.append("DAY_STORAGE AS ( SELECT COUNT(1) AMOUNT, TV.MODEL_ID\n");
	sbSql.append("                   FROM TM_VEHICLE TV\n");
	sbSql.append("                  WHERE TV.PROV_ID  NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                    AND TO_CHAR(TV.ORG_STORAGE_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd')\n");
	sbSql.append("                  GROUP BY TV.MODEL_ID ),--当日入库\n");
	sbSql.append("YEAR_F_CHECK AS (\n");
	sbSql.append("                  SELECT A.MODEL_ID,SUM(A.CHECK_AMOUNT) AMOUNT FROM\n");
	sbSql.append("                  ( SELECT  NVL(TVOD.CHECK_AMOUNT,0) CHECK_AMOUNT,\n");
	sbSql.append("                          MAT.MODEL_ID,\n");
	sbSql.append("                          TO_CHAR(TVO.INVO_DATE,'yyyy') INVO_DATE\n");
	sbSql.append("                    FROM  TT_VS_ORDER TVO,\n");
	sbSql.append("                          TT_VS_ORDER_DETAIL TVOD,\n");
	sbSql.append("                          TM_DEALER TD,\n");
	sbSql.append("                          TM_DEALER_ORG_RELATION TDOR,\n");
	sbSql.append("                          TM_ORG ORG,\n");
	sbSql.append("                          VW_MATERIAL_GROUP_MAT MAT\n");
	sbSql.append("                   WHERE  TVO.INVOICE_NO IS NOT NULL AND TVO.INVO_DATE IS NOT NULL\n");
	sbSql.append("                     AND  TVO.ORDER_STATUS IN (10211007,10211008,10211009,10211011,10211012)\n");
	sbSql.append("                     AND  TVOD.ORDER_ID = TVO.ORDER_ID\n");
	sbSql.append("                     AND  TVOD.MATERIAL_ID = MAT.MATERIAL_ID\n");
	sbSql.append("                     AND  TVO.DEALER_ID = TD.DEALER_ID\n");
	sbSql.append("                     AND ( TD.DEALER_ID = TDOR.DEALER_ID OR TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
	sbSql.append("                     AND  TDOR.ORG_ID = ORG.ORG_ID\n");
	sbSql.append("                     AND  ORG.ORG_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                     AND  TO_CHAR(TVO.INVO_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy')\n");
	sbSql.append("                   UNION ALL\n");
	sbSql.append("                   SELECT   NVL(TVOD.OUT_AMOUNT,0)  CHECK_AMOUNT,\n");
	sbSql.append("                            MAT.MODEL_ID,\n");
	sbSql.append("                            TO_CHAR(TVO.INVO_DATE,'yyyy') INVO_DATE\n");
	sbSql.append("                     FROM   TT_OUT_ORDER_DETAIL TVOD,\n");
	sbSql.append("                            TT_OUT_ORDER TVO,\n");
	sbSql.append("                            VW_MATERIAL_GROUP_MAT MAT,\n");
	sbSql.append("                            TM_DEALER TD,\n");
	sbSql.append("                            TM_DEALER_ORG_RELATION TDOR,\n");
	sbSql.append("                            TM_ORG ORG\n");
	sbSql.append("                     WHERE  TVO.INVOICE_NO IS NOT NULL AND TVO.INVO_DATE IS NOT NULL\n");
	sbSql.append("                     AND    TVO.ORDER_STATUS = 13741006\n");
	sbSql.append("                     AND    TVOD.ORDER_ID = TVO.ORDER_ID\n");
	sbSql.append("                     AND    TVOD.MATERIAL_ID = MAT.MATERIAL_ID\n");
	sbSql.append("                     AND    TVO.DEALER_ID = TD.DEALER_ID\n");
	sbSql.append("                     AND    (TD.DEALER_ID = TDOR.DEALER_ID OR TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
	sbSql.append("                     AND    TDOR.ORG_ID = ORG.ORG_ID\n");
	sbSql.append("                     AND    ORG.ORG_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                     AND  TO_CHAR(TVO.INVO_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy')\n");
	sbSql.append("                     UNION ALL\n");
	sbSql.append("                     SELECT TO_NUMBER(NVL(TVO.FBILLWAY,0))*TO_NUMBER(NVL(TVO.F_AMOUNT,0)) CHECK_AMOUNT,\n");
	sbSql.append("                            VMG.GROUP_ID MODEL_ID,\n");
	sbSql.append("                            TO_CHAR(TVO.INVO_DATE,'yyyy') INVO_DATE\n");
	sbSql.append("                       FROM TT_OLD_ORDER TVO , TM_VHCL_MATERIAL_GROUP VMG\n");
	sbSql.append("                      WHERE TVO.FLAG IN (0,2,4)\n");
	sbSql.append("                        AND TVO.INVO_DATE IS NOT NULL\n");
	sbSql.append("                        AND TVO.MODEL = VMG.GROUP_CODE\n");
	sbSql.append("                        AND TVO.MODEL = VMG.GROUP_CODE\n");
	sbSql.append("                        AND VMG.GROUP_LEVEL = 3\n");
	sbSql.append("                        AND TVO.PROV_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                        AND TO_CHAR(TVO.INVO_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy')\n");
	sbSql.append("                        AND MODEL IS NOT NULL ) A GROUP BY A.MODEL_ID),    --当年开票\n");
	sbSql.append("MONTH_F_CHECK AS (\n");
	sbSql.append("                   SELECT A.MODEL_ID,SUM(A.CHECK_AMOUNT) AMOUNT FROM\n");
	sbSql.append("                  ( SELECT  NVL(TVOD.CHECK_AMOUNT,0) CHECK_AMOUNT,\n");
	sbSql.append("                          MAT.MODEL_ID,\n");
	sbSql.append("                          TO_CHAR(TVO.INVO_DATE,'yyyy-MM') INVO_DATE\n");
	sbSql.append("                    FROM  TT_VS_ORDER TVO,\n");
	sbSql.append("                          TT_VS_ORDER_DETAIL TVOD,\n");
	sbSql.append("                          TM_DEALER TD,\n");
	sbSql.append("                          TM_DEALER_ORG_RELATION TDOR,\n");
	sbSql.append("                          TM_ORG ORG,\n");
	sbSql.append("                          VW_MATERIAL_GROUP_MAT MAT\n");
	sbSql.append("                   WHERE  TVO.INVOICE_NO IS NOT NULL AND TVO.INVO_DATE IS NOT NULL\n");
	sbSql.append("                     AND  TVO.ORDER_STATUS IN (10211007,10211008,10211009,10211011)\n");
	sbSql.append("                     AND  TVOD.ORDER_ID = TVO.ORDER_ID\n");
	sbSql.append("                     AND  TVOD.MATERIAL_ID = MAT.MATERIAL_ID\n");
	sbSql.append("                     AND  TVO.DEALER_ID = TD.DEALER_ID\n");
	sbSql.append("                     AND ( TD.DEALER_ID = TDOR.DEALER_ID OR TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
	sbSql.append("                     AND  TDOR.ORG_ID = ORG.ORG_ID\n");
	sbSql.append("                     AND  ORG.ORG_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                     AND  TO_CHAR(TVO.INVO_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM')\n");
	sbSql.append("                   UNION ALL\n");
	sbSql.append("                   SELECT   NVL(TVOD.OUT_AMOUNT,0)  CHECK_AMOUNT,\n");
	sbSql.append("                            MAT.MODEL_ID,\n");
	sbSql.append("                            TO_CHAR(TVO.INVO_DATE,'yyyy') INVO_DATE\n");
	sbSql.append("                     FROM   TT_OUT_ORDER_DETAIL TVOD,\n");
	sbSql.append("                            TT_OUT_ORDER TVO,\n");
	sbSql.append("                            VW_MATERIAL_GROUP_MAT MAT,\n");
	sbSql.append("                            TM_DEALER TD,\n");
	sbSql.append("                            TM_DEALER_ORG_RELATION TDOR,\n");
	sbSql.append("                            TM_ORG ORG\n");
	sbSql.append("                     WHERE  TVO.INVOICE_NO IS NOT NULL AND TVO.INVO_DATE IS NOT NULL\n");
	sbSql.append("                     AND    TVO.ORDER_STATUS = 13741006\n");
	sbSql.append("                     AND    TVOD.ORDER_ID = TVO.ORDER_ID\n");
	sbSql.append("                     AND    TVOD.MATERIAL_ID = MAT.MATERIAL_ID\n");
	sbSql.append("                     AND    TVO.DEALER_ID = TD.DEALER_ID\n");
	sbSql.append("                     AND    (TD.DEALER_ID = TDOR.DEALER_ID OR TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
	sbSql.append("                     AND    TDOR.ORG_ID = ORG.ORG_ID\n");
	sbSql.append("                     AND    ORG.ORG_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                     AND  TO_CHAR(TVO.INVO_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM')\n");
	sbSql.append("                     UNION ALL\n");
	sbSql.append("                     SELECT TO_NUMBER(NVL(TVO.FBILLWAY,0))*TO_NUMBER(NVL(TVO.F_AMOUNT,0)) CHECK_AMOUNT,\n");
	sbSql.append("                            VMG.GROUP_ID MODEL_ID,\n");
	sbSql.append("                            TO_CHAR(TVO.INVO_DATE,'yyyy-MM') INVO_DATE\n");
	sbSql.append("                       FROM TT_OLD_ORDER TVO , TM_VHCL_MATERIAL_GROUP VMG\n");
	sbSql.append("                      WHERE TVO.FLAG IN (0,2,4)\n");
	sbSql.append("                        AND TVO.INVO_DATE IS NOT NULL\n");
	sbSql.append("                        AND TVO.MODEL = VMG.GROUP_CODE\n");
	sbSql.append("                        AND VMG.GROUP_LEVEL = 3\n");
	sbSql.append("                        AND TVO.PROV_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                        AND TO_CHAR(TVO.INVO_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM')\n");
	sbSql.append("                        AND MODEL IS NOT NULL ) A GROUP BY A.MODEL_ID),    --当月开票\n");
	sbSql.append("DAY_F_CHECK AS (\n");
	sbSql.append("                  SELECT A.MODEL_ID,SUM(A.CHECK_AMOUNT) AMOUNT FROM\n");
	sbSql.append("                  ( SELECT  NVL(TVOD.CHECK_AMOUNT,0) CHECK_AMOUNT,\n");
	sbSql.append("                          MAT.MODEL_ID,\n");
	sbSql.append("                          TO_CHAR(TVO.INVO_DATE,'yyyy-MM-dd') INVO_DATE\n");
	sbSql.append("                    FROM  TT_VS_ORDER TVO,\n");
	sbSql.append("                          TT_VS_ORDER_DETAIL TVOD,\n");
	sbSql.append("                          TM_DEALER TD,\n");
	sbSql.append("                          TM_DEALER_ORG_RELATION TDOR,\n");
	sbSql.append("                          TM_ORG ORG,\n");
	sbSql.append("                          VW_MATERIAL_GROUP_MAT MAT\n");
	sbSql.append("                   WHERE  TVO.INVOICE_NO IS NOT NULL AND TVO.INVO_DATE IS NOT NULL\n");
	sbSql.append("                     AND  TVO.ORDER_STATUS IN (10211007,10211008,10211009,10211011)\n");
	sbSql.append("                     AND  TVOD.ORDER_ID = TVO.ORDER_ID\n");
	sbSql.append("                     AND  TVOD.MATERIAL_ID = MAT.MATERIAL_ID\n");
	sbSql.append("                     AND  TVO.DEALER_ID = TD.DEALER_ID\n");
	sbSql.append("                     AND ( TD.DEALER_ID = TDOR.DEALER_ID OR TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
	sbSql.append("                     AND  TDOR.ORG_ID = ORG.ORG_ID\n");
	sbSql.append("                     AND  ORG.ORG_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                     AND  TO_CHAR(TVO.INVO_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd')\n");
	sbSql.append("                   UNION ALL\n");
	sbSql.append("                   SELECT   NVL(TVOD.OUT_AMOUNT,0)  CHECK_AMOUNT,\n");
	sbSql.append("                            MAT.MODEL_ID,\n");
	sbSql.append("                            TO_CHAR(TVO.INVO_DATE,'yyyy-MM-dd') INVO_DATE\n");
	sbSql.append("                     FROM   TT_OUT_ORDER_DETAIL TVOD,\n");
	sbSql.append("                            TT_OUT_ORDER TVO,\n");
	sbSql.append("                            VW_MATERIAL_GROUP_MAT MAT,\n");
	sbSql.append("                            TM_DEALER TD,\n");
	sbSql.append("                            TM_DEALER_ORG_RELATION TDOR,\n");
	sbSql.append("                            TM_ORG ORG\n");
	sbSql.append("                     WHERE  TVO.INVOICE_NO IS NOT NULL AND TVO.INVO_DATE IS NOT NULL\n");
	sbSql.append("                     AND    TVO.ORDER_STATUS = 13741006\n");
	sbSql.append("                     AND    TVOD.ORDER_ID = TVO.ORDER_ID\n");
	sbSql.append("                     AND    TVOD.MATERIAL_ID = MAT.MATERIAL_ID\n");
	sbSql.append("                     AND    TVO.DEALER_ID = TD.DEALER_ID\n");
	sbSql.append("                     AND    (TD.DEALER_ID = TDOR.DEALER_ID OR TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
	sbSql.append("                     AND    TDOR.ORG_ID = ORG.ORG_ID\n");
	sbSql.append("                     AND    ORG.ORG_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                     AND  TO_CHAR(TVO.INVO_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd')\n");
	sbSql.append("                     UNION ALL\n");
	sbSql.append("                     SELECT TO_NUMBER(NVL(TVO.FBILLWAY,0))*TO_NUMBER(NVL(TVO.F_AMOUNT,0)) CHECK_AMOUNT,\n");
	sbSql.append("                            VMG.GROUP_ID MODEL_ID,\n");
	sbSql.append("                            TO_CHAR(TVO.INVO_DATE,'yyyy-MM-dd') INVO_DATE\n");
	sbSql.append("                       FROM TT_OLD_ORDER TVO , TM_VHCL_MATERIAL_GROUP VMG\n");
	sbSql.append("                      WHERE TVO.FLAG IN (0,2,4)\n");
	sbSql.append("                        AND TVO.INVO_DATE IS NOT NULL\n");
	sbSql.append("                        AND TVO.MODEL = VMG.GROUP_CODE\n");
	sbSql.append("                        AND TVO.MODEL = VMG.GROUP_CODE\n");
	sbSql.append("                        AND VMG.GROUP_LEVEL = 3\n");
	sbSql.append("                        AND TVO.PROV_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                        AND TO_CHAR(TVO.INVO_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd')\n");
	sbSql.append("                        AND MODEL IS NOT NULL \n");
	sbSql.append(			") A GROUP BY A.MODEL_ID),    --当日开票\n");
	sbSql.append("S_VEHICLE AS  (\n");
	sbSql.append("                  SELECT MODEL_ID,sum(AMOUNT) as AMOUNT from\n");
	sbSql.append("                  (SELECT  TV.MODEL_ID,1 as AMOUNT\n");
	sbSql.append("                  FROM    TM_VEHICLE TV\n");
	sbSql.append("                  WHERE   TV.LOCK_STATUS IN (10241001) AND TV.PROV_ID NOT IN ("+orgExp.toString()+")\n");
	sbSql.append("                  AND     TV.LIFE_CYCLE = 10321002\n");
//	sbSql.append("                  union all\n");
//	sbSql.append("                  SELECT  TV.MODEL_ID,-1 as AMOUNT\n");
//	sbSql.append("                  FROM    TM_VEHICLE TV\n");
//	sbSql.append("                  where 1 = 1\n");
//	sbSql.append("                and  tv.org_storage_date>sysdate\n");
//	sbSql.append("                  union all\n");
//	sbSql.append("                  select mat.MODEL_ID,1 as AMOUNT\n");
//	sbSql.append("                  from tt_sales_alloca_de t,tm_vehicle tv,vw_material_group_mat mat\n");
//	sbSql.append("                  where t.vehicle_id = tv.vehicle_id\n");
//	sbSql.append("                    and tv.material_id = mat.MATERIAL_ID\n");
//	sbSql.append("                    and t.is_out = 10041002 and t.out_date > sysdate\n");
	sbSql.append("                  )group by MODEL_ID\n");
	sbSql.append("                  ), --可发车\n");
	sbSql.append("Y_SALES AS ( SELECT TV.MODEL_ID,COUNT(TDAS.VEHICLE_ID) AMOUNT\n");
	sbSql.append("             FROM   TT_DEALER_ACTUAL_SALES TDAS,\n");
	sbSql.append("                    TM_VEHICLE TV\n");
	sbSql.append("             WHERE  TDAS.VEHICLE_ID = TV.VEHICLE_ID\n");
	sbSql.append("                    AND TDAS.IS_RETURN = 10041002     AND TO_CHAR(TDAS.SALES_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy')\n");
	sbSql.append("             GROUP BY TV.MODEL_ID ),--当年最终销售\n");
	sbSql.append("M_SALES AS ( SELECT TV.MODEL_ID,COUNT(TDAS.VEHICLE_ID) AMOUNT\n");
	sbSql.append("             FROM   TT_DEALER_ACTUAL_SALES TDAS,\n");
	sbSql.append("                    TM_VEHICLE TV\n");
	sbSql.append("             WHERE  TDAS.VEHICLE_ID = TV.VEHICLE_ID\n");
	sbSql.append("                     AND TDAS.IS_RETURN = 10041002    AND TO_CHAR(TDAS.SALES_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM')\n");
	sbSql.append("             GROUP BY TV.MODEL_ID ),--当月最终销售\n");
	sbSql.append("D_SALES AS ( SELECT TV.MODEL_ID,COUNT(TDAS.VEHICLE_ID) AMOUNT\n");
	sbSql.append("             FROM   TT_DEALER_ACTUAL_SALES TDAS,\n");
	sbSql.append("                    TM_VEHICLE TV\n");
	sbSql.append("             WHERE  TDAS.VEHICLE_ID = TV.VEHICLE_ID\n");
	sbSql.append("                    AND TDAS.IS_RETURN = 10041002     AND TO_CHAR(TDAS.SALES_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd')\n");
	sbSql.append("             GROUP BY TV.MODEL_ID ),--当日最终销售\n");
	sbSql.append("BORROW AS  (SELECT TV.MODEL_ID,COUNT(1) AMOUNT\n");
	sbSql.append("            FROM   TM_VEHICLE TV WHERE TV.LOCK_STATUS = 10241007\n");
	sbSql.append("                  AND TV.ORG_STORAGE_DATE <= SYSDATE\n");
	sbSql.append("            GROUP BY TV.MODEL_ID),--外借\n");
	sbSql.append("F_SELECT AS (\n");
	sbSql.append("SELECT VMG2.GROUP_NAME SERIES_NAME,\n");
	sbSql.append("       VMG.GROUP_CODE GROUP_NAME,TO_CHAR(VMG.GROUP_ID) GROUP_ID,\n");
	sbSql.append("       NVL(Y.AMOUNT,0) Y_AMOUNT,  --当年入库\n");
	sbSql.append("       NVL(M.AMOUNT,0) M_AMOUNT, --当月入库\n");
	sbSql.append("       NVL(D.AMOUNT,0) D_AMOUNT, --当日入库\n");
	sbSql.append("       NVL(YF.AMOUNT,0)  YF_AMOUNT, --当年开票\n");
	sbSql.append("       NVL(MF.AMOUNT,0)  MF_AMOUNT, --当月开票\n");
	sbSql.append("       NVL(DF.AMOUNT,0)  DF_AMOUNT, --当日开票\n");
	sbSql.append("       NVL(SV.AMOUNT,0)  SV_AMOUNT,  --可发车\n");
	sbSql.append("       NVL(YS.AMOUNT,0)  YS_AMOUNT, --当年最终销售\n");
	sbSql.append("       NVL(MS.AMOUNT,0)  MS_AMOUNT, --当月最终销售\n");
	sbSql.append("       NVL(DS.AMOUNT,0)  DS_AMOUNT,  --当日最终销售\n");
	sbSql.append("       NVL(B.AMOUNT,0)   B_AMOUNT, --外借\n");
	sbSql.append("       NVL(SV.AMOUNT,0)  STORAGE_AMOUNT --库存合计（没加中转库待中转库确认）\n");
	sbSql.append("FROM  TM_VHCL_MATERIAL_GROUP VMG,\n");
	sbSql.append("      YEAR_STORAGE Y,\n");
	sbSql.append("      MONTH_STORAGE M,\n");
	sbSql.append("      DAY_STORAGE D,\n");
	sbSql.append("      YEAR_F_CHECK YF,\n");
	sbSql.append("      MONTH_F_CHECK MF,\n");
	sbSql.append("      DAY_F_CHECK DF,\n");
	sbSql.append("      S_VEHICLE SV,\n");
	sbSql.append("      Y_SALES YS,\n");
	sbSql.append("      M_SALES MS,\n");
	sbSql.append("      D_SALES DS,\n");
	sbSql.append("      BORROW  B,\n");
	sbSql.append("      TM_AREA_GROUP AG,\n");
	sbSql.append("      TM_AREA_GROUP TAG,\n");
	sbSql.append("      TM_VHCL_MATERIAL_GROUP VMG2\n");
	sbSql.append("WHERE Y.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   M.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   D.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   YF.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   MF.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   DF.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   SV.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   YS.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   MS.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   DS.MODEL_ID(+) = VMG.GROUP_ID\n");
	sbSql.append("AND   B.MODEL_ID(+)  = VMG.GROUP_ID\n");
	sbSql.append("AND   VMG.GROUP_LEVEL = 3\n");
	sbSql.append("AND   VMG.PARENT_GROUP_ID = VMG2.GROUP_ID\n");
	sbSql.append("AND   VMG2.GROUP_ID = AG.MATERIAL_GROUP_ID\n");
	sbSql.append("AND   TAG.MATERIAL_GROUP_ID = VMG2.GROUP_ID\n");
	sbSql.append("ORDER BY VMG2.GROUP_NAME,VMG.GROUP_NAME)\n");
	sbSql.append("SELECT SERIES_NAME ,GROUP_NAME ,GROUP_ID ,Y_AMOUNT ,M_AMOUNT ,D_AMOUNT ,YF_AMOUNT ,MF_AMOUNT ,DF_AMOUNT ,SV_AMOUNT ,YS_AMOUNT ,MS_AMOUNT ,DS_AMOUNT ,B_AMOUNT ,STORAGE_AMOUNT\n");
	sbSql.append("FROM\n");
	sbSql.append("(SELECT * FROM F_SELECT\n");
	sbSql.append("UNION ALL\n");
	sbSql.append("SELECT F.SERIES_NAME,\n");
	sbSql.append("       F.SERIES_NAME||'小计' GROUP_NAME,'' GROUP_ID,\n");
	sbSql.append("       NVL(SUM(F.Y_AMOUNT),0) Y_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.M_AMOUNT),0) M_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.D_AMOUNT),0) D_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.YF_AMOUNT),0) YF_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.MF_AMOUNT),0) MF_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.DF_AMOUNT),0) DF_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.SV_AMOUNT),0) SV_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.YS_AMOUNT),0) YS_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.MS_AMOUNT),0) MS_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.DS_AMOUNT),0) DS_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.B_AMOUNT),0) B_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.STORAGE_AMOUNT),0) STORAGE_AMOUNT\n");
	sbSql.append("FROM F_SELECT F\n");
	sbSql.append("GROUP BY F.SERIES_NAME\n");
	sbSql.append("UNION ALL\n");
	sbSql.append("SELECT 'Z' SERIES_NAME,\n");
	sbSql.append("       '内销合计' GROUP_NAME,'' GROUP_ID,\n");
	sbSql.append("       NVL(SUM(F.Y_AMOUNT),0) Y_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.M_AMOUNT),0) M_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.D_AMOUNT),0) D_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.YF_AMOUNT),0) YF_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.MF_AMOUNT),0) MF_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.DF_AMOUNT),0) DF_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.SV_AMOUNT),0) SV_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.YS_AMOUNT),0) YS_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.MS_AMOUNT),0) MS_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.DS_AMOUNT),0) DS_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.B_AMOUNT),0) B_AMOUNT,\n");
	sbSql.append("       NVL(SUM(F.STORAGE_AMOUNT),0) STORAGE_AMOUNT\n");
	sbSql.append("FROM F_SELECT F)\n");
	sbSql.append("ORDER BY SERIES_NAME DESC,GROUP_ID)\n");
	sbSql.append("where Y_AMOUNT<>0 or M_AMOUNT<>0 or M_AMOUNT<>0 or YF_AMOUNT<>0 or MF_AMOUNT<>0 or DF_AMOUNT<>0 or SV_AMOUNT<>0 or YS_AMOUNT<>0 or MS_AMOUNT<>0 or DS_AMOUNT<>0 or B_AMOUNT<>0 or STORAGE_AMOUNT<>0"); 
	return pageQuery(sbSql.toString(), null, getFunName());
}

	public List<Map<String, Object>> getProductSaleReportInfoNew(Map<String, Object> map,Integer pageSize,Integer curPage){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("       WITH  IN_STORAGE  AS (  SELECT SUM(Y_COUNT) Y_COUNT,\n");
		sbSql.append("                  SUM(M_COUNT) M_COUNT,\n");
		sbSql.append("                  SUM(D_COUNT) D_COUNT,\n");
		sbSql.append("                  VEHICLE_MODEL_ID,\n");
		sbSql.append("                  VEHICLE_MODEL_NAME   FROM\n");
		sbSql.append("          (  SELECT CASE WHEN TO_CHAR(TV.ORG_STORAGE_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy') THEN 1\n");
		sbSql.append("                        ELSE 0 END Y_COUNT,\n");
		sbSql.append("                   CASE WHEN TO_CHAR(TV.ORG_STORAGE_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM') THEN 1\n");
		sbSql.append("                        ELSE 0 END M_COUNT ,\n");
		sbSql.append("                   CASE WHEN TO_CHAR(TV.ORG_STORAGE_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd') THEN 1\n");
		sbSql.append("                        ELSE 0 END D_COUNT,\n");
		sbSql.append("                   VM.VEHICLE_MODEL_ID,VM.VEHICLE_MODEL_NAME\n");
		sbSql.append("  FROM TM_VEHICLE TV, TM_VEHICLE_MODEL VM, TR_VEHICLE_MODEL_PACKAGE VMP ,VW_MATERIAL_GROUP_MAT MAT\n");
		sbSql.append(" WHERE TV.PROV_ID NOT IN (2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
		sbSql.append("   AND TV.MATERIAL_ID = MAT.MATERIAL_ID AND MAT.PACKAGE_ID = VMP.GROUP_ID AND VMP.VEHICLE_MODEL_ID = VM.VEHICLE_MODEL_ID\n");
		sbSql.append("   AND TO_CHAR(TV.ORG_STORAGE_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy') )\n");
		sbSql.append("   GROUP BY VEHICLE_MODEL_ID,VEHICLE_MODEL_NAME ), ---入库\n");
		sbSql.append("\n");
		sbSql.append("   FIN_CHECK AS (\n");
		sbSql.append("                             SELECT VEHICLE_MODEL_ID,\n");
		sbSql.append("                  SUM(Y_CHECK) Y_CHECK,\n");
		sbSql.append("                  SUM(M_CHECK) M_CHECK,\n");
		sbSql.append("                  SUM(D_CHECK) D_CHECK  FROM\n");
		sbSql.append("           (  SELECT  VM.VEHICLE_MODEL_ID,\n");
		sbSql.append("                     CASE WHEN TO_CHAR(TVO.INVO_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy') THEN NVL(TVOD.CHECK_AMOUNT,0) ELSE 0 END Y_CHECK,\n");
		sbSql.append("                     CASE WHEN TO_CHAR(TVO.INVO_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM') THEN NVL(TVOD.CHECK_AMOUNT,0)ELSE 0 END M_CHECK,\n");
		sbSql.append("                     CASE WHEN TO_CHAR(TVO.INVO_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd') THEN NVL(TVOD.CHECK_AMOUNT,0) ELSE 0 END D_CHECK\n");
		sbSql.append("               FROM  TT_VS_ORDER TVO,\n");
		sbSql.append("                     TT_VS_ORDER_DETAIL TVOD,\n");
		sbSql.append("                     TM_DEALER TD,\n");
		sbSql.append("                     TM_DEALER_ORG_RELATION TDOR,\n");
		sbSql.append("                     TM_ORG ORG,\n");
		sbSql.append("                     VW_MATERIAL_GROUP_MAT MAT,\n");
		sbSql.append("                     TM_VEHICLE_MODEL VM,\n");
		sbSql.append("                     TR_VEHICLE_MODEL_PACKAGE VMP\n");
		sbSql.append("              WHERE  TVO.INVOICE_NO IS NOT NULL AND TVO.INVO_DATE IS NOT NULL\n");
		sbSql.append("                AND  TVO.ORDER_STATUS IN (10211007,10211008,10211009,10211011,10211012)\n");
		sbSql.append("                AND  TVOD.ORDER_ID = TVO.ORDER_ID\n");
		sbSql.append("                AND  TVOD.MATERIAL_ID = MAT.MATERIAL_ID\n");
		sbSql.append("                AND  TVO.DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("                AND ( TD.DEALER_ID = TDOR.DEALER_ID OR TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
		sbSql.append("                AND  TDOR.ORG_ID = ORG.ORG_ID\n");
		sbSql.append("                AND  MAT.PACKAGE_ID = VMP.GROUP_ID\n");
		sbSql.append("                AND  VMP.VEHICLE_MODEL_ID = VM.VEHICLE_MODEL_ID\n");
		sbSql.append("                AND  ORG.ORG_ID NOT IN (2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
		sbSql.append("                AND  TO_CHAR(TVO.INVO_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy')\n");
		sbSql.append("                UNION ALL\n");
		sbSql.append("              SELECT  VM.VEHICLE_MODEL_ID,\n");
		sbSql.append("                      CASE WHEN TO_CHAR(TVO.INVO_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy') THEN NVL(TVOD.OUT_AMOUNT,0) ELSE 0 END Y_CHECK,\n");
		sbSql.append("                      CASE WHEN TO_CHAR(TVO.INVO_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM') THEN NVL(TVOD.OUT_AMOUNT,0)ELSE 0 END M_CHECK,\n");
		sbSql.append("                      CASE WHEN TO_CHAR(TVO.INVO_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd') THEN NVL(TVOD.OUT_AMOUNT,0) ELSE 0 END D_CHECK\n");
		sbSql.append("                FROM   TT_OUT_ORDER_DETAIL TVOD,\n");
		sbSql.append("                       TT_OUT_ORDER TVO,\n");
		sbSql.append("                       VW_MATERIAL_GROUP_MAT MAT,\n");
		sbSql.append("                       TM_DEALER TD,\n");
		sbSql.append("                       TM_DEALER_ORG_RELATION TDOR,\n");
		sbSql.append("                       TM_ORG ORG,\n");
		sbSql.append("                       TM_VEHICLE_MODEL VM,\n");
		sbSql.append("                       TR_VEHICLE_MODEL_PACKAGE VMP\n");
		sbSql.append("                WHERE  TVO.INVOICE_NO IS NOT NULL AND TVO.INVO_DATE IS NOT NULL\n");
		/** wangsw 修改中转出库开票状态 ------start*/
		sbSql.append("                AND    TVO.ORDER_STATUS = 13741006\n");
		/** wangsw 修改中转出库开票状态  ------end*/
		sbSql.append("                AND    TVOD.ORDER_ID = TVO.ORDER_ID\n");
		sbSql.append("                AND    TVOD.MATERIAL_ID = MAT.MATERIAL_ID\n");
		sbSql.append("                AND    TVO.DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("                AND    (TD.DEALER_ID = TDOR.DEALER_ID OR TD.PARENT_DEALER_D = TDOR.DEALER_ID)\n");
		sbSql.append("                AND    TDOR.ORG_ID = ORG.ORG_ID\n");
		sbSql.append("                AND    MAT.PACKAGE_ID = VMP.GROUP_ID\n");
		sbSql.append("                AND    VMP.VEHICLE_MODEL_ID = VM.VEHICLE_MODEL_ID\n");
		sbSql.append("                AND    ORG.ORG_ID NOT IN (2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
		sbSql.append("                AND  TO_CHAR(TVO.INVO_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy')\n");
		sbSql.append("                UNION ALL\n");
		sbSql.append("                SELECT VM.VEHICLE_MODEL_ID,\n");
		sbSql.append("                       CASE WHEN TO_CHAR(TVO.INVO_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy') THEN TO_NUMBER(NVL(TVO.FBILLWAY,0))*TO_NUMBER(NVL(TVO.F_AMOUNT,0)) ELSE 0 END Y_CHECK,\n");
		sbSql.append("                       CASE WHEN TO_CHAR(TVO.INVO_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM') THEN TO_NUMBER(NVL(TVO.FBILLWAY,0))*TO_NUMBER(NVL(TVO.F_AMOUNT,0)) ELSE 0 END M_CHECK,\n");
		sbSql.append("                       CASE WHEN TO_CHAR(TVO.INVO_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd') THEN TO_NUMBER(NVL(TVO.FBILLWAY,0))*TO_NUMBER(NVL(TVO.F_AMOUNT,0)) ELSE 0 END D_CHECK\n");
		sbSql.append("                  FROM TT_OLD_ORDER TVO , TM_VEHICLE_MODEL VM, TR_VEHICLE_MODEL_PACKAGE VMP\n");
		sbSql.append("                 WHERE TVO.FLAG IN (0,2,4)\n");
		sbSql.append("                   AND TVO.INVO_DATE IS NOT NULL\n");
		sbSql.append("                   AND TVO.PACKAGE_ID = VMP.GROUP_ID\n");
		sbSql.append("                   AND VMP.VEHICLE_MODEL_ID = VM.VEHICLE_MODEL_ID\n");
		sbSql.append("                   AND TVO.PROV_ID NOT IN (2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
		sbSql.append("                   AND TO_CHAR(TVO.INVO_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy')\n");
		sbSql.append("                   AND TVO.PACKAGE_ID IS NOT NULL ) GROUP BY VEHICLE_MODEL_ID ), -- 开票\n");
		sbSql.append("\n");
		sbSql.append("STORAGE_VEHICEL AS (\n");
		sbSql.append("                SELECT    VM.VEHICLE_MODEL_ID,\n");
		sbSql.append("                     SUM(1) S_AMOUNT\n");
		sbSql.append("             FROM    TM_VEHICLE TV , TM_VEHICLE_MODEL VM, TR_VEHICLE_MODEL_PACKAGE VMP\n");
		sbSql.append("             WHERE   TV.LOCK_STATUS IN (10241001)\n");
		sbSql.append("             AND     TV.PROV_ID NOT IN (2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
		sbSql.append("             AND     TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("             AND     TV.PACKAGE_ID = VMP.GROUP_ID\n");
		sbSql.append("             AND     VMP.VEHICLE_MODEL_ID = VM.VEHICLE_MODEL_ID\n");
		sbSql.append("             GROUP BY VM.VEHICLE_MODEL_ID ),--可发车\n");
		sbSql.append(" SALES_VEHICLE AS (\n");
		sbSql.append("                     SELECT VEHICLE_MODEL_ID,\n");
		sbSql.append("               SUM(YS_AMOUNT) YS_AMOUNT,\n");
		sbSql.append("               SUM(MS_AMOUNT) MS_AMOUNT,\n");
		sbSql.append("               SUM(DS_AMOUNT) DS_AMOUNT  FROM\n");
		sbSql.append("        ( SELECT VM.VEHICLE_MODEL_ID,\n");
		sbSql.append("                CASE WHEN TO_CHAR(TDAS.SALES_DATE,'yyyy') = TO_CHAR(SYSDATE,'yyyy') THEN 1 ELSE 0 END YS_AMOUNT,\n");
		sbSql.append("                CASE WHEN TO_CHAR(TDAS.SALES_DATE,'yyyy-MM') = TO_CHAR(SYSDATE,'yyyy-MM') THEN 1 ELSE 0 END MS_AMOUNT,\n");
		sbSql.append("                CASE WHEN TO_CHAR(TDAS.SALES_DATE,'yyyy-MM-dd') = TO_CHAR(SYSDATE,'yyyy-MM-dd') THEN 1 ELSE 0 END DS_AMOUNT\n");
		sbSql.append("          FROM TT_DEALER_ACTUAL_SALES TDAS , TM_VEHICLE TV,\n");
		sbSql.append("               TM_VEHICLE_MODEL VM , TR_VEHICLE_MODEL_PACKAGE VMP\n");
		sbSql.append("         WHERE TO_CHAR(SALES_DATE,'YYYY')=TO_CHAR(SYSDATE,'yyyy')\n");
		sbSql.append("           AND TDAS.VEHICLE_ID = TV.VEHICLE_ID AND TDAS.IS_RETURN = 10041002\n");
		sbSql.append("           AND TV.PACKAGE_ID = VMP.GROUP_ID\n");
		sbSql.append("           AND VMP.VEHICLE_MODEL_ID = VM.VEHICLE_MODEL_ID )\n");
		sbSql.append("           GROUP BY VEHICLE_MODEL_ID ),--实销\n");
		sbSql.append("  BORROW_VEHICEL AS (\n");
		sbSql.append("                 SELECT VM.VEHICLE_MODEL_ID,\n");
		sbSql.append("            COUNT(1) AMOUNT\n");
		sbSql.append("       FROM TM_VEHICLE TV , TM_VEHICLE_MODEL VM , TR_VEHICLE_MODEL_PACKAGE VMP\n");
		sbSql.append("      WHERE TV.LOCK_STATUS = 10241007\n");
		sbSql.append("        AND TV.PACKAGE_ID = VMP.GROUP_ID\n");
		sbSql.append("        AND VMP.VEHICLE_MODEL_ID = VM.VEHICLE_MODEL_ID\n");
		sbSql.append("       GROUP BY VM.VEHICLE_MODEL_ID ) --外借\n");
		sbSql.append("\n");
		sbSql.append("SELECT  VMG.GROUP_NAME,NVL(VM.VEHICLE_MODEL_NAME,'') COMMAND,\n");
//		sbSql.append("        DECODE(VM.VEHICLE_MODEL_NAME,null,VMG.GROUP_NAME||'合计',VM.VEHICLE_MODEL_NAME) VEHICLE_MODEL_NAME,\n");
		sbSql.append("CASE WHEN VM.VEHICLE_MODEL_NAME IS NULL AND VMG.GROUP_NAME IS NOT NULL THEN VMG.GROUP_NAME||'小计'\n");
		sbSql.append("             WHEN VM.VEHICLE_MODEL_NAME IS NULL AND VMG.GROUP_NAME IS NULL THEN '总计'\n");
		sbSql.append("             ELSE VM.VEHICLE_MODEL_NAME END VEHICLE_MODEL_NAME,"); 
		sbSql.append("		  NVL(SUM(A.D_COUNT),0) D_COUNT, --当日入库\n");
		sbSql.append("        NVL(SUM(A.M_COUNT),0) M_COUNT, --当月入库\n");
		sbSql.append("        NVL(SUM(A.Y_COUNT),0) Y_COUNT, --当年入库\n");
		sbSql.append("        NVL(SUM(B.D_CHECK),0) D_CHECK,  --当日开票\n");
		sbSql.append("        NVL(SUM(B.M_CHECK),0) M_CHECK,  --当月开票\n");
		sbSql.append("        NVL(SUM(B.Y_CHECK),0) Y_CHECK,  --当年开票\n");
		sbSql.append("        NVL(SUM(D.DS_AMOUNT),0) DS_AMOUNT, --当日实销\n");
		sbSql.append("        NVL(SUM(D.MS_AMOUNT),0) MS_AMOUNT, --当月实销\n");
		sbSql.append("        NVL(SUM(D.YS_AMOUNT),0) YS_AMOUNT, --当年实销\n");
		sbSql.append("        NVL(SUM(C.S_AMOUNT),0)  S_AMOUNT,  --可发车\n");
		sbSql.append("        NVL(SUM(E.AMOUNT),0) BORROW,    --外借\n");
		sbSql.append("        NVL(SUM(C.S_AMOUNT),0) STORAGE_AMOUNT"); 
		sbSql.append("  FROM  TM_VEHICLE_MODEL VM,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
		sbSql.append("        IN_STORAGE A, --入库\n");
		sbSql.append("        FIN_CHECK  B, --开票\n");
		sbSql.append("        STORAGE_VEHICEL C, --可发车\n");
		sbSql.append("        SALES_VEHICLE D, --实销\n");
		sbSql.append("        BORROW_VEHICEL E--外借\n");
		sbSql.append("  WHERE VM.VEHICLE_MODEL_ID = A.VEHICLE_MODEL_ID(+)\n");
		sbSql.append("    AND VM.VEHICLE_MODEL_ID = B.VEHICLE_MODEL_ID(+)\n");
		sbSql.append("    AND VM.VEHICLE_MODEL_ID = C.VEHICLE_MODEL_ID(+)\n");
		sbSql.append("    AND VM.VEHICLE_MODEL_ID = D.VEHIClE_MODEL_ID(+)\n");
		sbSql.append("    AND VM.VEHICLE_MODEL_ID = E.VEHICLE_MODEL_ID(+)\n");
		sbSql.append("    AND VM.SERIES_ID = VMG.GROUP_ID\n");
		sbSql.append("    GROUP BY ROLLUP(VMG.GROUP_NAME,VM.VEHICLE_MODEL_NAME) ORDER BY GROUP_NAME , COMMAND"); 
		return pageQuery(sbSql.toString(), null, getFunName());
	}

	
	public List<Map<String, Object>> getHistoryProductSalseInfo(String chooseDate){
		String sql = "SELECT * FROM  TEMP_PRODUCT_SALES_REPORT WHERE TO_CHAR(REPORT_DATE,'yyyy-MM-dd') = '"+chooseDate+"' order by row_num asc"; 
	    return pageQuery(sql, null, getFunName());
	}
	
	public List<Map<String, Object>> getHistoryProductSalseInfo1(String chooseDate){
		String sql = "select t.series_name,t.group_id,t.* from temp_prodct_sales t where to_char(t.report_date,'yyyy-MM-dd') = '"+chooseDate+"' ORDER BY t.SERIES_NAME DESC,t.group_id"; 
	    return pageQuery(sql, null, getFunName());
	}
}
