package com.infodms.dms.actions.report.jcafterservicereport;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ServiceCenterMonthlyReportBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.report.jcafterservicereport.ServiceCenterMonthlyReportDao;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 轿车公司服务中心月申报费用明细表
 */
public class ServiceCenterMonthlyReport {
	
	public Logger logger = Logger.getLogger(ServiceCenterMonthlyReport.class);
	
	private final String initUrl = "/jsp/report/jcafterservicereport/servicecentermonthlyreport.jsp";
	/**
	 * 轿车公司服务中心月申报费用明细表 首页初始化
	 */
	public void serviceCenterMonthlyReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
			List<Map<String, Object>> areaList = dao.getAreaList();//区域
			List<Map<String, Object>> modelList = dao.getModelGroupList(String.valueOf(Constant.WR_MODEL_GROUP_TYPE_01));//车型大类
			List<Map<String, Object>> seriesList = dao.getSeriesList();//区域
			
			act.setOutData("seriesList", seriesList);
			act.setOutData("areaList", areaList);
			act.setOutData("modelList", modelList);
			act.setForword(initUrl);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车公司服务中心月申报费用明细表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询 轿车公司服务中心月申报费用明细表
	 */
	public void queryServiceCenterMonthlyReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String seriesName = request.getParamValue("seriesName");//车系
			String areaName = request.getParamValue("areaName");//大区
			String dealerCode = Utility.getCODES(request.getParamValue("dealerCode"));//经销商代码集合
			String beginTime = request.getParamValue("beginTime");//审核时间
			String endTime = request.getParamValue("endTime");//审核结束时间
			String province = request.getParamValue("province");//省份代码

			ServiceCenterMonthlyReportDao reportDao = new ServiceCenterMonthlyReportDao();
			ServiceCenterMonthlyReportBean reportBean = new ServiceCenterMonthlyReportBean();
			reportBean.setSeriesName(seriesName);
			reportBean.setAreaName(areaName);
			reportBean.setDealerCode(dealerCode);
			reportBean.setBeginTime(beginTime);
			reportBean.setEndTime(endTime);
			reportBean.setProvince(province);
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = reportDao.queryServiceCenterMonthlyReport(reportBean,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车公司服务中心月申报费用明细表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 导出 轿车公司服务中心月申报费用明细表 报表
	 */
	public void exportToExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			
			List<Map<String, Object>> exportList = this.getExportList(request);
		    if(exportList!=null && exportList.size()>0){
				String[] head= this.getExcelHead();
				String[] column = this.getExcelColumns();
				String fileName = "轿车公司服务中心月申报费用明细表";
			    this.toExcel(act, fileName, head, column, exportList);
		    }
		    act.setForword(initUrl);	
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车公司服务中心月申报费用明细表报表导出");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
    
	/**
	 * 导出 轿车公司月审核明细报表
	 */
	public void exportToExce2(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			
			List<Map<String, Object>> exportList = this.getExportList2(request);
		    if(exportList!=null && exportList.size()>0){
				String[] head= this.getExcelHead2();
				String[] column = this.getExcelColumns2();
				String fileName = "轿车公司月审核费用明细报表";
			    this.toExcel(act, fileName, head, column, exportList);
		    }
		    act.setForword(initUrl);	
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车公司服务中心月申报费用明细表报表导出");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
    /**
     * 将数据生成Excel报表返回页面
     * @param aContext
     * @param fileName 生成文件名(不带后缀,自动补齐.XLS)
     * @param head 表头
     * @param column 列对应字段
     * @param dataList 数据
     * @return
     * @throws Exception
     */
	public Object toExcel(ActionContext aContext,String fileName ,String[] head,String[] column,
			List<Map<String,Object>> dataList) throws Exception {
		try {
			
			ResponseWrapper response = aContext.getResponse();
			String dateStr = CommonUtils.printDate(new Date());
			fileName = fileName+"_"+ dateStr + ".xls";
			
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
		    
			OutputStream out = response.getOutputStream();
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("Data", 0);

			if (head != null && head.length > 0) {
				//添加题头(写入到0行)
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
				int row = 0;//行号
				//写入数据
				for (Map<String, Object> rowMap : dataList) {
					row++;
					for (int i = 0; i < column.length; i++) {
						String key = column[i];
						String value = CommonUtils.getDataFromMap(rowMap, key).toString();
						ws.addCell(new Label(i,row,value));
					}
				}
			}
			wwb.write();
			wwb.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 查询需要导出的数据
	 * @param request
	 * @return
	 */
    private List<Map<String,Object>> getExportList(RequestWrapper request){
		String seriesName = request.getParamValue("seriesName");//车系
		String areaName = request.getParamValue("areaName");//大区
		String dealerCode = request.getParamValue("dealerCode");//经销商代码集合
		String beginTime = request.getParamValue("beginTime");//结算单开始时间
		String endTime = request.getParamValue("endTime");//结算单结束时间
		String province = request.getParamValue("province");//省份代码
		
		ServiceCenterMonthlyReportDao reportDao = new ServiceCenterMonthlyReportDao();
		ServiceCenterMonthlyReportBean reportBean = new ServiceCenterMonthlyReportBean();
		reportBean.setSeriesName(seriesName);
		reportBean.setAreaName(areaName);
		reportBean.setDealerCode(dealerCode);
		reportBean.setBeginTime(beginTime);
		reportBean.setEndTime(endTime);
		reportBean.setProvince(province);
		
		List<Map<String,Object>> resultList = reportDao.queryServiceCenterMonthlyReport(reportBean);
		return resultList;
    }		
	/**
	 * 查询需要导出的数据
	 * @param request
	 * @return
	 */
    private List<Map<String,Object>> getExportList2(RequestWrapper request){
        String beginTime = request.getParamValue("beginTime");		
        String endTime = request.getParamValue("endTime");	
        String audit_beginTime = request.getParamValue("audit_beginTime");		
        String audit_endTime = request.getParamValue("audit_endTime");	
        String dealerCode = request.getParamValue("dealerCode");
        String areaName = request.getParamValue("areaName");
        ServiceCenterMonthlyReportDao dao = new ServiceCenterMonthlyReportDao();
		List<Map<String,Object>>  resultList = dao.threeGuaranteesAuditReportView( beginTime, endTime, audit_beginTime, audit_endTime, dealerCode, areaName);
		return resultList;
    }	

    /**
     * 取的导出报表题头
     * @return
     */
    private String[] getExcelHead(){
    	String[] heads = new String[19];
    	heads[0] = "序号";
    	heads[1] = "服务商代码";
    	heads[2] = "服务商名称";
    	heads[3] = "一级服务商";
    	heads[4] = "所属大区";
    	heads[5] = "省份";
    	heads[6] = "售后维修工时费";
    	heads[7] = "售后维修材料费";
    	heads[8] = "售后维修单据数";
    	heads[9] = "售前维修工时费";
    	heads[10] = "售前维修材料费";
    	heads[11] = "售前维修单据数";
    	heads[12] = "售后外出费用";
    	/***add xiongchuan 20110217 售前外出费用改为在特殊费用统计报表中展示***/
 //   	heads[13] = "售前外出费用";
    	/***add xiongchuan 20110217 售前外出费用改为在特殊费用统计报表中展示***/
    	heads[13] = "保养费";
    	heads[14] = "保养单据数";
    	heads[15] = "服务活动费";
    	heads[16] = "服务活动单据数";
    	heads[17] = "车系名称";
    	return heads;
    }

    /**
     * 取得导出报表中每列中的字段
     * @return
     */
    private String[] getExcelColumns(){
    	String[] columns = new String[19];
    	columns[0] = "NUM";//序号
    	columns[1] = "DEALER_CODE";//服务商代码
    	columns[2] = "DEALER_SHORTNAME";//服务商名称
    	columns[3] = "DEALERNAME";//一级服务商
    	columns[4] = "ORG_NAME";//所属大区
    	columns[5] = "REGION_NAME";//省份
    	columns[6] = "SHGS";//售后维修工时费
    	columns[7] = "SHCL";//售后维修材料费
    	columns[8] = "SHDS";//售后维修单据数
    	columns[9] = "SQGS";//售前维修工时费
    	columns[10] = "SQCL";//售前维修材料费
    	columns[11] = "SQDS";//售前维修单据数
    	columns[12] = "SHWC";//售后外出费用
    	/***add xiongchuan 20110217 售前外出费用改为在特殊费用统计报表中展示***/
//    	columns[13] = "SQWC";//售前外出费用
    	/***add xiongchuan 20110217 售前外出费用改为在特殊费用统计报表中展示***/
    	columns[13] = "BYF";//保养费
    	columns[14] = "BYDS";//保养服务单数
    	columns[15] = "FWHD";//服务活动费
    	columns[16] = "FWHDDS";//服务活动单据数
    	columns[17] = "SERIES_NAME";
    	return columns;
    }
    
    
    /**
     * 取的导出报表题头
     * @return
     */
    private String[] getExcelHead2(){
    	String[] heads = new String[104];
    	heads[0] = "维修站编码";
    	heads[1] = "维修站名称";
    	heads[2] = "维修起时间";
    	heads[3] = "维修止时间";
    	heads[4] = "大区";
    	heads[5] = "审核时间";
    	heads[6] = "生产厂家";
    	heads[7] = "奔奔MINI轿车售前三包单数";
    	heads[8] = "奔奔MINI轿车售前材料费";
    	heads[9] = "奔奔MINI轿车售前工时费";
    	heads[10] = "奔奔MINI轿车售后三包单数";
    	heads[11] = "奔奔MINI轿车售后材料费";
    	heads[12] = "奔奔MINI轿车售后工时费";
    	heads[13] = "奔奔MINI轿车走保数";
    	heads[14] = "奔奔MINI轿车走保费";
    	heads[15] = "奔奔MINI轿车服务活动次数";
    	//
    	heads[16] = "奔奔轿车售前三包单数";
    	heads[17] = "奔奔轿车售前材料费";
    	heads[18] = "奔奔轿车售前工时费";
    	heads[19] = "奔奔轿车售后三包单数";
    	heads[20] = "奔奔轿车售后材料费";
    	heads[21] = "奔奔轿车售后工时费";
    	heads[22] = "奔奔轿车走保数";
    	heads[23] = "奔奔轿车走保费";
    	heads[24] = "奔奔轿车服务活动次数";
    	//    	
    	heads[25] = "悦翔轿车售前三包单数";
    	heads[26] = "悦翔轿车售前材料费";
    	heads[27] = "悦翔轿车售前工时费";
    	heads[28] = "悦翔轿车售后三包单数";
    	heads[29] = "悦翔轿车售后材料费";
    	heads[30] = "悦翔轿车售后工时费";
    	heads[31] = "悦翔轿车走保数";
    	heads[32] = "悦翔轿车走保费";
    	heads[33] = "悦翔轿车服务活动次数";
    	//
    	heads[34] = "志翔轿车售前三包单数";
    	heads[35] = "志翔轿车售前材料费";
    	heads[36] = "志翔轿车售前工时费";
    	heads[37] = "志翔轿车售后三包单数";
    	heads[38] = "志翔轿车售后材料费";
    	heads[39] = "志翔轿车售后工时费";
    	heads[40] = "志翔轿车走保数";
    	heads[41] = "志翔轿车走保费";
    	heads[42] = "志翔轿车服务活动次数";
    	//
    	heads[43] = "杰勋轿车售前三包单数";
    	heads[44] = "杰勋轿车售前材料费";
    	heads[45] = "杰勋轿车售前工时费";
    	heads[46] = "杰勋轿车售后三包单数";
    	heads[47] = "杰勋轿车售后材料费";
    	heads[48] = "杰勋轿车售后工时费";
    	heads[49] = "杰勋轿车走保数";
    	heads[50] = "杰勋轿车走保费";
    	heads[51] = "杰勋轿车服务活动次数";
    	//CX30
    	heads[52] = "CX30两厢轿车售前三包单数";
    	heads[53] = "CX30两厢轿车售前材料费";
    	heads[54] = "CX30两厢轿车售前工时费";
    	heads[55] = "CX30两厢轿车售后三包单数";
    	heads[56] = "CX30两厢轿车售后材料费";
    	heads[57] = "CX30两厢轿车售后工时费";
    	heads[58] = "CX30两厢轿车走保数";
    	heads[59] = "CX30两厢轿车走保费";
    	heads[60] = "CX30两厢轿车服务活动次数";
    	//
    	heads[61] = "CX20轿车售前三包单数";
    	heads[62] = "CX20轿车售前材料费";
    	heads[63] = "CX20轿车售前工时费";
    	heads[64] = "CX20轿车售后三包单数";
    	heads[65] = "CX20轿车售后材料费";
    	heads[66] = "CX20轿车售后工时费";
    	heads[67] = "CX20轿车走保数";
    	heads[68] = "CX20轿车走保费";
    	heads[69] = "CX20轿车服务活动次数";
    	//CX30三厢
    	heads[70] = "CX30三厢轿车售前三包单数";
    	heads[71] = "CX30三厢轿车售前材料费";
    	heads[72] = "CX30三厢轿车售前工时费";
    	heads[73] = "CX30三厢轿车售后三包单数";
    	heads[74] = "CX30三厢轿车售后材料费";
    	heads[75] = "CX30三厢轿车售后工时费";
    	heads[76] = "CX30三厢轿车走保数";
    	heads[77] = "CX30三厢轿车走保费";
    	heads[78] = "CX30三厢轿车服务活动次数";
    	//
    	heads[79] = "工时费";
    	heads[80] = "材料费";
    	heads[81] = "走保费";
    	heads[82] = "服务活动费";
    	heads[83] = "救急费";
    	heads[84] = "运费";
    	heads[85] = "特殊费用";
    	heads[86] = "保养费扣款";
    	heads[87] = "服务活动费扣款";
    	heads[88] = "旧件扣款";
    	heads[89] = "考核扣款";
    	heads[90] = "费用合计";
    	heads[91] = "扣款合计";
    	heads[92] = "总计";
    	heads[93] = "审核扣款";
    	heads[94] = "结算员";
    	return heads;
    }

    /**
     * 取得导出报表中每列中的字段
     * @return
     */
    private String[] getExcelColumns2(){
    	String[] columns = new String[104];
    	columns[0] = "DEALER_CODE";//
    	columns[1] = "DEALER_NAME";//服务商代码
    	columns[2] = "START_DATE";//服务商名称
    	columns[3] = "END_DATE";//一级服务商
    	columns[4] = "ROOT_ORG_NAME";//所属大区
    	columns[5] = "CREATE_DATE";//省份
    	columns[6] = "YILDLY";
    	//
    	columns[7] ="BBMI_SQSPDS";
    	columns[8] ="BBMI_SQCLF";
    	columns[9] = "BBMI_SQGSF";
    	columns[10] ="BBMI_SHSPDS";
    	columns[11] ="BBMI_SHCLF";
    	columns[12] ="BBMI_SHGSF";
    	columns[13] = "BBMI_MFBYDS";
    	columns[14] ="BBMI_MFBYZFY";
    	columns[15] ="BBMI_FWHDSPDS";
        //
    	columns[16] ="BB_SQSPDS";
    	columns[17] ="BB_SQCLF";
    	columns[18] = "BB_SQGSF";
    	columns[19] ="BB_SHSPDS";
    	columns[20] ="BB_SHCLF";
    	columns[21] ="BB_SHGSF";
    	columns[22] = "BB_MFBYDS";
    	columns[23] ="BB_MFBYZFY";
    	columns[24] ="BB_FWHDSPDS";
    	//
    	columns[25] ="YX_SQSPDS";
    	columns[26] ="YX_SQCLF";
    	columns[27] = "YX_SQGSF";
    	columns[28] ="YX_SHSPDS";
    	columns[29] ="YX_SHCLF";
    	columns[30] ="YX_SHGSF";
    	columns[31] = "YX_MFBYDS";
    	columns[32] ="YX_MFBYZFY";
    	columns[33] ="YX_FWHDSPDS";
    	//
    	columns[34] ="ZX_SQSPDS";
    	columns[35] ="ZX_SQCLF";
    	columns[36] = "ZX_SQGSF";
    	columns[37] ="ZX_SHSPDS";
    	columns[38] ="ZX_SHCLF";
    	columns[39] ="ZX_SHGSF";
    	columns[40] = "ZX_MFBYDS";
    	columns[41] ="ZX_MFBYZFY";
    	columns[42] ="ZX_FWHDSPDS";
    	//
    	columns[43] ="JX_SQSPDS";
    	columns[44] ="JX_SQCLF";
    	columns[45] = "JX_SQGSF";
    	columns[46] ="JX_SHSPDS";
    	columns[47] ="JX_SHCLF";
    	columns[48] ="JX_SHGSF";
    	columns[49] = "JX_MFBYDS";
    	columns[50] ="JX_MFBYZFY";
    	columns[51] ="JX_FWHDSPDS";
    	//
    	columns[52] ="CX30_SQSPDS";
    	columns[53] ="CX30_SQCLF";
    	columns[54] = "CX30_SQGSF";
    	columns[55] ="CX30_SHSPDS";
    	columns[56] ="CX30_SHCLF";
    	columns[57] ="CX30_SHGSF";
    	columns[58] = "CX30_MFBYDS";
    	columns[59] ="CX30_MFBYZFY";
    	columns[60] ="CX30_FWHDSPDS";
    	//    	
    	columns[61] ="CX20_SQSPDS";
    	columns[62] ="CX20_SQCLF";
    	columns[63] = "CX20_SQGSF";
    	columns[64] ="CX20_SHSPDS";
    	columns[65] ="CX20_SHCLF";
    	columns[66] ="CX20_SHGSF";
    	columns[67] = "CX20_MFBYDS";
    	columns[68] ="CX20_MFBYZFY";
    	columns[69] ="CX20_FWHDSPDS";
    	//    	
    	columns[70] ="CX30SX_SQSPDS";
    	columns[71] ="CX30SX_SQCLF";
    	columns[72] = "CX30SX_SQGSF";
    	columns[73] ="CX30SX_SHSPDS";
    	columns[74] ="CX30SX_SHCLF";
    	columns[75] ="CX30SX_SHGSF";
    	columns[76] = "CX30SX_MFBYDS";
    	columns[77] ="CX30SX_MFBYZFY";
    	columns[78] ="CX30SX_FWHDSPDS";
    	columns[79] ="LABOUR_AMOUNT";
    	columns[80] ="PART_AMOUNT";
    	columns[81] ="FREE_AMOUNT";
    	columns[82] ="SERVICE_TOTAL_AMOUNT";
    	columns[83]="OTHER_AMOUNT";
    	columns[84]="RETURN_AMOUNT";
    	columns[85]="SPEOUTFEE_AMOUNT";
    	columns[86]="FREE_DEDUCT";
    	columns[87]="SUM_SERVICE_DEDUCT";
    	columns[88]="OLD_DEDUCT";
    	columns[89]="CHECK_DEDUCT";
    	columns[90]="APPLY_AMOUNT";
    	columns[91]="SUM_KKZJ";
    	columns[92]="BALANCE_AMOUNT";
    	columns[93]="SUM_KKZJ";
    	columns[94]="AUTH_PERSON_NAME";

    	return columns;
    }
}
