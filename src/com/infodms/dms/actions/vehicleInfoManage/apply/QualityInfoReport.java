package com.infodms.dms.actions.vehicleInfoManage.apply;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.customerRelationships.baseSetting.seatsSet;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.RegionManageDao;
import com.infodms.dms.dao.vehicleInfoManage.QualityInfoReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtSalesQualityInfoReportPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 市场质量信息申报
 * @author wangming
 * @date 2013/7/31
 */
public class QualityInfoReport {
	
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//市场质量信息申报初始化页面
	private final String qualityInfoReportUrl = "/jsp/vehicleInfoManage/apply/qualityInfoReport.jsp";
	//市场质量信息申报新增页面
	private final String qualityInfoReportUpdateUrl = "/jsp/vehicleInfoManage/apply/qualityInfoReportUpdate.jsp";
	//市场质量信息申报查看页面
	private final String qualityInfoReportViewUrl = "/jsp/vehicleInfoManage/apply/qualityInfoReportView.jsp";
	
	//市场质量信息申报打印页面
	private final String qualityInfoReportPrintUrl = "/jsp/vehicleInfoManage/apply/qualityInfoReportPrint.jsp";
	//市场质量信息申报配件
	private final String selectPart ="/jsp/vehicleInfoManage/apply/selectPart.jsp";
	//选择索赔单号页面
	private final String selectPartFirst ="/jsp/vehicleInfoManage/apply/selectPartFirst.jsp";
	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 市场质量信息申报初始化
	 */
	public void qualityInfoReportInit(){		
		try{
			act.setForword(qualityInfoReportUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"市场质量信息申报初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryQualityInfoReport(){
		act.getResponse().setContentType("application/json");
		try{
			
			String reportName = CommonUtils.checkNull(request.getParamValue("reportName"));  				
			String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));  				
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));  				
			
			QualityInfoReportDao dao = QualityInfoReportDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> qualityInfoReportData = dao.queryQualityInfoReport(reportName,ctmName,dealerName,logonUser.getDealerId(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", qualityInfoReportData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"市场质量信息申报查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 市场质量信息申报更新
	 * @Description: 市场质量信息申报更新
	 * LastDate    : 2013-7-31
	 */
	public void updateQualityInfoReport(){
		
		QualityInfoReportDao dao = QualityInfoReportDao.getInstance();
		String qualityReportId = CommonUtils.checkNull(request.getParamValue("qualityReportId")); 
		//新增
		if("".equals(qualityReportId)){
			act.setOutData("engineTypeList", engineTypeInit());
			act.setOutData("doorList", doorInit());
			act.setOutData("driveTypeList", driveTypeInit());
			act.setOutData("equipmentList", equipmentInit());
			act.setOutData("purposeList", purposeInit());
			act.setOutData("roadList", roadInit());
			act.setOutData("temperatureAndTimeList", temperatureAndTimeInit());
			act.setOutData("happendTimeList", happendTimeInit());
			act.setOutData("speedList", speedInit());
			act.setOutData("rainList", rainInit());
			act.setOutData("airConditionStatusList", airConditionStatusInit());
			act.setOutData("usedList", usedInit());
			act.setOutData("importantLevelList", importantLevelInit());
			String dealerId = logonUser.getDealerId();
			
			// 2013.9.9 艾春添加 增加服务站代码查询
			Map<String,Object> data = dao.queryDataById(dealerId);
			act.setOutData("dealerdata", data);
		//修改
		}else{
			TtSalesQualityInfoReportPO ttSalesQualityInfoReportPO = new TtSalesQualityInfoReportPO();
			ttSalesQualityInfoReportPO.setQualityReportId(Long.parseLong(qualityReportId));
			List<TtSalesQualityInfoReportPO> ttSalesQualityInfoReportPOList = dao.select(ttSalesQualityInfoReportPO);
			if(ttSalesQualityInfoReportPOList!=null){
				ttSalesQualityInfoReportPO = ttSalesQualityInfoReportPOList.get(0);
				CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
				List<Map<String, Object>> markerList =  commonUtilDao.cascadePartMarker(ttSalesQualityInfoReportPO.getPartId());
				for(Map<String, Object> map : markerList){
					if(map.get("MAKER_ID").toString().equals(ttSalesQualityInfoReportPO.getMarkerId().toString())){
						map.put("isCheck", true);
					}
				}
				act.setOutData("markerList", markerList);
				act.setOutData("ttSalesQualityInfoReportPO", ttSalesQualityInfoReportPO);
			}			
			
			act.setOutData("engineTypeList", setIsCheck(engineTypeInit(), ttSalesQualityInfoReportPO.getEngineType()));
			act.setOutData("doorList", setIsCheck(doorInit(), ttSalesQualityInfoReportPO.getVehicleDoor()));
			act.setOutData("driveTypeList", setIsCheck(driveTypeInit(), ttSalesQualityInfoReportPO.getDriveType()));
			act.setOutData("equipmentList", setIsCheck(equipmentInit(), ttSalesQualityInfoReportPO.getEquipment()));
			act.setOutData("purposeList", setSpecialIsCheck(purposeInit(), ttSalesQualityInfoReportPO.getPurpose(),"其他"));
			act.setOutData("roadList", setIsCheck(roadInit(), ttSalesQualityInfoReportPO.getRoad()));
			act.setOutData("temperatureAndTimeList", setIsCheck(temperatureAndTimeInit(), ttSalesQualityInfoReportPO.getTemperature()));
			act.setOutData("happendTimeList", setIsCheck(happendTimeInit(), ttSalesQualityInfoReportPO.getHappenTime()));
			act.setOutData("speedList", setIsCheck(speedInit(), ttSalesQualityInfoReportPO.getHappenSpeed()));
			act.setOutData("rainList", setIsCheck(rainInit(), ttSalesQualityInfoReportPO.getRain()));
			act.setOutData("airConditionStatusList", setIsCheck(airConditionStatusInit(), ttSalesQualityInfoReportPO.getAirConditionStatus()));
			act.setOutData("usedList", setIsCheck(usedInit(), ttSalesQualityInfoReportPO.getUsed()));
			act.setOutData("importantLevelList", setIsCheck(importantLevelInit(), ttSalesQualityInfoReportPO.getImportantLevel()));
		
			List<FsFileuploadPO> fileList = dao.queryAttById(qualityReportId);// 取得附件
			act.setOutData("fileList", fileList);
			
			if(null != ttSalesQualityInfoReportPO.getDealerId()) {
				// 2013.9.9 艾春添加 增加服务站代码查询
				Map<String,Object> data = dao.queryDataById(ttSalesQualityInfoReportPO.getDealerId().toString());
				act.setOutData("dealerdata", data);
			}
			
		}
		
		act.setForword(qualityInfoReportUpdateUrl);
	}
	
	/**
	 * 
	 * @Title      : 市场质量信息申报更新提交
	 * @Description: 市场质量信息申报更新提交
	 * LastDate    : 2013-7-31
	 */
	public void updateQualityInfoReportSubmit(){
		act.getResponse().setContentType("application/json");
		try{
			String qualiteReportId = CommonUtils.checkNull(request.getParamValue("qualiteReportId")); 
			String qualiteId = ""; 
			QualityInfoReportDao dao = QualityInfoReportDao.getInstance();
			TtSalesQualityInfoReportPO ttSalesQualityInfoReportPO = setTtSalesQualityInfoReportPO();
			TtSalesQualityInfoReportPO ttSalesQualityInfoReportPO1 = new TtSalesQualityInfoReportPO();
			if("".equals(qualiteReportId)){
				ttSalesQualityInfoReportPO.setQualityReportId(new Long(SequenceManager.getSequence("")));
				ttSalesQualityInfoReportPO.setDealerId(Long.parseLong(logonUser.getDealerId()));
				ttSalesQualityInfoReportPO.setCreateBy(logonUser.getUserId());
				ttSalesQualityInfoReportPO.setCreateDate(new Date());
				dao.insert(ttSalesQualityInfoReportPO);
				
				qualiteId = ttSalesQualityInfoReportPO.getQualityReportId().toString();
			}else{
				ttSalesQualityInfoReportPO1.setQualityReportId(Long.parseLong(qualiteReportId));
				ttSalesQualityInfoReportPO.setUpdateBy(logonUser.getUserId());
				ttSalesQualityInfoReportPO.setUpdateDate(new Date());
				dao.update(ttSalesQualityInfoReportPO1,ttSalesQualityInfoReportPO);
				qualiteId = qualiteReportId;
			}
			
			String[] fjids = request.getParamValues("fjid");
			if(!"".equals(qualiteReportId)){
				FileUploadManager.delAllFilesUploadByBusiness(qualiteReportId, fjids);
			}
			FileUploadManager.fileUploadByBusiness(qualiteId, fjids, logonUser);
			
			if("".equals(qualiteReportId)){
				act.setOutData("qualiteReportId", qualiteId);
				act.setOutData("success", "addtrue");
			}else{
				act.setOutData("success", "updatetrue");
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"市场质量信息更新提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void applyQualityInfoReportSubmit(){
		act.getResponse().setContentType("application/json");
		try{
			String qualiteReportId = CommonUtils.checkNull(request.getParamValue("qualiteReportId")); 
			String qualiteId = ""; 
			QualityInfoReportDao dao = QualityInfoReportDao.getInstance();
			TtSalesQualityInfoReportPO ttSalesQualityInfoReportPO = setTtSalesQualityInfoReportPO();
			TtSalesQualityInfoReportPO ttSalesQualityInfoReportPO1 = new TtSalesQualityInfoReportPO();
			if("".equals(qualiteReportId)){
				ttSalesQualityInfoReportPO.setQualityReportId(new Long(SequenceManager.getSequence("")));
				ttSalesQualityInfoReportPO.setCreateBy(logonUser.getUserId());
				ttSalesQualityInfoReportPO.setCreateDate(new Date());
				ttSalesQualityInfoReportPO.setApplayBy(logonUser.getUserId());
				ttSalesQualityInfoReportPO.setApplayDate(new Date());
				//2013-11-07 添加经销商ID 开始 wangming
				ttSalesQualityInfoReportPO.setDealerId(Long.parseLong(logonUser.getDealerId()));
				//2013-11-07 添加经销商ID 结束 wangming
				ttSalesQualityInfoReportPO.setVerifyStatus(Constant.Quality_Verify_02);
				dao.insert(ttSalesQualityInfoReportPO);
				
				qualiteId = ttSalesQualityInfoReportPO.getQualityReportId().toString();
			}else{
				ttSalesQualityInfoReportPO1.setQualityReportId(Long.parseLong(qualiteReportId));
				ttSalesQualityInfoReportPO.setUpdateBy(logonUser.getUserId());
				ttSalesQualityInfoReportPO.setUpdateDate(new Date());
				ttSalesQualityInfoReportPO.setApplayBy(logonUser.getUserId());
				ttSalesQualityInfoReportPO.setApplayDate(new Date());
				ttSalesQualityInfoReportPO.setVerifyStatus(Constant.Quality_Verify_02);
				dao.update(ttSalesQualityInfoReportPO1,ttSalesQualityInfoReportPO);
				qualiteId = qualiteReportId;
			}
			
			String[] fjids = request.getParamValues("fjid");
			if(!"".equals(qualiteReportId)){
				FileUploadManager.delAllFilesUploadByBusiness(qualiteReportId, fjids);
			}
			FileUploadManager.fileUploadByBusiness(qualiteId, fjids, logonUser);
			
			act.setOutData("success", "true");
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"市场质量信息申报提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void delQualityInfoReport(){
		try{
			String ids = request.getParamValue("ids");
			ids = ids.replaceAll(",", "','");
			QualityInfoReportDao dao = QualityInfoReportDao.getInstance();
			dao.deleteQualityInfoReportDao(ids);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"市场质量信息");
			logger.error(logger,e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 
	 * @Title      : 市场质量信息申报查看
	 * @Description: 市场质量信息申报查看
	 * LastDate    : 2013-7-31
	 */
	public void viewQualityInfoReport(){
		
		String qualityReportId = CommonUtils.checkNull(request.getParamValue("qualityReportId")); 
		
		QualityInfoReportDao dao = QualityInfoReportDao.getInstance();
		TtSalesQualityInfoReportPO ttSalesQualityInfoReportPO = new TtSalesQualityInfoReportPO();
		ttSalesQualityInfoReportPO.setQualityReportId(Long.parseLong(qualityReportId));
		List<TtSalesQualityInfoReportPO> ttSalesQualityInfoReportPOList = dao.select(ttSalesQualityInfoReportPO);
		if(ttSalesQualityInfoReportPOList!=null){
			ttSalesQualityInfoReportPO = ttSalesQualityInfoReportPOList.get(0);
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			List<Map<String, Object>> markerList =  commonUtilDao.cascadePartMarker(ttSalesQualityInfoReportPO.getPartId());
			for(Map<String, Object> map : markerList){
				if(map.get("MAKER_ID").toString().equals(ttSalesQualityInfoReportPO.getMarkerId().toString())){
					map.put("isCheck", true);
				}
			}
			act.setOutData("markerList", markerList);
			act.setOutData("ttSalesQualityInfoReportPO", ttSalesQualityInfoReportPO);
		}			
		
		act.setOutData("engineTypeList", setIsCheck(engineTypeInit(), ttSalesQualityInfoReportPO.getEngineType()));
		act.setOutData("doorList", setIsCheck(doorInit(), ttSalesQualityInfoReportPO.getVehicleDoor()));
		act.setOutData("driveTypeList", setIsCheck(driveTypeInit(), ttSalesQualityInfoReportPO.getDriveType()));
		act.setOutData("equipmentList", setIsCheck(equipmentInit(), ttSalesQualityInfoReportPO.getEquipment()));
		act.setOutData("purposeList", setSpecialIsCheck(purposeInit(), ttSalesQualityInfoReportPO.getPurpose(),"其他"));
		act.setOutData("roadList", setIsCheck(roadInit(), ttSalesQualityInfoReportPO.getRoad()));
		act.setOutData("temperatureAndTimeList", setIsCheck(temperatureAndTimeInit(), ttSalesQualityInfoReportPO.getTemperature()));
		act.setOutData("happendTimeList", setIsCheck(happendTimeInit(), ttSalesQualityInfoReportPO.getHappenTime()));
		act.setOutData("speedList", setIsCheck(speedInit(), ttSalesQualityInfoReportPO.getHappenSpeed()));
		act.setOutData("rainList", setIsCheck(rainInit(), ttSalesQualityInfoReportPO.getRain()));
		act.setOutData("airConditionStatusList", setIsCheck(airConditionStatusInit(), ttSalesQualityInfoReportPO.getAirConditionStatus()));
		act.setOutData("usedList", setIsCheck(usedInit(), ttSalesQualityInfoReportPO.getUsed()));
		act.setOutData("importantLevelList", setIsCheck(importantLevelInit(), ttSalesQualityInfoReportPO.getImportantLevel()));
	
		List<FsFileuploadPO> fileList = dao.queryAttById(qualityReportId);// 取得附件
		act.setOutData("fileList", fileList);
		
		
		act.setForword(qualityInfoReportViewUrl);
	}
	
	/**
	 * 
	 * @Title      : 市场质量信息申报打印
	 * @Description: 市场质量信息申报打印
	 * LastDate    : 2013-7-31
	 */
	public void printQualityInfoReport(){
		
		String qualityReportId = CommonUtils.checkNull(request.getParamValue("qualityReportId")); 
		
		QualityInfoReportDao dao = QualityInfoReportDao.getInstance();
		TtSalesQualityInfoReportPO ttSalesQualityInfoReportPO = new TtSalesQualityInfoReportPO();
		ttSalesQualityInfoReportPO.setQualityReportId(Long.parseLong(qualityReportId));
		List<TtSalesQualityInfoReportPO> ttSalesQualityInfoReportPOList = dao.select(ttSalesQualityInfoReportPO);
		if(ttSalesQualityInfoReportPOList!=null){
			ttSalesQualityInfoReportPO = ttSalesQualityInfoReportPOList.get(0);
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			List<Map<String, Object>> markerList =  commonUtilDao.cascadePartMarker(ttSalesQualityInfoReportPO.getPartId());
			for(Map<String, Object> map : markerList){
				if(map.get("MAKER_ID").toString().equals(ttSalesQualityInfoReportPO.getMarkerId().toString())){
					map.put("isCheck", 1);
				}
			}
			act.setOutData("markerList", markerList);
			act.setOutData("ttSalesQualityInfoReportPO", ttSalesQualityInfoReportPO);
		}			
		
		act.setOutData("engineTypeList", setIsCheck(engineTypeInit(), ttSalesQualityInfoReportPO.getEngineType()));
		act.setOutData("doorList", setIsCheck(doorInit(), ttSalesQualityInfoReportPO.getVehicleDoor()));
		act.setOutData("driveTypeList", setIsCheck(driveTypeInit(), ttSalesQualityInfoReportPO.getDriveType()));
		act.setOutData("equipmentList", setIsCheck(equipmentInit(), ttSalesQualityInfoReportPO.getEquipment()));
		act.setOutData("purposeList", setSpecialIsCheck(purposeInit(), ttSalesQualityInfoReportPO.getPurpose(),"其他"));
		act.setOutData("roadList", setIsCheck(roadInit(), ttSalesQualityInfoReportPO.getRoad()));
		act.setOutData("temperatureAndTimeList", setIsCheck(temperatureAndTimeInit(), ttSalesQualityInfoReportPO.getTemperature()));
		act.setOutData("happendTimeList", setIsCheck(happendTimeInit(), ttSalesQualityInfoReportPO.getHappenTime()));
		act.setOutData("speedList", setIsCheck(speedInit(), ttSalesQualityInfoReportPO.getHappenSpeed()));
		act.setOutData("rainList", setIsCheck(rainInit(), ttSalesQualityInfoReportPO.getRain()));
		act.setOutData("airConditionStatusList", setIsCheck(airConditionStatusInit(), ttSalesQualityInfoReportPO.getAirConditionStatus()));
		act.setOutData("usedList", setIsCheck(usedInit(), ttSalesQualityInfoReportPO.getUsed()));
		act.setOutData("importantLevelList", setIsCheck(importantLevelInit(), ttSalesQualityInfoReportPO.getImportantLevel()));
	
		List<FsFileuploadPO> fileList = dao.queryAttById(qualityReportId);// 取得附件
		act.setOutData("fileList", fileList);
		
		
		act.setForword(qualityInfoReportPrintUrl);
	}
	
	public TtSalesQualityInfoReportPO setTtSalesQualityInfoReportPO() throws Exception{
		String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));  				
		String claimNo = CommonUtils.checkNull(request.getParamValue("claimNo"));  				
		String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));  				
		String reportName = CommonUtils.checkNull(request.getParamValue("reportName"));  				
		String phone = CommonUtils.checkNull(request.getParamValue("phone"));  				
		String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));  				
		String modelName = CommonUtils.checkNull(request.getParamValue("modelName"));  				
		String VIN = CommonUtils.checkNull(request.getParamValue("VIN"));  				
		String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));  				
		String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));  				
		String productDate = CommonUtils.checkNull(request.getParamValue("productDate"));  				
		String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));  				
		String faultName = CommonUtils.checkNull(request.getParamValue("faultName"));  				
		String faultDate = CommonUtils.checkNull(request.getParamValue("faultDate"));  				
		String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));  				
		String ctmPhone = CommonUtils.checkNull(request.getParamValue("ctmPhone"));  
		
		String enginTypesH = CommonUtils.checkNull(request.getParamValue("enginTypesH"));  				
		String doorsH = CommonUtils.checkNull(request.getParamValue("doorsH"));  				
		String driveTypesH = CommonUtils.checkNull(request.getParamValue("driveTypesH"));  				
		String equipmentsH = CommonUtils.checkNull(request.getParamValue("equipmentsH"));  				
		String purposesH = CommonUtils.checkNull(request.getParamValue("purposesH"));  				
		String roadsH = CommonUtils.checkNull(request.getParamValue("roadsH"));  				
		String temperatureAndTimesH = CommonUtils.checkNull(request.getParamValue("temperatureAndTimesH"));  				
		String happendTimesH = CommonUtils.checkNull(request.getParamValue("happendTimesH"));  				
		String speedsH = CommonUtils.checkNull(request.getParamValue("speedsH"));  				
		String rainsH = CommonUtils.checkNull(request.getParamValue("rainsH"));  				
		String usedsH = CommonUtils.checkNull(request.getParamValue("usedsH"));  				
		String airConditionStatussH = CommonUtils.checkNull(request.getParamValue("airConditionStatussH"));  				
		String importantLevelsH = CommonUtils.checkNull(request.getParamValue("importantLevelsH"));  				
		
		String condition = CommonUtils.checkNull(request.getParamValue("condition"));  				
		String checkResult = CommonUtils.checkNull(request.getParamValue("checkResult"));  				
		String content = CommonUtils.checkNull(request.getParamValue("content"));  				
		String partName = CommonUtils.checkNull(request.getParamValue("partName"));  				
		String partId = CommonUtils.checkNull(request.getParamValue("partId"));  				
		String marker = CommonUtils.checkNull(request.getParamValue("marker"));  				
		String remark = CommonUtils.checkNull(request.getParamValue("remark"));  
		
		TtSalesQualityInfoReportPO ttSalesQualityInfoReportPO = new TtSalesQualityInfoReportPO();
		
		ttSalesQualityInfoReportPO.setAirConditionStatus(airConditionStatussH);
		ttSalesQualityInfoReportPO.setCheckResult(checkResult);
		ttSalesQualityInfoReportPO.setClaimNo(claimNo);
		ttSalesQualityInfoReportPO.setCondition(condition);
		ttSalesQualityInfoReportPO.setContent(content);
		ttSalesQualityInfoReportPO.setCtmName(ctmName);
		ttSalesQualityInfoReportPO.setCtmPhone(ctmPhone);
		ttSalesQualityInfoReportPO.setDealerCode(dealerCode);
		ttSalesQualityInfoReportPO.setDealerName(dealerName);
		ttSalesQualityInfoReportPO.setDriveType(driveTypesH);
		ttSalesQualityInfoReportPO.setEngineNo(engineNo);
		ttSalesQualityInfoReportPO.setEngineType(enginTypesH);
		ttSalesQualityInfoReportPO.setEquipment(equipmentsH);
		ttSalesQualityInfoReportPO.setPurpose(purposesH);
		if(!"".equals(faultDate))ttSalesQualityInfoReportPO.setFaultDate(DateTimeUtil.stringToDate(faultDate));
		ttSalesQualityInfoReportPO.setFaultName(faultName);
		ttSalesQualityInfoReportPO.setHappenSpeed(speedsH);
		ttSalesQualityInfoReportPO.setHappenTime(happendTimesH);
		ttSalesQualityInfoReportPO.setImportantLevel(importantLevelsH);
		if(!"".equals(mileage))ttSalesQualityInfoReportPO.setMileage(Double.parseDouble(mileage));
		if(!"".equals(modelId))ttSalesQualityInfoReportPO.setModelId(Long.parseLong(modelId));
		ttSalesQualityInfoReportPO.setModelName(modelName);
		if(!"".equals(partId))ttSalesQualityInfoReportPO.setPartId(Long.parseLong(partId));
		ttSalesQualityInfoReportPO.setPartName(partName);
		ttSalesQualityInfoReportPO.setPhone(phone);
		if(!"".equals(productDate))ttSalesQualityInfoReportPO.setProductDate(DateTimeUtil.stringToDate(productDate));
		if(!"".equals(purchasedDate))ttSalesQualityInfoReportPO.setPurchasedDate(DateTimeUtil.stringToDate(purchasedDate));
		ttSalesQualityInfoReportPO.setRain(rainsH);
		ttSalesQualityInfoReportPO.setRemark(remark);
		ttSalesQualityInfoReportPO.setReportName(reportName);
		if(!"".equals(marker))ttSalesQualityInfoReportPO.setMarkerId(Long.parseLong(marker));
		ttSalesQualityInfoReportPO.setRoad(roadsH);
		ttSalesQualityInfoReportPO.setTemperature(temperatureAndTimesH);
		ttSalesQualityInfoReportPO.setUsed(usedsH);
		ttSalesQualityInfoReportPO.setVehicleDoor(doorsH);
		if(!"".equals(VIN))ttSalesQualityInfoReportPO.setVin(VIN);
		ttSalesQualityInfoReportPO.setVerifyStatus(Constant.Quality_Verify_01);
		return ttSalesQualityInfoReportPO;
	}
	
	public void queryDataByVin(){
		act.getResponse().setContentType("application/json");
		try{
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
					
			QualityInfoReportDao dao = QualityInfoReportDao.getInstance();
			Map<String,Object> data = dao.queryDataByVin(vin);
			
			act.setOutData("data", data);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"质量信息申报VIN带出查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void selectPart(){
		try{
			act.setForword(selectPart);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"质量信息申报");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void selectPartFirst(){
		try{
			act.setForword(selectPartFirst);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"选择索赔单号");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 设置选择框的默认项
	 * @param initList 初始化集合
	 * @param dataStr 默认值 格式为xx,xx,xx
	 * @return
	 */
	private List<Map<String,Object>> setIsCheck(List<Map<String,Object>> initList,String dataStr){
		if(dataStr != null){
			for(String str : dataStr.split(",")){
				for(Map<String,Object> map : initList){
					if(map.get("value").toString().equals(str)){
						map.put("isCheck", true);
					}
				}
			}
		}
		return initList;
	}
	
	/**
	 * 设置带有其它且输入其它内容的选择框的默认项
	 * @param initList 初始化集合
	 * @param dataStr 默认值 格式为xx,xx,xx
	 * @return
	 */
	private List<Map<String,Object>> setSpecialIsCheck(List<Map<String,Object>> initList,String dataStr,String specialStr){
		if(dataStr != null){
			for(String str : dataStr.split(",")){
				for(Map<String,Object> map : initList){
					String [] oStr = str.split("-");
					
					if(map.get("value").toString().equals(oStr[0])){
						map.put("isCheck", true);
						if(specialStr.equals(oStr[0]) && oStr.length>1){
							act.setOutData("otherValue", (String)oStr[1]);
						}
					}
				}
			}
		}
		
		return initList;
	}
	
	
	/**
	 * 发动机类型初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> engineTypeInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "汽油");
		map.put("value", "汽油");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "柴油");
		map.put("value", "柴油");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "CNG");
		map.put("value", "CNG");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "LPG");
		map.put("value", "LPG");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "电动");
		map.put("value", "电动");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "2气门");
		map.put("value", "2气门");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "4气门");
		map.put("value", "4气门");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "涡轮增压");
		map.put("value", "涡轮增压");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 车身初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> doorInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "2门");
		map.put("value", "2门");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "3门");
		map.put("value", "3门");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "2门");
		map.put("value", "2门");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "4门");
		map.put("value", "4门");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "5门");
		map.put("value", "5门");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 驱动方式初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> driveTypeInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "2WD");
		map.put("value", "2WD");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "4WD");
		map.put("value", "4WD");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "3AT");
		map.put("value", "3AT");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "4AT");
		map.put("value", "4AT");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "MT");
		map.put("value", "MT");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "AMT");
		map.put("value", "AMT");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 装备初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> equipmentInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "空调");
		map.put("value", "空调");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "电动窗");
		map.put("value", "电动窗");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "中控锁");
		map.put("value", "中控锁");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "动力转向");
		map.put("value", "动力转向");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "定速巡航");
		map.put("value", "定速巡航");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "发动机防盗");
		map.put("value", "发动机防盗");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "ABS");
		map.put("value", "ABS");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "安全气囊");
		map.put("value", "安全气囊");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 用途初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> purposeInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "客运");
		map.put("value", "客运");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "轻载");
		map.put("value", "轻载");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "家用");
		map.put("value", "家用");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "出租");
		map.put("value", "出租");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "超载");
		map.put("value", "超载");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "长距离运输");
		map.put("value", "长距离运输");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "其他");
		map.put("value", "其他");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 道路状况初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> roadInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "市区");
		map.put("value", "市区");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "铺装路");
		map.put("value", "铺装路");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "郊外");
		map.put("value", "郊外");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "凸凹路");
		map.put("value", "凸凹路");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "高速路");
		map.put("value", "高速路");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "平路");
		map.put("value", "平路");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "山路");
		map.put("value", "山路");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "沙石路");
		map.put("value", "沙石路");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "上坡 ");
		map.put("value", "上坡 ");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "泥路");
		map.put("value", "泥路");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "下坡");
		map.put("value", "下坡 ");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "新雪路");
		map.put("value", "新雪路");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "弯道");
		map.put("value", "弯道");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "压雪路");
		map.put("value", "压雪路");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "泥路");
		map.put("value", "泥路");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 温度/时间 初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> temperatureAndTimeInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "酷暑");
		map.put("value", "酷暑");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "寒冷");
		map.put("value", "寒冷");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "–20℃以下");
		map.put("value", "–20℃以下");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "冷机");
		map.put("value", "冷机");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "暖机");
		map.put("value", "暖机");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "早晨");
		map.put("value", "早晨");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "白天");
		map.put("value", "白天");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "夜晚");
		map.put("value", "夜晚");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 发生速度初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> speedInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "交通阻塞时");
		map.put("value", "交通阻塞时");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "40km/h以下");
		map.put("value", "40km/h以下");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "40km/h以上");
		map.put("value", "40km/h以上");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "80km/h以上");
		map.put("value", "80km/h以上");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 发生时机初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> happendTimeInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "启动时");
		map.put("value", "启动时");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "启动后");
		map.put("value", "启动后");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "起步时");
		map.put("value", "起步时");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "加速时");
		map.put("value", "加速时");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "匀速时");
		map.put("value", "匀速时");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "减速时");
		map.put("value", "减速时");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "制动时");
		map.put("value", "制动时");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "停止间隙");
		map.put("value", "停止间隙");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "空闲期间 ");
		map.put("value", "空闲期间 ");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "高速行驶后");
		map.put("value", "高速行驶后");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "左转向时");
		map.put("value", "左转向时 ");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "右转向时");
		map.put("value", "右转向时");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 雨水初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> rainInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "小雨");
		map.put("value", "小雨");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "大雨");
		map.put("value", "大雨");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "降雨后");
		map.put("value", "降雨后");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "洗车后");
		map.put("value", "洗车后");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "深水路行驶中");
		map.put("value", "深水路行驶中");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "深水路行驶后");
		map.put("value", "深水路行驶后");
		list.add(map);
		
		return list;
	}
	
	
	/**
	 * 空调初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> airConditionStatusInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "ON");
		map.put("value", "ON");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "OFF");
		map.put("value", "OFF");
		list.add(map);
		
		return list;
	}
	
	
	/**
	 * 平时使用状况初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> usedInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "冷机启动后就起步");
		map.put("value", "冷机启动后就起步");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "暖机后起步");
		map.put("value", "暖机后起步");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "经常短距离行驶");
		map.put("value", "经常短距离行驶");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "连续高速行驶");
		map.put("value", "连续高速行驶");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "无不必要的空闲");
		map.put("value", "无不必要的空闲");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "仅夜间使用");
		map.put("value", "仅夜间使用");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "长时间放置");
		map.put("value", "长时间放置");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "白天开灯行驶");
		map.put("value", "白天开灯行驶");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 重要度判定初始化
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String,Object>> importantLevelInit(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "A");
		map.put("value", "A");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "B");
		map.put("value", "B");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "C");
		map.put("value", "C");
		list.add(map);
		
		return list;
	}
}
