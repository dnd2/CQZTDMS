package com.infodms.dms.actions.sales.storage.sendmanage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesAssignPO;
import com.infodms.dms.po.TtSalesBoDetailPO;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryDtlPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
/**
 * 
 * @ClassName     : SendAssignmentChange
 * @Description   : 发送组板管理
 * @author        : ranjian
 * CreateDate     : 2013-4-19
 */
public class SendBoardManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SendBoardDao reDao = SendBoardDao.getInstance();
	private final StorageUtil stoUtil=StorageUtil.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String sendBoardInitUtl = "/jsp/sales/storage/sendmanage/sendBoard/sendBoardList.jsp";
	private final String addSendBoardInitUtl = "/jsp/sales/storage/sendmanage/sendBoard/addSendBoard.jsp";

	/**
	 * 
	 * @Title      : 
	 * @Description: 组板管理初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void sendBoardManageInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=reLDao.getLogiByWarehouse(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//产地LIST
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(sendBoardInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"组板查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 组板查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void sendBoardManageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String common=CommonUtils.checkNull(request.getParamValue("common"));//处理类型
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); //发运仓库
			String dlvStartDate = CommonUtils.checkNull(request.getParamValue("DLV_START_DATE")); //最晚发运日期开始
			String dlvEndDate = CommonUtils.checkNull(request.getParamValue("DLV_END_DATE")); // 最晚发运日期结束
			String transType = CommonUtils.checkNull(request.getParamValue("transType")); //发运方式
			String arrStartDate = CommonUtils.checkNull(request.getParamValue("ARR_START_DATE")); //最晚到货日期开始
			String arrEndDate = CommonUtils.checkNull(request.getParamValue("ARR_END_DATE")); // 最晚到货日期结束
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流公司
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); //提交日期开始
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 提交日期结束
			String status = CommonUtils.checkNull(request.getParamValue("status")); //状态
			String provinceId = CommonUtils.checkNull(request.getParamValue("jsProvince")); //省份
			String cityId = CommonUtils.checkNull(request.getParamValue("jsCity")); // 城市
			String countyId = CommonUtils.checkNull(request.getParamValue("jsCounty")); // 区县
			String isMiddleTurn = CommonUtils.checkNull(request.getParamValue("isMiddleTurn")); //是否中转
			String isSdan = CommonUtils.checkNull(request.getParamValue("isSdan")); //是否散单
			//大区
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode")); // 大区
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("dlvStartDate", dlvStartDate);
			map.put("dlvEndDate", dlvEndDate);
			map.put("transType", transType);
			map.put("arrStartDate", arrStartDate);
			map.put("arrEndDate", arrEndDate);
			map.put("logiName", logiName);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("status", status);
			map.put("provinceId", provinceId);
			map.put("cityId", cityId);
			map.put("countyId", countyId);
			map.put("isMiddleTurn", isMiddleTurn);
			map.put("isSdan", isSdan);
			map.put("orgCode", orgCode);
			map.put("poseId", logonUser.getPoseId().toString());
			
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=reDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiId", (BigDecimal)pmap.get("LOGI_ID"));
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = reDao.tgSum(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用
				List<Map<String, Object>> mapList = reDao.getSendBoardQueryExport(map);
				String [] head={"订单号","发运省份","发运城市","发运区县","经销商","车型","发票号","分派时间","订单数量","组板数量","剩余数量"};
				String [] cols={"ORDER_NO","PC_NAME","PC_NAME1","PC_NAME2","DEALER_NAME","MODEL_NAME","INVOICE_NO","ASS_DATE","ORDER_NUM","BOARD_NUM","INNAGE_NUM"};
				ToExcel.toReportExcel(act.getResponse(),request, "组板详细信息.xls",head,cols,mapList);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getSendBoardQuery(map, curPage,
						Constant.PAGE_SIZE_MIDDLE);
				act.setOutData("ps", ps);	
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运组板信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:初始化组板添加页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void addSendBordInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] orderId = request.getParamValues("orderIds");
			String orderIdStr="";
			for(int i=0;i<orderId.length;i++){
				orderIdStr+=orderId[i]+",";
			}
			
			List<Map<String, Object>> list = reDao.getSendBoardMatListQuery(orderIdStr);
			act.setOutData("list", list);
			act.setForword(addSendBoardInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"添加组板初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 校验是否有未组板的散单
	 */
	public void checkHasSdUnBo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] orderId = request.getParamValues("orderIds");
			String[] jsProvinces=request.getParamValues("jsProvinces");//结算省份
			String[] jsCitys=request.getParamValues("jsCitys");//结算城市
			String[] jsCountys=request.getParamValues("jsCountys");//结算区县
			String[] shipTypes=request.getParamValues("shipTypes");//发运方式
			String[] logiIds=request.getParamValues("logiIds");//承运商
			
			//根据结算地、发运方式、承运商获取分派数据，若存在未组板的散单。不允许组板
			String jsProvince=jsProvinces[0];
			String jsCity=jsCitys[0];
			String jsCounty=jsCountys[0];
			String shipType=shipTypes[0];
			String logiId=logiIds[0];
			
			StringBuffer ordIds=new StringBuffer();
			for(int i=0;i<orderId.length;i++){
				ordIds.append("'"+orderId[i]+"',");
				if(i==orderId.length-1){
					ordIds.append("'"+orderId[i]+"'");
				}
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("jsProvince", jsProvince);
			map.put("jsCity", jsCity);
			map.put("jsCounty",jsCounty);
			map.put("shipType", shipType);
			map.put("logiId", logiId);
			map.put("ordIds",ordIds.toString());
			List<Map<String, Object>> tlist=reDao.checkHasSD(map);
			if(tlist!=null&&tlist.size()>0){
				act.setOutData("status", "2");
			}else{
				act.setOutData("status", "1");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"添加组板初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 确定组板
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void  sendBordMain(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] reqIds=request.getParamValues("REQ_ID");//发运ID
			String[] yeIds=request.getParamValues("AREA_ID");//发运产地ID
			//String[] whIds=request.getParamValues("DLV_WH_ID");//发运仓库ID
			String[] orDeId=request.getParamValues("OR_DE_ID");//整车订单明细ID
			String[] matId=request.getParamValues("MAT_ID");//物料ID
			String[] colarCode=request.getParamValues("COLOR_CODE");//颜色
			String[] invoiceNum=request.getParamValues("INVOICE_NUM");//订单数量			
			String[] boardNum=request.getParamValues("BOARD_NUM");//本次组板数量
			String[] orderIds=request.getParamValues("ORDER_IDS");//订单ID
			String[] logiIds=request.getParamValues("LOGI_IDS");//物流商ID
			String carNo=CommonUtils.checkNull(request.getParamValue("CAR_NO"));//承运车牌号
			String loads=CommonUtils.checkNull(request.getParamValue("LOADS"));//装车道次
			String carTeam=CommonUtils.checkNull(request.getParamValue("CAR_TEAM"));//领票车队
			String driverName = CommonUtils.checkNull(request.getParamValue("DRIVER_NAME"));//驾驶员姓名
			String driverTel = CommonUtils.checkNull(request.getParamValue("DRIVER_TEL"));//驾驶员电话
		/************************************************组板表信息添加START***************************/
		//根据ID获取发运主信息
		TtVsDlvryPO tvds=new TtVsDlvryPO();
		tvds.setReqId(Long.parseLong(reqIds[0]));
		List slist=reDao.select(tvds);
		tvds=(TtVsDlvryPO) slist.get(0);
		//添加发运组板表信息
		String boardId=SequenceManager.getSequence(null);//组板表ID
		String boNo=CommonUtils.getBusNo(Constant.NOCRT_BOARD_NO,Long.parseLong(yeIds[0]));
		TtSalesBoardPO tsbPO=new TtSalesBoardPO();//组板表实体
		tsbPO.setBoId(Long.parseLong(boardId));//组板ID
		tsbPO.setBoNo(boNo);//组板编号(生成规则：ZB+日期+序列号)
		tsbPO.setBoPer(logonUser.getUserId());//组板人
		tsbPO.setBoDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//组板时间(数据库时间)
		tsbPO.setCreateBy(logonUser.getUserId());//创建人
		tsbPO.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
		tsbPO.setIsEnable(Constant.IF_TYPE_YES);//是否有效（有效）
		tsbPO.setHandleStatus(Constant.HANDLE_STATUS01);//默认未配车
		tsbPO.setAreaId(Long.parseLong(yeIds[0]));//产地ID都一样的
		tsbPO.setCarNo(carNo);
		tsbPO.setLoads(loads);
		tsbPO.setCarTeam(carTeam);
		tsbPO.setDriverName(driverName);
		tsbPO.setDriverTel(driverTel);
		tsbPO.setDlvShipType(tvds.getDlvShipType());//发运方式
		tsbPO.setDlvLogiId(tvds.getDlvLogiId());//承运商
		tsbPO.setDlvBalProvId(tvds.getDlvBalProvId());//结算省份
		tsbPO.setDlvBalCityId(tvds.getDlvBalCityId());//结算城市
		tsbPO.setDlvBalCountyId(tvds.getDlvBalCountyId());//结算区县
		//获取组板订单中最大的最晚发运日期和最晚交货日期
		if(reqIds!=null&&reqIds.length>0){
			StringBuffer sb=new StringBuffer();
			for(int m=0;m<reqIds.length;m++){
				if(m==reqIds.length-1){
					sb.append("'"+reqIds[m]+"'");
				}else{
					sb.append("'"+reqIds[m]+"',");
				}
			}
			Map<String,Object> dmap=reDao.getMaxDlvDateByRid(sb.toString());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(dmap.containsKey("DLV_FY_DATE")&&null!=dmap.get("DLV_FY_DATE")){
				Date dd=sdf.parse(dmap.get("DLV_FY_DATE").toString());
				tsbPO.setDlvFyDate(dd);//最晚发运日期
			}
			if(dmap.containsKey("DLV_JJ_DATE")&&null!=dmap.get("DLV_JJ_DATE")){
				tsbPO.setDlvJjDate(sdf.parse(dmap.get("DLV_JJ_DATE").toString()));//最晚交货日期
			}
		}
		reDao.addSendBoard(tsbPO);/**添加组板表信息*/
		String haveRetail="0";
		if(boardNum!=null && boardNum.length>0){
			for(int i=0;i<boardNum.length;i++){
				//2017.8.3添加逻辑：如果本次组板数量不为0，则插入组板明细表
				if(Integer.parseInt(CommonUtils.checkNullNum(boardNum[i])) > 0) {
					//添加组板明细表
					String boDeId=SequenceManager.getSequence(null);//组板明细表ID
					TtSalesBoDetailPO tsbdPO =new TtSalesBoDetailPO();//组板明细表实体
					tsbdPO.setBoDeId(Long.parseLong(boDeId));//组板明细ID
					tsbdPO.setBoId(Long.parseLong(boardId));//组板ID
					tsbdPO.setOrDeId(Long.parseLong(orDeId[i]));//整车订单明细ID
					tsbdPO.setMatId(Long.parseLong(matId[i]));//物料ID
					tsbdPO.setColorCode(colarCode[i]);//颜色
					tsbdPO.setInvoiceNum(Integer.parseInt(CommonUtils.checkNullNum(invoiceNum[i])));//订单数量		
					tsbdPO.setBoardNum(Integer.parseInt(CommonUtils.checkNullNum(boardNum[i])));//本次组板数量
					tsbdPO.setCreateBy(logonUser.getUserId());//创建人
					tsbdPO.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
					tsbdPO.setIsEnable(Constant.IF_TYPE_YES);//是否有效（有效）
					
					TtVsDlvryPO ass=new TtVsDlvryPO();
					ass.setOrdId(Long.parseLong(orderIds[i]));
					ass=(TtVsDlvryPO)reDao.select(ass).get(0);
					int dlvType=ass.getDlvType();
					//判断发运类型是订单还是调拨
					/*******/
					if(dlvType==Constant.DELIVERY_ORD_TYPE_ORDER){//订单
						TtVsDlvryReqPO order=new TtVsDlvryReqPO();
						order.setOrderId(Long.valueOf(orderIds[i]));
						order=(TtVsDlvryReqPO)reDao.select(order).get(0);
						tsbdPO.setOrderType(order.getOrderType());
						tsbdPO.setOrderNo(order.getDlvryReqNo());
						tsbdPO.setAccType(order.getFundType());
						tsbdPO.setAreaId(order.getAreaId());
						tsbdPO.setRaiseDate(order.getReqDate());
						tsbdPO.setSendType(order.getDeliveryType());
						tsbdPO.setLinkMan(order.getLinkMan());
						tsbdPO.setDealerId(order.getDealerId());
						tsbdPO.setTel(order.getTel());
					}else{
						TtVsDispatchOrderPO tvp=new TtVsDispatchOrderPO();
						tvp.setDispId(Long.valueOf(orderIds[i]));
						tvp=(TtVsDispatchOrderPO)reDao.select(tvp).get(0);
						tsbdPO.setOrderNo(tvp.getDispNo());
					}
					
					
					tsbdPO.setLogiId(Long.valueOf(logiIds[i]));//物流商ID
					tsbdPO.setOrderId(Long.valueOf(orderIds[i]));
					tsbdPO.setAssDate(ass.getDlvDate());
					tsbdPO.setAssPer(ass.getDlvBy());
					tsbdPO.setAssRemark(ass.getDlvRemark());
					reDao.addSendBoardDel(tsbdPO);/**组板明细表添加*/
				}
			}
		}
		//根据组板ID汇总组板明细表的组板数量,更新至组板主表
		TtSalesBoDetailPO tsp =new TtSalesBoDetailPO();//组板明细表实体
		tsp.setBoId(Long.parseLong(boardId));
		List tlist=reDao.select(tsp);
		int sumBo=0;
		if(tlist!=null&&tlist.size()>0){
			for(int i=0;i<tlist.size();i++){
				TtSalesBoDetailPO tspd=(TtSalesBoDetailPO) tlist.get(i);
				sumBo+=tspd.getBoardNum();
			}
		}
		//修改是否是零售
		TtSalesBoardPO pseach=new TtSalesBoardPO();
		pseach.setBoId(Long.parseLong(boardId));
		TtSalesBoardPO p=new TtSalesBoardPO();
		p.setHaveRetail(haveRetail);
		p.setBoNum(sumBo);
		reDao.update(pseach, p);
		//根据订单明细ID汇总组板数量，更新至发运分派明细表
		String orDeIdStr="";
		for(int i=0;i<orDeId.length;i++){
			orDeIdStr+=orDeId[i]+",";
		}
		List<Map<String, Object>> clist=reDao.getCountBoNumByBTid(orDeIdStr);
		if(clist!=null&&clist.size()>0){
			for(int i=0;i<clist.size();i++){
				Map<String, Object> map=clist.get(i);
				//更新发运分派明细表中的组板数量
				TtVsDlvryDtlPO tvdd=new TtVsDlvryDtlPO();
				tvdd.setOrdDetailId(Long.parseLong(map.get("OR_DE_ID").toString()));
				TtVsDlvryDtlPO tvdd2=new TtVsDlvryDtlPO();
				tvdd2.setBdTotal(Integer.parseInt(map.get("BO_NUM").toString()));//组板数
				tvdd2.setUpdateBy(logonUser.getUserId());
				tvdd2.setUpdateDate(new Date());
				reDao.update(tvdd, tvdd2);
			}
			
		}
		
		
		//修改已组板数量
		//stoUtil.updateBoardOrAllocaNum(Long.valueOf(boardId), logonUser.getUserId());
		//更新发运分派表中的组板数量和分派状态
		if(boardNum!=null&&boardNum.length>0){
			for(int i=0;i<boardNum.length;i++){
				//根据订单ID统计发运分派明细表中的组板数量
				Map<String, Object> map=reDao.getDlvDtlCount(orderIds[i]);
				TtVsDlvryPO tvd=new TtVsDlvryPO();
				tvd.setOrdId(Long.parseLong(orderIds[i]));
				List list=reDao.select(tvd);
				TtVsDlvryPO tvs=(TtVsDlvryPO)list.get(0);
				TtVsDlvryPO tvp=new TtVsDlvryPO();
				int bdTotal=Integer.parseInt(map.get("BD_TOTAL").toString());//组板数量
				tvp.setDlvBdTotal(bdTotal);
				if(tvs.getOrdTotal()>bdTotal){//组板数小于订单数
					tvp.setDlvStatus(Constant.ORDER_STATUS_04);//部分组板
				}else{
					tvp.setDlvStatus(Constant.ORDER_STATUS_05);//组板提交
				}
				tvp.setUpdateBy(logonUser.getUserId());
				tvp.setUpdateDate(new Date());
				reDao.update(tvd, tvp);
			}
		}
		
		act.setOutData("boNo", boNo);//返回组板号
		act.setOutData("returnValue", 1);
	} catch (Exception e) {// 异常方法
		BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "组板信息确认");
		logger.error(logonUser, e1);
		act.setException(e1);
	}
  }
	
	/**
	 * 修改物流商
	 * @author liufazhong
	 */
	public void updateLogi(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderIdsP = request.getParamValue("orderIds");
			String logiIdsP = request.getParamValue("logiIds");
			if (!XHBUtil.IsNull(orderIdsP) && !XHBUtil.IsNull(logiIdsP)) {
				String [] orderIds = orderIdsP.split(",");
				String [] logiIds = logiIdsP.split(",");
				if (orderIds != null && orderIds.length > 0 && logiIds != null && orderIds.length == logiIds.length) {
					int count = 0;
					for (int i = 0; i < orderIds.length; i++) {
						count += reDao.updateLogiIdByOrderId(orderIds[i], logiIds[i]);
					}
					if (count == orderIds.length) {
						act.setOutData("status", "00");
					} else {
						throw new BizException("操作失败!");
					}
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"修改物流商");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
