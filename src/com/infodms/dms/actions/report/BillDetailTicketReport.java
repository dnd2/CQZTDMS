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
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.report.BillDetailTicketDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;


public class BillDetailTicketReport {
	private Logger logger = Logger.getLogger(BillDetailTicketReport.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private BillDetailTicketDao dao =BillDetailTicketDao.getInstance();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	
	private static final String BillDetailTicket_URL = "/jsp/report/billDetailTicketReport.jsp";
	
	
	public void getBillDetailTicketReport(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			String modelId4 = CommonUtils.checkNull(request.getParamValue("modelId4"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String myOrgId = CommonUtils.checkNull(request.getParamValue("myOrgId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String dealerId2 = CommonUtils.checkNull(request.getParamValue("dealerId2"));
			String mydealerId = CommonUtils.checkNull(request.getParamValue("mydealerId"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("myGroupCode"));	//配置条件
			
			//只有大区范围限制	2012-08-29 韩晓宇
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString();
			}
			//添加业务范围限制	2012-08-29 韩晓宇
			if(areaId == null || "".equals(areaId)) {
				Long poseId = logonUser.getPoseId();
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
				for(int i=0; i<areaList.size(); i++) {
					areaId += areaList.get(i).get("AREA_ID");
					if(i != areaList.size()-1){
						areaId +=  ",";
					} 
				}
			}
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("modelId", modelId);
			map.put("modelId4", modelId4);
			map.put("orgId", orgId);
			map.put("myOrgId", myOrgId);
			map.put("dealerId", dealerId);
			map.put("dealerId2", dealerId2);
			map.put("mydealerId", mydealerId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			
			List<Map<String, Object>> list_report = dao.getBillDetailTicketSelect(map);
			act.setOutData("list_report", list_report);
			act.setOutData("beginTime", beginTime);
			act.setOutData("endTime", endTime);
			act.setForword(BillDetailTicket_URL);
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"启票明细表");
			logger.error(logonUser,e1);
			act.setActionReturn(e1);
		}
	}
	
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
			String modelId4 = CommonUtils.checkNull(request.getParamValue("modelId4"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String myOrgId = CommonUtils.checkNull(request.getParamValue("myOrgId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String dealerId2 = CommonUtils.checkNull(request.getParamValue("dealerId2"));
			String mydealerId = CommonUtils.checkNull(request.getParamValue("mydealerId"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("myGroupCode"));	//配置条件
			
			//只有大区范围限制	2012-08-29 韩晓宇
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString();
			}
			//添加业务范围限制	2012-08-29 韩晓宇
			if(areaId == null || "".equals(areaId)) {
				Long poseId = logonUser.getPoseId();
				List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
				for(int i=0; i<areaList.size(); i++) {
					areaId += areaList.get(i).get("AREA_ID");
					if(i != areaList.size()-1){
						areaId +=  ",";
					} 
				}
			}
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("modelId", modelId);
			map.put("modelId4", modelId4);
			map.put("orgId", orgId);
			map.put("myOrgId", myOrgId);
			map.put("dealerId", dealerId);
			map.put("dealerId2", dealerId2);
			map.put("mydealerId", mydealerId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("areaId", areaId);
			map.put("groupCode", groupCode);
			
			// 导出的文件名
			String fileName = "启票明细表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<Map<String, Object>> listDetail = dao.getBillDetailTicketSelect(map);
			int len = listDetail.size();
			int y =0;
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet =workbook.createSheet("启票明细表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			sheet.mergeCells(0, y, 8, y);
			sheet.addCell(new Label(0, y, "启票明细表",wcf));
			
			++y;
			sheet.mergeCells(0, y, 8, y);
			sheet.addCell(new Label(0, y, "起止时间："+beginTime+"--"+endTime));
			
			++y;
			sheet.addCell(new Label(0, y, "大区"));
			sheet.addCell(new Label(1, y, "省份"));
			sheet.addCell(new Label(2, y, "一级经销商"));
			sheet.addCell(new Label(3, y, "二级经销商"));
			sheet.addCell(new Label(4, y, "车系"));
			sheet.addCell(new Label(5, y, "车型编码"));
			sheet.addCell(new Label(6, y, "车型名称"));
			sheet.addCell(new Label(7, y, "业务范围"));
			sheet.addCell(new Label(8, y, "数量"));
			
			
			int tba =0;
			if(listDetail!=null&&len!=0){
				int sba =0 ;
				int sba_rn = 0;
				for(int i=0;i<len;i++){
					int ba = Integer.parseInt(listDetail.get(i).get("BILLAMOUNT").toString());
					if(i==0){
						sba = ba;
						sba_rn = ba ;
						tba = ba;
						
						++y;
						sheet.addCell(new Label(0, y, listDetail.get(i).get("ORG_NAME")==null?"":listDetail.get(i).get("ORG_NAME").toString()));
						sheet.addCell(new Label(1, y, listDetail.get(i).get("REGION_NAME")==null?"":listDetail.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new Label(2, y, listDetail.get(i).get("ROOT_DEALER_NAME")==null?"":listDetail.get(i).get("ROOT_DEALER_NAME").toString()));
						sheet.addCell(new Label(3, y, listDetail.get(i).get("DEALER_NAME")==null?"":listDetail.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new Label(4, y, listDetail.get(i).get("SERIES_NAME")==null?"":listDetail.get(i).get("SERIES_NAME").toString()));
						sheet.addCell(new Label(5, y, listDetail.get(i).get("PACKAGE_CODE")==null?"":listDetail.get(i).get("PACKAGE_CODE").toString()));
						sheet.addCell(new Label(6, y, listDetail.get(i).get("PACKAGE_NAME")==null?"":listDetail.get(i).get("PACKAGE_NAME").toString()));
						sheet.addCell(new Label(7, y, listDetail.get(i).get("AREA_NAME")==null?"":listDetail.get(i).get("AREA_NAME").toString()));
						sheet.addCell(new Label(8, y, listDetail.get(i).get("BILLAMOUNT")==null?"":listDetail.get(i).get("BILLAMOUNT").toString()));
						
					}else{
						String before = listDetail.get(i-1).get("REGION_NAME").toString();
						String after = listDetail.get(i).get("REGION_NAME").toString();
						
						String before11 = listDetail.get(i-1).get("ORG_NAME").toString();
						String after11 = listDetail.get(i).get("ORG_NAME").toString();
						if(after.equals(before)&&after11.equals(before11)){
							sba += ba;
							sba_rn += ba ;
							tba += ba;
							
							++y;
							sheet.addCell(new Label(0, y, listDetail.get(i).get("ORG_NAME")==null?"":listDetail.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, listDetail.get(i).get("REGION_NAME")==null?"":listDetail.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, listDetail.get(i).get("ROOT_DEALER_NAME")==null?"":listDetail.get(i).get("ROOT_DEALER_NAME").toString()));
							sheet.addCell(new Label(3, y, listDetail.get(i).get("DEALER_NAME")==null?"":listDetail.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new Label(4, y, listDetail.get(i).get("SERIES_NAME")==null?"":listDetail.get(i).get("SERIES_NAME").toString()));
							sheet.addCell(new Label(5, y, listDetail.get(i).get("PACKAGE_CODE")==null?"":listDetail.get(i).get("PACKAGE_CODE").toString()));
							sheet.addCell(new Label(6, y, listDetail.get(i).get("PACKAGE_NAME")==null?"":listDetail.get(i).get("PACKAGE_NAME").toString()));
							sheet.addCell(new Label(7, y, listDetail.get(i).get("AREA_NAME")==null?"":listDetail.get(i).get("AREA_NAME").toString()));
							sheet.addCell(new Label(8, y, Integer.valueOf(listDetail.get(i).get("BILLAMOUNT").toString())==0?"":listDetail.get(i).get("BILLAMOUNT").toString()));
						
						}else{
							++y;
							sheet.mergeCells(0, y, 7, y);
							sheet.addCell(new Label(0, y, "省份合计",wcf));
							if(sba==0){
								sheet.addCell(new Label(8, y, ""));
							}
							else{
								sheet.addCell(new Label(8, y, String.valueOf(sba)));
							}
							
							
							sba =ba;
							tba +=ba;
							
							//开始
							String before_rn = listDetail.get(i-1).get("ORG_NAME").toString();
							String after_rn = listDetail.get(i).get("ORG_NAME").toString();
							if(after_rn.equals(before_rn)){
								sba_rn += ba ;
							}else{
								++y;
								sheet.mergeCells(0, y, 7, y);
								sheet.addCell(new Label(0, y, "大区合计",wcf));
								if(sba_rn==0){
									sheet.addCell(new Label(8, y, ""));
								}
								else{
									sheet.addCell(new Label(8, y, String.valueOf(sba_rn)));
								}
								
								
								sba_rn = 0;
								sba_rn +=ba;
							}
							//结束
							++y;
							sheet.addCell(new Label(0, y, listDetail.get(i).get("ORG_NAME")==null?"":listDetail.get(i).get("ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, listDetail.get(i).get("REGION_NAME")==null?"":listDetail.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, listDetail.get(i).get("ROOT_DEALER_NAME")==null?"":listDetail.get(i).get("ROOT_DEALER_NAME").toString()));
							sheet.addCell(new Label(3, y, listDetail.get(i).get("DEALER_NAME")==null?"":listDetail.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new Label(4, y, listDetail.get(i).get("SERIES_NAME")==null?"":listDetail.get(i).get("SERIES_NAME").toString()));
							sheet.addCell(new Label(5, y, listDetail.get(i).get("PACKAGE_CODE")==null?"":listDetail.get(i).get("PACKAGE_CODE").toString()));
							sheet.addCell(new Label(6, y, listDetail.get(i).get("PACKAGE_NAME")==null?"":listDetail.get(i).get("PACKAGE_NAME").toString()));
							sheet.addCell(new Label(7, y, listDetail.get(i).get("AREA_NAME")==null?"":listDetail.get(i).get("AREA_NAME").toString()));
							sheet.addCell(new Label(8, y, Integer.valueOf(listDetail.get(i).get("BILLAMOUNT").toString())==0?"":listDetail.get(i).get("BILLAMOUNT").toString()));
							
						}
						if(i==len-1){
							++y;
							sheet.mergeCells(0, y, 7, y);
							sheet.addCell(new Label(0, y, "省份合计",wcf));
							if(sba==0){
								sheet.addCell(new Label(8, y, ""));
							}
							else{
								sheet.addCell(new Label(8, y, String.valueOf(sba)));
							}
							
							
							++y;
							sheet.mergeCells(0, y, 7, y);
							sheet.addCell(new Label(0, y, "大区合计",wcf));
							if(sba_rn==0){
								sheet.addCell(new Label(8, y, ""));
							}
							else{
								sheet.addCell(new Label(8, y, String.valueOf(sba_rn)));
							}
						}
					}
				}
			}
			++y;
			sheet.mergeCells(0, y, 7, y);
			sheet.addCell(new Label(0, y, "合计",wcf));
			if(tba==0){
				sheet.addCell(new Label(8, y, ""));
			}
			else{
				sheet.addCell(new Label(8, y, String.valueOf(tba)));
			}
			
			
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"启票明细表");
			logger.error(logonUser,e1);
			act.setActionReturn(e1);
		}
	}
}
