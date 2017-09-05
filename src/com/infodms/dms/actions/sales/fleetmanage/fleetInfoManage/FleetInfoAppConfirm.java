package com.infodms.dms.actions.sales.fleetmanage.fleetInfoManage;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.FleetInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetAuditDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetInfoAppDao;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtFleetAuditPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:FleetInfoAppConfirm.java</p>
 *
 * <p>Description: 集团客户报备确认业务逻辑层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-12</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class FleetInfoAppConfirm {

	private static Logger logger = Logger.getLogger(FleetInfoAppConfirm.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 集团客户报备确认初始化页面
	private final String fleetInfoConfirmInitUrl = "/jsp/sales/fleetmanage/fleetinfomanage/fleetInfoConfirmOEM.jsp";
	
	// 集团客户报备确认页面
	private final String fleetInfoConfirmUrl = "/jsp/sales/fleetmanage/fleetinfomanage/checkFleetInfoAppOEM.jsp";
	
	// 集团客户确认页面
	
	private final String showFieetUrl = "/jsp/sales/fleetmanage/fleetinfomanage/showFieet.jsp";
	
	/**
	 * 集团客户报备确认初始化
	 */
	public void confirmFleetAppInit(){
		try{
			act.setForword(fleetInfoConfirmInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"集团客户报备确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户报备确认查询
	 */
	public void queryFleetInfo(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));  	    //客户名称
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));	    //客户类型
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));       //提报开始日期
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));           //提报结束日期
			String dealerCodes = CommonUtils.checkNull(request.getParamValue("dealerCode"));     //经销商公司
			String dealerCode = "'" + dealerCodes.replaceAll(",", "','") + "'" ;
			
			
			// 传入查询条件
			FleetInfoBean fibean = new FleetInfoBean();
			fibean.setFleetName(fleetName);
			fibean.setFleetType(fleetType);
			fibean.setBeginTime(beginTime);
			fibean.setEndTime(endTime);
			String dtype = logonUser.getDutyType();
			//if(dtype.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
			fibean.setStatus(String.valueOf(Constant.FLEET_INFO_TYPE_02));
			//}else{
			//	fibean.setStatus(String.valueOf(Constant.FLEET_INFO_TYPE_05));
			//}
			fibean.setDlrCompanyCode(dealerCode);
			//新增组织ID 过滤大区  add by lishuai103@yahoo.cn
			fibean.setOrgId(String.valueOf(logonUser.getOrgId()));
			fibean.setDutyType(dtype);
			
			FleetInfoAppDao appdao = new FleetInfoAppDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps=null;
			//小区
			if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(logonUser.getDutyType())){
				ps = appdao.querySmallFleetInfo(fibean,logonUser.getOrgId().toString(),Constant.PAGE_SIZE,curPage);
				//大区
			}else if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())){
				fibean.setStatus(String.valueOf(Constant.FLEET_INFO_TYPE_06));
				ps = appdao.queryLargeFleetInfo(fibean,logonUser.getOrgId().toString(),Constant.PAGE_SIZE,curPage);
			}else{
				//车厂
				fibean.setStatus(Constant.FLEET_INFO_TYPE_05.toString());
				ps = appdao.queryFleetInfo(fibean,Constant.PAGE_SIZE,curPage);
				//分页方法 end
			}
			
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
			act.setForword(fleetInfoConfirmInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备确认查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户报备确认
	 */
	public void checkFleetInfo(){
		try{
			// 取得页面传参tt_fleet_audit
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));  	 
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			List<Map<String, Object>> list2 = appDao.getFleetAuditInfobyId(fleetId);
			// 根据集团客户ID查询出详细信息
			Map<String, Object> fleetMap = appDao.getFleetInfobyId(fleetId);
			
			
			act.setOutData("fleetMap", fleetMap);
			
			//根据集团客户主表的id查询子表需求说明中的内容
			
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			act.setOutData("tfrdMapList", tfrdMapList);
			if(list2.size()>0){
			act.setOutData("checkList",list2);
			}else{
				act.setOutData("checkList",null);
			}
			act.setForword(fleetInfoConfirmUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户报备确认审核
	 */
	public void auditFleetInfoApp(){
		try{
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			FleetAuditDao auditDao=new FleetAuditDao();
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));  // 集团客户ID
			String auditRemark = CommonUtils.checkNull(request.getParamValue("auditRemark"));  // 审核说明
			String flag =  CommonUtils.checkNull(request.getParamValue("flag"));    // 通过和驳回的标识，1为通过，0为驳回
			
			TtFleetAuditPO ttFleetAuditPO=new TtFleetAuditPO();
			ttFleetAuditPO.setFleetId(Long.parseLong(fleetId));
			ttFleetAuditPO.setAuditId(Long.parseLong(SequenceManager.getSequence("")));
			ttFleetAuditPO.setOrgId(logonUser.getOrgId());
			ttFleetAuditPO.setAuditUserId(logonUser.getUserId());
			if(flag.equals("1")){
			ttFleetAuditPO.setAuditResult(Constant.FLEET_AUDIT_01);
			}else{
				ttFleetAuditPO.setAuditResult(Constant.FLEET_AUDIT_02);
			}
			ttFleetAuditPO.setAuditRemark(auditRemark);
			ttFleetAuditPO.setAuditDate(new Date());
			ttFleetAuditPO.setCreateBy(logonUser.getUserId());
			ttFleetAuditPO.setCreateDate(ttFleetAuditPO.getAuditDate());
			
			auditDao.insert(ttFleetAuditPO);
			
			TmFleetPO po1 = new TmFleetPO();
			po1.setFleetId(new Long(fleetId));
			TmFleetPO po2 = new TmFleetPO();
			po2.setAuditDate(ttFleetAuditPO.getAuditDate());
			po2.setAuditUserId(logonUser.getUserId());
			po2.setAuditRemark(auditRemark);
			po2.setUpdateBy(logonUser.getUserId());
			po2.setUpdateDate(ttFleetAuditPO.getAuditDate());
			
			String dtype = logonUser.getDutyType();
			if(flag.equals("1")){
				if(dtype.equals(String.valueOf(Constant.DUTY_TYPE_COMPANY))){
					po2.setStatus(Constant.FLEET_INFO_TYPE_03);
					//生成大客户代码
					TmFleetPO po3=new TmFleetPO();
					po3=appDao.select(po1).get(0);
					String fleet_code="B";
					Calendar c=Calendar.getInstance();
					int day=c.get(Calendar.DAY_OF_MONTH);
					int year=c.get(Calendar.YEAR);
					int month=c.get(Calendar.MONTH);
					String dayString="";
					String monthString="";
					if(day<9){
						dayString="0"+day;
					}else{
						dayString=day+"";
					}
					if((month+1)<10){
						monthString="0"+(month+1);
					}else{
						monthString=""+(month+1);
					}
					String currentDate=""+year+monthString+dayString;
					fleet_code+=currentDate;
					String current_date=year+"-"+monthString+"-"+dayString;
					FleetInfoAppDao fdao = new FleetInfoAppDao();
					int counts=fdao.getDealerFleetCount(po3.getDlrCompanyId().toString(), current_date);
					String flow_number="";
					if(counts<10){
						flow_number="0"+(counts+1);
					}else{
						flow_number=(counts+1)+"";
					}
					fleet_code+=flow_number;
					String dealer_code="";
					UserManageDao udao=new UserManageDao();
					TmDealerPO tdpo=new TmDealerPO();
					tdpo.setCompanyId(po3.getDlrCompanyId());
					tdpo=(TmDealerPO) udao.select(tdpo).get(0);
					 dealer_code=tdpo.getDealerCode();
					 fleet_code+=dealer_code;
					String lastChar="";
					if(Constant.FLEET_TYPE_14.equals(po3.getFleetType().toString())){
						lastChar+="A";
					}else if(Constant.FLEET_TYPE_15.equals(po3.getFleetType().toString())){
						lastChar+="D";
					}else if(Constant.FLEET_TYPE_16.equals(po3.getFleetType().toString())){
						lastChar+="E";
					}else if(Constant.FLEET_TYPE_17.equals(po3.getFleetType().toString())){
						lastChar+="F";
					}else if(Constant.FLEET_TYPE_18.equals(po3.getFleetType().toString())){
						lastChar+="C";
					}else if(Constant.FLEET_TYPE_19.equals(po3.getFleetType().toString())){
						lastChar+="B";
					}
					fleet_code+=lastChar;
					po2.setFleetCode(fleet_code);
					
				}
				
			}else{
				po2.setStatus(Constant.FLEET_INFO_TYPE_04);
			}
			//小区确认后改为待大区审核
			if(flag.equals("1")&&dtype.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
				po2.setStatus(Constant.FLEET_INFO_TYPE_06);
			}
			//待大区审核改为待车厂审核
			if(flag.equals("1")&&dtype.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				po2.setStatus(Constant.FLEET_INFO_TYPE_05);
			}
			// 根据集团客户ID更新状态
			appDao.updateFleetInfo(po1, po2);
			act.setForword(fleetInfoConfirmInitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"集团客户报备确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 查看集团客户是否已报备
	 * 根据集团客户名词模糊查询
	 * add by lishuai103@yahoo.cn
	 */
	
	public void showFieetInit(){
		try {
			String name = CommonUtils.checkNull(java.net.URLDecoder.decode(request.getParamValue("val1"),"utf-8"));
			String id = CommonUtils.checkNull(request.getParamValue("val2"));//集团客户ID
			act.setOutData("fname", name);
			act.setOutData("fid", id);
			act.setForword(showFieetUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void showFieet(){
		try{
			String name = CommonUtils.checkNull(request.getParamValue("val1"));//集团客户名称  	 
			String id = CommonUtils.checkNull(request.getParamValue("val2"));//集团客户ID
			FleetInfoAppDao fdao = new FleetInfoAppDao();
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = fdao.showFieetList(id, name,curPage, Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备确认");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
