package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.parts.baseManager.PartBaseQueryDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
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
import com.infoservice.po3.core.context.POContext;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单提报
 * @author 
 * @version 
 * @see 
 * @since 
 * @deprecated
 */
public class PartDlrOrder extends BaseImport implements PTConstants {
	
    public Logger logger = Logger.getLogger(PartDlrOrder.class);
    
    PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
    
    public String[] allowState = {Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01 + "", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05 + "", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + ""};
    /**
     * 委托订单查询
     */
    String PART_DIRECT_DLR_ORDER = "/jsp/parts/salesManager/carFactorySalesManager/partDirectDlrOrder.jsp";
    /**
     * 委托订单新增
     */
    String PART_DIRECT_DLR_ORDER_ADD = "/jsp/parts/salesManager/carFactorySalesManager/partDirectDlrOrderAdd.jsp";
    /**
     * 委托订单修改
     */
    String PART_DIRECT_DLR_ORDER_MOD = "/jsp/parts/salesManager/carFactorySalesManager/partDirectDlrOrderMod.jsp";
    /**
     * 销售采购订单
     */
    String PART_SALE_PUR_ORDER = "/jsp/parts/salesManager/carFactorySalesManager/partSalePurOrder.jsp";
    /**
     * 销售采购订单-新增
     */
    String PART_SALE_PUR_ORDER_ADD = "/jsp/parts/salesManager/carFactorySalesManager/partSalePurOrderAdd.jsp";
    /**
     * 销售采购订单-修改
     */
    String PART_SALE_PUR_ORDER_MOD = "/jsp/parts/salesManager/carFactorySalesManager/partSalePurOrderMod.jsp";
    /**
     * 促销订单-初始化查询
     */
    String PART_DISCOUNT_DLR_ORDER = "/jsp/parts/salesManager/carFactorySalesManager/partDiscountDlrOrder.jsp";
    /**
     * 促销订单-修改
     */
    String PART_DISCOUNT_DLR_ORDER_MOD = "/jsp/parts/salesManager/carFactorySalesManager/partDiscountDlrOrderMod.jsp";
    /**
     * 紧急订单-初始化查询
     */
    String PART_EMERGENCY_DLR_ORDER = "/jsp/parts/salesManager/carFactorySalesManager/partEmergencyDlrOrder.jsp";
    
    /**
     * 紧急订单修改
     */
    String PART_EMERGENCY_DLR_ORDER_MOD = "/jsp/parts/salesManager/carFactorySalesManager/partEmergencyDlrOrderMod.jsp";
    
    String PART_PLAN_DLR_ORDER = "/jsp/parts/salesManager/carFactorySalesManager/partPlanDlrOrder.jsp";
    
    String PART_PLAN_DLR_ORDER_MOD = "/jsp/parts/salesManager/carFactorySalesManager/partPlanDlrOrderMod.jsp";
    
    String PART_MARKET_DLR_ORDER = "/jsp/parts/salesManager/carFactorySalesManager/partMarketDlrOrder.jsp";
    
    String PART_MARKET_DLR_ORDER_MOD = "/jsp/parts/salesManager/carFactorySalesManager/partMarketDlrOrderMod.jsp";
    
    String UPLOADFILE = "/jsp/parts/salesManager/carFactorySalesManager/uploadFile.jsp";
    
    String INPUT_ERROR_URL = "/jsp/parts/salesManager/carFactorySalesManager/partUploadInputError.jsp";

    /**
     * @param : @param orgAmtweb
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件采购订单列表.xls";
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
     * 常规订单提报--初始化
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 
     */
    public void partDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
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
            act.setForword(PART_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 促销订单提报-配件销售提报初始化
     */
    public void partDiscountDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
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
            act.setForword(PART_DISCOUNT_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 委托订单提报-委托订单提报初始化
     */
    public void partDirectDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
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
            act.setForword(PART_DIRECT_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 采购促销订单提报--初始化
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 
     */
    public void partSalePurOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
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
            act.setForword(PART_SALE_PUR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
    
    

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 计划订单-配件销售计划提报初始化
     */
    public void partPlanDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
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
            act.setForword(PART_PLAN_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 广宣订单提报-初始化
     */
    public void partMarketDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
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
            act.setForword(PART_MARKET_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 紧急订单提报-初始化
     */
    public void partEmergencyDlrOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
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
            act.setForword(PART_EMERGENCY_DLR_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件订单查询
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件销售查询
     */
    public void queryPartDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = dao.queryPartDlrOrder(request, curPage, Constant.PAGE_SIZE);
            
            //add by yuan 20130921 start   看不懂
           
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
            //end
            act.setOutData("ps", ps);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 常规订单-新增
     */
    public void addOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(loginUser.getUserId());
            //获取单位ID
            String dealerId = "";
            String dealerName = "";
            String dealerCode = "";
            List<OrgBean> beanList = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
                dealerName = beanList.get(0).getOrgName();
                dealerCode = beanList.get(0).getOrgCode();
            }
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            //折扣率         
            String discount = dao.getDiscount(dealerId);
            dataMap.put("buyerName", loginUser.getName());
            //单位信息
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerName", dealerName);
            dataMap.put("dealerCode", dealerCode);

            //设置默认销售单位 (只考虑有一个上级销售单位)
            String SELLER_NAME = "";
            String SELLER_CODE = "";
            String SELLER_ID = "";
            String accountId = "";
            String accountSum = "";
            String accountKy = "";
            String accountDj = "";

            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, "1", "", "", "", Constant.PAGE_SIZE_MAX, 1);
            List<Map<String, Object>> selList = null;
            if (null != ps && ps.getRecords().size() > 0) {
                selList = ps.getRecords();
                SELLER_NAME = selList.get(0).get("PARENTORG_NAME").toString();
                SELLER_CODE = selList.get(0).get("PARENTORG_CODE").toString();
                SELLER_ID = selList.get(0).get("PARENTORG_ID").toString();
                accountId = selList.get(0).get("ACCOUNT_ID").toString();
                accountSum = selList.get(0).get("ACCOUNT_SUM").toString();
                accountKy = selList.get(0).get("ACCOUNT_KY").toString();
                accountDj = selList.get(0).get("ACCOUNT_DJ").toString();
            }

            dataMap.put("SELLER_NAME", SELLER_NAME);
            dataMap.put("SELLER_CODE", SELLER_CODE);
            dataMap.put("SELLER_ID", SELLER_ID);
            dataMap.put("accountId", accountId);
            dataMap.put("accountSum", accountSum);
            dataMap.put("accountKy", accountKy);
            dataMap.put("accountDj", accountDj);

            //设置默认值
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            String defaultStation = "";
            String defaultLinkman = "";
            String defaultTel = "";
            String defaultPostCode = "";
            String defaultAddrId = "";
            String defaultAddr = "";
            defaultValueMap.put("defaultRcvOrgid", dealerId);
            defaultValueMap.put("defaultRcvOrg", dealerName);
            //默认地址
            PageResult<Map<String, Object>> addrPs = dao.getSalesRelation(dealerId, "3", "", "", "", 10, 1);
            if (addrPs != null) {
                List<Map<String, Object>> addrList = addrPs.getRecords();
                if (addrList != null && addrList.size() > 0 && addrList.get(0) != null) {
                    defaultStation = CommonUtils.checkNull(addrList.get(0).get("STATION"));
                    defaultLinkman = CommonUtils.checkNull(addrList.get(0).get("LINKMAN"));
                    defaultTel = CommonUtils.checkNull(addrList.get(0).get("TEL"));
                    defaultPostCode = CommonUtils.checkNull(addrList.get(0).get("POST_CODE"));
                    defaultAddrId = CommonUtils.checkNull(addrList.get(0).get("ADDR_ID"));
                    defaultAddr = CommonUtils.checkNull(addrList.get(0).get("ADDR"));
                }
            }
            defaultValueMap.put("defaultStation", defaultStation);
            defaultValueMap.put("defaultLinkman", defaultLinkman);
            defaultValueMap.put("defaultTel", defaultTel);
            defaultValueMap.put("defaultPostCode", defaultPostCode);
            defaultValueMap.put("defaultAddrId", defaultAddrId);
            defaultValueMap.put("defaultAddr", defaultAddr);

            request.setAttribute("defaultValueMap", defaultValueMap);
            dataMap.put("now", now);
            dataMap.put("discount", discount);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("transList", list);

            request.setAttribute("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02);
            act.setForword(PART_DLR_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 销售采购订单-新增
     */
    public void addSalePurOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(loginUser.getUserId());
            //获取单位ID
            String dealerId = "";
            String dealerName = "";
            String dealerCode = "";
            List<OrgBean> beanList = PartWareHouseDao.getInstance().getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
                dealerName = beanList.get(0).getOrgName();
                dealerCode = beanList.get(0).getOrgCode();
            }
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            //折扣率         
            String discount = dao.getDiscount(dealerId);
            dataMap.put("buyerName", loginUser.getName());
            //单位信息
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerName", dealerName);
            dataMap.put("dealerCode", dealerCode);

            //设置默认销售单位 (只考虑有一个上级销售单位)
            String SELLER_NAME = "";
            String SELLER_CODE = "";
            String SELLER_ID = "";
            String accountId = "";
            String accountSum = "";
            String accountKy = "";
            String accountDj = "";

            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, "1", "", "", "", Constant.PAGE_SIZE_MAX, 1);
            List<Map<String, Object>> selList = null;
            if (null != ps && ps.getRecords().size() > 0) {
                selList = ps.getRecords();
                SELLER_NAME = selList.get(0).get("PARENTORG_NAME").toString();
                SELLER_CODE = selList.get(0).get("PARENTORG_CODE").toString();
                SELLER_ID = selList.get(0).get("PARENTORG_ID").toString();
                accountId = selList.get(0).get("ACCOUNT_ID").toString();
                accountSum = selList.get(0).get("ACCOUNT_SUM").toString();
                accountKy = selList.get(0).get("ACCOUNT_KY").toString();
                accountDj = selList.get(0).get("ACCOUNT_DJ").toString();
            }

            dataMap.put("SELLER_NAME", SELLER_NAME);
            dataMap.put("SELLER_CODE", SELLER_CODE);
            dataMap.put("SELLER_ID", SELLER_ID);
            dataMap.put("accountId", accountId);
            dataMap.put("accountSum", accountSum);
            dataMap.put("accountKy", accountKy);
            dataMap.put("accountDj", accountDj);

            //设置默认值
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            String defaultStation = "";
            String defaultLinkman = "";
            String defaultTel = "";
            String defaultPostCode = "";
            String defaultAddrId = "";
            String defaultAddr = "";
            defaultValueMap.put("defaultRcvOrgid", dealerId);
            defaultValueMap.put("defaultRcvOrg", dealerName);
            //默认地址
            PageResult<Map<String, Object>> addrPs = dao.getSalesRelation(dealerId, "3", "", "", "", 10, 1);
            if (addrPs != null) {
                List<Map<String, Object>> addrList = addrPs.getRecords();
                if (addrList != null && addrList.size() > 0 && addrList.get(0) != null) {
                    defaultStation = CommonUtils.checkNull(addrList.get(0).get("STATION"));
                    defaultLinkman = CommonUtils.checkNull(addrList.get(0).get("LINKMAN"));
                    defaultTel = CommonUtils.checkNull(addrList.get(0).get("TEL"));
                    defaultPostCode = CommonUtils.checkNull(addrList.get(0).get("POST_CODE"));
                    defaultAddrId = CommonUtils.checkNull(addrList.get(0).get("ADDR_ID"));
                    defaultAddr = CommonUtils.checkNull(addrList.get(0).get("ADDR"));
                }
            }
            defaultValueMap.put("defaultStation", defaultStation);
            defaultValueMap.put("defaultLinkman", defaultLinkman);
            defaultValueMap.put("defaultTel", defaultTel);
            defaultValueMap.put("defaultPostCode", defaultPostCode);
            defaultValueMap.put("defaultAddrId", defaultAddrId);
            defaultValueMap.put("defaultAddr", defaultAddr);

            request.setAttribute("defaultValueMap", defaultValueMap);
            dataMap.put("now", now);
            dataMap.put("discount", discount);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("transList", list);

            request.setAttribute("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12);
            act.setForword(PART_SALE_PUR_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }
    }
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 跳转到增加页面
     */
    public void addPlanOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(loginUser.getUserId());
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }

            //获取订货单位和CODE
            Map<String, Object> dealerMap = dao.getDealerName(dealerId);
            String dealerName = "";
            String dealerCode = "";
            if (null != dealerMap) {
                dealerName = dealerMap.get("DEALER_NAME").toString();
                dealerCode = dealerMap.get("DEALER_CODE").toString();
            }
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            //折扣率         
            String discount = dao.getDiscount(dealerId);
            dataMap.put("buyerName", loginUser.getName());
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerName", dealerName);
            dataMap.put("dealerCode", dealerCode);
            //设置默认销售单位 (只考虑有一个上级销售单位)
            String SELLER_NAME = "";
            String SELLER_CODE = "";
            String SELLER_ID = "";
            String accountId = "";
            String accountSum = "";
            String accountKy = "";
            String accountDj = "";

            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, "1", "", "", "", Constant.PAGE_SIZE_MAX, 1);
            List<Map<String, Object>> selList = null;
            if (null != ps && ps.getRecords().size() > 0) {
                selList = ps.getRecords();
                SELLER_NAME = selList.get(0).get("PARENTORG_NAME").toString();
                SELLER_CODE = selList.get(0).get("PARENTORG_CODE").toString();
                SELLER_ID = selList.get(0).get("PARENTORG_ID").toString();
                accountId = selList.get(0).get("ACCOUNT_ID").toString();
                accountSum = selList.get(0).get("ACCOUNT_SUM").toString();
                accountKy = selList.get(0).get("ACCOUNT_KY").toString();
                accountDj = selList.get(0).get("ACCOUNT_DJ").toString();
            }

            dataMap.put("SELLER_NAME", SELLER_NAME);
            dataMap.put("SELLER_CODE", SELLER_CODE);
            dataMap.put("SELLER_ID", SELLER_ID);
            dataMap.put("accountId", accountId);
            dataMap.put("accountSum", accountSum);
            dataMap.put("accountKy", accountKy);
            dataMap.put("accountDj", accountDj);
            //设置默认值
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            String defaultStation = "";
            String defaultLinkman = "";
            String defaultTel = "";
            String defaultPostCode = "";
            String defaultAddrId = "";
            String defaultAddr = "";
            defaultValueMap.put("defaultRcvOrgid", dealerId);
            defaultValueMap.put("defaultRcvOrg", dealerName);

            //默认地址
            PageResult<Map<String, Object>> addrPs = dao.getSalesRelation(dealerId, "3", "", "", "", 10, 1);
            if (addrPs != null) {
                List<Map<String, Object>> addrList = addrPs.getRecords();
                if (addrList != null && addrList.size() > 0 && addrList.get(0) != null) {
                    defaultStation = CommonUtils.checkNull(addrList.get(0).get("STATION"));
                    defaultLinkman = CommonUtils.checkNull(addrList.get(0).get("LINKMAN"));
                    defaultTel = CommonUtils.checkNull(addrList.get(0).get("TEL"));
                    defaultPostCode = CommonUtils.checkNull(addrList.get(0).get("POST_CODE"));
                    defaultAddrId = CommonUtils.checkNull(addrList.get(0).get("ADDR_ID"));
                    defaultAddr = CommonUtils.checkNull(addrList.get(0).get("ADDR"));
                }
            }
            defaultValueMap.put("defaultStation", defaultStation);
            defaultValueMap.put("defaultLinkman", defaultLinkman);
            defaultValueMap.put("defaultTel", defaultTel);
            defaultValueMap.put("defaultPostCode", defaultPostCode);
            defaultValueMap.put("defaultAddrId", defaultAddrId);
            defaultValueMap.put("defaultAddr", defaultAddr);

            request.setAttribute("defaultValueMap", defaultValueMap);
            dataMap.put("now", now);
            dataMap.put("discount", discount);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("transList", list);
            request.setAttribute("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03);
            act.setForword(PART_DLR_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_PLAN_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_PLAN_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 紧急订单提报-跳转到增加页面
     */
    public void addEmergencyOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(loginUser.getUserId());
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }

            //获取订货单位和CODE
            Map<String, Object> dealerMap = dao.getDealerName(dealerId);
            String dealerName = "";
            String dealerCode = "";
            if (null != dealerMap) {
                dealerName = dealerMap.get("DEALER_NAME").toString();
                dealerCode = dealerMap.get("DEALER_CODE").toString();
            }
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            //折扣率         
            String discount = dao.getDiscount(dealerId);
            dataMap.put("buyerName", loginUser.getName());
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerName", dealerName);
            dataMap.put("dealerCode", dealerCode);

            //设置默认销售单位 (只考虑有一个上级销售单位)
            String SELLER_NAME = "";
            String SELLER_CODE = "";
            String SELLER_ID = "";
            String accountId = "";
            String accountSum = "";
            String accountKy = "";
            String accountDj = "";

            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, "1", "", "", "", Constant.PAGE_SIZE_MAX, 1);
            List<Map<String, Object>> selList = null;
            if (null != ps && ps.getRecords().size() > 0) {
                selList = ps.getRecords();
                SELLER_NAME = selList.get(0).get("PARENTORG_NAME").toString();
                SELLER_CODE = selList.get(0).get("PARENTORG_CODE").toString();
                SELLER_ID = selList.get(0).get("PARENTORG_ID").toString();
                accountId = selList.get(0).get("ACCOUNT_ID").toString();
                accountSum = selList.get(0).get("ACCOUNT_SUM").toString();
                accountKy = selList.get(0).get("ACCOUNT_KY").toString();
                accountDj = selList.get(0).get("ACCOUNT_DJ").toString();
            }

            dataMap.put("SELLER_NAME", SELLER_NAME);
            dataMap.put("SELLER_CODE", SELLER_CODE);
            dataMap.put("SELLER_ID", SELLER_ID);
            dataMap.put("accountId", accountId);
            dataMap.put("accountSum", accountSum);
            dataMap.put("accountKy", accountKy);
            dataMap.put("accountDj", accountDj);

            //设置默认值
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            String defaultStation = "";
            String defaultLinkman = "";
            String defaultTel = "";
            String defaultPostCode = "";
            String defaultAddrId = "";
            String defaultAddr = "";
            defaultValueMap.put("defaultRcvOrgid", dealerId);
            defaultValueMap.put("defaultRcvOrg", dealerName);

            //默认地址
            PageResult<Map<String, Object>> addrPs = dao.getSalesRelation(dealerId, "3", "", "", "", 10, 1);
            if (addrPs != null) {
                List<Map<String, Object>> addrList = addrPs.getRecords();
                if (addrList != null && addrList.size() > 0 && addrList.get(0) != null) {
                    defaultStation = CommonUtils.checkNull(addrList.get(0).get("STATION"));
                    defaultLinkman = CommonUtils.checkNull(addrList.get(0).get("LINKMAN"));
                    defaultTel = CommonUtils.checkNull(addrList.get(0).get("TEL"));
                    defaultPostCode = CommonUtils.checkNull(addrList.get(0).get("POST_CODE"));
                    defaultAddrId = CommonUtils.checkNull(addrList.get(0).get("ADDR_ID"));
                    defaultAddr = CommonUtils.checkNull(addrList.get(0).get("ADDR"));
                }
            }
            defaultValueMap.put("defaultStation", defaultStation);
            defaultValueMap.put("defaultLinkman", defaultLinkman);
            defaultValueMap.put("defaultTel", defaultTel);
            defaultValueMap.put("defaultPostCode", defaultPostCode);
            defaultValueMap.put("defaultAddrId", defaultAddrId);
            defaultValueMap.put("defaultAddr", defaultAddr);

            request.setAttribute("defaultValueMap", defaultValueMap);
            dataMap.put("now", now);
            dataMap.put("discount", discount);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("transList", list);
            request.setAttribute("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01);
            act.setForword(PART_DLR_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_PLAN_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_PLAN_DLR_ORDER);
        }
    }

    /**
     * 委托订单-新增
     */
    public void addDirectOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(loginUser.getUserId());
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }

            //获取订货单位和CODE
            Map<String, Object> dealerMap = dao.getDealerName(dealerId);
            String dealerName = "";
            String dealerCode = "";
            if (null != dealerMap) {
                dealerName = dealerMap.get("DEALER_NAME").toString();
                dealerCode = dealerMap.get("DEALER_CODE").toString();
            }
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            //折扣率         
            String discount = dao.getDiscount(dealerId);
            dataMap.put("buyerName", loginUser.getName());
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerName", dealerName);
            dataMap.put("dealerCode", dealerCode);
            //设置默认销售单位 (只考虑有一个上级销售单位)
            String SELLER_NAME = "";
            String SELLER_CODE = "";
            String SELLER_ID = "";
            String accountId = "";
            String accountSum = "";
            String accountKy = "";
            String accountDj = "";

            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, "1", "", "", Constant.PART_ACCOUNT_PURPOSE_TYPE_02 + "", Constant.PAGE_SIZE_MAX, 1);
            List<Map<String, Object>> selList = null;
            if (null != ps && ps.getRecords().size() > 0) {
                selList = ps.getRecords();
                SELLER_NAME = selList.get(0).get("PARENTORG_NAME").toString();
                SELLER_CODE = selList.get(0).get("PARENTORG_CODE").toString();
                SELLER_ID = selList.get(0).get("PARENTORG_ID").toString();
                accountId = selList.get(0).get("ACCOUNT_ID").toString();
                accountSum = selList.get(0).get("ACCOUNT_SUM").toString();
                accountKy = selList.get(0).get("ACCOUNT_KY").toString();
                accountDj = selList.get(0).get("ACCOUNT_DJ").toString();
            }

            dataMap.put("SELLER_NAME", SELLER_NAME);
            dataMap.put("SELLER_CODE", SELLER_CODE);
            dataMap.put("SELLER_ID", SELLER_ID);
            dataMap.put("accountId", accountId);
            dataMap.put("accountSum", accountSum);
            dataMap.put("accountKy", accountKy);
            dataMap.put("accountDj", accountDj);

            dataMap.put("now", now);
            dataMap.put("discount", discount);
            //品牌
            List<Map<String, Object>> brandList = dao.getBrand();
            request.setAttribute("brandList", brandList);
            //设置默认值
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            String defaultStation = "";
            String defaultLinkman = "";
            String defaultTel = "";
            String defaultPostCode = "";
            String defaultAddrId = "";
            String defaultAddr = "";
            defaultValueMap.put("defaultRcvOrgid", dealerId);
            defaultValueMap.put("defaultRcvOrg", dealerName);

            //默认地址
            PageResult<Map<String, Object>> addrPs = dao.getSalesRelation(dealerId, "3", "", "", "", 10, 1);
            if (addrPs != null) {
                List<Map<String, Object>> addrList = addrPs.getRecords();
                if (addrList != null && addrList.size() > 0 && addrList.get(0) != null) {
                    defaultStation = CommonUtils.checkNull(addrList.get(0).get("STATION"));
                    defaultLinkman = CommonUtils.checkNull(addrList.get(0).get("LINKMAN"));
                    defaultTel = CommonUtils.checkNull(addrList.get(0).get("TEL"));
                    defaultPostCode = CommonUtils.checkNull(addrList.get(0).get("POST_CODE"));
                    defaultAddrId = CommonUtils.checkNull(addrList.get(0).get("ADDR_ID"));
                    defaultAddr = CommonUtils.checkNull(addrList.get(0).get("ADDR"));
                }
            }
            defaultValueMap.put("defaultStation", defaultStation);
            defaultValueMap.put("defaultLinkman", defaultLinkman);
            defaultValueMap.put("defaultTel", defaultTel);
            defaultValueMap.put("defaultPostCode", defaultPostCode);
            defaultValueMap.put("defaultAddrId", defaultAddrId);
            defaultValueMap.put("defaultAddr", defaultAddr);

            request.setAttribute("defaultValueMap", defaultValueMap);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("transList", list);
            act.setForword(PART_DIRECT_DLR_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DIRECT_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DIRECT_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 跳转到增加页面
     */
    public void addMarketOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(loginUser.getUserId());
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }

            //获取订货单位和CODE
            Map<String, Object> dealerMap = dao.getDealerName(dealerId);
            String dealerName = "";
            String dealerCode = "";
            if (null != dealerMap) {
                dealerName = dealerMap.get("DEALER_NAME").toString();
                dealerCode = dealerMap.get("DEALER_CODE").toString();
            }
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            //折扣率         
            String discount = dao.getDiscount(dealerId);
            dataMap.put("buyerName", loginUser.getName());
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerName", dealerName);
            dataMap.put("dealerCode", dealerCode);

            //设置默认销售单位 (只考虑有一个上级销售单位)
            String SELLER_NAME = "";
            String SELLER_CODE = "";
            String SELLER_ID = "";
            String accountId = "";
            String accountSum = "";
            String accountKy = "";
            String accountDj = "";

            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, "1", "", "", "", Constant.PAGE_SIZE_MAX, 1);
            List<Map<String, Object>> selList = null;
            if (null != ps && ps.getRecords().size() > 0) {
                selList = ps.getRecords();
                SELLER_NAME = selList.get(0).get("PARENTORG_NAME").toString();
                SELLER_CODE = selList.get(0).get("PARENTORG_CODE").toString();
                SELLER_ID = selList.get(0).get("PARENTORG_ID").toString();
                accountId = selList.get(0).get("ACCOUNT_ID").toString();
                accountSum = selList.get(0).get("ACCOUNT_SUM").toString();
                accountKy = selList.get(0).get("ACCOUNT_KY").toString();
                accountDj = selList.get(0).get("ACCOUNT_DJ").toString();
            }

            dataMap.put("SELLER_NAME", SELLER_NAME);
            dataMap.put("SELLER_CODE", SELLER_CODE);
            dataMap.put("SELLER_ID", SELLER_ID);
            dataMap.put("accountId", accountId);
            dataMap.put("accountSum", accountSum);
            dataMap.put("accountKy", accountKy);
            dataMap.put("accountDj", accountDj);

            //设置默认值
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            String defaultStation = "";
            String defaultLinkman = "";
            String defaultTel = "";
            String defaultPostCode = "";
            String defaultAddrId = "";
            String defaultAddr = "";
            defaultValueMap.put("defaultRcvOrgid", dealerId);
            defaultValueMap.put("defaultRcvOrg", dealerName);

            //默认地址
            PageResult<Map<String, Object>> addrPs = dao.getSalesRelation(dealerId, "3", "", "", "", 10, 1);
            if (addrPs != null) {
                List<Map<String, Object>> addrList = addrPs.getRecords();
                if (addrList != null && addrList.size() > 0 && addrList.get(0) != null) {
                    defaultStation = CommonUtils.checkNull(addrList.get(0).get("STATION"));
                    defaultLinkman = CommonUtils.checkNull(addrList.get(0).get("LINKMAN"));
                    defaultTel = CommonUtils.checkNull(addrList.get(0).get("TEL"));
                    defaultPostCode = CommonUtils.checkNull(addrList.get(0).get("POST_CODE"));
                    defaultAddrId = CommonUtils.checkNull(addrList.get(0).get("ADDR_ID"));
                    defaultAddr = CommonUtils.checkNull(addrList.get(0).get("ADDR"));
                }
            }
            defaultValueMap.put("defaultStation", defaultStation);
            defaultValueMap.put("defaultLinkman", defaultLinkman);
            defaultValueMap.put("defaultTel", defaultTel);
            defaultValueMap.put("defaultPostCode", defaultPostCode);
            defaultValueMap.put("defaultAddrId", defaultAddrId);
            defaultValueMap.put("defaultAddr", defaultAddr);

            request.setAttribute("defaultValueMap", defaultValueMap);
            dataMap.put("now", now);
            dataMap.put("discount", discount);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("transList", list);
            request.setAttribute("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07);
            act.setForword(PART_DLR_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_MARKET_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_MARKET_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 促销订单-跳转到增加页面
     */
    public void addDiscountOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            //仓库
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(loginUser.getUserId());
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }

            //获取订货单位和CODE
            Map<String, Object> dealerMap = dao.getDealerName(dealerId);
            String dealerName = "";
            String dealerCode = "";
            if (null != dealerMap) {
                dealerName = dealerMap.get("DEALER_NAME").toString();
                dealerCode = dealerMap.get("DEALER_CODE").toString();
            }
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);

            //折扣率         
            String discount = dao.getDiscount(dealerId);
            dataMap.put("buyerName", loginUser.getName());
            dataMap.put("dealerId", dealerId);
            dataMap.put("dealerName", dealerName);
            dataMap.put("dealerCode", dealerCode);

            //设置默认销售单位 (只考虑有一个上级销售单位)
            String SELLER_NAME = "";
            String SELLER_CODE = "";
            String SELLER_ID = "";
            String accountId = "";
            String accountSum = "";
            String accountKy = "";
            String accountDj = "";

            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, "1", "", "", "", Constant.PAGE_SIZE_MAX, 1);
            List<Map<String, Object>> selList = null;
            if (null != ps && ps.getRecords().size() > 0) {
                selList = ps.getRecords();
                SELLER_NAME = selList.get(0).get("PARENTORG_NAME").toString();
                SELLER_CODE = selList.get(0).get("PARENTORG_CODE").toString();
                SELLER_ID = selList.get(0).get("PARENTORG_ID").toString();
                accountId = selList.get(0).get("ACCOUNT_ID").toString();
                accountSum = selList.get(0).get("ACCOUNT_SUM").toString();
                accountKy = selList.get(0).get("ACCOUNT_KY").toString();
                accountDj = selList.get(0).get("ACCOUNT_DJ").toString();
            }

            dataMap.put("SELLER_NAME", SELLER_NAME);
            dataMap.put("SELLER_CODE", SELLER_CODE);
            dataMap.put("SELLER_ID", SELLER_ID);
            dataMap.put("accountId", accountId);
            dataMap.put("accountSum", accountSum);
            dataMap.put("accountKy", accountKy);
            dataMap.put("accountDj", accountDj);

            //设置默认值
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            String defaultStation = "";
            String defaultLinkman = "";
            String defaultTel = "";
            String defaultPostCode = "";
            String defaultAddrId = "";
            String defaultAddr = "";
            defaultValueMap.put("defaultRcvOrgid", dealerId);
            defaultValueMap.put("defaultRcvOrg", dealerName);

            //默认地址
            PageResult<Map<String, Object>> addrPs = dao.getSalesRelation(dealerId, "3", "", "", "", 10, 1);
            if (addrPs != null) {
                List<Map<String, Object>> addrList = addrPs.getRecords();
                if (addrList != null && addrList.size() > 0 && addrList.get(0) != null) {
                    defaultStation = CommonUtils.checkNull(addrList.get(0).get("STATION"));
                    defaultLinkman = CommonUtils.checkNull(addrList.get(0).get("LINKMAN"));
                    defaultTel = CommonUtils.checkNull(addrList.get(0).get("TEL"));
                    defaultPostCode = CommonUtils.checkNull(addrList.get(0).get("POST_CODE"));
                    defaultAddrId = CommonUtils.checkNull(addrList.get(0).get("ADDR_ID"));
                    defaultAddr = CommonUtils.checkNull(addrList.get(0).get("ADDR"));
                }
            }
            defaultValueMap.put("defaultStation", defaultStation);
            defaultValueMap.put("defaultLinkman", defaultLinkman);
            defaultValueMap.put("defaultTel", defaultTel);
            defaultValueMap.put("defaultPostCode", defaultPostCode);
            defaultValueMap.put("defaultAddrId", defaultAddrId);
            defaultValueMap.put("defaultAddr", defaultAddr);

            request.setAttribute("defaultValueMap", defaultValueMap);
            dataMap.put("now", now);
            dataMap.put("discount", discount);
            request.setAttribute("wareHouseList", wareHouseList);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("transList", list);
            request.setAttribute("orderType", Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08);
            act.setForword(PART_DLR_ORDER_ADD);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DISCOUNT_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报新增错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DISCOUNT_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 精品订单提报-查看
     */
    public void detailDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(orderId);
            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            mainMap.put("TRANS_TYPE", transType);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("historyList", historyList);
            request.setAttribute("detailList", detailList);
            
            act.setForword(PART_DLR_ORDER_DETAIL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 选择经销商弹出页面
     */
    public void selSales() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));

            String type = CommonUtils.checkNull(request.getParamValue("type"));
            String parentorgName = CommonUtils.checkNull(request.getParamValue("PARENTORG_NAME"));
            String parentorgCode = CommonUtils.checkNull(request.getParamValue("PARENTORG_CODE"));
            int page_size = Constant.PAGE_SIZE;
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
                  
            //判断是否为车厂  PartWareHouseDao
            PageResult<Map<String, Object>> ps = dao.getSalesRelation(dealerId, type, parentorgName, parentorgCode, "", page_size, curPage);
            String userDealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                userDealerId = beanList.get(0).getOrgId() + "";

            }
            if ("5".equals(type) || "6".equals(type)) {
                //给销售单使用
                List<Map<String, Object>> list = ps.getRecords();
                if (list != null) {
                    for (Map<String, Object> map : list) {
                        String buyerId = CommonUtils.checkNull(map.get("CHILDORG_ID"));
                        Map<String, Object> accountMap = dao.getAccount(buyerId, userDealerId, "");
                        if (accountMap == null) {
                            accountMap = new HashMap<String, Object>();
                            accountMap.put("ACCOUNT_ID", "");
                            accountMap.put("ACCOUNT_SUM", "");
                            accountMap.put("ACCOUNT_DJ", "");
                            accountMap.put("ACCOUNT_KY", "");
                        }
                        map.put("accountMap", accountMap);

                    }
                }

            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取数据错误!,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    public void getAcount() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String childorgId = CommonUtils.checkNull(request.getParamValue("CHILDORG_ID"));

            String userDealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList && beanList.size() >= 0) {
                userDealerId = beanList.get(0).getOrgId() + "";
            }
            Map<String, Object> accountMap = dao.getAccount(childorgId, userDealerId, "");
            if (accountMap == null) {
                accountMap = new HashMap<String, Object>();
                accountMap.put("ACCOUNT_ID", "");
                accountMap.put("ACCOUNT_SUM", "");
                accountMap.put("ACCOUNT_DJ", "");
                accountMap.put("ACCOUNT_KY", "");
            }
            act.setOutData("accountMap", accountMap);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取数据错误!,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 选择地址弹出页面
     */
    public void selAddress() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            act.getResponse().setContentType("application/json");
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            String type = CommonUtils.checkNull(request.getParamValue("type"));
            String parentorgName = CommonUtils.checkNull(request.getParamValue("PARENTORG_NAME"));
            String parentorgCode = CommonUtils.checkNull(request.getParamValue("PARENTORG_CODE"));
            int page_size = Constant.PAGE_SIZE;
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = dao.getAddress(dealerId, page_size, curPage);
            act.setOutData("ps", ps);
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
     * @Description: 订单提报-查看配件
     */
    public void showPartBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
        	//销售单位
        	String seller_id = CommonUtils.checkNull(req.getParamValue("SELLER_ID"));
        	//经销商id
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
            //订单类型
            String orderType = req.getParamValue("ORDER_TYPE");

            req.setAttribute("bookDealerId", dealerId);
            //分页方法 begin 
            //处理当前页	
            Integer curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; 
            PageResult<Map<String, Object>> ps = null;
            //促销订单 因价格在tt_part_sales_price价格维护，无单独功能维护
            if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "")) {
                ps = dao.showDiscountPartBase(req,seller_id, curPage, Constant.PAGE_SIZE);
            } else {
                ps = dao.showPartBase(req,seller_id, curPage, Constant.PAGE_SIZE);
            }
            
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查看配件信息出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 查看销售单位配件
     */
    public void showSellerPartBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = req.getParamValue("curPage") != null ? Integer
                    .parseInt(req.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
            req.setAttribute("bookDealerId", dealerId);
            String orderType=req.getParamValue("ORDER_TYPE");

            if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "")) {
                ps = dao.showDiscountPartBase(req,CommonUtils.checkNull(req.getParamValue("SELLER_ID")), curPage, Constant.PAGE_SIZE);
            } else {
                ps = dao.showSellerPartBase(req, CommonUtils.checkNull(req.getParamValue("SELLER_ID")), curPage, Constant.PAGE_SIZE);
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查看配件信息出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 促销订单-查看配件
     */
    public void showDiscountPartBase() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")): 1; // 处理当前页	
            PageResult<Map<String, Object>> ps = null;
            String dealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            req.setAttribute("bookDealerId", dealerId);
            ps = dao.showDiscountPartBase(req,CommonUtils.checkNull(req.getParamValue("SELLER_ID")), curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查看配件信息出错,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 订单提报-保存订单
     */
    public void saveOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String err = null;
        try {
        	//经销商id
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
            //销售单位
            String parentId = CommonUtils.checkNull(req.getParamValue("SELLER_ID"));
            //账户id
            String accountId = CommonUtils.checkNull(req.getParamValue("accountId"));
            //订单类型
            String orderType = CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"));
            //根据采购单位和销售单号获取账号id
            if ("".equals(accountId) || null == accountId) {
                accountId = dealerAccoutMng(dealerId, parentId) + "";
            }
            //查询账户信息
            Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, accountId);
            
            Double mainAmount = 0.00D;
            Long orderId = Long.parseLong(SequenceManager.getSequence(""));
            String msg = "";
            
            req.setAttribute("orderId", orderId);
            req.setAttribute("msg", msg);

            //前端获取cb数据
            String[] partArr = req.getParamValues("cb");
            //遍历配件信息并生成订单
            if (null != partArr) {
            	//针对委托订单、销售采购订单
            	if(orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04+"")|| 
            			orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12+"")){
            		//判断是否是一个供应商,只有一个供应商时能保存
            		String partIdArr="";
                	for (int i = 0; i < partArr.length; i++) {
                		partIdArr =partIdArr+partArr[i]+",";
                	}
                	int venderNum=this.validateVender(partIdArr.substring(0, partIdArr.length()-1));
                	if(venderNum ==0 ){
                		 err = "您保存的配件信息未查找到供应商，请删除后重试!";
                         BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, err);
                         throw e1;
                	}else if(venderNum > 1){
	               		 err = "您保存的配件信息存在多个供应商，请删除后重试!";
	                     BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, err);
	                     throw e1;
                	}
            	}
            	
            	//插入数据
                for (int i = 0; i < partArr.length; i++) {
                	//生成明细订单
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
                	//保存订单明细
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
            if (CommonUtils.checkNull(req.getParamValue("state")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02.toString())) {
                if (!accountContorl()) {
                    //向车厂提必须有账户
                    if (parentId.equals(Constant.OEM_ACTIVITIES + "")) {
                        if (null == acountMap) {
                            BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!当前余额:0");
                            throw e1;
                        } else {
                            if (!this.validateSum(dealerId, parentId, mainAmount + "", accountId)) {
                                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!当前余额:" + CommonUtils.checkNull(acountMap.get("ACCOUNT_KY")));
                                throw e1;
                            }
                        }
                    }
                }
            }
            //保存主订单
            mainAmount = this.saveMainPo(req, act, mainAmount);
            //订单id
            String orderId1=CommonUtils.checkNull(req.getAttribute("orderId"));
            //订单编码
            String orderCode1=CommonUtils.checkNull(req.getAttribute("orderCode"));
            //资金占用(订单提交时占用资金)
            if (CommonUtils.checkNull(req.getParamValue("state")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02.toString())) {
                if (!"".equals(accountId)) {
                    this.insertAccount(dealerId, parentId, mainAmount, Long.valueOf(orderId1), orderCode1, logonUser, accountId);
                }
            }
            //状态
            String state=CommonUtils.checkNull(req.getParamValue("state"));
            this.saveHistory(req, act, Integer.valueOf(state));
           
            //委托订单和销售采购订单不涉及运费问题
            if(!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04) || 
            		!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12)){
            	//add by yuan 20140331 提交时重新校验运费
                ArrayList ins = new ArrayList();
                ins.add(0, orderId);
                dao.callProcedure("PKG_PART_TOOLS.P_CALEXP", ins, null);
                //end
            }
            
            POContext.endTxn(true);
            act.setOutData("orderCode", orderCode1);
            act.setOutData("success", "订单：" + orderCode1 + ",操作成功!");
        } catch (Exception e) {//异常方法
            act.setOutData("error", "保存失败!");
            if (e instanceof BizException) {
                POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                return;
            }
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, err);
            logger.error(logonUser, e1);
            act.setException(e1);
        } 
        finally {
            try {
                POContext.cleanTxn();
            } catch (Exception ex) {
                BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "保存订单详细信息出错,请联系管理员!");
                logger.error(logonUser, e1);
                act.setException(e1);
            }
        }

    }
    /**
     * 验证是否只有一个供应商
     * @param partId
     * @return
     * @throws Exception
     */
    public int validateVender(String partId) throws Exception {
        try {
        	//根据part_id判断是否有多个供应商
            Map<String, Object> map = dao.getVender(partId);
            if(map!=null){
                int  num= Integer.parseInt(map.get("TOTAL").toString());
            	return num;
            }
        	return 0;
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
    public boolean validateSum(String dealerId, String parentId, String sum, String accountId) throws Exception {
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, accountId);
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
     * @Description: 订单提报-保存主订单
     */
    private Double saveMainPo(RequestWrapper req, ActionContext act, Double mainAmount) throws Exception {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String exStr = "";
        String orderCode = "";
        try {
            TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
            Long orderId = Long.valueOf(req.getAttribute("orderId").toString());
            String accountId = CommonUtils.checkNull(req.getParamValue("accountId"));
            String produceFac = CommonUtils.checkNull(req.getParamValue("produceFac")) == "" ? Constant.YIELDLY_01.toString() : CommonUtils.checkNull(req.getParamValue("produceFac"));
            String payType = CommonUtils.checkNull(req.getParamValue("PAY_TYPE")) == "" ?Constant.CAR_FACTORY_SALES_PAY_TYPE_01.toString() : CommonUtils.checkNull(req.getParamValue("PAY_TYPE"));
            po.setOrderId(orderId);
            //获取订单号，订单类型不一样，号也不一样 需要判断
            if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_16, req.getParamValue("dealerId"));
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_06, req.getParamValue("dealerId"));
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_33, req.getParamValue("dealerId"));
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_35, req.getParamValue("dealerId"));
            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12 + "")) {
                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_70, req.getParamValue("dealerId"));
            }
            //众泰一下几个类型未使用
//            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03 + "")) {
//                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_34, req.getParamValue("dealerId"));
//            }else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "")) {
//                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_36, req.getParamValue("dealerId"));
//            } else if (req.getParamValue("ORDER_TYPE").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09 + "")) {
//                orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_37, req.getParamValue("dealerId"));
//            } 
            req.setAttribute("orderCode", orderCode);
            po.setProduceFac(produceFac);
            po.setOrderCode(orderCode);
            po.setOrderType(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"))));
            po.setPayType(Integer.valueOf(payType));
            try {
                po.setDealerId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("dealerId"))));
            } catch (Exception ex) {
                exStr = "订货单位出错";
                throw ex;
            }
            po.setDealerCode(CommonUtils.checkNull(req.getParamValue("dealerCode")));
            po.setDealerName(CommonUtils.checkNull(req.getParamValue("dealerName")));
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
            if (null != req.getParamValue("accountSum") && !"null".equals(req.getParamValue("accountSum"))) {
                po.setAccountSum(Double.valueOf(req.getParamValue("accountSum").replace(",", "")));
            }
            if (null != req.getParamValue("accountKy") && !"null".equals(req.getParamValue("accountKy"))) {
                po.setAccountKy(Double.valueOf(req.getParamValue("accountKy").replace(",", "")));
            }
            if (null != req.getParamValue("accountDj") && !"null".equals(req.getParamValue("accountDj"))) {
                po.setAccountDj(Double.valueOf(req.getParamValue("accountDj").replace(",", "")));
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
            //如果是服务商给供应中心提报的直发订单   需要先给供应中心审核   然后又供应中心给车厂审核
            //获取单位ID
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }
            //获取上级单位ID  为直发校验提供数据
            Map<String, Object> parentOrgMap = dao.getParentOrgId(dealerId);
            //获取订货单位和CODE
            Map<String, Object> dealerMap = dao.getDealerName(dealerId);
            //如果上级不是车厂   就说明上级为供应中心   所以USER为服务商
            if (!CommonUtils.checkNull(parentOrgMap.get("PARENTORG_ID")).equals(Constant.OEM_ACTIVITIES)) {
                //需要修改SELLER_ID  达到提给供应商的目的
                po.setSellerId(Long.valueOf(CommonUtils.checkNull(parentOrgMap.get("PARENTORG_ID"))));
                po.setSellerCode(CommonUtils.checkNull(parentOrgMap.get("PARENTORG_CODE")));
                po.setSellerName(CommonUtils.checkNull(parentOrgMap.get("PARENTORG_NAME")));
            }
            if ((po.getOrderType() + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "")) {
                po.setDiscountStatus(Constant.IF_TYPE_YES + "");
            }
            //重新获取运费存储
            Double freight = 0D;
            if ((po.getSellerId() + "").equals(Constant.OEM_ACTIVITIES)) {
                freight = Double.valueOf((dao.getFreight(po.getDealerId() + "", po.getOrderType() + "", po.getOrderAmount() + "")).replaceAll(",", ""));
            }
            if (po.getTransType().equals("3")) {
                freight = 0D;
            }
            po.setOrderAmount(Arith.add(po.getOrderAmount(), freight));
            mainAmount = Arith.add(mainAmount, freight);
            
            if (po.getTransType().equals("3")) {
            }
            
            po.setFreight(freight);
            if (freight == 0) {
                po.setIsTransfree(Constant.IF_TYPE_YES);
            } else {
                po.setIsTransfree(Constant.IF_TYPE_NO);
            }
            if (dealerId.equals(Constant.OEM_ACTIVITIES + "")) {
                po.setOemFlag(Constant.PART_BASE_FLAG_YES);
            } else {
                po.setOemFlag(Constant.PART_BASE_FLAG_NO);
            }
            po.setAccountId(Long.valueOf(accountId));
            dao.insert(po);
            req.setAttribute("orderCode", po.getOrderCode());//订单编码
            return mainAmount;
        } catch (Exception ex) {
            BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, exStr + ",请联系管理员!");
            throw e1;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 订单提报-保存订单明细并返回金额
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
            
            TtPartDefinePO definePO = new TtPartDefinePO();
            definePO.setPartId(Long.valueOf(partId));
            definePO = (TtPartDefinePO) dao.select(definePO).get(0);
            
            po.setPartCode(definePO.getPartCode());
            po.setUnit(CommonUtils.checkNull(req.getParamValue("unit_" + partId)));

            if (null != req.getParamValue("isLack_" + partId)) {
                po.setIsLack(Integer.valueOf(req.getParamValue("isLack_" + partId)));
            }
            if (null != req.getParamValue("isReplaced_" + partId)) {
            	int replaceFlag=Integer.valueOf(req.getParamValue("isReplaced_" + partId));
            	//存在替换关系
            	if(replaceFlag==Constant.IF_TYPE_YES){
                    TtPartReplacedDefinePO rdPo=new TtPartReplacedDefinePO();
                    rdPo.setRepartId(Long.parseLong(partId));
                    //查询完全替换还是部分替换
                    List<TtPartReplacedDefinePO> listPo=dao.select(rdPo);
                    if(listPo!=null){
                    	int listSize=listPo.size();
                    	for(int i=0;i<listSize;i++){
                    		TtPartReplacedDefinePO replacePo=listPo.get(i);
                    		//完全替换
                        	if(replacePo.getType().equals(Constant.ZT_PB_PART_REPLACE_TYPE_02)){
                            	TtPartBookPO bookPo=new TtPartBookPO();
                            	bookPo.setPartId(replacePo.getPartId());//配件id
//                            	bookPo.setWhId(replacePo.getPartId());//仓库id  因20170725未确定仓库id
                            	//查询库存
                            	TtPartBookPO bpo=(TtPartBookPO)dao.select(bookPo).get(0);
                            	String errormsg="";
                            	if(bpo.getNormalQty()>0){
                            		//可以库存大于0时，请先选择旧件
                            		BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件:" + po.getPartCname() + "存在旧件，请先选择旧件【旧件名称："+replacePo.getPartCname()+"】!");
                            		throw e1;
                            	}
                            }
                        	break;
                    	}
                    }
            	}
                po.setIsReplaced(replaceFlag);
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
                }
                exStr = "最小库存量数据出错";
                throw e;
            }
            po.setBuyQty(Long.valueOf(req.getParamValue("buyQty_" + partId)));
            try {
                po.setBuyPrice(parseDouble(req.getParamValue("buyPrice_" + partId)));
                po.setBuyPrice1(parseDouble(req.getParamValue("buyPrice_" + partId)));//订货单价(折扣后),因无折扣，存采购价
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
            if (CommonUtils.checkNull(req.getParamValue("upOrgStock_" + partId)).equals("Y") ||
                    CommonUtils.checkNull(req.getParamValue("upOrgStock_" + partId)).equals(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01.toString())) {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_01);
            } else {
                po.setIsHava(Constant.CAR_FACTORY_SALES_MANAGER_STOCK_02);
            }
            po.setRemark(CommonUtils.checkNull(req.getParamValue("remark_" + partId)));
            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setStatus(Constant.STATUS_ENABLE);

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
            po.setRemark(CommonUtils.checkNull(map.get("REMARK")));

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
//            po.setRemark("");
            po.setCreateDate(new Date());
            po.setCreateBy(logonUser.getUserId());
            po.setStatus(Constant.STATUS_ENABLE);
            
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
     * @Description: 重新计算金额
     */
    public Double reCountMoney(Double buyQty, Double price) {
        Double money = Arith.mul(Double.valueOf(buyQty), Double.valueOf(price));
        return money;
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 订单提报-作废
     */
    public void cancelOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
            String flag = CommonUtils.checkNull(req.getParamValue("flag"));
            //校验修改
            if (!"1".equals(flag) && this.validateState(orderId, allowState)) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已经改变，无法作废!");
                throw e1;
            }
            req.setAttribute("orderId", orderId);
            
            TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
            newPo.setOrderId(Long.valueOf(orderId));
            newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04);
            newPo.setSubmitBy(logonUser.getUserId());
            newPo.setSubmitDate(new Date());
            
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(orderId));
            dao.update(oldPo, newPo);
            
            this.saveHistory(req, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04);
            //add by yuan 20131212 主机厂新增订单删除预扣款
            if ("1".equals(flag)) {
                TtPartAccountRecordPO recordPO = new TtPartAccountRecordPO();
                recordPO.setSourceId(Long.valueOf(orderId));
                dao.delete(recordPO);
            }
            //end
            POContext.endTxn(true);
            act.setOutData("success", "操作成功!");
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                return;
            }
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "作废失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            try {
                POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "作废失败,请联系管理员!");
            }
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

    /**
     * 经销商订单提交
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 提报订单
     */
    public void submitOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        String error = "";
        try {
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
            String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
            String parentId = CommonUtils.checkNull(req.getParamValue("parentId"));
            String amount = CommonUtils.checkNull(req.getParamValue("amount")).replace(",", "");
            String orderCode = CommonUtils.checkNull(req.getParamValue("orderCode"));
            System.out.println("orderId:"+orderId);
            //查询订单信息
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            
            String accountId = CommonUtils.checkNull(mainMap.get("ACCOUNT_ID"));//资金账号ID
            String orderType = CommonUtils.checkNull(mainMap.get("ORDER_TYPE"));//订单类型

            //配件状态校验
            List<Map<String, Object>> list = dao.dlrOrderDtlCheck(orderType, orderId, dealerId, "2");
            if (list.size() > 0) {
                for (Map<String, Object> map : list) {
                    if (map.get("PART_OLDCODE").toString() != null) {
                        error += map.get("PART_OLDCODE").toString() + "状态无效不能订购!";
                    }
                }
            }
            //配件分网校验
//            List<Map<String, Object>> list2 = dao.dlrOrderDtlCheck(orderType, orderId, dealerId, "1");
//            if (list2.size() > 0) {
//                for (Map<String, Object> map : list2) {
//                    if (map.get("PART_OLDCODE").toString() != null) {
//                        error += map.get("PART_OLDCODE").toString() + "无权订购!";
//                    }
//                }
//            }
            //最小包装量检验
            TtPartDlrOrderDtlPO orderDtlPO = new TtPartDlrOrderDtlPO();
            orderDtlPO.setOrderId(Long.valueOf(orderId));
            List<TtPartDlrOrderDtlPO> dtlPOs = dao.select(orderDtlPO);
            if (dtlPOs.size() > 0) {
                for (TtPartDlrOrderDtlPO dtlPO : dtlPOs) {
                    if (dtlPO.getBuyPrice() == 0d || dtlPO.getBuyAmount() == 0d) {
                        error += dtlPO.getPartOldcode() + "无单价不允许订购!";
                    }
                }
            }
            if (dtlPOs.size() > 0) {
                for (TtPartDlrOrderDtlPO dtlPO : dtlPOs) {
                    if (dtlPO.getBuyQty() % dtlPO.getMinPackage() != 0) {
                        error += dtlPO.getPartOldcode() + "订货数量必须为最小包装量整数倍!";
                    }
                }
            }
            //校验修改
            if (this.validateState(orderId, allowState)) {
                error += "订单状态已经改变，无法重复提报!";
            }
            //提报时取最新价格
            dao.updateOrderDtlPrice(orderId);
            dao.updateOrderMainAmount(orderId);

            if ("".equals(accountId) || null == accountId) {
                accountId = dealerAccoutMng(dealerId, parentId) + "";
            }
            //获取账户余额等
            Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, accountId);
            if (null != acountMap) {
                if (!this.validateSum(dealerId, parentId, CommonUtils.checkNull(mainMap.get("ORDER_AMOUNT")), accountId)) {
                    error += "余额不足!当前余额:" + CommonUtils.checkNull(acountMap.get("ACCOUNT_KY")) + "";
                }
            }
            //accountContorl 配件销售虚拟金额校验开关
            if (!accountContorl()) {
                if (parentId.equals(Constant.OEM_ACTIVITIES + "")) {
                    if (null == acountMap) {
                        error += "余额不足!当前余额:0!";
                    }
                    if (null != acountMap) {
                        if (!this.validateSum(dealerId, parentId, CommonUtils.checkNull(mainMap.get("ORDER_AMOUNT")), accountId)) {
                            error += "余额不足!当前余额:" + CommonUtils.checkNull(acountMap.get("ACCOUNT_KY")) + "";
                        }
                    }
                }
            }
            req.setAttribute("orderId", orderId);
            req.setAttribute("orderCode", orderCode);
            
            if ("".equals(error)) {
                TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
                newPo.setOrderId(Long.valueOf(orderId));
                newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);
                newPo.setSubmitBy(logonUser.getUserId());
                newPo.setSubmitDate(new Date());
                if ("".equals(CommonUtils.checkNull(mainMap.get("ACCOUNT_ID")))) {
                    newPo.setAccountId(Long.valueOf(accountId));
                }
                TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
                oldPo.setOrderId(Long.valueOf(orderId));
                dao.update(oldPo, newPo);

                //占用资金
                if (null != acountMap.get("ACCOUNT_SUM") && !"null".equals(CommonUtils.checkNull(acountMap.get("ACCOUNT_SUM"))) && !"".equals(CommonUtils.checkNull(acountMap.get("ACCOUNT_SUM")))) {
                    this.insertAccount(dealerId, parentId, Double.valueOf(amount), Long.valueOf(orderId), orderCode, logonUser, accountId);
                }
                this.saveHistory(req, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);
                
                //委托订单和销售采购订单不涉及运费问题
                if(!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04) || 
                		!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12)){

                    ArrayList ins = new ArrayList();
                    ins.add(0, Long.valueOf(orderId));
                    dao.callProcedure("PKG_PART_TOOLS.P_CALEXP", ins, null);
                }
                act.setOutData("success", "提报成功!");
            } else {
                throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, error);
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, error);
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 常规订单、紧急订单、促销订单-修改订单
     */
    public void modifyDlrOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        //前台页面数据集合
        Map<String, Object> dataMap = new HashMap();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            List<Map<String, Object>> detailList = dao.queryPartDlrOrderDetail(orderId);

            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            request.setAttribute("transList", list);
            act.setOutData("isTransFreeOrg", dao.isTransFree(mainMap.get("DEALER_ID").toString()));
            //获取单位ID
            String dealerId = "";
            String accountId = CommonUtils.checkNull(mainMap.get("ACCOUNT_ID").toString());
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            Map<String, Object> acountMap = new HashMap<String, Object>();
            if (!"".equals(CommonUtils.checkNull(mainMap.get("ACCOUNT_SUM")))) {
                acountMap = dao.getAccount(CommonUtils.checkNull(mainMap.get("DEALER_ID")), CommonUtils.checkNull(mainMap.get("SELLER_ID")), accountId);
                dataMap.put("accountFlag", true);
                //账户数据可变   重新获取账户数据
            } else {
                dataMap.put("accountFlag", false);
            }
            dataMap.put("currAcountMap", acountMap);
            //折扣率         
            String discount = dao.getDiscount(dealerId);
            request.setAttribute("dataMap", dataMap);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("detailList", detailList);
            if(!CommonUtils.isNullList(detailList)){
	            List<Map<String, Object>> stoList = dao.getStoVender(CommonUtils.checkNull(detailList.get(0).get("DEFT_ID")));
	            String brand = "";
	            if (stoList != null && stoList.size() > 0 && stoList.get(0) != null) {
	                brand = CommonUtils.checkNull(stoList.get(0).get("BRAND"));
	            }
	            request.setAttribute("brand", brand);
            }
            //品牌
            List<Map<String, Object>> brandList = dao.getBrand();
            request.setAttribute("brandList", brandList);
            String forward = this.PART_DLR_ORDER_MOD;//常规订单

//            if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "").equals(CommonUtils.checkNull(mainMap.get("ORDER_TYPE")))) {
//                forward = PART_DIRECT_DLR_ORDER_MOD;
//            } else if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03 + "").equals(CommonUtils.checkNull(mainMap.get("ORDER_TYPE")))) {
//                forward = PART_PLAN_DLR_ORDER_MOD;
//            } else if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07 + "").equals(CommonUtils.checkNull(mainMap.get("ORDER_TYPE")))) {
//                forward = PART_MARKET_DLR_ORDER_MOD;
//            } else //以上这几个类型众泰没有
            
            if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "").equals(CommonUtils.checkNull(mainMap.get("ORDER_TYPE")))) {
              forward = PART_DIRECT_DLR_ORDER_MOD;//委托订单
            }else if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01 + "").equals(CommonUtils.checkNull(mainMap.get("ORDER_TYPE")))) {
                forward = PART_EMERGENCY_DLR_ORDER_MOD;//紧急订单
            }else if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08 + "").equals(CommonUtils.checkNull(mainMap.get("ORDER_TYPE")))) {
                forward = PART_DISCOUNT_DLR_ORDER_MOD;//促销订单
            } else if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12 + "").equals(CommonUtils.checkNull(mainMap.get("ORDER_TYPE")))) {
                forward = PART_SALE_PUR_ORDER_MOD;//销售采购订单
            }
            act.setForword(forward);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 订单提报-保存修改订单
     */
    public void saveModifyOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String err;
        try {
            String ulrFlg = "";
            String dealerId = logonUser.getDealerId() == null ? "" : logonUser.getDealerId();
            //订单id
            String orderId = CommonUtils.checkNull(req.getParamValue("orderId"));
            
            String parentId = CommonUtils.checkNull(req.getParamValue("SELLER_ID"));
            
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
            
            String orderType=CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"));
            
            //服务商修改跳转到服务商订单页面，销售单位修改跳转到审核页面
            if (dealerId.equals(mainMap.get("DEALER_ID").toString())) {
                ulrFlg = "1";
            } else {
                ulrFlg = "2";
            }
            //校验修改
            if ((req.getParamValue("state") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                if (!(mainMap.get("STATE") + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                    BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已经改变，无法修改!");
                    throw e1;
                }
            } else {
                if (dealerId != null && this.validateState(orderId, allowState) && dealerId.equals(mainMap.get("DEALER_ID").toString())) {
                    BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已经改变，无法修改!");
                    throw e1;
                }
            }

            //删除订单详细然后保存
            TtPartDlrOrderDtlPO ttPartDlrOrderDtlPO = new TtPartDlrOrderDtlPO();
            ttPartDlrOrderDtlPO.setOrderId(Long.valueOf(orderId));
            dao.delete(ttPartDlrOrderDtlPO);
            
            //保存详细订单
            Double mainAmount = 0.00D;
            String[] partArr = req.getParamValues("cb");
            String msg = "";
            req.setAttribute("msg", msg);
            
            if(partArr!=null){
            	//针对委托订单、销售采购订单
                if(orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08+"")|| 
            			orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12+"")){
            		//判断是否是一个供应商,只有一个供应商时能保存
            		String partIdArr="";
                	for (int i = 0; i < partArr.length; i++) {
                		partIdArr =partIdArr+partArr[i]+",";
                	}
                	System.out.println("+++++saveModifyOrder.partIdArr:"+partIdArr.substring(0, partIdArr.length()-1));
                	int venderNum=this.validateVender(partIdArr.substring(0, partIdArr.length()-1));
                	if(venderNum ==0 ){
                		 err = "您保存的配件信息未查找到供应商，请删除后重试!";
                         BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, err);
                         throw e1;
                	}else if(venderNum > 1){
                   		 err = "您保存的配件信息存在多个供应商，请删除后重试!";
                         BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, err);
                         throw e1;
                	}
            	}
                for (int i = 0; i < partArr.length; i++) {
                	//保存订单明细
                    Double detailMoney = this.saveDetailPo(req, act, partArr[i]);
                    
                    if ("0.0".equals(detailMoney + "")) {
                        err = "配件【" + CommonUtils.checkNull(req.getParamValue("partOldcode_" + partArr[i])) + "】采购价不能为0，请删除后重试!";
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, err);
                        throw e1;
                    }
                    mainAmount = Arith.add(mainAmount, detailMoney);
                }
            }
            msg = CommonUtils.checkNull(req.getAttribute("msg"));
            if (!"".equals(msg)) {
                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, msg);
                throw e1;
            }
            //更新主订单
            this.updateMainPo(req, act, mainAmount);


            POContext.endTxn(true);
            act.setOutData("success", "保存成功!");
            act.setOutData("ulrFlg", ulrFlg);
        } catch (Exception e) {//异常方法
            act.setOutData("error", "保存失败!");
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

    private void validatePkgBox() throws Exception {
        ActionContext act = ActionContext.getContext();
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String dealerId = CommonUtils.checkNull(req.getParamValue("dealerId"));
        if ((Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04.toString()).equals(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE")))) {//直发
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            String[] partArr = req.getParamValues("cb");
            String brand = CommonUtils.checkNull(req.getParamValue("brand"));
            List<Map<String, Object>> list = dao.getStoList(brand);
            Map<String, Object> map = list.get(0);
            int criterion = Integer.valueOf(map.get("CRITERION") + "");
            int box = 0;
            if (null != partArr) {
                for (int i = 0; i < partArr.length; i++) {
                    String partId = partArr[i];
                    int minPkg = Integer.valueOf(req.getParamValue("minPackage_" + partId));
                    int buyQty = Integer.valueOf(req.getParamValue("buyQty_" + partId));
                    box += buyQty / minPkg;
                }
            }
            String seq = CommonUtils.checkNull(req.getParamValue("seq"));
            if (!"".equals(seq)) {
                List<Map<String, Object>> upList = dao.getUploadList(seq);
                for (int i = 0; i < upList.size(); i++) {
                    String partId = (upList.get(i)).get("PART_ID") + "";
                    Map<String, Object> partMap = dao.getPartDefine(partId, dealerId);
                    int minPkg = Integer.valueOf(partMap.get("MIN_PACKAGE") + "");
                    int buyQty = Integer.valueOf((upList.get(i)).get("BUYQTY") + "");
                    box += buyQty / minPkg;
                }
            }

            if (box < criterion) {
                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, brand + "直发时所有品种订货箱数必须大于" + criterion + "箱!");
                throw e1;
            }
        }


    }

    private boolean validateVersion(String ver, String orderId) {
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        String version = dao.getVersion(orderId);
        if (ver.equals(version)) {
            return true;
        }
        return false;
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 更新主订单
     */
    private void updateMainPo(RequestWrapper req, ActionContext act, Double mainAmount) throws Exception {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String exStr = "";
        try {
            Map<String, Object> mainMap = dao.queryPartDlrOrderMain(CommonUtils.checkNull(req.getParamValue("orderId")));
            int ver = Integer.valueOf(mainMap.get("VER") == null ? "1" : mainMap.get("VER").toString()) + 1;
            String produceFac = CommonUtils.checkNull4Default(req.getParamValue("produceFac"), Constant.YIELDLY_01 + "");
            
            TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
            po.setProduceFac(produceFac);
            po.setOrderId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId"))));
            po.setOrderCode(CommonUtils.checkNull(req.getParamValue("orderCode")));
            po.setOrderType(Integer.valueOf(CommonUtils.checkNull(req.getParamValue("ORDER_TYPE"))));
            po.setPayType(Integer.valueOf(CommonUtils.checkNull4Default(req.getParamValue("PAY_TYPE"), Constant.CAR_FACTORY_SALES_PAY_TYPE_01 + "")));
            
            try {
                po.setDealerId(Long.valueOf(req.getParamValue("dealerId")));
            } catch (Exception ex) {
                exStr = "订货单位出错";
                throw ex;
            }
            po.setDealerCode(CommonUtils.checkNull(req.getParamValue("dealerCode")));
            po.setDealerName(CommonUtils.checkNull(req.getParamValue("dealerName")));
            
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
            po.setRcvOrg(CommonUtils.checkNull(req.getParamValue("RCV_ORG")));
            po.setIsAutchk(Constant.PART_BASE_FLAG_NO);
            po.setAddrId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("ADDR_ID"))));
            po.setAddr(CommonUtils.checkNull(req.getParamValue("ADDR")));
            po.setReceiver(CommonUtils.checkNull(req.getParamValue("RECEIVER")));
            po.setTel(CommonUtils.checkNull(req.getParamValue("TEL")));
            po.setPostCode(CommonUtils.checkNull(req.getParamValue("POST_CODE")));
            po.setStation(CommonUtils.checkNull(req.getParamValue("STATION")));
            po.setTransType(CommonUtils.checkNull(req.getParamValue("TRANS_TYPE")));
            
            if (null != req.getParamValue("accountSum")) {
                po.setAccountSum(parseDouble(req.getParamValue("accountSum")));
            }
            
            if (null != req.getParamValue("accountKy")) {
                po.setAccountKy(parseDouble(req.getParamValue("accountKy")));
            }
            if (null != req.getParamValue("accountDj")) {
                po.setAccountDj(parseDouble(req.getParamValue("accountDj")));
            }
            
            try {
                po.setOrderAmount(mainAmount);
            } catch (Exception ex) {
                exStr = "订购总金额出错";
                throw ex;
            }

            Double freight = 0D;
            if ((po.getSellerId() + "").equals(Constant.OEM_ACTIVITIES)) {
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
            if (freight > 0) {
                po.setIsTransfree(Constant.IF_TYPE_NO);
            } else {
                po.setIsTransfree(Constant.IF_TYPE_YES);
            }
            po.setOrderAmount(Arith.add(po.getOrderAmount(), freight));
            po.setFreight(freight);
            po.setDiscount(Double.valueOf(CommonUtils.checkNull(req.getParamValue("DISCOUNT"))));
            po.setRemark(CommonUtils.checkNull(req.getParamValue("textarea")));
            po.setUpdateDate(new Date());
            po.setUpdateBy(logonUser.getUserId());

            po.setState(Integer.valueOf(CommonUtils.checkNull(mainMap.get("STATE"))));
            po.setVer(ver);
            TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
            oldPo.setOrderId(Long.valueOf(CommonUtils.checkNull(req.getParamValue("orderId"))));
            dao.update(oldPo, po);

            //如果是已经提交的订单要处理金额
            if ((po.getState() + "").equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02 + "")) {
                //modfiy by yuan 20130924
                dealerAccoutReordMng(po.getDealerId(), po.getSellerId(), po.getOrderAmount(), po.getOrderId(), po.getOrderCode());
            }
        } catch (Exception ex) {
            BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, exStr + ",请联系管理员!");
            throw e1;
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
            listHead.add("订货数量");
            listHead.add("备注");
            list.add(listHead);
            // 导出的文件名
            String fileName = "配件提报模板.xls";
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

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public void exportPartPlanExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            PartBaseQueryDao partBaseQueryDao = PartBaseQueryDao.getInstance();
            String[] head = new String[12];
            head[0] = "订单号 ";
            head[1] = "订货单位编码";
            head[2] = "订货单位";
            head[3] = "订货人";
            head[4] = "订货日期";
            head[5] = "销售单位";
            head[6] = "订单类型";
            head[7] = "总金额";
            head[8] = "提交时间";
            head[9] = "订单状态";
            List<Map<String, Object>> list = dao.queryExportPartDlrOrder(request);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[3] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[5] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                    detail[6] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    detail[7] = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
                    detail[8] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[9] = CommonUtils.checkNull(map.get("STATE"));
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
    public void exportOemPartPlanExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            PartBaseQueryDao partBaseQueryDao = PartBaseQueryDao.getInstance();
            String[] head = new String[12];
            head[0] = "订单号 ";
            head[1] = "订货单位编码";
            head[2] = "订货单位";
            head[3] = "订货人";
            head[4] = "订货日期";
            head[5] = "销售单位";
            head[6] = "订单类型";
            head[7] = "总金额";
            head[8] = "提交时间";
            head[9] = "订单状态";
            List<Map<String, Object>> list = dao.queryOemExportPartDlrOrder(request);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[3] = CommonUtils.checkNull(map.get("BUYER_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[5] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                    detail[6] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    detail[7] = CommonUtils.checkNull(map.get("ORDER_AMOUNT"));
                    detail[8] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[9] = CommonUtils.checkNull(map.get("STATE"));
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
     * @Description:占用资金
     */
    public void insertAccount(String dealerId, String parentId, Double amount, Long orderId, String orderCode, AclUserBean loginUser, String accountId) throws Exception {
        try {
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            po.setRecordId(recordId);
            po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01);
            po.setDealerId(Long.valueOf(dealerId));
            //获取账户余额等
            Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, accountId);
            if (null != acountMap) {
                po.setAccountId(Long.valueOf(acountMap.get("ACCOUNT_ID").toString()));
            }
            po.setAmount(amount);
            po.setFunctionName("配件提报预占");
            po.setSourceId(Long.valueOf(orderId));
            po.setSourceCode(orderCode);
            //po.setOrderId(orderId);
            //po.setOrderCode(orderCode);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            dao.insert(po);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 上传配件信息(提报订单的配件信息)
     */
    public void uploadExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        act.setOutData("flag", true);
        try {
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile1", 3, 2, maxSize);
            List<Map<String, String>> errorInfo = null;
            String err = "";

            errorInfo = new ArrayList<Map<String, String>>();
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
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
                throw e1;
            }
            List excelList = this.getMapList();
            List<Map> list = getMapList();
            List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
            loadVoList(excelList, list, errorInfo, CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")));

            //是否有重复配件
            List<Map<String, String>> repeatErorr = new ArrayList<Map<String, String>>();
            List<Map<String, Object>> partRepeatList = dao.checkPartRepeat();
            if (partRepeatList.size() > 0) {
                for (Map<String, Object> map : partRepeatList) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("partOldcode", map.get("PART_OLDCODE").toString());
                    errormap.put("repeatQty", map.get("REPEAT_QTY").toString());
                    repeatErorr.add(errormap);
                }
                act.setOutData("repeatErorr", repeatErorr);
                act.setForword(INPUT_ERROR_URL);
                return;
            }
            if (errorInfo.size() > 0) {
                act.setOutData("errorInfo", errorInfo);
                act.setForword(INPUT_ERROR_URL);
                return;
            } else {
                String seq = SequenceManager.getSequence("");
                dao.saveUploadData(request, seq);
                PageResult<Map<String, Object>> ps = dao.showPartBaseForUpload(request, CommonUtils.checkNull(request.getParamValue("SELLER_ID")), 1, Constant.PART_STOCK_STATUS_CHANGE_MAX_LINENUM);
                if (ps.getRecords() == null) {
                    act.setOutData("noDtl", true);
                }
                if (CommonUtils.checkNull(request.getParamValue("saveFlag")).equals("save")) {
                    act.setOutData("saveFlag", "save");
                }
                act.setOutData("seq", seq);
                act.setForword(UPLOADFILE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, act.getOutData("error") == null ? "文件读取错误" : act.getOutData("error").toString());
            }
            act.setOutData("false", true);
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(UPLOADFILE);
        }
    }

    public void getUploadExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
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
            map.put("dealerId", dealerId);
            map.put("dealerName", dealerName);
            map.put("dealerCode", dealerCode);
            map.put("buyerName", loginUser.getName());
            map.put("accountId", CommonUtils.checkNull(request.getParamValue("accountId")));
            map.put("SELLER_NAME", CommonUtils.checkNull(request.getParamValue("SELLER_NAME")));
            map.put("SELLER_CODE", CommonUtils.checkNull(request.getParamValue("SELLER_CODE")));
            map.put("SELLER_ID", CommonUtils.checkNull(request.getParamValue("SELLER_ID")));
            map.put("PAY_TYPE", CommonUtils.checkNull(request.getParamValue("PAY_TYPE")));
            map.put("produceFac", CommonUtils.checkNull(request.getParamValue("produceFac")));
            map.put("now", now);
            map.put("textarea", CommonUtils.checkNull(request.getParamValue("textarea")));
            map.put("accountSum", CommonUtils.checkNull(request.getParamValue("accountSum")));
            map.put("accountKy", CommonUtils.checkNull(request.getParamValue("accountKy")));
            map.put("accountDj", CommonUtils.checkNull(request.getParamValue("accountDj")));

            act.setOutData("dataMap", map);
            Map<String, Object> defaultValueMap = new HashMap<String, Object>();
            defaultValueMap.put("defaultRcvOrg", CommonUtils.checkNull(request.getParamValue("RCV_ORG")));
            defaultValueMap.put("RCV_CODE", CommonUtils.checkNull(request.getParamValue("RCV_CODE")));
            defaultValueMap.put("defaultRcvOrgid", CommonUtils.checkNull(request.getParamValue("RCV_ORGID")));
            defaultValueMap.put("defaultAddr", CommonUtils.checkNull(request.getParamValue("ADDR")));
            defaultValueMap.put("defaultAddrId", CommonUtils.checkNull(request.getParamValue("ADDR_ID")));
            defaultValueMap.put("defaultStation", CommonUtils.checkNull(request.getParamValue("STATION")));
            defaultValueMap.put("defaultLinkman", CommonUtils.checkNull(request.getParamValue("RECEIVER")));
            defaultValueMap.put("defaultTel", CommonUtils.checkNull(request.getParamValue("TEL")));
            defaultValueMap.put("defaultPostCode", CommonUtils.checkNull(request.getParamValue("POST_CODE")));
            act.setOutData("defaultValueMap", defaultValueMap);
            List transList = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            
            act.setOutData("orderType", CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")));
            act.setOutData("transType", CommonUtils.checkNull(request.getParamValue("TRANS_TYPE")));

            act.setOutData("transList", transList);
            act.setOutData("detailList", list);
            List<Map<String, Object>> brandList = dao.getBrand();
            act.setOutData("brandList", brandList);
            if (CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                act.setForword(this.PART_DIRECT_DLR_ORDER_ADD);
            } else {
                act.setForword(PART_DLR_ORDER_ADD);
            }
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
            act.setForword(PART_DLR_ORDER_ADD);
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
    private Map<String, Object> loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo, String orderType) {

        List<Map<String, Object>> dataList = new ArrayList();
        Map<String, Object> rtnMap = new HashMap();
        List<TmpPartUploadPO> temPartUploadPO = new ArrayList<TmpPartUploadPO>();

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
                TmpPartUploadPO po = new TmpPartUploadPO();
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);

                if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("one", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("two", "配件编码");
                    errormap.put("three", "为空!");
                    errorInfo.add(errormap);
                } else {
                    List<Map<String, Object>> partCheck = dao.getTmpData(cells[0].getContents().trim(), orderType);
                    if (null != partCheck && partCheck.size() > 0) {
                        List<Map<String, Object>> partOldCodeCheck = dao.getTmpData(cells[0].getContents().trim(), orderType);

                        if (null != partOldCodeCheck && partOldCodeCheck.size() > 0) {
                            if ((!partOldCodeCheck.get(0).get("STATE").toString().equals(Constant.STATUS_ENABLE + ""))) {
                                Map<String, String> errormap = new HashMap<String, String>();
                                errormap.put("one", "第" + (i + 1) + "页,第" + key + "行配件" + cells[0].getContents().trim());
                                errormap.put("two", "配件状态");
                                errormap.put("three", "已失效!");
                                errorInfo.add(errormap);
                            } else {
                                po.setPartOldcode(CommonUtils.checkNull(cells[0].getContents()));
                            }
                        } else {
                            Map<String, String> errormap = new HashMap<String, String>();
                            errormap.put("one", "第" + (i + 1) + "页,第" + key + "行配件" + cells[0].getContents().trim());
                            errormap.put("two", "配件编码【" + cells[0].getContents().trim() + "】");
                            errormap.put("three", "不存在!");
                            errorInfo.add(errormap);
                        }
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("one", "第" + (i + 1) + "页,第" + key + "行配件" + cells[0].getContents().trim());
                        errormap.put("two", "提报状态");
                        errormap.put("three", "无效!");
                        errorInfo.add(errormap);
                    }
                }
                if ("".equals(cells[1].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("one", "第" + (i + 1) + "页,第" + key + "行配件" + cells[0].getContents().trim());
                    errormap.put("two", "配件订购数量");
                    errormap.put("three", "为空!");
                    errorInfo.add(errormap);
                } else {
                    try {
                        po.setQty(Long.valueOf(cells[1].getContents()));

                    } catch (Exception e) {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("one", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("two", "配件订购数量");
                        errormap.put("three", "格式错误!");
                        errorInfo.add(errormap);
                    }
                }
                //新增备注
                if (cells.length > 2 && "" != CommonUtils.checkNull(cells[2].getContents()) && null != CommonUtils.checkNull(cells[2].getContents())) {
                    po.setRemark(cells[2].getContents());
                }
                temPartUploadPO.add(po);

//                try {
//                    parseCells(dataList, key, cells);
//                } catch (Exception e) {
//                    if (e instanceof BizException) {
//                        BizException e1 = (BizException) e;
//                        error += "上传文件," + e1.getMessage().replaceAll("操作失败！失败原因：", "") + "</br>";
//                        continue;
//                    } else {
//                        throw e;
//                    }
//                }
            }
        }
        rtnMap.put("dataList", dataList);
        rtnMap.put("error", error);
        if (errorInfo.size() < 1) {
            //保存
            for (TmpPartUploadPO po : temPartUploadPO) {
                dao.insert(po);
            }
        }
        return rtnMap;
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
        if (cells.length < 2) {
            BizException ex = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "请填写配件编码和数量!");
            throw ex;
        }
        if ("" == CommonUtils.checkNull(cells[0].getContents())) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件编码为空!");
            throw e;
        }
        if ("" == CommonUtils.checkNull(cells[1].getContents())) {
            BizException e = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件订购数量为空!");
            throw e;
        }
        request.setAttribute("partOldcode", CommonUtils.checkNull(cells[0].getContents()));
        request.setAttribute("uploadFlag", "upload");
        String dealerId = "";

        request.setAttribute("bookDealerId", CommonUtils.checkNull(request.getParamValue("dealerId")));


        TmpPartUploadPO po = new TmpPartUploadPO();
        List<Map<String, Object>> tempList = dao.getTmpData(CommonUtils.checkNull(cells[0].getContents()), CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")));
        if (tempList.size() > 0) {
            return;
        }
        po.setPartOldcode(CommonUtils.checkNull(cells[0].getContents()));
        if ("" != CommonUtils.checkNull(cells[1].getContents())) {
            try {
                po.setQty(Long.valueOf(cells[1].getContents()));

            } catch (Exception e) {
                BizException ex = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订购数量格式错误!");
                throw ex;
            }
        }
        //新增备注
        if (cells.length > 2 && "" != CommonUtils.checkNull(cells[2].getContents()) && null != CommonUtils.checkNull(cells[2].getContents())) {
            po.setRemark(cells[2].getContents());
        }
        dao.insert(po);
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

    //后修改发运类型为配置！！！
    public Map<String, Object> getTransMap() throws Exception {
        List<TtPartFixcodeDefinePO> list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);
        Map<String, Object> transMap = new HashMap<String, Object>();
        for (TtPartFixcodeDefinePO po : list) {
            //list.fixValue }">${list.fixName
            transMap.put(po.getFixValue(), po.getFixName());
        }
        return transMap;
    }

    /**
     *
     */
    public void partReplaceValidate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            boolean replaceFlag = false;
            Map<String, Object> reMap = new HashMap<String, Object>();
            String[] partIdArr = request.getParamValues("cb");
            String code = CommonUtils.checkNull(request.getParamValue("code"));
            List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
            if (null != partIdArr) {
                for (int i = 0; i < partIdArr.length; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    String partId = partIdArr[i];
                    List<Map<String, Object>> list = dao.getReplacePart(partId);
                    if (list.size() > 0) {
                        map.put("partId", partId);
                        map.put("rePartCode", CommonUtils.checkNull(list.get(0).get("REPART_CODE")));
                        reList.add(map);
                        replaceFlag = true;
                    }
                }
            }
            reMap.put("code", code);
            reMap.put("replaceFlag", replaceFlag);
            reMap.put("reList", reList);
            act.setOutData("ps", reMap);
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "设变错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     *订单提报--提交
     */
    public void batchRep() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            String[] orderIdArr = CommonUtils.checkNull(request.getParamValue("ar")).split(",");
            if (null != orderIdArr) {
                for (int i = 0; i < orderIdArr.length; i++) {
                    String orderId = orderIdArr[i];
                    Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
                    String dealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
                    String parentId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
                    String amount = CommonUtils.checkNull(mainMap.get("AMOUNT")).replace(",", "");
                    String orderCode = CommonUtils.checkNull(mainMap.get("ORDER_CODE"));
                    String accountId = CommonUtils.checkNull(mainMap.get("ACCOUNT_ID"));
                    //资金账户初始化
                    this.dealerAccoutMng(dealerId, parentId);
                    //获取账户余额等
                    Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, accountId);
                    if (!accountContorl() && parentId.equals(Constant.OEM_ACTIVITIES.toString())) {
                        if (null != acountMap) {
                            if (!this.validateSum(dealerId, parentId, amount, accountId)) {
                                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!当前余额:" + CommonUtils.checkNull(acountMap.get("ACCOUNT_KY")));
                                throw e1;
                            }
                        }
                    }
                    request.setAttribute("orderId", orderId);
                    request.setAttribute("orderCode", orderCode);
                    //更新销售订单主表
                    TtPartDlrOrderMainPO newPo = new TtPartDlrOrderMainPO();
                    newPo.setOrderId(Long.valueOf(orderId));
                    newPo.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);
                    newPo.setSubmitBy(loginUser.getUserId());
                    newPo.setSubmitDate(new Date());
                    
                    TtPartDlrOrderMainPO oldPo = new TtPartDlrOrderMainPO();
                    oldPo.setOrderId(Long.valueOf(orderId));
                    dao.update(oldPo, newPo);
                    //占用资金
                    if (null != mainMap.get("ACCOUNT_SUM") && !"null".equals(CommonUtils.checkNull(mainMap.get("ACCOUNT_SUM"))) && !"".equals(CommonUtils.checkNull(mainMap.get("ACCOUNT_SUM")))) {
                        this.insertAccount(dealerId, parentId, Double.valueOf(amount), Long.valueOf(orderId), orderCode, loginUser, accountId);
                    }
                    this.saveHistory(request, act, Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);
                }
            }
            act.setOutData("success", "提交成功!");
            POContext.endTxn(true);
        } catch (Exception e) {
            POContext.endTxn(false);
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "提交错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    //订单提报  修改保存   作废 都需要校验状态是否是当前允许状态
    public boolean validateState(String orderId, String[] allowState) {
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        Map<String, Object> mainMap = dao.queryPartDlrOrderMain(orderId);
        List<String> tempList = Arrays.asList(allowState);
        if (tempList.contains(CommonUtils.checkNull(mainMap.get("STATE").toString()))) {
            return false;
        } else {
            return true;
        }

    }

    public boolean accountContorl() {
        String key = CommonDAO.getPara(Constant.PART_SALE_ACCOUNT_CONTROL + "");
        if (key.equals("0")) {
            return true;
        }
        return false;
    }

    /**
     * 账号信息，返回账户ID
     * @param dealerID采购单位id
     * @param sellerId销售单位id
     */
    public Long dealerAccoutMng(String dealerID, String sellerId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        
        //资金账户
        TtPartAccountDefinePO accountDefinePO = new TtPartAccountDefinePO();
        accountDefinePO.setChildorgId(Long.valueOf(dealerID));
        accountDefinePO.setParentorgId(Long.valueOf(sellerId));
        accountDefinePO.setAccountPurpose(Constant.PART_ACCOUNT_PURPOSE_TYPE_01);
        
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
            definePO.setAccountPurpose(Constant.PART_ACCOUNT_PURPOSE_TYPE_01);
            dao.insert(definePO);
            return accountId;
        } else {
            return ((TtPartAccountDefinePO) dao.select(accountDefinePO).get(0)).getAccountId();
        }
    }

    /**
     * @param dealerId
     * @param sellerId
     * @param aMount
     * @param orderId
     * @param orderCode
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
