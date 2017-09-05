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
 * 轿车保养VIN报表
 */
public class MainteranceVinReport {
	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/mainterancevinreport.jsp";
	
	public void mainteranceVinReportInit(){
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车保养VIN报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryMainteranceVinReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String areaName = request.getParamValue("areaName");//大区
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String seriesName = request.getParamValue("seriesName");//车系
			String modelName = request.getParamValue("modelName");//车型大类
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);
			bean.setSeriesName(seriesName);
			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryMainteranceVinReport(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车保养VIN报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void mainteranceVinReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String areaName = request.getParamValue("areaName");//大区
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码
			String seriesName = request.getParamValue("seriesName");//车系
			String modelName = request.getParamValue("modelName");//车型大类
			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			String[] head=new String[6];
			head[0]="序号";
			head[1]="维修站代码";
			head[2]="服务中心名称";
			head[3]="一级经销商";
			head[4]="所属大区";
			head[5]="VIN";
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);
			bean.setSeriesName(seriesName);
			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			List<Map<String, Object>> list= dao.getMainteranceVinExcelList(bean);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[6];
						detail[0] = String.valueOf(map.get("NUM"));
						detail[1] = String.valueOf(map.get("DEALER_CODE"));
						detail[2] = String.valueOf(map.get("DEALER_SHORTNAME"));
						detail[3] = String.valueOf(map.get("DEALER_NAME1"));
						detail[4] = String.valueOf(map.get("ORG_NAME"));
						detail[5] = String.valueOf(map.get("VIN"));
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
