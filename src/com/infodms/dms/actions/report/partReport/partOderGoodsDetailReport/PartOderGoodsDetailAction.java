package com.infodms.dms.actions.report.partReport.partOderGoodsDetailReport;

import com.infodms.dms.actions.report.partReport.partBuySaleStoreReport.PartBuySaleStoreAction;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.salesManager.PartOderGoodsDetailDao;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class PartOderGoodsDetailAction extends BaseImport {

    public Logger logger = Logger.getLogger(PartBuySaleStoreAction.class);
    ActionContext act = ActionContext.getContext();
    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    public static final String INIT_URL = "/jsp/parts/salesManager/carFactorySalesManager/partOderGoodsDetailQuery.jsp";
    private PartOderGoodsDetailDao dao = PartOderGoodsDetailDao.getInstance();

    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);//把日期设置为当月第一天
            c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            act.setOutData("start", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + "1");
            act.setOutData("end", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));

            act.setForword(INIT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "服务站订货明细页面初始化失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 销售明细报表
     */
    public void query() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> sumps = dao.query(request, curPage, Constant.PAGE_SIZE, "2");
            PageResult<Map<String, Object>> ps = dao.query(request, curPage, Constant.PAGE_SIZE, "1");
            act.setOutData("outQty", sumps.getRecords().get(0).get("OUTQTY") == null ? 0 : sumps.getRecords().get(0).get("OUTQTY"));
            act.setOutData("outAmount", sumps.getRecords().get(0).get("OUTAMOUNT") == null ? 0 : sumps.getRecords().get(0).get("OUTAMOUNT"));
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售明细页面查询失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 销售明细报表导出
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[11];
            head[0] = "序号";
            head[1] = "出库日期";
            head[2] = "服务站号";
            head[3] = "服务站名称";
            head[4] = "订单号";
            head[5] = "流水号";
            head[6] = "配件编码";
            head[7] = "配件名称";
            head[8] = "出库数量";
            head[9] = "销售单价";
            head[10] = "销售金额";

            PageResult<Map<String, Object>> ps = dao.query(request, 1, Constant.PAGE_SIZE_MAX, "1");
            List list1 = new ArrayList();
            for (int i = 0; i < ps.getRecords().size(); i++) {
                Map map = (Map) ps.getRecords().get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[11];
                    detail[0] = CommonUtils.checkNull((i + 1));
                    detail[1] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[5] = CommonUtils.checkNull(map.get("SO_CODE"));
                    detail[6] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                    detail[7] = CommonUtils.checkNull(map.get("PART_CNAME"));
                    detail[8] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
                    detail[9] = CommonUtils.checkNull(map.get("SALE_PRICE"));
                    detail[10] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
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

        String name = "销售明细.xls";
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
