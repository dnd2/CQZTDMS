package com.infodms.dms.actions.customerRelationships.reportformsManage;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import com.infodms.dms.dao.customerRelationships.ComplaintCloseReportformsDao;
import com.infodms.dms.dao.customerRelationships.ComplaintTotalReportformsDao;
import com.infodms.dms.dao.customerRelationships.ReturnVisitReportformsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CalendarUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 回访结果统计
 * @author wangming
 *
 */
public class ReturnVisitReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//回访结果统计初始化页面
	private final String returnVisitReportformsUrl = "/jsp/customerRelationships/reportformsManage/returnVisitReportforms.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void returnVisitReportformsInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//抱怨对象
			//act.setOutData("cpObjectList", commonUtilActions.getTmOrgPOAndTop());
			//小区及省分
			//act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			//报表类型
			//act.setOutData("reportTypeList", getReportType());
			
			act.setForword(returnVisitReportformsUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"回访结果统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryReturnVisitReportforms(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			//String reportType = CommonUtils.checkNull(request.getParamValue("reportType"));  
			
			
			ReturnVisitReportformsDao dao = ReturnVisitReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> returnVisitReportformsData = dao.queryReturnVisitReportforms(dealName,dateStart,dateEnd,Constant.PAGE_SIZE_MAX,curPage);
			act.setOutData("ps", returnVisitReportformsData);
				
				
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"回访结果统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void complaintClosedReportformsExcel(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			
			ReturnVisitReportformsDao dao = ReturnVisitReportformsDao.getInstance();
			List<Map<String,Object>> returnVisitReportformsData = dao.queryReturnVisitReportformsList(dealName,dateStart,dateEnd);
			
			
			if(returnVisitReportformsData!=null&&returnVisitReportformsData.size()>0){
				returnVisitReportformsDataToExcel(returnVisitReportformsData);
				if(returnVisitReportformsData.size()>10000) {act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");}
			}
			act.setForword(returnVisitReportformsUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void returnVisitReportformsDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[27];
		head[0]= "分类";
		head[1]="回访总数";
		head[2]="成功回访数量";
		head[3]="不成功回访数量";
		head[4]="回访成功率";
		head[5]="一次成功回访数量";
		head[6]="一次不成功回访数量";
		head[7]="一次成功回访率";
		head[8]="满意数量";
		head[9]="不满意数量";
		head[10]="满意率";
		head[11]="一次满意数量";
		head[12]="一次满意率";
		
		head[13]= "无人接听数量";
		head[14]="无人接听率";
		head[15]="拒访数量";
		head[16]="拒访率";
		head[17]="空号数量";
		head[18]="空号率";
		head[19]="电话有误数量";
		head[20]="电话有误率";
		head[21]="无法联系数量";
		head[22]="无法联系率";
		head[23]="无法接通数量";
		head[24]="无法接通率";
		head[25]="不成功回访数量";
		head[26]="不成功回访率";
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[] detail=new String[27];
					detail[0] = CommonUtils.checkNull(map.get("MODELNAME"));
					detail[1] = CommonUtils.checkNull(map.get("TOTAL"));
					detail[2] = CommonUtils.checkNull(map.get("SUCC"));
					detail[3] = CommonUtils.checkNull(map.get("FAILC"));
					detail[4] = CommonUtils.checkNull(map.get("SUCR"));
					detail[5] = CommonUtils.checkNull(map.get("ONCESUCC"));
					detail[6] = CommonUtils.checkNull(map.get("ONCEFAILC"));
					detail[7] = CommonUtils.checkNull(map.get("ONCESUCR"));
					detail[8] = CommonUtils.checkNull(map.get("SATISC"));
					detail[9] = CommonUtils.checkNull(map.get("NOSATISC"));
					detail[10] = CommonUtils.checkNull(map.get("SATISR"));
					detail[11] = CommonUtils.checkNull(map.get("ONCESATISC"));
					detail[12] = CommonUtils.checkNull(map.get("ONCESATISR"));
					
					detail[13] = CommonUtils.checkNull(map.get("FAIL1"));
					detail[14] = CommonUtils.checkNull(map.get("FAIL1R"));
					detail[15] = CommonUtils.checkNull(map.get("FAIL2"));
					detail[16] = CommonUtils.checkNull(map.get("FAIL2R"));
					detail[17] = CommonUtils.checkNull(map.get("FAIL3"));
					detail[18] = CommonUtils.checkNull(map.get("FAIL3R"));
					detail[19] = CommonUtils.checkNull(map.get("FAIL4"));
					detail[20] = CommonUtils.checkNull(map.get("FAIL4R"));
					detail[21] = CommonUtils.checkNull(map.get("FAIL5"));
					detail[22] = CommonUtils.checkNull(map.get("FAIL5R"));
					detail[23] = CommonUtils.checkNull(map.get("FAIL6"));
					detail[24] = CommonUtils.checkNull(map.get("FAIL6R"));
					detail[25] = CommonUtils.checkNull(map.get("FAIL7"));
					detail[26] = CommonUtils.checkNull(map.get("FAIL7R"));
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "回访结果统计.xls";
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

	
}
