package com.infodms.dms.actions.parts.salesManager.partInStockQueManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.salesManager.partInStockQueManager.inStockQueDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
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
 * @Title: 处理入库单查询业务
 * @Date: 2013-5-14
 * @remark
 */
public class inStockQueAction extends BaseImport {
    private static final inStockQueDao dao = inStockQueDao.getInstance();
    private static final int orderType1 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01;//紧急
    private static final int orderType2 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02;//常规
    private static final int orderType3 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03;//计划
    private static final int orderType4 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04;//直发
    private static final int orderType5 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05;//调拨
    private static final int orderType8 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08;//促销订单
    private static final int orderType12 = Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12;//销售采购订单
    private static final String USER_ROLE_OEM = "1"; //主机厂
    private static final String USER_ROLE_PDR = "2"; //供应中心
    private static final String USER_ROLE_DLR = "3"; //服务商
    //配件入库单查询
    private static final String PART_STOCK_MAIN = "/jsp/parts/salesManager/partInStockQueManager/inStockQueMain.jsp";//配件入库单查询首页
    private static final String PART_STOCK_VIEW = "/jsp/parts/salesManager/partInStockQueManager/inStockQueView.jsp";//入库单查询查看页面
    public Logger logger = Logger.getLogger(inStockQueAction.class);

    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

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

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-5-6
     * @Title : 跳转至配件入库单查询页面
     */
    public void inStockQueInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String companyName = ""; //制单单位
            String userRole = "";//用户角色
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                userRole = USER_ROLE_OEM;
                companyName = dao.getMainCompanyName(parentOrgId);
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
                companyName = dao.getDealerName(parentOrgId);
                TmDealerPO selPo = new TmDealerPO();

                selPo.setDealerId(Long.parseLong(parentOrgId));

                List<TmDealerPO> dlrList = dao.select(selPo);
                TmDealerPO resPo = null;
                if (null != dlrList && dlrList.size() == 1) {
                    resPo = dlrList.get(0);
                    int pDlrType;
                    if (null != resPo.getPdealerType() && !"".equals(resPo.getPdealerType())) {
                        pDlrType = resPo.getPdealerType();
                        if (Constant.PART_SALE_PRICE_DEALER_TYPE_01 == pDlrType) {
                            userRole = USER_ROLE_PDR;
                        } else {
                            userRole = USER_ROLE_DLR;
                        }
                    } else {
                        userRole = USER_ROLE_DLR;
                    }
                }
            }

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("companyName", companyName);
            act.setOutData("userRole", userRole);
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_STOCK_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件入库单查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title : 查询配件入库单查询信息
     */
    public void inStockQueSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String inCode = CommonUtils.checkNull(request
                    .getParamValue("inCode")); // 入库单号
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 订单类型
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 采购单位
            String sellerName = CommonUtils.checkNull(request
                    .getParamValue("sellerName")); // 销售单位
            String orderCode = CommonUtils.checkNull(request
                    .getParamValue("orderCode")); // 采购单号
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 入库开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 入库截止时间

            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String userRole = CommonUtils.checkNull(request.getParamValue("userRole")); // 用户角色
            StringBuffer sbString = new StringBuffer();
            if (null != inCode && !"".equals(inCode)) {
                sbString.append(" AND SALE_DATE like '%" + inCode + "%' ");
            }
            if (null != orderCode && !"".equals(orderCode)) {
                sbString.append(" AND IM.ORDER_CODE like '%" + orderCode + "%' ");
            }
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND IM.ORDER_TYPE = '" + orderType + "' ");
            }
            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND IM.DEALER_NAME LIKE '%" + dealerName + "%' ");
            }
            if (null != sellerName && !"".equals(sellerName)) {
                sbString.append(" AND IM.SELLER_NAME LIKE '%" + sellerName + "%' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(IM.SALE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(IM.SALE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
           /* if (null != parentOrgId && !"".equals(parentOrgId)) {
                if (USER_ROLE_OEM.equals(userRole)) {
                    sbString.append(" AND IM.SELLER_ID = '" + parentOrgId + "' ");
                } else if (USER_ROLE_PDR.equals(userRole)) {
                    sbString.append(" AND " + parentOrgId + " IN (IM.SELLER_ID, IM.DEALER_ID) ");
                } else {
                    sbString.append(" AND IM.DEALER_ID = '" + parentOrgId + "' ");
                }
            }*/

            sbString.append(" AND IM.DEALER_ID = ").append(logonUser.getDealerId());

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryInStockInfos(
                    sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件入库单查询信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-6
     * @Title : 跳转至入库单查询查看页面
     */
    public void viewDeatilInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String inId = CommonUtils.checkNull(request.getParamValue("inId"));// ruku单ID
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND IM.IN_ID = '" + inId + "' ");
            Map<String, Object> map = dao.queryInStockInfosList(sbString.toString()).get(0);

            if(!CommonUtils.isEmpty(CommonUtils.checkNull(map.get("ORDER_TYPE")))){
                int orderType = Integer.parseInt(CommonUtils.checkNull(map.get("ORDER_TYPE")));
                if (orderType1 == orderType) {
                    map.put("ORDER_TYPE", "紧急订单");
                } else if (orderType2 == orderType) {
                    map.put("ORDER_TYPE", "常规订单");
                }else if (orderType4 == orderType) {
                    map.put("ORDER_TYPE", "委托订单");
                }else if (orderType8 == orderType) {
                    map.put("ORDER_TYPE", "促销订单");
                } else if (orderType12 == orderType) {
                    map.put("ORDER_TYPE", "销售采购订单");
                } 
//                else if (orderType3 == orderType) {
//                    map.put("ORDER_TYPE", "计划订单");
//                }  else if (orderType5 == orderType) {
//                    map.put("ORDER_TYPE", "调拨");
//                } 
            }
            act.setOutData("map", map);
            act.setForword(PART_STOCK_VIEW);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "入库单查询查看或查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title : 入库单查询详情查询
     */
    public void partInStockDetailSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String inId = CommonUtils.checkNull(request.getParamValue("inId")); // 入库单ID

            StringBuffer sbString = new StringBuffer();
            if (null != inId && !"".equals(inId)) {
                sbString.append(" AND ID.IN_ID = '" + inId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryInStockDeatil(sbString.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件入库单查询信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-5-3
     * @Title :导出配件入库单查询信息
     */
    public void exportInStockExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String inCode = CommonUtils.checkNull(request
                    .getParamValue("inCode")); // 入库单号
            String orderType = CommonUtils.checkNull(request
                    .getParamValue("orderType")); // 订单类型
            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("dealerName")); // 采购单位
            String sellerName = CommonUtils.checkNull(request
                    .getParamValue("sellerName")); // 销售单位
            String orderCode = CommonUtils.checkNull(request
                    .getParamValue("orderCode")); // 采购单号
            String checkSDate = CommonUtils.checkNull(request
                    .getParamValue("checkSDate")); // 入库开始时间
            String checkEDate = CommonUtils.checkNull(request
                    .getParamValue("checkEDate")); // 入库截止时间

            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            String userRole = CommonUtils.checkNull(request.getParamValue("userRole")); // 用户角色
            StringBuffer sbString = new StringBuffer();
            if (null != inCode && !"".equals(inCode)) {
                sbString.append(" AND SALE_DATE like '%" + inCode + "%' ");
            }
            if (null != orderCode && !"".equals(orderCode)) {
                sbString.append(" AND IM.ORDER_CODE like '%" + orderCode + "%' ");
            }
            if (null != orderType && !"".equals(orderType)) {
                sbString.append(" AND IM.ORDER_TYPE = '" + orderType + "' ");
            }
            if (null != dealerName && !"".equals(dealerName)) {
                sbString.append(" AND IM.DEALER_NAME LIKE '%" + dealerName + "%' ");
            }
            if (null != sellerName && !"".equals(sellerName)) {
                sbString.append(" AND IM.SELLER_NAME LIKE '%" + sellerName + "%' ");
            }
            if (null != checkSDate && !"".equals(checkSDate)) {
                sbString.append(" AND TO_CHAR(IM.SALE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
            }
            if (null != checkEDate && !"".equals(checkEDate)) {
                sbString.append(" AND TO_CHAR(IM.SALE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
            }
            if (null != parentOrgId && !"".equals(parentOrgId)) {
                if (USER_ROLE_OEM.equals(userRole)) {
                    sbString.append(" AND IM.SELLER_ID = '" + parentOrgId + "' ");
                } else if (USER_ROLE_PDR.equals(userRole)) {
                    sbString.append(" AND " + parentOrgId + " IN (IM.SELLER_ID, IM.DEALER_ID) ");
                } else {
                    sbString.append(" AND IM.DEALER_ID = '" + parentOrgId + "' ");
                }
            }

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "入库单号";
            head[2] = "订货单号";
            head[3] = "订货单位";
            head[4] = "销售单位";
            head[5] = "制单人";
            head[6] = "入库日期";
            head[7] = "订单类型";
            head[8] = "备注";
            List<Map<String, Object>> list = dao.queryInStockInfosList(sbString.toString());
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("IN_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("ORDER_CODE"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[4] = CommonUtils
                                .checkNull(map.get("SELLER_NAME"));
                        detail[5] = CommonUtils
                                .checkNull(map.get("NAME"));
                        detail[6] = CommonUtils
                                .checkNull(map.get("SALE_DATE"));
                        if(!CommonUtils.isEmpty(CommonUtils.checkNull(map.get("ORDER_TYPE")))){
                            int orderTypeTmp = Integer.parseInt(CommonUtils.checkNull(map.get("ORDER_TYPE")));
                            if (orderType1 == orderTypeTmp) {
                                detail[7] = "紧急";
                            } else if (orderType2 == orderTypeTmp) {
                                detail[7] = "常规";
                            } else if (orderType3 == orderTypeTmp) {
                                detail[7] = "计划";
                            } else if (orderType4 == orderTypeTmp) {
                                detail[7] = "直发";
                            } else if (orderType5 == orderTypeTmp) {
                                detail[7] = "调拨";
                            }
                        }
                        detail[8] = CommonUtils.checkNull(map
                                .get("REMARK2"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件入库单信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件入库单查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
