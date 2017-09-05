package com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage.DealerOrderDispatchDAO;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtVsScMatchPO;
import com.infodms.dms.po.TtVsVehicleTransferPO;
import com.infodms.dms.po.TtVsVhclChngPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : DealerOrderDispatch.java
 * @Package: com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage
 * @Description: 结算中心：经销商订单调拨
 * @date   : 2010-6-26 
 * @version: V1.0   
 */
public class DealerOrderDispatch  extends BaseDao {
	public Logger logger = Logger.getLogger(DealerOrderDispatch.class);
	private ActionContext act = ActionContext.getContext();
	private static final DealerOrderDispatch dao = new DealerOrderDispatch();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	public static final DealerOrderDispatch getInstance() {
		return dao;
	}

	RequestWrapper request = act.getRequest();
	private final OrderQueryDao orderQueryDao = OrderQueryDao.getInstance();
	private final String dealerOrderDispatchInitURL = "/jsp/sales/balancecentermanage/dealerordermanage/dealerOrderDispatchInit.jsp";
	
	/** 
	* @Title	  : dealerOrderDispatchInit 
	* @Description: 经销商订单调拨页面初始化
	* @throws 
	* @LastUpdate :2010-6-26
	*/
	public void dealerOrderDispatchInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			Long orgId = logonUser.getOrgId();
			List<String> years = orderQueryDao.getYearList();
			List<String> weeks = orderQueryDao.getWeekList();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			Calendar c = Calendar.getInstance();
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					oemCompanyId);

			act.setOutData("orgId", orgId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear()
					: "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek()
					: "");
			act.setOutData("areaList", areaList);
			act.setForword(dealerOrderDispatchInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : dealerOrderDispatchInit_Query 
	* @Description: 经销商订单调拨:查询可调拨车辆
	* @throws 
	* @LastUpdate :2010-6-26
	*/
	public void dealerOrderDispatchInit_Query(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String orderYear1 = CommonUtils.checkNull(request.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request.getParamValue("orderWeek2"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dealerId = logonUser.getDealerId();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);//订单周度
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("areaId", areaId);		  //业务范围
			map.put("groupCode", groupCode);  //物料组
			map.put("orderNo", orderNo);	  //订单号
			map.put("dealerCode", dealerCode);   
			map.put("dealerId", dealerId);    
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerOrderDispatchDAO.getVehicleList(map,  Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单调拨:查询可调拨车辆");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : dealerOrderDispatchSubmit 
	* @Description: 经销商订单调拨:提交 
	* @throws 
	* @LastUpdate :2010-6-26
	*/
	public void dealerOrderDispatchSubmit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vehicleIds_ = CommonUtils.checkNull(request.getParamValue("vehicleIds_"));
			String dealerIds_ = CommonUtils.checkNull(request.getParamValue("dealerIds_"));
			
			String[] vehicleIds = vehicleIds_.split(",");
			String[] dealerIds = dealerIds_.split(",");
			
			for (int i = 0; i < vehicleIds.length; i++) {
				//1.更改车辆的dealerId
				TmVehiclePO tempVehiclePO = new TmVehiclePO();
				tempVehiclePO.setVehicleId(Long.parseLong(vehicleIds[i]));
				List list = dao.select(tempVehiclePO);
				Long outDealerId = ((TmVehiclePO)(list.get(0))).getDealerId();
				TmVehiclePO valueVehiclePO = new TmVehiclePO();
				valueVehiclePO.setDealerId(Long.parseLong(dealerIds[i]));
				dao.update(tempVehiclePO, valueVehiclePO);
				
				//2.更新“结算中心配车表”中对应记录的状态“已调拨”
				TtVsScMatchPO tempMatchPO = new TtVsScMatchPO();
				tempMatchPO.setVehicleId(Long.parseLong(vehicleIds[i]));
				TtVsScMatchPO valueMatchPO = new TtVsScMatchPO();
				valueMatchPO.setStatus(Constant.STORAGE_CHANGE_TYPE_02);
				dao.update(tempMatchPO, valueMatchPO);
				
				//3.新增车辆更改日志
				TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
				chngPO.setVhclChangeId(Long.parseLong(SequenceManager.getSequence("")));
				chngPO.setVehicleId(Long.parseLong(vehicleIds[i]));
				chngPO.setOrgType(logonUser.getOrgType());
				chngPO.setOrgId(logonUser.getOrgId());
				chngPO.setDealerId(outDealerId);
				chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE);
				chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_02+"");
				chngPO.setChangeDate(new Date());
				chngPO.setCreateDate(new Date());
				chngPO.setCreateBy(logonUser.getUserId());
				dao.insert(chngPO);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单调拨:提交 ");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
