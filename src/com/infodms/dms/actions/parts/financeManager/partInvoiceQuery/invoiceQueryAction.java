package com.infodms.dms.actions.parts.financeManager.partInvoiceQuery;

import com.infodms.dms.actions.parts.storageManager.partStoInveManager.entityInvImpAction;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.financeManager.partInvoiceQuery.invoiceQueryDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.exception.BizException;
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
import java.util.*;

/**
 * @author : huchao
 *         LastDate     : 2013-7-10
 * @ClassName : invoiceQueryAction
 * @Description : 财务管理-开票信息查询
 */
public class invoiceQueryAction {

    private static final invoiceQueryDao dao = invoiceQueryDao.getInstance();
    private static final int INV_TYPE_01 = Constant.DLR_INVOICE_TYPE_01;//增值税专用发票
    private static final int INV_TYPE_02 = Constant.DLR_INVOICE_TYPE_02;//增值税普通发票
    private static final String INVOICE_QUERY_URL = "/jsp/parts/financeManager/partInvoiceQuery/invoiceQuery.jsp";//开票信息查询页面
    private static final String INVOICE_PART_DTL = "/jsp/parts/financeManager/partInvoiceQuery/invPartDetail.jsp";//开票配件详情页面
    private static final String INVOICE_PRINT_DTL = "/jsp/parts/financeManager/partInvoiceQuery/invoicePrint.jsp";//开票配件打印页面


    private static final String PRINT_TEMPLATE_URL = "/jsp/parts/financeManager/partInvoiceQuery/printTemplate.jsp";


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
     * @throws : LastDate : 2013-7-10
     * @Title :
     * @Description: 开票信息查询页面初始化
     */
    public void invoiceQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String curYear = PlanUtil.getCurrentYear();
            String curMonth = PlanUtil.getCurrentMonth();

            act.setOutData("curYear", curYear);
            act.setOutData("curMonth", curMonth);
            act.setForword(INVOICE_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "开票信息查询初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :  开票信息查询信息
     */
    public void invoiceSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            String searchType = CommonUtils.checkNull(request
                    .getParamValue("searchType")); // 查询类型
            PageResult<Map<String, Object>> ps = null;
            if ("normal".equals(searchType)) {
                String sellCode = CommonUtils.checkNull(request
                        .getParamValue("sellCode")); // 销售单号
                String invCodeNum = CommonUtils.checkNull(request
                        .getParamValue("invCodeNum")); // 发票号码
                String dealerName = CommonUtils.checkNull(request
                        .getParamValue("dealerName")); // 订货单位
                String checkSDate = CommonUtils.checkNull(request
                        .getParamValue("checkSDate")); // 开票开始时间
                String checkEDate = CommonUtils.checkNull(request
                        .getParamValue("checkEDate")); // 开票票截止时间
                String dlrInvTpe = CommonUtils.checkNull(request
                        .getParamValue("dlrInvTpe")); // 开票类型
                String invPerson = CommonUtils.checkNull(request
                        .getParamValue("invPerson")); // 开票人

                StringBuffer sbString = new StringBuffer();
                if (null != invCodeNum && !"".equals(invCodeNum)) {
                    sbString.append(" AND UPPER(OM.BILL_NO) LIKE '%" + invCodeNum.trim().toUpperCase() + "%' ");
                }
                if (null != sellCode && !"".equals(sellCode)) {
                    sbString.append(" AND UPPER(OM.SO_CODE) LIKE '%" + sellCode.trim().toUpperCase() + "%' ");
                }
                if (null != invPerson && !"".equals(invPerson)) {
                    sbString.append(" AND OM.BILL_BY LIKE '%" + invPerson.trim() + "%' ");
                }
                if (null != dealerName && !"".equals(dealerName)) {
                    sbString.append(" AND OM.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
                }
                if (null != checkSDate && !"".equals(checkSDate)) {
                    sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
                }
                if (null != checkEDate && !"".equals(checkEDate)) {
                    sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
                }
                if (null != dlrInvTpe && !"".equals(dlrInvTpe)) {
                    sbString.append(" AND BD.INV_TYPE = '" + dlrInvTpe + "' ");
                }

                ps = dao.queryInvoices(sbString.toString(), Constant.PAGE_SIZE, curPage);
            } else {
                String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称
                String year = CommonUtils.checkNull(request.getParamValue("year1"));
                String month = CommonUtils.checkNull(request.getParamValue("month1"));
                String month2 = CommonUtils.checkNull(request.getParamValue("month2"));

                StringBuffer sbString = new StringBuffer();

                if (null != dealerCode && !"".equals(dealerCode)) {
                    sbString.append(" AND UPPER(OM.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' ");
                }
                if (null != dealerName && !"".equals(dealerName)) {
                    sbString.append(" AND OM.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
                }
                if (null != year && !"".equals(year) && null != month && !"".equals(month)) {
                    int numMth = Integer.parseInt(month);
                    if (9 < numMth) {
                        sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM') >= '" + year + "-" + month + "' ");
                    } else {
                        sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM') >= '" + year + "-0" + month + "' ");
                    }

                }
                if (null != year && !"".equals(year) && null != month2 && !"".equals(month2)) {
                    int numMth = Integer.parseInt(month2);
                    if (9 < numMth) {
                        sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM') <= '" + year + "-" + month2 + "' ");
                    } else {
                        sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM') <= '" + year + "-0" + month2 + "' ");
                    }
                }

                ps = dao.countInvoices(sbString.toString(), Constant.PAGE_SIZE, curPage);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询开票信息查询信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :
     * @Description: 查看开票配件详情
     */
    public void viewInvPartDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String valType = CommonUtils.checkNull(request.getParamValue("query"));
            String billId = CommonUtils.checkNull(request.getParamValue("billId"));

            if (null != valType && "query".equals(valType)) {
                String sqlStr = " AND EXISTS (SELECT 1 FROM Tt_Part_Bill_Main d WHERE d.bill_no=m.bill_no AND d.bill_id ='" + billId + "') ";


                Integer curPage = request.getParamValue("curPage") != null ? Integer
                        .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                PageResult<Map<String, Object>> ps = dao.queryInvoicePatrt(sqlStr, Constant.PAGE_SIZE, curPage);

                act.setOutData("ps", ps);
            } else {
                String sqlStr = " AND BM.BILL_ID = '" + billId + "' ";
                List<Map<String, Object>> invList = dao.queryInvoicesList(sqlStr);
                Map<String, Object> invMap = null;
                invMap = invList.get(0);
                int invType = Integer.parseInt(invMap.get("INV_TYPE").toString());
                if (INV_TYPE_01 == invType) {
                    invMap.put("INV_TYPE", "增值税专用发票");
                } else {
                    invMap.put("INV_TYPE", "增值税普通发票");
                }

                act.setOutData("invMap", invMap);
                act.setForword(INVOICE_PART_DTL);
            }

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title : 导出开票信息查询信息
     */
    public void exportInvoiceExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String sellCode = CommonUtils.checkNull(request
                    .getParamValue("sellCode")); // 销售单号
            String invCodeNum = CommonUtils.checkNull(request
                    .getParamValue("invCodeNum")); // 发票号码
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 订货单位
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开票开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 开票票截止时间
            String dlrInvTpe = CommonUtils.checkNull(request
                    .getParamValue("dlrInvTpe")); // 开票类型
            String invPerson = CommonUtils.checkNull(request
                    .getParamValue("invPerson")); // 开票人

            StringBuffer sbString = new StringBuffer();
            if (null != invCodeNum && !"".equals(invCodeNum)) {
                sbString.append(" AND UPPER(OM.BILL_NO) LIKE '%" + invCodeNum.trim().toUpperCase() + "%' ");
            }
            if (null != sellCode && !"".equals(sellCode)) {
                sbString.append(" AND UPPER(OM.SO_CODE) LIKE '%" + sellCode.trim().toUpperCase() + "%' ");
            }
            if (null != invPerson && !"".equals(invPerson)) {
                sbString.append(" AND OM.BILL_BY LIKE '%" + invPerson.trim() + "%' ");
            }
            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND OM.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != dlrInvTpe && !"".equals(dlrInvTpe)) {
                sbString.append(" AND BD.INV_TYPE = '" + dlrInvTpe + "' ");
            }

            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "销售单号";
            head[2] = "发票号码";
            head[3] = "含税开票金额(元)";
            head[4] = "订货单位编码";
            head[5] = "订货单位";
            head[6] = "开票人";
            head[7] = "开票日期";
            head[8] = "开票类型";
            List<Map<String, Object>> list = dao.queryInvoicesList(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("SO_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("BILL_NO"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("BILL_AMOUNT"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("DEALER_CODE"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("BILL_BY"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("BILL_DATE"));
                        int invTypeTmp = Integer.parseInt(CommonUtils.checkNull(map.get("INV_TYPE")).toString());
                        if (INV_TYPE_01 == invTypeTmp) {
                            detail[8] = "增值税专用发票";
                        } else {
                            detail[8] = "增值税普通发票";
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "开票信息查询结果";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出开票信息异常!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-11-5
     * @Title : 汇总导出
     */
    public void exportCountInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称
            String year = CommonUtils.checkNull(request.getParamValue("year1"));
            String month = CommonUtils.checkNull(request.getParamValue("month1"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));

            StringBuffer sbString = new StringBuffer();

            if (null != dealerCode && !"".equals(dealerCode)) {
                sbString.append(" AND UPPER(OM.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' ");
            }
            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND OM.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
            }
            if (null != year && !"".equals(year) && null != month && !"".equals(month)) {
                int numMth = Integer.parseInt(month);
                if (9 < numMth) {
                    sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM') >= '" + year + "-" + month + "' ");
                } else {
                    sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM') >= '" + year + "-0" + month + "' ");
                }

            }
            if (null != year && !"".equals(year) && null != month2 && !"".equals(month2)) {
                int numMth = Integer.parseInt(month2);
                if (9 < numMth) {
                    sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM') <= '" + year + "-" + month2 + "' ");
                } else {
                    sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM') <= '" + year + "-0" + month2 + "' ");
                }
            }

            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "服务商编码";
            head[2] = "服务商名称";
            head[3] = "开票金额(元)";
            head[4] = "开票年月";

            List<Map<String, Object>> list = dao.countInvoList(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("BILL_AMOUNT"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("BILL_MONTH"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "开票信息汇总查询表";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出开票信息异常!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    //明细下载 add zhumingwei 2013-09-28
    public void exportInvoiceExcelDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String sellCode = CommonUtils.checkNull(request
                    .getParamValue("sellCode")); // 销售单号
            String invCodeNum = CommonUtils.checkNull(request
                    .getParamValue("invCodeNum")); // 发票号码
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 订货单位
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开票开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 开票票截止时间
            String dlrInvTpe = CommonUtils.checkNull(request
                    .getParamValue("dlrInvTpe")); // 开票类型
            String invPerson = CommonUtils.checkNull(request
                    .getParamValue("invPerson")); // 开票人

            StringBuffer sbString = new StringBuffer();

            if (null != sellCode && !"".equals(sellCode)) {
                sbString.append(" AND UPPER(OM.SO_CODE) LIKE '%" + sellCode.trim().toUpperCase() + "%' ");
            }
            if (null != invCodeNum && !"".equals(invCodeNum)) {
                sbString.append(" AND UPPER(OM.BILL_NO) LIKE '%" + invCodeNum.trim().toUpperCase() + "%' ");
            }

            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND OM.DEALER_NAME LIKE '%" + dealerName.trim() + "%' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(OM.BILL_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != dlrInvTpe && !"".equals(dlrInvTpe)) {
                sbString.append(" AND d.INV_TYPE = '" + dlrInvTpe + "' ");
            }
            if (null != invPerson && !"".equals(invPerson)) {
                sbString.append(" AND OM.BILL_BY LIKE '%" + invPerson.trim() + "%' ");
            }

            String[] head = new String[7];
            head[0] = "序号";
            head[1] = "服务商编码";
            head[2] = "服务商名称";
            head[3] = "配件属性";
            head[4] = "含税金额(元)";
            head[5] = "发票号";
            head[6] = "开票日期";

            List<Map<String, Object>> list = dao.queryInvoicesDetailList(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[7];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("PART_TYPE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("SALE_AMOUNT"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("BILL_NO"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("BILL_DATE"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "开票信息明细下载";
            this.exportEx(fileName, ActionContext.getContext().getResponse(), request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "开票信息明细下载异常!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void invoicePrintPage() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao1 = PartDlrOrderDao.getInstance();
            List<Map<String, Object>> salerList = dao1.getSaler();
            act.setOutData("curUserId", loginUser.getUserId());
            act.setOutData("salerList", salerList);
            act.setForword(INVOICE_PRINT_DTL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "打印页面初始化失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void invoicePrintQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.getInvoiceAmount(request, Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "打印页面查询失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void printInfo() {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String dealerIds = CommonUtils.checkNull(request.getParamValue("dealerIds"));
            dealerIds = dealerIds.replaceFirst(",", "");
            List<Map<String, Object>> list = dao.getInvoiceAmount(request, dealerIds);
            Map<String, Object> map = new HashMap<String, Object>();
            Calendar c = Calendar.getInstance();
            String year = c.get(Calendar.YEAR) + "";
            String month = ((c.get(Calendar.MONTH)) + 1) + "";
            String day = c.get(Calendar.DAY_OF_MONTH) + "";
            String hour = c.get(Calendar.HOUR) + "";
            map.put("year", year);
            map.put("month", month);
            map.put("day", day);
            map.put("hour", hour);
            act.setOutData("dataMap", map);
            act.setOutData("list", list);
            act.setForword(PRINT_TEMPLATE_URL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "打印失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }
}
