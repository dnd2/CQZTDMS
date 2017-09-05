package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.PartBaseQueryDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartBoDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartSoManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
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
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 配件销售单
 * @author 
 * @version 
 * @see 
 * @since 
 * @deprecated
 */
public class PartSoManage extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(PartSoManage.class);
    String directPartOrderPrintUrl = "/jsp/parts/salesManager/carFactorySalesManager/directPartTransPrint.jsp";
    private PartBoDao partBoDao = PartBoDao.getInstance();

   /**
    * 销售单-初始化查询
    */
    public void partSoManageInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dealerId = "";
            String userType = null;
            boolean flag = false;
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();

            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
            userposeDefinePO.setUserId(loginUser.getUserId());
            if (dao.select(userposeDefinePO).size() > 0) {
                userposeDefinePO = (TtPartUserposeDefinePO) dao.select(userposeDefinePO).get(0);
                userType = userposeDefinePO.getUserType() + "";
            }
            ;
            List<Map<String, Object>> salerList = dao.getSaler(userType);
            //添加汇总~ 
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "", "财务未审核");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "", "财务审核通过");
            //stateMap.put(Constant.CAR_FACTORY_PKG_STATE_01+"","装箱中");
            //stateMap.put(Constant.CAR_FACTORY_PKG_STATE_02+"","已装箱");
            //stateMap.put(Constant.CAR_FACTORY_TRANS_STATE_01+"","已发运");
            //stateMap.put(Constant.CAR_FACTORY_OUTSTOCK_STATE_05+"","已入库");
            request.setAttribute("stateMap", stateMap);

            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
                flag = true;
            }
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(dealerId);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            String old = sdf.format(new Date(c.getTimeInMillis()));  //这个星期的第1天

            Map<String, Object> map = new HashMap<String, Object>();
            if (null != act.getSession().get("condition")) {
                if (null != request.getParamValue("flag")) {
                    map = (Map) act.getSession().get("condition");
                    act.getSession().remove("condition");
                } else {
                    act.getSession().remove("condition");
                }
            }

            act.setOutData("condition", map);
            act.setOutData("curUserId", loginUser.getUserId());
            request.setAttribute("wareHouseList", wareHouseList);
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("salerFlag", flag);
            act.setOutData("salerList", salerList);
            act.setForword(PART_DLR_ORDER_SO_MAIN);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-9
     * @Title : 直发订单打印验证
     */
    public void checkDirOrderPrint() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId")); // 销售单ID
            String success = "确认打印直发订单?";
            StringBuffer sbString = new StringBuffer();
            if (null != soId && !"".equals(soId)) {
                sbString.append(" AND SM.SO_ID = '" + soId + "' ");
                sbString.append(" AND SM.TRANS_PRINT_NUM > '0' ");

                List<Map<String, Object>> list = PartSoManageDao.getInstance().checkDirOrderPrint(sbString.toString());

                if (null != list && list.size() > 0) {
                    success = "该直发订单已于 " + list.get(0).get("DPT_DATE") + " 打印, 是否再次打印?";
                }
                act.setOutData("soId", soId);
                act.setOutData("success", success);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "验证直发订单是否有打印失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    public void cancelSub() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        PartSoManageDao dao = PartSoManageDao.getInstance();
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId")); // 销售单ID
            List<Map<String, Object>> list = dao.getStateOrder(soId, Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "");
            if (null == list || list.size() <= 0) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "撤消失败!");
                throw e1;
            }
            TtPartSoMainPO po = new TtPartSoMainPO();
            po.setSoId(Long.valueOf(soId));
            po.setState(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01);
            TtPartSoMainPO oldPo = new TtPartSoMainPO();
            oldPo.setSoId(Long.valueOf(soId));
            dao.update(oldPo, po);
            act.setOutData("success", "撤回成功!");
        } catch (Exception e) {// 异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "撤消失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-9
     * @Title : 直发订单打印更新
     */
    public void updateDirOrderPrint() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        try {
            String soId = request.getParamValue("soId"); // 销售单ID
            if (null != soId) {
                String sqlStr = " AND SO_ID = '" + soId + "'";
                PartSoManageDao.getInstance().updateTranInfo(sqlStr);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "直发订单打印更新失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 销售单查询
     */
    public void partSoQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartSoManageDao dao = PartSoManageDao.getInstance();

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = null;
            saveQueryCondition();
            Map<String, Object> getSumResult = dao.getSumData(request);
            ps = dao.queryPartSoOrder(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("accountSum", getSumResult.get("AMOUNT"));
            act.setOutData("xs", getSumResult.get("XS"));
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 跳转到增加页面
     */
    public void addOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String dealerId = "";
            String dealerCode = "";
            String dealerName = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
                dealerCode = beanList.get(0).getOrgCode();
                dealerName = beanList.get(0).getOrgName();
            }
            if ("".equals(dealerId)) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, " </br>此工号没有操作权限,请联系管理员!");
                throw e1;
            }
            boolean isBatchSoFlag = false;
            if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                isBatchSoFlag = true;
            }
            request.setAttribute("isBatchSoFlag", isBatchSoFlag);
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            dataMap.put("name", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = dao.getAccount(dealerId, dealerId, "");
            request.setAttribute("accountMap", accountMap);
            //折扣率         
            String discount = dao.getDiscount(dealerId);
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            request.setAttribute("transList", list);
            dataMap.put("now", now);
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerCode", dealerCode);
            dataMap.put("discount", discount);
            dataMap.put("dealerName", dealerName);
            request.setAttribute("dataMap", dataMap);

            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(dealerId);
            request.setAttribute("wareHouseList", wareHouseList);
            act.setForword(PART_DLR_ORDER_SO_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER_SO_MAIN);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER_SO_MAIN);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 选择地址弹出页面
     */
    public void getPartItemStock() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            if ("".equals(partId)) {
                return;
            }
            partId = partId.replaceFirst(",", "");
            String partArr[] = partId.split(",");
            String dealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            request.setAttribute("orgId", dealerId);
            List<Map<String, Object>> list = new ArrayList();
            for (int i = 0; i < partArr.length; i++) {
                Map<String, Object> ps = dao.getPartItemStock(whId, partArr[i]);
                if (null == ps) {
                    ps = new HashMap<String, Object>();
                    ps.put("PART_ID", partArr[i]);
                    ps.put("NORMAL_QTY", "0");
                }
                list.add(ps);
            }
            act.setOutData("list", list);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取数据错误!,请联系管理员");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存订单
     */
    public void saveOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartSoManageDao dao = PartSoManageDao.getInstance();
        PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
        String soCode = "";
        try {
            //如果是铺货   需要生成多个
            String isBatchSo = CommonUtils.checkNull(request.getParamValue("isBatchSo"));
            if (isBatchSo.equals(Constant.PART_BASE_FLAG_YES + "")) {
                String dealerIds = request.getParamValue("dealerId1");
                String dealerCodes = request.getParamValue("dealerCode1");
                String dealerNames = request.getParamValue("dealerName1");
                dealerIds = dealerIds.replaceFirst(",", "");
                dealerCodes = dealerCodes.replaceFirst(",", "");
                dealerNames = dealerNames.replaceFirst(",", "");
                String dealerIdArr[] = dealerIds.split(",");
                String dealerCodeArr[] = dealerCodes.split(",");
                String dealerNameArr[] = dealerNames.split(",");
                for (int i = 0; i < dealerIdArr.length; i++) {
                    request.setAttribute("dealerId", dealerIdArr[i]);
                    request.setAttribute("dealerCode", dealerCodeArr[i]);
                    request.setAttribute("dealerName", dealerNameArr[i]);
                    //插入详细
                    insertDetail();
                    //插入主订单
                    insertMain();
                    //插入日志
                    insertHistory(request, act, Integer.valueOf(CommonUtils.checkNull(request.getParamValue("state"))));
                    dao = PartSoManageDao.getInstance();
                    soCode += "," + CommonUtils.checkNull(request.getAttribute("soCode"));
                }
            } else {
                //插入详细
                insertDetail();
                //插入主订单
                insertMain();

                ArrayList ins1 = new ArrayList();
                ins1.add(0, Long.valueOf(request.getAttribute("soId").toString()));
                //   dao.callProcedure("PKG_PART.P_CREATEDLRORDER",ins1,null);
                //插入日志
                insertHistory(request, act, Integer.valueOf(CommonUtils.checkNull(request.getParamValue("state"))));
                //占用金额

                String dealerId = "";
                //判断是否为车厂  PartWareHouseDao
                PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
                List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
                if (null != beanList || beanList.size() >= 0) {
                    dealerId = beanList.get(0).getOrgId() + "";
                }
                String sellerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
                //如果有账户
                Map<String, Object> acountMap = partDlrOrderDao.getAccount(sellerId, dealerId, "");
                if (null != acountMap) {
                    insertAccount();
                }
                dao = PartSoManageDao.getInstance();
                ArrayList ins = new ArrayList();
                ins.add(0, Long.valueOf(request.getAttribute("soId").toString()));
                ins.add(1, Constant.PART_CODE_RELATION_07);
                ins.add(2, 1);//1占用，0释放
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                soCode = CommonUtils.checkNull(request.getAttribute("soCode"));
            }
            if (soCode.indexOf(",") > -1) {
                soCode = soCode.replaceFirst(",", "");
            }
            //POContext.endTxn(true);
            act.setOutData("success", "销售单：" + soCode + ",保存成功!");
        } catch (Exception e) {//异常方法
            act.setOutData("error", "保存失败!");
            if (e instanceof BizException) {
                //POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            //POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存订单出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        } finally {
            try {
                //POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "保存订单出错,请联系管理员!");
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存主订单
     */
    private void insertMain() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartSoManageDao dao = PartSoManageDao.getInstance();
            TtPartSoMainPO po = new TtPartSoMainPO();
            String dealer = "";
            String soCode = "";
            Long soId = Long.valueOf(CommonUtils.checkNull(request.getAttribute("soId")));
            if (loginUser.getDealerId() == null) {
                dealer = request.getParamValue("isBatchSo").toString().equals(Constant.PART_BASE_FLAG_YES.toString()) ? request.getAttribute("dealerId").toString() : request.getParamValue("dealerId").toString();
                soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_07, dealer);
            } else {
                dealer = request.getParamValue("dealerId").toString();
                soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_11, dealer);
            }
            request.setAttribute("soCode", soCode);
            PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
            String sellerId = "";//销售单位ID
            String sellerCode = "";//销售单位CODE
            String sellerName = "";//销售单位NAME
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                sellerId = beanList.get(0).getOrgId() + "";
                sellerCode = beanList.get(0).getOrgCode();
                sellerName = beanList.get(0).getOrgName();
            }
            request.setAttribute("sellerId", sellerId);
            String payType = CommonUtils.checkNull(request.getParamValue("payType")); //付费方式
            String buyerId = CommonUtils.checkNull(loginUser.getUserId()); //订货人ID
            String buyerName = CommonUtils.checkNull(loginUser.getName()); //订货人
            String consignees = CommonUtils.checkNull(request.getParamValue("RCV_ORG")); //接收单位
            String consigneesId = CommonUtils.checkNull(request.getParamValue("RCV_ORGID")); //接收单位ID
            String addr = CommonUtils.checkNull(request.getParamValue("ADDR")); //接收地址ID
            String addrId = CommonUtils.checkNull(request.getParamValue("ADDR_ID")); //接收地址ID
            String receiver = CommonUtils.checkNull(request.getParamValue("RECEIVER")); //接收人
            String tel = CommonUtils.checkNull(request.getParamValue("TEL")); //接收人电话
            String postCode = CommonUtils.checkNull(request.getParamValue("POST_CODE")); //接收邮编
            String station = CommonUtils.checkNull(request.getParamValue("STATION")); //接收站
            String transType = CommonUtils.checkNull(request.getParamValue("transType")); //发运方式
            String transpayType = CommonUtils.checkNull(request.getParamValue("transpayType")); //发运付费方式
            Double amount = Double.valueOf(request.getAttribute("reCountMoney").toString()); //获取JAVA重新计算的金额
            String discount = CommonUtils.checkNull(request.getParamValue("discount")); //折扣
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); //订单备注
            String remark2 = CommonUtils.checkNull(request.getParamValue("remark2")); //备注
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id")); //备注
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); //订单类型
            String state = CommonUtils.checkNull(request.getParamValue("state")); //订单状态
            String isBatchSo = CommonUtils.checkNull(request.getParamValue("isBatchSo"));
            String dealerIds = SequenceManager.getSequence("");
            String dealerId = "";
            String dealerCode = "";
            String dealerName = "";
            po.setSoId(soId);
            po.setSoCode(soCode);
            if (isBatchSo.equals(Constant.PART_BASE_FLAG_YES + "")) {
                //是铺货从ATTRIBUTE中取
                dealerId = CommonUtils.checkNull(request.getAttribute("dealerId"));
                dealerCode = CommonUtils.checkNull(request.getAttribute("dealerCode"));
                dealerName = CommonUtils.checkNull(request.getAttribute("dealerName"));
                po.setDealerIds(dealerIds);
                po.setSoFrom(Constant.CAR_FACTORY_SO_FORM_03);
                po.setDealerId(Long.valueOf(dealerId));
                po.setDealerCode(dealerCode);
                po.setDealerName(dealerName);
                po.setSellerId(Long.valueOf(sellerId));
                po.setSellerCode(sellerCode);
                po.setSellerName(sellerName);
                po.setIsBatchso(Integer.valueOf(isBatchSo));
                po.setVer(1);
                po.setRemark(remark);
                po.setRemark2(remark2);
                po.setVer(1);
                //查询是否有账户   如果有账户就不需要财务审核  直接装箱中，不能通过这个做判断 modify by yuan
                /*Map<String,Object> accountMap = dao.getAccount(dealerId);
                if(null==accountMap||accountMap.isEmpty()){
					state = Constant.CAR_FACTORY_PKG_STATE_01+"";
				}*/
                po.setState(Integer.valueOf(state));
                if (state.equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02.toString())) {
                    po.setSubmitDate(new Date());
                    po.setSubmitBy(loginUser.getUserId());
                }
                po.setCreateBy(loginUser.getUserId());
                po.setCreateDate(new Date());
                po.setSaleDate(new Date());
                po.setAmount(amount);
                dao.insert(po);
                return;
            } else {
                isBatchSo = Constant.PART_BASE_FLAG_NO + "";
                dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
                dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
                dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
                po.setSoFrom(Constant.CAR_FACTORY_SO_FORM_01);
                Double freight = Double.valueOf(partDlrOrderDao.getFreight(dealerId, orderType, amount + "").replaceAll(",", "")); //运费
                po.setFreight(freight);
                amount = Arith.add(freight, amount);
                request.setAttribute("reCountMoney", amount);
                if (freight > 0) {
                    po.setIsTransfree(Constant.PART_BASE_FLAG_NO);
                } else {
                    po.setIsTransfree(Constant.PART_BASE_FLAG_YES);
                }
            }
            if (!"".equals(payType)) {
                po.setPayType(Integer.valueOf(payType));
            }

            po.setDealerId(Long.valueOf(dealerId));
            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setSellerId(Long.valueOf(sellerId));
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            po.setSaleDate(new Date());
            po.setBuyerId(Long.valueOf(buyerId));
            po.setBuyerName(buyerName);
            if (!"".equals(consigneesId)) {
                po.setConsigneesId(Long.valueOf(consigneesId));
            }
            po.setConsignees(consignees);
            po.setAddr(addr);
            if (!"".equals(addrId)) {
                po.setAddrId(Long.valueOf(addrId));
            }
            po.setReceiver(receiver);
            po.setTel(tel);
            po.setPostCode(postCode);
            po.setStation(station);
            po.setTransType(transType);
            if (!"".equals(transpayType)) {
                po.setTranspayType(Integer.valueOf(transpayType));
            }
            po.setAmount(amount);
            po.setDiscount(Double.valueOf(discount));
            po.setIsBatchso(Integer.valueOf(isBatchSo));
            po.setRemark(remark);
            po.setRemark2(remark2);
            if (!"".equals(whId)) {
                po.setWhId(Long.valueOf(whId));
            }

            po.setVer(1);
            //校验帐户
            Map<String, Object> acountMap = partDlrOrderDao.getAccount(po.getDealerId() + "", po.getSellerId() + "", "");
            if (null != acountMap && !acountMap.isEmpty()) {
                String sumNow = CommonUtils.checkNull(acountMap.get("ACCOUNT_KY"));
                try {
                    if (Double.valueOf(sumNow.replaceAll(",", "")) < po.getAmount()) {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!当前余额:" + sumNow);
                        throw e1;
                    }
                } catch (Exception ex) {
                    throw ex;
                }
            }

            if (!sellerId.equals(Constant.OEM_ACTIVITIES + "")) {
                state = Constant.CAR_FACTORY_PKG_STATE_01 + "";
            }
            po.setState(Integer.valueOf(state));
            if (state.equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02.toString())) {
                po.setSubmitDate(new Date());
                po.setSubmitBy(loginUser.getUserId());
            }
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            if (!"".equals(orderType)) {
                po.setOrderType(Integer.valueOf(orderType));
            }
            dao.insert(po);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存订单详细
     */
    private void insertDetail() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartSoManageDao dao = PartSoManageDao.getInstance();
            PartBaseQueryDao partBaseQueryDao = PartBaseQueryDao.getInstance();
            PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
            Double reCountMoney = 0d;
            Long soId = 0L;
            if (request.getParamValue("soId") == null) {
                soId = Long.parseLong(SequenceManager.getSequence(""));
                request.setAttribute("soId", soId);
            } else {
                soId = Long.valueOf(CommonUtils.checkNull(request.getParamValue("soId")));
            }

            String isBatchSo = CommonUtils.checkNull(request.getParamValue("isBatchSo"));
            String dealerIds = SequenceManager.getSequence("");
            String dealerId = "";
            String dealerCode = "";
            String dealerName = "";
            if (isBatchSo.equals(Constant.PART_BASE_FLAG_YES + "")) {
                //是铺货从ATTRIBUTE中取
                dealerId = CommonUtils.checkNull(request.getAttribute("dealerId"));
                dealerCode = CommonUtils.checkNull(request.getAttribute("dealerCode"));
                dealerName = CommonUtils.checkNull(request.getAttribute("dealerName"));

            } else {
                dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
                dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
                dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));

            }

            Double discount = Double.valueOf(partDlrOrderDao.getDiscount(dealerId));
            String[] partIdArr = request.getParamValues("cb");
            List<TtPartSoDtlPO> list = new ArrayList<TtPartSoDtlPO>();
            Map<String, Object> mainMap = dao.getSoOrderMain(soId + "");
            String soFrom = "";
            if (null != mainMap) {
                soFrom = CommonUtils.checkNull(mainMap.get("SO_FROM"));
            } else {
                soFrom = Constant.CAR_FACTORY_SO_FORM_01 + "";
            }
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id")); //备注
            String sellerId = "";//销售单位ID

            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                sellerId = beanList.get(0).getOrgId() + "";

            }
            for (int i = 0; i < partIdArr.length; i++) {
                //获取数据生成订单明细
                Long slineId = Long.parseLong(SequenceManager.getSequence(""));
                String partId = partIdArr[i];
                Map<String, Object> detailMap = dao.getSoOrderMDetail(CommonUtils.checkNull(soId), partId);
                Map<String, Object> partInfoMap = partBaseQueryDao.queryPartDetail(partId);
                String isGift = CommonUtils.checkNull(partInfoMap.get("IS_GIFT"));
                Double buyQty = Double.valueOf(request.getParamValue("buyQty_" + partId));
                String partCode = CommonUtils.checkNull(request.getParamValue("partCode_" + partId));
                String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldcode_" + partId));
                String partCname = CommonUtils.checkNull(request.getParamValue("partCname_" + partId));
                String unit = CommonUtils.checkNull(request.getParamValue("unit_" + partId));
                String isLack = CommonUtils.checkNull(request.getParamValue("isLack_" + partId));
                String isReplaced = CommonUtils.checkNull(request.getParamValue("isReplaced_" + partId));
                String isPlan = CommonUtils.checkNull(request.getParamValue("isPlan_" + partId));
                String isDirect = CommonUtils.checkNull(request.getParamValue("isDirect_" + partId));
                String stockQty = CommonUtils.checkNull(request.getParamValue("itemQty_" + partId));
                String minPackage = CommonUtils.checkNull(request.getParamValue("minPackage_" + partId));
                String remark = CommonUtils.checkNull(request.getParamValue("remark_" + partId));
                String orderId = CommonUtils.checkNull(detailMap.get("ORDER_ID"));
                String saleQty = CommonUtils.checkNull(request.getParamValue("buyQty_" + partId));
                //校验是否铺货
                Double price = 0D;
                if (isBatchSo.equals(Constant.PART_BASE_FLAG_YES + "")) {
                    price = Double.valueOf(dao.getPrice(dealerId, partId));
                } else {
                    price = Double.valueOf(request.getParamValue("buyPrice_" + partId).replaceAll(",", ""));
                }
                price = Arith.mul(price, discount);
                Double amount = Arith.mul(price, buyQty);
                reCountMoney = Arith.add(reCountMoney, amount);
                TtPartSoDtlPO po = new TtPartSoDtlPO();
                po.setSlineId(slineId);
                po.setSoId(Long.valueOf(soId));
                po.setPartId(Long.valueOf(partId));
                po.setPartCode(partCode);
                po.setPartOldcode(partOldCode);
                po.setPartCname(partCname);

                if (!"".equals(isPlan)) {
                    po.setIsPlan(Integer.valueOf(isPlan));
                }

                if (!"".equals(isGift)) {
                    po.setIsGift(Integer.valueOf(isGift));
                }

                if (!"".equals(orderId)) {
                    po.setOrderId(Long.valueOf(orderId));
                }
                if (!"".equals(isLack)) {
                    po.setIsLack(Integer.valueOf(isLack));
                }
                if (!"".equals(isReplaced)) {
                    po.setIsReplaced(Integer.valueOf(isReplaced));
                }

                po.setUnit(unit);

                po.setStockQty(Long.valueOf(stockQty));
                po.setMinPackage(Long.valueOf(minPackage));
                po.setSalesQty(Long.valueOf(request.getParamValue("buyQty_" + partId)));
                po.setBuyPrice(price);
                po.setBuyAmount(amount);
                po.setRemark(remark);
                if (!"".equals(saleQty)) {
                    po.setBuyQty(Long.valueOf(saleQty));
                }
                po.setCreateBy(loginUser.getUserId());
                po.setCreateDate(new Date());
                po.setVer(1);
                //如果订单生成
                if (soFrom.equals(Constant.CAR_FACTORY_SO_FORM_02 + "") || po.getBuyQty() != 0L) {
                    if (po.getBuyQty() < po.getSalesQty()) {
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "销售数量不得大于订购数量!");
                        throw e1;
                    }
                }
                //库存校验
                Map<String, Object> stockMap = partDlrOrderDao.getPartItemStock(whId, partId);

                if (stockMap == null) {
                    BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, partOldCode + ":库存不足!");
                    throw e;
                }
                String stockTty = CommonUtils.checkNull(stockMap.get("NORMAL_QTY"));
                Long saledQty = detailMap.get("SALES_QTY") == null ? 0L : Long.valueOf(detailMap.get("SALES_QTY") + "");
                if (Long.valueOf(stockTty) < po.getSalesQty()) {
                    BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, partOldCode + ":库存不足!");
                    throw e;
                }
                list.add(po);

                //如果是BO单转过来的销售单就更新 配件BO单和BO单生成的销售单关系表和BO明细表中的相关数量
                if (!"".equals(soFrom) && soFrom.equals(Constant.CAR_FACTORY_SO_FORM_04.toString())) {
                    //页面上输入的销售数量
                    Long salQty1 = Long.valueOf(request.getParamValue("buyQty_" + partId));
                    boolean isUpdate = false;
                    List<Map<String, Object>> reList = dao.queryBoDtl(soId, partId);
                    if (reList.size() > 0) {
                        Map<String, Object> countMap = dao.countBoDtl(soId, partId);
                        //当前配件所有剩余的bo数量
                        Long allBoOddQty = ((BigDecimal) countMap.get("ALL_BO_ODDQTY")).longValue();
                        //当前配件所有转销售数量
                        Long allToSalQty = ((BigDecimal) countMap.get("ALL_TOSAL_QTY")).longValue();
                        Long extraQty = 0l;//增加的销售数量
                        Long lessQty = 0l;//减少的销售数量

                        //如果页面上修改的销售数量大于bo转过来的销售数量并且bo中剩余的数量大于0
                        if (salQty1 > saledQty && allBoOddQty > 0) {
                            extraQty = salQty1 - saledQty;

                            //把多余的数量分配到bo中,用于抵消bo剩余数量
                            for (Map<String, Object> map : reList) {
                                Long relationId = ((BigDecimal) map.get("RELATION_ID")).longValue();
                                Long boLineId = ((BigDecimal) map.get("BOLINE_ID")).longValue();
                                Long boOddQty = ((BigDecimal) map.get("BO_ODDQTY")).longValue();

                                TtPartBoDtlPO dtlPO = new TtPartBoDtlPO();
                                TtPartBoDtlPO dtlPO1 = new TtPartBoDtlPO();
                                TtPartBoDtlPO dtlPO2 = new TtPartBoDtlPO();
                                dtlPO1.setBolineId(boLineId);
                                dtlPO = (TtPartBoDtlPO) dao.select(dtlPO1).get(0);

                                TtPartBotosoRelationPO relationPO = new TtPartBotosoRelationPO();
                                TtPartBotosoRelationPO relationPO1 = new TtPartBotosoRelationPO();
                                TtPartBotosoRelationPO relationPO2 = new TtPartBotosoRelationPO();
                                relationPO1.setRelationId(relationId);
                                relationPO = (TtPartBotosoRelationPO) dao.select(relationPO1).get(0);

                                if (extraQty > 0 && extraQty >= boOddQty) {//如果增加的销售数量大于剩余bo数量,那剩余的bo数量就全部转销售

                                    isUpdate = true;

                                    dtlPO2.setBoOddqty(0l);
                                    dtlPO2.setTosalQty(dtlPO.getTosalQty() + boOddQty);
                                    dtlPO2.setStatus(0);

                                    relationPO2.setTosalesQty(relationPO.getTosalesQty() + boOddQty);
                                    relationPO2.setUpdateBy(loginUser.getUserId());
                                    relationPO2.setUpdateDate(new Date());

                                    dao.update(dtlPO1, dtlPO2);
                                    dao.update(relationPO1, relationPO2);
                                    extraQty = extraQty - boOddQty;
                                } else if (extraQty > 0) {//如果还有销售数量可用,但是此时的销售数量已经小于剩余bo数量

                                    isUpdate = true;

                                    dtlPO2.setBoOddqty(boOddQty - extraQty);
                                    dtlPO2.setTosalQty(relationPO.getTosalesQty() + extraQty);

                                    relationPO2.setTosalesQty(relationPO.getTosalesQty() + extraQty);
                                    dao.update(dtlPO1, dtlPO2);
                                    dao.update(relationPO1, relationPO2);
                                    extraQty = 0l;
                                    break;
                                }
                            }

                        }
                        //当页面修改的销售数量小于转销售数量
                        if (salQty1 < saledQty) {

                            if (reList.size() == 1) {//如果是单个转销售单

                                Map<String, Object> map = reList.get(0);

                                Long relationId = ((BigDecimal) map.get("RELATION_ID")).longValue();
                                Long boLineId = ((BigDecimal) map.get("BOLINE_ID")).longValue();

                                TtPartBoDtlPO dtlPO = new TtPartBoDtlPO();
                                TtPartBoDtlPO dtlPO1 = new TtPartBoDtlPO();
                                TtPartBoDtlPO dtlPO2 = new TtPartBoDtlPO();
                                dtlPO1.setBolineId(boLineId);
                                dtlPO = (TtPartBoDtlPO) dao.select(dtlPO1).get(0);

                                TtPartBotosoRelationPO relationPO = new TtPartBotosoRelationPO();
                                TtPartBotosoRelationPO relationPO1 = new TtPartBotosoRelationPO();
                                TtPartBotosoRelationPO relationPO2 = new TtPartBotosoRelationPO();
                                relationPO1.setRelationId(relationId);
                                relationPO = (TtPartBotosoRelationPO) dao.select(relationPO1).get(0);
                                Long toSalesQty = relationPO.getTosalesQty();//关系表中已转销售数量

                                if (salQty1 < toSalesQty) {//如果页面上减小后的销售数量小于toSalesQty

                                    lessQty = toSalesQty - salQty1;

                                    if (salQty1 == 0) {//如果销售数量减少为0,就删除关系表中对应的数据,并更新bo单中的数量

                                        dao.delete(relationPO1);

                                    } else {
                                        relationPO2.setTosalesQty(salQty1);
                                        dao.update(relationPO1, relationPO2);
                                    }

                                    isUpdate = true;

                                    dtlPO2.setBoOddqty(dtlPO.getBoOddqty() + lessQty);
                                    dtlPO2.setTosalQty(dtlPO.getTosalQty() - lessQty);
                                    dtlPO2.setStatus(1);
                                    dao.update(dtlPO1, dtlPO2);

                                }

                            } else if (reList.size() > 1 && salQty1 < allToSalQty) {//如果是汇总转销售单

                                lessQty = allToSalQty - salQty1;

                                for (Map<String, Object> map : reList) {
                                    Long relationId = ((BigDecimal) map.get("RELATION_ID")).longValue();
                                    Long boLineId = ((BigDecimal) map.get("BOLINE_ID")).longValue();

                                    TtPartBoDtlPO dtlPO = new TtPartBoDtlPO();
                                    TtPartBoDtlPO dtlPO1 = new TtPartBoDtlPO();
                                    TtPartBoDtlPO dtlPO2 = new TtPartBoDtlPO();
                                    dtlPO1.setBolineId(boLineId);
                                    dtlPO = (TtPartBoDtlPO) dao.select(dtlPO1).get(0);

                                    TtPartBotosoRelationPO relationPO = new TtPartBotosoRelationPO();
                                    TtPartBotosoRelationPO relationPO1 = new TtPartBotosoRelationPO();
                                    TtPartBotosoRelationPO relationPO2 = new TtPartBotosoRelationPO();
                                    relationPO1.setRelationId(relationId);
                                    relationPO = (TtPartBotosoRelationPO) dao.select(relationPO1).get(0);
                                    Long toSalesQty = relationPO.getTosalesQty();//关系表中已转销售数量

                                    if (lessQty > 0 && lessQty >= toSalesQty) {//如果减少的数量大于或等于当前已转销售数量

                                        isUpdate = true;

                                        dtlPO2.setBoOddqty(dtlPO.getBoOddqty() + toSalesQty);
                                        dtlPO2.setTosalQty(dtlPO.getTosalQty() - toSalesQty);
                                        dtlPO2.setStatus(1);
                                        dao.update(dtlPO1, dtlPO2);

                                        dao.delete(relationPO1);

                                        lessQty = lessQty - toSalesQty;

                                    } else if (lessQty > 0) {

                                        isUpdate = true;

                                        dtlPO2.setBoOddqty(dtlPO.getBoOddqty() + lessQty);
                                        dtlPO2.setTosalQty(dtlPO.getTosalQty() - lessQty);
                                        dtlPO2.setStatus(1);

                                        relationPO2.setTosalesQty(toSalesQty - lessQty);
                                        dao.update(relationPO1, relationPO2);

                                        dao.update(dtlPO1, dtlPO2);

                                        lessQty = 0l;
                                        break;
                                    }

                                }

                            }

                        }

                        if (isUpdate) {
                            for (Map<String, Object> map : reList) {
                                Long boId = ((BigDecimal) map.get("BO_ID")).longValue();

                                //判断当前bo单是否已经处理完毕,如果处理完毕就更新状态为已处理,否则更新为部分处理
                                TtPartBoMainPO smainPO = new TtPartBoMainPO();
                                smainPO.setBoId(boId);
                                boolean flag = partBoDao.isCloseAll(boId + "");
                                TtPartBoMainPO mainPO = new TtPartBoMainPO();
                                if (flag) {
                                    mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
                                } else {
                                    mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_02);
                                }
                                dao.update(smainPO, mainPO);
                            }
                        }

                    }

                }
            }
            TtPartSoDtlPO dtlPo = new TtPartSoDtlPO();
            dtlPo.setSoId(Long.valueOf(soId));
            dao.delete(dtlPo);
            for (TtPartSoDtlPO po : list) {
                dao.insert(po);
            }
            request.setAttribute("reCountMoney", reCountMoney);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存日志
     */
    public void insertHistory(RequestWrapper req, ActionContext act, int status) throws Exception {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
            Long optId = Long.parseLong(SequenceManager.getSequence(""));
            po.setBussinessId(CommonUtils.checkNull(req.getAttribute("soCode")));
            po.setOptId(optId);
            po.setOptBy(logonUser.getUserId());
            po.setOptDate(new Date());
            po.setOptType(Constant.PART_OPERATION_TYPE_02);
            po.setWhat("配件销售单");
            po.setOptName(logonUser.getName());
            if (!"".equals(CommonUtils.checkNull(req.getAttribute("orderId")))) {
                po.setOrderId(Long.valueOf(CommonUtils.checkNull(req.getAttribute("orderId"))));
            } else if (!"".equals(CommonUtils.checkNull(req.getParamValue("orderId")))) {
                po.setOrderId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId"))));
            }
            po.setStatus(status);
            dao.insert(po);
        } catch (Exception ex) {
            throw ex;
        }
    }


    /**
     * 配件销售单明细查询
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 订单详细
     */
    public void detailOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
            String buttonFalg = "";//采购订单查看 隐藏 【返回】按钮
            if (null != request.getParamValue("buttonFalg")) {
                buttonFalg = request.getParamValue("buttonFalg");
            }
            PartSoManageDao dao = PartSoManageDao.getInstance();
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            //订单明细
            List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
            //Map<String,Object> outMap = dao.getOutAmount(soId);
            /*for(Map map:detailList){
                if(outMap!=null&&!outMap.isEmpty()){
					String outId = CommonUtils.checkNull(outMap.get("OUT_ID"));
					Double outQty = Double.valueOf(CommonUtils.checkNull(dao.getOutQty(outId)));
					Double price = Double.valueOf(CommonUtils.checkNull(map.get("BUY_PRICE")));
					Double amount = Arith.mul(outQty,price);
					map.put("outQty", outQty);
					map.put("amount", amount);
				}else{
					map.put("outQty", "0");
					map.put("amount", "0.00");
				}
			}*/
            List<Map<String, Object>> historyList = dao.queryOrderHistory(soCode);
            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            mainMap.put("TRANS_TYPE", transType);
            act.setOutData("mainMap", mainMap);
            act.setOutData("detailList", detailList);
            act.setOutData("historyList", historyList);
            act.setOutData("buttonFalg", buttonFalg);
            act.setOutData("soCode", soCode);
            act.setOutData("soId", soId);
            act.setForword(PART_DLR_ORDER_SO_Detail);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单查看详细数据出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    /**
     * 销售单明细下载
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-10
     * @Title : 订单明细导出
     */
    public void exportOrderExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            PartSoManageDao dao = PartSoManageDao.getInstance();
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode")); // 订单单号
            String soId = CommonUtils.checkNull(request.getParamValue("soId")); // 订单ID

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            //head[3] = "配件件号";
            head[3] = "最小包装量";
            head[4] = "单位";
            head[5] = "采购数量";
            head[6] = "销售数量";
            head[7] = "销售单价";
            head[8] = "销售金额";
//            head[9] = "出库数量";
//            head[10] = "出库金额";
            head[9] = "备注";
//            head[10] = "批次号";
//            head[11] = "货位";

            List<Map<String, Object>> list = dao.getSoOrderMDetail(soId);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CNAME"));
//                        detail[3] = CommonUtils.checkNull(map
//                                .get("PART_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("MIN_PACKAGE"));
                        detail[4] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[5] = CommonUtils.checkNull(map.get("BUY_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("SALES_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("CONVERS_PRICE"));
                        detail[8] = CommonUtils.checkNull(map.get("CONVERS_AMOUNT"));
//                        detail[9] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
//                        detail[10] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
                        detail[9] = CommonUtils.checkNull(map.get("REMARK"));
                        detail[10] = CommonUtils.checkNull(map.get("BATCH_NO"));
                        detail[11] = CommonUtils.checkNull(map.get("LOC_CODE"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "销售单[" + soCode + "]明细";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出采购订单明细失败");
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

    //后修改发运类型为配置！！！
    public Map<String, Object> getTransMap() throws Exception {
        List<TtPartFixcodeDefinePO> list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
        Map<String, Object> transMap = new HashMap<String, Object>();
        for (TtPartFixcodeDefinePO po : list) {
            //list.fixValue }">${list.fixName
            transMap.put(po.getFixValue(), po.getFixName());
        }
        return transMap;
    }

    /**
     * @param : LastDate    : 2013-4-19
     * @return :
     * @throws :
     * @Title :
     * @Description: 校验余额
     */
    public boolean validateSum(String dealerId, String parentId, String sum) throws Exception {
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, "");
        String sumNow = acountMap.get("ACCOUNT_KY").toString();
        try {
            if (Double.valueOf(sumNow.replaceAll(",", "")) < parseDouble(sum)) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description:作废
     */
    public void cancelOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        act.getResponse().setContentType("application/json");
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            String flag = null;//1订单、2Bo单
            PartSoManageDao dao = PartSoManageDao.getInstance();
            TtPartSoMainPO po = new TtPartSoMainPO();
            po.setSoId(Long.valueOf(soId));
            po.setState(Constant.CAR_FACTORY_SALE_ORDER_STATE_03);
            TtPartSoMainPO oldPo = new TtPartSoMainPO();
            oldPo.setSoId(Long.valueOf(soId));

            //释放占用金额
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "") && !(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_06 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "销售单状态已经改变，无法修改，请重新确认!");
                throw e1;
            }
            dao.update(oldPo, po);
            String soFrom = CommonUtils.checkNull(mainMap.get("SO_FROM"));
            String orderId = "";
            if (soFrom.equals(Constant.CAR_FACTORY_SO_FORM_02 + "") && mainMap.get("ORDER_ID") != null) {
                orderId = CommonUtils.checkNull(mainMap.get("ORDER_ID"));
                flag = "1";
            } else {
                flag = "2";
            }

            //订单回滚
            if ("1".equals(flag)) {
                TtPartBoMainPO boMainPO = new TtPartBoMainPO();
                boMainPO.setOrderId(Long.valueOf(orderId));
                if (dao.select(boMainPO).size() > 0) {
                    boMainPO = (TtPartBoMainPO) dao.select(boMainPO).get(0);
                    //删除以前产生的BO单信息
                    if (boMainPO.getBoId() != null) {
                        TtPartBoDtlPO boDtlPO = new TtPartBoDtlPO();
                        boDtlPO.setBoId(boMainPO.getBoId());
                        dao.delete(boDtlPO);

                        TtPartBoMainPO boMainPO1 = new TtPartBoMainPO();
                        boMainPO1.setBoId(boMainPO.getBoId());
                        dao.delete(boMainPO1);
                    }
                }
                //更新订单状态
                TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
                mainPO.setOrderId(Long.valueOf(orderId));
                mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03);

                TtPartDlrOrderMainPO mainPO2 = new TtPartDlrOrderMainPO();
                mainPO2.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);

                //回滚预占金额
                TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
                recordPO.setSourceId(Long.valueOf(orderId));

                TtPartAccountRecordPO upPo = new TtPartAccountRecordPO();
                upPo.setAmount(((TtPartDlrOrderMainPO) dao.select(mainPO).get(0)).getOrderAmount());
                upPo.setOrderId(0l);
                upPo.setOrderCode("");

                dao.update(recordPO, upPo);

                dao.update(mainPO, mainPO2);
            } else {
                //回滚BO单数据
                ArrayList al = new ArrayList();
                al.add(0, soId);
                al.add(1, soId);
                al.add(2, soId);
                dao.update("UPDATE TT_PART_BO_DTL D\n" +
                        "   SET D.TOSAL_QTY = D.TOSAL_QTY - (SELECT BR.TOSALES_QTY\n" +
                        "                                      FROM TT_PART_BOTOSO_RELATION BR\n" +
                        "                                     WHERE BR.BO_ID = D.BO_ID\n" +
                        "                                       AND BR.PART_ID = D.PART_ID\n" +
                        "                                       AND BR.SO_ID = ?),\n" +
                        "       D.BO_ODDQTY = D.BO_ODDQTY + (SELECT BR.TOSALES_QTY\n" +
                        "                                      FROM TT_PART_BOTOSO_RELATION BR\n" +
                        "                                     WHERE BR.BO_ID = D.BO_ID\n" +
                        "                                       AND BR.PART_ID = D.PART_ID\n" +
                        "                                       AND BR.SO_ID = ?),\n" +
                        "       D.STATUS    = 1\n" +
                        " WHERE EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_BOTOSO_RELATION R\n" +
                        "         WHERE R.BO_ID = D.BO_ID\n" +
                        "           AND R.PART_ID = D.PART_ID\n" +
                        "           AND R.SO_ID = ?)\n", al);
                //作废记录
                ArrayList al2 = new ArrayList();
                al2.add(0, loginUser.getUserId());
                al2.add(1, soId);
                dao.update("UPDATE TT_PART_BOTOSO_RELATION BR\n" +
                        "   SET BR.STATUS = 0, BR.UPDATE_BY = ?, BR.UPDATE_DATE = SYSDATE\n" +
                        " WHERE BR.SO_ID = ?", al2);
                //回滚BO单状态
                ArrayList al3 = new ArrayList();
                al3.add(0, soId);
                dao.update("UPDATE TT_PART_BO_MAIN BM\n" +
                        "   SET BM.STATE = 92141001\n" +
                        " WHERE EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_BO_DTL BD\n" +
                        "         WHERE BD.BO_ID = BM.BO_ID HAVING SUM(BD.BO_ODDQTY) > 0)\n" +
                        "   AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_BOTOSO_RELATION R\n" +
                        "         WHERE R.BO_ID = BM.BO_ID\n" +
                        "           AND R.SO_ID = ?)", al3);

                ArrayList al4 = new ArrayList();
                al4.add(0, soId);
                dao.update("UPDATE TT_PART_BO_MAIN BM\n" +
                        "   SET BM.STATE = 92141002\n" +
                        " WHERE EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_BO_DTL BD\n" +
                        "         WHERE BD.BO_ID = BM.BO_ID HAVING SUM(BD.Tosal_Qty) > 0)\n" +
                        "   AND EXISTS (SELECT 1\n" +
                        "          FROM TT_PART_BOTOSO_RELATION R\n" +
                        "         WHERE R.BO_ID = BM.BO_ID\n" +
                        "           AND R.SO_ID = ?)", al4);
                //删除预占资金
                TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
                recordPO.setSourceId(Long.valueOf(soId));
                dao.delete(recordPO);
                //双重校验，防止漏删
                TtPartAccountRecordPO recordPO1 = new TtPartAccountRecordPO();
                recordPO1.setOrderId(Long.valueOf(soId));
                dao.delete(recordPO1);
            }
            //非直发销售单释放占用资源
            if (!(mainMap.get("ORDER_TYPE") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                ArrayList ins = new ArrayList();
                ins.add(0, Long.valueOf(soId));
                ins.add(1, Constant.PART_CODE_RELATION_07);
                ins.add(2, 0);//1占用，0释放
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
            }
            this.insertHistory(request, act, Constant.CAR_FACTORY_SALE_ORDER_STATE_03);
            //删除作废销售单
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setSoId(Long.valueOf(soId));

            TtPartSoDtlPO soDtlPO = new TtPartSoDtlPO();
            soDtlPO.setSoId(Long.valueOf(soId));

            TtPartBookDtlPO bookDtlPO = new TtPartBookDtlPO();
            bookDtlPO.setOrderId(Long.valueOf(soId));

            dao.delete(soMainPO);
            dao.delete(soDtlPO);
            dao.delete(bookDtlPO);

            act.setOutData("success", "操作成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单作废出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description:提交
     */
    public void commitOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        act.getResponse().setContentType("application/json");
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dealerId = "";
            int state = Constant.CAR_FACTORY_ORDER_CHECK_STATE_02;

            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
            Map<String, Object> mainMap = partSoManageDao.getSoOrderMain(soId);

            if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "") && !(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_06 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "销售单状态已经改变，请勿重复提交!");
                throw e1;
            }


            String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
            if (!sellerId.equals(Constant.OEM_ACTIVITIES + "")) {
                state = Constant.CAR_FACTORY_ORDER_CHECK_STATE_05;
            }
            PartSoManageDao dao = PartSoManageDao.getInstance();

            TtPartSoMainPO po = new TtPartSoMainPO();
            po.setState(state);
            po.setSubmitBy(loginUser.getUserId());//modify by yuan 20130529
            po.setSubmitDate(new Date());//modify by yuan 20130529
            TtPartSoMainPO oldPo = new TtPartSoMainPO();
            oldPo.setSoId(Long.valueOf(soId));
            dao.update(oldPo, po);
            request.setAttribute("soCode", CommonUtils.checkNull(mainMap.get("SO_CODE")));
            this.insertHistory(request, act, state);
            dao.delInvalidSo();//提交时剔除无效销售单（BO转的销售单）
            act.setOutData("success", "提交成功!");
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);

                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单提交出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description:修改页面
     */
    public void modifyOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        act.getResponse().setContentType("application/json");
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            PartSoManageDao dao = PartSoManageDao.getInstance();
            PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "") && !(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_06 + "")) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "销售单状态已经改变，无法修改，请重新查询!");
                throw e1;
            }
            List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(soId);
            String dealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }

            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(dealerId);
            request.setAttribute("wareHouseList", wareHouseList);

            String discount = partDlrOrderDao.getDiscount(mainMap.get("DEALER_ID").toString());
            mainMap.put("discount", discount);
            Map<String, Object> accountMap = partDlrOrderDao.getAccount(mainMap.get("DEALER_ID").toString(), mainMap.get("SELLER_ID").toString(), "");
            for (Map<String, Object> map : detailList) {
                Double price1 = Arith.mul(Double.valueOf(map.get("BUY_PRICE").toString()), Double.valueOf(discount));
                map.put("price1", price1);
            }
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            act.setOutData("mainMap", mainMap);
            request.setAttribute("transList", list);
            act.setOutData("dealerId", dealerId);
            act.setOutData("accountMap", accountMap);
            act.setOutData("detailList", detailList);
            act.setOutData("historyList", historyList);
            act.setForword(PART_DLR_ORDER_SO_MODIFY);
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER_SO_MAIN);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单修改出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER_SO_MAIN);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description:修改
     */
    public void saveModify() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        act.getResponse().setContentType("application/json");
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartSoManageDao dao = PartSoManageDao.getInstance();
        PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();

        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            ArrayList ins = new ArrayList();
            if (!"".equals(CommonUtils.checkNull(mainMap.get("WH_ID")))) {
                //先释放
                ins.add(0, Long.valueOf(soId));
                ins.add(1, Constant.PART_CODE_RELATION_07);
                ins.add(2, 0);//1占用，0释放
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
            }

            this.insertDetail();
            this.updateMain();

            //不是车厂的需要验证资金
            String dealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
            if (!CommonUtils.checkNull(mainMap.get("DEALER_ID")).equals(Constant.OEM_ACTIVITIES)) {//无效语句永远都成立
                String orderId = "";
                String accountId = "";
                //如果是订单生成需要释放订单部分的占用资金
                /*if(CommonUtils.checkNull(mainMap.get("SO_FROM")).equals(Constant.CAR_FACTORY_SO_FORM_02+"")){
                    orderId = CommonUtils.checkNull(mainMap.get("ORDER_ID"));
                    Map<String,Object> map = new HashMap<String,Object>();
                    List<Map<String,Object>> list = dao.getAccountRecord(orderId);
                    if(null!=list&&list.size()>0&&null!=list.get(0)){
                        Map<String,Object> accountMap = list.get(0);
                        accountId = CommonUtils.checkNull(accountMap.get("ACCOUNT_ID"));
                        if(!"".equals(accountId)){
                            String sumAmount = CommonUtils.checkNull(accountMap.get("SUMAMOUNT"));
                            if(!"".equals(accountId)){
                                //存在订单占用记录   需要抹平记录   插入负值
                                Double amount = Double.valueOf(sumAmount);
                                if(amount<0){
                                    map.put("sourceId", orderId);
                                    map.put("sourceCode", CommonUtils.checkNull(mainMap.get("ORDER_CODE")));
                                    map.put("orderId", soId);
                                    map.put("orderCode", CommonUtils.checkNull(mainMap.get("SO_CODE")));
                                    map.put("dealerId", dealerId);
                                    map.put("accountId", accountId);
                                    map.put("amount", amount);
                                    map.put("funcName", "销售单修改释放");
                                    map.put("changeType", Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02);
                                    insertAccountForModify(map);
                                }
                            }
                        }
                    }
                }
                //查询判断是否做过修改或者直接销售单占用  如果修改会有order_id为销售单号的占用 将金额抹平
                Map<String,Object> map = new HashMap<String,Object>();
                List<Map<String,Object>> list = dao.getAccountRecord(soId);
                if(null!=list&&list.size()>0&&null!=list.get(0)){
                    Map<String,Object> accountMap = list.get(0);
                    accountId = CommonUtils.checkNull(accountMap.get("ACCOUNT_ID"));
                    if(!"".equals(accountId)){
                        String sumAmount = CommonUtils.checkNull(accountMap.get("SUMAMOUNT"));
                        if(!"".equals(accountId)){
                            //存在销售单占用记录   需要抹平记录   插入负值
                            Double amount = 0-Double.valueOf(sumAmount);
                            if(amount<0){
                                map.put("sourceId", orderId);
                                map.put("sourceCode", CommonUtils.checkNull(mainMap.get("ORDER_CODE")));
                                map.put("orderId",soId );
                                map.put("orderCode", CommonUtils.checkNull(mainMap.get("SO_CODE")));
                                map.put("dealerId", dealerId);
                                map.put("accountId", accountId);
                                map.put("amount", amount);
                                map.put("funcName", "销售单修改释放");
                                map.put("changeType", Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02);
                                insertAccountForModify(map);
                            }
                        }
                    }
                }*/

                Map<String, Object> accountMap = dao.getAccount(dealerId, CommonUtils.checkNull(mainMap.get("SELLER_ID")));
                if (null != accountMap && accountMap.size() != 0) {
                    //重新占用资金
                    Map<String, Object> newMap = new HashMap<String, Object>();
                    Double amount = Double.valueOf(request.getAttribute("reCountMoney").toString()); //获取JAVA重新计算的金额;
                    //add by yuan 20131001 //修改前和修改后差额,存在已经占用的资金，此处只需要比较修改前后的差额和当前余额即可
                    Double oldAmount = Double.valueOf(mainMap.get("AMOUNT").toString());
                    Double balance = Arith.sub(amount, oldAmount);
                    //add by yuan 20131211 内部领件单不需要校验余额
                    if (!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09 + "") && !this.validateSum(dealerId, CommonUtils.checkNull(mainMap.get("SELLER_ID")), balance + "")) {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!");
                        throw e1;
                    }

                    accountId = CommonUtils.checkNull(accountMap.get("ACCOUNT_ID"));
                    if (CommonUtils.checkNull(mainMap.get("SO_FROM")).equals(Constant.CAR_FACTORY_SO_FORM_02 + "")) {
                        newMap.put("sourceId", orderId);
                        newMap.put("sourceCode", CommonUtils.checkNull(mainMap.get("ORDER_CODE")));
                    } else {
                        newMap.put("sourceId", soId);
                        newMap.put("sourceCode", CommonUtils.checkNull(mainMap.get("SO_CODE")));
                    }

                    newMap.put("orderId", soId);
                    newMap.put("orderCode", CommonUtils.checkNull(mainMap.get("SO_CODE")));
                    newMap.put("dealerId", dealerId);
                    newMap.put("accountId", accountId);
                    newMap.put("amount", amount);
                    newMap.put("funcName", "销售单修改占用");
                    newMap.put("changeType", Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01);
                    insertAccountForModify(newMap);
                }
            }
            //再预占
            ins = new ArrayList();
            ins.add(0, Long.valueOf(soId));
            ins.add(1, Constant.PART_CODE_RELATION_07);
            ins.add(2, 1);//1占用，0释放
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
            //POContext.endTxn(true);
            act.setOutData("success", "保存成功!");
        } catch (Exception e) {//异常方法
            act.setOutData("error", "保存失败!");
            if (e instanceof BizException) {
                //POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            //POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存订单出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        } finally {
            try {
                //POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "保存订单出错,请联系管理员!");
            }
        }
    }

    private Double parseDouble(Object obj) throws Exception {
        ActionContext act = ActionContext.getContext();
        String str = CommonUtils.checkNull(obj);
        try {
            if (str.indexOf(",") > -1) {
                String[] strArr = str.split("\\,");
                str = "";
                for (int i = 0; i < strArr.length; i++) {
                    str += strArr[i];
                }
            }
            return Double.valueOf(str);
        } catch (Exception ex) {
            BizException e = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "数字转换错误!");
            throw e;
        }
    }

    public void insertAccountForModify(Map<String, Object> map) throws Exception {
        PartSoManageDao dao = PartSoManageDao.getInstance();
        ActionContext act = ActionContext.getContext();
        try {
            String orderCode = CommonUtils.checkNull(map.get("orderCode"));
            Double amount = Double.valueOf(CommonUtils.checkNull(map.get("amount")));

            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            po.setOrderCode(orderCode);
            TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
            recordPO.setAmount(amount);

            dao.update(po, recordPO);
        } catch (Exception e) {
            throw e;
        }
    }

    private void updateMain() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartSoManageDao dao = PartSoManageDao.getInstance();
        PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
        try {
            String dealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            Long soId = Long.valueOf(CommonUtils.checkNull(request.getParamValue("soId")));
            TtPartSoMainPO po = new TtPartSoMainPO();
            TtPartSoMainPO oldPo = new TtPartSoMainPO();
            oldPo.setSoId(soId);
            String payType = CommonUtils.checkNull(request.getParamValue("payType")); //付费方式
            String sellerId = CommonUtils.checkNull(request.getParamValue("SELLER_ID"));
            String consignees = CommonUtils.checkNull(request.getParamValue("RCV_ORG")); //接收单位
            String consigneesId = CommonUtils.checkNull(request.getParamValue("RCV_ORGID")); //接收单位ID
            String addr = CommonUtils.checkNull(request.getParamValue("ADDR")); //接收地址ID
            String addrId = CommonUtils.checkNull(request.getParamValue("ADDR_ID")); //接收地址ID
            String receiver = CommonUtils.checkNull(request.getParamValue("RECEIVER")); //接收人
            String tel = CommonUtils.checkNull(request.getParamValue("TEL")); //接收人电话
            String postCode = CommonUtils.checkNull(request.getParamValue("POST_CODE")); //接收邮编
            String station = CommonUtils.checkNull(request.getParamValue("STATION")); //接收站
            String transType = CommonUtils.checkNull(request.getParamValue("transType")); //发运方式
            String transpayType = CommonUtils.checkNull(request.getParamValue("transpayType")); //发运付费方式
            Double amount = Double.valueOf(request.getAttribute("reCountMoney").toString()); //获取JAVA重新计算的金额
            String discount = CommonUtils.checkNull(request.getParamValue("discount")); //折扣
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); //订单备注
            String remark2 = CommonUtils.checkNull(request.getParamValue("REMARK2")); //备注
            String whId = CommonUtils.checkNull(request.getParamValue("wh_id")); //备注
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); //订单类型
            String bookDealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            Double freight = 0D;
            //向主机厂采购需要计算运费
            if ((sellerId).equals(Constant.OEM_ACTIVITIES)) {
                freight = Double.valueOf(partDlrOrderDao.getFreight(bookDealerId, orderType, amount + "").replaceAll(",", "")); //运费
            }
            //免运费销售单不计算运费
            if (((TtPartSoMainPO) dao.select(oldPo).get(0)).getIsTransfree().equals(Constant.IF_TYPE_YES)) {
                freight = 0D;
            }
            //BO转销售单不需要运费
            if (((TtPartSoMainPO) dao.select(oldPo).get(0)).getSoFrom().equals(Constant.CAR_FACTORY_SO_FORM_04)) {
                freight = 0D;
            }
            //自提不需要运费
            if (transType.equals("3")) {
                freight = 0D;
            }

            amount = Arith.add(amount, freight);
            request.setAttribute("reCountMoney", amount);
            po.setFreight(freight);

            if (freight > 0) {
                po.setIsTransfree(Constant.PART_BASE_FLAG_NO);
            } else {
                po.setIsTransfree(Constant.PART_BASE_FLAG_YES);
            }
            po.setSoId(soId);
            po.setPayType(Integer.valueOf(payType));
            po.setUpdateDate(new Date());
            po.setUpdateBy(loginUser.getUserId());

            po.setConsigneesId(Long.valueOf(consigneesId));
            po.setConsignees(consignees);
            po.setAddr(addr);
            po.setAddrId(Long.valueOf(addrId));
            po.setReceiver(receiver);
            po.setTel(tel);
            po.setPostCode(postCode);
            po.setStation(station);
            po.setTransType(transType);
            po.setTranspayType(Integer.valueOf(transpayType));
            po.setAmount(amount);
            po.setDiscount(Double.valueOf(discount));
            po.setRemark(remark);
            po.setRemark2(remark2);
            po.setWhId(Long.valueOf(whId));
            po.setOrderType(Integer.valueOf(orderType));
            dao.update(oldPo, po);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 导出EXECEL模板
     * @Description: TODO
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
            listHead.add("销售数量");
            list.add(listHead);
            // 导出的文件名
            String fileName = "销售销售单模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
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

    public void uploadExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        InputStream is = null;
        try {
            String filePath = CommonUtils.checkNull(request.getParamValue("uploadFile1"));
            File file = new File(filePath);
            try {
                is = new FileInputStream(file);
            } catch (Exception ex) {
                if (filePath.indexOf("fakepath") > -1) {
                    act.setOutData("error", "浏览器设置出错</br>" + "请点击工具 -> Internet选项 -> 安全 </br>-> 自定义级别 ->其他 </br>启用：将文件上载到服务器时包含本地目录路径");
                    throw ex;
                }
                if (ex instanceof FileNotFoundException) {
                    act.setOutData("error", "文件不存在!");
                    throw ex;
                }
                act.setOutData("error", "读取文件出错!");
                throw ex;
            }
            List<Map<String, Cell[]>> excelList = readExcel(is, 2);
            Map<String, Object> dataMap = loadDataList(excelList);
            act.setOutData("partData", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, act.getOutData("error") == null ? "文件读取错误" : act.getOutData("error").toString());
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception ex) {
                    BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "文件流关闭错误!");
                    logger.error(logonUser, e1);
                    act.setException(e1);
                }
            }
        }
    }


    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :循环获取CELL生成VO
     * @Description: TODO
     */
    private Map<String, Object> loadDataList(List<Map<String, Cell[]>> list) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList();
        Map<String, Object> rtnMap = new HashMap();
        String error = "";
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
                try {
                    parseCells(dataList, key, cells);
                } catch (Exception e) {
                    if (e instanceof BizException) {
                        BizException e1 = (BizException) e;
                        error += "上传文件的第" + ((i + 1) + "") + "行," + e1.getMessage().replaceAll("操作失败！失败原因：", "") + "</br>";
                        continue;
                    } else {
                        throw e;
                    }
                }
            }
        }
        rtnMap.put("dataList", dataList);
        rtnMap.put("error", error);
        return rtnMap;
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 查看配件
     */
    public void showPartBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            PartSoManageDao dao = PartSoManageDao.getInstance();
            //分页方法 begin
            Integer curPage = req.getParamValue("curPage") != null ? Integer
                    .parseInt(req.getParamValue("curPage"))
                    : 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = null;
            String sellerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                sellerId = beanList.get(0).getOrgId() + "";
            }
            ps = dao.showPartBase(req, req.getParamValue("dealerId"), sellerId, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查看配件信息出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 装载VO
     * @Description: TODO
     */
    private void parseCells(List<Map<String, Object>> list, String rowNum, Cell[] cells) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        RequestWrapper request = act.getRequest();
        String error = "";
        if ("" == CommonUtils.checkNull(cells[0].getContents())) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件编码为空!");
            throw e;
        }

        request.setAttribute("partOldcode", CommonUtils.checkNull(cells[0].getContents()));
        request.setAttribute("uploadFlag", "upload");


        PageResult<Map<String, Object>> ps = null;
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        request.setAttribute("bookDealerId", dealerId);
        ps = dao.showPartBase(request, CommonUtils.checkNull(request.getParamValue("dealerId")), 1, Constant.PAGE_SIZE);
        request.removeAttribute("uploadFlag");
        if (null == ps) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件" + cells[0].getContents() + "不存在!");
            throw e;
        }
        if (null == ps.getRecords() || ps.getRecords().size() <= 0) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件" + cells[0].getContents() + "不存在!");
            throw e;
        }
        List<Map<String, Object>> partList = ps.getRecords();
        Map<String, Object> partMap = partList.get(0);
        if (null == partMap) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件" + cells[0].getContents() + "不存在!");
            throw e;
        }
        if ("".equals(CommonUtils.checkNull(partMap.get("SALE_PRICE1")))) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件" + cells[0].getContents() + "不存在价格!");
            throw e;
        }
        if (!"".equals(CommonUtils.checkNull(request.getParamValue("wh_id")))) {
            request.setAttribute("orgId", dealerId);
            Map<String, Object> stockQty = dao.getPartItemStock(CommonUtils.checkNull(request.getParamValue("wh_id")), CommonUtils.checkNull(partMap.get("PART_ID")));
            if (stockQty != null) {
                partMap.put("ITEM_QTY", CommonUtils.checkNull(stockQty.get("NORMAL_QTY")));
            }
        }
        if ("" != CommonUtils.checkNull(cells[1].getContents())) {
            try {
                Double.valueOf(cells[1].getContents());
                partMap.put("buyQty", Double.valueOf(cells[1].getContents()));
                list.add(partMap);
            } catch (Exception e) {
                //即使错误了，也只是不显示数量  
                list.add(partMap);
                BizException ex = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "销售数量格式错误!");
                throw ex;
            }
        }

    }


    public List<Map<String, Cell[]>> readExcel(InputStream is, int rowSize) throws Exception {
        List<Map<String, Cell[]>> list = new ArrayList();
        Sheet sheet = null;
        try {
            Workbook wb = Workbook.getWorkbook(is);
            int len = is.read();
            // 如果多页则遍历
            Sheet[] sheets = wb.getSheets();
            for (int i = 0; i < sheets.length; i++) {  //页
                sheet = sheets[i];
                for (int j = 1; j < sheet.getRows(); j++) {  //行
                    Map<String, Cell[]> map = new HashMap<String, Cell[]>();
                    Cell[] cells = sheet.getRow(j);
                    map.put(j + "", cells);
                    list.add(map);
                }
            }
            return list;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public void exportSoOrderExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartSoManageDao dao = PartSoManageDao.getInstance();
            String[] head = new String[18];
            head[0] = "销售单号 ";
            head[1] = "备注";
            head[2] = "订单号";
            head[3] = "发运方式";
            head[4] = "订货单位编码";
            head[5] = "订货单位";
            head[6] = "制单人";
            head[7] = "订单类型";
            head[8] = "销售金额";
            head[9] = "出库仓库";
            head[10] = "发票号";
            head[11] = "物流单号";
            head[12] = "销售日期";
            head[13] = "提交日期";
            head[14] = "财务审核日期";
            head[15] = "状态";
            List<Map<String, Object>> list = dao.queryPartSoOrderForExcel(request);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[18];
                    detail[0] = CommonUtils.checkNull(map.get("SO_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("REMARK2"));
                    detail[2] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[3] = CommonUtils.checkNull(map.get("FIX_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[5] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[6] = CommonUtils.checkNull(map.get("CREATE_BY_NAME"));
                    detail[7] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    detail[8] = CommonUtils.checkNull(map.get("AMOUNT")).replace(",", "");
                    detail[9] = CommonUtils.checkNull(map.get("WH_NAME"));
                    detail[10] = CommonUtils.checkNull(map.get("INVOICE_NO"));
                    detail[11] = CommonUtils.checkNull(map.get("LOGISTICS_NO"));
                    detail[12] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[13] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                    detail[14] = CommonUtils.checkNull(map.get("FCAUDIT_DATE"));
                    detail[15] = CommonUtils.checkNull(map.get("STATE"));
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
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件销售订单列表.xls";
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
                    ;
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
    //资金占用

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:占用资金
     */
    public void insertAccount() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        RequestWrapper request = act.getRequest();
        try {
            Long soId = Long.valueOf(CommonUtils.checkNull(request.getAttribute("soId")));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            String sellerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            Double amount = Double.valueOf(request.getAttribute("reCountMoney").toString()); //获取JAVA重新计算的金额
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            po.setRecordId(recordId);
            po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01);
            po.setDealerId(Long.valueOf(sellerId));
            //获取账户余额等
            Map<String, Object> acountMap = dao.getAccount(sellerId, dealerId, "");
            if (null != acountMap) {
                po.setAccountId(Long.valueOf(acountMap.get("ACCOUNT_ID").toString()));
            }
            po.setAmount(amount);
            po.setFunctionName("配件销售预占");
            po.setSourceId(soId);
            po.setSourceCode(soCode);
            po.setOrderId(soId);
            po.setOrderCode(soCode);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            dao.insert(po);
        } catch (Exception e) {
            throw e;
        }
    }

    public void exportDetailExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartSoManageDao dao = PartSoManageDao.getInstance();
            String[] head = new String[12];
            head[0] = "销售单号 ";
            head[1] = "订货单位编码 ";
            head[2] = "订货单位名称";
            head[3] = "配件编码";
            head[4] = "配件名称";
            head[5] = "件号 ";
            head[6] = "单位";
            head[7] = "最小包装量 ";
            head[8] = "销售数量";
            head[9] = "销售单价 ";
            head[10] = "销售金额";
            List<Map<String, Object>> dtlList = dao.queryPartSoDtlForExcel(request);
            List list1 = new ArrayList();
            //for(Map<String, Object> map:dtlList){
            //String orderId = CommonUtils.checkNull(map.get("SO_ID"));
            //String orderCode = CommonUtils.checkNull(map.get("SO_CODE"));
            //List<Map<String ,Object>> dtlList = dao.getSoOrderMDetail(orderId);
            for (Map<String, Object> dtlMap : dtlList) {
                String[] detail = new String[11];
                detail[0] = CommonUtils.checkNull(dtlMap.get("SO_CODE"));
                detail[1] = CommonUtils.checkNull(dtlMap.get("DEALER_CODE"));
                detail[2] = CommonUtils.checkNull(dtlMap.get("DEALER_NAME"));
                detail[3] = CommonUtils.checkNull(dtlMap.get("PART_OLDCODE"));
                detail[4] = CommonUtils.checkNull(dtlMap.get("PART_CNAME"));
                detail[5] = CommonUtils.checkNull(dtlMap.get("PART_CODE"));
                detail[6] = CommonUtils.checkNull(dtlMap.get("UNIT"));
                detail[7] = CommonUtils.checkNull(dtlMap.get("MIN_PACKAGE"));
                detail[8] = CommonUtils.checkNull(dtlMap.get("SALES_QTY"));
                detail[9] = CommonUtils.checkNull(dtlMap.get("BUY_PRICE")).replace("", "");
                detail[10] = CommonUtils.checkNull(dtlMap.get("BUY_AMOUNT")).replace(",", "");
                list1.add(detail);
            }
            //}
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void batchToFinance() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String[] soIdArr = CommonUtils.checkNull(request.getParamValue("cbAr")).split(",");
            if (null != soIdArr) {
                for (int i = 0; i < soIdArr.length; i++) {
                    String soId = soIdArr[i];
                    String dealerId = "";
                    int state = Constant.CAR_FACTORY_ORDER_CHECK_STATE_02;
                    //判断是否为车厂  PartWareHouseDao
                    PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
                    List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
                    if (null != beanList || beanList.size() >= 0) {
                        dealerId = beanList.get(0).getOrgId() + "";
                    }
                    //PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
                    //Map<String,Object> mainMap = partSoManageDao.getSoOrderMain(soId);
                    PartSoManageDao dao = PartSoManageDao.getInstance();
                    TtPartSoMainPO po = new TtPartSoMainPO();
                    if (dealerId.equals(Constant.OEM_ACTIVITIES.toString())) {
                        po.setState(state);
                    } else {
                        po.setState(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05);
                    }
                    po.setSubmitBy(loginUser.getUserId());//modify by yuan 20130529
                    po.setSubmitDate(new Date());//modify by yuan 20130529
                    TtPartSoMainPO oldPo = new TtPartSoMainPO();
                    oldPo.setSoId(Long.valueOf(soId));
                    dao.update(oldPo, po);
                    //request.setAttribute("soCode", CommonUtils.checkNull(mainMap.get("SO_CODE")));
                    //提交时剔除无效销售单（BO转的销售单）
                    dao.delInvalidSo();
                }
            }
            act.setOutData("success", "提交成功!");
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单提交错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void printDirectOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartSoManageDao dao = PartSoManageDao.getInstance();
        try {
            String orgId = "";//ORG ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                orgId = Constant.OEM_ACTIVITIES;
            } else {
                orgId = logonUser.getDealerId();
            }

            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
            DateFormat locDate = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
            DateFormat ymDate = new SimpleDateFormat("yyyy-MM");
            Date date = (Date) mainMap.get("CREATE_DATE");
            act.setOutData("locYM", locDate.format(date) + "");
            List<Map<String, Object>> seqList = dao.getSequenceInfo(orgId, ymDate.format(date));
            if (null != seqList && seqList.size() > 0) {
                act.setOutData("seqNum", seqList.get(0).get("SEQ_NUM"));
            } else {
                act.setOutData("seqNum", "0001");
            }


            act.setOutData("mainMap", mainMap);
            act.setOutData("detailList", detailList);
            act.setForword(directPartOrderPrintUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void getDefault() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String id = CommonUtils.checkNull(request.getParamValue("id"));
            List<Map<String, Object>> addrList = dao.getAddr(id);
            Map<String, Object> map = new HashMap<String, Object>();
            if (addrList == null || addrList.size() <= 0 || addrList.get(0) == null) {
                map.put("ADDR_ID", "");
                map.put("ADDR", "");
                map.put("LINKMAN", "");
                map.put("TEL", "");
                map.put("POST_CODE", "");
                map.put("STATION", "");
            } else {
                map.putAll(addrList.get(0));
            }
            act.setOutData("defaultData", map);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    private void saveQueryCondition() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//订单号
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
        String salesOrderId = CommonUtils.checkNull(request.getParamValue("salesOrderId"));//销售单号
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//出库仓库
        String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName"));//销售单位
        String isBatchso = CommonUtils.checkNull(request.getParamValue("isBatchso"));//是否铺货单
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//销售日期开始
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//销售日期结束
        String RstartDate = CommonUtils.checkNull(request.getParamValue("RstartDate"));//提交财务时间开始
        String RendDate = CommonUtils.checkNull(request.getParamValue("RendDate"));//提交财务时间结束
        String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName"));//订货单位
        String buyerCode = CommonUtils.checkNull(request.getParamValue("buyerCode"));//订货单位CODE
        String finStat = CommonUtils.checkNull(request.getParamValue("finStat"));//财务状态
        String outFlag = CommonUtils.checkNull(request.getParamValue("outFlag"));//出库状态
        String repFlag = CommonUtils.checkNull(request.getParamValue("repFlag"));//提交状态
        String invoiceFlag = CommonUtils.checkNull(request.getParamValue("invoiceFlag"));//开票状态
        String pickOrderFlag = CommonUtils.checkNull(request.getParamValue("pickOrderFlag"));//提货状态
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderCode", orderCode);
        map.put("ORDER_TYPE", orderType);
        map.put("salesOrderId", salesOrderId);
        map.put("whId", whId);
        map.put("sellerName", sellerName);
        map.put("isBatchso", isBatchso);
        map.put("SstartDate", SstartDate);
        map.put("SendDate", SendDate);
        map.put("RstartDate", RstartDate);
        map.put("RendDate", RendDate);
        map.put("buyerName", buyerName);
        map.put("buyerCode", buyerCode);
        map.put("finStat", finStat);
        map.put("outFlag", outFlag);
        map.put("repFlag", repFlag);
        map.put("invoiceFlag", invoiceFlag);
        map.put("pickOrderFlag", pickOrderFlag);
        map.put("salerId", salerId);
        act.getSession().set("condition", map);
    }


    //zhumingwei add 2013-10-23 添加销售快报
    public void detailOrder1() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));
            //String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));

            act.setOutData("SstartDate", CommonUtils.getMonthFirstDay());
            act.setOutData("SendDate", CommonUtils.getDate());

            act.setForword(PART_DLR_ORDER_SO_Detail1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //zhumingwei add 2013-10-23 添加销售快报
    public void queryMakersDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String dealerId = "";
        PartSoManageDao dao = PartSoManageDao.getInstance();
        try {
            String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));
            String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }

            StringBuffer sql = new StringBuffer();

            if (null != SstartDate && !"".equals(SstartDate)) {
                sql.append(" AND TRUNC(SM.CREATE_DATE) >= TO_DATE('" + SstartDate + "', 'yyyy-mm-dd') ");
            }

            if (null != SendDate && !"".equals(SendDate)) {
                sql.append(" AND TRUNC(SM.CREATE_DATE) <= TO_DATE('" + SendDate + "', 'yyyy-mm-dd') ");
            }
            sql.append("AND SM.SELLER_ID = ").append(dealerId);
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.getSOSum(sql.toString(), 13, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}

