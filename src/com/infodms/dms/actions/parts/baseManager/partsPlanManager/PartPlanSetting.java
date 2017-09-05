package com.infodms.dms.actions.parts.baseManager.partsPlanManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsPlanManager.PartPlanSettingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPlanDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : luole CreateDate : 2013-4-7
 * @ClassName : PartPlanSetting
 * @Description : 计划参数维护
 */
public class PartPlanSetting extends BaseImport {
    private static final String PART_PLAN_SETTING_URL = "/jsp/parts/baseManager/partsPlanManager/partPlanSetting/partPlanSettingInit.jsp";
    private static final String PART_PLAN_SETTING_URL2 = "/jsp/parts/baseManager/partsPlanManager/partPlanSetting/partPlanSettingInit2.jsp";
    private static final String INPUT_ERROR_URL = "/jsp/parts/baseManager/partsPlanManager/partPlanSetting/inputError.jsp";//数据导入出错页面
    public Logger logger = Logger.getLogger(PartPlanSetting.class);
    PartPlanSettingDao dao = PartPlanSettingDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    private RequestWrapper request = act.getRequest();

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-7
     * @Title : partPlanSettingInit
     * @Description: 计划参数维护初始化
     */
    public void partPlanSettingInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> list = dao.getPartWareHouse(logonUser.getUserId());
            act.setOutData("list", list);
            act.setOutData("mySort", this.dao.getSortList());
            act.setForword(PART_PLAN_SETTING_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划参数维护页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-7
     * @Title : partPlanSettingInit
     * @Description: 计划参数维护初始化
     */
    public void partPlanSettingInit2() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> list = dao.getPartWareHouse(logonUser.getUserId());
            act.setOutData("list", list);
            act.setOutData("mySort", this.dao.getSortList());
            act.setForword(PART_PLAN_SETTING_URL2);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划参数维护页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-7
     * @Title : partPlanSettingQuery
     * @Description: 计划参数维护查询
     */
    public void partPlanSettingQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String command = request.getParamValue("command");
            String partOldcode = request.getParamValue("PART_OLDCODE");
            String partCname = request.getParamValue("PART_CNAME");
            String whId = request.getParamValue("WH_ID");
            String partCode = request.getParamValue("PART_CODE");
            String sort = request.getParamValue("sort");

            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(partOldcode)) {
                sql.append(" and   upper(p.PART_OLDCODE) like upper('%" + partOldcode + "%') ");
            }
            if (!CommonUtils.isNullString(partCname)) {
                sql.append(" and p.PART_CNAME like '%" + partCname + "%' ");
            }
            if (!CommonUtils.isNullString(whId)) {
                sql.append(" and w.WH_ID = '" + whId + "' ");
            }
            if (!CommonUtils.isNullString(partCode)) {
                sql.append(" and upper(p.PART_CODE) like upper('%" + partCode + "%') ");
            }
            if (!CommonUtils.isNullString(sort) && sort != "-1" && !"-1".equals(sort)) {
                sql.append(" and pp.plan_ratio = " + sort + " ");
            }
            sql.append("AND exists(SELECT 1 FROM tt_part_planer_wh_relation r WHERE r.wh_id=pp.wh_id AND r.planer_id=").append(logonUser.getUserId()).append(")");
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.planPageQuery(sql.toString(), command, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
            act.setOutData("mySort", this.dao.getSortList());
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划参数维护查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-7
     * @Title : partPlanDownload
     * @Description: 下载
     */
    public void partPlanDownload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            String command = request.getParamValue("command");
            String partOldcode = request.getParamValue("PART_OLDCODE");
            String partCname = request.getParamValue("PART_CNAME");
            String whId = request.getParamValue("WH_ID");
            String partCode = request.getParamValue("PART_CODE");
            String sort = request.getParamValue("sort");

            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(partOldcode)) {
                sql.append(" and   upper(p.PART_OLDCODE) like upper('%" + partOldcode + "%') ");
            }
            if (!CommonUtils.isNullString(partCname)) {
                sql.append(" and p.PART_CNAME like '%" + partCname + "%' ");
            }
            if (!CommonUtils.isNullString(whId)) {
                sql.append(" and w.WH_ID = '" + whId + "' ");
            }
            if (!CommonUtils.isNullString(partCode)) {
                sql.append(" and upper(p.PART_CODE) like upper('%" + partCode + "%') ");
            }
            if (!CommonUtils.isNullString(sort) && sort != "-1" && !"-1".equals(sort)) {
                sql.append(" and pp.plan_ratio = " + sort + " ");
            }
            sql.append("AND exists(SELECT 1 FROM tt_part_planer_wh_relation r WHERE r.wh_id=pp.wh_id AND r.planer_id=").append(logonUser.getUserId()).append(")");

            String[] head = new String[25];
            head[0] = "配件编码";
            head[1] = "配件名称";
            head[2] = "配件件号";
            head[3] = "前3月平均销量";
            head[4] = "前6月平均销量";
            head[5] = "前12月平均销量";
            head[6] = "日均销量";
            head[7] = "分类";
            head[8] = "安全系数";
            head[9] = "安全周期";
            head[10] = "安全库存";
            head[11] = "订货周期(天)";
            head[12] = "到货周期(天)";
            head[13] = "BO数量";
            head[14] = "现场BO数量";
            head[15] = "在途数量";
            if ("1".equals(command)) {
                head[16] = "月计划数量";
            } else {
                head[16] = "紧急计划数量";
            }
            head[17] = "当前库存";
            head[18] = "仓库";
            head[19] = "计划价(元)";
            head[20] = "安全库存金额(元)";
            List<Map<String, Object>> list = dao.partPlanDownload(sql.toString(), command);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[25];
                    detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE") != null ? map.get("PART_OLDCODE") : "");
                    detail[1] = CommonUtils.checkNull(map.get("PART_CNAME") != null ? map.get("PART_CNAME") : "");
                    detail[2] = CommonUtils.checkNull(map.get("PART_CODE") != null ? map.get("PART_CODE") : "");
                    detail[3] = CommonUtils.checkNull(map.get("QUARTER_QTY") != null ? map.get("QUARTER_QTY") : "");
                    detail[4] = CommonUtils.checkNull(map.get("HFYEAR_QTY") != null ? map.get("HFYEAR_QTY") : "");
                    detail[5] = CommonUtils.checkNull(map.get("YEAR_QTY") != null ? map.get("YEAR_QTY") : "");
                    detail[6] = CommonUtils.checkNull(map.get("AVG_QTY") != null ? map.get("AVG_QTY") : "");
                    detail[7] = CommonUtils.checkNull(map.get("PLAN_RATIO_TYPE") != null ? map.get("PLAN_RATIO_TYPE") : "");
                    detail[8] = CommonUtils.checkNull(map.get("SAFTY_RATE") != null ? map.get("SAFTY_RATE") : "");
                    detail[9] = CommonUtils.checkNull(map.get("SAFETY_CYCLE") != null ? map.get("SAFETY_CYCLE") : "0");
                    detail[10] = CommonUtils.checkNull(map.get("SAFETY_STOCK") != null ? map.get("SAFETY_STOCK") : "0");
                    detail[11] = CommonUtils.checkNull(map.get("PLAN_CYCLE") != null ? map.get("PLAN_CYCLE") : "");
                    detail[12] = CommonUtils.checkNull(map.get("ARRIVE_CYCLE") != null ? map.get("ARRIVE_CYCLE") : "");
                    detail[13] = CommonUtils.checkNull(map.get("BO_QTY") != null ? map.get("BO_QTY") : "0");
                    detail[14] = CommonUtils.checkNull(map.get("LOC_BO_QTY") != null ? map.get("LOC_BO_QTY") : "0");
                    detail[15] = CommonUtils.checkNull(map.get("ORDER_QTY") != null ? map.get("ORDER_QTY") : "0");
                    detail[16] = CommonUtils.checkNull(map.get("PLAN_QTY") != null ? map.get("PLAN_QTY") : "0");
                    detail[17] = CommonUtils.checkNull(map.get("ITEM_QTY") != null ? map.get("ITEM_QTY") : "0");
                    detail[18] = CommonUtils.checkNull(map.get("WH_NAME") != null ? map.get("WH_NAME") : "");
                    detail[19] = CommonUtils.checkNull(map.get("SALE_PRICE3") != null ? map.get("SALE_PRICE3") : "0");
                    detail[20] = CommonUtils.checkNull(map.get("SF_AMOUNT") != null ? map.get("SF_AMOUNT") : "0");
                    list1.add(detail);
                }

            }
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划参数维护查询初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
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
     * @throws : luole LastDate : 2013-4-7
     * @Title : partPlanSave
     * @Description: 保存
     */
    public void partPlanSave() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.getResponse().setContentType("application/json");
            String planId = request.getParamValue("PLAN_ID");
            String safetyStock = request.getParamValue("SAFETY_STOCK" + planId);
            String oldSafetyStock = request.getParamValue("OLD_SAFETY_STOCK" + planId);
            String plan_cycle = request.getParamValue("PLAN_CYCLE" + planId);
            String arrive_cycle = request.getParamValue("ARRIVE_CYCLE" + planId);
            String plan_ratio = request.getParamValue("PLAN_RATIO" + planId);
            TtPartPlanDefinePO oldpo = new TtPartPlanDefinePO();
            oldpo.setPlanId(Long.parseLong(planId));
            //TtPartPlanDefinePO tempPo = (TtPartPlanDefinePO) (dao.select(oldpo).get(0));
            //long tempMaxStock = 0l;
           /* if (tempPo.getMinStock() != null && (tempPo.getMinStock() > tempMaxStock)) {
            } else {*/
            TtPartPlanDefinePO po = new TtPartPlanDefinePO();
            if (!"".equals(safetyStock) && safetyStock != null && safetyStock != oldSafetyStock) {
                po.setSafetyStock(Long.parseLong(safetyStock));
                po.setManualFlag(1);
            } else {
                po.setSafetyStock(0l);
            }
            if (!"".equals(plan_cycle) && plan_cycle != null) {
                po.setPlanCycle(Long.parseLong(plan_cycle));
            } else {
                po.setPlanCycle(0l);
            }
            if (!"".equals(arrive_cycle) && arrive_cycle != null) {
                po.setArriveCycle(Long.parseLong(arrive_cycle));
            } else {
                po.setArriveCycle(0l);
            }
            if (!"".equals(plan_ratio) && plan_ratio != null && plan_ratio != "undefined" && !"undefined".equals(plan_ratio)) {
                po.setPlanRatio(Integer.parseInt(plan_ratio));
            } else {
                po.setPlanRatio(0);
            }
            po.setUpdateBy(logonUser.getUserId());
            po.setUpdateDate(new Date());
            dao.update(oldpo, po);
           /* }*/
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "success");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划参数保存");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-29
     * @Title : 获取总安全库存金额
     */
    public void getTotalSfAmount() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String totalSfAmount = "0.00";
        try {
            act.getResponse().setContentType("application/json");
            String command = request.getParamValue("command");
            String partOldcode = request.getParamValue("PART_OLDCODE");
            String partCname = request.getParamValue("PART_CNAME");
            String whId = request.getParamValue("WH_ID");
            String partCode = request.getParamValue("PART_CODE");
            String sort = request.getParamValue("sort");

            StringBuffer sql = new StringBuffer();
            if (!CommonUtils.isNullString(partOldcode)) {
                sql.append(" and   upper(p.PART_OLDCODE) like upper('%" + partOldcode + "%') ");
            }
            if (!CommonUtils.isNullString(partCname)) {
                sql.append(" and p.PART_CNAME like '%" + partCname + "%' ");
            }
            if (!CommonUtils.isNullString(whId)) {
                sql.append(" and PP.WH_ID = '" + whId + "' ");
            }
            if (!CommonUtils.isNullString(partCode)) {
                sql.append(" and upper(p.PART_CODE) like upper('%" + partCode + "%') ");
            }
            if (!CommonUtils.isNullString(sort) && sort != "-1" && !"-1".equals(sort)) {
                sql.append(" and pp.plan_ratio = " + sort + " ");
            }
            sql.append("AND exists(SELECT 1 FROM tt_part_planer_wh_relation r WHERE r.wh_id=pp.wh_id AND r.planer_id=").append(logonUser.getUserId()).append(")");

            List<Map<String, Object>> list = dao.getTotalSfAmount(sql.toString(), command);

            if (null != list && list.size() == 1) {
                totalSfAmount = list.get(0).get("TOTAL_SF_AMOUT").toString();
            }

            act.setOutData("totalSfAmount", totalSfAmount);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取总安全库存金额");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-29
     * @Title : 计划参数批量维护模板
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
            listHead.add("分类");
            listHead.add("安全库存");
            listHead.add("订货周期(天)");
            listHead.add("到货周期(天)");
            list.add(listHead);
            // 导出的文件名
            String fileName = "月/紧急计划参数批量维护模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出配件计划参数批量维护模板错误");
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

    public void partPlanSave(List<Map<String, String>> voList, String command) {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            if (null != voList && voList.size() > 0) {
                TtPartPlanDefinePO selPo = null;
                TtPartPlanDefinePO updPo = null;
                for (int i = 0; i < voList.size(); i++) {
                    String planId = voList.get(i).get("planId");
                    String safetyStock = voList.get(i).get("safetyStock");
                    String planCycle = voList.get(i).get("planCycle");
                    String arriveCycle = voList.get(i).get("arriveCycle");
                    String planRatio = voList.get(i).get("planRatio");

                    selPo = new TtPartPlanDefinePO();
                    updPo = new TtPartPlanDefinePO();

                    selPo.setPlanId(Long.parseLong(planId));

                    if (!"".equals(safetyStock) && safetyStock != null) {
                        updPo.setSafetyStock(Long.valueOf(safetyStock));
                        updPo.setManualFlag(1);
                    }
                    updPo.setPlanCycle(Long.parseLong(planCycle));
                    updPo.setArriveCycle(Long.parseLong(arriveCycle));
                    updPo.setPlanRatio(Integer.parseInt(planRatio));
                    updPo.setUpdateBy(logonUser.getUserId());
                    updPo.setUpdateDate(new Date());

                    dao.update(selPo, updPo);
                }
            }

            if ("1".equals(command)) {
                partPlanSettingInit();
            } else {
                partPlanSettingInit2();
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "计划参数保存");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-29
     * @Title : 导入月/紧急计划参数
     */
    public void planSettingUpload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String command = request.getParamValue("command");
            String whId = request.getParamValue("WH_ID");
            String pageUrl = request.getParamValue("pageUrl");

            List<Map<String, String>> errorInfo = null;
            String err = "";

            errorInfo = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 5, 3, maxSize);

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
                act.setOutData("pageUrl", pageUrl);
                act.setForword(INPUT_ERROR_URL);
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                loadVoList(voList, list, errorInfo, command, whId, logonUser.getUserId() + "");
                if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setOutData("pageUrl", pageUrl);
                    act.setForword(INPUT_ERROR_URL);
                } else {
                    //保存
                    partPlanSave(voList, command);
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
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo, String command, String whId, String userId) {
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
                String partIdTmp = "";
                Map<String, String> tempmap = new HashMap<String, String>();
                if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "配件编码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String partOldCodeStr = cells[0].getContents().trim().toUpperCase();
                    List<Map<String, Object>> partCheck = dao.checkOldCode(partOldCodeStr);
                    if (null != partCheck && partCheck.size() == 1) {

                        StringBuffer sbStr = new StringBuffer();
                        sbStr.append(" AND PP.WH_ID = '" + whId + "' ");
                        sbStr.append(" AND PP.PLAN_TYPE = '" + command + "' ");
                        sbStr.append(" AND EXISTS(SELECT 1 FROM TT_PART_PLANER_WH_RELATION R WHERE R.WH_ID = PP.WH_ID ");
                        sbStr.append(" AND R.PLANER_ID = '" + userId + "') ");
                        sbStr.append(" AND UPPER(P.PART_OLDCODE) = '" + partOldCodeStr + "'");
                        List<Map<String, Object>> partInPlan = dao.checkPartInPlan(sbStr.toString());

                        if (null != partInPlan && partInPlan.size() == 1) {
                            tempmap.put("planId", partInPlan.get(0).get("PLAN_ID").toString());
                        } else {
                            Map<String, String> errormap = new HashMap<String, String>();
                            errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                            errormap.put("2", "配件编码【" + cells[0].getContents().trim() + "】");
                            errormap.put("3", "不在当前的计划中!");
                            errorInfo.add(errormap);
                        }

                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "配件编码【" + cells[0].getContents().trim() + "】");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "分类");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String str = cells[1].getContents().trim().toUpperCase();

                    String sqlStr = " AND PD.SORT_TYPE = '" + str + "'";
                    List<Map<String, Object>> typeCheck = dao.checkSortType(sqlStr);

                    if (null != typeCheck && typeCheck.size() == 1) {
                        tempmap.put("planRatio", typeCheck.get(0).get("SAFTY_RATE").toString());
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "分类");
                        errormap.put("3", "不存在或已失效!");
                        errorInfo.add(errormap);
                    }

                }

                if (cells.length >= 3 && !CommonUtils.isEmpty(cells[2].getContents())) {
                    String minPkStr = cells[2].getContents().trim();
                    String regex = "(^([0])|([1-9]+(\\d)*)$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(minPkStr);

                    if (matcher.find()) {
                        tempmap.put("safetyStock", minPkStr);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "安全库存");
                        errormap.put("3", "不合法!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "订货&订货提前期(月)");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String minPkStr = cells[3].getContents().trim();
                    String regex = "(^([0])|([1-9]+(\\d)*)$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(minPkStr);

                    if (matcher.find()) {
                        tempmap.put("planCycle", minPkStr);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "订货&订货提前期(月)");
                        errormap.put("3", "不合法!");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 5 || CommonUtils.isEmpty(cells[4].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "到货周期(天)");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    String minPkStr = cells[4].getContents().trim();
                    String regex = "(^[1-9]+(\\d)*$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(minPkStr);

                    if (matcher.find()) {
                        tempmap.put("arriveCycle", minPkStr);
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "到货周期(天)");
                        errormap.put("3", "不合法!");
                        errorInfo.add(errormap);
                    }
                }

                voList.add(tempmap);
            }
        }
    }

}


