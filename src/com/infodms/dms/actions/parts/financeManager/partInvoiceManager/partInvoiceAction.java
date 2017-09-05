package com.infodms.dms.actions.parts.financeManager.partInvoiceManager;

import com.infodms.dms.actions.parts.storageManager.partStoInveManager.entityInvImpAction;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.financeManager.partInvoiceManager.partInvoiceDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartOutstockDao;
import com.infodms.dms.dao.parts.salesManager.PartSoManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.ReaderUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : huchao
 *         LastDate     : 2013-4-19
 * @ClassName : partInvoiceAction
 * @Description : 财务管理-财务金税发票
 */
public class partInvoiceAction {

    private static final partInvoiceDao dao = partInvoiceDao.getInstance();
    private static final int PAGE_SIZE = 500;
    private static final int orderType1 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01;//紧急
    private static final int orderType2 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02;//常规
    private static final int orderType3 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03;//计划
    private static final int orderType4 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04;//直发
    private static final int orderType5 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05;//调拨
    private static final int invOutState1 = Constant.PART_INVO_OUT_STATE_01;//未导出
    private static final int invOutState2 = Constant.PART_INVO_OUT_STATE_02;//已导出
    private static final int INV_WAY_01 = Constant.INVOICE_WAY_01; //正常开票
    private static final int INV_WAY_02 = Constant.INVOICE_WAY_02; //手工开票
    private static final String PART_PURPRICE_QUERY_URL = "/jsp/parts/financeManager/partInvoiceManager/partInvoice.jsp";//财务金税发票页面
    private static final String PART_OUT_ORDER_URL = "/jsp/parts/financeManager/partInvoiceManager/partOutOrderDetail.jsp";//出库单详情页面
    private static final String PART_INV_COUNT_URL = "/jsp/parts/financeManager/partInvoiceManager/expInvPreviewCount.jsp";//导出金税文本统计查询页面
    private static final String INV_BY_HANDLE = "/jsp/parts/financeManager/partInvoiceManager/invByHandlePg.jsp";//手工开票页面
    private static final String INPUT_ERROR_URL = "/jsp/parts/financeManager/partInvoiceManager/inputError.jsp";//数据导入出错页面
    private static final String INPUT_CONFIRM_URL = "/jsp/parts/financeManager/partInvoiceManager/loadInvConfirmPg.jsp";//开票导入确认页面
    public Logger logger = Logger.getLogger(entityInvImpAction.class);

    /**
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-5-3
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
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-19
     * @Title :
     * @Description: 财务金税发票页面初始化
     */
    public void partInvoiceQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
            }

            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("WHList", WHList);
            act.setForword(PART_PURPRICE_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "财务金税发票询初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-19
     * @Title : 导出金税文本查询初始化
     */
    public void expInvCountInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
            }

            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("WHList", WHList);
            act.setForword(PART_INV_COUNT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "财务导出金税文本查询初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-16
     * @Title :  查询财务金税发票信息
     */
    public void partInvoiceSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
//            String orderCode = CommonUtils.checkNull(request
//                    .getParamValue("orderCode")); // 订货单号
            String sellCode = CommonUtils.checkNull(request
                    .getParamValue("sellCode")); // 销售单号
            String outCode = CommonUtils.checkNull(request
                    .getParamValue("outCode")); // 出库单号
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 订货单位
            String sellerName = CommonUtils.checkNull(request
                    .getParamValue("sellerName")); // 销售单位
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 订单类型
            String invoOutState = CommonUtils.checkNull(request
                    .getParamValue("invoOutState")); // 金税导出状态
//            String whId = CommonUtils.checkNull(request
//                    .getParamValue("whId")); // 出库仓库ID
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 出库开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 出库截止时间
            String finChkSDate = CommonUtils.checkNull(request
                    .getParamValue("finChkSDate")); // 财务审核开始时间
            String finChkEDate = CommonUtils.checkNull(request
                    .getParamValue("finChkEDate")); // 财务审核截止时间
            String dlrInvTpe = CommonUtils.checkNull(request
                    .getParamValue("dlrInvTpe")); // 开票类型
            String invWay = CommonUtils.checkNull(request
                    .getParamValue("invWay")); // 开票方式
            String isInv = CommonUtils.checkNull(request
                    .getParamValue("isInv")); // 是否开票
            String inVo = CommonUtils.checkNull(request
                    .getParamValue("inVo")); // 发票号
            String isNbdw = CommonUtils.checkNull(request
                    .getParamValue("isNbdw")); // 是否内部单位

            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
           /* if (null != orderCode && !"".equals(orderCode)) {
                sbString.append(" AND OMM.ORDER_CODE LIKE '%" + orderCode + "%' ");
            }*/
            if (null != sellCode && !"".equals(sellCode)) {
                sbString.append(" AND OM.SO_CODE LIKE '%" + sellCode + "%' ");
            }
            if (null != outCode && !"".equals(outCode)) {
                sbString.append(" AND OM.OUT_CODE LIKE '%" + outCode + "%' ");
            }
            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND OM.DEALER_NAME LIKE '%" + dealerName + "%' ");
            }
            if (null != sellerName && !"".equals(sellerName)) {
                sbString.append(" AND OM.SELLER_NAME LIKE '%" + sellerName + "%' ");
            }
            /*if (null != whId && !"".equals(whId)) {
                sbString.append(" AND OM.WH_ID = '" + whId + "' ");
            }*/
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND OM.ORDER_TYPE = '" + orderType + "' ");
            }
            if (null != invoOutState && !"".equals(invoOutState)) {
                sbString.append(" AND OM.IS_EXPORT = '" + invoOutState + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != finChkSDate && !"".equals(finChkSDate)) {
                sbString.append(" AND TO_CHAR(SM.FCAUDIT_DATE,'yyyy-MM-dd') >= '" + finChkSDate + "' ");
            }
            if (null != finChkEDate && !"".equals(finChkEDate)) {
                sbString.append(" AND TO_CHAR(SM.FCAUDIT_DATE,'yyyy-MM-dd') <= '" + finChkEDate + "' ");
            }

            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND OM.SELLER_ID = '" + parentOrgId + "' ");
            }
            if (null != invWay && !"".equals(invWay)) {
                sbString.append(" AND OM.INV_WAY = '" + invWay + "' ");
            }
            if (null != dlrInvTpe && !"".equals(dlrInvTpe)) {
                sbString.append(" AND BD.INV_TYPE = '" + dlrInvTpe + "' ");
            }
            if (null != isInv && !"".equals(isInv)) {
                sbString.append(" AND OM.IS_INV = '" + isInv + "' ");
            }
            if (null != inVo && !"".equals(inVo)) {
                sbString.append(" AND OM.BILL_NO like '%" + inVo + "%' ");
            }
            if (null != isNbdw && !"".equals(isNbdw)) {
                if ("1".equals(isNbdw)) {
                    sbString.append(" AND EXISTS ( SELECT 1 FROM TM_DEALER D WHERE D.DEALER_ID = OM.DEALER_ID AND D.IS_NBDW = 1 ) ");
                } else if ("0".equals(isNbdw)) {
                    sbString.append(" AND EXISTS ( SELECT 1 FROM TM_DEALER D WHERE D.DEALER_ID = OM.DEALER_ID AND D.IS_NBDW IN( 0,2) ) ");
                }

            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            int pageSize = PAGE_SIZE;
            if (null != invoOutState && !"".equals(invoOutState) && null != isInv && !"".equals(isInv)) {
                int invState02 = Constant.PART_INVO_OUT_STATE_02;
                int typeYes = Constant.IF_TYPE_YES;
                if (typeYes == Integer.parseInt(isInv) && invState02 == Integer.parseInt(invoOutState)) {
                    pageSize = 100;
                }
            }
            PageResult<Map<String, Object>> ps = dao.queryPartInvoices(
                    sbString.toString(), pageSize, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询财务金税发票信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-19
     * @Title : 条件查询导出金税文本信息
     */
    public void expInvCountDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryInvCountInfos(
                    request, PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询导出金税文本信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-17
     * @Title : 手工开票
     */
    public void invByHandle() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String outIds = CommonUtils.checkNull(request
                    .getParamValue("outIds")); // 出库单ID
            String outIdsArr[] = outIds.trim().split(",");

            String soCodes = CommonUtils.checkNull(request
                    .getParamValue("soCodes")); // 出库单
            soCodes = soCodes.trim().substring(0, soCodes.length() - 1);

            String outId = "";

            for (int i = 0; i < outIdsArr.length; i++) {
                outId += "'" + outIdsArr[i] + "',";
            }

            outId = outId.substring(0, outId.length() - 1);

            String sqlStr = "";
            String amountSql = "";
            String dealerName = "";
            String invAmout = "";
            String billNo = "";
            String billBy = "";
            String invCode = "";
            String invNum = "";

            if (outIdsArr.length > 1) {
                sqlStr = " AND OM.OUT_ID = '" + outIdsArr[0] + "' ";
                amountSql = " AND OM.OUT_ID IN (";
                String temp = "";
                for (int m = 0; m < outIdsArr.length; m++) {
                    temp += "'" + outIdsArr[m] + "',";
                }
                temp = temp.substring(0, temp.length() - 1);
                amountSql += temp + ") ";

            } else {
                sqlStr = " AND OM.OUT_ID = " + outId + "";
                amountSql = " AND OM.OUT_ID = " + outId + "";
            }

            List<Map<String, Object>> outList = dao.queryOutStockInfos(sqlStr);
            List<Map<String, Object>> amountList = dao.queryOutStockInfosNew(amountSql);

            if (null != outList && outList.size() > 0) {
                dealerName = outList.get(0).get("DEALER_NAME").toString();

                if (null != outList.get(0).get("BILL_NO") && !"".equals(outList.get(0).get("BILL_NO"))) {
                    billNo = outList.get(0).get("BILL_NO").toString();
                    if (billNo.length() >= 10) {
                        invCode = billNo.substring(0, 10);
                        invNum = billNo.substring(10, billNo.length());
                    } else {
                        invCode = billNo.substring(0, billNo.length());
                        invNum = "";
                    }

                }

                if (null != outList.get(0).get("BILL_BY") && !"".equals(outList.get(0).get("BILL_BY"))) {
                    billBy = outList.get(0).get("BILL_BY").toString();
                } else {
                    billBy = logonUser.getName();
                }
            }

            if (null != amountList && amountList.size() > 0) {

                invAmout = amountList.get(0).get("INV_AMOUNT").toString();

            }

            act.setOutData("outId", outId.replace("'", ""));
            act.setOutData("soCode", soCodes);
            act.setOutData("dealerName", dealerName);
            act.setOutData("invAmout", invAmout);
            act.setOutData("billBy", billBy);
            act.setOutData("invCode", invCode);
            act.setOutData("invNum", invNum);
            act.setOutData("billNo", billNo);

            act.setForword(INV_BY_HANDLE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "手工开票页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-18
     * @Title :
     * @Description: 查看
     */
    public void viewOutOrdDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
        PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
        PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
        try {
            String outId = CommonUtils.checkNull(request.getParamValue("viewOutId"));
            Map<String, Object> mainMap = partOutstockDao.queryOutstockMain(outId);
            Map<String, Object> soMap = partSoManageDao.getSoOrderMain(CommonUtils.checkNull(mainMap.get("SO_ID")));
            String orderCreateBy = "";
            String orderCreateDate = "";
            if (Constant.CAR_FACTORY_SO_FORM_02.toString().equals(CommonUtils.checkNull(mainMap.get("SO_FROM")))) {
                String orderId = CommonUtils.checkNull(soMap.get("ORDER_ID"));
                Map<String, Object> orderMap = partDlrOrderDao.queryPartDlrOrderMain(orderId);
                orderCreateBy = CommonUtils.checkNull(orderMap.get("NAME"));
                orderCreateDate = CommonUtils.checkNull(orderMap.get("CREATE_DATE"));
            }
            List<Map<String, Object>> historyList = partSoManageDao.queryOrderHistory(CommonUtils.checkNull(mainMap.get("SO_CODE")));
            mainMap.put("orderCreateBy", orderCreateBy);
            mainMap.put("orderCreateDate", orderCreateDate);
            List<Map<String, Object>> detailList = partOutstockDao.queryOutstockDetail(outId);
            act.setOutData("mainMap", mainMap);
            act.setOutData("detailList", detailList);
            act.setOutData("historyList", historyList);
            act.setForword(PART_OUT_ORDER_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-17
     * @Title : 验证导出金税文本是否存在已导出单据
     */
    public void checkInvoTxtOP() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        try {
//            String orderCode = CommonUtils.checkNull(request
//                    .getParamValue("orderCode")); // 订货单号
            String sellCode = CommonUtils.checkNull(request
                    .getParamValue("sellCode")); // 销售单号
            String outCode = CommonUtils.checkNull(request
                    .getParamValue("outCode")); // 出库单号
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 订货单位
            String sellerName = CommonUtils.checkNull(request
                    .getParamValue("sellerName")); // 销售单位
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 订单类型
            String invoOutState = CommonUtils.checkNull(request
                    .getParamValue("invoOutState")); // 金税导出状态
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 出库仓库ID
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 出库开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 出库截止时间

            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            /*if (null != orderCode && !"".equals(orderCode)) {
                sbString.append(" AND OMM.ORDER_CODE LIKE '%" + orderCode + "%' ");
            }*/
            if (null != sellCode && !"".equals(sellCode)) {
                sbString.append(" AND OM.SO_CODE LIKE '%" + sellCode + "%' ");
            }
            if (null != outCode && !"".equals(outCode)) {
                sbString.append(" AND OM.OUT_CODE LIKE '%" + outCode + "%' ");
            }
            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND OM.DEALER_NAME LIKE '%" + dealerName + "%' ");
            }
            if (null != sellerName && !"".equals(sellerName)) {
                sbString.append(" AND OM.SELLER_NAME LIKE '%" + sellerName + "%' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND OM.WH_ID = '" + whId + "' ");
            }
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND OM.ORDER_TYPE = '" + orderType + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }

            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND OM.SELLER_ID = '" + parentOrgId + "' ");
            }
            int outStateInt = 0;
            if (null != invoOutState && !"".equals(invoOutState)) {
                sbString.append(" AND OM.IS_EXPORT = '" + invoOutState + "' ");
                outStateInt = Integer.parseInt(invoOutState);
            } else {
                sbString.append(" AND OM.IS_EXPORT = '" + invOutState2 + "' ");
            }

            String success = "确认导出金税文本?";

            if (invOutState1 == outStateInt) {
                success = "确认导出金税文本?";
            } else if (invOutState2 == outStateInt) {
                success = "存在已导出的订单,确认继续要执行?";
            } else {
                List<Map<String, Object>> invList = dao.queryPartInvoicesList(sbString.toString());

                if (null != invList && invList.size() > 0) {
                    success = "存在已导出的订单,确认继续要执行?";
                }
            }
            act.setOutData("success", success);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "验证是否存在已导出过的订单信息失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title : 手工开票
     */
    public void saveInvByHandle() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        String errorExist = "";
        try {
            String outIds = CommonUtils.checkNull(request
                    .getParamValue("outIds")); // 出库单ID
            /*String sellCode = CommonUtils.checkNull(request
                    .getParamValue("saleCode")); // 销售单号
            String outCode = CommonUtils.checkNull(request
                    .getParamValue("outCode")); // 出库单号
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 订货单位
            String invAmount = CommonUtils.checkNull(request
                    .getParamValue("invAmount")).trim().replace(",", ""); // 开票金额(含税)
            String tax = CommonUtils.checkNull(request
                    .getParamValue("tax")); // 税率
            String taxAmount = CommonUtils.checkNull(request
                    .getParamValue("taxAmount")).trim().replace(",", ""); // 税额
            String invAmountNoTax = CommonUtils.checkNull(request
                    .getParamValue("invAmountNoTax")).trim().replace(",", ""); // 开票金额(无税)
            */
            String invCode = CommonUtils.checkNull(request
                    .getParamValue("invCode")); // 发票代码
            String invNumber = CommonUtils.checkNull(request
                    .getParamValue("invNumber")); // 发票号码
            String invPerson = CommonUtils.checkNull(request
                    .getParamValue("invPerson")); // 开票人
            String oldBillNo = CommonUtils.checkNull(request
                    .getParamValue("oldBillNo")); // 原发票号码

            if (null != oldBillNo && !"".equals(oldBillNo)) {
                //更新账户记录开票号
                TtPartAccountHistoryPO selAHPo = new TtPartAccountHistoryPO();
                TtPartAccountHistoryPO updAHPo = new TtPartAccountHistoryPO();

                selAHPo.setInvoiceNo(oldBillNo);

                updAHPo.setInvoiceNo(invCode + "" + invNumber);

                dao.update(selAHPo, updAHPo);
            }

            long userId = logonUser.getUserId();
            Date date = new Date();

            String outIdsArr[] = outIds.split(",");

            for (int x = 0; x < outIdsArr.length; x++) {
                String outId = outIdsArr[x];
                String sellCode = "";

                String sqlStr = " AND BD.OUT_ID = '" + outId + "'";
                List<Map<String, Object>> bmList = dao.CheckBillInfo(sqlStr);

                String orgId = Constant.OEM_ACTIVITIES;
                String orgCode = Constant.ORG_ROOT_CODE;
                String orgName = "江西昌河汽车有限责任公司";
                String dlrId = "";
                String dlrCode = "";
                String dlrName = "";
                long billId = 0l;
                String typeYes = Constant.IF_TYPE_YES + "";

                if (null != bmList && bmList.size() == 1) {
                    sellCode = bmList.get(0).get("SO_CODE").toString();

                    if (typeYes.equals(bmList.get(0).get("IS_INV") + "")) {
                        //更新销售单
                        TtPartSoMainPO selSoPo = new TtPartSoMainPO();
                        TtPartSoMainPO updSoPo = new TtPartSoMainPO();

                        selSoPo.setSoCode(sellCode);

                        updSoPo.setInvoiceNo(invCode + "" + invNumber);

                        dao.update(selSoPo, updSoPo);

                        //更新金税文本导出状态
                        TtPartOutstockMainPO selOMPo = new TtPartOutstockMainPO();
                        TtPartOutstockMainPO updOMPo = new TtPartOutstockMainPO();

                        selOMPo.setOutId(Long.parseLong(outId));

                        updOMPo.setInvWay(INV_WAY_02);
                        updOMPo.setBillNo(invCode + "" + invNumber);
                        updOMPo.setBillUpdateBy(invPerson);
                        updOMPo.setBillUpdateDate(date);

                        dao.update(selOMPo, updOMPo);

                        //更新开票信息
                        /*TtPartBillMainPO selBMPo = new TtPartBillMainPO();
                        TtPartBillMainPO updBMPo = new TtPartBillMainPO();

                        selBMPo.setBillId(Long.parseLong(bmList.get(0).get("BILL_ID").toString()));

                        updBMPo.setBillNo(invCode + "" + invNumber);
                        updBMPo.setBillAmount(Double.parseDouble(invAmount));
                        updBMPo.setBillAmountnotax(Double.parseDouble(invAmountNoTax));
                        updBMPo.setTax(Float.parseFloat(tax));
                        updBMPo.setTaxAmount(Double.parseDouble(taxAmount));
                        updBMPo.setBillBy(invPerson);
                        updBMPo.setBillDate(date);
                        updBMPo.setUpdateBy(userId);
                        updBMPo.setUpdateDate(date);

                        dao.update(selBMPo, updBMPo);*/
                    } else {
                        dlrId = bmList.get(0).get("DEALER_ID").toString();
                        dlrCode = bmList.get(0).get("DEALER_CODE").toString();
                        dlrName = bmList.get(0).get("DEALER_NAME").toString();
                        billId = Long.parseLong(SequenceManager.getSequence(""));

                        //更新销售单
                        TtPartSoMainPO selSoPo = new TtPartSoMainPO();
                        TtPartSoMainPO updSoPo = new TtPartSoMainPO();

                        selSoPo.setSoCode(sellCode);

                        updSoPo.setInvoiceNo(invCode + "" + invNumber);

                        dao.update(selSoPo, updSoPo);

                        //更新金税文本导出状态
                        TtPartOutstockMainPO selOMPo = new TtPartOutstockMainPO();
                        TtPartOutstockMainPO updOMPo = new TtPartOutstockMainPO();

                        selOMPo.setOutId(Long.parseLong(outId));

                        updOMPo.setInvWay(INV_WAY_02);
                        updOMPo.setIsInv(Constant.IF_TYPE_YES);
                        updOMPo.setBillNo(invCode + "" + invNumber);
                        updOMPo.setBillBy(invPerson);
                        updOMPo.setBillDate(date);

                        dao.update(selOMPo, updOMPo);

                        //释放预占资金
                        TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
                        recordPO.setOrderCode(sellCode);
                        TtPartAccountRecordPO recordPO1 = new TtPartAccountRecordPO();
                        //recordPO1.setState(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_03);
                        recordPO1.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_03);//
                        recordPO1.setInvoiceNo(invCode + "" + invNumber);
                        recordPO1.setFunctionName("开票");
                        recordPO1.setCreateDate(date);//开票日期
                        dao.update(recordPO, recordPO1);

                        String accountId = dao.getdelearAccountId(dlrId, orgId);
                        TtPartOutstockMainPO outstockMainPO = new TtPartOutstockMainPO();
                        outstockMainPO.setSoCode(sellCode);
                        outstockMainPO = (TtPartOutstockMainPO) dao.select(outstockMainPO).get(0);
                        //账户扣除开票金额，处理成功一条释放扣除一次账户余额
                        TtPartAccountDefinePO srcPO = new TtPartAccountDefinePO();
                        srcPO.setAccountId(Long.valueOf(accountId));
                        if (dao.select(srcPO).size() > 0) {
                            TtPartAccountDefinePO updatePo = new TtPartAccountDefinePO();
                            //当前余额减去已开票金额(无税金额+税额）modify by yuan 20131009
                            //updatePo.setAccountSum(((TtPartAccountDefinePO) dao.select(srcPO).get(0)).getAccountSum() - (Double.valueOf(taxAmount) + Double.valueOf(noTaxAmount)));
                            //取出库金额
                            updatePo.setAccountSum(((TtPartAccountDefinePO) dao.select(srcPO).get(0)).getAccountSum() - outstockMainPO.getAmount());
                            updatePo.setUpdateDate(new Date());
                            updatePo.setUpdateBy(logonUser.getUserId());

                            dao.update(srcPO, updatePo);
                        }
                        //插入已开票金额
                        TtPartAccountHistoryPO ahPo = new TtPartAccountHistoryPO();
                        ahPo.setHistroryId(billId);
                        ahPo.setAccountId(Long.parseLong(accountId));
                        ahPo.setChildorgId(Long.parseLong(dlrId));
                        ahPo.setChildorgCode(dlrCode);
                        ahPo.setChildorgName(dlrName);
                        ahPo.setParentorgId(Long.parseLong(orgId));
                        ahPo.setParentorgCode(orgCode);
                        ahPo.setParentorgName(orgName);
                        ahPo.setAcountKind(Constant.FIXCODE_CURRENCY_01);
                        ahPo.setAcountType(Constant.FIXCODE_ACCOUNT_TYPE_02);
                        ahPo.setAmount(outstockMainPO.getAmount());//开票金额=无税金额+税额
                        ahPo.setRemark("");
                        ahPo.setInvoiceNo(invCode + "" + invNumber);
                        ahPo.setCreateBy(userId);
                        ahPo.setCreateDate(date);

                        dao.insert(ahPo);

                        //插入手工开票信息
                        /*TtPartBillMainPO insBMPo = new TtPartBillMainPO();

                        insBMPo.setBillId(Long.parseLong(SequenceManager.getSequence("")));
                        insBMPo.setSoCode(sellCode);
                        insBMPo.setBillNo(invCode + "" + invNumber);
                        insBMPo.setBillAmount(Double.parseDouble(invAmount));
                        insBMPo.setBillAmountnotax(Double.parseDouble(invAmountNoTax));
                        insBMPo.setTax(Float.parseFloat(tax));
                        insBMPo.setTaxAmount(Double.parseDouble(taxAmount));
                        insBMPo.setBillBy(invPerson);
                        insBMPo.setBillDate(date);
                        insBMPo.setCreateBy(userId);
                        insBMPo.setCreateDate(date);
                        insBMPo.setUpdateBy(userId);
                        insBMPo.setUpdateDate(date);
                        insBMPo.setRemark(sellCode);//用于获取订货单位

                        dao.insert(insBMPo);*/
                    }

                } else {
                    act.setOutData("errorExist", "手工开票失败，请联系管理员!");
                }

                act.setOutData("success", "true");
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "验证是否存在已导出过的订单信息失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-10-12
     * @Title : 财务发票信息导入验证页面
     */
    public void invoImpUploadNew() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = null;
//        Long billId = null;
        try {
            logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            RequestWrapper request = act.getRequest();
            FileObject importFile = request.getParamObject("importFile");
            String checkInvRes = checkInvImp(importFile);
            if (!"".equals(checkInvRes)) {
                String errArr[] = checkInvRes.split(",");

                act.setOutData("errArr", errArr);
                act.setForword(INPUT_ERROR_URL);
            } else {
//                Long userId = logonUser.getUserId();
//                String remark = "开票";
                List<Map<String, String>> unInvlist = new ArrayList<Map<String, String>>();
                List<Map<String, String>> invedlist = new ArrayList<Map<String, String>>();
                Map<String, String> unInvMap = null;
                Map<String, String> invedMap = null;
                String tax = "";

                if (null != importFile) {
                    ReaderUtil txtReader = new ReaderUtil(importFile.getContent(), "\t");
                    String mark1 = "//发票";//发票信息读取标记
//                    Date date = new Date();

                    List<String> list = txtReader.getList();


                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        if (null != list.get(i) && list.get(i).startsWith(mark1)) {
                            /*TtPartBillMainPO bmPo = new TtPartBillMainPO();
                            TtPartBillMainPO bmSelPo = new TtPartBillMainPO();
                            TtPartBillMainPO bmUpdPo = new TtPartBillMainPO();

                            billId = Long.parseLong(SequenceManager.getSequence(""));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");*/

                            //拆分数组
                            String bmArr[] = list.get(i + 1).split("~~");
                            if (bmArr.length > 20) {
                                String invCode = bmArr[3];//发票量编码
                                String invNum = bmArr[4];//发票号
                                String invDate = bmArr[6];//开票日期
//                                String salCode = bmArr[8];//销售单号
                                String noTaxAmount = bmArr[9];//无税金额
//                                String tax = bmArr[10];//税率
                                if ("".equals(tax)) {
                                    tax = bmArr[10];//税率
                                }
                                String taxAmount = bmArr[11];//税额
                                String invUser = bmArr[21];//开票人
//                                String dlrName = bmArr[12];//订货单位
//                                String orgName = bmArr[16];//开票单位
                                String soCodes = bmArr[20];//销售单号

                                String temStr = bmArr[8].replace(",", "");
//                                String dlrCode = temStr.substring(4, temStr.length() - 8);//订货单位编码
                                String soCodePre = temStr.substring(0, temStr.length() - 6);//销售单前缀


                               /* String dlrId = "";//采购单位ID
                                String orgId = "";//销售单位ID
                                String orgCode = "";//销售单位Code

                                List<Map<String, Object>> dlrList = dao.getDealerName(dlrCode.trim());
                                if (null != dlrList) {
                                    dlrId = dlrList.get(0).get("DEALER_ID").toString();
                                    dlrCode = dlrList.get(0).get("DEALER_CODE").toString();
                                } 
                                orgId = Constant.OEM_ACTIVITIES;
                                orgCode = Constant.ORG_ROOT_CODE;*/


                                if (null != soCodes && !"".equals(soCodes)) {
                                    String soCodeArr[] = soCodes.split(",");
                                    for (int t = 0; t < soCodeArr.length; t++) {
                                        //组装销售单号
                                        String soCode = soCodePre + soCodeArr[t];

                                        TtPartOutstockMainPO outstockMainPO1 = new TtPartOutstockMainPO();
                                        outstockMainPO1.setSoCode(soCode);
//                                        outstockMainPO1.setBillNo(invCode + "" + invNum);//错误的判断条件
                                        outstockMainPO1.setIsInv(Constant.IF_TYPE_YES);//已开票状态

                                        List<TtPartOutstockMainPO> omPo1 = dao.select(outstockMainPO1);

                                        //未开票
                                        if (null == omPo1 || omPo1.size() == 0) {
                                            TtPartOutstockMainPO outstockMainPO2 = new TtPartOutstockMainPO();
                                            outstockMainPO2.setSoCode(soCode);
                                            List<TtPartOutstockMainPO> omPo2 = dao.select(outstockMainPO2);
                                            if (null != omPo2 && omPo2.size() > 0) {
                                                unInvMap = new HashMap<String, String>();

                                                unInvMap.put("soCode", omPo2.get(0).getSoCode());
                                                unInvMap.put("dealerName", omPo2.get(0).getDealerName());
                                                unInvMap.put("dealerCode", omPo2.get(0).getDealerCode());
                                                unInvMap.put("dealerId", omPo2.get(0).getDealerId() + "");
                                                unInvMap.put("amount", omPo2.get(0).getAmount() + "");
                                                unInvMap.put("billNo", invCode + "" + invNum);
                                                unInvMap.put("billBy", invUser);
                                                unInvMap.put("billDate", invDate);
                                                unInvMap.put("noTaxAmount", noTaxAmount);
                                                unInvMap.put("taxAmount", taxAmount);

                                                unInvlist.add(unInvMap);
                                            }
                                        }
                                        //已开票
                                        else {
                                            invedMap = new HashMap<String, String>();

                                            invedMap.put("soCode", omPo1.get(0).getSoCode());
                                            invedMap.put("dealerName", omPo1.get(0).getDealerName());
                                            invedMap.put("dealerCode", omPo1.get(0).getDealerCode());
                                            invedMap.put("dealerId", omPo1.get(0).getDealerId() + "");
                                            invedMap.put("amount", omPo1.get(0).getAmount() + "");
                                            invedMap.put("billNo", omPo1.get(0).getBillNo());
                                            invedMap.put("billBy", omPo1.get(0).getBillBy());

                                            invedlist.add(invedMap);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                act.setOutData("unInvListSize", unInvlist.size());
                act.setOutData("unInvlist", unInvlist);
                act.setOutData("invedlist", invedlist);
                act.setOutData("tax", tax);
                act.setForword(INPUT_CONFIRM_URL);
            }

        } catch (BizException e) {
            logger.error(logonUser, e);
            act.setException(e);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "开票结果导入失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-10-12
     * @Title : 提交导入发票信息
     */
    public void commitInvoices() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = null;
        Long billId = null;
        String errorExist = "";
        String success = "";
        try {
            logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            RequestWrapper request = act.getRequest();
            Long userId = logonUser.getUserId();
            Date date = new Date();

            String unInvListSize = CommonUtils.checkNull(request.getParamValue("unInvListSize")); //未开票数据总条数
            String invTax = request.getParamValue("invTax"); //开票税率
            int unInvCount = 0;
            if (null != unInvListSize && !"".equals(unInvListSize)) {
                unInvCount = Integer.parseInt(unInvListSize);

                if (unInvCount > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();
                    numberFormat.setMinimumFractionDigits(2);
                    numberFormat.setMaximumFractionDigits(2);
                    numberFormat.setMaximumIntegerDigits(10);

                    String dlrId = "";//采购单位ID
                    String dlrCode = "";//采购单位Code
                    String dlrName = "";//采购单位name
                    String orgId = "";//销售单位ID
                    String orgCode = "";//销售单位Code
                    String orgName = "江西昌河汽车有限责任公司";//开票单位
                    String billNo = "";//发票号码
                    String billBy = "";//开票人
                    String billDate = "";//开票时间
                    String soCode = "";//销售单号
                    double ysf = 0;//运输费
                    double tax = 0.17;//税率
                    if (null != invTax && !"".equals(invTax)) {
                        tax = Double.parseDouble(numberFormat.format(Double.parseDouble(invTax)));
                    }
//                    double toaltSaleAmount = 0;//总金税金额（含税）
                    String soCodes = "";//销售单号组
                    String prvBillNo = "";
                    String gcjStr = "国产件";

                    for (int i = 0; i < unInvCount; i++) {
//	                    TtPartBillMainPO bmPo = new TtPartBillMainPO();
//	                    TtPartBillMainPO bmSelPo = new TtPartBillMainPO();
//	                    TtPartBillMainPO bmUpdPo = new TtPartBillMainPO();

                        billNo = CommonUtils.checkNull(request.getParamValue("billNo_" + (i + 1)));
                        if ("".equals(prvBillNo)) {
                            prvBillNo = billNo;
                        } else if (!"".equals(prvBillNo) && !prvBillNo.equals(billNo)) {
                            //插入到NC数据表
                            TtVsOrderInvoPO oiPo = null;
                            double noTaxAmount = Double.parseDouble(CommonUtils.checkNull(request.getParamValue("noTaxAmount_" + (i))).replace(",", ""));//不含税总金额
                            double taxAmount = Double.parseDouble(CommonUtils.checkNull(request.getParamValue("taxAmount_" + (i))).replace(",", ""));//税额总金额
                            double chgTaxAmount = taxAmount;
                            double chgNoTaxAmount = noTaxAmount;

                            soCodes = soCodes.substring(0, soCodes.length() - 2);

                            String sqlStr = " AND BD.SO_CODE IN (" + soCodes + ")";

                            List<Map<String, Object>> mainList = dao.getOutStockInfo(sqlStr);

                            if (null != mainList && mainList.size() > 0) {
                                for (int l = 0; l < mainList.size(); l++) {
                                    if (null != mainList.get(l).get("FREIGHT") && !"".equals(mainList.get(l).get("FREIGHT").toString())) {
                                        ysf += Double.parseDouble(mainList.get(l).get("FREIGHT").toString());
                                    }
                                }

                            }

                            String sqlDtl = " AND OM.SO_CODE IN (" + soCodes + ")";

                            List<Map<String, Object>> dtlList = dao.queryDtlList(sqlDtl);

                            if (null != dtlList) {
                                /*for(int t = 0; t < dtlList.size(); t ++)
                                {
                            		toaltSaleAmount += Double.parseDouble(dtlList.get(t).get("SALE_AMOUNT").toString());
                            	}
                            	
                            	if(ysf > 0)
                        		{
                            		toaltSaleAmount += ysf;
                        		}*/

                                String soCodesFm = "";
                                String tmpStr = soCodes.replace(" ", "").replace("'", "");
                                String soCodesArr[] = tmpStr.split(",");

                                for (int t = 0; t < soCodesArr.length; t++) {
                                    soCodesFm += soCodesArr[t].substring(soCodesArr[t].length() - 6, soCodesArr[t].length()) + ",";
                                }

                                soCodesFm = soCodesFm.substring(0, soCodesFm.length() - 1);

                                //验证明细是否包含国产件
                                boolean existGcj = false;
                                for (int t = 0; t < dtlList.size(); t++) {
                                    String partType = dtlList.get(t).get("PART_TYPE").toString();
                                    if ((Constant.PART_BASE_PART_TYPES_PURCHASE + "").equals(partType)) {
                                        existGcj = true;
                                    }
                                }

                                //明细不含国产件但存在运输费（国产件）
                                if (!existGcj && ysf > 0) {
                                    chgNoTaxAmount = chgNoTaxAmount - Double.parseDouble((numberFormat.format(ysf / (1 + tax)) + "").replace(",", ""));
                                    chgTaxAmount = chgTaxAmount - Double.parseDouble((numberFormat.format(tax * (ysf / (1 + tax))) + "").replace(",", ""));

                                    oiPo = new TtVsOrderInvoPO();

                                    oiPo.setId(Long.parseLong(SequenceManager.getSequence("")));
                                    oiPo.setInvoiceDep(orgName);
                                    oiPo.setOrderNo(soCodesFm);
                                    oiPo.setDealerCode(dlrCode);
                                    oiPo.setDealerName(dlrName);
                                    oiPo.setInvoiceNo(prvBillNo);
                                    oiPo.setInvoiceDate(sdf.parse(billDate));
                                    oiPo.setInvoiceNoVer("1");
                                    oiPo.setInvoiceRemark("");
                                    oiPo.setSName(gcjStr);
                                    oiPo.setPCode(gcjStr);
                                    oiPo.setGJsje((numberFormat.format(ysf / (1 + tax)) + "").replace(",", ""));//无税金额
                                    oiPo.setGJsse((numberFormat.format(tax * (ysf / (1 + tax))) + "").replace(",", ""));//金税税额
                                    oiPo.setGZkje("0");
                                    oiPo.setGZkse("0");
                                    oiPo.setDealFlag("0");
                                    oiPo.setOrderFlag("0");
                                    oiPo.setInvoiceNum(1);
                                    oiPo.setFondName("现款");
                                    oiPo.setIsDel("0");
                                    oiPo.setInvoType("02");
//                                    oiPo.setGSingle("");
                                    oiPo.setTotalJsse(taxAmount + "");//总金税税额
                                    oiPo.setGJssl(tax + "");

                                    dao.insert(oiPo);
                                }

                                for (int t = 0; t < dtlList.size(); t++) {
                                    if ((t + 1) == dtlList.size()) {
                                        String codeDesc = dtlList.get(t).get("CODE_DESC").toString();

                                        oiPo = new TtVsOrderInvoPO();

                                        oiPo.setId(Long.parseLong(SequenceManager.getSequence("")));
                                        oiPo.setInvoiceDep(orgName);
                                        oiPo.setOrderNo(soCodesFm);
                                        oiPo.setDealerCode(dlrCode);
                                        oiPo.setDealerName(dlrName);
                                        oiPo.setInvoiceNo(prvBillNo);
                                        oiPo.setInvoiceDate(sdf.parse(billDate));
                                        oiPo.setInvoiceNoVer("1");
                                        oiPo.setInvoiceRemark("");
                                        oiPo.setSName(codeDesc);
                                        oiPo.setPCode(codeDesc);
                                        oiPo.setGJsje((numberFormat.format(chgNoTaxAmount).replace(",", "")));//无税金额
                                        oiPo.setGJsse((numberFormat.format(chgTaxAmount).replace(",", "")));//金税税额
                                        oiPo.setGZkje("0");
                                        oiPo.setGZkse("0");
                                        oiPo.setDealFlag("0");
                                        oiPo.setOrderFlag("0");
                                        oiPo.setInvoiceNum(1);
                                        oiPo.setFondName("现款");
                                        oiPo.setIsDel("0");
                                        oiPo.setInvoType("02");
//                                        oiPo.setGSingle("");
                                        oiPo.setTotalJsse(taxAmount + "");//总金税税额
                                        oiPo.setGJssl(tax + "");

                                        dao.insert(oiPo);
                                    } else {
                                        String codeDesc = dtlList.get(t).get("CODE_DESC").toString();
                                        String partType = dtlList.get(t).get("PART_TYPE").toString();
                                        double saleAmount = 0;//销售金额（含税）
                                        if ((Constant.PART_BASE_PART_TYPES_PURCHASE + "").equals(partType)) {
                                            saleAmount = Double.parseDouble(dtlList.get(t).get("SALE_AMOUNT").toString()) + ysf;
                                        } else {
                                            saleAmount = Double.parseDouble(dtlList.get(t).get("SALE_AMOUNT").toString());
                                        }

                                        chgNoTaxAmount = chgNoTaxAmount - Double.parseDouble((numberFormat.format(saleAmount / (1 + tax)) + "").replace(",", ""));
                                        chgTaxAmount = chgTaxAmount - Double.parseDouble((numberFormat.format(tax * (saleAmount / (1 + tax))) + "").replace(",", ""));

                                        oiPo = new TtVsOrderInvoPO();

                                        oiPo.setId(Long.parseLong(SequenceManager.getSequence("")));
                                        oiPo.setInvoiceDep(orgName);
                                        oiPo.setOrderNo(soCodesFm);
                                        oiPo.setDealerCode(dlrCode);
                                        oiPo.setDealerName(dlrName);
                                        oiPo.setInvoiceNo(prvBillNo);
                                        oiPo.setInvoiceDate(sdf.parse(billDate));
                                        oiPo.setInvoiceNoVer("1");
                                        oiPo.setInvoiceRemark("");
                                        oiPo.setSName(codeDesc);
                                        oiPo.setPCode(codeDesc);
                                        oiPo.setGJsje((numberFormat.format(saleAmount / (1 + tax)) + "").replace(",", ""));//无税金额
                                        oiPo.setGJsse((numberFormat.format(tax * (saleAmount / (1 + tax))) + "").replace(",", ""));//金税税额
                                        oiPo.setGZkje("0");
                                        oiPo.setGZkse("0");
                                        oiPo.setDealFlag("0");
                                        oiPo.setOrderFlag("0");
                                        oiPo.setInvoiceNum(1);
                                        oiPo.setFondName("现款");
                                        oiPo.setIsDel("0");
                                        oiPo.setInvoType("02");
//                                        oiPo.setGSingle("");
                                        oiPo.setTotalJsse(taxAmount + "");//总金税税额
                                        oiPo.setGJssl(tax + "");

                                        dao.insert(oiPo);
                                    }
                                }
                            }

                            //重置数据
                            prvBillNo = billNo;
                            soCodes = "";
//	                    	toaltSaleAmount = 0;
                            ysf = 0;
                        }

                        billId = Long.parseLong(SequenceManager.getSequence(""));

                        soCode = CommonUtils.checkNull(request.getParamValue("soCode_" + (i + 1)));
                        soCodes += "'" + soCode + "', ";

                        dlrId = CommonUtils.checkNull(request.getParamValue("dealerId_" + (i + 1)));
                        dlrCode = CommonUtils.checkNull(request.getParamValue("dealerCode_" + (i + 1)));
                        dlrName = CommonUtils.checkNull(request.getParamValue("dealerName_" + (i + 1)));

                        billBy = CommonUtils.checkNull(request.getParamValue("billBy_" + (i + 1)));
                        billDate = CommonUtils.checkNull(request.getParamValue("billDate_" + (i + 1)));
                        orgId = Constant.OEM_ACTIVITIES;
                        orgCode = Constant.ORG_ROOT_CODE;

                        TtPartOutstockMainPO outstockMainPO1 = new TtPartOutstockMainPO();
                        outstockMainPO1.setSoCode(soCode);
                        outstockMainPO1.setIsInv(Constant.IF_TYPE_YES);//已开票状态

                        //add by yuan 2013
                        if (dao.select(outstockMainPO1).size() == 0) {
                            //更新销售单开票信息
                            TtPartSoMainPO soSelPo = new TtPartSoMainPO();
                            soSelPo.setSoCode(soCode);
                            TtPartSoMainPO soUpdPo = new TtPartSoMainPO();
                            soUpdPo.setInvoiceNo(billNo);
                            dao.update(soSelPo, soUpdPo);

                            //更新出库表开票信息
                            TtPartOutstockMainPO omSelPo = new TtPartOutstockMainPO();
                            omSelPo.setSoCode(soCode);
                            TtPartOutstockMainPO omUpdPo = new TtPartOutstockMainPO();
                            omUpdPo.setBillNo(billNo);
                            omUpdPo.setBillBy(billBy);
                            omUpdPo.setBillDate(sdf.parse(billDate));
//	                        omUpdPo.setIsExport(invOutState2);
                            omUpdPo.setIsInv(Constant.IF_TYPE_YES);
                            omUpdPo.setInvWay(INV_WAY_01);
                            dao.update(omSelPo, omUpdPo);

                            //释放预占资金
                            TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
                            recordPO.setOrderCode(soCode);
                            TtPartAccountRecordPO recordPO1 = new TtPartAccountRecordPO();
                            //recordPO1.setState(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_03);
                            recordPO1.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_03);//
                            recordPO1.setInvoiceNo(billNo);
                            recordPO1.setFunctionName("开票");
                            recordPO1.setCreateDate(sdf.parse(billDate));//开票日期
                            dao.update(recordPO, recordPO1);

                            String accountId = dao.getdelearAccountId(dlrId, orgId);
                            TtPartOutstockMainPO outstockMainPO = new TtPartOutstockMainPO();
                            outstockMainPO.setSoCode(soCode);
                            outstockMainPO = (TtPartOutstockMainPO) dao.select(outstockMainPO).get(0);
                            //账户扣除开票金额，处理成功一条释放扣除一次账户余额
                            TtPartAccountDefinePO srcPO = new TtPartAccountDefinePO();
                            srcPO.setAccountId(Long.valueOf(accountId));
                            if (dao.select(srcPO).size() > 0) {
                                TtPartAccountDefinePO updatePo = new TtPartAccountDefinePO();
                                //当前余额减去已开票金额(无税金额+税额）modify by yuan 20131009
                                //updatePo.setAccountSum(((TtPartAccountDefinePO) dao.select(srcPO).get(0)).getAccountSum() - (Double.valueOf(taxAmount) + Double.valueOf(noTaxAmount)));
                                //取出库金额
                                updatePo.setAccountSum(((TtPartAccountDefinePO) dao.select(srcPO).get(0)).getAccountSum() - outstockMainPO.getAmount());
                                updatePo.setUpdateDate(new Date());
                                updatePo.setUpdateBy(logonUser.getUserId());

                                dao.update(srcPO, updatePo);
                            }
                            //插入已开票金额
                            TtPartAccountHistoryPO ahPo = new TtPartAccountHistoryPO();
                            ahPo.setHistroryId(billId);
                            ahPo.setAccountId(Long.parseLong(accountId));
                            ahPo.setChildorgId(Long.parseLong(dlrId));
                            ahPo.setChildorgCode(dlrCode);
                            ahPo.setChildorgName(dlrName);
                            ahPo.setParentorgId(Long.parseLong(orgId));
                            ahPo.setParentorgCode(orgCode);
                            ahPo.setParentorgName(orgName);
                            ahPo.setAcountKind(Constant.FIXCODE_CURRENCY_01);
                            ahPo.setAcountType(Constant.FIXCODE_ACCOUNT_TYPE_02);
                            ahPo.setAmount(outstockMainPO.getAmount());//开票金额=无税金额+税额
                            ahPo.setInvoiceNo(billNo);
                            ahPo.setCreateBy(userId);
                            ahPo.setCreateDate(date);

                            dao.insert(ahPo);

                        } else {
                            errorExist += soCode + ", ";
                        }

                        if (i == unInvCount - 1) {
                            //插入到NC数据表
                            TtVsOrderInvoPO oiPo = null;
                            double noTaxAmount = Double.parseDouble(CommonUtils.checkNull(request.getParamValue("noTaxAmount_" + (i + 1))).replace(",", ""));//不含税总金额
                            double taxAmount = Double.parseDouble(CommonUtils.checkNull(request.getParamValue("taxAmount_" + (i + 1))).replace(",", ""));//税额总金额
                            double chgTaxAmount = taxAmount;
                            double chgNoTaxAmount = noTaxAmount;

                            soCodes = soCodes.substring(0, soCodes.length() - 2);

                            String sqlStr = " AND BD.SO_CODE IN (" + soCodes + ")";

                            List<Map<String, Object>> mainList = dao.getOutStockInfo(sqlStr);

                            if (null != mainList && mainList.size() > 0) {
                                for (int l = 0; l < mainList.size(); l++) {
                                    if (null != mainList.get(l).get("FREIGHT") && !"".equals(mainList.get(l).get("FREIGHT").toString())) {
                                        ysf += Double.parseDouble(mainList.get(l).get("FREIGHT").toString());
                                    }
                                }

                            }

                            String sqlDtl = " AND OM.SO_CODE IN (" + soCodes + ")";

                            List<Map<String, Object>> dtlList = dao.queryDtlList(sqlDtl);

                            if (null != dtlList) {
                            	/*for(int t = 0; t < dtlList.size(); t ++)
                            	{
                            		toaltSaleAmount += Double.parseDouble(dtlList.get(t).get("SALE_AMOUNT").toString());
                            	}
                            	
                            	if(ysf > 0)
                        		{
                            		toaltSaleAmount += ysf;
                        		}*/

                                String soCodesFm = "";
                                String tmpStr = soCodes.replace(" ", "").replace("'", "");
                                String soCodesArr[] = tmpStr.split(",");

                                for (int t = 0; t < soCodesArr.length; t++) {
                                    soCodesFm += soCodesArr[t].substring(soCodesArr[t].length() - 6, soCodesArr[t].length()) + ",";
                                }

                                soCodesFm = soCodesFm.substring(0, soCodesFm.length() - 1);

                                //验证明细是否包含国产件
                                boolean existGcj = false;
                                for (int t = 0; t < dtlList.size(); t++) {
                                    String partType = dtlList.get(t).get("PART_TYPE").toString();
                                    if ((Constant.PART_BASE_PART_TYPES_PURCHASE + "").equals(partType)) {
                                        existGcj = true;
                                    }
                                }

                                //明细不含国产件但存在运输费（国产件）
                                if (!existGcj && ysf > 0) {
                                    chgNoTaxAmount = chgNoTaxAmount - Double.parseDouble((numberFormat.format(ysf / (1 + tax)) + "").replace(",", ""));
                                    chgTaxAmount = chgTaxAmount - Double.parseDouble((numberFormat.format(tax * (ysf / (1 + tax))) + "").replace(",", ""));

                                    oiPo = new TtVsOrderInvoPO();

                                    oiPo.setId(Long.parseLong(SequenceManager.getSequence("")));
                                    oiPo.setInvoiceDep(orgName);
                                    oiPo.setOrderNo(soCodesFm);
                                    oiPo.setDealerCode(dlrCode);
                                    oiPo.setDealerName(dlrName);
                                    oiPo.setInvoiceNo(prvBillNo);
                                    oiPo.setInvoiceDate(sdf.parse(billDate));
                                    oiPo.setInvoiceNoVer("1");
                                    oiPo.setInvoiceRemark("");
                                    oiPo.setSName(gcjStr);
                                    oiPo.setPCode(gcjStr);
                                    oiPo.setGJsje((numberFormat.format(ysf / (1 + tax)) + "").replace(",", ""));//无税金额
                                    oiPo.setGJsse((numberFormat.format(tax * (ysf / (1 + tax))) + "").replace(",", ""));//金税税额
                                    oiPo.setGZkje("0");
                                    oiPo.setGZkse("0");
                                    oiPo.setDealFlag("0");
                                    oiPo.setOrderFlag("0");
                                    oiPo.setInvoiceNum(1);
                                    oiPo.setFondName("现款");
                                    oiPo.setIsDel("0");
                                    oiPo.setInvoType("02");
//                                    oiPo.setGSingle("");
                                    oiPo.setTotalJsse(taxAmount + "");//总金税税额
                                    oiPo.setGJssl(tax + "");

                                    dao.insert(oiPo);
                                }

                                for (int t = 0; t < dtlList.size(); t++) {
                                    if ((t + 1) == dtlList.size()) {
                                        String codeDesc = dtlList.get(t).get("CODE_DESC").toString();

                                        oiPo = new TtVsOrderInvoPO();

                                        oiPo.setId(Long.parseLong(SequenceManager.getSequence("")));
                                        oiPo.setInvoiceDep(orgName);
                                        oiPo.setOrderNo(soCodesFm);
                                        oiPo.setDealerCode(dlrCode);
                                        oiPo.setDealerName(dlrName);
                                        oiPo.setInvoiceNo(billNo);
                                        oiPo.setInvoiceDate(sdf.parse(billDate));
                                        oiPo.setInvoiceNoVer("1");
                                        oiPo.setInvoiceRemark("");
                                        oiPo.setSName(codeDesc);
                                        oiPo.setPCode(codeDesc);
                                        oiPo.setGJsje((numberFormat.format(chgNoTaxAmount).replace(",", "")));//无税金额
                                        oiPo.setGJsse((numberFormat.format(chgTaxAmount).replace(",", "")));//金税税额
                                        oiPo.setGZkje("0");
                                        oiPo.setGZkse("0");
                                        oiPo.setDealFlag("0");
                                        oiPo.setOrderFlag("0");
                                        oiPo.setInvoiceNum(1);
                                        oiPo.setFondName("现款");
                                        oiPo.setIsDel("0");
                                        oiPo.setInvoType("02");
//                                        oiPo.setGSingle("");
                                        oiPo.setTotalJsse(taxAmount + "");//总金税税额
                                        oiPo.setGJssl(tax + "");

                                        dao.insert(oiPo);
                                    } else {
                                        String codeDesc = dtlList.get(t).get("CODE_DESC").toString();
                                        String partType = dtlList.get(t).get("PART_TYPE").toString();
                                        double saleAmount = 0;//销售金额（含税）
                                        if ((Constant.PART_BASE_PART_TYPES_PURCHASE + "").equals(partType)) {
                                            saleAmount = Double.parseDouble(dtlList.get(t).get("SALE_AMOUNT").toString()) + ysf;
                                        } else {
                                            saleAmount = Double.parseDouble(dtlList.get(t).get("SALE_AMOUNT").toString());
                                        }

                                        chgNoTaxAmount = chgNoTaxAmount - Double.parseDouble((numberFormat.format(saleAmount / (1 + tax)) + "").replace(",", ""));
                                        chgTaxAmount = chgTaxAmount - Double.parseDouble((numberFormat.format(tax * (saleAmount / (1 + tax))) + "").replace(",", ""));

                                        oiPo = new TtVsOrderInvoPO();

                                        oiPo.setId(Long.parseLong(SequenceManager.getSequence("")));
                                        oiPo.setInvoiceDep(orgName);
                                        oiPo.setOrderNo(soCodesFm);
                                        oiPo.setDealerCode(dlrCode);
                                        oiPo.setDealerName(dlrName);
                                        oiPo.setInvoiceNo(billNo);
                                        oiPo.setInvoiceDate(sdf.parse(billDate));
                                        oiPo.setInvoiceNoVer("1");
                                        oiPo.setInvoiceRemark("");
                                        oiPo.setSName(codeDesc);
                                        oiPo.setPCode(codeDesc);
                                        oiPo.setGJsje((numberFormat.format(saleAmount / (1 + tax)) + "").replace(",", ""));//无税金额
                                        oiPo.setGJsse((numberFormat.format(tax * (saleAmount / (1 + tax))) + "").replace(",", ""));//金税税额
                                        oiPo.setGZkje("0");
                                        oiPo.setGZkse("0");
                                        oiPo.setDealFlag("0");
                                        oiPo.setOrderFlag("0");
                                        oiPo.setInvoiceNum(1);
                                        oiPo.setFondName("现款");
                                        oiPo.setIsDel("0");
                                        oiPo.setInvoType("02");
//                                        oiPo.setGSingle("");
                                        oiPo.setTotalJsse(taxAmount + "");//总金税税额
                                        oiPo.setGJssl(tax + "");

                                        dao.insert(oiPo);
                                    }
                                }
                            }
                        }
                    }

                    success = "true";
                }

            }
            if (!"".equals(errorExist)) {
                act.setOutData("errorExist", "开票信息提交成功!<br/>其中销售单号【" + errorExist + "】已开票，不重复开票!");
            }
            act.setOutData("success", success);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "开票结果导入失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-20
     * @Title : 开票结果导入
     */
    public void invoImpUpload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = null;
        Long billId = null;
        try {
            logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            RequestWrapper request = act.getRequest();
            FileObject importFile = request.getParamObject("importFile");
            String checkInvRes = checkInvImp(importFile);
            if (!"".equals(checkInvRes)) {
                String errArr[] = checkInvRes.split(",");

                act.setOutData("errArr", errArr);
                act.setForword(INPUT_ERROR_URL);
            } else {
                Long userId = logonUser.getUserId();
                String remark = "开票";
                if (null != importFile) {
                    ReaderUtil txtReader = new ReaderUtil(importFile.getContent(), "\t");
                    String mark1 = "//发票";//发票信息读取标记
                    Date date = new Date();

                    List<String> list = txtReader.getList();
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        if (null != list.get(i) && list.get(i).startsWith(mark1)) {
                            TtPartBillMainPO bmPo = new TtPartBillMainPO();
                            TtPartBillMainPO bmSelPo = new TtPartBillMainPO();
                            TtPartBillMainPO bmUpdPo = new TtPartBillMainPO();

                            billId = Long.parseLong(SequenceManager.getSequence(""));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                            //拆分数组
                            String bmArr[] = list.get(i + 1).split("~~");

                            String invCode = bmArr[3];//发票量编码
                            String invNum = bmArr[4];//发票号
                            String invDate = bmArr[6];//开票日期
                            String salCode = bmArr[8];//销售单号
                            String noTaxAmount = bmArr[9];//无税金额
                            String tax = bmArr[10];//税率
                            String taxAmount = bmArr[11];//税额
                            String invUser = bmArr[21];//开票人
                            String dlrName = bmArr[12];//订货单位
                            String orgName = bmArr[16];//开票单位
                            String soCodes = bmArr[20];//销售单号

                            String temStr = bmArr[8].replace(",", "");
                            String dlrCode = temStr.substring(4, temStr.length() - 8);//订货单位编码
                            String soCodePre = temStr.substring(0, temStr.length() - 6);//销售单前缀


                            String dlrId = "";//采购单位ID
                            String orgId = "";//销售单位ID
                            String orgCode = "";//销售单位Code

                            List<Map<String, Object>> dlrList = dao.getDealerName(dlrCode.trim());
                            if (null != dlrList) {
                                dlrId = dlrList.get(0).get("DEALER_ID").toString();
                                dlrCode = dlrList.get(0).get("DEALER_CODE").toString();
                            } /*else {
                                dlrId = Constant.OEM_ACTIVITIES;
                                dlrCode = Constant.ORG_ROOT_CODE;
                            }*/

                          /*  List<Map<String, Object>> orgList = dao.getDealerName(dlrCode.trim());
                            if (null != orgList) {
                                //orgId = orgList.get(0).get("DEALER_ID").toString();
                                //orgCode = orgList.get(0).get("DEALER_CODE").toString();
                            } else {*/
                            orgId = Constant.OEM_ACTIVITIES;
                            orgCode = Constant.ORG_ROOT_CODE;
                            /*}*/


                            if (null != soCodes && !"".equals(soCodes)) {
                                String soCodeArr[] = soCodes.split(",");
                                for (int t = 0; t < soCodeArr.length; t++) {
                                    //组装销售单号
                                    String soCode = soCodePre + soCodeArr[t];

                                    TtPartOutstockMainPO outstockMainPO1 = new TtPartOutstockMainPO();
                                    outstockMainPO1.setSoCode(soCode);
//                                    outstockMainPO1.setBillNo(invCode + "" + invNum);//错误的判断条件
                                    outstockMainPO1.setIsInv(Constant.IF_TYPE_YES);//已开票状态

                                    //add by yuan 2013
                                    if (dao.select(outstockMainPO1).size() == 0) {
                                        //更新销售单开票信息
                                        TtPartSoMainPO soSelPo = new TtPartSoMainPO();
                                        soSelPo.setSoCode(soCode);
                                        TtPartSoMainPO soUpdPo = new TtPartSoMainPO();
                                        soUpdPo.setInvoiceNo(invCode + "" + invNum);
                                        dao.update(soSelPo, soUpdPo);

                                        //更新出库表开票信息
                                        TtPartOutstockMainPO omSelPo = new TtPartOutstockMainPO();
                                        omSelPo.setSoCode(soCode);
                                        TtPartOutstockMainPO omUpdPo = new TtPartOutstockMainPO();
                                        omUpdPo.setBillNo(invCode + "" + invNum);
                                        omUpdPo.setBillBy(invUser);
                                        omUpdPo.setBillDate(sdf.parse(invDate));
//                                        omUpdPo.setIsExport(invOutState2);
                                        omUpdPo.setIsInv(Constant.IF_TYPE_YES);
                                        omUpdPo.setInvWay(INV_WAY_01);
                                        dao.update(omSelPo, omUpdPo);

                                        //释放预占资金
                                        TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
                                        recordPO.setOrderCode(soCode);
                                        TtPartAccountRecordPO recordPO1 = new TtPartAccountRecordPO();
                                        //recordPO1.setState(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_03);
                                        recordPO1.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_03);//
                                        recordPO1.setInvoiceNo(invCode + invNum);
                                        recordPO1.setFunctionName("开票");
                                        recordPO1.setCreateDate(sdf.parse(invDate));//开票日期
                                        dao.update(recordPO, recordPO1);

                                        String accountId = dao.getdelearAccountId(dlrId, orgId);
                                        TtPartOutstockMainPO outstockMainPO = new TtPartOutstockMainPO();
                                        outstockMainPO.setSoCode(soCode);
                                        outstockMainPO = (TtPartOutstockMainPO) dao.select(outstockMainPO).get(0);
                                        //账户扣除开票金额，处理成功一条释放扣除一次账户余额
                                        TtPartAccountDefinePO srcPO = new TtPartAccountDefinePO();
                                        srcPO.setAccountId(Long.valueOf(accountId));
                                        if (dao.select(srcPO).size() > 0) {
                                            TtPartAccountDefinePO updatePo = new TtPartAccountDefinePO();
                                            //当前余额减去已开票金额(无税金额+税额）modify by yuan 20131009
                                            //updatePo.setAccountSum(((TtPartAccountDefinePO) dao.select(srcPO).get(0)).getAccountSum() - (Double.valueOf(taxAmount) + Double.valueOf(noTaxAmount)));
                                            //取出库金额
                                            updatePo.setAccountSum(((TtPartAccountDefinePO) dao.select(srcPO).get(0)).getAccountSum() - outstockMainPO.getAmount());
                                            updatePo.setUpdateDate(new Date());
                                            updatePo.setUpdateBy(logonUser.getUserId());

                                            dao.update(srcPO, updatePo);
                                        }
                                        //插入已开票金额
                                        TtPartAccountHistoryPO ahPo = new TtPartAccountHistoryPO();
                                        ahPo.setHistroryId(billId);
                                        ahPo.setAccountId(Long.parseLong(accountId));
                                        ahPo.setChildorgId(Long.parseLong(dlrId));
                                        ahPo.setChildorgCode(dlrCode);
                                        ahPo.setChildorgName(dlrName);
                                        ahPo.setParentorgId(Long.parseLong(orgId));
                                        ahPo.setParentorgCode(orgCode);
                                        ahPo.setParentorgName(orgName);
                                        ahPo.setAcountKind(Constant.FIXCODE_CURRENCY_01);
                                        ahPo.setAcountType(Constant.FIXCODE_ACCOUNT_TYPE_02);
                                        ahPo.setAmount(outstockMainPO.getAmount());//开票金额=无税金额+税额
                                        ahPo.setRemark(remark);
                                        ahPo.setInvoiceNo(invCode + "" + invNum);
                                        ahPo.setCreateBy(userId);
                                        ahPo.setCreateDate(date);

                                        dao.insert(ahPo);
                                    }
                                }
                            }
                            //已经没有必要
                            /*String soCodeStr = " AND BD.SO_CODE = '" + soCodes + "' ";

                            //更新开票表信息
                            List<Map<String, Object>> bmList = dao.CheckBillInfo(soCodeStr);

                            if (null != bmList && bmList.size() > 0) {
                                bmSelPo.setSoCode(soCodes);
                                bmUpdPo.setBillNo(invCode + "" + invNum);
                                bmUpdPo.setBillAmount(Double.parseDouble(taxAmount) + Double.parseDouble(noTaxAmount));//开票金额
                                bmUpdPo.setBillAmountnotax(Double.parseDouble(noTaxAmount));//无税金额
                                bmUpdPo.setTax(Float.parseFloat(tax));//税率
                                bmUpdPo.setTaxAmount(Double.valueOf(taxAmount));//税额
                                bmUpdPo.setBillBy(invUser);
                                bmUpdPo.setBillDate(sdf.parse(invDate));
                                bmUpdPo.setCreateBy(userId);
                                bmUpdPo.setCreateDate(date);
                                bmUpdPo.setUpdateBy(userId);
                                bmUpdPo.setUpdateDate(date);
                                bmUpdPo.setRemark(salCode);//用于获取订货单位

                                dao.update(bmSelPo, bmUpdPo);
                            } else {
                                bmPo.setBillId(billId);
                                bmPo.setSoCode(soCodes);
                                bmPo.setBillNo(invCode + invNum);
                                bmPo.setBillAmount(Double.parseDouble(taxAmount) + Double.parseDouble(noTaxAmount));//开票金额
                                bmPo.setBillAmountnotax(Double.parseDouble(noTaxAmount));//无税金额
                                bmPo.setTax(Float.parseFloat(tax));//税率
                                bmPo.setTaxAmount(Double.valueOf(bmArr[11]));//税额
                                bmPo.setBillBy(invUser);
                                bmPo.setBillDate(sdf.parse(invDate));
                                bmPo.setCreateBy(userId);
                                bmPo.setCreateDate(date);
                                bmPo.setUpdateBy(userId);
                                bmPo.setUpdateDate(date);
                                bmPo.setRemark(salCode);//用于获取订货单位

                                dao.insert(bmPo);
                            }*/
                        } /*else if (null != strTmp) {  //modify by yuan 20130921 start
                            String mark2 = "";
                            if (strTmp.startsWith(mark21)) {
                                mark2 = mark21;
                            } else if (strTmp.startsWith(mark22)) {
                                mark2 = mark22;
                            } else if (strTmp.startsWith(mark23)) {
                                mark2 = mark23;
                            } else if (strTmp.startsWith(mark24)) {
                                mark2 = mark24;
                            }

                            if (!"".equals(mark2)) {
                                bdPo = new TtPartBillDtlPO();
                                strTmp = strTmp.substring(mark2.length(), strTmp.length() - mark3.length());

                                String partName = "";
                                String partCode = "";
                                String unit = "";
                                String billQty = "";
                                String billAmtNoTax = "";
                                String tax = "";
                                String taxAmount = "";
                                String discount = "";
                                String ukNum = "";

                                String msStr1 = "（不含税的开票金额）";
                                String msStr2 = "（税额）";

                                String btArr[] = strTmp.split(spltStr);

                                partName = btArr[0];
                                partCode = btArr[1];
                                unit = btArr[2];
                                billQty = btArr[3];
                                String bdStr = btArr[4];
                                if (null != bdStr && bdStr.indexOf(msStr1) > -1) {
                                    billAmtNoTax = bdStr.replace(msStr1, "");
                                } else {
                                    billAmtNoTax = bdStr;
                                }
                                tax = btArr[5];
                                bdStr = btArr[6];
                                if (null != bdStr && bdStr.indexOf(msStr2) > -1) {
                                    taxAmount = bdStr.replace(msStr2, "");
                                } else {
                                    taxAmount = bdStr;
                                }
                                ukNum = btArr[7];
                                discount = btArr[8];

                                bdPo.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
                                bdPo.setBillId(billId);
                                bdPo.setPartCode(partCode);
                                bdPo.setPartCname(partName);
                                bdPo.setUnit(unit);
                                bdPo.setBillQty(Integer.parseInt(billQty));
                                bdPo.setBillAmountnotax(Double.parseDouble(billAmtNoTax));
                                bdPo.setTax(Float.parseFloat(tax));
                                bdPo.setTaxAmount(Double.parseDouble(taxAmount));
                                bdPo.setBillAmunt(Double.parseDouble(billAmtNoTax) + Double.parseDouble(taxAmount));
                                bdPo.setDiscount(Float.parseFloat(discount));
                                bdPo.setImpBy(userId);
                                bdPo.setImpDate(date);

                                dao.insert(bdPo);
                            }
                        } else {

                        }*///end
                    }
                }
                partInvoiceQueryInit();
            }

        } catch (BizException e) {
            logger.error(logonUser, e);
            act.setException(e);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "开票结果导入");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param : @param importFile
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title : 验证导入的开票信息
     */
    private String checkInvImp(FileObject importFile) {
        String str = "";

        try {
            if (null != importFile) {
                ReaderUtil txtReader = new ReaderUtil(importFile.getContent(), "\t");
                String mark1 = "//发票";
                String spltStr = "~~";

                List<String> list = txtReader.getList();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (null != list.get(i) && list.get(i).startsWith(mark1)) {
                        /*String strTmp = list.get(i + 1);//读取标志位下一行

                        String salCode = "";
                        String msStr3 = "（销售单号）";
                        String bmArr[] = strTmp.split(spltStr);

                        String tempStr = bmArr[5];
                        if (tempStr.indexOf(msStr3) > -1) {
                            salCode = tempStr.replace(msStr3, "");
                        } else {
                            salCode = tempStr;
                        }*/

                        /*StringBuffer sbOutQry = new StringBuffer();
                        sbOutQry.append(" AND BD.SO_CODE = '" + salCode + "' ");

                        //配件出库表验证 1
                        List<Map<String, Object>> outList = dao
                                .getOutStockInfo(sbOutQry.toString());*/

                        //此处如果要验证销售单号请参考791行组装销售单号
                      /*  if (null == outList || outList.size() <= 0) {
                            str += "销售单号【" + salCode + "】不存在或不合法!,";
                            continue;
                        }*/

                      /*  String soCodeStr = " AND BD.SO_CODE = '" + salCode
                                + "' AND BD.IS_INV = '" + Constant.IF_TYPE_YES
                                + "'";*/

                        //配件出库表验证 2此处就
                       /* List<Map<String, Object>> bmList = dao
                                .getOutStockInfo(soCodeStr);

                        if (null != bmList && bmList.size() > 0) {
                            str += "销售单号【" + salCode + "】开票信息已导入，不能重复导入!,";
                            continue;
                        }*/
                    }
                }
            } else {
                str = "开票信息不能为空!,";
            }
        } catch (Exception e) {
            str = "开票信息读取失败!,";
        }
        return str;
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-16
     * @Title : 导出财务金税发票信息
     */
    public void exportPartInvoiceExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
//            String orderCode = CommonUtils.checkNull(request
//                    .getParamValue("orderCode")); // 订货单号
            String sellCode = CommonUtils.checkNull(request
                    .getParamValue("sellCode")); // 销售单号
            String outCode = CommonUtils.checkNull(request
                    .getParamValue("outCode")); // 出库单号
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 订货单位
            String sellerName = CommonUtils.checkNull(request
                    .getParamValue("sellerName")); // 销售单位
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 订单类型
            String invoOutState = CommonUtils.checkNull(request
                    .getParamValue("invoOutState")); // 金税导出状态
//            String whId = CommonUtils.checkNull(request
//                    .getParamValue("whId")); // 出库仓库ID
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 出库开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 出库截止时间
            String finChkSDate = CommonUtils.checkNull(request
                    .getParamValue("finChkSDate")); // 财务审核开始时间
            String finChkEDate = CommonUtils.checkNull(request
                    .getParamValue("finChkEDate")); // 财务审核截止时间
            String dlrInvTpe = CommonUtils.checkNull(request
                    .getParamValue("dlrInvTpe")); // 开票类型
            String invWay = CommonUtils.checkNull(request
                    .getParamValue("invWay")); // 开票方式
            String isInv = CommonUtils.checkNull(request
                    .getParamValue("isInv")); // 是否开票
            String inVo = CommonUtils.checkNull(request
                    .getParamValue("inVo")); // 发票号
            String isNbdw = CommonUtils.checkNull(request
                    .getParamValue("isNbdw")); // 是否内部单位

            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            /*if (null != orderCode && !"".equals(orderCode)) {
                sbString.append(" AND OMM.ORDER_CODE LIKE '%" + orderCode + "%' ");
            }*/
            if (null != sellCode && !"".equals(sellCode)) {
                sbString.append(" AND OM.SO_CODE LIKE '%" + sellCode + "%' ");
            }
            if (null != outCode && !"".equals(outCode)) {
                sbString.append(" AND OM.OUT_CODE LIKE '%" + outCode + "%' ");
            }
            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND OM.DEALER_NAME LIKE '%" + dealerName + "%' ");
            }
            if (null != sellerName && !"".equals(sellerName)) {
                sbString.append(" AND OM.SELLER_NAME LIKE '%" + sellerName + "%' ");
            }
            /*if (null != whId && !"".equals(whId)) {
                sbString.append(" AND OM.WH_ID = '" + whId + "' ");
            }*/
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND OM.ORDER_TYPE = '" + orderType + "' ");
            }
            if (null != invoOutState && !"".equals(invoOutState)) {
                sbString.append(" AND OM.IS_EXPORT = '" + invoOutState + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != finChkSDate && !"".equals(finChkSDate)) {
                sbString.append(" AND TO_CHAR(SM.FCAUDIT_DATE,'yyyy-MM-dd') >= '" + finChkSDate + "' ");
            }
            if (null != finChkEDate && !"".equals(finChkEDate)) {
                sbString.append(" AND TO_CHAR(SM.FCAUDIT_DATE,'yyyy-MM-dd') <= '" + finChkEDate + "' ");
            }

            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND OM.SELLER_ID = '" + parentOrgId + "' ");
            }
            if (null != invWay && !"".equals(invWay)) {
                sbString.append(" AND OM.INV_WAY = '" + invWay + "' ");
            }
            if (null != dlrInvTpe && !"".equals(dlrInvTpe)) {
                sbString.append(" AND BD.INV_TYPE = '" + dlrInvTpe + "' ");
            }
            if (null != isInv && !"".equals(isInv)) {
                sbString.append(" AND OM.IS_INV = '" + isInv + "' ");
            }
            if (null != inVo && !"".equals(inVo)) {
                sbString.append(" AND OM.BILL_NO like '%" + inVo + "%' ");
            }
            if (null != isNbdw && !"".equals(isNbdw)) {

                sbString.append(" AND EXISTS ( SELECT 1 FROM TM_DEALER D WHERE D.DEALER_ID = OM.DEALER_ID AND D.IS_NBDW = '" + isNbdw + "' ) ");
            }

            String[] head = new String[20];
            head[0] = "序号";
            head[1] = "销售单号";
            head[2] = "订货单位";
            head[3] = "销售金额(元)";
            head[4] = "财务审核人";
            head[5] = "财务审核日期";
            head[6] = "出库仓库";
            head[7] = "出库日期";
            head[8] = "导出状态";
            head[9] = "是否开票";
            head[10] = "发票号码";
            head[11] = "金税导出人";
            head[12] = "导出时间";
            head[13] = "订单类型";
            head[14] = "开票人";
            head[15] = "开票日期";
            head[16] = "改票人";
            head[17] = "改票日期";


            List<Map<String, Object>> list = dao.queryPartInvoicesList(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[20];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("SO_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        int ordType = Integer.parseInt(CommonUtils.checkNull(map.get("ORDER_TYPE")).toString());
                        if (orderType1 == ordType) {
                            detail[13] = "紧急";
                        } else if (orderType2 == ordType) {
                            detail[13] = "常规";
                        } else if (orderType3 == ordType) {
                            detail[13] = "计划";
                        } else if (orderType4 == ordType) {
                            detail[13] = "直发";
                        } else if (orderType5 == ordType) {
                            detail[13] = "调拨";
                        }
                        detail[3] = CommonUtils.checkNull(map
                                .get("AMOUNT"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("F_NAME"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("FCAUDIT_DATE"));
                        detail[6] = CommonUtils
                                .checkNull(map.get("WH_NAME"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("CREATE_DATE"));
                        int invOutState = Integer.parseInt(CommonUtils.checkNull(map.get("IS_EXPORT")));
                        if (invOutState1 == invOutState) {
                            detail[8] = "未导出";
                        } else if (invOutState2 == invOutState) {
                            detail[8] = "已导出";
                        }
                        detail[11] = CommonUtils
                                .checkNull(map.get("NAME"));
                        detail[12] = CommonUtils.checkNull(map
                                .get("EXPORT_DATE"));

                        int invState = Integer.parseInt(CommonUtils.checkNull(map.get("IS_INV")));
                        int invoicedVal = Constant.IF_TYPE_YES;
                        if (invoicedVal == invState) {
                            detail[9] = "是";
                        } else {
                            detail[9] = "否";
                        }

                        detail[10] = CommonUtils.checkNull(map
                                .get("BILL_NO"));
                        detail[14] = CommonUtils.checkNull(map
                                .get("BILL_BY"));
                        detail[15] = CommonUtils.checkNull(map
                                .get("BILL_DATE"));
                        detail[16] = CommonUtils.checkNull(map
                                .get("BILL_UPDATE_BY"));
                        detail[17] = CommonUtils.checkNull(map
                                .get("BILL_UPDATE_DATE"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "财务金税发票信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件库存盘点调整申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-16
     * @Title : 导出金税文本信息
     */
    public void exportPartInvoTaxTxt() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {

            String fileName = "金税文本信息" + CommonUtils.printDate(new Date());
            this.exportTxt(fileName, ActionContext.getContext().getResponse(), request);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出金税文本信息");
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
     * @throws : LastDate    : 2013-5-16
     * @Title : 文件导出为txt文件
     */
    public Object exportTxt(String fileName, ResponseWrapper response, RequestWrapper request)
            throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
//        String orderCode = CommonUtils.checkNull(request
//                .getParamValue("orderCode")); // 订货单号
        /*String sellCode = CommonUtils.checkNull(request
                .getParamValue("sellCode")); // 销售单号
        String outCode = CommonUtils.checkNull(request
                .getParamValue("outCode")); // 出库单号
        String dealerName = CommonUtils.checkNull(request
                .getParamValue("dealerName")); // 订货单位
        String sellerName = CommonUtils.checkNull(request
                .getParamValue("sellerName")); // 销售单位
        String orderType = CommonUtils.checkNull(request
                .getParamValue("orderType")); // 订单类型
        String invoOutState = CommonUtils.checkNull(request
                .getParamValue("invoOutState")); // 金税导出状态
        String whId = CommonUtils.checkNull(request
                .getParamValue("whId")); // 出库仓库ID
        String checkSDate = CommonUtils.checkNull(request
                .getParamValue("checkSDate")); // 出库开始时间
        String checkEDate = CommonUtils.checkNull(request
                .getParamValue("checkEDate")); // 出库截止时间
*/
        String outOrdIds = CommonUtils.checkNull(request
                .getParamValue("checkedOptions")); // 选中的单据出库单ID
        /*String dlrInvTpe = CommonUtils.checkNull(request
                .getParamValue("dlrInvTpe")); // 开票类型
        String invWay = CommonUtils.checkNull(request
                .getParamValue("invWay")); // 开票方式
*/
        outOrdIds = outOrdIds.trim().substring(0, outOrdIds.length() - 1);
//        String outIdsArr[] = outOrdIds.trim().split(",");
        outOrdIds = outOrdIds.trim().replace(",", "','");

        String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
        StringBuffer sbString = new StringBuffer();
        /*if (null != orderCode && !"".equals(orderCode)) {
            sbString.append(" AND OMM.ORDER_CODE LIKE '%" + orderCode + "%' ");
        }*/
        /*if (null != sellCode && !"".equals(sellCode)) {
            sbString.append(" AND OM.SO_CODE LIKE '%" + sellCode + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sbString.append(" AND OM.OUT_CODE LIKE '%" + outCode + "%' ");
        }
        if (null != dealerName && !"".equals(dealerName)) {
            sbString.append(" AND OM.DEALER_NAME LIKE '%" + dealerName + "%' ");
        }
        if (null != sellerName && !"".equals(sellerName)) {
            sbString.append(" AND OM.SELLER_NAME LIKE '%" + sellerName + "%' ");
        }
        if (null != whId && !"".equals(whId)) {
            sbString.append(" AND OM.WH_ID = '" + whId + "' ");
        }
        if (null != orderType && !"".equals(orderType)) {
            sbString.append(" AND OM.ORDER_TYPE = '" + orderType + "' ");
        }
        if (null != invoOutState && !"".equals(invoOutState)) {
            sbString.append(" AND OM.IS_EXPORT = '" + invoOutState + "' ");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sbString.append(" AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sbString.append(" AND TO_CHAR(OM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }*/

        if (null != parentOrgId && !"".equals(parentOrgId)) {
            sbString.append(" AND OM.SELLER_ID = '" + parentOrgId + "' ");
        }
        /*if (null != invWay && !"".equals(invWay)) {
            sbString.append(" AND OM.INV_WAY = '" + invWay + "' ");
        }
        if (null != dlrInvTpe && !"".equals(dlrInvTpe)) {
            sbString.append(" AND BD.INV_TYPE = '" + dlrInvTpe + "' ");
        }*/

        sbString.append(" AND OM.OUT_ID IN('" + outOrdIds + "') ");

        String opName = logonUser.getName(); //操作人Name
        Long userId = logonUser.getUserId();
        String file = fileName + ".txt";
        Date date = new Date();
        StringBuffer headStr = new StringBuffer();
        StringBuffer titlInfo;
        headStr.append("SJJK0101~~销售单据传入~~");
        DateFormat locDate = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        headStr.append(locDate.format(date));
        headStr.append("昌河配件公司销售业务");

        OutputStream out = null;
        PrintStream pw = null;
        try {
            file = new String(file.getBytes("GB2312"), "ISO8859-1");
            act.getResponse().setContentType("application/octet-stream");
            act.getResponse().addHeader("Content-Disposition", "attachment;filename=" + file);

            out = response.getOutputStream();
            pw = new PrintStream(out, false, "GBK");

            pw.println(headStr);
            float maxNum = 30;//默认为30

            List<Map<String, Object>> dlrIdList = dao.queryInvoDealerList(sbString.toString());

            int markNum = 1;
            String markStr = "";
            String ysfStrLeft = "昌河国产件-运输加价~~批~~YSF~~1~~";
            String ysfStrRight = "~~0.17~~税目~~0~~~~~~~~~~1";
            DecimalFormat df = new DecimalFormat("########0.00");
            if (null != dlrIdList && dlrIdList.size() > 0) {
                for (int m = 0; m < dlrIdList.size(); m++) {
                    String tempStr = sbString.toString();
                    String dealerIdTmp = dlrIdList.get(m).get("DEALER_ID").toString();
                    tempStr += " AND OM.DEALER_ID = '" + dealerIdTmp + "'";

                    List<Map<String, Object>> titlList = dao.queryInvoTxtTitleList(tempStr);
                    int titleListSize = titlList.size();

                    //如果小于30销售单
                    if (maxNum >= titleListSize) {
                        markStr = "//单据" + markNum + " ";
                        markNum++;
                        pw.println(markStr.substring(0, markStr.length() - 1));

                        List<Map<String, Object>> dtlList;

                        //更新金税文本导出状态
                        String outIdTmp = "";
                        double ysf = 0.00;
                        TtPartOutstockMainPO selPo = null;
                        TtPartOutstockMainPO updPo = null;

                        titlInfo = new StringBuffer();
                        String soCodeTmp = "";
                        String soCodePartTmp = "";
                        for (int i = 0; i < titleListSize; i++) {
                            String str = "";
                            str = titlList.get(i).get("SO_CODE").toString();
//    						soCodeTmp += str + ",";
                            soCodeTmp = str;
                            soCodePartTmp += str.substring(str.length() - 6, (str.length())) + ",";
                            ysf += Double.parseDouble(titlList.get(i).get("OM_FREIGHT").toString());
                        }

                        if (!"".equals(soCodePartTmp)) {
                            soCodePartTmp = soCodePartTmp.substring(0, soCodePartTmp.length() - 1);
                        }

                        String count = "0";//明细总数

                        String dealerNameTmp = titlList.get(0).get("TAX_NAME").toString();
                        String taxNoTmp = titlList.get(0).get("TAX_NO").toString();
                        String addrTmp = titlList.get(0).get("ADDR").toString();
                        String telTmp = titlList.get(0).get("TEL").toString();
                        String bankTmp = titlList.get(0).get("BANK").toString();
                        String accountTmp = titlList.get(0).get("ACCOUNT").toString();
                        String saleDateTmp = titlList.get(0).get("SALE_DATE").toString();

                        dtlList = dao.queryInvoTxtDtlList(tempStr);

                        //ADD BY YUAN 20130918    有
                        if (ysf > 0) {
                            count = (dtlList.size() + 1) + "";
                        } else {
                            count = dtlList.size() + "";
                        }

                        titlInfo.append("" + soCodeTmp);
                        titlInfo.append("~~" + count);
                        titlInfo.append("~~" + dealerNameTmp);
                        titlInfo.append("~~" + taxNoTmp);
                        titlInfo.append("~~" + addrTmp + telTmp);
                        titlInfo.append("~~" + bankTmp + accountTmp);
                        titlInfo.append("~~" + soCodePartTmp);
                        titlInfo.append("~~" + opName);
                        titlInfo.append("~~" + opName);

                        pw.println(titlInfo);
                        pw.println("");

                        StringBuffer dtlStr = new StringBuffer();
                        if (null != dtlList && dtlList.size() > 0) {
                            if (ysf > 0) {
                                dtlStr.append(ysfStrLeft + df.format(ysf) + ysfStrRight);
                                dtlStr.append("\r\n");
                            }
                            for (int i = 0; i < dtlList.size(); i++) {
                                dtlStr.append(dtlList.get(i).get("INVO_TXT_DTL").toString());
                                dtlStr.append("\r\n");
                            }
                        }
                        pw.println(dtlStr);

                        for (int j = 0; j < titleListSize; j++) {
                            selPo = new TtPartOutstockMainPO();
                            updPo = new TtPartOutstockMainPO();
                            outIdTmp = titlList.get(j).get("OUT_ID").toString();

                            selPo.setOutId(Long.parseLong(outIdTmp));

                            updPo.setIsExport(invOutState2);
                            updPo.setExportBy(userId);
                            updPo.setExportDate(date);
                            updPo.setUpdateBy(userId);
                            updPo.setUpdateDate(date);

                            dao.update(selPo, updPo);
                        }
                    } else {
                        int countDiv = 2;
                        countDiv = (int) Math.ceil(titleListSize / maxNum);
                        for (int k = 0; k < countDiv; k++) {
                            if ((k + 1) * maxNum < titleListSize) {
                                markStr = "//单据" + markNum + " ";
                                markNum++;
                                pw.println(markStr.substring(0, markStr.length() - 1));

                                List<Map<String, Object>> dtlList;

                                //更新金税文本导出状态
                                String outIdTmp = "";
                                double ysf = 0.00;
                                TtPartOutstockMainPO selPo = null;
                                TtPartOutstockMainPO updPo = null;

                                titlInfo = new StringBuffer();
                                String soCodeTmp = "";
                                String soCodePartTmp = "";
                                String sqlAdd = " AND OM.SO_CODE IN(";

                                for (int p = Math.round((k * maxNum)); p < ((k + 1) * maxNum); p++) {

                                    String str = "";
                                    str = titlList.get(p).get("SO_CODE").toString();
//            						soCodeTmp += str + ",";
                                    soCodeTmp = str;
                                    sqlAdd += "'" + str + "', ";
                                    soCodePartTmp += str.substring(str.length() - 6, (str.length())) + ",";
                                    ysf += Double.parseDouble(titlList.get(p).get("OM_FREIGHT").toString());

                                }

                                sqlAdd = sqlAdd.substring(0, sqlAdd.length() - 2);
                                sqlAdd += ") ";

                                if (!"".equals(soCodePartTmp)) {
                                    soCodePartTmp = soCodePartTmp.substring(0, soCodePartTmp.length() - 1);
                                }

                                String count = "0";//明细总数

                                String dealerNameTmp = titlList.get(Math.round((k * maxNum))).get("DEALER_NAME").toString();
                                String taxNoTmp = titlList.get(Math.round((k * maxNum))).get("TAX_NO").toString();
                                String addrTmp = titlList.get(Math.round((k * maxNum))).get("ADDR").toString();
                                String telTmp = titlList.get(Math.round((k * maxNum))).get("TEL").toString();
                                String bankTmp = titlList.get(Math.round((k * maxNum))).get("BANK").toString();
                                String accountTmp = titlList.get(Math.round((k * maxNum))).get("ACCOUNT").toString();
                                String saleDateTmp = titlList.get(Math.round((k * maxNum))).get("SALE_DATE").toString();

                                String sqlStr = tempStr + sqlAdd;

                                dtlList = dao.queryInvoTxtDtlList(sqlStr);

                                //ADD BY YUAN 20130918    有
                                if (ysf > 0) {
                                    count = (dtlList.size() + 1) + "";
                                } else {
                                    count = dtlList.size() + "";
                                }

                                titlInfo.append("" + soCodeTmp);
                                titlInfo.append("~~" + count);
                                titlInfo.append("~~" + dealerNameTmp);
                                titlInfo.append("~~" + taxNoTmp);
                                titlInfo.append("~~" + addrTmp + telTmp);
                                titlInfo.append("~~" + bankTmp + accountTmp);
                                titlInfo.append("~~" + soCodePartTmp);
                                titlInfo.append("~~" + opName);
                                titlInfo.append("~~" + opName);

                                pw.println(titlInfo);
                                pw.println("");

                                StringBuffer dtlStr = new StringBuffer();
                                if (null != dtlList && dtlList.size() > 0) {
                                    if (ysf > 0) {
                                        dtlStr.append(ysfStrLeft + df.format(ysf) + ysfStrRight);
                                        dtlStr.append("\r\n");
                                    }
                                    for (int i = 0; i < dtlList.size(); i++) {
                                        dtlStr.append(dtlList.get(i).get("INVO_TXT_DTL").toString());
                                        dtlStr.append("\r\n");
                                    }
                                }
                                pw.println(dtlStr);

                                for (int j = 0; j < titleListSize; j++) {
                                    selPo = new TtPartOutstockMainPO();
                                    updPo = new TtPartOutstockMainPO();
                                    outIdTmp = titlList.get(j).get("OUT_ID").toString();

                                    selPo.setOutId(Long.parseLong(outIdTmp));

                                    updPo.setIsExport(invOutState2);
                                    updPo.setExportBy(userId);
                                    updPo.setExportDate(date);
                                    updPo.setUpdateBy(userId);
                                    updPo.setUpdateDate(date);

                                    dao.update(selPo, updPo);
                                }
                            } else {
                                markStr = "//单据" + markNum + " ";
                                markNum++;
                                pw.println(markStr.substring(0, markStr.length() - 1));

                                List<Map<String, Object>> dtlList;

                                //更新金税文本导出状态
                                String outIdTmp = "";
                                double ysf = 0.00;
                                TtPartOutstockMainPO selPo = null;
                                TtPartOutstockMainPO updPo = null;

                                titlInfo = new StringBuffer();
                                String soCodeTmp = "";
                                String soCodePartTmp = "";
                                String sqlAdd = " AND OM.SO_CODE IN(";

                                for (int p = Math.round((k * maxNum)); p < titleListSize; p++) {

                                    String str = "";
                                    str = titlList.get(p).get("SO_CODE").toString();
//            						soCodeTmp += str + ",";
                                    soCodeTmp = str;
                                    sqlAdd += "'" + str + "', ";
                                    soCodePartTmp += str.substring(str.length() - 6, (str.length())) + ",";
                                    ysf += Double.parseDouble(titlList.get(p).get("OM_FREIGHT").toString());

                                }

                                sqlAdd = sqlAdd.substring(0, sqlAdd.length() - 2);
                                sqlAdd += ") ";

                                if (!"".equals(soCodePartTmp)) {
                                    soCodePartTmp = soCodePartTmp.substring(0, soCodePartTmp.length() - 1);
                                }

                                String count = "0";//明细总数

                                String dealerNameTmp = titlList.get(Math.round((k * maxNum))).get("DEALER_NAME").toString();
                                String taxNoTmp = titlList.get(Math.round((k * maxNum))).get("TAX_NO").toString();
                                String addrTmp = titlList.get(Math.round((k * maxNum))).get("ADDR").toString();
                                String telTmp = titlList.get(Math.round((k * maxNum))).get("TEL").toString();
                                String bankTmp = titlList.get(Math.round((k * maxNum))).get("BANK").toString();
                                String accountTmp = titlList.get(Math.round((k * maxNum))).get("ACCOUNT").toString();
                                String saleDateTmp = titlList.get(Math.round((k * maxNum))).get("SALE_DATE").toString();

                                String sqlStr = tempStr + sqlAdd;

                                dtlList = dao.queryInvoTxtDtlList(sqlStr);

                                //ADD BY YUAN 20130918    有
                                if (ysf > 0) {
                                    count = (dtlList.size() + 1) + "";
                                } else {
                                    count = dtlList.size() + "";
                                }

                                titlInfo.append("" + soCodeTmp);
                                titlInfo.append("~~" + count);
                                titlInfo.append("~~" + dealerNameTmp);
                                titlInfo.append("~~" + taxNoTmp);
                                titlInfo.append("~~" + addrTmp + telTmp);
                                titlInfo.append("~~" + bankTmp + accountTmp);
                                titlInfo.append("~~" + soCodePartTmp);
                                titlInfo.append("~~" + opName);
                                titlInfo.append("~~" + opName);

                                pw.println(titlInfo);
                                pw.println("");

                                StringBuffer dtlStr = new StringBuffer();
                                if (null != dtlList && dtlList.size() > 0) {
                                    if (ysf > 0) {
                                        dtlStr.append(ysfStrLeft + df.format(ysf) + ysfStrRight);
                                        dtlStr.append("\r\n");
                                    }
                                    for (int i = 0; i < dtlList.size(); i++) {
                                        dtlStr.append(dtlList.get(i).get("INVO_TXT_DTL").toString());
                                        dtlStr.append("\r\n");
                                    }
                                }
                                pw.println(dtlStr);

                                for (int j = 0; j < titleListSize; j++) {
                                    selPo = new TtPartOutstockMainPO();
                                    updPo = new TtPartOutstockMainPO();
                                    outIdTmp = titlList.get(j).get("OUT_ID").toString();

                                    selPo.setOutId(Long.parseLong(outIdTmp));

                                    updPo.setIsExport(invOutState2);
                                    updPo.setExportBy(userId);
                                    updPo.setExportDate(date);
                                    updPo.setUpdateBy(userId);
                                    updPo.setUpdateDate(date);

                                    dao.update(selPo, updPo);
                                }

                            }
                        }

                    }


                }
            }

            pw.flush();
            pw.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != pw) {
                pw.close();
            }
            if (null != out) {
                out.close();
            }
        }
        return null;
    }

}
