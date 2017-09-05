package com.infodms.dms.actions.sales.storage.sendmanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.AllocaDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtSalesAllocaDePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : AllocaManage 
 * @Description   : 配车管理控制类
 * @author        : ranjian
 * CreateDate     : 2013-4-23
 */
public class AllocaManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final AllocaDao reDao = AllocaDao.getInstance();
	private final StorageUtil stoUtil=StorageUtil.getInstance();
	private final String allocaListInitUtl = "/jsp/sales/storage/sendmanage/allocaManage/allocaManageList.jsp";
	private final String allocaSetInitUtl = "/jsp/sales/storage/sendmanage/allocaManage/allocaSet.jsp";
	private final String allocaSetZDInitUtl = "/jsp/sales/storage/sendmanage/allocaManage/allocaZDSet.jsp";
	/**
	 * 
	 * @Title      : 
	 * @Description: 配车管理列表页面初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void allocaManageInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(allocaListInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车管理列表页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 配车管理查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void allocaManageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String raiseStartDate = CommonUtils.checkNull(request.getParamValue("RAISE_STARTDATE")); //提报日期开始
			String raiseEndDate = CommonUtils.checkNull(request.getParamValue("RAISE_ENDDATE")); // 提报日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN查询
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 处理类型
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raiseStartDate", raiseStartDate);
			map.put("raiseEndDate", raiseEndDate);
			map.put("boNo", boNo);
			map.put("VIN", vin);
			map.put("poseId", logonUser.getPoseId().toString());
			
			
			if("1".equals(common)){
				Map<String, Object> valueMap = reDao.tgSum(map);
				act.setOutData("valueMap", valueMap);	
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getAllocaSeachQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配车管理查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:初始化配车页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void updateAllocaManageInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = CommonUtils.checkNull(request.getParamValue("Id"));//组板ID
			List<Object> params=new ArrayList<Object>();
			params.add(boId);
			Map<String,Object> sendBoardMap=SendBoardSeachDao.getInstance().getBoardByBoId(params);
			Map<Object,List<Map<String, Object>>>  boByVeMap= new HashMap<Object,List<Map<String, Object>>>() ;//根据某组板明细获取该明细下的车辆信息
			List<Map<String, Object>> list = reDao.getSendBoardMatListQuery(params);//查询组板明细信息
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> map=list.get(i);
					List<Object> paramsOrg =new ArrayList<Object>();
					paramsOrg.add(map.get("BO_DE_ID"));
					paramsOrg.add(Constant.RESOURCE_RESERVE_STATUS_01);
					List<Map<String, Object>> rList= reDao.getOrgQueryByBoDeId(paramsOrg); //查询各省资源数
					String org="ORG_"+map.get("BO_DE_ID");					
					List<Object> paramsVe=new ArrayList<Object>();
					paramsVe.add(map.get("BO_DE_ID"));//组板详细表ID
					List<Map<String, Object>> vehicleList= reDao.getVehicleQueryByBoDeId(paramsVe); //查询配车明细表
					boByVeMap.put(map.get("BO_DE_ID"), vehicleList);
					boByVeMap.put(org, rList);
				}
			}		
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setOutData("boId", boId);
			act.setOutData("boByVeMap", boByVeMap);
			act.setOutData("list", list);
			act.setForword(allocaSetInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车修改信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:初始化配车页面（自动）
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void updateAllocaManageZDInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = CommonUtils.checkNull(request.getParamValue("Id"));//组板ID
			List<Object> params=new ArrayList<Object>();
			params.add(boId);
			Map<String,Object> sendBoardMap=SendBoardSeachDao.getInstance().getBoardByBoId(params);
			Map<Object,List<Map<String, Object>>>  boByVeMap= new HashMap<Object,List<Map<String, Object>>>() ;//根据某组板明细获取该明细下的车辆信息
			List<Map<String, Object>> list = reDao.getSendBoardMatListQuery(params);//查询组板明细信息
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> map=list.get(i);
					List<Object> paramsOrg =new ArrayList<Object>();
					paramsOrg.add(map.get("BO_DE_ID"));
					paramsOrg.add(Constant.RESOURCE_RESERVE_STATUS_01);
					List<Map<String, Object>> rList= reDao.getOrgQueryByBoDeId(paramsOrg); //查询各省资源数
					String org="ORG_"+map.get("BO_DE_ID");					
					List<Object> paramsVe=new ArrayList<Object>();
					paramsVe.add(map.get("BO_DE_ID"));//组板详细表ID
					List<Map<String, Object>> vehicleList= reDao.getVehicleQueryByBoDeId(paramsVe); //查询配车明细表
					boByVeMap.put(map.get("BO_DE_ID"), vehicleList);
					boByVeMap.put(org, rList);
				}
			}		
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setOutData("boId", boId);
			act.setOutData("boByVeMap", boByVeMap);
			act.setOutData("list", list);
			act.setForword(allocaSetZDInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车修改信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:初始化配车页面（ONE key）
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void oneKey(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = CommonUtils.checkNull(request.getParamValue("boId"));//组板ID
			List<Object> params=new ArrayList<Object>();
			params.add(boId);
			Map<String,Object> sendBoardMap=SendBoardSeachDao.getInstance().getBoardByBoId(params);
			Map<Object,List<Map<String, Object>>>  boByVeMap= new HashMap<Object,List<Map<String, Object>>>() ;//根据某组板明细获取该明细下的车辆信息
			List<Map<String, Object>> list = reDao.getSendBoardMatListQuery(params);//查询组板明细信息
			String vehicleIds="";
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					
					Map<String, Object> map=list.get(i);
					List<Object> paramsOrg =new ArrayList<Object>();
					paramsOrg.add(map.get("BO_DE_ID"));
					paramsOrg.add(Constant.RESOURCE_RESERVE_STATUS_01);
					List<Map<String, Object>> rList= reDao.getOrgQueryByBoDeId(paramsOrg); //查询各省资源数
					String org="ORG_"+map.get("BO_DE_ID");					
					List<Map<String, Object>> vehicleList= reDao.getOneChooseVehicle(map.get("MATERIAL_ID").toString(),map.get("THIS_BOARD_NUM").toString(),vehicleIds);
					if(vehicleList!=null && vehicleList.size()>0){
						for(int s=0;s<vehicleList.size();s++){
							vehicleIds+=vehicleList.get(s).get("VEHICLE_ID").toString()+",";
						}
					}
					map.put("ALLOCA_NUM", vehicleList.size());
					boByVeMap.put(map.get("BO_DE_ID"), vehicleList);
					boByVeMap.put(org, rList);
				}
			}		
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setOutData("boId", boId);
			act.setOutData("boByVeMap", boByVeMap);
			act.setOutData("list", list);
			act.setForword(allocaSetZDInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"一键信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:配车处理逻辑主方法
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void allocaManageMain(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId=CommonUtils.checkNull(request.getParamValue("boId"));//组板ID
			String[] boDeId=request.getParamValues("BO_DE_ID");//组板明细ID
			String[] vehicleIds=request.getParamValues("VEHICLE_IDS");//新的列表车辆IDS
			String[] boDeIds=request.getParamValues("BO_DE_IDS");//配车列表组板明细IDS
			String[] newDetailId=request.getParamValues("NEW_DETAIL_ID");//新配车表ID（页面修改后返回的列表）
			/*****************************配车详细表添加START***************************/
			//首先判断是否是页面先加车辆信息（新添加的用有标记确认，后台好处理）
			if(boDeId!=null && boDeId.length>0){
				//首先删除页面已删除旧的数据
				List<Map<String, Object>> otherMatIdList=reDao.getNotMatByMatId(newDetailId,boDeId);
				if(otherMatIdList!=null && otherMatIdList.size()>0){
					for(int dl=0;dl<otherMatIdList.size();dl++){
						Map<String, Object> dlMap=otherMatIdList.get(dl);
						TmVehiclePO t=new TmVehiclePO();
						t.setOutStatus(Constant.PAYMENT_STATUS_01);//未还款
						t.setLockStatus(Constant.LOCK_STATUS_01);//正常状态
						t.setDealerId(Long.valueOf(Constant.SIT_VELID));//收货经销商-1
						t.setUpdateBy(logonUser.getUserId());
						t.setUpdateDate(new Date());
						TmVehiclePO t1=new TmVehiclePO();
						t1.setVehicleId(Long.valueOf(dlMap.get("VEHICLE_ID").toString()));
						reDao.update(t1, t);
						List<Object> delprarm=new ArrayList<Object>();//删除配车明细信息参数
						delprarm.add(dlMap.get("DETAIL_ID"));
						reDao.delAllocaDeById(delprarm);
					}
				}
				//根据车辆ID 过滤一次，防止同步操作情况
				List<Map<String, Object>> otherVehicleIdList=reDao.getVehicleIdsByMatId(vehicleIds);
				//记录已占用的数据（同步已保存的数据）
				String infoMsg="";
				//然后添加
				if(newDetailId!=null && newDetailId.length>0){//新配车表ID数组大于0表示有配车信息
					for(int i=0;i<newDetailId.length;i++){
						if(newDetailId[i].equals("newVechile")){//新添加的，直接入库
							boolean vBool=false;//判断是否已占用
							if(otherVehicleIdList!=null && otherVehicleIdList.size()>0){
								for(int x=0;x<otherVehicleIdList.size();x++){
									Map<String, Object> map=(Map<String, Object>)otherVehicleIdList.get(x);
									if((map.get("VEHICLE_ID")+"").equals(vehicleIds[i])){
										infoMsg+="["+map.get("AREA_NAME")+"]区-"+"["+map.get("ROAD_NAME")+"]道-"+"["+map.get("SIT_NAME")+"]位;";
										vBool=true;
										break;
									}
								}
							}
							if(vBool!=true){//不等于TRUE 说明未被占用
								String detailId=SequenceManager.getSequence("");
								TtSalesAllocaDePO po=new TtSalesAllocaDePO();
								po.setDetailId(Long.parseLong(detailId));//配车明细ID
								po.setBoDeId(Long.parseLong(boDeIds[i].toString()));//组板明细ID
								po.setVehicleId(Long.parseLong(vehicleIds[i].toString()));//车辆ID
								po.setAllocaPer(logonUser.getUserId());//配车人
								po.setAllocaDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//配车时间
								po.setCreateBy(logonUser.getUserId());//创建人
								po.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
								po.setIsOut(Constant.IF_TYPE_NO);//是否出库（默认为否）
								po.setIsSend(Constant.IF_TYPE_NO);//是否发运（默认为否）
								po.setIsAcc(Constant.IF_TYPE_NO);//是否验收（默认为否）
								reDao.addAllocaDe(po);
								//修改车辆信息[是中转库 未付款，其他已付款，是带交车 发运经销商位DEAR_ID]
								List<Object> prarmStatus=new ArrayList<Object>();//修改车辆信息表状态的参数
								prarmStatus.add(Constant.ORDER_TYPE_04);//中转库单
								prarmStatus.add(Constant.PAYMENT_STATUS_01);//付款方式（未付款）
								prarmStatus.add(Constant.PAYMENT_STATUS_03);//付款方式（已付款）
								prarmStatus.add(boDeIds[i].toString());//组板明细ID
								prarmStatus.add(Constant.LOCK_STATUS_08);//锁定状态
								//prarmStatus.add(Constant.TRANSPORT_TYPE_03);//发运方式   代交车
								prarmStatus.add(boDeIds[i].toString());//组板明细ID
								prarmStatus.add(logonUser.getUserId());//操作人
								prarmStatus.add(vehicleIds[i]);//车辆ID
								reDao.updateVehicleOutStatus(prarmStatus);
							}
						}
					}
					
				}
			/*****************************配车详细表添加END*****************************/
					stoUtil.updateAllocaTime(Long.parseLong(boId), logonUser.getUserId());
					stoUtil.updateBoardOrAllocaNum(Long.parseLong(boId), logonUser.getUserId());//修改所有表的配车数量
					act.setOutData("infoMsg", infoMsg);//占用信息
					act.setOutData("returnValue", 1);//处理成功			
			}else{
				act.setOutData("returnValue", 2);//处理失败（无数据）
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车处理逻辑主方法");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
