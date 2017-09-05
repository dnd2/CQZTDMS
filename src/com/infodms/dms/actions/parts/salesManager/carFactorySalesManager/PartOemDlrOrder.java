package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class PartOemDlrOrder extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    public String[] allowState = {Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + ""};
    String PART_OEM_DLR_ORDER = "/jsp/parts/salesManager/carFactorySalesManager/partOemDlrOrder.jsp";
    String PART_OEM_DLR_ORDER_ADD = "/jsp/parts/salesManager/carFactorySalesManager/partOemDlrOrderAdd.jsp";

    public void partInitControl() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            act.setOutData("now", now);
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "", "销售单已保存");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "", "已提交财务");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "", "财务审核通过");
            stateMap.put(Constant.CAR_FACTORY_PKG_STATE_02 + "", "已装箱");
            stateMap.put(Constant.CAR_FACTORY_TRANS_STATE_01 + "", "已发运");
            act.setOutData("stateMap", stateMap);
            act.setOutData("orderType", orderType);
            //act.setForword(PART_OEM_DLR_ORDER);
            act.setForword(PART_DLR_ORDER_CHECK_QUERY);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void partOemDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            act.setOutData("now", now);
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "", "销售单已保存");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "", "已提交财务");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "", "财务审核通过");
            stateMap.put(Constant.CAR_FACTORY_PKG_STATE_02 + "", "已装箱");
            stateMap.put(Constant.CAR_FACTORY_TRANS_STATE_01 + "", "已发运");
            act.setOutData("stateMap", stateMap);
            act.setOutData("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02);
            act.setForword(PART_OEM_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void partOemEmergencyDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            act.setOutData("now", now);
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "", "销售单已保存");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "", "已提交财务");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "", "财务审核通过");
            stateMap.put(Constant.CAR_FACTORY_PKG_STATE_02 + "", "已装箱");
            stateMap.put(Constant.CAR_FACTORY_TRANS_STATE_01 + "", "已发运");
            act.setOutData("stateMap", stateMap);
            act.setOutData("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01);
            act.setForword(PART_OEM_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void partOemPlanDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            act.setOutData("now", now);
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "", "销售单已保存");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "", "已提交财务");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "", "财务审核通过");
            stateMap.put(Constant.CAR_FACTORY_PKG_STATE_02 + "", "已装箱");
            stateMap.put(Constant.CAR_FACTORY_TRANS_STATE_01 + "", "已发运");
            act.setOutData("stateMap", stateMap);
            act.setOutData("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03);
            act.setForword(PART_OEM_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void partOemDirectDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            act.setOutData("now", now);
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "", "销售单已保存");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "", "已提交财务");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "", "财务审核通过");
            stateMap.put(Constant.CAR_FACTORY_PKG_STATE_02 + "", "已装箱");
            stateMap.put(Constant.CAR_FACTORY_TRANS_STATE_01 + "", "已发运");
            act.setOutData("stateMap", stateMap);
            act.setOutData("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04);
            act.setForword(PART_OEM_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void partOemMarketDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            act.setOutData("now", now);
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "", "销售单已保存");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "", "已提交财务");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "", "财务审核通过");
            stateMap.put(Constant.CAR_FACTORY_PKG_STATE_02 + "", "已装箱");
            stateMap.put(Constant.CAR_FACTORY_TRANS_STATE_01 + "", "已发运");
            act.setOutData("stateMap", stateMap);
            act.setOutData("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
            act.setForword(PART_OEM_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void partOemDiscountDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            act.setOutData("now", now);
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", "已保存");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "", "已提交");
            stateMap.put(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", "已驳回");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "", "销售单已保存");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02 + "", "已提交财务");
            stateMap.put(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "", "财务审核通过");
            stateMap.put(Constant.CAR_FACTORY_PKG_STATE_02 + "", "已装箱");
            stateMap.put(Constant.CAR_FACTORY_TRANS_STATE_01 + "", "已发运");
            act.setOutData("stateMap", stateMap);
            act.setOutData("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
            act.setForword(PART_OEM_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

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
            String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            dataMap.put("name", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = dao.getAccount(dealerId, dealerId, "");
            //折扣率
            String discount = dao.getDiscount(dealerId);
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式

            //品牌
            List<Map<String, Object>> brandList = dao.getBrand();

            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(dealerId);

            dataMap.put("now", now);
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerCode", dealerCode);
            dataMap.put("dealerName", dealerName);
            dataMap.put("discount", discount);
            dataMap.put("orderType", orderType);
            dataMap.put("createBy", loginUser.getName());
            act.setOutData("accountMap", accountMap);
            act.setOutData("transList", list);
            act.setOutData("brandList", brandList);

            act.setOutData("dataMap", dataMap);
            act.setOutData("wareHouseList", wareHouseList);
            act.setForword(PART_OEM_DLR_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_OEM_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "订单单新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_OEM_DLR_ORDER);
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
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        String err = null;
        try {
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
            String parentId = CommonUtils.checkNull(req.getParamValue("SELLER_ID"));
            String orderType = CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"));

            //保存详细订单
            String[] partArr = req.getParamValues("cb");
            Double mainAmount = 0.00D;
            Long orderId = Long.parseLong(SequenceManager.getSequence(""));
            req.setAttribute("orderId", orderId);
            String msg = "";
            req.setAttribute("msg", msg);
            if (null != partArr) {
                for (int i = 0; i < partArr.length; i++) {
                    Double detailMoney = this.saveDetailPo(req, act, partArr[i]);
                    if ("0.0".equals(detailMoney + "")) {
                        err = "配件【" + CommonUtils.checkNull(req.getParamValue("partOldcode_" + partArr[i])) + "】采购价不能为0，请删除后重试!";
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, err);
                        throw e1;
                    }
                    mainAmount = Arith.add(mainAmount, detailMoney);
                }
            }
            String seq = CommonUtils.checkNull(req.getParamValue("seq"));
            if (!"".equals(seq)) {
                List<Map<String, Object>> list = dao.getUploadList(seq);
                for (Map<String, Object> map : list) {
                    Double amount = saveDetailFromUpload(req, map, act);
                    if ("0.0".equals(amount + "")) {
                        err = "配件【" + CommonUtils.checkNull(map.get("PART_OLDCODE")) + "】采购价不能为0，请删除后重试!";
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, err);
                        throw e1;
                    }
                    mainAmount = Arith.add(mainAmount, amount);
                }
                List detailList = dao.queryPartDlrOrderDetail(req.getAttribute("orderId") + "");
                if (null == detailList || detailList.size() <= 0) {
                    err = "模板中的数据和订单不匹配,没有明细可以新增!";
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, err);
                    throw e1;
                }
            }
            msg = CommonUtils.checkNull(req.getAttribute("msg"));
            if (!"".equals(msg)) {
                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, msg);
                throw e1;
            }
            //初始化资金账户
            this.dealerAccoutMng(dealerId, parentId);
            Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, "");
            //向车厂提报非内部领用订单必须有账户//mod by yuan 20131019
            if (parentId.equals(Constant.OEM_ACTIVITIES + "") && !orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09.toString())) {
                if (null == acountMap) {
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!当前余额:0");
                    throw e1;
                } else {
                    if (!this.validateSum(dealerId, parentId, mainAmount + "")) {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!当前余额:" + CommonUtils.checkNull(acountMap.get("ACCOUNT_KY")));
                        throw e1;
                    }
                }
            }/*else{
                //获取账户余额等
	            if(null!=acountMap){
					if(!this.validateSum(dealerId,parentId, mainAmount+"")){
						BizException e1 = new BizException(act,new Exception(),ErrorCodeConstant.SPECIAL_MEG,"余额不足!当前余额:"+CommonUtils.checkNull(acountMap.get("ACCOUNT_KY")));
						throw e1;
					}
	            }
            }*/

            //保存主订单
            mainAmount = this.saveMainPo(req, act, mainAmount);
            //资金占用。内部领用不占用资金//mod by yuan 20131019
            if (!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09.toString()) && CommonUtils.checkNull(req.getParamValue("state")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02.toString())) {
                if (!"".equals(CommonUtils.checkNull(acountMap.get("ACCOUNT_ID")))) {
                    this.insertAccount(dealerId, parentId, mainAmount, Long.valueOf(CommonUtils.checkNull(req.getAttribute("orderId"))), CommonUtils.checkNull(req.getAttribute("orderCode")), logonUser);
                }
            }
            this.saveHistory(req, act, Integer.valueOf(CommonUtils.checkNull(req.getParamValue("state"))));
            POContext.endTxn(true);
            String orderCode = CommonUtils.checkNull(req.getAttribute("orderCode"));
            act.setOutData("orderCode", orderCode);
            act.setOutData("success", "订单：" + orderCode + ",操作成功!");
        } catch (Exception e) {//异常方法
            act.setOutData("error", "操作失败!");
            if (e instanceof BizException) {
                POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                return;
            }
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存订单详细信息出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            try {
                POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "保存订单详细信息出错,请联系管理员!");
            }
        }

    }

    private Double saveDetailFromUpload(RequestWrapper req, Map<String, Object> map, ActionContext act) throws Exception {
        String exStr = "";
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

        try {
            TtPartDlrOrderDtlPO po = new TtPartDlrOrderDtlPO();
            String msg = req.getAttribute("msg") + "";
            Long orderId = null;
            if (null != req.getAttribute("orderId")) {
                orderId = Long.valueOf(CommonUtils.checkNull(req.getAttribute("orderId")));
            } else {
                orderId = Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId")));
            }
            String partId = CommonUtils.checkNull(map.get("PART_ID"));
            if (req.getParamValue("partCname_" + partId) != null) {
                //说明已经存在   直接return 0
                return 0d;
            }
            po.setLineId(Long.parseLong(SequenceManager.getSequence("")));
            po.setOrderId(orderId);
            po.setPartId(Long.valueOf(partId));
            po.setPartCname(CommonUtils.checkNull(map.get("PART_CNAME")));
            po.setPartOldcode(CommonUtils.checkNull(map.get("PART_OLDCODE")));
            po.setPartCode(CommonUtils.checkNull(map.get("PART_CODE")));
            po.setUnit(CommonUtils.checkNull(map.get("UNIT")));

            if (null != map.get("IS_LACK")) {
                po.setIsLack(Integer.valueOf(map.get("IS_LACK") + ""));
            }
            if (null != map.get("IS_REPLACED")) {
                po.setIsReplaced(Integer.valueOf(map.get("IS_REPLACED") + ""));
            }
            if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04.toString()).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {//直发
                po.setIsDirect(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setIsDirect(Constant.PART_BASE_FLAG_NO);
            }
            if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03.toString()).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {//计划
                po.setIsPlan(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setIsPlan(Constant.PART_BASE_FLAG_NO);
            }
            try {
                po.setStockQty(Long.valueOf(map.get("ITEM_QTY") + ""));
            } catch (Exception e) {
                if (null == map.get("ITEM_QTY")) {
                    po.setStockQty(Long.valueOf(0));
                }
                exStr = "服务商当前库存数据出错";
                throw e;
            }
            try {
                po.setMinPackage(Long.valueOf(map.get("MIN_PACKAGE") + ""));
            } catch (Exception e) {
                if (null == map.get("MIN_PACKAGE")) {
                    po.setMinPackage(Long.valueOf(0));
                }
                exStr = "最小库存量数据出错";
                throw e;
            }
            po.setBuyQty(Long.valueOf(map.get("BUYQTY") + ""));
            try {
                po.setBuyPrice(parseDouble(map.get("SALE_PRICE1") + ""));
            } catch (Exception e) {
                exStr = "订购单价出错";
                throw e;
            }
            try {
                po.setBuyAmount(reCountMoney(Double.valueOf(po.getBuyQty()), po.getBuyPrice()));
            } catch (Exception e) {
                exStr = "订购金额出错";
                throw e;
            }
            if (CommonUtils.checkNull(map.get("UPORGSTOCK")).equals("Y") ||
                    CommonUtils.checkNull(map.get("UPORGSTOCK")).equals(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01.toString())) {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01);
            } else {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_02);
            }
            po.setRemark("");
            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setStatus(Constant.STATUS_ENABLE);
          /*  if(po.getBuyQty()<po.getMinPackage()||(po.getMinPackage()%po.getBuyQty())>0){
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件:" + po.getPartOldcode() + "必须为最小包装数的整数倍!");
                throw e1;
            }*/
            if ((CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"))).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                String deftId = dao.getDeftId(po.getPartId() + "", CommonUtils.checkNull(req.getParamValue("brand")));
                po.setDeftId(Long.valueOf(deftId));
            }
            //如果快件
            if (CommonUtils.checkNull(req.getParamValue("TRANS_TYPE")).equals("2")) {
                TtPartDefinePO partPo = new TtPartDefinePO();
                partPo.setPartId(po.getPartId());
                List list = dao.select(partPo);
                if (null != list) {
                    if (list.size() > 0) {
                        partPo = (TtPartDefinePO) list.get(0);
                        if ((partPo.getPackState() + "").equals(Constant.PART_PACK_STATE_02 + "")) {
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件:" + po.getPartCname() + "的包装属性为不可空运，不能使用快件!");
                            throw e1;
                        }
                    }
                }
            }
            String brand = CommonUtils.checkNull(req.getParamValue("brand"));
            req.setAttribute("msg", msg);
            dao.insert(po);
            return po.getBuyAmount();
        } catch (Exception ex) {

            throw ex;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存订单明细并返回金额
     */
    private Double saveDetailPo(RequestWrapper req, ActionContext act, String partId) throws Exception {
        String exStr = "";
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

        try {
            TtPartDlrOrderDtlPO po = new TtPartDlrOrderDtlPO();
            String msg = req.getAttribute("msg") + "";
            Long orderId = null;
            if (null != req.getAttribute("orderId")) {
                orderId = Long.valueOf(CommonUtils.checkNull(req.getAttribute("orderId")));
            } else {
                orderId = Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId")));
            }
            po.setLineId(Long.parseLong(SequenceManager.getSequence("")));
            po.setOrderId(orderId);
            po.setPartId(Long.valueOf(partId));
            po.setPartCname(CommonUtils.checkNull(req.getParamValue("partCname_" + partId)));
            po.setPartOldcode(CommonUtils.checkNull(req.getParamValue("partOldcode_" + partId)));
            po.setPartCode(CommonUtils.checkNull(req.getParamValue("partCode_" + partId)));
            po.setUnit(CommonUtils.checkNull(req.getParamValue("unit_" + partId)));

            if (null != req.getParamValue("isLack_" + partId)) {
                po.setIsLack(Integer.valueOf(req.getParamValue("isLack_" + partId)));
            }
            if (null != req.getParamValue("isReplaced_" + partId)) {
                po.setIsReplaced(Integer.valueOf(req.getParamValue("isReplaced_" + partId)));
            }
            if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04.toString()).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {//直发
                po.setIsDirect(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setIsDirect(Constant.PART_BASE_FLAG_NO);
            }
            if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03.toString()).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {//计划
                po.setIsPlan(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setIsPlan(Constant.PART_BASE_FLAG_NO);
            }
            try {
                po.setStockQty(Long.valueOf(req.getParamValue("stockQty_" + partId)));
            } catch (Exception e) {
                if (null == req.getParamValue("stockQty_" + partId)) {
                    po.setStockQty(Long.valueOf(0));
                } else {
                    exStr = "服务商当前库存数据出错";
                    throw e;
                }
            }
            try {
                po.setMinPackage(Long.valueOf(req.getParamValue("minPackage_" + partId)));
            } catch (Exception e) {
                if (null == req.getParamValue("minPackage_" + partId)) {
                    po.setMinPackage(Long.valueOf(0));
                } else {
                    exStr = "最小库存量数据出错";
                    throw e;
                }
            }

            po.setBuyQty(Long.valueOf(req.getParamValue("buyQty_" + partId)));

            try {
                po.setBuyPrice(parseDouble(req.getParamValue("buyPrice_" + partId)));
            } catch (Exception e) {
                exStr = "订购单价出错";
                throw e;
            }
            try {
                po.setBuyAmount(reCountMoney(Double.valueOf(po.getBuyQty()), po.getBuyPrice()));
            } catch (Exception e) {
                exStr = "订购金额出错";
                throw e;
            }
            if (CommonUtils.checkNull(req.getParamValue("upOrgStock_" + partId)).equals("Y") || !CommonUtils.checkNull(req.getParamValue("upOrgStock_" + partId)).equals("0")) {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01);
            } else {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_02);
            }
            po.setRemark(CommonUtils.checkNull(req.getParamValue("remark_" + partId)));
            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setStatus(Constant.STATUS_ENABLE);

            if ((CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"))).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                String deftId = dao.getDeftId(po.getPartId() + "", CommonUtils.checkNull(req.getParamValue("brand")));
                po.setDeftId(Long.valueOf(deftId));
            }

            String brand = CommonUtils.checkNull(req.getParamValue("brand"));
            //如果是直发的
            if (CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                List<Map<String, Object>> list = dao.getSto(brand, partId);
                if (list != null && list.size() > 0) {
                    Map<String, Object> stoMap = list.get(0);
                    String minPkg = CommonUtils.checkNull(stoMap.get("MIN_PKG"));
                    if (po.getBuyQty() % Long.valueOf(minPkg) != 0) {
                        msg += "</br>配件:" + po.getPartCname() + "必须满足包装数" + minPkg + "的整数倍!";
                    }
                }

            }
            req.setAttribute("msg", msg);
            dao.insert(po);
            return po.getBuyAmount();
        } catch (Exception ex) {

            throw ex;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 校验余额
     */
    public boolean validateSum(String dealerId, String parentId, String sum) throws Exception {
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, "");
        String sumNow = acountMap.get("ACCOUNT_KY").toString();
        try {
            if (parseDouble(sumNow) < parseDouble(sum)) {
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
     * @Description: 保存主订单
     */
    private Double saveMainPo(RequestWrapper req, ActionContext act, Double mainAmount) throws Exception {
        String exStr = "";
        String orderCode = "";
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
            Long orderId = Long.valueOf(req.getAttribute("orderId").toString());
            po.setOrderId(orderId);
            String produceFac = CommonUtils.checkNull(req.getParamValue("produceFac"));
            String accountId = CommonUtils.checkNull(req.getParamValue("accountId"));
            po.setProduceFac(produceFac);//add by yuan 20130806
            po.setOrderId(orderId);
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
            String transpayType = CommonUtils.checkNull(req.getParamValue("transpayType"));
            String isLock = CommonUtils.checkNull(req.getParamValue("isLock"));
            String lockFreight = CommonUtils.checkNull(req.getParamValue("freight"));//页面锁定价格
            //重新获取服务商信息
            TmDealerPO dealerPO = new TmDealerPO();
            dealerPO.setDealerId(Long.valueOf(dealerId));
            dealerPO = (TmDealerPO) dao.select(dealerPO).get(0);
            //获取订单号，订单类型不一样，号也不一样 需要判断
            if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_16, dealerId);
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_06, dealerId);
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_33, dealerId);
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_34, dealerId);
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_35, dealerId);
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_36, dealerId);
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_37, dealerId);
            }
            req.setAttribute("orderCode", orderCode);
            po.setOrderCode(orderCode);
            po.setOrderType(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"))));
            po.setPayType(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("PAY_TYPE"))));
            try {
                po.setDealerId(Long.valueOf(dealerId));
            } catch (Exception ex) {
                exStr = "订货单位出错";
                throw ex;
            }
//            po.setDealerCode(CommonUtils.checkNull(req.getParamValue("dealerCode")));
//            po.setDealerName(CommonUtils.checkNull(req.getParamValue("dealerName")));
            po.setDealerCode(dealerPO.getDealerCode());
            po.setDealerName(dealerPO.getDealerName());
            try {
                po.setSellerId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("SELLER_ID"))));
            } catch (Exception ex) {
                exStr = "销售单位出错";
                throw ex;
            }
            po.setSellerCode(CommonUtils.checkNull(req.getParamValue("SELLER_CODE")));
            po.setSellerName(CommonUtils.checkNull(req.getParamValue("SELLER_NAME")));
            po.setBuyerId(logonUser.getUserId());
            po.setBuyerName(CommonUtils.checkNull(req.getParamValue("buyerName")));
            po.setRcvOrgid(Long.valueOf(CommonUtils.checkNull(req.getParamValue("RCV_ORGID"))));
            //重新获取收货单位信息
            TmDealerPO dealerPO2 = new TmDealerPO();
            dealerPO2.setDealerId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("RCV_ORGID"))));
            dealerPO2 = (TmDealerPO) dao.select(dealerPO2).get(0);
            po.setRcvOrg(CommonUtils.checkNull(req.getParamValue("RCV_ORG")));
            po.setIsAutchk(Constant.PART_BASE_FLAG_NO);
            try {
                po.setAddrId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("ADDR_ID"))));
            } catch (Exception ex) {
                exStr = "接收地址出错";
                throw ex;
            }
            po.setAddr(CommonUtils.checkNull(req.getParamValue("ADDR")));
            po.setReceiver(CommonUtils.checkNull(req.getParamValue("RECEIVER")));
            po.setTel(CommonUtils.checkNull(req.getParamValue("TEL")));
            po.setPostCode(CommonUtils.checkNull(req.getParamValue("POST_CODE")));
            po.setStation(CommonUtils.checkNull(req.getParamValue("STATION")));
            po.setTransType(CommonUtils.checkNull(req.getParamValue("TRANS_TYPE")));
            if (!"".equals(accountId) && null != accountId) {
                po.setAccountId(Long.valueOf(accountId));
            }
            if (null != req.getParamValue("accountSum") && !"null".equals(req.getParamValue("accountSum"))) {
                Double accountSum = parseDouble(req.getParamValue("accountSum"));
                po.setAccountSum(accountSum);
            }
            if (null != req.getParamValue("accountKy") && !"null".equals(req.getParamValue("accountKy"))) {
                po.setAccountKy(parseDouble(req.getParamValue("accountKy")));
            }
            if (null != req.getParamValue("accountDj") && !"null".equals(req.getParamValue("accountDj"))) {
                po.setAccountDj(parseDouble(req.getParamValue("accountDj")));
            }
            try {
                po.setOrderAmount(mainAmount);
            } catch (Exception ex) {
                exStr = "订购总金额出错";
                throw ex;
            }
            po.setDiscount(Double.valueOf(CommonUtils.checkNull(req.getParamValue("DISCOUNT"))));
            //异地发货备注
            if (!CommonUtils.checkNull(req.getParamValue("dealerId")).equals(CommonUtils.checkNull(req.getParamValue("RCV_ORGID")))) {
                po.setRemark("异地发货;" + CommonUtils.checkNull(req.getParamValue("textarea")));
            } else {
                po.setRemark(CommonUtils.checkNull(req.getParamValue("textarea")));
            }
            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setState(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("state"))));
            if (CommonUtils.checkNull(req.getParamValue("state")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02.toString())) {
                po.setSubmitDate(new Date());
                po.setCreateBy(logonUser.getUserId());
            }
            po.setVer(1);

            if ((po.getOrderType() + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "")) {
                po.setDiscountStatus(Constant.IF_TYPE_YES + "");
            }
            //重新计算运费运费
            Double freight = 0D;
            //主机厂新增且不免运费时计算运费
            if ((po.getSellerId() + "").equals(Constant.OEM_ACTIVITIES) && !transpayType.equals(Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01.toString())) {
                freight = Double.valueOf((dao.getFreight(po.getDealerId() + "", po.getOrderType() + "", po.getOrderAmount() + "")).replaceAll(",", ""));
            }
            if (po.getTransType().equals("3")) {
                freight = 0D;
            }
            //2状态服务商免运费 ADD BY YUAN 20131225 START
            if (dao.isTransFree(po.getDealerId().toString()).equals("2")) {
                freight = 0D;
            }
            //END
            //运费锁定时不计算运费
            if (isLock.equals("1")) {
                po.setLockFreight(1);
                freight = Double.valueOf(lockFreight.replace(",", ""));
            }
            po.setOrderAmount(Arith.add(po.getOrderAmount(), freight));

            po.setFreight(freight);
            if (freight == 0) {
                po.setIsTransfree(Constant.IF_TYPE_YES);
            } else {
                po.setIsTransfree(Constant.IF_TYPE_NO);
            }

            String userDealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                userDealerId = beanList.get(0).getOrgId() + "";

            }
            if (userDealerId.equals(Constant.OEM_ACTIVITIES + "")) {
                po.setOemFlag(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setOemFlag(Constant.PART_BASE_FLAG_NO);
            }
            po.setCreateOrg(Long.valueOf(userDealerId));
            dao.insert(po);
            req.setAttribute("orderCode", po.getOrderCode());
            return po.getOrderAmount();
        } catch (Exception ex) {
            BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, exStr + ",请联系管理员!");
            throw e1;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件销售查询
     */
    public void queryOemPartDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryOemPartDlrOrder(request, curPage, Constant.PAGE_SIZE);
            List<Map<String, Object>> list = ps.getRecords();
            if (null != list && list.size() > 0) {
                for (Map<String, Object> map : list) {
                    String state = CommonUtils.checkNull(map.get("STATE"));
                    if (state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_03 + "")) {
                        //如果已经审核状态就开始显示销售单状态
                        String orderId = CommonUtils.checkNull(map.get("ORDER_ID"));
                        Map<String, Object> soMainMap = dao.getSoMainMap(orderId);
                        //如果不是已经保存就替换成销售单状态
                        if (!CommonUtils.checkNull(soMainMap.get("STATE")).equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01 + "")) {
                            map.put("STATE", CommonUtils.checkNull(soMainMap.get("STATE")));
                        }
                    }
                }
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);

                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:占用资金
     */
    public void insertAccount(String dealerId, String parentId, Double amount, Long orderId, String orderCode, AclUserBean loginUser) throws Exception {
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            po.setRecordId(recordId);
            po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01);
            po.setDealerId(Long.valueOf(dealerId));
            //获取账户余额等
            Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, "");
            if (null != acountMap) {
                po.setAccountId(Long.valueOf(acountMap.get("ACCOUNT_ID").toString()));
            }
            po.setAmount(amount);
            po.setFunctionName("配件提报预占");
            po.setSourceId(Long.valueOf(orderId));
            po.setSourceCode(orderCode);
            po.setOrderId(orderId);
            po.setOrderCode(orderCode);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            dao.insert(po);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存日志
     */
    public void saveHistory(RequestWrapper req, ActionContext act, int status) throws Exception {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
            Long optId = Long.parseLong(SequenceManager.getSequence(""));
            po.setBussinessId(CommonUtils.checkNull(req.getAttribute("orderCode")));
            po.setOptId(optId);
            po.setOptBy(logonUser.getUserId());
            po.setOptDate(new Date());
            po.setOptType(Constant.PART_OPERATION_TYPE_01);
            po.setWhat("配件订单提报");
            po.setOptName(logonUser.getName());
            po.setStatus(status);
            po.setOrderId(Long.valueOf(CommonUtils.checkNull(req.getAttribute("orderId"))));
            dao.insert(po);
        } catch (Exception ex) {
            throw ex;
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

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 重新计算金额
     */
    public Double reCountMoney(Double buyQty, Double price) {
        Double money = Arith.mul(Double.valueOf(buyQty), Double.valueOf(price));
        return money;
    }

    public void getUploadExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String seq = CommonUtils.checkNull(request.getParamValue("seq"));
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            String dealerName = "";
            String dealerCode = "";
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
                dealerName = beanList.get(0).getOrgName();
                dealerCode = beanList.get(0).getOrgCode();
            }
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            List<Map<String, Object>> list = dao.getUploadList(seq);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("discount", CommonUtils.checkNull(request.getParamValue("DISCOUNT")));
            map.put("SELLER_ID", CommonUtils.checkNull(request.getParamValue("dealerId")));
            map.put("SELLER_NAME", CommonUtils.checkNull(request.getParamValue("dealerName")));
            map.put("SELLER_CODE", CommonUtils.checkNull(request.getParamValue("dealerCode")));
            map.put("createBy", loginUser.getName());

            map.put("dealerName", CommonUtils.checkNull(request.getParamValue("SELLER_NAME")));
            map.put("dealerCode", CommonUtils.checkNull(request.getParamValue("SELLER_CODE")));
            map.put("dealerId", CommonUtils.checkNull(request.getParamValue("SELLER_ID")));
            map.put("PAY_TYPE", CommonUtils.checkNull(request.getParamValue("PAY_TYPE")));
            map.put("produceFac", CommonUtils.checkNull(request.getParamValue("produceFac")));
            map.put("now", now);
            map.put("textarea", CommonUtils.checkNull(request.getParamValue("textarea")));
            map.put("ORDER_TYPE", CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")));
            map.put("accountId", CommonUtils.checkNull(request.getParamValue("accountId")));

            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            defaultValueMap.put("defaultRcvOrg", CommonUtils.checkNull(request.getParamValue("RCV_ORG")));
            defaultValueMap.put("RCV_CODE", CommonUtils.checkNull(request.getParamValue("RCV_CODE")));
            defaultValueMap.put("defaultRcvOrgid", CommonUtils.checkNull(request.getParamValue("RCV_ORGID")));
            defaultValueMap.put("defaultStation", CommonUtils.checkNull(request.getParamValue("STATION")));
            defaultValueMap.put("defaultLinkman", CommonUtils.checkNull(request.getParamValue("RECEIVER")));
            defaultValueMap.put("defaultTel", CommonUtils.checkNull(request.getParamValue("TEL")));
            defaultValueMap.put("defaultPostCode", CommonUtils.checkNull(request.getParamValue("POST_CODE")));
            defaultValueMap.put("defaultAddrId", CommonUtils.checkNull(request.getParamValue("ADDR_ID")));
            defaultValueMap.put("defaultAddr", CommonUtils.checkNull(request.getParamValue("ADDR")));

            List transList = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            if (request.getParamValue("accountSum") != null) {
                defaultValueMap.put("flag", "upload");
                defaultValueMap.put("accountSum", CommonUtils.checkNull(request.getParamValue("accountSum")));
                defaultValueMap.put("accountKy", CommonUtils.checkNull(request.getParamValue("accountKy")));
                defaultValueMap.put("accountDj", CommonUtils.checkNull(request.getParamValue("accountDj")));
            }

            act.setOutData("defaultValueMap", defaultValueMap);
            act.setOutData("orderType", CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")));
            act.setOutData("transType", CommonUtils.checkNull(request.getParamValue("TRANS_TYPE")));
            act.setOutData("dataMap", map);
            act.setOutData("transList", transList);
            act.setOutData("detailList", list);
            act.setForword(PART_OEM_DLR_ORDER_ADD);
        } catch (Exception e) {
            e.printStackTrace();
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, act.getOutData("error") == null ? "文件读取错误" : act.getOutData("error").toString());
            }
            act.setOutData("false", true);
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_OEM_DLR_ORDER_ADD);
        }
    }

    /**
     * @param dealerID
     * @param sellerId
     */
    public void dealerAccoutMng(String dealerID, String sellerId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        TtPartAccountDefinePO accountDefinePO = new TtPartAccountDefinePO();
        accountDefinePO.setChildorgId(Long.valueOf(dealerID));
        accountDefinePO.setParentorgId(Long.valueOf(sellerId));
        if (dao.select(accountDefinePO).size() == 0) {
            Long accountId = Long.parseLong(SequenceManager.getSequence(""));
            TtPartAccountDefinePO definePO = new TtPartAccountDefinePO();
            definePO.setAccountId(accountId);
            definePO.setChildorgId(Long.valueOf(dealerID));
            definePO.setParentorgId(Long.valueOf(sellerId));
            definePO.setAccountKind(Constant.FIXCODE_CURRENCY_01);
            definePO.setAccountSum(0.00d);
            definePO.setCreateDate(new Date());
            definePO.setCreateBy(loginUser.getUserId());
            definePO.setState(Constant.STATUS_ENABLE);
            definePO.setStatus(1);
            dao.insert(definePO);

        }
    }

    /**
     * @param dealerID
     * @param sellerId
     */
    public void dealerAccoutReordMng(Long dealerId, Long sellerId, Double aMount, Long orderId, String orderCode) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        TtPartAccountDefinePO accountDefinePO = new TtPartAccountDefinePO();
        accountDefinePO.setChildorgId(dealerId);
        accountDefinePO.setParentorgId(sellerId);

        TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
        recordPO.setSourceId(orderId);

        if (dao.select(recordPO).size() == 0) {
            Long accountId = Long.parseLong(SequenceManager.getSequence(""));
            TtPartAccountRecordPO recordPO1 = new TtPartAccountRecordPO();
            recordPO1.setRecordId(Long.valueOf(SequenceManager.getSequence("")));
            recordPO1.setAccountId(accountId);
            recordPO1.setDealerId(dealerId);
            recordPO1.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01);
            recordPO1.setFunctionName("配件提报预占");
            recordPO1.setSourceId(orderId);
            recordPO1.setSourceCode(orderCode);
            recordPO1.setAmount(aMount);
            recordPO1.setCreateDate(new Date());
            recordPO1.setCreateBy(loginUser.getUserId());
            recordPO1.setState(Constant.STATUS_ENABLE);
            recordPO1.setStatus(1);
            dao.insert(recordPO1);
        } else {
            TtPartAccountRecordPO recordPO1 = new TtPartAccountRecordPO();
            //recordPO1.setSourceCode(orderCode);
            recordPO1.setSourceId(orderId);

            TtPartAccountRecordPO recordPO11 = new TtPartAccountRecordPO();
            recordPO11.setAmount(aMount);

            dao.update(recordPO1, recordPO11);
        }
    }
}
