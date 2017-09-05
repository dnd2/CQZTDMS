package com.infodms.yxdms.action;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.yxdms.service.ActivityService;
import com.infodms.yxdms.service.impl.ActivityServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;

public class ActivityAction extends BaseAction{
	
	private ActivityService activityservice = new ActivityServiceImpl();
	private ServiceActivityManageDao dao = ServiceActivityManageDao.getInstance();
	
	public void findActityData(){
		getDataById("show");
	}
	
	public void findActityByCamCode(){
		String id=activityservice.findTempletByCamCode(request);
		List<Map<String, Object>> list1 =activityservice.findTempletMain(id);
		List<Map<String, Object>> list2 =activityservice.findTempletLab(id);
		List<Map<String, Object>> list3 =activityservice.findTempletPart(id);
		List<Map<String, Object>> list4 =activityservice.findTempletAcc(id);
		List<Map<String, Object>> list5 =activityservice.findTempletCom(id);
		if(checkListNull(list1)){
			act.setOutData("t", list1.get(0));
		}
		if(checkListNull(list2)){
			act.setOutData("labours", list2);	
		}
		if(checkListNull(list3)){
			act.setOutData("parts", list3);
		}
		if(checkListNull(list4)){
			act.setOutData("acc", list4);
		}
		if(checkListNull(list5)){
			act.setOutData("com", list5.get(0));
		}
	}
	
	public void activityintoOthers() throws Exception{
			Map<String, Object> map= activityservice.findActivity(request);
			getBtnInfo();
			act.setOutData("po", map);
			getDataById("intoAdd");
			sendMsgByUrl("activity", "activity_manage_add", "新服务活动提报新增");
	}

	private void getBtnInfo() throws Exception {
		String activityId=DaoFactory.getParam(request, "activity_id");//活动ID
		List<TtAsActivityBean> ActivityVhclMaterialGroupList=dao.getVhclMaterialGroupList(activityId);//车型列表
		List<TtAsActivityBean> ActivitygetActivityAgeList=dao.getActivityAgeList(activityId);//车龄定义列表
		List<TtAsActivityBean> ActivityCharactorList=dao.getActivityCharactorList(activityId);//车辆性质//getMilageActivity
		List<Map<String, Object>> ActivityMileageList=dao.getMilageActivity(Long.valueOf(activityId));//里程限制
		request.setAttribute("ActivityVhclMaterialGroupList", ActivityVhclMaterialGroupList);
		request.setAttribute("ActivitygetActivityAgeList", ActivitygetActivityAgeList);
		request.setAttribute("ActivityCharactorList", ActivityCharactorList);
		act.setOutData("ActivityMileageList", ActivityMileageList);
	}
	
	public void cancelAcSure(){
		int res=activityservice.cancelAcSure(request);
		super.setJsonSuccByres(res);
	}
	public void relationShow(){
		String flag = getParam("query");
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.relationShow(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "activity_relation_show", "服务活动主题管理");
	}
	public void activityUpdateInit(){
		Map<String, Object> map= activityservice.findActivity(request);
		act.setOutData("po", map);
		getDataById("update");
		sendMsgByUrl("activity", "activity_manage_add", "新服务活动修改");
	}
	public void activityView() throws Exception{
		Map<String, Object> map= activityservice.findActivity(request);
		getBtnInfo();
		act.setOutData("po", map);
		getDataById("view");
		sendMsgByUrl("activity", "activity_manage_add", "新服务活动明细");
	}
	public void activityList(){
		String flag = getParam("query");
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.activityList(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "activity_manage_list", "服务活动管理");
	}
	
	public void activityAddSure(){
		String identify = DaoFactory.getParam(request, "identify");
		act.setOutData("identify", identify);
		int res=activityservice.activityAddSure(request,loginUser);
		setJsonSuccByres(res);
	}
	
	public void findDatatemplet(){
		getDataById("view");
	}
	
	public void showSubject(){
		String flag = getParam("query");
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.showSubject(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "activity_show_subject", "新服务活动主题选择");
	}
	
	public void activityAdd(){
		request.setAttribute("type", "add");
		sendMsgByUrl("activity", "activity_manage_add", "新服务活动新增");
	}
	
	public void deleteTemplet(){
		int res=activityservice.deleteTemplet(request);
		setJsonSuccByres(res);
	}
	public void templetPublish(){
		int res=activityservice.templetPublish(request);
		setJsonSuccByres(res);
	}
	public void updateTempletInit(){
		getDataById("update");
		sendMsgByUrl("activity", "activity_templet_add", "服务活动模板修改");
	}
	public void templetView(){
		getDataById("view");
		sendMsgByUrl("activity", "activity_templet_add", "服务活动模板明细");
	}
	public void templetAdd(){
		request.setAttribute("type", "add");
		sendMsgByUrl("activity", "activity_templet_add", "服务活动模板新增");
	}
	public void templetAddSure(){
		int res=activityservice.templetAddSure(request,loginUser);
		String identify = DaoFactory.getParam(request, "identify");
		act.setOutData("identify", identify);
		setJsonSuccByres(res);
	}
	public void activityTemplet(){
		String flag = getParam("query");
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.activityTemplet(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "activity_templet", "服务活动模板管理");
	}
	/**
	 * 服务活动VIN管理
	 */
	public void ActivityVinManage(){
		String flag = getParam("query");
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.ActivityVinManageData(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "activityVinManage", "服务活动VIN管理");
	}
	public void ActivityVinData(){
		String flag = getParam("query");
		AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
		List<Map<String, Object>> orgList;
		try {
			orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String activity_id = getParam("activity_id");
		request.setAttribute("activity_id", activity_id);
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.ActivityVinData(request,100,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "activityVinData", "服务活动VIN管理");
	}
	/**
	 * 删除服务活动VIN数据
	 */
	public void bntDelAll(){
		int res=activityservice.bntDelAll(request);
		setJsonSuccByres(res);
	}
	public void expotActivityVinData(){
		PageResult<Map<String, Object>> list=activityservice.ActivityVinData(request,Constant.PAGE_SIZE_MAX,getCurrPage());
		activityservice.expotActivityVinData(act,list);
	}
	public void openSubject(){
		PageResult<Map<String, Object>> list=activityservice.openSubject(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
		sendMsgByUrl("activity", "subjectName", "服务活动主题管理");
	}
	public void openActivity(){
		PageResult<Map<String, Object>> list=activityservice.openActivity(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
		sendMsgByUrl("activity", "activityName", "服务活动管理");
	}
	/**
	 * 经销商服务活动VIN总数查询
	 */
	public void vinByDetail(){
		String flag = getParam("query");
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.vinByDetail(getCurrDealerId(),request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "vinByDetail", "服务活动VIN管理");
	}
	/**
	 * 经销商服务活动VIN明细查询
	 */
	public void vinDetailByCount(){
		String flag = getParam("query");
		String activity_code = getParam("activity_code");
		String dealer_code = getParam("dealer_code");
		request.setAttribute("activity_code", activity_code);
		request.setAttribute("dealer_code", dealer_code);
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.vinDetailByCount(getCurrDealerId(),request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "vinDetailByCount", "服务活动VIN管理");
	}
	/**
	 * 车厂服务活动VIN明细查询
	 */
	public void vinDetailByCountFactory(){
		String flag = getParam("query");
		String activity_code = getParam("activity_code");
		String dealer_code = getParam("dealer_code");
		request.setAttribute("activity_code", activity_code);
		request.setAttribute("dealer_code", dealer_code);
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.vinDetailByCountFactory(dealer_code,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "vinDetailByCountFactory", "服务活动VIN管理");
	}
	/**
	 * 车厂服务活动VIN查询（根据服务站代码和活动代码分组）
	 */
	public void vinDetailByautomobile(){
		String flag = getParam("query");
		if("true".equals(flag)){
			PageResult<Map<String, Object>> list=activityservice.vinDetailByautomobile(getCurrDealerId(),request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("activity", "vinDetailByautomobile", "服务活动VIN管理");
	}
	/**
	 * 得到模板的数据
	 * @param type
	 */
	private void getDataById(String type) {
		List<Map<String, Object>> list1 =activityservice.findTempletMain(request);
		List<Map<String, Object>> list2 =activityservice.findTempletLab(request);
		List<Map<String, Object>> list3 =activityservice.findTempletPart(request);
		List<Map<String, Object>> list4 =activityservice.findTempletAcc(request);
		List<Map<String, Object>> list5 =activityservice.findTempletCom(request);
		if(checkListNull(list1)){
			act.setOutData("t", list1.get(0));
		}
		if(checkListNull(list2)){
			act.setOutData("labours", list2);	
		}
		if(checkListNull(list3)){
			act.setOutData("parts", list3);
		}
		if(checkListNull(list4)){
			act.setOutData("acc", list4);
		}
		if(checkListNull(list5)){
			act.setOutData("com", list5.get(0));
		}
		request.setAttribute("type", type);
	}
	/**
	 * 服务活动VIN明细展示 导出
	 */
	 public void exportToexcel(){
		 activityservice.exportToexcel(act,getCurrDealerId(),request,Constant.PAGE_SIZE_MAX,getCurrPage());
		
	}
	 
	 public void activityByvin(){
		 sendMsgByUrl("activity", "activity_Byvin_list", "服务活动VIN管理");
	 }
	 
	 public void QueryactivityByvin(){
		 PageResult<Map<String, Object>> list = activityservice.QueryactivityByvin(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
		 act.setOutData("ps", list);
	 }
}
