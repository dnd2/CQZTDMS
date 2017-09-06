package com.infodms.dms.actions.sales.storage.balancemanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.service.balancemanage.SalesBalanceService;
import com.infodms.dms.service.balancemanage.SalesBalanceServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PageResult;
/**
 * 销售结算管理
 * @author ljie
 *
 */
public class SalesBalanceAction extends BaseAction {
	private final String BILL_MANAGE_LIST_URL = "/jsp/sales/storage/balance/bill_manage_list.jsp";
	private final String BILL_QUERY_LIST_URL = "/jsp/sales/storage/balance/bill_query_list.jsp";
	private final String BILL_DETAIL_PAGE = "/jsp/sales/storage/balance/balanceViewDel.jsp";
	private final String QUERY = "query";
	private final String DOWNLOAD="download";
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	/**
	 * 挂帐单管理
	 */
	public void billManageList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		String type = DaoFactory.getParam(request, "query");
		try {
			if (QUERY.equals(type)) {
				SalesBalanceService service = new SalesBalanceServiceImpl();
				PageResult<Map<String, Object>> result =  service.getTtSalesBalance(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
			    act.setOutData("ps", result);
			}else {
				List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
				if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
				{
					list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
				}else{
					list_logi=reLDao.getLogiByArea(areaIds);
				}
				act.setOutData("list_logi", list_logi);//物流商LIST
				if(list_logi != null && list_logi.size() > 0)
				{
					Map<String, Object> obj = list_logi.get(0);
					act.setOutData("LOGI_NAME", obj.get("LOGI_NAME"));
				}
				act.setForword(BILL_MANAGE_LIST_URL);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 挂账单查询
	 */
	public void billQueryList() {
		String type = DaoFactory.getParam(request, "query");
		try {
			if (QUERY.equals(type)) {
				SalesBalanceService service = new SalesBalanceServiceImpl();
				PageResult<Map<String, Object>> result =  service.getTtSalesBalanceQuery(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
			    act.setOutData("ps", result);
			}else if (DOWNLOAD.equals(type)) {//导出查询结果
				SalesBalanceService service = new SalesBalanceServiceImpl();
				List<Map<String, Object>> list = service.getTtSalesBalanceExport(request,loginUser);
				String [] head={"交接单号","挂帐单号","承运商","承运商代码","挂账单挂账数量","挂账日期","挂账月份","详细收货地址","结算地址","状态","挂帐单金额","交接单挂账金额"};
				String [] cols={"BILL_NO","BAL_NO","LOGI_NAME","LOGI_CODE","BAL_COUNT","BAL_DATE","BAL_MONTH","ADDRESS_INFO","BALANCE_ADDRESS","STATUS_NAME","BAL_AMOUNT","BILL_AMOUNT"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "挂帐单查询列表.xls",head,cols,list);
			}else {
				AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				String areaIds = MaterialGroupManagerDao
				.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
				List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
				if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
				{
					list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
				}else{
					list_logi=reLDao.getLogiByArea(areaIds);
				}
				act.setOutData("list_logi", list_logi);//物流商LIST
				if(list_logi != null && list_logi.size() > 0)
				{
					Map<String, Object> obj = list_logi.get(0);
					act.setOutData("LOGI_NAME", obj.get("LOGI_NAME"));
				}
				act.setForword(BILL_QUERY_LIST_URL);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 明细导出
	 */
	public void billExportDtl() {
		try {
			SalesBalanceService service = new SalesBalanceServiceImpl();
			List<Map<String, Object>> list = service.getBalanceExportDtl(request);
			String [] head={"对账单号","承运商","是否散单","交接单号","最后交车日期","发运仓库","经销商/收货仓库","订单收货地","发运结算地","是否中转","中转地","发运方式","车系","车型","配置","颜色","车架号","里程(段1)","单价(段1)","里程(段2)","单价(段2)","挂账运费"};
			String [] cols={"BAL_NO","LOGI_NAME","DLV_IS_SD","BILL_NO","LAST_CAR_DATE","WAREHOUSE_NAME","DEALER_NAME","ADDRESS_INFO","BAL_ADDR","DLV_IS_ZZ","ZZ_ADDR",
					"DLV_SHIP_TYPE","SERIES_NAME","MODEL_NAME","PACKAGE_NAME","COLOR_NAME","VIN","MILEAGE","PRICE","MILEAGE_ZZ","PRICE_ZZ","ONE_BILL_AMOUNT"};//导出的字段名称
			ToExcel.toReportExcel(act.getResponse(),request, "挂帐单明细列表.xls",head,cols,list);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 废弃挂帐单
	 */
	public void wasteBill() {
		SalesBalanceService service = new SalesBalanceServiceImpl();
		try {
			String res = service.wasteBill(request,loginUser);
			act.setOutData("SUCCESS", "SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("废弃挂帐单异常============");
		}
	}
	/**
	 * 重新生成挂账单
	 */
	public void againBill() {
		try {
			SalesBalanceService service = new SalesBalanceServiceImpl(); 
			String res =  service.againBill();
			act.setOutData("SUCCESS", "SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("重新生成挂帐单异常==========");
		}
	}
	/**
	 * 挂帐单明细查看
	 */
	public void showBalanceView() {
		try {
			SalesBalanceService service = new SalesBalanceServiceImpl();
			Map<String, Object> map=service.getBalanceInfoById(request);//主信息
			List<Map<String, Object>> flist=service.getBalanceDtlFare(request);//运费明细
			List<Map<String, Object>> vlist = service.getBalanceExportDtl(request);//车辆明细
		    act.setOutData("map", map);
		    act.setOutData("flist", flist);
		    act.setOutData("vlist", vlist);
			act.setForword(BILL_DETAIL_PAGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
