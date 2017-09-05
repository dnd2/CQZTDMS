package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.baseSetting.seatsSet;
import com.infodms.dms.actions.customerRelationships.baseSetting.typeSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.CallRecordDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.customerRelationships.IncomingAlertScreenDao;
import com.infodms.dms.dao.customerRelationships.SeatsTeamSetDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmComplaintDealRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 咨询查询ACTIONS
 * @ClassName     : ConsultSearch 
 * @Description   : 咨询查询
 * @author        : wangming
 * CreateDate     : 2013-5-6
 */
public class ConsultSearch {
	private static Logger logger = Logger.getLogger(ConsultSearch.class);
	//咨询查询页面
	private final String ConsultSearch = "/jsp/customerRelationships/complaintConsult/consultSearch.jsp";
	//咨询查看页面
	private final String ConsultSearchWacth = "/jsp/customerRelationships/complaintConsult/consultSearchWatch.jsp";
	//咨询处理页面
	private final String ConsultSearchUpdate = "/jsp/customerRelationships/complaintConsult/consultSearchUpdate.jsp";
	//咨询编辑页面
	private final String ConsultSearchEdit = "/jsp/customerRelationships/complaintConsult/consultSearchEdit.jsp";

	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	//咨询查询初始化
	public void consultSearchInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			
			//咨询类型
			typeSet ts = new typeSet();
			act.setOutData("bclist", ts.getTypeSelect(Constant.TYPE_CONSULT));
			act.setForword(ConsultSearch);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"咨询查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryConsultSearch(){
		act.getResponse().setContentType("application/json");
		try{
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  				
			String type = CommonUtils.checkNull(request.getParamValue("type"));  				
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String cont = CommonUtils.checkNull(request.getParamValue("cont"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			
			seatsSet seat = new seatsSet();
			boolean isAdmin = seat.isAdmin(logonUser.getUserId());
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> complaintAcceptData = dao.queryConsultInfo(vin,name,tele,status,type,pro,dealName,cont,dateStart,dateEnd,isAdmin,logonUser.getUserId(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", complaintAcceptData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"咨询查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void consultSearchWatch(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("id"));  
			String openPage = CommonUtils.checkNull(request.getParamValue("openPage"));  
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			Map<String,Object> complaintAcceptMap = dao.queryConsultInfoWatch(Long.parseLong(cpid));
			
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			act.setOutData("openPage", openPage);
			act.setForword(ConsultSearchWacth);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"咨询查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void consultSearchUpdate(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("id")); 
			String openPage = CommonUtils.checkNull(request.getParamValue("openPage"));  
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			Map<String,Object> complaintAcceptMap = dao.queryConsultInfo(Long.parseLong(cpid));
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//内容类型
			act.setOutData("bclist", commonUtilActions.getTcCode(Constant.TYPE_CONSULT));
			//抱怨对象
			act.setOutData("cpObjectList", commonUtilActions.getVehicleCompany());
			//处理方式
			List<Map<String, Object>> dealModelLists = commonUtilActions.getTcCode(Constant.CONSULT_PROCESS.toString());
			List<Map<String, Object>> dealModelList = new ArrayList<Map<String,Object>>();
			for(Map<String, Object> map : dealModelLists){
				if(!map.get("CODE_ID").equals(Constant.CONSULT_PROCESS_SPOT.toString())){
					dealModelList.add(map);
				}
			}
			act.setOutData("dealModelList", dealModelList);
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			act.setOutData("dealRecordList", dao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			act.setOutData("openPage", openPage);
			act.setForword(ConsultSearchUpdate);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void consultSearchEdit(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("id"));  				
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			Map<String,Object> complaintAcceptMap = dao.queryConsultInfo(Long.parseLong(cpid));
			String openPage = CommonUtils.checkNull(request.getParamValue("openPage"));  
			
			SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm");			 
			//重置内容
			complaintAcceptMap.put("CPCONT", complaintAcceptMap.get("CPCONT").toString()+"  "+formate.format(new Date()));
			
			//业务类型
			act.setOutData("bizTypeList", getBizType());
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//内容类型
			act.setOutData("bclist", commonUtilActions.getTcCode(Constant.TYPE_CONSULT));
			//抱怨对象
			act.setOutData("cpObjectList", commonUtilActions.getVehicleCompany());
			//处理方式
			List<Map<String, Object>> dealModelLists = commonUtilActions.getTcCode(Constant.CONSULT_PROCESS.toString());
			List<Map<String, Object>> dealModelList = new ArrayList<Map<String,Object>>();
			for(Map<String, Object> map : dealModelLists){
				if(!map.get("CODE_ID").equals(Constant.CONSULT_PROCESS_SPOT.toString())){
					dealModelList.add(map);
				}
			}
			act.setOutData("dealModelList", dealModelList);
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//小区
			act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			act.setOutData("dealRecordList", dao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("complaintAcceptMap", complaintAcceptMap);
			act.setOutData("openPage", openPage);
			act.setForword(ConsultSearchEdit);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void consultSearchUpdateSubmit(){
		act.getResponse().setContentType("application/json");
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
					
			String bizType = CommonUtils.checkNull(request.getParamValue("bizType"));  				
			String contType = CommonUtils.checkNull(request.getParamValue("contType"));  				
			String cplevel = CommonUtils.checkNull(request.getParamValue("cplevel"));  	
			String cplimit = CommonUtils.checkNull(request.getParamValue("cplimit"));  				
			String cpObject = CommonUtils.checkNull(request.getParamValue("cpObject"));  	
			String vehicleCompany = CommonUtils.checkNull(request.getParamValue("vehicleCompany"));  
			String complaintContent = CommonUtils.checkNull(request.getParamValue("complaintContent"));  				
			String dealModel = CommonUtils.checkNull(request.getParamValue("dealModel"));  				
			String rcont = CommonUtils.checkNull(request.getParamValue("rcont")); 
			String dealuser = CommonUtils.checkNull(request.getParamValue("dealuser"));  				
						
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			//更新投诉咨询相关内容
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			ttCrmComplaintPO2.setCpBizType(Integer.parseInt(bizType));
			ttCrmComplaintPO2.setCpBizContent(Integer.parseInt(contType));
			if(!"".equals(cplevel)) ttCrmComplaintPO2.setCpLevel(Integer.parseInt(cplevel));
			if(!"".equals(cplimit)&&cplimit.equals(Constant.RULE_ONE_DAY.toString())){
				ttCrmComplaintPO2.setCpLimit(1);
			}else if(!"".equals(cplimit)&&cplimit.equals(Constant.RULE_THREE_DAY.toString())){
				ttCrmComplaintPO2.setCpLimit(3);
			}else if(!"".equals(cplimit)&&cplimit.equals(Constant.RULE_SEVEN_DAY.toString())){
				ttCrmComplaintPO2.setCpLimit(7);
			}
			if(!"".equals(cpObject))ttCrmComplaintPO2.setCpObject(Long.parseLong(cpObject));
			if(!"".equals(vehicleCompany))ttCrmComplaintPO2.setCpObject(Long.parseLong(vehicleCompany));
			if(!"".equals(dealuser))ttCrmComplaintPO2.setCpObject(Long.parseLong(dealuser));
			ttCrmComplaintPO2.setCpContent(complaintContent);
			ttCrmComplaintPO2.setCpDealMode(Integer.parseInt(dealModel));
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			//插入投诉咨询回复记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpContent(rcont);
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			if(bizType.equals(Constant.TYPE_COMPLAIN)){
				//投诉
				if(dealModel.equals(Constant.COMPLAINT_PROCESS_TURN.toString())){
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT));
					ttCrmComplaintPO2.setCpDealUser(Long.parseLong(dealuser));
				}
			}else if(bizType.equals(Constant.TYPE_CONSULT)){
				//咨询
				//已处理
				if(dealModel.equals(Constant.CONSULT_PROCESS_FINISH.toString())){
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
					ttCrmComplaintPO2.setCpDealUser(logonUser.getUserId());
					
					ttCrmComplaintDealRecordPO.setCpContent(rcont);
					ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_FINISH));
				//待处理
				}else if(dealModel.equals(Constant.CONSULT_PROCESS_WAIT.toString())){
					ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
					ttCrmComplaintPO2.setCpDealUser(logonUser.getUserId());
					ttCrmComplaintDealRecordPO.setCpContent(rcont);
					ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.CONSULT_STATUS_WAIT));
				}
			}
			
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void consultSearchEditSubmit(){
		act.getResponse().setContentType("application/json");
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
					
			String ctmname = CommonUtils.checkNull(request.getParamValue("ctmname"));  				
			String phone = CommonUtils.checkNull(request.getParamValue("phone"));  	
			String complaintContent = CommonUtils.checkNull(request.getParamValue("complaintContent"));  	
			String rcont = CommonUtils.checkNull(request.getParamValue("rcont"));  
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			ttCrmComplaintPO2.setCpName(ctmname);
			ttCrmComplaintPO2.setCpPhone(phone);
			ttCrmComplaintPO2.setCpContent(complaintContent);
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			dao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			ttCrmComplaintDealRecordPO.setCpContent(rcont);
			dao.insert(ttCrmComplaintDealRecordPO);
			act.setOutData("success", "true");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理咨询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//业务类型 只处理咨询
	private List<Map<String,Object>> getBizType(){
		List<Map<String,Object>> bizTypeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("id", Constant.TYPE_COMPLAIN);
//		map.put("name", Constant.TYPE_COMPLAIN_NAME);
//		bizTypeList.add(map);
//		map = new HashMap<String, Object>();
		map.put("id", Constant.TYPE_CONSULT);
		map.put("name", Constant.TYPE_CONSULT_NAME);
		bizTypeList.add(map);
		return bizTypeList;
	}

	public void consultSearchDownExcel(){
		try{
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  				
			String type = CommonUtils.checkNull(request.getParamValue("type"));  				
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String cont = CommonUtils.checkNull(request.getParamValue("cont"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				

			seatsSet seat = new seatsSet();
			boolean isAdmin = seat.isAdmin(logonUser.getUserId());
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
				
			List<Map<String, Object>> complaintAcceptData = dao.queryConsultInfo(vin,name,tele,status,type,pro,dealName,cont,dateStart,dateEnd,isAdmin,logonUser.getUserId());
			
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			
			//咨询类型
			typeSet ts = new typeSet();
			act.setOutData("bclist", ts.getTypeSelect(Constant.TYPE_CONSULT));
			
			if(complaintAcceptData!=null && complaintAcceptData.size()>0){
				act.setOutData("success", "true");
			}else {
				act.setOutData("noresult", "true");
			}	
			complaintAcceptDataToExcel(complaintAcceptData);
		//	act.setForword(ConsultSearch);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void complaintAcceptDataToExcel(List<Map<String, Object>> list) throws Exception{
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		String[] head=new String[8];
		head[0]="客户姓名";
		head[1]="联系电话";
		head[2]="受理日期";
		head[3]="受理人";
		head[4]="状态";
		head[5]="咨询内容";
		head[6]="回复内容";
		head[7]="业务类型";
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[8];
					detail[0] = CommonUtils.checkNull(map.get("CTMNAME"));
					detail[1] = CommonUtils.checkNull(map.get("PHONE"));
					detail[2] = CommonUtils.checkNull(map.get("ACCDATE"));
					detail[3] = CommonUtils.checkNull(map.get("ACCUSER"));
					detail[4] = CommonUtils.checkNull(map.get("STATUS_NAME"));
					detail[5] = CommonUtils.checkNull(map.get("CONTENT"));
					detail[6] = CommonUtils.checkNull(map.get("CPCONTENT"));
					detail[7] = CommonUtils.checkNull(map.get("BIZTYPE"));
					list1.add(detail);
		    	}
		    }
			
	    }else{
	    	String[]detail=new String[8];
			detail[0] ="";
			detail[1] ="";
			detail[2] ="";
			detail[3] ="";
			detail[4] ="";
			detail[5] ="";
			detail[6] ="";
			detail[7] ="";
			list1.add(detail);
	    }
	    this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "咨询查询清单.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("咨询查询清单", 0);

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
