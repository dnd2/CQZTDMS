package com.infodms.dms.actions.util;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.baseSetting.typeSet;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtCrmComplaintDelayRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class CommonUtilActions {
	
	private static Logger logger = Logger.getLogger(CommonUtilActions.class);
	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	

	CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
	/**
	 *  根据接收地区父类ID查询
	 * @Title      : changeRegion
	 * @Description: 根据接收地区父类ID查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void changeRegion(){

		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			act.setOutData("regionList", commonUtilDao.cascadeRegion(Integer.parseInt(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联地区");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据接收物料组父类ID查询
	 * @Title      : changeVhclMaterialGroup
	 * @Description: 根据接收物料组父类ID查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void changeVhclMaterialGroup(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			act.setOutData("vhclList", commonUtilDao.cascadeVhclMaterialGroup(Long.parseLong(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联物料组");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据接收经销商的省份CODE查询该下的经销商集合
	 * @Title      : changeVcPro
	 * @Description: 根据接收经销商的省份CODE查询该下的经销商集合
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void changeVcPro(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("dealerList", commonUtilDao.cascadeDealer(Integer.parseInt(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联省份下的经销商");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据接收经销商的ID查询经销商下的人员
	 * @Title      : changeVcPro
	 * @Description: 根据接收经销商的ID查询经销商下的人员
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void changeDealUser(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("dealUserList", commonUtilDao.cascadeDealerUser(Long.parseLong(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联经销商下的人员");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据大区ORG_ID查询该下的用户集合
	 * @Title      : changeVcPro
	 * @Description: 根据大区ORG_ID查询该下的用户集合
	 * @throws     :
	 * LastDate    : 2013-4-27
	 */
	public void changeOrg(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("orgUserList", commonUtilDao.cascadeOrgUser(Long.parseLong(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联大区用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据大区ORG_ID查询该下的省份
	 * @Title      : changeVcPro
	 * @Description: 根据大区ORG_ID查询该下的省份
	 * @throws     :
	 * LastDate    : 2013-5-2
	 */
	public void changeOrgPro(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("orgProList",commonUtilDao.cascadeOrgPro(Long.parseLong(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联大区省份");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据大区ID查询大区下的小区
	 * @Title      : cascadeOrgSmallOrg
	 * @Description: 根据大区ID查询大区下的小区
	 * @throws     :
	 * LastDate    : 2013-7-30
	 */
	public void cascadeOrgSmallOrg(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("orgProList",commonUtilDao.cascadeOrgSmallOrg(Long.parseLong(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联大区小区");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 根据接收业务类型查询业务内容和处理方式
	 * @Title      : changeBizContent
	 * @Description: 根据接收业务类型查询业务内容和处理方式
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void changeBizContent(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id")); 
			//内容类型
			List<Map<String,Object>> bclist = null;
			//处理方式
			List<Map<String,Object>> processList = null;
			typeSet ts = new typeSet();
			//投诉
			if(id.equals(String.valueOf(Constant.VOUCHER_TYPE_01))){
				bclist = ts.getTypeSelect(String.valueOf(Constant.COMPLAIN_CONTENT));
				processList = commonUtilDao.getTcCode(Constant.COMPLAINT_PROCESS.toString());
			//咨询	
			}else if(id.equals(String.valueOf(Constant.VOUCHER_TYPE_02))){
				bclist = ts.getTypeSelect(String.valueOf(Constant.CONSULT_CONTENT));
				processList = commonUtilDao.getTcCode(Constant.CONSULT_PROCESS.toString());
			}
			List<Map<String, Object>> rvList = new java.util.ArrayList<Map<String, Object>>();
			String type = "";
			/*if(bclist != null && bclist.size() > 0){
				for (Map<String, Object> map : bclist) {
					if(map.get("CODEDESCVIEW").toString().indexOf("-") == -1){
						type = "【"+map.get("CODEDESCVIEW").toString()+"】";
					}
					if(map.get("CODEDESCVIEW").toString().lastIndexOf("-") == 3){
						map.put("CODEDESCVIEW", map.get("CODEDESCVIEW").toString().replace("-", "")+type);
						rvList.add(map);
					}
				}
			}*/
			act.setOutData("bclist", bclist);
			act.setOutData("processList", processList);
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"根据接收业务类型查询业务内容和处理方式");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取详细信息
	 */
	public void getBizContentDetail(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id")); 
			String detail = "";
			if (!XHBUtil.IsNull(id)) {
				Map<String, Object> map = commonUtilDao.getBizContentDetail(id);
				if (map != null) {
					detail += map.get("TYPE_NAME")+"-"+map.get("CODE_DESC");
				}
			}
			act.setOutData("detail", detail);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"根据接收业务类型查询业务内容和处理方式");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 根据类型查询TCcode表
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> getTcCode(String type){
		return commonUtilDao.getTcCode(type);
	}
	
	/** 查询所有省份集合
	 * @Title      : getProvice
	 * @Description: 查询所有省份集合 
	 * @return     : List<Map<String,Object>>   
	 * LastDate    : 2013-4-24
	 * @author wangming
	 */
	public List<Map<String,Object>> getProvice(){
		return commonUtilDao.getProvice();
	}
	
	/**
	 * 查询所有的城市
	 * @Title      : getAllCity
	 * @Description: 查询所有的城市
	 * @return     : List<Map<String,Object>>   
	 * LastDate    : 2013-4-24
	 * @author wangming
	 */
	public List<Map<String,Object>> getAllCity(){
		return commonUtilDao.getAllCity();
	}
	
	/**
	 * 查询所有车系
	 * @Title      : getAllSeries
	 * @Description: 查询所有车系
	 * @return     :  List<TmVhclMaterialGroupPO>
	 * @throws     :
	 * LastDate    : 2013-4-24
	 * @author wangming
	 */
	public List<TmVhclMaterialGroupPO> getAllSeries(){
		return commonUtilDao.getAllSeries();
	}
	
	/**
	 * 级联操作车种车型配置等
	 * @Title      : getAllModel
	 * @Description: 查询所有车型
	 * @return     :  List<TmVhclMaterialGroupPO>
	 * @throws     :
	 * LastDate    : 2013-4-24
	 * @author wangming
	 */
	public List<TmVhclMaterialGroupPO> getAllModel(){
		return commonUtilDao.getAllModel();
	}
	
	/**
	 * 查询车厂
	 * @Title      : cascadeRegion
	 * @Description: 查询车厂
	 * @return     : List<Map<String,Object>>   
	 * LastDate    : 2013-4-24
	 * @author wangming
	 */
	public List<Map<String,Object>> getVehicleCompany(){
		return commonUtilDao.getVehicleCompany();
	}
	
	/**
	 * 查询所有大区(及顶级昌河)
	 * @Title      : getTmOrgPO
	 * @Description: 查询所有大区(及顶级昌河)
	 * @return     : List<Map<String,Object>>   
	 * LastDate    : 2013-4-24
	 * @author wangming
	 */
	public List<TmOrgPO> getTmOrgPOAndTop(){
		return commonUtilDao.queryTmOrgAndTop();
	}
	
	/**
	 * 查询所有大区
	 * @Title      : getTmOrgPO
	 * @Description: 查询所有大区
	 * @return     : List<Map<String,Object>>   
	 * LastDate    : 2013-4-24
	 * @author wangming
	 */
	public List<TmOrgPO> getTmOrgPO(){
		return commonUtilDao.queryTmOrgPO();
	}
	
	/**
	 * 查询所有大区及处部门
	 * @Title      : getAllOrg
	 * @Description: 查询所有大区及处部门
	 * @return     : List<Map<String,Object>>   
	 * LastDate    : 2013-4-24
	 * @author wangming
	 */
	public List<Map<String, Object>> getAllOrg(){
		return commonUtilDao.getAllOrg();
	}
	
	/**
	 * 根据level查询TM_ORG表
	 * @param level
	 * @return
	 */
	public List<Map<String, Object>> getOrgByLv(String orgId,String level){
		return commonUtilDao.getOrgByLv(orgId,level);
	}
	
	/**
	 * 根据大区获取大区下的所有经销商
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> getOrgDealer(String orgId){
		return commonUtilDao.getOrgDealer(orgId);
	}
	public List<Map<String, Object>> getRootOrgDealer(String orgId){
		return commonUtilDao.getRootOrgDealer(orgId);
	}
	/**
	 * 查询所有处部门
	 * @Title      : getOfficeOrg
	 * @Description: 查询所有处部门
	 * @return     : List<Map<String,Object>>   
	 * LastDate    : 2013-4-24
	 * @author wangming
	 */
	public List<Map<String, Object>> getOfficeOrg(){
		return commonUtilDao.getOfficeOrg();
	}
	public List<Map<String, Object>> getOfficeOrgSale(){
		return commonUtilDao.getOfficeOrgSale();
	}
	/**
	 * 查询所有小区
	 * @Title      : getSmallTmOrgPO
	 * @Description: 查询所有小区
	 * @return     : List<Map<String,Object>>   
	 * LastDate    : 2013-6-3
	 * @author wangming
	 */
	public List<TmOrgPO> getSmallTmOrgPO(){
		return commonUtilDao.querySmallTmOrgPO();
	}
	
	/**
	 * 查询所有小区
	 * @Title      : getSmallTmOrgPO
	 * @Description: 查询所有小区
	 * @return     : List<Map<String,Object>>   
	 * LastDate    : 2013-6-3
	 * @author wangming
	 */
	public List<Map<String,Object>> getSmallTmOrg(){
		return commonUtilDao.querygetSmallTmOrg();
	}
	
	/**
	 * 根据接收经销商的小区ID查询该下的经销商集合
	 * @Title      : changeOrgDealer
	 * @Description: 根据接收经销商的小区ID查询该下的经销商集合
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-3
	 */
	public void changeOrgDealer(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("dealerList", commonUtilDao.cascadeOrgDealer(Long.parseLong(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联小区下的经销商");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据接收经销商的小区ID查询该下的经销商集合
	 * @Title      : changeOrgDealer
	 * @Description: 根据接收经销商的小区ID查询该下的经销商集合
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-3
	 */
	public void changeOrgDealer1(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("dealerList", commonUtilDao.cascadeOrgDealer1(Long.parseLong(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联小区下的经销商");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据接收经销商的小区ID查询该下的经销商集合
	 * @Title      : changeOrgDealer
	 * @Description: 根据接收经销商的小区ID查询该下的经销商集合
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-3
	 */
	public void changeCity(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("myCityList", commonUtilDao.cascadeCity(Long.parseLong(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联小区下的经销商");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询所有经销商集合
	 * @Title      : getAllDealer
	 * @Description: 查询所有经销商集合
	 * @param      :       
	 * @return 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-6
	 */
	public List<Map<String, Object>> getAllDealer(){
		return commonUtilDao.getAllDealer();
	}
	
	/**
	 * 获取坐席管理员所在的部门
	 * @return  List<Map<String,Object>>
	 * @author wangming
	 * @date 2013-7-6
	 */
	public List<Map<String,Object>> getSeatAdminDepart(){
		return commonUtilDao.getSeatAdminDepart();
	}
	
	/**
	 * 获取坐席回访操作
	 * @return  List<Map<String,Object>>
	 * @author wangming
	 * @date 2013-7-6
	 */
	public List<Map<String,Object>> getSeatAdminDepartAc(){
		return commonUtilDao.getSeatAdminDepartAc();
	}
	
	/**
	 * 服务营销处大区下的坐席人员
	 * @return  List<Map<String,Object>>
	 * @author wangming
	 * @date 2013-7-15
	 */
	public void getSeatUser(){
		act.getResponse().setContentType("application/json");
		try{
			String orgid = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("seatList", commonUtilDao.getSeatUser(orgid));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联小区下的经销商");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public int turnCodeToDayForComplaintLimit(String cplimit){
		int day = 0;
		if(!"".equals(cplimit)&&cplimit.equals(Constant.RULE_ONE_DAY.toString())){
			day =  Constant.ONE_DAY;
		}else if(!"".equals(cplimit)&&cplimit.equals(Constant.RULE_THREE_DAY.toString())){
			day = Constant.THREE_DAY;
		}else if(!"".equals(cplimit)&&cplimit.equals(Constant.RULE_SEVEN_DAY.toString())){
			day = Constant.SEVEN_DAY;
		}
		return day;
	}
	
	public Integer turnDayToCodeForComplaintLimit(String cplimit){
		int day = 0;
		if(!"".equals(cplimit)&&cplimit.equals(Constant.ONE_DAY.toString())){
			day =  Constant.RULE_ONE_DAY;
		}else if(!"".equals(cplimit)&&cplimit.equals(Constant.THREE_DAY.toString())){
			day = Constant.RULE_THREE_DAY;
		}else if(!"".equals(cplimit)&&cplimit.equals(Constant.SEVEN_DAY.toString())){
			day = Constant.RULE_SEVEN_DAY;
		}
		return day;
	}
	
	public TtCrmComplaintDelayRecordPO copyTtCrmComplaintDelayRecordPO(TtCrmComplaintDelayRecordPO oldTtCrmComplaintDelayRecordPO,
			TtCrmComplaintDelayRecordPO newTtCrmComplaintDelayRecordPO){
		if(oldTtCrmComplaintDelayRecordPO == null ) return newTtCrmComplaintDelayRecordPO;
		newTtCrmComplaintDelayRecordPO.setApplyOrg(oldTtCrmComplaintDelayRecordPO.getApplyOrg());
		newTtCrmComplaintDelayRecordPO.setApplyType(oldTtCrmComplaintDelayRecordPO.getApplyType());
		newTtCrmComplaintDelayRecordPO.setClContent(oldTtCrmComplaintDelayRecordPO.getClContent());
		newTtCrmComplaintDelayRecordPO.setClDate(oldTtCrmComplaintDelayRecordPO.getClDate());
		newTtCrmComplaintDelayRecordPO.setClUser(oldTtCrmComplaintDelayRecordPO.getClUser());
		newTtCrmComplaintDelayRecordPO.setClUserId(oldTtCrmComplaintDelayRecordPO.getClUserId());
		newTtCrmComplaintDelayRecordPO.setClVerifyContent(oldTtCrmComplaintDelayRecordPO.getClVerifyContent());
		newTtCrmComplaintDelayRecordPO.setClVerifyDate(oldTtCrmComplaintDelayRecordPO.getClVerifyDate());
		newTtCrmComplaintDelayRecordPO.setClVerifyStatus(oldTtCrmComplaintDelayRecordPO.getClVerifyStatus());
		newTtCrmComplaintDelayRecordPO.setClVerifyUser(oldTtCrmComplaintDelayRecordPO.getClVerifyUser());
		newTtCrmComplaintDelayRecordPO.setClVerifyUserId(oldTtCrmComplaintDelayRecordPO.getClVerifyUserId());
		newTtCrmComplaintDelayRecordPO.setCpDealDealer(oldTtCrmComplaintDelayRecordPO.getCpDealDealer());
		newTtCrmComplaintDelayRecordPO.setCpDealOrg(oldTtCrmComplaintDelayRecordPO.getCpDealOrg());
		newTtCrmComplaintDelayRecordPO.setCpDeferType(oldTtCrmComplaintDelayRecordPO.getCpDeferType());
		newTtCrmComplaintDelayRecordPO.setCpId(oldTtCrmComplaintDelayRecordPO.getCpId());
		return newTtCrmComplaintDelayRecordPO;
	}
	 
	/**
	 * 根据配件ID查询责任单位集合
	 * @Title      : changePartMarker
	 * @Description: 根据配件ID查询责任单位集合
	 * @throws     :
	 * LastDate    : 2013-8-1
	 */
	public void changePartMarker(){
		act.getResponse().setContentType("application/json");
		try{
			String id = CommonUtils.checkNull(request.getParamValue("id"));  				
			
			act.setOutData("partMarkerList", commonUtilDao.cascadePartMarker(Long.parseLong(id)));
			act.setOutData("defaultValue", CommonUtils.checkNull(request.getParamValue("defaultValue")));
			act.setOutData("isdisabled", CommonUtils.checkNull(request.getParamValue("isdisabled")));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"级联配件责任单位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 翻译处理人或处理部门名字
	 * @return  List<Map<String,Object>>
	 * @author wangming
	 * @date 2013-10-25
	 */
	public String getDealUserName(Long id){
		return commonUtilDao.getDealUserName(id);
	}
	
	/**
	 * 根据大区获取小区
	 */
	public void choiseOrg(){
		try {
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			String orgId = request.getParamValue("orgId");
			act.setOutData("tmOrgs", commonUtilActions.getOrgByLv(orgId,""));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉受理");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
}
