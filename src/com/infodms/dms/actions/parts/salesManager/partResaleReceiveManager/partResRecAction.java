package com.infodms.dms.actions.parts.salesManager.partResaleReceiveManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.laborList.LaborListSummaryReportDao;
import com.infodms.dms.dao.parts.baseManager.partInnerOrgManager.partInnerOrgDao;
import com.infodms.dms.dao.parts.salesManager.partResaleReceiveManager.partResRecDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartRetailDtlPO;
import com.infodms.dms.po.TtPartRetailMainPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
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

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 配件零售/领用单业务处理
 * @Date: 2013-4-26
 * @remark
 */
@SuppressWarnings("unchecked")
public class partResRecAction extends BaseImport {
    public Logger logger = Logger.getLogger(partResRecAction.class);
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
    private static final int orderState6 = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_06;//已作废

    //配件零售/领用单URL
    private static final String PART_RESALE_RECCIVE_MAIN = "/jsp/parts/salesManager/partResaleReceive/partResaleReceiveMain.jsp";//配件零售/领用单首页
    private static final String PART_RESALE_MAIN = "/jsp/parts/salesManager/partResaleReceive/partResaleMain.jsp";//配件零售单首页
    private static final String PART_RECEIVE_MAIN = "/jsp/parts/salesManager/partResaleReceive/partReceiveMain.jsp";//配件领用单首页
    private static final String PART_RESALE_RECCIVE_VIEW = "/jsp/parts/salesManager/partResaleReceive/partResaleReceiveView.jsp";//配件零售/领用单查看页面
    private static final String PART_RESALE_RECCIVE_PRINT = "/jsp/parts/salesManager/partResaleReceive/partResRecPrtPg.jsp";//配件零售/领用单打印页面
    private static final String PART_RESALE_ADD = "/jsp/parts/salesManager/partResaleReceive/partResaleAdd.jsp";///配件零售单新增页面
    private static final String PART_RECEIVE_ADD = "/jsp/parts/salesManager/partResaleReceive/partReceiveAdd.jsp";//配件领用单新增页面
    private static final String PART_RESALE_MOD = "/jsp/parts/salesManager/partResaleReceive/partResaleModify.jsp";///配件零售单修改页面
    private static final String PART_RECEIVE_MOD = "/jsp/parts/salesManager/partResaleReceive/partReciveModify.jsp";//配件领用单修改页面
    private static final String INPUT_ERROR_URL = "/jsp/parts/salesManager/partResaleReceive/inputError.jsp";//数据导入出错页面
    private static final String IN_ORG_DIALOG = "/jsp/parts/salesManager/partResaleReceive/inOrgSelectPg.jsp";//内部单位选择页面

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-27
     * @Title : 跳转至配件零售/领用单页面
     */
    public void partResRecInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
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
            act.setForword(PART_RESALE_RECCIVE_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件零售/领用单页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-1
     * @Title : 跳转至配件零售单页面
     */
    public void partResInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
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
            act.setForword(PART_RESALE_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件零售单页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-1
     * @Title : 跳转至配件领用单页面
     */
    public void partRecInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
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
            act.setForword(PART_RECEIVE_MAIN);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件领用单页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-27
     * @Title : 查询配件零售/领用单信息
     */
    public void partResRecSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String changeCode = CommonUtils.checkNull(request.getParamValue("changeCode")); // 制单单号
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 类型
            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 出库ID
            String orderState = CommonUtils.checkNull(request.getParamValue("orderState")); // 制单状态
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

            Integer curPage = request.getParamValue("curPage") != null
                    ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSaleOrders(sbString.toString(), Constant.PAGE_SIZE,
                    curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件零售/领用单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-22
     * @Title : 跳转至配件零售/领用单查看页面
     */
    public void viewOrderDeatilInint() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
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

                if (orderType1 == odType) {
                    //零售修改页面
                    act.setForword(PART_RESALE_MOD);
                } else {
                    //领用修改页面
                    act.setForword(PART_RECEIVE_MOD);
                }
            } else {
                List<Map<String, Object>> list = dao.queryPartSaleOrderDeatilList(sbString.toString());
                Date date = new Date();
                DateFormat locDate = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                String dateStr = locDate.format(date);
                int totalCount = 0;
                Double totalAmount = 0D;
                if (null != list) {
                    for (int k = 0; k < list.size(); k++) {
                        totalCount += Double.parseDouble(list.get(k).get("QTY").toString());
                        totalAmount += Double.parseDouble(list.get(k).get("SALE_AMOUNT").toString().replace(",", ""));
                    }
                }

                act.setOutData("totalAmount", totalAmount);
                act.setOutData("totalCount", totalCount);
                act.setOutData("chineseAmount", laborListSummaryReportDao.numToChinese(totalAmount + ""));
                act.setOutData("pageType", pageType);
                act.setOutData("currDate", dateStr);
                act.setOutData("list", list);
                //打印页面
                act.setForword(PART_RESALE_RECCIVE_PRINT);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件零售/领用单查看页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-27
     * @Title : 跳转至配件零售/领用单新增领用页面
     */
    public void partOrderAddInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String addType = CommonUtils.checkNull(request.getParamValue("addType"));// 新增单类型
            String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String companyName = ""; //制单单位
            String selectedWhId = "";

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                companyName = dao.getMainCompanyName(parentOrgId);
                StringBuffer sb = new StringBuffer();
                sb.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
                sb.append(" AND TM.WH_NAME LIKE '%景德镇%' ");
                List<Map<String, Object>> whList = dao.getWareHouses(sb.toString());
                if (null != whList && whList.size() > 0) {
                    selectedWhId = whList.get(0).get("WH_ID").toString();
                }
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
                companyName = dao.getDealerName(parentOrgId);
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

            String marker = logonUser.getName();
            String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_14);//获取制单单号
            List<Map<String, String>> voList = null;

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("changeCode", changeCode);
            act.setOutData("actionURL", actionURL);
            act.setOutData("marker", marker);
            act.setOutData("companyName", companyName);
            act.setOutData("list", voList);
            act.setOutData("WHList", WHList);
            act.setOutData("selectedWhId", selectedWhId);
            act.setOutData("oemId", Constant.OEM_ACTIVITIES);
            if ("rec".equals(addType)) {
                act.setForword(PART_RECEIVE_ADD);
            } else if ("res".equals(addType)) {
                act.setForword(PART_RESALE_ADD);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件零售/领用单新增领用页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-27
     * @Title : 保存新增配件零售/领用单信息
     */
    public void saveOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String parentOrgId = CommonUtils.checkNull(req.getParamValue("parentOrgId")); //制单单位ID
            String parentOrgCode = CommonUtils.checkNull(req.getParamValue("parentOrgCode")); //制单单位编码
            String chgorgCname = CommonUtils.checkNull(req.getParamValue("chgorgCname")); //制单单位名称
            String changeCode = CommonUtils.checkNull(req.getParamValue("changeCode")); //制单单号
            //            String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_14);//获取制单单号
            String linkMan = CommonUtils.checkNull(req.getParamValue("linkMan")); //领用人
            String telPhone = CommonUtils.checkNull(req.getParamValue("telPhone")); //领用人电话
            String purpose = CommonUtils.checkNull(req.getParamValue("purpose")); //用途
            Long userId = logonUser.getUserId(); //制单人ID
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String whName = CommonUtils.checkNull(req.getParamValue("whName")); //仓库名
            String remark = CommonUtils.checkNull(req.getParamValue("remark")); //备注
            String cgType = CommonUtils.checkNull(req.getParamValue("orderType")); //类型
            String[] partIdArr = req.getParamValues("cb");
            int cgTypeInt = Integer.parseInt(cgType);

            String fixStr = "领用";
            if (Constant.PART_SALE_STOCK_REMOVAL_TYPE_01 == cgTypeInt) {
                fixStr = "零售";
            }

            Date date = new Date();
            TtPartRetailDtlPO insertRDPo = null;
            List<Map<String, Object>> partList = null;
            Long changeId = Long.parseLong(SequenceManager.getSequence(""));
            List<TtPartRetailDtlPO> dtlList = new ArrayList<TtPartRetailDtlPO>();
            if (null != partIdArr) {
                for (int i = 0; i < partIdArr.length; i++) {
                    insertRDPo = new TtPartRetailDtlPO();
                    String index = partIdArr[i];

                    String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + index)); //配件编码
                    String loc = CommonUtils.checkNull(request.getParamValue("locInfo_" + index)); // 货位信息
                    String[] locArr = loc.split(",");
                    String partId = locArr[0];
                    String locId = locArr[1]; // 货位id
                    String locCode = locArr[2]; // 货位编码
                    String locName = locArr[3]; // 货位名称
                    String batchNo = locArr[4]; // 货位编码
                    String returnQty = CommonUtils.checkNull(req.getParamValue("returnQty_" + index)); //零售/领用数量
                    
                    // 验证配件库存是否可用领用或零售
                    Map<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("orgId", parentOrgId);
                    paramMap.put("partId", partId);
                    paramMap.put("whId", whId);
                    paramMap.put("locId", locId);
                    paramMap.put("batchNo", batchNo);
                    partList = dao.getPartStockList(paramMap);
                    Long normalQty = Long.parseLong(partList.get(0).get("NORMAL_QTY").toString());//当前可用库存
                    if (Long.valueOf(returnQty) > normalQty) {
                        errorExist = "配件：" + partOldcode + " " + fixStr + "不能大于:" + normalQty + "!<br/>";
                        throw new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errorExist);
                    }

                    Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
                    String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + index));//件号
                    String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + index)); //配件名称
                    String unit = CommonUtils.checkNull(req.getParamValue("unit_" + index)); //单位
                    String salePrice = CommonUtils.checkNull(req.getParamValue("salePrice_" + index)).replace(",", ""); //零售/领用单价
                    double saleAmount = Arith.mul((Double.parseDouble(salePrice)), (Double.parseDouble(returnQty)));//零售/领用金额
                    String deRemark = CommonUtils.checkNull(req.getParamValue("remark_" + index)); //详细备注

                    insertRDPo.setDtlId(dtlId);
                    insertRDPo.setRetalId(changeId);
                    insertRDPo.setPartId(Long.parseLong(partId));
                    insertRDPo.setPartCode(partCode);
                    insertRDPo.setPartOldcode(partOldcode);
                    insertRDPo.setPartCname(partCname);
                    insertRDPo.setLineNo((long) i);
                    insertRDPo.setUnit(unit);
                    insertRDPo.setLocId(Long.parseLong(locId));
                    insertRDPo.setLocCode(locCode);
                    insertRDPo.setLocName(locName);
                    insertRDPo.setBatchNo(batchNo);
                    insertRDPo.setStockQty(normalQty);
                    insertRDPo.setSalePrice(Double.parseDouble(salePrice));
                    insertRDPo.setSaleAmount(saleAmount);
                    insertRDPo.setQty(Long.parseLong(returnQty));
                    insertRDPo.setOutQty(Long.parseLong("0"));
                    insertRDPo.setRemark(deRemark);
                    insertRDPo.setCreateBy(userId);
                    insertRDPo.setCreateDate(date);
                    dtlList.add(insertRDPo);
                }
            }

            // 主要信息
            TtPartRetailMainPO inserRMPo = new TtPartRetailMainPO();
            inserRMPo.setRetailId(changeId);
            inserRMPo.setRetailCode(changeCode);
            inserRMPo.setSorgId(Long.parseLong(parentOrgId));
            inserRMPo.setSorgCode(parentOrgCode);
            inserRMPo.setSorgCname(chgorgCname);
            inserRMPo.setWhId(Long.parseLong(whId));
            inserRMPo.setWhCname(whName);
            inserRMPo.setLinkman(linkMan);
            inserRMPo.setTel(telPhone);
            inserRMPo.setPurpose(purpose);
            inserRMPo.setRemark(remark);
            inserRMPo.setCreateBy(userId);
            inserRMPo.setCreateDate(date);
            inserRMPo.setChgType(Integer.parseInt(cgType));
            dao.insert(inserRMPo);

            // 插入明细记录
            dao.insert(dtlList);

            //资源锁定逻辑 start
            List<Object> ins = new LinkedList<Object>();
            ins.add(0, changeId);
            ins.add(1, Constant.PART_CODE_RELATION_14);
            ins.add(2, 1);// 1:占用 0：释放占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
            //end
            act.setOutData("success", "true");
            act.setOutData("orderCode", changeCode);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, errorExist);
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-7-11
     * @Title : 修改订单
     */
    public void updateOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        long changeId = 0;
        try {
            String parentOrgId = CommonUtils.checkNull(req.getParamValue("parentOrgId")); //制单单位ID
            changeId = Long.parseLong(CommonUtils.checkNull(req.getParamValue("changeId")));//获取制单ID
            String linkMan = CommonUtils.checkNull(req.getParamValue("linkMan")); //领用人
            String telPhone = CommonUtils.checkNull(req.getParamValue("telPhone")); //领用人电话
            String purpose = CommonUtils.checkNull(req.getParamValue("purpose")); //用途
            Long userId = logonUser.getUserId(); //制单人ID
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            String remark = CommonUtils.checkNull(req.getParamValue("remark")); //备注
            String cgType = CommonUtils.checkNull(req.getParamValue("orderType")); //类型
            String[] partIdArr = req.getParamValues("cb");
            int cgTypeInt = Integer.parseInt(cgType);

            String fixStr = "领用";
            if (Constant.PART_SALE_STOCK_REMOVAL_TYPE_01 == cgTypeInt) {
                fixStr = "零售";
            }

            // 释放资源
            List<Object> ins = new LinkedList<Object>();
            ins.add(0, changeId);
            ins.add(1, Constant.PART_CODE_RELATION_14);
            ins.add(2, 0);// 1:占用 0：释放占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
            
            Date date = new Date();
            TtPartRetailDtlPO insertRDPo = null;
            List<Map<String, Object>> partList = null;
            List<TtPartRetailDtlPO> dtlList = new ArrayList<TtPartRetailDtlPO>();
            if (null != partIdArr) {
                for (int i = 0; i < partIdArr.length; i++) {
                    insertRDPo = new TtPartRetailDtlPO();
                    String index = partIdArr[i];

                    String partOldcode = CommonUtils.checkNull(req.getParamValue("partOldcode_" + index)); //配件编码
                    String loc = CommonUtils.checkNull(request.getParamValue("locInfo_" + index)); // 货位信息
                    String[] locArr = loc.split(",");
                    String partId = locArr[0];
                    String locId = locArr[1]; // 货位id
                    String locCode = locArr[2]; // 货位编码
                    String locName = locArr[3]; // 货位名称
                    String batchNo = locArr[4]; // 货位编码
                    String returnQty = CommonUtils.checkNull(req.getParamValue("returnQty_" + index)); //零售/领用数量

                    // 验证配件库存是否可用领用或零售
                    Map<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("orgId", parentOrgId);
                    paramMap.put("partId", partId);
                    paramMap.put("whId", whId);
                    paramMap.put("locId", locId);
                    paramMap.put("batchNo", batchNo);
                    partList = dao.getPartStockList(paramMap);
                    Long normalQty = Long.parseLong(partList.get(0).get("NORMAL_QTY").toString());//当前可用库存
                    if (Long.valueOf(returnQty) > normalQty) {
                        errorExist = "配件：" + partOldcode + " " + fixStr + "不能大于:" + normalQty + "!<br/>";
                        throw new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errorExist);
                    }

                    Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
                    String partCode = CommonUtils.checkNull(req.getParamValue("partCode_" + index));//件号
                    String partCname = CommonUtils.checkNull(req.getParamValue("partCname_" + index)); //配件名称
                    String unit = CommonUtils.checkNull(req.getParamValue("unit_" + index)); //单位
                    String salePrice = CommonUtils.checkNull(req.getParamValue("salePrice_" + index)).replace(",", ""); //零售/领用单价
                    double saleAmount = Arith.mul((Double.parseDouble(salePrice)), (Double.parseDouble(returnQty)));//零售/领用金额
                    String deRemark = CommonUtils.checkNull(req.getParamValue("remark_" + index)); //详细备注

                    insertRDPo.setDtlId(dtlId);
                    insertRDPo.setRetalId(changeId);
                    insertRDPo.setPartId(Long.parseLong(partId));
                    insertRDPo.setPartCode(partCode);
                    insertRDPo.setPartOldcode(partOldcode);
                    insertRDPo.setPartCname(partCname);
                    insertRDPo.setLineNo((long) i);
                    insertRDPo.setUnit(unit);
                    insertRDPo.setLocId(Long.parseLong(locId));
                    insertRDPo.setLocCode(locCode);
                    insertRDPo.setLocName(locName);
                    insertRDPo.setBatchNo(batchNo);
                    insertRDPo.setStockQty(normalQty);
                    insertRDPo.setSalePrice(Double.parseDouble(salePrice));
                    insertRDPo.setSaleAmount(saleAmount);
                    insertRDPo.setQty(Long.parseLong(returnQty));
                    insertRDPo.setOutQty(Long.parseLong("0"));
                    insertRDPo.setRemark(deRemark);
                    insertRDPo.setCreateBy(userId);
                    insertRDPo.setCreateDate(date);
                    dtlList.add(insertRDPo);
                }
            }
            if ("".equals(errorExist)) {
                StringBuffer sbStr = new StringBuffer();
                if (null != whId && !"".equals(whId)) {
                    sbStr.append(" AND TM.WH_ID = '" + whId + "' ");
                }
                Map<String, Object> mapWH = dao.getWareHouses(sbStr.toString()).get(0);
                String whName = mapWH.get("WH_CNAME").toString();

                TtPartRetailMainPO selRMPo = new TtPartRetailMainPO();
                TtPartRetailMainPO updRMPo = new TtPartRetailMainPO();
                selRMPo.setRetailId(changeId);
                updRMPo.setWhId(Long.parseLong(whId));
                updRMPo.setWhCname(whName);
                updRMPo.setLinkman(linkMan);
                updRMPo.setTel(telPhone);
                updRMPo.setPurpose(purpose);
                updRMPo.setRemark(remark);
                updRMPo.setChgType(Integer.parseInt(cgType));
                updRMPo.setUpdateBy(userId);
                updRMPo.setUpdateDate(date);
                dao.update(selRMPo, updRMPo);

                //先清空 TtPartRetailDtlPO 特定单号数据
                TtPartRetailDtlPO delRDPo = new TtPartRetailDtlPO();
                delRDPo.setRetalId(changeId);
                dao.delete(delRDPo);
                //重新插入TtPartRetailDtlPO 特定单号数据
                dao.insert(dtlList);
                act.setOutData("success", "true");
            } else {
                act.setOutData("errorExist", errorExist);// 返回错误信息
            }
            
            // 重新锁定资源
            List<Object> ins2 = new LinkedList<Object>();
            ins2.add(0, changeId);
            ins2.add(1, Constant.PART_CODE_RELATION_14);
            ins2.add(2, 1);// 1:占用 0：释放占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
            
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "修改失败！");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-23
     * @Title : 显示仓库配件库存信息
     */
    public void showPartStockBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        //        RequestWrapper req = act.getRequest();
        StringBuffer sbStr = new StringBuffer();
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构ID
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));// 仓库ID
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));// 件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));// 配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称

            sbStr.append(" AND TD.ORG_ID = '" + parentOrgId + "' ");
            if (null != whId && !"".equals(whId)) {
                sbStr.append(" AND TD.WH_ID = '" + whId + "' ");
            }
            if (null != partCode && !"".equals(partCode)) {
                sbStr.append(" AND upper(TD.PART_CODE) LIKE upper('%" + partCode + "%') ");
            }
            if (null != partOldcode && !"".equals(partOldcode)) {
                sbStr.append(" AND upper(TD.PART_OLDCODE) LIKE upper('%" + partOldcode + "%') ");
            }
            if (null != partCname && !"".equals(partCname)) {
                sbStr.append(" AND TD.PART_CNAME LIKE '%" + partCname + "%' ");
            }
            Integer curPage = request.getParamValue("curPage") != null
                    ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.showPartStockBase(sbStr.toString(), Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "初始化失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-5-2
     * @Title : 提交配件零售/领用单信息
     */
    public void commitOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            Long userId = logonUser.getUserId(); //制单人ID
            String retailId = CommonUtils.checkNull(req.getParamValue("retailId")); //制单ID

            String str = " AND TM.RETAIL_ID = '" + retailId + "' ";
            List<Map<String, Object>> rtlMainList = dao.queryAllPartSaleOrders(str);
            if (null != rtlMainList && rtlMainList.size() == 1) {
                int orderState = Integer.parseInt(rtlMainList.get(0).get("STATE").toString()); //订单状态
                String orderCode = rtlMainList.get(0).get("RETAIL_CODE").toString(); //订单单号

                if (orderState1 == orderState || orderState5 == orderState) {
                    TtPartRetailMainPO selectRMPo = new TtPartRetailMainPO();
                    TtPartRetailMainPO updateRMPo = new TtPartRetailMainPO();

                    selectRMPo.setRetailId(Long.parseLong(retailId));
                    int chgType = Integer.parseInt(rtlMainList.get(0).get("CHG_TYPE").toString()); //订单类型
                    // 零售类型直接通过
                    if (chgType == orderType1) {
                        updateRMPo.setState(orderState4);
                    } else {
                        updateRMPo.setState(orderState2);
                    }
                    updateRMPo.setUpdateBy(userId);
                    updateRMPo.setUpdateDate(new Date());

                    dao.update(selectRMPo, updateRMPo);
                } else if (orderState2 == orderState) {
                    errorExist = "单号【" + orderCode + "】已提交,不能重复提交!";
                } else if (orderState3 == orderState) {
                    errorExist = "单号【" + orderCode + "】已完成,不能再提交!";
                } else if (orderState4 == orderState) {
                    errorExist = "单号【" + orderCode + "】已通过,不能再提交!";
                } else if (orderState6 == orderState) {
                    errorExist = "单号【" + orderCode + "】已作废,不能再提交!";
                } else {
                    errorExist = "提交失败,请联系管理员!";
                }
            } else {
                errorExist = "提交失败,请联系管理员!";
            }

            if ("".equals(errorExist)) {
                act.setOutData("success", "true");
            } else {
                act.setOutData("errorExist", errorExist);// 返回错误信息
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, errorExist);
            }

            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "提交配件零售/领用单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-9-26
     * @Title : 作废订单
     */

    public void disableOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            Long userId = logonUser.getUserId(); //制单人ID
            Date date = new Date();
            String retailId = CommonUtils.checkNull(req.getParamValue("retailId")); //制单ID
            int ordState = Constant.PART_RESALE_RECEIVE_ORDER_TYPE_06;//已作废

            //删除时释放锁定资源
            List<Object> ins = new LinkedList<Object>();
            ins.add(0, retailId);
            ins.add(1, Constant.PART_CODE_RELATION_14);
            ins.add(2, 0);// 1:占用 0：释放占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

            TtPartRetailMainPO selPo = new TtPartRetailMainPO();
            TtPartRetailMainPO updPo = new TtPartRetailMainPO();

            selPo.setRetailId(Long.parseLong(retailId));
            updPo.setState(ordState);
            updPo.setUpdateBy(userId);
            updPo.setUpdateDate(date);
            updPo.setDisableBy(userId);
            updPo.setDisableDate(new Date());

            dao.update(selPo, updPo);

            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "删除配件零售/领用单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-12-18
     * @Title : 撤回提交订单
     */
    public void rebackOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            Long userId = logonUser.getUserId(); //制单人ID
            String configId = Constant.PART_CODE_RELATION_14 + "";//配置ID
            String retailId = CommonUtils.checkNull(req.getParamValue("retailId")); //制单ID

            String str = " AND TM.RETAIL_ID = '" + retailId + "' ";
            List<Map<String, Object>> rtlMainList = dao.queryAllPartSaleOrders(str);

            if (null != rtlMainList && rtlMainList.size() == 1) {
                int orderState = Integer.parseInt(rtlMainList.get(0).get("STATE").toString()); //订单状态
                String orderCode = rtlMainList.get(0).get("RETAIL_CODE").toString(); //订单单号
                long outQtys = Long.parseLong(rtlMainList.get(0).get("OUT_QTYS").toString());//已出库总数量

                if (orderState1 == orderState || orderState5 == orderState) {
                    errorExist = "单号【" + orderCode + "】未提交或已驳回,不能再撤回!";
                } else if (orderState2 == orderState || orderState4 == orderState) {

                    if (outQtys <= 0) {
                        //调用库存释放逻辑
                        List<Object> ins = new LinkedList<Object>();
                        ins.add(0, retailId);
                        ins.add(1, configId);
                        ins.add(2, 0);// 1:占用  0：释放占用
                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

                        //订单撤回 到已保存状态
                        TtPartRetailMainPO selectRMPo = new TtPartRetailMainPO();
                        TtPartRetailMainPO updateRMPo = new TtPartRetailMainPO();

                        selectRMPo.setRetailId(Long.parseLong(retailId));

                        updateRMPo.setState(orderState1);
                        updateRMPo.setUpdateBy(userId);
                        updateRMPo.setUpdateDate(new Date());

                        dao.update(selectRMPo, updateRMPo);
                    } else {
                        errorExist = "单号【" + orderCode + "】已部分出库,不能再撤回!";
                    }
                } else if (orderState3 == orderState) {
                    errorExist = "单号【" + orderCode + "】已完成,不能再撤回!";
                } else if (orderState6 == orderState) {
                    errorExist = "单号【" + orderCode + "】已作废,不能再撤回!";
                } else {
                    errorExist = "撤回失败,请联系管理员!";
                }
            } else {
                errorExist = "撤回失败,请联系管理员!";
            }

            act.setOutData("errorExist", errorExist);// 返回错误信息
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "撤回配件零售/领用单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-23
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
            listHead.add("货位");
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
     * @throws : LastDate : 2013-4-25
     * @Title : 配件零售/领用单-> 导入文件
     */
    public void partResRecUpload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            String whId = CommonUtils.checkNull(req.getParamValue("whId")); //仓库ID
            int orderType = Integer.parseInt(CommonUtils.checkNull(req.getParamValue("orderType"))); //类型
            String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
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
            List<Map<String, String>> errorInfo = new ArrayList<Map<String, String>>();
            List<Map<String, String>> maxLineErro = new ArrayList<Map<String, String>>();
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
                act.setOutData("actionURL", actionURL);
                act.setForword(INPUT_ERROR_URL);
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                loadVoList(voList, list, errorInfo, maxLineErro, parentOrgId, whId, orderType);
                if (maxLineErro.size() > 0) {
                    err = maxLineErro.get(0).get("1").toString();
                    act.setOutData("error", err);
                    act.setOutData("actionURL", actionURL);
                    act.setForword(INPUT_ERROR_URL);
                } else if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setOutData("actionURL", actionURL);
                    act.setForword(INPUT_ERROR_URL);
                } else {
                    StringBuffer sbString = new StringBuffer();
                    sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
                    List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());

                    String marker = logonUser.getName();
                    String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_14);//获取制单单号

                    act.setOutData("selectedWhId", whId);
                    act.setOutData("parentOrgId", parentOrgId);
                    act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
                    act.setOutData("parentOrgCode", parentOrgCode);
                    act.setOutData("changeCode", changeCode);
                    act.setOutData("marker", marker);
                    act.setOutData("companyName", companyName);
                    act.setOutData("WHList", WHList);
                    act.setOutData("list", voList);
                    act.setOutData("actionURL", actionURL);
                    if (orderType1 == orderType) {
                        act.setForword(PART_RESALE_ADD);//新增零售
                    } else if (orderType2 == orderType) {
                        act.setForword(PART_RECEIVE_ADD);//新增领用
                    }
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
     * @return : LastDate : 2013-4-12
     * @Title : 读取CELL
     */
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo,
            List<Map<String, String>> maxLineErro, String parentOrgId, String whId, int orderType) {
        if (null == list) {
            list = new ArrayList<Map>();
        }
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            if (null == map) {
                //                map = new HashMap<String, Cell[]>();
                map = new HashMap<String, Object>();
            }
            Set<String> keys = map.keySet();
            Iterator<String> it = keys.iterator();
            double salePrice = 0.00;
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                Map<String, String> tempmap = new HashMap<String, String>();
                int maxLineNum = Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM;
                if (Integer.parseInt(key) > maxLineNum) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "导入数据行数不能超过 " + maxLineNum + "行!");
                    maxLineErro.add(errormap);
                    return;
                }
                if (cells.length < 1 || "".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "配件编码");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                    return;
                }
                if (cells.length < 2 || "".equals(cells[1].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "货位");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                    return;
                }

                else {
                    List<Map<String, Object>> partCheck = dao.checkOldCode(cells[0].getContents().trim());
                    if (partCheck.size() == 1) {
                        String locCode = CommonUtils.checkNull(cells[1].getContents().trim());
                        List<Map<String, Object>> partList = dao.getPartStockInfos(cells[0].getContents().trim(),
                                parentOrgId, whId, locCode, "");
                        if (partList.size() == 1) {
                            tempmap.put("partOldcode", cells[0].getContents().trim());
                            if (null != partList.get(0).get("PART_CODE")
                                    && !"".equals(partList.get(0).get("PART_CODE"))) {
                                tempmap.put("partCode", partList.get(0).get("PART_CODE").toString());
                            } else {
                                tempmap.put("partCode", "");
                            }

                            if (null != partList.get(0).get("PART_CNAME")
                                    && !"".equals(partList.get(0).get("PART_CNAME"))) {
                                tempmap.put("partCname", partList.get(0).get("PART_CNAME").toString());
                            } else {
                                tempmap.put("partCname", "");
                            }

                            if (null != partList.get(0).get("UNIT") && !"".equals(partList.get(0).get("UNIT"))) {
                                tempmap.put("unit", partList.get(0).get("UNIT").toString());
                            } else {
                                tempmap.put("unit", "");
                            }
                            tempmap.put("partId", partList.get(0).get("PART_ID").toString());
                            tempmap.put("normalQty", partList.get(0).get("NORMAL_QTY").toString());
                            if (orderType1 == orderType) {
                                tempmap.put("salePrice", partList.get(0).get("RES_PRICE").toString());
                                salePrice = Double.parseDouble(partList.get(0).get("RES_PRICE").toString());
                            } else {
                                tempmap.put("salePrice", partList.get(0).get("REC_PRICE").toString());
                                salePrice = Double.parseDouble(partList.get(0).get("REC_PRICE").toString());
                            }

                        } else {
                            Map<String, String> errormap = new HashMap<String, String>();
                            errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                            errormap.put("2", "配件编码");
                            errormap.put("3", "不在当前登录用户权限范围!");
                            errorInfo.add(errormap);
                        }

                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "配件编码");
                        errormap.put("3", "不存在");
                        errorInfo.add(errormap);
                    }
                }

                if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "零售/领用数量");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
                    String accTemp = cells[2].getContents().trim();
                    if (null == accTemp || "".equals(accTemp)) {
                        accTemp = "0";
                    } else {
                        accTemp = accTemp.replace(",", "");
                    }

                    String regex = "(^[1-9]+\\d*$)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(accTemp);

                    if (matcher.find()) {
                        NumberFormat numberFormat = NumberFormat.getNumberInstance();

                        numberFormat.setMinimumFractionDigits(2);
                        numberFormat.setMaximumFractionDigits(2);
                        numberFormat.setMaximumIntegerDigits(10);

                        tempmap.put("returnQty", accTemp);
                        tempmap.put("saleAmount",
                                numberFormat.format(Arith.mul(salePrice, Double.parseDouble(accTemp))));
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "零售/领用数量");
                        errormap.put("3", "非法数值!");
                        errorInfo.add(errormap);
                    }
                }
                tempmap.put("remark",
                        cells.length < 4 || null == cells[3].getContents() ? "" : cells[3].getContents().trim());
                voList.add(tempmap);
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-22
     * @Title : 配件零售/领用单详情查询
     */
    public void partOrderDetailSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String changeId = CommonUtils.checkNull(request.getParamValue("changeId")); // 订单ID

            StringBuffer sbString = new StringBuffer();
            if (null != changeId && !"".equals(changeId)) {
                sbString.append(" AND TD.RETAL_ID = '" + changeId + "' ");
            }

            Integer curPage = request.getParamValue("curPage") != null
                    ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartSaleOrderDeatil(sbString.toString(), Constant.PAGE_SIZE,
                    curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件零售/领用单详情信息");
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
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String changeCode = CommonUtils.checkNull(request.getParamValue("changeCode")); // 制单单号
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 类型
            String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 开始时间
            String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 截止时间
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 截止时间
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
                        detail[1] = CommonUtils.checkNull(map.get("RETAIL_CODE"));
                        Integer oderType = Integer.parseInt(CommonUtils.checkNull(map.get("CHG_TYPE")));
                        if (orderType1 == oderType) {
                            detail[2] = "零售";
                        } else if (orderType2 == oderType) {
                            detail[2] = "领用";
                        }
                        detail[3] = CommonUtils.checkNull(map.get("SORG_CNAME"));
                        detail[4] = CommonUtils.checkNull(map.get("WH_CNAME"));
                        detail[5] = CommonUtils.checkNull(map.get("NAME"));
                        detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[7] = CommonUtils.checkNull(map.get("AMOUNTS"));
                        int orderState = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")));
                        if (orderState1 == orderState) {
                            detail[8] = "已保存";
                        } else if (orderState2 == orderState) {
                            detail[8] = "已提交";
                        } else if (orderState3 == orderState) {
                            detail[8] = "已完成";
                        } else if (orderState4 == orderState) {
                            detail[8] = "已通过";
                        } else if (orderState5 == orderState) {
                            detail[8] = "已驳回";
                        } else if (orderState6 == orderState) {
                            detail[8] = "已作废";
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

            this.exportEx(fileName, ActionContext.getContext().getResponse(), request, head, list1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出配件零售/领用单");
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
     * @throws : LastDate : 2013-4-22
     * @Title : 文件导出为xls文件
     */
    public static Object exportEx(String fileName, ResponseWrapper response, RequestWrapper request, String[] head,
            List<String[]> list) throws Exception {

        String name = fileName + ".xls";
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
                    /* ws.addCell(new Label(i, z, str[i])); */ //modify by yuan
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

    //内部单位查询
    public void partInnerOrgSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String query = CommonUtils.checkNull(request.getParamValue("query"));
            if ("1".equals(query)) {
                String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 单位编码
                String orgName = CommonUtils.checkNull(request.getParamValue("orgName")); // 单位名称

                String parentOrgId = "";//父机构ID
                //判断主机厂与服务商
                String comp = logonUser.getOemCompanyId();
                if (null == comp) {

                    parentOrgId = Constant.OEM_ACTIVITIES;
                } else {
                    parentOrgId = logonUser.getDealerId();
                }

                StringBuffer sql = new StringBuffer();

                if (null != orgCode && !"".equals(orgCode)) {
                    sql.append(" AND UPPER(IO.IN_ORG_CODE) LIKE '%" + orgCode.trim().toUpperCase() + "%' ");
                }

                if (null != orgName && !"".equals(orgName)) {
                    sql.append(" AND IO.IN_ORG_NAME LIKE '%" + orgName.trim() + "%' ");
                }

                sql.append(" AND IO.PRT_ORG_ID = '" + parentOrgId + "' ");

                Integer curPage = request.getParamValue("curPage") != null
                        ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

                PageResult<Map<String, Object>> ps = partInnerOrgDao.getInstance().queryPartInnerOrg(sql.toString(),
                        parentOrgId, Constant.PAGE_SIZE, curPage);

                act.setOutData("ps", ps);
            } else {
                act.setForword(IN_ORG_DIALOG);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询内部单位信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 驳回
     */
    public void rejectOrderInfos() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            String retailId = CommonUtils.checkNull(req.getParamValue("retailId")); //制单ID

            TtPartRetailMainPO selPo = new TtPartRetailMainPO();
            TtPartRetailMainPO updPo = new TtPartRetailMainPO();

            selPo.setRetailId(Long.parseLong(retailId));

            updPo.setState(Constant.PART_RESALE_RECEIVE_ORDER_TYPE_01);
            updPo.setUpdateBy(logonUser.getUserId());
            updPo.setUpdateDate(new Date());

            dao.update(selPo, updPo);

            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "提交配件零售/领用单信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
