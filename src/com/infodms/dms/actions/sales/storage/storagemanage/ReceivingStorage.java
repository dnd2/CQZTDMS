package com.infodms.dms.actions.sales.storage.storagemanage;

import java.math.BigDecimal;
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
//import com.infodms.dms.common.erpinterface.ErpInterfaceCommon;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.storagebase.ReservoirPositionDao;
import com.infodms.dms.dao.sales.storage.storagebase.ReservoirRoadDao;
import com.infodms.dms.dao.sales.storage.storagemanage.ReceivingStorageDao;
import com.infodms.dms.dao.sales.storage.storagemanage.VehicleSiteAdjustDao;
import com.infodms.dms.exception.BizException;
//import com.infodms.dms.po.TiExpBusVehStoreDetPO;
//import com.infodms.dms.po.TiExpBusVehStorePO;
import com.infodms.dms.po.TmPlanDetailPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmpVehicleClPO;
import com.infodms.dms.po.TtSalesAccarPerPO;
import com.infodms.dms.po.TtSalesRoadPO;
import com.infodms.dms.po.TtSalesSitPO;
import com.infodms.dms.po.TtSalesVehicelInPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @ClassName : ReceivingStorage
 * @Description : 接车入库控制类
 * @author : ranjian
 *         CreateDate : 2013-4-11
 */
public class ReceivingStorage {
	public Logger logger = Logger.getLogger(ReceivingStorage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private ReceivingStorageDao reDao = ReceivingStorageDao.getInstance();
	private VehicleSiteAdjustDao vDao = VehicleSiteAdjustDao.getInstance();
	private ReservoirPositionDao reBDao = ReservoirPositionDao.getInstance();
	private ReservoirRoadDao reRDao = ReservoirRoadDao.getInstance();
	
	private final String receivingStorageInitUrl = "/jsp/sales/storage/storagemanage/receivingStorage/receivingStorageList.jsp";
	private final String receivingStorageMainInitUrl = "/jsp/sales/storage/storagemanage/receivingStorage/receivingStorageInit.jsp";
	private final String queryVinDetailUrl = "/jsp/sales/storage/storagemanage/vinShow.jsp";//VIN详细信息页面
	private final String SitCodePrintUtl = "/jsp/sales/storage/storagemanage/receivingStorage/lodop/receivingPrint.jsp";//入库打印
	private final String initMesVehichle = "/jsp/sales/storage/storagemanage/receivingStorage/mesVehicleInfo.jsp";
	
	/**
	 * @Title :
	 * @Description: 接车入库初始化查询条件
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-11
	 */
	public void receivingStorageInit()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String areaIds = MaterialGroupManagerDao
							.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
			List<Map<String, Object>> list_An = reBDao.getReservoirValue(areaIds);//获取库区列表
			act.setOutData("list_An", list_An);//库区信息
			act.setForword(receivingStorageInitUrl);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
							"接车入库查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @Title :
	 * @Description: 接车入库查询信息
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-11
	 */
	public void receivingStorageQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
						.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try
		{
			/****************************** 页面查询字段start **************************/
			String groupCode = request.getParamValue("groupCode"); // 物料组
			String materialCode = request.getParamValue("materialCode"); // 物料
			String areaName = request.getParamValue("AREA_NAME"); // 库区
			String roadName = request.getParamValue("ROAD_NAME"); // 库道
			String sitName = request.getParamValue("SIT_NAME"); // 库位
			String storageDays = request.getParamValue("STORAGE_DAYS"); // 超过天数
			String offlineStartDate = request.getParamValue("OFFLINE_STARTDATE"); // 下线日期开始
			String offlineEndDate = request.getParamValue("OFFLINE_ENDDATE"); // 下线日期结束
			String orgStartDate = request.getParamValue("ORG_STORAGE_STARTDATE"); // 入库日期开始
			String orgEndDate = request.getParamValue("ORG_STORAGE_ENDDATE"); // 入库日期结束
			String vin = request.getParamValue("VIN"); // vin
			/****************************** 页面查询字段end ***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("areaName", areaName);
			map.put("roadName", roadName);
			map.put("sitName", sitName);
			map.put("storageDays", storageDays);
			map.put("offlineStartDate", offlineStartDate);
			map.put("offlineEndDate", offlineEndDate);
			map.put("orgStartDate", orgStartDate);
			map.put("orgEndDate", orgEndDate);
			map.put("vin", vin);
			map.put("areaIds", areaIds);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getReceivingStorageQuery(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * @Title :
	 * @Description: 接车入库初始化
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-11
	 */
	public void receivingStorageMainInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String areaIds =
					Constant.areaIdJZD; 
					// MaterialGroupManagerDao
							//.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
			List<Map<String, Object>> list_An = reBDao.getReservoirValue(areaIds);//获取库区列表
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", Constant.PERSON_STATUS_01);//在职
			map.put("yieldly", areaIds);
			List<Map<String, Object>> list_Pn = reDao.getPerValue(map);//获取接车员
			act.setOutData("list_An", list_An);//库区信息
			act.setOutData("list_Pn", list_Pn);//接车员信息
			act.setOutData("userAreaId", areaIds);//产地
			act.setOutData("userZhen", logonUser.getUserId());//郑经理验证
			act.setForword(receivingStorageMainInitUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title :
	 * @Description: 接车入库业务逻辑主方法
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-7-6
	 */
	public void receivingStorageMain(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds= MaterialGroupManagerDao.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try{
			String TOTAL_ORDER_ID = CommonUtils.checkNull(request.getParamValue("TOTAL_ORDER_ID"));// 汇总订单号
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN
			//String userAreaId = CommonUtils.checkNull(request.getParamValue("userAreaId")); // 用户所属产地
			List<Object> veParams = new ArrayList<Object>();
			veParams.add(vin);//VIN号
			Map<String, Object> vehicleMap = reDao.getVehicleQuery(veParams);
			if (vehicleMap != null && !vehicleMap.isEmpty())
			{//非空表是已有数据
				act.setOutData("returnValue", 14);//该VIN已入库，无需在入库
			}else{
				//根据车辆表的汇总订单号 获取道计划详细表里面的计划数量，已入库数量
				List<Object> paramsC = new ArrayList<Object>();
				paramsC.add(Constant.PRODUCT_PLAN_CHECK_STATUS_02);//汇总订单号
				paramsC.add(TOTAL_ORDER_ID);//汇总订单号
				paramsC.add(areaIds);//产地
				Map<String, Object> planCMap = reDao.getCountByTotalOrderId(paramsC);
				//判断能否找到该汇总订单号
				if (planCMap != null && !planCMap.equals("null")){
					String erp_model = CommonUtils.checkNull(request.getParamValue("ERP_MODEL")); // 获取车型
					String erp_package = CommonUtils.checkNull(request.getParamValue("ERP_PACKAGE")); // 获取内部型号
					String erp_name =CommonUtils.checkNull(request.getParamValue("ERP_NAME")).toUpperCase(); // 获取装备状态
					//集团客户必须选中该单物料（是）//首先根据编码规则查出数据库的物料ID是否是生产计划里面的物料ID，相同才能入库
					//集团客户定做车不需判断新旧编码问题，非集团客户定做车需判断
					if(planCMap.get("IS_FLEET")!=null && planCMap.get("IS_FLEET").toString().equals(Constant.IF_TYPE_YES.toString())){//集团客户
						//CH410.SE.A40 旧的
						//CH410.A20B22.XXX 新的(集团客户单)判断物料编码+生产计划的装备状态
						if(erp_package!=null && erp_package.length()>2 && CommonUtils.isNumber(String.valueOf(erp_package.charAt(1)))){
							String es=erp_package.substring(0,3);
							String color=erp_package.substring(3,6);
							String[] arr={erp_model,es,color};
							//物料编码，(组装物料编码)
							String matCode=CommonUtils.getStrToArr(arr,Constant.REGEX);
							//新的编码根据物料CODE比较
							if(planCMap.get("MATERIAL_CODE").toString().equals(matCode) && planCMap.get("ERP_NAME").toString().equals(erp_name)){
								//不处理
							}else{
								act.setOutData("returnValue",20);//物料不一致，无法入库
								return;
							}
						}else{
							if(planCMap.get("ERP_MODEL").toString().equals(erp_model) && planCMap.get("ERP_PACKAGE").toString().equals(erp_package) && planCMap.get("ERP_NAME").toString().equals(erp_name)){
								//不处理
							}else{
								act.setOutData("returnValue",20);//物料不一致，无法入库
								return;
							}
						}
					}else{
						//CH410.SE.A40 旧的
						//CH410.A20B22.XXX 新的
						if(erp_package!=null && erp_package.length()>2 && CommonUtils.isNumber(String.valueOf(erp_package.charAt(1)))){
							String es=erp_package.substring(0,3);
							String color=erp_package.substring(3,6);
							String[] arr={erp_model,es,color};
							//物料编码，(组装物料编码)
							String matCode=CommonUtils.getStrToArr(arr,Constant.REGEX);
							//新的编码根据物料CODE比较
							//根据物料编码查询物料ID与生产计划的物料ID对比，相同则入库，不相同则不准入库
							TmVhclMaterialPO pp=new TmVhclMaterialPO();
							pp.setStatus(Constant.STATUS_ENABLE);
							pp.setMaterialCode(matCode);
							List<PO> r=reDao.select(pp);
							if(r!=null && r.size()>0){
								TmVhclMaterialPO tmp=(TmVhclMaterialPO)r.get(0);
								if(!planCMap.get("MAI_ID").toString().equals(tmp.getMaterialId().toString())){
									act.setOutData("returnValue",20);//物料不一致，无法入库
									return ;
								}
							}else{
								act.setOutData("returnValue",20);//物料不一致，无法入库
								return ;
							}
						}else{
							//旧的根据车型，内部状态，装配状态 查询的的MAT_ID判断是否属于同物料
							//根据物料编码查询物料ID与生产计划的物料ID对比，相同则入库，不相同则不准入库
							TmVhclMaterialPO pp=new TmVhclMaterialPO();
							pp.setStatus(Constant.STATUS_ENABLE);
							pp.setErpModel(erp_model);
							pp.setErpPackage(erp_package);
							pp.setErpName(erp_name);
							List<PO> r=reDao.select(pp);
							if(r!=null && r.size()>0){
								TmVhclMaterialPO tmp=(TmVhclMaterialPO)r.get(0);
								if(!planCMap.get("MAI_ID").toString().equals(tmp.getMaterialId().toString())){
									act.setOutData("returnValue",20);//物料不一致，无法入库
									return ;
								}
							}else{
								act.setOutData("returnValue",20);//物料不一致，无法入库
								return ;
							}
						}
					}
					//判断数量是否入满
					if(Integer.parseInt(CommonUtils.checkNullNum(planCMap.get("PLAN_NUM").toString()))
										> Integer.parseInt(CommonUtils.checkNullNum(planCMap.get("IN_NUM").toString())) &&
										Integer.parseInt(CommonUtils.checkNullNum(planCMap.get("PLAN_NUM").toString())) != 0){
						//定义一个MAP获取系统自动选位信息（包括区，道，位）能找到未满同物料的库位
						Map<String, Object> resultMap = new HashMap<String, Object>();
						List<Object> oneParams = new ArrayList<Object>();
						oneParams.add(CommonUtils.checkNull(planCMap.get("MATERIAL_CODE")));//物料CODE
						Map<String, Object> salesMap = reDao.getYieldlyAndSeries(oneParams);//获取车系等基础信息
						if (salesMap != null)
						{
							boolean isHave=false;
							boolean isMaxSit=false;
							List<Object> scode = new ArrayList<Object>();
							String SIT_CODE = "";//库位码
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("YIELDLY", salesMap.get("YIELDLY"));//产地
							map.put("SERIES_ID", CommonUtils.checkNull(planCMap.get("SERIES_ID")));//车系ID
							map.put("ORG_ID", CommonUtils.checkNull(planCMap.get("ORG_ID")));//省份ID
							map.put("MAI_ID", CommonUtils.checkNull(planCMap.get("MAI_ID")));//物料ID
							//如果是集团客户定做车需(2013.10.8 ranj添加集团客户定做车)
							if(planCMap.get("IS_FLEET")!=null && planCMap.get("IS_FLEET").toString().equals(Constant.IF_TYPE_YES.toString())){//集团客户定做车
								map.put("IS_FLEET", CommonUtils.checkNull(planCMap.get("IS_FLEET")));//集团客户定做车
								map.put("PLAN_DETAIL_ID", CommonUtils.checkNull(planCMap.get("PLAN_DETAIL_ID")));//生产计划明细ID	
							}
							List<Map<String, Object>> list = reDao.getRoadByhaves(map);
							if(list!=null && list.size()>0){//有数据，表是有同物料，同省份的库道
								//循环查询车位是否可停车（逻辑 按该库道最大库位后加，如前有空位不能入库）
								String aId="",rId="",sId="",aName="",rName="",sName="";
								for(int s=0;s<list.size();s++){
									Map<String, Object> haveMap=list.get(s);//取值
									//MAX_SITNAME 位有车的最大库位+1
									if(Integer.parseInt(haveMap.get("MAX_SITNAME").toString())>Integer.parseInt(haveMap.get("SUM_COUNT").toString())){//判断最大位和库道总位数是否相等，相等直接return
										continue;
									}else{
										//根据库道ID+库位名称获取区，道，位
										List<Object> p2=new ArrayList<Object>();
										p2.add(haveMap.get("ROAD_ID").toString());
										p2.add(haveMap.get("MAX_SITNAME").toString().length()<2?"0"+haveMap.get("MAX_SITNAME"):haveMap.get("MAX_SITNAME"));
										p2.add(Constant.STATUS_ENABLE);
										p2.add(Constant.STATUS_ENABLE);
										p2.add(Constant.STATUS_ENABLE);
										//查询区道位（根据条件）
										List<Map<String, Object>> llist=reDao.getARS(p2);
										if(llist.size()>0){
											if(Integer.parseInt(haveMap.get("MAX_SITNAME").toString())==Integer.parseInt(haveMap.get("SUM_COUNT").toString())){//最后一位判断
												isMaxSit=true;
											}
											isHave=true;
											Map<String, Object> hmap=llist.get(0);
											aId=hmap.get("AREA_ID").toString();
											rId=hmap.get("ROAD_ID").toString();
											sId=hmap.get("SIT_ID").toString();
											aName=hmap.get("AREA_NAME").toString().length()<2?"0"+hmap.get("AREA_NAME").toString():hmap.get("AREA_NAME").toString();
											rName=hmap.get("ROAD_NAME").toString().length()<2?"0"+hmap.get("ROAD_NAME").toString():hmap.get("ROAD_NAME").toString();
											sName=hmap.get("SIT_NAME").toString().length()<2?"0"+hmap.get("SIT_NAME").toString():hmap.get("SIT_NAME").toString();
											scode.add(hmap.get("AREA_NAME"));
											scode.add(hmap.get("ROAD_NAME"));
											scode.add(hmap.get("SIT_NAME"));
										}else{
											continue;
										}
										break;
									}
									
								}
								if(isHave){
								SIT_CODE = CommonUtils.getSitCode(scode);//获取库位码
								resultMap.put("YIELDLY", salesMap.get("YIELDLY"));//产地ID
								resultMap.put("YIELDLY_NAME",
												salesMap.get("YIELDLY_NAME"));//产地Name
								resultMap.put("SERIES_ID",
												salesMap.get("SERIES_ID"));//车系ID
								resultMap.put("MODEL_ID", salesMap.get("MODEL_ID"));//车型ID
								resultMap.put("MODEL_CODE",
												salesMap.get("MODEL_CODE"));//车型CODE
								resultMap.put("MODEL_NAME",
												salesMap.get("MODEL_NAME"));//车型Name
								resultMap.put("PACKAGE_ID",
												salesMap.get("PACKAGE_ID"));//配置ID
								resultMap.put("PACKAGE_CODE",
												salesMap.get("PACKAGE_CODE"));//配置CODE
								resultMap.put("PACKAGE_NAME",
												salesMap.get("PACKAGE_NAME"));//配置Name
								resultMap.put("MATERIAL_ID",
												salesMap.get("MATERIAL_ID"));//物料ID
								resultMap.put("MATERIAL_CODE",
												salesMap.get("MATERIAL_CODE"));//物料CODE
								resultMap.put("MATERIAL_NAME",
												salesMap.get("MATERIAL_NAME"));//物料Name
								resultMap.put("COLOR_NAME",
												salesMap.get("COLOR_NAME"));//颜色
								resultMap.put("MODEL_YEAR",
												salesMap.get("MODEL_YEAR"));//年型
								resultMap.put("AREA_ID", aId);//区
								resultMap.put("ROAD_ID", rId);//道
								resultMap.put("SIT_ID", sId);//位
								resultMap.put("PLAN_DETAIL_ID",
												planCMap.get("PLAN_DETAIL_ID"));//计划详细ID
								/*** 实际不需要这几个字段 **/
								resultMap.put("AREA_NAME", aName);//区
								resultMap.put("ROAD_NAME", rName);//道
								resultMap.put("SIT_NAME",sName);//位
								resultMap.put("SIT_CODE", SIT_CODE);//库位码
								resultMap.put("ORG_ID", CommonUtils.checkNull(planCMap.get("ORG_ID")));
								//根据当前库位ID 判断是否是最后一位
								//boolean bo=reDao.getLastSit(Long.parseLong(rId==null?"0":rId.toString()));
								if (isMaxSit)
								{//最后一位，记录状态，保证车辆入库时候，出库状态设置为解除锁定，入库状态设置为锁定
									resultMap.put("isLastPosition", true);//位
								}else{
									resultMap.put("isLastPosition", false);//位
								}
								}
							}
							if(!isHave){//找新库道
								Map<String, Object> mapNewRoad = reDao.getNullRoad(map);
								if(mapNewRoad!=null && !mapNewRoad.isEmpty()){
									scode.add(mapNewRoad.get("AREA_NAME"));
									scode.add(mapNewRoad.get("ROAD_NAME"));
									scode.add(mapNewRoad.get("SIT_NAME"));
									SIT_CODE = CommonUtils.getSitCode(scode);//获取库位码
									resultMap.put("YIELDLY", salesMap.get("YIELDLY"));//产地ID
									resultMap.put("YIELDLY_NAME",
													salesMap.get("YIELDLY_NAME"));//产地Name
									resultMap.put("SERIES_ID",
													salesMap.get("SERIES_ID"));//车系ID
									resultMap.put("MODEL_ID", salesMap.get("MODEL_ID"));//车型ID
									resultMap.put("MODEL_CODE",
													salesMap.get("MODEL_CODE"));//车型CODE
									resultMap.put("MODEL_NAME",
													salesMap.get("MODEL_NAME"));//车型Name
									resultMap.put("PACKAGE_ID",
													salesMap.get("PACKAGE_ID"));//配置ID
									resultMap.put("PACKAGE_CODE",
													salesMap.get("PACKAGE_CODE"));//配置CODE
									resultMap.put("PACKAGE_NAME",
													salesMap.get("PACKAGE_NAME"));//配置Name
									resultMap.put("MATERIAL_ID",
													salesMap.get("MATERIAL_ID"));//物料ID
									resultMap.put("MATERIAL_CODE",
													salesMap.get("MATERIAL_CODE"));//物料CODE
									resultMap.put("MATERIAL_NAME",
													salesMap.get("MATERIAL_NAME"));//物料Name
									resultMap.put("COLOR_NAME",
													salesMap.get("COLOR_NAME"));//颜色
									resultMap.put("MODEL_YEAR",
													salesMap.get("MODEL_YEAR"));//年型
									resultMap.put("AREA_ID", mapNewRoad.get("AREA_ID"));//区
									resultMap.put("ROAD_ID", mapNewRoad.get("ROAD_ID"));//道
									resultMap.put("SIT_ID", mapNewRoad.get("SIT_ID"));//位
									resultMap.put("PLAN_DETAIL_ID",
													planCMap.get("PLAN_DETAIL_ID"));//计划详细ID
									/*** 实际不需要这几个字段 **/
									resultMap.put("AREA_NAME", mapNewRoad.get("AREA_NAME"));//区
									resultMap.put("ROAD_NAME", mapNewRoad.get("ROAD_NAME"));//道
									resultMap.put("SIT_NAME",mapNewRoad.get("SIT_NAME"));//位
									resultMap.put("SIT_CODE", SIT_CODE);//库位码
									resultMap.put("ORG_ID", CommonUtils.checkNull(planCMap.get("ORG_ID")));
									resultMap.put("isLastPosition", false);//记录是否是最后一位
								}else{
									act.setOutData("returnValue", 5);//无空库道
								}
							}
							act.setOutData("resultMap", resultMap);//存储信息传入前台信息
							act.setOutData("returnValue", 1);//存储信息传入前台信息
						}else{
							act.setOutData("returnValue", 4);//错误信息(无产地)
						}
					}else{
						act.setOutData("returnValue", 3);//处理失败，已超过该订单的最大计划数，无法添加
					}
				}else{
					act.setOutData("returnValue", 2);//处理失败，无法找到该汇总订单号
				}
			}
		}catch (Exception e){//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title :
	 * @Description: 接车入库业务逻辑主方法(合肥)
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-7-6
	 */
	public void receivingStorageMainHF(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds= MaterialGroupManagerDao.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try{
			String TOTAL_ORDER_ID = CommonUtils.checkNull(request.getParamValue("TOTAL_ORDER_ID"));// 汇总订单号
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN
			List<Object> veParams = new ArrayList<Object>();
			veParams.add(vin);//VIN号
			Map<String, Object> vehicleMap = reDao.getVehicleQuery(veParams);
			if (vehicleMap != null && !vehicleMap.isEmpty())
			{//非空表是已有数据
				act.setOutData("returnValue", 14);//该VIN已入库，无需在入库
			}else{
				//根据车辆表的汇总订单号 获取道计划详细表里面的计划数量，已入库数量
				List<Object> paramsC = new ArrayList<Object>();
				paramsC.add(Constant.PRODUCT_PLAN_CHECK_STATUS_02);//汇总订单号
				paramsC.add(TOTAL_ORDER_ID);//汇总订单号
				paramsC.add(areaIds);//产地
				Map<String, Object> planCMap = reDao.getCountByTotalOrderId(paramsC);
				//判断能否找到该汇总订单号
				if (planCMap != null && !planCMap.equals("null")){
					String erp_model = CommonUtils.checkNull(request.getParamValue("ERP_MODEL")); // 获取车型
					String erp_package = CommonUtils.checkNull(request.getParamValue("ERP_PACKAGE")); // 获取内部型号
					String n_erpModel=erp_model+"-"+erp_package;
					String dzCode =CommonUtils.checkNull(request.getParamValue("dzCode")); // 定制码
					String xzCode =CommonUtils.checkNull(request.getParamValue("xzCode")); // 选装码
					String coCode =CommonUtils.checkNull(request.getParamValue("coCode")); // 颜色码
					//根据颜色码获取对应
					Map<String, Object> newCoCode=reDao.getColorName(coCode);//获取ERPNAME
					if(newCoCode==null || newCoCode.equals("null")){
						act.setOutData("returnValue", 95);//无法找到新的颜色编码
						return ;
					}
					Map<String, Object> erpName=reDao.getErpName(dzCode,n_erpModel,xzCode,newCoCode.get("CHOOSE_COLOR").toString());//获取ERPNAME
					if(erpName==null || erpName.equals("null")){
						act.setOutData("returnValue", 94);//无法找到装配状态
						return ;
					}
					String colorName=newCoCode.get("CHOOSE_COLOR").toString();//转换后的颜色编码
					String newErpName=erpName.get("ERP_CODE").toString().toUpperCase();//颜色转化后的erpName
					String oldErpName=erpName.get("OLD_ERP_CODE").toString().toUpperCase();//颜色转化前的erpName(原始的)
					//判断车系(福瑞达：2013073010000511，福瑞达2：2013073010000513)
					if(planCMap.get("SERIES_ID").toString().equals("2013073010000511") || planCMap.get("SERIES_ID").toString().equals("2013073010000513")){
						String oneErpName=newErpName.substring(0, 6);
						String reErpName=newErpName.substring(6, 8);
						String threeErpName=newErpName.substring(8, newErpName.length());
						if(colorName.equals("B34") || colorName.equals("B38") || colorName.equals("B3A")){
							if(reErpName.equals("CC")){
								reErpName="CC";
							}else if(reErpName.equals("CG")){
								reErpName="CM";
							}else if(reErpName.equals("CF")){
								reErpName="CK";
							}
						}
						newErpName=oneErpName+reErpName+threeErpName;
					}
					logger.info("合肥入库提示信息：{车型编码："+erp_model+";配置编码："+erp_package+";原始装配状态码："+oldErpName+";(颜色替换后)装备状态码:"+newErpName+"}");
					//集团客户必须选中该单物料（是）//首先根据编码规则查出数据库的物料ID是否是生产计划里面的物料ID，相同才能入库
					//CH410.SE.A40 旧的
					//CH410.A20B22.XXX 新的
				//	if(planCMap.get("IS_FLEET")!=null && planCMap.get("IS_FLEET").toString().equals(Constant.IF_TYPE_YES.toString())){//集团客户
							TmVhclMaterialPO pp=new TmVhclMaterialPO();
							pp.setStatus(Constant.STATUS_ENABLE);
							pp.setErpModel(erp_model);
							pp.setErpPackage(erp_package);
							pp.setErpName(newErpName);
							List<PO> r=reDao.select(pp);
							if(r!=null && r.size()>0){
								TmVhclMaterialPO tmp=(TmVhclMaterialPO)r.get(0);
								if(!planCMap.get("MAI_ID").toString().equals(tmp.getMaterialId().toString())){
									act.setOutData("returnValue",20);//物料不一致，无法入库
									return ;
								}
							}else{
								act.setOutData("returnValue",20);//物料不一致，无法入库
								return ;
							}
					//}
					//判断数量是否入满
					if(Integer.parseInt(CommonUtils.checkNullNum(planCMap.get("PLAN_NUM").toString()))
										> Integer.parseInt(CommonUtils.checkNullNum(planCMap.get("IN_NUM").toString())) &&
										Integer.parseInt(CommonUtils.checkNullNum(planCMap.get("PLAN_NUM").toString())) != 0){
						//定义一个MAP获取系统自动选位信息（包括区，道，位）能找到未满同物料的库位
						Map<String, Object> resultMap = new HashMap<String, Object>();
						List<Object> oneParams = new ArrayList<Object>();
						oneParams.add(CommonUtils.checkNull(planCMap.get("MATERIAL_CODE")));//物料CODE
						Map<String, Object> salesMap = reDao.getYieldlyAndSeries(oneParams);//获取车系等基础信息
						if (salesMap != null)
						{
							boolean isHave=false;
							boolean isMaxSit=false;//是否是最后一位
							List<Object> scode = new ArrayList<Object>();
							String SIT_CODE = "";//库位码
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("YIELDLY", salesMap.get("YIELDLY"));//产地
							map.put("SERIES_ID", CommonUtils.checkNull(planCMap.get("SERIES_ID")));//车系ID
							map.put("ORG_ID", CommonUtils.checkNull(planCMap.get("ORG_ID")));//省份ID
							map.put("MAI_ID", CommonUtils.checkNull(planCMap.get("MAI_ID")));//物料ID
							//如果是集团客户定做车需(2013.10.8 ranj添加集团客户定做车)
							if(planCMap.get("IS_FLEET")!=null && planCMap.get("IS_FLEET").toString().equals(Constant.IF_TYPE_YES.toString())){//集团客户定做车
								map.put("IS_FLEET", CommonUtils.checkNull(planCMap.get("IS_FLEET")));//集团客户定做车
								map.put("PLAN_DETAIL_ID", CommonUtils.checkNull(planCMap.get("PLAN_DETAIL_ID")));//生产计划明细ID	
							}
							List<Map<String, Object>> list = reDao.getRoadByhaves(map);
							if(list!=null && list.size()>0){//有数据，表是有同物料，同省份的库道
								//循环查询车位是否可停车（逻辑 按该库道最大库位后加，如前有空位不能入库）
								String aId="",rId="",sId="",aName="",rName="",sName="";
								for(int s=0;s<list.size();s++){
									Map<String, Object> haveMap=list.get(s);//取值
									//MAX_SITNAME 位有车的最大库位+1
									if(Integer.parseInt(haveMap.get("MAX_SITNAME").toString())>Integer.parseInt(haveMap.get("SUM_COUNT").toString())){//判断最大位和库道总位数是否相等，相等直接return
										continue;
									}else{
										//根据库道ID+库位名称获取区，道，位
										List<Object> p2=new ArrayList<Object>();
										p2.add(haveMap.get("ROAD_ID").toString());
										p2.add(haveMap.get("MAX_SITNAME").toString().length()<2?"0"+haveMap.get("MAX_SITNAME"):haveMap.get("MAX_SITNAME"));
										p2.add(Constant.STATUS_ENABLE);
										p2.add(Constant.STATUS_ENABLE);
										p2.add(Constant.STATUS_ENABLE);
										//查询区道位（根据条件）
										List<Map<String, Object>> llist=reDao.getARS(p2);
										if(llist.size()>0){
											if(Integer.parseInt(haveMap.get("MAX_SITNAME").toString())==Integer.parseInt(haveMap.get("SUM_COUNT").toString())){//最后一位判断
												isMaxSit=true;
											}
											isHave=true;
											Map<String, Object> hmap=llist.get(0);
											aId=hmap.get("AREA_ID").toString();
											rId=hmap.get("ROAD_ID").toString();
											sId=hmap.get("SIT_ID").toString();
											aName=hmap.get("AREA_NAME").toString().length()<2?"0"+hmap.get("AREA_NAME").toString():hmap.get("AREA_NAME").toString();
											rName=hmap.get("ROAD_NAME").toString().length()<2?"0"+hmap.get("ROAD_NAME").toString():hmap.get("ROAD_NAME").toString();
											sName=hmap.get("SIT_NAME").toString().length()<2?"0"+hmap.get("SIT_NAME").toString():hmap.get("SIT_NAME").toString();
											scode.add(hmap.get("AREA_NAME"));
											scode.add(hmap.get("ROAD_NAME"));
											scode.add(hmap.get("SIT_NAME"));
										}else{
											continue;
										}
										break;
									}
									
								}
								if(isHave){
								SIT_CODE = CommonUtils.getSitCode(scode);//获取库位码
								resultMap.put("YIELDLY", salesMap.get("YIELDLY"));//产地ID
								resultMap.put("YIELDLY_NAME",
												salesMap.get("YIELDLY_NAME"));//产地Name
								resultMap.put("SERIES_ID",
												salesMap.get("SERIES_ID"));//车系ID
								resultMap.put("MODEL_ID", salesMap.get("MODEL_ID"));//车型ID
								resultMap.put("MODEL_CODE",
												salesMap.get("MODEL_CODE"));//车型CODE
								resultMap.put("MODEL_NAME",
												salesMap.get("MODEL_NAME"));//车型Name
								resultMap.put("PACKAGE_ID",
												salesMap.get("PACKAGE_ID"));//配置ID
								resultMap.put("PACKAGE_CODE",
												salesMap.get("PACKAGE_CODE"));//配置CODE
								resultMap.put("PACKAGE_NAME",
												salesMap.get("PACKAGE_NAME"));//配置Name
								resultMap.put("MATERIAL_ID",
												salesMap.get("MATERIAL_ID"));//物料ID
								resultMap.put("MATERIAL_CODE",
												salesMap.get("MATERIAL_CODE"));//物料CODE
								resultMap.put("MATERIAL_NAME",
												salesMap.get("MATERIAL_NAME"));//物料Name
								resultMap.put("COLOR_NAME",
												salesMap.get("COLOR_NAME"));//颜色
								resultMap.put("MODEL_YEAR",
												salesMap.get("MODEL_YEAR"));//年型
								resultMap.put("AREA_ID", aId);//区
								resultMap.put("ROAD_ID", rId);//道
								resultMap.put("SIT_ID", sId);//位
								resultMap.put("PLAN_DETAIL_ID",
												planCMap.get("PLAN_DETAIL_ID"));//计划详细ID
								/*** 实际不需要这几个字段 **/
								resultMap.put("AREA_NAME", aName);//区
								resultMap.put("ROAD_NAME", rName);//道
								resultMap.put("SIT_NAME",sName);//位
								resultMap.put("SIT_CODE", SIT_CODE);//库位码
								resultMap.put("ORG_ID", CommonUtils.checkNull(planCMap.get("ORG_ID")));
								//根据当前库位ID 判断是否是最后一位
								//boolean bo=reDao.getLastSit(Long.parseLong(rId==null?"0":rId.toString()));
								if (isMaxSit)
								{//最后一位，记录状态，保证车辆入库时候，出库状态设置为解除锁定，入库状态设置为锁定
									resultMap.put("isLastPosition", true);//位
								}else{
									resultMap.put("isLastPosition", false);//位
								}
								}
							}
							if(!isHave){//找新库道
								Map<String, Object> mapNewRoad = reDao.getNullRoad(map);
								if(mapNewRoad!=null && !mapNewRoad.isEmpty()){
									scode.add(mapNewRoad.get("AREA_NAME"));
									scode.add(mapNewRoad.get("ROAD_NAME"));
									scode.add(mapNewRoad.get("SIT_NAME"));
									SIT_CODE = CommonUtils.getSitCode(scode);//获取库位码
									resultMap.put("YIELDLY", salesMap.get("YIELDLY"));//产地ID
									resultMap.put("YIELDLY_NAME",
													salesMap.get("YIELDLY_NAME"));//产地Name
									resultMap.put("SERIES_ID",
													salesMap.get("SERIES_ID"));//车系ID
									resultMap.put("MODEL_ID", salesMap.get("MODEL_ID"));//车型ID
									resultMap.put("MODEL_CODE",
													salesMap.get("MODEL_CODE"));//车型CODE
									resultMap.put("MODEL_NAME",
													salesMap.get("MODEL_NAME"));//车型Name
									resultMap.put("PACKAGE_ID",
													salesMap.get("PACKAGE_ID"));//配置ID
									resultMap.put("PACKAGE_CODE",
													salesMap.get("PACKAGE_CODE"));//配置CODE
									resultMap.put("PACKAGE_NAME",
													salesMap.get("PACKAGE_NAME"));//配置Name
									resultMap.put("MATERIAL_ID",
													salesMap.get("MATERIAL_ID"));//物料ID
									resultMap.put("MATERIAL_CODE",
													salesMap.get("MATERIAL_CODE"));//物料CODE
									resultMap.put("MATERIAL_NAME",
													salesMap.get("MATERIAL_NAME"));//物料Name
									resultMap.put("COLOR_NAME",
													salesMap.get("COLOR_NAME"));//颜色
									resultMap.put("MODEL_YEAR",
													salesMap.get("MODEL_YEAR"));//年型
									resultMap.put("AREA_ID", mapNewRoad.get("AREA_ID"));//区
									resultMap.put("ROAD_ID", mapNewRoad.get("ROAD_ID"));//道
									resultMap.put("SIT_ID", mapNewRoad.get("SIT_ID"));//位
									resultMap.put("PLAN_DETAIL_ID",
													planCMap.get("PLAN_DETAIL_ID"));//计划详细ID
									/*** 实际不需要这几个字段 **/
									resultMap.put("AREA_NAME", mapNewRoad.get("AREA_NAME"));//区
									resultMap.put("ROAD_NAME", mapNewRoad.get("ROAD_NAME"));//道
									resultMap.put("SIT_NAME",mapNewRoad.get("SIT_NAME"));//位
									resultMap.put("SIT_CODE", SIT_CODE);//库位码
									resultMap.put("ORG_ID", CommonUtils.checkNull(planCMap.get("ORG_ID")));
									resultMap.put("isLastPosition", false);//记录是否是最后一位
								}else{
									act.setOutData("returnValue", 5);//无空库道
								}
							}
							act.setOutData("resultMap", resultMap);//存储信息传入前台信息
							act.setOutData("returnValue", 1);//存储信息传入前台信息
						}else{
							act.setOutData("returnValue", 4);//错误信息(无产地)
						}
					}else{
						act.setOutData("returnValue", 3);//处理失败，已超过该订单的最大计划数，无法添加
					}
				}else{
					act.setOutData("returnValue", 2);//处理失败，无法找到该汇总订单号
				}
			}
		}catch (Exception e){//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title :
	 * @Description: 接车入库业务逻辑主方法(九江)
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-8-27
	 */
	public void receivingStorageMainJJ(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds= MaterialGroupManagerDao.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try{
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN
			List<Object> veParams = new ArrayList<Object>();
			veParams.add(vin);//VIN号
			Map<String, Object> vehicleMap = reDao.getVehicleQuery(veParams);
			if (vehicleMap != null && !vehicleMap.isEmpty())
			{//非空表是已有数据
				act.setOutData("returnValue", 14);//该VIN已入库，无需在入库
			}else{
				//首先根据车型，内部型号，装备状态查询物料ID
				String erp_model = CommonUtils.checkNull(request.getParamValue("ERP_MODEL")); // 获取车型
				String erp_package = CommonUtils.checkNull(request.getParamValue("ERP_PACKAGE")); // 获取内部型号
				String erp_name =CommonUtils.checkNull(request.getParamValue("ERP_NAME")).toUpperCase(); // 获取装备状态
				String orgId =CommonUtils.checkNull(request.getParamValue("orgId")); // 站点
				String planId=SequenceManager.getSequence(null);
				TmVhclMaterialPO tp=new TmVhclMaterialPO();
				tp.setErpModel(erp_model);
				tp.setErpPackage(erp_package);
				tp.setErpName(erp_name);
				List<PO> p=reDao.select(tp);
				if(p!=null && p.size()>0){
					TmVhclMaterialPO x=(TmVhclMaterialPO)p.get(0);
					Map<String, Object> m=reDao.getOrgId(orgId);
					String newOrgId="";
					if(m!=null && !m.equals("null")){
						newOrgId=m.get("NEW_ORG_ID").toString();
					}else{
						act.setOutData("returnValue", 90);//站点错误
						return ;
					}
					Map<String,Object> bo1=reDao.getCountByTotalOrderIdJJ(x.getMaterialId(),newOrgId);
					
					TmPlanDetailPO pd=new TmPlanDetailPO();
					pd.setPlanDetailId(Long.parseLong(planId));
					pd.setPlanId(Long.parseLong("2013082779748460"));
					pd.setTotalOrderNo(CommonUtils.getBusNo(Constant.NOCRT_TOTAL_PRO_NO,Long.valueOf(areaIds)));
					pd.setPlanNum(Long.parseLong("99999999"));
					pd.setInNum(Long.parseLong("0"));
					pd.setMaiId(x.getMaterialId());
					pd.setOrgId(Long.parseLong(newOrgId));
					pd.setCreateBy(logonUser.getUserId());
					pd.setIsFleet(Constant.IF_TYPE_NO.longValue());
					pd.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
					pd.setCheckStatus(Constant.PRODUCT_PLAN_CHECK_STATUS_02.longValue());
					if(bo1==null || bo1.equals("null")){//无添加
						reDao.insert(pd);
					}
					//查询生产计划信息
					List<Object> paramsC = new ArrayList<Object>();
					paramsC.add(Constant.PRODUCT_PLAN_CHECK_STATUS_02);//汇总订单号
					paramsC.add(bo1.get("TOTAL_ORDER_NO"));//汇总订单号
					paramsC.add(areaIds);//产地
					Map<String, Object> planCMap = reDao.getCountByTotalOrderId(paramsC);
					
					//判断能否找到该汇总订单号
					if (planCMap != null && !planCMap.equals("null")){
						//判断数量是否入满
						if(Integer.parseInt(CommonUtils.checkNullNum(planCMap.get("PLAN_NUM").toString()))
											> Integer.parseInt(CommonUtils.checkNullNum(planCMap.get("IN_NUM").toString())) &&
											Integer.parseInt(CommonUtils.checkNullNum(planCMap.get("PLAN_NUM").toString())) != 0){
							//定义一个MAP获取系统自动选位信息（包括区，道，位）能找到未满同物料的库位
							Map<String, Object> resultMap = new HashMap<String, Object>();
							List<Object> oneParams = new ArrayList<Object>();
							oneParams.add(CommonUtils.checkNull(planCMap.get("MATERIAL_CODE")));//物料CODE
							Map<String, Object> salesMap = reDao.getYieldlyAndSeries(oneParams);//获取车系等基础信息
							if (salesMap != null)
							{
								boolean isHave=false;
								boolean isMaxSit=false;
								List<Object> scode = new ArrayList<Object>();
								String SIT_CODE = "";//库位码
								Map<String, Object> map=new HashMap<String, Object>();
								map.put("YIELDLY", salesMap.get("YIELDLY"));//产地
								map.put("SERIES_ID", CommonUtils.checkNull(planCMap.get("SERIES_ID")));//车系ID
								map.put("ORG_ID", CommonUtils.checkNull(planCMap.get("ORG_ID")));//省份ID
								map.put("MAI_ID", CommonUtils.checkNull(planCMap.get("MAI_ID")));//物料ID
								List<Map<String, Object>> list = reDao.getRoadByhaves(map);
								if(list!=null && list.size()>0){//有数据，表是有同物料，同省份的库道
									//循环查询车位是否可停车（逻辑 按该库道最大库位后加，如前有空位不能入库）
									String aId="",rId="",sId="",aName="",rName="",sName="";
									for(int s=0;s<list.size();s++){
										Map<String, Object> haveMap=list.get(s);//取值
										//MAX_SITNAME 位有车的最大库位+1
										if(Integer.parseInt(haveMap.get("MAX_SITNAME").toString())>Integer.parseInt(haveMap.get("SUM_COUNT").toString())){//判断最大位和库道总位数是否相等，相等直接return
											continue;
										}else{
											//根据库道ID+库位名称获取区，道，位
											List<Object> p2=new ArrayList<Object>();
											p2.add(haveMap.get("ROAD_ID").toString());
											p2.add(haveMap.get("MAX_SITNAME").toString().length()<2?"0"+haveMap.get("MAX_SITNAME"):haveMap.get("MAX_SITNAME"));
											p2.add(Constant.STATUS_ENABLE);
											p2.add(Constant.STATUS_ENABLE);
											p2.add(Constant.STATUS_ENABLE);
											//查询区道位（根据条件）
											List<Map<String, Object>> llist=reDao.getARS(p2);
											if(llist.size()>0){
												isHave=true;
												if(Integer.parseInt(haveMap.get("MAX_SITNAME").toString())==Integer.parseInt(haveMap.get("SUM_COUNT").toString())){//最后一位判断
													isMaxSit=true;
												}
												Map<String, Object> hmap=llist.get(0);
												aId=hmap.get("AREA_ID").toString();
												rId=hmap.get("ROAD_ID").toString();
												sId=hmap.get("SIT_ID").toString();
												aName=hmap.get("AREA_NAME").toString().length()<2?"0"+hmap.get("AREA_NAME").toString():hmap.get("AREA_NAME").toString();
												rName=hmap.get("ROAD_NAME").toString().length()<2?"0"+hmap.get("ROAD_NAME").toString():hmap.get("ROAD_NAME").toString();
												sName=hmap.get("SIT_NAME").toString().length()<2?"0"+hmap.get("SIT_NAME").toString():hmap.get("SIT_NAME").toString();
												scode.add(hmap.get("AREA_NAME"));
												scode.add(hmap.get("ROAD_NAME"));
												scode.add(hmap.get("SIT_NAME"));
											}else{
												continue;
											}
											break;
										}
										
									}
									if(isHave){
									SIT_CODE = CommonUtils.getSitCode(scode);//获取库位码
									resultMap.put("YIELDLY", salesMap.get("YIELDLY"));//产地ID
									resultMap.put("YIELDLY_NAME",
													salesMap.get("YIELDLY_NAME"));//产地Name
									resultMap.put("SERIES_ID",
													salesMap.get("SERIES_ID"));//车系ID
									resultMap.put("MODEL_ID", salesMap.get("MODEL_ID"));//车型ID
									resultMap.put("MODEL_CODE",
													salesMap.get("MODEL_CODE"));//车型CODE
									resultMap.put("MODEL_NAME",
													salesMap.get("MODEL_NAME"));//车型Name
									resultMap.put("PACKAGE_ID",
													salesMap.get("PACKAGE_ID"));//配置ID
									resultMap.put("PACKAGE_CODE",
													salesMap.get("PACKAGE_CODE"));//配置CODE
									resultMap.put("PACKAGE_NAME",
													salesMap.get("PACKAGE_NAME"));//配置Name
									resultMap.put("MATERIAL_ID",
													salesMap.get("MATERIAL_ID"));//物料ID
									resultMap.put("MATERIAL_CODE",
													salesMap.get("MATERIAL_CODE"));//物料CODE
									resultMap.put("MATERIAL_NAME",
													salesMap.get("MATERIAL_NAME"));//物料Name
									resultMap.put("COLOR_NAME",
													salesMap.get("COLOR_NAME"));//颜色
									resultMap.put("MODEL_YEAR",
													salesMap.get("MODEL_YEAR"));//年型
									resultMap.put("AREA_ID", aId);//区
									resultMap.put("ROAD_ID", rId);//道
									resultMap.put("SIT_ID", sId);//位
									resultMap.put("PLAN_DETAIL_ID",
													planCMap.get("PLAN_DETAIL_ID"));//计划详细ID
									/*** 实际不需要这几个字段 **/
									resultMap.put("AREA_NAME", aName);//区
									resultMap.put("ROAD_NAME", rName);//道
									resultMap.put("SIT_NAME",sName);//位
									resultMap.put("SIT_CODE", SIT_CODE);//库位码
									//根据当前库位ID 判断是否是最后一位
									//boolean bo=reDao.getLastSit(Long.parseLong(rId==null?"0":rId.toString()));
									if (isMaxSit)
									{//最后一位，记录状态，保证车辆入库时候，出库状态设置为解除锁定，入库状态设置为锁定
										resultMap.put("isLastPosition", true);//位
									}else{
										resultMap.put("isLastPosition", false);//位
									}
									}
								}
								if(!isHave){//找新库道
									Map<String, Object> mapNewRoad = reDao.getNullRoad(map);
									if(mapNewRoad!=null && !mapNewRoad.isEmpty()){
										scode.add(mapNewRoad.get("AREA_NAME"));
										scode.add(mapNewRoad.get("ROAD_NAME"));
										scode.add(mapNewRoad.get("SIT_NAME"));
										SIT_CODE = CommonUtils.getSitCode(scode);//获取库位码
										resultMap.put("YIELDLY", salesMap.get("YIELDLY"));//产地ID
										resultMap.put("YIELDLY_NAME",
														salesMap.get("YIELDLY_NAME"));//产地Name
										resultMap.put("SERIES_ID",
														salesMap.get("SERIES_ID"));//车系ID
										resultMap.put("MODEL_ID", salesMap.get("MODEL_ID"));//车型ID
										resultMap.put("MODEL_CODE",
														salesMap.get("MODEL_CODE"));//车型CODE
										resultMap.put("MODEL_NAME",
														salesMap.get("MODEL_NAME"));//车型Name
										resultMap.put("PACKAGE_ID",
														salesMap.get("PACKAGE_ID"));//配置ID
										resultMap.put("PACKAGE_CODE",
														salesMap.get("PACKAGE_CODE"));//配置CODE
										resultMap.put("PACKAGE_NAME",
														salesMap.get("PACKAGE_NAME"));//配置Name
										resultMap.put("MATERIAL_ID",
														salesMap.get("MATERIAL_ID"));//物料ID
										resultMap.put("MATERIAL_CODE",
														salesMap.get("MATERIAL_CODE"));//物料CODE
										resultMap.put("MATERIAL_NAME",
														salesMap.get("MATERIAL_NAME"));//物料Name
										resultMap.put("COLOR_NAME",
														salesMap.get("COLOR_NAME"));//颜色
										resultMap.put("MODEL_YEAR",
														salesMap.get("MODEL_YEAR"));//年型
										resultMap.put("AREA_ID", mapNewRoad.get("AREA_ID"));//区
										resultMap.put("ROAD_ID", mapNewRoad.get("ROAD_ID"));//道
										resultMap.put("SIT_ID", mapNewRoad.get("SIT_ID"));//位
										resultMap.put("PLAN_DETAIL_ID",
														planCMap.get("PLAN_DETAIL_ID"));//计划详细ID
										/*** 实际不需要这几个字段 **/
										resultMap.put("AREA_NAME", mapNewRoad.get("AREA_NAME"));//区
										resultMap.put("ROAD_NAME", mapNewRoad.get("ROAD_NAME"));//道
										resultMap.put("SIT_NAME",mapNewRoad.get("SIT_NAME"));//位
										resultMap.put("SIT_CODE", SIT_CODE);//库位码
										resultMap.put("isLastPosition", false);//记录是否是最后一位
									}else{
										act.setOutData("returnValue", 5);//无空库道
									}
								}
								act.setOutData("resultMap", resultMap);//存储信息传入前台信息
								act.setOutData("returnValue", 1);//存储信息传入前台信息
							}else{
								act.setOutData("returnValue", 4);//错误信息(无产地)
							}
						}else{
							act.setOutData("returnValue", 3);//处理失败，已超过该订单的最大计划数，无法添加
						}
					}else{
						act.setOutData("returnValue", 29);//处理失败
					}
					
				}else{
					act.setOutData("returnValue", 93);//无法找到该物料
				}
			}
		}catch (Exception e){//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title :
	 * @Description: 保存车辆信息
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-12
	 */
	public void saveReceivingStorage()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//获取用户所在的职位
		try
		{
			String vehicleId = SequenceManager.getSequence(null);//车辆ID
			String materialId = CommonUtils.checkNull(request.getParamValue("MATERIAL_ID")); // 物料ID
			String vin = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN
			String engineNo = CommonUtils.checkNull(request.getParamValue("ENGINE_NO")); // 发动机号
			String seriesId = CommonUtils.checkNull(request.getParamValue("SERIES_ID")); // 车系ID
			String modelId = CommonUtils.checkNull(request.getParamValue("MODEL_ID")); // 车型ID
			String packageId = CommonUtils.checkNull(request.getParamValue("PACKAGE_ID")); // 配置ID
			String productDate = CommonUtils.checkNull(request.getParamValue("PRODUCT_DATE")); // 生产日期
			//String factoryDate = CommonUtils.checkNull(request.getParamValue("FACTORY_DATE")); // 出厂日期
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String sitId = CommonUtils.checkNull(request.getParamValue("SIT_NAME")); // 库位号
			String perId = CommonUtils.checkNull(request.getParamValue("PER_ID")); //接车人
			String sitCode = CommonUtils.checkNull(request.getParamValue("SIT_CODE")); // 库位码
			String gearboxNo = CommonUtils.checkNull(request.getParamValue("GEARBOX_NO")); // 变速箱号
			String rearaxleNo = CommonUtils.checkNull(request.getParamValue("REARAXLE_NO")); //后桥号
			String transferNo = CommonUtils.checkNull(request.getParamValue("TRANSFER_NO")); // 分动器号
			String isLastPosition = CommonUtils.checkNull(request.getParamValue("isLastPosition")); //是否最后一库位			
			String colorName = CommonUtils.checkNull(request.getParamValue("COLOR_NAME")); //颜色
			String modelYear = CommonUtils.checkNull(request.getParamValue("MODEL_YEAR")); //年型			
			String offlineDate = CommonUtils.checkNull(request.getParamValue("OFFLINE_DATE")); // 下线日期
			String planDetailId = CommonUtils.checkNull(request.getParamValue("PLAN_DETAIL_ID")); // 计划详细ID
			String orgStorageDate = CommonUtils.checkNull(request.getParamValue("ORG_STORAGE_DATE")); // 入库日期
			String sdNumber = CommonUtils.checkNull(request.getParamValue("SD_NUMBER")); // 流水号
			String hegezhengCode = CommonUtils.checkNull(request.getParamValue("HEGEZHENG_CODE")); // 合格证号
			String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注
			String processType = CommonUtils.checkNull(request.getParamValue("PROCESS_TYPE")); // 入库方式
			String org_id = CommonUtils.checkNull(request.getParamValue("ORG_ID")); // 站点
			String hgzNo= CommonUtils.checkNull(request.getParamValue("hgzNo"));//合格证号
			String vn = "";//vin号后8位
			if (vin != null && vin.length() > 8)
			{
				vn = vin.substring(vin.length() - 8, vin.length());
			}
			TmVehiclePO tvpo = new TmVehiclePO();
			tvpo.setVehicleId(Long.parseLong(vehicleId));//车辆ID
			tvpo.setMaterialId(Long.parseLong(materialId));// 物料ID
			tvpo.setVn(vn);//VN号
			tvpo.setVin(vin);//vin号
			tvpo.setLifeCycle(Constant.VEHICLE_LIFE_02);//生命周期(选的车厂库存)
			tvpo.setEngineNo(engineNo);//发动机号
			tvpo.setGearboxNo(gearboxNo);//变速箱号(有问题字段)
			tvpo.setRearaxleNo(rearaxleNo);//后桥号(有问题字段)
			tvpo.setTransferNo(transferNo);//分动器号(有问题字段)
			tvpo.setSeriesId(Long.parseLong(seriesId));//车系ID
			tvpo.setModelId(Long.parseLong(modelId));//车型ID
			tvpo.setPackageId(Long.parseLong(packageId));//配置ID
			tvpo.setModelYear(modelYear);//年型
			tvpo.setColor(colorName);//颜色
			tvpo.setProductDate(CommonUtils.parseDate(productDate));//生产日期
			//tvpo.setFactoryDate(CommonUtils.parseDate(factoryDate));//出厂时间
			tvpo.setLicenseDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//扫码日期(有问题字段)
			tvpo.setYieldly(Long.parseLong(yieldly));//产地
			tvpo.setSitId(Long.parseLong(sitId));//库位ID
			tvpo.setRemark(remark);//备注（条码里面读取）
			tvpo.setProvId(Long.parseLong(org_id));
			if (perId.equals(""))
			{
				throw new RuntimeException("当前入库没有接车人员,请添加接车员信息");
			}
			
			tvpo.setPerId(Long.parseLong(perId));//接车人
			tvpo.setSitCode(sitCode);//库位码
			tvpo.setOfflineDate(CommonUtils.parseDate(offlineDate));//下线日期
			tvpo.setCreateBy(logonUser.getUserId());//创建人
			tvpo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
			tvpo.setOrgStorageDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//入库时间
			tvpo.setPlanDetailId(Long.parseLong(planDetailId));//汇总订单号
			tvpo.setOutDetailId(Long.valueOf(Constant.DEFAULT_VALUE));//出库单明细ID（入库时候默认为-1）
			tvpo.setDealerId(Long.valueOf(Constant.DEFAULT_VALUE));//经销商ID（入库时候默认为-1）
			tvpo.setLockStatus(Constant.LOCK_STATUS_01);//锁定状态：正常状态
			//tvpo.setLockStatus(Constant.LOCK_STATUS_01);//锁定状态：正常状态
			tvpo.setSdNumber(Long.parseLong(sdNumber==""?"-1":sdNumber));//流水号
			tvpo.setPin(Constant.DEFAULT_VALUE.toString());//ping码
			tvpo.setHegezhengCode(hegezhengCode);//合格证号
			tvpo.setStorageDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//跟入库时间一样
			tvpo.setProcessType(processType);
			if (hgzNo.equals(""))
			{
				throw new RuntimeException("合格证号无值 ！无法入库！");
			}
			tvpo.setHgzNo(hgzNo);//合格证号
			//首先查询该库位是否放有车辆
			List<Object> oneParams = new ArrayList<Object>();
			oneParams.add(Constant.STATUS_ENABLE);//有效的
			oneParams.add(sitId);//库位ID
			Map<String, Object> sitMap = reBDao.getSitById(oneParams);//获取库位信息
			if (sitMap != null)
			{
				if (Integer.parseInt(sitMap.get("VEHICLE_ID").toString()) == Constant.DEFAULT_VALUE)
				{//车辆ID
					//1.根据VIN号查询是否有这辆车 ，有就更新 无就添加
					List<Object> veParams = new ArrayList<Object>();
					veParams.add(vin);//VIN号
					Map<String, Object> vehicleMap = reDao.getVehicleQuery(veParams);
					if (vehicleMap != null && !vehicleMap.isEmpty())
					{//非空表是已有数据
						act.setOutData("returnValue", 3);//错误提示
					}
					else
					{
						//添加车辆信息
						reDao.vehicleAdd(tvpo);//添加车辆信息
						//修改计划明细表已入库数量 +1
						List<Object> inNumParams = new ArrayList<Object>();
						inNumParams.add(planDetailId);//计划详细ID
						reDao.inNumUpdate(inNumParams);//修改计划明细表已入库字段
						
						//2.更新库位信息（库位表更新车辆ID字段）
						TtSalesSitPO tspo = new TtSalesSitPO();
						tspo.setVehicleId(Long.parseLong(vehicleId));//更新库位表的车辆ID
						TtSalesSitPO seachPO = new TtSalesSitPO();
						seachPO.setSitId(Long.parseLong(sitId));
						reBDao.sitUpdate(seachPO, tspo);
						//4.更新库道表状态
						TtSalesRoadPO tsrpo = new TtSalesRoadPO();//修改实体
						TtSalesRoadPO seachwhere = new TtSalesRoadPO();//条件实体
						if (isLastPosition.equals("true"))
						{//最后一库位 入库锁定，出库解锁
							/******************************** 修改字段 *********************************/
							tsrpo.setOutStatus(Constant.AUTO_OUT_STATUS_01.longValue());//出库状态为解锁
							/******************************** 查询条件 *********************************/
							seachwhere.setRoadId(Long.parseLong(sitMap.get("ROAD_ID").toString()));
							reRDao.reservoirSalesRoadAdd(seachwhere, tsrpo);
						}
						else
						{//不是最后一库位 入库正常，出库锁定（为了防止一些库道没出库锁定而写）
							/******************************** 修改字段 *********************************/
							tsrpo.setOutStatus(Constant.AUTO_OUT_STATUS_02.longValue());//出库状态为锁定
							/******************************** 查询条件 *********************************/
							seachwhere.setRoadId(Long.parseLong(sitMap.get("ROAD_ID").toString()));
							reRDao.reservoirSalesRoadAdd(seachwhere, tsrpo);
						}
						TtSalesAccarPerPO pp=new TtSalesAccarPerPO();
						pp.setPerId(Long.parseLong(perId));
						List<PO> PL=reDao.select(pp);
						String perCode="";
						if(PL!=null && PL.size()>0){
							TtSalesAccarPerPO P=(TtSalesAccarPerPO)PL.get(0);
							perCode=P.getPerCode();
						}
						// 判断是否是昌铃品牌的车或者是否是九江工厂生产的车
						List<Object> params=new ArrayList<Object>();
						params.add(materialId);
						List<Map<String, Object>> clist=reDao.getCLQuery(params);
						Map<String, Object> vmtMap = clist.get(0);
						
						if(CommonUtils.checkNull(vmtMap.get("EXPORT_SALES_FLAG")).equals(Constant.IF_TYPE_NO.toString())) {
							// 非出口车的昌铃和九江可以用于开票
							if(CommonUtils.checkNull(vmtMap.get("BRAND_ID")).equals("2013073019867557") || yieldly.equals(Constant.areaIdJJ)) {
								TmpVehicleClPO tvc=new TmpVehicleClPO();
								tvc.setVehicleId(Long.parseLong(vehicleId));
								reDao.insert(tvc);
							}
						} 
						
						act.setOutData("perCode",perCode);//接车员CODE
						act.setOutData("returnValue", 1);//成功
					}
				}
				else
				{//=不为-1代表该库位已有车辆停放
					act.setOutData("returnValue", 2);//错误提示
				}
			}
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/* VIN详细信息 */
	public void queryVinDetail()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String VIN = CommonUtils.checkNull(request.getParamValue("vin")); //VIN ID
			List<Object> params = new ArrayList<Object>();
			params.add(VIN);//VIN号
			Map<String, Object> vinInfo = reDao.getVinDetail(params);//Vin详细
			act.setOutData("vinInfo", vinInfo);
			act.setForword(queryVinDetailUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "VIN基本信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 打印车辆信息预览
	 */
	public void printVehicleMsg()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String vehicleId = request.getParamValue("VEHICLE_ID");//车辆信息ID
			List<Object> params = new ArrayList<Object>();
			params.add(vehicleId);
			Map<String, Object> vehicleInfo = reDao.getVecihleDetail(params);//打印车辆信息查询
			act.setOutData("vehicleInfo", vehicleInfo);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "打印车辆信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 判断是否是历史退回车
	 */
	public void getHisVehicle()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String VIN = CommonUtils.checkNull(request.getParamValue("VIN"));//车VIN
			List<Object> params = new ArrayList<Object>();
			params.add(VIN);
			params.add(Constant.RETREAT_STATUS02);
			List<Map<String, Object>> vehicleInfo = reDao.getHisVehicle(params);
			if(vehicleInfo!=null && vehicleInfo.size()>0){
				String retreatdes=vehicleInfo.get(0).get("RETREATDES").toString();//取最新一次的退车原因
				act.setOutData("retreatdes", retreatdes);//退车原因
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "退回历史车辆信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * @Title :
	 * @Description: 入库打印
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-5-30 ranjian
	 */
	public void SitCodePrint()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String vehicleId = CommonUtils.checkNullNum(request.getParamValue("vehicle_id"));
			String vinCode = CommonUtils.checkNull(request.getParamValue("vinCode"));
			List<Object> params = new ArrayList<Object>();
			params.add(Constant.STATUS_ENABLE);
			params.add(vehicleId);
			params.add(vinCode);
			Map<String, Object> valueMap = vDao.getVechileByVId(params);//车辆信息
			act.setOutData("valueMap", valueMap);
			act.setForword(SitCodePrintUtl);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
							"库位码打印");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
		
	}
	
	
	
	/**结接车入库*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void readyRecieveVehichle(){
	    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String vin  = request.getParamValue("VIN");
			String yieldly = request.getParamValue("YIELDLY");
			String inStoreType = request.getParamValue("IN_STORE_TYPE");//入库类型
			String inWay = request.getParamValue("inWay");//入库方式，手动，自动
			List params = new ArrayList();
			params.add(vin);
			Map<String,Object> vehMap =reDao.getVehicleQuery(params);
			//如果这VIN所代表的车辆存在
			if(null !=vehMap && !vehMap.isEmpty()){
				//判断车架号是否在库存中
				
				//判断车辆是否已经在库存中了
				List<Map<String,Object>> inSitVeh = reDao.getInSitVeh(vin);
				if(null == inSitVeh || inSitVeh.size() == 0){
					if (!"1".equals(inStoreType)) {
						//普通区
						//返回车辆信息
						returnVehArea(vin);
						//如果是自动入库则根据规则自动分配区道位
						if(inWay.equals("ZD")){
							recieveVehichle(vehMap,yieldly);
						}
					} else {
						//试制试验区
						//返回车辆信息
						returnVehArea(vin);
						//如果是自动入库则根据规则自动分配区道位
						if(inWay.equals("ZD")){
							recieveVehichle(vehMap,yieldly);
						}
					}
					
				}else{
					act.setOutData("returnValue", 2);
				}
				
			}else{
				act.setOutData("returnValue", 3);
			}
		
		} catch(BizException e){
			act.setException(e);
		} catch (Exception e) {
			act.setOutData("returnValue", 99);
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

   private void returnVehArea(String vin) {
	   List<Map<String, Object>> list = reDao.getInVehInfo(vin);
	   if(null != list && list.size() > 0){
		   Map<String, Object> data = list.get(0);
		   
		   act.setOutData("resultMap", data);
		   act.setOutData("returnValue",1);
	   }else{
		   act.setOutData("returnValue",4);
	   }
   }

/**接车入库主要方法
 * @param yieldly 
 * @throws Exception **/
	private void recieveVehichle(Map<String, Object> vehMap, String yieldly) throws BizException,Exception {
		//通过VIN好查询到，同物料，同车系的非空道可以入库，库位号
		String vin = (String) vehMap.get("VIN");
		BigDecimal vehId = (BigDecimal) vehMap.get("VEHICLE_ID");
		Long vinLongId = vehId.longValue();
		TtSalesVehicelInPO vh = new TtSalesVehicelInPO();
		vh.setVin(vin);
		List<PO> pos = reDao.select(vh);
		TtSalesVehicelInPO po = 	(TtSalesVehicelInPO) pos.get(0);
		String sitName = "";
		List<Object> scode = new ArrayList<Object>();
		String areaName = "";
		String roadName = "";
		Long areaId = null;
		Long roadId = null;
		Long sitId = null;
		boolean isSpecialFlag = false;
		String speNo = null;
		//如果特殊订单号不为空，则特殊定单的停在同一个区道为
		if(null != po.getSpecialOrderNo() && !"".equals(po.getSpecialOrderNo())){
			isSpecialFlag = true;
		    speNo = (String) vehMap.get("SPECIAL_ORDER_NO");
		}else{
			isSpecialFlag = false;
		}
		
		List<Map<String,Object>> sitList = reDao.getEableSit(vin,isSpecialFlag,speNo,"scan","");
	
		//如果存在，同物料，同车系的非空道可以入库，库位号，则按照区，道，位从小到大的顺序一次入库
		if(null != sitList && sitList.size() > 0){
			Map<String,Object> postionMap = sitList.get(0);
			BigDecimal bigAreaId = (BigDecimal)postionMap.get("AREA_ID");
			 areaId = bigAreaId.longValue();
		    areaName =  (String)postionMap.get("AREA_NAME");
			
		    BigDecimal bigRoadId = (BigDecimal)postionMap.get("ROAD_ID");
			 roadId = bigRoadId.longValue();
		    roadName = (String)postionMap.get("ROAD_NAME");

			BigDecimal bigSitId = (BigDecimal)postionMap.get("SIT_ID");
			 sitId = bigSitId.longValue();
			sitName =  (String)postionMap.get("SIT_NAME");
		}else{//如果不存在，同物料，同车系的非空道可以入库，库位号，说明需要开辟新的库道
				List<Map<String,Object>> roadList = reDao.getNullRoadList();
				if(null != roadList && roadList.size() > 0){
					Map<String,Object> postionMap = roadList.get(0);
					BigDecimal bigAreaId = (BigDecimal)postionMap.get("AREA_ID");
					 areaId = bigAreaId.longValue();
				    areaName =  (String)postionMap.get("AREA_NAME");
					
				    BigDecimal bigRoadId = (BigDecimal)postionMap.get("ROAD_ID");
					 roadId = bigRoadId.longValue();
				    roadName = (String)postionMap.get("ROAD_NAME");

					BigDecimal bigSitId = (BigDecimal)postionMap.get("SIT_ID");
					 sitId = bigSitId.longValue();
					sitName =  (String)postionMap.get("SIT_NAME");
//						act.setOutData("returnValue",50);
				}else{
					act.setOutData("returnValue", 5);
					throw new BizException("没有足够的区道位！");
				}
			
		}
		
		scode.add(areaName);
		scode.add(roadName);
		scode.add(sitName);

		String sitCode = CommonUtils.getSitCode(scode);
		act.setOutData("AREA_ID", areaId);
		act.setOutData("AREA_NAME", areaName);
		act.setOutData("ROAD_ID", roadId);
		act.setOutData("ROAD_NAME", roadName);
		act.setOutData("SIT_ID", sitId);
		act.setOutData("SIT_NAME", sitName);
		act.setOutData("SIT_CODE", sitCode);
	
	}
	
	/**
	 * 试验车获取区道位
	 * @author liufazhong
	 */
	private void recieveVehichleSy(Map<String, Object> vehMap, String yieldly) throws Exception {
		
		try {
			//通过VIN好查询到，同物料，同车系的非空道可以入库，库位号
			String vin = (String) vehMap.get("VIN");
			BigDecimal vehId = (BigDecimal) vehMap.get("VEHICLE_ID");
			Long vinLongId = vehId.longValue();
			TtSalesVehicelInPO vh = new TtSalesVehicelInPO();
			vh.setVin(vin);
			List<PO> pos = reDao.select(vh);
			TtSalesVehicelInPO po = 	(TtSalesVehicelInPO) pos.get(0);
			String sitName = "";
			List<Object> scode = new ArrayList<Object>();
			String areaName = "";
			String roadName = "";
			Long areaId = null;
			Long roadId = null;
			Long sitId = null;
			Long dealerId = null;
			//如果特殊订单号不为空，则特殊定单的停在同一个区道为
			if(null != po.getSpecialOrderNo() && !"".equals(po.getSpecialOrderNo())){
				//存在特殊物料的不能为试制试验区
				throw new BizException("存在特殊物料的不能为试制试验区");
			}
			
			List<Map<String,Object>> sitList = reDao.getEableSitSy(vin,dealerId);
		
			//如果存在，同物料，同车系的非空道可以入库，库位号，则按照区，道，位从小到大的顺序一次入库
			if(null != sitList && sitList.size() > 0){
				Map<String,Object> postionMap = sitList.get(0);
				BigDecimal bigAreaId = (BigDecimal)postionMap.get("AREA_ID");
				areaId = bigAreaId.longValue();
			    areaName =  (String)postionMap.get("AREA_NAME");
				
			    BigDecimal bigRoadId = (BigDecimal)postionMap.get("ROAD_ID");
				roadId = bigRoadId.longValue();
			    roadName = (String)postionMap.get("ROAD_NAME");

				BigDecimal bigSitId = (BigDecimal)postionMap.get("SIT_ID");
				sitId = bigSitId.longValue();
				sitName =  (String)postionMap.get("SIT_NAME");
			}else{//如果不存在，同物料，同车系的非空道可以入库，库位号，说明需要开辟新的库道
					List<Map<String,Object>> roadList = reDao.getNullRoadListSy();
					if(null != roadList && roadList.size() > 0){
						Map<String,Object> postionMap = roadList.get(0);
						BigDecimal bigAreaId = (BigDecimal)postionMap.get("AREA_ID");
						areaId = bigAreaId.longValue();
					    areaName =  (String)postionMap.get("AREA_NAME");
						
					    BigDecimal bigRoadId = (BigDecimal)postionMap.get("ROAD_ID");
						roadId = bigRoadId.longValue();
					    roadName = (String)postionMap.get("ROAD_NAME");

						BigDecimal bigSitId = (BigDecimal)postionMap.get("SIT_ID");
						sitId = bigSitId.longValue();
						sitName =  (String)postionMap.get("SIT_NAME");
//						act.setOutData("returnValue",50);
					}else{
						act.setOutData("returnValue", 5);
						throw new BizException("");
					}
				
			}
			
			scode.add(areaName);
			scode.add(roadName);
			scode.add(sitName);

			String sitCode = CommonUtils.getSitCode(scode);
			act.setOutData("AREA_ID", areaId);
			act.setOutData("AREA_NAME", areaName);
			act.setOutData("ROAD_ID", roadId);
			act.setOutData("ROAD_NAME", roadName);
			act.setOutData("SIT_ID", sitId);
			act.setOutData("SIT_NAME", sitName);
			act.setOutData("SIT_CODE", sitCode);
		
		} catch (Exception e) {
			throw new Exception(e);
		}//获取库位码
	}
	
	public void saveVehStorage(){
		  AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String vin = request.getParamValue("VIN");
			String yieldly = request.getParamValue("userAreaId");
			String inStoreType = request.getParamValue("inStoreType");
			String sitCode = "";
			String sitId =  "";
			if ("1".equals(inStoreType)) {
				sitCode = request.getParamValue("sySitCode");
				sitId =  request.getParamValue("sySitName");
			} else {
				sitCode = request.getParamValue("SIT_CODE");
				sitId =  request.getParamValue("SIT_NAME");
			}
			String accPersonId = request.getParamValue("PER_ID");
			List<Object> params = new ArrayList<Object>();
			params.add(vin);
			Map<String, Object> vehMap = reDao.getVehicleQuery(params);
			//如果这VIN所代表的车辆存在
			if(null !=vehMap && !vehMap.isEmpty()){
				
				TtSalesVehicelInPO oldveh = new TtSalesVehicelInPO();
				oldveh.setVin(vin);
				TtSalesVehicelInPO newveh = new TtSalesVehicelInPO();
			
				newveh.setYieldly(Long.parseLong(yieldly));
				
				TtSalesSitPO sitPo = new TtSalesSitPO();
				TtSalesSitPO oldsitPo = new TtSalesSitPO();
				oldsitPo.setSitId(Long.parseLong(sitId));
				
				List<PO>  checkPos = reDao.select(oldsitPo);
				TtSalesSitPO checkPo = (TtSalesSitPO) checkPos.get(0);
				//验证SIT重复
				if(null != checkPo.getVehicleId() && checkPo.getVehicleId() > 0){
					act.setOutData("returnValue",15);
					return;
				}
				newveh.setPerId(Long.valueOf(accPersonId));
				newveh.setSitCode(sitCode);
				newveh.setSitId(oldsitPo.getSitId());
				newveh.setIsStorage(Constant.VEHICLE_IN_STATUS_01);
				Date orgStoreageDate = new Date(); 
				newveh.setOrgStorageDate(orgStoreageDate);
				//生命周期
				if ("1".equals(inStoreType)) {
					newveh.setLifeCycle(Constant.VEHICLE_LIFE_06);
				} else {
					newveh.setLifeCycle(Constant.VEHICLE_LIFE_02);
				}
				//车辆状态
				newveh.setLockStatus(Constant.LOCK_STATUS_01);		
				//回填库位码和库位号
				reDao.update(oldveh, newveh);
				//更新到库位表中去
				Long vehId =Long.parseLong(SequenceManager.getSequence(null));
				//在车辆表中插入一条新的记录
				reDao.saveVehicleInfo(vehId,vin,logonUser.getUserId());
				
				sitPo.setVehicleId(vehId);
				reDao.update(oldsitPo, sitPo);
				
				TtSalesVehicelInPO veIn = (TtSalesVehicelInPO) reDao.select(oldveh).get(0);
				if ("0".equals(inStoreType)) {
					String sepcialOrderNo = CommonUtils.checkNull(vehMap.get("SPECIAL_ORDER_NO"));
					saveErpData(vin, orgStoreageDate, veIn.getErpNo(),sepcialOrderNo);
				}
				act.setOutData("returnValue",1);
			}else{
				act.setOutData("returnValue",2);
			}
		
		} catch(BizException e) {
			act.setException(e);
		} catch (Exception e) {
			act.setOutData("returnValue", 99);
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接车入库新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
		
	}

	public void queryMesVehicle(){
		 AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		 try {
			 String vin = request.getParamValue("VIN");
			 String isStorage = request.getParamValue("IS_STORAGE");
			 String startDate = request.getParamValue("START_DATE");
			 String endDate = request.getParamValue("END_DATE");
			 String materailCode =  request.getParamValue("MATERAIL_CODE");
			 String erpOrderNum = request.getParamValue("ERP_NO");
			 String queryType =  request.getParamValue("queryType");
			 Map<String,String> conditions = new HashMap<String, String>();
			 conditions.put("VIN", vin);
			 conditions.put("IS_STORAGE", isStorage);
			 conditions.put("START_DATE", startDate);
			 conditions.put("END_DATES", endDate);
			 conditions.put("MATERAIL_CODE", materailCode);
			 conditions.put("ERPORDER_NUM", erpOrderNum);
			 if("1".equals(queryType)){
				 Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
				 PageResult<Map<String, Object>> ps = reDao.queryMesVehicle(conditions, curPage,
							Constant.PAGE_SIZE);
				 act.setOutData("ps", ps);
			 }else{
					List<Map<String, Object>> maps = reDao.exportMesVehicle(conditions);
					String[] head = {"VIN","物料代码","物料名称","生产日期","下线日期","合格证号","发动机号","ERP工单号","特殊订单号","是否入库"};
					String[] columns={"VIN","MATERIAL_CODE","MATERIAL_NAME","PRODUCT_DATE","OFFLINE_DATE","HEGEZHENG_CODE","ENGINE_NO","ERP_NO","SPECIAL_ORDER_NO","IS_STORAGE"};
					try {
						ToExcel.toReportExcel(ActionContext.getContext().getResponse(), request,"日志报表.xls", head,columns,maps);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
			 }
		    } catch (Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "MES车辆信息查询");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		
	}
	
	public void initMesVehichle(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initMesVehichle);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "EPR接口维护查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void saveErpData(String vin,Date orgStoreageDate,String erpOrderNo,String specialOrderNo){
		Map<String,Object> map = reDao.queryVinMater(vin);
		String materialCode = "";
		if(!"".equals(specialOrderNo)){//如果特殊订单号不为空则物料为超级物料
			Map<String,Object> superMaterialMap = reDao.getSuperMaterial(vin,specialOrderNo);
			materialCode = CommonUtils.checkNull(superMaterialMap.get("MATNR"));
		}else{
			if(!map.isEmpty()){
				materialCode = CommonUtils.checkNull(map.get("MATERIAL_CODE"));
			}
		}
//		TiExpBusVehStorePO conditions = new TiExpBusVehStorePO();
//		conditions.setMaterial(materialCode);
//		conditions.setOrderid(erpOrderNo);
//		conditions.setIsRead(0);
//		
//		List<TiExpBusVehStorePO> listPo = reDao.select(conditions);
//		//如果存在主数据，则只需要修改主数据的对应物料的数量
//		if(null != listPo && listPo.size()> 0){
//			TiExpBusVehStorePO   storePo = listPo.get(0);
//			Long revId = storePo.getRevId();
//			//保存详细信息
//			TiExpBusVehStoreDetPO po = new TiExpBusVehStoreDetPO();
//			Long detId = Long.parseLong(SequenceManager.getSequence(null));
//			
//			po.setDetId(detId);
//			//关联的ID
//			po.setRevId(revId);
//			po.setSerialno(vin);
//			po.setMatdocItm("0001");
//			po.setIsRead(0);
//			reDao.insert(po);
//			reDao.updateEntryQnt(conditions);
//		}else{//如果主数据不存在自
//			TiExpBusVehStorePO  po = new TiExpBusVehStorePO();
//			Long revId = Long.parseLong(SequenceManager.getSequence(null));
//			
//			po.setRevId(revId);
//			//
//			po.setPlant("1000");//1000.整车
//			po.setStgeLoc("3000");//库位,整车
//			po.setMoveType("101");//移动类型.收货
//			po.setEntryQnt(1L);
//			po.setEntryUom("");
//			po.setOrderid(erpOrderNo);
//			po.setPstngDate(orgStoreageDate);
//			//物料
//			po.setMaterial(materialCode);
//			po.setHeaderTxt(ErpInterfaceCommon.getServiceDate());
//			po.setIsRead(0);
//			reDao.insert(po);
//			
//			//保存详细信息
//			TiExpBusVehStoreDetPO detpo = new TiExpBusVehStoreDetPO();
//			detpo.setDetId(revId);
//			//关联的ID
//			detpo.setRevId(revId);
//			detpo.setSerialno(vin);
//			detpo.setMatdocItm("0001");
//			detpo.setIsRead(0);
//			reDao.insert(detpo);
//		}
	}

}
