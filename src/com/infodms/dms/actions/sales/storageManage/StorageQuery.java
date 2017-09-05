package com.infodms.dms.actions.sales.storageManage;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.StorageQueryDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleLocationChangeDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class StorageQuery extends BaseDao{
	public Logger logger = Logger.getLogger(StorageQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	} 
	private final String  StorageQueryDLRInitUrl = "/jsp/sales/storageManage/storageQueryDLRInit.jsp";
	private final String  storageQueryDLRLowerInitUrl = "/jsp/sales/storageManage/storageQueryDLRLowerInit.jsp";
	private final String  StorageQueryOEMInitUrl = "/jsp/sales/storageManage/storageQueryOEMInit.jsp";
	private final String  StorageQueryDLRSumUrl = "/jsp/sales/storageManage/storageQueryDLR_Sum.jsp";
	
	/**
	 * FUNCTION		:	经销商仓库位置下拉列表
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-20
	 */
	public void warehouseList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String warehouseType = act.getRequest().getParamValue("warehouseType") ;
			if (warehouseType == null || warehouseType.equals("")) {
				warehouseType = "" ;
			}
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
			List<Map<String,Object>> list = VehicleLocationChangeDAO.warehouseQuery(dealerIds__,"") ;
			act.setOutData("list", list) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 

	/**
	 * FUNCTION		:	库存查询页面初始化(DLR)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-25
	 */
	public void StorageQueryDLRInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			/*List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);*/
			
		
			
			act.setForword(StorageQueryDLRInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询页面初始化(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * FUNCTION		:	下级经销商库存查询页面初始化(DLR)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-25
	 */
	public void storageQueryDLRLowerInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			/*List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);*/
			act.setForword(storageQueryDLRLowerInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询页面初始化(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * FUNCTION		:	库存查询:汇总查询(DLR)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-25
	 */
	/*public void StorageQueryDLR_Sum(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			//得到查询条件
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组
			String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));//物料
			String days =  CommonUtils.checkNull(request.getParamValue("days"));					//库存超过
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String vehicle_life = CommonUtils.checkNull(request.getParamValue("vehicle_life")) ;	// 汽车生命周期
			String vehicleOwn = CommonUtils.checkNull(request.getParamValue("vehicleOwn"));			//车辆所有者
			String whName = CommonUtils.checkNull(request.getParamValue("whName"));  				//所在仓库
			String lockStatus = CommonUtils.checkNull(request.getParamValue("lockStatus"));  		//锁定状态
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;						//当前用户职位对应的经销商ID
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")): 1;
			String areaId=request.getParamValue("areaId");
			if(areaId==null||areaId==""){
				areaId=MaterialGroupManagerDao.getMyPoseBusiness(String.valueOf(logonUser.getPoseId()));
			}
				PageResult<Map<String, Object>> ps = StorageQueryDAO.getSumList(areaId,vehicleOwn, whName, dealerIds__,materialCode,materialCode__,days,vin,vehicle_life,lockStatus,Constant.PAGE_SIZE_MAX, curPage);
				act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询页面初始化(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} */
	
	public void StorageQueryDLR_Sum(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			//得到查询条件
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组
			String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));//物料
			String days =  CommonUtils.checkNull(request.getParamValue("days"));					//库存超过
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String vehicle_life = CommonUtils.checkNull(request.getParamValue("vehicle_life")) ;	// 汽车生命周期
			String vehicleOwn = CommonUtils.checkNull(request.getParamValue("vehicleOwn"));			//车辆所有者
			String whName = CommonUtils.checkNull(request.getParamValue("whName"));  				//所在仓库
			String lockStatus = CommonUtils.checkNull(request.getParamValue("lockStatus"));  		//锁定状态
			String storStartDate=CommonUtils.checkNull(request.getParamValue("storStartDate")); // 入库时间开始
			String storEndDate=CommonUtils.checkNull(request.getParamValue("storEndDate"));  //入库时间结束
			String areaId = "";
			Long poseId = logonUser.getPoseId();
			
			DealerRelation dr = new DealerRelation() ;
			//String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(), poseId);					//当前用户职位对应的经销商ID
			String dealerIds__=logonUser.getDealerId();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			
			PageResult<Map<String, Object>> ps = StorageQueryDAO.getSumList_SUZUKI(poseId,areaId, whName, dealerIds__,materialCode,materialCode__,days,vin,vehicle_life,lockStatus,storStartDate,storEndDate,Constant.PAGE_SIZE_MAX, curPage);
				
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询页面初始化(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void StorageQueryDLR_Sum_Lower(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			//得到查询条件
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组
			String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));//物料
			String days =  CommonUtils.checkNull(request.getParamValue("days"));					//库存超过
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String vehicle_life = CommonUtils.checkNull(request.getParamValue("vehicle_life")) ;	// 汽车生命周期
			String vehicleOwn = CommonUtils.checkNull(request.getParamValue("vehicleOwn"));			//车辆所有者
			String whName = CommonUtils.checkNull(request.getParamValue("whName"));  				//所在仓库
			String lockStatus = CommonUtils.checkNull(request.getParamValue("lockStatus"));  		//锁定状态
			String areaId = "";
			DealerRelation dr = new DealerRelation() ;
			String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
			String dealerIds=request.getParamValue("dealerId");//当前登录经销商下级经销商ID
			String upDealerOrder=request.getParamValue("upDealerOrder");//上级采购订单号
			String downDealerOrder=request.getParamValue("downDealerOrder");//下级经销商采购订单号
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")): 1;
			
			PageResult<Map<String, Object>> ps = StorageQueryDAO.getSumList_Lower(dealerIds,upDealerOrder,downDealerOrder,areaId,vehicleOwn, whName, dealerIds__,materialCode,materialCode__,days,vin,vehicle_life,lockStatus,Constant.PAGE_SIZE_MAX, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询页面初始化(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	库存查询:明细查询(DLR)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-25
	 */
	public void StorageQueryDLR_Detail(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));	//物料
			String days =  CommonUtils.checkNull(request.getParamValue("days"));
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String vehicle_life = CommonUtils.checkNull(request.getParamValue("vehicle_life")) ;
			String vehicleOwn = CommonUtils.checkNull(request.getParamValue("vehicleOwn"));			//车辆所有者
			String whName = CommonUtils.checkNull(request.getParamValue("whName"));  				//所在仓库
			String lockStatus = CommonUtils.checkNull(request.getParamValue("lockStatus"));  		//锁定状态
			String storStartDate=CommonUtils.checkNull(request.getParamValue("storStartDate")); // 入库时间开始
			String storEndDate=CommonUtils.checkNull(request.getParamValue("storEndDate"));  //入库时间结束
			String areaId = "";
			DealerRelation dr = new DealerRelation() ;
		//	String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
			String dealerIds__=logonUser.getDealerId();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = StorageQueryDAO.getStorage_DLR_SUZUKI(areaId,whName,dealerIds__,materialCode,materialCode__,days,vin, vehicle_life, lockStatus,storStartDate, storEndDate,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询:明细查询(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	public void StorageQueryDLR_Detail_Lower(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));	//物料
			String days =  CommonUtils.checkNull(request.getParamValue("days"));
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String vehicle_life = CommonUtils.checkNull(request.getParamValue("vehicle_life")) ;
			String vehicleOwn = CommonUtils.checkNull(request.getParamValue("vehicleOwn"));			//车辆所有者
			String whName = CommonUtils.checkNull(request.getParamValue("whName"));  				//所在仓库
			String lockStatus = CommonUtils.checkNull(request.getParamValue("lockStatus"));  		//锁定状态
			String dealerIds=request.getParamValue("dealerId");//当前登录经销商下级经销商ID
			String upDealerOrder=request.getParamValue("upDealerOrder");//上级采购订单号
			String downDealerOrder=request.getParamValue("downDealerOrder");//下级经销商采购订单号
			String areaId = "";
			DealerRelation dr = new DealerRelation() ;
			String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = StorageQueryDAO.getStorage_DLR_Lower(dealerIds,upDealerOrder,downDealerOrder,areaId,vehicleOwn,whName,dealerIds__,materialCode,materialCode__,days,vin, vehicle_life, lockStatus, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询:明细查询(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/*
	 * 汇总导出
	 */
	public void sumDownLoad(){
		ResponseWrapper response = act.getResponse();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Map map = new HashMap();
		//得到查询条件
		RequestWrapper request = act.getRequest();
		String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组
		String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));//物料
		String days =  CommonUtils.checkNull(request.getParamValue("days"));					//库存超过
		String vin =  CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
		String vehicle_life = CommonUtils.checkNull(request.getParamValue("vehicle_life")) ;	// 汽车生命周期
		String vehicleOwn = CommonUtils.checkNull(request.getParamValue("vehicleOwn"));			//车辆所有者
		String whName = CommonUtils.checkNull(request.getParamValue("whName"));  				//所在仓库
		String lockStatus = CommonUtils.checkNull(request.getParamValue("lockStatus"));  		//锁定状态
		String storStartDate=CommonUtils.checkNull(request.getParamValue("storStartDate")); // 入库时间开始
		String storEndDate=CommonUtils.checkNull(request.getParamValue("storEndDate"));  //入库时间结束
		String areaId = "";
		DealerRelation dr = new DealerRelation() ;
		//String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
		String dealerIds__=logonUser.getDealerId();
		//业务参数
		OutputStream os = null;
		try {
			  String fileName = "经销商库存汇总.csv";
				// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				// 定义一个集合
				List<List<Object>> list = new LinkedList<List<Object>>();
				// 标题
				List<Object> listTemp = new LinkedList<Object>();
				listTemp.add("车系");
				listTemp.add("车型");
				listTemp.add("配置");
				listTemp.add("物料代码");
				listTemp.add("物料名称");
				listTemp.add("在途数量");
				listTemp.add("在库数量");
				listTemp.add("合计");
				list.add(listTemp);
			List <Map<String, Object>> rslist = StorageQueryDAO.getMySumList_SUZUKI(areaId, whName, dealerIds__,materialCode,materialCode__,days,vin,vehicle_life,lockStatus,storStartDate,storEndDate);
			for (int k = 0; k < rslist.size(); k++) {
				map = rslist.get(k);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("SERIES_NAME") != null ? map.get("SERIES_NAME") : "");
				listValue.add(map.get("MODEL_NAME") != null ? map.get("MODEL_NAME") : "");
				listValue.add(map.get("PACKAGE_NAME") != null ? map.get("PACKAGE_NAME") : "");
				listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				listValue.add(map.get("MATERIAL_NAME") != null ? map.get("MATERIAL_NAME") : "");
				listValue.add(map.get("ON_WAY") != null ? map.get("ON_WAY") : "");
				listValue.add(map.get("NO_WAY") != null ? map.get("NO_WAY") : "");
				listValue.add(map.get("SUM_NO") != null ? map.get("SUM_NO") : "");
				list.add(listValue);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询：汇总查询(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sumDownLoad_Lower(){
		ResponseWrapper response = act.getResponse();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Map map = new HashMap();
		//得到查询条件
		RequestWrapper request = act.getRequest();
		String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组
		String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));//物料
		String days =  CommonUtils.checkNull(request.getParamValue("days"));					//库存超过
		String vin =  CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
		String vehicle_life = CommonUtils.checkNull(request.getParamValue("vehicle_life")) ;	// 汽车生命周期
		String vehicleOwn = CommonUtils.checkNull(request.getParamValue("vehicleOwn"));			//车辆所有者
		String whName = CommonUtils.checkNull(request.getParamValue("whName"));  				//所在仓库
		String lockStatus = CommonUtils.checkNull(request.getParamValue("lockStatus"));  		//锁定状态
		String areaId = "";
		DealerRelation dr = new DealerRelation() ;
		String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
		String dealerIds=request.getParamValue("dealerId");//当前登录经销商下级经销商ID
		String upDealerOrder=request.getParamValue("upDealerOrder");//上级采购订单号
		String downDealerOrder=request.getParamValue("downDealerOrder");//下级经销商采购订单号
		//业务参数
		OutputStream os = null;
		try {
			  String fileName = "经销商库存汇总.csv";
				// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				// 定义一个集合
				List<List<Object>> list = new LinkedList<List<Object>>();
				// 标题
				List<Object> listTemp = new LinkedList<Object>();
				listTemp.add("经销商名称");
				listTemp.add("车系");
				listTemp.add("车型");
				listTemp.add("配置");
				listTemp.add("物料代码");
				listTemp.add("物料名称");
				listTemp.add("在途数量");
				listTemp.add("在库数量");
				listTemp.add("合计");
				list.add(listTemp);
			List <Map<String, Object>> rslist = StorageQueryDAO.getMySumList_Lower(dealerIds,upDealerOrder,downDealerOrder,areaId,vehicleOwn, whName, dealerIds__,materialCode,materialCode__,days,vin,vehicle_life,lockStatus);
			for (int k = 0; k < rslist.size(); k++) {
				map = rslist.get(k);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("DEALER_SHORTNAME") != null ? map.get("DEALER_SHORTNAME") : "");
				listValue.add(map.get("SERIES_NAME") != null ? map.get("SERIES_NAME") : "");
				listValue.add(map.get("MODEL_NAME") != null ? map.get("MODEL_NAME") : "");
				listValue.add(map.get("PACKAGE_NAME") != null ? map.get("PACKAGE_NAME") : "");
				listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				listValue.add(map.get("MATERIAL_NAME") != null ? map.get("MATERIAL_NAME") : "");
				listValue.add(map.get("ON_WAY") != null ? map.get("ON_WAY") : "");
				listValue.add(map.get("NO_WAY") != null ? map.get("NO_WAY") : "");
				listValue.add(map.get("SUM_NO") != null ? map.get("SUM_NO") : "");
				list.add(listValue);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询：汇总查询(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * 明细导出
	 */
	public void detailDownLoad(){
		ResponseWrapper response = act.getResponse();
		AclUserBean logonUser = null;
		OutputStream os = null;
		try {
			Map map=new HashMap();
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));	//物料
			String days =  CommonUtils.checkNull(request.getParamValue("days"));
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String vehicle_life = CommonUtils.checkNull(request.getParamValue("vehicle_life")) ;
			String vehicleOwn = CommonUtils.checkNull(request.getParamValue("vehicleOwn"));			//车辆所有者
			String whName = CommonUtils.checkNull(request.getParamValue("whName"));  				//所在仓库
			String lockStatus = CommonUtils.checkNull(request.getParamValue("lockStatus"));  		//锁定状态
			String storStartDate=CommonUtils.checkNull(request.getParamValue("storStartDate")); // 入库时间开始
			String storEndDate=CommonUtils.checkNull(request.getParamValue("storEndDate"));  //入库时间结束
			DealerRelation dr = new DealerRelation() ;
			String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
			String areaId = "";
			//传入参数			
			  String fileName = "经销商库存明细汇总.csv";
				// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				// 定义一个集合
				List<List<Object>> list = new LinkedList<List<Object>>();
				// 标题
				List<Object> listTemp = new LinkedList<Object>();
				listTemp.add("车型名称");
				listTemp.add("配置");
				listTemp.add("物料代码");
				listTemp.add("物料名称");
				listTemp.add("批次号");
				listTemp.add("VIN");
				listTemp.add("发动机号");
				listTemp.add("生产日期");
				listTemp.add("位置说明");
				listTemp.add("经销商");
				listTemp.add("库存状态");
				listTemp.add("锁定状态");
				listTemp.add("入库日期");
				listTemp.add("库存天数");
				list.add(listTemp);
			List<Map<String, Object>> rslist = StorageQueryDAO.getMyStorage_DLR_SUZUKI(areaId,whName,dealerIds__,materialCode,materialCode__,days,vin, vehicle_life, lockStatus,storStartDate,storEndDate);
			for (int k = 0; k < rslist.size(); k++) {
				map = rslist.get(k);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("MODEL_NAME") != null ? map.get("MODEL_NAME") : "");
				listValue.add(map.get("PACKAGE_NAME") != null ? map.get("PACKAGE_NAME") : "");
				listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				listValue.add(map.get("MATERIAL_NAME") != null ? map.get("MATERIAL_NAME") : "");
				listValue.add(map.get("BATCH_NO") != null ? map.get("BATCH_NO") : "");
				listValue.add(map.get("VIN") != null ? map.get("VIN") : "");
				listValue.add(map.get("ENGINE_NO") != null ? map.get("ENGINE_NO") : "");
				listValue.add(map.get("PRODUCT_DATE") != null ? map.get("PRODUCT_DATE") : "");
				listValue.add(map.get("WAREHOUSE_NAME") != null ? map.get("WAREHOUSE_NAME") : "");
				listValue.add(map.get("DEALER_SHORTNAME") != null ? map.get("DEALER_SHORTNAME") : "");
				listValue.add(map.get("LIFE_CYCLE") != null ? map.get("LIFE_CYCLE") : "");
				listValue.add(map.get("LOCK_STATUS") != null ? map.get("LOCK_STATUS") : "");
				listValue.add(map.get("STORAGE_TIME") != null ? map.get("STORAGE_TIME") : "");
				listValue.add(map.get("DAY_COUNT") != null ? map.get("DAY_COUNT") : "");
				list.add(listValue);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询:明细汇总查询(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void detailDownLoad_Lower(){
		ResponseWrapper response = act.getResponse();
		AclUserBean logonUser = null;
		OutputStream os = null;
		try {
			Map map=new HashMap();
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));	//物料
			String days =  CommonUtils.checkNull(request.getParamValue("days"));
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));
			String vehicle_life = CommonUtils.checkNull(request.getParamValue("vehicle_life")) ;
			String vehicleOwn = CommonUtils.checkNull(request.getParamValue("vehicleOwn"));			//车辆所有者
			String whName = CommonUtils.checkNull(request.getParamValue("whName"));  				//所在仓库
			String lockStatus = CommonUtils.checkNull(request.getParamValue("lockStatus"));  		//锁定状态
			DealerRelation dr = new DealerRelation() ;
			String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());					//当前用户职位对应的经销商ID
			String dealerIds=request.getParamValue("dealerId");//当前登录经销商下级经销商ID
			String upDealerOrder=request.getParamValue("upDealerOrder");//上级采购订单号
			String downDealerOrder=request.getParamValue("downDealerOrder");//下级经销商采购订单号
			String areaId = "";
			//传入参数			
			  String fileName = "经销商库存明细汇总.csv";
				// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
				response.setContentType("Application/text/csv");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
				// 定义一个集合
				List<List<Object>> list = new LinkedList<List<Object>>();
				// 标题
				List<Object> listTemp = new LinkedList<Object>();
				listTemp.add("经销商名称");
				listTemp.add("车型名称");
				listTemp.add("配置");
				listTemp.add("物料代码");
				listTemp.add("物料名称");
				listTemp.add("批次号");
				listTemp.add("VIN");
				listTemp.add("发动机号");
				listTemp.add("生产日期");
				listTemp.add("位置说明");
				listTemp.add("经销商");
				listTemp.add("库存状态");
				listTemp.add("锁定状态");
				listTemp.add("入库日期");
				listTemp.add("库存天数");
				list.add(listTemp);
			List<Map<String, Object>> rslist = StorageQueryDAO.getMyStorage_DLR_Lower(dealerIds,upDealerOrder,downDealerOrder,areaId,vehicleOwn,whName,dealerIds__,materialCode,materialCode__,days,vin, vehicle_life, lockStatus);
			for (int k = 0; k < rslist.size(); k++) {
				map = rslist.get(k);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("DEALER_SHORTNAME") != null ? map.get("DEALER_SHORTNAME") : "");
				listValue.add(map.get("MODEL_NAME") != null ? map.get("MODEL_NAME") : "");
				listValue.add(map.get("PACKAGE_NAME") != null ? map.get("PACKAGE_NAME") : "");
				listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				listValue.add(map.get("MATERIAL_NAME") != null ? map.get("MATERIAL_NAME") : "");
				listValue.add(map.get("BATCH_NO") != null ? map.get("BATCH_NO") : "");
				listValue.add(map.get("VIN") != null ? map.get("VIN") : "");
				listValue.add(map.get("ENGINE_NO") != null ? map.get("ENGINE_NO") : "");
				listValue.add(map.get("PRODUCT_DATE") != null ? map.get("PRODUCT_DATE") : "");
				listValue.add(map.get("WAREHOUSE_NAME") != null ? map.get("WAREHOUSE_NAME") : "");
				listValue.add(map.get("DEALER_SHORTNAME") != null ? map.get("DEALER_SHORTNAME") : "");
				listValue.add(map.get("LIFE_CYCLE") != null ? map.get("LIFE_CYCLE") : "");
				listValue.add(map.get("LOCK_STATUS") != null ? map.get("LOCK_STATUS") : "");
				listValue.add(map.get("STORAGE_TIME") != null ? map.get("STORAGE_TIME") : "");
				listValue.add(map.get("DAY_COUNT") != null ? map.get("DAY_COUNT") : "");
				list.add(listValue);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存查询:明细汇总查询(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * FUNCTION		:	经销商库存查询页面初始化(OEM)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-1
	 */
	public void storageQueryOEMInit(){
		AclUserBean logonUser = null;

		try {
            logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

            String dutyType=logonUser.getDutyType();
            act.setOutData("dutyType", dutyType);
            act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(StorageQueryOEMInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商库存查询页面初始化(OEM)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	经销商库存查询:页面展示(OEM)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-1
	 */
	public void storageQueryOEM(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			//选择经销商
			String dealerCode =  CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//物料组选择
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));
			//物料组选择
			String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));
			//库存天数
			String days =  CommonUtils.checkNull(request.getParamValue("days"));
			//VIN
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));	
			String batchNo = CommonUtils.checkNull(request.getParamValue("batchNo"));	//批次号 
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			orgId = logonUser.getOrgId().toString() ;

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = StorageQueryDAO.storageQueryOEM(dutyType,orgId, batchNo, dealerCode, materialCode,materialCode__, days, vin, areaIds, Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商库存查询:页面展示(OEM)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION		:	经销商库存下载
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-1
	 */
	public void donwload(){
		AclUserBean logonUser = null;
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			//选择经销商
			String dealerCode =  CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//物料组选择
			String materialCode =  CommonUtils.checkNull(request.getParamValue("materialCode"));
			//物料组选择
			String materialCode__ =  CommonUtils.checkNull(request.getParamValue("materialCode__"));
			//库存天数
			String days =  CommonUtils.checkNull(request.getParamValue("days"));
			//VIN
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));	
			String batchNo = CommonUtils.checkNull(request.getParamValue("batchNo"));	//批次号 
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());	//业务范围 
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			ResponseWrapper response = act.getResponse() ;
			// 导出的文件名
			String fileName = "经销商库存.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("经销商");
			listTemp.add("车型名称");
			listTemp.add("配置");
			listTemp.add("物料代码");
			listTemp.add("物料名称");
			listTemp.add("VIN");
			listTemp.add("发动机号");
			listTemp.add("批次号");
			listTemp.add("生产日期");
			listTemp.add("位置说明");
			listTemp.add("代管经销商");
			listTemp.add("库存状态");
			listTemp.add("入库日期");
			listTemp.add("库存天数");
			list.add(listTemp);

			List<Map<String, Object>> results = StorageQueryDAO.download(orgId, batchNo, dealerCode, materialCode, materialCode__, days, vin, areaIds) ;
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MODEL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("PACKAGE_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("VIN")));
				listTemp.add(CommonUtils.checkNull(record.get("ENGINE_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("BATCH_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("PRODUCT_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("VEHICLE_AREA")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("CODE_DESC")));
				listTemp.add(CommonUtils.checkNull(record.get("STORAGE_TIME")));
				listTemp.add(CommonUtils.checkNull(record.get("DAY_COUNT")));
				
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商库存下载失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void showWHNAME(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String ownid = request.getParamValue("ownid");//01 自有 02 代管
			if(Utility.testString(ownid)){
				String dealerId = logonUser.getDealerId();
				List<Map<String, Object>> list = dao.getOwnWarehouse(ownid, dealerId); 
				list.size();
				act.setOutData("list", list);
			}else{
				act.setOutData("list", null);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void showWHNAME__A(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String ownid = request.getParamValue("ownid");//01 自有 02 代管
			if(Utility.testString(ownid)){
				String dealerId = logonUser.getDealerId();
				Long own = new Long(ownid) ;
				List<Map<String, Object>> list = dao.getOwnWarehouse(own, dealerId); 
				// list.size();
				act.setOutData("list", list);
			}else{
				act.setOutData("list", null);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
