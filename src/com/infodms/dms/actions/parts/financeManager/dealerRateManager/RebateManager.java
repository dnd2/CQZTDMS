package com.infodms.dms.actions.parts.financeManager.dealerRateManager;


import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.ExcelUtil;
import com.infodms.dms.dao.parts.rebateManager.rebateManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDlrRebatePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.util.*;

import static com.infodms.dms.dao.parts.rebateManager.rebateManagerDao.formatMonth;


public class RebateManager extends BaseImport {
    private static final String Quarter_REBATE_IMPORT_URL_INIT = "/jsp/parts/rebate/rebateImport.jsp"; //季度返利导入
    private static final String Quarter_REBATE_IMPORT_CONFRI_URL_INIT = "/jsp/parts/rebate/rebateImportConfrim.jsp";//季度返利M倒入确认页面
    private static final String Quarter_REBATE_QUERY_URL_INIT = "/jsp/parts/rebate/partRebateQuery.jsp";//季度返利M倒入确认页面
    private static final String INVO_QUERY_URL_INIT = "/jsp/parts/rebate/partInvQuery.jsp";//销售收入报表
    private static final String INPUT_ERROR_URL = "/jsp/parts/rebate/inputerror.jsp";//月度计划查询

    public Logger logger = Logger.getLogger(RebateManager.class);
    rebateManagerDao dao = rebateManagerDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    /**
     * 季度返利导入初始界面
     */
    public void QuarterRebateInputInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curYear = PlanUtil.getCurrentYear();
            String curMonth = PlanUtil.getCurrentMonth();

            act.setOutData("curYear", curYear);
            act.setOutData("curMonth", curMonth);
            act.setForword(Quarter_REBATE_IMPORT_URL_INIT);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "季度返利导入初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 季度返利模板下载
     */
    public void download() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            List<List<Object>> list = new ArrayList<List<Object>>();
            List<Object> listHead = new ArrayList<Object>();
            listHead.add("服务商代码");
            listHead.add("返利类型(代码)");
            listHead.add("返利金额(精度为2)");
            list.add(listHead);
            // 导出的文件名
            String fileName = "服务商季度返利导入模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商季度返利导入模板下载失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * 服务商季度返利导入
     */
    public void monthPlanUpload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String year = request.getParamValue("year");
            String month = request.getParamValue("month");
            List<Map<String, String>> errorInfo = new ArrayList<Map<String, String>>();
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
                act.setForword(INPUT_ERROR_URL);
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                loadVoList(voList, list, errorInfo);
                if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setForword(INPUT_ERROR_URL);
                } else {
                    act.setOutData("taskMonth", (year + "-" + simMonth(month)));
                    act.setOutData("list", voList);
                    act.setForword(Quarter_REBATE_IMPORT_CONFRI_URL_INIT);
                }
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "季度返利导入失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo) {
        //一次加载所有返利信息和售后服务商信息，减少数据查询次数
        Map rebateMaps = dao.getRebateType();
        Map dealerMaps = dao.getDealerInfo();
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
                Map<String, String> tempmap = new HashMap<String, String>();
                //服务商代码
                if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "服务商代码");
                    errormap.put("3", "空!");
                    errorInfo.add(errormap);
                } else {
                    if (!dealerMaps.containsKey(cells[0].getContents().trim())) {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "服务商代码");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    } else {
                        tempmap.put("dealerId", dealerMaps.get(cells[0].getContents().trim()).toString().split(",")[0]);
                        tempmap.put("dealerName", dealerMaps.get(cells[0].getContents().trim()).toString().split(",")[1]);
                        tempmap.put("dealerCode", cells[0].getContents().trim());
                    }
                }

                //返利类型
                if ("".equals(cells[1].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "返利类型代码");
                    errormap.put("3", "空!");
                    errorInfo.add(errormap);
                } else {
                    if (!rebateMaps.containsKey(cells[1].getContents().trim())) {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "返利类型代码");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    } else {
                        tempmap.put("rebateType", rebateMaps.get(cells[1].getContents().trim()).toString().split(",")[0]);
                    }
                }
                //返利金额
                if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "返利金额");
                    errormap.put("3", "空!");
                    errorInfo.add(errormap);
                } else {
                    if (!isNumeric(cells[2].getContents().trim())) {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "返利金额");
                        errormap.put("3", "不是数值!");
                        errorInfo.add(errormap);
                    } else
                        tempmap.put("amount", cells[2].getContents().trim());
                }
                voList.add(tempmap);
            }
        }
    }

    //格式化当前月
    private String simMonth(String month) {
        if (month.length() == 1)
            return "0" + month;
        return month;
    }

    //数字判断
    public static boolean isNumeric(String str) {
        if (str.matches("^[0-9]+(.[0-9]*)?$")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 服务商季度返利导入
     */
    public void monthPlanAdd() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            String[] dealerIds = request.getParamValues("dealerId");
            String taskMonth = CommonUtils.checkNull(request.getParamValue("taskMonth"));

            List dlrRebatePOs = new ArrayList<TtPartDlrRebatePO>();

            for (int i = 0; i < dealerIds.length; i++) {
                List<String> templist = new ArrayList<String>();
                String code = CommonUtils.checkNull(request.getParamValue("dealerCode_" + dealerIds[i]));
                String name = CommonUtils.checkNull(request.getParamValue("dealerName_" + dealerIds[i]));
                String amount = CommonUtils.checkNull(request.getParamValue("amount_" + dealerIds[i]));
                String rebateType = CommonUtils.checkNull(request.getParamValue("rebateType_" + dealerIds[i]));

                TtPartDlrRebatePO srcPo = new TtPartDlrRebatePO();
                TtPartDlrRebatePO updatePo = new TtPartDlrRebatePO();
                srcPo.setDealerId(Long.parseLong(dealerIds[i]));
                srcPo.setYearMonth(taskMonth);
                srcPo.setRebateType(rebateType);
                //存在则更新不再存在则插入
                if (dao.select(srcPo).size() > 0) {
                    updatePo.setAmount(Double.valueOf(amount));
                    updatePo.setUpdateBy(logonUser.getUserId());
                    updatePo.setUpdateDate(new Date());
                    dao.update(srcPo, updatePo);
                } else {
                    TtPartDlrRebatePO dtlpo = new TtPartDlrRebatePO();
                    dtlpo.setRebateId(Long.parseLong(SequenceManager.getSequence("")));
                    dtlpo.setDealerId(Long.parseLong(dealerIds[i]));
                    dtlpo.setDealerCode(code);
                    dtlpo.setDealerName(name);
                    dtlpo.setYearMonth(taskMonth);
                    dtlpo.setRebateType(rebateType);
                    dtlpo.setAmount(Double.parseDouble(amount));
                    dtlpo.setCreateDate(date);
                    dtlpo.setCreateBy(logonUser.getUserId());
                    dlrRebatePOs.add(dtlpo);
                }
            }
            if (dlrRebatePOs.size() > 0) {
                dao.insert(dlrRebatePOs);
            }
            act.setOutData("success", "success");
            QuarterRebateInputInit();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件返利控制表初始化
     */
    public void RebateQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("curYear", PlanUtil.getCurrentYear());
            act.setOutData("curMonth", PlanUtil.getCurrentMonth());
            act.setForword(Quarter_REBATE_QUERY_URL_INIT);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "返利控制表初始化失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件收入统计表初始化
     */
    public void InvQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("curYear", PlanUtil.getCurrentYear());
            act.setOutData("curMonth", PlanUtil.getCurrentMonth());
            act.setForword(INVO_QUERY_URL_INIT);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售收入初始化失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件返利控制表查询
     */
    public void rebateQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            rebateMngQueryPre();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.rebateQuery(request, curPage, Constant.PAGE_SIZE, "1", loginUser);
            PageResult<Map<String, Object>> sumps = dao.rebateQuery(request, 1, Constant.PAGE_SIZE, "2", loginUser);
            if (sumps.getRecords().size() > 0) {
                Map<String, Object> sumMap = sumps.getRecords().get(0);
                act.setOutData("SWD_AMOUNT", sumMap.get("WD_AMOUNT") == null ? "0" : sumMap.get("WD_AMOUNT"));
                act.setOutData("SYF_AMOUNT", sumMap.get("YF_AMOUNT") == null ? "0" : sumMap.get("YF_AMOUNT"));
                act.setOutData("SCD_AMOUNT", sumMap.get("CD_AMOUNT") == null ? "0" : sumMap.get("CD_AMOUNT"));
                LinkedHashMap tmp = (LinkedHashMap) request.getAttribute("yfMap");
                Iterator it = tmp.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String fixed = entry.getKey().toString().substring(3);
                    act.setOutData("SYFM" + fixed, sumMap.get("YFM" + fixed) == null ? "0" : sumMap.get("YFM" + fixed));
                    act.setOutData("SCDM" + fixed, sumMap.get("CDM" + fixed) == null ? "0" : sumMap.get("CDM" + fixed));
                }
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "返利控制表查询失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件销售收入查询
     */
    public void invQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            invMngQueryPre();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.invQuery(request, curPage, Constant.PAGE_SIZE, "1");
            PageResult<Map<String, Object>> sumps = dao.invQuery(request, curPage, Constant.PAGE_SIZE, "2");
            if (sumps.getPageSize() > 0) {
                Map<String, Object> sumMap = sumps.getRecords().get(0);
                LinkedHashMap tmp = (LinkedHashMap) request.getAttribute("yfMap");
                act.setOutData("SWD_AMOUNT", sumMap.get("WD_AMOUNT"));
                act.setOutData("sumCol", tmp.entrySet().size());
                Iterator it = tmp.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String fixed = entry.getKey().toString().substring(3);
                    act.setOutData("SYFM" + fixed, sumMap.get("YFM" + fixed) == null ? "0" : sumMap.get("YFM" + fixed));
                }
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售收入查询失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 返利动态表头初始化
     */
    public void rebateMngQueryPre() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String year = CommonUtils.checkNull(request.getParamValue("year")) == "" ? PlanUtil.getCurrentYear() : CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            Map yfMap = new LinkedHashMap();
            Map cdMap = new LinkedHashMap();
            if ("".equals(month) && "".equals(month2)) {
                for (int i = 1; i <= 4; i++) {
//                    yfMap.put("YFM" + i + "_AMOUNT", "应返" + year + "年第0" + i + "季度");
//                    cdMap.put("CDM" + i + "_AMOUNT", "兑现" + year + "年第0" + i + "季度");
                }
            } else {
                if (!"".equals(month) && !"".equals(month2)) {
                    if (Integer.valueOf(month2) > Integer.valueOf(month)) {
                        for (int i = Integer.valueOf(month); i <= Integer.valueOf(month2); i++) {
                            yfMap.put("YFM" + i + "_AMOUNT", "应返" + year + "年第0" + i + "季度");
                            cdMap.put("CDM" + i + "_AMOUNT", "兑现" + year + "年第0" + i + "季度");
                        }

                    } else if (Integer.valueOf(month2) == Integer.valueOf(month)) {
                        yfMap.put("YFM" + month2 + "_AMOUNT", "应返" + year + "年第0" + month2 + "季度");
                        cdMap.put("CDM" + month2 + "_AMOUNT", "兑现" + year + "年第0" + month2 + "季度");
                    }
                } else {
                    if ("".equals(month2) && !"".equals(month)) {
                        yfMap.put("YFM" + month + "_AMOUNT", "应返" + year + "年第0" + month + "季度");
                        cdMap.put("CDM" + month + "_AMOUNT", "兑现" + year + "年第0" + month + "季度");
                    }
                    if ("".equals(month) && !"".equals(month2)) {
                        yfMap.put("YFM" + month2 + "_AMOUNT", "应返" + year + "年第0" + month2 + "季度");
                        cdMap.put("CDM" + month2 + "_AMOUNT", "兑现" + year + "年第0" + month2 + "季度");
                    }
                }
            }
            request.setAttribute("cdMap", cdMap);
            request.setAttribute("yfMap", yfMap);
            act.setOutData("cdMap", cdMap);
            act.setOutData("yfMap", yfMap);
            act.setOutData("m1", month == "" ? "0" : month);
            act.setOutData("m2", month2 == "" ? "0" : month2);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "返利控制表动态参数初始化失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    /**
     * 销售收入动态表头
     */
    public void invMngQueryPre() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String year = CommonUtils.checkNull(request.getParamValue("year")) == "" ? PlanUtil.getCurrentYear() : CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            LinkedHashMap yfMap = new LinkedHashMap();
            if ("".equals(month) && "".equals(month2)) {

            } else {
                if (!"".equals(month) && !"".equals(month2)) {
                    if (Integer.valueOf(month2) > Integer.valueOf(month)) {
                        for (int i = Integer.valueOf(month); i <= Integer.valueOf(month2); i++) {
                            yfMap.put("YFM" + formatMonth(i + "") + "_AMOUNT", year + "年" + (i + "") + "月");
                        }

                    } else if (Integer.valueOf(month2) == Integer.valueOf(month)) {
                        yfMap.put("YFM" + formatMonth(month2) + "_AMOUNT", year + "年" + (month2) + "月");
                    }
                } else {
                    if ("".equals(month2) && !"".equals(month)) {
                        yfMap.put("YFM" + formatMonth(month) + "_AMOUNT", year + "年" + (month) + "月");
                    }
                    if ("".equals(month) && !"".equals(month2)) {
                        yfMap.put("YFM" + formatMonth(month2) + "_AMOUNT", year + "年" + (month2) + "月");
                    }
                }
            }
            request.setAttribute("yfMap", yfMap);
            act.setOutData("yfMap", yfMap);
            act.setOutData("m1", month == "" ? "0" : month);
            act.setOutData("m2", month2 == "" ? "0" : month2);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件收入表动态参数初始化失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    /**
     * 明细导出
     */
    public void expExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            rebateMngQueryPre();
            Map yfMap = (LinkedHashMap) request.getAttribute("yfMap");
            Map cdMap = (LinkedHashMap) request.getAttribute("cdMap");

            List yfMapKeyList = new ArrayList();
            List cdMapKeyList = new ArrayList();

            String[] head = new String[45];
            head[0] = "序号";
            head[1] = "省份";
            head[2] = "服务商代码";
            head[3] = "服务商名称";
            head[4] = "返利类型";
            head[5] = "未兑现金额";
            head[6] = "应返小计";

            Iterator iterator = yfMap.entrySet().iterator();
            int j = 0;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                head[6 + j] = entry.getValue() + "";
                yfMapKeyList.add(entry.getKey() + "");
                j++;
            }

            head[arrUsedLength(head)] = "兑现小计";
            int jj = 0;
            int arrlength2 = arrUsedLength(head);
            Iterator iterator2 = cdMap.entrySet().iterator();
            while (iterator2.hasNext()) {
                Map.Entry entry2 = (Map.Entry) iterator2.next();
                head[arrlength2 + jj] = entry2.getValue() + "";
                cdMapKeyList.add(entry2.getKey() + "");
                jj++;
            }

            PageResult<Map<String, Object>> ps = dao.rebateQuery(request, 1, Constant.PAGE_SIZE_MAX, "1", logonUser);
            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[45];
                        detail[0] = CommonUtils.checkNull((i + 1));
                        detail[1] = CommonUtils.checkNull(map.get("REGION_NAME"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("FIN_RETURN_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("WD_AMOUNT"));
                        detail[6] = CommonUtils.checkNull(map.get("YF_AMOUNT"));
                        int j2 = 0;
                        for (int yk = 0; yk < yfMapKeyList.size(); yk++) {
                            detail[6 + j2] = CommonUtils.checkNull(map.get(yfMapKeyList.get(yk) + ""));
                            ++j2;
                        }

                        detail[arrUsedLength(detail)] = CommonUtils.checkNull(map.get("CD_AMOUNT"));
                        int jj2 = 0;
                        int arrlength = arrUsedLength(detail);
                        for (int cd = 0; cd < cdMapKeyList.size(); cd++) {
                            detail[arrlength + jj2] = CommonUtils.checkNull(map.get(cdMapKeyList.get(cd) + ""));
                            ++jj2;
                        }
                        list1.add(detail);
                    }
                }
                String name = "返利汇总报表.xls";
                ExcelUtil.toExceUtil(ActionContext.getContext().getResponse(), request, head, list1, name);
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
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
//            act.setForword(INIT_URL);
        }
    }

    /**
     * 明细导出
     */
    public void expInvExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            invMngQueryPre();
            Map yfMap = (LinkedHashMap) request.getAttribute("yfMap");

            List yfMapKeyList = new ArrayList();

            String[] head = new String[45];
            head[0] = "序号";
            head[1] = "省份";
            head[2] = "服务商代码";
            head[3] = "服务商名称";
            head[4] = "合计金额";

            Iterator iterator = yfMap.entrySet().iterator();
            int j = 0;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                head[5 + j] = entry.getValue() + "";
                yfMapKeyList.add(entry.getKey() + "");
                j++;
            }

            PageResult<Map<String, Object>> ps = dao.invQuery(request, 1, Constant.PAGE_SIZE_MAX, "1");
            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[45];
                        detail[0] = CommonUtils.checkNull((i + 1));
                        detail[1] = CommonUtils.checkNull(map.get("REGION_NAME"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[4] = CommonUtils.checkNull(map.get("WD_AMOUNT"));
                        int j2 = 0;
                        for (int yk = 0; yk < yfMapKeyList.size(); yk++) {
                            detail[5 + j2] = CommonUtils.checkNull(map.get(yfMapKeyList.get(yk) + ""));
                            ++j2;
                        }

                        list1.add(detail);
                    }
                }
                String name = "配件收入统计表.xls";
                ExcelUtil.toExceUtil(ActionContext.getContext().getResponse(), request, head, list1, name);
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
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
//            act.setForword(INIT_URL);
        }
    }

    /**
     * 数组已使用长度
     *
     * @param str
     * @return
     */
    private int arrUsedLength(Object[] str) {
        int length = 0;
        if (str.length == 0) {
            return 0;
        }
        for (int i = 0; i < str.length; i++) {
            if (str[i] == null) {
                length = i;
                break;
            }
        }
        return length;
    }
}
