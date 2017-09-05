package com.infodms.dms.actions.sales.storage.storagebase;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.sales.storage.storagebase.CityMileageDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class CityMileageManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final CityMileageDao reDao = CityMileageDao.getInstance();
	
	private TcCodeDao codeDao = TcCodeDao.getInstance();
	private final String cityMileageInitUrl = "/jsp/sales/storage/storagebase/cityMileage/cityMileageList.jsp";
	private final String updateCityUrl = "/jsp/sales/storage/storagebase/cityMileage/updateCity.jsp";
	private final String queryUrl = "/jsp/sales/storage/storagebase/cityMileage/cityQuery.jsp";
	private final String importUrl = "/jsp/sales/storage/storagebase/cityMileage/importClityMiles.jsp";
	/**
	 * 
	 * @Title      : 
	 * @Description: 城市里程数维护初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void cityMileageInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			TmVhclMaterialGroupPO groupPO  = new TmVhclMaterialGroupPO();
			groupPO.setGroupLevel(2);
			List<TmVhclMaterialGroupPO> list= commonUtilDao.select(groupPO);
			act.setOutData("groupList",list);
			act.setOutData("list", list_yieldly);
			act.setForword(cityMileageInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"城市里程数初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 城市里程数维护查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void cityMileageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String countyId = CommonUtils.checkNull(request.getParamValue("COUNTY_ID")); // 区县
			String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID")); // 地市
			String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); // 省份
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			
			String type = CommonUtils.checkNull(request.getParamValue("type")); // 处理标识（1）代表导出
            
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("countyId", countyId);
			map.put("CITY_ID", cityId);
			map.put("PROVINCE_ID", provinceId);
			map.put("YIELDLY", yieldly);
			map.put("poseId", logonUser.getPoseId().toString());
//			List<Map<String,Object>> map = codeDao.getTcCodesByType(Constant.TT_TRANS_WAY+"");
//			act.setOutData("map", map);//运输方式
//			if(map != null && map.size() > 0)
//			{
//				Map<String, Object> obj = map.get(0);
//				act.setOutData("CODE_DESC", obj.get("CODE_DESC"));
//			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			if("1".equals(type)){
				PageResult<Map<String, Object>> ps = reDao.getCityMileageQuery(map, curPage,Constant.PAGE_SIZE_MAX);
				String[] headExport={"出发仓库","目的地所在省","目的地所在地级市","目的地所在区县","里程数","运输方式","单价","手工运价","到达天数","燃油系数生效时间","燃油系数失效时间","备注"};
				String[] columns={"YIELDLY","PROVINCE_NAME","CITY_NAME","COUNTY_NAME","DISTANCE","CODE_DESC","SINGLE_PLACE","HAND_PRICE","ARRIVE_DAYS","FUEL_BEGIN_DATE","FUEL_END_DATE","REMARK"};
				ToExcel.toReportExcel(act.getResponse(), request,"城市里程数.xls", headExport,columns,ps.getRecords());
			}else{
				PageResult<Map<String, Object>> ps = reDao.getCityMileageQuery(map, curPage,Constant.PAGE_SIZE_CITY);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程数查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 城市里程数维护保存
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void saveCityMileage(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String[] countyId = request.getParamValues("COUNTY_ID"); //区县
			String[] cityId = request.getParamValues("CITY_ID"); // 地市
			String[] yieldly = request.getParamValues("YIELDLY");// 产地
			String[] areaId = request.getParamValues("areaId");// 产地
			String[] provinceId = request.getParamValues("PROVINCE"); // 省份
			String[] distance = request.getParamValues("DISTANCE");// 里程数
			String[] arriveDays = request.getParamValues("ARRIVE_DAYS"); // 到达天数
			String[] startPlace = request.getParamValues("areaId");//出发地
//			String[] endPlace = request.getParamValues("END_PLACE");//目的地
//			String[] CarTieId = request.getParamValues("group_id");//车系
			String[] transWay = request.getParamValues("TRANS_WAY");//运输方式
			String[] singlePrice = request.getParamValues("SINGLE_PLACE");//单价
			//String[] FuelCoefficient = request.getParamValues("FUEL_COEFFICIENT");//燃油费调节系数
			String[] handPrice = request.getParamValues("HAND_PRICE");//手工运价
			String[] remark = request.getParamValues("REMARK");//备注
			String[] beginDate = request.getParamValues("beginDate");
			String[] endDate = request.getParamValues("endDate");
			String dis_id = request.getParamValue("dis_id");
				for(int i=0; i<cityId.length; i++){
						String cityIdStr = countyId[i].toString().equals("0")?cityId[i]:countyId[i];//等于0表示地市级，不等于0表示区县
						String yieldlyStr = yieldly[i];
						String provinceIdStr = provinceId[i];
						String distanceStr = distance[i];
						String arriveDaysStr = arriveDays[i];
						String areaIdStr = areaId[i];
						String startPlaceStr = startPlace[i];
//						String endPlaceStr = endPlace[i];
//						String CarTieIdStr = CarTieId[i];
						String transWayStr = transWay[i];
						String singlePriceStr = singlePrice[i];
						//String FuelCoefficientStr = FuelCoefficient[i]; 
						String handPriceStr=handPrice[i];
						String remarkStr=remark[i];
						String beginDateStr = beginDate[i];
						String endDateStr = endDate[i];
						
						String city = cityIdStr;
						String province = provinceIdStr;
//						Map<String, Object> countyMap = reDao.queryByReginCode(cityIdStr);
//						if (countyMap != null) {
//							city = countyMap.get("REGION_ID").toString();
//						}
//						Map<String, Object> provinceMap = reDao.queryByReginCode(provinceIdStr);
//						if (provinceMap != null) {
//							province = provinceMap.get("REGION_ID").toString();
//						}
						
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put("CITY_ID", cityIdStr);
//						map.put("PROVINCE_ID", provinceIdStr);
//						map.put("YIELDLY", yieldlyStr);
//						map.put("DISTANCE", distanceStr);
//						map.put("ARRIVE_DAYS", arriveDaysStr);
//						map.put("START_PLACE", startPlaceStr);
//						map.put("END_PLACE", endPlaceStr);
//						map.put("CAR_TIE_ID", CarTieIdStr);
//						map.put("TRANS_WAY", transWayStr);
//						map.put("SINGLE_PLACE", singlePriceStr);
//						map.put("FUEL_COEFFICIENT", FuelCoefficientStr);
//						map.put("DIS_ID", disId);
//						map.put("userId", logonUser.getUserId().toString());
//						map.put("AREA_ID", areaIdStr);
						TtSalesCityDisPO tt = new TtSalesCityDisPO();
						tt.setYieldly(Long.valueOf(yieldlyStr));
						tt.setProvinceId(Long.valueOf(province));
						tt.setCityId(Long.valueOf(city));
						tt.setDistance(Long.valueOf(distanceStr));
						tt.setArriveDays(Integer.valueOf(arriveDaysStr));
						tt.setStartPlace(startPlaceStr);
//						tt.setEndPlace(endPlaceStr);
//						tt.setCarTieId(Long.valueOf(CarTieIdStr));
						tt.setTransWay(transWayStr);
						tt.setSinglePlace(Double.valueOf(singlePriceStr));
						//tt.setFuelCoefficient(Double.valueOf(FuelCoefficientStr));
						if(!"".equals(handPriceStr)&&null!=handPriceStr){
							tt.setHandPrice(Double.parseDouble(handPriceStr));
						}else{
							tt.setHandPrice(Double.valueOf(singlePriceStr)*Long.valueOf(distanceStr));
						}
						tt.setRemark(remarkStr);
						tt.setUpdateDate(new Date());
						tt.setUpdateBy(logonUser.getUserId());
						tt.setFuelBeginDate(DateUtil.str2Date(beginDateStr, "-"));
						tt.setFuelEndDate(DateUtil.str2Date(endDateStr, "-"));
						if (!XHBUtil.IsNull(dis_id)) {
							//修改
							//判断是否已经存在该城市里程
							List<Map<String, Object>> isHasList = reDao.validUpdateCityDis(yieldlyStr, city, dis_id);//(yieldlyStr, city, CarTieIdStr, dis_id);
							if (isHasList != null && isHasList.size() > 0) {
								throw new BizException("该城市里程数据已经存在");
							}
							
							TtSalesCityDisPO ttOld = new TtSalesCityDisPO();
							ttOld.setDisId(Long.valueOf(dis_id));
							tt.setDisId(Long.valueOf(dis_id));
							reDao.updateInfo(ttOld, tt);
						}else {
							//新增
							//判断是否已经存在该城市里程
							TtSalesCityDisPO ttOld = new TtSalesCityDisPO();
							ttOld.setCityId(Long.valueOf(city));
							ttOld.setYieldly(Long.valueOf(yieldlyStr));
							ttOld.setTransWay(transWayStr);
							//ttOld.setCarTieId(Long.parseLong(CarTieIdStr));
							List oldList = reDao.select(ttOld);
							if (oldList != null && oldList.size() > 0) {
								throw new BizException("该城市里程数据已经存在");
							}
							
							String disId = SequenceManager.getSequence(null);//城市里程数ID
							tt.setDisId(Long.valueOf(disId));
							tt.setCreateBy(logonUser.getUserId());
							tt.setCreateDate(new Date());
							reDao.insertInfo(tt);
						}
				}
				act.setOutData("message", "操作成功");
			
		} catch (BizException e) {
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "城市里程数保存信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void updateCity()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//产地
			CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
			//TmVhclMaterialGroupPO groupPO  = new TmVhclMaterialGroupPO();
			//groupPO.setGroupLevel(2);
			//List<TmVhclMaterialGroupPO> list= commonUtilDao.select(groupPO);
			//act.setOutData("groupList",list);//车系
			List<Map<String,Object>> map = codeDao.getTcCodesByType(Constant.TT_TRANS_WAY+"");
			act.setOutData("map", map);//运输方式
			if(map != null && map.size() > 0)
			{
				Map<String, Object> obj = map.get(0);
				
				act.setOutData("TRANS_WAY", obj.get("CODEDESC"));
			}
			
			String disId = request.getParamValue("dis_id");
			if (!XHBUtil.IsNull(disId)) {
				//修改
				CityMileageDao reDao = new CityMileageDao();
				List<Map<String, Object>> mapList = reDao.getCityMileageById(disId, logonUser.getPoseId().toString());
				if (mapList != null && mapList.size() > 0) {
					Map<String, Object> obj = mapList.get(0);
					act.setOutData("DIS_ID", obj.get("DIS_ID"));
					act.setOutData("AREA_ID", obj.get("AREA_ID") );
					act.setOutData("AREA_NAME", obj.get("AREA_NAME"));
					act.setOutData("REGION_CODE", obj.get("REGION_CODE"));
					act.setOutData("CITY_CODE", obj.get("CITY_CODE"));
					act.setOutData("COUNTY_CODE", obj.get("COUNTY_CODE"));
					act.setOutData("DISTANCE", obj.get("DISTANCE"));
					act.setOutData("ARRIVE_DAYS", obj.get("ARRIVE_DAYS"));
//					act.setOutData("CAR_TIE_ID", obj.get("CAR_TIE_ID"));
//					act.setOutData("END_PLACE", obj.get("END_PLACE"));
					act.setOutData("START_PLACE", obj.get("START_PLACE"));
					act.setOutData("TRANS_WAY", obj.get("TRANS_WAY"));
					act.setOutData("SINGLE_PLACE", obj.get("SINGLE_PLACE"));
					//act.setOutData("FUEL_COEFFICIENT", obj.get("FUEL_COEFFICIENT"));
					act.setOutData("HAND_PRICE", obj.get("HAND_PRICE"));
					act.setOutData("REMARK", obj.get("REMARK"));
					act.setOutData("beginDate", obj.get("FUEL_BEGIN_DATE"));
					act.setOutData("endDate", obj.get("FUEL_END_DATE"));
				}
			}else {
				//新增
				act.setForword(updateCityUrl);
			}
			act.setForword(updateCityUrl);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程数修改信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	/**
	 * 查询城市里程信息（订单提报用）
	 */
	public void queryCityMileageForOrderConvert(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));// 产地
			String countyCode = CommonUtils.checkNull(request.getParamValue("countyCode")); // 省份
			List<Map<String, Object>> maps = reDao.getCityMileageForOrderConvert(yieldly, countyCode);
			if (maps != null && maps.size() > 0) {
				act.setOutData("cityMileage", maps.get(0));				
			} else {
				act.setOutData("cityMileage", null);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程数查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryCity()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {	
			act.setForword(queryUrl);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程数修改信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	/**下载城市历程维护模板**/
	public void downloadTemplate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			String[] titles = {"出发仓库*","目的地所在省*","目的地所在地级市*","目的地所在区县*","里程数*","运输方式*","单价*","手工运价","到达天数","燃油系数生效时间*","燃油系数失效时间*","备注"};
			for(String title : titles){
				listHead.add(title);
			}
			list.add(listHead);
			
			// 导出的文件名
			String fileName = "城市里程维护导入模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			
			
			CsvWriterUtil.createXlsFile(list, os);
			
			
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
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
	
	public void openImportCityMile(){

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(importUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"城市里程数初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
}
