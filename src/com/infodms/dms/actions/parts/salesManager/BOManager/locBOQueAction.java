package com.infodms.dms.actions.parts.salesManager.BOManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.salesManager.BOManager.localBODao;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理现场BO单查询业务
 * @Date: 2013-5-13
 * @remark
 */
public class locBOQueAction extends BaseImport {
    public Logger logger = Logger.getLogger(locBOQueAction.class);
    private static final localBODao dao = localBODao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private static final int orderType1 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01;//紧急
    private static final int orderType2 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02;//常规
    private static final int orderType3 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03;//计划
    private static final int orderType4 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04;//直发
    private static final int orderType5 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05;//调拨
    private static final int boState1 = Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_01;//已保存
    private static final int boState2 = Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_02;//部分处理
    private static final int boState3 = Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03;//已处理
    private static final int payType1 = Constant.CAR_FACTORY_SALES_PAY_TYPE_01;//现金
    private static final int payType2 = Constant.CAR_FACTORY_SALES_PAY_TYPE_02;//支票
    private static final int payType3 = Constant.CAR_FACTORY_SALES_PAY_TYPE_03;//其他

    //配件现场BO单查询
    private static final String PART_STOCK_MAIN = "/jsp/parts/salesManager/BOManager/localBOQuery/locBOQueMain.jsp";//配件现场BO单查询首页
    
    private static final String PART_STOCK_VIEW = "/jsp/parts/salesManager/BOManager/localBOQuery/locBOQueView.jsp";//现场BO单查询查看页面
    private static final String LOC_BO_COUNT_URL = "/jsp/parts/salesManager/BOManager/localBOQuery/locBOCntPrev.jsp";//现场BO汇总查询页面

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-5-6
     * @Title : 跳转至配件现场BO单查询页面
     */
    public void locBOQueInit() {
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
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setForword(PART_STOCK_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件现场BO单查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title : 查询配件现场BO单查询信息
     */
    public void locBOQueSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String sellCode = CommonUtils.checkNull(request.getParamValue("sellCode")); // 销售单号
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 订货单位
            String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName")); // 销售单位
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 出库仓库ID
            String locBOCode = CommonUtils.checkNull(request.getParamValue("locBOCode")); // 现场BO单号
            String locBOState = CommonUtils.checkNull(request.getParamValue("locBOState")); // BO单状态
            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 现场BO开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 现场BO截止时间
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码

            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            if (null != sellCode && !"".equals(sellCode)) {
                sbString.append(" AND UPPER(SM.SO_CODE) like '%" + sellCode.trim().toUpperCase() + "%' ");
            }
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND SM.ORDER_TYPE = '" + orderType + "' ");
            }
            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND SM.DEALER_NAME LIKE '%" + dealerName + "%' ");
            }
            if (null != sellerName && !"".equals(sellerName)) {
                sbString.append(" AND SM.SELLER_NAME LIKE '%" + sellerName + "%' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND SM.WH_ID = '" + whId + "' ");
            }
            if (null != locBOCode && !"".equals(locBOCode)) {
                sbString.append(" AND UPPER(BM.BO_CODE) LIKE '%" + locBOCode.trim().toUpperCase() + "%' ");
            }
            if (null != locBOState && !"".equals(locBOState)) {
                //未处理
                if ("1".equals(locBOState)) {
                    sbString.append(" AND BM.LOC_STATE IN ('" + boState1 + "','" + boState2 + "') ");
                    sbString.append(" AND BD.LOC_STATUS = '1' ");

                }
                //已处理
                else {
                    sbString.append(" AND BD.LOC_CLOSE_QTY > 0 ");
                }
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(BM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(BM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }

            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND (SM.SELLER_ID = '" + parentOrgId + "' OR SM.DEALER_ID = '" + parentOrgId + "') ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbString.append(" AND UPPER(BD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            
            PageResult<Map<String, Object>> ps = dao.queryPartLocBOInfos(sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件现场BO单查询信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 跳转至现场BO单查询查看页面
     */
    public void viewDeatilInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String boId = CommonUtils.checkNull(request.getParamValue("boId"));// 盘点结果单ID
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND BM.BO_ID = '" + boId + "' ");
            Map<String, Object> map = dao.queryPartLocBOInfosList(sbString.toString()).get(0);

            int orderType = Integer.parseInt(map.get("ORDER_TYPE").toString());
            int boState = Integer.parseInt(map.get("LOC_STATE").toString());
            int payType = Integer.parseInt(map.get("PAY_TYPE").toString());

            if (orderType1 == orderType) {
                map.put("ORDER_TYPE", "紧急");
            } else if (orderType2 == orderType) {
                map.put("ORDER_TYPE", "常规");
            } else if (orderType3 == orderType) {
                map.put("ORDER_TYPE", "计划");
            } else if (orderType4 == orderType) {
                map.put("ORDER_TYPE", "直发");
            } else if (orderType5 == orderType) {
                map.put("ORDER_TYPE", "调拨");
            }

            if (boState1 == boState) {
                map.put("STATE", "已保存");
            } else if (boState2 == boState) {
                map.put("STATE", "部分处理");
            } else if (boState3 == boState) {
                map.put("STATE", "已处理");
            }

            if (payType1 == payType) {
                map.put("PAY_TYPE", "现金");
            } else if (payType2 == payType) {
                map.put("PAY_TYPE", "支票");
            } else if (payType3 == payType) {
                map.put("PAY_TYPE", "其他");
            }

            act.setOutData("map", map);
            act.setForword(PART_STOCK_VIEW);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "现场BO单查询查看或查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title : 现场BO单查询详情查询
     */
    public void partLocBODetailSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String boId = CommonUtils.checkNull(request.getParamValue("boId")); // BO详情单ID

            StringBuffer sbString = new StringBuffer();
            if (null != boId && !"".equals(boId)) {
                sbString.append(" AND BD.BO_ID = '" + boId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartBODeatil(sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件现场BO单查询信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title : 现场BO汇总查询初始化
     */
    public void countQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
            }

            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("WHList", WHList);
            act.setForword(LOC_BO_COUNT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "现场BO汇总查询初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title : 现场BO汇总查询
     */
    public void locBOCountSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String optionType = "view";
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 订货单位
            String partOldcode = CommonUtils.checkNull(request
                    .getParamValue("partOldcode")); // 配件编码
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 现场BO开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 现场BO截止时间
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID


            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryLocBOCntInfos(dealerName, partOldcode, checkSDate, checkEDate, parentOrgId, optionType, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件现场BO汇总信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title : 汇总查询导出
     */
    public void exportLocBoCountExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String optionType = "view";
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 订货单位
            String partOldcode = CommonUtils.checkNull(request
                    .getParamValue("partOldcode")); // 配件编码
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 现场BO开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 现场BO截止时间
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID


            String[] head = new String[5];
            head[0] = "序号";
            head[1] = "订货单位";
            head[2] = "销售单位";
            head[3] = "现场BO总数";
            List<Map<String, Object>> list = dao.queryLocBOCntInfos(dealerName, partOldcode, checkSDate, checkEDate, parentOrgId, optionType);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("SELLER_NAME"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("BO_TOTAL_QTY"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件现场BO单汇总信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件现场BO单汇总信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-5-3
     * @Title :导出配件现场BO单查询信息
     */
    public void exportLocBOExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String sellCode = CommonUtils.checkNull(request
                    .getParamValue("sellCode")); // 销售单号
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 订单类型
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 订货单位
            String sellerName = CommonUtils.checkNull(request
                    .getParamValue("sellerName")); // 销售单位
            String whId = CommonUtils.checkNull(request
                    .getParamValue("whId")); // 出库仓库ID
            String locBOCode = CommonUtils.checkNull(request
                    .getParamValue("locBOCode")); // 现场BO单号
            String locBOState = CommonUtils.checkNull(request
                    .getParamValue("locBOState")); // BO单状态
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 现场BO开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 现场BO截止时间
            String partOldcode = CommonUtils.checkNull(request
                    .getParamValue("partOldcode")); // 配件编码

            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            StringBuffer sbString = new StringBuffer();
            if (null != sellCode && !"".equals(sellCode)) {
                sbString.append(" AND UPPER(SM.SO_CODE) like '%" + sellCode.trim().toUpperCase() + "%' ");
            }
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND SM.ORDER_TYPE = '" + orderType + "' ");
            }
            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND SM.DEALER_NAME LIKE '%" + dealerName + "%' ");
            }
            if (null != sellerName && !"".equals(sellerName)) {
                sbString.append(" AND SM.SELLER_NAME LIKE '%" + sellerName + "%' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND SM.WH_ID = '" + whId + "' ");
            }
            if (null != locBOCode && !"".equals(locBOCode)) {
                sbString.append(" AND UPPER(BM.BO_CODE) LIKE '%" + locBOCode.trim().toUpperCase() + "%' ");
            }
            if (null != locBOState && !"".equals(locBOState)) {
                //未处理
                if ("1".equals(locBOState)) {
                    sbString.append(" AND BM.LOC_STATE IN ('" + boState1 + "','" + boState2 + "') ");
                    sbString.append(" AND BD.LOC_STATUS = '1' ");

                }
                //已处理
                else {
                    sbString.append(" AND BD.LOC_CLOSE_QTY > 0 ");
                }
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(BM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(BM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }

            if (null != parentOrgId && !"".equals(parentOrgId)) {
                sbString.append(" AND (SM.SELLER_ID = '" + parentOrgId + "' OR SM.DEALER_ID = '" + parentOrgId + "') ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbString.append(" AND UPPER(BD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "销售号";
            head[2] = "订货单位";
            head[3] = "出库仓库";
            head[4] = "现场BO日期";
            head[5] = "配件编码";
            head[6] = "配件名称";
            head[7] = "件号";
            head[8] = "单位";
            //未处理
            if ("1".equals(locBOState)) {
                head[9] = "可关闭BO数量";
                head[10] = "BO单状态";
                head[11] = "关闭数量";
                head[12] = "备注";

            }
            //已处理
            else {
                head[9] = "已关闭BO数量";
//				head[10] = "BO单状态";
                head[10] = "关闭人";
                head[11] = "关闭时间";
                head[12] = "备注";
            }

            List<Map<String, Object>> list = dao.queryPartLocBOInfosList(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("SO_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("WH_NAME"));
                        detail[4] = CommonUtils
                                .checkNull(map.get("BM_DATE"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("PART_CNAME"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("PART_CODE"));
                        detail[8] = CommonUtils.checkNull(map
                                .get("UNIT"));

                        //未处理
                        if ("1".equals(locBOState)) {
                            detail[9] = CommonUtils.checkNull(map
                                    .get("BO_ODDQTY"));
                            detail[10] = "未处理";
                            detail[11] = "";
                            detail[12] = "";

                        }
                        //已处理
                        else {
                            detail[9] = CommonUtils.checkNull(map
                                    .get("CLOSE_QTY"));
                            detail[10] = CommonUtils.checkNull(map
                                    .get("U_NAME"));
                            detail[11] = CommonUtils.checkNull(map
                                    .get("UPDATE_DATE"));
                            detail[12] = CommonUtils.checkNull(map
                                    .get("REMARK"));
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件现场BO单信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件现场BO单查询");
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
     * @throws : LastDate    : 2013-5-3
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
