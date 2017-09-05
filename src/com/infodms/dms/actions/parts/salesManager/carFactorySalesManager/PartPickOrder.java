package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartPickOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartPkgDao;
import com.infodms.dms.dao.parts.salesManager.PartSoManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartSoMainPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class PartPickOrder extends BaseImport {
    public Logger logger = Logger.getLogger(PartPickOrder.class);
    PartPickOrderDao dao = PartPickOrderDao.getInstance();

    /**
     * 装箱单管理-查询
     */
    private String mainUrl = "/jsp/parts/salesManager/carFactorySalesManager/partPickOrderMain.jsp";
    /**
     * 打印拣货单
     */
    private String printUrl = "/jsp/parts/salesManager/carFactorySalesManager/partPickOrderPrint.jsp";
    /**
     * 打印装箱单
     */
    private String pkgPrintUrl = "/jsp/parts/salesManager/carFactorySalesManager/partPKgOrderPrint.jsp";
    
    private String detailUrl = "/jsp/parts/salesManager/carFactorySalesManager/partPickOrderDetail.jsp";
    
    private String partDetailUrl = "/jsp/parts/salesManager/carFactorySalesManager/pickOrderDetail.jsp";

    /**
     * 打印发运计划单
     */
    String TRANS_1 = "/jsp/parts/salesManager/carFactorySalesManager/transOrderPrint_1.jsp";
    /**
     * 批量打印发运计划单
     */
    String batchPrintUrl = "/jsp/parts/salesManager/carFactorySalesManager/transOrderBatchPrint_1.jsp";
    
    String TRANS_2 = "/jsp/parts/salesManager/carFactorySalesManager/transOrderPrint_2.jsp";

    /**
     * 拣货单、装箱单打印-初始化
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件装箱初始化
     */
    public void PartPickOrderInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse2(orgId.toString());

            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式

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
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("transList", list);
            act.setOutData("wareHouseList", wareHouseList);
            if (flag.equals("pickPrint")) {
                act.setForword(printUrl);//打印拣货单
            } else if (flag.equals("pkgPrint")) {
                act.setForword(pkgPrintUrl);//打印装箱单
            } else {
                act.setForword(mainUrl);//
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "合并提货单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 拣货单查询-打印拣货单
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 拣货单查询
     */
    public void partPickOrderQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            //查询拣货单
            PageResult<Map<String, Object>> ps = dao.queryPickOrder(request, curPage, Constant.PAGE_SIZE);
            
//            saveQueryCondition();
            
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void partPickOrderDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            List<Map<String, Object>> soOrderList = dao.getSoMainList(pickOrderId);
            act.setOutData("soOrderList", soOrderList);
            act.setForword(detailUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void pickOrderDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            PartSoManageDao dao = PartSoManageDao.getInstance();
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
            act.setOutData("mainMap", mainMap);
            act.setOutData("detailList", detailList);
            act.setForword(partDetailUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void printTransOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPkgDao partPkgDao = PartPkgDao.getInstance();
        Map<String, Object> dataMap = new HashMap();
        String forward = TRANS_1;
        PartPkgDao pkgdao = PartPkgDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
            String sellerId = "";
            // 判断是否为车厂 PartWareHouseDao
            PartWareHouseDao dao1 = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = dao1.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                sellerId = beanList.get(0).getOrgId() + "";
            }
            //List<Map<String,Object>> soMainList = partPkgDao.getSoMain(pickOrderId);
            List<Map<String, Object>> transMainList = partPkgDao.getTransMain(sellerId, transId);
            pkgdao.updateTranInfo(pickOrderId);//更新发运单打印
            List detailList = new ArrayList();

            if (null != transMainList && transMainList.size() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String transCode = "";
                String whName = "";
                String dealerName = "";
                String dealerCode = "";
                String addr = "";
                String createDate = "";
                String phone = "";
                String linkName = "";
                String transType = "";
                String driveDate = "";
                String recvDate = "";

                Map<String, Object> transMainMap = transMainList.get(0);

                transCode = CommonUtils.checkNull(transMainMap.get("LOGISTICS_NO"));
                dealerName = CommonUtils.checkNull(transMainMap.get("DEALER_NAME"));
                dealerCode = CommonUtils.checkNull(transMainMap.get("DEALER_CODE"));
                whName = CommonUtils.checkNull(transMainMap.get("WH_NAME"));
                transType = CommonUtils.checkNull(transMainMap.get("ORDER_TRANS_TYPE"));
                driveDate = CommonUtils.checkNull(transMainMap.get("TRANS_DATE"));
                recvDate = CommonUtils.checkNull(transMainMap.get("TRANS_DATE"));
                addr = CommonUtils.checkNull(transMainMap.get("ADDR"));
                phone = CommonUtils.checkNull(transMainMap.get("TEL"));
                linkName = CommonUtils.checkNull(transMainMap.get("RECEIVER"));
                createDate = sdf.format(new Date());

                dataMap.put("transCode", transCode);
                dataMap.put("whName", whName);
                dataMap.put("dealerName", dealerName);
                dataMap.put("dealerCode", dealerCode);
                dataMap.put("whName", whName);
                dataMap.put("transType", transType);
                dataMap.put("driveDate", driveDate);
                dataMap.put("recvDate", recvDate);
                dataMap.put("addr", addr);
                dataMap.put("phone", phone);
                dataMap.put("linkName", linkName);
                dataMap.put("createDate", createDate);
                dataMap.put("pickOrderId", pickOrderId);

                List<Map<String, Object>> tempDetailList = partPkgDao.getTransDtl(pickOrderId, transId);
                detailList.add(tempDetailList);
                dataMap.put("detailList", detailList);
                List list = new ArrayList();
                List tempList = new ArrayList();
                int listAcount = 0;
                int dtlListAct = tempDetailList.size() % 15;//用于确定最后一页明细行数
                act.setOutData("dtlListAct", dtlListAct);
                //目前不分页
                if (tempDetailList.size() > 15) {
                    for (int i = 0; i < tempDetailList.size(); i++) {
                        tempList.add(tempDetailList.get(i));
                        if (tempList.size() == 15) {
                            list.add(tempList);
                            tempList = new ArrayList();
                        }
                        if (i == tempDetailList.size() - 1) {
                            list.add(tempList);
                        }
                    }
                    dataMap.put("detailList", list);
                }
                if (list.size() == 0) {
                    act.setOutData("listAcount", list.size() + 1);
                } else {
                    act.setOutData("listAcount", list.size());
                }

                String boxNum = "0";
                String volume = "0";
                String weight = "0";
                String eqWeight = "0";
                String chWeight = "0";

                List<Map<String, Object>> cuntDetailList = partPkgDao.getTransDtlAmount(pickOrderId, transId);

                if (null != cuntDetailList && cuntDetailList.size() > 0) {
                    boxNum = CommonUtils.checkNull(cuntDetailList.get(0).get("BOX_NUM"));
                    volume = CommonUtils.checkNull(cuntDetailList.get(0).get("VOLUME_SUM"));
                    weight = CommonUtils.checkNull(cuntDetailList.get(0).get("WEIGHT_SUM"));
                    eqWeight = CommonUtils.checkNull(cuntDetailList.get(0).get("EQ_WEIGHT_SUM"));
                    chWeight = CommonUtils.checkNull(cuntDetailList.get(0).get("CH_WEIGHT_SUM"));
                }

                dataMap.put("boxNum", boxNum);
                dataMap.put("volume", volume);
                dataMap.put("weight", weight);
                dataMap.put("eqWeight", eqWeight);
                dataMap.put("chWeight", chWeight);
            }
            act.setOutData("dataMap", dataMap);
            act.setForword(forward);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 打印发运计划单
     */
    public void printTransPlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPkgDao partPkgDao = PartPkgDao.getInstance();
        Map<String, Object> dataMap = new HashMap();
        String forward = TRANS_1;
        PartPkgDao pkgdao = PartPkgDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String trplanId = CommonUtils.checkNull(request.getParamValue("trplanId"));
            String sellerId = "";
            pkgdao.updatePkgWeightInfo("");
            // 判断是否为车厂 PartWareHouseDao
            PartWareHouseDao dao1 = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = dao1.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                sellerId = beanList.get(0).getOrgId() + "";
            }
            //List<Map<String,Object>> soMainList = partPkgDao.getSoMain(pickOrderId);
            List<Map<String, Object>> transMainList = partPkgDao.getTransPlanMain(sellerId, trplanId);
            //更新发运单打印
            pkgdao.updateTranInfo(pickOrderId);
            
            List detailList = new ArrayList();

            if (null != transMainList && transMainList.size() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String transCode = "";
                String whName = "";
                String dealerName = "";
                String dealerCode = "";
                String addr = "";
                String createDate = "";
                String phone = "";
                String linkName = "";
                String transType = "";
                String driveDate = "";
                String recvDate = "";
                String orderCode = "";
                String companyName = "";

                Map<String, Object> transMainMap = transMainList.get(0);

                transCode = CommonUtils.checkNull(transMainMap.get("LOGISTICS_NO"));
                dealerName = CommonUtils.checkNull(transMainMap.get("DEALER_NAME"));
                dealerCode = CommonUtils.checkNull(transMainMap.get("DEALER_CODE"));
                whName = CommonUtils.checkNull(transMainMap.get("WH_NAME"));
                transType = CommonUtils.checkNull(transMainMap.get("ORDER_TRANS_TYPE"));
                driveDate = CommonUtils.checkNull(transMainMap.get("TRANS_DATE"));
                recvDate = CommonUtils.checkNull(transMainMap.get("TRANS_DATE"));
                addr = CommonUtils.checkNull(transMainMap.get("ADDR"));
                phone = CommonUtils.checkNull(transMainMap.get("TEL"));
                linkName = CommonUtils.checkNull(transMainMap.get("RECEIVER"));
                orderCode = CommonUtils.checkNull(transMainMap.get("ORDER_CODE"));
                createDate = sdf.format(new Date());

                if (loginUser.getDealerId() == null) {
                    TmCompanyPO tmCompanyPO = new TmCompanyPO();
                    tmCompanyPO.setCompanyType(Integer.valueOf(Constant.COMPANY_TYPE_SGM));
                    companyName = ((TmCompanyPO) partPkgDao.select(tmCompanyPO).get(0)).getCompanyName();
                } else {
                    TmDealerPO tmDealerPO = new TmDealerPO();
                    tmDealerPO.setDealerId(Long.valueOf(loginUser.getDealerId()));
                    companyName = ((TmDealerPO) partPkgDao.select(tmDealerPO).get(0)).getDealerName();

                }

                dataMap.put("transCode", transCode);
                dataMap.put("whName", whName);
                dataMap.put("dealerName", dealerName);
                dataMap.put("dealerCode", dealerCode);
                dataMap.put("whName", whName);
                dataMap.put("transType", transType);
                dataMap.put("driveDate", driveDate);
                dataMap.put("recvDate", recvDate);
                dataMap.put("addr", addr);
                dataMap.put("phone", phone);
                dataMap.put("linkName", linkName);
                dataMap.put("createDate", createDate);
                dataMap.put("pickOrderId", pickOrderId);
                dataMap.put("companyName", companyName);
                dataMap.put("orderCode", orderCode);

                List<Map<String, Object>> tempDetailList = partPkgDao.getTransPlanDtl(trplanId);
                detailList.add(tempDetailList);
                dataMap.put("detailList", detailList);
                List list = new ArrayList();
                List tempList = new ArrayList();
                int listAcount = 0;
                int dtlListAct = tempDetailList.size() % 15;//用于确定最后一页明细行数
                act.setOutData("dtlListAct", dtlListAct);
                //目前不分页
                if (tempDetailList.size() > 15) {
                    for (int i = 0; i < tempDetailList.size(); i++) {
                        tempList.add(tempDetailList.get(i));
                        if (tempList.size() == 15) {
                            list.add(tempList);
                            tempList = new ArrayList();
                        }
                        if (i == tempDetailList.size() - 1) {
                            list.add(tempList);
                        }
                    }
                    dataMap.put("detailList", list);
                }
                if (list.size() == 0) {
                    act.setOutData("listAcount", list.size() + 1);
                } else {
                    act.setOutData("listAcount", list.size());
                }

                String boxNum = "0";
                String volume = "0";
                String weight = "0";
                String eqWeight = "0";
                String chWeight = "0";

                List<Map<String, Object>> cuntDetailList = partPkgDao.getTransPlanDtlAmount(trplanId);

                if (null != cuntDetailList && cuntDetailList.size() > 0) {
                    boxNum = CommonUtils.checkNull(cuntDetailList.get(0).get("BOX_NUM"));
                    volume = CommonUtils.checkNull(cuntDetailList.get(0).get("VOLUME_SUM"));
                    weight = CommonUtils.checkNull(cuntDetailList.get(0).get("WEIGHT_SUM"));
                    eqWeight = CommonUtils.checkNull(cuntDetailList.get(0).get("EQ_WEIGHT_SUM"));
                    chWeight = CommonUtils.checkNull(cuntDetailList.get(0).get("CH_WEIGHT_SUM"));
                }

                dataMap.put("boxNum", boxNum);
                dataMap.put("volume", volume);
                dataMap.put("weight", weight);
                dataMap.put("eqWeight", eqWeight);
                dataMap.put("chWeight", chWeight);
            }
            //更新打印信息
            partPkgDao.updateTransPlanPrintInfo(trplanId, loginUser.getUserId().toString());
            act.setOutData("dataMap", dataMap);
            act.setForword(forward);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "拣货单查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 批量打印发运计划单
     */
    public void batchPrintTransPlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        PartPkgDao pkgdao = PartPkgDao.getInstance();
        PartPkgDao partPkgDao = PartPkgDao.getInstance();

        pkgdao.updatePkgWeightInfo("");

        List<Map<String, Object>> batchDatas = new ArrayList<Map<String, Object>>();
        try {
            String[] ids = request.getParamValues("cb");
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    String[] id = ids[i].split(":");
                    String trplanId = id[0];
                    String pickOrderId = id[1];

                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    int indexNO = 1;
                    String sellerId = "";
                    // 判断是否为车厂 PartWareHouseDao
                    PartWareHouseDao dao1 = PartWareHouseDao.getInstance();
                    List<OrgBean> beanList = dao1.getOrgInfo(loginUser);
                    if (null != beanList && beanList.size() >= 0) {
                        sellerId = beanList.get(0).getOrgId() + "";
                    }
                    //更新发运单打印
                    pkgdao.updateTranInfo(pickOrderId);
                    //获取打印表头信息
                    List<Map<String, Object>> transMainList = partPkgDao.getTransPlanMain(sellerId, trplanId);
                    
                    if (null != transMainList && transMainList.size() > 0) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                        String createDate = sdf.format(new Date());
                        String transCode = "";
                        String whName = "";
                        String dealerName = "";
                        String dealerCode = "";
                        String addr = "";
                        String phone = "";
                        String linkName = "";
                        String transType = "";
                        String driveDate = "";
                        String recvDate = "";
                        String companyName = "";
                        String orderCode = "";
                        String transportOrg = "";
                        String orderType = "";

                        Map<String, Object> transMainMap = transMainList.get(0);
                        transCode = CommonUtils.checkNull(transMainMap.get("LOGISTICS_NO"));
                        dealerName = CommonUtils.checkNull(transMainMap.get("DEALER_NAME"));
                        dealerCode = CommonUtils.checkNull(transMainMap.get("DEALER_CODE"));
                        whName = CommonUtils.checkNull(transMainMap.get("WH_NAME"));
                        transType = CommonUtils.checkNull(transMainMap.get("ORDER_TRANS_TYPE"));
                        driveDate = CommonUtils.checkNull(transMainMap.get("TRANS_DATE"));
                        recvDate = CommonUtils.checkNull(transMainMap.get("TRANS_DATE"));
                        addr = CommonUtils.checkNull(transMainMap.get("ADDR"));
                        phone = CommonUtils.checkNull(transMainMap.get("TEL"));
                        linkName = CommonUtils.checkNull(transMainMap.get("RECEIVER"));
                        orderCode = CommonUtils.checkNull(transMainMap.get("ORDER_CODE"));
                        transportOrg = CommonUtils.checkNull(transMainMap.get("TRANSPORT_ORG"));
                        orderType = CommonUtils.checkNull(transMainMap.get("ORDER_TYPE"));

                        if (loginUser.getDealerId() == null) {
                            TmCompanyPO tmCompanyPO = new TmCompanyPO();
                            tmCompanyPO.setCompanyType(Integer.valueOf(Constant.COMPANY_TYPE_SGM));
                            companyName = ((TmCompanyPO) partPkgDao.select(tmCompanyPO).get(0)).getCompanyName();
                        } else {
                            TmDealerPO tmDealerPO = new TmDealerPO();
                            tmDealerPO.setDealerId(Long.valueOf(loginUser.getDealerId()));
                            companyName = ((TmDealerPO) partPkgDao.select(tmDealerPO).get(0)).getDealerName();

                        }

                        dataMap.put("transCode", transCode);
                        dataMap.put("whName", whName);
                        dataMap.put("dealerName", dealerName);
                        dataMap.put("dealerCode", dealerCode);
                        dataMap.put("whName", whName);
                        dataMap.put("transType", transType);
                        dataMap.put("driveDate", driveDate);
                        dataMap.put("recvDate", recvDate);
                        dataMap.put("addr", addr);
                        dataMap.put("phone", phone);
                        dataMap.put("linkName", linkName);
                        dataMap.put("createDate", createDate);
                        dataMap.put("pickOrderId", pickOrderId);
                        dataMap.put("companyName", companyName);
                        dataMap.put("orderCode", orderCode);
                        dataMap.put("transportOrg", transportOrg);
                        dataMap.put("orderType", orderType);
                        //获取箱子信息
                        List<Map<String, Object>> cuntDetailList = partPkgDao.getTransPlanDtlAmount(trplanId);
                        String boxNum = "0";
                        String volume = "0";
                        String weight = "0";
                        String eqWeight = "0";
                        String chWeight = "0";
                        if (null != cuntDetailList && cuntDetailList.size() > 0) {
                            boxNum = CommonUtils.checkNull(cuntDetailList.get(0).get("BOX_NUM"));
                            volume = CommonUtils.checkNull(cuntDetailList.get(0).get("VOLUME_SUM"));
                            weight = CommonUtils.checkNull(cuntDetailList.get(0).get("WEIGHT_SUM"));
                            eqWeight = CommonUtils.checkNull(cuntDetailList.get(0).get("EQ_WEIGHT_SUM"));
                            chWeight = CommonUtils.checkNull(cuntDetailList.get(0).get("CH_WEIGHT_SUM"));
                        }
                        dataMap.put("boxNum", boxNum);
                        dataMap.put("volume", volume);
                        dataMap.put("weight", weight);
                        dataMap.put("eqWeight", eqWeight);
                        dataMap.put("chWeight", chWeight);

                        List<Map<String, Object>> tempDetailList = partPkgDao.getTransPlanDtl(trplanId);//获取明细信息
                        int pageSize = 15;
                        int dtlListAct = tempDetailList.size() % pageSize;//用于确定最后一页明细行数
                        ArrayList<String> kongList = new ArrayList<String>();
                        if (dtlListAct != 0) {
                            for (int k = 0; k < (pageSize - dtlListAct); k++) {
                                kongList.add((k + 1) + "");
                            }
                        }
                        dataMap.put("dtlListAct", kongList);

                        List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
                        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
                        for (int k = 0; k < tempDetailList.size(); k++) {//indexNO
                            Map<String, Object> row = tempDetailList.get(k);
                            row.put("indexNO", indexNO);
                            tempList.add(row);
                            indexNO++;
                            if (tempList.size() == pageSize) {
                                list.add(tempList);
                                tempList = new ArrayList<Map<String, Object>>();
                            } else if (tempDetailList.size() == (k + 1)) {
                                list.add(tempList);
                            }
                        }
                        dataMap.put("detailList", list);
                        dataMap.put("listAcount", list.size());
//                        act.setOutData("listAcount", list.size());
                    }
                    indexNO = 1;
                    batchDatas.add(dataMap);
                    //更新打印信息
                    partPkgDao.updateTransPlanPrintInfo(trplanId, loginUser.getUserId().toString());
                }
            }
            act.setOutData("batchDatas", batchDatas);
            act.setForword(batchPrintUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    private void saveQueryCondition() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//单号
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//单号
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//单号
        String startDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//开始
        String endDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//结束
        String printFlag = CommonUtils.checkNull(request.getParamValue("printFlag"));//打印标识
        String transFlag = CommonUtils.checkNull(request.getParamValue("TransFlag"));//打印标识
        String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//发运方式
        String isPkg = CommonUtils.checkNull(request.getParamValue("IsPkg"));//是否已装箱
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pickOrderId", pickOrderId);
        map.put("dealerName", dealerName);
        map.put("dealerCode", dealerCode);
        map.put("whId", whId);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("printFlag", printFlag);
        map.put("transFlag", transFlag);
        map.put("transType", transType);
        map.put("isPkg", isPkg);
        act.getSession().set("condition", map);
    }

    /**
     * 拣货单明细导出
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        PartPkgDao partPkgDao = PartPkgDao.getInstance();
        try {
            String pickOrderId = request.getParamValue("pickOrderId");
            List<List<Object>> list = new LinkedList<List<Object>>();

            List<Object> listHead = new LinkedList<Object>();
            listHead.add("货位");
            listHead.add("批次");
            listHead.add("配件编码");
            listHead.add("配件名称");
            listHead.add("单位");
//            listHead.add("当前库存");
            listHead.add("订货数量");
            listHead.add("拣货数量");
            list.add(listHead);

            List<Map<String, Object>> soMainList = partPkgDao.getSoMain(pickOrderId);
            String soIds = "";
            String whId = "";
            for (int i = 0; i < soMainList.size(); i++) {
                Map<String, Object> mainMap = soMainList.get(i);
                whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
                String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
                soIds = soIds + "," + soId;
            }
            if (!"".equals(soIds)) {
                soIds = soIds.replaceFirst(",", "");
            }
            List<Map<String, Object>> tempDetailList = partPkgDao.getDtlLoc(soIds, whId);
            for (Map<String, Object> map : tempDetailList) {
                List<Object> listRowData = new LinkedList<Object>();
                listRowData.add(map.get("LOC").toString());
                listRowData.add((map.get("BATCH_NO")+"")==null || (map.get("BATCH_NO")+"").equals("null") ?"":(map.get("BATCH_NO")+""));
                listRowData.add(map.get("PART_OLDCODE").toString());
                listRowData.add(map.get("PART_CNAME").toString());
                listRowData.add(map.get("UNIT").toString());
//                listRowData.add(map.get("NORMAL_QTY_NOW").toString());
                listRowData.add(map.get("SALES_QTY").toString());
                listRowData.add(map.get("BOOKED_QTY").toString());
                list.add(listRowData);
            }

            String fileName = "配件拣配单[" + pickOrderId + "]明细信息.xls";
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            ResponseWrapper response = act.getResponse();
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();

            CsvWriterUtil.createXlsFile(list, os);

            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出[ 配件拣配单细节信息 ] EXECEL数据错误");
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
     * 驳回到销售单
     */
    public void rejectOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPkgDao dao = PartPkgDao.getInstance();
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("SO_ID"));
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setSoId(Long.valueOf(soId));
            
            if (((TtPartSoMainPO) dao.select(soMainPO).get(0)).getPickOrderId() != null) {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "取消合并提货单错误,请联系管理员!");
                act.setOutData("error", "【" + soMainPO.getSoCode() + "】已经开始拣货，不能取消！请先取消拣货，然后再驳回!");
                return;
            }

            TtPartSoMainPO updatePo = new TtPartSoMainPO();
            updatePo.setState(Constant.CAR_FACTORY_ORDER_CHECK_STATE_01);
            
            dao.update(soMainPO, updatePo);
            
            act.setOutData("success", "驳回成功!");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "驳回销售单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
}