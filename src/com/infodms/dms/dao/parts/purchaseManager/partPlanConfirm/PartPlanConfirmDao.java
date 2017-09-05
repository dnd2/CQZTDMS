package com.infodms.dms.dao.parts.purchaseManager.partPlanConfirm;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartPlanConfirmDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartPlanConfirmDao.class);

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    private static final PartPlanConfirmDao dao = new PartPlanConfirmDao();

    private PartPlanConfirmDao() {
    }

    public static final PartPlanConfirmDao getInstance() {
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
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
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

        sql.append("SELECT PLAN_CODE,\n");
        sql.append("       PLANER_NAME,\n");
        sql.append("       A.CREATE_DATE,\n");
        sql.append("       WH_NAME,\n");
        sql.append("       TO_CHAR(YEAR_MONTH,'YYYY-MM') YEAR_MONTH,\n");
        sql.append("       PLAN_TYPE,\n");
        sql.append("       to_char(SUM_QTY,'fm999,999,999') SUM_QTY,\n");
        sql.append("       to_char(AMOUNT,'FM999,999,999,999,990.00') AMOUNT,\n");
        sql.append("       STATE,\n");
        sql.append("       (SELECT COUNT(1) FROM  TT_PART_PLAN_DETAIL B WHERE B.PLAN_ID=A.PLAN_ID) XS,\n");
        sql.append("       CHECK_DATE,\n");
        sql.append("       U.NAME CHECK_NAME,\n");
        sql.append("       PLAN_ID\n");
        sql.append("  FROM TT_PART_PLAN_MAIN A,TC_USER U\n");
        //sql.append(" where A.CREATE_BY=").append(logonUser.getUserId());
        sql.append(" WHERE A.CHECK_BY=U.USER_ID");

        //modify by yuan
        if (!"".equals(year) && year != "") {
            if (!"".equals(month) && month != "") {
                sql.append(" AND to_char(a.year_month,'yyyy-mm')>='" + year + "-" + month + "'");
            } else {
                sql.append("  AND to_char(a.year_month,'yyyy')='" + year + "'");
            }
        } else {
            if (!"".equals(month) && month != "") {
                sql.append(" AND to_char(a.year_month,'mm') >='" + month + "'");
            }
        }
        /*if(!"".equals(year)){
            String Sdate=year + "/" + month +"/01";
			if(month.equals("12")){
				year = (Integer.valueOf(year)+1)+"";
				month = "01";
			}else{
			    month = "0"+(Integer.valueOf(month)+1);
			}
			String Edate=year + "/" + month +"/01";
			sql.append(" and a.YEAR_MONTH>= to_date('").append(Sdate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
			sql.append(" and a.YEAR_MONTH< to_date('").append(Edate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
		}*/
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
            sql.append(" and PLAN_CODE like '%").append(planCode).append("%'");
        }
        if (!"".equals(planerId)) {
            sql.append(" and A.PLANER_ID=").append(planerId);
        }
/*		if(!"".equals(planerId)){
			sql.append(" and PLANER_NAME like '%").append(planerId).append("%'");
		}
*/
        if (!"".equals(sCreateDate)) {
            sql.append(" and trunc(a.CREATE_DATE)>= to_date('").append(sCreateDate).append("','YYYY/MM/dd')");
            sql.append(" and trunc(a.CREATE_DATE)<= to_date('").append(eCreateDate).append("','YYYY/MM/dd')");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and trunc(CHECK_DATE)>= to_date('").append(sSubmitDate).append("','YYYY/MM/dd')");
            sql.append(" and trunc(CHECK_DATE)<= to_date('").append(eSubmitDate).append("','YYYY/MM/dd')");
        }
        sql.append(" and a.STATE='").append(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_03).append("'");
        sql.append(" ORDER BY a.CHECK_DATE DESC");
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
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
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


        sql.append(" select * from TT_PART_PLAN_MAIN a");
        sql.append(" where a.CREATE_BY=").append(logonUser.getUserId());
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

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 获取供应商
     */
    public List<Map<String, Object>> getVender(String id, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT Distinct D.VENDER_ID, D.VENDER_NAME,D.VENDER_CODE, IS_DEFAULT ");
        sql.append(" FROM TT_PART_VENDER_DEFINE D, TT_PART_BUY_PRICE P ");
        sql.append(" WHERE D.VENDER_ID = P.VENDER_ID ");
        if (!"".equals(partId)) {
            sql.append(" and P.PART_ID='").append(partId).append("'");
        }
        sql.append(" AND P.STATE ='").append(Constant.STATUS_ENABLE).append("'");
        sql.append(" AND P.STATUS = 1 ");
        if (!"".equals(id)) {
            sql.setLength(0);
            sql.append(" SELECT D.VENDER_ID, D.VENDER_NAME ");
            sql.append(" FROM TT_PART_VENDER_DEFINE D ");
            sql.append(" WHERE 1=1 ");
            if (Utility.testString(id)) {
                sql.append(" and d.vender_id = " + id);
            }
        }
        sql.append(" AND D.STATE ='").append(Constant.STATUS_ENABLE).append("'");
        sql.append(" AND D.STATUS = 1 ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public String getVersion(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select VER from tt_part_plan_detail where PLINE_ID ='").append(id).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() <= 0) {
            return "";
        }
        return CommonUtils.checkNull(((Map) list.get(0)).get("VER"));
    }

    public String getConfirmedNum(String planId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select sum(BUY_QTY) as sumQty from tt_part_po_dtl ");
        sql.append(" where 1=1 ");
        sql.append(" and part_id='").append(partId).append("'");
        sql.append(" and order_id in (");
        sql.append(" select order_id from tt_part_po_main where plan_id='").append(planId).append("'");
        sql.append(")");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null || list.get(0).get("SUMQTY") == null) {
            return "0";
        }
        return list.get(0).get("SUMQTY").toString();
    }

    public String getVenderCode(String venderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_VENDER_DEFINE where vender_id='").append(venderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null || list.get(0).get("VENDER_CODE") == null) {
            return "";
        }
        return list.get(0).get("VENDER_CODE").toString();
    }

    public TtPartMakerDefinePO getMaker(String makerId) {
        TtPartMakerDefinePO makerDefinePO = null;
        if ("".equals(makerId) || "" == makerId) {
            return null;
        } else {
            makerDefinePO = new TtPartMakerDefinePO();
            makerDefinePO.setMakerId(Long.valueOf(makerId));
            return (TtPartMakerDefinePO) this.select(makerDefinePO).get(0);
        }
    }

}
