package com.infodms.dms.actions.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.report.reportmng.DynamicReportMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.report.serviceReport.OldPartReportDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class OldPartReport {

	private Logger logger = Logger.getLogger(DynamicReportMng.class) ;
	private ActionContext act =ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	OldPartReportDao dao = OldPartReportDao.getInstance();
	
	private final String MAIN_URL = "/jsp/report/service/oldPartDownPer.jsp" ;//主查询页面
	private final String MAIN_URL2 = "/jsp/report/service/twiceClaimSuccPer.jsp" ;//主查询页面
	private final String MAIN_URL3 = "/jsp/report/service/oldPartInstorePer.jsp" ;//主查询页面
	/**
	 * 旧件打折报表
	 */
	public void oldPartDownPer(){
		try {
			act.setForword(MAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	public void oldPartDownQuery(){
		try {
			ClaimReportDao dao1  = ClaimReportDao.getInstance();
			
			String type = request.getParamValue("type");
			String supplyCode = request.getParamValue("supplyCode");
			String supplyName = request.getParamValue("supplyName");
			String partCode = request.getParamValue("partCode");
			String partName = request.getParamValue("partName");
			String modelCode = request.getParamValue("modelCode");
			String yieldly = request.getParamValue("YIELDLY");
			Map<String, String> map1 =new HashMap();
			map1.put("supplyCode", supplyCode);
			map1.put("supplyName", supplyName);
			map1.put("partCode", partCode);
			map1.put("partName", partName);
			map1.put("modelCode", modelCode);
			map1.put("yieldly", yieldly);
			map1.put("type", type);
			String name = "旧件索赔打折报表.xls";
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			if("0".equalsIgnoreCase(type)){
				PageResult<Map<String,Object>> pr = dao.getOldPartDownList( map1,Constant.PAGE_SIZE,curPage);
				act.setOutData("ps", pr);
			}else {
				String[] head=new String[10];
				head[0]="供应商代码";
				head[1]="供应商名称";
				head[2]="索赔单号";
				head[3]="车型";
				head[4]="配件代码";
				head[5]="配件名称";
				head[6]="出库数量";
				head[7]="折扣数量";
				head[8]="折后数量";
				head[9]="含税折扣金额";
				PageResult<Map<String,Object>> pr = dao.getOldPartDownList( map1,9999999,curPage);
				List<Map<String, Object>> list2 = pr.getRecords();
				
				String setList = "0,1,2,4,5,9";
				 List list1=new ArrayList();
				    if(list2!=null&&list2.size()!=0){
						for(int i=0;i<list2.size();i++){
					    	Map map =(Map)list2.get(i);
							String[]detail=new String[10];
							
							detail[0]=(String) map.get("NOTICE_CODE");
							detail[1]=(String) map.get("MAKER_NAME");
							detail[2]=(String)(map.get("CLAIM_NO"));
							detail[3]=(String) map.get("MODEL_NAME");
							detail[4]=(String) map.get("PART_CODE");
							detail[5]=(String) map.get("PART_NAME");
							detail[6]=String.valueOf( map.get("OUT_NUM"));
							detail[7]=String.valueOf( map.get("ZK_NUM"));
							detail[8]=String.valueOf(  map.get("ZH_NUM"));
							detail[9]=String.valueOf(  map.get("ZK_AMOUNT"));
							
							list1.add(detail);
					      }
					    }
				    dao1.toExceVender(ActionContext.getContext().getResponse(), request, head, list1,name,"旧件索赔打折报表",setList);
				    act.setForword(MAIN_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 二次索赔成功率报表
	 */
	public void twiceClaimSuccPer(){
		try {
			act.setForword(MAIN_URL2);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	public void twiceClaimSuccQuery(){
		try {
			ClaimReportDao dao1  = ClaimReportDao.getInstance();
			
			String type = request.getParamValue("type");
			String supplyCode = request.getParamValue("supplyCode");
			String supplyName = request.getParamValue("supplyName");
			String bgDate = request.getParamValue("bgDate");
			String egDate = request.getParamValue("egDate");
			String modelCode = request.getParamValue("modelCode");
			String yieldly = request.getParamValue("YIELDLY");
			Map<String, String> map1 =new HashMap();
			map1.put("supplyCode", supplyCode);
			map1.put("supplyName", supplyName);
			map1.put("bgDate", bgDate);
			map1.put("egDate", egDate);
			map1.put("modelCode", modelCode);
			map1.put("yieldly", yieldly);
			map1.put("type", type);
			String name = "二次索赔成功率报表.xls";
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			if("0".equalsIgnoreCase(type)){
				PageResult<Map<String,Object>> pr = dao.getTwiceClaimSuccList( map1,Constant.PAGE_SIZE,curPage);
				act.setOutData("ps", pr);
			}else {
				String[] head=new String[6];
				head[0]="供应商代码";
				head[1]="供应商名称";
				head[2]="入库数量";
				head[3]="出库数量";
				head[4]="含税索赔金额";
				head[5]="二次索赔成功率";
			
				PageResult<Map<String,Object>> pr = dao.getTwiceClaimSuccList( map1,9999999,curPage);
				List<Map<String, Object>> list2 = pr.getRecords();
				
				String setList = "0,1,4,5";
				 List list1=new ArrayList();
				    if(list2!=null&&list2.size()!=0){
						for(int i=0;i<list2.size();i++){
					    	Map map =(Map)list2.get(i);
							String[]detail=new String[6];
							detail[0]=(String) map.get("PRODUCER_CODE");
							detail[1]=(String) map.get("MAKER_NAME");
							detail[2]=String.valueOf( map.get("IN_NUM"));
							detail[3]=String.valueOf( map.get("OUT_NUM"));
							detail[4]=String.valueOf(  map.get("OUT_AMOUNT"));
							detail[5]=String.valueOf(  map.get("SUC_PERCENT"));
							list1.add(detail);
					      }
					    }
				    dao1.toExceVender(ActionContext.getContext().getResponse(), request, head, list1,name,"二次索赔成功率",setList);
				    act.setForword(MAIN_URL2);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 旧件库存报表
	 */
	public void oldPartInstorePer(){
		try {
			List<Map<String, Object>> seriesList = dao.getSeriesList();
			String str = dao.getStr(seriesList);
			act.setOutData("seriesList", str);
			act.setForword(MAIN_URL3);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	public void oldPartInstoreQuery(){
		try {
			ClaimReportDao dao1  = ClaimReportDao.getInstance();
			String type = request.getParamValue("type");
			String supplyCode = request.getParamValue("supplyCode");
			String supplyName = request.getParamValue("supplyName");
			String partCode = request.getParamValue("partCode");
			String partName = request.getParamValue("partName");
			String series_id = request.getParamValue("series_id");
			String yieldly = request.getParamValue("YIELDLY");
			Map<String, String> map1 =new HashMap();
			map1.put("supplyCode", supplyCode);
			map1.put("partCode", partCode);
			map1.put("partName", partName);
			map1.put("supplyName", supplyName);
			map1.put("series_id", series_id);
			map1.put("yieldly", yieldly);
			map1.put("type", type);
			String name = "三包旧件库存报表.xls";
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			if("0".equalsIgnoreCase(type)){
				PageResult<Map<String,Object>> pr = dao.getOldPartInstoreList( map1,Constant.PAGE_SIZE,curPage);
				act.setOutData("ps", pr);
			}else {
				String[] head=new String[11];
				head[0]="车系";
				head[1]="供应商代码";
				head[2]="供应商名称";
				head[3]="配件代码";
				head[4]="配件名称";
				head[5]="配件件号";
				head[6]="单位";
				head[7]="截至前月月底结存数量";
				head[8]="本月入库数量";
				head[9]="本月出库数量";
				head[10]="本月结存数量";
			
				PageResult<Map<String,Object>> pr = dao.getOldPartInstoreList( map1,9999999,curPage);
				List<Map<String, Object>> list2 = pr.getRecords();
				
				String setList = "1,2,3,4,5,7,8,9,10";
				 List list1=new ArrayList();
				    if(list2!=null&&list2.size()!=0){
						for(int i=0;i<list2.size();i++){
					    	Map map =(Map)list2.get(i);
							String[]detail=new String[11];
							detail[0]=(String) map.get("SERIES_NAME");
							detail[1]=(String) map.get("PRODUCER_CODE");
							detail[2]=(String) map.get("PRODUCER_NAME");
							detail[3]=(String) map.get("PART_CODE");
							detail[4]=(String) map.get("PART_NAME");
							detail[5]=(String) map.get("PART_NO");
							detail[6]=(String) map.get("UNIT");
							detail[7]=String.valueOf( map.get("H"));
							detail[8]=String.valueOf( map.get("I"));
							detail[9]=String.valueOf( map.get("O"));
							detail[10]=String.valueOf(  map.get("N"));
							list1.add(detail);
					      }
					    }
				    dao1.toExceVender(ActionContext.getContext().getResponse(), request, head, list1,name,"三包旧件库存",setList);
				    act.setForword(MAIN_URL3);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "动态报表主页面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
