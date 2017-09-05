package com.infodms.dms.actions.crmphone.ontheway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infodms.dms.service.ontheway.OnTheWayService;
import com.infodms.dms.service.ontheway.OnTheWayServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;

import net.sf.json.JSONObject;
/**
 * 在途
 * @author ljie
 *
 */
public class OnTheWayPhoneAction extends BaseAction {
	
	/**
	 * 获取交接单列表
	 */
	@SuppressWarnings("static-access")
	public void getTtSalesWaybill() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		OnTheWayService service  =  new OnTheWayServiceImpl();
		String page_size = DaoFactory.getParam(request, "page_size");
		String curr_page = DaoFactory.getParam(request, "curr_page");
		Map<String,Object>  map = new HashMap<String, Object>();
		JSONObject json = new JSONObject();
		if(StringUtils.isEmpty(page_size)||StringUtils.isEmpty(curr_page)){
			map.put("errcode", "参数错误");
		}else {
		  PageResult<Map<String, Object>> list = service.getTtSalesWaybill(request,Integer.valueOf(page_size),Integer.valueOf(curr_page));
		  List<Map<String, Object>> records = list.getRecords();
		  map.put("total", list.getTotalRecords());
		  map.put("list", records);
		  map.put("errcode", "0");
		}
		JSONObject fromObject = json.fromObject(map);
		act.setOutData("\"message\"", fromObject.toString());
	}
	
	/**
	 * 获取交接单明细列表
	 */
	@SuppressWarnings("static-access")
	public void getTtSalesWayBillDtl() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		OnTheWayService service  =  new OnTheWayServiceImpl();
//		String page_size = DaoFactory.getParam(request, "page_size");
//		String curr_page = DaoFactory.getParam(request, "curr_page");
		Map<String,Object>  map = new HashMap<String, Object>();
		JSONObject json = new JSONObject();
//		if(StringUtils.isEmpty(page_size)||StringUtils.isEmpty(curr_page)){
//			map.put("errcode", "参数错误");
//		}else {
		  List<Map<String, Object>> list = service.getTtSalesWayBillDtl(request);
		  Map<String, Object> map2 =  service.getTtSalesWaybillByBillId(request);
		  map.put("list", list);
		  map.put("HandInfo", map2);
		  map.put("errcode", "0");
//		}
		JSONObject fromObject = json.fromObject(map);
		act.setOutData("\"message\"", fromObject.toString());
	}
	/**
	 * 交接单明细状态修改(绑定车辆)
	 */
	@SuppressWarnings("static-access")
	public void handOverSlipStatus() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		OnTheWayService service  =  new OnTheWayServiceImpl();
		Map<String,Object>  map = new HashMap<String, Object>();
		String dtl_id = DaoFactory.getParam(request, "dtl_id");
		String user_id = DaoFactory.getParam(request, "user_id");
		if(StringUtils.isEmpty(user_id)||StringUtils.isEmpty(dtl_id)){
			map.put("errcode", "1003");
			map.put("errcode_message", "参数错误");
		}else{
			try {
				String res =  service.updateTtSalesWayBillDtlStatusByDtlId(request);
				if("SUCCESS".equals(res)){
					map.put("errcode", "0");
				}else {
					map.put("errcode", "1001");
					map.put("errcode_message", "该记录已经扫描过");
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("errcode", "1002");
				map.put("errcode_message", "接口异常");
			}
		}
		JSONObject json = new JSONObject();
		JSONObject fromObject = json.fromObject(map);
		act.setOutData("\"message\"", fromObject.toString());
	}
	/**
	 * 交接单明细状态修改(接触绑定车辆)
	 */
	@SuppressWarnings("static-access")
	public void unhandOverSlipStatus() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		OnTheWayService service  =  new OnTheWayServiceImpl();
		Map<String,Object>  map = new HashMap<String, Object>();
		String dtl_id = DaoFactory.getParam(request, "dtl_id");
		String user_id = DaoFactory.getParam(request, "user_id");
		if(StringUtils.isEmpty(user_id)||StringUtils.isEmpty(dtl_id)){
			map.put("errcode", "1003");
			map.put("errcode_message", "参数错误");
		}else{
			try {
				String res =  service.updateUnTtSalesWayBillDtlStatusByDtlId(request);
				if("SUCCESS".equals(res)){
					map.put("errcode", "0");
				}else {
					map.put("errcode", "1001");
					map.put("errcode_message", "该车不能解绑");
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("errcode", "1002");
				map.put("errcode_message", "接口异常");
			}
		}
		JSONObject json = new JSONObject();
		JSONObject fromObject = json.fromObject(map);
		act.setOutData("\"message\"", fromObject.toString());
	}
	
	
	/**
	 * 位置上报
	 */
	@SuppressWarnings("static-access")
	public void driverLocationReport() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		OnTheWayService service  =  new OnTheWayServiceImpl();
		Map<String,Object>  map = new HashMap<String, Object>();
		String dtl_id =  DaoFactory.getParam(request, "dtl_id");
		String address = DaoFactory.getParam(request, "address");
		String user_id = DaoFactory.getParam(request, "user_id");
		if(StringUtils.isEmpty(dtl_id)||StringUtils.isEmpty(address)||StringUtils.isEmpty(user_id)){
			map.put("errcode", "1003");
			map.put("errmessage", "参数错误");
			
		}else{
			try {
				String res = service.driverLocationReport(request);
				if("SUCCESS".equals(res)){
					map.put("errcode", "0");
					map.put("errmessage", "上报成功");
				}else {
					map.put("errcode", "1001");
					map.put("errmessage", "上报报失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("errcode", "1002");
				map.put("errmessage", "位置上报异常");
			}
		}
		JSONObject json = new JSONObject();
		JSONObject fromObject = json.fromObject(map);
		act.setOutData("\"message\"", fromObject.toString());
	}
	/**
	 * 交车上报
	 */
	@SuppressWarnings("static-access")
	public void leaveCarLocationReport() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		OnTheWayService service  =  new OnTheWayServiceImpl();
		Map<String,Object>  map = new HashMap<String, Object>();
		String dtl_id =  DaoFactory.getParam(request, "dtl_id");
		String address = DaoFactory.getParam(request, "address");
		String user_id = DaoFactory.getParam(request, "user_id");
		String bill_id = DaoFactory.getParam(request, "bill_id");
		if(StringUtils.isEmpty(dtl_id)||StringUtils.isEmpty(address)||StringUtils.isEmpty(user_id)||StringUtils.isEmpty(bill_id)){
			map.put("errcode", "1003");
			map.put("errmessage", "参数错误");
			
		}else{
			try {
				String res = service.leaveCarLocationReport(request);
				if("SUCCESS".equals(res)){
					map.put("errcode", "0");
					map.put("errmessage", "上报成功");
				}else {
					map.put("errcode", "1001");
					map.put("errmessage", "上报失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("errcode", "1002");
				map.put("errmessage", "位置上报异常");
			}
		}
		JSONObject json = new JSONObject();
		JSONObject fromObject = json.fromObject(map);
		act.setOutData("\"message\"", fromObject.toString());
	}
	

}
