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
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ComplaintDealReportformsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 抱怨处理满意率统计
 * @author wangming
 *
 */
public class ComplaintDealReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//抱怨处理满意率统计初始化页面
	private final String complaintDealReportformsUrl = "/jsp/customerRelationships/reportformsManage/complaintDealReportforms.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void complaintDealReportformsInit(){
		try{
			//CommonUtilActions commonUtilActions = new CommonUtilActions();
			//抱怨对象
			//act.setOutData("cpObjectList", commonUtilActions.getTmOrgPOAndTop());
			//小区及省分
			//act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			//报表类型
			act.setOutData("reportTypeList", getReportType());
			
			act.setForword(complaintDealReportformsUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"抱怨处理满意率统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryComplaintDealReportforms(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			String reportType = CommonUtils.checkNull(request.getParamValue("reportType"));  
			
			
			ComplaintDealReportformsDao dao = ComplaintDealReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			int tatal = dao.getTotalComplaintDealReportforms(dealName, dateStart,dateEnd);
			if(tatal  == 0){
				act.setOutData("ps", new PageResult<Map<String,Object>>());
			}else{
				if(reportType.equals("1")){
					PageResult<Map<String,Object>> complaintDealReportformsData = dao.queryComplaintDealReportformsByTmOrg(dealName,dateStart,dateEnd,Constant.PAGE_SIZE_MAX,curPage);
					setToatlName(complaintDealReportformsData.getRecords());
					act.setOutData("ps", complaintDealReportformsData);
				}else if(reportType.equals("2")){
					PageResult<Map<String,Object>> complaintDealReportformsData = dao.queryComplaintDealReportformsBySmalltmorg(dealName,dateStart,dateEnd,Constant.PAGE_SIZE_MAX,curPage);
					setToatlName(complaintDealReportformsData.getRecords());
					act.setOutData("ps", complaintDealReportformsData);
				}else if(reportType.equals("3")){
					PageResult<Map<String,Object>> complaintDealReportformsData = dao.queryComplaintDealReportformsByObject(dealName,dateStart,dateEnd,Constant.PAGE_SIZE_MAX,curPage);
					setToatlName(complaintDealReportformsData.getRecords());
					act.setOutData("ps", complaintDealReportformsData);
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
	
	public void complaintDealReportformsExcel(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			String reportType = CommonUtils.checkNull(request.getParamValue("reportType"));    
			
			ComplaintDealReportformsDao dao = ComplaintDealReportformsDao.getInstance();
			int tatal = 100 ;//dao.getTotalComplaintDealReportforms(dealName, dateStart,dateEnd);
			List<Map<String,Object>> complaintDealReportformsData = null;
			String reportTypeName = "";
			if(reportType.equals("1")){
				reportTypeName = "大区";
				complaintDealReportformsData = dao.queryComplaintDealReportformsByTmOrgList(dealName,dateStart,dateEnd);
			}else if(reportType.equals("2")){
				reportTypeName = "省份";
				complaintDealReportformsData = dao.queryComplaintDealReportformsBySmalltmorgList(dealName,dateStart,dateEnd);
			}else if(reportType.equals("3")){
				reportTypeName = "商家";
				complaintDealReportformsData = dao.queryComplaintDealReportformsByObjectList(dealName,dateStart,dateEnd);
			}
			
			
			if(tatal == 0){
				act.setForword(complaintDealReportformsUrl);
			}else if(tatal>10000 && tatal>0) {
				act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");
				act.setForword(complaintDealReportformsUrl);
			}else{
				complaintDealReportformsData = setToatlName(complaintDealReportformsData);
				complaintDealReportformsDataToExcel(complaintDealReportformsData,reportTypeName);
				act.setForword(complaintDealReportformsUrl);
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void complaintDealReportformsDataToExcel(List<Map<String, Object>> list,String reportTypeName) throws Exception{
		String[] head=new String[9];
		head[0]=reportTypeName;
		head[1]="需考核抱怨总数";
		head[2]="已关闭数量";
		head[3]="满意数量";
		head[4]="一次满意数量";
		head[5]="不满意数量";
		head[6]="满意率（%）";
		head[7]="一次满意率（%）";
		head[8]="不满意率（%）";
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[9];
					detail[0] = CommonUtils.checkNull(map.get("TYPENAME"));
					detail[1] = CommonUtils.checkNull(map.get("TOTAL"));
					detail[2] = CommonUtils.checkNull(map.get("CLOSED"));
					detail[3] = CommonUtils.checkNull(map.get("GOODC"));
					detail[4] = CommonUtils.checkNull(map.get("ONCEGOODC"));
					detail[5] = CommonUtils.checkNull(map.get("BADC"));
					detail[6] = CommonUtils.checkNull(map.get("GOODR"));
					detail[7] = CommonUtils.checkNull(map.get("OGOODR"));
					detail[8] = CommonUtils.checkNull(map.get("BADR"));
					
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "抱怨处理满意率统计.xls";
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
		map.put("value", 1);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按省份统计");
		map.put("value", 2);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("key", "按商家统计");
		map.put("value", 3);
		list.add(map);
	
		return list;
	}
	
}
