package com.infodms.yxdms.service;

import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface ClearTransferService {
    /**
     * 服务站结算转账明细查询
     * @param loginUser
     * @param request
     * @param pageSize
     * @param currPage
     * @return
     */
	PageResult<Map<String, Object>> clearTransferQuery(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);
   
	/**
	 * 服务站结算转账明细导出
	 * @param loginUser
	 * @param request
	 * @param pageSizeMax
	 * @param currPage
	 */
	void clearTransferExport(AclUserBean loginUser, RequestWrapper request,Integer pageSizeMax, Integer currPage);
    /**
     * 服务站已转账不含税金额查询
     * @param loginUser
     * @param request
     * @param pageSize
     * @param currPage
     * @return
     */
	PageResult<Map<String, Object>> TransferWithoutTaxQuery(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage);
    
	/**
	 * 服务站已转账不含税金额导出
	 * @param loginUser
	 * @param request
	 * @param pageSizeMax
	 * @param currPage
	 */
	void TransferWithoutTaxExport(AclUserBean loginUser,RequestWrapper request, Integer pageSizeMax, Integer currPage);
    /**
     * 服务站结算金额和开票金额对比查询
     * @param loginUser
     * @param request
     * @param pageSizeMax
     * @param currPage
     * @return
     */
	PageResult<Map<String, Object>> InvoiceCompareQueryList(AclUserBean loginUser, RequestWrapper request, Integer pageSize,Integer currPage);

	void InvoiceCompareExport(AclUserBean loginUser, RequestWrapper request,Integer pageSizeMax, Integer currPage);

}
