package com.infodms.dms.dao.parts.monthPlan;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonthPlanManagerDao extends BaseDao<PO> {
    public static final Logger logger = Logger.getLogger(MonthPlanManagerDao.class);
    private static final MonthPlanManagerDao dao = new MonthPlanManagerDao();

    private MonthPlanManagerDao() {
    }

    public static final MonthPlanManagerDao getInstance() {
        return dao;
    }


    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : checkDealer
     * @Description: 验证服务商代码是否存在
     */
    public String checkDealer(String delCode) {
        String sql = "select * from tm_dealer td where td.dealer_code = '" + delCode + "'";
        List<Map<String, Object>> list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        if (list.size() > 0)
            return (list.get(0).get("DEALER_ID")).toString();
        return "0";
    }

    /**
     * @param : @param taskMonth
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : checkVer
     * @Description: 获取最大版本号
     */
    public String checkVer(String taskMonth) {
        String sql = "select max(mt.ver) as VER from tt_part_dlr_month_task mt where mt.task_month= '" + taskMonth + "'";
        List<Map<String, Object>> list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        if (list.size() < 0 || list.get(0) == null || null == list.get(0).get("VER"))
            return "0";
        return (list.get(0).get("VER")).toString();

    }

    /**
     * @param : @param taskMonth
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : getPlanVer
     * @Description: 获取最大版本号
     */
    public List<String> getPlanVer(String taskMonth) {
        String sql = "select distinct(ver) from tt_part_dlr_month_task mt where mt.task_month= '" + taskMonth + "' order by ver";
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        List<String> list = new ArrayList<String>();
        for (Map<String, Object> map : listMap) {
            list.add(map.get("VER").toString());
        }
        return list;
    }

    /**
     * @param : @param consql
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : planPageQuery
     * @Description: 查询
     */
    public PageResult<Map<String, Object>> planPageQuery(String consql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TO_CHAR(TD.AMOUNT, 'fm999,999,990.00') AMOUNT,\n");
        sql.append("       TD.TASK_MONTH,\n");
        sql.append("       TO_CHAR(TD.P_AMOUNT, 'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       TD.P_RATIO RATIO\n");
        sql.append("  FROM VW_PART_DLR_TASK_MONTH TD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(consql);
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param consql
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> planCountQuery(String consql, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }
        sql.append("SELECT TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("      '" + year + "-" + month + " 至 " + year + "-" + month2 + "' AS TASK_MONTH,\n");
        sql.append("       TO_CHAR(SUM(NVL(TD.P_AMOUNT, 0)), 'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN SUM(NVL(TD.P_AMOUNT, 0)) <> 0 AND SUM(TD.AMOUNT) <> 0 THEN\n");
        sql.append("          ROUND(SUM(NVL(TD.P_AMOUNT, 0)) / SUM(TD.AMOUNT) * 100, 2) || '%'\n");
        sql.append("         ELSE\n");
        sql.append("          '-'\n");
        sql.append("       END RATIO\n");
        sql.append("  FROM VW_PART_DLR_TASK_MONTH TD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(consql);
        sql.append(" GROUP BY TD.DEALER_CODE, TD.DEALER_NAME\n");
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param consql
     * @param request
     * @return
     */
    public List<Map<String, Object>> planAmountCountQuery(String consql, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,999,990.00') AMOUNT,\n");
        sql.append("       TO_CHAR(SUM(NVL(TD.P_AMOUNT, 0)), 'fm999,999,999,990.00') AS SELLMONEY,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN SUM(NVL(TD.P_AMOUNT, 0)) <> 0 AND SUM(TD.AMOUNT) <> 0 THEN\n");
        sql.append("          ROUND(SUM(NVL(TD.P_AMOUNT, 0)) / SUM(TD.AMOUNT) * 100, 2) || '%'\n");
        sql.append("         ELSE\n");
        sql.append("          '-'\n");
        sql.append("       END RATIO\n");
        sql.append("  FROM VW_PART_DLR_TASK_MONTH TD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(consql);

        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * @param : @param consql
     * @param : @return
     * @return :LastDate    : 2013-8-23
     * @Title : downloadPlanQuery
     * @Description: 查询下载
     */
    public List<Map<String, Object>> downloadPlanQuery(String consql) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,999,990.00') AMOUNT,\n");
        sql.append("       TO_CHAR(SUM(TD.P_AMOUNT), 'fm999,999,999,990.00') SELLMONEY,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN SUM(TD.P_AMOUNT) <> 0 AND SUM(TD.AMOUNT) <> 0 THEN\n");
        sql.append("          ROUND(SUM(TD.P_AMOUNT) / SUM(TD.AMOUNT) * 100, 2) || '%'\n");
        sql.append("         ELSE\n");
        sql.append("          '_'\n");
        sql.append("       END RATIO\n");
        sql.append("  FROM VW_PART_DLR_TASK_MONTH TD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(consql);
        return pageQuery(sql.toString(), null, getFunName());
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    //本部后面不选（明细）

    /**
     * @param consql
     * @param orgId
     * @param a
     * @param b
     * @param is_type
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> planPageQueryBb(String consql, String orgId, String a, String b, String is_type, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT VM.TASK_MONTH TASK_MONTH,\n");
        sql.append("       VM.DEALER_ID,\n");
        sql.append("       VM.DEALER_CODE,\n");
        sql.append("       VM.DEALER_NAME,\n");

        sql.append("CASE\n");
        sql.append("        WHEN (SELECT 1\n");
        sql.append("                FROM VW_PART_OEM_DLR D\n");
        sql.append("               WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("         ROUND(VM.AMOUNT, 2)\n");
        sql.append("        ELSE\n");
        sql.append("         0\n");
        sql.append("      END AMOUNT,\n");

        sql.append("       NVL(PS.AMOUNT, 0) SELLMONEY,\n");
        sql.append("       TO_CHAR(NVL(PS.AMOUNT, 0) / VM.AMOUNT * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM VW_PART_DLR_TASK_MONTH VM,\n");
        sql.append("       (SELECT P.MONTH_NO, P.DEALER_ID, P.AMOUNT\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE P.SELLER_ID = " + orgId + ") PS\n");
        sql.append(" WHERE VM.TASK_MONTH = PS.MONTH_NO\n");
        sql.append("   AND VM.DEALER_ID = PS.DEALER_ID\n");
       /* sql.append("   AND P.SELLER_ID = "+orgId+"\n");*/
        sql.append("AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        if (("8").equals(is_type)) {
            sql.append("   and not exists(select 1 from tm_dealer tt where tt.dealer_id=ps.DEALER_ID and tt.is_nbdw=1)\n");
        } else {
            sql.append("   and exists(select 1 from tm_dealer tt where tt.dealer_id=ps.DEALER_ID and tt.is_nbdw=1)\n");
        }
        sql.append("UNION ALL\n");

        sql.append("SELECT P.MONTH_NO TASK_MONTH,\n");
        sql.append("       VM.DEALER_ID,\n");
        sql.append("       VM.DEALER_CODE,\n");
        sql.append("       VM.DEALER_NAME,\n");
        sql.append("       0 AMOUNT,\n");
        sql.append("       P.AMOUNT SELLMONEY,\n");
        sql.append("       '-' AS RATIO\n");
        sql.append("  FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER VM\n");
        sql.append(" WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        if (("8").equals(is_type)) {
            sql.append("   and not exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        } else {
            sql.append("   and exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        }
        sql.append("   AND P.SELLER_ID = " + orgId + "\n");
        sql.append("   AND NOT EXISTS (SELECT 1\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("         WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VMm.DEALER_ID = P.DEALER_ID)\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param consql
     * @param orgId
     * @param a
     * @param b
     * @param is_type
     * @return
     */
    public List<Map<String, Object>> downloadPlanQueryBb(String consql, String orgId, String a, String b, String is_type) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT VM.TASK_MONTH TASK_MONTH,\n");
        sql.append("       VM.DEALER_ID,\n");
        sql.append("       VM.DEALER_CODE,\n");
        sql.append("       VM.DEALER_NAME,\n");
        sql.append("CASE\n");
        sql.append("        WHEN (SELECT 1\n");
        sql.append("                FROM VW_PART_OEM_DLR D\n");
        sql.append("               WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("         ROUND(VM.AMOUNT, 2)\n");
        sql.append("        ELSE\n");
        sql.append("         0\n");
        sql.append("      END AMOUNT,\n");

        sql.append("nvl(PS.AMOUNT,0) SELLMONEY,\n");
        sql.append("     TO_CHAR(nvl(PS.AMOUNT,0) / DECODE(VM.AMOUNT, 0, 1, VM.AMOUNT) * 100,\n");
        sql.append("             '999.00') || '%' AS RATIO\n");
        sql.append(" FROM VW_PART_DLR_TASK_MONTH VM,\n");
        sql.append("      (SELECT P.MONTH_NO, P.DEALER_ID, P.AMOUNT\n");
        sql.append("         FROM VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("        WHERE P.SELLER_ID = " + orgId + ") PS\n");
        sql.append("WHERE VM.TASK_MONTH = PS.MONTH_NO\n");
        sql.append("  AND VM.DEALER_ID = PS.DEALER_ID\n");
        /*sql.append("   AND P.SELLER_ID = "+orgId+"\n"); */
        sql.append("AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        if (("8").equals(is_type)) {
            sql.append("   and not exists(select 1 from tm_dealer tt where tt.dealer_id=ps.DEALER_ID and tt.is_nbdw=1)\n");
        } else {
            sql.append("   and exists(select 1 from tm_dealer tt where tt.dealer_id=ps.DEALER_ID and tt.is_nbdw=1)\n");
        }
        sql.append("UNION ALL\n");

        sql.append("SELECT P.MONTH_NO TASK_MONTH,\n");
        sql.append("       VM.DEALER_ID,\n");
        sql.append("       VM.DEALER_CODE,\n");
        sql.append("       VM.DEALER_NAME,\n");
        sql.append("       0 AMOUNT,\n");
        sql.append("       P.AMOUNT SELLMONEY,\n");
        sql.append("       '-' AS RATIO\n");
        sql.append("  FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER VM\n");
        sql.append(" WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        if (("8").equals(is_type)) {
            sql.append("   and not exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        } else {
            sql.append("   and exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        }
        sql.append("   AND P.SELLER_ID = " + orgId + "\n");
        sql.append("   AND NOT EXISTS (SELECT 1\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("         WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VMm.DEALER_ID = P.DEALER_ID)\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * @param consql
     * @param orgId
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> planCountQueryBb(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        String is_type = CommonUtils.checkNull(request.getParamValue("is_type"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.DEALER_CODE,\n");
        sql.append("       a.DEALER_NAME,\n");
        sql.append("NVL(SUM(A.AMOUNT), 0) AMOUNT,\n");
        sql.append("NVL(SUM(A.SELLMONEY), 0) SELLMONEY,\n");
        sql.append("CASE\n");
        sql.append("  WHEN NVL(SUM(A.AMOUNT), 0)=0 THEN\n");
        sql.append("   '-'\n");
        sql.append("  ELSE\n");
        sql.append("   TO_CHAR(SUM(A.SELLMONEY) / NVL(SUM(A.AMOUNT), 0.001) * 100,\n");
        sql.append("           '999.00') || '%'\n");
        sql.append("END AS RATIO\n");
        sql.append("  from (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               VM.DEALER_CODE,\n");
        sql.append("               VM.DEALER_NAME,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");

        sql.append("CASE\n");
        sql.append("               WHEN (SELECT 1\n");
        sql.append("                       FROM VW_PART_OEM_DLR D\n");
        sql.append("                      WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("                ROUND(VM.AMOUNT, 2)\n");
        sql.append("               ELSE\n");
        sql.append("                0\n");
        sql.append("             END AMOUNT,\n");

        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH VM, \n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
       /* sql.append("   AND P.SELLER_ID = "+orgId+"\n"); */
        sql.append("   AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        if (("8").equals(is_type)) {
            sql.append("   and not exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        } else {
            sql.append("   and exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        }
        sql.append("\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               vm.DEALER_CODE,\n");
        sql.append("               vm.DEALER_NAME,\n");
        sql.append("               null AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("   AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append("   AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        if (("8").equals(is_type)) {
            sql.append("   and not exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        } else {
            sql.append("   and exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        }
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)\n");
        sql.append("\n");
        sql.append("        ) a\n");
        sql.append(" group by a.TASK_MONTH, a.DEALER_CODE, a.DEALER_NAME\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param consql
     * @param orgId
     * @param request
     * @return
     */
    public List<Map<String, Object>> planAmountCountQueryBb(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        String is_type = CommonUtils.checkNull(request.getParamValue("is_type"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.DEALER_CODE,\n");
        sql.append("       a.DEALER_NAME,\n");
        sql.append("NVL(SUM(A.AMOUNT), 0) AMOUNT,\n");
        sql.append("     NVL(SUM(A.SELLMONEY), 0) SELLMONEY,\n");
        sql.append("     CASE\n");
        sql.append("       WHEN SUM(A.AMOUNT) =0 THEN\n");
        sql.append("        '-'\n");
        sql.append("       ELSE\n");
        sql.append("        TO_CHAR(SUM(A.SELLMONEY) / NVL(SUM(A.AMOUNT), 0.001) * 100,\n");
        sql.append("                '999.00') || '%'\n");
        sql.append("     END AS RATIO\n");
        sql.append("  from (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");

        sql.append("VM.DEALER_CODE,\n");
        sql.append("            VM.DEALER_NAME,\n");
        //sql.append("            VM.AMOUNT AMOUNT,\n");

        sql.append("CASE\n");
        sql.append("               WHEN (SELECT 1\n");
        sql.append("                       FROM VW_PART_OEM_DLR D\n");
        sql.append("                      WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("                ROUND(VM.AMOUNT, 2)\n");
        sql.append("               ELSE\n");
        sql.append("                0\n");
        sql.append("             END AMOUNT,\n");

        sql.append("            NVL(P.AMOUNT, 0) SELLMONEY\n");
        sql.append("       FROM VW_PART_DLR_TASK_MONTH VM,\n");
        sql.append("            (SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("               FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("              WHERE PS.SELLER_ID = " + orgId + ") P\n");
        sql.append("      WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("        AND VM.DEALER_ID = P.DEALER_ID\n");

//        sql.append("   AND P.SELLER_ID = "+orgId+"\n");
        sql.append("   AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + bb + "'\n");
        if (("8").equals(is_type)) {
            sql.append("   and not exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        } else {
            sql.append("   and exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        }
        sql.append(consql);
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               vm.DEALER_CODE,\n");
        sql.append("               vm.DEALER_NAME,\n");
        sql.append("               0 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("   AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append("   AND P.SELLER_ID = " + orgId + "\n");
        if (("8").equals(is_type)) {
            sql.append("   and not exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        } else {
            sql.append("   and exists(select 1 from tm_dealer tt where tt.dealer_id=p.DEALER_ID and tt.is_nbdw=1)\n");
        }
        sql.append(consql);
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)\n");
        sql.append("\n");
        sql.append("        ) a\n");
        sql.append(" group by a.TASK_MONTH, a.DEALER_CODE, a.DEALER_NAME\n");

        return pageQuery(sql.toString(), null, getFunName());
    }


    /**
     * @param consql
     * @param orgId
     * @param a
     * @param b
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> planPageQueryBb_Xxy(String consql, String orgId, String a, String b, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT a.TASK_MONTH, --'2013-09 至 2013-10'\n");
        sql.append("       A.NAME,\n");
        sql.append("       NVL(ROUND(SUM(A.AMOUNT),2),0) AMOUNT,\n");
        sql.append("       NVL(SUM(A.SELLMONEY),0) SELLMONEY,\n");
        sql.append("       TO_CHAR(NVL(SUM(A.SELLMONEY),0) / NVL(SUM(A.AMOUNT),1) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT VM.TASK_MONTH TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");
        sql.append("CASE\n");
        sql.append("        WHEN (SELECT 1\n");
        sql.append("                FROM VW_PART_OEM_DLR D\n");
        sql.append("               WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("         ROUND(VM.AMOUNT, 2)\n");
        sql.append("        ELSE\n");
        sql.append("         0\n");
        sql.append("      END AMOUNT,\n");

        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("               WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("        AND VM.DEALER_ID = P.DEALER_ID\n");
        //sql.append("           AND P.SELLER_ID = "+orgId+"\n");
        sql.append("AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND VM.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  VM,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + b + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND P.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMM\n");
        sql.append("                 WHERE VMM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMM.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.NAME\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //本部销售员(明细合计)（下载也是它）

    /**
     * @param consql
     * @param orgId
     * @param a
     * @param b
     * @return
     */
    public List<Map<String, Object>> downloadPlanQueryBb_Xxy(String consql, String orgId, String a, String b) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT a.TASK_MONTH, --'2013-09 至 2013-10'\n");
        sql.append("       A.NAME,\n");
        sql.append("       ROUND(SUM(A.AMOUNT),2) AMOUNT,\n");
        sql.append("       NVL(SUM(A.SELLMONEY),0) SELLMONEY,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / SUM(A.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT VM.TASK_MONTH TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");
        sql.append("CASE\n");
        sql.append("        WHEN (SELECT 1\n");
        sql.append("                FROM VW_PART_OEM_DLR D\n");
        sql.append("               WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("         ROUND(VM.AMOUNT, 2)\n");
        sql.append("        ELSE\n");
        sql.append("         0\n");
        sql.append("      END AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        //sql.append("           AND P.SELLER_ID = "+orgId+"\n");
        sql.append("AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND VM.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  VM,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + b + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND P.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMM\n");
        sql.append("                 WHERE VMM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMM.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.NAME\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * @param consql
     * @param orgId
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> planCountQueryBb_Xxy(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT a.TASK_MONTH, --'2013-09 至 2013-10'\n");
        sql.append("       A.NAME,\n");
        sql.append("       SUM(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / SUM(A.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");

        sql.append("CASE\n");
        sql.append("               WHEN (SELECT 1\n");
        sql.append("                       FROM VW_PART_OEM_DLR D\n");
        sql.append("                      WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("                ROUND(VM.AMOUNT, 2)\n");
        sql.append("               ELSE\n");
        sql.append("                0\n");
        sql.append("             END AMOUNT,\n");

        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        //sql.append("           AND P.SELLER_ID = "+orgId+"\n");
        sql.append("AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND VM.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  VM,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND P.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMM\n");
        sql.append("                 WHERE VMM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMM.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.NAME\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param consql
     * @param orgId
     * @param request
     * @return
     */
    public List<Map<String, Object>> planAmountCountQueryBb_Xxy(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT a.TASK_MONTH, --'2013-09 至 2013-10'\n");
        sql.append("       A.NAME,\n");
        sql.append("       SUM(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / SUM(A.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");

        sql.append("CASE\n");
        sql.append("               WHEN (SELECT 1\n");
        sql.append("                       FROM VW_PART_OEM_DLR D\n");
        sql.append("                      WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("                ROUND(VM.AMOUNT, 2)\n");
        sql.append("               ELSE\n");
        sql.append("                0\n");
        sql.append("             END AMOUNT,\n");

        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        //sql.append("           AND P.SELLER_ID = "+orgId+"\n");
        sql.append("AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND VM.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  VM,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND P.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMM\n");
        sql.append("                 WHERE VMM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMM.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.NAME\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //本部省份（明细）

    /**
     * @param consql
     * @param orgId
     * @param a
     * @param b
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> planPageQueryBb_Sf(String consql, String orgId, String a, String b, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.region_name,\n");
        sql.append("       nvl(sum(a.AMOUNT),0) AMOUNT,\n");
        sql.append("       nvl(sum(a.SELLMONEY),0) SELLMONEY,\n");
        sql.append("CASE\n");
        sql.append("      WHEN SUM(A.AMOUNT) != 0 THEN\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / SUM(A.AMOUNT) * 100, '999.00') || '%'\n");
        sql.append("      ELSE\n");
        sql.append("       '-'\n");
        sql.append("    END RATIO\n");
        sql.append("  from (SELECT VM.TASK_MONTH TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");
        sql.append("CASE\n");
        sql.append("        WHEN (SELECT 1\n");
        sql.append("                FROM VW_PART_OEM_DLR D\n");
        sql.append("               WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("         ROUND(VM.AMOUNT, 2)\n");
        sql.append("        ELSE\n");
        sql.append("         0\n");
        sql.append("      END AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / VM.AMOUNT * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("               WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               tm_region                  r,\n");
        sql.append("               tm_dealer                  t\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        /*sql.append("           AND P.SELLER_ID = "+orgId+"\n"); */
        sql.append("AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("           and t.dealer_id = VM.DEALER_ID\n");
        sql.append("           and t.status = 10011001\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / 1 * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm, tm_region r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + b + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           and vm.province_id = r.region_code\n");
        sql.append("           and vm.status = 10011001\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) a\n");
        sql.append(" group by a.TASK_MONTH, a.region_name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param consql
     * @param orgId
     * @param a
     * @param b
     * @return
     */
    public List<Map<String, Object>> downloadPlanQueryBb_Sf(String consql, String orgId, String a, String b) {
        StringBuffer sql = new StringBuffer();

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.region_name,\n");
        sql.append("       ROUND(sum(a.AMOUNT),2) AMOUNT,\n");
        sql.append("       nvl(sum(a.SELLMONEY),0) SELLMONEY,\n");
        sql.append("CASE\n");
        sql.append("      WHEN SUM(A.AMOUNT) != 0 THEN\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / SUM(A.AMOUNT) * 100, '999.00') || '%'\n");
        sql.append("      ELSE\n");
        sql.append("       '-'\n");
        sql.append("    END RATIO\n");
        sql.append("  from (SELECT VM.TASK_MONTH TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");
        sql.append("CASE\n");
        sql.append("        WHEN (SELECT 1\n");
        sql.append("                FROM VW_PART_OEM_DLR D\n");
        sql.append("               WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("         ROUND(VM.AMOUNT, 2)\n");
        sql.append("        ELSE\n");
        sql.append("         0\n");
        sql.append("      END AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / VM.AMOUNT * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               tm_region                  r,\n");
        sql.append("               tm_dealer                  t\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        //sql.append("           AND P.SELLER_ID = "+orgId+"\n");
        sql.append("AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("           and t.dealer_id = VM.DEALER_ID\n");
        sql.append("           and t.status = 10011001\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / 1 * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm, tm_region r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + b + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           and vm.province_id = r.region_code\n");
        sql.append("           and vm.status = 10011001\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) a\n");
        sql.append(" group by a.TASK_MONTH, a.region_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //本部省份(汇总)
    public PageResult<Map<String, Object>> planCountQueryBb_Sf(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.region_name,\n");
        sql.append("       ROUND(sum(a.AMOUNT),2) AMOUNT,\n");
        sql.append("       nvl(sum(a.SELLMONEY),0) SELLMONEY,\n");
        sql.append("       TO_CHAR(sum(a.SELLMONEY) / sum(a.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  from (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               VM.AMOUNT AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / VM.AMOUNT * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               tm_region                  r,\n");
        sql.append("               tm_dealer                  t\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO(+)\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID(+)\n");
        /*sql.append("           AND P.SELLER_ID = "+orgId+"\n"); */
        sql.append("AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("           and t.dealer_id = VM.DEALER_ID\n");
        sql.append("           and t.status = 10011001\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               0 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / 1 * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm, tm_region r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           and vm.province_id = r.region_code\n");
        sql.append("           and vm.status = 10011001\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) a\n");
        sql.append(" group by a.TASK_MONTH, a.region_name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //本部省份(汇总合计)
    public List<Map<String, Object>> planAmountCountQueryBb_Sf(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.region_name,\n");
        sql.append("       ROUND(sum(a.AMOUNT),2) AMOUNT,\n");
        sql.append("       nvl(sum(a.SELLMONEY),0) SELLMONEY,\n");
        //sql.append("       TO_CHAR(sum(a.SELLMONEY) / sum(a.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");

        sql.append("CASE\n");
        sql.append("        WHEN SUM(A.AMOUNT) <> 0 THEN\n");
        sql.append("         TO_CHAR(SUM(A.SELLMONEY) / SUM(A.AMOUNT) * 100, '999.00') || '%'\n");
        sql.append("        ELSE\n");
        sql.append("         '_'\n");
        sql.append("\n");
        sql.append("      END RATIO\n");

        sql.append("  from (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");

        sql.append("CASE\n");
        sql.append("               WHEN (SELECT 1\n");
        sql.append("                       FROM VW_PART_OEM_DLR D\n");
        sql.append("                      WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("                ROUND(VM.AMOUNT, 2)\n");
        sql.append("               ELSE\n");
        sql.append("                0\n");
        sql.append("             END AMOUNT,\n");

        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / VM.AMOUNT * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               tm_region                  r,\n");
        sql.append("               tm_dealer                  t\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
       /* sql.append("           AND P.SELLER_ID = "+orgId+"\n"); */
        sql.append("AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("           and t.dealer_id = VM.DEALER_ID\n");
        sql.append("           and t.status = 10011001\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / 1 * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm, tm_region r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           and vm.province_id = r.region_code\n");
        sql.append("           and vm.status = 10011001\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) a\n");
        sql.append(" group by a.TASK_MONTH, a.region_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //本部大区（明细）
    public PageResult<Map<String, Object>> planPageQueryBb_Dq(String consql, String orgId, String a, String b, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.root_org_name,\n");
        sql.append("       ROUND(sum(a.AMOUNT),2) AMOUNT,\n");
        sql.append("       nvl(sum(a.SELLMONEY),0) SELLMONEY,\n");

        sql.append("CASE\n");
        sql.append("      WHEN SUM(A.AMOUNT) != 0 THEN\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / SUM(A.AMOUNT) * 100, '999.00') || '%'\n");
        sql.append("      ELSE\n");
        sql.append("       '-'\n");
        sql.append("    END RATIO\n");

        sql.append("  from (SELECT VM.TASK_MONTH TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");

        sql.append("CASE\n");
        sql.append("        WHEN (SELECT 1\n");
        sql.append("                FROM VW_PART_OEM_DLR D\n");
        sql.append("               WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("         VM.AMOUNT \n");
        sql.append("        ELSE\n");
        sql.append("         0\n");
        sql.append("      END AMOUNT,\n");

        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / VM.AMOUNT * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               vw_org_dealer_service      r,\n");
        sql.append("               tm_dealer                  t\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        //sql.append("           AND P.SELLER_ID = "+orgId+"\n");
        sql.append("AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           and t.dealer_id = r.dealer_id\n");
        sql.append("           and t.dealer_id = VM.DEALER_ID\n");
        sql.append("\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               null AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / 1 * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + b + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           and vm.dealer_id = r.dealer_id\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) a\n");
        sql.append(" group by a.TASK_MONTH, a.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //本部大区(明细合计)（下载也是它）
    public List<Map<String, Object>> downloadPlanQueryBb_Dq(String consql, String orgId, String a, String b) {
        StringBuffer sql = new StringBuffer();

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.root_org_name,\n");
        sql.append("       ROUND(sum(a.AMOUNT),2) AMOUNT,\n");

        sql.append("       ROUND(sum(a.SELLMONEY),2) SELLMONEY,\n");

        sql.append("CASE\n");
        sql.append("      WHEN SUM(A.AMOUNT) != 0 THEN\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / SUM(A.AMOUNT) * 100, '999.00') || '%'\n");
        sql.append("      ELSE\n");
        sql.append("       '-'\n");
        sql.append("    END RATIO\n");

        sql.append("  from (SELECT VM.TASK_MONTH TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");

        sql.append("CASE\n");
        sql.append("        WHEN (SELECT 1\n");
        sql.append("                FROM VW_PART_OEM_DLR D\n");
        sql.append("               WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("         VM.AMOUNT \n");
        sql.append("        ELSE\n");
        sql.append("         0\n");
        sql.append("      END AMOUNT,\n");

        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / VM.AMOUNT * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               vw_org_dealer_service      r,\n");
        sql.append("               tm_dealer                  t\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        //sql.append("           AND P.SELLER_ID = "+orgId+"\n");
        sql.append("AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           and t.dealer_id = r.dealer_id\n");
        sql.append("           and t.dealer_id = VM.DEALER_ID\n");
        sql.append("\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / 1 * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + b + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           and vm.dealer_id = r.dealer_id\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) a\n");
        sql.append(" group by a.TASK_MONTH, a.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //本部大区(汇总)
    public PageResult<Map<String, Object>> planCountQueryBb_Dq(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.root_org_name,\n");
        sql.append("       sum(a.AMOUNT) AMOUNT,\n");
        sql.append("       sum(a.SELLMONEY) SELLMONEY,\n");
        sql.append("       TO_CHAR(sum(a.SELLMONEY) / sum(a.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  from (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               VM.AMOUNT AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / VM.AMOUNT * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               vw_org_dealer_service      r,\n");
        sql.append("               tm_dealer                  t\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO(+)\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID(+)\n");
        //sql.append("           AND P.SELLER_ID = "+orgId+"\n");
        sql.append("AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           and t.dealer_id = r.dealer_id\n");
        sql.append("           and t.dealer_id = VM.DEALER_ID\n");
        sql.append("\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               null AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / 1 * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           and vm.dealer_id = r.dealer_id\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) a\n");
        sql.append(" group by a.TASK_MONTH, a.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //本部大区(汇总合计)
    public List<Map<String, Object>> planAmountCountQueryBb_Dq(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("select a.TASK_MONTH,\n");
        sql.append("       a.root_org_name,\n");
        sql.append("       sum(a.AMOUNT) AMOUNT,\n");
        sql.append("       sum(a.SELLMONEY) SELLMONEY,\n");
        //sql.append("       TO_CHAR(sum(a.SELLMONEY) / sum(a.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");

        sql.append("CASE\n");
        sql.append("        WHEN SUM(A.AMOUNT) <> 0 THEN\n");
        sql.append("         TO_CHAR(SUM(A.SELLMONEY) / SUM(A.AMOUNT) * 100, '999.00') || '%'\n");
        sql.append("        ELSE\n");
        sql.append("         '_'\n");
        sql.append("\n");
        sql.append("      END RATIO\n");

        sql.append("  from (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        //sql.append("               VM.AMOUNT AMOUNT,\n");

        sql.append("CASE\n");
        sql.append("               WHEN (SELECT 1\n");
        sql.append("                       FROM VW_PART_OEM_DLR D\n");
        sql.append("                      WHERE D.CHILDORG_ID = VM.DEALER_ID) = 1 THEN\n");
        sql.append("                ROUND(VM.AMOUNT, 2)\n");
        sql.append("               ELSE\n");
        sql.append("                0\n");
        sql.append("             END AMOUNT,\n");

        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / VM.AMOUNT * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("(SELECT PS.MONTH_NO, PS.DEALER_ID, PS.AMOUNT\n");
        sql.append("                 FROM VW_PART_DLR_MONTH_PURCHASE PS\n");
        sql.append("                WHERE PS.SELLER_ID = " + orgId + ") P,\n");
        sql.append("               vw_org_dealer_service      r,\n");
        sql.append("               tm_dealer                  t\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
       /* sql.append("           AND P.SELLER_ID = "+orgId+"\n");*/
        sql.append("AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           and t.dealer_id = r.dealer_id\n");
        sql.append("           and t.dealer_id = VM.DEALER_ID\n");
        sql.append("\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("        --TO_CHAR(P.AMOUNT / 1 * 100, '999.00') || '%' AS RATIO\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append("           AND P.SELLER_ID = " + orgId + "\n");
        sql.append(consql);
        sql.append("           and vm.dealer_id = r.dealer_id\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) a\n");
        sql.append(" group by a.TASK_MONTH, a.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //供应中心查询分页 查明细
    public PageResult<Map<String, Object>> planPageQueryGyzx(String consql, String orgId, String a, String b, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();


        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.SELLER_CODE,\n");
        sql.append("       A.SELLER_NAME,\n");
        sql.append("       A.DEALER_ID,\n");
        sql.append("       A.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       A.AMOUNT,\n");
        sql.append("       ROUND(SUM(A.SELLMONEY),2) SELLMONEY,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL THEN\n");
        sql.append("          TO_CHAR(SUM(A.SELLMONEY) / NVL(A.AMOUNT, 0.001) * 100, '999.00') || '%'\n");
        sql.append("         ELSE\n");
        sql.append("          '-'\n");
        sql.append("       END RATIO\n");
        sql.append("  FROM (SELECT VM.TASK_MONTH,\n");
        sql.append("      VM.DEALER_ID,\n");
        sql.append("      VM.DEALER_CODE,\n");
        sql.append("      VM.DEALER_NAME,\n");
        sql.append("      SR.PARENTORG_ID SELLER_ID,\n");
        sql.append("      VM.AMOUNT AMOUNT,\n");
        sql.append("      nvl((SELECT P.AMOUNT\n");
        sql.append("         FROM VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("        WHERE P.MONTH_NO = VM.TASK_MONTH\n");
        sql.append("          AND P.DEALER_ID = SR.CHILDORG_ID\n");
        sql.append("          AND P.SELLER_ID = SR.PARENTORG_ID\n");
        sql.append("          AND P.SELLER_ID <> 2010010100070674),0) SELLMONEY,\n");
        sql.append("      SR.PARENTORG_CODE SELLER_CODE,\n");
        sql.append("      SR.PARENTORG_NAME SELLER_NAME\n");
        sql.append(" FROM VW_PART_DLR_TASK_MONTH VM, TT_PART_SALES_RELATION SR\n");
        sql.append("WHERE VM.DEALER_ID = SR.CHILDORG_ID\n");
        sql.append("           AND SR.PARENTORG_ID <> " + orgId + "\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO,\n");
        sql.append("               vm.DEALER_ID,\n");
        sql.append("               vm.DEALER_CODE,\n");
        sql.append("               vm.DEALER_NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               NULL AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY,\n");
        sql.append("              TD.DEALER_CODE SELLER_CODE,\n");
        sql.append("          TD.DEALER_NAME SELLER_NAME\n");
        sql.append("     FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER VM, TM_DEALER TD\n");
        sql.append("    WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("      AND P.SELLER_ID = TD.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("              --AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND P.SELLER_ID <> " + orgId + "\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");

        sql.append("GROUP BY A.TASK_MONTH,\n");
        sql.append("         A.SELLER_CODE,\n");
        sql.append("         A.SELLER_NAME,\n");
        sql.append("         A.DEALER_ID,\n");
        sql.append("         A.DEALER_CODE,\n");
        sql.append("         A.DEALER_NAME,\n");
        sql.append("         A.AMOUNT\n");


        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public List<Map<String, Object>> downloadPlanQueryGyzx(String consql, String orgId, String a, String b) {
        StringBuffer sql = new StringBuffer();


        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.SELLER_CODE,\n");
        sql.append("       A.SELLER_NAME,\n");
        sql.append("       A.DEALER_ID,\n");
        sql.append("       A.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       A.AMOUNT,\n");
        sql.append("       ROUND(SUM(A.SELLMONEY), 2) SELLMONEY,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL THEN\n");
        sql.append("          TO_CHAR(SUM(A.SELLMONEY) / NVL(A.AMOUNT, 0.001) * 100, '999.00') || '%'\n");
        sql.append("         ELSE\n");
        sql.append("          '-'\n");
        sql.append("       END RATIO\n");
        sql.append("  FROM (SELECT VM.TASK_MONTH,\n");
        sql.append("      VM.DEALER_ID,\n");
        sql.append("      VM.DEALER_CODE,\n");
        sql.append("      VM.DEALER_NAME,\n");
        sql.append("      SR.PARENTORG_ID SELLER_ID,\n");
        sql.append("      VM.AMOUNT AMOUNT,\n");
        sql.append("      nvl((SELECT P.AMOUNT\n");
        sql.append("         FROM VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("        WHERE P.MONTH_NO = VM.TASK_MONTH\n");
        sql.append("          AND P.DEALER_ID = SR.CHILDORG_ID\n");
        sql.append("          AND P.SELLER_ID = SR.PARENTORG_ID\n");
        sql.append("          AND P.SELLER_ID <> 2010010100070674),0) SELLMONEY,\n");
        sql.append("      SR.PARENTORG_CODE SELLER_CODE,\n");
        sql.append("      SR.PARENTORG_NAME SELLER_NAME\n");
        sql.append(" FROM VW_PART_DLR_TASK_MONTH VM, TT_PART_SALES_RELATION SR\n");
        sql.append("WHERE VM.DEALER_ID = SR.CHILDORG_ID\n");
        sql.append("           AND SR.PARENTORG_ID <> " + orgId + "\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO,\n");
        sql.append("               vm.DEALER_ID,\n");
        sql.append("               vm.DEALER_CODE,\n");
        sql.append("               vm.DEALER_NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY,\n");
        sql.append("             TD.DEALER_CODE SELLER_CODE,\n");
        sql.append("             TD.DEALER_NAME SELLER_NAME\n");
        sql.append("        FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER VM, TM_DEALER TD\n");
        sql.append("       WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("         AND P.SELLER_ID = TD.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("              --AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND P.SELLER_ID <> " + orgId + "\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");

        sql.append("GROUP BY A.TASK_MONTH,\n");
        sql.append("         A.SELLER_CODE,\n");
        sql.append("         A.SELLER_NAME,\n");
        sql.append("         A.DEALER_ID,\n");
        sql.append("         A.DEALER_CODE,\n");
        sql.append("         A.DEALER_NAME,\n");
        sql.append("         A.AMOUNT\n");


        return pageQuery(sql.toString(), null, getFunName());
    }


    public PageResult<Map<String, Object>> planCountQueryGyzx(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT AA.TASK_MONTH,AA.DEALER_ID,\n");
        sql.append("     AA.SELLER_CODE,\n");
        sql.append("     AA.SELLER_NAME,\n");
        sql.append("       AA.DEALER_CODE,\n");
        sql.append("       AA.DEALER_NAME,\n");
        sql.append("       SUM(AA.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(SELLMONEY) SELLMONEY,\n");
        sql.append("       /*SUM(BB_AMOUNT) BB_AMOUNT,\n");
        sql.append("                     TO_CHAR(SUM(BB_AMOUNT) /\n");
        sql.append("                             DECODE(SUM(SELLMONEY), 0, 1, SUM(SELLMONEY)) * 100,\n");
        sql.append("                             '999.00') || '%' BB_RATIO,*/\n");
        sql.append("       SUM(GY_AMOUNT) GY_AMOUNT,\n");
        sql.append("       TO_CHAR(SUM(GY_AMOUNT) /\n");
        sql.append("               DECODE(SUM(SELLMONEY), 0, 1, SUM(SELLMONEY)) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(SELLMONEY) / SUM(AA.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT A.TASK_MONTH,\n");
        sql.append("             A.SELLER_CODE,\n");
        sql.append("             A.SELLER_NAME,\n");
        sql.append("               A.DEALER_ID,\n");
        sql.append("               A.DEALER_CODE,\n");
        sql.append("               A.DEALER_NAME,\n");
        sql.append("               A.AMOUNT,\n");
        sql.append("               SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("               /*NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                   0) BB_AMOUNT,*/\n");
        sql.append("               (SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) GY_AMOUNT\n");
        sql.append("          FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,--VM.TASK_MONTH,\n");
        sql.append("                       VM.DEALER_ID,\n");
        sql.append("                       VM.DEALER_CODE,\n");
        sql.append("                       VM.DEALER_NAME,\n");
        sql.append("                       P.SELLER_ID,\n");
        sql.append("                       VM.AMOUNT,\n");
        sql.append("                       P.AMOUNT SELLMONEY,\n");
        sql.append("                     TD.DEALER_CODE SELLER_CODE,\n");
        sql.append("                     TD.DEALER_NAME SELLER_NAME\n");
        sql.append("                FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("                     TM_DEALER                  TD,\n");
        sql.append("                     VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("               WHERE VM.TASK_MONTH(+) = P.MONTH_NO\n");
        sql.append("                 AND VM.DEALER_ID(+) = P.DEALER_ID\n");
        sql.append("                 AND P.SELLER_ID = TD.DEALER_ID\n");

        sql.append("                      --AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("                   AND P.SELLER_ID <> " + orgId + "\n");
        sql.append("                   AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("                   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("                UNION ALL\n");
        sql.append("                SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,--P.MONTH_NO,\n");
        sql.append("                       vm.DEALER_ID,\n");
        sql.append("                       vm.DEALER_CODE,\n");
        sql.append("                       vm.DEALER_NAME,\n");
        sql.append("                       P.SELLER_ID,\n");
        sql.append("                       NULL,\n");
        sql.append("                       P.AMOUNT SELLMONEY,\n");
        sql.append("                     TD.DEALER_CODE SELLER_CODE,\n");
        sql.append("                     TD.DEALER_NAME SELLER_NAME\n");
        sql.append("                FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("                     TM_DEALER                  VM,\n");
        sql.append("                     TM_DEALER                  TD\n");
        sql.append("               WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("                 AND P.SELLER_ID = TD.DEALER_ID\n");

        sql.append("                   AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("                   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("                      --AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("                   AND P.SELLER_ID <> " + orgId + "\n");
        sql.append("                   AND NOT EXISTS\n");
        sql.append("                 (SELECT 1\n");
        sql.append("                          FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                         WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                           AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append("         GROUP BY A.TASK_MONTH,\n");
        sql.append("                  A.SELLER_CODE,\n");
        sql.append("                  A.SELLER_NAME,\n");
        sql.append("                  A.DEALER_ID,\n");
        sql.append("                  A.DEALER_CODE,\n");
        sql.append("                  A.DEALER_NAME,\n");
        sql.append("                  A.AMOUNT) AA\n");

        sql.append("GROUP BY AA.TASK_MONTH,\n");
        sql.append("         AA.SELLER_CODE,\n");
        sql.append("         AA.SELLER_NAME,\n");
        sql.append("         AA.DEALER_ID,\n");
        sql.append("         AA.DEALER_CODE,\n");
        sql.append("         AA.DEALER_NAME\n");


        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public List<Map<String, Object>> planAmountCountQueryGyzx(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT AA.DEALER_ID,\n");
        sql.append("       AA.DEALER_CODE,\n");
        sql.append("       AA.DEALER_NAME,\n");
        sql.append("       SUM(AA.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(SELLMONEY) SELLMONEY,\n");
        sql.append("       /*SUM(BB_AMOUNT) BB_AMOUNT,\n");
        sql.append("                     TO_CHAR(SUM(BB_AMOUNT) /\n");
        sql.append("                             DECODE(SUM(SELLMONEY), 0, 1, SUM(SELLMONEY)) * 100,\n");
        sql.append("                             '999.00') || '%' BB_RATIO,*/\n");
        sql.append("       SUM(GY_AMOUNT) GY_AMOUNT,\n");
        sql.append("       TO_CHAR(SUM(GY_AMOUNT) /\n");
        sql.append("               DECODE(SUM(SELLMONEY), 0, 1, SUM(SELLMONEY)) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(SELLMONEY) / SUM(AA.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT A.TASK_MONTH,\n");
        sql.append("               A.DEALER_ID,\n");
        sql.append("               A.DEALER_CODE,\n");
        sql.append("               A.DEALER_NAME,\n");
        sql.append("               A.AMOUNT,\n");
        sql.append("               SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("               /*NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                   0) BB_AMOUNT,*/\n");
        sql.append("               (SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) GY_AMOUNT\n");
        sql.append("          FROM (SELECT VM.TASK_MONTH,\n");
        sql.append("                       VM.DEALER_ID,\n");
        sql.append("                       VM.DEALER_CODE,\n");
        sql.append("                       VM.DEALER_NAME,\n");
        sql.append("                       P.SELLER_ID,\n");
        sql.append("                       VM.AMOUNT AMOUNT,\n");
        sql.append("                       P.AMOUNT SELLMONEY\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("                       VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                 WHERE VM.TASK_MONTH(+) = P.MONTH_NO\n");
        sql.append("                   AND VM.DEALER_ID(+) = P.DEALER_ID\n");
        sql.append("                      --AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("                   AND P.SELLER_ID <> " + orgId + "\n");
        sql.append("                   AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("                   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("                UNION ALL\n");
        sql.append("                SELECT P.MONTH_NO,\n");
        sql.append("                       vm.DEALER_ID,\n");
        sql.append("                       vm.DEALER_CODE,\n");
        sql.append("                       vm.DEALER_NAME,\n");
        sql.append("                       P.SELLER_ID,\n");
        sql.append("                       0.001 AMOUNT,\n");
        sql.append("                       P.AMOUNT SELLMONEY\n");
        sql.append("                  FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm\n");
        sql.append("                 WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("                   AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("                   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("                      --AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("                   AND P.SELLER_ID <> " + orgId + "\n");
        sql.append("                   AND NOT EXISTS\n");
        sql.append("                 (SELECT 1\n");
        sql.append("                          FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                         WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                           AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append("         GROUP BY A.TASK_MONTH,\n");
        sql.append("                  A.DEALER_ID,\n");
        sql.append("                  A.DEALER_CODE,\n");
        sql.append("                  A.DEALER_NAME,\n");
        sql.append("                  A.AMOUNT) AA\n");
        sql.append(" GROUP BY AA.DEALER_ID, AA.DEALER_CODE, AA.DEALER_NAME\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public PageResult<Map<String, Object>> planPageQueryZz(String consql, String orgId, String a, String b, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.DEALER_ID,\n");
        sql.append("       A.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       A.AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN A.AMOUNT IS NULL THEN\n");
        sql.append("          '-'\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL AND  NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)), 0) > 0 THEN\n");
        sql.append("          TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                      0) / NVL(SUM(A.SELLMONEY), 1) * 100,\n");
        sql.append("                  '999.00') || '%'\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL AND  NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)), 0) = 0 THEN\n");
        sql.append("          '-'\n");
        sql.append("       END BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN A.AMOUNT IS NULL THEN\n");
        sql.append("          '-'\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL AND\n");
        sql.append("              (SUM(A.SELLMONEY) -\n");
        sql.append("              NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                   0)) > 0 THEN\n");
        sql.append("          TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("                  NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                       0)) / NVL(SUM(A.SELLMONEY), 1) * 100,\n");
        sql.append("                  '999.00') || '%'\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL AND\n");
        sql.append("              (SUM(A.SELLMONEY) -\n");
        sql.append("              NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                   0)) = 0 THEN\n");
        sql.append("          '-'\n");
        sql.append("       END GY_RATIO,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN A.AMOUNT IS NULL THEN\n");
        sql.append("          '-'\n");
        sql.append("         ELSE\n");
        sql.append("          TO_CHAR(SUM(A.SELLMONEY) / NVL(A.AMOUNT, 0.001) * 100, '999.00') || '%'\n");
        sql.append("       END AS RATIO\n");

        sql.append("  FROM (SELECT VM.TASK_MONTH,\n");
        sql.append("               VM.DEALER_ID,\n");
        sql.append("               VM.DEALER_CODE,\n");
        sql.append("               VM.DEALER_NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH VM, VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);

        sql.append("      UNION ALL\n");
        sql.append("     SELECT VM.TASK_MONTH,\n");
        sql.append("            VM.DEALER_ID,\n");
        sql.append("            VM.DEALER_CODE,\n");
        sql.append("            VM.DEALER_NAME,\n");
        sql.append("            0              SELLER_ID,\n");
        sql.append("            VM.AMOUNT,\n");
        sql.append("            0              SELLMONEY\n");
        sql.append("       FROM VW_PART_DLR_TASK_MONTH VM\n");
        sql.append("      WHERE VM.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("        AND NOT EXISTS (SELECT 1\n");
        sql.append("               FROM VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("              WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("                AND P.MONTH_NO = VM.TASK_MONTH)\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO,\n");
        sql.append("               vm.DEALER_ID,\n");
        sql.append("               vm.DEALER_CODE,\n");
        sql.append("               vm.DEALER_NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               NULL,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.IS_NBDW != 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.DEALER_ID, A.DEALER_CODE, A.DEALER_NAME, A.AMOUNT\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public List<Map<String, Object>> downloadPlanQueryZz(String consql, String orgId, String a, String b) {
        StringBuffer sql = new StringBuffer();


        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.DEALER_ID,\n");
        sql.append("       A.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       NVL(A.AMOUNT, '0') AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN A.AMOUNT IS NULL THEN\n");
        sql.append("          '-'\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL AND  NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)), 0) > 0 THEN\n");
        sql.append("          TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                      0) / NVL(SUM(A.SELLMONEY), 1) * 100,\n");
        sql.append("                  '999.00') || '%'\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL AND  NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)), 0) = 0 THEN\n");
        sql.append("          '-'\n");
        sql.append("       END BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN A.AMOUNT IS NULL THEN\n");
        sql.append("          '-'\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL AND\n");
        sql.append("              (SUM(A.SELLMONEY) -\n");
        sql.append("              NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                   0)) > 0 THEN\n");
        sql.append("          TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("                  NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                       0)) / NVL(SUM(A.SELLMONEY), 1) * 100,\n");
        sql.append("                  '999.00') || '%'\n");
        sql.append("         WHEN A.AMOUNT IS NOT NULL AND\n");
        sql.append("              (SUM(A.SELLMONEY) -\n");
        sql.append("              NVL(SUM(DECODE(A.SELLER_ID, 2010010100070674, A.SELLMONEY)),\n");
        sql.append("                   0)) = 0 THEN\n");
        sql.append("          '-'\n");
        sql.append("       END GY_RATIO,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN A.AMOUNT IS NULL THEN\n");
        sql.append("          '-'\n");
        sql.append("         ELSE\n");
        sql.append("          TO_CHAR(SUM(A.SELLMONEY) / NVL(A.AMOUNT, 0.001) * 100, '999.00') || '%'\n");
        sql.append("       END AS RATIO\n");

        sql.append("  FROM (SELECT VM.TASK_MONTH,\n");
        sql.append("               VM.DEALER_ID,\n");
        sql.append("               VM.DEALER_CODE,\n");
        sql.append("               VM.DEALER_NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH VM, VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);

        sql.append("UNION ALL\n");
        sql.append("     SELECT VM.TASK_MONTH,\n");
        sql.append("            VM.DEALER_ID,\n");
        sql.append("            VM.DEALER_CODE,\n");
        sql.append("            VM.DEALER_NAME,\n");
        sql.append("            0              SELLER_ID,\n");
        sql.append("            VM.AMOUNT,\n");
        sql.append("            0              SELLMONEY\n");
        sql.append("       FROM VW_PART_DLR_TASK_MONTH VM\n");
        sql.append("      WHERE VM.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("        AND NOT EXISTS (SELECT 1\n");
        sql.append("               FROM VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("              WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("                AND P.MONTH_NO = VM.TASK_MONTH)\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO,\n");
        sql.append("               vm.DEALER_ID,\n");
        sql.append("               vm.DEALER_CODE,\n");
        sql.append("               vm.DEALER_NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               NULL,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND VM.IS_NBDW != 1\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.DEALER_ID, A.DEALER_CODE, A.DEALER_NAME, A.AMOUNT\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public PageResult<Map<String, Object>> planCountQueryZz(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT aa.TASK_MONTH,AA.DEALER_ID,\n");
        sql.append("       AA.DEALER_CODE,\n");
        sql.append("       AA.DEALER_NAME,\n");
        sql.append("       SUM(AA.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(SELLMONEY) SELLMONEY,\n");
        sql.append("       SUM(BB_AMOUNT) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(SUM(BB_AMOUNT) /\n");
        sql.append("               DECODE(SUM(SELLMONEY), 0, 1, SUM(SELLMONEY)) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       SUM(GY_AMOUNT) GY_AMOUNT,\n");
        sql.append("       TO_CHAR(SUM(GY_AMOUNT) /\n");
        sql.append("               DECODE(SUM(SELLMONEY), 0, 1, SUM(SELLMONEY)) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(SELLMONEY) / SUM(AA.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT A.TASK_MONTH,\n");
        sql.append("               A.DEALER_ID,\n");
        sql.append("               A.DEALER_CODE,\n");
        sql.append("               A.DEALER_NAME,\n");
        sql.append("               A.AMOUNT,\n");
        sql.append("               SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) BB_AMOUNT,\n");
        sql.append("               (SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) GY_AMOUNT\n");
        sql.append("          FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,--VM.TASK_MONTH,\n");
        sql.append("                       VM.DEALER_ID,\n");
        sql.append("                       VM.DEALER_CODE,\n");
        sql.append("                       VM.DEALER_NAME,\n");
        sql.append("                       P.SELLER_ID,\n");
        sql.append("                       VM.AMOUNT,\n");
        sql.append("                       P.AMOUNT SELLMONEY\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("                       VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                 WHERE VM.TASK_MONTH(+) = P.MONTH_NO\n");
        sql.append("                   AND VM.DEALER_ID(+) = P.DEALER_ID\n");
        sql.append("                   AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("                   AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("                   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);

        sql.append("UNION ALL\n");
        sql.append("            SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,--P.MONTH_NO,\n");
        sql.append("            VM.DEALER_ID,\n");
        sql.append("            VM.DEALER_CODE,\n");
        sql.append("            VM.DEALER_NAME,\n");
        sql.append("            0              SELLER_ID,\n");
        sql.append("            VM.AMOUNT,\n");
        sql.append("            0              SELLMONEY\n");
        sql.append("       FROM VW_PART_DLR_TASK_MONTH VM\n");
        sql.append("      WHERE VM.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("        AND NOT EXISTS (SELECT 1\n");
        sql.append("               FROM VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("              WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("                AND P.MONTH_NO = VM.TASK_MONTH)\n");
        sql.append("        AND VM.TASK_MONTH >= '2013-11'\n");
        sql.append("        AND VM.TASK_MONTH <= '2013-11'\n");
        sql.append(consql);
        sql.append("                UNION ALL\n");
        sql.append("                SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,--P.MONTH_NO,\n");
        sql.append("                       vm.DEALER_ID,\n");
        sql.append("                       vm.DEALER_CODE,\n");
        sql.append("                       vm.DEALER_NAME,\n");
        sql.append("                       P.SELLER_ID,\n");
        sql.append("                       NULL,\n");
        sql.append("                       P.AMOUNT SELLMONEY\n");
        sql.append("                  FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm\n");
        sql.append("                 WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("                   AND VM.IS_NBDW != 1\n");
        sql.append("                   AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("                   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("                   AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("                   AND NOT EXISTS\n");
        sql.append("                 (SELECT 1\n");
        sql.append("                          FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                         WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                           AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append("         GROUP BY A.TASK_MONTH,\n");
        sql.append("                  A.DEALER_ID,\n");
        sql.append("                  A.DEALER_CODE,\n");
        sql.append("                  A.DEALER_NAME,\n");
        sql.append("                  A.AMOUNT) AA\n");
        sql.append(" GROUP BY aa.TASK_MONTH,AA.DEALER_ID, AA.DEALER_CODE, AA.DEALER_NAME\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public List<Map<String, Object>> planAmountCountQueryZz(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT AA.DEALER_ID,\n");
        sql.append("       AA.DEALER_CODE,\n");
        sql.append("       AA.DEALER_NAME,\n");
        sql.append("       SUM(AA.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(SELLMONEY) SELLMONEY,\n");
        sql.append("       SUM(BB_AMOUNT) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(SUM(BB_AMOUNT) /\n");
        sql.append("               DECODE(SUM(SELLMONEY), 0, 1, SUM(SELLMONEY)) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       SUM(GY_AMOUNT) GY_AMOUNT,\n");
        sql.append("       TO_CHAR(SUM(GY_AMOUNT) /\n");
        sql.append("               DECODE(SUM(SELLMONEY), 0, 1, SUM(SELLMONEY)) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(SELLMONEY) / SUM(AA.AMOUNT) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT A.TASK_MONTH,\n");
        sql.append("               A.DEALER_ID,\n");
        sql.append("               A.DEALER_CODE,\n");
        sql.append("               A.DEALER_NAME,\n");
        sql.append("               A.AMOUNT,\n");
        sql.append("               SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) BB_AMOUNT,\n");
        sql.append("               (SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) GY_AMOUNT\n");
        sql.append("          FROM (SELECT VM.TASK_MONTH,\n");
        sql.append("                       VM.DEALER_ID,\n");
        sql.append("                       VM.DEALER_CODE,\n");
        sql.append("                       VM.DEALER_NAME,\n");
        sql.append("                       P.SELLER_ID,\n");
        sql.append("                       VM.AMOUNT AMOUNT,\n");
        sql.append("                       P.AMOUNT SELLMONEY\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("                       VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                 WHERE VM.TASK_MONTH(+) = P.MONTH_NO\n");
        sql.append("                   AND VM.DEALER_ID(+) = P.DEALER_ID\n");
        sql.append("                   AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("                   AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("                   AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);

        sql.append("     UNION ALL\n");
        sql.append("     SELECT VM.TASK_MONTH,\n");
        sql.append("            VM.DEALER_ID,\n");
        sql.append("            VM.DEALER_CODE,\n");
        sql.append("            VM.DEALER_NAME,\n");
        sql.append("            0              SELLER_ID,\n");
        sql.append("            VM.AMOUNT,\n");
        sql.append("            0              SELLMONEY\n");
        sql.append("       FROM VW_PART_DLR_TASK_MONTH VM\n");
        sql.append("      WHERE VM.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("        AND NOT EXISTS (SELECT 1\n");
        sql.append("               FROM VW_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("              WHERE P.DEALER_ID = VM.DEALER_ID\n");
        sql.append("                AND P.MONTH_NO = VM.TASK_MONTH)\n");
        sql.append("                AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("                AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("                UNION ALL\n");
        sql.append("                SELECT P.MONTH_NO,\n");
        sql.append("                       vm.DEALER_ID,\n");
        sql.append("                       vm.DEALER_CODE,\n");
        sql.append("                       vm.DEALER_NAME,\n");
        sql.append("                       P.SELLER_ID,\n");
        sql.append("                       0.001 AMOUNT,\n");
        sql.append("                       P.AMOUNT SELLMONEY\n");
        sql.append("                  FROM VW_PART_DLR_MONTH_PURCHASE P, TM_DEALER vm\n");
        sql.append("                 WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("                   AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("                   AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append("                   AND VM.IS_NBDW != 1\n");
        sql.append(consql);
        sql.append("                   AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("                   AND NOT EXISTS\n");
        sql.append("                 (SELECT 1\n");
        sql.append("                          FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                         WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                           AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append("         GROUP BY A.TASK_MONTH,\n");
        sql.append("                  A.DEALER_ID,\n");
        sql.append("                  A.DEALER_CODE,\n");
        sql.append("                  A.DEALER_NAME,\n");
        sql.append("                  A.AMOUNT) AA\n");
        sql.append(" GROUP BY AA.DEALER_ID, AA.DEALER_CODE, AA.DEALER_NAME\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //终端销售员（明细）
    public PageResult<Map<String, Object>> planPageQueryZz_Xxy(String consql, String orgId, String a, String b, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.NAME,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT VM.TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND VM.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO, U.NAME, P.SELLER_ID, NULL, P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND P.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND VM.IS_NBDW != 1\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.NAME\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //终端销售员（明细合计）（下载也是它）
    public List<Map<String, Object>> downloadPlanQueryZz_Xxy(String consql, String orgId, String a, String b) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.NAME,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT VM.TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND VM.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT P.MONTH_NO, U.NAME, P.SELLER_ID, NULL, P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND P.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND VM.IS_NBDW != 1\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.NAME\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //终端销售员(汇总)
    public PageResult<Map<String, Object>> planCountQueryZz_Xxy(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);


        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.NAME,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND VM.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("         /*  and not exists (select 1\n");
        sql.append("                  from tt_part_userpose_define u\n");
        sql.append("                 where u.user_id = sp.user_id\n");
        sql.append("                   and u.user_type in (6, 5))*/\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH, U.NAME, P.SELLER_ID, NULL, P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND P.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("         /*  and not exists (select 1\n");
        sql.append("                  from tt_part_userpose_define u\n");
        sql.append("                 where u.user_id = sp.user_id\n");
        sql.append("                   and u.user_type in (6, 5))*/\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.NAME\n");


        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //终端销售员(汇总合计)
    public List<Map<String, Object>> planAmountCountQueryZz_Xxy(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.NAME,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               U.NAME,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT amount,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND VM.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("         /*  and not exists (select 1\n");
        sql.append("                  from tt_part_userpose_define u\n");
        sql.append("                 where u.user_id = sp.user_id\n");
        sql.append("                   and u.user_type in (6, 5))*/\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH, U.NAME, P.SELLER_ID, 0.001 AMOUNT, P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               TC_USER                    U,\n");
        sql.append("               TT_PART_SALESSCOPE_DEFINE  SP\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND U.USER_ID = SP.USER_ID\n");
        sql.append("           AND P.DEALER_ID = SP.DEALER_ID\n");
        sql.append("           AND SP.USER_TYPE = 3\n");
        sql.append("           AND SP.STATUS = 1\n");
        sql.append("         /*  and not exists (select 1\n");
        sql.append("                  from tt_part_userpose_define u\n");
        sql.append("                 where u.user_id = sp.user_id\n");
        sql.append("                   and u.user_type in (6, 5))*/\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, A.NAME\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //终端省份（明细）
    public PageResult<Map<String, Object>> planPageQueryZz_Sf(String consql, String orgId, String a, String b, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();


        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.region_name,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT vm.TASK_MONTH TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               tm_region                  r\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           and t.status = 10011001\n");
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT p.MONTH_NO TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               NULL,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               tm_region                  r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           and t.status = 10011001\n");
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("           AND VM.IS_NBDW != 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, a.region_name\n");


        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //终端省份(明细合计)（下载也是它）
    public List<Map<String, Object>> downloadPlanQueryZz_Sf(String consql, String orgId, String a, String b) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.region_name,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT vm.TASK_MONTH TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               tm_region                  r\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           and t.status = 10011001\n");
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT p.MONTH_NO TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               NULL,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               tm_region                  r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           and t.status = 10011001\n");
        sql.append("           AND VM.IS_NBDW != 1\n");
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, a.region_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //终端省份(汇总)
    public PageResult<Map<String, Object>> planCountQueryZz_Sf(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.region_name,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               tm_region                  r\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           and t.status = 10011001\n");
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               NULL,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               tm_region                  r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           and t.status = 10011001\n");
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, a.region_name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //终端省份(汇总合计)
    public List<Map<String, Object>> planAmountCountQueryZz_Sf(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.region_name,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               tm_region                  r\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           and t.status = 10011001\n");
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.region_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               NULL,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               tm_region                  r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           and t.status = 10011001\n");
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.province_id = r.region_code\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, a.region_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //终端大区（明细）
    public PageResult<Map<String, Object>> planPageQueryZz_Dq(String consql, String orgId, String a, String b, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();


        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.root_org_name,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT vm.TASK_MONTH TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.dealer_id = r.dealer_id\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT p.MONTH_NO TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               NULL,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           and vm.dealer_id = r.dealer_id\n");
        sql.append("           AND VM.IS_NBDW != 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, a.root_org_name\n");


        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //终端大区(明细合计)（下载也是它）
    public List<Map<String, Object>> downloadPlanQueryZz_Dq(String consql, String orgId, String a, String b) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.root_org_name,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT vm.TASK_MONTH TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + a + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.dealer_id = r.dealer_id\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT p.MONTH_NO TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + a + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + b + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           and vm.dealer_id = r.dealer_id\n");
        sql.append("           AND VM.IS_NBDW != 1\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, a.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //终端大区(汇总)
    public PageResult<Map<String, Object>> planCountQueryZz_Dq(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.root_org_name,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.dealer_id = r.dealer_id\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               NULL,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           and vm.dealer_id = r.dealer_id\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, a.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //终端大区(汇总合计)
    public List<Map<String, Object>> planAmountCountQueryZz_Dq(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        String aa = year + "-" + simMonth(month);
        String bb = year + "-" + simMonth(month2);

        sql.append("SELECT A.TASK_MONTH,\n");
        sql.append("       A.root_org_name,\n");
        sql.append("       sum(A.AMOUNT) AMOUNT,\n");
        sql.append("       SUM(A.SELLMONEY) SELLMONEY,\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0) BB_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                   0) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' BB_RATIO,\n");
        sql.append("       (SUM(A.SELLMONEY) -\n");
        sql.append("       NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)), 0)) GY_AMOUNT,\n");
        sql.append("       TO_CHAR((SUM(A.SELLMONEY) -\n");
        sql.append("               NVL(SUM(DECODE(A.SELLER_ID, " + orgId + ", A.SELLMONEY)),\n");
        sql.append("                    0)) / NVL(sum(A.AMOUNT), 0.001) * 100,\n");
        sql.append("               '999.00') || '%' GY_RATIO,\n");
        sql.append("       TO_CHAR(SUM(A.SELLMONEY) / NVL(sum(A.AMOUNT), 0.001) * 100, '999.00') || '%' AS RATIO\n");
        sql.append("  FROM (SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               VM.AMOUNT AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_TASK_MONTH     VM,\n");
        sql.append("               VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               tm_dealer                  t,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE VM.TASK_MONTH = P.MONTH_NO\n");
        sql.append("           AND VM.DEALER_ID = P.DEALER_ID\n");
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           AND VM.TASK_MONTH >= '" + aa + "'\n");
        sql.append("           AND VM.TASK_MONTH <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           and t.dealer_id = vm.dealer_id\n");
        sql.append("           and t.dealer_id = r.dealer_id\n");
        sql.append("        UNION ALL\n");
        sql.append("        SELECT '" + year + "-" + month + " 至 " + year + "-" + month2 + "' TASK_MONTH,\n");
        sql.append("               r.root_org_name,\n");
        sql.append("               P.SELLER_ID,\n");
        sql.append("               0.001 AMOUNT,\n");
        sql.append("               P.AMOUNT SELLMONEY\n");
        sql.append("          FROM VW_PART_DLR_MONTH_PURCHASE P,\n");
        sql.append("               TM_DEALER                  vm,\n");
        sql.append("               vw_org_dealer_service      r\n");
        sql.append("         WHERE P.DEALER_ID = vm.DEALER_ID\n");
        sql.append("           AND P.MONTH_NO >= '" + aa + "'\n");
        sql.append("           AND P.MONTH_NO <= '" + bb + "'\n");
        sql.append(consql);
        sql.append("           AND P.DEALER_CODE NOT LIKE '%G%'\n");
        sql.append("           and vm.dealer_id = r.dealer_id\n");
        sql.append("           AND NOT EXISTS (SELECT 1\n");
        sql.append("                  FROM VW_PART_DLR_TASK_MONTH VMm\n");
        sql.append("                 WHERE VMm.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                   AND VMm.DEALER_ID = P.DEALER_ID)) A\n");
        sql.append(" GROUP BY A.TASK_MONTH, a.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }


    public List<Map<String, Object>> downloadPlanQueryZz(String consql) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TO_CHAR(TD.AMOUNT,'fm999,999,990.00') AMOUNT,\n");
        sql.append("       MT.TASK_MONTH,\n");
        sql.append("       TO_CHAR(NVL((SELECT sum(p.AMOUNT)\n");
        sql.append("          FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE  MT.TASK_MONTH = P.MONTH_NO AND\n");
        sql.append("          TD.DEALER_ID = P.DEALER_ID),0),'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("        to_char(ROUND((NVL((SELECT sum(p.AMOUNT)\n");
        sql.append("          FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE  MT.TASK_MONTH = P.MONTH_NO AND\n");
        sql.append("          TD.DEALER_ID = P.DEALER_ID),0))/ TD.AMOUNT,6)*100,'990.00')||'%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD, TT_PART_DLR_MONTH_TASK MT\n");
        sql.append(" where td.task_id = mt.task_id and mt.status=10011001");
        sql.append(consql);
        return pageQuery(sql.toString(), null, getFunName());
    }


    public PageResult<Map<String, Object>> planCountQueryZz(String consql, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT),'fm999,999,990.00') AMOUNT,\n");
        sql.append("      '" + year + "-" + month + " 至 " + year + "-" + month2 + "' AS TASK_MONTH,\n");
        sql.append("       TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT)\n");
        sql.append("          FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE  MT.TASK_MONTH = P.MONTH_NO AND\n");
        sql.append("          TD.DEALER_ID = P.DEALER_ID),0)),'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("        to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT)\n");
        sql.append("          FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE  MT.TASK_MONTH = P.MONTH_NO AND\n");
        sql.append("          TD.DEALER_ID = P.DEALER_ID),0)))/ SUM(TD.AMOUNT),3)*100,'fm990.00')||'%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD, TT_PART_DLR_MONTH_TASK MT\n");
        sql.append(" where td.task_id = mt.task_id and mt.status=10011001");
        sql.append(consql);
        sql.append(" GROUP BY (TD.DEALER_CODE, TD.DEALER_NAME) ");
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }


    public List<Map<String, Object>> planAmountCountQueryZz(String consql, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT),'fm999999990.00') AMOUNT,\n");
        sql.append("      '" + year + "-" + month + " 至 " + year + "-" + month2 + "' AS TASK_MONTH,\n");
        sql.append("       TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT)\n");
        sql.append("          FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE  MT.TASK_MONTH = P.MONTH_NO AND\n");
        sql.append("          TD.DEALER_ID = P.DEALER_ID),0)),'fm999999990.00') AS SELLMONEY,\n");
        sql.append("        to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT)\n");
        sql.append("          FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE  MT.TASK_MONTH = P.MONTH_NO AND\n");
        sql.append("          TD.DEALER_ID = P.DEALER_ID),0)))/ SUM(TD.AMOUNT),6)*100,'fm990.00')||'%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD, TT_PART_DLR_MONTH_TASK MT\n");
        sql.append(" where td.task_id = mt.task_id and mt.status=10011001");
        sql.append(consql);
        sql.append(" GROUP BY (TD.DEALER_CODE, TD.DEALER_NAME) ");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> excelDownloadPlanQueryZz(String consql) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TO_CHAR(TD.AMOUNT,'fm999,999,990.00') AMOUNT,\n");
        sql.append("       MT.TASK_MONTH,\n");
        sql.append("       TO_CHAR(NVL((SELECT sum(p.AMOUNT)\n");
        sql.append("          FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE  MT.TASK_MONTH = P.MONTH_NO AND\n");
        sql.append("          TD.DEALER_ID = P.DEALER_ID),0),'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("        to_char(ROUND((NVL((SELECT sum(p.AMOUNT)\n");
        sql.append("          FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("         WHERE  MT.TASK_MONTH = P.MONTH_NO AND\n");
        sql.append("          TD.DEALER_ID = P.DEALER_ID),0))/ TD.AMOUNT,6)*100,'990.00')||'%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD, TT_PART_DLR_MONTH_TASK MT\n");
        sql.append(" where td.task_id = mt.task_id and mt.status=10011001");
        sql.append(consql);
        return pageQuery(sql.toString(), null, getFunName());
    }

    //add zhumingwei 2013-09-27 服务商月度目标查询(根据销售员选择)
    public PageResult<Map<String, Object>> planPageQueryXxy(String consql, String orgId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT u.name,\n");
        sql.append("       TO_CHAR(sum(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("       MT.TASK_MONTH,\n");
        sql.append("       TO_CHAR(NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                          AND P.SELLER_ID = " + orgId + ")),\n");
        sql.append("                   0),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND((NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 AND P.SELLER_ID = " + orgId + ")),\n");
        sql.append("                          0)) / sum(TD.AMOUNT),\n");
        sql.append("                     3) * 100,\n");
        sql.append("               '990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tc_user                    u,\n");
        sql.append("       tt_part_salesscope_define  sp\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and u.user_id = sp.user_id\n");
        sql.append("   and t.dealer_id = sp.dealer_id\n");
        sql.append("    AND SP.USER_TYPE = 3\n");
        sql.append(consql);
        sql.append(" group by u.name, MT.TASK_MONTH\n");
        sql.append(" order by u.name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    //add zhumingwei 2013-09-27 服务商月度目标查询(根据销售员选择)
    public List<Map<String, Object>> downloadPlanQueryXxy(String consql, String orgId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT u.name,\n");
        sql.append("       TO_CHAR(sum(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("       MT.TASK_MONTH,\n");
        sql.append("       TO_CHAR(NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                          AND P.SELLER_ID = " + orgId + ")),\n");
        sql.append("                   0),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND((NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 AND P.SELLER_ID = " + orgId + ")),\n");
        sql.append("                          0)) / sum(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               '990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tc_user                    u,\n");
        sql.append("       tt_part_salesscope_define  sp\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and u.user_id = sp.user_id\n");
        sql.append("   and t.dealer_id = sp.dealer_id\n");
        sql.append("    AND SP.USER_TYPE = 3\n");
        sql.append(consql);
        sql.append(" group by u.name, MT.TASK_MONTH\n");
        sql.append(" order by u.name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public PageResult<Map<String, Object>> planCountQueryXxy(String consql, String orgId, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT u.name,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("      '" + year + "-" + month + " 至 " + year + "-" + month2 + "' AS TASK_MONTH,\n");
        sql.append("       TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                          AND P.SELLER_ID = " + orgId + "),\n");
        sql.append("                       0)),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 AND P.SELLER_ID = " + orgId + "),\n");
        sql.append("                              0))) / SUM(TD.AMOUNT),\n");
        sql.append("                     3) * 100,\n");
        sql.append("               'fm990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tc_user                    u,\n");
        sql.append("       tt_part_salesscope_define  sp\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and u.user_id = sp.user_id\n");
        sql.append("   and t.dealer_id = sp.dealer_id\n");
        sql.append("    AND SP.USER_TYPE = 3\n");
        sql.append(consql);
        sql.append(" group by u.name, MT.TASK_MONTH\n");
        sql.append(" order by u.name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public List<Map<String, Object>> planAmountCountQueryXxy(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT u.name,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("      '" + year + "-" + month + " 至 " + year + "-" + month2 + "' AS TASK_MONTH,\n");
        sql.append("       TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                          AND P.SELLER_ID = " + orgId + "),\n");
        sql.append("                       0)),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 AND P.SELLER_ID = " + orgId + "),\n");
        sql.append("                              0))) / SUM(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               'fm990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tc_user                    u,\n");
        sql.append("       tt_part_salesscope_define  sp\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and u.user_id = sp.user_id\n");
        sql.append("   and t.dealer_id = sp.dealer_id\n");
        sql.append("   AND SP.USER_TYPE = 3\n");
        sql.append(consql);
        sql.append(" group by u.name, MT.TASK_MONTH\n");
        sql.append(" order by u.name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> excelDownloadPlanQueryXxy(String consql, String orgId, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();

        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT u.name,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        //sql.append("      '"+ year +"-"+ month +" 至 "+ year +"-"+ month2 +"' AS TASK_MONTH,\n");
        sql.append("       MT.TASK_MONTH,TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                          AND P.SELLER_ID = " + orgId + "),\n");
        sql.append("                       0)),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 AND P.SELLER_ID = " + orgId + "),\n");
        sql.append("                              0))) / SUM(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               'fm990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tc_user                    u,\n");
        sql.append("       tt_part_salesscope_define  sp\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and u.user_id = sp.user_id\n");
        sql.append("   and t.dealer_id = sp.dealer_id\n");
        sql.append("   AND SP.USER_TYPE = 3\n");
        sql.append(consql);
        sql.append(" group by u.name, MT.TASK_MONTH\n");
        sql.append(" order by u.name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public PageResult<Map<String, Object>> planPageQuerySf(String consql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT r.region_name,\n");
        sql.append("       TO_CHAR(sum(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("       MT.TASK_MONTH,\n");
        sql.append("       TO_CHAR(NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID)),\n");
        sql.append("                   0),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND((NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID)),\n");
        sql.append("                          0)) / sum(TD.AMOUNT),\n");
        sql.append("                     3) * 100,\n");
        sql.append("               '990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tm_region                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.province_id = r.region_code\n");
        sql.append(consql);
        sql.append(" group by r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.region_name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public PageResult<Map<String, Object>> planPageQueryDq(String consql, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT r.root_org_name,r.region_name,\n");
        sql.append("       TO_CHAR(sum(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("       MT.TASK_MONTH,\n");
        sql.append("       TO_CHAR(NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID)),\n");
        sql.append("                   0),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND((NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID)),\n");
        sql.append("                          0)) / sum(TD.AMOUNT),\n");
        sql.append("                     3) * 100,\n");
        sql.append("               '990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       vw_org_dealer_service                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.dealer_id = r.dealer_id\n");
        sql.append(consql);
        sql.append(" group by r.root_org_name,r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public List<Map<String, Object>> downloadPlanQuerysF(String consql) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT r.region_name,\n");
        sql.append("       TO_CHAR(sum(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("       MT.TASK_MONTH,\n");
        sql.append("       TO_CHAR(NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID)),\n");
        sql.append("                   0),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND((NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID)),\n");
        sql.append("                          0)) / sum(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               '990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tm_region                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.province_id = r.region_code\n");
        sql.append(consql);
        sql.append(" group by r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.region_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> downloadPlanQueryDq(String consql) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT r.root_org_name,r.region_name,\n");
        sql.append("       TO_CHAR(sum(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("       MT.TASK_MONTH,\n");
        sql.append("       TO_CHAR(NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID)),\n");
        sql.append("                   0),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND((NVL(sum((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID)),\n");
        sql.append("                          0)) / sum(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               '990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       vw_org_dealer_service                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.dealer_id = r.dealer_id\n");
        sql.append(consql);
        sql.append(" group by r.root_org_name,r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public PageResult<Map<String, Object>> planCountQuerySf(String consql, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }


        sql.append("SELECT r.region_name,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("      '" + year + "-" + month + " 至 " + year + "-" + month2 + "' AS TASK_MONTH,\n");
        sql.append("       TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                           ),\n");
        sql.append("                       0)),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 ),\n");
        sql.append("                              0))) / SUM(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               'fm990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tm_region                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.province_id = r.region_code\n");
        sql.append(consql);
        sql.append(" group by r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.region_name\n");


        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public PageResult<Map<String, Object>> planCountQueryDq(String consql, RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }


        sql.append("SELECT r.root_org_name,r.region_name,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("      '" + year + "-" + month + " 至 " + year + "-" + month2 + "' AS TASK_MONTH,\n");
        sql.append("       TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                           ),\n");
        sql.append("                       0)),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 ),\n");
        sql.append("                              0))) / SUM(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               'fm990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       vw_org_dealer_service                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.dealer_id = r.dealer_id\n");
        sql.append(consql);
        sql.append(" group by r.root_org_name,r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.root_org_name\n");


        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    public List<Map<String, Object>> planAmountCountQuerySf(String consql, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT r.region_name,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("      '" + year + "-" + month + " 至 " + year + "-" + month2 + "' AS TASK_MONTH,\n");
        sql.append("       TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                           ),\n");
        sql.append("                       0)),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 ),\n");
        sql.append("                              0))) / SUM(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               'fm990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tm_region                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.province_id = r.region_code\n");
        sql.append(consql);
        sql.append(" group by r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.region_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> planAmountCountQueryDq(String consql, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT r.root_org_name,r.region_name,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        sql.append("      '" + year + "-" + month + " 至 " + year + "-" + month2 + "' AS TASK_MONTH,\n");
        sql.append("       TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                           ),\n");
        sql.append("                       0)),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 ),\n");
        sql.append("                              0))) / SUM(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               'fm990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       vw_org_dealer_service                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.dealer_id = r.dealer_id\n");
        sql.append(consql);
        sql.append(" group by r.root_org_name,r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> excelDownloadPlanQuerySf(String consql, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();

        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT r.region_name,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        //sql.append("      '"+ year +"-"+ month +" 至 "+ year +"-"+ month2 +"' AS TASK_MONTH,\n");
        sql.append("       MT.TASK_MONTH,TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                           ),\n");
        sql.append("                       0)),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 ),\n");
        sql.append("                              0))) / SUM(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               'fm990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       tm_region                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.province_id = r.region_code\n");
        sql.append(consql);
        sql.append(" group by r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.region_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public List<Map<String, Object>> excelDownloadPlanQueryDq(String consql, RequestWrapper request) {
        StringBuffer sql = new StringBuffer();

        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        if (10 > Integer.parseInt(month)) {
            month = "0" + month;
        }
        if (10 > Integer.parseInt(month2)) {
            month2 = "0" + month2;
        }

        sql.append("SELECT r.root_org_name,r.region_name,\n");
        sql.append("       TO_CHAR(SUM(TD.AMOUNT), 'fm999,999,990.00') AMOUNT,\n");
        //sql.append("      '"+ year +"-"+ month +" 至 "+ year +"-"+ month2 +"' AS TASK_MONTH,\n");
        sql.append("       MT.TASK_MONTH,TO_CHAR(SUM(NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                         FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                        WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                          AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                           ),\n");
        sql.append("                       0)),\n");
        sql.append("               'fm999,999,990.00') AS SELLMONEY,\n");
        sql.append("       to_char(ROUND(SUM((NVL((SELECT sum(p.AMOUNT) \n");
        sql.append("                                FROM VM_PART_DLR_MONTH_PURCHASE P\n");
        sql.append("                               WHERE MT.TASK_MONTH = P.MONTH_NO\n");
        sql.append("                                 AND TD.DEALER_ID = P.DEALER_ID\n");
        sql.append("                                 ),\n");
        sql.append("                              0))) / SUM(TD.AMOUNT),\n");
        sql.append("                     6) * 100,\n");
        sql.append("               'fm990.00') || '%' AS RATIO\n");
        sql.append("  FROM TT_PART_DLR_TASK_MONTH_DTL TD,\n");
        sql.append("       TT_PART_DLR_MONTH_TASK     MT,\n");
        sql.append("       tm_dealer                  t,\n");
        sql.append("       vw_org_dealer_service                  r\n");
        sql.append(" where td.task_id = mt.task_id\n");
        sql.append("   and mt.status = 10011001\n");
        sql.append("   and t.dealer_id = td.dealer_id\n");
        sql.append("   and t.dealer_id = r.dealer_id\n");
        sql.append(consql);
        sql.append(" group by r.root_org_name,r.region_name, MT.TASK_MONTH\n");
        sql.append(" order by r.root_org_name\n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    //格式化当前月
    private String simMonth(String month) {
        if (month.length() == 1)
            return "0" + month;
        return month;
    }

    /**
     * @param dealerId 服务商ID
     * @param monthNo  任务年月
     */
    public void updateTaskState(String dealerId, String monthNo) {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE TT_PART_DLR_MONTH_TASK T\n");
        sql.append("   SET T.STATUS = 0\n");
        sql.append(" WHERE EXISTS (SELECT 1\n");
        sql.append("          FROM TT_PART_DLR_TASK_MONTH_DTL D\n");
        sql.append("         WHERE T.TASK_ID = D.TASK_ID\n");
        sql.append("           AND D.DEALER_ID = ?)\n");
        sql.append("   AND T.STATE = 10011001\n");
        sql.append("   AND T.TASK_MONTH = ?\n");
        ArrayList arrayList = new ArrayList();
        arrayList.add(dealerId);
        arrayList.add(monthNo);
        update(sql.toString(), arrayList);

    }
}
