package com.infodms.dms.actions.report.partReport.partStockReport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseOrderManager.LogTimelyArrivalRateDao;
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

public class LogTimelyArrivalRateReport implements PTConstants {

    public Logger logger = Logger.getLogger(LogTimelyArrivalRateReport.class);
    private LogTimelyArrivalRateDao dao = LogTimelyArrivalRateDao.getInstance();


    String LogTimelyArrivalRate_URL = "/jsp/parts/purchaseOrderManager/purchaseOrderIn/logTimelyArrivalRate.jsp";

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-8-21
     * @Title :
     * @Description: 物流到货及时率页面初始化
     */
    public void init() {
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

            act.setForword(LogTimelyArrivalRate_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "BO订单发出数");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    public void query() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryLogTimelyArrivalRateList(request, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物流到货及时率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    public void expBoOrderIssueExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            String query = CommonUtils.checkNull(request.getParamValue("Query"));
            String[] head = new String[14];
            if ("1".equals(query)) {
                head[0] = "序号";
                head[1] = "承运物流-发运方式";
                head[2] = "发运数量";
                head[3] = "及时到货数量";
                head[4] = "及时率";
            } else {
                head[0] = "序号";
                head[1] = "服务商编码";
                head[2] = "服务商名称";
                head[3] = "订单号";
                head[4] = "流水号";
                head[5] = "提交日期";
                head[6] = "发运日期(出库)";
                head[7] = "承运物流";
                head[8] = "发运方式";
                head[9] = "约定天数";
                head[10] = "实际到货时间";
                head[11] = "要求到货时间";
                head[12] = "及时与否";
            }
            PageResult<Map<String, Object>> ps = dao.queryLogTimelyArrivalRateList(request, 1, 99999);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        if ("1".equals(query)) {
                            detail[0] = CommonUtils.checkNull(i + 1);
                            detail[1] = CommonUtils.checkNull(map.get("TRANSPORT_ORG"));
                            detail[2] = CommonUtils.checkNull(map.get("TRANS_TYPE"));
                            detail[3] = CommonUtils.checkNull(map.get("TRCNT"));
                            detail[4] = CommonUtils.checkNull(map.get("WLCNT"));
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
                            if (CommonUtils.checkNull(map.get("WL")).equals("1")) {
                                detail[12] = "及时";
                            } else {
                                detail[12] = "不及时";
                            }
                        }
                        list1.add(detail);
                    }
                }
                String name = "物流到货及时率.xls";
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
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "物流到货及时率文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-8-21
     * @Title :
     * @Description: 导出
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
                    ///ws.addCell(new Label(i, z, str[i])); //modify by yuan
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