package com.infodms.dms.actions.parts.storageManager.partReturnManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.taglibs.standard.tag.common.core.ParamParent;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.publicSaleCarBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartOemReturnApplyDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.po.TtPartOemReturnDtlPO;
import com.infodms.dms.po.TtPartOemReturnMainPO;
import com.infodms.dms.po.TtPartPoInPO;
import com.infodms.dms.po.TtPartVenderDefinePO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;

/**
 * @author : chenjunjiang
 *         CreateDate     : 2013-4-27
 * @ClassName : PartOemReturnApplyManager
 * @Description : 采购退货申请
 */
@SuppressWarnings("unchecked")
public class PartOemReturnApplyManager implements PTConstants {

	private static final Integer PRINT_SIZE = 10;
    public Logger logger = Logger.getLogger(PartOemReturnApplyManager.class);
    private PartOemReturnApplyDao dao = PartOemReturnApplyDao.getInstance();
    ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-27
     * @Title :
     * @Description: 采购退货申请查询初始化
     */
    public void queryPartOemReturnApplyInit() {
        try {
        	act.setOutData("old",CommonUtils.getBefore(new Date()));
            act.setOutData("now",CommonUtils.getDate());
            act.setForword(PART_OEMRETURN_APPLY_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购入库信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-2
     * @Title :
     * @Description: 查询采购退货申请信息
     */
    public void queryPartOemReturnApplyInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
            String returnType = CommonUtils.checkNull(request.getParamValue("PART_OEM_RETURN_TYPE"));//退货类型
            String createName = CommonUtils.checkNull(request.getParamValue("CREATE_NAME"));//制单人
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//制单结束时间

            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("returnCode", returnCode);
            paramMap.put("returnType", returnType);
            paramMap.put("createName", createName);
            paramMap.put("startDate", startDate);
            paramMap.put("endDate", endDate);
            paramMap.put("state", state);
            
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOemReturnApplyList(paramMap, logonUser, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }


    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-2
     * @Title :
     * @Description: 采购申请的详细信息查询初始化
     */
    public void queryOemReturnApplyDetailInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String state = CommonUtils.checkNull(request.getParamValue("state"));
            String pageFlag = CommonUtils.checkNull(request.getParamValue("pageFlag"));
//            boolean flag = true;//是否是车厂
//            if (logonUser.getDealerId() != null) {
//                flag = false;
//            }
            Map<String, Object> map = dao.getPartOemReturnMainInfo(returnId);
            request.setAttribute("po", map);
            request.setAttribute("state", state);
            request.setAttribute("pageFlag", pageFlag);
            request.setAttribute("rstate", Constant.PART_OEM_RETURN_STATUS_02);
            act.setForword(PART_OEMRETURN_APPLY_VIEW_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "采购退货申请明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-2
     * @Title :
     * @Description: 通过returnid查询采购申请明细
     */
    public void queryPartOemReturnApplyById() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOemReturnDetailList(returnId, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购退货申请明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-27
     * @Title :
     * @Description: 采购退货申请初始化, 转向入库信息查询页面
     */
    public void queryOrderInInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
        	List<TtPartWarehouseDefinePO> list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
            act.setOutData("wareHouses", list);
            act.setForword(PART_OEMRETURN_IN_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购入库信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * <p>
     * Description: 分页查询入库单
     * </p>
     */
    public void queryInCode(){
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("orgId", logonUser.getOrgId().toString());
            paramMap.put("inCode", request.getParamValue("inCode"));
            paramMap.put("beginTime", request.getParamValue("beginTime"));
            paramMap.put("endTime", request.getParamValue("endTime"));
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderInList(paramMap, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单入库");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-28
     * @Title :
     * @Description: 查询入库单信息
     */
    public void queryOrderInInfo() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//采购订单号
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));//入库单号
            String buyer = CommonUtils.checkNull(request.getParamValue("BUYER"));//采购员
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//制单开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//制单结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String produceState = CommonUtils.checkNull(request.getParamValue("PRODUCE_STATE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String inBeginTime = CommonUtils.checkNull(request.getParamValue("inBeginTime"));//入库开始时间
            String inEndTime = CommonUtils.checkNull(request.getParamValue("inEndTime"));//入库结束时间
            String inName = CommonUtils.checkNull(request.getParamValue("IN_NAME"));//入库人员

            TtPartPoInPO po = new TtPartPoInPO();
            po.setOrderCode(orderCode);
            po.setInCode(inCode);
            if (!"".equals(whId)) {
                po.setWhId(CommonUtils.parseLong(whId));
            }
            if (!"".equals(produceState)) {
                po.setProduceState(CommonUtils.parseInteger(produceState));
            }
            if (!"".equals(venderId)) {
                po.setVenderId(CommonUtils.parseLong(venderId));
            }
            po.setPartOldcode(partOldCode);
            po.setPartCname(partName);

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderInList(po, beginTime, endTime, inBeginTime, inEndTime,
                    inName, buyer, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单入库");
            logger.error(logonUser, e1);
            act.setException(e1);
        }


    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-28
     * @Title :
     * @Description: 跳转新增退货申请页面
     */
    public void generateApplyInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String ids = request.getParamValue("ids");//入库id
            String returnCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_18);//退货单号

            long orgId = 0;//制单单位id
            String createOrgName = "";//制单单位名称
            String createOrgCode = "";//制单单位编码
            TmOrgPO tmOrgPO = new TmOrgPO();
            orgId = logonUser.getOrgId();
            tmOrgPO.setOrgId(orgId);
            tmOrgPO = (TmOrgPO) dao.select(tmOrgPO).get(0);
            createOrgName = tmOrgPO.getOrgName();
            createOrgCode = tmOrgPO.getOrgCode();

            //查询仓库
            TtPartWarehouseDefinePO wDefinePO = new TtPartWarehouseDefinePO();
            wDefinePO.setOrgId(orgId);
            List<TtPartWarehouseDefinePO> list = dao.select(wDefinePO);
            
            act.setOutData("ids", ids);
            act.setOutData("returnCode", returnCode);
            act.setOutData("orgId", orgId);
            act.setOutData("createOrgCode", createOrgCode);
            act.setOutData("createOrgName", createOrgName);
            act.setOutData("wareHouses", list);
            act.setForword(PART_OEMRETURN_APPLY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "采购退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    public void modApplyInit(){
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            
            String ids = request.getParamValue("ids");//入库id

            long orgId = 0;//制单单位id
            String createOrgName = "";//制单单位名称
            String createOrgCode = "";//制单单位编码
            TmOrgPO tmOrgPO = new TmOrgPO();
            orgId = logonUser.getOrgId();
            tmOrgPO.setOrgId(orgId);
            tmOrgPO = (TmOrgPO) dao.select(tmOrgPO).get(0);
            createOrgName = tmOrgPO.getOrgName();
            createOrgCode = tmOrgPO.getOrgCode();

            //查询仓库
            TtPartWarehouseDefinePO wDefinePO = new TtPartWarehouseDefinePO();
            wDefinePO.setOrgId(orgId);
            List<TtPartWarehouseDefinePO> list = dao.select(wDefinePO);
            // 退货主要信息
            Map<String, Object> map = dao.getPartOemReturnMainInfo(returnId);
            List<Map<String, Object>> dtlList = dao.queryApplyDetailListById(returnId);
            
            act.setOutData("po", map);
            act.setOutData("dtlList", dtlList);
            act.setOutData("ids", ids);
            act.setOutData("orgId", orgId);
            act.setOutData("createOrgCode", createOrgCode);
            act.setOutData("createOrgName", createOrgName);
            act.setOutData("wareHouses", list);
            act.setForword(PART_OEMRETURN_APPLY_MOD_URL);
            
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "采购退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-28
     * @Title :
     * @Description: 生成退货申请(无入库单)（废弃，必须入库后才能退货）
     */
    public void generateApplyInit1() {
    	try {
    		String returnCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_18);//退货单号
    		long orgId = 0;//制单单位id
    		String createOrgName = "";//制单单位名称
    		String createOrgCode = "";//制单单位编码
    		TmOrgPO tmOrgPO = new TmOrgPO();
    		orgId = logonUser.getOrgId();
    		tmOrgPO.setOrgId(orgId);
    		tmOrgPO = (TmOrgPO) dao.select(tmOrgPO).get(0);
    		createOrgName = tmOrgPO.getOrgName();
    		createOrgCode = tmOrgPO.getOrgCode();
    		
            PurchasePlanSettingDao planSettingDao =  PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> list = planSettingDao.getWareHouse(orgId+"");
            List<Map<String, Object>> list2 = planSettingDao.getWareHouse(1+"");

    		act.setOutData("returnCode", returnCode);
    		act.setOutData("orgId", orgId);
    		act.setOutData("createOrgCode", createOrgCode);
    		act.setOutData("createOrgName", createOrgName);
    		act.setOutData("list", list);
    		act.setOutData("list2", list2);
    		act.setForword(PART_OEMRETURN_APPLYNO_URL);
    	} catch (Exception e) {//异常方法
    		BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "采购退货申请");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-28
     * @Title :
     * @Description: 查询申请明细
     */
    public void queryPartOemReturnApplyDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String idStr = request.getParamValue("ids");//入库id
            String[] ids = null;
            if (idStr != null && !"".equals(idStr)) {
                ids = idStr.split(",");
            }
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryApplyDetailList(ids, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购退货申请明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * 
     * @Title      : 
     * @Description: 查询配件信息 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-2
     */
    public void queryPartInfo(){
    	 ActionContext act = ActionContext.getContext();
         RequestWrapper request = act.getRequest();
         AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String partCode =CommonUtils.checkNull(request.getParamValue("PART_CODE"));
			String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
			String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
			
			String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//仓库id
			
			TtPartDefinePO po = new TtPartDefinePO();
			po.setPartCode(partCode);
			po.setPartOldcode(partOldCode);
			po.setPartCname(partCname);
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartInfoList(po,whId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-29
     * @Title :
     * @Description: 提交或保存采购退货申请
     */
    public void submitReturnApply()  {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String[] inIds = request.getParamValues("cb");//入库id
            String returnCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_18);//退货单号
            String orgId = request.getParamValue("orgId");//制单单位id
            String orgCode = request.getParamValue("orgCode");//单位编码
            String orgName = request.getParamValue("orgName");//单位名称
            String reason = request.getParamValue("reason");//退货原因
            String type = CommonUtils.checkNull(request.getParamValue("type"));// 1:保存,2:提交
//            String selCheckCode = "";//验货单号
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));
            

            TtPartOemReturnMainPO mainPO = new TtPartOemReturnMainPO();
            mainPO.setReturnId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            mainPO.setReturnCode(returnCode);
        	mainPO.setCheckCode(inCode); // 入库单号
            mainPO.setOrgId(CommonUtils.parseLong(orgId));
            mainPO.setOrgCode(orgCode);
            mainPO.setOrgName(orgName);
            mainPO.setRemark(reason);
            mainPO.setCreateDate(new Date());
            mainPO.setCreateBy(logonUser.getUserId());
            mainPO.setCreateOrg(CommonUtils.parseLong(orgId));
            // 1:保存,2:提交
            if("2".equals(type)){
                mainPO.setApplyDate(new Date());
                mainPO.setApplyBy(logonUser.getUserId());
                mainPO.setApplyDate(new Date());
                mainPO.setState(Constant.PART_OEM_RETURN_STATUS_01);//审核中
            }else{
                mainPO.setState(Constant.PART_OEM_RETURN_STATUS_00);//未提交
            }
            
            mainPO.setReturnType(Constant.PART_OEM_RETURN_TYPE_01);
            mainPO.setReturnTo(1); // 1：退回供应商， 2:退回北汽银翔

            String error = "";
            //查询申请中的配件对应的入库数量以及可用数量,然后与申请退货数量比较
            List<Map<String, Object>> list = dao.queryInAndNormalQty(inIds, "");
            List<TtPartOemReturnDtlPO> insertList = new ArrayList<TtPartOemReturnDtlPO>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                
                long inId = ((BigDecimal) map.get("IN_ID")).longValue(); // 入库id
                long applyQty = CommonUtils.parseLong(request.getParamValue("APPLY_QTY"+inId));
                long inQty = ((BigDecimal) map.get("IN_QTY")).longValue(); // 入库数量
                long applyQty2 = ((BigDecimal) map.get("APPLY_QTY")).longValue();// 已申请数量
//                long returnQty = ((BigDecimal) map.get("RETURN_QTY")).longValue();// 已退货数量
                long normalQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue(); // 可用库存
                
                String loc = CommonUtils.checkNull(request.getParamValue("LOC_"+inId));
                String[] applyQtys = request.getParamValues("S_APPLY_QTY"+loc);
                int count = 0;
                for(int j = 0; j < applyQtys.length; j++){
                    if(!CommonUtils.isEmpty(applyQtys[i])){
                        count += Integer.parseInt(applyQtys[i]);
                    }
                }
                if(count > normalQty){
                    error = "配件【"+map.get("PART_OLDCODE")+"】的退货数量不能大于可用库存,请在刷新后再重新确认!";
                    break;
                }
//                if(applyQty + applyQty2 + returnQty > inQty){
                if(applyQty + applyQty2 > inQty){
                    error = "配件【"+map.get("PART_OLDCODE")+"】的退货数量与已退货数量之和不能大于入库数量,请在刷新后再重新确认!";
                    break;
                }
                
                String batchNo = map.get("BATCH_NO").toString(); // 批次号
                
                TtPartOemReturnDtlPO dtlPO = new TtPartOemReturnDtlPO();
                dtlPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                dtlPO.setReturnId(mainPO.getReturnId());
                dtlPO.setVenderId(((BigDecimal) map.get("VENDER_ID")).longValue());
                dtlPO.setVenderCode((String) map.get("VENDER_CODE"));
                dtlPO.setVenderName((String) map.get("VENDER_NAME"));
                dtlPO.setUnit((String) map.get("UNIT"));
                dtlPO.setPartId(((BigDecimal) map.get("PART_ID")).longValue());
                dtlPO.setPartCode((String) map.get("PART_CODE"));
                dtlPO.setPartOldcode((String) map.get("PART_OLDCODE"));
                dtlPO.setPartCname((String) map.get("PART_CNAME"));
                dtlPO.setItemQty(((BigDecimal) map.get("ITEM_QTY")).longValue());
                dtlPO.setNormalQty(((BigDecimal) map.get("NORMAL_QTY")).longValue());
                dtlPO.setInQty(((BigDecimal) map.get("IN_QTY")).longValue());
                dtlPO.setApplyQty(applyQty);
                dtlPO.setBuyPrice(((BigDecimal) map.get("BUY_PRICE")).doubleValue());
                dtlPO.setRemark(request.getParamValue("REMARK"+inId));
                dtlPO.setCreateDate(new Date());
                dtlPO.setCreateBy(logonUser.getUserId());
                dtlPO.setInId(inId);//入库单ID
                dtlPO.setInCode((String) map.get("IN_CODE"));
                //默认情况下入库库房就是出库库房
                dtlPO.setStockIn(((BigDecimal) map.get("WH_ID")).longValue());//入库库房id
                dtlPO.setStockOut(((BigDecimal) map.get("WH_ID")).longValue());//出库库房id
                dtlPO.setLocId(((BigDecimal) map.get("LOC_ID")).longValue());
                dtlPO.setLocCode(map.get("LOC_CODE").toString());
                dtlPO.setBatchNo(batchNo);
                insertList.add(dtlPO);

            }
            if (!"".equals(error)) {
                act.setOutData("error", error);
                POContext.endTxn(false);
                return;
            }
            dao.insert(mainPO);
            dao.insert(insertList);

            //更新已申请的退货数量
            if("2".equals(type)){
                for (int i = 0; i < insertList.size(); i++) {
                    long inId = insertList.get(i).getInId();
                    long applyQty = insertList.get(i).getApplyQty();
                    TtPartPoInPO poInPO = new TtPartPoInPO();
                    poInPO.setInId(inId);
                    poInPO = (TtPartPoInPO) dao.select(poInPO).get(0);
                    
                    TtPartPoInPO poInPO1 = new TtPartPoInPO();
                    poInPO1.setInId(inId);
                    
                    TtPartPoInPO poInPO2 = new TtPartPoInPO();
                    poInPO2.setApplyQty(poInPO.getApplyQty()+applyQty);
                    dao.update(poInPO1,poInPO2);
                }
            }

            //调用库存占用逻辑
            List<Object> ins = new LinkedList<Object>();
            ins.add(0, mainPO.getReturnId());
            ins.add(1, Constant.PART_CODE_RELATION_18);
            ins.add(2,1);// 1:占用 0：释放占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

            POContext.endTxn(true);

            if("2".equals(type)){
                act.setOutData("success", "申请提交成功!");
            }else{
                act.setOutData("success", "申请保存成功!");
            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "采购退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>
     * Description: 修改采购退货申请
     * </p>
     */
    public void modReturnOrder(){
        try {
            String[] inIds = request.getParamValues("cb");//入库id
            String reason = request.getParamValue("reason");//退货原因
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE")); // 入库单号
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId")); // 入库单号
            
            TtPartOemReturnMainPO selMainPO = new TtPartOemReturnMainPO();
            selMainPO.setReturnId(Long.parseLong(returnId));
            
            String error = "";
            List<TtPartOemReturnDtlPO> dtlPoList = new ArrayList<TtPartOemReturnDtlPO>();
            
            // 释放占用
            List<Object> ins = new LinkedList<Object>();
            ins.add(0, returnId);
            ins.add(1, Constant.PART_CODE_RELATION_18);
            ins.add(2,0);// 1:占用 0：释放占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
            
            //查询申请中的配件对应的入库数量以及可用数量,然后与申请退货数量比较
            List<Map<String, Object>> list = dao.queryInAndNormalQty(inIds, "");
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                long inId = ((BigDecimal) map.get("IN_ID")).longValue(); // 入库id
                long applyQty = CommonUtils.parseLong(request.getParamValue("APPLY_QTY"+inId));
                long inQty = ((BigDecimal) map.get("IN_QTY")).longValue(); // 入库数量
                long applyQty2 = ((BigDecimal) map.get("APPLY_QTY")).longValue();// 已申请数量
//                long returnQty = ((BigDecimal) map.get("RETURN_QTY")).longValue();// 已退货数量
                long normalQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue(); // 可用库存
                
                String loc = CommonUtils.checkNull(request.getParamValue("LOC_"+inId));
                String[] applyQtys = request.getParamValues("S_APPLY_QTY"+loc);
                int count = 0;
                for(int j = 0; j < applyQtys.length; j++){
                    if(!CommonUtils.isEmpty(applyQtys[i])){
                        count += Integer.parseInt(applyQtys[i]);
                    }
                }
                if(count > normalQty){
                    error = "配件【"+map.get("PART_OLDCODE")+"】的退货数量不能大于可用库存,请在刷新后再重新确认!";
                    break;
                }
//                if(applyQty + applyQty2 + returnQty > inQty){
                if(applyQty + applyQty2 > inQty){
                    error = "配件【"+map.get("PART_OLDCODE")+"】的退货数量与已退货数量之和不能大于入库数量,请在刷新后再重新确认!";
                    break;
                }
                
                String batchNo = map.get("BATCH_NO").toString(); // 批次号
                
                TtPartOemReturnDtlPO dtlPO = new TtPartOemReturnDtlPO();
                dtlPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                dtlPO.setReturnId(Long.parseLong(returnId));
                dtlPO.setVenderId(((BigDecimal) map.get("VENDER_ID")).longValue());
                dtlPO.setVenderCode((String) map.get("VENDER_CODE"));
                dtlPO.setVenderName((String) map.get("VENDER_NAME"));
                dtlPO.setUnit((String) map.get("UNIT"));
                dtlPO.setPartId(((BigDecimal) map.get("PART_ID")).longValue());
                dtlPO.setPartCode((String) map.get("PART_CODE"));
                dtlPO.setPartOldcode((String) map.get("PART_OLDCODE"));
                dtlPO.setPartCname((String) map.get("PART_CNAME"));
                dtlPO.setItemQty(((BigDecimal) map.get("ITEM_QTY")).longValue());
                dtlPO.setNormalQty(((BigDecimal) map.get("NORMAL_QTY")).longValue());
                dtlPO.setInQty(((BigDecimal) map.get("IN_QTY")).longValue());
                dtlPO.setApplyQty(applyQty);
                dtlPO.setBuyPrice(((BigDecimal) map.get("BUY_PRICE")).doubleValue());
                dtlPO.setRemark(request.getParamValue("REMARK"+inId));
                dtlPO.setCreateDate(new Date());
                dtlPO.setCreateBy(logonUser.getUserId());
                dtlPO.setInId(inId);//入库单ID
                dtlPO.setInCode((String) map.get("IN_CODE"));
                //默认情况下入库库房就是出库库房
                dtlPO.setStockIn(((BigDecimal) map.get("WH_ID")).longValue());//入库库房id
                dtlPO.setStockOut(((BigDecimal) map.get("WH_ID")).longValue());//出库库房id
                dtlPO.setLocId(((BigDecimal) map.get("LOC_ID")).longValue());
                dtlPO.setLocCode(map.get("LOC_CODE").toString());
                dtlPO.setBatchNo(batchNo);
                dtlPoList.add(dtlPO);
            }
            
            if (!"".equals(error)) {
                act.setOutData("error", error);
                POContext.endTxn(false);
                return;
            }
            // 退货主要信息
            TtPartOemReturnMainPO upMainPO = new TtPartOemReturnMainPO();
            upMainPO.setCheckCode(inCode);
            upMainPO.setRemark(reason);
            upMainPO.setUpdateBy(logonUser.getUserId());
            upMainPO.setUpdateDate(new Date());
            
            // 根据退货id删掉的明细记录
            TtPartOemReturnDtlPO delDtlPO = new TtPartOemReturnDtlPO();
            delDtlPO.setReturnId(selMainPO.getReturnId());
            
            // 更新主记录
            dao.update(selMainPO, upMainPO);
            // 删除明细记录
            dao.delete(delDtlPO);
            // 插入明细记录
            dao.insert(dtlPoList);
            
            //调用库存占用逻辑
            ins = new LinkedList<Object>();
            ins.add(0, returnId);
            ins.add(1, Constant.PART_CODE_RELATION_18);
            ins.add(2,1);// 1:占用 0：释放占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

            POContext.endTxn(true);
            act.setOutData("success", "退货申请修改成功!");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "修改采购退货申请");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * <p>
     * Description: 提交采购退货申请
     * </p>
     */
    public void sumitReturnOrder(){
        try{
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId")); // 退货单id
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            // 获取退货单主要信息
            TtPartOemReturnMainPO mainPO = new TtPartOemReturnMainPO();
            mainPO.setReturnId(CommonUtils.parseLong(returnId));
            mainPO = (TtPartOemReturnMainPO) dao.select(mainPO).get(0);
            
            // 验证采购退货申请是否存在出库（入库）情况
            String sqlStr = " AND RD.RETURN_ID = '"+ mainPO.getReturnId() +"' ";
            List<Map<String,Object>> orderChkList = dao.orderStateCheck(sqlStr);

            int outCount = 0;
            if(null != orderChkList && orderChkList.size() > 0){
                outCount = Integer.parseInt(orderChkList.get(0).get("OUT_COUNT").toString());
            }
            
            String errorMsg = "";

            if(mainPO.getState().intValue() == Constant.PART_OEM_RETURN_STATUS_00 || outCount  == 0){
                //查询申请中的配件对应的入库数量以及可用数量,然后与申请退货数量比较
                List<Map<String, Object>> list = dao.queryInAndNormalQty(null, returnId);
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    long applyQty = ((BigDecimal) map.get("THIS_APPLY_QTY")).longValue();
                    long inQty = ((BigDecimal) map.get("IN_QTY")).longValue(); // 入库数量
                    long applyQty2 = ((BigDecimal) map.get("APPLY_QTY")).longValue();// 已申请数量
//                    long returnQty = ((BigDecimal) map.get("RETURN_QTY")).longValue();// 已退货数量
                    long normalQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue(); // 可用库存
                    long sunQty = ((BigDecimal) map.get("SUM_QTY")).longValue(); // 该退货单下同一批次，同一配件，同一货位，同一仓库的退货数量之和
                    
                    if(sunQty > normalQty){
                        errorMsg = "配件【"+map.get("PART_OLDCODE")+"】的退货数量不能大于可用库存,请在刷新后再重新确认!";
                        break;
                    }
//                    if(applyQty + applyQty2 + returnQty > inQty){
                    if(applyQty + applyQty2 > inQty){
                        errorMsg = "配件【"+map.get("PART_OLDCODE")+"】的退货数量与已退货数量之和不能大于入库数量,请在刷新后再重新确认!";
                        break;
                    }
                }
                
                if("".equals(errorMsg)){
                    // 更新状态
                    TtPartOemReturnMainPO sMainPO = new TtPartOemReturnMainPO();
                    TtPartOemReturnMainPO mainPO1 = new TtPartOemReturnMainPO();
                    sMainPO.setReturnId(CommonUtils.parseLong(returnId)); // 退货单id
                    mainPO1.setState(Constant.PART_OEM_RETURN_STATUS_01); // 状态
                    mainPO1.setApplyBy(logonUser.getUserId()); // 提交人
                    mainPO1.setApplyDate(new Date()); // 提交时间
                    mainPO1.setUpdateBy(logonUser.getUserId()); // 更新人
                    mainPO1.setUpdateDate(new Date()); // 更新时间
                    dao.update(sMainPO, mainPO1);
                    
                    // 更新退货申请数量
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = list.get(i);
                        long inID = ((BigDecimal) map.get("IN_ID")).longValue(); // 入库id
                        long applyQty = ((BigDecimal) map.get("THIS_APPLY_QTY")).longValue(); // 本次配件退货数量
                        TtPartPoInPO poInPO = new TtPartPoInPO();
                        poInPO.setInId(inID);
                        poInPO = (TtPartPoInPO) dao.select(poInPO).get(0);
                        
                        TtPartPoInPO poInPO1 = new TtPartPoInPO();
                        poInPO1.setInId(inID);
                        
                        TtPartPoInPO poInPO2 = new TtPartPoInPO();
                        poInPO2.setApplyQty(poInPO.getApplyQty()+applyQty);
                        dao.update(poInPO1,poInPO2);
                    }
                }
                
                
            } else{
                errorMsg = "退货申请状态已更新，请重新查询结果！";
            } 
            // 返回
            if("".equals(errorMsg)){
                act.setOutData("success", "提交采购退货申请采购成功，请等待审核结果！");
                act.setOutData("curPage", curPage);
            }else {
                act.setOutData("error", curPage);
            }
        }catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购退货申请明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-2
     * @Title :
     * @Description: 作废
     */
    public void delReturnOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
            String rType = CommonUtils.checkNull(request.getParamValue("rType"));//退货类型
            
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            
            Date date = new Date();
            TtPartOemReturnMainPO mainPO = new TtPartOemReturnMainPO();
            mainPO.setReturnId(CommonUtils.parseLong(returnId));
            mainPO = (TtPartOemReturnMainPO) dao.select(mainPO).get(0);
            
            String sqlStr = " AND RD.RETURN_ID = '"+ mainPO.getReturnId() +"' ";
            List<Map<String,Object>> orderChkList = dao.orderStateCheck(sqlStr);
            int outCount = 0;
            
            if(null != orderChkList && orderChkList.size() > 0)
            {
                outCount = Integer.parseInt(orderChkList.get(0).get("OUT_COUNT").toString());
            }
            
            if(mainPO.getState().intValue()==Constant.PART_OEM_RETURN_STATUS_01.intValue() || 
                    (mainPO.getState().intValue()==Constant.PART_OEM_RETURN_STATUS_03.intValue() && outCount  == 0)){
                
                TtPartOemReturnMainPO sMainPO = new TtPartOemReturnMainPO();
                TtPartOemReturnMainPO mainPO1 = new TtPartOemReturnMainPO();
                sMainPO.setReturnId(CommonUtils.parseLong(returnId));
                mainPO1.setState(Constant.PART_OEM_RETURN_STATUS_05);
                mainPO1.setDisableBy(logonUser.getUserId());
                mainPO1.setDeleteDate(date);
                
                dao.update(sMainPO, mainPO1);
                
                //有单退货
                if(rType.equals(Constant.PART_OEM_RETURN_TYPE_01.toString())){

                    TtPartOemReturnDtlPO dtlPO = new TtPartOemReturnDtlPO();
                    dtlPO.setReturnId(CommonUtils.parseLong(returnId));
                    List<TtPartOemReturnDtlPO> list = dao.select(dtlPO);
                    //如果是有单退货就要更新入库单中的申请退货数量,并释放占用库存
                    for(TtPartOemReturnDtlPO po:list){
                        dao.updateApplyQty(po.getInId(),po.getApplyQty());
                    }
                }
                
                if(mainPO.getState().intValue()==Constant.PART_OEM_RETURN_STATUS_01.intValue())
                {
                    //1.已提交状态
                    //调用库存释放逻辑
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, CommonUtils.parseLong(returnId));
                    ins.add(1, Constant.PART_CODE_RELATION_18);
                    ins.add(2,0);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                    act.setOutData("success", "作废成功!");
                    act.setOutData("curPage", curPage);
                }
                else
                {
                    //2.已审核状态
                    //调用库存释放逻辑
                    List<Object> ins = new LinkedList<Object>();
                    ins.add(0, CommonUtils.parseLong(returnId));
                    ins.add(1, Constant.PART_CODE_RELATION_25);
                    ins.add(2,0);// 1:占用 0：释放占用
                    dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
                    act.setOutData("success", "作废成功!");
                    act.setOutData("curPage", curPage);
                }
            }else{
                act.setOutData("error", "该退货单状态已经更新,请重新查询退货单!");
            }
            
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购退货单作废失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
//    /**
//     * @param :
//     * @return :
//     * @throws : LastDate    : 2013-4-29
//     * @Title :
//     * @Description: 提交采购退货申请(无入库单)
//     */
//    public void submitReturnApplyNoCode()  {
//    	
//    	ActionContext act = ActionContext.getContext();
//    	RequestWrapper request = act.getRequest();
//    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//    	try {
//    		String[] partIds = request.getParamValues("cb");//配件id
//    		String returnCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_18);//退货单号
//    		String orgId = request.getParamValue("orgId");//制单单位id
//    		String orgCode = request.getParamValue("orgCode");//单位编码
//    		String orgName = request.getParamValue("orgName");//单位名称
//    		String reason = request.getParamValue("reason");//退货原因
//    		String whId = request.getParamValue("WH_ID");//仓库
//    		String whId2 = request.getParamValue("WH_ID2");//仓库
//    		String venderId = request.getParamValue("VENDER_ID");//供应商
//    		String returnTo = request.getParamValue("returnTo");//仓库
//
//            TtPartMakerDefinePO makerDefinePO = new TtPartMakerDefinePO();
//            if(venderId != null ){
//                makerDefinePO.setMakerId(Long.valueOf(venderId));
//                makerDefinePO = (TtPartMakerDefinePO)dao.select(makerDefinePO).get(0);
//            }
//
//    		TtPartOemReturnMainPO mainPO = new TtPartOemReturnMainPO();
//    		mainPO.setReturnId(CommonUtils.parseLong(SequenceManager.getSequence("")));
//    		mainPO.setReturnCode(returnCode);
//    		mainPO.setOrgId(CommonUtils.parseLong(orgId));
//    		mainPO.setOrgCode(orgCode);
//    		mainPO.setOrgName(orgName);
//    		mainPO.setRemark(reason);
//    		mainPO.setCreateDate(new Date());
//    		mainPO.setCreateBy(logonUser.getUserId());
//    		mainPO.setCreateOrg(CommonUtils.parseLong(orgId));
//    		mainPO.setApplyDate(new Date());
//    		mainPO.setApplyBy(logonUser.getUserId());
//    		mainPO.setState(Constant.PART_OEM_RETURN_STATUS_01);//提交之后状态就变为审核中
//    		mainPO.setReturnType(Constant.PART_OEM_RETURN_TYPE_02);
//            mainPO.setReturnTo(Integer.valueOf(returnTo));
//
//    		String error = "";
//    		//查询申请中的配件对应的入库数量以及可用数量,然后与申请退货数量比较
//    		for (int i = 0; i < partIds.length; i++) {
//                String partId = partIds[i].split(",")[0];
//                String locId = partIds[i].split(",")[1];
//
//                List<Map<String, Object>> list = dao.queryNormalQty(partId,whId,locId);
//    			Map<String, Object> map = list.get(0);
////    			long partId = ((BigDecimal) map.get("PART_ID")).longValue();
//    			long applyQty = CommonUtils.parseLong(request.getParamValue("APPLY_QTY" + partIds[i]));
////    			long locId = CommonUtils.parseLong(request.getParamValue("LOC_ID"+partId));
//    			long normalQty = ((BigDecimal) map.get("NORMAL_QTY")).longValue();
//    			if (applyQty > normalQty) {
//    				error = map.get("PART_OLDCODE")+"的申请退货数量不能大于库存数量,请在刷新后再重新确认!";
//    				break;
//    			}
//    			TtPartOemReturnDtlPO dtlPO = new TtPartOemReturnDtlPO();
//    			dtlPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
//    			dtlPO.setReturnId(mainPO.getReturnId());
//    			dtlPO.setUnit((String) map.get("UNIT"));
//    			dtlPO.setPartId(((BigDecimal) map.get("PART_ID")).longValue());
//    			dtlPO.setPartCode((String) map.get("PART_CODE"));
//    			dtlPO.setPartOldcode((String) map.get("PART_OLDCODE"));
//    			dtlPO.setPartCname((String) map.get("PART_CNAME"));
//    			dtlPO.setItemQty(((BigDecimal) map.get("ITEM_QTY")).longValue());
//    			dtlPO.setNormalQty(((BigDecimal) map.get("NORMAL_QTY")).longValue());
//    			dtlPO.setApplyQty(applyQty);
//    			dtlPO.setBuyPrice(((BigDecimal) map.get("BUY_PRICE")).doubleValue());
//    			dtlPO.setRemark(request.getParamValue("REMARK"+partId));
//    			dtlPO.setCreateDate(new Date());
//    			dtlPO.setCreateBy(logonUser.getUserId());
//    			//默认情况下入库库房就是出库库房
//    			dtlPO.setStockIn(((BigDecimal) map.get("WH_ID")).longValue());//入库库房id
//    			dtlPO.setStockOut(((BigDecimal) map.get("WH_ID")).longValue());//出库库房id
//                if(!"".equals(venderId) && null != venderId){
//                    dtlPO.setVenderId(Long.valueOf(venderId));
//                    dtlPO.setVenderCode(makerDefinePO.getMakerCode());
//                    dtlPO.setVenderName(makerDefinePO.getMakerName());
//                }
//                dtlPO.setLocId(Long.valueOf(locId));
//                if(!"".equals(whId2) && null != whId2){
//                    dtlPO.setTowhId(Long.valueOf(whId2));
//                }
//    			dao.insert(dtlPO);
//    		}
//    		if (!"".equals(error)) {
//    			act.setOutData("error", error);
//    			POContext.endTxn(false);
//    			return;
//    		}
//    		dao.insert(mainPO);
//    		
//    		//调用库存占用逻辑
//    		List ins = new LinkedList<Object>();
//    		ins.add(0, mainPO.getReturnId());
//    		ins.add(1, Constant.PART_CODE_RELATION_18);
//    		ins.add(2,1);// 1:占用 0：释放占用
//    		dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
//    		
//    		act.setOutData("success", "申请提交成功!");
//    	} catch (Exception e) {//异常方法
//    		BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "采购退货申请");
//    		logger.error(logonUser, e1);
//    		act.setException(e1);
//    	}
//    	
//    }

    
    /**
	 * 
	 * @Title      : 
	 * @Description: 打印 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-1
	 */
	public void opPrintHtml(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {

			String returnId = CommonUtils.checkNull(request
					.getParamValue("returnId"));
			String venderId = CommonUtils.checkNull(request
					.getParamValue("venderId"));

			Map<String, Object> map = dao.getPartOemReturnMainInfo(returnId);

			TtPartVenderDefinePO venderDefinePO = new TtPartVenderDefinePO();
			venderDefinePO.setVenderId(CommonUtils.parseLong(venderId));
			venderDefinePO = (TtPartVenderDefinePO) dao.select(venderDefinePO)
					.get(0);

			List<Map<String, Object>> detailList = dao
					.queryPartOemReturn(returnId);

			List<List<Map<String, Object>>> allList = new ArrayList<List<Map<String, Object>>>();

			for (int i = 0; i < detailList.size();) {
				List<Map<String, Object>> subList = detailList.subList(i,
						i + PRINT_SIZE > detailList.size() ? detailList.size()
								: i + PRINT_SIZE);
				i = i + PRINT_SIZE;
				allList.add(subList);
			}

			Map<String, Object> map1 = new HashMap<String, Object>();
			if (detailList.size() > 0) {
				map1 = detailList.get(0);
			}

			map.put("venderName", venderDefinePO.getVenderName());
			map.put("whName", map1.get("WH_NAME"));
			map.put("curDate", DateUtil.getDateStr(new Date(), 1));

			int allQty = 0;
			double amount = 0;
			double famount = 0;
			for (Map<String, Object> m : detailList) {
				allQty += ((BigDecimal) m.get("RETURN_QTY")).intValue();
				amount = Arith.add(amount, ((BigDecimal) m.get("BUY_AMOUNT"))
						.doubleValue());
			}

			BigDecimal b = new BigDecimal(amount);
			famount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			DecimalFormat df = new DecimalFormat("#.00");
			String samonut = df.format(famount);
			map.put("allQty", allQty);
			map.put("amount", samonut);
			dataMap.put("mainMap", map);
			dataMap.put("detailList", detailList);
			act.setOutData("allList", allList);
			act.setOutData("dataMap", dataMap);
			act.setForword(PART_OEMRETURN_APPLY_PRINT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "采购退货单打印错误,请联系管理员!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
}
