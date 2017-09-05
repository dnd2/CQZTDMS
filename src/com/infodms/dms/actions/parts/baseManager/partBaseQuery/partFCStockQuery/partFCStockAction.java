package com.infodms.dms.actions.parts.baseManager.partBaseQuery.partFCStockQuery;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partBaseQuery.partFCStockQuery.partFCStockDao;
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

//      /parts/baseManager/partBaseQuery/partFCStockQuery/partFCStockAction/partFCStockInit

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理配件封存查询业务
 * @Date: 2013-5-4
 * @remark
 */
public class partFCStockAction extends BaseImport {
    public Logger logger = Logger.getLogger(partFCStockAction.class);
    private static final partFCStockDao dao = partFCStockDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private static final int PART_STOCK_ZC = 1;//正常
    private static final int PART_STATE_ZC = 2;//正常封存
    private static final int PART_STATE_PD = 3;//盘点封存

    //配件封存查询
    private static final String PART_STOCK_QUERY_MAIN = "/jsp/parts/baseManager/partBaseQuery/partFCStockQuery/partFCStockMain.jsp";//配件封存查询首页
    private static final String PART_PD_STOCK_DETAIL = "/jsp/parts/baseManager/partBaseQuery/partFCStockQuery/partPDStockDetail.jsp";//配件盘点封存详情
    private static final String PART_ZC_STOCK_DETAIL = "/jsp/parts/baseManager/partBaseQuery/partFCStockQuery/partZCStockDetail.jsp";//配件正常封存详情
    private static final String PART_IN_STOCK_DETAIL = "/jsp/parts/baseManager/partBaseQuery/partFCStockQuery/partINStockDetail.jsp";//配件入库详情
    private static final String PART_OUT_STOCK_DETAIL = "/jsp/parts/baseManager/partBaseQuery/partFCStockQuery/partOUTStockDetail.jsp";//配件出库详情
    private static final String PART_ZY_STOCK_DETAIL = "/jsp/parts/baseManager/partBaseQuery/partFCStockQuery/partZYStockDetail.jsp";//配件出库详情

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-5-4
     * @Title : 跳转至配件封存查询页面
     */
    public void partFCStockInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            StringBuffer sbString = new StringBuffer();

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
            sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
            act.setOutData("companyName", companyName);
            act.setOutData("WHList", WHList);
            act.setForword(PART_STOCK_QUERY_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件封存查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 查询配件封存信息
     */
    public void partFCStockSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname")); // 配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String searchType = CommonUtils.checkNull(request.getParamValue("searchType")); // 查询类型
            String isLocked = CommonUtils.checkNull(request.getParamValue("isLocked")); // 是否锁定
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 是否有效

            StringBuffer sbString = new StringBuffer();

            if ("specific".equalsIgnoreCase(searchType)) {
                String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称

                if (null != dealerCode && !"".equals(dealerCode)) {
                    sbString.append(" AND UPPER(DL.DEALER_CODE) LIKE '%" + dealerCode.toUpperCase() + "%' ");
                }
                if (null != dealerName && !"".equals(dealerName)) {
                    sbString.append(" AND DL.DEALER_NAME LIKE '%" + dealerName + "%' ");
                }
                sbString.append(" AND TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' ");
            } else {
                if (null != parentOrgId && !"".equals(parentOrgId)) {
                    sbString.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
                }
            }
            if (null != partCode && !"".equals(partCode)) {
                sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbString.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TD.WH_ID = '" + whId + "' ");
            }
            if (null != isLocked && !"".equals(isLocked)) {
                sbString.append(" AND TD.IS_LOCKED = '" + isLocked + "' ");
            }
            if (null != state && !"".equals(state)) {
                sbString.append(" AND TD.PDSTATE = '" + state + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartStockBase(
                    sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件封存信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title : 配件详情查看初始化
     */
    public void showPDDetInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));// 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID
            String viewPage = CommonUtils.checkNull(request.getParamValue("viewPage"));// 跳转页面

            act.setOutData("partId", partId);
            act.setOutData("whId", whId);

            if ("pdPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_PD_STOCK_DETAIL);
            } else if ("zcPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_ZC_STOCK_DETAIL);
            } else if ("inPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_IN_STOCK_DETAIL);
            } else if ("outPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_OUT_STOCK_DETAIL);
            } else if ("zyPage".equalsIgnoreCase(viewPage)) {
                act.setForword(PART_ZY_STOCK_DETAIL);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "盘点封存详情初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title : 盘点封存详情查询
     */
    public void showPDDetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String fcName = CommonUtils.checkNull(request.getParamValue("fcName")); // 封存人
            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间

            String showType = CommonUtils.checkNull(request.getParamValue("showType")); // 显示类型

            StringBuffer sbString = new StringBuffer();
            if (null != partId && !"".equals(partId)) {
                sbString.append(" AND R.PART_ID = '" + partId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND R.WH_ID = '" + whId + "' ");
            }
            if (null != fcName && !"".equals(fcName)) {
                sbString.append(" AND R.PERSON_NAME LIKE '%" + fcName + "%' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }

            if ("pdfc".equalsIgnoreCase(showType)) {
                sbString.append(" AND R.PART_STATE = '" + PART_STATE_PD + "' ");
            } else if ("zcfc".equalsIgnoreCase(showType)) {
                sbString.append(" AND R.PART_STATE = '" + PART_STATE_ZC + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartPDStockDT(
                    sbString.toString(), 12, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件盘点封存详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title : 占用详情
     */
    public void showPartZYDetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String OrgId = "";//父机构（销售单位）ID

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                OrgId = Constant.OEM_ACTIVITIES;
            } else {
                OrgId = logonUser.getDealerId();
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartZYStockDT(
                    request, OrgId, 12, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件盘点封存详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-11
     * @Title : 配件出入库明细查询
     */
    public void showInOrOutDetSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String OrgId = "";//父机构（销售单位）ID

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                OrgId = Constant.OEM_ACTIVITIES;
            } else {
                OrgId = logonUser.getDealerId();
            }

            String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID

            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间

            String showType = CommonUtils.checkNull(request.getParamValue("showType")); // 显示类型

            PageResult<Map<String, Object>> ps = null;
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

            StringBuffer sbString = new StringBuffer();
            if (null != partId && !"".equals(partId)) {
                sbString.append(" AND R.PART_ID = '" + partId + "' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND R.WH_ID = '" + whId + "' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(R.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }

            sbString.append(" AND R.PART_STATE = '" + PART_STOCK_ZC + "' "); //正常

            if ("inDetail".equalsIgnoreCase(showType)) {
                String checkCode = CommonUtils.checkNull(request.getParamValue("checkCode")); // 验货单号
                String inCode = CommonUtils.checkNull(request.getParamValue("inCode")); // 入库单号

                sbString.append(" AND R.ADD_FLAG = 1 "); //入库

                ps = dao.showPartInStockDT(sbString.toString(), checkCode, inCode, 12, curPage);
            } else if ("outDetail".equalsIgnoreCase(showType)) {
                String soCode = CommonUtils.checkNull(request.getParamValue("soCode")); // 销售单号
                String outCode = CommonUtils.checkNull(request.getParamValue("outCode")); // 出库单号

                sbString.append(" AND R.ADD_FLAG = 2 "); //出库

                ps = dao.showPartOutStockDT(sbString.toString(), soCode, outCode, 12, curPage);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件出入库详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-5-4
     * @Title :导出配件封存信息
     */
    public void exportPartStockStatusExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode")); // 配件件号
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname")); // 配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode")); // 配件编码
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
            String searchType = CommonUtils.checkNull(request.getParamValue("searchType")); // 查询类型
            String isLocked = CommonUtils.checkNull(request.getParamValue("isLocked")); // 是否锁定
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); // 是否有效

            StringBuffer sbString = new StringBuffer();
            if ("specific".equalsIgnoreCase(searchType)) {
                String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 服务商编码
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); // 服务商名称

                if (null != dealerCode && !"".equals(dealerCode)) {
                    sbString.append(" AND UPPER(DL.DEALER_CODE) LIKE '%" + dealerCode.trim().toUpperCase() + "%' ");
                }
                if (null != dealerName && !"".equals(dealerName)) {
                    sbString.append(" AND DL.DEALER_NAME LIKE '%" + dealerName + "%' ");
                }
                sbString.append(" AND TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' ");
            } else {
                if (null != parentOrgId && !"".equals(parentOrgId)) {
                    sbString.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
                }
            }
            if (null != partCode && !"".equals(partCode)) {
                sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbString.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
            }
            if (null != whId && !"".equals(whId)) {
                sbString.append(" AND TD.WH_ID = '" + whId + "' ");
            }
            if (null != isLocked && !"".equals(isLocked)) {
                sbString.append(" AND TD.IS_LOCKED = '" + isLocked + "' ");
            }
            if (null != state && !"".equals(state)) {
                sbString.append(" AND TD.PDSTATE = '" + state + "' ");
            }

            String[] head = new String[20];

            if (!"specific".equalsIgnoreCase(searchType)) {
                head[0] = "序号";
                head[1] = "仓库";
                head[2] = "配件编码";
                head[3] = "配件名称";
                //head[4] = "件号";
                head[4] = "单位";
                head[5] = "货位";
                head[6] = "可用库存";
                head[7] = "占用封存";
                head[8] = "来货错误";
                head[9] = "质量问题";
                head[10] = "借条处理";
                head[11] = "现场BO";
                head[12] = "普通封存";
                head[13] = "盘亏封存";
                head[14] = "账面封存";
                head[15] = "盘盈封存";

                /*if (Constant.OEM_ACTIVITIES.equals(parentOrgId)) {
                    head[16] = "计划价(元)";
                } else {
                    head[16] = "采购价(元)";
                } 20170830屏蔽
                head[17] = "是否锁定";
                head[18] = "是否有效";*/

                head[16] = "是否锁定";
                head[17] = "是否有效";
            } else {
                head[0] = "序号";
                head[1] = "服务商编码";
                head[2] = "服务商名称";
                head[3] = "仓库";
                head[4] = "配件编码";
                head[5] = "配件名称";
                //head[6] = "件号";
                head[6] = "单位";
                head[7] = "货位";
                head[8] = "可用库存";
                head[9] = "占用封存";
                head[10] = "正常封存";
                head[11] = "账面封存";
                head[12] = "盘点封存";
//                head[13] = "采购价(元)";//20170830屏蔽
//                head[14] = "是否锁定";
//                head[15] = "是否有效";
                head[13] = "是否锁定";
                head[14] = "是否有效";
            }

            List<Map<String, Object>> list = dao.showPartStockBase(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[20];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        if (!"specific".equalsIgnoreCase(searchType)) {
                        	detail[1] = CommonUtils.checkNull(map.get("WH_NAME"));
                            detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                            detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
//							detail[4] = CommonUtils.checkNull(map.get("PART_CODE"));
                            detail[4] = CommonUtils.checkNull(map.get("UNIT"));
                            detail[5] = CommonUtils.checkNull(map.get("LOC_NAME"));
                            detail[6] = CommonUtils.checkNull(map.get("NORMAL_QTY"));
                            detail[7] = CommonUtils.checkNull(map.get("BOOKED_QTY"));
                            detail[8] = CommonUtils.checkNull(map.get("LHCW"));
                            detail[9] = CommonUtils.checkNull(map.get("ZLWT"));
                            detail[10] = CommonUtils.checkNull(map.get("JTCL"));
                            detail[11] = CommonUtils.checkNull(map.get("XCBO"));
                            detail[12] = CommonUtils.checkNull(map.get("PTFC"));
                            detail[13] = CommonUtils.checkNull(map.get("PKFC_QTY"));
                            detail[14] = CommonUtils.checkNull(map.get("ITEM_QTY"));
                            detail[15] = CommonUtils.checkNull(map.get("PDFC_QTY"));
                            /*detail[16] = CommonUtils.checkNull(map.get("PRICE")); 20170830屏蔽*/
                            if ("1".equals(CommonUtils.checkNull(map.get("IS_LOCKED")))) {
                                detail[16] = "是";
                            } else {
                                detail[16] = "否";
                            }
                            if ((Constant.STATUS_ENABLE + "").equals(map.get("STATE").toString())) {
                                detail[17] = "有效";
                            } else {
                                detail[17] = "无效";
                            }
                        } else {
                        	detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("WH_NAME"));
                            detail[4] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                            detail[5] = CommonUtils.checkNull(map.get("PART_CNAME"));
//							detail[6] = CommonUtils.checkNull(map.get("PART_CODE"));
                            detail[6] = CommonUtils.checkNull(map.get("UNIT"));
                            detail[7] = CommonUtils.checkNull(map.get("LOC_NAME"));
                            detail[8] = CommonUtils.checkNull(map.get("NORMAL_QTY"));
                            detail[9] = CommonUtils.checkNull(map.get("BOOKED_QTY"));
                            detail[10] = CommonUtils.checkNull(map.get("ZCFC_QTY"));
                            detail[11] = CommonUtils.checkNull(map.get("ITEM_QTY"));
                            detail[12] = CommonUtils.checkNull(map.get("PDFC_QTY"));
                           /* detail[13] = CommonUtils.checkNull(map.get("PRICE")); 20170830屏蔽*/

                            if ("1".equals(CommonUtils.checkNull(map.get("IS_LOCKED")))) {
                                detail[13] = "是";
                            } else {
                                detail[13] = "否";
                            }
                            if ((Constant.STATUS_ENABLE + "").equals(map.get("PDSTATE").toString())) {
                                detail[14] = "有效";
                            } else {
                                detail[14] = "无效";
                            }
                        }

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件封存信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件封存信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expPartStockAmountExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String groupType = CommonUtils.checkNull(request.getParamValue("groupType"));// 汇总类型

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "汇总类型";
            head[2] = "汇总数量";
            head[3] = "占用资金";

            List<Map<String, Object>> list = dao.showPartGroupAmount(parentOrgId, groupType);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("GROUP_TYPE"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("GROUP_AMOUNT"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("GROUP_COUNT"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件占用资金汇总查询信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件封存信息");
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
