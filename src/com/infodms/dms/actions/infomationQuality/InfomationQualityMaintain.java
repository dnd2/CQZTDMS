package com.infodms.dms.actions.infomationQuality;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrInformationqualityPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class InfomationQualityMaintain {
	private Logger logger = Logger.getLogger(InfomationQualityMaintain.class);
	//private final InfomationQualityMaintainDAO dao = InfomationQualityMaintainDAO.getInstance();
	private final ClaimAuditingDao daom = ClaimAuditingDao.getInstance();
	private final String INFO_URL = "/jsp/infomationQuality/infomationQualityMaintain.jsp";// 主页面（查询）
	private final String INFO_ADD_URL = "/jsp/infomationQuality/infomationQualityAdd.jsp";// 增加页面
	private final String INFO_MODIFY_URL = "/jsp/infomationQuality/infomationQualityModify.jsp";// 修改页面
	private final String SHOW_VIN_URL = "/jsp/claim/dealerClaimMng/showVin.jsp";// VIN选择
	private final String MAIN_TIME_URL = "/jsp/claim/dealerClaimMng/showMainTime.jsp";// 工时选择
	private final String TIME_URL = "/jsp/claim/dealerClaimMng/showTime.jsp";// 工时选择
	private final String PARTCODE_URL = "/jsp/claim/dealerClaimMng/showPartCode.jsp";// 配件选择
	private final String MAIN_PARTCODE_URL = "/jsp/claim/dealerClaimMng/showMainPartCode.jsp";// 配件选择
	private final String DOWN_PARTCODE_URL = "/jsp/claim/dealerClaimMng/showDownPartCode.jsp";// 配件选择
	private final String PRINT_URL = "/jsp/infomationQuality/infomationQualityPrint.jsp" ; //质量信息跟踪处理卡打印页面
	
	/**
	 * 
	* @Title: infoForward 
	* @Description: TODO(质量信息主页跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void infoForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {

			act.setForword(INFO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: infoAddForward 
	* @Description: TODO(质量信息新增跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void infoAddForward(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {

			act.setForword(INFO_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: infoMofidyForward 
	* @Description: TODO(质量信息修改跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void infoMofidyForward(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {

			act.setForword(INFO_MODIFY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: infoDetailForward 
	* @Description: TODO(质量信息明细跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void infoDetailForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {

			act.setForword(INFO_MODIFY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	* @Title: infoQuery 
	* @Description: TODO(质量信息查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void infoQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		

	}
	/**
	 * 
	* @Title: infoInsert 
	* @Description: TODO(质量信息新增) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void infoInsert() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String claimId = request.getParamValue("CLAIM_ID"); 
			String dealerCode = request.getParamValue("DEALER_CODE");
			String complainSort = request.getParamValue("COMPLAIN_SORT");
			String dealerName = request.getParamValue("DEALER_NAME");
			String tel = request.getParamValue("TEL");
			String infomationSort = request.getParamValue("INFOMATION_SORT");
			String currentDate = request.getParamValue("CURRENT_DATE");
			String vin = request.getParamValue("VIN");
			String modelCode = request.getParamValue("MODEL_CODE");
			String modelName = request.getParamValue("MODEL_NAME");
			String engineNo = request.getParamValue("ENGINE_NO");
			String factoryDate = request.getParamValue("FACTORY_DATE");
			String purchasedDate = request.getParamValue("PURCHASED_DATE");
			String mileage = request.getParamValue("MILEAGE");
			String customerName = request.getParamValue("CUSTOMER_NAME");
			String phone = request.getParamValue("PHONE");
			String questionPart = request.getParamValue("QUESTION_PART");
			String faultNature = request.getParamValue("FAULT_NATURE");
			String gearboxNature = request.getParamValue("GEARBOX_NATURE");
			String vhclUse = request.getParamValue("VHCL_USE");
			String speedCase = request.getParamValue("SPEED_CASE");
			String roadCase = request.getParamValue("ROAD_CASE");
			String loadingCase = request.getParamValue("LOADING_CASE");
			String adptCase = request.getParamValue("ADPT_CASE");
			String produceFactory = request.getParamValue("PRODUCE_FACTORY");
			String completeEngineno = request.getParamValue("COMPLETE_ENGINENO");
			String faultOpinion = request.getParamValue("FAULT_OPINION");
			String damageCase = request.getParamValue("DAMAGE_CASE");
			String damagePrice = request.getParamValue("DAMAGE_PRICE");
			String processOpinion = request.getParamValue("PROCESS_OPINION");
			String result = request.getParamValue("RESULT");
			String booker = request.getParamValue("BOOKER");
			String backUnit = request.getParamValue("BACK_UNIT");
			String backDate = request.getParamValue("BACK_DATE");
			String caseWork = request.getParamValue("CASE_WORK");
			String questionCase = request.getParamValue("QUESTION_CASE");
			String conntion = request.getParamValue("CONNTION");
			String dutyUnit = request.getParamValue("DUTY_UNIT");
			String tempStep = request.getParamValue("TEMP_STEP");
			String forveStep = request.getParamValue("FORVE_STEP");
			String suggest = request.getParamValue("SUGGEST");
			String serviceOpinion = request.getParamValue("SERVICE_OPINION");
			
			TtAsWrInformationqualityPO taip = new TtAsWrInformationqualityPO();
			taip.setId(Utility.getLong(SequenceManager.getSequence("")));
			taip.setClaimId(Utility.getLong(claimId));
			taip.setDealerCode(dealerCode);
			taip.setDealerName(dealerName);
			taip.setComplainSort(Utility.getInt(complainSort));
			taip.setTel(tel);
			taip.setInformationSort(Utility.getInt(infomationSort));
			taip.setCurrentDate(Utility.getDate(currentDate, 1));
			taip.setVin(vin);
			taip.setModelCode(modelCode);
			taip.setModelName(modelName);
			taip.setEngineNo(engineNo);
			taip.setFactoryDate(Utility.getDate(factoryDate, 1));
			taip.setPurchasedDate(Utility.getDate(purchasedDate, 1));
			taip.setMileage(Utility.getDouble(mileage));
			taip.setCustomName(customerName);
			taip.setPhone(phone);
			taip.setQuestionPart(Utility.getInt(questionPart));
			taip.setFaultNatrue(Utility.getInt(faultNature));
			taip.setGearboxNatrue(Utility.getInt(gearboxNature));
			taip.setVhclUse(vhclUse);
			taip.setSpeedCase(speedCase);
			taip.setRoadCase(roadCase);
			taip.setLoadingCase(loadingCase);
			taip.setAdaptCase(adptCase);
			taip.setProduceFactroy(produceFactory);
			taip.setCompleteEngineno(completeEngineno);
			taip.setFaultOpinion(faultOpinion);
			taip.setDamageCase(damageCase);
			taip.setDamagePrice(Utility.getDouble(damagePrice));
			taip.setProcessOpinion(processOpinion);
			taip.setResult(result);
			taip.setBooker(booker);
			taip.setBackUnit(backUnit);
			taip.setBackDate(Utility.getDate(backDate, 1));
			taip.setCaseWork(caseWork);
			taip.setQuestionCase(questionCase);
			taip.setConntion(conntion);
			taip.setDutyUnit(dutyUnit);
			taip.setTempStep(tempStep);
			taip.setForveStep(forveStep);
			taip.setSuggest(suggest);
			taip.setServiceOpinion(serviceOpinion);
			
			daom.insert(taip);
			act.setOutData("success", true); //插入成功，关闭信息页
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			act.setOutData("success", false);
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: infoUpdate 
	* @Description: TODO(质量信息修改) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void infoUpdate() {
		
	}
	
	/*
	 * 打印页面初始化
	 */
	public void printUrlInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper req = act.getRequest() ;
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long id =Long.parseLong(req.getParamValue("id"));
			TtAsWrInformationqualityPO po = new TtAsWrInformationqualityPO();
			po.setId(id);
			List<TtAsWrInformationqualityPO> list = daom.select(po);
			if(list.size()>0)
				act.setOutData("info",list.get(0));
			act.setForword(PRINT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息打印");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
