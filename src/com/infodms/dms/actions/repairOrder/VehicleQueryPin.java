package com.infodms.dms.actions.repairOrder;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.repair.VehicleQueryPinDao;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.dao.report.serviceReport.TechnologyUpgradeDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrFineTemporaryPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
* @ClassName: VehicleQueryPin 
* @Description: TODO(车辆PIN码导入查询) 
* @author wenyudan 
* @date 2013-11-11 下午3:54:01 
*  
*/ 

public class VehicleQueryPin extends BaseImport{
	private Logger logger = Logger.getLogger(VehicleQueryPin.class);

	private final VehicleQueryPinDao cdao = VehicleQueryPinDao.getInstance();

	private final String VEHICLE_QUERY_PIN = "/jsp/repairorder/VehicleQueryPin.jsp";
	private final String VEHICLE_QUERY_PIN_IMP = "/jsp/repairorder/VehicleQueryPinImport.jsp";
	private final String IMPORT_OEM_FAILURE = "/jsp/repairorder/importVehicleQueryPinFailure.jsp"; //导入失败
	private final String IMPORT_OEM_FAILURE_del = "/jsp/repairorder/importVehicleQueryPinFailuredel.jsp"; //导入失败明细
	private final String IMPORT_OEM_Success = "/jsp/repairorder/importVehicleQueryPinsuccess.jsp"; //导入成功
	List<Map<String, String>> listm =null;
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	/**
	* 初始化页面
	*/
	public void VehicleQueryPinInit(){

		try {
/*			List<Map<String, Object>> list = dao.getBigOrgList();
			List<Map<String, Object>> serisList = ajaxDao.getSerisList(logonUser.getPoseId().toString(), 2);
			act.setOutData("serislist", serisList);
			act.setOutData("list", list);*/
			act.setForword(VEHICLE_QUERY_PIN);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆PIN码导入加载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	* 查询
	*/
	public void queryVehicleQueryPin(){

	   	try {

			String serisCode = CommonUtils.checkNull(request.getParamValue("serisCode"));//车型ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型code
			String PIN = CommonUtils.checkNull(request.getParamValue("PIN"));//PIN
			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//
			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//
			String Importpeople = CommonUtils.checkNull(request.getParamValue("Importpeople"));
			String engineNo = CommonUtils.checkNull(request.getParamValue("engine_no"));
			String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//
			String egDate = CommonUtils.checkNull(request.getParamValue("egDate"));//
			String SelectPin = CommonUtils.checkNull(request.getParamValue("SelectPin"));

			Map<String, String> map = new HashMap<String, String>();
			map.put("serisCode", serisCode);
			map.put("groupCode", groupCode);
			map.put("PIN", PIN);
			map.put("bDate", bDate);
			map.put("eDate", eDate);
			map.put("bgDate", bgDate);
			map.put("egDate", egDate);
			map.put("Importpeople", Importpeople);
			map.put("engineNo", engineNo);
			map.put("SelectPin", SelectPin);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=cdao.QueryActivity(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);

	   		
	   	} catch (Exception e) {
	   		BizException e1 = new BizException(act, e,
	   				ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆PIN码导入查询");
	   		logger.error(logonUser, e1);
	   		act.setException(e1);
	   	}
	   	
	   

}
	
 	/**
 	 * 导出页面
 	 */
 	public void VehicleQueryPinExport(){
 		OutputStream os = null;

 		try {
 			Map<String, String> map = new HashMap<String, String>();

			String serisCode = CommonUtils.checkNull(request.getParamValue("serisCode"));//车型ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));//车型code
			String PIN = CommonUtils.checkNull(request.getParamValue("PIN"));//PIN
			String bDate = CommonUtils.checkNull(request.getParamValue("bDate"));//
			String eDate = CommonUtils.checkNull(request.getParamValue("eDate"));//
			String Importpeople = CommonUtils.checkNull(request.getParamValue("Importpeople"));
			String engineNo = CommonUtils.checkNull(request.getParamValue("engine_no"));
			String bgDate = CommonUtils.checkNull(request.getParamValue("bgDate"));//
			String egDate = CommonUtils.checkNull(request.getParamValue("egDate"));//
			String SelectPin = CommonUtils.checkNull(request.getParamValue("SelectPin"));

			map.put("serisCode", serisCode);
			map.put("groupCode", groupCode);
			map.put("PIN", PIN);
			map.put("bDate", bDate);
			map.put("eDate", eDate);
			map.put("bgDate", bgDate);
			map.put("egDate", egDate);
			map.put("Importpeople", Importpeople);
			map.put("engineNo", engineNo);
			map.put("SelectPin", SelectPin);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=cdao.QueryActivity(map, 999999, curPage);
 			ResponseWrapper response = act.getResponse();
 			// 导出的文件名
 			String fileName = "车辆PIN码导出查询表.xls";
 			// 导出的文字编码客户关怀活动数据明细表
 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
 			response.setContentType("Application/text/csv");
 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
 			// 定义一个集合
 			List<List<Object>> list = new LinkedList<List<Object>>();
 			// 标题
 			List<Object> listTemp = new LinkedList<Object>();
 			listTemp.add("车系");
 			listTemp.add("车型");
 			listTemp.add("VIN");
 			listTemp.add("发动机号");
 			listTemp.add("生产时间");
 			listTemp.add("销售时间");
 			listTemp.add("PIN码");
 			listTemp.add("导入人");
 			listTemp.add("导入时间");


 			list.add(listTemp);
 			List<Map<String, Object>> rslist = ps.getRecords();
 			Map<String, Object> map1 = new HashMap<String, Object>();

 			if(rslist!=null){

 			for (int i = 0; i < rslist.size(); i++) {
 				map1 = rslist.get(i);
 				List<Object> listValue = new LinkedList<Object>();

 				listValue.add(map1.get("SERIES_NAME") != null ? map1.get("SERIES_NAME") : "");
 				listValue.add(map1.get("MODEL_NAME") != null ? map1.get("MODEL_NAME") : "");
 				listValue.add(map1.get("VIN") != null ? map1.get("VIN") : "");
 				listValue.add(map1.get("ENGINE_NO") != null ? map1.get("ENGINE_NO") : "");
 				listValue.add(map1.get("PRODUCT_DATE") != null ? map1.get("PRODUCT_DATE") : "");
 				listValue.add(map1.get("PURCHASED_DATE") != null ? map1.get("PURCHASED_DATE") : "");
 				listValue.add(map1.get("PIN") != null ? map1.get("PIN") : "");
 				listValue.add(map1.get("IMPORT_PEOPLE") != null ? map1.get("IMPORT_PEOPLE") : "");
 				listValue.add(map1.get("IMPORT_DATE") != null ? map1.get("IMPORT_DATE") : "");

 				list.add(listValue);
 			}
 			}
 			os = response.getOutputStream();
 			CsvWriterUtil.createXlsFileToName(list, os,"车辆PIN码导出查询表");
 			os.flush();	

 		} catch (Exception e) {
 			BizException e1 = new BizException(act, e,
 					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出");
 			logger.error(logonUser, e1);
 			act.setException(e1);
 		}

 	  }
 	
 
 	
	/**
	* 加载导入页面
	*/
	public void VehicleQueryPinimport(){

		try {

			act.setForword(VEHICLE_QUERY_PIN_IMP);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆PIN码导入加载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	 /**
	 * 模板下载
	 */
	    public void downloadVehicleQueryPin(){
		  
	    	ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			OutputStream os = null;
			try{
				ResponseWrapper response = act.getResponse();

				
				// 用于下载传参的集合
				List<List<Object>> list = new LinkedList<List<Object>>();
				
				//标题
				List<Object> listHead = new LinkedList<Object>();
				
				listHead.add("VIN");
				listHead.add("PIN");

				
				list.add(listHead);

				// 导出的文件名
				String fileName = "车辆PIN码导入模板.xls";
				// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				response.setContentType("Application/text/xls");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				
				os = response.getOutputStream();
	 			CsvWriterUtil.createXlsFileToName(list, os,"车辆PIN码导入模板");
				os.flush();			
	    		} catch (Exception e) {
	    			BizException e1 = new BizException(act, e,
	    					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆PIN码导入模板下载");
	    			logger.error(logonUser, e1);
	    			act.setException(e1);
	    		}	
	    	
	   }
	    
	    /**
	     * 导入临时表并进行规则校验
	     */
	    public void exceloadVehicleQueryPinByDlr(){
	    	ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try{
			RequestWrapper request = act.getRequest();

			 long maxSize=1024*1024*5;
			 int a=insertIntoTmp(request, "uploadFile",2,3,maxSize);
				List<ExcelErrors> el=getErrList();
				if(null!=el&&el.size()>0){
					act.setOutData("errorList", el);
					act.setForword(IMPORT_OEM_FAILURE);
				}else{
					List<Map> list=getMapList();
					
					//将数据插入临时表
					Map<String,Object> map1 =insertVehicleQueryPin(list);
					   if(map1!=null&&map1.size()>0){
						   act.setOutData("errorInfo", map1);
						   act.setForword(IMPORT_OEM_FAILURE_del);;
					   }else{
					
						//	List<ExcelErrors> errorList=checkData();//校验数据
						   
						   //查询临时表的数据
						   List<Map<String, String>>  list2 =listm;
						   act.setOutData("list", list2);
						   act.setForword(IMPORT_OEM_Success);
						   
				          }
					   }
				}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"车辆PIN码导入读取错误");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	    	}
	    
	    /*
		 * 
		 */
		public Map<String,Object> insertVehicleQueryPin(List<Map> list) throws Exception{
			Map<String,Object> map1 =new HashMap<String, Object>();
			listm=new ArrayList<Map<String,String>>();; 

			if(null==list){
				list=new ArrayList();
			}
			String  aa="";
			for(int i=0;i<list.size();i++){
				Map map=list.get(i);
				if(null==map){
					map=new HashMap<String, Cell[]>();
				}
				Set<String> keys=map.keySet();
				Iterator it=keys.iterator();
				String key="";
				while(it.hasNext()){
					key=(String)it.next();
					Cell[] cells=(Cell[])map.get(key);
				    aa=parseCells(key, cells);
			
				    if(aa!=null){
				    	map1.put("num",key);
				    	map1.put("err", aa);
						return map1;

				    }
				
				}
			
			}
			return null;
			
		}
		/*
		 * 每一行插入（昌河汽车）
		 * 数字只截取30位
		 * modify by wenyd at 2013-07-30
		 * 
		 */
		public String parseCells(String rowNum,Cell[] cells) throws Exception{
	    	ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
				TmVehiclePO  po  =new TmVehiclePO();
				po.setVin(cells[0].getContents().trim());
				Map<String,String> map =new HashMap<String, String>();
				List<TmVehiclePO> list=cdao.select(po);
					 if(list!=null&&list.size()>0){
						 map.put("VIN", list.get(0).getVin());
						}else{
							return "Vin在车辆信息中不存在！";
						}
		      map.put("PIN", cells[1].getContents().trim());		 
		      listm.add(map);		 
			return null;
		}
		
		//插入正式库
	    public void importExcel(){
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try {
				String [] PIN = request.getParamValues("PIN");//PIN
				String [] VIN = request.getParamValues("VIN");//VIN
				if(VIN!=null&&VIN.length>0)
				for(int i=0;i<VIN.length;i++){
					TmVehiclePO  po  =new TmVehiclePO();
	                po.setVin(VIN[i]);
					TmVehiclePO  po1  =new TmVehiclePO();
					po1.setPin(PIN[i]);
					po1.setImportDate(new Date());
					po1.setImportPeople(logonUser.getUserId().toString());
	                cdao.update(po,po1);
				}

				act.setForword(VEHICLE_QUERY_PIN);
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆PIN码导入正式表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	    	
	    	
	    }
	
	}


 
