package com.infodms.dms.actions.report.jcafterservicereport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;
/**
 * 车辆维修记录
 */
public class VehicleRepairReport {
	
	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/vehicleRepairReport.jsp";
	private final String MAINTAIN_URL = "/jsp/report/jcafterservicereport/maintainReport.jsp" ;
	private final String WEIXIU_URL = "/jsp/report/jcafterservicereport/weixiu.jsp" ;
	private final String BAOYANG_URL = "/jsp/report/jcafterservicereport/baoyang.jsp" ;
	private final String FUWUHUODONG_URL = "/jsp/report/jcafterservicereport/fuwuhuodong.jsp" ;
	
	public void vehicleRepairReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修记录");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void vehicleRepairReport(){
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
			PageResult<Map<String, Object>> ps = dao.vehicleRepairReport(vin,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修记录");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryVehicleRepairReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String vin = request.getParamValue("vin");
			String[] head=new String[10];
			head[0]="序号";
			head[1]="维修类型";
			head[2]="VIN";
			head[3]="维修站";
			head[4]="维修时间";
			head[5]="行驶里程";
			head[6]="出厂日期";
			head[7]="故障模式";
			head[8]="故障部位";
			head[9]="故障件数量";
			List<Map<String, Object>> list= dao.queryVehicleRepairReportExcel(vin);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[10];
						detail[0] = String.valueOf(map.get("NUM"));
						detail[1] = String.valueOf(map.get("CODE_DESC"));
						detail[2] = String.valueOf(map.get("VIN")==null?"":map.get("VIN"));
						detail[3] = String.valueOf(map.get("DEALER_SHORTNAME")==null?"":map.get("DEALER_SHORTNAME"));
						detail[4] = String.valueOf(map.get("RO_STARTDATE")==null?"":map.get("RO_STARTDATE"));
						detail[5] = String.valueOf(map.get("IN_MILEAGE")==null?"":map.get("IN_MILEAGE"));
						detail[6] = String.valueOf(map.get("PRODUCT_DATE")==null?"":map.get("PRODUCT_DATE"));
						detail[7] = String.valueOf(map.get("TROUBLE_CODE_NAME")==null?"":map.get("TROUBLE_CODE_NAME"));
						detail[8] = String.valueOf(map.get("DAMAGE_AREA_NAME")==null?"":map.get("DAMAGE_AREA_NAME"));
						detail[9] = String.valueOf(map.get("COUNTID")==null?"":map.get("COUNTID"));
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
	
	public void maintainReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(MAINTAIN_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆保养与维修记录表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void maintainReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String vin = request.getParamValue("vin");
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; 	
			PageResult<Map<String, Object>> ps = dao.maintainReport(vin,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);     
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修记录");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void maintainReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try {
		    String fileName = "车辆保养与维修记录表.csv";
			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			act.getResponse().setContentType("Application/text/csv");
			act.getResponse().addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			String vin = request.getParamValue("vin");
			List<Object> titleList = new LinkedList<Object>();
			titleList.add("用户名称");
			titleList.add("VIN");
			titleList.add("购车时间");
			titleList.add("车型");
			titleList.add("用户地址");
			titleList.add("用户电话");
			titleList.add("维修次数");
			titleList.add("保养次数");
			titleList.add("服务活动次数");
			List<Map<String, Object>> list= dao.maintainReportExcel(vin);
			List<List<Object>> hList = new LinkedList<List<Object>>();
			hList.add(titleList) ;
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
			    		List<Object> dataList = new LinkedList<Object>();
			    		dataList.add(map.get("CTM_NAME")==null?"":map.get("CTM_NAME"));
			    		dataList.add(map.get("VIN"));
			    		dataList.add(map.get("PURCHASED_DATE")==null?"":map.get("PURCHASED_DATE"));
			    		dataList.add(map.get("GROUP_NAME")==null?"":map.get("GROUP_NAME"));
			    		dataList.add(map.get("ADDRESS")==null?"":map.get("ADDRESS"));
			    		dataList.add(map.get("PHONE")==null?"":map.get("PHONE"));
			    		dataList.add(map.get("WEIXIU")==null?"":map.get("WEIXIU"));
			    		dataList.add(map.get("BAOYANG")==null?"":map.get("BAOYANG"));
			    		dataList.add(map.get("FUWUHUODONG")==null?"":map.get("FUWUHUODONG"));
						hList.add(dataList);
			    	}
				}
		    }
			os = act.getResponse().getOutputStream();
			CsvWriterUtil.writeCsv(hList, os);
			os.flush();	
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆维修厂与保养记录");
			logger.error(logonUser, e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void weixiu(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String vin = request.getParamValue("vin");	
			List<Map<String,Object>> list = dao.weixiu(vin) ;
			act.setOutData("list", list) ;
			act.setForword(WEIXIU_URL) ;
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修记录");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void baoyang(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String vin = request.getParamValue("vin");	
			List<Map<String,Object>> list = dao.baoyang(vin) ;
			act.setOutData("list", list) ;
			act.setForword(BAOYANG_URL) ;
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆保养记录");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void fuwuhuodong(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String vin = request.getParamValue("vin");	
			List<Map<String,Object>> list = dao.fuwuhuodong(vin) ;
			act.setOutData("list", list) ;
			act.setForword(FUWUHUODONG_URL) ;
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆服务活动记录");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
