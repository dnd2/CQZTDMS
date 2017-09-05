package com.infodms.dms.dao.report.partSalesReport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class PartCheckAnalysisRepDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartCheckAnalysisRepDao.class);
    private static final PartCheckAnalysisRepDao dao = new PartCheckAnalysisRepDao();

    private PartCheckAnalysisRepDao() {
    }

    public static final PartCheckAnalysisRepDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @param loginUser
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-11-26
     * @Title : 考核分析报表（本部）
     */
    public PageResult<Map<String, Object>> queryRep(RequestWrapper request, int curPage, int pageSize, AclUserBean loginUser) {
        StringBuffer sql = new StringBuffer();

        String startMoth = CommonUtils.checkNull(request.getParamValue("StMth"));
        String endMoth = CommonUtils.checkNull(request.getParamValue("EdMth"));
        String year = CommonUtils.checkNull(request.getParamValue("SYear"));
        String queryFlag = CommonUtils.checkNull(request.getParamValue("searchType"));//查询标识

        String startYM = year + "-" + startMoth;
        String endYm = year + "-" + endMoth;

        //按大区、省份查询
        if ("prov".equals(queryFlag)) {
            sql.append(" SELECT '" + startYM + "至" + endYm + "' MONTH_NO, ");

            sql.append("SR.NAME,\n");
            sql.append("UPR.NAME REG_USER,\n");
            sql.append("DECODE(GROUPING(DS.ROOT_ORG_NAME), 1, '合计', DS.ROOT_ORG_NAME) ROOT_ORG_NAME,\n");
            sql.append("DECODE(GROUPING(DS.REGION_NAME), 1, '', DS.REGION_NAME) DREGION_NAME,\n");
            sql.append("ROUND(SUM(TM.AMOUNT)) AMOUNT,\n");
            sql.append("ROUND(SUM(TM.P_AMOUNT), 2) P_AMOUNT,\n");
            sql.append("CASE\n");
            sql.append("  WHEN ROUND(SUM(TM.AMOUNT)) > 0 AND ROUND(SUM(TM.P_AMOUNT), 2) > 0 THEN\n");
            sql.append("   TO_CHAR(ROUND(SUM(TM.P_AMOUNT), 2) / ROUND(SUM(TM.AMOUNT)) * 100,\n");
            sql.append("           '999.99') || '%'\n");
            sql.append("  ELSE\n");
            sql.append("   '-'\n");
            sql.append("END P_RTO,\n");
            sql.append("NVL(SUM((SELECT SUM(BA.BALANCE_AMOUNT)\n");
            sql.append("          FROM TT_PART_AS_BALANCE_AMOUNT BA\n");
            sql.append("         WHERE BA.DEALER_ID = DS.DEALER_ID\n");
            sql.append("           AND BA.FI_DATE = TM.TASK_MONTH)),\n");
            sql.append("    0) B_A,\n");
            sql.append("CASE\n");
            sql.append("  WHEN SUM((SELECT SUM(BA.BALANCE_AMOUNT)\n");
            sql.append("             FROM TT_PART_AS_BALANCE_AMOUNT BA\n");
            sql.append("            WHERE BA.DEALER_ID = DS.DEALER_ID\n");
            sql.append("              AND BA.FI_DATE = TM.TASK_MONTH)) <> 0 THEN\n");
            sql.append("   to_char(NVL(ROUND(SUM(TM.P_AMOUNT) /\n");
            sql.append("             SUM((SELECT SUM(BA.BALANCE_AMOUNT)\n");
            sql.append("                   FROM TT_PART_AS_BALANCE_AMOUNT BA\n");
            sql.append("                  WHERE BA.DEALER_ID = DS.DEALER_ID\n");
            sql.append("                    AND BA.FI_DATE = TM.TASK_MONTH)),\n");
            sql.append("             2),\n");
            sql.append("       0))\n");
            sql.append("  ELSE\n");
            sql.append("   '-'\n");
            sql.append("END TZ_RATIO\n");

            sql.append(" FROM VW_PART_DLR_TASK_MONTH TM, ");
            sql.append(" VW_ORG_DEALER_SERVICE DS, ");
            sql.append(" VW_PART_USER_POSE_REGION UPR, ");
            sql.append(" VW_PART_SALER_REGION SR ");
            sql.append(" WHERE TM.DEALER_ID = DS.DEALER_ID ");
            sql.append(" AND UPR.ORG_ID = DS.ROOT_ORG_ID ");
            sql.append(" AND SR.REGION_ID = DS.REGION_ID ");
            sql.append(" AND TM.DEALER_CODE NOT LIKE 'G%' ");
            sql.append(" AND TM.TASK_MONTH >= '" + startYM + "' ");
            sql.append(" AND TM.TASK_MONTH <= '" + endYm + "' ");
            sql.append(" GROUP BY ROLLUP(SR.NAME, UPR.NAME, DS.ROOT_ORG_NAME, DS.REGION_NAME) ");
        }
        //按供应中心查询
        else if ("gyzx".equals(queryFlag)) {
            sql.append(" SELECT '" + startYM + "至" + endYm + "' MONTH_NO, ");
            sql.append(" DQ.PARENTORG_CODE, ");
            sql.append(" DQ.PARENTORG_NAME, ");
            sql.append(" DQ.REGION_NAME, ");
            sql.append(" SUM(T.AMOUNT) AMOUNT, ");
            sql.append(" SUM(T.P_AMOUNT) P_AMOUNT, ");
            sql.append(" CASE ");
            sql.append(" WHEN SUM(T.P_AMOUNT) > 0 AND SUM(T.AMOUNT) > 0 THEN ");
            sql.append(" TO_CHAR(SUM(T.P_AMOUNT) / SUM(T.AMOUNT) * 100, '999.99') || '%' ");
            sql.append(" ELSE ");
            sql.append(" '-' ");
            sql.append(" END P_RTO, ");
            sql.append(" SUM(DQJS.BALANCE_AMOUNT) B_A, ");
            sql.append("CASE\n");
            sql.append("        WHEN SUM(DQJS.BALANCE_AMOUNT) > 0 THEN\n");
            sql.append("         TO_CHAR(ROUND(SUM(T.P_AMOUNT) / SUM(DQJS.BALANCE_AMOUNT), 2))\n");
            sql.append("        ELSE\n");
            sql.append("         '-'\n");
            sql.append("      END TZ_RATIO\n");
            sql.append(" FROM (SELECT SR.PARENTORG_ID, ");
            sql.append(" SR.PARENTORG_CODE, ");
            sql.append(" SR.PARENTORG_NAME, ");
            sql.append(" za_concat(DISTINCT DS.REGION_NAME) REGION_NAME ");
            sql.append(" FROM TT_PART_SALES_RELATION SR, VW_ORG_DEALER_SERVICE DS ");
            sql.append(" WHERE SR.CHILDORG_ID = DS.DEALER_ID ");
            sql.append(" AND SR.PARENTORG_ID <> '" + Constant.OEM_ACTIVITIES + "' ");
            sql.append(" GROUP BY SR.PARENTORG_ID, SR.PARENTORG_CODE, SR.PARENTORG_NAME ");
            sql.append(" ORDER BY SR.PARENTORG_NAME) DQ, ");
            sql.append(" VW_PART_DLR_TASK_MONTH T, ");
            sql.append(" (SELECT SR.PARENTORG_ID, ");
            sql.append(" T.FI_DATE, ");
            sql.append(" SUM(T.BALANCE_AMOUNT) BALANCE_AMOUNT ");
            sql.append(" FROM TT_PART_AS_BALANCE_AMOUNT T, ");
            sql.append(" VW_ORG_DEALER_SERVICE DS, ");
            sql.append(" TT_PART_SALES_RELATION SR ");
            sql.append(" WHERE DS.DEALER_ID = T.DEALER_ID ");
            sql.append(" AND SR.CHILDORG_ID = T.DEALER_ID ");
            sql.append(" AND SR.PARENTORG_ID <> '" + Constant.OEM_ACTIVITIES + "' ");
            sql.append(" GROUP BY SR.PARENTORG_ID, T.FI_DATE) DQJS ");
            sql.append(" WHERE DQ.PARENTORG_ID = T.DEALER_ID ");
            sql.append(" AND DQ.PARENTORG_ID = DQJS.PARENTORG_ID ");
            sql.append(" AND T.DEALER_CODE LIKE 'G%' ");
            sql.append(" AND T.TASK_MONTH = DQJS.FI_DATE ");
            sql.append(" AND T.TASK_MONTH > = '" + startYM + "' ");
            sql.append(" AND T.TASK_MONTH <= '" + endYm + "' ");
            sql.append(" GROUP BY DQ.PARENTORG_CODE, DQ.PARENTORG_NAME, DQ.REGION_NAME ");
        }
        //按服务商查询
        else {
            String dealerIds = CommonUtils.checkNull(request.getParamValue("dealerId"));//服务商ID

            sql.append(" SELECT '" + startYM + "至" + endYm + "' MONTH_NO, ");
            sql.append(" DS.DEALER_CODE, ");
            sql.append(" DS.DEALER_NAME, ");
            sql.append(" SUM(TM.AMOUNT) AMOUNT, ");
            sql.append(" ROUND(SUM(TM.P_AMOUNT), 2) P_AMOUNT, ");
            sql.append(" CASE ");
            sql.append(" WHEN SUM(TM.AMOUNT) > 0 AND ROUND(SUM(TM.P_AMOUNT), 2) > 0 THEN ");
            sql.append(" TO_CHAR(ROUND(SUM(TM.P_AMOUNT), 2) / SUM(TM.AMOUNT) * 100, ");
            sql.append(" '999.99') || '%' ");
            sql.append(" ELSE ");
            sql.append(" '-' ");
            sql.append(" END P_RTO, ");
            sql.append(" NVL(SUM((SELECT SUM(BA.BALANCE_AMOUNT) ");
            sql.append(" FROM TT_PART_AS_BALANCE_AMOUNT BA ");
            sql.append(" WHERE BA.DEALER_ID = DS.DEALER_ID ");
            sql.append(" AND BA.FI_DATE = TM.TASK_MONTH)), ");
            sql.append(" 0) B_A, ");
            //modify by yuan 2013
            sql.append("CASE\n");
            sql.append("        WHEN ROUND(SUM(TM.P_AMOUNT), 2) > 0 AND\n");
            sql.append("             SUM((SELECT SUM(BA.BALANCE_AMOUNT)\n");
            sql.append("                   FROM TT_PART_AS_BALANCE_AMOUNT BA\n");
            sql.append("                  WHERE BA.DEALER_ID = DS.DEALER_ID\n");
            sql.append("                    AND BA.FI_DATE = TM.TASK_MONTH)) > 0 THEN\n");
            sql.append("         TO_CHAR(NVL(ROUND(ROUND(SUM(TM.P_AMOUNT), 2) /\n");
            sql.append("                           SUM((SELECT SUM(BA.BALANCE_AMOUNT)\n");
            sql.append("                                 FROM TT_PART_AS_BALANCE_AMOUNT BA\n");
            sql.append("                                WHERE BA.DEALER_ID = DS.DEALER_ID\n");
            sql.append("                                  AND BA.FI_DATE = TM.TASK_MONTH)),\n");
            sql.append("                           2),\n");
            sql.append("                     0))\n");
            sql.append("        ELSE\n");
            sql.append("         '-'\n");
            sql.append("      END TZ_RATIO\n");
            //end
            sql.append(" FROM VW_PART_DLR_TASK_MONTH TM, ");
            sql.append(" VW_ORG_DEALER_SERVICE DS, ");
            sql.append(" VW_PART_USER_POSE_REGION UPR, ");
            sql.append(" VW_PART_SALER_REGION SR ");
            sql.append(" WHERE TM.DEALER_ID = DS.DEALER_ID ");
            sql.append(" AND UPR.ORG_ID = DS.ROOT_ORG_ID ");
            sql.append(" AND SR.REGION_ID = DS.REGION_ID ");
            sql.append(" AND TM.TASK_MONTH >= '" + startYM + "' ");
            sql.append(" AND TM.TASK_MONTH <= '" + endYm + "' ");

            if (!"".equals(dealerIds)) {
                sql.append(" AND DS.DEALER_ID IN (").append(dealerIds).append(") ");
            }

            sql.append(" AND TM.DEALER_CODE NOT LIKE 'G%' ");
            sql.append(" GROUP BY DS.DEALER_CODE,DS.DEALER_NAME ");


        }


        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

}
