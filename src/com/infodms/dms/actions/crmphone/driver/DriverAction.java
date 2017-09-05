package com.infodms.dms.actions.crmphone.driver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.service.DriverAuditService;
import com.infodms.dms.service.impl.DriverAuditServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DriverAction extends BaseAction {
	
	/**
	 * 司机注册
	 */
	public void driverRegister() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			DriverAuditService service  = new DriverAuditServiceImpl();
			String carrier_id = DaoFactory.getParam(request, "carrier_id");
			String driver_name = DaoFactory.getParam(request, "driver_name");
			String driver_phone = DaoFactory.getParam(request, "driver_phone");
			String password = DaoFactory.getParam(request, "password");
			if(StringUtils.isEmpty(carrier_id)||StringUtils.isEmpty(driver_phone)||StringUtils.isEmpty(driver_name)||StringUtils.isEmpty(password)){
				map.put("errcode", "参数错误");
			}else {
				String res = service.driverRegister(request);
				map.put("errcode", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("司机注册接口异常================"+e.getMessage());
			map.put("errcode", "司机注册接口异常");
		}
		JSONObject jsonObject = new JSONObject();
		JSONObject object = jsonObject.fromObject(map);
		act.setOutData("\"message\"", object.toString());
	}
	/**
	 * 获取承运商接口
	 */
	public void getTtSalesLogi() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		try {
		    DriverAuditService service  = new DriverAuditServiceImpl();
			List<Map<String, Object>> list = service.getTtSalesLogi(request);
			JSONArray object = JSONArray.fromObject(list);
			Map<String,Object> map  =  new HashMap<String, Object>();
			map.put("errcode", "0");
			map.put("list", object);
			JSONObject jsonObject = new JSONObject();
			JSONObject fromObject = jsonObject.fromObject(map);
			logger.info(fromObject+"===============");
			act.setOutData("\"message\"", fromObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
			act.setOutData("errcode", "接口异常");
		}
	}
	/**
	 * 司机注册校验
	 */
	@SuppressWarnings("static-access")
	public void driverCalibration() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			DriverAuditService service  = new DriverAuditServiceImpl();
			String driver_phone = DaoFactory.getParam(request, "driver_phone");
			if(StringUtils.isEmpty(driver_phone)){
				map.put("errcode", "参数错误");
			}else {
				String res = service.driverCalibration(request);
				if("SUCCESS".equals(res)){
					map.put("errcode", "0");
				}else{
					map.put("errcode", "1004");
					map.put("err_message", "该手机号已经注册过了！");
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("司机注册接口异常================"+e.getMessage());
			map.put("errcode", "1003");
			map.put("err_message", "司机注册接口异常");
		}
		JSONObject jsonObject = new JSONObject();
		JSONObject object = jsonObject.fromObject(map);
		act.setOutData("\"message\"", object.toString());
	}
	
	/**
	 * 司机登陆
	 */
	@SuppressWarnings("static-access")
	public void driverLogin() {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			DriverAuditService service  = new DriverAuditServiceImpl();
			String driver_phone = DaoFactory.getParam(request, "driver_phone");
			String password = DaoFactory.getParam(request, "password");
			if(StringUtils.isEmpty(driver_phone)||StringUtils.isEmpty(password)){
				map.put("errcode", "1003");
				map.put("err_message", "参数错误");
			}else {
				TcUserPO po = service.driverLogin(request);
				if(null!=po){
					map.put("errcode", "0");
					map.put("user_id", po.getUserId());
					//根据userId获取承运商ID
					Map<String, Object> smap=service.getLogiIdByUserId(po.getUserId().toString());
					if(smap==null){
						map.put("errcode", "1005");
						map.put("err_message", "用户名或者密码错误");
					}else{
						map.put("logi_id", smap.get("LOGI_ID").toString());
					}
				}else{
					map.put("errcode", "1005");
					map.put("err_message", "用户名或者密码错误");
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("司机登陆接口异常================"+e.getMessage());
			map.put("errcode", "1003");
			map.put("err_message", "接口异常");
		}
		JSONObject jsonObject = new JSONObject();
		JSONObject object = jsonObject.fromObject(map);
		act.setOutData("\"message\"", object.toString());
	}

}
