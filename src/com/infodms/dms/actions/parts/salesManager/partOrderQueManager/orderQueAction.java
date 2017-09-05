package com.infodms.dms.actions.parts.salesManager.partOrderQueManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.salesManager.partOrderQueManager.orderQueDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartLogisticsInfoPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理配件订单进度查询业务
 * @Date: 2013-5-14
 * @remark
 */
public class orderQueAction extends BaseImport {

    public Logger logger = Logger.getLogger(orderQueAction.class);
    private static final orderQueDao dao = orderQueDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    
    private static final int orderType1 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01;//紧急
    private static final int orderType2 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02;//常规
//    private static final int orderType3 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03;//计划
    private static final int orderType4 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04;//直发
//    private static final int orderType5 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05;//调拨
//    private static final int orderType6 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_06;//BO订单
//    private static final int orderType7 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07;//市场升级订单
    private static final int orderType8 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08;//促销订单
    private static final int orderType12 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12;//销售采购订单
    
    private static final int orderState1 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01;//已保存
    private static final int orderState2 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02;//已提交
    private static final int orderState3 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03;//已审核
    private static final int orderState4 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04;//已作废
    private static final int orderState5 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05;//已驳回
    private static final int payType1 = Constant.CAR_FACTORY_SALES_PAY_TYPE_01;//现金
    private static final int payType2 = Constant.CAR_FACTORY_SALES_PAY_TYPE_02;//支票
    private static final int payType3 = Constant.CAR_FACTORY_SALES_PAY_TYPE_03;//其他
    private static final String USER_ROLE_OEM = "1"; //主机厂
    private static final String USER_ROLE_PDR = "2"; //供应中心
    private static final String USER_ROLE_DLR = "3"; //服务商
    /**
     * 配件订单查询-查询首页
     */
    private static final String PART_STOCK_MAIN = "/jsp/parts/salesManager/partOrderQueManager/orderQueMain.jsp";
    /**
     * 配件订单查询-进度查看页面
     */
    private static final String PART_STOCK_VIEW = "/jsp/parts/salesManager/partOrderQueManager/orderQueView.jsp";
    

    /**
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 文件导出为xls文件
     */
    public static Object exportEx(String fileName, ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = fileName + ".xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(name, "utf-8"));
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

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-5-13
     * @Title : 跳转至配件配件订单进度查询页面
     */
    public void orderQueInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String companyName = ""; //制单单位
            String userRole = "";//用户角色

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                userRole = USER_ROLE_OEM;
                companyName = dao.getMainCompanyName(parentOrgId);
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
                companyName = dao.getDealerName(parentOrgId);
                TmDealerPO selPo = new TmDealerPO();

                selPo.setDealerId(Long.parseLong(parentOrgId));

                List<TmDealerPO> dlrList = dao.select(selPo);
                TmDealerPO resPo = null;
                if (null != dlrList && dlrList.size() == 1) {
                    resPo = dlrList.get(0);
                    int pDlrType;
                    if (null != resPo.getPdealerType() && !"".equals(resPo.getPdealerType())) {
                        pDlrType = resPo.getPdealerType();
                        if (Constant.PART_SALE_PRICE_DEALER_TYPE_01 == pDlrType) {
                            userRole = USER_ROLE_PDR;
                        } else {
                            userRole = USER_ROLE_DLR;
                        }
                    } else {
                        userRole = USER_ROLE_DLR;
                    }
                }
            }

            String dealerId = logonUser.getDealerId();
            if (dealerId == null || "".equals(dealerId)) {
                act.setOutData("OK", 1);
            }

            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put("审核中", "审核中");
            stateMap.put("拣货中", "拣货中");
            stateMap.put("装箱中", "装箱中");
            stateMap.put("发运中", "发运中");
            stateMap.put("验收中", "验收中");
            stateMap.put("已验收", "已验收");
            stateMap.put("已关闭", "已关闭");

            Map<String, String> orderType = new LinkedHashMap<String, String>();
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02 + "", "常规订单");
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 + "", "紧急订单");
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "", "委托订单");
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "", "促销订单");
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12 + "", "销售采购订单");

            act.setOutData("stateMap", stateMap);
            act.setOutData("orderType", orderType);
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("companyName", companyName);
            act.setOutData("userRole", userRole);
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("dealerId", logonUser.getDealerId());
            act.setForword(PART_STOCK_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件配件订单进度查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title : 查询配件配件订单进度查询信息-配件订单查询
     */
    public void orderQueSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderMainInfos(request, logonUser, curPage, Constant.PAGE_SIZE);
            //页面汇总
            Map<String, Object> getSumResult = dao.querySumPartDlrOrder(request, logonUser);

            act.setOutData("ORDERQTY", getSumResult.get("ORDERQTY"));
            act.setOutData("ORDERAMOUNT", getSumResult.get("ORDERAMOUNT"));
            act.setOutData("OUTQTY", getSumResult.get("OUTQTY"));
            act.setOutData("OUTAMOUNT", getSumResult.get("OUTAMOUNT"));
            act.setOutData("ORDERCNT", getSumResult.get("ORDERCNT"));
            act.setOutData("OUTCNT", getSumResult.get("OUTCNT"));
            System.out.println("==ORDERQTY:"+getSumResult.get("ORDERQTY"));
            System.out.println("==ORDERAMOUNT:"+getSumResult.get("ORDERAMOUNT"));
            System.out.println("==OUTQTY:"+getSumResult.get("OUTQTY"));
            System.out.println("==OUTAMOUNT:"+getSumResult.get("OUTAMOUNT"));
            System.out.println("==ORDERCNT:"+getSumResult.get("ORDERCNT"));
            System.out.println("==OUTCNT:"+getSumResult.get("OUTCNT"));
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件订单查询信息错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 跳转至配件订单进度查询查看页面
     */
    public void viewDeatilInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));// 订单ID
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND OM.ORDER_ID = '" + orderId + "' ");
            Map<String, Object> map = dao.queryPartOrderMainInfosList(sbString.toString()).get(0);

            int orderType = Integer.parseInt(map.get("ORDER_TYPE").toString());
            int orderState = Integer.parseInt(map.get("STATE").toString());
            String payTypeStr = map.get("PAY_TYPE").toString();
            int payType = payType1;
            if (null != payTypeStr && !"".equals(payTypeStr)) {
                payType = Integer.parseInt(payTypeStr);
            }
            if (orderType1 == orderType) {
                map.put("ORDER_TYPE", "紧急");
            } else if (orderType2 == orderType) {
                map.put("ORDER_TYPE", "常规");
            } else if (orderType4 == orderType) {
                map.put("ORDER_TYPE", "直发");
            } else if (orderType8 == orderType) {
                map.put("ORDER_TYPE", "促销");
            }else if (orderType12 == orderType) {
                map.put("ORDER_TYPE", "销售采购");
            }//以下两种众泰未用
//            } else if (orderType3 == orderType) {
//                map.put("ORDER_TYPE", "计划");
//            } else if (orderType5 == orderType) {
//                map.put("ORDER_TYPE", "调拨");

            if (orderState1 == orderState) {
                map.put("STATE", "已保存");
            } else if (orderState2 == orderState) {
                map.put("STATE", "已提交");
            } else if (orderState3 == orderState) {
                map.put("STATE", "已审核");
            } else if (orderState4 == orderState) {
                map.put("STATE", "已作废");
            } else if (orderState5 == orderState) {
                map.put("STATE", "已驳回");
            }

            if (payType1 == payType) {
                map.put("PAY_TYPE", "现金");
            } else if (payType2 == payType) {
                map.put("PAY_TYPE", "支票");
            } else if (payType3 == payType) {
                map.put("PAY_TYPE", "其他");
            }

            String sbStr = "AND OH.ORDER_ID = '" + orderId + "' ";
            List<Map<String, Object>> historyList = dao.getOrderOperationHistory(sbStr);
            Map<String, Object> hisMap = null;
            int hisOrdState;
            if (null != historyList && historyList.size() > 0) {
                for (int i = 0; i < historyList.size(); i++) {
                    hisMap = historyList.get(i);
                    hisOrdState = Integer.parseInt(hisMap.get("STATUS").toString());
                    if (orderState1 == hisOrdState) {
                        hisMap.put("STATUS", "已保存");
                        historyList.set(i, hisMap);
                    } else if (orderState2 == hisOrdState) {
                        hisMap.put("STATUS", "已提交");
                        historyList.set(i, hisMap);
                    } else if (orderState3 == hisOrdState) {
                        hisMap.put("STATUS", "已审核");
                        historyList.set(i, hisMap);
                    } else if (orderState4 == hisOrdState) {
                        hisMap.put("STATUS", "已作废");
                        historyList.set(i, hisMap);
                    } else if (orderState5 == hisOrdState) {
                        hisMap.put("STATUS", "已驳回");
                        historyList.set(i, hisMap);
                    }
                }
            }
            act.setOutData("map", map);
            act.setOutData("list", historyList);
            act.setForword(PART_STOCK_VIEW);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件订单进度查询查看或查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title : 配件订单进度查询详情查询
     */
    public void partOrderDetailSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // BO详情单ID

            StringBuffer sbString = new StringBuffer();
            if (null != orderId && !"".equals(orderId)) {
                sbString.append(" AND OD.ORDER_ID = '" + orderId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartOrderDtlInfos(sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件配件订单进度查询信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-5-3
     * @Title :导出配件配件订单进度查询信息
     */
    public void exportOrderExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {

            PageResult<Map<String, Object>> ps = dao.queryPartOrderMainInfos(request, logonUser, 1, Constant.PAGE_SIZE_MAX);

            String[] head = new String[31];
            head[0] = "序号";
            head[1] = "订单类型";
            head[2] = "订单次数";
            head[3] = "订单号";
            head[4] = "订单金额";
            head[5] = "BO单号";
            head[6] = "流水号";
            head[7] = "销售单状态";
            head[8] = "订货单位编码";
            head[9] = "订货单位";
            head[10] = "订货人";
            head[11] = "提交日期";
            head[12] = "流水金额";
            head[13] = "初审日期";
            head[14] = "审核金额";
            head[15] = "财务审核日期";
            head[16] = "装箱完成日期";
            head[17] = "发运金额";
            head[18] = "订单备注";
            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[31];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("CODE_DESC"));
                        detail[2] = CommonUtils.checkNull(map.get("ORDER_NUM"));
                        detail[3] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
                        detail[5] = CommonUtils.checkNull(map.get("BO_CODE"));
                        detail[6] = CommonUtils.checkNull(map.get("SO_CODE"));
                        detail[7] = CommonUtils.checkNull(map.get("SO_STATE"));
                        detail[8] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[9] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[10] = CommonUtils.checkNull(map.get("NAME"));
                        detail[11] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                        detail[12] = CommonUtils.checkNull(map.get("AMOUNT"));
                        detail[13] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[14] = CommonUtils.checkNull(map.get("SH_AMOUNT"));
                        detail[15] = CommonUtils.checkNull(map.get("FCAUDIT_DATE"));
                        detail[16] = CommonUtils.checkNull(map.get("PKG_OVER_DATE"));
                        detail[17] = CommonUtils.checkNull(map.get("TRANS_AMOUNT"));
                        detail[18] = CommonUtils.checkNull(map.get("REMARK"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件订单查询";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件配件订单进度查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-8-21
     * @Title : 导出EXECEL模板
     * @Description: TODO
     */
    public void exportExcelTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        try {
            List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            //标题
            listHead.add("物流单号");
            listHead.add("物流发生时间");
            listHead.add("物流信息");
            list.add(listHead);
            // 导出的文件名
            String fileName = "配件订单导入模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);


            os = response.getOutputStream();
//				CsvWriterUtil.writeCsv(list, os);
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2014-8-21
     * @Title :Excel导入
     * @Description: TODO
     */
    public void uploadPartLogExcel() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();
        try {
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 3, 3, maxSize);

            String err = "";

            if (errNum != 0) {
                switch (errNum) {
                    case 1:
                        err += "文件列数过多,请修改后再上传!";
                        break;
                    case 2:
                        err += "空行不能大于三行,请修改后再上传!";
                        break;
                    case 3:
                        err += "文件内容不能为空,请修改后再上传!";
                        break;
                    case 4:
                        err += "文件类型错误,请重新上传!";
                        break;
                    case 5:
                        err += "文件不能大于" + maxSize + ",请修改后再上传";
                        break;
                    default:
                        break;
                }
            }

            if (!"".equals(err)) {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, err);
                throw e1;
            } else {

                List<Map> list = getMapList();
                List voList = new ArrayList();

                loadVoList(voList, list, errorInfo);
                if (errorInfo.length() > 0) {

                    BizException e1 = new BizException(act,
                            ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                    throw e1;
                }
                for (int i = 0; i < voList.size(); i++) {

                    TtPartLogisticsInfoPO logisticsInfoPo = (TtPartLogisticsInfoPO) voList.get(i);

                    if (errorInfo.length() > 0) {
                        BizException e1 = new BizException(act,
                                ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                        throw e1;
                    }

                    logisticsInfoPo.setLogisticsId(Long.parseLong(SequenceManager.getSequence("")));
                    logisticsInfoPo.setCreateBy(logonUser.getUserId());
                    logisticsInfoPo.setCreateDate(new Date());
                    dao.insert(logisticsInfoPo);
                }
            }
            String dealerId = logonUser.getDealerId();
            if (dealerId == null || "".equals(dealerId)) {
                int ok = 1;
                act.setOutData("OK", ok);
            }

            act.setForword(PART_STOCK_MAIN);

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件读取错误");
            }
            logger.error(logonUser, e1);
            String dealerId = logonUser.getDealerId();
            if (dealerId == null || "".equals(dealerId)) {
                int ok = 1;
                act.setOutData("OK", ok);
            }
            act.setException(e1);
            act.setForword(PART_STOCK_MAIN);
        }
    }


    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :循环获取CELL
     * @Description: TODO
     */
    private void loadVoList(List voList, List<Map> list, StringBuffer errorInfo)
            throws Exception {
        if (null == list) {
            list = new ArrayList();
        }

        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (null == map) {
                map = new HashMap<String, Cell[]>();
            }
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                parseCells(voList, key, cells, errorInfo);
                if (errorInfo.length() > 0) {
                    break;
                }
            }
        }
    }

    private void parseCells(List list, String rowNum, Cell[] cells,
                            StringBuffer errorInfo) throws Exception {

        TtPartLogisticsInfoPO logisticsInfoPo = new TtPartLogisticsInfoPO();


        logisticsInfoPo.setLogisticsNo(subCell(cells[0].getContents().trim()));

        List<TtPartLogisticsInfoPO> t = dao.select(logisticsInfoPo);

        if (!(t == null)) {
            dao.delete(logisticsInfoPo);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dstr = subCell(cells[1].getContents().trim());
        java.util.Date date = sdf.parse(dstr);

        logisticsInfoPo.setLogisticsDate(date);

        logisticsInfoPo.setLogisticsInfo(subCell(cells[2].getContents().trim()));

        list.add(logisticsInfoPo);
    }

    /**
     * 截取字符串
     *
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-7
     * @Title :
     * @Description: TODO
     */
    private String subCell(String orgAmt) {
        String newAmt = "";
        if (null == orgAmt || "".equals(orgAmt)) {
            return newAmt;
        }
        if (orgAmt.length() > 30) {
            newAmt = orgAmt.substring(0, 30);
        } else {
            newAmt = orgAmt;
        }
        return newAmt;
    }
}
