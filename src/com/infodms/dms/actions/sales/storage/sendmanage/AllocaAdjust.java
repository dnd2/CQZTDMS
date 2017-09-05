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
import com.infodms.dms.dao.sales.storage.sendManage.AllocaAdjustDao;
import com.infodms.dms.dao.sales.storage.sendManage.AllocaDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtSalesAllocaDePO;
import com.infodms.dms.po.TtSalesBoDetailPO;
import com.infodms.dms.po.TtSalesChaHisPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : AllocaAdjust 
 * @Description   : 配车更改控制类
 * @author        : ranjian
 * CreateDate     : 2013-4-26
 */
public class AllocaAdjust {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final AllocaAdjustDao reDao = AllocaAdjustDao.getInstance();
	private final AllocaDao aDao = AllocaDao.getInstance();
	private final StorageUtil stoUtil=StorageUtil.getInstance();
	private final String allocaListInitUtl = "/jsp/sales/storage/sendmanage/allocaAdjust/allocaAdjustList.jsp";
	private final String allocaAdjustInitUtl = "/jsp/sales/storage/sendmanage/allocaAdjust/allocaAdjust.jsp";
	/**
	 * 
	 * @Title      : 
	 * @Description: 配车管理列表页面初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void allocaAdjustInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(allocaListInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车更改列表页面初始化");
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
	 * LastDate    : 2013-4-26
	 */
	public void allocaAdjustQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String raiseStartDate = CommonUtils.checkNull(request.getParamValue("RAISE_STARTDATE")); //提报日期开始
			String raiseEndDate = CommonUtils.checkNull(request.getParamValue("RAISE_ENDDATE")); // 提报日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN查询
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raiseStartDate", raiseStartDate);
			map.put("raiseEndDate", raiseEndDate);
			map.put("boNo", boNo);
			map.put("VIN", vin);
			map.put("poseId", logonUser.getPoseId().toString());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getAllocaSeachQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
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
	 * LastDate    : 2013-4-26
	 */
	public void updateAllocaAdjustInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = CommonUtils.checkNull(request.getParamValue("Id"));//组板ID
			List<Object> params=new ArrayList<Object>();
			params.add(boId);
			Map<String,Object> sendBoardMap=SendBoardSeachDao.getInstance().getBoardByBoId(params);
			Map<Object,List<Map<String, Object>>>  boByVeMap= new HashMap<Object,List<Map<String, Object>>>() ;//根据某组板明细获取该明细下的车辆信息
			List<Map<String, Object>> list = aDao.getSendBoardMatListQuery(params);//查询组板明细信息
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> map=list.get(i);
					List<Object> paramsVe=new ArrayList<Object>();
					paramsVe.add(map.get("BO_DE_ID"));//组板详细表ID
					List<Map<String, Object>> vehicleList= aDao.getVehicleQueryByBoDeId(paramsVe); //查询配车明细表
					List<Object> paramsOrg =new ArrayList<Object>();
					paramsOrg.add(map.get("BO_DE_ID"));
					paramsOrg.add(Constant.RESOURCE_RESERVE_STATUS_01);
					List<Map<String, Object>> rList= aDao.getOrgQueryByBoDeId(paramsOrg); //查询各省资源数
					String org="ORG_"+map.get("BO_DE_ID");
					boByVeMap.put(map.get("BO_DE_ID"), vehicleList);
					boByVeMap.put(org, rList);
				}
			}
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setOutData("boByVeMap", boByVeMap);
			act.setOutData("list", list);
			act.setForword(allocaAdjustInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"修改组板信息初始化");
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
	 * LastDate    : 2013-4-26
	 */
	public void allocaAdjustMain(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] oldDeIDS=request.getParamValues("OLD_DETAIL_IDS");//旧的配车信息IDS
			String[] oldVeIDS=request.getParamValues("VEHICLE_IDS");//旧的车辆IDS
			String[] boDeIDS=request.getParamValues("BO_DE_IDS");//组板详细IDS
			String[] reDeIDS=request.getParamValues("RE_DEALER_IDS");// 收货方ID
			String[] newVeIDS=request.getParamValues("NEW_VEHICLE_IDS");//新的车辆IDS
			String[] orderTypes=request.getParamValues("ORDER_TYPES");//订单类型
			//根据车辆ID 过滤一次，防止同步操作情况
			List<Map<String, Object>> otherVehicleIdList=aDao.getVehicleIdsByMatId(newVeIDS);
			//记录已占用的数据（同步已保存的数据）
			String infoMsg="";
			boolean isAllNULL=true;//是否有修改
			if(newVeIDS!=null && newVeIDS.length>0){//有修改才进行操作
				for(int i=0;i<newVeIDS.length;i++){
					if(newVeIDS[i]!=null && !newVeIDS[i].equals("")){
						boolean vBool=false;//判断是否已占用
						if(otherVehicleIdList!=null && otherVehicleIdList.size()>0){//防止同步操作同一辆车
							for(int v=0;v<otherVehicleIdList.size();v++){
								Map<String, Object> map=otherVehicleIdList.get(v);
								if(map.get("VEHICLE_ID").toString().equals(newVeIDS[i].toString())){//查出来的车辆ID有，表示已做操作
									infoMsg+="["+map.get("AREA_NAME")+"]区-"+"["+map.get("ROAD_NAME")+"]道-"+"["+map.get("SIT_NAME")+"]位;";
									vBool=true;
									break;
								}
							}
						}
						isAllNULL=false;
						if(vBool!=true){//不等于TRUE 说明未被占用
							//添加新的【配车详细信息
							String detailId=SequenceManager.getSequence("");
							TtSalesAllocaDePO po=new TtSalesAllocaDePO();
							po.setDetailId(Long.parseLong(detailId));//配车明细ID
							po.setBoDeId(Long.parseLong(boDeIDS[i]));//组板明细ID
							po.setVehicleId(Long.parseLong(newVeIDS[i].toString()));//车辆ID
							po.setAllocaPer(logonUser.getUserId());//配车人
							po.setAllocaDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//配车时间
							po.setCreateBy(logonUser.getUserId());//创建人
							po.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
							po.setIsOut(Constant.IF_TYPE_NO);//是否出库（默认为否）
							po.setIsSend(Constant.IF_TYPE_NO);//是否发运（默认为否）
							po.setIsAcc(Constant.IF_TYPE_NO);//是否验收（默认为否）
							aDao.addAllocaDe(po);/**添加新的【配车详细信息*/
							//添加换车历史记录
							String hisId=SequenceManager.getSequence("");//历史记录ID
							TtSalesChaHisPO tschpo=new TtSalesChaHisPO();//换车历史表实体
							tschpo.setHisId(Long.parseLong(hisId));//历史记录ID
							tschpo.setBoDeId(Long.parseLong(boDeIDS[i]));//组板详细ID
							tschpo.setOldVehicleId(Long.parseLong(oldVeIDS[i]));//旧的车辆ID
							tschpo.setNewVehicleId(Long.parseLong(newVeIDS[i]));//新的车辆ID
							tschpo.setChaPer(logonUser.getUserId());//换车人
							tschpo.setChaDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//换车时间
							tschpo.setCreateBy(logonUser.getUserId());
							tschpo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
							TmVehiclePO tvp=new TmVehiclePO();
							tvp.setVehicleId(Long.valueOf(oldVeIDS[i]));
							tvp=(TmVehiclePO)reDao.select(tvp).get(0);
							if(tvp.getLifeCycle().intValue()==Constant.VEHICLE_LIFE_08.intValue()){//换下车是已出库状态
								tschpo.setIsReturn(Constant.IF_TYPE_NO);    //换下车如是已出库，需要再次入库
							}else{
								tschpo.setIsReturn(Constant.IF_TYPE_YES);   //否则不需要再次入库 
							}
							reDao.addCarHis(tschpo);/**添加换车历史*/
							//修改旧的数据
							TmVehiclePO t=new TmVehiclePO();
							t.setOutStatus(Constant.PAYMENT_STATUS_01);//未还款
							t.setLockStatus(Constant.LOCK_STATUS_01);//正常状态
							t.setDealerId(Long.valueOf(Constant.SIT_VELID));//收货经销商-1
							t.setUpdateBy(logonUser.getUserId());
							t.setUpdateDate(new Date());
							TmVehiclePO t1=new TmVehiclePO();
							t1.setVehicleId(Long.valueOf(oldVeIDS[i]));
							reDao.update(t1, t);
							//修改新的车辆信息[是中转库 未付款，其他已付款，是带交车 发运经销商位DEAR_ID]
							List<Object> prarmStatus=new ArrayList<Object>();//修改车辆信息表状态的参数
							prarmStatus.add(Constant.ORDER_TYPE_04);//中转库单
							prarmStatus.add(Constant.PAYMENT_STATUS_01);//付款方式（未付款）
							prarmStatus.add(Constant.PAYMENT_STATUS_03);//付款方式（已付款）
							prarmStatus.add(boDeIDS[i].toString());//组板明细ID
							prarmStatus.add(Constant.LOCK_STATUS_08);//锁定状态
							//prarmStatus.add(Constant.TRANSPORT_TYPE_03);//发运方式   代交车
							prarmStatus.add(boDeIDS[i].toString());//组板明细ID
							prarmStatus.add(logonUser.getUserId());//操作人
							prarmStatus.add(newVeIDS[i]);//车辆ID
							reDao.updateVehicleOutStatus(prarmStatus);
							if(tvp.getLifeCycle().toString().equals(Constant.VEHICLE_LIFE_08.toString())){//是车厂出库
								//判断是否已生成中转订单
								int count=reDao.getOutOrderCount(Long.parseLong(oldVeIDS[i]));//查询数量
								if(count>0){//表示已关联中转库订单
									TmVehiclePO T1=new TmVehiclePO();//旧的解除中转库关联
									T1.setOutDetailId(Long.parseLong(Constant.DEFAULT_VALUE.toString()));//默认值
									T1.setOutStatus(Constant.PAYMENT_STATUS_03);
									TmVehiclePO T2=new TmVehiclePO();//旧的解除中转库关联
									T2.setVehicleId(Long.parseLong(oldVeIDS[i]));
									reDao.update(T2, T1);
									TmVehiclePO T3=new TmVehiclePO();//新的添加中转库关联
									T3.setOutDetailId(tvp.getOutDetailId());//旧的关联outdetailid
									T3.setOutStatus(tvp.getOutStatus());
									TmVehiclePO T4=new TmVehiclePO();//新的添加中转库关联
									T4.setVehicleId(Long.parseLong(newVeIDS[i]));
									reDao.update(T4, T3);
								}
							}
							List<Object> paramsDel=new ArrayList<Object>();//删除配车明细的参数
							paramsDel.add(oldDeIDS[i]);//旧的配车明细ID
							aDao.delAllocaDeById(paramsDel);/**删除旧的车辆信息*/
						}
					}
				}
				TtSalesBoDetailPO detail=new TtSalesBoDetailPO();
				detail.setBoDeId(Long.valueOf(boDeIDS[0]));
				
				long boId=((TtSalesBoDetailPO)aDao.select(detail).get(0)).getBoId();
				StorageUtil.getInstance().updateOutStorgeNum(boId,logonUser.getUserId());
				if(isAllNULL==true){//等于TRUE 无数据修改
					act.setOutData("returnValue", 2);//无操作记录，无需保存
				}else{
					act.setOutData("infoMsg", infoMsg);//占用信息
					act.setOutData("returnValue", 1);//操作成功
				}
			}else{
				act.setOutData("returnValue", 2);//无操作记录，无需保存
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"配车处理逻辑主方法");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:取消配车逻辑方法
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void canelAllocaMain(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String boId=CommonUtils.checkNull(request.getParamValue("boId"));//组板号
		try {
			if(boId!="" && !boId.equals("")){
				//判断是否有车出库，有车出库无法取消该组板
				int allocaCount=reDao.getAllocaCount(Long.parseLong(boId));
				if(allocaCount==0){
					//添加配车删除明细表数据
					reDao.addAllocaDeDel(Long.parseLong(boId),logonUser.getUserId());
					//修改车辆状态(正常状态)
					reDao.updateVehicleStatus(Long.parseLong(boId),logonUser.getUserId());
					//删除配车详细明细表
					reDao.delAllocaDe(Long.parseLong(boId));
					//修改组板表 组板明细表状态
					//stoUtil.updateIsEnable(Long.parseLong(boId),logonUser.getUserId());
					//回写数量
					stoUtil.updateBoardOrAllocaNum(Long.parseLong(boId),logonUser.getUserId());
					act.setOutData("returnValue", 1);
				}else{
					act.setOutData("returnValue", 3);//有车辆出库，无法取消组板！
				}
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e){
			act.setOutData("returnValue", 3);
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
			"取消配车逻辑方法");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:取消组板的逻辑方法
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-28
	 */
	public void canelBoardMain(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String boId=CommonUtils.checkNull(request.getParamValue("boId"));//组板号
		try {
			if(boId!="" && !boId.equals("")){
				//判断是否有车出库，有车出库无法取消该组板
				int allocaCount=reDao.getAllocaCount(Long.parseLong(boId));
				if(allocaCount==0){
					//添加配车删除明细表数据
					reDao.addAllocaDeDel(Long.parseLong(boId),logonUser.getUserId());
					//修改车辆状态(正常状态)
					reDao.updateVehicleStatus(Long.parseLong(boId),logonUser.getUserId());
					//删除配车详细明细表
					reDao.delAllocaDe(Long.parseLong(boId));
					//修改组板表 组板明细表状态
					stoUtil.updateIsEnable(Long.parseLong(boId),logonUser.getUserId());
					//回写数量
					stoUtil.updateBoardOrAllocaNum(Long.parseLong(boId),logonUser.getUserId());
					act.setOutData("returnValue", 1);
				}else{
					act.setOutData("returnValue", 3);//有车辆出库，无法取消组板！
				}
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e){
			act.setOutData("returnValue", 3);
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
			"取消配车逻辑方法");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

}
