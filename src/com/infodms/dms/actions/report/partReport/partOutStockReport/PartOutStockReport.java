package com.infodms.dms.actions.report.partReport.partOutStockReport;

import com.infodms.dms.actions.parts.salesManager.carFactorySalesManager.PartDlrOrderCheck;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.report.partSalesReport.PartOutStockRepDao;
import com.infodms.dms.exception.BizException;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PartOutStockReport extends BaseImport {
    public Logger logger = Logger.getLogger(PartDlrOrderCheck.class);
    String init = "/jsp/report/partSalesReport/partOutStockRepMain.jsp";
    PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();

    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String dealerId = "";
            String oemFlag = Constant.IF_TYPE_NO + "";
            String dealerName = "";
            String dealerCode = "";
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
                oemFlag = Constant.IF_TYPE_YES + "";
            } else {
                dealerName = beanList.get(0).getOrgName();
                dealerCode = beanList.get(0).getOrgCode();
                act.setOutData("dealerName", dealerName);
                act.setOutData("dealerCode", dealerCode);
            }
            act.setOutData("toDay",CommonUtils.getDate());
            act.setOutData("preDay",CommonUtils.getBefore(new Date()));
            act.setOutData("oemFlag", oemFlag);
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
        PartOutStockRepDao dao =  PartOutStockRepDao.getInstance();
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
            PartOutStockRepDao dao =  PartOutStockRepDao.getInstance();
            String[] head = new String[13];
            head[0] = "出库单号";
            head[1] = "服务商编码";
            head[2] = "服务商名称";
            head[3] = "出库类型";
            head[4] = "配件编码";
            head[5] = "配件名称";
//            head[6] = "配件件号";
            head[6] = "配件类型";
            head[7] = "单位";
            head[8] = "出库数量";
            head[9] = "单价";
            head[10] = "金额";
            head[11] = "出库日期";
            PageResult<Map<String, Object>> ps = dao.queryRep(request, 1, 99999, logonUser);
            List list1 = new ArrayList();
            if (ps != null && ps.getRecords() != null) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[25];
                        detail[0] = CommonUtils.checkNull(map.get("RETAIL_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("IN_TYPE"));
                        detail[4] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_CNAME"));
//                        detail[6] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_TYPE"));
                        detail[7] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[8] = CommonUtils.checkNull(map.get("OUT_QTY"));
                        detail[9] = CommonUtils.checkNull(map.get("SALE_PRICE"));
                        detail[10] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
                        detail[11] = CommonUtils.checkNull(map.get("CREATE_DATE"));
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

        String name = "出库明细.xls";
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
