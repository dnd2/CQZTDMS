package com.infodms.dms.actions.vehicleInfoManage.check;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.vehicleInfoManage.apply.VehicleInfoChangeApply;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtVehicleChangeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.dao.vehicleInfoManage.VehicleInfoManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrAuthmonitorpartPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrRulePO;
import com.infodms.dms.po.TtVehicleChangePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
* @ClassName: VehicleInfoChangeCheck 
* @Description: TODO(车辆变更审核) 
* @author liuqiang 
* @date Oct 28, 2010 6:11:52 PM 
*
 */
public class VehicleInfoChangeCheck implements Constant {
	public Logger logger = Logger.getLogger(VehicleInfoChangeApply.class);
	private VehicleInfoManageDao dao = VehicleInfoManageDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	//车辆信息变更审核查询页面
	private final String INIT_URL = "/jsp/vehicleInfoManage/check/vehicleInfoChangeCheckQuery.jsp";
	//车辆信息变更申请新增页面
	private final String ADD_URL = "/jsp/vehicleInfoManage/apply/vehicleInfoChangeApplyAdd.jsp";
	//车辆信息变更审核页面
	private final String CHECK_URL = "/jsp/vehicleInfoManage/check/vehicleInfoChangeCheck.jsp";
	//车辆信息变更审核详细页面
	private final String VIEW_URL = "/jsp/vehicleInfoManage/check/vehicleInfoChangeCheckView.jsp";
	//车辆信息三包规则变更审核查询页面
	private final String VEHICLE_RULE_CHANGE_QUERY = "/jsp/vehicleInfoManage/check/vehicleInfoRuleChangeCheckQuery.jsp";
	//车辆信息变更审核页面
	private final String VEHICLE_RULE_CHANGE_CHECK = "/jsp/vehicleInfoManage/check/vehicleInfoRuleChangeCheck.jsp";
	//车辆信息变更明细查看页面
	private final String VEHICLE_RULE_CHANGE_VIEW = "/jsp/vehicleInfoManage/check/vehicleInfoRuleChangeView.jsp";
	/**
	 * 
	* @Title: vehicleInfoChangeApplyInit 
	* @Description: TODO(经销商车辆变更审核初始页面) 
	 */
	public void vehicleInfoChangeCheckInit() {
		try {
			act.setForword(INIT_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更审核初始化");
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
			List<TtAsWrGamePO> wrGames = dao.queryWrGame(Long.parseLong(logonUser.getOemCompanyId()));//所有三包策略
			act.setOutData("wrGames", wrGames);
			act.setForword(ADD_URL);
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
			if(Constant.VEHICLE_CHANGE_TYPE_03.toString().equals(po.getApplyType().toString()) ||
				Constant.VEHICLE_CHANGE_TYPE_05.toString().equals(po.getApplyType().toString())	){
				String[] fjids = request.getParamValues("fjid");
				FileUploadManager.fileUploadByBusiness(po.getId().toString(), fjids, logonUser);
			}
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
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
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: doCheck 
	* @Description: TODO(车辆变更信息审核操作) 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void doCheck() {
		try {
			String id = request.getParamValue("id");
			TtVehicleChangePO spo = new TtVehicleChangePO();
			spo.setId(Long.parseLong(id));
			TtVehicleChangePO dpo = assembleVehicleCheckPO();
			dao.update(spo, dpo);
			act.setForword(INIT_URL);
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
	private TtVehicleChangePO assembleVehicleUpdatePO() {
		String modFlag = request.getParamValue("modFlag");//判断车辆信息修改页面保存还是提报 0:保存 1:提报
		TtVehicleChangePO dpo = new TtVehicleChangePO();
		String changeType = CommonUtils.checkNull(request.getParamValue("changeType"));//申请类型
		String applyData = getChangeDataByType(Integer.parseInt(changeType));//申请数据
		String errorDealerId = CommonUtils.checkNull(request.getParamValue("errorDealerId"));//数据提报错误经销商ID
		String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注
		dpo.setApplyType(Integer.parseInt(changeType));
		dpo.setApplyData(applyData);
		dpo.setErrorDealerId(Utility.testString(errorDealerId) ? Long.parseLong(errorDealerId) : null);
		dpo.setErrorType(Integer.parseInt(request.getParamValue("errorType")));
		dpo.setApplyRemark(remark);
		dpo.setUpdateBy(logonUser.getUserId());
		Date sysDate = new Date();
		dpo.setUpdateDate(sysDate);
		if ("1".equals(modFlag)) {
			//将状态更新成审核中
			dpo.setStatus(Constant.VEHICLE_CHANGE_STATUS_02);
			dpo.setApplyDate(sysDate);
			dpo.setApplyPerson(logonUser.getUserId());
		}
		return dpo;
	}
	
	private TtVehicleChangePO assembleVehicleChangePO() {
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
		String errorDealerId = CommonUtils.checkNull(request.getParamValue("errorDealerId"));//数据提报错误经销商ID
		String errorType = CommonUtils.checkNull(request.getParamValue("errorType"));//数据提报错误类型
		String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注
		
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
		if (Utility.testString(mileage)) {
			po.setMileage(Double.parseDouble(mileage));
		}
		if (Utility.testString(mileage)) {
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
		if (Utility.testString(errorDealerId)) {
			po.setErrorDealerId(Long.parseLong(errorDealerId));
		}
		po.setErrorType(Integer.parseInt(errorType));
		po.setApplyRemark(remark);
		Date sysDate = new Date();
		po.setCreateDate(sysDate);
		po.setCreateBy(logonUser.getUserId());
		po.setId(Long.parseLong(SequenceManager.getSequence("")));
		po.setIsDel(STATUS_ENABLE);
		if ("0".equals(addFlag)) {
			//保存操作
			po.setStatus(VEHICLE_CHANGE_STATUS_01);//已保存
		} else {
			//提报操作
			po.setApplyDate(sysDate);//申请时间
			po.setApplyPerson(logonUser.getUserId());//申请人
			po.setStatus(VEHICLE_CHANGE_STATUS_02);//审核中
		}
		return po;
	}
	/**
	 * 
	* @Title: assembleVehicleCheckPO 
	* @Description: TODO(组装车辆审核PO) 
	* @return TtVehicleChangePO    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private TtVehicleChangePO assembleVehicleCheckPO() throws Exception {
		String checkFlag = request.getParamValue("checkFlag");//判断车辆信息审核页面时同意还是退回 0:同意 1:退回
		TtVehicleChangePO dpo = new TtVehicleChangePO();
		String ctmId = request.getParamValue("ctmId");
		String ctmName = request.getParamValue("ctm_name");
		String ctmPhone = request.getParamValue("ctm_phone");
		String ctmAddress = request.getParamValue("ctm_address");
		String checkRemark = CommonUtils.checkNull(request.getParamValue("checkRemark"));//备注
//		/checkRemark = new String(checkRemark.getBytes("ISO-8859-1"), "UTF-8");
		String changeType = CommonUtils.checkNull(request.getParamValue("changeType"));//申请类型
		String vin = CommonUtils.checkNull(request.getParamValue("vin"));//申请类型
		dpo.setCheckRemark(checkRemark);
		dpo.setUpdateBy(logonUser.getUserId());
		Date sysDate = new Date();
		dpo.setUpdateDate(sysDate);
		dpo.setCheckPerson(logonUser.getUserId());
		dpo.setCheckDate(sysDate);
		if ("0".equals(checkFlag)) {
			//将状态更新成审核通过
			dpo.setStatus(Constant.VEHICLE_CHANGE_STATUS_03);
			TmVehiclePO svehicle = new TmVehiclePO();//条件po
			svehicle.setVin(vin);
			TmVehiclePO dvehicle = new TmVehiclePO();//更改数据po
			dvehicle = getTmVehiclePOByType(Integer.parseInt(changeType),vin);
			dao.update(svehicle, dvehicle);//更新车辆表数据
			
			//如果是车主信息变更，且是审核通过的话，将此车主原信息写进TT_CUSTOMER_BAK表中
			if(Constant.VEHICLE_CHANGE_TYPE_05.toString().equals(changeType)){
				StringBuffer sql = new StringBuffer("\n");
				sql.append("insert into tt_customer_bak\n");
				sql.append("  select *\n");  
				sql.append("    from (select "+SequenceManager.getSequence("")+" id,\n");  
				sql.append("                 "+dpo.getApplyPerson()+" apply_dealer_id,\n");  
				sql.append("                 sysdate change_date,\n");  
				sql.append("                 "+logonUser.getUserId()+" approve_person,\n");  
				sql.append("                 c.*\n");  
				sql.append("            from tt_customer c\n");  
				sql.append("           where c.ctm_id = "+ctmId+")\n");
				dao.myInsert(sql.toString());
				
				StringBuffer con = new StringBuffer("\n");
				con.append(" update tt_customer set ctm_name='").append(ctmName).append("',main_phone=").append(ctmPhone).append(",address='").append(ctmAddress).append("' where ctm_id=").append(ctmId);
				dao.update(con.toString(), null);
			}
		} else {
			//将状态更新成审核退回
			dpo.setStatus(Constant.VEHICLE_CHANGE_STATUS_04);
		}
		return dpo;
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
	private TmVehiclePO getTmVehiclePOByType(Integer changeType,String vin) throws Exception {
		TmVehiclePO vehicle = new TmVehiclePO();
		if (changeType.intValue() == VEHICLE_CHANGE_TYPE_01.intValue()) {//行驶里程变更
			vehicle.setMileage(Double.parseDouble(request.getParamValue("changeData")));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_02) {//保养次数变更
			vehicle.setFreeTimes(Integer.parseInt(request.getParamValue("changeData")));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_03) {//购车时间变更
			TmVehiclePO v = new TmVehiclePO();
			v.setVin(vin);
			CommonUtils.jugeVinNull(vin);
			v = (TmVehiclePO) dao.select(v).get(0);
			SimpleDateFormat sdf = new SimpleDateFormat("", Locale.CHINESE); 
			sdf.applyPattern("yyyy-MM-dd");     
			java.util.Date date = sdf.parse(request.getParamValue("changeData")); 
			Calendar cal= Calendar.getInstance();      
			cal.setTime(date);      
			TtAsWrGamePO gamePO = new TtAsWrGamePO();
			gamePO.setId(v.getClaimTacticsId());
			List<TtAsWrGamePO> listgame = dao.select(gamePO);
			if(listgame != null && listgame.size()>0)
			{
				cal.add(Calendar.MONTH, +listgame.get(0).getWrMonth()); 
			}
			   
			vehicle.setPurchasedDate(DateTimeUtil.stringToDate(request.getParamValue("changeData")));
			vehicle.setWrEndDate(DateTimeUtil.stringToDate(sdf.format(cal.getTime())));//修改三包到期时间
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_04) {//三包策略变更
			TtAsWrGamePO game = new TtAsWrGamePO();
			game.setGameCode(request.getParamValue("changeData"));
			List<TtAsWrGamePO> pos = dao.select(game);
			vehicle.setClaimTacticsId(pos.get(0).getId());
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_05) {//车主信息变更
			vehicle.setUpdateDate(new Date());
		} else {
			throw new IllegalArgumentException("Invalid changeType, changeType == " + changeType);
		}
		return vehicle;
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
			return CommonUtils.checkNull(request.getParamValue("cfreeTimes"));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_03) {//购车时间变更
			return CommonUtils.checkNull(request.getParamValue("cpurchasedDate"));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_04) {//三包策略变更
			return CommonUtils.checkNull(request.getParamValue("cwrGames"));
		} else if (changeType.intValue() == VEHICLE_CHANGE_TYPE_05) {//车主信息变更
			return null;
		} else {
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
			act.setOutData("vehicle", map);
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更审核查询");
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
		String dealerId = request.getParamValue("dealerId");
		String errorDealerId = request.getParamValue("errorDealerId");
		String checkQueryStatus = request.getParamValue("checkQueryStatus");
		TtVehicleChangeBean bean = new TtVehicleChangeBean();
		bean.setVin(vin);
		bean.setVehicleNo(vehicleNo);
		if (Utility.testString(changeType)) {
			bean.setApplyType(Integer.parseInt(changeType));
		}
		if (Utility.testString(status)) {
			bean.setStatus(Integer.parseInt(status));
		}
		if (Utility.testString(dealerId)) {
			bean.setApplyPerson(Long.parseLong(dealerId));
		}
		if (Utility.testString(errorDealerId)) {
			bean.setErrorDealerId(Long.parseLong(errorDealerId));
		}
		bean.setCheckQueryStatus(checkQueryStatus);
		bean.setApplyStartDate(request.getParamValue("beginDate"));
		bean.setApplyEndDate(request.getParamValue("endDate"));
		
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
			if ("0".equals(viewFlag)) {
				act.setForword(CHECK_URL);
			} else {
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
	* @Title: queryVehicleChangeInfo 
	* @Description: TODO(分页查询车辆三包变更信息) 
	* @throws
	 */
	public void queryVehicleRuleChangeInfo() {
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String vehicleNo = CommonUtils.checkNull(request.getParamValue("vehicleNo"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			if(!"".equals(COMMAND)){
				String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
				Integer curPage = request.getParamValue("curPage") != null ?
						Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.queryVehicleRuleChangeInfo(vin,vehicleNo,status,beginDate,endDate,
						dealerId, curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}else{
				act.setForword(VEHICLE_RULE_CHANGE_QUERY);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: queryVehicleChangeInfo 
	* @Description: TODO(查询车辆三包变更信息（审核）) 
	* @throws
	 */
	public void queryVehicleRuleChangeInfoDetail() {
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
			Map<String, Object> map = dao.queryVehicleRuleChangeAndRuleListDetailByChangeId(Long.valueOf("".equals(dealerId)?"0":dealerId), changeId);
			Map<String, Object> usualMap = dao.queryVehicleUsualRuleChangeAndRuleListDetailByChangeId(partCode);
			Map<String, Object> dealerInfo = dao.getPartDetailByPartCode(map.get("PART_CODE").toString());//配件详细
			List listGame = dao.queryVehicleGame();
			
			//区分微车和轿车系统
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List list = dao.select(code) ;
			if(list.size()>0){
				code = (TcCodePO)list.get(0);
			
				act.setOutData("code", code) ;
				//轿车添加配件是不是监控判断
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					
				    TtAsWrAuthmonitorpartPO monitor = new TtAsWrAuthmonitorpartPO();
				    monitor.setPartCode(map.get("PART_CODE").toString());
				    monitor.setIsDel(0);
				    List<TtAsWrAuthmonitorpartPO> listMon = dao.select(monitor);
				    if(listMon!=null){
				    	if(listMon.size()>0){
				    		act.setOutData("monFlag", "1");
				    	}else{
				    		act.setOutData("monFlag", "0");
				    	}
				    }else{
				    	act.setOutData("monFlag", "0");
				    }

				}
			}
			
			
			act.setOutData("map", map);
			act.setOutData("usualMap", usualMap);
			act.setOutData("listGame", listGame);
			act.setOutData("dealerInfo", dealerInfo);
			act.setForword(VEHICLE_RULE_CHANGE_CHECK);

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: queryVehicleRuleChangeDetailView 
	* @Description: TODO(查询车辆三包变更信息(查看)) 
	* @throws
	 */
	public void queryVehicleRuleChangeDetailView() {
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
			Map<String, Object> map = dao.queryVehicleRuleChangeAndRuleListDetailByChangeId(Long.valueOf("".equals(dealerId)?"0":dealerId), changeId);
			Map<String, Object> usualMap = dao.queryVehicleUsualRuleChangeAndRuleListDetailByChangeId(partCode);
			Map<String, Object> dealerInfo = dao.getPartDetailByPartCode(map.get("PART_CODE").toString());//配件详细
			//区分微车和轿车系统
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List list = dao.select(code) ;
			if(list.size()>0){
				code = (TcCodePO)list.get(0);
			
				act.setOutData("code", code) ;
				//轿车添加配件是不是监控判断
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					
				    TtAsWrAuthmonitorpartPO monitor = new TtAsWrAuthmonitorpartPO();
				    monitor.setPartCode(map.get("PART_CODE").toString());
				    List<TtAsWrAuthmonitorpartPO> listMon = dao.select(monitor);
				    if(listMon!=null){
				    	if(listMon.size()>0){
				    		act.setOutData("monFlag", "1");
				    	}else{
				    		act.setOutData("monFlag", "0");
				    	}
				    }else{
				    	act.setOutData("monFlag", "0");
				    }

				}
			}
			
			act.setOutData("map", map);
			act.setOutData("usualMap", usualMap);
			act.setOutData("dealerInfo", dealerInfo);
			act.setForword(VEHICLE_RULE_CHANGE_VIEW);

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	* @Title: queryVehicleChangeInfo 
	* @Description: TODO(查询车辆策略) 
	* @throws
	 */
	public void getGameBySomething() {
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String usualRule = CommonUtils.checkNull(request.getParamValue("usualRule"));
			List listGame = null;
			if(!"".equals(usualRule)){
				listGame = dao.queryVehicleGameByChangeId(changeId);
			}else{
				listGame = dao.queryVehicleGame();
			}
			
			act.setOutData("listGame", listGame);
			act.setForword(VEHICLE_RULE_CHANGE_CHECK);

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: queryVehicleChangeInfo 
	* @Description: TODO(根据策略查询规则及明细) 
	* @throws
	 */
	public void getRuleDetailFromGame() {
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));
			String gameId = request.getParamValue("gameSel");
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
			
			
			Map<String,Object> mapRuleDetail = dao.queryVehicleRuleDetailByGameId(gameId, partCode);
			Map<String,Object> mapRuleCode = dao.queryVehicleRuleCodeByGameId(gameId); 
			act.setOutData("mapRuleDetail", mapRuleDetail);
			act.setOutData("mapRuleCode", mapRuleCode);

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: queryVehicleChangeInfo 
	* @Description: TODO(同意车辆三包规则修改) 
	* @throws
	 */
	public void doRuleChangeCheck(){
		try{
			String sysRule = CommonUtils.checkNull(request.getParamValue("sysRule"));
			String usualRule = CommonUtils.checkNull(request.getParamValue("usualRule"));
			String sysGame = CommonUtils.checkNull(request.getParamValue("sysGame"));
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId"));
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
			String partName = CommonUtils.checkNull(request.getParamValue("partName"));
			String checkRemark = CommonUtils.checkNull(request.getParamValue("checkRemark"));
			String ruleCode = CommonUtils.checkNull(request.getParamValue("ruleCode"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String gameId = CommonUtils.checkNull(request.getParamValue("gameId"));
			String checkFlag = CommonUtils.checkNull(request.getParamValue("checkFlag"));
			String ruleListId = CommonUtils.checkNull(request.getParamValue("ruleListId"));
			//判断是否存在该车对应系统规则
			Map<String,Object> mapGame = dao.queryVehicleRuleDetailByGameId(gameId, partCode);
			//判断是否存在通用三包规则
			Map<String,Object> usualMap = dao.queryVehicleUsualRuleChangeAndRuleListDetailByChangeId(partCode);
			if("0".equals(checkFlag)){
				//修改申请表
				TtVehicleChangePO change = new TtVehicleChangePO();
				change.setId(Long.valueOf(changeId));
				TtVehicleChangePO changeValue = new TtVehicleChangePO();
				changeValue.setStatus(Constant.VEHICLE_CHANGE_STATUS_03);
				changeValue.setCheckPerson(logonUser.getUserId());
				changeValue.setCheckDate(new Date());
				changeValue.setCheckRemark(checkRemark);
				changeValue.setUpdateBy(logonUser.getUserId());
				changeValue.setUpdateDate(new Date());
				
				//系统三包规则
				if(!"".equals(sysRule)){
					String changeSysClaimMonth = CommonUtils.checkNull(request.getParamValue("changeSysClaimMonth"));
					String changeSysClaimMelieage = CommonUtils.checkNull(request.getParamValue("changeSysClaimMelieage"));
					
					
					//通过车辆策略和配件代码查询RULE_LIST是否存在该配件
					if(mapGame==null){    //不存在，使用通用三包规则，插入新的规则配件
						
						TtAsWrGamePO game = new TtAsWrGamePO();
						game.setId(Long.valueOf(gameId));
						TtAsWrGamePO gameValue = (TtAsWrGamePO) dao.select(game).get(0);
						
						TtAsWrRuleListPO ruleList = new TtAsWrRuleListPO();
						ruleList.setId(Utility.getLong(SequenceManager.getSequence("")));
						ruleList.setRuleId(gameValue.getRuleId());
						ruleList.setPartCode(partCode);
						ruleList.setPartName(partName);
						ruleList.setClaimMonth(Integer.valueOf(changeSysClaimMonth));
						ruleList.setClaimMelieage(Double.valueOf(changeSysClaimMelieage));
						ruleList.setCreateBy(logonUser.getUserId());
						ruleList.setCreateDate(new Date());
						ruleList.setUpdateBy(logonUser.getUserId());
						ruleList.setUpdateDate(new Date());
						dao.insert(ruleList);
						//更新以前申请时为通用三包规则为最新规则
						TtAsWrRulePO r = new TtAsWrRulePO();
						r.setRuleCode(Constant.COMMON_RULE.toString());
						TtAsWrRulePO rRes = (TtAsWrRulePO) dao.select(r).get(0);  //取通用三包规则
						TtAsWrRuleListPO l = new TtAsWrRuleListPO();
						l.setRuleId(rRes.getId());
						l.setPartCode(partCode);
						TtAsWrRuleListPO lRes = (TtAsWrRuleListPO) dao.select(l).get(0);//取该配件通用明细ID
						TtVehicleChangePO cha = new TtVehicleChangePO();
						cha.setRuleListId(lRes.getId());
						TtVehicleChangePO chaValue = new TtVehicleChangePO();
						chaValue.setRuleListId(ruleList.getId());
						dao.update(cha, chaValue);                                //更新以前的申请外键
					}else{                //存在，修改该规则配件
						TtAsWrRuleListPO ruleList = new TtAsWrRuleListPO();
						ruleList.setId(Long.valueOf(mapGame.get("LISTID").toString()));
						TtAsWrRuleListPO ruleListValue = new TtAsWrRuleListPO();
						ruleList.setClaimMonth(Integer.valueOf(changeSysClaimMonth));
						ruleList.setClaimMelieage(Double.valueOf(changeSysClaimMelieage));
						ruleListValue.setUpdateBy(logonUser.getUserId());
						ruleListValue.setUpdateDate(new Date());
						dao.update(ruleList, ruleListValue);
					}
				}
				//通用三包规则
				if(!"".equals(usualRule)){
					String changeUsualClaimMonth = CommonUtils.checkNull(request.getParamValue("changeUsualClaimMonth"));
					String changeUsualClaimMelieage = CommonUtils.checkNull(request.getParamValue("changeUsualClaimMelieage"));
					
					
					if(usualMap==null){    //不存在，使用通用三包规则，插入新的规则配件
						
						TtAsWrRulePO rule = new TtAsWrRulePO();
						rule.setRuleType(Constant.RULE_TYPE_01);
						rule.setRuleCode(Constant.COMMON_RULE.toString());
						TtAsWrRulePO ruleValue = (TtAsWrRulePO) dao.select(rule).get(0);
						
						TtAsWrRuleListPO ruleList = new TtAsWrRuleListPO();
						ruleList.setId(Utility.getLong(SequenceManager.getSequence("")));
						ruleList.setRuleId(ruleValue.getId());
						ruleList.setPartCode(partCode);
						ruleList.setPartName(partName);
						ruleList.setClaimMonth(Integer.valueOf(changeUsualClaimMonth));
						ruleList.setClaimMelieage(Double.valueOf(changeUsualClaimMelieage));
						ruleList.setCreateBy(logonUser.getUserId());
						ruleList.setCreateDate(new Date());
						ruleList.setUpdateBy(logonUser.getUserId());
						ruleList.setUpdateDate(new Date());
						dao.insert(ruleList);
					}else{                //存在，修改该规则配件
						TtAsWrRuleListPO ruleList = new TtAsWrRuleListPO();
						ruleList.setId(Long.valueOf(ruleListId));
						TtAsWrRuleListPO ruleListValue = new TtAsWrRuleListPO();
						ruleList.setClaimMonth(Integer.valueOf(changeUsualClaimMonth));
						ruleList.setClaimMelieage(Double.valueOf(changeUsualClaimMelieage));
						ruleListValue.setUpdateBy(logonUser.getUserId());
						ruleListValue.setUpdateDate(new Date());
						dao.update(ruleList, ruleListValue);
					}
					
				}
				//修改三包策略
				if(!"".equals(sysGame)){
					String gameSel = CommonUtils.checkNull(request.getParamValue("gameSel"));
					String claimMonth = CommonUtils.checkNull(request.getParamValue("claimMonth"));
					String claimMelieage = CommonUtils.checkNull(request.getParamValue("claimMelieage"));
					if(!"".equals(claimMonth)&&!"".equals(claimMelieage)){
						//修改车辆策略
						TmVehiclePO veh = new TmVehiclePO();
						veh.setVin(vin);
						TmVehiclePO vehValue = new TmVehiclePO();
						vehValue.setClaimTacticsId(Long.valueOf(gameSel));
						vehValue.setUpdateBy(logonUser.getUserId());
						vehValue.setUpdateDate(new Date());
						dao.update(veh, vehValue);
						
						
						//修改车辆配件规则策略
						TtAsWrGamePO game = new TtAsWrGamePO();
						game.setId(Long.valueOf(gameSel));
						TtAsWrGamePO gameValue = (TtAsWrGamePO) dao.select(game).get(0);
						TtAsWrRulePO rule = new TtAsWrRulePO();
						rule.setId(gameValue.getRuleId());
						TtAsWrRulePO ruleValue = (TtAsWrRulePO) dao.select(rule).get(0);
						TtAsWrRuleListPO ruleListCon = new TtAsWrRuleListPO();
						ruleListCon.setRuleId(ruleValue.getId());
						ruleListCon.setPartCode(partCode);
						TtAsWrRuleListPO ruleListConValue = (TtAsWrRuleListPO)dao.select(ruleListCon).get(0);
						
						TtAsWrRuleListPO ruleList = new TtAsWrRuleListPO();
						ruleList.setId(ruleListConValue.getId());
						TtAsWrRuleListPO ruleListValue = new TtAsWrRuleListPO();
						ruleList.setClaimMonth(Integer.valueOf(claimMonth));
						ruleList.setClaimMelieage(Double.valueOf(claimMelieage));
						ruleListValue.setUpdateBy(logonUser.getUserId());
						ruleListValue.setUpdateDate(new Date());
						dao.update(ruleList, ruleListValue);
						
						//申请表里三包策略外键修改
						changeValue.setClaimTacticsId(Long.valueOf(gameSel));
					}
				}
				
				dao.update(change, changeValue);
			}else{    //退回
				TtVehicleChangePO change = new TtVehicleChangePO();
				change.setId(Long.valueOf(changeId));
				TtVehicleChangePO changeValue = new TtVehicleChangePO();
				changeValue.setCheckPerson(logonUser.getUserId());
				changeValue.setCheckDate(new Date());
				changeValue.setCheckRemark(checkRemark);
				changeValue.setStatus(Constant.VEHICLE_CHANGE_STATUS_04);
				changeValue.setUpdateBy(logonUser.getUserId());
				changeValue.setUpdateDate(new Date());
				dao.update(change, changeValue);
			}
			//轿车添加修改配件信息功能
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List<TcCodePO> list = dao.select(code) ;
			if(list.size()>0){
	
				if(Constant.chana_jc==Integer.parseInt(list.get(0).getCodeId().toString())){
					modPartInfoInChgVehRule();
				}
			}
			//轿车添加修改配件信息功能
			
			act.setForword(VEHICLE_RULE_CHANGE_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"车辆信息变更审核修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: queryVehicleChangeInfo 
	* @Description: TODO(轿车车辆三包信息变更修改配件信息) 
	* @throws
	 */
	public void modPartInfoInChgVehRule(){
		try{
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
			String isReturn = CommonUtils.checkNull(request.getParamValue("isReturn"));
			String IS_NEW_PART = CommonUtils.checkNull(request.getParamValue("IS_NEW_PART"));
			
			TmPtPartBasePO partSet = new TmPtPartBasePO();
			partSet.setPartCode(partCode);
			TmPtPartBasePO partValue = new TmPtPartBasePO();
			partValue.setIsReturn(Integer.valueOf(isReturn));
			partValue.setIsNewPart(Integer.valueOf(IS_NEW_PART));
			partValue.setUpdateDate(new Date());
			partValue.setUpdateBy(logonUser.getUserId());
			
			dao.update(partSet, partValue);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"配件信息修改失败！");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
