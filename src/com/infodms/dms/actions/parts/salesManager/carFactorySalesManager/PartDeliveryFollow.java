package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.salesManager.PartDeliveryFollowDao;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PartDeliveryFollow {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    String PART_DELIVERY_FOLLOW_QUERY = "/jsp/parts/salesManager/carFactorySalesManager/partDeliveryFollowQuery.jsp";
    PartDeliveryFollowDao dao = PartDeliveryFollowDao.getInstance();

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2014-5-15
     * @Title : PartDeliveryFollowDao
     * @Description: 初始化
     */
    public void partDeliveryFollowInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.SORT_BY_ORDER_TYPE, "订单类型");
            stateMap.put(Constant.SORT_BY_PICK_ORDER_ID, "拣配单号");
            stateMap.put(Constant.SORT_BY_DEALER_CODE, "服务商代码");
            stateMap.put(Constant.SORT_BY_CREATE_DATE, "销售单生成日期");
            stateMap.put(Constant.SORT_BY_ROW_NUM, "行数");
            stateMap.put(Constant.SORT_BY_ROW_SQTY, "发货数量");
            stateMap.put(Constant.SORT_BY_PICK_PRINT, "拣配打印");
            stateMap.put(Constant.SORT_BY_PKG_STATUS, "包装状态");
            stateMap.put(Constant.SORT_BY_PKG_PRINT, "包装打印");
            stateMap.put(Constant.SORT_BY_TRANS_STATUS, "发运状态");
            stateMap.put(Constant.SORT_BY_TRANS_PRINT, "发运打印");
            stateMap.put(Constant.SORT_BY_OUT_DATE, "出库日期");
            stateMap.put(Constant.SORT_BY_IS_OVER, "是否已完成");

            Map<String, String> stateMap2 = new LinkedHashMap<String, String>();
            stateMap2.put(Constant.SORT_BY_PICK_PRINT, "拣配打印");
            stateMap2.put(Constant.SORT_BY_PKG_STATUS, "包装状态");
            stateMap2.put(Constant.SORT_BY_PKG_PRINT, "包装打印");
            stateMap2.put(Constant.SORT_BY_TRANS_STATUS, "发运状态");
            stateMap2.put(Constant.SORT_BY_TRANS_PRINT, "发运打印");

            act.setOutData("stateMap", stateMap);
            act.setOutData("stateMap2", stateMap2);

            act.setOutData("pastDate", CommonUtils.getPreviousXMonthFirst(-1));
            act.setOutData("nowDate", CommonUtils.getDate());
            act.setForword(PART_DELIVERY_FOLLOW_QUERY);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "入库初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void partDeliveryFollowQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryDatas(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "配件交货跟踪表查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DELIVERY_FOLLOW_QUERY);
        }
    }

    public void expExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[25];

            head[0] = "订单类型";
            head[1] = "订单号";
            head[2] = "BO单号";
            head[3] = "销售单";
            head[4] = "销售金额";
            head[5] = "拣配单";
            head[6] = "服务商代码";
            head[7] = "服务商";
            head[8] = "捡货日期";
            head[9] = "行数";
            head[10] = "发货数量";
            head[11] = "拣配打印";
            head[12] = "包装状态";
            head[13] = "包装打印";
            head[14] = "发运状态";
            head[15] = "发运打印";
            head[16] = "发运单";
            head[17] = "发运方式";
            head[18] = "承运物流";
            head[19] = "出库日期";
            head[20] = "现场BO行数";
            head[21] = "现场BO数量";
            head[22] = "是否完成";

            PageResult<Map<String, Object>> ps = dao.queryDatas(request, 1,
                    Constant.PAGE_SIZE_MAX);
            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[25];
                        detail[0] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                        detail[1] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("BO_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("SO_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("AMOUNT"));
                        detail[5] = CommonUtils.checkNull(map.get("PICK_ORDER_ID"));
                        detail[6] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[7] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[8] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[9] = CommonUtils.checkNull(map.get("ROW_NUM"));
                        detail[10] = CommonUtils.checkNull(map.get("ROW_SQTY"));
                        detail[11] = CommonUtils.checkNull(map.get("PICK_PRINT"));
                        detail[12] = CommonUtils.checkNull(map.get("PKG_STATUS"));
                        detail[13] = CommonUtils.checkNull(map.get("PKG_PRINT"));
                        detail[14] = CommonUtils.checkNull(map.get("TRANS_STATUS"));
                        detail[15] = CommonUtils.checkNull(map.get("TRANS_PRINT"));
                        detail[16] = CommonUtils.checkNull(map.get("TRPLAN_CODE"));
                        detail[17] = CommonUtils.checkNull(map.get("TRANS_TYPE"));
                        detail[18] = CommonUtils.checkNull(map.get("TRANSPORT_ORG"));
                        detail[19] = CommonUtils.checkNull(map.get("OUT_DATE"));
                        detail[20] = CommonUtils.checkNull(map.get("BO_LINES"));
                        detail[21] = CommonUtils.checkNull(map.get("BO_QTY"));
                        detail[22] = CommonUtils.checkNull(map.get("IS_OVER"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, 1);
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
            act.setForword(PART_DELIVERY_FOLLOW_QUERY);
        }
    }

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list, int flag)
            throws Exception {

        String name = "配件交货跟踪表信息.xls";
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
                    /* ws.addCell(new Label(i, z, str[i])); */// modify by yuan
                    if (CheckUtil.checkFormatNumber1(str[i] == null ? ""
                            : str[i])) {
                        ws.addCell(new jxl.write.Number(i, z, Double
                                .parseDouble(str[i])));
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