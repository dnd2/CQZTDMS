package com.infodms.yxdms.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.dealerClaimMng.DealerClaimReportDao;
import com.infodms.dms.dao.claim.preAuthorization.AuthorizationDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityProjectPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.service.OrderService;
import com.infodms.yxdms.service.YsqService;
import com.infodms.yxdms.service.impl.ClaimServiceImpl;
import com.infodms.yxdms.service.impl.OrderServiceImpl;
import com.infodms.yxdms.service.impl.YsqServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 索赔单action
 * @author yuewei
 *
 */
public class ClaimAction extends BaseAction{

	private ClaimService claimservice=new ClaimServiceImpl();
	private OrderService orderservice=new OrderServiceImpl();
	private YsqService ysqservice = new YsqServiceImpl();
	private DealerClaimReportDao reportDao = DealerClaimReportDao.getInstance();
	PageResult<Map<String, Object>> list=null;
	private AuthorizationDao dao = AuthorizationDao.getInstance();
	
	public void showWorkFlowByid(){
		List<Map<String, Object>> list=ysqservice.findYsqRecords(request);
		act.setOutData("list", list);
		sendMsgByUrl("ysq", "ysq_show_workflow_app", "预授权流程显示");
	}
	
	public void showAllRecords(){
		List<Map<String, Object>> records=claimservice.showAllRecords(getParam("id"));
		act.setOutData("records", records);
	}
	
	public void normalReportSubmit(){
		int res=claimservice.normalReportSubmit(getParam("id"));
		super.setJsonSuccByres(res);
	}
	
	public void pdiAddCheck(){
		String res=claimservice.pdiAddCheck(getParam("vin"),request,loginUser);
		act.setOutData("res", res);
	}
	
	public void findFreeRoByPartId(){
		String res=orderservice.findFreeRoByPartId(request);
		act.setOutData("res", res);
	}
	
	public void findAccData(){
		List<Map<String, Object>> accData=orderservice.findAccData(request);
		act.setOutData("acc", accData);
	}
	
	public void checkClaim(){
		String str=claimservice.checkClaim(request,loginUser);
		act.setOutData("res", str);
	}
	
	public void deleteTrPart(){
		int res=orderservice.deleteTrPart(request);
		setJsonSuccByres(res);
	}
	
	public void pdiManageList(){
		String flag = getParam("query");
		if("true".equals(flag)){
			list=claimservice.pdiManageList(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("claim", "pdi_manage_list", "索赔单PDI列表查询页面");
	}
	public void pdiAdd(){
		request.setAttribute("type", "add");
		sendMsgByUrl("claim", "pdi_manage_add", "索赔单PDI新增查询页面");
	}
	public void pdiAddSure(){
		int res=claimservice.pdiAddSure(loginUser,request);
		setJsonSuccByres(res);
		String identify = DaoFactory.getParam(request, "identify");
		act.setOutData("identify", identify);
	}
	public void pdiDelete(){
		int res=claimservice.pdiDelete(request);
		setJsonSuccByres(res);
	}
	public void pdiView(){
		Map<String,Object> data=claimservice.pdiView(loginUser,request);
		act.setOutData("t", data);
		this.getFile("id");
		request.setAttribute("type", "view");
		sendMsgByUrl("claim", "pdi_manage_add", "索赔单PDI新增查询页面");
	}
	public void pdiUpdateInit(){
		Map<String,Object> data=claimservice.pdiView(loginUser,request);
		act.setOutData("t", data);
		this.getFile("id");
		request.setAttribute("type", "update");
		sendMsgByUrl("claim", "pdi_manage_add", "索赔单PDI新增查询页面");
	}
	public void checkshowInfoByVin(){
		String res=claimservice.checkPdiByVin(request);
		act.setOutData("res", res);
		if("".equals(res)){
			Map<String,Object> data =orderservice.showInfoByVin(request);
			act.setOutData("info", data);
		}
	}
	public void keepFitAdd(){
		request.setAttribute("type", "add");
		sendMsgByUrl("claim", "keep_fit_manage_add", "索赔单保养新增查询页面");
	}
	public void keepFitManageList(){
		String flag = getParam("query");
		if("true".equals(flag)){
			list=claimservice.keepFitManageList(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("claim", "keep_fit_manage_list", "索赔单保养列表查询页面");
	}
	public void keepFitView(){
		this.findData("view");
		sendMsgByUrl("claim", "keep_fit_manage_add", "索赔单保养明细查询页面");
	}
	
	public void keepFitDelete(){
		int res =claimservice.keepFitDelete(request);
		setJsonSuccByres(res);
	}
	public void keepFitCancel(){
		int res =claimservice.keepFitCancel(request);
		setJsonSuccByres(res);
	}
	public void keepFitUpdateInit(){
		this.findData("update");
		sendMsgByUrl("claim", "keep_fit_manage_add", "索赔单保养修改查询页面");
	}
	private void findData(String type) {
		List<Map<String,Object>> labours2= claimservice.findLaboursById(request);
		List<Map<String,Object>> parts2= claimservice.findPartsById(request);
		Map<String,Object> data =claimservice.findClaimPoById(request);
		Map<String,Object> outrepair=claimservice.findoutrepair(request);
//		Map<String,Object> Winterdate =orderservice.showInfoWinterByVin(request,data.get("MODEL_CODE").toString(),loginUser);
//		act.setOutData("Winterdate", Winterdate);
		act.setOutData("t", data);
		if(checkListNull(labours2)){
			act.setOutData("labours2", labours2);
		}
		if(checkListNull(parts2)){
			act.setOutData("parts2", parts2);
		}
		this.getFile("id");
		request.setAttribute("type", type);
	}
	public void showInfoByVin(){
		Map<String,Object> data =orderservice.showInfoByVin(request);
		Map<String,Object> roNodate =orderservice.showInfoByroNo(request);
		Map<String,Object> Winterdate =orderservice.showInfoWinterByVin(request,data.get("MODEL_CODE").toString(),loginUser);
		act.setOutData("Winterdate", Winterdate);
		act.setOutData("info", data);
		act.setOutData("roNodate", roNodate);
	}
	public void keepFitAddSure(){
		int res =claimservice.keepFitAddSure(loginUser,request);
		setJsonSuccByres(res);
		String identify = DaoFactory.getParam(request, "identify");
		act.setOutData("identify", identify);
		if(res!=-1){
			sendMsgByUrl("claim", "keep_fit_manage_list", "索赔单保养列表查询页面");
		}
	}
	public void findRoKeepFit(){
		String flag = getParam("query");
		
		if("true".equals(flag)){
			list=claimservice.findRoKeepFit(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}else{
			request.setAttribute("vin", DaoFactory.getParam(request, "vin"));
		}
		sendMsgByUrl("claim", "find_ro_keep_fit", "工单保养列表查询页面");
	}
	public void getOrderDataByVin(){
		act.setOutData("ro_no", DaoFactory.getParam(request, "ro_no"));
		act.setOutData("vin", DaoFactory.getParam(request, "vin"));
		List<Map<String,Object>> labours2= orderservice.findLaboursById(request,"93331002");
		List<Map<String,Object>> parts2= orderservice.findPartsById(request,"93331002");
		if(checkListNull(labours2)){
			act.setOutData("labours2", labours2);
		}
		if(checkListNull(parts2)){
			act.setOutData("parts2", parts2);
		}
	}
	public void normalManageList(){
		String flag = getParam("query");
		if("true".equals(flag)){
			list=claimservice.normalManageList(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("claim", "normal_manage_list", "一般索赔单列表查询页面");
	}
	public void normalUpdateInit(){
		request.setAttribute("claim_type", DaoFactory.getParam(request, "claim_type"));
		request.setAttribute("type", "update");
		this.getAllData();
		sendMsgByUrl("claim", "normal_manage_add", "一般索赔修改页面");
	}
	private void getAllData() {
		Map<String,Object> data =claimservice.findClaimPoById(request);
		Map<String,Object> outrepair=claimservice.findoutrepair(request);
		Map<String,Object> outrepairmoney=claimservice.findoutrepairmoney(request);
		List<Map<String,Object>> labours= claimservice.findLaboursById(request);
		List<Map<String,Object>> parts= claimservice.findPartsById(request);
		Map<String,Object> dataCom =claimservice.findComPoById(request);
		Map<String,Object> dataYsq =claimservice.findYsqPoById(request);//查询预授权
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
		act.setOutData("dataYsq", dataYsq);
	}
	@SuppressWarnings("unchecked")
	public void normalView(){
		request.setAttribute("claim_type", DaoFactory.getParam(request, "claim_type"));
		request.setAttribute("type", "view");
		this.getAllData();
		
		StringBuffer sql= new StringBuffer();
		sql.append("select \n" );
		sql.append("a.id,to_char( a.create_date,'yyyy-MM-dd hh24:mi' ) sb_date ,\n" );
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
		sql.append("where a.create_by = b.user_id  and  a.id ="+ request.getParamValue("id"));
		List<Map<String, Object>> pg = dao.pageQuery01(sql.toString(), null, dao.getFunName());
		act.setOutData("pg", pg.get(0));
		String claimId = pg.get(0).get("ID").toString();
		StringBuffer sb= new StringBuffer();
		sb.append("select d.audit_date,\n" );
		sb.append("       c.name as user_Name,\n" );
		sb.append("       d.audit_remark,\n" );
		sb.append("       (select tc.code_desc\n" );
		sb.append("          from tc_code tc\n" );
		sb.append("         where tc.code_id = d.audit_result) as audit_result\n" );
		sb.append("  from tt_as_wr_app_audit_detail d, tc_user c\n" );
		sb.append(" where d.audit_by = c.user_id\n");
		DaoFactory.getsql(sb, "d.claim_id", claimId, 1);
		List<Map<String, Object>> list  = dao.pageQuery(sb.toString(), null, dao.getFunName());
		act.setOutData("list", list);
		sendMsgByUrl("claim", "normalView", "一般索赔明细页面");
	}
	public void normalDelete(){
		int res=claimservice.normalDelete(request);
		setJsonSuccByres(res);
	}
	public void normalAdd(){
		request.setAttribute("claim_type", DaoFactory.getParam(request, "claim_type"));
		request.setAttribute("type", "add");
		Map<String,Object> data=orderservice.findLoginUserInfo(loginUser.getUserId());
		act.setOutData("userInfo",data);
		sendMsgByUrl("claim", "normal_manage_add", "一般索赔新增页面");
	}
	public void normalAddSure(){
		int res=claimservice.normalAddSure(loginUser,request);
		String identify = DaoFactory.getParam(request, "identify");
		act.setOutData("identify", identify);
		setJsonSuccByres(res);
		if(res==1){
			sendMsgByUrl("claim", "normal_manage_list", "一般索赔单列表查询页面");
		}
	}
	public void normalCancel(){
		int res =claimservice.keepFitCancel(request);
		setJsonSuccByres(res);
	}
	private void getFile(String ywzjName) {
		String ywzj = DaoFactory.getParam(request, ywzjName);
		List<FsFileuploadPO> fileList = claimservice.queryAttById(ywzj);// 取得附件
		act.setOutData("fileList", fileList);
	}
	public void findRoBaseInfo(){
		String flag = getParam("query");
		request.setAttribute("repairtypecode", DaoFactory.getParam(request, "repairtypecode"));
		request.setAttribute("claimType", DaoFactory.getParam(request, "claimType"));
		if("true".equals(flag)){
			list=claimservice.findRoBaseInfo(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("claim", "normal_ro_base_info", "工单列表查询页面");
	}
	public void findYsqBaseInfo(){
		String flag = getParam("query");
		request.setAttribute("claim_type", getParam("claim_type"));
		request.setAttribute("ro_no", getParam("ro_no"));
		request.setAttribute("vin", getParam("vin"));//工单上的vin
		if("true".equals(flag)){
			list=claimservice.findYsqBaseInfo(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("claim", "normal_ysq_base_info", "预授权列表查询页面");
	}
	public void addPartNormal(){
		String flag = getParam("query");
		request.setAttribute("ro_no", DaoFactory.getParam(request, "ro_no"));
		request.setAttribute("part_id", DaoFactory.getParam(request, "part_id"));
		request.setAttribute("claim_type", DaoFactory.getParam(request, "claim_type"));
		if("true".equals(flag)){
			list=claimservice.addPartNormal(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("claim", "normal_ro_part", "工单配件列表查询页面");
	}
	public void addLabourNormal(){
		String flag = getParam("query");
		String wrgroup_id = getParam("wrgroup_id");
		request.setAttribute("wrgroup_id", wrgroup_id);
		String package_id = getParam("package_id");
		request.setAttribute("package_id", package_id);
		request.setAttribute("dealer_id", getCurrDealerId());
		request.setAttribute("labour_codes", getParam("labour_codes"));
		request.setAttribute("labour_codes", getParam("labour_codes"));
		request.setAttribute("part_id_1", getParam("part_id_1"));
		if("true".equals(flag)){
			list=orderservice.addLabour(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("order", "add_labour", "工单列表查询页面");
	}
	public void supplierCodeByPartCode(){
		String flag = getParam("query");
		request.setAttribute("partcode", DaoFactory.getParam(request, "partcode"));
		if("true".equals(flag)){
			list=claimservice.supplierCodeByPartCode(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("claim", "showSupplierCode", "供应商查询页面");
	}
	public void queryOldPartCode(){
		String flag = getParam("query");
		String model_id = getParam("model_id");
		request.setAttribute("model_id", model_id);
		String series_id = getParam("series_id");
		request.setAttribute("series_id", series_id);
		request.setAttribute("dealer_id", getCurrDealerId());
		if("true".equals(flag)){
			list=orderservice.addPart(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("claim", "choose_old_part", "配件列表查询页面");
	}
	public void normalAudit(){
		int res=claimservice.normalAudit(loginUser,request);
		if(res==1){
			act.setOutData("info", "审核成功！");
		}
	}
	/**
	 * 批量操作
	 */
	public void passOrRebutByIds(){
		int res=1;
		try {
			String idStrs = getParam("id");
			String type = getParam("type");
			String auditOpinion = getParam("ADUIT_REMARK");
			String[] ids = StringUtils.split(idStrs, ",");
			if("1".equals(type)){//批量审核通过
				for (String id : ids) {
					reportDao.doPassByClaimTypeAndId(id,auditOpinion,loginUser);
				}
			}
			if("2".equals(type)){//批量退回
				reportDao.doRebutByClaimId(ids,auditOpinion,loginUser);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			setJsonSuccByres(res);
		}
	}
	/**
	 * 查询预授权的信息和预授权配件信息
	 */
	public void queryYsqDataById(){
		Map<String,Object> data=claimservice.queryYsqDataById(loginUser,request);//查询预授权信息
		Map<String,Object> ysqPartdata=claimservice.ysqPartdataById(loginUser,request);
		//获取三包等级
		String vrLevel = "无";
		List<Map<String, Object>> vrLevelList = dao.getVrLevel(request);
		if(checkListNull(vrLevelList)){
			vrLevel = BaseUtils.checkNull(vrLevelList.get(0).get("VR_LEVEL"));
		}
		act.setOutData("vrLevel", vrLevel);
		act.setOutData("data", data);
		act.setOutData("ysqPart", ysqPartdata);
		if(null != request.getParamValue("bc_pass") && !"null".equals(request.getParamValue("bc_pass")) && !"".equals(request.getParamValue("bc_pass")) ){
			act.setOutData("bc_pass", request.getParamValue("bc_pass"));
		}else
			act.setOutData("bc_pass", "0");
		act.setOutData("max_estimate", request.getParamValue("max_estimate"));
	}
	public void showDataInfoByVin(){
		Map<String,Object> data =orderservice.showInfoByVin(request);
		act.setOutData("info", data);
	}
	public void  ClaimAbandonedList(){
		sendMsgByUrl("claim", "ClaimAbandonedList", "索赔单废弃查询主页面");
	}
	/**
	 * 索赔单废弃查询
	 */
	public void ClaimAbandonedQuery(){
		
		PageResult<Map<String, Object>> ps = claimservice.ClaimAbandonedQuery(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", ps);
	}
	/**
	 * 索赔单标签
	 */
	public void barcodePrintDoGet(){
		try{
			RequestWrapper request = act.getRequest();
			String dtlIds = request.getParamValue("dtlIds");
			List<Map<String, Object>> ls= claimservice.barcodePrintDoGet(request);
			//修改索赔单是否打印的状态
			claimservice.updatePrint(dtlIds);
	    		if(ls!=null&&ls.size()>0){
	    			act.setOutData("ls1", "轿车"+ls.get(0).get("AREA_NAME")) ;
	    			act.setOutData("yieldly", ls.get(0).get("BALANCE_YIELDLY"));
	    		} else{
	    			act.setOutData("yieldly", "");
	    			act.setOutData("ls1", "轿车") ;
	    		}
			act.setOutData("ls", ls) ;
			sendMsgByUrl("claim", "barcode_print_doget", "索赔单标签打印");
//			act.setForword(barcodePrintDoGet);
		}
		 catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	
	}
	/**
	 * 
	 * @param claimId 索赔单状态跟踪打印
	 *
	 */
   public void claimBillForwardPrint(){
		
	   try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("dtlIds");
			String type = request.getParamValue("claimType");
			List<TtAsWrApplicationExtPO> list2 = null;
			Double zhushu = 0.0;
			Double canbu = 0.0;
			Double buzhu = 0.0;
			Double guoqiao = 0.0;
			Double jiaot = 0.0;
			Double partDown = 0.0;
			Double labourDown = 0.0;
			Double outAll=0.0;
			if(Constant.CLA_TYPE_09==Integer.parseInt(type)){
				List<TtAsWrOutrepairPO> listO = claimservice.getOutDetail(id);
				List<TtAsWrNetitemPO> nList = claimservice.getOther(id);
				if(nList.size()>0){
					for(int i=0;i<nList.size();i++){
						if("QT006".equalsIgnoreCase(nList.get(i).getItemCode())){
							guoqiao+=nList.get(i).getAmount();
						}else if("QT007".equalsIgnoreCase(nList.get(i).getItemCode())){
							jiaot+=nList.get(i).getAmount();
						}else if("QT008".equalsIgnoreCase(nList.get(i).getItemCode())){
							zhushu+=nList.get(i).getAmount();
						}else if("QT009".equalsIgnoreCase(nList.get(i).getItemCode())){
							canbu+=nList.get(i).getAmount();
						}else if("QT010".equalsIgnoreCase(nList.get(i).getItemCode())){
							buzhu+=nList.get(i).getAmount();
						}
						outAll += nList.get(i).getAmount();
					}
				}
				act.setOutData("nList", nList);
				act.setOutData("tawep", listO.get(0));
				act.setOutData("guoqiao", guoqiao);
				act.setOutData("jiaot", jiaot);
				act.setOutData("outAll", outAll);
				act.setOutData("zhushu", zhushu);
				act.setOutData("canbu", canbu);
				act.setOutData("buzhu", buzhu);
			}else if(Constant.CLA_TYPE_06==Integer.parseInt(type)){
				List<TtAsActivityPO> aList = claimservice.getADetail(id);
				if(aList!=null && aList.size()>0){
					act.setOutData("a", aList.get(0));
					TtAsActivityProjectPO jp = new TtAsActivityProjectPO();
					jp.setActivityId(aList.get(0).getActivityId());
					jp.setProCode(Constant.SERVICEACTIVITY_CAR_cms_06);
					List<TtAsActivityProjectPO> ss= claimservice.selectTtAsActivityProject(jp);
					if(ss!=null&&ss.size()>0){
						labourDown+=ss.get(0).getAmount();
					}
					TtAsActivityProjectPO jp2 = new TtAsActivityProjectPO();
					jp2.setActivityId(aList.get(0).getActivityId());
					jp2.setProCode(Constant.SERVICEACTIVITY_CAR_cms_06);
					List<TtAsActivityProjectPO> ss2= claimservice.selectTtAsActivityProject(jp2);
					if(ss2!=null&&ss2.size()>0){
						partDown+=ss2.get(0).getAmount();
					}
					act.setOutData("labourDown", labourDown);
					act.setOutData("partDown", partDown);
				}
			}
			TtAsWrApplicationPO tawaPo = new TtAsWrApplicationPO();
			tawaPo.setId(Long.valueOf(id));
			List <TtAsWrApplicationPO> tawaPoList = claimservice.selectttAsWrApplication(tawaPo);
			String claimNo = tawaPoList.get(0).getClaimNo();
			String length = "";
			List<Map<String, Object>> claimNoAccessoryList = claimservice.getclaimAccessoryDtl(claimNo);
			if(claimNoAccessoryList.size()==0){
				length = "0";
				act.setOutData("length", length);
			}else{
				act.setOutData("length", length);
			}
			act.setOutData("accessoriesPrice", tawaPoList.get(0).getAccessoriesPrice());
			act.setOutData("claimNoAccessoryList", claimNoAccessoryList);
			
			list2 = claimservice.getDetail(id);
			List<TtAsWrApplicationExtPO> list = claimservice.getBaseBean(id);
			String repairTimes = claimservice.getTimes(list.get(0).getVin());
			List<TtAsWrApplicationExtPO> baoy = claimservice.getbaoy(list.get(0).getVin());
				act.setOutData("bean", list.get(0));
				act.setOutData("list2", list2);
				act.setOutData("repairTimes", repairTimes);
				act.setOutData("baoy", baoy.get(0).getNum());
				act.setOutData("part", baoy.get(0).getPartPrice());
				act.setOutData("labour", baoy.get(0).getLabourPrice());
				sendMsgByUrl("claim", "claimBillTrackPrint", "索赔单打印");

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商索赔管理--索赔单打印");
			act.setException(e1);
		}
	}
	/**
	 * 修改配件
	 */
    public void updateprodutercode(){
       int res =	claimservice.updateprodutercode(request);
       setJsonSuccByres(res);
	   
    }
    /**
     * 查询总金额
     */
    public void findamount(){
       Map<String, Object> map = 	claimservice.findamount(loginUser,request);
       BigDecimal	amount=(BigDecimal) map.get("SUMAMOUNT");
    	if (null==amount) {
    		act.setOutData("SUMAMOUNT", 0);
		}else {
			act.setOutData("SUMAMOUNT", amount);
		}
    	
    }
    
    public void exportkeepFitManageList(){//保养索赔单导出
    	list=claimservice.keepFitManageList(loginUser,request,Constant.PAGE_SIZE_MAX,getCurrPage());
    	claimservice.exportkeepFitManageList(list,act,loginUser);
     }
    
    public void exportnormalManageList(){
    	list=claimservice.normalManageList(loginUser,request,Constant.PAGE_SIZE_MAX,getCurrPage());
    	claimservice.exportnormalManageList(list,act,loginUser);
	}
    public void exportpdiManageList(){
			list=claimservice.pdiManageList(loginUser,request,Constant.PAGE_SIZE_MAX,getCurrPage());
	    	claimservice.exportpdiManageList(list,act,loginUser);

	}
    public void  checkpartCode(){
     int res=claimservice.checkpartCode(loginUser,request); 
     setJsonSuccByres(res);
    }
    
    public void updatenewpartcode(){
    	int res=claimservice.updatenewpartcode(loginUser,request); 
        setJsonSuccByres(res);
    	
    	
    }
    public void viewbackaduit(){
    	list =claimservice.viewbackaduitList(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
    	List ps = list.getRecords();
    	act.setOutData("ps", ps);
    	sendMsgByUrl("claim", "view_back_audit_list", "索赔单退回记录页面");
    }
    /**
     * 修改索赔单是否回运
     */
    public void updateIsReturn(){
    	int res =claimservice.updateIsReturnById(loginUser,request);
    	setJsonSuccByres(res);
    }
    public void querypackagename(){
    	List<Map<String, Object>> list  =claimservice.findCarsystemByvin(loginUser,request);
    	if (null!=list && list.size()>0) {
			act.setOutData("systemcar", list.get(0));
		}else {
			act.setOutData("systemcar", -1);
		}
    }
}
