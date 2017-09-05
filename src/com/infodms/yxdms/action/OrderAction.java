package com.infodms.yxdms.action;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.dao.OrderDAO;
import com.infodms.yxdms.service.OrderService;
import com.infodms.yxdms.service.impl.OrderServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;
/**
 * 工单action
 * @author yuewei
 *
 */
public class OrderAction extends BaseAction{

	private OrderService orderservice=new OrderServiceImpl();
	PageResult<Map<String, Object>> list=null;
	
	
	public void roBalancePrint(){
		List<Map<String, Object>> labours = orderservice.findLaboursById(request, null);
		List<Map<String, Object>> parts = orderservice.findPartsById(request, null);
		List<Map<String, Object>> acc = orderservice.findAccById(request);
		Map<String, Object> ro = orderservice.findRepairById(request);
		List<Map<String,Object>> outData= orderservice.findOutById(request);
		if(checkListNull(outData)){
			act.setOutData("outData", outData.get(0));
		}
		if(checkListNull(acc)){
			act.setOutData("acc", acc);
		}
		if(checkListNull(labours)){
			act.setOutData("labours", labours);
		}
		if(checkListNull(parts)){
			act.setOutData("parts", parts);
		}
		act.setOutData("ro", ro);
		sendMsgByUrl("order", "ro_balance_print", "工单打印界面");
	}
	
	public void checkActivityByVin(){
		Map<String,String> map=orderservice.checkActivityByVin(request);
		act.setOutData("map", map);
	}
	
	public void doActivity(){
		String flag = getParam("query");
		request.setAttribute("in_mileage", getParam("in_mileage"));
		request.setAttribute("vin", getParam("vin"));
		request.setAttribute("dealerCode", loginUser.getDealerCode());
		if("true".equals(flag)){
			list=orderservice.doActivity(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("order", "ro_do_activity", "服务活动查询页面");
	}
	
	public void orderList(){
		String flag = getParam("query");
		if("true".equals(flag)){
			list=orderservice.orderList(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("order", "ro_list", "工单列表查询页面");
	}
	public void orderAdd(){
		Map<String,Object> data=orderservice.findLoginUserInfo(loginUser.getUserId());
		act.setOutData("userInfo",data);
		request.setAttribute("type", "add");
		sendMsgByUrl("order", "ro_add", "工单新增页面");
	}
	public void chooseRoType(){
		List<Map<String, Object>> list=orderservice.chooseRoType(request);
		act.setOutData("list", list);
		sendMsgByUrl("order", "choose_ro_type", "工单新增页面选择类型");
	}
	public void addLabour(){
		String flag = getParam("query");
		String wrgroup_id = getParam("wrgroup_id");
		request.setAttribute("wrgroup_id", wrgroup_id);
		String package_id = getParam("package_id");
		String val = getParam("val");
		request.setAttribute("package_id", package_id);
		request.setAttribute("dealer_id", getCurrDealerId());
		request.setAttribute("val", val);
		request.setAttribute("labour_codes", getParam("labour_codes"));
		request.setAttribute("labour_codes_1", getParam("labour_codes_1"));
		request.setAttribute("labour_codes_3", getParam("labour_codes_3"));
		request.setAttribute("part_id_3", getParam("part_id_3"));
		request.setAttribute("part_id_1", getParam("part_id_1"));
		if("true".equals(flag)){
			list=orderservice.addLabour(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("order", "add_labour", "工单列表查询页面");
	}
	public void addPart(){
		String flag = getParam("query");
		String model_id = getParam("model_id");
		request.setAttribute("model_id", model_id);
		String series_id = getParam("series_id");
		String val = getParam("val");
		request.setAttribute("series_id", series_id);
		request.setAttribute("dealer_id", getCurrDealerId());
		request.setAttribute("val", val);
		request.setAttribute("IS_SPJJ", getParam("IS_SPJJ"));
		request.setAttribute("part_id_1", DaoFactory.getParam(request, "part_id_1"));
		request.setAttribute("part_id_3", DaoFactory.getParam(request, "part_id_3"));
		if("true".equals(flag)){
			list=orderservice.addPart(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("order", "add_part", "工单列表查询页面");
	}
	public void showInfoByVin(){
		response.setContentType("application/json");
		boolean flag=orderservice.checkIsRoEndByVin(request);//判断是否有未结算的，true为可以开，false为不可以开单
		setJsonSuccByres(flag);
		if(flag){
			Map<String,Object> data =orderservice.showInfoByVin(request);
			act.setOutData("info", data);
			Map<String, String> map=orderservice.checkActivityByVin(request);
			act.setOutData("map", map);
		}
	}
	public void roInsert(){
		loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		int res = orderservice.roInsert(request,loginUser);
		setJsonSuccByres(res);
		if(res==1){
			sendMsgByUrl("order", "ro_list", "工单列表查询页面");
		}
	}
	
	public void sureYj()
	{
		//11801002
		OrderDAO orderDAO = new OrderDAO();
		String[] part_code_1 = DaoFactory.getParams(request, "part_code_1");
		String[] pay_type_1 = DaoFactory.getParams(request, "pay_type_1");
		String vin = DaoFactory.getParam(request, "vin");
		StringBuffer sql= new StringBuffer();
		sql.append("select\n" );
		sql.append("  t.cur_days,\n" );
		sql.append("  case when  t.cur_days >=25 and  t.cur_days < 28 then '一级'\n" );
		sql.append("  when   t.cur_days >=28 and  t.cur_days < 32 then '二级'\n" );
		sql.append("  when  t.cur_days >=32 then '三级'\n" );
		sql.append("  end vinRule\n" );
		sql.append(" from tt_as_wr_vin_repair_days t where t.vin = '"+vin+"'");
		String Yj = "";
		Map<String, Object> vinMap = orderDAO.pageQueryMap(sql.toString(), null,orderDAO.getFunName() );
			if(vinMap != null &&  vinMap.get("vinRule") != null && vinMap.get("vinRule").toString().length() > 1)
				Yj = "VIN:"+vin + "已经达到" + vinMap.get("vinRule").toString()+"预警" + " 次数为:"+vinMap.get("CUR_DAYS").toString()+"\n";
		if(pay_type_1 != null)
		{
			for(int i = 0 ;i < pay_type_1.length ; i++)
			{
				if(pay_type_1[i].equals("11801002"))
				{
				    sql= new StringBuffer();
					sql.append("select vr_warranty from tt_as_wr_vin_rule t where\n" );
					sql.append(" t.vr_type = 94021002\n" );
					sql.append(" and  t.vr_part_code = '"+part_code_1[i]+"'\n" );
					sql.append(" order by t.vr_warranty");
					List<Map<String,Object>>  list = orderDAO.pageQuery(sql.toString(), null, orderDAO.getFunName() );
					if(list.size() == 0)
					{
						sql= new StringBuffer();
						sql.append("select\n" );
						sql.append("  t.cur_times,\n" );
						sql.append("  case when t.cur_times = 2 then '一级'\n" );
						sql.append("  when t.cur_times = 3 then '二级'\n" );
						sql.append("  when  t.cur_times > 3 then '三级'\n" );
						sql.append("  end  partRule\n" );
						sql.append("  from tt_as_wr_vin_part_repair_times t where t.vin = '"+vin+"'\n" );
						sql.append("and t.part_code = '"+part_code_1[i]+"'");
						Map<String, Object> map = orderDAO.pageQueryMap(sql.toString(), null,orderDAO.getFunName() );
						if(map != null &&  map.get("PARTRULE") != null && map.get("PARTRULE").toString().length() > 0)
								Yj = Yj + "配件:"+part_code_1[i]+"已经达到" +map.get("PARTRULE").toString()  + " 次数为:" + map.get("CUR_TIMES").toString()  +"\n";
	                       
					}else
					{
					    sql= new StringBuffer();
						sql.append("select\n" );
						sql.append("  t.cur_times,\n" );
						sql.append("  case when t.cur_times = "+list.get(0).get("VR_WARRANTY").toString()+" then '一级'\n" );
						sql.append("  when t.cur_times = "+list.get(1).get("VR_WARRANTY").toString()+" then '二级'\n" );
						sql.append("  when  t.cur_times > "+list.get(2).get("VR_WARRANTY").toString()+" then '三级'\n" );
						sql.append("  end  partRule\n" );
						sql.append("  from tt_as_wr_vin_part_repair_times t where t.vin = '"+vin+"'\n" );
						sql.append("and t.part_code = '"+part_code_1[i]+"'");
						Map<String, Object> map = orderDAO.pageQueryMap(sql.toString(), null,orderDAO.getFunName() );
						if(map != null && map.get("PARTRULE") != null && map.get("PARTRULE").toString().length() > 0)
							Yj = Yj + "配件:"+part_code_1[i]+"已经达到" +map.get("PARTRULE").toString()  + " 次数为:" + map.get("CUR_TIMES").toString()  +"\n";
					}

				}
			}
		}
		
		act.setOutData("Yj", Yj);
		
		
	}
	
	
	public void orderUpdateInit(){
		Map<String,Object> data=orderservice.findLoginUserInfo(loginUser.getUserId());
		act.setOutData("userInfo",data);
		Map<String,Object> ro=orderservice.findRepairById(request);
		List<Map<String,Object>> labours1= orderservice.findLaboursById(request,"93331001");
		List<Map<String,Object>> parts1= orderservice.findPartsById(request,"93331001");
		List<Map<String,Object>> labours2= orderservice.findLaboursById(request,"93331002");
		List<Map<String,Object>> parts2= orderservice.findPartsById(request,"93331002");
		List<Map<String,Object>> labours3= orderservice.findLaboursById(request,"93331003");
		List<Map<String,Object>> parts3= orderservice.findPartsById(request,"93331003");
		List<Map<String,Object>> acc= orderservice.findAccById(request);
		List<Map<String,Object>> outData= orderservice.findOutById(request);
		if(checkListNull(outData)){
			act.setOutData("outData", outData);
		}
		if(checkListNull(acc)){
			act.setOutData("acc", acc);
		}
		if(checkListNull(labours1)){
			act.setOutData("labours1", labours1);
		}
		if(checkListNull(labours2)){
			act.setOutData("labours2", labours2);
		}
		if(checkListNull(labours3)){
			act.setOutData("labours3", labours3);
		}
		if(checkListNull(parts1)){
			act.setOutData("parts1", parts1);
		}
		if(checkListNull(parts2)){
			act.setOutData("parts2", parts2);
		}
		if(checkListNull(parts3)){
			act.setOutData("parts3", parts3);
		}
		act.setOutData("ro", ro);
		request.setAttribute("type", "update");
		sendMsgByUrl("order", "ro_add", "工单新增页面");
	}
	public void orderDelete(){
		String msg=orderservice.orderDelete(request);
		act.setOutData("msg", msg);
	}
	public void orderView(){
		Map<String,Object> data=orderservice.findLoginUserInfo(loginUser.getUserId());
		act.setOutData("userInfo",data);
		Map<String,Object> ro=orderservice.findRepairById(request);
		List<Map<String,Object>> labours1= orderservice.findLaboursById(request,"93331001");
		List<Map<String,Object>> parts1= orderservice.findPartsById(request,"93331001");
		List<Map<String,Object>> labours2= orderservice.findLaboursById(request,"93331002");
		List<Map<String,Object>> parts2= orderservice.findPartsById(request,"93331002");
		List<Map<String,Object>> labours3= orderservice.findLaboursById(request,"93331003");
		List<Map<String,Object>> parts3= orderservice.findPartsById(request,"93331003");
		List<Map<String,Object>> acc= orderservice.findAccById(request);
		List<Map<String,Object>> outData= orderservice.findOutById(request);
		if(checkListNull(outData)){
			act.setOutData("outData", outData);
		}
		if(checkListNull(acc)){
			act.setOutData("acc", acc);
		}
		if(checkListNull(labours1)){
			act.setOutData("labours1", labours1);
		}
		if(checkListNull(labours2)){
			act.setOutData("labours2", labours2);
		}
		if(checkListNull(labours3)){
			act.setOutData("labours3", labours3);
		}
		if(checkListNull(parts1)){
			act.setOutData("parts1", parts1);
		}
		if(checkListNull(parts2)){
			act.setOutData("parts2", parts2);
		}
		if(checkListNull(parts3)){
			act.setOutData("parts3", parts3);
		}
		act.setOutData("ro", ro);
		String view = CommonUtils.checkNull(request.getParamValue("view"));
		if(null==view || "".equals(view))
			view = "view";
		request.setAttribute("type", view);
		sendMsgByUrl("order", "ro_add", "工单明细页面");
	}
	public void orderBalanceList(){
		String flag = getParam("query");
		if("true".equals(flag)){
			list=orderservice.orderList(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("order", "ro_balance_list", "工单列表查询页面");
	}
	public void accList() {
		String flag = getParam("query");
		if("true".equals(flag)){
			list= orderservice.accList(request,Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("order", "ro_acc_show", "辅料费选择页面");
	}
	public void keepFitTemplate(){
		request.setAttribute("vin", getParam("vin"));
		request.setAttribute("in_mileage", getParam("in_mileage"));
		sendMsgByUrl("maintain", "choose_keepFit_list", "免费保养模板维护选择页面");
	}
	public void checkMileage(){
		String res=orderservice.checkMileage(request);
		act.setOutData("res", res);
	}
	/**
	 * 模糊查询根据车牌号
	 */
	public void showInfoBylicenseNo(){
		List<Map<String,Object>>list= orderservice.showInfoBylicenseNo(request,loginUser);
		act.setOutData("licenseData", list);
	}
	/**
	 * 模糊查询
	 */
	public void findVinListByVin(){
		List<Map<String,Object>> list=orderservice.findVinListByVin(request);
		act.setOutData("vinData", list);
	}
	/**
	 * 判断是否三包期
	 */
	public void getGuaFlag() {
		String partCode = request.getParamValue("partCode");
		String vin = request.getParamValue("vin");
		String inMileage = request.getParamValue("inMileage");
		String purchasedDate = request.getParamValue("purchasedDate"); // 保修开始时间
		Map<String, Object> map= orderservice.getGuaFlag(partCode,vin,inMileage,purchasedDate);
		act.setOutData("map", map);
	}
	/**
	 * 判断件是否维护三包
	 */
	public void checkaddPart(){
		List<Map<String,Object>> list = orderservice.checkaddPart(request,loginUser);
		if (null==list || list.size()==0) {
			setJsonSuccByres(-1);
		}
		
	}
}
