package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.baseSetting.typeSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmComplaintDealRecordPO;
import com.infodms.dms.po.TtCrmComplaintDelayRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 业务部门投诉处理ACTIONS
 * @ClassName     : ODepartmentComplaintDeal
 * @Description   : 业务部门投诉处理
 * @author        : wangming
 * CreateDate     : 2013-5-14
 */
public class ODepartmentComplaintDeal {
	private static Logger logger = Logger.getLogger(ODepartmentComplaintDeal.class);
	//业务部门投诉处理页面
	private final String ODepartmentComplaintDeal = "/jsp/customerRelationships/complaintConsult/oDepartmentComplaintDeal.jsp";
	private final String ApplayDelay = "/jsp/customerRelationships/complaintConsult/oDepartmentComplaintApplayDelay.jsp";
	private final String ApplayDelay01 = "/jsp/customerRelationships/complaintConsult/oDepartmentComplaintApplayDelay01.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	//业务部门投诉处理初始化
	public void oDepartmentComplaintDealInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			
			//处理特殊添加工单状态
			List<Map<String, Object>> statusList = commonUtilActions.getTcCode(Constant.COMPLAINT_STATUS);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("CODE_ID", "unClose");
			map.put("CODE_DESC", "未关闭");
			statusList.add(map);
			act.setOutData("stautsList", statusList);
			//报怨类型
			typeSet ts = new typeSet();
			act.setOutData("bclist", ts.getTypeSelect(Constant.TYPE_COMPLAIN));
			
			act.setForword(ODepartmentComplaintDeal);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"业务部门投诉查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryODepartmentComplaintDeal(){
		act.getResponse().setContentType("application/json");
		try{			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	
			String dealUser = CommonUtils.checkNull(request.getParamValue("dealUser"));  	
			String accUser = CommonUtils.checkNull(request.getParamValue("accUser"));  	
			String biztype = CommonUtils.checkNull(request.getParamValue("biztype"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));
			String region = CommonUtils.checkNull(request.getParamValue("region"));  	
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String checkStart = CommonUtils.checkNull(request.getParamValue("checkStart"));  	
			String checklEnd = CommonUtils.checkNull(request.getParamValue("checklEnd"));  				
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			
			List<Map<String, Object>> orgDepart = commonUtilDao.getUserOrgForDepart(logonUser.getUserId());	
			List<Map<String, Object>> org = commonUtilDao.getUserOrg(logonUser.getUserId());
			
			String orgid = "";
			for(Map map : orgDepart){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}
			for(Map map : org){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}
			
			
			
			PageResult<Map<String,Object>> complaintAcceptData = dao.queryComplaintInfo(vin,name,tele,level,dealUser,accUser,biztype,
					status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,orgid,
					Constant.COMPLAINT_STATUS_DOING+"','"+Constant.COMPLAINT_STATUS_DOING_REJECT,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", complaintAcceptData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"业务部门投诉查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void applayDelay(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid")); 
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			TtCrmComplaintPO ttCrmComplaintPO = new TtCrmComplaintPO();
			ttCrmComplaintPO.setCpId(Long.parseLong(cpid));
			//查询出咨询投诉表信息 
			List<TtCrmComplaintPO> ttCrmComplaintPOList =  dao.select(ttCrmComplaintPO);
			//延期申请的最小时间，延期申请必须大于该天
			Date endDate = null;
			if(ttCrmComplaintPOList!=null&&ttCrmComplaintPOList.size()>0){
				ttCrmComplaintPO = ttCrmComplaintPOList.get(0);
				//当存在已经延期申请过时取最延期至的时间 否则不存在延期，则算出应该结束的日期
				if(ttCrmComplaintPO.getCpClStatus().toString().equals(Constant.PASS_APPLY.toString()) 
						&& ttCrmComplaintPO.getCpClDate() !=null && ttCrmComplaintPO.getCpClVfDate()!=null){
					endDate = ttCrmComplaintPO.getCpClDate();
				}else{
					endDate = DateTimeUtil.getDateAfter(ttCrmComplaintPO.getCpAccDate(), ttCrmComplaintPO.getCpLimit());
				}
				act.setOutData("endDate", DateTimeUtil.parseDateToDate(endDate));	
			}
			
			act.setOutData("cpid", cpid);
			act.setForword(ApplayDelay);			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"业务部门延期申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//大区延期处理
	public void applayDelay01(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid")); 
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			TtCrmComplaintPO ttCrmComplaintPO = new TtCrmComplaintPO();
			ttCrmComplaintPO.setCpId(Long.parseLong(cpid));
			//查询出咨询投诉表信息 
			List<TtCrmComplaintPO> ttCrmComplaintPOList =  dao.select(ttCrmComplaintPO);
			//延期申请的最小时间，延期申请必须大于该天
			Date endDate = null;
			if(ttCrmComplaintPOList!=null&&ttCrmComplaintPOList.size()>0){
				ttCrmComplaintPO = ttCrmComplaintPOList.get(0);
				//当存在已经延期申请过时取最延期至的时间 否则不存在延期，则算出应该结束的日期
				if(ttCrmComplaintPO.getCpClStatus().toString().equals(Constant.PASS_APPLY.toString()) 
						&& ttCrmComplaintPO.getCpClDate() !=null && ttCrmComplaintPO.getCpClVfDate()!=null){
					endDate = ttCrmComplaintPO.getCpClDate();
				}else{
					endDate = DateTimeUtil.getDateAfter(ttCrmComplaintPO.getCpAccDate(), ttCrmComplaintPO.getCpLimit());
				}
				act.setOutData("endDate", DateTimeUtil.parseDateToDate(endDate));	
			}
			
			//交给处级部门
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			act.setOutData("orgList", commonUtilActions.getOfficeOrg());
			
			act.setOutData("cpid", cpid);
			act.setForword(ApplayDelay01);			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"业务部门延期申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void applayDelaySubmit(){
		try{			
			act.getResponse().setContentType("application/json");
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String delayDate = CommonUtils.checkNull(request.getParamValue("delayDate"));  				
			String reason = CommonUtils.checkNull(request.getParamValue("reason"));  
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			List list = complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid));
			
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)complaintAcceptDao.select(ttCrmComplaintPO1).get(0);
			
			ttCrmComplaintPO2.setCpClStatus(Constant.ALREADY_APPLY);
			//ttCrmComplaintPO2.setCpClUser(logonUser.getUserId());
			//ttCrmComplaintPO2.setCpClDate(DateTimeUtil.stringToDate(delayDate));
			//ttCrmComplaintPO2.setCpClContent(reason);
			ttCrmComplaintPO2.setCpClApplyDate(DateTimeUtil.stringToDate(delayDate));
			if(list == null || list.size() == 0) ttCrmComplaintPO2.setCpClOnceDate(new Date());
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			TtCrmComplaintDelayRecordPO  ttCrmComplaintDelayRecordPO = new TtCrmComplaintDelayRecordPO();
			ttCrmComplaintDelayRecordPO.setClContent(reason);
			ttCrmComplaintDelayRecordPO.setClDate(DateTimeUtil.stringToDate(delayDate));
			ttCrmComplaintDelayRecordPO.setClId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDelayRecordPO.setClUser(logonUser.getName());
			ttCrmComplaintDelayRecordPO.setClUserId(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDelayRecordPO.setClVerifyStatus(Constant.ALREADY_APPLY);
			ttCrmComplaintDelayRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO.setCreateDate(new Date());
			ttCrmComplaintDelayRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDelayRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());
			
			complaintAcceptDao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			complaintAcceptDao.insert(ttCrmComplaintDelayRecordPO);
			act.setOutData("isSuccess", "true");		
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"业务部门延期申请保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	//大区延期处理
	public void applayDelaySubmit01(){
		try{			
			act.getResponse().setContentType("application/json");
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String delayDate = CommonUtils.checkNull(request.getParamValue("delayDate"));  				
			String reason = CommonUtils.checkNull(request.getParamValue("reason"));  
			String cpdefertype = CommonUtils.checkNull(request.getParamValue("cpdefertype"));  
			String orgObj = CommonUtils.checkNull(request.getParamValue("orgObj"));  
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			List list = complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid));
			
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)complaintAcceptDao.select(ttCrmComplaintPO1).get(0);
			
			ttCrmComplaintPO2.setCpClStatus(Constant.ALREADY_APPLY);
			ttCrmComplaintPO2.setCpClTurnOrg(Long.parseLong(orgObj));
			//ttCrmComplaintPO2.setCpClUser(logonUser.getUserId());
			//ttCrmComplaintPO2.setCpClDate(DateTimeUtil.stringToDate(delayDate));
			//ttCrmComplaintPO2.setCpClContent(reason);
			if(cpdefertype.equals(""+Constant.DEFER_TYPE_01))
			{
				ttCrmComplaintPO2.setCpClApplyDate(DateTimeUtil.stringToDate(delayDate));
				ttCrmComplaintPO2.setCpDeferType(Long.parseLong(cpdefertype));
			}else
			{
				ttCrmComplaintPO2.setCpDeferType(Long.parseLong(cpdefertype));
			}
			
			if(list == null || list.size() == 0) ttCrmComplaintPO2.setCpClOnceDate(new Date());
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			TtCrmComplaintDelayRecordPO  ttCrmComplaintDelayRecordPO = new TtCrmComplaintDelayRecordPO();
			ttCrmComplaintDelayRecordPO.setClContent(reason);
			if(cpdefertype.equals(""+Constant.DEFER_TYPE_01))
			{
				ttCrmComplaintDelayRecordPO.setClDate(DateTimeUtil.stringToDate(delayDate));
				ttCrmComplaintDelayRecordPO.setCpDeferType(Long.parseLong(cpdefertype));
			}else
			{
				ttCrmComplaintDelayRecordPO.setCpDeferType(Long.parseLong(cpdefertype));
			}
			ttCrmComplaintDelayRecordPO.setClId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDelayRecordPO.setClUser(logonUser.getName());
			ttCrmComplaintDelayRecordPO.setClUserId(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDelayRecordPO.setClVerifyStatus(Constant.ALREADY_APPLY);
			ttCrmComplaintDelayRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO.setCreateDate(new Date());
			// 艾春 2013.12.2 添加延期申请机构和申请类型
			ttCrmComplaintDelayRecordPO.setApplyOrg(logonUser.getOrgId());
			ttCrmComplaintDelayRecordPO.setApplyType(1);
			// 艾春 2013.12.2 添加延期申请机构和申请类型
			ttCrmComplaintDelayRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDelayRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDelayRecordPO.setCpNextDealId(Long.parseLong(orgObj));
			
			// 艾春 2013.12.2 添加投诉处理明细记录
			//插入投诉咨询回复记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealDealer());
			ttCrmComplaintDealRecordPO.setCpContent(reason);
			ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING_ALREADY)); // 设置为大区已处理
			ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(orgObj)); // 获得下一个处理人
			ttCrmComplaintDealRecordPO.setCdUserProcess(Integer.parseInt(Constant.CONFIRE_WAIT_TYPE)); // 设置为未处理完毕继续处理
			complaintAcceptDao.insert(ttCrmComplaintDealRecordPO);
			// 艾春 2013.12.2 添加投诉处理明细记录
			
			complaintAcceptDao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			complaintAcceptDao.insert(ttCrmComplaintDelayRecordPO);
			act.setOutData("isSuccess", "true");		
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"业务部门延期申请保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	
	
	
	public void oDepartmentComplaintDealDownExcel(){
		try{
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	
			String dealUser = CommonUtils.checkNull(request.getParamValue("dealUser"));  	
			String accUser = CommonUtils.checkNull(request.getParamValue("accUser"));  	
			String biztype = CommonUtils.checkNull(request.getParamValue("biztype"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));
			String region = CommonUtils.checkNull(request.getParamValue("region"));  	
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String checkStart = CommonUtils.checkNull(request.getParamValue("checkStart"));  	
			String checklEnd = CommonUtils.checkNull(request.getParamValue("checklEnd"));  			

			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			List<Map<String, Object>> orgDepart = commonUtilDao.getUserOrgForDepart(logonUser.getUserId());	
			List<Map<String, Object>> org = commonUtilDao.getUserOrg(logonUser.getUserId());
			
			String orgid = "";
			for(Map map : orgDepart){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}
			for(Map map : org){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}	
			List<Map<String, Object>> complaintAcceptData =  dao.queryComplaintInfoList(vin,name,tele,level,dealUser,accUser,biztype,
					status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,orgid,
					Constant.COMPLAINT_STATUS_DOING+"','"+Constant.COMPLAINT_STATUS_DOING_REJECT);
			if(complaintAcceptData!=null && complaintAcceptData.size()>0){
				complaintAcceptDataToExcel(complaintAcceptData);
				act.setOutData("success", "true");
				act.setForword(ODepartmentComplaintDeal);
			}else {
				act.setOutData("noresult", "true");
			}	
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void complaintAcceptDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[37];
		head[0]="延期";
		head[1]="抱怨单编号";
		head[2]="抱怨级别";
		head[3]="抱怨类别";
		head[4]="省市";
		head[5]="地市";
		head[6]="客户姓名";
		head[7]="联系电话";
		head[8]="抱怨内容";
		head[9]="抱怨对象";
		head[10]="抱怨时间";
		head[11]="受理人";
		head[12]="车种";
		head[13]="VIN号";
		head[14]="购车时间";
		head[15]="行驶里程";
		head[16]="里程范围";
		head[17]="处理部门";
		head[18]="处理人";
		head[19]="签收日期";
		head[20]="转出时间";
		head[21]="回访结果";
		head[22]="回访人";
		head[23]="回访日期";
		head[24]="关闭时间";
		head[25]="处理时长(小时)";
		head[26]="规定处理期限(天)";
		head[27]="延期天数(天)";
		head[28]="规定及时关闭时间";
		head[29]="是否及时关闭";
		head[30]="是否一次处理满意";
		head[31]="是否正常关闭";
		head[32]="处理过程";
		head[33]="最终反馈日期";
		head[34]="处理结果";
		head[35]="状态";
		head[36]="当前处理人";
		
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		
		List<Map<String, Object>> cpns = tcCodeDao.getTcCodesByType(Constant.COMPLAINT_STATUS.toString());
		List<Map<String, Object>> cpis = tcCodeDao.getTcCodesByType(Constant.GRADE_TYPE);
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[37];
					detail[0] = CommonUtils.checkNull(map.get("CPNO"));
					detail[1] = CommonUtils.checkNull(map.get("CPNO"));
					detail[2] = CommonUtils.checkNull(map.get("CPLEVEL"));
					detail[3] = CommonUtils.checkNull(map.get("BIZCONT"));
					detail[4] = CommonUtils.checkNull(map.get("PRO"));
					detail[5] = CommonUtils.checkNull(map.get("CITY"));
					detail[6] = CommonUtils.checkNull(map.get("CTMNAME"));
					detail[7] = CommonUtils.checkNull(map.get("PHONE"));
					detail[8] = CommonUtils.checkNull(map.get("CPCONT"));
					detail[9] = CommonUtils.checkNull(map.get("CPOBJECT"));
					detail[10] = CommonUtils.checkNull(map.get("CREATEDATE"));
					detail[11] = CommonUtils.checkNull(map.get("ACUSER"));
					detail[12] = CommonUtils.checkNull(map.get("SNAME"));
					detail[13] = CommonUtils.checkNull(map.get("VIN"));
					detail[14] = CommonUtils.checkNull(map.get("BDATE"));
					detail[15] = CommonUtils.checkNull(map.get("CPMILEAGE"));
					detail[16] = CommonUtils.checkNull(map.get("MILEAGERANGE"));
					detail[17] = CommonUtils.checkNull(map.get("ORGNAME"));
					detail[18] = CommonUtils.checkNull(map.get("DEALUSER"));
					detail[19] = CommonUtils.checkNull(map.get("CPACCDATE"));
					detail[20] = CommonUtils.checkNull(map.get("TURNDATE"));
					detail[21] = CommonUtils.checkNull(map.get("CRCONET"));
					detail[22] = CommonUtils.checkNull(map.get("CUSER"));
					detail[23] = CommonUtils.checkNull(map.get("CDATE"));
					detail[24] = CommonUtils.checkNull(map.get("CPCDATE"));
					detail[25] = CommonUtils.checkNull(map.get("DEALTIME"));
					detail[26] = CommonUtils.checkNull(map.get("CPLIMIT"));
					detail[27] = CommonUtils.checkNull(map.get("DELAYDATE"));
					detail[28] = CommonUtils.checkNull(map.get("SHOULDCLOSETIME"));
					detail[29] = CommonUtils.checkNull(map.get("ISTIMELYCLOSE"));
					detail[30] = CommonUtils.checkNull(map.get("CPISONCESF"));
					detail[31] = CommonUtils.checkNull(map.get("ISNORMALCLOSE"));
					detail[32] = CommonUtils.checkNull(map.get("DEALCONTENT"));
					detail[33] = CommonUtils.checkNull(map.get("LASTDEALDATE"));
					detail[34] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CPSF")),cpis);
					detail[35] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("STATUS")),cpns);
					detail[36] = CommonUtils.checkNull(map.get("DUSER"));
					
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "抱怨查询清单.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
}
