package com.infodms.dms.actions.crm.report;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.report.ClueReportDao;
import com.infodms.dms.dao.crm.report.PotentialCustomerAnalysisDao;
import com.infodms.dms.dao.crm.report.ReceptionAnalysisDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;



import org.apache.log4j.Logger;

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

/**
 * @Title: 潜客等级分析报表Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date:  2015-02-05
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class   PotentialCustomerAnalysisReport{
	
	public Logger logger = Logger.getLogger(PotentialCustomerAnalysisReport.class);
	PotentialCustomerAnalysisDao dao  = PotentialCustomerAnalysisDao.getInstance();
	ClueReportDao cDao  = ClueReportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
    private ResponseWrapper response = act.getResponse();
	private final String initUrl = "/jsp/crm/report/PotentialAnalysisInit.jsp";
	private final String queryUrl = "/jsp/crm/report/PotentialAnalysisBalance.jsp";
									 
	/**
	 * 潜客等级分析报表 页面初始化
	 */
	public void doInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String today=sdf.format(new Date());
            act.setOutData("today", today);
            act.setOutData("dutyType",logonUser.getDutyType());
         
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜客等级分析报表初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 潜客分析报表汇总表
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
			List<Map<String,Object>> balanceList;
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
		
			if( Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){ //10431005
			    balanceList=dao.getStatisticsDaoSelect(map);
			    act.setOutData("dutyType",logonUser.getDutyType());
			} else{
				balanceList=dao.getStatisticsDaoSelectAll(map);
				act.setOutData("startDate",startDate);
				act.setOutData("endDate",endDate);
				act.setOutData("dealerCode",dealerCode);
				act.setOutData("seriesId",seriesId);
			}
			
			act.setOutData("balanceList",balanceList);
            
            act.setForword(queryUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜客等级分析报表统计表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜客分析报表dcrc经销商详情
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
			
			act.setOutData("orgId", orgId);
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
			
			List<Map<String,Object>>  balanceList=dao.getStatisticsDaoSelect(map);
			 act.setOutData("dutyType","10431005");
			 
			act.setOutData("balanceList",balanceList);
            
            act.setForword(queryUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜客等级分析报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void doDownload() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 导出的文件名
			String fileName = "潜客等级分析报表.xls";
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
				list= dao.getStatisticsDaoSelect(paraMap);
				getExcelName(list, workbook);
			}else{
				list=dao.getStatisticsDaoSelectAll(paraMap);
				getExcel(list, workbook);
			}
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "潜客等级分析报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商导出
	 * @param list
	 * @param workbook
	 * @throws WriteException
	 */
	public void getExcelName(List<Map<String, Object>> list,WritableWorkbook workbook) throws WriteException{
		WritableSheet sheet = workbook.createSheet("潜客等级分析报表", 0);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(Alignment.CENTRE);
		int y = 0;
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		DisplayFormat displayFormat = NumberFormats.TEXT;
		WritableCellFormat format = new WritableCellFormat(wf,displayFormat);
		format.setAlignment(jxl.format.Alignment.LEFT);
		format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.NONE); 
		
		sheet.mergeCells(0, 0, 5, 0);
		
		sheet.mergeCells(6, 0, 10, 0);
		
		sheet.mergeCells(11, 0, 15, 0);
		
		sheet.mergeCells(16, 0, 20, 0);
		
		sheet.mergeCells(21, 0, 25, 0);
		
		sheet.mergeCells(26, 0, 29, 0);
		
		sheet.mergeCells(30, 0, 33, 0);
		
		sheet.mergeCells(34, 0, 37, 0);
		
		sheet.mergeCells(38, 0, 40, 0);
		sheet.mergeCells(41, 0, 43, 0);

		sheet.addCell(new Label(0, y, "信息",wcf));
		sheet.addCell(new Label(1, y, ""));
		sheet.addCell(new Label(2, y, ""));
		sheet.addCell(new Label(3, y, ""));
		sheet.addCell(new Label(4, y, ""));
		sheet.addCell(new Label(5, y, ""));
		
		sheet.addCell(new Label(6, y, "H级",wcf));
		sheet.addCell(new Label(7, y, ""));
		sheet.addCell(new Label(8, y, ""));
		sheet.addCell(new Label(9, y, ""));
		sheet.addCell(new Label(10, y, ""));
		
		sheet.addCell(new Label(11, y, "A级",wcf));
		sheet.addCell(new Label(12, y, ""));
		sheet.addCell(new Label(13, y, ""));
		sheet.addCell(new Label(14, y, ""));
		sheet.addCell(new Label(15, y, ""));
		
		
		sheet.addCell(new Label(16, y, "B级",wcf));
		sheet.addCell(new Label(17, y, ""));
		sheet.addCell(new Label(18, y, ""));
		sheet.addCell(new Label(19, y, ""));
		sheet.addCell(new Label(20, y, ""));
		
		sheet.addCell(new Label(21, y, "C级",wcf));
		sheet.addCell(new Label(22, y, ""));
		sheet.addCell(new Label(23, y, ""));
		sheet.addCell(new Label(24, y, ""));
		sheet.addCell(new Label(25, y, ""));
		
		sheet.addCell(new Label(26, y, "O级",wcf));
		sheet.addCell(new Label(27, y, ""));
		sheet.addCell(new Label(28, y, ""));
		sheet.addCell(new Label(29, y, ""));
		
		sheet.addCell(new Label(30, y, "E级",wcf));
		sheet.addCell(new Label(31, y, ""));
		sheet.addCell(new Label(32, y, ""));
		sheet.addCell(new Label(33, y, ""));
		
		sheet.addCell(new Label(34, y, "L级",wcf));
		sheet.addCell(new Label(35, y, ""));
		sheet.addCell(new Label(36, y, ""));
		sheet.addCell(new Label(37, y, ""));
		
		sheet.addCell(new Label(38, y, "合计(HAB)",wcf));
		sheet.addCell(new Label(39, y, ""));
		sheet.addCell(new Label(40, y, ""));
		
		sheet.addCell(new Label(41, y, "合计(HABC)",wcf));
		sheet.addCell(new Label(42, y, ""));
		sheet.addCell(new Label(43, y, ""));
	

		y = 1;
		sheet.addCell(new Label(0, y, "大区"));
		sheet.addCell(new Label(1, y, "省份"));
		sheet.addCell(new Label(2, y, "代码"));
		sheet.addCell(new Label(3, y, "简称"));
		
		sheet.addCell(new Label(4, y, "销售组"));
		sheet.addCell(new Label(5, y, "销售顾问"));
		
		
		sheet.addCell(new Label(6, y, "当月新增"));
		sheet.addCell(new Label(7, y, "平均卡龄"));
		sheet.addCell(new Label(8, y, "前期留存"));
		sheet.addCell(new Label(9, y, "平均卡龄"));
		sheet.addCell(new Label(10, y, "当月新增占比"));
		
		sheet.addCell(new Label(11, y, "当月新增"));
		sheet.addCell(new Label(12, y, "平均卡龄"));
		sheet.addCell(new Label(13, y, "前期留存"));
		sheet.addCell(new Label(14, y, "平均卡龄"));
		sheet.addCell(new Label(15, y, "当月新增占比"));
		
		
		sheet.addCell(new Label(16, y, "当月新增"));
		sheet.addCell(new Label(17, y, "平均卡龄"));
		sheet.addCell(new Label(18, y, "前期留存"));
		sheet.addCell(new Label(19, y, "平均卡龄"));
		sheet.addCell(new Label(20, y, "当月新增占比"));
		
		sheet.addCell(new Label(21, y, "新增"));
		sheet.addCell(new Label(22, y, "平均卡龄"));
		sheet.addCell(new Label(23, y, "前期留存"));
		sheet.addCell(new Label(24, y, "平均卡龄"));
		sheet.addCell(new Label(25, y, "当月新增占比"));
		
		sheet.addCell(new Label(26, y, "新增"));
		sheet.addCell(new Label(27, y, "平均卡龄"));
		sheet.addCell(new Label(28, y, "前期留存"));
		sheet.addCell(new Label(29, y, "平均卡龄"));
		
		sheet.addCell(new Label(30, y, "新增"));
		sheet.addCell(new Label(31, y, "平均卡龄"));
		sheet.addCell(new Label(32, y, "前期留存"));
		sheet.addCell(new Label(33, y, "平均卡龄"));
		
		sheet.addCell(new Label(34, y, "新增"));
		sheet.addCell(new Label(35, y, "平均卡龄"));
		sheet.addCell(new Label(36, y, "前期留存"));
		sheet.addCell(new Label(37, y, "平均卡龄"));
		
		sheet.addCell(new Label(38, y, "当月新增"));
		sheet.addCell(new Label(39, y, "前期留存"));
		sheet.addCell(new Label(40, y, "小计"));
		
		sheet.addCell(new Label(41, y, "当月新增"));
		sheet.addCell(new Label(42, y, "前期留存"));
		sheet.addCell(new Label(43, y, "小计"));
		
		
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null?"":list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i).get("PQ_ORG_NAME"))));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE")==null?"":list.get(i).get("DEALER_CODE").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Label(4, y, list.get(i).get("GROUP_NAME")==null?"":list.get(i).get("GROUP_NAME").toString()));
				sheet.addCell(new Label(5, y, list.get(i).get("NAME")==null?"":list.get(i).get("NAME").toString()));
				sheet.addCell(new Number(6, y, Double.parseDouble(list.get(i).get("HXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(7, y, Double.parseDouble(list.get(i).get("HPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(8, y, Double.parseDouble(list.get(i).get("HLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(9, y, Double.parseDouble(list.get(i).get("HLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(10, y, list.get(i).get("HXZRATE").toString()));
				
				sheet.addCell(new Number(11, y, Double.parseDouble(list.get(i).get("AXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(12, y, Double.parseDouble(list.get(i).get("APJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(13, y, Double.parseDouble(list.get(i).get("ALC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(14, y, Double.parseDouble(list.get(i).get("ALCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(15, y, list.get(i).get("AXZRATE").toString()));
				
				sheet.addCell(new Number(16, y, Double.parseDouble(list.get(i).get("BXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(17, y, Double.parseDouble(list.get(i).get("BPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(18, y, Double.parseDouble(list.get(i).get("BLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(19, y, Double.parseDouble(list.get(i).get("BLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(20, y, list.get(i).get("BXZRATE").toString()));
				
				sheet.addCell(new Number(21, y, Double.parseDouble(list.get(i).get("CXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(22, y, Double.parseDouble(list.get(i).get("CPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(23, y, Double.parseDouble(list.get(i).get("CLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(24, y, Double.parseDouble(list.get(i).get("CLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(25, y, list.get(i).get("CXZRATE").toString()));
				
				sheet.addCell(new Number(26, y, Double.parseDouble(list.get(i).get("OXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(27, y, Double.parseDouble(list.get(i).get("OPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(28, y, Double.parseDouble(list.get(i).get("OLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(29, y, Double.parseDouble(list.get(i).get("OLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
			
				sheet.addCell(new Number(30, y, Double.parseDouble(list.get(i).get("EXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(31, y, Double.parseDouble(list.get(i).get("EPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(32, y, Double.parseDouble(list.get(i).get("ELC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(33, y, Double.parseDouble(list.get(i).get("ELCKL").toString()),new WritableCellFormat(wf,displayFormat)));
			
				sheet.addCell(new Number(34, y, Double.parseDouble(list.get(i).get("LXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(35, y, Double.parseDouble(list.get(i).get("LPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(36, y, Double.parseDouble(list.get(i).get("LLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(37, y, Double.parseDouble(list.get(i).get("LLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
			
				sheet.addCell(new Number(38, y, Double.parseDouble(list.get(i).get("HABXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(39, y, Double.parseDouble(list.get(i).get("HABLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(40, y, Double.parseDouble(list.get(i).get("HABXZLC").toString()),new WritableCellFormat(wf,displayFormat)));
			
				sheet.addCell(new Number(41, y, Double.parseDouble(list.get(i).get("HABCXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(42, y, Double.parseDouble(list.get(i).get("HABCLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(43, y, Double.parseDouble(list.get(i).get("HABCXZLC").toString()),new WritableCellFormat(wf,displayFormat)));
				
				
			}
		}
		++y;
	}
	
	/**
	 * 车厂导出
	 * @param list
	 * @param workbook
	 * @throws WriteException
	 */
	public void getExcel(List<Map<String, Object>> list,WritableWorkbook workbook) throws WriteException{
		WritableSheet sheet = workbook.createSheet("潜客等级分析报表", 0);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(Alignment.CENTRE);
		int y = 0;
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false);
		DisplayFormat displayFormat = NumberFormats.TEXT;
		WritableCellFormat format = new WritableCellFormat(wf,displayFormat);
		format.setAlignment(jxl.format.Alignment.LEFT);
		format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.NONE); 
		sheet.mergeCells(0, 0, 3, 0);
		
		sheet.mergeCells(4, 0, 8, 0);
		
		sheet.mergeCells(9, 0, 13, 0);
		
		sheet.mergeCells(14, 0, 18, 0);
		
		sheet.mergeCells(19, 0, 23, 0);
		
		sheet.mergeCells(24, 0, 27, 0);
		
		sheet.mergeCells(28, 0, 31, 0);
		
		sheet.mergeCells(32, 0, 35, 0);
		
		sheet.mergeCells(36, 0, 38, 0);
		sheet.mergeCells(39, 0, 41, 0);
		
		sheet.addCell(new Label(0, y, "信息",wcf));
		sheet.addCell(new Label(1, y, ""));
		sheet.addCell(new Label(2, y, ""));
		sheet.addCell(new Label(3, y, ""));
		
		sheet.addCell(new Label(4, y, "H级",wcf));
		sheet.addCell(new Label(5, y, ""));
		sheet.addCell(new Label(6, y, ""));
		sheet.addCell(new Label(7, y, ""));
		sheet.addCell(new Label(8, y, ""));
		
		sheet.addCell(new Label(9, y, "A级",wcf));
		sheet.addCell(new Label(10, y, ""));
		sheet.addCell(new Label(11, y, ""));
		sheet.addCell(new Label(12, y, ""));
		sheet.addCell(new Label(13, y, ""));
		
		
		sheet.addCell(new Label(14, y, "B级",wcf));
		sheet.addCell(new Label(15, y, ""));
		sheet.addCell(new Label(16, y, ""));
		sheet.addCell(new Label(17, y, ""));
		sheet.addCell(new Label(18, y, ""));
		
		sheet.addCell(new Label(19, y, "C级",wcf));
		sheet.addCell(new Label(20, y, ""));
		sheet.addCell(new Label(21, y, ""));
		sheet.addCell(new Label(22, y, ""));
		sheet.addCell(new Label(23, y, ""));
		
		sheet.addCell(new Label(24, y, "O级",wcf));
		sheet.addCell(new Label(25, y, ""));
		sheet.addCell(new Label(26, y, ""));
		sheet.addCell(new Label(27, y, ""));
		
		
		sheet.addCell(new Label(28, y, "E级",wcf));
		sheet.addCell(new Label(29, y, ""));
		sheet.addCell(new Label(30, y, ""));
		sheet.addCell(new Label(31, y, ""));
		
		sheet.addCell(new Label(32, y, "L级",wcf));
		sheet.addCell(new Label(33, y, ""));
		sheet.addCell(new Label(34, y, ""));
		sheet.addCell(new Label(35, y, ""));
		
		
		sheet.addCell(new Label(36, y, "合计(HAB)",wcf));
		sheet.addCell(new Label(37, y, ""));
		sheet.addCell(new Label(38, y, ""));
		
		sheet.addCell(new Label(39, y, "合计(HABC)",wcf));
		sheet.addCell(new Label(40, y, ""));
		sheet.addCell(new Label(41, y, ""));
	

		y = 1;
		sheet.addCell(new Label(0, y, "大区"));
		sheet.addCell(new Label(1, y, "省份"));
		sheet.addCell(new Label(2, y, "代码"));
		sheet.addCell(new Label(3, y, "简称"));
		sheet.addCell(new Label(4, y, "当月新增"));
		sheet.addCell(new Label(5, y, "平均卡龄"));
		sheet.addCell(new Label(6, y, "前期留存"));
		sheet.addCell(new Label(7, y, "平均卡龄"));
		sheet.addCell(new Label(8, y, "当月新增占比"));
		
		sheet.addCell(new Label(9, y, "当月新增"));
		sheet.addCell(new Label(10, y, "平均卡龄"));
		sheet.addCell(new Label(11, y, "前期留存"));
		sheet.addCell(new Label(12, y, "平均卡龄"));
		sheet.addCell(new Label(13, y, "当月新增占比"));
		
		
		sheet.addCell(new Label(14, y, "当月新增"));
		sheet.addCell(new Label(15, y, "平均卡龄"));
		sheet.addCell(new Label(16, y, "前期留存"));
		sheet.addCell(new Label(17, y, "平均卡龄"));
		sheet.addCell(new Label(18, y, "当月新增占比"));
		
		sheet.addCell(new Label(19, y, "当月新增"));
		sheet.addCell(new Label(20, y, "平均卡龄"));
		sheet.addCell(new Label(21, y, "前期留存"));
		sheet.addCell(new Label(22, y, "平均卡龄"));
		sheet.addCell(new Label(23, y, "当月新增占比"));
		
		sheet.addCell(new Label(24, y, "当月新增"));
		sheet.addCell(new Label(25, y, "平均卡龄"));
		sheet.addCell(new Label(26, y, "前期留存"));
		sheet.addCell(new Label(27, y, "平均卡龄"));
		
		sheet.addCell(new Label(28, y, "当月新增"));
		sheet.addCell(new Label(29, y, "平均卡龄"));
		sheet.addCell(new Label(30, y, "前期留存"));
		sheet.addCell(new Label(31, y, "平均卡龄"));
		
		sheet.addCell(new Label(32, y, "当月新增"));
		sheet.addCell(new Label(33, y, "平均卡龄"));
		sheet.addCell(new Label(34, y, "前期留存"));
		sheet.addCell(new Label(35, y, "平均卡龄"));
		
		sheet.addCell(new Label(36, y, "当月新增"));
		sheet.addCell(new Label(37, y, "前期留存"));
		sheet.addCell(new Label(38, y, "小计"));
		
		sheet.addCell(new Label(39, y, "当月新增"));
		sheet.addCell(new Label(40, y, "前期留存"));
		sheet.addCell(new Label(41, y, "小计"));
		
		
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null?"":list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i).get("PQ_ORG_NAME"))));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE")==null?"":list.get(i).get("DEALER_CODE").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Number(4, y, Double.parseDouble(list.get(i).get("HXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(5, y, Double.parseDouble(list.get(i).get("HPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(6, y, Double.parseDouble(list.get(i).get("HLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(7, y, Double.parseDouble(list.get(i).get("HLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(8, y, list.get(i).get("HXZRATE").toString()));
				
				sheet.addCell(new Number(9, y, Double.parseDouble(list.get(i).get("AXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(10, y, Double.parseDouble(list.get(i).get("APJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(11, y, Double.parseDouble(list.get(i).get("ALC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(12, y, Double.parseDouble(list.get(i).get("ALCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(13, y, list.get(i).get("AXZRATE").toString()));
				
				sheet.addCell(new Number(14, y, Double.parseDouble(list.get(i).get("BXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(15, y, Double.parseDouble(list.get(i).get("BPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(16, y, Double.parseDouble(list.get(i).get("BLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(17, y, Double.parseDouble(list.get(i).get("BLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(18, y, list.get(i).get("BXZRATE").toString()));
				
				sheet.addCell(new Number(19, y, Double.parseDouble(list.get(i).get("CXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(20, y, Double.parseDouble(list.get(i).get("CPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(21, y, Double.parseDouble(list.get(i).get("CLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(22, y, Double.parseDouble(list.get(i).get("CLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(23, y, list.get(i).get("CXZRATE").toString()));
				
				sheet.addCell(new Number(24, y, Double.parseDouble(list.get(i).get("OXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(25, y, Double.parseDouble(list.get(i).get("OPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(26, y, Double.parseDouble(list.get(i).get("OLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(27, y, Double.parseDouble(list.get(i).get("OLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				
				sheet.addCell(new Number(28, y, Double.parseDouble(list.get(i).get("EXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(29, y, Double.parseDouble(list.get(i).get("EPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(30, y, Double.parseDouble(list.get(i).get("ELC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(31, y, Double.parseDouble(list.get(i).get("ELCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				
				sheet.addCell(new Number(32, y, Double.parseDouble(list.get(i).get("LXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(33, y, Double.parseDouble(list.get(i).get("LPJKL").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(34, y, Double.parseDouble(list.get(i).get("LLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(35, y, Double.parseDouble(list.get(i).get("LLCKL").toString()),new WritableCellFormat(wf,displayFormat)));
				
				sheet.addCell(new Number(36, y, Double.parseDouble(list.get(i).get("HABXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(37, y, Double.parseDouble(list.get(i).get("HABLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(38, y, Double.parseDouble(list.get(i).get("HABXZLC").toString()),new WritableCellFormat(wf,displayFormat)));
			
				sheet.addCell(new Number(39, y, Double.parseDouble(list.get(i).get("HABCXZ").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(40, y, Double.parseDouble(list.get(i).get("HABCLC").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(41, y, Double.parseDouble(list.get(i).get("HABCXZLC").toString()),new WritableCellFormat(wf,displayFormat)));
				
				
				
			}
		}
		++y;
	}

}
