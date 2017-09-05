package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanQuery.PartPlanQueryDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrInstockDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartOutstockDao;
import com.infodms.dms.dao.parts.salesManager.PartPkgDao;
import com.infodms.dms.dao.parts.salesManager.PartSoManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDlrInstockDtlPO;
import com.infodms.dms.po.TtPartDlrInstockMainPO;
import com.infodms.dms.po.TtPartInstockExceptionLogPO;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartOperationHistoryPO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.po.TtPartTransPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import jxl.Workbook;
import jxl.write.Label;

/**
 * <p>ClassName: PartDlrInstock</p>
 * <p>Description: 经销商入库</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年8月16日</p>
 */
@SuppressWarnings("unchecked")
public class PartDlrInstock extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(PartDlrInstock.class);
    PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
    String PRINT = "/jsp/parts/salesManager/carFactorySalesManager/instockPrint.jsp";
    String PAY_APPLY_PRINT = "/jsp/parts/salesManager/carFactorySalesManager/payApplyPrint.jsp";


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 经销商入库查询初始化
     */
    public void partDlrInstockInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//        RequestWrapper request = act.getRequest();
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_DLR_INSTOCK_MAIN);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "入库初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 经销商入库查询
     */
    public void partDlrInstockQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            
            PageResult<Map<String, Object>> ps  = dao.queryInstockOrder(request, curPage, Constant.PAGE_SIZE);
            
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_OUTSTOCK_ORDER_MAIN);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "入库查询数据错误,请联系管理员!");
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
     * @Description: 经销商入库
     */
    public void partInstock() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String dealerId = "";
//            String whId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList && beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            List<Map<String, Object>> wareHouseList = dao.getWareHouse(dealerId);
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));

            Map<String, Object> dataMap = dao.getTransMain(transId);
            Map<String, Object> mainMap = new HashMap<String, Object>();
            mainMap.put("createBy", loginUser.getName());
            mainMap.put("createDate", CommonUtils.getDate());
            mainMap.put("ORDER_TYPE", dataMap.get("ORDER_TYPE"));
            mainMap.put("transId", transId);
            mainMap.put("whName", CommonUtils.checkNull(dataMap.get("WH_NAME")));
            mainMap.put("whId", CommonUtils.checkNull(dataMap.get("WH_ID")));
            act.setOutData("mainMap", mainMap);
            
//            List<Map<String, Object>> locList = dao.getPartLocId(transId); // 产生货位
            List<Map<String, Object>> list = dao.getTransDetail2(transId, null);
            List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
            String soId = CommonUtils.checkNull(dataMap.get("SO_ID"));

            List<Map<String, Object>> inMainList = dao.getInstockMain(soId);
            if (null != inMainList && inMainList.size() > 0) {
                String inIds = "";
                for (Map<String, Object> map : inMainList) {
                    String inId = CommonUtils.checkNull(map.get("IN_ID"));
                    inIds += "," + inId;
                }
                if (!"".equals(inIds)) {
                    inIds = inIds.replaceFirst(",", "");
                }
                //做过入库 数据需要计算处理
                for (Map<String, Object> map : list) {
                    Long transQty = Long.valueOf(CommonUtils.checkNull(map.get("TRANS_QTY")));
                    String partId = CommonUtils.checkNull(map.get("PART_ID"));
                    Long inQty = dao.getInQty(inIds, partId);
                    if (inQty == Long.valueOf(CommonUtils.checkNull(map.get("TRANS_QTY")))) {
                        continue;
                    }
                    if (inQty <= transQty) {
                        map.put("TRANS_QTY", transQty - inQty);
                        detailList.add(map);
                    }

                }
            } else {
                detailList = list;
            }

            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("detailList", detailList);
            act.setOutData("wareHouseList", wareHouseList);
            act.setForword(PART_DLR_INSTOCK);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "入库初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 查看
     */
    public void partOutstockDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
        PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
        try {
            String trplanCode = CommonUtils.checkNull(request.getParamValue("TRPLAN_CODE"));
            String transId = null;
            Map<String, Object> mainMap = null;
            if (!"".equals(trplanCode) && null != trplanCode) {
                mainMap = dao.getTransMainNew(trplanCode, "2"); //
                transId = mainMap.get("TRANS_ID") + "";
            } else {
                transId = CommonUtils.checkNull(request.getParamValue("transId"));
                mainMap = dao.getTransMainNew(transId, "0"); //先前默认查询
            }
            Map<String, Object> soMap = partSoManageDao.getSoOrderMain(CommonUtils.checkNull(mainMap.get("SO_ID")));
            String orderCreateBy = "";
            String orderCreateDate = "";
            if (Constant.CAR_FACTORY_SO_FORM_02.toString().equals(CommonUtils.checkNull(mainMap.get("SO_FORM")))) {
                String orderId = CommonUtils.checkNull(soMap.get("ORDER_ID"));
                Map<String, Object> orderMap = partDlrOrderDao.queryPartDlrOrderMain(orderId);
                orderCreateBy = CommonUtils.checkNull(orderMap.get("NAME"));
                orderCreateDate = CommonUtils.checkNull(orderMap.get("CREATE_DATE"));
            }
            mainMap.put("orderCreateBy", orderCreateBy);
            mainMap.put("orderCreateDate", orderCreateDate);
//            List<Map<String, Object>> detailList = dao.getTransDetail(transId);
            List<Map<String, Object>> detailList = dao.getTransDetail2(transId, null);
            List<Map<String, Object>> historyList = partSoManageDao.queryOrderHistory(CommonUtils.checkNull(mainMap.get("SO_CODE")));
            act.setOutData("mainMap", mainMap);
            act.setOutData("historyList", historyList);
            act.setOutData("detailList", detailList);
            act.setOutData("trplanCode", trplanCode);
            act.setForword(PART_DLR_INSTOCK_DETAIL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "入库初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商入库-生成入库单
     */
    public void createInstock() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
//        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
        try {

            // 机构信息
            String orgId = "";
            String orgName = "";
            String orgCode = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            PartDlrInstockDao partDlrInstockDao = PartDlrInstockDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList && beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
                orgName = beanList.get(0).getOrgCode() + "";
                orgCode = beanList.get(0).getOrgCode() + "";
            }
            
        	//经销商入库主表
//            insertMain();
            /*-------------------------获取发运主要信息------------------------*/
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
            Map<String, Object> dataMap = dao.getTransMain(transId);
            String inCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_26);
            request.setAttribute("inCode", inCode);
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));
            String transCode = CommonUtils.checkNull(dataMap.get("TRANS_CODE"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
            Map<String, Object> warehouseMap = partOutstockDao.getWarehouse(whId);
            String whName = CommonUtils.checkNull(warehouseMap.get("WH_NAME"));
            String orderId = CommonUtils.checkNull(dataMap.get("ORDER_ID"));
            String orderCode = CommonUtils.checkNull(dataMap.get("ORDER_CODE"));
            String orderType = CommonUtils.checkNull(dataMap.get("ORDER_TYPE"));
            String dealerId = CommonUtils.checkNull(dataMap.get("DEALER_ID"));
            String dealerCode = CommonUtils.checkNull(dataMap.get("DEALER_CODE"));
            String dealerName = CommonUtils.checkNull(dataMap.get("DEALER_NAME"));
            String sellerId = CommonUtils.checkNull(dataMap.get("SELLER_ID"));
            String sellerCode = CommonUtils.checkNull(dataMap.get("SELLER_CODE"));
            String sellerName = CommonUtils.checkNull(dataMap.get("SELLER_NAME"));
            String RDate = CommonUtils.checkNull(request.getParamValue("RDate"));
            String soId = CommonUtils.checkNull(dataMap.get("SO_ID"));
            String soCode = CommonUtils.checkNull(dataMap.get("SO_CODE"));
            /*-------------------------获取发运主要信息------------------------*/
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(RDate);
            
            
            /*-------------------------经销商入库主表信息------------------------*/
            TtPartDlrInstockMainPO po = new TtPartDlrInstockMainPO();
            Long inId = Long.parseLong(SequenceManager.getSequence(""));
            request.setAttribute("inId", inId);
            po.setInId(inId);
            po.setInCode(inCode);
            po.setTransId(Long.valueOf(transId));
            po.setTransCode(transCode);
            po.setRemark2(remark);
            po.setWhId(Long.valueOf(whId));
            po.setWhName(whName);
            if (!"".equals(orderId)) {
                po.setOrderId(Long.valueOf(orderId));
            }
            if (!"".equals(soId)) {
                po.setSoId(Long.valueOf(soId));
            }
            po.setSoCode(soCode);
            po.setOrderCode(orderCode);
            po.setOrderType(Integer.valueOf(orderType));
            po.setDealerId(Long.valueOf(dealerId));
            po.setDealerName(dealerName);
            po.setDealerCode(dealerCode);
            po.setSellerId(Long.valueOf(sellerId));
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            po.setCreateDate(new Date());
            po.setSaleDate(new Date());
            po.setArrivalDate(date);
            po.setCreateBy(loginUser.getUserId());
            po.setVer(1);
            po.setState(Constant.CAR_FACTORY_INSTOCK_STATE_01);
            /*-------------------------经销商入库主表信息------------------------*/
            
            //经销商入库明细表
            String[] partIds = request.getParamValues("partId");
            List<TtPartDlrInstockDtlPO> dlrInDtlPoList = new ArrayList<TtPartDlrInstockDtlPO>(); // 经销商入库明细记录
            List<TtPartRecordPO> recordPOList = new ArrayList<TtPartRecordPO>(); // 入库异常记录
            List<TtPartInstockExceptionLogPO> exceptionPoList = new ArrayList<TtPartInstockExceptionLogPO>(); // 入库记录
            List<Map<String, Object>> partList = dao.getTransDetail2(transId, partIds); // 入库配件列表
            for(int i = 0; i < partList.size(); i++){
                Map<String, Object> partMap = partList.get(i);
                String partId = CommonUtils.checkNull(partMap.get("PART_ID")); // 配件id
                
                String inQty = CommonUtils.checkNull(request.getParamValue("inQty_" + partId)); // 入库数量
                String inType = CommonUtils.checkNull(request.getParamValue("inType_" + partId)); // 入库类型
                String dtlRemark = CommonUtils.checkNull(request.getParamValue("remark_" + partId)); // 备注
                String partCode = CommonUtils.checkNull(partMap.get("PART_CODE")); // 件号
                String partOldcode = CommonUtils.checkNull(partMap.get("PART_OLDCODE")); // 配件编码
                String partCname = CommonUtils.checkNull(partMap.get("PART_CNAME")); // 配件名称
                String unit = CommonUtils.checkNull(partMap.get("UNIT")); // 单位
                String minPackage = CommonUtils.checkNull(partMap.get("MIN_PACKAGE")); // 最小包装量
                String buyQty = CommonUtils.checkNull(partMap.get("BUY_QTY")); // 采购数量
                String transQty = CommonUtils.checkNull(partMap.get("TRANS_QTY")); // 销售数量
                
                /*-------------------------服务商接收入库明细------------------------*/
                TtPartDlrInstockDtlPO dtlPo = new TtPartDlrInstockDtlPO();
                if (inQty == null || inQty.equals("0")) {
                    continue;
                }
//                dtlPo.setInlineId(Long.valueOf(inId) + (long)(i + 1));
                dtlPo.setInlineId(Long.parseLong(SequenceManager.getSequence("")));
                dtlPo.setInQty(Long.valueOf(inQty)); // 入库数量
                dtlPo.setInType(Integer.valueOf(inType)); // 入库类型
                dtlPo.setTransId(Long.valueOf(transId)); // 发运单id
                dtlPo.setRemark(dtlRemark);  // 备注
                dtlPo.setInId(Long.valueOf(inId)); // 入库id
                dtlPo.setPartCode(partCode); // 件号
                dtlPo.setPartId(Long.valueOf(partId)); // 配件id
                dtlPo.setPartOldcode(partOldcode); // 配件编码
                dtlPo.setPartCname(partCname); // 配件名称
                dtlPo.setUnit(unit); // 单位
                dtlPo.setMinPackage(Long.valueOf(minPackage)); // 最小包装量
                dtlPo.setBuyQty(Long.valueOf(buyQty)); // 采购数量
                dtlPo.setTransQty(Long.valueOf(transQty)); // 发运数量
                dtlPo.setCreateBy(loginUser.getUserId()); // 创建人
                dtlPo.setCreateDate(date); // 创建时间
                dtlPo.setVer(1); // 版本
                dlrInDtlPoList.add(dtlPo);
                // 破/缺件
                if (!"".equals(inType) && !inType.equals(Constant.CAR_FACTORY_INSTOCK_APPROVAL_STATE_01 + "")) {
                    TtPartInstockExceptionLogPO exceptionPo = new TtPartInstockExceptionLogPO();
                    Long exceptionId = Long.parseLong(SequenceManager.getSequence(""));
                    String exceptionNum = request.getParamValue("exceptionNum_" + partId);
                    exceptionPo.setExceptionId(exceptionId);
                    exceptionPo.setExceptionNum(Long.valueOf(exceptionNum));
                    exceptionPo.setExceptionRemark(remark);
                    exceptionPo.setCreateBy(loginUser.getUserId());
                    exceptionPo.setCreateDate(date);
                    exceptionPo.setInId(inId);
                    exceptionPo.setInCode(inCode);
                    exceptionPo.setSoId(Long.valueOf(soId));
                    exceptionPo.setSoCode(soCode);
                    exceptionPo.setState(Constant.STATUS_ENABLE);
                    exceptionPo.setPartId(Long.valueOf(partId));
                    exceptionPo.setState(Constant.INSTOCK_EXCEPTION_REPLY_01);
                    exceptionPoList.add(exceptionPo);
                }
                /*-------------------------服务商接收入库明细------------------------*/

                /*-------------------------获取货位------------------------*/
                Map<String, Object> locMap = partDlrInstockDao.getLoc(partId, whId, orgId);
                String locName = CommonUtils.checkNull(locMap.get("LOC_NAME"));
                String locId = CommonUtils.checkNull(locMap.get("LOC_ID"));
                String locCode = CommonUtils.checkNull(locMap.get("LOC_CODE"));
                if (locMap.isEmpty()) {
                    TtPartLoactionDefinePO locPo = new TtPartLoactionDefinePO();
                    locPo.setLocId(Long.parseLong(SequenceManager.getSequence("")));
                    locPo.setLocCode("暂无");
                    locPo.setLocName("暂无");
                    locPo.setPartId(Long.parseLong(partId));
                    locPo.setOrgId(Long.parseLong(orgId));
                    locPo.setWhId(Long.parseLong(whId));
                    locPo.setState(Constant.STATUS_ENABLE);
                    locPo.setStatus(Constant.IF_TYPE_YES);
                    locPo.setCreateBy(loginUser.getUserId());
                    locPo.setCreateDate(new Date());
                    dao.insert(locPo);
                    locId = locPo.getLocId() + "";
                    locCode = locPo.getLocCode();
                    locName = locPo.getLocName();
                }
                /*-------------------------获取货位------------------------*/

                /*-------------------------入库记录------------------------*/
                TtPartRecordPO insertPRPo = new TtPartRecordPO();
                Long recId = Long.parseLong(SequenceManager.getSequence(""));
                insertPRPo.setRecordId(recId);
                insertPRPo.setAddFlag(1);//入库
                insertPRPo.setPartId(Long.parseLong(partId));
                insertPRPo.setPartCode(partCode);
                insertPRPo.setPartOldcode(partOldcode);
                insertPRPo.setPartName(partCname);
                insertPRPo.setPartBatch("1306");
                insertPRPo.setVenderId(Long.parseLong(Constant.PART_RECORD_VENDER_ID));
                insertPRPo.setPartNum(Long.parseLong(inQty));//入库数量
                insertPRPo.setConfigId((long) Constant.PART_CODE_RELATION_26);
                insertPRPo.setOrderId(inId);//业务ID
                insertPRPo.setOrgId(Long.parseLong(orgId));
                insertPRPo.setOrgCode(orgCode);
                insertPRPo.setOrgName(orgName);
                insertPRPo.setWhId(Long.valueOf(whId));
                insertPRPo.setWhCode(CommonUtils.checkNull(locMap.get("LOC_NAME")));
                insertPRPo.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));
                insertPRPo.setLocId(Long.parseLong(locId));
                insertPRPo.setLocCode(locCode);
                insertPRPo.setLocName(locName);
                insertPRPo.setOptDate(new Date());
                insertPRPo.setCreateDate(new Date());
                insertPRPo.setPersonId(loginUser.getUserId());
                insertPRPo.setPersonName(loginUser.getName());
                insertPRPo.setPartState(1);//配件状态
                recordPOList.add(insertPRPo);
                /*-------------------------入库记录------------------------*/
            }
            
            // 插入经销商入库主表记录
            dao.insert(po);
            // 插入经销商入库明细记录
            dao.insert(dlrInDtlPoList);
            // 插入经销商入库异常记录
            dao.insert(exceptionPoList);
            // 插入入库记录
            dao.insert(recordPOList);
            
            // 如果有异常入库记录返回信息
            if (exceptionPoList.size() > 0) {
                act.setOutData("inId", inId);
                act.setOutData("expFlag", 1);
            }
            
            // 校验经销商入库
            if (validateChangeState(dataMap)) {
            	//更新总部发运单状态
                changeStatus();
            }
            
            //调用入库逻辑start
            ArrayList<Object> ins = new ArrayList<Object>();
            ins.add(0, inId);
            ins.add(1, Constant.PART_CODE_RELATION_26);
            ins.add(2, 1);
            dao.callProcedure("PKG_PART.p_updatepartstock", ins, null);
            //调入入库逻辑 end
            //添加配件操作记录
            saveHistory(Constant.CAR_FACTORY_INSTOCK_STATE_01);
            //判断是否是委托订单
            stockControl();
            
            act.setOutData("success", "入库成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "入库出错,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    private boolean validateChangeState(Map<String, Object> dataMap) throws Exception {
        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
//        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
        try {
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
            List<Map<String, Object>> list = dao.getTransDetail2(transId, null);
            String soId = CommonUtils.checkNull(dataMap.get("SO_ID"));
            List<Map<String, Object>> inMainList = dao.getInstockMain(soId);
            if (null != inMainList && inMainList.size() > 0) {
                String inIds = "";
                for (Map<String, Object> map : inMainList) {
                    String inId = CommonUtils.checkNull(map.get("IN_ID"));
                    inIds += "," + inId;
                }
                if (!"".equals(inIds)) {
                    inIds = inIds.replaceFirst(",", "");
                }
                //做过入库 数据需要计算处理
                for (Map<String, Object> map : list) {
//                    Long transQty = Long.valueOf(CommonUtils.checkNull(map.get("TRANS_QTY")));
                    String partId = CommonUtils.checkNull(map.get("PART_ID"));
                    Long inQty = dao.getInQty(inIds, partId);
                    if ((inQty + "").equals(CommonUtils.checkNull(map.get("TRANS_QTY")))) {
                        continue;
                    } else {
                        if (inQty > Long.valueOf(CommonUtils.checkNull(map.get("TRANS_QTY")))) {
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "入库数据出错,请联系管理员!");
                            throw e1;
                        } else {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 判断是否是委托订单，委托订单在经销商入库时总部会虚拟入库和虚拟出库
     * 委托订单就是直发订单
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes" })
	private void stockControl() throws Exception {
//        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
//        PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Boolean flag = false;
        //如果是直发(委托) 开关控制
//        String key = CommonDAO.getPara(Constant.PART_SALE_STOCK_CONTROL + "");
        String transId = CommonUtils.checkNull(request.getParamValue("transId"));//发运单id
        Map<String, Object> dataMap = dao.getTransMain(transId);//查询发运单
//        String soId = CommonUtils.checkNull(dataMap.get("SO_ID"));//销售单id
        //Long outId = Long.valueOf(CommonUtils.checkNull(dataMap.get("OUT_ID")));
//        Map<String, Object> mainMap = partSoManageDao.getSoOrderMain(soId);//查询销售单
        String orderType = CommonUtils.checkNull(dataMap.get("ORDER_TYPE"));//订单类型
        //如果是直发   并且通过
        //PartSalesOrderFinCheck psofc = new PartSalesOrderFinCheck();
        //insertTtPartPoIn(CommonUtils.checkNull(request.getAttribute("soId")));
        if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "") && flag) {
            ArrayList ins = new ArrayList();
            ins.add(0, Long.valueOf(CommonUtils.checkNull(request.getAttribute("soId"))));
            ins.add(1, 0);
            ins.add(2, 1);
            dao.callProcedure("PKG_PART.p_createdirectorder", ins, null);
//            ArrayList ins = new ArrayList();
//            ins.add(0, Long.valueOf(CommonUtils.checkNull(request.getAttribute("soId"))));
//            dao.callProcedure("PROC_TT_PART_DIRECTORDER", ins, null);
            
        }
        //供应中心代订，出库单和入库单补充
        /*if(!orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04+"")){
            ArrayList ins = new ArrayList();
            ins.add(0, Long.valueOf(CommonUtils.checkNull(request.getAttribute("soId"))));
            dao.callProcedure("PKG_PART.P_CREATEGYZXORDER",ins,null);
        }*/
    }

    /*20170829 注释
     public void insertTtPartPoIn(String soId) throws Exception {
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

            String trlineId = CommonUtils.checkNull(map.get("TRLINE_ID"));
            String inQty = request.getParamValue("inQty_" + trlineId);
            if (inQty == null || inQty.equals("0")) {
                continue;
            }
            TtPartPoInPO inPo = new TtPartPoInPO();//需要将每次入库的信息保存到入库表中
            //设置入库信息
            Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
            inPo.setInId(InId);
            String inCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_04);//入库单编码
            inPo.setInCode((inCode));
            inPo.setInType(Constant.PART_PURCHASE_ORDERCIN_TYPE_02);
                    }
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
            inPo.setInQty(Long.valueOf(inQty));//已入库数量
            inPo.setBuyPriceNotax(Arith.mul(Double.valueOf(price), Arith.sub(1, Constant.PART_TAX_RATE)));//无税单价
            inPo.setInAmount(Arith.mul(Double.valueOf(price), inPo.getInQty()));//采购金额
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
            this.insertPartRecord2(map, 1);
            ArrayList ins = new ArrayList();
            ins.add(0, InId);
            ins.add(1, Constant.PART_CODE_RELATION_04);
            ins.add(2, 1);
            dao.callProcedure("PKG_PART.p_updatepartstock", ins, null);

        }
    }*/


//    /**
//     * @param :
//     * @return :
//     * @throws : LastDate    : 2013-4-18
//     * @Title : 经销商入库记录，经销商入库不存在货位和批次
//     * @Description: 插入预占
//     */
//    public void insertPartRecord(Map<String, Object> map) throws Exception {
//        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//        RequestWrapper request = act.getRequest();
//        PartSoManageDao dao = PartSoManageDao.getInstance();
//        PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
//        PartDlrInstockDao partDlrInstockDao = PartDlrInstockDao.getInstance();
//        TtPartRecordPO insertPRPo = new TtPartRecordPO();
////        String partBatch = Constant.PART_RECORD_BATCH;//配件批次
//        
//        String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
//        String configId = Constant.PART_CODE_RELATION_26 + "";//配置ID
//        String orgId = "";
//        String orgName = "";
//        String orgCode = "";
//        try {
////            Map<String, Object> mainMap = dao.getSoOrderMain(CommonUtils.checkNull(map.get("SO_ID")));
//            //判断是否为车厂  PartWareHouseDao
//            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
//            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
//            if (null != beanList && beanList.size() >= 0) {
//                orgId = beanList.get(0).getOrgId() + "";
//                orgName = beanList.get(0).getOrgCode() + "";
//                orgCode = beanList.get(0).getOrgCode() + "";
//            }
//            String partBatch = CommonUtils.checkNull(map.get("BATCH_NO"));//配件批次 20170829 add
////            String partBatch = "1306";
//            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
//            String trlineId = CommonUtils.checkNull(map.get("TRLINE_ID"));
//            String inQty = request.getParamValue("inQty_" + trlineId);
//            Map<String, Object> warehouseMap = partOutstockDao.getWarehouse(whId);
//            String inId = CommonUtils.checkNull(request.getAttribute("inId"));
////            Map<String,Object> locMap = partOutstockDao.queryPartLocationByPartId(CommonUtils.checkNull(map.get("PART_ID")),whId);
//
//            Map<String, Object> locMap = partDlrInstockDao.getLoc(CommonUtils.checkNull(map.get("PART_ID")), whId, orgId);
//            String locName = CommonUtils.checkNull(locMap.get("LOC_NAME"));
//            String locId = CommonUtils.checkNull(locMap.get("LOC_ID"));
//            String locCode = CommonUtils.checkNull(locMap.get("LOC_CODE"));
//            if (map.isEmpty()) {
//                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "货位出错,请维护货位!");
//                throw e1;
//            }
//            Long recId = Long.parseLong(SequenceManager.getSequence(""));
//            insertPRPo.setRecordId(recId);
//            insertPRPo.setAddFlag(1);//入库
//            insertPRPo.setPartId(Long.parseLong(CommonUtils.checkNull(map.get("PART_ID"))));
//            insertPRPo.setPartCode(CommonUtils.checkNull(map.get("PART_CODE")));
//            insertPRPo.setPartOldcode(CommonUtils.checkNull(map.get("PART_OLDCODE")));
//            insertPRPo.setPartName(CommonUtils.checkNull(map.get("PART_CNAME")));
//            insertPRPo.setPartBatch(partBatch);
//            insertPRPo.setVenderId(Long.parseLong(partVenId));
//            insertPRPo.setPartNum(Long.parseLong(inQty));//入库数量
//            insertPRPo.setConfigId(Long.parseLong(configId));
//            insertPRPo.setOrderId(Long.parseLong(inId));//业务ID
//            insertPRPo.setOrgId(Long.parseLong(orgId));
//            insertPRPo.setOrgCode(orgCode);
//            insertPRPo.setOrgName(orgName);
//            insertPRPo.setWhId(Long.valueOf(whId));
//            insertPRPo.setWhCode(CommonUtils.checkNull(locMap.get("LOC_NAME")));
//            insertPRPo.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));
//
//            insertPRPo.setLocId(Long.parseLong(locId));
//            insertPRPo.setLocCode(locCode);
//            insertPRPo.setLocName(locName);
//
//            insertPRPo.setOptDate(new Date());
//            insertPRPo.setCreateDate(new Date());
//            insertPRPo.setPersonId(loginUser.getUserId());
//            insertPRPo.setPersonName(loginUser.getName());
//            insertPRPo.setPartState(1);//配件状态
//            dao.insert(insertPRPo);
//        } catch (Exception ex) {
//            throw ex;
//        }
//
//    }

//    public void insertMain() throws Exception {
//        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//        RequestWrapper request = act.getRequest();
//        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
//        try {
//            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
//            Map<String, Object> dataMap = dao.getTransMain(transId);
//            String inCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_26);
//            request.setAttribute("inCode", inCode);
//            String remark = CommonUtils.checkNull(request.getParamValue("remark"));
//            String transCode = CommonUtils.checkNull(dataMap.get("TRANS_CODE"));
//            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
//            PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
//            Map<String, Object> warehouseMap = partOutstockDao.getWarehouse(whId);
//            String whName = CommonUtils.checkNull(warehouseMap.get("WH_NAME"));
//            String orderId = CommonUtils.checkNull(dataMap.get("ORDER_ID"));
//            String orderCode = CommonUtils.checkNull(dataMap.get("ORDER_CODE"));
//            String orderType = CommonUtils.checkNull(dataMap.get("ORDER_TYPE"));
//            String dealerId = CommonUtils.checkNull(dataMap.get("DEALER_ID"));
//            String dealerCode = CommonUtils.checkNull(dataMap.get("DEALER_CODE"));
//            String dealerName = CommonUtils.checkNull(dataMap.get("DEALER_NAME"));
//            String sellerId = CommonUtils.checkNull(dataMap.get("SELLER_ID"));
//            String sellerCode = CommonUtils.checkNull(dataMap.get("SELLER_CODE"));
//            String sellerName = CommonUtils.checkNull(dataMap.get("SELLER_NAME"));
//            String RDate = CommonUtils.checkNull(request.getParamValue("RDate"));
//            String soId = CommonUtils.checkNull(dataMap.get("SO_ID"));
//            request.setAttribute("soId", soId);
//            String soCode = CommonUtils.checkNull(dataMap.get("SO_CODE"));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = sdf.parse(RDate);
//            TtPartDlrInstockMainPO po = new TtPartDlrInstockMainPO();
//            Long inId = Long.parseLong(SequenceManager.getSequence(""));
//            request.setAttribute("inId", inId);
//            po.setInId(inId);
//            po.setInCode(inCode);
//            po.setTransId(Long.valueOf(transId));
//            po.setTransCode(transCode);
//            po.setRemark2(remark);
//            po.setWhId(Long.valueOf(whId));
//            po.setWhName(whName);
//            if (!"".equals(orderId)) {
//                po.setOrderId(Long.valueOf(orderId));
//            }
//            if (!"".equals(soId)) {
//                po.setSoId(Long.valueOf(soId));
//            }
//			po.setSoCode(soCode);
//			po.setOrderCode(orderCode);
//			po.setOrderType(Integer.valueOf(orderType));
//            po.setDealerId(Long.valueOf(dealerId));
//            po.setDealerName(dealerName);
//            po.setDealerCode(dealerCode);
//            po.setSellerId(Long.valueOf(sellerId));
//            po.setSellerCode(sellerCode);
//            po.setSellerName(sellerName);
//            po.setCreateDate(new Date());
//            po.setSaleDate(new Date());
//            po.setArrivalDate(date);
//            po.setCreateBy(loginUser.getUserId());
//            po.setVer(1);
//            po.setState(Constant.CAR_FACTORY_INSTOCK_STATE_01);
//            dao.insert(po);
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

//    public void insertDtl() throws Exception {
//        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//        RequestWrapper request = act.getRequest();
//        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
//        try {
//            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
//            List<Map<String, Object>> detailList = dao.getTransDetail(transId);
//            Map<String, Object> mainMap = dao.getTransMain(transId);
//            Date date = new Date();
//            String inId = CommonUtils.checkNull(request.getAttribute("inId"));
//            String[] partIds = request.getParamValues("partIds");
//            for(int i = 0; i < partIds.length; i++){
////                String trlineId = CommonUtils.checkNull(map.get("TRLINE_ID")); // 发运单明细id
//                String inCode = CommonUtils.checkNull(request.getAttribute("inCode")); // 入库编码
//                String partId = partIds[i]; // 发运单明细id
//                
//                String inQty = CommonUtils.checkNull(request.getParamValue("inQty_" + partId)); // 入库数量
//                String inType = CommonUtils.checkNull(request.getParamValue("inType_" + partId)); // 入库类型
//                String remark = CommonUtils.checkNull(request.getParamValue("remark_" + partId)); // 备注
////                Long inlineId = Long.parseLong(SequenceManager.getSequence(""));
//                String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); // 件号
//                String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); // 配件编码
//                String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME")); // 配件名称
//                String unit = CommonUtils.checkNull(request.getParamValue("UNIT")); // 单位
//                String minPackage = CommonUtils.checkNull(request.getParamValue("MIN_PACKAGE")); // 最小包装量
//                String buyQty = CommonUtils.checkNull(request.getParamValue("BUY_QTY")); // 采购数量
//                String transQty = CommonUtils.checkNull(request.getParamValue("TRANS_QTY")); // 销售数量
//                String soId = CommonUtils.checkNull(mainMap.get("SO_ID")); // 销售id
//                String soCode = CommonUtils.checkNull(mainMap.get("SO_CODE")); // 销售单号
//                TtPartDlrInstockDtlPO po = new TtPartDlrInstockDtlPO();
//                if (inQty == null || inQty.equals("0")) {
//                    continue;
//                }
//                po.setInlineId(Long.valueOf(inId) + (long)(i + 1));
//                po.setInQty(Long.valueOf(inQty));
//                po.setInType(Integer.valueOf(inType));
//                po.setTransId(Long.valueOf(transId));
//                po.setRemark(remark);
//                po.setInId(Long.valueOf(inId));
//                po.setPartCode(partCode);
//                po.setPartId(Long.valueOf(partId));
//                po.setPartOldcode(partOldcode);
//                po.setPartCname(partCname);
//                po.setUnit(unit);
//                po.setMinPackage(Long.valueOf(minPackage));
//                po.setBuyQty(Long.valueOf(buyQty));
//                po.setTransQty(Long.valueOf(transQty));
//                po.setCreateBy(loginUser.getUserId());
//                po.setCreateDate(date);
//                po.setVer(1);
//                dao.insert(po);
//                // 破/缺件
//                if (!"".equals(inType) && !inType.equals(Constant.CAR_FACTORY_INSTOCK_APPROVAL_STATE_01 + "")) {
//                    TtPartInstockExceptionLogPO exceptionPo = new TtPartInstockExceptionLogPO();
//                    Long exceptionId = Long.parseLong(SequenceManager.getSequence(""));
//                    String exceptionNum = request.getParamValue("exceptionNum_" + partId);
//                    exceptionPo.setExceptionId(exceptionId);
//                    exceptionPo.setExceptionNum(Long.valueOf(exceptionNum));
//                    exceptionPo.setExceptionRemark(remark);
//                    exceptionPo.setCreateBy(loginUser.getUserId());
//                    exceptionPo.setCreateDate(date);
//                    exceptionPo.setInId(Long.valueOf(inId));
//                    exceptionPo.setInCode(inCode);
//					exceptionPo.setSoId(Long.valueOf(soId));
//                    exceptionPo.setSoCode(soCode);
//                    exceptionPo.setState(Constant.STATUS_ENABLE);
//                    exceptionPo.setPartId(Long.valueOf(partId));
//                    exceptionPo.setState(Constant.INSTOCK_EXCEPTION_REPLY_01);
//                    dao.insert(exceptionPo);
//                }
////                insertPartRecord(map);
//            }
//            if (dao.checkShowExpPrint(inId)) {
//                act.setOutData("inId", inId);
//                act.setOutData("expFlag", 1);
//            }
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

    /**
     * <p>
     * Description: 更新发运单状态
     * </p>
     * @throws Exception
     */
    public void changeStatus() throws Exception {
        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
        try {
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));

//            Map<String, Object> dataMap = dao.getTransMain(transId);
//            String soId = CommonUtils.checkNull(dataMap.get("SO_ID"));

            TtPartTransPO oldPo = new TtPartTransPO();
            oldPo.setTransId(Long.valueOf(transId));
            TtPartTransPO newPo = new TtPartTransPO();
            newPo.setTransId(Long.valueOf(transId));
            newPo.setState(Constant.CAR_FACTORY_OUTSTOCK_STATE_05);
            /*TtPartSoMainPO oldSoPo = new TtPartSoMainPO();
            oldSoPo.setSoId(Long.valueOf(soId));
			TtPartSoMainPO newSoPo = new TtPartSoMainPO();
			newSoPo.setSoId(Long.valueOf(soId));
			newPo.setState(Constant.CAR_FACTORY_OUTSTOCK_STATE_05);*/
            dao.update(oldPo, newPo);
//			dao.update(oldSoPo, newSoPo);
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
            PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
            PartPlanQueryDao partPlanQueryDao = PartPlanQueryDao.getInstance();
            String[] head = new String[12];
            head[0] = "承运物流 ";
            head[1] = "发运单 ";
            head[2] = "采购单号";
//			head[2]="发票号";
//			head[3]="订货单位";
            head[3] = "制单人";
            head[4] = "发运日期";
            head[5] = "销售单位";
            head[6] = "订单类型";
            head[7] = "采购金额";
            PageResult<Map<String, Object>> ps = dao.queryInstockOrder(request, 1, Constant.PAGE_SIZE_MAX);
            Map<String, Object> planTypeMap = partPlanQueryDao.getTcCodeMap(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE.toString());//类型
            List<Map<String, Object>> list = ps.getRecords();
            list = list == null ? new ArrayList() : list;
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("TRANSPORT_ORG_CN"));
                    detail[1] = CommonUtils.checkNull(map.get("TRANS_CODE"));
                    detail[2] = CommonUtils.checkNull(map.get("ORDER_CODE"));
//						detail[2] = CommonUtils.checkNull(null);
//						detail[3] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[3] = CommonUtils.checkNull(map.get("CREATE_BY_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                    detail[5] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                    detail[6] = CommonUtils.checkNull(map.get("ORDER_TYPE") == null ? null : planTypeMap.get(CommonUtils.checkNull(map.get("ORDER_TYPE"))));
                    detail[7] = CommonUtils.checkNull(map.get("AMOUNT"));
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
        String name = "配件入库列表.xls";
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

    /*
     *20170829 注释 
     public void insertPartRecord2(Map<String, Object> map, int addFlag) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartSoManageDao dao = PartSoManageDao.getInstance();
        PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
        TtPartRecordPO insertPRPo = new TtPartRecordPO();
        PartDlrInstockDao partDlrInstockDao = PartDlrInstockDao.getInstance();
        String partBatch = Constant.PART_RECORD_BATCH;//配件批次
        String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
        String configId = Constant.PART_CODE_RELATION_09 + "";//配置ID
        String orgId = Constant.OEM_ACTIVITIES;
        String orgCode = Constant.ORG_ROOT_CODE;
        try {
            Map<String, Object> mainMap = dao.getSoOrderMain(CommonUtils.checkNull(map.get("SO_ID")));
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);

            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
            Map<String, Object> warehouseMap = partOutstockDao.getWarehouse(whId);
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
            Map<String, Object> dataMap = partDlrInstockDao.getTransMain(transId);
            String soId = CommonUtils.checkNull(dataMap.get("SO_ID"));
            Long outId = Long.valueOf(CommonUtils.checkNull(dataMap.get("OUT_ID")));
            Map<String, Object> locMap = partOutstockDao.queryPartLocationByPartId(CommonUtils.checkNull(map.get("PART_ID")), whId);
            Long recId = Long.parseLong(SequenceManager.getSequence(""));
            insertPRPo.setRecordId(recId);
            insertPRPo.setAddFlag(addFlag);
            insertPRPo.setPartId(Long.parseLong(CommonUtils.checkNull(map.get("PART_ID"))));
            insertPRPo.setPartCode(CommonUtils.checkNull(map.get("PART_CODE")));
            insertPRPo.setPartOldcode(CommonUtils.checkNull(map.get("PART_OLDCODE")));
            insertPRPo.setPartName(CommonUtils.checkNull(map.get("PART_CNAME")));
            insertPRPo.setPartBatch(partBatch);
            insertPRPo.setVenderId(Long.parseLong(partVenId));
            insertPRPo.setPartNum(Long.parseLong(CommonUtils.checkNull(map.get("SALES_QTY"))));//出库数量
            insertPRPo.setConfigId(Long.parseLong(configId));
            insertPRPo.setOrderId(outId);//业务ID
            insertPRPo.setOrgId(Long.parseLong(orgId));
            insertPRPo.setOrgCode(orgCode);
            insertPRPo.setWhId(Long.valueOf(whId));
            insertPRPo.setWhCode(CommonUtils.checkNull(warehouseMap.get("WH_CODE")));
            insertPRPo.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));


            insertPRPo.setLocId(Long.parseLong(CommonUtils.checkNull(locMap.get("LOC_ID"))));
            insertPRPo.setLocCode(CommonUtils.checkNull(locMap.get("LOC_CODE")));
            insertPRPo.setLocName(CommonUtils.checkNull(locMap.get("LOC_NAME")));
            insertPRPo.setOptDate(new Date());
            insertPRPo.setCreateDate(new Date());
            insertPRPo.setPersonId(Long.valueOf(CommonUtils.checkNull(mainMap.get("CREATE_BY"))));
            insertPRPo.setPersonName(loginUser.getName());
            insertPRPo.setPartState(1);//配件状态
            dao.insert(insertPRPo);
        } catch (Exception ex) {
            throw ex;
        }
    }*/

//    public void getLoc() {
//        ActionContext act = ActionContext.getContext();
//        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//        RequestWrapper request = act.getRequest();
//        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
//        try {
//            act.getResponse().setContentType("application/json");
//            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
//            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
//            String orgId = "";
//            //判断是否为车厂  PartWareHouseDao
//            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
//            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
//            if (null != beanList && beanList.size() >= 0) {
//                orgId = beanList.get(0).getOrgId() + "";
//            }
//            List<Map<String, Object>> list = dao.getTransDetail(transId);
//            for (Map<String, Object> map : list) {
//                Map<String, Object> locMap = dao.getLoc(CommonUtils.checkNull(map.get("PART_ID")), whId, orgId);
//                map.put("LOC_NAME", CommonUtils.checkNull(locMap.get("LOC_NAME")));
//                map.put("LOC_ID", CommonUtils.checkNull(locMap.get("LOC_ID")));
//                String qty = dao.getStockQty(orgId, whId, CommonUtils.checkNull(map.get("PART_ID")));
//                map.put("QTY", qty);
//            }
//            act.setOutData("locList", list);
//        } catch (Exception e) {//异常方法
//            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取数据错误!");
//            logger.error(loginUser, e1);
//            act.setException(e1);
//        }
//    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 保存日志
     */
    public void saveHistory(int state) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
        RequestWrapper request = act.getRequest();
        try {
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
            Map<String, Object> dataMap = dao.getTransMain(transId);
            String soCode = CommonUtils.checkNull(dataMap.get("SO_CODE"));
//            String inId = CommonUtils.checkNull(request.getAttribute("inId"));
            TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
            Long optId = Long.parseLong(SequenceManager.getSequence(""));
            po.setBussinessId(soCode);
            po.setOptId(optId);
            po.setOptBy(logonUser.getUserId());
            po.setOptDate(new Date());
            po.setOptType(Constant.PART_OPERATION_TYPE_02);
            po.setWhat("配件入库");
            po.setOptName(logonUser.getName());
            po.setStatus(state);
            if (!"".equals(CommonUtils.checkNull(dataMap.get("ORDER_ID")))) {
                po.setOrderId(Long.valueOf(CommonUtils.checkNull(dataMap.get("ORDER_ID"))));
            }
            dao.insert(po);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 打印入库单
     */
    public void opPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
        PartPkgDao partPkgDao = PartPkgDao.getInstance();
//        PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
//        Map<String, Object> dataMap = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
            List<Map<String, Object>> mainList = dao.getInstockMain(transId);
            if (mainList == null || mainList.size() <= 0) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "打印数据错误,请联系管理员!");
                throw e1;
            }
            Map<String, Object> mainMap = mainList.get(0);
//            Map<String, Object> soMainMap = partSoManageDao.getSoOrderMain(transId);
//            mainMap.putAll(soMainMap);

            String transTypeName = partPkgDao.getTranTypeName(CommonUtils.checkNull(mainMap.get("TRANS_TYPE")));
            String whName = partPkgDao.getWhName(CommonUtils.checkNull(mainMap.get("WH_ID")));

            mainMap.put("transTypeName", transTypeName);
            mainMap.put("whName", whName);
            mainMap.put("date", sdf.format(new Date()));
            List<Map<String, Object>> inList = dao.getInDetail(CommonUtils.checkNull(mainMap.get("IN_ID")));
            act.setOutData("mainMap", mainMap);
            act.setOutData("inList", inList);
            act.setForword(PRINT);
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);

                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "打印数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    /**
     * 打印配件赔偿申请清单
     */
    public void payApplyPrintHtml() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
            List<Map<String, Object>> mainList = dao.getPayApplyMain(transId);
            if (mainList == null || mainList.size() <= 0) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "打印数据错误,请联系管理员!");
                throw e1;
            }
            Map<String, Object> mainMap = mainList.get(0);

            mainMap.put("date", sdf.format(new Date()));
            List<Map<String, Object>> inList = dao.getPayDetail(transId);
            act.setOutData("mainMap", mainMap);
            act.setOutData("inList", inList);
            act.setForword(PAY_APPLY_PRINT);
        } catch (Exception e) {//异常方法
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);

                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "打印数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);

        }
    }

    /**
     * 经销商入库明细下载
     * 明细下载 add zhumingwei 2013-10-17
     */
    public void exportExcelDetal() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String transId = CommonUtils.checkNull(request.getParamValue("transId"));
            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            //head[3] = "件号";
            head[3] = "最小包装量";
            head[4] = "单位";
            head[5] = "采购数量";
            head[6] = "采购单价";
            head[7] = "采购金额";
            head[8] = "备注";

            PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
            List<Map<String, Object>> list = dao.exportPurOrder(transId);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[10];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        //detail[3] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("MIN_PACKAGE"));
                        detail[4] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[5] = CommonUtils.checkNull(map.get("TRANS_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("SALE_PRICE1"));
                        detail[7] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
                        detail[8] = CommonUtils.checkNull(map.get("REMARK"));
                        list1.add(detail);
                    }
                }
                this.exportEx1(ActionContext.getContext().getResponse(), request, head, list1);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }
        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_INSTOCK_MAIN);
        }
    }

    public static Object exportEx1(ResponseWrapper response, RequestWrapper request, String[] head, List<String[]> list) throws Exception {
        String name = "接收入库单明细下载.xls";
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

    public void exportExcelDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartDlrInstockDao dao = PartDlrInstockDao.getInstance();
//            PartPlanQueryDao partPlanQueryDao = PartPlanQueryDao.getInstance();
            String[] head = new String[15];
            head[0] = "序号 ";
            head[1] = "发运单号";
            head[2] = "采购单号";
            head[3] = "销售单号";
            head[4] = "订单类型";
            head[5] = "发运日期";
            head[6] = "配件编码";
            head[7] = "配件名称";
            //head[8]="件号";
            head[8] = "最小包装量";
            head[9] = "单位";
            head[10] = "采购数量";
            head[11] = "采购单价";
            head[12] = "采购金额";

            List<Map<String, Object>> list = dao.exportDetail(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("TRANS_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("SO_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                        detail[5] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        //detail[8] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[8] = CommonUtils.checkNull(map.get("MIN_PACKAGE"));
                        detail[9] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[10] = CommonUtils.checkNull(map.get("TRANS_QTY"));
                        detail[11] = CommonUtils.checkNull(map.get("SALE_PRICE1"));
                        detail[12] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));

                        list1.add(detail);
                    }
                }
                this.exportEx2(ActionContext.getContext().getResponse(), request, head, list1);
            } else {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public static Object exportEx2(ResponseWrapper response, RequestWrapper request, String[] head, List<String[]> list) throws Exception {
        String name = "接收入库单本经销商明细下载.xls";
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

}
