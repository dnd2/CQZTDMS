package com.infodms.dms.actions.report.partReport.partRebateReport;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.report.RebateReportDao;
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

public class RebateReport implements PTConstants {
    public Logger logger = Logger.getLogger(RebateReport.class);

    private static final String INIT_URL = "/jsp/report/rebateReportMain.jsp";

    //查询初始化
    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Calendar c = Calendar.getInstance();
            act.setOutData("year", c.get(Calendar.YEAR) + "");
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getMonthLastDay());
            act.setForword(INIT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "返利报表查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    //查询页面
    public void query() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            RebateReportDao dao = RebateReportDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            String query = CommonUtils.checkNull(request.getParamValue("Query")); //明细和汇总查询标记
            PageResult<Map<String, Object>> sumPs = dao.query(request, 1, 1, "2", loginUser);
            PageResult<Map<String, Object>> ps = ps = dao.query(request, curPage, Constant.PAGE_SIZE, "1", loginUser);
            if (sumPs.getRecords().size() > 0) {
                Map<String, Object> sumMap = sumPs.getRecords().get(0);
                //汇总
                if ("1".equals(query)) {
                    act.setOutData("PART_AMOUNT", sumMap.get("PART_AMOUNT") == null ? 0 : sumMap.get("PART_AMOUNT"));
                    act.setOutData("RETURN_AMOUNT", sumMap.get("RETURN_AMOUNT") == null ? 0 : sumMap.get("RETURN_AMOUNT"));
                    act.setOutData("QH_AMOUNT", sumMap.get("QH_AMOUNT") == null ? 0 : sumMap.get("QH_AMOUNT"));
                    act.setOutData("TS_AMOUNT", sumMap.get("TS_AMOUNT") == null ? 0 : sumMap.get("TS_AMOUNT"));
                    act.setOutData("SP_AMOUNT", sumMap.get("SP_AMOUNT") == null ? 0 : sumMap.get("SP_AMOUNT"));
                    act.setOutData("CL_AMOUNT", sumMap.get("CL_AMOUNT") == null ? 0 : sumMap.get("CL_AMOUNT"));
                    act.setOutData("CP_AMOUNT", sumMap.get("CP_AMOUNT") == null ? 0 : sumMap.get("CP_AMOUNT"));
                    act.setOutData("CP_RATIO", sumMap.get("CP_RATIO") == null ? 0 : sumMap.get("CP_RATIO"));
                    act.setOutData("FL_AMOUNT", sumMap.get("FL_AMOUNT") == null ? 0 : sumMap.get("FL_AMOUNT"));
                    act.setOutData("TASK_AMOUNT", sumMap.get("TASK_AMOUNT") == null ? 0 : sumMap.get("TASK_AMOUNT"));
                } else {
                    for (int i = 1; i <= 12; i++) {
                        String var = i + "";
                        if (i < 10) {
                            var = "0" + i;
                        }
                        act.setOutData("YM_TK_" + var + "", sumMap.get("YM_TK_" + var + "") == null ? 0 : sumMap.get("YM_TK_" + var + ""));
                        act.setOutData("YM_CP_" + var + "", sumMap.get("YM_CP_" + var + "") == null ? 0 : sumMap.get("YM_CP_" + var + ""));
                        act.setOutData("YM_RT_" + var + "", sumMap.get("YM_RT_" + var + "") == null ? 0 : sumMap.get("YM_RT_" + var + ""));
                    }
                    act.setOutData("TK_SUM", sumMap.get("TK_SUM") == null ? 0 : sumMap.get("TK_SUM"));
                    act.setOutData("CP_SUM", sumMap.get("CP_SUM") == null ? 0 : sumMap.get("CP_SUM"));
                    act.setOutData("SUM_RT", sumMap.get("SUM_RT") == null ? 0 : sumMap.get("SUM_RT"));
                }

            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "返利报表查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    //导出
    public void expExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RebateReportDao dao = RebateReportDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            String query = CommonUtils.checkNull(request.getParamValue("Query")); //明细和汇总查询标记
            String[] head = new String[45];
            if ("1".equals(query)) {
                head[0] = "序号";
                head[1] = "服务商代码";
                head[2] = "服务商名称";
                head[3] = "任务金额";
                head[4] = "上传订单金额";
                head[5] = "退货金额";
                head[6] = "切换件金额";
                head[7] = "完成金额";
                head[8] = "完成率";
                head[9] = "返利金额";
            } else {
                head[0] = "序号";
                head[1] = "服务商代码";
                head[2] = "服务商名称";
                head[3] = "1月任务";
                head[4] = "1月完成金额";
                head[5] = "1月完成率";
                head[6] = "2月任务";
                head[7] = "2月完成金额";
                head[8] = "2月完成率";
                head[9] = "3月任务";
                head[10] = "3月完成金额";
                head[11] = "3月完成率";
                head[12] = "4月任务";
                head[13] = "4月完成金额";
                head[14] = "4月完成率";
                head[15] = "5月任务";
                head[16] = "5月完成金额";
                head[17] = "5月完成率";
                head[18] = "6月任务";
                head[19] = "6月完成金额";
                head[20] = "6月完成率";
                head[21] = "7月任务";
                head[22] = "7月完成金额";
                head[23] = "7月完成率";
                head[24] = "8月任务";
                head[25] = "8月完成金额";
                head[26] = "8月完成率";
                head[27] = "9月任务";
                head[28] = "9月完成金额";
                head[29] = "9月完成率";
                head[30] = "10月任务";
                head[31] = "10月完成金额";
                head[32] = "10月完成率";
                head[33] = "11月任务";
                head[34] = "11月完成金额";
                head[35] = "11月完成率";
                head[36] = "12月任务";
                head[37] = "12月完成金额";
                head[38] = "12月完成率";
                head[39] = "合计任务";
                head[40] = "合计完成金额";
                head[41] = "合计完成率";
            }

            PageResult<Map<String, Object>> ps = dao.query(request, 1, Constant.PAGE_SIZE_MAX, "1", logonUser);
            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[45];
                        if ("1".equals(query)) {
                            detail[0] = CommonUtils.checkNull((i + 1));
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE")).replace(",", "");
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME")).replace(",", "");
                            detail[3] = CommonUtils.checkNull(map.get("TASK_AMOUNT")).replace(",", "");
                            detail[4] = CommonUtils.checkNull(map.get("PART_AMOUNT")).replace(",", "");
                            detail[5] = CommonUtils.checkNull(map.get("RETURN_AMOUNT")).replace(",", "");
                            detail[6] = CommonUtils.checkNull(map.get("QH_AMOUNT")).replace(",", "");
                            detail[7] = CommonUtils.checkNull(map.get("CP_AMOUNT")).replace(",", "");
                            detail[8] = CommonUtils.checkNull(map.get("CP_RATIO"));
                            detail[9] = CommonUtils.checkNull(map.get("FL_AMOUNT")).replace(",", "");
                        } else {
                            detail[0] = CommonUtils.checkNull((i + 1));
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE")).replace(",", "");
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME")).replace(",", "");
                            for (int j = 1; j <= 12; j++) {
                                String mth = j + "";
                                if (j < 10) {
                                    mth = "0" + j;
                                }
                                detail[(j * 3)] = CommonUtils.checkNull(map.get("YM_TK_" + mth)).replace(",", "");
                                detail[(j * 3) + 1] = CommonUtils.checkNull(map.get("YM_CP_" + mth)).replace(",", "");
                                detail[(j * 3) + 2] = CommonUtils.checkNull(map.get("YM_RT_" + mth)).replace(",", "");
                            }

                            detail[39] = CommonUtils.checkNull(map.get("TK_SUM")).replace(",", "");
                            detail[40] = CommonUtils.checkNull(map.get("CP_SUM")).replace(",", "");
                            detail[41] = CommonUtils.checkNull(map.get("SUM_RT")).replace(",", "");
                        }
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, 1, query);
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
            act.setForword(INIT_URL);
        }
    }


    //物流信息表导出
    public Object exportEx(ResponseWrapper response,
                           RequestWrapper request, String[] head, List<String[]> list, int flag, String query)
            throws Exception {
        String name = null;
        if ("1".equals(query)) {
            name = "返利汇总报表.xls";
        } else {
            name = "返利明细报表.xls";
        }
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
