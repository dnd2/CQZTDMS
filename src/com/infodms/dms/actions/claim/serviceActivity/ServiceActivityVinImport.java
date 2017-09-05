/**********************************************************************
* <pre>
* FILE : ServiceActivityVinImport.java
* CLASS : ServiceActivityVinImport
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理--VIN导入清单
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-17| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityVinImport.java,v 1.8 2011/01/12 10:08:00 zuoxj Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.ExcelUtil;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityVinImportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityRelationCodePO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infodms.dms.po.TtAsRecallVehiclePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

/**
 * Function       :  服务活动管理---VIN导入清单
 * @author        :  PGM
 * CreateDate     :  2010-06-03
 * @version       :  0.1
 */

public class ServiceActivityVinImport extends BaseImport{
	private Logger logger = Logger.getLogger(ServiceActivityVinImport.class);
	private ServiceActivityVinImportDao dao = ServiceActivityVinImportDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityVinInitUrl = "/jsp/claim/serviceActivity/serviceActivityVinImport.jsp";//VIN导入清单页面
	private final String serviceActivityVinCodeImportInit = "/jsp/claim/serviceActivity/serviceActivityVinCodeImport.jsp";//VIN导入关系页面
	private final String ServiceActivityVinSuccessImportUrl = "/jsp/claim/serviceActivity/serviceActivityVinImportInfo.jsp";//VIN导入清单页面
	private final String ServiceActivityVinImportUrl = "/jsp/claim/serviceActivity/serviceActivityVinSuccess.jsp";//VIN导入清单页面
	private final String templateDownLoadInitUrl = "/jsp/systemMng/paraConfig/templateDownLoad.jsp";
	private final String ServiceActivityVinFailureUrl = "/jsp/claim/serviceActivity/serviceActivityVinFailure.jsp";//VIN导入清单失败页面
	private final String serviceActivityVinCodeFailureUrl = "/jsp/claim/serviceActivity/serviceActivityVinCodeFailure.jsp";//VIN导入清单失败页面
	
	/**
	 * Function       :  服务活动管理---VIN导入清单初始化
	 * @param         :  
	 * @return        :  serviceActivityVinImportInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public void serviceActivityVinImportInit(){
		try {
			RequestWrapper request = act.getRequest();
			String flag=request.getParamValue("flag");//修改
			String activityId=request.getParamValue("activityId");//活动ID
			request.setAttribute("activityId", activityId);
			request.setAttribute("flag", flag);
			act.setForword(ServiceActivityVinInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---VIN导入清单页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 打开导入关系的页面
	 */
	public void serviceActivityVinCodeImportInit(){
		try {
			RequestWrapper request = act.getRequest();
			String ActivityCode=request.getParamValue("ActivityCode");//ActivityCode
			request.setAttribute("ActivityCode", ActivityCode);
			act.setForword(serviceActivityVinCodeImportInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---VIN导入清单页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void serviceActivityVinjuede()
	{
		try {
			RequestWrapper request = act.getRequest();
			String activityId=request.getParamValue("activityId");//活动ID
			TtAsActivityVehiclePO vehiclePO = new TtAsActivityVehiclePO();
			vehiclePO.setActivityId(Long.parseLong(activityId));
			if(dao.select(vehiclePO).size() == 0)
			{
				act.setOutData("ret","false");
			}else
			{
				act.setOutData("ret","true");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---VIN导入清单页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * Function       :  服务活动管理---VIN导入清单
	 * @param         :  
	 * @return        :  serviceActivityVinImportOption
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public void serviceActivityVinImportOption(){
		try {
			//获得本地文件
			RequestWrapper request = act.getRequest();
			String flag=request.getParamValue("flag");//修改
			String activityId=request.getParamValue("activityId");//获得activityId  
			TtAsRecallVehiclePO VehiclePO = new TtAsRecallVehiclePO();
			if (activityId!=null&&!"".equals(activityId)) {
				VehiclePO.setActivityId(Long.parseLong(activityId));
			}
			dao.serviceActivityVinImportDelete(VehiclePO);//调用删除方法，在导入VIN清单之前，先清空临时表

			long maxSize=5000;
			int errNum=insertIntoTmp(request, "importFile",18,6,maxSize);
			if(errNum!=0){
				List<ExcelErrors> errorList=new ArrayList();
				ExcelErrors ees=new ExcelErrors();
				ees.setRowNum(0);
				switch (errNum) {
				case 1:
					ees.setErrorDesc("文件列数过多");
					break;
				case 2:
					ees.setErrorDesc("空行不能大于三行");
					break;
				case 3:
					ees.setErrorDesc("文件不能为空");
					break;
				case 4:
					ees.setErrorDesc("文件不能为空");
					break;
				case 5:
					ees.setErrorDesc("文件不能大于"+maxSize);
					break;
				case 6:
					ees.setErrorDesc("文件内容格式不正确");
					break;
				default:
					break;
				}
				errorList.add(ees);
				act.setOutData("errorList", errorList);
				request.setAttribute("flag", flag);
				request.setAttribute("activityId", activityId);
				if(1==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(2==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(3==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(4==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(5==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(6==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				else{
					act.setForword(ServiceActivityVinSuccessImportUrl);
				}
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTtAsRecallVehicle(list,activityId,flag);
			}
		}catch(Exception e)
		{   
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"VIN清单导入");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 	
	}
	/**
	 * 关系导入操作
	 */
	public void serviceActivityVinCodeImportOption(){
		try {
			//获得本地文件
			RequestWrapper request = act.getRequest();
			
			long maxSize=1024*1024*5;
			int errNum=insertIntoTmp(request, "importFile",18,6,maxSize);
			if(errNum!=0){
				List<ExcelErrors> errorList=new ArrayList();
				ExcelErrors ees=new ExcelErrors();
				ees.setRowNum(0);
				switch (errNum) {
				case 1:
					ees.setErrorDesc("文件列数过多");
					break;
				case 2:
					ees.setErrorDesc("空行不能大于三行");
					break;
				case 3:
					ees.setErrorDesc("文件不能为空");
					break;
				case 4:
					ees.setErrorDesc("文件不能为空");
					break;
				case 5:
					ees.setErrorDesc("文件不能大于"+maxSize);
					break;
				case 6:
					ees.setErrorDesc("文件内容格式不正确");
					break;
				default:
					break;
				}
				errorList.add(ees);
				act.setOutData("errorList", errorList);
				if(1==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(2==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(3==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(4==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(5==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				if(6==errNum){
					request.setAttribute("ees", ees);
					act.setForword(ServiceActivityVinFailureUrl);
				}
				
			}
			FileObject uploadFile = request.getParamObject("importFile");
			ExcelUtil t =new ExcelUtil();
			String result = t.readExcel(uploadFile);
			if("".equals(result)){
				act.setForword(serviceActivityVinCodeImportInit);
			}else{
				act.setForword(serviceActivityVinCodeFailureUrl);
			}
		}catch(Exception e)
		{   
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"VIN关系清单导入");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 	
	}
	/**
	 * 关系模板下载
	 */
	public void downFileUpload(){
			act = ActionContext.getContext() ;
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			try {
				//List<List<Object>> list = new ArrayList();
				String[] head = new String[3];
				head[0]="服务商代码";
				head[1]="VIN";
				head[2]="服务活动编码";
				/*List list1=new ArrayList();
				String[]detail=new String[1];
				detail[0]="LVFAB2ADXDG128922";
				list1.add(detail);*/
		    	BaseUtils.toExcel(response, request, head, null, "服务活动VIN经销商关系模板.xls", "sheet1",null);
		  
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---VIN关系清单页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	private boolean isExistdealer(ServiceActivityVinImportDao dao,String dealer) throws Exception{  //yh 2010.11.29
		boolean b = false;
		TmDealerPO tmp = new TmDealerPO();
		tmp.setDealerCode(dealer);
		List list = dao.select(tmp);
		if(list.size()>0){
	       b = true;
		}
		if(! Utility.testString(dealer))
		{
		   b = true;
		}
		return b;
	}
	
	/*
	 * 把所有导入记录插入Tt_As_Recall_Vehicle临时表
	 */
	private void insertTtAsRecallVehicle(List<Map> list,String activityId,String flag) throws Exception{
		RequestWrapper request = act.getRequest();
		boolean fl=true;
		StringBuffer sb=new StringBuffer();
		if(null==list){
			list=new ArrayList();
		}
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
				int vin =cells[0].getContents().length();
				if(vin >17 ){
					sb.append("VIN超过17位！");
					fl=false;
					break;
				}
				if(false == isExistVin(this.dao, cells[0].getContents())){					
					sb.append("VIN在系统中不存在！"+cells[0].getContents());
					fl=false;
					break;
				}
				
				if(cells.length >= 2  && cells[1] != null)
				{
					if(false == isExistdealer(this.dao, cells[1].getContents())){					
						sb.append("服务站在系统中不存在！"+cells[1].getContents());
						fl=false;
						break;
					}

				}
				
			}
					
		}
		if(fl==true){
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
					parseCells(key, cells,activityId,flag);
				}
			}
			request.setAttribute("activityId", activityId);
			request.setAttribute("flag", flag);
			act.setForword(ServiceActivityVinSuccessImportUrl);	
		}else{
			ExcelErrors ees=new ExcelErrors();
			request.setAttribute("ees", ees);
			request.setAttribute("sb", sb.toString());
			act.setForword(ServiceActivityVinFailureUrl);
		}
		
	}
	/*private void insertTtAsRecallVehicle(List<Map> list,String activityId,String flag) throws Exception{
		RequestWrapper request = act.getRequest();
		if(null==list){
			list=new ArrayList();
		}
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
				parseCells(key, cells,activityId,flag);
			}
		}
		request.setAttribute("activityId", activityId);
		request.setAttribute("flag", flag);
		act.setForword(ServiceActivityVinSuccessImportUrl);	
	}*/
	/*
	 * 每一行插入Tt_As_Recall_Vehicle临时表
	 * 数字只截取30位
	 */
	private void parseCells(String rowNum,Cell[] cells,String activityId,String flag) throws Exception{
		    TtAsRecallVehiclePO VehiclePO=new TtAsRecallVehiclePO();
		    TtAsRecallVehiclePO VehiclePO1=new TtAsRecallVehiclePO();
		    StringBuffer sb=new StringBuffer();
		    RequestWrapper request = act.getRequest();
		    if(cells[0].getContents().equals("")||cells[0].getContents()==null){
				sb.append("VIN为空！"+",");
			}else{
				VehiclePO.setVin(subCell(cells[0].getContents().trim()));//VIN
				VehiclePO1.setVin(subCell(cells[0].getContents().trim()));
			}
		    
		    
		    TtAsActivityPO activityPO = new TtAsActivityPO();
		    activityPO.setActivityId(Long.parseLong(activityId));
		    List<TtAsActivityPO> listp= dao.select(activityPO);
		    
		    
		    TtAsActivityRelationCodePO activityRelationCodePO1 = new TtAsActivityRelationCodePO();
		    activityRelationCodePO1.setActivityId(Long.parseLong(activityId));
		    activityRelationCodePO1.setVin(cells[0].getContents());
		   
		    
		    TtAsActivityRelationCodePO activityRelationCodePO = new TtAsActivityRelationCodePO();
		    
		    activityRelationCodePO.setActivityId(Long.parseLong(activityId));
		    activityRelationCodePO.setId(Long.parseLong(SequenceManager.getSequence("")));
		    activityRelationCodePO.setCreateDate(new Date());
		    activityRelationCodePO.setVin(cells[0].getContents());
		    if(cells.length >= 2 && cells[1] != null )
		    {
		    	activityRelationCodePO.setDealerCode(cells[1].getContents());
		    }
		    
		    activityRelationCodePO.setActivityCode(listp.get(0).getActivityCode());
		    if(dao.select(activityRelationCodePO1).size() == 0)
		    {
		    	 dao.insert(activityRelationCodePO);
				    
		    }
		   
		    
		    
		    
		   
		    VehiclePO.setId(Long.parseLong(SequenceManager.getSequence("")));//ID
		    VehiclePO.setActivityId(Long.parseLong(activityId));//活动ID
		    VehiclePO1.setActivityId(Long.parseLong(activityId));
		    VehiclePO.setCarStatus(Constant.SERVICEACTIVITY_CAR_STATUS_03);//尚未下发
		   //校验临时表数据VIN是否真实有效
			List vinlist= dao.VinVerificationTruth(subCell(cells[0].getContents().trim()));//VIN码验证真实性（VIN 不存在，更新flag为：1；0表示：真实有效数据；1 表示：非真实有效数据）
			if(vinlist.size()==0){
				sb.append("VIN不是真实有效"+",");
			}
			//验证VIN是否重复；
			List checkVinRepeat= dao.checkVinRepeat(activityId,subCell(cells[0].getContents().trim()));//验证VIN是否重复；如果重复页面提示：无法导入
			if(checkVinRepeat.size()>0){
				sb.append("VIN已经下发过，不能导入"+",");
				TtAsRecallVehiclePO RecallVehiclePO = new TtAsRecallVehiclePO();//修改条件
				RecallVehiclePO.setVin(subCell(cells[0].getContents().trim()));
				TtAsRecallVehiclePO VehiclePOContent = new TtAsRecallVehiclePO();//修改内容
				VehiclePOContent.setFlag(Long.parseLong("1"));
				dao.VinVerificationTruthOption(RecallVehiclePO,VehiclePOContent);//验证VIN是否真实(VIN不存在，更新flag 为1；0 表示：真是数据；1 表示：不存在数据)
			}
			String ss=null;
			if(sb.toString().lastIndexOf(",")!=-1){
				if(sb.toString().lastIndexOf(",")==sb.toString().length()-1){
					ss=sb.toString().substring(0, sb.toString().lastIndexOf(","));
				}else{
					ss=sb.toString();
				}
			}
			VehiclePO.setErrorRemark(ss); //错误原因
			
			if(Utility.testString(VehiclePO.getDealerCode())){
		    List<TmDealerPO> list= dao.serviceActivityVinDealerCode(VehiclePO); //根据dealerCode查询dealerId
		    if(list!=null){
		    	Iterator<TmDealerPO> it=list.iterator();
			    while(it.hasNext()){
			    	TmDealerPO DealerPO=(TmDealerPO) it.next();
			    	VehiclePO.setDealerId(DealerPO.getDealerId());
			    }
		    }
		}
			
			if(dao.select(VehiclePO1).size() == 0 )
			{
				  dao.serviceActivityVinImportAdd(VehiclePO); //调用新增导入方法 
			}
			  
		   
		    if(vinlist.size()==0){
				TtAsRecallVehiclePO RecallVehiclePO = new TtAsRecallVehiclePO();//修改条件
				RecallVehiclePO.setVin(subCell(cells[0].getContents().trim()));
				TtAsRecallVehiclePO VehiclePOContent = new TtAsRecallVehiclePO();//修改内容
				VehiclePOContent.setFlag(Long.parseLong("1"));
				dao.VinVerificationTruthOption(RecallVehiclePO,VehiclePOContent);//验证VIN是否真实(VIN不存在，更新flag 为1；0 表示：真是数据；1 表示：不存在数据)
			}
			       
	}
	/*
	 * 将输入字符截取最多30位
	 */
    private String subCell(String orgAmt){
		String newAmt="";
		if(null==orgAmt||"".equals(orgAmt)){
			return newAmt;
		}
		if(orgAmt.length()>30){
			newAmt=orgAmt.substring(0,30);
		}else{
			newAmt=orgAmt;
		}
		return newAmt;
	}
	/** 
	 * Function       :  服务活动管理---VIN导入清单查询
	 * @param         :  
	 * @return        :  serviceActivityVinImportQuery
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public void serviceActivityVinImportQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId=request.getParamValue("activityId");//获得activityId 
			TtAsRecallVehiclePO VehiclePO = new TtAsRecallVehiclePO();
			VehiclePO.setActivityId(Long.parseLong(activityId));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.serviceActivityVinImportQuery(VehiclePO,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityVinSuccessImportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"VIN导入清单查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	 * Function       :  服务活动管理---VIN清单导入到业务表[TT_AS_ACTIVITY_VEHICLE]中（如果数据表中存在则更新，不存在则新增）
	 * @param         :  
	 * @return        :  serviceActivityVinImportVehicle
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	@SuppressWarnings("unchecked")
	public void serviceActivityVinImportVehicle(){
		RequestWrapper request = act.getRequest();
		String flag=request.getParamValue("flag");//修改
		String activityId=request.getParamValue("activityId");//获得activityId 
		Long createBy=logonUser.getUserId();//创建人
		Long updateBy=logonUser.getUserId();//修改人
		if(!"".equals(activityId)&&null!=activityId){
		      dao.serviceActivityVinImportVehicle(activityId, createBy,updateBy);//功能：将所有正确的数据导入到业务表中[TT_AS_ACTIVITY_VEHICLE]
		}
		List<TtAsActivityVehiclePO> list=null;
		if(!"".equals(activityId)&&null!=activityId){
		     list=dao.QueryCarNum(activityId); ///功能：调用查询方法；描述：查询车辆数
		}
		int carNum=list.size();//车辆数
		TtAsActivityPO activityPO=new TtAsActivityPO();//条件
		activityPO.setActivityId(Long.parseLong(activityId));
		TtAsActivityPO activityPOContent=new TtAsActivityPO();//内容
		activityPOContent.setCarNum(carNum);
		if("againImport".equals(flag)){//againImport 为重新发布页面传值，需修改状态为：  已经发布
				activityPOContent.setStatus(Constant.SERVICEACTIVITY_STATUS_02);//活动状态为:已经发布
		}else{
			    activityPOContent.setStatus(Constant.SERVICEACTIVITY_STATUS_01);//活动状态为:尚未发布
		}
		dao.updateCarNumAndStatusOption(activityPO, activityPOContent);//功能：调用修改方法；描述：根据活动ID修改车辆数
		request.setAttribute("activityId", activityId);
		act.setForword(ServiceActivityVinImportUrl);
	}
	/** 
	 * Function       :  服务活动管理---VIN清单模板下载
	 * @param         :  ActionContext.getContext().getResponse()
	 * @return        :  serviceActivityVinDownLoad
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public void serviceActivityVinDownLoad(){
				String	filePath = "http://dms.changan.com.cn:9001/dms";
				String  fileName="/活动VIN导入清单模板.xls";
				String  old="活动VIN导入清单模板.xls";
				try {
					 Download.downloadTemplate(ActionContext.getContext().getResponse(), fileName, filePath, old);
				}catch (Exception e) {
					BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"VIN清单模板下载");
					logger.error(logonUser,e1);
					act.setException(e1);
				}
	}
	/** 
	 * Function       :  服务活动管理---VIN清单模板下载
	 * @return        :  templateDownLoadInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-23
	 */
	public void templateDownLoadInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		//TemplateDownLoadDao dao=TemplateDownLoadDao.getInstance();
		try {
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);      //公司ID
			List<Map<String, Object>> list=dao.selectTemplateParaConfig("20141008",companyId);
			act.setOutData("list", list);
			act.setForword(templateDownLoadInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	 * Function       : 查询VIN在Tm_vehicle表中是否存在
	 * @return        :  isExistVin
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-23
	 */
	@SuppressWarnings("unused")
	private boolean isExistVin(ServiceActivityVinImportDao dao,String vin) throws Exception{  //yh 2010.11.29
		boolean b = false;
		TmVehiclePO tmp = new TmVehiclePO();
		tmp.setVin(vin);
		/*****add by liuxh 20131108判断车架号不能为空*****/
		CommonUtils.jugeVinNull(vin);
		/*****add by liuxh 20131108判断车架号不能为空*****/
		List list = dao.select(tmp);
		if(list.size()>0){
	       b = true;
		}
		return b;
	}
}