package com.infodms.dms.dao.report;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import common.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class RebateReportDao extends BaseDao {
    public static Logger logger = Logger.getLogger(RebateReportDao.class);
    private static final RebateReportDao dao = new RebateReportDao();

    private RebateReportDao() {

    }

    public static final RebateReportDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }


    public PageResult<Map<String, Object>> query(
            RequestWrapper request, Integer curPage, Integer pageSize, String flag, AclUserBean logonUser) throws Exception {
        try {
            String startDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
            String endDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
            String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); //大区
            String query = CommonUtils.checkNull(request.getParamValue("Query")); //明细和汇总查询标记
            String year = CommonUtils.checkNull(request.getParamValue("year")); //明细查询年月

            StringBuffer sql = new StringBuffer();
            //汇总查询
            if ("1".equals(query)) {
                sql.append("WITH PART_ORDER_SUMBMIT AS\n");
                sql.append(" (SELECT A.DEALER_ID，SUM(ORDER_AMOUNT) PART_AMOUNT,\n");
                sql.append("         SUM(JJ_AMOUNT) JJ_AMOUNT,\n");
                sql.append("         SUM(CG_AMOUNT) CG_AMOUNT,\n");
                sql.append("         SUM(TS_AMOUNT) TS_AMOUNT,\n");
                sql.append("         SUM(QH_AMOUNT) QH_AMOUNT\n");
                sql.append("    FROM (SELECT OM.DEALER_ID,\n");
                sql.append("                 SUM(OM.ORDER_AMOUNT) ORDER_AMOUNT, --订单总金额\n");
                sql.append("                 SUM(DECODE(OM.ORDER_TYPE, 92151001, OM.ORDER_AMOUNT，0)) JJ_AMOUNT， --紧急订单金额\n");
                sql.append("                 SUM(DECODE(OM.ORDER_TYPE, 92151002, OM.ORDER_AMOUNT，0)) CG_AMOUNT, --常规订单金额\n");
                sql.append("                 SUM(DECODE(OM.ORDER_TYPE, 92151003, OM.ORDER_AMOUNT，0)) TS_AMOUNT, --特殊订单金额\n");
                sql.append("                 SUM(DECODE(OM.ORDER_TYPE, 92151010, OM.ORDER_AMOUNT，0)) QH_AMOUNT --切换订单金额\n");
                sql.append("            FROM VW_PART_DLR_ORDER_TRANS OM\n");
                sql.append("           WHERE OM.ORDER_TYPE IN (92151001, 92151002, 92151003, 92151010)\n");
//                    sql.append("             AND OM.STATE IN (92161002, 92161003, 92161013, 92161014)\n");
                sql.append("             AND OM.SELLER_ID = 2010010100070674\n");
                if (!"".equals(startDate) && null != startDate) {
                    sql.append("             AND TRUNC(OM.SUBMIT_DATE) >= TO_DATE('" + startDate + "', 'yyyy-mm-dd') --订单提报日期\n");
                }
                if (!"".equals(endDate) && null != endDate) {
                    sql.append("             AND TRUNC(OM.SUBMIT_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd') --订单提报日期\n");
                }
                sql.append("           GROUP BY OM.DEALER_ID ) A\n");
                sql.append("   GROUP BY A.DEALER_ID),\n");
                sql.append("PART_DLR_RETURN AS\n");
                sql.append(" (SELECT RM.DEALER_ID, SUM(RR.RETURN_QTY * RD.BUY_PRICE) RETURN_AMOUNT --退货金额\n");
                sql.append("    FROM TT_PART_DLR_RETURN_DTL  RD,\n");
                sql.append("         TT_PART_DLR_RETURN_MAIN RM,\n");
                sql.append("         TT_PART_RETURN_RECORD   RR\n");
                sql.append("   WHERE RD.RETURN_ID = RM.RETURN_ID\n");
                sql.append("     AND RM.RETURN_ID = RR.RETURN_ID\n");
                sql.append("     AND RR.PART_ID = RD.PART_ID\n");
                sql.append("     AND RM.SELLER_ID = 2010010100070674\n");
                if (!"".equals(startDate) && null != startDate) {
                    sql.append("     AND TRUNC(RR.CREATE_DATE) >= TO_DATE('" + startDate + "', 'yyyy-mm-dd')\n");
                }
                if (!"".equals(endDate) && null != endDate) {
                    sql.append("     AND TRUNC(RR.CREATE_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd')\n");
                }
                sql.append("   GROUP BY RM.DEALER_ID),\n");
                sql.append("AS_PART AS\n");
                sql.append(" (SELECT W.DEALER_ID, SUM(NVL(T.BALANCE_PART_AMOUNT, 0)) AS CL_AMOUNT\n");
                sql.append("    FROM TT_AS_WR_APPLICATION_FIXED T, TM_DEALER W\n");
                sql.append("   WHERE W.DEALER_ID = T.DEALER_ID\n");
                sql.append("     AND T.CLAIM_TYPE IN\n");
                sql.append("         (10661001, 10661006, 10661007, 10661012, 10661010, 10661009)\n");
                sql.append("     AND T.STATUS >= 10791007\n");
                sql.append("     AND T.IS_IMPORT = 10041002\n");
                if (!"".equals(startDate) && null != startDate) {
                    sql.append("     AND TRUNC(T.REPORT_DATE) >= TO_DATE('" + startDate + "', 'yyyy-mm-dd')\n");
                }
                if (!"".equals(endDate) && null != endDate) {
                    sql.append("     AND TRUNC(T.REPORT_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd')\n");
                }
                sql.append("   GROUP BY W.DEALER_ID),\n");
                sql.append("PART_SP_AMOUNT AS\n");
                sql.append(" (SELECT H.CHILDORG_ID DEALER_ID, SUM(H.AMOUNT) AMOUNT\n");
                sql.append("    FROM TT_PART_ACCOUNT_IMPORT_HISTORY H\n");
                sql.append("   WHERE H.STATUS < 0\n");
                if (!"".equals(startDate) && null != startDate) {
                    sql.append("     AND TRUNC(H.DK_DATE) >= TO_DATE('" + startDate + "', 'yyyy-mm-dd') --首批登记日期\n");
                }
                if (!"".equals(endDate) && null != endDate) {
                    sql.append("     AND TRUNC(H.DK_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd') --首批登记日期\n");
                }
                sql.append("   GROUP BY H.CHILDORG_ID),\n");
                sql.append("PART_DLR_TASK AS\n");
                sql.append(" (SELECT TD.DEALER_ID, SUM(TD.AMOUNT) TASK_AMOUNT\n");
                sql.append("    FROM TT_PART_DLR_TASK_MONTH_DTL TD, TT_PART_DLR_MONTH_TASK MT\n");
                sql.append("   WHERE TD.TASK_ID = MT.TASK_ID\n");
                sql.append("     AND MT.STATUS = 10011001\n");
                sql.append("     AND (TD.DEALER_ID = 2010010100070674 OR EXISTS\n");
                sql.append("          (SELECT 1\n");
                sql.append("             FROM TT_PART_SALES_RELATION R\n");
                sql.append("            WHERE R.CHILDORG_ID = TD.DEALER_ID\n");
                sql.append("              AND R.PARENTORG_ID = 2010010100070674))\n");
                sql.append("     AND MT.TASK_MONTH >= '" + startDate.substring(0, 7) + "' --任务起始日期\n");
                sql.append("     AND MT.TASK_MONTH <= '" + endDate.substring(0, 7) + "' --任务结束日期\n");
                sql.append("  GROUP BY TD.DEALER_ID )\n");
                if ("1".equals(flag)) {
                    sql.append("SELECT TD.DEALER_CODE,\n");
                    sql.append("       TD.DEALER_NAME,\n");
                    sql.append("       TO_CHAR(NVL(PDT.TASK_AMOUNT，0), 'fm999,999,999,990.00') TASK_AMOUNT, --任务金额\n");
                    sql.append("       TO_CHAR(NVL(PO.PART_AMOUNT, 0), 'fm999,999,999,990.00') PART_AMOUNT, --提报金额\n");
                    sql.append("       TO_CHAR(NVL(PDL.RETURN_AMOUNT，0), 'fm999,999,999,990.00') RETURN_AMOUNT, --退货金额\n");
                    sql.append("       TO_CHAR(NVL(PO.QH_AMOUNT，0), 'fm999,999,999,990.00') QH_AMOUNT, --切换金额\n");
                    sql.append("       TO_CHAR(NVL(PO.TS_AMOUNT，0), 'fm999,999,999,990.00') TS_AMOUNT, --特殊金额\n");
                    sql.append("       TO_CHAR(NVL(PSA.AMOUNT, 0), 'fm999,999,999,990.00') SP_AMOUNT, --首批金额\n");
                    sql.append("       TO_CHAR(NVL(AP.CL_AMOUNT, 0), 'fm999,999,999,990.00') CL_AMOUNT, --材料金额\n");
                    sql.append("       TO_CHAR((NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("               NVL(PDL.RETURN_AMOUNT，0)),\n");
                    sql.append("               'fm999,999,999,990.00') CP_AMOUNT, --完成金额\n");
                    sql.append("       CASE\n");
                    sql.append("         WHEN NVL(PDT.TASK_AMOUNT，0) > 0 AND\n");
                    sql.append("              (NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("               NVL(PDL.RETURN_AMOUNT，0)) > 0 THEN\n");
                    sql.append("          TO_CHAR(ROUND((NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("                        NVL(PDL.RETURN_AMOUNT，0)) / NVL(PDT.TASK_AMOUNT，0) * 100,\n");
                    sql.append("                        2),\n");
                    sql.append("                  'fm999.99') || '%'\n");
                    sql.append("         ELSE\n");
                    sql.append("          '-'\n");
                    sql.append("       END CP_RATIO, --完成率\n");
                    sql.append("       TO_CHAR(ROUND(CASE\n");
                    sql.append("                       WHEN (NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("                            NVL(PDL.RETURN_AMOUNT，0) - NVL(PDT.TASK_AMOUNT，0)) > 0 THEN\n");
                    sql.append("                        NVL(PDT.TASK_AMOUNT，0) * 0.01 +\n");
                    sql.append("                        (NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("                         NVL(PDL.RETURN_AMOUNT，0) - NVL(PDT.TASK_AMOUNT，0)) * 0.01\n");
                    sql.append("                       ELSE\n");
                    sql.append("                        0\n");
                    sql.append("                     END,\n");
                    sql.append("                     2),\n");
                    sql.append("               'fm999,999,999,990.00') FL_AMOUNT --返利金额\n");
                } else {
                    sql.append("SELECT TO_CHAR(SUM(NVL(PDT.TASK_AMOUNT，0)), 'fm999,999,999,990.00') TASK_AMOUNT, --任务金额\n");
                    sql.append("       TO_CHAR(SUM(NVL(PO.PART_AMOUNT, 0)), 'fm999,999,999,990.00') PART_AMOUNT, --提报金额\n");
                    sql.append("       TO_CHAR(SUM(NVL(PDL.RETURN_AMOUNT，0)), 'fm999,999,999,990.00') RETURN_AMOUNT, --退货金额\n");
                    sql.append("       TO_CHAR(SUM(NVL(PO.QH_AMOUNT，0)), 'fm999,999,999,990.00') QH_AMOUNT, --切换金额\n");
                    sql.append("       TO_CHAR(SUM(NVL(PO.TS_AMOUNT，0)), 'fm999,999,999,990.00') TS_AMOUNT, --特殊金额\n");
                    sql.append("       TO_CHAR(SUM(NVL(PSA.AMOUNT, 0)), 'fm999,999,999,990.00') SP_AMOUNT, --首批金额\n");
                    sql.append("       TO_CHAR(SUM(NVL(AP.CL_AMOUNT, 0)), 'fm999,999,999,990.00') CL_AMOUNT, --材料金额\n");
                    sql.append("       TO_CHAR(SUM(NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("                   NVL(PDL.RETURN_AMOUNT，0)),\n");
                    sql.append("               'fm999,999,999,990.00') CP_AMOUNT, --完成金额\n");
                    sql.append("       CASE\n");
                    sql.append("         WHEN SUM(NVL(PDT.TASK_AMOUNT，0)) > 0 AND\n");
                    sql.append("              SUM(NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("                  NVL(PDL.RETURN_AMOUNT，0)) > 0 THEN\n");
                    sql.append("          TO_CHAR(ROUND(SUM(NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("                            NVL(PDL.RETURN_AMOUNT，0)) /\n");
                    sql.append("                        SUM(NVL(PDT.TASK_AMOUNT，0)) * 100,\n");
                    sql.append("                        2),\n");
                    sql.append("                  'fm999.99') || '%'\n");
                    sql.append("         ELSE\n");
                    sql.append("          '-'\n");
                    sql.append("       END CP_RATIO, --完成率\n");
                    sql.append("       TO_CHAR(ROUND(SUM(CASE\n");
                    sql.append("                           WHEN (NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("                                NVL(PDL.RETURN_AMOUNT，0) - NVL(PDT.TASK_AMOUNT，0)) > 0 THEN\n");
                    sql.append("                            NVL(PDT.TASK_AMOUNT，0) * 0.01 +\n");
                    sql.append("                            (NVL(PO.PART_AMOUNT, 0) - NVL(PO.QH_AMOUNT，0) -\n");
                    sql.append("                             NVL(PDL.RETURN_AMOUNT，0) - NVL(PDT.TASK_AMOUNT，0)) * 0.01\n");
                    sql.append("                           ELSE\n");
                    sql.append("                            0\n");
                    sql.append("                         END),\n");
                    sql.append("                     2),\n");
                    sql.append("               'fm999,999,999,990.00') FL_AMOUNT --返利金额\n");
                }
                sql.append(" FROM TM_DEALER          TD,\n");
                sql.append("      PART_ORDER_SUMBMIT PO,\n");
                sql.append("      PART_DLR_RETURN    PDL,\n");
                sql.append("      AS_PART            AP,\n");
                sql.append("      PART_SP_AMOUNT     PSA,\n");
                sql.append("      PART_DLR_TASK      PDT\n");
                sql.append("WHERE 1 = 1\n");
                sql.append("  AND TD.DEALER_ID = PO.DEALER_ID(+)\n");
                sql.append("  AND TD.DEALER_ID = PDL.DEALER_ID(+)\n");
                sql.append("  AND TD.DEALER_ID = AP.DEALER_ID(+)\n");
                sql.append("  AND TD.DEALER_ID = PSA.DEALER_ID(+)\n");
                sql.append("  AND TD.DEALER_ID = PDT.DEALER_ID(+)\n");
                sql.append("  AND TD.DEALER_TYPE = 10771002\n");
                sql.append("  AND TD.DEALER_LEVEL = 10851001\n");
//                    sql.append("   AND TD.SERVICE_STATUS = 13691002\n");
                if (!"".equals(dealerCode) && null != dealerCode) {
                    if (!"".equals(dealerCode)) {
                        if (dealerCode.split(",").length > 1) {
                            String[] dcs = dealerCode.split(",");
                            StringBuffer tempsql = new StringBuffer();
                            for (String dc : dcs) {
                                tempsql.append("'" + dc + "',");
                            }
                            tempsql.deleteCharAt(tempsql.length() - 1);
                            sql.append(" AND TD.DEALER_CODE IN (" + tempsql + ")\n");
                        } else {
                            sql.append(" AND UPPER(TD.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%'\n");
                        }

                    }
                }
                if (!"".equals(dealerName) && null != dealerName) {
                    sql.append("  AND TD.DEALER_NAME LIKE '%" + dealerName + "%'\n");
                }

                //增加大区人员限制 150518
                if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals("4000005644")) {
                    sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", logonUser));
                }

                //增加大区限制
                if (!"".equals(orgCode) && null != orgCode) {
                    sql.append("  AND EXISTS (SELECT 1\n");
                    sql.append("         FROM VW_ORG_DEALER_SERVICE S\n");
                    sql.append("        WHERE S.DEALER_ID = TD.DEALER_ID\n");
                    sql.append("          AND S.Org_Code IN (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(orgCode) + "))\n");
                }
                sql.append(" ORDER BY TD.DEALER_CODE\n");
            } else {
                sql.append("WITH TSK_MONTH AS --年任务\n");
                sql.append(" (SELECT TM.DEALER_ID,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-01', NVL(TM.AMOUNT, 0), 0)) YM_TK_01,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-02', NVL(TM.AMOUNT, 0), 0)) YM_TK_02,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-03', NVL(TM.AMOUNT, 0), 0)) YM_TK_03,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-04', NVL(TM.AMOUNT, 0), 0)) YM_TK_04,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-05', NVL(TM.AMOUNT, 0), 0)) YM_TK_05,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-06', NVL(TM.AMOUNT, 0), 0)) YM_TK_06,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-07', NVL(TM.AMOUNT, 0), 0)) YM_TK_07,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-08', NVL(TM.AMOUNT, 0), 0)) YM_TK_08,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-09', NVL(TM.AMOUNT, 0), 0)) YM_TK_09,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-10', NVL(TM.AMOUNT, 0), 0)) YM_TK_10,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-11', NVL(TM.AMOUNT, 0), 0)) YM_TK_11,\n");
                sql.append("         SUM(DECODE(TM.TASK_MONTH, '" + year + "-12', NVL(TM.AMOUNT, 0), 0)) YM_TK_12,\n");
                sql.append("         SUM(TM.AMOUNT) TK_SUM\n");
                sql.append("    FROM VW_PART_DLR_TASK_MONTH TM\n");
                sql.append("   WHERE TM.TASK_MONTH >= '" + year + "-01'\n");
                sql.append("     AND TM.TASK_MONTH <= '" + year + "-12'\n");
                sql.append("   GROUP BY TM.DEALER_ID),\n");
                sql.append("CP_MONTH AS --月完成\n");
                sql.append(" (SELECT TM.DEALER_ID,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-01', NVL(TM.AMOUNT, 0), 0)) YM_CP_01,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-02', NVL(TM.AMOUNT, 0), 0)) YM_CP_02,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-03', NVL(TM.AMOUNT, 0), 0)) YM_CP_03,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-04', NVL(TM.AMOUNT, 0), 0)) YM_CP_04,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-05', NVL(TM.AMOUNT, 0), 0)) YM_CP_05,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-06', NVL(TM.AMOUNT, 0), 0)) YM_CP_06,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-07', NVL(TM.AMOUNT, 0), 0)) YM_CP_07,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-08', NVL(TM.AMOUNT, 0), 0)) YM_CP_08,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-09', NVL(TM.AMOUNT, 0), 0)) YM_CP_09,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-10', NVL(TM.AMOUNT, 0), 0)) YM_CP_10,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-11', NVL(TM.AMOUNT, 0), 0)) YM_CP_11,\n");
                sql.append("         SUM(DECODE(TM.MONTH_NO, '" + year + "-12', NVL(TM.AMOUNT, 0), 0)) YM_CP_12,\n");
                sql.append("         SUM(TM.AMOUNT) CP_SUM\n");
                sql.append("    FROM VW_PART_DLR_MONTH_PURCHASE TM\n");
                sql.append("   WHERE TM.MONTH_NO >= '" + year + "-01'\n");
                sql.append("     AND TM.MONTH_NO <= '" + year + "-12'\n");
                sql.append("   GROUP BY TM.DEALER_ID)\n");
                if ("1".equals(flag)) {
                    sql.append("SELECT TD.DEALER_ID,\n");
                    sql.append("       TD.DEALER_CODE,\n");
                    sql.append("       TD.DEALER_NAME,\n");
                    for (int i = 1; i <= 12; i++) {
                        String mth = i + "";
                        if (i < 10) {
                            mth = "0" + i;
                        }
                        sql.append("       TO_CHAR(SUM(DECODE(TD.DEALER_ID, TM.DEALER_ID, TM.YM_TK_" + mth + ", 0)),\n");
                        sql.append("               'fm999,999,999,990.00') YM_TK_" + mth + ",\n");
                        sql.append("       TO_CHAR(SUM(DECODE(TD.DEALER_ID, CM.DEALER_ID, CM.YM_CP_" + mth + ", 0)),\n");
                        sql.append("               'fm999,999,999,990.00') YM_CP_" + mth + ",\n");
                        sql.append("       CASE\n");
                        sql.append("         WHEN SUM(DECODE(TD.DEALER_ID, TM.DEALER_ID, TM.YM_TK_" + mth + ", 0)) > 0 AND\n");
                        sql.append("              SUM(DECODE(TD.DEALER_ID, CM.DEALER_ID, CM.YM_CP_" + mth + ", 0)) > 0 THEN\n");
                        sql.append("          ROUND(SUM(DECODE(TD.DEALER_ID, CM.DEALER_ID, CM.YM_CP_" + mth + ", 0)) /\n");
                        sql.append("                SUM(DECODE(TD.DEALER_ID, TM.DEALER_ID, TM.YM_TK_" + mth + ", 0)) * 100,\n");
                        sql.append("                2) || '%'\n");
                        sql.append("         ELSE\n");
                        sql.append("          '-'\n");
                        sql.append("       END YM_RT_" + mth + ",\n");
                    }
                    sql.append("       TO_CHAR(SUM(TM.TK_SUM), 'fm999,999,999,990.00') TK_SUM,\n");
                    sql.append("       TO_CHAR(SUM(CM.CP_SUM), 'fm999,999,999,990.00') CP_SUM,\n");
                    sql.append("       CASE\n");
                    sql.append("         WHEN SUM(TM.TK_SUM) > 0 AND SUM(CM.CP_SUM) > 0 THEN\n");
                    sql.append("          ROUND(SUM(CM.CP_SUM) / SUM(TM.TK_SUM) * 100, 2) || '%'\n");
                    sql.append("         ELSE\n");
                    sql.append("          '-'\n");
                    sql.append("       END SUM_RT\n");
                } else {
                    sql.append("SELECT  \n");
                    for (int i = 1; i <= 12; i++) {
                        String mth = i + "";
                        if (i < 10) {
                            mth = "0" + i;
                        }
                        sql.append("       TO_CHAR(SUM(DECODE(TD.DEALER_ID, TM.DEALER_ID, TM.YM_TK_" + mth + ", 0)),\n");
                        sql.append("               'fm999,999,999,990.00') YM_TK_" + mth + ",\n");
                        sql.append("       TO_CHAR(SUM(DECODE(TD.DEALER_ID, CM.DEALER_ID, CM.YM_CP_" + mth + ", 0)),\n");
                        sql.append("               'fm999,999,999,990.00') YM_CP_" + mth + ",\n");
                        sql.append("       CASE\n");
                        sql.append("         WHEN SUM(DECODE(TD.DEALER_ID, TM.DEALER_ID, TM.YM_TK_" + mth + ", 0)) > 0 AND\n");
                        sql.append("              SUM(DECODE(TD.DEALER_ID, CM.DEALER_ID, CM.YM_CP_" + mth + ", 0)) > 0 THEN\n");
                        sql.append("          ROUND(SUM(DECODE(TD.DEALER_ID, CM.DEALER_ID, CM.YM_CP_" + mth + ", 0)) /\n");
                        sql.append("                SUM(DECODE(TD.DEALER_ID, TM.DEALER_ID, TM.YM_TK_" + mth + ", 0)) * 100,\n");
                        sql.append("                2) || '%'\n");
                        sql.append("         ELSE\n");
                        sql.append("          '-'\n");
                        sql.append("       END YM_RT_" + mth + ",\n");
                    }
                    sql.append("       TO_CHAR(SUM(TM.TK_SUM), 'fm999,999,999,990.00') TK_SUM,\n");
                    sql.append("       TO_CHAR(SUM(CM.CP_SUM), 'fm999,999,999,990.00') CP_SUM,\n");
                    sql.append("       CASE\n");
                    sql.append("         WHEN SUM(TM.TK_SUM) > 0 AND SUM(CM.CP_SUM) > 0 THEN\n");
                    sql.append("          ROUND(SUM(CM.CP_SUM) / SUM(TM.TK_SUM) * 100, 2) || '%'\n");
                    sql.append("         ELSE\n");
                    sql.append("          '-'\n");
                    sql.append("       END SUM_RT\n");
                }
                sql.append("  FROM TM_DEALER TD, TSK_MONTH TM, CP_MONTH CM\n");
                sql.append(" WHERE TD.DEALER_ID = TM.DEALER_ID(+)\n");
                sql.append("   AND TD.DEALER_ID = CM.DEALER_ID(+)\n");
                sql.append("   AND TD.DEALER_TYPE = 10771002\n");
                sql.append("   AND TD.DEALER_LEVEL = 10851001\n");
                //服务商代码
                if (!"".equals(dealerCode) && null != dealerCode) {
                    if (!"".equals(dealerCode)) {
                        if (dealerCode.split(",").length > 1) {
                            String[] dcs = dealerCode.split(",");
                            StringBuffer tempsql = new StringBuffer();
                            for (String dc : dcs) {
                                tempsql.append("'" + dc + "',");
                            }
                            tempsql.deleteCharAt(tempsql.length() - 1);
                            sql.append(" AND TD.DEALER_CODE IN (" + tempsql + ")\n");
                        } else {
                            sql.append(" AND UPPER(TD.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%'\n");
                        }

                    }
                }
                //服务商名称
                if (!"".equals(dealerName) && null != dealerName) {
                    sql.append("  AND TD.DEALER_NAME LIKE '%" + dealerName + "%'\n");
                }
                //增加大区人员限制 150518
                if (this.getPoseRoleId(logonUser.getPoseId().toString()).equals("4000005644")) {
                    sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", logonUser));
                }
                //增加大区限制
                if (!"".equals(orgCode) && null != orgCode) {
                    sql.append("  AND EXISTS (SELECT 1\n");
                    sql.append("         FROM VW_ORG_DEALER_SERVICE S\n");
                    sql.append("        WHERE S.DEALER_ID = TD.DEALER_ID\n");
                    sql.append("          AND S.Org_Code IN (" + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(orgCode) + "))\n");
                }
                if ("1".equals(flag)) {
                    sql.append(" GROUP BY TD.DEALER_ID, TD.DEALER_CODE, TD.DEALER_NAME\n");
                    sql.append(" ORDER BY TD.DEALER_CODE\n");
                }
            }
            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;
        } catch (Exception e) {
            throw e;
        }
    }


}
