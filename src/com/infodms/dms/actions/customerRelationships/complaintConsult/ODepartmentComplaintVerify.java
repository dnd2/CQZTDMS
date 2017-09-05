package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.infodms.dms.common.Utility;
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
 * 业务部门投诉处理审核ACTIONS
 * @ClassName     : ODepartmentComplaintDeal
 * @Description   : 业务部门投诉处理审核
 * @author        : wangming
 * CreateDate     : 2013-5-14
 */
public class ODepartmentComplaintVerify {
	private static Logger logger = Logger.getLogger(ODepartmentComplaintVerify.class);
	//业务部门投诉处理审核初始化
	private final String ODepartmentComplaintVerify = "/jsp/customerRelationships/complaintConsult/oDepartmentComplaintVerify.jsp";
	private final String VerifyDelay = "/jsp/customerRelationships/complaintConsult/oDepartmentComplaintVerifyDelay.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	//业务部门投诉处理审核初始化
	public void oDepartmentComplaintVerifyInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//报怨类型
			typeSet ts = new typeSet();
			act.setOutData("bclist", ts.getTypeSelect(Constant.TYPE_COMPLAIN));
			
			act.setForword(ODepartmentComplaintVerify);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"业务部门投诉处理审核初始化查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryODepartmentComplaintVerify(){
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
				
			PageResult<Map<String,Object>> complaintAcceptData = dao.queryComplaintInfo01(vin,name,tele,level,dealUser,accUser,biztype,
					status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,""+logonUser.getUserId(),Constant.COMPLAINT_STATUS_DOING+"','"+Constant.COMPLAINT_STATUS_DOING_REJECT,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", complaintAcceptData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"业务部门投诉延期审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	private String getStatusByLevel(String level){
		String status = "";
		if(level.equals(Constant.Level_Manager_01)){
			status =  Constant.ALREADY_APPLY.toString();
		}else if(level.equals(Constant.Level_Manager_02)){
			status =  Constant.PASS_Manager_01.toString();
		}else if(level.equals(Constant.Level_Manager_03)){
			status =  Constant.PASS_Manager_02.toString();
		}
		return status;
	}
	
	//大区延期车厂审核
	public void verifyDelay(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  	
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  	
			String clstatus = CommonUtils.checkNull(request.getParamValue("clstatus"));  	
			
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
			Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
			act.setOutData("complaintConsult", complaintConsult);
			act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
			act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
			
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
			act.setOutData("clstatus",clstatus);
			
			act.setForword(VerifyDelay);			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"业务部门延期申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void verifyDelaySubmit(){
		try{			
			act.getResponse().setContentType("application/json");
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  				
			String verifystatus = CommonUtils.checkNull(request.getParamValue("verifystatus")); 
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont")); 
			String clstatus = CommonUtils.checkNull(request.getParamValue("clstatus")); 
			
			
			//审核状态
			int status = 0;
			if(verifystatus.equals("0")) status = Constant.REJECT_APPLY;
			else if(verifystatus.equals("1")) status = Constant.PASS_APPLY;
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			ttCrmComplaintPO2.setCpClOnceDate(null);
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)complaintAcceptDao.select(ttCrmComplaintPO1).get(0);
			
			
			//查询出为申请状态的申请记录
			TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO = complaintAcceptDao.queryApplayDelay(Long.parseLong(cpid),clstatus,null);
			//TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO2 = new TtCrmComplaintDelayRecordPO();
			//ttCrmComplaintDelayRecordPO2.setClId(ttCrmComplaintDelayRecordPO.getClId());
			TtCrmComplaintDelayRecordPO ttCrmComplaintDelayRecordPO1 = new TtCrmComplaintDelayRecordPO();
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			ttCrmComplaintDelayRecordPO1 = commonUtilActions.copyTtCrmComplaintDelayRecordPO(ttCrmComplaintDelayRecordPO, ttCrmComplaintDelayRecordPO1);
			ttCrmComplaintDelayRecordPO1.setClId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDelayRecordPO1.setClVerifyContent(ccont);
			ttCrmComplaintDelayRecordPO1.setClVerifyDate(new Date());
			
			ttCrmComplaintDelayRecordPO1.setClVerifyUser(logonUser.getName());
			ttCrmComplaintDelayRecordPO1.setClVerifyUserId(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO1.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO1.setUpdateDate(new Date());
			ttCrmComplaintDelayRecordPO1.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDelayRecordPO1.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDelayRecordPO1.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDelayRecordPO1.setCreateDate(new Date());
			
			if(status == Constant.PASS_APPLY){
				//已申请 ：室主任审核
				if(clstatus.equals(Constant.ALREADY_APPLY.toString())){
					ttCrmComplaintPO2.setCpClStatus(Constant.PASS_Manager_01);
					ttCrmComplaintDelayRecordPO1.setClVerifyStatus(Constant.PASS_Manager_01);
					ttCrmComplaintDelayRecordPO1.setCpNextDealId(ttCrmComplaintPO3.getCpClTurnOrg());
					
				//室主任审核通过 : 处长审核
				}else if(clstatus.equals(Constant.PASS_Manager_01.toString())){
					ttCrmComplaintPO2.setCpClStatus(Constant.PASS_Manager_02);
					ttCrmComplaintDelayRecordPO1.setClVerifyStatus(Constant.PASS_Manager_02);
					//2010010100070674L昌河ORGID
					ttCrmComplaintDelayRecordPO1.setCpNextDealId(2010010100070674L);
				//处长审核通过：副总审核
				}else if(clstatus.equals(Constant.PASS_Manager_02.toString())){
					
					if((""+Constant.DEFER_TYPE_01).equals(""+ttCrmComplaintPO3.getCpDeferType()))
					{
						// ttCrmComplaintPO2.setCpClDate(ttCrmComplaintDelayRecordPO.getClDate());
						
						ttCrmComplaintPO2.setCpClContent(ttCrmComplaintDelayRecordPO.getClContent());
						/**
						 * 设置 投诉最终延期至时间[CP_CL_FDATE] = 余总审核的最总延期至时间
						 * 需考核延期至时间设置 TT_CRM_COMPLAINT
						 * 规定关闭时间:SELECT t.cp_turn_date+NVL(t.cp_limit,0) FROM Tt_Crm_Complaint t 
						 * 
							SELECT T.CP_ID, T.CL_DATE, MIN(T.CREATE_DATE)
							  FROM TT_CRM_COMPLAINT_DELAY_RECORD T
							 WHERE T.APPLY_ORG IS NOT NULL
							   AND T.CP_DEFER_TYPE = 94081001
							 GROUP BY T.CP_ID, T.CL_DATE
							 ORDER BY 1, 2 DESC;
						 * 取第一条数据
						 * 1.判断当次延期申请时间[tt_crm_complaint_delay_record](大区或经销商延期申请时间) 是否 与 规定关闭时间在同一个月
						 *   如果是: 则把余总审核的投诉延期至时间 设置到 [CP_CL_DATE需考核延期申请时间 审核通过后记录判断]
						 */
						StringBuffer sql= new StringBuffer();
						sql.append("SELECT T.CP_ID,\n" );
						sql.append("       to_char(T.CL_DATE, 'yyyy-mm-dd') CL_DATE,\n" );
						sql.append("       to_char(MIN(T.CREATE_DATE), 'yyyy-mm') CREATE_DATE\n" );
						sql.append("  FROM TT_CRM_COMPLAINT_DELAY_RECORD T \n" );
						sql.append(" WHERE \n" );
						sql.append("    T.CP_DEFER_TYPE = 94081001\n" );
						sql.append("  AND T.CP_ID =  "+ cpid );
						sql.append(" GROUP BY T.CP_ID, T.CL_DATE\n" );
						sql.append(" ORDER BY 1, 2 DESC");
						List<Map<String,Object>>  AcceptList= complaintAcceptDao.pageQuery(sql.toString(), null,complaintAcceptDao.getFunName() );
						String  CL_DATE = "";
						String  CREATE_DATE = "";
						if(AcceptList.size() > 0 )
						{
							  CL_DATE = AcceptList.get(0).get("CL_DATE").toString();
							  CREATE_DATE = AcceptList.get(0).get("CREATE_DATE").toString();
						}
						
						sql= new StringBuffer();
						sql.append("SELECT to_char((t.cp_turn_date + NVL(t.cp_limit, 0)), 'yyyy-mm') CP_TURN_DATE\n" );
						sql.append("  FROM Tt_Crm_Complaint t\n" );
						sql.append(" where t.CP_TURN_DATE is not null   and t.CP_ID =  " + cpid );
						List<Map<String,Object>>  cpAcceptList= complaintAcceptDao.pageQuery(sql.toString(), null,complaintAcceptDao.getFunName() );
						String CP_TURN_DATE = "";
						if(cpAcceptList.size() > 0 )
						{
							CP_TURN_DATE = cpAcceptList.get(0).get("CP_TURN_DATE").toString();
						}
						
						 String formatStyle ="yyyy-MM-dd";   
					     DateFormat df = new SimpleDateFormat(formatStyle); 
						
						if(Utility.testString(CREATE_DATE) && CREATE_DATE.equals(CP_TURN_DATE))
						{
							if(Utility.testString(CL_DATE))
							{
								ttCrmComplaintPO2.setCpClDate(df.parse(CL_DATE));
								ttCrmComplaintPO2.setCpClFdate(df.parse(CL_DATE));
							}
							
						}else
						{
							if(Utility.testString(CL_DATE))
							{
							    ttCrmComplaintPO2.setCpClFdate(df.parse(CL_DATE));
							}
						}

						
						ttCrmComplaintPO2.setCpClUser(ttCrmComplaintDelayRecordPO.getClUserId());
						
						ttCrmComplaintPO2.setCpClStatus(Constant.PASS_Manager_03);
						ttCrmComplaintPO2.setCpClUser(ttCrmComplaintDelayRecordPO.getClUserId());
						ttCrmComplaintPO2.setCpClVfContent(ccont);
						ttCrmComplaintPO2.setCpClVfDate(new Date());
						ttCrmComplaintPO2.setCpClVfUser(logonUser.getName());
						ttCrmComplaintPO2.setCpClVfUserId(logonUser.getUserId());
						
						ttCrmComplaintPO2.setCpClTurnOrg(-1L);
						
						ttCrmComplaintDelayRecordPO1.setClVerifyStatus(Constant.PASS_Manager_03);
						
					}else if((""+Constant.DEFER_TYPE_02).equals(""+ttCrmComplaintPO3.getCpDeferType()))
					{
						ttCrmComplaintPO2.setCpClStatus(Constant.PASS_Manager_03);
						ttCrmComplaintDelayRecordPO1.setClVerifyStatus(Constant.PASS_Manager_03);
						ttCrmComplaintDelayRecordPO1.setCpNextDealId(2013070519381899L);//服务营销处
//						ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_CLOSE));
//						ttCrmComplaintPO2.setCpSf(Integer.parseInt(Constant.YAWP));
//						ttCrmComplaintPO2.setCpCloseDate(new Date());
//						ttCrmComplaintPO2.setCpCloseUser(logonUser.getUserId());
					}
				}
				ttCrmComplaintPO2.setCpClContent(ttCrmComplaintDelayRecordPO.getClContent());
			
				

						
			}else if(status == Constant.REJECT_APPLY)
			{
				ttCrmComplaintPO2.setCpClStatus(status);
				//ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_DOING));
				ttCrmComplaintPO2.setCpDealUser(ttCrmComplaintPO3.getCpDealOrg());
				ttCrmComplaintDelayRecordPO1.setClVerifyStatus(status);
				ttCrmComplaintPO2.setCpClTurnOrg(-1L);
			}
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			complaintAcceptDao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			
			complaintAcceptDao.insert(ttCrmComplaintDelayRecordPO1);
			//complaintAcceptDao.update(ttCrmComplaintDelayRecordPO2, ttCrmComplaintDelayRecordPO1);
		
			act.setOutData("isSuccess", "true");		
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"业务部门延期申请审核保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	

	public void oDepartmentComplaintVerifyDownExcel(){
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
				
			List<Map<String, Object>> complaintAcceptData =  dao.queryComplaintInfo01List(vin,name,tele,level,dealUser,accUser,biztype,
					status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,""+logonUser.getUserId(),Constant.COMPLAINT_STATUS_DOING+"','"+Constant.COMPLAINT_STATUS_DOING_REJECT);
			if(complaintAcceptData!=null && complaintAcceptData.size()>0){
				complaintAcceptDataToExcel(complaintAcceptData);
				act.setOutData("success", "true");
				act.setForword(ODepartmentComplaintVerify);
			}else {
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
		List<Map<String, Object>> cpls = tcCodeDao.getTcCodesByType(Constant.COMPLAINT_LEVEL.toString());
	    List<Map<String, Object>> cpns = tcCodeDao.getTcCodesByType(Constant.TYPE_COMPLAIN.toString());
	    List<Map<String, Object>> cpss = tcCodeDao.getTcCodesByType(Constant.COMPLAINT_STATUS);
	    List<Map<String, Object>> cpis = tcCodeDao.getTcCodesByType(Constant.IF_TYPE.toString());
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[37];
					detail[0] = CommonUtils.checkNull(map.get("CPNO"));
					detail[1] = CommonUtils.checkNull(map.get("CPNO"));
					detail[2] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CPLEVEL")),cpls);
					detail[3] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("BIZCONT")),cpns);
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
					detail[30] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CPISONCESF")),cpis);
					detail[31] = CommonUtils.checkNull(map.get("ISNORMALCLOSE"));
					detail[32] = CommonUtils.checkNull(map.get("DEALCONTENT"));
					detail[33] = CommonUtils.checkNull(map.get("LASTDEALDATE"));
					detail[34] = CommonUtils.checkNull(map.get(""));
					detail[35] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CPSTATUS")),cpss);
					detail[36] = CommonUtils.checkNull(map.get("DUSER"));
					
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
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
