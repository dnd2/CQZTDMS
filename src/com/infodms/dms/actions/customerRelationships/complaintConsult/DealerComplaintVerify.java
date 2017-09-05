package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.baseSetting.typeSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.customerRelationships.ComplaintConsultDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmComplaintDelayRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 经销商投诉处理审核ACTIONS
 * @ClassName     : dealerComplaintVerify
 * @Description   : 经销商投诉处理审核
 * @author        : wangming
 * CreateDate     : 2013-5-14
 */
public class DealerComplaintVerify {
	private static Logger logger = Logger.getLogger(DealerComplaintVerify.class);
	//经销商投诉处理审核初始化
	private final String DealerComplaintVerify = "/jsp/customerRelationships/complaintConsult/dealerComplaintVerify.jsp";
	//经销商延期大区审核界面
	private final String VerifyDelay = "/jsp/customerRelationships/complaintConsult/dealerComplaintVerifyDelay.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	//经销商投诉处理审核初始化
	public void dealerComplaintVerifyInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//报怨类型
			typeSet ts = new typeSet();
			act.setOutData("bclist", ts.getTypeSelect(Constant.TYPE_COMPLAIN));
			
			act.setForword(DealerComplaintVerify);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"经销商投诉处理审核初始化查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryDealerComplaintVerify(){
		act.getResponse().setContentType("application/json");
		try{			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	
			String dealUser = CommonUtils.checkNull(request.getParamValue("dealUser"));  	
			String accUser = CommonUtils.checkNull(request.getParamValue("accUser"));  	
			String biztype = CommonUtils.checkNull(request.getParamValue("biztype"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));
			String region = CommonUtils.checkNull(request.getParamValue("region"));  	
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String checkStart = CommonUtils.checkNull(request.getParamValue("checkStart"));  	
			String checklEnd = CommonUtils.checkNull(request.getParamValue("checklEnd"));  				
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			//查询用户属于哪个大区
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			List<Map<String, Object>> userOrgList = commonUtilDao.getUserOrg(logonUser.getUserId());
			if(userOrgList!=null&&userOrgList.size()>0){
				String orgids = "";
				for(Map<String, Object> mapOrg :userOrgList){
					if(orgids == "")
					orgids = ((BigDecimal)mapOrg.get("ORGID")).toString();
					else 
					orgids = orgids +"','"+((BigDecimal)mapOrg.get("ORGID")).toString();
				}
				
				PageResult<Map<String,Object>> complaintAcceptData = dao.queryDealerComplaintInfo(vin,name,tele,level,dealUser,accUser,biztype,
						status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,orgids,Constant.COMPLAINT_STATUS_DOING+"','"+Constant.COMPLAINT_STATUS_DOING_REJECT,Constant.PAGE_SIZE,curPage);
				
				act.setOutData("ps", complaintAcceptData);
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商投诉延期审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//经销商延期大区审核 界面
	public void verifyDelay(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  	
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  	
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
			Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
			act.setOutData("complaintConsult", complaintConsult);
			act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
			act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
			
			//交给处级部门
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			act.setOutData("orgList", commonUtilActions.getOfficeOrg());
			
			List<Map<String, Object>> stautsList = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "通过");
			map.put("value", "1");
			stautsList.add(map);
			map = new HashMap<String, Object>();
			map.put("name", "驳回");
			map.put("value", "0");
			stautsList.add(map);
			act.setOutData("stautsList",stautsList);
			
			act.setForword(VerifyDelay);			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商延期申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//经销商延期大区审核提交
	public void verifyDelaySubmit(){
		try{			
			act.getResponse().setContentType("application/json");
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String verifystatus = CommonUtils.checkNull(request.getParamValue("verifystatus")); 
			String orgObj = CommonUtils.checkNull(request.getParamValue("orgObj")); 
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));  
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			//List<Map<String,Object>>  org = commonUtilDao.getUserOrgForAdminDepart(logonUser.getUserId());
			//Map map = org.get(0);
			//审核状态
			int status = 0;
			if(verifystatus.equals("0")) status = Constant.REJECT_APPLY;
			else if(verifystatus.equals("1")) status = Constant.PASS_APPLY;
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)complaintAcceptDao.select(ttCrmComplaintPO1).get(0);

			//查询出为申请状态的申请记录
			TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO  = complaintAcceptDao.queryApplayDelay(Long.parseLong(cpid), Constant.ALREADY_APPLY.toString(), String.valueOf(0));
			
			//TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO2 = new TtCrmComplaintDelayRecordPO();
			//ttCrmComplaintDelayRecordPO2.setClId(ttCrmComplaintDelayRecordPO.getClId());
			TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO1 = new TtCrmComplaintDelayRecordPO();
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			ttCrmComplaintDelayRecordPO1 = commonUtilActions.copyTtCrmComplaintDelayRecordPO(ttCrmComplaintDelayRecordPO,ttCrmComplaintDelayRecordPO1);
			
			ttCrmComplaintDelayRecordPO1.setClId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDelayRecordPO1.setClVerifyContent(ccont);
			ttCrmComplaintDelayRecordPO1.setClVerifyDate(new Date());
			ttCrmComplaintDelayRecordPO1.setClVerifyUser(logonUser.getName());
			ttCrmComplaintDelayRecordPO1.setClVerifyUserId(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO1.setCpDeferType(Constant.DEFER_TYPE_01.longValue());
			ttCrmComplaintDelayRecordPO1.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO1.setUpdateDate(new Date());
			ttCrmComplaintDelayRecordPO1.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDelayRecordPO1.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDelayRecordPO1.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO1.setCreateDate(new Date());

			
			ttCrmComplaintPO2.setCpClOnceDate(null);
			if(status == Constant.PASS_APPLY){
				ttCrmComplaintPO2.setCpClTurnOrg(Long.parseLong(orgObj));
				ttCrmComplaintPO2.setCpClApplyDate(ttCrmComplaintDelayRecordPO.getClDate());
				ttCrmComplaintDelayRecordPO1.setClVerifyStatus(status);
				
				
				TtCrmComplaintDelayRecordPO  ttCrmComplaintDelayRecordPOs = new TtCrmComplaintDelayRecordPO();
				ttCrmComplaintDelayRecordPOs.setClContent(ttCrmComplaintDelayRecordPO.getClContent());
				ttCrmComplaintDelayRecordPOs.setClDate(ttCrmComplaintDelayRecordPO.getClDate());
				ttCrmComplaintDelayRecordPOs.setClId(new Long(SequenceManager.getSequence("")));
				ttCrmComplaintDelayRecordPOs.setClUser(logonUser.getName());
				ttCrmComplaintDelayRecordPOs.setClUserId(logonUser.getUserId());
				ttCrmComplaintDelayRecordPOs.setCpId(Long.parseLong(cpid));
				ttCrmComplaintDelayRecordPOs.setClVerifyStatus(Constant.ALREADY_APPLY);
				
				ttCrmComplaintDelayRecordPOs.setApplyOrg(logonUser.getOrgId());
				ttCrmComplaintDelayRecordPOs.setApplyType(1);
				ttCrmComplaintDelayRecordPOs.setCpDeferType(Constant.DEFER_TYPE_01.longValue());
				ttCrmComplaintDelayRecordPOs.setCreateBy(logonUser.getUserId());
				ttCrmComplaintDelayRecordPOs.setCreateDate(new Date());
				ttCrmComplaintDelayRecordPOs.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
				ttCrmComplaintDelayRecordPOs.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());
				ttCrmComplaintDelayRecordPOs.setCpNextDealId(Long.parseLong(orgObj));
				complaintAcceptDao.insert(ttCrmComplaintDelayRecordPOs);
				
			}else{
				ttCrmComplaintPO2.setCpDealUser(ttCrmComplaintDelayRecordPO.getApplyOrg());
				ttCrmComplaintPO2.setCpClStatus(status);
				ttCrmComplaintDelayRecordPO1.setClVerifyStatus(Constant.REJECT_APPLY);
				ttCrmComplaintDelayRecordPO1.setCpNextDealId(ttCrmComplaintPO3.getCpDealDealer());
				//ttCrmComplaintDelayRecordPOs.setCpNextDealId(Long.parseLong(orgObj));
			}
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			complaintAcceptDao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			complaintAcceptDao.insert(ttCrmComplaintDelayRecordPO1);
			//complaintAcceptDao.update(ttCrmComplaintDelayRecordPO2, ttCrmComplaintDelayRecordPO1);
			act.setOutData("isSuccess", "true");		
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商延期申请审核保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void dealerComplaintVerifyDownExcel(){
		try{
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	
			String dealUser = CommonUtils.checkNull(request.getParamValue("dealUser"));  	
			String accUser = CommonUtils.checkNull(request.getParamValue("accUser"));  	
			String biztype = CommonUtils.checkNull(request.getParamValue("biztype"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));
			String region = CommonUtils.checkNull(request.getParamValue("region"));  	
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String checkStart = CommonUtils.checkNull(request.getParamValue("checkStart"));  	
			String checklEnd = CommonUtils.checkNull(request.getParamValue("checklEnd"));  			

			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			//查询用户属于哪个大区
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			List<Map<String, Object>> userOrgList = commonUtilDao.getUserOrg(logonUser.getUserId());
			if(userOrgList!=null&&userOrgList.size()>0){
				String orgids = "";
				for(Map<String, Object> mapOrg :userOrgList){
					if(orgids == "")
					orgids = ((BigDecimal)mapOrg.get("ORGID")).toString();
					else 
					orgids = orgids +"','"+((BigDecimal)mapOrg.get("ORGID")).toString();
				}
				List<Map<String, Object>> complaintAcceptData =  dao.queryDealerComplaintInfoList(vin,name,tele,level,dealUser,accUser,biztype,
						status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,orgids,Constant.COMPLAINT_STATUS_DOING+"','"+Constant.COMPLAINT_STATUS_DOING_REJECT);
					complaintAcceptDataToExcel(complaintAcceptData);
					
			}else{
				act.setOutData("noresult", "true");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void complaintAcceptDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[37];
		head[0]="延期";
		head[1]="抱怨单编号";
		head[2]="抱怨级别";
		head[3]="抱怨类别";
		head[4]="省市";
		head[5]="地市";
		head[6]="客户姓名";
		head[7]="联系电话";
		head[8]="抱怨内容";
		head[9]="抱怨对象";
		head[10]="抱怨时间";
		head[11]="受理人";
		head[12]="车种";
		head[13]="VIN号";
		head[14]="购车时间";
		head[15]="行驶里程";
		head[16]="里程范围";
		head[17]="处理部门";
		head[18]="处理人";
		head[19]="签收日期";
		head[20]="转出时间";
		head[21]="回访结果";
		head[22]="回访人";
		head[23]="回访日期";
		head[24]="关闭时间";
		head[25]="处理时长(小时)";
		head[26]="规定处理期限(天)";
		head[27]="延期天数(天)";
		head[28]="规定及时关闭时间";
		head[29]="是否及时关闭";
		head[30]="是否一次处理满意";
		head[31]="是否正常关闭";
		head[32]="处理过程";
		head[33]="最终反馈日期";
		head[34]="处理结果";
		head[35]="状态";
		head[36]="当前处理人";
		
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		
		List<Map<String, Object>> cpns = tcCodeDao.getTcCodesByType(Constant.COMPLAINT_STATUS.toString());
		List<Map<String, Object>> cpis = tcCodeDao.getTcCodesByType(Constant.GRADE_TYPE);
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[37];
					detail[0] = CommonUtils.checkNull(map.get("CPNO"));
					detail[1] = CommonUtils.checkNull(map.get("CPNO"));
					detail[2] = CommonUtils.checkNull(map.get("CPLEVEL"));
					detail[3] = CommonUtils.checkNull(map.get("BIZCONT"));
					detail[4] = CommonUtils.checkNull(map.get("PRO"));
					detail[5] = CommonUtils.checkNull(map.get("CITY"));
					detail[6] = CommonUtils.checkNull(map.get("CTMNAME"));
					detail[7] = CommonUtils.checkNull(map.get("PHONE"));
					detail[8] = CommonUtils.checkNull(map.get("CPCONT"));
					detail[9] = CommonUtils.checkNull(map.get("CPOBJECT"));
					detail[10] = CommonUtils.checkNull(map.get("CREATEDATE"));
					detail[11] = CommonUtils.checkNull(map.get("ACUSER"));
					detail[12] = CommonUtils.checkNull(map.get("SNAME"));
					detail[13] = CommonUtils.checkNull(map.get("VIN"));
					detail[14] = CommonUtils.checkNull(map.get("BDATE"));
					detail[15] = CommonUtils.checkNull(map.get("CPMILEAGE"));
					detail[16] = CommonUtils.checkNull(map.get("MILEAGERANGE"));
					detail[17] = CommonUtils.checkNull(map.get("ORGNAME"));
					detail[18] = CommonUtils.checkNull(map.get("DEALUSER"));
					detail[19] = CommonUtils.checkNull(map.get("CPACCDATE"));
					detail[20] = CommonUtils.checkNull(map.get("TURNDATE"));
					detail[21] = CommonUtils.checkNull(map.get("CRCONET"));
					detail[22] = CommonUtils.checkNull(map.get("CUSER"));
					detail[23] = CommonUtils.checkNull(map.get("CDATE"));
					detail[24] = CommonUtils.checkNull(map.get("CPCDATE"));
					detail[25] = CommonUtils.checkNull(map.get("DEALTIME"));
					detail[26] = CommonUtils.checkNull(map.get("CPLIMIT"));
					detail[27] = CommonUtils.checkNull(map.get("DELAYDATE"));
					detail[28] = CommonUtils.checkNull(map.get("SHOULDCLOSETIME"));
					detail[29] = CommonUtils.checkNull(map.get("ISTIMELYCLOSE"));
					detail[30] = CommonUtils.checkNull(map.get("CPISONCESF"));
					detail[31] = CommonUtils.checkNull(map.get("ISNORMALCLOSE"));
					detail[32] = CommonUtils.checkNull(map.get("DEALCONTENT"));
					detail[33] = CommonUtils.checkNull(map.get("LASTDEALDATE"));
					detail[34] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CPSF")),cpis);
					detail[35] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("STATUS")),cpns);
					detail[36] = CommonUtils.checkNull(map.get("DUSER"));
					
					list1.add(detail);
		    	}
		    }
	    }
	    this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "抱怨查询清单.xls";
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
			if(list != null && list.size() > 0 )
			{
				int pageSize=list.size()/30000;
				for (int z = 1; z < list.size() + 1; z++) {
					String[] str = list.get(z - 1);
					for (int i = 0; i < str.length; i++) {
						ws.addCell(new Label(i, z, str[i]));
					}
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
