package com.infodms.dms.actions.sales.usermng;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import com.infodms.dms.actions.sales.storageManage.AddressAddApply;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.usermng.IntegationManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsIntegrationAgainstPO;
import com.infodms.dms.po.TtVsIntegrationChangePO;
import com.infodms.dms.po.TtVsPersonPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class IntegationManage {
	public Logger logger = Logger.getLogger(AddressAddApply.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	IntegationManageDao dao=IntegationManageDao.getInstance();
	private ResponseWrapper response = act.getResponse();
	private final String  INTEG_CHANGE="/jsp/sales/userMng/integ_change_select.jsp";
	private final String  INTEG_TOATL="/jsp/sales/userMng/integ_total_select.jsp";
	private final String  INTEG_MONTH="/jsp/sales/userMng/integ_month_select.jsp";
	private final String  INTEG_YEAR="/jsp/sales/userMng/integ_year_select.jsp";
	private final String  INTEG_AGAINST_SELECT="/jsp/sales/userMng/integ_against_select.jsp";
	private final String PERSON_INTEG_AGAINST="/jsp/sales/userMng/person_integ_against.jsp";
	
	/**
	 * 积分变动管理
	 */
	public void integChange(){
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			
			act.setForword(INTEG_CHANGE);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分变动管理初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 积分变动管理查询
	 */
	public void integChangeSelect(){
		AclUserBean logonUser = null ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String,Object> parm=new HashMap<String,Object>();
			String name=CommonUtils.checkNull(request.getParamValue("name"));
			if(name!=null){
				name=name.trim();
			}
			String idNo=CommonUtils.checkNull(request.getParamValue("idNo"));
			if(idNo!=null){
				idNo=idNo.trim();
			}
			String status=CommonUtils.checkNull(request.getParamValue("status"));
			//积分类型
			String integType=CommonUtils.checkNull(request.getParamValue("integType"));
			String positionStatus=CommonUtils.checkNull(request.getParamValue("positionStatus"));
			String position=CommonUtils.checkNull(request.getParamValue("position"));
			String bankCardNo=CommonUtils.checkNull(request.getParamValue("bankCardNo"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dealerId=CommonUtils.checkNull(logonUser.getDealerId());
			String dutyType=logonUser.getDutyType();
			//把参数加入到parm集合中
			parm.put("name",name);
			parm.put("idNo",idNo);
			parm.put("status",status);
			parm.put("integType",integType);
			parm.put("orgId", logonUser.getOrgId());
			parm.put("dealerId", dealerId);
			parm.put("positionStatus", positionStatus);
			parm.put("position", position);
			parm.put("bankCardNo", bankCardNo);
			parm.put("dealerCode", dealerCode);
			String pageSize0=CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0=CommonUtils.checkNull(request.getParamValue("curPage"));
			int curPage=1;
			int pageSize=10;
			if(curPage0!=null&&!"".equals(curPage0)){
				curPage=Integer.parseInt(curPage0);
			}
			if(pageSize0!=null&&!"".equals(pageSize0)){
				pageSize=Integer.parseInt(pageSize0);
			}
			PageResult<Map<String,Object>> ps=dao.integChangeSelect(parm,dutyType,pageSize,curPage);
			act.setOutData("ps", ps);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分变动管理查询异常");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 积分综合评价管理
	 */
	public void integTotal(){
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			
			act.setForword(INTEG_TOATL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分综合评价管理初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 积分综合评价管理查询
	 */
	public void integTotalSelect(){
		AclUserBean logonUser = null ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String,Object> parm=new HashMap<String,Object>();
			String name=CommonUtils.checkNull(request.getParamValue("name"));
			String idNo=CommonUtils.checkNull(request.getParamValue("idNo"));
			String status=CommonUtils.checkNull(request.getParamValue("status"));
			String dealerId=CommonUtils.checkNull(logonUser.getDealerId());
			String dutyType=logonUser.getDutyType();
			//把参数加入到parm集合中
			parm.put("name",name);
			parm.put("idNo",idNo);
			parm.put("status",status);
			parm.put("orgId", logonUser.getOrgId());
			parm.put("dealerId", dealerId);
			String pageSize0=CommonUtils.checkNull(request.getParamValue("pagesize"));
			String curPage0=CommonUtils.checkNull(request.getParamValue("curPage"));
			int curPage=1;
			int pageSize=10;
			if(curPage0!=null&&!"".equals(curPage0)){
				curPage=Integer.parseInt(curPage0);
			}
			if(pageSize0!=null&&!"".equals(pageSize0)){
				pageSize=Integer.parseInt(pageSize0);
			}
			PageResult<Map<String,Object>> ps=dao.integTotalSelect(parm,dutyType,curPage,pageSize);
			act.setOutData("ps", ps);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分综合评价查询异常");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 人员月度积分管理
	 */
	public void integMonth(){
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
			//可以选择近三年的数据
			List list=new ArrayList();
			Calendar c=Calendar.getInstance();
			int year=c.get(c.YEAR);
			for(int i=year-3;i<=year;i++){
				list.add(i);
			}
//			act.setOutData("dateList", list) ;
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(INTEG_MONTH);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"月度积分管理初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 人员月度积分管理查询
	 */
	public void integMonthSelect(){
		AclUserBean logonUser = null ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String,Object> parm=new HashMap<String,Object>();
			String name=CommonUtils.checkNull(request.getParamValue("name"));
			String idNo=CommonUtils.checkNull(request.getParamValue("idNo"));
			String status=CommonUtils.checkNull(request.getParamValue("status"));
			String positionStatus=CommonUtils.checkNull(request.getParamValue("positionStatus"));
			String dealerId=CommonUtils.checkNull(logonUser.getDealerId());
			String dutyType=logonUser.getDutyType();
			String month=CommonUtils.checkNull(request.getParamValue("month"));
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String position=CommonUtils.checkNull(request.getParamValue("position"));
			String bankCardNo=CommonUtils.checkNull(request.getParamValue("bankCardNo"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//把参数加入到parm集合中
			parm.put("name",name);
			parm.put("idNo",idNo);
			parm.put("status",status);
			parm.put("orgId", logonUser.getOrgId());
			parm.put("dealerId", dealerId);
			parm.put("positionStatus", positionStatus);
			parm.put("month", month);
			parm.put("position", position);
			parm.put("bankCardNo", bankCardNo);
			parm.put("year", year);
			parm.put("dealerCode", dealerCode);
			String pageSize0=CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0=CommonUtils.checkNull(request.getParamValue("curPage"));
			int curPage=1;
			int pageSize=10;
			if(curPage0!=null&&!"".equals(curPage0)){
				curPage=Integer.parseInt(curPage0);
			}
			if(pageSize0!=null&&!"".equals(pageSize0)){
				pageSize=Integer.parseInt(pageSize0);
			}
			PageResult<Map<String,Object>> ps=dao.integMonthSelect(parm,dutyType,curPage,pageSize);
			act.setOutData("ps", ps);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"人员月度积分查询异常");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 人员年度积分管理
	 */
	public void integYear(){
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
			//可以选择近三年的数据
			List list=new ArrayList();
			Calendar c=Calendar.getInstance();
			int year=c.get(c.YEAR);
			for(int i=year-3;i<=year;i++){
				list.add(i);
			}
//			act.setOutData("dateList", list) ;
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(INTEG_YEAR);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"年度积分管理初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 人员年度积分管理查询
	 */
	public void integYearSelect(){
		AclUserBean logonUser = null ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String,Object> parm=new HashMap<String,Object>();
			String name=CommonUtils.checkNull(request.getParamValue("name"));
			String idNo=CommonUtils.checkNull(request.getParamValue("idNo"));
			String status=CommonUtils.checkNull(request.getParamValue("status"));
			String dealerId=CommonUtils.checkNull(logonUser.getDealerId());
			String dutyType=logonUser.getDutyType();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String positionStatus=CommonUtils.checkNull(request.getParamValue("positionStatus"));
			String position=CommonUtils.checkNull(request.getParamValue("position"));
			String bankCardNo=CommonUtils.checkNull(request.getParamValue("bankCardNo"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//把参数加入到parm集合中
			parm.put("name",name);
			parm.put("idNo",idNo);
			parm.put("status",status);
			parm.put("orgId", logonUser.getOrgId());
			parm.put("dealerId", dealerId);
			parm.put("year", year);
			parm.put("positionStatus", positionStatus);
			parm.put("position", position);
			parm.put("bankCardNo", bankCardNo);
			parm.put("dealerCode", dealerCode);
			String pageSize0=CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0=CommonUtils.checkNull(request.getParamValue("curPage"));
			int curPage=1;
			int pageSize=10;
			if(curPage0!=null&&!"".equals(curPage0)){
				curPage=Integer.parseInt(curPage0);
			}
			if(pageSize0!=null&&!"".equals(pageSize0)){
				pageSize=Integer.parseInt(pageSize0);
			}
			PageResult<Map<String,Object>> ps=dao.integYearSelect(parm,dutyType,curPage,pageSize);
			act.setOutData("ps", ps);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"人员年度积分查询异常");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 积分兑现管理
	 */
	public void integ_against_select(){
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areaIds = acc.getAreaStr(areaList) ;
			//可以选择近三年的数据
			List list=new ArrayList();
			Calendar c=Calendar.getInstance();
			int year=c.get(c.YEAR);
			for(int i=year-3;i<=year;i++){
				list.add(i);
			}
			act.setOutData("dateList", list) ;
//			
//			act.setOutData("areaIds", areaIds) ;
			act.setOutData("areaList", areaList);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(INTEG_AGAINST_SELECT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分兑现查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 人员积分兑现管理查询
	 */
	public void integAgainstSelect(){
		AclUserBean logonUser = null ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String,Object> parm=new HashMap<String,Object>();
			String name=CommonUtils.checkNull(request.getParamValue("name"));
			String idNo=CommonUtils.checkNull(request.getParamValue("idNo"));
			String status=CommonUtils.checkNull(request.getParamValue("status"));
			String dealerId=CommonUtils.checkNull(logonUser.getDealerId());
			String dutyType=logonUser.getDutyType();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String positionStatus=CommonUtils.checkNull(request.getParamValue("positionStatus"));
			String position=CommonUtils.checkNull(request.getParamValue("position"));
			String bankCardNo=CommonUtils.checkNull(request.getParamValue("bankCardNo"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String isAgainst=CommonUtils.checkNull(request.getParamValue("isAgainst"));
			//把参数加入到parm集合中
			parm.put("name",name);
			parm.put("idNo",idNo);
			parm.put("status",status);
			parm.put("orgId", logonUser.getOrgId());
			parm.put("dealerId", dealerId);
			parm.put("year", year);
			parm.put("positionStatus", positionStatus);
			parm.put("position", position);
			parm.put("bankCardNo", bankCardNo);
			parm.put("dealerCode", dealerCode);
			parm.put("isAgainst", isAgainst);
			String pageSize0=CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0=CommonUtils.checkNull(request.getParamValue("curPage"));
			int curPage=1;
			int pageSize=10;
			if(curPage0!=null&&!"".equals(curPage0)){
				curPage=Integer.parseInt(curPage0);
			}
			if(pageSize0!=null&&!"".equals(pageSize0)){
				pageSize=Integer.parseInt(pageSize0);
			}
			PageResult<Map<String,Object>> ps=dao.integAgainstSelect(parm,dutyType,curPage,pageSize);
			act.setOutData("ps", ps);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"人员积分兑现查询异常");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	 
		/**
		 * 
		 * 人员积分兑现和清零
		 */
		public void integAgainstInit(){
			AclUserBean logonUser = null ;
			try {
				logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
//				
//				ActivitiesCamCost acc = new ActivitiesCamCost() ;
//				String areaIds = acc.getAreaStr(areaList) ;
//				
//				act.setOutData("areaIds", areaIds) ;
				act.setOutData("areaList", areaList);
				act.setOutData("dutyType", logonUser.getDutyType());
				act.setOutData("orgId", logonUser.getOrgId());
				//可以选择近三年的数据
				List list=new ArrayList();
				Calendar c=Calendar.getInstance();
				int year=c.get(c.YEAR);
				for(int i=year-3;i<=year;i++){
					list.add(i);
				}
				act.setOutData("dateList", list) ;
				act.setForword(PERSON_INTEG_AGAINST);
			}catch(Exception e) {//异常方法
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分兑现和清零初始化异常");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		/**
		 * 积分兑现
		 */
		public void integAgainstOper(){
			AclUserBean logonUser = null ;
			try {
				logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
				String ids=CommonUtils.checkNull(request.getParamValue("againstId"));
				if(ids==null||(ids!=null&&"".equals(ids))){
					act.setOutData("subFlag", "nullvalue");
					return;
				}
				String [] id_array=ids.split(",");
				for(int i=0;i<id_array.length;i++){
					//修改状态
					TtVsIntegrationAgainstPO tviap0=new TtVsIntegrationAgainstPO();
					tviap0.setIntegrationAgainstId(id_array[i]==null||"".equals(id_array[i])?null:Long.parseLong(id_array[i]));
					TtVsIntegrationAgainstPO tviap1=new TtVsIntegrationAgainstPO();
					tviap1.setIsAgainst(Constant.IS_AGAINST_YES);
					tviap1.setAgainstDate(new Date());
					dao.update(tviap0, tviap1);
				}
				act.setOutData("subFlag", "success");
				
			}catch(Exception e) {//异常方法
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分兑现异常");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		/**
		 * 积分清零
		 */
		public void integClearOper(){
			AclUserBean logonUser = null ;
			try {
				logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
				logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
				String ids=CommonUtils.checkNull(request.getParamValue("againstId"));
				if(ids==null||(ids!=null&&"".equals(ids))){
					act.setOutData("subFlag", "nullvalue");
					return;
				}
				String [] id_array=ids.split(",");
				for(int i=0;i<id_array.length;i++){
					TtVsPersonPO tvp=new TtVsPersonPO();
					//根据兑现表id找出身份证号
					TtVsIntegrationAgainstPO tviap=new TtVsIntegrationAgainstPO();
					tviap.setIntegrationAgainstId(id_array[i]==null||"".equals(id_array[i])?null:Long.parseLong(id_array[i]));
					tviap=(TtVsIntegrationAgainstPO) dao.select(tviap).get(0);
					tvp.setIdNo(tviap.getIdNo());
					tvp=(TtVsPersonPO) dao.select(tvp).get(0);
					//写入积分变动历史表
					TtVsIntegrationChangePO tvic=new TtVsIntegrationChangePO();
					Long integChangeId=Long.parseLong(SequenceManager.getSequence(""));
					tvic.setIntegChangeId(integChangeId);
					tvic.setCreateDate(new Date());
					tvic.setDealerId(tvp.getDealerId());
					tvic.setIdNo(tvp.getIdNo());
					tvic.setIntegAfter(Long.parseLong("0"));
					tvic.setChangeType(Constant.INTEG_CHANGE_CLEAR);
					tvic.setName(tvp.getName());
					tvic.setRelationId(integChangeId);
					//积分变动表里写入清零记录
					dao.insert(tvic);
					//积分清零后调用的方法
					List in = new ArrayList();
					in.add(new Date());
					in.add(tvp.getIdNo());
					dao.callProcedure("PERSON_INTEG_ZERO", in, null);
				}
				act.setOutData("subFlag", "success");
			}catch(Exception e) {//异常方法
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分清零异常");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		/**
		 * 积分兑现下载
		 */
		public void integAgainstDownLoad(){
			AclUserBean logonUser = null ;
			try {
				logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
//				
//				ActivitiesCamCost acc = new ActivitiesCamCost() ;
//				String areaIds = acc.getAreaStr(areaList) ;
				act.setOutData("dutyType", logonUser.getDutyType());
//				act.setOutData("orgId", logonUser.getOrgId());
//				act.setOutData("areaIds", areaIds) ;
				act.setOutData("areaList", areaList);
				Map<String,String> parm=new HashMap<String,String>();
				String name=CommonUtils.checkNull(request.getParamValue("name"));
				String idNo=CommonUtils.checkNull(request.getParamValue("idNo"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String dealerId=CommonUtils.checkNull(logonUser.getDealerId());
				String dutyType=logonUser.getDutyType();
				String year=CommonUtils.checkNull(request.getParamValue("year"));
				String positionStatus=CommonUtils.checkNull(request.getParamValue("positionStatus"));
				String position=CommonUtils.checkNull(request.getParamValue("position"));
				String bankCardNo=CommonUtils.checkNull(request.getParamValue("bankCardNo"));
				String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
				//把参数加入到parm集合中
				parm.put("name",name);
				parm.put("idNo",idNo);
				parm.put("status",status);
				parm.put("orgId", logonUser.getOrgId().toString());
				parm.put("dealerId", dealerId);
				parm.put("year", year);
				parm.put("positionStatus", positionStatus);
				parm.put("position", position);
				parm.put("bankCardNo", bankCardNo);
				parm.put("dealerCode", dealerCode);
				parm.put("dutyType", dutyType);
				List<Map<String,Object>> returnList=dao.getPersonIntegAgaist(parm);
				// 导出的文件名
				String fileName = "兑现数据.xls";
				// 导出的文字编码
					fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename="
						+ fileName);
				OutputStream os = response.getOutputStream();
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				WritableSheet sheet = workbook.createSheet("兑现数据查询", 0);
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setAlignment(Alignment.CENTRE);
				List<List<Object>> lists = new LinkedList<List<Object>>();
				int y=0;
				sheet.mergeCells(0, y, 20, y);
				sheet.addCell(new jxl.write.Label(0, y, "兑现数据",wcf));
				++y;
				sheet.addCell(new Label(0, y, "片区"));
				sheet.addCell(new Label(1, y, "机构编码"));
				sheet.addCell(new Label(2, y, "机构名称"));
				sheet.addCell(new Label(3, y, "机构状态"));
				sheet.addCell(new Label(4, y, "年份"));
				sheet.addCell(new Label(5, y, "岗位"));
				sheet.addCell(new Label(6, y, "职位状态"));
				sheet.addCell(new Label(7, y, "姓名"));
				sheet.addCell(new Label(8, y, "银行"));
				sheet.addCell(new Label(9, y, "银行卡号"));
				sheet.addCell(new Label(10, y, "身份证"));
				sheet.addCell(new Label(11, y, "业绩N-3"));
				sheet.addCell(new Label(12, y, "业绩N-2"));
				sheet.addCell(new Label(13, y, "业绩N-1"));
				sheet.addCell(new Label(14, y, "认证N-3"));
				sheet.addCell(new Label(15, y, "认证N-2"));
				sheet.addCell(new Label(16, y, "认证N-1"));
				sheet.addCell(new Label(17, y, "年限N-3"));
				sheet.addCell(new Label(18, y, "年限N-2"));
				sheet.addCell(new Label(19, y, "年限N-1"));
				sheet.addCell(new Label(20, y, "业绩积分"));
				sheet.addCell(new Label(21, y, "认证积分"));
				sheet.addCell(new Label(22, y, "年限积分"));
				sheet.addCell(new Label(23, y, "合计积分"));
				sheet.addCell(new Label(24, y, "综合积分"));
				
				int length=returnList.size();
				for(int i=0;i<length;i++){
					++y;
					Map<String,Object> maps=returnList.get(i);
					sheet.addCell(new Label(0, y, maps.get("PQ_ORG_NAME").toString()));
					sheet.addCell(new Label(1, y, maps.get("DEALER_CODE").toString()));
					sheet.addCell(new Label(2, y, maps.get("DEALER_NAME").toString()));
					sheet.addCell(new Label(3, y, maps.get("DEALER_STATUS").toString()));
					sheet.addCell(new Label(4, y, maps.get("YEAR").toString()));
					sheet.addCell(new Label(5, y, maps.get("POSITION").toString()));
					sheet.addCell(new Label(6, y, maps.get("POSITION_STATUS").toString()));
					sheet.addCell(new Label(7, y, maps.get("NAME").toString()));
					sheet.addCell(new Label(8, y, maps.get("BANK").toString()));
					sheet.addCell(new Label(9, y, maps.get("BANK_CARDNO").toString()));
					sheet.addCell(new Label(10, y, maps.get("ID_NO").toString()));
					sheet.addCell(new Label(11, y, maps.get("THIRD_PERFORMANCE_AGAINST")==null?"0":maps.get("THIRD_PERFORMANCE_AGAINST").toString()));
					sheet.addCell(new Label(12, y, maps.get("SENCOND_PERFORMANCE_AGAINST")==null?"0":maps.get("SENCOND_PERFORMANCE_AGAINST").toString()));
					sheet.addCell(new Label(13, y, maps.get("FIRST_PERFORMANCE_AGAINST")==null?"0":maps.get("FIRST_PERFORMANCE_AGAINST").toString()));
					sheet.addCell(new Label(14, y, maps.get("THIRD_AUTHENTICATION_AGAINST")==null?"0":maps.get("THIRD_AUTHENTICATION_AGAINST").toString()));
					sheet.addCell(new Label(15, y, maps.get("SECOND_AUTHENTICATION_AGAINST")==null?"0":maps.get("SECOND_AUTHENTICATION_AGAINST").toString()));
					sheet.addCell(new Label(16, y, maps.get("FIRST_AUTHENTICATION_AGAINST")==null?"0":maps.get("FIRST_AUTHENTICATION_AGAINST").toString()));
					sheet.addCell(new Label(17, y, maps.get("THIRD_YEAR")==null?"0":maps.get("THIRD_YEAR").toString()));
					sheet.addCell(new Label(18, y, maps.get("SENCOND_YEAR")==null?"0":maps.get("SENCOND_YEAR").toString()));
					sheet.addCell(new Label(19, y, maps.get("FIRST_YEAR")==null?"0":maps.get("FIRST_YEAR").toString()));
					sheet.addCell(new Label(20, y, maps.get("PERFORMANCE_AGAINST").toString()));
					sheet.addCell(new Label(21, y, maps.get("AUTH_AGAINST").toString()));
					sheet.addCell(new Label(22, y, maps.get("YEAR_AGAINST").toString()));
					sheet.addCell(new Label(23, y, maps.get("TOTAL").toString()));
					sheet.addCell(new Label(24, y, maps.get("ALLTOTAL").toString()));
					
				}
				workbook.write();
				workbook.close();
			}catch(Exception e) {//异常方法
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分变动管理初始化");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		/**
		 * 月度积分下载
		 */
		public void integMonthDownLoad(){
			AclUserBean logonUser = null ;
			try {
				logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
				
//				ActivitiesCamCost acc = new ActivitiesCamCost() ;
//				String areaIds = acc.getAreaStr(areaList) ;
				act.setOutData("dutyType", logonUser.getDutyType());
//				act.setOutData("orgId", logonUser.getOrgId());
//				act.setOutData("areaIds", areaIds) ;
				act.setOutData("areaList", areaList);
				Map<String,String> parm=new HashMap<String,String>();
				String name=CommonUtils.checkNull(request.getParamValue("name"));
				String idNo=CommonUtils.checkNull(request.getParamValue("idNo"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String dealerId=CommonUtils.checkNull(logonUser.getDealerId());
				String dutyType=logonUser.getDutyType();
				String year=CommonUtils.checkNull(request.getParamValue("year"));
				String month=CommonUtils.checkNull(request.getParamValue("month"));
				String positionStatus=CommonUtils.checkNull(request.getParamValue("positionStatus"));
				String position=CommonUtils.checkNull(request.getParamValue("position"));
				String bankCardNo=CommonUtils.checkNull(request.getParamValue("bankCardNo"));
				String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
				
				//把参数加入到parm集合中
				parm.put("name",name);
				parm.put("idNo",idNo);
				parm.put("status",status);
				parm.put("orgId", logonUser.getOrgId().toString());
				parm.put("dealerId", dealerId);
				parm.put("year", year);
				parm.put("month", month);
				parm.put("positionStatus", positionStatus);
				parm.put("position", position);
				parm.put("bankCardNo", bankCardNo);
				parm.put("dealerCode", dealerCode);
				parm.put("dutyType", dutyType);
				
				List<Map<String,Object>> returnList=dao.getPersonIntegMonth(parm);
				// 导出的文件名
				String fileName = "月度积分.xls";
				// 导出的文字编码
					fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename="
						+ fileName);
				OutputStream os = response.getOutputStream();
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				WritableSheet sheet = workbook.createSheet("月度积分查询", 0);
				WritableCellFormat wcf = new WritableCellFormat();
				wcf.setAlignment(Alignment.CENTRE);
				List<List<Object>> lists = new LinkedList<List<Object>>();
				int y=0;
				sheet.mergeCells(0, y, 20, y);
				sheet.addCell(new jxl.write.Label(0, y, "月度积分数据",wcf));
				++y;
				sheet.addCell(new Label(0, y, "片区"));
				sheet.addCell(new Label(1, y, "机构编码"));
				sheet.addCell(new Label(2, y, "机构名称"));
				sheet.addCell(new Label(3, y, "年份"));
				sheet.addCell(new Label(4, y, "月份"));
				sheet.addCell(new Label(5, y, "岗位"));
				sheet.addCell(new Label(6, y, "职位状态"));
				sheet.addCell(new Label(7, y, "姓名"));
				sheet.addCell(new Label(8, y, "银行"));
				sheet.addCell(new Label(9, y, "银行卡号"));
				sheet.addCell(new Label(10, y, "身份证"));
				sheet.addCell(new Label(11, y, "本月业绩积分"));
				sheet.addCell(new Label(12, y, "本月认证积分"));
				sheet.addCell(new Label(13, y, "本月年限积分"));
				sheet.addCell(new Label(14, y, "本月积分"));
				int length=returnList.size();
				for(int i=0;i<length;i++){
					++y;
					Map<String,Object> maps=returnList.get(i);
					sheet.addCell(new Label(0, y, maps.get("PQ_ORG_NAME").toString()));
					sheet.addCell(new Label(1, y, maps.get("DEALER_CODE").toString()));
					sheet.addCell(new Label(2, y, maps.get("DEALER_NAME").toString()));
					sheet.addCell(new Label(3, y, maps.get("YEAR").toString()));
					sheet.addCell(new Label(4, y, maps.get("MONTH").toString()));
					sheet.addCell(new Label(5, y, maps.get("POSITION").toString()));
					sheet.addCell(new Label(6, y, maps.get("POSITION_STATUS").toString()));
					sheet.addCell(new Label(7, y, maps.get("NAME").toString()));
					sheet.addCell(new Label(8, y, maps.get("BANK").toString()));
					sheet.addCell(new Label(9, y, maps.get("BANK_CARDNO").toString()));
					sheet.addCell(new Label(10, y, maps.get("ID_NO").toString()));
					sheet.addCell(new Label(11, y, maps.get("MONTH_PERFORMANCE_INTEG").toString()));
					sheet.addCell(new Label(12, y, maps.get("MONTH_AUTHENTICATION_INTEG").toString()));
					sheet.addCell(new Label(13, y, maps.get("MONTH_YEAR_INTEG").toString()));
					sheet.addCell(new Label(14, y, maps.get("MONTH_INTEG").toString()));
					
				}
				workbook.write();
				workbook.close();
			}catch(Exception e) {//异常方法
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"积分变动管理初始化");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		
}
