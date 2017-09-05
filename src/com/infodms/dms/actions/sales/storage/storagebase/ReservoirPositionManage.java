package com.infodms.dms.actions.sales.storage.storagebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.storagebase.ReservoirPositionDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesSitPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : reservoirPositionManage 
 * @Description   : 库位管理控制类 
 * @author        : ranjian
 * CreateDate     : 2013-4-5
 */
public class ReservoirPositionManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final ReservoirPositionDao reDao = ReservoirPositionDao.getInstance();
	private final String reservoirPositionInitUrl = "/jsp/sales/storage/storagebase/reservoirPositionManage/reservoirPositionList.jsp";
	private final String addReservoirPositionInitUrl = "/jsp/sales/storage/storagebase/reservoirPositionManage/addReservoirPosition.jsp";
	private final String printReservoirPositionInitUrl = "/jsp/sales/storage/storagebase/reservoirPositionManage/printReservoirPosition.jsp";
	private final String printReservoirPositionPageUrl = "/jsp/sales/storage/storagebase/reservoirPositionManage/printReservoirPositionPage.jsp";


	/**
	 * 
	 * @Title      : 
	 * @Description: 库位管理初始化查询条件
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void reservoirPositionInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(reservoirPositionInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"库位信息查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库位管理查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void reservoirPositionQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = request.getParamValue("YIELDLY"); // 产地
			String areaName = request.getParamValue("AREA_NAME"); // 库区
			String roadName = request.getParamValue("ROAD_NAME"); // 库道
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("YIELDLY", yieldly);
			map.put("AREA_NAME", areaName);
			map.put("ROAD_NAME", roadName);
			map.put("poseId", logonUser.getPoseId().toString());
			
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getReservoirPositionQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "库位信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库位信息新增初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void addReservoirPositionInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly=MaterialGroupManagerDao.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			List<Map<String, Object>> list_An = reDao.getReservoirValue(yieldly);//获取库区列表
			act.setOutData("list_An", list_An);
			act.setForword(addReservoirPositionInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"库位信息新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 添位库位信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void addReservoirPosition() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String areaName = CommonUtils.checkNull(request.getParamValue("AREA_NAME")); //库区 
			String roadName = CommonUtils.checkNull(request.getParamValue("ROAD_NAME")); //库道
			String sitCount = CommonUtils.checkNull(request.getParamValue("SIT_COUNT")); //位数
			String operatorType = CommonUtils.checkNull(request.getParamValue("OPERATOR_TYPE")); //操作类型
			String addType = CommonUtils.checkNull(request.getParamValue("addType")); //添加方式
			String startRoad = CommonUtils.checkNull(request.getParamValue("startRoad")); //库道开始
			String endRoad = CommonUtils.checkNull(request.getParamValue("endRoad")); //库道结束
			int count=Integer.parseInt(sitCount);
			
			if(addType.equals("only")){//单一添加
			//存放查询条件
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaName", areaName);//这个不需要
			map.put("roadName", roadName.length()<2?"0"+roadName:roadName);
			//TtSalesRoadPO
			//根据操作类型判断是否是添加库道或减少库道
			if(operatorType!=null && !"".equals(operatorType)){
				//根据地区，库区查询原有的库区最大库道号依次往上加传入的道数
				//获取库道最大
				int positionMaxNum=reDao.getMaxPosition(map);
				int sitNum=0;//当前地区下该库区是否有库道0为无库道
				if(Integer.parseInt(operatorType)==Constant.OPERATOR_TYPE_01){//新增道数
					if(positionMaxNum==0){//如果该地区，该库区下无库道，直接累加
						sitNum=0;//无库位
					}else{
						sitNum=positionMaxNum;//有库位
					}
					for(int i=0;i<count;i++){//++库位
						String sitId = SequenceManager.getSequence(null);//库位序列
						//添加方法
						TtSalesSitPO tssp=new TtSalesSitPO();
						tssp.setSitId(Long.parseLong(sitId));//库位ID
						tssp.setRoadId(Long.parseLong(roadName.length()<2?"0"+roadName:roadName));//库道ID
						tssp.setSitName(String.valueOf(sitNum+i+1).length()<2?"0"+String.valueOf(sitNum+i+1):String.valueOf(sitNum+i+1));//库道
						tssp.setStatus(Constant.STATUS_ENABLE.longValue());//状态
						tssp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
						tssp.setCreateBy(logonUser.getUserId());//创建人
						tssp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
						tssp.setUpdateBy(logonUser.getUserId());//修改人
						tssp.setVehicleId(Long.parseLong(Constant.SIT_VELID.toString()));
						reDao.reservoirSalesPositionAdd(tssp);
					}
					act.setOutData("returnValue", 1);
					
				}else if(Integer.parseInt(operatorType)==Constant.OPERATOR_TYPE_02){//减少位数
					//过滤 如果该地区该库区无库道   或者   该地区该库道小于前台传入的数字时候，无法减少位数
					if(positionMaxNum!=0 && positionMaxNum>=count){
						List<Object> params=new ArrayList<Object>();
						params.add(roadName.length()<2?"0"+roadName:roadName);//库道ID
						params.add(Constant.STATUS_ENABLE);//库位状态
						params.add(String.valueOf(positionMaxNum+1-count).length()<2?"0"+String.valueOf(positionMaxNum+1-count):String.valueOf(positionMaxNum+1-count));//开始
						params.add(String.valueOf(positionMaxNum).length()<2?"0"+positionMaxNum:positionMaxNum);//结束
						params.add(Constant.DEFAULT_VALUE);//车辆默认值
						List<Map<String,Object>> list=reDao.getSitIsHaveVehicle(params);
						if(list!=null && list.size()>0){//有车
							int info=list.size();
//							for(int i=0;i<list.size();i++){
//								info+=list.get(i).get("SIT_NAME")+",";
//							}
							act.setOutData("info", info);//提示信息
							act.setOutData("returnValue", 3);
							
						}else{
							for(int i=0;i<count;i++){//++库位
								TtSalesSitPO tspo=new TtSalesSitPO();
								tspo.setStatus(Constant.STATUS_DISABLE.longValue());//状态(无效)
								tspo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
								tspo.setUpdateBy(logonUser.getUserId());//修改人
	
								TtSalesSitPO seachPO=new TtSalesSitPO();
								//seachBean.setAreaId(Long.parseLong(areaName)); //库区ID
								seachPO.setRoadId(Long.parseLong(roadName.length()<2?"0"+roadName:roadName)); //库道ID
								seachPO.setSitName(String.valueOf(positionMaxNum-i).length()<2?"0"+String.valueOf(positionMaxNum-i):String.valueOf(positionMaxNum-i));
								reDao.reservoirSalesPositionUpdate(tspo,seachPO);
							}
							act.setOutData("returnValue", 1);
						}
					}else{
						act.setOutData("positionMaxNum", positionMaxNum);
						act.setOutData("returnValue", 2);
					}
				}
			}
			}else{//区间添加
				if(startRoad!="" && endRoad!=""){
					List<Map<String,Object>> roadList=reDao.getRoadIdByRd(startRoad,endRoad,areaName);
					if(roadList!=null && roadList.size()>0){
						String errorInfo="";
						for(int X=0;X<roadList.size();X++){
							Map<String,Object> kMap=roadList.get(X);
							//存放查询条件
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("areaName", areaName);//这个不需要
							map.put("roadName", kMap.get("ROAD_ID"));
							//TtSalesRoadPO
							//根据操作类型判断是否是添加库道或减少库道
							if(operatorType!=null && !"".equals(operatorType)){
								//根据地区，库区查询原有的库区最大库道号依次往上加传入的道数
								//获取库位最大
								int positionMaxNum=reDao.getMaxPosition(map);
								int sitNum=0;//当前地区下该库区是否有库道0为无库道
								if(Integer.parseInt(operatorType)==Constant.OPERATOR_TYPE_01){//新增道数
									if(positionMaxNum==0){//如果该地区，该库区下无库道，直接累加
										sitNum=0;//无库位
									}else{
										sitNum=positionMaxNum;//有库位
									}
									for(int i=0;i<count;i++){//++库位
										String sitId = SequenceManager.getSequence(null);//库位序列
										//添加方法
										TtSalesSitPO tssp=new TtSalesSitPO();
										tssp.setSitId(Long.parseLong(sitId));//库位ID
										tssp.setRoadId(Long.parseLong(kMap.get("ROAD_ID").toString()));//库道ID
										tssp.setSitName(String.valueOf(sitNum+i+1).length()<2?"0"+String.valueOf(sitNum+i+1):String.valueOf(sitNum+i+1));//库道
										tssp.setStatus(Constant.STATUS_ENABLE.longValue());//状态
										tssp.setCreateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//创建时间
										tssp.setCreateBy(logonUser.getUserId());//创建人
										tssp.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
										tssp.setUpdateBy(logonUser.getUserId());//修改人
										tssp.setVehicleId(Long.parseLong(Constant.SIT_VELID.toString()));
										reDao.reservoirSalesPositionAdd(tssp);
									}
								}else if(Integer.parseInt(operatorType)==Constant.OPERATOR_TYPE_02){//减少位数
									List<Object> params=new ArrayList<Object>();
									params.add(kMap.get("ROAD_ID").toString());//库道ID
									params.add(Constant.STATUS_ENABLE);//库位状态
									params.add(String.valueOf(positionMaxNum+1-count).length()<2?"0"+String.valueOf(positionMaxNum+1-count):String.valueOf(positionMaxNum+1-count));//开始
									params.add(String.valueOf(positionMaxNum).length()<2?"0"+String.valueOf(positionMaxNum):String.valueOf(positionMaxNum));//结束
									params.add(Constant.DEFAULT_VALUE);//车辆默认值
									List<Map<String,Object>> list=reDao.getSitIsHaveVehicle(params);
									if(list!=null && list.size()>0){//有车
										errorInfo+=kMap.get("ROAD_NAME").toString()+",";//有车
										continue;
									}
									int k=positionMaxNum;
									for(int i=0;i<count;i++){//++库位
										if(k==0){
											break;
										}
										TtSalesSitPO tspo=new TtSalesSitPO();
										tspo.setStatus(Constant.STATUS_DISABLE.longValue());//状态(无效)
										tspo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));//修改时间
										tspo.setUpdateBy(logonUser.getUserId());//修改人
			
										TtSalesSitPO seachPO=new TtSalesSitPO();
										//seachBean.setAreaId(Long.parseLong(areaName)); //库区ID
										seachPO.setRoadId(Long.parseLong(kMap.get("ROAD_ID").toString())); //库道ID
										seachPO.setSitName(String.valueOf(positionMaxNum-i).length()<2?"0"+String.valueOf(positionMaxNum-i):String.valueOf(positionMaxNum-i));
										reDao.reservoirSalesPositionUpdate(tspo,seachPO);
										k--;
									}
								}
							}
						}
						act.setOutData("errorInfo", errorInfo);
						act.setOutData("returnValue", 190);
					}else{
						act.setOutData("returnValue", 11);//无库道
					}
				}else{
					act.setOutData("returnValue", 10);//输入有误
				}
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 新增库位信息");
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
		List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));
			String seachY=yieldly;	
			if(yieldly==null || "".equals(yieldly)){//首次进入
				if(list_yieldly.size()>0){
					Map<String, Object> map=(Map<String, Object>)list_yieldly.get(0);//获取第一个 如有多个情况下
					seachY=map.get("AREA_ID").toString();
				}
			}
			List<Map<String, Object>> reList = reDao.getReservoirValue(seachY);//获取库区列表
			act.setOutData("reList", reList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "获取库区下拉列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 打印库位初始化
	 * @author liufazhong
	 */
	public void printReservoirPositionInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly=MaterialGroupManagerDao.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			List<Map<String, Object>> list_An = reDao.getReservoirValue(yieldly);//获取库区列表
			act.setOutData("list_An", list_An);
			act.setForword(printReservoirPositionInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"库位信息打印");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 打开打印页面
	 * @author liufazhong
	 */
	public void printReservoirPositionPage(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldlyId = request.getParamValue("yieldlyId");
			String areaId = request.getParamValue("areaId");
			String startRoad = request.getParamValue("startRoad");
			String endRoad = request.getParamValue("endRoad");
			String startSit = request.getParamValue("startSit");
			String endSit = request.getParamValue("endSit");
			List<Map<String, Object>> maps = null;
			if (!XHBUtil.IsNull(startSit) && !XHBUtil.IsNull(endSit)) {
				maps = reDao.getSitStartToEnd(yieldlyId, areaId, startRoad, endRoad, startSit, endSit);
			} else {
				maps = reDao.getSit(yieldlyId, areaId, startRoad, endRoad);
			}
			List<List<Map<String, Object>>> mapN3 = new ArrayList<List<Map<String,Object>>>();
			if (maps != null && maps.size() > 0) {
				for (int i = 0; i < maps.size(); i++) {
					int nowI = i;
					List<Map<String, Object>> mapNs = new ArrayList<Map<String,Object>>();
					mapNs.add(maps.get(i));
					if (maps.size()-1 >= i+1) {
						mapNs.add(maps.get(i+1));
						nowI = i+1;
					}
					if (maps.size()-1 >= i+2) {
						mapNs.add(maps.get(i+2));
						nowI = i+2;
					}
					mapN3.add(mapNs);
					i = nowI;
				}
			}
			act.setOutData("maps", mapN3);
			act.setOutData("maps_count", maps.size());
			act.setForword(printReservoirPositionPageUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"库位信息打印预览");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
