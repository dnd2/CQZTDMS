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
import com.infodms.dms.dao.customerRelationships.ComplaintTypeReportformsDao;
import com.infodms.dms.dao.customerRelationships.ConsultTypeReportformsDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 抱怨分类统计
 * @author wangming
 *
 */
public class ComplaintTypeReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//抱怨分类统计初始化页面
	private final String complaintTypeReportformsUrl = "/jsp/customerRelationships/reportformsManage/complaintTypeReportforms.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void complaintTypeReportformsInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//抱怨对象
			act.setOutData("cpObjectList", commonUtilActions.getTmOrgPOAndTop());
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//报表类型
			act.setOutData("reportTypeList", getReportType());
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			
			act.setForword(complaintTypeReportformsUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"抱怨分类统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryComplaintTypeReportforms(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  
			String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));  				
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));  
			String reportType = CommonUtils.checkNull(request.getParamValue("reportType"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			
			
			ComplaintTypeReportformsDao dao = ComplaintTypeReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			int tatal = dao.getTotalComplaintTypeReportforms(dealName, dateStart,dateEnd,tmorg,tmOrgSmall,model);
			if(tatal  == 0){
				act.setOutData("ps", new PageResult<Map<String,Object>>());
			}else{
				if(reportType.equals("1")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByDealOrg(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("2")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsBySeries(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("3")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByMileageRange(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("4")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByBizContent(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("5")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByLevel(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("6")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByFaultPart(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("7")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByTmOrg(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("8")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsBySmalltmorg(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("9")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByBuyDateRange(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}else if(reportType.equals("10")){
					PageResult<Map<String,Object>> complaintTypeReportformsData = dao.queryComplaintTypeReportformsByDealRange(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model,Constant.PAGE_SIZE_MAX,curPage);
					act.setOutData("ps", complaintTypeReportformsData);
				}
			}	
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"咨询类别统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void complaintTypeReportformsExcel(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  
			String tmorg = CommonUtils.checkNull(request.getParamValue("tmorg"));  				
			String tmOrgSmall = CommonUtils.checkNull(request.getParamValue("tmOrgSmall"));  
			String reportType = CommonUtils.checkNull(request.getParamValue("reportType"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			
			ComplaintTypeReportformsDao dao = ComplaintTypeReportformsDao.getInstance();
			int tatal = dao.getTotalComplaintTypeReportforms(dealName, dateStart,dateEnd,tmorg,tmOrgSmall,model);
			List<Map<String,Object>> complaintTypeReportformsData = null;
			String reportTypeName = "";
			if(reportType.equals("1")){
				reportTypeName = "来源部门";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByDealOrgList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("2")){
				reportTypeName = "车种";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsBySeriesList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("3")){
				reportTypeName = "里程范围";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByMileageRangeList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("4")){
				reportTypeName = "类别";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByBizContentList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("5")){
				reportTypeName = "抱怨级别";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByLevelList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("6")){
				reportTypeName = "故障部件";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByFaultPartList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("7")){
				reportTypeName = "大区";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByTmOrgList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("8")){
				reportTypeName = "省市";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsBySmalltmorgList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("9")){
				reportTypeName = "购买期限";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByBuyDateRangeList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}else if(reportType.equals("10")){
				reportTypeName = "处理时长";
				complaintTypeReportformsData = dao.queryComplaintTypeReportformsByDealRangeList(dealName,dateStart,dateEnd,tmorg,tmOrgSmall,tatal,model);
			}
			
			
			if(tatal == 0){
				act.setForword(complaintTypeReportformsUrl);
			}else if(tatal>10000 && tatal>0) {
				act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");
				act.setForword(complaintTypeReportformsUrl);
			}else{
				complaintTypeReportformsDataToExcel(complaintTypeReportformsData,reportTypeName);
				act.setForword(complaintTypeReportformsUrl);
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void complaintTypeReportformsDataToExcel(List<Map<String, Object>> list,String reportTypeName) throws Exception{
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		String[] head=new String[3];
		head[0]=reportTypeName;
		head[1]="抱怨次数";
		head[2]="占抱怨总量的百分比（%）";
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[3];
					detail[0] = CommonUtils.checkNull(map.get("TYPENAME"));
					detail[1] = CommonUtils.checkNull(map.get("COUNTDESC"));
					detail[2] = CommonUtils.checkNull(map.get("COUNTRATE"));
					
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "抱怨分类统计.xls";
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
		map.put("value", 7);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按来源部门统计");
		map.put("value", 1);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按车种统计");
		map.put("value", 2);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按里程范围统计");
		map.put("value", 3);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按抱怨类型统计");
		map.put("value", 4);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按抱怨级别统计");
		map.put("value", 5);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按故障部件统计");
		map.put("value", 6);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按省市统计");
		map.put("value", 8);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按购买期限统计");
		map.put("value", 9);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按处理时长统计");
		map.put("value", 10);
		list.add(map);
		
		return list;
	}
	
}
