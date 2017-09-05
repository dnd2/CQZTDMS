package com.infodms.dms.actions.vehicleInfoManage.apply;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsBarcodeApplyBean;
import com.infodms.dms.bean.TtAsPackgeChangeDetailBean;
import com.infodms.dms.bean.TtVehicleChangeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.vehicleInfoManage.QualityReportInfoDao;
import com.infodms.dms.dao.vehicleInfoManage.VehicleInfoManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.QualityReportInfoMaintasinPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVehiclePinRequestPO;
import com.infodms.dms.po.TtAsBarcodeApplyPO;
import com.infodms.dms.po.TtAsPackgeChangeApplyPO;
import com.infodms.dms.po.TtAsPackgeChangeDetailPO;
import com.infodms.dms.po.TtAsPinSearchDetailPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrQamaintainPO;
import com.infodms.dms.po.TtVehicleChangePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: VehicleInfoChangeApply 
* @Description: TODO(车辆变更申请) 
* @author liuqiang 
* @date Oct 28, 2010 6:11:34 PM 
*
 */
public class VehicleInfoChangeApply extends BaseAction implements Constant {
	public Logger logger = Logger.getLogger(VehicleInfoChangeApply.class);
	private VehicleInfoManageDao dao = VehicleInfoManageDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	ClaimReportDao dao1  = ClaimReportDao.getInstance();
	//车辆信息变更申请查询页面  经销商页面
	private final String INIT_URL = "/jsp/vehicleInfoManage/apply/vehicleInfoChangeApplyQuery.jsp";
	//车辆信息变更申请查询页面  车厂页面
	private final String OEM_INIT_URL = "/jsp/vehicleInfoManage/apply/vehicleInfoChangeApplyOemQuery.jsp";
	//车辆信息变更申请新增页面(经销商页面)
	private final String ADD_URL = "/jsp/vehicleInfoManage/apply/vehicleInfoChangeApplyAdd.jsp";
   //车辆信息变更申请新增页面（车厂页面）
	private final String OEM_ADD_URL = "/jsp/vehicleInfoManage/apply/vehicleInfoChangeApplyOemAdd.jsp";
	//车辆信息变更申请修改页面
	private final String MOD_URL = "/jsp/vehicleInfoManage/apply/vehicleInfoChangeApplyMod.jsp";
	//车辆信息变更申请修改页面 车厂用户
	private final String OEM_MOD_URL = "/jsp/vehicleInfoManage/apply/vehicleInfoChangeApplyOemMod.jsp";
	//车辆信息变更申请详细页面
	private final String VIEW_URL = "/jsp/vehicleInfoManage/apply/vehicleInfoChangeApplyView.jsp";
	//查询维修工单页面
	private final String SHOW_ORDER = "/jsp/vehicleInfoManage/apply/showOrder.jsp";
	//PIN查询页面
	private final String VEHICLE_PIN_REQUEST_URL = "/jsp/vehicleInfoManage/apply/vehiclePinRequestQuery.jsp";
	//PIN新增页面
	private final String VEHICLE_PIN_ADD_URL = "/jsp/vehicleInfoManage/apply/vehiclePinRequestAdd.jsp";
	//PIN修改页面
	private final String VEHICLE_PIN_DETAIL_URL = "/jsp/vehicleInfoManage/apply/vehiclePinRequestDetail.jsp";
	//车辆三包规则变化申请页面
	private final String VEHICLE_RULE_CHANGE_REQUEST = "/jsp/vehicleInfoManage/apply/vehicleRuleInfoChangeApplyQuery.jsp";
	//车辆信息变更申请新增页面(经销商页面)
	private final String VEHICLE_RULE_CHANGE_ADD = "/jsp/vehicleInfoManage/apply/vehicleRuleChangeApplyAdd.jsp";
	//车辆信息变更申请修改页面(经销商页面)
	private final String VEHICLE_RULE_CHANGE_MODIFY = "/jsp/vehicleInfoManage/apply/vehicleRuleChangeApplyModify.jsp";
	//车辆信息变更申请修改页面(经销商页面)
	private final String VEHICLE_RULE_CHANGE_VIEW = "/jsp/vehicleInfoManage/apply/vehicleRuleChangeApplyView.jsp";
	
	//条码申请查询
	private final String BAR_CODE_URL = "/jsp/vehicleInfoManage/apply/barCodeApplyPer.jsp";
	private final String BAR_CODE_APPLY_ADD="/jsp/vehicleInfoManage/apply/barCodeApplyAdd.jsp";
	private final String BAR_CODE_URL2 = "/jsp/vehicleInfoManage/apply/barCodeApplyPer2.jsp";
	private final String BAR_CODE_URL3 = "/jsp/vehicleInfoManage/apply/barCodeApplyPer3.jsp";
	private final String BAR_CODE_PRINT = "/jsp/vehicleInfoManage/apply/barCodeApplyPrint.jsp";
	private final String BAR_CODE_PRINT2 = "/jsp/vehicleInfoManage/apply/barCodeApplyPrint2.jsp";
	//三包凭证补办申请
	private final String PACKGE_APPLY_URL = "/jsp/vehicleInfoManage/apply/packgeApplyPer.jsp";
	private final String PACKGE_APPLY_ADD="/jsp/vehicleInfoManage/apply/packgeApplyAdd.jsp";
	private final String PACKGE_APPLY_URL2 = "/jsp/vehicleInfoManage/apply/packgeApplyPer2.jsp";
	private final String PACKGE_APPLY_URL3 = "/jsp/vehicleInfoManage/apply/packgeApplyPer3.jsp";
	private final String PACKGE_APPLY_AUDIT = "/jsp/vehicleInfoManage/apply/packgeApplyAudit.jsp";
	private final String PACKGE_APPLY_DETAIL = "/jsp/vehicleInfoManage/apply/packgeApplyDetail.jsp";
	private final String PACKGE_APPLY_PRINT = "/jsp/vehicleInfoManage/apply/packgeApplyPrint.jsp";
	
	private final String PIN_SEARCH_URL = "/jsp/vehicleInfoManage/apply/vehiclePinSearch.jsp";
	private final String PIN_SEARCH_DETAIL_URL = "/jsp/vehicleInfoManage/apply/vehiclePinSearchDetail.jsp";
	private final String CAR_ALARM = "/jsp/vehicleInfoManage/apply/carAlarm.jsp";
	
	private PageResult<Map<String, Object>> list=null;
	
	
	private Integer getPage(int type){
		Integer page_size=0;
		if(type==1){
			page_size=Constant.PAGE_SIZE;
		}else if(type==2){
			page_size=Constant.PAGE_SIZE_MAX;
		}
		return page_size;
	}
	protected Integer getCurrPage() {
		return request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
	}
	/**
	 * 
	* @Title: vehicleInfoChangeApplyInit 
	* @Description: TODO(经销商车辆变更申请初始页面) 
	 */
	public void vehicleInfoChangeApplyInit() {
		try {
			act.setForword(INIT_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: vehicleInfoChangeApplyOemInit 
	* @Description: TODO(OEM车辆变更申请初始页面) 
	* @throws
	 */
	public void vehicleInfoChangeApplyOemInit() {
		try {
			act.setForword(OEM_INIT_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: applyAdd 
	* @Description: TODO(变更申请新增页面) 
	 */
	public void applyAdd() {
		try {
			List<TtAsWrGamePO> wrGames = null;
			if (Utility.testString(logonUser.getOemCompanyId())) {
				//经销商查询
				wrGames = dao.queryWrGame(Long.parseLong(logonUser.getOemCompanyId()));//所有三包策略
			} else {
				//车厂用户查询
				wrGames = dao.queryWrGame(logonUser.getCompanyId());//所有三包策略
			}
			act.setOutData("wrGames", wrGames);
			if (Utility.testString(logonUser.getOemCompanyId())){
				TmDealerPO p = new TmDealerPO();
				p.setDealerId(Long.valueOf(logonUser.getDealerId()));
				List<TmDealerPO> dealerList = dao.select(p);
				act.setOutData("isScan",dealerList.get(0).getIsScan() );
				act.setForword(ADD_URL);
			}else {
				act.setForword(OEM_ADD_URL);
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: doSave 
	* @Description: TODO(第一次保存) 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void doSave() {
		try {
			TtVehicleChangePO po = assembleVehicleChangePO();
			dao.insert(po);
			
			//附件管理
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.delAllFilesUploadByBusiness(po.getId().toString(), fjids);
			FileUploadManager.fileUploadByBusiness(po.getId().toString(), fjids, logonUser);
			act.setOutData("msgs", "操作成功!");
			//act.setForword(INIT_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: doOemSave 
	* @Description: TODO(车厂售后保存) 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void doOemSave() { //YH 2010.11.12
		try {
			TtVehicleChangePO po = assembleVehicleChangePO();
			dao.insert(po);
			TmVehiclePO svehicle = new TmVehiclePO();//条件po
			svehicle.setVin(po.getVin());
			TmVehiclePO dvehicle = new TmVehiclePO();//更改数据po
			
			dvehicle.setVin(po.getVin());
			
			String changeType = CommonUtils.checkNull(request.getParamValue("changeType"));//申请类型	
			dvehicle = getTmVehiclePOByType(Integer.parseInt(changeType),po);
			dao.update(svehicle, dvehicle);//更新车辆表数据
			act.setForword(OEM_INIT_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: getTmVehiclePOByType 
	* @Description: TODO(根据申请类型取车辆表更改数据) 
	* @param @param changeType 申请类型
	* @param @return  申请数据
	 * @throws Exception 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private TmVehiclePO getTmVehiclePOByType(Integer changeType,TtVehicleChangePO po) throws Exception {
		TmVehiclePO vehicle = new TmVehiclePO();
		if (changeType.intValue() == VEHICLE_CHANGE_TYPE_01.intValue()) {//行驶里程变更
			vehicle.setMileage(Double.parseDouble(po.getApplyData()));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_02) {//保养次数变更
			vehicle.setFreeTimes(Integer.parseInt(po.getApplyData()));
			Double mileage = dao.getMileage(po.getErrorRoCode(), po.getVin());
			if(mileage>=0){//如果查到的结果大于0 ,说明有维修历史，并将里程变为工单中最大的进厂里程
				vehicle.setMileage(mileage);
			}
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_03) {//购车时间变更
			vehicle.setPurchasedDate(DateTimeUtil.stringToDate(po.getApplyData()));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_04) {//三包策略变更
			TtAsWrGamePO game = new TtAsWrGamePO();
			game.setGameCode(po.getApplyData());
			List<TtAsWrGamePO> pos = dao.select(game);
			vehicle.setClaimTacticsId(pos.get(0).getId());
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_05) {//车主信息变更
			
		} else {
			throw new IllegalArgumentException("Invalid changeType, changeType == " + changeType);
		}
		return vehicle;
	}
	
	/**
	 * 
	* @Title: doMod 
	* @Description: TODO(修改) 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void doMod() {
		try {
			String id = request.getParamValue("id");
			TtVehicleChangePO spo = new TtVehicleChangePO();
			spo.setId(Long.parseLong(id));
			TtVehicleChangePO dpo = assembleVehicleUpdatePO();
			dao.update(spo, dpo);
			
			//附件管理
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.delAllFilesUploadByBusiness(id, fjids);
			FileUploadManager.fileUploadByBusiness(id, fjids, logonUser);
			act.setOutData("msgs", "操作成功!");
			//act.setForword(INIT_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: assembleVehicleUpdatePO 
	* @Description: TODO(组装车辆变更信息更新的PO) 
	* @return TtVehicleChangePO    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private TtVehicleChangePO assembleVehicleUpdatePO() throws Exception {
		String modFlag = request.getParamValue("modFlag");//判断车辆信息修改页面保存还是提报 0:保存 1:提报
		TtVehicleChangePO dpo = new TtVehicleChangePO();
		String changeType = CommonUtils.checkNull(request.getParamValue("changeType"));//申请类型
		String applyData = getChangeDataByType(Integer.parseInt(changeType));//申请数据
		String errorDealerId = CommonUtils.checkNull(request.getParamValue("errorDealerId"));//数据提报错误经销商ID
		String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注
		String cPurchaseDate = request.getParamValue("c_purchase_date");
		String cCtmName = request.getParamValue("c_ctm_name");
		String cCtmPhone = request.getParamValue("c_ctm_phone");
		String cCtmAddress = request.getParamValue("c_ctm_address");
		//remark = new String(remark.getBytes("ISO-8859-1"), "GB2312");
		dpo.setApplyType(Integer.parseInt(changeType));
		dpo.setApplyData(applyData);
		getRoAndDealerByChangeType(dpo, Integer.parseInt(changeType));
		if(Utility.testString(cCtmName)){
			dpo.setcCtmName(cCtmName);
		}
		if(Utility.testString(cCtmPhone)){
			dpo.setcCtmPhone(cCtmPhone);
		}
		if(Utility.testString(cPurchaseDate)){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date d = fmt.parse(cPurchaseDate);
			dpo.setcPurchaseDate(d);
		}
		if(Utility.testString(cCtmAddress)){
			dpo.setcCtmAddress(cCtmAddress);
		}
		if (Utility.testString(errorDealerId)) {
			dpo.setErrorDealerId(Long.parseLong(errorDealerId));
		}
		if (Utility.testString(request.getParamValue("errorType"))) {
			dpo.setErrorType(Integer.parseInt(request.getParamValue("errorType")));
		}
		if (Utility.testString(request.getParamValue("troMileage"))) {//工单里程
			dpo.setRoMileage(Double.parseDouble(request.getParamValue("troMileage")));
		}
		if (Utility.testString(request.getParamValue("troFreeTimes"))) {//工单保养次数
			dpo.setRoFreeTimes(Integer.parseInt(request.getParamValue("troFreeTimes")));
		}
		if (Utility.testString(request.getParamValue("tcMileage"))) {//工单最小里程
			dpo.setCMileage(Double.parseDouble(request.getParamValue("tcMileage")));
		}
		if (Utility.testString(request.getParamValue("tcFreeTimes"))) {//工单可变保养次数
			dpo.setCFreeTimes(request.getParamValue("tcFreeTimes"));
		}
		dpo.setApplyRemark(remark);
		dpo.setUpdateBy(logonUser.getUserId());
		Date sysDate = new Date();
		dpo.setUpdateDate(sysDate);
		dpo.setVin(request.getParamValue("vin"));
		if ("1".equals(modFlag)) {
			//将状态更新成审核中
			if (Integer.parseInt(changeType) == VEHICLE_CHANGE_TYPE_01.intValue() || 
					Integer.parseInt(changeType) == VEHICLE_CHANGE_TYPE_02.intValue()) {//如果是行驶里程或保养次数变更,直接将状态置成审核通过
				dpo.setStatus(Constant.VEHICLE_CHANGE_STATUS_03);
				TmVehiclePO svehicle = new TmVehiclePO();//条件po
				svehicle.setVin(request.getParamValue("vin"));
				TmVehiclePO dvehicle = new TmVehiclePO();//更改数据po
				dvehicle = getTmVehiclePOByType(Integer.parseInt(changeType), dpo);
				dao.update(svehicle, dvehicle);//更新车辆表数据
				updateRepair(dpo.getErrorRoCode(), sysDate);//更新工单表为问题工单
			} else {
				dpo.setStatus(Constant.VEHICLE_CHANGE_STATUS_02);
			}
			dpo.setUpdateBy(logonUser.getUserId());
			dpo.setUpdateDate(sysDate);
			dpo.setApplyDate(sysDate);
			if (Utility.testString(logonUser.getDealerId())) {
				dpo.setApplyPerson(Long.parseLong(logonUser.getDealerId()));
			} else {
				dpo.setApplyPerson(logonUser.getUserId());
			}	
		}
		
		return dpo;
	}
	
	@SuppressWarnings("unchecked")
	private TtVehicleChangePO assembleVehicleChangePO() throws Exception {
		String addFlag = request.getParamValue("addFlag");//判断车辆信息新增页面保存还是提报 0:保存 1:提报
		String vin = CommonUtils.checkNull(request.getParamValue("vin")); //vin
		String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));//发动机号
		String vehicleNo = CommonUtils.checkNull(request.getParamValue("vehicleNo"));//牌照号
		String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));//产地
		String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));//车系
		String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));//车型
		String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));//购车日期
		String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//行驶里程
		String freeTimes = CommonUtils.checkNull(request.getParamValue("freeTimes"));//保养次数
		String ctmId = CommonUtils.checkNull(request.getParamValue("ctmId"));//客户ID
		//String mainPhone = CommonUtils.checkNull(request.getParamValue("mainPhone"));//客户电话
		//String gameCode = CommonUtils.checkNull(request.getParamValue("gameCode"));//三包策略代码
		String gameId = CommonUtils.checkNull(request.getParamValue("gameId"));//三包策略ID
		String changeType = CommonUtils.checkNull(request.getParamValue("changeType"));//申请类型
		String applyData = getChangeDataByType(Integer.parseInt(changeType));//申请数据
		
		String errorType = CommonUtils.checkNull(request.getParamValue("errorType"));//数据提报错误类型
		String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注
		//remark = new String(remark.getBytes("ISO-8859-1"), "utf-8");
		String cPurchaseDate = request.getParamValue("c_purchase_date");
		String cCtmName = request.getParamValue("c_ctm_name");
		String cCtmPhone = request.getParamValue("c_ctm_phone");
		String cCtmAddress = request.getParamValue("c_ctm_address");
		
		//zhumingwei 2012-07-16
		String ctmName = request.getParamValue("ctmName");;//老车主姓名
		String ctmPhone = request.getParamValue("ctmPhone");//老车主电话
		String ctmAddress = request.getParamValue("ctmAddress");//老车主地址
		
		TtVehicleChangePO po = new TtVehicleChangePO();
		po.setVin(vin);
		po.setEngineNo(engineNo);
		po.setVehicleNo(vehicleNo);
		po.setYieldly(yieldly);
		po.setSeriesId(Long.parseLong(seriesId));
		po.setModelId(Long.parseLong(modelId));
		try {
			po.setPurchasedDate(DateTimeUtil.stringToDate(purchasedDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(Utility.testString(cCtmName)){
			po.setcCtmName(cCtmName);
		}
		if(Utility.testString(cCtmPhone)){
			po.setcCtmPhone(cCtmPhone);
		}
		if(Utility.testString(cPurchaseDate)){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date d = fmt.parse(cPurchaseDate);
			po.setcPurchaseDate(d);
		}
		if(Utility.testString(cCtmAddress)){
			po.setcCtmAddress(cCtmAddress);
		}
		//zhumingwei 2012-07-16 begin
		if(Utility.testString(ctmName)){
			po.setOldCtmName(ctmName);
		}
		if(Utility.testString(ctmPhone)){
			po.setOldCtmPhone(ctmPhone);
		}
		if(Utility.testString(ctmAddress)){
			po.setOldCtmAddress(ctmAddress);
		}
		//zhumingwei 2012-07-16 end
		if (Utility.testString(mileage)) {
			po.setMileage(Double.parseDouble(mileage));
		}
		if (Utility.testString(freeTimes)) {
			po.setFreeTimes(Integer.parseInt(freeTimes));
		}
		if (Utility.testString(ctmId)) {
			po.setCtmId(Long.parseLong(ctmId));
		}
		if (Utility.testString(gameId)) {
			po.setClaimTacticsId(Long.parseLong(gameId));
		}
		if (Utility.testString(changeType)) {
			po.setApplyType(Integer.parseInt(changeType));
		}
		po.setApplyData(applyData);
		//zhumingwei add 2011-6-20 begin
		String errorRoCode = CommonUtils.checkNull(request.getParamValue("errorRoCode"));
		if (Utility.testString(errorRoCode)) {
			TtAsRepairOrderPO roPo = new TtAsRepairOrderPO();
			roPo.setRoNo(errorRoCode);
			TtAsRepairOrderPO roPoValue = (TtAsRepairOrderPO)dao.select(roPo).get(0);
			long aaa = roPoValue.getDealerId();
			po.setErrorDealerId(roPoValue.getDealerId());
		}
		//zhumingwei add 2011-6-20 end
		if (Utility.testString(errorType)) {
			po.setErrorType(Integer.parseInt(errorType));
		}
		getRoAndDealerByChangeType(po, Integer.parseInt(changeType));
		if (Utility.testString(request.getParamValue("troMileage"))) {//工单里程
			po.setRoMileage(Double.parseDouble(request.getParamValue("troMileage")));
		}
		if (Utility.testString(request.getParamValue("troFreeTimes"))) {//工单保养次数
			po.setRoFreeTimes(Integer.parseInt(request.getParamValue("troFreeTimes")));
		}
		if (Utility.testString(request.getParamValue("tcMileage"))) {//工单最小里程
			po.setCMileage(Double.parseDouble(request.getParamValue("tcMileage")));
		}
		if (Utility.testString(request.getParamValue("tcFreeTimes"))) {//工单可变保养次数
			po.setCFreeTimes(request.getParamValue("tcFreeTimes"));
		}
		po.setApplyRemark(remark);
		Date sysDate = new Date();
		po.setCreateDate(sysDate);
		if (Utility.testString(logonUser.getDealerId())) {
			//经销商保存
			po.setCreateBy(Long.parseLong(logonUser.getDealerId()));
			po.setApplyPerson(Long.parseLong(logonUser.getDealerId()));//申请人 经销商用户
		} else {
			//车厂保存
			po.setCreateBy(logonUser.getUserId());
			po.setApplyPerson(logonUser.getUserId());//车厂用户
		}
		po.setId(Long.parseLong(SequenceManager.getSequence("")));
		po.setIsDel(STATUS_ENABLE);
		if ("0".equals(addFlag)) {
			//保存操作
			po.setStatus(VEHICLE_CHANGE_STATUS_01);//已保存
		}else {
			//提报操作
			if (Integer.parseInt(changeType) == VEHICLE_CHANGE_TYPE_01.intValue() || 
					Integer.parseInt(changeType) == VEHICLE_CHANGE_TYPE_02.intValue()) {//如果是行驶里程或保养次数变更,直接将状态置成审核通过
				po.setStatus(Constant.VEHICLE_CHANGE_STATUS_03); //审核通过
				po.setCheckDate(new Date());
				TmVehiclePO svehicle = new TmVehiclePO();//条件po
				svehicle.setVin(vin);
				TmVehiclePO dvehicle = new TmVehiclePO();//更改数据po
				System.out.println(vin+"-------"+po.getVin());
				dvehicle = getTmVehiclePOByType(Integer.parseInt(changeType), po);  
				dao.update(svehicle, dvehicle);//更新车辆表数据
				updateRepair(po.getErrorRoCode(), sysDate);//更新工单表为问题工单
			} else {
				po.setStatus(VEHICLE_CHANGE_STATUS_02);//审核中
			}
			po.setApplyDate(sysDate);//申请时间
			//if (null != logonUser.getDealerId()) {
				//po.setApplyPerson(Long.parseLong(logonUser.getDealerId()));//申请人 经销商用户
			//} else {
				//po.setApplyPerson(logonUser.getUserId());//车厂用户
			//}
		}
		return po;
	}
	/**
	 * 
	* @Title: getRoAndDealerByChangeType 
	* @Description: TODO(根据下拉框取值 分行驶里程变更和保养次数变更) 
	* @param @param changeType 变更类型
	* @return TtVehicleChangePO    返回类型 
	* @throws
	 */
	private TtVehicleChangePO getRoAndDealerByChangeType(TtVehicleChangePO po, Integer changeType) {
		if (changeType.intValue() == VEHICLE_CHANGE_TYPE_01.intValue()) {//行驶里程变更
			if (Utility.testString(request.getParamValue("errorRoCode"))) {//错误工单编号
				po.setErrorRoCode(request.getParamValue("errorRoCode"));
			}
			if (Utility.testString(request.getParamValue("troDealerCode"))) {//工单经销商
				po.setRoDealerCode(request.getParamValue("troDealerCode"));
			}
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_02) {//保养次数变更
			String errorRoCode = CommonUtils.checkNull(request.getParamValue("ferrorRoCode"));//错误工单编号
			if (Utility.testString(errorRoCode)) {
				po.setErrorRoCode(errorRoCode);
			}
			if (Utility.testString(request.getParamValue("ftroDealerCode"))) {//工单经销商
				po.setRoDealerCode(request.getParamValue("ftroDealerCode"));
			}
		} 
		return po;
	}
	
	/**
	 * 
	* @Title: getChangeDataByType 
	* @Description: TODO(根据申请类型取申请数据) 
	* @param @param changeType 申请类型
	* @param @return  申请数据
	* @throws
	 */
	private String getChangeDataByType(Integer changeType) {
		if (changeType.intValue() == VEHICLE_CHANGE_TYPE_01.intValue()) {//行驶里程变更
			return CommonUtils.checkNull(request.getParamValue("cmileage"));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_02) {//保养次数变更
			return CommonUtils.checkNull(request.getParamValue("cfree_times"));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_03) {//购车时间变更
			return CommonUtils.checkNull(request.getParamValue("c_purchased_date"));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_04) {//三包策略变更
			return CommonUtils.checkNull(request.getParamValue("cwrGames"));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_05) {//车主信息变更
			return CommonUtils.checkNull(request.getParamValue("ctmInfo"));
		}else {
			throw new IllegalArgumentException("Invalid changeType, changeType == " + changeType);
		}
	}
	
	/**
	 * 
	* @Title: queryVehicle 
	* @Description: TODO(查询所有三包策略) 
	 */
	public void queryVehicle() {
		try {
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			Map<String, Object> map = dao.queryVehicleInfoByVin(vin);//车辆信息
			int i=0;//判断是否售前车
			if(map!=null){
			/*三包信息变更是否保养判定*/
			  Date now = new Date(); //今天
				
			  String formatStyle ="yyyy-MM-dd";  
			  SimpleDateFormat df = new SimpleDateFormat(formatStyle); 
			  String d1 = "";
			  
			  //取得购买日期
			  try{
				   if(map.get("PURCHASED_DATE")!=null){
					   i=1;
				    d1 = map.get("PURCHASED_DATE").toString();//(String)list.get(0).get("SALES_DATE");
				   }
			  }catch(Exception e){
					
				    BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"该车没有购车日期");
					logger.error(logonUser,e1);
					act.setException(e1);
			  }
			  String d2 = df.format(now);
			  int month  = Utility.compareDate(d1,d2,1); //取得今日和保养开始时间的插值
			  int day = Utility.compareDate(d1, d2, 0); //取得今日和保养开始时间的插值 天数
			  Long comId ;
			  if(StringUtil.notNull(logonUser.getOemCompanyId())){
				  comId = Long.parseLong(logonUser.getOemCompanyId()) ;
				  System.out.println(comId);
			  } else{
				  comId = logonUser.getCompanyId() ;
			  }
			  
			  List<TtAsWrQamaintainPO> lsq = ClaimBillMaintainDAO.getInstance().getFree2(Integer.parseInt(map.get("FREE_TIMES")==null?"0":map.get("FREE_TIMES").toString())+1,comId, month, day, Double.parseDouble(map.get("MILEAGE").toString()));
			  if(lsq.size()>0){
				  act.setOutData("flag", "YES");
			  }
			  else{
				  act.setOutData("flag", "NO");
			  }
			}
			  /*三包信息变更是否保养判断完*/
			act.setOutData("vehicle", map);
			act.setOutData("succ", i);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: queryVehicleChangeInfo 
	* @Description: TODO(分页查询车辆变更信息) 
	* @throws
	 */
	public void queryVehicleChangeInfo() {
		try {
			Integer curPage = request.getParamValue("curPage") != null ?
					Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.queryVehicleChangeInfo(assembleQueryBean(), curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: assembleQueryPo 
	* @Description: TODO(组装查询条件) 
	* @return TtVehicleChangePO    返回类型 
	* @throws
	 */
	private TtVehicleChangeBean assembleQueryBean() {
		String vin = request.getParamValue("vin");
		String vehicleNo = request.getParamValue("vehicleNo");
		String changeType = request.getParamValue("changeType");
		String status = request.getParamValue("status");
		TtVehicleChangeBean bean = new TtVehicleChangeBean();
		bean.setVin(vin);
		bean.setVehicleNo(vehicleNo);
		if (Utility.testString(changeType)) {
			bean.setApplyType(Integer.parseInt(changeType));
		}
		if (Utility.testString(status)) {
			bean.setStatus(Integer.parseInt(status));
		}
		if (Utility.testString(logonUser.getDealerId())) {
			bean.setCreateBy(Long.parseLong(logonUser.getDealerId()));
		} else {
			bean.setCreateBy(logonUser.getUserId());
		}
		return bean;
	}
	/**
	 * 
	* @Title: queryVehicleChangeInfoById 
	* @Description: TODO(根据id查询车辆详细信息) 
	* @throws
	 */
	public void queryVehicleChangeInfoById() {
		try {
			String id = request.getParamValue("id");
			String viewFlag = request.getParamValue("viewFlag");//0:修改页面 1:详细页面
			//车辆变更详细信息
			Map<String, Object> map = dao.queryVehicleChangeInfoById(Long.parseLong(id));
			act.setOutData("map", map);
			
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			act.setOutData("fileList", fileList);
			
			if ("0".equals(viewFlag)) { //修改详细页面
				List<TtAsWrGamePO> wrGames = null;
				if (Utility.testString(logonUser.getOemCompanyId())) {
					//经销商查询
					wrGames = dao.queryWrGame(Long.parseLong(logonUser.getOemCompanyId()));//所有三包策略
				} else {
					//车厂用户查询
					wrGames = dao.queryWrGame(logonUser.getCompanyId());//所有三包策略
				}
				act.setOutData("wrGames", wrGames);
				if (Utility.testString(logonUser.getOemCompanyId())){
					act.setForword(MOD_URL);
				}else {
					act.setForword(OEM_MOD_URL);
				}
			} else { //查看详细页面
				act.setForword(VIEW_URL);
			}
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: modForward 
	* @Description: TODO(跳转到修改页面) 
	* @return void    返回类型 
	* @throws
	 */
	public void modForward() {
		try {
			String id = request.getParamValue("id");
			act.setOutData("ID", id);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			act.setOutData("fileList", fileList);
			if (Utility.testString(logonUser.getOemCompanyId())){
				act.setForword(MOD_URL);
			}else {
				act.setForword(OEM_MOD_URL);
			}
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	public void queryOrder() {
		try {
			String vin = request.getParamValue("vin");
			Integer curPage = request.getParamValue("curPage") != null ?
					Integer.parseInt(request.getParamValue("curPage")) : 1;
			//String selectType = request.getParamValue("selectType");//判断是行驶里程修改还是保养次数修改
			PageResult<Map<String, Object>> ps = dao.queryRoByVin(vin,logonUser.getDealerId(), curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
//			if (Utility.testString(selectType)) {
//			if (selectType.equals("13141001")) {
//				ps = dao.queryRoByVin(vin, curPage, Constant.PAGE_SIZE);
//			} else {
//				ps = dao.queryRoByVin1(vin, curPage, Constant.PAGE_SIZE);
//			}
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: queryOrderByFreeTimes 
	* @Description: TODO(根据保养次数查询保养类型的工单) 
	* @throws
	 */
	public void queryOrderByFreeTimes() {
		try {
			String vin = request.getParamValue("vin");
			int freeTimes = Integer.parseInt(request.getParamValue("cfree_times"));//保养次数变更
			//保养次数变更找变更数据的+1工单  比如3-5  变更为3  要把第四次的置成问题单据
			Map<String, Object> ps = dao.queryRoByVinAndFreeTimes(vin, freeTimes + 1);
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: updateRepair 
	* @Description: TODO(做完提报操作,更新该工单为问题工单) 
	* @param @param errorRoCode 错误的工单编号
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private void updateRepair(String errorRoCode, Date sysDate) {
		if (Utility.testString(errorRoCode)) {
			TtAsRepairOrderPO sorder = new TtAsRepairOrderPO();
			sorder.setRoNo(errorRoCode);
			TtAsRepairOrderPO dorder = new TtAsRepairOrderPO();
			dorder.setOrderValuableType(Constant.RO_PRO_STATUS_02);
			dorder.setUpdateBy(logonUser.getUserId());
			dorder.setUpdateDate(sysDate);
			dao.update(sorder, dorder);
		}
	}
	/**
	 * 经销商端车辆PIN订单查询申报
	 * writed by yx 20101215
	 */
	public void queryVehiclePinRequest(){
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String pinCode = CommonUtils.checkNull(request.getParamValue("pinCode"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				String pinCreateDate = CommonUtils.checkNull(request.getParamValue("PIN_CREATE_DATE"));
				String pinEndDate = CommonUtils.checkNull(request.getParamValue("PIN_END_DATE"));
				String status = CommonUtils.checkNull(request.getParamValue("status"));
				String dealerId = logonUser.getDealerId();
				Integer curPage = request.getParamValue("curPage") != null ?
						Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.queryVehiclePin(pinCode, vin, dealerId, 
						pinCreateDate, pinEndDate, status, curPage);
				act.setOutData("ps", ps);
			}else{
				act.setForword(VEHICLE_PIN_REQUEST_URL);
			}
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆PIN订单申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商端车辆PIN订单申报
	 * writed by yx 20101215
	 */
	public void addVehiclePinRequest(){
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String pinCode = CommonUtils.checkNull(request.getParamValue("pinCode"));
				String vinNo = CommonUtils.checkNull(request.getParamValue("vinNo"));
				String remark = CommonUtils.checkNull(request.getParamValue("remark"));
				
				String dealerId = logonUser.getDealerId();
				String dealerCode = logonUser.getDealerCode();
				long userId = logonUser.getUserId();
				
				TmVehiclePinRequestPO pinReq = new TmVehiclePinRequestPO();
				pinReq.setPinId(Utility.getLong(SequenceManager.getSequence("")));
				
				pinReq.setVin(vinNo);
				pinReq.setDealerId(Long.valueOf(dealerId));
				pinReq.setRemark(remark);
				pinReq.setBackRemark("");
				pinReq.setCreateBy(userId);
				pinReq.setCreateDate(new Date());
				pinReq.setMakeDate(new Date());
				pinReq.setStatus(Constant.VEHICLE_PIN_01);
				
				
				//生成PIN申请编号
				Calendar   calendar = Calendar.getInstance();
				int month = calendar.get(Calendar.MONTH)+1;
				String year2 = (calendar.get(Calendar.YEAR)+"").substring(2);
				String  date = calendar.get(Calendar.DATE)+"";
				System.out.println(date.length());
				if(date.length()<2){
					date=0+date;
				}
				String pNo = Utility.GetBillNo("",dealerCode,"PNO");
				pNo = pNo.substring(pNo.length()-4);
				pNo = "CX"+dealerCode+year2+month+date+pNo;
				
				pinReq.setPinCode(pNo);
				
				String msg = dao.addVehiclePin(pinReq);
				
				
				act.setOutData("pNo", pNo);
			}else{
				Map<String, Object> ps = dao.addVehiclePinQueryUserInfo(logonUser.getUserId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String nowDate = sdf.format(new Date());
				ps.put("nowDate", nowDate);
				act.setOutData("ps", ps);
				act.setForword(VEHICLE_PIN_ADD_URL);
			}
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆PIN订单申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商端车辆PIN订单查看
	 * writed by yx 20101215
	 */
	public void viewVehiclePinRequest(){
		try {
			String pinId = CommonUtils.checkNull(request.getParamValue("pinId"));
			String userId = CommonUtils.checkNull(request.getParamValue("userId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			Map<String, Object> ps = dao.viewVehiclePinQuery(pinId,userId,dealerId);
			act.setOutData("ps", ps);
			act.setForword(VEHICLE_PIN_DETAIL_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆PIN订单申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商端车辆策略规则变化申请查询
	 * writed by yx 20101223
	 */

	public void vehicleRuleInfoChangeApplyInit() {
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String vehicleNo = CommonUtils.checkNull(request.getParamValue("vehicleNo"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			if(!"".equals(COMMAND)){
				Integer curPage = request.getParamValue("curPage") != null ?
						Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.queryVehicleRuleChangeInfo(vin,vehicleNo,status,
						beginDate,endDate,logonUser.getDealerId(), curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}else{
				act.setForword(VEHICLE_RULE_CHANGE_REQUEST);
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: ruleChangeApplyAdd 
	* @Description: TODO(三包规则变更申请新增页面) 
	 */
	public void ruleChangeApplyAdd() {
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(!"".equals(COMMAND)){
				
				Long userId = logonUser.getUserId();
				String ruleListId[] = request.getParamValues("ruleListId");
				String flag = CommonUtils.checkNull(request.getParamValue("flag"));
				Integer changeStatus = Constant.VEHICLE_CHANGE_STATUS_01;
				if("1".equals(flag)){
					changeStatus = Constant.VEHICLE_CHANGE_STATUS_01;
				}else{
					changeStatus = Constant.VEHICLE_CHANGE_STATUS_02;
				}
				for(int i = 0; i < ruleListId.length; i++){
					String vin = CommonUtils.checkNull(request.getParamValue("vin"));
					String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));
					String vehicleNo = CommonUtils.checkNull(request.getParamValue("vehicleNo"));
					String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));
					String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
					String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
					String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));
					String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));
					String applyRemark = CommonUtils.checkNull(request.getParamValue("applyRemark"));
					String claimMonthNew[] = request.getParamValues("claimMonthNew");
					String claimMelieageNew[] = request.getParamValues("claimMelieageNew");
					String gameId = CommonUtils.checkNull(request.getParamValue("gameId"));
					
					TtVehicleChangePO change = new TtVehicleChangePO();
					
					
					Long changeId = Utility.getLong(SequenceManager.getSequence(""));
					change.setId(changeId);
					change.setVin(vin);
					change.setEngineNo(engineNo);
					change.setVehicleNo(vehicleNo);
					change.setYieldly(yieldly);
					change.setSeriesId(Long.valueOf("".equals(seriesId)?"0":seriesId));
					change.setModelId(Long.valueOf("".equals(modelId)?"0":modelId));
					if(!"".equals(purchasedDate)){
						change.setPurchasedDate(sdf.parse(purchasedDate));
					}
					change.setMileage(Double.valueOf(mileage));
					change.setApplyPerson(new Long(logonUser.getDealerId()));
					change.setApplyDate(new Date());
					change.setApplyType(Constant.VEHICLE_CHANGE_TYPE_04);
					change.setApplyRemark(applyRemark);
					change.setClaimTacticsId(Long.valueOf(gameId));
					change.setStatus(changeStatus);
					change.setVer(0);
					change.setIsDel(Constant.STATUS_ENABLE);
					change.setClaimMonthNew(Long.valueOf(claimMonthNew[i]));
					change.setClaimMelieageNew(Double.valueOf(claimMelieageNew[i]));
					change.setCreateBy(Long.valueOf(logonUser.getDealerId()));
					change.setCreateDate(new Date());
					change.setUpdateBy(logonUser.getUserId());
					change.setUpdateDate(new Date());
					//外键关联到rule_list表
					change.setRuleListId(Long.valueOf(ruleListId[i]));
					dao.insert(change);
				}
				act.setOutData("msg", "succ");
				//act.setForword(VEHICLE_RULE_CHANGE_REQUEST);
			}else{
				TmCompanyPO com = new TmCompanyPO();
				com.setCompanyId(logonUser.getCompanyId());
				String comName = ((TmCompanyPO)dao.select(com).get(0)).getCompanyShortname();
				TmDealerPO dealer = new TmDealerPO();
				dealer.setDealerId(new Long(logonUser.getDealerId()));
				
				act.setOutData("comName",  comName);
				act.setOutData("createBy", ((TmDealerPO)dao.select(dealer).get(0)).getDealerShortname());
				act.setOutData("createDate", sdf.format(new Date()));
				act.setOutData("msg", "");
				act.setForword(VEHICLE_RULE_CHANGE_ADD);
			}
			
		} catch(Exception e) {
			String success = "";
			act.setOutData(success, "售前车不能做三包期信息变更");
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"三包规则变更申请新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: ruleChangeApplyAdd 
	* @Description: TODO(三包规则变更申请修改页面) 
	 */
	public void ruleChangeApplyModify() {
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Long userId = logonUser.getUserId();
				String ruleListId[] = request.getParamValues("ruleListId");
				String flag = CommonUtils.checkNull(request.getParamValue("flag"));
				Integer changeStatus = Constant.VEHICLE_CHANGE_STATUS_01;
				if("1".equals(flag)){
					changeStatus = Constant.VEHICLE_CHANGE_STATUS_01;
				}else{
					changeStatus = Constant.VEHICLE_CHANGE_STATUS_02;
				}
				for(int i = 0; i < ruleListId.length; i++){
					String vin = CommonUtils.checkNull(request.getParamValue("vin"));
					String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));
					String vehicleNo = CommonUtils.checkNull(request.getParamValue("vehicleNo"));
					String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));
					String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
					String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
					String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));
					String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));
					String applyRemark = CommonUtils.checkNull(request.getParamValue("applyRemark"));
					String claimMonthNew[] = request.getParamValues("claimMonthNew");
					String claimMelieageNew[] = request.getParamValues("claimMelieageNew");
					String gameId = CommonUtils.checkNull(request.getParamValue("gameId"));
					String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));
					
					TtVehicleChangePO change = new TtVehicleChangePO();
					change.setId(Long.valueOf(changeId));
					TtVehicleChangePO changeValue = new TtVehicleChangePO();
					changeValue.setVin(vin);
					changeValue.setEngineNo(engineNo);
					changeValue.setVehicleNo(vehicleNo);
					changeValue.setYieldly(yieldly);
					changeValue.setSeriesId(Long.valueOf("".equals(seriesId)?"0":seriesId));
					changeValue.setModelId(Long.valueOf("".equals(modelId)?"0":modelId));
					if(!"".equals(purchasedDate)){
						changeValue.setPurchasedDate(sdf.parse(purchasedDate));
					}
					changeValue.setMileage(Double.valueOf(mileage));
					changeValue.setApplyType(Constant.VEHICLE_CHANGE_TYPE_04);
					changeValue.setApplyRemark(applyRemark);
					changeValue.setClaimTacticsId(Long.valueOf(gameId));
					changeValue.setStatus(changeStatus);
					changeValue.setIsDel(Constant.STATUS_ENABLE);
					changeValue.setClaimMonthNew(Long.valueOf(claimMonthNew[i]));
					changeValue.setClaimMelieageNew(Double.valueOf(claimMelieageNew[i]));
					changeValue.setUpdateBy(logonUser.getUserId());
					changeValue.setUpdateDate(new Date());
					//外键关联到rule_list表
					changeValue.setRuleListId(Long.valueOf(ruleListId[i]));
					dao.update(change, changeValue);
				}
			//	act.setForword(VEHICLE_RULE_CHANGE_REQUEST);
				act.setOutData("msg", "succ");
			}else{
				String changeId = CommonUtils.checkNull(request.getParamValue("id"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
//				Map<String, Object> vehicle = dao.queryVehicleRuleChangeAndRuleListDetailByChangeId(logonUser.getUserId(), changeId);
				Map<String,Object> part = dao.queryPartByVin(vin,changeId);
				act.setOutData("part", part);
				act.setOutData("vin", vin);
				act.setOutData("msg", "");
				act.setForword(VEHICLE_RULE_CHANGE_MODIFY);
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"三包规则变更申请新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: ruleChangeApplyAdd 
	* @Description: TODO(三包规则变更申请明细页面) 
	 */
	public void ruleChangeApplyView() {
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Long userId = logonUser.getUserId();
				String ruleListId[] = request.getParamValues("ruleListId");
				String flag = CommonUtils.checkNull(request.getParamValue("flag"));
				Integer changeStatus = Constant.VEHICLE_CHANGE_STATUS_01;
				if("1".equals(flag)){
					changeStatus = Constant.VEHICLE_CHANGE_STATUS_01;
				}else{
					changeStatus = Constant.VEHICLE_CHANGE_STATUS_02;
				}
				for(int i = 0; i < ruleListId.length; i++){
					String vin = CommonUtils.checkNull(request.getParamValue("vin"));
					String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));
					String vehicleNo = CommonUtils.checkNull(request.getParamValue("vehicleNo"));
					String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));
					String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
					String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
					String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));
					String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));
					String applyRemark = CommonUtils.checkNull(request.getParamValue("applyRemark"));
					String claimMonthNew[] = request.getParamValues("claimMonthNew");
					String claimMelieageNew[] = request.getParamValues("claimMelieageNew");
					String gameId = CommonUtils.checkNull(request.getParamValue("gameId"));
					String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));
					
					TtVehicleChangePO change = new TtVehicleChangePO();
					change.setId(Long.valueOf(changeId));
					TtVehicleChangePO changeValue = new TtVehicleChangePO();
					changeValue.setVin(vin);
					changeValue.setEngineNo(engineNo);
					changeValue.setVehicleNo(vehicleNo);
					changeValue.setYieldly(yieldly);
					changeValue.setSeriesId(Long.valueOf("".equals(seriesId)?"0":seriesId));
					changeValue.setModelId(Long.valueOf("".equals(modelId)?"0":modelId));
					if(!"".equals(purchasedDate)){
						changeValue.setPurchasedDate(sdf.parse(purchasedDate));
					}
					changeValue.setMileage(Double.valueOf(mileage));
					changeValue.setApplyType(Constant.VEHICLE_CHANGE_TYPE_04);
					changeValue.setApplyRemark(applyRemark);
					changeValue.setClaimTacticsId(Long.valueOf(gameId));
					changeValue.setStatus(changeStatus);
					changeValue.setIsDel(Constant.STATUS_ENABLE);
					changeValue.setClaimMonthNew(Long.valueOf(claimMonthNew[i]));
					changeValue.setClaimMelieageNew(Double.valueOf(claimMelieageNew[i]));
					changeValue.setUpdateBy(logonUser.getUserId());
					changeValue.setUpdateDate(new Date());
					//外键关联到rule_list表
					changeValue.setRuleListId(Long.valueOf(ruleListId[i]));
					dao.update(change, changeValue);
				}
				act.setForword(VEHICLE_RULE_CHANGE_REQUEST);
			}else{
				String changeId = CommonUtils.checkNull(request.getParamValue("id"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
//				Map<String, Object> vehicle = dao.queryVehicleRuleChangeAndRuleListDetailByChangeId(logonUser.getUserId(), changeId);
				Map<String,Object> part = dao.queryPartByVin(vin,changeId);
				act.setOutData("part", part);
				act.setOutData("vin", vin);
				act.setForword(VEHICLE_RULE_CHANGE_VIEW);
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"三包规则变更申请新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void signIt() {
		try {
			String id = request.getParamValue("id");
			TtAsBarcodeApplyPO po1 = new TtAsBarcodeApplyPO();
			TtAsBarcodeApplyPO po2 = new TtAsBarcodeApplyPO();
			po1.setId(Long.parseLong(id));
			po2.setSignName(logonUser.getName());
			po2.setSignDate(new Date());
			po2.setSignStatus(1);
			dao.update(po1,po2);
			act.setOutData("result", "success");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆条码补办申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//VIN条码申请准备
	public void barCodeApply() {
		try {
			act.setForword(BAR_CODE_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void barCodeApplyQuery() {
		try {
			String vin = request.getParamValue("vin");
			String status = request.getParamValue("status");
			Integer curPage = request.getParamValue("curPage") != null ?
					Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.queryBarCode(vin,status,logonUser.getDealerId(), curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void barCodeApplyAdd() {
		try {
			String type = request.getParamValue("type");
			Long dealerId =0l;
			TtAsBarcodeApplyPO ap = new TtAsBarcodeApplyPO();
			//得到当前日期的年月
			Date aDate = new Date(); 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM"); 
			String time = formatter.format(aDate); 
			String rand = "";
			String randomId = "";
			CommonUtilDao comDao = new CommonUtilDao();
			if("0".equalsIgnoreCase(type)){
				dealerId = Long.valueOf(logonUser.getDealerId());
				//根据ID查询经销商详细信息
				TmDealerPO tm =  comDao.queryById(dealerId);
				act.setOutData("tm", tm);
				//给此表单指定报告编号
				Long id = Utility.getLong(SequenceManager.getSequence(""));
				QualityReportInfoMaintasinPO maintasinPO = comDao.queryByCode();
				int i = 1;
				if(maintasinPO == null)
				{
					maintasinPO = new QualityReportInfoMaintasinPO();
					maintasinPO.setId(id);
					maintasinPO.setServiceCode("1");
					maintasinPO.setRandomId(1);
					//得到当前的randomId	
					rand = maintasinPO.getRandomId().toString();
					char[] random = rand.toCharArray();
					char[] art = {'0','0','0','0'};
					System.arraycopy(random, 0, art, art.length-random.length, random.length);
					rand = new String(art);
					//保存报告编号
					randomId = time+rand;
				}
				else
				{
					i = maintasinPO.getRandomId();
					QualityReportInfoMaintasinPO maintasinPO1 = new QualityReportInfoMaintasinPO();
					maintasinPO1.setId(maintasinPO.getId());
					QualityReportInfoMaintasinPO q = new QualityReportInfoMaintasinPO();
					q.setRandomId(maintasinPO.getRandomId()+1);
					//dao.update(maintasinPO1, q);
					//保存报告编号
					rand = q.getRandomId().toString();
					char[] random = rand.toCharArray();
					char[] art = {'0','0','0','0'};
					System.arraycopy(random, 0, art, art.length-random.length, random.length);
					rand = new String(art);
					randomId = time+rand;
					act.setOutData("rand", rand);
				}
			}else if("1".equalsIgnoreCase(type)||"2".equalsIgnoreCase(type)||"4".equalsIgnoreCase(type)||"5".equalsIgnoreCase(type)){
				String id = request.getParamValue("id");
				ap.setId(Long.valueOf(id));
				ap = (TtAsBarcodeApplyPO) dao.select(ap).get(0);
				dealerId = ap.getApplyBy();
				TmDealerPO tm =  comDao.queryById(dealerId);
				FsFileuploadPO detail = new FsFileuploadPO();
				detail.setYwzj(Long.valueOf(id));
				List<FsFileuploadPO> lists = dao.select(detail);
				act.setOutData("lists", lists);
				act.setOutData("tm", tm);
			}
			TmDealerPO p = new TmDealerPO();
			p.setDealerId(dealerId);
			List<TmDealerPO> dealerList = dao.select(p);
			act.setOutData("isScan",dealerList.get(0).getIsScan() );
			act.setOutData("randomId", randomId);
			act.setOutData("type", type);
			act.setOutData("bean", ap);
			act.setForword(BAR_CODE_APPLY_ADD);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void queryVehicleInfo() {
		try {
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			Map<String, Object> map = dao.queryVehicleInfoByVin(vin);//车辆信息
			act.setOutData("vehicle", map);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void barCodeApplySave() {
		try {
			String randId = request.getParamValue("rand");
			QualityReportInfoMaintasinPO maintasinPO = new QualityReportInfoMaintasinPO();
			if(randId == null)
			{
				Long ids = Utility.getLong(SequenceManager.getSequence(""));
				maintasinPO.setId(ids);
				maintasinPO.setServiceCode("1");
				maintasinPO.setRandomId(1);
				dao.insert(maintasinPO);
			}
			else
			{
				QualityReportInfoMaintasinPO maintasinPO1 = new QualityReportInfoMaintasinPO();
				maintasinPO1.setId(maintasinPO.getId());
				QualityReportInfoMaintasinPO q = new QualityReportInfoMaintasinPO();
				q.setRandomId(Integer.parseInt(randId));
				dao.update(maintasinPO1, q);
			}
			String type = request.getParamValue("type");
			String vin = request.getParamValue("vin");
			String remark = request.getParamValue("applyRemark");
			String randomId = request.getParamValue("randomId");
			String applicantPeople = request.getParamValue("applicantPeople");
			TtAsBarcodeApplyPO ap = new TtAsBarcodeApplyPO();
			if("0".equalsIgnoreCase(type)){
				ap.setApplyBy(Long.valueOf(logonUser.getDealerId()));
				
				TmDealerPO tmDealerPO = new TmDealerPO();
				tmDealerPO.setDealerId(Long.valueOf(logonUser.getDealerId()));
				List<TmDealerPO> selectLists = dao.select(tmDealerPO);
				if(selectLists != null && selectLists.size() == 1) {
					ap.setDealerName(selectLists.get(0).getDealerName());
					ap.setDealerShortname(selectLists.get(0).getDealerShortname());
				}
				
				ap.setApplicantPeople(applicantPeople);
				ap.setReportNumber(Long.parseLong(randomId));
				ap.setApplyDate(new Date());
				ap.setApplyRemark(remark);
				ap.setApplyStatus(Constant.BARCODE_APPLY_STATUS_01);
				ap.setId(Utility.getLong(SequenceManager.getSequence("")));
				ap.setVin(vin);
				dao.insert(ap);
				String ywzj = ap.getId().toString();
				String[] fjids = request.getParamValues("fjid");
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			}else if("1".equalsIgnoreCase(type)){
				String id = request.getParamValue("ids");
				TtAsBarcodeApplyPO ap2 = new TtAsBarcodeApplyPO();
				ap.setId(Long.valueOf(id));
				ap2.setApplyRemark(remark);
				ap2.setApplyStatus(Constant.BARCODE_APPLY_STATUS_01);
				ap2.setAuditRemark(" ");
				ap2.setAuditBy(0L);
				dao.update(ap, ap2);
				String sql="update  tt_as_barcode_apply a set a.audit_date=null,a.audit_acount=null where a.id="+id;
				dao.update(sql, null);
				String ywzj = ap.getId().toString();
				String[] fjids = request.getParamValues("fjid");
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			}else if("3".equalsIgnoreCase(type)){
				String id = request.getParamValue("id");
				ap.setId(Long.valueOf(id));
				TtAsBarcodeApplyPO ap2 = new TtAsBarcodeApplyPO();
				ap2.setApplyStatus(Constant.BARCODE_APPLY_STATUS_02);
				ap2.setReportDate(new Date());
				dao.update(ap, ap2);
			}else if("4".equalsIgnoreCase(type) || "5".equalsIgnoreCase(type)){
				String id = request.getParamValue("id");
				String str2 = request.getParamValue("str");
				String auditRemark = request.getParamValue("auditRemark");
				String checkPeople = request.getParamValue("checkPeople");
				String reviewRemark = request.getParamValue("reviewRemark");
				String distributionPeople = request.getParamValue("distributionPeople");
				String expressMail = request.getParamValue("expressMail");
				String chapterCode = request.getParamValue("chapterCode");
				ap.setId(Long.valueOf(id));
				TtAsBarcodeApplyPO ap2 = new TtAsBarcodeApplyPO();
				if("agree".equalsIgnoreCase(str2)){
				ap2.setApplyStatus(Constant.BARCODE_APPLY_STATUS_04);
				}else if("disagree".equalsIgnoreCase(str2)){
				ap2.setApplyStatus(Constant.BARCODE_APPLY_STATUS_03);
				}
				ap2.setAuditRemark(auditRemark);
				ap2.setCheckPeople(checkPeople);
				ap2.setAuditBy(logonUser.getUserId());
				ap2.setAuditDate(new Date());
				ap2.setReviewRemark(reviewRemark);
				ap2.setDistributionPeople(distributionPeople);
				ap2.setDistributionTime(new Date());
				ap2.setExpressMail(expressMail==null?"":expressMail);
				ap2.setChapterCode(chapterCode==null?0L:Long.parseLong(chapterCode));
				dao.update(ap, ap2);
			}
			act.setOutData("randomId", randId);
			act.setOutData("type", type);
			act.setOutData("result", "success");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//条码补办审核
	public void barCodeAudit(){
		try {
			act.setForword(BAR_CODE_URL2);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//条码补办发运
	public void barCodeSend(){
		try {
			act.setForword(BAR_CODE_URL3);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//审核查询
	@SuppressWarnings({ "unchecked", "static-access" })
	public void barCodeApplyQuery2() {
		try {
			String vin = request.getParamValue("vin");
			String status = request.getParamValue("status");
			String dealerId = request.getParamValue("dealerId");
			String dealerName = request.getParamValue("DEALER_NAME");
			String sDate = request.getParamValue("S_DATE");
			String eDate = request.getParamValue("E_DATE");
			String signSDate = request.getParamValue("SIGN_S_DATE");
			String signEDate = request.getParamValue("SIGN_E_DATE");
			String signName = request.getParamValue("SIGN_NAME");
			String signStatus = request.getParamValue("SIGN_STATUS");
			String type = request.getParamValue("type");
			String groupName = request.getParamValue("GROUP_NAME");
			String chapterCode = request.getParamValue("CHAPTER_CODE");
			String name = "三包凭证补办.xls";
			if(!Utility.testString(type)){
				Integer curPage = request.getParamValue("curPage") != null ?
						Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.queryBarCode2(sDate,eDate,vin,status,dealerId,dealerName,logonUser, curPage, Constant.PAGE_SIZE,signSDate,signEDate,signName,signStatus,groupName,chapterCode);
				act.setOutData("ps", ps);
			}else{
				String[] head=new String[11];
				head[0]="VIN";
				head[1]="经销商代码";
				head[2]="经销商名称";
				head[3]="申请时间";
				head[4]="审核状态";
				head[5]="审核人";
				head[6]="审核时间";
				head[7]="签收人";
				head[8]="签收时间";
				head[9]="是否签收";
				PageResult<Map<String, Object>> ps = dao.queryBarCode2(sDate,eDate,vin,status,dealerId,dealerName,logonUser, 1, 99999,signSDate,signEDate,signName,signStatus,groupName,chapterCode);
				List<Map<String, Object>> list2 = ps.getRecords();
				String setList = "0,1,2,3,4,5,6,7,8,9";
				 List list1=new ArrayList();
				    if(list2!=null&&list2.size()!=0){
						for(int i=0;i<list2.size();i++){
					    	Map map =(Map)list2.get(i);
							String[]detail=new String[11];
							
							detail[0]=(String) map.get("VIN");
							detail[1]=(String) map.get("DEALER_CODE");
							detail[2]=(String)(map.get("DEALER_NAME"));
							detail[3]=(String) map.get("APP_DATE");
							detail[4]=(String) map.get("CODE_DESC");
							detail[5]=(String)(map.get("AUDIT_NAME"));
							detail[6]=(String) map.get("AUDIT_TIME");
							detail[7]=(String) map.get("SIGN_NAME");
							if (map.get("SIGN_DATE") != null) {
								detail[8]= map.get("SIGN_DATE") + "";
							} else {
								detail[8]="";
							}
							if (new BigDecimal(1).equals(map.get("SIGN_STATUS"))) {
								detail[9] = "已签收";
							} else {
								detail[9] = "未签收";
							}
							list1.add(detail);
					      }
						//dao.toExceVender(ActionContext.getContext().getResponse(), req, head, list1,name);
					    }
				    dao1.toExceVender(ActionContext.getContext().getResponse(), request, head, list1,name,"三包凭证补办",setList);
				    act.setForword(BAR_CODE_URL2);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void barCodeApplyPrint(){
		try {
			String id = request.getParamValue("id");
			String sql = " update tt_as_barcode_apply a set a.print_by ="+logonUser.getUserId()+" ,a.print_date=sysdate,a.print_times=a.print_times+1 where a.id= "+id;
			dao.update(sql, null);
			List<TtAsBarcodeApplyBean> list=dao.getList(id);
			act.setOutData("bean", list.get(0));
			act.setOutData("VIN", list.get(0).getVin());
			act.setForword(BAR_CODE_PRINT);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings("unchecked")
	public void barCodeApplyPrint2(){
		try {
			String ids = request.getParamValue("ids");
			String id[] = ids.split(",");
			for(int i=0;i<id.length;i++){
				String sql = " update tt_as_barcode_apply a set a.print_by ="+logonUser.getUserId()+" ,a.print_date=sysdate,a.print_times=a.print_times+1 where a.id= "+id[i];
				dao.update(sql, null);
			}
			List <TtAsBarcodeApplyBean> list = dao.getList(ids);
			act.setOutData("list", list);
			act.setForword(BAR_CODE_PRINT2);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 三包凭证补办流程
	 * @author KFQ 2013-7-20 20:25
	 * @return 
	 */
	public  void packgeChangeApply() {
		try {
			act.setForword(PACKGE_APPLY_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void packgeApplyAdd() {
		try {
			String type = request.getParamValue("type");
			TtAsPackgeChangeApplyPO ap = new TtAsPackgeChangeApplyPO();
			if("0".equalsIgnoreCase(type)){
				
			}else if("1".equalsIgnoreCase(type)){
				String id = request.getParamValue("id");
				ap.setId(Long.valueOf(id));
				ap = (TtAsPackgeChangeApplyPO) dao.select(ap).get(0);
				List<FsFileuploadPO> fileList = dao.queryAttById(id);// 取得附件
				act.setOutData("fileList", fileList);
			}
			TmDealerPO p = new TmDealerPO();
			p.setDealerId(Long.valueOf(logonUser.getDealerId()));
			List<TmDealerPO> dealerList = dao.select(p);
			act.setOutData("isScan",dealerList.get(0).getIsScan() );
				act.setOutData("type", type);
				act.setOutData("bean", ap);
				act.setForword(PACKGE_APPLY_ADD);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryVehicleInfoPackge() {
		try {
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			Map<String, Object> map = dao.queryVehicleInfoByVin2(vin);//车辆信息
			Map<String,Object> list = dao.checkVin(vin);
			String sale = "";
			if(list!=null && list.size()>0){
				sale = "sale";
			}else{
				sale = "unSale";
			}
			act.setOutData("sale", sale);
			act.setOutData("vehicle", map);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void packgeApplySave() {
		try {
			String type = request.getParamValue("type");
			String vin = request.getParamValue("vin");
			String remark = request.getParamValue("remark");
			TtAsPackgeChangeApplyPO cap = new TtAsPackgeChangeApplyPO();
			if("0".equalsIgnoreCase(type)){
				cap.setApplyBy(Long.valueOf(logonUser.getDealerId()));
				cap.setApplyDate(new Date());
				cap.setApplyRemark(remark);
				cap.setApplyStatus(Constant.PACKGE_CHANGE_STATUS_01);
				cap.setId(Utility.getLong(SequenceManager.getSequence("")));
				cap.setVin(vin);
				dao.insert(cap);
				String ywzj = cap.getId().toString();
				String[] fjids = request.getParamValues("fjids");
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			}else if("1".equalsIgnoreCase(type)){
				String id = request.getParamValue("ids");
				TtAsPackgeChangeApplyPO cap2 = new TtAsPackgeChangeApplyPO();
				cap.setId(Long.valueOf(id));
				cap2.setApplyRemark(remark);
				cap2.setApplyStatus(Constant.PACKGE_CHANGE_STATUS_01);
				cap2.setAuditAcount(0.0);
				dao.update(cap, cap2);
				String ywzj = cap.getId().toString();
				String[] fjids = request.getParamValues("fjid");
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			}else if("3".equalsIgnoreCase(type)){
				String id = request.getParamValue("id");
				cap.setId(Long.valueOf(id));
				TtAsPackgeChangeApplyPO cap2 = new TtAsPackgeChangeApplyPO();
				cap2.setApplyStatus(Constant.PACKGE_CHANGE_STATUS_02);
				cap2.setReportDate(new Date());
				dao.update(cap, cap2);
			}
			act.setOutData("type", type);
			act.setOutData("result", "success");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void packgeApplyQuery() {
		try {
			String vin = request.getParamValue("vin");
			String status = request.getParamValue("status");
			Integer curPage = request.getParamValue("curPage") != null ?
					Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.queryPackgeApply(vin,status,logonUser.getDealerId(), curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void packgeApplyDetail(){
		try {
			String id = request.getParamValue("id");
			TtAsPackgeChangeApplyPO ap = new TtAsPackgeChangeApplyPO();
			ap.setId(Long.valueOf(id));
			ap = (TtAsPackgeChangeApplyPO) dao.select(ap).get(0);
			List<FsFileuploadPO> fileList = dao.queryAttById(id);// 取得附件
			
			List<TtAsPackgeChangeDetailBean> list = dao.getAuditDetail(id);
			act.setOutData("fileList", fileList);
			act.setOutData("bean", ap);
			act.setOutData("list", list);
			act.setForword(PACKGE_APPLY_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void packgeChangeAudit(){
		try {
			act.setOutData("type", "dq");
			act.setForword(PACKGE_APPLY_URL2);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void packgeApplyQuery2() {
		try {
			String vin = request.getParamValue("vin");
			String status = request.getParamValue("status");
			String dealerName=request.getParamValue("DEALER_NAME");
			String dealerId = request.getParamValue("dealerId");
			Integer curPage = request.getParamValue("curPage") != null ?
					Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.queryPackgeApply2(vin,status,logonUser.getOrgId(),dealerId,dealerName, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//大区审核三包凭证审核
	public void packgeAppAudit(){
		try {
			String type = request.getParamValue("type");
			TtAsPackgeChangeApplyPO ap = new TtAsPackgeChangeApplyPO();
				String id = request.getParamValue("id");
				ap.setId(Long.valueOf(id));
				ap = (TtAsPackgeChangeApplyPO) dao.select(ap).get(0);
				List<FsFileuploadPO> fileList = dao.queryAttById(id);// 取得附件
				
				List<TtAsPackgeChangeDetailBean> list = dao.getAuditDetail(id);
				act.setOutData("fileList", fileList);
				act.setOutData("bean", ap);
				act.setOutData("list", list);
				act.setOutData("type", type);
				act.setForword(PACKGE_APPLY_AUDIT);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//大区审核三包凭证审核
	public void packgeAppAuditSave(){
		try {
			TtAsPackgeChangeApplyPO ap = new TtAsPackgeChangeApplyPO();
			TtAsPackgeChangeApplyPO ap2 = new TtAsPackgeChangeApplyPO();
			String type=request.getParamValue("type");
				String id = request.getParamValue("id");
				String str = request.getParamValue("str");
				String remark = request.getParamValue("audit_remark");
				String auditCount = request.getParamValue("auditAcount");
				ap.setId(Long.valueOf(id));
				ap2.setUpdateBy(logonUser.getUserId());
				ap2.setUpdateDate(new Date());
				if("agree".equalsIgnoreCase(str)&&"dq".equalsIgnoreCase(type)){
					ap2.setApplyStatus(PACKGE_CHANGE_STATUS_04);
					ap2.setAuditAcount(Double.valueOf(auditCount));
				}else if("disagree".equalsIgnoreCase(str)&&"dq".equalsIgnoreCase(type)){
					ap2.setApplyStatus(PACKGE_CHANGE_STATUS_03);
				}else if("agree".equalsIgnoreCase(str)&&"cc".equalsIgnoreCase(type)){
					ap2.setApplyStatus(PACKGE_CHANGE_STATUS_06);
					ap2.setAuditAcount(Double.valueOf(auditCount));
				}else if("disagree".equalsIgnoreCase(str)&&"cc".equalsIgnoreCase(type)){
					ap2.setApplyStatus(PACKGE_CHANGE_STATUS_05);
				}
				dao.update(ap, ap2);
				TtAsPackgeChangeDetailPO dp = new TtAsPackgeChangeDetailPO();
				dp.setApplyId(ap.getId());
				dp.setAuditAcount(Double.valueOf(auditCount));
				dp.setAuditDate(new Date());
				dp.setAuditPerson(logonUser.getUserId());
				dp.setAuditRemark(remark);
				dp.setAuditStatus(ap2.getApplyStatus());
				dp.setId(Utility.getLong(SequenceManager.getSequence("")));
				dao.insert(dp);
				act.setOutData("type", type);
				act.setOutData("result", "success");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void packgeChangeAudit2(){
		try {
			act.setOutData("type", "cc");
			act.setForword(PACKGE_APPLY_URL3);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void packgeApplyQuery3() {
		try {
			String vin = request.getParamValue("vin");
			String status = request.getParamValue("status");
			String dealerName=request.getParamValue("DEALER_NAME");
			String dealerId = request.getParamValue("dealerId");
			Integer curPage = request.getParamValue("curPage") != null ?
					Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.queryPackgeApply3(vin,status,dealerId,dealerName, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 三包凭证补办打印
	 */
	public void packgeAppviewPrint(){
		try {
				String vin = request.getParamValue("vin");
				String id = request.getParamValue("id");
				String name =new String(request.getParamValue("name").getBytes("ISO-8859-1"),"UTF-8");
				String fax = request.getParamValue("fax");
				String address =new String(request.getParamValue("address").getBytes("ISO-8859-1"),"UTF-8");
				TtAsPackgeChangeApplyPO p = new TtAsPackgeChangeApplyPO();
				p.setId(Long.valueOf(id));
				p = (TtAsPackgeChangeApplyPO) dao.select(p).get(0);
				List<TtAsPackgeChangeDetailBean> list = dao.getPrintDetail(p.getVin());
				String sql = "update tt_as_packge_change_apply a set a.print_by="+logonUser.getUserId()+",a.print_date = sysdate,a.print_times = nvl(a.print_times,0)+1 where a.id="+id;
				dao.update(sql, null);
				if(list!=null&&list.size()>0){
					act.setOutData("bean", list.get(0));
				}else{
					act.setOutData("bean", null);
				}
				act.setOutData("name", name);
				act.setOutData("fax",fax);
				act.setOutData("address", address);
				act.setForword(PACKGE_APPLY_PRINT);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//经销商pin码查询跳转
	public void pinSearch(){
		try {
			TmDealerPO p = new TmDealerPO();
			p.setDealerId(Long.valueOf(logonUser.getDealerId()));
			List<TmDealerPO> dealerList = dao.select(p);
			act.setOutData("isScan",dealerList.get(0).getIsScan() );
			act.setForword(PIN_SEARCH_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"经销商pin 查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//PIN 查询
	@SuppressWarnings("unchecked")
	public void pinSearch2(){
		try {
			String vin = request.getParamValue("vinNo");
			TtAsPinSearchDetailPO dp = new TtAsPinSearchDetailPO();
			dp.setCreateDate(new Date());
			dp.setDealerId(Long.valueOf(logonUser.getDealerId()));
			dp.setId(Utility.getLong(SequenceManager.getSequence("")));
			dp.setVin(vin);
			
			String pNo="";
			TmVehiclePO p = new TmVehiclePO();
			p.setVin(vin);
			
			/*****add by liuxh 20131108判断车架号不能为空*****/
			CommonUtils.jugeVinNull(vin);
			/*****add by liuxh 20131108判断车架号不能为空*****/
			
			List <TmVehiclePO> list = dao.select(p);
			if(list!=null&&list.size()>0){
				pNo = list.get(0).getPin()	;
				if(pNo!=null&& !"".equalsIgnoreCase(pNo)){
					dp.setPinNo(list.get(0).getPin());
				}else {
					dp.setPinNo("PIN码不存在");
				}
				
			}else if(list.size()==0){
				dp.setPinNo("VIN不存在");
			} 
			dao.insert(dp);
			act.setOutData("pNo", pNo);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"经销商pin 查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 车厂查询经销商的 PIN查询记录
	 */
		public void dealerPinSearch(){
			try {
				act.setForword(PIN_SEARCH_DETAIL_URL);
			} catch(Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"经销商pin 查询");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		public void dealerPinSearch2(){
			try {
				String dealerName = request.getParamValue("dealerName");
				String dealerId = request.getParamValue("dealerId");
				String startDate = request.getParamValue("PIN_CREATE_DATE");
				String endDate = request.getParamValue("PIN_END_DATE");
				Integer curPage = request.getParamValue("curPage") != null ?
						Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.getDetail(dealerName,dealerId,startDate, endDate,curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			} catch(Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"经销商pin 查询");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		
		
		public void carAlarm(){
			try {
				
			
				act.setForword(CAR_ALARM);
			} catch(Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"预警车辆查询");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		
		
	
}
