package com.infodms.dms.dao.sales.ordermanage.orderreport;

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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketPartDlrOrderDao extends BaseDao {
    public Logger logger = Logger.getLogger(MarketPartDlrOrderDao.class);
    private static final MarketPartDlrOrderDao dao = new MarketPartDlrOrderDao();

    private MarketPartDlrOrderDao() {

    }

    public static final MarketPartDlrOrderDao getInstance() {
        return dao;
    }

    private Object wl_NO;

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
    public PageResult<Map<String, Object>> queryPartDlrOrder(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        String oemFlag = "";
        if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
            oemFlag = Constant.PART_BASE_FLAG_YES + "";
        } else {
            oemFlag = Constant.PART_BASE_FLAG_NO + "";
        }
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String sCreateDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SSUBMIT_DATE"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("ESUBMIT_DATE"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态

        String discount = CommonUtils.checkNull(request.getParamValue("discount"));//
        sql.append(" select ");
        sql.append(" order_id,order_code,order_type,pay_type,dealer_id,dealer_code, ");
        sql.append(" dealer_name,seller_id,seller_code,seller_name,seller_type,buyer_id,buyer_name, ");
        sql.append(" rcv_orgid,rcv_org,addr_id,addr,receiver,tel,post_code,station,trans_type,account_sum,account_ky, ");
        sql.append(" account_dj,to_char(order_amount,'FM999,999,999,999,990.00') as order_amount,discount,remark,create_date,create_by,update_date,update_by,disable_date,disable_by, ");
        sql.append(" delete_date,delete_by,submit_date,submit_by,audit_date,audit_by,autchk_date,is_autchk,ver,state,status,wh_id,wh_cname,audit_opinion,freight,rebut_reason ");
        sql.append(" from tt_part_dlr_order_main  ");
        sql.append(" where 1=1 ");
        if (!"".equals(orderCode)) {
            sql.append(" and ORDER_CODE like '%").append(orderCode.toUpperCase()).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and SELLER_NAME like '%").append(sellerName).append("%'");
        }
        //前台已经加限制  必须要有时间范围  所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and CREATE_DATE>= to_date('").append(sCreateDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and CREATE_DATE<= to_date('").append(eCreateDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and ORDER_TYPE='").append(orderType).append("'");
        } else {
            sql.append(" and ORDER_TYPE in (").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02).append(")");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and SUBMIT_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(eSubmitDate)) {
            sql.append(" and SUBMIT_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (discount.equals("discount")) {
            sql.append(" and discount_Status='").append(Constant.IF_TYPE_YES).append("'");
        }
        if (!"".equals(state)) {
            if (!state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "") && !state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "") && !state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03 + "") && !state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "")) {
                sql.append(" and order_id in ( select order_id from tt_part_so_main where state='").append(state).append("')");
            } else {
                sql.append(" and STATE='").append(state).append("'");
            }
        }
        sql.append(" and oem_flag='").append(oemFlag).append("'");
        sql.append(" and dealer_id='").append(dealerId).append("' order by create_date desc");

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
    public PageResult<Map<String, Object>> queryOemPartDlrOrder(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        String oemFlag = "";
        if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
            oemFlag = Constant.PART_BASE_FLAG_YES + "";
        } else {
            oemFlag = Constant.PART_BASE_FLAG_NO + "";
        }
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String sCreateDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SSUBMIT_DATE"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("ESUBMIT_DATE"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态

        String discount = CommonUtils.checkNull(request.getParamValue("discount"));//
        sql.append(" select ");
        sql.append(" order_id,order_code,order_type,pay_type,dealer_id,dealer_code, ");
        sql.append(" dealer_name,seller_id,seller_code,seller_name,seller_type,buyer_id,buyer_name, ");
        sql.append(" rcv_orgid,rcv_org,addr_id,addr,receiver,tel,post_code,station,trans_type,account_sum,account_ky, ");
        sql.append(" account_dj,to_char(order_amount,'FM999,999,999,999,990.00') as order_amount,discount,remark,create_date,create_by,update_date,update_by,disable_date,disable_by, ");
        sql.append(" delete_date,delete_by,submit_date,submit_by,audit_date,audit_by,autchk_date,is_autchk,ver,state,status,wh_id,wh_cname,audit_opinion,freight ");
        sql.append(" from tt_part_dlr_order_main  ");
        sql.append(" where 1=1 ");
        if (!"".equals(orderCode)) {
            sql.append(" and ORDER_CODE like '%").append(orderCode.toUpperCase()).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and SELLER_NAME like '%").append(sellerName).append("%'");
        }
        //前台已经加限制  必须要有时间范围  所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and CREATE_DATE>= to_date('").append(sCreateDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and CREATE_DATE<= to_date('").append(eCreateDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and ORDER_TYPE='").append(orderType).append("'");
        } else {
            sql.append(" and ORDER_TYPE in (").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 + "," + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02).append(")");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and SUBMIT_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and SUBMIT_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (discount.equals("discount")) {
            sql.append(" and discount_Status='").append(Constant.IF_TYPE_YES).append("'");
        }
        if (!"".equals(state)) {
            if (!state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "") && !state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "") && !state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03 + "") && !state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "")) {
                sql.append(" and order_id in ( select order_id from tt_part_so_main where state='").append(state).append("')");
            } else {
                sql.append(" and STATE='").append(state).append("'");
            }
        }
        sql.append(" and oem_flag='").append(oemFlag).append("'");
        sql.append(" and seller_id='").append(dealerId).append("' order by create_date desc");

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
     * @Description: 查询(审核)
     */
    public PageResult<Map<String, Object>> queryCheckPartDlrOrder(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String sCreateDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("CendDate"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态
        String autoPreCheck = CommonUtils.checkNull(request.getParamValue("autoPreCheck"));//自动预审
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        String planFlag = request.getParamValue("planFlag");
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订货单位
        String orderBy = CommonUtils.checkNull(request.getParamValue("order_By"));//排序方式

        String dealerId = "";
        String userType = null;
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

        TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
        userposeDefinePO.setUserId(loginUser.getUserId());
        if (dao.select(userposeDefinePO).size() > 0) {
            userposeDefinePO = (TtPartUserposeDefinePO) dao.select(userposeDefinePO).get(0);
            userType = userposeDefinePO.getUserType() + "";
        }
        ;

        sql.append("SELECT A.ORDER_ID,\n");
        sql.append("       A.ORDER_CODE,\n");
        sql.append("       A.ORDER_TYPE,\n");
        sql.append("       A.PAY_TYPE,\n");
        sql.append("       A.DEALER_ID,\n");
        sql.append("       A.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       A.SELLER_ID,\n");
        sql.append("       A.SELLER_CODE,\n");
        sql.append("       A.SELLER_NAME,\n");
        sql.append("       A.SELLER_TYPE,\n");
        sql.append("       A.BUYER_ID,\n");
        sql.append("       A.BUYER_NAME,\n");
        sql.append("       A.RCV_ORGID,\n");
        sql.append("       A.RCV_ORG,\n");
        sql.append("       A.ADDR_ID,\n");
        sql.append("       A.ADDR,\n");
        sql.append("       RECEIVER,\n");
        sql.append("       TEL,\n");
        sql.append("       A.POST_CODE,\n");
        sql.append("       A.STATION,\n");
        sql.append("       A.TRANS_TYPE,\n");
        sql.append("       A.ACCOUNT_SUM,\n");
        sql.append("       A.ACCOUNT_KY,\n");
        sql.append("       A.ACCOUNT_DJ,\n");
        sql.append("       TO_CHAR(A.ORDER_AMOUNT, 'FM999,999,999,999,990.00') AS ORDER_AMOUNT,\n");
        sql.append("       A.DISCOUNT,\n");
        sql.append("       A.REMARK,\n");
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
        sql.append("       A.AUTCHK_DATE,\n");
        sql.append("       A.IS_AUTCHK,\n");
        sql.append("       A.VER,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.STATUS,\n");
        sql.append("       A.WH_ID,\n");
        sql.append("       A.WH_CNAME,\n");
        sql.append("       A.AUDIT_OPINION,\n");
        sql.append("       A.REBUT_REASON,\n");
        sql.append("       A.OEM_FLAG,a.order_num\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN A, TM_DEALER TD\n");
        /*sql.append(" join tt_part_salesscope_define b on a.dealer_id=b.dealer_id and b.user_id='").append(loginUser.getUserId()).append("'");*/
        sql.append(" where 1=1 ");
        sql.append("AND A.DEALER_ID = TD.DEALER_ID\n");
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
                    sql.append("        AND D.USER_ID = " + loginUser.getUserId() + "\n");
                    sql.append("        AND D.STATE = 10011001\n");
                    sql.append("        AND D.STATUS = 1)");
                } else if ("5".equals(userType)) {//精品配件销售员订单类型限制
                    sql.append("AND EXISTS (SELECT 1\n");
                    sql.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                    sql.append("      WHERE D.ORDER_TYPE = A.ORDER_TYPE\n");
                    sql.append("        AND D.USER_ID = " + loginUser.getUserId() + "\n");
                    sql.append("        AND D.STATE = 10011001\n");
                    sql.append("        AND D.STATUS = 1)\n");
                }

            }
        }
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode).append("%'\n");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName).append("%'\n");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(td.Dealer_code) like upper('%").append(dealerCode).append("%')\n");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName).append("%'\n");
        }
        //前台已经加限制  必须要有时间范围  所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(eCreateDate)) {
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE=").append(orderType + "\n");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(eSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'\n");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'\n");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07).append("\n");
        sql.append(") and a.seller_id='").append(CommonUtils.checkNull(dealerId)).append("'\n");
        /*if(getUserType(loginUser.getUserId()).equals("6")){
            sql.append(" AND A.create_by=").append(loginUser.getUserId());
        }*/
        //add by yuan 20140305  过滤广宣品订单
        sql.append(" and a.ORDER_TYPE !=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "\n");
        //add by yuan 20140305  过滤替换件订单
        sql.append(" and a.ORDER_TYPE !=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10 + "\n");
        //end
        if (!"".equals(orderBy) && null != orderBy) {
            if ("1".equals(orderBy)) {
                sql.append(" order by a.ORDER_CODE asc \n");
            } else if ("2".equals(orderBy)) {
                sql.append(" order by a.SUBMIT_DATE asc \n");
            } else if ("3".equals(orderBy)) {
                sql.append(" order by a.DEALER_CODE ,a.SUBMIT_DATE asc\n");
            } else if ("4".equals(orderBy)) {
                sql.append(" order by a.ORDER_TYPE ,a.SUBMIT_DATE asc\n");
            }
        } else {
            sql.append(" order by a.SUBMIT_DATE asc\n");
        }

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
     * @Description: 查询(审核导出)
     */
    public List<Map<String, Object>> queryCheckPartDlrOrder(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String sCreateDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("CendDate"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态
        String autoPreCheck = CommonUtils.checkNull(request.getParamValue("autoPreCheck"));//自动预审
        String orderBy = CommonUtils.checkNull(request.getParamValue("order_By"));//排序方式
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        sql.append(" select ");
        sql.append(" order_id,order_code,(select tc.code_desc from tc_code tc where tc.code_id=order_type ) as order_type,pay_type,dealer_id,dealer_code, ");
        sql.append(" dealer_name,seller_id,seller_code,seller_name,seller_type,buyer_id,buyer_name,remark ");
        sql.append(" rcv_orgid,rcv_org,addr_id,addr,receiver,tel,post_code,station,trans_type,account_sum,account_ky, ");
        sql.append(" account_dj,to_char(order_amount,'FM999,999,999,999,990.00') as order_amount,discount,remark,create_date,create_by,update_date,update_by,disable_date,disable_by, ");
        sql.append(" delete_date,delete_by,submit_date,submit_by,audit_date,audit_by,autchk_date,is_autchk,ver,(select tc.code_desc from tc_code tc where tc.code_id=state ) as state,status,wh_id,wh_cname,audit_opinion ");
        sql.append(" from tt_part_dlr_order_main a ");
        sql.append(" where 1=1 ");
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName).append("%'");
        }
        //前台已经加限制  必须要有时间范围  所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE='").append(orderType).append("'");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
        sql.append(")");
       /* if(getUserType(loginUser.getUserId()).equals("6")){
            sql.append(" AND create_by=").append(loginUser.getUserId());
        }*/
        //add by yuan 20140322  过滤广宣品订单
        sql.append(" and a.ORDER_TYPE !=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
        //add by yuan 20140322  过滤替换件订单
        sql.append(" and a.ORDER_TYPE !=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10);
        //end
        if (!"".equals(orderBy) && null != orderBy) {
            if ("1".equals(orderBy)) {
                sql.append(" order by a.ORDER_CODE asc ");
            } else if ("2".equals(orderBy)) {
                sql.append(" order by a.SUBMIT_DATE asc ");
            } else if ("3".equals(orderBy)) {
                sql.append(" order by a.DEALER_CODE ,a.SUBMIT_DATE asc");
            } else if ("4".equals(orderBy)) {
                sql.append(" order by a.ORDER_TYPE ,a.SUBMIT_DATE asc");
            }
        } else {
            sql.append(" order by a.SUBMIT_DATE asc");
        }
        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 获取仓库
     */
    public List<Map<String, Object>> getWareHouse() {
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.wh_id,t.wh_name from TT_PART_WAREHOUSE_DEFINE t");
        List<Map<String, Object>> wareHoustList = this.pageQuery(sql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    /**
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 获取订货单位
     */
    public Map<String, Object> getDealerName(String dealerId) {
        StringBuffer sql = new StringBuffer();
//        sql.append(" select * from  TM_DEALER t ");
//        sql.append(" where 1=1 ");
//        sql.append(" and t.DEALER_ID='").append(dealerId).append("'");

        sql.append(" SELECT DEALER_ID,DEALER_NAME ,DEALER_CODE ");
        sql.append(" FROM TM_DEALER D ");
        sql.append(" WHERE D.DEALER_ID ='").append(dealerId).append("'");
        sql.append(" 	AND D.STATUS =  10011001 ");


        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 获取折扣率
     */
    public String getDiscount(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(DISCOUNT,'1') DISCOUNT from  TT_PART_SALES_RELATION t ");
        sql.append(" where 1=1 ");
        sql.append(" and t.CHILDORG_ID='").append(dealerId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() <= 0) {
            return null;
        }
        return CommonUtils.checkNull(list.get(0).get("DISCOUNT"));
    }

    /**
     * @param : @param orderId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 获取销售单位和接受单位
     */
    public PageResult<Map<String, Object>> getSalesRelation(String dealerId, String yieldId, String type, String name, String code, String fixValue, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        //1为销售单位
        //2为接收单位
        //3为地址
        if ("1".equals(type)) {
            sql.append(" with oem_org as \n");
            sql.append("  (select og.org_id   PARENTORG_ID, \n");
            sql.append("          og.org_name PARENTORG_NAME, \n");
            sql.append("          og.org_code PARENTORG_CODE \n");
            sql.append("     from tm_org og \n");
            sql.append("    where og.org_iD = 2010010100070674) \n");
            sql.append(" SELECT oo.PARENTORG_ID, \n");
            sql.append("        oo.PARENTORG_NAME, \n");
            sql.append("        oo.PARENTORG_CODE, \n");
            sql.append("        DECODE(ACC.FIN_TYPE, \n");
            sql.append("               10251001, \n");
            sql.append("               NVL(ACC.AMOUNT, 0) + \n");
            sql.append("               NVL((SELECT CREDIT_AMOUNT \n");
            sql.append("                     FROM TT_SALES_DEALER_CREDIT_LIMIT \n");
            sql.append("                    WHERE DEALER_ID = ").append(dealerId).append(" \n");
            sql.append("                      AND STATUS = 10011001 \n");
            sql.append("                      AND TRUNC(TERMINATION_DATE) >= TRUNC(SYSDATE) \n");
            sql.append("                      AND TRUNC(EFFECT_DATE) <= TRUNC(SYSDATE)), \n");
            sql.append("                   0), \n");
            sql.append("               NVL(ACC.AMOUNT, 0)) ACCOUNT_KY, -- 可用余额     \n");
            sql.append("        NVL(ACC.AMOUNT, 0) + NVL(ACC.FREEZE_AMOUNT, 0) ACCOUNT_SUM, \n");
            sql.append("        acc.acc_id ACCOUNT_ID, \n");
            sql.append("        0          ACCOUNT_DJ \n");
            sql.append("   FROM TT_SALES_FIN_ACC ACC, oem_org oo \n");
            sql.append("  WHERE ACC.DEALER_ID =").append(dealerId).append(" \n");
            sql.append("    AND ACC.YIELDLY = ").append(yieldId).append(" \n");
            sql.append("    AND ACC.FIN_TYPE = ").append(Constant.ACCOUNT_TYPE_01).append(" \n");

        } else if ("2".equals(type)) {
            sql.append(" SELECT Distinct R.CHILDORG_ID, R.CHILDORG_NAME FROM TT_PART_SALES_RELATION R");
            sql.append(" WHERE R.childorg_id ='").append(dealerId).append("' ");
            sql.append(" AND R.state='").append(Constant.STATUS_ENABLE).append("' AND R.status='1' and R.childorg_id<>'").append(Constant.OEM_ACTIVITIES).append("'");
            if (!"".equals(name)) {
                sql.append(" and R.CHILDORG_NAME like '%").append(name).append("%'");
            }
            if (!"".equals(code)) {
                sql.append(" and upper(R.CHILDORG_CODE) like upper('%").append(code).append("%')");
            }
            sql.append(" AND EXISTS (SELECT 1\n");
            sql.append("       FROM TM_DEALER TD\n");
            sql.append("      WHERE TD.DEALER_ID = R.CHILDORG_ID\n");
            sql.append("        AND TD.SERVICE_STATUS != 13691004)");
            sql.append(" UNION ");
            sql.append(" SELECT Distinct R.CHILDORG_ID, R.CHILDORG_NAME FROM TT_PART_SALES_RELATION R ");
            sql.append(" WHERE R.Parentorg_Id ='").append(dealerId).append("'");
            sql.append(" AND R.state='").append(Constant.STATUS_ENABLE).append("' AND R.status='1' ");
            if (!"".equals(name)) {
                sql.append(" and R.CHILDORG_NAME like '%").append(name).append("%'");
            }
            if (!"".equals(code)) {
                sql.append(" and upper(R.CHILDORG_CODE) like upper('%").append(code).append("%')");
            }
            sql.append("  AND EXISTS (SELECT 1\n");
            sql.append("       FROM TM_DEALER TD\n");
            sql.append("      WHERE TD.DEALER_ID = R.CHILDORG_ID\n");
            sql.append("        AND TD.SERVICE_STATUS != 13691004)");

        } else if ("3".equals(type)) {

            sql.append(" select * from TM_VS_ADDRESS va where va.address_type=20481001 and va.dealer_id='").append(dealerId).append("'");
            sql.append(" union ");
            sql.append(" select * from TM_VS_ADDRESS va where va.address_type=20481004 and va.dealer_id='").append(dealerId).append("'");
        } else if ("4".equals(type) || "5".equals(type) || "6".equals(type)) {
            sql.append("select td.dealer_id   CHILDORG_ID, \n");
            sql.append("       td.dealer_name CHILDORG_NAME, \n");
            sql.append("       td.dealer_code CHILDORG_CODE \n");
            sql.append("  from tm_dealer td \n");
            sql.append(" where td.dealer_type = 10771001 \n");
            sql.append("   and td.service_status = 13691002 \n");
            if (!"".equals(name)) {
                sql.append(" and td.dealer_name like '%").append(name).append("%'\n");
            }
            if (!"".equals(code)) {
                sql.append(" and td.dealer_code like '%").append(code).append("%'");
            }
        } else {
            return null;
        }
        return this.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

    /**
     * @param : @param orderId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 获取地址
     */
    public PageResult<Map<String, Object>> getAddress(String dealerId, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" ADDR_ID,ADDR ");
        sql.append(" from tt_part_addr_define ");
        sql.append(" where 1=1 ");
        sql.append(" and DEALER_ID='").append(dealerId).append("'");
        return this.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
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
    //RequestWrapper中需要放入获取金额的dealer_id  销售单和提报的dealer_id不一样
    public PageResult<Map<String, Object>> showPartBase(RequestWrapper req, String sellerId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String produceFac = CommonUtils.checkNull(req.getParamValue("produceFac"));
        String orderType = CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"));

        sql.append("SELECT A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.MODEL_NAME,\n");
        sql.append("       A.PIC_URL,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT,\n");
        sql.append("       NVL((SELECT SUM(S.normal_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = A.PART_ID\n");
        sql.append("              AND S.ORG_ID = '").append(req.getAttribute("bookDealerId")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES)) { //主机厂按照最小包装量量销售
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                    FROM TM_DEALER D\n");
            sql.append("                   WHERE D.DEALER_ID = ").append(req.getAttribute("bookDealerId"));
            sql.append("                     AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) MIN_PACKAGE,\n");//供应中心油品整箱，其他1
        }
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
        sql.append("       (SELECT CASE\n");
        sql.append("                 WHEN SUM(normal_QTY) > 0 THEN\n");
        sql.append("                  'Y'\n");
        sql.append("                 WHEN SUM(normal_QTY) = 0 THEN\n");
        sql.append("                  'N'\n");
        sql.append("                 ELSE\n");
        sql.append("                  'N'\n");
        sql.append("               END\n");
        sql.append("          FROM VW_PART_STOCK E\n");
        sql.append("         WHERE E.PART_ID = A.PART_ID\n");
        sql.append("           AND E.STATE = 10011001\n");
        sql.append("           AND E.STATUS = 1\n");
        sql.append("           AND E.ORG_ID = ").append(sellerId).append(") AS UPORGSTOCK,\n");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("            WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("              AND ORG_ID ='").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append("'),'0') as qty,\n");
        sql.append("  nvl(a.is_special,'10041002') as is_special ");
        sql.append("  FROM TT_PART_DEFINE A\n");
        sql.append(" WHERE A.STATUS = 1\n");

        sql.append(" and  A.produce_Fac='").append(produceFac).append("'");

        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "").equals(orderType)) {
            sql.append(" and A.is_gxp = " + Constant.IF_TYPE_YES);
        } else {
            sql.append(" and A.is_gxp = " + Constant.IF_TYPE_NO);
        }
        String uploadFlag = CommonUtils.checkNull(req.getAttribute("uploadFlag"));
        if (!"".equals(uploadFlag) && "upload".equals(uploadFlag)) {
            if (!"".equals(CommonUtils.checkNull(req.getAttribute("partOldcode")))) {
                sql.append(" and a.PART_OLDCODE ='").append(req.getAttribute("partOldcode")).append("'");
            }
        }
        if (!"".equals(CommonUtils.checkNull(orderType))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03).equals(orderType)) {
                //如果是计划订单(特殊类型订单) 配件类型筛选
                if (CommonUtils.checkNull(req.getParamValue("isSpecialOrPlan")).equals("1")) {  //1 特殊配件
                    sql.append(" and a.is_special='").append(Constant.IF_TYPE_YES).append("'");
                } else if (CommonUtils.checkNull(req.getParamValue("isSpecialOrPlan")).equals("2")) {  //2 计划配件
                    sql.append(" and a.is_plan='").append(Constant.IF_TYPE_YES).append("'").append(" and ( a.is_special='").append(Constant.IF_TYPE_NO).append("' or a.is_special is null or a.is_special='' )");
                }
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).equals(orderType)) {
//                sql.append(" and part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='").append(CommonUtils.checkNull(req.getParamValue("brand"))).append("')");
                sql.append(" and A.IS_DIRECT ='").append(Constant.IF_TYPE_YES).append("'");
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%").append(req.getParamValue("PART_CODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%").append(req.getParamValue("PART_OLDCODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%").append(req.getParamValue("PART_CNAME").toUpperCase()).append("%'");
        }
        //add by yuan 20130913   踢掉销售单位无效无库存配件
        if (!(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "").equals(orderType)) {
            sql.append("AND not EXISTS (SELECT 1\n");
            sql.append("       FROM VW_PART_STOCK S\n");
            sql.append("      WHERE S.PDSTATE = 10011002\n");
            sql.append("        AND S.ORG_ID = ").append(sellerId);
            sql.append("        AND S.PART_ID = A.PART_ID\n");
            sql.append("        HAVING  SUM(S.NORMAL_QTY) = 0)\n");
        }
        if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03.toString())) {
            sql.append("AND A.IS_SPECIAL = ").append(Constant.IF_TYPE_YES);
        } else {
            //sql.append("AND A.IS_SPECIAL = ").append(Constant.IF_TYPE_NO);
        }
        if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 + "") || orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02 + "")) {
            sql.append("   AND A.IS_DIRECT = " + Constant.IF_TYPE_NO + "\n");
            sql.append("   AND A.IS_SPECIAL = " + Constant.IF_TYPE_NO + "\n");
            sql.append("   AND A.IS_GXP = " + Constant.IF_TYPE_NO + "\n");
        }
        sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
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
    //RequestWrapper中需要放入获取金额的dealer_id  销售单和提报的dealer_id不一样
    public PageResult<Map<String, Object>> showSellerPartBase(RequestWrapper req, String sellerId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String produceFac = CommonUtils.checkNull(req.getParamValue("produceFac"));
        String orderType = CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"));

        sql.append("SELECT A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT,\n");
        sql.append("       NVL((SELECT SUM(S.normal_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = A.PART_ID\n");
        sql.append("              AND S.ORG_ID = '").append(req.getAttribute("bookDealerId")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES) && !orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09.toString())) { //主机厂按照最小包装量量销售
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                    FROM TM_DEALER D\n");
            sql.append("                   WHERE D.DEALER_ID = ").append(req.getAttribute("bookDealerId"));
            sql.append("                     AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES) && orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09.toString())) {
            sql.append(" 1 MIN_PACKAGE,\n");//内部领用为1
        } else {
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) MIN_PACKAGE,\n");//供应中心油品整箱，其他1
        }
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
        sql.append("       nvl((SELECT SUM(normal_QTY)\n");
        sql.append("          FROM VW_PART_STOCK E\n");
        sql.append("         WHERE E.PART_ID = A.PART_ID\n");
        sql.append("           AND E.STATE = 10011001\n");
        sql.append("           AND E.STATUS = 1\n");
        sql.append("           AND E.ORG_ID = ").append(sellerId).append("),0) AS UPORGSTOCK,\n");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("            WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("              AND ORG_ID ='").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append("'),'0') as qty,\n");
        sql.append("  nvl(a.is_special,'10041002') as is_special ");
        sql.append("  FROM TT_PART_DEFINE A\n");
        sql.append(" WHERE A.STATUS = 1\n");

        sql.append(" and  A.produce_Fac='").append(produceFac).append("'"); //mob by 20140215
        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "").equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append(" and A.is_gxp = " + Constant.IF_TYPE_YES);
        } else {
            sql.append(" and A.is_gxp = " + Constant.IF_TYPE_NO);
        }
        String uploadFlag = CommonUtils.checkNull(req.getAttribute("uploadFlag"));
        if (!"".equals(uploadFlag) && "upload".equals(uploadFlag)) {
            if (!"".equals(CommonUtils.checkNull(req.getAttribute("partOldcode")))) {
                sql.append(" and a.PART_OLDCODE ='").append(req.getAttribute("partOldcode")).append("'");
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                //如果是计划订单(特殊类型订单) 配件类型筛选
                if (CommonUtils.checkNull(req.getParamValue("isSpecialOrPlan")).equals("1")) {  //1 特殊配件
                    sql.append(" and a.is_special='").append(Constant.IF_TYPE_YES).append("'");
                } else if (CommonUtils.checkNull(req.getParamValue("isSpecialOrPlan")).equals("2")) {  //2 计划配件
                    sql.append(" and a.is_plan='").append(Constant.IF_TYPE_YES).append("'").append(" and ( a.is_special='").append(Constant.IF_TYPE_NO).append("' or a.is_special is null or a.is_special='' )");
                }
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                sql.append(" and part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='").append(CommonUtils.checkNull(req.getParamValue("brand"))).append("')");
                /*sql.append(" and A.IS_DIRECT ='").append(Constant.IF_TYPE_YES).append("'");*/
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%").append(req.getParamValue("PART_CODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%").append(req.getParamValue("PART_OLDCODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%").append(req.getParamValue("PART_CNAME").toUpperCase()).append("%'");
        }

        //add by yuan 20130913   踢掉销售单位无效无库存配件
        sql.append("AND not EXISTS (SELECT 1\n");
        sql.append("       FROM VW_PART_STOCK S\n");
        sql.append("      WHERE S.PDSTATE = 10011002\n");
        sql.append("        AND S.ORG_ID = ").append(sellerId);
        sql.append("        AND S.PART_ID = A.PART_ID\n");
        sql.append("        HAVING  SUM(S.NORMAL_QTY) = 0)\n");
        if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03.toString())) {
            sql.append("AND A.IS_SPECIAL = ").append(Constant.IF_TYPE_YES);
        } else {
            sql.append("AND A.IS_SPECIAL = ").append(Constant.IF_TYPE_NO);
        }
        //add end
        //sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
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
    //RequestWrapper中需要放入获取金额的dealer_id  销售单和提报的dealer_id不一样
    public PageResult<Map<String, Object>> showDiscountPartBase(RequestWrapper req, String dealerId, int curPage, int pageSize) {
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
        sql.append("       NVL(A.UNIT, '件') UNIT， NVL((SELECT SUM(S.ITEM_QTY)\n");
        sql.append("                                     FROM TT_PART_ITEM_STOCK S\n");
        sql.append("                                    WHERE S.PART_ID = A.PART_ID\n");
        sql.append("                                      AND S.ORG_ID = '").append(req.getAttribute("bookDealerId")).append("'");
        sql.append("                                      AND S.STATE = 1\n");
        sql.append("                                      AND S.STATUS = 1), 0) AS ITEM_QTY,\n");
        sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
        sql.append("                    FROM TM_DEALER D\n");
        sql.append("                   WHERE D.DEALER_ID = ").append(req.getAttribute("bookDealerId"));
        sql.append("                     AND D.STATUS = 10011001),\n");
        sql.append("                  92101001,\n");
        sql.append("                  A.MIN_PACK1,\n");
        sql.append("                  A.MIN_PACK2),\n");
        sql.append("           0) AS MIN_PACKAGE,\n");
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append(", A.PART_ID)*B.RATE,'FM999,999,990.00') AS SALE_PRICE1,");
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
        sql.append("         AND E.ORG_ID = ").append(dealerId).append(") AS UPORGSTOCK\n");
        sql.append(" ,B.RATE ");
        sql.append(" ，nvl((select qty from vw_part_dlr_thm_per_sale vpdtps where vpdtps.part_id=A.part_id and org_id='").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append("'),'0') as qty");
        sql.append("  FROM TT_PART_DEFINE A\n");
        sql.append(" join VW_PART_DISCOUNT_DEFINE B on a.part_id=b.part_id  ");
        sql.append(" WHERE /*A.STATE = 10011001\n");
        sql.append("   AND*/\n");
        sql.append(" A.STATUS = 1\n");

        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "").equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append(" and part_id in (select part_id from TT_PART_SPECIAL_DEFINE  where sysdate between start_date and end_date ) ");

        }
        String uploadFlag = CommonUtils.checkNull(req.getAttribute("uploadFlag"));
        if (!"".equals(uploadFlag) && "upload".equals(uploadFlag)) {
            if (!"".equals(CommonUtils.checkNull(req.getAttribute("partOldcode")))) {
                sql.append(" and a.PART_OLDCODE ='").append(req.getAttribute("partOldcode")).append("'");
            }
            sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                sql.append(" and A.IS_PLAN ='").append(Constant.IF_TYPE_YES).append("'");
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                sql.append(" and part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='").append(CommonUtils.checkNull(req.getParamValue("brand"))).append("')");
                /*sql.append(" and A.IS_DIRECT ='").append(Constant.IF_TYPE_YES).append("'");*/
            }
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
        if (CommonUtils.checkNull(req.getParamValue("TRANS_TYPE")).equals("2")) {
            sql.append(" and a.PACK_STATE <>'").append(Constant.PART_PACK_STATE_02 + "").append("'");
        }
        sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
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
     * @Description: 查询账户
     */
    public Map<String, Object> getAccount(String dealerId, String parentId, String accountId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" ACCOUNT_ID, ACCOUNT_KIND, CHILDORG_ID, DEALER_ID, DEALER_CODE, DEALER_NAME, PARENTORG_ID, PARENTORG_CODE, PARENTORG_NAME, to_char(ACCOUNT_SUM,'FM999,999,999,999,990.00') as ACCOUNT_SUM, to_char(ACCOUNT_DJ,'FM999,999,999,999,990.00') as ACCOUNT_DJ, to_char(ACCOUNT_KY,'FM999,999,999,999,990.00') as ACCOUNT_KY, ACCOUNT_INVO ");
        sql.append(" from VW_PART_DLR_ACCOUNT a ");
        sql.append(" where 1=1 ");
        sql.append(" and DEALER_ID =").append(dealerId);
        sql.append(" and PARENTORG_ID=").append(parentId);
        if (!"".equals(accountId) && null != accountId) {
            sql.append(" and ACCOUNT_ID =").append(accountId);
        } else {
            sql.append(" AND a.ACCOUNT_PURPOSE=").append(Constant.PART_ACCOUNT_PURPOSE_TYPE_01);
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() <= 0) {
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
     * @Description: 查询账户
     */
    public Map<String, Object> getPartItemStock(String whId, String partId) {
        ActionContext act = ActionContext.getContext();
        //RequestWrapper request = act.getRequest();
        //String orgId = CommonUtils.checkNull(request.getAttribute("orgId"));
        StringBuffer sql = new StringBuffer();
       /* sql.append(" select t.part_id,sum(t.Item_Qty_Line) as NORMAL_QTY from vw_part_stock t ");
        sql.append(" where 1=1 ");
        sql.append(" and t.PART_ID ='").append(partId).append("'");
        sql.append(" and t.WH_ID='").append(whId).append("'");*/

        sql.append("SELECT T.PART_ID, SUM(T.NORMAL_QTY) NORMAL_QTY\n");
        sql.append("  FROM VW_PART_STOCK T\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND T.PART_ID = '").append(partId).append("'\n");
        sql.append("   AND T.WH_ID = '").append(whId).append("'\n");
        sql.append("   AND T.STATE = 10011001\n");
        sql.append("   AND T.STATUS = 1\n");
        sql.append(" GROUP BY T.PART_ID\n");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() == 0) {
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
     * @Description: 查询配件销售主订单
     */
    public Map<String, Object> queryPartDlrOrderMain(String orderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("WITH PART_DLR_ORDER_AMOUNT AS\n");
        sql.append(" (SELECT T.ORDER_ID,\n");
        sql.append("         SUM(NVL(DO.BUY_QTY, T.BUY_QTY)) YX_QTY,\n");
        sql.append("         TO_CHAR(SUM(T.BUY_AMOUNT), 'FM999,999,999,999,990.00') AS BUY_AMOUNT,\n");
        sql.append("         /* TO_CHAR(SUM(NVL(DO.BUY_QTY, T.BUY_QTY) * C.SALE_PRICE1),\n");
        sql.append("         'FM999,999,999,999,990.00') AS YX_AMOUNT,*/\n");
        sql.append("         TO_CHAR(SUM(NVL(DO.BUY_QTY, T.BUY_QTY) * T.BUY_PRICE),\n");
        sql.append("                 'FM999,999,999,999,990.00') AS YX_AMOUNT\n");
        sql.append("    FROM TT_PART_DLR_ORDER_DTL T\n");
        sql.append("    LEFT JOIN TT_PART_DEFINE B\n");
        sql.append("      ON T.PART_ID = B.PART_ID\n");
        sql.append("    LEFT JOIN TT_PART_SALES_PRICE C\n");
        sql.append("      ON C.PART_ID = T.PART_ID\n");
        sql.append("     AND C.STATE = '10011001'\n");
        sql.append("    LEFT JOIN TMP_PART_DLR_ORDERPART DO\n");
        sql.append("      ON DO.ORDER_ID = T.ORDER_ID\n");
        sql.append("     AND DO.PART_ID = T.PART_ID\n");
        sql.append("   WHERE 1=1\n");
        sql.append("   GROUP BY T.ORDER_ID)\n");
        sql.append("SELECT B.NAME,\n");
        sql.append("       T.ORDER_ID,\n");
        sql.append("       T.ORDER_CODE,\n");
        sql.append("       T.ORDER_TYPE,\n");
        sql.append("       T.PAY_TYPE,\n");
        sql.append("       T.DEALER_ID,\n");
        sql.append("       T.DEALER_CODE,\n");
        sql.append("       T.DEALER_NAME,\n");
        sql.append("       T.SELLER_ID,\n");
        sql.append("       T.SELLER_CODE,\n");
        sql.append("       T.SELLER_NAME,\n");
        sql.append("       T.SELLER_TYPE,\n");
        sql.append("       T.BUYER_ID,\n");
        sql.append("       T.BUYER_NAME,\n");
        sql.append("       T.RCV_ORGID,\n");
        sql.append("       T.RCV_ORG,\n");
        sql.append("       T.ADDR_ID,\n");
        sql.append("       NVL(T.ADDR, '无') ADDR,\n");
        sql.append("       NVL(T.RECEIVER, '配件联系人') RECEIVER,\n");
        sql.append("       NVL(T.TEL, '无') TEL,\n");
        sql.append("       NVL(T.POST_CODE, '无') POST_CODE,\n");
        sql.append("       NVL(T.STATION, '无') STATION,\n");
        sql.append("       T.TRANS_TYPE,\n");
        sql.append("       TO_CHAR(NVL(T.ACCOUNT_SUM, 0), 'FM999,999,999,999,990.00') AS ACCOUNT_SUM,\n");
        sql.append("       TO_CHAR(NVL(T.ACCOUNT_KY, 0), 'FM999,999,999,999,990.00') AS ACCOUNT_KY,\n");
        sql.append("       TO_CHAR(NVL(T.ACCOUNT_DJ, 0), 'FM999,999,999,999,990.00') AS ACCOUNT_DJ,\n");
        sql.append("       TO_CHAR(T.ORDER_AMOUNT, 'FM999,999,999,999,990.00') AS ORDER_AMOUNT,\n");
        sql.append("       T.DISCOUNT,\n");
        sql.append("       T.REMARK,\n");
        sql.append("       TO_CHAR(T.CREATE_DATE, 'yyyy-mm-dd') CREATE_DATE,\n");
        sql.append("       T.CREATE_BY,\n");
        sql.append("       T.UPDATE_DATE,\n");
        sql.append("       T.UPDATE_BY,\n");
        sql.append("       T.DISABLE_DATE,\n");
        sql.append("       T.DISABLE_BY,\n");
        sql.append("       T.DELETE_DATE,\n");
        sql.append("       T.DELETE_BY,\n");
        sql.append("       T.SUBMIT_DATE,\n");
        sql.append("       T.SUBMIT_BY,\n");
        sql.append("       T.AUDIT_DATE,\n");
        sql.append("       T.AUDIT_BY,\n");
        sql.append("       T.AUTCHK_DATE,\n");
        sql.append("       T.IS_AUTCHK,\n");
        sql.append("       T.VER,\n");
        sql.append("       T.STATE,\n");
        sql.append("       T.STATUS,\n");
        sql.append("       T.WH_ID,\n");
        sql.append("       T.WH_CNAME,\n");
        sql.append("       T.AUDIT_OPINION,\n");
        sql.append("       T.ORDER_AMOUNT AS AMOUNT,\n");
        sql.append("       TO_CHAR(T.FREIGHT, 'FM999,999,999,999,990.00') AS FREIGHT,\n");
        sql.append("       T.FREIGHT AS FREIGHT2,\n");
        sql.append("       T.PRODUCE_FAC,\n");
        sql.append("       T.LOCK_FREIGHT,\n");
        sql.append("       T.OEM_FLAG,\n");
        sql.append("       T.ACCOUNT_ID,\n");
        sql.append("       T.IS_TRANSFREE,\n");
        sql.append("       OA.YX_AMOUNT\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN T, TC_USER B, PART_DLR_ORDER_AMOUNT OA\n");
        sql.append(" WHERE T.ORDER_ID = OA.ORDER_ID(+)\n");
        sql.append("   AND T.CREATE_BY = B.USER_ID\n");
        sql.append("   AND T.ORDER_ID = " + orderId + "");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    public void updateOrderNum(Long orderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("update tt_part_dlr_order_main m\n");
        sql.append("set m.order_num = m.order_num - 1\n");
        sql.append("where exists (select 1\n");
        sql.append("from tt_part_dlr_order_main t1\n");
        sql.append("where t1.dealer_id = m.dealer_id\n");
        sql.append("and t1.order_type = m.order_type\n");
        sql.append("and to_char(t1.submit_date, 'yyyy-mm') =\n");
        sql.append("to_char(m.submit_date, 'yyyy-mm')\n");
        sql.append("and m.order_num > t1.order_num\n");
        sql.append("and t1.order_id = " + orderId + ")\n");
        sql.append("and m.order_num > 0 and m.oem_flag=10041002\n");

        update(sql.toString(), null);
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 查询配件销售订单详细
     */


    public List<Map<String, Object>> queryPartDlrOrderDetail(String orderId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orgId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            orgId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.LINE_ID,\n");
        sql.append("       T.ORDER_ID,\n");
        sql.append("       T.LINE_NO,\n");
        sql.append("       T.PART_ID,\n");
        sql.append("       T.PART_CODE,\n");
        sql.append("       T.PART_OLDCODE,\n");
        sql.append("       T.PART_CNAME,\n");
        sql.append("       T.UNIT,\n");
        sql.append("       T.IS_DIRECT,\n");
        sql.append("       T.IS_PLAN,\n");
        sql.append("       T.IS_LACK,\n");
        sql.append("       T.IS_REPLACED,\n");
        sql.append("       T.MIN_PACKAGE,\n");
        sql.append("       T.BUY_QTY,\n");
        sql.append("       NVL(DO.BUY_QTY, T.BUY_QTY) YX_QTY,\n");
        sql.append("       T.SALES_QTY,\n");
        sql.append("       T.BUY_QTY - NVL(DO.BUY_QTY, T.BUY_QTY) CLOSE_QTY,\n");
        sql.append("       TO_CHAR(T.BUY_PRICE, 'FM999,999,999,999,990.00') AS BUY_PRICE,\n");
        sql.append("       TO_CHAR(T.BUY_AMOUNT, 'FM999,999,999,999,990.00') AS BUY_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(DO.BUY_QTY, T.BUY_QTY) * T.BUY_PRICE,\n");
        sql.append("               'FM999,999,999,999,990.00') AS YX_AMOUNT,\n");
        sql.append("       T.IS_HAVA,\n");
        sql.append("       T.REMARK,\n");
        sql.append("       T.CREATE_DATE,\n");
        sql.append("       T.CREATE_BY,\n");
        sql.append("       T.UPDATE_DATE,\n");
        sql.append("       T.UPDATE_BY,\n");
        sql.append("       T.DISABLE_DATE,\n");
        sql.append("       NVL((SELECT SUM(S.NORMAL_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = T.PART_ID\n");
        sql.append("              AND S.ORG_ID = '" + orgId + "'\n");
        sql.append("              AND S.WH_TYPE = 1\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS STOCK_QTY,\n");
        sql.append("       T.DISABLE_BY,\n");
        sql.append("       T.DELETE_DATE,\n");
        sql.append("       T.DELETE_BY,\n");
        sql.append("       T.RESERVED_DATE,\n");
        sql.append("       T.VER,\n");
        sql.append("       T.STATUS,\n");
        sql.append("       --NVL(C.SALE_PRICE1, '0') SALE_PRICE1,\n");
        sql.append("       BUY_PRICE SALE_PRICE1,\n");
        sql.append("       B.IS_GIFT,\n");
        sql.append("       T.DEFT_ID\n");
        sql.append("  FROM TT_PART_DLR_ORDER_DTL T\n");
        sql.append("  LEFT JOIN TT_PART_DEFINE B\n");
        sql.append("    ON T.PART_ID = B.PART_ID\n");
        sql.append("  LEFT JOIN TT_PART_SALES_PRICE C\n");
        sql.append("    ON C.PART_ID = T.PART_ID\n");
        sql.append("   AND C.STATE = '10011001'\n");
        sql.append("  LEFT JOIN TMP_PART_DLR_ORDERPART DO\n");
        sql.append("    ON DO.ORDER_ID = T.ORDER_ID\n");
        sql.append("   AND DO.PART_ID = T.PART_ID\n");
        sql.append(" WHERE T.ORDER_ID = '" + orderId + "'\n");
        sql.append(" ORDER BY T.PART_OLDCODE");

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
     * @Description: 查询配件销售订单详细
     */


    public List<Map<String, Object>> queryPartDlrOrderDetail4SODTL(String orderId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" t.line_id ,t.order_id ,t.line_no ,t.part_id ,t.part_code ,t.part_oldcode ,t.part_cname,  ");
        sql.append(" t.unit ,t.is_direct ,t.is_plan ,t.is_lack ,t.is_replaced  ,t.min_package ,t.buy_qty ,t.sales_qty,  ");
        sql.append(" to_char(pkg_part.F_GETPRICE(m.dealer_id,t.part_id),'FM999,999,999,999,990.00') as buy_price,to_char(pkg_part.F_GETPRICE(m.dealer_id,t.part_id)*t.buy_qty,'FM999,999,999,999,990.00') as buy_amount ,t.is_hava ,t.remark ,t.create_date ,t.create_by ,t.update_date ,t.update_by ,t.disable_date, ");
        sql.append(" NVL((SELECT SUM(S.ITEM_QTY) ");
        sql.append("  FROM TT_PART_ITEM_STOCK S\n");
        sql.append("  WHERE S.PART_ID = t.PART_ID\n");
        sql.append("  AND S.ORG_ID = '").append(dealerId).append("'");
        sql.append("  AND S.STATE = 1\n");
        sql.append("  AND S.STATUS = 1), 0) AS stock_qty,\n");
        sql.append(" t.disable_by ,t.delete_date ,t.delete_by ,t.reserved_date ,t.ver ,t.status ,  ");
        sql.append(" nvl(c.SALE_PRICE1,'0') SALE_PRICE1, ");
        sql.append(" b.IS_GIFT,t.deft_id ");
        sql.append(" from TT_PART_DLR_ORDER_DTL t ");

        sql.append(" JOIN TT_PART_DLR_ORDER_MAIN M\n");
        sql.append("    ON M.ORDER_ID = T.ORDER_ID\n");

        sql.append(" left join TT_PART_DEFINE b on t.PART_ID=b.PART_ID ");
        sql.append(" left join  TT_PART_SALES_PRICE c on c.PART_ID=t.PART_ID and c.state='").append(Constant.STATUS_ENABLE).append("'");
        sql.append(" where t.ORDER_ID='").append(orderId).append("'");
        sql.append(" order by t.part_oldcode  ");
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
     * @Description: 查询配件销售订单详细
     */


    public List<Map<String, Object>> queryPartDlrOrderDetail(String orderId, String whId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.LINE_ID,\n");
        sql.append("       T.ORDER_ID,\n");
        sql.append("       T.LINE_NO,\n");
        sql.append("       T.PART_ID,\n");
        sql.append("       T.PART_CODE,\n");
        sql.append("       T.PART_OLDCODE,\n");
        sql.append("       T.PART_CNAME,\n");
        sql.append("       T.UNIT,\n");
        sql.append("       T.IS_DIRECT,\n");
        sql.append("       T.IS_PLAN,\n");
        sql.append("       T.IS_LACK,\n");
        sql.append("       T.IS_REPLACED,\n");
        sql.append("       T.MIN_PACKAGE,\n");
        sql.append("       T.BUY_QTY,\n");
        sql.append("       T.SALES_QTY,\n");
        sql.append("       TO_CHAR(T.BUY_PRICE, 'FM999,999,999,999,990.00') AS BUY_PRICE,\n");
        sql.append("       TO_CHAR(T.BUY_AMOUNT, 'FM999,999,999,999,990.00') AS BUY_AMOUNT,\n");
        sql.append("       T.IS_HAVA,\n");
        sql.append("       T.REMARK,\n");
        sql.append("       T.CREATE_DATE,\n");
        sql.append("       T.CREATE_BY,\n");
        sql.append("       T.UPDATE_DATE,\n");
        sql.append("       T.UPDATE_BY,\n");
        sql.append("       T.DISABLE_DATE,\n");
        sql.append("       NVL((SELECT S.NORMAL_QTY\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = T.PART_ID\n");
        sql.append("              AND S.ORG_ID = ").append(dealerId);
        sql.append("              AND S.WH_ID =").append(whId);
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS STOCK_QTY,\n");
        sql.append("       T.DISABLE_BY,\n");
        sql.append("       T.DELETE_DATE,\n");
        sql.append("       T.DELETE_BY,\n");
        sql.append("       T.RESERVED_DATE,\n");
        sql.append("       T.VER,\n");
        sql.append("       T.STATUS,\n");
        sql.append("       NVL(C.SALE_PRICE1, '0') SALE_PRICE1,\n");
        sql.append("       B.IS_GIFT,\n");
        sql.append("       T.DEFT_ID\n");
        sql.append("  FROM TT_PART_DLR_ORDER_DTL T\n");
        sql.append("  LEFT JOIN TT_PART_DEFINE B\n");
        sql.append("    ON T.PART_ID = B.PART_ID\n");
        sql.append("  LEFT JOIN TT_PART_SALES_PRICE C\n");
        sql.append("    ON C.PART_ID = T.PART_ID\n");
        sql.append("   AND C.STATE = '10011001'\n");
        sql.append(" WHERE T.ORDER_ID = ").append(orderId);
        sql.append(" ORDER BY T.PART_OLDCODE\n");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param orderId
     * @param whId
     * @return
     */
    public List<Map<String, Object>> queryPartDlrOrderDetail4SODTL(String orderId, String whId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();


        sql.append("SELECT T.*,\n");
        sql.append("       CASE\n");
        sql.append("         WHEN T.ORDER_TYPE = 92151004 THEN\n");
        sql.append("          T.BUY_QTY\n");
        sql.append("         WHEN T.ORDER_TYPE != 92151004 THEN\n");
        sql.append("          DECODE(SIGN(T.STOCK_QTY - T.BUY_QTY),\n");
        sql.append("                 1,\n");
        sql.append("                 T.BUY_QTY,\n");
        sql.append("                 DECODE(SIGN(T.STOCK_QTY), 1, T.STOCK_QTY, 0))\n");
        sql.append("       END PER_SALE_QTY\n");
        sql.append("  FROM (\n");

        sql.append("SELECT T.LINE_ID,\n");
        sql.append("       T.ORDER_ID,\n");
        sql.append("       T.LINE_NO,\n");
        sql.append("       T.PART_ID,\n");
        sql.append("       T.PART_CODE,\n");
        sql.append("       T.PART_OLDCODE,\n");
        sql.append("       T.PART_CNAME,\n");
        sql.append("       T.UNIT,\n");
        sql.append("       T.IS_DIRECT,\n");
        sql.append("       T.IS_PLAN,\n");
        sql.append("       T.IS_LACK,\n");
        sql.append("       T.IS_REPLACED,\n");
        sql.append("       T.MIN_PACKAGE,\n");
        sql.append("       T.BUY_QTY,\n");
        sql.append("       T.SALES_QTY,\n");
        //重新取销售价格和金额
        //sql.append("       TO_CHAR(T.BUY_PRICE, 'FM999,999,999,999,990.00') AS BUY_PRICE,\n");
        //sql.append("       TO_CHAR(T.BUY_AMOUNT, 'FM999,999,999,999,990.00') AS BUY_AMOUNT,\n");
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(M.DEALER_ID, T.PART_ID), 'FM999,999,999,999,990.00') AS BUY_PRICE,\n");
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(M.DEALER_ID, T.PART_ID) * T.BUY_QTY, 'FM999,999,999,999,990.00') AS BUY_AMOUNT,\n");
        sql.append("       T.IS_HAVA,\n");
        sql.append("       T.REMARK,\n");
        sql.append("       T.CREATE_DATE,\n");
        sql.append("       T.CREATE_BY,\n");
        sql.append("       T.UPDATE_DATE,\n");
        sql.append("       T.UPDATE_BY,\n");
        sql.append("       T.DISABLE_DATE,\n");
        sql.append("       NVL((SELECT sum(S.NORMAL_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = T.PART_ID\n");
        sql.append("              AND S.ORG_ID = ").append(dealerId);
        sql.append("              AND S.WH_ID =").append(whId);
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS STOCK_QTY,\n");
        sql.append("       T.DISABLE_BY,\n");
        sql.append("       T.DELETE_DATE,\n");
        sql.append("       T.DELETE_BY,\n");
        sql.append("       T.RESERVED_DATE,\n");
        sql.append("       T.VER,\n");
        sql.append("       T.STATUS,\n");
        sql.append("       NVL(C.SALE_PRICE1, '0') SALE_PRICE1,\n");
        sql.append("       B.IS_GIFT,\n");
        sql.append("       T.DEFT_ID，\n");
        sql.append("      M.ORDER_TYPE\n");
        sql.append("  FROM TT_PART_DLR_ORDER_DTL T\n");
        //关联主表 add by yuan start
        sql.append("JOIN TT_PART_DLR_ORDER_MAIN M\n");
        sql.append("   ON M.ORDER_ID = T.ORDER_ID\n");
        //end
        sql.append("  LEFT JOIN TT_PART_DEFINE B\n");
        sql.append("    ON T.PART_ID = B.PART_ID\n");
        sql.append("  LEFT JOIN TT_PART_SALES_PRICE C\n");
        sql.append("    ON C.PART_ID = T.PART_ID\n");
        sql.append("   AND C.STATE = '10011001'\n");
        sql.append(" WHERE T.ORDER_ID = ").append(orderId);
        sql.append(" ORDER BY T.PART_OLDCODE \n");
        sql.append("  ) t \n");
        logger.info("--------sql=" + sql);

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
     * @Description: 查询订单操作记录
     */
    public List<Map<String, Object>> queryOrderHistory(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_OPERATION_HISTORY ");
        sql.append(" where 1=1 ");
        //sql.append(" and OPT_TYPE='").append(Constant.PART_OPERATION_TYPE_01).append("'");
        sql.append(" and ORDER_ID='").append(orderId).append("'");
        sql.append(" order by OPT_DATE DESC ");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 查询导出数据
     */
    public List<Map<String, Object>> queryExportPartDlrOrder(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String sCreateDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SSUBMIT_DATE"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("ESUBMIT_DATE"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        sql.append(" select ");
        sql.append(" ORDER_ID,REBUT_REASON,ORDER_CODE,DEALER_CODE,DEALER_NAME,BUYER_NAME,to_char(CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') as CREATE_DATE,SELLER_NAME,ORDER_AMOUNT, ");
        sql.append(" (select CODE_DESC FROM TC_CODE b WHERE b.CODE_ID=a.ORDER_TYPE) as ORDER_TYPE, ");
        sql.append(" to_char(SUBMIT_DATE,'yyyy-mm-dd hh24:mi:ss') as SUBMIT_DATE, (select CODE_DESC FROM TC_CODE b WHERE b.CODE_ID=a.STATE) AS STATE");
        sql.append(" from tt_part_dlr_order_main  a");
        sql.append(" where 1=1 ");
        if (!"".equals(orderCode)) {
            sql.append(" and ORDER_CODE='").append(orderCode).append("'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and SELLER_NAME like '%").append(sellerName).append("%'");
        }
        //前台已经加限制  必须要有时间范围  所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and CREATE_DATE>= to_date('").append(sCreateDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and CREATE_DATE<= to_date('").append(eCreateDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and ORDER_TYPE='").append(orderType).append("'");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and SUBMIT_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and SUBMIT_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and STATE='").append(state).append("'");
        }
        sql.append(" and oem_flag='").append(Constant.PART_BASE_FLAG_NO + "").append("'");
        sql.append(" and dealer_id='").append(dealerId).append("'");
        sql.append(" order by a.create_date desc");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
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
     * @Description: 查询导出数据
     */
    public List<Map<String, Object>> queryOemExportPartDlrOrder(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String sCreateDate = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SSUBMIT_DATE"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("ESUBMIT_DATE"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        sql.append(" select ");
        sql.append(" ORDER_ID,ORDER_CODE,DEALER_CODE,DEALER_NAME,BUYER_NAME,CREATE_DATE,SELLER_NAME,ORDER_AMOUNT, ");
        sql.append(" (select CODE_DESC FROM TC_CODE b WHERE b.CODE_ID=a.ORDER_TYPE) as ORDER_TYPE, ");
        sql.append(" SUBMIT_DATE, (select CODE_DESC FROM TC_CODE b WHERE b.CODE_ID=a.STATE) AS STATE");
        sql.append(" from tt_part_dlr_order_main  a");
        sql.append(" where 1=1 ");
        if (!"".equals(orderCode)) {
            sql.append(" and ORDER_CODE='").append(orderCode).append("'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and SELLER_NAME like '%").append(sellerName).append("%'");
        }
        //前台已经加限制  必须要有时间范围  所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and CREATE_DATE>= to_date('").append(sCreateDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and CREATE_DATE<= to_date('").append(eCreateDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and ORDER_TYPE='").append(orderType).append("'");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and SUBMIT_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and SUBMIT_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and STATE='").append(state).append("'");
        }
        sql.append(" and oem_flag='").append(Constant.PART_BASE_FLAG_YES + "").append("'");
        sql.append(" and seller_id='").append(dealerId).append("'");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
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
     * @Description: 获取版本号
     */
    public String getVersion(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select VER from TT_PART_DLR_ORDER_MAIN where ORDER_ID='");
        sql.append(orderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return "";
        }
        return list.get(0).get("VER").toString();
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 校验是否被提交
     */
    public boolean validateRep(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_DLR_ORDER_MAIN ");
        sql.append(" where 1=1 ");
        sql.append(" and ORDER_ID='").append(orderId).append("'");
        sql.append(" and state='").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return false;
        }
        return true;
    }

    /**
     * @param dealerId 子机构ID
     * @param sellerId 父机构ID
     * @return
     */
    public Map<String, Object> getAccountMoney(String dealerId, String sellerId, String accoutPurpose) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT R.PARENTORG_ID,\n");
        sql.append("       R.PARENTORG_NAME,\n");
        sql.append("       R.PARENTORG_CODE,\n");
        sql.append("       NVL(B.ACCOUNT_ID, '') AS ACCOUNT_ID,\n");
        sql.append("       TO_CHAR(NVL(ACCOUNT_SUM, '0'), 'FM999,999,999,999,990.00') AS ACCOUNT_SUM,\n");
        sql.append("       TO_CHAR(NVL(ACCOUNT_DJ, '0'), 'FM999,999,999,999,990.00') AS ACCOUNT_DJ,\n");
        sql.append("       TO_CHAR(NVL(ACCOUNT_KY, '0'), 'FM999,999,999,999,990.00') AS ACCOUNT_KY,\n");
        sql.append("       B.ACCOUNT_PURPOSE\n");
        sql.append("  FROM TT_PART_SALES_RELATION R\n");
        sql.append(" left join VW_PART_DLR_ACCOUNT b on b.DEALER_ID='").append(dealerId).append("' and r.PARENTORG_ID=b.PARENTORG_ID ");
        sql.append(" WHERE r.childorg_id=").append(dealerId);
        sql.append("  AND r.PARENTORG_ID=").append(sellerId);
        if (!"".equals(accoutPurpose) && null != accoutPurpose) {
            sql.append("  AND B.ACCOUNT_PURPOSE = " + accoutPurpose + "\n");
        } else {
            sql.append("  AND B.ACCOUNT_PURPOSE = 95631001\n");
        }

        sql.append("  AND r.state=").append(Constant.STATUS_ENABLE).append(" AND r.status=1 ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getAccountRecord(String orderId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from tt_part_account_record where source_id='");
        sql.append(orderId).append("' ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 获取上级机构ID
     */
    public Map<String, Object> getParentOrgId(String childOrgId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_sales_relation  ");
        sql.append(" where 1=1 ");
        sql.append(" and childorg_id='").append(childOrgId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0)) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public Map<String, Object> getOem(String code, String id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tm_company ");
        sql.append(" where 1=1 ");
        if (!"".equals(id)) {
            sql.append(" and company_id='").append(id).append("'");
        }
        if (!"".equals(code)) {
            sql.append(" and company_code='").append(code).append("'");
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0)) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getReplacePart(String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_replaced_define where part_id ＝'");
        sql.append(partId);
        sql.append("' and state='").append(Constant.STATUS_ENABLE).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public Map<String, Object> getSoMainMap(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_main where order_id='").append(orderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0)) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getBrand() {
        StringBuffer sql = new StringBuffer();
        sql.append(" select DISTINCT BRAND, CRITERION, SORT from tt_part_STO_DEFINE order by sort ");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getSoMain(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_main where order_id='").append(orderId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public Long getSoDetailPartNum(String soIds, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(sum(SALES_QTY),'0') qty  from tt_part_so_dTL where so_id in (").append(soIds).append(")");
        sql.append("  and part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("QTY") == null) {
            return 0L;
        }
        return Long.valueOf(CommonUtils.checkNull(list.get(0).get("QTY")));
    }

    public String getDeftId(String partId, String brand) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_sto_define  where BRAND='").append(brand).append("'");
        sql.append(" and part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("DEFT_ID") == null) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("DEFT_ID"));
    }

    public List<Map<String, Object>> getStoVender(String deftId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_sto_define where deft_id='").append(deftId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getReplacePartInfo(String partId, String dealerId, String sellerId) {
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
        sql.append("       NVL(A.UNIT, '件') UNIT， NVL((SELECT SUM(S.ITEM_QTY)\n");
        sql.append("                                     FROM TT_PART_ITEM_STOCK S\n");
        sql.append("                                    WHERE S.PART_ID = A.PART_ID\n");
        sql.append("                                      AND S.ORG_ID = '").append(sellerId).append("'");
        sql.append("                                      AND S.STATE = 1\n");
        sql.append("                                      AND S.STATUS = 1), 0) AS ITEM_QTY,\n");
        sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
        sql.append("                    FROM TM_DEALER D\n");
        sql.append("                   WHERE D.DEALER_ID = ").append(sellerId);
        sql.append("                     AND D.STATUS = 10011001),\n");
        sql.append("                  92101001,\n");
        sql.append("                  A.MIN_PACK1,\n");
        sql.append("                  A.MIN_PACK2),\n");
        sql.append("           0) AS MIN_PACKAGE,\n");
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(").append(sellerId).append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
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
        sql.append("         AND E.ORG_ID = ").append(dealerId).append(") AS UPORGSTOCK\n");
        sql.append("  FROM TT_PART_DEFINE A\n");
        sql.append(" WHERE /*A.STATE = 10011001\n");
        sql.append("   AND*/\n");
        sql.append(" A.STATUS = 1\n");
        sql.append(" and part_id in (").append(partId).append(")");
        //sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public String getFreight(String dealerId, String orderType, String orderAmount) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" to_char(nvl(PKG_PART.F_GETTRNASEXP('" + dealerId + "','" + orderType + "','" + orderAmount + "'),'0'),'FM999,999,999,999,990.00') as freight ");
        sql.append(" from dual ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("FREIGHT") == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("FREIGHT"));
    }

    public List<Map<String, Object>> getAddr(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  nvl(a.ADDR_ID,'') as ADDR_ID,nvl(a.ADDR,'') as ADDR,nvl(a.LINKMAN,'') as LINKMAN,nvl(TEL,'') as TEL,nvl(a.LINKMAN2,'') as LINKMAN2,nvl(TEL2,'') as TEL2,nvl(a.POST_CODE,'') as POST_CODE,nvl(a.STATION,'') as STATION  from TT_PART_ADDR_DEFINE a where dealer_id='").append(dealerId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getSto(String brand, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_sto_define  where BRAND='").append(brand).append("'");
        sql.append(" and part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public PageResult<Map<String, Object>> getGift(RequestWrapper request, int curPage) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" distinct part_id ");
        sql.append(" from Tt_Part_Gift_Define  a");
        sql.append(" where state='").append(Constant.STATUS_ENABLE).append("' and status='1' ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), Constant.PAGE_SIZE, curPage);
        return ps;
    }

    public Map<String, Object> getPartDefine(String partId, String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select part_cname,part_oldcode,part_code,nvl(unit,'件') as unit ,");
        sql.append(" NVL(DECODE((SELECT D.PDEALER_TYPE\n");
        sql.append("  FROM TM_DEALER D\n");
        sql.append("  WHERE D.DEALER_ID = ").append(dealerId);
        sql.append("  AND D.STATUS = 10011001),\n");
        sql.append("  92101001,\n");
        sql.append("  A.MIN_PACK1,\n");
        sql.append("  A.MIN_PACK2),\n");
        sql.append("  0) AS MIN_PACKAGE\n");
        sql.append(" from tt_part_define A where  part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0)) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public Map<String, Object> getPartDefine(String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" A.* ");
        sql.append(" from tt_part_define A where  part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0)) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public Double getBuyPrice(String dealerId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" TO_CHAR(PKG_PART.F_GETPRICE('").append(dealerId).append("','").append(partId).append("'）,'FM999,999,990.00') AS PRICE ");
        sql.append(" from dual ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0) || list.get(0).get("PRICE") == null) {
            return 0D;
        }
        return Double.valueOf(CommonUtils.checkNull(list.get(0).get("PRICE")));
    }

    public Double getOrderPrice(String orderId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append("  buy_price  ");
        sql.append(" from tt_part_dlr_order_dtl where order_id='").append(orderId).append("' and part_id ='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0) || list.get(0).get("BUY_PRICE") == null) {
            return 0D;
        }
        return Double.valueOf(CommonUtils.checkNull(list.get(0).get("BUY_PRICE")));
    }

    public List<Map<String, Object>> getWareHouse(String orgId, String proFac) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT Distinct d.wh_id,d.wh_name\n");
        sbSql.append("  FROM TT_PART_PLANER_WH_RELATION R, TT_PART_WAREHOUSE_DEFINE D\n");
        sbSql.append(" WHERE R.WH_ID = D.WH_ID\n");
        sbSql.append("    AND D.org_Id = " + orgId);
        sbSql.append("    AND r.state =" + Constant.STATUS_ENABLE);
        sbSql.append(" and D.PRODUCE_FAC='").append(proFac).append("'");
        sbSql.append(" union   ");
        sbSql.append("SELECT Distinct d.wh_id,d.wh_name\n");
        sbSql.append("  FROM TT_PART_WAREHOUSE_DEFINE D\n");
        sbSql.append(" WHERE ");
        sbSql.append("   D.org_Id = " + orgId);
        sbSql.append("    AND D.state =" + Constant.STATUS_ENABLE);
        sbSql.append(" and PRODUCE_FAC='").append(proFac).append("'");
        List<Map<String, Object>> wareHoustList = this.pageQuery(sbSql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    public List<Map<String, Object>> getStoList(String brand) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_sto_define  where BRAND='").append(brand).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public Map<String, Object> getAddrMap(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" nvl(a.LINKMAN2,'') as LINKMAN,nvl(TEL2,'') as TEL");
        sql.append(" from TT_PART_ADDR_DEFINE a");
        sql.append(" where 1=1 ");
        sql.append(" and STATE='").append(Constant.STATUS_ENABLE).append("' AND status='1' ");
        sql.append(" and DEALER_ID='").append(dealerId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0)) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getSaler() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT U.USER_ID, U.NAME\n");
        sql.append("  FROM TC_USER U,\n");
        sql.append("       (SELECT PU.*\n");
        sql.append("          FROM TT_PART_USERPOSE_DEFINE PU\n");
        sql.append("         WHERE PU.STATE = '10011001'\n");
        sql.append("           AND PU.STATUS = 1\n");
        sql.append("           AND (PU.USER_TYPE = '3' OR PU.USER_TYPE = '5' OR PU.USER_TYPE = '6')) PU\n");
        sql.append(" WHERE COMPANY_ID = '" + Constant.OEM_ACTIVITIES + "'\n");
        sql.append("   AND U.USER_ID = PU.USER_ID\n");
        sql.append("   AND U.USER_STATUS = '10011001'\n");
        sql.append(" ORDER BY PU.USER_TYPE, U.ACNT");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;

    }

    public void saveUploadData(RequestWrapper req, String seq) {
        String sellerId = CommonUtils.checkNull(req.getParamValue("SELLER_ID"));
        StringBuffer sql = new StringBuffer();
        String produceFac = CommonUtils.checkNull(req.getParamValue("produceFac"));
        String orderType = CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"));
        req.setAttribute("bookDealerId", CommonUtils.checkNull(req.getParamValue("dealerId")));

        sql.append(" insert into tt_part_sale_upload (");
        sql.append(" select ").append(seq).append(",");
        sql.append(" A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT,\n");
        sql.append("       NVL((SELECT SUM(S.ITEM_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = A.PART_ID\n");
        sql.append("              AND S.ORG_ID = '").append(req.getAttribute("bookDealerId")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES)) { //主机厂按照最小包装量量销售
            sql.append("CEIL(sum(B.QTY) / NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                          FROM TM_DEALER D\n");
            sql.append("                         WHERE D.DEALER_ID =").append(req.getAttribute("bookDealerId"));
            sql.append("                           AND D.STATUS = 10011001),\n");
            sql.append("                        92101001,\n");
            sql.append("                        A.MIN_PACK1,\n");
            sql.append("                        A.MIN_PACK2),\n");
            sql.append("                 0)) *\n");
            sql.append("NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("             FROM TM_DEALER D\n");
            sql.append("            WHERE D.DEALER_ID = ").append(req.getAttribute("bookDealerId"));
            sql.append("              AND D.STATUS = 10011001),\n");
            sql.append("           92101001,\n");
            sql.append("           A.MIN_PACK1,\n");
            sql.append("           A.MIN_PACK2),\n");
            sql.append("    0) AS BUYQTY,\n");
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                    FROM TM_DEALER D\n");
            sql.append("                   WHERE D.DEALER_ID = ").append(req.getAttribute("bookDealerId"));
            sql.append("                     AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("CEIL(sum(B.QTY) / nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1))*nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) AS BUYQTY,\n");
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) as MIN_PACKAGE,\n");//供应中心油品整箱，其他1
        }
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
        sql.append("       (SELECT CASE\n");
        sql.append("                 WHEN SUM(ITEM_QTY) > 0 THEN\n");
        sql.append("                  'Y'\n");
        sql.append("                 WHEN SUM(ITEM_QTY) = 0 THEN\n");
        sql.append("                  'N'\n");
        sql.append("                 ELSE\n");
        sql.append("                  'N'\n");
        sql.append("               END\n");
        sql.append("          FROM VW_PART_STOCK E\n");
        sql.append("         WHERE E.PART_ID = A.PART_ID\n");
        sql.append("           AND E.STATE = 10011001\n");
        sql.append("           AND E.STATUS = 1\n");
        sql.append("           AND E.ORG_ID = ").append(sellerId).append(") AS UPORGSTOCK,\n");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("            WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("              AND ORG_ID ='").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append("'),'0') as qty\n");
        sql.append(" ,sysdate, B.REMARK ");
        sql.append("  FROM TT_PART_DEFINE A\n");
        sql.append(" join Tmp_Part_Upload B on a.part_oldcode=upper(trim(b.part_oldcode))  ");
        sql.append(" WHERE A.STATUS = 1\n");
        sql.append(" and  A.produce_Fac='").append(produceFac).append("'"); //mob by 20140215
        //广宣订单
        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "").equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append(" and A.IS_GXP = " + Constant.IF_TYPE_YES);
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                sql.append(" and A.IS_SPECIAL ='").append(Constant.IF_TYPE_YES).append("'");
            }
           /* if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                sql.append(" and A.part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='").append(CommonUtils.checkNull(req.getParamValue("brand"))).append("')");
            }*/
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%").append(req.getParamValue("PART_CODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%").append(req.getParamValue("PART_OLDCODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%").append(req.getParamValue("PART_CNAME").toUpperCase()).append("%'");
        }
        //广宣不需要以下验证
        if (!(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "").equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append("AND NOT EXISTS (SELECT 1\n");
            sql.append("   FROM VW_PART_STOCK S\n");
            sql.append("  WHERE S.PDSTATE = 10011002\n");
            sql.append("    AND S.ORG_ID = " + sellerId + "\n");
            sql.append("    AND S.PART_ID = A.PART_ID\n");
            sql.append("    HAVING  SUM(S.NORMAL_QTY) = 0)\n");
        }
        //常规订单导入限制配件类型为非直发、非特殊、非广宣品
        if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 + "") || orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02 + "")) {
            sql.append("   AND A.IS_DIRECT = " + Constant.IF_TYPE_NO + "\n");
            sql.append("   AND A.IS_SPECIAL = " + Constant.IF_TYPE_NO + "\n");
            sql.append("   AND A.IS_GXP = " + Constant.IF_TYPE_NO + "");

        } else if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03 + "")) {//特殊订单限制为特殊类型
            sql.append("   AND A.IS_SPECIAL = " + Constant.IF_TYPE_YES + "\n");
        } else if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {//直发订单限制为直发类型
            sql.append("   AND A.IS_DIRECT = " + Constant.IF_TYPE_YES + "\n");
        } else if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")) {//广宣订单限制为广宣品
            sql.append("   AND A.IS_GXP = " + Constant.IF_TYPE_YES + "\n");
        }
        sql.append("GROUP BY A.PART_ID,\n");
        sql.append("         A.IS_REPLACED,\n");
        sql.append("         A.IS_LACK,\n");
        sql.append("         A.PART_CODE,\n");
        sql.append("         A.IS_DIRECT,\n");
        sql.append("         A.IS_PLAN,\n");
        sql.append("         A.PART_OLDCODE,\n");
        sql.append("         A.PART_CNAME,\n");
        sql.append("         A.STATE,\n");
        sql.append("         A.UNIT,\n");
        sql.append("         A.MIN_PACK1,\n");
        sql.append("         A.MIN_PACK2,\n");
        sql.append("         A.PART_MATERIAL,\n");
        sql.append("         B.REMARK\n");

        sql.append(")");
        this.update(sql.toString(), null);

    }

    public PageResult<Map<String, Object>> showPartBaseForUpload(RequestWrapper req, String sellerId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String produceFac = CommonUtils.checkNull(req.getParamValue("produceFac"));//品牌配件

        sql.append("SELECT A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT,\n");
        sql.append("       NVL((SELECT SUM(S.ITEM_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = A.PART_ID\n");
        sql.append("              AND S.ORG_ID = '").append(req.getAttribute("bookDealerId")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        sql.append("           B.QTY BUYQTY,\n");
        sql.append("           B.REMARK,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES)) { //主机厂按照最小包装量量销售
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                    FROM TM_DEALER D\n");
            sql.append("                   WHERE D.DEALER_ID = ").append(req.getAttribute("bookDealerId"));
            sql.append("                     AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) as MIN_PACKAGE,\n");//供应中心油品整箱，其他1
        }
        sql.append("       ROUND(B.QTY*(PKG_PART.F_GETPRICE(").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append(", A.PART_ID)),2) AS  BUY_AMOUNT,");
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
        ;
        sql.append("       (SELECT CASE\n");
        sql.append("                 WHEN SUM(ITEM_QTY) > 0 THEN\n");
        sql.append("                  'Y'\n");
        sql.append("                 WHEN SUM(ITEM_QTY) = 0 THEN\n");
        sql.append("                  'N'\n");
        sql.append("                 ELSE\n");
        sql.append("                  'N'\n");
        sql.append("               END\n");
        sql.append("          FROM VW_PART_STOCK E\n");
        sql.append("         WHERE E.PART_ID = A.PART_ID\n");
        sql.append("           AND E.STATE = 10011001\n");
        sql.append("           AND E.STATUS = 1\n");
        sql.append("           AND E.ORG_ID = ").append(sellerId).append(") AS UPORGSTOCK,\n");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("            WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("              AND ORG_ID ='").append(CommonUtils.checkNull(req.getAttribute("bookDealerId"))).append("'),'0') as qty\n");
        sql.append("  FROM TT_PART_DEFINE A\n");
        sql.append(" join Tmp_Part_Upload B on a.part_oldcode=upper(trim(b.part_oldcode))  ");
        sql.append(" WHERE A.STATUS = 1\n");
        sql.append(" and  A.produce_Fac='").append(produceFac).append("'");
        //广宣品
        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "").equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append(" and A.IS_GXP = " + Constant.IF_TYPE_YES + "\n");

        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                sql.append(" and A.IS_PLAN ='").append(Constant.IF_TYPE_YES).append("'");
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
                sql.append(" and A.part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='").append(CommonUtils.checkNull(req.getParamValue("brand"))).append("')");
                /*sql.append(" and A.IS_DIRECT ='").append(Constant.IF_TYPE_YES).append("'");*/
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%").append(req.getParamValue("PART_CODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%").append(req.getParamValue("PART_OLDCODE").toUpperCase()).append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%").append(req.getParamValue("PART_CNAME").toUpperCase()).append("%'");
        }
        if (!(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "").equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append("AND NOT EXISTS (SELECT 1\n");
            sql.append("          FROM VW_PART_STOCK S\n");
            sql.append("         WHERE S.PDSTATE = 10011002\n");
            sql.append("           AND S.ORG_ID = " + sellerId + "\n");
            sql.append("           AND S.PART_ID = A.PART_ID\n");
            sql.append("           HAVING  SUM(S.NORMAL_QTY) = 0)\n");
        }

        //sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public Map<String, Object> getGift(String partId, String giftWay) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_GIFT_DEFINE ");
        sql.append(" where part_id='").append(partId).append("' and gift_way='").append(giftWay).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getUploadList(String seq) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select d.*,REPLACE(d.sale_price1,',')*d.buyqty buy_Amount from tt_part_sale_upload d where d.ul_id='").append(seq).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public List<Map<String, Object>> getTmpData(String code, String orderType) {
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT * \n");
        sql.append(" FROM TT_PART_DEFINE D \n");
        sql.append(" WHERE D.PART_OLDCODE = '" + code + "' \n");

        //常规订单导入限制配件类型为非直发、非特殊、非广宣品
        if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 + "") || orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02 + "")) {
            sql.append("   AND D.IS_DIRECT = " + Constant.IF_TYPE_NO + "\n");
            sql.append("   AND D.IS_SPECIAL = " + Constant.IF_TYPE_NO + "\n");
            sql.append("   AND D.IS_GXP = " + Constant.IF_TYPE_NO + "");

        } else if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03 + "")) {//特殊订单限制为特殊类型
            sql.append("   AND D.IS_SPECIAL = " + Constant.IF_TYPE_YES + "\n");
        } else if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {//直发订单限制为直发类型
            sql.append("   AND D.IS_DIRECT = " + Constant.IF_TYPE_YES + "\n");
        } else if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")) {//广宣订单限制为广宣品
            sql.append("   AND D.IS_GXP = " + Constant.IF_TYPE_YES + "\n");
        }


        //sql.append(" select 1 from  Tmp_Part_Upload where part_oldcode='").append(code).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public List<Map<String, Object>> checkPartRepeat() {

        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT tmp.part_oldcode, \n");
        sql.append("        COUNT(*) REPEAT_QTY \n");
        sql.append("   FROM Tmp_Part_Upload tmp \n");
        sql.append("  GROUP BY tmp.part_oldcode, \n");
        sql.append("           tmp.part_id, \n");
        sql.append("           tmp.wh_id, \n");
        sql.append("           tmp.org_id \n");
        sql.append(" HAVING COUNT(*) > 1 \n");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;

    }

    public List<Map<String, Object>> getGiftDefine(String partId, String oemStart, String way) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_GIFT_DEFINE ");
        sql.append(" where 1=1 ");
        sql.append(" and (sysdate between start_date and end_date) ");
        if (!"".equals(partId)) {
            sql.append(" and part_id ='").append(partId).append("'");
        }
        sql.append(" and is_oem_start='").append(oemStart).append("'");
        sql.append(" and gift_way='").append(way).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());

    }

    /**
     * @param userId
     * @return
     */
    public String getUserType(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT user_type AS userType FROM tt_part_userpose_define d WHERE  d.user_id=").append(userId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("USERTYPE"));
    }

    public String isTransFree(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT td.is_nbdw FROM tm_dealer td WHERE td.dealer_id=").append(dealerId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("IS_NBDW"));
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询(审核)
     */
    public Map<String, Object> querySumPartDlrOrder(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String sCreateDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("CendDate"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态
        String autoPreCheck = CommonUtils.checkNull(request.getParamValue("autoPreCheck"));//自动预审
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        String planFlag = request.getParamValue("planFlag");
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订货单位

        String dealerId = "";
        String userType = null;

        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

        TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
        userposeDefinePO.setUserId(loginUser.getUserId());
        if (dao.select(userposeDefinePO).size() > 0) {
            userposeDefinePO = (TtPartUserposeDefinePO) dao.select(userposeDefinePO).get(0);
            userType = userposeDefinePO.getUserType() + "";
        }
        ;
        sql.append("SELECT nvl(SUM(A.ORDER_AMOUNT),0) AMOUNT, COUNT(1) XS\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN A\n");
        /*sql.append(" join tt_part_salesscope_define b on a.dealer_id=b.dealer_id and b.user_id='").append(loginUser.getUserId()).append("'");*/
        sql.append(" where 1=1 ");
        if (dealerId.equals(Constant.OEM_ACTIVITIES)) { // add by yuan 2013062 车厂才有限制
            if ("".equals(salerId)) {
            /*   sql.append("  AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
		                "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
		                "           AND B.USER_ID ="+loginUser.getUserId() +")");*/
                if ("3".equals(userType) || "5".equals(userType)) {
                    sql.append("AND EXISTS (SELECT 1\n");
                    sql.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                    sql.append("      WHERE D.ORDER_TYPE = A.ORDER_TYPE\n");
                    sql.append("        AND D.USER_ID = " + loginUser.getUserId() + "\n");
                    sql.append("        AND D.STATE = 10011001\n");
                    sql.append("        AND D.STATUS = 1)\n");
                }
            } else {
                if ("3".equals(userType)) {//配件销售员区域限制
                    sql.append("  AND EXISTS (SELECT 1\n" +
                            "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                            "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
                            "           AND B.USER_ID =" + salerId + ")");
                    sql.append(" AND EXISTS (SELECT 1\n");//配件销售员订单类型限制
                    sql.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                    sql.append("      WHERE D.ORDER_TYPE = A.ORDER_TYPE\n");
                    sql.append("        AND D.USER_ID = " + loginUser.getUserId() + "\n");
                    sql.append("        AND D.STATE = 10011001\n");
                    sql.append("        AND D.STATUS = 1)");
                } else if ("5".equals(userType)) {//配件销售员订单类型限制
                    sql.append("AND EXISTS (SELECT 1\n");
                    sql.append("       FROM TT_PART_USER_ORDERTYPE_DEFINE D\n");
                    sql.append("      WHERE D.ORDER_TYPE = A.ORDER_TYPE\n");
                    sql.append("        AND D.USER_ID = " + salerId + "\n");
                    sql.append("        AND D.STATE = 10011001\n");
                    sql.append("        AND D.STATUS = 1)");
                }

            }
        }
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(a.Dealer_code) like upper('%").append(dealerCode).append("%')");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName).append("%'");
        }
        //前台已经加限制  必须要有时间范围  所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(eCreateDate)) {
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE=").append(orderType);
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(eSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
        sql.append(") and a.seller_id='").append(CommonUtils.checkNull(dealerId)).append("'");
        /*if(getUserType(loginUser.getUserId()).equals("6")){
            sql.append(" AND A.create_by=").append(loginUser.getUserId());
        }*/
        //add by yuan 20140305  过滤广宣品订单和替换件订单
        sql.append(" and a.ORDER_TYPE !=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
        //add by yuan 20140317  过滤替换件订单
        sql.append(" and a.ORDER_TYPE !=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10);
        //end
        sql.append(" order by a.SUBMIT_DATE ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    public PageResult<Map<String, Object>> queryCheckPartDlrGxOrder(
            RequestWrapper request, Integer curPage, Integer pageSize) {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String sCreateDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("CendDate"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态
        String autoPreCheck = CommonUtils.checkNull(request.getParamValue("autoPreCheck"));//自动预审
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        String planFlag = request.getParamValue("planFlag");
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订货单位

        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

        sql.append("SELECT A.ORDER_ID,\n");
        sql.append("       A.ORDER_CODE,\n");
        sql.append("       A.ORDER_TYPE,\n");
        sql.append("       A.PAY_TYPE,\n");
        sql.append("       A.DEALER_ID,\n");
        sql.append("       TD.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       A.SELLER_ID,\n");
        sql.append("       A.SELLER_CODE,\n");
        sql.append("       A.SELLER_NAME,\n");
        sql.append("       A.SELLER_TYPE,\n");
        sql.append("       A.BUYER_ID,\n");
        sql.append("       A.BUYER_NAME,\n");
        sql.append("       A.RCV_ORGID,\n");
        sql.append("       A.RCV_ORG,\n");
        sql.append("       A.ADDR_ID,\n");
        sql.append("       A.ADDR,\n");
        sql.append("       RECEIVER,\n");
        sql.append("       TEL,\n");
        sql.append("       A.POST_CODE,\n");
        sql.append("       A.STATION,\n");
        sql.append("       A.TRANS_TYPE,\n");
        sql.append("       A.ACCOUNT_SUM,\n");
        sql.append("       A.ACCOUNT_KY,\n");
        sql.append("       A.ACCOUNT_DJ,\n");
        sql.append("       TO_CHAR(A.ORDER_AMOUNT, 'FM999,999,999,999,990.00') AS ORDER_AMOUNT,\n");
        sql.append("       A.DISCOUNT,\n");
        sql.append("       A.REMARK,\n");
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
        sql.append("       A.AUTCHK_DATE,\n");
        sql.append("       A.IS_AUTCHK,\n");
        sql.append("       A.VER,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.STATUS,\n");
        sql.append("       A.WH_ID,\n");
        sql.append("       A.WH_CNAME,\n");
        sql.append("       A.AUDIT_OPINION,\n");
        sql.append("       A.REBUT_REASON,\n");
        sql.append("       A.OEM_FLAG\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN A, TM_DEALER TD\n");
        sql.append(" where 1=1 ");
        sql.append("AND A.DEALER_ID = TD.DEALER_ID\n");
        if (dealerId.equals(Constant.OEM_ACTIVITIES)) { // add by yuan 2013062 车厂才有限制
            if ("".equals(salerId)) {
                /*   sql.append("  AND EXISTS (SELECT 1\n" +
                            "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
			                "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
			                "           AND B.USER_ID ="+loginUser.getUserId() +")");*/
            } else {
                sql.append("  AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                        "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
                        "           AND B.USER_ID =" + salerId + ")");
            }
        }
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(td.Dealer_code) like upper('%").append(dealerCode).append("%')");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName).append("%'");
        }
        //前台已经加限制  必须要有时间范围  所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE=").append(orderType);
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
        sql.append(") and a.seller_id='").append(CommonUtils.checkNull(dealerId)).append("'");

        sql.append(" and a.ORDER_TYPE =").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
        //end
        sql.append(" order by a.SUBMIT_DATE,a.ORDER_ID ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;

    }

    public List<Map<String, Object>> queryCheckPartDlrGxOrder(
            RequestWrapper request) {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String sCreateDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request.getParamValue("CendDate"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态
        String autoPreCheck = CommonUtils.checkNull(request.getParamValue("autoPreCheck"));//自动预审
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        String planFlag = request.getParamValue("planFlag");
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订货单位

        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

        sql.append("SELECT A.ORDER_ID,\n");
        sql.append("       A.ORDER_CODE,\n");
        sql.append("       A.ORDER_TYPE,\n");
        sql.append("       A.PAY_TYPE,\n");
        sql.append("       A.DEALER_ID,\n");
        sql.append("       TD.DEALER_CODE,\n");
        sql.append("       A.DEALER_NAME,\n");
        sql.append("       A.SELLER_ID,\n");
        sql.append("       A.SELLER_CODE,\n");
        sql.append("       A.SELLER_NAME,\n");
        sql.append("       A.SELLER_TYPE,\n");
        sql.append("       A.BUYER_ID,\n");
        sql.append("       A.BUYER_NAME,\n");
        sql.append("       A.RCV_ORGID,\n");
        sql.append("       A.RCV_ORG,\n");
        sql.append("       A.ADDR_ID,\n");
        sql.append("       A.ADDR,\n");
        sql.append("       RECEIVER,\n");
        sql.append("       TEL,\n");
        sql.append("       A.POST_CODE,\n");
        sql.append("       A.STATION,\n");
        sql.append("       A.TRANS_TYPE,\n");
        sql.append("       A.ACCOUNT_SUM,\n");
        sql.append("       A.ACCOUNT_KY,\n");
        sql.append("       A.ACCOUNT_DJ,\n");
        sql.append("       TO_CHAR(A.ORDER_AMOUNT, 'FM999,999,999,999,990.00') AS ORDER_AMOUNT,\n");
        sql.append("       A.DISCOUNT,\n");
        sql.append("       A.REMARK,\n");
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
        sql.append("       A.AUTCHK_DATE,\n");
        sql.append("       A.IS_AUTCHK,\n");
        sql.append("       A.VER,\n");
        sql.append("       TC.CODE_DESC STATE,\n");
        sql.append("       A.STATUS,\n");
        sql.append("       A.WH_ID,\n");
        sql.append("       A.WH_CNAME,\n");
        sql.append("       A.AUDIT_OPINION,\n");
        sql.append("       A.REBUT_REASON,\n");
        sql.append("       A.OEM_FLAG\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN A, TM_DEALER TD, TC_CODE TC\n");
        sql.append(" where 1=1 ");
        sql.append("AND A.DEALER_ID = TD.DEALER_ID\n");
        sql.append(" AND A.STATE = TC.CODE_ID\n");
        if (dealerId.equals(Constant.OEM_ACTIVITIES)) { // add by yuan 2013062 车厂才有限制
            if ("".equals(salerId)) {
            /*   sql.append("  AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
		                "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
		                "           AND B.USER_ID ="+loginUser.getUserId() +")");*/
            } else {
                sql.append("  AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
                        "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
                        "           AND B.USER_ID =" + salerId + ")");
            }
        }
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(td.Dealer_code) like upper('%").append(dealerCode).append("%')");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName).append("%'");
        }
        //前台已经加限制  必须要有时间范围  所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE=").append(orderType);
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
        sql.append(") and a.seller_id='").append(CommonUtils.checkNull(dealerId)).append("'");

        sql.append(" and a.ORDER_TYPE =").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
        //end
        sql.append(" order by a.SUBMIT_DATE,a.ORDER_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;

    }

    public Map<String, Object> getReportedQty(String orderId, String partId) {
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT NVL(SUM(T2.REPORT_QTY), 0) REPORTED_QTY \n");
        sql.append("   FROM TT_PART_DPLAN_MAIN T1, TT_PART_DPLAN_DTL T2 \n");
        sql.append("  WHERE T1.PLAN_ID = T2.PLAN_ID \n");
        sql.append("    AND T1.ORDER_ID = ").append(orderId);
        sql.append("    AND T2.PART_ID = ").append(partId);

        return pageQueryMap(sql.toString(), null, getFunName());
    }

    public Long getReportedQty(String orderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT ");
        sql.append("        NVL(SUM(T2.REPORT_QTY), 0) REPORT_QTY \n");
        sql.append("   FROM TT_PART_DPLAN_MAIN T1, TT_PART_DPLAN_DTL T2 \n");
        sql.append("  WHERE T1.PLAN_ID = T2.PLAN_ID \n");
        sql.append("    AND T1.ORDER_ID = ").append(orderId);

        map = pageQueryMap(sql.toString(), null, getFunName());

        Long reportQty = ((BigDecimal) map.get("REPORT_QTY")).longValue();

        return reportQty;

    }

    public PageResult<Map<String, Object>> queryPartGxPlan(
            RequestWrapper request, Integer curPage, Integer pageSize) {

        String planCode = CommonUtils.checkNull(request.getParamValue("planCode"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));
        String outType = CommonUtils.checkNull(request.getParamValue("OUT_TYPE"));
        String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
        String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
        String state = CommonUtils.checkNull(request.getParamValue("STATE"));

        StringBuffer sql = new StringBuffer();
        sql.append("  SELECT T1.PLAN_ID, \n");
        sql.append("            T1.ORDER_ID, \n");
        sql.append("            T1.PLAN_CODE, \n");
        sql.append("            T1.DEALER_NAME, \n");
        sql.append("            U.NAME, \n");
        sql.append("            T1.CREATE_DATE, \n");
        sql.append("            W.WH_NAME, \n");
        sql.append("            T1.OUT_TYPE, \n");
        sql.append("            T1.STATE \n");
        sql.append("       FROM TT_PART_DPLAN_MAIN T1, TC_USER U, TT_PART_WAREHOUSE_DEFINE W \n");
        sql.append("      WHERE T1.CREATE_BY = U.USER_ID \n");
        sql.append("        AND T1.WH_ID = W.WH_ID \n");

        if (!"".equals(planCode)) {
            sql.append(" AND T1.PLAN_CODE LIKE '%").append(planCode).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" AND T1.DEALER_NAME LIKE '%").append(dealerName).append("%'");
        }
        if (!"".equals(whId)) {
            sql.append(" AND T1.WH_ID = ").append(whId);
        }
        if (!"".equals(outType)) {
            sql.append(" AND T1.OUT_TYPE = ").append(outType);
        }
        if (!"".equals(startDate)) {
            sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(startDate).append("','yyyy-MM-dd')");
        }

        if (!"".equals(endDate)) {
            sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(endDate).append("','yyyy-MM-dd')");
        }
        if (!"".equals(state)) {
            sql.append(" AND T1.STATE = ").append(state);
        }
        sql.append(" ORDER BY T1.CREATE_DATE,T1.PLAN_ID");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;

    }

    public Long getAllBuyQty(Long orderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT ");
        sql.append("        NVL(SUM(DT.BUY_QTY), 0) BUY_QTY \n");
        sql.append("   FROM TT_PART_DLR_ORDER_DTL DT \n");
        sql.append("    WHERE  DT.ORDER_ID = ").append(orderId);

        map = pageQueryMap(sql.toString(), null, getFunName());

        Long buyQty = ((BigDecimal) map.get("BUY_QTY")).longValue();

        return buyQty;
    }

    public PageResult<Map<String, Object>> queryPartGxCar(
            RequestWrapper request, Integer curPage, Integer pageSize) {

        String planCode = CommonUtils.checkNull(request.getParamValue("planCode"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));
        String outType = CommonUtils.checkNull(request.getParamValue("OUT_TYPE"));

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T1.PLAN_ID, \n");
        sql.append("        T1.PLAN_CODE, \n");
        sql.append("        T1.DEALER_NAME, \n");
        sql.append("        T1.OUT_TYPE, \n");
        sql.append("        W.WH_NAME, \n");
        sql.append("        (SELECT ZA_CONCAT(T2.PKG_NO) \n");
        sql.append("           FROM TT_PART_DPLAN_DTL T2 \n");
        sql.append("          WHERE T2.PLAN_ID = T1.PLAN_ID \n");
        sql.append("            AND T2.VIN IS NOT NULL) PKG_NO1, \n");
        sql.append("        (SELECT ZA_CONCAT(T2.PKG_NO) \n");
        sql.append("           FROM TT_PART_DPLAN_DTL T2 \n");
        sql.append("          WHERE T2.PLAN_ID = T1.PLAN_ID \n");
        sql.append("            AND T2.VIN IS NULL) PKG_NO2 \n");
        sql.append("   FROM TT_PART_DPLAN_MAIN T1, TT_PART_WAREHOUSE_DEFINE W \n");
        sql.append("  WHERE T1.WH_ID = W.WH_ID \n");
        sql.append("    AND T1.STATE = ").append(Constant.PART_GX_ORDER_STATE_04);
        sql.append("    AND T1.OUT_TYPE = ").append(Constant.PART_GX_ORDER_OUT_TYPE_01);


        if (!"".equals(planCode)) {
            sql.append(" AND T1.PLAN_CODE LIKE '%").append(planCode).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" AND T1.DEALER_NAME LIKE '%").append(dealerName).append("%'");
        }
        if (!"".equals(whId)) {
            sql.append(" AND T1.WH_ID = ").append(whId);
        }
        if (!"".equals(outType)) {
            sql.append(" AND T1.OUT_TYPE = ").append(outType);
        }

        sql.append(" ORDER BY T1.PLAN_CODE");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;

    }

    public List<Map<String, Object>> queryPartGxZCar(RequestWrapper request) {

        String planCode = CommonUtils.checkNull(request.getParamValue("planCode"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));
        String outType = CommonUtils.checkNull(request.getParamValue("OUT_TYPE"));

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T1.PLAN_ID, \n");
        sql.append("        T1.PLAN_CODE, \n");
        sql.append("        T1.DEALER_NAME, \n");
        sql.append("        TC.CODE_DESC OUT_TYPE, \n");
        sql.append("        W.WH_NAME, \n");
        sql.append("        (SELECT ZA_CONCAT(T2.PKG_NO) \n");
        sql.append("           FROM TT_PART_DPLAN_DTL T2 \n");
        sql.append("          WHERE T2.PLAN_ID = T1.PLAN_ID \n");
        sql.append("            AND T2.VIN IS NOT NULL) PKG_NO1, \n");
        sql.append("        (SELECT ZA_CONCAT(T2.PKG_NO) \n");
        sql.append("           FROM TT_PART_DPLAN_DTL T2 \n");
        sql.append("          WHERE T2.PLAN_ID = T1.PLAN_ID \n");
        sql.append("            AND T2.VIN IS NULL) PKG_NO2 \n");
        sql.append("   FROM TT_PART_DPLAN_MAIN T1, TT_PART_WAREHOUSE_DEFINE W, TC_CODE TC \n");
        sql.append("  WHERE T1.WH_ID = W.WH_ID AND T1.OUT_TYPE = TC.CODE_ID \n");
        sql.append("    AND T1.STATE = ").append(Constant.PART_GX_ORDER_STATE_04);
        sql.append("    AND T1.OUT_TYPE = ").append(Constant.PART_GX_ORDER_OUT_TYPE_01);


        if (!"".equals(planCode)) {
            sql.append(" AND T1.PLAN_CODE LIKE '%").append(planCode).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" AND T1.DEALER_NAME LIKE '%").append(dealerName).append("%'");
        }
        if (!"".equals(whId)) {
            sql.append(" AND T1.WH_ID = ").append(whId);
        }
        if (!"".equals(outType)) {
            sql.append(" AND T1.OUT_TYPE = ").append(outType);
        }

        sql.append(" ORDER BY T1.PLAN_CODE");

        return pageQuery(sql.toString(), null, getFunName());
    }

    public PageResult<Map<String, Object>> queryPartBo(RequestWrapper request,
                                                       Integer curPage, Integer pageSize) throws Exception {
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String partOldCOde = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT T2.PART_CODE, \n");
            sql.append("        T2.PART_OLDCODE, \n");
            sql.append("        T2.PART_CNAME, \n");
            sql.append("        T2.BUY_QTY, \n");
            sql.append("        T2.SALES_QTY, \n");
            sql.append("        T2.BO_QTY \n");
            sql.append("   FROM TT_PART_BO_MAIN T1, TT_PART_BO_DTL T2 \n");
            sql.append("  WHERE T1.BO_ID = T2.BO_ID \n");
            sql.append("    AND T1.ORDER_ID = ").append(orderId);

            if (!"".equals(partOldCOde)) {
                sql.append(" AND UPPER(T2.PART_OLDCODE) LIKE '%").append(partOldCOde.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T2.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T2.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            sql.append(" ORDER BY T2.PART_OLDCODE");
            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;

        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartGxTransTypeAdjust(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        try {
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String pkgNo = CommonUtils.checkNull(request.getParamValue("PKG_NO"));
            //String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));
            String cstartDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));
            String cendDate = CommonUtils.checkNull(request.getParamValue("CendDate"));
            String isTrans = CommonUtils.checkNull(request.getParamValue("ISTRANS"));
            //String transportOrg = CommonUtils.checkNull(request.getParamValue("transportOrg"));

            StringBuffer sql = new StringBuffer();

            sql.append(" SELECT DISTINCT DM.PLAN_ID, \n");
            sql.append("                 DM.PLAN_CODE, \n");
            sql.append("                 DM.DEALER_CODE, \n");
            sql.append("                 DM.DEALER_NAME, \n");
            sql.append("                 DD.PKG_NO, \n");
            sql.append("                 DD.ASS_NO, \n");
            sql.append("                 T.FIX_NAME TRANS_ORG, \n");
            sql.append("                 DD.TRANS_TYPE,dd.wl_no,dd.wl_date, \n");
            sql.append("                 TO_CHAR(DD.CREATE_DATE, 'yyyy-mm-dd') CREATE_DATE, \n");
            sql.append("                 TO_CHAR(DM.FINAL_DATE, 'yyyy-mm-dd') FINAL_DATE \n");
            sql.append("   FROM TT_PART_DPLAN_DTL DD, \n");
            sql.append("        TT_PART_DPLAN_MAIN DM, \n");
            sql.append("        (SELECT FX.FIX_NAME, FX.FIX_VALUE \n");
            sql.append("           FROM TT_PART_FIXCODE_DEFINE FX \n");
            sql.append("          WHERE FX.FIX_GOUPTYPE = ").append(Constant.FIXCODE_TYPE_08).append(") T \n");
            sql.append("  WHERE DD.PLAN_ID = DM.PLAN_ID \n");
            sql.append("    AND DD.TRANS_ORG = T.FIX_VALUE(+) \n");

            sql.append("    AND DM.STATE <> ").append(Constant.PART_GX_ORDER_STATE_01);

            if (!"".equals(dealerCode)) {
                sql.append(" AND DM.DEALER_CODE LIKE '%").append(dealerCode).append("%'");
            }
            if (!"".equals(dealerName)) {
                sql.append(" AND DM.DEALER_NAME LIKE '%").append(dealerName).append("%'");
            }
            if (!"".equals(pkgNo)) {
                sql.append(" AND DD.PKG_NO LIKE '%").append(pkgNo).append("%'");
            }
            //if (!"".equals(transType)) {
            //sql.append(" AND DD.TRANS_TYPE = ").append(transType);
            //}
            //if (!"".equals(transportOrg)) {
            //sql.append(" AND T.FIX_VALUE = ").append(transportOrg);
            //}

            if (!"".equals(cstartDate)) {
                sql.append(" AND TO_DATE(DM.FINAL_DATE)>=").append("TO_DATE('").append(cstartDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(cendDate)) {
                sql.append(" AND TO_DATE(DM.FINAL_DATE)<=").append("TO_DATE('").append(cendDate).append("','yyyy-MM-dd')");
            }

            if ("10041001".equals(isTrans)) {
                sql.append(" AND DD.ASS_NO IS NOT NULL");
            }
            if ("10041002".equals(isTrans)) {
                sql.append(" AND DD.ASS_NO IS NULL");
            }

            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;

        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> getGxPlanOrderInfo(String planCode, String pkgNo) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append(" SELECT DISTINCT DM.PLAN_CODE, \n");
            sql.append("                 DM.PLAN_ID, \n");
            sql.append("                 DM.DEALER_CODE, \n");
            sql.append("                 DM.DEALER_NAME, \n");
            sql.append("                 DD.PKG_NO, \n");
            sql.append("                 DD.TRANS_TYPE, \n");
            sql.append("                 DD.TRANS_ORG, \n");
            sql.append("                 TO_CHAR(DD.CREATE_DATE, 'yyyy-mm-dd') CREATE_DATE, \n");
            sql.append("                 TO_CHAR(DM.FINAL_DATE, 'yyyy-mm-dd') FINAL_DATE \n");
            sql.append("   FROM TT_PART_DPLAN_DTL DD, TT_PART_DPLAN_MAIN DM \n");
            sql.append("  WHERE DD.PLAN_ID = DM.PLAN_ID \n");
            sql.append(" AND DM.PLAN_CODE = '").append(planCode).append("'");
            sql.append(" AND DD.PKG_NO = '").append(pkgNo).append("'");

            return pageQueryMap(sql.toString(), null, getFunName());

        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartGxDtl(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        try {
            String planCode = CommonUtils.checkNull(request.getParamValue("planCode"));
            String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));
            String partOldCOde = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT DD.PART_CODE, \n");
            sql.append("        DD.PART_OLDCODE, \n");
            sql.append("        DD.PART_CNAME, \n");
            sql.append("        DD.REPORT_QTY, \n");
            sql.append("        DD.PKG_NO, \n");
            sql.append("        DD.REMARK \n");
            sql.append("   FROM TT_PART_DPLAN_DTL DD, TT_PART_DPLAN_MAIN DM \n");
            sql.append("  WHERE DD.PLAN_ID = DM.PLAN_ID \n");
            sql.append(" AND DM.PLAN_CODE = '").append(planCode).append("'");
            sql.append(" AND DD.PKG_NO = '").append(pkgNo).append("'");


            if (!"".equals(partOldCOde)) {
                sql.append(" AND UPPER(DD.PART_OLDCODE) LIKE '%").append(partOldCOde.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(DD.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND DD.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            sql.append(" ORDER BY DD.PART_OLDCODE");
            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;

        } catch (Exception e) {
            throw e;
        }
    }

    public void updatePartGxTransType(String planId, String pkgNo, String transType, String transOrg, String wl_NO) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("    UPDATE TT_PART_DPLAN_DTL DD \n");
            sql.append("       SET DD.TRANS_TYPE =").append(transType);
            sql.append(", DD.TRANS_ORG = ").append(transOrg);
            sql.append(", DD.wl_no = ").append(wl_NO);
            sql.append(", DD.wl_date = sysdate");
            sql.append("     WHERE DD.PLAN_ID = ").append(planId);
            sql.append("       AND DD.PKG_NO = '").append(pkgNo).append("'");

            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartDlrOrderEX(RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        StringBuffer sql = new StringBuffer();
        String inId = CommonUtils.checkNull(request.getParamValue("IN_ID"));//订单号

        sql.append("SELECT M.TRANS_CODE,\n");
        sql.append("       D.PART_OLDCODE,\n");
        sql.append("       D.PART_CNAME,\n");
        sql.append("       PD.TRANS_QTY,\n");
        sql.append("       EL.EXCEPTION_NUM,\n");
        sql.append("       EL.EXCEPTION_REMARK\n");
        sql.append("  FROM TT_PART_INSTOCK_EXCEPTION_LOG EL,\n");
        sql.append("       TT_PART_DLR_INSTOCK_MAIN      M,\n");
        sql.append("       TT_PART_DLR_INSTOCK_DTL       PD,\n");
        sql.append("       TT_PART_DEFINE                D\n");
        sql.append(" WHERE EL.IN_ID = M.IN_ID\n");
        sql.append("   AND M.IN_ID = PD.IN_ID\n");
        sql.append("   AND PD.PART_ID = EL.PART_ID\n");
        sql.append("   AND EL.PART_ID = D.PART_ID\n");
        sql.append("   AND M.IN_ID =" + inId);

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getWareHouseFac(String orgId, String proFac) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT Distinct d.wh_id,d.produce_fac\n");
        sbSql.append("  FROM TT_PART_WAREHOUSE_DEFINE D\n");
        sbSql.append(" WHERE 1 = 1");
        sbSql.append("   AND D.org_Id = " + orgId);
        sbSql.append("   AND D.state =" + Constant.STATUS_ENABLE);
        sbSql.append("   AND PRODUCE_FAC='").append(proFac).append("'");
        List<Map<String, Object>> wareHoustList = this.pageQuery(sbSql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    public void updatePartTransType(String planId, String transType, String transOrg) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append("UPDATE TT_PART_TRANS_PLAN P\n");
            sql.append("   SET P.TRANS_TYPE = '" + transType + "', P.TRANSPORT_ORG = '" + transOrg + "'\n");
            sql.append(" WHERE P.TRPLAN_ID = '" + planId + "'");
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> getSaler(String userType) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT U.USER_ID, U.NAME\n");
        sql.append("  FROM TC_USER U,\n");
        sql.append("       (SELECT PU.*\n");
        sql.append("          FROM TT_PART_USERPOSE_DEFINE PU\n");
        sql.append("         WHERE PU.STATE = '10011001'\n");
        sql.append("           AND PU.STATUS = 1\n");
        sql.append("           AND PU.USER_TYPE = '" + userType + "') PU\n");
        sql.append(" WHERE COMPANY_ID = '" + Constant.OEM_ACTIVITIES + "'\n");
        sql.append("   AND U.USER_ID = PU.USER_ID\n");
        sql.append("   AND U.USER_STATUS = '10011001'\n");
        sql.append(" ORDER BY PU.USER_TYPE, U.ACNT");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;

    }

    public void updatePartGxPlan(String planId, String transType, String transOrg) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append("UPDATE TT_PART_TRANS_PLAN P\n");
            sql.append("   SET P.TRANS_TYPE = '" + transType + "', P.TRANSPORT_ORG = '" + transOrg + "'\n");
            sql.append(" WHERE P.TRPLAN_ID = '" + planId + "'");
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    //广宣物流查询
    public PageResult<Map<String, Object>> queryGXWL(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        try {
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE")).toUpperCase();//服务站号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//服务站名称
            String pkgNo = CommonUtils.checkNull(request.getParamValue("PKG_NO"));//装箱号
            String cstartDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));//最终发运开始时间
            String cendDate = CommonUtils.checkNull(request.getParamValue("CendDate"));//最终发运截止时间
            String outType = CommonUtils.checkNull(request.getParamValue("OUT_TYPE"));//发运方式
            String isHava = CommonUtils.checkNull(request.getParamValue("IS_HAVA"));//是否入库

            StringBuffer sql = new StringBuffer();

            sql.append("with strans  as\n");
            sql.append("(select lg.logi_name,\n");
            sql.append("w.ass_date,\n");
            sql.append("w.ass_no　from tt_sales_assign w,\n");
            sql.append("tt_sales_ass_detail d,\n");
            sql.append("tt_sales_alloca_de de,\n");
            sql.append("tt_sales_waybill wb,\n");
            sql.append("tt_sales_logi lg\n");
            sql.append("where w.ass_id = d.ass_id\n");
            sql.append("and d.ass_detail_id = de.ass_detail_id\n");
            sql.append("and de.bill_id = wb.bill_id\n");
            sql.append("and wb.logi_id = lg.logi_id)\n");
            sql.append("select distinct dm.dealer_code,\n");
            sql.append("                dm.dealer_name,\n");
            sql.append("                dm.plan_code,\n");
            sql.append("                dm.order_code,\n");
//            sql.append("                dt.pkg_no,\n");
            sql.append("                w.logi_name,\n");
            sql.append("                dt.ass_no,\n");
            sql.append("                w.ass_date,\n");
            sql.append("                (select fd.fix_name\n");
            sql.append("                   from tt_part_fixcode_define fd\n");
            sql.append("                  where fd.fix_gouptype = '92251008'\n");
            sql.append("                    and fd.fix_value = dt.trans_org) AS trans_org,\n");
            sql.append("                dt.wl_no,\n");
            sql.append("                dt.wl_date,\n");
            sql.append("                dm.PLAN_ID,\n");
            sql.append("                s.state\n");
            sql.append("  from tt_part_dplan_dtl dt, tt_part_dplan_main dm, strans w,tt_part_trans s\n");
            sql.append(" where dt.plan_id = dm.plan_id\n");
            sql.append("   and dt.ass_no = w.ass_no(+)\n");
            sql.append("   and dm.plan_code=s.order_code(+)");
            sql.append("    AND DM.STATE <> ").append(Constant.PART_GX_ORDER_STATE_01);

            if (!"".equals(dealerName)) {
                sql.append(" AND DM.DEALER_NAME LIKE '%").append(dealerName).append("%'");
            }
            if (!"".equals(pkgNo)) {
                sql.append(" AND dt.pkg_no LIKE '%").append(pkgNo).append("%'");
            }

            if (!"".equals(cstartDate)) {
                sql.append(" AND TO_DATE(DM.FINAL_DATE)>=").append("TO_DATE('").append(cstartDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(cendDate)) {
                sql.append(" AND TO_DATE(DM.FINAL_DATE)<=").append("TO_DATE('").append(cendDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(dealerCode)) {
                if (dealerCode != null) {
                    if (!(dealerCode.split(",").length <= 0)) {
                        sql.append(" and dm.dealer_code in(");
                        String[] paramStrArr = dealerCode.split(",");
                        for (int i = 0; i < paramStrArr.length; i++) {
                            String tempCode = paramStrArr[i];
                            if (i == paramStrArr.length - 1) {
                                sql.append("'" + tempCode + "')");
                            } else {
                                sql.append("'" + tempCode + "', ");
                            }
                        }
                    }
                }
            }
            if (!"".equals(outType) && null != outType) {
                if (outType.equals(Constant.PART_GX_ORDER_OUT_TYPE_01 + "")) {//随车
                    sql.append(" AND dt.ass_no IS NOT NULL");
                } else {
                    sql.append(" AND dt.ass_no IS  NULL");
                }
            }
            if (!"".equals(isHava) && null != isHava) {
                sql.append(" AND s.state=" + isHava);
            }

            PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            return ps;

        } catch (Exception e) {
            throw e;
        }
    }

}
