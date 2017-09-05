package com.infodms.dms.actions.parts.storageManager.partReturnManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartDlrReturnChkrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDlrReturnDtlPO;
import com.infodms.dms.po.TtPartDlrReturnMainPO;
import com.infodms.dms.po.TtPartOutstockMainPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author : chenjunjiang CreateDate : 2013-4-25
 * @ClassName : PartDlrReturnChkManager
 * @Description : 销售退货申请审核
 */
@SuppressWarnings("unchecked")
public class PartDlrReturnChkManager implements PTConstants {

    public Logger logger = Logger.getLogger(PartDlrReturnChkManager.class);
    private PartDlrReturnChkrDao dao = PartDlrReturnChkrDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    private RequestWrapper request = act.getRequest();

    /**
     * <p>
     * Description: 配件销售退货审核初始化公共调用方法
     * </p>
     * 
     * @param act
     * @param logonUser 当前登录用户信息
     * @param chkLevel 审核等级
     * @throws Exception
     */
    private void partReturnChkInit(ActionContext act, AclUserBean logonUser, int chkLevel) throws Exception {
        PartDlrOrderDao dao2 = PartDlrOrderDao.getInstance();
        List<Map<String, Object>> salerList = dao2.getSaler();
        boolean salerFlag = false;
        String dealerId = "";
        PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
        if (null != beanList && beanList.size() >= 0) {
            dealerId = beanList.get(0).getOrgId() + "";
        }
        if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
            salerFlag = true;

        }
        act.setOutData("chkLevel", chkLevel);
        act.setOutData("old", CommonUtils.getBefore(new Date()));
        act.setOutData("now", CommonUtils.getDate());
        act.setOutData("curUserId", logonUser.getUserId());
        act.setOutData("salerList", salerList);
        act.setOutData("salerFlag", salerFlag);
        act.setForword(PART_RETURN_APPLY_QUERY_URL);
    }

    /**
     * <p>
     * Description: 配件销售退货一级审核初始化页面
     * </p>
     */
    public void queryPartReturnChkOneInit() {
        try {
            this.partReturnChkInit(act, logonUser, 1);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货一级审核初始化页面");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>
     * Description: 配件销售退货二级审核初始化页面
     * </p>
     */
    public void queryPartReturnChkTwoInit() {
        try {
            this.partReturnChkInit(act, logonUser, 2);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货二级审核初始化页面");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>
     * Description: 配件销售退货三级审核初始化页面
     * </p>
     */
    public void queryPartReturnChkThreeInit() {
        try {
            this.partReturnChkInit(act, logonUser, 3);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货三级审核初始化页面");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    //    /**
    //     * @param :
    //     * @return :
    //     * @throws : LastDate    : 2013-4-25
    //     * @Title :
    //     * @Description: 销售退货申请查询初始化，转到查询页面
    //     */
    //    public void queryPartReturnApplyInit() {
    //        try {
    //            PartDlrOrderDao dao2 = PartDlrOrderDao.getInstance();
    //            List<Map<String, Object>> salerList = dao2.getSaler();
    //            boolean salerFlag = false;
    //            String dealerId = "";
    //            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
    //            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
    //            if (null != beanList && beanList.size() >= 0) {
    //                dealerId = beanList.get(0).getOrgId() + "";
    //            }
    //            if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
    //                salerFlag = true;
    //
    //            }
    //            act.setOutData("old", CommonUtils.getBefore(new Date()));
    //            act.setOutData("now", CommonUtils.getDate());
    //            act.setOutData("curUserId", logonUser.getUserId());
    //            act.setOutData("salerList", salerList);
    //            act.setOutData("salerFlag", salerFlag);
    //            act.setForword(PART_RETURN_APPLY_QUERY_URL);
    //        } catch (Exception e) {//异常方法
    //            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货申请");
    //            logger.error(logonUser, e1);
    //            act.setException(e1);
    //        }
    //    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-25
     * @Title :
     * @Description: 分页查询销售退货申请
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
            String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));//销售员ID
            String chkLevel = CommonUtils.checkNull(request.getParamValue("chkLevel")); // 审核等级
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("returnCode", returnCode);
            paramMap.put("dealerName", dealerName);
            paramMap.put("startDate", startDate);
            paramMap.put("endDate", endDate);
            paramMap.put("salerId", salerId);
            paramMap.put("chkLevel", chkLevel);
            paramMap.put("state", Constant.PART_DLR_RETURN_STATUS_02.toString());
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null
                    ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDlrReturnApplyList(paramMap, logonUser, curPage,
                    Constant.PAGE_SIZE);
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
     * @throws : LastDate : 2013-4-25
     * @Title :
     * @Description: 进入审核页面, 查看退货申请明细
     */
    public void queryApplyDetailInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String chkLevel = CommonUtils.checkNull(request.getParamValue("chkLevel"));
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("returnId", returnId);
            paramMap.put("chkLevel", chkLevel);
            Map<String, Object> map = dao.getPartDlrReturnMainInfo(paramMap, logonUser).get(0);
            request.setAttribute("po", map);
            act.setOutData("chkLevel", chkLevel);
            act.setForword(PART_RETURN_APPLY_CHECK_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货申请明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-25
     * @Title :
     * @Description: 查询配件销售退货申请明细
     */
    public void queryPartDlrReturnApplyDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null
                    ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDlrReturnApplyDetailList(returnId, soCode, curPage,
                    Constant.PAGE_SIZE);
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
     * @throws : LastDate : 2013-4-26
     * @Title :
     * @Description: 审核通过
     */
    public void agreePartDlrReturnApply() {
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String[] dtlIds = request.getParamValues("dtlId");//明细id
            if (dtlIds.length == 0) {//如果没有明细就提示错误信息
                act.setOutData("error", "该退货单没有明细,不能通过!");
                return;
            }

            TtPartDlrReturnMainPO mainPO = new TtPartDlrReturnMainPO();
            mainPO.setReturnId(CommonUtils.parseLong(returnId));
            mainPO = (TtPartDlrReturnMainPO) dao.select(mainPO).get(0);
            Long stockOut = mainPO.getStockOut();

            //只有状态为审核中的才可以审核
            if (mainPO.getState().intValue() == Constant.PART_DLR_RETURN_STATUS_02) {
                // 审核意见
                String rejectReason = CommonUtils.checkNull(request.getParamValue("rejectReason"));
                for (int i = 0; i < dtlIds.length; i++) {
                    Long dtlId = CommonUtils.parseLong(dtlIds[i]);

                    long checkQty = CommonUtils.parseLong(request.getParamValue("CHECK_QTY" + dtlIds[i]));//审核数量

                    TtPartDlrReturnDtlPO po1 = new TtPartDlrReturnDtlPO();
                    TtPartDlrReturnDtlPO po2 = new TtPartDlrReturnDtlPO();
                    po1.setDtlId(dtlId);

                    po2.setCheckQty(checkQty);//审核退货数量

                    dao.update(po1, po2);
                }

                TtPartDlrReturnMainPO spo = new TtPartDlrReturnMainPO();
                spo.setReturnId(CommonUtils.parseLong(returnId));

                TtPartDlrReturnMainPO po = new TtPartDlrReturnMainPO();

                if (mainPO.getVerifyLevel().intValue() == Constant.PART_RETURN_CHK_LEVEL_01) {
                    // 一级审核
                    po.setVlOneBy(logonUser.getUserId()); // 审核人
                    po.setVlOneStatus(Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS_01); // 审核状态
                    po.setVlOneRemark(rejectReason); // 审核意见
                    po.setVlOneDate(new Date()); // 审核时间
                    po.setVerifyLevel(Constant.PART_RETURN_CHK_LEVEL_02); // 审核等级
                } else if (mainPO.getVerifyLevel().intValue() == Constant.PART_RETURN_CHK_LEVEL_02) {
                    // 二级审核
                    po.setVlTwoBy(logonUser.getUserId());// 审核人
                    po.setVlTwoStatus(Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS_01); // 审核状态
                    po.setVlTwoRemark(rejectReason);// 审核意见
                    po.setVlTwoDate(new Date());// 审核时间
                    po.setVerifyLevel(Constant.PART_RETURN_CHK_LEVEL_03); // 审核等级
                } else if (mainPO.getVerifyLevel().intValue() == Constant.PART_RETURN_CHK_LEVEL_03) {
                    // 三级审核
                    po.setVerifyBy(logonUser.getUserId());// 审核人
                    po.setVerifyStatus(Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS_01); // 审核状态
                    po.setRemark1(rejectReason);// 审核意见
                    po.setVerifyDate(new Date());// 审核时间
                    po.setState(Constant.PART_DLR_RETURN_STATUS_04);//状态更新为回运中
                }
                
                dao.update(spo, po);
                
                if (mainPO.getVerifyLevel().intValue() == Constant.PART_RETURN_CHK_LEVEL_03 && stockOut != null && !stockOut.equals(0l)) {
                    // 三级审核通过
                    //库存释放和占用逻辑，此审核页面审核数量可以修改，需要把先前已经占用的资源释放掉，然后根据审核通过的结果再重新占用资源
                    //调用库存释放逻辑
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, CommonUtils.parseLong(returnId));
                    ins.add(1, Constant.PART_CODE_RELATION_17);
                    ins.add(2, 0);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

                    //调用库存占用逻辑
                    List<Object> ins2 = new LinkedList<Object>();
                    ins2.add(0, CommonUtils.parseLong(returnId));
                    ins2.add(1, Constant.PART_CODE_RELATION_28);
                    ins2.add(2, 1);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
                }

            } else {
                act.setOutData("error", "该退货单已经被审核,请选择其他退货单审核!");
                return;
            }

            act.setOutData("success", "审核通过!");
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售退货申请审核失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    //    public void agreePartDlrReturnApply() {
    //        try {
    //            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
    //            String[] dtlIds = request.getParamValues("dtlId");//明细id
    //
    //            if (dtlIds.length == 0) {//如果没有明细就提示错误信息
    //                act.setOutData("error", "该退货单没有明细,不能通过!");
    //                return;
    //            }
    //
    //            TtPartDlrReturnMainPO partDlrReturnMainPO = new TtPartDlrReturnMainPO();
    //            partDlrReturnMainPO.setReturnId(CommonUtils.parseLong(returnId));
    //            partDlrReturnMainPO = (TtPartDlrReturnMainPO) dao.select(partDlrReturnMainPO).get(0);
    //
    //            Long stockOut = partDlrReturnMainPO.getStockOut();
    //
    //            if (partDlrReturnMainPO.getState().intValue() == Constant.PART_DLR_RETURN_STATUS_02) {//只有状态为审核中的才可以审核
    //                TmBusinessParaPO tmBusinessParaPO = new TmBusinessParaPO();
    //                tmBusinessParaPO.setParaId(60031001);
    //                tmBusinessParaPO = (TmBusinessParaPO) dao.select(tmBusinessParaPO).get(0);
    //
    //                //判断销售退货申请是否自动出入库
    //                if ("1".equals(tmBusinessParaPO.getParaValue())) {//自动出入库
    //                    double amount = 0;//退货金额
    //                    TtPartDlrReturnMainPO mpo = new TtPartDlrReturnMainPO();
    //
    //                    for (int i = 0; i < dtlIds.length; i++) {
    //                        Long dtlId = CommonUtils.parseLong(dtlIds[i]);
    //                        TtPartDlrReturnDtlPO po = new TtPartDlrReturnDtlPO();
    //                        po.setDtlId(dtlId);
    //                        po = (TtPartDlrReturnDtlPO) dao.select(po).get(0);
    //
    //                        long checkQty = CommonUtils.parseLong(request.getParamValue("CHECK_QTY" + dtlIds[i]));//审核数量
    //
    //                        TtPartDlrReturnDtlPO po1 = new TtPartDlrReturnDtlPO();
    //                        TtPartDlrReturnDtlPO po2 = new TtPartDlrReturnDtlPO();
    //                        po1.setDtlId(dtlId);
    //
    //                        po2.setCheckQty(checkQty);//审核退货数量
    //                        po2.setOutQty(checkQty);//退货出库数量
    //                        po2.setInQty(checkQty);//退货入库数量
    //                        po2.setReturnQty(checkQty);//已退货数量
    //                        po2.setBuyAmount(Arith.mul(checkQty, po.getBuyPrice()));//退货行金额
    //                        amount += po.getBuyAmount();
    //                        dao.update(po1, po2);
    //
    //                        //插入信息到出入库记录表 (出库)
    //                        TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
    //                        ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
    //                        ttPartRecordPO.setAddFlag(2);//出库标记
    //                        ttPartRecordPO.setState(1);//正常出库
    //                        ttPartRecordPO.setPartNum(po.getCheckQty());//出库数量
    //                        ttPartRecordPO.setTranstypeId(0l);//默认0
    //                        ttPartRecordPO.setPartId(po.getPartId());//配件ID
    //                        ttPartRecordPO.setPartCode(po.getPartCode());//配件件号
    //                        ttPartRecordPO.setPartOldcode(po.getPartOldcode());//配件编码
    //                        ttPartRecordPO.setPartName(po.getPartCname());//配件名称
    //                        ttPartRecordPO.setPartBatch("1306");//////////////////配件批次
    //                        ttPartRecordPO.setVenderId(21799l);///////////////////配件供应商
    //                        ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_29));//出库单
    //                        ttPartRecordPO.setOrderId(CommonUtils.parseLong(returnId));//出库单ID
    //                        ttPartRecordPO.setOrderCode(partDlrReturnMainPO.getReturnCode());//出库单编码
    //                        List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
    //                        ttPartRecordPO.setOrgId(partDlrReturnMainPO.getDealerId());
    //                        ttPartRecordPO.setOrgCode(partDlrReturnMainPO.getDealerCode());
    //                        ttPartRecordPO.setOrgName(partDlrReturnMainPO.getDealerName());
    //
    //                        if (stockOut != null && !stockOut.equals(0l)) {//如果在退货的时候选择了仓库才设置货位信息
    //                            ttPartRecordPO.setWhId(stockOut);
    //
    //                            TtPartLoactionDefinePO loactionDefinePO = new TtPartLoactionDefinePO();
    //                            loactionDefinePO.setWhId(partDlrReturnMainPO.getStockOut());
    //                            loactionDefinePO.setPartId(po.getPartId());
    //                            loactionDefinePO.setOrgId(partDlrReturnMainPO.getDealerId());
    //                            loactionDefinePO.setState(Constant.STATUS_ENABLE);
    //                            loactionDefinePO.setStatus(1);
    //                            loactionDefinePO = (TtPartLoactionDefinePO) dao.select(loactionDefinePO).get(0);
    //
    //                            ttPartRecordPO.setLocId(loactionDefinePO.getLocId());
    //                            ttPartRecordPO.setLocCode(loactionDefinePO.getLocCode());
    //                        }
    //                        ttPartRecordPO.setOptDate(new Date());
    //                        ttPartRecordPO.setCreateDate(new Date());
    //                        ttPartRecordPO.setPersonId(logonUser.getUserId());
    //                        ttPartRecordPO.setPersonName(logonUser.getName());
    //                        ttPartRecordPO.setPartState(1);
    //
    //                        //插入信息到出入库记录表 (入库)
    //                        TtPartRecordPO ttPartRecordPO1 = new TtPartRecordPO();
    //                        ttPartRecordPO1.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
    //                        ttPartRecordPO1.setAddFlag(1);//入库标记
    //                        ttPartRecordPO1.setState(1);//正常入库
    //                        ttPartRecordPO1.setPartNum(po.getOutQty());//入库数量
    //                        ttPartRecordPO1.setTranstypeId(0l);//默认0
    //                        ttPartRecordPO1.setPartId(po.getPartId());//配件ID
    //                        ttPartRecordPO1.setPartCode(po.getPartCode());//配件件号
    //                        ttPartRecordPO1.setPartOldcode(po.getPartOldcode());//配件编码
    //                        ttPartRecordPO1.setPartName(po.getPartCname());//配件名称
    //                        ttPartRecordPO1.setPartBatch("1306");//////////////////配件批次
    //                        ttPartRecordPO1.setVenderId(21799l);///////////////////配件供应商
    //                        ttPartRecordPO1.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_30));//入库单
    //                        ttPartRecordPO1.setOrderId(po.getReturnId());//退货单ID
    //                        ttPartRecordPO1.setOrderCode(partDlrReturnMainPO.getReturnCode());//退货单编码
    //                        //ttPartRecordPO.setLineId();
    //                        ttPartRecordPO1.setOrgId(orgBeanList.get(0).getOrgId());
    //                        ttPartRecordPO1.setOrgCode(orgBeanList.get(0).getOrgCode());
    //                        ttPartRecordPO1.setOrgName(orgBeanList.get(0).getOrgName());
    //
    //                        String soCOde = partDlrReturnMainPO.getSoCode();
    //                        TtPartWarehouseDefinePO warehouseDefinePO = new TtPartWarehouseDefinePO();
    //                        //自动出入库的时候,如果有销售单号,那么此时入库的仓库就是出库单中的仓库
    //                        if (soCOde != null && !"".equals(soCOde)) {
    //                            TtPartOutstockMainPO outstockMainPO = new TtPartOutstockMainPO();
    //                            outstockMainPO.setSoCode(soCOde);
    //                            outstockMainPO = (TtPartOutstockMainPO) dao.select(outstockMainPO).get(0);
    //                            ttPartRecordPO1.setWhId(outstockMainPO.getWhId());
    //                            mpo.setStockIn(outstockMainPO.getWhId());//设置入库仓库
    //                        } else {//如果没有销售单号,就随机选择当前机构下的一个仓库作为入库仓库
    //                            Long orgId = orgBeanList.get(0).getOrgId();
    //                            warehouseDefinePO.setOrgId(orgId);
    //                            warehouseDefinePO.setState(Constant.STATUS_ENABLE);
    //                            warehouseDefinePO.setStatus(1);
    //                            List<TtPartWarehouseDefinePO> wlist = dao.select(warehouseDefinePO);
    //                            if (wlist.size() == 0) {//如果当前机构下已经没有了可用的仓库,就提示错误
    //                                act.setOutData("error", "你当前的机构没有可用的仓库,不能自动入库!");
    //                                return;
    //                            }
    //
    //                            warehouseDefinePO = (TtPartWarehouseDefinePO) wlist.get(0);
    //
    //                            TtPartLoactionDefinePO loactionDefinePO = new TtPartLoactionDefinePO();
    //                            loactionDefinePO.setWhId(warehouseDefinePO.getWhId());
    //                            loactionDefinePO.setPartId(po.getPartId());
    //                            loactionDefinePO.setOrgId(orgBeanList.get(0).getOrgId());
    //                            loactionDefinePO.setState(Constant.STATUS_ENABLE);
    //                            loactionDefinePO.setStatus(1);
    //                            List<TtPartLoactionDefinePO> llist = dao.select(loactionDefinePO);
    //
    //                            if (llist.size() == 0) {//如果没有在该仓库中没有对应的货位信息,就新建一个货位
    //                                loactionDefinePO.setLocId(CommonUtils.parseLong(SequenceManager.getSequence("")));
    //                                loactionDefinePO.setLocCode("暂无");
    //                                loactionDefinePO.setLocName("暂无");
    //                                loactionDefinePO.setOrgId(orgBeanList.get(0).getOrgId());
    //                                loactionDefinePO.setCreateDate(new Date());
    //                                loactionDefinePO.setCreateBy(-1l);
    //                                dao.insert(loactionDefinePO);
    //                            }
    //
    //                        }
    //
    //                        PurchaseOrderInDao indao = PurchaseOrderInDao.getInstance();
    //                        Map<String, Object> map = indao.queryPartAndLocationInfo(po.getPartId(),
    //                                orgBeanList.get(0).getOrgId(), warehouseDefinePO.getWhId());//查询当前配件信息及其货位信息
    //
    //                        ttPartRecordPO1.setWhId(warehouseDefinePO.getWhId());
    //                        mpo.setStockIn(warehouseDefinePO.getWhId());//设置入库仓库
    //                        ttPartRecordPO1.setLocId(((BigDecimal) map.get("LOC_ID")).longValue());
    //                        ttPartRecordPO1.setLocCode((String) map.get("LOC_CODE"));
    //                        ttPartRecordPO1.setOptDate(new Date());
    //                        ttPartRecordPO1.setCreateDate(new Date());
    //                        ttPartRecordPO1.setPersonId(logonUser.getUserId());
    //                        ttPartRecordPO1.setPersonName(logonUser.getName());
    //                        ttPartRecordPO1.setPartState(1);
    //
    //                        //销售退货完成之后需要向退货记录表中插入退货信息
    //                        TtPartReturnRecordPO ttPartReturnRecordPO = new TtPartReturnRecordPO();
    //                        ttPartReturnRecordPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
    //                        ttPartReturnRecordPO.setReturnId(po.getReturnId());
    //                        ttPartReturnRecordPO.setReturnCode(partDlrReturnMainPO.getReturnCode());
    //                        ttPartReturnRecordPO.setReturnType(Constant.PART_RETURN_TYPE_01);//退货类型
    //                        ttPartReturnRecordPO.setSourceId(partDlrReturnMainPO.getSoId());
    //                        ttPartReturnRecordPO.setSourceCaode(partDlrReturnMainPO.getSoCode());
    //                        ttPartReturnRecordPO.setPartId(po.getPartId());
    //                        ttPartReturnRecordPO.setPartCode(po.getPartCode());
    //                        ttPartReturnRecordPO.setPartOldcode(po.getPartOldcode());
    //                        ttPartReturnRecordPO.setPartCname(po.getPartCname());
    //                        ttPartReturnRecordPO.setUnit(po.getUnit());
    //                        ttPartReturnRecordPO.setReturnQty(po.getReturnQty());
    //                        ttPartReturnRecordPO.setRemark(po.getRemark());
    //                        ttPartReturnRecordPO.setCreateDate(new Date());
    //                        ttPartReturnRecordPO.setCreateBy(logonUser.getUserId());
    //                        ttPartReturnRecordPO.setStockOut(stockOut);
    //                        ttPartReturnRecordPO.setStockIn(mpo.getStockIn());
    //
    //                        dao.insert(ttPartRecordPO);//新增出入库记录
    //                        dao.insert(ttPartRecordPO1);//新增出入库记录
    //                        dao.insert(ttPartReturnRecordPO);
    //                    }
    //
    //                    TtPartDlrReturnMainPO spo = new TtPartDlrReturnMainPO();
    //                    spo.setReturnId(CommonUtils.parseLong(returnId));
    //
    //                    mpo.setVerifyDate(new Date());//审核日期
    //                    mpo.setVerifyBy(logonUser.getUserId());//审核人
    //                    mpo.setReturnDate(new Date());//退货日期
    //                    mpo.setAmount(amount);//退货金额
    //                    mpo.setState(Constant.PART_DLR_RETURN_STATUS_06);//主表状态更新为退货完成
    //                    dao.update(spo, mpo);
    //
    //                    if (stockOut != null && !stockOut.equals(0l)) {//选了出库仓库才会调用出库逻辑
    //
    //                        //库存释放和占用逻辑，此审核页面审核数量可以修改，需要把先前已经占用的资源释放掉，然后根据审核通过的结果再重新占用资源
    //                        //调用库存释放逻辑
    //                        List<Object> ins = new LinkedList<Object>();
    //                        ins.add(0, CommonUtils.parseLong(returnId));
    //                        ins.add(1, Constant.PART_CODE_RELATION_17);
    //                        ins.add(2, 0);// 1:占用 0：释放占用
    //                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
    //
    //                        //调用库存占用逻辑
    //                        List<Object> ins2 = new LinkedList<Object>();
    //                        ins2.add(0, CommonUtils.parseLong(returnId));
    //                        ins2.add(1, Constant.PART_CODE_RELATION_28);
    //                        ins2.add(2, 1);// 1:占用 0：释放占用
    //                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
    //
    //                        //调用出库逻辑
    //                        List<Object> ins3 = new LinkedList<Object>();
    //                        ins3.add(0, CommonUtils.parseLong(returnId));
    //                        ins3.add(1, Constant.PART_CODE_RELATION_29);
    //                        ins3.add(2, 1);//0表示先前未占用(默认),1表示先前已占用
    //                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins3, null);
    //                    }
    //
    //                    //调用入库逻辑
    //                    List<Object> ins4 = new LinkedList<Object>();
    //                    ins4.add(0, CommonUtils.parseLong(returnId));
    //                    ins4.add(1, Constant.PART_CODE_RELATION_30);
    //                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins4, null);
    //                }
    //
    //                if ("0".equals(tmBusinessParaPO.getParaValue())) {//手动出入库
    //                    for (int i = 0; i < dtlIds.length; i++) {
    //                        Long dtlId = CommonUtils.parseLong(dtlIds[i]);
    //
    //                        long checkQty = CommonUtils.parseLong(request.getParamValue("CHECK_QTY" + dtlIds[i]));//审核数量
    //
    //                        TtPartDlrReturnDtlPO po1 = new TtPartDlrReturnDtlPO();
    //                        TtPartDlrReturnDtlPO po2 = new TtPartDlrReturnDtlPO();
    //                        po1.setDtlId(dtlId);
    //
    //                        po2.setCheckQty(checkQty);//审核退货数量
    //
    //                        dao.update(po1, po2);
    //                    }
    //
    //                    TtPartDlrReturnMainPO spo = new TtPartDlrReturnMainPO();
    //                    spo.setReturnId(CommonUtils.parseLong(returnId));
    //
    //                    TtPartDlrReturnMainPO po = new TtPartDlrReturnMainPO();
    //                    po.setVerifyDate(new Date());//审核日期
    //                    po.setVerifyBy(logonUser.getUserId());//审核人
    //                    po.setState(Constant.PART_DLR_RETURN_STATUS_04);//状态更新为回运中
    //
    //                    dao.update(spo, po);
    //
    //                    if (stockOut != null && !stockOut.equals(0l)) {
    //                        //库存释放和占用逻辑，此审核页面审核数量可以修改，需要把先前已经占用的资源释放掉，然后根据审核通过的结果再重新占用资源
    //                        //调用库存释放逻辑
    //                        List<Object> ins = new LinkedList<Object>();
    //                        ins.add(0, CommonUtils.parseLong(returnId));
    //                        ins.add(1, Constant.PART_CODE_RELATION_17);
    //                        ins.add(2, 0);// 1:占用 0：释放占用
    //                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
    //
    //                        //调用库存占用逻辑
    //                        List<Object> ins2 = new LinkedList<Object>();
    //                        ins2.add(0, CommonUtils.parseLong(returnId));
    //                        ins2.add(1, Constant.PART_CODE_RELATION_28);
    //                        ins2.add(2, 1);// 1:占用 0：释放占用
    //                        dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
    //                    }
    //
    //                }
    //            } else {
    //                act.setOutData("error", "该退货单已经被审核,请选择其他退货单审核!");
    //                return;
    //            }
    //
    //            act.setOutData("success", "审核通过!");
    //        } catch (Exception e) {// 异常方法
    //            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售退货申请审核失败,请联系管理员!");
    //            logger.error(logonUser, e1);
    //            act.setException(e1);
    //        }
    //    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-26
     * @Title :
     * @Description: 驳回销售退货申请
     */
    public void rejectPartDlrReturnApply() {
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String rejectReason = CommonUtils.checkNull(request.getParamValue("rejectReason"));

            TtPartDlrReturnMainPO mainPo = new TtPartDlrReturnMainPO();
            mainPo.setReturnId(CommonUtils.parseLong(returnId));
            mainPo = (TtPartDlrReturnMainPO) dao.select(mainPo).get(0);
            Long stockOut = mainPo.getStockOut();
            

            //只有状态为审核中的才可以驳回
            if (mainPo.getState().intValue() == Constant.PART_DLR_RETURN_STATUS_02) {
                TtPartDlrReturnMainPO spo = new TtPartDlrReturnMainPO();
                TtPartDlrReturnMainPO po = new TtPartDlrReturnMainPO();
                spo.setReturnId(CommonUtils.parseLong(returnId));
                po.setState(Constant.PART_DLR_RETURN_STATUS_03);//更新状态为驳回

                if (mainPo.getVerifyLevel().intValue() == Constant.PART_RETURN_CHK_LEVEL_01) {
                    // 一级审核
                    po.setVlOneBy(logonUser.getUserId()); // 审核人
                    po.setVlOneStatus(Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS_02); // 审核状态
                    po.setVlOneRemark(rejectReason); // 审核意见
                    po.setVlOneDate(new Date()); // 审核时间
//                    po.setVerifyLevel(Constant.PART_RETURN_CHK_LEVEL_02); // 审核等级
                } else if (mainPo.getVerifyLevel().intValue() == Constant.PART_RETURN_CHK_LEVEL_02) {
                    // 二级审核
                    po.setVlTwoBy(logonUser.getUserId());// 审核人
                    po.setVlTwoStatus(Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS_02);// 审核状态
                    po.setVlTwoRemark(rejectReason);// 审核意见
                    po.setVlTwoDate(new Date());// 审核时间
//                    po.setVerifyLevel(Constant.PART_RETURN_CHK_LEVEL_03); // 审核等级
                } else if (mainPo.getVerifyLevel().intValue() == Constant.PART_RETURN_CHK_LEVEL_03) {
//                    // 冻结出库单开票
//                    if(mainPo.getSoId() != null){
//                        TtPartOutstockMainPO outstockPO = new TtPartOutstockMainPO();
//                        outstockPO.setSoId(mainPo.getSoId());
//                        outstockPO.setIsInv(Constant.IF_TYPE_NO);
//                    }
                    // 三级审核
                    po.setVerifyBy(logonUser.getUserId());// 审核人
                    po.setVerifyStatus(Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS_02);// 审核状态
                    po.setRemark1(rejectReason);// 审核意见
                    po.setVerifyDate(new Date());// 审核时间
                }

                dao.update(spo, po);

                if (stockOut != null && !stockOut.equals(0l)) {//如果退货单位有仓库才会调用释放逻辑
                    //调用库存释放逻辑
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, CommonUtils.parseLong(returnId));
                    ins.add(1, Constant.PART_CODE_RELATION_17);
                    ins.add(2, 0);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                }

                act.setOutData("success", "驳回成功!");
            } else {
                act.setOutData("error", "该退货单已经驳回,请选择其他退货单审核!");
                return;
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "驳回失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-25
     * @Title :
     * @Description: 销售退货申请查询初始化，转到查询页面
     */
    public void queryPartReturnBackInit() {
        try {
            //            PartDlrOrderDao dao2 = PartDlrOrderDao.getInstance();
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_RETURN_BACK_CHECK_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-25
     * @Title :
     * @Description: 进入审核页面, 查看退货申请明细
     */
    public void returnBackDetailInit() {
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String chkLevel = CommonUtils.checkNull(request.getParamValue("chkLevel"));
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("returnId", returnId);
            paramMap.put("chkLevel", chkLevel);
            Map<String, Object> map = dao.getPartDlrReturnMainInfo(paramMap, logonUser).get(0);

            request.setAttribute("po", map);
            act.setOutData("chkLevel", chkLevel);
            act.setForword(PART_RETURN_BACK_CHECK_URL2);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货申请明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-26
     * @Title :
     * @Description: 销售退货回运
     */
    public void PartDlrReturnBack() {
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String wl = CommonUtils.checkNull(request.getParamValue("wl"));
            String wlNo = CommonUtils.checkNull(request.getParamValue("wlNo"));
            String wlDate = CommonUtils.checkNull(request.getParamValue("wlDate"));
            String wlRemark = CommonUtils.checkNull(request.getParamValue("wlRemark"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            TtPartDlrReturnMainPO spo = new TtPartDlrReturnMainPO();
            TtPartDlrReturnMainPO po = new TtPartDlrReturnMainPO();
            spo.setReturnId(CommonUtils.parseLong(returnId));
            po.setState(Constant.PART_DLR_RETURN_STATUS_05);//更新状态为已回运
            po.setWl(wl);
            po.setWlno(wlNo);
            po.setWlDate(dateFormat.parse(wlDate));
            po.setWlRemark(wlRemark);
            po.setWlBy(logonUser.getUserId());//回运人

            dao.update(spo, po);

            act.setOutData("success", "回运成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "回运失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
