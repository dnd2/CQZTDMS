package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
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
import com.infodms.dms.dao.parts.purchaseManager.partPlanConfirm.PartPlanConfirmDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.*;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class PartSalesOrderFinCheck extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 销售单财务审核初始化
     */
    public void partSalesOrderFinCheckInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(loginUser.getUserId());
            request.setAttribute("wareHouseList", wareHouseList);
            act.setForword(PART_DLR_ORDER_FIN_CHECK_MAIN);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单财务审核初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
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
    public void partSalesOrderFinCheckQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartSalesOrderFinCheckDao dao = PartSalesOrderFinCheckDao.getInstance();
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartSoOrder(request, orgId, curPage, Constant.PAGE_SIZE);
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
     * @Description: 订单详细
     */
    public void detailOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String soId = CommonUtils.checkNull(request.getParamValue("soId"));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
            PartSoManageDao dao = PartSoManageDao.getInstance();
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
            List<Map<String, Object>> historyList = dao.queryOrderHistory(soCode);
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            mainMap.put("TRANS_TYPE", transType);
            act.setOutData("transList", list);
            act.setOutData("mainMap", mainMap);
            act.setOutData("detailList", detailList);
            act.setOutData("historyList", historyList);
            act.setForword(PART_DLR_ORDER_FIN_CHECK_DETAIL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售单查看详细数据出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

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
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存历史记录
     */
    public void saveHistory(int status, String soCode, String orderId) throws Exception {
        ActionContext act = ActionContext.getContext();

        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {

            TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
            Long optId = Long.parseLong(SequenceManager.getSequence(""));
            po.setBussinessId(soCode);
            po.setOptId(optId);
            po.setOptBy(loginUser.getUserId());
            po.setOptDate(new Date());
            po.setOptType(Constant.PART_OPERATION_TYPE_02);
            po.setWhat("配件销售单");
            po.setOptName(loginUser.getName());
            if (!"".equals(CommonUtils.checkNull(orderId))) {
                po.setOrderId(Long.valueOf(orderId));
            }
            po.setStatus(status);
            dao.insert(po);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 通过或驳回动作
     */
    public void partSalesOrderFinCheckActions() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            PartSoManageDao dao = PartSoManageDao.getInstance();
            String state = request.getParamValue("state");
            String[] soIdArr = request.getParamValues("cb");
            String remark = request.getParamValue("remark");

            for (int i = 0; i < soIdArr.length; i++) {
                Map<String, Object> mainMap = dao.getSoOrderMain(soIdArr[i]);
                String orderType = CommonUtils.checkNull(mainMap.get("ORDER_TYPE"));

                //如果是直发   并且通过
                if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "") && state.equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "")) {
                    String key = CommonDAO.getPara(Constant.PART_SALE_STOCK_CONTROL.toString());
                    /*if(key.equals("1")){
                         ArrayList ins = new ArrayList();
					     ins.add(0, soIdArr[i]);
					     ins.add(1, 1);  //是否需要发运单 1是 是
					     ins.add(2, 1); //是否虚拟出入库 1是 是
					     dao.callProcedure("PKG_PART.p_createdirectorder",ins,null);
					}else{*/
                    ArrayList ins = new ArrayList();
                    ins.add(0, soIdArr[i]);
                    ins.add(1, 1);  //是否需要发运单 1是 是
                    ins.add(2, 0); //是否虚拟出入库 1是 是
                    dao.callProcedure("PKG_PART.p_createdirectorder", ins, null);
                    /*}*/
                }
                String orderId = CommonUtils.checkNull(mainMap.get("ORDER_ID"));
                TtPartSoMainPO po = new TtPartSoMainPO();
                po.setSoId(Long.valueOf(soIdArr[i]));
                po.setState(Integer.valueOf(state));
                if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "") && state.equals(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "")) {
                    po.setState(Constant.CAR_FACTORY_TRANS_STATE_01);
                }
                po.setFcRemark(remark);
                po.setFcauditBy(loginUser.getUserId());
                po.setFcauditDate(new Date());
                TtPartSoMainPO oldPo = new TtPartSoMainPO();
                oldPo.setSoId(Long.valueOf(soIdArr[i]));

                dao.update(oldPo, po);
                saveHistory(Integer.valueOf(state), CommonUtils.checkNull(mainMap.get("SO_CODE")), orderId);

               /* //财务审核通过后释放预占金额
                TtPartSoMainPO mainPO = new TtPartSoMainPO();
                mainPO.setSoId(Long.valueOf(soIdArr[i]));
                TtPartSoMainPO soMainPO =(TtPartSoMainPO)dao.select(mainPO).get(0);

                insertAccount(soMainPO.getDealerId().toString(),soMainPO.getSellerId().toString(),-soMainPO.getAmount(),soMainPO.getOrderId(),soMainPO.getOrderCode(),loginUser);
                TtPartAccountDefinePO accountDefinePO = new TtPartAccountDefinePO();
                accountDefinePO.setChildorgId(soMainPO.getDealerId());
                accountDefinePO.setParentorgId(soMainPO.getSellerId());
                accountDefinePO.setState(Constant.STATUS_ENABLE);
                accountDefinePO.setStatus(1);

                TtPartAccountDefinePO definePO = (TtPartAccountDefinePO)dao.select(accountDefinePO).get(0);

                TtPartAccountDefinePO definePO1 = new TtPartAccountDefinePO();;
                definePO1.setAccountSum(definePO.getAccountSum()-soMainPO.getAmount());

                dao.update(accountDefinePO,definePO1);*/

            }
            POContext.endTxn(true);
            act.setOutData("success", "success");
            //act.setForword(PART_DLR_ORDER_FIN_CHECK_MAIN);
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "修改订单状态出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        } finally {
            try {
                POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "修改订单状态出错,请联系管理员!");
            }
        }

    }

    public void insertTtPartPoIn(String soId) throws Exception {
        String key = CommonDAO.getPara(Constant.PART_SALE_STOCK_CONTROL.toString());
        if (key.equals("1")) {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            PartSoManageDao dao = PartSoManageDao.getInstance();
            PartBaseQueryDao partBaseQueryDao = PartBaseQueryDao.getInstance();
            PartSalesOrderFinCheckDao partSalesOrderFinCheckDao = PartSalesOrderFinCheckDao.getInstance();
            Map<String, Object> mainMap = dao.getSoOrderMain(soId);
            List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
            PartPlanConfirmDao partPlanConfirmDao = PartPlanConfirmDao.getInstance();
            for (Map<String, Object> map : detailList) {
                TtPartPoInPO inPo = new TtPartPoInPO();//需要将每次入库的信息保存到入库表中
                //设置入库信息
                Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
                inPo.setInId(InId);
                String inCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_04);//入库单编码
                inPo.setInCode((inCode));
                inPo.setInType(Constant.PART_PURCHASE_ORDERCIN_TYPE_02);
                    /*}*/
                inPo.setCheckId(Long.parseLong(SequenceManager.getSequence("")));//验货ID
                inPo.setPoId(Long.valueOf(CommonUtils.checkNull(mainMap.get("ORDER_ID"))));//采购订单iD
                inPo.setOrderCode(CommonUtils.checkNull(mainMap.get("ORDER_CODE")));//采购订单号

                inPo.setBuyerId(Long.valueOf(CommonUtils.checkNull(mainMap.get("BUYER_ID"))));//采购员ID
                String partId = CommonUtils.checkNull(map.get("PART_ID"));
                Map<String, Object> dtlMap = partBaseQueryDao.queryPartDetail(partId);
                if (null != dtlMap.get("PART_TYPE")) {
                    inPo.setPartType(Integer.valueOf(CommonUtils.checkNull(dtlMap.get("PART_TYPE"))));//配件类型
                }
                inPo.setPartId(Long.valueOf(partId));//配件ID
                inPo.setPartCode(CommonUtils.checkNull(map.get("PART_CODE")));//配件件号
                inPo.setPartOldcode(CommonUtils.checkNull(map.get("PART_OLDCODE")));//配件编码
                inPo.setPartCname(CommonUtils.checkNull(map.get("PART_CNAME")));//配件名称
                inPo.setUnit(CommonUtils.checkNull(map.get("UNIT")));//配件包装单位
                inPo.setVenderId(Long.valueOf(CommonUtils.checkNull(map.get("VENDER_ID"))));//配件供应商ID
                inPo.setVenderCode(partPlanConfirmDao.getVenderCode(inPo.getVenderId() + ""));//配件供应商编码

                inPo.setVenderName(CommonUtils.checkNull(map.get("VENDER_NAME")));//配件供应商名称

                String price = partSalesOrderFinCheckDao.getPrice(CommonUtils.checkNull(map.get("VENDER_ID")), partId);

                if (!"".equals(price)) {
                    inPo.setBuyPrice(Double.valueOf(price));
                }
                inPo.setBuyQty(Long.valueOf(CommonUtils.checkNull(map.get("BUY_QTY"))));//采购数量
                inPo.setCheckQty(Long.valueOf(CommonUtils.checkNull(map.get("SALES_QTY"))));//验货数量
                inPo.setInQty(Long.valueOf(CommonUtils.checkNull(map.get("SALES_QTY"))));//已入库数量
                if (!"".equals(price)) {
                    inPo.setBuyPriceNotax(Arith.mul(Double.valueOf(price), Arith.sub(1, Constant.PART_TAX_RATE)));//无税单价
                }
                if (!"".equals(price)) {
                    inPo.setInAmount(Arith.mul(Double.valueOf(price), inPo.getInQty()));//采购金额
                }
                inPo.setOrgId(-1L);
                inPo.setTaxRate(Constant.PART_TAX_RATE);//税率
                inPo.setInAmountNotax(Arith.mul(inPo.getBuyPriceNotax(), inPo.getInQty()));//无税总金额
                inPo.setWhId(Long.valueOf(CommonUtils.checkNull(mainMap.get("WH_ID"))));//库房iD
                String whName = partSalesOrderFinCheckDao.getWhName(CommonUtils.checkNull(mainMap.get("WH_ID")));
                inPo.setWhName(whName);//库房名称
                inPo.setInDate(new Date());
                inPo.setInBy(loginUser.getUserId());
                inPo.setCheckDate(new Date());
                inPo.setCheckBy(loginUser.getUserId());
                inPo.setCreateDate(new Date());
                inPo.setCreateBy(loginUser.getUserId());
                inPo.setVer(1);
                inPo.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
                dao.insert(inPo);
                this.insertPartRecord(map);
                ArrayList ins = new ArrayList();
                ins.add(0, InId);
                ins.add(1, Constant.PART_CODE_RELATION_04);
                ins.add(2, 1);
                dao.callProcedure("PKG_PART.p_updatepartstock", ins, null);
            }
        }
    }

    public void insertOutStockDtl(String soId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPkgDao dao = PartPkgDao.getInstance();
        PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
        Long outId = Long.parseLong(SequenceManager.getSequence(""));
        request.setAttribute("outId", outId);
        Long transId = Long.parseLong(SequenceManager.getSequence(""));
        request.setAttribute("transId", transId);
        Map<String, Object> mainMap = partSoManageDao.getSoOrderMain(soId);
        Double discount = Double.valueOf(CommonUtils.checkNull(mainMap.get("DISCOUNT")));
        Double recountMon = 0D;
        List<Map<String, Object>> detailList = partSoManageDao.getSoOrderMDetail(soId);
        for (Map<String, Object> map : detailList) {
            Long outlineId = Long.parseLong(SequenceManager.getSequence(""));
            String partId = CommonUtils.checkNull(map.get("PART_ID"));
            String partCode = CommonUtils.checkNull(map.get("PART_CODE"));
            String partOldcode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(map.get("PART_CNAME"));
            String unit = CommonUtils.checkNull(map.get("UNIT"));
            String isDirect = CommonUtils.checkNull(map.get("IS_DIRECT"));
            String isPlan = CommonUtils.checkNull(map.get("IS_PLAN"));
            String isLack = CommonUtils.checkNull(map.get("IS_LACK"));
            String isReplaced = CommonUtils.checkNull(map.get("IS_REPLACED"));
            String isGift = CommonUtils.checkNull(map.get("IS_GIFT"));
            String stockQty = CommonUtils.checkNull(map.get("STOCK_QTY"));
            String minPackage = CommonUtils.checkNull(map.get("MIN_PACKAGE"));
            String saleQty = CommonUtils.checkNull(map.get("SALES_QTY"));
            String buyPrice = CommonUtils.checkNull(map.get("BUY_PRICE"));
            String buyAmount = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
            String remark = CommonUtils.checkNull(map.get("REMARK"));
            dao.queryPkg(soId, partId);
            String outQty = CommonUtils.checkNull(saleQty);
            Double rPrice = Arith.mul(Double.valueOf(discount), Double.valueOf(buyPrice));
            Double outAmount = Arith.mul(Double.valueOf(outQty), rPrice);
            recountMon = Arith.add(recountMon, outAmount);
            String pkgedNo = CommonUtils.checkNull(saleQty);
            String boQty = "0";

            TtPartOutstockDtlPO po = new TtPartOutstockDtlPO();
            if (CommonUtils.checkNull(map.get("SO_FROM")).equals(Constant.CAR_FACTORY_SO_FORM_02 + "")) {
                po.setReservedQty(Long.valueOf(CommonUtils.checkNull(map.get("SALES_QTY"))));
            }
            po.setOutstockQty(Long.valueOf(outQty));
            po.setOutlineId(outlineId);
            po.setOutId(outId);
            po.setPackgQty(Long.valueOf(pkgedNo));
            po.setSoId(Long.valueOf(soId));
            po.setPartId(Long.valueOf(partId));
            po.setPartCode(partCode);
            po.setPartCname(partCname);
            po.setPartOldcode(partOldcode);
            po.setUnit(unit);
            po.setBoQty(Long.valueOf(boQty));
            if (!"".equals(isPlan)) {
                po.setIsPlan(Integer.valueOf(isPlan));
            }
            if (!"".equals(isDirect)) {
                po.setIsDirect(Integer.valueOf(isDirect));
            }
            if (!"".equals(isGift)) {
                po.setIsGift(Integer.valueOf(isGift));
            }
            if (!"".equals(isLack)) {
                po.setIsLack(Integer.valueOf(isLack));
            }
            if (!"".equals(isReplaced)) {
                po.setIsReplaced(Integer.valueOf(isReplaced));
            }
            po.setStockQty(Long.valueOf(stockQty));
            po.setMinPackage(Long.valueOf(minPackage));

            if (!"".equals(saleQty)) {
                po.setBuyQty(Long.valueOf(saleQty));
            }
            po.setSalePrice(Double.valueOf(buyPrice));
            po.setSaleAmount(outAmount);
            po.setRemark(remark);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            po.setVer(1);
            dao.insert(po);
            insertTtPartTransDtl(po);
        }
        request.setAttribute("recountMon", recountMon);
    }

    public void insertOutStockMain(String soId) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartSoManageDao dao = PartSoManageDao.getInstance();
        Map<String, Object> mainMap = dao.getSoOrderMain(soId);
        TtPartOutstockMainPO po = new TtPartOutstockMainPO();
        Long outId = Long.valueOf(CommonUtils.checkNull(request.getAttribute("outId")));
        String outCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_09);
        request.setAttribute("outCode", outCode);
        String soCode = CommonUtils.checkNull(mainMap.get("SO_CODE"));
        String orderType = CommonUtils.checkNull(mainMap.get("ORDER_TYPE"));
        String isBatchso = CommonUtils.checkNull(mainMap.get("IS_BATCHSO"));
        String dealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
        String dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
        String dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
        String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
        String sellerCode = CommonUtils.checkNull(mainMap.get("SELLER_CODE"));
        String sellerName = CommonUtils.checkNull(mainMap.get("SELLER_NAME"));
        String buyerId = CommonUtils.checkNull(mainMap.get("BUYER_ID"));
        String buyerName = CommonUtils.checkNull(mainMap.get("BUYER_NAME"));
        String consigneesId = CommonUtils.checkNull(mainMap.get("CONSIGNEES_ID"));
        String consignees = CommonUtils.checkNull(mainMap.get("CONSIGNEES"));
        String addrId = CommonUtils.checkNull(mainMap.get("ADDR_ID"));
        String addr = CommonUtils.checkNull(mainMap.get("ADDR"));
        String receiver = CommonUtils.checkNull(mainMap.get("RECEIVER"));
        String tel = CommonUtils.checkNull(mainMap.get("TEL"));
        String postCode = CommonUtils.checkNull(mainMap.get("POST_CODE"));
        String station = CommonUtils.checkNull(mainMap.get("STATION"));
        String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
        String amount = CommonUtils.checkNull(mainMap.get("AMOUNT"));
        String discount = CommonUtils.checkNull(mainMap.get("DISCOUNT"));
        String remark = CommonUtils.checkNull(mainMap.get("REMARK"));
        String payType = CommonUtils.checkNull(mainMap.get("PAY_TYPE"));
        String transpayType = CommonUtils.checkNull(mainMap.get("TRANSPAY_TYPE"));
        String sDate = CommonUtils.checkNull(mainMap.get("SALE_DATE"));
        String soFrom = CommonUtils.checkNull(mainMap.get("SO_FROM"));
        String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date saleDate = new Date();
        try {
            saleDate = sdf.parse(sDate);
        } catch (Exception ex) {
            saleDate = new Date();
        }
        po.setOutId(outId);
        po.setOutCode(outCode);
        po.setSoId(Long.valueOf(soId));
        po.setSoCode(soCode);
        po.setOrderType(Integer.valueOf(orderType));
        if (!"".equals(isBatchso)) {
            po.setIsBatchso(Integer.valueOf(isBatchso));
        }
        po.setSaleDate(saleDate);
        po.setDealerId(Long.valueOf(dealerId));
        po.setDealerCode(dealerCode);
        po.setDealerName(dealerName);
        po.setSellerId(Long.valueOf(sellerId));
        po.setSellerCode(sellerCode);
        po.setSellerName(sellerName);
        if (buyerId != null && !"".equals(buyerId)) {
            po.setBuyerId(Long.valueOf(buyerId));
        }
        po.setBuyerName(buyerName);
        po.setConsignees(consignees);
        po.setConsigneesId(Long.valueOf(consigneesId));
        po.setAddr(addr);
        po.setWhId(Long.valueOf(whId));
        po.setAddrId(Long.valueOf(addrId));
        po.setReceiver(receiver);
        po.setTel(tel);
        po.setSoFrom(Integer.valueOf(soFrom));
        po.setPostCode(postCode);
        po.setStation(station);
        po.setTransType(transType);

        po.setAmount(Double.valueOf(mainMap.get("AMOUNT") + ""));
        po.setDiscount(Double.valueOf(discount));
        po.setRemark(remark);
        po.setCreateDate(new Date());
        po.setCreateBy(loginUser.getUserId());
        po.setPayType(Integer.valueOf(payType));
        po.setTranspayType(Integer.valueOf(transpayType));
        po.setVer(1);
        po.setState(Constant.CAR_FACTORY_OUTSTOCK_STATE_03);
        dao.insert(po);
        //
        String key = CommonDAO.getPara(Constant.PART_SALE_STOCK_CONTROL.toString());
        if (key.equals("1")) {
            ArrayList ins = new ArrayList();
            ins.add(0, outId);
            ins.add(1, Constant.PART_CODE_RELATION_09);
            ins.add(2, 1);//
            dao.callProcedure("PKG_PART.p_updatepartstock", ins, null);
        }
        insertTtPartTrans(soId);
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
            PartSalesOrderFinCheckDao dao = PartSalesOrderFinCheckDao.getInstance();
            String[] head = new String[14];
            head[0] = "销售单号 ";
            head[1] = "订单号";
            head[2] = "订货单位编码";
            head[3] = "订货单位";
            head[4] = "制单人";
            head[5] = "销售日期";
            head[6] = "销售单位";
            head[7] = "订单类型";
            head[8] = "销售金额";
            head[9] = "可用金额";
            head[10] = "出库仓库";
            head[11] = "业务提交时间";
            head[12] = "财务审核时间";
            head[13] = "订单状态";

            String orgId = Constant.OEM_ACTIVITIES;
            List<Map<String, Object>> list = dao.queryPartSoOrder(request, orgId, 1, Constant.PAGE_SIZE_MAX).getRecords();
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[14];
                    detail[0] = CommonUtils.checkNull(map.get("SO_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("CREATE_BY_NAME"));
                    detail[5] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[6] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                    detail[7] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                    detail[8] = CommonUtils.checkNull(map.get("AMOUNT")).replace(",", "");
                    detail[9] = CommonUtils.checkNull(map.get("ACCOUNT_KY")).replace(",", "");
                    detail[10] = CommonUtils.checkNull(map.get("WH_NAME"));
                    detail[11] = CommonUtils.checkNull(map.get("SUBMIT_DATE"));
                    detail[12] = CommonUtils.checkNull(map.get("FCAUDIT_DATE"));
                    if (null != map.get("STATE") && Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 == Integer.parseInt(map.get("STATE").toString())) {
                        detail[13] = "财务审核通过";
                    } else {
                        detail[13] = "已提交";
                    }
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

        String name = "财务审核列表.xls";
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

    public void insertPartRecord(Map<String, Object> map) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartSoManageDao dao = PartSoManageDao.getInstance();
        PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
        TtPartRecordPO insertPRPo = new TtPartRecordPO();
        String partBatch = ""/*Constant.PART_RECORD_BATCH*/;//配件批次
        String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
        String configId = Constant.PART_CODE_RELATION_09 + "";//配置ID
        String orgId = "";
        String orgName = "";
        String orgCode = "";
        try {
            Map<String, Object> mainMap = dao.getSoOrderMain(CommonUtils.checkNull(map.get("SO_ID")));
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
                orgName = beanList.get(0).getOrgDesc() + "";
                orgCode = beanList.get(0).getOrgCode() + "";
            }
            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
            Map<String, Object> warehouseMap = partOutstockDao.getWarehouse(whId);
            String outId = CommonUtils.checkNull(request.getAttribute("outId"));
            Map<String, Object> locMap = partOutstockDao.queryPartLocationByPartId(CommonUtils.checkNull(map.get("PART_ID")), whId);
            Long recId = Long.parseLong(SequenceManager.getSequence(""));
            insertPRPo.setRecordId(recId);
            insertPRPo.setAddFlag(2);
            insertPRPo.setPartId(Long.parseLong(CommonUtils.checkNull(map.get("PART_ID"))));
            insertPRPo.setPartCode(CommonUtils.checkNull(map.get("PART_CODE")));
            insertPRPo.setPartOldcode(CommonUtils.checkNull(map.get("PART_OLDCODE")));
            insertPRPo.setPartName(CommonUtils.checkNull(map.get("PART_CNAME")));
            insertPRPo.setPartBatch(partBatch);
            insertPRPo.setVenderId(Long.parseLong(partVenId));
            insertPRPo.setPartNum(Long.parseLong(CommonUtils.checkNull(map.get("SALES_QTY"))));//出库数量
            insertPRPo.setConfigId(Long.parseLong(configId));
            insertPRPo.setOrderId(Long.parseLong(outId));//业务ID
            insertPRPo.setOrgId(Long.parseLong(orgId));
            insertPRPo.setOrgCode(orgCode);
            insertPRPo.setOrgName(orgName);
            insertPRPo.setWhId(Long.valueOf(whId));
            insertPRPo.setWhCode(CommonUtils.checkNull(warehouseMap.get("WH_CODE")));
            insertPRPo.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));


            insertPRPo.setLocId(Long.parseLong(CommonUtils.checkNull(locMap.get("LOC_ID"))));
            insertPRPo.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
            insertPRPo.setLocName(CommonUtils.checkNull(locMap.get("LOC_NAME")));
            insertPRPo.setOptDate(new Date());
            insertPRPo.setCreateDate(new Date());
            insertPRPo.setPersonId(loginUser.getUserId());
            insertPRPo.setPersonName(loginUser.getName());
            insertPRPo.setPartState(1);//配件状态
            dao.insert(insertPRPo);
            insertPRPo.setAddFlag(1);
            insertPRPo.setRecordId(Long.parseLong(SequenceManager.getSequence("")));
            dao.insert(insertPRPo);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void insertTtPartTrans(String soId) throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartSoManageDao dao = PartSoManageDao.getInstance();
        PartBaseQueryDao partBaseQueryDao = PartBaseQueryDao.getInstance();
        PartSalesOrderFinCheckDao partSalesOrderFinCheckDao = PartSalesOrderFinCheckDao.getInstance();
        Map<String, Object> mainMap = dao.getSoOrderMain(soId);
        List<Map<String, Object>> detailList = dao.getSoOrderMDetail(soId);
        Long transId = Long.valueOf(request.getAttribute("transId") + "");
        String transCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_10);
        String saleDate = CommonUtils.checkNull(mainMap.get("CREATE_DATE"));
        String outId = CommonUtils.checkNull(request.getAttribute("outId"));
        String outCode = CommonUtils.checkNull(request.getAttribute("outCode"));
        String soCode = CommonUtils.checkNull(mainMap.get("SO_CODE"));
        String orderType = CommonUtils.checkNull(mainMap.get("ORDER_TYPE"));
        String isBatchso = CommonUtils.checkNull(mainMap.get("IS_BATCHSO"));
        String soFrom = CommonUtils.checkNull(mainMap.get("SO_FROM"));
        String orderId = CommonUtils.checkNull(mainMap.get("ORDER_ID"));
        String orderCode = CommonUtils.checkNull(mainMap.get("ORDER_CODE"));
        String payType = CommonUtils.checkNull(mainMap.get("PAY_TYPE"));
        String dealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
        String amount = CommonUtils.checkNull(mainMap.get("AMOUNT"));
        String dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
        String dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
        String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
        String sellerCode = CommonUtils.checkNull(mainMap.get("SELLER_CODE"));
        String sellerName = CommonUtils.checkNull(mainMap.get("SELLER_NAME"));
        String buyerId = CommonUtils.checkNull(mainMap.get("BUYER_ID"));
        String buyerName = CommonUtils.checkNull(mainMap.get("BUYER_NAME"));
        String consignees = CommonUtils.checkNull(mainMap.get("CONSIGNEES"));
        String consigneesId = CommonUtils.checkNull(mainMap.get("CONSIGNEES_ID"));

        String addr = CommonUtils.checkNull(mainMap.get("ADDR"));
        String addrId = CommonUtils.checkNull(mainMap.get("ADDR_ID"));
        String receiver = CommonUtils.checkNull(mainMap.get("RECEIVER"));
        String tel = CommonUtils.checkNull(mainMap.get("TEL"));
        String tel2 = "-1";
        String transportOrg = "-1";
        String linkman = "-1";
        String postCode = CommonUtils.checkNull(mainMap.get("POST_CODE"));
        String station = CommonUtils.checkNull(mainMap.get("STATION"));
        String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
        String transpayType = CommonUtils.checkNull(mainMap.get("TRANSPAY_TYPE"));

        TtPartTransPO po = new TtPartTransPO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sd = sdf.parse(saleDate.split("\\.")[0]);
        po.setTransId(transId);
        po.setTransCode(transCode);
        po.setOutId(Long.valueOf(outId));
        po.setOutCode(outCode);
        po.setSoId(Long.valueOf(soId));
        po.setSoCode(soCode);
        po.setOrderType(Integer.valueOf(orderType));
        po.setIsBatchso(Integer.valueOf(isBatchso));
        if ((Constant.CAR_FACTORY_SO_FORM_02 + "").equals(soFrom)) {
            po.setSoFrom(Integer.valueOf(soFrom));
            po.setOrderId(Long.valueOf(orderId));
            po.setOrderCode(orderCode);
        }
        po.setPayType(Integer.valueOf(payType));
        po.setDealerId(Long.valueOf(dealerId));
        po.setAmount(Double.valueOf(amount));
        po.setDealerName(dealerName);
        po.setDealerCode(dealerCode);
        po.setSellerId(Long.valueOf(sellerId));
        po.setSellerCode(sellerCode);
        po.setSellerName(sellerName);
        po.setSaleDate(sd);
        if (buyerId != "" && !"".equals(buyerId)) {
            po.setBuyerId(Long.valueOf(buyerId));
        } else {
            po.setBuyerId(-1l);
        }
        po.setBuyerName(buyerName);
        po.setConsignees(consignees);
        po.setConsigneesId(Long.valueOf(consigneesId));
        po.setAddr(addr);
        po.setAddrId(Long.valueOf(addrId));
        po.setReceiver(receiver);
        po.setTel(tel);
        po.setTel2(tel2);
        po.setTransportOrg(transportOrg);
        po.setLinkman(linkman);
        po.setPostCode(postCode);
        po.setStation(station);
        po.setTransType(transType);
        po.setTranspayType(Integer.valueOf(transpayType));
        po.setCreateBy(loginUser.getUserId());
        po.setCreateDate(new Date());
        po.setState(Constant.CAR_FACTORY_TRANS_STATE_01);
        po.setWhId(Long.valueOf(CommonUtils.checkNull(mainMap.get("WH_ID"))));
        po.setVer(1);
        dao.insert(po);
    }

    public void insertTtPartTransDtl(TtPartOutstockDtlPO vo) {
        PartSoManageDao dao = PartSoManageDao.getInstance();
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        TtPartTransDtlPO po = new TtPartTransDtlPO();
        Long trlineId = Long.parseLong(SequenceManager.getSequence(""));
        Long transId = Long.valueOf(request.getAttribute("transId") + "");
        Long outlineId = vo.getOutlineId();
        Long partId = vo.getPartId();
        String partCode = vo.getPartCode();
        String partOldcode = vo.getPartOldcode();
        String partCname = vo.getPartCname();
        String unit = vo.getUnit();
        Long minPackage = vo.getMinPackage();
        Long buyQty = vo.getBuyQty();
        Long reservedQty = vo.getReservedQty();
        Long packgQty = vo.getPackgQty();
        Long outstockQty = vo.getOutstockQty();
        Long outId = vo.getOutId();
        String remark = vo.getRemark();
        po.setOutId(Long.valueOf(outId));
        po.setTrlineId(trlineId);
        po.setTransId(transId);
        po.setOutlineId(outlineId);
        po.setPartId(partId);
        po.setPartCode(partCode);
        po.setPartOldcode(partOldcode);
        po.setPartCname(partCname);
        po.setUnit(unit);
        po.setMinPackage(minPackage);
        po.setBuyQty(buyQty);

        po.setPackgQty(packgQty);
        po.setOutstockQty(outstockQty);
        po.setTransQty(outstockQty);
        po.setRemark(remark);
        po.setCreateBy(loginUser.getUserId());
        po.setCreateDate(new Date());
        dao.insert(po);
    }

    /**
     *
     * @Title      :
     * @Description: 释放资金
     * @param      : @param orgAmt
     * @param      : @return
     * @return     :
     * @throws     :
     * LastDate    : 2013-4-3
     */
   /* public void insertAccount(String dealerId,String parentId,Double amount,Long orderId,String orderCode,AclUserBean loginUser) throws Exception{
        try{
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            po.setRecordId(recordId);
            po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_02);
            po.setDealerId(Long.valueOf(dealerId));
            //获取账户余额等
            Map<String,Object> acountMap = dao.getAccount(dealerId,parentId);
            if(null!=acountMap){
                po.setAccountId(Long.valueOf(acountMap.get("ACCOUNT_ID").toString()));
            }
            po.setAmount(amount);
            po.setFunctionName("财务审核通过预扣释放");
            po.setSourceId(Long.valueOf(orderId));
            po.setSourceCode(orderCode);
            po.setOrderId(orderId);
            po.setOrderCode(orderCode);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            dao.insert(po);
        }catch(Exception e){
            throw e;
        }
    }*/
}

