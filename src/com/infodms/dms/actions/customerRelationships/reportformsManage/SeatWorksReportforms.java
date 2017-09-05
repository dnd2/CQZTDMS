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
import com.infodms.dms.dao.customerRelationships.ComplaintCloseReportformsDao;
import com.infodms.dms.dao.customerRelationships.ComplaintTotalReportformsDao;
import com.infodms.dms.dao.customerRelationships.SeatWorksReportformsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 坐席工作量统计
 * @author wangming
 *
 */
public class SeatWorksReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//坐席工作量统计初始化页面
	private final String seatWorksReportformsUrl = "/jsp/customerRelationships/reportformsManage/seatWorksReportforms.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void seatWorksReportformsInit(){
		try{
			act.setForword(seatWorksReportformsUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"坐席工作量统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void querySeatWorksReportforms(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName")); 
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd")); 
			String paiStart = CommonUtils.checkNull(request.getParamValue("paiStart"));  				
			String paiEnd = CommonUtils.checkNull(request.getParamValue("paiEnd"));   
			SeatWorksReportformsDao dao = SeatWorksReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			
			PageResult<Map<String,Object>> seatTalksReportformsData = dao.querySeatWorksReportforms(dealName,dateStart,dateEnd,paiStart,paiEnd,Constant.PAGE_SIZE_MAX,curPage);
			if(seatTalksReportformsData!=null){
				if(seatTalksReportformsData.getRecords() != null && seatTalksReportformsData.getRecords().size()>0){
					seatTalksReportformsData.getRecords().get(seatTalksReportformsData.getRecords().size()-1).put("NAME", "");
				}
			}
			act.setOutData("ps", seatTalksReportformsData);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"坐席工作量统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void seatWorksReportformsExcel(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));
			String paiStart = CommonUtils.checkNull(request.getParamValue("paiStart"));  				
			String paiEnd = CommonUtils.checkNull(request.getParamValue("paiEnd"));
			
			
			
			SeatWorksReportformsDao dao = SeatWorksReportformsDao.getInstance();
			List<Map<String,Object>> seatTalksReportformsData = (dao.querySeatWorksReportforms(dealName, dateStart, dateEnd,paiStart,paiEnd,999999,1)).getRecords();

			
			if(seatTalksReportformsData!=null&&seatTalksReportformsData.size()>0){
				seatTalksReportformsDataToExcel(seatTalksReportformsData);
				if(seatTalksReportformsData.size()>10000) {act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");}
			}
			act.setForword(seatWorksReportformsUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void seatTalksReportformsDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[14];
		head[0]="工号";
		head[1]="姓名";
		head[2]="白班投诉电话";
		head[3]="晚班投诉电话";
		head[4]="白班咨询电话";
		head[5]="晚班咨询电话";
		head[6]="'1333'抽查回访（成功）";
		head[7]="'1333'抽查回访（成功）";
		head[8]="客户关怀回访（成功）";
		head[9]="客户关怀回访（不成功）";
		head[10]="市场调查回访（成功）";
		head[11]="市场调查回访（不成功）";
		head[12]="服务站满意度回访（成功）";
		head[13]="服务站满意度回访（不成功）";

		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[] detail=new String[14];
					detail[0] = CommonUtils.checkNull(map.get("ACC"));
					detail[1] = CommonUtils.checkNull(map.get("NAME"));
					detail[2] = CommonUtils.checkNull(map.get("COMCM"));
					detail[3] = CommonUtils.checkNull(map.get("COMCN"));
					detail[4] = CommonUtils.checkNull(map.get("COUCM"));
					detail[5] = CommonUtils.checkNull(map.get("COUCN"));
					detail[6] = CommonUtils.checkNull(map.get("RVDS"));
					detail[7] = CommonUtils.checkNull(map.get("RVEF"));
					detail[8] = CommonUtils.checkNull(map.get("RVFS"));
					detail[9] = CommonUtils.checkNull(map.get("RVGF"));
					detail[10] = CommonUtils.checkNull(map.get("RVHS"));
					detail[11] = CommonUtils.checkNull(map.get("RVIF"));
					detail[12] = CommonUtils.checkNull(map.get("RVJS"));
					detail[13] = CommonUtils.checkNull(map.get("RVKF"));

					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "坐席工作量统计.xls";
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
