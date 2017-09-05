package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.common.Util;
import com.infodms.dms.dao.parts.salesManager.*;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;

import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class PartTrans extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    private String partDetailUrl = "/jsp/parts/salesManager/carFactorySalesManager/transOrderPartDetail.jsp";
    private static final String PART_ADD_PAGE = "/jsp/parts/salesManager/carFactorySalesManager/queryTransMess.jsp"; //发运选择信息

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 初始化
     */
    public void partTransInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartTransDao dao = PartTransDao.getInstance();
            String parentOrgId = "";//父机构（销售单位）ID

            //判断主机厂与服务商
            String comp = loginUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = loginUser.getDealerId();
            }
            List<Map<String, Object>> wareHouseList = dao.getWareHouse(parentOrgId);
            request.setAttribute("wareHouseList", wareHouseList);
            List listTransType = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_06);// 获取配件运单类型
            List listTransCompany = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 获取配件承运物流
            act.setOutData("transList", listTransType);
            act.setOutData("transCompList", listTransCompany);
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setForword(PART_TRANS_MAIN);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 查询
     */
    public void partTransQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartTransDao dao = PartTransDao.getInstance();
            String searchType = CommonUtils.checkNull(request.getParamValue("searchType"));
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            if ("normal".equals(searchType)) {
                ps = dao.queryOutstockOrder(request, curPage, Constant.PAGE_SIZE);
            } else {
                ps = dao.queryOrderDtl(request, curPage, Constant.PAGE_SIZE);
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_OUTSTOCK_ORDER_MAIN);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_OUTSTOCK_ORDER_MAIN);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 发运
     */
    public void partTransOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            String outId = CommonUtils.checkNull(request.getParamValue("outId"));
            PartTransDao dao = PartTransDao.getInstance();
            Map<String, Object> mainMap = dao.getOutStockMain(outId);

            if (null != mainMap) {
                if (!"".equals(CommonUtils.checkNull(mainMap.get("SO_FROM")))) {
                    if ((Constant.CAR_FACTORY_SO_FORM_02 + "").equals(CommonUtils.checkNull(mainMap.get("SO_FROM")))) {
                        String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
                        Map<String, Object> orderMainMap = dao.getOrderMainInfo(soId);
                        String orderCreateByName = CommonUtils.checkNull(orderMainMap.get("CREATE_BY_NAME"));
                        String orderCreateDate = CommonUtils.checkNull(orderMainMap.get("CREATE_DATE"));
                        mainMap.put("orderCreateByName", orderCreateByName);
                        mainMap.put("orderCreateDate", orderCreateDate);
                    }
                }
            }
            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            mainMap.put("TRANS_TYPE", transType);
            act.setOutData("mainMap", mainMap);
            List<Map<String, Object>> detailList = dao.getOutStockDetail(outId);
            act.setOutData("detailList", detailList);
            act.setForword(PART_TRANS_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 查看
     */
    public void detailOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            PartPickOrderDao dao = PartPickOrderDao.getInstance();
            List<Map<String, Object>> soOrderList = dao.getSoMainList(pickOrderId);
            act.setOutData("soOrderList", soOrderList);
            act.setForword(PART_TRANS_ORDER_DTL);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_TRANS_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_TRANS_ORDER);
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
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "详细查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 生成发运单
     */
    public void createTransOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            insertMain();
            insertDtl();
            changeStatus();
            insertHistory();
            POContext.endTxn(true);
            act.setOutData("success", "发运单:" + (request.getAttribute("transCode") + "") + ",操作成功!");
        } catch (Exception e) {//异常方法
            act.setOutData("error", "生成发运单失败!");
            if (e instanceof BizException) {
                POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成发运单出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        } finally {
            try {
                POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "生成发运单出错,请联系管理员!");
            }
        }
    }

    public void insertMain() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            Long transId = Long.parseLong(SequenceManager.getSequence(""));
            String outId = CommonUtils.checkNull(request.getParamValue("outId"));
            PartOutstockDao dao = PartOutstockDao.getInstance();
            PartTransDao partTransDao = PartTransDao.getInstance();
            Map<String, Object> mainMap = dao.queryOutstockMain(outId);
            String soId = CommonUtils.checkNull(mainMap.get("SO_ID"));
            request.setAttribute("soId", soId);
            Map<String, Object> orderMainMap = partTransDao.getOrderMainInfo(soId);
            String orderId = CommonUtils.checkNull(orderMainMap.get("ORDER_ID"));
            request.setAttribute("orderId", orderId);
            String orderCode = CommonUtils.checkNull(orderMainMap.get("ORDER_CODE"));
            TtPartTransPO po = new TtPartTransPO();
            request.setAttribute("transId", transId);
            String transCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_10);
            request.setAttribute("transCode", transCode);
            String outCode = CommonUtils.checkNull(mainMap.get("OUT_CODE"));
            String soCode = CommonUtils.checkNull(mainMap.get("SO_CODE"));
            request.setAttribute("soCode", soCode);
            String isBatchso = CommonUtils.checkNull(mainMap.get("IS_BATCHSO"));
            String soFrom = CommonUtils.checkNull(mainMap.get("SO_FROM"));
            String payType = CommonUtils.checkNull(mainMap.get("PAY_TYPE"));
            String dealerId = CommonUtils.checkNull(mainMap.get("DEALER_ID"));
            String dealerCode = CommonUtils.checkNull(mainMap.get("DEALER_CODE"));
            String dealerName = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
            String sellerId = CommonUtils.checkNull(mainMap.get("SELLER_ID"));
            String sellerCode = CommonUtils.checkNull(mainMap.get("SELLER_CODE"));
            String saleDate = CommonUtils.checkNull(mainMap.get("SALE_DATE"));
            String sellerName = CommonUtils.checkNull(mainMap.get("SELLER_NAME"));
            String buyerId = CommonUtils.checkNull(mainMap.get("BUYER_ID"));
            String buyerName = CommonUtils.checkNull(mainMap.get("BUYER_NAME"));
            String consigneesId = CommonUtils.checkNull(mainMap.get("CONSIGNEES_ID"));
            String consignees = CommonUtils.checkNull(mainMap.get("CONSIGNEES"));
            String addrId = CommonUtils.checkNull(mainMap.get("ADDR_ID"));
            String addr = CommonUtils.checkNull(mainMap.get("ADDR"));
            String receiver = CommonUtils.checkNull(mainMap.get("RECEIVER"));
            String tel = CommonUtils.checkNull(mainMap.get("TEL"));
            String tel2 = CommonUtils.checkNull(request.getParamValue("TEL2"));
            String transportOrg = CommonUtils.checkNull(request.getParamValue("TRANSPORTORG"));
            String linkman = CommonUtils.checkNull(request.getParamValue("LINKMAN"));
            String postCode = CommonUtils.checkNull(mainMap.get("POST_CODE"));
            String station = CommonUtils.checkNull(mainMap.get("STATION"));
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            String transpayType = CommonUtils.checkNull(mainMap.get("TRANSPAY_TYPE"));
            String orderType = CommonUtils.checkNull(mainMap.get("ORDER_TYPE"));
            String amount = CommonUtils.checkNull(mainMap.get("AMOUNT"));
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
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void insertDtl() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartTransDao dao = PartTransDao.getInstance();
        try {
            String outId = CommonUtils.checkNull(request.getParamValue("outId"));
            String[] outlineIdArr = request.getParamValues("cb");
            for (int i = 0; i < outlineIdArr.length; i++) {
                Map<String, Object> detailMap = dao.queryOutstockDetail(outlineIdArr[i]);
                TtPartTransDtlPO po = new TtPartTransDtlPO();
                Long trlineId = Long.parseLong(SequenceManager.getSequence(""));
                Long transId = Long.valueOf(request.getAttribute("transId") + "");
                String outlineId = detailMap.get("OUTLINE_ID") + "";
                String partId = detailMap.get("PART_ID") + "";
                String partCode = detailMap.get("PART_CODE") + "";
                String partOldcode = detailMap.get("PART_OLDCODE") + "";
                String partCname = detailMap.get("PART_CNAME") + "";
                String unit = detailMap.get("UNIT") + "";
                String minPackage = detailMap.get("MIN_PACKAGE") + "";
                String buyQty = detailMap.get("BUY_QTY") + "";
                String reservedQty = detailMap.get("RESERVED_QTY") + "";
                String packgQty = detailMap.get("PACKG_QTY") + "";
                String outstockQty = detailMap.get("OUTSTOCK_QTY") + "";
                String remark = detailMap.get("REMARK") + "";
                po.setOutId(Long.valueOf(outId));
                po.setTrlineId(trlineId);
                po.setTransId(transId);
                po.setOutlineId(Long.valueOf(outlineId));
                po.setPartId(Long.valueOf(partId));
                po.setPartCode(partCode);
                po.setPartOldcode(partOldcode);
                po.setPartCname(partCname);
                po.setUnit(unit);
                po.setMinPackage(Long.valueOf(minPackage));
                po.setBuyQty(Long.valueOf(buyQty));

                po.setPackgQty(Long.valueOf(packgQty));
                po.setOutstockQty(Long.valueOf(outstockQty));
                po.setTransQty(Long.valueOf(outstockQty));
                po.setRemark(remark);
                po.setCreateBy(loginUser.getUserId());
                po.setCreateDate(new Date());
                dao.insert(po);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void changeStatus() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartTransDao dao =  PartTransDao.getInstance();
        try {
            String outId = CommonUtils.checkNull(request.getParamValue("outId"));
            TtPartOutstockMainPO oldPo = new TtPartOutstockMainPO();
            TtPartOutstockMainPO newPo = new TtPartOutstockMainPO();
            oldPo.setOutId(Long.valueOf(outId));
            newPo.setOutId(Long.valueOf(outId));
            newPo.setState(Constant.CAR_FACTORY_TRANS_STATE_01);
            dao.update(oldPo, newPo);
            TtPartSoMainPO oldSoPo = new TtPartSoMainPO();
            String soId = CommonUtils.checkNull(request.getAttribute("soId"));
            oldSoPo.setSoId(Long.valueOf(soId));
            TtPartSoMainPO newSoPo = new TtPartSoMainPO();
            newSoPo.setSoId(Long.valueOf(soId));
            newSoPo.setState(Constant.CAR_FACTORY_TRANS_STATE_01);
            dao.update(oldSoPo, newSoPo);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 导出
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartTransDao dao = PartTransDao.getInstance();
            String[] head = new String[20];
            String excelName = "配件发运单信息.xls";
            head[0] = "序号";
            head[1] = "拣货单号";
            head[2] = "订货单位编码";
            head[3] = "订货单位名称";
            head[4] = "发运日期";
            head[5] = "发运单号";
            head[6] = "承运物流";
            head[7] = "运单类型";
            head[8] = "出库数量";
            head[9] = "出库金额";
            head[10] = "出库人";
            head[11] = "箱数";
            head[12] = "重量";
            head[13] = "验货人";
            head[14] = "装箱人";
            head[15] = "出库仓库";
            head[16] = "备注";
            head[17] = "发运单打印";

            PageResult<Map<String, Object>> ps = dao.queryOutstockOrder(request, 1, Constant.PAGE_SIZE_MAX);
            List<Map<String, Object>> list = ps.getRecords();
            list = list == null ? new ArrayList() : list;
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[20];

                    detail[0] = (i + 1) + "";
                    detail[1] = CommonUtils.checkNull(map.get("PICK_ORDER_ID"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("TRANS_DATE"));
                    detail[5] = CommonUtils.checkNull(map.get("LOGISTICS_NO"));
                    detail[6] = CommonUtils.checkNull(map.get("TRANS_ORG"));
                    detail[7] = CommonUtils.checkNull(map.get("ORDER_TRANS_TYPE"));
                    detail[8] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
                    detail[9] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
                    detail[10] = CommonUtils.checkNull(map.get("CREATE_BY_NAME"));
                    detail[11] = CommonUtils.checkNull(map.get("PKG_NUM"));
                    detail[12] = CommonUtils.checkNull(map.get("WEIGHT"));
                    detail[13] = CommonUtils.checkNull(map.get("PKG_BY"));
                    detail[14] = CommonUtils.checkNull(map.get("CHECK_PICK_BY"));
                    detail[15] = CommonUtils.checkNull(map.get("WH_NAME"));
                    detail[16] = CommonUtils.checkNull(map.get("REMARK"));
                    if (null != map.get("TRANS_PRINT_NUM") && Integer.parseInt(map.get("TRANS_PRINT_NUM").toString()) > 0) {
                        detail[17] = "是";
                    } else {
                        detail[17] = "否";
                    }

                    list1.add(detail);
                }
            }
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, excelName);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-9
     * @Title : 明细导出
     */
    public void expTransDtlExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartTransDao dao = PartTransDao.getInstance();
            String[] head = new String[15];
            String excelName = "配件发运明细信息.xls";

            head[0] = "序号";
            head[1] = "拣货单号";
//            head[2]="发运方式";
            head[2] = "货位";
            head[3] = "配件编码";
            head[4] = "配件名称";
            head[5] = "配件件号";
            head[6] = "出库数量";
            head[7] = "单价";
            head[8] = "出库金额";
            head[9] = "出库仓库";
            head[10] = "装箱人";
            head[11] = "捡货人";

            List<Map<String, Object>> list = dao.orderDtlList(request);

            list = list == null ? new ArrayList() : list;
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[15];

                    detail[0] = (i + 1) + "";
                    detail[1] = CommonUtils.checkNull(map.get("PICK_ORDER_ID"));
//                        detail[2] = CommonUtils.checkNull(map.get("FIX_NAME"));
                    detail[2] = CommonUtils.checkNull(map.get("LOC_CODE"));
                    detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                    detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
                    detail[5] = CommonUtils.checkNull(map.get("PART_CODE"));
                    detail[6] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
                    detail[7] = CommonUtils.checkNull(map.get("SALE_PRICE"));
                    detail[8] = CommonUtils.checkNull(map.get("SALE_AMOUNT")).replace(",", "");
                    detail[9] = CommonUtils.checkNull(map.get("WH_NAME"));
                    detail[10] = CommonUtils.checkNull(map.get("PKG_BY"));
                    detail[11] = CommonUtils.checkNull(map.get("CHECK_PICK_BY"));

                    list1.add(detail);
                }
            }
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, excelName);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运明细导出异常!");
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
                                  RequestWrapper request, String[] head, List<String[]> list, String name)
            throws Exception {

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
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存日志
     */
    public void insertHistory() throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
            Long optId = Long.parseLong(SequenceManager.getSequence(""));
            po.setBussinessId(CommonUtils.checkNull(request.getAttribute("soCode")));
            po.setOptId(optId);
            po.setOptBy(logonUser.getUserId());
            po.setOptDate(new Date());
            po.setOptType(Constant.PART_OPERATION_TYPE_02);
            po.setWhat("配件发运");
            po.setOptName(logonUser.getName());
            if (!"".equals(CommonUtils.checkNull(request.getAttribute("orderId")))) {
                po.setOrderId(Long.valueOf(CommonUtils.checkNull(request.getAttribute("orderId"))));
            }
            po.setStatus(Constant.CAR_FACTORY_TRANS_STATE_01);
            dao.insert(po);
        } catch (Exception ex) {
            throw ex;
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

    public void modifyInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartTransDao dao = PartTransDao.getInstance();
        try {
            String transId = CommonUtils.checkNull(request.getParamValue("TRANS_ID"));
            String transOrg = CommonUtils.checkNull(request.getParamValue("transOrgSelect_" + transId));//承运物流
            String transType = CommonUtils.checkNull(request.getParamValue("transTypeSelect_" + transId));//运单类型

            TtPartTransPO srcPo = new TtPartTransPO();
            TtPartTransPO updatePo = new TtPartTransPO();
            srcPo.setTransId(Long.valueOf(transId));

            updatePo.setTransportOrg(transOrg);
            updatePo.setTransType(transType);
            updatePo.setIsCheck(Constant.IF_TYPE_YES);
            updatePo.setUpdateBy(loginUser.getUserId());
            updatePo.setUpdateDate(new Date());

            dao.update(srcPo, updatePo);


            act.setOutData("pickOrderId", transId);
            act.setOutData("success", "保存成功!");
        } catch (Exception e) {//异常方法
            act.setOutData("error", "生成发运单失败!");
            if (e instanceof BizException) {
                POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运信息修改出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        } finally {
            try {
                POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "发运信息修改出错,请联系管理员!");
            }
        }
    }

    /**
     *
     */
    public void cmpCheckIn() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartTransDao dao = PartTransDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));

            TtPartPickOrderMainPO newPo = new TtPartPickOrderMainPO();
            TtPartPickOrderMainPO oldPo = new TtPartPickOrderMainPO();
            oldPo.setPickOrderId(pickOrderId);
            oldPo.setIsCheck(0);
            newPo.setIsCheck(1);
            dao.update(oldPo, newPo);
            act.setOutData("pickOrderId", pickOrderId);
            act.setOutData("success", "登记完成成功!");
        } catch (Exception e) {//异常方法
            act.setOutData("error", "发运单登记完成出错!");
            if (e instanceof BizException) {
                POContext.endTxn(false);
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运单登记完成出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        } finally {
            try {
                POContext.cleanTxn();
            } catch (Exception ex) {
                BizException be = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "发运单登记完成出错,请联系管理员!");
            }
        }
    }

    public void queryTransMessInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String pickById = request.getParamValue("pickById");

            act.setOutData("pickById", pickById);
            act.setForword(PART_ADD_PAGE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryMessDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
//			String partolcode = CommonUtils.checkNull(request.getParamValue("partolcode")); // 配件编码
//			String partcname = CommonUtils.checkNull(request.getParamValue("partcname")); // 配件名称
            PartTransDao dao = PartTransDao.getInstance();

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.queryMessages(request, curPage, 50);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
/*===============================发运信息补录start============================================*/
    
    /**
     * 发运信息补录查询页面
     */
    private static final String TO_TRANS_INFO_MAKE_UP = "/jsp/parts/salesManager/carFactorySalesManager/transplan/transInfoMakeUpMain.jsp";
    
    /**
     * 修改页面
     */
    private static final String TO_UPDATE_MAIKE_UP = "/jsp/parts/salesManager/carFactorySalesManager/transplan/transInfoMakeUpdate.jsp";
    
    /**
     * 跳转发运信息补录查询页面
     */
    public void toTransInfoMakeUpInit(){
    	 ActionContext act = ActionContext.getContext();
         RequestWrapper request = act.getRequest();
         AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
         try{
        	String transCode = request.getParamValue("transCode");
         	String orderCode = request.getParamValue("orderCode");
         	String state = request.getParamValue("state");
         	if(StringUtil.notNull(transCode)){
         		act.setOutData("transCode", transCode);
         	}
         	if(StringUtil.notNull(orderCode)){
         		act.setOutData("orderCode", orderCode);
         	}
         	if(StringUtil.notNull(state)){
         		act.setOutData("state", state);
         	}
         	act.setOutData("old", CommonUtils.getBefore(new Date()));
         	act.setOutData("now", CommonUtils.getDate());
         	act.setForword(TO_TRANS_INFO_MAKE_UP);
         }catch (Exception e) {// 异常方法
             BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "跳转发运信息补录查询页面异常");
             logger.error(logonUser, e1);
             act.setException(e1);
         }
    }
    
    /**
     * 查询需要补录的发运信息
     */
    public void getTransInfoMakeUp(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try{
        	PartTransDao dao = PartTransDao.getInstance();
        	String dealerId = logonUser.getDealerId();
//        	Integer pdealerType = logonUser.getPdealerType();
//        	if(StringUtil.isNull(dealerId) && pdealerType==null){
        	if(StringUtil.isNull(dealerId)){
        		dealerId = Constant.OEM_ACTIVITIES;
        	}
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.getTransInfoMakeUp(dealerId,request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        }catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询需要补录的发运信息异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 跳转到补录页面
     */
    @SuppressWarnings("unchecked")
	public void toUpdateMakeUp(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try{
        	PartTransDao dao = PartTransDao.getInstance();
        	String id = request.getParamValue("id");
        	//发运
        	TtPartTransPO tran = new TtPartTransPO();
        	tran.setTransId(Long.valueOf(id));
        	TtPartTransPO tranx = (TtPartTransPO) dao.select(tran).get(0);
        	act.setOutData("tranx", tranx);
        	//承运物流
//        	List<Map<String, Object>> listc = LogisticsDao.getInstance().getLogisticsInfo(logonUser); 
        	List<Map<String, Object>> listc = PartTransPlanDao.getInstance().getLogiType();// 承运物流
        	act.setOutData("wuliuList", listc);
        	//发运方式
//        	TtPartFixcodeDefinePO tranType = new TtPartFixcodeDefinePO();
//        	tranType.setFixGouptype(Constant.FIXCODE_TYPE_04);
//        	List<TtPartFixcodeDefinePO> tranTypeList = dao.select(tranType);
        	List<Map<String, Object>> tranTypeList = PartTransPlanDao.getInstance().getTransportType();// 发运类型
        	act.setOutData("tranTypeList", tranTypeList);
        	
        	String transCode = request.getParamValue("transCode");
        	String orderCode = request.getParamValue("orderCode");
        	String state = request.getParamValue("state");
        	
        	if(StringUtil.notNull(transCode)){
         		act.setOutData("transCode", transCode);
         	}
         	if(StringUtil.notNull(orderCode)){
         		act.setOutData("orderCode", orderCode);
         	}
         	if(StringUtil.notNull(state)){
         		act.setOutData("state", state);
         	}
        	
        	act.setForword(TO_UPDATE_MAIKE_UP);
        }catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询需要补录的发运信息异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 保存补录信息
     */
    @SuppressWarnings("unchecked")
	public void saveTransInfoMakeUp(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try{
        	PartTransDao dao = PartTransDao.getInstance();
        	String TRANS_ID = request.getParamValue("TRANS_ID");//发运单id
        	String tranType = request.getParamValue("TRANS_TYPE");//发运方式
        	String TRANSPORT_ID = request.getParamValue("TRANSPORT_ID");//承运物流
        	String WULIU_CODE = request.getParamValue("WULIU_CODE");
        	String OUT_ID = request.getParamValue("OUT_ID");
        	String freightAmount = request.getParamValue("freightAmount");//运费
        	String transRemark = request.getParamValue("transRemark");
//        	String transBoxNum = request.getParamValue("transBoxNum");
        	
        	//更新发运单tt_part_trans
        	TtPartTransPO tran = new TtPartTransPO();
        	tran.setTransId(Long.valueOf(TRANS_ID));//源po
        	TtPartTransPO tranx = new TtPartTransPO();//更新po
        	TtPartTransPO tranx2 = new TtPartTransPO();//更新po
        	tranx.setTransportOrg(TRANSPORT_ID);
        	tranx.setTransType(tranType);
        	tranx.setWuliuCode(WULIU_CODE);
        	tranx.setAcAmount(Double.valueOf(freightAmount));
        	tranx.setRemark2(transRemark);
//        	tranx.setTransBoxNum(Integer.valueOf(transBoxNum));//箱数
        	//此处只更新基本的发运信息，不更新修改日期
        	dao.update(tran, tranx);
        	
        	//更新发运计划tt_part_trans_plan
        	TtPartTransPlanPO plan = new TtPartTransPlanPO();
        	plan.setOutId(Long.valueOf(OUT_ID));
        	List<TtPartTransPlanPO> planList = dao.select(plan);
        	if(planList.size()>0){
        		//w委托订单不会出现在这里，可以直接更新（一个outid对应多个transid的情况）
        		TtPartTransPlanPO planx = new TtPartTransPlanPO();
        		planx.setTransportOrg(tranx.getTransportOrg());
        		planx.setTransType(tranx.getTransType());
//        		planx.setWuliuCode(tranx.getWuliuCode());
        		dao.update(plan, planx);
        	}
        	//如果发运方式是顺丰发货方付 的付款方式，，进行经销商运费政策的扣除
        	/*if("顺丰陆运（发货方付）".equals(tranType)  || "顺丰航空（发货方付）".equals(tranType)  ){
        		ArrayList ins = new ArrayList();
                ins.add(0, TRANS_ID);
                ins.add(1, logonUser.getUserId());
                dao.callProcedure("PKG_PART_TOOLS.PROC_CREATE_TRANS_FRTIGHT_LOG", ins, null);
        	}*/
        	//此环节更新发运单的更新时间，否则顺丰政策处理会出现差异
        	tranx2.setUpdateDate(new Date());
        	tranx2.setUpdateBy(logonUser.getUserId());
        	dao.update(tran, tranx2);
        	
        	act.setOutData("success", "保存成功!");
        }catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "保存补录信息异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
   
    /**
     * 发运补录信息模板下载
     */
    public void exportExcelTemplate(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        PartTransDao dao = PartTransDao.getInstance();
		try {
			List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            //标题
            listHead.add("发运单号");
            listHead.add("发运方式");
            listHead.add("承运物流");
            listHead.add("物流单号");
            listHead.add("运费");
            listHead.add("补录备注");
            list.add(listHead);
            
            //根据条件查询变更信息
            String dealerId = loginUser.getDealerId();
            if(dealerId==null){
            	dealerId = Constant.OEM_ACTIVITIES;
            }
            List<Map<String,Object>> infoList =  dao.getTransByDealerId(act.getRequest(),dealerId);
            // 导出的文件名
            String fileName = "发运信息补录模板"+DateUtil.format(new Date(), "yyyy-MM-dd")+".xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            
            this.createXlsFile(list,infoList,os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运补录信息模板下载异常");
            logger.error(loginUser, e1);
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
     * 制作excel
     * @param list
     * @param infoList
     * @param os
     */
    private void createXlsFile(List<List<Object>> list, List<Map<String, Object>> infoList, OutputStream os) {
    	ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    try {
		   WritableWorkbook workbook = Workbook.createWorkbook(os);
		   WritableSheet sheet = workbook.createSheet("下载模板", 0);
		   for (int i = 0; i < list.size(); i++) {
               for (int j = 0; j < list.get(i).size(); j++) {
                   // 添加单元格
                   sheet.addCell(new Label(j, i, (list.get(i).get(j) != null ? list.get(i).get(j).toString() : "")));
               }
               
           }
		   //模板内容写入
		   if (infoList != null && infoList.size() > 0) {
               for (int i = 0; i < infoList.size(); i++) {
                   Map<String, Object> map = infoList.get(i);
                   //发运单号
                   sheet.addCell(new Label(0, i + 1, CommonUtils.checkNull(map.get("TRANS_CODE"))));
                   //发运方式
                   sheet.addCell(new Label(1, i + 1, CommonUtils.checkNull(map.get("TRANS_TYPE"))));
                   //承运物流
                   sheet.addCell(new Label(2, i + 1, CommonUtils.checkNull(map.get("TRANSPORT_ORG"))));
                   //物流单号
                   sheet.addCell(new Label(3, i + 1, CommonUtils.checkNull(map.get("WULIU_CODE"))));
                   //物流费用=运费
                   sheet.addCell(new Label(4, i + 1, CommonUtils.checkNull(map.get("AC_AMOUNT"))));
                   //补录备注
                   sheet.addCell(new Label(5, i + 1, CommonUtils.checkNull(map.get("REMARK2"))));
               }
           }
		   workbook.write();
           workbook.close();
		}catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "创建xls下载文件异常");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
	}
    
    /**
     * 批量变更发运补录信息
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void uploadXlsFileByTransInfo(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String err = "";
		StringBuffer errorInfo = new StringBuffer("");
		PartTransDao dao = PartTransDao.getInstance();
		try {
	    	long maxSize = 1024 * 1024 * 5;
	    	int errNum = insertIntoTmp(request, "uploadFile", 6, 2, maxSize,1);
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
				case 6:
					err += "文件不能大于!";
					break;
				default:
					break;
				}
			}
	    	
	    	if (!"".equals(err)) {
				BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "");
				throw e1;
			}else{
				List<Map> list = getMapList();
                List voList = new ArrayList();
                List<String> errList = new ArrayList<String>();
                loadVoList(voList, list, errList,dao,loginUser);
                
                if (errList.size() > 0) {
                	err = errorInfo.toString();
                	act.setOutData("errList", errList);
                    BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, "");
                    throw e1;
                }
                
                for (int i = 0; i < voList.size(); i++) {
                	TtPartTransPO tran = (TtPartTransPO) voList.get(i);//新
                	
                	TtPartTransPO tranPo = new TtPartTransPO();
                	tranPo.setTransId(tran.getTransId());//源po
                	
                	TtPartTransPO tranx = new TtPartTransPO();
                	TtPartTransPO tranx2 = new TtPartTransPO();
                	tranx.setTransportOrg(tran.getTransportOrg());
                	tranx.setTransType(tran.getTransType());
                	tranx.setWuliuCode(tran.getWuliuCode());
                	
                	tranx.setAcAmount(Double.valueOf(tran.getAcAmount()));
                	tranx.setRemark2(tran.getRemark2());
                	//此处只更新基本的发运信息，不更新修改日期
                	dao.update(tranPo, tranx);
                	
                	TtPartTransPlanPO plan = new TtPartTransPlanPO();
                	plan.setOutId(tran.getOutId());
                	List<TtPartTransPlanPO> planList = dao.select(plan);
                	if(planList.size()>0){
                		TtPartTransPlanPO planx = new TtPartTransPlanPO();
                		planx.setTransportOrg(tran.getTransportOrg());
                		planx.setTransType(tran.getTransType());
                		dao.update(plan, planx);//更新计划表
                	}
                	//如果发运方式是顺丰发货方付 的付款方式，，进行经销商运费政策的扣除
                	/*if("顺丰陆运（发货方付）".equals(tran.getTransType()) 
                			|| "顺丰航空（发货方付）".equals(tran.getTransType())  ){
                		ArrayList ins = new ArrayList();
                        ins.add(0, tran.getTransId()+"");
                        ins.add(1, loginUser.getUserId());
                        dao.callProcedure("PKG_PART_TOOLS.PROC_CREATE_TRANS_FRTIGHT_LOG", ins, null);
                	}*/
                	 
                	//此环节更新发运单的更新时间，否则顺丰政策处理会出现差异
                	tranx2.setUpdateDate(new Date());
                	tranx2.setUpdateBy(loginUser.getUserId());
                	dao.update(tranPo, tranx2);
                }
			}
	    	act.setOutData("successInfo", "变更成功！");
	    	toTransInfoMakeUpInit();
	    }catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "批量变更发运补录信息异常");
            logger.error(loginUser, e1);
            if(StringUtil.notNull(err)){
            	act.setOutData("info", err);
            }else{
            	act.setOutData("info", " 批量变更发运补录信息异常");
            }
            
            toTransInfoMakeUpInit();
        }
    }
    
    /**
	 * 循环获取cell，生成数据存入list
     * @param dao 
     * @param loginUser 
	 * @param request
	 * @param emerList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadVoList(List voList, List<Map> list,  List<String> errList, PartTransDao dao, AclUserBean loginUser)throws Exception {
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
                parseCells(voList, key, cells, errList,dao,loginUser);
            }
        }
	}
	
	/**
     * 装载VO
	 * @param loginUser 
     *
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @return :
     * @throws Exception
     * @throws :
     * @Title :
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parseCells(List list, String rowNum, Cell[] cells, List<String> errList, PartTransDao dao, AclUserBean loginUser) throws Exception {
		
		if ("" == subCell(cells[0].getContents().trim())) {
			//经销商代码
			errList.add("第" + rowNum + "行发运单号为空,请修改后再上传!<br>");
            return;
		}
		if ("" == subCell(cells[1].getContents().trim())) {
            errList.add("第" + rowNum + "行发运方式为空,请修改后再上传!<br>");
            return;
        }
		if ("" == subCell(cells[2].getContents().trim())) {
            errList.add("第" + rowNum + "行承运物流为空,请修改后再上传!<br>");
            return;
        }
		if ("" == subCell(cells[3].getContents().trim())) {
            errList.add("第" + rowNum + "行物流单号为空,请修改后再上传!<br>");
            return;
        }else{
        	int len = subCell(cells[3].getContents().trim()).length();
        	if(len>50){
        		 errList.add("第" + rowNum + "行物流单号长度超过50,请修改后再上传!<br>");
                 return;
        	}
        	
        }
		if ("" == subCell(cells[4].getContents().trim())) {
            errList.add("第" + rowNum + "运费为空,请修改后再上传!<br>");
            return;
        }else{
        	if(!Util.isNumber(subCell(cells[4].getContents().trim()))){
        		errList.add("第" + rowNum + "运费不为数字,请修改后再上传!<br>");
        	}
        }
		
		Map<String, Object> map1 = null;//发运单
		map1 = dao.validateTransInfoByTransCode(subCell(cells[0].getContents().trim()));
        if (map1 == null) {
            errList.add("第" + rowNum + "行的发运单号不存在,请修改后再上传!<br>");
            return;
        }
        Map<String, Object> map2 = null;//发运方式
        map2 = dao.validateTransType(subCell(cells[1].getContents().trim()));
		if(map2 == null){
			errList.add("第" + rowNum + "行的发运方式不存在,请修改后再上传!<br>");
            return;
		}
		Map<String, Object> map3 = null;//承运物流
		map3 = dao.validateWuliu(subCell(cells[2].getContents().trim()),loginUser);
		if(map3 == null){
			errList.add("第" + rowNum + "行的承运物流不存在,请修改后再上传!<br>");
            return;
		}
		
		Long transId = Long.valueOf(map1.get("TRANS_ID").toString());//发运id
		String transCode = subCell(cells[0].getContents().trim());//发运单号
		Long outId = Long.valueOf(map1.get("OUT_ID").toString());//出库id
		
//		String transType = subCell(cells[1].getContents().trim());//发运方式
		String transType = map2.get("TV_ID").toString();//发运方式
		
//		Long transPortId = Long.valueOf(map3.get("ID").toString());//承运物流单位
//		String transPortOrg = map3.get("NAME").toString();//承运物流名称
		String transPortOrg=map3.get("ID").toString();//承运物流单位
		
		String wuliuCode = subCell(cells[3].getContents().trim()).toString();//物流单号
		
		Double freightAmount = Double.valueOf(subCell(cells[4].getContents().trim()));//运费
//		Integer transBoxNum = Integer.valueOf(subCell(cells[5].getContents().trim()));//发运箱数
		
		String transRemark = subCell(cells[5].getContents().trim()).toString();//运费补录备注
		
		TtPartTransPO tran = new TtPartTransPO();
		//验证是否存在重复数据
        for (int i = 0; i < list.size(); i++) {
        	TtPartTransPO tranx = (TtPartTransPO) list.get(i);
			Long tId =tranx.getTransId();
			
			if(tId.equals(transId)){
				//已存在
				errList.add("第"+rowNum+"行发运单号："+transCode+"的数据,已经存在！<br>");
			}
		}
        
        tran.setTransId(transId);
        tran.setTransCode(transCode);
        tran.setOutId(outId);
        tran.setTransType(transType);
//        tran.setTransportId(transPortId);
        tran.setTransportOrg(transPortOrg);
        tran.setWuliuCode(wuliuCode);
        tran.setAcAmount(freightAmount);
//        tran.setTransBoxNum(transBoxNum);
        tran.setRemark2(transRemark);
		list.add(tran);//存入list
		
	}
	
	/**
     * 截取字符串
     *
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-7
     * @Title :
     */
    private String subCell(String orgAmt) {
        String newAmt = "";
        if (null == orgAmt || "".equals(orgAmt)) {
            return newAmt;
        }
        if (orgAmt.length() > 200) {
            newAmt = orgAmt.substring(0, 200);
        } else {
            newAmt = orgAmt;
        }
        return newAmt;
    }
    
    /*===============================发运信息补录end==============================================*/

	

}
