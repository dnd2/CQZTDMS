package com.infodms.yxdms.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.preAuthorization.AuthorizationDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.yxdms.entity.maintain.TtAsYsqPartPO;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.service.OrderService;
import com.infodms.yxdms.service.YsqService;
import com.infodms.yxdms.service.impl.ClaimServiceImpl;
import com.infodms.yxdms.service.impl.OrderServiceImpl;
import com.infodms.yxdms.service.impl.YsqServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;
/**
 * 预授权
 * @author yuewei
 *
 */
public class YsqAction extends BaseAction{

	private YsqService ysqservice = new YsqServiceImpl();
	private ClaimService claimservice=new ClaimServiceImpl();
	private final AuthorizationDao dao = AuthorizationDao.getInstance();
	private OrderService orderservice=new OrderServiceImpl();
	PageResult<Map<String, Object>> list=null;
	
	public void judgePartUseType(){
		Map<String,Object> map=ysqservice.judgePartUseType(request);
		act.setOutData("data", map);
	}
	
	public void ysqReportSubmit(){
		int res=ysqservice.ysqReportSubmit(getParam("id"),request,loginUser);
		setJsonSuccByres(res);
	}
	
	public void showWorkFlowTemp(){
		int res=ysqservice.showWorkFlow(request);
		List<Map<String, Object>> list=ysqservice.findYsqRecords(getParam("id"));
		act.setOutData("list", list);
		act.setOutData("res", res);
		sendMsgByUrl("ysq", "ysq_show_workflow_temp", "预授权流程显示");
	}
	
	public void showWorkFlow(){
		int res=ysqservice.showWorkFlow(request);
		List<Map<String, Object>> list=ysqservice.findYsqRecords(request);
		act.setOutData("list", list);
		act.setOutData("res", res);
		sendMsgByUrl("ysq", "ysq_show_workflow", "预授权流程显示");
	}
	
	public void auditRebut(){
		int res=ysqservice.auditRebut(request,loginUser);
		setJsonSuccByres(res);
	}
	public void auditYsq(){
		int res=ysqservice.auditYsq(request,loginUser);
		setJsonSuccByres(res);
	}
	
	public void jugeYsq(){
		String msg=ysqservice.jugeYsq(request);
		act.setOutData("msg", msg);
	}

	public void getInfoByVin(){
		//获取VIN信息
		Map<String, Object> data =  orderservice.showInfoByVin(request);
		//获取三包等级
		String vrLevel = "无";
		List<Map<String, Object>> vrLevelList = dao.getVrLevel(request);
		if(checkListNull(vrLevelList)){
			vrLevel = BaseUtils.checkNull(vrLevelList.get(0).get("VR_LEVEL"));
		}
		act.setOutData("data", data);
		act.setOutData("vrLevel", vrLevel);
	}
	//技术部人员审核列表
	public void ysqTechList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = ysqservice.ysqTechList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("ysq", "ysq_tech_list", "技术部人员审核列表");
	}
	//技术部主管审核列表
	public void ysqTechDirectorList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = ysqservice.ysqTechDirectorList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("ysq", "ysq_tech_director_list", "技术部主管审核列表");
	}
	//经销商申请列表
	public void ysqDealerList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = ysqservice.ysqDealerList(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("ysq", "ysq_dealer_list", "经销商申请列表查询页面");
	}
	
	//发动机列表
	public void ysqEngineList(){
		String query = getParam("query");
		setCurrMonthTime("startTime", "endTime");
		if ("true".equals(query)) {
			list = ysqservice.ysqEngineList(request, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("ysq", "ysq_engine_list", "发动机列表查询页面");
	}
	//======================================预授权申请和审核页面
	public void addYsqApply(){
		request.setAttribute("type", "add");
		sendMsgByUrl("ysq", "add_ysq_apply", "预授权申请页面");
	}
	public void ysqEngineAudit(){
		Long userId = Long.valueOf(DaoFactory.getParam(request, "createBy"));
		this.getYSQInfo(userId);
		this.getFile("id");
		request.setAttribute("type", "engine");
		sendMsgByUrl("ysq", "ysq_apply_audit", "发动机部门审核");
	}
	public void ysqTechAudit(){
		Long userId = Long.valueOf(DaoFactory.getParam(request, "createBy"));
		this.getYSQInfo(userId);
		this.getFile("id");
		request.setAttribute("type", "tech");
		sendMsgByUrl("ysq", "ysq_apply_audit", "主机厂技术部审核");
	}
	public void ysqTechDirectorAudit(){
		Long userId = Long.valueOf(DaoFactory.getParam(request, "createBy"));
		this.getYSQInfo(userId);
		this.getFile("id");
		request.setAttribute("type", "director");
		sendMsgByUrl("ysq", "ysq_apply_audit", "主机厂主管部审核");
	}
	//======================================
	/***
	 * 索赔单跳转到预授权申请页面，带入索赔单上的信息
	 */
	public void goToYsqApply(){
		Map<String, Object> userInfo;
		//获取三包等级
		String vrLevel;
		vrLevel = "无";
		List<Map<String, Object>> vrLevelList = dao.getVrLevel(request);
		if (DaoFactory.checkListNull(vrLevelList)) {
			vrLevel = vrLevelList.get(0).get("VR_LEVEL").toString();
		}
		userInfo = orderservice.findLoginUserInfo(loginUser.getUserId());
		act.setOutData("vrLevel", vrLevel);
		act.setOutData("u", userInfo);
		
	    request.setAttribute("ysq_part_id", DaoFactory.getParam(request, "real_part_id"));
	    request.setAttribute("ysq_vin", DaoFactory.getParam(request, "vin"));
		
	        String part_id = DaoFactory.getParam(request, "real_part_id");
	        String part_code = DaoFactory.getParam(request, "part_code");
	        String vin = DaoFactory.getParam(request, "vin");
	        String part_name = DaoFactory.getParam(request, "part_name");
			String apply_amount = DaoFactory.getParam(request, "apply_amount");
			String pass_amount = DaoFactory.getParam(request, "pass_amount");
			String out_mileage = DaoFactory.getParam(request, "out_mileage");
			String remark = DaoFactory.getParam(request, "remark");
			String claimType = DaoFactory.getParam(request, "claim_type");
			String trouble_reason = DaoFactory.getParam(request, "trouble_reason");
			String trouble_desc = DaoFactory.getParam(request, "trouble_desc");
			String producer_code = DaoFactory.getParam(request, "producer_code");
			String is_return = DaoFactory.getParam(request, "is_return");
			//======主因件名称，代码，供应商
			String part_code_main= DaoFactory.getParam(request, "part_code_main");
			String part_name_main= DaoFactory.getParam(request, "part_name_main");
			String produce_code_main= DaoFactory.getParam(request, "produce_code_main");
			String produce_name_main= DaoFactory.getParam(request, "produce_name_main");
			String our_car_falg= DaoFactory.getParam(request, "our_car_falg");//背车费标识
			
			Map<String, Object> map=new HashMap<String, Object>();
			
//			List<TtAsYsqPartPO> list = ysqservice.checkpartCodeToysq(loginUser,request);
//			if (null!=list &&list.size()>0) {
//				Long isreturned = list.get(0).getIsReturned();//预授权是否回运
//				if (!"".equals(isreturned)&&null!=isreturned) {
//					map.put("IS_RETURNED",isreturned);
//				}
//			}
			
			map.put("PART_CODE_MAIN",part_code_main);
			map.put("PART_NAME_MAIN",part_name_main);
			map.put("PRODUCE_CODE_MAIN",produce_code_main);
			map.put("PRODUCE_NAME_MAIN",produce_name_main);
			map.put("COM_PASS",pass_amount);
			map.put("CLAIM_TYPE",claimType);
			map.put("TROUBLE_REASON",trouble_reason);
			map.put("TROUBLE_DESC",trouble_desc);
			map.put("OUR_CAR_FALG",our_car_falg);
			if(!"".equals(apply_amount)){
				map.put("COM_APPLY",apply_amount);
			}
			if(!"".equals(out_mileage)){
				map.put("BC_MILEAGE",out_mileage);
			}
			if(!"".equals(pass_amount)){
				map.put("COM_PASS",pass_amount);
			}
			map.put("BC_REMARK",remark);
			map.put("PRODUCER_CODE",producer_code);
			map.put("IS_RETURN",is_return);
			map.put("PART_ID",part_id);
			map.put("PART_CODE",part_code);
			map.put("PART_NAME",part_name);
			map.put("VIN",vin);
			act.setOutData("t", map);
			request.setAttribute("type", "goToYsq");
			sendMsgByUrl("ysq", "add_ysq_apply", "预授权服务站申请");
	}
	
	/**
	 * 预授权新增
	 */
	public void ysqAdd(){
		Map<String, Object>	userInfo = orderservice.findLoginUserInfo(loginUser.getUserId());
		act.setOutData("u", userInfo);
		request.setAttribute("type", "add");
		sendMsgByUrl("ysq", "add_ysq_apply", "预授权服务站申请");
	}
	
	public void ysqAddSure(){
		String identify = DaoFactory.getParam(request, "identify");
		act.setOutData("identify", identify);
		int res=ysqservice.ysqAddSure(request,loginUser);
		if (res==2) {
			act.setOutData("succ", 2);
		}else {
			setJsonSuccByres(res);
		}
	}
	
	/**
	 * 预授权新修改
	 */
	public void ysqUpate(){
		Long userId = Long.valueOf(DaoFactory.getParam(request, "createBy"));
		this.getYSQInfo(userId);
		request.setAttribute("type", "update");
		this.getFile("id");
		sendMsgByUrl("ysq", "add_ysq_apply", "预授权服务站修改申请");
	}
	private void getFile(String ywzjName) {
		String ywzj = DaoFactory.getParam(request, ywzjName);
		List<FsFileuploadPO> fileList = claimservice.queryAttById(ywzj);// 取得附件
		act.setOutData("fileList", fileList);
	}
	/**
	 * 预授权新打印
	 */
	public void ysqPrint(){
		Long userId = Long.valueOf(DaoFactory.getParam(request, "createBy"));
		this.getYSQInfo(userId);
		sendMsgByUrl("ysq", "add_ysq_apply_print", "预授权服务站打印申请");
	}
	/**
	 * 预授权新明细
	 */
	public void ysqView(){
		Long userId = Long.valueOf(DaoFactory.getParam(request, "createBy"));
		this.getYSQInfo(userId);
		request.setAttribute("type", "view");
		this.getFile("id");
		sendMsgByUrl("ysq", "add_ysq_view", "预授权新明细");
	}
	private void getYSQInfo(Long userId) {
		Map<String, Object>	userInfo = orderservice.findLoginUserInfo(userId);
		act.setOutData("u", userInfo);
		List<Map<String, Object>> ysqlist=ysqservice.findYsqData(request);
		if(checkListNull(ysqlist)){
			act.setOutData("t", ysqlist.get(0));
		}
		//获取VIN信息
		Map<String, Object> data =  orderservice.showInfoByVin(request);
		act.setOutData("v", data);
		//获取三包等级
		String vrLevel = "无";
		List<Map<String, Object>> vrLevelList = dao.getVrLevel(request);
		if(checkListNull(vrLevelList)){
			vrLevel = BaseUtils.checkNull(vrLevelList.get(0).get("VR_LEVEL"));
		}
		act.setOutData("vrLevel", vrLevel);
	}
	
	public void changeByYsqRo(){
		String id=ysqservice.changeByYsqRo(getParam("ysq_no"));
		act.setOutData("id", id);
	}
	
	public void auditEidt(){
		int res=ysqservice.auditEidt(request,loginUser);
		setJsonSuccByres(res);
		
	}
}
