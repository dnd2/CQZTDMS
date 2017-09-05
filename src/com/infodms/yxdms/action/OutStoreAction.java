package com.infodms.yxdms.action;

import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.yxdms.service.OutStoreService;
import com.infodms.yxdms.service.impl.OutStoreServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;

public class OutStoreAction extends BaseAction{

	private OutStoreService outstoreservice = new OutStoreServiceImpl();
	private PageResult<Map<String, Object>> list = null;
	
	/**
	 * 汇总出库链接配件代码
	 */
	public void linkByPartCode(){
		String partCode = getParam("partCode");
		List<Map<String, Object>> list= outstoreservice.linkByPartCode(partCode);
		act.setOutData("partList", list);
		sendMsgByUrl("outstore", "link_by_part_code", "汇总出库根据配件代码批量修改供应商");
	}
	
	public void showSupply(){
		request.setAttribute("yieldly", DaoFactory.getParam(request, "yieldly"));
	}
	
	public void showOutStoreNumByCondition(){
		Map<String, Double> map= outstoreservice.showOutStoreNumByCondition(request);
		act.setOutData("mapNum", map);
	}
	public void expotDataOldAudit(){
		outstoreservice.expotDataOldAudit(act,request, Constant.PAGE_SIZE_MAX, getCurrPage());
	}
	public void gotoDiyOutPartInit(){
		sendMsgByUrl("outstore", "diy_out_part", "手工添加退赔单");
	}
	public void diyOutPartSure(){
		int res=outstoreservice.diyOutPartSure(request,loginUser);
		setJsonSuccByres(res);
	}
	public void auditAllRebut(){
		int res=outstoreservice.auditAllRebut(request);
		setJsonSuccByres(res);
	}
}
