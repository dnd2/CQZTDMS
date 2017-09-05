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
 * 投诉受理ACTIONS
 * @ClassName     : ComplaintAccept 
 * @Description   : 投诉受理
 * @author        : wangming
 * CreateDate     : 2013-4-17
 */
public class ComplaintAccept {
	private static Logger logger = Logger.getLogger(ComplaintAccept.class);
	//投诉受理页面
	private final String ComplaintAcceptUrl = "/jsp/customerRelationships/complaintConsult/complaintAccept.jsp";
	//客户查询界面
	private final String SelectCustom = "/jsp/customerRelationships/complaintConsult/selectCustomer.jsp";
	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	//投诉受理初始化
	public void complaintAcceptInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//城市
			//act.setOutData("cityList", commonUtilActions.getAllCity());
			//车种
			act.setOutData("seriesList", commonUtilActions.getAllSeries());
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			//里程范围
			act.setOutData("mileageRangeList", commonUtilActions.getTcCode(Constant.MILEAGE_RANGE.toString()));
			//故障部件
			act.setOutData("faultPartList", commonUtilActions.getTcCode(Constant.FAULT_PART.toString()));	
			//车辆用途
			act.setOutData("vinUseList", commonUtilActions.getTcCode(Constant.SALES_ADDRESS));
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			//抱怨对象
			act.setOutData("cpObjectList", commonUtilActions.getOfficeOrg());
			//act.setOutData("cpObjectList", commonUtilActions.getVehicleCompany());
			//处理方式
			act.setOutData("dealModelList", commonUtilActions.getTcCode(Constant.COMPLAINT_PROCESS.toString()));
			//大区
			//act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			//管理员所在部门
			act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
			
			
			act.setForword(ComplaintAcceptUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉受理");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void complaintAcceptInitPort(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//城市
		 	act.setOutData("cityList", commonUtilActions.getAllCity());
			//车种
			act.setOutData("seriesList", commonUtilActions.getAllSeries());
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			//里程范围
			act.setOutData("mileageRangeList", commonUtilActions.getTcCode(Constant.MILEAGE_RANGE.toString()));
			//故障部件
			act.setOutData("faultPartList", commonUtilActions.getTcCode(Constant.FAULT_PART.toString()));	
			//车辆用途
			act.setOutData("vinUseList", commonUtilActions.getTcCode(Constant.SALES_ADDRESS));
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			//抱怨对象
			act.setOutData("cpObjectList", commonUtilActions.getOfficeOrg());
			//act.setOutData("cpObjectList", commonUtilActions.getVehicleCompany());
			//处理方式
			act.setOutData("dealModelList", commonUtilActions.getTcCode(Constant.COMPLAINT_PROCESS.toString()));
			//大区
			//act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			//管理员所在部门
			act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepart());
			
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid")); //客户ID
			String orderid = CommonUtils.checkNull(request.getParamValue("orderid"));     //所属地区

				
			IncomingAlertScreenDao dao = IncomingAlertScreenDao.getInstance();
	
			Map<String,Object> queryCustomerInforData = dao.queryCustomerInfor(ctmid,orderid);
			
			act.setOutData("queryCustomerInforData", queryCustomerInforData);
			
			
			act.setForword(ComplaintAcceptUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉受理");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void saveComplaintAccept(){
		act.getResponse().setContentType("application/json");
		try{
			String ctmname = CommonUtils.checkNull(request.getParamValue("ctmname"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String vin = CommonUtils.checkNull(request.getParamValue("vinStr"));  				
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));  				
			String mileageRange = CommonUtils.checkNull(request.getParamValue("mileageRange"));
			String faultPart = CommonUtils.checkNull(request.getParamValue("faultPart"));
			String vinuse = CommonUtils.checkNull(request.getParamValue("vinuse"));  				
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String citysel = CommonUtils.checkNull(request.getParamValue("citysel"));  								
				
			String bizType = CommonUtils.checkNull(request.getParamValue("bizType"));  				
			String contType = CommonUtils.checkNull(request.getParamValue("contType"));  				
			String cplevel = CommonUtils.checkNull(request.getParamValue("cplevel"));  				
			String cplimit = CommonUtils.checkNull(request.getParamValue("cplimit"));  
			
			String cpObject1 = CommonUtils.checkNull(request.getParamValue("cpObject")); //报怨处
			String cpObject = ""; //报怨所属大区
			String vcPro = CommonUtils.checkNull(request.getParamValue("vcPro")); //报怨所属小区
			String vehicleCompany = CommonUtils.checkNull(request.getParamValue("vehicleCompany")); //报怨经销商 
 				
			String complaintContent = CommonUtils.checkNull(request.getParamValue("complaintContent"));  				
			String dealModel = CommonUtils.checkNull(request.getParamValue("dealModel"));  
			
			String rcont = CommonUtils.checkNull(request.getParamValue("rcont"));  	
			//String dealuser = CommonUtils.checkNull(request.getParamValue("dealuser"));  	
			String dealorg = CommonUtils.checkNull(request.getParamValue("orgObj"));
			
			String ser = CommonUtils.checkNull(request.getParamValue("ser"));       // 车系			
			String model = CommonUtils.checkNull(request.getParamValue("model"));  	// 车型			
			String pudate = CommonUtils.checkNull(request.getParamValue("pudate")); // 购车时间			
			String pdate = CommonUtils.checkNull(request.getParamValue("pdate"));   // 生产时间
			
			//wizard_lee 增加来电登记 2014.04.14
			String complaintStatus = request.getParamValue("complaint_status");
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
			if(bizType.equals(Constant.TYPE_COMPLAIN)){
				Map<String,Object> queryVinData = dao.queryVin(vin);
				if(queryVinData == null || queryVinData.size()==0){
					act.setOutData("isSuccess", 1);
					return;
				}
			}
			
			
			Map<String,Object> queryCustomerInforData = dao.queryCustomerInfor(vin);
			//存在底盘号则录入
			if(queryCustomerInforData!=null && queryCustomerInforData.size()>0){
				if(queryCustomerInforData.get("CTMID") != null) cpCusId = ((BigDecimal)queryCustomerInforData.get("CTMID")).longValue();
				if(queryCustomerInforData.get("SERIESID") != null) cpSerriesId = ((BigDecimal)queryCustomerInforData.get("SERIESID")).longValue();
				if(queryCustomerInforData.get("MODELID") != null) cpModelId = ((BigDecimal)queryCustomerInforData.get("MODELID")).longValue();
				if(queryCustomerInforData.get("PDATE") != null) cpProductDate = DateUtil.str2Date((String)queryCustomerInforData.get("PDATE"), "-");
				if(queryCustomerInforData.get("PUDATE") != null) cpBuyDate = DateUtil.str2Date((String)queryCustomerInforData.get("PUDATE"), "-");
				
				ComplaintAcceptDao cadao = ComplaintAcceptDao.getInstance();
				
				TtCrmComplaintPO tccp = new TtCrmComplaintPO();
				String cpid = SequenceManager.getSequence("");
				tccp.setCpId(Long.parseLong(cpid));
				if(null != cpCusId)       tccp.setCpCusId(cpCusId); //设置客户ID
				if(null != cpSerriesId)   tccp.setCpSeriesId(cpSerriesId); // 设置车系ID
				if(null != cpModelId)     tccp.setCpModelId(cpModelId); // 设置车型ID
				if(null != cpProductDate) tccp.setCpProductDate(cpProductDate); // 设置生产时间
				if(null != cpBuyDate)     tccp.setCpBuyDate(cpBuyDate); // 设置购买时间
				
				tccp.setCpPhone(tele);
				tccp.setCpName(ctmname);
				tccp.setCpVin(vin);
				if(!"".equals(mileage)) tccp.setCpMileage(Long.parseLong(mileage)); // 艾春 9.24 修改 里程可以为空
//				if(queryCustomerInforData.get("MILEAGE") != null)tccp.setCpMileage(((BigDecimal)queryCustomerInforData.get("MILEAGE")).longValue());
				
				if(!"".equals(mileageRange))tccp.setCpMileageRange(Integer.parseInt(mileageRange));
				if(!"".equals(faultPart))tccp.setFaultPart(Integer.parseInt(faultPart));
				if(!"".equals(vinuse))tccp.setCpVinUse(Integer.parseInt(vinuse));
				if(!"".equals(pro))tccp.setCpProvinceId(Long.parseLong(pro));
				if(!"".equals(citysel))tccp.setCpCityId(Long.parseLong(citysel));

				tccp.setCpBizType(Integer.parseInt(bizType));
				if(orgDepart!=null && orgDepart.size()>0)  
					tccp.setCpSourceCustom(Long.parseLong(orgDepart.get(0).get("ORGID").toString()));
				//投诉咨询处理记录
				TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
				ttCrmComplaintDealRecordPO.setCdDate(new Date());
				ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
				ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
				ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
				ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
				ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
				ttCrmComplaintDealRecordPO.setCreateDate(new Date());
				//ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(dealorg));
				if(bizType.equals(Constant.TYPE_COMPLAIN)){
					//投诉
					tccp.setCpNo(Utility.GetBillNo("", "D", "TS"));
					if(dealModel.equals(Constant.COMPLAINT_PROCESS_TURN.toString())){
						if(complaintStatus==null||"".equals(complaintStatus)){
							complaintStatus = Constant.COMPLAINT_STATUS_WAIT;
						}
						tccp.setCpStatus(Integer.parseInt(complaintStatus));
						tccp.setCpDealOrg(Long.parseLong(dealorg));
						tccp.setCpDealUser(Long.parseLong(dealorg));
						tccp.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(dealorg)));
						ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT));
					}
				}else if(bizType.equals(Constant.TYPE_CONSULT)){
					//咨询
					tccp.setCpNo(Utility.GetBillNo("", "D", "ZX"));
					//设置版本号
					tccp.setVar(0);
					//现场处理
					if(dealModel.equals(Constant.CONSULT_PROCESS_SPOT.toString())){
						tccp.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
						tccp.setCpDealUser(logonUser.getUserId());
						tccp.setCpDealUserName(logonUser.getName());
						ttCrmComplaintDealRecordPO.setCpContent(rcont);
						ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
					//待处理
					}else if(dealModel.equals(Constant.CONSULT_PROCESS_WAIT.toString())){
						tccp.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
						tccp.setCpDealUser(logonUser.getUserId());
						tccp.setCpDealUserName(logonUser.getName());
						ttCrmComplaintDealRecordPO.setCpContent(rcont);
						ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
					}

				}
				if(!"".equals(contType))tccp.setCpBizContent(Integer.parseInt(contType));
				if(!"".equals(cplevel))tccp.setCpLevel(Integer.parseInt(cplevel));
				if(!"".equals(cplimit)){
					tccp.setCpLimit(commonUtilActions.turnCodeToDayForComplaintLimit(cplimit));
				}
				
				
				if(!"".equals(vcPro))tccp.setCpObjectSmallOrg(Long.parseLong(vcPro));
				if(!"".equals(vehicleCompany))tccp.setCpObject(Long.parseLong(vehicleCompany));
				if(!"".equals(cpObject1))tccp.setCpObject(Long.parseLong(cpObject1));
				
				if(!"".equals(vcPro) && !"".equals(vehicleCompany) ){
					cpObject = cadao.queryPid(Long.parseLong(vcPro)).get(0).get("PARENT_ORG_ID").toString();
					if(!"".equals(cpObject))tccp.setCpObjectOrg(Long.parseLong(cpObject));
				}
				
				tccp.setCpContent(complaintContent);
				tccp.setCpDealMode(Integer.parseInt(dealModel));

				tccp.setCpAccUser(logonUser.getUserId());
				tccp.setCpAccDate(new Date());
				
				tccp.setCreateBy(logonUser.getUserId());
				tccp.setCreateDate(new Date());
				
				cadao.insert(tccp);
				cadao.insert(ttCrmComplaintDealRecordPO);
				act.setOutData("isSuccess", 0);
			}else{
					if(null != ser && !"".equals(ser)) cpSerriesId = Long.parseLong(ser);                   // 处理车系
					if(null != model && !"".equals(model)) cpModelId = Long.parseLong(model);                 // 处理车型
					if(null != pdate && !"".equals(pdate)) cpProductDate = DateUtil.str2Date(pdate, "-");   // 处理生产时间  
					if(null != pudate && !"".equals(pudate)) cpBuyDate = DateUtil.str2Date(pudate, "-");     // 处理购买时间
					
					ComplaintAcceptDao cadao = ComplaintAcceptDao.getInstance();
					
					TtCrmComplaintPO tccp = new TtCrmComplaintPO();
					String cpid = SequenceManager.getSequence("");
					tccp.setCpId(Long.parseLong(cpid));
					tccp.setCpPhone(tele);
					tccp.setCpName(ctmname);
					tccp.setCpVin(vin);
					if(!"".equals(mileage)) tccp.setCpMileage(Long.parseLong(mileage));
					if(!"".equals(vcPro)) cpObject = cadao.queryPid(Long.parseLong(vcPro)).get(0).get("PARENT_ORG_ID").toString();
					if(!"".equals(mileageRange))tccp.setCpMileageRange(Integer.parseInt(mileageRange));
					if(!"".equals(faultPart))tccp.setFaultPart(Integer.parseInt(faultPart));
					if(!"".equals(vinuse))tccp.setCpVinUse(Integer.parseInt(vinuse));
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
					ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
					ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
					ttCrmComplaintDealRecordPO.setCreateDate(new Date());
					ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(dealorg));
					if(bizType.equals(Constant.TYPE_COMPLAIN)){
						//投诉
						tccp.setCpNo(Utility.GetBillNo("", "D", "TS"));
						if(dealModel.equals(Constant.COMPLAINT_PROCESS_TURN.toString())){
							if(complaintStatus==null||"".equals(complaintStatus)){
								complaintStatus = Constant.COMPLAINT_STATUS_WAIT;
							}
							tccp.setCpStatus(Integer.parseInt(complaintStatus));
							//tccp.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT));
							tccp.setCpDealOrg(Long.parseLong(dealorg));
							tccp.setCpDealUser(Long.parseLong(dealorg));
							tccp.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(dealorg)));
							ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT));
						}
					}else if(bizType.equals(Constant.TYPE_CONSULT)){
						//咨询
						tccp.setCpNo(Utility.GetBillNo("", "D", "ZX"));
						//设置版本号
						tccp.setVar(0);
						//现场处理
						if(dealModel.equals(Constant.CONSULT_PROCESS_SPOT.toString())){
							tccp.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
							tccp.setCpDealUser(logonUser.getUserId());
							tccp.setCpDealUserName(logonUser.getName());
							ttCrmComplaintDealRecordPO.setCpContent(rcont);
							ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
						//待处理
						}else if(dealModel.equals(Constant.CONSULT_PROCESS_WAIT.toString())){
							tccp.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
							tccp.setCpDealUser(logonUser.getUserId());
							tccp.setCpDealUserName(logonUser.getName());
							ttCrmComplaintDealRecordPO.setCpContent(rcont);
							ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
						}

					}

					if(!"".equals(contType))tccp.setCpBizContent(Integer.parseInt(contType));
					if(!"".equals(cplevel))tccp.setCpLevel(Integer.parseInt(cplevel));
					if(!"".equals(cplimit)){
						tccp.setCpLimit(commonUtilActions.turnCodeToDayForComplaintLimit(cplimit));
					}
					
					if(!"".equals(vcPro))tccp.setCpObjectSmallOrg(Long.parseLong(vcPro));
					if(!"".equals(vehicleCompany))tccp.setCpObject(Long.parseLong(vehicleCompany));
					if(!"".equals(cpObject1))tccp.setCpObject(Long.parseLong(cpObject1));
					
					if(!"".equals(vcPro) && !"".equals(vehicleCompany) ){
						cpObject = cadao.queryPid(Long.parseLong(vcPro)).get(0).get("PARENT_ORG_ID").toString();
						if(!"".equals(cpObject))tccp.setCpObjectOrg(Long.parseLong(cpObject));
					}
					
					
					tccp.setCpContent(complaintContent);
					tccp.setCpDealMode(Integer.parseInt(dealModel));


					
					tccp.setCpAccUser(logonUser.getUserId());
					tccp.setCpAccDate(new Date());
					
					
					tccp.setCreateBy(logonUser.getUserId());
					tccp.setCreateDate(new Date());
					
					cadao.insert(tccp);
					cadao.insert(ttCrmComplaintDealRecordPO);
					act.setOutData("isSuccess", 0);
			}
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"投诉单信息");
			logger.error(logonUser,e1);
			act.setException(e1);
			act.setOutData("isSuccess", 2);
		}
	}
	
	//业务类型
	private List<Map<String,Object>> getBizType(){
		List<Map<String,Object>> bizTypeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", Constant.TYPE_COMPLAIN);
		map.put("name", Constant.TYPE_COMPLAIN_NAME);
		bizTypeList.add(map);
		map = new HashMap<String, Object>();
		map.put("id", Constant.TYPE_CONSULT);
		map.put("name", Constant.TYPE_CONSULT_NAME);
		bizTypeList.add(map);
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
	 * @author KFQ
	 * 2013-9-17 19:32
	 */
	//点击接听，挂断，拨号按钮时，将对应的坐席状态改变
	@SuppressWarnings("unchecked")
	public void updateStatus(){
		try{
			IncomingAlertScreenDao daos = IncomingAlertScreenDao.getInstance();
			String type = request.getParamValue("type");
			TtCrmSeatsPO  s  =  new TtCrmSeatsPO();
			TtCrmSeatsPO  s2  =  new TtCrmSeatsPO();
			s.setSeUserId(logonUser.getUserId());
			if("Pickup".equalsIgnoreCase(type)){//接听
				s2.setSeWorkStatus(Constant.SEAT_CALLING_TYPE);
			}else if("Hangup".equalsIgnoreCase(type)){//挂断
				s2.setSeWorkStatus(Constant.SEAT_FREE_TYPE);
			}else if("PlaceCall".equalsIgnoreCase(type)){//拨号
				s2.setSeWorkStatus(Constant.SEAT_BUSY_TYPE);
			}
			else if("Free".equalsIgnoreCase(type)){//闲
				s2.setSeWorkStatus(Constant.SEAT_FREE_TYPE);
			}
			else if("Busy".equalsIgnoreCase(type)){//忙
				s2.setSeWorkStatus(Constant.SEAT_BUSY_TYPE);
			}
			daos.update(s, s2);
			act.setOutData("type", type);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉受理");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
}
