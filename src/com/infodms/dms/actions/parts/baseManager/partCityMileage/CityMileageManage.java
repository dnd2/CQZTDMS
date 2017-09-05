package com.infodms.dms.actions.parts.baseManager.partCityMileage;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.parts.baseManager.partCityMileage.CityMileageDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmpPartCityDisPO;
import com.infodms.dms.po.TtPartCityDisPO;
import com.infodms.dms.po.TtPartFixcodeDefinePO;
import com.infodms.dms.po.TtPartLogisticsPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CityMileageManage extends BaseImport  {
    public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    private final CityMileageDao reDao = CityMileageDao.getInstance();
    private final String cityMileageInitUrl = "/jsp/parts/baseManager/partCityMileage/cityMileageList.jsp";
    private final String addLogisticsUrl = "/jsp/parts/baseManager/partCityMileage/addLogistics.jsp";
    private final String INPUT_ERROR_URL = "/jsp/parts/baseManager/partCityMileage/inputError.jsp";

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title :
     * @Description: 城市里程数维护初始化
     */
    public void cityMileageInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<TtPartFixcodeDefinePO> listf = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_06);// 发运类型
            List<TtPartFixcodeDefinePO> listc = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
            act.setOutData("listf", listf);
            act.setOutData("listc", listc);
            act.setForword(cityMileageInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
                    "城市里程数初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title :
     * @Description: 城市里程数维护查询信息
     */
    public void cityMileageQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = reDao.getCityMileageQuery(request, curPage, Constant.PAGE_SIZE_CITY);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程数查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title :
     * @Description: 城市里程数维护保存
     */
    public void saveCityMileage() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String[] countyId = request.getParamValues("countyId"); //区县
            String[] cityId = request.getParamValues("cityId"); // 地市
            String[] yieldly = request.getParamValues("yieldly");// 产地
            String[] areaId = request.getParamValues("areaId");// 产地
            String[] provinceId = request.getParamValues("provinceId"); // 省份
            String[] distance = request.getParamValues("DISTANCE");// 里程数
            String[] arriveDays = request.getParamValues("ARRIVE_DAYS"); // 到达天数
            if (cityId != null) {
                for (int i = 0; i < cityId.length; i++) {
                    //if(distance[i]!=null || arriveDays[i]!=null){//不需要过滤
                    String disId = SequenceManager.getSequence(null);//城市里程数ID
                    String cityIdStr = countyId[i].toString().equals("0") ? cityId[i] : countyId[i];//等于0表示地市级，不等于0表示区县
                    String yieldlyStr = yieldly[i];
                    String provinceIdStr = provinceId[i];
                    String distanceStr = distance[i];
                    String arriveDaysStr = arriveDays[i];
                    String areaIdStr = areaId[i];
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("CITY_ID", cityIdStr);
                    map.put("PROVINCE_ID", provinceIdStr);
                    map.put("YIELDLY", yieldlyStr);
                    map.put("DISTANCE", distanceStr);
                    map.put("ARRIVE_DAYS", arriveDaysStr);
                    map.put("DIS_ID", disId);
                    map.put("userId", logonUser.getUserId().toString());
                    map.put("AREA_ID", areaIdStr);
                    reDao.saveCityMileage(map);
                    //}
                }

                act.setOutData("returnValue", 1);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "城市里程数保存信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addLogisticsInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> list_yieldly = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            act.setOutData("list", list_yieldly);
            List<TtPartFixcodeDefinePO> listwl = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
            act.setOutData("listwl", listwl);
            List<TtPartFixcodeDefinePO> listf = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_06);// 发运类型
            act.setOutData("listf", listf);
            act.setForword(addLogisticsUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物流商管理新增初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addLogisticsCityQuery() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
            String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); // 省份
            String disIds = CommonUtils.checkNull(request.getParamValue("cityIds")); //
            String disIds_o = CommonUtils.checkNull(request.getParamValue("disIds_o")); // 显示页面传回来的里程IDS
            String isShow = CommonUtils.checkNull(request.getParamValue("isShow")); // 是否是显示页面返回

            String provinceSeach = CommonUtils.checkNull(request.getParamValue("PROVINCE_SEACH"));// 省
            String citySeach = CommonUtils.checkNull(request.getParamValue("CITY_SEACH")); // 市
            String countySeach = CommonUtils.checkNull(request.getParamValue("COUNTY_SEACH"));// 地区
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("YIELDLY", yieldly);
            map.put("PROVINCE", provinceId);
            map.put("disIds", disIds);//里程IDS
            map.put("disIds_o", disIds_o);//显示页面传回来的里程IDS
            map.put("isShow", isShow);//是否是显示页面返回
            map.put("provinceSeach", provinceSeach);
            map.put("citySeach", citySeach);
            map.put("countySeach", countySeach);
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = reDao.addCityQuery(map, curPage, Constant.PAGE_SIZE);
            String cityIds = "";
            if (ps.getRecords() != null) {
                for (Map<String, Object> datasMap : ps.getRecords()) {
                    cityIds += datasMap.get("CITY_ID").toString() + ",";
                }

                cityIds = cityIds.substring(0, cityIds.length() - 1);
            }
            for (Map<String, Object> datasMap : ps.getRecords()) {
                datasMap.put("cityIds", cityIds);

            }
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "里程信息列表查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addLogistics() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //***start***保存物流商主表信息
            String logiId = CommonUtils.checkNull(request.getParamValue("LOGI_ID")); //物流商ID
            String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流商简称
            String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE")); //发运方式
            String weightRatio = request.getParamValue("WEIGHT_RATIO"); //系数
            String miniWeight = request.getParamValue("MINI_WEIGHT"); //最小发运重量
            String remark = request.getParamValue("REMARK"); //备注

            TtPartFixcodeDefinePO fixcodeDefinePO = new TtPartFixcodeDefinePO();
            fixcodeDefinePO.setFixGouptype(Constant.FIXCODE_TYPE_08);
            fixcodeDefinePO.setFixValue(logiId);
            fixcodeDefinePO = (TtPartFixcodeDefinePO) reDao.select(fixcodeDefinePO).get(0);
            TtPartLogisticsPO tslp = new TtPartLogisticsPO();//物流商管理表
            tslp.setLogiId(Long.parseLong(logiId));
            tslp.setLogiName(fixcodeDefinePO.getFixName());
            tslp.setLogiFullName(fixcodeDefinePO.getFixName());
            tslp.setStatus(1l);
            tslp.setRemark(remark);
            tslp.setCreateBy(logonUser.getUserId());//创建人
            tslp.setCreateDate(new Date());//创建时间
            tslp.setWeightRatio(Double.valueOf(weightRatio));
            tslp.setMiniWeight(Double.valueOf(miniWeight));

            String cityIds = request.getParamValue("cityIds"); //城市IDS
            if (null != cityIds && !"".equals(cityIds)) {
                TtPartLogisticsPO tslpPO = new TtPartLogisticsPO();
                tslpPO.setLogiId(Long.parseLong(logiId));
                List<PO> disList = LogisticsDao.getInstance().select(tslpPO);//查看物流商在物流商主表中是否存在
                if (disList == null || disList.size() <= 0) {
                    LogisticsDao.getInstance().insert(tslp);
                } else {
                    TtPartLogisticsPO logisticsPO = new TtPartLogisticsPO();
                    logisticsPO.setLogiId(Long.parseLong(logiId));
                    logisticsPO.setLogiName(fixcodeDefinePO.getFixName());
                    logisticsPO.setLogiFullName(fixcodeDefinePO.getFixName());
                    logisticsPO.setRemark(remark);
                    logisticsPO.setStatus(1l);
                    logisticsPO.setUpdateBy(logonUser.getUserId());//创建人
                    logisticsPO.setUpdateDate(new Date());//创建时间
                    logisticsPO.setWeightRatio(Double.valueOf(weightRatio));
                    logisticsPO.setMiniWeight(Double.valueOf(miniWeight));
                    LogisticsDao.getInstance().update(tslpPO, logisticsPO);
                }
                String[] array = cityIds.split(",");
                for (int i = 0; i < array.length; i++) {
                    if (null != array[i] && !"".equals(array[i])) {
                        String transCycle = CommonUtils.checkNull(request.getParamValue("TRANS_CYCLE_" + array[i]));
                        String firstWeight = CommonUtils.checkNull(request.getParamValue("FIRST_WEIGHT_" + array[i]));
                        String addWeight = CommonUtils.checkNull(request.getParamValue("ADDITIONAL_WEIGHT_" + array[i]));
                        TtPartCityDisPO tslap = new TtPartCityDisPO();
                        if (transCycle != "" && firstWeight != "" && addWeight != "") {
                            if (!CityMileageDao.getInstance().isRepeat(logiId, array[i])) {
                                String cityDisId = SequenceManager.getSequence("");
                                tslap.setDisId(Long.parseLong(cityDisId));
                                tslap.setLogiId(Long.parseLong(logiId));//物流商ID
                                tslap.setEndPlaceId(Long.parseLong(array[i]));
                                tslap.setTransType(transType);
                                tslap.setArriveDays(Integer.parseInt(transCycle));
                                tslap.setMiniWeight(Double.parseDouble(miniWeight));
                                tslap.setFirstWeight(Double.parseDouble(firstWeight));
                                tslap.setAdditionalWeight(Double.parseDouble(addWeight));
                                tslap.setCreateBy(logonUser.getUserId());//创建人
                                tslap.setCreateDate(new Date());//创建时间
                                LogisticsDao.getInstance().insert(tslap);
                            } else {
                                TtPartCityDisPO disPO = new TtPartCityDisPO();
                                tslap.setEndPlaceId(Long.parseLong(array[i]));
                                disPO.setLogiId(Long.parseLong(logiId));//物流商ID
                                disPO.setEndPlaceId(Long.parseLong(array[i]));
                                disPO.setTransType(transType);
                                disPO.setArriveDays(Integer.parseInt(transCycle));
                                disPO.setMiniWeight(Double.parseDouble(miniWeight));
                                disPO.setFirstWeight(Double.parseDouble(firstWeight));
                                disPO.setAdditionalWeight(Double.parseDouble(addWeight));
                                disPO.setUpdateBy(logonUser.getUserId());
                                disPO.setUpdateDate(new Date());
                                LogisticsDao.getInstance().update(tslap, disPO);
                            }
                        }
                    }
                }
                act.setOutData("returnValue", 1);
            }/**/
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, " 新增物流商信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void locImpUpload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
		try {
			
			String exportSel = request.getParamValue("export_sel");
			
			String parentOrgId = "";//父机构（销售单位）ID
			//判断主机厂与服务商
			String comp = logonUser.getOemCompanyId();
			if (null == comp ){
				
				parentOrgId = Constant.OEM_ACTIVITIES;
			}else {
				parentOrgId = logonUser.getDealerId();
			}
			List<Map<String,String>> errorInfo = null;
			String err="";
			
			errorInfo =  new ArrayList<Map<String,String>>();
			long maxSize=1024*1024*5;
			int errNum = 0;
			if(exportSel.equals("1")){
				errNum = insertIntoTmp(request, "uploadFile1",6,3,maxSize);
			}else{
				errNum = insertIntoTmp(request, "uploadFile2",4,3,maxSize);
			}
			if(errNum!=0){
				switch (errNum) {
				case 1:
					err+="文件列数过多!";
					break;
				case 2:
					err+="空行不能大于三行!";
					break;
				case 3:
					err+="文件不能为空!";
					break;
				case 4:
					err+="文件不能为空!";
					break;
				case 5:
					err+="文件不能大于"+maxSize+"!";
					break;
				default:
					break;
				}
			}
			
			if(!"".equals(err)){
				act.setOutData("error", err);
				act.setForword(INPUT_ERROR_URL);
			}else{
				List<Map> list= getMapList();//tt_part_city_dis
				List<Map<String,String>> voList = new ArrayList<Map<String,String>>();
				loadVoList(voList,list, errorInfo, exportSel);
				if(errorInfo.size()>0){
					act.setOutData("errorInfo", errorInfo);
					act.setForword(INPUT_ERROR_URL);
				}else{
					//保存
					savePkgStk(voList, exportSel);
				}
				
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = null;
			if(e instanceof BizException){
				e1 = (BizException)e;
			}else{
				new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			}
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
    
    /**
	 * 
	 * @Title      : 读取CELL
	 * @param      : @param voList
	 * @param      : @param list
	 * @param      : @param errorInfo      
	 * @return     :    
	 */
	private void loadVoList(List<Map<String,String>> voList,List<Map> list,List<Map<String,String>> errorInfo, String exportSel){
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys = map.keySet();
			Iterator it = keys.iterator();
			String key="";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				String partIdTmp = "";
				if(exportSel.equals("1")){
					String logiId = null;
					String transType = null;
					String proviceId = null;
//					String arriveDays = null;
					String firstWeight = null;
					String additionalWeight = null;
					String remark = null;
	//				String cityId = null;
					
					if(cells[3].getContents().equals("\\") || cells[4].getContents().equals("\\")){
						continue;
					}else{
					
						if ("".equals(cells[0].getContents().trim())) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "承运物流");
							errormap.put("3", "为空!");
							errorInfo.add(errormap);
						} else {
							List<Map<String, Object>> fixCheck = reDao.checkFixName(cells[0].getContents().trim().toUpperCase(), "92251008");
							if (null != fixCheck && fixCheck.size() == 1) {
								logiId = fixCheck.get(0).get("FIX_VALUE").toString();
		//						logiId = cells[0].getContents();
							} else {
								Map<String, String> errormap = new HashMap<String, String>();
								errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
								errormap.put("2", "承运物流【" + cells[0].getContents().trim()+"】");
								errormap.put("3", "不存在!");
								errorInfo.add(errormap);
							}
						}
						
						if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "发运方式");
							errormap.put("3", "为空!");
							errorInfo.add(errormap);
						} else {
							List<Map<String, Object>> fixCheck = reDao.checkFixName(cells[1].getContents().trim().toUpperCase(),"92251006");
							if (null != fixCheck && fixCheck.size() == 1) {
								transType = fixCheck.get(0).get("FIX_VALUE").toString();
		//						transType = cells[1].getContents();
							} else {
								Map<String, String> errormap = new HashMap<String, String>();
								errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
								errormap.put("2", "发运方式【" + cells[1].getContents().trim()+"】");
								errormap.put("3", "不存在!");
								errorInfo.add(errormap);
							}
						}
		
						if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "省份");
							errormap.put("3", "为空!");
							errorInfo.add(errormap);
						} else {
							List<Map<String, Object>> proviceCheck = reDao.checkProvice(cells[2].getContents().trim().toUpperCase());
							if (null != proviceCheck && proviceCheck.size() == 1) {
								proviceId = proviceCheck.get(0).get("PROVICE_ID").toString();
		//						proviceId = cells[2].getContents();
							} else {
								Map<String, String> errormap = new HashMap<String, String>();
								errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
								errormap.put("2", "省份【" + cells[2].getContents().trim()+"】");
								errormap.put("3", "不存在!");
								errorInfo.add(errormap);
							}
						}
						
						
						
//						if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
//							Map<String, String> errormap = new HashMap<String, String>();
//							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//							errormap.put("2", "约定路运时效");
//							errormap.put("3", "为空!");
//							errorInfo.add(errormap);
//							arriveDays = "0";
//						} else {
//							String days = cells[3].getContents().trim();
//							String regex = "(^([0])|([1-9]+(\\d)*)$)";
//							Pattern pattern = Pattern.compile(regex);
//							Matcher matcher = pattern.matcher(days);
//							
//							if(matcher.find())
//							{
//								arriveDays = days;
//							}
//							else
//							{
//								Map<String, String> errormap = new HashMap<String, String>();
//								errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//								errormap.put("2", "约定路运时效");
//								errormap.put("3", "不合法!");
//								errorInfo.add(errormap);
//							}
//						}
						
						if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "计费首重");
							errormap.put("3", "为空!");
							errorInfo.add(errormap);
						} else {
							String weight = cells[3].getContents().trim();
							String regex = "(^([0])|([1-9]+(\\d)*)$)";
							Pattern pattern = Pattern.compile(regex);
							Matcher matcher = pattern.matcher(weight);
							
							if(matcher.find())
							{
								firstWeight = weight;
							}
							else
							{
								Map<String, String> errormap = new HashMap<String, String>();
								errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
								errormap.put("2", "计费首重");
								errormap.put("3", "不合法!");
								errorInfo.add(errormap);
							}
						}
						
						if (cells.length < 5 || CommonUtils.isEmpty(cells[4].getContents())) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "计费续重");
							errormap.put("3", "为空!");
							errorInfo.add(errormap);
						} else {
							String addWeight = cells[4].getContents().trim();
							String regex = "(^([0])|([1-9]+(\\d)*)$)";
							Pattern pattern = Pattern.compile(regex);
							Matcher matcher = pattern.matcher(addWeight);
							
							if(matcher.find())
							{
								additionalWeight = addWeight;
							}
							else
							{
								Map<String, String> errormap = new HashMap<String, String>();
								errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
								errormap.put("2", "计费续重");
								errormap.put("3", "不合法!");
								errorInfo.add(errormap);
							}
						}
						if (cells.length < 6 || CommonUtils.isEmpty(cells[5].getContents())) {
							
							remark = null;
						} else {
							remark = cells[5].getContents().trim();
						}
						
	//					if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
	//						citys = reDao.ctiyQuery(proviceId);
	//					} else {
	//						List<Map<String, Object>> ctiyCheck = reDao.checkCtiy(cells[3].getContents().trim().toUpperCase());
	//						if (null != ctiyCheck && ctiyCheck.size() == 1) {
	//							cityId = ctiyCheck.get(0).get("CITY_ID").toString();
	//							citys = ctiyCheck;
	//						} else {
	//							Map<String, String> errormap = new HashMap<String, String>();
	//							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
	//							errormap.put("2", "城市【" + cells[3].getContents().trim()+"】");
	//							errormap.put("3", "不存在!");
	//							errorInfo.add(errormap);
	//						}
	//					}
						
						Map<String, String> tempmap = new HashMap<String, String>();
						tempmap.put("logiId", logiId);
						tempmap.put("transType", transType);
						tempmap.put("proviceId", proviceId);
	//					tempmap.put("cityId", citys.get(c).get("CITY_ID").toString());
//						tempmap.put("arriveDays", arriveDays);
						tempmap.put("firstWeight", firstWeight);
						tempmap.put("additionalWeight", additionalWeight);
						tempmap.put("remark", remark);
	
						voList.add(tempmap);						
					}				
				}else{
					
					String proviceId = null;
					List<Map<String, Object>> cityIds = new ArrayList<Map<String, Object>>();
					String arriveDays = null;
					String transType = null;
					
					if ("".equals(cells[0].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "省份");
						errormap.put("3", "为空!");
						errorInfo.add(errormap);
					} else {
						List<Map<String, Object>> proviceCheck = reDao.checkProvice(cells[0].getContents().trim().toUpperCase());
						if (null != proviceCheck && proviceCheck.size() == 1) {
							proviceId = proviceCheck.get(0).get("PROVICE_ID").toString();
						} else {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "省份【" + cells[2].getContents().trim()+"】");
							errormap.put("3", "不存在!");
							errorInfo.add(errormap);
						}
					}
					
					if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
						//citys = reDao.ctiyQuery(proviceId);
					} else {
						List<Map<String, Object>> ctiyCheck = reDao.checkCtiy(cells[1].getContents().trim().toUpperCase(), proviceId);
						if (null != ctiyCheck && ctiyCheck.size() >= 1) {
							//String cityId = ctiyCheck.get(0).get("CITY_ID").toString();
							cityIds = ctiyCheck;
						} else {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "城市【" + cells[1].getContents().trim()+"】");
							errormap.put("3", "不存在!");
							errorInfo.add(errormap);
						}
					}
					

					if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "发运方式");
						errormap.put("3", "为空!");
						errorInfo.add(errormap);
					} else {
						List<Map<String, Object>> fixCheck = reDao.checkFixName(cells[2].getContents().trim().toUpperCase(),"92251006");
						if (null != fixCheck && fixCheck.size() == 1) {
							transType = fixCheck.get(0).get("FIX_VALUE").toString();
	//						transType = cells[1].getContents();
						} else {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "发运方式【" + cells[2].getContents().trim()+"】");
							errormap.put("3", "不存在!");
							errorInfo.add(errormap);
						}
					}
					
					if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
//						Map<String, String> errormap = new HashMap<String, String>();
//						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//						errormap.put("2", "约定路运时效");
//						errormap.put("3", "为空!");
//						errorInfo.add(errormap);
						arriveDays = "0";
					} else {
						String days = cells[3].getContents().trim();
						String regex = "(^([0])|([1-9]+(\\d)*)$)";
						Pattern pattern = Pattern.compile(regex);
						Matcher matcher = pattern.matcher(days);
						
						if(matcher.find())
						{
							arriveDays = days;
						}
						else
						{
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "约定路运时效");
							errormap.put("3", "不合法!");
							errorInfo.add(errormap);
						}
					}
					if(cityIds.size() > 0){
						for(int c = 0; c < cityIds.size(); c++){
							Map<String, String> tempmap = new HashMap<String, String>();
							tempmap.put("transType", transType);
							tempmap.put("proviceId", proviceId);
							tempmap.put("cityId", cityIds.get(c).get("CITY_ID").toString());
							tempmap.put("arriveDays", arriveDays);
		
							voList.add(tempmap);
						}
					}else{
						Map<String, String> tempmap = new HashMap<String, String>();
						tempmap.put("transType", transType);
						tempmap.put("proviceId", proviceId);
						tempmap.put("cityId", null);
						tempmap.put("arriveDays", arriveDays);
	
						voList.add(tempmap);
					}
					
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title      : 批量更新配件包装储运
	 * @param      : @param relList      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-21
	 */
	public void savePkgStk(List<Map<String,String>> relList, String exportSel){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			if(null != relList && relList.size() > 0)
			{
				
				int listSize = relList.size();
				long disId = Long.parseLong(SequenceManager.getSequence(""));
				if(exportSel.equals("1")){
					for(int i = 0; i < listSize; i ++ )
					{
						TmpPartCityDisPO tmpCityMoneyPo = new TmpPartCityDisPO();
						Long logiId = Long.parseLong(relList.get(i).get("logiId")); //承运商ID
						String transType = relList.get(i).get("transType");//发运方式ID
						Long proviceId = Long.parseLong(relList.get(i).get("proviceId"));//省份
//						Long cityid = Long.parseLong(relList.get(i).get("cityId"));//城市
//						int arriveDays = Integer.parseInt(relList.get(i).get("arriveDays"));//约定陆运时效
						double firstWeight = Double.parseDouble(relList.get(i).get("firstWeight"));//计费首重
						double additionalWeight = Double.parseDouble(relList.get(i).get("additionalWeight"));//计费续重
						
						tmpCityMoneyPo.setDisId(disId);//单号
						tmpCityMoneyPo.setLogiId(logiId);
						tmpCityMoneyPo.setTransType(transType);
						tmpCityMoneyPo.setStartPlaceId(proviceId);
						
						List<PO> check = reDao.select(tmpCityMoneyPo);
						
						if(check.size() > 0){
							continue;
						}
						
//						tmpCityMoneyPo.setArriveDays(arriveDays);
						tmpCityMoneyPo.setFirstWeight(firstWeight);
						tmpCityMoneyPo.setAdditionalWeight(additionalWeight);
						tmpCityMoneyPo.setMiniWeight(1d);
						tmpCityMoneyPo.setCreateDate(new Date());
						tmpCityMoneyPo.setUpdateBy(logonUser.getUserId());
						
						reDao.insert(tmpCityMoneyPo);
					}
				}else{
					for(int i = 0; i < listSize; i ++ ){
						TmpPartCityDisPO tmpCityDaysPo = new TmpPartCityDisPO();
						
						String transType = relList.get(i).get("transType");//发运方式ID
						Long proviceId = Long.parseLong(relList.get(i).get("proviceId"));//省份
						Long endPlaceId = Long.parseLong(relList.get(i).get("cityId"));//城市
						int arriveDays = Integer.parseInt(relList.get(i).get("arriveDays"));//约定陆运时效
						
						tmpCityDaysPo.setTransType(transType);
						tmpCityDaysPo.setStartPlaceId(proviceId);
						tmpCityDaysPo.setEndPlaceId(endPlaceId);//改为城市
						tmpCityDaysPo.setDisId(disId);//单号
						
						List<PO> check = reDao.select(tmpCityDaysPo);
						
						if(check.size() > 0){
							continue;
						}
						
						tmpCityDaysPo.setArriveDays(arriveDays);
						tmpCityDaysPo.setCreateDate(new Date());
						tmpCityDaysPo.setUpdateBy(logonUser.getUserId());
						
						reDao.insert(tmpCityDaysPo);
					}
				}
				reDao.insertCtiyMileage(disId,exportSel);
			}
			
			cityMileageInit();
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"批量更新配件包装储运失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 导出EXECEL模板
     * @Description: TODO
     */
    public void exportExcelTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        ResponseWrapper response = act.getResponse();
        RequestWrapper request = act.getRequest();
        OutputStream os = null;
        try {
        	String fileName = null;
        	List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
        	if(request.getParamValue("export_sel").toString().equals("1")){            
	            //标题
	            listHead.add("承运物流");
	            listHead.add("发运方式");
	            listHead.add("省份");
	//            listHead.add("城市");
//	            listHead.add("约定路运时效(天)");
	            listHead.add("计费首重(元/1kg)");
	            listHead.add("计费续重(元/kg)");
	            listHead.add("备注");
	            list.add(listHead);
	            // 导出的文件名
	            fileName = "物流计费模板.xls";
        	}else{
        		//标题
	            
	            listHead.add("省份");
	            listHead.add("城市");
	            listHead.add("发运方式");
	            listHead.add("约定路运时效(天)");
	            list.add(listHead);
	            // 导出的文件名
	            fileName = "物流时效模板.xls";
        	}
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            logger.error(logonUser, e1);
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
    
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
            } else {
                parentOrgId = logonUser.getDealerId();
            }
            String countyId = CommonUtils.checkNull(request.getParamValue("COUNTY_ID")); // 区县
            String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID")); // 地市
            String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); // 省份
            String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
            //

            Map<String, Object> mapData = new HashMap<String, Object>();
            mapData.put("countyId", countyId);
            mapData.put("CITY_ID", cityId);
            mapData.put("PROVINCE_ID", provinceId);
            mapData.put("YIELDLY", yieldly);
            mapData.put("poseId", logonUser.getPoseId().toString());

            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "物流商";
            head[2] = "运输方式";
            head[3] = "运输目的省份";
            head[4] = "运输目的城市";
            head[5] = "约定时效（天）";
            head[6] = "计费首重(元/1kg)";
            head[7] = "计费续重(元/kg)";
            head[8] = "最小发运重量(kg)";
            
            List<Map<String, Object>> list = reDao.getCityMileageExport(mapData);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        
                        detail[1] = CommonUtils.checkNull(map
                                .get("TRANS_ORG"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("TRANS_TYPE"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("PROVICE_NAME"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("CITY_NAME"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("ARRIVE_DAYS"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("FIRST_WEIGHT"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("ADDITIONAL_WEIGHT"));
                        detail[8] = CommonUtils.checkNull(map
                                .get("MINI_WEIGHT"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "城市里程数维护";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出城市里程数维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 文件导出为xls文件
     */
    public static Object exportEx(String fileName, ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = fileName + ".xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode(name, "utf-8"));
            out = response.getOutputStream();
            wwb = Workbook.createWorkbook(out);
            jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

            if (head != null && head.length > 0) {
                for (int i = 0; i < head.length; i++) {
                    ws.addCell(new Label(i, 0, head[i]));
                }
            }
            int pageSize = list.size() / 30000;
            for (int z = 1; z < list.size() + 1; z++) {
                String[] str = list.get(z - 1);
                for (int i = 0; i < str.length; i++) {
						/*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                    if (CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])) {
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    } else {
                        ws.addCell(new Label(i, z, str[i]));
                    }
                }
            }
            wwb.write();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != wwb) {
                wwb.close();
            }
            if (null != out) {
                out.close();
            }
        }
        return null;
    }
}
