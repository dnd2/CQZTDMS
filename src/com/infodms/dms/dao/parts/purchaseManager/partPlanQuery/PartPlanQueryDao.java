package com.infodms.dms.dao.parts.purchaseManager.partPlanQuery;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartPlanQueryDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartPlanQueryDao.class);

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    private static final PartPlanQueryDao dao = new PartPlanQueryDao();

    private PartPlanQueryDao() {
    }

    public static final PartPlanQueryDao getInstance() {
        return dao;
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 获取库房
     */
    public List<Map<String, Object>> getWareHouse(Long userId) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT Distinct d.wh_id,d.wh_name\n");
        sbSql.append("  FROM TT_PART_PLANER_WH_RELATION R, TT_PART_WAREHOUSE_DEFINE D\n");
        sbSql.append(" WHERE R.WH_ID = D.WH_ID\n");
        sbSql.append("    AND r.planer_id = " + userId);

        List<Map<String, Object>> wareHoustList = this.pageQuery(sbSql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    public List<Map<String, Object>> getUserPoseLise(Integer type, Long userId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT U.USER_ID, U.NAME User_name\n");
        sql.append("  FROM TT_PART_USERPOSE_DEFINE D, TC_USER U\n");
        sql.append(" WHERE D.USER_ID = U.USER_ID\n");
        sql.append("   AND D.user_type=" + type);
        sql.append("   AND D.State=" + Constant.STATUS_ENABLE);
        if (!"".equals(userId) && userId != null) {
            sql.append(" AND U.user_id=" + userId);
        }

        List<Map<String, Object>> wareHoustList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public PageResult<Map<String, Object>> queryPartPlan(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String planCode = CommonUtils.checkNull(request.getParamValue("PLAN_CODE"));//计划单号
        String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//计划年
        String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//计划月
        String planType = CommonUtils.checkNull(request.getParamValue("PLAN_TYPE"));//计划类型
        String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房
        String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态
        String createType = CommonUtils.checkNull(request.getParamValue("CREATE_TYPE"));//生成方式
        String planerId = CommonUtils.checkNull(request.getParamValue("planerId"));//计划员

        String sCreateDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));//制单开始日期
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));//制单结束日期

        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SSUBMIT_DATE"));//提交开始日期
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("ESUBMIT_DATE"));//提交结束日期

        String sCheckDate = CommonUtils.checkNull(request.getParamValue("SCHECK_DATE"));//审核开始日期
        String eCheckDate = CommonUtils.checkNull(request.getParamValue("ECHECK_DATE"));//审核结束日期

        String sConfirmDate = CommonUtils.checkNull(request.getParamValue("SCONFIRM_DATE"));//确认开始日期
        String eConfirmDate = CommonUtils.checkNull(request.getParamValue("ECONFIRM_DATE"));//确认结束日期

        String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
        String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号


        //modify by yuan 2013-04-29

        sql.append("SELECT A.PLAN_ID,\n");
        sql.append("       A.PLAN_CODE,\n");
        sql.append("       A.PLANER_ID,\n");
        sql.append("       A.PLANER_NAME,\n");
        sql.append("       A.BUYER_ID,\n");
        sql.append("       A.BUYER,\n");
        sql.append("       A.PART_TYPE,\n");
        sql.append("       A.PRODUCE_FAC,\n");
        sql.append("       TO_char(A.YEAR_MONTH,'yyyy-mm') AS YEAR_MONTH,\n");
        sql.append("       A.PLAN_TYPE,\n");
        sql.append("       A.WH_ID,\n");
        sql.append("       A.WH_NAME,\n");
        sql.append("       A.SUM_QTY,\n");
        sql.append("       to_char(A.AMOUNT,'FM999,999,999,999,990.00') AMOUNT,\n");
        sql.append("       A.CREATE_TYPE,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       A.REMARK2,\n");
        sql.append("       A.SUBMIT_DATE,\n");
        sql.append("       A.SUBMIT_BY,\n");
        sql.append("       A.CREATE_DATE,\n");
        sql.append("       A.CREATE_BY,\n");
        sql.append("       A.ORG_ID,\n");
        sql.append("       A.UPDATE_DATE,\n");
        sql.append("       A.UPDATE_BY,\n");
        sql.append("       A.DISABLE_DATE,\n");
        sql.append("       A.DISABLE_BY,\n");
        sql.append("       A.DELETE_DATE,\n");
        sql.append("       A.DELETE_BY,\n");
        sql.append("       A.CHECK_BY,\n");
        sql.append("       A.CHECK_DATE,\n");
        sql.append("       A.CONFIRM_BY,\n");
        sql.append("       A.CONFIRM_DATE,\n");
        sql.append("       A.VER,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.STATUS\n");
        sql.append("  FROM TT_PART_PLAN_MAIN A,TT_PART_PLAN_DETAIL B\n");
        sql.append(" WHERE A.PLAN_ID=B.PLAN_ID\n");

        //modify by yuan
        if (!"".equals(year) && year != "") {
            if (!"".equals(month) && month != "") {
                sql.append(" AND to_char(A.year_month,'yyyy-mm')>='" + year + "-" + month + "'");
            } else {
                sql.append("  AND to_char(A.year_month,'yyyy'='" + year + "'");
            }
        } else {
            if (!"".equals(month) && month != "") {
                sql.append(" AND to_char(A.year_month,'mm') ='" + month + "'");
            }
        }

        if (!"".equals(planType)) {
            sql.append(" and A.PLAN_TYPE='").append(planType).append("'");
        }
        if (!"".equals(whId)) {
            sql.append(" and A.WH_ID='").append(whId).append("'");
        }
        if (!"".equals(state)) {
            sql.append(" and A.STATE='").append(state).append("'");
        }
        if (!"".equals(createType)) {
            sql.append(" and A.CREATE_TYPE='").append(createType).append("'");
        }
        if (!"".equals(planCode)) {
            sql.append(" and upper(A.PLAN_CODE) like upper('%").append(planCode).append("%')");
        }
        if (!"".equals(partOldCode)) {
            sql.append(" and upper(B.PART_OLDCODE) like upper('%").append(partOldCode).append("%')");
        }
        if (!"".equals(partCname)) {
            sql.append(" and B.PART_CNAME like '%").append(partCname).append("%'");
        }
        if (!"".equals(partCode)) {
            sql.append(" and  upper(B.PART_CODE） like  upper('%").append(partCode).append("%')");
        }
        if (!"".equals(planerId)) {
            sql.append(" and A.PLANER_ID='").append(planerId).append("'");
        }
        if (!"".equals(sCreateDate)) {
            sql.append(" and TRUNC(A.CREATE_DATE)>= to_date('").append(sCreateDate).append("','YYYY-MM-dd')");
        }
        if (!"".equals(eCreateDate)) {
            sql.append(" and TRUNC(a.CREATE_DATE)<= to_date('").append(eCreateDate).append("','YYYY-MM-dd')");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and TRUNC(SUBMIT_DATE)>= to_date('").append(sSubmitDate).append("','YYYY-MM-dd')");
        }
        if (!"".equals(eSubmitDate)) {
            sql.append(" and TRUNC(SUBMIT_DATE)<= to_date('").append(eSubmitDate).append("','YYYY-MM-dd')");
        }
        if (!"".equals(sCheckDate)) {
            sql.append(" and TRUNC(CHECK_DATE)>= to_date('").append(sCheckDate).append("','YYYY-MM-dd')");
        }
        if (!"".equals(eCheckDate)) {
            sql.append(" and TRUNC(CHECK_DATE)<= to_date('").append(eCheckDate).append("','YYYY-MM-dd')");
        }
        if (!"".equals(sConfirmDate)) {
            sql.append(" and TRUNC(CONFIRM_DATE)>= to_date('").append(sConfirmDate).append("','YYYY-MM-dd')");
        }
        if (!"".equals(eConfirmDate)) {
            sql.append(" and TRUNC(CONFIRM_DATE)<= to_date('").append(eConfirmDate).append("','YYYY-MM-dd')");
        }

        sql.append(" GROUP BY  \n");
        sql.append("  A.PLAN_ID, \n");
        sql.append("        A.PLAN_CODE, \n");
        sql.append("        A.PLANER_ID, \n");
        sql.append("        A.PLANER_NAME, \n");
        sql.append("        A.BUYER_ID, \n");
        sql.append("        A.BUYER, \n");
        sql.append("        A.PART_TYPE, \n");
        sql.append("        A.PRODUCE_FAC, \n");
        sql.append("        A.YEAR_MONTH, \n");
        sql.append("        A.PLAN_TYPE, \n");
        sql.append("        A.WH_ID, \n");
        sql.append("        A.WH_NAME, \n");
        sql.append("        A.SUM_QTY, \n");
        sql.append("        A.AMOUNT, \n");
        sql.append("        A.CREATE_TYPE, \n");
        sql.append("        A.REMARK, \n");
        sql.append("        A.REMARK2, \n");
        sql.append("        A.SUBMIT_DATE, \n");
        sql.append("        A.SUBMIT_BY, \n");
        sql.append("        A.CREATE_DATE, \n");
        sql.append("        A.CREATE_BY, \n");
        sql.append("        A.ORG_ID, \n");
        sql.append("        A.UPDATE_DATE, \n");
        sql.append("        A.UPDATE_BY, \n");
        sql.append("        A.DISABLE_DATE, \n");
        sql.append("        A.DISABLE_BY, \n");
        sql.append("        A.DELETE_DATE, \n");
        sql.append("        A.DELETE_BY, \n");
        sql.append("        A.CHECK_BY, \n");
        sql.append("        A.CHECK_DATE, \n");
        sql.append("        A.CONFIRM_BY, \n");
        sql.append("        A.CONFIRM_DATE, \n");
        sql.append("        A.VER, \n");
        sql.append("        A.STATE, \n");
        sql.append("        A.STATUS \n");

        sql.append(" order by A.CREATE_DATE desc");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */

    public List<Map<String, Object>> queryPartPlan(RequestWrapper request) {
        StringBuffer sql = new StringBuffer();
        String planCode = CommonUtils.checkNull(request.getParamValue("PLAN_CODE"));//计划单号
        String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//计划年
        String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//计划月
        String planType = CommonUtils.checkNull(request.getParamValue("PLAN_TYPE"));//计划类型
        String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房
        String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态
        String createType = CommonUtils.checkNull(request.getParamValue("CREATE_TYPE"));//生成方式

        String sCreateDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));//制单开始日期
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));//制单结束日期

        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SSUBMIT_DATE"));//提交开始日期
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("ESUBMIT_DATE"));//提交结束日期

        String sCheckDate = CommonUtils.checkNull(request.getParamValue("SCHECK_DATE"));//审核开始日期
        String eCheckDate = CommonUtils.checkNull(request.getParamValue("ECHECK_DATE"));//审核结束日期

        String sConfirmDate = CommonUtils.checkNull(request.getParamValue("SCONFIRM_DATE"));//确认开始日期
        String eConfirmDate = CommonUtils.checkNull(request.getParamValue("ECONFIRM_DATE"));//确认结束日期


        sql.append(" select * from TT_PART_PLAN_MAIN a");
        sql.append(" where 1=1 ");
        if (!"".equals(year)) {
            String Sdate = year + "/" + month + "/01";
            if (month.equals("12")) {
                year = (Integer.valueOf(year) + 1) + "";
                month = "01";
            } else {
                month = "0" + (Integer.valueOf(month) + 1);
            }
            String Edate = year + "/" + month + "/01";
            sql.append(" and a.YEAR_MONTH>= to_date('").append(Sdate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.YEAR_MONTH< to_date('").append(Edate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(planType)) {
            sql.append(" and PLAN_TYPE='").append(planType).append("'");
        }
        if (!"".equals(whId)) {
            sql.append(" and WH_ID='").append(whId).append("'");
        }
        if (!"".equals(state)) {
            sql.append(" and STATE='").append(state).append("'");
        }
        if (!"".equals(createType)) {
            sql.append(" and CREATE_TYPE='").append(createType).append("'");
        }
        if (!"".equals(planCode)) {
            sql.append(" and PLAN_CODE='").append(planCode).append("'");
        }
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate).append("','YYYY/MM/dd')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate).append("','YYYY/MM/dd')");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and SUBMIT_DATE>= to_date('").append(sSubmitDate).append("','YYYY/MM/dd')");
            sql.append(" and SUBMIT_DATE<= to_date('").append(eSubmitDate).append("','YYYY/MM/dd')");
        }
        if (!"".equals(sCheckDate)) {
            sql.append(" and CHECK_DATE>= to_date('").append(sCheckDate).append("','YYYY/MM/dd')");
            sql.append(" and CHECK_DATE<= to_date('").append(eCheckDate).append("','YYYY/MM/dd')");
        }
        if (!"".equals(sConfirmDate)) {
            sql.append(" and CONFIRM_DATE>= to_date('").append(sConfirmDate).append("','YYYY/MM/dd')");
            sql.append(" and CONFIRM_DATE<= to_date('").append(eConfirmDate).append("','YYYY/MM/dd')");
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param type
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 获取CODE
     */
    public Map<String, Object> getTcCodeMap(String type) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select CODE_ID,CODE_DESC from TC_CODE ");
        sql.append(" where 1=1 ");
        sql.append(" and TYPE='").append(type).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        Map<String, Object> map = new HashMap();
        for (Map<String, Object> dataMap : list) {
            map.put(CommonUtils.checkNull(dataMap.get("CODE_ID")), CommonUtils.checkNull(dataMap.get("CODE_DESC")));
        }
        return map;
    }
}
