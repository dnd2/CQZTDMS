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
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;
/**
 *车辆维修明细报表
 */
public class RepairAmountReport {

	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/repairAmountReport.jsp";
	
	public void repairAmountInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            List<Map<String, Object>> seriesList = dao.getSeriesList();//车系
           
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId()); //取得该用户拥有的产地权限

			act.setOutData("yieldly", yieldly);
			act.setOutData("seriesList", seriesList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修明细报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryRepairAmountReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空				
			String beginTime = request.getParamValue("beginTime"); //生产日期
			String endTime = request.getParamValue("endTime");
			
			String pbeginTime = request.getParamValue("pbeginTime"); //审核通过日期
			String pendTime = request.getParamValue("pendTime");
			
			String bbeginTime = request.getParamValue("bbeginTime");//购车时间			
			String bendTime = request.getParamValue("bendTime");
			
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码			
			
			String yieldly = Utility.getCODES(request.getParamValue("yieldly"));//生产基地
			String seriesName = request.getParamValue("seriesName"); //车系
			String modelName = request.getParamValue("modelName"); //车型
			
			String partYes = request.getParamValue("partYes"); //零件授权  是
			String partNo = request.getParamValue("partNo");//零件授权 否
			String NoNeed = request.getParamValue("NoNeed"); //零件授权 不需要
			
			String bugDesc = request.getParamValue("bugDesc"); //故障描述
			String oldPartName = request.getParamValue("oldPartName");//旧件名称
			String bugName = request.getParamValue("bugName");// 故障名称
			String partName = request.getParamValue("partName");//零件名称
			
			String jobName = request.getParamValue("jobName"); //作业名称
			String vin  = request.getParamValue("vin"); //VIN码
			Long orgId = logonUser.getOrgId();
			
			//生产日期开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			//审核通过日期开始时间和结束时间相同时
			if(null!=pbeginTime&&!"".equals(pbeginTime)&&null!=pendTime&&!"".equals(pendTime)){
				pbeginTime = pbeginTime+" 00:00:00";
				pendTime = pendTime+" 23:59:59";
			}
			//购车时间开始时间和结束时间相同时
			if(null!=bbeginTime&&!"".equals(bbeginTime)&&null!=bendTime&&!"".equals(bendTime)){
				bbeginTime = bbeginTime+" 00:00:00";
				bendTime = bendTime+" 23:59:59";
			}
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryRepairAmountReport
			(beginTime,endTime,pbeginTime,pendTime,bbeginTime,bendTime,dealerCode,yieldly,seriesName,modelName,
			 partYes,partNo,NoNeed,bugDesc,oldPartName,bugName,partName,jobName,vin,orgId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修明细报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getRepairAmountReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {			
			String beginTime = request.getParamValue("beginTime"); //生产日期
			String endTime = request.getParamValue("endTime");
			
			String pbeginTime = request.getParamValue("pbeginTime"); //审核通过日期
			String pendTime = request.getParamValue("pendTime");
			
			String bbeginTime = request.getParamValue("bbeginTime");//购车时间			
			String bendTime = request.getParamValue("bendTime");
			
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码			
			
			String yieldly = Utility.getCODES(request.getParamValue("yieldly"));//生产基地
			String seriesName = request.getParamValue("seriesName"); //车系
			String modelName = request.getParamValue("modelName"); //车型
			
			String partYes = request.getParamValue("partYes"); //零件授权  是
			String partNo = request.getParamValue("partNo");//零件授权 否
			String NoNeed = request.getParamValue("NoNeed"); //零件授权 不需要
			
			String bugDesc = request.getParamValue("bugDesc"); //故障描述
			String oldPartName = request.getParamValue("oldPartName");//旧件名称
			String bugName = request.getParamValue("bugName");// 故障名称
			String partName = request.getParamValue("partName");//零件名称
			
			String jobName = request.getParamValue("jobName"); //作业名称
			String vin  = request.getParamValue("vin"); //VIN码
			Long orgId = logonUser.getOrgId();
			 
			//生产日期开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			//审核通过日期开始时间和结束时间相同时
			if(null!=pbeginTime&&!"".equals(pbeginTime)&&null!=pendTime&&!"".equals(pendTime)){
				pbeginTime = pbeginTime+" 00:00:00";
				pendTime = pendTime+" 23:59:59";
			}
			//购车时间开始时间和结束时间相同时
			if(null!=bbeginTime&&!"".equals(bbeginTime)&&null!=bendTime&&!"".equals(bendTime)){
				bbeginTime = bbeginTime+" 00:00:00";
				bendTime = bendTime+" 23:59:59";
			}
						
			String[] head=new String[38];
			head[0]="索赔申请单号";
			head[1]="车型";
			head[2]="VIN码";
			head[3]="发动机号";
			head[4]="生产日期";
			head[5]="购车日期";
			head[6]="工单开始日期";
			head[7]="核通过日期";
			head[8]="行驶里程";
			head[9]="索赔类型";
			head[10]="质损类型";
			head[11]="故障描述";
			head[12]="故障原因";
			head[13]="维修措施";
			head[14]="申请备注";
			head[15]="故障名称";
			head[16]="顾客问题-ccc代码";
			head[17]="零件故障";
			head[18]="作业名称";
			head[19]="工时金额";
			head[20]="作业累积";
			head[21]="整单追加工时";
			head[22]="旧件编码";
			head[23]="旧件名称";
			head[24]="换件数量";
			head[25]="零件金额";
			head[26]="供应商";
			head[27]="分销中心";
			head[28]="经销商代码";
			head[29]="经销商名称";
			head[30]="接待员";
			head[31]="维修站联系人";
			head[32]="联系电话";
			head[33]="送修人";
			head[34]="送修人电话";
			head[35]="零件是否授权";
			
			List<Map<String, Object>> list= dao.queryRepairAmountReportExcel
			(beginTime,endTime,pbeginTime,pendTime,bbeginTime,bendTime,dealerCode,yieldly,seriesName,modelName,
					 partYes,partNo,NoNeed,bugDesc,oldPartName,bugName,partName,jobName,vin,orgId);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[38];
						detail[0] = CommonUtils.checkNull(String.valueOf(map.get("CLAIM_NO")));
						detail[1] = CommonUtils.checkNull(String.valueOf(map.get("MODEL_NAME")));
						detail[2] = CommonUtils.checkNull(String.valueOf(map.get("VIN")));
						detail[3] = CommonUtils.checkNull(String.valueOf(map.get("ENGINE_NO")));
						detail[4] = CommonUtils.checkNull(String.valueOf(map.get("PRODUCT_DATE")));
						detail[5] = CommonUtils.checkNull(String.valueOf(map.get("PURCHASED_DATE")));
						detail[6] = CommonUtils.checkNull(String.valueOf(map.get("CREATE_DATE")));
						detail[7] = CommonUtils.checkNull(String.valueOf(map.get("AUDITING_DATE")));
						detail[8] = CommonUtils.checkNull(String.valueOf(map.get("IN_MILEAGE")));
						detail[9] = CommonUtils.checkNull(String.valueOf(map.get("CLAIM_TYPE")));
						detail[10] = CommonUtils.checkNull(String.valueOf(map.get("DEGRADATION_TYPE")));
						detail[11] = CommonUtils.checkNull(String.valueOf(map.get("TROUBLE_DESCS")));
						detail[12] = CommonUtils.checkNull(String.valueOf(map.get("TROUBLE_REASON")));
						detail[13] = CommonUtils.checkNull(String.valueOf(map.get("REPAIR_METHOD")));
						detail[14] = CommonUtils.checkNull(String.valueOf(map.get("REMARK2")));
						detail[15] = CommonUtils.checkNull(String.valueOf(map.get("TROUBLE_CODE_NAME")));
						detail[16] = CommonUtils.checkNull(String.valueOf(map.get("TROUBLE_TYPE")));
						detail[17] = CommonUtils.checkNull(String.valueOf(map.get("REMARK")));
						detail[18] = CommonUtils.checkNull(String.valueOf(map.get("WR_LABOURNAME")));
						detail[19] = CommonUtils.checkNull(String.valueOf(map.get("LABOUR_AMOUNT")));
						detail[20] = CommonUtils.checkNull(String.valueOf(map.get("FIRST_PART")));
						detail[21] = CommonUtils.checkNull(String.valueOf(map.get("APPENDLABOUR_NUM")));
						detail[22] = CommonUtils.checkNull(String.valueOf(map.get("DOWN_PART_CODE")));
						detail[23] = CommonUtils.checkNull(String.valueOf(map.get("DOWN_PART_NAME")));
						detail[24] = CommonUtils.checkNull(String.valueOf(map.get("QUANTITY")));
						detail[25] = CommonUtils.checkNull(String.valueOf(map.get("AMOUNT")));
						detail[26] = CommonUtils.checkNull(String.valueOf(map.get("PRODUCER_NAME")));
						detail[27] = CommonUtils.checkNull(String.valueOf(map.get("ROOT_ORG_NAME")));
						detail[28] = CommonUtils.checkNull(String.valueOf(map.get("DEALER_CODE")));
						detail[29] = CommonUtils.checkNull(String.valueOf(map.get("DEALER_NAME")));
						detail[30] = CommonUtils.checkNull(String.valueOf(map.get("SERVE_ADVISOR")));
						detail[31] = CommonUtils.checkNull(String.valueOf(map.get("LINK_MAN")));
						detail[32] = CommonUtils.checkNull(String.valueOf(map.get("PHONE")));
						detail[33] = CommonUtils.checkNull(String.valueOf(map.get("DELIVERER")));
						detail[34] = CommonUtils.checkNull(String.valueOf(map.get("DELIVERER_PHONE")));
						detail[35] = CommonUtils.checkNull(String.valueOf(map.get("IS_AGREE")));
						//detail[11] = String.valueOf(map.get("DAMAGE_TYPE_NAME"));
						//detail[12] = String.valueOf(map.get("DAMAGE_DEGREE_NAME"));
											
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(ActionContext.getContext().getResponse(), request, head, list1,"车辆维修明细报表.xls");
		    }
		    String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId()); //取得该用户拥有的产地权限
			act.setOutData("yieldly", yieldlys);
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修明细报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
}
