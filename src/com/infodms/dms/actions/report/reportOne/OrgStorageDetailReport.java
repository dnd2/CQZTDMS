package com.infodms.dms.actions.report.reportOne;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.report.OrgStorageDetailQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

/**
 * 各大区库存统计
 * @author Administrator
 *
 */
public class OrgStorageDetailReport {
	private Logger logger = Logger.getLogger(OrgStorageDetailReport.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private OrgStorageDetailQueryDao dao = OrgStorageDetailQueryDao.getInstance();
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String ORG_STORAGE_DETAIL_INIT = "/jsp/report/sales/orgStorgeDetailQuery.jsp";
	private final String ORG_STORAGE_DETAIL_INFO = "/jsp/report/sales/orgStorageDetailInfo.jsp";
	/*
	 * 各大区库存统计查询初始化 
	 */
	public void orgStorageDetailQuery(){
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(ORG_STORAGE_DETAIL_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
   /**
    * 查询
    */
	public void getOrgStorageDetailInfo(){
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Map<String, Object> param = new HashMap<String, Object>();
		    String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
		    param.put("areaId", areaId);
		    //得到所有车型
		    List<Map<String, Object>> modelList = dao.getModelList(param);
		    //得到所有车系
		    List<Map<String, Object>> seriesList = dao.getSeriesList(param);
		    //得到所有产地
		    List<Map<String, Object>> vehicleKindsList = dao.getVehicleKinds(param); //商用车、乘用车
			List<Map<String, Object>> result = dao.getOrgStorageDetailReportInfo(param,vehicleKindsList,seriesList,modelList,Constant.PAGE_SIZE_MAX, curPage);
			/*********************************/
			//商用车、乘用车循环
			List<String> title_list = new ArrayList<String>();
			List<String> column_list = new ArrayList<String>();
			title_list.add("片区");
			column_list.add("片区");
			if(vehicleKindsList != null && vehicleKindsList.size()>0){
				
				for(int j = 0 ; j < vehicleKindsList.size() ; j++){
					Map<String, Object> mapp = vehicleKindsList.get(j);
					String vehicleKind = mapp.get("VEHICLE_KIND").toString();	
					boolean flag = false;
					//车系循环
					if(seriesList != null && seriesList.size()>0){
						for(int k = 0 ; k < seriesList.size() ; k++){
							Map<String, Object> map = seriesList.get(k);
							String vehicle_kind = map.get("VEHICLE_KIND").toString();
							if(vehicleKind.equals(vehicle_kind)){
								flag = true;
								String series_id = map.get("GROUP_ID").toString();
								String series_name = map.get("GROUP_NAME").toString();
								//车型循环
								/*if(modelList != null && modelList.size()>0){
									for(int i = 0 ; i < modelList.size() ; i++){
										Map<String, Object> map_1 = modelList.get(i);
										String seriesId = map_1.get("SERIES_ID").toString();
										//循环当前车系下的车型
										if(series_id.equals(seriesId)){
											String MODEL_NAME = (String)map_1.get("MODEL_NAME");
											String MODEL_ID  = map_1.get("MODEL_ID").toString();
											title_list.add(MODEL_NAME);
											column_list.add("A"+MODEL_ID);
										}					
									}						
								}*/
								
								title_list.add(series_name+"小计");
								column_list.add(series_name+"小计");
							}				
						}
					}
					if(flag){
						title_list.add(vehicleKind+"合计");
						column_list.add(vehicleKind+"合计");
					}
				}
					title_list.add("总计");
					column_list.add("总计");

			}
			/*********************************/
			act.setOutData("title_list", title_list);
			act.setOutData("column_list", column_list);
			act.setOutData("result", result);
			act.setOutData("size", title_list.size());
			act.setForword(ORG_STORAGE_DETAIL_INFO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "各大区库存信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 导出
	 */
	public void toExcel(){
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Map<String, Object> param = new HashMap<String, Object>();
		    String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
		    param.put("areaId", areaId);
		    //得到所有车型
		    List<Map<String, Object>> modelList = dao.getModelList(param);
		    //得到所有车系
		    List<Map<String, Object>> seriesList = dao.getSeriesList(param);
		    //得到所有产地
		    List<Map<String, Object>> vehicleKindsList = dao.getVehicleKinds(param); //商用车、乘用车
			List<Map<String, Object>> result = dao.getOrgStorageDetailReportInfo(param,vehicleKindsList,seriesList,modelList,Constant.PAGE_SIZE_MAX, curPage);
			/*********************************/
			//商用车、乘用车循环
			List<String> title_list = new ArrayList<String>();
			List<String> column_list = new ArrayList<String>();
			title_list.add("片区");
			column_list.add("片区");
			if(vehicleKindsList != null && vehicleKindsList.size()>0){
				
				for(int j = 0 ; j < vehicleKindsList.size() ; j++){
					Map<String, Object> mapp = vehicleKindsList.get(j);
					String vehicleKind = mapp.get("VEHICLE_KIND").toString();	
					boolean flag = false;
					//车系循环
					if(seriesList != null && seriesList.size()>0){
						for(int k = 0 ; k < seriesList.size() ; k++){
							Map<String, Object> map = seriesList.get(k);
							String vehicle_kind = map.get("VEHICLE_KIND").toString();
							if(vehicleKind.equals(vehicle_kind)){
								flag = true;
								String series_id = map.get("GROUP_ID").toString();
								String series_name = map.get("GROUP_NAME").toString();
								//车型循环
								if(modelList != null && modelList.size()>0){
									for(int i = 0 ; i < modelList.size() ; i++){
										Map<String, Object> map_1 = modelList.get(i);
										String seriesId = map_1.get("SERIES_ID").toString();
										//循环当前车系下的车型
										if(series_id.equals(seriesId)){
											String MODEL_NAME = (String)map_1.get("MODEL_NAME");
											String MODEL_ID = map_1.get("MODEL_ID").toString();
											title_list.add(MODEL_NAME);
											column_list.add("A"+MODEL_ID);
										}					
									}						
								}
								
								title_list.add(series_name+"小计");
								column_list.add(series_name+"小计");
							}				
						}
					}
					if(flag){
						title_list.add(vehicleKind+"合计");
						column_list.add(vehicleKind+"合计");
					}
				}
					title_list.add("总计");
					column_list.add("总计");

			}
			/*********************************/
//			act.setOutData("title_list", title_list);
//			act.setOutData("result", result);
//			act.setOutData("size", title_list.size());
//			act.setForword(ORG_STORAGE_DETAIL_INFO);
			toReportExcel(act.getResponse(), act.getRequest(), "各大区库存信息.xls",title_list,result,column_list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "各大区库存信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public Object toReportExcel(ResponseWrapper response,RequestWrapper request,String excelName,List<String> title_list,
							List<Map<String, Object>> result,List<String> column_list) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName,0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			//居中
			WritableFont font_1 = new WritableFont(WritableFont.ARIAL,12,WritableFont.BOLD);
			WritableCellFormat wcf_1 = new WritableCellFormat(font_1);
			wcf_1.setAlignment(Alignment.CENTRE);
			//居中
			WritableFont font_2 = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);
			WritableCellFormat wcf_2 = new WritableCellFormat(font_2);
			wcf_2.setAlignment(Alignment.CENTRE);
			sheet.mergeCells(0, 0, title_list.size()-1, 0);
			sheet.addCell(new Label(0,0,"各大区库存统计",wcf_1));
			for(int i  = 0 ; i < title_list.size() ; i++){
				sheet.addCell(new Label(i,1,title_list.get(i),wcf_2));
			}
			int count = 2;
			for(int i = 0 ; i < result.size() ; i++){
				Map<String, Object> map = result.get(i);
				for(int j = 0 ; j < column_list.size() ; j++){
					String title = column_list.get(j);
					if(j==0){
						String orgName = map.get("ORG_NAME")==null?"":map.get("ORG_NAME").toString();
						sheet.addCell(new Label(0,count,orgName,wcf));
					}else{
						String num = map.get(title)==null?"":map.get(title).toString();
						sheet.addCell(new Label(j,count,num,wcf));
					}
				}
				count++;
			}
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
}

