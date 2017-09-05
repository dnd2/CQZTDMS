package com.infodms.dms.service.ontheway;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infodms.dms.po.TtSalesWayBillAddressPO;
import com.infodms.dms.po.TtSalesWayBillDtlPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface OnTheWayService {
    /**
     * 在途列表维护
     * @param request
     * @param loginUser
     * @param pageSize
     * @param currPage
     * @return
     * @throws Exception
     */
	public PageResult<Map<String, Object>> getOnTheWayList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,
			Integer currPage) throws Exception;
    /**
     * 交接单明细查询
     * @param request
     * @param loginUser
     * @param pageSize
     * @param currPage
     * @return
     * @throws Exception
     */
	public PageResult<Map<String, Object>> getTtSalesWayBillDtlpo(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage)throws Exception;
	/**
	 * 交接单列表
	 * （手机端）
	 * @param request
	 * @param valueOf
	 * @param valueOf2
	 * @return
	 */
	public PageResult<Map<String, Object>> getTtSalesWaybill(RequestWrapper request, Integer page_size, Integer curr_page);
	/**
	 * 获取交接单明细（手机端）
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getTtSalesWayBillDtl(RequestWrapper request);
	/**
	 * 根据交接单id查询交接单
	 * @param valueOf
	 * @return
	 */
	public TtSalesWaybillPO getTtSalesWaybillPO(Long bill_id);
	/**
	 * 根据承运商id获取物流信息
	 * @param logiId
	 * @return
	 */
	public TtSalesLogiPO getTtSalesLogiPOByLogiId(Long logiId);
	/**
	 * 查询明细
	 * @param billId
	 * @return
	 */
	public List<Map<String, Object>> getMateriaDetail(String billId);
	/**
	 * 根据billid获取交接单
	 * @param request
	 * @return
	 */
	public Map<String, Object> getTtSalesWaybillByBillId(RequestWrapper request);
	/**
	 * 保存位置信息
	 * @param request
	 * @param loginUser
	 * @return
	 */
	public String saveOntheWayAddress(RequestWrapper request, AclUserBean loginUser)throws Exception;
	/**
	 * 根据明细id查询
	 * @param dtl_id
	 * @return
	 * @throws Exception
	 */
	public TtSalesWayBillDtlPO getTtSalesWayBillDtlPO(Long dtl_id) throws Exception;
	/**
	 * 查询在途位置信息根据明细id
	 * @param request
	 * @param loginUser
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> showOntheWayAddress(RequestWrapper request, AclUserBean loginUser) throws Exception;
	/**
	 * （手机端）扫描修改车辆状态
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String updateTtSalesWayBillDtlStatusByDtlId(RequestWrapper request)throws Exception;
	/**
	 * 位置上报
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String driverLocationReport(RequestWrapper request)throws Exception;
	/**
	 * 交车上报
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String leaveCarLocationReport(RequestWrapper request)throws Exception;
	/**
	 * 取消绑定（手机端）
	 * @param request
	 * @return
	 */
	public String updateUnTtSalesWayBillDtlStatusByDtlId(RequestWrapper request) throws Exception;
	/**
	 * 导入在途信息
	 * @param voList
	 * @param loginUser 
	 * @param request 
	 * @return
	 * @throws Exception
	 */
	public String importExcelOnTheWay(List<TtSalesWayBillAddressPO> voList, RequestWrapper request, AclUserBean loginUser) throws Exception;
	/**
	 * 根据交接单id vin查询交接单明细
	 * @param valueOf
	 * @param vin
	 * @return
	 */
	public TtSalesWayBillDtlPO getTtSalesWayBillDtlPOByBillIdAndVin(Long bill_id, String vin);
	/**
	 * 获取承运商列表
	 * @return
	 */
	public List<Map<String, Object>> getLogiName();
	/**
	 * 车厂查询在途信心
	 * @param request
	 * @param loginUser
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	public PageResult<Map<String, Object>> getCarFactoryOnTheWayList(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage);
	/**
	 * 根据职位id查询职位
	 * @param loginUser
	 * @return
	 */
	public TcPosePO getTcPostByPostId(AclUserBean loginUser);
	/**
	 * 车厂查询在途
	 * @param request
	 * @param loginUser
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	public PageResult<Map<String, Object>> getCarFactoryOnTheWayListSGM(RequestWrapper request, AclUserBean loginUser,
			Integer pageSize, Integer currPage);
	/**.
	 * 经销商查询在途
	 * @param request
	 * @param loginUser
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	public PageResult<Map<String, Object>> getCarFactoryOnTheWayListDealer(RequestWrapper request,
			AclUserBean loginUser, Integer pageSize, Integer currPage);
	/**
	 * 查询绑定解绑日志
	 * @param request
	 * @param loginUser
	 * @return
	 */
	public List<Map<String, Object>> getbindCarlog(RequestWrapper request, AclUserBean loginUser);

}
