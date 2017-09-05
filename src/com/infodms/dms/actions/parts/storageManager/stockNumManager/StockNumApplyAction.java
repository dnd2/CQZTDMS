package com.infodms.dms.actions.parts.storageManager.stockNumManager;

import java.util.ArrayList;
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
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * <p>ClassName: StockNumApplyAction</p>
 * <p>Description: 库存调整申请相关业务类</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年7月24日</p>
 */
public class StockNumApplyAction  implements PTConstants{

    public Logger logger = Logger.getLogger(entityInvImpAction.class);
    private static final StockAbjustmentDao dao = StockAbjustmentDao.getInstance();

    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    
    
    /**
     * <p>Description: 库存调整申请初始化</p>
     */
    public void queryStockNumApplyInit() {
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
            act.setForword(PART_STOCK_NUM_APPLY);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整申请初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>Description: 库存调整申请列表分页查询</p>
     */
    public void queryStockNumApply(){
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("abjustmentCode", CommonUtils.checkNull(request.getParamValue("abjustmentCode")));
            paramMap.put("createSDate", CommonUtils.checkNull(request.getParamValue("createSDate")));
            paramMap.put("createEDate", CommonUtils.checkNull(request.getParamValue("createEDate")));
            paramMap.put("status", CommonUtils.checkNull(request.getParamValue("status")));
            paramMap.put("whId", CommonUtils.checkNull(request.getParamValue("whId")));
            paramMap.put("abjustmentType", CommonUtils.checkNull(request.getParamValue("abjustmentType")));
            paramMap.put("state", CommonUtils.checkNull(request.getParamValue("state")));
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
     * <p>Description: 调整库存调整申请新增页面</p>
     */
    public void toAddStockNumApply(){
        try {
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + logonUser.getOrgId() + "' ");
            List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
            
            String marker = logonUser.getName();
            String changeCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_62);//获取调整单号
            List<Map<String,String>> voList = null;
            
            act.setOutData("changeCode", changeCode);
            act.setOutData("marker", marker);
            act.setOutData("WHList", WHList);
            act.setOutData("list", voList);
            act.setForword(PART_STOCK_NUM_APPLY_ADD);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调整库存调整申请新增页面");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>Description: 获取库存配件</p>
     */
    public void queryPartStock(){
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("orgId", logonUser.getOrgId().toString());
            paramMap.put("partCode", CommonUtils.checkNull(request.getParamValue("partCode")));
            paramMap.put("partOldcode", CommonUtils.checkNull(request.getParamValue("partOldcode")));
            paramMap.put("partCname", CommonUtils.checkNull(request.getParamValue("partCname")));
            paramMap.put("whId", CommonUtils.checkNull(request.getParamValue("whId")));
            paramMap.put("itemQtyRangeStart", CommonUtils.checkNull(request.getParamValue("itemQtyRangeStart")));
            paramMap.put("itemQtyRangeEnd", CommonUtils.checkNull(request.getParamValue("itemQtyRangeEnd")));
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页      
            PageResult<Map<String, Object>> ps = dao.getValidPartsStock(paramMap, Constant.PAGE_SIZE, curPage);           
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调整库存调整申请新增页面");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>Description: 库存调整新增申请</p>
     */
    @SuppressWarnings("unchecked")
    public void saveStockNumApply(){
        try {
            String abjustmentCode = CommonUtils.checkNull(request.getParamValue("abjustmentCode")); // 调整编码
            String abjustmentName = CommonUtils.checkNull(request.getParamValue("abjustmentName")); // 调整名称
            int abjustmentType = Integer.parseInt(CommonUtils.checkNull(request.getParamValue("abjustmentType"))); // 调整类型
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 调整仓库
            String whCname = CommonUtils.checkNull(request.getParamValue("whName")); // 调整仓库名
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 调整备注
            String[] partIdAndLocIdArr = request.getParamValues("cb"); // 配件id和货位id数组
            
            int returnCode = 1;
            String msg = "保存成功！";
            Long abjId = Long.parseLong(SequenceManager.getSequence(""));
            List<TtPartStockAbjustmentDtlPO> dtlList = new ArrayList<TtPartStockAbjustmentDtlPO>();
            for(int i = 0; i <partIdAndLocIdArr.length; i++){
                String partAndLocId = partIdAndLocIdArr[i];
                String partId = partAndLocId.substring(0, partAndLocId.indexOf("_RNUM")); // 配件id
                String partCode = CommonUtils.checkNull(request.getParamValue("partCode_"+partAndLocId));//件号
                String partOldcode =  CommonUtils.checkNull(request.getParamValue("partOldcode_"+partAndLocId)); // 配件编码  
                String partCname =  CommonUtils.checkNull(request.getParamValue("partCname_"+partAndLocId)); // 配件名称
                long abjustmentNum = Long.parseLong(CommonUtils.checkNull(request.getParamValue("abjustmentNum_"+partAndLocId))); // 调整数量 
                String dtlRemark = CommonUtils.checkNull(request.getParamValue("remark_"+partAndLocId)); // 备注
                String bookId = CommonUtils.checkNull(request.getParamValue("bookId_"+partAndLocId)); // 资源id
                String stockId = CommonUtils.checkNull(request.getParamValue("stockId_"+partAndLocId)); // 库存id
                
                String loc =  CommonUtils.checkNull(request.getParamValue("locInfo_"+partAndLocId)); // 批次信息
                String[] locArr = loc.split(",");
                String locId = locArr[1]; // 货位id
                String locCode = locArr[2]; // 货位编码
                String locName = locArr[3]; // 货位名称
                String batchCode = locArr[4]; // 批次号
                
                // 获取配件库存信息
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("partId", partId);
                paramMap.put("locId", locId);
                paramMap.put("batchNo", batchCode);
                paramMap.put("whId", whId);
                paramMap.put("orgId", logonUser.getOrgId().toString());
                Map<String, Object> itemStockMap = dao.getPartStockList(paramMap).get(0);
                
                // 验证库存数量是否可减少
                long normalQty = Long.parseLong(CommonUtils.checkNull(itemStockMap.get("NORMAL_QTY"))); // 可用库存
                String itemQty = CommonUtils.checkNull(request.getParamValue("itemQty_"+partAndLocId)); // 库存数量
                if(abjustmentType == Constant.PART_ABJUSTMENT_TYPE_02 && normalQty < abjustmentNum){
                    returnCode = 2;
                    msg =  "【"+partOldcode+"】配件的可用库存数量不足，不能够减少库存！";
                    break;
                }
                
                TtPartStockAbjustmentDtlPO dtlPO = new TtPartStockAbjustmentDtlPO();
                dtlPO.setDltId(Long.parseLong(SequenceManager.getSequence(""))); // 明细id
                dtlPO.setAbjustmentId(abjId);
                dtlPO.setPartId(Long.parseLong(partId));
                dtlPO.setPartCode(partCode);
                dtlPO.setPartOldcode(partOldcode);
                dtlPO.setPartCname(partCname);
                dtlPO.setLocId(Long.parseLong(locId));
                dtlPO.setLocCode(locCode);
                dtlPO.setLocName(locName);
                dtlPO.setBookId(Long.parseLong(bookId));
                dtlPO.setStockId(Long.parseLong(stockId));
                dtlPO.setNormalQty(normalQty);
                dtlPO.setItemQty(Long.parseLong(itemQty));
                dtlPO.setAbjustmentNum(abjustmentNum);
                dtlPO.setBatchCode(batchCode);
                dtlPO.setRemark(dtlRemark);
                dtlPO.setCheckStatus(Constant.PART_ABJUSTMENT_CHECK_01);
                dtlPO.setCreateBy(logonUser.getUserId());
                dtlPO.setCreateDate(new Date());
                dtlPO.setStatus(Constant.STATUS_ENABLE);
                dtlList.add(dtlPO);
            }
            
            if(returnCode == 1){
                // 主要信息
                TtPartStockAbjustmentMainPO mainPO = new TtPartStockAbjustmentMainPO();
                mainPO.setAbjustmentId(abjId); // 调整id
                mainPO.setAbjustmentCode(abjustmentCode); // 调整编码
                mainPO.setAbjustmentName(abjustmentName); // 调整名称
                mainPO.setWhId(Long.parseLong(whId)); // 调整仓库id
                mainPO.setWhCname(whCname);
                mainPO.setAbjustmentType(abjustmentType); // 调整类型
                mainPO.setRemark(remark); // 备注
                mainPO.setApplyState(Constant.PART_ABJUSTMENT_APPLY_01); // 申请状态-未申请 
                mainPO.setCheckState(Constant.PART_ABJUSTMENT_CHECK_01); // 审核状态-未审核
                mainPO.setState(Constant.PART_ABJUSTMENT_STATE_01); // 调整状态-未提交审核
                mainPO.setStatus(Constant.STATUS_ENABLE); // 状态-有效
                mainPO.setCreateBy(logonUser.getUserId()); // 创建人
                mainPO.setCreateDate(new Date()); // 创建时间
                mainPO.setOrgId(logonUser.getOrgId());
                mainPO.setOrgCode(logonUser.getCompanyCode());
                mainPO.setOrgName(dao.getMainCompanyName(logonUser.getOrgId().toString()));
                // 插入主表记录
                dao.insert(mainPO);
                
                // 插入明细表记录
                dao.insert(dtlList);
                
                // 减少库存时锁定资源
                if(abjustmentType == Constant.PART_ABJUSTMENT_TYPE_02){
                    List<Object> ins2 = new LinkedList<Object>();
                    ins2.add(0, abjId);
                    ins2.add(1, Constant.PART_CODE_RELATION_62);
                    ins2.add(2, 1);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
                }
            }
            
            
            act.setOutData("returnCode", returnCode);
            act.setOutData("msg", msg);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整申请新增");
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
            //paramMap.put("status", Constant.STATUS_ENABLE.toString());
            // 调整主要信息
            Map<String, Object> infoMainMap = dao.getAbjustmentMainList(paramMap).get(0);
            // 调整明细
            List<Map<String, Object>> dtlList = dao.getAbjustmentDetailList(paramMap);
            act.setOutData("mainMap", infoMainMap);
            act.setOutData("dtlList", dtlList);
            if("view".equals(actionType)){
                act.setForword(PART_STOCK_NUM_APPLY_VIEW);
            }else if("edit".equals(actionType)){
                String parentOrgId = "";//父机构（销售单位）ID
                //判断主机厂与服务商
                String comp = logonUser.getOemCompanyId();
                if (null == comp ){
                    parentOrgId = Constant.OEM_ACTIVITIES;
                }else {
                    parentOrgId = logonUser.getDealerId();
                }
                StringBuffer sbString = new StringBuffer();
                sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
                List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
                act.setOutData("WHList", WHList);
                act.setForword(PART_STOCK_NUM_APPLY_MOD);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整详情");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>Description: 库存调整修改申请</p>
     */
    @SuppressWarnings("unchecked")
    public void modStockNumApply(){
        try {
            String abjIdStr = CommonUtils.checkNull(request.getParamValue("abjustmentId")); // 调整编码
            String[] partIdAndLocIdArr = request.getParamValues("cb"); // 配件id和货位id数组
            if(CommonUtils.isEmpty(abjIdStr) || partIdAndLocIdArr.length <= 0){
                act.setOutData("returnCode", -1);
                act.setOutData("msg", "保存失败！");
            }

            String abjustmentCode = CommonUtils.checkNull(request.getParamValue("abjustmentCode")); // 调整编码
            String abjustmentName = CommonUtils.checkNull(request.getParamValue("abjustmentName")); // 调整名称
            int abjustmentType = Integer.parseInt(CommonUtils.checkNull(request.getParamValue("abjustmentType"))); // 调整类型
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 调整仓库
            String whCname = CommonUtils.checkNull(request.getParamValue("whName")); // 调整仓库名
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 调整备注

            Long abjId = Long.parseLong(abjIdStr);
            
            // 减少库存时释放资源
            if(abjustmentType == Constant.PART_ABJUSTMENT_TYPE_02){
                List<Object> ins2 = new LinkedList<Object>();
                ins2.add(0, abjId);
                ins2.add(1, Constant.PART_CODE_RELATION_62);
                ins2.add(2, 0);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
            }

            int returnCode = 1;
            String msg = "保存成功！";
            List<TtPartStockAbjustmentDtlPO> dtlList = new ArrayList<TtPartStockAbjustmentDtlPO>();
            for(int i = 0; i <partIdAndLocIdArr.length; i++){
                String partAndLocId = partIdAndLocIdArr[i];
                String partId = partAndLocId.substring(0, partAndLocId.indexOf("_RNUM")); // 配件id
                String partCode = CommonUtils.checkNull(request.getParamValue("partCode_"+partAndLocId));//件号
                String partOldcode =  CommonUtils.checkNull(request.getParamValue("partOldcode_"+partAndLocId)); // 配件编码  
                String partCname =  CommonUtils.checkNull(request.getParamValue("partCname_"+partAndLocId)); // 配件名称
                long abjustmentNum = Long.parseLong(CommonUtils.checkNull(request.getParamValue("abjustmentNum_"+partAndLocId))); // 调整数量 
                String dtlRemark = CommonUtils.checkNull(request.getParamValue("remark_"+partAndLocId)); // 备注
                String bookId = CommonUtils.checkNull(request.getParamValue("bookId_"+partAndLocId)); // 资源id
                String stockId = CommonUtils.checkNull(request.getParamValue("stockId_"+partAndLocId)); // 库存id
                
                String loc =  CommonUtils.checkNull(request.getParamValue("locInfo_"+partAndLocId)); // 批次信息
                String[] locArr = loc.split(",");
                String locId = locArr[1]; // 货位id
                String locCode = locArr[2]; // 货位编码
                String locName = locArr[3]; // 货位名称
                String batchCode = locArr[4]; // 批次号
                
                // 获取配件库存信息
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("partId", partId);
                paramMap.put("locId", locId);
                paramMap.put("batchNo", batchCode);
                paramMap.put("whId", whId);
                paramMap.put("orgId", logonUser.getOrgId().toString());
                Map<String, Object> itemStockMap = dao.getPartStockList(paramMap).get(0);
                
                // 验证库存数量是否可减少
                long normalQty = Long.parseLong(CommonUtils.checkNull(itemStockMap.get("NORMAL_QTY"))); // 可用库存
                String itemQty = CommonUtils.checkNull(request.getParamValue("itemQty_"+partAndLocId)); // 库存数量
                if(abjustmentType == Constant.PART_ABJUSTMENT_TYPE_02 && normalQty < abjustmentNum){
                    returnCode = 2;
                    msg =  "【"+partOldcode+"】配件的可用库存数量不足，不能够减少库存！";
                    break;
                }
                
                TtPartStockAbjustmentDtlPO dtlPO = new TtPartStockAbjustmentDtlPO();
                dtlPO.setDltId(Long.parseLong(SequenceManager.getSequence(""))); // 明细id
                dtlPO.setAbjustmentId(abjId);
                dtlPO.setPartId(Long.parseLong(partId));
                dtlPO.setPartCode(partCode);
                dtlPO.setPartOldcode(partOldcode);
                dtlPO.setPartCname(partCname);
                dtlPO.setLocId(Long.parseLong(locId));
                dtlPO.setLocCode(locCode);
                dtlPO.setLocName(locName);
                dtlPO.setBookId(Long.parseLong(bookId));
                dtlPO.setStockId(Long.parseLong(stockId));
                dtlPO.setNormalQty(normalQty);
                dtlPO.setItemQty(Long.parseLong(itemQty));
                dtlPO.setAbjustmentNum(abjustmentNum);
                dtlPO.setBatchCode(batchCode);
                dtlPO.setRemark(dtlRemark);
                dtlPO.setCheckStatus(Constant.PART_ABJUSTMENT_CHECK_01);
                dtlPO.setCreateBy(logonUser.getUserId());
                dtlPO.setCreateDate(new Date());
                dtlPO.setStatus(Constant.STATUS_ENABLE);
                dtlList.add(dtlPO);
            }

            if(returnCode == 1){
                // 主要信息
                TtPartStockAbjustmentMainPO upMainPO = new TtPartStockAbjustmentMainPO();
                upMainPO.setAbjustmentCode(abjustmentCode); // 调整编码
                upMainPO.setAbjustmentName(abjustmentName); // 调整名称
                upMainPO.setWhId(Long.parseLong(whId)); // 调整仓库id
                upMainPO.setWhCname(whCname);
                upMainPO.setAbjustmentType(abjustmentType); // 调整类型
                upMainPO.setRemark(remark); // 备注
                upMainPO.setUpdateBy(logonUser.getUserId()); // 创建人
                upMainPO.setUpdateDate(new Date()); // 创建时间
                // 更新主表记录
                TtPartStockAbjustmentMainPO selMainPo = new TtPartStockAbjustmentMainPO();
                selMainPo.setAbjustmentId(abjId);
                dao.update(selMainPo, upMainPO);
                
                // 删除原来配件调整明细记录
                TtPartStockAbjustmentDtlPO delDtlPo = new TtPartStockAbjustmentDtlPO();
                dao.delete(delDtlPo);
                // 插入明细表记录
                dao.insert(dtlList);
            }
            
            // 锁定资源
            if(abjustmentType == Constant.PART_ABJUSTMENT_TYPE_02){
                List<Object> ins2 = new LinkedList<Object>();
                ins2.add(0, abjId);
                ins2.add(1, Constant.PART_CODE_RELATION_62);
                ins2.add(2, 1);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
            }

            act.setOutData("returnCode", returnCode);
            act.setOutData("msg", msg);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整申请新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>Description: 提交申请</p>
     */
    @SuppressWarnings("unchecked")
    public void submitApply() {
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if("".equals(curPage)){
                curPage = "1";
            }
            act.setOutData("curPage", curPage);
            String abjustmentId = CommonUtils.checkNull(request.getParamValue("abjustmentId")); 
            if(CommonUtils.isEmpty(abjustmentId)){
                act.setOutData("returnCode", "-1");
                act.setOutData("msg", "提交申请失败！");
            }
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("abjustmentId", abjustmentId);
            paramMap.put("isLocked", Constant.PART_STATE_LOCKED.toString());
            List<Map<String, Object>> lockedItemStockList = dao.getItemStockList(paramMap);
            if(lockedItemStockList.size() > 0){
                act.setOutData("returnCode", "-2");
                act.setOutData("msg", "该记录下的配件库存被锁定了，不能够提交库存调整申请！");
                return;
            }
            
            // 锁定库存
            List<Map<String, Object>> dtlList = dao.getAbjustmentDetailList(paramMap);
            for(int i = 0 ; i < dtlList.size(); i++){
                Map<String, Object> itemStockMap = dtlList.get(i);
                TtPartItemStockPO selItemPO = new TtPartItemStockPO();
                selItemPO.setStockId(Long.parseLong(itemStockMap.get("STOCK_ID").toString()));
                TtPartItemStockPO upItemPO = new TtPartItemStockPO();
                upItemPO.setIsLocked(Constant.PART_STATE_LOCKED);
                dao.update(selItemPO, upItemPO);
            }

            // 更新申请标准信息
            TtPartStockAbjustmentMainPO selMainPo = new TtPartStockAbjustmentMainPO();
            selMainPo.setAbjustmentId(Long.parseLong(abjustmentId));
            TtPartStockAbjustmentMainPO upMainPo = new TtPartStockAbjustmentMainPO();
            upMainPo.setApplyState(Constant.PART_ABJUSTMENT_APPLY_02);
            upMainPo.setState(Constant.PART_ABJUSTMENT_STATE_02);
            upMainPo.setApplyBy(logonUser.getUserId());
            upMainPo.setApplyDate(new Date());
            upMainPo.setUpdateBy(logonUser.getUserId());
            upMainPo.setUpdateDate(new Date());
            dao.update(selMainPo, upMainPo);

            act.setOutData("returnCode", "1");
            act.setOutData("msg", "提交申请成功！");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整提交申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>Description: 失效库存调整申请</p>
     */
    @SuppressWarnings("unchecked")
    public void invalidApply(){
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if("".equals(curPage)){
                curPage = "1";
            }
            act.setOutData("curPage", curPage);
            String abjustmentId = CommonUtils.checkNull(request.getParamValue("abjustmentId")); 
            String type = CommonUtils.checkNull(request.getParamValue("type")); 
            if(CommonUtils.isEmpty(abjustmentId) || CommonUtils.isEmpty(type)){
                act.setOutData("returnCode", "-1");
                act.setOutData("msg", "失效失败！");
            }

            TtPartStockAbjustmentMainPO selMainPo = new TtPartStockAbjustmentMainPO();
            selMainPo.setAbjustmentId(Long.parseLong(abjustmentId));
            TtPartStockAbjustmentMainPO upMainPo = new TtPartStockAbjustmentMainPO();
            upMainPo.setStatus(Constant.STATUS_DISABLE);
            upMainPo.setUpdateBy(logonUser.getUserId());
            upMainPo.setUpdateDate(new Date());
            dao.update(selMainPo, upMainPo);

            // 释放资源
            if(Integer.parseInt(type) == Constant.PART_ABJUSTMENT_TYPE_02){
                List<Object> ins2 = new LinkedList<Object>();
                ins2.add(0, selMainPo.getAbjustmentId());
                ins2.add(1, Constant.PART_CODE_RELATION_62);
                ins2.add(2, 0);// 1:占用 0：释放占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);
            }
            
            
            act.setOutData("returnCode", 1);
            act.setOutData("msg", "保存成功！");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库存调整提交申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
}
