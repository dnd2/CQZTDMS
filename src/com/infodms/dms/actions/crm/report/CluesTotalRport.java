package com.infodms.dms.actions.crm.report;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.report.ClueReportDao;
import com.infodms.dms.dao.crm.report.CluesTotalRportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

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

import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: 线索总量效率分析报表Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date:  2015-02-05
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class   CluesTotalRport{
	
	public Logger logger = Logger.getLogger(CluesTotalRport.class);
	CluesTotalRportDao dao  = CluesTotalRportDao.getInstance();
	ClueReportDao cDao  = ClueReportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
    private ResponseWrapper response = act.getResponse();
	private final String initUrl = "/jsp/crm/report/cluesTotalRportInit.jsp";
	private final String queryUrl = "/jsp/crm/report/cluesTotalRportBalance.jsp";
									 
	/**
	 * 线索总量效率分析报表 页面初始化
	 */
	public void doInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String today=sdf.format(new Date());
            act.setOutData("today", today);
            act.setOutData("dutyType",logonUser.getDutyType());
            act.setOutData("orgId", logonUser.getOrgId());
            String  download =null;
            String longUser=CommonUtils.getPoseRank(logonUser);
            if("60281001".equals(longUser) ||"60281002".equals(longUser)){
            	Map<String,String> map =new HashMap<String,String>();
            	map.put("dealerId", logonUser.getDealerId());
            	map.put("userId", logonUser.getUserId().toString());
        		PageResult<Map<String, Object>> ps = cDao.getDealerCode(map,1, 1);
            	if(ps.getTotalPages()> 0){
            		act.setOutData("flag", "flag");
            	}
            	download="true";
			} else if(!"10431005".equals(logonUser.getDutyType())){
				download="true";
			}
            act.setOutData("download", download);
            act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"线索总量效率分析报表页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 线索总量效率分析报表 汇总表
	 */
	public void doSearch(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
            String leadsOrigin = CommonUtils.checkNull(request.getParamValue("leads_origin"));
            String originId = CommonUtils.checkNull(request.getParamValue("originId")) ;
            if(CommonUtils.isNullString(dealerCode)){
            	dealerCode = CommonUtils.checkNull(request.getParamValue("cdealerCode")) ;
            }

            String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			
			act.setOutData("orgId", orgId);
			Map<String,String> map=new HashMap<String,String>();			
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
		    map.put("seriesId", seriesId);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("dealerId", logonUser.getDealerId());
			map.put("dutyType", logonUser.getDutyType());
			map.put("leadsOrigin", leadsOrigin);
			map.put("originId", originId);
			String userId=  logonUser.getUserId().toString();
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				userId = logonUser.getUserId().toString();
				map.put("userId", userId);
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				//获取主管下属分组的所有顾问
				userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
				map.put("flag", "flag");
				map.put("userId", userId);
			}else if(CommonUtils.judgeMgrLogin(logonUser.getUserId().toString())){
				map.put("flag", "flag");
				map.put("manager", "manager");
			}else if(CommonUtils.judgeTotalMgrAllLogin(logonUser.getUserId().toString())){
				map.put("flag", "flag");
				map.put("manager", "manager");
			}
			
			List<Map<String,Object>> balanceList;
			if( Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){ //10431005
				balanceList=dao.getClueReportDaoSelect(map);
				act.setOutData("dutyType",logonUser.getDutyType());
			}else{
				balanceList=dao.getClueReportDaoSelectAll(map);
				act.setOutData("startDate",startDate);
				act.setOutData("endDate",endDate);
				act.setOutData("dealerCode",dealerCode);
				act.setOutData("seriesId",seriesId);
			}
			act.setOutData("xsNameList",dao.getNameList("6016"));
			act.setOutData("balanceList",balanceList);
            act.setForword(queryUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"线索总量效率分析报表汇总表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 线索总量效率分析报表dcrc经销商详情
	 */
	public void getDealerList(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            String dealer_id = CommonUtils.checkNull(request.getParamValue("dealer_id")) ;
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
           
            String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;

            String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
				orgId="1";
			}
			
			String codeID="";
			List<Map<String, Object>> codeList = dao.getNameList(codeID);
			act.setOutData("orgId", orgId);
			act.setOutData("codeList", codeList);
			Map<String,String> map=new HashMap<String,String>();			
			 
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("orgId", orgId);
			map.put("dealerId", dealer_id); 
			map.put("dutyType", "10431005");
			map.put("seriesId", seriesId);
			map.put("flag", "flag");
			map.put("manager", "manager");
			
			List<Map<String,Object>>  balanceList=dao.getClueReportDaoSelect(map);
			 act.setOutData("dutyType","10431005");
			 
			act.setOutData("balanceList",balanceList);
            
            act.setForword(queryUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"线索总量效率分析报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void doDownload() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 导出的文件名
			String fileName = "线索总量效率分析报表(有效).xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            String startDate = CommonUtils.checkNull(request.getParamValue("startDate")) ;
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate")) ;
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")) ;
            String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
            if(CommonUtils.isNullString(dealerCode)){
            	dealerCode = CommonUtils.checkNull(request.getParamValue("cdealerCode")) ;
            }
            Map<String, String> paraMap = new HashMap<String, String>();

            paraMap.put("startDate", startDate);
            paraMap.put("endDate", endDate);
            paraMap.put("dealerCode", dealerCode);
            paraMap.put("dutyType", logonUser.getDutyType());
            paraMap.put("orgId", logonUser.getOrgId().toString());
            paraMap.put("dealerId", logonUser.getDealerId()); 
            
            paraMap.put("dutyType", logonUser.getDutyType());
            paraMap.put("seriesId", seriesId);
			String userId=logonUser.getUserId().toString();
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				userId = logonUser.getUserId().toString();
				paraMap.put("userId", userId);
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				//获取主管下属分组的所有顾问
				userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
				paraMap.put("userId", userId);
				paraMap.put("flag", "flag");
			}else if(CommonUtils.judgeMgrLogin(logonUser.getUserId().toString())){
				paraMap.put("flag", "flag");
				paraMap.put("manager", "manager");
			}else if(CommonUtils.judgeTotalMgrAllLogin(logonUser.getUserId().toString())){
				paraMap.put("flag", "flag");
				paraMap.put("manager", "manager");
			}
            OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			List<Map<String, Object>> list=null;
			if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){
				list= dao.getClueReportDaoSelect(paraMap);
				getExcelName(list, workbook);
			}else{
				list=dao.getClueReportDaoSelectAll(paraMap);
				getExcel(list, workbook);
			} 
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "线索总量效率分析报表(有效)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getExcelName(List<Map<String, Object>> list,WritableWorkbook workbook) throws WriteException{
		int len = list.size();
		WritableSheet sheet = workbook.createSheet("线索总量效率分析报表(有效)", 0);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(Alignment.CENTRE);
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		DisplayFormat displayFormat = NumberFormats.TEXT;
		WritableCellFormat format = new WritableCellFormat(wf,displayFormat);
		format.setAlignment(jxl.format.Alignment.LEFT);
		format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.NONE); 
		int y = 0;
		sheet.addCell(new Label(0, y, "大区"));
		sheet.addCell(new Label(1, y, "省份"));
		sheet.addCell(new Label(2, y, "代码"));
		sheet.addCell(new Label(3, y, "简称"));
	    sheet.addCell(new Label(4, y, "销售组"));
	    sheet.addCell(new Label(5, y, "销售顾问"));
		sheet.addCell(new Label(6, y, "总量"));
		sheet.addCell(new Label(7, y, "处理总量"));	
		sheet.addCell(new Label(8, y, "24小时处理量"));
		sheet.addCell(new Label(9, y, "24小时处理率"));
		sheet.addCell(new Label(10, y, "有效留存线索"));	
		sheet.addCell(new Label(11, y, "占比"));
		sheet.addCell(new Label(12, y, "战败线索"));	
		sheet.addCell(new Label(13, y, "占比"));
		sheet.addCell(new Label(14, y, "失效线索"));	
		sheet.addCell(new Label(15, y, "占比"));
		sheet.addCell(new Label(16, y, "无效线索"));	
		sheet.addCell(new Label(17, y, "占比"));
		sheet.addCell(new Label(18, y, "重复"));
		sheet.addCell(new Label(19, y, "占比"));
		sheet.addCell(new Label(20, y, "首次到店"));
		sheet.addCell(new Label(21, y, "占比"));

		if (list != null) {
			for (int i = 0; i < len; i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, list.get(i).get("PQ_ORG_NAME").toString()));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Label(4, y, list.get(i).get("GROUP_NAME").toString()));
				sheet.addCell(new Label(5, y, list.get(i).get("NAME").toString()));
				sheet.addCell(new Number(6, y, Double.parseDouble(String.valueOf(Integer.parseInt(list.get(i).get("TOTALALL").toString())+Integer.parseInt(list.get(i).get("WCLCOUNT").toString()))),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(7, y, Double.parseDouble(list.get(i).get("TOTALALL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(8, y, Double.parseDouble(list.get(i).get("CECOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(9, y, list.get(i).get("HOURS").toString()));
				sheet.addCell(new Number(10, y, Double.parseDouble(list.get(i).get("YXCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(11, y, list.get(i).get("YXPROPORTION").toString()));
				sheet.addCell(new Number(12, y, Double.parseDouble(list.get(i).get("ZBCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(13, y, list.get(i).get("ZBPROPORTION").toString()));
				sheet.addCell(new Number(14, y, Double.parseDouble(list.get(i).get("SXCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(15, y, list.get(i).get("SXPROPORTION").toString()));
				sheet.addCell(new Number(16, y, Double.parseDouble(list.get(i).get("WXCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(17, y, list.get(i).get("WXPROPORTION").toString()));
				sheet.addCell(new Number(18, y, Double.parseDouble(list.get(i).get("CFTOTAL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(19, y, list.get(i).get("CFPROPORTION").toString()));
				sheet.addCell(new Number(20, y, Double.parseDouble(list.get(i).get("SCCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(21, y, list.get(i).get("SCPROPORTION").toString()));
			}
		}
		++y;
	}
	public void getExcel(List<Map<String, Object>> list,WritableWorkbook workbook) throws WriteException{
		int len = list.size();
		WritableSheet sheet = workbook.createSheet("线索总量效率分析报表(有效)", 0);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(Alignment.CENTRE);
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
		sheet.addCell(new Label(4, y, "总量"));
		sheet.addCell(new Label(5, y, "处理总量"));	
		sheet.addCell(new Label(6, y, "24小时处理率"));
		sheet.addCell(new Label(7, y, "24小时处理率"));
		sheet.addCell(new Label(8, y, "有效留存线索"));	
		sheet.addCell(new Label(9, y, "占比"));
		sheet.addCell(new Label(10, y, "战败线索"));	
		sheet.addCell(new Label(11, y, "占比"));
		sheet.addCell(new Label(12, y, "失效线索"));	
		sheet.addCell(new Label(13, y, "占比"));
		sheet.addCell(new Label(14, y, "无效线索"));	
		sheet.addCell(new Label(15, y, "占比"));
		sheet.addCell(new Label(16, y, "重复"));
		sheet.addCell(new Label(17, y, "占比"));
		sheet.addCell(new Label(18, y, "首次到店"));
		sheet.addCell(new Label(19, y, "占比"));

		if (list != null) {
			for (int i = 0; i < len; i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null?"":list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, list.get(i).get("PQ_ORG_NAME")==null?"":list.get(i).get("PQ_ORG_NAME").toString()));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE")==null?"":list.get(i).get("DEALER_CODE").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Number(4, y, Double.parseDouble(String.valueOf(Integer.parseInt(list.get(i).get("TOTALALL").toString())+Integer.parseInt(list.get(i).get("WCLCOUNT").toString()))),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(5, y, Double.parseDouble(list.get(i).get("TOTALALL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(6, y, Double.parseDouble(list.get(i).get("CECOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(7, y, list.get(i).get("HOURS").toString()));
				sheet.addCell(new Number(8, y, Double.parseDouble(list.get(i).get("YXCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(9, y, list.get(i).get("YXPROPORTION").toString()));
				sheet.addCell(new Number(10, y, Double.parseDouble(list.get(i).get("ZBCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(11, y, list.get(i).get("ZBPROPORTION").toString()));
				sheet.addCell(new Number(12, y, Double.parseDouble(list.get(i).get("SXCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(13, y, list.get(i).get("SXPROPORTION").toString()));
				sheet.addCell(new Number(14, y, Double.parseDouble(list.get(i).get("WXCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(15, y, list.get(i).get("WXPROPORTION").toString()));
				sheet.addCell(new Number(16, y, Double.parseDouble(list.get(i).get("CFTOTAL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(17, y, list.get(i).get("CFPROPORTION").toString()));
				sheet.addCell(new Number(18, y, Double.parseDouble(list.get(i).get("SCCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(19, y, list.get(i).get("SCPROPORTION").toString()));
			}
		}
		++y;
	}
	 
}
