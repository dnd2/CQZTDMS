package com.infodms.dms.actions.parts.purchaseManager.partPlanQuery;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseManager.partPlanQuery.PartPlanQueryDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
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

public class PartPlanQuery extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(PartPlanQuery.class);

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 计划查询初始
     */
    public void partPlanQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper req = act.getRequest();
            PartPlanQueryDao dao = PartPlanQueryDao.getInstance();
            List<Map<String, Object>> planerList = dao.getUserPoseLise(1, null);
            req.setAttribute("wareHouseList", dao.getWareHouse(logonUser.getUserId()));
            req.setAttribute("planerList", planerList);
            act.setForword(partPlanQueryUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, " 计划查询");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanQueryUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title :
     * @Description: 计划查询
     */
    public void queryPartPlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPlanQueryDao dao = PartPlanQueryDao.getInstance();
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
                act.setForword(partPlanQueryUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划查询失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanQueryUrl);
        }
    }

    public void partPlanView() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PurchasePlanSettingDao dao = PurchasePlanSettingDao.getInstance();

            String planId = CommonUtils.checkNull(request.getParamValue("planId"));
            Map<String, Object> mainMap = dao.queryPlanMainView(planId);
            List<Map<String, Object>> detailList = dao.queryPlanDetailView1(planId);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("detailList", detailList);
            act.setOutData("ps", null);
            act.setForword(partPlanQueryViewUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(partPlanQueryViewUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划查询明细");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(partPlanQueryViewUrl);
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
            PartPlanQueryDao dao = PartPlanQueryDao.getInstance();
            String[] head = new String[12];
            head[0] = "计划单号";
            head[1] = "计划员";
            head[2] = "制单日期";
            head[3] = "库房";
            head[4] = "计划年月";
            head[5] = "计划类型";
            head[6] = "总数量";
            head[7] = "总金额";
            head[8] = "提交时间";
            head[9] = "确认时间";
            head[10] = "状态";
            head[11] = "生成方式";

            List<Map<String, Object>> list = dao.queryPartPlan(request);
            Map<String, Object> planTypeMap = dao.getTcCodeMap(Constant.PART_PURCHASE_PLAN_TYPE.toString());//计划类型
            Map<String, Object> stateMap = dao.getTcCodeMap(Constant.PART_PURCHASE_PLAN_CHECK_STATUS.toString());//状态
            Map<String, Object> createTypeMap = dao.getTcCodeMap(Constant.PART_PURCHASE_PLAN_CREATE_TYPE.toString());//生成方式

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
                    detail[5] = this.getName(map, "PLAN_TYPE", planTypeMap); //获取计划类型
                    detail[6] = CommonUtils.checkNull(map.get("SUM_QTY"));
                    detail[7] = CommonUtils.checkNull(map.get("AMOUNT"));
                    detail[8] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                    detail[9] = CommonUtils.checkNull(map.get("CONFIRM_DATE"));
                    detail[10] = this.getName(map, "STATE", stateMap); //获取计划类型
                    detail[11] = this.getName(map, "CREATE_TYPE", createTypeMap); //获取计划类型
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

        String name = "配件计划列表.xls";
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
