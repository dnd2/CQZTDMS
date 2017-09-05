package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
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

public class PartReplacedDlrOrderDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartReplacedDlrOrderDao.class);
    private static final PartReplacedDlrOrderDao dao = new PartReplacedDlrOrderDao();

    private PartReplacedDlrOrderDao() {
    }

    public static final PartReplacedDlrOrderDao getInstance() {
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
     * @throws : LastDate : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public PageResult<Map<String, Object>> queryPartDlrOrder(
            RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
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
        String orderCode = CommonUtils.checkNull(request
                .getParamValue("ORDER_CODE"));// 订单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));// 订货单位
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("SELLER_NAME"));// 销售单位
        String sCreateDate = CommonUtils.checkNull(request
                .getParamValue("SCREATE_DATE"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request
                .getParamValue("ECREATE_DATE"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request
                .getParamValue("ORDER_TYPE"));// 订单类型
        String sSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SSUBMIT_DATE"));// 提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request
                .getParamValue("ESUBMIT_DATE"));// 提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 订单状态

        String discount = CommonUtils.checkNull(request
                .getParamValue("discount"));//
        sql.append(" select ");
        sql.append(" order_id,order_code,order_type,pay_type,dealer_id,dealer_code, ");
        sql.append(" dealer_name,seller_id,seller_code,seller_name,seller_type,buyer_id,buyer_name, ");
        sql.append(" rcv_orgid,rcv_org,addr_id,addr,receiver,tel,post_code,station,trans_type,account_sum,account_ky, ");
        sql.append(" account_dj,to_char(order_amount,'FM999,999,999,999,990.00') as order_amount,discount,remark,create_date,create_by,update_date,update_by,disable_date,disable_by, ");
        sql.append(" delete_date,delete_by,submit_date,submit_by,audit_date,audit_by,autchk_date,is_autchk,ver,state,status,wh_id,wh_cname,audit_opinion,freight,rebut_reason,M.ACTIVITY_CODE, ");
        sql.append(" (SELECT D.PART_TYPE\n");
        sql.append("        FROM TT_PART_SPECIAL_DEFINE D\n");
        sql.append("       WHERE D.ACTIVITY_CODE = M.ACTIVITY_CODE\n");
        sql.append("         AND ROWNUM = 1) PART_TYPE");
        sql.append(" from tt_part_dlr_order_main m ");
        sql.append(" where 1=1 ");
        if (!"".equals(orderCode)) {
            sql.append(" and ORDER_CODE like '%")
                    .append(orderCode.toUpperCase()).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and DEALER_NAME like '%").append(dealerName)
                    .append("%'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and SELLER_NAME like '%").append(sellerName)
                    .append("%'");
        }
        // 前台已经加限制 必须要有时间范围 所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and CREATE_DATE>= to_date('").append(sCreateDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and CREATE_DATE<= to_date('").append(eCreateDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and ORDER_TYPE='").append(orderType).append("'");
        } else {
            sql.append(" and ORDER_TYPE in (")
                    .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01
                            + ","
                            + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02)
                    .append(")");
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and SUBMIT_DATE>= to_date('").append(sSubmitDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and SUBMIT_DATE<= to_date('").append(eSubmitDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (discount.equals("discount")) {
            sql.append(" and discount_Status='").append(Constant.IF_TYPE_YES)
                    .append("'");
        }
        if (!"".equals(state)) {
            if (state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01
                    + "")) {
                sql.append(" and STATE in (")
                        .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01)
                        .append(",")
                        .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05)
                        .append(")");
            } else {
                sql.append(" and STATE='").append(state).append("'");
            }
        }
        sql.append(" and oem_flag='").append(oemFlag).append("'");
        sql.append(" and dealer_id='").append(dealerId)
                .append("' order by create_date desc");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public PageResult<Map<String, Object>> partDlrOrderStateQuery(
            RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
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
        String orderCode = CommonUtils.checkNull(request
                .getParamValue("ORDER_CODE"));// 订单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));// 订货单位
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("SELLER_NAME"));// 销售单位
        String sCreateDate = CommonUtils.checkNull(request
                .getParamValue("SCREATE_DATE"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request
                .getParamValue("ECREATE_DATE"));// 制单日期 结束
        String sSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SSUBMIT_DATE"));// 提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request
                .getParamValue("ESUBMIT_DATE"));// 提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 订单状态
        String actiCode = CommonUtils.checkNull(request
                .getParamValue("ACTI_CODE"));// 订单状态
        String dealerCode = CommonUtils.checkNull(request
                .getParamValue("dealerCode"));// 订单状态
        String dealerCode2 = CommonUtils.checkNull(request
                .getParamValue("DEALER_CODE"));// 订单状态
        String orgCode = CommonUtils
                .checkNull(request.getParamValue("orgCode"));// 大区

        String discount = CommonUtils.checkNull(request
                .getParamValue("discount"));//
        sql.append(" select ");
        sql.append(" order_id,order_code,order_type,pay_type,dealer_id,dealer_code, ");
        sql.append(" dealer_name,seller_id,seller_code,seller_name,seller_type,buyer_id,buyer_name, ");
        sql.append(" rcv_orgid,rcv_org,addr_id,addr,receiver,tel,post_code,station,trans_type,account_sum,account_ky, ");
        sql.append(" account_dj,to_char(order_amount,'FM999,999,999,999,990.00') as order_amount,discount,remark,create_date,create_by,update_date,update_by,disable_date,disable_by, ");
        sql.append(" delete_date,delete_by,submit_date,submit_by,audit_date,audit_by,autchk_date,is_autchk,ver,state,status,wh_id,wh_cname,audit_opinion,freight,rebut_reason,ACTIVITY_CODE ");
        sql.append(" from tt_part_dlr_order_main M ");
        sql.append(" where 1=1 ");
        sql.append(" AND M.STATE <> 92161004\n");

        if (!"".equals(orderCode)) {
            sql.append(" and M.ORDER_CODE like '%")
                    .append(orderCode.toUpperCase()).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and M.DEALER_NAME like '%").append(dealerName)
                    .append("%'");
        }
        if (!"".equals(dealerCode2)) {
            sql.append(" and M.DEALER_CODE like '%").append(dealerCode2)
                    .append("%'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and M.SELLER_NAME like '%").append(sellerName)
                    .append("%'");
        }
        sql.append(" and M.ORDER_TYPE='")
                .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10)
                .append("'");
        if (!"".equals(sSubmitDate) && null != sSubmitDate) {
            sql.append(" and M.SUBMIT_DATE>= to_date('").append(sSubmitDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(eSubmitDate) && null != eSubmitDate) {
            sql.append(" and M.SUBMIT_DATE<= to_date('").append(eSubmitDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (discount.equals("discount")) {
            sql.append(" and M.discount_Status='").append(Constant.IF_TYPE_YES)
                    .append("'");
        }
        if (!"".equals(state)) {
            sql.append(" and M.state='").append(state).append("'");
        }

        if (Constant.PART_BASE_FLAG_NO.toString().equals(oemFlag)) {
            //sql.append(" and M.oem_flag='").append(oemFlag).append("'");
            sql.append(" and M.dealer_id='").append(dealerId).append("'");
        } else {
            sql.append(" and M.seller_id='").append(dealerId).append("'");
        }
        if (!"".equals(actiCode) && null != actiCode) {
            sql.append(" and M.ACTIVITY_CODE like '%").append(actiCode)
                    .append("%'");
        }
        if (!"".equals(dealerCode) && null != dealerCode) {
            sql.append(" AND M.DEALER_CODE in ("
                    + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(
                    dealerCode) + ")\n");
        }
        // 增加大区限制
        if (!"".equals(orgCode) && null != orgCode) {
            sql.append("AND EXISTS (SELECT 1\n");
            sql.append("         FROM VW_ORG_DEALER_SERVICE S\n");
            sql.append("        WHERE S.DEALER_ID = M.DEALER_ID\n");
            sql.append("          AND S.Org_Code IN ("
                    + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(
                    orgCode) + "))\n");
        }
        sql.append(" order by create_date desc");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-16
     * @Title :
     * @Description: 查询(审核)
     */
    public PageResult<Map<String, Object>> queryCheckPartDlrOrder(
            RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request
                .getParamValue("ORDER_CODE"));// 订单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));// 订货单位
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("SELLER_NAME"));// 销售单位
        String sCreateDate = CommonUtils.checkNull(request
                .getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request
                .getParamValue("CendDate"));// 制单日期 结束
        String orderType = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10
                .toString();// 订单类型
        String sSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SstartDate"));// 提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SendDate"));// 提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 订单状态
        String autoPreCheck = CommonUtils.checkNull(request
                .getParamValue("autoPreCheck"));// 自动预审
        String salerId = CommonUtils
                .checkNull(request.getParamValue("salerId"));
        String planFlag = request.getParamValue("planFlag");
        String dealerCode = CommonUtils.checkNull(request
                .getParamValue("DEALER_CODE"));// 订货单位

        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
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
        sql.append("       A.OEM_FLAG,\n");
        sql.append("       A.ACTIVITY_CODE,\n");
        sql.append("       (SELECT Distinct D.PART_TYPE\n");
        sql.append("          FROM TT_PART_SPECIAL_DEFINE D\n");
        sql.append("         WHERE D.ACTIVITY_CODE = A.ACTIVITY_CODE\n");
        sql.append("           AND ROWNUM = 1) PART_TYPE");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN A, TM_DEALER TD\n");
        /*
         * sql.append(
		 * " join tt_part_salesscope_define b on a.dealer_id=b.dealer_id and b.user_id='"
		 * ).append(loginUser.getUserId()).append("'");
		 */
        sql.append(" where 1=1 ");
        sql.append("AND A.DEALER_ID = TD.DEALER_ID\n");
        if (dealerId.equals(Constant.OEM_ACTIVITIES)) { // add by yuan 2013062
            // 车厂才有限制
            if ("".equals(salerId)) {
                /*
                 * sql.append("  AND EXISTS (SELECT 1\n" +
				 * "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
				 * "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
				 * "           AND B.USER_ID ="+loginUser.getUserId() +")");
				 */
            } else {
                sql.append("  AND EXISTS (SELECT 1\n"
                        + "          FROM TT_PART_SALESSCOPE_DEFINE B\n"
                        + "         WHERE A.DEALER_ID = B.DEALER_ID\n"
                        + "           AND B.USER_ID =" + salerId + ")");
            }
        }
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode)
                    .append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName)
                    .append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(td.Dealer_code) like upper('%")
                    .append(dealerCode).append("%')");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName)
                    .append("%'");
        }
        // 前台已经加限制 必须要有时间范围 所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE=").append(orderType);
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
        sql.append(") and a.seller_id='")
                .append(CommonUtils.checkNull(dealerId)).append("'");
        sql.append(" order by a.SUBMIT_DATE desc");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-16
     * @Title :
     * @Description: 查询(回运)只查询库存配件
     */
    public PageResult<Map<String, Object>> queryCheckPartDlrOrderBackhaul(
            RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request
                .getParamValue("ORDER_CODE"));// 订单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));// 订货单位b
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("SELLER_NAME"));// 销售单位
        String sCreateDate = CommonUtils.checkNull(request
                .getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request
                .getParamValue("CendDate"));// 制单日期 结束
        String orderType = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10
                .toString();// 订单类型
        String sSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SstartDate"));// 提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SendDate"));// 提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 订单状态为审核的
        String autoPreCheck = CommonUtils.checkNull(request
                .getParamValue("autoPreCheck"));// 自动预审
        String salerId = CommonUtils
                .checkNull(request.getParamValue("salerId"));
        String planFlag = request.getParamValue("planFlag");
        String dealerCode = CommonUtils.checkNull(request
                .getParamValue("DEALER_CODE"));// 订货单位
        String wlNO = CommonUtils.checkNull(request.getParamValue("wlNO"));// 物流单号
        String wl = CommonUtils.checkNull(request.getParamValue("wl"));// 物流

        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

        sql.append("SELECT distinct A.ORDER_ID,\n");
        sql.append("       A.ORDER_CODE,\n");
        sql.append("       A.WULIU_CODE,\n");
        sql.append("       A.WULIU_COMPANY,\n");
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
        sql.append("       A.activity_code,\n");
        sql.append("       A.OEM_FLAG,\n");
        sql.append("       SPECIAL.PART_TYPE\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN A\n");
        sql.append("  join TM_DEALER TD on A.DEALER_ID = TD.DEALER_ID\n");
        sql.append("  join TT_PART_SPECIAL_DEFINE SPECIAL on A.ACTIVITY_CODE=SPECIAL.ACTIVITY_CODE\n");
        sql.append("  join TT_PART_DLR_ORDER_DTL dtl on A.ORDER_ID=dtl.ORDER_ID\n");
        sql.append("  where 1=1 ");
//        sql.append("  AND SPECIAL.PART_TYPE = ").append(Constant.ACTIVITY_PART_TYPE_02).append("\n");
        sql.append("  AND dtl.ISNEED_FLAG = ").append(Constant.IF_TYPE_YES)
                .append("\n");
        sql.append("  AND dtl.CHECK_QTY > 0 ").append("\n");
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode)
                    .append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName)
                    .append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(td.Dealer_code) like upper('%")
                    .append(dealerCode).append("%')");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName)
                    .append("%'");
        }
        // 前台已经加限制 必须要有时间范围 所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE=").append(orderType);
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'");
        }
        if ("1".equals(CommonDAO.getPara(60041003 + ""))) {
            sql.append("AND EXISTS\n");
            sql.append(" (SELECT 1\n");
            sql.append("          FROM TT_PART_SPECIAL_DEFINE SD, TT_PART_SPECIAL_DLR_REL DR\n");
            sql.append("         WHERE SD.DESCRIBE_ID = DR.DESCRIBE_ID\n");
            sql.append("           AND SD.ACTIVITY_CODE = A.ACTIVITY_CODE\n");
            sql.append("           AND DR.STATE = 10011001\n");
            sql.append("           AND DR.DEALER_ID = A.DEALER_ID\n");
            sql.append("           AND DR.STUTUS = 1)\n");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
        sql.append(") ");
        if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
            sql.append(" and a.seller_id =").append(dealerId);
        } else {
            sql.append(" and a.dealer_id =").append(dealerId);
        }
        if (!"".equals(wl) && null != wl) {
            sql.append(" and a.wuliu_company like '%").append(wl).append("%'");
        }
        if (!"".equals(wlNO) && null != wlNO) {
            sql.append(" and a.wuliu_code like '%").append(wlNO).append("%'");
        }
        sql.append(" and a.ORDER_TYPE !=").append(
                Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
        // end
        sql.append(" order by a.SUBMIT_DATE desc");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-16
     * @Title :
     * @Description: 查询(回运)只查询库存配件
     */
    public List<Map<String, Object>> queryCheckPartDlrOrderBackhaul(
            RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request
                .getParamValue("ORDER_CODE"));// 订单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));// 订货单位b
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("SELLER_NAME"));// 销售单位
        String sCreateDate = CommonUtils.checkNull(request
                .getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request
                .getParamValue("CendDate"));// 制单日期 结束
        String orderType = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10
                .toString();// 订单类型
        String sSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SstartDate"));// 提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SendDate"));// 提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 订单状态为审核的
        String autoPreCheck = CommonUtils.checkNull(request
                .getParamValue("autoPreCheck"));// 自动预审
        String salerId = CommonUtils
                .checkNull(request.getParamValue("salerId"));
        String planFlag = request.getParamValue("planFlag");
        String dealerCode = CommonUtils.checkNull(request
                .getParamValue("DEALER_CODE"));// 订货单位
        String wlNO = CommonUtils.checkNull(request.getParamValue("wlNO"));// 物流单号
        String wl = CommonUtils.checkNull(request.getParamValue("wl"));// 物流

        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

        sql.append("SELECT distinct A.ORDER_ID,\n");
        sql.append("       A.ORDER_CODE,\n");
        sql.append("       A.WULIU_CODE,\n");
        sql.append("       A.WULIU_COMPANY,\n");
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
        //sql.append("       SPECIAL.PART_TYPE\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN A\n");
        sql.append("  join TM_DEALER TD on A.DEALER_ID = TD.DEALER_ID\n");
        sql.append("  join TT_PART_SPECIAL_DEFINE SPECIAL on A.ACTIVITY_CODE=SPECIAL.ACTIVITY_CODE\n");
        sql.append("  join TT_PART_DLR_ORDER_DTL dtl on A.ORDER_ID=dtl.ORDER_ID\n");
        sql.append("  where 1=1 ");
        sql.append("  AND SPECIAL.PART_TYPE = ")
                .append(Constant.ACTIVITY_PART_TYPE_02).append("\n");
        sql.append("  AND dtl.ISNEED_FLAG = ").append(Constant.IF_TYPE_YES)
                .append("\n");
        sql.append("  AND dtl.CHECK_QTY > 0 ").append("\n");
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode)
                    .append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName)
                    .append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(td.Dealer_code) like upper('%")
                    .append(dealerCode).append("%')");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName)
                    .append("%'");
        }
        // 前台已经加限制 必须要有时间范围 所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE=").append(orderType);
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_13).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_14).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
        sql.append(") ");
        if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
            sql.append(" and a.seller_id =").append(dealerId);
        } else {
            sql.append(" and a.dealer_id =").append(dealerId);
        }
        if (!"".equals(wl) && null != wl) {
            sql.append(" and a.wuliu_company like '%").append(wl).append("%'");
        }
        if (!"".equals(wlNO) && null != wlNO) {
            sql.append(" and a.wuliu_code like '%").append(wlNO).append("%'");
        }
        sql.append(" and a.ORDER_TYPE !=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
        // end
        sql.append(" order by a.SUBMIT_DATE desc");
        List<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName());
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-16
     * @Title :
     * @Description: 查询(审核导出)
     */
    public List<Map<String, Object>> queryCheckPartDlrOrder(
            RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request
                .getParamValue("ORDER_CODE"));// 订单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));// 订货单位
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("SELLER_NAME"));// 销售单位
        String sCreateDate = CommonUtils.checkNull(request
                .getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request
                .getParamValue("CendDate"));// 制单日期 结束
        String orderType = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10
                .toString();// 订单类型
        String sSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SstartDate"));// 提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SendDate"));// 提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 订单状态
        String autoPreCheck = CommonUtils.checkNull(request
                .getParamValue("autoPreCheck"));// 自动预审
        String salerId = CommonUtils
                .checkNull(request.getParamValue("salerId"));
        String planFlag = request.getParamValue("planFlag");
        String dealerCode = CommonUtils.checkNull(request
                .getParamValue("DEALER_CODE"));// 订货单位

        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }

        sql.append("SELECT A.ORDER_ID,\n");
        sql.append("       A.ORDER_CODE,\n");
        sql.append(" (select CODE_DESC FROM TC_CODE b WHERE b.CODE_ID=A.ORDER_TYPE) as ORDER_TYPE, ");
        // sql.append("       A.ORDER_TYPE,\n");
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
        sql.append("       to_char(A.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') as CREATE_DATE,\n");
        sql.append("       A.CREATE_BY,\n");
        sql.append("       to_char(A.UPDATE_DATE,'yyyy-mm-dd hh24:mi:ss') as UPDATE_DATE,\n");
        sql.append("       A.UPDATE_BY,\n");
        sql.append("       A.DISABLE_DATE,\n");
        sql.append("       A.DISABLE_BY,\n");
        sql.append("       A.DELETE_DATE,\n");
        sql.append("       A.DELETE_BY,\n");
        sql.append("       to_char(A.SUBMIT_DATE,'yyyy-mm-dd hh24:mi:ss') as SUBMIT_DATE,\n");
        sql.append("       A.SUBMIT_BY,\n");
        sql.append("       A.AUDIT_DATE,\n");
        sql.append("       A.AUDIT_BY,\n");
        sql.append("       A.AUTCHK_DATE,\n");
        sql.append("       A.IS_AUTCHK,\n");
        sql.append("       A.VER,\n");
        sql.append(" (select CODE_DESC FROM TC_CODE b WHERE b.CODE_ID=A.STATE) as STATE, ");
        // sql.append("       A.STATE,\n");
        sql.append("       A.STATUS,\n");
        sql.append("       A.WH_ID,\n");
        sql.append("       A.WH_CNAME,\n");
        sql.append("       A.AUDIT_OPINION,\n");
        sql.append("       A.REBUT_REASON,\n");
        sql.append("       A.OEM_FLAG\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN A, TM_DEALER TD\n");
        /*
         * sql.append(
		 * " join tt_part_salesscope_define b on a.dealer_id=b.dealer_id and b.user_id='"
		 * ).append(loginUser.getUserId()).append("'");
		 */
        sql.append(" where 1=1 ");
        sql.append("AND A.DEALER_ID = TD.DEALER_ID\n");
        if (dealerId.equals(Constant.OEM_ACTIVITIES)) { // add by yuan 2013062
            // 车厂才有限制
            if ("".equals(salerId)) {
                /*
                 * sql.append("  AND EXISTS (SELECT 1\n" +
				 * "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
				 * "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
				 * "           AND B.USER_ID ="+loginUser.getUserId() +")");
				 */
            } else {
                sql.append("  AND EXISTS (SELECT 1\n"
                        + "          FROM TT_PART_SALESSCOPE_DEFINE B\n"
                        + "         WHERE A.DEALER_ID = B.DEALER_ID\n"
                        + "           AND B.USER_ID =" + salerId + ")");
            }
        }
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode)
                    .append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName)
                    .append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(td.Dealer_code) like upper('%")
                    .append(dealerCode).append("%')");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName)
                    .append("%'");
        }
        // 前台已经加限制 必须要有时间范围 所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE=").append(orderType);
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
        sql.append(") and a.seller_id='")
                .append(CommonUtils.checkNull(dealerId)).append("'");
        /*
         * if(getUserType(loginUser.getUserId()).equals("6")){
		 * sql.append(" AND A.create_by=").append(loginUser.getUserId()); }
		 */
        // add by yuan 20140305 过滤广宣品订单
        sql.append(" and a.ORDER_TYPE !=").append(
                Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
        // end
        sql.append(" order by SUBMIT_DATE desc");
        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-9
     * @Title :
     * @Description: 获取仓库
     */
    public List<Map<String, Object>> getWareHouse() {
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.wh_id,t.wh_name from TT_PART_WAREHOUSE_DEFINE t");
        List<Map<String, Object>> wareHoustList = this.pageQuery(
                sql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    /**
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-19
     * @Title :
     * @Description: 获取订货单位
     */
    public Map<String, Object> getDealerName(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from  TM_DEALER t ");
        sql.append(" where 1=1 ");
        sql.append(" and t.DEALER_ID='").append(dealerId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-19
     * @Title :
     * @Description: 获取折扣率
     */
    public String getDiscount(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(DISCOUNT,'1') DISCOUNT from  TT_PART_SALES_RELATION t ");
        sql.append(" where 1=1 ");
        sql.append(" and t.CHILDORG_ID='").append(dealerId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (list.size() <= 0) {
            return null;
        }
        return CommonUtils.checkNull(list.get(0).get("DISCOUNT"));
    }

    /**
     * @param : @param orderId
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-19
     * @Title :
     * @Description: 获取地址
     */
    public PageResult<Map<String, Object>> getAddress(String dealerId,
                                                      int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" ADDR_ID,ADDR ");
        sql.append(" from tt_part_addr_define ");
        sql.append(" where 1=1 ");
        sql.append(" and DEALER_ID='").append(dealerId).append("'");
        return this.pageQuery(sql.toString(), null, getFunName(), pageSize,
                curPage);
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 查询配件
     */
    // RequestWrapper中需要放入获取金额的dealer_id 销售单和提报的dealer_id不一样
    public PageResult<Map<String, Object>> showPartBase(RequestWrapper req,
                                                        String sellerId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String produceFac = CommonUtils.checkNull(req
                .getParamValue("produceFac"));
        String orderType = CommonUtils.checkNull(req
                .getParamValue("ORDER_TYPE"));

        sql.append("SELECT REPLACED.REPART_OLDCODE,REPLACED.REPART_CNAME,A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.MODEL_NAME,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT,\n");
        sql.append("       NVL((SELECT SUM(S.normal_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = A.PART_ID\n");
        sql.append("              AND S.ORG_ID = '")
                .append(req.getAttribute("bookDealerId")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES)) { // 主机厂按照最小包装量量销售
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                    FROM TM_DEALER D\n");
            sql.append("                   WHERE D.DEALER_ID = ").append(
                    req.getAttribute("bookDealerId"));
            sql.append("                     AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) MIN_PACKAGE,\n");// 供应中心油品整箱，其他1
        }
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
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
        sql.append("           AND E.ORG_ID = ").append(sellerId)
                .append(") AS UPORGSTOCK,\n");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("            WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("              AND ORG_ID ='")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append("'),'0') as qty,\n");
        sql.append("  nvl(a.is_special,'10041002') as is_special ");
        sql.append("  FROM TT_PART_DEFINE A\n ");
        sql.append("  JOIN TT_PART_REPLACED_DEFINE REPLACED ON a.PART_ID=REPLACED.PART_ID  ");
        sql.append(" WHERE A.STATUS = 1\n");

        // sql.append(" and  A.produce_Fac='").append(produceFac).append("'");
        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")
                .equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append(" and A.is_gxp = " + Constant.IF_TYPE_YES);
        }
        String uploadFlag = CommonUtils.checkNull(req
                .getAttribute("uploadFlag"));
        if (!"".equals(uploadFlag) && "upload".equals(uploadFlag)) {
            if (!"".equals(CommonUtils.checkNull(req
                    .getAttribute("partOldcode")))) {
                sql.append(" and a.PART_OLDCODE ='")
                        .append(req.getAttribute("partOldcode")).append("'");
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                // 如果是计划订单(特殊类型订单) 配件类型筛选
                if (CommonUtils.checkNull(req.getParamValue("isSpecialOrPlan"))
                        .equals("1")) { // 1 特殊配件
                    sql.append(" and a.is_special='")
                            .append(Constant.IF_TYPE_YES).append("'");
                } else if (CommonUtils.checkNull(
                        req.getParamValue("isSpecialOrPlan")).equals("2")) { // 2
                    // 计划配件
                    sql.append(" and a.is_plan='")
                            .append(Constant.IF_TYPE_YES)
                            .append("'")
                            .append(" and ( a.is_special='")
                            .append(Constant.IF_TYPE_NO)
                            .append("' or a.is_special is null or a.is_special='' )");
                }
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                sql.append(
                        " and part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='")
                        .append(CommonUtils.checkNull(req
                                .getParamValue("brand"))).append("')");
                /*
                 * sql.append(" and A.IS_DIRECT ='").append(Constant.IF_TYPE_YES)
				 * .append("'");
				 */
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%")
                    .append(req.getParamValue("PART_CODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%")
                    .append(req.getParamValue("PART_OLDCODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%")
                    .append(req.getParamValue("PART_CNAME").toUpperCase())
                    .append("%'");
        }

        // add by yuan 20130913 踢掉销售单位无效无库存配件
        if (!(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")
                .equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            /*
             * sql.append("AND not EXISTS (SELECT 1\n");
			 * sql.append("       FROM VW_PART_STOCK S\n");
			 * sql.append("      WHERE S.PDSTATE = 10011002\n");
			 * sql.append("        AND S.ORG_ID = ").append(sellerId);
			 * sql.append("        AND S.PART_ID = A.PART_ID\n");
			 * sql.append("        AND S.NORMAL_QTY = 0)\n");
			 */
        }
        if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03
                .toString())) {
            // sql.append("AND A.IS_SPECIAL = ").append(Constant.IF_TYPE_YES);
        } else {
            // sql.append("AND A.IS_SPECIAL = ").append(Constant.IF_TYPE_NO);
        }
        // add end
        // sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 查询配件
     */
    public PageResult<Map<String, Object>> showRepplacedPartBase(
            RequestWrapper req, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        // String produceFac =
        // CommonUtils.checkNull(req.getParamValue("produceFac"));
        // String orderType =
        // CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"));

        sql.append("SELECT DISTINCT REPLACED.PART_TYPE,REPLACED.ACTIVITY_CODE,REPLACED.ISNEED_FLAG,REPLACED.REPART_OLDCODE,REPLACED.REPART_ID,REPLACED.REPART_CODE,REPLACED.REPART_NAME,A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.MODEL_NAME,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT,\n");
        sql.append("       NVL((SELECT SUM(S.normal_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("             WHERE S.PART_ID = A.PART_ID\n");
        sql.append("              AND S.ORG_ID = '")
                .append(req.getAttribute("bookDealerId")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES)) { // 主机厂按照最小包装量量销售
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                  FROM TM_DEALER D\n");
            sql.append("                  WHERE D.DEALER_ID = ").append(
                    req.getAttribute("bookDealerId"));
            sql.append("                  AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) MIN_PACKAGE,\n");// 供应中心油品整箱，其他1
        }
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("             WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("             AND ORG_ID ='")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append("'),'0') as qty,\n");
        sql.append("  nvl(a.is_special,'10041002') as is_special ");
        sql.append("  FROM TT_PART_DEFINE A\n ");
        sql.append("  JOIN TT_PART_SPECIAL_DEFINE REPLACED ON a.PART_ID=REPLACED.PART_ID  ");
        sql.append("  WHERE 1 = 1\n");
        List<Map<String, Object>> actCodelist = this.getCurrentActCode("");
        if (actCodelist.size() > 0) {
            if (CommonUtils.checkNull(req.getParamValue("actCodeMap")).equals(
                    "")) {
                sql.append("  and REPLACED.ACTIVITY_CODE = '")
                        .append(actCodelist.get(0).get("ACTIVITY_CODE"))
                        .append("'\n");
            } else {
                sql.append("  and REPLACED.ACTIVITY_CODE = '")
                        .append(CommonUtils.checkNull(req
                                .getParamValue("actCodeMap"))).append("'\n");
            }
        } else {
            // sql.append("  and REPLACED.ACTIVITY_CODE = '").append(actCodelist.get(0).get("ACTIVITY_CODE")).append("'\n");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("DESCRIBE")))) {
            sql.append("  and REPLACED.DESCRIBE = '")
                    .append(CommonUtils.checkNull(req.getParamValue("DESCRIBE")))
                    .append("'\n");
        }
        sql.append("  and REPLACED.START_DATE<= TRUNC(SYSDATE) \n");
        sql.append("  and REPLACED.END_DATE>= TRUNC(SYSDATE) \n");
        sql.append("  and REPLACED.ACTIVITY_TYPE =  ")
                .append(Constant.PART_ACTIVITY_TYPE_REPLACED_01).append(" \n");

        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")
                .equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append(" and A.is_gxp = " + Constant.IF_TYPE_YES);
        }
        String uploadFlag = CommonUtils.checkNull(req
                .getAttribute("uploadFlag"));
        if (!"".equals(uploadFlag) && "upload".equals(uploadFlag)) {
            if (!"".equals(CommonUtils.checkNull(req
                    .getAttribute("partOldcode")))) {
                sql.append(" and a.PART_OLDCODE ='")
                        .append(req.getAttribute("partOldcode")).append("'");
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                // 如果是计划订单(特殊类型订单) 配件类型筛选
                if (CommonUtils.checkNull(req.getParamValue("isSpecialOrPlan"))
                        .equals("1")) { // 1 特殊配件
                    sql.append(" and a.is_special='")
                            .append(Constant.IF_TYPE_YES).append("'");
                } else if (CommonUtils.checkNull(
                        req.getParamValue("isSpecialOrPlan")).equals("2")) { // 2
                    // 计划配件
                    sql.append(" and a.is_plan='")
                            .append(Constant.IF_TYPE_YES)
                            .append("'")
                            .append(" and ( a.is_special='")
                            .append(Constant.IF_TYPE_NO)
                            .append("' or a.is_special is null or a.is_special='' )");
                }
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                sql.append(
                        " and part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='")
                        .append(CommonUtils.checkNull(req
                                .getParamValue("brand"))).append("')");
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%")
                    .append(req.getParamValue("PART_CODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%")
                    .append(req.getParamValue("PART_OLDCODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%")
                    .append(req.getParamValue("PART_CNAME").toUpperCase())
                    .append("%'");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 查询配件
     */
    public PageResult<Map<String, Object>> showRepplacedPartBaseForMod(
            RequestWrapper req, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        // String produceFac =
        // CommonUtils.checkNull(req.getParamValue("produceFac"));
        // String orderType =
        // CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"));

        sql.append("SELECT DISTINCT REPLACED.PART_TYPE,REPLACED.ACTIVITY_CODE,REPLACED.ISNEED_FLAG,REPLACED.REPART_OLDCODE,REPLACED.REPART_ID,REPLACED.REPART_CODE,REPLACED.REPART_NAME,A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.MODEL_NAME,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT,\n");
        sql.append("       NVL((SELECT SUM(S.normal_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("             WHERE S.PART_ID = A.PART_ID\n");
        sql.append("              AND S.ORG_ID = '")
                .append(req.getAttribute("bookDealerId")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES)) { // 主机厂按照最小包装量量销售
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                  FROM TM_DEALER D\n");
            sql.append("                  WHERE D.DEALER_ID = ").append(
                    req.getAttribute("bookDealerId"));
            sql.append("                  AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) MIN_PACKAGE,\n");// 供应中心油品整箱，其他1
        }
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("             WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("             AND ORG_ID ='")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append("'),'0') as qty,\n");
        sql.append("  nvl(a.is_special,'10041002') as is_special ");
        sql.append("  FROM TT_PART_DEFINE A\n ");
        sql.append("  JOIN TT_PART_SPECIAL_DEFINE REPLACED ON a.PART_ID=REPLACED.PART_ID  ");
        sql.append("  WHERE A.STATUS = 1\n");
        // List<Map<String, Object>> actCodelist = this.getCurrentActCode();
        // if(CommonUtils.checkNull(req.getParamValue("actCodeMap")).equals("")){
        // sql.append("  and REPLACED.ACTIVITY_CODE = '").append(actCodelist.get(0).get("ACTIVITY_CODE")).append("'\n");
        // }else{
        // PartReplacedDlrOrderDao dao = new PartReplacedDlrOrderDao();
        // String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
        // List<Map<String, Object>> list = dao.getActCodeByOrderId(orderId);
        // String actCode =
        // CommonUtils.checkNull(list.get(0).get("ACTIVITY_CODE"));
        // req.setAttribute("ACTIVITY_CODE", actCode);
        // sql.append("  and REPLACED.ACTIVITY_CODE = '").append(actCode).append("'\n");
        // }
        String actCode = CommonUtils.checkNull(req.getParamValue("actCodeMap"));
        sql.append("  and REPLACED.ACTIVITY_CODE = '").append(actCode)
                .append("'\n");
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("DESCRIBE")))) {
            sql.append("  and REPLACED.DESCRIBE = '")
                    .append(CommonUtils.checkNull(req.getParamValue("DESCRIBE")))
                    .append("'\n");
        }
        sql.append("  and REPLACED.START_DATE<= trunc(sysdate) \n");
        sql.append("  and REPLACED.END_DATE>= trunc(sysdate) \n");
        sql.append("  and REPLACED.ACTIVITY_TYPE =  ")
                .append(Constant.PART_ACTIVITY_TYPE_REPLACED_01).append(" \n");

        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")
                .equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append(" and A.is_gxp = " + Constant.IF_TYPE_YES);
        }
        String uploadFlag = CommonUtils.checkNull(req
                .getAttribute("uploadFlag"));
        if (!"".equals(uploadFlag) && "upload".equals(uploadFlag)) {
            if (!"".equals(CommonUtils.checkNull(req
                    .getAttribute("partOldcode")))) {
                sql.append(" and a.PART_OLDCODE ='")
                        .append(req.getAttribute("partOldcode")).append("'");
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                // 如果是计划订单(特殊类型订单) 配件类型筛选
                if (CommonUtils.checkNull(req.getParamValue("isSpecialOrPlan"))
                        .equals("1")) { // 1 特殊配件
                    sql.append(" and a.is_special='")
                            .append(Constant.IF_TYPE_YES).append("'");
                } else if (CommonUtils.checkNull(
                        req.getParamValue("isSpecialOrPlan")).equals("2")) { // 2
                    // 计划配件
                    sql.append(" and a.is_plan='")
                            .append(Constant.IF_TYPE_YES)
                            .append("'")
                            .append(" and ( a.is_special='")
                            .append(Constant.IF_TYPE_NO)
                            .append("' or a.is_special is null or a.is_special='' )");
                }
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                sql.append(
                        " and part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='")
                        .append(CommonUtils.checkNull(req
                                .getParamValue("brand"))).append("')");
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%")
                    .append(req.getParamValue("PART_CODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%")
                    .append(req.getParamValue("PART_OLDCODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%")
                    .append(req.getParamValue("PART_CNAME").toUpperCase())
                    .append("%'");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param dealerId
     * @param :        @param req
     * @param :        @param curPage
     * @param :        @param pageSize
     * @param :        @return
     * @return :
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 查询替换配件
     */
    // RequestWrapper中需要放入获取金额的dealer_id 销售单和提报的dealer_id不一样
    public List<Map<String, Object>> getDetails(RequestWrapper req,
                                                String sellerId, String partId, String dealerId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT REPLACED.REPART_OLDCODE,REPLACED.REPART_CODE,REPLACED.REPART_CNAME,A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.MODEL_NAME,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT,\n");
        sql.append("       NVL((SELECT SUM(S.normal_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = A.PART_ID\n");
        sql.append("              AND S.ORG_ID = '").append(dealerId)
                .append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES)) { // 主机厂按照最小包装量量销售
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                    FROM TM_DEALER D\n");
            sql.append("                   WHERE D.DEALER_ID = ").append(
                    dealerId);
            sql.append("                     AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) MIN_PACKAGE,\n");// 供应中心油品整箱，其他1
        }
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(")
                .append(CommonUtils.checkNull(dealerId))
                .append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
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
        sql.append("           AND E.ORG_ID = ").append(sellerId)
                .append(") AS UPORGSTOCK,\n");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("            WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("              AND ORG_ID ='")
                .append(CommonUtils.checkNull(dealerId))
                .append("'),'0') as qty,\n");
        sql.append("  nvl(a.is_special,'10041002') as is_special ");
        sql.append("  FROM TT_PART_DEFINE A\n ");
        sql.append("  JOIN TT_PART_REPLACED_DEFINE REPLACED ON a.PART_ID=REPLACED.PART_ID  ");
        sql.append(" WHERE A.STATUS = 1\n");
        sql.append("  and a.part_id = ").append(partId).append("\n");
        String uploadFlag = CommonUtils.checkNull(req
                .getAttribute("uploadFlag"));
        if (!"".equals(uploadFlag) && "upload".equals(uploadFlag)) {
            if (!"".equals(CommonUtils.checkNull(req
                    .getAttribute("partOldcode")))) {
                sql.append(" and a.PART_OLDCODE ='")
                        .append(req.getAttribute("partOldcode")).append("'");
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                // 如果是计划订单(特殊类型订单) 配件类型筛选
                if (CommonUtils.checkNull(req.getParamValue("isSpecialOrPlan"))
                        .equals("1")) { // 1 特殊配件
                    sql.append(" and a.is_special='")
                            .append(Constant.IF_TYPE_YES).append("'");
                } else if (CommonUtils.checkNull(
                        req.getParamValue("isSpecialOrPlan")).equals("2")) { // 2
                    // 计划配件
                    sql.append(" and a.is_plan='")
                            .append(Constant.IF_TYPE_YES)
                            .append("'")
                            .append(" and ( a.is_special='")
                            .append(Constant.IF_TYPE_NO)
                            .append("' or a.is_special is null or a.is_special='' )");
                }
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                sql.append(
                        " and part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='")
                        .append(CommonUtils.checkNull(req
                                .getParamValue("brand"))).append("')");
                /*
                 * sql.append(" and A.IS_DIRECT ='").append(Constant.IF_TYPE_YES)
				 * .append("'");
				 */
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%")
                    .append(req.getParamValue("PART_CODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%")
                    .append(req.getParamValue("PART_OLDCODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%")
                    .append(req.getParamValue("PART_CNAME").toUpperCase())
                    .append("%'");
        }

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        return list;
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 查询账户
     */
    public Map<String, Object> getAccount(String dealerId, String parentId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" ACCOUNT_ID, ACCOUNT_KIND, CHILDORG_ID, DEALER_ID, DEALER_CODE, DEALER_NAME, PARENTORG_ID, PARENTORG_CODE, PARENTORG_NAME, to_char(ACCOUNT_SUM,'FM999,999,999,999,990.00') as ACCOUNT_SUM, to_char(ACCOUNT_DJ,'FM999,999,999,999,990.00') as ACCOUNT_DJ, to_char(ACCOUNT_KY,'FM999,999,999,999,990.00') as ACCOUNT_KY, ACCOUNT_INVO ");
        sql.append(" from VW_PART_DLR_ACCOUNT a ");
        sql.append(" where 1=1 ");
        sql.append(" and DEALER_ID =").append(dealerId);
        sql.append(" and PARENTORG_ID=").append(parentId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
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
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 查询账户
     */
    public Map<String, Object> getPartItemStock(String whId, String partId) {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        String orgId = CommonUtils.checkNull(request.getAttribute("orgId"));
        StringBuffer sql = new StringBuffer();
        sql.append("\n select * from vw_part_stock t ");
        sql.append(" where 1=1 ");
        sql.append(" and t.PART_ID ='").append(partId).append("'");
        sql.append(" and t.WH_ID='").append(whId).append("'");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
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
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 查询配件销售主订单
     */
    public Map<String, Object> queryPartDlrOrderMain(String orderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT B.NAME,\n");
        sql.append("       T.ORDER_ID,\n");
        sql.append("       T.ACTIVITY_CODE,\n");
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
        sql.append("       ADDR.ADDR_ID,\n");
        sql.append("       nvl(ADDR.ADDR,'无') ADDR,\n");
        sql.append("       nvl(ADDR.LINKMAN,'配件联系人') RECEIVER,\n");
        sql.append("       nvl(ADDR.TEL,'无') TEL,\n");
        sql.append("       ADDR.POST_CODE,\n");
        sql.append("       ADDR.STATION,\n");
        sql.append("       T.TRANS_TYPE,\n");
        sql.append("       TO_CHAR(nvl(T.ACCOUNT_SUM,0), 'FM999,999,999,999,990.00') AS ACCOUNT_SUM,\n");
        sql.append("       TO_CHAR(nvl(T.ACCOUNT_KY,0), 'FM999,999,999,999,990.00') AS ACCOUNT_KY,\n");
        sql.append("       TO_CHAR(nvl(T.ACCOUNT_DJ,0), 'FM999,999,999,999,990.00') AS ACCOUNT_DJ,\n");
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
        sql.append("       T.IS_TRANSFREE,\n");
        sql.append("       T.ACTIVITY_CODE,\n");
        sql.append("       T.WULIU_CODE,\n");
        sql.append("       T.WULIU_COMPANY,\n");
        sql.append("       (SELECT DISTINCT D.PART_TYPE\n");
        sql.append("          FROM TT_PART_SPECIAL_DEFINE D\n");
        sql.append("         WHERE D.ACTIVITY_CODE = T.ACTIVITY_CODE\n");
        sql.append("           AND ROWNUM = 1) PART_TYPE\n");
        sql.append(" FROM TT_PART_DLR_ORDER_MAIN T, TT_PART_ADDR_DEFINE ADDR, TC_USER B\n");
        sql.append(" WHERE T.RCV_ORGID = ADDR.DEALER_ID\n");
        sql.append("  AND t.addr_ID = addr.addr_id\n");
        sql.append("  AND T.CREATE_BY = B.USER_ID\n");
        sql.append("  AND t.order_id=").append(orderId);
        sql.append(" order by create_date desc");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
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
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 查询配件销售订单详细
     */

    public List<Map<String, Object>> queryPartDlrOrderDetail(String orderId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();

        sql.append("WITH ASWR AS\n");
        sql.append(" (SELECT TPD.PART_ID,\n");
        sql.append("         D.DEALER_ID,\n");
        sql.append("         SUM((SELECT COUNT(P.ID)\n");
        sql.append("               FROM TT_AS_REPAIR_ORDER O，TT_AS_RO_REPAIR_PART P\n");
        sql.append("              WHERE O.RO_STATUS != 11591003\n");
        sql.append("                AND O.ID = P.RO_ID(+)\n");
        sql.append("                AND O.CAM_CODE = A. ACTIVITY_CODE\n");
        sql.append("                AND O.DEALER_ID = D.DEALER_ID)) AS WR_QTY\n");
        sql.append("    FROM TT_AS_WR_PARTSITEM_RAPLCE F,\n");
        sql.append("         TT_AS_ACTIVITY            A,\n");
        sql.append("         TT_AS_ACTIVITY_DEALER     D,\n");
        sql.append("         TT_PART_DEFINE            TPD\n");
        sql.append("   WHERE A.ACTIVITY_ID = F.ID(+)\n");
        sql.append("     AND D.ACTIVITY_ID = A.ACTIVITY_ID\n");
        sql.append("     AND TPD.PART_OLDCODE = F.PART_CODE\n");
        sql.append("     AND A.ACTIVITY_TYPE = 10561005\n");
        sql.append("     AND F.REAL_PART_ID IS NOT NULL\n");
        sql.append("     AND EXISTS\n");
        sql.append("   (SELECT 1\n");
        sql.append("            FROM TT_PART_SPECIAL_DEFINE SD\n");
        sql.append("           WHERE EXISTS (SELECT 1\n");
        sql.append("                    FROM TT_PART_DLR_ORDER_MAIN OM\n");
        sql.append("                   WHERE OM.ORDER_ID = " + orderId + "\n");
        sql.append("                     AND OM.ACTIVITY_CODE = SD.BAND_ACTICODE)\n");
        sql.append("             AND SD.DEF_ID = F.REAL_PART_ID)\n");
        sql.append("     AND EXISTS (SELECT 1\n");
        sql.append("            FROM TT_PART_DLR_ORDER_MAIN N\n");
        sql.append("           WHERE D.DEALER_ID = N.DEALER_ID\n");
        sql.append("             AND N.ORDER_ID = " + orderId + ")\n");
        sql.append("   GROUP BY TPD.PART_ID, D.DEALER_ID),");
        sql.append("PT AS\n");
        sql.append(" (SELECT SM.DEALER_ID, SD.PART_ID, SUM(NVL(SD.SALES_QTY, 0)) SALES_QTY\n");
        sql.append("    FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
        sql.append("   WHERE SD.SO_ID = SM.SO_ID\n");
        sql.append("     AND SM.ORDER_TYPE = 92151010\n");
        sql.append("     AND EXISTS\n");
        sql.append("   (SELECT M.ORDER_ID\n");
        sql.append("            FROM TT_PART_DLR_ORDER_MAIN M\n");
        sql.append("           WHERE M.ORDER_ID = SM.ORDER_ID\n");
        sql.append("             AND M.ORDER_ID = " + orderId + "\n");
        sql.append("          UNION\n");
        sql.append("          SELECT M.ORDER_ID\n");
        sql.append("            FROM TT_PART_DLR_ORDER_MAIN M\n");
        sql.append("           WHERE M.ORDER_ID = SM.ORDER_ID\n");
        sql.append("             AND EXISTS\n");
        sql.append("           (SELECT 1\n");
        sql.append("                    FROM TT_PART_SPECIAL_DEFINE SD\n");
        sql.append("                   WHERE EXISTS (SELECT 1\n");
        sql.append("                            FROM TT_PART_DLR_ORDER_MAIN OM\n");
        sql.append("                           WHERE OM.ORDER_ID = " + orderId + "\n");
        sql.append("                             AND OM.ACTIVITY_CODE = SD.BAND_ACTICODE)\n");
        sql.append("                     AND SD.ACTIVITY_CODE = M.ACTIVITY_CODE)\n");
        sql.append("             AND EXISTS (SELECT 1\n");
        sql.append("                    FROM TT_PART_DLR_ORDER_MAIN N\n");
        sql.append("                   WHERE M.DEALER_ID = N.DEALER_ID\n");
        sql.append("                     AND N.ORDER_ID = " + orderId + "))\n");
        sql.append("   GROUP BY SM.DEALER_ID, SD.PART_ID)");
        sql.append(" select ");
        sql.append(" T.CHECK_QTY, ");
        sql.append(" P.SALES_QTY, ");
        sql.append(" NVL(A.WR_QTY, 0) WR_QTY, ");
        sql.append(" M.WULIU_CODE,M.WULIU_COMPANY,t.ISNEED_FLAG,t.CHECK_QTY,t.BACKHAUL_QTY,t.WAREHOUSING_QTY, T.BACKHAUL_QTY-T.WAREHOUSING_QTY ys,t.line_id ,t.order_id ,t.line_no ,t.part_id ,t.part_code ,t.part_oldcode ,"
                + "  t.part_cname,replaced.part_cname as repart_name,replaced.part_oldcode as repart_oldcode,replaced.part_id as repart_id, "
                + "  replaced.part_code as repart_code, ");
        sql.append(" t.unit ,t.is_direct ,t.is_plan ,t.is_lack ,t.is_replaced  ,t.min_package ,t.buy_qty ,t.sales_qty,  ");
        sql.append(" to_char(t.buy_price,'FM999,999,999,999,990.00') as buy_price,to_char(t.buy_amount,'FM999,999,999,999,990.00') as buy_amount ,t.is_hava ,t.remark ,t.create_date ,t.create_by ,t.update_date ,t.update_by ,t.disable_date, ");
        sql.append(" NVL((SELECT SUM(S.NORMAL_QTY) ");
        sql.append("  FROM vw_PART_STOCK S\n");
        sql.append("  WHERE S.PART_ID = t.REPART_ID\n");
        sql.append("  AND S.ORG_ID = '").append(dealerId).append("'");
        if (Constant.OEM_ACTIVITIES.equals(dealerId)) {
            sql.append("  AND S.WH_TYPE = 1 ");
        }
        sql.append("  AND S.STATUS = 1), 0) AS repart_stock_qty,\n");
        sql.append(" NVL((SELECT SUM(S.NORMAL_QTY) ");
        sql.append("  FROM vw_PART_STOCK S\n");
        sql.append("  WHERE S.PART_ID = t.PART_ID\n");
        sql.append("  AND S.ORG_ID = '").append(dealerId).append("'");
        if (Constant.OEM_ACTIVITIES.equals(dealerId)) {
            sql.append("  AND S.WH_TYPE = 1 ");
        }
        sql.append("  AND S.STATUS = 1), 0) AS stock_qty,\n");
        sql.append(" t.disable_by ,t.delete_date ,t.delete_by ,t.reserved_date ,t.ver ,t.status ,  ");
        sql.append(" buy_price SALE_PRICE1, ");
        sql.append(" b.IS_GIFT,t.deft_id,t.Nbacked_Qty,  ");
        sql.append("(NVL(P.SALES_QTY, 0) - NVL(A.WR_QTY, 0) - NVL(T.BACKHAUL_QTY, 0)) DEF_BACK_QTY， ");
        sql.append(" T.NBACK_QTY,\n");
        sql.append(" T.NINSTOCK_QTY\n");
        sql.append(" FROM TT_PART_DLR_ORDER_DTL T ");
        sql.append("   JOIN TT_PART_DLR_ORDER_MAIN M ");
        sql.append("     ON T.ORDER_ID = M.ORDER_ID ");
        sql.append("   LEFT JOIN ASWR A ");
        sql.append("     ON T.PART_ID = A.PART_ID ");
        sql.append("    AND A.DEALER_ID = M.DEALER_ID ");
        sql.append("   LEFT JOIN PT P ");
        sql.append("     ON T.REPART_ID = P.PART_ID ");
        sql.append("    AND P.DEALER_ID = M.DEALER_ID ");
        sql.append("   JOIN TT_PART_DEFINE B ");
        sql.append("     ON T.PART_ID = B.PART_ID ");
        sql.append("  JOIN TT_PART_DEFINE REPLACED ");
        sql.append("    ON T.REPART_ID = REPLACED.PART_ID ");
        //sql.append("    AND (NVL(P.SALES_QTY, 0) - NVL(A.WR_QTY, 0) - NVL(T.BACKHAUL_QTY, 0)) > 0 ");
        sql.append(" where t.ORDER_ID=").append(orderId);
        // sql.append(" AND t.backhaul_qty<>t.warehousing_qty");
        sql.append(" order by t.part_oldcode  ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        return list;
    }


    /**
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> queryPartDlrOrderDetail2(String orderId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.CHECK_QTY,\n");
        sql.append("       M.WULIU_CODE,\n");
        sql.append("       M.WULIU_COMPANY,\n");
        sql.append("       T.ISNEED_FLAG,\n");
        sql.append("       T.CHECK_QTY,\n");
        sql.append("       T.BACKHAUL_QTY,\n");
        sql.append("       T.WAREHOUSING_QTY,\n");
        sql.append("       T.BACKHAUL_QTY - T.WAREHOUSING_QTY YS,\n");
        sql.append("       T.LINE_ID,\n");
        sql.append("       T.ORDER_ID,\n");
        sql.append("       T.LINE_NO,\n");
        sql.append("       T.PART_ID,\n");
        sql.append("       T.PART_CODE,\n");
        sql.append("       T.PART_OLDCODE,\n");
        sql.append("       T.PART_CNAME,\n");
        sql.append("       REPLACED.PART_CNAME AS REPART_NAME,\n");
        sql.append("       REPLACED.PART_OLDCODE AS REPART_OLDCODE,\n");
        sql.append("       REPLACED.PART_ID AS REPART_ID,\n");
        sql.append("       REPLACED.PART_CODE AS REPART_CODE,\n");
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
        sql.append("       NVL((SELECT SUM(S.NORMAL_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = T.REPART_ID\n");
        sql.append("              AND S.ORG_ID = '2010010100070674'\n");
        sql.append("              AND S.WH_TYPE = 1\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS REPART_STOCK_QTY,\n");
        sql.append("       NVL((SELECT SUM(S.NORMAL_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = T.PART_ID\n");
        sql.append("              AND S.ORG_ID = '2010010100070674'\n");
        sql.append("              AND S.WH_TYPE = 1\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS STOCK_QTY,\n");
        sql.append("       T.DISABLE_BY,\n");
        sql.append("       T.DELETE_DATE,\n");
        sql.append("       T.DELETE_BY,\n");
        sql.append("       T.RESERVED_DATE,\n");
        sql.append("       T.VER,\n");
        sql.append("       T.STATUS,\n");
        sql.append("       BUY_PRICE SALE_PRICE1,\n");
        sql.append("       B.IS_GIFT,\n");
        sql.append("       T.DEFT_ID,\n");
        sql.append("       1  Flag\n");
        sql.append("  FROM TT_PART_DLR_ORDER_DTL T\n");
        sql.append("  JOIN TT_PART_DLR_ORDER_MAIN M\n");
        sql.append("    ON T.ORDER_ID = M.ORDER_ID\n");
        sql.append("  JOIN TT_PART_DEFINE B\n");
        sql.append("    ON T.PART_ID = B.PART_ID\n");
        sql.append("  JOIN TT_PART_DEFINE REPLACED\n");
        sql.append("    ON T.REPART_ID = REPLACED.PART_ID\n");
        sql.append(" WHERE T.ORDER_ID = " + orderId + "\n");
        sql.append("   AND T.CHECK_QTY > 0\n");
        sql.append("   AND T.BACKHAUL_QTY > 0\n");
        sql.append("--ORDER BY T.PART_OLDCODE\n");
        sql.append("UNION ALL\n");
        sql.append("SELECT T.CHECK_QTY,\n");
        sql.append("       M.WULIU_CODE,\n");
        sql.append("       M.WULIU_COMPANY,\n");
        sql.append("       T.ISNEED_FLAG,\n");
        sql.append("       T.CHECK_QTY,\n");
        sql.append("       T.NBACK_QTY BACKHAUL_QTY,\n");
        sql.append("       nvl(T.NINSTOCK_QTY,0) WAREHOUSING_QTY,\n");
        sql.append("       T.NBACK_QTY - T.NINSTOCK_QTY YS,\n");
        sql.append("       T.LINE_ID,\n");
        sql.append("       T.ORDER_ID,\n");
        sql.append("       T.LINE_NO,\n");
        sql.append("       B.PART_ID,\n");
        sql.append("       B.PART_CODE,\n");
        sql.append("       B.PART_OLDCODE,\n");
        sql.append("       B.PART_CNAME,\n");
        sql.append("       REPLACED.PART_CNAME AS REPART_NAME,\n");
        sql.append("       REPLACED.PART_OLDCODE AS REPART_OLDCODE,\n");
        sql.append("       REPLACED.PART_ID AS REPART_ID,\n");
        sql.append("       REPLACED.PART_CODE AS REPART_CODE,\n");
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
        sql.append("       NVL((SELECT SUM(S.NORMAL_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = T.REPART_ID\n");
        sql.append("              AND S.ORG_ID = '2010010100070674'\n");
        sql.append("              AND S.WH_TYPE = 1\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS REPART_STOCK_QTY,\n");
        sql.append("       NVL((SELECT SUM(S.NORMAL_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("            WHERE S.PART_ID = T.PART_ID\n");
        sql.append("              AND S.ORG_ID = '2010010100070674'\n");
        sql.append("              AND S.WH_TYPE = 1\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS STOCK_QTY,\n");
        sql.append("       T.DISABLE_BY,\n");
        sql.append("       T.DELETE_DATE,\n");
        sql.append("       T.DELETE_BY,\n");
        sql.append("       T.RESERVED_DATE,\n");
        sql.append("       T.VER,\n");
        sql.append("       T.STATUS,\n");
        sql.append("       BUY_PRICE SALE_PRICE1,\n");
        sql.append("       B.IS_GIFT,\n");
        sql.append("       T.DEFT_ID,\n");
        sql.append("       2 flag\n");
        sql.append("  FROM TT_PART_DLR_ORDER_DTL T\n");
        sql.append("  JOIN TT_PART_DLR_ORDER_MAIN M\n");
        sql.append("    ON T.ORDER_ID = M.ORDER_ID\n");
        sql.append("  JOIN TT_PART_DEFINE B\n");
        sql.append("    ON T.REPART_ID = B.PART_ID\n");//
        sql.append("  JOIN TT_PART_DEFINE REPLACED\n");
        sql.append("    ON T.REPART_ID = REPLACED.PART_ID\n");
        sql.append(" WHERE T.ORDER_ID = " + orderId + "\n");
        sql.append("   AND T.CHECK_QTY > 0\n");
        sql.append("   AND T.NBACK_QTY > 0\n");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        return list;
    }

    /**
     * @param : @param req
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 查询订单操作记录
     */
    public List<Map<String, Object>> queryOrderHistory(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select OPT_ID,BUSSINESS_ID,OPT_BY,OPT_NAME,to_char(OPT_DATE,'yyyy-mm-dd hh24:mi:ss') as OPT_DATE,WHAT,STATUS,OPT_TYPE,REMARK,ORDER_ID from TT_PART_OPERATION_HISTORY ");
        sql.append(" where 1=1 ");
        // sql.append(" and OPT_TYPE='").append(Constant.PART_OPERATION_TYPE_01).append("'");
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
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 获取版本号
     */
    public String getVersion(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select VER from TT_PART_DLR_ORDER_MAIN where ORDER_ID='");
        sql.append(orderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return "";
        }
        return list.get(0).get("VER").toString();
    }

    public Map<String, Object> getAccountMoney(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT R.PARENTORG_ID,R.PARENTORG_NAME,R.PARENTORG_CODE,nvl(b.ACCOUNT_ID,'') AS ACCOUNT_ID,to_char(nvl(ACCOUNT_SUM,'0'),'FM999,999,999,999,990.00') AS ACCOUNT_SUM,to_char(nvl(ACCOUNT_DJ,'0'),'FM999,999,999,999,990.00') AS ACCOUNT_DJ,to_char(nvl(ACCOUNT_KY,'0'),'FM999,999,999,999,990.00') AS ACCOUNT_KY FROM tt_part_sales_relation r ");
        sql.append(" left join VW_PART_DLR_ACCOUNT b on b.DEALER_ID='")
                .append(dealerId)
                .append("' and r.PARENTORG_ID=b.PARENTORG_ID ");
        sql.append(" WHERE r.childorg_id='").append(dealerId)
                .append("' AND r.state='").append(Constant.STATUS_ENABLE)
                .append("' AND r.status='1' ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @param : @param
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-12
     * @Title :
     * @Description: 获取上级机构ID
     */
    public Map<String, Object> getParentOrgId(String childOrgId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_sales_relation  ");
        sql.append(" where 1=1 ");
        sql.append(" and childorg_id='").append(childOrgId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
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

    public List<Map<String, Object>> getStoVender(String deftId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_sto_define where deft_id='")
                .append(deftId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public String getFreight(String dealerId, String orderType,
                             String orderAmount) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" to_char(nvl(PKG_PART.F_GETTRNASEXP('" + dealerId + "','"
                + orderType + "','" + orderAmount
                + "'),'0'),'FM999,999,999,999,990.00') as freight ");
        sql.append(" from dual ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null
                || list.get(0).get("FREIGHT") == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("FREIGHT"));
    }

    public List<Map<String, Object>> getAddr(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                " select  nvl(a.ADDR_ID,'') as ADDR_ID,nvl(a.ADDR,'') as ADDR,nvl(a.LINKMAN,'') as LINKMAN,nvl(TEL,'') as TEL,nvl(a.LINKMAN2,'') as LINKMAN2,nvl(TEL2,'') as TEL2,nvl(a.POST_CODE,'') as POST_CODE,nvl(a.STATION,'') as STATION  from TT_PART_ADDR_DEFINE a where dealer_id='")
                .append(dealerId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public Double getBuyPrice(String dealerId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" TO_CHAR(PKG_PART.F_GETPRICE('").append(dealerId)
                .append("','").append(partId)
                .append("'）,'FM999,999,990.00') AS PRICE ");
        sql.append(" from dual ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0)
                || list.get(0).get("PRICE") == null) {
            return 0D;
        }
        return Double.valueOf(CommonUtils.checkNull(list.get(0).get("PRICE")));
    }

    public Double getOrderPrice(String orderId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append("  buy_price  ");
        sql.append(" from tt_part_dlr_order_dtl where order_id='")
                .append(orderId).append("' and part_id ='").append(partId)
                .append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0)
                || list.get(0).get("BUY_PRICE") == null) {
            return 0D;
        }
        return Double.valueOf(CommonUtils.checkNull(list.get(0)
                .get("BUY_PRICE")));
    }

    public List<Map<String, Object>> getWareHouse(String orgId, String proFac) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT Distinct d.wh_id,d.wh_name\n");
        sbSql.append("  FROM TT_PART_PLANER_WH_RELATION R, TT_PART_WAREHOUSE_DEFINE D\n");
        sbSql.append(" WHERE R.WH_ID = D.WH_ID\n");
        sbSql.append("    AND D.org_Id = " + orgId);
        sbSql.append("    AND r.state =" + Constant.STATUS_ENABLE);
        // sbSql.append(" and D.PRODUCE_FAC='").append(proFac).append("'");
        sbSql.append(" union   ");
        sbSql.append("SELECT Distinct d.wh_id,d.wh_name\n");
        sbSql.append("  FROM TT_PART_WAREHOUSE_DEFINE D\n");
        sbSql.append(" WHERE ");
        sbSql.append("   D.org_Id = " + orgId);
        sbSql.append("    AND D.state =" + Constant.STATUS_ENABLE);
        // 判断是否为车厂 PartWareHouseDao
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String dealerId = "";
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        if (Constant.OEM_ACTIVITIES.equals(dealerId)) {
            sbSql.append("    AND D.WH_TYPE =1");
        }
        // sbSql.append(" and PRODUCE_FAC='").append(proFac).append("'");
        List<Map<String, Object>> wareHoustList = this.pageQuery(
                sbSql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    public Map<String, Object> getAddrMap(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" nvl(a.LINKMAN2,'') as LINKMAN,nvl(TEL2,'') as TEL");
        sql.append(" from TT_PART_ADDR_DEFINE a");
        sql.append(" where 1=1 ");
        sql.append(" and STATE='").append(Constant.STATUS_ENABLE)
                .append("' AND status='1' ");
        sql.append(" and DEALER_ID='").append(dealerId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0)) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public void saveUploadData(RequestWrapper req, String seq) {
        String sellerId = CommonUtils.checkNull(req.getParamValue("SELLER_ID"));
        StringBuffer sql = new StringBuffer();
        String produceFac = CommonUtils.checkNull(req
                .getParamValue("produceFac"));
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
        sql.append("              AND S.ORG_ID = '")
                .append(req.getAttribute("bookDealerId")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES)) { // 主机厂按照最小包装量量销售
            sql.append("CEIL(sum(B.QTY) / NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                          FROM TM_DEALER D\n");
            sql.append("                         WHERE D.DEALER_ID =").append(
                    req.getAttribute("bookDealerId"));
            sql.append("                           AND D.STATUS = 10011001),\n");
            sql.append("                        92101001,\n");
            sql.append("                        A.MIN_PACK1,\n");
            sql.append("                        A.MIN_PACK2),\n");
            sql.append("                 0)) *\n");
            sql.append("NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("             FROM TM_DEALER D\n");
            sql.append("            WHERE D.DEALER_ID = ").append(
                    req.getAttribute("bookDealerId"));
            sql.append("              AND D.STATUS = 10011001),\n");
            sql.append("           92101001,\n");
            sql.append("           A.MIN_PACK1,\n");
            sql.append("           A.MIN_PACK2),\n");
            sql.append("    0) AS BUYQTY,\n");
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                    FROM TM_DEALER D\n");
            sql.append("                   WHERE D.DEALER_ID = ").append(
                    req.getAttribute("bookDealerId"));
            sql.append("                     AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("CEIL(sum(B.QTY) / nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1))*nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) AS BUYQTY,\n");
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) as MIN_PACKAGE,\n");// 供应中心油品整箱，其他1
        }
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
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
        sql.append("           AND E.ORG_ID = ").append(sellerId)
                .append(") AS UPORGSTOCK,\n");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("            WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("              AND ORG_ID ='")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append("'),'0') as qty\n");
        sql.append(" ,sysdate, B.REMARK ");
        sql.append("  FROM TT_PART_DEFINE A\n");
        sql.append(" join Tmp_Part_Upload B on a.part_oldcode=upper(trim(b.part_oldcode))  ");
        sql.append(" WHERE A.STATUS = 1\n");
        // 广宣订单
        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")
                .equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append(" and A.IS_GXP = " + Constant.IF_TYPE_YES);
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                sql.append(" and A.IS_SPECIAL ='").append(Constant.IF_TYPE_YES)
                        .append("'");
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                sql.append(
                        " and A.part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='")
                        .append(CommonUtils.checkNull(req
                                .getParamValue("brand"))).append("')");
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%")
                    .append(req.getParamValue("PART_CODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%")
                    .append(req.getParamValue("PART_OLDCODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%")
                    .append(req.getParamValue("PART_CNAME").toUpperCase())
                    .append("%'");
        }
        // 广宣不需要以下验证
        if (!(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")
                .equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append("AND NOT EXISTS (SELECT 1\n");
            sql.append("   FROM VW_PART_STOCK S\n");
            sql.append("  WHERE S.PDSTATE = 10011002\n");
            sql.append("    AND S.ORG_ID = " + sellerId + "\n");
            sql.append("    AND S.PART_ID = A.PART_ID\n");
            sql.append("    AND S.NORMAL_QTY = 0)\n");
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

    public PageResult<Map<String, Object>> showPartBaseForUpload(
            RequestWrapper req, String sellerId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        // String produceFac =
        // CommonUtils.checkNull(req.getParamValue("produceFac"));

        sql.append("SELECT distinct REPLACED.REPART_OLDCODE,REPLACED.REPART_NAME,REPLACED.REPART_ID,A.PART_ID,\n");
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
        sql.append("              AND S.ORG_ID = '")
                .append(req.getAttribute("bookDealerId")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        sql.append("           B.QTY BUYQTY,\n");
        sql.append("           B.REMARK,\n");
        if (req.getParamValue("SELLER_ID").equals(Constant.OEM_ACTIVITIES)) { // 主机厂按照最小包装量量销售
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                    FROM TM_DEALER D\n");
            sql.append("                   WHERE D.DEALER_ID = ").append(
                    req.getAttribute("bookDealerId"));
            sql.append("                     AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) as MIN_PACKAGE,\n");// 供应中心油品整箱，其他1
        }
        sql.append("       ROUND(B.QTY*(PKG_PART.F_GETPRICE(")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append(", A.PART_ID)),2) AS  BUY_AMOUNT,");
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
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
        sql.append("           AND E.ORG_ID = ").append(sellerId)
                .append(") AS UPORGSTOCK,\n");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("            WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("              AND ORG_ID ='")
                .append(CommonUtils.checkNull(req.getAttribute("bookDealerId")))
                .append("'),'0') as qty\n");
        sql.append("  FROM TT_PART_DEFINE A\n");
        sql.append(" join TT_PART_SPECIAL_DEFINE REPLACED on A.PART_ID = REPLACED.PART_ID  ");
        sql.append(" join Tmp_Part_Upload B on a.part_oldcode=upper(trim(b.part_oldcode))  ");
        sql.append(" WHERE A.STATUS = 1\n");
        sql.append("  and REPLACED.START_DATE<= trunc(sysdate) \n");
        sql.append("  and REPLACED.END_DATE>= trunc(sysdate) \n");
        sql.append("  and REPLACED.ACTIVITY_TYPE =  ")
                .append(Constant.PART_ACTIVITY_TYPE_REPLACED_01).append(" \n");
        // sql.append(" and  A.produce_Fac='").append(produceFac).append("'");
        // 广宣品
        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")
                .equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append(" and A.IS_GXP = " + Constant.IF_TYPE_YES + "\n");

        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                sql.append(" and A.IS_PLAN ='").append(Constant.IF_TYPE_YES)
                        .append("'");
            }
            if (("" + Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04)
                    .equals(CommonUtils.checkNull(req
                            .getParamValue("ORDER_TYPE")))) {
                sql.append(
                        " and A.part_id in (select part_id from tt_part_STO_DEFINE tpsd where a.part_id=tpsd.part_id and tpsd.BRAND='")
                        .append(CommonUtils.checkNull(req
                                .getParamValue("brand"))).append("')");
                /*
                 * sql.append(" and A.IS_DIRECT ='").append(Constant.IF_TYPE_YES)
				 * .append("'");
				 */
            }
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CODE")))) {
            sql.append(" and upper(a.PART_CODE) like '%")
                    .append(req.getParamValue("PART_CODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")))) {
            sql.append(" and upper(a.PART_OLDCODE) like '%")
                    .append(req.getParamValue("PART_OLDCODE").toUpperCase())
                    .append("%'");
        }
        if (!"".equals(CommonUtils.checkNull(req.getParamValue("PART_CNAME")))) {
            sql.append(" and upper(a.PART_CNAME) like '%")
                    .append(req.getParamValue("PART_CNAME").toUpperCase())
                    .append("%'");
        }
        if (!(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")
                .equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {
            sql.append("AND NOT EXISTS (SELECT 1\n");
            sql.append("          FROM VW_PART_STOCK S\n");
            sql.append("         WHERE S.PDSTATE = 10011002\n");
            sql.append("           AND S.ORG_ID = " + sellerId + "\n");
            sql.append("           AND S.PART_ID = A.PART_ID\n");
            sql.append("           AND S.NORMAL_QTY = 0)\n");
        }
        // sql.append(" and a.STATE='").append(Constant.STATUS_ENABLE).append("'");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getUploadList(String seq) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                " SELECT DISTINCT replaced.REPART_NAME,replaced.REPART_ID,replaced.REPART_OLDCODE,d.*,REPLACE(d.sale_price1,',')*d.buyqty buy_Amount from tt_part_sale_upload d "
                        + "join TT_PART_SPECIAL_DEFINE replaced on d.part_id = replaced.part_id where d.ul_id='")
                .append(seq).append("'");
        sql.append("  and REPLACED.START_DATE<= trunc(sysdate) \n");
        sql.append("  and REPLACED.END_DATE>= trunc(sysdate) \n");
        sql.append("  and REPLACED.ACTIVITY_TYPE =  ")
                .append(Constant.PART_ACTIVITY_TYPE_REPLACED_01).append(" \n");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        return list;
    }

    /**
     * @param userId
     * @return
     */
    public String getUserType(Long userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                "SELECT user_type AS userType FROM tt_part_userpose_define d WHERE  d.user_id=")
                .append(userId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("USERTYPE"));
    }

    public String isTransFree(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT td.is_nbdw FROM tm_dealer td WHERE td.dealer_id=")
                .append(dealerId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
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
     * @throws : LastDate : 2013-4-16
     * @Title :
     * @Description: 查询(审核)
     */
    public Map<String, Object> querySumPartDlrOrder(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String orderCode = CommonUtils.checkNull(request
                .getParamValue("ORDER_CODE"));// 订单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));// 订货单位
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("SELLER_NAME"));// 销售单位
        String sCreateDate = CommonUtils.checkNull(request
                .getParamValue("CstartDate"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request
                .getParamValue("CendDate"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request
                .getParamValue("ORDER_TYPE"));// 订单类型
        String sSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SstartDate"));// 提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SendDate"));// 提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 订单状态
        String autoPreCheck = CommonUtils.checkNull(request
                .getParamValue("autoPreCheck"));// 自动预审
        String salerId = CommonUtils
                .checkNull(request.getParamValue("salerId"));
        String planFlag = request.getParamValue("planFlag");
        String dealerCode = CommonUtils.checkNull(request
                .getParamValue("DEALER_CODE"));// 订货单位

        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        sql.append("SELECT nvl(SUM(A.ORDER_AMOUNT),0) AMOUNT, COUNT(1) XS\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN A\n");
        /*
         * sql.append(
		 * " join tt_part_salesscope_define b on a.dealer_id=b.dealer_id and b.user_id='"
		 * ).append(loginUser.getUserId()).append("'");
		 */
        sql.append(" where 1=1 ");
        if (dealerId.equals(Constant.OEM_ACTIVITIES)) { // add by yuan 2013062
            // 车厂才有限制
            if ("".equals(salerId)) {
                /*
				 * sql.append("  AND EXISTS (SELECT 1\n" +
				 * "          FROM TT_PART_SALESSCOPE_DEFINE B\n" +
				 * "         WHERE A.DEALER_ID = B.DEALER_ID\n" +
				 * "           AND B.USER_ID ="+loginUser.getUserId() +")");
				 */
            } else {
                sql.append("  AND EXISTS (SELECT 1\n"
                        + "          FROM TT_PART_SALESSCOPE_DEFINE B\n"
                        + "         WHERE A.DEALER_ID = B.DEALER_ID\n"
                        + "           AND B.USER_ID =" + salerId + ")");
            }
        }
        if (!"".equals(orderCode)) {
            sql.append(" and a.ORDER_CODE like '%").append(orderCode)
                    .append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and a.DEALER_NAME like '%").append(dealerName)
                    .append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(a.Dealer_code) like upper('%")
                    .append(dealerCode).append("%')");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and a.SELLER_NAME like '%").append(sellerName)
                    .append("%'");
        }
        // 前台已经加限制 必须要有时间范围 所以只判断一个即可
        if (!"".equals(sCreateDate)) {
            sql.append(" and a.CREATE_DATE>= to_date('").append(sCreateDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.CREATE_DATE<= to_date('").append(eCreateDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(orderType)) {
            sql.append(" and a.ORDER_TYPE=").append(orderType);
        }
        if (!"".equals(sSubmitDate)) {
            sql.append(" and a.SUBMIT_DATE>= to_date('").append(sSubmitDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
            sql.append(" and a.SUBMIT_DATE<= to_date('").append(eSubmitDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(state)) {
            sql.append(" and a.STATE='").append(state).append("'");
        }
        if (!"".equals(autoPreCheck)) {
            sql.append(" and a.IS_AUTCHK='").append(autoPreCheck).append("'");
        }
        sql.append(" and a.State in ( ");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(
                ",");
        sql.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
        sql.append(") and a.seller_id='")
                .append(CommonUtils.checkNull(dealerId)).append("'");
		/*
		 * if(getUserType(loginUser.getUserId()).equals("6")){
		 * sql.append(" AND A.create_by=").append(loginUser.getUserId()); }
		 */
        // add by yuan 20140305 过滤广宣品订单
        sql.append(" and a.ORDER_TYPE !=").append(
                Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
        // end
        sql.append(" order by a.SUBMIT_DATE ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 切换件活动查询
     *
     * @param dealerId 服务商ID
     * @return
     */
    public List<Map<String, Object>> getCurrentActCode(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT ACTIVITY_CODE FROM TT_PART_SPECIAL_DEFINE SD WHERE");
        sql.append(" TRUNC(SYSDATE) BETWEEN START_DATE AND END_DATE \n");
        sql.append(" and state = 10011001\n");
        if ("1".equals(CommonDAO.getPara(60041003 + "")) && !"".equals(dealerId)) {
            sql.append("AND EXISTS (SELECT 1\n");
            sql.append("       FROM TT_PART_SPECIAL_DLR_REL DR\n");
            sql.append("      WHERE DR.DESCRIBE_ID = SD.DESCRIBE_ID\n");
            sql.append("        AND DR.STATE = 10011001\n");
            sql.append("        AND DR.STUTUS = 1\n");
            sql.append("        AND DR.DEALER_ID = '" + dealerId + "')\n");
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        return list;
    }

    /**
     * @return
     */
    public List<Map<String, Object>> getActCode() {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT ACTIVITY_CODE from TT_PART_SPECIAL_DEFINE ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        return list;
    }

    /**
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> getActCodeByOrderId(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT ACTIVITY_CODE from TT_PART_DLR_ORDER_MAIN WHERE ORDER_ID = ");
        sql.append(orderId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        return list;
    }

    /**
     * @param Id
     * @return
     */
    public List<Map<String, Object>> getCode(String Id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * from tc_code WHERE CODE_ID = ");
        sql.append(Id);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        return list;
    }

    public List<Map<String, Object>> getPartType(String actCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT s.* from tt_part_special_define s ");
        // sql.append("  join tt_part_special_define s on M.ACTIVITY_CODE = s.ACTIVITY_CODE\n");
        sql.append(" where ACTIVITY_CODE= '").append(actCode).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
                this.getFunName());
        return list;
    }

    /**
     * @param orgId
     * @return
     */
    public List<Map<String, Object>> getWareHouse(String orgId) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT Distinct d.wh_id,d.wh_name\n");
        sbSql.append("  FROM TT_PART_WAREHOUSE_DEFINE D\n");
        sbSql.append(" WHERE D.org_Id = " + orgId);
        sbSql.append("   AND D.state =" + Constant.STATUS_ENABLE);
        sbSql.append("   AND D.WH_TYPE =3");
        List<Map<String, Object>> wareHoustList = this.pageQuery(
                sbSql.toString(), null, this.getFunName());
        return wareHoustList;
    }

    /**
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> partDlrOrderDtlQuery(
            RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
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
        String orderCode = CommonUtils.checkNull(request
                .getParamValue("ORDER_CODE"));// 订单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));// 订货单位
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("SELLER_NAME"));// 销售单位
        String sSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SSUBMIT_DATE"));// 提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request
                .getParamValue("ESUBMIT_DATE"));// 提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 订单状态
        String actiCode = CommonUtils.checkNull(request
                .getParamValue("ACTI_CODE"));// 活动编码
        String partCode = CommonUtils.checkNull(request
                .getParamValue("PART_CODE"));// 件号
        String partOldCode = CommonUtils.checkNull(request
                .getParamValue("PART_OLDCODE"));// 配件编码
        String partCname = CommonUtils.checkNull(request
                .getParamValue("PART_CNAME"));// 配件名称
        String dealerCode = CommonUtils.checkNull(request
                .getParamValue("dealerCode")); // 订货单位编码
        String dealerCode2 = CommonUtils.checkNull(request
                .getParamValue("dealer_Code")); // 订货单位编码
        String orgCode = CommonUtils
                .checkNull(request.getParamValue("orgCode")); // 大区

        sql.append("SELECT M.ORDER_CODE,\n");
        sql.append("       M.DEALER_CODE,\n");
        sql.append("       M.DEALER_NAME,\n");
        sql.append("       D.PART_OLDCODE,\n");
        sql.append("       D.PART_CNAME,\n");
        sql.append("       PD.PART_OLDCODE PART_OLDCODE2,\n");
        sql.append("       PD.PART_CNAME PART_CNAME2,\n");
        sql.append("       D.BUY_QTY,\n");
        sql.append("       D.CHECK_QTY,\n");
        sql.append("       D.WAREHOUSING_QTY,\n");
        sql.append("       D.BACKHAUL_QTY,\n");
        sql.append("      D.NBACK_QTY,\n");
        sql.append("      D.NINSTOCK_QTY,\n");
        sql.append("      D.CHECK_QTY - D.WAREHOUSING_QTY - NVL(D.NINSTOCK_QTY, 0) DIFF_QTY,\n");
        sql.append("\t\t\t m.state\n");
        sql.append("  FROM TT_PART_DLR_ORDER_DTL D, TT_PART_DLR_ORDER_MAIN M, TT_PART_DEFINE PD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND D.ORDER_ID = M.ORDER_ID\n");
        sql.append("   AND D.REPART_ID = PD.PART_ID");
        sql.append("   AND M.STATE <> 92161004\n");

        if (!"".equals(orderCode)) {
            sql.append(" and M.ORDER_CODE like '%")
                    .append(orderCode.toUpperCase()).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and M.DEALER_NAME like '%").append(dealerName)
                    .append("%'");
        }
        if (!"".equals(dealerCode2)) {
            sql.append(" and M.DEALER_CODE like '%").append(dealerCode2)
                    .append("%'");
        }
        if (!"".equals(sellerName)) {
            sql.append(" and M.SELLER_NAME like '%").append(sellerName)
                    .append("%'");
        }
        sql.append(" and M.ORDER_TYPE='")
                .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_10)
                .append("'");
        if (!"".equals(sSubmitDate) && null != sSubmitDate) {
            sql.append(" and M.SUBMIT_DATE>= to_date('").append(sSubmitDate)
                    .append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(eSubmitDate) && null != eSubmitDate) {
            sql.append(" and M.SUBMIT_DATE<= to_date('").append(eSubmitDate)
                    .append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }

        if (!"".equals(state)) {
            sql.append(" and M.state='").append(state).append("'");
        }

        if (Constant.PART_BASE_FLAG_NO.toString().equals(oemFlag)) {
//            sql.append(" and M.oem_flag='").append(oemFlag).append("'");
            sql.append(" and M.dealer_id='").append(dealerId).append("'");
        } else {
            sql.append(" and M.seller_id='").append(dealerId).append("'");
        }
        if (!"".equals(actiCode) && null != actiCode) {
            sql.append(" and M.ACTIVITY_CODE like '%").append(actiCode)
                    .append("%'");
        }
        if (!"".equals(partOldCode) && null != partOldCode) {
            sql.append("AND d.part_oldcode LIKE '%" + partOldCode + "%'");
        }
        if (!"".equals(partCode) && null != partCode) {
            sql.append("AND pd.part_code LIKE '%" + partCode + "%'");
        }
        if (!"".equals(partCname) && null != partCname) {
            sql.append("AND d.PART_CNAME LIKE '%" + partCname + "%'");
        }
        if (!"".equals(dealerCode) && null != dealerCode) {
            sql.append(" AND M.DEALER_CODE in ("
                    + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(
                    dealerCode) + ")\n");
        }
        // 增加大区限制
        if (!"".equals(orgCode) && null != orgCode) {
            sql.append("AND EXISTS (SELECT 1\n");
            sql.append("         FROM VW_ORG_DEALER_SERVICE S\n");
            sql.append("        WHERE S.DEALER_ID = M.DEALER_ID\n");
            sql.append("          AND S.Org_Code IN ("
                    + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(
                    orgCode) + "))\n");
        }
        sql.append(" order by M.create_date desc");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 获取订单服务商对应库房信息
     *
     * @param orderId 订单ID
     * @return
     */
    public List<Map<String, Object>> getOrderDLRWhid(String orderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT D.WH_ID, M.DEALER_ID, M.DEALER_CODE, M.DEALER_NAME\n");
        sql.append("  FROM TT_PART_DLR_ORDER_MAIN M, TT_PART_WAREHOUSE_DEFINE D\n");
        sql.append(" WHERE M.DEALER_ID = D.ORG_ID\n");
        sql.append("   AND M.ORDER_ID = " + orderId + "");

        return this.pageQuery(sql.toString(), null, this.getFunName());

    }


    /**
     * 验证活动编码、配件编码、切换件编码是否存在
     *
     * @param activityCode  活动编码
     * @param partOldcode   配件编码
     * @param repartOldcode 切换件编码
     * @return
     */
    public List<Map<String, Object>> checkCode(String activityCode, String partOldcode, String repartOldcode) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append(" select * \n");
        sql.append(" from tt_part_special_define d \n");
        sql.append(" where 1 = 1 \n");
        if (!"".equals(activityCode) && null != activityCode) {
            sql.append(" 	and d.activity_code = '" + activityCode + "' \n");// --活动编码
        }
        if (!"".equals(partOldcode) && null != partOldcode) {
            sql.append(" 	and d.part_oldcode = '" + partOldcode + "' \n");// --配件编码
        }
        if (!"".equals(repartOldcode) && null != repartOldcode) {
            sql.append(" 	and d.repart_oldcode= '" + repartOldcode + "' \n");// --切换件编码
        }

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * 验证服务商编码是否存在
     *
     * @param dealerCode 服务商编码
     * @return
     */
    public List<Map<String, Object>> checkDealerCode(String dealerCode) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" select *  \n");
        sql.append(" from tm_dealer td  \n");
        sql.append(" where td.dealer_type=10771002  \n");
        sql.append(" 	and td.dealer_level=10851001  \n");
        sql.append(" 	and td.dealer_code='" + dealerCode + "'   \n");//--服务商编码
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param uploadId
     * @param dealerID
     * @param activityCode
     * @return
     */
    public List<Map<String, Object>> dataExtraction(String uploadId, String dealerID, String activityCode) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT DISTINCT REPLACED.PART_TYPE,\n");
        sql.append("                REPLACED.ACTIVITY_CODE,\n");
        sql.append("                REPLACED.ISNEED_FLAG,\n");
        sql.append("                REPLACED.REPART_OLDCODE,\n");
        sql.append("                REPLACED.REPART_ID,\n");
        sql.append("                REPLACED.REPART_CODE,\n");
        sql.append("                REPLACED.REPART_NAME,\n");
        sql.append("                A.PART_ID,\n");
        sql.append("                NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("                NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("                A.PART_CODE,\n");
        sql.append("                NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("                NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("                A.PART_OLDCODE,\n");
        sql.append("                A.PART_CNAME,\n");
        sql.append("                A.STATE,\n");
        sql.append("                A.MODEL_NAME,\n");
        sql.append("                A.REMARK,\n");
        sql.append("                NVL(A.UNIT, '件') UNIT,\n");
        sql.append("                /* NVL((SELECT SUM(S.ITEM_QTY)\n");
        sql.append("                  FROM TT_PART_ITEM_STOCK S\n");
        sql.append("                 WHERE S.PART_ID = A.PART_ID\n");
        sql.append("                   AND S.ORG_ID = '2014051394712426'\n");
        sql.append("                   AND S.STATE = 10011001\n");
        sql.append("                   AND S.STATUS = 1),\n");
        sql.append("                0) AS ITEM_QTY,*/\n");
        sql.append("                NVL(DECODE((SELECT D.PDEALER_TYPE\n");
        sql.append("                             FROM TM_DEALER D\n");
        sql.append("                            WHERE D.DEALER_ID = 2014051394712426\n");
        sql.append("                              AND D.STATUS = 10011001),\n");
        sql.append("                           92101001,\n");
        sql.append("                           A.MIN_PACK1,\n");
        sql.append("                           A.MIN_PACK2),\n");
        sql.append("                    0) AS MIN_PACKAGE,\n");
        sql.append("                TO_CHAR(PKG_PART.F_GETPRICE(2014051394712426, A.PART_ID),\n");
        sql.append("                        'FM999,999,990.00') AS SALE_PRICE1,\n");
        sql.append("                /*NVL((SELECT QTY\n");
        sql.append("                  FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("                 WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("                   AND ORG_ID = '2014051394712426'),\n");
        sql.append("                '0') AS*/\n");
        sql.append("                0 QTY,\n");
        sql.append("                NVL(A.IS_SPECIAL, '10041002') AS IS_SPECIAL,\n");
        sql.append("                POU.REMARK,\n");
        sql.append("                POU.PART_NUM,\n");
        sql.append("                SP.SALE_PRICE1\n");
        sql.append("  FROM TT_PART_DEFINE         A,\n");
        sql.append("       TT_PART_SPECIAL_DEFINE REPLACED,\n");
        sql.append("       TT_PART_ORDER_UPLOAD   POU,\n");
        sql.append("       TT_PART_SALES_PRICE    SP\n");
        sql.append(" WHERE A.PART_ID = REPLACED.REPART_ID\n");
        sql.append("   AND POU.REPART_OLDCODE = A.PART_OLDCODE\n");
        sql.append("   AND POU.ACTIVITY_CODE = REPLACED.ACTIVITY_CODE\n");
        sql.append("   AND SP.PART_ID = REPLACED.PART_ID\n");
        sql.append("   AND POU.UPLOAD_ID = '" + uploadId + "'\n");
        sql.append("   AND POU.DEALER_ID = '" + dealerID + "'\n");
        sql.append("   AND POU.ACTIVITY_CODE = '" + activityCode + "'\n");
        sql.append("   AND A.STATUS = 1\n");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }


    /**
     * @param uploadId
     * @return
     */
    public List<Map<String, Object>> checkDataRepeat(String uploadId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.UPLOAD_ID,\n");
        sql.append("       T.ACTIVITY_CODE,\n");
        sql.append("       T.DEALER_CODE,\n");
        sql.append("       T.PART_OLDCODE,\n");
        sql.append("       T.REPART_OLDCODE,\n");
        sql.append("       COUNT(*)\n");
        sql.append("  FROM TT_PART_ORDER_UPLOAD T\n");
        sql.append(" WHERE UPLOAD_ID = '2015071016882164'\n");
        sql.append(" GROUP BY T.UPLOAD_ID,\n");
        sql.append("          T.ACTIVITY_CODE,\n");
        sql.append("          T.DEALER_CODE,\n");
        sql.append("          T.PART_OLDCODE,\n");
        sql.append("          T.REPART_OLDCODE\n");
        sql.append("HAVING COUNT(*) > 1\n");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param uploadId
     * @return
     */
    public List<Map<String, Object>> dealerIDQuery(String uploadId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT T.DEALER_ID, T.DEALER_CODE, T.DEALER_NAME, T.ACTIVITY_CODE\n");
        sql.append("  FROM TT_PART_ORDER_UPLOAD T\n");
        sql.append(" WHERE T.UPLOAD_ID = '" + uploadId + "'\n");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param dealerId
     * @return
     */
    public List<Map<String, Object>> dealerInfoQuery(String dealerId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append(" select *  \n");
        sql.append(" from tm_dealer td  \n");
        sql.append(" where td.dealer_id='" + dealerId + "'  \n");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param mapList
     * @param sellerId
     * @return
     */
    public List<Map<String, Object>> RepplacedPartInfo(Map<String, Object> mapList, String sellerId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT REPLACED.PART_TYPE,REPLACED.ACTIVITY_CODE,REPLACED.ISNEED_FLAG,REPLACED.REPART_OLDCODE,REPLACED.REPART_ID,REPLACED.REPART_CODE,REPLACED.REPART_NAME,A.PART_ID,\n");
        sql.append("       NVL(A.IS_REPLACED, 10041002) AS IS_REPLACED,\n");
        sql.append("       NVL(A.IS_LACK, 10041002) AS IS_LACK,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       NVL(A.IS_DIRECT, 10041002) AS IS_DIRECT,\n");
        sql.append("       NVL(A.IS_PLAN, 10041002) AS IS_PLAN,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.MODEL_NAME,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       NVL(A.UNIT, '件') UNIT,\n");
        sql.append("       NVL((SELECT SUM(S.normal_QTY)\n");
        sql.append("             FROM VW_PART_STOCK S\n");
        sql.append("             WHERE S.PART_ID = A.PART_ID\n");
        sql.append("              AND S.ORG_ID = '").append(mapList.get("DEALER_ID")).append("'");
        sql.append("              AND S.STATE = 10011001\n");
        sql.append("              AND S.STATUS = 1),\n");
        sql.append("           0) AS ITEM_QTY,\n");
        if (sellerId.equals(Constant.OEM_ACTIVITIES)) { // 主机厂按照最小包装量量销售
            sql.append("       NVL(DECODE((SELECT D.PDEALER_TYPE\n");
            sql.append("                  FROM TM_DEALER D\n");
            sql.append("                  WHERE D.DEALER_ID = ").append(mapList.get("DEALER_ID"));
            sql.append("                  AND D.STATUS = 10011001),\n");
            sql.append("                  92101001,\n");
            sql.append("                  A.MIN_PACK1,\n");
            sql.append("                  A.MIN_PACK2),\n");
            sql.append("           0) AS MIN_PACKAGE,\n");
        } else {
            sql.append("nvl(DECODE(A.Part_Material,92031006,a.min_pack1,1),1) MIN_PACKAGE,\n");// 供应中心油品整箱，其他1
        }
        sql.append("       TO_CHAR(PKG_PART.F_GETPRICE(")
                .append(CommonUtils.checkNull(mapList.get("DEALER_ID")))
                .append(", A.PART_ID),'FM999,999,990.00') AS SALE_PRICE1,");
        sql.append("       NVL((SELECT QTY\n");
        sql.append("             FROM VW_PART_DLR_THM_PER_SALE VPDTPS\n");
        sql.append("             WHERE VPDTPS.PART_ID = A.PART_ID\n");
        sql.append("             AND ORG_ID ='").append(CommonUtils.checkNull(mapList.get("DEALER_ID"))).append("'),'0') as qty,\n");
        sql.append("  nvl(a.is_special,'10041002') as is_special ");
        sql.append("  FROM TT_PART_DEFINE A\n ");
        sql.append("  JOIN TT_PART_SPECIAL_DEFINE REPLACED ON a.PART_ID=REPLACED.PART_ID  ");
        sql.append("  WHERE A.STATUS = 1\n");
        sql.append("  and REPLACED.ACTIVITY_CODE = '").append(mapList.get("ACTIVITY_CODE")).append("'\n");
//        sql.append("  and REPLACED.START_DATE<= TRUNC(SYSDATE) \n");
//        sql.append("  and REPLACED.END_DATE>= TRUNC(SYSDATE) \n");
        sql.append("  and REPLACED.ACTIVITY_TYPE =  ").append(Constant.PART_ACTIVITY_TYPE_REPLACED_01).append(" \n");
        sql.append(" and a.PART_OLDCODE ='").append(mapList.get("PART_OLDCODE")).append("'");
        sql.append(" and REPLACED.REPART_OLDCODE ='").append(mapList.get("REPART_OLDCODE")).append("'");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null,
                getFunName());
        return list;
    }


    public PageResult<Map<String, Object>> partDlrOrderSumQuery(
            RequestWrapper request, int curPage, int pageSize) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();
        String dealerId = "";
        // 判断是否为车厂 PartWareHouseDao
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
        String orderCode = CommonUtils.checkNull(request
                .getParamValue("ORDER_CODE"));// 订单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("DEALER_NAME"));// 订货单位
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("SELLER_NAME"));// 销售单位
        String sCreateDate = CommonUtils.checkNull(request
                .getParamValue("SCREATE_DATE"));// 制单日期 开始
        String eCreateDate = CommonUtils.checkNull(request
                .getParamValue("ECREATE_DATE"));// 制单日期 结束
        String sSubmitDate = CommonUtils.checkNull(request
                .getParamValue("SSUBMIT_DATE"));// 提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request
                .getParamValue("ESUBMIT_DATE"));// 提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 订单状态
        String partCname = CommonUtils.checkNull(request
                .getParamValue("PART_CNAME"));// 订单状态
        String dealerCode = CommonUtils.checkNull(request
                .getParamValue("dealerCode"));// 订单状态
        String dealerCode2 = CommonUtils.checkNull(request
                .getParamValue("DEALER_CODE"));// 订单状态
        String orgCode = CommonUtils
                .checkNull(request.getParamValue("orgCode"));// 大区

        String partOldcode = CommonUtils.checkNull(request
                .getParamValue("PART_OLDCODE"));//

        sql.append("WITH QHJ_HY AS\n");
        sql.append(" (SELECT A.DEALER_ID, A.PART_ID, SUM(PART_NUM) PART_NUM\n");
        sql.append("    FROM (SELECT M.DEALER_ID, R.PART_ID, R.PART_NUM + NVL(D.NINSTOCK_QTY, 0) PART_NUM\n");
        sql.append("            FROM TT_PART_RECORD         R,\n");
        sql.append("                 TT_PART_DLR_ORDER_DTL  D,\n");
        sql.append("                 TT_PART_DLR_ORDER_MAIN M\n");
        sql.append("           WHERE R.CONFIG_ID = 92291041\n");
        sql.append("             AND R.PART_ID = D.PART_ID\n");
        sql.append("             AND M.ORDER_CODE = R.ORDER_CODE\n");
        sql.append("             AND M.ORDER_ID = D.ORDER_ID\n");
        sql.append("             AND R.STATE = 4\n");
        sql.append("          UNION ALL\n");
        sql.append("          SELECT WA.DEALER_ID, D.PART_ID, RD.SIGN_AMOUNT PART_NUM\n");
        sql.append("            FROM TT_AS_WR_OLD_RETURNED_DETAIL RD,\n");
        sql.append("                 TT_AS_WR_APPLICATION         WA,\n");
        sql.append("                 TT_PART_DEFINE               D,\n");
        sql.append("                 TM_DEALER                    TD,\n");
        sql.append("                 TT_PART_SALES_PRICE          SP\n");
        sql.append("           WHERE RD.QHJ_FLAG = 1\n");
        sql.append("                --AND RD.IS_STATUS = 1\n");
        sql.append("             AND RD.KCDB_FLAG IN (1, 2, 3)\n");
        sql.append("             AND RD.CLAIM_ID = WA.ID\n");
        sql.append("             AND D.PART_OLDCODE = RD.PART_CODE\n");
        sql.append("             AND WA.DEALER_ID = TD.DEALER_ID\n");
        sql.append("             AND D.PART_ID = SP.PART_ID\n");
        sql.append("             AND RD.IS_MAIN_CODE = 94001001\n");
        sql.append("                --AND RD.IS_OUT = 0\n");
        sql.append("             AND RD.SIGN_AMOUNT > 0) A\n");
        sql.append("   GROUP BY A.DEALER_ID, A.PART_ID),\n");
        sql.append("QHJ_FC AS\n");
        sql.append(" (SELECT M.DEALER_ID, D.PART_ID, SUM(D.OUTSTOCK_QTY) OUTSTOCK_QTY\n");
        sql.append("    FROM TT_PART_OUTSTOCK_MAIN M, TT_PART_OUTSTOCK_DTL D\n");
        sql.append("   WHERE M.OUT_ID = D.OUT_ID\n");
        sql.append("     AND M.ORDER_TYPE = '92151010'\n");
        sql.append("   GROUP BY M.DEALER_ID, D.PART_ID)\n");
        sql.append("SELECT F.OUTSTOCK_QTY,\n");
        sql.append("       H.PART_NUM,\n");
        sql.append("       ABS(NVL(F.OUTSTOCK_QTY,0)-NVL(H.PART_NUM,0)) DIFF_QTY,\n");
        sql.append("       TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TPD.PART_OLDCODE,\n");
        sql.append("       TPD.PART_CNAME\n");
        sql.append("  FROM QHJ_FC F, QHJ_HY H, TM_DEALER TD, TT_PART_DEFINE TPD\n");
        sql.append(" WHERE F.DEALER_ID = TD.DEALER_ID(+)\n");
        sql.append("   AND F.PART_ID = TPD.PART_ID(+)\n");
        sql.append("   AND F.DEALER_ID = H.DEALER_ID(+)\n");
        sql.append("   AND F.PART_ID = H.PART_ID(+)\n");

        if (!"".equals(dealerName)) {
            sql.append(" and TD.DEALER_NAME like '%").append(dealerName)
                    .append("%'");
        }
        if (!"".equals(dealerCode2)) {
            sql.append(" and TD.DEALER_CODE like '%").append(dealerCode2)
                    .append("%'");
        }

        if (!"".equals(dealerCode) && null != dealerCode) {
            sql.append(" AND TD.DEALER_CODE in ("
                    + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(
                    dealerCode) + ")\n");
        }
        // 增加大区限制
        if (!"".equals(orgCode) && null != orgCode) {
            sql.append("AND EXISTS (SELECT 1\n");
            sql.append("         FROM VW_ORG_DEALER_SERVICE S\n");
            sql.append("        WHERE S.DEALER_ID = TD.DEALER_ID\n");
            sql.append("          AND S.Org_Code IN ("
                    + CarSubmissionQueryDao.getInstance().getSqlQueryCondition(
                    orgCode) + "))\n");
        }

        if (!"".equals(partOldcode)) {
            sql.append(" and TPD.PART_OLDCODE like '%").append(partOldcode)
                    .append("%'");
        }
        if (!"".equals(partCname)) {
            sql.append(" and TPD.part_cname like '%").append(partCname)
                    .append("%'");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }


    /**
     * 获取所有有效活动编码
     *
     * @return
     */
    public Map<String, Object> getAllActivityMap() {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT DISTINCT SD.ACTIVITY_CODE, SD.DESCRIBE_ID\n");
        sql.append("  FROM TT_PART_SPECIAL_DEFINE SD\n");
        sql.append(" WHERE SD.STATE = 10011001\n");
        sql.append("   AND SD.STATUS = 1\n");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return CommonUtils.listMap2Map(list);
    }

    /**
     * @param flag 1 配件ID 其它切换件ID
     * @return
     */
    public Map<String, Object> getAllActivityPartMap(String flag) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        if ("1".equals(flag)) {
            sql.append("SELECT DISTINCT SD.ACTIVITY_CODE||'|'||SD.PART_OLDCODE A,SD.PART_ID\n");
            sql.append("  FROM TT_PART_SPECIAL_DEFINE SD\n");
            sql.append(" WHERE SD.STATE = 10011001\n");
            sql.append("   AND SD.STATUS = 1\n");
        } else {
            sql.append("SELECT DISTINCT SD.ACTIVITY_CODE||'|'||SD.Repart_Oldcode A,SD.Repart_Id PART_ID\n");
            sql.append("  FROM TT_PART_SPECIAL_DEFINE SD\n");
            sql.append(" WHERE SD.STATE = 10011001\n");
            sql.append("   AND SD.STATUS = 1\n");
        }

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return CommonUtils.listMap2Map(list);
    }
}
