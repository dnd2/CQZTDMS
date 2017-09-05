/**
 * 
 */
package com.infodms.dms.actions.sales.fleetmanage.fleetInfoManage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetInfoAppDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.dao.sales.marketmanage.planissued.ActivitiesPlanIssuedDao;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetFollowPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmFleetRequestDetailPO;
import com.infodms.dms.po.TtFleetSupportInfoPO;
import com.infodms.dms.po.TtFleetSupportPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户跟进维护Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-8-20
 * @author 
 * @mail:lishuai103@yahoo.cn
 * @version 1.0
 * @remark 
 */
public class FleetFollow {
	public Logger logger = Logger.getLogger(FleetFollow.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetFollowProtect.jsp";
	private final String queryinitUrl = "/jsp/sales/fleetmanage/fleetSupport/queryfollow.jsp";
	private final String detailUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetFollowCheck.jsp";
	private final String detailInfoUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetFollowInfo.jsp";
	private final String fleetAddInit="/jsp/sales/fleetmanage/fleetinfomanage/fleetAddInit.jsp";
	private final String fleetAddPre="/jsp/sales/fleetmanage/fleetinfomanage/fleetClientAdd.jsp";
	private final String fleetUpdatePre="/jsp/sales/fleetmanage/fleetinfomanage/fleetClientUpdate.jsp";
	/*
	 * 集团客户跟进初始化
	 */
	public void searchFollowInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户跟进信息维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 集团客户跟进查询
	 */
	public void fleetFollowforquery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String dutyType = logonUser.getDutyType();
			String companyId = String.valueOf(logonUser.getCompanyId());
			String orgId = String.valueOf(logonUser.getOrgId());
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetFollowCheckQuery(orgId, dutyType, fleetName, fleetType, startDate, endDate, companyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持财务复核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户跟进查询专用
	 */
	
	public void fleetFollowforqueryList(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//报备起始时间
			String endDate = request.getParamValue("endDate");		//报备结束时间
			String followStartDate = request.getParamValue("followStartDate");	//跟进起始时间
			String followEndDate = request.getParamValue("followEndDate");		//跟进结束时间
			String dutyType = logonUser.getDutyType();
			String companyId = String.valueOf(logonUser.getCompanyId());
			String orgId = String.valueOf(logonUser.getOrgId());
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetFollowCheckQueryList(orgId, dutyType, fleetName, fleetType, startDate, endDate, followStartDate, followEndDate, companyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持财务复核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 集团客户跟进
	 */
	public void fleetFollowCheck(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户Id
			String intentId = request.getParamValue("intentId");	//意向Id
			String contractId=request.getParamValue("contractId");//合同ID
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
			List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,contractId);
			//List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
			List<Map<String, Object>> list3 = dao.getFleetFollowbyId(fleetId, intentId);
			//act.setOutData("checkList", list2);
			//act.setOutData("intentId", intentId);
			//根据集团客户主表的id查询子表需求说明中的内容
			FleetInfoAppDao appDao=new FleetInfoAppDao();
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			act.setOutData("tfrdMapList", tfrdMapList);
			act.setOutData("intentList", list1);
			act.setOutData("list3", list3);
			act.setOutData("intentMap", map2);
			act.setOutData("fleetMap", map);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户跟进");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 跟进操作
	 */
	public void fleetFollowCheckdo(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户Id
			//String intentId = request.getParamValue("intentId");	//意向Id
			String followDate = request.getParamValue("followDate");//跟进时间
			String followRemark = request.getParamValue("followRemark");//跟进明细
			String followRank=request.getParamValue("followRank");//跟进等级
			String followPerson=request.getParamValue("followPerson");//跟进人员
			String followTheme=request.getParamValue("followTheme");//跟进明细
			TmFleetFollowPO po = new TmFleetFollowPO();
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			po.setFollowId(Long.parseLong(SequenceManager.getSequence("")));
			po.setFleetId(Long.parseLong(fleetId));
			//po.setIntentId(Long.parseLong(intentId));
			po.setFollowDate(f.parse(followDate));
			po.setFollowRemark(followRemark);
			po.setStatus(Constant.STATUS_ENABLE);
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			po.setFollowRank(Long.parseLong(followRank));
			po.setFollowTheme(followTheme);
			po.setFollowPerson(followPerson);
			dao.insert(po);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户跟进");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryFollowInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(queryinitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户跟进信息维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void fleetFollowInfo(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户Id
			String intentId = request.getParamValue("intentId");	//意向Id
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
			List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,"");
			List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
			List<Map<String, Object>> list3 = dao.getFleetFollowbyId(fleetId, intentId);
			FleetInfoAppDao appDao=new FleetInfoAppDao();
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			act.setOutData("tfrdMapList", tfrdMapList);
			act.setOutData("intentList", list1);
			act.setOutData("checkList", list2);
			act.setOutData("list3", list3);
			act.setOutData("intentMap", map2);
			act.setOutData("intentId", intentId);
			act.setOutData("fleetMap", map);
			act.setForword(detailInfoUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户跟进");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 集团客户新增
	 */
	public void fleetAddInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(fleetAddInit);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户跟进信息维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 集团客户新增初始化
	 */
	public void fleetAddPre(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(fleetAddPre);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户跟进信息维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 集团客户新增初始化
	 */
	public void fleetUpdatePre(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			// 取得页面传入的需更新对象的Id
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));  
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			List<Map<String, Object>> list2 = appDao.getFleetAuditInfobyId(fleetId);
			//查询车系  add by lishuai
			String poseId = String.valueOf(logonUser.getPoseId());
			String level = Constant.GROUP_LEVEL_02;
			String companyId = String.valueOf(logonUser.getOemCompanyId());
			List<Map<String, Object>> list = MaterialGroupManagerDao.getGroupDropDownBox(poseId, level, companyId);
			
			// 根据ID查询集团客户信息
			Map<String, Object> fleetMap = appDao.getFleetInfobyId(fleetId);
			
			act.setOutData("list", list);
			TmFleetPO tfpo=new TmFleetPO();
			tfpo.setFleetId(new Long(fleetId));
			tfpo=appDao.select(tfpo).get(0);
			
			TmDealerPO td =new TmDealerPO();
			td.setCompanyId(tfpo.getDlrCompanyId());
			td=(TmDealerPO)new UserManageDao().select(td).get(0);
			act.setOutData("dealerCode", td.getDealerCode());
			if(list2.size()>0){
			act.setOutData("checkList",list2);
			}else{
				act.setOutData("checkList",null);
			}
			act.setOutData("fleetMap", fleetMap);
			//根据集团客户主表的id查询子表需求说明中的内容
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			act.setOutData("tfrdMapList", tfrdMapList);
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("status", Constant.STATUS_ENABLE.toString()) ;
			map.put("isAllowApply", Constant.IF_TYPE_YES.toString()) ;
			List<Map<String, Object>> pactList = appDao.getPactInfo(map) ;
			act.setOutData("pactList", pactList) ;
			//根据集团客户主表的id查询子表商务支持中的内容
			List<Map<String,Object>> supportInfoList=appDao.getSupportInfoByFleetId(fleetId);
			act.setOutData("supportInfoList", supportInfoList);
			// 根据fleetId查询上传附件
			ActivitiesPlanIssuedDao dao = ActivitiesPlanIssuedDao.getInstance();
			List<Map<String, Object>> attachList = dao.getAttachInfos(fleetId);
			if (null != attachList && attachList.size() != 0) {
				act.setOutData("attachList", attachList);
			}
			Map<String, Object> map1 = FleetSupportDao.getInstance().getFleetInfobyId(fleetId);
			act.setOutData("fleetMaps", map1);
			act.setForword(fleetUpdatePre);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户跟进信息维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 集团客户新增
	 */
	public void fleetAddQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName=request.getParamValue("fleetName");
			String fleetType=request.getParamValue("fleetType");
			String companyId=request.getParamValue("companyId");
			String startDate=request.getParamValue("startDate");
			String endDate=request.getParamValue("endDate");
			Map<String,String> m=new HashMap<String, String>();
			m.put("fleetName", fleetName);
			m.put("fleetType", fleetType);
			m.put("companyId", companyId);
			m.put("startDate", startDate);
			m.put("endDate", endDate);
			String  curPages=request.getParamValue("curPage");
			int curPage=1;
			if(!"".equals(curPages)&&null!=curPages){
				curPage=Integer.parseInt(curPages);
			}
			int pageSize=10;
			PageResult<Map<String, Object>> ps=dao.fleetClientQuery(m, curPage, pageSize);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户跟进信息维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 集团客户保存和修改
	 */
	public void  fleetClientSave(){
		 try {
			String  operateType=CommonUtils.checkNull(request.getParamValue("operateType"));	
			TmFleetPO po= fleetClientAdd();
			//提交的时候执行
			 if("1".equals(operateType)){
				 CommonUtils.getFleetCode(po);
			 }
			 act.setForword(fleetAddInit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 修改和保存都调用的公用方法
	 */
	public TmFleetPO  fleetClientAdd() throws Exception{
		 AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		 	String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
		 	TmCompanyPO tc=CommonUtils.getCompanyInfo(dealerCode);
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));  	    //客户名称
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));	    //客户类型
			String mainBusiness = CommonUtils.checkNull(request.getParamValue("mainBusiness")); //主要业务 
			String fundSize = CommonUtils.checkNull(request.getParamValue("fundSize"));         //资金规模
			String staffSize = CommonUtils.checkNull(request.getParamValue("staffSize"));       //人员规模
			String purpose = CommonUtils.checkNull(request.getParamValue("purpose"));           //购车用途
			String region = CommonUtils.checkNull(request.getParamValue("region"));             //区域
			String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));           //邮编
			String address = CommonUtils.checkNull(request.getParamValue("address"));           //详细地址
			String mainLinkman = CommonUtils.checkNull(request.getParamValue("mainLinkman"));   //主要联系人
			String mainJob = CommonUtils.checkNull(request.getParamValue("mainJob"));           //主要联系人职务
			String mainPhone = CommonUtils.checkNull(request.getParamValue("mainPhone"));       //主要联系人电话
			String mainEmail = CommonUtils.checkNull(request.getParamValue("mainEmail"));       //主要联系人邮件
			String otherLinkman = CommonUtils.checkNull(request.getParamValue("otherLinkman")); //其他联系人
			String otherJob = CommonUtils.checkNull(request.getParamValue("otherJob"));         //其他联系人职务
			String otherPhone = CommonUtils.checkNull(request.getParamValue("otherPhone"));     //其他联系人电话
			String otherEmail = CommonUtils.checkNull(request.getParamValue("otherEmail"));     //其他联系人邮件
			String reqRemark = CommonUtils.checkNull(request.getParamValue("reqRemark"));       //需求说明
			//String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")); 	    //需求车系
			//String seriesCount = CommonUtils.checkNull(request.getParamValue("seriesCount"));   //需求数量
			
			String isPact = CommonUtils.checkNull(request.getParamValue("isPact"));
			String pactValueId = CommonUtils.checkNull(request.getParamValue("pactValueId"));
			//需求说明
			String visitDate = CommonUtils.checkNull(request.getParamValue("visitDate"));//拜访日期
			String marketInfo = CommonUtils.checkNull(request.getParamValue("marketInfo"));//市场信息
			String configRequire = CommonUtils.checkNull(request.getParamValue("configRequire"));//配置要求
			String fleetDiscount = CommonUtils.checkNull(request.getParamValue("fleetDiscount"));//大客户要求折让
			String ocandfp = CommonUtils.checkNull(request.getParamValue("ocandfp"));//其他竞争车型和优惠政策
			
			
			String pactManage = CommonUtils.checkNull(request.getParamValue("pactManage"));//批售经理名称
			String pactManagePhone = CommonUtils.checkNull(request.getParamValue("pactManagePhone"));//批售经理手机
			String pactManageEmail=CommonUtils.checkNull(request.getParamValue("pactManageEmail"));//批售经理邮箱
			String fleetId=CommonUtils.checkNull(request.getParamValue("fleetId"));
			TmFleetPO po = new TmFleetPO();
			
			// session中取得经销商公司ID
			Long dlrCompanyId = tc.getCompanyId();
			
			// session中取得车厂公司ID
			String oemCompanyId = tc.getOemCompanyId().toString();
			
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			Date currTime = new Date();
			po.setFleetName(fleetName);
			po.setIsPact(Integer.parseInt(isPact)) ;
			
			po.setPactManage(pactManage) ;
			po.setPactManagePhone(pactManagePhone) ;
			po.setPactManageEmail(pactManageEmail);
			if(Constant.IF_TYPE_YES.toString().equals(isPact)) {
				po.setPactId(Long.parseLong(pactValueId)) ;
			}
			
			if(!"".equals(fleetType)){
				po.setFleetType(new Integer(fleetType));
			}
			if(!"".equals(mainBusiness)){
				po.setMainBusiness(new Integer(mainBusiness));
			}
			if(!"".equals(fundSize)){
				po.setFundSize(new Integer(fundSize));
			}
			if(!"".equals(staffSize)){
				po.setStaffSize(new Integer(staffSize));
			}
			
			po.setZipCode(zipCode);
			po.setAddress(address);
			
			if(!"".equals(purpose)){
				po.setPurpose(new Integer(purpose));
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(!"".equals(visitDate)){
				po.setVisitDate(formatter.parse(visitDate));
			}
			//市场信息
			if(!"".equals(marketInfo)){
				po.setMarketInfo(marketInfo);
			}
			//配置要求
			if(!"".equals(configRequire)){
				po.setConfigRquire(configRequire);
			}
			//大客户要求折让
			if(!"".equals(fleetDiscount)){
				po.setFleetreqDiscount(fleetDiscount);
			}
			//其他竞争车型和优惠政策
			if(!"".equals(ocandfp)){
				po.setOthercompFavorpol(ocandfp);
			}
			
			po.setRegion(region);
			po.setMainLinkman(mainLinkman);
			po.setMainJob(mainJob);
			po.setMainPhone(mainPhone);
			po.setMainEmail(mainEmail);
			po.setOtherLinkman(otherLinkman);
			po.setOtherJob(otherJob);
			po.setOtherPhone(otherPhone);
			po.setOtherEmail(otherEmail);
			po.setReqRemark(reqRemark);
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(currTime);
			po.setSubmitUser(logonUser.getUserId());
			po.setSubmitDate(currTime);
			//po.setSeriesId(Long.parseLong(seriesId));
			//po.setSeriesCount(Integer.parseInt(seriesCount));
			// 设置状态为未提交
			po.setStatus(Constant.FLEET_INFO_TYPE_03);
			po.setDlrCompanyId(dlrCompanyId);
			po.setOemCompanyId(new Long(oemCompanyId));
			po.setIsDel(Integer.valueOf(Constant.IS_DEL_00));
			//如果没有值就新增，有值就修改
			if(fleetId==null||"".equals(fleetId)){
				fleetId = SequenceManager.getSequence(""); 
				po.setFleetId(new Long(fleetId));
				appDao.saveFleetInfo(po);
			}else{
				po.setFleetId(new Long(fleetId));
				TmFleetPO tpo=new TmFleetPO();
				tpo.setFleetId(new Long(fleetId));
				appDao.update(tpo, po);
			}
			//删除原来的数据
			TmFleetRequestDetailPO tfrd0=new TmFleetRequestDetailPO();
			tfrd0.setFleetId(new Long(fleetId));
			List<PO> oldList=dao.select(tfrd0);
			for(int i=0;i<oldList.size();i++){
				tfrd0=(TmFleetRequestDetailPO) oldList.get(i);
				dao.delete(tfrd0);
			}
			
			//start yinshunhui
			//获取明细表中的数据并且保存
			String[] materialIds = request.getParamValues("materialIds");
			String[] amounts=request.getParamValues("amounts");
			String[]describes=request.getParamValues("describes");
			if(materialIds!=null){
				for(int i=0;i<materialIds.length;i++){
					TmFleetRequestDetailPO tfrd=new TmFleetRequestDetailPO();
					tfrd.setDetailId(new Long(SequenceManager.getSequence("")));
					tfrd.setMaterId(new Long(materialIds[i]));
					tfrd.setFleetId(new Long(fleetId));
					tfrd.setAmount(new Long(amounts[i]));
					tfrd.setDiscribe(describes[i]);
					dao.insert(tfrd);
				}
			}
			
			String supportRemark= CommonUtils.checkNull(request.getParamValue("supportRemark"));//商务支持信息备注
			String groupIds = CommonUtils.checkNull(request.getParamValue("groupIds"));				//物料组IDS
			//String remarks = CommonUtils.checkNull(request.getParamValue("remarks"));				//备注
			String samounts = CommonUtils.checkNull(request.getParamValue("samounts"));				//数量
			String prices=CommonUtils.checkNull(request.getParamValue("prices"));
			String depotproprices=CommonUtils.checkNull(request.getParamValue("depotproprices"));
			String profits=CommonUtils.checkNull(request.getParamValue("profits"));
			String gandaccepts=CommonUtils.checkNull(request.getParamValue("gandaccepts"));
			String realprices=CommonUtils.checkNull(request.getParamValue("realprices"));
			String marketdevelops=CommonUtils.checkNull(request.getParamValue("marketdevelops"));
			String realprofits=CommonUtils.checkNull(request.getParamValue("realprofits"));
			String requestsupports=CommonUtils.checkNull(request.getParamValue("requestsupports"));
			String supportId=CommonUtils.checkNull(request.getParamValue("requestsupports"));
			//向tmfleet表中加入supportRemark的值
			TtFleetSupportPO tfpo=new TtFleetSupportPO() ;
			TtFleetSupportPO tfpo1=new TtFleetSupportPO();
			
			tfpo.setFleetId(new Long(fleetId));
			List<PO> tfsList=dao.select(tfpo);
			if(tfsList.size()>0){
				tfpo1.setSupportRemark(supportRemark);
				tfpo1.setSupportDate(new java.util.Date());
				tfpo1.setSupportStatus(Constant.SUPPORT_INFO_STATUS_04);
				tfpo1.setUpdateBy(new BigDecimal(logonUser.getUserId()));
				tfpo1.setUpdateDate(new java.util.Date());
				dao.update(tfpo,tfpo1);
			}else{
				tfpo1.setSupportId(new Long(SequenceManager.getSequence("")));
				tfpo1.setFleetId(new Long(fleetId));
				tfpo1.setCreateBy(logonUser.getUserId());
				tfpo1.setSupportStatus(Constant.SUPPORT_INFO_STATUS_04);
				tfpo1.setSupportRemark(supportRemark);
				tfpo1.setSupportDate(new java.util.Date());
				tfpo1.setCreateDate(new java.util.Date());
				dao.insert(tfpo1);
			}
			String [] groupId = groupIds.split(",");
			//String [] remark = remarks.split(",");
			String [] amount = samounts.split(",");
			String []price=prices.split(",");
			
			String []depotproprice=depotproprices.split(",");
			
			String []profit=profits.split(",");
			
			String []gandaccept=gandaccepts.split(",");
			String []realprice=realprices.split(",");
			String []marketdevelop=marketdevelops.split(",");
			String []realprofit=realprofits.split(",");
			String []requestsupport=requestsupports.split(",");
			//删除数据
			UserManageDao appDaos=new UserManageDao();
			TtFleetSupportInfoPO supportInfo0=new TtFleetSupportInfoPO();
			supportInfo0.setFleetId(new Long(fleetId));
			appDaos.delete(supportInfo0);
			for(int i = 0; i< groupId.length; i++){
				if(!"".equals(groupId[i])&&groupId[i]!=null){
					TtFleetSupportInfoPO supportInfo=new TtFleetSupportInfoPO();
					supportInfo.setSupportInfoId(Long.parseLong(SequenceManager.getSequence("")));
					supportInfo.setFleetId(Long.parseLong(fleetId));
					supportInfo.setPrice(Double.parseDouble(price[i]));
					supportInfo.setAmount(Long.parseLong(amount[i]));
					supportInfo.setIntentSeries(Long.parseLong(groupId[i]));
					supportInfo.setDepotProPrice(Double.parseDouble(depotproprice[i]));
					supportInfo.setGiveAndAccept(Double.parseDouble(gandaccept[i]));
					
//					supportInfo.setProfit(Double.parseDouble(profit[i]));
					supportInfo.setMarketDevelop(Double.parseDouble(marketdevelop[i]));
					supportInfo.setRealPrice(Double.parseDouble(realprice[i]));
					supportInfo.setRealProfit(Double.parseDouble(realprofit[i]));
					supportInfo.setRequestSupport(Double.parseDouble(requestsupport[i]));
					supportInfo.setCreateDate(new Date(System.currentTimeMillis()));
					supportInfo.setCreateBy(logonUser.getUserId());
					dao.insert(supportInfo);
				}
			}
			// 页面删除的附件
			String delAttachs = CommonUtils.checkNull(request.getParamValue("delAttachs"));
			String delAttachIds = delAttachs.replaceFirst(",", "");
			String[] delAttachArr = delAttachIds.split(",");
			if (null != delAttachArr && 0 != delAttachArr.length) {
				for (int i = 0; i < delAttachArr.length; i++) {
					FileUploadManager.delfileUploadByBusiness(delAttachArr[i], logonUser);
				}
			}
			// 附件ID
			String[] fjids = request.getParamValues("fjids");
			// 附件添加
			FileUploadManager.fileUploadByBusiness(fleetId, fjids, logonUser);
			return po;
	}
	
}
