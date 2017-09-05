package com.infodms.dms.actions.sales.storage.storagebase;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.storage.storagebase.CityMileageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmPoseBusinessAreaPO;
import com.infodms.dms.po.TmpTtCityDisPO;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

/**
 * 城市里程数导入action
 * 
 * @author yixg 2014-5-26
 * 
 * **/
public class CityMilesManageImport extends BaseImport {
	public Logger logger = Logger.getLogger(CityMilesManageImport.class);
	/** 导入成功跳转页面 */
	private static final String CITY_MILES_IMPORT_SUCCESS_URL = "/jsp/sales/storage/storagebase/cityMileage/importCityMilesSucess.jsp";

	/*** 导入失败跳转页面 */

	private static final String CITY_MILES_IMPORT_FAIL_URL = "/jsp/sales/storage/storagebase/cityMileage/importCityMilesFailure.jsp";
	/** 导入EXECL操作 */
	private CityMileageDao dao = CityMileageDao.getInstance();
	private ActionContext act = ActionContext.getContext();

	private static Map<String, String> provicetMap = new HashMap<String, String>();
	private static Map<String, String> cityMap = new HashMap<String, String>();
	private static Map<String, String> countyMap = new HashMap<String, String>();
	private static Map<String, String> startPalceMap = new HashMap<String, String>();
	private static Map<String, String> transWays = new HashMap<String, String>();
	private static Map<String, String> vehsysMap = new HashMap<String, String>();
	public void setProvicetMap(List<Map<String, Object>> proviceList) {
		for (Map<String, Object> proMap : proviceList) {
			provicetMap.put(proMap.get("PROVINCE_NAME").toString(), proMap.get("PROVINCE_ID").toString());
		}
	}

	private List<Map<String, Object>> startList;

	//public List<Map<String, Object>> getStartList(String areaName) {
	//	//if (null == startList || startList.size() == 0) {
	//		startList = dao.getBussienssArea(areaName);
	//	//}
	//	return startList;
	//}
	public List<Map<String, Object>> getStartList(String areaName) {
		startList = dao.getTmWarehouse(areaName);
		return startList;
	}
	private void setStartPalceMap(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			String key = map.get("AREA_NAME").toString();
			String value = map.get("AREA_ID").toString();
			startPalceMap.put(key, value);
		}
	}
	public void setStartList(List<Map<String, Object>> startList) {
		this.startList = startList;
	}

	public void cityMilesExcelOperate() {
		initSet();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();

			// 清空临时表中目标的数据
			dao.update(" truncate table tmp_tt_city_dis", null);

			long maxSize = 1024 * 1024 * 5;
			insertIntoTmp(request, "uploadFile", 12, 3, maxSize);
			List<ExcelErrors> el = getErrList();

			if (null != el && el.size() > 0) {
				act.setOutData("errorList", el);
				act.setForword(CITY_MILES_IMPORT_FAIL_URL);
			} else {
				List<Map> list = getMapList();

				// 将数据插入临时表
				insertTmpCityDist(list, logonUser);
				// 校验临时表数据
				List<ExcelErrors> errorList = null;
				errorList = checkData(logonUser.getUserId().toString(),logonUser.getCompanyId().toString(),
						logonUser.getPoseId().toString());

				if (null != errorList) {
					act.setOutData("errorList", errorList);
					act.setForword(CITY_MILES_IMPORT_FAIL_URL);
				} else {
					act.setForword(CITY_MILES_IMPORT_SUCCESS_URL);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	private void initSet() {
		provicetMap.clear();
		cityMap.clear();
		countyMap.clear();
		startPalceMap.clear();
		vehsysMap.clear();
		transWays.clear();
	}

	/**
	 * 校验TMP_TT_CITY_DIS表中数据是否符合导入标准 DEALER_CODE 是否存在 GROUP_CODE 车系代码是否存在
	 * 合计是否等于12个月数量合 合计、月份数量是否是整是整数在写入临时表时校验，以异常形式处理
	 */
	private List<ExcelErrors> checkData(String userId,String companyId,String poseId) {
		TmpTtCityDisPO selectPo = new TmpTtCityDisPO();
		selectPo.setUserId(userId);
		List pos = dao.select(selectPo);
		ExcelErrors errors = null;

		StringBuffer errorInfo = new StringBuffer("");
		boolean isError = false;
		TmpTtCityDisPO po;
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		if (pos == null || pos.size() > 4000) {
			isError = true;
			errorInfo.append("导入数据量异常,每次导入数量不能超过4000条！");
			String info = errorInfo.substring(0, errorInfo.length() - 1);
			errors = new ExcelErrors();
			errors.setRowNum(1);
			errors.setErrorDesc(info);
			errorList.add(errors);
			errorInfo.delete(0, errorInfo.length());
		} else {
			String errorInfo2="";
			//校验出发仓库
			List<Map<String, Object>> slist=dao.checkStartWarehouse();
			 if(slist!=null&&slist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="出发仓库为空或不存在！";
			 	for(int i=0;i<slist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=slist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			 	
			 }else{
				 //获取临时表中出发仓库
				 List<Map<String, Object>> tslist=dao.getStartWarehouse();
				 setStartPalceMap(tslist);
			 }
			//校验目的地
			 List<Map<String, Object>> elist=dao.checkEndPlace();
			 if(elist!=null&&elist.size()>0){//获取错误行号
			 	isError = true;
			 	errorInfo2="目的省市县为空或不存在！";
			 	for(int i=0;i<elist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=elist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			 }else{
				 //获取目的省市区列表
				 List<Map<String, Object>> proviceList=dao.getDesProvince();
				 setProvicetMap(proviceList);
				 List<Map<String, Object>> cityList=dao.getDesCity();
				 setCityMap(cityList);
				 List<Map<String, Object>> countyList=dao.getDesCounty();
				 setCountyMap(countyList);
			 }
			//校验运输方式
			 List<Map<String, Object>> tlist=dao.checkTransWay();
			 if(tlist!=null&&tlist.size()>0){//获取错误行号
				 
			 	isError = true;
			 	errorInfo2="运输方式为空或不存在！";
			 	for(int i=0;i<tlist.size();i++){
			 		errors = new ExcelErrors();
			 		Map<String,Object> map=tlist.get(i);
				 	String info = errorInfo2;
					errors.setRowNum(new Integer(map.get("ROW_NUMBER").toString()));
					errors.setErrorDesc(info);
					errorList.add(errors);
			 	}
			 }
			
			for (int i = 0; i < pos.size(); i++) {
				errors = new ExcelErrors();
				// 取ddd得TmpYearlyPlanPO
				po = (TmpTtCityDisPO) pos.get(i);
				// 取得行号
				String rowNum = po.getRowNumber();
	
				try {
	
					if (!checkFormatNumber(po.getDistence())) {
						isError = true;
						errorInfo.append("里程数必须为正整数,且第一位不能为0！");
					}
					if (!checkFormatNumber(po.getReachDay())) {
						isError = true;
						errorInfo.append("到达天数必须为正整数,且第一位不能为0！");
					}
					// 检查出发地
					//checkFromPlace(errorInfo, po, isError,companyId,poseId);
					// 检查目的地
					//checkDestination(errorInfo, po, isError);
					// 检查车系
					//cheVehSys(errorInfo, po, isError);
	
					// 检查运输方式
					//checkTransWay(errorInfo, po, isError);
	
					String price = po.getPrice();
					if (null == price || "".equals(price)) {
						isError = true;
						errorInfo.append("价格不能为空！");
					} else {
						if (!isDouble(price)) {
							isError = true;
							errorInfo.append("价格必须是数字（包括小数）！");
						}
					}
	
//					String fuelNum = po.getFuelCoefficeient();
//					if (null == fuelNum || "".equals(fuelNum)) {
//						isError = true;
//						errorInfo.append("燃油调节系数不能为空！");
//					} else {
//						if (isDouble(fuelNum)) {
//							double f = Double.parseDouble(fuelNum);
//							if (f < 0 || f > 1) {
//								isError = true;
//								errorInfo.append("燃油调节系数必须在0-1之间！");
//							}
//						} else {
//							isError = true;
//							errorInfo.append("燃油调节系数必须为数字类型！");
//						}
//					}
					String handPrice=po.getHandPrice();
					if (null == handPrice || "".equals(handPrice)) {
//						isError = true;
//						errorInfo.append("手工运价不能为空！");
					} else {
						if (isFloat(handPrice)) {
							float f = Float.parseFloat(handPrice);
							//若手工运价!=单价*里程数，则备注不能为空
							if (isFloat(price)&&checkFormatNumber(po.getDistence())) {
								 BigDecimal b  = new BigDecimal(Float.parseFloat(price)*Float.parseFloat(po.getDistence()));  
								 float sum =  b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();  
								if(sum!=f){
									if(("").equals(po.getRemark())||null==po.getRemark()){
										isError = true;
										errorInfo.append("手工运价不等于（单价*里程数），需填写说明备注！");
									}
									
								}
							}
						} else {
							isError = true;
							errorInfo.append("手工运价必须为数字类型！");
						}
					}
					//验证燃油系数有效期
					String fuelBeginDate = po.getFuelBeginDate();
					String fuelEndDate = po.getFuelEndDate();
					if (XHBUtil.IsNull(fuelBeginDate) || XHBUtil.IsNull(fuelEndDate)) {
						isError = true;
						errorInfo.append("燃油系数有效期不能为空！");
					} else {
						try {
							DateUtil.str2Date(fuelBeginDate, "-");
							DateUtil.str2Date(fuelEndDate, "-");
						} catch (Exception e) {
							try {
								DateUtil.str2Date(fuelBeginDate, "/");
								DateUtil.str2Date(fuelEndDate, "/");
							} catch (Exception e2) {
								isError = true;
								errorInfo.append("燃油系数有效期格式不正确！");
							}
						}
						if (DateUtil.nDaysBetweenTwoDate(fuelBeginDate, fuelEndDate) < 0) {
							isError = true;
							errorInfo.append("燃油系数结束日期必须大于生效日期！");
						}
					}
					//验证重复
//					Long yielilId = Long.parseLong(startPalceMap.get(po.getFromPlace()));
//					Long cityId = Long.parseLong(countyMap.get(po.getDesProvice() + "|" + po.getDesCity() + "|" + po.getDesCounty()));
//					TtSalesCityDisPO tsOld = new TtSalesCityDisPO();
//					tsOld.setYieldly(yielilId);
//					tsOld.setCityId(cityId);
//					List oldList = dao.select(tsOld);
//					if (oldList != null && oldList.size() > 0) {
//						isError = true;
//						errorInfo.append(po.getDesProvice() + " " + po.getDesCity() + " " + po.getDesCounty()+" 已经设置城市里程信息");
//					}
	
				} catch (Exception e) {
					e.printStackTrace();
					isError = true;
				}
	
				if (errorInfo.length() > 0) {
					String info = errorInfo.substring(0, errorInfo.length() - 1);
					errors.setRowNum(new Integer(rowNum));
					errors.setErrorDesc(info);
					errorList.add(errors);
					errorInfo.delete(0, errorInfo.length());
				}
			}
		}

		if (errorList.size() > 0) {
			return errorList;
		} else {
			return null;
		}

	}

	/** 判断运输方式是否存在 */
	private void checkTransWay(StringBuffer errorInfo, TmpTtCityDisPO po, boolean isError) {
		String transWay = po.getTransWay();

		if (null != transWay || !"".equals(transWay)) {
			List<Map<String, Object>> transWayList = dao.getTransWays(transWay);
			if (null == transWayList || transWayList.size() == 0) {
				isError = true;
				errorInfo.append("运输方式不存在，或者运输方式填写错误!");
			} else {
				setTransWays(transWayList);
			}

		} else {
			isError = true;
			errorInfo.append("运输方式不能为空！");
		}

	}

	private void setTransWays(List<Map<String, Object>> transWayList) {
		for (Map<String, Object> proMap : transWayList) {
			String subkey1 = proMap.get("CODE_DESC").toString();
			String value = proMap.get("CODE_ID").toString();
			transWays.put(subkey1, value);
		}
	}

	private boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	private boolean isFloat(String str) {
		try {
			Float.parseFloat(str);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/** 校验车系是否存在 */
	private void cheVehSys(StringBuffer errorInfo, TmpTtCityDisPO po, boolean isError) {
		String sysName = po.getVehSys();
		if (null != sysName || !"".equals(sysName)) {
			List<Map<String, Object>> sysNameList = dao.getVehSys(sysName);
			if (null == sysNameList || sysNameList.size() == 0) {
				isError = true;
				errorInfo.append("车系不存在，或者目车系填写错误");
			} else {
				setVehSysMap(sysNameList);
			}
		} else {
			isError = true;
			errorInfo.append("车系不能为空！");
		}
	}

	private void setVehSysMap(List<Map<String, Object>> sysNameList) {
		// TODO Auto-generated method stub
		for (Map<String, Object> proMap : sysNameList) {
			String subkey1 = proMap.get("SERIES_NAME").toString();
			String value = proMap.get("SERIES_ID").toString();
			vehsysMap.put(subkey1, value);
		}

	}

	/** 校验地区是否存在 **/
	private void checkDestination(StringBuffer errorInfo, TmpTtCityDisPO po, boolean isError) {
		String proviceName = po.getDesProvice();
		boolean isProviceNameRight = false;
		boolean iscityNameRight = false;
		if (null != proviceName && !"".endsWith(proviceName)) {
			List<Map<String, Object>> proviceList = dao.getDestinatation(proviceName, null, null, "province");
			if (null == proviceList || proviceList.size() == 0) {
				isError = true;
				errorInfo.append("目的地省份不存在，或者目的地省份填写错误!");
			} else {
				isProviceNameRight = true;
				setProvicetMap(proviceList);
			}
		} else {
			isError = true;
			errorInfo.append("目的地省份不能为空！");
		}

		String cityName = po.getDesCity();
		if (isProviceNameRight) {
			if (null != cityName && !"".endsWith(cityName)) {
				List<Map<String, Object>> cityList = dao.getDestinatation(proviceName, cityName, null, "city");
				if (null == cityList || cityList.size() == 0) {
					isError = true;
					errorInfo.append("目的地级市不存在，或者目的地地级市填写错误!");
				} else {
					iscityNameRight = true;
					setCityMap(cityList);
				}
			} else {
				isError = true;
				errorInfo.append("目的地级市不能为空！");
			}
		} else {
			isError = true;
			errorInfo.append("目的省份不正确!");
		}

		String countyName = po.getDesCounty();
		if (iscityNameRight && isProviceNameRight) {
			if (null != countyName && !"".endsWith(countyName)) {
				List<Map<String, Object>> countyList = dao.getDestinatation(proviceName, cityName, countyName, "county");
				if (null == countyList || countyList.size() == 0) {
					isError = true;
					errorInfo.append("目的地区县不存在，或者目的地区县填写错误!");
				} else {
					setCountyMap(countyList);
				}
			} else {
				isError = true;
				errorInfo.append("目的地区县不能为空！");
			}
		}

	}

	private void setCountyMap(List<Map<String, Object>> countyList) {
		for (Map<String, Object> proMap : countyList) {
			String subkey1 = proMap.get("PROVINCE_NAME").toString();
			String subkey2 = proMap.get("CITY_NAME").toString();
			String subkey3 = proMap.get("COUNTY_NAME").toString();
			String value = proMap.get("COUNTY_ID").toString();
			countyMap.put(subkey1 + "|" + subkey2 + "|" + subkey3, value);
		}
	}

	private void setCityMap(List<Map<String, Object>> cityList) {
		for (Map<String, Object> proMap : cityList) {
			String subkey1 = proMap.get("PROVINCE_NAME").toString();
			String subkey2 = proMap.get("CITY_NAME").toString();
			String value = proMap.get("CITY_ID").toString();
			cityMap.put(subkey1 + "|" + subkey2, value);
		}
	}
	
	// 出发地校验
	private void checkFromPlace(StringBuffer errorInfo, TmpTtCityDisPO po, boolean isError,String companyId,String poseId) {
		String fromPlace = po.getFromPlace();
		if (null == fromPlace || "".equals(fromPlace)) {
			isError = true;
			errorInfo.append("出发仓库不能为空！");
		} else {
			/**
			 * 1、查询出发地是否维护，若未维护，根据出发地名称查询地区表市级信息，若不存在，提示错误，若存在，新增出发地信息
			 * 2、若已维护，获取出发地信息
			 */
			List<Map<String, Object>> startPlaces = getStartList(fromPlace);
//			if(null == startPlaces || startPlaces.size() == 0){
//				Map<String, Object> region = dao.queryByRegionName(fromPlace, Constant.REGION_TYPE_03);
//				if(region==null||region.isEmpty()){
//					errorInfo.append("出发仓库填写错误，或者不存在！");
//				}else{
//					//新增出发地信息
//					TmBusinessAreaPO tt=new TmBusinessAreaPO();
//					String areaId=SequenceManager.getSequence("");//region.get("REGION_ID").toString()
//					tt.setAreaId(Long.parseLong(areaId));
//					tt.setCompanyId(Long.parseLong(companyId));
//					tt.setAreaCode(region.get("REGION_CODE").toString());
//					tt.setAreaName(fromPlace);
//					tt.setStatus(Constant.STATUS_ENABLE);
//					tt.setAreaShortcode("A");
//					tt.setErpCode(82L);
//					dao.insertTmBusinessArea(tt);
//					//根据职位ID和范围ID查询职位业务范围关系，没有则新增
//					Map<String, Object> tba = dao.getTmPoseBusinessAreaByPidAid(poseId, areaId);
//					if(tba==null||tba.isEmpty()){
//						TmPoseBusinessAreaPO t2=new TmPoseBusinessAreaPO();
//						t2.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
//						t2.setPoseId(Long.parseLong(poseId));
//						t2.setAreaId(Long.parseLong(areaId));
//						t2.setCreateDate(new Date());
//						dao.insert(t2);
//					}
//					startPlaces = getStartList(fromPlace);
//				}
//			}
			if (null == startPlaces || startPlaces.size() == 0) {
				errorInfo.append("出发仓库填写错误，或者不存在！");
			} else {
				isError = true;
				setStartPalceMap(startPlaces);
			}
		}

	}

	/**
	 * 判断输入的字符必须是正整数, 而且第一位不能为0；
	 * 
	 * @param source
	 * @return 如果符合规则则返回true,否则返回false
	 */
	public static boolean checkFormatNumber(String source) {
		final String SEQUECNE_FORMAT_STR5 = "^-?[0-9]\\d*$";
		Pattern pattern = Pattern.compile(SEQUECNE_FORMAT_STR5);
		Matcher matcher = pattern.matcher(source);
		return matcher.matches();
	}

	private void insertTmpCityDist(List<Map> list, AclUserBean logonUser) {
		if (null == list) {
			list = new ArrayList();
		}
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			if (null == map) {
				map = new HashMap<String, Cell[]>();
			}
			Set<String> keys = map.keySet();
			Iterator it = keys.iterator();
			String key = "";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				parseCells(key, cells, logonUser);
			}
		}

	}

	/** 解析某一列的数据 **/
	private void parseCells(String rowNum, Cell[] cells, AclUserBean logonUser) {

		TmpTtCityDisPO po = new TmpTtCityDisPO();
		po.setRowNumber(rowNum.trim());
		try {
			po.setFromPlace(cells.length >= 1 ? subCell(cells[0].getContents().trim()) : "");

			po.setDesProvice(cells.length >= 2 ? subCell(cells[1].getContents().trim()) : "");
			po.setDesCity(cells.length >= 3 ? subCell(cells[2].getContents().trim()) : "");
			po.setDesCounty(cells.length >= 4 ? subCell(cells[3].getContents().trim()) : "");
			po.setDistence(cells.length >= 5 ? subCell(cells[4].getContents().trim()) : "");
			po.setTransWay(cells.length >= 6 ? subCell(cells[5].getContents().trim()) : "");
			po.setPrice(cells.length >= 7 ? subCell(cells[6].getContents().trim()) : "");
			po.setHandPrice(cells.length >= 8 ? subCell(cells[7].getContents().trim()) : "");
			po.setReachDay(cells.length >= 9 ? subCell(cells[8].getContents().trim()) : "");
			po.setFuelBeginDate(cells.length >= 10 ? subCell(cells[9].getContents().trim()) : "");
			po.setFuelEndDate(cells.length >= 11 ? subCell(cells[10].getContents().trim()) : "");
			po.setRemark(cells.length >= 12 ? subCell(cells[11].getContents().trim()) : "");
			po.setUserId(logonUser.getUserId().toString());
			dao.insert(po);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程数维护导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/* 将输入字符截取最多30位 */
	private String subCell(String orgAmt) throws Exception {
		String newAmt = "";
		if (null == orgAmt || "".equals(orgAmt)) {
			return newAmt;
		}
		if (orgAmt.length() > 30) {
			newAmt = orgAmt.substring(0, 30);
		} else {
			newAmt = orgAmt;
		}
		return newAmt;
	}

	public void expressResultSelect() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 10000;
			PageResult<Map<String, Object>> ps = dao.importQuery(logonUser.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			act.setOutData("temList", temList);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程维护导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	public void importRebaeSave() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//获取全部发运方式
			setTransWays(dao.getTransWays());
			long count = 0;
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 10000;
			//获取临时表中相同产地和相同省市县的数据
			List<Map<String, Object>> rlist=dao.checkRepeatRow();
			if(rlist!=null&&rlist.size()>0){
					Map<String, Object> map=rlist.get(0);
					act.setOutData("eMsg", "存在"+map.get("RENUM")+"条<出发仓库为"+map.get("FROM_PLACE")+
							",目的省市县为"+map.get("DES_PROVICE")+"|"+map.get("DES_CITY")+"|"+map.get("DES_COUNTY")+">相同的数据");
					return;
			}
			PageResult<Map<String, Object>> ps = dao.importQuery(logonUser.getUserId().toString(), pageSize, curPage);
			List<Map<String, Object>> temList = ps.getRecords();
			for (Map<String, Object> map : temList) {
				TtSalesCityDisPO po = new TtSalesCityDisPO();
				String proviceName = (String) map.get("DES_PROVICE");
				String cityName = (String) map.get("DES_CITY");
				String countyName = (String) map.get("DES_COUNTY");

				Long startId = Long.parseLong(startPalceMap.get(map.get("FROM_PLACE")));
				po.setYieldly(startId);
				Long provinceId = Long.parseLong(provicetMap.get(proviceName));
				po.setProvinceId(provinceId);
				Long countyId = Long.parseLong(countyMap.get(proviceName + "|" + cityName + "|" + countyName));
				po.setCityId(countyId);
				// 里程
				po.setDistance(Long.parseLong(map.get("DISTENCE").toString()));

				po.setTransWay(transWays.get(map.get("TRANS_WAY").toString()));
				//Long carTieId = Long.parseLong(vehsysMap.get(map.get("VEH_SYS").toString()));
				//po.setCarTieId(carTieId);

				po.setSinglePlace(Double.parseDouble(map.get("PRICE").toString()));

				//po.setFuelCoefficient(Double.parseDouble(map.get("FUEL_COEFFICEIENT").toString()));
				if(!("").equals((String)map.get("HAND_PRICE"))&&null!=map.get("HAND_PRICE")){
					po.setHandPrice(Double.parseDouble(map.get("HAND_PRICE").toString()));
				}else{
					Double handPrice=Long.parseLong(map.get("DISTENCE").toString())*Double.parseDouble(map.get("PRICE").toString());
					po.setHandPrice(handPrice);
				}
				
				po.setRemark((String)map.get("REMARK"));
				if(!("").equals((String)map.get("REACH_DAY"))&&null!=map.get("REACH_DAY")){
					po.setArriveDays(Integer.parseInt(map.get("REACH_DAY").toString()));
				}
				
				
				String fuelBeginDate = map.get("FUEL_BEGIN_DATE")+"";
				if (fuelBeginDate.length() <= 8) {
					fuelBeginDate = DateUtil.getCurrentDateTime("yyyy").substring(0, 2)+fuelBeginDate;
				}
				po.setFuelBeginDate(DateUtil.str2Date(fuelBeginDate, "-"));
				
				String fuelEndDate = map.get("FUEL_END_DATE")+"";
				if (fuelEndDate.length() <= 8) {
					fuelEndDate = DateUtil.getCurrentDateTime("yyyy").substring(0, 2)+fuelEndDate;
				}
				po.setFuelEndDate(DateUtil.str2Date(fuelEndDate, "-"));

				po.setCreateBy(logonUser.getUserId());

				po.setCreateDate(new Date());

				//艾春调整对象保存策略
				//TtSalesCityDisPO oldPo = saveOrUpdate(po);

				if (null == map.get("DIS_ID")) {
					po.setDisId(Long.parseLong(SequenceManager.getSequence(null)));
					dao.insert(po);
				} else {
					po.setDisId(Long.parseLong(map.get("DIS_ID").toString()));
					TtSalesCityDisPO oldPo = new TtSalesCityDisPO();
					oldPo.setDisId(Long.parseLong(map.get("DIS_ID").toString()));
					dao.update(oldPo, po);
				}

				count++;
			}

			act.setOutData("count", count);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程维护导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	private TtSalesCityDisPO saveOrUpdate(TtSalesCityDisPO po) {
		List<Map<String, Object>> list = dao.getTtSalesCityDisPO(po);
		if (null != list && list.size() > 0) {
			TtSalesCityDisPO returnPo = new TtSalesCityDisPO();
			BigDecimal bd = (BigDecimal) list.get(0).get("DIS_ID");
			returnPo.setDisId(bd.longValue());
			return returnPo;
		} else {
			return null;
		}
	}
}
