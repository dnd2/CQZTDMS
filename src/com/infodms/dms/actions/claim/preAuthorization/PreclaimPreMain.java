/**   
* @Title: PreclaimPreMain.java 
* @Package com.infodms.dms.actions.claim.preAuthorization 
* @Description: TODO(索赔预授权工单申请Action) 
* @author wangjinbao   
* @date 2010-6-21 下午02:53:30 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.preAuthorization;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.ClaimCommonAction;
import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ConditionBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.preAuthorization.PreclaimPreDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmVehicleExtPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;
import com.infodms.dms.po.TtAsWrForeapprovalitemPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: PreclaimPreMain 
 * @Description: TODO(索赔预授权工单申请)经销商端 
 * @author wangjinbao 
 * @date 2010-6-21 下午02:53:30 
 *  
 */
public class PreclaimPreMain {
	private Logger logger = Logger.getLogger(PreclaimPreMain.class);
	private final PreclaimPreDao dao = PreclaimPreDao.getInstance();
	private final String PRE_CLAIM_URL = "/jsp/claim/preAuthorization/preclaimPreIndex.jsp";//主页面（查询）
	private final String PRE_CLAIM_SAVE_URL = "/jsp/claim/preAuthorization/preclaimSave.jsp";//保存页面
	private final String PRE_CLAIM_UPDATE_URL = "/jsp/claim/preAuthorization/preclaimModify.jsp";//修改页面
	private final String PRE_CLAIM_DETAIL_URL = "/jsp/claim/preAuthorization/preclaimDetail.jsp";//明细页面
	private final String SHOW_VIN_URL = "/jsp/claim/preAuthorization/showVin.jsp";// VIN选择
	private final String PRE_CLAIM_QUERY_URL ="/jsp/claim/preAuthorization/preclaimQuery.jsp";//项目选择页面
	private final ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
	/**
	 * 
	* @Title: preclaimPreInit 
	* @Description: TODO(索赔预授权工单申请初始化) 
	* @param    
	* @return void  
	* @throws 
	 */
	public void preclaimPreInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PRE_CLAIM_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: preclaimAddInit 
	* @Description: TODO(索赔预授权工单保存初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void preclaimAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Map hm = null;
		Map userHm = null;
		try {
			hm = claimCommon.getDealerMap(logonUser.getDealerId());
			userHm = claimCommon.getUserMap(logonUser.getUserId());
//			act.setOutData("LOGONUSER", logonUser);//存登录人信息
			act.setOutData("USERHM", userHm);//存当前登录人的用户信息
			act.setOutData("DEALERHM", hm);//存入经销商信息
			act.setForword(PRE_CLAIM_SAVE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: getDetailByVinForward 
	* @Description: TODO(选择VIN带出车辆信息跳转) 
	* @param    
	* @return void  
	* @throws
	 */
	public void getDetailByVinForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(SHOW_VIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔预授权工单申请");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	 * @Title: getDetailByVin
	 * @Description: TODO(根据VIN和车主姓名查询车辆信息表)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getDetailByVin() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询	
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 10;
			String vin = request.getParamValue("VIN_PARAM");
			String customer = request.getParamValue("CUSTOMER");
			String vinParent = request.getParamValue("vinParent");
			//modify at 2010-07-19 start
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
			String dealerId = logonUser.getDealerId();  //经销商ID
			//modify end			
			/*
			 * if (dealerId != null) { con.append(" and v.DEALER_ID = " +
			 * dealerId + " "); }
			 */
			map.put("vin", vin);
			map.put("customer", customer);
			map.put("vinParent", vinParent);
			PageResult<TmVehicleExtPO> ps = dao.getVin(companyId,dealerId,map,
					pageSize, curPage);
			act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔预授权工单申请");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	
	/**
	 * 
	* @Title: preclaimAdd 
	* @Description: TODO(索赔预授权工单保存/提报) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void preclaimAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//判断是：提报（u）,保存(s)
			String submitType = request.getParamValue("submitType");
			String roNo = request.getParamValue("RO_NO");//维修工单号
			String keepBegDate = request.getParamValue("KEEP_BEG_DATE");//保修开始日期
			String inFactoryDate = request.getParamValue("IN_FACTORY_DATE");//进厂日期
			String inMileage = request.getParamValue("IN_MILEAGE");//进厂里程数
			String destClerk = request.getParamValue("DEST_CLERK");//接待员
			String approvalDate = request.getParamValue("APPROVAL_DATE");//申请日期
			String deliverer = request.getParamValue("APPROVAL_PERSON");//申请人
			String delivererMobile = request.getParamValue("APPROVAL_PHONE");//手机
			String vin = request.getParamValue("VIN");//VIN
			
			String[] itemidIds = request.getParamValues("ITEMID_ID");//项目id
			String[] itemidTypes = request.getParamValues("ITEMID_TYPE");//项目类型（tc_code）id
			String[] itemidCodes= request.getParamValues("ITEMID_CODE");//项目代码
			String[] itemidNames = request.getParamValues("ITEMID_NAME");//项目名称
			String[] dealerRemarks = request.getParamValues("DEALER_REMARK");//申请说明
			String dealerCode = request.getParamValue("dealerCode");//经销商代码
			
			String approvalType = request.getParamValue("APPROVAL_TYPE");//申请类型
			
			//modify at 2010-07-16
			String brandCode = request.getParamValue("BRAND_CODE");//品牌
			String seriesCode = request.getParamValue("SERIES_CODE");//车系
			String modelCode = request.getParamValue("MODEL_CODE");//车型
			String licenseNo = request.getParamValue("LICENSE_NO");//车牌号
			String yieldly = request.getParamValue("YIELDLY");//产地
			String engineNo = request.getParamValue("ENGINE_NO_H");//发动机号
			
			//modify at 2010-07-19 start
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
			//modify end
			//modify at 2010-08-30 
			String outDate = request.getParamValue("OUT_DATE");//外出日期
			String outPerson = request.getParamValue("OUT_PERSON");//外出人
			String outFee = request.getParamValue("OUT_FEE");//外出费用
			
			//预授权申请单
			TtAsWrForeapprovalPO addpo = new TtAsWrForeapprovalPO();
			String foNo = SequenceManager.getSequence("FO");
			Long id = Utility.getLong(SequenceManager.getSequence(""));//主表ID
			addpo.setFoNo(foNo);//预授权申请单号
			addpo.setRoNo(roNo);
			addpo.setId(id);//预申请ID
			addpo.setDealerId(Utility.getLong(logonUser.getDealerId()));//经销商ID
			addpo.setVin(vin);
			addpo.setInMileage(Utility.getDouble(inMileage));
			
			addpo.setDeliverer(deliverer);
			addpo.setDelivererMobile(delivererMobile);
			//modify at 2010-07-19
			addpo.setApprovalPerson(deliverer);//申请人
			addpo.setApprovalPhone(delivererMobile);//申请人的电话
			addpo.setOemCompanyId(companyId);//公司ID
			//modify end
			//modify at 2010-08-30
			addpo.setOutPerson(outPerson);//外出人
			addpo.setOutDate(Utility.getDate(outDate, 1));//外出时间
			addpo.setOutFee(Utility.getDouble(outFee));//外出费用
			// modify end
			addpo.setApprovalDate(Utility.getDate(approvalDate, 1));
			addpo.setKeepBegDate(Utility.getDate(keepBegDate, 1));
			addpo.setDestClerk(destClerk);
			addpo.setInFactoryDate(Utility.getDate(inFactoryDate, 1));
			addpo.setBrandCode(brandCode);
			addpo.setSeriesCode(seriesCode);
			addpo.setModelCode(modelCode);
			addpo.setLicenseNo(licenseNo);
			addpo.setYieldly(yieldly);
			addpo.setEngineNo(engineNo);
			if(Utility.testString(submitType) && "u".equals(submitType)){
				addpo.setReportStatus(Constant.PRE_AUTH_STATUS_02);//已上报
			}else{
				addpo.setReportStatus(Constant.PRE_AUTH_STATUS_01);//未上报
			}
			addpo.setApprovalType(Utility.getInt(approvalType));
			addpo.setCreateBy(logonUser.getUserId());
			addpo.setCreateDate(new Date());
			dao.insert(addpo);
			
			
			//预授权申请项目
			int len = itemidIds.length;
			for (int i = 0; i < len; i++) {
				//索赔授权规则明细
				TtAsWrForeapprovalitemPO additempo = new TtAsWrForeapprovalitemPO();
				additempo.setId(Long.parseLong(SequenceManager.getSequence("")));//申请项目子表ID
				additempo.setFid(id);//主表ID
				//预授权工单号：
				additempo.setRoNo(roNo);//预授权申请维修工单单号
				additempo.setDealerCode(dealerCode);//索赔代码
				additempo.setItemId(Utility.getLong(itemidIds[i]));//项目ID
				additempo.setItemType(Utility.getInt(itemidTypes[i]));//项目类别
				additempo.setItemCode(itemidCodes[i]);//项目代码
				additempo.setItemDesc(itemidNames[i]);//项目说明
				additempo.setDealerRemark(dealerRemarks[i]);//申请说明
				additempo.setCreateBy(logonUser.getUserId());
				additempo.setCreateDate(new Date());
				additempo.setApplier(deliverer);//申请人
				additempo.setApplyDate(Utility.getDate(approvalDate, 1));//申请日期				
				if(Utility.testString(submitType) && "u".equals(submitType)){
					additempo.setStatus(Constant.PRECLAIM_AUDIT_03);//状态：已接收
					additempo.setReporter(logonUser.getName());//提报人
					additempo.setReportDate(new Date());//提报日期
				}
				//往索赔授权规则明细表插入一条记录
				dao.insert(additempo);
			}
			// 附件功能
			String ywzj = id.toString();
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
//			hm = claimCommon.getDealerMap(logonUser.getDealerId());
//			act.setOutData("LOGONUSER", logonUser);//存登录人信息
//			act.setOutData("DEALERHM", hm);//存入经销商信息
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}	
	/**
	 * 
	* @Title: preclaimPreQuery 
	* @Description: TODO(索赔预授权工单申请查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void preclaimPreQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				String beginTime = request.getParamValue("beginTime");//申请日期起
				String endTime = request.getParamValue("endTime");//申请日期止
				String orderId = request.getParamValue("ORDER_ID");//预授权单号
				
				String roNo = request.getParamValue("RO_NO");//工单号
				String vin = request.getParamValue("VIN");
				String preAuthStatus = request.getParamValue("PRE_AUTH_STATUS");//提报状态
				//modify at 2010-07-19 start
				Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
				String dealerId = logonUser.getDealerId();  //经销商ID
				//modify end
				ConditionBean bean = new ConditionBean();//查询条件bean
				bean.setConOne(beginTime);
				bean.setConTwo(endTime);
				bean.setConThree(orderId);
				bean.setConFour(vin);
				bean.setConFive(preAuthStatus);
				bean.setConSix(roNo);
				PageResult<Map<String, Object>> ps = dao.preclaimQuery(companyId,dealerId,Constant.PAGE_SIZE, curPage,bean);
				act.setOutData("ps", ps);
			}
		} 
		catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} 
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: preclaimPreAddInit 
	* @Description: TODO(项目选择初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void preclaimPreAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String wrgroupid = null;
		try {
			RequestWrapper request = act.getRequest();
			String modelId = request.getParamValue("MODEL_ID");//车型ID
			String ids = request.getParamValue("ids");//删选的id
			HashMap wrgroup = dao.getWrgroupIdByModelId(modelId);
			ConditionBean bean = new ConditionBean();//查询条件bean
			if(wrgroup != null && !wrgroup.isEmpty()){
				wrgroupid = wrgroup.get("WRGROUP_ID").toString();
			}
			act.setOutData("wrgroupid", wrgroupid);//车型组id
			act.setOutData("MODEL_ID", modelId);//车型id
			act.setOutData("ids", ids);//车型组id
			act.setOutData("bean", bean);//查询条件bean
			act.setForword(PRE_CLAIM_QUERY_URL);
		} 
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: preclaimItemQuery 
	* @Description: TODO(项目选择页面方法) 
	* @param    
	* @return void  
	* @throws
	 */
	public void preclaimItemQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询	
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				String wrgroupId = request.getParamValue("wrgroupid");//车型组ID
				String modelId = request.getParamValue("MODEL_ID");//车型ID
				String ids = request.getParamValue("ids");//删选的id
				String itemType = request.getParamValue("PRE_AUTH_ITEM");//项目类型
				String itemCode = request.getParamValue("ITEMCODE");//项目代码
				String itemName = request.getParamValue("ITEMNAME");//项目名称
				
				//modify at 2010-07-19 start
				Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
				//modify end			
				ConditionBean bean = new ConditionBean();//查询条件bean
				bean.setConOne(itemType);
				bean.setConTwo(itemCode);
				bean.setConThree(itemName);
				bean.setConFour(ids);
				
//				List list = dao.getItems(companyId,bean, wrgroupId,modelId);//项目列表
				PageResult<Map<String, Object>> ps = dao.getItems(companyId,wrgroupId,Constant.PAGE_SIZE, curPage,bean);
				act.setOutData("wrgroupid", wrgroupId);//车型组id
				act.setOutData("MODEL_ID", modelId);//车型id
				act.setOutData("ids", ids);//删选的id
//				act.setOutData("ADDLIST", list);//项目列表结果
				act.setOutData("bean", bean);
//				act.setForword(PRE_CLAIM_QUERY_URL);
				act.setOutData("ps", ps);
				}			
		} 
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: preclaimUpdateInit 
	* @Description: TODO(修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void preclaimUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Map hm = null;
		try {
			RequestWrapper request = act.getRequest();
			//modify at 2010-07-19 start
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
//			String dealerId = logonUser.getDealerId();  //经销商ID
			//modify end			
			hm = claimCommon.getDealerMap(logonUser.getDealerId());
			act.setOutData("LOGONUSER", logonUser);//存登录人信息
			act.setOutData("DEALERHM", hm);//存入经销商信息
//			String wid = request.getParamValue("WID");//对应车型组id
			String id = request.getParamValue("ID");//对应预申请ID
			
			//根据主键查找预授权
			HashMap hashmap = dao.getPreclaimById(companyId,id);
			List itemList = dao.getPreclaimItemById(id);
			//取附件信息：
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
//			HashMap hm = dao.ruleQueryById(ruleElement);
//			act.setOutData("SELMAP", hm);//对应的授权角色map
//			act.setOutData("wid", wid);//车型组id
			act.setOutData("FOREAPPROVAL_HASHMAP", hashmap);//预授权申请主表
			act.setOutData("FOREAPPROVALITEM_LIST", itemList);//预授权申请子表
			act.setOutData("ID", id);//主键ID
			act.setOutData("attachLs", attachLs);//附件列表

			act.setForword(PRE_CLAIM_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: preclaimUpdate 
	* @Description: TODO(索赔预授权工单申请修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void preclaimUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//判断是：提报（u）,保存(s)
			String submitType = request.getParamValue("submitType");
			String id = request.getParamValue("ID");//主键ID
			
			String roNo = request.getParamValue("RO_NO");//维修工单号
			String keepBegDate = request.getParamValue("KEEP_BEG_DATE");//保修开始日期
			String inFactoryDate = request.getParamValue("IN_FACTORY_DATE");//进厂日期
			String inMileage = request.getParamValue("IN_MILEAGE");//进厂里程数
			String destClerk = request.getParamValue("DEST_CLERK");//接待员
			String approvalDate = request.getParamValue("APPROVAL_DATE");//申请日期
			String deliverer = request.getParamValue("APPROVAL_PERSON");//申请人
			String delivererMobile = request.getParamValue("APPROVAL_PHONE");//手机
			String vin = request.getParamValue("VIN");//VIN
			
			String[] itemidIds = request.getParamValues("ITEMID_ID");//项目id
			String[] itemidTypes = request.getParamValues("ITEMID_TYPE");//项目类型（tc_code）id
			String[] itemidCodes= request.getParamValues("ITEMID_CODE");//项目代码
			String[] itemidNames = request.getParamValues("ITEMID_NAME");//项目名称
			String[] dealerRemarks = request.getParamValues("DEALER_REMARK");//申请说明
			String dealerCode = request.getParamValue("dealerCode");//经销商代码
			
			String approvalType = request.getParamValue("APPROVAL_TYPE");//申请类型
			
			//modify at 2010-07-16
			String brandCode = request.getParamValue("BRAND_CODE");//品牌
			String seriesCode = request.getParamValue("SERIES_CODE");//车系
			String modelCode = request.getParamValue("MODEL_CODE");//车型
			String licenseNo = request.getParamValue("LICENSE_NO");//车牌号
			String yieldly = request.getParamValue("YIELDLY");//产地
			String engineNo = request.getParamValue("ENGINE_NO_H");//发动机号
			//modify at 2010-08-30 
			String outDate = request.getParamValue("OUT_DATE");//外出日期
			String outPerson = request.getParamValue("OUT_PERSON");//外出人
			String outFee = request.getParamValue("OUT_FEE");//外出费用
			//修改
			TtAsWrForeapprovalPO selpo = new TtAsWrForeapprovalPO();
			selpo.setId(Utility.getLong(id));
			TtAsWrForeapprovalPO updatepo = new TtAsWrForeapprovalPO();
			updatepo.setApprovalType(Utility.getInt(approvalType));//申请类型
			updatepo.setRoNo(roNo);
			updatepo.setVin(vin);
			updatepo.setInMileage(Utility.getDouble(inMileage));
			updatepo.setDeliverer(deliverer);
			updatepo.setDelivererMobile(delivererMobile);
			//modify at 2010-07-19
			updatepo.setApprovalPerson(deliverer);//申请人
			updatepo.setApprovalPhone(delivererMobile);//申请人的电话
			//modify at 2010-08-30
			updatepo.setOutPerson(outPerson);//外出人
			updatepo.setOutDate(Utility.getDate(outDate, 1));//外出时间
			updatepo.setOutFee(Utility.getDouble(outFee));//外出费用
			// modify end
			updatepo.setApprovalDate(Utility.getDate(approvalDate, 1));
			updatepo.setKeepBegDate(Utility.getDate(keepBegDate, 1));
			updatepo.setDestClerk(destClerk);
			updatepo.setInFactoryDate(Utility.getDate(inFactoryDate, 1));
			updatepo.setBrandCode(brandCode);
			updatepo.setSeriesCode(seriesCode);
			updatepo.setModelCode(modelCode);
			updatepo.setLicenseNo(licenseNo);
			updatepo.setYieldly(yieldly);
			updatepo.setEngineNo(engineNo);
			//预授权申请单修改：当且仅当提交状态是：“提报”u
			if(Utility.testString(submitType) && "u".equals(submitType)){
				updatepo.setReportStatus(Constant.PRE_AUTH_STATUS_02);//已上报
			}			
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			dao.update(selpo, updatepo);
			
			
			
			
			//预授权申请项目
			//先删除再添加：
			TtAsWrForeapprovalitemPO delpo = new TtAsWrForeapprovalitemPO();
			delpo.setFid(Utility.getLong(id));
			dao.delete(delpo);
			//添加
			int len = itemidCodes.length;
			for (int i = 0; i < len; i++) {
				//索赔授权规则明细
				TtAsWrForeapprovalitemPO additempo = new TtAsWrForeapprovalitemPO();
				additempo.setId(Long.parseLong(SequenceManager.getSequence("")));//申请项目子表ID
				additempo.setFid(Utility.getLong(id));//主表ID
				//预授权工单号：
				additempo.setRoNo(roNo);//预授权申请维修工单单号
				additempo.setDealerCode(dealerCode);//索赔代码
				additempo.setItemId(Utility.getLong(itemidIds[i]));//项目ID
				additempo.setItemType(Utility.getInt(itemidTypes[i]));//项目类别
				additempo.setItemCode(itemidCodes[i]);//项目代码
				additempo.setItemDesc(itemidNames[i]);//项目说明
				additempo.setDealerRemark(dealerRemarks[i]);//申请说明
				additempo.setCreateBy(logonUser.getUserId());
				additempo.setCreateDate(new Date());
				additempo.setApplier(deliverer);//申请人
				additempo.setApplyDate(Utility.getDate(approvalDate, 1));//申请日期				
				if(Utility.testString(submitType) && "u".equals(submitType)){
					additempo.setStatus(Constant.PRECLAIM_AUDIT_03);//状态：已接收
					additempo.setReporter(logonUser.getName());//提报人
					additempo.setReportDate(new Date());//提报日期
				}
				//往索赔授权规则明细表插入一条记录
				dao.insert(additempo);
			}
			//附近功能：
			String ywzj = id.toString();
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);			
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	@SuppressWarnings("unchecked")
	public void preclaimCommit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String id = request.getParamValue("ID");//要上报的主表ID
			TtAsWrForeapprovalPO selpo = new TtAsWrForeapprovalPO();
			selpo.setId(Utility.getLong(id));
			TtAsWrForeapprovalPO updatepo = new TtAsWrForeapprovalPO();
			updatepo.setReportStatus(Constant.PRE_AUTH_STATUS_02);//已上报
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			dao.update(selpo, updatepo);
			//子表信息：
			List itemList = dao.getPreclaimItemById(id);
			if(itemList != null && itemList.size() > 0){
				TtAsWrForeapprovalitemPO selitempo = new TtAsWrForeapprovalitemPO();
				selitempo.setFid(Utility.getLong(id));
				TtAsWrForeapprovalitemPO updateitempo = new TtAsWrForeapprovalitemPO();
				updateitempo.setStatus(Constant.PRECLAIM_AUDIT_03);//状态：已接收
				updateitempo.setReporter(logonUser.getName());//提报人
				updateitempo.setReportDate(new Date());//提报日期
				dao.update(selitempo, updateitempo);
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	@SuppressWarnings("unchecked")
	public void preclaimDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Map hm = null;
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");//要上报的主表ID
			//modify at 2010-07-19 start
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
//			String dealerId = logonUser.getDealerId();  //经销商ID
			//modify end			
			//根据主键查找预授权
			HashMap hashmap = dao.getPreclaimById(companyId,id);
			List itemList = dao.getPreclaimItemById(id);
			//取附件信息：
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			act.setOutData("attachLs", attachLs);//附件列表			
			act.setOutData("FOREAPPROVAL_HASHMAP", hashmap);//预授权申请主表
			act.setOutData("FOREAPPROVALITEM_LIST", itemList);//预授权申请子表	
			hm = claimCommon.getDealerMap(logonUser.getDealerId());
			act.setOutData("DEALERHM", hm);//存入经销商信息
			act.setForword(PRE_CLAIM_DETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权工单申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	* @Title: getItemsIdByCodes 
	* @Description: TODO(动态添加预授权的项目信息（接口所用）) 
	* @param    
	* @return void  
	* @throws
	 */
	public void getItemsIdByCodes() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String[] codes = CommonUtils.checkNull(request.getParamValue("codes")).split(",");
			String[] types = CommonUtils.checkNull(request.getParamValue("types")).split(",");
			List<String> values = setValue(codes,types);
			act.setOutData("str", values.get(0));
			act.setOutData("itemtypedesc", values.get(1));
			act.setOutData("itemtypeid", values.get(2));
			act.setOutData("itemcode", values.get(3));
			act.setOutData("itemname", values.get(4));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	* @Title: setValue 
	* @Description: TODO(具体查询预授权项目) 接口用
	* @param @param codes
	* @param @param types
	* @param @return   
	* @return List<String>  
	* @throws
	 */
	private List<String> setValue(String[] codes,String[] types) {
		List str = new ArrayList();
		List<String> itemid = new ArrayList<String>();//项目ID
		List<String> itemtypedesc= new ArrayList<String>();//项目类型描述
		List<String> itemtypeid = new ArrayList<String>();//项目类型ID，tc_code_id
		List<String> itemcode = new ArrayList<String>();
		List<String> itemname = new ArrayList<String>();
		if(codes != null && codes.length > 0){
			for(int i=0;i<codes.length;i++){
				try {
					Map map = dao.getPreItems(codes[i], types[i]);
					itemid.add(map.get("ID").toString());
					itemtypedesc.add(map.get("CODE_DESC").toString());
					itemtypeid.add(map.get("CODE_ID").toString());
					itemcode.add(codes[i]);
					itemname.add(map.get("NAME").toString());
				} catch (Exception e) {
					logger.error(codes[i] + "  " + types[i] + " not found.", e);
				}
			}
		}
		str.add(0,itemid);
		str.add(1,itemtypedesc);
		str.add(2,itemtypeid);
		str.add(3,itemcode);
		str.add(4,itemname);
		return str;
	}
	

}
