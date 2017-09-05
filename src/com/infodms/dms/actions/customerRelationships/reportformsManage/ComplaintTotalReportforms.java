package com.infodms.dms.actions.customerRelationships.reportformsManage;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.customerRelationships.baseSetting.seatsSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ComplaintTotalReportformsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 抱怨总数统计
 * @author wangming
 *
 */
public class ComplaintTotalReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//抱怨总数统计初始化页面
	private final String complaintTotalReportformsUrl = "/jsp/customerRelationships/reportformsManage/complaintTotalReportforms.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void complaintTotalReportformsInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//抱怨对象
			//act.setOutData("cpObjectList", commonUtilActions.getTmOrgPOAndTop());
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//报表类型
			act.setOutData("reportTypeList", getReportType());
			
			act.setForword(complaintTotalReportformsUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"抱怨总数统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryComplaintTotalReportforms(){
		try{
			String dateYear = CommonUtils.checkNull(request.getParamValue("dateYear")); 
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));  
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			String cpObject = CommonUtils.checkNull(request.getParamValue("cpObject"));
			String reportType = CommonUtils.checkNull(request.getParamValue("reportType"));  
			
			
			ComplaintTotalReportformsDao dao = ComplaintTotalReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			int tatal = dao.getTotalComplaintDealReportforms(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject);
			if(tatal  == 0){
				act.setOutData("ps", new PageResult<Map<String,Object>>());
			}else{
				if(reportType.equals("1")){
					PageResult<Map<String,Object>> complaintTotalReportformsData = dao.queryComplaintTotalReportformsByCpObject(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject,Constant.PAGE_SIZE_MAX,curPage);
					setToatlName(complaintTotalReportformsData.getRecords());
					act.setOutData("ps", complaintTotalReportformsData);
				}else if(reportType.equals("2")){
					PageResult<Map<String,Object>> complaintTotalReportformsData = dao.queryComplaintTotalReportformsBySmallOrg(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject,Constant.PAGE_SIZE_MAX,curPage);
					setToatlName(complaintTotalReportformsData.getRecords());
					act.setOutData("ps", complaintTotalReportformsData);
				}else if(reportType.equals("3")){
					PageResult<Map<String,Object>> complaintTotalReportformsData = dao.queryComplaintTotalReportformsByCustom(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject,Constant.PAGE_SIZE_MAX,curPage);
					setToatlName(complaintTotalReportformsData.getRecords());
					act.setOutData("ps", complaintTotalReportformsData);
				}else if(reportType.equals("4")){
					PageResult<Map<String,Object>> complaintTotalReportformsData = dao.queryComplaintTotalReportformsByBizContent(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject,Constant.PAGE_SIZE_MAX,curPage);
					setToatlName(complaintTotalReportformsData.getRecords());
					act.setOutData("ps", complaintTotalReportformsData);
				}else if(reportType.equals("5")){
					PageResult<Map<String,Object>> complaintTotalReportformsData = dao.queryComplaintTotalReportformsByYear(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject,Constant.PAGE_SIZE_MAX,curPage);
					setToatlName(complaintTotalReportformsData.getRecords());
					act.setOutData("ps", complaintTotalReportformsData);
				}else if(reportType.equals("6")){
					PageResult<Map<String,Object>> complaintTotalReportformsData = dao.queryComplaintTotalReportformsByTmOrg(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject,Constant.PAGE_SIZE_MAX,curPage);
					setToatlName(complaintTotalReportformsData.getRecords());
					act.setOutData("ps", complaintTotalReportformsData);
				}
			}	
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"咨询类别统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	private List<Map<String,Object>> setToatlName(List<Map<String,Object>> data){
		if(data==null || data.size() == 0)return data;
		data.get(data.size()-1).put("TYPENAME", "全国");
		return data;
	}
	
	public void complaintTotalReportformsExcel(){
		try{
			String dateYear = CommonUtils.checkNull(request.getParamValue("dateYear")); 
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));  
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			String cpObject = CommonUtils.checkNull(request.getParamValue("cpObject"));
			String reportType = CommonUtils.checkNull(request.getParamValue("reportType")); 
			
			ComplaintTotalReportformsDao dao = ComplaintTotalReportformsDao.getInstance();
			int tatal = dao.getTotalComplaintDealReportforms(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject);
			List<Map<String,Object>> complaintTotalReportformsData = null;
			String reportTypeName = "";
			if(reportType.equals("1")){
				reportTypeName = "受理商家";
				complaintTotalReportformsData = dao.queryComplaintTotalReportformsByCpObjectList(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject);
			}else if(reportType.equals("2")){
				reportTypeName = "省份";
				complaintTotalReportformsData = dao.queryComplaintTotalReportformsBySmallOrgList(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject);
			}else if(reportType.equals("3")){
				reportTypeName = "受理部门";
				complaintTotalReportformsData = dao.queryComplaintTotalReportformsByCustomList(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject);
				complaintTotalReportformsData = setToatlName(complaintTotalReportformsData);
			}else if(reportType.equals("4")){
				reportTypeName = "类别";
				complaintTotalReportformsData = dao.queryComplaintTotalReportformsByBizContentList(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject);
			}else if(reportType.equals("5")){
				reportTypeName = "年份";
				complaintTotalReportformsData = dao.queryComplaintTotalReportformsByYearList(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject);
			}else if(reportType.equals("6")){
				reportTypeName = "大区";
				complaintTotalReportformsData = dao.queryComplaintTotalReportformsByTmOrgList(dateYear,tmOrgSmall,dealName, dateStart,dateEnd,cpObject);
			}
			if(tatal == 0){
				act.setForword(complaintTotalReportformsUrl);
			}else if(tatal>10000 && tatal>0) {
				act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");
				act.setForword(complaintTotalReportformsUrl);
			}else{
				complaintTotalReportformsData = setToatlName(complaintTotalReportformsData);
				complaintTotalReportformsDataToExcel(complaintTotalReportformsData,reportTypeName);
				act.setForword(complaintTotalReportformsUrl);
			}
				
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void complaintTotalReportformsDataToExcel(List<Map<String, Object>> list,String reportTypeName) throws Exception{
		String[] head=new String[19];
		head[0]=reportTypeName;
		head[1]="需考核抱怨总数";
		head[2]="实际受理抱怨总数";
		head[3]="移入上月考核数量";
		head[4]="移出至下月考核数量";
		head[5]="当月未转交或次月转交数量";
		head[6]="当月前跨月数量";
		head[7]="正在处理中数量";
		head[8]="已关闭数量";
		head[9]="强制关闭数量";
		head[10]="用户满意数量";
		head[11]="抱怨关闭率";
		head[12]="抱怨处理满意率";
		head[13]="平均处理时长[小时]";
		head[14]="平均处理时长(1天)[小时]";
		head[15]="平均处理时长(3天)[小时]";
		head[16]="平均处理时长(7天)[小时]";
		head[17]="当月前受理当月转交当月考核数";
		head[18]="当月前受理当月转交下月考核数";

		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[19];
					detail[0] = CommonUtils.checkNull(map.get("TYPENAME"));
					detail[1] = CommonUtils.checkNull(map.get("TOTAL"));
					detail[2] = CommonUtils.checkNull(map.get("ACCTOTAL"));
					detail[3] = CommonUtils.checkNull(map.get("INTOTAL"));
					detail[4] = CommonUtils.checkNull(map.get("OUTTOTAL"));
					detail[5] = CommonUtils.checkNull(map.get("UNTURNTOTAL"));
					detail[6] = CommonUtils.checkNull(map.get("UPTOTAL"));
					detail[7] = CommonUtils.checkNull(map.get("DOINGC"));
					detail[8] = CommonUtils.checkNull(map.get("CLOSEDC"));
					detail[9] = CommonUtils.checkNull(map.get("FORCECLOSEDC"));
					detail[10] = CommonUtils.checkNull(map.get("GOODC"));
					detail[11] = CommonUtils.checkNull(map.get("CLOSEDR"));
					detail[12] = CommonUtils.checkNull(map.get("GOODR"));
					detail[13] = CommonUtils.checkNull(map.get("AVGTOTAL"));
					detail[14] = CommonUtils.checkNull(map.get("AVGONEDAY"));
					detail[15] = CommonUtils.checkNull(map.get("AVGTHREEDAY"));
					detail[16] = CommonUtils.checkNull(map.get("AVGSEVENDAY"));
					detail[17] = CommonUtils.checkNull(map.get("YACCTOTAL"));
					detail[18] = CommonUtils.checkNull(map.get("NACCTOTAL"));
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "抱怨总数统计.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

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
	
	public List<Map<String,Object>> getReportType(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "按大区统计");
		map.put("value", 6);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按受理商家统计");
		map.put("value", 1);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按省份统计");
		map.put("value", 2);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按受理部门统计");
		map.put("value", 3);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按内容类型统计");
		map.put("value", 4);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按年统计");
		map.put("value", 5);
		list.add(map);
		
		return list;
	}
	
}
