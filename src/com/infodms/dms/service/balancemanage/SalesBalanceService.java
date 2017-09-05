package com.infodms.dms.service.balancemanage;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.TtSalesWayBillDtlPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface SalesBalanceService {
    /**
     * 挂帐单管理查询
     * @param request
     * @param loginUser
     * @param pageSize
     * @param currPage
     * @return
     */
	public PageResult<Map<String, Object>> getTtSalesBalance(RequestWrapper request, AclUserBean loginUser, Integer pageSize,
			Integer currPage);
	
	public PageResult<Map<String, Object>> getTtSalesBalanceQuery(RequestWrapper request, AclUserBean loginUser, Integer pageSize,
			Integer currPage);
	/**
	 * 导出挂帐单查询结果
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getTtSalesBalanceExport(RequestWrapper request, AclUserBean loginUser);
	/**
	 * 获取挂帐单查看主信息
	 * @param request
	 * @return
	 */
	public Map<String, Object> getBalanceInfoById(RequestWrapper request);
	/**
	 * 获取运费明细
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getBalanceDtlFare(RequestWrapper request);
	/**
	 * 明细导出
	 * @param request
	 * @param loginUser
	 * @return
	 */
	public List<Map<String, Object>> getBalanceExportDtl(RequestWrapper request);
	/**
     * 废弃挂帐单
     * @param request
     * @param loginUser
     * @return
     * @throws Exception
     */
	public String wasteBill(RequestWrapper request, AclUserBean loginUser) throws Exception;
    /**
     * 根据挂帐单id查询交接单
     * @param bal_id
     * @return
     */
	public List<TtSalesWaybillPO> getTtSalesWaybillPOByBalId(Long bal_id);
	/**
	 * 根据交接单id查询交接单明细
	 * @param billId
	 * @return
	 */
	public List<TtSalesWayBillDtlPO> getTtSalesWayBillDtlPOByBillId(Long billId);
	/**
	 * 生成挂账单
	 * @return
	 * @throws Exception
	 */
	public String againBill()throws Exception;
}
