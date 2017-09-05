package com.infodms.dms.actions.parts.purchaseManager;

import com.infodms.dms.actions.parts.purchaseOrderManager.PurchaseOrderChkManager;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partPlannerQueryManager.partPlannerQueryDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanConfirm.PartPlanConfirmDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanQuery.PartPlanQueryDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderChkDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.partRecvOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : chenjunjiang
 *         CreateDate     : 2013-7-2
 * @ClassName : PurchaseOrderManager
 * @Description : 采购订单
 */
public class PurchaseOrderManager extends BaseImport implements PTConstants {

    public Logger logger = Logger.getLogger(PurchaseOrderChkManager.class);
    private PurchaseOrderChkDao dao = PurchaseOrderChkDao.getInstance();
    private static final String DEALERID_BILL = "2013060728370313";
    private static final Integer PRINT_SIZE = 35;
    private static final int orderType3 = Constant.PART_PURCHASE_PLAN_TYPE_03;//领用计划
    private static final int orderState1 = Constant.PURCHASE_ORDER_STATE_01;//未完成
    private static final int orderState2 = Constant.PURCHASE_ORDER_STATE_02;//已完成
    private static final int orderState3 = Constant.PURCHASE_ORDER_STATE_03;//已关闭
    private static final int orderState4 = Constant.PURCHASE_ORDER_STATE_04;//已超期

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 查询初始化, 转到进货单查询页面
     */
    public void purchaseOrderQueryInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            PartPlanConfirmDao dao = PartPlanConfirmDao.getInstance();
            PartPlanQueryDao dao2 = PartPlanQueryDao.getInstance();

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
            List<Map<String, Object>> planerList = dao2.getUserPoseLise(1, null);
            request.setAttribute("planerList", planerList);
            request.setAttribute("wareHouseList", dao.getWareHouse(logonUser.getUserId()));
            request.setAttribute("curUserId", logonUser.getUserId());
            act.setForword(PART_PURCHASEORDERM_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-10-30
     * @Title : 生成领用页面初始化
     */
    public void productRecvOrderInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单Id
            String parentOrgId = "";//制单单位ID
            String venderId = "";
            String venderName = "";

            Map<String, Object> mainInfo = dao.getOrderMain(orderId);
            List<Map<String, Object>> dtlList = dao.queryPurOrderDetailList(orderId);
            if (null != dtlList && dtlList.size() > 0) {
                venderId = dtlList.get(0).get("VENDER_ID").toString();
                venderName = dtlList.get(0).get("VENDER_NAME").toString();
            }

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }

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
            act.setOutData("marker", marker);
            act.setOutData("list", voList);
            act.setOutData("selectedWhId", mainInfo.get("WH_ID"));
            act.setOutData("selectedWhName", mainInfo.get("WH_NAME"));
            act.setOutData("venderId", venderId);
            act.setOutData("venderName", venderName);
            act.setOutData("curDate", sdf.format(date));
            act.setOutData("orderId", orderId);
            act.setOutData("planOrderCode", mainInfo.get("ORDER_CODE"));
            act.setForword(PRODUCT_RECV_ORDER_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-10-30
     * @Title : 获取采购订单中可生成领用订单配件
     */
    public void showPartStockBase() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        StringBuffer sbStr = new StringBuffer();
        try {
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));// 件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));// 配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));// 供应商ID
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
            if (null != orderId && !"".equals(orderId)) {
                sbStr.append(" AND PD.ORDER_ID  = '" + orderId + "' ");
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
     * @throws : LastDate    : 2013-10-30
     * @Title : 生成领用订单
     */
    public void saveOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String parentOrgId = CommonUtils.checkNull(req.getParamValue("parentOrgId"));// 制单单位ID
            String venderId = CommonUtils.checkNull(req.getParamValue("venderId")); //供应商ID
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String remark = CommonUtils.checkNull(req.getParamValue("remark")); //备注
            String whName = CommonUtils.checkNull(req.getParamValue("whName"));//仓库name
            String vdName = CommonUtils.checkNull(req.getParamValue("venderName")); //供应商名称
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));// 采购订单ID
            String planOrderCode = CommonUtils.checkNull(req.getParamValue("planOrderCode"));// 采购订单Code

            Long userId = logonUser.getUserId(); //制单人ID
            String buyer = logonUser.getName(); //领件人

            Date date = new Date();

            String[] partIdArr = req.getParamValues("cb");

            if (null != partIdArr) {
                String checkSql = "";
                String errInfoTemp = "";
                List<Map<String, Object>> checkOrderDtlList = null;
                for (int i = 0; i < partIdArr.length; i++) {
                    checkSql = " AND PD.PART_ID = '" + partIdArr[i] + "'  AND PD.ORDER_ID = '" + orderId + "' AND PD.CHECK_QTY <= 0 ";
                    checkOrderDtlList = dao.checkOrderDtlList(checkSql);
                    if (null == checkOrderDtlList || checkOrderDtlList.size() < 1) {
                        String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partIdArr[i]));   //配件编码
                        errInfoTemp += partOldcode + ",";
                    }

                }
                if (!"".equals(errInfoTemp)) {
                    errorExist = "配件编码【" + errInfoTemp + "】已生成处理，不能再生成领用订单!";
                }
            }

            if ("".equals(errorExist)) {
                SimpleDateFormat sdfM = new SimpleDateFormat("MM");
                sdfM.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//			String moth = sdfM.format(date);

                SimpleDateFormat sdfD = new SimpleDateFormat("dd");
                sdfD.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//			String day = sdfD.format(date);

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
                List<Map<String, Object>> saverList = partRecvOrderDao.getInstance().getWhManInfos(" AND SD.PART_ID IN(" + buffer.toString() + ") ");

			/*String countSql = " AND UPPER(PM.ORDER_CODE) LIKE '%"+ planOrderCode.toUpperCase() +"%' ";
            List<Map<String, Object>> countList = dao.getCountPoMain(countSql);
			int orderCount = 1;
			int orgOderCount = 1;
			if(null != countList && countList.size() > 0)
			{
				orderCount = Integer.parseInt(countList.get(0).get("ORDER_COUNT").toString());
			}
			orgOderCount = orderCount;*/

                for (int k = 0; k < saverList.size(); k++) {
                    double recvAmount = 0.00;
                    long recvSum = 0;
                    String strSql = " AND SD.WHMAN = '" + saverList.get(k).get("WHMAN") + "' ";
                    strSql += " AND PD.PART_ID IN(" + buffer.toString() + ") ";
                    strSql += " AND PD.ORDER_ID = '" + orderId + "' ";

                    List<Map<String, Object>> spplanList = dao.getPartSpplanInfos(strSql);
                    TtPartPoDtlPO insertRDPo = null;
                    TtPartPoDtlPO selRDPo = null;
                    TtPartPoDtlPO updRDPo = null;

                    if (null != spplanList && spplanList.size() > 0) {
                        String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_39);//获取制单单号
					/*orderCount = orgOderCount;
					orderCount = orderCount + k;
					if(orderCount < 10)
					{
						changeCode = planOrderCode + "-" + moth + day + "00" + orderCount;
					}
					else if(orderCount >= 10 && orderCount < 100)
					{
						changeCode = planOrderCode + "-" + moth + day + "0" + orderCount;
					}
					else
					{
						changeCode = planOrderCode + "-" + moth + day + orderCount;
					}*/

                        Long changeId = Long.parseLong(SequenceManager.getSequence(""));

                        for (int m = 0; m < spplanList.size(); m++) {
                            insertRDPo = new TtPartPoDtlPO();
                            selRDPo = new TtPartPoDtlPO();
                            updRDPo = new TtPartPoDtlPO();

                            String partId = spplanList.get(m).get("PART_ID").toString();
                            Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
                            String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + partId));//件号
                            String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId));   //配件编码
                            String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + partId)); //配件名称
                            String unit = CommonUtils.checkNull(req.getParamValue("unit_" + partId)); //单位
                            String returnQty = CommonUtils.checkNull(req.getParamValue("returnQty_" + partId)); //计划领用数量
                            String salePrice = CommonUtils.checkNull(req.getParamValue("salePrice_" + partId)).replace(",", ""); //计划领用单价
                            double saleAmount = Arith.mul((Double.parseDouble(salePrice)), (Double.parseDouble(returnQty)));//计划领用金额
                            String deRemark = CommonUtils.checkNull(req.getParamValue("remark_" + partId)); //详细备注

                            String polineId = spplanList.get(m).get("POLINE_ID").toString();//采购订单明细ID

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

                            //更新源采购订单状态
                            selRDPo.setPolineId(Long.parseLong(polineId));
                            selRDPo.setOrderId(Long.parseLong(orderId));

                            updRDPo.setCheckQty(Long.parseLong(returnQty));
                            updRDPo.setSpareQty(0l);
                            updRDPo.setRemark("已生成领件订单");
                            updRDPo.setIsProductRecv(0);//已生成领用订单

                            dao.update(selRDPo, updRDPo);

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
                        inserRMPo.setPurOrderCode(planOrderCode);

                        dao.insert(inserRMPo);
                    }
                }
            }

            act.setOutData("errorExist", errorExist);// 返回错误信息
            act.setOutData("success", "true");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存新增配件计划领用单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("errorExist", "保存失败!");
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 查询采购订单信息
     */
    public void queryPurchaseOrderInfo() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//采购订单号
            String buyer = CommonUtils.checkNull(request.getParamValue("BUYER"));//采购员
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
//            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
//            String planType = CommonUtils.checkNull(request.getParamValue("PLAN_TYPE"));//计划类型 只有周计划
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态
            System.out.println(":state==="+state);
//            String planer_id = CommonUtils.checkNull(request.getParamValue("PLANER_NAME"));//计划员
            String partOldCode = CommonUtils.checkNull(request.getParamValue("DPARTOLD_CODE"));//配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("DPART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("DPART_CODE"));//配件件号
            savePlanContion();
            TtPartPoMainPO mainPO = new TtPartPoMainPO();
            mainPO.setOrderCode(orderCode);
            mainPO.setBuyer(buyer);
            if (!"".equals(state)) {
                mainPO.setState((CommonUtils.parseInteger((state))));
            }
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPurchaseOrderList(mainPO,
                    beginTime, endTime, curPage, Constant.PAGE_SIZE, null, partOldCode, partCname, partCode);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-29
     * @Title :
     * @Description: 明细查询初始化
     */
    public void queryPurOrderDtlInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String flag = CommonUtils.checkNull(request.getParamValue("flag")); // 返回标志
            act.setForword(PART_PURCHASEORDERM_QUERY_URL);
            act.setOutData("flag", flag);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-15
     * @Title :
     * @Description: 明细查询
     */
    public void queryPurOrderDtlInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("DORDER_CODE"));//采购订单号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));//配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime2"));//开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime2"));//结束时间
            String state = CommonUtils.checkNull(request.getParamValue("STATE2"));//

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPurOrderDtlList(orderCode, partOldCode, partCname, partCode, beginTime, endTime, state, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title :
     * @Description: 查询订单详细信息
     */
    public void purchaseOrderView() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单Id
            String flag = CommonUtils.checkNull(request.getParamValue("flag")); // 返回标志
            String actUrl = ""; // 计划领用订单返回地址
            if (null != request.getParamValue("actUrl")) {
                actUrl = request.getParamValue("actUrl").toString();
            }
            Map<String, Object> mainInfo = dao.getOrderMain(orderId);
            List whmans = new ArrayList();
            whmans = dao.queryWhmanInfo();
            act.setOutData("whmans", whmans);
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("curPage", curPage);
            act.setOutData("backflag", flag);
            act.setOutData("actUrl", actUrl);
            act.setOutData("mainInfo", mainInfo);
            act.setForword(PURCHASEORDER_CHK_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title :
     * @Description: 查询订单详细信息
     */
    public void purchaseOrderView2() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单Id
            String flag = CommonUtils.checkNull(request.getParamValue("flag")); // 返回标志
            String actUrl = ""; // 计划领用订单返回地址
            if (null != request.getParamValue("actUrl")) {
                actUrl = request.getParamValue("actUrl").toString();
            }
            Map<String, Object> mainInfo = dao.getOrderMain(orderId);
            List whmans = new ArrayList();
            whmans = dao.queryWhmanInfo();
            act.setOutData("whmans", whmans);
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("curPage", curPage);
            act.setOutData("backflag", flag);
            act.setOutData("actUrl", actUrl);
            act.setOutData("mainInfo", mainInfo);
            act.setForword(PURCHASEORDER_CHK_URL1);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title :
     * @Description: 修改订单信息
     */
    public void updatePurOrderInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单Id
            Map<String, Object> mainInfo = dao.getOrderMain(orderId);
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("curPage", curPage);
            act.setOutData("mainInfo", mainInfo);
            act.setForword(PURCHASEORDER_MOD_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title :
     * @Description: 修改订单信息
     */
    public void deletePodtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单Id
            String pId = CommonUtils.checkNull(request.getParamValue("pId")); // 明细Id
            String sumQty = CommonUtils.checkNull(request.getParamValue("SUM_QTY"));
            String amount = CommonUtils.checkNull(request.getParamValue("AMOUNT"));
            String buyAmount = CommonUtils.checkNull(request.getParamValue("BUY_AMOUNT" + pId));
            String buyQty = CommonUtils.checkNull(request.getParamValue("BUY_QTY" + pId));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));

            //zhumingwei 2013-09-12
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            //zhumingwei 2013-09-12

            //删除明细
            TtPartPoDtlPO dtlPO = new TtPartPoDtlPO();
            dtlPO.setPolineId(CommonUtils.parseLong(pId));
            dao.delete(dtlPO);

            boolean backFlag = false;
            Map<String, Object> mainInfo = new HashMap<String, Object>();

            //如果该订单的所有明细都被删除了,那么该订单也会被删除
            boolean flag = dao.isAllDel(orderId);
            if (flag) {
                TtPartPoMainPO mainPO = new TtPartPoMainPO();
                mainPO.setOrderId(CommonUtils.parseLong(orderId));
                dao.delete(mainPO);
                backFlag = true;
            } else {
                //更新主表中的数量和金额
                TtPartPoMainPO mainPO1 = new TtPartPoMainPO();
                TtPartPoMainPO mainPO2 = new TtPartPoMainPO();
                mainPO1.setOrderId(CommonUtils.parseLong(orderId));

                mainPO2.setSumQty(CommonUtils.parseLong(sumQty) - CommonUtils.parseLong(buyQty));
                mainPO2.setAmount(Arith.sub(Double.valueOf(amount.replaceAll(",", "")), Double.valueOf(buyAmount)));
                dao.update(mainPO1, mainPO2);

                //如果满足条件的明细被删除完成了(整个订单的明细并没有被完全删除),那么就返回到主界面
                boolean flag1 = dao.isAllDelCon(orderId);
                if (flag1) {
                    backFlag = true;
                } else {
                    mainInfo = dao.getOrderMain(orderId);
                }
            }

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            act.setOutData("success", "删除成功!");
            act.setOutData("sumQty", ((BigDecimal) mainInfo.get("SUM_QTY")).longValue());
            act.setOutData("amount", (String) mainInfo.get("AMOUNT"));
            act.setOutData("curPage", curPage);
            act.setOutData("backFlag", backFlag);
            act.setOutData("partOldCode", partOldCode);
            act.setOutData("partCname", partCname);
            act.setOutData("PART_CODE", PART_CODE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "订单明细删除失败,请联系管理员！");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * zhumingwei 2013-09-12
     *
     * @param :
     * @return :
     * @throws : LastDate    : 2013-09-12
     * @Title :
     * @Description: 修改采购数量
     */
    public void updatePodtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单Id
            String pId = CommonUtils.checkNull(request.getParamValue("pId")); // 明细Id
            String sumQty = CommonUtils.checkNull(request.getParamValue("SUM_QTY"));//更改后的采购数量
            String amount = CommonUtils.checkNull(request.getParamValue("AMOUNT"));
            String buyQty = CommonUtils.checkNull(request.getParamValue("BUY_QTY" + pId));
            String buyQty1 = CommonUtils.checkNull(request.getParamValue("BUY_QTY1" + pId));//原来的采购数量

            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            Long count = CommonUtils.parseLong(buyQty1) - CommonUtils.parseLong(buyQty);//算出采购数量的变化

            //修改明细数量
            TtPartPoDtlPO dtlPO = new TtPartPoDtlPO();
            dtlPO.setPolineId(CommonUtils.parseLong(pId));
            TtPartPoDtlPO poValue = (TtPartPoDtlPO) dao.select(dtlPO).get(0);
            double totel = poValue.getBuyPrice() * Double.parseDouble(count.toString());//算出采购数量变化后乘以计划书后的金额
            TtPartPoDtlPO dtlPOValue = new TtPartPoDtlPO();
            dtlPOValue.setBuyQty(Long.parseLong(buyQty));//更新明细数量（变化后的）
            dtlPOValue.setBuyAmount(poValue.getBuyAmount() - totel);//更新明细金额
            dtlPOValue.setUpdateBy(logonUser.getUserId());
            dtlPOValue.setUpdateDate(new Date());
            dao.update(dtlPO, dtlPOValue);

            //更新主表中的数量和金额
            TtPartPoMainPO mainPO1 = new TtPartPoMainPO();
            TtPartPoMainPO mainPO2 = new TtPartPoMainPO();
            mainPO1.setOrderId(CommonUtils.parseLong(orderId));
            mainPO2.setSumQty(CommonUtils.parseLong(sumQty) - CommonUtils.parseLong(count.toString()));
            mainPO2.setAmount(Arith.sub(Double.valueOf(amount.replaceAll(",", "")), totel));
            dao.update(mainPO1, mainPO2);

            Map<String, Object> mainInfo = dao.getOrderMain(orderId);

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            act.setOutData("sumQty", ((BigDecimal) mainInfo.get("SUM_QTY")).longValue());
            act.setOutData("amount", (String) mainInfo.get("AMOUNT"));
            act.setOutData("curPage", curPage);
            act.setOutData("partOldCode", partOldCode);
            act.setOutData("partCname", partCname);
            act.setOutData("PART_CODE", PART_CODE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "订单明细保存失败,请联系管理员！");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title :
     * @Description: 查询订单详细信息
     */
    public void purchaseOrderView1() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单Id
            Map<String, Object> mainInfo = dao.getOrderMain(orderId);
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("curPage", curPage);
            act.setOutData("mainInfo", mainInfo);
            act.setForword(PURCHASEORDER_VIEW_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单明细");
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
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPurchaseOrderDetailList(orderId, partOldCode, partCname, partCode, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-9
     * @Title : 领件单明细关闭
     */
    public void closeRecvOrderParts() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String curPage = CommonUtils.checkNull(req.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            Long userId = logonUser.getUserId(); //制单人ID
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId")); //制单ID
            String poLineId = CommonUtils.checkNull(req.getParamValue("poLineId")); //明细ID
            String purOrderCode = req.getParamValue("purOrderCode"); //采购订单号

            StringBuffer strBff = new StringBuffer();
            strBff.append(" AND A.ORDER_ID = '" + orderId + "' ");

            List<Map<String, Object>> checkList = partRecvOrderDao.getInstance().queryPartRecvOrderList(strBff.toString(), "");

            if (null != checkList && checkList.size() > 0) {
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
            } else {
                errorExist = "该领件订单无效，请联系管理员!";
            }


            if ("".equals(errorExist)) {
                TtPartPoMainPO selPo = null;
                TtPartPoMainPO updPo = null;
                Date date = new Date();

                StringBuffer sqlStr = new StringBuffer();
                sqlStr.append(" AND OD.ORDER_ID = '" + orderId + "' ");
                sqlStr.append(" AND OD.POLINE_ID = '" + poLineId + "' ");

                List<Map<String, Object>> dtlList = partRecvOrderDao.getInstance().queryPartRecvOrderDtlList(sqlStr.toString());

                if (null != purOrderCode && !"".equals(purOrderCode)) {
                    String purOderId = "";

                    String strSql = " AND A.ORDER_CODE = '" + purOrderCode + "' ";
                    List<Map<String, Object>> mainList = partRecvOrderDao.getInstance().queryPoMainList(strSql);

                    if (null != mainList && mainList.size() > 0) {
                        purOderId = mainList.get(0).get("ORDER_ID").toString();
                    }

                    if (!"".equals(purOderId)) {
                        if (null != dtlList && dtlList.size() > 0) {
                            long rtuQty = 0;//回退数量
                            long buyQty = 0;
                            long chkQty = 0;
                            String partId = "";
                            TtPartPoDtlPO selDPo = null;
                            TtPartPoDtlPO updDPo = null;

                            partId = dtlList.get(0).get("PART_ID").toString();
                            buyQty = Long.parseLong(dtlList.get(0).get("BUY_QTY").toString());
                            chkQty = Long.parseLong(dtlList.get(0).get("CHECK_QTY").toString());
                            if ((buyQty - chkQty) >= 0) {
                                rtuQty = buyQty - chkQty;
                            }

                            if (rtuQty > 0) {
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
                                updDPo.setUpdateBy(userId);
                                updDPo.setUpdateDate(date);

                                dao.update(selDPo, updDPo);

                                //更新领件订单明细
                                selDPo = new TtPartPoDtlPO();
                                updDPo = new TtPartPoDtlPO();

                                selDPo.setOrderId(Long.parseLong(orderId));
                                selDPo.setPolineId(Long.parseLong(poLineId));

                                updDPo.setCheckQty(buyQty);
                                updDPo.setSpareQty(0l);
                                updDPo.setRemark("已关闭");
                                updDPo.setUpdateBy(userId);
                                updDPo.setUpdateDate(date);

                                dao.update(selDPo, updDPo);

                                //更新采购订单状态
                                selPo = new TtPartPoMainPO();
                                updPo = new TtPartPoMainPO();

                                selPo.setOrderId(Long.parseLong(purOderId));

                                updPo.setState(Constant.PURCHASE_ORDER_STATE_01);
                                updDPo.setUpdateBy(userId);
                                updDPo.setUpdateDate(date);

                                dao.update(selPo, updPo);
                            } else {
                                errorExist = "该明细已完成，不可以进行关闭操作!";
                            }
                        } else {
                            errorExist = "领件关闭明细查询失败!";
                        }
                    }
                } else {
                    if (null != dtlList && dtlList.size() > 0) {
                        long buyQty = 0;
                        TtPartPoDtlPO selDPo = null;
                        TtPartPoDtlPO updDPo = null;

                        buyQty = Long.parseLong(dtlList.get(0).get("BUY_QTY").toString());

                        //更新领件订单明细
                        selDPo = new TtPartPoDtlPO();
                        updDPo = new TtPartPoDtlPO();

                        selDPo.setOrderId(Long.parseLong(orderId));
                        selDPo.setPolineId(Long.parseLong(poLineId));

                        updDPo.setCheckQty(buyQty);
                        updDPo.setSpareQty(0l);
                        updDPo.setRemark("已关闭");
                        updDPo.setUpdateBy(userId);
                        updDPo.setUpdateDate(date);

                        dao.update(selDPo, updDPo);
                    } else {
                        errorExist = "领件关闭明细查询失败!";
                    }
                }

                if ("".equals(errorExist)) {
                    StringBuffer chkStr = new StringBuffer();
                    chkStr.append(" AND OD.ORDER_ID = '" + orderId + "' ");
                    chkStr.append(" AND OD.BUY_QTY > OD.CHECK_QTY ");

                    List<Map<String, Object>> chkList = partRecvOrderDao.getInstance().queryPartRecvOrderDtlList(chkStr.toString());

                    boolean flag = true;

                    if (null != chkList && chkList.size() > 0) {
                        flag = false;
                    }
                    //明细全部被关闭
                    if (flag) {
                        //更新领件单状态
                        selPo = new TtPartPoMainPO();
                        updPo = new TtPartPoMainPO();

                        selPo.setOrderId(Long.parseLong(orderId));

                        updPo.setState(Constant.PURCHASE_ORDER_STATE_03);
                        updPo.setRemark1("已关闭");
                        updPo.setUpdateBy(userId);
                        updPo.setUpdateDate(date);

                        dao.update(selPo, updPo);
                    }
                }
            }

            act.setOutData("errorExist", errorExist);// 返回错误信息
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划领件单明细关闭失败");
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
    public void queryPurchaseOrderDetail1() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPurchaseOrderDetailList1(orderId, partOldCode, partCname, curPage, Constant.PAGE_SIZE_MIDDLE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-5
     * @Title :
     * @Description: 查询订单明细(用于修改)
     */
    public void queryPurOrderDtl4Mod() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPurOrderDtl4Mod(orderId, partOldCode, partCname, PART_CODE, curPage, Constant.PAGE_SIZE_MIDDLE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title :
     * @Description: 生成验收指令
     */
    public void generateOrderChk() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));
            String buyId = CommonUtils.checkNull(request.getParamValue("BUYER_ID"));
            String buyer = CommonUtils.checkNull(request.getParamValue("BUYER"));
            String planType = CommonUtils.checkNull(request.getParamValue("PLAN_TYPE1"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME"));
            String vflag = CommonUtils.checkNull(request.getParamValue("vflag"));
            String whmanId = CommonUtils.checkNull(request.getParamValue("WHMAN_ID"));
            String remark2 = CommonUtils.checkNull(request.getParamValue("REMARK"));

            String checkCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_32);

            TtPartPoMainPO poMainPO = new TtPartPoMainPO();
            poMainPO.setOrderId(CommonUtils.parseLong(orderId));
            poMainPO = (TtPartPoMainPO) dao.select(poMainPO).get(0);

            TtPartChkMainPO chkMainPO = new TtPartChkMainPO();
            chkMainPO.setChkId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            chkMainPO.setChkCode(checkCode);
            chkMainPO.setOrderId(CommonUtils.parseLong(orderId));
            chkMainPO.setOrderCode(orderCode);
            chkMainPO.setBuyerId(logonUser.getUserId());
            chkMainPO.setBuyer(logonUser.getName());
            chkMainPO.setPlanType(CommonUtils.parseInteger(planType));
            chkMainPO.setWhId(CommonUtils.parseLong(whId));
            chkMainPO.setWhName(whName);
            chkMainPO.setCreateDate(new Date());
            chkMainPO.setCreateBy(logonUser.getUserId());
            chkMainPO.setOrgId(logonUser.getOrgId());
            chkMainPO.setWhmanId(CommonUtils.parseLong(whmanId));
            chkMainPO.setRemark(remark2);


            if (vflag.equals("1")) {
                chkMainPO.setOriginType(Constant.ORDER_ORIGIN_TYPE_02);
            } else {
                chkMainPO.setOriginType(Constant.ORDER_ORIGIN_TYPE_01);
            }

            int flag = 0;//是否生成完成

            String[] pIds = request.getParamValues("cb");
            if (pIds != null && pIds.length > 0) {
                for (int i = 0; i < pIds.length; i++) {

                    TtPartChkDtlPO chkDtlPO = new TtPartChkDtlPO();
                    String partId = CommonUtils.checkNull(request.getParamValue("PART_ID1" + pIds[i]));
                    String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE" + pIds[i]));
                    String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE" + pIds[i]));
                    String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME" + pIds[i]));
                    String unit = CommonUtils.checkNull(request.getParamValue("UNIT" + pIds[i]));
                    String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE" + pIds[i]));
                    String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID1" + pIds[i]));
                    String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME1" + pIds[i]));
                    String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID1" + pIds[i]));
                    String makerName = CommonUtils.checkNull(request.getParamValue("MAKER_NAME" + pIds[i]));
                    String buyPrice = CommonUtils.checkNull(request.getParamValue("BUY_PRICE1" + pIds[i])).replace(",", "");
                    String planQty = CommonUtils.checkNull(request.getParamValue("PLAN_QTY" + pIds[i]));
                    String buyQty = CommonUtils.checkNull(request.getParamValue("BUY_QTY" + pIds[i]));
                    String geQty = CommonUtils.checkNull(request.getParamValue("GE_QTY" + pIds[i]));
                    String remark = CommonUtils.checkNull(request.getParamValue("REMARK" + pIds[i]));
                    double buyAmount = 0;

                    chkDtlPO.setChklineId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                    chkDtlPO.setChkId(chkMainPO.getChkId());
                    chkDtlPO.setOrderlineId(CommonUtils.parseLong(pIds[i]));
                    chkDtlPO.setPartId(CommonUtils.parseLong(partId));
                    chkDtlPO.setPartCode(partCode);
                    chkDtlPO.setPartOldcode(partOldCode);
                    chkDtlPO.setPartCname(partCname);
                    chkDtlPO.setUnit(unit);
                    chkDtlPO.setStatus(1);
                    if (!"".endsWith(partType) && !"null".equals(partType)) {
                        chkDtlPO.setPartType(CommonUtils.parseInteger(partType));
                    }
                    chkDtlPO.setVenderId(CommonUtils.parseLong(venderId));
                    chkDtlPO.setVenderName(venderName);
                    chkDtlPO.setPlanQty(CommonUtils.parseLong(planQty));
                    chkDtlPO.setBuyQty(CommonUtils.parseLong(buyQty));
                    if (!"".equals(buyPrice) && !"null".equals(buyPrice)) {
                        chkDtlPO.setBuyPrice(CommonUtils.parseDouble(buyPrice));
                        buyAmount = Arith.mul(CommonUtils.parseDouble(buyPrice), CommonUtils.parseLong(buyQty));
                        chkDtlPO.setBuyAmount(buyAmount);
                    } else {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, partOldCode + ":计划价不可以为零!");
                        throw e1;
                    }
                    chkDtlPO.setRemark(remark);
                    chkDtlPO.setCreateDate(new Date());
                    chkDtlPO.setCreateBy(logonUser.getUserId());
                    chkDtlPO.setOrgId(logonUser.getOrgId());
                    chkDtlPO.setCheckQty(CommonUtils.parseLong(geQty));
                    chkDtlPO.setMakerId(CommonUtils.parseLong(makerId));
                    chkDtlPO.setMakerName(makerName);

                    TtPartPoDtlPO dtlPO = new TtPartPoDtlPO();
                    dtlPO.setPolineId(CommonUtils.parseLong(pIds[i]));
                    dtlPO = (TtPartPoDtlPO) dao.select(dtlPO).get(0);

                    TtPartPoDtlPO dtlPO1 = new TtPartPoDtlPO();
                    TtPartPoDtlPO dtlPO2 = new TtPartPoDtlPO();
                    dtlPO1.setPolineId(CommonUtils.parseLong(pIds[i]));
                    dtlPO2.setCheckQty(dtlPO.getCheckQty() + CommonUtils.parseLong(geQty));//已生成数量
                    dtlPO2.setSpareQty(dtlPO.getBuyQty() - dtlPO2.getCheckQty());//待生成数量
                    chkDtlPO.setForecastDate(dtlPO.getForecastDate());

                    dao.insert(chkDtlPO);
                    dao.update(dtlPO1, dtlPO2);
                }
            }

            if (dao.isAllChk(orderId)) {//判断是否已经全部生成完成
                flag = 1;
                TtPartPoMainPO mainPO1 = new TtPartPoMainPO();
                TtPartPoMainPO mainPO2 = new TtPartPoMainPO();
                mainPO1.setOrderId(CommonUtils.parseLong(orderId));
                mainPO2.setState(Constant.PURCHASE_ORDER_STATE_02);
                dao.update(mainPO1, mainPO2);
            }

            dao.insert(chkMainPO);

            List ins = new LinkedList<Object>();
            ins.add(0, chkMainPO.getChkId());
            dao.callProcedure("PKG_PART.P_CREATECHKORDER", ins, null);

            act.setOutData("success", "生成成功!");
            act.setOutData("flag", flag);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, e.getMessage());
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-6
     * @Title :
     * @Description: 领用计划生成验收指令
     */
    public void generateRecvOrderChk() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));
            String buyId = CommonUtils.checkNull(request.getParamValue("BUYER_ID"));
            String buyer = CommonUtils.checkNull(request.getParamValue("BUYER"));
            String planType = CommonUtils.checkNull(request.getParamValue("PLAN_TYPE1"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String whName = CommonUtils.checkNull(request.getParamValue("WH_NAME"));
            String vflag = CommonUtils.checkNull(request.getParamValue("vflag"));
            String whmanId = CommonUtils.checkNull(request.getParamValue("WHMAN_ID"));

            String checkCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_32);

            TtPartPoMainPO poMainPO = new TtPartPoMainPO();
            poMainPO.setOrderId(CommonUtils.parseLong(orderId));
            poMainPO = (TtPartPoMainPO) dao.select(poMainPO).get(0);

            TtPartChkMainPO chkMainPO = new TtPartChkMainPO();
            chkMainPO.setChkId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            chkMainPO.setChkCode(checkCode);
            chkMainPO.setOrderId(CommonUtils.parseLong(orderId));
            chkMainPO.setOrderCode(orderCode);
            chkMainPO.setBuyerId(CommonUtils.parseLong(buyId));
            chkMainPO.setBuyer(buyer);
            chkMainPO.setPlanType(CommonUtils.parseInteger(planType));
            chkMainPO.setWhId(CommonUtils.parseLong(whId));
            chkMainPO.setWhName(whName);
            chkMainPO.setCreateDate(new Date());
            chkMainPO.setCreateBy(logonUser.getUserId());
            chkMainPO.setOrgId(logonUser.getOrgId());
            chkMainPO.setWhmanId(CommonUtils.parseLong(whmanId));


            if (vflag.equals("1")) {
                chkMainPO.setOriginType(Constant.ORDER_ORIGIN_TYPE_02);
            } else {
                chkMainPO.setOriginType(Constant.ORDER_ORIGIN_TYPE_01);
            }

            int flag = 0;//是否生成完成

            String[] pIds = request.getParamValues("ck");
            if (pIds != null && pIds.length > 0) {
                for (int i = 0; i < pIds.length; i++) {

                    TtPartChkDtlPO chkDtlPO = new TtPartChkDtlPO();
                    String partId = CommonUtils.checkNull(request.getParamValue("PART_ID" + pIds[i]));
                    String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE" + pIds[i]));
                    String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE" + pIds[i]));
                    String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME" + pIds[i]));
                    String unit = CommonUtils.checkNull(request.getParamValue("UNIT" + pIds[i]));
                    String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE" + pIds[i]));
                    String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID" + pIds[i]));
                    String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME" + pIds[i]));
                    String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID" + pIds[i]));
                    String makerName = CommonUtils.checkNull(request.getParamValue("MAKER_NAME" + pIds[i]));
                    String buyPrice = CommonUtils.checkNull(request.getParamValue("BUY_PRICE" + pIds[i])).replace(",", "");
                    String planQty = CommonUtils.checkNull(request.getParamValue("PLAN_QTY" + pIds[i]));
                    String buyQty = CommonUtils.checkNull(request.getParamValue("BUY_QTY" + pIds[i]));
                    String geQty = CommonUtils.checkNull(request.getParamValue("GE_QTY" + pIds[i]));
                    String remark = CommonUtils.checkNull(request.getParamValue("REMARK" + pIds[i]));
                    double buyAmount = 0;

                    chkDtlPO.setChklineId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                    chkDtlPO.setChkId(chkMainPO.getChkId());
                    chkDtlPO.setOrderlineId(CommonUtils.parseLong(pIds[i]));
                    chkDtlPO.setPartId(CommonUtils.parseLong(partId));
                    chkDtlPO.setPartCode(partCode);
                    chkDtlPO.setPartOldcode(partOldCode);
                    chkDtlPO.setPartCname(partCname);
                    chkDtlPO.setUnit(unit);
                    chkDtlPO.setStatus(1);
                    if (!"".endsWith(partType) && !"null".equals(partType)) {
                        chkDtlPO.setPartType(CommonUtils.parseInteger(partType));
                    }
                    chkDtlPO.setVenderId(CommonUtils.parseLong(venderId));
                    chkDtlPO.setVenderName(venderName);
                    chkDtlPO.setPlanQty(CommonUtils.parseLong(planQty));
                    chkDtlPO.setBuyQty(CommonUtils.parseLong(buyQty));
                    if (!"".equals(buyPrice) && !"null".equals(buyPrice)) {
                        chkDtlPO.setBuyPrice(CommonUtils.parseDouble(buyPrice));
                        buyAmount = Arith.mul(CommonUtils.parseDouble(buyPrice), CommonUtils.parseLong(buyQty));
                        chkDtlPO.setBuyAmount(buyAmount);
                    } else {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, partOldCode + ":计划价不可以为零!");
                        throw e1;
                    }
                    chkDtlPO.setRemark(remark);
                    chkDtlPO.setCreateDate(new Date());
                    chkDtlPO.setCreateBy(logonUser.getUserId());
                    chkDtlPO.setOrgId(logonUser.getOrgId());
                    chkDtlPO.setCheckQty(CommonUtils.parseLong(geQty));
                    chkDtlPO.setMakerId(CommonUtils.parseLong(makerId));
                    chkDtlPO.setMakerName(makerName);

                    TtPartPoDtlPO dtlPO = new TtPartPoDtlPO();
                    dtlPO.setPolineId(CommonUtils.parseLong(pIds[i]));
                    dtlPO = (TtPartPoDtlPO) dao.select(dtlPO).get(0);

                    TtPartPoDtlPO dtlPO1 = new TtPartPoDtlPO();
                    TtPartPoDtlPO dtlPO2 = new TtPartPoDtlPO();
                    dtlPO1.setPolineId(CommonUtils.parseLong(pIds[i]));
                    dtlPO2.setCheckQty(dtlPO.getCheckQty() + CommonUtils.parseLong(geQty));//已生成数量
                    dtlPO2.setSpareQty(dtlPO.getBuyQty() - dtlPO2.getCheckQty());//待生成数量
                    chkDtlPO.setForecastDate(dtlPO.getForecastDate());

                    dao.insert(chkDtlPO);
                    dao.update(dtlPO1, dtlPO2);
                }
            }

            if (dao.isAllChk(orderId)) {//判断是否已经全部生成完成
                flag = 1;
                TtPartPoMainPO mainPO1 = new TtPartPoMainPO();
                TtPartPoMainPO mainPO2 = new TtPartPoMainPO();
                mainPO1.setOrderId(CommonUtils.parseLong(orderId));
                mainPO2.setState(Constant.PURCHASE_ORDER_STATE_02);
                dao.update(mainPO1, mainPO2);
            }

            dao.insert(chkMainPO);

            List ins = new LinkedList<Object>();
            ins.add(0, chkMainPO.getChkId());
            dao.callProcedure("PKG_PART.P_CREATECHKORDER", ins, null);

            act.setOutData("success", "生成成功!");
            act.setOutData("flag", flag);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "验收指令生成失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :
     * @Description: 关闭订单
     */
    public void closePo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String remark1 = CommonUtils.checkNull(request.getParamValue("REMARK1" + orderId));//关闭原因
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));

            curPage = curPage.equals("") ? "1" : curPage;
            TtPartPoMainPO spo = new TtPartPoMainPO();
            TtPartPoMainPO po = new TtPartPoMainPO();
            spo.setOrderId(CommonUtils.parseLong(orderId));
            po.setState(Constant.PURCHASE_ORDER_STATE_03);
            po.setRemark1(remark1);
            po.setCloseDate(new Date());
            dao.update(spo, po);
            act.setOutData("success", "关闭成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "订单关闭失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title :
     * @Description: 打开订单
     */
    public void openPo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
            curPage = curPage.equals("") ? "1" : curPage;
            TtPartPoMainPO spo = new TtPartPoMainPO();
            TtPartPoMainPO po = new TtPartPoMainPO();
            spo.setOrderId(CommonUtils.parseLong(orderId));
            po.setState(Constant.PURCHASE_ORDER_STATE_01);
            dao.update(spo, po);
            act.setOutData("success", "打开成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "订单打开失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-1
     * @Title :
     * @Description: 打印
     */
    @SuppressWarnings("unchecked")
    public void opPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//订单号
            
            List allList = new ArrayList();
            Map<String, Object> map = dao.getOrderMain(orderId);//根据订单id查询

            Map<String, Object> map1 = dao.getBillInfo(DEALERID_BILL);//根据采购单位id查询开票信息

            List<Map<String, Object>> detailList = dao.queryPurOrderDetailList(orderId);//根据订单号查询明细
            
            
            for (int i = 0; i < detailList.size(); ) {
                List subList = detailList.subList(i, i + PRINT_SIZE > detailList.size() ? detailList.size() : i + PRINT_SIZE);
                i = i + PRINT_SIZE;
                allList.add(subList);
            }
            //取出最后一个元素
            List lastList = (List) allList.get(allList.size() - 1);
            //分页设置
            if (lastList.size() == PRINT_SIZE) {//如果最后一个list的大小刚好等于打印中每页允许的最大记录数,就需要取出最后一个list的最后一个元素放到另一个list中
                List newList = new ArrayList();
                Map map2 = (Map) lastList.get(lastList.size() - 1);
                newList.add(map2);
                allList.remove(lastList);
                List list1 = lastList.subList(0, lastList.size() - 1);
                allList.add(list1);
                allList.add(newList);
            }
            Long venderId=new Long(0);
            if(detailList.size()>0){
            	System.out.println("=====VENDER_ID:"+detailList.get(0).get("VENDER_ID"));
                venderId = ((BigDecimal) detailList.get(0).get("VENDER_ID")).longValue();
            }

            Map<String, Object> map2 = dao.getVenderInfo(venderId);
            act.setOutData("mainMap", map);
            act.setOutData("billMap", map1);
            act.setOutData("venderMap", map2);
            act.setOutData("detailList", detailList);
            act.setOutData("allList", allList);
            
            act.setForword(PURCHASEORDER_PRINT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购订单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }


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
            listHead.add("生成数量");
            listHead.add("备注");
            list.add(listHead);
            // 导出的文件名
            String fileName = "验收指令生成模板.xls";
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
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :Excel导入
     * @Description: TODO
     */
    public void uploadPartPlanExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();

        String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单Id
        String flag = CommonUtils.checkNull(request.getParamValue("flag")); // 返回标志
        String whmanId = CommonUtils.checkNull(request.getParamValue("WHMAN_ID"));//当前库管员
        String isDeft = CommonUtils.checkNull(request.getParamValue("isDeft"));//是否默认
        String actUrl = ""; // 计划领用订单返回地址
        if (null != request.getParamValue("actUrl")) {
            actUrl = request.getParamValue("actUrl").toString();
        }

        InputStream is = null;
        try {
            Map<String, Object> mainInfo = dao.getOrderMain(orderId);
            List whmans = new ArrayList();
            whmans = dao.queryWhmanInfo();
            act.setOutData("whmans", whmans);
            act.setOutData("backflag", flag);
            act.setOutData("whmanId", whmanId);
            act.setOutData("isDeft", isDeft);
            act.setOutData("mainInfo", mainInfo);

            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile1", 3, 3, maxSize);
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
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
                throw e1;
            }
            List excelList = this.getMapList();
            //将导入的数据先放入到临时表中
            insertTmpPart(excelList, errorInfo);

            if (errorInfo.length() > 0) {

                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                throw e1;
            }

            //关联其他表获取需要的数据
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if ("1".equals(isDeft)) {
                list = dao.getPartInfo1(request, orderId);
            } else {
                list = dao.getPartInfo(request, orderId);
            }

            //获取没有匹配的配件信息
            List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
            list1 = dao.getPartInfoNot(request, orderId);
				/*
				if(list.size()==0){
					BizException e1 = new BizException(act,
							ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据,请修改上传文件中的配件编码!");
					throw e1;
				}*/

            act.setOutData("actUrl", actUrl);
            act.setOutData("list", list);
            act.setOutData("list1", list1);

            act.setForword("1".equals(isDeft) ? PURCHASEORDER_CHK_URL1 : PURCHASEORDER_CHK_URL);

        } catch (Exception e) {
            e.printStackTrace();
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, act.getOutData("error") == null ? "文件读取错误" : act.getOutData("error").toString());
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword("1".equals(isDeft) ? PURCHASEORDER_CHK_URL1 : PURCHASEORDER_CHK_URL);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception ex) {
                    BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "文件流关闭错误!");
                    logger.error(logonUser, e1);
                    act.setException(e1);
                }
            }
        }
    }

    private void insertTmpPart(List<Map<String, Cell[]>> list, StringBuffer errorInfo) throws Exception {
        String error = "";
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
                parseCells(i, key, cells, errorInfo);
                if (errorInfo.length() > 0) {
                    break;
                }
            }
        }
    }

    /**
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 装载VO
     * @Description: TODO
     */
    private void parseCells(int i, String rowNum, Cell[] cells, StringBuffer errorInfo) throws Exception {
        String partOldCode = CommonUtils.checkNull(cells[0].getContents());//配件编码
        if ("" == partOldCode) {
            errorInfo.append("第" + rowNum + "行的配件编码不能为空!<br>");
        } else {
            if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                errorInfo.append("第" + (i + 1) + "页,第" + rowNum + "行的数量不能为空!<br>");
            } else {
                String accTemp = cells[1].getContents().trim();
                if (null == accTemp || "".equals(accTemp)) {
                    accTemp = "0";
                } else {
                    accTemp = accTemp.replace(",", "");
                }

                String regex = "((^[0]$)|(^[1-9]+(\\d)*$))";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(accTemp);

                if (!matcher.find()) {
                    errorInfo.append("第" + (i + 1) + "页,第" + rowNum + "行的数量为非法数值!<br>");
                } else {
                    TmpPartUploadPO po = new TmpPartUploadPO();
                    po.setPartOldcode(partOldCode);
                    po.setQty(CommonUtils.parseLong(accTemp));
                    if (cells.length == 3) {
                        po.setRemark(cells[2].getContents().trim());
                    }
                    dao.insert(po);
                }
            }
        }

    }

    public void savePlanContion() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
        String planType = CommonUtils.checkNull(request.getParamValue("PLAN_TYPE"));//计划类型
        String planerName = CommonUtils.checkNull(request.getParamValue("PLANER_NAME"));//计划员
        String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//
        String partOldcode = CommonUtils.checkNull(request.getParamValue("DPARTOLD_CODE"));// part_oldcode
        String partCname = CommonUtils.checkNull(request.getParamValue("DPART_CNAME"));//part_name
        String partCode = CommonUtils.checkNull(request.getParamValue("DPART_CODE"));//part_ocde
        String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//时间 开始
        String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//间 结束
        String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderCode", orderCode);
        map.put("planType", planType);
        map.put("planerName", planerName);
        map.put("state", state);
        map.put("whId", whId);
        map.put("partOldcode", partOldcode);
        map.put("partCname", partCname);
        map.put("partCode", partCode);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("state", state);
        act.getSession().set("condition", map);

    }

    /**
     * : 导出订单入库明细 add zhumingwei 2013-09-22
     *
     * @param :
     * @return :
     * @throws :
     * @Title :
     */
    public void exportPurOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String[] head = new String[13];
            head[0] = "配件编码";
            head[1] = "配件名称";
            head[2] = "件号";
            head[3] = "单位";
            head[4] = "供应商名称";
            head[5] = "计划数量";
            head[6] = "采购数量";
            head[7] = "金额";
            head[8] = "已生成订单数量";
//            head[9] = "备注";

            List<Map<String, Object>> list = dao.exportPurOrder(orderId);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[13];
                        detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[4] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("PLAN_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("BUY_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
                        detail[8] = CommonUtils.checkNull(map.get("CHECK_QTY"));
//                        detail[9] = CommonUtils.checkNull(map.get("REMARK"));

                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
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
            act.setForword(PART_PURCHASEORDERM_QUERY_URL);
        }

    }

    //add zhumingwei 2013-09-22
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {
        String name = "配件采购订单明细.xls";
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

    //2013-10-16 add zhumingwei 增加复选框保存数量金额 begin
    public void partPlanCheckCommit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            String polineIds = CommonUtils.checkNull(request.getParamValue("polineIds")); // 明细Id
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单Id

            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));

            String[] planArr = polineIds.split(",");
            for (int i = 0; i < planArr.length; i++) {
                String buyQty = CommonUtils.checkNull(request.getParamValue("BUY_QTY" + planArr[i]));
                String buyQty1 = CommonUtils.checkNull(request.getParamValue("BUY_QTY1" + planArr[i]));//原来的采购数量
                Long count = CommonUtils.parseLong(buyQty1) - CommonUtils.parseLong(buyQty);//算出采购数量的变化

                //修改明细数量
                TtPartPoDtlPO dtlPO = new TtPartPoDtlPO();
                dtlPO.setPolineId(Long.valueOf(planArr[i]));
                TtPartPoDtlPO poValue = (TtPartPoDtlPO) dao.select(dtlPO).get(0);
                double totel = poValue.getBuyPrice() * Double.parseDouble(count.toString());//算出采购数量变化后乘以计划书后的金额
                TtPartPoDtlPO dtlPOValue = new TtPartPoDtlPO();
                dtlPOValue.setBuyQty(Long.parseLong(buyQty));//更新明细数量（变化后的）
                dtlPOValue.setBuyAmount(poValue.getBuyAmount() - totel);//更新明细金额
                dtlPOValue.setUpdateBy(logonUser.getUserId());
                dtlPOValue.setUpdateDate(new Date());
                dao.update(dtlPO, dtlPOValue);

                //更新主表中的数量和金额
                TtPartPoMainPO mainPO1 = new TtPartPoMainPO();
                mainPO1.setOrderId(CommonUtils.parseLong(orderId));
                TtPartPoMainPO mainPO2 = new TtPartPoMainPO();
                TtPartPoMainPO mainPO3 = (TtPartPoMainPO) dao.select(mainPO1).get(0);
                mainPO1.setOrderId(CommonUtils.parseLong(orderId));
                mainPO2.setSumQty(mainPO3.getSumQty() - CommonUtils.parseLong(count.toString()));
                mainPO2.setAmount(Arith.sub(Double.valueOf(mainPO3.getAmount().toString().replaceAll(",", "")), totel));
                dao.update(mainPO1, mainPO2);

                Map<String, Object> mainInfo = dao.getOrderMain(orderId);
                String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
                if ("".equals(curPage)) {
                    curPage = "1";
                }

                act.setOutData("sumQty", ((BigDecimal) mainInfo.get("SUM_QTY")).longValue());
                act.setOutData("amount", (String) mainInfo.get("AMOUNT"));
                act.setOutData("curPage", curPage);
                act.setOutData("partOldCode", partOldCode);
                act.setOutData("partCname", partCname);
                act.setOutData("PART_CODE", PART_CODE);
            }

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(purchasePlanSettingUrl);
        }
    }
    //2013-10-16 add zhumingwei 增加复选框保存数量金额 end
}
