package com.infodms.dms.actions.parts.monthPlan;


import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.monthPlan.MonthPlanManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartDlrMonthTaskPO;
import com.infodms.dms.po.TtPartDlrTaskMonthDtlPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
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

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author : luole
 *         CreateDate     : 2013-4-8
 * @ClassName : MonthPlanManager
 * @Description : 月度目标管理
 */
public class MonthPlanManager extends BaseImport {
    private static final String MONTH_PLAN_INPUT_URL = "/jsp/parts/monthPlan/monthPlanInput.jsp"; //月度计划导入
    private static final String MONTH_PLAN_QUERY_URL = "/jsp/parts/monthPlan/monthPlanQuery.jsp";//月度计划查询
    private static final String MONTH_PLAN_INPUT_COM_URL = "/jsp/parts/monthPlan/monthPlanInputCom.jsp";//月度计划导入确定
    private static final String INPUT_ERROR_URL = "/jsp/parts/monthPlan/inputerror.jsp";//月度计划查询
    private static final String MONTH_PLAN_QUERY_Bb_URL = "/jsp/parts/monthPlan/monthPlanQueryBb.jsp";//月度计划查询(本部)
    private static final String MONTH_PLAN_QUERY_Gyzx_URL = "/jsp/parts/monthPlan/monthPlanQueryGyzx.jsp";//月度计划查询(供应中心)
    private static final String MONTH_PLAN_QUERY_Zz_URL = "/jsp/parts/monthPlan/monthPlanQueryZz.jsp";//月度计划查询(终端)

    public Logger logger = Logger.getLogger(MonthPlanManager.class);
    MonthPlanManagerDao dao = MonthPlanManagerDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private static int pageSize = 1000;

    /**
     * @param :
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : monthPlanInputInit
     * @Description: 月度目标导入初始化
     */
    public void monthPlanInputInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curYear = PlanUtil.getCurrentYear();
            String curMonth = PlanUtil.getCurrentMonth();

            act.setOutData("curYear", curYear);
            act.setOutData("curMonth", curMonth);
            act.setForword(MONTH_PLAN_INPUT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "月度目标导入初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : monthPlanQueryInit
     * @Description: 月度目标查询 初始化
     */
    public void monthPlanQueryInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curMonth = PlanUtil.getCurrentMonth();
            String curYear = PlanUtil.getCurrentYear();
            act.setOutData("curYear", curYear);
            act.setOutData("curMonth", curMonth);
            if (logonUser.getDealerId() != null) {
                TmDealerPO tmDealerPO = new TmDealerPO();
                tmDealerPO.setDealerId(Long.valueOf(logonUser.getDealerId()));
                TmDealerPO tmDealerPO1 = (TmDealerPO) dao.select(tmDealerPO).get(0);
                if ("92101001".equals(tmDealerPO1.getPdealerType().toString())) {
                    act.setOutData("dealertype", "2");
                } else
                    act.setOutData("dealertype", "1");
            } else {
                act.setOutData("dealertype", "0");
            }
            boolean oemFlag = false;
            String dealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                oemFlag = true;
            }
            act.setOutData("portalType", logonUser.getPoseType());//类型 10021001是主机厂端
            act.setOutData("oemFlag", oemFlag);
            act.setOutData("verList", dao.getPlanVer(curYear + "-" + simMonth(curMonth)));
            act.setForword(MONTH_PLAN_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "月度目标查询初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : download
     * @Description: 模块下载
     */
    public void download() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            List<List<Object>> list = new ArrayList<List<Object>>();
            List<Object> listHead = new ArrayList<Object>();
            listHead.add("经销商代码");
            listHead.add("经销商名称");
            listHead.add("任务金额(元)");
            list.add(listHead);
            // 导出的文件名
            String fileName = "经销商月度目标.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商月度目标模板下载异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Description: 获取版本
     */
    public void getPlanVer() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.getResponse().setContentType("application/json");
            String curYear = request.getParamValue("year");
            String curMonth = request.getParamValue("month");
            act.setOutData("maxVer", dao.checkVer(curYear + "-" + simMonth(curMonth)));
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取版本");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String queryType = CommonUtils.checkNull(request.getParamValue("queryType"));//请求类型类型
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));

            StringBuffer sql = new StringBuffer();
            // 处理当前页
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            //供应中心限制
            if ("0".equals(type)) {
                long orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId();
                sql.append(" and  ( td.DEALER_ID = " + orgId).append(" OR EXISTS\n" +
                        "        (SELECT 1\n" +
                        "           FROM TT_PART_SALES_RELATION R\n" +
                        "          WHERE R.CHILDORG_ID = TD.DEALER_ID\n" +
                        "            AND R.PARENTORG_ID = " + orgId + "))");
            }
            //服务商编码
            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" AND  TD.DEALER_CODE IN (" + tempsql + ")\n");
                } else {
                    sql.append(" AND  UPPER(TD.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%'\n");
                }

            }
            if ("0".equals(queryType) || "".equals(month)) {  //当前年月最大版本
                //选择了月
                sql.append(" AND TD.TASK_MONTH >= '");
                sql.append(year);
                sql.append("-" + simMonth(month));
                sql.append("'\n");
                sql.append(" AND TD.TASK_MONTH <= '");
                sql.append(year);
                sql.append("-" + simMonth(month2));
                sql.append("'\n");
                //增加大区人员限制 1128
                if (logonUser.getCompanyId().toString().equals(Constant.OEM_ACTIVITIES) && !logonUser.getOrgId().toString().equals(Constant.OEM_ACTIVITIES)
                        && logonUser.getParentOrgId().toString().equals(Constant.OEM_ACTIVITIES)) {
                    sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", logonUser));
                }
            }
            String[] head = new String[13];
            head[0] = "年-月";
            head[1] = "经销商代码";
            head[2] = "经销商名称";
            head[3] = "任务金额(元)";
            head[4] = "完成金额(元)";
            head[5] = "完成率";
            PageResult<Map<String, Object>> ps = null;
            if ("1".equals(flag)) {
                ps = dao.planPageQuery(sql.toString(), 1, Constant.PAGE_SIZE_MAX);
            } else {
                ps = dao.planCountQuery(sql.toString(), request, 1, Constant.PAGE_SIZE_MAX);
            }
            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[13];
                        detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH")).replace(",", "");
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE")).replace(",", "");
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME")).replace(",", "");
                        detail[3] = CommonUtils.checkNull(map.get("AMOUNT")).replace(",", "");
                        detail[4] = CommonUtils.checkNull(map.get("SELLMONEY")).replace(",", "");
                        detail[5] = CommonUtils.checkNull(map.get("RATIO")).replace(",", "");
                        list1.add(detail);
                    }
                }
            } else {
            	logger.info("没有满足条件的数据!");
//                BizException e1 = new BizException(act,
//                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
//                throw e1;
            }

            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public Object exportEx(ResponseWrapper response,
                           RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "经销商月度目标.xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(name, "utf-8"));
            out = response.getOutputStream();
            wwb = Workbook.createWorkbook(out);
            jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

            if (head != null && head.length > 0) {
                for (int i = 0; i < head.length; i++) {
                    ws.addCell(new Label(i, 0, head[i]));
                }
            }
            if(list.size()>0){
            	int pageSize = list.size() / 30000;
                for (int z = 1; z < list.size() + 1; z++) {
                    String[] str = list.get(z - 1);
                    for (int i = 0; i < str.length; i++) {
                        if (CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])) {
                            ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                        } else {
                            ws.addCell(new Label(i, z, str[i]));
                        }
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
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : planQuery
     * @Description: 查询
     */
    public void planQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String queryType = CommonUtils.checkNull(request.getParamValue("queryType"));//请求类型类型
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));

            StringBuffer sql = new StringBuffer();
            PageResult<Map<String, Object>> ps = null;
            // 处理当前页
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            //供应中心限制
            if ("0".equals(type)) {
                long orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId();
                sql.append(" and  ( td.DEALER_ID = " + orgId).append(" OR EXISTS\n" +
                        "        (SELECT 1\n" +
                        "           FROM TT_PART_SALES_RELATION R\n" +
                        "          WHERE R.CHILDORG_ID = TD.DEALER_ID\n" +
                        "            AND R.PARENTORG_ID = " + orgId + "))");
            }
            //服务商编码
            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" AND  TD.DEALER_CODE IN (" + tempsql + ")\n");
                } else {
                    sql.append(" AND  UPPER(TD.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%'\n");
                }

            }
            if ("0".equals(queryType) || "".equals(month)) {  //当前年月最大版本
                //选择了月
                sql.append(" AND TD.TASK_MONTH >= '");
                sql.append(year);
                sql.append("-" + simMonth(month));
                sql.append("'\n");
                sql.append(" AND TD.TASK_MONTH <= '");
                sql.append(year);
                sql.append("-" + simMonth(month2));
                sql.append("'\n");
                //增加大区人员限制 1128
                if (logonUser.getCompanyId().toString().equals(Constant.OEM_ACTIVITIES) && !logonUser.getOrgId().toString().equals(Constant.OEM_ACTIVITIES)
                        && logonUser.getParentOrgId().toString().equals(Constant.OEM_ACTIVITIES)) {
                    sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", logonUser));
                }

                if ("0".equals(type)) {
                    ps = dao.planPageQuery(sql.toString(), curPage, pageSize);
                }
                if ("1".equals(type)) {
                    //ps = dao.planPageQueryBb(sql.toString(),Constant.OEM_ACTIVITIES, curPage, pageSize);
                }
                if ("2".equals(type)) {
                    //ps = dao.planPageQueryZz(sql.toString(), curPage, pageSize);
                }
                if ("3".equals(type)) {
                    ps = dao.planPageQueryXxy(sql.toString(), Constant.OEM_ACTIVITIES, curPage, pageSize);
                }
                if ("4".equals(type)) {
                    ps = dao.planPageQuerySf(sql.toString(), curPage, pageSize);
                }
                if ("5".equals(type)) {
                    ps = dao.planPageQueryDq(sql.toString(), curPage, pageSize);
                }
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-23
     * @Title : 统计查询
     */
    public void normalCountQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));

            String type = CommonUtils.checkNull(request.getParamValue("type"));

            StringBuffer sql = new StringBuffer();
            //供应中心
            if ("0".equals(type)) {
                long orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId();
                sql.append(" and  ( td.DEALER_ID = " + orgId).append(" OR EXISTS\n" +
                        "        (SELECT 1\n" +
                        "           FROM TT_PART_SALES_RELATION R\n" +
                        "          WHERE R.CHILDORG_ID = TD.DEALER_ID\n" +
                        "            AND R.PARENTORG_ID = " + orgId + "))");
            }
            //服务商编码
            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  td.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(td.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }
            //选择了月
            sql.append(" and td.task_month >= '");
            sql.append(year);
            sql.append("-" + simMonth(month));
            sql.append("'");
            sql.append(" and td.task_month <= '");
            sql.append(year);
            sql.append("-" + simMonth(month2));
            sql.append("'");
            //增加大区人员限制 1128
            if (logonUser.getCompanyId().toString().equals(Constant.OEM_ACTIVITIES) && !logonUser.getOrgId().toString().equals(Constant.OEM_ACTIVITIES)
                    && logonUser.getParentOrgId().toString().equals(Constant.OEM_ACTIVITIES)) {
                sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", logonUser));
            }
            List<Map<String, Object>> list = null;
            if ("0".equals(type)) {
                list = dao.downloadPlanQuery(sql.toString());
            }
            if ("1".equals(type)) {
                //list = dao.downloadPlanQueryBb(sql.toString(),Constant.OEM_ACTIVITIES);
            }
            if ("2".equals(type)) {
                list = dao.downloadPlanQueryZz(sql.toString());
            }
            if ("3".equals(type)) {
                list = dao.downloadPlanQueryXxy(sql.toString(), Constant.OEM_ACTIVITIES);
            }
            if ("4".equals(type)) {
                list = dao.downloadPlanQuerysF(sql.toString());
            }
            if ("5".equals(type)) {
                list = dao.downloadPlanQueryDq(sql.toString());
            }
            act.setOutData("amountTotal", list.get(0).get("AMOUNT") == null ? "0" : list.get(0).get("AMOUNT"));
            act.setOutData("saleTotal", list.get(0).get("SELLMONEY") == null ? "0" : list.get(0).get("SELLMONEY"));
            act.setOutData("rate", list.get(0).get("RATIO") == null ? "0" : list.get(0).get("RATIO"));
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-20
     * @Title : 汇总查询
     */
    public void planCountQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));

            StringBuffer sql = new StringBuffer();
            PageResult<Map<String, Object>> ps = null;
            // 处理当前页
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

            //供应中心限制
            if ("0".equals(type)) {
                long orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId();
                sql.append(" and  ( td.DEALER_ID = " + orgId).append(" OR EXISTS\n" +
                        "        (SELECT 1\n" +
                        "           FROM TT_PART_SALES_RELATION R\n" +
                        "          WHERE R.CHILDORG_ID = TD.DEALER_ID\n" +
                        "            AND R.PARENTORG_ID = " + orgId + "))");
            }
            //  服务商代码
            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  td.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(td.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }
            //选择了月
            sql.append(" and TD.task_month >= '");
            sql.append(year);
            sql.append("-" + simMonth(month));
            sql.append("'");
            sql.append(" and TD.task_month <= '");
            sql.append(year);
            sql.append("-" + simMonth(month2));
            sql.append("'");

            //增加大区人员限制 1128
            if (logonUser.getCompanyId().toString().equals(Constant.OEM_ACTIVITIES) && !logonUser.getOrgId().toString().equals(Constant.OEM_ACTIVITIES)
                    && logonUser.getParentOrgId().toString().equals(Constant.OEM_ACTIVITIES)) {
                sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", logonUser));
            }
            if ("0".equals(type)) {
                ps = dao.planCountQuery(sql.toString(), request, curPage, pageSize);
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-23
     * @Title : 汇总统计查询
     */
    public void countAmountQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));

            StringBuffer sql = new StringBuffer();

            if ("0".equals(type)) {
                long orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId();
                sql.append(" and  ( td.DEALER_ID = " + orgId).append(" OR EXISTS\n" +
                        "        (SELECT 1\n" +
                        "           FROM TT_PART_SALES_RELATION R\n" +
                        "          WHERE R.CHILDORG_ID = TD.DEALER_ID\n" +
                        "            AND R.PARENTORG_ID = " + orgId + "))");
            }

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  td.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(td.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }
            //选择了月
            sql.append(" and TD.task_month >= '");
            sql.append(year);
            sql.append("-" + simMonth(month));
            sql.append("'");
            sql.append(" and TD.task_month <= '");
            sql.append(year);
            sql.append("-" + simMonth(month2));
            sql.append("'");
            //增加大区人员限制 1128
            if (logonUser.getCompanyId().toString().equals(Constant.OEM_ACTIVITIES) && !logonUser.getOrgId().toString().equals(Constant.OEM_ACTIVITIES)
                    && logonUser.getParentOrgId().toString().equals(Constant.OEM_ACTIVITIES)) {
                sql.append(CommonUtils.getOrgDealerLimitSqlByPose("TD", logonUser));
            }
            List<Map<String, Object>> list = null;
            if ("0".equals(type)) {
                list = dao.planAmountCountQuery(sql.toString(), request);
            }

            act.setOutData("amountTotal", list.get(0).get("AMOUNT") == null ? 0 : list.get(0).get("AMOUNT"));
            act.setOutData("saleTotal", list.get(0).get("SELLMONEY") == null ? 0 : list.get(0).get("SELLMONEY"));
            act.setOutData("rate", list.get(0).get("RATIO") == null ? 0 : list.get(0).get("RATIO"));
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : monthPlanAdd
     * @Description: 添加
     */
    public void monthPlanAdd() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            String[] dealerIds = request.getParamValues("dealerId");
            String taskMonth = CommonUtils.checkNull(request.getParamValue("taskMonth"));
            int ver = Integer.parseInt(dao.checkVer(taskMonth)) + 1;
            for (int i = 0; i < dealerIds.length; i++) {
                List<String> templist = new ArrayList<String>();
                String code = CommonUtils.checkNull(request.getParamValue("dealerCode_" + dealerIds[i]));
                String name = CommonUtils.checkNull(request.getParamValue("dealerName_" + dealerIds[i]));
                String amount = CommonUtils.checkNull(request.getParamValue("amount_" + dealerIds[i]));

                //作废已经存在的
                dao.updateTaskState(dealerIds[i], taskMonth);
                //重新插入新的
                TtPartDlrMonthTaskPO po = new TtPartDlrMonthTaskPO();
                TtPartDlrTaskMonthDtlPO dtlpo = new TtPartDlrTaskMonthDtlPO();
                po.setTaskId(Long.parseLong(SequenceManager.getSequence("")));
                po.setCreateDate(date);
                po.setCreateBy(logonUser.getUserId());
                po.setTaskMonth(taskMonth);
                po.setVer(String.valueOf(ver));
                po.setState(Constant.STATUS_ENABLE);
                po.setStatus(Constant.STATUS_ENABLE);
                dtlpo.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
                dtlpo.setTaskId(po.getTaskId());
                dtlpo.setDealerId(Long.parseLong(dealerIds[i]));
                dtlpo.setDealerCode(code);
                dtlpo.setDealerName(name);
                dtlpo.setAmount(Double.parseDouble(amount));
                dtlpo.setCreateDate(date);
                dtlpo.setCreateBy(logonUser.getUserId());
                dao.insert(po);
                dao.insert(dtlpo);
            }
            act.setOutData("success", "success");
            monthPlanInputInit();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : monthPlanUpload
     * @Description: 上传
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
                    act.setForword(MONTH_PLAN_INPUT_COM_URL);
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
            monthPlanInputInit();
        }
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @return :
     * @throws : luole
     *           LastDate    : 2013-4-8
     * @Title : 取单元格值
     * @Description: TODO
     */
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo) {
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
                if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "经销商代码");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
                    String dealerId = dao.checkDealer(cells[0].getContents().trim());
                    if ("0".equals(dealerId)) {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "经销商代码");
                        errormap.put("3", "不存在");
                        errorInfo.add(errormap);
                    } else {
                        tempmap.put("dealerId", dealerId);
                    }
                }
                tempmap.put("dealerCode", cells[0].getContents().trim());
                tempmap.put("dealerName", cells.length < 2 || null == cells[1].getContents() ? ""
                        : cells[1].getContents().trim());
                if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "任务金额");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
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


    //本部（初始化页）
    public void monthPlanQueryInitBB() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curMonth = PlanUtil.getCurrentMonth();
            String curYear = PlanUtil.getCurrentYear();
            act.setOutData("curYear", curYear);
            act.setOutData("curMonth", curMonth);
            act.setForword(MONTH_PLAN_QUERY_Bb_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "月度目标查询(本部)");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void planQueryBb() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String is_type = CommonUtils.checkNull(request.getParamValue("is_type"));
            StringBuffer sql = new StringBuffer();
            PageResult<Map<String, Object>> ps = null;
            // 处理当前页
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }


            String a = year + "-" + simMonth(month);
            String b = year + "-" + simMonth(month2);

            if (("0").equals(type)) {
                ps = dao.planPageQueryBb(sql.toString(), Constant.OEM_ACTIVITIES, a, b, is_type, curPage, pageSize);
            }
            if (("3").equals(type)) {
                ps = dao.planPageQueryBb_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, a, b, curPage, pageSize);
            }
            if (("4").equals(type)) {
                ps = dao.planPageQueryBb_Sf(sql.toString(), Constant.OEM_ACTIVITIES, a, b, curPage, pageSize);
            }
            if (("5").equals(type)) {
                ps = dao.planPageQueryBb_Dq(sql.toString(), Constant.OEM_ACTIVITIES, a, b, curPage, pageSize);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void normalCountQueryBb() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            String is_type = CommonUtils.checkNull(request.getParamValue("is_type"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String aa = year + "-" + simMonth(month);
            String bb = year + "-" + simMonth(month2);

            List<Map<String, Object>> list = null;

            if ("0".equals(type)) {
                list = dao.downloadPlanQueryBb(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb, is_type);
            }
            if ("3".equals(type)) {
                list = dao.downloadPlanQueryBb_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
            }
            if ("4".equals(type)) {
                list = dao.downloadPlanQueryBb_Sf(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
            }
            if ("5".equals(type)) {
                list = dao.downloadPlanQueryBb_Dq(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
            }

            double amountTotal = 0.00;
            double saleTotal = 0.00;
            String rate = "0.000%";

            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    amountTotal += Double.parseDouble(list.get(i).get("AMOUNT").toString().replace(",", ""));
                    saleTotal += Double.parseDouble(list.get(i).get("SELLMONEY").toString().replace(",", ""));
                }
            }
            if (0 < amountTotal) {
                double temp = ((saleTotal * 1000) / (amountTotal * 1000)) * 100;
                BigDecimal b = new BigDecimal(temp);
                rate = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
            }
            BigDecimal bd1 = new BigDecimal(amountTotal);
            BigDecimal bd2 = new BigDecimal(saleTotal);

            act.setOutData("amountTotal", bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("saleTotal", bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("rate", rate);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void planCountQueryBb() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            StringBuffer sql = new StringBuffer();
            PageResult<Map<String, Object>> ps = null;
            // 处理当前页
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            if (("0").equals(type)) {
                ps = dao.planCountQueryBb(sql.toString(), Constant.OEM_ACTIVITIES, request, curPage, pageSize);
            }
            if (("3").equals(type)) {
                ps = dao.planCountQueryBb_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, request, curPage, pageSize);
            }
            if (("4").equals(type)) {
                ps = dao.planCountQueryBb_Sf(sql.toString(), Constant.OEM_ACTIVITIES, request, curPage, pageSize);
            }
            if (("5").equals(type)) {
                ps = dao.planCountQueryBb_Dq(sql.toString(), Constant.OEM_ACTIVITIES, request, curPage, pageSize);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void countAmountQueryBb() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            List<Map<String, Object>> list = null;
            if (("0").equals(type)) {
                list = dao.planAmountCountQueryBb(sql.toString(), Constant.OEM_ACTIVITIES, request);
            }
            if (("3").equals(type)) {
                list = dao.planAmountCountQueryBb_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, request);
            }
            if (("4").equals(type)) {
                list = dao.planAmountCountQueryBb_Sf(sql.toString(), Constant.OEM_ACTIVITIES, request);
            }
            if (("5").equals(type)) {
                list = dao.planAmountCountQueryBb_Dq(sql.toString(), Constant.OEM_ACTIVITIES, request);
            }

            double amountTotal = 0.00;
            double saleTotal = 0.00;
            String rate = "0.000%";

            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    amountTotal += Double.parseDouble(list.get(i).get("AMOUNT").toString().replace(",", ""));
                    saleTotal += Double.parseDouble(list.get(i).get("SELLMONEY").toString().replace(",", ""));
                }
            }
            if (0 < amountTotal) {
                double temp = ((saleTotal * 1000) / (amountTotal * 1000)) * 100;
                BigDecimal b = new BigDecimal(temp);
                rate = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
            }
            BigDecimal bd1 = new BigDecimal(amountTotal);
            BigDecimal bd2 = new BigDecimal(saleTotal);

            act.setOutData("amountTotal", bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("saleTotal", bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("rate", rate);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //服务商月度目标(本部)里面的所有下载
    public void downloadDataBb() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();

            //查询条件
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));

            String type = CommonUtils.checkNull(request.getParamValue("type"));
            String is_type = CommonUtils.checkNull(request.getParamValue("is_type"));

            //List<List<Object>> list = new ArrayList<List<Object>>();
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String aa = year + "-" + simMonth(month);
            String bb = year + "-" + simMonth(month2);

            if (("0").equals(type)) {
                RequestWrapper request = act.getRequest();
                String[] head = new String[20];
                head[0] = "年月";
                head[1] = "服务商代码";
                head[2] = "服务商名称";
                head[3] = "任务金额";
                head[4] = "完成金额(元)";
                head[5] = "完成率";

                List<Map<String, Object>> list = dao.downloadPlanQueryBb(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb, is_type);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("AMOUNT")).replace(",", "");
                            detail[4] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[5] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }

                }
                String fileName = "服务商月度目标(本部).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("3").equals(type)) {
                RequestWrapper request = act.getRequest();
                String[] head = new String[20];
                head[0] = "年月";
                head[1] = "销售员";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "完成率";

                List<Map<String, Object>> list = dao.downloadPlanQueryBb_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }

                }
                String fileName = "服务商月度目标(本部销售员).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("4").equals(type)) {
                RequestWrapper request = act.getRequest();
                String[] head = new String[20];
                head[0] = "年月";
                head[1] = "省份";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "完成率";

                List<Map<String, Object>> list = dao.downloadPlanQueryBb_Sf(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("REGION_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }

                }
                String fileName = "服务商月度目标(本部省份).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("5").equals(type)) {

                RequestWrapper request = act.getRequest();
                String[] head = new String[20];
                head[0] = "年月";
                head[1] = "大区";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "完成率";

                List<Map<String, Object>> list = dao.downloadPlanQueryBb_Dq(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("ROOT_ORG_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }

                }
                String fileName = "服务商月度目标(本部大区).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //服务商月度目标(本部)里面的所有的汇总下载
    public void downloadDataBb1() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //查询条件
            String type = CommonUtils.checkNull(request.getParamValue("type"));

            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            if (("0").equals(type)) {
                String[] head = new String[20];
                head[0] = "起止年月";
                head[1] = "服务商代码";
                head[2] = "服务商名称";
                head[3] = "任务金额(元)";
                head[4] = "完成金额(元)";
                head[5] = "完成率";

                List<Map<String, Object>> list = dao.planAmountCountQueryBb(sql.toString(), Constant.OEM_ACTIVITIES, request);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("AMOUNT")).replace(",", "");
                            detail[4] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[5] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }
                }
                String fileName = "服务商月度目标(本部)(汇总下载).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("3").equals(type)) {
                String[] head = new String[20];
                head[0] = "起止年月";
                head[1] = "销售员";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "完成率";

                List<Map<String, Object>> list = dao.planAmountCountQueryBb_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, request);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }
                }
                String fileName = "服务商月度目标(本部销售员)(汇总下载).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("4").equals(type)) {
                String[] head = new String[20];
                head[0] = "起止年月";
                head[1] = "省份";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "完成率";

                List<Map<String, Object>> list = dao.planAmountCountQueryBb_Sf(sql.toString(), Constant.OEM_ACTIVITIES, request);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("REGION_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }
                }
                String fileName = "服务商月度目标(本部省份)(汇总下载).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("5").equals(type)) {
                String[] head = new String[20];
                head[0] = "起止年月";
                head[1] = "大区";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "完成率";

                List<Map<String, Object>> list = dao.planAmountCountQueryBb_Dq(sql.toString(), Constant.OEM_ACTIVITIES, request);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("ROOT_ORG_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }

                }
                String fileName = "服务商月度目标(本部大区)(汇总下载).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //供应中心（初始化页）
    public void monthPlanQueryInitGyzx() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curMonth = PlanUtil.getCurrentMonth();
            String curYear = PlanUtil.getCurrentYear();
            act.setOutData("curYear", curYear);
            act.setOutData("curMonth", curMonth);
            act.setForword(MONTH_PLAN_QUERY_Gyzx_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "月度目标查询(供应中心)");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //供应中心（分页）（查询明细）
    public void planQueryGyzx() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();
            PageResult<Map<String, Object>> ps = null;
            // 处理当前页
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String a = year + "-" + simMonth(month);
            String b = year + "-" + simMonth(month2);

            ps = dao.planPageQueryGyzx(sql.toString(), Constant.OEM_ACTIVITIES, a, b, curPage, pageSize);

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //供应中心（查询明细统计）
    public void normalCountQueryGyzx() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String aa = year + "-" + simMonth(month);
            String bb = year + "-" + simMonth(month2);

            List<Map<String, Object>> list = dao.downloadPlanQueryGyzx(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);

            double amountTotal = 0.00;
            double saleTotal = 0.00;
            String rate = "0.000%";

            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    amountTotal += Double.parseDouble(list.get(i).get("AMOUNT").toString().replace(",", ""));
                    saleTotal += Double.parseDouble(list.get(i).get("SELLMONEY").toString().replace(",", ""));
                }
            }
            if (0 < amountTotal) {
                double temp = ((saleTotal * 1000) / (amountTotal * 1000)) * 100;
                BigDecimal b = new BigDecimal(temp);
                rate = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
            }
            BigDecimal bd1 = new BigDecimal(amountTotal);
            BigDecimal bd2 = new BigDecimal(saleTotal);

            act.setOutData("amountTotal", bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("saleTotal", bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("rate", rate);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //供应中心(汇总查询)(分页)
    public void planCountQueryGyzx() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();
            PageResult<Map<String, Object>> ps = null;
            // 处理当前页
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            ps = dao.planCountQueryGyzx(sql.toString(), Constant.OEM_ACTIVITIES, request, curPage, pageSize);

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //供应中心(汇总统计)
    public void countAmountQueryGyzx() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            List<Map<String, Object>> list = dao.planAmountCountQueryGyzx(sql.toString(), Constant.OEM_ACTIVITIES, request);

            double amountTotal = 0.00;
            double saleTotal = 0.00;
            String rate = "0.000%";

            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    amountTotal += Double.parseDouble(list.get(i).get("AMOUNT").toString().replace(",", ""));
                    saleTotal += Double.parseDouble(list.get(i).get("SELLMONEY").toString().replace(",", ""));
                }
            }
            if (0 < amountTotal) {
                double temp = ((saleTotal * 1000) / (amountTotal * 1000)) * 100;
                BigDecimal b = new BigDecimal(temp);
                rate = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
            }
            BigDecimal bd1 = new BigDecimal(amountTotal);
            BigDecimal bd2 = new BigDecimal(saleTotal);

            act.setOutData("amountTotal", bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("saleTotal", bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("rate", rate);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //服务商月度目标(供应中心)下载
    public void downloadDataGyzx() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();

            //查询条件
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));

            //List<List<Object>> list = new ArrayList<List<Object>>();
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String aa = year + "-" + simMonth(month);
            String bb = year + "-" + simMonth(month2);


            RequestWrapper request = act.getRequest();
            String[] head = new String[20];
            head[0] = "年月";
            head[1] = "供应中心代码";
            head[2] = "供应中心名称";
            head[3] = "服务商代码";
            head[4] = "服务商名称";
            head[5] = "任务金额(元)";
            head[6] = "完成金额(元)";
            head[7] = "完成率";

            List<Map<String, Object>> list = dao.downloadPlanQueryGyzx(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[20];
                        detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                        detail[1] = CommonUtils.checkNull(map.get("SELLER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("AMOUNT"));
                        detail[6] = CommonUtils.checkNull(map.get("SELLMONEY"));
                        detail[7] = CommonUtils.checkNull(map.get("RATIO"));

                        list1.add(detail);
                    }
                }

            }
            String fileName = "服务商月度目标(供应中心).xls";
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //服务商月度目标(供应中心)汇总下载
    public void downloadDataGyzx1() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //查询条件
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String[] head = new String[20];
            head[0] = "起止年月";
            head[1] = "供应中心代码";
            head[2] = "供应中心名称";
            head[3] = "服务商代码";
            head[4] = "服务商名称";
            head[5] = "任务金额(元)";
            head[6] = "完成金额(元)";
            head[7] = "完成率";

            List<Map<String, Object>> list = dao.planAmountCountQueryGyzx(sql.toString(), Constant.OEM_ACTIVITIES, request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[20];
                        detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                        detail[1] = CommonUtils.checkNull(map.get("SELLER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("AMOUNT"));
                        detail[6] = CommonUtils.checkNull(map.get("SELLMONEY"));
                        detail[7] = CommonUtils.checkNull(map.get("RATIO"));

                        list1.add(detail);
                    }
                }

            }
            String fileName = "服务商月度目标(供应中心)(汇总下载).xls";
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //服务商月度目标(终端)（初始化）
    public void monthPlanQueryInitZZ() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curMonth = PlanUtil.getCurrentMonth();
            String curYear = PlanUtil.getCurrentYear();
            act.setOutData("curYear", curYear);
            act.setOutData("curMonth", curMonth);
            act.setForword(MONTH_PLAN_QUERY_Zz_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "月度目标查询(终端)");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //服务商月度目标(终端)(分页)
    public void planQueryZz() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();
            PageResult<Map<String, Object>> ps = null;
            // 处理当前页
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String a = year + "-" + simMonth(month);
            String b = year + "-" + simMonth(month2);

            if (("0").equals(type)) {
                ps = dao.planPageQueryZz(sql.toString(), Constant.OEM_ACTIVITIES, a, b, curPage, pageSize);
            }
            if (("3").equals(type)) {
                ps = dao.planPageQueryZz_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, a, b, curPage, pageSize);
            }
            if (("4").equals(type)) {
                ps = dao.planPageQueryZz_Sf(sql.toString(), Constant.OEM_ACTIVITIES, a, b, curPage, pageSize);
            }
            if (("5").equals(type)) {
                ps = dao.planPageQueryZz_Dq(sql.toString(), Constant.OEM_ACTIVITIES, a, b, curPage, pageSize);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    //服务商月度目标(终端)(合计)
    public void normalCountQueryZz() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String aa = year + "-" + simMonth(month);
            String bb = year + "-" + simMonth(month2);

            List<Map<String, Object>> list = null;

            if ("0".equals(type)) {
                list = dao.downloadPlanQueryZz(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
            }
            if ("3".equals(type)) {
                list = dao.downloadPlanQueryZz_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
            }
            if ("4".equals(type)) {
                list = dao.downloadPlanQueryZz_Sf(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
            }
            if ("5".equals(type)) {
                list = dao.downloadPlanQueryZz_Dq(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
            }

            double amountTotal = 0.00;
            double saleTotal = 0.00;
            double bbsaleTotal = 0.00;
            double gysaleTotal = 0.00;
            String rate = "0.000%";
            String bbrate = "0.000%";
            String gyrate = "0.000%";

            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    amountTotal += Double.parseDouble(list.get(i).get("AMOUNT").toString().replace(",", ""));
                    saleTotal += Double.parseDouble(list.get(i).get("SELLMONEY").toString().replace(",", ""));
                    bbsaleTotal += Double.parseDouble(list.get(i).get("BB_AMOUNT").toString().replace(",", ""));
                    gysaleTotal += Double.parseDouble(list.get(i).get("GY_AMOUNT").toString().replace(",", ""));
                }
            }
            if (0 < amountTotal) {
                double temp = ((saleTotal * 1000) / (amountTotal * 1000)) * 100;
                double bbtemp = ((bbsaleTotal * 1000) / (saleTotal * 1000)) * 100;
                double gytemp = ((gysaleTotal * 1000) / (saleTotal * 1000)) * 100;
                BigDecimal b = new BigDecimal(temp);
                BigDecimal b2 = new BigDecimal(bbtemp);
                BigDecimal b3 = new BigDecimal(gytemp);
                rate = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
                bbrate = b2.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
                gyrate = b3.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
            }
            BigDecimal bd1 = new BigDecimal(amountTotal);
            BigDecimal bd2 = new BigDecimal(saleTotal);
            BigDecimal bd3 = new BigDecimal(bbsaleTotal);
            BigDecimal bd4 = new BigDecimal(gysaleTotal);

            act.setOutData("amountTotal", bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("saleTotal", bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("bbsaleTotal", bd3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("gysaleTotal", bd4.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("rate", rate);
            act.setOutData("bbrate", bbrate);
            act.setOutData("gyrate", gyrate);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //服务商月度目标(终端)(汇总查询)
    public void planCountQueryZz() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            StringBuffer sql = new StringBuffer();
            PageResult<Map<String, Object>> ps = null;
            // 处理当前页
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            if (("0").equals(type)) {
                ps = dao.planCountQueryZz(sql.toString(), Constant.OEM_ACTIVITIES, request, curPage, pageSize);
            }
            if (("3").equals(type)) {
                ps = dao.planCountQueryZz_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, request, curPage, pageSize);
            }
            if (("4").equals(type)) {
                ps = dao.planCountQueryZz_Sf(sql.toString(), Constant.OEM_ACTIVITIES, request, curPage, pageSize);
            }
            if (("5").equals(type)) {
                ps = dao.planCountQueryZz_Dq(sql.toString(), Constant.OEM_ACTIVITIES, request, curPage, pageSize);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //服务商月度目标(终端)(汇总合计)
    public void countAmountQueryZz() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            List<Map<String, Object>> list = null;
            if (("0").equals(type)) {
                list = dao.planAmountCountQueryZz(sql.toString(), Constant.OEM_ACTIVITIES, request);
            }
            if (("3").equals(type)) {
                list = dao.planAmountCountQueryZz_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, request);
            }
            if (("4").equals(type)) {
                list = dao.planAmountCountQueryZz_Sf(sql.toString(), Constant.OEM_ACTIVITIES, request);
            }
            if (("5").equals(type)) {
                list = dao.planAmountCountQueryZz_Dq(sql.toString(), Constant.OEM_ACTIVITIES, request);
            }

            double amountTotal = 0.00;
            double saleTotal = 0.00;
            double bbsaleTotal = 0.00;
            double gysaleTotal = 0.00;
            String rate = "0.000%";
            String bbrate = "0.000%";
            String gyrate = "0.000%";

            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    amountTotal += Double.parseDouble(list.get(i).get("AMOUNT").toString().replace(",", ""));
                    saleTotal += Double.parseDouble(list.get(i).get("SELLMONEY").toString().replace(",", ""));
                    bbsaleTotal += Double.parseDouble(list.get(i).get("BB_AMOUNT").toString().replace(",", ""));
                    gysaleTotal += Double.parseDouble(list.get(i).get("GY_AMOUNT").toString().replace(",", ""));
                }
            }
            if (0 < amountTotal) {
                double temp = ((saleTotal * 1000) / (amountTotal * 1000)) * 100;
                double bbtemp = ((bbsaleTotal * 1000) / (saleTotal * 1000)) * 100;
                double gytemp = ((gysaleTotal * 1000) / (saleTotal * 1000)) * 100;
                BigDecimal b = new BigDecimal(temp);
                BigDecimal b2 = new BigDecimal(bbtemp);
                BigDecimal b3 = new BigDecimal(gytemp);
                rate = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
                bbrate = b2.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
                gyrate = b3.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
            }
            BigDecimal bd1 = new BigDecimal(amountTotal);
            BigDecimal bd2 = new BigDecimal(saleTotal);
            BigDecimal bd3 = new BigDecimal(bbsaleTotal);
            BigDecimal bd4 = new BigDecimal(gysaleTotal);

            act.setOutData("amountTotal", bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("saleTotal", bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("bbsaleTotal", bd3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("gysaleTotal", bd4.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            act.setOutData("rate", rate);
            act.setOutData("bbrate", bbrate);
            act.setOutData("gyrate", gyrate);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    //服务商月度目标(终端)里面的所有下载
    public void downloadDataZz() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();

            //查询条件
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));

            String type = CommonUtils.checkNull(request.getParamValue("type"));

            //List<List<Object>> list = new ArrayList<List<Object>>();
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String aa = year + "-" + simMonth(month);
            String bb = year + "-" + simMonth(month2);

            if (("0").equals(type)) {

                RequestWrapper request = act.getRequest();
                String[] head = new String[20];
                head[0] = "年月";
                head[1] = "服务商代码";
                head[2] = "服务商名称";
                head[3] = "任务金额(元)";
                head[4] = "完成金额(元)";
                head[5] = "本部购买";
                head[6] = "本部占比";
                head[7] = "中心购买";
                head[8] = "中心占比";
                head[9] = "完成率";

                List<Map<String, Object>> list = dao.downloadPlanQueryZz(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[4] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[5] = CommonUtils.checkNull(map.get("BB_AMOUNT"));
                            detail[6] = CommonUtils.checkNull(map.get("BB_RATIO"));
                            detail[7] = CommonUtils.checkNull(map.get("GY_AMOUNT"));
                            detail[8] = CommonUtils.checkNull(map.get("GY_RATIO"));
                            detail[9] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }

                }
                String fileName = "服务商月度目标(终端).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("3").equals(type)) {

                RequestWrapper request = act.getRequest();
                String[] head = new String[20];
                head[0] = "年月";
                head[1] = "销售员";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "本部购买";
                head[5] = "本部占比";
                head[6] = "中心购买";
                head[7] = "中心占比";
                head[8] = "完成率";

                List<Map<String, Object>> list = dao.downloadPlanQueryZz_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("BB_AMOUNT"));
                            detail[5] = CommonUtils.checkNull(map.get("BB_RATIO"));
                            detail[6] = CommonUtils.checkNull(map.get("GY_AMOUNT"));
                            detail[7] = CommonUtils.checkNull(map.get("GY_RATIO"));
                            detail[8] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }

                }
                String fileName = "服务商月度目标(终端销售员).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);

            }
            if (("4").equals(type)) {

                RequestWrapper request = act.getRequest();
                String[] head = new String[20];
                head[0] = "年月";
                head[1] = "省份";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "本部购买";
                head[5] = "本部占比";
                head[6] = "中心购买";
                head[7] = "中心占比";
                head[8] = "完成率";

                List<Map<String, Object>> list = dao.downloadPlanQueryZz_Sf(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("REGION_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("BB_AMOUNT"));
                            detail[5] = CommonUtils.checkNull(map.get("BB_RATIO"));
                            detail[6] = CommonUtils.checkNull(map.get("GY_AMOUNT"));
                            detail[7] = CommonUtils.checkNull(map.get("GY_RATIO"));
                            detail[8] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }

                }
                String fileName = "服务商月度目标(终端省份).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);

            }
            if (("5").equals(type)) {

                RequestWrapper request = act.getRequest();
                String[] head = new String[20];
                head[0] = "年月";
                head[1] = "大区";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "本部购买";
                head[5] = "本部占比";
                head[6] = "中心购买";
                head[7] = "中心占比";
                head[8] = "完成率";

                List<Map<String, Object>> list = dao.downloadPlanQueryZz_Dq(sql.toString(), Constant.OEM_ACTIVITIES, aa, bb);
                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("ROOT_ORG_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("BB_AMOUNT"));
                            detail[5] = CommonUtils.checkNull(map.get("BB_RATIO"));
                            detail[6] = CommonUtils.checkNull(map.get("GY_AMOUNT"));
                            detail[7] = CommonUtils.checkNull(map.get("GY_RATIO"));
                            detail[8] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }

                }
                String fileName = "服务商月度目标(终端大区).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //服务商月度目标(终端)里面的所有汇总下载
    public void downloadDataZz1() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();

            //查询条件
            String year = CommonUtils.checkNull(request.getParamValue("year"));
            String month = CommonUtils.checkNull(request.getParamValue("month"));
            String month2 = CommonUtils.checkNull(request.getParamValue("month2"));

            String type = CommonUtils.checkNull(request.getParamValue("type"));

            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
            StringBuffer sql = new StringBuffer();

            if (!"".equals(dealerCode)) {
                if (dealerCode.split(",").length > 1) {
                    String[] dcs = dealerCode.split(",");
                    StringBuffer tempsql = new StringBuffer();
                    for (String dc : dcs) {
                        tempsql.append("'" + dc + "',");
                    }
                    tempsql.deleteCharAt(tempsql.length() - 1);
                    sql.append(" and  vm.dealer_code in (" + tempsql + ")");
                } else {
                    sql.append(" and  UPPER(vm.dealer_code) LIKE '%" + dealerCode.trim().toUpperCase() + "%'");
                }
            }

            String aa = year + "-" + simMonth(month);
            String bb = year + "-" + simMonth(month2);

            if (("0").equals(type)) {
                String[] head = new String[20];
                head[0] = "起止年月";
                head[1] = "服务商代码";
                head[2] = "服务商名称";
                head[3] = "任务金额(元)";
                head[4] = "完成金额(元)";
                head[5] = "本部购买";
                head[6] = "本部占比";
                head[7] = "中心购买";
                head[8] = "中心占比";
                head[9] = "完成率";

                List<Map<String, Object>> list = dao.planAmountCountQueryZz(sql.toString(), Constant.OEM_ACTIVITIES, request);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[4] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[5] = CommonUtils.checkNull(map.get("BB_AMOUNT"));
                            detail[6] = CommonUtils.checkNull(map.get("BB_RATIO"));
                            detail[7] = CommonUtils.checkNull(map.get("GY_AMOUNT"));
                            detail[8] = CommonUtils.checkNull(map.get("GY_RATIO"));
                            detail[9] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }
                }
                String fileName = "服务商月度目标(终端)(汇总下载).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("3").equals(type)) {
                String[] head = new String[20];
                head[0] = "起止年月";
                head[1] = "销售员";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "本部购买";
                head[5] = "本部占比";
                head[6] = "中心购买";
                head[7] = "中心占比";
                head[8] = "完成率";

                List<Map<String, Object>> list = dao.planAmountCountQueryZz_Xxy(sql.toString(), Constant.OEM_ACTIVITIES, request);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("BB_AMOUNT"));
                            detail[5] = CommonUtils.checkNull(map.get("BB_RATIO"));
                            detail[6] = CommonUtils.checkNull(map.get("GY_AMOUNT"));
                            detail[7] = CommonUtils.checkNull(map.get("GY_RATIO"));
                            detail[8] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }
                }
                String fileName = "服务商月度目标(终端销售员)(汇总下载).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("4").equals(type)) {
                String[] head = new String[20];
                head[0] = "起止年月";
                head[1] = "省份";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "本部购买";
                head[5] = "本部占比";
                head[6] = "中心购买";
                head[7] = "中心占比";
                head[8] = "完成率";

                List<Map<String, Object>> list = dao.planAmountCountQueryZz_Sf(sql.toString(), Constant.OEM_ACTIVITIES, request);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("REGION_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("BB_AMOUNT"));
                            detail[5] = CommonUtils.checkNull(map.get("BB_RATIO"));
                            detail[6] = CommonUtils.checkNull(map.get("GY_AMOUNT"));
                            detail[7] = CommonUtils.checkNull(map.get("GY_RATIO"));
                            detail[8] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }
                }
                String fileName = "服务商月度目标(终端省份)(汇总下载).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
            if (("5").equals(type)) {
                String[] head = new String[20];
                head[0] = "起止年月";
                head[1] = "大区";
                head[2] = "任务金额(元)";
                head[3] = "完成金额(元)";
                head[4] = "本部购买";
                head[5] = "本部占比";
                head[6] = "中心购买";
                head[7] = "中心占比";
                head[8] = "完成率";

                List<Map<String, Object>> list = dao.planAmountCountQueryZz_Dq(sql.toString(), Constant.OEM_ACTIVITIES, request);

                List list1 = new ArrayList();
                if (list != null && list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Map map = (Map) list.get(i);
                        if (map != null && map.size() != 0) {
                            String[] detail = new String[20];
                            detail[0] = CommonUtils.checkNull(map.get("TASK_MONTH"));
                            detail[1] = CommonUtils.checkNull(map.get("ROOT_ORG_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("AMOUNT"));
                            detail[3] = CommonUtils.checkNull(map.get("SELLMONEY"));
                            detail[4] = CommonUtils.checkNull(map.get("BB_AMOUNT"));
                            detail[5] = CommonUtils.checkNull(map.get("BB_RATIO"));
                            detail[6] = CommonUtils.checkNull(map.get("GY_AMOUNT"));
                            detail[7] = CommonUtils.checkNull(map.get("GY_RATIO"));
                            detail[8] = CommonUtils.checkNull(map.get("RATIO"));

                            list1.add(detail);
                        }
                    }
                }
                String fileName = "服务商月度目标(终端大区)(汇总下载).xls";
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, fileName);
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list, String name)
            throws Exception {

        //String name = "配件主数据信息.xls";
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
            //int pageSize=list.size()/30000;
            for (int z = 1; z < list.size() + 1; z++) {
                String[] str = list.get(z - 1);
                for (int i = 0; i < str.length; i++) {
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
