package com.infodms.dms.actions.report.partReport.partPlanReport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.dao.report.partPlansReport.partCtrlHisReportDao;
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
import java.util.List;
import java.util.Map;

public class partCtrlHisReport implements PTConstants {

    public Logger logger = Logger.getLogger(partCtrlHisReport.class);
    private partCtrlHisReportDao dao = partCtrlHisReportDao.getInstance();
    private PurchaseOrderInDao dao1 = PurchaseOrderInDao.getInstance();

    private static String PART_PLANE_CTRL_RPT_URL = "/jsp/report/partPlanRecvReport/partPlanCtrlRpt.jsp";

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-22
     * @Title :
     * @Description: 调拨历史查询初始化
     */
    public void ctrlHisReportInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            List list = dao1.getPartWareHouseList(logonUser);// 获取配件库房信息
            act.setOutData("wareHouses", list);
            act.setOutData("old", CommonUtils.getPreviousXMonthFirst(-3));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_PLANE_CTRL_RPT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨历史查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-22
     * @Title :
     * @Description: 调拨历史查询
     */
    public void queryCtrlHisInfos() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryCtrlHisInfos(request,
                    Constant.PAGE_SIZE, curPage);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨历史查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-22
     * @Title :
     * @Description: 导出
     */
    public void expPartCtrlHisExcel() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            String[] head = null;
            List<Map<String, Object>> list;
            head = new String[20];
            head[0] = "序号";
            head[1] = "领件单号";
            head[2] = "验收单号";
            head[3] = "采购订单号";
            head[4] = "配件编码";
            head[5] = "配件名称";
            head[6] = "配件件号";
            head[7] = "需求数量";
            head[8] = "领件数量";
            head[9] = "验收数量";
            head[10] = "计划员";
            head[11] = "保管员";
            head[12] = "室";
            head[13] = "当前库存";
            head[14] = "打印日期";
            head[15] = "验收日期";
            head[16] = "库房";
            head[17] = "供应商名称";
            head[18] = "制单状态";

            PageResult<Map<String, Object>> ps = dao.queryCtrlHisInfos(request,
                    Constant.PAGE_SIZE_MAX, curPage);
            list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = null;
                        detail = new String[20];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("CHK_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[7] = CommonUtils.checkNull(map.get("BUY_QTY"));
                        detail[8] = CommonUtils.checkNull(map.get("CHECK_QTY"));
                        detail[9] = CommonUtils.checkNull(map.get("CHECK_QTY_IN"));
                        detail[10] = CommonUtils.checkNull(map.get("BUYER"));
                        detail[11] = CommonUtils.checkNull(map.get("WHMAN"));
                        detail[12] = CommonUtils.checkNull(map.get("ROOM"));
                        detail[13] = CommonUtils.checkNull(map.get("ITEM_QTY"));
                        detail[14] = CommonUtils.checkNull(map.get("PRINT_DATE"));
                        detail[15] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[16] = CommonUtils.checkNull(map.get("WH_NAME"));
                        detail[17] = CommonUtils.checkNull(map.get("VENDER_NAME"));

                        String stateVal = CommonUtils.checkNull(map.get("STATE"));

                        if ((Constant.PURCHASE_ORDER_STATE_02 + "").equals(stateVal)) {
                            detail[18] = "已完成";
                        } else if ((Constant.PURCHASE_ORDER_STATE_03 + "").equals(stateVal)) {
                            detail[18] = "已关闭";
                        } else {
                            detail[18] = "未完成";
                        }

                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, 1);
            } else {
                List wareHouses = dao1.getPartWareHouseList(logonUser);// 获取配件库房信息
                act.setOutData("wareHouses", wareHouses);
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
            act.setForword(PART_PURORDERBALANCEDTL_QUERY_URL);
        }

    }

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list, int flag)
            throws Exception {

        String name = "调拨历史查询结果.xls";
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
}
