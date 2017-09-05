package com.infodms.dms.dao.parts.rebateManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.*;

public class rebateManagerDao extends BaseDao<PO> {
    public static final Logger logger = Logger.getLogger(rebateManagerDao.class);
    private static final rebateManagerDao dao = new rebateManagerDao();

    private rebateManagerDao() {
    }

    public static final rebateManagerDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public String checkDealer(String delCode) {
        String sql = "select * from tm_dealer td where td.dealer_code = '" + delCode + "'";
        List<Map<String, Object>> list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        if (list.size() > 0)
            return (list.get(0).get("DEALER_ID")).toString();
        return "0";
    }

    /**
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> rebateQuery(RequestWrapper request, int curPage, int pageSize, String flag, AclUserBean loginUser) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));

        String ogrId = "";
        if (null != loginUser.getDealerId()) {
            ogrId = loginUser.getDealerId();
        }

        if ("1".equals(flag)) {
            sql.append("SELECT DS.REGION_NAME,\n");
            sql.append("       TD.DEALER_CODE,\n");
            sql.append("       TD.DEALER_NAME,\n");
            sql.append("       NVL(FT.FIN_RETURN_NAME, '配件季度返利') FIN_RETURN_NAME,\n");
            sql.append("       NVL(SUM(NVL(DR.AMOUNT, 0)) - SUM(NVL(DR.CD_AMOUNT, 0)),0 ) WD_AMOUNT,\n");
            sql.append("       NVL(SUM(DR.AMOUNT),0 ) YF_AMOUNT,\n");
            sql.append("       NVL(SUM(DR.CD_AMOUNT),0 ) CD_AMOUNT,\n");
        } else {
            sql.append("SELECT NVL(SUM(NVL(DR.AMOUNT, 0)) - SUM(NVL(DR.CD_AMOUNT, 0)),0 ) WD_AMOUNT,\n");
            sql.append("       NVL(SUM(DR.AMOUNT),0 ) YF_AMOUNT,\n");
            sql.append("       NVL(SUM(DR.CD_AMOUNT),0 ) CD_AMOUNT,\n");
        }
        //拼凑查询SQL
        if (!"".equals(month) && !"".equals(month2)) {
            if (Integer.valueOf(month2) > Integer.valueOf(month)) {
                for (int i = Integer.valueOf(month); i <= Integer.valueOf(month2); i++) {
                    sql.append("       NVL(SUM(DECODE(DR.YEAR_MONTH, '" + year + "-0" + i + "', NVL(DR.AMOUNT, 0), 0)),0 ) YFM" + i + "_AMOUNT,\n");
                }
                for (int i = Integer.valueOf(month); i <= Integer.valueOf(month2); i++) {
                    sql.append("       NVL(SUM(DECODE(DR.YEAR_MONTH, '" + year + "-0" + i + "', NVL(DR.CD_AMOUNT, 0), 0)),0 ) CDM" + i + "_AMOUNT,\n");
                }
            } else if (Integer.valueOf(month2) == Integer.valueOf(month)) {
                sql.append("       NVL(SUM(DECODE(DR.YEAR_MONTH, '" + year + "-0" + month2 + "', NVL(DR.AMOUNT, 0), 0)),0 ) YFM" + month2 + "_AMOUNT,\n");
                sql.append("       NVL(SUM(DECODE(DR.YEAR_MONTH, '" + year + "-0" + month2 + "', NVL(DR.CD_AMOUNT, 0), 0)),0 ) CDM" + month2 + "_AMOUNT,\n");
            }
        } else {
            if ("".equals(month2) && !"".equals(month)) {
                sql.append("       NVL(SUM(DECODE(DR.YEAR_MONTH, '" + year + "-0" + month + "', NVL(DR.AMOUNT, 0), 0)),0 ) YFM" + month + "_AMOUNT,\n");
                sql.append("       NVL(SUM(DECODE(DR.YEAR_MONTH, '" + year + "-0" + month + "', NVL(DR.CD_AMOUNT, 0), 0)),0 ) CDM" + month + "_AMOUNT,\n");
            }
            if ("".equals(month) && !"".equals(month2)) {
                sql.append("       NVL(SUM(DECODE(DR.YEAR_MONTH, '" + year + "-0" + month2 + "', NVL(DR.AMOUNT, 0), 0)),0 ) YFM" + month2 + "_AMOUNT,\n");
                sql.append("       NVL(SUM(DECODE(DR.YEAR_MONTH, '" + year + "-0" + month2 + "', NVL(DR.CD_AMOUNT, 0), 0)),0 ) CDM" + month2 + "_AMOUNT,\n");
            }

        }

        sql.deleteCharAt(sql.length() - 2);
        sql.append(" FROM TM_DEALER             TD,\n");
        sql.append("      TT_PART_DLR_REBATE    DR,\n");
        sql.append("      TM_FIN_RETURN_TYPE    FT,\n");
        sql.append("      VW_ORG_DEALER_SERVICE DS\n");
        sql.append(" WHERE TD.DEALER_ID = DR.DEALER_ID(+)\n");
        sql.append("  AND DR.REBATE_TYPE = FT.FIN_RETURN_TYPE(+)\n");
        sql.append("  AND TD.DEALER_ID = DS.DEALER_ID(+)\n");
        sql.append("  AND TD.DEALER_TYPE = 10771002\n");
        sql.append("  AND TD.DEALER_LEVEL = 10851001\n");
//        sql.append("  AND TD.SERVICE_STATUS = 13691002\n");

        if (!"".equals(month) && null != month && !"".equals(month2) && null != month2) {
            sql.append("   AND DR.YEAR_MONTH >= '" + year + "-0" + month + "'\n");
            sql.append("   AND DR.YEAR_MONTH <= '" + year + "-0" + month2 + "'\n");
        }

        if ("".equals(month) || "".equals(month2)) {
            if (!"".equals(month) && null != month) {
                sql.append("   AND DR.YEAR_MONTH = '" + year + "-0" + month + "'\n");
            } else if (!"".equals(month2) && null != month2) {
                sql.append("   AND DR.YEAR_MONTH = '" + year + "-0" + month2 + "'\n");
            }
        }

        if ("".equals(month) && "".equals(month2)) {
            sql.append("   AND DR.YEAR_MONTH like '" + year + "%'\n");
        }
        if (!"".equals(dealerCode)) {
            if (dealerCode.split(",").length > 1) {
                String[] dcs = dealerCode.split(",");
                StringBuffer tempsql = new StringBuffer();
                for (String dc : dcs) {
                    tempsql.append("'" + dc + "',");
                }
                tempsql.deleteCharAt(tempsql.length() - 1);
                sql.append(" and  TD.dealer_code in (" + tempsql + ")");
            } else {
                sql.append(" and  UPPER(TD.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
            }
        }
        //权限控制
        sql.append("  AND TD.DEALER_ID = DECODE('" + ogrId + "', '', TD.DEALER_ID, '" + ogrId + "')\n");
        if ("1".equals(flag)) {
            sql.append(" GROUP BY DS.REGION_NAME,TD.DEALER_CODE, TD.DEALER_NAME, FT.FIN_RETURN_NAME\n");
        }
        sql.append("  ORDER BY TD.DEALER_CODE");
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * 销售收入查询
     *
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> invQuery(RequestWrapper request, int curPage, int pageSize, String flag) {
        StringBuffer sql = new StringBuffer();
        String year = CommonUtils.checkNull(request.getParamValue("year"));
        String month = CommonUtils.checkNull(request.getParamValue("month"));
        String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
        String account_type = CommonUtils.checkNull(request.getParamValue("ACCOUNT_TYPE"));

        if ("1".equals(flag)) {
            sql.append("SELECT DS.REGION_NAME,\n");
            sql.append("       TD.DEALER_CODE,\n");
            sql.append("       TD.DEALER_NAME,\n");
            sql.append("       ROUND(SUM(NVL(RD.AMOUNT, 0)), 2) WD_AMOUNT,\n");
        } else {
            sql.append("SELECT ROUND(SUM(NVL(RD.AMOUNT, 0)), 2) WD_AMOUNT,\n");
        }

        //拼凑查询SQL
        if ("".equals(month) && "".equals(month2)) {
//            month = "1";
//            month2 = "12";
        }
        if (!"".equals(month) && !"".equals(month2)) {
            if (Integer.valueOf(month2) > Integer.valueOf(month)) {
                for (int i = Integer.valueOf(month); i <= Integer.valueOf(month2); i++) {
                    sql.append("       ROUND(SUM(DECODE(RD.CREATE_DATE, '" + year + "-" + formatMonth(i + "") + "', NVL(RD.AMOUNT, 0), 0)),2) YFM" + formatMonth(i + "") + "_AMOUNT,\n");
                }
            } else if (Integer.valueOf(month2) == Integer.valueOf(month)) {
                sql.append("       ROUND(SUM(DECODE(RD.CREATE_DATE, '" + year + "-" + formatMonth(month2) + "', NVL(RD.AMOUNT, 0), 0)),2) YFM" + formatMonth(month2) + "_AMOUNT,\n");
            }
        } else {
            if ("".equals(month2) && !"".equals(month)) {
                sql.append("       ROUND(SUM(DECODE(RD.CREATE_DATE, '" + year + "-" + formatMonth(month) + "', NVL(RD.AMOUNT, 0), 0)),2) YFM" + formatMonth(month) + "_AMOUNT,\n");
            }
            if ("".equals(month) && !"".equals(month2)) {
                sql.append("       ROUND(SUM(DECODE(RD.CREATE_DATE, '" + year + "-" + formatMonth(month2) + "', NVL(RD.AMOUNT, 0), 0)),2) YFM" + formatMonth(month2) + "_AMOUNT,\n");
            }
        }
        sql.deleteCharAt(sql.length() - 2);
        sql.append("  FROM TT_PART_FUNDS_DETAIL RD, TM_DEALER TD, VW_ORG_DEALER_SERVICE DS \n");
        sql.append(" WHERE RD.DEALER_ID = TD.DEALER_ID\n");
        sql.append("   AND TD.DEALER_ID = DS.DEALER_ID(+)\n");
        sql.append("   AND RD.IS_TYPE = 3\n");
        if (!"".equals(account_type) && null != account_type) {
            sql.append("   AND RD.FIN_TYPE='" + account_type + "'\n");
        }

        if (!"".equals(month) && null != month && !"".equals(month2) && null != month2) {
            sql.append("   AND RD.CREATE_DATE >= '" + year + "-" + formatMonth(month) + "'\n");
            sql.append("   AND RD.CREATE_DATE <= '" + year + "-" + formatMonth(month2) + "'\n");
        }

        if ("".equals(month) || "".equals(month2)) {
            if (!"".equals(month) && null != month) {
                sql.append("   AND RD.CREATE_DATE = '" + year + "-" + formatMonth(month) + "'\n");
            } else if (!"".equals(month2) && null != month2) {
                sql.append("   AND RD.CREATE_DATE = '" + year + "-" + formatMonth(month2) + "'\n");
            }
        }
        if ("".equals(month) && "".equals(month2)) {
            sql.append("   AND RD.CREATE_DATE like '" + year + "%'\n");
        }
        if (!"".equals(dealerCode)) {
            if (dealerCode.split(",").length > 1) {
                String[] dcs = dealerCode.split(",");
                StringBuffer tempsql = new StringBuffer();
                for (String dc : dcs) {
                    tempsql.append("'" + dc + "',");
                }
                tempsql.deleteCharAt(tempsql.length() - 1);
                sql.append(" and  TD.dealer_code in (" + tempsql + ")");
            } else {
                sql.append(" and  UPPER(TD.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
            }
        }
        if ("1".equals(flag)) {
            sql.append("GROUP BY DS.REGION_NAME, TD.DEALER_CODE, TD.DEALER_NAME\n");
        }
        return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * 获取返利类型Map
     *
     * @return
     */
    public Map getRebateType() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.FIN_RETURN_CODE, T.FIN_RETURN_TYPE || ','||T.FIN_RETURN_NAME TNAME\n");
        sql.append("  FROM TM_FIN_RETURN_TYPE T\n");
        sql.append(" WHERE T.STATUS = 10011001\n");
        return listMap2Map(pageQuery(sql.toString(), null, getFunName()));
    }

    /**
     * 获取服务商类型Map
     *
     * @return
     */
    public Map getDealerInfo() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TD.DEALER_CODE, TD.DEALER_ID||','||td.dealer_name IDN\n");
        sql.append("  FROM TM_DEALER TD\n");
        sql.append(" WHERE TD.DEALER_TYPE = 10771002\n");
        sql.append("   AND TD.DEALER_LEVEL = 10851001\n");
        return listMap2Map(pageQuery(sql.toString(), null, getFunName()));
    }

    /**
     * List to Map
     *
     * @param list
     * @return
     */
    public static Map listMap2Map(List<Map<String, Object>> list) {
        Map map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> tmp = list.get(i);
            Set set = tmp.entrySet();
            ArrayList arr = new ArrayList();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                arr.add(entry.getKey());
            }
            map.put(tmp.get(arr.toArray()[0]), tmp.get(arr.toArray()[1]));
        }

        return map;
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

    /**
     * 月份格式化
     *
     * @param mth
     * @return
     */
    public static String formatMonth(String mth) {
        String month = "";
        if (Integer.valueOf(mth) < 10) {
            month = "0" + mth;
        } else
            month += mth;
        return month;
    }

}
