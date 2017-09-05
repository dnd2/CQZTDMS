package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsPaymentPO;
import com.infoservice.mvc.context.RequestWrapper;

public interface InvoiceService {
	/**
	 * 接口查询 结算单上取值费用
	 * @param no
	 * @return
	 */
	public Map<String, Double> invoiceInfoMoneyByNO(String no);
	/**
	 * 查询二次入库明细
	 * @param no
	 * @return
	 */
	public List<Map<String, Object>> invoiceInfoSecondTimeByNo(String no);
	
	public List<Map<String, Object>> returnShow(String param);
	/**
	 * 查询当前月审核的特殊费用金额
	 * @param dealerId
	 * @param startTime
	 * @param endTime
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> findSpecialAmount(String dealerId,String startTime, String endTime, Long areaId);
	
	public List<FsFileuploadPO> AppprintFJ(String balanecNo);
	public List<TtAsPaymentPO> queryPaymentByid(RequestWrapper request,AclUserBean loginUser);
	/**
	 * 待收票增加备注
	 * @param request
	 * @param loginUser
	 * @return
	 */
	public int addsureRemarkPayment(RequestWrapper request,AclUserBean loginUser);
	
	public String checkticeksByBalanceNo(RequestWrapper request);
}
