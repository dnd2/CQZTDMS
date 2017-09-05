package com.infodms.dms.actions.customerRelationships.baseSetting;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;


import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.CRMSortDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtCrmSortShiftPO;
import com.infodms.dms.po.TtCrmWorktimePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.TC_CodeAddUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class CRMSortManager {
	public Logger logger = Logger.getLogger(CRMSortManager.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	CRMSortDAO dao = CRMSortDAO.getInstance();
	private final String CRMSortMainInit = "/jsp/customerRelationships/baseSetting/CRMSort/CRMSortMain.jsp";
	private final String CRMSortViewInit = "/jsp/customerRelationships/baseSetting/CRMSort/CRMSortView.jsp";
	private final String CRMSortAddInit = "/jsp/customerRelationships/baseSetting/CRMSort/CRMSortAdd.jsp";
	private final String CRMSortEditInit = "/jsp/customerRelationships/baseSetting/CRMSort/CRMSortEdit.jsp";
	
	
	/**
	 * @FUNCTION : 坐席排班主页面初始化
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void CRMSortMainInit(){
		try{
			String selectBox=TC_CodeAddUtil.genSelBoxExp(Constant.STATUS,"STATUS");
			act.setOutData("selectBox", selectBox);
			act.setForword(CRMSortMainInit);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @FUNCTION : 坐席排班主页面
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void getMainList(){
		try{
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")) : 1;
	        //String json =URLDecoder.decode(request.getParamValue("json"),"UTF-8");
			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");	        
	        JSONObject paraObject = JSONObject.fromObject(json);
	        paraObject.put("STATUS", request.getParamValue("STATUS"));
	         String WT_TYPE = request.getParamValue("WT_TYPE");
	        logger.info("---paraObject"+paraObject);
			PageResult<Map<String, Object>> ps = dao.getMainList(paraObject, WT_TYPE,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);   
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "坐席排班主页面错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @FUNCTION : 坐席排班查看页面
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void CRMSortViewInit(){
		try{
			String ssId = CommonUtils.checkNull(request.getParamValue("ssId"));
			Map<String,Object> dataMap = dao.getViewData(ssId);
			act.setOutData("ssId", ssId);
			act.setOutData("dataMap", dataMap);
			act.setForword(CRMSortViewInit);
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "坐席排班查看页面错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @FUNCTION : 坐席排班新增初始化
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void CRMSortAddInit(){
		try{            
			List<Map<String,Object>> wtTypeList = dao.getWtTypeList();
            act.setOutData("wtTypeList", wtTypeList);
			act.setForword(CRMSortAddInit);
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @FUNCTION : 坐席排班修改初始化
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void CRMSortEditInit(){	
		try{            
			List<Map<String,Object>> wtTypeList = dao.getWtTypeList();
            String ssId = request.getParamValue("ssId");
            Map<String, Object> dataMap = dao.getViewData(ssId);
            act.setOutData("wtTypeList", wtTypeList);
            act.setOutData("dataMap", dataMap);
			act.setForword(CRMSortEditInit);
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "未知错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	
	
	
	
	 public static String getSpecifiedDayAfter(String specifiedDay) {
	        Calendar c = Calendar.getInstance();
	        Date date = null;
	        try {
	            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        c.setTime(date);
	        int day = c.get(Calendar.DATE);
	        c.set(Calendar.DATE, day + 1);

	        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
	                .format(c.getTime());
	        return dayAfter;
	    }

	/**
	 * @FUNCTION : 坐席排班保存
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void CRMSortSave(){
		try {           			
			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");			
			JSONObject dataObject = JSONObject.fromObject(json);			
			//dataObject.put("WT_TYPE", request.getParamValue("WT_TYPE"));
			logger.info("---------dataObject="+dataObject);
			String ssId=dataObject.getString("SS_ID");
			logger.info("-------ssId="+ssId);
			TtCrmSortShiftPO po=new TtCrmSortShiftPO();
			TtCrmSortShiftPO oldpo=new TtCrmSortShiftPO();
			String codeDesc = "";
			if(!ssId.equals("")){
				logger.info("-------ssId1="+ssId);
			    po.setSsId(Long.parseLong(ssId));
			    oldpo.setSsId(Long.parseLong(ssId));
			}else{
				logger.info("-------ssId2="+ssId);
				po.setSsId(Long.parseLong(SequenceManager.getSequence("")));
			}
			po.setDutyDate(DateUtil.str2Date(dataObject.getString("DUTY_DATE"), "-"));
			
			po.setUserId(dataObject.getLong("USER_ID"));
			po.setSeAccount(dataObject.getString("ACNT"));
			TcUserPO u = new TcUserPO();
			u.setUserId(dataObject.getLong("USER_ID"));
			u = (TcUserPO) dao.select(u).get(0);
			//po.setSeName(dataObject.getString("NAME"));
			po.setSeName(u.getName());
			po.setWtType(dataObject.getInt("WT_TYPE"));
			SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			TtCrmWorktimePO crmWorktimePO = new TtCrmWorktimePO();
			crmWorktimePO.setWtType(dataObject.getInt("WT_TYPE"));
			crmWorktimePO = (TtCrmWorktimePO)dao.select(crmWorktimePO).get(0);
			if((""+dataObject.getInt("WT_TYPE")).equals("95501003") )
			{
				po.setStaDate(dateFormat.parse(dataObject.getString("DUTY_DATE")+" "+crmWorktimePO.getWtStaOnMinute()+":"+crmWorktimePO.getWtStaOffMinute2()));
				po.setEndDate(dateFormat.parse(getSpecifiedDayAfter(dataObject.getString("DUTY_DATE"))+" "+crmWorktimePO.getWtEndOnMinute()+":"+crmWorktimePO.getWtEndOffMinute()));

			}else
			{
				po.setStaDate(dateFormat.parse(dataObject.getString("DUTY_DATE")+" "+crmWorktimePO.getWtStaOnMinute()+":"+crmWorktimePO.getWtStaOffMinute2()));
				po.setEndDate(dateFormat.parse(dataObject.getString("DUTY_DATE")+" "+crmWorktimePO.getWtEndOnMinute()+":"+crmWorktimePO.getWtEndOffMinute()));

			}
			
			
			po.setShiftKind(dataObject.getString("SHIFT_KIND"));
			//po.setShiftKindDesc(dataObject.getString("SHIFT_KIND_DESC"));
			codeDesc = dao.getCodeDesc(dataObject.getString("SHIFT_KIND"));
			po.setShiftKindDesc(codeDesc);
			po.setSsBy(logonUser.getUserId());
			po.setSsDate(new Date());
			po.setStatus(Constant.STATUS_ENABLE);
			if(!ssId.equals("")){	
				po.setUpdateDate(new Date());
				po.setUpdateBy(logonUser.getUserId());
			    dao.update(oldpo,po);
			    act.setOutData("success", "保存成功!");
			}else{
				Map<String,Object> map=dao.getIsSort(dataObject.getLong("USER_ID"), dataObject.getString("DUTY_DATE"));//查看是否排版
				if(map == null || ((BigDecimal)map.get("RES")).intValue() == 0){
					dao.insert(po);
					act.setOutData("success", "保存成功!");
				}else{
					act.setOutData("alreadySort", "对不起,您选择的坐席在该时间已经排班!");
				}
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"坐席排班保存出错,请联系管理员!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	

	
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void CRMSortDel(){
		try{
			String ckids = request.getParamValue("ckids");//获取一个集合要删除的ID
			logger.info("-----ckids="+ckids);
			dao.CRMSortDel(ckids);
			act.setOutData("msg", "01");
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"删除错误,请联系系统管理员!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * @FUNCTION : 获取排班人员列表
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void getUserList(){
		try{
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")) : 1;
	        //String json =URLDecoder.decode(request.getParamValue("json"),"UTF-8");
			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");			
	        logger.info("---json"+json);
	        JSONObject paraObject = JSONObject.fromObject(json);
			PageResult<Map<String, Object>> ps = dao.getMemberList(paraObject, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps); 
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取人员列表错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	
	/**
	 * @FUNCTION : 获取坐席业务列表
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public void getShiftKindList(){
		try{
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")) : 1;
	        //String json =URLDecoder.decode(request.getParamValue("json"),"UTF-8");
			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");			
	        logger.info("---json"+json);
	        JSONObject paraObject = JSONObject.fromObject(json);
			PageResult<Map<String, Object>> ps = dao.getShiftKindList(paraObject, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps); 
		}
		catch (Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取坐席业务列表错误!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}	
	
	
    public void readExcel(){
    	ByteArrayInputStream is = null;
		try {
			act = ActionContext.getContext();
			RequestWrapper request = act.getRequest();
		//	String filePath = CommonUtils.checkNull(request.getParamValue("uploadFile"));
			FileObject uploadFile = request.getParamObject("uploadFile");
			String msgResult="",msgMinDateRow="",msgNotPepRow="",msgClassTypeRow="",msgPepFailRow="",msgPepMoreRow="";//总的提示/值班日期小于当前日期/没有匹配的值班员工/没有匹配的班次类型/员工号错误/员工已经排班
			String msgNullDate="",msgNullPepNo="",msgNullPepName="",msgNullPeType="";
			try{
				 is = new ByteArrayInputStream(uploadFile.getContent());
			}catch(Exception ex){
//				if(filePath.indexOf("fakepath")>-1){
//					act.setOutData("error", "浏览器设置出错</br>"+"请点击工具 -> Internet选项 -> 安全 </br>-> 自定义级别 ->其他 </br>启用：将文件上载到服务器时包含本地目录路径");
//					throw ex;
//				}
				act.setOutData("error", "读取文件出错!");
				throw ex;
			}
			logger.info("--------importExcel start");
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet=null;
			for(int m=0;m<wb.getSheets().length;m++){
				sheet=wb.getSheet(m);
				if(sheet.getRows()<=0){
				    continue;
				}else{
					break;
				}
			}
			//Sheet sheet = wb.getSheet(0);//获取第一页
			for(int j=1;j<sheet.getRows();j++){  //行
				logger.info("----j="+j);
				Cell[] cells = sheet.getRow(j);
				boolean chkFlag=true;
				//首先判断空值
				if(cells[0].getContents()==""){//日期空
					msgNullDate+=j+", ";
					continue;
				}
				if(cells[1].getContents()==""){//人员工号空
					msgNullPepNo+=j+",";
					continue;
				}
				if(cells[2].getContents()==""){//人员名称空
					msgNullPepName+=j+",";
					continue;
				}
				if(cells[3].getContents()==""){//类型空
					msgNullPeType+=j+",";
					continue;
				}
				System.out.println(cells[0].getContents().replace("/", "-"));
				Date dutyDate=DateUtil.str2Date(cells[0].getContents().replace("/", "-"),"-");
				Date date=new Date();
				Date nowDate=DateUtil.str2Date((date.getYear()+1900)+"-"+(date.getMonth()+1)+"-"+date.getDate(),"-");
				Long userId=dao.getUserId(cells[1].getContents().trim())==null?Long.parseLong(Constant.DEFAULT_VALUE.toString()):dao.getUserId(cells[1].getContents().trim());//根据工号获取信息
				Integer wtType=dao.getWtType(cells[3].getContents().trim());//查询是否有该班次类型
				
				
				
				Map<String,Object> map=dao.getIsSort(userId, cells[0].getContents().replace("/", "-").toString());//查看是否排版
				if(dutyDate.before(nowDate)){
					msgMinDateRow+=j+",";
					chkFlag=false;
				}
				if(userId==Long.parseLong(Constant.DEFAULT_VALUE.toString())){//防止后台报错，所以设置为一个默认值-1
					msgNotPepRow+=j+",";
					chkFlag=false;
				}
				if(wtType==null){
					msgClassTypeRow+=j+",";
					chkFlag=false;
				}
				if(map==null){
					msgPepFailRow+=j+",";
					chkFlag=false;
				}else{
					Integer res= Integer.parseInt(map.get("RES").toString());
					if(res>0){
						msgPepMoreRow+=j+", ";
						chkFlag=false;
					}
				}
				if(!chkFlag){
					continue;
				}
				TtCrmSortShiftPO po=new TtCrmSortShiftPO();
				SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			      
		      TtCrmWorktimePO crmWorktimePO = new TtCrmWorktimePO();
		      crmWorktimePO.setWtType(wtType);
		      crmWorktimePO = (TtCrmWorktimePO)dao.select(crmWorktimePO).get(0);
		      
		      if((""+wtType).equals("95501003") )
				{
		    	  po.setStaDate(dateFormat.parse( cells[0].getContents().replace("/", "-")+" "+crmWorktimePO.getWtStaOnMinute()+":"+crmWorktimePO.getWtStaOffMinute2()));
			      po.setEndDate(dateFormat.parse( getSpecifiedDayAfter(cells[0].getContents().replace("/", "-"))+" "+crmWorktimePO.getWtEndOnMinute()+":"+crmWorktimePO.getWtEndOffMinute()));
					
				}else
				{
					  po.setStaDate(dateFormat.parse( cells[0].getContents().replace("/", "-")+" "+crmWorktimePO.getWtStaOnMinute()+":"+crmWorktimePO.getWtStaOffMinute2()));
				      po.setEndDate(dateFormat.parse( cells[0].getContents().replace("/", "-")+" "+crmWorktimePO.getWtEndOnMinute()+":"+crmWorktimePO.getWtEndOffMinute()));
						
				}
				
		      
		    
				
				logger.info("-----userId="+userId);
				logger.info("-----wtType="+wtType);				
				logger.info("----dutyDate="+dutyDate+",nowDate="+nowDate);
				
				po.setSsId(Long.parseLong(SequenceManager.getSequence("")));
				po.setDutyDate(dutyDate);				
				po.setUserId(userId);				
				po.setSeAccount(cells[1].getContents().toString());
//				po.setSeName(cells[2].getContents().toString());
				po.setSeName(dao.getUserName(userId).get("SE_NAME").toString());
				po.setWtType(wtType);
				po.setSsBy(logonUser.getUserId());
				po.setSsDate(new Date());
				po.setStatus(Constant.STATUS_ENABLE);
				//logger.info("insert start");
				dao.insert(po);
				//logger.info("insert end");
			}
			String error_null1="第"+msgNullDate.substring(0,msgNullDate.length()>0?msgNullDate.length()-1:0)+"行值班日期为空!<br>";
			String error_null2="第"+msgNullPepNo.substring(0,msgNullPepNo.length()>0?msgNullPepNo.length()-1:0)+"行值班员工代码为空!<br>";
			String error_null3="第"+msgNullPepName.substring(0,msgNullPepName.length()>0?msgNullPepName.length()-1:0)+"行值班员工姓名为空!<br>";
			String error_null4="第"+msgNullPeType.substring(0,msgNullPeType.length()>0?msgNullPeType.length()-1:0)+"行班次类型为空!<br>";
			String error_01="第"+msgMinDateRow.substring(0,msgMinDateRow.length()>0?msgMinDateRow.length()-1:0)+"行值班日期小于当前日期!<br>";
			String error_02="第"+msgNotPepRow.substring(0,msgNotPepRow.length()>0?msgNotPepRow.length()-1:0)+"行没有匹配的值班员工!<br>";
			String error_03="第"+msgClassTypeRow.substring(0,msgClassTypeRow.length()>0?msgClassTypeRow.length()-1:0)+"行没有匹配的班次类型!<br>";
			String error_04="第"+msgPepFailRow.substring(0,msgPepFailRow.length()>0?msgPepFailRow.length()-1:0)+"行值班员工号错误!<br>";
			String error_05="第"+msgPepMoreRow.substring(0,msgPepMoreRow.length()>0?msgPepMoreRow.length()-1:0)+"行值班员工已经排班!<br>";
			msgResult=(msgNullDate==""?"":error_null1)
			+(msgNullPepNo==""?"":error_null2)
			+(msgNullPepName==""?"":error_null3)
			+(msgNullPeType==""?"":error_null4)
			+(msgMinDateRow==""?"":error_01)
			+(msgNotPepRow==""?"":error_02)
			+(msgClassTypeRow==""?"":error_03)
			+(msgPepFailRow==""?"":error_04)
			+(msgPepMoreRow==""?"":error_05);
			logger.info("--------msg="+msgResult);
			String selectBox=TC_CodeAddUtil.genSelBoxExp(Constant.STATUS,"STATUS");
			act.setOutData("selectBox", selectBox);
			act.setOutData("msg", msgResult);
//			act.setOutData("msg","document.write(alert("+msg+"));");
			act.setForword(CRMSortMainInit);			
		}catch (Exception e) {
			e.printStackTrace();
				BizException e1 = null;
			if(e instanceof BizException){
				e1 = (BizException)e;
			}else{
				//new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,act.getOutData("error")==null?"文件读取错误":act.getOutData("error").toString());
				e1=new BizException(act, ErrorCodeConstant.SPECIAL_MEG, 2, e.getMessage());
			}
			logger.error(logonUser,e1);
			act.setException(e1);
		}finally{
			if(null!=is){
				try{
					is.close();
				}catch(Exception ex){
					BizException e1 = new BizException(act,ex,ErrorCodeConstant.SPECIAL_MEG,"文件流关闭错误!");
					logger.error(logonUser,e1);
					act.setException(e1);
				}
			}
		}
	}
    
    
    
    public void readExcel01(){
    	ByteArrayInputStream is = null;
		try {
			act = ActionContext.getContext();
			OutputStream os = null;
			RequestWrapper request = act.getRequest();
			FileObject uploadFile = request.getParamObject("uploadFile");
			try{
				ResponseWrapper response = act.getResponse();
	 			// 导出的文件名
	 			String fileName = "结算数量金额明细表.xls";
	 			// 导出的文字编码客户关怀活动数据明细表
	 			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
	 			response.setContentType("Application/text/csv");
	 			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
	 			List<Object> listTemp = new LinkedList<Object>();
	 			List<List<Object>> list = new LinkedList<List<Object>>();
	 			listTemp.add("车系");
	 			listTemp.add("配件代码");
	 			listTemp.add("配件名称");
	 			listTemp.add("故障件名称");
	 			listTemp.add("供应商");
	 			listTemp.add("更换件数量");
	 			listTemp.add("结算金额");
	 			list.add(listTemp);
				 is = new ByteArrayInputStream(uploadFile.getContent());
				 Workbook wb = Workbook.getWorkbook(is);
					Sheet sheet=null;
					for(int m=0;m<wb.getSheets().length;m++){
						sheet=wb.getSheet(m);
						int count = sheet.getRows();
						for(int i = 1 ;i < count; i++)
						{
							Cell[] cells = sheet.getRow(i);
							List<Object> listValue = new LinkedList<Object>();
							listValue.add(cells[0].getContents().trim());
							listValue.add(cells[1].getContents().trim());
							listValue.add(cells[2].getContents().trim());
							
							String guans = cells[3].getContents().trim();
							String[] guan = guans.split(",");
							if(guan.length == 0)
							{
								listValue.add(guans);
							}else
							{
								String time = "";
								for(int k = 0 ;k < guan.length;k++ )
								{
									if(guan.length == k +1)
									{
										time = time +  guan[k];
									}else
									{
										if(guan[k].split("--")[0].endsWith(guan[k+1].split("--")[0]))
										{
											int y = Integer.parseInt(guan[k].split("--")[1]) + Integer.parseInt(guan[k+1].split("--")[1]);
											guan[k+1] = guan[k].split("--")[0] +"--"+y; 
										}else
										{
											time = time + guan[k]+"  ";
										}
									}
									
								}
								listValue.add(time);
							}
							
							
							String dels = cells[4].getContents().trim();
							String[] del = dels.split(",");
							if(del.length == 0)
							{
								listValue.add(del);
							}else
							{
							  
								HashSet<String> set = new HashSet<String>();
								for(int p = 0 ;p < del.length; p++)
								{
									set.add(del[p].split("--")[0]);
								}
								Iterator it = set.iterator();
								String time = "";
								while(it.hasNext())
								{
									String name = it.next().toString();
									int con = 0;
									for(int k = 0 ;k < del.length;k++ )
									{
										if(name.equals(del[k].split("--")[0]))
										{
											con = con + Integer.parseInt(del[k].split("--")[1]);
										}
									}
									time = time + name+"--"+con+"  ";
								}
								
								listValue.add(time);
							}
							listValue.add(cells[5].getContents().trim());
							listValue.add(cells[6].getContents().trim());
							list.add(listValue);
						}
						
					}
					os = response.getOutputStream();
		 			CsvWriterUtil.createXlsFileToName(list, os,"结算数量金额明细表");
		 			os.flush();	
			}catch(Exception ex){
				act.setOutData("error", "读取文件出错!");
				throw ex;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
    /*
	public void importExcel(InputStream is)throws Exception{
		String msg="";
		try{
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);//获取第一页
			for(int j=1;j<sheet.getRows();j++){  //行
				logger.info("----j="+j);
				Cell[] cells = sheet.getRow(j);
				boolean chkFlag=true;
				Date dutyDate=DateUtil.str2Date("20"+cells[0].getContents(),"-");
				Date nowDate=new Date();
				Long userId=dao.getUserId(cells[1].getContents());
				Integer wtType=dao.getWtType(cells[3].getContents());
				logger.info("-----userId="+userId);
				logger.info("-----wtType="+wtType);				
				logger.info("----dutyDate="+dutyDate+",nowDate="+nowDate);
				if(dutyDate.before(nowDate)){
					msg+="第"+j+"行值班日期小于当前日期,不予导入!";
					chkFlag=false;
				}
				if(userId==null){
					msg+="第"+j+"行没有匹配的值班员工,不予导入!";
					chkFlag=false;
				}
				if(wtType==null){
					msg+="第"+j+"行没有匹配的班次类型,不予导入!";
					chkFlag=false;
				}
				if(!chkFlag){
					continue;
				}
				TtCrmSortShiftPO po=new TtCrmSortShiftPO();
				po.setSsId(Long.parseLong(SequenceManager.getSequence("")));
				po.setDutyDate(dutyDate);				
				po.setUserId(userId);				
				po.setSeAccount(cells[1].toString());
				po.setSeName(cells[2].toString());
				po.setWtType(wtType);
				po.setSsBy(logonUser.getUserId());
				po.setSsDate(new Date());
				po.setStatus(Constant.STATUS_ENABLE);
				logger.info("insert start");
				dao.insert(po);
				logger.info("insert end");
			}
			if(msg!=""){
				BizException e1 = new BizException(act, ErrorCodeConstant.UPDATE_FAILURE_CODE, msg);
				logger.error(logonUser, e1);
				act.setException(e1);
			}else{
				msg="保存成功！";
			}						
		}catch(Exception ex){
			throw ex;
		}finally{
			logger.info("---------msg="+msg);
			act.setOutData("msg", msg);
		}
	}
	*/
	 
}
