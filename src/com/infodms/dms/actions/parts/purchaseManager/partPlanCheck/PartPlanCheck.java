package com.infodms.dms.actions.parts.purchaseManager.partPlanCheck;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseManager.partPlanCheck.PartPlanCheckDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanQuery.PartPlanQueryDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPlanDetailPO;
import com.infodms.dms.po.TtPartPlanMainPO;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class PartPlanCheck extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 计划审核初始
     */
    public void partPlanCheckInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper req = act.getRequest();
            PartPlanQueryDao dao = PartPlanQueryDao.getInstance();
            List<Map<String, Object>> planerList = dao.getUserPoseLise(1, null);
            req.setAttribute("planerList", planerList);
            req.setAttribute("wareHouseList", dao.getWareHouse(logonUser.getUserId()));
            req.setAttribute("curUserId", logonUser.getUserId());
            act.setForword(partPlanCheckUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, " 计划审核");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanCheckUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description:查询可审批计划
     */
    public void queryPartPlanCheck() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPlanCheckDao dao = PartPlanCheckDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartPlan(request, curPage, Constant.PAGE_SIZE, logonUser.getUserId());
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(partPlanCheckUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划审核");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanCheckUrl);
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
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            Map<String, Object> mainMap = dao.queryPlanMainView(planId);
            String whId = ((BigDecimal) mainMap.get("WH_ID")).longValue() + "";
            String planType = ((BigDecimal) mainMap.get("PLAN_TYPE")).intValue() + "";
            double planCycle = ((BigDecimal) mainMap.get("PLAN_CYCLE")).doubleValue();
            List<Map<String, Object>> detailList = dao.queryPlanDetailView2(planId, whId, planType, planCycle);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("detailList", detailList);
            act.setOutData("ps", null);
            act.setForword(partPlanCheckViewUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(partPlanCheckViewUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划审核");
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
     * @Description: 审批通过
     */
    public void passTheCheck() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        try {

            PartPlanCheckDao dao = PartPlanCheckDao.getInstance();
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            String CheckOpinion = CommonUtils.checkNull(request.getParamValue("CheckOpinion"));
            String sumQty = CommonUtils.checkNull(request.getParamValue("SUM_QTY"));
            String amount = CommonUtils.checkNull(request.getParamValue("AMOUNT"));
            TtPartPlanMainPO newPo = new TtPartPlanMainPO();
            TtPartPlanMainPO oldPo = new TtPartPlanMainPO();
            newPo.setPlanId(Long.valueOf(planId));
            newPo.setState(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_03);
            newPo.setRemark2(CheckOpinion);
            newPo.setCheckBy(loginUser.getUserId());
            newPo.setCheckDate(new Date());
            if (!"".equals(sumQty)) {
                newPo.setSumQty(Long.valueOf(sumQty.replace(",", "")));
            }
            if (!"".equals(amount)) {
                newPo.setAmount(Double.valueOf(amount.replace(",", "")));
            }
            oldPo.setPlanId(Long.valueOf(planId));
            dao.update(oldPo, newPo);
            //更新可用库存量
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> detailList = purchasePlanSettingDao.queryPlanDetailView1(planId);
            for (Map<String, Object> map : detailList) {
                String partId = CommonUtils.checkNull(map.get("PART_ID"));
                String checkNum = CommonUtils.checkNull(request.getParamValue("CHECK_NUM_" + partId));
                TtPartPlanDetailPO po = new TtPartPlanDetailPO();
                po.setPlineId(Long.valueOf(map.get("PLINE_ID").toString()));
                po.setCheckNum(Long.valueOf(checkNum));
                if (null != request.getParamValue("AMOUNT_" + partId)) {
                    po.setCheckAmount(Double.valueOf(request.getParamValue("AMOUNT_" + partId).toString()));
                }
                TtPartPlanDetailPO oldTtPartPlanDetailPO = new TtPartPlanDetailPO();
                oldTtPartPlanDetailPO.setPlineId(Long.valueOf(map.get("PLINE_ID").toString()));
                purchasePlanSettingDao.update(oldTtPartPlanDetailPO, po);
            }
            //spit to small oem PO by venderID
            List ins = new LinkedList<Object>();
            ins.add(0, planId);
            dao.callProcedure("PKG_PART.P_CREATEOEMPOORDER", ins, null);
            act.setForword(partPlanCheckUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(partPlanCheckUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划审核");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(partPlanCheckUrl);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 审批驳回
     */
    public void rebutTheCheck() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            PartPlanCheckDao dao = PartPlanCheckDao.getInstance();
            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            String CheckOpinion = CommonUtils.checkNull(request.getParamValue("CheckOpinion"));
            TtPartPlanMainPO newPo = new TtPartPlanMainPO();
            TtPartPlanMainPO oldPo = new TtPartPlanMainPO();
            //newPo.setPlanId(Long.valueOf(planId));
            newPo.setState(Constant.PART_PURCHASE_PLAN_CHECK_STATUS_04);
            //newPo.setCheckBy(loginUser.getUserId());
            //newPo.setCheckDate(sdf.parse(now));
            oldPo.setPlanId(Long.valueOf(planId));
            dao.update(oldPo, newPo);
            act.setForword(partPlanCheckUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(partPlanCheckUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划审核");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(partPlanCheckUrl);
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
            PartPlanCheckDao partPlanCheckDao = PartPlanCheckDao.getInstance();
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
            if (list.size() > 0) {
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

                    this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
                }
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
            act.setForword(partPlanCheckUrl);
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

        String name = "配件计划审核列表.xls";
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
}
