package com.infodms.dms.actions.sales.usermng;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

//import com.infodms.dms.actions.sales.marketmanage.planmanage.ActivitiesCamCost;
import com.infodms.dms.actions.sales.storageManage.AddressAddApply;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.usermng.UserManageReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class UserManageReport {
	public Logger logger = Logger.getLogger(AddressAddApply.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private UserManageReportDao dao = UserManageReportDao.getInstance();
	
	private final String PERSON_RATE_INIT = "/jsp/sales/userMng/person_rate_init.jsp";
	

	/**
	 * @author  yinshunhui
	 * 人员流失率初始化
	 */
	public void personRatePre() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(PERSON_RATE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员转换机构异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 人员流失率查询
	 * 
	 */
	public void personRateSelect() {
		AclUserBean logonUser = null;
		try {

			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerCodes=request.getParamValue("dealerCode");
			String leaveStartDate=request.getParamValue("leaveStartDate");
			String leaveEndDate=request.getParamValue("leaveEndDate");
			String pageSize0 = CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0 = CommonUtils.checkNull(request.getParamValue("curPage"));
			int curPage = 1;
			int pageSize = 10;
			if (curPage0 != null && !"".equals(curPage0)) {
				curPage = Integer.parseInt(curPage0);
			}
			if (pageSize0 != null && !"".equals(pageSize0)) {
				pageSize = Integer.parseInt(pageSize0);
			}
			Map<String,String> conditionMap=new HashMap<String, String>();
			conditionMap.put("leaveStartDate", leaveStartDate);
			conditionMap.put("leaveEndDate", leaveEndDate);
			
			PageResult<Map<String, Object>> ps = null;
			ps = dao.personRateSelect(logonUser,conditionMap,dealerCodes, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	/*
	 * 人员流失率下载
	 */
	public void personRateDownLoad(){
		
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String orgId=logonUser.getOrgId().toString();
			String dealerCodes=request.getParamValue("dealerCode");
			String leaveStartDate=request.getParamValue("leaveStartDate");
			String leaveEndDate=request.getParamValue("leaveEndDate");
			Map<String,String> conditionMap=new HashMap<String, String>();
			conditionMap.put("leaveStartDate", leaveStartDate);
			conditionMap.put("leaveEndDate", leaveEndDate);
			List<Map<String,Object>> balanceList=dao.personRateDownLoad(logonUser,conditionMap,dealerCodes);
			// 导出的文件名
			String fileName = "经销商人员流失率报表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("人员流失率查询", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<List<Object>> lists = new LinkedList<List<Object>>();
			int y=0;
			sheet.mergeCells(0, y, 9, y);
			sheet.addCell(new jxl.write.Label(0, y, "人员流失率明细",wcf));
			++y;
			sheet.addCell(new Label(0, y, "片区名称"));
			sheet.addCell(new Label(1, y, "经销商代码"));
			sheet.addCell(new Label(2, y, "经销商名称"));
			sheet.addCell(new Label(3, y, "在职"));
			sheet.addCell(new Label(4, y, "离职"));
			sheet.addCell(new Label(5, y, "流失率"));
			sheet.addCell(new Label(6, y, "总经理"));
			sheet.addCell(new Label(7, y, "销售经理"));
			sheet.addCell(new Label(8, y, "市场经理"));
			sheet.addCell(new Label(9, y, "销售顾问"));
			
			int length=balanceList.size();
			for(int i=0;i<length;i++){
				++y;
				Map<String,Object> maps=balanceList.get(i);
				sheet.addCell(new Label(0, y, maps.get("PQ_ORG_NAME")==null?"":maps.get("PQ_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, maps.get("DEALER_CODE")==null?"":maps.get("DEALER_CODE").toString()));
				sheet.addCell(new Label(2, y, maps.get("DEALER_NAME")==null?"":maps.get("DEALER_NAME").toString()));
				
				sheet.addCell(new Label(3, y, maps.get("ZZ")==null?"":maps.get("ZZ").toString()));
				sheet.addCell(new Label(4, y, maps.get("LZ")==null?"":maps.get("LZ").toString()));
				sheet.addCell(new Label(5, y, maps.get("RATE")==null?"":maps.get("RATE").toString()));
				sheet.addCell(new Label(6, y, maps.get("Z_JL")==null?"":maps.get("Z_JL").toString()));
				sheet.addCell(new Label(7, y, maps.get("XS_JL")==null?"":maps.get("XS_JL").toString()));
				sheet.addCell(new Label(8, y, maps.get("SC_JL")==null?"":maps.get("SC_JL").toString()));
				sheet.addCell(new Label(9, y, maps.get("XS_GW")==null?"":maps.get("XS_GW").toString()));
			}
			workbook.write();
			workbook.close();
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
