package com.infodms.dms.actions.sales.storage.storagebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtSalesRoadBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.storagebase.ReservoirRoadDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesRoadPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : reservoirRoadManage 
 * @Description   : 库道管理控制类 
 * @author        : ranjian
 * CreateDate     : 2013-4-2
 */
public class reservoirRoadManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final ReservoirRoadDao reDao = ReservoirRoadDao.getInstance();
	private final String reservoirRoadInitUrl = "/jsp/sales/storage/storagebase/reservoirRoadManage/reservoirRoadList.jsp";
	private final String addReservoirRoadInitUrl = "/jsp/sales/storage/storagebase/reservoirRoadManage/addReservoirRoad.jsp";


	/**
	 * 
	 * @Title      : 
	 * @Description: 库道管理初始化查询条件
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void reservoirRoadInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String yieldly = request.getParamValue("YIELDLY");// 产地
			String seachY=yieldly;	
			if(yieldly==null || "".equals(yieldly)){//首次进入
				if(list_yieldly.size()>0){
					Map<String, Object> map=(Map<String, Object>)list_yieldly.get(0);//获取第一个 如有多个情况下
					seachY=map.get("AREA_ID").toString();
				}
			}
			List<Map<String, Object>> list_Re = reDao.getReservoirValue(seachY);//获取库区列表
			act.setOutData("list_Re", list_Re);
			act.setForword(reservoirRoadInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"库道信息查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询区下的道数
	 */
	public void getAreaRoadNum(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly=CommonUtils.checkNull(request.getParamValue("yieldly"));
			String kAreaId=CommonUtils.checkNull(request.getParamValue("kAreaId"));
			String roadNum = reDao.getAreaRoadNum(yieldly,kAreaId);//获取库区列表
			act.setOutData("roadNum", roadNum);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询区下的位数
	 */
	public void getRoadSitNum(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String areaId=CommonUtils.checkNull(request.getParamValue("areaId"));
			String roadId=CommonUtils.checkNull(request.getParamValue("roadId"));
			String sitNum = reDao.getRoadSitNum(areaId,roadId);//获取库区列表
			act.setOutData("sitNum", sitNum);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库道管理查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void reservoirRoadQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = request.getParamValue("YIELDLY"); // 产地
			String areaName = request.getParamValue("AREA_NAME"); // 库区
			String roadName = request.getParamValue("ROAD_NAME_"); // 库道
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("YIELDLY", yieldly);
			map.put("AREA_NAME", areaName);
			map.put("ROAD_NAME", roadName);
			map.put("poseId", logonUser.getPoseId().toString());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getReservoirRoadQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库道信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库道信息新增初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void addReservoirRoadInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			String yieldly = request.getParamValue("YIELDLY");// 产地
			String seachY=yieldly;	
			if(yieldly==null || "".equals(yieldly)){//首次进入
				if(list_yieldly.size()>0){
					Map<String, Object> map=(Map<String, Object>)list_yieldly.get(0);//获取第一个 如有多个情况下
					seachY=map.get("AREA_ID").toString();
				}
			}
			List<Map<String, Object>> list_Re = reDao.getReservoirValue(seachY);//获取库区列表
			act.setOutData("list_Re", list_Re);
			act.setForword(addReservoirRoadInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"库道信息新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 添加库道信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void addReservoirRoad() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String areaName = CommonUtils.checkNull(request.getParamValue("AREA_NAME")); //所属库区 
			String operatorType = CommonUtils.checkNull(request.getParamValue("OPERATOR_TYPE")); //操作类型
			String roadCount = CommonUtils.checkNull(request.getParamValue("ROAD_COUNT")); //道数
			int count=Integer.parseInt(roadCount);
			//存放查询条件
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("YIELDLY", yieldly);
			map.put("areaName", areaName);
			//TtSalesRoadPO
			//根据操作类型判断是否是添加库道或减少库道
			if(operatorType!=null && !"".equals(operatorType)){
				//根据地区，库区查询原有的库区最大库道号依次往上加传入的道数
				//获取库道最大
				int roadMaxNum=reDao.getMaxRoad(map);
				int roadNum=0;//当前地区下该库区是否有库道0为无库道
				if(Integer.parseInt(operatorType)==Constant.OPERATOR_TYPE_01){//新增道数
					if(roadMaxNum==0){//如果该地区，该库区下无库道，直接累加
						roadNum=0;//无库道
					}else{
						roadNum=roadMaxNum;//有库道
					}
					for(int i=0;i<count;i++){//++库道
						String roadId = SequenceManager.getSequence(null);//库道序列
						//添加方法
						TtSalesRoadPO tspo=new TtSalesRoadPO();
						tspo.setRoadId(Long.parseLong(roadId));//库道ID
						tspo.setAreaId(Long.parseLong(areaName));//库区ID
						tspo.setRoadName(String.valueOf(roadNum+i+1).length()<2?"0"+String.valueOf(roadNum+i+1):String.valueOf(roadNum+i+1));//库道
						tspo.setInStatus(Constant.AUTO_IN_STATUS_01.longValue());//入库状态(正常)
						tspo.setOutStatus(Constant.AUTO_OUT_STATUS_01.longValue());//出库状态
						tspo.setStatus(Constant.STATUS_ENABLE.longValue());//状态
						tspo.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
						tspo.setCreateBy(logonUser.getUserId());//创建人
						tspo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
						tspo.setUpdateBy(logonUser.getUserId());//修改人
						reDao.reservoirSalesRoadAdd(tspo);
					}
					act.setOutData("returnValue", 1);
					
				}else if(Integer.parseInt(operatorType)==Constant.OPERATOR_TYPE_02){//减少道数
					//过滤 如果该地区该库区无库道   或者   该地区该库道小于前台传入的数字时候，无法减少道数
					if(roadMaxNum!=0 && roadMaxNum>=count){
						List<Object> params=new ArrayList<Object>();
						params.add(areaName);//库区ID
						params.add(Constant.STATUS_ENABLE);//库道状态
						params.add(Constant.STATUS_ENABLE);//库位状态
						params.add(String.valueOf(roadMaxNum+1-count).length()<2?"0"+String.valueOf(roadMaxNum+1-count):String.valueOf(roadMaxNum+1-count));//开始
						params.add(String.valueOf(roadMaxNum).length()<2?"0"+String.valueOf(roadMaxNum):String.valueOf(roadMaxNum));//结束
						params.add(Constant.DEFAULT_VALUE);//车辆默认值
						List<Map<String,Object>> list=reDao.getSitIsHaveVehicle(params);
						if(list!=null && list.size()>0){//有车
							int info=list.size();
//							for(int i=0;i<list.size();i++){
//								info+=list.get(i).get("ROAD_NAME")+",";
//							}
							act.setOutData("info", info);//提示信息
							act.setOutData("returnValue", 3);
							
						}else{
							for(int i=0;i<count;i++){//++库道
								//添加方法
								TtSalesRoadPO tspo=new TtSalesRoadPO();
								tspo.setStatus(Constant.STATUS_DISABLE.longValue());//状态(无效)
								tspo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
								tspo.setUpdateBy(logonUser.getUserId());//修改人
								
								TtSalesRoadBean seachBean=new TtSalesRoadBean();
								seachBean.setRoadName(String.valueOf(roadMaxNum-i).length()<2?"0"+String.valueOf(roadMaxNum-i):String.valueOf(roadMaxNum-i));
								seachBean.setAreaId(Long.parseLong(areaName));
								seachBean.setYieldly(Long.parseLong(yieldly));
								reDao.reservoirSalesRoadUpdate(tspo,seachBean);
							}
							act.setOutData("returnValue", 1);
						}
					}else{
						act.setOutData("roadMaxNum", roadMaxNum);
						act.setOutData("returnValue", 2);
					}
				}
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增库道信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 入库锁定
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void roadInClock() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			TtSalesRoadPO tspo=new TtSalesRoadPO();
			tspo.setInStatus(Constant.AUTO_IN_STATUS_02.longValue());
			tspo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
			tspo.setUpdateBy(logonUser.getUserId());//修改人
			String[] roadIds=request.getParamValues("groupIds");
			String roadId="";
			String newRoadId="";
			if(roadIds.length>0){
				for(int i=0;i<roadIds.length;i++){
					roadId+=roadIds[i]+",";
				}
				newRoadId=roadId.substring(0, roadId.length()-1);
			}
			reDao.getRoadInStatus(tspo,newRoadId);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 入库锁定道信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 入库锁定解除
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void inClockClear() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			TtSalesRoadPO tspo=new TtSalesRoadPO();
			tspo.setInStatus(Constant.AUTO_IN_STATUS_01.longValue());
			tspo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
			tspo.setUpdateBy(logonUser.getUserId());//修改人
			String[] roadIds=request.getParamValues("groupIds");
			String roadId="";
			String newRoadId="";
			if(roadIds.length>0){
				for(int i=0;i<roadIds.length;i++){
					roadId+=roadIds[i]+",";
				}
				newRoadId=roadId.substring(0, roadId.length()-1);
			}
			reDao.getRoadInStatus(tspo,newRoadId);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 入库锁定信息解除");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 出库锁定
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void roadOutClock() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			TtSalesRoadPO tspo=new TtSalesRoadPO();
			tspo.setOutStatus(Constant.AUTO_OUT_STATUS_02.longValue());
			tspo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
			tspo.setUpdateBy(logonUser.getUserId());//修改人
			String[] roadIds=request.getParamValues("groupIds");
			String roadId="";
			String newRoadId="";
			if(roadIds.length>0){
				for(int i=0;i<roadIds.length;i++){
					roadId+=roadIds[i]+",";
				}
				newRoadId=roadId.substring(0, roadId.length()-1);
			}
			reDao.getRoadOutStatus(tspo,newRoadId);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 出库锁定道信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 出库锁定解除
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void outClockClear() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			TtSalesRoadPO tspo=new TtSalesRoadPO();
			tspo.setOutStatus(Constant.AUTO_OUT_STATUS_01.longValue());
			tspo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
			tspo.setUpdateBy(logonUser.getUserId());//修改人
			String[] roadIds=request.getParamValues("groupIds");
			String roadId="";
			String newRoadId="";
			if(roadIds.length>0){
				for(int i=0;i<roadIds.length;i++){
					roadId+=roadIds[i]+",";
				}
				newRoadId=roadId.substring(0, roadId.length()-1);
			}
			reDao.getRoadOutStatus(tspo,newRoadId);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 出库锁定道信息解除");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 获取库区下拉列表
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public void getRegionList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));
			List<Map<String, Object>> reList = reDao.getReservoirValue(yieldly);//获取库区列表
			act.setOutData("reList", reList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "获取库区下拉列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
