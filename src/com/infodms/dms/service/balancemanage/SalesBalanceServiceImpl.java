package com.infodms.dms.service.balancemanage;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.sales.storage.balancemanage.SalesBalanceDao;
import com.infodms.dms.po.TtSalesBalancePO;
import com.infodms.dms.po.TtSalesWayBillDtlPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class SalesBalanceServiceImpl implements SalesBalanceService {

	public PageResult<Map<String, Object>> getTtSalesBalance(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		SalesBalanceDao dao = new SalesBalanceDao();

		return dao.getTtSalesBalance(request, loginUser, pageSize, currPage);
	}
	
	public PageResult<Map<String, Object>> getTtSalesBalanceQuery(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage) {
		SalesBalanceDao dao = new SalesBalanceDao();

		return dao.getTtSalesBalanceQuery(request, loginUser, pageSize, currPage);
	}
	@SuppressWarnings("unchecked")
	public String wasteBill(RequestWrapper request, AclUserBean loginUser) throws Exception {
		String bal_id = DaoFactory.getParam(request, "bal_id");
		SalesBalanceDao dao = new SalesBalanceDao();
		List<TtSalesWaybillPO> waybillPO = this.getTtSalesWaybillPOByBalId(Long.valueOf(bal_id));
		for (TtSalesWaybillPO po : waybillPO) {
//			List<TtSalesWayBillDtlPO> list =this.getTtSalesWayBillDtlPOByBillId(po.getBillId());
		    dao.updateTtSalesWayBillDtlbyBillId(po.getBillId(),loginUser);
		    dao.updaTtSalesWaybillByBillId(po.getBillId(),loginUser);
		}
		TtSalesBalancePO balancePO = new TtSalesBalancePO();
		balancePO.setBalId(Long.valueOf(bal_id));
		dao.delete(balancePO);
		return "SUCCESS";
	}
	

	@SuppressWarnings("unchecked")
	public List<TtSalesWayBillDtlPO> getTtSalesWayBillDtlPOByBillId(Long billId) {
		SalesBalanceDao dao = new SalesBalanceDao();
		TtSalesWayBillDtlPO dtlPO = new TtSalesWayBillDtlPO();
		dtlPO.setBillId(billId);
		List<TtSalesWayBillDtlPO> list = dao.select(dtlPO);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<TtSalesWaybillPO> getTtSalesWaybillPOByBalId(Long bal_id) {
		SalesBalanceDao dao = new SalesBalanceDao();
		TtSalesWaybillPO po = new TtSalesWaybillPO();
		po.setBalId(bal_id);
		List<TtSalesWaybillPO> list = dao.select(po);
		return list;
	}

	public String againBill() throws Exception {
		POFactoryBuilder.getInstance().callProcedure("P_SALES_BALANCE_TRANSPORT", null, null);
//		POFactoryBuilder.getInstance().callFunction("P_SALES_BALANCE_TRANSPORT",java.sql.Types.VARCHAR,null);
		return "SUCCESS";
	}
	/**
	 * 导出挂帐单查询结果
	 */
	public List<Map<String, Object>> getTtSalesBalanceExport(RequestWrapper request,AclUserBean loginUser) {
		SalesBalanceDao dao = new SalesBalanceDao();
		
		return dao.getTtSalesBalanceExport(request, loginUser);
	}

	public List<Map<String, Object>> getBalanceExportDtl(
			RequestWrapper request) {
		SalesBalanceDao dao = new SalesBalanceDao();
		
		return dao.getBalanceExportDtl(request);
	}

	public Map<String, Object> getBalanceInfoById(RequestWrapper request) {
		SalesBalanceDao dao = new SalesBalanceDao();
		
		return dao.getBalanceInfo(request);
	}
	/**
	 * 获取运费明细
	 */
	public List<Map<String, Object>> getBalanceDtlFare(RequestWrapper request) {
		SalesBalanceDao dao = new SalesBalanceDao();
		
		return dao.getBalanceDtlFare(request);
	}

}
