package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptMgrDao;
import com.infodms.dms.dao.customerRelationships.IncomingAlertScreenDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmComplaintDealRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.po.TtCrmSeatsPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * 咨询投诉处理（坐席）ACTIONS
 * @ClassName     : ComplaintAccept 
 * @Description   : 投诉受理
 * @author        : pengbo
 * CreateDate     : 2017-7-10
 */
public class ComplaintAcceptZx {
	private static Logger logger = Logger.getLogger(ComplaintAcceptZx.class);
	//投诉咨询处理初始化页面  
	private final String ComplaintAcceptQueryUrl = "/jsp/customerRelationships/complaintConsult/complaintAcceptZxQuery.jsp";
	//投诉咨询新增/修改页面
	private final String ComplaintAcceptUrl = "/jsp/customerRelationships/complaintConsult/complaintAcceptZxUpdate.jsp";
	//咨询处理页面
	private final String waitConsultZxUrl = "/jsp/customerRelationships/complaintConsult/waitConsultZx.jsp";
	//客户查询界面
	private final String SelectCustom = "/jsp/customerRelationships/complaintConsult/selectCustomer.jsp";
	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	private ComplaintAcceptMgrDao dao = ComplaintAcceptMgrDao.getInstance();
	
	
	//投诉受理初始化
	public void getComplaintAcceptZxInit(){
		try{
			act.setForword(ComplaintAcceptQueryUrl);
			}catch(Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"咨询投诉处理初始化");
				logger.error(logger,e1);
				act.setException(e1);
			}
		}
	
 	/**
	* 投诉咨询分派（坐席）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void complaintDealerSetInit(){
		act.getResponse().setContentType("application/json");
		try{			 				
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			String CuRa = CommonUtils.checkNull(request.getParamValue("CuRa"));//客户评分
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));
			String dealStatus=String.valueOf(Constant.VOUCHER_STATUS_03);
			//更新投诉咨询相关内容
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			if(!"".equals(CuRa))ttCrmComplaintPO2.setCpCustomerRating(Integer.parseInt(CuRa));
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)dao.select(ttCrmComplaintPO1).get(0);
			
			//插入投诉咨询回复记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			String cdId=SequenceManager.getSequence("");
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(cdId));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			if("".equals(ccont)){
				ccont=logonUser.getName()+"   坐席分派！";
			}
			ttCrmComplaintDealRecordPO.setCpContent(ccont);
			ttCrmComplaintPO2.setCpStatus(Integer.parseInt(dealStatus));
			ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_03);
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
			}catch(Exception e){
					BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉咨询分派（坐席）");
					logger.error(logger,e1);
					act.setException(e1);
				}
			}
	//投诉处理新增单据
	public void complaintAcceptInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));
			//业务类型
			act.setOutData("bizTypeList", getBizType(flag));
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			
			//车系
			act.setOutData("seriesList", commonUtilActions.getAllSeries());
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			//里程范围
			act.setOutData("mileageRangeList", commonUtilActions.getTcCode(Constant.MILEAGE_RANGE.toString()));
			//车辆用途
			act.setOutData("faultPartList", commonUtilActions.getTcCode(Constant.VEHICLE_USE));
			//车辆性质
			act.setOutData("vinUseList", commonUtilActions.getTcCode(Constant.SERVICEACTIVITY_CHARACTOR.toString()));
			//投诉对象
			act.setOutData("cpObjectList", commonUtilActions.getOfficeOrg());
			//act.setOutData("cpObjectList", commonUtilActions.getVehicleCompany());
			//处理方式
			act.setOutData("dealModelList", commonUtilActions.getTcCode(Constant.COMPLAINT_PROCESS.toString()));
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			//管理员所在部门
//			act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
			act.setOutData("seatAdminDepartList", "");
			
			act.setForword(ComplaintAcceptUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉受理");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	
	//单据保存
	public void saveComplaintAccept(){
		act.getResponse().setContentType("application/json");
		try{
			String cpId = CommonUtils.checkNull(request.getParamValue("cpId"));  //客户名称
			String ctmname = CommonUtils.checkNull(request.getParamValue("ctmname"));  //客户名称			
			String tele = CommonUtils.checkNull(request.getParamValue("tele")); //客户电话
			String concatPerson = CommonUtils.checkNull(request.getParamValue("concatPerson")); //联系人 
			String concatPhone = CommonUtils.checkNull(request.getParamValue("concatPhone"));  //联系电话
			String vin = CommonUtils.checkNull(request.getParamValue("vinStr"));  	//vin			
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));  	//行驶里程			
			String mileageRange = CommonUtils.checkNull(request.getParamValue("mileageRange"));//里程范围
			String faultPart = CommonUtils.checkNull(request.getParamValue("faultPart"));//车辆用途
			String vinuse = CommonUtils.checkNull(request.getParamValue("vinuse"));  //车辆性质
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));//省 				
			String citysel = CommonUtils.checkNull(request.getParamValue("citysel")); //市 								
			String bizType = CommonUtils.checkNull(request.getParamValue("bizType"));  	//业务类型			
			String contType = CommonUtils.checkNull(request.getParamValue("contType"));  //内容类型				
			String cplevel = CommonUtils.checkNull(request.getParamValue("cplevel"));  	//复杂度状态			
			String cplimit = CommonUtils.checkNull(request.getParamValue("cplimit"));  //规定处理期限
			String cpObject1 = CommonUtils.checkNull(request.getParamValue("cpObject")); //投诉对象
			String vcPro = CommonUtils.checkNull(request.getParamValue("vcPro")); //投诉所属大区
			String myCity = CommonUtils.checkNull(request.getParamValue("myCity")); //投诉对象所属小区
			String vehicleCompany = CommonUtils.checkNull(request.getParamValue("vehicleCompany")); //投诉经销商 
 			String complaintContent = CommonUtils.checkNull(request.getParamValue("complaintContent")); //投诉咨询内容 	
			String contentTypeRemark = CommonUtils.checkNull(request.getParamValue("contentTypeRemark")); //备注 	
			String hasCarCustomer = CommonUtils.checkNull(request.getParamValue("hasCarCustomer")); //0、有车	1、无车
			String ser = CommonUtils.checkNull(request.getParamValue("ser"));       // 车系			
			String model = CommonUtils.checkNull(request.getParamValue("model"));  	// 车型			
			String pudate = CommonUtils.checkNull(request.getParamValue("pudate")); // 购车时间			
			String pdate = CommonUtils.checkNull(request.getParamValue("pdate"));   // 生产时间
			
			
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			IncomingAlertScreenDao dao = IncomingAlertScreenDao.getInstance();
			List<Map<String, Object>> orgDepart = commonUtilDao.getUserOrgForDepart(logonUser.getUserId());	
			// 艾春9.24添加 无车架号投诉
			Long cpCusId = null;
			Long cpSerriesId = null;
			Long cpModelId = null;
			Date cpProductDate = null;
			Date cpBuyDate = null;
			String ccton=logonUser.getName()+"    受理";
			//如果是有车客户验证vin
			if("0".equals(hasCarCustomer)) {
				if(bizType.equals(Constant.TYPE_COMPLAIN)){
					Map<String,Object> queryVinData = dao.queryVin(vin);
					if(queryVinData == null || queryVinData.size()==0){
						act.setOutData("isSuccess", 1);
						return;
					}
				}
			}
			Map<String,Object> queryCustomerInforData = null;
			//无车架号投诉
			if(!"".equals(vin)) {
				queryCustomerInforData = dao.queryCustomerInfor(vin);
			}
			//存在底盘号则录入
			if((queryCustomerInforData!=null && queryCustomerInforData.size()>0) || "1".equals(hasCarCustomer)){
				if(queryCustomerInforData != null) {
					if(queryCustomerInforData.get("CTMID") != null) cpCusId = ((BigDecimal)queryCustomerInforData.get("CTMID")).longValue();
					if(queryCustomerInforData.get("SERIESID") != null) cpSerriesId = ((BigDecimal)queryCustomerInforData.get("SERIESID")).longValue();
					if(queryCustomerInforData.get("MODELID") != null) cpModelId = ((BigDecimal)queryCustomerInforData.get("MODELID")).longValue();
					if(queryCustomerInforData.get("PDATE") != null) cpProductDate = DateUtil.str2Date((String)queryCustomerInforData.get("PDATE"), "-");
					if(queryCustomerInforData.get("PUDATE") != null) cpBuyDate = DateUtil.str2Date((String)queryCustomerInforData.get("PUDATE"), "-");
				}
				
				ComplaintAcceptDao cadao = ComplaintAcceptDao.getInstance();
				
				TtCrmComplaintPO tccp = new TtCrmComplaintPO();
				
				
				if(null != cpCusId)       tccp.setCpCusId(cpCusId); //设置客户ID
				if(null != cpSerriesId)   tccp.setCpSeriesId(cpSerriesId); // 设置车系ID
				if(null != cpModelId)     tccp.setCpModelId(cpModelId); // 设置车型ID
				if(null != cpProductDate) tccp.setCpProductDate(cpProductDate); // 设置生产时间
				if(null != cpBuyDate)     tccp.setCpBuyDate(cpBuyDate); // 设置购买时间
				
				tccp.setCpPhone(tele);
				tccp.setCpName(ctmname);
				tccp.setCpVin(vin);
				if(!"".equals(mileage)) tccp.setCpMileage(Long.parseLong(mileage)); // 艾春 9.24 修改 里程可以为空
				
				if(!"".equals(mileageRange))tccp.setCpMileageRange(Integer.parseInt(mileageRange));
				if(!"".equals(faultPart))tccp.setCpVinUse(Integer.parseInt(faultPart));
				if(!"".equals(vinuse))tccp.setCpVinNature(Integer.parseInt(vinuse));
				if(!"".equals(pro))tccp.setCpProvinceId(Long.parseLong(pro));
				if(!"".equals(citysel))tccp.setCpCityId(Long.parseLong(citysel));
				if(!"".equals(concatPerson))tccp.setCpConcatPerson(concatPerson);
				if(!"".equals(concatPhone))tccp.setCpConcatPhone(concatPhone);

				tccp.setCpBizType(Integer.parseInt(bizType));
				if(orgDepart!=null && orgDepart.size()>0)  
					tccp.setCpSourceCustom(Long.parseLong(orgDepart.get(0).get("ORGID").toString()));
				//投诉咨询处理记录
				TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
				ttCrmComplaintDealRecordPO.setCdDate(new Date());
				ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
				ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
				ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
				ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
				ttCrmComplaintDealRecordPO.setCreateDate(new Date());
				if(bizType.equals(String.valueOf(Constant.VOUCHER_TYPE_01))){
					//投诉
					if(!"".equals(cplevel))tccp.setCpLevel(Integer.parseInt(cplevel));
					if(!"".equals(cplimit)){
						tccp.setCpLimit(commonUtilActions.turnCodeToDayForComplaintLimit(cplimit));
					}
					if(!"".equals(vcPro))tccp.setCpObjectOrg(Long.parseLong(vcPro));
					if(!"".equals(myCity))tccp.setCpObjectSmallOrg(Long.parseLong(myCity));
					if(!"".equals(vehicleCompany))tccp.setCpObject(Long.parseLong(vehicleCompany));
					if(!"".equals(cpObject1))tccp.setCpObject(Long.parseLong(cpObject1));
					//待处理
					tccp.setCpStatus(Constant.VOUCHER_STATUS_01);
					ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_01);
					
					tccp.setCpNo(Utility.GetBillNo("", "", "TS"));
				} else if(bizType.equals(String.valueOf(Constant.VOUCHER_TYPE_02))){
					//咨询
					tccp.setCpNo(Utility.GetBillNo("", "", "ZX"));
					//设置版本号
					tccp.setVar(0);
					if(!"".equals(cpId)){
						tccp.setCpLevel(null);
						tccp.setCpLimit(null);
						tccp.setCpObjectOrg(null);
						tccp.setCpObjectSmallOrg(null);
						tccp.setCpObject(null);
						tccp.setCpObject(null);
					}
					//待处理
					tccp.setCpStatus(Constant.VOUCHER_STATUS_01);
					tccp.setCpDealUser(logonUser.getUserId());
					tccp.setCpDealUserName(logonUser.getName());
					ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_01);
				}
				if(!"".equals(contType))tccp.setCpBizContent(Integer.parseInt(contType));
				//内容类型备注
				if(!"".equals(contentTypeRemark)) tccp.setCpContentTypeRemark(contentTypeRemark);
				if(!"".equals(hasCarCustomer)) tccp.setCpHasCarCustomer(Integer.parseInt(hasCarCustomer));
				
				tccp.setCpContent(complaintContent);

				tccp.setCpAccUser(logonUser.getUserId());
				tccp.setCpAccDate(new Date());
				
				tccp.setCreateBy(logonUser.getUserId());
				tccp.setCreateDate(new Date());
				if("".equals(cpId)){
					String cpid = SequenceManager.getSequence("");
					tccp.setCpId(Long.parseLong(cpid));
					ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
					ttCrmComplaintDealRecordPO.setCpContent(ccton);
					cadao.insert(tccp);
					cadao.insert(ttCrmComplaintDealRecordPO);
				}else{
					TtCrmComplaintPO otccp = new TtCrmComplaintPO();
					otccp.setCpId(Long.valueOf(cpId));
					cadao.update(otccp, tccp);
				}
				act.setOutData("isSuccess", 0);
			}else{
				if(null != ser && !"".equals(ser)) cpSerriesId = Long.parseLong(ser);                   // 处理车系
				if(null != model && !"".equals(model)) cpModelId = Long.parseLong(model);                 // 处理车型
				if(null != pdate && !"".equals(pdate)) cpProductDate = DateUtil.str2Date(pdate, "-");   // 处理生产时间  
				if(null != pudate && !"".equals(pudate)) cpBuyDate = DateUtil.str2Date(pudate, "-");     // 处理购买时间
				
				ComplaintAcceptDao cadao = ComplaintAcceptDao.getInstance();
				
				TtCrmComplaintPO tccp = new TtCrmComplaintPO();
				tccp.setCpPhone(tele);
				tccp.setCpName(ctmname);
				tccp.setCpVin(vin);
				if(!"".equals(mileage)) tccp.setCpMileage(Long.parseLong(mileage));
				if(!"".equals(mileageRange))tccp.setCpMileageRange(Integer.parseInt(mileageRange));
				if(!"".equals(faultPart))tccp.setCpVinUse(Integer.parseInt(faultPart));
				if(!"".equals(vinuse))tccp.setCpVinNature(Integer.parseInt(vinuse));
				if(!"".equals(pro))tccp.setCpProvinceId(Long.parseLong(pro));
				if(!"".equals(citysel))tccp.setCpCityId(Long.parseLong(citysel));
				if(null != cpSerriesId)   tccp.setCpSeriesId(cpSerriesId); // 设置车系ID
				if(null != cpModelId)     tccp.setCpModelId(cpModelId); // 设置车型ID
				if(null != cpProductDate) tccp.setCpProductDate(cpProductDate); // 设置生产时间
				if(null != cpBuyDate)     tccp.setCpBuyDate(cpBuyDate); // 设置购买时间
				
				tccp.setCpBizType(Integer.parseInt(bizType));
				if(orgDepart!=null && orgDepart.size()>0)  
					tccp.setCpSourceCustom(Long.parseLong(orgDepart.get(0).get("ORGID").toString()));
				//投诉咨询处理记录
				TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
				ttCrmComplaintDealRecordPO.setCdDate(new Date());
				ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
				ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
				ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
				ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
				ttCrmComplaintDealRecordPO.setCreateDate(new Date());
				if(bizType.equals(String.valueOf(Constant.VOUCHER_TYPE_01))){
					//投诉
					tccp.setCpNo(Utility.GetBillNo("", "", "TS"));
					if(!"".equals(cplevel))tccp.setCpLevel(Integer.parseInt(cplevel));
					if(!"".equals(cplimit)){
						tccp.setCpLimit(commonUtilActions.turnCodeToDayForComplaintLimit(cplimit));
					}
					if(!"".equals(vcPro))tccp.setCpObjectOrg(Long.parseLong(vcPro));
					if(!"".equals(myCity))tccp.setCpObjectSmallOrg(Long.parseLong(myCity));
					if(!"".equals(vehicleCompany))tccp.setCpObject(Long.parseLong(vehicleCompany));
					if(!"".equals(cpObject1))tccp.setCpObject(Long.parseLong(cpObject1));
					//待处理
					tccp.setCpStatus(Constant.VOUCHER_STATUS_01);
					ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_01);
					
				}else if(bizType.equals(String.valueOf(Constant.VOUCHER_TYPE_02))){
					//咨询
					tccp.setCpNo(Utility.GetBillNo("", "", "ZX"));
					//设置版本号
					tccp.setVar(0);
					if(!"".equals(cpId)){
						tccp.setCpLevel(null);
						tccp.setCpLimit(null);
						tccp.setCpObjectOrg(null);
						tccp.setCpObjectSmallOrg(null);
						tccp.setCpObject(null);
						tccp.setCpObject(null);
					}
					//待处理
					tccp.setCpStatus(Constant.VOUCHER_STATUS_01);
					tccp.setCpDealUser(logonUser.getUserId());
					tccp.setCpDealUserName(logonUser.getName());
					ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_01);
				}
				
				//内容类型备注
				if(!"".equals(contentTypeRemark)) tccp.setCpContentTypeRemark(contentTypeRemark);
				if(!"".equals(contType))tccp.setCpBizContent(Integer.parseInt(contType));
				if(!"".equals(hasCarCustomer)) tccp.setCpHasCarCustomer(Integer.parseInt(hasCarCustomer));
				
				tccp.setCpContent(complaintContent);
				tccp.setCpAccUser(logonUser.getUserId());
				tccp.setCpAccDate(new Date());
				tccp.setCreateBy(logonUser.getUserId());
				tccp.setCreateDate(new Date());
				if("".equals(cpId)){
					String cpid = SequenceManager.getSequence("");
					tccp.setCpId(Long.parseLong(cpid));
					ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
					ttCrmComplaintDealRecordPO.setCpContent(ccton);
					cadao.insert(tccp);
					cadao.insert(ttCrmComplaintDealRecordPO);
				}else{
					TtCrmComplaintPO otccp = new TtCrmComplaintPO();
					otccp.setCpId(Long.valueOf(cpId));
					cadao.update(otccp, tccp);
				}
				act.setOutData("isSuccess", 0);
			}
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"投诉单信息");
			logger.error(logonUser,e1);
			act.setException(e1);
			act.setOutData("isSuccess", 2);
		}
	}
	
	/**
	 * 待处理查询（坐席）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void waitComplaintUpdate(){
		try{	
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  		
			String pageId = CommonUtils.checkNull(request.getParamValue("pageId")); 
			String openPage = CommonUtils.checkNull(request.getParamValue("openPage"));  	
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//车系
			act.setOutData("seriesList", commonUtilActions.getAllSeries());
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			//里程范围
			act.setOutData("mileageRangeList", commonUtilActions.getTcCode(Constant.MILEAGE_RANGE.toString()));
			//车辆用途
			act.setOutData("faultPartList", commonUtilActions.getTcCode(Constant.SALES_ADDRESS));
			//车辆性质
			act.setOutData("vinUseList", commonUtilActions.getTcCode(Constant.SERVICEACTIVITY_CHARACTOR.toString()));
			//业务类型
			act.setOutData("bizTypeList", getBizType("3"));
			//城市
		 	act.setOutData("cityList", commonUtilActions.getAllCity());
			//投诉对象
			act.setOutData("cpObjectList", commonUtilActions.getOfficeOrg());
			//处理方式
			act.setOutData("dealModelList", commonUtilActions.getTcCode(Constant.COMPLAINT_PROCESS.toString()));
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			//管理员所在部门
//			act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
			act.setOutData("seatAdminDepartList", "");
			Map<String,Object> complaintAcceptMap = dao.queryComplaintInformation(Long.parseLong(cpid));		 
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			act.setOutData("pageId", pageId);
			act.setOutData("openPage", openPage);
			act.setForword(ComplaintAcceptUrl);			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 处理咨询初始化跳转（坐席）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void doComplaintZX(){
		try{	
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  
			//标识哪个页面的提交处理
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			if("0".equals(id)){
				id=String.valueOf(Constant.VOUCHER_STATUS_02);
			}else{
				id=String.valueOf(Constant.VOUCHER_STATUS_03);
			}
			Map<String,Object> complaintAcceptMap = dao.queryComplaintInfor(Long.parseLong(cpid));	
			Map<String,Object> map = dao.queryComplaintRecord(Long.parseLong(cpid),id);
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			act.setOutData("dealRecordList", dao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("map", map);
			act.setForword(waitConsultZxUrl);			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"处理咨询初始化跳转（坐席）");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 咨询处理
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void adviceUpdateSubmit(){
		act.getResponse().setContentType("application/json");
		try{			 				
			String dealStatus = CommonUtils.checkNull(request.getParamValue("dealStatus")); //处理状态 				
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont")); 	
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			//标识哪个页面的提交处理
			int dealStatus1;
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			if("0".equals(dealStatus)){
				if("0".equals(id)) dealStatus=String.valueOf(Constant.VOUCHER_STATUS_02);
				dealStatus1=Constant.DISPOSE_STATUS_06;
			}else{
				dealStatus=String.valueOf(Constant.VOUCHER_STATUS_06);
				dealStatus1=Constant.DISPOSE_STATUS_02;
			}
			//更新投诉咨询相关内容
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)dao.select(ttCrmComplaintPO1).get(0);
			
			//插入投诉咨询回复记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			String cdId=SequenceManager.getSequence("");
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(cdId));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			
			ttCrmComplaintPO2.setCpStatus(Integer.parseInt(dealStatus));
			ttCrmComplaintDealRecordPO.setCpStatus(dealStatus1);
			ttCrmComplaintDealRecordPO.setCpContent(ccont);
			ttCrmComplaintPO2.setCpDealId(logonUser.getUserId());
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			ttCrmComplaintPO2.setCpDealId(logonUser.getUserId());
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//业务类型
	private List<Map<String,Object>> getBizType(String flag){
		List<Map<String,Object>> bizTypeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		if("1".equals(flag)){
			map.put("id", Constant.VOUCHER_TYPE_01);
			map.put("name", Constant.VOUCHER_COMPLAIN_NAME);
			bizTypeList.add(map);
		}else{
			map.put("id", Constant.VOUCHER_TYPE_01);
			map.put("name", Constant.VOUCHER_COMPLAIN_NAME);
			bizTypeList.add(map);
			map = new HashMap<String, Object>();
			map.put("id", Constant.VOUCHER_TYPE_02);
			map.put("name", Constant.VOUCHER_CONSULT_NAME);
			bizTypeList.add(map);
		}
		return bizTypeList;
	}
	//跳转到查看客户的页面
	public void showCustomList(){
		try{
			act.setForword(SelectCustom);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉受理");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
 	/**
	* 投诉咨询关闭（坐席）
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void complaintDealerCloseInit(){
		act.getResponse().setContentType("application/json");
		try{			 				
			String cpid = CommonUtils.checkNull(request.getParamValue("cpId"));
			String CuRa = CommonUtils.checkNull(request.getParamValue("CuRa"));//客户评分
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));
			String dealStatus=String.valueOf(Constant.VOUCHER_STATUS_08);
			//更新投诉咨询相关内容
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			if("".equals(CuRa)){
				CuRa=Constant.PLEASED;
			}
			ttCrmComplaintPO2.setCpCustomerRating(Integer.parseInt(CuRa));
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)dao.select(ttCrmComplaintPO1).get(0);
			
			//插入投诉咨询回复记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			String cdId=SequenceManager.getSequence("");
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(cdId));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			if("".equals(ccont)){
				ccont=logonUser.getName()+"   坐席关闭！";
			}
			ttCrmComplaintDealRecordPO.setCpContent(ccont);
			ttCrmComplaintPO2.setCpStatus(Integer.parseInt(dealStatus));
			ttCrmComplaintDealRecordPO.setCpStatus(Constant.DISPOSE_STATUS_05);
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
			}catch(Exception e){
					BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉咨询分派（坐席）");
					logger.error(logger,e1);
					act.setException(e1);
				}
			}
	

}
