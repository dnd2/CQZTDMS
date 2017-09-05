package com.infodms.dms.actions.crm.report;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.report.CreateCtmManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class CreateCtmManage {
	public Logger logger = Logger.getLogger(ClueReportTotal.class);
	CreateCtmManageDao dao  = CreateCtmManageDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
    private ResponseWrapper response = act.getResponse();
	private final String initUrl = "/jsp/crm/report/createCtmManageInit.jsp";
	private final String queryUrl = "/jsp/crm/report/createCtmManage.jsp";
	private final String queryDealerUrl = "/jsp/crm/report/createDealerCtmManage.jsp";
	public void doInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String today=sdf.format(new Date());
            act.setOutData("today", today);
            act.setOutData("dutyType",logonUser.getDutyType());
            //act.setOutData("dealerId",logonUser.getDealerId());
            act.setOutData("orgId",logonUser.getOrgId());
            String longUserPose=CommonUtils.getPoseRank(logonUser);
            if("60281001".equals(longUserPose) ||"60281002".equals(longUserPose)){
            	Map<String,String> map =new HashMap<String,String>();
            	map.put("dealerId", logonUser.getDealerId().toString());
            	map.put("userId", logonUser.getUserId().toString());
        		PageResult<Map<String, Object>> ps = dao.getDealerCode(map,10, 1);
            	 if(ps.getTotalPages()> 0){
            		act.setOutData("flag", "flag");
            	 }
			}
            act.setOutData("download", "true");
            
            act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增潜客管理报表页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜客管理报表
	 */
	public void doSearch(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
            if(CommonUtils.isNullString(dealerCode)){
            	dealerCode = CommonUtils.checkNull(request.getParamValue("cdealerCode")) ;
            }
            String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
			
			Map<String,String> map=new HashMap<String,String>();			
			 
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("seriesId", seriesId);
			map.put("dealerId", logonUser.getDealerId());
			map.put("dutyType", logonUser.getDutyType());
			map.put("orgId", logonUser.getOrgId().toString());
			map.put("userId", logonUser.getUserId().toString());
			map.put("dealerCode", dealerCode);
			if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){
				
				List<Map<String,Object>> dealerCtmManageList=null;
				dealerCtmManageList=dao.getDealerCtmManageInfo(map);
				act.setOutData("startDate",startDate);
				act.setOutData("endDate",endDate);
				act.setOutData("dealerId",logonUser.getDealerId());
				act.setOutData("seriesId",seriesId);
				act.setOutData("dealerCtmManageList",dealerCtmManageList);
				request.setAttribute("ctmManageList",dealerCtmManageList);
				System.out.println("dealerCtmManageList==="+dealerCtmManageList);
	            act.setForword(queryDealerUrl);
			}else{
				
				List<Map<String,Object>> ctmManageList=null;
				ctmManageList=dao.getCtmManageInfo(map);
				act.setOutData("startDate",startDate);
				act.setOutData("endDate",endDate);
				act.setOutData("dealerCode",dealerCode);
				act.setOutData("seriesId",seriesId);
				act.setOutData("ctmManageList",ctmManageList);
				request.setAttribute("ctmManageList",ctmManageList);
				System.out.println("ctmManageList==="+ctmManageList);
	            act.setForword(queryUrl);
			}

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增潜客管理报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 新增潜客管理报表 下载
	 */

	public void doDownload() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 导出的文件名
			String fileName = "新增潜客管理报表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
            if(CommonUtils.isNullString(dealerCode)){
            	dealerCode = CommonUtils.checkNull(request.getParamValue("cdealerCode")) ;
            }
            String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
            
            Map<String, String> paraMap = new HashMap<String, String>();

            paraMap.put("startDate", startDate);
            paraMap.put("endDate", endDate);
            paraMap.put("dealerCode", dealerCode);
            paraMap.put("dutyType", logonUser.getDutyType());
            paraMap.put("orgId", logonUser.getOrgId().toString());
            paraMap.put("dealerId", logonUser.getDealerId());
            paraMap.put("dutyType", logonUser.getDutyType());
            paraMap.put("userId", logonUser.getUserId().toString());
            OutputStream os = response.getOutputStream();
    		WritableWorkbook workbook = Workbook.createWorkbook(os);
			List<Map<String, Object>> list =null; 
			if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){
				list= dao.getDealerCtmManageInfo(paraMap);
				getDealerExcel(list,workbook);
			}else{
				list=dao.getCtmManageInfo(paraMap);
				getExcel(list, workbook);
			}
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增潜客管理报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 车厂端查出经销商明细
	 */
	
	public void getDealerList(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            String dealer_id = CommonUtils.checkNull(request.getParamValue("dealer_id")) ;
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
           
            String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
			Map<String,String> map=new HashMap<String,String>();			
			 
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("userId", logonUser.getUserId().toString());
			map.put("dealerId", dealer_id); 
			map.put("seriesId", seriesId);
			
			List<Map<String,Object>> dealerCtmManageList=null;
			dealerCtmManageList=dao.getDealerCtmManageInfo(map);
			act.setOutData("dealerCtmManageList",dealerCtmManageList);
			request.setAttribute("dealerCtmManageList",dealerCtmManageList);
            
            act.setForword(queryDealerUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"线索来源效率分析报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商导出
	 * @param list
	 * @param workbook
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void getDealerExcel(List<Map<String, Object>> list,WritableWorkbook workbook) 
								throws IOException, RowsExceededException, WriteException{
		String codeID="";
		WritableSheet sheet = workbook.createSheet("新增潜客管理报表", 0);
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		DisplayFormat displayFormat = NumberFormats.TEXT;
		WritableCellFormat format = new WritableCellFormat(wf,displayFormat);
		format.setAlignment(jxl.format.Alignment.LEFT);
		format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.NONE); 
		int y = 0;
		sheet.addCell(new Label(0, y, "经销商代码"));
		sheet.addCell(new Label(1, y, "经销商名称"));
		sheet.addCell(new Label(2, y, "销售组"));
		sheet.addCell(new Label(3, y, "销售顾问"));
		sheet.addCell(new Label(4, y, "客户总数"));
		sheet.addCell(new Label(5, y, "战败客户"));
		sheet.addCell(new Label(6, y, "占比"));
		sheet.addCell(new Label(7, y, "失效客户"));
		sheet.addCell(new Label(8, y, "占比"));
		sheet.addCell(new Label(9, y, "保有客户"));
		sheet.addCell(new Label(10, y, "占比"));
		sheet.addCell(new Label(11, y, "有望客户"));
		sheet.addCell(new Label(12, y, "占比"));
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				++y;
				sheet.addCell(new Label(0, y, CommonUtils.checkNull(list.get(i) .get("DEALER_CODE"))));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i) .get("DEALER_SHORTNAME"))));
				sheet.addCell(new Label(2, y, CommonUtils.checkNull(list.get(i) .get("GROUP_NAME"))));
				sheet.addCell(new Label(3, y, CommonUtils.checkNull(list.get(i) .get("NAME"))));
				sheet.addCell(new Label(4, y, CommonUtils.checkNull(list.get(i) .get("TOTAL_ACCOUNT"))));
				sheet.addCell(new Label(5, y, CommonUtils.checkNull(list.get(i) .get("ZBKH_ACCOUNT"))));
				sheet.addCell(new Label(6, y, CommonUtils.checkNull(list.get(i).get("ZBKH_RATE"))));
				sheet.addCell(new Label(7, y, CommonUtils.checkNull(list.get(i) .get("SXKH_ACCOUNT"))));
				sheet.addCell(new Label(8, y, CommonUtils.checkNull(list.get(i).get("SXKH_RATE"))));
				sheet.addCell(new Label(9, y, CommonUtils.checkNull(list.get(i) .get("BYKH_ACCOUNT"))));
				sheet.addCell(new Label(10, y, CommonUtils.checkNull(list.get(i).get("BYKH_RATE"))));
				sheet.addCell(new Label(11, y, CommonUtils.checkNull(list.get(i) .get("YWKH_ACCOUNT"))));
				sheet.addCell(new Label(12, y, CommonUtils.checkNull(list.get(i).get("YWKH_RATE"))));
				}
		}
		++y;
	}

	/**
	 * 车厂导出
	 * @param list
	 * @param workbook
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void getExcel(List<Map<String, Object>> list, WritableWorkbook workbook) 
					throws IOException, RowsExceededException, WriteException {
		WritableSheet sheet = workbook.createSheet("新增潜客管理报表", 0);
		int y = 0;
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		DisplayFormat displayFormat = NumberFormats.TEXT;
		WritableCellFormat format = new WritableCellFormat(wf,displayFormat);
		format.setAlignment(jxl.format.Alignment.LEFT);
		format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.NONE); 
		sheet.addCell(new Label(0, y, "大区"));
		sheet.addCell(new Label(1, y, "省份"));
		sheet.addCell(new Label(2, y, "代码"));
		sheet.addCell(new Label(3, y, "简称"));
		sheet.addCell(new Label(4, y, "客户总数"));
		sheet.addCell(new Label(5, y, "战败客户"));
		sheet.addCell(new Label(6, y, "占比"));
		sheet.addCell(new Label(7, y, "失效客户"));
		sheet.addCell(new Label(8, y, "占比"));
		sheet.addCell(new Label(9, y, "保有客户"));
		sheet.addCell(new Label(10, y, "占比"));
		sheet.addCell(new Label(11, y, "有望客户"));
		sheet.addCell(new Label(12, y, "占比"));
		 

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				// sheet.mergeCells(0, y, 1, y);
				++y;
				sheet.addCell(new Label(0, y, CommonUtils.checkNull(list.get(i) .get("ROOT_ORG_NAME"))));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i) .get("PQ_ORG_NAME"))));
				sheet.addCell(new Label(2, y, CommonUtils.checkNull(list.get(i) .get("DEALER_CODE"))));
				sheet.addCell(new Label(3, y, CommonUtils.checkNull(list.get(i) .get("DEALER_SHORTNAME"))));
				sheet.addCell(new Label(4, y, CommonUtils.checkNull(list.get(i) .get("TOTAL_ACCOUNT"))));
				sheet.addCell(new Label(5, y, CommonUtils.checkNull(list.get(i) .get("ZBKH_ACCOUNT"))));
				sheet.addCell(new Label(6, y, CommonUtils.checkNull(list.get(i).get("ZBKH_RATE"))));
				sheet.addCell(new Label(7, y, CommonUtils.checkNull(list.get(i) .get("SXKH_ACCOUNT"))));
				sheet.addCell(new Label(8, y, CommonUtils.checkNull(list.get(i).get("SXKH_RATE"))));
				sheet.addCell(new Label(9, y, CommonUtils.checkNull(list.get(i) .get("BYKH_ACCOUNT"))));
				sheet.addCell(new Label(10, y, CommonUtils.checkNull(list.get(i).get("BYKH_RATE"))));
				sheet.addCell(new Label(11, y, CommonUtils.checkNull(list.get(i) .get("YWKH_ACCOUNT"))));
				sheet.addCell(new Label(12, y, CommonUtils.checkNull(list.get(i).get("YWKH_RATE"))));
			}
		}
		++y;
	}
	
}
