package com.infodms.dms.actions.parts.salesManager.partInStockQueManager;

import com.infodms.dms.actions.claim.basicData.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.salesManager.partInStockQueManager.inStockQueDao;
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

public class InStockDetailQueAction extends BaseImport {
    public Logger logger = Logger.getLogger(inStockQueAction.class);
    private static final inStockQueDao dao =  inStockQueDao.getInstance();
    private static final String INIT_URL = "/jsp/parts/salesManager/partInStockQueManager/inStockDetailQueView.jsp";

    public void init() {
        ActionContext act = ActionContext.getContext();
//        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getPreviousXMonthFirst(-2));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(INIT_URL);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件入库单详细查询页面初始化失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void query() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
//            Long qtySum = 0l;
//            Double amountSum = 0d;
            
            PageResult<Map<String, Object>> ps = dao.queryDetail(request, Constant.PAGE_SIZE, curPage);
//            List<Map<String, Object>> list = dao.queryDetail(request, Constant.PAGE_SIZE_MAX, 1).getRecords();
//            if (list != null && list.size() > 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    qtySum += Long.valueOf(list.get(i).get("IN_QTY") + "");
//                    amountSum += Double.valueOf(list.get(i).get("AMOUNT").toString().replace(",", ""));
//                }
//            }
//            act.setOutData("qtySum", qtySum);
//            act.setOutData("amountSum", amountSum.floatValue());
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配件入库单详细查询失败!");
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
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[25];
//            head[0] = "采购订单号";
//            head[1] = "订单类型";
            head[0] = "发运单号";
            head[1] = "入库单号";
            head[2] = "配件编码";
            head[3] = "配件名称";
//            head[4] = "配件件号";
            head[4] = "单位";
            head[5] = "入库数量";
            head[6] = "采购单价";
            head[7] = "采购金额";
            head[8] = "入库日期";
            head[9] = "备注";
            List<Map<String, Object>> list = dao.queryDetail(request, Constant.PAGE_SIZE_MAX, 1).getRecords();
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[25];
//                    detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
//                    detail[1] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    detail[0] = CommonUtils.checkNull(map.get("TRANS_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("IN_CODE"));
                    detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                    detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
//                    detail[4] = CommonUtils.checkNull(map.get("PART_CODE"));
                    detail[4] = CommonUtils.checkNull(map.get("UNIT"));
                    detail[5] = CommonUtils.checkNull(map.get("IN_QTY"));
                    detail[6] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                    detail[7] = CommonUtils.checkNull(map.get("AMOUNT").toString().replace(",", ""));
                    detail[8] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[9] = CommonUtils.checkNull(map.get("REMARK"));

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

        String name = "入库单详细.xls";
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
