package com.infodms.yxdms.action;

import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.oldPart.EmergencyDeviceDao;
import com.infodms.yxdms.utils.BaseAction;
import com.infoservice.po3.bean.PageResult;

public class ApplicationAction extends BaseAction{

	private PageResult<Map<String, Object>> list = null;
	private final EmergencyDeviceDao dao = EmergencyDeviceDao.getInstance();
	
	public void showCarType(){
		String query = getParam("query");
		if ("true".equals(query)) {
			list=dao.listMeterialGroup(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps",list);
		}else{
			sendMsgByUrl("showJsp", "show_car_type", "通用显示车型jsp");
		}
	}
	
}
