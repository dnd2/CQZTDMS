package com.infodms.dms.actions.claim.dealerClaimMng;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.repairOrder.WrRuleUtil;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ActivityPartDetailBean;
import com.infodms.dms.bean.ClaimListBean;
import com.infodms.dms.bean.NewPartBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.bean.TtAsWrApplicationExtBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.exception.UserException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessChngCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDownParameterPO;
import com.infodms.dms.po.TmPtPartBaseExtPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TmVehicleExtendPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityProjectPO;
import com.infodms.dms.po.TtAsActivityRelationPO;
import com.infodms.dms.po.TtAsRepairOrderExtPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrCompensationAppPO;
import com.infodms.dms.po.TtAsWrFeeRulePO;
import com.infodms.dms.po.TtAsWrFeeWarrantyPO;
import com.infodms.dms.po.TtAsWrGamefeePO;
import com.infodms.dms.po.TtAsWrInformationqualityExtPO;
import com.infodms.dms.po.TtAsWrInformationqualityPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrModelItemPO;
import com.infodms.dms.po.TtAsWrNetitemExtPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrQamaintainPO;
import com.infodms.dms.po.TtAsWrVinPartRepairTimesvalPO;
import com.infodms.dms.po.TtAsWrVinRepairDaysPO;
import com.infodms.dms.po.TtAsWrVinRulePO;
import com.infodms.dms.po.TtAsWrWrlabinfoExtPO;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.po.TtClaimAccessoryDtlPO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.po.VwMaterialGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.vo.WarrantyPartVO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName: ClaimBillMaintainMain
 * @Description: TODO(索赔单维护)
 * @author wangchao
 * @date May 31, 2010 4:17:16 PM
 * 
 */
@SuppressWarnings("unchecked")
public class ClaimBillMaintainMain extends BaseAction {
	private Logger logger = Logger.getLogger(ClaimBillMaintainMain.class);
	private final ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
	private final ClaimAuditingDao daom = ClaimAuditingDao.getInstance();
	private final String CLAIM_BILL_URL = "/jsp/claim/dealerClaimMng/claimBillMaintain.jsp";// 主页面（查询）
	private final String CLAIM_BILL_ADD_URL = "/jsp/claim/dealerClaimMng/2claimBillMaintainAdd.jsp";// 增加页面
	private final String CLAIM_BILL_ADD_URL2 = "/jsp/claim/dealerClaimMng/2claimBillMaintainAdd2.jsp";// 增加页面
	private final String CLAIM_BILL_MODIFY_URL = "/jsp/claim/dealerClaimMng/2claimBillMaintainModify.jsp";// 修改页面(保养+PDI)
	private final String CLAIM_BILL_MODIFY_URL2 = "/jsp/claim/dealerClaimMng/2claimBillMaintainModify2.jsp";// 修改页面
	private final String SHOW_VIN_URL = "/jsp/claim/dealerClaimMng/showVin.jsp";// VIN选择
	private final String MAIN_TIME_URL = "/jsp/claim/dealerClaimMng/showMainTime.jsp";// 工时选择
	private final String TIME_URL = "/jsp/claim/dealerClaimMng/showTime.jsp";// 工时选择
	private final String PARTCODE_URL = "/jsp/claim/dealerClaimMng/showPartCode.jsp";// 配件选择
	private final String MAIN_PARTCODE_URL = "/jsp/claim/dealerClaimMng/showMainPartCode.jsp";// 配件选择
	private final String MAIN_PARTCODE_URL1 = "/jsp/claim/dealerClaimMng/showMainPartCode1.jsp";
	private final String DOWN_PARTCODE_URL = "/jsp/claim/dealerClaimMng/showDownPartCode.jsp";// 配件选择
	private final String RO_URL = "/jsp/claim/dealerClaimMng/showRo.jsp";
	private final String RO_DETAIL_URL = "/jsp/claim/dealerClaimMng/showRoAll.jsp";
	private final String RO_ITEM_LABOUR = "/jsp/claim/dealerClaimMng/showRoItemWorkHours.jsp";
	private final String RO_ITEM_PART = "/jsp/claim/dealerClaimMng/showRoItemParts.jsp";
	private final String RO_ITEM_OTHER = "/jsp/claim/dealerClaimMng/showRoItemOthers.jsp";
	private final String SHOW_CODE_URL = "/jsp/claim/dealerClaimMng/showCode.jsp"; 
	private final String CLAIM_REPORT_URL = "/jsp/claim/dealerClaimMng/claimReport.jsp" ;//配件三包判定页面;
	/*****zhumingwei 2011-02-12******/
	private final String SHOW_CODE_URL111 = "/jsp/claim/dealerClaimMng/showCode111.jsp"; 
	/*****zhumingwei 2011-02-12******/
	
	private final String SHOW_SUPPLIER_URL = "/jsp/claim/dealerClaimMng/showSupplier.jsp";
	
	private final String INFO_ADD_URL = "/jsp/infomationQuality/infomationQualityAdd.jsp";// 增加页面
	private final String INFO_DETAIL_URL = "/jsp/infomationQuality/infomationQualityDetail.jsp";// 修改页面
	
	private final String ACTIVITY_URL = "/jsp/claim/dealerClaimMng/showActivity.jsp";
	
    private final String MAINTAIN_HIS_URL = "/jsp/claim/application/maintainHistory.jsp";//维修历史信息页面
    private final String AUDITING_HIS_URL = "/jsp/claim/application/auditingHistory.jsp";//授权历史记录信息页面
    private final String FREEMAINTAIM_HIS_URL = "/jsp/claim/application/freeMaintainHistory.jsp";//保养历史记录
    private final String OEM_HIS_URL = "/jsp/claim/application/OEMMaintainHistory.jsp";//OEM维修历史记录页面
  
    private final String QUALITY_URL="/jsp/claim/dealerClaimMng/qualitycheck.jsp";//质量信息卡页面
    private final String QUALITY_SERCH_URL="/jsp/claim/dealerClaimMng/qualityModify.jsp";//质量信息卡修改查询
    
    private final String MAINTAIN_STATE_SET_URL = "/jsp/claim/dealerClaimMng/maintainStateSet.jsp" ;//保养状态判定页面
    private final String THREE_PACKAGE_SET_URL = "/jsp/claim/dealerClaimMng/threePackageSet.jsp" ;//配件三包判定页面

    private final String RULE_PARTCODE_URL = "/jsp/vehicleInfoManage/apply/showRuleListPartCode.jsp";// 配件选择
    private final String MAIN_PARTCODE_URL2 = "/jsp/claim/dealerClaimMng/showMainPartCode2.jsp";// 配件选择
    private final String MAIN_PARTCODE_URL3 = "/jsp/claim/dealerClaimMng/showMainPartCode3.jsp";// 配件选择
    private final String MAIN_CHECK_POSITION = "/jsp/claim/dealerClaimMng/checkToPosition2.jsp";
    private final String MAIN_LARGESS_POSITION = "/jsp/claim/dealerClaimMng/largessToPosition2.jsp";
	private final String MAIN_laber_POSITION = "/jsp/claim/dealerClaimMng/laberToPosition2.jsp";
	private final String MAIN_Base_POSITION = "/jsp/claim/dealerClaimMng/BaseToPosition2.jsp";
    
	private final String MAIN_MAL_URL = "/jsp/claim/dealerClaimMng/showMainMal.jsp";// 故障代码选择

	private final String WORK_HOUR_URL = "/jsp/claim/dealerClaimMng/showWorkHour.jsp";// 故障代码选择
	private final String SUPPLIER_CODE_URL = "/jsp/claim/dealerClaimMng/showSupplierCode.jsp";// 故障代码选择
	
	private final String SHOW_WORKHOURS_CODE = "/jsp/claim/dealerClaimMng/showWorkHoursCode.jsp";// 故障代码选择

	public void chooseRoForward() {
		try {
			TcCodePO c  = new TcCodePO();
			c.setCodeId("94051003");
			c = (TcCodePO) dao.select(c).get(0);
			act.setOutData("day", c.getCodeDesc());
			act.setForword(RO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: claimBillForword
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimBillForward() {
		try {
			act.setForword(CLAIM_BILL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: claimBillAddForword
	 * @Description: TODO(索赔但新增页面跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimBillAddForward() {
		String dealerId = loginUser.getDealerId();
		String phone="";
		try {
			TmDealerPO tdp = new TmDealerPO();
			tdp.setDealerId(Utility.getLong(dealerId));
			List<TmDealerPO> lsd = dao.select(tdp);
			if (lsd != null) {
				if (lsd.size() > 0) {
					tdp = lsd.get(0);
				}
			}

			if(tdp.getPhone()!=null&&!tdp.getPhone().equals("")){
				phone=tdp.getPhone();
			}
			act.setOutData("OTHERFEE", getOtherfeeStr());
			act.setOutData("dealerName", tdp.getDealerName());
			act.setOutData("dealerCode", tdp.getDealerCode());
			act.setOutData("phone", phone);
			List<TtAsWrGamefeePO> lst = daom.queryGuaranteeAmount(null, null,
					-1l, null);
			act.setOutData("FEE", lst);
//			List<Map<String, Object>> accessoryDtlList = dao.getAccessoryDtl(roNo);
//			List<Map<String, Object>> workHourCodeMap = dao.getWorkhoursCode();
//			act.setOutData("workHourCodeMap", workHourCodeMap);
//			act.setOutData("accessoryDtlList", accessoryDtlList);
			act.setForword(CLAIM_BILL_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: claimBillModifyForword
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void claimBillModifyForward() {
		String phone = "";
		try {
			TtAsActivityPO activity = new TtAsActivityPO();
			String id = request.getParamValue("ID");
			String type = request.getParamValue("type");
			TtAsWrApplicationPO a = new TtAsWrApplicationPO();
			a.setId(Long.valueOf(id));
			a= (TtAsWrApplicationPO) dao.select(a).get(0);
			List<Map<String, Object>> mainCodeList = dao.queryMainList(id);//得到主因件集合
			
			TtAsWrApplicationExtPO tawep = dao.queryApplicationById(id);
			List<ClaimListBean> partls = dao.queryPartById(id); // 取配件信息
			List<ClaimListBean> itemls = dao.queryItemById(id); // 取工时
			List<TtAsWrNetitemExtPO> otherls = dao.queryOtherByid(id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			List<Map<String, Object>> appAuthls = dao.queryAppAuthDetail(id);//审核授权信息
			
			List<Map<String, Object>> accessoryDtlList = dao.getclaimAccessoryDtl(a.getClaimNo());
			act.setOutData("accessoryDtlList", accessoryDtlList);
			
			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoneyAPP(a.getClaimNo());
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================
			TmDealerPO d =  new TmDealerPO();
			d.setDealerCode(tawep.getDealerCode());
			
			List<TmDealerPO> resList = dao.select(d);
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				phone = dealerPO.getPhone();
			}
			act.setOutData("application", tawep);
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("appAuthls",appAuthls);//索赔单之审核信息
			act.setOutData("mainCodeList",mainCodeList);
			act.setOutData("size",mainCodeList.size());
			act.setOutData("otherLs", otherls);
			act.setOutData("attachLs", attachLs);
			act.setOutData("type", type);
			act.setOutData("phone", phone);
			TtAsRepairOrderPO po=new TtAsRepairOrderPO();
			po.setRoNo(tawep.getRoNo());
			List<TtAsRepairOrderPO> select = dao.select(po);
			String code = select.get(0).getCamCode();
			String claimType = a.getClaimType().toString();
			if((claimType.equals(Constant.CLA_TYPE_06.toString())&&jugeReplce(code))||claimType.equals(Constant.CLA_TYPE_02.toString())||claimType.equals(Constant.CLA_TYPE_11.toString())){
				act.setForword(this.CLAIM_BILL_MODIFY_URL);
			}else{
				act.setForword(this.CLAIM_BILL_MODIFY_URL2);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: getDetailByVinForward
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getDetailByVinForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(SHOW_VIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
    public void getQualityCard(){
    	
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String claimId = request.getParamValue("claimId");
			TtAsWrInformationqualityPO po=dao.getQualityCard(claimId);
			act.setOutData("po", po);
			act.setForword(QUALITY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
    
    public void qualitySerch(){
    	
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String claimId = request.getParamValue("claimId");
			TtAsWrInformationqualityPO po=dao.getQualityCard(claimId);
			act.setOutData("po", po);
			act.setOutData("claimId",claimId);
			act.setForword(QUALITY_SERCH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
    }
    
    public void qualityModify(){
    	
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	try{
    		
    		TtAsWrInformationqualityPO po=new TtAsWrInformationqualityPO();
    		String id = request.getParamValue("id");
    		String caseWork = request.getParamValue("caseWork");
    		String questionCase = request.getParamValue("questionCase");
    		String conntion = request.getParamValue("conntion");
    		String dutyUnit = request.getParamValue("dutyUnit");
    		String tempStep = request.getParamValue("tempStep");
    		String forveStep = request.getParamValue("forveStep");
    		String suggest = request.getParamValue("suggest");
    		String serviceOpinion = request.getParamValue("serviceOpinion");
    		po.setId(Long.parseLong(id));
    		po.setCaseWork(caseWork);
    		po.setQuestionCase(questionCase);
    		po.setConntion(conntion);
    		po.setDutyUnit(dutyUnit);
    		po.setTempStep(tempStep);
    		po.setForveStep(forveStep);
    		po.setSuggest(suggest);
    		po.setServiceOpinion(serviceOpinion);
    		po.setUpdateBy(logonUser.getUserId());
    		po.setUpdateDate(new Date());
    		dao.qualityUpdate(po);
    		act.setOutData("flag",true);
    	}catch(Exception e){
    		BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
    	}
    }

	/**
	 * 
	 * @Title: selectMainTimeForward
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void selectMainTimeForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String roNo = request.getParamValue("roNo");
			String vin = request.getParamValue("vin");
			String treeCode = request.getParamValue("TREE_CODE");
			String id = request.getParamValue("timeId"); // 主页面中的主工时代码
			String timeCode = request.getParamValue("timeCode");
			String modelId = request.getParamValue("MODEL_ID");
			String aCode = request.getParamValue("aCode");
			String yiedly = request.getParamValue("yiedly");
			List<Map<String,Object>> list = dao.getLabourDan(logonUser.getDealerId(), modelId, logonUser.getOemCompanyId());
			if(list!=null&&list.size()>0){
				act.setOutData("price", "isPrice");//是否维护了工时单价
			}else{
				act.setOutData("price", "noPrice");
			}
			act.setOutData("yiedly", yiedly);
			act.setOutData("roNo", roNo);
			act.setOutData("aCode", aCode);
			act.setOutData("vin", vin);
			act.setOutData("timeId", id);
			act.setOutData("timeCode", timeCode);
			act.setOutData("TREE_CODE", treeCode);
			act.setOutData("modelId", modelId);
			act.setOutData("GROUP", getGroupNameStr());
			act.setForword(MAIN_TIME_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: selectTimeForward
	 * @Description: TODO(选择工时页面跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void selectTimeForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String treeCode = request.getParamValue("TREE_CODE");
			String id = request.getParamValue("timeId"); // 主页面中的主工时代码
			String timeCode = request.getParamValue("timeCode");
			String modelId = request.getParamValue("MODEL_ID");
			act.setOutData("timeId", id);
			act.setOutData("timeCode", timeCode);
			act.setOutData("TREE_CODE", treeCode);
			act.setOutData("modelId", modelId);
			act.setOutData("GROUP", getGroupNameStr());
			act.setForword(TIME_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: selectMainPartCodeForward
	 * @Description: TODO(选择主配件)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void selectMainPartCodeForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String roNo = request.getParamValue("roNo");
			String vin = request.getParamValue("vin");
			String groupId = request.getParamValue("GROUP_ID");
			String yiedlyType = request.getParamValue("yiedlyType");
			String aCode = request.getParamValue("aCode");
			String repairType = request.getParamValue("repairType");
			TmDownParameterPO p = new TmDownParameterPO();
			p.setDealerId(Long.valueOf(logonUser.getDealerId()));
			if(Constant.PART_IS_CHANGHE_01.toString().equalsIgnoreCase(yiedlyType)){
				p.setParameterCode(Constant.CLAIM_BASIC_PARAMETER_09.toString());
			}else{
				p.setParameterCode(Constant.CLAIM_BASIC_PARAMETER_08.toString());
			}
			List<TmDownParameterPO> list = dao.select(p);
			if(list!=null&&list.size()>0){
				act.setOutData("partPrice", "yes");
			}else{
				act.setOutData("partPrice", "no");
			}
			act.setOutData("yiedlyType", yiedlyType);
			act.setOutData("aCode", aCode);
			act.setOutData("repairType", repairType);
			act.setOutData("roNo", roNo);
			act.setOutData("vin", vin);
			act.setOutData("GROUP_ID", groupId);
			if("11441009".equals(repairType)){
				act.setForword(MAIN_PARTCODE_URL1);
			}else{
				act.setForword(MAIN_PARTCODE_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	public void getPartDetai() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String id = request.getParamValue("id");
			String code = request.getParamValue("code");
			String yiedlyType = request.getParamValue("yiedlyType");
			if(Utility.testString(code)){
				TtAsActivityPO p = new TtAsActivityPO();
				p.setActivityCode(code);
				p = (TtAsActivityPO) dao.select(p).get(0);
				id = p.getActivityId().toString();
			}
			act.setOutData("yiedlyType", yiedlyType);
			act.setOutData("id", id);
			act.setForword(MAIN_PARTCODE_URL3);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 旧件入库时，修改旧件选择
	 */

	public void selectModPartCodeForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String partCode = request.getParamValue("partCode");
			act.setOutData("partCode", partCode);
			act.setForword(MAIN_PARTCODE_URL2);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	 * @Title: selectPartCodeForward
	 * @Description: TODO(上件选择跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void selectPartCodeForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String groupId = request.getParamValue("GROUP_ID");
			act.setOutData("GROUP_ID", groupId);
			act.setForword(PARTCODE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: selectDownPartCodeForward
	 * @Description: TODO(下件选择跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void selectDownPartCodeForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String groupId = request.getParamValue("GROUP_ID");
			String PartCode = request.getParamValue("partCode");
			String yiedily = request.getParamValue("yiedily");
			String vin = request.getParamValue("vin");
			if(!Utility.testString(yiedily)){
				yiedily = Constant.PART_IS_CHANGHE_01.toString();
			}
			act.setOutData("yiedily", yiedily);
			act.setOutData("vin", vin);
			act.setOutData("GROUP_ID", groupId);
			act.setOutData("PartCode", PartCode);
			act.setForword(DOWN_PARTCODE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
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
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		String companyId =logonUser.getOemCompanyId(); //GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		System.out.println(logonUser.getOemCompanyId());
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 10;
			String vin = request.getParamValue("VIN_PARAM");
			String customer = request.getParamValue("CUSTOMER");
			String vinParent = request.getParamValue("vinParent");
			map.put("dealerId", dealerId);
			map.put("vin", vin);
			map.put("vinParent", vinParent);
			map.put("customer", customer);
			map.put("companyId", companyId.toString());
			String vin1 = request.getParamValue("VIN");
			//增加判断vin是否为空导致车辆信息全表扫描 2015-03-13
			if(!(Utility.testString(map.get("vin")) || Utility.testString(map.get("vinParent")))){
				throw new UserException("vin参数无效!");
			}
			
			PageResult<TmVehicleExtendPO> ps = dao.getVin(map, pageSize, curPage);
		//	List<Map<String, Object>> list = dao.getVinUserName(vinParent);
			//act.setOutData("list", list);
			//List<Map<String, Object>> list1 = dao.getGuranteeCode(vinParent);//查询三包规则代码
		//	if(list1.size()>0){
		//	act.setOutData("list1", list1.get(0));
		//	}else{
		//		act.setOutData("list1", 0);	
		//	}
			String pdiInfo="";
			Long orderId=-1L;
			String date = "";
			/*TmVehiclePO vehiclePO = new TmVehiclePO();
			vehiclePO.setVin(vin1);
			List<TmVehiclePO> list= dao.select(vehiclePO);*/
			int IS_PDI = 0;
			/*if(list.size() > 0 )
			{
				IS_PDI = list.get(0).getIsPdi();
			}*/
			if(ps.getTotalRecords()>0){
				orderId = ps.getRecords().get(0).getOrderId();
				date = ps.getRecords().get(0).getInStoreDate();
				IS_PDI = ps.getRecords().get(0).getIsPdi();
			}
			if(IS_PDI == 0)
			{
				
				if("".equalsIgnoreCase(date)|| date == null){
					pdiInfo = "noTime";
				}else{
					String d2 = DateTimeUtil.parseDateToString2(new Date());//将当前时间转化为 字符串
					int day  = Utility.compareDate(date,d2,0);//获取相差天数
					if(day>7){
						pdiInfo = "outTime";
					}
				}
			}
			
			String saleInfo = "unSale";
		//	List <TtDealerActualSalesPO>  unSale = dao.checkUnSaleVechile(vinParent);//查询是否销售
			if( orderId>0){
				saleInfo = "sale";
			} 
			System.out.println(saleInfo);
			/**判断是否在整车三包范围内。
			 * 首先,根据VIN 判断时间是否超保。
			 * 然后得到 保修里程 传入页面进行判断
			 
			String timeOut="inTime";
			Date date = new Date();
			TmVehiclePO v= new TmVehiclePO();
			v.setVin(vinParent);
			List<TmVehiclePO> vList = dao.select(v);
			if(vList!=null && vList.size()>0){
				v = vList.get(0);
			
			
			if(v.getPurchasedDate()!=null){
				date = v.getPurchasedDate();
			}
			}
			Date now = new Date();
			String formatStyle ="yyyy-MM-dd";  
			SimpleDateFormat df = new SimpleDateFormat(formatStyle);  
			String d1 = df.format(date);
			String d2 = df.format(now);
			int month  = Utility.compareDate(d1,d2,1); // 取得购车时间到现在的月份相差
			TtAsWrGamePO g = new TtAsWrGamePO();
			if("sale".equals(saleInfo)){
				g.setId(v.getClaimTacticsId());
				g = (TtAsWrGamePO) dao.select(g).get(0);
				if(month>g.getClaimMonth()){
					timeOut="outTime";
					System.out.println("该车超过了三包月份!");
				}
			}
		
			*/
			System.out.println(vin1+",,,,,,,,"+vin+"......"+vinParent);
			act.setOutData("saleInfo", saleInfo);
			act.setOutData("pdiInfo", pdiInfo);
			act.setOutData("flag", "true");
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2012-12-25
	public void getVinAndUser() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			//zhumingwei 2012-12-25 判断这个vin是否在不在这个用户的业务范围里面
			String userId=logonUser.getUserId().toString();
			String vin1 = request.getParamValue("VIN");
			int count = dao.getCount1(userId,vin1);
			if(count<=0){
				act.setOutData("flag", "false");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	/**
	 * 
	 * @Title: queryTime
	 * @Description: TODO(弹出框查询工时)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void queryTime() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		StringBuffer con = new StringBuffer();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 10;
			String treeCode = request.getParamValue("TREE_CODE");
			String id = request.getParamValue("timeId"); // 主页面中的主工时代码
			String roNo = request.getParamValue("roNo"); // 工单号
			String vin = request.getParamValue("vin"); // 车辆VIN
			String aCode = request.getParamValue("aCode");
			Long id0 = null;
			if (Utility.testString(id)) {
			
				TtAsWrWrlabinfoPO tawp = new TtAsWrWrlabinfoPO();
				tawp.setLabourCode(id); //
				List<TtAsWrWrlabinfoPO> temp = dao.select(tawp);// 通过labour_code取工时信息表中的id
				if (temp != null) {
					if (temp.size() > 0) {
						tawp = temp.get(0);
					}
				}
				id0 = tawp.getId();
			} else {

			}

			act.setOutData("timeId", id);
			act.setOutData("TREE_CODE", treeCode);
			String modelId = request.getParamValue("WRGROUP_ID");//取道的是车型
			String wrgroupId = request.getParamValue("WRGROUP_ID"); // 取道的是车型
			TtAsWrModelItemPO tawmip = new TtAsWrModelItemPO();
			if (Utility.testString(wrgroupId)) {
				List<TtAsWrModelItemPO> wLs = dao.getWgroupId(wrgroupId);
				if (wLs != null) {
					if (wLs.size() > 0) {
						tawmip = wLs.get(0);
						wrgroupId = tawmip.getWrgroupId().toString();
					} else {
						wrgroupId = "-1"; // 使其没有车型组
					}
				} else {
					wrgroupId = "-1"; // 使其没有车型组
				}
			}

			String labourCode = request.getParamValue("LABOUR_CODE"); // 查询页面中的查询条件工时代码
			String cnDes = request.getParamValue("CN_DES");

			//检验是否为服务活动以及活动下面的配件
			if(Utility.testString(aCode)){
				TtAsActivityPO p = new TtAsActivityPO();
				p.setActivityCode(aCode);
				p = (TtAsActivityPO) dao.select(p).get(0);
				TtAsActivityRelationPO rp = new TtAsActivityRelationPO();
				rp.setActivityId(p.getActivityId());
				rp.setLargessType(Long.valueOf(Constant.SERVICEACTIVITY_CAR_cms_06));
				List <TtAsActivityRelationPO> rList = dao.select(rp);
				if(rList!=null &&rList.size()>0){
					String str = "";
					for(int i=0;i<rList.size();i++){
						str = str+"'"+rList.get(i).getProjectCode()+"',";
					}
					System.out.println(str);
					con.append(" and t.labour_code in("+str.substring(0, str.length()-1)+")");
				}
			} 
			// 主维修工时
			if (Utility.testString(treeCode)) {
				con.append(" and T.TREE_CODE = '" + treeCode + "' ");
			}
			if (id0 != null || "4".equals(treeCode)) {
				con
						.append(" AND T.ID IN (select add_id from TT_AS_WR_ADDITIONALITEM where w_id="
								+ id0 + ") ");
			}
			// if (Utility.testString(wrgroupId)) {
			con.append(" and G.WRGROUP_ID = " + wrgroupId + " ");
			// }
			if (Utility.testString(labourCode)) {
				con.append(" and t.LABOUR_CODE LIKE '%" + labourCode.toUpperCase() + "%' ");
			}
			if (Utility.testString(cnDes)) {
				con.append(" and t.CN_DES LIKE '%" + cnDes + "%' ");
			}
			PageResult<TtAsWrWrlabinfoExtPO> ps = dao.queryTime(logonUser, con.toString(), roNo, vin, modelId,companyId.toString(),pageSize, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void queryTime1() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; 
			// 处理当前页
			PageResult<Map<String,Object>> list=dao.queryTime1(request,logonUser,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps",list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryTime2() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; 
			// 处理当前页
			PageResult<Map<String,Object>> list=dao.queryTime2(request,logonUser,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps",list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: queryPartCode
	 * @Description: TODO(查询配件)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void queryPartCode1() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 15;
			String roNo = request.getParamValue("roNo");
			String vin = request.getParamValue("vin");
			String groupId = request.getParamValue("GROUP_ID"); // 车型
			String partCode = request.getParamValue("PART_CODE");
			String downpartCode = request.getParamValue("DOWN_PART_CODE");
			String partName = request.getParamValue("PART_NAME"); // 主页面中的主工时代码
			String supplierName = request.getParamValue("SUPPLIER_NAME");
			String yiedily = request.getParamValue("yiedily");
			String seriesId = dao.getSeries(vin);
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("partCode", partCode);
			map.put("partName", partName);
			map.put("groupId", groupId);
			map.put("companyId", companyId.toString());
			map.put("supplierName", supplierName);
			map.put("yiedily", yiedily);
			map.put("seriesId", seriesId);
			PageResult<TmPtPartBaseExtPO> ps = dao.queryPartCode1(logonUser,
					map, pageSize, curPage,downpartCode);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryPartCode5() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 15;
			String roNo = request.getParamValue("roNo");
			String vin = request.getParamValue("vin");
			String groupId = request.getParamValue("GROUP_ID"); // 车型
			String partCode = request.getParamValue("PART_CODE");
			String partName = request.getParamValue("PART_NAME"); // 主页面中的主工时代码
			String supplierName = request.getParamValue("SUPPLIER_NAME");
			String yiedlyType = request.getParamValue("yiedlyType");
			String aCode = request.getParamValue("aCode");
			String repairType = request.getParamValue("repairType");
			String seriesId = dao.getSeries(vin);
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("partCode", partCode);
			map.put("partName", partName);
			map.put("groupId", groupId);
			map.put("companyId", companyId.toString());
			map.put("supplierName", supplierName);
			map.put("yiedlyType", yiedlyType);
			map.put("seriesId", seriesId);
			map.put("repairType", repairType);
			String str = "";
			if(Utility.testString(aCode)){
				TtAsActivityPO p = new TtAsActivityPO();
				p.setActivityCode(aCode);
				p = (TtAsActivityPO) dao.select(p).get(0);
				TtAsActivityRelationPO rp = new TtAsActivityRelationPO();
				rp.setActivityId(p.getActivityId());
				rp.setLargessType(Long.valueOf(Constant.SERVICEACTIVITY_CAR_cms_07));
				List <TtAsActivityRelationPO> rList = dao.select(rp);
				if(rList!=null &&rList.size()>0){
					for(int i=0;i<rList.size();i++){
						str = str+"'"+rList.get(i).getProjectCode()+"',";
					}
					System.out.println(str);
					if(!"".equalsIgnoreCase(str)){
						str= str.substring(0, str.length()-1);
					}
				}
			} 
			//根据VIN获取车型
			Map<String, Object> vehicle = dao.queryVehicleByVin(vin);
			if (vehicle != null) {
				map.put("modelId", vehicle.get("MODEL_ID")+"");
			}
			PageResult<TmPtPartBaseExtPO> ps = dao.queryPartCode5(logonUser,
					map,str, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryPartCode() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 15;
			String roNo = request.getParamValue("roNo");
			String vin = request.getParamValue("vin");
			String groupId = request.getParamValue("GROUP_ID"); // 车型
			String partCode = request.getParamValue("PART_CODE");
			String partName = request.getParamValue("PART_NAME"); // 主页面中的主工时代码
			String supplierName = request.getParamValue("SUPPLIER_NAME");
			String yiedlyType = request.getParamValue("yiedlyType");
			String aCode = request.getParamValue("aCode");
			String repairType = request.getParamValue("repairType");
			String seriesId = dao.getSeries(vin);
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("partCode", partCode);
			map.put("partName", partName);
			map.put("groupId", groupId);
			map.put("companyId", companyId.toString());
			map.put("supplierName", supplierName);
			map.put("yiedlyType", yiedlyType);
			map.put("seriesId", seriesId);
			map.put("repairType", repairType);
			String str = "";
			if(Utility.testString(aCode)){
				TtAsActivityPO p = new TtAsActivityPO();
				p.setActivityCode(aCode);
				p = (TtAsActivityPO) dao.select(p).get(0);
				TtAsActivityRelationPO rp = new TtAsActivityRelationPO();
				rp.setActivityId(p.getActivityId());
				rp.setLargessType(Long.valueOf(Constant.SERVICEACTIVITY_CAR_cms_07));
				List <TtAsActivityRelationPO> rList = dao.select(rp);
				if(rList!=null &&rList.size()>0){
					for(int i=0;i<rList.size();i++){
						str = str+"'"+rList.get(i).getProjectCode()+"',";
					}
					System.out.println(str);
					if(!"".equalsIgnoreCase(str)){
						str= str.substring(0, str.length()-1);
					}
				}
			} 
			//根据VIN获取车型
			Map<String, Object> vehicle = dao.queryVehicleByVin(vin);
			if (vehicle != null) {
				map.put("modelId", vehicle.get("MODEL_ID")+"");
			}
			PageResult<TmPtPartBaseExtPO> ps = dao.queryPartCode(logonUser,
					map,str, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void queryPartCode4() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			PageResult<Map<String,Object>>  ps = dao.queryPartCode4(request,logonUser,15, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//旧件入库时选择需修改的配件
	public void queryPartCode2() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 10;
			String partCode = request.getParamValue("partCode"); // 主页面中的主工时代码
			map.put("partCode", partCode);
			map.put("companyId", companyId.toString());
			PageResult<TmPtPartBaseExtPO> ps = dao.queryPartCodes(logonUser,
					map, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void queryPartCode3() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 10;
			String yiedily = request.getParamValue("yiedlyType");
			String id = request.getParamValue("id");
			map.put("yiedily", yiedily);
			map.put("id", id);
			map.put("dealerId", logonUser.getDealerId());
			PageResult<ActivityPartDetailBean> ps = dao.activityPartDetail(map, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: selectCodeForward 
	* @Description: TODO(跳转代码选择页面) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void selectCodeForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String type = request.getParamValue("type");
			String labourCode = request.getParamValue("labourCode");
			act.setOutData("labourCode", labourCode);
			act.setOutData("type", type);
			act.setForword(SHOW_CODE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * zhumingwei 2011-02-12
	 */
	public void selectCodeForward111() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String type = request.getParamValue("type");
			String labourCode = request.getParamValue("labourCode");
			List<Map<String, Object>> list = dao.selectCode();
			act.setOutData("list", list);
			act.setOutData("labourCode", labourCode);
			act.setOutData("type", type);
			act.setForword(SHOW_CODE_URL111);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 
	* @Title: selectSupplierForward 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void selectSupplierForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String partCode = request.getParamValue("partCode");
			act.setOutData("partCode", partCode);
			
			//zhumingwei 2011-11-30  此方法用于区分轿车和微车
			TcCodePO codePo= new TcCodePO();
			codePo.setType(Constant.chana+"");
			TcCodePO poValue = (TcCodePO)dao.select(codePo).get(0);
			String codeId = poValue.getCodeId();
			act.setOutData("codeId", Integer.parseInt(codeId));
			//zhumingwei 2011-11-30
			
			act.setForword(SHOW_SUPPLIER_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	* @Title: querySupplier 
	* @Description: TODO(查询配件经销商) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void querySupplier() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		try {
			
			String partCode = request.getParamValue("partCode"); //配件代码
			String supplierCode = request.getParamValue("SUPPLIER_CODE"); //供应商代码
			String supplierName = request.getParamValue("SUPPLIER_NAME"); //供应商名称
			String count = request.getParamValue("count"); //判断是否全查
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					if("00-000".equalsIgnoreCase(partCode)){
						partCode="00-000','00-0000";// 改为东安昌河无配件
					}
			Map<String,String> map = new HashMap<String,String>();
			map.put("partCode", partCode);
			map.put("companyId", companyId.toString());
			map.put("supplierCode", supplierCode);
			map.put("supplierName", supplierName);
			map.put("count", count);
			//List<Map<String, Object>> supplierList = dao.viewSupplier(partCode);
		    int len = 1;
//			if(supplierList.size()>0){
//				len=1;
//			}
			map.put("len", String.valueOf(len));
			map.put("DealerId", logonUser.getDealerId());
			PageResult<TmPtSupplierPO> ps = dao.querySupplier(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("partCode", partCode);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	* @Title: getChngCode 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void getChngCode() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String type = request.getParamValue("type");
			String labourCode = request.getParamValue("labourCode");
			String code = request.getParamValue("CODE");
			String codeName = request.getParamValue("CODE_NAME");
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			if ("1".equals(type)) {
				act.setOutData("ps",getChngCodeList(Constant.BUSINESS_CHNG_CODE_01,code,codeName,curPage,Constant.PAGE_SIZE)); //质损程度
			}else if  ("2".equals(type)) {
			act.setOutData("ps",getChngCodeList(Constant.BUSINESS_CHNG_CODE_02,code,codeName,curPage,Constant.PAGE_SIZE));// 质损区域
			}else if  ("3".equals(type)) {
			act.setOutData("ps",getChngCodeList(Constant.BUSINESS_CHNG_CODE_03,code,codeName,curPage,Constant.PAGE_SIZE));// 质损类型
			}else if  ("4".equals(type)) {
				if (Utility.testString(labourCode)) {
					act.setOutData("ps",dao.queryPartCodeByItemId(Constant.BUSINESS_CHNG_CODE_04,labourCode,code,codeName,curPage,Constant.PAGE_SIZE)); //质损程度
				}else {
					act.setOutData("ps",getChngCodeList(Constant.BUSINESS_CHNG_CODE_04,code,codeName,curPage,Constant.PAGE_SIZE)); //质损程度
					
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-02-12
	public void getChngCode111() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String type = request.getParamValue("type");
			String code = request.getParamValue("CODE");
			String codeName = request.getParamValue("CODE_NAME");
			String CUSTOMERS_PROBLEM = request.getParamValue("CUSTOMERS_PROBLEM");
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			if ("5".equals(type)) {
				act.setOutData("ps",getChngCodeList111(code,codeName,CUSTOMERS_PROBLEM,curPage,Constant.PAGE_SIZE));
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-03-08
	public void queryCon(){
		
		
		String code = request.getParamValue("code");
		List<Map<String, Object>> list = dao.queryCon(code);
		act.setOutData("list", list);
	}
	
	//zhumingwei add 2011-06-17 根据车系查询车型组
	public void queryCon1(){
		
		
		String groupId = request.getParamValue("groupId");
		List<Map<String, Object>> list = dao.queryCon1(groupId);
		act.setOutData("list", list);
	}
	
	/**
	 * 
	* @Title: getChngCode 
	* @Description: TODO(根据类型返回故障代码，质损区域，质损类型，质损程度) 
	* @param @param type
	* @param @return    设定文件 
	* @return List<TmBusinessChngCodePO>    返回类型 
	* @throws
	 */
	public PageResult<TmBusinessChngCodePO> getChngCodeList(int type,String code,String codeName,int curPage,int pageSize) {
		ActionContext ctx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) ctx.getSession().get(Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		
		PageResult<TmBusinessChngCodePO> seriesList = dao.queryChngCodeByType(type,companyId,code,codeName,curPage,pageSize);
		return seriesList;
	}
	
	//zhumingwei 2011-02-12
	public PageResult<Map<String, Object>> getChngCodeList111(String code,String codeName,String CUSTOMERS_PROBLEM,int curPage,int pageSize) {
		PageResult<Map<String, Object>> ps = dao.queryChngCodeByType111(code,codeName,CUSTOMERS_PROBLEM,curPage,pageSize);
		return ps;
	}

	/**
	 * 
	 * @Title: getChngCodeStr
	 * @Description: TODO(取得故障代码，质损区域，质损类型，质损程度下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getChngCodeStr(int type) {
		ActionContext ctx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) ctx.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		List<TmBusinessChngCodePO> seriesList = dao.queryChngCodeByType(type,
				companyId,null,null);
		String retStr = "";
		// retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TmBusinessChngCodePO bean = new TmBusinessChngCodePO();
			bean = (TmBusinessChngCodePO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getCode() + "\' title=\'"
					+ bean.getCodeName() + "\'>" + bean.getCodeName()
					+ "</option>";
		}
		return retStr;
	}

	/**
	 * 
	 * @Title: getOtherfeeStr
	 * @Description: TODO(取得其他费用下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getOtherfeeStr() {
		ActionContext ctx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) ctx.getSession().get(
				Constant.LOGON_USER);

		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		List<TtAsWrOtherfeePO> seriesList = dao.queryOtherFee(companyId);
		String retStr = "";
		retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsWrOtherfeePO bean = new TtAsWrOtherfeePO();
			bean = (TtAsWrOtherfeePO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getFeeCode() + "\'"
					+ "title=\'" + bean.getFeeName() + "\'>"
					+ bean.getFeeCode() + "-" + bean.getFeeName() + "</option>";
		}
		return retStr;
	}

	/**
	 * 
	 * @Title: getOtherfeeStr
	 * @Description: TODO()
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getActivityStr() {
		List<TtAsActivityPO> seriesList = dao.queryActivityCombo();
		String retStr = "";
		retStr += "<option value=\',0,\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsActivityPO bean = new TtAsActivityPO();
			bean = (TtAsActivityPO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getActivityCode() + ","
					+ bean.getActivityFee() + "," + bean.getIsFixfee() + "\'>"
					+ bean.getActivityName() + "</option>";
		}
		return retStr;
	}
	/**
	 * 
	* @Title: getActivityStr 
	* @Description: TODO(根据车型ID查询服务活动) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public void getActivityStrById() {
		
		
		String modelId = request.getParamValue("modelId");
		List<TtAsActivityPO> seriesList = dao.queryActivityCombo(modelId);
		String retStr = "";
		retStr += "<option value=\',0,\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsActivityPO bean = new TtAsActivityPO();
			bean = (TtAsActivityPO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getActivityCode() + ","
					+ bean.getActivityFee() + "," + bean.getIsFixfee() + "\'>"
					+ bean.getActivityName() + "</option>";
		}
		act.setOutData("activity", retStr);
	}
	
	/**
	 * 
	 * @Title: changeItem
	 * @Description: TODO(选择工时会使故障代码联动)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void changeItem() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			// 取得ITEM_ID
			String itemId = request.getParamValue("ITEM_ID");
			// String partName = request.getParamValue("PART_NAME"); //
			// 主页面中的主工时代码
			List<TmBusinessChngCodePO> seriesList = dao.queryPartCodeByItemId(
					Constant.BUSINESS_CHNG_CODE_04, itemId,null,null);
			String retStr = "";
			// retStr += "<option value=\'\'>-请选择-</option>";
			for (int i = 0; i < seriesList.size(); i++) {
				TmBusinessChngCodePO bean = new TmBusinessChngCodePO();
				bean = (TmBusinessChngCodePO) seriesList.get(i);
				retStr += "<option value=\'" + bean.getCode() + "\' title=\'"
						+ bean.getCodeName() + "\'>" + bean.getCodeName()
						+ "</option>";
			}
			// 如果工时没有对应故障代码则显示全部故障代码
			if (seriesList.size() == 0) {
				retStr = getChngCodeStr(Constant.BUSINESS_CHNG_CODE_04);
			}
			act.setOutData("changeCode", retStr);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	* @Title: changeOtherFore 
	* @Description: TODO(选择其他项目后带出是否已经授权) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void changeOtherFore() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		try {
			
			String itemCode = request.getParamValue("itemCode"); // 其他项目代码
			String roNo = request.getParamValue("roNo");
			String vin = request.getParamValue("vin");
			Map<String,String> map =new HashMap();
			map.put("companyId", companyId.toString());
			map.put("itemCode", itemCode);
			map.put("roNo", roNo);
			map.put("vin", vin);
			List<TtAsWrOtherfeePO> ls= dao.changeOtherFore(map);
			int count = 0;
			TtAsWrOtherfeePO tawop = new TtAsWrOtherfeePO();
			if(ls!=null) {
				if (ls.size()>0) {
					tawop = ls.get(0);
					count = tawop.getIsDel();
				}
			}
			act.setOutData("count",count);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: changeFree
	 * @Description: TODO(选择VIN后根据车型ID联动免费保养次数下拉框)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void changeFree() {
		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		Long provinceId = null;
		try {
			Integer pageSize = 10;
			List resList = daom.queryDealerById(Utility.getLong(loginUser
					.getDealerId()));
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				provinceId = dealerPO.getProvinceId();
			}
			// 取得ITEM_ID
			String modelId = request.getParamValue("MODEL_ID");
			Long modelIdLong = Utility.getLong(modelId);
			String purchasedDateStr = request.getParamValue("PURCHASED_DATE");
			Date purchasedDate = Utility.getDate(purchasedDateStr, 3);
			// String partName = request.getParamValue("PART_NAME"); //
			// 主页面中的主工时代码
			List<TtAsWrGamefeePO> seriesList = daom.queryGuaranteeAmount(
					purchasedDate, provinceId, modelIdLong, companyId);
			// List<TmBusinessChngCodePO> seriesList =
			// dao.queryPartCodeByItemId(
			// Constant.BUSINESS_CHNG_CODE_04, itemId);
			String retStr = "";
			retStr += "<option value=\'0\'>-请选择-</option>";
			if (seriesList != null) {
				for (int i = 0; i < seriesList.size(); i++) {
					TtAsWrGamefeePO bean = new TtAsWrGamefeePO();
					bean = (TtAsWrGamefeePO) seriesList.get(i);
					retStr += "<option value=\'" + bean.getManintainFee()
							+ "\'>" + bean.getMaintainfeeOrder() + "</option>";
				}
			}
			/*
			 * // 如果工时没有对应故障代码则显示全部故障代码 if (seriesList.size() == 0) { retStr =
			 * getChngCodeStr(Constant.BUSINESS_CHNG_CODE_04); }
			 */
			act.setOutData("changeFree", retStr);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: changeFreeText
	 * @Description: TODO(在隐藏于设置免费保养次数以及免费保养费用)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public void changeFreeText() {
		String dealerId = loginUser.getDealerId();
		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		Long provinceId = null;
		StringBuffer con = new StringBuffer();
		try {
			List resList = daom.queryDealerById(Utility.getLong(loginUser.getDealerId()));
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				provinceId = dealerPO.getProvinceId();
			}
			String modelId = request.getParamValue("MODEL_ID");
			Long modelIdLong = Utility.getLong(modelId);
			String purchasedDateStr = request.getParamValue("PURCHASED_DATE");
			String vin = request.getParamValue("VIN");
			List<Map<String, Object>> modelList = dao.viewModelId(vin);
			List<Map<String, Object>> freeList = daom.viewFreeModel(vin);
			String retStr = "";
			//这里判定此次保养是否同意费用选项。。。
			String roNo = request.getParamValue("ro_no");
			if(StringUtil.notNull(roNo)){
				TtAsRepairOrderPO po = new TtAsRepairOrderPO();
				po.setRoNo(roNo);
				List<TtAsRepairOrderPO> roList = dao.select(po);
				if(roList.size()>0){
					// 0代表同意给费用。。1 不同意费用
					if("1".equals(roList.get(0).getAccreditAmount().toString())&&Integer.valueOf(roList.get(0).getApprovalYn().toString())==1){
						act.setOutData("payment", true);
					}
				}
			}
			// retStr += "<option value=\'0\'>-请选择-</option>";
			if (freeList != null&&freeList.size()>0) {
				retStr = freeList.get(0).get("FREE") + "*" + 1;
			}
			System.out.println(retStr);
			/*
			 * // 如果工时没有对应故障代码则显示全部故障代码 if (seriesList.size() == 0) { retStr =
			 * getChngCodeStr(Constant.BUSINESS_CHNG_CODE_04); }
			 */
			act.setOutData("changeFree", retStr);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: getGroupNameStr
	 * @Description: TODO(取得车型组下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getGroupNameStr() {
		List<TtAsWrModelGroupPO> seriesList = dao.queryGroupName();
		String retStr = "";
		// retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsWrModelGroupPO bean = new TtAsWrModelGroupPO();
			bean = (TtAsWrModelGroupPO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getWrgroupId() + "\'>"
					+ bean.getWrgroupName() + "</option>";
		}
		return retStr;
	}

	/**
	 * 
	 * @Title: getBrandStr
	 * @Description: TODO(品牌下拉框)
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getBrandStr() {
		List<VwMaterialGroupPO> seriesList = dao.queryBrand();
		String retStr = "";
		// retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			VwMaterialGroupPO bean = new VwMaterialGroupPO();
			bean = (VwMaterialGroupPO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getBrandCode() + "\'>"
					+ bean.getBrandName() + "</option>";
		}
		return retStr;
	}

	/**
	 * 
	 * @Title: getSeriesStr
	 * @Description: TODO(车系下拉框)
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getSeriesStr(String brand) {
		List<VwMaterialGroupPO> seriesList = dao.querySeries(brand);
		String retStr = "";
		// retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			VwMaterialGroupPO bean = new VwMaterialGroupPO();
			bean = (VwMaterialGroupPO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getSeriesCode() + "\'>"
					+ bean.getSeriesName() + "</option>";
		}
		return retStr;
	}

	/**
	 * 
	 * @Title: getModelStr
	 * @Description: TODO(车型下拉框)
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getModelStr(String series) {
		List<VwMaterialGroupPO> seriesList = dao.queryModel(series);
		String retStr = "";
		// retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			VwMaterialGroupPO bean = new VwMaterialGroupPO();
			bean = (VwMaterialGroupPO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getModelCode() + "\'>"
					+ bean.getModelName() + "</option>";
		}
		return retStr;
	}

	/**
	 * 
	 * @Title: getBrand
	 * @Description: TODO(取得品牌)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getBrand() {
		try {
			act.setOutData("brand", getBrandStr());
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: getSeries
	 * @Description: TODO(取得车系)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getSeries() {
		try {
			String brandCode = request.getParamValue("BRAND_CODE");
			act.setOutData("series", getSeriesStr(brandCode));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: getModel
	 * @Description: TODO(取得车型)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getModel() {
		try {
			String series = request.getParamValue("SERIES_CODE");
			act.setOutData("model", getModelStr(series));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: getActivityRepairs
	 * @Description: TODO(根据服务活动CODE取得维修项目)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getActivityRepairs() {
		String dealerId = loginUser.getDealerId();
		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		try {
			
			String code = request.getParamValue("CODE");
			String modelId = request.getParamValue("modelId");
			act.setOutData("activityRepairs", dao.queryActivityRepair(code,
					dealerId,modelId,companyId.toString()));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: getActivityParts
	 * @Description: TODO(根据服务活动CODE取得配件)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getActivityParts() {
		String dealerId = loginUser.getDealerId();
		try {
			
			String code = request.getParamValue("CODE");
			String mileage = request.getParamValue("IN_MILEAGE");
			String vin = request.getParamValue("VIN");
			Double mileagedb = Utility.getDouble(mileage);
			act.setOutData("activityParts", dao.queryActivityPart(code,vin,mileagedb,
					dealerId));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: getActivityOthers
	 * @Description: TODO(根据服务活动CODE取得其他项目)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getActivityOthers() {
		String dealerId = loginUser.getDealerId();
		try {
			String code = request.getParamValue("CODE");
			String flag = request.getParamValue("Uflag");
			String yiedily = request.getParamValue("yiedily");
			String vin = request.getParamValue("vin");
			System.out.println(flag);
			Double amounts = 0.0;
			Double baoyAmount=0.0;
			DecimalFormat df = new DecimalFormat("###0.00");
			List<TtAsActivityRelationPO> list = dao.getReList(code,vin,Constant.SERVICEACTIVITY_CAR_cms_05,yiedily);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Double amount2 = 0.0;
					String partCode = list.get(i).getProjectCode();
					amount2 = dao.getAmount(partCode,yiedily,loginUser.getDealerId());
					amounts +=amount2;
				}
			}
			baoyAmount = dao.getFreeForActivity(vin);
			if(Integer.parseInt(flag)==1 ){
				act.setOutData("activityOthers", dao.queryActivityOtherNew(code,
						dealerId));
				act.setOutData("amounts", df.format(amounts));
				act.setOutData("baoyAmount", df.format(baoyAmount));
			}else {
			act.setOutData("activityOthers", dao.queryActivityOther(code,
					dealerId));
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: applicationQuery
	 * @Description: TODO(查询索赔单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void applicationQuery() {
		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		String dealerId = loginUser.getDealerId();
		StringBuffer con = new StringBuffer();
		Date date = new Date();
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String claimNo = request.getParamValue("CLAIM_NO");
			String roNo = request.getParamValue("RO_NO");
			String lineNo = request.getParamValue("LINE_NO");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String vin = request.getParamValue("VIN");
			String roStartdate = request.getParamValue("RO_STARTDATE");
			String roEnddate = request.getParamValue("RO_ENDDATE");
			String status = request.getParamValue("STATUS");
			map.put("dealerId", dealerId);
			map.put("claimNo", claimNo);
			map.put("roNo", roNo);
			map.put("lineNo", lineNo);
			map.put("claimType", claimType);
			map.put("vin", vin);
			map.put("roStartdate", roStartdate);
			map.put("roEnddate", roEnddate);
			map.put("status", status);
			map.put("oemCompanyId", companyId.toString());

			PageResult<TtAsWrApplicationExtBean> ps = dao.queryApplication1(
					loginUser, map, null, 10, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: applicationInsert
	 * @Description: TODO(新增索赔)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void applicationInsert() {
		AclUserBean logonUser = loginUser;
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
		String dealerId = logonUser.getDealerId();
		Date date = new Date();
		String roNo = request.getParamValue("RO_NO"); // 工单号
		boolean flag = DBLockUtil.lock(roNo, DBLockUtil.BUSINESS_TYPE_13);
		if(flag){
			try {
			float labourHoursMain = 0f; // 基本工时数
			int partsCount = 0; // 总配件数
			double labourAmountMain = 0; // 总工时金额
			double partsAmountMain = 0; // 配件金额
			double netItemAmount = 0; // 其他项目金额
			double repairTotal = 0; // 索赔申请金额
			Double compensationMoney = 0d;//补偿费
			Double accPrice = 0d;//辅料费
			String pids = request.getParamValue("pids");//主因件ID
			String roId = request.getParamValue("roId"); // 从工单带过来的roId:为提交时更新工单附属表使用

			String yieldly = request.getParamValue("YIELDLY"); // 产地
			String licenseNo = request.getParamValue("LICENSE_NO"); // 车牌号
			String brandCode = request.getParamValue("BRAND_CODE0"); // 品牌
			String brandName = request.getParamValue("BRAND_NAME0");
			String modelCode = request.getParamValue("MODEL_CODE0"); // 车型
			String modelName0 = request.getParamValue("MODEL_NAME0");
			String seriesCode = request.getParamValue("SERIES_CODE0");// 车系
			String seriesName = request.getParamValue("SERIES_NAME0");
			String engineNo = request.getParamValue("ENGINE_NO");
			String gearboxNo = request.getParamValue("GEARBOX_NO0"); // 变速箱
			String rearaxleNo = request.getParamValue("REARAXLE_NO0"); // 后桥
			String transferNo = request.getParamValue("TRANSFER_NO0"); // 分动器
			String lineNo = request.getParamValue("LINE_NO");// 行号
			String yiedlyType = request.getParamValue("YIELDLY_TYPE"); //经销商选择的结算基地
			String quelityGrate = request.getParamValue("QUELITY_GRATE"); //质量等级
			quelityGrate = Constant.QUELITY_GRATE_01.toString();
			lineNo = "1";
			String roStartdate = request.getParamValue("RO_STARTDATE");// 工单开始时间
			String roEnddate = request.getParamValue("RO_ENDDATE");// 工单结束时间
			String guaranteeDate = request.getParamValue("GUARANTEE_DATE");// 保修开始时间
			String serveAdvisor = request.getParamValue("SERVE_ADVISOR");// 接待员
			String vin = request.getParamValue("VIN");// VIN
			String claimType = request.getParamValue("CLAIM_TYPE"); // 索赔类型
			
			String inMileage = request.getParamValue("IN_MILEAGE");

			String campaignCode = request.getParamValue("CAMPAIGN_CODE"); // 服务活动代码
			String campaignFee = request.getParamValue("CAMPAIGN_FEE"); // 服务活动费用

			String freeMAmount = request.getParamValue("FREE_M_AMOUNT"); // 免费保养次数
			String freeMPrice = request.getParamValue("FREE_M_PRICE"); // 免费保养费用

			String[] itemCodes = request.getParamValues("ITEM_CODE"); // 其他项目代码
			String[] itemNames = request.getParamValues("ITEM_NAME"); // 其他项目名称
			String[] itemAmounts = request.getParamValues("ITEM_AMOUNT");// 其他项目金额
			String[] itemRemarks = request.getParamValues("ITEM_REMARK");// 备注
			
			String partDown = request.getParamValue("ACTIVITY_AMOUNT_PARTS");//材料费打折
			String labourDown = request.getParamValue("ACTIVITY_AMOUNT_LABOURS");//工时费打折
			boolean deal = DBLockUtil.lock(logonUser.getDealerCode(), DBLockUtil.BUSINESS_TYPE_20);
			boolean inFlag=true;
			String isFixing="0";
			String IS_AUDIT="0";
			
			if(inFlag){
				if(deal){
					/**
					 * 根据索赔类型，确定那些需要值空。 
					 */
					if (Utility.getInt(claimType) == Constant.CLA_TYPE_01) { // 一般索赔
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
					} else if (Utility.getInt(claimType) == Constant.CLA_TYPE_10) { // 特殊服务
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
					} else if (Utility.getInt(claimType) == Constant.CLA_TYPE_12) { // 急件
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
					} else if (Utility.getInt(claimType) == Constant.CLA_TYPE_02) {// 免费保养
						
						itemCodes = null;
					} else if (Utility.getInt(claimType) == Constant.CLA_TYPE_03) {// 追加索赔
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
					} else if (Utility.getInt(claimType) == Constant.CLA_TYPE_04) {// 重复修理索赔
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
					} else if (Utility.getInt(claimType) == Constant.CLA_TYPE_05) {// 零件索赔更换
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
					} else if (Utility.getInt(claimType) == Constant.CLA_TYPE_06) {// 服务活动
						//如果此服务活动是送免费保养，刚置保养费用为0
						freeMPrice = null; // 免费保养费用
						
					} else if (Utility.getInt(claimType) == Constant.CLA_TYPE_07) {// 售前维修
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
					} else if (Utility.getInt(claimType) == Constant.CLA_TYPE_08) {// 保外索赔
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
					}else if (Utility.getInt(claimType) == Constant.CLA_TYPE_09) {//外出维修
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
						
					}else if (Utility.getInt(claimType) == Constant.CLA_TYPE_11) {//PDI
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
						
					}else if (Utility.getInt(claimType) == Constant.CLA_TYPE_13) {//配件索赔
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
						
					}else{
						campaignCode = null; // 服务活动代码
						campaignFee = null; // 服务活动费用
						isFixing = null; // 服务活动是否固定费用1为固定费用

						freeMAmount = null; // 免费保养次数
						freeMPrice = null; // 免费保养费用
					}
					double winterAmountTemp=0.0d;
					Double campaignFeeDb = Utility.getDouble(campaignFee);
					//=====================添加了保养费用加上冬季保养
					if(Utility.getInt(claimType) == Constant.CLA_TYPE_02){
						String roStartdateTemp=roStartdate.substring(0, roStartdate.length()-5);
						BigDecimal winterAmount = dao.getWinterAmount(getCurrDealerId(), roStartdateTemp);
						winterAmountTemp = winterAmount.doubleValue();
					}
					//=====================
					Double freeMPriceDb = Utility.getDouble(freeMPrice)+winterAmountTemp;
					Long claimId = Utility.getLong(SequenceManager.getSequence("")); // 产生一个索赔ID
					String claimNo=""; //一家经销商产生的索赔单号是连续的
					String fkNo=""; //一家经销商产生的反馈单是连续的
					TtAsWrApplicationPO tawap = new TtAsWrApplicationPO();
					 List<Map<String, Object>> listRo = dao.getRepairOrder(roId);
					//取得工单中的开始里程
					 TtAsRepairOrderPO op =  new TtAsRepairOrderPO();
					 op.setId(Long.valueOf(roId));
					 op = (TtAsRepairOrderPO) dao.select(op).get(0);
					
					tawap.setId(claimId);
					tawap.setStartMileage(Utility.getDouble(op.getInMileage()+""));
					/**
					 * 判断是否是一级经销商，如果不是则将经销商ID 改为一级经销商,同时保存二级经销商信息到工单表
					 */
					TmDealerPO dealer = new TmDealerPO();
					dealer.setDealerId(Long.valueOf(dealerId));
					dealer = (TmDealerPO) dao.select(dealer).get(0);
					if(Constant.DEALER_LEVEL_02.toString().equalsIgnoreCase(dealer.getDealerLevel().toString().trim())){
						tawap.setSecondDealerCode(dealer.getDealerCode());
						tawap.setSecondDealerId(dealer.getDealerId());
						tawap.setSecondDealerName(dealer.getDealerName());
					}
					long dealerid = Long.valueOf(listRo.get(0).get("ID").toString());
					tawap.setDealerId(dealerid);//
					TmDealerPO tmp = new TmDealerPO();
					tmp.setDealerId(dealerid);
					List<TmDealerPO> lists = dao.select(tmp);
					if(lists != null && lists.size() == 1) {
						tawap.setDealerName(lists.get(0).getDealerName());
						tawap.setDealerShortname(lists.get(0).getDealerShortname());
					}
					
					claimNo = Utility.GetClaimBillNo("",logonUser.getDealerCode(),"S");
					claimNo = claimNo.substring(1,claimNo.length() );
					System.out.println(claimType);
					//重新分开保存保养的工时费和材料费
					if (Utility.getInt(claimType) == Constant.CLA_TYPE_02) { 
						List<Map<String,Object>> list =dao.getFreeAmount(vin);
						if(list!=null&&list.size()>0){
							tawap.setFreeLabourAmount(Double.valueOf(String.valueOf(list.get(0).get("LABOUR_PRICE"))));
							tawap.setFreePartAmount(Double.valueOf(String.valueOf(list.get(0).get("PART_PRICE"))));
						}else{
							tawap.setFreeLabourAmount(0.0);
							tawap.setFreePartAmount(0.0);
						}
					}else {
						tawap.setFreeLabourAmount(0.0);
						tawap.setFreePartAmount(0.0);
					}
					fkNo = Utility.GetClaimBillNo("",logonUser.getDealerCode(),"FK");
					if(!Utility.testString(claimNo)){
						throw new Exception("索赔单号生成失败!");
					}
					claimNo = claimNo.toUpperCase();
					tawap.setClaimNo(claimNo);
					tawap.setFkNo(fkNo);
					tawap.setRoNo(roNo);
					tawap.setBalanceYieldly(Integer.parseInt(yiedlyType));
					tawap.setLineNo(Utility.getLong(lineNo));
					tawap.setVin(vin);
					tawap.setVerseon(0);
					tawap.setRoStartdate(Utility.getDate(roStartdate, 3));
					tawap.setRoEnddate(Utility.getDate(roEnddate, 3));
					tawap.setGuaranteeDate(Utility.getDate(guaranteeDate, 3));
					tawap.setServeAdvisor(serveAdvisor);
					tawap.setClaimType(Utility.getInt(claimType));
					tawap.setLicenseNo(licenseNo);
					tawap.setBrandCode(brandCode);
					tawap.setBrandName(brandName);
					tawap.setModelCode(modelCode);
					tawap.setModelName(modelName0);
					tawap.setSeriesCode(seriesCode);
					tawap.setSeriesName(seriesName);
					tawap.setEngineNo(engineNo);
					tawap.setGearboxNo(gearboxNo);
					tawap.setRearaxleNo(rearaxleNo);
					tawap.setTransferNo(transferNo);
					tawap.setPartDown(null);
					tawap.setLabourDown(null);
					tawap.setYieldly(yieldly);
					tawap.setQuelityGrate(Integer.parseInt(quelityGrate));
					tawap.setApplicationDel(Constant.RO_APP_STATUS_01.toString());
					tawap.setIsAudit(Integer.valueOf(IS_AUDIT));		
					
					tawap.setCampaignCode(campaignCode); // 服务活动代码
					tawap.setIsFixing(Utility.getInt(isFixing)); // 是否固定费用1为固定
					tawap.setSubmitTimes(0);
					tawap.setInMileage(Utility.getDouble(inMileage));
					tawap.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_01);
					tawap.setOemCompanyId(companyId);
					tawap.setCreateBy(logonUser.getUserId());
					tawap.setCreateDate(date);
					 int temp =2;//判断索赔单是否需要旧件审核标识
					if(pids!=null && !"".equalsIgnoreCase(pids)){//如果得到的主因件ID不为空,说明不是PDI或者保养
						String[] ids=pids.split(",");
						for(int i=0;i<ids.length;i++){//循环主因件,进行插入
							/**
							 * 工时
							 */
							String[] wrLabourCodes = request.getParamValues("WR_LABOURCODE"+ids[i]); // 工时代码
							String[] wrLabournames = request.getParamValues("WR_LABOURNAME"+ids[i]); // 工时名称
							String[] labourHours = request.getParamValues("LABOUR_HOUR"+ids[i]); // 工时数
							String[] labourPrices = request.getParamValues("LABOUR_PRICE"+ids[i]); // 工时单价
							String[] labourAmounts = request.getParamValues("LABOUR_AMOUNT"+ids[i]); // 工时总金额
							String[] malIds = request.getParamValues("MAL_ID"+ids[i]); // 故障名称ID
							
							/**
							 * 配件
							 */
							String[] partCodes = request.getParamValues("PART_CODE"+ids[i]); // 新件代码
							String[] partNames = request.getParamValues("PART_NAME"+ids[i]); // 新件名称
							String[] downPartCodes = request.getParamValues("DOWN_PART_CODE"+ids[i]); // 换下件代码
							String[] downPartNames = request.getParamValues("DOWN_PART_NAME"+ids[i]); // 换下件名称
							String[] supplierCodes = request.getParamValues("PRODUCER_CODE"+ids[i]);// 旧件供应商代码
							String[] supplierNames = request.getParamValues("PRODUCER_NAME"+ids[i]); // 旧件供应商名称
							String[] quantitys = request.getParamValues("PART_QUANTITY"+ids[i]); // 旧件数量
							String[] prices = request.getParamValues("PART_PRICE"+ids[i]); // 旧件单价
							String[] amounts = request.getParamValues("PART_AMOUNT"+ids[i]); // 旧件总金额
							String[] respons = request.getParamValues("RESPONS_NATURE"+ids[i]);//责任性质
							String[] partUseType = request.getParamValues("PART_USE_TYPE"+ids[i]); //旧件使用类型
							String[] labourCode = request.getParamValues("LABOUR_CODE"+ids[i]); // 旧件对应工时代码
							String[] mainPartCode = request.getParamValues("MAIN_PART_CODE"+ids[i]);//旧件对应主因件
							String trableDesc = request.getParamValue("ADUIT_REMARK"+ids[i]);//主因件故障描述
							String trableReson = request.getParamValue("TROUBLE_REASON"+ids[i]);//主因件故障原因
							String dealMethod=request.getParamValue("DEAL_METHOD"+ids[i]);//主因件处理结果
							
							String[] zfRono = request.getParamValues("ZF_RONO"+ids[i]); //自费工单
							
							if(wrLabourCodes!=null){
								for(int k=0;k<wrLabourCodes.length;k++){
									TtAsWrLabouritemPO l = new TtAsWrLabouritemPO();
									l.setId(claimId);
									l.setWrLabourcode(wrLabourCodes[k]);
									List<TtAsWrLabouritemPO> lList=dao.select(l);
									if(lList.size()<1){//由于存在多个配件对应一个工时的情况，所以这里要先判断一次
										labourHoursMain += Utility.getFloat(labourHours[k]);//工时数
										// 累加总工时金额
										labourAmountMain += Utility.getDouble(labourAmounts[k]);//工时金额
										
										TtAsWrLabouritemPO tawlp = new TtAsWrLabouritemPO();
										
										tawlp.setIsMainlabour(Constant.IS_MAIN_TROUBLE);
										
										tawlp.setId(claimId);
										tawlp.setLabourId(Utility.getLong(SequenceManager.getSequence("")));
										tawlp.setTroubleCode(malIds[k]);
										tawlp.setTroubleType(malIds[k]);
										
										tawlp.setWrLabourcode(wrLabourCodes[k]);
										tawlp.setWrLabourname(wrLabournames[k]);
										tawlp.setLabourCode(wrLabourCodes[k]);
										tawlp.setLabourName(wrLabournames[k]);
										
										tawlp.setLabourHours(Utility.getFloat(labourHours[k]));//工时基础信息
										tawlp.setLabourPrice(Utility.getFloat(labourPrices[k]));
										tawlp.setLabourAmount(Utility.getDouble(labourAmounts[k]));
										tawlp.setLabourQuantity(Utility.getFloat(labourHours[k]));
										
										tawlp.setApplyPrice(Utility.getDouble(labourPrices[k]));//工时申请信息
										tawlp.setApplyQuantity(Utility.getDouble(labourHours[k]));
										tawlp.setApplyAmount(Utility.getDouble(labourAmounts[k]));
										
										
										tawlp.setBalanceAmount(Utility.getDouble(labourAmounts[k]));//工时结算信息
										tawlp.setBalancePrice(Utility.getDouble(labourPrices[k]));
										tawlp.setBalanceQuantity(Utility.getFloat(labourHours[k]));
										tawlp.setIsFore(1); //授权
										
										tawlp.setPayType(Constant.PAY_TYPE_02);
										tawlp.setIsClaim(Constant.IF_TYPE_YES);
										tawlp.setLabourQuantityHidden(Utility.getDouble("0"));
										tawlp.setCreateBy(logonUser.getUserId());
										tawlp.setCreateDate(date);
										tawap.setLabourPrice(Utility.getDouble(labourPrices[k]));
										dao.insert(tawlp);
								}
								}
							}//工时结束
							
								if (partCodes != null) {
									for (int k = 0; k < partCodes.length; k++) {
										partsCount += Utility.getDouble(quantitys[k]);//配件数量
										partsAmountMain += Utility.getDouble(amounts[k]);// 累计配件金额
										
										TtAsWrPartsitemPO tawp = new TtAsWrPartsitemPO();
										tawp.setId(claimId);
										tawp.setPartId(Utility.getLong(SequenceManager.getSequence("")));
										tawp.setWrLabourcode(labourCode[k]);
										
										tawp.setPartCode(partCodes[k]);//新件/旧件信息
										tawp.setPartName(partNames[k]);
										tawp.setDownPartCode(downPartCodes[k]);
										tawp.setDownPartName(downPartNames[k]);
										tawp.setProducerCode(supplierCodes[k]);
										tawp.setProducerName(supplierNames[k]);
										tawp.setDownProductCode(supplierCodes[k]);
										tawp.setDownProductName(supplierNames[k]);
										
										tawp.setResponsibilityType(Integer.parseInt(respons[k].trim()));
										tawp.setMainPartCode(mainPartCode[k]);
										tawp.setQuantity(Utility.getFloat(quantitys[k]));//数量金额
										tawp.setPrice(Utility.getDouble(prices[k]));
										tawp.setAmount(Utility.getDouble(amounts[k]));
										tawp.setBalanceAmount(Utility.getDouble(amounts[k]));
										tawp.setBalanceQuantity(Utility.getFloat(quantitys[k]));
										tawp.setBalancePrice(Utility.getDouble(prices[k]));//结算价格
										tawp.setApplyAmount(Utility.getDouble(amounts[k]));//申请金额
										tawp.setApplyPrice(Utility.getDouble(prices[k]));//申请价格
										tawp.setApplyQuantity(Utility.getDouble(quantitys[k]));//申请数量
										
										tawp.setZfRono(zfRono[k]);//自费工单
										
										tawp.setPayType(Constant.PAY_TYPE_02);
										
										if(Constant.RESPONS_NATURE_STATUS_01.toString().equalsIgnoreCase(respons[k])){
											tawp.setIsMainpart(Constant.IS_MAIN_TROUBLE);
											//tawp.setBMpCode(partCodes[k]);
										}else{
											tawp.setIsMainpart(Constant.IS_NOT_MAIN_TROUBLE);
										//	tawp.setBMpCode(mainPartCode[k]);
										}
										tawp.setIsClaim(Long.valueOf("10041001"));
										tawp.setIsNotice(Constant.IF_TYPE_NO);
										tawp.setIsFore(1); //授权
										tawp.setCreateBy(logonUser.getUserId());
										tawp.setCreateDate(date);
										tawp.setIsGua(1);
										tawp.setRemark(trableDesc);
										tawp.setTroubleReason(trableReson);
										tawp.setDealMethod(dealMethod);
										tawp.setReturnNum(0f);
										if(Constant.PART_USE_TYPE_01.toString().equalsIgnoreCase(partUseType[k])){
											tawp.setPartUseType(0);
										}else{
											tawp.setPartUseType(1);
										}
										tawp.setAuditStatus(Constant.PART_AUDIT_STATUS_01);
										tawp.setIsOldClaimPrint(Constant.IF_TYPE_NO);
										
										String part = downPartCodes[k];
										if("00-000".equalsIgnoreCase(downPartCodes[k])&&"95411001".equalsIgnoreCase(yiedlyType)){//如果是无配件，则需要将配件代码修改为数据库中的无配件代码
											part="00-000";
										}else if("00-0000".equalsIgnoreCase(downPartCodes[k])&&"95411002".equalsIgnoreCase(yiedlyType)){
											part="00-0000";
										}
										
										TmPtPartBasePO bp = new TmPtPartBasePO();
										bp.setPartCode(part);
										bp = (TmPtPartBasePO) dao.select(bp).get(0);
										
										
										tawp.setIsReturn(bp.getIsReturn());//新增旧件是否回运字段表示
										tawp.setRealPartId(bp.getPartId());
										
										boolean flagIsReturn = Constant.IS_RETURN_01.toString().equalsIgnoreCase(bp.getIsReturn().toString());//判断是否回运
										boolean flagRepair = Constant.PART_USE_TYPE_01.toString().equalsIgnoreCase(partUseType[k]);//判断是否维修
										boolean flagNothingPart = "00-000".equalsIgnoreCase(downPartCodes[k]);//判断是否
										if(flagIsReturn&&flagRepair){//需要回运但是是维修，设置新增旧件是否回运字段 为null
											tawp.setIsReturn(null);
										}
										if(flagIsReturn&&!flagNothingPart&&!flagRepair){//回运+不是维修+不是无配件
											temp=0;
										}
										dao.insert(tawp);
									}
								}//配件结束
								
								String[] itemCodes1 = request.getParamValues("ItemCode"+ids[i]); // 项目代码
								String[] itemNames1 = request.getParamValues("ItemName"+ids[i]); // 项目名称
								String[] itemAmounts1 = request.getParamValues("ItemAmount"+ids[i]);// 申请金额
								String[] itemRemarks1 = request.getParamValues("ItemRemark"+ids[i]);// 申请备注
								String[] itemMainCode = request.getParamValues("ItemMainCode"+ids[i]);// 对应主因件件
								if(itemCodes1!=null){
									for(int k=0;k<itemCodes1.length;k++){
										netItemAmount += Utility.getDouble(itemAmounts1[k]);// 累计其他项目总金额
										TtAsWrNetitemPO tawp = new TtAsWrNetitemPO();
										tawp.setId(claimId);
										tawp.setNetitemId(Utility.getLong(SequenceManager.getSequence("")));
										tawp.setItemCode(itemCodes1[k]);
										tawp.setItemDesc(itemNames1[k]);
										tawp.setAmount(Utility.getDouble(itemAmounts1[k]));
										tawp.setApplyAmount(Utility.getDouble(itemAmounts1[k]));
										tawp.setRemark(itemRemarks1[k]);
										tawp.setBalanceAmount(Utility.getDouble(itemAmounts1[k]));
										tawp.setPayType(Constant.PAY_TYPE_02);
										tawp.setIsFore(1); //授权
										tawp.setCreateBy(logonUser.getUserId());
										tawp.setMainPartCode(itemMainCode[k]);
										tawp.setCreateDate(date);
										dao.insert(tawp);
									}
								}//其他项目结束
								String[] bcapply_price =request.getParamValues("apply_price"+ids[i]); //申请金额
								String[] pass_price =request.getParamValues("pass_price"+ids[i]); //审核金额
								String[] bcREASON =request.getParamValues("REASON"+ids[i]); //申请原因
								String[] COM_MAIN_CODE =request.getParamValues("COM_MAIN_CODE"+ids[i]); //对应主因件
								String[] suupCode = request.getParamValues("COM_SUPP_CODE"+ids[i]);//供应商代码
								String[] PKID = request.getParamValues("PKID"+ids[i]);//PKID
								if(PKID!=null){
									for(int k=0;k<PKID.length;k++){
										compensationMoney += Utility.getDouble(bcapply_price[k]);// 累计补偿费总金额
										TtAsWrCompensationAppPO compensation=new TtAsWrCompensationAppPO();
										compensation.setRoNo(roNo);
										compensation.setClaimNo(claimNo);
										compensation.setCreateDate(new Date());
										compensation.setPartCode(COM_MAIN_CODE[k]);
										compensation.setPassPrice(Utility.getDouble(pass_price[k]));
										compensation.setApplyPrice(Utility.getDouble(bcapply_price[k]));
										compensation.setPkid(super.getSeq());
										compensation.setSupplierCode(suupCode[k]);
										compensation.setReason(bcREASON[k]);
										dao.insert(compensation);
									}
								}//补偿费结束
								
								String[] WORKHOUR_CODE = request.getParamValues("WORKHOUR_CODE"+ids[i]);//辅料代码
								String[] WORKHOUR_NAME = request.getParamValues("WORKHOUR_NAME"+ids[i]);//辅料名称
								String[] WORKHOUR_PRICE = request.getParamValues("WORKHOUR_PRICE"+ids[i]);//辅料价格
								String[] WORKHOUR_MAIN_CODE = request.getParamValues("WORKHOUR_MAIN_CODE"+ids[i]);//辅料主因件
								if(WORKHOUR_CODE!=null){
									for(int k=0;k<WORKHOUR_CODE.length;k++){
										accPrice += Utility.getDouble(WORKHOUR_PRICE[k]);// 累计福利费总金额
										
										TtClaimAccessoryDtlPO po = new TtClaimAccessoryDtlPO();
										po.setClaimNo(claimNo);
										po.setWorkhourCode(WORKHOUR_CODE[k]);
										po.setWorkhourName(WORKHOUR_NAME[k]);
										po.setPrice(Utility.getDouble(WORKHOUR_PRICE[k]));
										po.setMainPartCode(WORKHOUR_MAIN_CODE[k]);
										po.setId(Long.parseLong(SequenceManager.getSequence("")));
										po.setAppPrice(Utility.getDouble(WORKHOUR_PRICE[k]));
										dao.insert(po);
								}
							}//辅料结束
						}
						}else{//如果得到的主因件代码为空,则说明是PDI或者保养。否则就是错误数据
						// 其他项目插入
						if (itemCodes != null) {
							for (int i = 0; i < itemCodes.length; i++) {
								netItemAmount += Utility.getDouble(itemAmounts[i]);// 累计其他项目总金额
								TtAsWrNetitemPO tawp = new TtAsWrNetitemPO();
								tawp.setId(claimId);
								tawp.setNetitemId(Utility.getLong(SequenceManager.getSequence("")));
								tawp.setItemCode(itemCodes[i]);
								tawp.setItemDesc(itemNames[i]);
								tawp.setAmount(Utility.getDouble(itemAmounts[i]));
								/**addUser:xiongchuan addDate:2010-12-14 **/
								tawp.setApplyAmount(Utility.getDouble(itemAmounts[i]));
								/**endUser:xiongchuan endDate:2010-12-14 **/
								tawp.setRemark(itemRemarks[i]);
								tawp.setBalanceAmount(Utility.getDouble(itemAmounts[i]));
								tawp.setPayType(Constant.PAY_TYPE_02);
								tawp.setIsFore(1); //授权
								tawp.setCreateBy(logonUser.getUserId());
								tawp.setMainPartCode("-1");
								tawp.setCreateDate(date);
								dao.insert(tawp);
							}
						}
					}
					/**
					 * 子项已经处理完成,现在开始保存主表中的子项统计数据
					 */
					//总费用
					repairTotal = labourAmountMain
							+ partsAmountMain 
							+ netItemAmount
							+ campaignFeeDb 
							+ freeMPriceDb
							+ accPrice
							+compensationMoney;
					
					
					tawap.setLabourHours(labourHoursMain);//工时
					tawap.setLabourAmount(labourAmountMain);
					tawap.setApplyLabourAmount(labourAmountMain);//经销商申请工时金额
					tawap.setBalanceLabourAmount(labourAmountMain);
					
					// 配件插入
					tawap.setIsOldAudit(temp);
					tawap.setPartsCount(partsCount);
					BigDecimal b = new BigDecimal(Double.toString(partsAmountMain));   
					BigDecimal one = new BigDecimal("1"); 
					tawap.setPartAmount(b.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue()); 
					tawap.setBalancePartAmount(partsAmountMain);
					tawap.setApplyPartAmount(partsAmountMain);//经销商申请配件金额
					//其他项目
					tawap.setNetitemAmount(netItemAmount);
					tawap.setApplyNetitemAmount(netItemAmount);//经销商其他项目费
					tawap.setBalanceNetitemAmount(netItemAmount);
					
					//辅料
					tawap.setAccessoriesPrice(accPrice);
					//补偿
					tawap.setCompensationMoney(compensationMoney);
					//保养
					tawap.setFreeMPrice(freeMPriceDb);
					tawap.setFreeMAmount((int) Utility.getDouble(freeMAmount)); // 免费保养次数
					tawap.setBalanceFreeMAmount(Utility.getDouble(freeMAmount));//经销商结算保养费用总金额
					tawap.setApplyFreeMAmount(Utility.getDouble(freeMAmount));//经销商申请保养费用总金额
					// 服务活动费用
					tawap.setCampaignFee(Utility.getDouble(campaignFee)); 
					tawap.setApplyCampaignFee(Utility.getDouble(campaignFee));//经销商申请服务活动费用金额
					tawap.setBalanceCampaignFee(Utility.getDouble(campaignFee));//经销商结算服务活动费用总金额
		           
					tawap.setApplyRepairTotal(repairTotal);//经销商申请总金额
					tawap.setRepairTotal(repairTotal);
					tawap.setBalanceAmount(repairTotal);
					tawap.setGrossCredit(repairTotal * 1);// 这里插入总金额 税额暂时设定为1
					
					if(tawap.getClaimType().equals(Constant.CLA_TYPE_02)){
						if(tawap.getBalanceAmount()<1){
							throw new Exception("保养费不能为0");
						}
					}
					tawap.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_01);
					
					// 插入到索赔申请单表
					dao.insert(tawap);
					//将工单的附件转换到索赔单下面
					dao.insetFile(logonUser.getUserId(), tawap.getId(), Long.valueOf(roId));
					String ywzj = tawap.getId().toString();
					String[] fjids = request.getParamValues("fjids");
					FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);
					act.setOutData("msg", "保存成功!");
				}else {
					act.setOutData("msg", "保存失败,请不要同时创建索赔单或者操作不要太频繁!");
				}
			}
				DBLockUtil.freeLock(logonUser.getDealerCode(), DBLockUtil.BUSINESS_TYPE_20);
			
			act.setForword(CLAIM_BILL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
		DBLockUtil.freeLock(roNo, DBLockUtil.BUSINESS_TYPE_13);
	}

	/**
	 * 
	 * @Title: applicationInsert
	 * @Description: TODO(修改索赔)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void applicationUpdate() {
		AclUserBean logonUser = loginUser;
		Date date = new Date();
		try {
			String id = request.getParamValue("ID");
			String roNo = request.getParamValue("RO_NO"); // 工单号
			String lineNo = request.getParamValue("LINE_NO");// 行号
			lineNo = "1";
			String roStartdate = request.getParamValue("RO_STARTDATE");// 工单开始时间
			String roEnddate = request.getParamValue("RO_ENDDATE");// 工单结束时间
			String guaranteeDate = request.getParamValue("GUARANTEE_DATE");// 保修开始时间
			String serveAdvisor = request.getParamValue("SERVE_ADVISOR");// 接待员
			String vin = request.getParamValue("VIN");// VIN
			String remark = request.getParamValue("APP_REMARK");// 申请备注
			String claimType = request.getParamValue("CLAIM_TYPE"); // 索赔类型
			String inMileage = request.getParamValue("IN_MILEAGE");
			String yieldly = request.getParamValue("YIELDLY"); // 产地
			String licenseNo = request.getParamValue("LICENSE_NO"); // 车牌号
			String brandCode = request.getParamValue("BRAND_CODE0"); // 品牌
			String brandName = request.getParamValue("BRAND_NAME0");
			String modelCode = request.getParamValue("MODEL_CODE0"); // 车型
			String modelName = request.getParamValue("MODEL_NAME0");
			String seriesCode = request.getParamValue("SERIES_CODE0");// 车系
			String seriesName = request.getParamValue("SERIES_NAME0");
			String engineNo = request.getParamValue("ENGINE_NO");
			String gearboxNo = request.getParamValue("GEARBOX_NO0"); // 变速箱
			String rearaxleNo = request.getParamValue("REARAXLE_NO0"); // 后桥
			String transferNo = request.getParamValue("TRANSFER_NO0"); // 分动器
			
			String pids = request.getParamValue("pids");//获取页面主因件的数据ID
			//String[] downPartCode = request.getParamValues("oldPartCode"); 
			//String[] upPartCode = request.getParamValues("PART_CODE"); 


			// 索赔单ID
			Long claimId = Utility.getLong(id);
			
			
			TtAsWrApplicationPO tawap0 = new TtAsWrApplicationPO();
			tawap0.setId(claimId);
			
			TtAsWrApplicationPO tawap = new TtAsWrApplicationPO();
			tawap.setId(claimId);


			tawap.setRoNo(roNo);
			tawap.setLineNo(Utility.getLong(lineNo));
			tawap.setVin(vin);
			tawap.setRoStartdate(Utility.getDate(roStartdate, 3));
			tawap.setRoEnddate(Utility.getDate(roEnddate, 3));
			tawap.setGuaranteeDate(Utility.getDate(guaranteeDate, 1));
			tawap.setServeAdvisor(serveAdvisor);
			tawap.setClaimType(Utility.getInt(claimType));
			tawap.setLicenseNo(licenseNo);
			tawap.setBrandCode(brandCode);
			tawap.setBrandName(brandName);
			tawap.setModelCode(modelCode);
			tawap.setModelName(modelName);
			tawap.setSeriesCode(seriesCode);
			tawap.setSeriesName(seriesName);
			tawap.setEngineNo(engineNo);
			tawap.setGearboxNo(gearboxNo);
			tawap.setRearaxleNo(rearaxleNo);
			tawap.setTransferNo(transferNo);
			tawap.setYieldly(yieldly);
			tawap.setIsAudit(Integer.valueOf(0));
			tawap.setRemark(remark);
			tawap.setInMileage(Utility.getDouble(inMileage));
			tawap.setUpdateBy(logonUser.getUserId());
			tawap.setUpdateDate(date);
			//将索赔单的修改提交次数加1
			String sql = " update tt_as_wr_application a set a.submit_times = nvl(a.submit_times,0)+1 where a.id= "+claimId;
			dao.update(sql, null);
		/**
		 * 
		 * 由于服务站在修改界面无法修改金额，所以保存时无需考虑配件，工时等其他项目
		 * @author KFQ
		 * 2014-9-24 16:48
		 */
			//更新主因件故障描述等字段
			if(Utility.testString(pids)){//如果有值,说明页面上有主因件出现
				String[] str = pids.split(",");
				for(int i=0;i<str.length;i++){
					if(!"".equalsIgnoreCase(str[i])){
						String REMARKs = request.getParamValue("REMARK"+str[i]);
						String TROUBLE_REASON = request.getParamValue("TROUBLE_REASON"+str[i]);
						String DEAL_METHOD = request.getParamValue("DEAL_METHOD"+str[i]);
						TtAsWrPartsitemPO p =new TtAsWrPartsitemPO();
						TtAsWrPartsitemPO p2 =new TtAsWrPartsitemPO();
						p.setPartId(Long.valueOf(str[i]));
						p2.setRemark(REMARKs);
						p2.setTroubleReason(TROUBLE_REASON);
						p2.setDealMethod(DEAL_METHOD);
						dao.update(p, p2);
					}
				}
			}
			//添加照片
			String ywzj = tawap.getId().toString();
			String[] fjids = request.getParamValues("fjid");

			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			act.setOutData("msg", "修改成功!");
			
			act.setForword(CLAIM_BILL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: getOutDetailByRoNo 
	* @Description: TODO(根据RO_NO取得外出维修详细) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void getOutDetailByRoNo(){
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		try {
			
			String roNo = request.getParamValue("roNo");
			TtAsWrOutrepairPO tawo = new TtAsWrOutrepairPO();
			if (Utility.testString(roNo)) {
				tawo.setRoNo(roNo);
				List<TtAsWrOutrepairPO> ls = dao.select(tawo);
				if (ls!=null) {
					if (ls.size()>0) {
						tawo = ls.get(0);
					}
				}
			}
			act.setOutData("outBean", tawo);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: verifyDupRo
	 * @Description: TODO(验证索赔单中的工单号和行号，一个工单号加一个行号可以唯一标识一个索赔单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void verifyDupRo() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		try {
			
			String roNo = request.getParamValue("roNo"); // 工单号
			String lineNo = request.getParamValue("lineNo");// 行号
			Boolean dup = false; // 默认没有重复
			TtAsWrApplicationPO tawap = new TtAsWrApplicationPO();
			if (Utility.testString(roNo)) {
				tawap.setRoNo(roNo);
				if (Utility.testString(lineNo)) {
					tawap.setLineNo(Utility.getLong(lineNo));
				}
				List<TtAsWrApplicationPO> ls = dao.select(tawap);
				if (ls != null) {
					if (ls.size() > 0) {
						dup = true; // 工单有重复
					}
				}
			}
			/*
			 * if (Utility.testString(lineNo)) {
			 * tawap.setLineNo(Utility.getInt(lineNo)); List<TtAsWrApplicationPO>
			 * ls1 = dao.select(tawap); if (ls1 != null) { if (ls1.size() > 0) {
			 * liDu = true;// 行号有重复 } } }
			 */
			act.setOutData("dup", dup);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: verifyVINExist 
	* @Description: TODO(判断系统中是否存在vin) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void verifyVINExist() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		try {
			
			String vin = request.getParamValue("VIN"); // VIN
			Boolean exist = false; // 默认没有重复
			TmVehiclePO tawap = new TmVehiclePO();
			if (Utility.testString(vin)) {
				tawap.setVin(vin);
				/*****add by liuxh 20131108判断车架号不能为空*****/
				CommonUtils.jugeVinNull(vin);
				/*****add by liuxh 20131108判断车架号不能为空*****/
				List<TmVehiclePO> ls = dao.select(tawap);
				if (ls != null) {
					if (ls.size() > 0) {
						exist = true; // 存在
					}
				}
			}
			/*
			 * if (Utility.testString(lineNo)) {
			 * tawap.setLineNo(Utility.getInt(lineNo)); List<TtAsWrApplicationPO>
			 * ls1 = dao.select(tawap); if (ls1 != null) { if (ls1.size() > 0) {
			 * liDu = true;// 行号有重复 } } }
			 */
			act.setOutData("dup", exist);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roItemQuery
	 * @Description: TODO(工单中工时，配件，其他项目查询)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roItemQuery() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID

		try {
			
			String roId = request.getParamValue("roId");// 活动ID
			String roNo = request.getParamValue("ro_no");// 活动ID
			String modelId = request.getParamValue("modelId");
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("roId", roId);
			PageResult<TtAsRepairOrderExtPO> ps = dao.queryRepairOrder(map,
					Constant.PAGE_SIZE, 1);
			List<TtAsRepairOrderExtPO> ls = ps.getRecords();
			TtAsRepairOrderExtPO tarp = new TtAsRepairOrderExtPO();
			if (ls != null) {
				if (ls.size() > 0) {
					tarp = ls.get(0);
				}
			}
			TtAsRepairOrderPO bean = new TtAsRepairOrderPO();
			bean.setId(Utility.getLong(roId));
			List<TtAsRepairOrderPO> ls0 = dao.select(bean);
			bean = ls0.get(0);
			TmVehiclePO tvp = new TmVehiclePO();
			tvp.setVin(bean.getVin());
			
			/*****add by liuxh 20131108判断车架号不能为空*****/
			CommonUtils.jugeVinNull(bean.getVin());
			/*****add by liuxh 20131108判断车架号不能为空*****/
			
			List<TmVehiclePO> ls1 = dao.select(tvp);
			if (ls1!=null&&ls1.size()>0) {
			tvp = ls1.get(0);
			}
			map.put("roNo", bean.getRoNo());
			map.put("vin", bean.getVin());
			map.put("yieldly", bean.getBalanceYieldly().toString());
			map.put("modelId",tvp.getModelId()==null?"-1":tvp.getModelId().toString());
			map.put("companyId", companyId.toString());
			List<TtAsActivityBean> ActivityBeanList = dao.getWorkingHoursInfoList(roId, map,logonUser);// 活动工时
			List<TtAsActivityBean> ActivityPartsList = dao.getPartsList(roId,map,logonUser);// 活动配件

			List<FsFileuploadPO> attachLs = dao.queryAttById(roId);// 取得附件
			List<TtAsActivityBean> ActivityNetItemList = dao.getNetItemList(roId, map, logonUser);
			// List<TtAsActivityBean>

			List<Map<String, Object>> accessoryDtlList = dao.getAccessoryDtl(roNo);
			List<Map<String, Object>> workHourCodeMap = dao.getWorkhoursCode();
			act.setOutData("workHourCodeMap", workHourCodeMap);
			act.setOutData("accessoryDtlList", accessoryDtlList);
			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoney(roNo);
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================
			
			act.setOutData("ActivityBeanList", ActivityBeanList);
			act.setOutData("ActivityPartsList", ActivityPartsList);
			act.setOutData("ActivityNetItemList", ActivityNetItemList);
			act.setOutData("attachLs", attachLs);
			request.setAttribute("roId", roId);
			request.setAttribute("roNo", roNo);
			request.setAttribute("roBean", tarp);
			
			act.setForword(RO_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动管理--活动项目");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: changeRoDetail
	 * @Description: TODO(工单带出工单基本信息到索赔单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void changeRoDetail() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		
		String roId = request.getParamValue("roId");// 活动ID
		
		int newPart=0;
		try { 
			if(null==roId||"null".equalsIgnoreCase(roId)){
				logger.error("changeRoDetail方法中roId为空");
				throw new BizException("roId为空!");
			}
			roId=CommonUtils.checkNull(roId);
			if("".equals(roId)){
				logger.error("changeRoDetail方法中roId为空");
				throw new BizException("roId为空!");
			}
			// 计算服务活动打折金额,如果没有活动，则算出来为0 
			Double actityAmountLabour =0.0;
			Double actityAmountPart = 0.0;
			DecimalFormat format = new DecimalFormat("###0.00");
			TtAsRoRepairPartPO pp = new TtAsRoRepairPartPO();
			pp.setRoId(Long.valueOf(roId));
			pp.setPartUseType(Constant.PART_USE_TYPE_02);
			pp.setPayType(Constant.PAY_TYPE_01);
			List<TtAsRoRepairPartPO> plist = dao.select(pp);
			TtAsRoAddItemPO ip = new TtAsRoAddItemPO();
			ip.setRoId(Long.valueOf(roId));
			ip.setPayType(Constant.PAY_TYPE_02);
			ip.setAddItemCode(Constant.SERVICEACTIVITY_CAR_cms_07.toString());
			List<TtAsRoAddItemPO> list2 = dao.select(ip);
			if(plist!=null && plist.size()>0&&list2.size()>0){
				for(int i=0;i<plist.size();i++){
					actityAmountPart += (plist.get(i).getPartQuantity()*plist.get(i).getPartCostPrice()) - plist.get(i).getPartCostAmount();
				System.out.println(actityAmountPart);
				}
			}
			System.out.println("配件最后得分：："+format.format(actityAmountPart));
			TtAsRoLabourPO lp = new TtAsRoLabourPO();
			lp.setRoId(Long.valueOf(roId));
			lp.setPayType(Constant.PAY_TYPE_01);
			List<TtAsRoLabourPO> llist = dao.select(lp);
			
			TtAsRoAddItemPO ip2 = new TtAsRoAddItemPO();
			ip2.setRoId(Long.valueOf(roId));
			ip2.setPayType(Constant.PAY_TYPE_02);
			ip2.setAddItemCode(Constant.SERVICEACTIVITY_CAR_cms_06.toString());
			List<TtAsRoAddItemPO> list3 = dao.select(ip2);
			if(llist!=null && llist.size()>0&&list3.size()>0){
				for(int i=0;i<llist.size();i++){
					actityAmountLabour += (llist.get(i).getStdLabourHour()*llist.get(i).getLabourPrice())-llist.get(i).getLabourAmount();
				}
			}
			System.out.println("工时最后得分：："+format.format(actityAmountLabour));
			List<FsFileuploadPO> attachLs = dao.queryAttById(roId);// 取得附件
			Map<String, String> map = new HashMap<String, String>();
			map.put("roId", roId);
			PageResult<TtAsRepairOrderExtPO> ps = dao.queryRepairOrder(map,
					Constant.PAGE_SIZE, 1);
			List<TtAsRepairOrderExtPO> ls = ps.getRecords();
			TtAsRepairOrderExtPO tarp = new TtAsRepairOrderExtPO();
			if (ls != null) {
				if (ls.size() > 0) {
					tarp = ls.get(0);
				}
			}
			List<Map<String, Object>> isNewPART = dao.viewIsNewPart(roId);
			List<Map<String, Object>> NewPartCodeList = dao.viewIsNewPartString(roId);
			String NewPartCode = "";
			if(NewPartCodeList.size()>0){
				for(int i=0;i<NewPartCodeList.size();i++){
					NewPartCode=NewPartCode+NewPartCodeList.get(i).get("PART_NAME")+",";	
				}
			}
			if(Integer.valueOf(isNewPART.get(0).get("COUNT").toString())>0){
				newPart=1;
			}
			List<Map<String,Object>> jc = dao.queryTcCode();
			int jc_1 = 0;
			if(jc.size()>0){
				jc_1 = Integer.valueOf(jc.get(0).get("CODE_ID").toString());
			}
			request.setAttribute("roId", roId);
			act.setOutData("actityAmount", format.format(actityAmountPart));
			act.setOutData("actityAmountPart", format.format(actityAmountPart));
			act.setOutData("actityAmountLabour", format.format(actityAmountLabour));
			act.setOutData("jc_1", jc_1);
			act.setOutData("roBean", tarp);
			act.setOutData("newPart", newPart);
			act.setOutData("attachLs", attachLs);
			act.setOutData("NewPartCode", NewPartCode);
			
			String roNo = request.getParamValue("roNo");// 活动ID
			List<Map<String, Object>> accessoryDtlList = dao.getAccessoryDtl(roNo);
			List<Map<String, Object>> workHourCodeMap = dao.getWorkhoursCode();
			act.setOutData("workHourCodeMap", workHourCodeMap);
			act.setOutData("accessoryDtlList", accessoryDtlList);

			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoney(roNo);
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================
			request.setAttribute("roNo", roNo);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动管理--活动项目");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: changeRoItem
	 * @Description: TODO(工单带出工时)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void changeRoRepair() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		
		String roId = request.getParamValue("roId");// 活动ID
		String modelId = request.getParamValue("modelId");
		String itemIds = request.getParamValue("itemIds");
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
		try {
			TtAsRepairOrderPO bean = new TtAsRepairOrderPO();
			bean.setId(Utility.getLong(roId));
			List<TtAsRepairOrderPO> ls = dao.select(bean);
			bean = ls.get(0);
			Map<String,String> map = new HashMap<String,String>();
			map.put("roNo", bean.getRoNo());
			map.put("vin", bean.getVin());
			map.put("isSel", "isSel");
			map.put("itemIds", itemIds);
			map.put("companyId", companyId.toString());
			if (Utility.testString(modelId)) {
				
			}else {
				TmVehiclePO tvp = new TmVehiclePO();
				tvp.setVin(bean.getVin());
				
				/*****add by liuxh 20131108判断车架号不能为空*****/
				CommonUtils.jugeVinNull(bean.getVin());
				/*****add by liuxh 20131108判断车架号不能为空*****/
				
				List<TmVehiclePO> lsv = dao.select(tvp);
				if (lsv!=null) {
					if (lsv.size()>0) {
						modelId = lsv.get(0).getModelId()+"";
					}
				}
			}
			map.put("modelId", modelId);
			List<TtAsActivityBean> ActivityBeanList = dao.getWorkingHoursInfoList(roId, map,logonUser);// 活动工时
			//TtAsRoLabourPO tarlp = new TtAsRoLabourPO();
			//tarlp.setRoId(Utility.getLong(roId));
			//TtAsRoLabourPO tarlp1 = new TtAsRoLabourPO();
			//tarlp1.setRoId(Utility.getLong(roId));
			//tarlp1.setIsSel("0");
			//dao.update(tarlp, tarlp1);
			act.setOutData("ActivityBeanList", ActivityBeanList);
			//request.setAttribute("ActivityNetItemList", ActivityNetItemList);
			request.setAttribute("roId", roId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: 
	 * @Description: TODO(工单带出配件)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void changeRoPart() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String partAddPer = (String)act.getSession().get(Constant.PART_ADD_PER);
		
		String roId = request.getParamValue("roId");// 活动ID
		String partIds = request.getParamValue("partIds");
		try {
			TtAsRepairOrderPO bean = new TtAsRepairOrderPO();
			bean.setId(Utility.getLong(roId));
			List<TtAsRepairOrderPO> ls = dao.select(bean);
			bean = ls.get(0);
			Map<String,String> map = new HashMap<String,String>();
			map.put("roNo", bean.getRoNo());
			map.put("vin", bean.getVin());
			map.put("isSel", "isSel");
			map.put("partIds", partIds);
			map.put("partAddPer", partAddPer);
			map.put("yieldly", bean.getBalanceYieldly().toString());
			List<TtAsActivityBean> ActivityPartsList = dao.getPartsList(roId,map,logonUser);// 活动配件
		
			//TtAsRoRepairPartPO tarrp = new TtAsRoRepairPartPO();
			//tarrp.setRoId(Utility.getLong(roId));
			//TtAsRoRepairPartPO tarrp1 = new TtAsRoRepairPartPO();
			//tarrp1.setRoId(Utility.getLong(roId));
			//tarrp1.setIsSel("0");
			//dao.update(tarrp, tarrp1);
			act.setOutData("ActivityPartsList", ActivityPartsList);
			
			request.setAttribute("roId", roId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动管理--活动项目");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title: 
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void changeRoFile() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		
		String roId = request.getParamValue("roId");// 活动ID
		try {
			List<FsFileuploadPO> attachLs = dao.queryAttById(roId);// 取得附件
			act.setOutData("ActivityPartsList", attachLs);
			request.setAttribute("roId", roId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动管理--活动项目");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title: changeRoPart
	 * @Description: TODO(工单带出附件项目)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void changeRoOther() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		
		String roId = request.getParamValue("roId");// 活动ID
		String otherIds = request.getParamValue("otherIds");
		try {
			TtAsRepairOrderPO bean = new TtAsRepairOrderPO();
			bean.setId(Utility.getLong(roId));
			List<TtAsRepairOrderPO> ls = dao.select(bean);
			bean = ls.get(0);
			Map<String,String> map = new HashMap<String,String>();
			map.put("roNo", bean.getRoNo());
			map.put("vin", bean.getVin());
			map.put("isSel", "isSel");
			map.put("otherIds", otherIds);
			List<TtAsActivityBean> ActivityNetItemList = dao.getNetItemList(roId,map,logonUser);// 活动配件
			//TtAsRoAddItemPO tarap = new TtAsRoAddItemPO();
			//tarap.setRoId(Utility.getLong(roId));
			//TtAsRoAddItemPO tarap1 = new TtAsRoAddItemPO();
			//tarap1.setRoId(Utility.getLong(roId));
			//tarap1.setIsSel(0);
			//dao.update(tarap, tarap1);
			act.setOutData("ActivityNetItemList", ActivityNetItemList);
			request.setAttribute("roId", roId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动管理--活动项目");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roItemWorkHoursQuery
	 * @Description: TODO(工单查询工时)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roItemWorkHoursQuery() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String roId = request.getParamValue("roId");// 活动ID
			String labourCode = request.getParamValue("labourCodes");// 工时代码
			String cnDes = request.getParamValue("cnDess");// 名称
			TtAsActivityBean MantainBean = new TtAsActivityBean();
			MantainBean.setActivityId(roId);
			MantainBean.setLabourCode(labourCode);
			MantainBean.setCnDes(cnDes);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			Map<String,String> map = new HashMap<String,String>();
			PageResult<Map<String, Object>> ps = dao.getRoItemWorkHoursQuery(
					MantainBean, map,logonUser,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			request.setAttribute("roId", roId);
			act.setForword(RO_ITEM_LABOUR);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔工时信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roItemWorkHoursOption
	 * @Description: TODO(工单新增工时)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("static-access")
	public void roItemWorkHoursOption() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String roId = request.getParamValue("roId"); // 工单ID
			// String claimId = request.getParamValue("claimId"); //索赔单ID
			String labourId = request.getParamValue("labourId"); // labourId
			String labourCode = request.getParamValue("labourCode"); // 维修项目代码
			String cnDes = request.getParamValue("cnDes"); // 维修项目名称
			String labourHour = request.getParamValue("labourHour"); // 工时数
			if (labourId != null && !"".equals(labourId)) {
				String[] labourIdArray = labourId.split(","); // 取得所有groupIds放在数组中
				// String [] labourCodeArray = labourCode.split(",");
				// //取得所有labourCode放在数组中
				// String [] cnDesArray = cnDes.split(","); //取得所有cnDes放在数组中
				// String [] labourHourArray = labourHour.split(",");
				// //取得所有labourHour放在数组中
				TtAsRoLabourPO RepairitemPO = new TtAsRoLabourPO();
				// TtAsWrLabouritemPO RepairitemPO = new TtAsWrLabouritemPO();
				// RepairitemPO.setId(Long.parseLong(claimId)); //设置索赔单ID
				// RepairitemPO.setCreateBy(logonUser.getUserId());
				// RepairitemPO.setCreateDate(new Date());
				// RepairitemPO.setUpdateBy(logonUser.getUserId());
				// RepairitemPO.setUpdateDate(new Date());
				dao.roItemWorkHoursOption(roId, labourIdArray, logonUser);
				act.setOutData("success", "true");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工时信息增加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: deleteItemWorkHoursOption
	 * @Description: TODO(工单删除工时)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("static-access")
	public void deleteItemWorkHoursOption() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String activityId = request.getParamValue("roId"); // 活动ID
			String itemId = request.getParamValue("itemId"); // itemId
			TtAsRoLabourPO RepairitemPO = new TtAsRoLabourPO();
			RepairitemPO.setId(Long.parseLong(itemId));
			// RepairitemPO.setItemId(Long.parseLong(itemId));
			dao.deleteItemWorkHoursOption(RepairitemPO);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工时信息增加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roItemPartsQuery
	 * @Description: TODO(工单查询配件)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roItemPartsQuery() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String roId = request.getParamValue("roId");// 活动ID
			String partNo = request.getParamValue("partNos"); // 配件代码
			String partName = request.getParamValue("partNames"); // 配件名称
			TtAsActivityBean MantainBean = new TtAsActivityBean();
			MantainBean.setActivityId(roId);
			MantainBean.setPartNo(partNo);
			MantainBean.setPartName(partName);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			Map<String,String> map = new HashMap<String,String>();
			
			PageResult<Map<String, Object>> ps = dao.getRoPartsQuery(
					MantainBean, map,logonUser,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			request.setAttribute("roId", roId);
			act.setForword(RO_ITEM_PART);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roItemPartsOption
	 * @Description: TODO(工单新增配件)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("static-access")
	public void roItemPartsOption() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String roId = request.getParamValue("roId"); // 活动ID
			String partsId = request.getParamValue("partsId"); // partsId
			String partNo = request.getParamValue("partNo"); // 配件项目代码
			String partName = request.getParamValue("partName"); // 配件项目名称
			String partsQuantityArray = request.getParamValue("PART_QUANTITY");// 配件项目数量
			String claimPrice = request.getParamValue("claimPrice");// 单价
			String supplierCode = request.getParamValue("supplierCode");// 供应商代码
			String supplierName = request.getParamValue("supplierName");// 供应商名称
			if (partsId != null && !"".equals(partsId)) {
				String[] partsIdArray = partsId.split(","); // 取得所有partsId放在数组中
				//String[] partNoArray = partNo.split(","); // 取得所有partNo放在数组中
				//String[] partNameArray = partName.split(","); // 取得所有partName放在数组中
				//String[] partsQuantityArrays = null;
				String[] claimPriceArray = null;
				String[] supplierCodeArray = null;
				String[] supplierNameArray = null;
				/*if (null != partsQuantityArray
						&& !"".equals(partsQuantityArray)
						&& !",".equals(partsQuantityArray)) {
					//partsQuantityArrays = partsQuantityArray.split(","); // 取得所有partsQuantity放在数组中
				}
				if (null != claimPrice && !"".equals(claimPrice)) {
					claimPriceArray = claimPrice.split(","); // 取得所有单价放在数组中
				}
				if (null != supplierCode && !"".equals(supplierCode)) {
					supplierCodeArray = supplierCode.split(","); // 取得所有供应商代码放在数组中
				}
				if (null != supplierName && !"".equals(supplierName)) {
					supplierNameArray = supplierName.split(","); // 取得所有供应商名称放在数组中
				}*/
				/*
				 * TtAsActivityPartsPO PartsPO =new TtAsActivityPartsPO();
				 * PartsPO.setActivityId(Long.parseLong(activityId));
				 * PartsPO.setCreateBy(logonUser.getUserId());
				 * PartsPO.setCreateDate(new Date());
				 * PartsPO.setUpdateBy(logonUser.getUserId());
				 * PartsPO.setUpdateDate(new Date());
				 */
				dao.roItemPartsOption(roId, partsIdArray, logonUser);
				act.setOutData("success", "true");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息增加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: deleteItemPartsOption
	 * @Description: TODO(工单删除配件)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("static-access")
	public void deleteItemPartsOption() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String roId = request.getParamValue("roId"); // 活动ID
			String partsId = request.getParamValue("partsId"); // itemId
			TtAsRoRepairPartPO PartsPO = new TtAsRoRepairPartPO();
			PartsPO.setId(Long.parseLong(partsId));
			// PartsPO.setPartsId(Long.parseLong(partsId));
			dao.deleteItemPartsOption(PartsPO);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息增加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roItemOthersQuery
	 * @Description: TODO(工单查询其他项目)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roItemOthersQuery() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String roId = request.getParamValue("roId"); // 活动ID
			// String partNo=request.getParamValue("partNo"); //配件代码
			// String partName=request.getParamValue("partName"); //配件名称
			TtAsActivityBean MantainBean = new TtAsActivityBean();
			MantainBean.setActivityId(roId);
			// MantainBean.setPartNo(partNo);
			// MantainBean.setPartName(partName);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao.getRoOthersQuery(MantainBean, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			request.setAttribute("roId", roId);
			act.setForword(RO_ITEM_OTHER);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roItemOthersOption
	 * @Description: TODO(工单新增其他项目)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("static-access")
	public void roItemOthersOption() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String roId = request.getParamValue("roId"); // 活动ID
			String id = request.getParamValue("id"); // ID
			String itemCode = request.getParamValue("itemCode"); // 项目代码
			String itemDesc = request.getParamValue("itemDesc"); // 项目名称
			if (id != null && !"".equals(id)) {
				String[] idArray = id.split(","); // 取得所有id放在数组中
				//String[] itemCodeArray = itemCode.split(","); // 取得所有itemCode放在数组中
				//String[] itemDescArray = itemDesc.split(","); // 取得所有itemDesc放在数组中
				/*
				 * TtAsActivityNetitemPO NetitemPO =new TtAsActivityNetitemPO();
				 * NetitemPO.setActivityId(Long.parseLong(activityId));
				 * NetitemPO.setCreateBy(logonUser.getUserId());
				 * NetitemPO.setCreateDate(new Date());
				 * NetitemPO.setUpdateBy(logonUser.getUserId());
				 * NetitemPO.setUpdateDate(new Date());
				 */
				dao.roItemOthersOption(roId, idArray, logonUser);
				act.setOutData("success", "true");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息增加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: deleteItemOthersOption
	 * @Description: TODO(工单删除其他项目)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("static-access")
	public void deleteItemOthersOption() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String activityId = request.getParamValue("roId"); // 活动ID
			String id = request.getParamValue("id"); // itemId
			TtAsRoAddItemPO PartsPO = new TtAsRoAddItemPO();
			PartsPO.setRoId(Long.parseLong(activityId));
			PartsPO.setId(Long.parseLong(id));
			dao.deleteItemOthersOption(PartsPO);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "附加项目增加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: selAllOptions 
	* @Description: TODO(选择其他项目，工时，配件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void selAllOptions () {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			roItemOthersOption();
			roItemPartsOption();
			roItemWorkHoursOption();
			act.setOutData("success", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "附加项目增加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}

	/**
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(工单信息查询)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void queryRepairOrder() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
		String dealerId = logonUser.getDealerId();
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String roId = request.getParamValue("roId"); // 活动ID
			String roNo = request.getParamValue("RO_NO"); // 工单号
			String vin = request.getParamValue("VIN"); // 工单号
			String id = request.getParamValue("id"); // itemId
			String roStatus = request.getParamValue("roStatus");
			String customerName = request.getParamValue("CUSTOMER_NAME"); //车主姓名
			String licenseNo = request.getParamValue("LICENSE_NO");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String modelId = request.getParamValue("MODEL_ID");
			String balanceDateStart = request.getParamValue("BALANCE_DATE_START");
			String balanceDateEnd = request.getParamValue("BALANCE_DATE_END");
			String createDateStr = request.getParamValue("CREATE_DATE_STR");
			String createDateEnd = request.getParamValue("CREATE_DATE_END");
			String isApp = request.getParamValue("isApp");
			Map<String, String> map = new HashMap<String, String>();
			map.put("roId", roId);
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("roStatus", roStatus);
			map.put("dealerId", dealerId);
			map.put("balanceDateStart", balanceDateStart);
			map.put("balanceDateEnd", balanceDateEnd);
			map.put("repairType", repairType);
			map.put("modelId", modelId);
			map.put("licenseNo", licenseNo);
			map.put("customerName", customerName);
			map.put("createDateStr", createDateStr);
			map.put("createDateEnd", createDateEnd);
			if (Utility.testString(isApp)) {
				map.put("isApp", "isApp");
			}
			PageResult<TtAsRepairOrderExtPO> ps = dao.queryRepairOrder(map,
					Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	/*------------------质量信息跟踪卡---------------*/
	/**
	 * 
	* @Title: infoAddForward 
	* @Description: TODO(质量信息新增跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void infoAddForward(){
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		try {
			
			String claimId = request.getParamValue("claimId");
			String detail = request.getParamValue("detail");
			String infoId = request.getParamValue("infoId");
			
			TmDealerPO tdp = new TmDealerPO();
			tdp.setDealerId(Utility.getLong(dealerId));
			List<TmDealerPO> lsd = dao.select(tdp);
			if (lsd != null) {
				if (lsd.size() > 0) {
					tdp = lsd.get(0);
				}
			}
			TtAsWrInformationqualityExtPO tawep = new TtAsWrInformationqualityExtPO();
			//tawep.setClaimId(Utility.getLong(claimId));
			tawep = dao.queryInfoByClaimId(claimId);
			if (Utility.testString(claimId)) {
				
			}else {
				if (Utility.testString(infoId)){
					tawep=dao.queryInfoByClaimInfoId(infoId);
				}
			}
			if ("1".equals(detail)) {
				act.setForword(INFO_DETAIL_URL);
			}else{
				act.setForword(INFO_ADD_URL);
			}
			act.setOutData("info",tawep);
			act.setOutData("claimId", claimId);
			act.setOutData("dealerName", tdp.getDealerName());
			act.setOutData("dealerCode", tdp.getDealerCode());
		} catch (Exception e) {
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
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		try {
			
			
			String claimId = request.getParamValue("claimId"); 
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
			
			Date date  = new Date();
			TtAsWrInformationqualityPO taip = new TtAsWrInformationqualityPO();
			String infoId = SequenceManager.getSequence("");
			taip.setId(Utility.getLong(infoId));
			taip.setBillNo(SequenceManager.getSequence("IQ"));
			taip.setClaimId(Utility.getLong(claimId)); //
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
			taip.setCreateBy(logonUser.getUserId());
			taip.setCreateDate(date);
			
			dao.insert(taip);
			act.setOutData("success", true); //插入成功，关闭信息页
			act.setOutData("infoId", infoId);
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

		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String claimId = request.getParamValue("claimId"); 
			String billNo = request.getParamValue("billNo");
			String infoId = request.getParamValue("infoSubId");
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
			
			Date date  = new Date();
			TtAsWrInformationqualityPO taip0 = new TtAsWrInformationqualityPO();
			taip0.setId(Utility.getLong(infoId));
			TtAsWrInformationqualityPO taip = new TtAsWrInformationqualityPO();
			taip.setId(Utility.getLong(infoId));
			taip.setClaimId(Utility.getLong(claimId)); //不插入索赔ID，待总提交时对信息表进行更新
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
			taip.setCreateBy(logonUser.getUserId());
			taip.setCreateDate(date);
			
			dao.update(taip0,taip);
			act.setOutData("success", true); //修改成功，关闭信息页
			act.setOutData("infoId", infoId);
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
	* @Title: showActivityForward 
	* @Description: TODO(查询服务活动跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void showActivityForward() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String vin = request.getParamValue("vin");
			String inMileage = request.getParamValue("IN_MILEAGE");
			
			if (Utility.testString(inMileage)) {
				
			}else {
				inMileage = "0";
			}
			act.setOutData("vin", vin);
			act.setOutData("inMileage", inMileage);
			act.setForword(ACTIVITY_URL);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动信息");
			act.setOutData("success", false);
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
		
	}
	/**
	 * 
	* @Title: queryActivity 
	* @Description: TODO(查询服务活动页面) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void queryActivity() {
		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {
			Map<String,String> map = new HashMap<String,String>();
			String vin = request.getParamValue("vin"); 
			String inMileage = request.getParamValue("inMileage");  
			String activityCode = request.getParamValue("ACTIVITY_CODE");
			String activityName = request.getParamValue("ACTIVITY_NAME");
			String modelId = "";
			Long yieldly = 0L;
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Date proDate = null;
			Date purDate = null;
			String proDateStr = "";
			String purDateStr = "";
			TmVehiclePO tvp = new TmVehiclePO();
			
			/*****add by liuxh 20131108判断车架号不能为空*****/
			CommonUtils.jugeVinNull(vin);
			/*****add by liuxh 20131108判断车架号不能为空*****/
			
			if (Utility.testString(vin))
			tvp.setVin(vin);
			else 
			tvp.setVin("-1");
			List<TmVehiclePO> lsv = dao.select(tvp);
			if (lsv!=null) {
				if (lsv.size()>0) {
					tvp = lsv.get(0);
					modelId = tvp.getModelId()+"";
					proDate = tvp.getProductDate();
					if (proDate!=null) {
						proDateStr = sf.format(proDate);
					}
					purDate = tvp.getPurchasedDate();
					if (purDate!=null) {
						purDateStr = sf.format(purDate);
					}
					yieldly = tvp.getYieldly();
				}else {
					act.setOutData("noCar", true); //无该车信息
				}
			}else {
				act.setOutData("noCar", true); //无该车信息
			}
			map.put("vin", vin);
			map.put("dealerCode", dealerCode);
			map.put("proDate",proDateStr);
			map.put("yieldly", String.valueOf(yieldly));
			map.put("purDate", purDateStr);
			map.put("dealerId", dealerId);
			map.put("modelId", modelId);
			map.put("activityCode", activityCode);
			map.put("activityName", activityName);
			map.put("inMileage", inMileage);
			PageResult<TtAsActivityPO> ps = dao.queryActivityComboByVin(map,getCurrPage(),Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动信息");
			act.setOutData("success", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 查询维修信息
	 */
	public void queryReplace() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper reqeust = act.getRequest();
			String id = reqeust.getParamValue("id");//活动ID
			List<Map<String, Object>> labouritemList = dao.queryLabouritemReplace(id);
			List<Map<String, Object>> partsitemList = dao.queryPartsitemReplace(id);
			act.setOutData("labouritemList", labouritemList);//维修工时
			act.setOutData("partsitemList", partsitemList);//维修配件
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动信息");
			act.setOutData("success", false);
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
		
	}
	
	  /**
	   * 车型免费保养历史记录
	   * 规则：从维修工单中统计出对应该车辆的保养类型维修历史记录
	   *      和服务活动类型的工单（服务活动类型为保养）
	   * 呈现信息：1."工单号码"2."工单日期"3."经销商名称"4."保养次数"5."保养费用"6."行驶里程"
	   */
	  public void freeMaintainHistory(){
		    
		    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		    try {
		      
		      String vin = request.getParamValue("VIN");
		      List<Map<String,Object>> freeMaintaimHisList = dao.freeMaintainHistory(vin);
		      
		      act.setOutData("freeMaintaimHisList", freeMaintaimHisList);
		      act.setOutData("VIN", vin);
		      act.setForword(this.FREEMAINTAIM_HIS_URL);
		    }catch (Exception e) {
		      BizException e1 = new BizException(act, e,
		          ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆维修历史记录");
		      act.setOutData("success", false);
		      logger.error(logonUser, e1);
		      act.setException(e1);
		    } 
	  }
	  
	  /**
	   * 维修历史记录
	   * 规则：从索赔单中统计出对应车辆"一般维修","售前维修","外出维修"类型索赔单
	   * 呈现信息：1."索赔单号码"2."维修日期"3."经销商名称"4."行驶里程"5."工时代码"6."工时名称"7."配件代码"8."配件名称"
	   */
	  public void maintaimHistory(){
	    
	    
	    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    try {
	      
	      String vin = request.getParamValue("VIN");
	      String claimTypes = "";
	      if(logonUser.getDealerId() != null )
	      {
	    	  claimTypes = ""+Constant.REPAIR_TYPE_03 ;
        
	      }
	      
	      List<Map<String,Object>> maintaimHisList = dao.maintaimHistory(vin, claimTypes);
	      
	      act.setOutData("maintaimHisList", maintaimHisList);
	      act.setOutData("VIN", vin);
	      act.setForword(this.MAINTAIN_HIS_URL);
	    }catch (Exception e) {
	      BizException e1 = new BizException(act, e,
	          ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆维修历史记录");
	      act.setOutData("success", false);
	      logger.error(logonUser, e1);
	      act.setException(e1);
	    }  
	  }
	  
	  /**
	   * 授权历史记录
	   * 规则：历史索赔单的技术室授权历史(经过人工审核或自动审核的索赔单) 
	   * 呈现信息：1."索赔单号吗"2."经销商名称"3."授权时间"4."授权人"5."授权结果"6."授权备注"7."工时名称"8."配件名称" 
	   */
	  public void auditingHistory(){
	    
	    
	    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    try {
	      
	      String vin = request.getParamValue("VIN");
	      //10791001 未上报
	      String expStatus = Constant.CLAIM_APPLY_ORD_TYPE_01.toString();
	      //添加预授权申请展示2012-11-20 modify by tanv
	      List<Map<String,Object>> auditingHisList = dao.auditingHistory(vin, expStatus);
	      
	      act.setOutData("auditingHisList", auditingHisList);
	      act.setOutData("VIN", vin);
	      act.setForword(this.AUDITING_HIS_URL);
	    }catch (Exception e) {
	      BizException e1 = new BizException(act, e,
	          ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆授权历史记录");
	      act.setOutData("success", false);
	      logger.error(logonUser, e1);
	      act.setException(e1);
	    }
	  }
	  
	  /**
	   * 
	  * @Title: getPartPrice 
	  * @Description: TODO(根据partCode和supplierCode取得配件索赔价格) 
	  * @param     设定文件 
	  * @return void    返回类型 
	  * @throws
	   */
	  public void getPartPrice() {
		  
	    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    String partAddPer = (String)act.getSession().get(Constant.PART_ADD_PER);
	    try {
	      
	      String partCode = request.getParamValue("partCode");
	      String supplierCode = request.getParamValue("supplierCode");
	      Map<String,String> map = new HashMap();
	      map.put("partCode", partCode);
	      if ("UNKNOWN".equals(supplierCode)) {//如果选择位置不放入MAP
	    	  
	      }else {
	      map.put("supplierCode",supplierCode);
	      }
	      TmPtPartBasePO tp = dao.getPartPrice(map);
	      Float claimPrice = tp.getClaimPrice()==null?0f:tp.getClaimPrice();
	      Float partAddPerFloat = Utility.getFloat(partAddPer);
	      claimPrice = claimPrice*(1+partAddPerFloat);
	      act.setOutData("claimPrice", claimPrice);
	    }catch (Exception e) {
	      BizException e1 = new BizException(act, e,
	          ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆授权历史记录");
	      act.setOutData("success", false);
	      logger.error(logonUser, e1);
	      act.setException(e1);
		}
	  }
	  
	  /**
	   * 售前车辆 检测规则
	   * 规则：当 车辆 未卖出前（售前车辆，无实效记录）时
	   *       索赔单类型只能为 服务活动 和 售前维修
	   */
	  public void checkUnSaleVechile(){
		  
		  
		  AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		  try{
			  
			  String vin = request.getParamValue("vin");
			  String claimType = request.getParamValue("claimType");
			  
			  boolean isSale = true;
			  if(Constant.CLA_TYPE_06.toString().equals(claimType) || Constant.CLA_TYPE_07.toString().equals(claimType)|| Constant.CLA_TYPE_11.toString().equals(claimType)){//服务活动或售前维修不用验证对应车辆
				  isSale = true;
			  }else{//其他类型需要验证对应车辆是否存在销售记录
				  List<TtDealerActualSalesPO> saleList = dao.checkUnSaleVechile(vin);
				  if(saleList==null || saleList.size()<1){
					  isSale = false;
				  }
			  }
			  
			  act.setOutData("isSale", isSale);
		  }catch(Exception e){
			  BizException e1 = new BizException(act, e,
			          ErrorCodeConstant.QUERY_FAILURE_CODE, "售前车辆 检测规则");
		      act.setOutData("success", false);
		      logger.error(logonUser, e1);
		      act.setException(e1);
		  }
	  }
	  
	  /*
	   * 保养状态判定
	   */
	  public void maintainStateSet(){
		  
		  AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		  try{
			  
			  String vin = request.getParamValue("VIN");
			  String mile = request.getParamValue("mile");
			  String roNo = request.getParamValue("ro_no");
			  
			  List<Map<String,Object>> list = dao.getMaintainStateSet(vin);
			  if(list.size()>0){
				  act.setOutData("map", list.get(0));
				  Date now = new Date(); //今天
					
				  String formatStyle ="yyyy-MM-dd";  
				  SimpleDateFormat df = new SimpleDateFormat(formatStyle);  
				  String d1 = (String)list.get(0).get("SALES_DATE");
				  String d2 = df.format(now);
				  int month  = Utility.compareDate(d1,d2,1); //取得今日和保养开始时间的插值
				  int day = Utility.compareDate(d1, d2, 0); //取得今日和保养开始时间的插值 天数
					
				  List<TtAsWrQamaintainPO> lsq = dao.getFree(Integer.parseInt(list.get(0).get("FREE_TIMES").toString())+1,logonUser.getCompanyId(), month, day, Double.parseDouble(mile));
				  if(lsq.size()>0){
					  act.setOutData("flag", Constant.IF_TYPE_NO.toString());
				  }else{
					  act.setOutData("flag", Constant.IF_TYPE_YES.toString());
				  }
				  
				  act.setOutData("mile", mile);
				  act.setOutData("days", day);
				  act.setOutData("now", d2);
				  
				//预授权状态
				  Integer preClaimStatus = null;
				  String preClaimYN = Constant.IF_TYPE_NO.toString();
				  //判定是否存在工单
				  if(StringUtil.notNull(roNo)){
					  TtAsRepairOrderPO ropo = new TtAsRepairOrderPO();
					  ropo.setRoNo(roNo);
					  List<TtAsRepairOrderPO> roList = dao.select(ropo);
					  if(roList.size()>0){
						  preClaimStatus = roList.get(0).getForlStatus();
						  if(preClaimStatus!=null)
							  preClaimYN = Constant.IF_TYPE_YES.toString();
					  }
				  }
				  
				  act.setOutData("preClaimYN", preClaimYN);
				  act.setOutData("preClaimStatus", preClaimStatus);
				  act.setForword(MAINTAIN_STATE_SET_URL);
			  }
			  else{
				  act.setOutData("map",null);
			  }
		  }catch(Exception e){
			  BizException e1 = new BizException(act, e,
			          ErrorCodeConstant.QUERY_FAILURE_CODE, "售前车辆 检测规则");
		      act.setOutData("success", false);
		      logger.error(logonUser, e1);
		      act.setException(e1);
		  }
	  }
	  /*
	   * 配件三包判定
	   */
	  public void threePackageSet(){
		  
		  AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		  try{
			  
			  String flag= request.getParamValue("flag");
			  String vin = request.getParamValue("VIN");
			  String mile = request.getParamValue("mile");
			  String codes = request.getParamValue("codes");
			  String codes_type = request.getParamValue("codes_type");
			  String labcodes = request.getParamValue("labcodes");
			  String labcodes_type= request.getParamValue("labcodes_type");
			  String roNo = request.getParamValue("roNo");
			  List<String> codelist = new ArrayList<String>();
			  List<String> labcodelist = new ArrayList<String>();
			  StringBuffer sql1 = new StringBuffer();
			  sql1.append("select t.* from tm_vehicle t where   t.vin='"+vin+"' AND t.WR_END_DATE is null");
			  List<TmVehiclePO> listve= dao.select(TmVehiclePO.class, sql1.toString(), null);
			  
			  if(listve !=null && listve.size()>0)
			  {
		    	act.setOutData("jude", 1);
			  }else
			  {
				  act.setOutData("jude", 0);
			  }
			 if(roNo !=null && roNo.length()>0)
			 {
				 act.setOutData("feettype",0);
				 TtAsWrFeeWarrantyPO asWrFeeWarrantyPO = new TtAsWrFeeWarrantyPO();
				 asWrFeeWarrantyPO.setRoNo(roNo);
			     List<TtAsWrFeeWarrantyPO> feeList= dao.select(asWrFeeWarrantyPO);
				 if(feeList != null && feeList.size()>0)
				 {
					 asWrFeeWarrantyPO =  feeList.get(0);
					 int feedays = asWrFeeWarrantyPO.getWarCountDays()+1;
					 act.setOutData("feedays", feedays);
					 String sql = "SELECT * from  TT_AS_WR_FEE_RULE t where t.FR_WR_DAYS <= "+feedays +" order by t.FR_WR_DAYS desc";
					 List<TtAsWrFeeRulePO> rulelist = dao.select(TtAsWrFeeRulePO.class, sql, null);
					 if(rulelist != null && rulelist.size()>0)
					 {
						 int  Fee_LEVEL = rulelist.get(0).getFrLevel();
						 act.setOutData("Fee_LEVEL", Fee_LEVEL);
					 }
					 act.setOutData("asWrFeeWarrantyPO",asWrFeeWarrantyPO);
				 }
			 }else{
				 act.setOutData("feettype",1);
			 }
			  if(codes_type != null && codes_type.length()>0 && codes != null && codes.length() > 0)
			  {
				  String[] code = codes.split(",");
				  String[] code_type = codes_type.split(",");
				  for(int i = 0 ;i < code_type.length;i++)
				  {
					  if(code_type[i].equals(""+Constant.PAY_TYPE_02))
					  {
						  if(!"00-000".equalsIgnoreCase(code[i].trim())){
							  codelist.add(code[i].trim());
						  }
					  }
				  }
				  List<TtAsWrVinPartRepairTimesvalPO> codeslist= dao.getCodes(vin, codelist);
				  act.setOutData("codeslist", codeslist);
			  }
			  List<TtAsWrVinPartRepairTimesvalPO> codeslistnull= dao.getCodesNull(vin, codelist,1);
			  act.setOutData("codeslistnull", codeslistnull);
			  

			  if(labcodes_type != null && labcodes_type.length()>0 && labcodes != null && labcodes.length() > 0)
			  {
				  String[] labcode = labcodes.split(",");
				  String[] labcode_type = labcodes_type.split(",");
				  for(int i = 0 ;i < labcode_type.length;i++)
				  {
					  if(labcode_type[i].equals(""+Constant.PAY_TYPE_02))
					  {
						  labcodelist.add(labcode[i]);
					  }
				  }
				  List<TtAsWrVinPartRepairTimesvalPO> labcodeslist= dao.getCodes(vin, labcodelist);
				  act.setOutData("labcodeslist", labcodeslist);
			  }
			  List<TtAsWrVinPartRepairTimesvalPO> labcodeslistnull= dao.getCodesNull(vin, labcodelist,2);
			  act.setOutData("labcodeslistnull", labcodeslistnull);
			 
			  TtAsWrVinRepairDaysPO daysPO = new TtAsWrVinRepairDaysPO();
			  daysPO.setVin(vin);
			  List<TtAsWrVinRepairDaysPO> vinlist = dao.select(daysPO);
			  if(vinlist != null && vinlist.size()> 0)
			  {
				  daysPO = vinlist.get(0);
				  int pepair = daysPO.getCurDays();
				  String sql = "SELECT * from  TT_AS_WR_VIN_RULE t where t.VR_WARRANTY <= "+pepair +" and t.VR_TYPE = "+Constant.VR_TYPE_1+" order by t.VR_WARRANTY desc";
				  List<TtAsWrVinRulePO> VinRulelist= dao.select(TtAsWrVinRulePO.class,sql,null);
				 if(VinRulelist != null && VinRulelist.size()>0)
				 {
					 act.setOutData("pepair_LEVEL",VinRulelist.get(0).getVrLevel() );
				 }
				  
				  
			  }
			  act.setOutData("vinlist", vinlist);
			  
			  List<String> poslist= dao.getpart(codelist);
			  if(poslist!= null && poslist.size()>0)
			  {
				  List<TtAsWrVinPartRepairTimesvalPO> posslist= dao.getCodes(vin, poslist);
				  act.setOutData("poslist", posslist);
			  }
			  List<TtAsWrVinPartRepairTimesvalPO> posslistnull= dao.getCodesNull(vin, poslist,3);
			  act.setOutData("posslistnull", posslistnull);


			  Date now = new Date(); //今天
			  List<Map<String,Object>> list = dao.getMaintainStateSet(vin);
			  //取得工单开始时间
			  if(StringUtil.notNull(roNo) && !"null".equals(roNo)){
				  TtAsRepairOrderPO po = new TtAsRepairOrderPO() ;
				  po.setRoNo(roNo);
				  List list2 = dao.select(po) ;
				  if(list2.size()>0)
					  now = ((TtAsRepairOrderPO)list2.get(0)).getRoCreateDate() ;
			  }
			  if(list.size()>0){
				  act.setOutData("map", list.get(0));
					
				  String formatStyle ="yyyy-MM-dd HH:mm";  
				  SimpleDateFormat df = new SimpleDateFormat(formatStyle);  
				  String d1 = (String)list.get(0).get("SALES_DATE");
				  String d2 = df.format(now);
				  int month  = Utility.compareDate(d1,d2,1); //取得今日和保养开始时间的插值
				  int day = Utility.compareDate(d1, d2, 0); //取得今日和保养开始时间的插值 天数
				  Long comId ;
				  if(StringUtil.notNull(logonUser.getOemCompanyId())){
					  comId = Long.parseLong(logonUser.getOemCompanyId()) ;
				  } else
					  comId = logonUser.getCompanyId() ;
				  
				  
				  if(flag==null){
					  List<TtAsWrQamaintainPO> lsq = dao.getFree2(Integer.parseInt(list.get(0).get("FREE_TIMES").toString())+1,comId, month, day, Double.parseDouble(mile));
					  if(lsq.size()>0){
						  act.setOutData("flag", "正常");

						  //判定配件是否三包
						  if(codes!=null){
							  String[] cs = codes.split(",") ;
							  List<NewPartBean> nlist = new ArrayList() ;
							  for(int i=0;i<cs.length;i++){
								  if(!"00-000".equalsIgnoreCase(cs[i])){
								  List<NewPartBean> l = dao.getNewPartBean(cs[i],vin);
								  if(l.size()<=0)
									  l = dao.getNewPartBean2(cs[i]);
								  //begin   zhumingwei添加if判断防止dms系统中没有的件引起报错2013 2-20
								  if(l.size()>0&&l!=null){
									  nlist.add(l.get(0)) ;
								  }
								  //end     zhumingwei添加if判断防止dms系统中没有的件引起报错2013 2-20
								  }
							  }
							  WrRuleUtil util = new WrRuleUtil();
							  WarrantyPartVO wp = null;
							  for(int i=0;i<nlist.size();i++){
								  //try{}catch{}的原因是针对此配件，没定义三包就默认为不是三包的
								  try{
									  wp = util.wrRuleCompute2(d2,mile, list.get(0).get("SALES_DATE")!=null?list.get(0).get("SALES_DATE").toString():null, vin, nlist.get(i).getPartCode());
									  nlist.get(i).setIsWarranty(wp.getIsInWarranty());
								  }catch(Exception e){
									  nlist.get(i).setIsWarranty(Constant.IF_TYPE_NO);
								  }
							  }
							  
							  act.setOutData("nlist",nlist);
							  System.out.println(nlist);
						  }
					  }else{
						  if(codes!=null){
							  String[] cs = codes.split(",") ;
							  List<NewPartBean> nlist = new ArrayList() ;
							  for(int i=0;i<cs.length;i++){
								  List<NewPartBean> l = dao.getNewPartBean(cs[i],vin);
								  if(l.size()<=0){
									  //没有为此配件单独定义三包规则，则取此规则下的通用规则定义
									  l = dao.getNewPartBean2(cs[i]);
								  }else { //YH 2011.7.5
								     nlist.add(l.get(0)) ;
								  }
							  }
							  WrRuleUtil util = new WrRuleUtil();
							  WarrantyPartVO wp = null;
							  for(int i=0;i<nlist.size();i++){
								  //try{}catch{}的原因是针对此配件，没定义三包就默认为不是三包的
								  try{
									  wp = util.wrRuleCompute2(d2,mile, list.get(0).get("SALES_DATE")!=null?list.get(0).get("SALES_DATE").toString():null, vin, nlist.get(i).getPartCode());
									  nlist.get(i).setIsWarranty(wp.getIsInWarranty());
								  }catch(Exception e){
									  nlist.get(i).setIsWarranty(Constant.IF_TYPE_NO);
								  }
							  }
							  act.setOutData("nlist",nlist);
						  }
						  act.setOutData("flag", "已脱保");
					  }
				  }
				  act.setOutData("mile", mile);
				  act.setOutData("days", day);
				  act.setOutData("month", month);
				  act.setOutData("now", d2);
				  act.setOutData("yes", Constant.IF_TYPE_YES);
				  act.setOutData("no",Constant.IF_TYPE_NO);
			  }
			  
			  act.setForword(THREE_PACKAGE_SET_URL);
		  }catch(Exception e){
			  BizException e1 = new BizException(act, e,
			          ErrorCodeConstant.QUERY_FAILURE_CODE, "售前车辆 检测规则");
		      act.setOutData("success", false);
		      logger.error(logonUser, e1);
		      act.setException(e1);
		  }
	  }
	  /*
	   * 下端系统配件三包判定（索赔简报） YH 2011.9.20
	   */
	  public void claimReport(){
		  
		  AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		  try{
			  
			  String vin = request.getParamValue("VIN");
			  String mile = request.getParamValue("mile");
			  String codes = request.getParamValue("codes");
			  String roNo = request.getParamValue("roNo");
			  String ENGINE_NO = request.getParamValue("ENGINE_NO");

			  Date now = new Date(); //今天
			  List<Map<String,Object>> list = dao.getMaintainStateSet_tow(vin,ENGINE_NO);
			  //取得工单开始时间
			  if(StringUtil.notNull(roNo) && !"null".equals(roNo)){
				  TtAsRepairOrderPO po = new TtAsRepairOrderPO() ;
				  po.setRoNo(roNo);
				  List list2 = dao.select(po) ;
				  if(list2.size()>0)
					  now = ((TtAsRepairOrderPO)list2.get(0)).getRoCreateDate() ;
			  }
			  if(list.size()>0){
				  act.setOutData("map", list.get(0));
					
				  String formatStyle ="yyyy-MM-dd";  
				  SimpleDateFormat df = new SimpleDateFormat(formatStyle);  
				  String d1 = (String)list.get(0).get("SALES_DATE");
				  String d2 = df.format(now);
				  int month  = Utility.compareDate(d1,d2,1); //取得今日和保养开始时间的插值
				  int day = Utility.compareDate(d1, d2, 0); //取得今日和保养开始时间的插值 天数
				  Long comId ;
				  if(StringUtil.notNull(logonUser.getOemCompanyId())){
					  comId = Long.parseLong(logonUser.getOemCompanyId()) ;
				  } else
					  comId = logonUser.getCompanyId() ;
				  
				  List<TtAsWrQamaintainPO> lsq = dao.getFree2(Integer.parseInt(list.get(0).get("FREE_TIMES").toString())+1,comId, month, day, Double.parseDouble(mile));
				  if(lsq.size()>0){
					  act.setOutData("flag", "正常");

					  //判定配件是否三包
					  if(codes!=null){
						  String[] cs = codes.split(",") ;
						  List<NewPartBean> nlist = new ArrayList() ;
						  for(int i=0;i<cs.length;i++){
							  List<NewPartBean> l = dao.getNewPartBean(cs[i],vin);
							  
							  if(l.size()<=0)
								  l = dao.getNewPartBean2(cs[i]);
							  
							  nlist.add(l.get(0)) ;
						  }
						  WrRuleUtil util = new WrRuleUtil();
						  WarrantyPartVO wp = null;
						  for(int i=0;i<nlist.size();i++){
							  //try{}catch{}的原因是针对此配件，没定义三包就默认为不是三包的
							  try{
								  wp = util.wrRuleCompute2(d2,mile, list.get(0).get("SALES_DATE")!=null?list.get(0).get("SALES_DATE").toString():null, vin, nlist.get(i).getPartCode());
								  nlist.get(i).setIsWarranty(wp.getIsInWarranty());
							  }catch(Exception e){
								  nlist.get(i).setIsWarranty(Constant.IF_TYPE_NO);
							  }
						  }
						  
						  act.setOutData("nlist",nlist);
					  }
				  }else{
					  if(codes!=null){
						  String[] cs = codes.split(",") ;
						  List<NewPartBean> nlist = new ArrayList() ;
						  for(int i=0;i<cs.length;i++){
							  List<NewPartBean> l = dao.getNewPartBean(cs[i],vin);
							  if(l.size()<=0){
								  //没有为此配件单独定义三包规则，则取此规则下的通用规则定义
								  l = dao.getNewPartBean2(cs[i]);
							  }else { //YH 2011.7.5
							     nlist.add(l.get(0)) ;
							  }
						  }
						  WrRuleUtil util = new WrRuleUtil();
						  WarrantyPartVO wp = null;
						  for(int i=0;i<nlist.size();i++){
							  //try{}catch{}的原因是针对此配件，没定义三包就默认为不是三包的
							  try{
								  wp = util.wrRuleCompute2(d2,mile, list.get(0).get("SALES_DATE")!=null?list.get(0).get("SALES_DATE").toString():null, vin, nlist.get(i).getPartCode());
								  nlist.get(i).setIsWarranty(wp.getIsInWarranty());
							  }catch(Exception e){
								  nlist.get(i).setIsWarranty(Constant.IF_TYPE_NO);
							  }
						  }
						  act.setOutData("nlist",nlist);
					  }
					  act.setOutData("flag", "已脱保");
				  }
				  act.setOutData("CHANA", "长安汽车");
				  act.setOutData("mile", mile);
				  act.setOutData("days", day);
				  act.setOutData("month", month);
				  act.setOutData("now", d2);
				  act.setOutData("yes", Constant.IF_TYPE_YES);
				  act.setOutData("no",Constant.IF_TYPE_NO);
			  }else {
				  act.setOutData("CHANA", "非长安汽车");
			  }			  
			  act.setForword(CLAIM_REPORT_URL);
		  }catch(Exception e){
			  BizException e1 = new BizException(act, e,
			          ErrorCodeConstant.QUERY_FAILURE_CODE, "售前车辆 检测规则");
		      act.setOutData("success", false);
		      logger.error(logonUser, e1);
		      act.setException(e1);
		  }
	  }
	  public void viewEngineNo() throws Exception{
		  
		  AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		  
		  String vin = request.getParamValue("VIN");
		  
			/*****add by liuxh 20131108判断车架号不能为空*****/
			CommonUtils.jugeVinNull(vin);
			/*****add by liuxh 20131108判断车架号不能为空*****/
		  
		  String engineNo = request.getParamValue("ENGINE_NO");
		  List<TmVehiclePO> list = dao.getCar(vin);
		  Integer  is_a = 0;
		  if(list.size()>0){
			  if(list.get(0).getEngineNo().equals(engineNo)){
				  is_a=1;
			  }
		  }
		  act.setOutData("is_a", is_a);
	  }
	  
		/**
		 * 
		 * @Title: selectMainPartCodeForward
		 * @Description: TODO(选择页面跳转主配件)
		 * @param 设定文件
		 * @return void 返回类型
		 * @throws
		 */
		public void selectRulePartCodeForward() {
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				
				String roNo = request.getParamValue("roNo");
				String vin = request.getParamValue("vin");
				String groupId = request.getParamValue("GROUP_ID");
				act.setOutData("roNo", roNo);
				act.setOutData("vin", vin);
				act.setOutData("GROUP_ID", groupId);
				act.setForword(RULE_PARTCODE_URL);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
				logger.error(logonUser, e1);
				act.setException(e1);
			}

		}
	  
	  /**
		 * 
		 * @Title: queryPartCode
		 * @Description: TODO(根据三包规则查询配件)
		 * @param 设定文件
		 * @return void 返回类型
		 * @throws
		 */
		public void queryPartCodeFromRuleListByVinAndPartCode() {
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
			String dealerId = logonUser.getDealerId();
			StringBuffer con = new StringBuffer();
			Map<String, String> map = new HashMap<String, String>();
			try {
				
				String roNo = request.getParamValue("roNo");
				String vin = request.getParamValue("vin");
				String groupId = request.getParamValue("GROUP_ID"); // 车型
				String partCode = request.getParamValue("partCode");
				String partName = request.getParamValue("PART_NAME"); // 主页面中的主工时代码
				String supplierName = request.getParamValue("SUPPLIER_NAME");
				map.put("roNo", roNo);
				map.put("vin", vin);
				map.put("partCode", partCode);
				map.put("partName", partName);
				map.put("groupId", groupId);
				map.put("companyId", companyId.toString());
				map.put("supplierName", supplierName);
				Map<String,Object> ps = dao.queryPartCodeByVin(logonUser,
						map);
				//如果RULE_LIST表里没有该配件，查询该配件通用三包规则
				if(ps==null){
					ps = dao.queryUsualRulePartCode(partCode);
				}
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		/**
		 * 查询基础表配件（三包信息变更申请）
		 * add by yx 20110107
		 */
		public void queryPartCodeFromBaseForRule() {
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
			String dealerId = logonUser.getDealerId();
			StringBuffer con = new StringBuffer();
			Map<String, String> map = new HashMap<String, String>();
			try {
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				String partCode = request.getParamValue("PART_CODE");
				String partName = request.getParamValue("PART_NAME"); // 主页面中的主工时代码
				PageResult<TmPtPartBaseExtPO> ps = dao.queryPartCodeForRuleChange(
						partCode,partName, Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
	/**
	 * 供下端客户接待时查询维修记录，保养记录
	 * YH 2011.9.21
	 */	
		public void queryOEMmaintaimHistory() { 
			
			
		    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		    try {
		      
		      String vin = request.getParamValue("VIN");
		      String claimTypes = "" + Constant.CLA_TYPE_01 + "," + Constant.CLA_TYPE_07 
		                + "," + Constant.CLA_TYPE_09;
		      List<Map<String,Object>> maintaimHisList = dao.maintaimHistory(vin, claimTypes);
              List<Map<String,Object>> freeMaintaimHisList = dao.freeMaintainHistory(vin);
              
		      act.setOutData("maintaimHisList", maintaimHisList);
		      act.setOutData("freeMaintaimHisList", freeMaintaimHisList);
		      act.setOutData("VIN", vin);
		      act.setForword(this.OEM_HIS_URL);
		    }catch (Exception e) {
		      BizException e1 = new BizException(act, e,
		          ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆维修历史记录");
		      act.setOutData("success", false);
		      logger.error(logonUser, e1);
		      act.setException(e1);
		    }  
      }	
		public void getJianche(){
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				
				String activityId = request.getParamValue("id");
				String code = request.getParamValue("code");
				String yiedlyType = request.getParamValue("yiedlyType");
				if(Utility.testString(code)){
					TtAsActivityPO p = new TtAsActivityPO();
					p.setActivityCode(code);
					p = (TtAsActivityPO) dao.select(p).get(0);
					activityId = p.getActivityId().toString();
				}
				
				act.setOutData("is_add", 1);
				TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
				relationPO.setActivityId(Long.parseLong(activityId));
				relationPO.setLargessType(Long.valueOf(3537001));
				List<TtAsActivityRelationPO> list= dao.select(relationPO);
				act.setOutData("largess", list);
				act.setOutData("largess_type", "3537001");
				act.setOutData("activityId", activityId);
				act.setForword(MAIN_CHECK_POSITION);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		
		public void getZengSong(){
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				
				String activityId = request.getParamValue("id");
				String code = request.getParamValue("code");
				String yiedlyType = request.getParamValue("yiedlyType");
				if(Utility.testString(code)){
					TtAsActivityPO p = new TtAsActivityPO();
					p.setActivityCode(code);
					p = (TtAsActivityPO) dao.select(p).get(0);
					activityId = p.getActivityId().toString();
				}
				
				act.setOutData("is_add", 1);
				TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
				relationPO.setActivityId(Long.parseLong(activityId));
				relationPO.setLargessType(Long.valueOf(3537004));
				List<TtAsActivityRelationPO> list= dao.select(relationPO);
				act.setOutData("largess", list);
				act.setOutData("largess_type", "3537004");
				act.setOutData("activityId", activityId);
				act.setForword(MAIN_LARGESS_POSITION);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		public void getLabour(){
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				
				String activityId = request.getParamValue("id");
				String code = request.getParamValue("code");
				if(Utility.testString(code)){
					TtAsActivityPO p = new TtAsActivityPO();
					p.setActivityCode(code);
					p = (TtAsActivityPO) dao.select(p).get(0);
					activityId = p.getActivityId().toString();
				}
				
				act.setOutData("is_add", 1);
				TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
				relationPO.setActivityId(Long.parseLong(activityId));
				relationPO.setLargessType(Long.valueOf(3537006));
				List<TtAsActivityRelationPO> list= dao.select(relationPO);
				TtAsActivityProjectPO jp = new TtAsActivityProjectPO();
				jp.setActivityId(Long.valueOf(activityId));
				jp.setProCode(3537006);
				jp = (TtAsActivityProjectPO) dao.select(jp).get(0);
				System.out.println(jp.getAmount());
				act.setOutData("zhekou", jp.getAmount());
				act.setOutData("largess", list);
				act.setOutData("largess_type", "3537006");
				act.setOutData("activityId", activityId);
				act.setForword(MAIN_laber_POSITION);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		public void getPart(){
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				
				String activityId = request.getParamValue("id");
				String code = request.getParamValue("code");
				String yiedlyType = request.getParamValue("yiedlyType");
				if(Utility.testString(code)){
					TtAsActivityPO p = new TtAsActivityPO();
					p.setActivityCode(code);
					p = (TtAsActivityPO) dao.select(p).get(0);
					activityId = p.getActivityId().toString();
				}
				
				act.setOutData("is_add", 1);
				TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
				relationPO.setActivityId(Long.parseLong(activityId));
				relationPO.setLargessType(Long.valueOf(3537007));
				List<TtAsActivityRelationPO> list= dao.select(relationPO);
				TtAsActivityProjectPO jp = new TtAsActivityProjectPO();
				jp.setActivityId(Long.valueOf(activityId));
				jp.setProCode(3537007);
				jp = (TtAsActivityProjectPO) dao.select(jp).get(0);
				System.out.println(jp.getAmount());
				act.setOutData("zhekou", jp.getAmount());
				act.setOutData("largess", list);
				act.setOutData("largess_type", "3537007");
				act.setOutData("activityId", activityId);
				act.setForword(MAIN_Base_POSITION);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		public void open_laber()
		{

			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				
				String largess_type = request.getParamValue("largess_type");
				String activityId = request.getParamValue("activityId");
				String PART_CODE = request.getParamValue("PART_CODE");
				String PART_NAME = request.getParamValue("PART_NAME");
				
				Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.getLaber(Constant.PAGE_SIZE, largess_type, activityId, PART_CODE, PART_NAME, curPage);
				act.setOutData("ps",ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		public void selectMalCodeForward() {
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				act.setForword(MAIN_MAL_URL);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "故障代码查询失败");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		public void selectWorkHoursCodeForward() {
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				
				String codes = request.getParamValue("codes");
				act.setOutData("codes", codes);
				act.setForword(SHOW_WORKHOURS_CODE);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "工时代码查询失败");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		public void queryMal() {
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser); // 公司ID
			StringBuffer con = new StringBuffer();
			try {
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				Integer pageSize = 10;

				String malCode = request.getParamValue("MAL_CODE");//取道的是车型
				String malName = request.getParamValue("MAL_NAME"); // 取道的是车型
			
				PageResult<Map<String, Object>> ps = dao.queryMal(malCode,malName,pageSize, curPage);
				
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		
		public void selectworkHourForward() {
			
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				act.setForword(WORK_HOUR_URL);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "辅料查询失败");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		/**
		 * 补偿费供应商选择
		 */
		public void selectSupplierCodeForward() {
			String partcode =request.getParamValue("partcode");
			String id =request.getParamValue("id");
			request.setAttribute("partcode", partcode);
			request.setAttribute("id", id);
			super.sendMsgByUrl(SUPPLIER_CODE_URL, "供应商查询失败！");
		}
		/**
		 * 查询供应商数据
		 */
		public void querySupplierCode() {
			PageResult<Map<String, Object>> ps =dao.querySupplierCode(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", ps);
		}
		
		public void queryworkHour() {
			try {
				
				Integer pageSize = 10;
				String workCode = request.getParamValue("WORK_CODE");//
				String workName = request.getParamValue("WORK_NAME"); // 
			
				PageResult<Map<String, Object>> ps = dao.queryworkHour(workCode,workName,pageSize, getCurrPage());
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
				logger.error(super.loginUser, e1);
				act.setException(e1);
			}
		}
		
		public void queryWorkhoursCode() {
			Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
			StringBuffer con = new StringBuffer();
			try {
				Integer pageSize = 10;
				String workhourCode = request.getParamValue("WORKHOUR_CODE");//工时代码
				String workhourName = request.getParamValue("WORKHOUR_NAME"); // 工时名称
				PageResult<Map<String, Object>> ps = dao.queryWorkhoursCode(workhourCode,workhourName,pageSize, getCurrPage());
				
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
				logger.error(loginUser, e1);
				act.setException(e1);
			}
		}
		/**
		 * 改变供应商的值
		 */
		public void changValSupplierCode(){
			try {
				String id = DaoFactory.getParam(request, "id");
				String supplierCode = DaoFactory.getParam(request, "supplierCode");
				TtAsWrCompensationAppPO po1=new TtAsWrCompensationAppPO();
				po1.setPkid(BaseUtils.ConvertLong(id));
				TtAsWrCompensationAppPO po2=new TtAsWrCompensationAppPO();
				po2.setSupplierCode(supplierCode);
				dao.update(po1, po2);
				act.setOutData("succ", "1");
			} catch (Exception e) {
				act.setOutData("succ", "0");
				e.printStackTrace();
			}
		}
		/**
		 * 选择工单后,执行该方法，查询出所有的数据，然后直接扔向前台 zyw 重新维护2014-11-6
		 */
		public void changeRoDetail2() {
			try { 
				String roId = getParam("roId");// 工单ID
				if("".equals(roId)){
					logger.error("changeRoDetail方法中roId为空");
					throw new BizException("roId为空!");
				}
				DecimalFormat format = new DecimalFormat("###0.00");
				Double tatol =0.0;
				/**
				 *  新增时需要循环所有字表进行总费用的累加
				 *  总费用=工时+配件+其他+补偿+辅料
				 */
				TtAsRepairOrderExtPO tarp = dao.getDeailBeanById(roId);//工单基础数据BEAN 查询工单数据
				String roNo = tarp.getRoNo();
				List<Map<String, Object>> mainCodeList = dao.queryMainList2(roId);//得到工单主因件集合
				List<Map<String, Object>> labours = dao.queryItemls(roId);//得到工时
				List<Map<String, Object>> laboursTemp = dao.queryItemlsTemp(roId);//得到工时
				List<Map<String, Object>> parts = dao.queryPartLs(roId);//得到配件
				List<Map<String, Object>> others = dao.queryOtherLs(roId);//得到其他项目
				List<Map<String, Object>> accs = dao.getAccessoryDtl(roNo);//辅料
				List<Map<String, Object>> comMoneys = dao.getCompensationMoney(roNo);//补偿
				List<FsFileuploadPO> attachLs = dao.queryAttById(roId);// 取得附件
				
				if(checkListNull(laboursTemp)){
					tatol= getListCount(tatol,laboursTemp, "LABOUR_AMOUNT");
				}
				if(checkListNull(parts)){
					tatol= getListCount(tatol,parts, "PART_COST_AMOUNT");
				}
				if(checkListNull(others)){
					tatol= getListCount(tatol,others, "ADD_ITEM_AMOUNT");
				}
				if(checkListNull(accs)){
					tatol= getListCount(tatol,accs, "PRICE");
				}
				if(checkListNull(comMoneys)){
					tatol= getListCount(tatol,comMoneys, "APPLY_PRICE");
				}
				act.setOutData("tatol", format.format(tatol));
				request.setAttribute("roId", roId);
				act.setOutData("mainCodeList", mainCodeList);
				act.setOutData("itemLs", labours);
				act.setOutData("partLs", parts);
				act.setOutData("otherLs", others);
				act.setOutData("roBean", tarp);
				act.setOutData("attachLs", attachLs);
				act.setOutData("compensationMoneyList", comMoneys);//补偿
				act.setOutData("accessoryDtlList", accs);
				request.setAttribute("roNo", roNo);
				act.setOutData("size", mainCodeList.size());
				String repairType = tarp.getRepairTypeCode();
				String claimType=dao.changeRoCodeToClaimCode(repairType);
				request.setAttribute("claimType", claimType);
				if((repairType.equals(Constant.REPAIR_TYPE_05)&&this.jugeReplce(tarp.getCamCode())) ||repairType.equals(Constant.REPAIR_TYPE_04)||repairType.equals(Constant.REPAIR_TYPE_08)){
					//如果工单是保养或者PDI则直接跳转 如果不是切换件的服务活动
					act.setForword(sendUrl(this.getClass(), "2claimBillMaintainAdd"));
				}else{
					act.setForword(sendUrl(this.getClass(), "2claimBillMaintainAdd2"));
				}
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "工单新增跳转");
				logger.error(loginUser, e1);
				act.setException(e1);
			}
		}

		@SuppressWarnings("rawtypes")
		public boolean jugeReplce(String code){
			boolean flag=false;
			TtAsActivityPO po=new TtAsActivityPO();
			po.setActivityCode(code);
			po.setActivityType(Constant.SERVICEACTIVITY_TYPE_05);
			List select = dao.select(po);
			if(select!=null && select.size()==0){
				flag=true;
			}
			return flag;
		}
		/**
		 * 判断库存验证的数据 zyw 2014-9-26
		 */
		public void checkStoreBypartCodeAndDealerid(){
			try {
				Long dealerid = getCurrDealerId();
				StringBuffer sb=new StringBuffer();
				String pids = DaoFactory.getParam(request, "pids");
				if(!"".equals(pids)){
					String[] ids=pids.split(",");
					for (int i = 0; i < ids.length; i++) {
						String[] downPartCodes = request.getParamValues("DOWN_PART_CODE"+ids[i]); // 换下件代码
						for (String partcode : downPartCodes) {
							boolean bool=dao.checkStoreBypartCodeAndDealerid(partcode,dealerid);
							if(bool==false){
								sb.append(partcode+"这个配件库存不足或没有库存；");
							}
						}
						
					}
				}
				act.setOutData("check", "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
