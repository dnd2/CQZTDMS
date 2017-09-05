package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.po.TtPartUserposeDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.*;

public class PartSoManageDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartSoManageDao.class);
    private static final PartSoManageDao dao = new PartSoManageDao();

    private PartSoManageDao() {
    }

    public static final PartSoManageDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> showPartBase(RequestWrapper req, String dealerId, String sellerId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT， NVL((SELECT SUM(S.normal_QTY)\n");
        sql.append("                                     FROM vw_part_stock S\n");
        sql.append("                                    WHERE S.PART_ID = A.PART_ID\n");
        sql.append("                                      AND S.ORG_ID = ").append(sellerId);
        sql.append("                                      ");
        sql.append("                                       ), 0) AS ITEM_QTY,\n");
        sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
        sql.append("                    FROM TM_DEALER D\n");
        sql.append("                   WHERE D.DEALER_ID = ").append(dealerId);
        sql.append("                     AND D.STATUS = 10011001),\n");
        sql.append("                  92101001,\n");
        sql.append("                  A.MIN_PACK1,\n");
        sql.append("                  A.MIN_PACK2),\n");
        sql.append("           0) AS MIN_PACKAGE,\n");
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(").append(dealerId).append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
        sql.append("(SELECT CASE\n");
        sql.append("               WHEN SUM(ITEM_QTY) > 0 THEN\n");
        sql.append("                'Y'\n");
        sql.append("               WHEN SUM(ITEM_QTY) = 0 THEN\n");
        sql.append("                'N'\n");
        sql.append("               ELSE\n");
        sql.append("                'N'\n");
        sql.append("             END\n");
        sql.append("        FROM TT_PART_ITEM_STOCK E\n");
        sql.append("       WHERE E.PART_ID = A.PART_ID\n");
        sql.append("         AND E.STATE = 1\n");
        sql.append("         AND E.STATUS = 1\n");
        sql.append("         AND E.ORG_ID = ").append(sellerId).append(") AS UPORGSTOCK\n");

        sql.append("  FROM TT_PART_DEFINE A\n");
        sql.append(" WHERE /*A.STATE = 10011001\n");
        sql.append("   AND*/\n");
        sql.append(" A.STATUS = 1\n");


        String uploadFlag = CommonUtils.checkNull(req.getAttribute("uploadFlag"));
        if (!"".equals(uploadFlag) && "upload".equals(uploadFlag)) {
            if (!"".equals(CommonUtils.checkNull(req.getAttribute("partOldcode")))) {
                sql.append(" and a.PART_OLDCODE ='").append(req.getAttribute("partOldcode")).append("'");
            }
            sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;
        }


        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and a.PART_CODE like '%").append(req.getParamValue("PART_CODE")).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and a.PART_OLDCODE like '%").append(req.getParamValue("PART_OLDCODE")).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and a.PART_CNAME like '%").append(req.getParamValue("PART_CNAME")).append("%'");
        }
        if (CommonUtils.checkNull(req.getParamValue("transType")).equals("2")) {
            sql.append(" and a.PACK_STATE <>'").append(Constant.PART_PACK_STATE_02 + "").append("'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                sql.append(" and A.IS_PLAN ='").append(Constant.IF_TYPE_YES).append("'");
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                sql.append(" and A.IS_DIRECT ='").append(Constant.IF_TYPE_YES).append("'");
            }
        }


        sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
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
    public PageResult<Map<String, Object>> queryPartSoOrder(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//订单号
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String salesOrderId = CommonUtils.checkNull(request.getParamValue("salesOrderId"));//销售单号
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//出库仓库
        String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName"));//销售单位
        String isBatchso = CommonUtils.checkNull(request.getParamValue("isBatchso"));//是否铺货单
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//销售日期开始
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//销售日期结束
        String RstartDate = CommonUtils.checkNull(request.getParamValue("RstartDate"));//提交财务时间开始
        String RendDate = CommonUtils.checkNull(request.getParamValue("RendDate"));//提交财务时间结束
        String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName"));//订货单位
        String buyerCode = CommonUtils.checkNull(request.getParamValue("buyerCode"));//订货单位CODE
        String finStat = CommonUtils.checkNull(request.getParamValue("finStat"));//财务状态
        String outFlag = CommonUtils.checkNull(request.getParamValue("outFlag"));//出库状态
        String repFlag = CommonUtils.checkNull(request.getParamValue("repFlag"));//提交状态
        String invoiceFlag = CommonUtils.checkNull(request.getParamValue("invoiceFlag"));//开票状态
        String pickOrderFlag = CommonUtils.checkNull(request.getParamValue("pickOrderFlag"));//提货状态
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        String printFlag = CommonUtils.checkNull(request.getParamValue("printFlag"));
        String orderBy = CommonUtils.checkNull(request.getParamValue("order_By"));//排序方式
        String state = "";//状态

        Map<String, String> stateMap = new LinkedHashMap<String, String>();
        String stat1 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "";//已保存
        String stat2 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "";//财务未审核
        String stat3 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "";//财务审核通过
        String stat4 = Constant.CAR_FACTORY_PKG_STATE_01 + "";//装箱中
        String stat5 = Constant.CAR_FACTORY_PKG_STATE_02 + "";//已装箱
        String stat6 = Constant.CAR_FACTORY_TRANS_STATE_01 + ""; //已发运
        String stat7 = Constant.CAR_FACTORY_OUTSTOCK_STATE_05 + "";//已入库
        String stat8 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_06 + "";//已驳回

        stateMap.put(stat1, stat1);  //已保存
        stateMap.put(stat2, stat2);  //已提交
        stateMap.put(stat3, stat3);  //财务审核通过
        stateMap.put(stat4, stat4);  //装箱中
        stateMap.put(stat5, stat5);  //已装箱
        stateMap.put(stat6, stat6);  //已发运
        stateMap.put(stat7, stat7);  //已入库
        stateMap.put(stat8, stat8);  //已驳回
        //财务审核状态
        if (!"".equals(finStat)) {
            if ("0".equals(finStat)) { //未审核
                stateMap.remove(stat1);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
                stateMap.remove(stat8);
            } else if ("1".equals(finStat)) {//已审核
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat8);
            } else if ("2".equals(finStat)) {//已驳回
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
            }
        }
        //出库状态
        if (!"".equals(outFlag)) {
            if ("0".equals(outFlag)) {//未出库
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
                stateMap.remove(stat8);
            } else { //已出库
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat8);
            }
        }
        //提交状态
        if (!"".equals(repFlag)) {
            if ("0".equals(repFlag)) { // 未提交
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
            } else { //已提交
                stateMap.remove(stat1);
                stateMap.remove(stat8);
            }
        }

        if (!stateMap.isEmpty()) {
            Iterator it = stateMap.keySet().iterator();
            while (it.hasNext()) {
                state = state + "," + CommonUtils.checkNull(it.next());
            }
            if (!"".equals(state)) {
                state = state.replaceFirst(",", "");
            }
        }
        if ("".equals(state)) {
            state = "''";
        }
        String dealerId = "";
        String userType = null;
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                dealerId = Constant.OEM_ACTIVITIES;
            } else {
                dealerId = loginUser.getDealerId();
            }
        }

        TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
        userposeDefinePO.setUserId(loginUser.getUserId());
        if (dao.select(userposeDefinePO).size() > 0) {
            userposeDefinePO = (TtPartUserposeDefinePO) dao.select(userposeDefinePO).get(0);
            userType = userposeDefinePO.getUserType() + "";
        }
        ;

        sql.append("SELECT A.SO_ID,\n");
        sql.append("       A.SO_CODE,\n");
        sql.append("       A.ORDER_ID,\n");
        sql.append("       A.ORDER_CODE,\n");
        sql.append("       A.ORDER_TYPE,\n");
        sql.append("       A.IS_BATCHSO,\n");
        sql.append("       A.SO_FROM,\n");
        sql.append("       A.PAY_TYPE,\n");
        sql.append("       A.FREIGHT,\n");
        sql.append("       A.DEALER_IDS,\n");
        sql.append("       A.DEALER_ID,\n");
        sql.append("       td.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       A.SELLER_ID,\n");
        sql.append("       A.SELLER_CODE,\n");
        sql.append("       A.SELLER_NAME,\n");
        sql.append("       A.SALE_DATE,\n");
        sql.append("       A.BUYER_ID,\n");
        sql.append("       A.BUYER_NAME,\n");
        sql.append("       A.CONSIGNEES_ID,\n");
        sql.append("       A.CONSIGNEES,\n");
        sql.append("       A.ADDR_ID,\n");
        sql.append("       A.ADDR,\n");
        sql.append("       A.RECEIVER,\n");
        sql.append("       A.TEL,\n");
        sql.append("       A.POST_CODE,\n");
        sql.append("       A.STATION,\n");
        sql.append("       A.TRANS_TYPE,\n");
        sql.append("       A.TRANSPAY_TYPE,\n");
        sql.append("       TO_CHAR(A.AMOUNT, 'FM999,999,990.00') AS AMOUNT,\n");
        sql.append("       A.DISCOUNT,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       A.IS_BOTTOM,\n");
        sql.append("       A.REMARK2,\n");
        sql.append("       A.CREATE_DATE,\n");
        sql.append("       A.CREATE_BY,\n");
        sql.append("       A.UPDATE_DATE,\n");
        sql.append("       A.UPDATE_BY,\n");
        sql.append("       A.DISABLE_DATE,\n");
        sql.append("       A.DISABLE_BY,\n");
        sql.append("       A.DELETE_DATE,\n");
        sql.append("       A.DELETE_BY,\n");
        sql.append("       A.SUBMIT_DATE,\n");
        sql.append("       A.SUBMIT_BY,\n");
        sql.append("       A.AUDIT_DATE,\n");
        sql.append("       A.AUDIT_BY,\n");
        sql.append("       A.FCAUDIT_DATE,\n");
        sql.append("       A.FCAUDIT_BY,\n");
        sql.append("       A.FC_REMARK,\n");
        sql.append("       A.PKG_BEIGIN,\n");
        sql.append("       A.PACKG_DATE,\n");
        sql.append("       A.VER,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.STATUS,\n");
        sql.append("       A.WH_ID,\n");
        sql.append("       A.TRANS_PRINT_DATE,\n");
        sql.append("       (SELECT NVL(T.WH_NAME, '') AS WH_NAME\n");
        sql.append("          FROM TT_PART_WAREHOUSE_DEFINE T\n");
        sql.append("         WHERE T.WH_ID = A.WH_ID) AS WH_NAME,\n");
        sql.append("       B.NAME AS CREATE_BY_NAME,\n");
        sql.append("       (SELECT D.FIX_NAME\n");
        sql.append("          FROM TT_PART_FIXCODE_DEFINE D\n");
        sql.append("         WHERE D.FIX_GOUPTYPE = 92251004\n");
        sql.append("           AND D.FIX_VALUE = A.TRANS_TYPE) FIX_NAME\n");
        sql.append("  FROM TT_PART_SO_MAIN A, TC_USER B, TM_DEALER TD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND A.CREATE_BY = B.USER_ID\n");
        sql.append("   AND A.DEALER_ID = TD.DEALER_ID\n");
        if (dealerId.equals(Constant.OEM_ACTIVITIES)) { // add by yuan 2013062 车厂才有限制
            if ("".equals(salerId)) {
            /*   sql.append("  AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
		                "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
		                "           AND B.USER_ID ="+loginUser.getUserId() +")");*/
                sql.append("AND EXISTS (SELECT 1\n");
                sql.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                sql.append("      WHERE D.ORDER_TYPE = A.ORDER_TYPE\n");
                sql.append("        AND D.USER_ID = " + loginUser.getUserId() + "\n");
                sql.append("        AND D.STATE = 10011001\n");
                sql.append("        AND D.STATUS = 1)\n");
            } else {
                if ("3".equals(userType)) {//配件销售员区域限制
                    sql.append("  AND EXISTS (SELECT 1\n" +
                            "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                            "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
                            "           AND B.USER_ID =" + salerId + ")");
                    sql.append(" AND EXISTS (SELECT 1\n");//配件销售员订单类型限制
                    sql.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                    sql.append("      WHERE D.ORDER_TYPE = A.ORDER_TYPE\n");
                    sql.append("        AND D.USER_ID = DECODE(" + loginUser.getUserId() + ", 1000002433,D.USER_ID, " + loginUser.getUserId() + ")\n");
                    sql.append("        AND D.STATE = 10011001\n");
                    sql.append("        AND D.STATUS = 1)");
                } else if ("5".equals(userType)) {//精品配件销售员订单类型限制
                    sql.append("AND EXISTS (SELECT 1\n");
                    sql.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                    sql.append("      WHERE D.ORDER_TYPE = A.ORDER_TYPE\n");
                    sql.append("        AND D.USER_ID = DECODE(" + loginUser.getUserId() + ", 1000002433,D.USER_ID, " + loginUser.getUserId() + ")\n");
                    sql.append("        AND D.STATE = 10011001\n");
                    sql.append("        AND D.STATUS = 1)\n");
                }

            }
        }
        if (!"".equals(orderCode)) {
            sql.append(" and  a.ORDER_CODE like '%").append(orderCode).append("%'\n");
        }
        if (!"".equals(orderType)) {
            sql.append(" and  a.ORDER_TYPE='").append(orderType).append("'\n");
        }
        if (!"".equals(salesOrderId)) {
            sql.append(" and  a.SO_CODE like '%").append(salesOrderId).append("%'\n");
        }
        if (!"".equals(whId)) {
            sql.append(" and  a.WH_ID='").append(whId).append("'\n");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and  a.SELLER_NAME like '%").append(sellerName).append("%'\n");
        }
        if (!"".equals(SstartDate) && !"".equals(SendDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(SstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
            sql.append(" and a.CREATE_DATE<= to_date('").append(SendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(RstartDate) && !"".equals(RendDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(RstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(RendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(buyerCode)) {
            sql.append(" and  td.DEALER_CODE like '%").append(buyerCode).append("%'\n");
        }
        if (!"".equals(buyerName)) {
            sql.append(" and  a.DEALER_NAME like '%").append(buyerName).append("%'\n");
        }

        sql.append(" and  a.STATE in (").append(state).append(")\n");

        if (!"".equals(isBatchso)) {
            sql.append(" and  a.IS_BATCHSO='").append(isBatchso).append("'\n");
        }
        if (invoiceFlag.equals("1")) {
            sql.append(" and (INVOICE_NO is not null or INVOICE_NO <>'') \n");
        } else if (invoiceFlag.equals("0")) {
            sql.append(" and (INVOICE_NO is null or INVOICE_NO = '') \n");
        }

        if (pickOrderFlag.equals("1")) {
            sql.append(" and (pick_order_id is not null or pick_order_id <>'') \n");
        } else if (pickOrderFlag.equals("0")) {
            sql.append(" and (pick_order_id is null or pick_order_id = '') \n");
        }

        if (!"".equals(printFlag)) {
            sql.append(" and A.order_type='").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).append("'\n");
            if (printFlag.equals(Constant.IF_TYPE_YES + "")) {
                sql.append(" and A.DIRECT_PRINT_DATE is null \n");
            } else {
                sql.append(" and A.DIRECT_PRINT_DATE is not null  \n");
            }
        }
       
        if (!salerId.equals("")) {
            sql.append(" AND EXISTS (SELECT 1 \n");
            sql.append("         FROM TT_PART_SALESSCOPE_DEFINE D\n");
            sql.append("        WHERE D.DEALER_ID = A.DEALER_ID\n");
            //sql.append("          AND D.USER_ID =A.create_by \n");
            sql.append("          AND D.STATUS = 1\n");
            sql.append("          AND D.STATE = 10011001\n");
            sql.append("          AND D.USER_ID =").append(salerId).append(")");
        }
        sql.append(" and seller_id='").append(CommonUtils.checkNull(dealerId)).append("'\n");

        if (!"".equals(orderBy) && null != orderBy) {
            if ("1".equals(orderBy)) {
                sql.append(" order by a.ORDER_CODE desc\n");
            } else if ("2".equals(orderBy)) {
                sql.append(" order by a.SUBMIT_DATE desc  \n");
            } else if ("3".equals(orderBy)) {
                sql.append(" order by a.DEALER_CODE \n");
            } else if ("4".equals(orderBy)) {
                sql.append(" order by a.ORDER_TYPE  \n");
            }
        } else {
            sql.append(" order by A.CREATE_DATE DESC\n");
        }

       /* if("0".equals(repFlag)){
            sql.append(" order by a.dealer_Name,a.create_date DESC");
        }else{
            sql.append(" order by a.create_date DESC");
        }*/

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }


    public Map<String, Object> getSumData(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//订单号
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String salesOrderId = CommonUtils.checkNull(request.getParamValue("salesOrderId"));//销售单号
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//出库仓库
        String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName"));//销售单位
        String isBatchso = CommonUtils.checkNull(request.getParamValue("isBatchso"));//是否铺货单
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//销售日期开始
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//销售日期结束
        String RstartDate = CommonUtils.checkNull(request.getParamValue("RstartDate"));//提交财务时间开始
        String RendDate = CommonUtils.checkNull(request.getParamValue("RendDate"));//提交财务时间结束
        String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName"));//订货单位
        String buyerCode = CommonUtils.checkNull(request.getParamValue("buyerCode"));//订货单位CODE
        String finStat = CommonUtils.checkNull(request.getParamValue("finStat"));//财务状态
        String outFlag = CommonUtils.checkNull(request.getParamValue("outFlag"));//出库状态
        String repFlag = CommonUtils.checkNull(request.getParamValue("repFlag"));//提交状态
        String invoiceFlag = CommonUtils.checkNull(request.getParamValue("invoiceFlag"));//开票状态
        String pickOrderFlag = CommonUtils.checkNull(request.getParamValue("pickOrderFlag"));//提货状态
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        String printFlag = CommonUtils.checkNull(request.getParamValue("printFlag"));
        String state = "";//状态
        String userType = null;

        Map<String, String> stateMap = new LinkedHashMap<String, String>();
        String stat1 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "";//已保存
        String stat2 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "";//财务未审核
        String stat3 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "";//财务审核通过
        String stat4 = Constant.CAR_FACTORY_PKG_STATE_01 + "";//装箱中
        String stat5 = Constant.CAR_FACTORY_PKG_STATE_02 + "";//已装箱
        String stat6 = Constant.CAR_FACTORY_TRANS_STATE_01 + ""; //已发运
        String stat7 = Constant.CAR_FACTORY_OUTSTOCK_STATE_05 + "";//已入库
        String stat8 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_06 + "";//已驳回

        stateMap.put(stat1, stat1);  //已保存
        stateMap.put(stat2, stat2);  //已提交
        stateMap.put(stat3, stat3);  //财务审核通过
        stateMap.put(stat4, stat4);  //装箱中
        stateMap.put(stat5, stat5);  //已装箱
        stateMap.put(stat6, stat6);  //已发运
        stateMap.put(stat7, stat7);  //已入库
        stateMap.put(stat8, stat8);  //已驳回
        //财务审核状态
        if (!"".equals(finStat)) {
            if ("0".equals(finStat)) { //未审核
                stateMap.remove(stat1);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
                stateMap.remove(stat8);
            } else if ("1".equals(finStat)) {//已审核
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat8);
            } else if ("2".equals(finStat)) {//已驳回
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
            }
        }
        //出库状态
        if (!"".equals(outFlag)) {
            if ("0".equals(outFlag)) {//未出库
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
                stateMap.remove(stat8);
            } else { //已出库
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat8);
            }
        }
        //提交状态
        if (!"".equals(repFlag)) {
            if ("0".equals(repFlag)) { // 未提交
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
            } else { //已提交
                stateMap.remove(stat1);
                stateMap.remove(stat8);
            }
        }

        if (!stateMap.isEmpty()) {
            Iterator it = stateMap.keySet().iterator();
            while (it.hasNext()) {
                state = state + "," + CommonUtils.checkNull(it.next());
            }
            if (!"".equals(state)) {
                state = state.replaceFirst(",", "");
            }
        }
        if ("".equals(state)) {
            state = "''";
        }
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                dealerId = Constant.OEM_ACTIVITIES;
            } else {
                dealerId = loginUser.getDealerId();
            }
        }
        TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
        userposeDefinePO.setUserId(loginUser.getUserId());
        if (dao.select(userposeDefinePO).size() > 0) {
            userposeDefinePO = (TtPartUserposeDefinePO) dao.select(userposeDefinePO).get(0);
            userType = userposeDefinePO.getUserType() + "";
        }
        ;
        sql.append("SELECT nvl(SUM(A.amount),0) amount,count(1) xs\n");
        sql.append("  FROM TT_PART_SO_MAIN A\n");
        sql.append("  LEFT JOIN TC_USER B\n");
        sql.append("    ON A.CREATE_BY = B.USER_ID\n");
        sql.append(" WHERE 1 = 1\n");

        if (!"".equals(orderCode)) {
            sql.append(" and  a.ORDER_CODE like '%").append(orderCode).append("%'");
        }
        if (!"".equals(orderType)) {
            sql.append(" and  a.ORDER_TYPE='").append(orderType).append("'");
        }
        if (!"".equals(salesOrderId)) {
            sql.append(" and  a.SO_CODE like '%").append(salesOrderId).append("%'");
        }
        if (!"".equals(whId)) {
            sql.append(" and  a.WH_ID='").append(whId).append("'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and  a.SELLER_NAME like '%").append(sellerName).append("%'");
        }
        if (!"".equals(SstartDate) && !"".equals(SendDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(SstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(SendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(RstartDate) && !"".equals(RendDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(RstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(RendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(buyerCode)) {
            sql.append(" and  a.DEALER_CODE like '%").append(buyerCode).append("%'");
        }
        if (!"".equals(buyerName)) {
            sql.append(" and  a.DEALER_NAME like '%").append(buyerName).append("%'");
        }

        sql.append(" and  a.STATE in (").append(state).append(")");

        if (!"".equals(isBatchso)) {
            sql.append(" and  a.IS_BATCHSO='").append(isBatchso).append("'");
        }
        if (invoiceFlag.equals("1")) {
            sql.append(" and (INVOICE_NO is not null or INVOICE_NO <>'') ");
        } else if (invoiceFlag.equals("0")) {
            sql.append(" and (INVOICE_NO is null or INVOICE_NO = '') ");
        }

        if (pickOrderFlag.equals("1")) {
            sql.append(" and (pick_order_id is not null or pick_order_id <>'') ");
        } else if (pickOrderFlag.equals("0")) {
            sql.append(" and (pick_order_id is null or pick_order_id = '') ");
        }

        if (!"".equals(printFlag)) {
            sql.append(" and A.order_type='").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).append("'");
            if (printFlag.equals(Constant.IF_TYPE_YES + "")) {
                sql.append(" and A.DIRECT_PRINT_DATE is null ");
            } else {
                sql.append(" and A.DIRECT_PRINT_DATE is not null  ");
            }
        }
        if (dealerId.equals(Constant.OEM_ACTIVITIES)) { // add by yuan 2013062 车厂才有限制
            if ("".equals(salerId)) {
            /*   sql.append("  AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
		                "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
		                "           AND B.USER_ID ="+loginUser.getUserId() +")");*/
            } else {
                if ("3".equals(userType)) {//配件销售员区域限制
                    sql.append("  AND EXISTS (SELECT 1\n" +
                            "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                            "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
                            "           AND B.USER_ID =" + salerId + ")");
                    sql.append(" AND EXISTS (SELECT 1\n");//配件销售员订单类型限制
                    sql.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                    sql.append("      WHERE D.ORDER_TYPE = A.ORDER_TYPE\n");
                    sql.append("        AND D.USER_ID = DECODE(" + loginUser.getUserId() + ", 1000002433,D.USER_ID, " + loginUser.getUserId() + ")\n");
                    sql.append("        AND D.STATE = 10011001\n");
                    sql.append("        AND D.STATUS = 1)");
                } else if ("5".equals(userType)) {//配件销售员订单类型限制
                    sql.append("AND EXISTS (SELECT 1\n");
                    sql.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                    sql.append("      WHERE D.ORDER_TYPE = A.ORDER_TYPE\n");
                    sql.append("        AND D.USER_ID = DECODE(" + loginUser.getUserId() + ", 1000002433,D.USER_ID, " + loginUser.getUserId() + ")\n");
                    sql.append("        AND D.STATE = 10011001\n");
                    sql.append("        AND D.STATUS = 1)");
                }

            }
        }
        sql.append(" and seller_id='").append(CommonUtils.checkNull(dealerId)).append("'");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }


    public String getDealertId(String userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select Company_id from tc_user where user_id='").append(userId).append("'");
        List<Map<String, Object>> list2 = this.pageQuery(sql.toString(), null, this.getFunName());
        String companyId = "";
        if (null == list2 || list2.size() <= 0) {
            companyId = "";
        }
        companyId = CommonUtils.checkNull(list2.get(0).get("COMPANY_ID"));
        sql.setLength(0);
        sql.append("SELECT p.company_id,p.company_code,p.company_name \n");
        sql.append("  FROM tm_company p\n");
        sql.append(" WHERE p.company_id = '").append(companyId).append("'");
        sql.append(" AND p.STATUS = " + Constant.STATUS_ENABLE);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            sql.setLength(0);
            sql.append("SELECT DEALER_ID,DEALER_NAME ,DEALER_CODE\n");
            sql.append("        FROM TM_DEALER D\n");
            sql.append("       WHERE D.DEALER_ID ='").append(companyId).append("'");
            sql.append("         AND D.STATUS =  " + Constant.STATUS_ENABLE);
            List<Map<String, Object>> list1 = this.pageQuery(sql.toString(), null, this.getFunName());
            if (null == list1 || list1.size() <= 0) {
                return "";
            }
            return CommonUtils.checkNull(list1.get(0).get("DEALER_ID"));
        }
        return CommonUtils.checkNull(list.get(0).get("COMPANY_ID"));
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 获取主订单信息
     */
    public Map getSoOrderMain(String soId) {
        StringBuffer sql = new StringBuffer();

        sql.append("WITH ORDER_XC_BO AS\n");
        sql.append(" (SELECT BM.PICK_ORDER_ID, SUM(BD.BO_QTY * BD.BUY_PRICE) BO_AMOUNT\n");
        sql.append("    FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM\n");
        sql.append("   WHERE BD.BO_ID = BM.BO_ID\n");
        sql.append("     AND BM.PICK_ORDER_ID IS NOT NULL\n");
        sql.append("   GROUP BY BM.PICK_ORDER_ID)\n");
        sql.append("SELECT A.*,\n");
        sql.append("       (SELECT NAME FROM TC_USER B WHERE B.USER_ID = A.CREATE_BY) AS CREATE_BY_NAME,\n");
        sql.append("       TO_CHAR(A.AMOUNT, 'FM999,999,990.00') AS CONVERS_AMOUNT,\n");
        sql.append("       TO_CHAR(A.CREATE_DATE, 'yyyy-MM-dd hh24:mm:ss') AS CREATE_DATE_FM,\n");
        sql.append("       FREIGHT,\n");
        sql.append("       (select tv_name from TT_TRANSPORT_VALUATION tv where tv.tv_id=a.trans_type ) trans_type1,\n");//20170828 add
        sql.append("       (SELECT WH_NAME\n");
        sql.append("          FROM TT_PART_WAREHOUSE_DEFINE C\n");
        sql.append("         WHERE C.WH_ID = A.WH_ID) AS WH_NAME,\n");
        sql.append("       TO_CHAR((A.AMOUNT - NVL(XB.BO_AMOUNT, 0)), 'FM999,999,990.00') YX_AMOUNT\n");
        sql.append("  FROM TT_PART_SO_MAIN A, ORDER_XC_BO XB\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND A.PICK_ORDER_ID = XB.PICK_ORDER_ID(+)\n");
        sql.append("   AND SO_ID = '" + soId + "'");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 获取订单详细信息
     */
    public List<Map<String, Object>> getSoOrderMDetail(String soId) {
        StringBuffer sql = new StringBuffer();


        sql.append("WITH ORDER_XC_BO AS\n");
        sql.append(" (SELECT BM.PICK_ORDER_ID, BD.PART_ID, SUM(BD.BO_QTY) BO_QTY\n");
        sql.append("    FROM TT_PART_BO_DTL BD, TT_PART_BO_MAIN BM\n");
        sql.append("   WHERE BD.BO_ID = BM.BO_ID\n");
        sql.append("     AND BM.PICK_ORDER_ID IS NOT NULL\n");
        sql.append("   GROUP BY BM.PICK_ORDER_ID, BD.PART_ID)\n");
        sql.append("SELECT A.*,\n");
        sql.append("       NVL((SELECT SUM(ITEM_QTY)  FROM VW_PART_STOCK T\n");
        sql.append("            WHERE T.WH_ID = SM.WH_ID AND T.PART_ID = A.PART_ID),\n");
        sql.append("           '0') AS NORMAL_QTY_NOW,\n");
        sql.append("       TO_CHAR(A.BUY_AMOUNT, 'FM999,999,990.00') AS CONVERS_AMOUNT,\n");
        sql.append("       TO_CHAR(A.BUY_PRICE, 'FM999,999,990.00') AS CONVERS_PRICE,\n");
        sql.append("       (NVL(A.BUY_QTY, '1') / NVL(TD.OEM_MIN_PKG, '1')) AS PKG_NUM,\n");
        sql.append("       NVL((SELECT XB.BO_QTY FROM ORDER_XC_BO XB\n");
        sql.append("            WHERE SM.PICK_ORDER_ID = XB.PICK_ORDER_ID\n");
        sql.append("              AND A.PART_ID = XB.PART_ID),\n");
        sql.append("           0) BO_QTY,\n");
        sql.append("       (A.SALES_QTY - NVL((SELECT XB.BO_QTY FROM ORDER_XC_BO XB\n");
        sql.append("                           WHERE SM.PICK_ORDER_ID = XB.PICK_ORDER_ID\n");
        sql.append("                             AND A.PART_ID = XB.PART_ID),\n");
        sql.append("                          0)) YX_QTY,\n");
        sql.append("       (A.SALES_QTY - NVL((SELECT XB.BO_QTY FROM ORDER_XC_BO XB\n");
        sql.append("                           WHERE SM.PICK_ORDER_ID = XB.PICK_ORDER_ID\n");
        sql.append("                             AND A.PART_ID = XB.PART_ID),\n");
        sql.append("                          0)) * A.BUY_PRICE YX_AMOUNT\n");        
//        sql.append("       ,TB.BATCH_NO ");//批次号
//        sql.append("       ,(SELECT LOC_CODE FROM TT_PART_LOACTION_DEFINE LD WHERE LD.LOC_ID=TB.LOC_ID ) LOC_CODE ");//货位
//        sql.append("       ,TB.BOOKED_QTY ");//该批次该货位占用数量
        sql.append("  FROM TT_PART_SO_DTL A, TT_PART_SO_MAIN SM, TT_PART_DEFINE TD \n");
//        sql.append("  ,TT_PART_BOOK_DTL TB\n");
        sql.append(" WHERE 1 = 1 ");
        sql.append("   AND A.SO_ID = SM.SO_ID ");
        sql.append("   AND A.PART_ID = TD.PART_ID ");
//        sql.append("   AND A.PART_ID = TB.PART_ID ");
//        sql.append("   AND A.SO_ID = TB.ORDER_ID ");
        sql.append("   AND TD.STATE=10011001 ");
        sql.append("   AND TD.STATUS=1 ");
        sql.append("   AND A.SO_ID ="+soId);
        sql.append("   ORDER BY A.CREATE_DATE DESC");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param orgId
     * @param : @param OrderDate
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-9-16
     * @Title : 获取直发打印订单序号信息
     */
    public List<Map<String, Object>> getSequenceInfo(String orgId, String OrderDate) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT TO_CHAR(A.CREATE_DATE, 'YYYY-MM') AS YEAR_MOTH, TO_CHAR((SUM(A.TRANS_PRINT_NUM) + 1),'FM0000') AS SEQ_NUM ");
        sql.append(" FROM TT_PART_SO_MAIN A ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND A.ORDER_TYPE = '" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "' ");
        sql.append(" AND A.STATE IN ('" + Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "', '" + Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "', ");
        sql.append(" '" + Constant.CAR_FACTORY_PKG_STATE_01 + "', '" + Constant.CAR_FACTORY_PKG_STATE_02 + "', '" + Constant.CAR_FACTORY_TRANS_STATE_01 + "', ");
        sql.append(" '" + Constant.CAR_FACTORY_OUTSTOCK_STATE_05 + "', '" + Constant.CAR_FACTORY_ORDER_CHECK_STATE_06 + "' ) ");
        sql.append(" AND A.TRANS_PRINT_NUM > 0 ");
        sql.append(" AND SELLER_ID = '" + orgId + "' ");
        sql.append(" AND TO_CHAR(A.CREATE_DATE, 'YYYY-MM') = '" + OrderDate + "' ");
        sql.append(" GROUP BY TO_CHAR(A.CREATE_DATE, 'YYYY-MM') ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 获取订单详细信息
     */
    public Map<String, Object> getSoOrderMDetail(String soId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_SO_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and SO_ID='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return new HashMap();
        }
        return list.get(0);
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 查询订单操作记录
     */
    public List<Map<String, Object>> queryOrderHistory(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_OPERATION_HISTORY ");
        sql.append(" where 1=1 ");
        //sql.append(" and OPT_TYPE='").append(Constant.PART_OPERATION_TYPE_02).append("'");
        sql.append(" and BUSSINESS_ID='").append(orderId).append("'");
        sql.append(" order by OPT_DATE DESC ");
        return this.pageQuery(sql.toString(), null, this.getFunName());
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
    public List<Map<String, Object>> queryPartSoOrderForExcel(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//订单号
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String salesOrderId = CommonUtils.checkNull(request.getParamValue("salesOrderId"));//销售单号
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//出库仓库
        String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName"));//销售单位
        String isBatchso = CommonUtils.checkNull(request.getParamValue("isBatchso"));//是否铺货单
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//销售日期开始
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//销售日期结束
        String RstartDate = CommonUtils.checkNull(request.getParamValue("RstartDate"));//提交财务时间开始
        String RendDate = CommonUtils.checkNull(request.getParamValue("RendDate"));//提交财务时间结束
        String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName"));//订货单位
        String buyerCode = CommonUtils.checkNull(request.getParamValue("buyerCode"));//订货单位CODE
        String finStat = CommonUtils.checkNull(request.getParamValue("finStat"));//财务状态
        String outFlag = CommonUtils.checkNull(request.getParamValue("outFlag"));//出库状态
        String repFlag = CommonUtils.checkNull(request.getParamValue("repFlag"));//提交状态
        String invoiceFlag = CommonUtils.checkNull(request.getParamValue("invoiceFlag"));//开票状态
        String pickOrderFlag = CommonUtils.checkNull(request.getParamValue("pickOrderFlag"));//提货状态
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        String printFlag = CommonUtils.checkNull(request.getParamValue("printFlag"));
        String state = "";//状态

        Map<String, String> stateMap = new LinkedHashMap<String, String>();
        String stat1 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "";//已保存
        String stat2 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "";//财务未审核
        String stat3 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "";//财务审核通过
        String stat4 = Constant.CAR_FACTORY_PKG_STATE_01 + "";//装箱中
        String stat5 = Constant.CAR_FACTORY_PKG_STATE_02 + "";//已装箱
        String stat6 = Constant.CAR_FACTORY_TRANS_STATE_01 + ""; //已发运
        String stat7 = Constant.CAR_FACTORY_OUTSTOCK_STATE_05 + "";//已入库
        String stat8 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_06 + "";//已驳回

        stateMap.put(stat1, stat1);  //已保存
        stateMap.put(stat2, stat2);  //已提交
        stateMap.put(stat3, stat3);  //财务审核通过
        stateMap.put(stat4, stat4);  //装箱中
        stateMap.put(stat5, stat5);  //已装箱
        stateMap.put(stat6, stat6);  //已发运
        stateMap.put(stat7, stat7);  //已入库
        stateMap.put(stat8, stat8);  //已驳回
        //财务审核状态
        if (!"".equals(finStat)) {
            if ("0".equals(finStat)) { //未审核
                stateMap.remove(stat1);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
                stateMap.remove(stat8);
            } else if ("1".equals(finStat)) {//已审核
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat8);
            } else if ("2".equals(finStat)) {//已驳回
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
            }
        }
        //出库状态
        if (!"".equals(outFlag)) {
            if ("0".equals(outFlag)) {//未出库
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
                stateMap.remove(stat8);
            } else { //已出库
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat8);
            }
        }
        //提交状态
        if (!"".equals(repFlag)) {
            if ("0".equals(repFlag)) { // 未提交
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
            } else { //已提交
                stateMap.remove(stat1);
                stateMap.remove(stat8);
            }
        }

        if (!stateMap.isEmpty()) {
            Iterator it = stateMap.keySet().iterator();
            while (it.hasNext()) {
                state = state + "," + CommonUtils.checkNull(it.next());
            }
            if (!"".equals(state)) {
                state = state.replaceFirst(",", "");
            }
        }
        if ("".equals(state)) {
            state = "''";
        }
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                dealerId = Constant.OEM_ACTIVITIES;
            } else {
                dealerId = loginUser.getDealerId();
            }
        }
        sql.append("SELECT A.SO_ID,\n");
        sql.append("       A.SO_CODE,\n");
        sql.append("       A.ORDER_ID,\n");
        sql.append("       A.ORDER_CODE,\n");
        sql.append("       (SELECT tc.code_desc FROM tc_code tc WHERE tc.code_id=A.ORDER_TYPE) ORDER_TYPE,\n");
        sql.append("       A.IS_BATCHSO,\n");
        sql.append("       A.SO_FROM,\n");
        sql.append("       A.PAY_TYPE,\n");
        sql.append("       A.FREIGHT,\n");
        sql.append("       A.DEALER_IDS,\n");
        sql.append("       A.DEALER_ID,\n");
        sql.append("       A.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       A.SELLER_ID,\n");
        sql.append("       A.SELLER_CODE,\n");
        sql.append("       A.SELLER_NAME,\n");
        sql.append("       A.CREATE_DATE,\n");
        sql.append("       A.BUYER_ID,\n");
        sql.append("       A.BUYER_NAME,\n");
        sql.append("       A.CONSIGNEES_ID,\n");
        sql.append("       A.CONSIGNEES,\n");
        sql.append("       A.ADDR_ID,\n");
        sql.append("       A.ADDR,\n");
        sql.append("       A.RECEIVER,\n");
        sql.append("       A.TEL,\n");
        sql.append("       A.POST_CODE,\n");
        sql.append("       A.STATION,\n");
        sql.append("       A.TRANS_TYPE,\n");
        sql.append("       A.TRANSPAY_TYPE,\n");
        sql.append("       TO_CHAR(A.AMOUNT, 'FM999,999,990.00') AS AMOUNT,\n");
        sql.append("       A.DISCOUNT,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       A.IS_BOTTOM,\n");
        sql.append("       A.REMARK2,\n");
        sql.append("       A.CREATE_DATE,\n");
        sql.append("       A.CREATE_BY,\n");
        sql.append("       A.UPDATE_DATE,\n");
        sql.append("       A.UPDATE_BY,\n");
        sql.append("       A.DISABLE_DATE,\n");
        sql.append("       A.DISABLE_BY,\n");
        sql.append("       A.DELETE_DATE,\n");
        sql.append("       A.DELETE_BY,\n");
        sql.append("       A.SUBMIT_DATE,\n");
        sql.append("       A.SUBMIT_BY,\n");
        sql.append("       A.AUDIT_DATE,\n");
        sql.append("       A.AUDIT_BY,\n");
        sql.append("       A.FCAUDIT_DATE,\n");
        sql.append("       A.FCAUDIT_BY,\n");
        sql.append("       A.FC_REMARK,\n");
        sql.append("       A.PKG_BEIGIN,\n");
        sql.append("       A.PACKG_DATE,\n");
        sql.append("       A.VER,\n");
        sql.append("       (SELECT tc.code_desc FROM tc_code tc WHERE tc.code_id=A.STATE) STATE,\n");
        sql.append("       A.STATUS,\n");
        sql.append("       A.WH_ID,\n");
        sql.append("       A.TRANS_PRINT_DATE,\n");
        sql.append("       (SELECT NVL(T.WH_NAME, '') AS WH_NAME\n");
        sql.append("          FROM TT_PART_WAREHOUSE_DEFINE T\n");
        sql.append("         WHERE T.WH_ID = A.WH_ID) AS WH_NAME,\n");
        sql.append("       B.NAME AS CREATE_BY_NAME,\n");
        sql.append("       (SELECT D.FIX_NAME\n");
        sql.append("          FROM TT_PART_FIXCODE_DEFINE D\n");
        sql.append("         WHERE D.FIX_GOUPTYPE = 92251004\n");
        sql.append("           AND D.FIX_VALUE = A.TRANS_TYPE) FIX_NAME\n");
        sql.append("  FROM TT_PART_SO_MAIN A\n");
        sql.append("  LEFT JOIN TC_USER B\n");
        sql.append("    ON A.CREATE_BY = B.USER_ID\n");
        sql.append(" WHERE 1 = 1\n");

        if (!"".equals(orderCode)) {
            sql.append(" and  a.ORDER_CODE like '%").append(orderCode).append("%'");
        }
        if (!"".equals(orderType)) {
            sql.append(" and  a.ORDER_TYPE='").append(orderType).append("'");
        }
        if (!"".equals(salesOrderId)) {
            sql.append(" and  a.SO_CODE like '%").append(salesOrderId).append("%'");
        }
        if (!"".equals(whId)) {
            sql.append(" and  a.WH_ID='").append(whId).append("'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and  a.SELLER_NAME like '%").append(sellerName).append("%'");
        }
        if (!"".equals(SstartDate) && !"".equals(SendDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(SstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(SendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(RstartDate) && !"".equals(RendDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(RstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(RendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(buyerCode)) {
            sql.append(" and  a.DEALER_CODE like '%").append(buyerCode).append("%'");
        }
        if (!"".equals(buyerName)) {
            sql.append(" and  a.DEALER_NAME like '%").append(buyerName).append("%'");
        }

        sql.append(" and  a.STATE in (").append(state).append(")");

        if (!"".equals(isBatchso)) {
            sql.append(" and  a.IS_BATCHSO='").append(isBatchso).append("'");
        }
        if (invoiceFlag.equals("1")) {
            sql.append(" and (INVOICE_NO is not null or INVOICE_NO <>'') ");
        } else if (invoiceFlag.equals("0")) {
            sql.append(" and (INVOICE_NO is null or INVOICE_NO = '') ");
        }

        if (pickOrderFlag.equals("1")) {
            sql.append(" and (pick_order_id is not null or pick_order_id <>'') ");
        } else if (pickOrderFlag.equals("0")) {
            sql.append(" and (pick_order_id is null or pick_order_id = '') ");
        }

        if (!"".equals(printFlag)) {
            sql.append(" and A.order_type='").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).append("'");
            if (printFlag.equals(Constant.IF_TYPE_YES + "")) {
                sql.append(" and A.DIRECT_PRINT_DATE is null ");
            } else {
                sql.append(" and A.DIRECT_PRINT_DATE is not null  ");
            }
        }
       /* if(getUserType(loginUser.getUserId()).equals("6")){
            sql.append(" AND A.create_by=").append(loginUser.getUserId());
        }*/
        if (!salerId.equals("")) {
            sql.append(" AND EXISTS (SELECT 1 \n");
            sql.append("         FROM TT_PART_SALESSCOPE_DEFINE D\n");
            sql.append("        WHERE D.DEALER_ID = A.DEALER_ID\n");
            //sql.append("          AND D.USER_ID =A.create_by \n");
            sql.append("          AND D.STATUS = 1\n");
            sql.append("          AND D.STATE = 10011001\n");
            sql.append("          AND D.USER_ID =").append(salerId).append(")");
        }
        sql.append(" and seller_id='").append(CommonUtils.checkNull(dealerId)).append("'");
        sql.append(" order by  a.create_date DESC,a.dealer_Name");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
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
    public List<Map<String, Object>> queryPartSoDtlForExcel(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//订单号
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String salesOrderId = CommonUtils.checkNull(request.getParamValue("salesOrderId"));//销售单号
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//出库仓库
        String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName"));//销售单位
        String isBatchso = CommonUtils.checkNull(request.getParamValue("isBatchso"));//是否铺货单
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//销售日期开始
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//销售日期结束
        String RstartDate = CommonUtils.checkNull(request.getParamValue("RstartDate"));//提交财务时间开始
        String RendDate = CommonUtils.checkNull(request.getParamValue("RendDate"));//提交财务时间结束
        String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName"));//订货单位
        String buyerCode = CommonUtils.checkNull(request.getParamValue("buyerCode"));//订货单位CODE
        String finStat = CommonUtils.checkNull(request.getParamValue("finStat"));//财务状态
        String outFlag = CommonUtils.checkNull(request.getParamValue("outFlag"));//出库状态
        String repFlag = CommonUtils.checkNull(request.getParamValue("repFlag"));//提交状态
        String invoiceFlag = CommonUtils.checkNull(request.getParamValue("invoiceFlag"));//开票状态
        String pickOrderFlag = CommonUtils.checkNull(request.getParamValue("pickOrderFlag"));//提货状态
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        String printFlag = CommonUtils.checkNull(request.getParamValue("printFlag"));
        String state = "";//状态

        Map<String, String> stateMap = new LinkedHashMap<String, String>();
        String stat1 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "";//已保存
        String stat2 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "";//财务未审核
        String stat3 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "";//财务审核通过
        String stat4 = Constant.CAR_FACTORY_PKG_STATE_01 + "";//装箱中
        String stat5 = Constant.CAR_FACTORY_PKG_STATE_02 + "";//已装箱
        String stat6 = Constant.CAR_FACTORY_TRANS_STATE_01 + ""; //已发运
        String stat7 = Constant.CAR_FACTORY_OUTSTOCK_STATE_05 + "";//已入库
        String stat8 = Constant.CAR_FACTORY_ORDER_CHECK_STATE_06 + "";//已驳回

        stateMap.put(stat1, stat1);  //已保存
        stateMap.put(stat2, stat2);  //财务未审核
        stateMap.put(stat3, stat3);  //财务审核通过
        stateMap.put(stat4, stat4);  //装箱中
        stateMap.put(stat5, stat5);  //已装箱
        stateMap.put(stat6, stat6);  //已发运
        stateMap.put(stat7, stat7);  //已驳回
        stateMap.put(stat8, stat8);

        //财务审核状态
        if (!"".equals(finStat)) {
            if ("0".equals(finStat)) { //未审核
                stateMap.remove(stat1);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
                stateMap.remove(stat8);
            } else if ("1".equals(finStat)) {//已审核
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat8);
            } else if ("2".equals(finStat)) {//已驳回
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
            }
        }
        //出库状态
        if (!"".equals(outFlag)) {
            if ("0".equals(outFlag)) {//未出库
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
                stateMap.remove(stat8);
            } else { //已出库
                stateMap.remove(stat1);
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat8);
            }
        }
        //提交状态
        if (!"".equals(repFlag)) {
            if ("0".equals(repFlag)) { // 未提交
                stateMap.remove(stat2);
                stateMap.remove(stat3);
                stateMap.remove(stat4);
                stateMap.remove(stat5);
                stateMap.remove(stat6);
                stateMap.remove(stat7);
            } else { //已提交
                stateMap.remove(stat1);
                stateMap.remove(stat8);
            }
        }

        if (!stateMap.isEmpty()) {
            Iterator it = stateMap.keySet().iterator();
            while (it.hasNext()) {
                state = state + "," + CommonUtils.checkNull(it.next());
            }
            if (!"".equals(state)) {
                state = state.replaceFirst(",", "");
            }
        }
        if ("".equals(state)) {
            state = "''";
        }
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                dealerId = Constant.OEM_ACTIVITIES;
            } else {
                dealerId = loginUser.getDealerId();
            }
        }


        sql.append("SELECT A.SO_CODE,\n");
        sql.append("       A.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       D.PART_OLDCODE,\n");
        sql.append("       D.PART_CNAME,\n");
        sql.append("       D.PART_CODE,\n");
        sql.append("       D.UNIT,\n");
        sql.append("       D.MIN_PACKAGE,\n");
        sql.append("       D.SALES_QTY,\n");
        sql.append("       D.BUY_PRICE,\n");
        sql.append("       D.BUY_AMOUNT\n");
        sql.append("  FROM TT_PART_SO_MAIN A\n");
        sql.append("  JOIN TT_PART_SO_DTL D\n");
        sql.append("    ON A.SO_ID = D.SO_ID\n");
        sql.append("  LEFT JOIN TC_USER B\n");
        sql.append("    ON A.CREATE_BY = B.USER_ID\n");
        sql.append(" WHERE 1 = 1\n");

        if (!"".equals(orderCode)) {
            sql.append(" and  a.ORDER_CODE like '%").append(orderCode).append("%'");
        }
        if (!"".equals(orderType)) {
            sql.append(" and  a.ORDER_TYPE='").append(orderType).append("'");
        }
        if (!"".equals(salesOrderId)) {
            sql.append(" and  a.SO_CODE like '%").append(salesOrderId).append("%'");
        }
        if (!"".equals(whId)) {
            sql.append(" and  a.WH_ID='").append(whId).append("'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and  a.SELLER_NAME like '%").append(sellerName).append("%'");
        }
        if (!"".equals(SstartDate) && !"".equals(SendDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(SstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(SendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(RstartDate) && !"".equals(RendDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(RstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(RendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(buyerCode)) {
            sql.append(" and  a.DEALER_CODE like '%").append(buyerCode).append("%'");
        }
        if (!"".equals(buyerName)) {
            sql.append(" and  a.DEALER_NAME like '%").append(buyerName).append("%'");
        }

        sql.append(" and  a.STATE in (").append(state).append(")");

        if (!"".equals(isBatchso)) {
            sql.append(" and  a.IS_BATCHSO='").append(isBatchso).append("'");
        }
        if (invoiceFlag.equals("1")) {
            sql.append(" and (INVOICE_NO is not null or INVOICE_NO <>'') ");
        } else if (invoiceFlag.equals("0")) {
            sql.append(" and (INVOICE_NO is null or INVOICE_NO = '') ");
        }

        if (pickOrderFlag.equals("1")) {
            sql.append(" and (pick_order_id is not null or pick_order_id <>'') ");
        } else if (pickOrderFlag.equals("0")) {
            sql.append(" and (pick_order_id is null or pick_order_id = '') ");
        }

        if (!"".equals(printFlag)) {
            sql.append(" and A.order_type='").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).append("'");
            if (printFlag.equals(Constant.IF_TYPE_YES + "")) {
                sql.append(" and A.DIRECT_PRINT_DATE is null ");
            } else {
                sql.append(" and A.DIRECT_PRINT_DATE is not null  ");
            }
        }
		/*if(!salerId.equals("")){
			dealerId = this.getDealertId(salerId);
		}*/
        if (getUserType(loginUser.getUserId()).equals("6")) {
            sql.append(" AND A.create_by=").append(loginUser.getUserId());
        }
        if (!salerId.equals("")) {
            sql.append(" AND EXISTS (SELECT 1 \n");
            sql.append("         FROM TT_PART_SALESSCOPE_DEFINE D\n");
            sql.append("        WHERE D.DEALER_ID = A.DEALER_ID\n");
            //sql.append("          AND D.USER_ID =A.create_by \n");
            sql.append("          AND D.STATUS = 1\n");
            sql.append("          AND D.STATE = 10011001\n");
            sql.append("          AND D.USER_ID =").append(salerId).append(")");
        }
        sql.append(" and seller_id='").append(CommonUtils.checkNull(dealerId)).append("'");
        sql.append(" order by a.create_date DESC");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public String getPartLoc(String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select LOC_NAME,LOC_ID from TT_PART_LOACTION_DEFINE where STATE='");
        sql.append(Constant.STATUS_ENABLE).append("'");
        sql.append(" and PART_ID ='");
        sql.append(partId);
        sql.append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("LOC_NAME"));
    }

    public Map<String, Object> getAccount(String dealerId, String sellerId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT R.PARENTORG_ID,\n");
        sql.append("       R.PARENTORG_NAME,\n");
        sql.append("       R.PARENTORG_CODE,\n");
        sql.append("       NVL(B.ACCOUNT_ID, '') AS ACCOUNT_ID,\n");
        sql.append("       TO_CHAR(NVL(ACCOUNT_SUM, '0.00'), 'FM999,999,999,999,990.00') AS ACCOUNT_SUM,\n");
        sql.append("       TO_CHAR(ABS(NVL(ACCOUNT_DJ, '0.00')), 'FM999,999,999,999,990.00') AS ACCOUNT_DJ,\n");
        sql.append("       TO_CHAR(NVL(ACCOUNT_KY, '0.00'), 'FM999,999,999,999,990.00') AS ACCOUNT_KY\n");
        sql.append("  FROM TT_PART_SALES_RELATION R，VW_PART_DLR_ACCOUNT B\n");
        sql.append(" WHERE R.PARENTORG_ID = B.PARENTORG_ID\n");
        sql.append("   AND R.CHILDORG_ID = B.DEALER_ID\n");
        if (!"".equals(dealerId)) {
            sql.append(" AND B.DEALER_ID =").append(dealerId);
        }
        if (!"".equals(sellerId)) {
            sql.append(" AND  B.PARENTORG_ID =").append(sellerId);
        }
        sql.append(" and  r.state='").append(Constant.STATUS_ENABLE).append("' AND r.status='1'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return new HashMap();
        }
        return list.get(0);
    }


    public List<Map<String, Object>> getAccountRecord(String orderId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(amount) sumAmount,account_id from tt_part_account_record where source_id='");
        sql.append(orderId).append("' group by account_id");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }


    public String getPrice(String dealerId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  ");
        sql.append(" TO_CHAR(PKG_PART.F_GETPRICE(").append(dealerId).append(",").append(partId).append("),'FM999,999,990.00') AS price ");
        sql.append(" from dual ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("PRICE"));
    }

    public void updateAccount(String sourceId, String state) {
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE Tt_Part_Account_Record SET State='").append(state).append("'");
        sql.append(" where Source_Id='").append(sourceId).append("'");
        this.update(sql.toString(), null);
    }

    public Long getOrderNum(String partId, String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select buy_qty from tt_part_dlr_order_dtl ");
        sql.append(" where 1=1 ");
        sql.append(" and order_id='").append(orderId).append("'");
        sql.append(" and part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null || list.get(0).get("BUY_QTY") == null) {
            return null;
        }
        return Long.valueOf(CommonUtils.checkNull(list.get(0).get("BUY_QTY")));
    }

    public Map<String, Object> getOutAmount(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select amount,out_id from  tt_part_outstock_main ");
        sql.append(" where 1=1 ");
        sql.append(" and so_id='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public String getOutQty(String outId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(sum(outstock_qty),'0') as OUT_QTY from  ");
        sql.append(" tt_part_outstock_dtl where out_id='").append(outId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null || list.get(0).get("OUT_QTY") == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("OUT_QTY"));
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-9-9
     * @Title : 验证直发订单是否有打印
     */
    public List<Map<String, Object>> checkDirOrderPrint(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT  SM.*, TO_CHAR(SM.TRANS_PRINT_DATE,'yyyy-MM-dd') AS DPT_DATE  FROM TT_PART_SO_MAIN SM ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sqlStr
     * @return :
     * @throws : LastDate    : 2013-9-9
     * @Title : 直发订单打印信息更新
     */
    public void updateTranInfo(String sqlStr) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tt_part_so_main set TRANS_PRINT_NUM = nvl(TRANS_PRINT_NUM,1) + 1,TRANS_PRINT_DATE=sysdate where 1 = 1 ");
        sql.append(sqlStr);
        this.update(sql.toString(), null);
    }

    public List<Map<String, Object>> getStateOrder(String soId, String state) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_main where so_id='").append(soId).append("' and state='").append(state).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    //zhumingwei add 2013-10-23 添加销售快报
    public PageResult<Map<String, Object>> getSOSum(String sqlStr, int pageSize, int curPage) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT XS.*,\n");
        sql.append("       CW.*,\n");
        sql.append("       CK.*,\n");
        sql.append("       DCK.*,\n");
        sql.append("       INV.*,\n");
        sql.append("       NO_INV1.CG_NOINVOAMOUNT,\n");
        sql.append("       NO_INV2.ZF_NOINVOAMOUNT,\n");
        sql.append("       NO_INV3.QZGB_NOINVOAMOUNT,\n");
       /* sql.append("       (NO_INV1.CG_NOINVOAMOUNT + NO_INV2.ZF_NOINVOAMOUNT +\n");
        sql.append("       NO_INV3.QZGB_NOINVOAMOUNT) NOINVO_AMOUNT\n");*/
        sql.append("CK.CK_AMOUNT - INV.INVO_AMOUNT NOINVO_AMOUNT\n");
        sql.append("  FROM (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) CW_AMOUNT\n");
        sql.append("          FROM TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE SM.FCAUDIT_DATE IS NOT NULL\n");
        sql.append("           AND SM.STATE NOT IN (92401003, 92401009)\n");
        sql.append(sqlStr);
        sql.append("\t\t\t ) CW,\n");
        sql.append("       (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) XS_AMOUNT\n");
        sql.append("          FROM TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append("           AND SM.STATE NOT IN (92401003, 92401009)\n");
        sql.append(sqlStr);
        sql.append("\t\t\t ) XS,\n");
        sql.append("(SELECT SUM(CK_AMOUNT) CK_AMOUNT\n");
        sql.append("   FROM (SELECT ROUND(SUM(SM.AMOUNT) / 1.17, 2) CK_AMOUNT\n");
        sql.append("           FROM TT_PART_SO_MAIN SM\n");
        sql.append("          WHERE 1 = 1\n");
        sql.append(sqlStr);
        sql.append("            AND SM.STATE IN (92451003, 92561001)\n");
        sql.append("            AND SM.ORDER_TYPE <> 92151004\n");
        sql.append("         UNION ALL\n");
        sql.append("         SELECT ROUND(SUM(SM.AMOUNT) / 1.17, 2) CK_AMOUNT\n");
        sql.append("           FROM TT_PART_SO_MAIN SM\n");
        sql.append("          WHERE 1 = 1\n");
        sql.append(sqlStr);
        sql.append("            AND SM.STATE IN (92451003, 92561001)\n");
        sql.append("            AND SM.ORDER_TYPE = 92151004\n");
        sql.append("            AND EXISTS (SELECT 1\n");
        sql.append("                   FROM TT_PART_OUTSTOCK_MAIN OM\n");
        sql.append("                  WHERE OM.SO_ID = SM.SO_ID)) A) CK,\n");
        sql.append("(SELECT SUM(DCK_AMOUNT) DCK_AMOUNT\n");
        sql.append("   FROM (SELECT ROUND(SUM(SM.AMOUNT) / 1.17, 2) DCK_AMOUNT\n");
        sql.append("           FROM TT_PART_SO_MAIN SM\n");
        sql.append("          WHERE 1 = 1\n");
        sql.append(sqlStr);
        sql.append("            AND SM.STATE IN (92371005, 92451001, 92451002)\n");
        sql.append("         UNION ALL\n");
        sql.append("         SELECT ROUND(SUM(SM.AMOUNT) / 1.17, 2) DCK_AMOUNT\n");
        sql.append("           FROM TT_PART_SO_MAIN SM\n");
        sql.append("          WHERE 1 = 1\n");
        sql.append(sqlStr);
        sql.append("            AND SM.STATE IN (92451003, 92561001)\n");
        sql.append("            AND SM.ORDER_TYPE = 92151004\n");
        sql.append("            AND NOT EXISTS (SELECT 1\n");
        sql.append("                   FROM TT_PART_OUTSTOCK_MAIN OM\n");
        sql.append("                  WHERE OM.SO_ID = SM.SO_ID)) A) DCK,\n");
        sql.append("       (SELECT ROUND(SUM(OM.AMOUNT)/1.17,2) INVO_AMOUNT\n");
        sql.append("          FROM TT_PART_OUTSTOCK_MAIN OM\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append("           AND OM.BILL_NO IS NOT NULL\n");
        sql.append("           AND EXISTS (SELECT 1\n");
        sql.append("                  FROM TT_PART_SO_MAIN SM\n");
        sql.append("                 WHERE SM.SO_ID = OM.SO_ID\n");
        sql.append(sqlStr);
        sql.append("\t\t\t)) INV,\n");
        sql.append("(SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) CG_NOINVOAMOUNT\n");
        sql.append("        FROM TT_PART_SO_MAIN SM\n");
        sql.append("       WHERE 1 = 1\n");
        sql.append(sqlStr);
        sql.append("         AND EXISTS (SELECT 1\n");
        sql.append("                FROM TT_PART_OUTSTOCK_MAIN OM\n");
        sql.append("               WHERE SM.SO_ID = OM.SO_ID\n");
        sql.append("                 AND OM.BILL_NO IS NULL)) NO_INV1,\n");
        sql.append("     (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) ZF_NOINVOAMOUNT\n");
        sql.append("        FROM TT_PART_SO_MAIN SM\n");
        sql.append("       WHERE 1 = 1\n");
        sql.append(sqlStr);
        sql.append("         AND SM.ORDER_TYPE = 92151004\n");
        sql.append("         AND NOT EXISTS (SELECT 1\n");
        sql.append("                FROM TT_PART_OUTSTOCK_MAIN OM\n");
        sql.append("               WHERE SM.SO_ID = OM.SO_ID)) NO_INV2,\n");
        sql.append("     (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) QZGB_NOINVOAMOUNT\n");
        sql.append("        FROM TT_PART_SO_MAIN SM\n");
        sql.append("       WHERE 1 = 1\n");
        sql.append(sqlStr);
        sql.append("         AND SM.ORDER_TYPE != 92151004\n");
        sql.append("         AND SM.STATE = 92401009\n");
        sql.append("         AND NOT EXISTS (SELECT 1\n");
        sql.append("                FROM TT_PART_OUTSTOCK_MAIN OM\n");
        sql.append("               WHERE SM.SO_ID = OM.SO_ID)) NO_INV3\n");

        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    public String getUserType(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT user_type AS userType FROM tt_part_userpose_define d WHERE  d.user_id=").append(userId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("USERTYPE"));
    }

    public List<Map<String, Object>> getGift(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select tpd.part_oldcode,tpd.part_cname,tpd.part_code,tpsg.gift_qty from tt_part_so_gift tpsg ");
        sql.append(" join tt_part_define tpd on tpd.part_id=tpsg.part_id ");
        sql.append(" where so_id='").append(soId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public PageResult<Map<String, Object>> getOUTSum(String sqlStr, int pageSize, int curPage) {
        PageResult<Map<String, Object>> result = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT CK.*,\n");
        sql.append("       DCK.*,\n");
        sql.append("       ZXZ.*,\n");
        sql.append("       YZX.*,\n");
        sql.append("       WHB.*,\n");
        sql.append("       CKYF.*,\n");
        sql.append("       QZGB.*\n");
        sql.append("  FROM (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) CK_AMOUNT\n");
        sql.append("          FROM TT_PART_OUTSTOCK_MAIN SM\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append(sqlStr);
        sql.append("\t\t\t ) CK,\n");
        sql.append("       (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) DCK_AMOUNT\n");
        sql.append("          FROM TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append("           AND SM.STATE IN (92371005, 92451001, 92451002)\n");
        sql.append(sqlStr);
        sql.append("\t\t\t ) DCK,\n");
        sql.append("       (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) ZXZ_AMOUNT\n");
        sql.append("          FROM TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append("           AND (SM.STATE IN (92451001) OR\n");
        sql.append("               (SM.STATE = 92371005 AND SM.PICK_ORDER_ID IS NOT NULL))\n");
        sql.append(sqlStr);
        sql.append("\t\t\t ) ZXZ,\n");
        sql.append("       (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) YZX_AMOUNT\n");
        sql.append("          FROM TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append("           AND SM.STATE IN (92451002)\n");
        sql.append(sqlStr);
        sql.append("\t\t\t ) YZX,\n");
        sql.append("       (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) WHB_AMOUNT\n");
        sql.append("          FROM TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE SM.FCAUDIT_DATE IS NOT NULL\n");
        sql.append("           AND SM.STATE NOT IN (92401003)\n");
        sql.append("           AND SM.PICK_ORDER_ID IS NULL\n");
        sql.append("           AND SM.ORDER_TYPE <> 92151004\n");
        sql.append(sqlStr);
        sql.append("\t\t\t ) WHB,\n");
        sql.append("       (SELECT ROUND(SUM(SM.FREIGHT)/1.17,2) YF_AMOUNT\n");
        sql.append("          FROM TT_PART_OUTSTOCK_MAIN SM\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append(sqlStr);
        sql.append("\t\t\t ) CKYF,\n");
        sql.append("       (SELECT ROUND(SUM(SM.AMOUNT)/1.17,2) QZGB_AMOUNT\n");
        sql.append("          FROM TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append("           AND SM.STATE IN (92451003, 92561001,92401009)\n");
        sql.append("           AND SM.ORDER_TYPE <> 92151004\n");
        sql.append("           AND NOT EXISTS\n");
        sql.append("         (SELECT 1\n");
        sql.append("                  FROM TT_PART_OUTSTOCK_MAIN OM\n");
        sql.append("                 WHERE OM.SO_ID = SM.SO_ID)\n");
        sql.append(sqlStr);
        sql.append("\t\t\t\t) QZGB\n");

        result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return result;
    }

    /**
     * 无效销售单过滤
     *
     * @param boId
     * @throws Exception
     */
    public void delInvalidSo() throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("DELETE FROM tt_part_so_main m WHERE NOT exists(SELECT 1 FROM tt_part_so_dtl sd WHERE sd.so_id=m.so_id )");
            delete(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryBoDtl(Long soId, String partId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT T1.RELATION_ID, T1.BO_ID, T2.BOLINE_ID, T2.PART_ID, T2.BO_ODDQTY, T2.TOSAL_QTY\n");
            sql.append("   FROM TT_PART_BOTOSO_RELATION T1, TT_PART_BO_DTL T2 \n");
            sql.append("  WHERE T1.BO_ID = T2.BO_ID \n");
            sql.append("    AND T1.PART_ID = T2.PART_ID \n");
            sql.append("    AND T1.SO_ID = ").append(soId);
            sql.append("    AND T1.PART_ID = ").append(partId);
            sql.append("  ORDER BY T2.BO_ODDQTY DESC \n");

            return this.pageQuery(sql.toString(), null, this.getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> countBoDtl(Long soId, String partId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT SUM(T2.BO_ODDQTY) ALL_BO_ODDQTY, SUM(T2.TOSAL_QTY) ALL_TOSAL_QTY \n");
            sql.append("   FROM TT_PART_BOTOSO_RELATION T1, TT_PART_BO_DTL T2 \n");
            sql.append("  WHERE T1.BO_ID = T2.BO_ID \n");
            sql.append("    AND T1.PART_ID = T2.PART_ID \n");
            sql.append("    AND T1.SO_ID = ").append(soId);
            sql.append("    AND T1.PART_ID = ").append(partId);

            return this.pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }
}
