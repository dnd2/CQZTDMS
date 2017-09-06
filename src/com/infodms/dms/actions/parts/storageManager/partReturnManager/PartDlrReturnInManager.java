package com.infodms.dms.actions.parts.storageManager.partReturnManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartDlrReturnInDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartAccountDefinePO;
import com.infodms.dms.po.TtPartAccountRecordPO;
import com.infodms.dms.po.TtPartDlrReturnDtlPO;
import com.infodms.dms.po.TtPartDlrReturnMainPO;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.po.TtPartReturnRecordPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.po.VwPartStockPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PartDlrReturnInManager implements PTConstants {

    public Logger logger = Logger.getLogger(PartDlrReturnInManager.class);
    private PartDlrReturnInDao dao = PartDlrReturnInDao.getInstance();
    private PurchaseOrderInDao orderInDao = PurchaseOrderInDao.getInstance();

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title :
     * @Description: 销售退货入库查询初始化，转到查询页面
     */
    public void queryPartReturnApplyInit() {
        ActionContext act = ActionContext.getContext();
//        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Map<String, String> stateMap = new LinkedHashMap<String, String>();
            stateMap.put(Constant.PART_DLR_RETURN_STATUS_05 + "", "已回运");
            stateMap.put(Constant.PART_DLR_RETURN_STATUS_06 + "", "已入库");
            stateMap.put(Constant.PART_DLR_RETURN_STATUS_08 + "", "已关闭");
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("stateMap", stateMap);
            act.setForword(PART_RETURN_IN_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title :
     * @Description: 查询销售退货信息
     */
    public void queryPartDlrReturnApplyInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//退货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//制单结束时间
            String state = CommonUtils.checkNull(request.getParamValue("state"));//状态

            boolean flag = true;//是否是车厂
            if (logonUser.getDealerId() != null) {
                flag = false;
            }
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDlrReturnApplyList(returnCode, dealerName, startDate, endDate, flag, logonUser, dealerCode, curPage, Constant.PAGE_SIZE, state);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-27
     * @Title :
     * @Description: 销售退货入库初始化, 转到入库页面
     */
    public void queryPartDlrReturnInInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
//            boolean flag = true;//是否是车厂
//            if (logonUser.getDealerId() != null) {
//                flag = false;
//            }
            Map<String, Object> map = dao.getPartDlrReturnMainInfo(returnId);
            List<PO> list = dao.getPartWareHouseList(logonUser,"");//获取当前机构的库房信息
            act.setOutData("wareHouses", list);
            request.setAttribute("po", map);
            act.setForword(PART_RETURN_IN_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货主信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-27
     * @Title :
     * @Description: 查询销售退货明细
     */
    public void queryPartDlrReturnInfo() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDlrReturnInfoList(returnId, soCode, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货申请明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-27
     * @Title :
     * @Description: 销售退货入库
     */
    @SuppressWarnings("unchecked")
    public void inPartDlrReturn() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String errors = "";
        String dtlError = "";
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String[] dtlIds = request.getParamValues("ck");//退货明细id

            TtPartDlrReturnMainPO sPartDlrReturnMainPO = new TtPartDlrReturnMainPO();//源po
            TtPartDlrReturnMainPO mainPO = new TtPartDlrReturnMainPO();//更新po
            sPartDlrReturnMainPO.setReturnId(CommonUtils.parseLong(returnId));

            TtPartDlrReturnMainPO partDlrReturnMainPO = new TtPartDlrReturnMainPO();
            partDlrReturnMainPO.setReturnId(CommonUtils.parseLong(returnId));
            partDlrReturnMainPO = (TtPartDlrReturnMainPO) dao.select(partDlrReturnMainPO).get(0);

            boolean flag = false;
            
            String batchNo = dao.getBatchNo(returnId); // 批次号
            long venderId = logonUser.getOrgId(); // 供应商

            if (partDlrReturnMainPO.getState().intValue() == Constant.PART_DLR_RETURN_STATUS_05.intValue()) {//退货单状态为入库中才能入库
                TtPartWarehouseDefinePO warehousePO = new TtPartWarehouseDefinePO();
                warehousePO.setWhId(CommonUtils.parseLong(whId));
                warehousePO = (TtPartWarehouseDefinePO) dao.select(warehousePO).get(0);//获取当前所选入库仓库信息

                if (!"".equals(whId)) {
                    mainPO.setStockIn(CommonUtils.parseLong(whId));//入库库房
                    mainPO.setInDate(new Date());//入库日期
                    mainPO.setInBy(logonUser.getUserId());//入库人
                }

                double amount = 0;//退货金额
                Long stockOut = partDlrReturnMainPO.getStockOut();//退货单位出库库房

                for (int i = 0; i < dtlIds.length; i++) {
                    
                    TtPartDlrReturnDtlPO po = new TtPartDlrReturnDtlPO();
                    po.setDtlId(CommonUtils.parseLong(dtlIds[i]));
                    po = (TtPartDlrReturnDtlPO) dao.select(po).get(0);

                    // 获取退货配件的批次号
//                    Map<String, Object> poMap = dao.getReturnPartDtl(dtlIds[i]).get(0);
//                    String batchNo = CommonUtils.checkNull(poMap.get("BATCH_NO")); // 批次号
//                    String batchNo = "1306"; // 批次号
                    
                    if (po.getStatus().intValue() == 2) {//如果已经入完成就不能再次入库
                        dtlError += "配件【" + po.getPartCname() + "】已经完成入库,不能重复入库!<br>";
                    } else if(CommonUtils.isEmpty(batchNo)){
                        dtlError += "配件【" + po.getPartCname() + "】批次号错误,不能入库!<br>";
                    } else {
                        flag = true;
                        String inQty = CommonUtils.checkNull(request.getParamValue("IN_QTY" + dtlIds[i]));
                        TtPartDlrReturnDtlPO returnDtlPO2 = new TtPartDlrReturnDtlPO();
                        TtPartDlrReturnDtlPO sdtDtlPO = new TtPartDlrReturnDtlPO();
                        sdtDtlPO.setDtlId(po.getDtlId());
                        TtPartDlrReturnDtlPO dtlPO2 = new TtPartDlrReturnDtlPO();
//                            dtlPO2.setStatus(2);//更新退货明细状态为已完全入库
                        //后台校验已验收数量与待验收数量之和不能大于审核数量
                        returnDtlPO2 = (TtPartDlrReturnDtlPO) dao.select(sdtDtlPO).get(0);
                        dtlPO2.setInQty(CommonUtils.parseLong(inQty) + returnDtlPO2.getInQty());//设置入库数量
                        dtlPO2.setOutQty(CommonUtils.parseLong(inQty) + returnDtlPO2.getInQty());//设置出库数量
                        dtlPO2.setReturnQty(CommonUtils.parseLong(inQty) + returnDtlPO2.getInQty());//已退货数量

                        if ((returnDtlPO2.getInQty() + CommonUtils.parseLong(inQty) == returnDtlPO2.getCheckQty())) {
                            dtlPO2.setStatus(2);//更新退货明细状态为已完全入库
                        }

                        if ((returnDtlPO2.getInQty() + CommonUtils.parseLong(inQty) > returnDtlPO2.getCheckQty())) {
                            dtlError += "配件【" + po.getPartCname() + "】总验收数量不能大于审核数量,不能入库!";
                            BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "总验收数量不能大于审核数量,请联系管理员!");
                            throw e1;
                        }
                        String locId = CommonUtils.checkNull(request.getParamValue("LOC_ID_" + po.getPartId()));
                        String locCode = CommonUtils.checkNull(request.getParamValue("LOC_CODE_" + po.getPartId()));
                        String locName = CommonUtils.checkNull(request.getParamValue("LOC_NAME_" + po.getPartId()));
                        if (locId == null && !"".equals(locId)) {
                            BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "销售退货入库失败,请联系管理员!");
                            throw e1;
                        }
                        dtlPO2.setBuyAmount(Arith.mul(CommonUtils.parseLong(inQty), po.getBuyPrice()));//退货行金额
                        amount += dtlPO2.getBuyAmount();
                        dtlPO2.setInBatchNo(batchNo);
                        dtlPO2.setInlocId(Long.parseLong(locId));

                        
                        //销售退货完成之后需要向退货记录表中插入退货信息
                        TtPartReturnRecordPO ttPartReturnRecordPO = new TtPartReturnRecordPO();
                        ttPartReturnRecordPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                        ttPartReturnRecordPO.setReturnId(po.getReturnId());
                        ttPartReturnRecordPO.setReturnCode(partDlrReturnMainPO.getReturnCode());
                        ttPartReturnRecordPO.setReturnType(Constant.PART_RETURN_TYPE_01);//退货类型
                        ttPartReturnRecordPO.setSourceId(partDlrReturnMainPO.getSoId());
                        ttPartReturnRecordPO.setSourceCaode(partDlrReturnMainPO.getSoCode());
                        ttPartReturnRecordPO.setPartId(po.getPartId());
                        ttPartReturnRecordPO.setPartCode(po.getPartCode());
                        ttPartReturnRecordPO.setPartOldcode(po.getPartOldcode());
                        ttPartReturnRecordPO.setPartCname(po.getPartCname());
                        ttPartReturnRecordPO.setUnit(po.getUnit());
                        ttPartReturnRecordPO.setReturnQty(CommonUtils.parseLong(inQty));
                        ttPartReturnRecordPO.setRemark(po.getRemark());
                        ttPartReturnRecordPO.setCreateDate(new Date());
                        ttPartReturnRecordPO.setCreateBy(logonUser.getUserId());
                        ttPartReturnRecordPO.setStockOut(partDlrReturnMainPO.getStockOut());
                        ttPartReturnRecordPO.setStockIn(mainPO.getStockIn());
                        ttPartReturnRecordPO.setLocId(Long.parseLong(locId));
                        ttPartReturnRecordPO.setBatchNo(batchNo);
                        

                        //插入出入库记录表 (入库)
                        TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
                        ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                        ttPartRecordPO.setAddFlag(1);//入库标记
                        ttPartRecordPO.setState(1);//正常入库
                        ttPartRecordPO.setPartNum(CommonUtils.parseLong(inQty));//入库数量
                        ttPartRecordPO.setTranstypeId(0l);//默认0
                        ttPartRecordPO.setPartId(po.getPartId());//配件ID
                        ttPartRecordPO.setPartCode(po.getPartCode());//配件件号
                        ttPartRecordPO.setPartOldcode(po.getPartOldcode());//配件编码
                        ttPartRecordPO.setPartName(po.getPartCname());//配件名称
                        ttPartRecordPO.setPartBatch(batchNo);//////////////////配件批次
                        ttPartRecordPO.setVenderId(venderId);///////////////////配件供应商
                        ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_30));//入库单
                        ttPartRecordPO.setOrderId(po.getReturnId());//退货单ID
                        ttPartRecordPO.setOrderCode(partDlrReturnMainPO.getReturnCode());//退货单编码
                        List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
                        ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
                        ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
                        ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());
                        ttPartRecordPO.setWhId(CommonUtils.parseLong(whId));
                        ttPartRecordPO.setWhName(warehousePO.getWhName());
                        ttPartRecordPO.setLocId(Long.parseLong(locId));
                        ttPartRecordPO.setLocCode(locCode);
                        ttPartRecordPO.setLocName(locName);
                        ttPartRecordPO.setOptDate(new Date());
                        ttPartRecordPO.setCreateDate(new Date());
                        ttPartRecordPO.setPersonId(logonUser.getUserId());
                        ttPartRecordPO.setPersonName(logonUser.getName());
                        ttPartRecordPO.setPartState(1);

                        //入库的同时需要对退货单位进行出库(退货单位有仓库的情况)
                        //插入信息到出入库记录表 (出库)
                        TtPartRecordPO ttPartRecordPO1 = new TtPartRecordPO();
                        ttPartRecordPO1.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                        ttPartRecordPO1.setAddFlag(2);//出库标记
                        ttPartRecordPO1.setState(1);//正常出库
                        ttPartRecordPO1.setPartNum(CommonUtils.parseLong(inQty));//出库数量
                        ttPartRecordPO1.setTranstypeId(0l);//默认0
                        ttPartRecordPO1.setPartId(po.getPartId());//配件ID
                        ttPartRecordPO1.setPartCode(po.getPartCode());//配件件号
                        ttPartRecordPO1.setPartOldcode(po.getPartOldcode());//配件编码
                        ttPartRecordPO1.setPartName(po.getPartCname());//配件名称
                        ttPartRecordPO1.setPartBatch("1306");//////////////////配件批次-经销商的批次号都是1306
                        ttPartRecordPO1.setVenderId(Long.parseLong(Constant.PART_RECORD_VENDER_ID));///////////////////配件供应商
                        ttPartRecordPO1.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_29));//出库单
                        ttPartRecordPO1.setOrderId(po.getReturnId());//出库单ID
                        ttPartRecordPO1.setOrderCode(partDlrReturnMainPO.getReturnCode());//出库单编码
                        ttPartRecordPO1.setOrgId(partDlrReturnMainPO.getDealerId());
                        ttPartRecordPO1.setOrgCode(partDlrReturnMainPO.getDealerCode());
                        ttPartRecordPO1.setOrgName(partDlrReturnMainPO.getDealerName());

                        if (stockOut != null && !stockOut.equals(0l)) { // 如果退货单位有仓库
                            ttPartRecordPO1.setWhId(stockOut);
                            TtPartLoactionDefinePO loactionDefinePO = new TtPartLoactionDefinePO();
                            loactionDefinePO.setWhId(stockOut);
                            loactionDefinePO.setPartId(po.getPartId());
                            loactionDefinePO.setOrgId(partDlrReturnMainPO.getDealerId());
                            loactionDefinePO.setState(Constant.STATUS_ENABLE);
                            loactionDefinePO.setStatus(Constant.IF_TYPE_YES);
                            loactionDefinePO = (TtPartLoactionDefinePO) dao.select(loactionDefinePO).get(0);

                            ttPartRecordPO1.setLocId(loactionDefinePO.getLocId());
                            ttPartRecordPO1.setLocCode(loactionDefinePO.getLocCode());
                        }
                        ttPartRecordPO1.setOptDate(new Date());
                        ttPartRecordPO1.setCreateDate(new Date());
                        ttPartRecordPO1.setPersonId(logonUser.getUserId());
                        ttPartRecordPO1.setPersonName(logonUser.getName());
                        ttPartRecordPO1.setPartState(1);

                        dao.update(sdtDtlPO, dtlPO2);
                        dao.insert(ttPartReturnRecordPO);
                        dao.insert(ttPartRecordPO);//新增出入库记录
                        dao.insert(ttPartRecordPO1);//新增出入库记录
//                            dao.update(sdtDtlPO, returnDtlPO); // 更新入库货位ID

                    }

                }

                if (flag) {

                    mainPO.setAmount(amount);

                    //退货完成时， 如果是向供应中心退货，那么退货金额要更新到服务商在供应中心的资金账户上
                    //向本部退货，产生负的占用余额（相当于增加可用余额），后财务在U9中冲销后释放负占用，最后通过资金异动接口增加账户余额。
                    TmDealerPO dealerPO = new TmDealerPO();
                    dealerPO.setDealerId(partDlrReturnMainPO.getSellerId());
                    if (dao.select(dealerPO).size() > 0) {
                        dealerPO = (TmDealerPO) dao.select(dealerPO).get(0);
                        if (dealerPO.getPdealerType().intValue() == Constant.PART_SALE_PRICE_DEALER_TYPE_01.intValue()) {
                            dao.updateAccount(partDlrReturnMainPO.getSellerId(), partDlrReturnMainPO.getDealerId(), mainPO.getAmount());
                        }
                    } else {
                        //销售退货入库完成后增加账户余额 start
                        TtPartAccountDefinePO accountDefinePO = new TtPartAccountDefinePO();
                        accountDefinePO.setChildorgId(partDlrReturnMainPO.getDealerId());
                        accountDefinePO.setParentorgId(partDlrReturnMainPO.getSellerId());
                        accountDefinePO.setAccountPurpose(Constant.PART_ACCOUNT_PURPOSE_TYPE_01);
                        accountDefinePO = (TtPartAccountDefinePO) dao.select(accountDefinePO).get(0);

                        TtPartAccountRecordPO accountRecordPO = new TtPartAccountRecordPO();
                        accountRecordPO.setRecordId(Long.valueOf(SequenceManager.getSequence("")));
                        accountRecordPO.setAccountId(accountDefinePO.getAccountId());
                        accountRecordPO.setDealerId(accountDefinePO.getChildorgId());
                        accountRecordPO.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01);
                        accountRecordPO.setAmount(-amount);
                        accountRecordPO.setFunctionName("销售退货增加可用余额");
                        accountRecordPO.setSourceId(partDlrReturnMainPO.getReturnId());
                        accountRecordPO.setSourceCode(partDlrReturnMainPO.getReturnCode());
                        accountRecordPO.setOrderId(partDlrReturnMainPO.getReturnId());
                        accountRecordPO.setOrderCode(partDlrReturnMainPO.getReturnCode());
                        accountRecordPO.setCreateDate(new Date());
                        accountRecordPO.setCreateBy(logonUser.getUserId());
                        accountRecordPO.setState(Constant.STATUS_ENABLE);
                        accountRecordPO.setStatus(1);
                        dao.insert(accountRecordPO);
                        //end
                    }

                    dao.update(sPartDlrReturnMainPO, mainPO);

                    if (stockOut != null && !stockOut.equals(0l)) {//如果退货单位有仓库
                        //调用库存释放逻辑
                        List<Object> ins = new LinkedList<Object>();
                        ins.add(0, CommonUtils.parseLong(returnId));
                        ins.add(1, Constant.PART_CODE_RELATION_28);
                        ins.add(2, 0);// 1:占用 0：释放占用
                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

                        //调用出库逻辑
                        List<Object> ins3 = new LinkedList<Object>();
                        ins3.add(0, CommonUtils.parseLong(returnId));
                        ins3.add(1, Constant.PART_CODE_RELATION_29);
                        ins3.add(2, 0);//0表示先前未占用(默认),1表示先前已占用
                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins3, null);
                    }

                    //调用入库逻辑
                    List<Object> ins4 = new LinkedList<Object>();
                    ins4.add(0, CommonUtils.parseLong(returnId));
                    ins4.add(1, Constant.PART_CODE_RELATION_30);
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins4, null);
                    
                    //调用库存占用逻辑
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, CommonUtils.parseLong(returnId));
                    ins.add(1, Constant.PART_CODE_RELATION_83);
                    ins.add(2, 1);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

                    boolean fStatus = dao.isAllIn(returnId);

                    if (fStatus) {//如果已经全部入库
                        TtPartDlrReturnMainPO rSpo = new TtPartDlrReturnMainPO();
                        rSpo.setReturnId(CommonUtils.parseLong(returnId));
                        TtPartDlrReturnMainPO rPo = new TtPartDlrReturnMainPO();
                        rPo.setReturnDate(new Date());
                        rPo.setState(Constant.PART_DLR_RETURN_STATUS_06);//更新主表状态为退货完成
                        dao.update(rSpo, rPo);
                    }
                }

            } else {
                errors = "该退货单已经入库,请选择其他退货单入库!";
            }

            if ("".equals(dtlError) && "".equals(errors)) {
                act.setOutData("success", "入库成功!");
            }
            act.setOutData("dtlError", dtlError);
            act.setOutData("errors", errors);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, dtlError + "，销售退货入库失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-27
     * @Title :
     * @Description: 销售退货入库初始化, 转到入库页面
     */
    @SuppressWarnings("unchecked")
    public void closeReturnOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

            TtPartDlrReturnMainPO dlrReturnMainPO = new TtPartDlrReturnMainPO();
            dlrReturnMainPO.setReturnId(Long.valueOf(orderId));

            TtPartDlrReturnMainPO returnMainPO = new TtPartDlrReturnMainPO();
            returnMainPO.setState(Constant.PART_DLR_RETURN_STATUS_08);
            returnMainPO.setUpdateDate(new Date());
            returnMainPO.setUpdateBy(logonUser.getUserId());

            dao.update(dlrReturnMainPO, returnMainPO);
            act.setOutData("success", "关闭成功!");

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货关闭失败，请联系管理员");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


}
