package com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PurchasePlanSettingDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PurchasePlanSettingDao.class);
    private static final PurchasePlanSettingDao dao = new PurchasePlanSettingDao();

    private PurchasePlanSettingDao() {
    }

    public static final PurchasePlanSettingDao getInstance() {
        return dao;
    }

    private static final int defVal = Constant.IF_TYPE_YES;//默认供应商
    private static final int enableVal = Constant.STATUS_ENABLE;//有效

    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 获取库房
     */
    public List<Map<String, Object>> getWareHouse(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT d.wh_id,d.wh_name\n");
        sql.append("  FROM TT_PART_PLANER_WH_RELATION R, TT_PART_WAREHOUSE_DEFINE D\n");
        sql.append(" WHERE R.WH_ID = D.WH_ID\n");
        sql.append("    AND r.planer_id = " + userId);
        sql.append("    AND r.state =" + Constant.STATUS_ENABLE);
        sql.append("  group by d.wh_id,d.wh_name   ");

        List<Map<String, Object>> wareHoustList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    /**
     * @param orgId
     * @return
     */
    public List<Map<String, Object>> getWareHouse(String orgId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT D.WH_ID, D.WH_NAME\n");
        sql.append("  FROM TT_PART_WAREHOUSE_DEFINE D\n");
        sql.append(" WHERE D.ORG_ID = " + orgId + "\n");
        sql.append("   AND D.STATE = 10011001\n");
        sql.append("   group by D.WH_ID, D.WH_NAME\n");
        sql.append(" ORDER BY D.WH_ID");
        List<Map<String, Object>> wareHoustList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    /**
     * @param orgId
     * @return
     */
    public List<Map<String, Object>> getWareHouse2(String orgId) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT Distinct d.wh_id,d.wh_name\n");
        sbSql.append("  FROM TT_PART_WAREHOUSE_DEFINE D\n");
        sbSql.append(" WHERE 1=1");
        sbSql.append("   AND D.org_Id = " + orgId);
        sbSql.append("   AND D.state =" + Constant.STATUS_ENABLE);
        sbSql.append(" ORDER BY D.WH_ID ");
        List<Map<String, Object>> wareHoustList = this.pageQuery(sbSql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 查询计划
     */
    public PageResult<Map<String, Object>> queryPurchasePlan(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//计划年
        String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//计划月
        String planCode = CommonUtils.checkNull(request.getParamValue("PLAN_CODE"));//计划单号
        String whName = CommonUtils.checkNull(request.getParamValue("wh_id"));//库房
        String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_NAME"));//计划员
        String planType = CommonUtils.checkNull(request.getParamValue("PLAN_TYPE"));//计划类型
        String createSDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));//制单日期
        String createEDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));//制单日期
        String createType = CommonUtils.checkNull(request.getParamValue("CREATE_TYPE"));//生成方式
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.PLAN_ID,PLAN_CODE,A.CREATE_DATE,A.WH_NAME,to_char(YEAR_MONTH,'yyyy-mm') YEAR_MONTH,");
        sql.append("(SELECT COUNT(*) FROM TT_PART_PLAN_DETAIL T WHERE T.PLAN_ID=A.PLAN_ID) DTL_COUNT,");
        sql.append("A.PLAN_TYPE,to_char(A.SUM_QTY,'fm999,999,999') SUM_QTY,to_char(A.AMOUNT,'FM999,999,999,999,990.00') AMOUNT,A.CREATE_TYPE, B.NAME\n");
        sql.append("  FROM TT_PART_PLAN_MAIN A\n");
        sql.append("  JOIN TC_USER B\n");
        sql.append("    ON A.PLANER_ID = B.USER_ID\n");
        sql.append(" WHERE 1 = 1\n");
        if (!"".equals(planerId)) {
            sql.append(" AND A.PLANER_ID=").append(planerId);
        }
        //modify by yuan
        if (!"".equals(year) && year != "") {
            if (!"".equals(month) && month != "") {
                sql.append(" AND to_char(a.year_month,'yyyy-mm')>='" + year + "-" + month + "'");
            } else {
                sql.append("  AND to_char(a.year_month,'yyyy')='" + year + "'");
            }
        } else {
            if (!"".equals(month) && month != "") {
                sql.append(" AND to_char(a.year_month,'mm') ='" + month + "'");
            }
        }

        if (!"".equals(planCode)) {
            sql.append(" and PLAN_CODE like '%").append(planCode).append("%'");
        }
        if (!"".equals(whName)) {
            sql.append(" and WH_ID='").append(whName).append("'");
        }
        if (!"".equals(planType)) {
            sql.append(" and PLAN_TYPE='").append(planType).append("'");
        }

        if (!"".equals(createSDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(createSDate).append(" 0:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(createEDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(createType)) {
            sql.append(" and a.CREATE_TYPE='").append(createType).append("'");
        }
        sql.append(" and a.plan_type =" + request.getParamValue("planType"));
        sql.append(" and a.STATE in ('").append(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_01).append("','").append(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_04).append("')");
        sql.append(" order by a.create_date DESC \n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 查询配件
     */
    public PageResult<Map<String, Object>> showPartBase(RequestWrapper req, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String whId = CommonUtils.checkNull(req.getParamValue("wh_id"));
        String beginTime1 = CommonUtils.checkNull(req.getParamValue("beginTime2"));//预计到货时间
      //  String planCycle = CommonUtils.checkNull(req.getParamValue("PLAN_CYCLE"));//订货周期
      //  String comeCycle = CommonUtils.checkNull(req.getParamValue("COME_CYCLE"));  //到货周期
        String venderId = CommonUtils.checkNull(req.getParamValue("VENDER_ID"));//供应商id
        String makerId = CommonUtils.checkNull(req.getParamValue("MAKER_ID"));//制造商id
        String selectOp = CommonUtils.checkNull(req.getParamValue("SELECT_OP"));//计划数量选择条件
        String planQty = CommonUtils.checkNull(req.getParamValue("PLANQTY"));//计划数量
        String negativeFilt = CommonUtils.checkNull(req.getParamValue("NEGATIVE_FILT"));//计划数量

      //  double planCycle1 = CommonUtils.parseDouble(planCycle);
       // double comeCycle1 = CommonUtils.parseDouble(comeCycle);
      //  double allCycle = Arith.add(planCycle1, comeCycle1);
        //modify by yuan 2013-04-29

					
					
			
			sql.append(" WITH PART_STOCK AS\n");
			sql.append("  (SELECT PS.PART_ID, SUM(PS.NORMAL_QTY) NORMAL_QTY\n");
			sql.append("     FROM VW_PART_STOCK PS\n");
			sql.append("    WHERE 1 = 1\n");
			sql.append("      AND ORG_ID = 2010010100070674\n");
			sql.append("    GROUP BY PS.PART_ID)\n");
			sql.append(" SELECT TPD.PART_ID,\n");
			sql.append("        TPD.PART_CODE,\n");
			sql.append("        TPD.PART_OLDCODE,\n");
			sql.append("        TPD.PART_CNAME,\n");
			sql.append("        TPD.UNIT,\n");
			sql.append("        tpdd.VENDER_ID,\n");
			sql.append("        TPDD.VENDER_NAME,\n");
			sql.append("        TPBD.BO_ODDQTY BO_NUM,\n");
			sql.append("        TPD.PRODUCE_STATE PRODUCE_STATE,\n");
			sql.append("        SYSDATE + NVL(TPD.DELIVER_PERIOD, 30) DELIVER_PERIOD,\n");
			sql.append("        TPBP.MIN_PACKAGE,\n");
			sql.append("        nvl(CASE\n");
			sql.append("          WHEN TPS.NORMAL_QTY < 0 THEN\n");
			sql.append("           0\n");
			sql.append("          ELSE\n");
			sql.append("           TPS.NORMAL_QTY\n");
			sql.append("        END,0) QTY\n");
			sql.append("   FROM TT_PART_DEFINE TPD\n");
			sql.append("   LEFT JOIN (SELECT SUM(BO_ODDQTY) BO_ODDQTY, PART_ID, ORG_ID\n");
			sql.append("                FROM VW_PART_BO_YX\n");
			sql.append("               GROUP BY PART_ID, ORG_ID) TPBD\n");
			sql.append("     ON TPBD.PART_ID = TPD.PART_ID\n");
			sql.append("    AND TPBD.ORG_ID = 2010010100070674\n");
			sql.append("   LEFT JOIN PART_STOCK TPS\n");
			sql.append("     ON TPS.PART_ID = TPD.PART_ID\n");
			sql.append("   LEFT JOIN TT_PART_BUY_PRICE TPBP\n");
			sql.append("     ON TPD.PART_ID = TPBP.PART_ID\n");
			sql.append("   LEFT JOIN TT_PART_VENDER_DEFINE TPDD\n");
			sql.append("     ON TPBP.VENDER_ID = TPDD.VENDER_ID\n");
			sql.append("  WHERE 1 = 1\n");
			sql.append("    AND TPD.STATE = 10011001\n");


        if (!venderId.equals("")) {
            sql.append(" AND TPDD.VENDER_ID = ").append(venderId).append("\n");
        }

        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(TPD.PART_CODE) like '%").append(req.getParamValue("PART_CODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(TPD.PART_OLDCODE) like '%").append(req.getParamValue("PART_OLDCODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and TPD.PART_CNAME like '%").append(req.getParamValue("PART_CNAME")).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getAttribute("PART_OLDCODE")))) {
            sql.append(" and TPD.PART_OLDCODE ='").append(req.getAttribute("PART_OLDCODE")).append("'");
        }
		

			sql.append(" GROUP BY TPD.PART_ID,\n");
			sql.append("           TPD.PART_CODE,\n");
			sql.append("           TPD.PART_OLDCODE,\n");
			sql.append("           TPD.PART_CNAME,\n");
			sql.append("           TPD.PRODUCE_STATE,\n");
			sql.append("           TPD.UNIT,\n");
			sql.append("           TPS.NORMAL_QTY,\n");
			sql.append("           TPBD.BO_ODDQTY,\n");
			sql.append("           TPBP.MIN_PACKAGE,\n");
			sql.append("           TPD.DELIVER_PERIOD,\n");
			sql.append("           TPDD.VENDER_ID,\n");
			sql.append("           TPDD.VENDER_NAME");


        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param planId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 查询详细MAIN
     */
    public Map<String, Object> queryPlanMainView(String planId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.PLAN_CODE, \n");
        sql.append("        to_char(a.CREATE_DATE, 'yyyy-mm-dd') CREATE_DATE, \n");
        sql.append("        to_char(a.YEAR_MONTH, 'yyyy-mm-dd') YEAR_MONTH, \n");
        sql.append("        a.PLAN_TYPE, \n");
        sql.append("        a.WH_NAME, \n");
        sql.append("        a.WH_ID, \n");
        sql.append("        a.PLAN_CYCLE, \n");
        sql.append("        a.COME_CYCLE, \n");
        sql.append("        to_char(a.SUM_QTY, 'fm999,999,999') SUM_QTY, \n");
        sql.append("        to_char(a.AMOUNT, 'FM999,999,999,999,990.00') AMOUNT, \n");
        sql.append("        a.PLANER_ID,\n");
        sql.append("        a.PLANER_NAME as NAME, \n");
        sql.append("        a.PLAN_ID, \n");
        sql.append("        to_char(B.FORECAST_DATE,'yyyy-MM-dd') FORECAST_DATE, \n");
        sql.append("        a.REMARK, \n");
        sql.append("        a.IS_URGENT_IN, \n");
        sql.append("        to_char(nvl(AMOUNT, '0'), 'FM999,999,999,999,990.00') as converseAmount \n");
        sql.append("   from TT_PART_PLAN_MAIN a,TT_PART_PLAN_DETAIL B \n");
        sql.append("  where  A.PLAN_ID=B.PLAN_ID \n");
        sql.append("    AND ROWNUM = 1 \n");
        sql.append("    and a.PLAN_ID = ").append(planId);

        Map<String, Object> map = this.pageQueryMap(sql.toString(), null, this.getFunName());
        return map;
    }

    /**
     * @param : @param planId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 查询详细DETAIL
     */
    public List<Map<String, Object>> queryPlanDetailView(String planId, String partOldCode, String partCname, String venderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT DISTINCT A.*,\n");
        sql.append("                (SELECT B.SALE_PRICE3\n");
        sql.append("                   FROM TT_PART_SALES_PRICE B\n");
        sql.append("                  WHERE A.PART_ID = B.PART_ID\n");
        sql.append("                    AND B.STATUS = '1'\n");
        sql.append("                    AND B.STATE = 10011001) BUY_PRICE,\n");
        sql.append("                NVL(A.VER, '') VERSION,\n");
        sql.append("                (SELECT C.VENDER_ID\n");
        sql.append("                   FROM TT_PART_BUY_PRICE C\n");
        sql.append("                  WHERE A.PART_ID = C.PART_ID\n");
        sql.append("                    AND C.IS_DEFAULT = 10041001) VENDER_ID\n");
        sql.append("  FROM TT_PART_PLAN_DETAIL A\n");

        sql.append("  WHERE 1=1 \n");
        sql.append("    AND A.PLAN_ID = ").append(planId);

        if (!"".equals(partOldCode)) {
            sql.append(" and a.PART_OLDCODE like '%").append(partOldCode).append("%'\n");
        }
        if (!"".equals(partCname)) {
            sql.append(" and a.PART_CNAME like '%").append(partCname).append("%'\n");
        }
        if (!"".equals(venderId)) {
            sql.append("AND EXISTS (SELECT 1 FROM tt_part_buy_price p WHERE  a.vender_id=p.vender_id and p.part_id=a.part_id AND p.vender_id=").append(venderId).append(")\n");
        }
        //add by yuan 20131011 start
        sql.append("AND EXISTS (SELECT 1\n");
        sql.append("        FROM TT_PART_PO_DTL PD, TT_PART_PO_MAIN PM\n");
        sql.append("       WHERE 1 = 1\n");
        sql.append("         AND PD.ORDER_ID = PM.ORDER_ID\n");
        sql.append("         AND PD.PART_ID = A.PART_ID\n");
        sql.append("         AND A.PLAN_ID = PM.PLAN_ID HAVING\n");
        sql.append("       NVL(SUM(PD.BUY_QTY), 0) < A.CHECK_NUM)\n");
        //end
        sql.append(" order by LINE_NO \n");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public List<Map<String, Object>> queryPartPlan(RequestWrapper request) {
        String year = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//计划年
        String month = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//计划月
        String planCode = CommonUtils.checkNull(request.getParamValue("PLAN_CODE"));//计划单号
        String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME"));//库房
        String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
        String planType = CommonUtils.checkNull(request.getParamValue("PLAN_TYPE"));//计划类型
        String createSDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));//制单日期
        String createEDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));//制单日期
        String createType = CommonUtils.checkNull(request.getParamValue("CREATE_TYPE"));//生成方式
        StringBuffer sql = new StringBuffer();
        sql.append(" select *,b.CODE_DESC PLAN_TYPE_NAME from TT_PART_PLAN_MAIN a ");
        sql.append(" left join TC_CODE b on a.PLAN_TYPE=b.CODE_ID and b.type_id='").append(Constant.PART_PURCHASE_PLAN_CREATE_TYPE).append("'");
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
            sql.append("and YEAR_MONTH>= to_date('").append(Sdate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append("and YEAR_MONTH< to_date('").append(Edate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(planCode)) {
            sql.append(" and PLAN_CODE like '%").append(planCode).append("%'");
        }
        if (!"".equals(whName)) {
            sql.append(" and WH_NAME='").append(whName).append("'");
        }
        if (!"".equals(planerId)) {
            sql.append(" and PLANER_ID='").append(planerId).append("'");
        }
        if (!"".equals(planType)) {
            sql.append(" and PLAN_TYPE='").append(planType).append("'");
        }

        if (!"".equals(createSDate)) {
            sql.append("and CREATE_DATE>= to_date('").append(createSDate).append(" 0:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append("and CREATE_DATE<= to_date('").append(createEDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(createType)) {
            sql.append(" and CREATE_TYPE='").append(createType).append("'");
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public String getPartId(String partCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select PART_ID from TT_PART_DEFINE  ");
        sql.append(" where PART_CODE='").append(partCode).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() <= 0) {
            return "";
        }
        return ((Map) list.get(0)).get("PART_ID").toString();
    }

    public Map<String, Object> getData(String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.PART_ID,a.PART_CODE ,a.PART_OLDCODE , a.PART_CNAME,a.STATE,a.UNIT,nvl(b.MIN_PACKAGE,'0') as MIN_PACKAGE,");
        sql.append(" nvl(c.SALE_PRICE3,'0') SALE_PRICE3 ,nvl(d.ITEM_QTY,'0') ITEM_QTY ,nvl(d.BO_QTY,'0') BO_QTY,nvl(d.ORDER_QTY,'0') ORDER_QTY,nvl(d.SAFETY_STOCK,'0') SAFETY_STOCK ,");
        sql.append(" nvl(d.AVG_QTY,'0') AVG_QTY, nvl(b.BUY_PRICE,'0') BUY_PRICE,trunc(( SYSDATE+20 )) as preArriveDate ");
        sql.append(" FROM TT_PART_DEFINE a ");
        sql.append(" left join  TT_PART_BUY_PRICE b on b.PART_ID=a.PART_ID and  b.STATUS='1'").append(" and b.STATE='").append(Constant.STATUS_ENABLE).append("'");
        sql.append(" left join  TT_PART_SALES_PRICE c on c.PART_ID=a.PART_ID and c.state='").append(Constant.STATUS_ENABLE).append("'");
        sql.append(" left join  TT_PART_PLAN_DEFINE d on d.PART_ID=a.PART_ID ");
        sql.append(" where a.PART_ID='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() <= 0) {
            return null;
        }


        return list.get(0);
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

    public List<Map<String, Object>> getVenderList() {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_vender_define t where t.state='");
        sql.append(Constant.STATUS_ENABLE).append("'");
        sql.append(" and t.status=1");
        List<Map<String, Object>> venderList = this.pageQuery(sql.toString(), null, this.getFunName());
        return venderList;
    }


    public PageResult<Map<String, Object>> queryForeCastPartList(
            String partOldCode, String partName, String partCode, String foreCastBeginTime,
            String foreCastEndTime,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("         T.PLAN_QTY, \n");
            sql.append("        D.BUY_MIN_PKG, \n");
            sql.append("        T.FORECAST_DATE \n");
            sql.append("   FROM TT_PART_PO_MAIN T1, TT_PART_PO_DTL T, TT_PART_DEFINE D \n");
            sql.append("  WHERE T1.ORDER_ID = T.ORDER_ID \n");
            sql.append("    AND T.PART_ID = D.PART_ID \n");
            sql.append("    and t1.BUYER_TYPE=15061001 \n"); //20170818 add

            if (!partOldCode.equals("")) {
                sql.append(" AND upper(T.PART_OLDCODE) LIKE upper('%")
                        .append(partOldCode).append("%')\n");
            }
            if (!partCode.equals("")) {
                sql.append(" AND upper(T.partCode) LIKE upper('%")
                        .append(partCode).append("%')\n");
            }
            if (!partName.equals("") && partName != null) {
                sql.append(" AND T.PART_CNAME LIKE '%")
                        .append(partName).append("%'\n");
            }

            if (!"".equals(foreCastBeginTime)) {
                sql.append(" AND to_date(T.FORECAST_DATE)>=").append("to_date('").append(foreCastBeginTime).append("','yyyy-MM-dd') \n");
            }

            if (!"".equals(foreCastEndTime)) {
                sql.append(" AND to_date(T.FORECAST_DATE)<=").append("to_date('").append(foreCastEndTime).append("','yyyy-MM-dd') \n");
            }

            sql.append("  ORDER BY T.PART_OLDCODE ASC, T.FORECAST_DATE ASC \n");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPlanDetailView1(String planId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select rownum R_NUM,a.PLINE_ID,a.PLAN_ID,a.LINE_NO,a.PART_ID,a.PART_CODE,a.PART_OLDCODE,a.PART_CNAME,a.UNIT,a.MIN_PACKAGE,");
        sql.append("a.PLAN_PRICE,a.PLAN_QTY,a.PLAN_AMOUNT,a.CHECK_NUM,a.CHECK_AMOUNT,a.STOCK_QTY,a.BO_QTY,a.AVG_QTY,a.ZT_NUM,");
        sql.append("a.SFATE_STOCK,a.FORECAST_DATE,a.REMARK,a.CREATE_DATE,a.CREATE_BY,a.ORG_ID,a.UPDATE_DATE,a.UPDATE_BY,a.STATUS,a.LOGIC_QTY,");
        sql.append("a.YEAR_QTY,a.HFYEAR_QTY,a.QUARTER_QTY,a.VENDER_ID,v.VENDER_NAME,a.PART_TYPE,");
        sql.append("       DECODE(A.PART_TYPE,\n")
                .append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",'自制件',")
                .append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",'国产件',")
                .append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",'进口件') PARTTYPENAME\n");
        sql.append(",B.SALE_PRICE3 BUY_PRICE,\n");
        sql.append("nvl(a.VER,'') VERSION  from  TT_PART_PLAN_DETAIL a,TT_PART_SALES_PRICE b,TT_PART_VENDER_DEFINE v ");
        sql.append(" where a.PART_ID = b.PART_ID(+) and a.vender_id=v.vender_id(+) ").append(" and b.STATUS='1'").append(" and b.STATE='").append(Constant.STATUS_ENABLE).append("'");
        sql.append(" and a.PLAN_ID='").append(planId).append("'");
        sql.append(" order by a.PART_OLDCODE ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public List<Map<String, Object>> queryPlanDetailView2(String planId, String whId, String planType, double planCycle) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*, \n");
        sql.append("        ROUND(NVL(D.SAFETY_STOCK, '0') + NVL(D.AVG_QTY, '0') * ").append(planCycle);
        sql.append("              +NVL(D.BO_QTY, '0') - NVL((SELECT S.NORMAL_QTY \n");
        sql.append("                                         FROM VW_PART_STOCK S \n");
        sql.append("                                        WHERE S.WH_ID = D.WH_ID \n");
        sql.append("                                          AND S.PART_ID = D.PART_ID), \n");
        sql.append("                                       '0') - NVL(D.ORDER_QTY, '0')) GPLAN_QTY, \n");
        sql.append("        B.SALE_PRICE3 BUY_PRICE, \n");
        sql.append("        nvl(a.VER, '') VERSION \n");
        sql.append("   from TT_PART_PLAN_DETAIL a \n");
        sql.append("   left join TT_PART_SALES_PRICE b \n");
        sql.append("     on a.PART_ID = b.PART_ID \n");
        sql.append("    and b.STATUS = '1' \n");
        sql.append("    and b.STATE = '10011001' \n");
        sql.append("    left join TT_PART_PLAN_DEFINE D \n");
        sql.append("    ON A.PART_ID=D.PART_ID \n");
        sql.append("     AND D.WH_ID=").append(whId);
        sql.append("     AND d.plan_type = DECODE(").append(planType).append(", 92111001, 1, 2) \n");
        sql.append("  where 1 = 1 \n");
        sql.append("    and a.PLAN_ID = ").append(planId);
        sql.append("  order by LINE_NO");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }


    public List<Map<String, Object>> getPartInfo(RequestWrapper req) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            StringBuffer sql = new StringBuffer("");
            String whId = CommonUtils.checkNull(req.getParamValue("wh_id"));
            String beginTime1 = CommonUtils.checkNull(req.getParamValue("beginTime2"));//预计到货时间
            String planCycle = CommonUtils.checkNull(req.getParamValue("PLAN_CYCLE"));//订货周期
            String comeCycle = CommonUtils.checkNull(req.getParamValue("COME_CYCLE"));  //到货周期
            String venderId = CommonUtils.checkNull(req.getParamValue("VENDER_ID"));//供应商id
            String makerId = CommonUtils.checkNull(req.getParamValue("MAKER_ID"));//制造商id

            double planCycle1 = CommonUtils.parseDouble(planCycle);
            double comeCycle1 = CommonUtils.parseDouble(comeCycle);
            double allCycle = Arith.add(planCycle1, comeCycle1);

            sql.append("SELECT rownum R_NUM, A.PART_ID,\n");
            sql.append("       A.PART_CODE,\n");
            sql.append("       A.PART_OLDCODE,\n");
            sql.append("       A.PART_CNAME,\n");
            sql.append("       A.PART_TYPE,\n");
            sql.append("       DECODE(A.PART_TYPE,\n")
                    .append(Constant.PART_BASE_PART_TYPES_SELF_MADE).append(",'自制件',")
                    .append(Constant.PART_BASE_PART_TYPES_PURCHASE).append(",'国产件',")
                    .append(Constant.PART_BASE_PART_TYPES_ENTRANCE).append(",'进口件') PARTTYPENAME,\n");
            sql.append("       VE.VENDER_ID,\n");
            sql.append("       VE.VENDER_NAME,\n");
            sql.append("       A.STATE,\n");
            sql.append("       NVL(UNIT,'件') UNIT,\n");
            sql.append("       NVL(B.MIN_PACKAGE, '1') AS MIN_PACKAGE,\n");

            sql.append("to_char(ROUND((SELECT P.SALE_PRICE3\n");
            sql.append("              FROM TT_PART_SALES_PRICE P\n");
            sql.append("             WHERE P.PART_ID = A.PART_ID\n");
            sql.append("               AND P.STATE = 10011001\n");
            sql.append("               AND P.STATUS = 1),\n");
            sql.append("            2))SALE_PRICE3, --计划价\n");
/*	        sql.append("to_char(ROUND((SELECT P.SALE_PRICE3\n");
            sql.append("              FROM TT_PART_SALES_PRICE P\n");
	        sql.append("             WHERE P.PART_ID = A.PART_ID\n");
	        sql.append("               AND P.STATE = 10011001\n");
	        sql.append("               AND P.STATUS = 1),\n");
	        sql.append("            2),'fm999,999,990.00') SALE_PRICE3, --计划价\n");
*/
            sql.append("round((SELECT P.SALE_PRICE3\n");
            sql.append("              FROM TT_PART_SALES_PRICE P\n");
            sql.append("             WHERE P.PART_ID = A.PART_ID\n");
            sql.append("               AND P.STATE = 10011001\n");
            sql.append("               AND P.STATUS = 1)*M.QTY,2) AMOUNT,\n");

            sql.append("NVL((SELECT S.NORMAL_QTY\n");
            sql.append("           FROM VW_PART_STOCK S\n");
            sql.append("          WHERE S.WH_ID = D.WH_ID\n");
            sql.append("            AND S.PART_ID = D.PART_ID),\n");
            sql.append("         '0') ITEM_QTY,\n");

            sql.append("       NVL(D.BO_QTY, '0') BO_QTY,\n");
            sql.append("       NVL(D.ORDER_QTY, '0') ORDER_QTY,\n");
            sql.append("       NVL(D.SAFETY_STOCK, '0') SAFETY_STOCK,\n");
            sql.append("       NVL(D.AVG_QTY, '0') AVG_QTY,\n");
            sql.append("       NVL(D.YEAR_QTY, '0') YEAR_QTY,\n");
            sql.append("       NVL(D.HFYEAR_QTY, '0') HFYEAR_QTY,\n");
            sql.append("       NVL(D.QUARTER_QTY, '0') QUARTER_QTY,\n");
            sql.append("       NVL(B.BUY_PRICE, '0') BUY_PRICE,\n");
//	        sql.append("       to_char(NVL(B.BUY_PRICE, '0'),'fm999,999,990.00') BUY_PRICE,\n");
            sql.append("ROUND (NVL(D.SAFETY_STOCK, '0') + NVL(D.AVG_QTY, '0') * ").append(allCycle).append(" +NVL(D.BO_QTY, '0')");
            sql.append("-NVL((SELECT S.NORMAL_QTY FROM VW_PART_STOCK S WHERE S.WH_ID = D.WH_ID AND S.PART_ID = D.PART_ID),'0')");
            sql.append("-NVL(D.ORDER_QTY, '0')) PLAN_QTY,\n");
            sql.append("'" + beginTime1 + "'").append(" AS PREARRIVEDATE, \n");
            sql.append(" M.QTY,M.REMARK \n");
            sql.append("  FROM TT_PART_DEFINE A\n");
            sql.append("  LEFT JOIN TT_PART_BUY_PRICE B\n");
            sql.append("    ON B.PART_ID = A.PART_ID\n");
            sql.append("   AND B.STATUS = '1'\n");
            sql.append("   AND A.STATE = '" + enableVal + "'\n");
            sql.append(" AND A.STATUS=1");
            sql.append("   AND B.STATE = '" + enableVal + "'\n");
            sql.append("   AND B.IS_DEFAULT = '" + defVal + "'\n");
            sql.append("  LEFT JOIN TT_PART_PLAN_DEFINE D\n");
            sql.append("    ON D.PART_ID = A.PART_ID\n");
            sql.append(" and D.wh_id='").append(whId).append("'");
            sql.append("  AND d.plan_type=DECODE(").append(req.getParamValue("planType")).append(",92111001,1,2)");
            sql.append(" JOIN TT_PART_VENDER_DEFINE VE \n");
            sql.append(" ON B.VENDER_ID = VE.VENDER_ID \n");
            sql.append(" JOIN TMP_PART_UPLOAD M ON A.PART_OLDCODE=M.PART_OLDCODE");
            sql.append(" WHERE M.QTY > 0\n");
            list = this.pageQuery(sql.toString(), null, this.getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * @param orgCode
     * @return
     */
    public List<Map<String, Object>> getDepartment(String orgCode) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT T.DEPARTMENT_CODE, T.DEPARTMENT_NAME\n");
        sql.append("  FROM TT_VS_DEPARTMENT T\n");
        sql.append(" WHERE T.ORG_CODE = '" + orgCode + "'\n");
        sql.append(" ORDER BY T.DEPARTMENT_CODE");
        List<Map<String, Object>> wareHoustList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    /**
     * @param orgId
     * @return
     */
    public List<Map<String, Object>> getWareHouse(String orgId, String wh_Type) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT D.WH_ID, D.WH_NAME\n");
        sql.append("  FROM TT_PART_WAREHOUSE_DEFINE D\n");
        sql.append(" WHERE D.ORG_ID = " + orgId + "\n");
        sql.append("   AND D.STATE = 10011001\n");
        if (!"".equals(wh_Type) && "" != wh_Type) {
            sql.append("   AND D.WH_TYPE = " + wh_Type + "\n");
        }
        sql.append(" ORDER BY D.WH_NAME");
        List<Map<String, Object>> wareHoustList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wareHoustList;
    }
}