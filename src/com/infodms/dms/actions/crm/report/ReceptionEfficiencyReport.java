package com.infodms.dms.actions.crm.report;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.report.ClueReportDao;
import com.infodms.dms.dao.crm.report.ReceptionEfficiencyDao;
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
 * @Title: 潜客跟踪统计表Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date:  2015-02-05
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class   ReceptionEfficiencyReport{
	
	public Logger logger = Logger.getLogger(ReceptionEfficiencyReport.class);
	ReceptionEfficiencyDao dao  = ReceptionEfficiencyDao.getInstance();
	ClueReportDao cDao  = ClueReportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
    private ResponseWrapper response = act.getResponse();
	private final String initUrl = "/jsp/crm/report/receptionEfficiencyInit.jsp";
	private final String queryUrl = "/jsp/crm/report/receptionEfficiencyBalance.jsp";
									 
	/**
	 * 潜客跟踪统计表
	 * 页面初始化
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜客跟踪统计表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 潜客跟踪统计表汇总表
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
			logonUser.getUserType();//判断经销商和车厂数据
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
			if ( Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){
				balanceList=dao.getStatisticsDaoSelect(map);
				act.setOutData("dutyType",logonUser.getDutyType());
			} else{
				balanceList=dao.getStatisticsDaoSelectAll(map);
				act.setOutData("userType","false");
				act.setOutData("startDate",startDate);
				act.setOutData("endDate",endDate);
				act.setOutData("dealerCode",dealerCode);
				act.setOutData("seriesId",seriesId);
			}
			
			act.setOutData("balanceList",balanceList);
            
            act.setForword(queryUrl);

		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜客跟踪统计表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 潜客跟踪统计表dcrc经销商详情
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"潜客跟踪统计表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void doDownload() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			// 导出的文件名
			String fileName = "潜客跟踪统计表.xls";
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
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "潜客跟踪统计表");
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
		WritableSheet sheet = workbook.createSheet("潜客跟踪统计表", 0);
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
		sheet.addCell(new Label(4, y, "销售组"));
		sheet.addCell(new Label(5, y, "销售顾问"));
		sheet.addCell(new Label(8, y, "跟踪"));
		sheet.addCell(new Label(12, y, "邀约"));
		sheet.addCell(new Label(16, y, "保有客户回访"));
		sheet.addCell(new Label(6, y+1, "计划跟踪数"));
		sheet.addCell(new Label(7, y+1, "实际跟踪数"));
		sheet.addCell(new Label(8, y+1, "占比"));
		sheet.addCell(new Label(9, y+1, "计划线索邀约数"));
		sheet.addCell(new Label(10, y+1, "实际线索邀约数"));
		sheet.addCell(new Label(11, y+1, "占比"));
		sheet.addCell(new Label(12, y+1, "计划再次邀约数"));
		sheet.addCell(new Label(13, y+1, "实际再次邀约数"));
		sheet.addCell(new Label(14, y+1, "占比"));
		sheet.addCell(new Label(15, y+1, "保有客户计划回访数"));
		sheet.addCell(new Label(16, y+1, "保有客户实际回访数"));
		sheet.addCell(new Label(17, y+1, "占比"));
		++y;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i).get("PQ_ORG_NAME"))));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Label(4, y, list.get(i).get("GROUP_NAME").toString()));
				sheet.addCell(new Label(5, y, list.get(i).get("NAME").toString()));
				sheet.addCell(new Number(6, y, Double.parseDouble(list.get(i).get("JHSCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(7, y, Double.parseDouble(list.get(i).get("SJSCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(8, y, list.get(i).get("GZRATE").toString()));
				sheet.addCell(new Number(9, y, Double.parseDouble(list.get(i).get("XSZLCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(10, y, Double.parseDouble(list.get(i).get("SJXSCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(11, y, list.get(i).get("XSYYRATE").toString()));
				sheet.addCell(new Number(12, y, Double.parseDouble(list.get(i).get("ZYYCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(13, y, Double.parseDouble(list.get(i).get("ZDDCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(14, y, list.get(i).get("ZYYRATE").toString()));
				sheet.addCell(new Number(15, y, Double.parseDouble(list.get(i).get("HFCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(16, y, Double.parseDouble(list.get(i).get("HFWCCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(17, y, list.get(i).get("HFRATE").toString()));
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
		WritableSheet sheet = workbook.createSheet("潜客跟踪统计表", 0);
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
 		sheet.addCell(new Label(6, y, "跟踪"));
		sheet.addCell(new Label(10, y, "邀约"));
		sheet.addCell(new Label(14, y, "保有客户回访"));
		sheet.addCell(new Label(4, y+1, "计划跟踪数"));
		sheet.addCell(new Label(5, y+1, "实际跟踪数"));
		sheet.addCell(new Label(6, y+1, "占比"));
		sheet.addCell(new Label(7, y+1, "计划线索邀约数"));
		sheet.addCell(new Label(8, y+1, "实际线索邀约数"));
		sheet.addCell(new Label(9, y+1, "占比"));
		sheet.addCell(new Label(10, y+1, "计划再次邀约数"));
		sheet.addCell(new Label(11, y+1, "实际再次邀约数"));
		sheet.addCell(new Label(12, y+1, "占比"));
		sheet.addCell(new Label(13, y+1, "保有客户计划回访数"));
		sheet.addCell(new Label(14, y+1, "保有客户实际回访数"));
		sheet.addCell(new Label(15, y+1, "占比"));
		++y;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				++y;
				sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(list.get(i).get("PQ_ORG_NAME"))));
				sheet.addCell(new Label(2, y, list.get(i).get("DEALER_CODE").toString()));
				sheet.addCell(new Label(3, y, list.get(i).get("DEALER_SHORTNAME")==null?"":list.get(i).get("DEALER_SHORTNAME").toString()));
				sheet.addCell(new Number(4, y, Double.parseDouble(list.get(i).get("JHSCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(5, y, Double.parseDouble(list.get(i).get("SJSCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(6, y, list.get(i).get("GZRATE").toString()));
				sheet.addCell(new Number(7, y, Double.parseDouble(list.get(i).get("XSZLCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(8, y, Double.parseDouble(list.get(i).get("SJXSCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(9, y, list.get(i).get("XSYYRATE").toString()));
				sheet.addCell(new Number(10, y, Double.parseDouble(list.get(i).get("ZYYCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(11, y, Double.parseDouble(list.get(i).get("ZDDCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(12, y, list.get(i).get("ZYYRATE").toString()));
				sheet.addCell(new Number(13, y, Double.parseDouble(list.get(i).get("HFCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Number(14, y, Double.parseDouble(list.get(i).get("HFWCCOUNT").toString()),new WritableCellFormat(wf,displayFormat)));
				sheet.addCell(new Label(15, y, list.get(i).get("HFRATE").toString()));
				 
			}
		}
		++y;
	}

}
