package com.infodms.dms.actions.afterSales.serviceActivity;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.actions.util.PoiUtil;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.afterSales.ServiceActivityDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsWrActivityDealerPO;
import com.infodms.dms.po.TtAsWrActivityHoursPO;
import com.infodms.dms.po.TtAsWrActivityModelPO;
import com.infodms.dms.po.TtAsWrActivityPO;
import com.infodms.dms.po.TtAsWrActivityPartPO;
import com.infodms.dms.po.TtAsWrActivityVinPO;
import com.infodms.dms.po.TtAsWrActivityVinTempPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.utils.BaseUtils;
import com.infoservice.filestore.FileStore;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.util.URLEncoder;

public class ServiceActivityAction {
	private Logger logger = Logger.getLogger(ServiceActivityAction.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private static POFactory fac=POFactoryBuilder.getInstance();
	//服务活动页面初始化
	private static final String SREVICEACTIVITY_URL = "/jsp/afterSales/serviceActivity/serviceActivityPageInit.jsp" ;//服务活动页面初始化
	//服务活动新增-技术提升
	private static final String TECHNIQUE_URL = "/jsp/afterSales/serviceActivity/serviceActivityTechnique.jsp";
	//服务活动新增-送保养
	private static final String MAINTAIN_URL = "/jsp/afterSales/serviceActivity/serviceActivityMaintain.jsp";
	//服务活动新增-送检测
	private static final String DETECTION_URL = "/jsp/afterSales/serviceActivity/serviceActivityDetection.jsp";
	//明细查看页面
	private static final String CHECK_URL = "/jsp/afterSales/serviceActivity/serviceActivityDetail.jsp";
	//VIN新增
	private static final String VIN_URL = "/jsp/afterSales/serviceActivity/addVin.jsp";
	//导入错误页面
	 private static final String importError_URL = "/jsp/afterSales/serviceActivity/importError.jsp";
	
	private ServiceActivityDao dao = ServiceActivityDao.getInstance();
	/**
	 * @DESCRIPTION 服务活动页面初始化
	 * @DATE 2017-03-28
	 * @author HS
	 * @VERSION 1.0
	 * @PARAM VOID
	 * */
	public void serviceActivityPageInit(){
		try {
			dao.deleteTempVINData();
			Map<String,String> map = new HashMap<String, String>();
			map.put("ACTIVITY_ID", SequenceManager.getSequence(""));
			act.setOutData("info", map);
			act.setForword(SREVICEACTIVITY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动页面初始化");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * @DESCRIPTION 服务活动页面查询
	 * @DATE 2017-03-28
	 * @author HS
	 * @VERSION 1.0
	 * @PARAM VOID
	 * */
	public void serviceActivityQuery(){
		try {
			String activityCode = CommonUtils.checkNull(request.getParamValue("activityCode"));//活动编号
			String activityName = CommonUtils.checkNull(request.getParamValue("activityName"));//活动名称
			String activityStatus = CommonUtils.checkNull(request.getParamValue("activityStatus"));//活动状态
			String activityType = CommonUtils.checkNull(request.getParamValue("activityType"));//活动状态

			Map<String,Object> params = new HashMap<String, Object>();
			params.put("activityCode", activityCode);
			params.put("activityName", activityName);
			params.put("activityStatus", activityStatus);
			params.put("activityType", activityType);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> ps = dao.getServiceActivityInfos(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动页面初始化");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	
	/**
	* 	新增页面跳转
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void serviceActivityDetail(){
		try {
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));//判断页面跳转
			Map<String,String> map = new HashMap<String, String>();
			map.put("ACTIVITY_ID", SequenceManager.getSequence(""));
			act.setOutData("info", map);
			if("0".equals(flag)) act.setForword(TECHNIQUE_URL);//维修
			if("1".equals(flag)) act.setForword(MAINTAIN_URL);//送保养
			if("2".equals(flag)) act.setForword(DETECTION_URL);//送检测
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增页面跳转");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	* 	修改页面跳转
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void serviceActivityUpdate(){
		try {
			String type = CommonUtils.checkNull(request.getParamValue("type"));//判断页面跳转
			String id = CommonUtils.checkNull(request.getParamValue("id"));//ID
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtil.notNull(id)){
				params.put("id", id);
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> pr = dao.getServiceActivityInfos(params,Constant.PAGE_SIZE,curPage);
			if(null!=pr&&StringUtil.notNull(id)){
				List<Map<String, Object>> list = pr.getRecords();
				if(!CommonUtils.isNullList(list)&&list.size()==1){
					act.setOutData("info", list.get(0));
				}
			}
			List<Map<String, Object>> activityPart = dao.getActivityPart(id);
			List<Map<String, Object>> activityLabour = dao.getActivityLabour(id);
			List<Map<String, Object>> activityDealer = dao.getActivityDealer(id);
			if(activityDealer != null && activityDealer.size() > 0){
				String dealerId = "";
				if(activityDealer != null && activityDealer.size() > 0){
					for (Map<String, Object> map : activityDealer) {
						dealerId += map.get("DEALER_ID")+",";
					}
				}
				act.setOutData("dealerId", dealerId);
				act.setOutData("activityDealer", activityDealer);
			}
			if(activityLabour==null||activityLabour.size()==0){
				act.setOutData("isflag", "0");
			}
			List<Map<String, Object>> activityModel = dao.getActivityModel(id);
			if(activityModel != null && activityModel.size() > 0){
				String modelId = "";
				String modelCode = "";
				if(activityModel != null && activityModel.size() > 0){
					for (Map<String, Object> map : activityModel) {
						modelId += map.get("MODE_ID")+",";
						modelCode += map.get("WRGROUP_CODE")+",";
					}
				}
				modelId = modelId.substring(0,modelId.length() - 1);
				modelCode = modelCode.substring(0,modelCode.length() - 1);
				act.setOutData("modelId", modelId);
				act.setOutData("modelCode", modelCode);
			}
			
			//获取附件根据claimId				
            List<FsFileuploadPO> fsList = new ArrayList<FsFileuploadPO>();
            if (id.toString() != null) {
                FsFileuploadPO fs = new FsFileuploadPO();
                fs.setYwzj(Long.parseLong(id.toString()));
                fsList = fac.select(fs);
            }
            //查询VIN
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("activityId", id);
            PageResult<Map<String,Object>> ps = dao.getActivityVin(map,10,curPage);
            List<Map<String,Object>> pt=ps.getRecords();
            if(pt!=null&&pt.size()>0){
            	act.setOutData("ps", ps);
            }else{
            	act.setOutData("pst", "0");
            }
			
            act.setOutData("fsList", fsList);
            act.setOutData("update", "0");
			act.setOutData("activityPart", activityPart);
			act.setOutData("activityLabour", activityLabour);
			if("96281001".equals(type)) act.setForword(TECHNIQUE_URL);//维修
			if("96281002".equals(type)) act.setForword(MAINTAIN_URL);//送保养
			if("96281003".equals(type)) act.setForword(DETECTION_URL);//送检测
			if("1".equals(type)) act.setForword(CHECK_URL);//查看
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增页面跳转");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	/**
	* 	工时查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addLabour(){
		String cnt = CommonUtils.checkNull(request.getParamValue("cnt"));
		 
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> list=dao.addLabour(request,cnt,Constant.PAGE_SIZE,curPage);
		act.setOutData("ps", list);
	}
	
	/**
	* 	配件查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPart(){
		String cnt = CommonUtils.checkNull(request.getParamValue("cnt"));
		 
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> list=dao.addPart(request,cnt,Constant.PAGE_SIZE,curPage);
		act.setOutData("ps", list);
	}
	
	/**
	* 	经销商查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getDealer(){
		try {
			String code = request.getParamValue("code");
			String name = request.getParamValue("name");
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("code", code);
			params.put("name", name);
			PageResult<Map<String,Object>> ps = dao.getDealer(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动页面添加获取经销商");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	* 	VIN查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getActivityVin(){
		try {
			String code = CommonUtils.checkNull(request.getParamValue("code"));//经销商编码
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			String activityId = CommonUtils.checkNull(request.getParamValue("activityId"));//activityId
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("vin", vin);
			params.put("code", code);
			params.put("activityId", activityId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> ps = dao.getActivityVin(params,10,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动页面vin");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	/**
	* 	VIN删除
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void deleteVinData(){
		try{
			String id = request.getParamValue("id");
			if(id!=null && !"".equals(id)){
				TtAsWrActivityVinPO po = new TtAsWrActivityVinPO();
				po.setId(Long.valueOf(id));
				dao.delete(po);
			}
			act.setOutData("msg", "01");
		}catch (Exception e) {
			logger.debug(e);
			BizException e1= new BizException(ErrorCodeConstant.SPECIAL_MEG);
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	* 	VIN新增
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addVin() {
		try {
			String id = request.getParamValue("activityId");//id
			act.setOutData("id", id);
			act.setForword(VIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "vin");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	/**
	 * 下载Excel模板
	 */
	public void downloadExcel() {
		try {
			// 创建HSSFWorkbook对象(excel的文档对象)
			HSSFWorkbook wb = new HSSFWorkbook();
			// 建立新的sheet对象（excel的表单）
			HSSFSheet sheet = wb.createSheet("服务活动vin导入模板");
			HSSFRow row2 = sheet.createRow(0);
			// 创建单元格并设置单元格内容
			row2.createCell(0).setCellValue("VIN");
			// 在sheet里创建第三行
			HSSFRow row3 = sheet.createRow(1);
			row3.createCell(0).setCellValue("LVZKN2175FC114363");
	
			// 输出Excel文件
			act.getResponse().setContentType("application/vnd.ms-excel");
			String fname = java.net.URLEncoder.encode("服务活动vin导入模板","utf-8");
			String filename= fname+BaseUtils.getSystemDateStr1()+".xls";
			fname=URLEncoder.encode(fname,"UTF-8");
			act.getResponse().addHeader("Content-disposition", "attachment;filename="+filename);
			OutputStream ouputStream = act.getResponse().getOutputStream();    
			wb.write(ouputStream);    
			ouputStream.flush();    
			ouputStream.close();  	
		} catch (Exception e) {
			logger.debug(e);
			BizException e1= new BizException(ErrorCodeConstant.SPECIAL_MEG);
			e.printStackTrace();
			act.setException(e1);
		}
	}
	
	/**
	 * 导入Excel
	 */
	public void uploadExcel() {
		try {
			String id = request.getParamValue("id");//id
			FileObject file = request.getParamObject("uploadFile");
			List<String> vinMap = new ArrayList<String>();
			List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
			if(file == null){
				act.setOutData("msg", "5");//文件为空
				act.setOutData("id", id);
				act.setForword(VIN_URL);
				return;
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			String fileName=file.getFileName();
			int k=fileName.lastIndexOf(".");
			String ext=fileName.substring(k+1, fileName.length());		//扩展名
			if(!ext.equalsIgnoreCase("xls")){
				act.setOutData("msg", "0");
				act.setOutData("id", id);
				act.setForword(VIN_URL);
				return;
			}
			fileName = fileName.substring(fileName.lastIndexOf("\\.") + 1,fileName.length());
			FileStore fs = FileStore.getInstance();
			String fileId = fs.write(file.getFileName(), file.getContent());
			InputStream is = fs.getInputStream(fileId);		
			PoiUtil pu = new PoiUtil(is);					//输入流套接进该对象
			List<List> datas = pu.getDatasInSheet(0);
			List<TtAsWrActivityVinTempPO> taList = new java.util.ArrayList<TtAsWrActivityVinTempPO>();
			if(CommonUtils.checkIsNullObject(datas) || datas.size() == 0){
				act.setOutData("msg", "1");    
				act.setOutData("id", id);
				act.setForword(VIN_URL);
				return;
			}else {
				for (int i=1;i<datas.size();i++) { 
					List row = (List) datas.get(i);
					Map<String, Object> params = new HashMap<String, Object>();
					TtAsWrActivityVinTempPO po = new TtAsWrActivityVinTempPO();
					if(StringUtil.notNull((String) row.get(0))){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("vin", (String) row.get(0));
						PageResult<Map<String, Object>> pr = dao.getVehicleVin(map, Constant.PAGE_SIZE, curPage);
						List<Map<String, Object>> list = pr.getRecords();
						if(list != null && list.size() > 0){
							po.setVin(row.get(0).toString());
							po.setActivityId(Long.parseLong(id));
							po.setId(Long.valueOf(SequenceManager.getSequence("")));
							po.setCreateBy(logonUser.getUserId());
							po.setCreateDate(new Date());
							params.put("vin", row.get(0).toString());
							params.put("id", id);
							
						}else{
							act.setOutData("msg", "6");
							act.setOutData("message", i);
							act.setOutData("id", id);
							act.setForword(VIN_URL);
							return;	
						}
						
					}else{
						act.setOutData("msg", "2");
						act.setOutData("message", i);
						act.setOutData("id", id);
						act.setForword(VIN_URL);
						return;
					}
					taList.add(po);
					vinMap.add((String) row.get(0));
				}
			}
			//删除临时表数据
			dao.deleteTempData(id);
			for (TtAsWrActivityVinTempPO po : taList) {
				dao.addData(po);
			}
			//判断导入数据是否重复
			List<Map<String, Object>> list = dao.getTempData(id);
			
			if(list != null && list.size() > 0){
				//删除临时表数据
				dao.deleteTempData(id);
				for(int m=0;m<vinMap.size();m++){
					for (int j=0;j<list.size();j++) {
						if(list.get(j).get("VIN").toString().equals(vinMap.get(m).toString())){
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("num",m+1);
							map.put("vin",list.get(j).get("VIN").toString());
							map.put("msg","vin重复");
							errorList.add(map);
							
						}
					}
				}
				if(errorList!=null&&errorList.size()>0){
					act.setOutData("errorList", errorList);
					act.setForword(importError_URL);
					return;
				}
			}else{
				for (TtAsWrActivityVinTempPO pot : taList) {
					TtAsWrActivityVinPO po = new TtAsWrActivityVinPO();
					po.setActivityId(Long.parseLong(id));
					po.setId(Long.valueOf(SequenceManager.getSequence("")));
					po.setDealerId(pot.getDealerId());
					po.setVin(pot.getVin());
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(new Date());
					dao.insert(po);
					//删除临时表数据
					dao.deleteTempData(id);
				}
			}
			act.setOutData("msg", "4");
			act.setOutData("id", id);
			act.setForword(VIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "导入服务活动失败，请联系管理员");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存服务活动数据
	 */
	
	public void saveData(){
		try {
			
			String updateId = request.getParamValue("updateId");//id
			String id = request.getParamValue("activityId");//id
			String activity_name = request.getParamValue("activity_name");//活动名称
			String activity_type = request.getParamValue("activity_type");//活动类型
			String activity_code = request.getParamValue("activity_code");//活动编号
			String activity_status = request.getParamValue("activity_status");//活动状态
			String activity_strate_date = request.getParamValue("activity_strate_date");//活动开始日期
			String activity_end_date = request.getParamValue("activity_end_date");//活动结束日期
			
			String activity_strate_mileage = request.getParamValue("activity_strate_mileage");//里程开始
			String activity_end_mileage = request.getParamValue("activity_end_mileage");//里程结束
			String MODEL11 = request.getParamValue("MODEL_ID11");//车型选择
			String activity_sales_strate_date = request.getParamValue("activity_sales_strate_date");//实销日期 开始
			String activity_sales_end_date = request.getParamValue("activity_sales_end_date");//实销日期 结束
			
			String activity_discount = request.getParamValue("activity_discount");//折扣率
			String maintain_number = request.getParamValue("maintain_number");//保养次数
			String maintain_money = request.getParamValue("maintain_money");//保养金额
			String detection_money = request.getParamValue("detection_money");//检测金额

			
			String trouble_desc = request.getParamValue("trouble_desc");//故障描述
			String trouble_reason = request.getParamValue("trouble_reason");//故障原因
			String maintenance_measures = request.getParamValue("maintenance_measures");//故障原因
			
			
			String dealer_id = request.getParamValue("dealer_id");//下发经销商
			
			String LABOUR_CODE [] = request.getParamValues("LABOUR_CODE");//工时代码
			String LABOUR_NAME [] = request.getParamValues("LABOUR_NAME");//工时名称
			String LABOUR_HOUR [] = request.getParamValues("LABOUR_HOUR");//工时数
			
			String PART_ID [] = request.getParamValues("PART_ID");//配件ID
			String PART_CODE [] = request.getParamValues("PART_CODE");//配件代码
			String PART_NAME [] = request.getParamValues("PART_NAME");//配件名称
			String QUANTITY [] = request.getParamValues("QUANTITY");//申请数量
			String Labour0 [] = request.getParamValues("Labour0");//关联工时
			String Part0 [] = request.getParamValues("Part0");//关联主因件
			String HAS_PART [] = request.getParamValues("HAS_PART");//是否关联件
			String PART_USE_TYPE [] = request.getParamValues("PART_USE_TYPE");//配件使用类型
			Long userId=logonUser.getUserId();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("id", id);
			PageResult<Map<String,Object>> pr = dao.getVin(params,Constant.PAGE_SIZE,curPage);
			List<Map<String, Object>> list = pr.getRecords();
			if(list == null || list.size() == 0){
				if(MODEL11==null&& activity_strate_mileage==null
						&& activity_end_mileage==null && activity_sales_strate_date==null && activity_sales_end_date==null){
					act.setOutData("message", "当没有导入vin码时，里程设置、车型选择、实销日期应至少有一个不为空！");
					return;
				}
			}
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("activity_code", activity_code);
			map.put("activityId", id);
			if("".equals(updateId)){
			PageResult<Map<String,Object>> ps = dao.getServiceActivityInfos(map,Constant.PAGE_SIZE,curPage);
			List<Map<String, Object>> list2 = ps.getRecords();
			if(list2 != null && list2.size() > 0){
				act.setOutData("message", "该服务活动编号已存在，请重新录入 ！");
				return;
				}
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Map<String,Object> mp = new HashMap<String, Object>();
			
			TtAsWrActivityPO po = new TtAsWrActivityPO();//服务活动管理
			List<TtAsWrActivityDealerPO> listPo1 = new ArrayList<TtAsWrActivityDealerPO>();
			List<TtAsWrActivityHoursPO> listPo2 = new ArrayList<TtAsWrActivityHoursPO>();
			List<TtAsWrActivityModelPO> listPo3 = new ArrayList<TtAsWrActivityModelPO>();
			List<TtAsWrActivityPartPO> listPo4 = new ArrayList<TtAsWrActivityPartPO>();
			if(StringUtil.notNull(dealer_id)){
				String dealerId[] = dealer_id.split(",");
				if(dealerId != null && dealerId.length > 0){
					for (int i = 0; i < dealerId.length; i++) {
						if(StringUtil.notNull(dealerId[i])){
							TtAsWrActivityDealerPO po1 = new TtAsWrActivityDealerPO();//活动经销商表
							po1.setActivityId(Long.parseLong(id));
							po1.setId(Long.valueOf(SequenceManager.getSequence("")));
							po1.setDealerId(Long.parseLong(dealerId[i]));
							po1.setCreateBy(userId);
							po1.setCreateDate(new Date());
							listPo1.add(po1);
						}
					}
				}
			}
			if(LABOUR_CODE != null){
				for (int i = 0; i < LABOUR_CODE.length; i++) {
						TtAsWrActivityHoursPO po2 = new TtAsWrActivityHoursPO();//活动工时
						po2.setId(Long.valueOf(SequenceManager.getSequence("")));
						po2.setActivityId(Long.parseLong(id));
						po2.setHoursName(LABOUR_NAME[i]);
						po2.setHoursCode(LABOUR_CODE[i]);
						po2.setApplyHoursCount(Float.valueOf(LABOUR_HOUR[i]));
						listPo2.add(po2);
					}
			}
			if(PART_ID!=null){
				for (int i = 0; i < PART_ID.length; i++) {
					TtAsWrActivityPartPO po4 = new TtAsWrActivityPartPO();//增加配件
					po4.setId(Long.valueOf(SequenceManager.getSequence("")));
					po4.setActivityId(Long.parseLong(id));
					po4.setPartId(Long.valueOf(PART_ID[i]));
					po4.setPartCode(PART_CODE[i]);
					po4.setPartName(PART_NAME[i]);
					po4.setApplyPartCount(Integer.valueOf(QUANTITY[i]));
					po4.setActivityHoursCode(Labour0[i]);
					po4.setIsMain(Integer.parseInt(HAS_PART[i]));
					po4.setPartUseType(Integer.parseInt(PART_USE_TYPE[i]));
					if(Part0[i]!=null&&!"".equals(Part0[i])){
						po4.setPartMainId(Long.valueOf(Part0[i]));
					}
					listPo4.add(po4);
				}
			}	
				//增加车型
			if(StringUtil.notNull(MODEL11)){
				String model[] = MODEL11.split(",");
				if(model != null && model.length > 0){
					for (int i = 0; i < model.length; i++) {
						TtAsWrActivityModelPO po3 = new TtAsWrActivityModelPO();
						po3.setActivityId(Long.parseLong(id));
						po3.setId(Long.valueOf(SequenceManager.getSequence("")));
						po3.setModeId(Long.parseLong(model[i]));
						listPo3.add(po3);
					}
				}
			}
			po.setActivityCode(activity_code);
			po.setActivityName(activity_name);
			po.setActivityStrateDate(formatter.parse(activity_strate_date));
			po.setActivityEndDate(formatter.parse(activity_end_date));
			po.setActivityStatus(Integer.parseInt(activity_status));
			po.setActivityDiscount(Integer.parseInt(activity_discount));
			po.setActivityType(Integer.parseInt(activity_type));
			if(StringUtil.notNull(trouble_reason)){
				po.setFaultProblem(trouble_reason);
			}
			if(StringUtil.notNull(maintenance_measures)){
				po.setMaintenanceMeasures(maintenance_measures);
			}
			if(StringUtil.notNull(trouble_desc)){
				po.setFaultDescription(trouble_desc);
			}
			if(StringUtil.notNull(maintain_money)){
			po.setMaintainMoney(Float.valueOf(maintain_money));
			}
			if(StringUtil.notNull(maintain_number)){
			po.setMaintainNumber(Integer.parseInt(maintain_number));
			}
			if(StringUtil.notNull(detection_money)){
			po.setDetectionMoney(Float.valueOf(detection_money));
			}
			
			if(StringUtil.notNull(activity_strate_mileage)){
				po.setActivityStrateMileage(Integer.parseInt(activity_strate_mileage));
			}
			if(StringUtil.notNull(activity_end_mileage)){
				po.setActivityEndMileage(Integer.parseInt(activity_end_mileage));
			}
			if(StringUtil.notNull(activity_sales_strate_date)){
				po.setActivitySalesStrateDate(formatter.parse(activity_sales_strate_date));
			}
			if(StringUtil.notNull(activity_sales_end_date)){
				po.setActivitySalesEndDate(formatter.parse(activity_sales_end_date));
			}
			
			mp.put("po", po);
			mp.put("listPo1", listPo1);
			mp.put("listPo2", listPo2);
			mp.put("listPo3", listPo3);
			mp.put("listPo4", listPo4);
			this.saveData(mp,updateId,id);
			if(Constant.SERVICEACTIVITY_STATUS_NEW_01.toString().equals(activity_status)){
				act.setOutData("message", "1");
			}
			if(Constant.SERVICEACTIVITY_STATUS_NEW_02.toString().equals(activity_status)){
				act.setOutData("message", "2");
			}
			
			} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动添加页面");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void saveData(Map<String,Object> mp,String updateId,String id) throws Exception {
		//id不为null修改，为null添加
		if(StringUtil.notNull(updateId)){
			TtAsWrActivityPO po = (TtAsWrActivityPO)mp.get("po");
			
			//插入附件 
	        String ywzj = id.toString();   //取值为单据的id，fs_fileupload表的ywzj字段
	        String[] fjids = request.getParamValues("fjid");   //页面提交时，通过附件控件上传的附件id ，fs_fileupload表的fjid字段
	        FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);          // 删除已保存的附件记录，避免重复
	        FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);   // 保存所有的附件记录
	        
	        po.setUpdateBy(logonUser.getUserId());
	        po.setUpdateDate(new Date());
	        TtAsWrActivityPO poo=new TtAsWrActivityPO();
	        poo.setActivityId(Long.valueOf(id));
	        dao.update(poo, po);
			TtAsWrActivityDealerPO po1 = new TtAsWrActivityDealerPO();
			po1.setActivityId(Long.valueOf(id));
			dao.delete(po1);
			
			TtAsWrActivityHoursPO po2 = new TtAsWrActivityHoursPO();
			po2.setActivityId(Long.valueOf(id));
			dao.delete(po2);
			
			TtAsWrActivityModelPO po3 = new TtAsWrActivityModelPO();
			po3.setActivityId(Long.valueOf(id));
			dao.delete(po3);
			
			TtAsWrActivityPartPO po4 = new TtAsWrActivityPartPO();
			po4.setActivityId(Long.valueOf(id));
			dao.delete(po4);
			List<TtAsWrActivityDealerPO> listPo1 = (List<TtAsWrActivityDealerPO>) mp.get("listPo1");
			for (TtAsWrActivityDealerPO ttAsWrActivityDealerPO : listPo1) {
				dao.insert(ttAsWrActivityDealerPO);
			}
			List<TtAsWrActivityHoursPO> listPo2 = (List<TtAsWrActivityHoursPO>) mp.get("listPo2");
			for (TtAsWrActivityHoursPO ttAsWrActivityHoursPO : listPo2) {
				dao.insert(ttAsWrActivityHoursPO);
			}
			List<TtAsWrActivityModelPO> listPo3 = (List<TtAsWrActivityModelPO>) mp.get("listPo3");
			for (TtAsWrActivityModelPO ttAsWrActivityModelPO : listPo3) {
				dao.insert(ttAsWrActivityModelPO);
			}
			List<TtAsWrActivityPartPO> listPo4 = (List<TtAsWrActivityPartPO>) mp.get("listPo4");
			for (TtAsWrActivityPartPO ttAsWrActivityPartPO : listPo4) {
				dao.insert(ttAsWrActivityPartPO);
			}
		}else{
			TtAsWrActivityPO po = (TtAsWrActivityPO)mp.get("po");
			//插入附件 
	        String ywzj = id.toString();   //取值为单据的id，fs_fileupload表的ywzj字段
	        String[] fjids = request.getParamValues("fjid");   //页面提交时，通过附件控件上传的附件id ，fs_fileupload表的fjid字段
	        FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);          // 删除已保存的附件记录，避免重复
	        FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);   // 保存所有的附件记录
	        po.setActivityId(Long.parseLong(id));
	        po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
	        dao.insert(po);
			List<TtAsWrActivityDealerPO> listPo1 = (List<TtAsWrActivityDealerPO>) mp.get("listPo1");
			for (TtAsWrActivityDealerPO ttAsWrActivityDealerPO : listPo1) {
				dao.insert(ttAsWrActivityDealerPO);
			}
			List<TtAsWrActivityHoursPO> listPo2 = (List<TtAsWrActivityHoursPO>) mp.get("listPo2");
			for (TtAsWrActivityHoursPO ttAsWrActivityHoursPO : listPo2) {
				dao.insert(ttAsWrActivityHoursPO);
			}
			List<TtAsWrActivityModelPO> listPo3 = (List<TtAsWrActivityModelPO>) mp.get("listPo3");
			for (TtAsWrActivityModelPO ttAsWrActivityModelPO : listPo3) {
				dao.insert(ttAsWrActivityModelPO);
			}
			List<TtAsWrActivityPartPO> listPo4 = (List<TtAsWrActivityPartPO>) mp.get("listPo4");
			for (TtAsWrActivityPartPO ttAsWrActivityPartPO : listPo4) {
				dao.insert(ttAsWrActivityPartPO);
			}
		}
	}
	
	/**
	* 	发布
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void changeStatus(){
		try{
			String id = request.getParamValue("id");
			if(id!=null && !"".equals(id)){
				dao.changeStatus(id,logonUser.getUserId());
			}
			act.setOutData("msg", "01");
		}catch (Exception e) {
			logger.debug(e);
			BizException e1= new BizException(ErrorCodeConstant.SPECIAL_MEG);
			e.printStackTrace();
			act.setException(e1);
		}
	}
	
	/**
	* 	删除活动
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void deleteData(){
		try{
			String id = request.getParamValue("id");
			if(id!=null && !"".equals(id)){
				TtAsWrActivityPO po = new TtAsWrActivityPO();
				po.setActivityId(Long.valueOf(id));
				dao.delete(po);
			}
			act.setOutData("msg", "01");
		}catch (Exception e) {
			logger.debug(e);
			BizException e1= new BizException(ErrorCodeConstant.SPECIAL_MEG);
			e.printStackTrace();
			act.setException(e1);
		}
	}
}
