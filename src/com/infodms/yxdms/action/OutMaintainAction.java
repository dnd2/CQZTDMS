package com.infodms.yxdms.action;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.OutMainTainDao;
import com.infodms.yxdms.entity.maintain.TtAsEgressPO;
import com.infodms.yxdms.entity.maintain.TtAsWrEgressRecordPO;
import com.infodms.yxdms.entity.special.TtAsWrSpeGoodwillClaimPO;
import com.infodms.yxdms.entity.special.TtAsWrSpecialPO;
import com.infodms.yxdms.entity.special.TtAsWrSpecialRecordPO;
import com.infodms.yxdms.service.OutMainTainService;
import com.infodms.yxdms.service.impl.OutMainTainServiceImpl;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

/**
 * 2016-07-21 外出维修申请
 * 
 * @author Administrator
 * 
 */
public class OutMaintainAction {
	private Logger logger = Logger.getLogger(this.getClass());
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private static POFactory fac = POFactoryBuilder.getInstance();
	private OutMainTainService service = new OutMainTainServiceImpl();
	private OutMainTainDao dao = new OutMainTainDao();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private int pageSize = Constant.PAGE_SIZE;// 页面显示行数
	private Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
	private String MaintainSelect = "/jsp/afterSales/MainTain/MaintainApplicationList.jsp";
	private String MA_ADD = "/jsp/afterSales/MainTain/MaintainApplicationAdd.jsp";//添加
	private String MA_SE = "/jsp/afterSales/MainTain/MaintainApplicationSe.jsp";//查看
	private String MA_BL = "/jsp/afterSales/MainTain/MaintainApplicationBL.jsp";//补录
	private String MA_CHECK = "/jsp/afterSales/MainTain/MaintainApplicationCheck.jsp";//审核
	private String MA_UPDATE = "/jsp/afterSales/MainTain/MaintainApplicationUpdate.jsp";//修改
	private String MA_PRINT = "/jsp/afterSales/MainTain/printModel.jsp";//打印
	//private String CHECK_Z = "/jsp/AfterSales/threeGuaratees/threeGuarateesResAddInit.jsp";//三包新增
	/**
	 * 打开外出维修 服务商段
	 */
	public void MaintainSelect() {
		try {
			act.setOutData("dealerId", logonUser.getDealerId());
			act.setForword(MaintainSelect);
		} catch (Exception e) {
			BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * 部长页面
	 */
	public void MaintainSelectB() {
		try {
			act.setOutData("type", "B");
			act.setForword(MaintainSelect);
		} catch (Exception e) {
			BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	
	
	/**
	 * 上报
	 */
	@SuppressWarnings("unchecked")
	public void auditing() {
		try {
			String id = request.getParamValue("id");
			TtAsEgressPO po = new TtAsEgressPO();
			TtAsEgressPO uppo = new TtAsEgressPO();
			po.setId(Long.parseLong(id));
			uppo.setStatus(Constant.OUT_MAINTAIN_02);
			dao.update(po, uppo);
			act.setOutData("message", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * 审核
	 */
	@SuppressWarnings("unchecked")
	public void auditingh() {
			String id = request.getParamValue("id");
			String s=DaoFactory.getParam(request, "s");
			String ministerAuditingReamrk=DaoFactory.getParam(request, "ministerAuditingReamrk");//审核意见
			try {
				if("1".equals(s)){//通过
					TtAsEgressPO po = new TtAsEgressPO();
					TtAsEgressPO uppo = new TtAsEgressPO();
					po.setId(Long.parseLong(id));				
					//审核					
					uppo.setMinisterAuditingReamrk(ministerAuditingReamrk);
					uppo.setMinisterAuditingDate(new Date());
					uppo.setMinisterAuditingBy(logonUser.getUserId());
					uppo.setStatus(Constant.OUT_MAINTAIN_04);				
					dao.update(po, uppo);
					//审核日志
					insertAuditRecord(Constant.OUT_MAINTAIN_04.toString(), logonUser.getUserId(), id,ministerAuditingReamrk);
				}else if("2".equals(s)){//驳回
					TtAsEgressPO po = new TtAsEgressPO();
					TtAsEgressPO uppo = new TtAsEgressPO();
					po.setId(Long.parseLong(id));				
					//审核					
					uppo.setMinisterAuditingReamrk(ministerAuditingReamrk);
					uppo.setMinisterAuditingDate(new Date());
					uppo.setMinisterAuditingBy(logonUser.getUserId());
					uppo.setStatus(Constant.OUT_MAINTAIN_03);				
					dao.update(po, uppo);
					//审核日志
					insertAuditRecord(Constant.OUT_MAINTAIN_03.toString(), logonUser.getUserId(), id,ministerAuditingReamrk);
				}
				else if("3".equals(s)){//拒绝
					TtAsEgressPO po = new TtAsEgressPO();
					TtAsEgressPO uppo = new TtAsEgressPO();
					po.setId(Long.parseLong(id));				
					//审核					
					uppo.setMinisterAuditingReamrk(ministerAuditingReamrk);
					uppo.setMinisterAuditingDate(new Date());
					uppo.setMinisterAuditingBy(logonUser.getUserId());
					uppo.setStatus(Constant.OUT_MAINTAIN_05);				
					dao.update(po, uppo);
					//审核日志
					insertAuditRecord(Constant.OUT_MAINTAIN_05.toString(), logonUser.getUserId(), id,ministerAuditingReamrk);
				}
				
			act.setOutData("message", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
/**
 * 插入审核日志
 * @param status
 * @param userId
 * @param speId
 */
public void insertAuditRecord(String status, Long userId, String speId,String c) {
	try {
		TtAsWrEgressRecordPO po=new TtAsWrEgressRecordPO();
		po.setAuditBy(userId);
		po.setAuditDate(new Date());
		po.setOperaStstus(Integer.parseInt(status));
		po.setId(DaoFactory.getPkId());
		po.setEgressId(Long.parseLong(speId));
		po.setAuditRecord(c);
		dao.insert(po);
	} catch (Exception e) {
		BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
		e.printStackTrace();
		act.setException(e1);
	}
}	
	
	/**
	 * 外出维修查询
	 */
	public void MaintainSelectList() {
		try {
			// 传入参数
			Map<String, Object> paraMap = new HashMap<String, Object>();
			String egressNo = CommonUtils.checkNull(request.getParamValue("egress_no"));//外出维修申请单号
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//起止日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束日期
			if(StringUtils.isNotBlank(endDate)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
			    Date date = sdf.parse(endDate);  
				Calendar calendar   =   new   GregorianCalendar(); 
			    calendar.setTime(date); 
			    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
			    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
			    String SDate = sdf.format(date);
			    paraMap.put("endDate",SDate);
			}
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			//String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));//发动机号
			String status = CommonUtils.checkNull(request.getParamValue("status"));//单据状态
			String dealerId=CommonUtils.checkNull(request.getParamValue("dealerId"));
			if(logonUser.getDealerId()==null || logonUser.getDealerId()==""){
				paraMap.put("ftype", "f");
				paraMap.put("dealerId", dealerId);
			}else{
				paraMap.put("dealerId", logonUser.getDealerId());
			}
			paraMap.put("egressNo", egressNo);
			paraMap.put("startDate", startDate);
			paraMap.put("vin", vin);
			//paraMap.put("engineNo", engineNo);
			paraMap.put("status", status);
			
			PageResult<Map<String, Object>> ps = service.Maintainselect(paraMap, curPage, pageSize);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	
	
	/**
	 * 下载Excel模板
	 */
	public void downloadExcel() {
		try {
			String id = request.getParamValue("id");
			Map<String, Object> params = new HashMap<String, Object>();
			if(StringUtils.isNotEmpty(id)){
				params.put("id", id);
			}
			PageResult<Map<String, Object>> pr = service.Maintainselect(params, curPage, pageSize);
			List<Map<String, Object>> list = pr.getRecords();
			if(list != null && list.size() > 0){
				Map<String, Object> map = list.get(0);
				// 创建HSSFWorkbook对象(excel的文档对象)
				HSSFWorkbook wb = new HSSFWorkbook();
				// 建立新的sheet对象（excel的表单）
				HSSFSheet sheet = wb.createSheet("外出救援单");
				HSSFRow row0 = sheet.createRow(0);
				// 创建单元格并设置单元格内容
				row0.createCell(0).setCellValue("单据编码");
				row0.createCell(1).setCellValue("制单人姓名");
				row0.createCell(2).setCellValue("制单日期");
				row0.createCell(3).setCellValue("服务站编码");
				row0.createCell(4).setCellValue("服务站");
				row0.createCell(5).setCellValue("联系电话");
				row0.createCell(6).setCellValue("VIN码");
				//row0.createCell(7).setCellValue("发动机号");
				row0.createCell(7).setCellValue("车系名称");
				row0.createCell(8).setCellValue("车型名称");
				row0.createCell(9).setCellValue("配置");
				row0.createCell(10).setCellValue("车牌号");
				row0.createCell(11).setCellValue("出厂日期");
				row0.createCell(12).setCellValue("购车日期");
				row0.createCell(13).setCellValue("车主名字");
				row0.createCell(14).setCellValue("客户名字");
				row0.createCell(15).setCellValue("客户电话");
				row0.createCell(16).setCellValue("用户地址");
				row0.createCell(17).setCellValue("行驶里程");
				row0.createCell(18).setCellValue("救急里程(公里)");
				row0.createCell(19).setCellValue("救急所在省");
				row0.createCell(20).setCellValue("救急所在市");
				row0.createCell(21).setCellValue("救急所在县(区)");
				row0.createCell(22).setCellValue("救急详细地址");
				row0.createCell(23).setCellValue("救急开始时间");
				row0.createCell(24).setCellValue("救急结束时间");
				row0.createCell(25).setCellValue("救急人数");
				row0.createCell(26).setCellValue("派车车牌号");
				row0.createCell(27).setCellValue("救急人名字");
				row0.createCell(28).setCellValue("审批人");
				row0.createCell(29).setCellValue("审批时间");
				row0.createCell(30).setCellValue("申请内容");
				row0.createCell(31).setCellValue("申请意见");
				// 在sheet里创建第三行
				HSSFRow row1 = sheet.createRow(1);
				if(map.get("EGRESS_NO") != null){
					row1.createCell(0).setCellValue(map.get("EGRESS_NO").toString());
				}
				if(map.get("CREATE_BY") != null){
					row1.createCell(1).setCellValue(map.get("CREATE_BY").toString());
				}
				if(map.get("CREATE_DATE") != null){
					row1.createCell(2).setCellValue(map.get("CREATE_DATE").toString());
				}
				if(map.get("DEALER_CODE") != null){
					row1.createCell(3).setCellValue(map.get("DEALER_CODE").toString());
				}
				if(map.get("DEALER_NAME") != null){
					row1.createCell(4).setCellValue(map.get("DEALER_NAME").toString());
				}
				if(map.get("PHONE") != null){
					row1.createCell(5).setCellValue(map.get("PHONE").toString());
				}
				if(map.get("VIN") != null){
					row1.createCell(6).setCellValue(map.get("VIN").toString());
				}
				/*if(map.get("ENGINE_NO") != null){
					row1.createCell(7).setCellValue(map.get("ENGINE_NO").toString());
				}*/
				if(map.get("SE_NAME") != null){
					row1.createCell(7).setCellValue(map.get("SE_NAME").toString());
				}
				if(map.get("GROUP_NAME") != null){
					row1.createCell(8).setCellValue(map.get("GROUP_NAME").toString());
				}
				if(map.get("PZ_NAME") != null){
					row1.createCell(9).setCellValue(map.get("PZ_NAME").toString());
				}
				if(map.get("LICENSE_NO") != null){
					row1.createCell(10).setCellValue(map.get("LICENSE_NO").toString());
				}
				if(map.get("FACTORY_DATE") != null){
					row1.createCell(11).setCellValue(map.get("FACTORY_DATE").toString());
				}
				if(map.get("PURCHASED_DATE") != null){
					row1.createCell(12).setCellValue(map.get("PURCHASED_DATE").toString());
				}
				if(map.get("CTM_NAME") != null){
					row1.createCell(13).setCellValue(map.get("CTM_NAME").toString());
				}
				if(map.get("CUSTOMER_NAME") != null){
					row1.createCell(14).setCellValue(map.get("CUSTOMER_NAME").toString());
				}
				if(map.get("TELEPHONE") != null){
					row1.createCell(15).setCellValue(map.get("TELEPHONE").toString());
				}
				if(map.get("ADDRESS") != null){
					row1.createCell(16).setCellValue(map.get("ADDRESS").toString());
				}
				if(map.get("MILEAGE") != null){
					row1.createCell(17).setCellValue(map.get("MILEAGE").toString());
				}
				if(map.get("RELIEF_MILEAGE") != null){
					row1.createCell(18).setCellValue(map.get("RELIEF_MILEAGE").toString());
				}
				if(map.get("PROVINCE") != null){
					row1.createCell(19).setCellValue(map.get("PROVINCE").toString());
				}
				if(map.get("CITY") != null){
					row1.createCell(20).setCellValue(map.get("CITY").toString());
				}
				
				if(map.get("COUNTY") != null){
					row1.createCell(21).setCellValue(map.get("COUNTY").toString());
				}
				//保证Excel里面数据不为空
				if(map.get("PROVINCE") != null || map.get("TOWN") != null){
					String PROVINCE = "";
					String CITY = "";
					String COUNTY = "";
					String TOWN = "";
					if(map.get("PROVINCE") != null){
						PROVINCE = map.get("PROVINCE").toString();
					}
					if(map.get("CITY") != null){
						CITY = map.get("CITY").toString();
					}
					if(map.get("COUNTY") != null){
						COUNTY = map.get("COUNTY").toString();
					}
					if(map.get("TOWN") != null){
						TOWN = map.get("TOWN").toString();
					}
					//row1.createCell(23).setCellValue(PROVINCE+CITY+COUNTY+TOWN);
				}
				if(map.get("TOWN") != null){
					row1.createCell(22).setCellValue(map.get("TOWN").toString());
				}
				if(map.get("E_START_DATE") != null){
					row1.createCell(23).setCellValue(map.get("E_START_DATE").toString());
				}
				if(map.get("E_END_DATE") != null){
					row1.createCell(24).setCellValue(map.get("E_END_DATE").toString());
				}
				if(map.get("E_NUM") != null){
					row1.createCell(25).setCellValue(map.get("E_NUM").toString());
				}
				if(map.get("E_LICENSE_NO") != null){
					row1.createCell(26).setCellValue(map.get("E_NUM").toString());
				}
				if(map.get("E_NAME") != null){
					row1.createCell(27).setCellValue(map.get("E_NAME").toString());
				}
				if(map.get("MINISTER_AUDITING_BY") != null){
					row1.createCell(28).setCellValue(map.get("MINISTER_AUDITING_BY").toString());
				}
				if(map.get("MINISTER_AUDITING_DATE") != null){
					row1.createCell(29).setCellValue(map.get("MINISTER_AUDITING_DATE").toString());
				}
				if(map.get("EGRESS_REAMRK") != null){
					row1.createCell(30).setCellValue(map.get("EGRESS_REAMRK").toString());
				}
				if(map.get("MINISTER_AUDITING_REAMRK") != null){
					row1.createCell(31).setCellValue(map.get("MINISTER_AUDITING_REAMRK").toString());
				}
				
				// 输出Excel文件
				act.getResponse().setContentType("application/vnd.ms-excel");
				String fname = java.net.URLEncoder.encode("外出维修单","utf-8");
				String filename= fname+BaseUtils.getSystemDateStr1()+".xls";
				fname=URLEncoder.encode(fname,"UTF-8");
				act.getResponse().addHeader("Content-disposition", "attachment;filename="+filename);
				OutputStream ouputStream = act.getResponse().getOutputStream();    
				wb.write(ouputStream);    
				ouputStream.flush();    
				ouputStream.close(); 
			}else{
				
			}
		} catch (Exception e) {
			logger.debug(e);
			BizException e1= new BizException(ErrorCodeConstant.SPECIAL_MEG);
			e.printStackTrace();
			act.setException(e1);
		}
	}
	
	
	/**
	 * 2017-03-23
	 * 添加、查看跳转页面
	 * @author zouyang
	 * 
	 */
	public void addOrCheckMaintainApplication(){
		try {
			String id = request.getParamValue("id");
			String check = request.getParamValue("check");
			String dealerId = logonUser.getDealerId();//经销商ID
			String type = request.getParamValue("type"); //部长标识
			//判断是查看还是保存页面
			if("1".equals(check) || "3".equals(check) || "6".equals(check)){
				Map<String, Object> params = new HashMap<String, Object>();
				if(StringUtils.isNotEmpty(id)){
					params.put("id", id);
				}
				params.put("dealerId", dealerId);
				PageResult<Map<String, Object>> pr = service.Maintainselect(params, curPage, pageSize);
				if(null!=pr&&StringUtils.isNotEmpty(id)){
					List<Map<String, Object>> list = pr.getRecords();
					act.setOutData("info", list.get(0));
				}
				List<Map<String, Object>> rList=service.specialRecord(id);
				act.setOutData("rList", rList);
				act.setOutData("check", check);
				act.setOutData("dealerId", dealerId);
				act.setOutData("type", type);
				if("3".equals(check)){
					act.setForword(MA_CHECK);
				}else if("1".equals(check)){
					act.setForword(MA_SE);
				}else if("6".equals(check)){
					act.setForword(MA_BL);
				}
				
			}else if("2".equals(check)){
				Map<String, Object> params = new HashMap<String, Object>();
				if(StringUtils.isNotEmpty(id)){
					params.put("id", id);
				}
				params.put("dealerId", dealerId);
				PageResult<Map<String, Object>> pr = service.Maintainselect(params, curPage, pageSize);
				if(null!=pr&&StringUtils.isNotEmpty(id)){
					List<Map<String, Object>> list = pr.getRecords();
					act.setOutData("info", list.get(0));
				}
				act.setForword(MA_UPDATE);
			}else if("4".equals(check)){
				Map<String, Object> params = new HashMap<String, Object>();
				if(StringUtils.isNotEmpty(id)){
					params.put("id", id);
				}
				params.put("dealerId", dealerId);
				PageResult<Map<String, Object>> pr = service.Maintainselect(params, curPage, pageSize);
				if(null!=pr&&StringUtils.isNotEmpty(id)){
					List<Map<String, Object>> list = pr.getRecords();
					act.setOutData("info", list.get(0));
				}
				act.setForword(MA_PRINT);
			}else{
				Long userId = logonUser.getUserId();//用户ID
				List<Map<String,Object>> listUser = dao.getName(userId);//制单人
				List<Map<String,Object>> dealerInfos = dao.getDealerStationInfos(dealerId);//服务站信息
				act.setOutData("listUser", listUser.get(0));
				act.setOutData("dealerInfos", dealerInfos.get(0));
				
				DateFormat format = new SimpleDateFormat("yyMMdd"); 
				String Numberdate = format.format(new Date());
				String serialNumber=GenerateNumber("DEALER_ID" ,logonUser.getDealerId().toString(),"TT_AS_EGRESS", "EGRESS_NO", "CREATE_DATE");
				act.setOutData("listCode", "SWJY"+Numberdate+serialNumber);//单据编码
				act.setOutData("date", BaseUtils.getSystemDateStr1());
				act.setForword(MA_ADD);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	//生成序列号-经销商id字段名，id值，表名，要生成的编码字段名，创建时间字段名
		public String GenerateNumber(String dealer_Id_name,String dealer_Id_value,String po,String obtainName,String createDateName){
			String no="";
			try {
				List<Map<String, Object>> list = dao.generateNumber(dealer_Id_name,dealer_Id_value,po,obtainName,createDateName);
				if(list.size()>0){
					no=list.get(0).get(obtainName).toString();
					no=no.substring(no.length()-4, no.length());
					Long max=Long.parseLong(no)+1;
					no=max.toString();
					int a=4-no.length();
					for(int i=0;i<a;i++){
						no="0"+no;
					}

				}else{
					no="0001";
				}
			}	catch (Exception e) {
				BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
				e.printStackTrace();
				act.setException(e1);
			}
			return no;
		}
		/**
		 * @description 三包维修登记单输入VIN码带出用户信息DLR
		 * @Date 2017-03-16
		 * @author hs
		 * @param void
		 * @version 1.0
		 * */
		public void showCustomerInfosByVin() {
			try {
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));// 获取VIN码
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("vin", vin);
				// 检索该VIN的车辆信息
				List<Map<String, Object>> isExist = dao.judgeIsVin(params);				
				if (null != isExist && isExist.size() > 0) {
					Map<String, Object> maps = isExist.get(0);
					act.setOutData("maps", maps);
					act.setOutData("msg", "00");
				}	
				
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "外出救援登记单输入VIN码带出用户信息");
				logger.error(logonUser, e1);
				act.setActionReturn(e1);
			}
		}
	/**
	 * 2017-03-23
	 * 转外出维修申请单跳转页面
	 * @author lsw
	 * 
	 */
	/*public void checkZ(){
		try {
			String id = request.getParamValue("id");
			String flag = request.getParamValue("flag");
			String dealerId = logonUser.getDealerId();//经销商ID
			List<Map<String, Object>> list=null;
			if("t".equals(flag)){
					Map<String, Object> params = new HashMap<String, Object>();
					if(StringUtils.isNotEmpty(id)){
						params.put("id", id);
					}
					params.put("dealerId", dealerId);
					PageResult<Map<String, Object>> pr = service.Maintainselect(params, curPage, pageSize);
					if(null!=pr&&StringUtils.isNotEmpty(id)){
						list = pr.getRecords();
						act.setOutData("info", list.get(0));//外出维修申请
					}
					Long userId = logonUser.getUserId();// 用户ID
					List<Map<String, Object>> listUser = service
							.getListUserName(userId.toString());// 制单人
					List<Map<String, Object>> dealerInfos = service
							.getDealerInfos(dealerId);// 服务站信息

					//是否显示预警规则
					Map<String, Object> map = waringRuleService.getWaringRuleCheck();
					if(map != null && map.size() > 0){
						act.setOutData("waringRule", map.get("STATUS"));
					}
				
					Map<String, Object> paramt = new HashMap<String, Object>();
					paramt.put("dealerCode", logonUser.getDealerCode());
					paramt.put("vin", list.get(0).get("VIN").toString());
					paramt.put("status", Constant.LIST_STATUS_05);
					Map<String, Object> workFeeMap = threeService.getWorkFee(paramt);
					if(workFeeMap != null && workFeeMap.size() > 0){
						act.setOutData("workFee", workFeeMap.get("PRICE").toString());
					}
					// 维修次数(除去状态是“已拒绝”状态)
					List<Map<String, Object>> repairTimes = threeService.getRepairTimes(paramt);
					if (null != repairTimes && repairTimes.size() > 0) {
						act.setOutData("repairTimes", repairTimes.get(0));
					}

					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String generalDate = sdf.format(new Date());
					String serialNumber = wa.GenerateNumber(logonUser.getDealerId(), "8060001", "REPAIR_TYPE","TT_AS_WR_APP_CLAIM", "CLAIM_NO", "CREAT_DATE");// 生成序列号
					String listCode = logonUser.getDealerCode() + "ZCSB"+ generalDate + serialNumber;// 单据编码
					act.setOutData("serialNo", listCode);
					act.setOutData("listUser", listUser.get(0));
					act.setOutData("userId", userId);
					act.setOutData("dealerInfos", dealerInfos.get(0));
			}
				act.setForword(CHECK_Z);
		} catch (Exception e) {
			BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}*/
	
	
	/**
	 * @author zouyang
	 * 保存数据
	 */
	@SuppressWarnings("unchecked")
	public void saveMaintainApplication(){
		try {
			
			String dealerId = logonUser.getDealerId();
			Long userId = logonUser.getUserId();
			String egressNo = CommonUtils.checkNull(request.getParamValue("egress_no"));//外出维修申请单号
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//行驶里程
			String reliefMileage = CommonUtils.checkNull(request.getParamValue("relief_mileage"));//单程救急里程
			String province = CommonUtils.checkNull(request.getParamValue("province"));//救急所在省
			String city = CommonUtils.checkNull(request.getParamValue("city"));//救急所在市
			String county = CommonUtils.checkNull(request.getParamValue("county"));//救急所在县
			String town = CommonUtils.checkNull(request.getParamValue("town"));//救急详细地址
			String egressReamrk = CommonUtils.checkNull(request.getParamValue("egress_reamrk"));//申请内容
			String ctmId = CommonUtils.checkNull(request.getParamValue("ctmId"));//客户id
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//VIN码
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));//联系人
			String mainPhone = CommonUtils.checkNull(request.getParamValue("tel"));//联系电话
			
			TtAsEgressPO po = new TtAsEgressPO();//外出维修申请单
			TtAsEgressPO po1 = new TtAsEgressPO();
	
			po.setStatus(Constant.OUT_MAINTAIN_01);//状态默认未上报
			if(StringUtils.isNotBlank(egressNo)){
				po.setEgressNo(egressNo);//外出维修申请单号
			}
			if(StringUtils.isNotBlank(vin)){
				po.setVin(vin);
			}
			po.setDealerId(Long.parseLong(dealerId));//服务站ID			
			if(StringUtils.isNotBlank(mileage)){
				po.setMileage(Integer.parseInt(mileage));//行驶里程
			}
			if(StringUtils.isNotBlank(reliefMileage)){
				po.setReliefMileage(Integer.parseInt(reliefMileage));//单程救急里程
			}
			if(StringUtils.isNotBlank(ctmId)){
				po.setCusmerId(Long.parseLong(ctmId));
			}
			po.setProvince(province);//救急所在省
			po.setCity(city);//救急所在市
			po.setCounty(county);//救急所在县
			po.setTown(town);//救急详细地址
			po.setEgressReamrk(egressReamrk);//申请内容
			po.setCustomerName(linkMan);//联系人
			po.setTelephone(mainPhone);//联系电话
			if(StringUtils.isNotBlank(id)){
				po1.setId(Long.parseLong(id));//id
				dao.update(po1, po);
			}else{
				po.setId(Long.parseLong(SequenceManager.getSequence("")));
				po.setCreateBy(userId);//制单人
				po.setCreateDate(new Date());
				dao.insert(po);
			}
			act.setOutData("message", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * @author zouyang
	 * 保存数据补录信息
	 */
	@SuppressWarnings("unchecked")
	public void saveMaintainApplicationBL(){
		try {
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			String statrDate = CommonUtils.checkNull(request.getParamValue("statrDate"));//救急开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//救急结束时间
			String eNum = CommonUtils.checkNull(request.getParamValue("eNum"));//救急人数
			String eName = CommonUtils.checkNull(request.getParamValue("eName"));//救急名字
			String eLicenseNo = CommonUtils.checkNull(request.getParamValue("eLicenseNo"));//救急车牌号
			TtAsEgressPO po = new TtAsEgressPO();//外出维修申请单
			TtAsEgressPO po1 = new TtAsEgressPO();
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(StringUtils.isNotBlank(id)){
				po.setId(Long.parseLong(id));//id
				po1.setEStartDate(format1.parse(statrDate));
				po1.setEEndDate(format1.parse(endDate));
				po1.setENum(eNum);
				po1.setEName(eName);
				po1.setELicenseNo(eLicenseNo);
				po1.setIsMakeUp(Constant.IF_TYPE_YES);
				dao.update(po, po1);
			}
			act.setOutData("message", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	
}
