package com.infodms.dms.actions.report.partReport.partStockReport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.ExcelUtil;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
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
import java.text.SimpleDateFormat;
import java.util.*;

public class PurOrderInReport implements PTConstants {

    public Logger logger = Logger.getLogger(PurOrderInReport.class);
    private PurchaseOrderInDao dao = PurchaseOrderInDao.getInstance();


    String pckMaterials_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/pckMaterials.jsp";//包装材料出入库汇总报表
    String pckMaterialsDetail_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/pckMaterialsDetail.jsp";//包装材料出入库明细报表
    String stockAnalysis_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/stockAnalysis.jsp";//库存分析报表(本部)

    String boOrderIssue_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/boOrderIssue.jsp";//BO订单发出数
    String orderIssuedTimely_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/orderIssuedTimely.jsp";//订单发出及时率
    String dealerStorageTimely_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/dealerStorageTimely.jsp";//服务商入库及时率
    String partDlrOrderDtlQuery_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/partDlrOrderDtlQuery.jsp";//服务站订货明细
    String orderMeet_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/orderMeet.jsp";//订单满足率
    String dealerOrderSituation_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/dealerOrderSituation.jsp";//服务站订货情况

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-23
     * @Title :
     * @Description: 入库统计初始化
     */
    public void purOrderInReportInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_PURCHASEORDER_IN_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "入库统计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void pckMaterialsInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);//把日期设置为当月第一天
            c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            act.setOutData("start", CommonUtils.getMonthFirstDay());//
            act.setOutData("end", CommonUtils.getDate());//
            act.setForword(pckMaterials_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "包装材料出入库明细报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void pckMaterialsDetailInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();

            act.setOutData("start", CommonUtils.getBefore(new Date()));
            act.setOutData("end", CommonUtils.getDate());

            //zhumingwei add 2013-11-19 出入库明细查询
            String buttonFalg = "";// 隐藏 【返回】按钮
            if (null != request.getParamValue("buttonFalg")) {
                buttonFalg = request.getParamValue("buttonFalg");
            }
            act.setOutData("buttonFalg", buttonFalg);
            //zhumingwei add 2013-11-19 出入库明细查询

            act.setForword(pckMaterialsDetail_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "包装材料出入库汇总报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void stockAnalysisInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);//把日期设置为当月第一天
            c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            act.setOutData("start", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + "1");
            act.setOutData("end", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));
            act.setForword(stockAnalysis_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "包装材料出入库汇总报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei 2014-08-20   BO订单发出数
    public void boOrderIssueInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);//把日期设置为当月第一天
            c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            act.setOutData("start", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + "1");
            act.setOutData("end", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));

            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());

            act.setForword(boOrderIssue_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "BO订单发出数");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryboOrderIssue() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryboOrderIssueList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "BO订单发出数");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expBoOrderIssueExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[14];
            head[0] = "序号";
            head[1] = "订单类型";
//            head[2] = "BO类型";
            head[2] = "产生品种数";
            head[3] = "已处理品种数";
            head[4] = "处理率";
            head[5] = "产生数量";
            head[6] = "处理数量";
            head[7] = "处理率";

            PageResult<Map<String, Object>> ps = dao.queryboOrderIssueList(request, 1, 99999);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
//                        detail[2] = CommonUtils.checkNull(map.get("BO_TYPE"));
                        detail[2] = CommonUtils.checkNull(map.get("BOCNT"));
                        detail[3] = CommonUtils.checkNull(map.get("BOHDCNT"));
                        detail[4] = CommonUtils.checkNull(map.get("BORATE"));
                        detail[5] = CommonUtils.checkNull(map.get("BO_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("CL_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("BOQTYRATE"));
                        list1.add(detail);
                    }
                }
                String name = "BO订单发出数.xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, name);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    //zhumingwei 2014-08-20   订单发出及时率
    public void orderIssuedTimelyInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);//把日期设置为当月第一天
            c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            act.setOutData("start", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + "1");
            act.setOutData("end", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));

            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());

            act.setForword(orderIssuedTimely_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "BO订单发出数");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryOrderIssuedTimely() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryOrderIssuedTimelyList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单发出及时率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expOrderIssuedTimelyExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String flag = CommonUtils.checkNull(request.getParamValues("Query"));
            String[] head = new String[14];
            if ("1".equals(flag)) {
                head[0] = "序号";
                head[1] = "订单类型";
                head[2] = "订单数量";
                head[3] = "及时发出数量";
                head[4] = "及时率";
            } else {
                head[0] = "序号";
                head[1] = "服务商代码";
                head[2] = "服务商名称";
                head[3] = "订单类型";
                head[4] = "订单号";
                head[5] = "流水号";
                head[6] = "审核日期";
                head[7] = "发运日期";
                head[8] = "及时与否";
            }
            PageResult<Map<String, Object>> ps = dao.queryOrderIssuedTimelyList(request, 1, Constant.PAGE_SIZE_MAX);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        if ("1".equals(flag)) {
                            detail[0] = CommonUtils.checkNull(i + 1);
                            detail[1] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                            detail[2] = CommonUtils.checkNull(map.get("SOCNT"));
                            detail[3] = CommonUtils.checkNull(map.get("BETIMESCNT"));
                            detail[4] = CommonUtils.checkNull(map.get("SMRA"));
                        } else {
                            detail[0] = CommonUtils.checkNull(i + 1);
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                            detail[4] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                            detail[5] = CommonUtils.checkNull(map.get("SO_CODE"));
                            detail[6] = CommonUtils.checkNull(map.get("SO_DATE"));
                            detail[7] = CommonUtils.checkNull(map.get("TRNAS_DATE"));
                            detail[8] = CommonUtils.checkNull(map.get("TIMELY_DESC"));
                        }
                        list1.add(detail);
                    }
                }
                String name = "订单发出及时率.xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, name);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void dealerStorageTimelyInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);//把日期设置为当月第一天
            c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            act.setOutData("start", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + "1");
            act.setOutData("end", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));

            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());

            act.setForword(dealerStorageTimely_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "BO订单发出数");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei 2014-08-21   服务商入库及时率
    public void queryDealerStorageTimely() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryDealerStorageTimelyList(request, curPage, Constant.PAGE_SIZE,logonUser);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商入库及时率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expDealerStorageTimelyExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String flag = CommonUtils.checkNull(request.getParamValue("Query"));
            String[] head = new String[20];
            if ("1".equals(flag)) {
                head[0] = "序号";
                head[1] = "服务商编码";
                head[2] = "服务商名称";
                head[3] = "发出订单数量";
                head[4] = "及时入库订单数量";
                head[5] = "及时率";
            } else {
                head[0] = "序号";
                head[1] = "服务商编码";
                head[2] = "服务商名称";
                head[3] = "订单号";
                head[4] = "流水号";
                head[5] = "提交时间";
                head[6] = "发运时间(出库)";
                head[7] = "承运物流";
                head[8] = "发运方式";
                head[9] = "约定天数";
                head[10] = "到货时间";
                head[11] = "实际到货时间";
                head[12] = "验收时间";
                head[13] = "要求验收时间";
                head[14] = "及时与否";
            }

            PageResult<Map<String, Object>> ps = dao.queryDealerStorageTimelyList(request, 1, 99999,logonUser);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[20];
                        if ("1".equals(flag)) {
                            detail[0] = CommonUtils.checkNull(i + 1);
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("TRCNT"));
                            detail[4] = CommonUtils.checkNull(map.get("DLRCNT"));
                            detail[5] = CommonUtils.checkNull(map.get("WLRATE"));
                        } else {
                            detail[0] = CommonUtils.checkNull(i + 1);
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                            detail[4] = CommonUtils.checkNull(map.get("SO_CODE"));
                            detail[5] = CommonUtils.checkNull(map.get("SO_DATE"));
                            detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                            detail[7] = CommonUtils.checkNull(map.get("TRANSPORT_ORG"));
                            detail[8] = CommonUtils.checkNull(map.get("TRANS_TYPE"));
                            detail[9] = CommonUtils.checkNull(map.get("ARR_DATE"));
                            detail[10] = CommonUtils.checkNull(map.get("ARR_DATE2"));
                            detail[11] = CommonUtils.checkNull(map.get("ARRIVE_DAYS"));
                            detail[12] = CommonUtils.checkNull(map.get("IN_DATE"));
                            detail[13] = CommonUtils.checkNull(map.get("XX2"));
                            if (CommonUtils.checkNull(map.get("DLR")).equals("1")) {
                                detail[14] = "及时";
                            } else {
                                detail[14] = "不及时";
                            }
                        }

                        list1.add(detail);
                    }
                }
                String name = "服务商入库及时率.xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, name);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void orderMeetInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);//把日期设置为当月第一天
            c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            act.setOutData("start", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + "1");
            act.setOutData("end", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));

            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());

            act.setForword(orderMeet_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单满足率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryOrderMeet() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryOrderMeetList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单满足率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expOrderMeetExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String flag = CommonUtils.checkNull(request.getParamValue("Query"));//查询标志

            String[] head = new String[14];
            if ("1".equals(flag)) {
                head[0] = "序号";
                head[1] = "订单类型";
                head[2] = "订货品种";
                head[3] = "发运品种";
                head[4] = "品种满足率";
                head[5] = "订货数量";
                head[6] = "发运数量";
                head[7] = "数量满足率";
            } else {
                head[0] = "序号";
                head[1] = "服务商编码";
                head[2] = "服务商名称";
                head[3] = "订单类型";
                head[4] = "订单号";
                head[5] = "流水号";
                head[6] = "配件编码";
                head[7] = "配件名称";
                head[8] = "订货数量";
                head[9] = "发运数量";
            }
            PageResult<Map<String, Object>> ps = dao.queryOrderMeetList(request, 1, 99999);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        if ("1".equals(flag)) {
                            detail[0] = CommonUtils.checkNull(i + 1);
                            detail[1] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                            detail[2] = CommonUtils.checkNull(map.get("DH_PARTCNT"));
                            detail[3] = CommonUtils.checkNull(map.get("FY_PARTCNT"));
                            detail[4] = CommonUtils.checkNull(map.get("PARTRATE"));
                            detail[5] = CommonUtils.checkNull(map.get("DH_QTYSUM"));
                            detail[6] = CommonUtils.checkNull(map.get("FY_QTYSUM"));
                            detail[7] = CommonUtils.checkNull(map.get("QTYRATE"));
                        } else {
                            detail[0] = CommonUtils.checkNull(i + 1);
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("ORDER_TYPE1"));
                            detail[4] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                            detail[5] = CommonUtils.checkNull(map.get("SO_CODE"));
                            detail[6] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                            detail[7] = CommonUtils.checkNull(map.get("PART_CNAME"));
                            detail[8] = CommonUtils.checkNull(map.get("BUY_QTY"));
                            detail[9] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
                        }
                        list1.add(detail);
                    }
                }
                String name = "订单满足率.xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, name);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    //zhumingwei 2014-08-21   服务站订货情况
    public void dealerOrderSituationInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);//把日期设置为当月第一天
            c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            act.setOutData("start", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + "1");
            act.setOutData("end", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getMonthLastDay());

            act.setForword(dealerOrderSituation_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务站订货情况");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei 2014-08-22   服务站订货情况
    public void queryDealerOrderSituation() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            Map<String, Object> sumMap = dao.queryDealerOrderSituationList(request, 1, 1, "2").getRecords().get(0);
            PageResult<Map<String, Object>> ps = dao.queryDealerOrderSituationList(request, curPage, Constant.PAGE_SIZE, "1");
            //分页方法 end
            act.setOutData("PARTCNT", sumMap.get("PARTCNT") == null ? 0 : sumMap.get("PARTCNT"));
            act.setOutData("PARTQTY", sumMap.get("PARTQTY") == null ? 0 : sumMap.get("PARTQTY"));
            act.setOutData("ORDER_AMOUNT", sumMap.get("ORDER_AMOUNT") == null ? 0 : sumMap.get("ORDER_AMOUNT"));
            act.setOutData("BO_AMOUNT", sumMap.get("BO_AMOUNT") == null ? 0 : sumMap.get("BO_AMOUNT"));
            act.setOutData("RETURN_AMOUNT", sumMap.get("RETURN_AMOUNT") == null ? 0 : sumMap.get("RETURN_AMOUNT"));
            act.setOutData("AMOUNT", sumMap.get("AMOUNT") == null ? 0 : sumMap.get("AMOUNT"));
            act.setOutData("QH_AMOUNT", sumMap.get("QH_AMOUNT") == null ? 0 : sumMap.get("QH_AMOUNT"));
            act.setOutData("TS_AMOUNT", sumMap.get("TS_AMOUNT") == null ? 0 : sumMap.get("TS_AMOUNT"));

            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务站订货情况");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expDealerOrderSituationExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[14];
            head[0] = "序号";
            head[1] = "服务商编码";
            head[2] = "服务商名称";
            head[3] = "出库品种";
            head[4] = "出库数量";
            head[5] = "订货金额";
            head[6] = "缺件金额";
            head[7] = "退货金额";
            head[9] = "切换件金额";
            head[10] = "特殊定件金额";
            head[8] = "实际销售金额";

            PageResult<Map<String, Object>> ps = dao.queryDealerOrderSituationList(request, 1, 99999, "1");
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PARTCNT"));
                        detail[4] = CommonUtils.checkNull(map.get("PARTQTY"));
                        detail[5] = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
                        detail[6] = CommonUtils.checkNull(map.get("BO_AMOUNT"));
                        detail[7] = CommonUtils.checkNull(map.get("RETURN_AMOUNT"));
                        detail[8] = CommonUtils.checkNull(map.get("AMOUNT"));
                        detail[9] = CommonUtils.checkNull(map.get("QH_AMOUNT"));
                        detail[10] = CommonUtils.checkNull(map.get("TS_AMOUNT"));

                        list1.add(detail);
                    }
                }
                String name = "销售明细.xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, name);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei 2013-11-15 包装材料出入库明细报表
    public void queryPckMaterialsDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPckMaterialsDetailList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "包装材料出入库明细报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei 2013-11-15 库存分析报表(本部)
    public void queryStockAnalysis() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryStockAnalysisList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存分析报表(本部)");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei 2013-11-15 包装材料出入库汇总报表
    public void queryPckMaterials() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPckMaterialsList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "包装材料出入库汇总报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei 2013-11-15 包装材料出入库明细报表(导出)
    public void expPckMaterialsDetailExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[14];
            head[0] = "序号";
            head[1] = "规格";
            head[2] = "名称";
            head[3] = "出入库类型";
            head[4] = "数量";
            head[5] = "操作人";
            head[6] = "日期";

            List<Map<String, Object>> list = dao.queryPckMaterialsDetail(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PACK_SPEC"));
                        detail[2] = CommonUtils.checkNull(map.get("PACK_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("FLAG"));
                        detail[4] = CommonUtils.checkNull(map.get("QTY"));
                        detail[5] = CommonUtils.checkNull(map.get("NAME"));
                        detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));

                        list1.add(detail);
                    }
                }
                String name = "包装材料出入库明细报表.xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, name);
            } else {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            this.pckMaterialsDetailInit();
        }
    }

    public void expStockAnalysisExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[14];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            head[3] = "配件件号";
            head[4] = "单位";
            head[5] = "货位";
            head[6] = "账面库存";
            head[7] = "库龄(天)";
            head[8] = "计划价";
            head[9] = "库存金额";
            head[10] = "适用车系";
            head[11] = "适用车型";

            PageResult<Map<String, Object>> ps = dao.queryStockAnalysisList(request, 1, Constant.PAGE_SIZE_MAX);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[5] = CommonUtils.checkNull(map.get("LOC_CODE"));
                        detail[6] = CommonUtils.checkNull(map.get("ITEM_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("AGING"));
                        detail[8] = CommonUtils.checkNull(map.get("STOCK_AMOUNT"));
                        detail[9] = CommonUtils.checkNull(map.get("SALE_PRICE3"));
                        detail[10] = CommonUtils.checkNull(map.get("SERIES_NAME"));
                        detail[11] = CommonUtils.checkNull(map.get("MODEL_NAME"));

                        list1.add(detail);
                    }
                }
                String name = "库存分析报表.xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, name);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(stockAnalysis_URL);
        }
    }

    //zhumingwei 2013-11-15 包装材料出入库汇总报表(导出)
    public void expPckMaterialsExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[14];
            head[0] = "序号";
            head[1] = "规格";
            head[2] = "包装类型";
            head[3] = "名称";
            head[4] = "单位";
            head[5] = "入库数量";
            head[6] = "出库数量";
            head[7] = "账面库存";

            List<Map<String, Object>> list = dao.queryPckMaterials(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PACK_SPEC"));
                        detail[2] = CommonUtils.checkNull(map.get("PACK_TYPE"));
                        detail[3] = CommonUtils.checkNull(map.get("PACK_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("PACK_UOM"));
                        detail[5] = CommonUtils.checkNull(map.get("IN_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("OUT_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("PACK_QTY"));

                        list1.add(detail);
                    }
                }
                String name = "包装材料出入库汇总报表.xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, name);
            } else {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(pckMaterials_URL);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-23
     * @Title :
     * @Description: 入库统计
     */
    public void queryPurOrderIn() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPurOrderInList(request,
                    curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "入库统计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-23
     * @Title :
     * @Description: 导出
     */
    public void expPurOrderInExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[14];
            head[0] = "入库单号";
            head[1] = "验收单号";
            head[2] = "入库退货单号";
            head[3] = "供应商代码";
            head[4] = "供应商名称";
            head[5] = "配件编码";
            head[6] = "配件名称";
            head[7] = "入库数量";
            head[8] = "不含税计划价";
            head[9] = "不含税计划金额";
            head[10] = "车型";
            head[11] = "配件类型";
            head[12] = "备注";
            List<Map<String, Object>> list = dao.queryPurOrderIn(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull(map.get("IN_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("CHECK_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("RETURN_CODE"));
                        detail[3] = CommonUtils
                                .checkNull(map.get("VENDER_CODE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("VENDER_NAME"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[7] = CommonUtils.checkNull(map.get("IN_QTY"));
                        detail[8] = CommonUtils.checkNull(map.get("BUY_PRICE_NOTAX")).replace(",", "");
                        detail[9] = CommonUtils.checkNull(map.get("IN_AMOUNT_NOTAX")).replace(",", "");
                        detail[10] = CommonUtils.checkNull(map.get("MODEL_NAME"));
                        detail[11] = CommonUtils.checkNull(map.get("PART_TYPE"));
                        detail[12] = CommonUtils.checkNull(map.get("REMARK"));
                        list1.add(detail);
                    }
                }
                String name = "入库统计.xls";
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, name);
            } else {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_PURCHASEORDER_IN_QUERY_URL);
        }
    }

    /**
     * @param response
     * @param request
     * @param head
     * @param list
     * @param name
     * @return
     * @throws Exception
     */
    public static Object exportEx(ResponseWrapper response, RequestWrapper request, String[] head, List<String[]> list, String name) throws Exception {

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
     *
     */
    public void partsInvoicingDataInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchaseOrderInDao dao = PurchaseOrderInDao.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List list = dao.getPartWareHouseList(logonUser);// 获取配件库房信息
            act.setOutData("wareHouses", list);
            act.setOutData("old", sdf.format(CommonUtils.getTZDate()));
            act.setForword(partsInvoicingData_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件进销存数据统计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     *
     */
    public void queryPartsInvoicingData() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartsInvoicingDataList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件进销存数据统计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expPartsInvoicingDataExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[11];
            head[0] = "序号";
            head[1] = "配件代码";
            head[2] = "配件名称";
            head[3] = "件号";
            head[4] = "期初数量";
            head[5] = "入库数量";
            head[6] = "出库数量";
            head[7] = "末期数量";
            head[8] = "当前库存数量";
//            head[9] = "差异";

            PageResult<Map<String, Object>> list = dao.queryPartsInvoicingDataList(request, 1, 99999);
            List list1 = new ArrayList();
            if (list != null && list.getRecords().size() != 0) {
                for (int i = 0; i < list.getRecords().size(); i++) {
                    Map map = (Map) list.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[11];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("QC_QTY"));
                        detail[5] = CommonUtils.checkNull(map.get("IN_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("OUT_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("JS_QTY"));
                        detail[8] = CommonUtils.checkNull(map.get("ITEM_QTY"));
//                        detail[9] = CommonUtils.checkNull(map.get("CY"));

                        list1.add(detail);
                    }
                }
                this.exportEx1(ActionContext.getContext().getResponse(), request, head, list1);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }
        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partsInvoicingData_URL);
        }
    }

    /**
     * @param response
     * @param request
     * @param head
     * @param list
     * @return
     * @throws Exception
     */
    public static Object exportEx1(ResponseWrapper response, RequestWrapper request, String[] head, List<String[]> list) throws Exception {
        String name = "配件进销存报表.xls";
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
     *
     */
    public void partsDInInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        //PurchaseOrderInDao dao = PurchaseOrderInDao.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //List list = dao.getPartWareHouseList(logonUser);// 获取配件库房信息
            //act.setOutData("wareHouses", list);
            //act.setOutData("old", sdf.format(CommonUtils.getTZDate()));
            act.setForword(partsDInData_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件进销存数据统计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     *
     */
    public void queryPartsDInData() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartsDInDataList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件进销存数据统计");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     *
     */
    public void expPartsDInDataExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "验收单号";
            head[2] = "配件编码";
            head[3] = "配件名称";
            head[4] = "件号";
            head[5] = "待入库数量";
            head[6] = "计划员";
            head[7] = "库管员";
            head[8] = "供应商";

            List<Map<String, Object>> list = dao.queryPartsDInData(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[11];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("CHK_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[4] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[5] = CommonUtils.checkNull(map.get("SPAREIN_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("PLANER"));
                        detail[7] = CommonUtils.checkNull(map.get("NAME"));
                        detail[8] = CommonUtils.checkNull(map.get("VENDER_NAME"));

                        list1.add(detail);
                    }
                }
                this.exportEx2(ActionContext.getContext().getResponse(), request, head, list1);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }
        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partsDInData_URL);
        }
    }

    public static Object exportEx2(ResponseWrapper response, RequestWrapper request, String[] head, List<String[]> list) throws Exception {
        String name = "待入库明细.xls";
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
     * 有效占用初始化
     */
    public void partsYXZYInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(partsYxzyData_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "有效占用初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 有效占用查询
     */
    public void queryPartsYxzyData() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartsYxzyDataList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "有效占用");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expPartsYxzyDataExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[11];
            head[0] = "序号";
            head[1] = "销售单号";
            head[2] = "经销商编码";
            head[3] = "经销商名称";
            head[4] = "配件编码";
            head[5] = "配件名称";
            head[6] = "件号";
            head[7] = "销售数量";
            head[8] = "创建日期";
            head[9] = "占用类型";

            List<Map<String, Object>> list = dao.queryPartsYxzyData(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[11];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("SO_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[7] = CommonUtils.checkNull(map.get("QTY"));
                        detail[8] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[9] = CommonUtils.checkNull(map.get("BTYPE"));

                        list1.add(detail);
                    }
                }
                this.exportEx3(ActionContext.getContext().getResponse(), request, head, list1);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }
        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partsDInData_URL);
        }
    }

    public static Object exportEx3(ResponseWrapper response, RequestWrapper request, String[] head, List<String[]> list) throws Exception {
        String name = "有效占用.xls";
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
     * 服务站订货明细
     */
    public void dlrOrderDtlInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("start", CommonUtils.getMonthFirstDay());//
            act.setOutData("end", CommonUtils.getDate());//
            act.setForword(partDlrOrderDtlQuery_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商订货明细查询失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 服务商订货明细查询
     */
    public void dlrOrderDtlQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps2 = dao.queryDlrOrderSumList(request, 1, 1,logonUser);
            PageResult<Map<String, Object>> ps = dao.queryDlrOrderDtlList(request, curPage, Constant.PAGE_SIZE,logonUser);
            Map<String, Object> map = ps2.getRecords().get(0);
            int buyQty = 0;
            double buyAmount = 0d;
            int outQty = 0;
            double outAmount = 0d;

            buyQty = Integer.valueOf(map.get("BUY_QTY") == null ? "0" : map.get("BUY_QTY").toString());
            buyAmount = Double.valueOf(map.get("ORDER_AMOUNT") == null ? "0" : map.get("ORDER_AMOUNT").toString());
            outQty = Integer.valueOf(map.get("OUTSTOCK_QTY") == null ? "0" : map.get("OUTSTOCK_QTY").toString());
            outAmount = Double.valueOf(map.get("SALES_AMOUNT") == null ? "0" : map.get("SALES_AMOUNT").toString());

            //分页方法 end
            act.setOutData("buyQty", buyQty);
            act.setOutData("buyAmount", buyAmount);
            act.setOutData("outQty", outQty);
            act.setOutData("outAmount", outAmount);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商订货明细查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expdlrOrderDtl() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String name = "服务商订货明细.xls";
            String[] head = new String[15];

            head[0] = "序号";
            head[1] = "服务商编码";
            head[2] = "服务商名称";
            head[3] = "订货日期";
            head[4] = "订单号";
            head[5] = "流水号";
            head[6] = "配件编码";
            head[7] = "配件名称";
            head[8] = "订货数量";
            head[9] = "发货数量";
            head[10] = "销售单价";
            head[11] = "订货金额";
            head[12] = "发货金额";

            List<Map<String, Object>> list = dao.queryDlrOrderDtlList(request, 1, Constant.PAGE_SIZE_MAX,logonUser).getRecords();

            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                        detail[4] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[5] = CommonUtils.checkNull(map.get("SO_CODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[8] = CommonUtils.checkNull(map.get("BUY_QTY"));
                        detail[9] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
                        detail[10] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                        detail[11] = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
                        detail[12] = CommonUtils.checkNull(map.get("SALES_AMOUNT"));

                        list1.add(detail);
                    }
                }
                ExcelUtil.toExceUtil(ActionContext.getContext().getResponse(), request, head, list1, name);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "服务商订货明细!");
                throw e1;
            }
        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}