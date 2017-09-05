package com.infodms.dms.actions.parts.financeManager.dealerAccQueryManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.parts.financeManager.dealerAccContactDetailManager.partDealerAccConDetDao;
import com.infodms.dms.dao.parts.financeManager.dealerAccQueryManager.partDealerAccQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理服务商账户查询业务
 * @Description:CHANADMS
 * @Date: 2013-4-15
 * @remark
 */
public class partDealerAccQueryAction {
    public Logger logger = Logger.getLogger(partDealerAccQueryAction.class);
    private static final partDealerAccQueryDao dao = partDealerAccQueryDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    //服务商账户查询
    private static final String PART_DEALER_ACC_QUERY = "/jsp/parts/financeManager/dealerAccQueryManager/partAccountquery.jsp";//服务商账户查询首页
    private static final String DEALER_INFO = "/jsp/parts/financeManager/dealerAccQueryManager/dealerSelectSingle.jsp";//服务商选择页面
    private static final String ACC_PREE_DETAIL = "/jsp/parts/financeManager/dealerAccQueryManager/dealerAccPreeDetail.jsp";//金额预扣详情
    private static final String ACC_INV_DETAIL = "/jsp/parts/financeManager/dealerAccQueryManager/dealerAccInvDetail.jsp";//已开票金额详情
    private static final String ACC_KY_DETAIL = "/jsp/parts/financeManager/dealerAccQueryManager/dealerAccKyDetail.jsp";//服务商可用余额查询详情

    private static final String USER_ROLE_OEM = "1"; //主机厂
    private static final String USER_ROLE_PDR = "2"; //供应中心
    private static final String USER_ROLE_DLR = "3"; //服务商

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-15
     * @Title : 跳转至服务商账户查询页面
     */
    public void partDealerAccQueryInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String childOrgId = "";
            String childOrgCode = "";
            String userRole = "";//用户角色
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                userRole = USER_ROLE_OEM;

            } else {
                childOrgId = logonUser.getDealerId();
                childOrgCode = logonUser.getDealerCode();

                TmDealerPO selPo = new TmDealerPO();

                selPo.setDealerId(Long.parseLong(childOrgId));

                List<TmDealerPO> dlrList = dao.select(selPo);
                TmDealerPO resPo = null;
                if (null != dlrList && dlrList.size() == 1) {
                    resPo = dlrList.get(0);
                    int pDlrType;
                    if (null != resPo.getPdealerType() && !"".equals(resPo.getPdealerType())) {
                        pDlrType = resPo.getPdealerType();
                        if (Constant.PART_SALE_PRICE_DEALER_TYPE_01 == pDlrType) {
                            userRole = USER_ROLE_PDR;
                        } else {
                            userRole = USER_ROLE_DLR;
                        }
                    } else {
                        userRole = USER_ROLE_DLR;
                    }
                }

            }
            act.setOutData("userRole", userRole);
            act.setOutData("normalDlr", USER_ROLE_DLR);
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("childOrgId", childOrgId);
            act.setOutData("childOrgCode", childOrgCode);
            act.setForword(PART_DEALER_ACC_QUERY);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商账户查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 查询服务商账户信息
     */
    public void partDealerAccQuerySearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            //add by yuan 20130528
            if (logonUser.getDealerId() != null) {
                parentOrgId = logonUser.getDealerId();
            }
            PageResult<Map<String, Object>> ps = dao.queryPartDealerAccount(request, parentOrgId, logonUser, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询服务商账户信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title : 预扣金额详情查询
     */
    public void partDealerPreemptionDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            if ("1".equals(request.getParamValue("query"))) { // 开始查询
                String dealerId = request.getParamValue("dealerId");// 服务商ID
                String parentOrgIdHidden = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
                String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));// 开始时间
                String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));// 结束时间
                String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));// 业务单号(销售单、出库单)
                String sourceCode = CommonUtils.checkNull(request.getParamValue("sourceCode"));// 订单号
                String accountId = CommonUtils.checkNull(request.getParamValue("ACCOUNT_ID"));// 订单号

                StringBuffer sb = new StringBuffer();
                if (Utility.testString(dealerId)) {
                    sb.append(" AND UPPER(TD.DEALER_ID) = '" + dealerId.trim() + "'  \n");
                }
                if (null != sourceCode && !"".equals(sourceCode)) {
                    sb.append(" AND UPPER(AC.SOURCE_CODE) LIKE '%" + sourceCode.trim().toUpperCase() + "%' ");
                }
                if (null != orderCode && !"".equals(orderCode)) {
                    sb.append(" AND UPPER(AC.ORDER_CODE) LIKE '%" + orderCode.trim().toUpperCase() + "%' ");
                }
                if (null != startDate && !"".equals(startDate)) {
                    sb.append(" AND TO_CHAR(AC.CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "' ");
                }
                if (null != endDate && !"".equals(endDate)) {
                    sb.append(" AND TO_CHAR(AC.CREATE_DATE,'yyyy-MM-dd') <= '" + endDate + "' ");
                }
                if (null != parentOrgIdHidden && !"".equals(parentOrgIdHidden)) {
                    sb.append(" AND PA.PARENTORG_ID = '" + parentOrgIdHidden + "' ");
                }
                if (null != accountId && !"".equals(accountId)) {
                    sb.append(" AND PA.ACCOUNT_ID = '" + accountId + "' ");
                }

                sb.append(" AND AC.CHANGE_TYPE IN ('" + Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01 + "','" + Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02 + "') ");

                Integer curPage = request.getParamValue("curPage") != null ? Integer
                        .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                PageResult<Map<String, Object>> ps = dao.queryPartDealerPreemptionDetail(12, curPage, sb.toString());
                act.setOutData("ps", ps);
            } else {
                String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
                String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 服务商ID
                String accountId = CommonUtils.checkNull(request.getParamValue("ACCOUNT_ID"));//
                StringBuffer sbStr = new StringBuffer();
                if (null != dealerId && !"".equals(dealerId)) {
                    sbStr.append(" AND TD.DEALER_ID = '" + dealerId + "' ");
                }
                String dealerNameParms = dao.getDealerName(sbStr.toString());
                act.setOutData("dealerName", dealerNameParms);
                act.setOutData("dealerId", dealerId);
                act.setOutData("parentOrgId", parentOrgId);
                act.setOutData("ACCOUNT_ID", accountId);
                act.setForword(ACC_PREE_DETAIL);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "返回预扣金额详情信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-20
     * @Title : 获取已开票金额详情
     */
    public void partDealerInvAccDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");

            if ("1".equals(request.getParamValue("query"))) { // 开始查询
                String dealerId = request.getParamValue("dealerId");// 服务商ID
                String parentOrgIdHidden = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
                String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));// 开始时间
                String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));// 结束时间
//				String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));// 业务单号(销售单、出库单)
                String invNo = CommonUtils.checkNull(request.getParamValue("invNo"));// 发票号

                StringBuffer sb = new StringBuffer();
                if (Utility.testString(dealerId)) {
                    sb.append(" AND UPPER(TD.DEALER_ID) = '" + dealerId.trim() + "'  \n");
                }
//				if(null != sourceCode && !"".equals(sourceCode))
//				{
//					sb.append(" AND AC.SOURCE_CODE LIKE '%" + sourceCode + "%' ");
//				}
//				if(null != orderCode && !"".equals(orderCode))
//				{
//					sb.append(" AND UPPER(AC.ORDER_CODE) LIKE '%" + orderCode.trim().toUpperCase() + "%' ");
//				}
                if (null != invNo && !"".equals(invNo)) {
                    sb.append(" AND UPPER(AC.INVOICE_NO) LIKE '%" + invNo.trim().toUpperCase() + "%' ");
                }
                if (null != startDate && !"".equals(startDate)) {
                    sb.append(" AND TO_CHAR(AC.CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "' ");
                }
                if (null != endDate && !"".equals(endDate)) {
                    sb.append(" AND TO_CHAR(AC.CREATE_DATE,'yyyy-MM-dd') <= '" + endDate + "' ");
                }
                if (null != parentOrgIdHidden && !"".equals(parentOrgIdHidden)) {
                    sb.append(" AND PA.PARENTORG_ID = '" + parentOrgIdHidden + "' ");
                }

                sb.append(" AND AC.CHANGE_TYPE = '" + Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_03 + "' ");

                Integer curPage = request.getParamValue("curPage") != null ? Integer
                        .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                PageResult<Map<String, Object>> ps = dao.queryPartDealerPreemptionDetail(12, curPage, sb.toString());
                act.setOutData("ps", ps);
            } else {
                String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
                String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 服务商ID
                StringBuffer sbStr = new StringBuffer();
                if (null != dealerId && !"".equals(dealerId)) {
                    sbStr.append(" AND UPPER(TD.DEALER_ID) = '" + dealerId.trim() + "' ");
                }
                String dealerNameParms = dao.getDealerName(sbStr.toString());
                act.setOutData("dealerName", dealerNameParms);
                act.setOutData("dealerId", dealerId);
                act.setOutData("parentOrgId", parentOrgId);
                act.setForword(ACC_INV_DETAIL);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "返回预扣金额详情信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-20
     * @Title : 获取已开票金额详情
     */
    public void partDealerInvAccDetail2() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");

            if ("1".equals(request.getParamValue("query"))) { // 开始查询

                Integer curPage = request.getParamValue("curPage") != null ? Integer
                        .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                PageResult<Map<String, Object>> ps = dao.queryPartDealerPreemptionDetail2(request, 12, curPage);
                act.setOutData("ps", ps);
            } else {
                String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
                String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 服务商ID
                String accountId = CommonUtils.checkNull(request.getParamValue("ACCOUNT_ID"));// 发票号
                StringBuffer sbStr = new StringBuffer();
                if (null != dealerId && !"".equals(dealerId)) {
                    sbStr.append(" AND UPPER(TD.DEALER_ID) = '" + dealerId.trim() + "' ");
                }
                String dealerNameParms = dao.getDealerName(sbStr.toString());
                act.setOutData("dealerName", dealerNameParms);
                act.setOutData("dealerId", dealerId);
                act.setOutData("parentOrgId", parentOrgId);
                act.setOutData("ACCOUNT_ID", accountId);
                act.setForword(ACC_INV_DETAIL);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "返回预扣金额详情信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title :返回服务商信息
     */
    public void partDealerSelect() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer sb = new StringBuffer();
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            if ("1".equals(request.getParamValue("query"))) { // 开始查询
                String dealerCode = request.getParamValue("dealerCodeSelect");// 服务商编码
//				String dealerName = request.getParamValue("dealerNameSelect");// 服务商名称
                String parentOrgIdHidden = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
                if (Utility.testString(dealerCode)) {
                    sb.append(" and UPPER(TD.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' \n");
                }
                if (null != parentOrgIdHidden && !"".equals(parentOrgIdHidden)) {
                    sb.append(" AND PA.PARENTORG_ID = '" + parentOrgIdHidden + "' ");
                }
                Integer curPage = request.getParamValue("curPage") != null ? Integer
                        .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                PageResult<Map<String, Object>> ps = dao.getDealers(Constant.PAGE_SIZE, curPage, sb.toString());
                act.setOutData("ps", ps);
            } else {
                act.setOutData("parentOrgId", parentOrgId);
                act.setForword(DEALER_INFO);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "返回服务商信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-15
     * @Title :导出服务商账户信息
     */
    public void exportPartDealerAccountExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            if (logonUser.getDealerId() != null) {
                parentOrgId = logonUser.getDealerId();
            }
            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "服务商编码";
            head[2] = "服务商名称";
            head[3] = "销售单位编码";
            head[4] = "销售单位名称";
            head[5] = "款项类型";
            head[6] = "总可用金额";
            head[8] = "现金可用金额";
            head[7] = "已扣款金额";
            head[9] = "信用额度";
            head[10] = "现金账户余额";
            head[11] = "已开票金额";
            List<Map<String, Object>> list = dao.queryAllPartDealerAccount(request, parentOrgId);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PARENTORG_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("PARENTORG_NAME"));
                        Integer kindTemp = Integer.parseInt(CommonUtils.checkNull(map.get("ACCOUNT_PURPOSE")));
                        if (Constant.PART_ACCOUNT_PURPOSE_TYPE_01.equals(kindTemp)) {
                            detail[5] = "配件款";
                        } else if (Constant.PART_ACCOUNT_PURPOSE_TYPE_02.equals(kindTemp)) {
                            detail[5] = "精品款";
                        }
                        detail[6] = CommonUtils.checkNull(map.get("USEABLEACCOUNT"));
                        detail[8] = CommonUtils.checkNull(map.get("CASH_KY"));
                        detail[7] = CommonUtils.checkNull(map.get("PREEMPTIONVALUE"));
                        detail[9] = CommonUtils.checkNull(map.get("CREDIT_LIMIT"));
                        detail[10] = CommonUtils.checkNull(map.get("ACCOUNT_SUM"));
                        detail[11] = CommonUtils.checkNull(map.get("ACCOUNT_INVO"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "服务商账户信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出服务商账户信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title : 导出服务商资金占用详细信息
     */
    public void exportParDeaAccPreeDetExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerCode = request.getParamValue("dealerCode");// 服务商编码
            String parentOrgIdHidden = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));// 开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));// 结束时间
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));// 业务单号(销售单、出库单)
            String sourceId = CommonUtils.checkNull(request.getParamValue("sourceId"));// 订单号
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 父机构（销售单位）ID

            StringBuffer sb = new StringBuffer();
            if (Utility.testString(dealerCode)) {
                sb.append(" AND UPPER(TD.DEALER_CODE) = '" + dealerCode.trim().toUpperCase() + "' \n");
            }
            if (null != sourceId && !"".equals(sourceId)) {
                sb.append(" AND UPPER(AC.SOURCE_ID) LIKE '%" + sourceId.trim().toUpperCase() + "%' ");
            }
            if (null != orderId && !"".equals(orderId)) {
                sb.append(" AND UPPER(AC.ORDER_ID) LIKE '%" + orderId.trim().toUpperCase() + "%' ");
            }
            if (null != startDate && !"".equals(startDate)) {
                sb.append(" AND TO_CHAR(AC.CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "' ");
            }
            if (null != endDate && !"".equals(endDate)) {
                sb.append(" AND TO_CHAR(AC.CREATE_DATE,'yyyy-MM-dd') <= '" + endDate + "' ");
            }
            if (null != parentOrgIdHidden && !"".equals(parentOrgIdHidden)) {
                sb.append(" AND PA.PARENTORG_ID = '" + parentOrgIdHidden + "' ");
            }
            if (null != dealerId && !"".equals(dealerId)) {
                sb.append(" AND PA.CHILDORG_ID = '" + dealerId + "' ");
            }
            sb.append(" AND AC.CHANGE_TYPE IN ('" + Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01 + "','" + Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02 + "') ");

            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "服务商名称";
            head[2] = "订单号";
            head[3] = "业务单号";
//			head[4] = "订单金额";
//			head[5] = "开票金额";
            head[4] = "占用/释放金额(元)";
            head[5] = "变动类型";
            head[6] = "变动人";
            head[7] = "发生日期";
            List<Map<String, Object>> list = dao.queryAllPartDealerPreemptionDetail(sb.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[10];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("SOURCE_ID"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("ORDER_ID"));
                        /*detail[4] = CommonUtils.checkNull(map
                                .get("AMOUNT"));*/
                        /*detail[5] = CommonUtils.checkNull(map
                                .get("INVO_AMOUNT"));*/
                        detail[4] = CommonUtils.checkNull(map
                                .get("AMOUNT"));
                        String changeTypeTemp = CommonUtils.checkNull(map.get("CHANGE_TYPE"));
                        if (changeTypeTemp.equals(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01 + "")) {
                            detail[5] = "占用";
                        } else if (changeTypeTemp.equals(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02 + "")) {
                            detail[5] = "释放";
                        } else {
                            detail[5] = "开票";
                        }
                        detail[6] = CommonUtils.checkNull(map
                                .get("NAME"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("CREATE_DATE"));
                        list1.add(detail);
                    }
                }
            }
            String fileName = "服务商资金占用明细";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出服务商账户信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 文件导出为xls文件
     */
    public static Object exportEx(String fileName, ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = fileName + ".xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode(name, "utf-8"));
            out = response.getOutputStream();
            wwb = Workbook.createWorkbook(out);
            jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

            if (head != null && head.length > 0) {
                for (int i = 0; i < head.length; i++) {
                    ws.addCell(new Label(i, 0, head[i]));
                }
            }
            int pageSize = list.size() / 30000;
            for (int z = 1; z < list.size() + 1; z++) {
                String[] str = list.get(z - 1);
                for (int i = 0; i < str.length; i++) {
                        /*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                    if (CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])) {
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    } else {
                        ws.addCell(new Label(i, z, str[i]));
                    }
                }
            }
            wwb.write();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != wwb) {
                wwb.close();
            }
            if (null != out) {
                out.close();
            }
        }
        return null;
    }

    /**
     * 往来明细账查询
     */
    public void partDealerKyDetail2() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            if ("1".equals(request.getParamValue("query"))) { // 开始查询

                Integer curPage = request.getParamValue("curPage") != null ? Integer
                        .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                PageResult<Map<String, Object>> ps = dao.queryPartDealerKYDetail(request, Constant.PAGE_SIZE, curPage);
                act.setOutData("ps", ps);
            } else {
                String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
                String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 服务商ID
                String accountId = CommonUtils.checkNull(request.getParamValue("ACCOUNT_ID"));//
                StringBuffer sbStr = new StringBuffer();
                if (null != dealerId && !"".equals(dealerId)) {
                    sbStr.append(" AND TD.DEALER_ID = '" + dealerId + "' ");
                }
                String dealerNameParms = dao.getDealerName(sbStr.toString());
                act.setOutData("dealerName", dealerNameParms);
                act.setOutData("dealerId", dealerId);
                act.setOutData("parentOrgId", parentOrgId);
                act.setOutData("ACCOUNT_ID", accountId);
                act.setForword(ACC_KY_DETAIL);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "往来明细账查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void exportDLRKYDtl() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();

            String[] head = new String[10];
            head[0] = "序号";
//            head[1] = "服务商名称";
            head[1] = "日期";
            head[2] = "类型";
            head[3] = "摘要";
            head[4] = "销售单";
            head[5] = "入账金额";
            head[6] = "出账金额";
            head[7] = "余额";
            head[8] = "开票金额";

            PageResult<Map<String, Object>> ps = dao.queryPartDealerKYDetail(request, Constant.PAGE_SIZE_MAX, 1);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[10];
                        detail[0] = CommonUtils.checkNull(i + 1);
//                        detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[1] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[2] = CommonUtils.checkNull(map.get("CHANGE_TYPE"));
                        detail[3] = CommonUtils.checkNull(map.get("ABSTRACT"));
                        detail[4] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[5] = CommonUtils.checkNull(map.get("R_AMOUNT"));
                        detail[6] = CommonUtils.checkNull(map.get("C_AMOUNT"));
                        detail[7] = CommonUtils.checkNull(map.get("BALANCE"));
                        detail[8] = CommonUtils.checkNull(map.get("INVO_AMOUNT"));
                        list1.add(detail);
                    }
                }
            }
            String fileName = "服务商往来明细";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出服务商服务商往来明细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void exportDLRInvoDtl() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();

            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "订单号";
            head[2] = "销售单号";
            head[3] = "发运单号";
            head[4] = "开票金额";
            head[5] = "发票号";
            head[6] = "开票日期";

            PageResult<Map<String, Object>> ps = dao.queryPartDealerPreemptionDetail2(request, Constant.PAGE_SIZE_MAX, 1);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[10];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("SO_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("TRPLAN_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("INVO_AMOUNT"));
                        detail[5] = CommonUtils.checkNull(map.get("INVOICE_NO"));
                        detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        list1.add(detail);
                    }
                }
            }
            String fileName = "配件开票信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出服务商服务商往来明细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件往来明细到处
     */
    public void exportFINDtl() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        final partDealerAccConDetDao dao = partDealerAccConDetDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();

            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 服务商ID
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注(财务凭证)
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));// 开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));// 结束时间
            String invoiceNO = CommonUtils.checkNull(request.getParamValue("invoiceNO"));// 发票号
            String accountKind = CommonUtils.checkNull(request.getParamValue("accountKind"));// 资金类型

            String sqlStr = "";
            if (null != dealerId && !"".equals(dealerId)) {
                sqlStr += " AND PA.DEALER_ID = '" + dealerId + "' ";
            }
            if (null != remark && !"".equals(remark)) {
                sqlStr += " AND PA.REMARK LIKE '%" + remark + "%'";
            }
            if (null != startDate && !"".equals(startDate)) {
                sqlStr += " AND TO_CHAR(PA.CREATE_DATE,'yyyy-MM-dd') >= '" + startDate + "' ";
            }
            if (null != endDate && !"".equals(endDate)) {
                sqlStr += " AND TO_CHAR(PA.CREATE_DATE,'yyyy-MM-dd') <= '" + endDate + "' ";
            }
            if (null != accountKind && !accountKind.equals("")) {
                sqlStr += " AND PA.FIN_TYPE  = '" + accountKind + "' ";
            }
            if (null != invoiceNO && !"".equals(invoiceNO)) {
                sqlStr += " AND PA.INVOICE_NO LIKE '%" + invoiceNO + "%' ";
            }

            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "服务商编码";
            head[2] = "服务商名称";
            head[3] = "金额";
            head[4] = "往来类型";
            head[5] = "备注";
            head[6] = "日期";

            PageResult<Map<String, Object>> ps = dao.queryDealerAccConDet(Constant.PAGE_SIZE_MAX, 1, sqlStr);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[10];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("CHILDORG_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("CHILDORG_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("AMOUNT"));
                        detail[4] = CommonUtils.checkNull(map.get("FIN_TYPE"));
                        detail[5] = CommonUtils.checkNull(map.get("REMARK"));
                        detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        list1.add(detail);
                    }
                }
            }
            String fileName = "服务商配件往来明细账信息.xls";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "服务商配件往来明细账信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
