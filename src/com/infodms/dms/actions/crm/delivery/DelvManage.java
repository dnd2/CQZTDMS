package com.infodms.dms.actions.crm.delivery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.crm.follow.FollowManage;
import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.delivery.DelvManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcDelvyPO;
import com.infodms.dms.po.TPcOrderDetailPO;
import com.infodms.dms.po.TPcOrderPO;
import com.infodms.dms.po.TPcRemindPO;
import com.infodms.dms.po.TPcRevisitPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.PageQuery;

public class DelvManage {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DelvManageDao dao = DelvManageDao.getInstance();

	private final String DELV_QUERY_URL = "/jsp/crm/delivery/delvInit.jsp";// 已交车列表
	private final String PAYCAR_QUERY_URL = "/jsp/crm/delivery/payCarInit.jsp";// 交车查询
	private final String DELV_URL = "/jsp/crm/delivery/delv.jsp";// 
	private final String VIN_LIST_INIT = "/jsp/crm/delivery/vinList.jsp";// vin选择界面
	private final String DELV_SURE_URL = "/jsp/crm/delivery/delvSureInit.jsp";// 待交车确认
	private final String PRINT_DELV_URL = "/jsp/crm/delivery/printDelv.jsp";// 交车打印
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
				act.setOutData("funcStr", funcStr);
				act.setForword(DELV_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "已交车查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void doSureInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String userId = null;
		String userType=null;
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			userType="60281003";
		}else if(CommonUtils.judgeMgrLogin(logonUser.getUserId().toString())){//判断是否经理登陆
			userType="60281002";
		}
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
				act.setOutData("funcStr", funcStr);
				act.setOutData("userType",userType);
				act.setForword(DELV_SURE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "待交车查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void doPayCarInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			FollowManage.getManager(logonUser, act);
				act.setOutData("funcStr", funcStr);
				act.setForword(PAYCAR_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "已交车查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	待交车查询
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void delvQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
            logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));		
			String telephone =  CommonUtils.checkNull(request.getParamValue("telephone"));	
			String orderDate =  CommonUtils.checkNull(request.getParamValue("orderDate"));	
			String orderDateEnd =  CommonUtils.checkNull(request.getParamValue("orderDateEnd"));	
			String preDlvDate =  CommonUtils.checkNull(request.getParamValue("preDlvDate"));	
			String preDlvDateEnd =  CommonUtils.checkNull(request.getParamValue("preDlvDateEnd"));	
			String vin=CommonUtils.checkNull(request.getParamValue("vin"));
			Map<String ,String > map=new HashMap<String,String>();
			map.put("ctmName", ctmName);
			map.put("telephone", telephone);
			map.put("orderDate", orderDate);
			map.put("orderDateEnd", orderDateEnd);
			map.put("preDlvDate", preDlvDate);
			map.put("preDlvDateEnd", preDlvDateEnd);
			map.put("dealerId", logonUser.getDealerId());
			map.put("logonId", logonUser.getUserId().toString());
			map.put("vin", vin);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDelvQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	查询已交车的数据（实效上报）
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void delvCheckQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));		
			String telephone =  CommonUtils.checkNull(request.getParamValue("telephone"));	
			String orderDate =  CommonUtils.checkNull(request.getParamValue("orderDate"));	
			String preDlvDate =  CommonUtils.checkNull(request.getParamValue("preDlvDate"));	
			String preDlvDateEnd =  CommonUtils.checkNull(request.getParamValue("preDlvDateEnd"));	
			String vin=CommonUtils.checkNull(request.getParamValue("vin"));
			Map<String ,String > map=new HashMap<String,String>();
			map.put("ctmName", ctmName);
			map.put("telephone", telephone);
			map.put("orderDate", orderDate);
			map.put("preDlvDate", preDlvDate);
			map.put("preDlvDateEnd", preDlvDateEnd);
			map.put("vin", vin);
			
			map.put("dealerId", logonUser.getDealerId());
			//map.put("logonId", logonUser.getUserId().toString());
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDelvCheckQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	查询所有的数据
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void delvAllQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));		
			String telephone =  CommonUtils.checkNull(request.getParamValue("telephone"));	
			String orderDate =  CommonUtils.checkNull(request.getParamValue("orderDate"));	
			String preDlvDate =  CommonUtils.checkNull(request.getParamValue("preDlvDate"));	
			String preDlvDateEnd =  CommonUtils.checkNull(request.getParamValue("preDlvDateEnd"));	
			String ifDelv=CommonUtils.checkNull(request.getParamValue("ifDelv"));
			String adviserId=CommonUtils.checkNull(request.getParamValue("adviserId"));//顾问 
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));//组
			String vin=CommonUtils.checkNull(request.getParamValue("vin"));
			Map<String ,String > map=new HashMap<String,String>();
			map.put("ctmName", ctmName);
			map.put("telephone", telephone);
			map.put("orderDate", orderDate);
			map.put("preDlvDate", preDlvDate);
			map.put("preDlvDateEnd", preDlvDateEnd);
			map.put("dealerId", logonUser.getDealerId());
			map.put("vin", vin);
			String poseRank=CommonUtils.getPoseRank(logonUser);
			if(!Constant.DEALER_USER_LEVEL_05.toString().equals(poseRank)){
				map.put("logonId", logonUser.getUserId().toString());
			}
			map.put("ifDelv", ifDelv);
			map.put("adviserId", adviserId);
			map.put("groupId", groupId);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDelvAllQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION : 待交的车架号
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toVinList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String qkId = CommonUtils.checkNull(request.getParamValue("qkId"));		
			String qkOrderDetailId =  CommonUtils.checkNull(request.getParamValue("qkOrderDetailId"));
			String inputId=request.getParamValue("inputId");
			String moderId=request.getParamValue("moderId");
			String colorTdId=request.getParamValue("colorTdId");
			String hiddenmoderId=request.getParamValue("hiddenmoderId");
			act.setOutData("inputId", inputId);
			act.setOutData("moderId", moderId);
			act.setOutData("colorTdId", colorTdId);
			act.setOutData("hiddenmoderId", hiddenmoderId);
			act.setOutData("qkId", qkId);
			act.setOutData("qkOrderDetailId", qkOrderDetailId);
			act.setForword(VIN_LIST_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 交车界面点击退车执行的方法
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void returnVechile() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String ctmId = CommonUtils.checkNull(request.getParamValue("ctmId"));		
			String orderDetailId =  CommonUtils.checkNull(request.getParamValue("orderDetailId"));
			String vechileId = CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			String delvDetailId=CommonUtils.checkNull(request.getParamValue("delvDetailId"));
			int carDelvy=0;
			int carSales=0;
			//查询订单明细
			TPcOrderDetailPO tpd =new TPcOrderDetailPO();
			tpd.setOrderDetailId(new Long(orderDetailId));
			tpd=(TPcOrderDetailPO) dao.select(tpd).get(0);
			//查询出订单
			TPcOrderPO tpo=new TPcOrderPO();
			tpo.setOrderId(tpd.getOrderId());
			tpo=(TPcOrderPO) dao.select(tpo).get(0);
			int remindNum=0;
			//判断订单状态如果订单状态为锁定无法交车，只有部分交车和完成交车的订单可以修改
			if(Constant.TPC_ORDER_STATUS_02.toString().equals(tpo.getOrderStatus().toString())||Constant.TPC_ORDER_STATUS_05.toString().equals(tpo.getOrderStatus().toString())){
				TPcOrderDetailPO tpd1 =new TPcOrderDetailPO();
				tpd1.setOrderDetailId(new Long(orderDetailId));
				TPcOrderDetailPO tpd2 =new TPcOrderDetailPO();
				TPcOrderPO tpo1=new TPcOrderPO();
				tpo1.setOrderId(tpd.getOrderId());
				
				//订单最终状态默认修改成部分交车
				TPcOrderPO tpo2=new TPcOrderPO();
				tpo2.setOrderStatus(Constant.TPC_ORDER_STATUS_02);
				int delvyNum=0;
				delvyNum=tpd.getDeliveryNumber()-1;
				tpd2.setDeliveryNumber(delvyNum);
				tpd2.setTaskStatus(Constant.TASK_STATUS_01);
				dao.update(tpd1, tpd2);
				remindNum=tpd.getNum()-delvyNum;
				//判断是否是所有的交车都为0如果是订单状态修改为正常状态
				TPcOrderDetailPO tempd=new TPcOrderDetailPO();
				tempd.setOrderId(tpo.getOrderId());
				List<PO> tempList=dao.select(tempd);
				boolean orderStatusFlag=false;
				for(int k=0;k<tempList.size();k++){
					TPcOrderDetailPO temp=new TPcOrderDetailPO();
					temp=(TPcOrderDetailPO) tempList.get(k);
					if(temp.getDeliveryNumber()!=0){
						orderStatusFlag=true;
						break;
					}
				}
				//如果所有的交车数据都是0
				if(!orderStatusFlag){
					tpo2.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
				}
				dao.update(tpo1, tpo2);
				//取消所有回访任务
				TPcRevisitPO tpr1=new TPcRevisitPO();
				tpr1.setVinId(new Long(vechileId));
				tpr1.setTaskStatus(Constant.TASK_STATUS_01);
				TPcRevisitPO tpr2=new TPcRevisitPO();
				tpr2.setTaskStatus(Constant.TASK_STATUS_03);
				dao.update(tpr1, tpr2);
				//结束原来的提醒
				TPcRevisitPO tprps=new TPcRevisitPO();
				tprps.setVinId(new Long(vechileId));
				List<PO> revisitlist=dao.select(tprps);
				int size=dao.select(tprps).size();
				
				for(int j=0;j<size;j++){
					TPcRevisitPO tempTprp=new TPcRevisitPO();
					tempTprp=(TPcRevisitPO) revisitlist.get(j);
					if(tempTprp.getTaskStatus().equals(Constant.TASK_STATUS_03)){
					     String behindId=tempTprp.getRevisitId().toString();
					     CommonUtils.setRemindDone(behindId, Constant.REMIND_TYPE_12.toString());
					}
				}
				
				
				//如果是已经确认车架号的情况
				TPcRemindPO tprp1=new TPcRemindPO();
				TPcRemindPO tprp2=new TPcRemindPO();
				TPcRemindPO tprp3=new TPcRemindPO();
				carDelvy=CommonUtils.getCarBackDelvy(vechileId, delvDetailId);
				carSales=CommonUtils.getCarBackSales(vechileId);
				//已经确认车架号
				if(tpd.getVehicleId()!=0){
					tprp1.setBeremindId(new Long(orderDetailId));
					tprp1.setRemindStatus(Constant.TASK_STATUS_02);
					tprp1.setRemindType(Constant.REMIND_TYPE_09.toString());
					tprp3.setRemindNum(1);
					tprp3.setRemindStatus(Constant.TASK_STATUS_01);
					dao.update(tprp1, tprp3);
					if(carDelvy==0 && carSales==0){
					//修改车辆状态
					TmVehiclePO tv1 =new TmVehiclePO();
					tv1.setVehicleId(new Long(vechileId));
					TmVehiclePO tv2 =new TmVehiclePO();
					tv2.setLifeCycle(Constant.VEHICLE_LIFE_10);
					dao.update(tv1, tv2);
					}
				}else{
					//没有确认车架号的
					tprp1.setBeremindId(new Long(orderDetailId));
					tprp1.setRemindStatus(Constant.TASK_STATUS_01);
					tprp1.setRemindType(Constant.REMIND_TYPE_09.toString());
					tprp3.setRemindNum(remindNum);
					tprp3.setRemindStatus(Constant.TASK_STATUS_01);
					dao.update(tprp1, tprp3);
					tprp2.setBeremindId(new Long(orderDetailId));
					tprp2.setRemindStatus(Constant.TASK_STATUS_02);
					tprp2.setRemindType(Constant.REMIND_TYPE_09.toString());
					dao.update(tprp2, tprp3);
					if(carDelvy==0 && carSales==0){
					//修改车辆状态
					TmVehiclePO tv1 =new TmVehiclePO();
					tv1.setVehicleId(new Long(vechileId));
					TmVehiclePO tv2 =new TmVehiclePO();
					tv2.setLifeCycle(Constant.VEHICLE_LIFE_03);
					dao.update(tv1, tv2);
					}
				}
				
				

				//增加接触点信息
				CommonUtils.addContackPoint(Constant.POINT_WAY_17, "完成退车", ctmId, logonUser.getUserId().toString(), logonUser.getDealerId());
			
				TPcDelvyPO tpdp=new TPcDelvyPO();
				tpdp.setDelvDetailId(new Long(delvDetailId));
				TPcDelvyPO tpdp1=new TPcDelvyPO();
				//tpdp1.setStatus(new Long(Constant.STATUS_DISABLE));
				tpdp1.setFailDate(new Date());
				tpdp1.setDeliveryStatus(Constant.delivery_status_04);
				dao.update(tpdp, tpdp1);
				//修改客户状态为有望
				TPcCustomerPO tp1=new TPcCustomerPO();
				tp1.setCustomerId(new Long(ctmId));
				TPcCustomerPO tp2=new TPcCustomerPO();
				tp2.setCtmType(Constant.CTM_TYPE_02.toString());
				dao.update(tp1, tp2);
			}else{
				act.setOutData("subFlag", "2");
			}
			act.setOutData("subFlag", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "已交车退车！！！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 查询到需要交车的vin列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getVinList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
			String qkOrderDetailId=CommonUtils.checkNull(request.getParamValue("qkOrderDetailId"));
			Map<String,String> map=new HashMap<String,String>();
			map.put("vin", vin);
			map.put("modelCode", modelCode);
			map.put("dealerId", logonUser.getDealerId());
			map.put("qkOrderDetailId", qkOrderDetailId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getVinList(map,logonUser.getDealerId(),Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 交车确认执行的方法
	 * */
	public void addDelvData() throws Exception{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String vehicle_id = CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			String qkId = CommonUtils.checkNull(request.getParamValue("qkId"));
			String qkOrderDetailId = CommonUtils.checkNull(request.getParamValue("qkOrderDetailId"));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			
			//判断是否已交完车辆
			TPcOrderDetailPO oPo = new TPcOrderDetailPO();
			oPo.setOrderDetailId(Long.parseLong(qkOrderDetailId));
			List<PO> poxx = dao.select(oPo);
			oPo = (TPcOrderDetailPO)poxx.get(0);
			Integer num = oPo.getNum();
			Long orderIdx = oPo.getOrderId();
			Integer deNum = oPo.getDeliveryNumber();
			if(num <= deNum) {
				throw new Exception("该条数据已被处理过！");
			}
			//避免重复交车
			TPcDelvyPO delvyPo= new TPcDelvyPO();
			delvyPo.setVehicleId(Long.parseLong(vehicle_id));
			List<PO> delvyList = dao.select(delvyPo);
			boolean isDelvy = false;
			for(int i=0;i<delvyList.size();i++){
				delvyPo=(TPcDelvyPO)delvyList.get(i);
				if(!delvyPo.getDeliveryStatus().toString().equals("60571004")){
					isDelvy = true;
				}
			}
			if(isDelvy) {
				throw new Exception("该VIN号已交车");
			}
			//判断订单状态（如若不为有效或者部分交车的则抛出异常）
//			TPcOrderPO oPo2 = new TPcOrderPO();
//			oPo2.setOrderId(orderIdx);
//			List<PO> poxx2 = dao.select(oPo2);
//			oPo2 = (TPcOrderPO)poxx2.get(0);
//			Integer oStatus = oPo2.getOrderStatus();
//			if(oStatus != 60231001 && oStatus !=60231002) {
//				throw new BizException("该条数据已被处理过！");
//			}
			int numbers=0;//待交车的总数量
			//根据vehicle_id 获取车型代码和颜色
			//修改车辆的生命周期为已交车
			TmVehiclePO tv1 =new TmVehiclePO();
			tv1.setVehicleId(new Long(vehicle_id));
			TmVehiclePO tv2 =new TmVehiclePO();
			tv2.setLifeCycle(Constant.VEHICLE_LIFE_09);
			dao.update(tv1, tv2);
			String color="";
			TmVehiclePO tv =new TmVehiclePO();
			tv.setVehicleId(new Long(vehicle_id));
			tv=(TmVehiclePO) dao.select(tv).get(0);
			color=tv.getColor();
			 //回写已交车数量
			TPcOrderDetailPO tpod0=new TPcOrderDetailPO();
			tpod0.setOrderDetailId(new Long(qkOrderDetailId));
			tpod0=(TPcOrderDetailPO) dao.select(tpod0).get(0);
			
			//得到订单id
			String orderStatus=Constant.TPC_ORDER_STATUS_02.toString();
			Long orderId=tpod0.getOrderId();
			TPcOrderPO tpo=new TPcOrderPO();
			tpo.setOrderId(orderId);
			numbers=tpod0.getNum();
			//得到已交车数量
			int numer=0;
			if(tpod0.getDeliveryNumber()!=null){
				numer=tpod0.getDeliveryNumber().intValue()+1;
			}else{
				numer=1;
			}
			int leftNumber=numbers-numer;
			TPcOrderDetailPO tpod1=new TPcOrderDetailPO();
			tpod1.setOrderDetailId(new Long(qkOrderDetailId));
			TPcOrderDetailPO tpod2=new TPcOrderDetailPO();
			tpod2.setDeliveryNumber(numer);
			tpod2.setActDelvDate(new Date());
			if(leftNumber==0){
				tpod2.setTaskStatus(Constant.TASK_STATUS_02);
			}
			tpod2.setUpdateBy(logonUser.getUserId().toString());
			tpod2.setUpdateDate(new Date());
			dao.update(tpod1, tpod2);//修改订单已交车数量
			boolean flag=false;//订单是否交车完成
			flag=CommonUtils.getOrderIfFinish(orderId.toString());
			if(flag){
				orderStatus=Constant.TPC_ORDER_STATUS_05.toString();
				//修改客户状态为保有
				TPcCustomerPO tp1=new TPcCustomerPO();
				tp1.setCustomerId(new Long(qkId));
				TPcCustomerPO tp2=new TPcCustomerPO();
				tp2.setCtmType(Constant.CTM_TYPE_01.toString());
				dao.update(tp1, tp2);
				//清理来店数据
				CommonUtils.clearComeCount(new Long(qkId));
			}
			
			TPcOrderPO tpo1=new TPcOrderPO();
			tpo1.setOrderStatus(new Integer(orderStatus));
			dao.update(tpo, tpo1);
			//生成交车列表
			TPcDelvyPO tpdp=new TPcDelvyPO();
			String seqId=SequenceManager.getSequence("");
			tpdp.setDelvDetailId(new Long(seqId));
			tpdp.setOrderDetailId(new Long(qkOrderDetailId));
			tpdp.setCustomerId(new Long(qkId));
			tpdp.setDeliveryDate(new Date());
			tpdp.setAmount(tpod0.getPrice());
			tpdp.setMaterial(tpdp.getMaterial());
			tpdp.setMaterial(tv.getMaterialId());
			tpdp.setColor(color);
			tpdp.setVehicleId(new Long(vehicle_id));
			tpdp.setPreviousTask(tpod0.getOrderDetailId());
			tpdp.setTaskStatus(Constant.TASK_STATUS_01);
			tpdp.setDeliveryStatus(Constant.delivery_status_01);//设置交车状态为已交车
			tpdp.setDeliveryDate(new Date());
			tpdp.setPrice(tpod0.getPrice());
			tpdp.setCreateDate(new Date());
			tpdp.setCreateBy(logonUser.getUserId().toString());
			tpdp.setStatus(new Long(Constant.STATUS_ENABLE));
			dao.insert(tpdp);
			//生成回访数据
			//获取客户信息
			TPcCustomerPO tpc=new TPcCustomerPO();
			tpc.setCustomerId(new Long(qkId));
			tpc=(TPcCustomerPO) dao.select(tpc).get(0);
			//获取下次回访时间自动生成当日七日月度季度回访
			Date d=new Date();
			Calendar c=Calendar.getInstance();
			c.setTime(d);
			//查询回访记录规则
			List<Map<String,Object>> trpList = dao.pageQuery("SELECT CODE_ID,CODE_DESC,NUM FROM TC_CODE WHERE TYPE="+Constant.REVISIT_TYPE+"  and STATUS="+Constant.STATUS_ENABLE, null, dao.getFunName());
			for(int i=0;i<trpList.size();i++){
				TPcRevisitPO tpr2=new TPcRevisitPO();
				Map<String,Object> one = trpList.get(i);
				String revistName = one.get("CODE_DESC")==null?"":one.get("CODE_DESC").toString(); //回访的名称
				String numValue = one.get("NUM")==null?"0":one.get("NUM").toString(); // 回访的天数
				String revisitType = one.get("CODE_ID")==null?"0":one.get("CODE_ID").toString(); // 回访的类型ID
				String seqId1=SequenceManager.getSequence("");
				tpr2.setRevisitId(new Long(seqId1));
				tpr2.setCustomerId(new Long(qkId));
				tpr2.setCreateBy(logonUser.getUserId().toString());
				tpr2.setCreateDate(new Date());
				tpr2.setRevistTypeName(revistName);
				tpr2.setRevisitType(revisitType);
				c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(numValue));
				tpr2.setRevisitDate(c.getTime());
				tpr2.setPreviousTask(new Long(seqId));
				tpr2.setBuyDate(new Date());
				tpr2.setVinId(new Long(vehicle_id));
				tpr2.setTaskStatus(Constant.TASK_STATUS_01);
				tpr2.setRevisitDealer(logonUser.getDealerId());
				tpr2.setRevisitAdviser(logonUser.getUserId().toString());
				dao.insert(tpr2);
				//新增一个回访提醒
				CommonUtils.addRemindInfo(Constant.REMIND_TYPE_12.toString(), seqId1, qkId, logonUser.getDealerId(), tpc.getAdviser().toString(), sdf.format(c.getTime()),"");
			}
			//如果交车完成就是数量为0了，这时候结束原来的提醒，否则还得新增交车提醒
			CommonUtils.setRemindDone(qkOrderDetailId,Constant.REMIND_TYPE_09.toString());//结束原来的提醒
			if(leftNumber>0){
				CommonUtils.addRemindInfo(Constant.REMIND_TYPE_09.toString(), qkOrderDetailId, qkId, logonUser.getDealerId(), tpc.getAdviser().toString(), sdf.format(tpod0.getDeliveryDate()),leftNumber+"");
			}
			//增加接触点信息
			CommonUtils.addContackPoint(Constant.POINT_WAY_08, "完成交车", qkId, logonUser.getUserId().toString(), logonUser.getDealerId());
			act.setOutData("flag", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void printDelvInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
				act.setOutData("funcStr", funcStr);
				String vechile_id=request.getParamValue("vechile_id");
				String customer_id=request.getParamValue("customer_id");
				String order_detail_id=request.getParamValue("order_detail_id");
				act.setForword(PRINT_DELV_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
