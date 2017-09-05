/**********************************************************************
* <pre>
* FILE : CarType.java
* CLASS : CarType
*
* AUTHOR : wry
*
* FUNCTION : 基础数据维护
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-19| wry  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sysmng.sysData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.VhclModelInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.baseData.CarTypeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBrandPO;
import com.infodms.dms.po.TmModelPO;
import com.infodms.dms.po.TmSeriesPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.csv.ReaderUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

/**
 * function:基础数据导入 
 * author: wry
 * CreateDate: 2009-8-19
 * @version:0.1
 */
public class CarType {
	public Logger logger = Logger.getLogger(CarType.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	public final static int pageSize = 10;
	
	/**
	 * function:查询车型文件 
	 * @param:request-厂家 车系 车辆类型 排量 变速箱 年款
	 * @return:满足条件的信息
	 * author: wry
	 * @throws BizException 
	 * date: 2009-09-20
	 */
	public void queryCarInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try{
		    logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
//			if(!"1".equals(request.getParamValue("COMMAND"))){ //json请求
			String startDate = request.getParamValue("startDate")==null?null:request.getParamValue("startDate").trim();
			String endDate = request.getParamValue("endDate")==null?null:request.getParamValue("endDate").trim();
			String operator = request.getParamValue("operator")==null?null:request.getParamValue("operator").trim();
			//厂家
			String brand = CommonUtils.checkNull(request.getParamValue("brand"));
			//车系
			String seriesId = CommonUtils.checkNull(request.getParamValue("series_id"));
			//车辆类型
			String vhclType = CommonUtils.checkNull(request.getParamValue("VHCL_TYPE"));
			//排量
			String dspm = CommonUtils.checkNull(request.getParamValue("DSPM"));
			//变速箱
			String gearBox = CommonUtils.checkNull(request.getParamValue("GEAR_BOX"));
			//年款
			String modelYear = CommonUtils.checkNull(request.getParamValue("MODEL_YEAR"));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			//获取排序字段和排序类型
			String orderName = request.getParamValue("orderCol");
			String da = request.getParamValue("order");
//			String dealerId = logonUser.getDealerId();
			//查询结果集
			PageResult<VhclModelInfoBean> ps = CarTypeDao.getVhclInfo(brand,seriesId,vhclType,dspm,gearBox,modelYear, startDate, endDate,operator,request.getRequestURI(), logonUser,orderName, da, curPage);
			logger.debug("条数："+ps.getTotalRecords());
			act.setOutData("ps", ps);
//			}
			if("1".equals(request.getParamValue("falt"))){
			act.setForword("/jsp/systemMng/baseData/carTypeInfoSearch.jsp");
			}else{
			act.setForword("/jsp/systemMng/baseData/carTypeInfoImprotTxt.jsp");
			}
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"操作");
			logger.error(logonUser,e);
			act.setException(e1);
		}
	}
	/**
	 * function:导入车型信息 
	 * author: wry
	 * @throws BizException 
	 * date: 2009-08-23
	 */
	public void carTypeInfoImport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try{
		    logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			FileObject importFile = request.getParamObject("importFile");
			String YEAR = CommonUtils.checkNull(request.getParamValue("YEAR"));
			String MONTH = CommonUtils.checkNull(request.getParamValue("MONTH"));
			String modelDate =YEAR+MONTH;
			act.setForword("/jsp/systemMng/baseData/carTypeInfoImprotTxt.jsp");
			logger.debug("-----modelDate----"+modelDate);
			if(importFile != null){ //json请求
				long startTime =System.currentTimeMillis();
				ReaderUtil txtReader = new ReaderUtil(importFile.getContent(),"\t");
				//执行导入报表的数据检查，检查通过则插入到数据库中，如果检查不通过，则将错误的信息返回
				act.setOutData("fileName", importFile.getFileName());
				act.setOutData("goback", "ok");
				insertModelDatabae(txtReader,act,modelDate);
				logger.debug("----end --"+(System.currentTimeMillis()-startTime));
			}
			act.setOutData("MONTH", MONTH);
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"导入信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 执行插入数据库
	 * author: wry
	 * @throws BizException 
	 * date: 2009-08-23 
	 */
	private void insertModelDatabae(ReaderUtil txtReader,ActionContext act,String modelDate) throws Exception{
		//全局标识，如果为ture,则执行插入动作，如果为false，则只进行检查；
		AclUserBean logonUser = null;
		boolean allFlag = true;
		List<VhclModelInfoBean> errorList = new LinkedList<VhclModelInfoBean>();
//		List<TmModelPO	> rightList = new LinkedList<TmModelPO>();
//		List<TtVhclQuotPO> rightQuotList = new LinkedList<TtVhclQuotPO>();
		TmModelPO modeldPO =  null;
		VhclModelInfoBean vmBean = null;
		Date startDate = new Date();
		List<TmBrandPO> brandList = factory.select("select brand_name,brand_id from TM_BRAND",null,new DAOCallback<TmBrandPO>() {
			public TmBrandPO wrapper(ResultSet rs, int idx){
				TmBrandPO bean  = new TmBrandPO();
				try{
					bean.setBrandName(rs.getString("brand_name"));
					bean.setBrandId(rs.getLong("brand_id"));
					return bean;
				}catch(SQLException e){
					throw new DAOException(e);
				}
			}
		});
		List<TmSeriesPO> seriesList = factory.select("select series_name,series_id from TM_SERIES",null,new DAOCallback<TmSeriesPO>() {
			public TmSeriesPO wrapper(ResultSet rs, int idx){
				TmSeriesPO bean  = new TmSeriesPO();
				try{
					bean.setSeriesName(rs.getString("series_name"));
					bean.setSeriesId(rs.getLong("series_id"));
					return bean;
				}catch(SQLException e){
					throw new DAOException(e);
				}
			}
		});

		try{
		    logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//			JSONArray array = new JSONArray();
		    List array = new ArrayList();
//			logger.debug("-----getColNum-----"+txtReader.getColNum());
			if(txtReader.getColNum()!=53){
				throw new BizException(act,ErrorCodeConstant.WRITE_CSV_FORMAT_FALSENESS);
			}
//			logger.debug("-----getRowNum-----"+txtReader.getRowNum());
			for(int i=1;i<txtReader.getRowNum();i++){
				if(errorList.size()<=Constant.errorNum){
//					logger.debug("%%%%%%%%"+txtReader.getRow(i));
					logger.debug("********"+i);
					String checkStr =null;
//					String checkStr = checkModelDataOfRow(txtReader.getRow(i),seriesList);
					if(checkStr !=null){
						allFlag = false;
						vmBean = new VhclModelInfoBean();
//						vmBean = new VhclModelInfoBean();
						vmBean.setModelCode(txtReader.getString(i, 0));
						vmBean.setProdArea(txtReader.getString(i, 46));
						vmBean.setSeriesId(txtReader.getString(i, 2));
						vmBean.setVhclType(txtReader.getString(i, 3));
						vmBean.setDspm(txtReader.getString(i, 23));
						vmBean.setGearBox(txtReader.getString(i, 18));
						vmBean.setNoticeCode(txtReader.getString(i, 47));
						vmBean.setModelName(txtReader.getString(i, 7));
						vmBean.setModelYear(txtReader.getString(i, 4));
						vmBean.setRow(String.valueOf(i+1));
						vmBean.setMark(checkStr);
						errorList.add(vmBean);
						//转化成json格式
//						JSONObject object = new JSONObject(vmBean);
						array.add(vmBean);
					}
					//如果发现有错误的特殊车辆信息，将不再进行数据库的插入。
					if(allFlag){
						String modelCode = txtReader.getString(i, 0);
						String prodArea = txtReader.getString(i, 46);
						String vhclType = txtReader.getString(i, 3);
						String dspm = txtReader.getString(i, 23);
						String gearBox = txtReader.getString(i, 18);
						String noticeCode = txtReader.getString(i, 47);
						String modelName = txtReader.getString(i, 7);
						String boxNum = txtReader.getString(i, 25);
						String powerType = txtReader.getString(i, 26);
						logger.debug("powerType------"+powerType);
						String loadNum = txtReader.getString(i, 22);
						String fuel =  txtReader.getString(i, 30);
						String doorNum = txtReader.getString(i, 21);
						String engine =  txtReader.getString(i, 24);	
						String avgConPrice = txtReader.getString(i, 48);
						String avgConRetailPrice = txtReader.getString(i, 49);
						String year = txtReader.getString(i, 4);
						logger.debug("year------"+year);
						String month = txtReader.getString(i, 5);
						logger.debug("month------"+month);
						String goodConDlrPrice = txtReader.getString(i, 50);
						String goodConRetaiPrice = txtReader.getString(i, 51);
						String msrp = txtReader.getString(i, 52);
//                      根据品牌名字得到品牌id,判断品牌名称如果不存在，就插入数据
						Long  brandId= null;
						String  brandName = txtReader.getString(i, 1).trim();
						for(TmBrandPO brand: brandList){
							if(brand.getBrandName()!=null&&(brand.getBrandName().equals(brandName))){
								brandId = brand.getBrandId();
								break;
							 }
						}
						TmBrandPO brandPO = new TmBrandPO();
						if(brandId==null){
//							logger.debug("-----brand into-------");
							//往品牌里面插入信息
							brandPO.setBrandId(factory.getLongPK(brandPO));
							brandPO.setBrandName(brandName);
							brandPO.setStatus(Constant.STATUS_ENABLE);
							//初使化值
							ActionUtil.setCreatePOTrack(brandPO,logonUser);
							ActionUtil.setUpatePOTrack(brandPO,logonUser);
							factory.insert(brandPO);
							brandList.add(brandPO);
						}
//                      根据车系名字得到车系id, 根据车系名字得到品牌id,判断车系名称如果不存在，就插入数据
						Long  seriesId= null;
						String  seriesName = txtReader.getString(i, 2).trim();
						for(TmSeriesPO series: seriesList){
							if(series.getSeriesName()!=null&&series.getSeriesName().equals(seriesName)){
								seriesId = series.getSeriesId();
								break;
							}
						}
						TmSeriesPO seriesPO = new TmSeriesPO();
						if(seriesId==null){
//							logger.debug("-----series into-------");
							seriesPO.setSeriesId(factory.getLongPK(seriesPO));
							seriesPO.setSeriesName(seriesName);
							seriesPO.setStatus(Constant.STATUS_ENABLE);
							if(brandId==null){
								seriesPO.setBrandId(brandPO.getBrandId());
							}else{
//								logger.debug("brandId---"+brandId);
								seriesPO.setBrandId(brandId);
							}
						//初使化值
							ActionUtil.setCreatePOTrack(seriesPO,logonUser);
							ActionUtil.setUpatePOTrack(seriesPO,logonUser);
							factory.insert(seriesPO);
							seriesList.add(seriesPO);
						}
//						    查看车型代码是否存在，如果存在就更新，不存在就插入
						    TmModelPO  modeldSPO = new TmModelPO();
//						   logger.debug("modelCode------"+ modelCode);
						   modeldSPO.setModelCode(modelCode);
						    List<TmModelPO> tmList = factory.select(modeldSPO);
						    if(tmList.size() > 0){
						    modeldSPO = (TmModelPO)tmList.get(0);
						    }
						   if(tmList.size()==0){
//							  modeldPO =  new TmModelPO();
////							 logger.debug("&&&&&&modeld insert to&&&&&&");
////							车型代码不存在直接添加到车型表
//							modeldPO.setModelId(factory.getStringPK(modeldPO));
//							modeldPO.setModelCode(modelCode);
//							modeldPO.setProdArea(prodArea);
//							if(seriesId==null){
//								modeldPO.setSeriesId(seriesPO.getSeriesId());
//							}else{
////								logger.debug("insert-----seriesId---"+seriesId);
//								modeldPO.setSeriesId(seriesId);
//							}
//							modeldPO.setVhclType(vhclType);
//							modeldPO.setDspm(dspm);
//							modeldPO.setModelStat(Constant.STATUS_ENABLE);
//							modeldPO.setGearBox(gearBox);
//							modeldPO.setNoticeCode(noticeCode);
//							modeldPO.setModelName(modelName);
//							modeldPO.setModelYear(year);
//							modeldPO.setModelMonth(month);
//							modeldPO.setRedbookCode(modelCode);
//							modeldPO.setEngine(engine);
//							modeldPO.setFuel(fuel);
//							modeldPO.setBoxNum(boxNum);
//							modeldPO.setPowerType(powerType);
//							modeldPO.setLoadNum(loadNum);
//							modeldPO.setDoorNum(doorNum);
//							//初使化值
//							ActionUtil.setCreatePOTrack(modeldPO,logonUser);
//							ActionUtil.setUpatePOTrack(modeldPO,logonUser);
////							rightList.add(modeldPO);
//							factory.insert(modeldPO);
//						   }else{
//							   logger.debug("&&&&&&modeld update to&&&&&&");
//						    车型代码存在就直接修改
//							   TmModelPO  conditionPO = new TmModelPO();
//							   conditionPO.setModelId(modeldSPO.getModelId());
//							   modeldPO =  new TmModelPO();
//								modeldPO.setModelCode(modelCode);
//								modeldPO.setProdArea(prodArea);
//								if(seriesId==null){
//									modeldPO.setSeriesId(seriesPO.getSeriesId());
//									}else{
////										logger.debug("update----seriesId---"+seriesId);
//										modeldPO.setSeriesId(seriesId);
//									}
//								modeldPO.setVhclType(vhclType);
//								modeldPO.setDspm(dspm);
//								modeldPO.setGearBox(gearBox);
//								modeldPO.setNoticeCode(noticeCode);
//								modeldPO.setModelName(modelName);
//								modeldPO.setModelYear(year);
//								modeldPO.setFuel(fuel);
//								modeldPO.setRedbookCode(modelCode);
//								modeldPO.setModelMonth(month);
//								modeldPO.setEngine(engine);
//								modeldPO.setBoxNum(boxNum);
//								modeldPO.setPowerType(powerType);
//								modeldPO.setLoadNum(loadNum);
//								modeldPO.setDoorNum(doorNum);
//								
								ActionUtil.setUpatePOTrack(modeldPO, logonUser);
								ActionUtil.setCreatePOTrack(modeldPO, logonUser);
//								logger.debug("modelID---"+modeldSPO.getModelId());
//								factory.update(conditionPO, modeldPO);
//								rightList.add(modeldPO);
						   }
//						   判断车型行情表里的id是否存在，如果存在就直接删除，然后再插入，不存在就直接插入
//						   TtVhclQuotPO vhclsPO = new TtVhclQuotPO();
//						   TtVhclQuotPO delvhclsPO = new TtVhclQuotPO();
//							if(modeldSPO.getModelId() !=null){
//								vhclsPO.setModelId(modeldSPO.getModelId());
//							}else{
//								vhclsPO.setModelId(modeldPO.getModelId());
//							}
////							   logger.debug("modelID****---"+vhclsPO.getModelId());
////						   vhclsPO.setModelId(modeldPO.getModelId());
//						   vhclsPO.setEfficientYearMonth(modelDate);
//						   List<TtVhclQuotPO> tvqList = factory.select(vhclsPO);
//						   if(tvqList.size() > 0){
//							   for (int j = 0; j < tvqList.size(); j++) {
//								   vhclsPO = (TtVhclQuotPO)tvqList.get(j);
//								   delvhclsPO.setVhclQuotId(vhclsPO.getVhclQuotId());
//								   factory.delete(delvhclsPO);
//							}
//						   }
//						   
//						添加车型行情表
						   String tempStr = null;
//							TtVhclQuotPO vhclPO = new TtVhclQuotPO();
//							vhclPO.setVhclQuotId(factory.getStringPK(vhclPO));
//							if(modeldSPO.getModelId() !=null){
//								vhclPO.setModelId(modeldSPO.getModelId());
//							}else{
//								vhclPO.setModelId(modeldPO.getModelId());
//							}
//						    tempStr = CommonUtils.checkNull(avgConPrice);
//							if(!"".equalsIgnoreCase(tempStr)) vhclPO.setAvgConDlrPrice(CommonUtils.parseDouble(tempStr));	
//						    tempStr = CommonUtils.checkNull(avgConRetailPrice);
//						    if(!"".equalsIgnoreCase(tempStr)) vhclPO.setAvgConRetailPrice(CommonUtils.parseDouble(tempStr));
//							vhclPO.setEfficientYearMonth(modelDate);
//						    tempStr = CommonUtils.checkNull(goodConDlrPrice);
//						    if(!"".equalsIgnoreCase(tempStr)) vhclPO.setGoodConDlrPrice(CommonUtils.parseDouble(tempStr));
//						    tempStr = CommonUtils.checkNull(goodConRetaiPrice);
//						    if(!"".equalsIgnoreCase(tempStr)) vhclPO.setGoodConRetailPrice(CommonUtils.parseDouble(tempStr));
//							vhclPO.setImportedDate(new Date(System.currentTimeMillis()));
//						    tempStr = CommonUtils.checkNull(msrp);
//						    if(!"".equalsIgnoreCase(tempStr)) vhclPO.setMsrp(CommonUtils.parseDouble(tempStr));
//							ActionUtil.setCreatePOTrack(vhclPO,logonUser);
//							ActionUtil.setUpatePOTrack(vhclPO,logonUser);
////							rightQuotList.add(vhclPO);
//							factory.insert(vhclPO);
					}
				}else{
					break;
				}
			}
			//对数据检查完成之后，对数据进行处理，如果有错误，则将错误的特殊车辆信息返回到页面中
			//如果没有错误，则将数据插入到数据库中。
			if(!allFlag){
				logger.debug("message:"+array.toString());
				logger.debug("message:"+array.size());
				act.setOutData("errorList", array);
				act.setOutData("errorSize", array.size());
				act.setOutData("falt", "1");
//				throw new BizException("error");
			}else{
//				factory.insert(rightList);
//				factory.insert(rightQuotList);
				Date endDate = new Date();
				act.setOutData("startDate", DateTimeUtil.parseDateToString(startDate));
				act.setOutData("endDate", DateTimeUtil.parseDateToString(endDate));
				act.setOutData("operator", ((AclUserBean)act.getSession().get(Constant.LOGON_USER)).getUserId());
			}
			act.setOutData("falt", "1");
		}catch(BizException e){
			logger.error(logonUser,e);
			e.printStackTrace();
			act.setException(e);
		}catch(Exception e){;
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"操作");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 检查数据的合法性
	 * author: wry
	 * @throws BizException 
	 * date: 2009-08-22
	 */
	private String checkModelDataOfRow(String rowContent,List<TmSeriesPO> seriesList){
		String[] cols = rowContent.split("\t",-1);
//		for (int i = 0; i < cols.length; i++) {
//			System.err.println("cols----i:"+cols[i]);
//		}
		String modelCode = cols[0];
		String series = cols[2];
		String month = cols[5];
		String monthValidate = "";
		String avgConDlrPrice = cols[48];
		String avgConRetailPrice = cols[49];
		String goodConDlrPrice = cols[50];
		String goodConRetailPrice = cols[51];
		System.err.println("cols.length----"+cols.length );
		String msrp= cols[52];
	
		System.err.println("msrp----"+msrp);
		StringBuffer sb = new StringBuffer();
		//检查车系代码是否存在
//		boolean flag = false;
//		for(TmSeriesPO seriest:seriesList){
//			if(seriest.getSeriesName()!=null&&(seriest.getSeriesName().equals(series))){
//				flag = true;
//				break;
//			}
//		}
		
		if(CheckUtil.checkNull(modelCode)){
			String noNull = "REDBOOK代码不能为空";
			sb.append(noNull).append(";");
		}
		if(CheckUtil.checkNull(series)){
			String noNull = "车系不能为空";
			sb.append(noNull).append(";");
		}
//		if(!flag){
//			String seriesCodeStr ="您导入的车系不存在！";
//			sb.append(seriesCodeStr).append(";");
//		}
		  if(CommonUtils.isMinus(Double.valueOf(month))){
    		  monthValidate="月份必须是正数值型！";
				sb.append(monthValidate).append(";");
    	  }
//		if(month.length()!=2){
//			monthValidate="月份必须是两位数！";
//			sb.append(monthValidate).append(";");
//		}else {
//		      if(!CommonUtils.isNumber(month) ){
//		    	  monthValidate="月份必须是数值型！";
//					sb.append(monthValidate).append(";");
//		      }else{
//		    	  if(CommonUtils.isMinus(Double.valueOf(month))){
//		    		  monthValidate="月份必须是正数值型！";
//						sb.append(monthValidate).append(";");
//		    	  }else{
//			    	  if(Long.parseLong(month) > 12){
//			    		  monthValidate="月份不能大于12！";
//			    		  sb.append(monthValidate).append(";");
//			    	  }
//		    	  }
//		      }
//		}
		if(!CommonUtils.isNumber(avgConDlrPrice)){
			String avgPrice = "平均车况批发价必须是数值型！";
			sb.append(avgPrice).append(";");
		}
		if(!CommonUtils.isNumber(avgConRetailPrice)){
			String avgCPrice = "平均车况零售价必须是数值型！";
			sb.append(avgCPrice).append(";");
		}
		if(!CommonUtils.isNumber(goodConDlrPrice)){
			String goodPrice = "良好车况批发价必须是数值型！";
			sb.append(goodPrice).append(";");
		}
		if(!CommonUtils.isNumber(goodConRetailPrice)){
			String goodCPrice = "良好车况零售价必须是数值型！";
			sb.append(goodCPrice).append(";");
		}
		if(!CommonUtils.isNumber(msrp)){
			String msrpPrice = "车厂家指导价必须是数值型！";
			sb.append(msrpPrice).append(";");
		}
		//检查描述
//		String descStr =CheckUtil.checkDescAll(cols[1], 50, "配置描述", false);
//		if(descStr.length()>0){
//			sb.append(descStr).append(";");
//		}
		if(sb.length()>0){
			return sb.substring(0,(sb.length()-1)).toString();
		}else{
			return null;
		}
		
	}
}
