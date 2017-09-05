package com.infodms.dms.actions.report;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.FleetActTjDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class FleetActTjReport {
	private Logger logger = Logger.getLogger(FleetActTjReport.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private FleetActTjDao dao = FleetActTjDao.getInstance();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private static final String  FleetActTj_URL="/jsp/report/FleetActTjReport.jsp";
	
	
	public void getFleetActTj(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String contractNo = CommonUtils.checkNull(request.getParamValue("contractNo"));
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String companyId = CommonUtils.checkNull(request.getParamValue("companyId"));
			String checkStatus = CommonUtils.checkNull(request.getParamValue("checkStatus"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("contractNo", contractNo);
			map.put("fleetName", fleetName);
			map.put("fleetType", fleetType);
			map.put("vin", vin);
			map.put("groupCode", groupCode);
			map.put("companyId", companyId);
			map.put("checkStatus", checkStatus);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			
			
			List<Map<String, Object>> list_FleetActTj = dao.getFleetActTjSelect(map);
			act.setOutData("list_FleetActTj", list_FleetActTj);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setForword(FleetActTj_URL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户DMS实销信息统计表");
			logger.error(logonUser,e1);
			act.setActionReturn(e1);
		}
	}
	
	public void Download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String contractNo = CommonUtils.checkNull(request.getParamValue("contractNo"));
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String companyId = CommonUtils.checkNull(request.getParamValue("companyId"));
			String checkStatus = CommonUtils.checkNull(request.getParamValue("checkStatus"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("contractNo", contractNo);
			map.put("fleetName", fleetName);
			map.put("fleetType", fleetType);
			map.put("vin", vin);
			map.put("groupCode", groupCode);
			map.put("companyId", companyId);
			map.put("checkStatus", checkStatus);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			
			String fileName="集团客户DMS实销信息统计表.xls";
					fileName = new String(fileName.getBytes("GB2312"),"ISO8859-1");
					response.setContentType("Application/text/csv");
					response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
					
					List<Map<String, Object>> list = dao.getFleetActTjSelect(map);
					int len = list.size();
					int y = 0;
					
					OutputStream os = response.getOutputStream();
					WritableWorkbook workbook = Workbook.createWorkbook(os);
					WritableSheet sheet = workbook.createSheet("集团客户DMS实销信息统计表",0);
					WritableCellFormat wcf = new WritableCellFormat();
					wcf.setAlignment(Alignment.CENTRE);
					sheet.mergeCells(0, y, 19, y);
					sheet.addCell(new Label(0, y, "集团客户DMS实销信息统计表",wcf));
					
					++y;
					sheet.mergeCells(0, y, 19, y);
					sheet.addCell(new Label(0, y, "上报日期："+String.valueOf(startDate)+"--"+String.valueOf(endDate)));
					
					++y;
					sheet.addCell(new Label(0, y, ""));
					sheet.addCell(new Label(1, y, ""));
					sheet.addCell(new Label(2, y, ""));
					sheet.addCell(new Label(3, y, ""));
					sheet.addCell(new Label(4, y, ""));
					sheet.addCell(new Label(5, y, ""));
					sheet.addCell(new Label(6, y, ""));
					sheet.addCell(new Label(7, y, ""));
					sheet.addCell(new Label(8, y, ""));
					sheet.addCell(new Label(9, y, ""));
					sheet.addCell(new Label(10, y, ""));
					sheet.addCell(new Label(11, y, ""));
					sheet.addCell(new Label(12, y, ""));
					sheet.addCell(new Label(13, y, ""));
					sheet.addCell(new Label(14, y, ""));
					sheet.addCell(new Label(15, y, ""));
					sheet.addCell(new Label(16, y, ""));
					sheet.addCell(new Label(17, y, ""));
					sheet.addCell(new Label(18, y, ""));
					sheet.addCell(new Label(19, y, ""));
					
					sheet.addCell(new Label(0, y, "大区"));
					sheet.addCell(new Label(1, y, "省"));
					sheet.addCell(new Label(2, y, "市"));
					sheet.addCell(new Label(3, y, "县"));
					sheet.addCell(new Label(4, y, "A/B网"));
					sheet.addCell(new Label(5, y, "销售日期"));
					sheet.addCell(new Label(6, y, "生产日期"));
					sheet.addCell(new Label(7, y, "启票日期"));
					sheet.addCell(new Label(8, y, "上级单位"));
					sheet.addCell(new Label(9, y, "销售单位"));
					sheet.addCell(new Label(10, y, "客户名称"));
					sheet.addCell(new Label(11, y, "一级客户类型"));
					sheet.addCell(new Label(12, y, "二级客户类型"));
					sheet.addCell(new Label(13, y, "合同编号"));
					sheet.addCell(new Label(14, y, "车型类别"));
					sheet.addCell(new Label(15, y, "车型系列"));
					sheet.addCell(new Label(16, y, "车型编码"));
					sheet.addCell(new Label(17, y, "状态"));
					sheet.addCell(new Label(18, y, "颜色"));
					sheet.addCell(new Label(19, y, "底盘号"));
					
					for (int i = 0; i < len; i++) {
						++y;
						sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null?"":list.get(i).get("ROOT_ORG_NAME").toString()));
						sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null?"":list.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new Label(2, y, list.get(i).get("CITY_NAME")==null?"":list.get(i).get("CITY_NAME").toString()));
						sheet.addCell(new Label(3, y, list.get(i).get("TOWN")==null?"":list.get(i).get("TOWN").toString()));
						sheet.addCell(new Label(4, y, list.get(i).get("AREA_NAME")==null?"":list.get(i).get("AREA_NAME").toString()));
						sheet.addCell(new Label(5, y, list.get(i).get("SALES_DATE")==null?"":list.get(i).get("SALES_DATE").toString()));
						sheet.addCell(new Label(6, y, list.get(i).get("PRODUCT_DATE")==null?"":list.get(i).get("PRODUCT_DATE").toString()));
						sheet.addCell(new Label(7, y, list.get(i).get("INVOICE_DATE")==null?"":list.get(i).get("INVOICE_DATE").toString()));
						sheet.addCell(new Label(8, y, list.get(i).get("ROOT_DEALER_NAME")==null?"":list.get(i).get("ROOT_DEALER_NAME").toString()));
						sheet.addCell(new Label(9, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
						sheet.addCell(new Label(10, y, list.get(i).get("CTM_NAME")==null?"":list.get(i).get("CTM_NAME").toString()));
						sheet.addCell(new Label(11, y, list.get(i).get("PARTFLEET_TYPE")==null?"":list.get(i).get("PARTFLEET_TYPE").toString()));
						sheet.addCell(new Label(12, y, list.get(i).get("FLEET_TYPE")==null?"":list.get(i).get("FLEET_TYPE").toString()));
						sheet.addCell(new Label(13, y, list.get(i).get("CONTRACT_NO")==null?"":list.get(i).get("CONTRACT_NO").toString()));
						sheet.addCell(new Label(14, y, list.get(i).get("SERNAME")==null?"":list.get(i).get("SERNAME").toString()));
						sheet.addCell(new Label(15, y, list.get(i).get("MODNAME")==null?"":list.get(i).get("MODNAME").toString()));
						sheet.addCell(new Label(16, y, list.get(i).get("MODCODE")==null?"":list.get(i).get("MODCODE").toString()));
						sheet.addCell(new Label(17, y, list.get(i).get("PACKNAME")==null?"":list.get(i).get("PACKNAME").toString()));
						sheet.addCell(new Label(18, y, list.get(i).get("COLOR")==null?"":list.get(i).get("COLOR").toString()));
						sheet.addCell(new Label(19, y, list.get(i).get("VIN")==null?"":list.get(i).get("VIN").toString()));
					}
			
					workbook.write();
					workbook.close();
					os.flush();
					os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户DMS实销信息统计表");
			logger.error(logonUser,e1);
			act.setActionReturn(e1);
		}
	}
}
