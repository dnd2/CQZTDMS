package com.infodms.dms.actions.parts.storageManager.stockNumManager;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.parts.storageManager.partStoInveManager.entityInvImpAction;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.storageManager.stockNumManager.StockAbjustmentDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartItemStockPO;
import com.infodms.dms.po.TtPartStockAbjustmentDtlPO;
import com.infodms.dms.po.TtPartStockAbjustmentMainPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * <p>ClassName: StockNumCheckAction</p>
 * <p>Description: 配件库存调整审核相关业务</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年7月25日</p>
 */
public class StockNumChkAction implements PTConstants{

    public Logger logger = Logger.getLogger(entityInvImpAction.class);
    private static final StockAbjustmentDao dao = StockAbjustmentDao.getInstance();

    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    
    /**
     * <p>Description: 库存审核申请初始化</p>
     */
    public void queryStockNumChkInit() {
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String companyName = ""; //制单单位
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp ){
                
                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                companyName = dao.getMainCompanyName(parentOrgId);
            }else {
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
            act.setForword(PART_STOCK_NUM_CHK_QUERY);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整申请初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>Description: 库存调整审核列表分页查询</p>
     */
    public void queryStockNumCheck(){
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("abjustmentCode", CommonUtils.checkNull(request.getParamValue("abjustmentCode")));
            paramMap.put("applySDate", CommonUtils.checkNull(request.getParamValue("applySDate")));
            paramMap.put("applyEDate", CommonUtils.checkNull(request.getParamValue("applyEDate")));
            paramMap.put("status", Constant.STATUS_ENABLE.toString());
            paramMap.put("applyName", CommonUtils.checkNull(request.getParamValue("applyName")));
            paramMap.put("whId", CommonUtils.checkNull(request.getParamValue("whId")));
            paramMap.put("abjustmentType", CommonUtils.checkNull(request.getParamValue("abjustmentType")));
            paramMap.put("state", CommonUtils.checkNull(request.getParamValue("state")));
            paramMap.put("applyStatus", Constant.PART_ABJUSTMENT_APPLY_02.toString());
            paramMap.put("decodeSql", "1");
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页    
            PageResult<Map<String, Object>> ps = dao.queryStockAbjustmentPageList(paramMap, Constant.PAGE_SIZE, curPage);           
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整申请列表分页查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>Description: 跳转库存调整明细</p>
     */
    public void toAbjustmentDetail(){
        try {
            String actionType = CommonUtils.checkNull(request.getParamValue("actionType"));
            String abjustmentId = CommonUtils.checkNull(request.getParamValue("abjustmentId"));
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("abjustmentId", abjustmentId);
            paramMap.put("status", Constant.STATUS_ENABLE.toString());
            // 调整主要信息
            Map<String, Object> infoMainMap = dao.getAbjustmentMainList(paramMap).get(0);
            // 调整明细
            List<Map<String, Object>> dtlList = dao.getAbjustmentDetailList(paramMap);
            act.setOutData("mainMap", infoMainMap);
            act.setOutData("dtlList", dtlList);
            if("view".equals(actionType)){
                act.setForword(PART_STOCK_NUM_CHK_VIEW);
            }else if("check".equals(actionType)){
                act.setForword(PART_STOCK_NUM_CHK);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整详情");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>
     * Description: 库存调整审核
     * </p>
     */
    @SuppressWarnings("unchecked")
    public void partsStockNumCheck(){
        try {
            String abjustmentId = CommonUtils.checkNull(request.getParamValue("abjustmentId")); // 调整id
            String abjustmentType = CommonUtils.checkNull(request.getParamValue("abjustmentType")); // 调整类型
            String dealStatusStr = CommonUtils.checkNull(request.getParamValue("dealStatus")); // 处理状态
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 审核意见
            if(CommonUtils.isEmpty(abjustmentId) || CommonUtils.isEmpty(dealStatusStr)|| CommonUtils.isEmpty(abjustmentType)){
                act.setOutData("returnCode", -1);
                act.setOutData("msg", "参数异常，审核失败！");
                return;
            }
            Map<String, String> paramMap = new HashMap<String, String>();
            if(abjustmentType.equals(Constant.PART_ABJUSTMENT_TYPE_01.toString())){
                paramMap.put("operator", "+");
            }else if(abjustmentType.equals(Constant.PART_ABJUSTMENT_TYPE_02.toString())){
                paramMap.put("operator", "-");
            }else {
                act.setOutData("returnCode", -1);
                act.setOutData("msg", "参数异常，审核失败！");
                return;
            }
            paramMap.put("abjustmentId", abjustmentId);
            paramMap.put("userId", logonUser.getUserId().toString());

            // 验证减少库存时，可用库存是否能够调整
            if(Integer.parseInt(abjustmentType) == Constant.PART_ABJUSTMENT_TYPE_02){
                List<Map<String, Object>> bookStockList = dao.getAbjPartsStockList(paramMap);
                for(Map<String, Object> m:bookStockList){
                    long normalQty = Long.parseLong(CommonUtils.checkNull(m.get("NORMAL_QTY"))); // 可用库存
                    long bookedQty = Long.parseLong(CommonUtils.checkNull(m.get("BOOKED_QTY"))); // 占用库存
                    long abjustmentNum = Long.parseLong(CommonUtils.checkNull(m.get("ABJUSTMENT_NUM"))); // 调整数量
                    if (normalQty + bookedQty < abjustmentNum) {
                        String partOldcode = CommonUtils.checkNull(m.get("PART_OLDCODE"));
                        act.setOutData("returnCode", -1);
                        act.setOutData("msg", "配件【"+partOldcode+"】可用库存不足，不能减少库存！");
                        return;
                    }
                }
            }
            
            int dealStatus = 0;
            if("1".equals(dealStatusStr)){
                dealStatus = Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS_01;
            }else if("2".equals(dealStatus)){
                dealStatus = Constant.PART_ABJUSTMENT_CHECK_DEAL_STATUS_02;
            }
            
            // 更新库存调整主表记录
            TtPartStockAbjustmentMainPO selMainPO = new TtPartStockAbjustmentMainPO();
            selMainPO.setAbjustmentId(Long.parseLong(abjustmentId));
            TtPartStockAbjustmentMainPO upMainPO = new TtPartStockAbjustmentMainPO();
            upMainPO.setCheckState(Constant.PART_ABJUSTMENT_CHECK_02); // 审核状态
            upMainPO.setState(Constant.PART_ABJUSTMENT_STATE_03); // 调整状态
            upMainPO.setCheckRemark(remark); // 审核意见
            upMainPO.setUpdateBy(logonUser.getUserId());
            upMainPO.setUpdateDate(new Date());
            dao.update(selMainPO, upMainPO);
            
            // 更新库存调整明细记录
            TtPartStockAbjustmentDtlPO setDtlPO = new TtPartStockAbjustmentDtlPO();
            setDtlPO.setAbjustmentId(selMainPO.getAbjustmentId());
            TtPartStockAbjustmentDtlPO upDtlPO = new TtPartStockAbjustmentDtlPO();
            upDtlPO.setDealStatus(dealStatus);
            upDtlPO.setUpdateBy(logonUser.getUserId());
            upDtlPO.setUpdateDate(new Date());
            dao.update(setDtlPO, upDtlPO);
            
            // 更新库存数量
            paramMap.put("isLocked", Constant.PART_STATE_UN_LOCKED.toString());
            dao.updateItemQty(paramMap);
            
            // 更新库存可用数量
            dao.updateNormalQty(paramMap);
            
            // 减少库存时释放占用
            if(Integer.parseInt(abjustmentType) == Constant.PART_ABJUSTMENT_TYPE_02){
                List<Object> ins2 = new LinkedList<Object>();
                ins2.add(0, abjustmentId);
                ins2.add(1, Constant.PART_CODE_RELATION_62);
                ins2.add(2, 0);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
            }

            act.setOutData("returnCode", 1);
            act.setOutData("msg", "审核成功！");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整审核");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
}
