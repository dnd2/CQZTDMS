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
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.util.CheckUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;
/**
 * 特殊费用申报表
 */
public class SpecialCostReport {

	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	
	private final String initUrl = "/jsp/report/jcafterservicereport/specialcostreport.jsp";
	
	public void specialCostReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList = dao.getAreaList();//区域
			List<Map<String, Object>> modelList = dao.getModelGroupList(String.valueOf(Constant.WR_MODEL_GROUP_TYPE_01));//车型大类
			List<Map<String, Object>> seriesList = dao.getSeriesList();//区域
			
			act.setOutData("seriesList", seriesList);
			act.setOutData("areaList", areaList);
			act.setOutData("modelList", modelList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用申报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void querySpecialCostReport(){
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
			String feeType = request.getParamValue("feeType");
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
			bean.setFeeType(feeType);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.querySpecialCostReport(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用申报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void specialCostReportExcel(){
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
			String feeType = request.getParamValue("feeType");
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			String[] head=new String[14];
		
			head[1]="申报单位";
			head[2]="服务中心编码";
			head[3]="大区";
			head[4]="费用类型";
			head[5]="特殊费用单号";
			head[6]="申报金额(元)";
			head[7]="结算金额(元)";
			head[8]="VIN码个数";
			head[9]="VIN";
			head[10]="车型";
			head[11]="车系";
			head[12]="单VIN码申报费用";
			head[13]="单VIN码结算费用";
			
			StandardVipApplyManagerBean bean = new StandardVipApplyManagerBean();
			bean.setAreaName(areaName);
			bean.setDealerCode(dealerCode);
			bean.setSeriesName(seriesName);
			bean.setModelName(modelName);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setFeeType(feeType);
			List<Map<String, Object>> list= dao.getExcelList(bean);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[14];
						if(map.get("DEALER_CODE")!=null){
							detail[1] = String.valueOf(map.get("DEALER_CODE"));
							detail[2] = String.valueOf(map.get("DEALER_NAME"));
							detail[3] = String.valueOf(map.get("ROOT_ORG_NAME"));
							detail[4] = String.valueOf(map.get("CODE_DESC"));
							detail[5] = String.valueOf(map.get("FEE_NO"));
							detail[6] = String.valueOf(map.get("DECLARE_SUM1"));
							detail[7] = String.valueOf(map.get("DECLARE_SUM"));
							detail[8] = String.valueOf(map.get("COU"));
							detail[9] = String.valueOf(map.get("VIN"));
							detail[10] = String.valueOf(map.get("GROUP_CODE"));
							detail[11] = String.valueOf(map.get("GROUP_NAME"));
							detail[12] = String.valueOf(map.get("AVERAGE"));
							detail[13] = String.valueOf(map.get("AVERAGE1"));
							list1.add(detail);
						}else{
							detail[0]= "总计";
							detail[5]= String.valueOf(map.get("DECLARE_SUM"));
							list1.add(detail);
						}
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(ActionContext.getContext().getResponse(), request, head, list1, "特殊费用申报明细.xls");
		    }
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用申报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}	
	}
}

