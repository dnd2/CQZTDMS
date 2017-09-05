package com.infodms.yxdms.action;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsWrApplicationExtBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.application.ClaimManualAuditingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.exception.UserException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmParameterPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsWrAppAuditDetailPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.ClaimBalanceDAO;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.service.CommonService;
import com.infodms.yxdms.service.impl.ClaimServiceImpl;
import com.infodms.yxdms.service.impl.CommonServiceImpl;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ClaimBalanceAction  extends BaseAction{
	public Logger logger = Logger.getLogger(ClaimBalanceAction.class); 
	private ActionContext act = ActionContext.getContext();
	private ClaimBalanceDAO dao = ClaimBalanceDAO.getInstance();
	
	private final String MANUAL_AUDITING = "/jsp_new/claim/claimAuditIndexNew.jsp";
	private final String KPFIT_AUDIT = "/jsp_new/claim/keep_fit_manage_view_dtl.jsp";
	private final String PDI_AUDIT = "/jsp_new/claim/pdi_manage_view.jsp";
	private final String NORMAL_AUDIT = "/jsp_new/claim/normalViewDetail.jsp";
	private final String NORMAL_PRINT = "/jsp_new/claim/normal_view_Detail_print.jsp";
	private final String DEL_MANUAL_FOR_AUDITING = "/jsp_new/claim/deleteClaimAuditForIndexNew.jsp";
	private final String CLAIM_BILL_URL = "/jsp_new/claim/claimBillStatusTrackNew.jsp";// 主页面（查询）
	private final String CLAIM_QUERY ="/jsp_new/claim/claim_all_list.jsp";
	private String FUNCTION_DESC = "索赔申请审核作业";
	private ClaimService claimservice=new ClaimServiceImpl();
	private  CommonService  commonservice  = new CommonServiceImpl();
	/**
	 * 初始化页面
	 */
	public void claimBalanceList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String sql = "SELECT * FROM tm_org WHERE org_level = 2";
			List<Map<String, Object>> org = dao.pageQuery01(sql, null, dao.getFunName());
			act.setOutData("org", org);
			List listpo=  commonservice.findSelectList(null, null, null, "select to_char(p.pose_id) as pose_id, c.name from tr_user_pose p, tc_user c where p.user_id = c.user_id and p.pose_id in (select r.pose_id from TC_POSE r where r.pose_name like '%索赔结算专员%' and r.choose_dealer = '10041002'and r.POSE_TYPE = '10021001' AND r.POSE_STATUS = '10011001') and c.name<>'肖杨'");
			act.setOutData("listpo", listpo);
			request.setAttribute("poseId", loginUser.getPoseId());
			act.setForword(this.MANUAL_AUDITING);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,this.FUNCTION_DESC);
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 索赔审核查询页面
	 */
	public void claimManualAuditingQuery(){
		Map<String, String> map = new HashMap<String, String>();
		try{
			//取得查询参数
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("dealerId");//经销商代码（多个）
			String poseId = request.getParamValue("pose_id");
			String dealerName = request.getParamValue("DEALER_NAME");//经销商名称
			String roNo = request.getParamValue("CON_RO_NO");//工单号
			String claimType = request.getParamValue("CLAIM_TYPE");//索赔类型
			String vin = request.getParamValue("CON_VIN");//车辆唯一标识码
			String applyStartDate = request.getParamValue("CON_APPLY_DATE_START");//申请日期范围（开始时间）
			String applyEndDate = request.getParamValue("CON_APPLY_DATE_END");//申请日期范围（结束时间）
			String claimNo = request.getParamValue("CLAIM_NO");//索赔申请单号

			String STATUS = request.getParamValue("STATUS");//索赔申请单号
			String orgId = request.getParamValue("ORG_ID");//大区				
			map.put("dealerId", dealerId);
			map.put("dealerName", dealerName);
			map.put("roNo", roNo);
			map.put("claimType", claimType);
			map.put("vin", vin);
			map.put("applyStartDate", applyStartDate);
			map.put("applyEndDate", applyEndDate);
			map.put("claimNo", claimNo);

			map.put("STATUS", STATUS);

			map.put("orgId", orgId);
			map.put("pose_id",poseId);

			
			request.setAttribute("dealerId", dealerId);
			request.setAttribute("CON_APPLY_DATE_START", applyStartDate);
			request.setAttribute("CON_APPLY_DATE_END", applyEndDate);
			
			PageResult<Map<String,Object>> result =  dao.queryClaimAudit1(map,getCurrPage(),ActionUtil.getPageSize(request));
			ActionUtil.setCustomPageSizeFlag(act, true);
			
			act.setOutData("ps", result);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔申请单审核");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	public void claimBillForward() {
		try {
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			// 取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
			List<TmBusinessAreaPO> list = dao.select(new TmBusinessAreaPO());
			act.setOutData("loginUser", loginUser.getName());
			act.setOutData("yieldly", yieldly);
			act.setOutData("yieldlys", list);
			//车型
			act.setOutData("modelList", commonUtilActions.getAllModel());
			act.setForword(CLAIM_BILL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单状态跟踪");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void applicationQuery2() {
		PageResult<Map<String, Object>>  ps;
		try{
			ps = dao.queryApplication2(request,loginUser, ActionUtil.getPageSize(request), getCurrPage());
			ActionUtil.setCustomPageSizeFlag(act, true);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getFile(String ywzjName) {
		String ywzj = DaoFactory.getParam(request, ywzjName);
		List<FsFileuploadPO> fileList = claimservice.queryAttById(ywzj);// 取得附件
		act.setOutData("fileList", fileList);
	}
	
	public void claimBalanceAuditingPage(){
		ActionContext act = ActionContext.getContext();
		//取得参数
		RequestWrapper request = act.getRequest();
		String ID = request.getParamValue("id");//索赔单ID
		String claim_type = request.getParamValue("claim_type");//索赔单ID
		String view = request.getParamValue("view");//索赔单查询标识 view查看历史和其它明细
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String sql1 = "SELECT * FROM tm_org WHERE org_level = 2";
			List<Map<String, Object>> org = dao.pageQuery01(sql1, null, dao.getFunName());
			act.setOutData("org", org);
			
			StringBuffer sql= new StringBuffer();
			sql.append("select\n" );
			sql.append("to_char( a.create_date,'yyyy-MM-dd hh24:mi' ) sb_date ,\n" );
			sql.append("b.name sb_name\n" );
			sql.append(", to_char(a.report_date,'yyyy-MM-dd hh24:mi') sh_date,\n" );
			sql.append("d.name sh_name,\n" );
			sql.append("to_char(pf.create_date,'yyyy-MM-dd hh24:mi') hy_date,\n" );
			sql.append("pa.name  hy_name,\n" );
			sql.append("to_char(pf.SIGN_DATE,'yyyy-MM-dd hh24:mi') qs_date,\n" );
			sql.append("pb.name  qs_name,\n" );
			sql.append("to_char(pf.IN_WARHOUSE_DATE,'yyyy-MM-dd hh24:mi') rk_date,\n" );
			sql.append("pf.IN_WARHOUSE_NAME rk_name,\n" );
			sql.append("to_char(pc.create_date,'yyyy-MM-dd hh24:mi') kp_date,\n" );
			sql.append("pc.apply_person_name kp_name,\n" );
			sql.append("to_char(pd.COLLECT_TICKETS_DATE,'yyyy-MM-dd hh24:mi') sp_date,\n" );
			sql.append("pe.name sp_name,\n" );
			sql.append("to_char(pd.CHECK_TICKETS_DATE,'yyyy-MM-dd hh24:mi') yp_date,\n" );
			sql.append("pg.name zz_name,\n" );
			sql.append("to_char(pd.TRANSFER_TICKETS_DATE,'yyyy-MM-dd hh24:mi') zz_date,\n" );
			sql.append("ph.name zz_name,\n" );
			sql.append("to_char(pi.create_date,'yyyy-MM-dd hh24:mi') ck_date,\n" );
			sql.append("pj.name  ck_name,\n" );
			sql.append("to_char(phh.audit_date, 'yyyy-mm-dd hh24:mi') tp_date ,\n" );
			sql.append("ppp.name tp_name\n" );
			sql.append("\n" );
			sql.append("from  tc_user b , tt_as_wr_application a left join\n" );
			sql.append("(select\n" );
			sql.append("k.claim_id , max(k.audit_by) audit_by from\n" );
			sql.append(" tt_as_wr_app_audit_detail k group by  k.claim_id  ) c\n" );
			sql.append(" on a.id = c.claim_id\n" );
			sql.append("left join   tc_user d on  d.user_id = c.audit_by\n" );
			sql.append("\n" );
			sql.append("left join  (select aa.claim_id,max(aa.return_id) return_id from   tt_as_wr_old_returned_detail aa group by aa.claim_id ) p   on p.claim_id = a.id\n" );
			sql.append("left join tt_as_wr_old_returned pf on  pf.id = p.return_id\n" );
			sql.append("left join tc_user pa on pa.user_id = pf.create_by\n" );
			sql.append("left join tc_user pb on pb.user_id = pf.sign_person\n" );
			sql.append("left join tt_as_wr_claim_balance pc on pc.start_date < a.report_date and pc.end_date +1 > a.report_date and pc.dealer_id = a.dealer_id\n" );
			sql.append("left join ( select max(COLLECT_TICKETS) COLLECT_TICKETS\n" );
			sql.append(" , max(COLLECT_TICKETS_DATE )COLLECT_TICKETS_DATE ,\n" );
			sql.append(" max(CHECK_TICKETS) CHECK_TICKETS,\n" );
			sql.append(" max(COLLECT_TICKETS_DATE) CHECK_TICKETS_DATE,\n" );
			sql.append(" max(TRANSFER_TICKETS ) TRANSFER_TICKETS,\n" );
			sql.append(" max(TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE , balance_oder  from\n" );
			sql.append(" tt_as_payment  group by balance_oder     ) pd on pd.balance_oder = pc.remark\n" );
			sql.append(" left join tc_user pe on  pe.user_id = pd.COLLECT_TICKETS\n" );
			sql.append(" left join tc_user pg on  pg.user_id = pd.CHECK_TICKETS\n" );
			sql.append(" left join tc_user ph on  ph.user_id = pd.TRANSFER_TICKETS\n" );
			sql.append(" left join  (select claim_no, max(CREATE_DATE) CREATE_DATE , max(CREATE_by ) CREATE_by,max(out_no) out_no from    tt_as_wr_old_out_part group by  claim_no )  pi on pi.claim_no = a.claim_no\n" );
			sql.append(" left join  tc_user pj  on  pj.user_id = pi.CREATE_by\n" );
			sql.append(" left join Tt_As_Wr_Range_Single phh on  phh.out_no = pi.out_no\n" );
			sql.append(" left join tc_user ppp on ppp.user_id = phh.audit_by\n" );
			sql.append("where a.create_by = b.user_id  and  a.id ="+ ID);
			List<Map<String, Object>> pg = dao.pageQuery01(sql.toString(), null, dao.getFunName());
			act.setOutData("pg", pg.get(0));
			request.setAttribute("poseId", loginUser.getPoseId());
			act.setOutData("id", ID);
			request.setAttribute("type", "audit");
			request.setAttribute("goBackType", getParam("goBackType"));
			
			if(claim_type.equals(Constant.CLA_TYPE_02.toString())){//保养
				List<Map<String,Object>> labours2= claimservice.findLaboursById(request);
				List<Map<String,Object>> parts2= claimservice.findPartsById(request);
				Map<String,Object> data =claimservice.findClaimPoById(request);
				act.setOutData("t", data);
				if(checkListNull(labours2)){
					act.setOutData("labours2", labours2);
				}
				if(checkListNull(parts2)){
					act.setOutData("parts2", parts2);
				}
				this.getFile("id");
				request.setAttribute("type", claim_type);
				act.setOutData("type", view);
				act.setForword(this.KPFIT_AUDIT);
			}else if(claim_type.equals(Constant.CLA_TYPE_11.toString())){//PDI
				TtAsWrApplicationPO tempPo1=new TtAsWrApplicationPO();
				tempPo1.setId(Long.parseLong(ID));
				List<TtAsWrApplicationPO> select1 = dao.select(tempPo1);
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				tempPo1 = select1.get(0);
				Date subDate = (Date)tempPo1.getSubDate();
				if(null==subDate){
					throw new UserException("当前索赔单的上报日期为空,请联系管理员 !");
				}
				String dte = df.format(subDate);
				subDate = df.parse(dte);  
				
				TmParameterPO tpPO = new TmParameterPO();
				tpPO.setParameterStatus(Constant.STATUS_ENABLE);
				tpPO.setParameterType(Constant.PARAMETER_TYPE_01);
				List<TmParameterPO> tmPPOList= dao.select(tpPO);
				boolean flag =true ;
				if(null!=tmPPOList && tmPPOList.size()>0){
					flag = dao.checkInvoiceDateIsInTime(subDate,tmPPOList.get(0).getTimeout(),tempPo1.getVin());
				}
				if(!flag){
					act.setOutData("timeout", "！注意:"+tempPo1.getVin()+"该车的PDI申报日期已经超出设置时间,审核建议金额为0!");
				}
				Map<String,Object> data=dao.pdiView(loginUser,request,flag);
				act.setOutData("t", data);
				act.setOutData("type", view);
				act.setForword(this.PDI_AUDIT);
			}else{//一般索赔
				request.setAttribute("claim_type",claim_type);
				Map<String,Object> data =claimservice.findClaimPoById(request);
				Map<String,Object> outrepair=claimservice.findoutrepair(request);
				Map<String,Object> outrepairmoney=claimservice.findoutrepairmoney(request);
				List<Map<String,Object>> labours= claimservice.findLaboursById(request);
				List<Map<String,Object>> parts= claimservice.findPartsById(request);
				Map<String,Object> dataCom =claimservice.findComPoById(request);
				List<Map<String,Object>> dataAcc =claimservice.findAccPoById(request);
				List<Map<String,Object>> dataCarsystem =claimservice.findCarsystemById(request);
				if(checkListNull(labours)){
					act.setOutData("dataCarsystem", dataCarsystem.get(0));
				}
				if(checkListNull(labours)){
					act.setOutData("labours", labours);
				}
				if(checkListNull(parts)){
					act.setOutData("parts", parts);
				}
				if(checkListNull(dataAcc)){
					act.setOutData("acc", dataAcc);
				}
				this.getFile("id");
				act.setOutData("t", data);
				act.setOutData("com", dataCom);
				act.setOutData("o", outrepairmoney);
				act.setOutData("r", outrepair);
				act.setOutData("type", view);
				if ("print".equals(view)) {
					act.setOutData("type", "view");
					act.setForword(this.NORMAL_PRINT);
				}else {
					act.setForword(this.NORMAL_AUDIT);
				}
			}
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,this.FUNCTION_DESC);
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void claimManualAuditingForInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());
			act.setOutData("yieldly", yieldly);
			act.setForword(this.DEL_MANUAL_FOR_AUDITING);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,this.FUNCTION_DESC);
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void claimManualAuditingForQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Map<String, String> map = new HashMap<String, String>();
		try{
			//取得查询参数
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("dealerId");//经销商代码（多个）
			String dealerName = request.getParamValue("DEALER_NAME");//经销商名称
			String roNo = request.getParamValue("CON_RO_NO");//工单号
			String claimType = request.getParamValue("CLAIM_TYPE");//索赔类型
			String vin = request.getParamValue("CON_VIN");//车辆唯一标识码
			String partCode = request.getParamValue("partCode");//零件代码
			String wrLabourCode = request.getParamValue("wrLabourCode");//作业代码
			String applyStartDate = request.getParamValue("CON_APPLY_DATE_START");//申请日期范围（开始时间）
			String applyEndDate = request.getParamValue("CON_APPLY_DATE_END");//申请日期范围（结束时间）
			String claimNo = request.getParamValue("CLAIM_NO");//索赔申请单号
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			map.put("dealerId", dealerId);
			map.put("dealerName", dealerName);
			map.put("roNo", roNo);
			map.put("claimType", claimType);
			map.put("vin", vin);
			map.put("applyStartDate", applyStartDate);
			map.put("applyEndDate", applyEndDate);
			map.put("claimNo", claimNo);
			//增加零件代码，作业代码条件查询
			map.put("partCode", partCode);
			map.put("wrLabourCode", wrLabourCode);
			
			request.setAttribute("dealerId", dealerId);
			request.setAttribute("CON_APPLY_DATE_START", applyStartDate);
			request.setAttribute("CON_APPLY_DATE_END", applyEndDate);
			
			PageResult<Map<String,Object>> result =  dao.queryClaimAudit2(map,curPage,Constant.PAGE_SIZE);
			
			act.setOutData("ps", result);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔申请单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void pdiAudit(){
		RequestWrapper request = act.getRequest();
		String id = request.getParamValue("id");
		String amount = request.getParamValue("amount");
		
		String oldStatus = request.getParamValue("status");
		String auditRemark = request.getParamValue("AUDIT_REMARK");
		String identify = request.getParamValue("identify");
		String status = Constant.CLAIM_APPLY_ORD_TYPE_08.toString();//索赔已审核
		Map<String, Object> infoMap = new HashMap<String, Object>();
		//-------lj 3.23  更新审核人、时间
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		sb.append(" UPDATE  TT_AS_WR_APPLICATION  TT SET \n");
		sb.append("TT.AUDITING_MAN = "+logonUser.getUserId());
		sb.append(",\n" );
		sb.append("TT.AUDITING_DATE = sysdate " );
		sb.append("\n" );
		sb.append("where TT.ID ="+id);
		dao.update(sb.toString(), null);
		if("1".equals(identify)){//审核退回
			status = Constant.CLAIM_APPLY_ORD_TYPE_06.toString();
		}else if("2".equals(identify)){
			//撤销审核
			status = Constant.CLAIM_APPLY_ORD_TYPE_02.toString();
			amount = "0.0";
		}
		auditInterface(id,status,auditRemark,amount,oldStatus);
		infoMap.put("message", "PDI审核成功!");
		act.setOutData("info",infoMap );
		act.setForword(MANUAL_AUDITING);
	}
	
	/**
	 * PDI审核
	 * @param id
	 * @param status	审核状态
	 * @param auditRemark 
	 * @param claimType
	 * @param balanceAmount
	 * @param auditType
	 */
	public void auditInterface(String id,String status,String auditRemark,String balanceAmount,String oldStatus){
		/**
		 * 更新索赔单状态和结算金额
		 */
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
		TtAsWrApplicationPO parameterPO = new TtAsWrApplicationPO();
		TtAsWrApplicationPO parameterPO2 = new TtAsWrApplicationPO();
		parameterPO.setStatus(Integer.parseInt(status));
		parameterPO.setReportDate(new Date());
		parameterPO.setUpdateBy(logonUser.getUserId());
		parameterPO.setUpdateDate(new Date());
		//这个为单个审核的
		if(id!=null && !"".equals(id)){
			TtAsWrApplicationPO tempPo1=new TtAsWrApplicationPO();
			tempPo1.setId(Long.parseLong(id));
			List<TtAsWrApplicationPO> select1 = dao.select(tempPo1);
			TtAsWrApplicationPO awAPP = select1.get(0);
			if(!awAPP.getStatus().toString().equals(oldStatus)){
				throw new UserException("当前索赔单的状态已经发生变更,请刷新页面再试 !");
			}
			String RoNo = awAPP.getRoNo();
			TtAsRepairOrderPO tempPo=new TtAsRepairOrderPO();
			tempPo.setRoNo(RoNo);
			/*List<TtAsRepairOrderPO> select = dao.select(tempPo);
			String code = select.get(0).getCamCode();*/
			if(String.valueOf(Constant.CLA_TYPE_11).equals(awAPP.getClaimType().toString())){
				if(Constant.CLAIM_APPLY_ORD_TYPE_08.toString().equals(status)){
					parameterPO.setBalanceNetitemAmount(Double.parseDouble(balanceAmount));
					parameterPO.setBalanceAmount(Double.parseDouble(balanceAmount));
					parameterPO.setApplyNetitemAmount(Double.parseDouble(balanceAmount));
				}
			}
		}
		parameterPO.setAuditOpinion(auditRemark);
		parameterPO.setAuditingDate(new Date());
		parameterPO.setAuditingMan(logonUser.getUserId());
		parameterPO2.setId(Long.parseLong(id));
		
		if(Constant.CLAIM_APPLY_ORD_TYPE_08.toString().equals(status) || 
				Constant.CLAIM_APPLY_ORD_TYPE_02.toString().equals(status)){
			//审核通过或者撤销审核的时候执行
			TtAsWrNetitemPO tAWNIPO = new TtAsWrNetitemPO();
			TtAsWrNetitemPO tAWNIPO2 = new TtAsWrNetitemPO();
			tAWNIPO.setBalanceAmount(Double.parseDouble(balanceAmount));
			tAWNIPO.setApplyAmount(Double.parseDouble(balanceAmount));
			tAWNIPO2.setId(Long.parseLong(id));
			dao.update(tAWNIPO2, tAWNIPO);
			//====lj撤销审核时不再清空结算金额2015-6-10
//			if(Constant.CLAIM_APPLY_ORD_TYPE_02.toString().equals(status)){
//				//撤销审核需要将结算金额清空
//				parameterPO.setBalanceAmount(0.0);
//				parameterPO.setBalancePartAmount(0.0);
//				parameterPO.setBalanceLabourAmount(0.0);
//			}
		}
		
		//更新审核状态和金额
		dao.update(parameterPO2, parameterPO);
		
		//插入审核记录
		insertClaimRecord(logonUser,auditRemark,status,id);
		//更新索赔单结算费用
		/*TtAsWrApplicationPO po=new TtAsWrApplicationPO();
		TtAsWrApplicationPO updatePo=new TtAsWrApplicationPO();
		po.setId(BaseUtils.ConvertLong(id));
		List<TtAsWrApplicationPO> list = dao.select(po);
		if(list!=null&& list.size()>0){
			updatePo=list.get(0);
				TtAsWrApplicationPO po1=new TtAsWrApplicationPO();
				//索赔单总费用 = 辅料费+工时费+材料费+补偿费+外出(其他)+活动+保养+材料打折+工时打折
				Double BalanceAmount=updatePo.getAccessoriesPrice()+updatePo.getLabourAmount()+updatePo.getPartAmount()+BALANCE_NETITEM_AMOUNT2+BALANCE_NETITEM_AMOUNT+updatePo.getCampaignFee()+updatePo.getFreeMPrice()+updatePo.getPartDown()+updatePo.getLabourDown();
				po1.setBalanceAmount(BalanceAmount);
				po1.setBalanceNetitemAmount(BALANCE_NETITEM_AMOUNT);
				po1.setCompensationMoney(BALANCE_NETITEM_AMOUNT2);
				dao.update(po, po1);
		}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,this.FUNCTION_DESC);
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void insertClaimRecord(AclUserBean logonUser,String auditRemark,String status,String id) throws Exception{
		/**
		 * 记录审核记录（专门针对外出维修的进行结算金额修改等操作）
		 */
		TtAsWrAppAuditDetailPO dp = new TtAsWrAppAuditDetailPO();
		dp.setId(Utility.getLong(SequenceManager.getSequence("")));
		dp.setAuditBy(logonUser.getUserId());
		dp.setAuditDate(new Date());
		dp.setAuditRemark(auditRemark);
		dp.setAuditResult(Integer.parseInt(status));
		dp.setClaimId(Long.parseLong(id));
		dp.setCreateBy(logonUser.getUserId());
		dp.setCreateDate(new Date());
		dp.setAuditType(2);//整单审核
		dao.insert(dp);
	}
	
	public void fitAudit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String id = request.getParamValue("id");
			String oldStatus = request.getParamValue("status");
			String auditRemark = request.getParamValue("AUDIT_REMARK");
			String identify = request.getParamValue("identify");
			String[] partAmount = request.getParamValues("part_amont_2");
			String[] payType = request.getParamValues("pay_type_2");
			String[] partID = request.getParamValues("part_id_2");
			String[] partCode = request.getParamValues("part_code_2");
			String[] labourAmount = request.getParamValues("labour_fix_2");
			String[] labourPayType = request.getParamValues("pay_type_labour_2");
			String[] labourCode = request.getParamValues("labour_code_2");
			String claim_no = request.getParamValue("claim_no");
			String bntCountnew = request.getParamValue("bntCountnew");//审核金额
			String status = "";
			StringBuffer info = new StringBuffer(claim_no);
			if("0".equals(identify)){
				//审核通过
				status = Constant.CLAIM_APPLY_ORD_TYPE_08.toString();
				info.append("审核通过");
				
			}else if("1".equals(identify)){
				//审核退回
				status = Constant.CLAIM_APPLY_ORD_TYPE_06.toString();
				info.append("审核退回");
				
			}else if("2".equals(identify)){
				//撤销审核
				status = Constant.CLAIM_APPLY_ORD_TYPE_02.toString();
				info.append("撤销审核");
			}
			fitAuditInterface(id,status,auditRemark,partAmount,payType,labourAmount,labourPayType,oldStatus,bntCountnew);
			insertClaimRecord(logonUser,auditRemark,status,id);
			act.setOutData("info", info.append("操作成功!").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,this.FUNCTION_DESC);
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void fitAuditInterface(String id,String status,String auditRemark,String[] partAmount,String[] payType,
			String[] labourAmount,String[] labourPayType,String oldStatus,String bntCountnew){
		/**
		 * 更新索赔单状态和结算金额
		 */
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
		TtAsWrApplicationPO parameterPO = new TtAsWrApplicationPO();
		TtAsWrApplicationPO parameterPO2 = new TtAsWrApplicationPO();
		parameterPO.setStatus(Integer.parseInt(status));
		parameterPO.setReportDate(new Date());
		parameterPO.setUpdateBy(logonUser.getUserId());
		parameterPO.setUpdateDate(new Date());
		//这个为单个审核的
		if(id!=null && !"".equals(id)){
			/**
			 * 检查索赔单是否重复审核
			 */
			TtAsWrApplicationPO tempPo1=new TtAsWrApplicationPO();
			tempPo1.setId(Long.parseLong(id));
			List<TtAsWrApplicationPO> select1 = dao.select(tempPo1);
			TtAsWrApplicationPO awAPP = select1.get(0);
			if(!awAPP.getStatus().toString().equals(oldStatus)){
				throw new UserException("当前索赔单的状态已经发生变更,请刷新页面再试 !");
			}
			String RoNo = awAPP.getRoNo();
			TtAsRepairOrderPO tempPo=new TtAsRepairOrderPO();
			tempPo.setRoNo(RoNo);
			List<TtAsRepairOrderPO> select = dao.select(tempPo);
			String code = select.get(0).getCamCode();
			if(String.valueOf(Constant.CLA_TYPE_02).equals(awAPP.getClaimType().toString())){
				if(Constant.CLAIM_APPLY_ORD_TYPE_08.toString().equals(status)){
					Double t1 = getFee(partAmount,payType);
					Double t2 = getFee(labourAmount,labourPayType);
					parameterPO.setBalanceAmount(Double.parseDouble(bntCountnew));
					parameterPO.setBalancePartAmount(t1);
					parameterPO.setBalanceLabourAmount(t2);
				}
			}
		}
		parameterPO.setAuditOpinion(auditRemark);
		parameterPO2.setId(Long.parseLong(id));
		//=====lj撤销审核时结算金额不设为0  2015-6-10
//		if(Constant.CLAIM_APPLY_ORD_TYPE_02.toString().equals(status)){
//			parameterPO.setBalanceAmount(0.0);
//			parameterPO.setBalancePartAmount(0.0);
//			parameterPO.setBalanceLabourAmount(0.0);
//		}
		//更新审核状态和金额
		dao.update(parameterPO2, parameterPO);
		//插入审核记录
//		insertClaimRecord(logonUser,auditRemark,status,id);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 计算免费保养的材料费和工时费
	 * @param objs
	 * @param payTypes
	 * @return
	 */
	public Double getFee(String[] objs,String[] payTypes){
		Double totalFee = 0.0;
		if(null != payTypes && payTypes.length>0){
			for(int i=0;i<payTypes.length;i++){
				if("11801002".equals(payTypes[i])){
					if(null != objs[i]){
						totalFee += Double.parseDouble(objs[i]);
					}
				}
			}
		}
		return totalFee;
	}
	
	public void queryapplicationlist() {
		PageResult<Map<String, Object>> list =	dao.queryapplicationlist(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
	    act.setOutData("ps", list);
	}
	@SuppressWarnings("unchecked")
	public void queryapplication() {
		String range_no = getParam("range_no");
		String out_time = getParam("out_time");
		String supply_code = getParam("supply_code");
		String out_by = getParam("out_by");
		String supply_name = getParam("supply_name");
		String out_type = getParam("out_type");
		try {
			supply_name = new String(supply_name.getBytes("ISO8859-1"),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		act.setOutData("range_no", range_no);
		act.setOutData("out_time",out_time );
		act.setOutData("supply_code",supply_code);
		act.setOutData("supply_name", supply_name);
		act.setOutData("out_by", out_by);
		act.setOutData("out_type", out_type);
		act.setForword(CLAIM_QUERY);
	}
	
	
	
}
