package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.parts.baseManager.partsBaseManager.PartBaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtPartDlrOrderMainPO;
import com.infodms.dms.po.TtPartDplanDtlPO;
import com.infodms.dms.po.TtPartDplanMainPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class PartGxPlan extends PartBaseImport implements PTConstants {

    public Logger logger = Logger.getLogger(PartGxPlan.class);
    private static final String PART_GX_PLAN_QUERY = "/jsp/parts/salesManager/carFactorySalesManager/partGXPlanQuery.jsp";
    private static final String PART_GX_CAR_QUERY = "/jsp/parts/salesManager/carFactorySalesManager/partGXCarQuery.jsp";
    private static final String PART_GX_PLAN_MOD = "/jsp/parts/salesManager/carFactorySalesManager/partGXPlanMod.jsp";
    private static final String PART_GX_PLAN_VIEW = "/jsp/parts/salesManager/carFactorySalesManager/partGXPlanView.jsp";
    private static final String PART_OEM_GX_PLAN_ADD = "/jsp/parts/salesManager/carFactorySalesManager/partGXPlanAdd.jsp";

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list,
                                  String tableName) throws Exception {

        String name = tableName + ".xls";
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

    public void partGxPlanInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());

            request.setAttribute("wareHouseList", wareHouseList);
            act.setForword(PART_GX_PLAN_QUERY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 广宣品随车确认查询
     */
    public void partGxCarInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PurchasePlanSettingDao purchasePlanSettingDao =  PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());

            request.setAttribute("wareHouseList", wareHouseList);
            act.setForword(PART_GX_CAR_QUERY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }


    public void queryPartGxPlan() {
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
            ps = dao.queryPartGxPlan(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_GX_PLAN_QUERY);
        }
    }

    public void queryPartGxZCar() {
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
            ps = dao.queryPartGxCar(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品随车确认查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_GX_CAR_QUERY);
        }
    }

    /**
     * 修改页面
     */
    public void modPlanInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());

            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            TtPartDplanMainPO mainPO = new TtPartDplanMainPO();
            mainPO.setPlanId(CommonUtils.parseLong(planId));
            mainPO = (TtPartDplanMainPO) dao.select(mainPO).get(0);

            Long orderId = mainPO.getOrderId();

            TtPartDplanDtlPO dtlPO = new TtPartDplanDtlPO();
            dtlPO.setPlanId(mainPO.getPlanId());
            List detailList = dao.select(dtlPO);

            //查询该发运单对应的订单中的所有订货数量
            Long allBuyQty = dao.getAllBuyQty(orderId);

            TcUserPO userPO = new TcUserPO();
            userPO.setUserId(mainPO.getCreateBy());
            userPO = (TcUserPO) dao.select(userPO).get(0);

            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            request.setAttribute("now", now);

            request.setAttribute("allBuyQty", allBuyQty);
            request.setAttribute("name", userPO.getName());
            request.setAttribute("mainPO", mainPO);
            request.setAttribute("planDate", mainPO.getFinalDate() != null ? sdf.format(mainPO.getFinalDate()) : sdf.format(new Date()));
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("detailList", detailList);
            act.setForword(PART_GX_PLAN_MOD);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划修改失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 查看页面
     */
    public void viewPlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> lo = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(lo.get(0).getOrgId().toString());

            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            TtPartDplanMainPO mainPO = new TtPartDplanMainPO();
            mainPO.setPlanId(CommonUtils.parseLong(planId));
            mainPO = (TtPartDplanMainPO) dao.select(mainPO).get(0);

            TtPartDplanDtlPO dtlPO = new TtPartDplanDtlPO();
            dtlPO.setPlanId(mainPO.getPlanId());
            List detailList = dao.select(dtlPO);


            TcUserPO userPO = new TcUserPO();
            userPO.setUserId(mainPO.getCreateBy());
            userPO = (TcUserPO) dao.select(userPO).get(0);

            request.setAttribute("name", userPO.getName());
            request.setAttribute("mainPO", mainPO);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("detailList", detailList);
            act.setForword(PART_GX_PLAN_VIEW);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划查看失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 修改
     */
    public void modGxPlan() {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id"));
            String outType = CommonUtils.checkNull(request.getParamValue("OUT_TYPE"));
            String finalDate = CommonUtils.checkNull(request.getParamValue("planDate")); //随车最终发运日期
            String remark = CommonUtils.checkNull(request.getParamValue("REMARK2"));
            String[] partIds = request.getParamValues("cb");//所有配件id
            Long allBuyQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("allBuyQty")));//该订单对应的所有订货数量
            String amount = CommonUtils.checkNull(request.getParamValue("partAmount")).replace(",", "");//总金额

            for (int i = 0; i < partIds.length; i++) {
                String partId = partIds[i];

                //验证当前配件的提报数量是否大于订货数量
                Long reportedQty = 0l;//已经提报的数量
                Long buyQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("buyQty_" + partId)));//订货数量
                Long saleQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("saleQty_" + partId)));//页面输入的提报数量
                Long reportQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("reportQty_" + partId)));//已提报数量
                String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode_" + partId));
                String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo_" + partId));
                String dtlRemark = CommonUtils.checkNull(request.getParamValue("remark_" + partId));
                String dlineId = CommonUtils.checkNull(request.getParamValue("dlineId_" + partId));
                String buyPrice = CommonUtils.checkNull(request.getParamValue("buyPrice_" + partId));
                String buyAmount = CommonUtils.checkNull(request.getParamValue("buyAmount_" + partId)).replace(",", "");
                Map<String, Object> map = dao.getReportedQty(orderId, partId);
                if (map != null) {
                    reportedQty = ((BigDecimal) map.get("REPORTED_QTY")).longValue();
                }
                //如果大于订货数量
                if (reportedQty - reportQty + saleQty > buyQty) {
                    BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, partOldCode + "的提报数量不能大于" + (buyQty - reportedQty + reportQty) + "!");
                    throw e;
                }

                TtPartDplanDtlPO dtlPO1 = new TtPartDplanDtlPO();
                TtPartDplanDtlPO dtlPO2 = new TtPartDplanDtlPO();
                dtlPO1.setDlineId(CommonUtils.parseLong(dlineId));
                dtlPO2.setReportQty(saleQty);
                dtlPO2.setPkgNo(pkgNo);
                dtlPO2.setRemark(dtlRemark);
                dtlPO2.setBuyPrice(Double.valueOf(buyPrice));
                dtlPO2.setBuyAmount(Double.valueOf(buyAmount));
                dao.update(dtlPO1, dtlPO2);
            }

            TtPartDplanMainPO mainPO1 = new TtPartDplanMainPO();
            TtPartDplanMainPO mainPO2 = new TtPartDplanMainPO();
            mainPO1.setPlanId(CommonUtils.parseLong(planId));
            mainPO2.setWhId(CommonUtils.parseLong(whId));
            if (!"".equals(outType)) {
                mainPO2.setOutType(CommonUtils.parseInteger(outType));
            } else {
                mainPO2.setOutType(null);
            }
            if (!"".equals(finalDate)) {
                mainPO2.setFinalDate(CommonUtils.parseDTime(finalDate));
            }
            mainPO2.setRemark(remark);
            mainPO2.setAmount(Double.valueOf(amount));
            dao.update(mainPO1, mainPO2);

            TtPartDlrOrderMainPO dlrOrderMainPO1 = new TtPartDlrOrderMainPO();
            TtPartDlrOrderMainPO dlrOrderMainPO2 = new TtPartDlrOrderMainPO();
            dlrOrderMainPO1.setOrderId(CommonUtils.parseLong(orderId));

            //判断当前订单是否已经全部审核完成,如果全部完成就更新状态为已审核
            Long allReportedQty = dao.getReportedQty(orderId);
            if (allReportedQty == allBuyQty) {
                dlrOrderMainPO2.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03);
            } else {
                dlrOrderMainPO2.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);
            }
            dao.update(dlrOrderMainPO1, dlrOrderMainPO2);
            act.setOutData("success", "发运计划修改成功!");
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划修改失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    //保存
    public void saveGxPlan() {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //插入数据
            this.insertPlanMain();
            this.insertPlanDtl();

            act.setOutData("success", "发运计划保存成功!");
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划新增失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    private void insertPlanMain() throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

            Long planId = CommonUtils.parseLong(SequenceManager.getSequence(""));
            request.setAttribute("planId", planId);
            //发运计划单号
            String planCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_40, request.getParamValue("dealerId"));

            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); //订货单位ID
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); //订货单位CODE
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); //订货单位名称
            String sellerId = CommonUtils.checkNull(request.getParamValue("SELLER_ID")); //销售单位ID
            String sellerCode = CommonUtils.checkNull(request.getParamValue("SELLER_CODE")); //销售单位CODE
            String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME")); //销售单位NAME
            String remark2 = CommonUtils.checkNull(request.getParamValue("REMARK2")); //备注
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id")); //出库仓库
            String outType = CommonUtils.checkNull(request.getParamValue("OUT_TYPE")); //发运方式
            String finalDate = CommonUtils.checkNull(request.getParamValue("planDate")); //随车最终发运日期
            String amount = CommonUtils.checkNull(request.getParamValue("partAmount")).replace(",", ""); //随车最终发运日期

            TtPartDplanMainPO po = new TtPartDplanMainPO();
            po.setPlanId(planId);
            po.setPlanCode(planCode);
            po.setDealerId(CommonUtils.parseLong(dealerId));
            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setSellerId(CommonUtils.parseLong(sellerId));
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            po.setCreateDate(new Date());
            po.setCreateBy(loginUser.getUserId());
            po.setWhId(CommonUtils.parseLong(whId));
            po.setAmount(Double.valueOf(amount));
            po.setIsInvo(Constant.IF_TYPE_NO);//主机厂新增不需要开票
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


    private void insertPlanDtl() throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

            Long planId = (Long) request.getAttribute("planId");
            String outType = CommonUtils.checkNull(request.getParamValue("OUT_TYPE")); //发运方式

            String[] partIds = request.getParamValues("cb");//所有配件id

            for (int i = 0; i < partIds.length; i++) {
                String partId = partIds[i];

                Long saleQty = Long.valueOf(CommonUtils.checkNull(request.getParamValue("buyQty_" + partId)));//提报数量
                String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldcode_" + partId));

                Long dLineId = CommonUtils.parseLong(SequenceManager.getSequence(""));
                String partCname = CommonUtils.checkNull(request.getParamValue("partCname_" + partId));
                String partCode = CommonUtils.checkNull(request.getParamValue("partCode_" + partId));
                String unit = CommonUtils.checkNull(request.getParamValue("unit_" + partId));
                String minPackage = CommonUtils.checkNull(request.getParamValue("minPackage_" + partId));
                String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo_" + partId));
                String remark = CommonUtils.checkNull(request.getParamValue("remark_" + partId));
                String salePrice = CommonUtils.checkNull(request.getParamValue("salePrice_" + partId)).replace(",", "");
                String buyAmount = CommonUtils.checkNull(request.getParamValue("buyAmount_" + partId)).replace(",", "");

                TtPartDplanDtlPO po = new TtPartDplanDtlPO();
                po.setDlineId(dLineId);
                po.setPlanId(planId);
                po.setPartId(CommonUtils.parseLong(partId));
                po.setPartOldcode(partOldCode);
                po.setPartCname(partCname);
                po.setPartCode(partCode);
                po.setUnit(unit);
                po.setMinPackage(CommonUtils.parseLong(minPackage));
                po.setBuyQty(saleQty);
                po.setReportQty(saleQty);
                po.setPkgNo(pkgNo);
                po.setCreateDate(new Date());
                po.setCreateBy(loginUser.getUserId());
                po.setRemark(remark);
                po.setBuyPrice(Double.valueOf(salePrice));
                po.setBuyAmount(Double.valueOf(buyAmount));
                if (!"".equals(outType)) {
                    po.setTransType(CommonUtils.parseInteger(outType));
                }

                dao.insert(po);
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    //提交
    public void submitPlan() {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            TtPartDplanMainPO mainPO1 = new TtPartDplanMainPO();
            TtPartDplanMainPO mainPO2 = new TtPartDplanMainPO();
            mainPO1.setPlanId(CommonUtils.parseLong(planId));
            mainPO2.setState(Constant.PART_GX_ORDER_STATE_02);
            dao.update(mainPO1, mainPO2);
            act.setOutData("success", "发运计划提交成功!");
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划提交失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    //随车确认
    public void confirmPlan() {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            TtPartDplanMainPO mainPO1 = new TtPartDplanMainPO();
            TtPartDplanMainPO mainPO2 = new TtPartDplanMainPO();
            mainPO1.setPlanId(CommonUtils.parseLong(planId));
            mainPO2.setState(Constant.PART_GX_ORDER_STATE_03);
            dao.update(mainPO1, mainPO2);
            act.setOutData("success", "广宣品随车确认成功!");
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品随车确认失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    //删除
    public void delPlan() {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

            TtPartDplanMainPO mainPO = new TtPartDplanMainPO();
            mainPO.setPlanId(CommonUtils.parseLong(planId));
            dao.delete(mainPO);

            TtPartDplanDtlPO dtlPO = new TtPartDplanDtlPO();
            dtlPO.setPlanId(CommonUtils.parseLong(planId));
            dao.delete(dtlPO);

            //更新订单状态
            if (!"".equals(orderId) && !"null".equals(orderId) && null != orderId) {
                TtPartDlrOrderMainPO dlrOrderMainPO1 = new TtPartDlrOrderMainPO();
                TtPartDlrOrderMainPO dlrOrderMainPO2 = new TtPartDlrOrderMainPO();
                dlrOrderMainPO1.setOrderId(CommonUtils.parseLong(orderId));
                dlrOrderMainPO2.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);
                dao.update(dlrOrderMainPO1, dlrOrderMainPO2);
            }

            act.setOutData("success", "发运计划删除成功!");
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划删除失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    public void addGxPlanOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            String dealerId = "";
            String dealerCode = "";
            String dealerName = "";
            //判断是否为车厂  PartWareHouseDao

            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);

            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
                dealerCode = beanList.get(0).getOrgCode();
                dealerName = beanList.get(0).getOrgName();

                List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(beanList.get(0).getOrgId().toString(), "2");
                act.setOutData("wareHouseList", wareHouseList);
            }
            if ("".equals(dealerId)) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, " </br>此账户没有操作权限,请联系管理员!");
                throw e1;
            }

            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            dataMap.put("name", loginUser.getName());
            dataMap.put("now", now);
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerCode", dealerCode);
            dataMap.put("dealerName", dealerName);
            dataMap.put("defaultWh", dealerName);

            act.setOutData("dataMap", dataMap);
            act.setForword(PART_OEM_GX_PLAN_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_GX_PLAN_QUERY);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "广宣品发运计划新增失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_GX_PLAN_QUERY);
        }
    }

    public void exportGxCarExcel() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[6];
            head[0] = "计划单号";
            head[1] = "订货单位";
            head[2] = "出库类型";
            head[3] = "出库仓库";
            head[4] = "随车包装号";
            head[5] = "未随车包装号";
            List<Map<String, Object>> list = dao.queryPartGxZCar(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[6];
                        detail[0] = CommonUtils.checkNull(map
                                .get("PLAN_CODE"));
                        detail[1] = CommonUtils
                                .checkNull(map.get("DEALER_NAME"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("OUT_TYPE"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("WH_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("PKG_NO1"));
                        detail[5] = CommonUtils.checkNull(map.get("PKG_NO2"));

                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, "广宣品随车确认信息 ");
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
            act.setForword(PART_GX_CAR_QUERY);
        }

    }

}
