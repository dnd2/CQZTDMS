package com.infodms.dms.actions.report.partReport.partSalesReport;

import com.infodms.dms.actions.parts.salesManager.carFactorySalesManager.PartDlrOrderCheck;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.partSalesReport.PartCheckAnalysisRepDao;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : Administrator
 *         CreateDate     : 2013-11-26
 * @ClassName : PartCheckAnalysisReport
 * @Description : 本部报表>考核分析报表(本部)
 */
public class PartCheckAnalysisReport extends BaseImport {
    public Logger logger = Logger.getLogger(PartDlrOrderCheck.class);
    String init = "/jsp/report/partSalesReport/partChkAnalysisRep.jsp";

    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(init);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "页面始化错误,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void query() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartCheckAnalysisRepDao dao = PartCheckAnalysisRepDao.getInstance();
        try {
            String queryFlag = CommonUtils.checkNull(request.getParamValue("searchType"));//查询标识

            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryRep(request, 1, Constant.PAGE_SIZE_MAX, loginUser);

            List<Map<String, Object>> list = null;
            DecimalFormat df = new DecimalFormat("0.00");

            if ("gyzx".equalsIgnoreCase(queryFlag) || "fws".equalsIgnoreCase(queryFlag)) {
                list = ps.getRecords();
                double totalAmount = 0;
                double totalPAmount = 0;
                String totalPRto = "-";
                double totalBA = 0;
                String totalTZRatio = "0";

                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = list.get(i);
                        if (map != null && map.size() != 0) {

                            double amount = ((BigDecimal) map.get("AMOUNT")).doubleValue();
                            double pAmount = ((BigDecimal) map.get("P_AMOUNT")).doubleValue();
                            double bA = ((BigDecimal) map.get("B_A")).doubleValue();

                            totalAmount = Arith.add(totalAmount, amount);
                            totalPAmount = Arith.add(totalPAmount, pAmount);
                            totalBA = Arith.add(totalBA, bA);

                        }
                    }

                    if (totalAmount > 0 && totalPAmount > 0) {
                        totalPRto = df.format((totalPAmount / totalAmount) * 100) + "%";
                    }

                    if (totalBA > 0) {
                        totalTZRatio = df.format(totalPAmount / totalBA);
                    }

                    act.setOutData("totalAmount", df.format(totalAmount));
                    act.setOutData("totalPAmount", df.format(totalPAmount));
                    act.setOutData("totalPRto", totalPRto);
                    act.setOutData("totalBA", df.format(totalBA));
                    act.setOutData("totalTZRatio", totalTZRatio);
                }
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询错误,请联系管理员!");
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
    public void export() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String queryFlag = CommonUtils.checkNull(request.getParamValue("searchType"));//查询标识
            PartCheckAnalysisRepDao dao = PartCheckAnalysisRepDao.getInstance();

            String[] head = new String[13];

            head[0] = "年月";

            if ("prov".equalsIgnoreCase(queryFlag)) {
                head[1] = "销售员";
                head[2] = "大区服务区经理";
                head[3] = "大区";
                head[4] = "省份";
                head[5] = "任务金额";
                head[6] = "完成金额";
                head[7] = "完成率";
                head[8] = "辖区结算金额";
                head[9] = "拓展率";
            } else if ("gyzx".equalsIgnoreCase(queryFlag)) {
                head[1] = "供应中心代码";
                head[2] = "供应中心名称";
                head[3] = "辐射区域";
                head[4] = "任务金额";
                head[5] = "购买金额";
                head[6] = "完成率";
                head[7] = "辐射区域结算金额";
                head[8] = "拓展率";
            } else {
                head[1] = "服务商代码";
                head[2] = "服务商名称";
                head[3] = "任务金额";
                head[4] = "购买金额";
                head[5] = "完成率";
                head[6] = "结算金额";
                head[7] = "拓展率";
            }

            List<Map<String, Object>> list = dao.queryRep(request, 1, Constant.PAGE_SIZE_MAX, logonUser).getRecords();
            List list1 = new ArrayList();
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[25];
                        detail[0] = CommonUtils.checkNull(map.get("MONTH_NO"));
                        if ("prov".equalsIgnoreCase(queryFlag)) {
                            detail[1] = CommonUtils.checkNull(map.get("NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("REG_USER"));
                            detail[3] = CommonUtils.checkNull(map.get("ROOT_ORG_NAME"));
                            detail[4] = CommonUtils.checkNull(map.get("DREGION_NAME"));
                            detail[5] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[6] = CommonUtils.checkNull(map.get("P_AMOUNT"));
                            detail[7] = CommonUtils.checkNull(map.get("P_RTO"));
                            detail[8] = CommonUtils.checkNull(map.get("B_A"));
                            detail[9] = CommonUtils.checkNull(map.get("TZ_RATIO"));
                        } else if ("gyzx".equalsIgnoreCase(queryFlag)) {
                            detail[1] = CommonUtils.checkNull(map.get("PARENTORG_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("PARENTORG_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("REGION_NAME"));
                            detail[4] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[5] = CommonUtils.checkNull(map.get("P_AMOUNT"));
                            detail[6] = CommonUtils.checkNull(map.get("P_RTO"));
                            detail[7] = CommonUtils.checkNull(map.get("B_A"));
                            detail[8] = CommonUtils.checkNull(map.get("TZ_RATIO"));
                        } else {
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[4] = CommonUtils.checkNull(map.get("P_AMOUNT"));
                            detail[5] = CommonUtils.checkNull(map.get("P_RTO"));
                            detail[6] = CommonUtils.checkNull(map.get("B_A"));
                            detail[7] = CommonUtils.checkNull(map.get("TZ_RATIO"));
                        }

                        list1.add(detail);
                    }
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
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "考核分析报表(本部).xls";
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
}
