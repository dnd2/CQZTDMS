package com.infodms.dms.actions.report.partReport.partSalesReport;

import com.infodms.dms.actions.parts.salesManager.carFactorySalesManager.PartDlrOrderCheck;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.partSalesReport.PartSaleReturnGReportDao;
import com.infodms.dms.dao.report.partSalesReport.mothOutStockAmtDlrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
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
 * @ClassName : PartSaleReturnGReport
 * @Description : 供应中心报表>销售退货报表(供应中心)
 */
public class PartSaleReturnGReport extends BaseImport {
    public Logger logger = Logger.getLogger(PartDlrOrderCheck.class);
    String init = "/jsp/report/partSalesReport/partSaleReturnGRep.jsp";

    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            int flag = 0;//默认是车厂,0表示车厂,1表示供应中心
            if (logonUser.getDealerId() != null) {
                flag = 1;
                TmDealerPO po = new TmDealerPO();
                po.setDealerId(Long.parseLong(logonUser.getDealerId()));
                po = (TmDealerPO) mothOutStockAmtDlrDao.getInstance().select(po).get(0);
                act.setOutData("venderId", po.getDealerId());
                act.setOutData("venderCode", po.getDealerCode());
                act.setOutData("venderName", po.getDealerName());
            }
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("flag", flag);
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
        PartSaleReturnGReportDao dao = PartSaleReturnGReportDao.getInstance();
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryGRep(request, curPage, Constant.PAGE_SIZE_MAX, loginUser);

            double allSalesAmount = 0;
            List<Map<String, Object>> list = ps.getRecords();

            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    if (map != null && map.size() != 0) {

                        double salAmount = ((BigDecimal) map.get("BUY_AMOUNT")).doubleValue();

                        allSalesAmount = Arith.add(allSalesAmount, salAmount);
                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");

                act.setOutData("allSalesAmount", df.format(allSalesAmount));
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
            PartSaleReturnGReportDao dao = PartSaleReturnGReportDao.getInstance();
            String[] head = new String[19];
            head[0] = "退货单号";
            head[1] = "服务商代码";
            head[2] = "服务商名称";
            head[3] = "供应中心代码";
            head[4] = "供应中心名称";
            head[5] = "配件编码";
            head[6] = "配件名称";
            head[7] = "配件件号";
            head[8] = "单位";
            head[9] = "配件类型";
            head[10] = "退货数量";
            head[11] = "单价";
            head[12] = "退货金额";
            head[13] = "原因";
            head[14] = "退货日期";
            List<Map<String, Object>> list = dao.queryGRep(request, 1, Constant.PAGE_SIZE_MAX, logonUser).getRecords();
            List list1 = new ArrayList();
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[25];
                        detail[0] = CommonUtils.checkNull(map.get("RETURN_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("SELLER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[9] = CommonUtils.checkNull(map.get("PART_TYPE"));
                        detail[8] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[10] = CommonUtils.checkNull(map.get("RETURN_QTY"));
                        detail[11] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                        detail[12] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
                        detail[13] = CommonUtils.checkNull(map.get("REMARK"));
                        detail[14] = CommonUtils.checkNull(map.get("CREATE_DATE"));
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

        String name = "销售退货报表(供应中心).xls";
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
                    ws.addCell(new Label(i, z, str[i]));
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
