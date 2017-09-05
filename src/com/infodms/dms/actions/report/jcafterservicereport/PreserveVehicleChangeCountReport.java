package com.infodms.dms.actions.report.jcafterservicereport;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;
/**
 * 车辆维修换件次数
 */
public class PreserveVehicleChangeCountReport {
	
	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/preservevehiclechangecountreport.jsp";
	private final String workUrl = "/jsp/report/jcafterservicereport/preservevehicleworkcountreport.jsp";

	public void preserveVehicleChangeCountReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> modelList = dao.getModelGroupList(String.valueOf(Constant.WR_MODEL_GROUP_TYPE_01));//车型大类
			act.setOutData("modelList", modelList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修换件次数");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void preserveVehicleWorkCountReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> modelList = dao.getModelGroupList(String.valueOf(Constant.WR_MODEL_GROUP_TYPE_01));//车型大类
			act.setOutData("modelList", modelList);
			act.setForword(workUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修作业次数");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryPreserveVehicleChangeCountReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String modelName = request.getParamValue("modelName");//车型大类
			String groupCode = Utility.getCODES(request.getParamValue("groupCode"));//车型代码
			String beginTime = request.getParamValue("beginTime");//工单开始时间
			String endTime = request.getParamValue("endTime");//工单结束时间
			String bbeginTime = request.getParamValue("bbeginTime");  //购车开始花时间
			String bendTime = request.getParamValue("bendTime");//购车结束时间
			String pbeginTime = request.getParamValue("pbeginTime");//出厂开始时间
			String pendTime = request.getParamValue("pendTime");//出厂结束时间
			String claType = request.getParamValue("claType");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)){
					beginTime = beginTime+" 00:00:00";
			}
			if(null!=endTime&&!"".equals(endTime)){
				endTime = endTime+" 23:59:59";
			}
			if(null!=bbeginTime&&!"".equals(bbeginTime)){
				bbeginTime = bbeginTime+" 00:00:00";
			}
			if(null!=bendTime&&!"".equals(bendTime)){
					bendTime = bendTime+" 23:59:59";
			}
			if(null!=pbeginTime&&!"".equals(pbeginTime)){
					pbeginTime = pbeginTime+" 00:00:00";
			}
			if(null!=pendTime&&!"".equals(pendTime)){
				pendTime = pendTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setBbeginTime(bbeginTime);
			bean.setBendTime(bendTime);
			bean.setPbeginTime(pbeginTime);
			bean.setPendTime(pendTime);
			bean.setGroupCode(groupCode);
			bean.setClaType(claType);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPreserveVehicleChangeCountReport(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修换件次数");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryPreserveVehicleWorkCountReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String modelName = request.getParamValue("modelName");//车型大类
			String groupCode = Utility.getCODES(request.getParamValue("groupCode"));//车型代码
			String beginTime = request.getParamValue("beginTime");//工单开始时间
			String endTime = request.getParamValue("endTime");//工单结束时间
			String bbeginTime = request.getParamValue("bbeginTime");  //购车开始花时间
			String bendTime = request.getParamValue("bendTime");//购车结束时间
			String pbeginTime = request.getParamValue("pbeginTime");//出厂开始时间
			String pendTime = request.getParamValue("pendTime");//出厂结束时间
			String claType = request.getParamValue("claType");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)){
					beginTime = beginTime+" 00:00:00";
			}
			if(null!=endTime&&!"".equals(endTime)){
				endTime = endTime+" 23:59:59";
			}
			if(null!=bbeginTime&&!"".equals(bbeginTime)){
				bbeginTime = bbeginTime+" 00:00:00";
			}
			if(null!=bendTime&&!"".equals(bendTime)){
					bendTime = bendTime+" 23:59:59";
			}
			if(null!=pbeginTime&&!"".equals(pbeginTime)){
					pbeginTime = pbeginTime+" 00:00:00";
			}
			if(null!=pendTime&&!"".equals(pendTime)){
				pendTime = pendTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setBbeginTime(bbeginTime);
			bean.setBendTime(bendTime);
			bean.setPbeginTime(pbeginTime);
			bean.setPendTime(pendTime);
			bean.setGroupCode(groupCode);
			bean.setClaType(claType);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPreserveVehicleWorkCountReport(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修作业次数");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void preserveVehicleWorkCountReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String modelName = request.getParamValue("modelName");//车型大类
			String groupCode = Utility.getCODES(request.getParamValue("groupCode"));//车型代码
			String beginTime = request.getParamValue("beginTime");//工单开始时间
			String endTime = request.getParamValue("endTime");//工单结束时间
			String bbeginTime = request.getParamValue("bbeginTime");  //购车开始花时间
			String bendTime = request.getParamValue("bendTime");//购车结束时间
			String pbeginTime = request.getParamValue("pbeginTime");//出厂开始时间
			String pendTime = request.getParamValue("pendTime");//出厂结束时间
			String claType = request.getParamValue("claType");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)){
					beginTime = beginTime+" 00:00:00";
			}
			if(null!=endTime&&!"".equals(endTime)){
				endTime = endTime+" 23:59:59";
			}
			if(null!=bbeginTime&&!"".equals(bbeginTime)){
				bbeginTime = bbeginTime+" 00:00:00";
			}
			if(null!=bendTime&&!"".equals(bendTime)){
					bendTime = bendTime+" 23:59:59";
			}
			if(null!=pbeginTime&&!"".equals(pbeginTime)){
					pbeginTime = pbeginTime+" 00:00:00";
			}
			if(null!=pendTime&&!"".equals(pendTime)){
				pendTime = pendTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setBbeginTime(bbeginTime);
			bean.setBendTime(bendTime);
			bean.setPbeginTime(pbeginTime);
			bean.setPendTime(pendTime);
			bean.setGroupCode(groupCode);
			bean.setClaType(claType);
			String[] head=new String[5];
			head[0]="序号";
			head[1]="车型";
			head[2]="配件代码";
			head[3]="配件名称";
			head[4]="次数";
			List<Map<String, Object>> list= dao.getPreserveVehicleChangeCountExcelList(bean);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[5];
						detail[0] = String.valueOf(map.get("NUM"));
						detail[1] = String.valueOf(map.get("GROUP_CODE"));
						detail[2] = String.valueOf(map.get("PART_CODE"));
						detail[3] = String.valueOf(map.get("PART_NAME"));
						detail[4] = String.valueOf(map.get("PCOUNT"));
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toExcel(ActionContext.getContext().getResponse(), request, head, list1);
		    }
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车保养VIN报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}	
	}
	
	public void preserveVehicleChangeCountReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String modelName = request.getParamValue("modelName");//车型大类
			String groupCode = Utility.getCODES(request.getParamValue("groupCode"));//车型代码
			String beginTime = request.getParamValue("beginTime");//工单开始时间
			String endTime = request.getParamValue("endTime");//工单结束时间
			String bbeginTime = request.getParamValue("bbeginTime");  //购车开始花时间
			String bendTime = request.getParamValue("bendTime");//购车结束时间
			String pbeginTime = request.getParamValue("pbeginTime");//出厂开始时间
			String pendTime = request.getParamValue("pendTime");//出厂结束时间
			String claType = request.getParamValue("claType");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)){
					beginTime = beginTime+" 00:00:00";
			}
			if(null!=endTime&&!"".equals(endTime)){
				endTime = endTime+" 23:59:59";
			}
			if(null!=bbeginTime&&!"".equals(bbeginTime)){
				bbeginTime = bbeginTime+" 00:00:00";
			}
			if(null!=bendTime&&!"".equals(bendTime)){
					bendTime = bendTime+" 23:59:59";
			}
			if(null!=pbeginTime&&!"".equals(pbeginTime)){
					pbeginTime = pbeginTime+" 00:00:00";
			}
			if(null!=pendTime&&!"".equals(pendTime)){
				pendTime = pendTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setBbeginTime(bbeginTime);
			bean.setBendTime(bendTime);
			bean.setPbeginTime(pbeginTime);
			bean.setPendTime(pendTime);
			bean.setGroupCode(groupCode);
			bean.setClaType(claType);
			String[] head=new String[5];
			head[0]="序号";
			head[1]="车型";
			head[2]="配件代码";
			head[3]="配件名称";
			head[4]="次数";
			List<Map<String, Object>> list= dao.getPreserveVehicleChangeCountExcelList(bean);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[5];
						detail[0] = String.valueOf(map.get("NUM"));
						detail[1] = String.valueOf(map.get("GROUP_CODE"));
						detail[2] = String.valueOf(map.get("PART_CODE"));
						detail[3] = String.valueOf(map.get("PART_NAME"));
						detail[4] = String.valueOf(map.get("PCOUNT"));
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toExcel(ActionContext.getContext().getResponse(), request, head, list1);
		    }
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车保养VIN报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}	
	}
}
