package com.infodms.dms.actions.sales.fleetmanage.fleetInfoManage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.FleetInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetInfoAppDao;
import com.infodms.dms.dao.sales.usermng.IntegationManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmFleetRequestDetailPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:FleetInfoApplication.java</p>
 *
 * <p>Description: 集团客户信息报备业务逻辑层(经销商端)</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-8</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class FleetInfoApplication {
	
	private static Logger logger = Logger.getLogger(FleetInfoApplication.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	IntegationManageDao dao=IntegationManageDao.getInstance();
	
	// 集团客户信息报备初始化页面
	private final String fleetInfoAppInitUrl = "/jsp/sales/fleetmanage/fleetinfomanage/fleetInfoAppDLR.jsp";
	// 集团客户信息报备新增页面
	private final String fleetInfoAppAddInitUrl = "/jsp/sales/fleetmanage/fleetinfomanage/addFleetInfoAppDLR.jsp";
	
	// 集团客户信息选择页面
	private final String showFleetUrl = "/jsp/sales/fleetmanage/fleetinfomanage/showFleetInfo.jsp";
	
	// 集团客户信息报备更改页面
	private final String fleetInfoAppUpdateUrl = "/jsp/sales/fleetmanage/fleetinfomanage/updateFleetInfoAppDLR.jsp";
	
	// 集团客户报备信息详情页面
	private final String fleetInfoDetailUrl = "/jsp/sales/fleetmanage/fleetinfomanage/viewFleetDetailInfo.jsp";
	
	//物料查询页面
	private final String SELECTMATERIALURL="/jsp/sales/fleetmanage/fleetinfomanage/materialList.jsp";
	//选择物料的页面
	private final String CHOOSE_MATERIAL_URL = "/jsp/sales/fleetmanage/fleetinfomanage/chooseMaterialForReturn.jsp";
	/**
	 * 集团客户信息报备初始化
	 */
	public void applyFleetInfoInit(){
		try{
			act.setForword(fleetInfoAppInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"集团客户信息报备");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户信息报备新增初始化
	 */
	public void addFleetInfo(){
		try{
			//查询车系  add by lishuai
			String poseId = String.valueOf(logonUser.getPoseId());
			String level = Constant.GROUP_LEVEL_02;
			String companyId = String.valueOf(logonUser.getOemCompanyId());
			List<Map<String, Object>> list = MaterialGroupManagerDao.getGroupDropDownBox(poseId, level, companyId);
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("status", Constant.STATUS_ENABLE.toString()) ;
			map.put("isAllowApply", Constant.IF_TYPE_YES.toString()) ;
			
			FleetInfoAppDao appdao = new FleetInfoAppDao();
			
			List<Map<String, Object>> pactList = appdao.getPactInfo(map) ;
			Map<String, Object> pactMap = appdao.getFleetPactManage(logonUser.getCompanyId().toString()) ;
			
			act.setOutData("pactMap", pactMap) ;
			act.setOutData("pactList", pactList) ;
			act.setOutData("list", list);
			act.setForword(fleetInfoAppAddInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"集团客户信息报备新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户信息报备查询
	 */
	public void queryFleetInfo(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));  	    //客户名称
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));	    //客户类型
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));       //提报开始日期
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));           //提报结束日期
			String dlrCompanyId = CommonUtils.checkNull(logonUser.getCompanyId());              //经销商公司
			
			
			FleetInfoBean fibean = new FleetInfoBean();
			fibean.setFleetName(fleetName);
			fibean.setFleetType(fleetType);
			fibean.setBeginTime(beginTime);
			fibean.setEndTime(endTime);
			fibean.setDlrCompanyId(dlrCompanyId);
			fibean.setStatus(String.valueOf(Constant.FLEET_INFO_TYPE_01)+","+String.valueOf(Constant.FLEET_INFO_TYPE_04));
			
			FleetInfoAppDao appdao = new FleetInfoAppDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = appdao.queryFleetInfo(fibean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
			act.setForword(fleetInfoAppInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 显示集团客户信息
	 */
	public void showFleetList(){
		try{
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));       //客户类型
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));		//客户名称
			String dlrCompanyId = CommonUtils.checkNull(logonUser.getCompanyId());              //经销商公司
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			
			FleetInfoBean fibean = new FleetInfoBean();
			fibean.setFleetName(fleetName);
			fibean.setFleetType(fleetType);
			fibean.setDlrCompanyId(dlrCompanyId);
			fibean.setStatus(String.valueOf(Constant.FLEET_INFO_TYPE_03));
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = appDao.queryFleetInfo(fibean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			act.setForword(showFleetUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增集团客户信息
	 */
	public void saveFleetInfo(){
		try{
			//CommonUtils.checkNull() 校验是否为空
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
			
			// Sequence生成ID
			String fleetId = SequenceManager.getSequence(""); 
			
			// session中取得经销商公司ID
			Long dlrCompanyId = logonUser.getCompanyId();
			
			// session中取得车厂公司ID
			String oemCompanyId = logonUser.getOemCompanyId();
			
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			Date currTime = new Date();
			
			TmFleetPO po = new TmFleetPO();
			po.setFleetId(new Long(fleetId));
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
//			if(!"".equals(marketInfo)){
				po.setMarketInfo(marketInfo);
//			}
			//配置要求
//			if(!"".equals(configRequire)){
				po.setConfigRquire(configRequire);
//			}
			//大客户要求折让
//			if(!"".equals(fleetDiscount)){
				po.setFleetreqDiscount(fleetDiscount);
//			}
			//其他竞争车型和优惠政策
//			if(!"".equals(ocandfp)){
				po.setOthercompFavorpol(ocandfp);
//			}
			
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
			po.setStatus(Constant.FLEET_INFO_TYPE_01);
			po.setDlrCompanyId(dlrCompanyId);
			po.setOemCompanyId(new Long(oemCompanyId));
			po.setIsDel(Integer.valueOf(Constant.IS_DEL_00));
			
			
			appDao.saveFleetInfo(po);
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
			//end
			act.setRedirect(fleetInfoAppInitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"集团客户信息报备新增保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户报备信息修改初始化
	 */
	public void updateFleetInfo(){
		try{
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
			
			act.setForword(fleetInfoAppUpdateUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"集团客户信息报备修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改集团客户报备信息
	 */
	public void saveModifyInfo(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));  	        //客户ID
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
		//	String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));         //需求车系
			//String seriesCount = CommonUtils.checkNull(request.getParamValue("seriesCount"));   //需求数量
			//String visitDate = CommonUtils.checkNull(request.getParamValue("visitDate"));       //拜访日期
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
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			Date currTime = new Date(System.currentTimeMillis());
			
			TmFleetPO po = new TmFleetPO();
			
			po.setFleetName(fleetName);
			
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
			//市场信息
//			if(!"".equals(marketInfo)){
				po.setMarketInfo(marketInfo);
//			}
			//配置要求
//			if(!"".equals(configRequire)){
				po.setConfigRquire(configRequire);
//			}
			//大客户要求折让
//			if(!"".equals(fleetDiscount)){
				po.setFleetreqDiscount(fleetDiscount);
//			}
			//其他竞争车型和优惠政策
//			if(!"".equals(ocandfp)){
				po.setOthercompFavorpol(ocandfp);
//			}
			po.setZipCode(zipCode);
			po.setAddress(address);
			
			if(!"".equals(purpose)){
				po.setPurpose(new Integer(purpose));
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(!"".equals(visitDate)){
				po.setVisitDate(formatter.parse(visitDate));
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
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(currTime);
			//po.setSeriesId(Long.parseLong(seriesId));
			//po.setSeriesCount(Integer.parseInt(seriesCount));
			po.setIsPact(Integer.parseInt(isPact)) ;
			
			po.setPactManage(pactManage) ;
			po.setPactManagePhone(pactManagePhone) ;
			po.setPactManageEmail(pactManageEmail);
			
			if(Constant.IF_TYPE_YES.toString().equals(isPact)) {
				po.setPactId(Long.parseLong(pactValueId)) ;
			} else {
				po.setPactId(new Long("-1")) ;
			}
		
			TmFleetPO po1 = new TmFleetPO();
			po1.setFleetId(new Long(fleetId));
			
			appDao.updateFleetInfo(po1, po);
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
			//end
			
			act.setRedirect(fleetInfoAppInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"集团客户信息报备修改完成");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户信息报备提交
	 */
	public void submitFleetInfo(){
		try{
			// 取得页面传参
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));  	        //客户ID
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			Date currTime = new Date();
			
			TmFleetPO po1 = new TmFleetPO();
			po1.setFleetId(new Long(fleetId));
			TmFleetPO po2 = new TmFleetPO();
			po2.setStatus(Constant.FLEET_INFO_TYPE_02);
			po2.setSubmitDate(currTime);
			po2.setSubmitUser(logonUser.getUserId());
			po2.setUpdateBy(logonUser.getUserId());
			po2.setUpdateDate(currTime);
			
			appDao.updateFleetInfo(po1,po2);
			
			// 传返回值给页面回调函数
			act.setOutData("returnValue", 1);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"集团客户信息报备提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查看集团客户信息详情
	 */
	public void viewFleetDetailInfo(){
		try{
			// 取得页面传参
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));  	 
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			
			// 根据集团客户ID取得集团客户详细信息
			Map<String, Object> fleetMap = appDao.getFleetInfobyId(fleetId);
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("status", Constant.STATUS_ENABLE.toString()) ;
			map.put("isAllowApply", Constant.IF_TYPE_YES.toString()) ;
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			act.setOutData("tfrdMapList", tfrdMapList);
			
			List<Map<String, Object>> pactList = appDao.getPactInfo(map) ;
			
			List<Map<String, Object>> list2 = appDao.getFleetAuditInfobyId(fleetId);
			act.setOutData("checkList", list2);
			act.setOutData("pactList", pactList) ;
			
			act.setOutData("fleetMap", fleetMap);
			
			act.setForword(fleetInfoDetailUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户信息详情");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增页面提交按钮的响应事件
	 */
	public void addSubmitInfo(){
		try{
			//CommonUtils.checkNull() 校验是否为空
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
//			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")); 	    //需求车系
//			String seriesCount = CommonUtils.checkNull(request.getParamValue("seriesCount"));   //需求数量
//			String visitDate = CommonUtils.checkNull(request.getParamValue("visitDate"));       //拜访时间
			String isPact = CommonUtils.checkNull(request.getParamValue("isPact"));
			String pactValueId = CommonUtils.checkNull(request.getParamValue("pactValueId"));
			
			String pactManage = CommonUtils.checkNull(request.getParamValue("pactManage"));
			String pactManagePhone = CommonUtils.checkNull(request.getParamValue("pactManagePhone"));
			
			//需求说明
			String visitDate = CommonUtils.checkNull(request.getParamValue("visitDate"));//拜访日期
			String marketInfo = CommonUtils.checkNull(request.getParamValue("marketInfo"));//市场信息
			String configRequire = CommonUtils.checkNull(request.getParamValue("configRequire"));//配置要求
			String fleetDiscount = CommonUtils.checkNull(request.getParamValue("fleetDiscount"));//大客户要求折让
			String ocandfp = CommonUtils.checkNull(request.getParamValue("ocandfp"));//其他竞争车型和优惠政策
			
			
			// Sequence生成ID
			String fleetId = SequenceManager.getSequence("");
			
			// session中取得经销商公司
			Long dlrCompanyId = logonUser.getCompanyId();
			
			// session中取得车厂公司ID
			String oemCompanyId = logonUser.getOemCompanyId();
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			Date currTime = new Date(System.currentTimeMillis());
			
			TmFleetPO po = new TmFleetPO();
			po.setFleetId(new Long(fleetId));
			po.setFleetName(fleetName);
			po.setIsPact(Integer.parseInt(isPact)) ;
			
			po.setPactManage(pactManage) ;
			po.setPactManagePhone(pactManagePhone) ;
			
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
//			po.setSeriesId(Long.parseLong(seriesId));
//			po.setSeriesCount(Integer.parseInt(seriesCount));
			// 设置状态为待确认
			po.setStatus(Constant.FLEET_INFO_TYPE_02);
			po.setDlrCompanyId(dlrCompanyId);
			po.setOemCompanyId(new Long(oemCompanyId));
			po.setIsDel(Integer.valueOf(Constant.IS_DEL_00));
			po.setSubmitDate(currTime);
			po.setSubmitUser(logonUser.getUserId());
			
			appDao.saveFleetInfo(po);
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
			if(materialIds.length!=0){
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
			
			act.setRedirect(fleetInfoAppInitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"集团客户信息报备新增提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getPactType() {
		String pactId = CommonUtils.checkNull(request.getParamValue("pactId")) ;
		
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("status", Constant.STATUS_ENABLE.toString()) ;
		map.put("isAllowApply", Constant.IF_TYPE_YES.toString()) ;
		map.put("pactId", pactId) ;
		
		FleetInfoAppDao appdao = new FleetInfoAppDao();
		
		List<Map<String, Object>> pactList = appdao.getPactInfo(map) ;
		
		act.setOutData("pactType", pactList.get(0).get("PARENT_FLEET_TYPE").toString()) ;
	}
	/**
	 * FUNCTION		:	实销信息上报:查询物料列表
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-18
	 */
	public void selectMaterialList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			act.setForword(SELECTMATERIALURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	实销信息上报:查询物料结果展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-18
	 */
	public void getMaterialList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			Long dlrCompanyId =null;
			Long oemCompanyId=null;
			
			if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType().toString())){
				 dlrCompanyId = logonUser.getCompanyId();
				 oemCompanyId = new Long(logonUser.getOemCompanyId());
			}
			String name = CommonUtils.checkNull(request.getParamValue("materialName"));
			String code = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String selectedVehicle= CommonUtils.checkNull(request.getParamValue("selectedVehicle"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = FleetInfoAppDao.getMaterialList(name,code,selectedVehicle,dlrCompanyId,oemCompanyId,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void chooseMaterialInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CHOOSE_MATERIAL_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "退车车辆选择初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
