package com.infodms.dms.actions.report.partReport.partSalesReport;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.partSalesReport.PartOrderDetailRepDao;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartOrderDetailRep extends BaseImport {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    private static final String init = "/jsp/report/partSalesReport/partOrderDetailRepMain.jsp";
    private static final String gyzxInit = "/jsp/report/partSalesReport/partOrderDtlGyzx.jsp";

    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(init);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "页面始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void query() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartOrderDetailRepDao dao = PartOrderDetailRepDao.getInstance();
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryRep(request, curPage, Constant.PAGE_SIZE, loginUser);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void queryPartOrderDtl4Gyzx() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartOrderDetailRepDao dao = PartOrderDetailRepDao.getInstance();
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryPartOrderDtl4GyzxList(request, curPage, Constant.PAGE_SIZE, loginUser);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件订货明细及交货率统计");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013年11月14日
     * @Title :
     * @Description: TODO
     */
    public void partOrderDtlGyzxInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartOrderDetailRepDao dao = PartOrderDetailRepDao.getInstance();
        try {
            int flag = 0;//默认是车厂,0表示车厂,1表示供应中心
            if (loginUser.getDealerId() != null) {
                flag = 1;
                TmDealerPO po = new TmDealerPO();
                po.setDealerId(Long.parseLong(loginUser.getDealerId()));
                po = (TmDealerPO) dao.select(po).get(0);
                act.setOutData("venderId", po.getDealerId());
                act.setOutData("venderCode", po.getDealerCode());
                act.setOutData("venderName", po.getDealerName());

            }
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("flag", flag);
            act.setForword(gyzxInit);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件订货明细及交货率统计");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartOrderDetailRepDao dao = PartOrderDetailRepDao.getInstance();
            String[] head = new String[25];
           /* head[7] = "是否生成销售单";*/
            head[0] = "配件代码";
            head[1] = "配件名称";
            head[2] = "件号";
            head[3] = "配件类型";
            head[4] = "当前可用库存";
            head[5] = "单位";
            head[6] = "订货单价";
            head[7] = "订货数量";
            head[8] = "订货金额";
            head[9] = "已交货数量";
            head[10] = "BO数量";
            head[11] = "BO金额";
            head[12] = "BO项数";
            head[13] = "BO满足数量";
          /*  head[22] = "销售总项数";
            head[23] = "BO总项数";
            head[24] = "BO率";*/
            head[14] = "默认供货商";
            head[15] = "订货时间";
            head[16] = "订货单位代码";
            head[17] = "订货单位名称";
            head[18] = "订货单位地区代码";
            head[19] = "订货单位地区名称";
            head[20] = "销售单号";
            List<Map<String, Object>> list = dao.queryExp(request, logonUser);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[25];

                   /* detail[7] = CommonUtils.checkNull(map.get("SO_FLAG"));*/


                    detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                    detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
                    detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                    detail[3] = CommonUtils.checkNull(map.get("PART_TYPE"));
                    detail[4] = CommonUtils.checkNull(map.get("STOCK"));
                    detail[5] = CommonUtils.checkNull(map.get("UNIT"));
                    detail[6] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                    detail[7] = CommonUtils.checkNull(map.get("BUY_QTY"));
                    detail[8] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
                    detail[9] = CommonUtils.checkNull(map.get("SALES_QTY"));
                    detail[10] = CommonUtils.checkNull(map.get("BO_QTY"));
                    detail[11] = CommonUtils.checkNull(map.get("BO_AMOUNT"));
                    detail[12] = CommonUtils.checkNull(map.get("BOXS"));
                    detail[13] = CommonUtils.checkNull(map.get("TOSAL_QTY"));
                    /*detail[22] = CommonUtils.checkNull(map.get("SALE_TERM_QTY"));
                    detail[23] = CommonUtils.checkNull(map.get("BO_TOTAL_TERM_QTY"));
                    detail[24] = CommonUtils.checkNull(map.get("BO_PERCENT"));*/
                    detail[14] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                    detail[15] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                    detail[16] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[17] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[18] = CommonUtils.checkNull(map.get("PROVINCE_ID"));
                    detail[19] = CommonUtils.checkNull(map.get("REGION_NAME"));
                    detail[20] = CommonUtils.checkNull(map.get("SO_CODE"));
                    list1.add(detail);
                }
            }
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public void expPartOrderDtl4GyzxExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartOrderDetailRepDao dao = PartOrderDetailRepDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[23];
            head[0] = "配件编码";
            head[1] = "配件名称";
            head[2] = "配件件号";
            head[3] = "配件类型";
            head[4] = "单位";
            head[5] = "当前可用库存";
            head[6] = "订货数量";
            head[7] = "订货单价";
            head[8] = "订货金额";
            head[9] = "已交货数量";
            head[10] = "BO数量";
            head[11] = "BO项数";
            head[12] = "BO金额";
            head[13] = "BO满足数量";
            head[14] = "订单号";
            head[15] = "服务商编码";
            head[16] = "服务商名称";
            head[17] = "订货日期";
            head[18] = "供应中心编码";
            head[19] = "供应中心名称";
            head[20] = "省份";
            head[21] = "销售单号";
            List<Map<String, Object>> list = dao.queryPartOrderDtl4Gyzx(request, logonUser);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[23];

                        detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                        int partType = ((BigDecimal) map.get("PART_TYPE")).intValue();
                        if (partType == Constant.PART_BASE_PART_TYPES_SELF_MADE.intValue()) {
                            detail[3] = "自制件";
                        } else if (partType == Constant.PART_BASE_PART_TYPES_PURCHASE.intValue()) {
                            detail[3] = "国产件";
                        } else if (partType == Constant.PART_BASE_PART_TYPES_ENTRANCE.intValue()) {
                            detail[3] = "进口件";
                        } else {
                            detail[3] = "";
                        }
                        detail[4] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[5] = CommonUtils.checkNull(map.get("STOCK"));
                        detail[6] = CommonUtils.checkNull(map.get("BUY_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                        detail[8] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
                        detail[9] = CommonUtils.checkNull(map.get("SALES_QTY"));
                        detail[10] = CommonUtils.checkNull(map.get("BO_QTY"));
                        detail[11] = CommonUtils.checkNull(map.get("BOXS"));
                        detail[12] = CommonUtils.checkNull(map.get("BO_AMOUNT"));
                        detail[13] = CommonUtils.checkNull(map.get("TOSAL_QTY"));
                        detail[14] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[15] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[16] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[17] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                        detail[18] = CommonUtils.checkNull(map.get("SELLER_CODE"));
                        detail[19] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                        detail[20] = CommonUtils.checkNull(map.get("REGION_NAME"));
                        detail[21] = CommonUtils.checkNull(map.get("SO_CODE"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
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
            int flag = 0;//默认是车厂,0表示车厂,1表示供应中心
            if (logonUser.getDealerId() != null) {
                flag = 1;
                TmDealerPO po = new TmDealerPO();
                po.setDealerId(Long.parseLong(logonUser.getDealerId()));
                po = (TmDealerPO) dao.select(po).get(0);
                act.setOutData("venderId", po.getDealerId());
                act.setOutData("venderCode", po.getDealerCode());
                act.setOutData("venderName", po.getDealerName());

            }
            act.setOutData("flag", flag);
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(gyzxInit);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件订货明细及交货率统计.xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
            out = response.getOutputStream();
            wwb = Workbook.createWorkbook(out);
            jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

            if (head != null && head.length > 0) {
                for (int i = 0; i < head.length; i++) {
                    ws.addCell(new Label(i, 0, head[i]));
                }
            }

            for (int z = 1; z < list.size() + 1; z++) {
                String[] str = list.get(z - 1);
                for (int i = 0; i < str.length; i++) {
                    //ws.addCell(new Label(i, z, str[i]));处理数字导出为数值类型 modify date 20131108
                    if (CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i].replace(",", ""))) {
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i].replace(",", ""))));
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

}
