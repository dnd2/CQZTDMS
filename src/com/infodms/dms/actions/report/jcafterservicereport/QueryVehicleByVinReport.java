package com.infodms.dms.actions.report.jcafterservicereport;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

/**
 * VIN码查车辆信息
 */
public class QueryVehicleByVinReport {
	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/queryVehicleByVinReport.jsp";
	
	public void queryVehicleByVinReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"VIN码查车辆信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryVehicleByVinReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String vin = request.getParamValue("vin");//车型大类
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryVehicleByVinReport(vin,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"VIN码查车辆信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryVehicleByVinReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String vin = request.getParamValue("vin");
			String[] head=new String[10];
			head[0]="序号";
			head[1]="VIN";
			head[2]="车型";
			head[3]="发送机";
			head[4]="系统最大里程数";
			head[5]="生产日期";
			head[6]="购车日期";
			head[7]="三包规则";
			head[8]="状态";
			head[9]="车辆颜色";
			List<Map<String, Object>> list= dao.queryVehicleByVinReportExcelList(vin);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[10];
						detail[0] = String.valueOf(map.get("NUM"));
						detail[1] = String.valueOf(map.get("VIN"));
						detail[2] = String.valueOf(map.get("MODEL_CODE")==null?"":map.get("MODEL_CODE"));
						detail[3] = String.valueOf(map.get("ENGINE_NO")==null?"":map.get("ENGINE_NO"));
						detail[4] = String.valueOf(map.get("MILEAGE")==null?"":map.get("MILEAGE"));
						detail[5] = String.valueOf(map.get("PRODUCT_DATE")==null?"":map.get("PRODUCT_DATE"));
						detail[6] = String.valueOf(map.get("PURCHASED_DATE")==null?"":map.get("PURCHASED_DATE"));
						detail[7] = String.valueOf(map.get("CLAIM_TACTICS_ID")==null?"":map.get("CLAIM_TACTICS_ID"));
						detail[8] = String.valueOf(map.get("CODE_DESC")==null?"":map.get("CODE_DESC"));
						detail[9] = String.valueOf(map.get("COLOR_NAME")==null?"":map.get("COLOR_NAME"));
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
