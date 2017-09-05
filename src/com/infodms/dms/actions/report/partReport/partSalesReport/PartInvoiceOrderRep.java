package com.infodms.dms.actions.report.partReport.partSalesReport;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.partSalesReport.PartInvoiceOrderRepDao;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartInvoiceOrderRep extends BaseImport {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    private static final String init = "/jsp/report/partSalesReport/partInviceOrderRepMain.jsp";

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
        PartInvoiceOrderRepDao dao = PartInvoiceOrderRepDao.getInstance();
        try {
            Map<String, Object> map = dao.queryRepSum(request);
            act.setOutData("outline_id", map == null ? 0 : ((BigDecimal) map.get("OUTLINE_ID")).longValue());
            act.setOutData("outstock_qty", map == null ? 0 : ((BigDecimal) map.get("OUTSTOCK_QTY")).longValue());
            act.setOutData("sale_amount", map == null ? 0 : (BigDecimal) map.get("SALE_AMOUNT"));
            act.setOutData("plan_amount", map == null ? 0 : (BigDecimal) map.get("PLAN_AMOUNT"));
            act.setOutData("freight", map == null ? 0 : (BigDecimal) map.get("FREIGHT"));
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryRep(request, curPage, Constant.PAGE_SIZE);
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
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartInvoiceOrderRepDao dao = PartInvoiceOrderRepDao.getInstance();
            String[] head = new String[19];
            head[0] = "服务站代码";
            head[1] = "服务站名称";
            head[2] = "是否出口";
            head[3] = "销售单号";
            head[4] = "销售退货单号";
            head[5] = "配件代码";
            head[6] = "配件名称";
            head[7] = "件号";
            head[8] = "发票号码";
            head[9] = "实际出库数量";
            head[10] = "计划价";
            head[11] = "计划金额";
            head[12] = "销售单价";
            head[13] = "销售金额";
            head[14] = "车型";
            head[15] = "配件类型";
            head[16] = "备注";
            List<Map<String, Object>> list = dao.queryExp(request);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[19];
                    detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[2] = CommonUtils.checkNull(map.get("IS_EXPORT"));
                    detail[3] = CommonUtils.checkNull(map.get("SO_CODE"));
                    detail[4] = CommonUtils.checkNull(map.get("RETURN_CODE"));
                    detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                    detail[6] = CommonUtils.checkNull(map.get("PART_CNAME"));
                    detail[7] = CommonUtils.checkNull(map.get("PART_CODE"));
                    detail[8] = CommonUtils.checkNull(map.get("INVOICE_NO"));
                    detail[9] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
                    detail[10] = CommonUtils.checkNull(map.get("SALE_PRICE3"));
                    detail[11] = CommonUtils.checkNull(map.get("AMOUNT"));
                    detail[12] = CommonUtils.checkNull(map.get("SALE_PRICE"));
                    detail[13] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
                    detail[14] = CommonUtils.checkNull(map.get("MODEL_NAME"));
                    detail[15] = CommonUtils.checkNull(map.get("PART_TYPE"));
                    detail[16] = CommonUtils.checkNull(map.get("REMARK"));
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
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "已开票销售统计.xls";
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
