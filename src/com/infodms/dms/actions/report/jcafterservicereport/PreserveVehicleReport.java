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

public class PreserveVehicleReport {
	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/preservevehiclereport02.jsp";
	private final String initUrl01 = "/jsp/report/jcafterservicereport/preservevehiclereport.jsp";
/**
 * 轿车维修车辆报表
 */
	public void preserveVehicleReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = dao.getAreaList();//区域
			List<Map<String, Object>> modelList = dao.getModelGroupList(String.valueOf(Constant.WR_MODEL_GROUP_TYPE_01));//车型大类
			List<Map<String, Object>> seriesList = dao.getSeriesList();//车系
			
			act.setOutData("seriesList", seriesList);
			act.setOutData("areaList", areaList);
			act.setOutData("modelList", modelList);
			act.setForword(initUrl01);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车维修车辆报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
/**
 * 轿车维修车辆报表(不包含服务商)
 */
	public void preserveVehicleReportInit02(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = dao.getAreaList();//区域
			List<Map<String, Object>> modelList = dao.getModelGroupList(String.valueOf(Constant.WR_MODEL_GROUP_TYPE_01));//车型大类
			List<Map<String, Object>> seriesList = dao.getSeriesList();//车系
			
			act.setOutData("seriesList", seriesList);
			act.setOutData("areaList", areaList);
			act.setOutData("modelList", modelList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车维修车辆报表(不包含服务商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryPreserveVehicleReport02(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String areaName = request.getParamValue("areaName");//大区
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String dealerId = Utility.getCODES(request.getParamValue("dealerId"));//经销商Id
			String modelName = request.getParamValue("modelName");//车型大类
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String freeTims = request.getParamValue("freeTims");
			String repairType = request.getParamValue("repairType");
			String seriesName = request.getParamValue("seriesName");//车系
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);

			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFreeTims(freeTims);
			bean.setRepairType(repairType);
			bean.setSeriesName(seriesName);
			bean.setDealerId(dealerId);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPreserveVehicleReport02(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车维修车辆报表(不包含服务商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void queryPreserveVehicleReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String areaName = request.getParamValue("areaName");//大区
			//String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String dealerCode = request.getParamValue("dealerCode");//经销商代码
			String dealerId = Utility.getCODES(request.getParamValue("dealerId"));//经销商ID
			String modelName = request.getParamValue("modelName");//车型大类
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String freeTims = request.getParamValue("freeTims");
			String repairType = request.getParamValue("repairType");
			String seriesName = request.getParamValue("seriesName");//车系
			String province = request.getParamValue("province");//省份
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);

			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFreeTims(freeTims);
			bean.setRepairType(repairType);
			bean.setSeriesName(seriesName);
			bean.setDealerId(dealerId);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPreserveVehicleReport03(province,bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车维修车辆报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void preserveVehicleReportExcel02(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String areaName = request.getParamValue("areaName");//大区
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String modelName = request.getParamValue("modelName");//车型大类
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String freeTims = request.getParamValue("freeTims");
			String repairType = request.getParamValue("repairType");
			String seriesName = request.getParamValue("seriesName");//车系
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			String[] head=new String[1];
			head[0]="VIN";
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);
			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFreeTims(freeTims);
			bean.setSeriesName(seriesName);
			bean.setRepairType(repairType);
			List<Map<String, Object>> list= dao.queryPreserveVehicleReport04(bean);
		    List list1=new ArrayList();
		    
		    if(list!=null&&list.size()!=0){
//				for(int i=0;i<list.size();i++){
//			    	Map map =(Map)list.get(i);
//			    	if(map!=null&&map.size()!=0){
//						String[]detail=new String[2];
//						detail[0] = String.valueOf(map.get("ROWNUM"));
//						detail[1] = String.valueOf(map.get("VIN"));
//						list1.add(detail);
//			    	}
//			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toExcel2(ActionContext.getContext().getResponse(), request, head, list);
		    }
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车维修车辆报表(不包含服务商)");
				logger.error(logonUser,e1);
				act.setException(e1);
			}	
	}
	
	public void preserveVehicleReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String areaName = request.getParamValue("areaName");//大区
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String modelName = request.getParamValue("modelName");//车型大类
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String freeTims = request.getParamValue("freeTims");
			String repairType = request.getParamValue("repairType");
			String seriesName = request.getParamValue("seriesName");//车系
			String province = request.getParamValue("province");//省份
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			String[] head=new String[6];
			head[0]="序号";
			head[1]="大区";
			head[2]="省份";
			head[3]="经销商代码";
			head[4]="经销商名称";
			head[5]="维修车辆数";
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);
			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFreeTims(freeTims);
			bean.setSeriesName(seriesName);
			bean.setRepairType(repairType);
			List<Map<String, Object>> list= dao.getPreserveVehicleExcelList02(province,bean);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[6];
						detail[0] = String.valueOf(map.get("ROWNUM"));
						detail[1] = String.valueOf(map.get("ROOT_ORG_NAME"));
						detail[2] = String.valueOf(map.get("REGION_NAME"));
						detail[3] = String.valueOf(map.get("DEALER_CODE"));
						detail[4] = String.valueOf(map.get("DEALER_NAME"));
						detail[5] = String.valueOf(map.get("CON"));
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toExcel(ActionContext.getContext().getResponse(), request, head, list1);
		    }
		    act.setForword(initUrl01);	
		    //act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车维修车辆报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}	
	}
}
