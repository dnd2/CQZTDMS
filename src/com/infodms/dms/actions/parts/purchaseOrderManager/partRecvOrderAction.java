package com.infodms.dms.actions.parts.purchaseOrderManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partPlannerQueryManager.partPlannerQueryDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderChkDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.partRecvOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPoDtlPO;
import com.infodms.dms.po.TtPartPoMainPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 配件计划领件单业务处理
 * @Date: 2013-7-31
 * @remark
 */
public class partRecvOrderAction extends BaseImport {
    public Logger logger = Logger.getLogger(partRecvOrderAction.class);
    private static final partRecvOrderDao dao = partRecvOrderDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private static final int orderType3 = Constant.PART_PURCHASE_PLAN_TYPE_03;//领件计划
    private static final int orderState1 = Constant.PURCHASE_ORDER_STATE_01;//未完成
    private static final int orderState2 = Constant.PURCHASE_ORDER_STATE_02;//已完成
    private static final int orderState3 = Constant.PURCHASE_ORDER_STATE_03;//已关闭
    private static final int orderState4 = Constant.PURCHASE_ORDER_STATE_04;//已超期

    //配件计划领件单URL
    private static final String PART_RECEIVE_MAIN = "/jsp/parts/purchaseOrderManager/partRecvOrder/recvOrderMain.jsp";//配件领件单首页
    private static final String PART_RESALE_RECCIVE_PRINT = "/jsp/parts/purchaseOrderManager/partRecvOrder/recvPrtPg.jsp";//计划领件单打印页面
    private static final String PART_RECEIVE_ADD = "/jsp/parts/purchaseOrderManager/partRecvOrder/recvOrderAdd.jsp";//配件领件单新增页面
    private static final String PART_RECEIVE_MOD = "/jsp/parts/purchaseOrderManager/partRecvOrder/recvOrderModify.jsp";//配件领件单修改页面
    private static final String PART_RECEIVE_CHK = "/jsp/parts/purchaseOrderManager/partRecvOrder/recvOrderChk.jsp";//配件领件单生成验收页面
    private static final String INPUT_ERROR_URL = "/jsp/parts/purchaseOrderManager/partRecvOrder/inputError.jsp";//数据导入出错页面

    private static final Integer PRINT_SIZE = 10;

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-1
     * @Title : 跳转至配件领件单页面
     */
    public void partRecInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String companyName = ""; //制单单位
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                companyName = dao.getMainCompanyName(parentOrgId);
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
                companyName = dao.getDealerName(parentOrgId);
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            String userAct = "";
            String plannerStr = "计划员";
            List<Map<String, Object>> plannersList = partPlannerQueryDao.getInstance().getUsers(plannerStr, userAct);

            act.setOutData("plannersList", plannersList);
            act.setOutData("currUserId", logonUser.getUserId());
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("companyName", companyName);
            act.setOutData("WHList", WHList);
            act.setForword(PART_RECEIVE_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件领件单页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title : 查询配件计划领件单信息
     */
    public void partRecvOrderSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String searchType = CommonUtils.checkNull(request
                    .getParamValue("searchType")); // 查询类型
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            if (null != searchType && "normal".equals(searchType)) {
                String orderCode = CommonUtils.checkNull(request
                        .getParamValue("orderCode")); // 领件单号
                String buyer = CommonUtils.checkNull(request
                        .getParamValue("buyer")); // 领件人
                String checkSDate = CommonUtils.checkNull(request
                        .getParamValue("checkSDate")); // 开始时间
                String checkEDate = CommonUtils.checkNull(request
                        .getParamValue("checkEDate")); // 截止时间
                String whId = CommonUtils.checkNull(request
                        .getParamValue("whId")); // 出库ID
                String orderState = CommonUtils.checkNull(request
                        .getParamValue("state")); // 制单状态
                String partOldcode = CommonUtils.checkNull(request
                        .getParamValue("partOldcode")); // 配件编码
                String partName = CommonUtils.checkNull(request
                        .getParamValue("partName")); //配件名称
                String partCode = CommonUtils.checkNull(request
                        .getParamValue("partCode")); // 配件件号
                String purOrderCode = CommonUtils.checkNull(request
                        .getParamValue("purOrderCode")); //采购订单号
                String printSDate = CommonUtils.checkNull(request
                        .getParamValue("printSDate")); // 打印开始时间
                String printEDate = CommonUtils.checkNull(request
                        .getParamValue("printEDate")); // 打印截止时间

                StringBuffer sbString = new StringBuffer();
                StringBuffer sbStrN = new StringBuffer();
                if (null != orderCode && !"".equals(orderCode)) {
                    sbString.append(" AND UPPER(A.ORDER_CODE) LIKE '%" + orderCode.trim().toUpperCase() + "%' ");
                }
                if (null != buyer && !"".equals(buyer)) {
                    sbString.append(" AND A.BUYER LIKE '%" + buyer.trim() + "%' ");
                }
                if (null != orderState && !"".equals(orderState)) {
                    sbString.append(" AND A.STATE = '" + orderState + "' ");
                }
                if (null != checkSDate && !"".equals(checkSDate)) {
                    sbString.append(" AND TO_CHAR(A.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
                }
                if (null != checkEDate && !"".equals(checkEDate)) {
                    sbString.append(" AND TO_CHAR(A.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
                }
                if (null != whId && !"".equals(whId)) {
                    sbString.append(" AND A.WH_ID = '" + whId + "' ");
                }
                if (null != purOrderCode && !"".equals(purOrderCode)) {
                    sbString.append(" AND UPPER(A.PUR_ORDER_CODE) LIKE '%" + purOrderCode.trim().toUpperCase() + "%' ");
                }
                if (null != partName && !"".equals(partName)) {
                    sbStrN.append(" AND OD.PART_CNAME LIKE '%" + partName.trim() + "%' ");
                }
                if (null != partOldcode && !"".equals(partOldcode)) {
                    sbStrN.append(" AND UPPER(OD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
                }
                if (null != partCode && !"".equals(partCode)) {
                    sbStrN.append(" AND OD.PART_CODE LIKE '%" + partCode.trim().toUpperCase() + "%' ");
                }
                if (null != printSDate && !"".equals(printSDate)) {
                    sbString.append(" AND TO_CHAR(A.PRINT_DATE,'yyyy-MM-dd') >= '" + printSDate + "' ");
                }
                if (null != printEDate && !"".equals(printEDate)) {
                    sbString.append(" AND TO_CHAR(A.PRINT_DATE,'yyyy-MM-dd') <= '" + printEDate + "' ");
                }

                ps = dao.queryPartRecvOrder(sbString.toString(), sbStrN.toString(), Constant.PAGE_SIZE, curPage);
            } else {
                String orderCode = CommonUtils.checkNull(request
                        .getParamValue("orderCode1")); // 领件单号
                String partOldcode = CommonUtils.checkNull(request
                        .getParamValue("partOldcode1")); // 配件编码
                String partName = CommonUtils.checkNull(request
                        .getParamValue("partName1")); //配件名称

                StringBuffer sbString = new StringBuffer();
                if (null != orderCode && !"".equals(orderCode)) {
                    sbString.append(" AND UPPER(A.ORDER_CODE) LIKE '%" + orderCode.trim().toUpperCase() + "%' ");
                }
                if (null != partOldcode && !"".equals(partOldcode)) {
                    sbString.append(" AND UPPER(OD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
                }
                if (null != partName && !"".equals(partName)) {
                    sbString.append(" AND OD.PART_CNAME LIKE '%" + partName.trim() + "%' ");
                }

                ps = dao.queryPartRecvOrderDtl(
                        sbString.toString(), Constant.PAGE_SIZE, curPage);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件计划领件单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 跳转至配件计划领件单查看页面
     */
    public void viewOrderDeatilInint() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));// 订单ID
            String optionType = CommonUtils.checkNull(request.getParamValue("optionType"));// 操作类型
            String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID

            StringBuffer sbStr = new StringBuffer();
            sbStr.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbStr.toString());

            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND A.ORDER_ID = '" + orderId + "' ");
            Map<String, Object> map = dao.queryPartRecvOrderList(sbString.toString(), "").get(0);

            act.setOutData("map", map);
            act.setOutData("actionURL", actionURL);
            act.setOutData("parentOrgId", parentOrgId);
            if ("check".equalsIgnoreCase(optionType)) {
                List whmans = new ArrayList();
                whmans = PurchaseOrderChkDao.getInstance().queryWhmanInfo();
                act.setOutData("whmans", whmans);

                //生成验收页面
                act.setForword(PART_RECEIVE_CHK);
            } else if ("detail".equalsIgnoreCase(optionType)) {
                List<Map<String, Object>> list = dao.queryPartRecvOrderDtlList(sbString.toString());

                String selVendId = "";
                String selVendName = "";
                if (null != list && list.size() > 0) {
                    selVendId = list.get(0).get("VENDER_ID").toString();
                    selVendName = list.get(0).get("VENDER_NAME").toString();
                }

                act.setOutData("selVendId", selVendId);
                act.setOutData("selVendName", selVendName);
                act.setOutData("WHList", WHList);
                act.setOutData("list", list);
                act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);

                //领件修改页面
                act.setForword(PART_RECEIVE_MOD);
            }
            /*else
			{
				List<Map<String, Object>> list = dao.queryPartRecvOrderDtlList(sbString.toString());
				Date date = new Date();
				DateFormat locDate = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
				String dateStr = locDate.format(date);
				
				act.setOutData("pageType", pageType);
				act.setOutData("currDate",dateStr);
				act.setOutData("list",list);
				//打印页面
				act.setForword(PART_RESALE_RECCIVE_PRINT);
			}*/

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件计划领件单查看页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title :
     * @Description: 查询订单明细
     */
    public void queryPurchaseOrderDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));

            String partOldcode = CommonUtils.checkNull(request
                    .getParamValue("partOldcode")); // 配件编码
            String partName = CommonUtils.checkNull(request
                    .getParamValue("partName")); //配件名称

            StringBuffer sbString = new StringBuffer();
            if (null != orderId && !"".equals(orderId)) {
                sbString.append(" AND UPPER(A.ORDER_ID) = '" + orderId + "' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbString.append(" AND UPPER(OD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }
            if (null != partName && !"".equals(partName)) {
                sbString.append(" AND OD.PART_CNAME LIKE '%" + partName + "%' ");
            }
            if (null != partCode && !"".equals(partCode)) {
                sbString.append(" AND UPPER(OD.PART_CODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
            }

            sbString.append(" AND OD.BUY_QTY - NVL(OD.CHECK_QTY, '0') > 0 ");

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartRecvOrderDtl(sbString.toString(), Constant.PAGE_SIZE, curPage);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "领件订单明细异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-1
     * @Title : 订单打印（单）
     */
    public void opPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> dataMap = new HashMap();
        try {

            String selOrders = CommonUtils.checkNull(request.getParamValue("selOrders"));
            String[] orderIdArr = null;
            Date date = new Date();
            DateFormat locDate = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

            if (null != selOrders && !"".equals(selOrders)) {
                orderIdArr = selOrders.split(",");
            }
            if (null != orderIdArr) {
                String orderId = "";
                TtPartPoMainPO selMPo = null;
                TtPartPoMainPO updMPo = null;
                for (int i = 0; i < orderIdArr.length; i++) {
                    orderId += "'" + orderIdArr[i] + "', ";

                    selMPo = new TtPartPoMainPO();
                    updMPo = new TtPartPoMainPO();

                    selMPo.setOrderId(Long.parseLong(orderIdArr[i]));

                    updMPo.setPrintDate(date);
                    updMPo.setPrintBy(loginUser.getUserId());

                    dao.update(selMPo, updMPo);
                }
                orderId = orderId.substring(0, orderId.length() - 2);
                String str = " AND A.ORDER_ID IN (" + orderId + ")";

//				Map<String,Object> map = dao.queryPartRecvOrderList(str).get(0);
                Map<String, Object> map = new HashMap<String, Object>();

                map.put("CREATE_DATE", locDate.format(date));

                List<Map<String, Object>> detailList = dao.queryPartRecvOrderDtlList(str);
                List allList = new ArrayList();

                if (null != detailList) {
                    map.put("ORDER_CODE", detailList.get(0).get("ORDER_CODE"));
                    map.put("PUR_ORDER_CODE", detailList.get(0).get("PUR_ORDER_CODE"));

                    for (int i = 0; i < detailList.size(); ) {
                        List subList = detailList.subList(i, i + PRINT_SIZE > detailList.size() ? detailList.size() : i + PRINT_SIZE);
                        i = i + PRINT_SIZE;
                        allList.add(subList);
                    }
                }

                act.setOutData("allList", allList);
                act.setOutData("mainMap", map);

                act.setForword(PART_RESALE_RECCIVE_PRINT);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "进货单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title : 跳转至配件计划领件单新增页面
     */
    public void partOrderAddInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
            String parentOrgId = "";//制单单位ID
            String selectedWhId = "";
            String selectedVdId = "";

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                parentOrgId = Constant.OEM_ACTIVITIES;
                StringBuffer sb = new StringBuffer();
                sb.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
                sb.append(" AND TM.WH_NAME LIKE '%景德镇%' ");
                List<Map<String, Object>> whList = dao.getWareHouses(sb.toString());
                if (null != whList && whList.size() > 0) {
                    selectedWhId = whList.get(0).get("WH_ID").toString();
                }
            } else {
                parentOrgId = logonUser.getDealerId();
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            String sqlStr = "";
            List<Map<String, Object>> vdList = dao.getVenderList(sqlStr);

            String marker = logonUser.getName();
            String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_39);//获取制单单号
            List<Map<String, String>> voList = null;

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String userAct = "";
            String plannerStr = "计划员";
            List<Map<String, Object>> plannersList = partPlannerQueryDao.getInstance().getUsers(plannerStr, userAct);

            act.setOutData("plannersList", plannersList);
            act.setOutData("currUserId", logonUser.getUserId());
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("orderCode", orderCode);
            act.setOutData("actionURL", actionURL);
            act.setOutData("marker", marker);
            act.setOutData("list", voList);
            act.setOutData("WHList", WHList);
            act.setOutData("vdList", vdList);
            act.setOutData("selectedVdId", selectedVdId);
            act.setOutData("selectedWhId", selectedWhId);
            act.setOutData("curDate", sdf.format(date));

            act.setForword(PART_RECEIVE_ADD);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件计划领件单新增页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 显示配件库存信息
     */
    public void showPartStockBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        StringBuffer sbStr = new StringBuffer();
        try {
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));// 件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));// 配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));// 供应商ID
            String plannerId = CommonUtils.checkNull(request.getParamValue("plannerId"));// 计划员ID

            if (null != partCode && !"".equals(partCode)) {
                sbStr.append(" AND UPPER(P.PART_CODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbStr.append(" AND UPPER(P.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbStr.append(" AND P.PART_CNAME LIKE '%" + partCname.trim() + "%' ");
            }
            if (null != venderId && !"".equals(venderId)) {
                sbStr.append(" AND BP.VENDER_ID  = '" + venderId + "' ");
            }
            if (null != plannerId && !"".equals(plannerId)) {
                sbStr.append(" AND SD.PLANNER_ID  = '" + plannerId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartStockBase(
                    sbStr.toString(), whId, Constant.PAGE_SIZE_MIDDLE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取配件库存信息失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-2
     * @Title : 关闭或打开配件计划领件单信息
     */
    public void closeOrOpenOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            Long userId = logonUser.getUserId(); //制单人ID
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId")); //制单ID
            String remark = CommonUtils.checkNull(req.getParamValue("remark")); //备注
            String option = CommonUtils.checkNull(req.getParamValue("option")); //操作类型
            String purOrderCode = req.getParamValue("purOrderCode"); //采购订单号

            StringBuffer strBff = new StringBuffer();
            strBff.append(" AND A.ORDER_ID = '" + orderId + "' ");

            List<Map<String, Object>> checkList = dao.queryPartRecvOrderList(strBff.toString(), "");

            if (null != checkList && checkList.size() > 0) {
                if ("close".equals(option)) {
                    String stateStr = checkList.get(0).get("STATE").toString();

                    if ((orderState2 + "").equals(stateStr)) {
                        errorExist = "该领件订单已完成，不能进行关闭操作!";
                    } else if ((orderState3 + "").equals(stateStr)) {
                        errorExist = "该领件订单已关闭，不需重复关闭操作!";
                    } else if ((orderState4 + "").equals(stateStr)) {
                        errorExist = "该领件订单已超期，不能进行关闭操作!";
                    } else {
                        errorExist = "";
                    }
                }
            } else {
                errorExist = "该领件订单无效，请联系管理员!";
            }

            if ("".equals(errorExist)) {
                TtPartPoMainPO selPo = null;
                TtPartPoMainPO updPo = null;

                if (null != purOrderCode && !"".equals(purOrderCode) && "close".equals(option)) {
                    String purOderId = "";

                    String strSql = " AND A.ORDER_CODE = '" + purOrderCode + "' ";
                    List<Map<String, Object>> mainList = dao.queryPoMainList(strSql);

                    if (null != mainList && mainList.size() > 0) {
                        purOderId = mainList.get(0).get("ORDER_ID").toString();
                    }

                    if (!"".equals(purOderId)) {
                        StringBuffer sqlStr = new StringBuffer();
                        sqlStr.append(" AND OD.ORDER_ID = '" + orderId + "' ");
//					sqlStr.append(" AND (OD.BUY_QTY - OD.CHECK_QTY) > 0 ");
                        sqlStr.append(" AND A.PUR_ORDER_CODE = '" + purOrderCode + "' ");

                        List<Map<String, Object>> dtlList = dao.queryPartRecvOrderDtlList(sqlStr.toString());

                        if (null != dtlList && dtlList.size() > 0) {
                            long rtuQty = 0;//回退数量
                            long buyQty = 0;
                            long chkQty = 0;
                            String partId = "";
                            TtPartPoDtlPO selDPo = null;
                            TtPartPoDtlPO updDPo = null;

                            for (int m = 0; m < dtlList.size(); m++) {
                                partId = dtlList.get(m).get("PART_ID").toString();
                                buyQty = Long.parseLong(dtlList.get(m).get("BUY_QTY").toString());
                                chkQty = Long.parseLong(dtlList.get(m).get("CHECK_QTY").toString());
                                if ((buyQty - chkQty) >= 0) {
                                    rtuQty = buyQty - chkQty;
                                }

                                //更新采购订单明细
                                selDPo = new TtPartPoDtlPO();
                                updDPo = new TtPartPoDtlPO();

                                selDPo.setOrderId(Long.parseLong(purOderId));
                                selDPo.setPartId(Long.parseLong(partId));

                                updDPo.setCheckQty(chkQty);
                                updDPo.setSpareQty(rtuQty);
                                updDPo.setIsProductRecv(1);
                                if (chkQty > 0) {
                                    updDPo.setRemark("部分生成领件订单");
                                } else {
                                    updDPo.setRemark("");
                                }


                                dao.update(selDPo, updDPo);
                            }

                            //更新采购订单状态
                            selPo = new TtPartPoMainPO();
                            updPo = new TtPartPoMainPO();

                            selPo.setOrderId(Long.parseLong(purOderId));

                            updPo.setState(Constant.PURCHASE_ORDER_STATE_01);

                            dao.update(selPo, updPo);
                        }

                        //更新领件单状态
                        selPo = new TtPartPoMainPO();
                        updPo = new TtPartPoMainPO();

                        selPo.setOrderId(Long.parseLong(orderId));

                        updPo.setState(Constant.PURCHASE_ORDER_STATE_03);
                        updPo.setRemark1(remark);
                        updPo.setUpdateBy(userId);
                        updPo.setUpdateDate(new Date());

                        dao.update(selPo, updPo);
                    } else {
                        selPo = new TtPartPoMainPO();
                        updPo = new TtPartPoMainPO();

                        selPo.setOrderId(Long.parseLong(orderId));

                        updPo.setState(Constant.PURCHASE_ORDER_STATE_03);
                        updPo.setRemark1(remark);
                        updPo.setUpdateBy(userId);
                        updPo.setUpdateDate(new Date());

                        dao.update(selPo, updPo);
                    }
                } else {
                    selPo = new TtPartPoMainPO();
                    updPo = new TtPartPoMainPO();

                    selPo.setOrderId(Long.parseLong(orderId));

                    if ("close".equals(option)) {
                        updPo.setState(Constant.PURCHASE_ORDER_STATE_03);
                    } else {
                        updPo.setState(Constant.PURCHASE_ORDER_STATE_01);
                    }
                    updPo.setRemark1(remark);
                    updPo.setUpdateBy(userId);
                    updPo.setUpdateDate(new Date());

                    dao.update(selPo, updPo);
                }
            }

            act.setOutData("errorExist", errorExist);// 返回错误信息
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "提交配件计划领件单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title : 保存新增配件计划领件单信息
     */
    public void saveOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 制单单位ID

            String venderId = CommonUtils.checkNull(req.getParamValue("venderId")); //供应商ID
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String remark = CommonUtils.checkNull(req.getParamValue("remark")); //备注

            Long userId = logonUser.getUserId(); //制单人ID
            String buyer = logonUser.getName(); //领件人

            String sqlStr = " AND VD.VENDER_ID = '" + venderId + "'";

            List<Map<String, Object>> vdList = dao.getVenderList(sqlStr);
            String vdName = "";

            if (null != vdList && vdList.size() == 1) {
                vdName = vdList.get(0).get("VENDER_NAME").toString();
            } else {
                errorExist = "供应商已失效或不存在!";
            }

            if ("".equals(errorExist)) {
                Date date = new Date();

                String[] partIdArr = req.getParamValues("cb");
                StringBuffer sbStr = new StringBuffer();
                if (null != whId && !"".equals(whId)) {
                    sbStr.append(" AND TM.WH_ID = '" + whId + "' ");
                }
                Map<String, Object> mapWH = dao.getWareHouses(sbStr.toString()).get(0);
                String whName = mapWH.get("WH_CNAME").toString();

                //配件ID按 保管员分组
                //1.获取 保管员数组
                StringBuffer buffer = new StringBuffer();
                if (null != partIdArr) {
                    for (int i = 0; i < partIdArr.length; i++) {
                        if (i != (partIdArr.length - 1)) {
                            buffer.append("'" + partIdArr[i] + "', ");
                        } else {
                            buffer.append("'" + partIdArr[i] + "'");
                        }
                    }
                }
                List<Map<String, Object>> saverList = dao.getWhManInfos(" AND SD.PART_ID IN(" + buffer.toString() + ") ");

                for (int k = 0; k < saverList.size(); k++) {
                    double recvAmount = 0.00;
                    long recvSum = 0;
                    String strSql = " AND SD.WHMAN = '" + saverList.get(k).get("WHMAN") + "' ";
                    strSql += " AND SD.PART_ID IN(" + buffer.toString() + ") ";

                    List<Map<String, Object>> spplanList = dao.getPartSpplanInfos(strSql);
                    TtPartPoDtlPO insertRDPo = null;

                    if (null != spplanList && spplanList.size() > 0) {
                        String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_39);//获取制单单号
                        Long changeId = Long.parseLong(SequenceManager.getSequence(""));

                        for (int m = 0; m < spplanList.size(); m++) {
                            insertRDPo = new TtPartPoDtlPO();

                            String partId = spplanList.get(m).get("PART_ID").toString();
                            Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
                            String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + partId));//件号
                            String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId));   //配件编码
                            String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + partId)); //配件名称
                            String unit = CommonUtils.checkNull(req.getParamValue("unit_" + partId)); //单位
                            String returnQty = CommonUtils.checkNull(req.getParamValue("returnQty_" + partId)); //计划领件数量
                            String salePrice = CommonUtils.checkNull(req.getParamValue("salePrice_" + partId)).replace(",", ""); //计划领件单价
                            double saleAmount = Arith.mul((Double.parseDouble(salePrice)), (Double.parseDouble(returnQty)));//计划领件金额
                            String deRemark = CommonUtils.checkNull(req.getParamValue("remark_" + partId)); //详细备注

                            recvSum += Long.parseLong(returnQty);
                            recvAmount += saleAmount;

                            insertRDPo.setPolineId(dtlId);
                            insertRDPo.setPlineId(changeId);
                            insertRDPo.setOrderId(changeId);
                            insertRDPo.setLineNo(Long.parseLong((m + 1) + ""));
                            insertRDPo.setPartId(Long.parseLong(partId));
                            insertRDPo.setPartCode(partCode);
                            insertRDPo.setPartOldcode(partOldcode);
                            insertRDPo.setPartCname(partCname);
                            insertRDPo.setUnit(unit);
                            insertRDPo.setVenderId(Long.parseLong(venderId));
                            insertRDPo.setVenderName(vdName);
                            insertRDPo.setBuyPrice(Double.parseDouble(salePrice));
                            insertRDPo.setPlanQty(Long.parseLong(returnQty));
                            insertRDPo.setBuyQty(Long.parseLong(returnQty));
                            insertRDPo.setSpareQty(Long.parseLong(returnQty));
                            insertRDPo.setBuyAmount(saleAmount);
                            insertRDPo.setForecastDate(date);
                            insertRDPo.setRemark(deRemark);
                            insertRDPo.setCreateBy(userId);
                            insertRDPo.setOrgId(Long.parseLong(parentOrgId));
                            insertRDPo.setCreateDate(date);

                            dao.insert(insertRDPo);
                        }

                        TtPartPoMainPO inserRMPo = new TtPartPoMainPO();

                        inserRMPo.setOrderId(changeId);
                        inserRMPo.setOrderCode(changeCode);
                        inserRMPo.setPlanId(changeId);
                        inserRMPo.setBuyerId(userId);
                        inserRMPo.setAmount(recvAmount);
                        inserRMPo.setSumQty(recvSum);
                        inserRMPo.setBuyer(buyer);
                        inserRMPo.setWhId(Long.parseLong(whId));
                        inserRMPo.setWhName(whName);
                        inserRMPo.setPlanType(orderType3);
                        inserRMPo.setRemark(remark);
                        inserRMPo.setCreateBy(userId);
                        inserRMPo.setOrgId(Long.parseLong(parentOrgId));
                        inserRMPo.setCreateDate(date);
                        inserRMPo.setState(Constant.PURCHASE_ORDER_STATE_01);

                        dao.insert(inserRMPo);
                    }
                }
            }

            act.setOutData("errorExist", errorExist);// 返回错误信息
            act.setOutData("success", "true");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存新增配件计划领件单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("errorExist", "保存失败!");
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-11
     * @Title : 修改订单
     */
    public void updateOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String parentOrgId = CommonUtils.checkNull(req.getParamValue("parentOrgId")); //制单单位ID
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));//获取制单ID
            Long userId = logonUser.getUserId(); //制单人ID
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String venderId = CommonUtils.checkNull(req.getParamValue("venderId")); //供应商ID
            String venderName = CommonUtils.checkNull(req.getParamValue("venderName")); //供应商名称
            String remark = CommonUtils.checkNull(req.getParamValue("remark")); //备注

            String[] partIdArr = req.getParamValues("cb");

            List<Map<String, Object>> partList = null;

            if ("".equals(errorExist)) {
                Date date = new Date();
			
			/*StringBuffer sbStr = new StringBuffer();
			if(null != whId && !"".equals(whId))
			{
				sbStr.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			Map<String, Object> mapWH = dao.getWareHouses(sbStr.toString()).get(0);
			String whName = mapWH.get("WH_CNAME").toString();*/


                //先清空 TtPartPoDtlPO 特定单号数据
                TtPartPoDtlPO delRDPo = new TtPartPoDtlPO();

                delRDPo.setOrderId(Long.parseLong(orderId));

                dao.delete(delRDPo);

                double recvAmount = 0.00;
                long recvSum = 0;

                //重新插入TtPartPoDtlPO 特定单号数据
                TtPartPoDtlPO insertRDPo = null;

                if (null != partIdArr) {
                    for (int i = 0; i < partIdArr.length; i++) {
                        insertRDPo = new TtPartPoDtlPO();

                        String partId = partIdArr[i];
                        Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
                        String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + partId));//件号
                        String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId));   //配件编码
                        String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + partId)); //配件名称
                        String unit = CommonUtils.checkNull(req.getParamValue("unit_" + partId)); //单位
                        String returnQty = CommonUtils.checkNull(req.getParamValue("returnQty_" + partId)); //计划领件数量
                        String salePrice = CommonUtils.checkNull(req.getParamValue("salePrice_" + partId)).replace(",", ""); //计划领件单价
                        double saleAmount = Arith.mul((Double.parseDouble(salePrice)), (Double.parseDouble(returnQty)));//计划领件金额
                        String deRemark = CommonUtils.checkNull(req.getParamValue("remark_" + partId)); //详细备注

                        recvSum += Long.parseLong(returnQty);
                        recvAmount += saleAmount;

                        insertRDPo.setPolineId(dtlId);
                        insertRDPo.setPlineId(Long.parseLong(orderId));
                        insertRDPo.setOrderId(Long.parseLong(orderId));
                        insertRDPo.setLineNo(Long.parseLong((i + 1) + ""));
                        insertRDPo.setPartId(Long.parseLong(partId));
                        insertRDPo.setPartCode(partCode);
                        insertRDPo.setPartOldcode(partOldcode);
                        insertRDPo.setPartCname(partCname);
                        insertRDPo.setUnit(unit);
                        insertRDPo.setVenderId(Long.parseLong(venderId));
                        insertRDPo.setVenderName(venderName);
                        insertRDPo.setBuyPrice(Double.parseDouble(salePrice));
                        insertRDPo.setPlanQty(Long.parseLong(returnQty));
                        insertRDPo.setBuyQty(Long.parseLong(returnQty));
                        insertRDPo.setSpareQty(Long.parseLong(returnQty));
                        insertRDPo.setBuyAmount(saleAmount);
                        insertRDPo.setForecastDate(date);
                        insertRDPo.setRemark(deRemark);
                        insertRDPo.setCreateBy(userId);
                        insertRDPo.setOrgId(Long.parseLong(parentOrgId));
                        insertRDPo.setCreateDate(date);

                        dao.insert(insertRDPo);
                    }
                }

                //更新 TtPartPoMainPO
                TtPartPoMainPO selRMPo = new TtPartPoMainPO();
                TtPartPoMainPO updRMPo = new TtPartPoMainPO();

                selRMPo.setOrderId(Long.parseLong(orderId));

                updRMPo.setAmount(recvAmount);
                updRMPo.setSumQty(recvSum);
                updRMPo.setUpdateBy(userId);
                updRMPo.setUpdateDate(date);
                updRMPo.setRemark(remark);

                dao.update(selRMPo, updRMPo);

            }
            act.setOutData("errorExist", errorExist);// 返回错误信息
            act.setOutData("success", "true");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "修改配件计划领件单信息异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 导出配件销售计划领件单EXECEL模板
     */
    public void exportExcelTemplate() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();


            List<List<Object>> list = new LinkedList<List<Object>>();

            //标题
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            listHead.add("配件编码");
            listHead.add("领件数量 ");
            listHead.add("备注");
            list.add(listHead);
            // 导出的文件名
            String fileName = "配件领件订单.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
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
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title : 配件计划领件单-> 导入文件
     */
    public void partResRecUpload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String venderId = CommonUtils.checkNull(req.getParamValue("venderId")); //供应商ID
            String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String companyName = ""; //制单单位
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                companyName = dao.getMainCompanyName(parentOrgId);
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
                companyName = dao.getDealerName(parentOrgId);
            }
            List<Map<String, String>> errorInfo = new ArrayList<Map<String, String>>();
            List<Map<String, String>> maxLineErro = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 3, 3, maxSize);

            String err = "";

            if (errNum != 0) {
                switch (errNum) {
                    case 1:
                        err += "文件列数过多!";
                        break;
                    case 2:
                        err += "空行不能大于三行!";
                        break;
                    case 3:
                        err += "文件不能为空!";
                        break;
                    case 4:
                        err += "文件不能为空!";
                        break;
                    case 5:
                        err += "文件不能大于!";
                        break;
                    default:
                        break;
                }
            }
            if (!"".equals(err)) {
                act.setOutData("error", err);
                act.setOutData("actionURL", actionURL);
                act.setForword(INPUT_ERROR_URL);
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                loadVoList(voList, list, errorInfo, maxLineErro, parentOrgId, whId, venderId);
                if (maxLineErro.size() > 0) {
                    err = maxLineErro.get(0).get("1").toString();
                    act.setOutData("error", err);
                    act.setOutData("actionURL", actionURL);
                    act.setForword(INPUT_ERROR_URL);
                } else if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setOutData("actionURL", actionURL);
                    act.setForword(INPUT_ERROR_URL);
                } else {
                    StringBuffer sbString = new StringBuffer();
                    sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
                    List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
                    String sqlStr = "";
                    List<Map<String, Object>> vdList = dao.getVenderList(sqlStr);

                    String marker = logonUser.getName();
                    String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_39);//获取制单单号

                    act.setOutData("selectedWhId", whId);
                    act.setOutData("selectedVdId", venderId);
                    act.setOutData("parentOrgId", parentOrgId);
                    act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
                    act.setOutData("parentOrgCode", parentOrgCode);
                    act.setOutData("changeCode", changeCode);
                    act.setOutData("marker", marker);
                    act.setOutData("companyName", companyName);
                    act.setOutData("WHList", WHList);
                    act.setOutData("list", voList);
                    act.setOutData("vdList", vdList);
                    act.setOutData("actionURL", actionURL);

                    act.setForword(PART_RECEIVE_ADD);//新增领件
                }

            }
        } catch (Exception e) {// 异常方法
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @return :
     * LastDate    : 2013-4-12
     * @Title : 读取CELL
     */
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo, List<Map<String, String>> maxLineErro, String parentOrgId, String whId, String venderId) {
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
            double salePrice = 0.00;
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                Map<String, String> tempmap = new HashMap<String, String>();
                int maxLineNum = Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM;
                if (Integer.parseInt(key) > maxLineNum) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "导入数据行数不能超过 " + maxLineNum + "行!");
                    maxLineErro.add(errormap);
                } else if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "配件编码");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
                    String partOldCode = cells[0].getContents().trim().toUpperCase();
                    List<Map<String, Object>> partCheck = dao.checkOldCode(partOldCode);
                    if (partCheck.size() == 1) {
                        List<Map<String, Object>> partList = dao.getPartStockInfos(partOldCode, parentOrgId, whId, venderId);
                        if (partList.size() == 1) {
                            tempmap.put("partOldcode", cells[0].getContents().trim());
                            tempmap.put("partCode", partList.get(0).get("PART_CODE").toString());
                            tempmap.put("partCname", partList.get(0).get("PART_CNAME").toString());
                            tempmap.put("unit", partList.get(0).get("UNIT").toString());
                            tempmap.put("partId", partList.get(0).get("PART_ID").toString());
                            tempmap.put("normalQty", partList.get(0).get("NORMAL_QTY").toString());
                            salePrice = Double.parseDouble(partList.get(0).get("PLAN_PRICE").toString());

                        } else {
                            Map<String, String> errormap = new HashMap<String, String>();
                            errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                            errormap.put("2", "配件编码【" + cells[0].getContents() + "】");
                            errormap.put("3", "非领件件或供应商与配件无采购关系!");
                            errorInfo.add(errormap);
                        }

                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "配件编码【" + cells[0].getContents() + "】");
                        errormap.put("3", "不存在");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "计划领件数量");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
                    String accTemp = cells[1].getContents().trim();
                    if (null == accTemp || "".equals(accTemp)) {
                        accTemp = "0";
                    } else {
                        accTemp = accTemp.replace(",", "");
                    }

                    String regex = "(^[1-9]+\\d*$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(accTemp);

                    if (matcher.find()) {
                        NumberFormat numberFormat = NumberFormat.getNumberInstance();

                        numberFormat.setMinimumFractionDigits(2);
                        numberFormat.setMaximumFractionDigits(2);
                        numberFormat.setMaximumIntegerDigits(10);

                        tempmap.put("returnQty", accTemp);
                        tempmap.put("salePrice", salePrice + "");
                        tempmap.put("saleAmount", numberFormat.format(Arith.mul(salePrice, Double.parseDouble(accTemp))));
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "计划领件数量");
                        errormap.put("3", "非法数值!");
                        errorInfo.add(errormap);
                    }
                }
                tempmap.put("remark", cells.length < 3 || null == cells[2].getContents() ? "" : cells[2].getContents().trim());
                voList.add(tempmap);
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 配件计划领件单详情查询
     */
    public void partOrderDetailSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String changeId = CommonUtils.checkNull(request.getParamValue("changeId")); // 订单ID

            StringBuffer sbString = new StringBuffer();
            if (null != changeId && !"".equals(changeId)) {
                sbString.append(" AND TD.RETAL_ID = '" + changeId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartRecvOrderDtl(
                    sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件计划领件单详情信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-7-31
     * @Title :导出配件计划领件单信息
     */
    public void exportSaleOrdersExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request
                    .getParamValue("orderCode")); // 领件单号
            String buyer = CommonUtils.checkNull(request
                    .getParamValue("buyer")); // 领件人
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 出库ID
            String orderState = CommonUtils.checkNull(request
                    .getParamValue("state")); // 制单状态
            String partOldcode = CommonUtils.checkNull(request
                    .getParamValue("partOldcode")); // 配件编码
            String partName = CommonUtils.checkNull(request
                    .getParamValue("partName")); //配件名称
            String partCode = CommonUtils.checkNull(request
                    .getParamValue("partCode")); // 配件件号
            String purOrderCode = CommonUtils.checkNull(request
                    .getParamValue("purOrderCode")); //采购订单号

            StringBuffer sbString = new StringBuffer();
            StringBuffer sbStrN = new StringBuffer();
            if (null != orderCode && !"".equals(orderCode)) {
                sbString.append(" AND UPPER(A.ORDER_CODE) LIKE '%" + orderCode.trim().toUpperCase() + "%' ");
            }
            if (null != buyer && !"".equals(buyer)) {
                sbString.append(" AND A.BUYER LIKE '%" + buyer.trim() + "%' ");
            }
            if (null != orderState && !"".equals(orderState)) {
                sbString.append(" AND A.STATE = '" + orderState + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(A.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(A.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND A.WH_ID = '" + whId + "' ");
            }
            if (null != purOrderCode && !"".equals(purOrderCode)) {
                sbString.append(" AND UPPER(A.PUR_ORDER_CODE) LIKE '%" + purOrderCode.trim().toUpperCase() + "%' ");
            }
            if (null != partName && !"".equals(partName)) {
                sbStrN.append(" AND OD.PART_CNAME LIKE '%" + partName.trim() + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbStrN.append(" AND UPPER(OD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }
            if (null != partCode && !"".equals(partCode)) {
                sbStrN.append(" AND OD.PART_CODE LIKE '%" + partCode.trim().toUpperCase() + "%' ");
            }
            String[] head = new String[12];
            head[0] = "序号";
            head[1] = "采购订单号";
            head[2] = "领件单号";
            head[3] = "制单人";
            head[4] = "制单日期";
            head[5] = "仓库";
            head[6] = "计划类型";
            head[7] = "总数量";
            head[8] = "总金额(元)";
            head[9] = "状态";
            List<Map<String, Object>> list = dao.queryPartRecvOrderList(sbString.toString(), sbStrN.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[12];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("PUR_ORDER_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("ORDER_CODE"));
                        detail[3] = CommonUtils
                                .checkNull(map.get("BUYER"));
                        detail[4] = CommonUtils
                                .checkNull(map.get("CREATE_DATE"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("WH_NAME"));
                        detail[6] = "领件计划";
                        detail[7] = CommonUtils.checkNull(map
                                .get("SUM_QTY"));
                        detail[8] = CommonUtils.checkNull(map
                                .get("AMOUNT"));
                        int ordState = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")));
                        if (orderState1 == ordState) {
                            detail[9] = "未完成";
                        } else if (orderState2 == ordState) {
                            detail[9] = "已完成";
                        } else if (orderState3 == ordState) {
                            detail[9] = "已关闭";
                        } else {
                            detail[9] = "已超期";
                        }

                        list1.add(detail);
                    }
                }
            }
            String fileName = "";
            fileName = "配件领件订单信息";

            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件计划领件单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-22
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
}
