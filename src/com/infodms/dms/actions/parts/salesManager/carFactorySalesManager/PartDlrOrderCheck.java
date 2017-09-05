package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanConfirm.PartPlanConfirmDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartSoManageDao;
import com.infodms.dms.dao.parts.salesManager.PartTransPlanDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.MarketPartDlrOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 配件订单审核
 * @author  
 * @version  
 * @see 
 * @since 
 * @deprecated
 */
public class PartDlrOrderCheck extends BaseImport implements PTConstants {
    private static final String PART_BO_QUERY = "/jsp/parts/salesManager/carFactorySalesManager/dlrOrderBoQuery.jsp";
    private static final String PART_OEM_GX_ORDER_ADD = "/jsp/parts/salesManager/carFactorySalesManager/partOemGxOrderAdd.jsp";
    public Logger logger = Logger.getLogger(PartDlrOrderCheck.class);

    PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
    
    String PART_DIRECT_DLR_ORDER_CHECK_QUERY = "/jsp/parts/salesManager/carFactorySalesManager/partDirectDlrOrderQuery.jsp";
    String PART_PLAN_DLR_ORDER_CHECK_QUERY = "/jsp/parts/salesManager/carFactorySalesManager/partPlanDlrOrderQuery.jsp";
    String PART_MARKET_DLR_ORDER_CHECK_QUERY = "/jsp/parts/salesManager/carFactorySalesManager/partMarketDlrOrderQuery.jsp";

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

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {
        String name = "配件订单审核列表.xls";
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
            int pageSize = list.size() / 30000;
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

    /**
     * 配件审核查询-初始化
     */
    public void partDlrOrderCheckInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        boolean salerFlag = false;
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String userType = null;
            TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
            userposeDefinePO.setUserId(loginUser.getUserId());
            if (dao.select(userposeDefinePO).size() > 0) {
                userposeDefinePO = (TtPartUserposeDefinePO) dao.select(userposeDefinePO).get(0);
                userType = userposeDefinePO.getUserType() + "";
            }
            List<Map<String, Object>> salerList = dao.getSaler(userType);

            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                salerFlag = true;

            }
            Map<String, Object> map = new HashMap<String, Object>();
            if (null != act.getSession().get("condition")) {
                if (null != request.getParamValue("flag")) {
                    map = (Map) act.getSession().get("condition");
                    act.getSession().remove("condition");
                } else {
                    act.getSession().remove("condition");
                }
            }


            Map<String, String> orderType = new LinkedHashMap<String, String>();
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02 + "", "常规订单");
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 + "", "紧急订单");
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "", "委托订单");
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "", "促销订单");
            orderType.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12 + "", "销售采购订单");

            act.setOutData("orderType", orderType);
            act.setOutData("condition", map);
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("planFlag", "noPlan");
            act.setOutData("curUserId", loginUser.getUserId());
            act.setOutData("salerList", salerList);
            act.setOutData("salerFlag", salerFlag);
            act.setForword(PART_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售审核初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 广宣品订单审核初始化
     */
    public void partGxDlrOrderCheckInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        boolean salerFlag = false;
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            List<Map<String, Object>> salerList = dao.getSaler();

            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                salerFlag = true;

            }
            Map<String, Object> map = new HashMap<String, Object>();
            if (null != act.getSession().get("condition")) {
                if (null != request.getParamValue("flag")) {
                    map = (Map) act.getSession().get("condition");
                    act.getSession().remove("condition");
                } else {
                    act.getSession().remove("condition");
                }
            }

            act.setOutData("condition", map);
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("planFlag", "noPlan");
            act.setOutData("curUserId", loginUser.getUserId());
            act.setOutData("salerList", salerList);
            act.setOutData("salerFlag", salerFlag);
            act.setForword(PART_DLR_GXORDER_CHECK_QUERY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售审核初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 配件销售审核初始化
     */
    public void partDirectDlrOrderCheckInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            act.setOutData("now", now);
            act.setForword(PART_DIRECT_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售审核初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 配件计划销售审核初始化
     */
    public void partPlanDlrOrderCheckInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("planFlag", "plan");
            act.setForword(PART_PLAN_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售审核初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 配件计划销售审核初始化
     */
    public void partMarketDlrOrderCheckInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("planFlag", "plan");
            act.setForword(PART_MARKET_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售审核初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件订单审核-查询
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件销售审核查询
     */
    public void partDlrOrderQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = null;
            saveQueryCondition();
            Map<String, Object> getSumResult = dao.querySumPartDlrOrder(request);
            ps = dao.queryCheckPartDlrOrder(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("accountSum", getSumResult.get("AMOUNT"));
            act.setOutData("xs", getSumResult.get("XS"));
            
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售订单审核查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 广宣订单审核查询
     */
    public void partDlrOrderGxQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryCheckPartDlrGxOrder(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣订单审核查询失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER_CHECK_QUERY);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 订单提报明细-查看
     */
    public void detailDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            MarketPartDlrOrderDao dao = MarketPartDlrOrderDao.getInstance();
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));
            String showFlag = null;

            if (loginUser.getDealerId() == null) {
                showFlag = "1";
            }
            String buttonFalg = "";//采购订单查看 隐藏 【返回】按钮
            if (null != request.getParamValue("buttonFalg")) {
                buttonFalg = request.getParamValue("buttonFalg");
            }

            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);
            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            mainMap.put("TRANS_TYPE", transType);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            request.setAttribute("detailList", detailList);

            act.setOutData("buttonFalg", buttonFalg);
            act.setOutData("orderId", orderId);
            act.setOutData("showFlag", showFlag);
            act.setForword(PART_DLR_ORDER_CHECK_DETAIL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER_CHECK_QUERY);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-10
     * @Title : 订单提报-订单明细导出
     */
    public void exportOrderExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            // 订单单号
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); 
            // 订单ID
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); 

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            head[3] = "最小包装量";
            head[4] = "单位";
            head[5] = "订货数量";
            head[6] = "有效数量";
            head[7] = "关闭数量";
            head[8] = "订货单价";
            head[9] = "有效订货金额";
            head[10] = "备注";
            if (logonUser.getDealerId() == null) {
                head[11] = "当前库存";
            }

            List<Map<String, Object>> list = dao.queryPartDlrOrderDetail(orderId);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[3] = CommonUtils.checkNull(map.get("MIN_PACKAGE"));
                        detail[4] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[5] = CommonUtils.checkNull(map.get("BUY_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("YX_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("CLOSE_QTY"));
                        detail[8] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                        detail[9] = CommonUtils.checkNull(map.get("YX_AMOUNT"));
                        detail[10] = CommonUtils.checkNull(map.get("REMARK"));
                        if (logonUser.getDealerId() == null) {
                            detail[11] = CommonUtils.checkNull(map.get("STOCK_QTY"));
                        }
                        list1.add(detail);
                    }
                }
            }

            String fileName = "订单" + orderCode + "明细";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),request, head, list1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出采购订单明细失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 选择经销商弹出页面
     */
    public void selSales() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            String parentorgName = CommonUtils.checkNull(request.getParamValue("PARENTORG_NAME"));
            String parentorgCode = CommonUtils.checkNull(request.getParamValue("PARENTORG_CODE"));
            int page_size = Constant.PAGE_SIZE;
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页	
            Integer poseBusType = logonUser.getPoseBusType();
            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, type, parentorgName, parentorgCode, "", page_size, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取数据错误!,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 选择地址弹出页面
     */
    public void selAddress() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            String parentorgName = CommonUtils.checkNull(request.getParamValue("PARENTORG_NAME"));
            String parentorgCode = CommonUtils.checkNull(request.getParamValue("PARENTORG_CODE"));
            int page_size = Constant.PAGE_SIZE;
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页	
            Integer poseBusType = logonUser.getPoseBusType();
            PageResult<Map<String, Object>> ps = dao.getAddress(dealerId, page_size, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取数据错误!,请联系管理员");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 选择地址弹出页面
     */
    public void getPartItemStock() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            if ("".equals(partId)) {
                return;
            }
            partId = partId.replaceFirst(",", "");
            String partArr[] = partId.split(",");
            String dealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            request.setAttribute("orgId", dealerId);
            List<Map<String, Object>> list = new ArrayList();
            for (int i = 0; i < partArr.length; i++) {
                Map<String, Object> ps = dao.getPartItemStock(whId, partArr[i]);
                if (null == ps) {
                    ps = new HashMap<String, Object>();
                    ps.put("PART_ID", partArr[i]);
                    ps.put("NORMAL_QTY", "0");
                }
                list.add(ps);
            }
            act.setOutData("list", list);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取数据错误!,请联系管理员");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 查看配件
     */
    public void showPartBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            String dealerId = CommonUtils.checkNull(req.getParamValue("SELLER_ID"));
            //分页方法 begin
            Integer curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")): 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = null;
            ps = dao.showPartBase(req, dealerId, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查看配件信息出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 保存日志 Tt_Part_Operation_History
     * @param req
     * @param act
     * @param status
     * @throws Exception
     */
    public void saveHistory(String orderType,RequestWrapper req, ActionContext act, int status) throws Exception {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            //订单编号
            Long orderId = Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId")));
            TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
            Long optId = Long.parseLong(SequenceManager.getSequence(""));
            po.setBussinessId(CommonUtils.checkNull(req.getParamValue("orderCode")));
            po.setOptId(optId);
            po.setOptBy(logonUser.getUserId());
            po.setOptDate(new Date());
            po.setOptType(Constant.PART_OPERATION_TYPE_01);
            po.setWhat("配件订单审核");
            po.setOptName(logonUser.getName());
            po.setStatus(status);
            po.setOrderId(orderId);
            dao.insert(po);
            if(!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04) && 
               !orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12)){
                //还需要插入销售单保存初始历史
                po = new TtPartOperationHistoryPO();
                Long optId1 = Long.parseLong(SequenceManager.getSequence(""));
                po.setBussinessId(CommonUtils.checkNull(req.getAttribute("soCode")));
                po.setOptId(optId1);
                po.setOptBy(logonUser.getUserId());
                po.setOptDate(new Date());
                po.setOptType(Constant.PART_OPERATION_TYPE_02);
                po.setWhat("配件销售单");
                po.setOptName(logonUser.getName());
                po.setStatus(Constant.CAR_FACTORY_SALE_ORDER_STATE_01);
                po.setOrderId(orderId);
                dao.insert(po);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 审核订单-明细查看
     */
    public void checkDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String accountPurpose = CommonUtils.checkNull(request.getParamValue("ACCOUNT_PURPOSE"));
            //根据订单id查询订单信息
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            
            if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已改变!请重新查询!");
                throw e1;
            }

            String dealerId = "";
            String flag = "";//用于前端是否展示供应商字段，1展示2不展示
           
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            //折扣率         
            String discount = dao.getDiscount(mainMap.get("DEALER_ID").toString());
            mainMap.put("discount", discount);
            mainMap.put("saleName", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = dao.getAccountMoney(mainMap.get("DEALER_ID").toString(), mainMap.get("SELLER_ID").toString(), accountPurpose);
            String accountSumNow = "";
            String accountKyNow = "";
            String accountDjNow = "";
            boolean accountFlag = false;
            if (null != accountMap) {
                accountFlag = true;
                accountSumNow = accountMap.get("ACCOUNT_SUM").toString();
                accountKyNow = accountMap.get("ACCOUNT_KY").toString();
                accountDjNow = accountMap.get("ACCOUNT_DJ").toString();
            }
            mainMap.put("accountFlag", accountFlag);
            mainMap.put("accountSumNow", accountSumNow);
            mainMap.put("accountKyNow", accountKyNow);
            mainMap.put("accountDjNow", accountDjNow);

            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());
            List<Map<String, Object>> wareHouseFacList = null;
            //20170904 屏蔽 start
//            String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
//            if (sellerId.equals(Constant.OEM_ACTIVITIES + "")) {
//                wareHouseList = dao.getWareHouse(lo.get(0).getOrgId().toString(), CommonUtils.checkNull(mainMap.get("PRODUCE_FAC")));
//                wareHouseFacList = dao.getWareHouseFac(lo.get(0).getOrgId().toString(), CommonUtils.checkNull(mainMap.get("PRODUCE_FAC")));//产地，众泰没有
//            }
            //20170904 屏蔽 end
            //查询订单明细并取销售价格
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail4SODTL(orderId, wareHouseList.get(0).get("WH_ID").toString());
            
            //委托订单、销售采购订单审核（便于前台展示制造商，暂时不用）
            if (CommonUtils.checkNull(mainMap.get("ORDER_TYPE")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")
            	|| CommonUtils.checkNull(mainMap.get("ORDER_TYPE")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12 + "")) {
                if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                    flag = "1";
//                    for (Map<String, Object> map : detailList) {
//                        String partId = map.get("PART_ID").toString();
//                        List<Map<String, Object>> venderList = dao.getStoVender(CommonUtils.checkNull(map.get("DEFT_ID")));
//                        
//                        if (venderList != null && venderList.size() > 0 && venderList.get(0) != null) {
//                            map.put("venderId", CommonUtils.checkNull(venderList.get(0).get("VENDER_ID")));
//                            map.put("venderName", CommonUtils.checkNull(venderList.get(0).get("VENDER_NAME")));
//                        }
//                    }
                } else {
                    flag = "2";
                }
            }
            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);


//            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            PartTransPlanDao daoTrans=PartTransPlanDao.getInstance();
            List list =  daoTrans.getTransportType();// 获取配件发运方式
            
            //获取服务商联系人和联系方式
            String orderDealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
            Map<String, Object> map = dao.getAddrMap(orderDealerId);
            Map<String, Object> dealerMap = dao.getDealerName(orderDealerId);

            request.setAttribute("phone", CommonUtils.checkNull(dealerMap.get("TEL")));
            request.setAttribute("linkMan", CommonUtils.checkNull(dealerMap.get("LINKMAN")));
            request.setAttribute("phone2", CommonUtils.checkNull(map.get("TEL2")));
            request.setAttribute("linkMan2", CommonUtils.checkNull(map.get("LINKMAN2")));
            request.setAttribute("transList", list);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("wareHouseFacList", wareHouseFacList);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            request.setAttribute("detailList", detailList);
            request.setAttribute("flag", flag);
            request.setAttribute("planFlag", CommonUtils.checkNull(request.getParamValue("planFlag")));
            request.setAttribute("oemFlag", CommonUtils.checkNull(mainMap.get("OEM_FLAG")));
            request.setAttribute("produceFac", CommonUtils.checkNull(mainMap.get("PRODUCE_FAC")));
            act.setForword(PART_DLR_ORDER_CHECK_CHECK);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER_CHECK_QUERY);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 审核广宣订单
     */
    public void checkDlrGxOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            MarketPartDlrOrderDao dao = MarketPartDlrOrderDao.getInstance();
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String accountPurpose = CommonUtils.checkNull(request.getParamValue("ACCOUNT_PURPOSE"));
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已改变!请重新查询!");
                throw e1;
            }

            String orgId = "";
            String flag = "";
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            dealerId = mainMap.get("DEALER_ID").toString();
            mainMap.put("discount", 1);
            mainMap.put("saleName", loginUser.getName());

            //获取账户
            List<Map<String, Object>> dealerBusinessArea = MaterialGroupManagerDao.getDealerBusinessArea(mainMap.get("DEALER_ID").toString());
            String yieldId = dealerBusinessArea.get(0).get("AREA_ID").toString();

            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, yieldId, "1", "", "", "", Constant.PAGE_SIZE_MAX, 1);
            List<Map<String, Object>> acountMap = ps.getRecords();
            Map<String, Object> accMap = acountMap.get(0);
            String accountSumNow = "";
            String accountKyNow = "";
            String accountDjNow = "";
            boolean accountFlag = false;
            if (null != accMap) {
                accountFlag = true;
                accountSumNow = accMap.get("ACCOUNT_SUM").toString();
                accountKyNow = accMap.get("ACCOUNT_KY").toString();
                accountDjNow = accMap.get("ACCOUNT_DJ").toString();
            }
            mainMap.put("accountFlag", accountFlag);
            mainMap.put("accountSumNow", accountSumNow);
            mainMap.put("accountKyNow", accountKyNow);
            mainMap.put("accountDjNow", accountDjNow);

            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString(), "2");
            String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
            /*if(sellerId.equals(Constant.OEM_ACTIVITIES+"")){
                wareHouseList = dao.getWareHouse(lo.get(0).getOrgId().toString(), CommonUtils.checkNull(mainMap.get("PRODUCE_FAC")));
			}*/
            //查询订单明细并取销售价格
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail4SODTL(orderId, wareHouseList.get(0).get("WH_ID").toString());

            Long allBuyQty = 0l;
            List<Map<String, Object>> detailList1 = new ArrayList<Map<String, Object>>();

            for (Map<String, Object> map : detailList) {
                String partId = map.get("PART_ID").toString();
                Long buyQty = ((BigDecimal) map.get("BUY_QTY")).longValue();
                allBuyQty += buyQty;
                //获取当前配件已经提报的数量
                Map map1 = dao.getReportedQty(orderId, partId);
                Long reportedQty = ((BigDecimal) map1.get("REPORTED_QTY")).longValue();
                map.put("reportQty", buyQty - reportedQty);
                map.put("checkQty", reportedQty);
                if (buyQty > reportedQty) {
                    detailList1.add(map);
                }
            }

            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);


            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //获取服务商联系人和联系方式
            String orderDealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
            Map<String, Object> map = dao.getSalesRelation(dealerId, null, "3", "", "", "", 10, 1).getRecords().get(0);

            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            act.setOutData("now", now);
            request.setAttribute("phone2", CommonUtils.checkNull(map.get("TEL2")));
            request.setAttribute("linkMan2", CommonUtils.checkNull(map.get("LINKMAN2")));
            request.setAttribute("transList", list);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            request.setAttribute("detailList", detailList1);
            request.setAttribute("flag", flag);
            request.setAttribute("allBuyQty", allBuyQty);
            request.setAttribute("planFlag", CommonUtils.checkNull(request.getParamValue("planFlag")));
            act.setForword(PART_DLR_ORDER_CHECK_GX);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_GXORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣订单审核失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_GXORDER_CHECK_QUERY);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 审核计划订单
     */
    public void checkPlanDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String accountPurpose = CommonUtils.checkNull(request.getParamValue("ACCOUNT_PURPOSE"));
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            String dealerId = "";
            String flag = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            //折扣率         
            String discount = dao.getDiscount(mainMap.get("DEALER_ID").toString());
            mainMap.put("discount", discount);
            mainMap.put("saleName", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = dao.getAccountMoney(mainMap.get("DEALER_ID").toString(), mainMap.get("SELLER_ID").toString(), accountPurpose);
            String accountSumNow = "";
            String accountKyNow = "";
            String accountDjNow = "";
            boolean accountFlag = false;
            if (null != accountMap) {
                accountFlag = true;
                accountSumNow = accountMap.get("ACCOUNT_SUM").toString();
                accountKyNow = accountMap.get("ACCOUNT_KY").toString();
                accountDjNow = accountMap.get("ACCOUNT_DJ").toString();
            }
            mainMap.put("accountFlag", accountFlag);
            mainMap.put("accountSumNow", accountSumNow);
            mainMap.put("accountKyNow", accountKyNow);
            mainMap.put("accountDjNow", accountDjNow);
            List<Map<String, Object>> list = dao.queryPartDlrOrderDetail(orderId);
            List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> soMainList = dao.getSoMain(orderId);
            //如果没做过 销售单越过
            if (null != soMainList) {
                if (soMainList.size() > 0) {
                    String soIds = "";
                    for (Map<String, Object> map : soMainList) {
                        String soId = CommonUtils.checkNull(map.get("SO_ID"));
                        soIds += "," + soId;
                    }
                    if (!"".equals(soIds)) {
                        soIds = soIds.replaceFirst(",", "");
                    }
                    //做过销售单
                    for (Map<String, Object> map : list) {
                        String partId = CommonUtils.checkNull(map.get("PART_ID"));
                        Long saleQty = dao.getSoDetailPartNum(soIds, partId);
                        Long buyQty = Long.valueOf(CommonUtils.checkNull(map.get("BUY_QTY")));
                        if (buyQty <= saleQty) {
                            continue;
                        } else {
                            Long qty = buyQty - saleQty;
                            map.put("BUY_QTY", qty);
                            detailList.add(map);
                        }
                    }
                } else {
                    detailList = list;
                }
            } else {
                detailList = list;
            }

            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());

            List transList = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //获取服务商联系人和联系方式
            String orderDealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
            Map<String, Object> map = dao.getDealerName(orderDealerId);
            request.setAttribute("phone", CommonUtils.checkNull(map.get("PHONE")));
            request.setAttribute("linkMan", CommonUtils.checkNull(map.get("LINK_MAN")));
            request.setAttribute("transList", transList);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            request.setAttribute("detailList", detailList);
            request.setAttribute("flag", flag);
            request.setAttribute("planFlag", CommonUtils.checkNull(request.getParamValue("planFlag")));
            act.setForword(PART_DLR_ORDER_CHECK_CHECK);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_PLAN_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_PLAN_DLR_ORDER_CHECK_QUERY);
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
    public void exportPartPlanExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String[] head = new String[13];
            head[0] = "订单号 ";
            head[1] = "订货单位编码";
            head[2] = "订货单位";
            head[3] = "订货人";
            head[4] = "订货日期";
            head[5] = "销售单位";
            head[6] = "订货次数";
            head[7] = "订单类型";
            head[8] = "总金额";
            head[9] = "提交时间";
            head[10] = "订单状态";
            head[11] = "备注";
            PageResult<Map<String, Object>> list = dao.queryCheckPartDlrOrder(request, 1, 9999);
            List list1 = new ArrayList();
            if (list.getRecords() != null) {
                for (int i = 0; i < list.getRecords().size(); i++) {
                    Map map = (Map) list.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[13];
                        detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[5] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                        detail[6] = CommonUtils.checkNull(map.get("ORDER_NUM"));
                        detail[7] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                        detail[8] = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
                        detail[9] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[10] = CommonUtils.checkNull(map.get("STATE"));
                        detail[11] = CommonUtils.checkNull(map.get("REMARK"));
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
    public void exportPartGxExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String[] head = new String[9];
            head[0] = "订单号 ";
            head[1] = "订货单位编码";
            head[2] = "订货单位";
            head[3] = "订货人";
            head[4] = "订货日期";
            head[5] = "销售单位";
            head[6] = "总金额";
            head[7] = "提交时间";
            head[8] = "订单状态";
            List<Map<String, Object>> list = dao.queryCheckPartDlrGxOrder(request);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[9];
                    detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[3] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[5] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                    detail[6] = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
                    detail[7] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[8] = CommonUtils.checkNull(map.get("STATE"));
                    list1.add(detail);
                }

            }
            this.exportEx("广宣订单审核列表", ActionContext.getContext().getResponse(), request, head, list1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 总部审核-生成销售单
     * @throws : LastDate    : 2017-7-27
     * @Description:生成销售单
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void createSaleOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            request.setAttribute("boFlag", false);//bo单标志
            
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//订单ID
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); //订单CODE
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); //订单类型
            Double discount = Double.valueOf(request.getParamValue("DISCOUNT")); //折扣
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id")); //库房
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); //订货单位ID
            
            //校验是否有同时操作,防止重复生成销售单
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            String state = CommonUtils.checkNull(mainMap.get("STATE"));//订单状态
            if (state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "该订单已审核,请不要重复审核!");
                throw e1;
            }
            //判断是否订单已经生成销售单，防止重复生成
            orderDubCheck(orderId, dao, act);
            if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "").equals(orderType)) {
            	//委托订单
                //生成销售单-->总部采购单，采购单类型为经销商委托 -->总部虚拟入库和虚拟出库、发运单-->经销商入库(经销商端入库操作)
            	//1.生成销售单，包括销售订单主表、明细表
//            	this.insertOrderInfo(orderId,orderCode,orderType,discount,whId,dealerId);
            	//2.生成总部采购单，采购单类型：经销商委托
                //3.总部虚拟入库、出库、发运单
            	//更新订单发运方式
                String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE")); //发运方式
                TtPartDlrOrderMainPO po1=new TtPartDlrOrderMainPO();
                po1.setOrderId(Long.valueOf(orderId));
                TtPartDlrOrderMainPO updPo=new TtPartDlrOrderMainPO();
                updPo.setTransType(transType);
                dao.update(po1,updPo);
            	//生成总部采购单，总部虚拟入库和虚拟出库、发运单、销售单
                ArrayList ins1 = new ArrayList();
                ins1.add(0, Long.valueOf(orderId));
                ins1.add(1, loginUser.getUserId());
                dao.callProcedure("PROC_DIRECT_ORDER_DEAL", ins1, null);
                //更新订单状态
                this.changeOrderStatus(orderType,loginUser, orderId, act, request);
                
                act.setOutData("success", "委托订单" + CommonUtils.checkNull(request.getAttribute("soCode")) + ",操作成功!");
                return;
            }else if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12 + "").equals(orderType)) {
            	String remark2 = CommonUtils.checkNull(request.getParamValue("REMARK2")); //审核时备注
            	//销售采购订单
            	//1、生成总部采购单 2、采购单手工转换成验收单 3、手工入库 （入库判断订单类型，入库完成时生成销售单(销售单只能卖给订货单位)）
            	//5、然后走销售流程：销售单-拣货单-装箱-发运计划-出库-发运
            	//更新订单发运方式
                String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE")); //发运方式
                TtPartDlrOrderMainPO po1=new TtPartDlrOrderMainPO();
                po1.setOrderId(Long.valueOf(orderId));
                TtPartDlrOrderMainPO updPo=new TtPartDlrOrderMainPO();
                updPo.setTransType(transType);
                dao.update(po1,updPo);
            	//1、生成总部采购单
            	List ins = new ArrayList();
                ins.add(0, Long.valueOf(orderId));
                ins.add(1, loginUser.getUserId());//制单人
                ins.add(2, remark2);//备注
                dao.callProcedure("PROC_TT_PART_SALE_ORDER", ins, null);
                //更新订单状态
                this.changeOrderStatus(orderType,loginUser, orderId, act, request);
                
            	act.setOutData("success", "销售采购订单" + CommonUtils.checkNull(request.getAttribute("soCode")) + ",操作成功!");
                return;
            }else{
            	//紧急订单、常规订单、促销订单  流程相同
                //插入主数据，销售订单明细表
                this.insertOrderDetail(orderId,orderType,discount,whId,dealerId);
                //插入明细数据，销售单主表
                this.insertSoMain(orderId,orderCode,discount,whId);
                //更新订单状态
                this.changeOrderStatus(orderType,loginUser, orderId, act, request);
                //存在bo单时则插入bo单主表、明细表
                this.createBoOrder(orderId);
                
                //账户增加内部领用订单判断  众泰无领用单
//                if (!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09 + "") && !"".equals(CommonUtils.checkNull(mainMap.get("ACCOUNT_SUM"))) && !"".equals(CommonUtils.checkNull(mainMap.get("ACCOUNT_KY"))) && !"".equals(CommonUtils.checkNull(mainMap.get("ACCOUNT_DJ")))) {
//                    insertAccount();
//                }
//                //判断是否订单已经生成销售单，防止重复生成
//                orderDubCheck(orderId, dao, act);
                //资源锁定 start 资源占用(TT_PART_BOOK_DTL,占用明细表)
                List ins = new ArrayList();
                ins.add(0, Long.valueOf(request.getAttribute("soId").toString()));
                ins.add(1, Constant.PART_CODE_RELATION_07);
                ins.add(2, 1);
                dao.callProcedure("PROC_TT_PART_UPDATE_PART_STATE", ins, null);
                //资源锁定 end
                act.setOutData("boStr",  request.getAttribute("boStr"));//bo单标志，1表示存在bo单
            }

            act.setOutData("orderId", orderId);
            act.setOutData("orderCode", orderCode);
            act.setOutData("success", "销售单号：" + CommonUtils.checkNull(request.getAttribute("soCode")) + ",操作成功!");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成销售单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 总部销售单-插入销售订单明细表、主表
     * @param orderId订单id
     * @param orderCode订单编码
     * @param orderType订单类型
     * @param discount折扣
     * @param whId库房
     * @param dealerId订货单位ID
     * @throws Exception
     */
    private void insertOrderInfo(String orderId,String orderCode,String orderType,Double discount,
    		String whId,String dealerId) throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartPlanConfirmDao partPlanConfirmDao = PartPlanConfirmDao.getInstance();
        try {
            //折扣后的金额
            Double reCountMoney = 0d;
            //销售单id
            Long soId = Long.parseLong(SequenceManager.getSequence(""));
            request.setAttribute("soId", soId);
            //获取当前用户机构ID
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            //机构id
            request.setAttribute("orgId", orgId);
            //订单明细list
            List<TtPartSoDtlPO> list = new ArrayList<TtPartSoDtlPO>();
            //查询订单明细
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail4SODTL(orderId);
            //-------------------------------------插入销售订单明细数据-------------------------------------
            //循环处理明细
            String[] partIdArr = request.getParamValues("cb");//前端传入值
            System.out.println("******审核-配件总条数-前端传入:"+partIdArr.length);
            System.out.println("******审核-配件总条数-数据库查询:"+detailList.size());
            
            for (Map<String, Object> map : detailList) {
                for (int i = 0; i < partIdArr.length; i++) {
                	String partId = partIdArr[i];
                    if (partId.equals(map.get("PART_ID").toString())) {//如果前端传入选中的partid等于明细里面查询的partid
                        Double price = parseDouble(map.get("BUY_PRICE"));//销售单价
                        Double saleQty = Double.valueOf(request.getParamValue("saleQty_" + partId));//前台销售数量
                        Double realPrice = Arith.mul(price, discount);//折扣后单价
                        Double amount = Arith.mul(realPrice, saleQty);//行金额
                        reCountMoney = Arith.add(reCountMoney, amount);//折扣后行金额
                        
                        String partCode = CommonUtils.checkNull(map.get("PART_CODE"));
                        String partOldCode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        String partCname = CommonUtils.checkNull(map.get("PART_CNAME"));
                        String unit = CommonUtils.checkNull(map.get("UNIT"));
                        String isDirect = CommonUtils.checkNull(map.get("IS_DIRECT"));
                        String isPlan = CommonUtils.checkNull(map.get("IS_PLAN"));
                        String isLack = CommonUtils.checkNull(map.get("IS_LACK"));
                        String isReplaced = CommonUtils.checkNull(map.get("IS_REPLACED"));
                        String stockQty = CommonUtils.checkNull(map.get("STOCK_QTY"));
                        String minPackage = CommonUtils.checkNull(map.get("MIN_PACKAGE"));
                        String isHava = CommonUtils.checkNull(map.get("IS_HAVA"));
                        String remark = CommonUtils.checkNull(map.get("REMARK"));
                        String isGift = CommonUtils.checkNull(map.get("IS_GIFT"));
                        String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID_" + partId));
                        //主销售单
                        TtPartSoDtlPO po = new TtPartSoDtlPO();
                        po.setSoId(soId);
                        po.setSlineId(Long.parseLong((i + 1 + soId) + ""));
                        po.setOrderId(Long.valueOf(orderId));
                        po.setPartId(Long.valueOf(partId));
                        po.setPartOldcode(partOldCode);
                        po.setPartCode(partCode);
                        po.setPartCname(partCname);
                        po.setUnit(unit);
                        if (!"".equals(venderId)) {
                            po.setVenderId(Long.valueOf(venderId));
                            TtPartMakerDefinePO venderList = partPlanConfirmDao.getMaker(venderId);//部品制造商
                            if (null != venderList) {
                                po.setVenderName(CommonUtils.checkNull(venderList.getMakerName()));
                                po.setVenderCode(CommonUtils.checkNull(venderList.getMakerCode()));
                            }
                        }
                        if (!"".equals(isDirect)) {
                            po.setIsDirect(Integer.valueOf(isDirect));//直发件，如机油
                        }
                        if (!"".equals(isPlan)) {
                            po.setIsPlan(Integer.valueOf(isPlan));//大件、占空间（如保险杠）
                        }
                        if (!"".equals(isLack)) {
                            po.setIsLack(Integer.valueOf(isLack));//紧缺件
                        }
                        po.setIsReplaced(Constant.IF_TYPE_NO);//是否替换件
                        po.setIsGift(Constant.IF_TYPE_NO);//是否赠品
                        po.setStockQty(Long.valueOf(stockQty));//当前库存
                        po.setMinPackage(Long.valueOf(minPackage));//最小包装量
                        po.setBuyQty(Long.valueOf(CommonUtils.checkNull(map.get("BUY_QTY"))));//订货数量
                        po.setSalesQty(Long.valueOf(request.getParamValue("saleQty_" + partId)));//销售数量
                        po.setBuyPrice(price);//销售单价
                        po.setBuyAmount(amount);//销售金额
                        po.setRemark(remark);
                        po.setCreateBy(loginUser.getUserId());
                        po.setCreateDate(new Date());
                        
                        po.setSalesQty(po.getSalesQty());
//                        if (po.getSalesQty() == 0) {
//                            continue;
//                        }
                        //单价和金额重新校验
                        if (po.getBuyPrice() == 0d || po.getBuyAmount() == 0d) {
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + po.getPartOldcode() + "】销售数据出现异常,请联系管理员!");
                            throw e1;
                        }
                        list.add(po);
                    } 
                }
            }
            //插入销售订单明细数据
            dao.insert(list);
            request.setAttribute("reCountMoney", reCountMoney);//折扣后金额
            //-------------------------------------插入销售订单主数据-------------------------------------
            String payType = CommonUtils.checkNull(request.getParamValue("PAY_TYPE")); //付费方式
//            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); //订货单位ID
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); //订货单位CODE
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); //订货单位CODE
            String sellerId = CommonUtils.checkNull(request.getParamValue("sellerId")); //销售单位ID
            String sellerCode = CommonUtils.checkNull(request.getParamValue("sellerCode")); //销售单位CODE
            String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName")); //销售单位NAME
            String buyerId = CommonUtils.checkNull(request.getParamValue("buyerId")); //订货人ID
            String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName")); //订货人
            String consignees = CommonUtils.checkNull(request.getParamValue("RCV_ORG")); //接收单位
            String consigneesId = CommonUtils.checkNull(request.getParamValue("RCV_ORGID")); //接收单位ID
            String addr = CommonUtils.checkNull(request.getParamValue("ADDR")); //接收地址
            String addrId = CommonUtils.checkNull(request.getParamValue("ADDR_ID")); //接收地址ID
            String receiver = CommonUtils.checkNull(request.getParamValue("RECEIVER")); //接收人
            String tel = CommonUtils.checkNull(request.getParamValue("TEL")); //接收人电话
            String postCode = CommonUtils.checkNull(request.getParamValue("POST_CODE")); //接收邮编
            String station = CommonUtils.checkNull(request.getParamValue("STATION")); //接收站
            String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE")); //发运方式
            String transpayType = CommonUtils.checkNull(request.getParamValue("transpayType")); //发运付费方式
            Double amount = Double.valueOf(request.getAttribute("reCountMoney").toString()); //获取JAVA重新计算的金额
//            String discount = CommonUtils.checkNull(request.getParamValue("DISCOUNT")); //折扣
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); //订单备注
            String remark2 = CommonUtils.checkNull(request.getParamValue("REMARK2")); //备注
            Double freight = Double.valueOf(CommonUtils.checkNull(request.getParamValue("freight")).replace(",", ""));//运费
            String isLock = CommonUtils.checkNull(request.getParamValue("isLock"));
            //订单编码
            String soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_07, request.getParamValue("dealerId"));
            request.setAttribute("soCode", soCode);
            //如果运费产生改变  占用和释放也需要改变
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            amount = Arith.add(freight, amount);
            //重新放入给资金使用
            request.setAttribute("reCountMoney", amount);
            //销售主订单
            TtPartSoMainPO po = new TtPartSoMainPO();
            po.setSoId(soId);
            po.setSoCode(soCode);
            po.setOrderId(Long.valueOf(orderId));
            po.setOrderCode(orderCode);
            po.setOrderType(Integer.valueOf(orderType));
            po.setIsBatchso(Constant.PART_BASE_FLAG_NO);//默认否铺货
            po.setSoFrom(Constant.CAR_FACTORY_SO_FORM_02);//订单生成
            po.setPayType(Integer.valueOf(payType));//支付方式，目前只有现金
            po.setDealerId(Long.valueOf(dealerId));//订货单位ID
            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setSellerId(Long.valueOf(sellerId));//销售单位ID
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            po.setSaleDate(new Date());//审核日期
            if (buyerId != null && !"".equals(buyerId)) {
                po.setBuyerId(Long.valueOf(buyerId));//订货人ID
            }
            po.setBuyerName(buyerName);
            po.setConsigneesId(Long.valueOf(consigneesId));//接收单位ID
            po.setConsignees(consignees);//接收单位
            po.setAddr(addr);//接收地址
            po.setAddrId(Long.valueOf(addrId));//接收地址ID
            po.setReceiver(receiver);//接收人
            po.setTel(tel);//接收人电话
            po.setPostCode(postCode);//邮政编码
            po.setStation(station);//到站名称
            po.setTransType(transType);//发运方式
            po.setTranspayType(Integer.valueOf(transpayType));//运费支付方式
            po.setAmount(amount);//销售金额
            po.setDiscount(discount);//折扣率
            po.setRemark(remark);//订单备注
            po.setRemark2(remark2);//备注
            po.setWhId(Long.valueOf(whId));//仓库ID
            po.setVer(1);//版本
            po.setState(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05);//生成销售单状态直接为财务审核通过
            po.setFcauditDate(new Date());//财务审核日期
            po.setSubmitDate(new Date());
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            if (isLock != null && isLock.equals("1")) {
                po.setLockFreight(1);//运费锁定
            }
            if (freight != null) {
                po.setFreight(freight);//运费
            }
            //是否免运费
            if (freight != null && freight > 0) {
                po.setIsTransfree(Constant.IF_TYPE_NO);
            } else {
                po.setIsTransfree(Constant.IF_TYPE_YES);
            }
            po.setAccountId(Long.valueOf(mainMap.get("ACCOUNT_ID").toString()));//账户ID
            po.setCheckId(Long.valueOf(orderId));//校验ID(唯一不允许重复)
            //插入销售订单主表数据
            dao.insert(po);
            //-----------------------------------------------------------------------------------------------------------
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    

    
    /**
     * 订单重复生成校验
     *
     * @param orderId 订单ID
     * @param dao
     * @param act
     * @throws BizException
     */
    public void orderDubCheck(String orderId, PartDlrOrderDao dao, ActionContext act) throws BizException {
        TtPartSoMainPO soMainPO1 = new TtPartSoMainPO();
        soMainPO1.setOrderId(Long.valueOf(orderId));
        if (dao.select(soMainPO1).size() > 1) {
            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "该订单已经生成销售不能重复生成!");
            throw e1;
        }

    }

    /**
     * 显示订单产生的bo信息
     */
    public void showPartBoInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_BO_QUERY);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "bo信息查询失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 显示订单产生的bo信息
     */
    public void showPartBo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryPartBo(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "bo信息查询失败,请联系管理员!");
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
     * @Description:生成发运计划
     */
    public void createPlanOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            //订单id
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            Long allBuyQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("allBuyQty")));//所有订货数量
            //插入数据
            this.insertPlanMain(orderId);
            this.insertPlanDtl(orderId);

            //判断当前订单是否已经全部审核完成,如果全部完成就更新状态为已审核
            Long allReportedQty = dao.getReportedQty(orderId);
            if (allReportedQty.equals(allBuyQty)) {
                TtPartDlrOrderMainPO spo = new TtPartDlrOrderMainPO();
                TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
                spo.setOrderId(CommonUtils.parseLong(orderId));
                po.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03);
                dao.update(spo, po);
            }
            act.setOutData("success", "发运计划单生成成功!");
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成发运计划错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @return
     * @throws Exception
     */

    public boolean doReplace() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        boolean rtnFlag = false;
        try {
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id"));
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); //订货单位ID
            String sellerId = CommonUtils.checkNull(request.getParamValue("sellerId")); //销售单位ID
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            Map<String, Object> accountMap = dao.getAccount(dealerId, sellerId, "");
            //非主机厂不做设变,没帐户不做设变
            if (!sellerId.equals(Constant.OEM_ACTIVITIES.toString()) || accountMap == null || accountMap.isEmpty() || null == accountMap.get("ACCOUNT_KY")) {
                return false;
            }
            Long soId = Long.valueOf(request.getAttribute("soId").toString());
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            if (request.getAttribute("boMap") != null && !((Map) request.getAttribute("boMap")).isEmpty()) {
                Map<String, Object> boMap = (Map<String, Object>) request.getAttribute("boMap");
                List<TtPartBoDtlPO> detailVoList = (List<TtPartBoDtlPO>) boMap.get("detailVoList");
                List<TtPartBoDtlPO> removeList = new ArrayList<TtPartBoDtlPO>();
                Double amount = 0D;
                Double reCountMoney = Double.valueOf(request.getAttribute("reCountMoney").toString()); //获取JAVA重新计算的金额
                Double reTotalAmout = 0D;
                for (TtPartBoDtlPO po : detailVoList) {
                    Long partId = po.getPartId();
                    List<Map<String, Object>> list = dao.getReplacePart(partId + "");
                    if (list == null || list.size() <= 0) {
                        continue;
                    }
                    Long boQty = po.getBoQty();
                    for (Map<String, Object> map : list) {
                        Long replaceQty = 0L;
                        String replacePartId = CommonUtils.checkNull(map.get("REPART_ID"));
                        //库存最大满足
                        Map<String, Object> stockMap = dao.getPartItemStock(whId, replacePartId + "");
                        if (null == stockMap || stockMap.isEmpty() || null == stockMap.get("NORMAL_QTY")) {
                            continue;
                        }
                        Long stockQty = Long.valueOf(CommonUtils.checkNull(stockMap.get("NORMAL_QTY")));
                        if (stockQty >= boQty) {
                            replaceQty = boQty;
                        } else {
                            replaceQty = stockQty;
                        }
                        //金额开始
                        Double buyPrice = dao.getBuyPrice(dealerId, replacePartId + "");
                        Double orderBuyPrice = dao.getOrderPrice(orderId, partId + "");
                        Double boAmount = Arith.mul(replaceQty, orderBuyPrice);
                        Double kyAmount = Arith.sub(Double.valueOf(CommonUtils.checkNull(accountMap.get("ACCOUNT_KY")).replaceAll(",", "")), reTotalAmout);
                        kyAmount = Arith.add(kyAmount, boAmount);
                        //金额最大满足公式
                        replaceQty = Arith.div(kyAmount, buyPrice) > replaceQty ? replaceQty : Long.valueOf(Math.floor(Arith.div(kyAmount, buyPrice)) + "");
                        Double rpAmount = Arith.mul(buyPrice, replaceQty);
                        reTotalAmout = Arith.add(reTotalAmout, rpAmount);
                        //插入设变件纪录
                        List<Map<String, Object>> partList = dao.getReplacePartInfo(replacePartId + "", dealerId, sellerId);
                        if (null == list || list.size() <= 0 || list.get(0) == null) {
                            continue;
                        }
                        Map<String, Object> partMap = partList.get(0);
                        TtPartSoDtlPO soDtlPo = new TtPartSoDtlPO();
                        Long lineId = Long.parseLong(SequenceManager.getSequence(""));
                        soDtlPo.setSoId(soId);
                        soDtlPo.setSlineId(lineId);
                        soDtlPo.setOrderId(Long.valueOf(orderId));
                        soDtlPo.setPartId(Long.valueOf(replacePartId));
                        soDtlPo.setPartOldcode(CommonUtils.checkNull(partMap.get("PART_OLDCODE")));
                        soDtlPo.setPartCode(CommonUtils.checkNull(partMap.get("PART_CODE")));
                        soDtlPo.setPartCname(CommonUtils.checkNull(partMap.get("PART_CNAME")));
                        soDtlPo.setUnit(partMap.get("UNIT") == null ? "件" : CommonUtils.checkNull(partMap.get("UNIT")));
                        soDtlPo.setIsReplaced(Constant.IF_TYPE_YES);
                        soDtlPo.setIsGift(Constant.IF_TYPE_NO);
                        soDtlPo.setStockQty(Long.valueOf(stockQty));
                        soDtlPo.setMinPackage(Long.valueOf(CommonUtils.checkNull(partMap.get("MIN_PACKAGE"))));
                        soDtlPo.setBuyQty(replaceQty);
                        soDtlPo.setSalesQty(replaceQty);
                        soDtlPo.setBuyPrice(buyPrice);
                        soDtlPo.setBuyAmount(rpAmount);
                        soDtlPo.setRemark("设变件");
                        soDtlPo.setCreateBy(loginUser.getUserId());
                        soDtlPo.setCreateDate(new Date());
                        dao.insert(soDtlPo);
                        rtnFlag = true;
                        if (replaceQty == boQty) {
                            removeList.add(po);

                        } else {
                            po.setBoQty(boQty - replaceQty);
                            po.setBoOddqty(boQty - replaceQty);
                        }

                    }
                }
                reCountMoney = Arith.add(reCountMoney, reTotalAmout);
                request.setAttribute("reCountMoney", reCountMoney);
                ((List<TtPartBoDtlPO>) boMap.get("detailVoList")).removeAll(removeList);
            }
        } catch (Exception e) {
            throw e;
        }
        return rtnFlag;
    }

    private boolean validateChangeState() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {

            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            List<Map<String, Object>> list = dao.queryPartDlrOrderDetail(orderId);
            List<Map<String, Object>> soMainList = dao.getSoMain(orderId);
            //如果没做过 销售单越过
            if (null == soMainList) {
                if (soMainList.size() > 0) {
                    String soIds = "";
                    for (Map<String, Object> map : soMainList) {
                        String soId = CommonUtils.checkNull(map.get("SO_ID"));
                        soIds += "," + soId;
                    }
                    if (!"".equals(soIds)) {
                        soIds = soIds.replaceFirst(",", "");
                    }
                    //做过销售单
                    for (Map<String, Object> map : list) {
                        String partId = CommonUtils.checkNull(map.get("PART_ID"));
                        Long saledQty = dao.getSoDetailPartNum(soIds, partId);
                        Long buyQty = Long.valueOf(CommonUtils.checkNull(map.get("BUY_QTY")));
                        if (buyQty > saledQty) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void changeOrderStatus(String orderType,AclUserBean loginUser, String orderId, ActionContext act, RequestWrapper request) throws Exception {
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {

            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id"));
            newPo.setOrderId(Long.valueOf(orderId));
            newPo.setWhId(Long.valueOf(whId));
            TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
            mainPO.setOrderId(Long.valueOf(orderId));
            mainPO = (TtPartDlrOrderMainPO) dao.select(mainPO).get(0);
            mainPO.setWhId(Long.valueOf(whId));
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setOrderId(Long.valueOf(orderId));
            //TtPartSoMainPO po = new TtPartSoMainPO();
            /*if(mainPO.getState().equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06)){//如果订单状态为车厂已审核,那就是调拨,此时销售单状态直接更新为财务审核通过
                //po.setState(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05);
				po.setSubmitDate(new Date());
				dao.update(soMainPO, po);
				PartSoManage partSoManage = new PartSoManage();
				partSoManage.insertHistory(request, act,Constant.CAR_FACTORY_ORDER_CHECK_STATE_05);
			}*//*else{*/
            newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03);
            newPo.setAuditBy(loginUser.getUserId());
            newPo.setAuditDate(new Date());
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));
            dao.update(oldPo, newPo);
            this.saveHistory(orderType,request, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03);
            /*}*/
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 插入销售订单主表
     * @param orderId 订单id
     * @param orderCode 订单编码
     * @param discount 折扣率
     * @param whId 仓库
     * @throws Exception
     */
    private void insertSoMain(String orderId,String orderCode,Double discount,String whId) throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Long soId = Long.valueOf(request.getAttribute("soId").toString());
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); //订单类型
            String payType = CommonUtils.checkNull(request.getParamValue("PAY_TYPE")); //付费方式
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); //订货单位ID
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); //订货单位CODE
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); //订货单位CODE
            String sellerId = CommonUtils.checkNull(request.getParamValue("sellerId")); //销售单位ID
            String sellerCode = CommonUtils.checkNull(request.getParamValue("sellerCode")); //销售单位CODE
            String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName")); //销售单位NAME
            String buyerId = CommonUtils.checkNull(request.getParamValue("buyerId")); //订货人ID
            String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName")); //订货人
            String consignees = CommonUtils.checkNull(request.getParamValue("RCV_ORG")); //接收单位
            String consigneesId = CommonUtils.checkNull(request.getParamValue("RCV_ORGID")); //接收单位ID
            String addr = CommonUtils.checkNull(request.getParamValue("ADDR")); //接收地址
            String addrId = CommonUtils.checkNull(request.getParamValue("ADDR_ID")); //接收地址ID
            String receiver = CommonUtils.checkNull(request.getParamValue("RECEIVER")); //接收人
            String tel = CommonUtils.checkNull(request.getParamValue("TEL")); //接收人电话
            String postCode = CommonUtils.checkNull(request.getParamValue("POST_CODE")); //接收邮编
            String station = CommonUtils.checkNull(request.getParamValue("STATION")); //接收站
            String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE")); //发运方式
            String transpayType = CommonUtils.checkNull(request.getParamValue("transpayType")); //发运付费方式
            Double amount = Double.valueOf(request.getAttribute("reCountMoney").toString()); //获取JAVA重新计算的金额
//            String discount = CommonUtils.checkNull(request.getParamValue("DISCOUNT")); //折扣
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); //订单备注
            String remark2 = CommonUtils.checkNull(request.getParamValue("REMARK2")); //备注
            Double freight = Double.valueOf(CommonUtils.checkNull(request.getParamValue("freight")).replace(",", ""));//运费
            String isLock = CommonUtils.checkNull(request.getParamValue("isLock"));
            
            //mod by yuan 20131019新增内部领用单判断  众泰无内部领用单
//            if (loginUser.getDealerId() == null) {
//                if (!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09.toString())) {
//                    soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_07, request.getParamValue("dealerId"));
//                } else {
//                    soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_38, request.getParamValue("dealerId"));
//                }
//            } else {
//                soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_11, request.getParamValue("dealerId"));
//            }
            //订单编码
            String soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_07, request.getParamValue("dealerId"));
            request.setAttribute("soCode", soCode);
            //如果运费产生改变  占用和释放也需要改变
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            amount = Arith.add(freight, amount);
            //重新放入给资金使用
            request.setAttribute("reCountMoney", amount);

            //销售主订单
            TtPartSoMainPO po = new TtPartSoMainPO();
            po.setSoId(soId);
            po.setSoCode(soCode);
            po.setOrderId(Long.valueOf(orderId));
            po.setOrderCode(orderCode);
            po.setOrderType(Integer.valueOf(orderType));
            po.setIsBatchso(Constant.PART_BASE_FLAG_NO);//默认否铺货
            po.setSoFrom(Constant.CAR_FACTORY_SO_FORM_02);//订单生成
            po.setPayType(Integer.valueOf(payType));//支付方式，目前只有现金
            po.setDealerId(Long.valueOf(dealerId));//订货单位ID
            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setSellerId(Long.valueOf(sellerId));//销售单位ID
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            po.setSaleDate(new Date());//审核日期
            if (buyerId != null && !"".equals(buyerId)) {
                po.setBuyerId(Long.valueOf(buyerId));//订货人ID
            }
            po.setBuyerName(buyerName);
            po.setConsigneesId(Long.valueOf(consigneesId));//接收单位ID
            po.setConsignees(consignees);//接收单位
            po.setAddr(addr);//接收地址
            po.setAddrId(Long.valueOf(addrId));//接收地址ID
            po.setReceiver(receiver);//接收人
            po.setTel(tel);//接收人电话
            po.setPostCode(postCode);//邮政编码
            po.setStation(station);//到站名称
            po.setTransType(transType);//发运方式
            po.setTranspayType(Integer.valueOf(transpayType));//运费支付方式
            po.setAmount(amount);//销售金额
            po.setDiscount(discount);//折扣率
            po.setRemark(remark);//订单备注
            po.setRemark2(remark2);//备注
            po.setWhId(Long.valueOf(whId));//仓库ID
            po.setVer(1);//版本
            po.setState(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05);//生成销售单状态直接为财务审核通过
            po.setFcauditDate(new Date());//财务审核日期
            po.setSubmitDate(new Date());
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            if (isLock != null && isLock.equals("1")) {
                po.setLockFreight(1);//运费锁定
            }
            if (freight != null) {
                po.setFreight(freight);//运费
            }
          //是否免运费
            if (freight != null && freight > 0) {
                po.setIsTransfree(Constant.IF_TYPE_NO);
            } else {
                po.setIsTransfree(Constant.IF_TYPE_YES);
            }
            po.setAccountId(Long.valueOf(mainMap.get("ACCOUNT_ID").toString()));//账户ID
            po.setCheckId(Long.valueOf(orderId));//校验ID(唯一不允许重复)
            //插入销售订单主表数据
            dao.insert(po);
            
        } catch (Exception ex) {
            throw ex;
        }
    }


    private void insertPlanMain(String orderId) throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

            Long planId = CommonUtils.parseLong(SequenceManager.getSequence(""));
            request.setAttribute("planId", planId);
            //发运计划单号
            String planCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_40, request.getParamValue("dealerId"));

            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); //订单CODE
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); //订货单位ID
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); //订货单位CODE
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); //订货单位名称
            String sellerId = CommonUtils.checkNull(request.getParamValue("sellerId")); //销售单位ID
            String sellerCode = CommonUtils.checkNull(request.getParamValue("sellerCode")); //销售单位CODE
            String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName")); //销售单位NAME
            String remark2 = CommonUtils.checkNull(request.getParamValue("REMARK2")); //备注
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id")); //出库仓库
            String outType = CommonUtils.checkNull(request.getParamValue("OUT_TYPE")); //发运方式
            String finalDate = CommonUtils.checkNull(request.getParamValue("planDate")); //随车最终发运日期

            TtPartDplanMainPO po = new TtPartDplanMainPO();
            po.setPlanId(planId);
            po.setPlanCode(planCode);
            po.setOrderId(CommonUtils.parseLong(orderId));
            po.setOrderCode(orderCode);
            po.setDealerId(CommonUtils.parseLong(dealerId));
            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setSellerId(CommonUtils.parseLong(sellerId));
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            po.setCreateDate(new Date());
            po.setCreateBy(loginUser.getUserId());
            po.setWhId(CommonUtils.parseLong(whId));
            if (!"".equals(outType)) {
                po.setOutType(CommonUtils.parseInteger(outType));
            }
            if (!"".equals(finalDate)) {
                po.setFinalDate(CommonUtils.parseDTime(finalDate));
            }
            po.setRemark(remark2);
            po.setState(Constant.PART_GX_ORDER_STATE_01);//广宣发运计划单状态为已保存

            dao.insert(po);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 插入销售订单详细表
     * @param orderId订单id
     * @param orderType订单类型
     * @param discount折扣
     * @param whId库房
     * @param dealerId订货单位ID
     * @throws Exception
     */
    private void insertOrderDetail(String orderId,String orderType,Double discount,String whId,String dealerId) throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartPlanConfirmDao partPlanConfirmDao = PartPlanConfirmDao.getInstance();
        try {
            //折扣后的金额
            Double reCountMoney = 0d;
            //销售单id
            Long soId = Long.parseLong(SequenceManager.getSequence(""));
            request.setAttribute("soId", soId);
            //获取当前用户机构ID
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            //机构id
            request.setAttribute("orgId", orgId);
            //bo map对象
            Map<String, Object> boMap = new HashMap();
            //订单明细list
            List<TtPartSoDtlPO> list = new ArrayList<TtPartSoDtlPO>();
            //查询订单明细
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail4SODTL(orderId);
            //循环处理明细
            String[] partIdArr = request.getParamValues("cb");//前端传入值
            for (Map<String, Object> map : detailList) {
                boolean flag = false;
                for (int i = 0; i < partIdArr.length; i++) {
                	String partId = partIdArr[i];
                    if (partId.equals(map.get("PART_ID").toString())) {//如果前端传入选中的partid等于明细里面查询的partid
                        flag = true;
                        request.setAttribute("partMap", map);//销售订单信息
                   
                        Double price = parseDouble(map.get("BUY_PRICE"));//销售单价
                        Double saleQty = Double.valueOf(request.getParamValue("saleQty_" + partId));//前台销售数量
                        Double realPrice = Arith.mul(price, discount);//折扣后单价
                        Double amount = Arith.mul(realPrice, saleQty);//行金额
                        reCountMoney = Arith.add(reCountMoney, amount);//折扣后行金额
                        
                        String partCode = CommonUtils.checkNull(map.get("PART_CODE"));
                        String partOldCode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        String partCname = CommonUtils.checkNull(map.get("PART_CNAME"));
                        String unit = CommonUtils.checkNull(map.get("UNIT"));
                        String isDirect = CommonUtils.checkNull(map.get("IS_DIRECT"));
                        String isPlan = CommonUtils.checkNull(map.get("IS_PLAN"));
                        String isLack = CommonUtils.checkNull(map.get("IS_LACK"));
                        String isReplaced = CommonUtils.checkNull(map.get("IS_REPLACED"));
                        String stockQty = CommonUtils.checkNull(map.get("STOCK_QTY"));
                        String minPackage = CommonUtils.checkNull(map.get("MIN_PACKAGE"));
                        String isHava = CommonUtils.checkNull(map.get("IS_HAVA"));
                        String remark = CommonUtils.checkNull(map.get("REMARK"));
                        String isGift = CommonUtils.checkNull(map.get("IS_GIFT"));
                        String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID_" + partId));

                        //主销售单
                        TtPartSoDtlPO po = new TtPartSoDtlPO();
                        po.setSoId(soId);
                        po.setSlineId(Long.parseLong((i + 1 + soId) + ""));
                        po.setOrderId(Long.valueOf(orderId));
                        po.setPartId(Long.valueOf(partId));
                        po.setPartOldcode(partOldCode);
                        po.setPartCode(partCode);
                        po.setPartCname(partCname);
                        po.setUnit(unit);
                        if (!"".equals(venderId)) {
                            po.setVenderId(Long.valueOf(venderId));
                            TtPartMakerDefinePO venderList = partPlanConfirmDao.getMaker(venderId);
                            if (null != venderList) {
                                po.setVenderName(CommonUtils.checkNull(venderList.getMakerName()));
                                po.setVenderCode(CommonUtils.checkNull(venderList.getMakerCode()));
                            }
                        }
                        if (!"".equals(isDirect)) {
                            po.setIsDirect(Integer.valueOf(isDirect));//直发件，如机油
                        }
                        if (!"".equals(isPlan)) {
                            po.setIsPlan(Integer.valueOf(isPlan));//大件、占空间（如保险杠）
                        }
                        if (!"".equals(isLack)) {
                            po.setIsLack(Integer.valueOf(isLack));//紧缺件
                        }

                        po.setIsReplaced(Constant.IF_TYPE_NO);//是否替换件
                        po.setIsGift(Constant.IF_TYPE_NO);//是否赠品
                        po.setStockQty(Long.valueOf(stockQty));//当前库存
                        po.setMinPackage(Long.valueOf(minPackage));//最小包装量
                        po.setBuyQty(Long.valueOf(CommonUtils.checkNull(map.get("BUY_QTY"))));//订货数量
                        po.setSalesQty(Long.valueOf(request.getParamValue("saleQty_" + partId)));//销售数量
                        po.setBuyPrice(price);//销售单价
                        po.setBuyAmount(amount);//销售金额
                        po.setRemark(remark);
                        po.setCreateBy(loginUser.getUserId());
                        po.setCreateDate(new Date());
                        
                        //销售数量小于订货数量则产生一般BO
                        if (po.getSalesQty() < Long.valueOf(map.get("BUY_QTY").toString())) {
                        	//bo单
                            loadBoVo(boMap, price,orderId,orderType);
                            request.setAttribute("boStr", "1");
                        }
                        if (po.getSalesQty() == 0) {
                            continue;
                        }

                        if (!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")
                        	&& !orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12 + "")) {
                            //重新校验库存
                            Map<String, Object> stockMap = dao.getPartItemStock(whId, partId);
                            if (stockMap == null) {
                                BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件[" + partOldCode + "}库存不足!");
                                throw e;
                            }
                            String stockTty = CommonUtils.checkNull(stockMap.get("NORMAL_QTY"));
                            if (Long.valueOf(stockTty) < po.getSalesQty()) {
                                BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件[" + partOldCode + "]库存不足！当前可用库存为:" + stockTty + "，请刷新页面后重新确认！");
                                throw e;
                            }
                        }
                        //校验是否有多人操作 导致数量过多
                        /*if (!validatePlanSaleOrder(orderId, partId, po.getSalesQty(), po.getBuyQty())) {
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "销售数量不得大于订购数量!");
                            throw e1;
                        }*/
                        
                        //单价和金额重新校验
                        if (po.getBuyPrice() == 0d || po.getBuyAmount() == 0d) {
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + po.getPartOldcode() + "】销售数据出现异常,请联系管理员!");
                            throw e1;
                        }
                        list.add(po);
                    } else if (i == (partIdArr.length - 1) && !flag) {
                        //前端没选择配件 的全部生成BO
                        request.setAttribute("partMap", map);
                        //销售单价
                        Double price = parseDouble(map.get("BUY_PRICE"));
                        price = Arith.mul(price, discount);
                        loadBoVo(boMap, price,orderId,orderType);//生成bo单
                        request.setAttribute("boStr", "1");
                    }
                }
            }
            //判断是否有明细，因前端审核时新增配件功能屏蔽
//            String[] gift = request.getParamValues("gift");//获取前端name等于gift的控件，gift判断是否有明细
//            if (null != gift) {
//                for (int i = 0; i < gift.length; i++) {
//                    //获取数据生成订单明细
//                    Long lineId = Long.parseLong(SequenceManager.getSequence(""));
//                    String partId = gift[i];//配件编码
//                    Double price = parseDouble(0);
//                    Double saleQty = Double.valueOf(request.getParamValue("saleQty_" + partId));//销售数量
//                    Double realPrice = 0D;
//                    Double amount = 0D;
//                    Map<String, Object> partMap = dao.getPartDefine(partId);
//                    partMap.putAll(dao.getPartDefine(partId, dealerId));
//                    String partCode = CommonUtils.checkNull(partMap.get("PART_CODE"));
//                    String partOldCode = CommonUtils.checkNull(partMap.get("PART_OLDCODE"));
//                    String partCname = CommonUtils.checkNull(partMap.get("PART_CNAME"));
//                    String unit = CommonUtils.checkNull(partMap.get("UNIT"));
//                    String isDirect = CommonUtils.checkNull(partMap.get("IS_DIRECT"));
//                    String isPlan = CommonUtils.checkNull(partMap.get("IS_PLAN"));
//                    String isLack = CommonUtils.checkNull(partMap.get("IS_LACK"));
//                    String isReplaced = CommonUtils.checkNull(partMap.get("IS_REPLACED"));
//                    String minPackage = CommonUtils.checkNull(partMap.get("MIN_PACKAGE"));
//                    String isHava = CommonUtils.checkNull(partMap.get("IS_HAVA"));
//                    String remark = "赠品";
//                    String venderId = CommonUtils.checkNull(request.getParamValue("vender_" + partId));
//
//                    TtPartSoDtlPO po = new TtPartSoDtlPO();
//                    po.setSoId(soId);
//                    po.setSlineId(lineId);
//                    po.setOrderId(Long.valueOf(orderId));
//                    po.setPartId(Long.valueOf(partId));
//                    po.setPartOldcode(partOldCode);
//                    po.setPartCode(partCode);
//                    po.setPartCname(partCname);
//                    po.setUnit(unit);
//                    if (!"".equals(venderId)) {
//                        po.setVenderId(Long.valueOf(venderId));
//                        List<Map<String, Object>> venderList = partPlanConfirmDao.getVender(venderId, partId);
//                        if (null != venderList && venderList.size() > 0 && null != venderList.get(0)) {
//                            String venderName = CommonUtils.checkNull(venderList.get(0).get("VENDER_NAME"));
//                            po.setVenderName(venderName);
//                        }
//                    }
//                    if (!"".equals(isDirect)) {
//                        po.setIsDirect(Integer.valueOf(isDirect));
//                    }
//                    if (!"".equals(isPlan)) {
//                        po.setIsPlan(Integer.valueOf(isPlan));
//                    }
//                    if (!"".equals(isLack)) {
//                        po.setIsLack(Integer.valueOf(isLack));
//                    }
//                    po.setIsReplaced(Constant.IF_TYPE_YES);
//                    po.setIsGift(Constant.IF_TYPE_YES);
//                    po.setMinPackage(Long.valueOf(minPackage));
//                    po.setBuyQty(Long.valueOf(request.getParamValue("saleQty_" + partId)));
//                    po.setSalesQty(Long.valueOf(request.getParamValue("saleQty_" + partId)));
//                    //重新校验库存
//                    Map<String, Object> stockMap = dao.getPartItemStock(whId, partId);
//                    if (!CommonUtils.checkNull(request.getParamValue("orderType")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "") && stockMap == null) {
//                        BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "库存不足!");
//                        throw e;
//                    }
//                    String stockTty = CommonUtils.checkNull(stockMap.get("NORMAL_QTY"));
//                    po.setStockQty(Long.valueOf(stockTty));
//                    if (!CommonUtils.checkNull(request.getParamValue("orderType")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "") && Long.valueOf(stockTty) < po.getSalesQty()) {
//                        BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "库存不足!");
//                        throw e;
//                    }
//                    po.setBuyPrice(price);
//                    po.setBuyAmount(amount);
//                    po.setRemark(remark);
//                    po.setCreateBy(loginUser.getUserId());
//                    po.setCreateDate(new Date());
//                    if (po.getSalesQty() == 0) {
//                        continue;
//                    }
//
//                    list.add(po);
//
//                }
//            }
            //插入销售订单明细数
            dao.insert(list);
            
            request.setAttribute("boMap", boMap);//bo单map 存产生bo单的信息
            request.setAttribute("reCountMoney", reCountMoney);
            
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void insertPlanDtl(String orderId) throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

            Long planId = (Long) request.getAttribute("planId");

            String[] partIds = request.getParamValues("cb");//所有配件id
            String outType = CommonUtils.checkNull(request.getParamValue("OUT_TYPE")); //发运方式

            for (int i = 0; i < partIds.length; i++) {
                String partId = partIds[i];

                //验证当前配件的提报数量是否大于订货数量
                Long reportedQty = 0l;//已经提报的数量
                Long buyQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("buyQty_" + partId)));//订货数量
                Long saleQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("saleQty_" + partId)));//提报数量
                String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode_" + partId));
                Map<String, Object> map = dao.getReportedQty(orderId, partId);
                if (map != null) {
                    reportedQty = ((BigDecimal) map.get("REPORTED_QTY")).longValue();
                }
                //如果大于订货数量
                if (reportedQty + saleQty > buyQty) {
                    BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, partOldCode + "的审核数量不能大于" + (buyQty - reportedQty) + "!");
                    throw e;
                }

                Long dLineId = CommonUtils.parseLong(SequenceManager.getSequence(""));
                String partCname = CommonUtils.checkNull(request.getParamValue("partCname_" + partId));
                String partCode = CommonUtils.checkNull(request.getParamValue("partCode_" + partId));
                String unit = CommonUtils.checkNull(request.getParamValue("unit_" + partId));
                String minPackage = CommonUtils.checkNull(request.getParamValue("minPackage_" + partId));
                String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo_" + partId));
                String remark = CommonUtils.checkNull(request.getParamValue("remark_" + partId));

                TtPartDplanDtlPO po = new TtPartDplanDtlPO();
                po.setDlineId(dLineId);
                po.setPlanId(planId);
                po.setPartId(CommonUtils.parseLong(partId));
                po.setPartOldcode(partOldCode);
                po.setPartCname(partCname);
                po.setPartCode(partCode);
                po.setUnit(unit);
                po.setMinPackage(CommonUtils.parseLong(minPackage));
                po.setBuyQty(buyQty);
                po.setReportQty(saleQty);
                po.setPkgNo(pkgNo);
                po.setCreateDate(new Date());
                po.setCreateBy(loginUser.getUserId());
                po.setRemark(remark);
                if (!"".equals(outType)) {
                    po.setTransType(CommonUtils.parseInteger(outType));
                }

                dao.insert(po);
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

//    public boolean validatePlanSaleOrder(String orderId, String partId, Long saleQty, Long buyQty) {
//        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
//        List<Map<String, Object>> soMainList = dao.getSoMain(orderId);
//        //如果没做过 销售单越过
//        if (null == soMainList) {
//            if (soMainList.size() > 0) {
//                String soIds = "";
//                for (Map<String, Object> map : soMainList) {
//                    String soId = CommonUtils.checkNull(map.get("SO_ID"));
//                    soIds += "," + soId;
//                }
//                if (!"".equals(soIds)) {
//                    soIds = soIds.replaceFirst(",", "");
//                }
//                //做过销售单
//                Long saledQty = dao.getSoDetailPartNum(soIds, partId);
//
//                if ((saleQty + saledQty) > buyQty) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    /**
     * 生成BO的集合
     * @param boMap  bomap对象
     * @param price销售价
     * @param orderId订单id
     * @param orderCode订单编码
     */
    private void loadBoVo(Map<String, Object> boMap, Double price,String orderId,String orderCode) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //判断bo单主表VO是否存在
        if (boMap == null || null == boMap.get("mainVo")) {//无主表bo单主表实体类
            Long boId = Long.parseLong(SequenceManager.getSequence(""));
            String boCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_21);//一般BO
            String soId = CommonUtils.checkNull(request.getAttribute("soId"));//销售单明细id
            String remark = CommonUtils.checkNull(request.getParamValue("REMARK"));//订单备注

            //bo单主表po
            TtPartBoMainPO po = new TtPartBoMainPO();
            po.setBoId(boId);
            po.setBoCode(boCode);
            po.setBoType("1");
            po.setSoId(Long.valueOf(soId));
            if (!"".equals(orderId)) {
                po.setOrderId(Long.valueOf(orderId));
            }
            
            po.setOrderCode(orderCode);
            
            if (!"".equals(remark)) {
                po.setRemark(remark);
            }
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            po.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_01);//BO单状态-已保存
            po.setVer(1);
            boMap.put("mainVo", po);
        }
        //判断bo单明细表实体类是否存在
        if (boMap == null || null == boMap.get("detailVoList")) {
            List<TtPartBoDtlPO> list = new ArrayList();
            boMap.put("detailVoList", list);
        }
        //销售明细信息
        Map<String, Object> partMap = (Map) request.getAttribute("partMap");
        //bo单明细id
        Long bolineId = Long.parseLong(SequenceManager.getSequence(""));
        //bo单id
        Long boId = ((TtPartBoMainPO) boMap.get("mainVo")).getBoId();
        String partId = CommonUtils.checkNull(partMap.get("PART_ID"));
        String partCode = CommonUtils.checkNull(partMap.get("PART_CODE"));
        String partOldCode = CommonUtils.checkNull(partMap.get("PART_OLDCODE"));
        String partCname = CommonUtils.checkNull(partMap.get("PART_CNAME"));
        String unit = CommonUtils.checkNull(partMap.get("UNIT"));
        String remark = CommonUtils.checkNull(partMap.get("REMARK"));
        
        //bo单明细表po
        TtPartBoDtlPO po = new TtPartBoDtlPO();
        po.setBolineId(bolineId);
        po.setBoId(boId);
        po.setPartId(Long.valueOf(partId));
        po.setPartCode(partCode);
        po.setPartOldcode(partOldCode);
        po.setBuyPrice(price);//采购单价
        po.setPartCname(partCname);
        po.setUnit(unit);
        po.setBuyQty(Long.valueOf(partMap.get("BUY_QTY") == null ? "0" : partMap.get("BUY_QTY").toString()));//订货数量
        po.setSalesQty(Long.valueOf(request.getParamValue("saleQty_" + partId) == null ? "0" : request.getParamValue("saleQty_" + partId)));//满足数量
        //bo数量=订货数量-满足数量
        Long boQty = Long.valueOf(partMap.get("BUY_QTY").toString()) - po.getSalesQty();
        
        po.setBoQty(boQty);//bo数量
        po.setBoOddqty(boQty);//剩余BO数量
        po.setCreateBy(loginUser.getUserId());
        po.setCreateDate(new Date());
        
        if (!"".equals(remark)) {
            po.setRemark(remark);
        }
        //bo单明细添加到bo单里
        ((List) boMap.get("detailVoList")).add(po);
        
    }

    /**
     * 生成BO单，包括主表、明细表
     * @param orderId 订单id
     * @throws Exception
     */
    private void createBoOrder(String orderId) throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
//        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
//        String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
        //存在BO
        if (request.getAttribute("boMap") != null && !((Map) request.getAttribute("boMap")).isEmpty()) {
            Map<String, Object> boMap = (Map<String, Object>) request.getAttribute("boMap");
            //bo主表
            TtPartBoMainPO ttPartBoMainPO = (TtPartBoMainPO) boMap.get("mainVo");
            dao.insert(ttPartBoMainPO);
            //bo明细表
            List<TtPartBoDtlPO> detailVoList = (List<TtPartBoDtlPO>) boMap.get("detailVoList");
            for (int i = 0; i < detailVoList.size(); i++) {
                dao.insert(detailVoList.get(i));
            }
        }

    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:释放占用的资金
     */
//    private void insertAccount() throws Exception {
//        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//        RequestWrapper request = act.getRequest();
//        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
//        try {
//            //获取参数
//            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
//            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
//            Double orderAmount = Double.valueOf((mainMap.get("ORDER_AMOUNT") + "").replaceAll(",", ""));
//            String accountId = CommonUtils.checkNull(mainMap.get("ACCOUNT_ID"));
//            Double checkAmout = Double.valueOf(request.getAttribute("reCountMoney").toString());
//            if (orderAmount == checkAmout) {
//                return;
//            }
//            //更新预扣信息，关联销售单
//            TtPartAccountRecordPO srcPo = new TtPartAccountRecordPO();
//            srcPo.setSourceId(Long.valueOf(orderId));
//            srcPo.setAccountId(Long.valueOf(accountId));
//
//            TtPartAccountRecordPO updatePo = new TtPartAccountRecordPO();
//            updatePo.setAmount(checkAmout);
//            updatePo.setOrderId(Long.valueOf(request.getAttribute("soId").toString()));
//            updatePo.setOrderCode(CommonUtils.checkNull(request.getAttribute("soCode").toString()));
//
//            dao.update(srcPo, updatePo);
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    private Double parseDouble(Object obj) throws Exception {
        ActionContext act = ActionContext.getContext();
        String str = CommonUtils.checkNull(obj);
        try {
            if (str.indexOf(",") > -1) {
                String[] strArr = str.split("\\,");
                str = "";
                for (int i = 0; i < strArr.length; i++) {
                    str += strArr[i];
                }
            }
            return Double.valueOf(str);
        } catch (Exception ex) {
            BizException e = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "数字转换错误!");
            throw e;
        }
    }

    //后修改发运类型为配置！！！
    public Map<String, Object> getTransMap() throws Exception {
        List<TtPartFixcodeDefinePO> list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
        Map<String, Object> transMap = new HashMap<String, Object>();
        for (TtPartFixcodeDefinePO po : list) {
            //list.fixValue }">${list.fixName
            transMap.put(po.getFixValue(), po.getFixName());
        }
        return transMap;
    }

    /**
     * 提交给车厂
     */
    public void repToFactory() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
        try {
            String dealerId = "";
            String flag = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }

            String sellerId = Constant.OEM_ACTIVITIES;
            String sellerCode = Constant.ORG_ROOT_CODE;
            Map<String, Object> oemMap = dao.getOem(sellerCode, sellerId);
            String sellerName = CommonUtils.checkNull(oemMap.get("COMPANY_NAME"));
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

            //更新DTL中的单价
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);
            Double orderAmount = 0D;
            for (Map<String, Object> map : detailList) {
                String partId = CommonUtils.checkNull(map.get("PART_ID"));
                Double price = Double.valueOf(partSoManageDao.getPrice(dealerId, partId));
                Double discount = Double.valueOf(dao.getDiscount(sellerId));
                Double realPrice = Arith.mul(price, discount);
                Double buyQty = Double.valueOf(CommonUtils.checkNull(map.get("BUY_QTY")));
                Double amount = Arith.mul(buyQty, realPrice);
                orderAmount += amount;
                TtPartDlrOrderDtlPO oldDtlPo = new TtPartDlrOrderDtlPO();
                oldDtlPo.setLineId(Long.valueOf(CommonUtils.checkNull(map.get("LINE_ID"))));
                TtPartDlrOrderDtlPO newDtlPo = new TtPartDlrOrderDtlPO();
                newDtlPo.setLineId(Long.valueOf(CommonUtils.checkNull(map.get("LINE_ID"))));
                newDtlPo.setBuyPrice(realPrice);
                newDtlPo.setBuyAmount(amount);
                dao.update(oldDtlPo, newDtlPo);
            }
            TmDealerPO dealerPO = new TmDealerPO();
            dealerPO.setDealerId(Long.valueOf(loginUser.getDealerId()));

            //更新MAIN
            //替换订货单位为供应中心，同时替换销售单位为主机厂
            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            //newPo.setOrderId(Long.valueOf(orderId));
            newPo.setSellerId(Long.valueOf(sellerId)); //oemID
            newPo.setSellerCode(sellerCode);
            newPo.setSellerName(sellerName);
            newPo.setOrderAmount(orderAmount);
            newPo.setDealerId(Long.valueOf(loginUser.getDealerId()));//供应中心ID
            newPo.setDealerCode(loginUser.getDealerCode());
            newPo.setDealerName(((TmDealerPO) dao.select(dealerPO).get(0)).getDealerName());
            newPo.setIsSuborder(Constant.IF_TYPE_YES);//下级订单标志
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));
            dao.update(oldPo, newPo);


            //直发占用资金
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            String orderCode = CommonUtils.checkNull(mainMap.get("ORDER_CODE"));

            //获取账户余额等
            Map<String, Object> accountMap = dao.getAccount(dealerId, sellerId, "");
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            if (null != accountMap) {
                try {
                    //校验金额
                    if (orderAmount > Double.valueOf(CommonUtils.checkNull(accountMap.get("ACCOUNT_KY")).replace(",", ""))) {
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "账户金额不足!");
                        throw e1;
                    }

                } catch (Exception ex) {
                    BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "账户金额不足!!");
                    throw e1;
                }
            } else {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "账户错误,请联系管理员!");
                throw e1;
            }

            po.setAccountId(Long.valueOf(accountMap.get("ACCOUNT_ID").toString()));
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            po.setRecordId(recordId);
            po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01);
            po.setDealerId(Long.valueOf(dealerId));
            po.setAmount(orderAmount);
            po.setFunctionName("配件提报预占");
            po.setSourceId(Long.valueOf(orderId));
            po.setSourceCode(orderCode);
            po.setOrderId(Long.valueOf(orderId));
            po.setOrderCode(orderCode);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            dao.insert(po);

            POContext.endTxn(true);
            act.setOutData("success", "操作成功!");
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成销售单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void getFreight() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));

            if ("".equals(orderType)) {
                orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
            }
            String money = CommonUtils.checkNull(request.getParamValue("money"));
            String freight = dao.getFreight(dealerId, orderType, money);
            act.setOutData("freight", freight);
            act.setOutData("amountCount", money);
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取运费错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }


    /**
     * 订单驳回
     */
    public void rebut() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            //释放预占资金
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String reason = CommonUtils.checkNull(request.getParamValue("reason"));
            reason = URLDecoder.decode(reason, "UTF-8"); //转乱码 add zhumingwei 2014-08-15
            //String reason1 = new String(reason.getBytes("ISO-8859-1"), "UTF-8");

            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            if(mainMap!=null){
	            if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
	                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已改变!请重新查询!");
	                throw e1;
	            }
            }else{
            	BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "未查询到订单信息!请重新查询!");
                throw e1;
            }

            po.setSourceId(Long.valueOf(orderId));

            //modify by yuan 20130921 start
            /*List<PO> list = dao.select(po);
            Double amount = 0D;
			for(PO recordpo:list){
				po = (TtPartAccountRecordPO)recordpo;
				amount =Arith.add(amount,po.getAmount());
			}*/
            /*if(amount!=0D){
                Long recordId = Long.parseLong(SequenceManager.getSequence(""));
				po.setRecordId(recordId);
				po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02);
				po.setFunctionName("驳回释放");
				
				po.setAmount(-(amount));//释放金额为 负数
				po.setCreateBy(loginUser.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}*/
            //end
            //更新订单状态和驳回原因
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderMainPO srcPo = new TtPartDlrOrderMainPO();
            srcPo.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            newPo.setOrderId(Long.valueOf(orderId));
            newPo.setRebutReason(reason);
            newPo.setSubmitDate(null);
            newPo.setSubmitBy(null);

            //增加是否下级订单判断
            oldPo = (TtPartDlrOrderMainPO) dao.select(oldPo).get(0);
            if (oldPo.getIsSuborder().equals(Constant.IF_TYPE_YES)) {//如果是转下级订单，还原采购单位和销售单位
                TmDealerPO dealerPO = new TmDealerPO();
                dealerPO.setDealerId(oldPo.getRcvOrgid());

                newPo.setDealerId(oldPo.getRcvOrgid());
                newPo.setDealerName(oldPo.getRcvOrg());
                newPo.setDealerCode(((TmDealerPO) dao.select(dealerPO).get(0)).getDealerCode());
                newPo.setSellerId(oldPo.getDealerId());
                newPo.setSellerCode(oldPo.getDealerCode());
                newPo.setSellerName(oldPo.getDealerName());
                newPo.setIsSuborder(Constant.IF_TYPE_NO);
                newPo.setUpdateDate(new Date());
                newPo.setUpdateBy(loginUser.getUserId());
            } else {
                newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01);//保存
            }
            po.setDealerId(oldPo.getDealerId());//删除占有资金
            dao.delete(po);
            dao.update(srcPo, newPo);
            dao.rebutOrder(orderId);

            //重新计算订单次数
            ArrayList ins = new ArrayList();
            ins.add(0, orderId);
            dao.callProcedure("PKG_PART_TOOLS.P_DLRORDERNUM", ins, null);

            act.setOutData("success", "驳回成功!");
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "驳回失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER_CHECK_QUERY);
        }
    }

    /**
     * 广宣订单驳回
     */
    public void rebutGxOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        MarketPartDlrOrderDao dao = MarketPartDlrOrderDao.getInstance();
        try {
            //去除预占资金
            //TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String reason = CommonUtils.checkNull(request.getParamValue("reason"));
            reason = URLDecoder.decode(reason, "UTF-8");

            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已改变!请重新查询!");
                throw e1;
            }

            //更新订单状态和驳回原因
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderMainPO srcPo = new TtPartDlrOrderMainPO();
            srcPo.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            newPo.setOrderId(Long.valueOf(orderId));
            newPo.setRebutReason(reason);

            //增加是否下级订单判断
            oldPo = (TtPartDlrOrderMainPO) dao.select(oldPo).get(0);
            if (oldPo.getIsSuborder().equals(Constant.IF_TYPE_YES)) {//如果是转下级订单，还原采购单位和销售单位
                TmDealerPO dealerPO = new TmDealerPO();
                dealerPO.setDealerId(oldPo.getRcvOrgid());

                newPo.setDealerId(oldPo.getRcvOrgid());
                newPo.setDealerName(oldPo.getRcvOrg());
                newPo.setDealerCode(((TmDealerPO) dao.select(dealerPO).get(0)).getDealerCode());
                newPo.setSellerId(oldPo.getDealerId());
                newPo.setSellerCode(oldPo.getDealerCode());
                newPo.setSellerName(oldPo.getDealerName());
                newPo.setIsSuborder(Constant.IF_TYPE_NO);
                newPo.setUpdateDate(new Date());
                newPo.setUpdateBy(loginUser.getUserId());
            } else {
                newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01);//保存
            }
            //资金还原
            StringBuffer sbSql = new StringBuffer();
            List<Object> params = new ArrayList<Object>();
            if (!"".equals(oldPo.getAccountId())) {
                sbSql.append("UPDATE TT_SALES_FIN_ACC T\n");
                sbSql.append("   SET T.AMOUNT        = NVL(T.AMOUNT, 0) + " + oldPo.getOrderAmount() + ",\n");
                sbSql.append("       T.FREEZE_AMOUNT = NVL(T.FREEZE_AMOUNT, 0) - " + oldPo.getOrderAmount() + "\n");
                sbSql.append(" WHERE T.ACC_ID = " + oldPo.getAccountId() + "");
            }
            dao.update(sbSql.toString(), params);
            request.setAttribute("mainAmount", oldPo.getOrderAmount());

            orderFinAccFlow(request, act, oldPo.getDealerId());//财务扣款流水明细
            dao.update(srcPo, newPo);

            //dao.updateOrderNum(Long.valueOf(orderId));//修改订单次数
            //this.saveHistory(request, act, Integer.valueOf(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05));
            act.setOutData("success", "驳回成功!");
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "驳回失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER_CHECK_QUERY);
        }
    }

    /**
     * 财务扣款流水明细
     *
     * @param req
     * @param act
     */
    private void orderFinAccFlow(RequestWrapper req, ActionContext act, long dealerId) {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        MarketPartDlrOrderDao dao = MarketPartDlrOrderDao.getInstance();
        TtSaleFundsRoseDetailPO fundDetailPO = new TtSaleFundsRoseDetailPO();

        String orderCode = CommonUtils.checkNull(req.getAttribute("orderCode"));
        double mainAmount = Double.parseDouble(CommonUtils.checkNull(req.getAttribute("mainAmount")));

        fundDetailPO.setRemark("广宣品订单:" + orderCode + "驳回释放冻结");
        fundDetailPO.setDealerId(dealerId);
        fundDetailPO.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
        fundDetailPO.setAmount(-mainAmount);
        fundDetailPO.setCreateBy(logonUser.getUserId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fundDetailPO.setCreateDate(sdf.format(new Date()));
        fundDetailPO.setFinType(Constant.ACCOUNT_TYPE_01);
        fundDetailPO.setIsType(6);
        dao.insert(fundDetailPO);
        fundDetailPO.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
        fundDetailPO.setRemark("广宣品订单:" + orderCode + "返还可用余额");
        fundDetailPO.setAmount(mainAmount);//扣除可用余额(-)
        fundDetailPO.setIsType(5);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fundDetailPO.setCreateDate(sdf.format(new Date()));
        dao.insert(fundDetailPO);
    }


    public void getGift() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            act.getResponse().setContentType("application/json");
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = dao.getGift(request, curPage);
            //如果数据量大   这样1可以避免赠品数据错误    2循环10次获取比join的快
            if (ps != null && ps.getRecords() != null) {
                List<Map<String, Object>> list = ps.getRecords();
                for (Map<String, Object> map : list) {
                    String partId = CommonUtils.checkNull(map.get("PART_ID"));
                    Map<String, Object> partMap = dao.getPartDefine(partId, CommonUtils.checkNull(request.getParamValue("dealerId")));
                    map.putAll(partMap);
                }
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询赠品失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER_CHECK_QUERY);
        }
    }

    private void saveQueryCondition() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
        String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
        String CstartDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));// 制单日期 开始
        String CendDate = CommonUtils.checkNull(request.getParamValue("CendDate"));// 制单日期 结束
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//提交时间 开始
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//提交时间 结束
        String state = CommonUtils.checkNull(request.getParamValue("state"));//订单状态
        String autoPreCheck = CommonUtils.checkNull(request.getParamValue("autoPreCheck"));//自动预审
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderCode", orderCode);
        map.put("dealerName", dealerName);
        map.put("sellerName", sellerName);
        map.put("state", state);
        map.put("autoPreCheck", autoPreCheck);
        map.put("salerId", salerId);
        map.put("CstartDate", CstartDate);
        map.put("CendDate", CendDate);
        map.put("orderType", orderType);
        map.put("SstartDate", SstartDate);
        map.put("SendDate", SendDate);
        act.getSession().set("condition", map);
    }

    /**
     * 生成bo单，整个订单生成bo单
     * 关闭的action方法
     */
    public void closeOrderAction() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id"));
            //关闭销售单，生成bo单
            closeOrder(orderId, whId);
            //设置查询条件
            saveQueryCondition();
            
            act.setOutData("orderId", orderId);
            act.setOutData("orderCode", orderCode);
            act.setOutData("success", "强制关闭成功!");
            act.setOutData("boStr", "1");
            //act.setForword(PART_DIRECT_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "关闭失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    //关闭的私有方法
    private void closeOrder(String id, String whId) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        String name = "";
        try {
            //更新主订单状态 start
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            
            TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
            recordPO.setSourceId(Long.valueOf(id));
            try {
                oldPo.setOrderId(Long.valueOf(id));
                newPo.setOrderId(Long.valueOf(id));
                newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_07);
                newPo.setWhId(Long.valueOf(whId));
            } catch (Exception e) {
                BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "数据出错!");
                throw e1;
            }
            try {
                dao.update(oldPo, newPo);
                dao.delete(recordPO);//强制关闭时资金预占解除
            } catch (Exception e) {
                BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "主订单状态更新出错!");
                throw e1;
            }
            //更新主订单状态 end

            //生成bo start
            //获取主订单数据
            TtPartDlrOrderMainPO mainPo = new TtPartDlrOrderMainPO();
            List<TtPartDlrOrderMainPO> mainList = new ArrayList<TtPartDlrOrderMainPO>();
            try {
                mainPo.setOrderId(Long.valueOf(id));
                mainList = dao.select(mainPo);
            } catch (Exception e) {
                BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询订单主数据出错!");
                throw e1;
            }
            if (mainList.size() <= 0) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单不存在!");
                throw e1;
            }
            mainPo = mainList.get(0);
            //获取订单明细数据
            TtPartDlrOrderDtlPO dtlPo = new TtPartDlrOrderDtlPO();
            List<TtPartDlrOrderDtlPO> dtlList = new ArrayList<TtPartDlrOrderDtlPO>();
            try {
                dtlPo.setOrderId(Long.valueOf(id));
                dtlList = dao.select(dtlPo);
            } catch (Exception e) {
                BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询订单明细出错!");
                throw e1;
            }
            if (dtlList.size() <= 0) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单不存在配件明细!");
                throw e1;
            }
            //生成bo主表数据
            TtPartBoMainPO ttPartBoMainPO = new TtPartBoMainPO();
            Long boId = Long.parseLong(SequenceManager.getSequence(""));
            String boCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_21);
            try {
                ttPartBoMainPO.setBoId(boId);
                ttPartBoMainPO.setBoType("1");
                ttPartBoMainPO.setBoCode(boCode);
                ttPartBoMainPO.setOrderCode(mainPo.getOrderCode());
                ttPartBoMainPO.setOrderId(Long.valueOf(id));
                ttPartBoMainPO.setCreateBy(loginUser.getUserId());
                ttPartBoMainPO.setCreateDate(new Date());
                ttPartBoMainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_01);
                ttPartBoMainPO.setVer(1);
                ttPartBoMainPO.setRemark(mainPo.getRemark());
                dao.insert(ttPartBoMainPO);
            } catch (Exception e) {
                BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "BO主表数据出错!");
                throw e1;
            }
            //生成bo明细数据
            try {
                for (TtPartDlrOrderDtlPO po : dtlList) {
                    TtPartBoDtlPO ttPartBoDtlPO = new TtPartBoDtlPO();
                    name = po.getPartCname();
                    Long bolineId = Long.parseLong(SequenceManager.getSequence(""));
                    ttPartBoDtlPO.setBolineId(bolineId);
                    ttPartBoDtlPO.setBoId(boId);
                    ttPartBoDtlPO.setPartId(po.getPartId());
                    ttPartBoDtlPO.setPartCode(po.getPartCode());
                    ttPartBoDtlPO.setPartOldcode(po.getPartOldcode());
                    ttPartBoDtlPO.setBuyPrice(po.getBuyPrice());
                    ttPartBoDtlPO.setBuyPrice1(po.getBuyPrice1());
                    ttPartBoDtlPO.setPartCname(po.getPartCname());
                    ttPartBoDtlPO.setUnit(po.getUnit());
                    ttPartBoDtlPO.setBuyQty(po.getBuyQty());
                    ttPartBoDtlPO.setSalesQty(0L);
                    ttPartBoDtlPO.setBoQty(po.getBuyQty());
                    ttPartBoDtlPO.setBoOddqty(po.getBuyQty());
                    ttPartBoDtlPO.setCreateBy(loginUser.getUserId());
                    ttPartBoDtlPO.setCreateDate(new Date());
                    ttPartBoDtlPO.setRemark(po.getRemark());//备注
                    dao.insert(ttPartBoDtlPO);
                }
            } catch (Exception e) {
                String msg = "生成BO明细失败!";
                if (!"".equals(name)) {
                    msg = "生成配件：" + name + "的BO失败!";
                }
                BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, msg);
                throw e1;
            }
            
            
            //
            //更新BO单主表BO单商品总金额
            dao.updateBOMainAmount(boId);
            //获取BO单商品总金额
            TtPartBoMainPO tmpMainPO = new TtPartBoMainPO();
            tmpMainPO.setBoId(boId);
            TtPartBoMainPO tmpMainPO2 = (TtPartBoMainPO) dao.select(tmpMainPO).get(0);
            double amount = tmpMainPO2.getBoAmount();

            //组织数据插入资金占用信息

            //获取账户余额等
            Map<String, Object> acountMap = dao.getAccount(mainPo.getDealerId() + "", mainPo.getSellerId() + "", null);
            Long accountId = null;
            if (null != acountMap) {
                accountId = Long.valueOf(acountMap.get("ACCOUNT_ID").toString());
            }
            Map infoMap = new HashMap();
            infoMap.put("dealerId", mainPo.getDealerId());
            infoMap.put("accountId", accountId);
            infoMap.put("sourceId", ttPartBoMainPO.getOrderId());
            infoMap.put("sourceCode", ttPartBoMainPO.getOrderCode());
            infoMap.put("boId", ttPartBoMainPO.getBoId());
            infoMap.put("boCode", ttPartBoMainPO.getBoCode());
            //infoMap.put("orderId", ttPartBoMainPO.getOrderId());
            //infoMap.put("orderCode", ttPartBoMainPO.getOrderCode());
            infoMap.put("amount", amount);
            String funName = "BO单预占";
            infoMap.put("funName", funName);
            dao.insertAccountNew(loginUser, infoMap);
            
            
        } catch (Exception e) {//异常方法
            POContext.endTxn(false);
            if (e instanceof BizException) {
                throw e;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "关闭失败!");
            throw e1;
        }
    }


    public void addMarketOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String dealerId = "";
            String dealerCode = "";
            String dealerName = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
                dealerCode = beanList.get(0).getOrgCode();
                dealerName = beanList.get(0).getOrgName();
            }
            if ("".equals(dealerId)) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, " </br>此工号没有操作权限,请联系管理员!");
                throw e1;
            }
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            dataMap.put("name", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = dao.getAccount(dealerId, dealerId, "");
            //折扣率
            String discount = dao.getDiscount(dealerId);
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //品牌
            List<Map<String, Object>> brandList = dao.getBrand();
            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(dealerId);

            dataMap.put("now", now);
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerCode", dealerCode);
            dataMap.put("dealerName", dealerName);
            dataMap.put("discount", discount);
            dataMap.put("orderType", orderType);
            dataMap.put("createBy", loginUser.getName());
            act.setOutData("accountMap", accountMap);
            act.setOutData("transList", list);
            act.setOutData("brandList", brandList);

            act.setOutData("dataMap", dataMap);
            act.setOutData("wareHouseList", wareHouseList);
            act.setForword(PART_OEM_GX_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_GXORDER_CHECK_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "订单新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_GXORDER_CHECK_QUERY);
        }
    }

    /**
     * 验收异常初始化
     */
    public void detailDlrOrderEX() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String inId = CommonUtils.checkNull(request.getParamValue("IN_ID"));
            act.setOutData("IN_ID", inId);
            act.setForword(PART_DLR_ORDER_CHECK_DETAIL_EX);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "验收异常查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件销售审核查询
     */
    public void partDlrOrderEXQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String inId = CommonUtils.checkNull(request.getParamValue("IN_ID"));
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryPartDlrOrderEX(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件验收异常查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void updateOrderRemark() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));

            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            TtPartDlrOrderMainPO dlrOrderMainPO = new TtPartDlrOrderMainPO();
            dlrOrderMainPO.setOrderId(Long.valueOf(orderId));

            TtPartDlrOrderMainPO updatePO = new TtPartDlrOrderMainPO();
            updatePO.setRemark(remark);
            dao.update(dlrOrderMainPO, updatePO);

            act.setOutData("success", "修改成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "关闭失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

}
