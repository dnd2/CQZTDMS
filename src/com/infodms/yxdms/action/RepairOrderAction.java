package com.infodms.yxdms.action;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.service.RepairOrderService;
import com.infodms.yxdms.service.impl.RepairOrderServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infoservice.po3.bean.PageResult;

public class RepairOrderAction extends BaseAction{
	
	private   RepairOrderService orderService = new RepairOrderServiceImpl();
	private final ApplicationDao dao = ApplicationDao.getInstance();

	public void   orderWastList(){
		sendMsgByUrl("order", "order_wast_list", "工单废弃查询主页面");
	}
	public void  queryRepairOrderDelet(){
		PageResult<Map<String, Object>> ps =  orderService.queryRepairOrderDelet(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", ps);
	
	}
	/**
	 * 维修工单主页面查询
	 */
	public void   repairOrderQuery(){
		List<Map<String, Object>>   ps =  orderService.ModelcodeQuery(request,loginUser,Constant.PAGE_SIZE_MAX,getCurrPage());
		act.setOutData("model",ps);
		sendMsgByUrl("order", "ro_Maintain_list", "维修工单查询主页面");
	}
	public void   repairOrderQuerylist(){
		CommonUtilActions commonUtilActions = new CommonUtilActions();
		String action = CommonUtils.checkNull(request.getParamValue("action"));
		if ("query".equals(action)) {
			PageResult<Map<String, Object>>   ps =  orderService.repairOrderQuery(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", ps);
		}else {
			//车型
			try {
				act.setOutData("startDate", DateUtil.getFirstMonthDay(1));
				act.setOutData("endDate", DateUtil.getYesToDay());
				act.setOutData("modelList", commonUtilActions.getAllModel());
				sendMsgByUrl("order", "ro_Maintain_list", "维修工单查询主页面");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 维修工单数据导出
	 */
	public void  repairOrderQueryExport(){
		PageResult<Map<String, Object>> list = orderService.repairOrderQuery(request,loginUser, Constant.PAGE_SIZE_MAX, getCurrPage());
		dao.repairOrderQueryExport(act, list);
	}
	/**
	 * 根据vin查询工单
	 */
	public void  OrderByVindata(){
	    String	flag  = getParam("query");
	    String	vin  =getParam("vin");
	    if ("true".equals(flag)) {
	    	PageResult<Map<String, Object>> list = orderService.OrderByVindata(request,loginUser, Constant.PAGE_SIZE, getCurrPage());
	    	act.setOutData("ps",list);
	    }else {
	    	act.setOutData("vin",vin);
	    	sendMsgByUrl("activity", "actity_OrderByVindata_list", "服务活动展示");
		}
	    
	}
}
