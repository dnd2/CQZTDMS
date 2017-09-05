package com.infodms.dms.actions.sales.storage.sendmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.dao.sales.storage.sendManage.AllocaDao;
import com.infodms.dms.dao.sales.storage.sendManage.AllocaSeachDao;
import com.infodms.dms.dao.sales.storage.sendManage.BatchOrderManageDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 批售订单管理
 * @author shuyh
 * 2017/7/17
 */
public class BatchOrderManage {
	public Logger logger = Logger.getLogger(BatchOrderManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final BatchOrderManageDao reDao = BatchOrderManageDao.getInstance();
	private static final CarSubmissionQueryDao csdao = CarSubmissionQueryDao.getInstance();
	private final String orderListInitUtl = "/jsp/sales/storage/sendmanage/orderManage/BatchOrderSeachList.jsp";
	private static final String ORDER_DETAIL_PAGE = "/jsp/sales/storage/sendmanage/orderManage/orderViewDel.jsp";
	
	/**
	 * 批售订单查询初始化
	 */
	public void orderSeachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//仓库LIST
			act.setForword(orderListInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车查询列表页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 批售订单查询
	 */
	public void queryBatchOrder() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); //经销商
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")); //批售单号
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo")); //批售单号
			String warehouseId = CommonUtils.checkNull(request.getParamValue("warehouseId")); //仓库ID
			String lastStartdate = CommonUtils.checkNull(request.getParamValue("lastStartdate")); //最晚到货日期开始
			String lastEndDate = CommonUtils.checkNull(request.getParamValue("lastEndDate")); // 最晚到货日期结束
			String transType = CommonUtils.checkNull(request.getParamValue("transType")); // 发运方式
			String subStartdate = CommonUtils.checkNull(request.getParamValue("subStartdate")); //提报日期开始
			String subEndDate = CommonUtils.checkNull(request.getParamValue("subEndDate")); // 提报日期结束
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus")); //订单状态
			String common = CommonUtils.checkNull(request.getParamValue("common")); //操作标记，1表示导出
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dealerCode", dealerCode);
			map.put("orderNo", orderNo);
			map.put("reqNo", reqNo);
			map.put("warehouseId", warehouseId);
			map.put("lastStartdate", lastStartdate);
			map.put("lastEndDate", lastEndDate);
			map.put("transType", transType);
			map.put("subStartdate", subStartdate);
			map.put("subEndDate", subEndDate);
			map.put("orderStatus", orderStatus);
			map.put("poseId", logonUser.getPoseId().toString());
			
			if("1".equals(common)){//导出 调用
				List<Map<String, Object>> list = reDao.getAllocaSeachQueryExport(map);
				String [] head={"批售单号","申请单号","经销商简称","发运仓库", "发运方式", "申请日期","最晚到货日期",  "申请量", "分派量", "组板量", "发运量", "交接量","验收量", "在途量", "订单状态"};
				String [] cols={"ORD_NO","REQ_NO","DEALER_NAME","WAREHOUSE_NAME","SEND_WAY_TXT","SUB_DATE","JJ_DATE","SUB_NUM","FP_NUM","BD_NUM","FY_NUM","JJ_NUM","YS_NUM","ZT_NUM","ORDER_STATUS_TXT"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "批售订单查询列表.xls",head,cols,list);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getBatchOrderQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "批售订单查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查看订单信息
	 */
	public void showOrderReport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单ID号
			act.setOutData("orderId", orderId);

			// 查询订单基本信息返回到页面
			Map<String, Object> map = reDao.getOrderMainInfo(orderId);
			act.setOutData("orderMap", map);

			// 返回订单的物料信息
			List<Map<String, Object>> materialList = reDao.getBatchOrderDel(orderId);
			act.setOutData("materialList", materialList);

			act.setForword(ORDER_DETAIL_PAGE);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
