package com.infodms.dms.actions.report.reportOne;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

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
import com.infodms.dms.dao.report.StorageAgeQueryDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 库龄报表
 * @author Administrator
 *
 */
public class StorageAgeReport {
	private Logger logger = Logger.getLogger(StorageAgeReport.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private StorageAgeQueryDao dao = StorageAgeQueryDao.getInstance();
	
	private final String STORAGE_AGE_REPORT_INIT = "/jsp/report/sales/storgeAgeQuery.jsp";
	private final String STORAGE_AGE_REPORT_INFO = "/jsp/report/sales/storageAgeInfo.jsp";
	/*
	 * 库龄报表查询初始化
	 */
	public void storageAgeReportQueryInit(){
		try {
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(STORAGE_AGE_REPORT_INIT);
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
	public void getStorageAgeReportInfo(){
		try {
			String areaId = request.getParamValue("areaId")==null?"":request.getParamValue("areaId");
			String dealerId = request.getParamValue("outDealerId")==null?"":request.getParamValue("outDealerId");
			String haveCon = request.getParamValue("haveCon")==null?"":request.getParamValue("haveCon");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("areaId", areaId);
			param.put("dealerId", dealerId);
			param.put("haveCon", haveCon);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			List<Map<String, Object>> result = dao.getStorageAgeReportInfo(param,Constant.PAGE_SIZE_MAX, curPage);
			act.setOutData("result", result);
			HashMap<String,Object> paramMap=new  HashMap<String,Object>();
			paramMap.put("areaId", areaId);
			paramMap.put("dealerId", dealerId);
			paramMap.put("haveCon", haveCon);
			act.setOutData("result", result);
			act.setOutData("paramMap", paramMap);
			act.setForword(STORAGE_AGE_REPORT_INFO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库龄信息");
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
//			String chooseDate = request.getParamValue("chooseDate");
//			String areaId = request.getParamValue("areaId");
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("chooseDate", chooseDate);
//			map.put("areaId", areaId);
//			PageResult<Map<String, Object>> ps = dao.getProductSaleReportInfo(map,Constant.PAGE_SIZE_MAX, curPage);
//			List<Map<String, Object>> result = ps.getRecords();
//			String[] excelHead = {"车辆型号","当日入库","本月入库","本年入库","当日开票","本月开票","本年开票","可发车","当日最终销售","本月最终销售","本年最终销售","中转库","外借","库存合计"};
//			String[] columns = {"GROUP_NAME","D_AMOUNT","M_AMOUNT","Y_MOUNT","DF_AMOUNT","MF_AMOUNT","YF_AMOUNT","SV_AMOUNT","DS_AMOUNT","MS_AMOUNT","YS_AMOUNT","STORAGE_AMOUNT","B_AMOUNT","STORAGE_AMOUNT"};
//			ToExcel.toReportExcel(act.getResponse(), request, "生产销售快报.xls",excelHead,columns,result);
					toReportExcel(act.getResponse(), request, "库龄报表.xls");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @param response
	 * @param request
	 * @param excelName 导出的报表EXCEL名字
	 * @param firstName EXCEL内容第一行名称
	 * @param excelHead EXCEL头部名称数组
	 * @param columns 需要从结果集中取值的字段名
	 * @param result  查询到的结果集
	 * @return
	 * @throws Exception
	 */
	public Object toReportExcel(ResponseWrapper response,RequestWrapper request,String excelName) throws Exception{
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
			sheet.mergeCells(0,0,10,0);
			sheet.addCell(new Label(0, 0, "昌河汽车产品库龄统计表", wcf_1));
			sheet.mergeCells(0, 1, 0, 2);
			sheet.mergeCells(1, 1, 1, 2);
			sheet.mergeCells(2, 1, 5, 1);
			sheet.mergeCells(6, 1, 9, 1);
			sheet.mergeCells(10,1, 10, 2);
			sheet.addCell(new Label(0,1,"类别",wcf));
			sheet.addCell(new Label(1,1,"车种",wcf));
			sheet.addCell(new Label(2,1,"经销商库存",wcf));
			sheet.addCell(new Label(6,1,"企业库存",wcf));
			sheet.addCell(new Label(2,2,"3个月以内库存",wcf));
			sheet.addCell(new Label(3,2,"3-6个月库存",wcf));
			sheet.addCell(new Label(4,2,"6个月以上库存",wcf));
			sheet.addCell(new Label(5,2,"合计",wcf));
			sheet.addCell(new Label(6,2,"3个月以内库存",wcf));
			sheet.addCell(new Label(7,2,"3-6个月库存",wcf));
			sheet.addCell(new Label(8,2,"6个月以上库存",wcf));
			sheet.addCell(new Label(9,2,"合计",wcf));
			sheet.addCell(new Label(10,1,"库存合计",wcf));
			sheet.setColumnView(2,15);
			sheet.setColumnView(3,15);
			sheet.setColumnView(4,15);
			sheet.setColumnView(6,15);
			sheet.setColumnView(7,15);
			sheet.setColumnView(8,15);
			sheet.setColumnView(0, 20);
			/***************************************************************************/
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			String areaId = request.getParamValue("areaId")==null?"":request.getParamValue("areaId");
			String dealerId = request.getParamValue("outDealerId")==null?"":request.getParamValue("outDealerId");
			String haveCon = request.getParamValue("haveCon")==null?"":request.getParamValue("haveCon");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("areaId", areaId);
			param.put("dealerId", dealerId);
			param.put("haveCon", haveCon);
			List<Map<String, Object>> result = dao.getStorageAgeReportInfo(param,Constant.PAGE_SIZE_MAX, curPage);		
			if(result != null && result.size()>0){
				int count = 3;
				for(int i = 0 ; i < result.size() ; i++){
					Map<String, Object> map = result.get(i);
					String yieldly = map.get("YIELDLY")==null?"":map.get("YIELDLY").toString();
					String groupName = map.get("GROUP_NAME")==null?"":map.get("GROUP_NAME").toString();
					if(!"".equals(yieldly) && !"".equals(groupName)){
						sheet.addCell(new Label(0,count,map.get("YIELDLY").toString(),wcf));
						sheet.addCell(new Label(1,count,map.get("GROUP_NAME").toString(),wcf));
						sheet.addCell(new Label(2,count,map.get("STORAGE_1").toString(),wcf));
						sheet.addCell(new Label(3,count,map.get("STORAGE_2").toString(),wcf));
						sheet.addCell(new Label(4,count,map.get("STORAGE_3").toString(),wcf));
						sheet.addCell(new Label(5,count,map.get("TOTAL_STORAGE").toString(),wcf));
						sheet.addCell(new Label(6,count,map.get("OEM_STORAGE_1").toString(),wcf));
						sheet.addCell(new Label(7,count,map.get("OEM_STORAGE_2").toString(),wcf));
						sheet.addCell(new Label(8,count,map.get("OEM_STORAGE_3").toString(),wcf));
						sheet.addCell(new Label(9,count,map.get("TOTAL_OEM_STORAGE").toString(),wcf));
						sheet.addCell(new Label(10,count,map.get("TOTAL").toString(),wcf));
						count++;
					}
					if(!"".equals(yieldly) && "".equals(groupName)){
						sheet.addCell(new Label(0,count,map.get("YIELDLY").toString()+"合计",wcf));
						sheet.addCell(new Label(1,count,"",wcf));
						sheet.addCell(new Label(2,count,map.get("STORAGE_1").toString(),wcf));
						sheet.addCell(new Label(3,count,map.get("STORAGE_2").toString(),wcf));
						sheet.addCell(new Label(4,count,map.get("STORAGE_3").toString(),wcf));
						sheet.addCell(new Label(5,count,map.get("TOTAL_STORAGE").toString(),wcf));
						sheet.addCell(new Label(6,count,map.get("OEM_STORAGE_1").toString(),wcf));
						sheet.addCell(new Label(7,count,map.get("OEM_STORAGE_2").toString(),wcf));
						sheet.addCell(new Label(8,count,map.get("OEM_STORAGE_3").toString(),wcf));
						sheet.addCell(new Label(9,count,map.get("TOTAL_OEM_STORAGE").toString(),wcf));
						sheet.addCell(new Label(10,count,map.get("TOTAL").toString(),wcf));
						count++;
					}
					if("".equals(yieldly) && "".equals(groupName)){
						sheet.addCell(new Label(0,count,"合计",wcf));
						sheet.addCell(new Label(1,count,"",wcf));
						sheet.addCell(new Label(2,count,map.get("STORAGE_1").toString(),wcf));
						sheet.addCell(new Label(3,count,map.get("STORAGE_2").toString(),wcf));
						sheet.addCell(new Label(4,count,map.get("STORAGE_3").toString(),wcf));
						sheet.addCell(new Label(5,count,map.get("TOTAL_STORAGE").toString(),wcf));
						sheet.addCell(new Label(6,count,map.get("OEM_STORAGE_1").toString(),wcf));
						sheet.addCell(new Label(7,count,map.get("OEM_STORAGE_2").toString(),wcf));
						sheet.addCell(new Label(8,count,map.get("OEM_STORAGE_3").toString(),wcf));
						sheet.addCell(new Label(9,count,map.get("TOTAL_OEM_STORAGE").toString(),wcf));
						sheet.addCell(new Label(10,count,map.get("TOTAL").toString(),wcf));
						count++;
					}
				}
//				List<Map<String, Object>> list_1 = new ArrayList<Map<String,Object>>(); //商用车
//				List<Map<String, Object>> list_2 = new ArrayList<Map<String,Object>>(); //乘用车
//				List<Map<String, Object>> list_3 = new ArrayList<Map<String,Object>>(); //商用车合计
//				List<Map<String, Object>> list_4 = new ArrayList<Map<String,Object>>(); //乘用车合计 
//				Map<String, Object> sum = new HashMap<String, Object>();
//				for(int i = 0 ; i < result.size() ; i++){
//					Map<String, Object> map = result.get(i);
//					String areaName = map.get("AREA_NAME")==null?"":(String)map.get("AREA_NAME");
//					String groupName = map.get("GROUP_NAME")==null?"":(String)map.get("GROUP_NAME");
//					String codeName = TcCodeDao.getInstance().getCodeDescByCodeId(Constant.YIELDLY_03.toString());
//					if(!"".equals(areaName) && !"".equals(groupName)){ //得到明细数据(不包括合计)
//						if(areaName.contains(codeName)){ //商用车(产地：合肥)
//							list_1.add(map);
//						}else{
//							list_2.add(map); //乘用车
//						}
//					}
//					if(!"".equals(areaName) && "".equals(groupName)){ //得到按产地合计的数据
//						if(areaName.contains(codeName)){
//							list_3.add(map);
//						}else{
//							list_4.add(map);
//						}
//					}
//					if("".equals(areaName) && "".equals(groupName)){
//						sum = map;
//					}
//				}
//			/***************************乘用车start************************************/
//				int count_1 = 3;
//				if(list_2 != null && list_2.size()>0){
//					for(int i = 0 ; i < list_2.size() ; i++){
//						Map<String, Object> temp = list_2.get(i);
//						sheet.addCell(new Label(0,count_1,"乘用车",wcf));
//						sheet.addCell(new Label(1,count_1,temp.get("GROUP_NAME").toString(),wcf));
//						sheet.addCell(new Label(2,count_1,temp.get("D_AMOUNT_1").toString(),wcf));
//						sheet.addCell(new Label(3,count_1,temp.get("D_AMOUNT_2").toString(),wcf));
//						sheet.addCell(new Label(4,count_1,temp.get("D_AMOUNT_3").toString(),wcf));
//						sheet.addCell(new Label(5,count_1,temp.get("D_TOTAL_AMOUNT").toString(),wcf));
//						sheet.addCell(new Label(6,count_1,temp.get("O_AMOUNT_1").toString(),wcf));
//						sheet.addCell(new Label(7,count_1,temp.get("O_AMOUNT_2").toString(),wcf));
//						sheet.addCell(new Label(8,count_1,temp.get("O_AMOUNT_3").toString(),wcf));
//						sheet.addCell(new Label(9,count_1,temp.get("O_TOTAL_AMOUNT").toString(),wcf));
//						sheet.addCell(new Label(10,count_1,temp.get("TOTAL_AMOUNT").toString(),wcf));
//						count_1++;
//					}
//						Integer d_amount_1 = 0;
//						Integer d_amount_2 = 0;
//						Integer d_amount_3 = 0;
//						Integer d_total_amount = 0;
//						Integer o_amount_1 = 0;
//						Integer o_amount_2 = 0;
//						Integer o_amount_3 = 0;
//						Integer o_total_amount = 0;
//						Integer total_amount = 0;
//						for(int i = 0 ; i < list_4.size() ; i++){
//							Map<String, Object> map = list_4.get(i);
//							d_amount_1 += ((BigDecimal)map.get("D_AMOUNT_1")).intValue();
//							d_amount_2 += ((BigDecimal)map.get("D_AMOUNT_2")).intValue();
//							d_amount_3 += ((BigDecimal)map.get("D_AMOUNT_3")).intValue();
//							d_total_amount += ((BigDecimal)map.get("D_TOTAL_AMOUNT")).intValue();
//							o_amount_1 += ((BigDecimal)map.get("O_AMOUNT_1")).intValue();
//							o_amount_2 += ((BigDecimal)map.get("O_AMOUNT_2")).intValue();
//							o_amount_3 += ((BigDecimal)map.get("O_AMOUNT_3")).intValue();
//							o_total_amount += ((BigDecimal)map.get("O_TOTAL_AMOUNT")).intValue();
//							total_amount += ((BigDecimal)map.get("TOTAL_AMOUNT")).intValue();
//							
//						}
//						sheet.addCell(new Label(0,count_1,"合计",wcf));
//						sheet.addCell(new Label(1,count_1,"  ",wcf));
//						sheet.addCell(new Label(2,count_1,d_amount_1.toString(),wcf));
//						sheet.addCell(new Label(3,count_1,d_amount_2.toString(),wcf));
//						sheet.addCell(new Label(4,count_1,d_amount_3.toString(),wcf));
//						sheet.addCell(new Label(5,count_1,d_total_amount.toString(),wcf));
//						sheet.addCell(new Label(6,count_1,o_amount_1.toString(),wcf));
//						sheet.addCell(new Label(7,count_1,o_amount_2.toString(),wcf));
//						sheet.addCell(new Label(8,count_1,o_amount_3.toString(),wcf));
//						sheet.addCell(new Label(9,count_1,o_total_amount.toString(),wcf));
//						sheet.addCell(new Label(10,count_1,total_amount.toString(),wcf));
//						count_1++;
//						
//				}
//				/**********************************乘用车 end************************************************/
//				/**********************************商用车start************************************/
//				if(list_1 != null && list_1.size()>0){
//					for(int i = 0 ; i < list_1.size() ; i++){
//						Map<String, Object> temp = list_1.get(i);
//						sheet.addCell(new Label(0,count_1,"商用车",wcf));
//						sheet.addCell(new Label(1,count_1,temp.get("GROUP_NAME").toString(),wcf));
//						sheet.addCell(new Label(2,count_1,temp.get("D_AMOUNT_1").toString(),wcf));
//						sheet.addCell(new Label(3,count_1,temp.get("D_AMOUNT_2").toString(),wcf));
//						sheet.addCell(new Label(4,count_1,temp.get("D_AMOUNT_3").toString(),wcf));
//						sheet.addCell(new Label(5,count_1,temp.get("D_TOTAL_AMOUNT").toString(),wcf));
//						sheet.addCell(new Label(6,count_1,temp.get("O_AMOUNT_1").toString(),wcf));
//						sheet.addCell(new Label(7,count_1,temp.get("O_AMOUNT_2").toString(),wcf));
//						sheet.addCell(new Label(8,count_1,temp.get("O_AMOUNT_3").toString(),wcf));
//						sheet.addCell(new Label(9,count_1,temp.get("O_TOTAL_AMOUNT").toString(),wcf));
//						sheet.addCell(new Label(10,count_1,temp.get("TOTAL_AMOUNT").toString(),wcf));
//						count_1++;
//					}
//						Integer d_amount_1 = 0;
//						Integer d_amount_2 = 0;
//						Integer d_amount_3 = 0;
//						Integer d_total_amount = 0;
//						Integer o_amount_1 = 0;
//						Integer o_amount_2 = 0;
//						Integer o_amount_3 = 0;
//						Integer o_total_amount = 0;
//						Integer total_amount = 0;
//						for(int i = 0 ; i < list_3.size() ; i++){
//							Map<String, Object> map = list_3.get(i);
//							d_amount_1 += ((BigDecimal)map.get("D_AMOUNT_1")).intValue();
//							d_amount_2 += ((BigDecimal)map.get("D_AMOUNT_2")).intValue();
//							d_amount_3 += ((BigDecimal)map.get("D_AMOUNT_3")).intValue();
//							d_total_amount += ((BigDecimal)map.get("D_TOTAL_AMOUNT")).intValue();
//							o_amount_1 += ((BigDecimal)map.get("O_AMOUNT_1")).intValue();
//							o_amount_2 += ((BigDecimal)map.get("O_AMOUNT_2")).intValue();
//							o_amount_3 += ((BigDecimal)map.get("O_AMOUNT_3")).intValue();
//							o_total_amount += ((BigDecimal)map.get("O_TOTAL_AMOUNT")).intValue();
//							total_amount += ((BigDecimal)map.get("TOTAL_AMOUNT")).intValue();
//							
//						}
//						sheet.addCell(new Label(0,count_1,"合计",wcf));
//						sheet.addCell(new Label(1,count_1,"  ",wcf));
//						sheet.addCell(new Label(2,count_1,d_amount_1.toString(),wcf));
//						sheet.addCell(new Label(3,count_1,d_amount_2.toString(),wcf));
//						sheet.addCell(new Label(4,count_1,d_amount_3.toString(),wcf));
//						sheet.addCell(new Label(5,count_1,d_total_amount.toString(),wcf));
//						sheet.addCell(new Label(6,count_1,o_amount_1.toString(),wcf));
//						sheet.addCell(new Label(7,count_1,o_amount_2.toString(),wcf));
//						sheet.addCell(new Label(8,count_1,o_amount_3.toString(),wcf));
//						sheet.addCell(new Label(9,count_1,o_total_amount.toString(),wcf));
//						sheet.addCell(new Label(10,count_1,total_amount.toString(),wcf));
//						count_1++;
//						
//				}
//				/**********************************商用车 end************************************************/
//				if((list_1 != null && list_1.size()>0) && (list_2 != null && list_2.size()>0)){
//					sheet.addCell(new Label(0,count_1,"总计",wcf));
//					sheet.addCell(new Label(1,count_1,"   ",wcf));
//					sheet.addCell(new Label(2,count_1,sum.get("D_AMOUNT_1").toString(),wcf));
//					sheet.addCell(new Label(3,count_1,sum.get("D_AMOUNT_2").toString(),wcf));
//					sheet.addCell(new Label(4,count_1,sum.get("D_AMOUNT_3").toString(),wcf));
//					sheet.addCell(new Label(5,count_1,sum.get("D_TOTAL_AMOUNT").toString(),wcf));
//					sheet.addCell(new Label(6,count_1,sum.get("O_AMOUNT_1").toString(),wcf));
//					sheet.addCell(new Label(7,count_1,sum.get("O_AMOUNT_2").toString(),wcf));
//					sheet.addCell(new Label(8,count_1,sum.get("O_AMOUNT_3").toString(),wcf));
//					sheet.addCell(new Label(9,count_1,sum.get("O_TOTAL_AMOUNT").toString(),wcf));
//					sheet.addCell(new Label(10,count_1,sum.get("TOTAL_AMOUNT").toString(),wcf));
//				}
			wwkb.write();
			out.flush();
		}
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
	/**
	    * 详细明细查询
	    */
		public void getStorageAgeReportInfoMsg(){
			try {
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String dealerId =CommonUtils.checkNull(request.getParamValue("outDealerId")); 
				String haveCon =CommonUtils.checkNull(request.getParamValue("haveCon"));
				String type =CommonUtils.checkNull(request.getParamValue("type"));
				String serName =CommonUtils.checkNull(request.getParamValue("serName"));
				String kcType =CommonUtils.checkNull(request.getParamValue("kcType"));
				String daylen =CommonUtils.checkNull(request.getParamValue("daylen"));
				String vin =CommonUtils.checkNull(request.getParamValue("VIN"));

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("areaId", areaId);
				param.put("dealerId", dealerId);
				param.put("haveCon", haveCon);
				param.put("type", type);
				param.put("serName", serName);
				param.put("kcType", kcType);
				param.put("daylen", daylen);
				param.put("vin", vin);
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.getStorageAgeReportInfoMsg(param, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "库龄信息");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
}

