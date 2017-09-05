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
import com.infodms.dms.dao.sales.storage.storagebase.SpecialFareSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TmSpecialCityFarePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : SpecialFareSet 
 * @Description   : 特殊省市运费设定 
 * @author        : xieyj
 * CreateDate     : 2013-6-17
 */
public class SpecialFareSet {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SpecialFareSetDao reDao = SpecialFareSetDao.getInstance();
	
	private final String SPECIAL_FARE_SET_URL = "/jsp/sales/storage/storagebase/fareSet/specialFareSetList.jsp";
	private final String ADD_SPECIAL_FARE_SET_URL = "/jsp/sales/storage/storagebase/fareSet/addSpecialFareSet.jsp";
	private final String EDIT_SPEICIAL_FARE_SET_URL = "/jsp/sales/storage/storagebase/fareSet/editSpecialFareSet.jsp";
	/**
	 * 
	 * @Title      : 
	 * @Description: 特殊省市运费设定初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void pecialFareSetInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg("");
			act.setOutData("list_vchl", list_vchl);	
			act.setForword(SPECIAL_FARE_SET_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费设定查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void specialFareSetQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
			String groupId=CommonUtils.checkNull(request.getParamValue("GROUP_ID")); //车系
			String province=CommonUtils.checkNull(request.getParamValue("PROVINCE")); //省份
			//String city=CommonUtils.checkNull(request.getParamValue("CITY")); //地级市
			String poseId = logonUser.getPoseId().toString();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("YIELDLY", yieldly);
			map.put("GROUP_ID", groupId);
			map.put("PROVINCE", province);
			//map.put("CITY", city);
			map.put("poseId", poseId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
		    PageResult<Map<String, Object>> ps = reDao.specialFareSetQuery(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运费设定查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void addSpecialFareSetInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly= MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl=new ArrayList<Map<String, Object>>();
			if(list_yieldly!=null && list_yieldly.size()>0){
				list_vchl =reDao.getVhclMsg(list_yieldly.get(0).get("AREA_ID").toString());
			}
			act.setOutData("list_vchl", list_vchl);	
			act.setForword(ADD_SPECIAL_FARE_SET_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"增加特殊省市运费信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 根据省份得到城市列表 
	 */
	public void getCityList(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String provinceCode=CommonUtils.checkNull(request.getParamValue("PROVINCE"));//产地
			Map<String, Object> map =new HashMap<String, Object>();
			map.put("PROVINCE", provinceCode);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getCitys(map, curPage,
					Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询城市");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void editSpecialFareSet(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = request.getParamValue("YIELDLY");
			String groupId = request.getParamValue("GROUP_ID");
			String provinceCode = request.getParamValue("PROVINCE");
			//String cityCode = request.getParamValue("city");
			String amount = request.getParamValue("amount");
			
			TmSpecialCityFarePO po = new TmSpecialCityFarePO();
			po.setSeriesId(Long.valueOf(groupId));
			po.setProvinceId(Long.valueOf(provinceCode));
			po.setYieldly(Long.valueOf(yieldly));
			TmSpecialCityFarePO po1 = new TmSpecialCityFarePO();
			po1.setAmount(Float.valueOf(amount));
			po1.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
			po1.setGreateBy(logonUser.getUserId());
			reDao.update(po, po1);
			
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"保存特殊省市运费信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void addSpecialFareSet(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));
			String groupId = CommonUtils.checkNull(request.getParamValue("GROUP_ID"));
			String provinceCode = CommonUtils.checkNull(request.getParamValue("PROVINCE"));
			String amount = CommonUtils.checkNull(request.getParamValue("amount"));
			List<Map<String, Object>> xList=reDao.getUpdateMsg(yieldly,groupId,provinceCode);
			if(xList!=null && xList.size()>0){
				act.setOutData("returnValue", 2);//该省份已设置费用
				return;
			}
			//首先判断数据库是否有该省的特殊运费
			List<Object> params=new ArrayList<Object>();
			params.add(Long.parseLong(groupId));
			params.add(Long.parseLong(yieldly));
			params.add(Float.valueOf(amount));
			params.add(logonUser.getUserId());
			params.add(Long.parseLong(provinceCode));
			reDao.insertFare(params);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"保存特殊省市运费信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void editFareSetInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = request.getParamValue("yieldly");
			String groupId = request.getParamValue("groupId");
			String provinceCode = request.getParamValue("provinceCode");
			//String cityCode = request.getParamValue("cityCode");
			List<Map<String, Object>> xList=reDao.getUpdateMsg(yieldly,groupId,provinceCode);
			Map<String, Object> xMap=new HashMap<String, Object>();
			if(xList!=null && xList.size()>0){
				xMap=xList.get(0);
			}
			act.setOutData("xMap", xMap);
			act.setForword(EDIT_SPEICIAL_FARE_SET_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"特殊省市运费修改初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/***
	 * 删除特殊城市运费设定
	 */
	public void deleteFareSet(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = request.getParamValue("yieldly");
			String groupId = request.getParamValue("groupId");
			String provinceCode = request.getParamValue("provinceCode");
			//String cityCode = request.getParamValue("cityCode");
			TmSpecialCityFarePO po = new TmSpecialCityFarePO();
			po.setYieldly(Long.valueOf(yieldly));
			po.setSeriesId(Long.valueOf(groupId));
			po.setProvinceId(Long.valueOf(provinceCode));
			reDao.delFare(po);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			act.setOutData("returnValue", 2);
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"特殊省市运费删除");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getVhclMsg(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));
			List<Map<String, Object>> groupList=reDao.getVhclMsg(yieldly);
			act.setOutData("groupList", groupList);	
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"车系下拉框");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
