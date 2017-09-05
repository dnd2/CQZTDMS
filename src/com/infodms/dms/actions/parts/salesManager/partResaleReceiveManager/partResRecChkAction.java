package com.infodms.dms.actions.parts.salesManager.partResaleReceiveManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.laborList.LaborListSummaryReportDao;
import com.infodms.dms.dao.parts.salesManager.partResaleReceiveManager.partResRecDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartRetailMainPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 配件零售/领用单业务审核
 * @Date: 2013-4-26
 * @remark
 */
public class partResRecChkAction extends BaseImport {
    public Logger logger = Logger.getLogger(partResRecChkAction.class);
    private static final partResRecDao dao = partResRecDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private static final int orderType1 = Constant.PART_SALE_STOCK_REMOVAL_TYPE_01;//零售
    private static final int orderType2 = Constant.PART_SALE_STOCK_REMOVAL_TYPE_02;//领用
    private static final int orderState1 = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_01;//已保存
    private static final int orderState2 = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_02;//已提交
    private static final int orderState3 = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_03;//已完成
    private static final int orderState4 = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_04;//已通过
    private static final int orderState5 = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_05;//已驳回

    //配件零售/领用单URL
    private static final String PART_RESALE_MAIN = "/jsp/parts/salesManager/partResRecCheck/partResaleMain.jsp";//配件零售单首页
    private static final String PART_RECEIVE_MAIN = "/jsp/parts/salesManager/partResRecCheck/partReceiveMain.jsp";//配件领用单首页
    private static final String PART_RESALE_RECCIVE_VIEW = "/jsp/parts/salesManager/partResaleReceive/partResaleReceiveView.jsp";//配件零售/领用单查看页面
    private static final String PART_RESALE_RECCIVE_CHECK = "/jsp/parts/salesManager/partResRecCheck/partResaleReceiveCheck.jsp";//配件零售/领用单审核页面

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-1
     * @Title : 跳转至配件零售单页面
     */
    public void partResInit() {
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

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("companyName", companyName);
            act.setOutData("WHList", WHList);
            act.setForword(PART_RESALE_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件零售单页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-1
     * @Title : 跳转至配件领用单页面
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

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("companyName", companyName);
            act.setOutData("WHList", WHList);
            act.setForword(PART_RECEIVE_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件领用单页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-27
     * @Title : 查询配件零售/领用单信息
     */
    public void partResRecSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String changeCode = CommonUtils.checkNull(request
                    .getParamValue("changeCode")); // 制单单号
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 类型
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 出库ID
            String orderState = CommonUtils.checkNull(request
                    .getParamValue("orderState")); // 制单状态
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            if (null != changeCode && !"".equals(changeCode)) {
                sbString.append(" AND TM.RETAIL_CODE LIKE '%" + changeCode + "%' ");
            }
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND TM.CHG_TYPE = '" + orderType + "' ");
            }
            if (null != orderState && !"".equals(orderState)) {
                sbString.append(" AND TM.STATE = '" + orderState + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TM.WH_ID = '" + whId + "' ");
            }
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND TM.SORG_ID = '" + parentOrgId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 审核当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSaleOrders(
                    sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件零售/领用单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 跳转至配件零售/领用单查看页面
     */
    public void viewOrderDeatilInint() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));// 变更单ID
            String optionType = CommonUtils.checkNull(request.getParamValue("optionType"));// 操作类型
            String pageType = CommonUtils.checkNull(request.getParamValue("pageType"));// 操作类型
            String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
            int odType = Integer.parseInt(request.getParamValue("orderType"));// 订单类型（页面传值）
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String parentOrgCode = CommonUtils.checkNull(request.getParamValue("parentOrgCode")); //制单单位编码
            String chgorgCname = CommonUtils.checkNull(request.getParamValue("chgorgCname")); //制单单位名称

            StringBuffer sbStr = new StringBuffer();
            sbStr.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbStr.toString());
            LaborListSummaryReportDao laborListSummaryReportDao = LaborListSummaryReportDao.getInstance();

            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.RETAIL_ID = '" + changeId + "' ");
            Map<String, Object> map = dao.queryAllPartSaleOrders(sbString.toString()).get(0);

            int orderType = Integer.parseInt(map.get("CHG_TYPE").toString());

            if (orderType1 == orderType) {
                map.put("CHG_TYPE", "零售");
            } else if (orderType2 == orderType) {
                map.put("CHG_TYPE", "领用");
            }
            act.setOutData("map", map);
            act.setOutData("actionURL", actionURL);
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("chgorgCname", chgorgCname);
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);

            if ("view".equalsIgnoreCase(optionType)) {
                //查看页面
                act.setForword(PART_RESALE_RECCIVE_VIEW);
            } else if ("modify".equalsIgnoreCase(optionType)) {
                List<Map<String, Object>> list = dao.queryPartSaleOrderDeatilList(sbString.toString());

                act.setOutData("orderType", odType);
                act.setOutData("WHList", WHList);
                act.setOutData("list", list);
                act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);

                //领用/零售审核页面
                act.setForword(PART_RESALE_RECCIVE_CHECK);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件零售/领用单查看页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 显示仓库配件库存信息
     */
    public void showPartStockBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        StringBuffer sbStr = new StringBuffer();
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));// 件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));// 配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称

            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbStr.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbStr.append(" AND TD.WH_ID = '" + whId + "' ");
            }
            if (null != partCode && !"".equals(partCode)) {
                sbStr.append(" AND TD.PART_CODE LIKE '%" + partCode + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbStr.append(" AND TD.PART_OLDCODE LIKE '%" + partOldcode + "%' ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbStr.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
            }
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 审核当前页
            PageResult<Map<String, Object>> ps = dao.showPartStockBase(
                    sbStr.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取仓库配件库存信息失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-2
     * @Title : 提交配件零售/领用单信息
     */
    public void commitOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            Long userId = logonUser.getUserId(); //审核人ID
            String retailId = CommonUtils.checkNull(req.getParamValue("retailId")); //制单ID
            String result = CommonUtils.checkNull(req.getParamValue("result")); //审核结果
            String configId = Constant.PART_CODE_RELATION_14 + "";//配置ID

            String str = " AND TM.RETAIL_ID = '" + retailId + "' ";
            List<Map<String, Object>> rtlMainList = dao.queryAllPartSaleOrders(str);
            List<Map<String, Object>> rtlDtlList = null;
            List<Map<String, Object>> partList = null;

            if (null != rtlMainList && rtlMainList.size() == 1) {
                int orderState = Integer.parseInt(rtlMainList.get(0).get("STATE").toString()); //订单状态
                String orderCode = rtlMainList.get(0).get("RETAIL_CODE").toString(); //订单单号

                if (orderState2 == orderState) {
                    TtPartRetailMainPO selectRMPo = new TtPartRetailMainPO();
                    TtPartRetailMainPO updateRMPo = new TtPartRetailMainPO();

                    selectRMPo.setRetailId(Long.parseLong(retailId));

                    if ("pass".equalsIgnoreCase(result)) {
                        updateRMPo.setState(orderState4);//通过
                    } else {
                        updateRMPo.setState(orderState5);//驳回
                    }

                    updateRMPo.setUpdateBy(userId);
                    updateRMPo.setUpdateDate(new Date());


                    if (!"pass".equalsIgnoreCase(result)) {
                        //调用库存释放用逻辑
                        List ins = new LinkedList<Object>();
                        ins.add(0, retailId);
                        ins.add(1, configId);
                        ins.add(2, 0);// 1:占用  0：释放占用
                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                    }

                    dao.update(selectRMPo, updateRMPo);


                } else if (orderState4 == orderState) {
                    errorExist = "单号【" + orderCode + "】已通过,不能重复审核!";
                } else if (orderState5 == orderState) {
                    errorExist = "单号【" + orderCode + "】已驳回,不能再审核!";
                } else {
                    errorExist = "审核失败,请联系管理员!";
                }
            } else {
                errorExist = "审核失败,请联系管理员!";
            }

            act.setOutData("errorExist", errorExist);// 返回错误信息
            act.setOutData("success", "true");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "审核配件零售/领用单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 导出配件销售零售/领用单EXECEL模板
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
            listHead.add("零售/领用数量 ");
            listHead.add("备注");

            list.add(listHead);

            // 导出的文件名
            String fileName = "配件零售领用单模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出配件零售领用单模板错误");
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
     * @throws : LastDate    : 2013-4-22
     * @Title : 配件零售/领用单详情查询
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
                    .parseInt(request.getParamValue("curPage")) : 1; // 审核当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSaleOrderDeatil(
                    sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件零售/领用单详情信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-27
     * @Title :导出配件零售/领用单信息
     */
    public void exportSaleOrdersExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String changeCode = CommonUtils.checkNull(request
                    .getParamValue("changeCode")); // 制单单号
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 类型
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 截止时间
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            if (null != changeCode && !"".equals(changeCode)) {
                sbString.append(" AND TM.RETAIL_CODE LIKE '%" + changeCode + "%' ");
            }
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND TM.CHG_TYPE = '" + orderType + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TM.WH_ID = '" + whId + "' ");
            }
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND TM.SORG_ID = '" + parentOrgId + "' ");
            }
            String[] head = new String[9];
            head[0] = "序号";
            head[1] = "制单单号";
            head[2] = "类型";
            head[3] = "制单单位";
            head[4] = "仓库";
            head[5] = "制单人";
            head[6] = "制单日期";
            head[7] = "制单金额(元)";
            head[8] = "状态";
            List<Map<String, Object>> list = dao.queryAllPartSaleOrders(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[9];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("RETAIL_CODE"));
                        Integer oderType = Integer.parseInt(CommonUtils.checkNull(map.get("CHG_TYPE")));
                        if (orderType1 == oderType) {
                            detail[2] = "零售";
                        } else if (orderType2 == oderType) {
                            detail[2] = "领用";
                        }
                        detail[3] = CommonUtils
                                .checkNull(map.get("SORG_CNAME"));
                        detail[4] = CommonUtils
                                .checkNull(map.get("WH_CNAME"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("NAME"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("CREATE_DATE"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("AMOUNTS"));
                        int orderState = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")));
                        if (orderState1 == orderState) {
                            detail[8] = "已保存";
                        } else if (orderState2 == orderState) {
                            detail[8] = "已提交";
                        } else if (orderState3 == orderState) {
                            detail[8] = "已完成";
                        } else if (orderState4 == orderState) {
                            detail[8] = "已通过";
                        } else if (orderState4 == orderState) {
                            detail[8] = "已驳回";
                        }

                        list1.add(detail);
                    }
                }
            }
            String fileName = "";
            if ((orderType1 + "").equals(orderType + "")) {
                fileName = "配件零售单信息";
            } else {
                fileName = "配件领用单信息";
            }

            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件零售/领用单");
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
