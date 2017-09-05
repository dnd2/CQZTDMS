package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartSalesOrderFinCheckDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartSalesOrderFinCheckDao.class);
    private static final PartSalesOrderFinCheckDao dao = new PartSalesOrderFinCheckDao();

    private PartSalesOrderFinCheckDao() {
    }

    public static final PartSalesOrderFinCheckDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
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
    public PageResult<Map<String, Object>> queryPartSoOrder(RequestWrapper request, String orgId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//订单号
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));//订单类型
        String salesOrderId = CommonUtils.checkNull(request.getParamValue("salesOrderId"));//销售单号
        String CstartDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));//制单日期开始
        String CendDate = CommonUtils.checkNull(request.getParamValue("CendDate"));//制单日期结束
        String CHstartDate = CommonUtils.checkNull(request.getParamValue("CHstartDate"));//审核时间开始
        String CHendDate = CommonUtils.checkNull(request.getParamValue("CHendDate"));//审核时间结束
        String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName"));//订货单位
        String buyerCode = CommonUtils.checkNull(request.getParamValue("buyerCode"));//订货单位
        String status = CommonUtils.checkNull(request.getParamValue("status"));//状态
        String state = CommonUtils.checkNull(request.getParamValue("orderstate"));//订单状态
        String isNbdw = CommonUtils.checkNull(request.getParamValue("isNbdw")); // 是否内部单位

        sql.append("SELECT A.*,\n");
        sql.append("       (SELECT NVL(T.WH_NAME, '') AS WH_NAME\n");
        sql.append("          FROM TT_PART_WAREHOUSE_DEFINE T\n");
        sql.append("         WHERE T.WH_ID = A.WH_ID) AS WH_NAME,\n");
        sql.append("       NVL((SELECT D.IS_NBDW\n");
        sql.append("             FROM TM_DEALER D\n");
        sql.append("            WHERE D.DEALER_ID = A.DEALER_ID),\n");
        sql.append("           '0') AS IS_NBDW,\n");
        sql.append("       (SELECT NAME FROM TC_USER B WHERE A.CREATE_BY = B.USER_ID) AS CREATE_BY_NAME,\n");
        sql.append("       (SELECT NVL(ACCOUNT_KY, '0.00')\n");
        sql.append("          FROM VW_PART_DLR_ACCOUNT C\n");
        sql.append("         WHERE C.PARENTORG_ID = '2010010100070674'\n");
        sql.append("           AND C.ACCOUNT_ID = A.ACCOUNT_ID\n");
        sql.append("           AND C.DEALER_ID = A.DEALER_ID) AS ACCOUNT_KY,\n");
        sql.append("       TO_CHAR(A.AMOUNT, 'FM999,999,990.00') AS CONVERS_AMOUNT,\n");
        sql.append("       TD.DEALER_CODE DEALER_CODE2\n");
        sql.append("  FROM TT_PART_SO_MAIN A, TM_DEALER TD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND A.DEALER_ID = TD.DEALER_ID\n");

        if (!"".equals(orderCode)) {
            sql.append(" and  ORDER_CODE like '%").append(orderCode).append("%'");
        }
        if (!"".equals(orderType)) {
            sql.append(" and  ORDER_TYPE='").append(orderType).append("'");
        }
        if (!"".equals(salesOrderId)) {
            sql.append(" and  SO_CODE like '%").append(salesOrderId).append("%'");
        }
        if (!"".equals(CstartDate) && !"".equals(CendDate)) {
            sql.append(" and SUBMIT_DATE>= to_date('").append(CstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and SUBMIT_DATE<= to_date('").append(CendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orgId)) {
            sql.append(" and SELLER_ID =").append(orgId);
        }
        if (!"".equals(buyerName)) {
            sql.append(" and  A.DEALER_NAME like '%").append(buyerName).append("%'");
        }
        if (!"".equals(buyerCode)) {
            sql.append(" and  upper(TD.DEALER_CODE) like upper ('%").append(buyerCode).append("%')");
        }
        if (!"".equals(state)) {
            sql.append(" and STATE=" + state + "");
        }
        if (null != isNbdw && !"".equals(isNbdw)) {
            if ("1".equals(isNbdw)) {
                sql.append(" AND EXISTS ( SELECT 1 FROM TM_DEALER D WHERE D.DEALER_ID = A.DEALER_ID AND D.IS_NBDW = '" + isNbdw + "' ) ");
            } else if ("0".equals(isNbdw)) {
                sql.append(" AND EXISTS ( SELECT 1 FROM TM_DEALER D WHERE D.DEALER_ID = A.DEALER_ID AND D.IS_NBDW in(0,2) ) ");
            }

        }
        sql.append(" and STATE in(" + Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "," + Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + ") and PICK_ORDER_ID is null ");
        sql.append(" order by a.submit_date  ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
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
    public List<Map<String, Object>> queryPartSoOrderForExcel(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//订单号
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));//订单类型
        String salesOrderId = CommonUtils.checkNull(request.getParamValue("salesOrderId"));//销售单号
        String CstartDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));//制单日期开始
        String CendDate = CommonUtils.checkNull(request.getParamValue("CendDate"));//制单日期结束
        String CHstartDate = CommonUtils.checkNull(request.getParamValue("CHstartDate"));//审核时间开始
        String CHendDate = CommonUtils.checkNull(request.getParamValue("CHendDate"));//审核时间结束
        String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName"));//订货单位
        String status = CommonUtils.checkNull(request.getParamValue("status"));//状态
        sql.append(" select a.*,(select nvl(t.wh_name,'') as wh_name from TT_PART_WAREHOUSE_DEFINE t where t.WH_ID=a.wh_id) as WH_NAME ");
        sql.append(" ,(select NAME from TC_USER b where a.CREATE_BY = b.USER_ID) as CREATE_BY_NAME ");
        sql.append(" ,(select nvl(ACCOUNT_KY,'0.00') from VW_PART_ACCOUNT c where c.PARENTORG_ID='").append(Constant.OEM_ACTIVITIES).append("'");
        sql.append(" and  c.DEALER_ID =a.DEALER_ID) as ACCOUNT_KY ");
        sql.append(" from TT_PART_SO_MAIN a ");
        sql.append(" left join TC_CODE R on r.code_id=a.state ");
        sql.append(" where 1=1 ");
        if (!"".equals(orderCode)) {
            sql.append(" and  ORDER_CODE='").append(orderCode).append("'");
        }
        if (!"".equals(orderType)) {
            sql.append(" and  ORDER_TYPE='").append(orderType).append("'");
        }
        if (!"".equals(salesOrderId)) {
            sql.append(" and  SO_CODE='").append(salesOrderId).append("'");
        }
        if (!"".equals(CstartDate) && "".equals(CendDate)) {
            sql.append(" and CREATE_DATE>= to_date('").append(CstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and CREATE_DATE<= to_date('").append(CendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(CHstartDate) && "".equals(CHendDate)) {
            sql.append(" and FCAUDIT_DATE>= to_date('").append(CHstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and FCAUDIT_DATE<= to_date('").append(CHendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(buyerName)) {
            sql.append(" and  DEALER_NAME like '%").append(buyerName).append("%'");
        }
        sql.append(" and STATE='").append(Constant.CAR_FACTORY_SALE_ORDER_STATE_02).append("'");
        sql.append(" order by a.create_date ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }


    public String getPrice(String venderId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select buy_price from tt_part_buy_price where vender_id='").append(venderId).append("'");
        sql.append(" and part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("BUY_PRICE")) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("BUY_PRICE"));
    }

    public String getWhName(String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select wh_name from tt_part_warehouse_define where wh_id='").append(whId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0)) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("WH_NAME"));
    }
}
