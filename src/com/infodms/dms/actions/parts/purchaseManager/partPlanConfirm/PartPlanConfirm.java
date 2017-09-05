package com.infodms.dms.actions.parts.purchaseManager.partPlanConfirm;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseManager.partPlanCheck.PartPlanCheckDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanConfirm.PartPlanConfirmDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanQuery.PartPlanQueryDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
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
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PartPlanConfirm extends BaseImport implements PTConstants {

    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);

    /**
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 生成EXCEL
     */
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件计划确认列表.xls";
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
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 计划确认初始
     */
    public void partPlanConfirmInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper req = act.getRequest();
            PartPlanConfirmDao dao = PartPlanConfirmDao.getInstance();
            PartPlanQueryDao dao1 = PartPlanQueryDao.getInstance();
            Map<String, Object> map = new HashMap<String, Object>();

            if (null != act.getSession().get("condition")) {
                if (null != req.getParamValue("flag")) {
                    map = (Map) act.getSession().get("condition");
                    act.getSession().remove("condition");
                } else {
                    act.getSession().remove("condition");
                }
            }

            act.setOutData("condition", map);
            List<Map<String, Object>> planerList = dao1.getUserPoseLise(1, null);
            req.setAttribute("planerList", planerList);
            req.setAttribute("wareHouseList", dao.getWareHouse(logonUser.getUserId()));
            req.setAttribute("curUserId", logonUser.getUserId());
            act.setForword(partPlanConfirmUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, " 计划确认");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanConfirmUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description:查询可审批计划
     */
    public void queryPartPlanConfirm() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPlanConfirmDao dao = PartPlanConfirmDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartPlan(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(partPlanConfirmUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划确认查询失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanConfirmUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 审批页面
     */
    public void partPlanView() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            PartPlanConfirmDao partPlanConfirmDao = PartPlanConfirmDao.getInstance();
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));

            Map<String, Object> mainMap = dao.queryPlanMainView(planId);
            List<Map<String, Object>> list = dao.queryPlanDetailView(planId, partOldCode, partCname, venderId);
            request.setAttribute("mainMap", mainMap);
            List<Map<String, Object>> detailList = new ArrayList();
            for (Map<String, Object> map : list) {
                String partId = map.get("PART_ID").toString();
                Double buyPrice = 0d;
                if (map.get("BUY_PRICE") != null) {
                    buyPrice = ((BigDecimal) map.get("BUY_PRICE")).doubleValue();
                }
                long chkNum = ((BigDecimal) map.get("CHECK_NUM")).longValue();
                List<Map<String, Object>> venderList = partPlanConfirmDao.getVender("", partId);
                List<Map<String, Object>> defaultList = new ArrayList<Map<String, Object>>();
                Map<String, Object> defaultMap = new HashMap<String, Object>();
                for (Map<String, Object> venderMap : venderList) {
                    if (CommonUtils.checkNull(venderMap.get("IS_DEFAULT")).equals(Constant.IF_TYPE_YES + "")) {
                        defaultList.add(venderMap);
                        map.put("defaultVenderId", venderMap.get("VENDER_ID"));
                        break;
                    }
                }
                venderList.removeAll(defaultList);
                String confirmPlanQty = partPlanConfirmDao.getConfirmedNum(planId, partId);
                map.put("confirmPlanQty", confirmPlanQty);
                map.put("venderList", venderList);
                map.put("defaultList", defaultList);
                DecimalFormat df = new DecimalFormat("#.00");
                double buyAmount = Arith.mul(buyPrice, (chkNum - (Long.valueOf(confirmPlanQty).longValue())));
                map.put("buyAmount", df.format(buyAmount));
                if (!"0".equals(map.get("CHECK_NUM").toString())) {
                    detailList.add(map);
                }
            }
            request.setAttribute("detailList", detailList);
            request.setAttribute("partOldCode", partOldCode);
            request.setAttribute("partCname", partCname);
            request.setAttribute("venderId", venderId);
            request.setAttribute("venderName", venderName);
            act.setOutData("ps", null);
            act.setForword(partPlanConfirmViewUrl);

        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(partPlanConfirmViewUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划确认");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanCheckViewUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 确认通过
     */
    public void passTheConfirm() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            Long orderId = Long.parseLong(SequenceManager.getSequence(""));
            PartPlanConfirmDao dao = PartPlanConfirmDao.getInstance();
            try {

                String planId = CommonUtils.checkNull(request.getParamValue("planId"));
                String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
                String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
                String cvenderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
                String planerId = CommonUtils.checkNull(request.getParamValue("planerId"));
                String planerName = CommonUtils.checkNull(request.getParamValue("planerName"));
                String remark = CommonUtils.checkNull(request.getParamValue("REMARK"));//备注

                TtPartPlanMainPO newPo = new TtPartPlanMainPO();
                TtPartPlanMainPO oldPo = new TtPartPlanMainPO();
                //newPo.setPlanId(Long.valueOf(planId));
                newPo.setConfirmBy(logonUser.getUserId());
                newPo.setConfirmDate(new Date());
                oldPo.setPlanId(Long.valueOf(planId));
                PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
                //
                List<Map<String, Object>> detailList = purchasePlanSettingDao.queryPlanDetailView(planId, partOldCode, partCname, cvenderId);
                Map<String, Object> mainMap = purchasePlanSettingDao.queryPlanMainView(planId);

                //插入订单主表 TT_PART_PO_MAIN
                TtPartPoMainPO ttPartPoMainPO = new TtPartPoMainPO();
                String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_02);
                ttPartPoMainPO.setOrderId(orderId);
                ttPartPoMainPO.setOrderCode(orderCode);
                ttPartPoMainPO.setPlanId(Long.valueOf(planId));
                ttPartPoMainPO.setCreateDate(new Date());
                ttPartPoMainPO.setCreateBy(logonUser.getUserId());
                ttPartPoMainPO.setOrgId(logonUser.getOrgId());
                ttPartPoMainPO.setState(Constant.PURCHASE_ORDER_STATE_01);
                ttPartPoMainPO.setBuyer(logonUser.getName());
                ttPartPoMainPO.setBuyerId(logonUser.getUserId());
                ttPartPoMainPO.setRemark(remark);

                if (null != mainMap.get("PRODUCE_FAC")) {
                    ttPartPoMainPO.setProduceFac(Integer.valueOf(mainMap.get("PRODUCE_FAC").toString()));
                }
                if (null != mainMap.get("PLAN_TYPE")) {
                    ttPartPoMainPO.setPlanType(Integer.valueOf(mainMap.get("PLAN_TYPE").toString()));
                }
                if (null != mainMap.get("WH_ID")) {
                    ttPartPoMainPO.setWhId(Long.valueOf(mainMap.get("WH_ID").toString()));
                }
                if (null != mainMap.get("WH_NAME")) {
                    ttPartPoMainPO.setWhName(mainMap.get("WH_NAME").toString());
                }
                if (null != mainMap.get("IS_URGENT_IN")) {
                    ttPartPoMainPO.setIsUrgentIn(((BigDecimal) mainMap.get("IS_URGENT_IN")).intValue());
                }
                if (null != request.getParamValue("SUM_QTY")) {
                    String sumQty = CommonUtils.checkNull(request.getParamValue("SUM_QTY"));
                    if (sumQty.indexOf(".") > -1) {
                        sumQty = sumQty.split("\\.")[0];
                    }
                    ttPartPoMainPO.setSumQty(Long.valueOf(sumQty.replace(",", "")));
                    //ttPartPoMainPO.setSumQty(Long.valueOf(mainMap.get("SUM_QTY").toString()));
                }
                if (null != request.getParamValue("AMOUNT")) {
                    ttPartPoMainPO.setAmount(Double.valueOf(CommonUtils.checkNull(request.getParamValue("AMOUNT").replace(",", ""))));
                    //ttPartPoMainPO.setAmount(Double.valueOf(mainMap.get("AMOUNT").toString()));
                }
               /* if (null != mainMap.get("REMARK")) {
                    ttPartPoMainPO.setRemark(mainMap.get("REMARK").toString());
                }*/
                Long counter = 0l;
                dao.insert(ttPartPoMainPO);
                for (Map<String, Object> map : detailList) {
                    String partId = map.get("PART_ID").toString();
                    //前台通过DISABLED属性控制提交 ，只要判断是否含有，即可取到被选择的数据
                    String buyQty = "";
                    String venderId = "";
                    String plineId = CommonUtils.checkNull(map.get("PLINE_ID"));
                    //校验是否并发  校验VERSION
                    String newVersion = dao.getVersion(plineId);
                    String version = CommonUtils.checkNull(request.getParamValue("VER_" + partId));
                    if ("".equals(version)) {
                        continue;
                    }
                    if (!newVersion.equals(version)) {
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "有其他用户同时操作此记录,请检查后重新提交!");
                        throw e1;
                    }
                    if (null != request.getParamValue("buyQty1_" + partId)) {
                        String amount = CommonUtils.checkNull(request.getParamValue("AMOUNT_" + partId));
                        buyQty = CommonUtils.checkNull(request.getParamValue("buyQty1_" + partId));
                        venderId = CommonUtils.checkNull(request.getParamValue("origin1_" + partId));
                        this.insertDetail(buyQty, venderId, plineId, map, orderId, version, counter, amount);
                    }
                    if (null != request.getParamValue("buyQty2_" + partId)) {
                        String amount = request.getParamValue("AMOUNT_" + partId);
                        buyQty = CommonUtils.checkNull(request.getParamValue("buyQty2_" + partId));
                        venderId = CommonUtils.checkNull(request.getParamValue("origin2_" + partId));
                        this.insertDetail(buyQty, venderId, plineId, map, orderId, version, counter, amount);
                    }
                    if (null != request.getParamValue("buyQty3_" + partId)) {
                        String amount = request.getParamValue("AMOUNT_" + partId);
                        buyQty = CommonUtils.checkNull(request.getParamValue("buyQty3_" + partId));
                        venderId = CommonUtils.checkNull(request.getParamValue("origin3_" + partId));
                        this.insertDetail(buyQty, venderId, plineId, map, orderId, version, counter, amount);
                    }
                }
                //只有数量为0和提交所有详细计划才会让计划完全通过，置换状态@@
                if (Integer.valueOf("1").equals(OrderCodeManager.isOver(Constant.PART_CODE_RELATION_01, planId))) {
                    newPo.setState(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_05);
                    act.setOutData("msg", "finish");
                } else {
                    act.setOutData("msg", "continue");
                }
                dao.update(oldPo, newPo);
                POContext.endTxn(true);
            } catch (Exception e) {
                //回滚事务
                POContext.endTxn(false);
                throw e;
            } finally {
                POContext.cleanTxn();
            }

            //调用存储过程更新采购价格和金额
            // POFactory poFactory = POFactoryBuilder.getInstance();
            List ins = new LinkedList<Object>();
            ins.add(orderId);
            dao.callProcedure("PKG_PART.P_UPDATEOEMPO", ins, null);
            act.setOutData("success", "采购订单生成成功!");
        } catch (Exception e) {

            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setOutData("error", "数据错误!");
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "计划数据错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 确认通过
     */
    public void passTheConfirmDefault() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPlanConfirmDao dao = PartPlanConfirmDao.getInstance();
        try {
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            String partIdArr[] = request.getParamValues("cb");//配件id
            String remark = CommonUtils.checkNull(request.getParamValue("REMARK"));//备注
            String deVenderStr = CommonUtils.checkNull(request.getParamValue("deVenderStr"));//默认供应商

            String deVenderArr[] = deVenderStr.split(",");

            for (int i = 0; i < deVenderArr.length; i++) {
                Long orderId = Long.parseLong(SequenceManager.getSequence(""));

                PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
                Map<String, Object> mainMap = purchasePlanSettingDao.queryPlanMainView(planId);

                //插入订单主表 TT_PART_PO_MAIN
                TtPartPoMainPO ttPartPoMainPO = new TtPartPoMainPO();
                String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_02);
                ttPartPoMainPO.setOrderId(orderId);
                ttPartPoMainPO.setOrderCode(orderCode);
                ttPartPoMainPO.setPlanId(Long.valueOf(planId));
                ttPartPoMainPO.setCreateDate(new Date());
                ttPartPoMainPO.setCreateBy(logonUser.getUserId());
                ttPartPoMainPO.setOrgId(logonUser.getOrgId());
                ttPartPoMainPO.setState(Constant.PURCHASE_ORDER_STATE_01);
                ttPartPoMainPO.setBuyer(logonUser.getName());
                ttPartPoMainPO.setBuyerId(logonUser.getUserId());
                ttPartPoMainPO.setRemark(remark);

                if (null != mainMap.get("PRODUCE_FAC")) {
                    ttPartPoMainPO.setProduceFac(Integer.valueOf(mainMap.get("PRODUCE_FAC").toString()));
                }
                if (null != mainMap.get("PLAN_TYPE")) {
                    ttPartPoMainPO.setPlanType(Integer.valueOf(mainMap.get("PLAN_TYPE").toString()));
                }
                if (null != mainMap.get("WH_ID")) {
                    ttPartPoMainPO.setWhId(Long.valueOf(mainMap.get("WH_ID").toString()));
                }
                if (null != mainMap.get("WH_NAME")) {
                    ttPartPoMainPO.setWhName(mainMap.get("WH_NAME").toString());
                }
                if (null != mainMap.get("IS_URGENT_IN")) {
                    ttPartPoMainPO.setIsUrgentIn(((BigDecimal) mainMap.get("IS_URGENT_IN")).intValue());
                }

                Long sumQty = 0l;
                Double sumAmount = 0d;

                for (int j = 0; j < partIdArr.length; j++) {

                    String deVenderId = CommonUtils.checkNull(request.getParamValue("DEVENDERID_" + partIdArr[j]));//默认供应商

                    if (deVenderId.equals(deVenderArr[i])) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        String plineId = CommonUtils.checkNull(request.getParamValue("PLINE_ID_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String partCname = CommonUtils.checkNull(request.getParamValue("partCname_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String partCode = CommonUtils.checkNull(request.getParamValue("partCode_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String partType = CommonUtils.checkNull(request.getParamValue("partType_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String unit = CommonUtils.checkNull(request.getParamValue("unit_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String planQty = CommonUtils.checkNull(request.getParamValue("planQty_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String buyQty1 = CommonUtils.checkNull(request.getParamValue("buyQty1_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String amount = CommonUtils.checkNull(request.getParamValue("AMOUNT_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String forecastDate = CommonUtils.checkNull(request.getParamValue("FORECAST_DATE_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String dtlRemark = CommonUtils.checkNull(request.getParamValue("REMARK_" + partIdArr[j] + "_" + deVenderArr[i]));
                        String lineNo = CommonUtils.checkNull(request.getParamValue("lineNo_" + partIdArr[j] + "_" + deVenderArr[i]));

                        //校验是否并发  校验VERSION
                        String newVersion = dao.getVersion(plineId);
                        String version = CommonUtils.checkNull(request.getParamValue("VER_" + partIdArr[j] + "_" + deVenderArr[i]));

                        if (!newVersion.equals(version)) {
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "有其他用户同时操作此记录,请检查后重新提交!");
                            throw e1;
                        }

                        TtPartPoDtlPO dtlPo = new TtPartPoDtlPO();
                        Long polineId = Long.parseLong(SequenceManager.getSequence(""));
                        dtlPo.setPolineId(polineId);
                        dtlPo.setOrderId(orderId);
                        if (!"".equals(plineId)) {
                            dtlPo.setPlineId(Long.valueOf(plineId));
                        }
                        if (!"".equals(lineNo)) {
                            dtlPo.setLineNo(Long.valueOf(lineNo));
                        }
                        dtlPo.setPartId(Long.valueOf(partIdArr[j]));
                        dtlPo.setPartCname(partCname);
                        dtlPo.setPartCode(partCode);
                        dtlPo.setPartOldcode(partOldCode);
                        dtlPo.setUnit(unit);
                        dtlPo.setRemark(dtlRemark);
                        if (!"".equals(partType)) {
                            dtlPo.setPartType(Integer.valueOf(partType));
                        }
                        dtlPo.setVenderId(Long.valueOf(deVenderArr[i]));

                        TtPartVenderDefinePO venderDefinePO = new TtPartVenderDefinePO();
                        venderDefinePO.setVenderId(Long.valueOf(deVenderArr[i]));
                        venderDefinePO = (TtPartVenderDefinePO) dao.select(venderDefinePO).get(0);

                        dtlPo.setVenderName(venderDefinePO.getVenderName());

                        TtPartSalesPricePO salesPricePO = new TtPartSalesPricePO();
                        salesPricePO.setPartId(Long.valueOf(partIdArr[j]));
                        salesPricePO.setStatus(1);
                        salesPricePO.setState(Constant.STATUS_ENABLE);
                        salesPricePO = (TtPartSalesPricePO) dao.select(salesPricePO).get(0);

                        if (null != salesPricePO.getSalePrice3()) {
                            dtlPo.setBuyPrice(salesPricePO.getSalePrice3());
                        }
                        if (!"".equals(planQty)) {
                            dtlPo.setPlanQty(Long.valueOf(planQty));
                        }
                        if (!"".equals(forecastDate)) {
                            dtlPo.setForecastDate(sdf.parse(forecastDate));
                        }
                        dtlPo.setBuyQty(Long.valueOf(buyQty1));
                        sumQty += Long.valueOf(buyQty1);
                        if (!"".equals(amount)) {
                            dtlPo.setBuyAmount(Double.valueOf(amount));
                            sumAmount = Arith.add(sumAmount, Double.valueOf(amount));
                        }
                        dtlPo.setSpareQty(Long.valueOf(buyQty1));
                        dtlPo.setCreateBy(logonUser.getUserId());
                        dtlPo.setCreateDate(new Date());
                        dtlPo.setOrgId(logonUser.getOrgId());
                        dtlPo.setLineNo(Long.valueOf(lineNo));
                        dao.insert(dtlPo);

                        TtPartPlanDetailPO newTtPartPlanDetailPO = new TtPartPlanDetailPO();
                        TtPartPlanDetailPO oldTtPartPlanDetailPO = new TtPartPlanDetailPO();
                        oldTtPartPlanDetailPO.setPlineId(Long.valueOf(plineId));
                        newTtPartPlanDetailPO.setVer(Utility.getInt(version) + 1);

                        dao.update(oldTtPartPlanDetailPO, newTtPartPlanDetailPO);
                    }

                }

                ttPartPoMainPO.setSumQty(sumQty);
                ttPartPoMainPO.setAmount(sumAmount);

                dao.insert(ttPartPoMainPO);

                //调用存储过程更新采购价格和金额
                List ins = new LinkedList<Object>();
                ins.add(orderId);
                dao.callProcedure("PKG_PART.P_UPDATEOEMPO", ins, null);
                act.setOutData("success", "采购订单生成成功!");
            }

            TtPartPlanMainPO newPo = new TtPartPlanMainPO();
            TtPartPlanMainPO oldPo = new TtPartPlanMainPO();
            newPo.setConfirmBy(logonUser.getUserId());
            newPo.setConfirmDate(new Date());
            oldPo.setPlanId(Long.valueOf(planId));

            if (Integer.valueOf("1").equals(OrderCodeManager.isOver(Constant.PART_CODE_RELATION_01, planId))) {
                newPo.setState(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_05);
                act.setOutData("msg", "finish");
            } else {
                act.setOutData("msg", "continue");
            }
            dao.update(oldPo, newPo);

        } catch (Exception e) {

            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setOutData("error", "数据错误!");
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "计划数据错误!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void insertDetail(String buyQty, String venderId, String plineId, Map<String, Object> map, Long orderId, String version, Long counter, String amount) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PartPlanConfirmDao dao = PartPlanConfirmDao.getInstance();
        //插入订单明细表
        //add by yuan 20130630     数据过滤
        if (!"0".equals(buyQty) && !"null".equals(buyQty) && !"".equals(buyQty)) {
            TtPartPoDtlPO dtlPo = new TtPartPoDtlPO();
            Long polineId = Long.parseLong(SequenceManager.getSequence(""));
            dtlPo.setPolineId(polineId);
            if (!"".equals(plineId)) {
                dtlPo.setPlineId(Long.valueOf(plineId));
            }
            dtlPo.setPartId(Long.valueOf(map.get("PART_ID").toString()));
            dtlPo.setPartCname(CommonUtils.checkNull(map.get("PART_CNAME")));
            dtlPo.setPartCode(CommonUtils.checkNull(map.get("PART_CODE")));
            dtlPo.setPartOldcode(CommonUtils.checkNull(map.get("PART_OLDCODE")));
            dtlPo.setUnit(CommonUtils.checkNull(map.get("UNIT")));
            dtlPo.setRemark(CommonUtils.checkNull(map.get("REMARK")));
            if (null != map.get("PART_TYPE")) {
                dtlPo.setPartType(Integer.valueOf(map.get("PART_TYPE").toString()));
            }
            if (!"".equals(venderId)) {
                dtlPo.setVenderId(Long.valueOf(venderId));
                List<Map<String, Object>> venderList = dao.getVender(venderId, "");
                if (venderList.size() >= 0) {
                    dtlPo.setVenderName(((Map) venderList.get(0)).get("VENDER_NAME").toString());
                }
            }
            if (null != map.get("BUY_PRICE")) {
                dtlPo.setBuyPrice(Double.valueOf(map.get("BUY_PRICE").toString()));
            }
            if (null != map.get("PLAN_QTY")) {
                dtlPo.setPlanQty(Long.valueOf(map.get("PLAN_QTY").toString()));
            }
            if (null != map.get("FORECAST_DATE")) {
                dtlPo.setForecastDate(sdf.parse(map.get("FORECAST_DATE").toString()));
            }
            dtlPo.setBuyQty(Long.valueOf(buyQty));
            if (!"".equals(amount)) {
                dtlPo.setBuyAmount(Double.valueOf(amount));
            }
            dtlPo.setSpareQty(Long.valueOf(buyQty));
            dtlPo.setCreateBy(Long.valueOf(map.get("CREATE_BY").toString()));
            dtlPo.setCreateDate(new Date());
            dtlPo.setOrderId(Long.valueOf(orderId));
            dtlPo.setOrgId(logonUser.getOrgId());
            dtlPo.setLineNo(Long.valueOf(map.get("LINE_NO").toString()));
            dao.insert(dtlPo);

            //更新详细计划审核数量
            TtPartPlanDetailPO newTtPartPlanDetailPO = new TtPartPlanDetailPO();
            TtPartPlanDetailPO oldTtPartPlanDetailPO = new TtPartPlanDetailPO();
            oldTtPartPlanDetailPO.setPlineId(Long.valueOf(map.get("PLINE_ID").toString()));

            //modify by yuan 20130510
            newTtPartPlanDetailPO.setVer(Utility.getInt(version) + 1);
            //newTtPartPlanDetailPO.setCheckNum(Long.valueOf(map.get("CHECK_NUM").toString()) - Long.valueOf(buyQty));

            counter += Long.valueOf(map.get("CHECK_NUM").toString()) - Long.valueOf(buyQty);
            if (!"".equals(amount)) {
                newTtPartPlanDetailPO.setPlanAmount(Double.valueOf(amount));
            }
            dao.update(oldTtPartPlanDetailPO, newTtPartPlanDetailPO);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 确认驳回
     */
    public void rebutTheConfirm() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            PartPlanCheckDao dao = PartPlanCheckDao.getInstance();
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            String CheckOpinion = CommonUtils.checkNull(request.getParamValue("CheckOpinion"));
            TtPartPlanMainPO newPo = new TtPartPlanMainPO();
            TtPartPlanMainPO oldPo = new TtPartPlanMainPO();
            newPo.setPlanId(Long.valueOf(planId));
            newPo.setState(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_06);
            newPo.setRemark2(CheckOpinion);
            newPo.setConfirmBy(logonUser.getUserId());
            newPo.setConfirmDate(new Date());
            oldPo.setPlanId(Long.valueOf(planId));
            dao.update(oldPo, newPo);
            act.setForword(partPlanConfirmUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(partPlanConfirmUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划确认");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanConfirmUrl);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 作废
     */
    public void disableOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartPlanConfirmDao dao = PartPlanConfirmDao.getInstance();
        try {
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            List params = new ArrayList();

            String sql = "UPDATE TT_PART_PLAN_MAIN T SET T.STATE=? WHERE T.PLAN_ID=?";
            params.add(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_07);
            params.add(planId);
            dao.update(sql, params);
            act.setOutData("success", "作废成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划作废失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }


    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 导出EXCEL
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartPlanQueryDao partPlanQueryDao = PartPlanQueryDao.getInstance();
            PartPlanConfirmDao partPlanCheckDao = PartPlanConfirmDao.getInstance();
            String[] head = new String[12];
            head[0] = "计划单号";
            head[1] = "计划员";
            head[2] = "制单日期";
            head[3] = "库房";
            head[4] = "计划年月";
            head[5] = "计划类型";
            head[6] = "总数量";
            head[7] = "总金额";
            head[8] = "状态";
            head[9] = "提交时间";
            List<Map<String, Object>> list = partPlanCheckDao.queryPartPlan(request);
            Map<String, Object> planTypeMap = partPlanQueryDao.getTcCodeMap(Constant.PART_PURCHASE_PLAN_TYPE.toString());//计划类型
            Map<String, Object> stateMap = partPlanQueryDao.getTcCodeMap(Constant.PART_PURCHASE_PLAN_CHECK_STATUS.toString());//状态
            List list1 = new ArrayList();

            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("PLAN_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("PLANER_NAME"));
                    detail[2] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[3] = CommonUtils.checkNull(map.get("WH_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("YEAR_MONTH"));
                    detail[5] = this.getName(map, "PART_TYPE", planTypeMap); //获取计划类型
                    detail[6] = CommonUtils.checkNull(map.get("SUM_QTY"));
                    detail[7] = CommonUtils.checkNull(map.get("AMOUNT"));
                    detail[8] = this.getName(map, "STATE", stateMap); //获取计划类型
                    detail[9] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                    list1.add(detail);
                }
            }


            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
            act.setForword(partPlanConfirmUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(partPlanConfirmUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划确认");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanConfirmUrl);
        }
    }

    /**
     * @param : @param map
     * @param : @param key
     * @param : @param data
     * @param : @param codeMap
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 获取装载方法
     */
    public String getName(Map map, String key, Map<String, Object> codeMap) {
        if (null == map.get(key)) {
            return "";
        }
        String code = map.get(key).toString();
        return CommonUtils.checkNull(codeMap.get(code));
    }

}
