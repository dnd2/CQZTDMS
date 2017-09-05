package com.infodms.dms.actions.parts.purchaseManager.purchasePlanSetting;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmpPartUploadPO;
import com.infodms.dms.po.TtPartPlanDetailPO;
import com.infodms.dms.po.TtPartPlanMainPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 采购计划管理
 * @author hyy 
 * @version 2017-7-4
 * @see 
 * @since 
 * @deprecated
 */
public class PurchasePlanSetting extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    String UPLOADFILE = "/jsp/parts/purchaseManager/purchasePlanSetting/uploadFile.jsp";

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件计划.xls";
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
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 月度计划编制-界面初始化
     */
    public void purchasePlanSettingInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            //库房和人员关联
            List<Map<String, Object>> wareHouseList = dao.getWareHouse(logonUser.getUserId());
            List<Map<String, Object>> planerList = dao.getUserPoseLise(1, null);
            List<Map<String, Object>> venderList = dao.getVenderList();
            RequestWrapper req = act.getRequest();
            req.setAttribute("venderList", venderList);
            req.setAttribute("wareHouseList", wareHouseList);
            req.setAttribute("planerList", planerList);
            req.setAttribute("curUserId", logonUser.getUserId());
            act.setForword(purchasePlanSettingUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划维护失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(purchasePlanSettingUrl);
        }
    }

    public void purchasePlanSettingInit2() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            //库房和人员关联
            List<Map<String, Object>> wareHouseList = dao.getWareHouse(logonUser.getUserId());
            List<Map<String, Object>> planerList = dao.getUserPoseLise(1, null);
            List<Map<String, Object>> venderList = dao.getVenderList();
            RequestWrapper req = act.getRequest();
            req.setAttribute("venderList", venderList);
            req.setAttribute("wareHouseList", wareHouseList);
            req.setAttribute("planerList", planerList);
            req.setAttribute("curUserId", logonUser.getUserId());
            act.setForword(purchasePlanSettingUrl2);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划维护失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(purchasePlanSettingUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 查询计划
     */
    public void queryPurchasePlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPurchasePlan(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(purchasePlanSettingUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "计划维护失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(purchasePlanSettingUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-9
     * @Title :
     * @Description: 新增
     */
    public void addPurchasePlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();

        try {
            String plan_type = req.getParamValue("planType");
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = dao.getWareHouse(logonUser.getUserId());
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            String planCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_01);//获取单据编码
            req.setAttribute("wareHouseList", wareHouseList);
            req.setAttribute("name", logonUser.getName());
            req.setAttribute("now", now);
            req.setAttribute("planCode", planCode);
            req.setAttribute("beginTime2", 1);
            if (plan_type.equals(Constant.PART_PURCHASE_PLAN_TYPE_01.toString())) {
                act.setForword(purchasePlanSettingAddUrl);
            } else {
                act.setForword(purchasePlanSettingAddUrl2);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "计划新增失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(purchasePlanSettingAddUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 提交
     */
    public void partPlanCheckCommit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            String planIds = CommonUtils.checkNull(req.getParamValue("planIds"));
            String plan_type = CommonUtils.checkNull(req.getParamValue("PLAN_TYPE"));
            String[] planArr = planIds.split(",");
            for (int i = 0; i < planArr.length; i++) {

                TtPartPlanMainPO oldMainPO = new TtPartPlanMainPO();
                oldMainPO.setPlanId(Long.valueOf(planArr[i]));

                TtPartPlanMainPO mainPO = new TtPartPlanMainPO();
                mainPO.setState(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_02);
                mainPO.setSubmitBy(logonUser.getUserId());
                mainPO.setSubmitDate(new Date());

                PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
                dao.update(oldMainPO, mainPO);
            }
            if (plan_type.equals(Constant.PART_PURCHASE_PLAN_TYPE_01.toString())) {
                act.setForword(purchasePlanSettingUrl);
            } else {
                act.setForword(purchasePlanSettingUrl2);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划维护提交失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(purchasePlanSettingUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 查询配件
     */
    public void showPartBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            //分页方法 begin
            Integer curPage = req.getParamValue("curPage") != null ? Integer
                    .parseInt(req.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.showPartBase(req, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划维护失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 保存计划
     */
    public void savePlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            String[] partIdArr = req.getParamValues("cb");
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            List<Map<String, Object>> wareHouseList = dao.getWareHouse(logonUser.getUserId());
            Long planId = Long.parseLong(SequenceManager.getSequence(""));
           /* String year = CommonUtils.checkNull(req.getParamValue("YEAR"));
            String month = CommonUtils.checkNull(req.getParamValue("MONTH"));
            String yearMonth = year + "-" + month + "-01";*///计划年月
            String yearMonth = CommonUtils.checkNull(req.getParamValue("beginTime2"));//计划日期
            String planType = CommonUtils.checkNull(req.getParamValue("planType"));   //计划类型
            String planState = CommonUtils.checkNull(req.getParamValue("planState"));
            String remark = CommonUtils.checkNull(req.getParamValue("remark"));  //备注
            String planCycle = CommonUtils.checkNull(req.getParamValue("PLAN_CYCLE"));  //订货周期
            String comeCycle = CommonUtils.checkNull(req.getParamValue("COME_CYCLE"));  //到货周期
            String isUrgentIn = CommonUtils.checkNull(req.getParamValue("IS_URGENT_IN"));  //是否紧急入库
            Long createBy = logonUser.getUserId(); //制单人，计划人
            Long orgId = logonUser.getCompanyId(); //制单单位
            String planCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_01);//获取单据编码
            //保存主计划
            TtPartPlanMainPO mainPO = new TtPartPlanMainPO();
            mainPO.setPlanId(planId);
            mainPO.setPlanCode(planCode);
            mainPO.setYearMonth(sdf.parse(yearMonth));
            mainPO.setPlanType(Integer.valueOf(planType));
            if (null != req.getParamValue("wh_id")) {
                mainPO.setWhName(this.getWhName(wareHouseList, req.getParamValue("wh_id")));
                mainPO.setWhId(Long.valueOf(req.getParamValue("wh_id")));
            }

            if (null != req.getParamValue("SUM_QTY")) {
                mainPO.setSumQty(Long.valueOf(req.getParamValue("SUM_QTY")));
            }
            if (null != req.getParamValue("AMOUNT")) {
                mainPO.setAmount(Double.valueOf(req.getParamValue("AMOUNT")));
            }
            mainPO.setCreateBy(createBy);
            mainPO.setPlanerId(createBy);
            mainPO.setPlanerName(logonUser.getName());
            mainPO.setOrgId(orgId);
            mainPO.setCreateDate(new Date());
            mainPO.setRemark(remark);
            mainPO.setPlanCycle(CommonUtils.parseDouble(planCycle));
            mainPO.setComeCycle(CommonUtils.parseDouble(comeCycle));
            mainPO.setCreateType(Constant.PART_PURCHASE_PLAN_CREATE_TYPE_02);
            mainPO.setState(Integer.valueOf(planState));//一般保存计划
            if (!"".equals(isUrgentIn)) {
                mainPO.setIsUrgentIn(CommonUtils.parseInteger(isUrgentIn));
            }
            //提交计划
            if (planState.equals(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_02.toString())) {
                mainPO.setSubmitBy(logonUser.getUserId());
                mainPO.setSubmitDate(new Date());
                mainPO.setState(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_02);
            }
            dao.insert(mainPO);
            if (partIdArr != null) {
                for (int i = 0; i < partIdArr.length; i++) {
                    String partId = partIdArr[i];
                    Long plineId = Long.parseLong(SequenceManager.getSequence(""));
                    String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + partId));//件号
                    String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId));   //配件编码
                    String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + partId)); //配件名称
                    String partType = CommonUtils.checkNull(req.getParamValue("partType_" + partId)); //配件属性
                    String venderId = CommonUtils.checkNull(req.getParamValue("venderId_" + partId)); //供应商id
                    String unit = CommonUtils.checkNull(req.getParamValue("unit_" + partId)); //单位
                    String minPackage = CommonUtils.checkNull(req.getParamValue("minPackage_" + partId)); //最小包装量
                    String salePrice3 = CommonUtils.checkNull(req.getParamValue("salePrice3_" + partId));//计划价
                    String planQty = CommonUtils.checkNull(req.getParamValue("plan_qty_" + partId));    //计划数量
                    String planAmount = CommonUtils.checkNull(req.getParamValue("planAmount_" + partId)); //计划金额
                    String itemQty = CommonUtils.checkNull(req.getParamValue("itemQty_" + partId)); //可用库存
                    String boQty = CommonUtils.checkNull(req.getParamValue("boQty_" + partId));//BO量
                    String avgQty = CommonUtils.checkNull(req.getParamValue("avgQty_" + partId)); //月均销量
                    String yearQty = CommonUtils.checkNull(req.getParamValue("yearQty_" + partId)); //年均销量
                    String hfyearQty = CommonUtils.checkNull(req.getParamValue("hfyearQty_" + partId)); //半年均销量
                    String quarterQty = CommonUtils.checkNull(req.getParamValue("quarterQty_" + partId)); //季均销量
                    String orderQty = CommonUtils.checkNull(req.getParamValue("orderQty_" + partId));//在途数量
                    String safetyStock = CommonUtils.checkNull(req.getParamValue("safetyStock_" + partId));//安全库存
                    String preArriveDate = CommonUtils.checkNull(req.getParamValue("preArriveDate_" + partId)); //预计到货日期
                    String detailRemark = CommonUtils.checkNull(req.getParamValue("remark_" + partId)); //备注

                    TtPartPlanDetailPO po = new TtPartPlanDetailPO();
                    po.setPlineId(plineId);
                    po.setPlanId(planId);
                    po.setLineNo(i + 1l);
                    po.setPartId(Long.valueOf(partId));
                    po.setPartCode(partCode);
                    po.setPartOldcode(partOldcode);
                    po.setPartCname(partCname);
                    if (!"".equals(partType)) {
                        po.setPartType(CommonUtils.parseInteger(partType));
                    }
                    po.setUnit(unit);
                    if (!"".equals(venderId)) {
                        po.setVenderId(CommonUtils.parseLong(venderId));
                    }
                    po.setMinPackage(Long.valueOf(minPackage));
                    po.setPlanPrice(Double.valueOf(salePrice3.replaceAll(",", "")));
                    po.setPlanQty(Long.valueOf(planQty));
                    po.setPlanAmount(Double.valueOf(planAmount.replaceAll(",", "")));
                    po.setStockQty(Long.valueOf(itemQty));
                    po.setBoQty(Long.valueOf(boQty));
                    po.setAvgQty(Double.valueOf(avgQty));
                    po.setYearQty(Double.valueOf(yearQty));
                    po.setHfyearQty(Double.valueOf(hfyearQty));
                    po.setQuarterQty(Double.valueOf(quarterQty));
                    po.setYearQty(Double.valueOf(avgQty));
                    po.setZtNum(Long.valueOf(orderQty));
                    po.setSfateStock(Long.valueOf(safetyStock));
                    po.setForecastDate(sdf1.parse(preArriveDate));
                    po.setRemark(detailRemark);
                    po.setVer(1);
                    po.setCreateBy(createBy);
                    po.setOrgId(orgId);
                    po.setCreateDate(new Date());
                    dao.insert(po);
                }
            }
            act.setOutData("success", "保存成功!");
            if (planType.equals(Constant.PART_PURCHASE_PLAN_TYPE_01.toString())) {
                act.setForword(purchasePlanSettingUrl);
            } else {
                act.setForword(purchasePlanSettingUrl2);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划保存失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("error", "保存失败!");
        }
    }

    private String getWhName(List<Map<String, Object>> whList, String id) {
        String whName = "";
        for (Map<String, Object> wh : whList) {
            if (wh.get("WH_ID").toString().equals(id)) {
                whName = wh.get("WH_NAME").toString();
                break;
            }
        }
        return whName;
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-12
     * @Title :
     * @Description: 详细 修改 删除
     */
    public void partPlanActions() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            String forword = "";
            String flag = CommonUtils.checkNull(req.getParamValue("flag"));
            String planId = CommonUtils.checkNull(req.getParamValue("planId"));
            String plan_type = CommonUtils.checkNull(req.getParamValue("planType"));
            if (flag.equals("view")) {
                forword = purchasePlanSettingViewUrl;
                req.setAttribute("flag", flag);
                Map<String, Object> mainMap = dao.queryPlanMainView(planId);
                List<Map<String, Object>> detailList = dao.queryPlanDetailView1(planId);
                req.setAttribute("mainMap", mainMap);
                req.setAttribute("detailList", detailList);

            } else if (flag.equals("mod")) {
                forword = purchasePlanSettingModUrl;
                req.setAttribute("flag", flag);
                Map<String, Object> mainMap = dao.queryPlanMainView(planId);
                List<Map<String, Object>> detailList = dao.queryPlanDetailView1(planId);
                List<Map<String, Object>> wareHouseList = dao.getWareHouse(logonUser.getUserId());
                req.setAttribute("wareHouseList", wareHouseList);
                req.setAttribute("mainMap", mainMap);
                req.setAttribute("detailList", detailList);
            } else if (flag.equals("del")) {
                TtPartPlanMainPO po = new TtPartPlanMainPO();
                TtPartPlanDetailPO detailPo = new TtPartPlanDetailPO();
                detailPo.setPlanId(Long.valueOf(planId));
                po.setPlanId(Long.valueOf(planId));
                dao.delete(po);
                dao.delete(detailPo);
                if (plan_type.equals(Constant.PART_PURCHASE_PLAN_TYPE_01.toString())) {
                    act.setForword(purchasePlanSettingAddUrl);
                } else {
                    act.setForword(purchasePlanSettingAddUrl2);
                }
            }
            act.setForword(forword);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划维护失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(purchasePlanSettingUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title :
     * @Description: 删除计划
     */
    public void deletePlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();

            String planId = CommonUtils.checkNull(req.getParamValue("planId"));

            TtPartPlanMainPO po = new TtPartPlanMainPO();
            TtPartPlanDetailPO detailPo = new TtPartPlanDetailPO();
            detailPo.setPlanId(Long.valueOf(planId));
            po.setPlanId(Long.valueOf(planId));
            dao.delete(po);
            dao.delete(detailPo);
            act.setOutData("success", "删除成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划删除失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 修改
     */
    public void modifyPlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            Long planId = Long.valueOf(req.getParamValue("planId"));
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> detailList = dao.queryPlanDetailView1(planId.toString());
            //delete detail
            TtPartPlanDetailPO delPo = new TtPartPlanDetailPO();
            delPo.setPlanId(Long.valueOf(planId));
            dao.delete(delPo);

            String[] partIdArr = req.getParamValues("cb");
            //Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            List<Map<String, Object>> wareHouseList = dao.getWareHouse(logonUser.getUserId());

            /*String year = CommonUtils.checkNull(req.getParamValue("YEAR"));
            String month = CommonUtils.checkNull(req.getParamValue("MONTH"));
            String yearMonth = year + "-" + month + "-01";*///计划年月
            String yearMonth = CommonUtils.checkNull(req.getParamValue("beginTime2"));//计划日期
            String planType = CommonUtils.checkNull(req.getParamValue("planType"));   //计划类型
            String remark = CommonUtils.checkNull(req.getParamValue("remark"));  //备注
            String planState = CommonUtils.checkNull(req.getParamValue("planState"));//状态
            Long orgId = logonUser.getCompanyId(); //制单单位

            TtPartPlanMainPO mainPO = new TtPartPlanMainPO();
            TtPartPlanMainPO oldMainPO = new TtPartPlanMainPO();
            oldMainPO.setPlanId(planId);
            //modify  by yuan 20130510 不需要
            //mainPO.setPlanId(planId);
            //mainPO.setPlanCode(planId.toString());
            mainPO.setYearMonth(sdf.parse(yearMonth));
            mainPO.setPlanType(Integer.valueOf(planType));
            if (null != req.getParamValue("wh_id")) {
                mainPO.setWhName(this.getWhName(wareHouseList, req.getParamValue("wh_id")));
                mainPO.setWhId(Long.valueOf(req.getParamValue("wh_id")));
            }

            if (null != req.getParamValue("SUM_QTY")) {
                mainPO.setSumQty(Long.valueOf(req.getParamValue("SUM_QTY")));
            }
            if (null != req.getParamValue("AMOUNT")) {
                mainPO.setAmount(Double.valueOf(req.getParamValue("AMOUNT")));
            }
            //modify by yuan 20130508        不需要更新以下信息
            //mainPO.setCreateBy(createBy);
            //mainPO.setPlanerName(logonUser.getName());
            //mainPO.setPlanerId(createBy);
            //mainPO.setOrgId(orgId);
            //mainPO.setCreateDate(sdf.parse(now));
            mainPO.setRemark(remark);
            mainPO.setUpdateBy(logonUser.getUserId());
            mainPO.setUpdateDate(new Date());
            //mainPO.setCreateType(Constant.PART_PURCHASE_PLAN_CREATE_TYPE_02);
            mainPO.setState(Integer.valueOf(planState));

            dao.update(oldMainPO, mainPO);


            if (partIdArr != null) {
                for (int i = 0; i < partIdArr.length; i++) {
                    String partId = partIdArr[i];
                    Long plineId = Long.parseLong(SequenceManager.getSequence(""));
                    String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + partId));//件号
                    String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId));   //配件编码
                    String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + partId)); //配件名称
                    String unit = CommonUtils.checkNull(req.getParamValue("unit_" + partId)); //单位
                    String minPackage = CommonUtils.checkNull(req.getParamValue("minPackage_" + partId)); //最小包装量
                    String salePrice3 = CommonUtils.checkNull(req.getParamValue("salePrice3_" + partId));//计划价
                    String planQty = CommonUtils.checkNull(req.getParamValue("plan_qty_" + partId));    //计划数量
                    String planAmount = CommonUtils.checkNull(req.getParamValue("planAmount_" + partId)); //计划金额
                    String itemQty = CommonUtils.checkNull(req.getParamValue("itemQty_" + partId)); //可用库存
                    String boQty = CommonUtils.checkNull(req.getParamValue("boQty_" + partId));//BO量
                    String avgQty = CommonUtils.checkNull(req.getParamValue("avgQty_" + partId)); //月均销量
                    String orderQty = CommonUtils.checkNull(req.getParamValue("orderQty_" + partId));//在途数量
                    String safetyStock = CommonUtils.checkNull(req.getParamValue("safetyStock_" + partId));//安全库存
                    String preArriveDate = CommonUtils.checkNull(req.getParamValue("preArriveDate_" + partId)); //预计到货日期
                    String detailRemark = CommonUtils.checkNull(req.getParamValue("remark_" + partId)); //备注

                    TtPartPlanDetailPO po = new TtPartPlanDetailPO();
                    po.setPlineId(plineId);
                    po.setPlanId(planId);
                    po.setPartId(Long.valueOf(partId));
                    po.setPartCode(partCode);
                    po.setPartOldcode(partOldcode);
                    po.setPartCname(partCname);
                    po.setUnit(unit);
                    po.setLineNo(i + 1l);
                    po.setMinPackage(Long.valueOf(minPackage));
                    po.setPlanPrice(Double.valueOf(salePrice3));
                    po.setPlanQty(Long.valueOf(planQty));
                    po.setPlanAmount(Double.valueOf(planAmount));
                    po.setStockQty(Long.valueOf(itemQty));
                    po.setBoQty(Long.valueOf(boQty));
                    po.setAvgQty(Double.valueOf(avgQty));
                    po.setZtNum(Long.valueOf(orderQty));
                    po.setSfateStock(Long.valueOf(safetyStock));
                    po.setForecastDate(sdf1.parse(preArriveDate));
                    po.setRemark(detailRemark);
                    po.setCreateBy(logonUser.getUserId());
                    po.setOrgId(orgId);
                    po.setCreateDate(new Date());
                    dao.insert(po);
                }
            }
            //提交计划
            if (planState.equals(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_02.toString())) {
                mainPO.setSubmitBy(logonUser.getUserId());
                mainPO.setSubmitDate(new Date());
                mainPO.setState(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_02);
            }
            act.setOutData("success", "修改成功!");
            act.setOutData("planType", planType);
            if (planType.equals(Constant.PART_PURCHASE_PLAN_TYPE_01.toString())) {
                act.setForword(purchasePlanSettingUrl);
            } else {
                act.setForword(purchasePlanSettingUrl2);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划修改失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(purchasePlanSettingUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 导出EXECEL模板
     * @Description: TODO
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
            listHead.add("计划量");
            listHead.add("备注");
            list.add(listHead);
            // 导出的文件名
            String fileName = "配件计划维护模板.xls";
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
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            String[] head = new String[12];
            head[0] = "计划单号";
            head[1] = "计划员";
            head[2] = "制单日期";
            head[3] = "库房";
            head[4] = "计划年月";
            head[5] = "计划类型";
            head[6] = "总数量";
            head[7] = "总金额";
            head[8] = "生成方式";

            List<Map<String, Object>> list = dao.queryPartPlan(request);
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
                    detail[5] = CommonUtils.checkNull(map.get("PART_TYPE_NAME"));
                    detail[6] = CommonUtils.checkNull(map.get("SUM_QTY"));
                    detail[7] = CommonUtils.checkNull(map.get("AMOUNT"));
                    detail[8] = CommonUtils.checkNull(map.get("CREATE_TYPE"));
                    list1.add(detail);
                }

                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
            }

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
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
        PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();

        String planCode = request.getParamValue("planCode");
        String planName = request.getParamValue("planName");
        String createDate = request.getParamValue("createDate");
        String wh_id = request.getParamValue("wh_id");
        String remark = request.getParamValue("remark");
        String beginTime2 = request.getParamValue("beginTime2");
        String planCycle = request.getParamValue("PLAN_CYCLE");
        String comeCycle = request.getParamValue("COME_CYCLE");

        List<Map<String, Object>> wareHouseList = dao.getWareHouse(logonUser.getUserId());
        request.setAttribute("wareHouseList", wareHouseList);
        request.setAttribute("planCode", planCode);
        request.setAttribute("name", planName);
        request.setAttribute("now", createDate);
        request.setAttribute("wh_id", wh_id);
        request.setAttribute("remark", remark);
        request.setAttribute("beginTime2", beginTime2);
        request.setAttribute("planCycle", planCycle);
        request.setAttribute("comeCycle", comeCycle);

        InputStream is = null;
        act.setOutData("flag", true);
        String plan_type = request.getParamValue("planType");
        try {
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

            //验证临时表中的数据,并关联其他表获取需要的数据
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            list = dao.getPartInfo(request);

            act.setOutData("list", list);

            if (plan_type.equals(Constant.PART_PURCHASE_PLAN_TYPE_01.toString())) {
                act.setForword(purchasePlanSettingAddUrl);
            } else {
                act.setForword(purchasePlanSettingAddUrl2);
            }

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
            if (plan_type.equals(Constant.PART_PURCHASE_PLAN_TYPE_01.toString())) {
                act.setForword(purchasePlanSettingAddUrl);
            } else {
                act.setForword(purchasePlanSettingAddUrl2);
            }
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
                parseCells(key, cells, errorInfo);
                if (errorInfo.length() > 0) {
                    break;
                }
            }
        }
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :循环获取CELL生成VO
     * @Description: TODO
     */
    /*private Map<String, Object> loadDataList(List<Map<String, Cell[]>> list) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList();
        Map<String, Object> rtnMap = new HashMap();
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
                try {
                    parseCells(dataList, key, cells);
                } catch (Exception e) {
                    if (e instanceof BizException) {
                        BizException e1 = (BizException) e;
                        error += "上传文件的第" + ((i + 1) + "") + "行," + e1.getMessage().replaceAll("操作失败！失败原因：", "") + "</br>";
                        continue;
                    } else {
                        throw e;
                    }
                }
            }
        }
        rtnMap.put("dataList", dataList);
        rtnMap.put("error", error);
        return rtnMap;
    }*/

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
    private void parseCells(String rowNum, Cell[] cells, StringBuffer errorInfo) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
        RequestWrapper request = act.getRequest();

        String partOldCode = "";
        String planQty = "";
        String remark = "";
        if (cells.length == 3) {
            partOldCode = CommonUtils.checkNull(cells[0].getContents().trim());//配件编码
            planQty = CommonUtils.checkNull(cells[1].getContents());//计划数量
            remark = CommonUtils.checkNull(cells[2].getContents().replace(".", ""));//备注
        } else {
            partOldCode = CommonUtils.checkNull(cells[0].getContents().trim());//配件编码
            planQty = CommonUtils.checkNull(cells[1].getContents());//计划数量
            remark = ".";
        }
        if ("" == partOldCode) {
            errorInfo.append("第" + rowNum + "行的配件编码不能为空,请修改后再上传!");
            return;
        }

        TmpPartUploadPO po = new TmpPartUploadPO();
        po.setPartOldcode(partOldCode);
        po.setOrgId(loginUser.getOrgId() == null ? CommonUtils.parseLong(loginUser.getDealerId()) : loginUser.getOrgId());
        try {
            po.setQty(CommonUtils.parseLong(planQty));
        } catch (Exception e) {
            errorInfo.append("第" + rowNum + "行的计划量无效,请修改后再上传!");
            return;
        }
        po.setRemark(remark);
        po.setWhId(CommonUtils.parseLong(request.getParamValue("wh_id")));
        dao.insert(po);

    }

    public List<Map<String, Cell[]>> readExcel(InputStream is, int rowSize) throws Exception {
        List<Map<String, Cell[]>> list = new ArrayList();
        Sheet sheet = null;
        try {
            Workbook wb = Workbook.getWorkbook(is);
            int len = is.read();
            // 如果多页则遍历
            Sheet[] sheets = wb.getSheets();
            for (int i = 0; i < sheets.length; i++) {  //页
                sheet = sheets[i];
                for (int j = 1; j < sheet.getRows(); j++) {  //行
                    Map<String, Cell[]> map = new HashMap<String, Cell[]>();
                    Cell[] cells = sheet.getRow(j);
                    map.put(j + "", cells);
                    list.add(map);
                }
            }
            return list;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void batchAddPlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
        try {
            String partType = CommonUtils.checkNull(request.getParamValue("partType"));
            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id"));
            String planType = CommonUtils.checkNull(request.getParamValue("planType"));
            String planCycle = CommonUtils.checkNull(request.getParamValue("planCycle"));
            String arriveCycle = CommonUtils.checkNull(request.getParamValue("arriveCycle"));
            String boRadio = CommonUtils.checkNull(request.getParamValue("boRadio"));
            String boDateF = CommonUtils.checkNull(request.getParamValue("checkSDate"));
            String boDateE = CommonUtils.checkNull(request.getParamValue("checkEDate"));
            String filtNeg = CommonUtils.checkNull(request.getParamValue("filtNeg"));
            String plannerId = CommonUtils.checkNull(request.getParamValue("plannerId"));

            String loginId = loginUser.getUserId() + "";
            if (partType == "null" || "".equals(partType)) {
                partType = "0";
            }
            if (venderId == "null" || "".equals(venderId)) {
                venderId = "0";
            }
            if (whId == "null" || "".equals(whId)) {
                whId = "0";
            }
            if (plannerId == "null" || "".equals(plannerId)) {
                plannerId = "0";
            }
           /* ArrayList ins = new ArrayList();
            ins.add(0, partType);
            ins.add(1, venderId);
            ins.add(2, loginId);
            ins.add(3, whId);
            ins.add(4, planType);
            ins.add(5, orderCount);
            dao.callProcedure("PKG_PART.P_PARTCREATEBATCHPLAN", ins, null);*/

            Connection conn = DBService.getInstance().getConnection();
            CallableStatement c = conn.prepareCall("{call PKG_PART.P_PARTCREATEBATCHPLAN2(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            c.setString(1, partType);
            c.setString(2, venderId);
            c.setString(3, loginId);
            c.setString(4, whId);
            c.setString(5, planType);
            c.setString(6, planCycle);
            c.setString(7, arriveCycle);
            c.setString(8, boRadio);
            c.setString(9, boDateF);
            c.setString(10, boDateE);
            c.setString(11, filtNeg);
            c.setString(12, plannerId);//计划员
            c.registerOutParameter(13, java.sql.Types.INTEGER);
            c.execute();
            act.setOutData("success", "根据选择条件，共批量生成" + c.getInt(13) + "张计划单！");
            POContext.endTxn(true);
        } catch (Exception e) {//异常方法
            act.setOutData("error", "保存失败!");
            if (e instanceof BizException) {
                POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "批量生成计划出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        } finally {
            try {
                POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "批量生成计划出错,请联系管理员!");
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title :
     * @Description: 预计到货查询初始化
     */
    public void foreCastPartQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(foreCastPartQueryUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "预计到货查询失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title :
     * @Description: 预计到货查询
     */
    public void queryForeCastPartInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
        try {
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String foreCastBeginTime = CommonUtils.checkNull(request.getParamValue("foreCastBeginTime"));//开始时间
            String foreCastEndTime = CommonUtils.checkNull(request.getParamValue("foreCastEndTime"));//结束时间
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryForeCastPartList(partOldCode, partName, partCode, foreCastBeginTime,
                    foreCastEndTime, curPage, Constant.PAGE_SIZE);
            // 分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "预计到货");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-21
     * @Title :
     * @Description: 导出计划明细
     */
    public void expDtl() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
        RequestWrapper request = act.getRequest();
        String planId = CommonUtils.checkNull(request.getParamValue("planId"));
        String flag = CommonUtils.checkNull(request.getParamValue("flag"));
        try {
            String[] head = new String[21];
            head[0] = "配件编码";
            head[1] = "配件名称";
            head[2] = "件号";
            head[3] = "单位";
            head[4] = "最小包装量";
            head[5] = "计划价";
            head[6] = "计划量";
            head[7] = "计划金额";
            head[8] = "可用库存量";
            head[9] = "BO量";
            head[10] = "月均销量";
            head[11] = "年平均销量";
            head[12] = "半年平均销量";
            head[13] = "季平均销量";
            head[14] = "在途量";
            head[15] = "安全库存量";
            head[16] = "配件属性 ";
            head[17] = "供应商";
            head[18] = "预计到货日期";
            head[19] = "备注";
            List<Map<String, Object>> list = dao.queryPlanDetailView1(planId);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[21];
                        detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[4] = CommonUtils.checkNull(map.get("MIN_PACKAGE"));
                        detail[5] = CommonUtils.checkNull(map.get("PLAN_PRICE"));
                        detail[6] = CommonUtils.checkNull(map.get("PLAN_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("PLAN_AMOUNT"));
                        detail[8] = CommonUtils.checkNull(map.get("STOCK_QTY"));
                        detail[9] = CommonUtils.checkNull(map.get("BO_QTY"));
                        detail[10] = CommonUtils.checkNull(map.get("AVG_QTY"));
                        detail[11] = CommonUtils.checkNull(map.get("YEAR_QTY"));
                        detail[12] = CommonUtils.checkNull(map.get("HFYEAR_QTY"));
                        detail[13] = CommonUtils.checkNull(map.get("QUARTER_QTY"));
                        detail[14] = CommonUtils.checkNull(map.get("ZT_NUM"));
                        detail[15] = CommonUtils.checkNull(map.get("SFATE_STOCK"));
                        detail[16] = CommonUtils.checkNull(map.get("PARTTYPENAME"));
                        detail[17] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[18] = CommonUtils.checkNull(map.get("FORECAST_DATE"));
                        detail[19] = CommonUtils.checkNull(map.get("REMARK"));

                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1);
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
            if ("1".equals(flag)) {
                act.setForword(purchasePlanSettingUrl);
            } else {
                act.setForword(purchasePlanSettingUrl2);
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-21
     * @Title : 批量新增初始化
     */
    public void batchAddInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();
            //库房和人员关联
            List<Map<String, Object>> wareHouseList = dao.getWareHouse(logonUser.getUserId());
            List<Map<String, Object>> planerList = dao.getUserPoseLise(1, null);
            List<Map<String, Object>> venderList = dao.getVenderList();
            RequestWrapper req = act.getRequest();
            req.setAttribute("venderList", venderList);
            req.setAttribute("wareHouseList", wareHouseList);
            req.setAttribute("planerList", planerList);
            act.setForword(purchasePlanBatchAddUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件计划维护失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(purchasePlanSettingUrl);
        }
    }
}
