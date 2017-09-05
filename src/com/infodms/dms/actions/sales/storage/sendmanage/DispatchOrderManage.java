package com.infodms.dms.actions.sales.storage.sendmanage;

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
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.dao.sales.storage.sendManage.AllocaDao;
import com.infodms.dms.dao.sales.storage.sendManage.AllocaSeachDao;
import com.infodms.dms.dao.sales.storage.sendManage.BatchOrderManageDao;
import com.infodms.dms.dao.sales.storage.sendManage.DispatchOrderManageDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleLocationChangeDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmWarehousePO;
import com.infodms.dms.po.TtVsDispatchOrderDtlPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryDtlPO;
import com.infodms.dms.po.TtVsDlvryErpDtlPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.po.TtVsInspectionPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.po.TtVsVhclChngPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 调拨单管理
 * @author shuyh
 * 2017/7/17
 */
public class DispatchOrderManage {
	public Logger logger = Logger.getLogger(DispatchOrderManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DispatchOrderManageDao reDao = DispatchOrderManageDao.getInstance();
	private final String orderListInitUtl = "/jsp/sales/storage/sendmanage/orderManage/DispatchOrderApplyInit.jsp";
	private static final String ORDER_DETAIL_PAGE = "/jsp/sales/storage/sendmanage/orderManage/dispatchOrderViewDel.jsp";
	private static final String ORDER_ADD_URL = "/jsp/sales/storage/sendmanage/orderManage/addDispatchOrder.jsp";
	private static final String ORDER_MODIFY_URL = "/jsp/sales/storage/sendmanage/orderManage/updateDispatchOrder.jsp";
	private final String orderQueryInitUtl = "/jsp/sales/storage/sendmanage/orderManage/DispatchOrderQueryInit.jsp";
	private static final String StockInitUrl="/jsp/sales/storage/sendmanage/orderManage/dispatchStockQueryInit.jsp";
	private static final String StockVehicleDetailUrl = "/jsp/sales/storage/sendmanage/orderManage/dispatchStockVehicleDetail.jsp";
	
	/**
	 * 调拨申请初始化
	 */
	public void orderApplyInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//仓库LIST
			act.setForword(orderListInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"调拨申请初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 调拨查询初始化
	 */
	public void orderSeachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//仓库LIST
			act.setForword(orderQueryInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"调拨查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 调拨订单查询
	 */
	public void queryDisPatchOrder() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String dispNo = CommonUtils.checkNull(request.getParamValue("dispNo")); //销售清单号
			String sendWareId = CommonUtils.checkNull(request.getParamValue("sendWareId")); //发运仓库ID
			String receiveWareId = CommonUtils.checkNull(request.getParamValue("receiveWareId")); //收货仓库ID
			String lastStartdate = CommonUtils.checkNull(request.getParamValue("lastStartdate")); //最晚到货日期开始
			String lastEndDate = CommonUtils.checkNull(request.getParamValue("lastEndDate")); // 最晚到货日期结束
			String transType = CommonUtils.checkNull(request.getParamValue("transType")); // 发运方式
			String subStartdate = CommonUtils.checkNull(request.getParamValue("subStartdate")); //提报日期开始
			String subEndDate = CommonUtils.checkNull(request.getParamValue("subEndDate")); // 提报日期结束
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus")); //订单状态
			
			String common = CommonUtils.checkNull(request.getParamValue("common")); //操作标记，1表示导出
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dispNo", dispNo);
			map.put("sendWareId", sendWareId);
			map.put("receiveWareId", receiveWareId);
			map.put("lastStartdate", lastStartdate);
			map.put("lastEndDate", lastEndDate);
			map.put("transType", transType);
			map.put("subStartdate", subStartdate);
			map.put("subEndDate", subEndDate);
			map.put("orderStatus", orderStatus);
			map.put("poseId", logonUser.getPoseId().toString());
			
			if("1".equals(common)){//导出 调用
				List<Map<String, Object>> list = reDao.getBatchOrderQueryExport(map);
				String [] head={"调拨单号","发运仓库","收货仓库","最晚到货日期","发运方式","提交日期","已提报数量","已分派数量","已组板数量","已发运数量","在途数量","已交接数量","订单状态"};
				String [] cols={"DISP_NO","SEND_WAREHOUSE","RECEIVE_WAREHOUSE","PLAN_DELIVER_DATE","SEND_WAY_TXT","SUB_DATE","SUB_NUM","ASS_NUM","BO_NUM","SEND_NUM","IN_WAY_NUM","REP_NUM","ORDER_STATUS_TXT"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "调拨订单查询列表.xls",head,cols,list);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getBatchOrderQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨订单查询");
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
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨订单查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 调拨订单新增初始化
	 */
	public void addDispatchOrderInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//仓库LIST
			act.setForword(ORDER_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"调拨订单新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 根据收货仓库获取地址列表
	 */
	public void getAddrByReceiveWr(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//act.getResponse().setContentType("application/json");
			String receiveWareId = CommonUtils.checkNull(request.getParamValue("receiveWareId")); // 收货仓库ID
			
			List<Map<String, Object>> addrlist = reDao.getAddrByReceiveWrList(receiveWareId);
			act.setOutData("addrlist", addrlist.get(0));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"调拨订单新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 根据物料ID和发运仓库获取库存数量
	 */
	public void getStockNumByWhId() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String materialId = CommonUtils.checkNull(request.getParamValue("materialId")); //物料ID
			String sendWareId = CommonUtils.checkNull(request.getParamValue("sendWareId")); //发运仓库ID
			
			Map<String, Object> map = reDao.getMatCountByWhId(materialId, sendWareId);
			if(map!=null&&map.containsKey("WARE_NUM")){
				act.setOutData("stockMap", map);
			}else{
				map=new HashMap<String,Object>();
				map.put("WARE_NUM", 0);
				act.setOutData("stockMap", map);
			}
			

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨订单查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 根据地址ID获取地址信息
	 */
	public void getAddrInfoById() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String receiveAddr = CommonUtils.checkNull(request.getParamValue("receiveAddr")); //地址ID
			
			Map<String, Object> map = reDao.getAddrInfoById(receiveAddr);
			act.setOutData("addrInfo", map);

		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "地址信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 新增调拨单
	 */
	public void addOrderAction() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			String sendWareId = CommonUtils.checkNull(request.getParamValue("sendWareId")); //发运仓库
			String receiveWareId = CommonUtils.checkNull(request.getParamValue("receiveWareId")); //收货仓库
			String transType = CommonUtils.checkNull(request.getParamValue("transType")); //发运方式
			String receiveAddr = CommonUtils.checkNull(request.getParamValue("receiveAddr")); //收车地址
			String accPerson = CommonUtils.checkNull(request.getParamValue("accPerson")); //收车联系人
			String accPhone = CommonUtils.checkNull(request.getParamValue("accPhone")); //收车电话
			String remark = CommonUtils.checkNull(request.getParamValue("remark")); //备注
			String[] materialIds = request.getParamValues("MATERIAL_ID"); //物料IDs
			String[] commitNums = request.getParamValues("COMMIT_NUMBER"); //调拨数量s
			
			String dispIdM = CommonUtils.checkNull(request.getParamValue("dispId")); //调拨单ID(修改用)
			String opFlag = CommonUtils.checkNull(request.getParamValue("opFlag")); //1表示新增，2表示修改
			
			String sendWare="";
			TmWarehousePO twp=new TmWarehousePO();
			twp.setWarehouseId(Long.parseLong(sendWareId));
			List wlist=reDao.select(twp);
			if(wlist==null||wlist.size()==0){
				act.setOutData("returnValue", "2");
				act.setOutData("returnMsg", "发运仓库不存在!");
				return;
			}else{
				sendWare=((TmWarehousePO)wlist.get(0)).getWarehouseName();
			}
			
			String receiveWare="";
			TmWarehousePO twp2=new TmWarehousePO();
			twp2.setWarehouseId(Long.parseLong(receiveWareId));
			List wlist2=reDao.select(twp2);
			if(wlist2==null||wlist2.size()==0){
				act.setOutData("returnValue", "2");
				act.setOutData("returnMsg", "收货仓库不存在!");
				return;
			}else{
				receiveWare=((TmWarehousePO)wlist2.get(0)).getWarehouseName();
			}
			String address=receiveAddr;
//			Map<String, Object> map = reDao.getAddrInfoById(receiveAddr);
//			if(map==null||map.isEmpty()){
//				act.setOutData("returnValue", "2");
//				act.setOutData("returnMsg", "收货地址不存在!");
//				return;
//			}else{
//				address=map.get("ADDRESS").toString();
//			}
			
			String dispIdD="";
			if(opFlag.equals("1")){//新增
				//向调拨单主表插入数据
				String dispId=SequenceManager.getSequence("");
				dispIdD=dispId;
				TtVsDispatchOrderPO tvp=new TtVsDispatchOrderPO();
				tvp.setDispId(Long.parseLong(dispId));
				String dispNo=CommonUtils.getBusNo(Constant.NOCRT_DB_ORDER_NO, Long.parseLong(Constant.areaIdJZD));
				tvp.setDispNo(dispNo);
				tvp.setSendWareId(Long.parseLong(sendWareId));
				tvp.setSendWarehouse(sendWare);
				tvp.setReceiveWareId(Long.parseLong(receiveWareId));
				tvp.setReceiveWarehouse(receiveWare);
				tvp.setSendWay(Integer.parseInt(transType));
				//tvp.setAddressId(Long.parseLong(receiveAddr));
				tvp.setAddress(address);
				tvp.setAccPerson(accPerson);
				tvp.setAccTel(accPhone);
				tvp.setRemark(remark);
				tvp.setSubDate(new Date());
				tvp.setOrderStatus(Constant.DB_ORDER_STATUS_01);//订单状态为已保存
				//获取城市里程中计划到达天数
				Date now=new Date();   
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
				int planDays=0;
				List<Map<String, Object>> plist=reDao.getPlanDateInfo(sendWareId, receiveWareId);
				if(plist!=null&&plist.size()>0){
					Map<String,Object> map2=plist.get(0);
					planDays=Integer.parseInt(map2.get("ARRIVE_DAYS").toString())+2;//需加上两天出库的天数
				}else{
					planDays=2;
				}
				String dd=sdf.format(new Date(now.getTime()+ planDays* 24 * 60 * 60 * 1000));
				tvp.setPlanDeliverDate(sdf.parse(dd));//最晚到货日期
				
				tvp.setCreateBy(logonUser.getUserId());
				tvp.setCreateDate(new Date());
				int subNum=0;
				if(commitNums.length>0){
					for(int i=0;i<commitNums.length;i++){
						subNum+=Integer.parseInt(commitNums[i]);
					}
				}
				tvp.setSubNum(subNum);
				reDao.insert(tvp);
			}else if(opFlag.equals("2")){//修改
				dispIdD=dispIdM;
				TtVsDispatchOrderPO tvopo=new TtVsDispatchOrderPO();
				tvopo.setDispId(Long.parseLong(dispIdM));
				
				TtVsDispatchOrderPO tvopn=new TtVsDispatchOrderPO();
				tvopn.setSendWareId(Long.parseLong(sendWareId));
				tvopn.setSendWarehouse(sendWare);
				tvopn.setReceiveWareId(Long.parseLong(receiveWareId));
				tvopn.setReceiveWarehouse(receiveWare);
				tvopn.setSendWay(Integer.parseInt(transType));
				//tvopn.setAddressId(Long.parseLong(receiveAddr));
				tvopn.setAddress(address);
				tvopn.setAccPerson(accPerson);
				tvopn.setAccTel(accPhone);
				tvopn.setRemark(remark);
				//获取城市里程中计划到达天数
				Date now=new Date();   
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
				int planDays=0;
				List<Map<String, Object>> plist=reDao.getPlanDateInfo(sendWareId, receiveWareId);
				if(plist!=null&&plist.size()>0){
					Map<String,Object> map2=plist.get(0);
					planDays=Integer.parseInt(map2.get("ARRIVE_DAYS").toString())+2;//需加上两天出库的天数
				}else{
					planDays=2;
				}
				String dd=sdf.format(new Date(now.getTime()+ planDays* 24 * 60 * 60 * 1000));
				tvopn.setPlanDeliverDate(sdf.parse(dd));//最晚到货日期
//				else{
//					Date now=new Date();   
//					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
//					String dd=sdf.format(new Date(now.getTime()+ 2* 24 * 60 * 60 * 1000));
//					tvopn.setPlanDeliverDate(sdf.parse(dd));
//				}
				
				tvopn.setUpdateBy(logonUser.getUserId());
				tvopn.setUpdateDate(new Date());
				int subNum=0;
				if(commitNums.length>0){
					for(int i=0;i<commitNums.length;i++){
						subNum+=Integer.parseInt(commitNums[i]);
					}
				}
				tvopn.setSubNum(subNum);
				reDao.update(tvopo, tvopn);
				
				//删除原调拨单明细数据
				TtVsDispatchOrderDtlPO tvpdo=new TtVsDispatchOrderDtlPO();
				tvpdo.setDispId(Long.parseLong(dispIdM));
				reDao.delete(tvpdo);
				
			}else{
				act.setOutData("returnValue", "2");
				act.setOutData("returnMsg", "操作失败!");
				return;
			}
			
			
			//向调拨单明细表中插入数据
			List dlist=new ArrayList();
			
			if(materialIds!=null&&materialIds.length>0&&materialIds.length==commitNums.length){
				for(int i=0;i<materialIds.length;i++){
					TtVsDispatchOrderDtlPO tvpd=new TtVsDispatchOrderDtlPO();
					tvpd.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
					tvpd.setDispId(Long.parseLong(dispIdD));
					tvpd.setMaterialId(Long.parseLong(materialIds[i]));
					tvpd.setOrderAmount(Integer.parseInt(commitNums[i]));
					tvpd.setCreateBy(logonUser.getUserId());
					tvpd.setCreateDate(new Date());
					tvpd.setSendWareId(Long.parseLong(sendWareId));
					dlist.add(tvpd);
				}
			}
			if(dlist!=null&&dlist.size()>0){
				reDao.insert(dlist);
			}
			
			//更新车辆在库数量
//			if(materialIds!=null&&materialIds.length>0&&materialIds.length==commitNums.length){
//				for(int i=0;i<materialIds.length;i++){
//					TmVehiclePO tpm=new TmVehiclePO();
//					tpm.setMaterialId(Long.parseLong(materialIds[i]));
//					tpm.setWarehouseId(Long.parseLong(sendWareId));
//					tpm.setLifeCycle(Constant.VEHICLE_LIFE_02);
//					List vlist=reDao.select(tpm);
//					for(int j=0;j<Integer.parseInt(commitNums[i]);j++){//更新车辆数为调拨数量
//						TmVehiclePO tpm2=(TmVehiclePO) vlist.get(j);
//						TmVehiclePO old=new TmVehiclePO();
//						old.setVehicleId(tpm2.getVehicleId());
//						TmVehiclePO newPo=new TmVehiclePO();
//						newPo.setLifeCycle(Constant.VEHICLE_LIFE_10);//订单锁定
//						newPo.setUpdateBy(logonUser.getUserId());
//						newPo.setUpdateDate(new Date());
//						reDao.update(old, newPo);
//					}
//				}
//			}
			act.setOutData("returnValue", "1");
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "地址信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 跳转到修改页面
	 */
	public void toEditDispOrder(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dispId = CommonUtils.checkNull(request.getParamValue("dispId")); //调拨单ID
			//根据调拨单ID获取主信息和明细信息
			// 查询订单基本信息
			Map<String, Object> map = reDao.getOrderMainInfo(dispId);
			act.setOutData("orderMap", map);

			// 返回订单的物料信息
			List<Map<String, Object>> materialList = reDao.getBatchOrderDel(dispId);
			act.setOutData("materialList", materialList);
			
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//仓库LIST
			
			//List<Map<String, Object>> addrlist = reDao.getAddrByReceiveWrList(map.get("RECEIVE_WARE_ID").toString());
			//act.setOutData("addrlist", addrlist);
			
			act.setForword(ORDER_MODIFY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"调拨修改初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 订单申请
	 */
	public void applyAction(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dispId = CommonUtils.checkNull(request.getParamValue("dispId")); //调拨单ID
			//检查可用库存是否满足订单提报数
			
			// 返回订单的物料信息
			List<Map<String, Object>> materialList = reDao.getBatchOrderDel(dispId);
			if(materialList!=null&&materialList.size()>0){
				for(int i=0;i<materialList.size();i++){
					Map<String, Object> map=materialList.get(i);
					if(Integer.parseInt(map.get("ORDER_AMOUNT").toString())>Integer.parseInt(map.get("WARE_NUM").toString())){
						act.setOutData("returnValue", "3");//提报数大于库存可用数
						return;
					}
					
				}
			}else{
				act.setOutData("returnValue", "2");
				return;
			}
			//更新调拨单状态为已提交
			TtVsDispatchOrderPO tvp=new TtVsDispatchOrderPO();
			tvp.setDispId(Long.parseLong(dispId));
			TtVsDispatchOrderPO tvpn=new TtVsDispatchOrderPO();
			tvpn.setOrderStatus(Constant.DB_ORDER_STATUS_02);//已提报
			tvpn.setUpdateBy(logonUser.getUserId());
			tvpn.setUpdateDate(new Date());
			reDao.update(tvp, tvpn);
			
			List tlist=reDao.select(tvp);
			TtVsDispatchOrderPO tvpp=(TtVsDispatchOrderPO)tlist.get(0);
			
			//向发运表中增加一条记录
			TtVsDlvryPO tvdp=new TtVsDlvryPO();
			String reqNo=CommonUtils.getBusNo(Constant.NOCRT_SEND_ORDER_NO, Long.parseLong(Constant.areaIdJZD));
			long reqId=Long.parseLong(SequenceManager.getSequence(""));
			tvdp.setReqId(reqId);
			tvdp.setReqNo(reqNo);
			tvdp.setReqTotal(tvpp.getSubNum().toString());
			tvdp.setReqShipType(tvpp.getSendWay());
			tvdp.setReqDate(new Date());
			tvdp.setReqRecAddrId(tvpp.getAddressId());
			tvdp.setReqRecAddr(tvpp.getAddress());
			//获取收货地址信息
			List<Map<String, Object>> mlist=reDao.getAddrByReceiveWrList(tvpp.getReceiveWareId().toString());
			Map<String, Object> map = mlist.get(0);//reDao.getAddrInfoById(tvpp.getAddressId().toString());
			tvdp.setReqRecProvId(Long.parseLong(map.get("PROV_CODE").toString()));
			tvdp.setReqRecCityId(Long.parseLong(map.get("CITY_CODE").toString()));
			tvdp.setReqRecCountyId(Long.parseLong(map.get("COUNTY_CODE").toString()));
			tvdp.setDlvBalProvId(Long.parseLong(map.get("PROV_CODE").toString()));
			tvdp.setDlvBalCityId(Long.parseLong(map.get("CITY_CODE").toString()));
			tvdp.setDlvBalCountyId(Long.parseLong(map.get("COUNTY_CODE").toString()));
			tvdp.setReqLinkMan(tvpp.getAccPerson());
			tvdp.setReqTel(tvpp.getAccTel());
			tvdp.setReqWhId(tvpp.getSendWareId());
			tvdp.setReqRemark(tvpp.getRemark());
			tvdp.setOrdId(tvpp.getDispId());
			tvdp.setOrdNo(tvpp.getDispNo());
			tvdp.setOrdTotal(tvpp.getSubNum());
			tvdp.setDlvType(Constant.DELIVERY_ORD_TYPE_ALLOCAT);//调拨单
			tvdp.setDlvShipType(tvpp.getSendWay());
			tvdp.setDlvStatus(Constant.ORDER_STATUS_01);//发运状态：未分派
			
			Date now=new Date();   
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
			String dd=sdf.format(new Date(now.getTime()+ 2* 24 * 60 * 60 * 1000));
			tvdp.setDlvFyDate(sdf.parse(dd));//最晚发运日期
			tvdp.setDlvJjDate(tvpp.getPlanDeliverDate());
			tvdp.setReqRecWhId(tvpp.getReceiveWareId());
			tvdp.setDlvRecWhId(tvpp.getReceiveWareId());
			tvdp.setDlvWhId(tvpp.getSendWareId());
			tvdp.setCreateBy(logonUser.getUserId());
			tvdp.setCreateDate(new Date());
			reDao.insert(tvdp);
			
			//向明细表添加数据
			List dlist=new ArrayList();
			if(materialList!=null&&materialList.size()>0){
				for(int i=0;i<materialList.size();i++){
					Map<String, Object> mm=materialList.get(i);
					TtVsDlvryDtlPO tvdd=new TtVsDlvryDtlPO();
					String detailId=SequenceManager.getSequence("");
					tvdd.setReqDetailId(Long.parseLong(detailId));
					tvdd.setReqId(reqId);
					tvdd.setOrdId(tvpp.getDispId());
					tvdd.setOrdDetailId(Long.parseLong(mm.get("DETAIL_ID").toString()));
					tvdd.setMaterialId(Long.parseLong(mm.get("MATERIAL_ID").toString()));
					tvdd.setOrdTotal(Integer.parseInt(mm.get("ORDER_AMOUNT").toString()));
					tvdd.setReqTotal(Integer.parseInt(mm.get("ORDER_AMOUNT").toString()));
					tvdd.setCreateBy(logonUser.getUserId());
					tvdd.setCreateDate(new Date());
					
					dlist.add(tvdd);
				}
				
			}
			if(dlist!=null&&dlist.size()>0){
				reDao.insert(dlist);
			}
			//向资源锁定表新增记录
			TtVsDlvryDtlPO tvpd=new TtVsDlvryDtlPO();
			tvpd.setReqId(reqId);
			List dilist=reDao.select(tvpd);
			List alist=new ArrayList();
			if(dilist!=null&&dilist.size()>0){
				for(int i=0;i<dilist.size();i++){
					TtVsDlvryDtlPO tvdo=(TtVsDlvryDtlPO) dilist.get(i);
					TtVsOrderResourceReservePO tvr=new TtVsOrderResourceReservePO();
					tvr.setReserveId(Long.parseLong(SequenceManager.getSequence("")));
					//tvr.setOrderDetailId(tvdo.getReqDetailId());
					tvr.setReqDetailId(tvdo.getReqDetailId());
					tvr.setMaterialId(tvdo.getMaterialId());
					tvr.setAmount(tvdo.getOrdTotal());
					tvr.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);//保留状态：保留
					tvr.setCreateBy(logonUser.getUserId());
					tvr.setCreateDate(new Date());
					tvr.setCreateId(tvdo.getReqId());
					tvr.setWarehouseCode(tvpp.getSendWareId().toString());
					tvr.setWarehouseId(tvpp.getSendWareId());
					tvr.setOemCompanyId(logonUser.getCompanyId());
					alist.add(tvr);
				}
				reDao.insert(alist);
			}else{
				act.setOutData("returnValue", "2");
				return;
			}
			act.setOutData("returnValue", "1");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"调拨订单提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 调拨单删除
	 */
	public void delDispatchOrder(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dispId = CommonUtils.checkNull(request.getParamValue("dispId")); //调拨单ID
			//删除调拨单明细数据
			TtVsDispatchOrderDtlPO tvpdo=new TtVsDispatchOrderDtlPO();
			tvpdo.setDispId(Long.parseLong(dispId));
			reDao.delete(tvpdo);
			
			//删除调拨单主数据
			TtVsDispatchOrderPO tvp=new TtVsDispatchOrderPO();
			tvp.setDispId(Long.parseLong(dispId));
			reDao.delete(tvp);
			
			act.setOutData("returnValue", "1");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"调拨订单提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 车厂调拨入库
	 */
	
	public void stockInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			//warehouseList();
			//Integer oemFlag = CommonUtils.getNowSys(Long.parseLong(logonUser.getOemCompanyId()));

			//act.setOutData("oemFlag", oemFlag);
			getWarehouse();
			String poseId = logonUser.getPoseId().toString();
			String dealerIds = logonUser.getDealerId();
			List<Map<String,Object>> lists=reDao.getCheckLists(poseId, dealerIds, null, null,null, Constant.PAGE_SIZE, 1);;
			act.setOutData("lists",lists);
			int flag=0;
			if(lists.size()>0){
				flag=1;
			}
			act.setOutData("flag", flag);
			act.setForword(StockInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 查询可车辆验收的车辆列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-5-20
	 */
	public void stockCheckList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerIds = logonUser.getDealerId();

			// 得到VIN
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String dlvNo = CommonUtils.checkNull(request.getParamValue("dlvNo")); // 获取发运申请单号
			String dlvryNo = CommonUtils.checkNull(request.getParamValue("dlvryNo")); // 获取发运单号
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			String poseId = logonUser.getPoseId().toString();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = reDao.getCheckList(poseId, dealerIds, vin, dlvNo,dlvryNo, Constant.PAGE_SIZE, curPage);
			List<Map<String, Object>> lists= reDao.getCheckLists(poseId, dealerIds, vin, dlvNo,dlvryNo, Constant.PAGE_SIZE, curPage);
			act.setOutData("lists",lists);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询可验收车辆");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * 获取仓库
	 */
	
	public void getWarehouse(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			//String dealerId=logonUser.getDealerId().toString();
			//获取仓库
			List<Map<String,Object>> list =reDao.getWarehouseList();
			act.setOutData("list", list);
			//act.setOutData("rebateAmount", list.get(0).getRebateAmount());
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 调拨入库批量验收
	 */
	public void checkAllSubmit() {
		AclUserBean logonUser = null;
		try {

			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String arrive_date = CommonUtils.checkNull(request.getParamValue("arrive_date")); // 实际到车日期
			String arrive_time = CommonUtils.checkNull(request.getParamValue("arrive_time")); // 实际到车时间
			String inspection_person = CommonUtils.checkNull(request.getParamValue("inspection_person")); // 验收人员
			String warehouseId = CommonUtils.checkNull(request.getParamValue("warehouse__")); // 车辆所在位置
			String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注
			//String[] dlvryDtlIds = request.getParamValues("dlvryDtlIds");
			String[] vehicleIds = request.getParamValues("dlvryDtlIds");
			String dealerId = logonUser.getDealerId();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			//if (null != dlvryDtlIds) {
			if (null != vehicleIds) {
				//int len = dlvryDtlIds.length;
				int len = vehicleIds.length;
				for (int i = 0; i < len; i++) {
					//System.out.println(dlvryDtlIds[i]);

					//TtVsDlvryDtlPO ttVsDlvryDtlPO = new TtVsDlvryDtlPO();
					//ttVsDlvryDtlPO.setDetailId(new Long(dlvryDtlIds[i]));
					//ttVsDlvryDtlPO = (ttVsDlvryDtlPO) reDao.select(ttVsDlvryDtlPO).get(0);

					 String vehicleId =vehicleIds[i];
					// 1、修改节点状态：经销商在库
					TmVehiclePO tempVehiclePO = new TmVehiclePO();
					tempVehiclePO.setVehicleId(Long.valueOf(vehicleId));
					Map<String,Object> map=reDao.getCheckListByVehicleId(vehicleId);
					String reqId=map.get("REQ_ID").toString();

					TmVehiclePO valueVehiclePO = new TmVehiclePO();
					valueVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_02);// 修改车辆生命周期：车厂在库
					//valueVehiclePO.setDealerId(new Long(logonUser.getDealerId()));
					valueVehiclePO.setStorageDate(format.parse(arrive_date)); // 车辆验收日期
					valueVehiclePO.setWarehouseId(Long.valueOf(warehouseId)); // 车辆所在位置，存放车厂仓库ID
					reDao.update(tempVehiclePO, valueVehiclePO); // 完成车辆节点更新

					// 从车辆表中查询dealerID
					// TmVehiclePO tmVehiclePO = new TmVehiclePO();
					// tmVehiclePO.setVehicleId(Long.parseLong(vehicleId));

					// 2、向“经销商验收(TT_INSPECTION)”写入验收基本信息
					TtVsInspectionPO inspectionPO = new TtVsInspectionPO();
					Long inspectionId = Long.parseLong(SequenceManager.getSequence(""));
					String oemComId = logonUser.getCompanyId().toString();
					inspectionPO.setCompanyId(Long.parseLong(oemComId));
					inspectionPO.setInspectionId(inspectionId); // 验收ID
					inspectionPO.setVehicleId(Long.valueOf(vehicleId)); // 车辆ID
					inspectionPO.setArriveDate(format.parse(arrive_date)); // 到达日期
					inspectionPO.setArriveTime(arrive_time); // 到达时间
					String inspection_no = "YS" + inspectionId.toString(); // 验收单号：YS+验收ID
					inspectionPO.setInspectionNo(inspection_no.trim()); // 验收单号
					inspectionPO.setInspectionPerson(inspection_person.trim()); // 验收人
					inspectionPO.setVehicleArea(warehouseId.toString()); // 车辆位置，存放经销商仓库ID
					inspectionPO.setOperateDealer(Long.parseLong(warehouseId)); // 收货方

					if (null != remark && !"".equals(remark)) {
						inspectionPO.setRemark(remark.trim()); // 备注
					}
					inspectionPO.setCreateBy(logonUser.getUserId());
					inspectionPO.setCreateDate(new Date());
					// inspectionPO.setMatchId(matchId); // 配车表id
					//inspectionPO.setDlvryDtlId(ttVsDlvryErpDtlPO.getDetailId()); // 发运明细id

					reDao.insert(inspectionPO);

					//TtVsDlvryErpDtlPO tvdedA = new TtVsDlvryErpDtlPO();
					//TtVsDlvryErpDtlPO tvdedB = new TtVsDlvryErpDtlPO();
					//tvdedA.setDetailId(ttVsDlvryErpDtlPO.getDetailId());
					//tvdedB.setDetailId(ttVsDlvryErpDtlPO.getDetailId());
					//tvdedB.setIsReceive(new Long(Constant.IS_RECEIVE_1));// 车辆是否验收,修改为已验收
					//tvdedB.setUpdateDate(new Date());// 修改时间
					//reDao.update(tvdedA, tvdedB); // 修改配车验收状态
					
					//**验收更改tt_vs_dlvry dlv_ys_total
					//TtVsDlvryErpDtlPO tvdedQ = new TtVsDlvryErpDtlPO();
					//tvdedQ.setDetailId(ttVsDlvryErpDtlPO.getDetailId());
					//tvdedQ=(TtVsDlvryErpDtlPO)reDao.select(tvdedQ).get(0);
					TmVehiclePO tvp=new TmVehiclePO();
					tvp.setVehicleId(Long.valueOf(vehicleId));
					tvp=(TmVehiclePO)reDao.select(tvp).get(0);//查出车辆信息
					
					TtVsDlvryDtlPO tvdp=new TtVsDlvryDtlPO();
					TtVsDlvryDtlPO tvdpQ=new TtVsDlvryDtlPO();
					tvdp.setReqId(Long.valueOf(reqId));
					tvdp.setMaterialId(tvp.getMaterialId());
					tvdpQ.setReqId(Long.valueOf(reqId));
					tvdpQ.setMaterialId(tvp.getMaterialId());
					tvdpQ=(TtVsDlvryDtlPO) reDao.select(tvdpQ).get(0);//根据req_id 和物料ID更新子表
					int ys_total=0;
					if(tvdpQ.getYsTotal()==null||"".equals(tvdpQ.getYsTotal())){
						ys_total=0;
					}else{
						ys_total=tvdpQ.getYsTotal();
					}
					TtVsDlvryDtlPO tvdp1=new TtVsDlvryDtlPO();
					tvdp1.setYsTotal(ys_total+1);
					tvdp1.setUpdateBy(logonUser.getUserId());
					tvdp1.setUpdateDate(new Date());
					reDao.update(tvdp, tvdp1); //更新子表验收数量
					
					TtVsDlvryPO tp=new TtVsDlvryPO();
					TtVsDlvryPO tpQ=new TtVsDlvryPO();
					tp.setReqId(tvdp.getReqId());
					tpQ.setReqId(tvdp.getReqId());
					int dlvystotal=0;
					tpQ=(TtVsDlvryPO) reDao.select(tpQ).get(0);//根据req_id 和物料ID更新子表
					if(tpQ.getDlvYsTotal()==null||"".equals(tpQ.getDlvYsTotal())){
						dlvystotal=0;
					}else{
						dlvystotal=tpQ.getDlvYsTotal();
					}
					TtVsDlvryPO tp1=new TtVsDlvryPO();
					tp1.setDlvYsTotal(dlvystotal+1);
					tp1.setUpdateBy(logonUser.getUserId());
					tp1.setUpdateDate(new Date());//更新主表验收数量
					reDao.update(tp,tp1);
					/*
					 * 根据vehicleId查询该车辆的dealerId，并关联到订单表，查询订单的“定货方id”、“付款方id” 如果
					 * 车辆表的dealerId==定货方id && 车辆表的dealerId != 付款方id，说明此车为二级经销商订购
					 * 则系统自动生成“车辆调拨记录”： 调出方：付款方id 调入方：定货方id by Davey
					 */

					// 3、向TT_VS_VHCL_CHNG写入变更日志
					TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
					Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
					chngPO.setVhclChangeId(vhclChangeId); // 改变序号
					chngPO.setVehicleId(Long.valueOf(vehicleId)); // 车辆ID
					chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
					chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
					//chngPO.setDealerId(Long.parseLong(dealerId)); // 经销商ID
					chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型:库存状态更改
					chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_01.toString()); // 改变名称:验收入库
					chngPO.setChangeDate(new Date()); // 改变时间
					if (null != remark && !"".equals(remark)) {
						chngPO.setChangeDesc(remark.trim()); // 改变描述
					}
					chngPO.setCreateDate(new Date()); // 记录创建日期
					chngPO.setCreateBy(logonUser.getUserId()); // 记录创建者
					reDao.insert(chngPO);

					//String[] vId = { Long.valueOf(vehicleId) + "" };
					//this.callOther(vId, remark, "13071002", dealerId, null);

				}

			}

			// act.setForword(CheckVehicleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收车辆错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 单个审核初始化
	 */
	public void toCheck() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			String vehicle_id = CommonUtils.checkNull(request.getParamValue("vehlId"));
			Map<String,Object> vehicleInfo=reDao.getCheckListByVehicleId(vehicle_id);
		//Map<String, Object> vehicleInfo = CheckVehicleDAO.getVehicleInfo(vehicle_id); // 根据vehicle_id查询车辆详细信息

			getWarehouse();

			act.setOutData("vehicleInfo", vehicleInfo);
			act.setOutData("vehicle_id", vehicle_id);
			act.setForword(StockVehicleDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查看入库车辆信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 调拨入库单个验收
	 */
	public void checkSubmit() {
		AclUserBean logonUser = null;
		try {

			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String arrive_date = CommonUtils.checkNull(request.getParamValue("arrive_date")); // 实际到车日期
			String arrive_time = CommonUtils.checkNull(request.getParamValue("arrive_time")); // 实际到车时间
			String inspection_person = CommonUtils.checkNull(request.getParamValue("inspection_person")); // 验收人员
			String warehouseId = CommonUtils.checkNull(request.getParamValue("warehouse__")); // 车辆所在位置
			String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注
			//String[] dlvryDtlIds = request.getParamValues("dlvryDtlIds");
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicleId"));
			//String dealerId = logonUser.getDealerId();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			//if (null != dlvryDtlIds) {
			if (null != vehicleId) {
				//int len = dlvryDtlIds.length;
				//int len = vehicleIds.length;
				//for (int i = 0; i < len; i++) {
					//System.out.println(dlvryDtlIds[i]);

					//TtVsDlvryDtlPO ttVsDlvryDtlPO = new TtVsDlvryDtlPO();
					//ttVsDlvryDtlPO.setDetailId(new Long(dlvryDtlIds[i]));
					//ttVsDlvryDtlPO = (ttVsDlvryDtlPO) reDao.select(ttVsDlvryDtlPO).get(0);

					 //String vehicleId =vehicleIds;
					// 1、修改节点状态：经销商在库
					TmVehiclePO tempVehiclePO = new TmVehiclePO();
					tempVehiclePO.setVehicleId(Long.valueOf(vehicleId));
					Map<String,Object> map=reDao.getCheckListByVehicleId(vehicleId);
					String reqId=map.get("REQ_ID").toString();

					TmVehiclePO valueVehiclePO = new TmVehiclePO();
					valueVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_02);// 修改车辆生命周期：车厂在库
					//valueVehiclePO.setDealerId(new Long(logonUser.getDealerId()));
					valueVehiclePO.setStorageDate(format.parse(arrive_date)); // 车辆验收日期
					valueVehiclePO.setWarehouseId(Long.valueOf(warehouseId)); // 车辆所在位置，存放车厂仓库ID
					reDao.update(tempVehiclePO, valueVehiclePO); // 完成车辆节点更新

					// 从车辆表中查询dealerID
					// TmVehiclePO tmVehiclePO = new TmVehiclePO();
					// tmVehiclePO.setVehicleId(Long.parseLong(vehicleId));

					// 2、向“经销商验收(TT_INSPECTION)”写入验收基本信息
					TtVsInspectionPO inspectionPO = new TtVsInspectionPO();
					Long inspectionId = Long.parseLong(SequenceManager.getSequence(""));
					String oemComId = logonUser.getCompanyId().toString();
					inspectionPO.setCompanyId(Long.parseLong(oemComId));
					inspectionPO.setInspectionId(inspectionId); // 验收ID
					inspectionPO.setVehicleId(Long.valueOf(vehicleId)); // 车辆ID
					inspectionPO.setArriveDate(format.parse(arrive_date)); // 到达日期
					inspectionPO.setArriveTime(arrive_time); // 到达时间
					String inspection_no = "YS" + inspectionId.toString(); // 验收单号：YS+验收ID
					inspectionPO.setInspectionNo(inspection_no.trim()); // 验收单号
					inspectionPO.setInspectionPerson(inspection_person.trim()); // 验收人
					inspectionPO.setVehicleArea(warehouseId.toString()); // 车辆位置，存放经销商仓库ID
					inspectionPO.setOperateDealer(Long.parseLong(warehouseId)); // 收货方

					if (null != remark && !"".equals(remark)) {
						inspectionPO.setRemark(remark.trim()); // 备注
					}
					inspectionPO.setCreateBy(logonUser.getUserId());
					inspectionPO.setCreateDate(new Date());
					// inspectionPO.setMatchId(matchId); // 配车表id
					//inspectionPO.setDlvryDtlId(ttVsDlvryErpDtlPO.getDetailId()); // 发运明细id

					reDao.insert(inspectionPO);

					//TtVsDlvryErpDtlPO tvdedA = new TtVsDlvryErpDtlPO();
					//TtVsDlvryErpDtlPO tvdedB = new TtVsDlvryErpDtlPO();
					//tvdedA.setDetailId(ttVsDlvryErpDtlPO.getDetailId());
					//tvdedB.setDetailId(ttVsDlvryErpDtlPO.getDetailId());
					//tvdedB.setIsReceive(new Long(Constant.IS_RECEIVE_1));// 车辆是否验收,修改为已验收
					//tvdedB.setUpdateDate(new Date());// 修改时间
					//reDao.update(tvdedA, tvdedB); // 修改配车验收状态
					
					//**验收更改tt_vs_dlvry dlv_ys_total
					//TtVsDlvryErpDtlPO tvdedQ = new TtVsDlvryErpDtlPO();
					//tvdedQ.setDetailId(ttVsDlvryErpDtlPO.getDetailId());
					//tvdedQ=(TtVsDlvryErpDtlPO)reDao.select(tvdedQ).get(0);
					TmVehiclePO tvp=new TmVehiclePO();
					tvp.setVehicleId(Long.valueOf(vehicleId));
					tvp=(TmVehiclePO)reDao.select(tvp).get(0);//查出车辆信息
					
					TtVsDlvryDtlPO tvdp=new TtVsDlvryDtlPO();
					TtVsDlvryDtlPO tvdpQ=new TtVsDlvryDtlPO();
					tvdp.setReqId(Long.valueOf(reqId));
					tvdp.setMaterialId(tvp.getMaterialId());
					tvdpQ.setReqId(Long.valueOf(reqId));
					tvdpQ.setMaterialId(tvp.getMaterialId());
					tvdpQ=(TtVsDlvryDtlPO) reDao.select(tvdpQ).get(0);//根据req_id 和物料ID更新子表
					int ys_total=0;
					if(tvdpQ.getYsTotal()==null||"".equals(tvdpQ.getYsTotal())){
						ys_total=0;
					}else{
						ys_total=tvdpQ.getYsTotal();
					}
					TtVsDlvryDtlPO tvdp1=new TtVsDlvryDtlPO();
					tvdp1.setYsTotal(ys_total+1);
					tvdp1.setUpdateBy(logonUser.getUserId());
					tvdp1.setUpdateDate(new Date());
					reDao.update(tvdp, tvdp1); //更新子表验收数量
					
					TtVsDlvryPO tp=new TtVsDlvryPO();
					TtVsDlvryPO tpQ=new TtVsDlvryPO();
					tp.setReqId(tvdp.getReqId());
					tpQ.setReqId(tvdp.getReqId());
					int dlvystotal=0;
					tpQ=(TtVsDlvryPO) reDao.select(tpQ).get(0);//根据req_id 和物料ID更新子表
					if(tpQ.getDlvYsTotal()==null||"".equals(tpQ.getDlvYsTotal())){
						dlvystotal=0;
					}else{
						dlvystotal=tpQ.getDlvYsTotal();
					}
					TtVsDlvryPO tp1=new TtVsDlvryPO();
					tp1.setDlvYsTotal(dlvystotal+1);
					tp1.setUpdateBy(logonUser.getUserId());
					tp1.setUpdateDate(new Date());//更新主表验收数量
					reDao.update(tp,tp1);
					/*
					 * 根据vehicleId查询该车辆的dealerId，并关联到订单表，查询订单的“定货方id”、“付款方id” 如果
					 * 车辆表的dealerId==定货方id && 车辆表的dealerId != 付款方id，说明此车为二级经销商订购
					 * 则系统自动生成“车辆调拨记录”： 调出方：付款方id 调入方：定货方id by Davey
					 */

					// 3、向TT_VS_VHCL_CHNG写入变更日志
					TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
					Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
					chngPO.setVhclChangeId(vhclChangeId); // 改变序号
					chngPO.setVehicleId(Long.valueOf(vehicleId)); // 车辆ID
					chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
					chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
					//chngPO.setDealerId(Long.parseLong(dealerId)); // 经销商ID
					chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型:库存状态更改
					chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_01.toString()); // 改变名称:验收入库
					chngPO.setChangeDate(new Date()); // 改变时间
					if (null != remark && !"".equals(remark)) {
						chngPO.setChangeDesc(remark.trim()); // 改变描述
					}
					chngPO.setCreateDate(new Date()); // 记录创建日期
					chngPO.setCreateBy(logonUser.getUserId()); // 记录创建者
					reDao.insert(chngPO);

					//String[] vId = { Long.valueOf(vehicleId) + "" };
					//this.callOther(vId, remark, "13071002", dealerId, null);

				}

			//}

			act.setForword(StockInitUrl);
			//stockInit(); 
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收车辆错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
