package com.infodms.dms.actions.report.partReport.partSalesReport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.salesManager.PartBoDao;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartSalesReport implements PTConstants {

    public Logger logger = Logger.getLogger(PartSalesReport.class);
    String PART_SALES_RETURN_QUERY_URL = "/jsp/parts/salesManager/carFactorySalesManager/partSalesReturnQuery.jsp";//销售退货报表(本部)
    String PART_STOCK_ANALYSIS_URL = "/jsp/parts/salesManager/carFactorySalesManager/partStockAnalysis.jsp";//销售退货报表(本部)
    private PartBoDao dao = PartBoDao.getInstance();

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list, String title)
            throws Exception {

        String name = title;
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

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static List getList(List<?> list, String prefix, String type) {
        List list1 = new ArrayList();
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    for (int j = 0; j < map.size(); j++) {
                        if (type.equals("N")) {
                            if (CommonUtils.checkNull(map.get(prefix + (i + j + 1))).equals("")) {
                                list1.add(null);
                            } else {
                                list1.add(Long.valueOf(CommonUtils.checkNull(map.get(prefix + (i + j + 1)))));
                            }
                        } else {
                            list1.add(CommonUtils.checkNull(map.get(prefix + (i + j + 1))));
                        }
                    }
                }
            }
        }
        return list1;
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-9-16
     * @Title :
     * @Description: 查询初始化
     */
    public void partSalesReportInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_SALES_REPORT_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei add 2013-11-27 销售退货报表(本部)
    public void partSalesReturnInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_SALES_RETURN_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货报表(本部)");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei add 2013-11-27 销售退货报表(本部)
    public void queryPartSalesReturnInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSalesReturnList(request, curPage, Constant.PAGE_SIZE_MAX);
            // 分页方法 end
            List<Map<String, Object>> list = ps.getRecords();

            double allSalesAmount = 0;

            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {

                        double salAmount = ((BigDecimal) map.get("BUY_AMOUNT")).doubleValue();

                        allSalesAmount = Arith.add(allSalesAmount, salAmount);
                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");

                act.setOutData("allSalesAmount", df.format(allSalesAmount));
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货报表(本部)");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-9-16
     * @Title :
     * @Description: 查询初始化
     */
    public void partSalesReportGyzxInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
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
            act.setForword(PART_SALES_REPORTGYZX_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-9-17
     * @Title :
     * @Description: 配件销售查询
     */
    public void queryPartSalesInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSalesList(
                    request, curPage, Constant.PAGE_SIZE);
            // 分页方法 end
            List<Map<String, Object>> list = ps.getRecords();
            double allSalesAmount = 0;
            double allSalesCb = 0;
            double allMl = 0;
            String allMll = "";
            long allXsZxs = 0;
            long allBoZxs = 0;
            String allBoRate = "";
            double allBuyAmount = 0;
            double allInAmount = 0;
            double allOutAmount = 0;
            double allStockAmount = 0;
            long allbuyQty = 0;
            long allSalQty = 0;
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {

                        double salAmount = ((BigDecimal) map.get("SAL_AMOUNT")).doubleValue();
                        double salCb = ((BigDecimal) map.get("SAL_CB")).doubleValue();
                        double ml = ((BigDecimal) map.get("ML")).doubleValue();
                        Long boXs = ((BigDecimal) map.get("BOZXS")).longValue();
                        Long dhXs = ((BigDecimal) map.get("SALZXS")).longValue();
                        Long buyQty = ((BigDecimal) map.get("BUY_QTY")).longValue();
                        Long salQty = ((BigDecimal) map.get("SALES_QTY")).longValue();
                        double buyAmount = ((BigDecimal) map.get("BUY_AMOUNT")).doubleValue();
                        double inAmount = ((BigDecimal) map.get("IN_AMOUNT")).doubleValue();
                        double outAmount = ((BigDecimal) map.get("OUT_AMOUNT")).doubleValue();
                        double stockAmount = ((BigDecimal) map.get("STOCK_AMOUNT")).doubleValue();

                        allSalesAmount = Arith.add(allSalesAmount, salAmount);
                        allSalesCb = Arith.add(allSalesCb, salCb);
                        allMl = Arith.add(allMl, ml);
                        allXsZxs = allXsZxs + dhXs;
                        allBoZxs = allBoZxs + boXs;
                        allbuyQty = allbuyQty + buyQty;
                        allSalQty = allSalQty + salQty;
                        allBuyAmount = Arith.add(allBuyAmount, buyAmount);
                        allInAmount = Arith.add(allInAmount, inAmount);
                        allOutAmount = Arith.add(allOutAmount, outAmount);
                        allStockAmount = Arith.add(allStockAmount, stockAmount);

                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");

                if (allBoZxs == 0) {
                    allBoRate = "0%";
                } else if (allXsZxs == 0 && allBoZxs > 0) {
                    allBoRate = "100%";
                } else {
                    BigDecimal b1 = new BigDecimal(Double.toString(allBoZxs));
                    BigDecimal b2 = new BigDecimal(Double.toString(allXsZxs));
                    double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                    allBoRate = subZeroAndDot(df.format(s * 100)) + "%";
                }
                if (allMl == 0) {
                    allMll = "0%";
                } else if (allSalesAmount == 0 && allMl > 0) {
                    allMll = "100%";
                } else {
                    BigDecimal b1 = new BigDecimal(Double.toString(allMl));
                    BigDecimal b2 = new BigDecimal(Double.toString(allSalesAmount));
                    double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                    allMll = subZeroAndDot(df.format(s * 100)) + "%";
                }

                act.setOutData("allSalesAmount", df.format(allSalesAmount));
                act.setOutData("allSalesCb", df.format(allSalesCb));
                act.setOutData("allMl", df.format(allMl));
                act.setOutData("allMll", allMll);
                act.setOutData("allXsZxs", allXsZxs);
                act.setOutData("allBoZxs", allBoZxs);
                act.setOutData("allBoRate", allBoRate);
                act.setOutData("allBuyAmount", df.format(allBuyAmount));
                act.setOutData("allInAmount", df.format(allInAmount));
                act.setOutData("allOutAmount", df.format(allOutAmount));
                act.setOutData("allStockAmount", df.format(allStockAmount));
                act.setOutData("allbuyQty", allbuyQty);
                act.setOutData("allSalQty", allSalQty);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-9-17
     * @Title :
     * @Description: 配件销售查询(供应中心)
     */
    public void queryPartSalesGyzx() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSalesGyzxList(
                    request, logonUser, curPage, Constant.PAGE_SIZE);
            // 分页方法 end
            if ("0".equals(flag)) {

                List<Map<String, Object>> list = ps.getRecords();
                double allSalesAmount = 0;
                long allXsZxs = 0;
                long allBoZxs = 0;
                String allBoRate = "";
                double allBuyAmount = 0;
                double allStockAmount = 0;
                long allbuyQty = 0;
                long allSalQty = 0;
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {

                            double salAmount = ((BigDecimal) map.get("SALES_AMOUNT")).doubleValue();
                            Long boXs = ((BigDecimal) map.get("BOZXS")).longValue();
                            Long dhXs = ((BigDecimal) map.get("SALZXS")).longValue();
                            Long buyQty = ((BigDecimal) map.get("BUY_QTY")).longValue();
                            Long salQty = ((BigDecimal) map.get("SALES_QTY")).longValue();
                            double buyAmount = ((BigDecimal) map.get("BUY_AMOUNT")).doubleValue();
                            double stockAmount = ((BigDecimal) map.get("STOCK_AMOUNT")).doubleValue();

                            allSalesAmount = Arith.add(allSalesAmount, salAmount);
                            allXsZxs = allXsZxs + dhXs;
                            allBoZxs = allBoZxs + boXs;
                            allbuyQty = allbuyQty + buyQty;
                            allSalQty = allSalQty + salQty;
                            allBuyAmount = Arith.add(allBuyAmount, buyAmount);
                            allStockAmount = Arith.add(allStockAmount, stockAmount);

                        }
                    }

                    DecimalFormat df = new DecimalFormat("0.00");

                    if (allBoZxs == 0) {
                        allBoRate = "0%";
                    } else if (allXsZxs == 0 && allBoZxs > 0) {
                        allBoRate = "100%";
                    } else {
                        BigDecimal b1 = new BigDecimal(Double.toString(allBoZxs));
                        BigDecimal b2 = new BigDecimal(Double.toString(allXsZxs));
                        double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                        allBoRate = subZeroAndDot(df.format(s * 100)) + "%";
                    }

                    act.setOutData("allSalesAmount", df.format(allSalesAmount));
                    act.setOutData("allXsZxs", allXsZxs);
                    act.setOutData("allBoZxs", allBoZxs);
                    act.setOutData("allBoRate", allBoRate);
                    act.setOutData("allBuyAmount", df.format(allBuyAmount));
                    act.setOutData("allStockAmount", df.format(allStockAmount));
                    act.setOutData("allbuyQty", allbuyQty);
                    act.setOutData("allSalQty", allSalQty);
                }
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-9-17
     * @Title :
     * @Description: TODO
     */
    public void expPartSalesExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[15];
            head[0] = "配件类型";
            head[1] = "销售额(不含税)";
            head[2] = "销售成本(不含税)";
            head[3] = "毛利";
            head[4] = "毛利率";
            head[5] = "销售总项数";
            head[6] = "BO总项数";
            head[7] = "BO率";
            head[8] = "采购金额(不含税)";
            head[9] = "入库金额(计划价)";
            head[10] = "出库金额(计划价)";
            head[11] = "在库金额(计划价)";
            head[12] = "总进货数量";
            head[13] = "总销售数量";
            List<Map<String, Object>> list = dao.queryPartSales(request);
            List list1 = new ArrayList();
            double allSalesAmount = 0;
            double allSalesCb = 0;
            double allMl = 0;
            long allXsZxs = 0;
            long allBoZxs = 0;
            long allbuyQty = 0;
            long allSalQty = 0;
            double allBuyAmount = 0;
            double allInAmount = 0;
            double allOutAmount = 0;
            double allStockAmount = 0;
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(map.get("PART_TYPE"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("SAL_AMOUNT"));
                        double salAmount = ((BigDecimal) map.get("SAL_AMOUNT")).doubleValue();
                        detail[2] = CommonUtils
                                .checkNull(map.get("SAL_CB"));
                        double salCb = ((BigDecimal) map.get("SAL_CB")).doubleValue();
                        detail[3] = CommonUtils.checkNull(map.get("ML"));
                        double ml = ((BigDecimal) map.get("ML")).doubleValue();
                        if (ml == 0) {
                            detail[4] = "0%";
                        } else if (salAmount == 0 && ml > 0) {
                            detail[4] = "100%";
                        } else {
                            BigDecimal b1 = new BigDecimal(Double.toString(ml));
                            BigDecimal b2 = new BigDecimal(Double.toString(salAmount));
                            double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                            DecimalFormat df = new DecimalFormat("#.00");
                            detail[4] = subZeroAndDot(df.format(s * 100)) + "%";
                        }
                        detail[5] = CommonUtils.checkNull(map.get("SALZXS"));
                        detail[6] = CommonUtils.checkNull(map.get("BOZXS"));
                        Long boXs = ((BigDecimal) map.get("BOZXS")).longValue();
                        Long dhXs = ((BigDecimal) map.get("SALZXS")).longValue();
                        Long buyQty = ((BigDecimal) map.get("BUY_QTY")).longValue();
                        Long salQty = ((BigDecimal) map.get("SALES_QTY")).longValue();
                        if (boXs.longValue() == 0) {
                            detail[7] = "0%";
                        } else if (dhXs.longValue() == 0 && boXs.longValue() > 0) {
                            detail[7] = "100%";
                        } else {
                            BigDecimal b1 = new BigDecimal(Double.toString(boXs));
                            BigDecimal b2 = new BigDecimal(Double.toString(dhXs));
                            double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                            DecimalFormat df = new DecimalFormat("#.00");
                            detail[7] = subZeroAndDot(df.format(s * 100)) + "%";
                        }
                        detail[8] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
                        double buyAmount = ((BigDecimal) map.get("BUY_AMOUNT")).doubleValue();
                        detail[9] = CommonUtils.checkNull(map.get("IN_AMOUNT"));
                        double inAmount = ((BigDecimal) map.get("IN_AMOUNT")).doubleValue();
                        detail[10] = CommonUtils.checkNull(map.get("OUT_AMOUNT"));
                        double outAmount = ((BigDecimal) map.get("OUT_AMOUNT")).doubleValue();
                        detail[11] = CommonUtils.checkNull(map.get("STOCK_AMOUNT"));
                        double stockAmount = ((BigDecimal) map.get("STOCK_AMOUNT")).doubleValue();
                        detail[12] = buyQty.toString();
                        detail[13] = salQty.toString();
                        allSalesAmount = Arith.add(allSalesAmount, salAmount);
                        allSalesCb = Arith.add(allSalesCb, salCb);
                        allMl = Arith.add(allMl, ml);
                        allXsZxs = allXsZxs + dhXs;
                        allBoZxs = allBoZxs + boXs;
                        allbuyQty = allbuyQty + buyQty;
                        allSalQty = allSalQty + salQty;
                        allBuyAmount = Arith.add(allBuyAmount, buyAmount);
                        allInAmount = Arith.add(allInAmount, inAmount);
                        allOutAmount = Arith.add(allOutAmount, outAmount);
                        allStockAmount = Arith.add(allStockAmount, stockAmount);

                        list1.add(detail);
                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");

                String[] detail1 = new String[15];
                detail1[0] = "合计：";
                detail1[1] = df.format(allSalesAmount);
                detail1[2] = df.format(allSalesCb);
                detail1[3] = df.format(allMl);
                if (allMl == 0) {
                    detail1[4] = "0%";
                } else if (allSalesAmount == 0 && allMl > 0) {
                    detail1[4] = "100%";
                } else {
                    BigDecimal b1 = new BigDecimal(Double.toString(allMl));
                    BigDecimal b2 = new BigDecimal(Double.toString(allSalesAmount));
                    double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                    detail1[4] = subZeroAndDot(df.format(s * 100)) + "%";
                }
                detail1[5] = allXsZxs + "";
                detail1[6] = allBoZxs + "";
                if (allBoZxs == 0) {
                    detail1[7] = "0%";
                } else if (allXsZxs == 0 && allBoZxs > 0) {
                    detail1[7] = "100%";
                } else {
                    BigDecimal b1 = new BigDecimal(Double.toString(allBoZxs));
                    BigDecimal b2 = new BigDecimal(Double.toString(allXsZxs));
                    double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                    detail1[7] = subZeroAndDot(df.format(s * 100)) + "%";
                }
                detail1[8] = df.format(allBuyAmount);
                detail1[9] = df.format(allInAmount);
                detail1[10] = df.format(allOutAmount);
                detail1[11] = df.format(allStockAmount);
                detail1[12] = allbuyQty + "";
                detail1[13] = allSalQty + "";
                list1.add(detail1);
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, "配件销售月、季度、年报表.xls");
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
            act.setForword(PART_SALES_REPORT_QUERY_URL);
        }
    }

    //zhumingwei add 2013-11-27 销售退货报表(本部)
    public void expPartSalesReturnExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[15];
            head[0] = "退货单号";
            head[1] = "服务商代码";
            head[2] = "服务商名称";
            head[3] = "配件编码";
            head[4] = "配件名称";
            head[5] = "件号";
            head[6] = "单位";
            head[7] = "配件类型";
            head[8] = "退货数量";
            head[9] = "单价";
            head[10] = "退货金额";
            head[11] = "退货原因";
            head[12] = "退货日期";
            List<Map<String, Object>> list = dao.queryPartSalesReturn(request);
            List list1 = new ArrayList();
            double allSalesAmount = 0;
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(map.get("RETURN_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[6] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_TYPE"));
                        detail[8] = CommonUtils.checkNull(map.get("RETURN_QTY"));
                        detail[9] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                        detail[10] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
                        detail[11] = CommonUtils.checkNull(map.get("REMARK"));
                        detail[12] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        allSalesAmount = Arith.add(allSalesAmount, ((BigDecimal) map.get("BUY_AMOUNT")).doubleValue());

                        list1.add(detail);
                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");

                String[] detail1 = new String[15];
                detail1[0] = "合计：";
                detail1[1] = "";
                detail1[2] = "";
                detail1[3] = "";
                detail1[4] = "";
                detail1[5] = "";
                detail1[6] = "";
                detail1[7] = "";
                detail1[8] = "";
                detail1[9] = "";
                detail1[10] = df.format(allSalesAmount);
                detail1[11] = "";
                detail1[12] = "";
                list1.add(detail1);
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, "销售退货报表(本部).xls");
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
            act.setForword(PART_SALES_RETURN_QUERY_URL);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-9-17
     * @Title :
     * @Description: TODO
     */
    public void expPartSalesGyzxExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            String[] head = new String[11];
            head[0] = "供应中心代码";
            head[1] = "供应中心";
            head[2] = "采购金额(不含税)";
            head[3] = "采购数量";
            head[4] = "BO总项数";
            head[5] = "销售总项数";
            head[6] = "BO率";
            head[7] = "销售金额(不含税)";
            head[8] = "销售数量";
            head[9] = "库存金额(服务商价格)";
            List<Map<String, Object>> list = dao.queryPartSalesGyzx(request);
            List list1 = new ArrayList();
            double allSalesAmount = 0;
            long allXsZxs = 0;
            long allBoZxs = 0;
            long allbuyQty = 0;
            long allSalQty = 0;
            double allBuyAmount = 0;
            double allStockAmount = 0;
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[11];
                        detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[2] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
                        double buyAmount = ((BigDecimal) map.get("BUY_AMOUNT")).doubleValue();
                        Long buyQty = ((BigDecimal) map.get("BUY_QTY")).longValue();
                        detail[3] = buyQty.toString();
                        detail[4] = CommonUtils.checkNull(map.get("BOZXS"));
                        detail[5] = CommonUtils.checkNull(map.get("SALZXS"));
                        Long boXs = ((BigDecimal) map.get("BOZXS")).longValue();
                        Long dhXs = ((BigDecimal) map.get("SALZXS")).longValue();

                        if (boXs.longValue() == 0) {
                            detail[6] = "0%";
                        } else if (dhXs.longValue() == 0 && boXs.longValue() > 0) {
                            detail[6] = "100%";
                        } else {
                            BigDecimal b1 = new BigDecimal(Double.toString(boXs));
                            BigDecimal b2 = new BigDecimal(Double.toString(dhXs));
                            double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                            DecimalFormat df = new DecimalFormat("#.00");
                            detail[6] = subZeroAndDot(df.format(s * 100)) + "%";
                        }

                        detail[7] = CommonUtils.checkNull(map.get("SALES_AMOUNT"));
                        double salAmount = ((BigDecimal) map.get("SALES_AMOUNT")).doubleValue();


                        Long salQty = ((BigDecimal) map.get("SALES_QTY")).longValue();
                        detail[8] = salQty.toString();
                        detail[9] = CommonUtils.checkNull(map.get("STOCK_AMOUNT"));
                        double stockAmount = ((BigDecimal) map.get("STOCK_AMOUNT")).doubleValue();
                        allSalesAmount = Arith.add(allSalesAmount, salAmount);
                        allXsZxs = allXsZxs + dhXs;
                        allBoZxs = allBoZxs + boXs;
                        allbuyQty = allbuyQty + buyQty;
                        allSalQty = allSalQty + salQty;
                        allBuyAmount = Arith.add(allBuyAmount, buyAmount);
                        allStockAmount = Arith.add(allStockAmount, stockAmount);

                        list1.add(detail);
                    }
                }

                if ("0".equals(flag)) {
                    DecimalFormat df = new DecimalFormat("0.00");

                    String[] detail1 = new String[11];
                    detail1[0] = "";
                    detail1[1] = "合计：";
                    detail1[2] = df.format(allBuyAmount);
                    detail1[3] = allbuyQty + "";
                    detail1[4] = allBoZxs + "";
                    detail1[5] = allXsZxs + "";
                    if (allBoZxs == 0) {
                        detail1[6] = "0%";
                    } else if (allXsZxs == 0 && allBoZxs > 0) {
                        detail1[6] = "100%";
                    } else {
                        BigDecimal b1 = new BigDecimal(Double.toString(allBoZxs));
                        BigDecimal b2 = new BigDecimal(Double.toString(allXsZxs));
                        double s = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                        detail1[6] = subZeroAndDot(df.format(s * 100)) + "%";
                    }
                    detail1[7] = df.format(allSalesAmount);
                    detail1[8] = allSalQty + "";
                    detail1[9] = df.format(allStockAmount);

                    list1.add(detail1);
                }

                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, "配件销售汇总报表.xls");
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
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_SALES_REPORTGYZX_QUERY_URL);
        }
    }

    /**
     * 配件进销存分析报表入口
     */
    public void partStockAnalysisInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_STOCK_ANALYSIS_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货报表(本部)");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件进销存分析报表
     */
    public void querypartStockAnalysisInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PARTOLDCODE"));//配件编码
            String myyear = CommonUtils.checkNull(request.getParamValue("MYYEAR"));//年份
            String mymonth = CommonUtils.checkNull(request.getParamValue("MYMONTH"));//月份

            List<Map<String, Object>> In = dao.queryStockIn(request);
            List<Map<String, Object>> Out = dao.queryStockOut(request);
            List<Map<String, Object>> Stock = dao.queryStock(request);
            List<Map<String, Object>> Date = dao.queryDate(request);
            if ("".equals(mymonth)) {
                act.setOutData("Date", getList(Date, "M", "S"));
                act.setOutData("In", getList(In, "YI", "N"));
                act.setOutData("Out", getList(Out, "YS", "N"));
                act.setOutData("Stock", getList(Stock, "YT", "N"));
            } else {
                act.setOutData("Date", getList(Date, "D", "S"));
                act.setOutData("In", getList(In, "ID", "N"));
                act.setOutData("Out", getList(Out, "OD", "N"));
                act.setOutData("Stock", getList(Stock, "SD", "N"));
            }
            act.setOutData("PartOldcode", partOldcode);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件进销存分析报表");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}
