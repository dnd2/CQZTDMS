package com.infodms.dms.actions.report.service;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.report.serviceReport.DealerFinFDetailDao;
import com.infodms.dms.dao.report.serviceReport.DealerReportFormsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;


public class DealerFinFDetail 
{
	 private Logger logger = Logger.getLogger(DealerFinFDetailDao.class);
	 private final DealerFinFDetailDao dao = DealerFinFDetailDao.getInstance();
	 private final String DEALER_FINF_DETAIL = "/jsp/report/service/Dealer_Fin_Detail.jsp";
	 private final String THREE_GUARANTEES_DETAIL = "/jsp/report/service/Three_guarantees.jsp";
	 
	 
	 
	 public void Three_guarantees()
	 {
		 ActionContext act = ActionContext.getContext();
	    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    	RequestWrapper request = act.getRequest();
	    	DealerReportForms forms = new DealerReportForms();
	    	try {
	    		act.setOutData("typeList", forms.typeInit());
				act.setOutData("specialInit", forms.specialInit());
				act.setOutData("activityInit", forms.activityInit());
				CommonUtilActions commonUtilActions = new CommonUtilActions();
				//大区
				act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
	    		act.setForword(THREE_GUARANTEES_DETAIL);
	    		
	    	} catch (Exception e) {
	    		BizException e1 = new BizException(act, e,
	    				ErrorCodeConstant.QUERY_FAILURE_CODE, "正负激励导入加载");
	    		logger.error(logonUser, e1);
	    		act.setException(e1);
	    	}
	    	
	 }
	 
	 public void Three_guarantees_json()
	 {
		 ActionContext act = ActionContext.getContext();
	    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    	RequestWrapper request = act.getRequest();
		 try{
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));   
				String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); 
				String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));   
				String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));   
				String claimType = CommonUtils.checkNull(request.getParamValue("typeH"));  			 
				String feeType = CommonUtils.checkNull(request.getParamValue("specialH"));   
				String activityType = CommonUtils.checkNull(request.getParamValue("activityH")); 
				
				String AStartDate = CommonUtils.checkNull(request.getParamValue("AStartDate"));   
				String AEndDate = CommonUtils.checkNull(request.getParamValue("AEndDate"));   
				String FStartDate = CommonUtils.checkNull(request.getParamValue("FStartDate"));  
				String FEndDate = CommonUtils.checkNull(request.getParamValue("FEndDate"));
				
				if(claimType!=null&& !"".equals(claimType)) claimType = claimType.replaceAll(",", "','");
				if(feeType!=null&& !"".equals(feeType)) feeType = feeType.replaceAll(",", "','");
				if(activityType!=null&& !"".equals(activityType)) activityType = activityType.replaceAll(",", "','");
				
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				PageResult<Map<String,Object>> dealerReportFormsData = dao.queryDealerReportForms(dealerCode, dealerName, tmorg, tmOrgSmall, claimType, feeType, activityType, AStartDate, AEndDate, FStartDate, FEndDate, Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", dealerReportFormsData);
			}catch(Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务商上报材料费情况表查询");
				logger.error(logger,e1);
				act.setException(e1);
			}
	 }
	 
	 public void subjectNoExcel(){
		 ActionContext act = ActionContext.getContext();
	    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    	RequestWrapper request = act.getRequest();
			try{
				String subject_no = CommonUtils.checkNull(request.getParamValue("subject_no"));   
				String dealer_id = CommonUtils.checkNull(request.getParamValue("dealer_id")); 
				
				StringBuffer sql= new StringBuffer();
				sql.append("select t.W_name WNAME,\n" );
				sql.append("       t.W_add WADD,\n" );
				sql.append("       to_char(t.publish_date, 'yyyy-mm-dd') PUBLISHDATE,\n" );
				sql.append("       t.conduct_cont CONDUCTCONT,\n" );
				sql.append("       t.DEALER_ID ,\n" );
				sql.append("        b.DEALER_CODE, ");
				sql.append("       b.DEALER_NAME ,\n" );
				sql.append("       t.MEDIA_NAME\n" );
				sql.append("  from TT_AS_activity_conduct t ,TT_AS_ACTIVITY_SUBJECT a,TM_DEALER b\n" );
				sql.append(" where 1=1 \n" );
				if(Utility.testString(dealer_id))
				{
					sql.append("  and  t.DEALER_ID =   "+dealer_id);
				}
				sql.append("   and t.SUMMARY_ID = a.SUBJECT_ID\n" );
				sql.append("   and a.SUBJECT_NO like '%"+subject_no+"%'\n" );
				sql.append("   and t.DEALER_ID = b.DEALER_ID");
				List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
				subjectNoExcelToExcel(list,request);
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
				logger.error(logonUser,e1);
				act.setException(e1);
			}

		}
	 
	 
	 private void subjectNoExcelToExcel(List<Map<String, Object>> list,RequestWrapper request) throws Exception{
			String[] head=new String[10];
			head[0]="服务站代码";
			head[1]="服务站名称";
			head[2]="宣传方式";
			head[3]="媒体名称";
			head[4]="链接/版面/音频/视频/其他";
			head[5]="宣传主题";
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[] detail=new String[10];
						detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
						detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
						detail[2] = CommonUtils.checkNull(map.get("WNAME"));
						detail[3] = CommonUtils.checkNull(map.get("MEDIA_NAME"));
						detail[4] = CommonUtils.checkNull(map.get("WADD"));
						detail[5] = CommonUtils.checkNull(map.get("CONDUCTCONT"));
						list1.add(detail);
			    	}
			    }
				
		    }
		    String name = "主题宣传.xls";
			String sheetName ="服务商主题宣传统计";
		    this.exportEx(ActionContext.getContext().getResponse(), request, head, list1,name,sheetName);
		}	
	 
	 
	 
	 public void dealerReportFormsExcel(){
		 ActionContext act = ActionContext.getContext();
	    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    	RequestWrapper request = act.getRequest();
			try{
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));   
				String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); 
				String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));   
				String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));   
				String claimType = CommonUtils.checkNull(request.getParamValue("typeH"));  	//索赔类型		 
				String feeType = CommonUtils.checkNull(request.getParamValue("specialH"));  //特殊费用类型 
				String activityType = CommonUtils.checkNull(request.getParamValue("activityH")); //活动类型
				
				String AStartDate = CommonUtils.checkNull(request.getParamValue("AStartDate"));   
				String AEndDate = CommonUtils.checkNull(request.getParamValue("AEndDate"));   
				String FStartDate = CommonUtils.checkNull(request.getParamValue("FStartDate"));  
				String FEndDate = CommonUtils.checkNull(request.getParamValue("FEndDate"));
				
				if(claimType!=null&& !"".equals(claimType)) claimType = claimType.replaceAll(",", "','");
				if(feeType!=null&& !"".equals(feeType)) feeType = feeType.replaceAll(",", "','");
				if(activityType!=null&& !"".equals(activityType)) activityType = activityType.replaceAll(",", "','");
				
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String,Object>> dealerReportFormsData = dao.queryDealerReportForms(dealerCode, dealerName, tmorg, tmOrgSmall, claimType, feeType, 
						activityType, AStartDate, AEndDate, FStartDate, FEndDate, 20000, curPage);

				dealerReportFormsDataToExcel(dealerReportFormsData.getRecords(),request);
			
				Three_guarantees();
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
				logger.error(logonUser,e1);
				act.setException(e1);
			}

		}
	 
	 private void dealerReportFormsDataToExcel(List<Map<String, Object>> list,RequestWrapper request) throws Exception{
			String[] head=new String[10];
			head[0]="服务站代码";
			head[1]="服务站名称";
			head[2]="结算总金额";
			head[3]="维修台数";
			
			
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[] detail=new String[10];
						detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
						detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
						detail[2] = CommonUtils.checkNull(map.get("BALANCE_AMOUNT"));
						detail[3] = CommonUtils.checkNull(map.get("REPAIR_TOTAL"));
						list1.add(detail);
			    	}
			    }
				String name = "服务商单台三包费统计.xls";
				String sheetName ="服务商单台三包费统计";
				this.exportEx(ActionContext.getContext().getResponse(), request, head, list1,name,sheetName);
		    }
		}	
	 
	 public static Object exportEx(ResponseWrapper response,
				RequestWrapper request, String[] head, List<String[]> list,String name,String sheetName)
				throws Exception {
			jxl.write.WritableWorkbook wwb = null;
			OutputStream out = null;
			try {
				response.setContentType("application/octet-stream");
			    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
				out = response.getOutputStream();
				wwb = Workbook.createWorkbook(out);
				jxl.write.WritableSheet ws = wwb.createSheet(sheetName, 0);

				if (head != null && head.length > 0) {
					for (int i = 0; i < head.length; i++) {
						ws.addCell(new Label(i, 0, head[i]));
					}
				}
				int pageSize=list.size()/30000;
				for (int z = 1; z < list.size() + 1; z++) {
					String[] str = list.get(z - 1);
					for (int i = 0; i < str.length; i++) {
						ws.addCell(new Label(i, z, str[i]));
					}
				}
				wwb.write();
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (null != wwb) {
					wwb.close();
				}
				if (null != out) {
					out.close();
				}
			}
			return null;
		}
	 
	 public void exportDealerByDlrInit(){
	    	ActionContext act = ActionContext.getContext();
	    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    	RequestWrapper request = act.getRequest();
	    	try {
	    		
	    		act.setForword(DEALER_FINF_DETAIL);
	    		
	    	} catch (Exception e) {
	    		BizException e1 = new BizException(act, e,
	    				ErrorCodeConstant.QUERY_FAILURE_CODE, "正负激励导入加载");
	    		logger.error(logonUser, e1);
	    		act.setException(e1);
	    	}
	    	
	    }
	 public void queryOEMDealer () 
	 {
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				RequestWrapper request = act.getRequest();
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				//从session中取得车厂公司id		
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);		
				String dealerCode = request.getParamValue("DEALER_CODE");
				String dealerName = request.getParamValue("DEALER_NAME");
				String deductStartDate = CommonUtils.checkNull(request.getParamValue("deductStartDate"));
				String deductEndDate = CommonUtils.checkNull(request.getParamValue("deductEndDate"));
				String SubsidiesType = CommonUtils.checkNull(request.getParamValue("SubsidiesType"));
				String ButieBh = CommonUtils.checkNull(request.getParamValue("ButieBh"));
				String fine_type = CommonUtils.checkNull(request.getParamValue("fine_type"));
				Map<String,String> param = new HashMap();
				param.put("dealerCode", dealerCode);
				param.put("dealerName", dealerName);
				param.put("deductStartDate", deductStartDate);
				param.put("deductEndDate", deductEndDate);
				param.put("SubsidiesType", SubsidiesType);
				param.put("ButieBh", ButieBh);
				param.put("fine_type", fine_type);
				param.put("oemCompanyId", String.valueOf(oemCompanyId));
				PageResult<Map<String, Object>> ps  =  dao.exportqueryOemDealer(param, Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);	
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
	 
	  public void resourcesAuditDown()
	  {
			ActionContext act = ActionContext.getContext();
			OutputStream os = null;
			RequestWrapper request = act.getRequest();

			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try{
				
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);		
				String dealerCode = request.getParamValue("DEALER_CODE");
				String dealerName = request.getParamValue("DEALER_NAME");
				String deductStartDate = CommonUtils.checkNull(request.getParamValue("deductStartDate"));
				String deductEndDate = CommonUtils.checkNull(request.getParamValue("deductEndDate"));
				String SubsidiesType = CommonUtils.checkNull(request.getParamValue("SubsidiesType"));
				String ButieBh = CommonUtils.checkNull(request.getParamValue("ButieBh"));
				String fine_type = CommonUtils.checkNull(request.getParamValue("fine_type"));

				Map<String,String> param = new HashMap();
				param.put("dealerCode", dealerCode);
				param.put("dealerName", dealerName);
				param.put("deductStartDate", deductStartDate);
				param.put("deductEndDate", deductEndDate);
				param.put("SubsidiesType", SubsidiesType);
				param.put("ButieBh", ButieBh);
				param.put("fine_type", fine_type);
				param.put("oemCompanyId", String.valueOf(oemCompanyId));

			   
				List<Map<String, Object>> ps =  dao.exportqueryOemDealer(param);
				Map map=new HashMap();
				ResponseWrapper response = act.getResponse();
				// 导出的文件名
				String fileName = "正负激励明细.csv";
				// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				// 定义一个集合
				List<List<Object>> list = new LinkedList<List<Object>>();
				// 标题
				List<Object> listTemp = new LinkedList<Object>();
				listTemp.add("序号");
				listTemp.add("服务站代码");
				listTemp.add("服务站名称");
				listTemp.add("补贴类型");
				listTemp.add("奖惩劳务费");
				listTemp.add("奖惩材料费");
				listTemp.add("费用标志");
				listTemp.add("可用标志");
				listTemp.add("补贴编号");
				listTemp.add("导入人");
				listTemp.add("录入时间");
				listTemp.add("产地");
				listTemp.add("备注");
				listTemp.add("开票通知单号");
				list.add(listTemp);
				List<Map<String, Object>> rslist = ps;
				if(rslist!=null && rslist.size() > 0){
				for (int i = 0; i < rslist.size(); i++) {
					map = rslist.get(i);
					List<Object> listValue = new LinkedList<Object>();
					listValue = new LinkedList<Object>();
					listValue.add(i+1);
					listValue.add(map.get("DEALER_CODE") != null ? map.get("DEALER_CODE") : "");
					listValue.add(map.get("DEALER_NAME") != null ? map.get("DEALER_NAME") : "");
					listValue.add(map.get("REMARK") != null ? map.get("REMARK") : "");
					listValue.add(map.get("LABOUR_SUM") != null ? map.get("LABOUR_SUM") : "");
					listValue.add(map.get("DATUM_SUM") != null ? map.get("DATUM_SUM") : "");
					String Status=null;
					if(map.get("FINE_TYPE") != null){
						if(Integer.parseInt(map.get("FINE_TYPE").toString())==Constant.FINE_TYPE_02){
							Status="奖励";
						}else{
							Status="扣款";
						}
						
					}else {
						Status="";
					}
					listValue.add(Status);
					
					Status = "";
					if(map.get("PAY_STATUS") != null){
						if(map.get("PAY_STATUS").toString().equals(Constant.PAY_STATUS_01)){
							Status="可用";
						}else{
							Status="不可用";
						}
						
					}else {
						Status="";
					}
					listValue.add(Status);
					
					listValue.add(map.get("LABOUR_BH") != null ? map.get("LABOUR_BH") : "");
					listValue.add(map.get("NAME") != null ? map.get("NAME") : "");
					listValue.add(map.get("FINE_DATE") != null ? map.get("FINE_DATE") : "");
					listValue.add(map.get("AREA_NAME") != null ? map.get("AREA_NAME") : "");
					listValue.add(map.get("FINE_REASON") != null ? map.get("FINE_REASON") : "");
					listValue.add(map.get("BALANCE_ODER") != null ? map.get("BALANCE_ODER") : "");
				
					list.add(listValue);
				}
				}
				os = response.getOutputStream();
				CsvWriterUtil.writeCsv(list, os);
				os.flush();			
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"正负激励导出失败");
				logger.error(logonUser,e1);
				act.setException(e1);
				}
			
			
		}
}
