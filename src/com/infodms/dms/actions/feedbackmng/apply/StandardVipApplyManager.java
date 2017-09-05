package com.infodms.dms.actions.feedbackmng.apply;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtIfStandardAuditPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


/**
 * @Title: 
 *
 * @Description:InfoFrame3.0.V01
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-17
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class StandardVipApplyManager {

	public Logger logger = Logger.getLogger(StandardVipApplyManager.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/feedbackMng/apply/queryStanderVipApply.jsp";
	private final String addInitUrl = "/jsp/feedbackMng/apply/addStanderVipApply.jsp";
	private final String infoUrl = "/jsp/feedbackMng/apply/infoStanderVipApply.jsp";
	private final String updateUrl = "/jsp/feedbackMng/apply/updateStanderVipApply.jsp";
	private final String vinUrl = "/jsp/feedbackMng/apply/showVin.jsp";
	
	/**
	 * 合格证/VIP卡申请表初始化
	 */
	public void standardVipApplyInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId()); //取车系
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡申请表初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP卡申请表查询
	 */
	public void queryStandardVip(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//CommonUtils.checkNull() 校验是否为空, CommonUtils里有一些基本的校验方法   
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));  		//取工单号
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));		   		//VIN
			String stType = CommonUtils.checkNull(request.getParamValue("stType"));	   		//申请的类型， 合格证或VIP
			String stAction = CommonUtils.checkNull(request.getParamValue("stAction"));		//操作类型，补办，修复，更换
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));	//创建开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));		//创建结束时间
			String vehicleModel = CommonUtils.checkNull(request.getParamValue("vehicleSeriesList"));   //车系
			String dealerId = String.valueOf(logonUser.getDealerId());	//取经销商ID
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			StandardVipApplyManagerBean smb = new StandardVipApplyManagerBean();     //参数bean 查询条件用
			smb.setOrderId(orderId);
			smb.setVin(vin);
			smb.setStType(stType);
			smb.setStAction(stAction);
			smb.setBeginTime(beginTime);
			smb.setEndTime(endTime);
			smb.setDealerId(dealerId);
			smb.setVehicleModel(vehicleModel);
			//modify by xiayanpeng begin 
			smb.setLogonUser(logonUser);
			//modify by xiayanpeng end
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = smDao.queryStandardVip(smb,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡申请表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP卡新增初始化
	 */
	public void addStandderVipInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = String.valueOf(logonUser.getDealerId());  //经销商ID 
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			Map<String, Object> dealerInfo = smDao.getDealerInfo(dealerId);
			act.setOutData("dealerInfo", dealerInfo);
			act.setForword(addInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP卡新增
	 */
	public void addStandderVip(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		try {
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));   //服务中心联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));		    //服务中心联系电话
			String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));	//服务中心邮编
			String customerAdress = CommonUtils.checkNull(request.getParamValue("customerAdress"));	//服务中心地址
			String stType = CommonUtils.checkNull(request.getParamValue("stType"));	 //工单类型
			String stAction = CommonUtils.checkNull(request.getParamValue("stAction"));	  //操作类系那个
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));	//车辆识别码(VIN)
			String dealerId = String.valueOf(logonUser.getDealerId());  //经销商ID 
			String stContent = CommonUtils.checkNull(request.getParamValue("stContent"));	//申请内容
			//String phonoName = request.getParamValue("phonoName");  上传的相应的照片 以后会用公共方法
			TtIfStandardPO po = new TtIfStandardPO();			//把数据放到po里准备插入
			po.setDealerId(new Long(dealerId));
			po.setVin(vin);
			po.setStType(new Integer(stType));
			po.setStAction(new Integer(stAction));
			po.setStStatus(Constant.SERVICEINFO_VIP_STATUS_UNREPORT);
			po.setStContent(stContent);
			po.setLinkMan(linkMan);
			po.setTel(tel);
			po.setZipCode(zipCode);
			po.setAddress(customerAdress);
			po.setCreateBy(logonUser.getUserId());
			po.setCompanyId(companyId);
			po.setCreateDate(new Date(System.currentTimeMillis()));
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			
			String ywzj = smDao.insertStandderVip(po);//插入并获得主键id,插入附件时用
			
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId()); //取车系
			//act.setRedirect(initUrl);		//禁止刷新插入，修改操作
			//附近功能：
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	
			standardVipApplyInit();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡新增初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP卡提报
	 */
	public void putForwordStandardVip(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String[] orderIds = request.getParamValues("orderIds");     //申请单号  数组类型
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			//循环遍历修改TT_IF_STANDARD表ST_STATUS字段
			if(orderIds!=null || !orderIds.equals("")){
				for(int i=0;i<orderIds.length;i++){
					TtIfStandardPO tp = new TtIfStandardPO();
					TtIfStandardPO po = new TtIfStandardPO();
					TtIfStandardAuditPO ap = new TtIfStandardAuditPO();   
					ap.setOrderId(orderIds[i]);
					ap.setAuditDate(new Date(System.currentTimeMillis()));
					ap.setAuditBy(logonUser.getUserId());
					ap.setAuditStatus(Constant.SERVICEINFO_VIP_STATUS_REPORTED);
					ap.setOrgId(logonUser.getOrgId());
					po.setOrderId(orderIds[i]);
					tp.setStDate(new Date(System.currentTimeMillis()));
					tp.setStStatus(Constant.SERVICEINFO_VIP_STATUS_REPORTED);
					smDao.putFOrwordStandardVip(po, tp, ap);
				}
			}
			act.setOutData("returnValue", 1);  //前台回调函数的参数
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"合格证/VIP卡提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 合格证/VIP卡删除
	 */
	public void deleteStandardVip(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String[] orderIds = request.getParamValues("orderIds");     //申请单号  数组类型
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			//循环遍历修改TT_IF_STANDARD表IS_DEL字段
			if(orderIds!=null || !orderIds.equals("")){
				for(int i=0;i<orderIds.length;i++){
					TtIfStandardPO tp = new TtIfStandardPO();
					TtIfStandardPO po = new TtIfStandardPO();
					tp.setIsDel(new Integer(Constant.IS_DEL_01));	   	//删除标示
					po.setOrderId(orderIds[i]);
					smDao.deleteStandardVip(po, tp);
				}
			}
			act.setOutData("returnValue", 1);  //前台回调函数的参数
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"合格证/VIP卡");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP卡详细页
	 */
	public void queryStandardVipApplyInfo(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");    //工单号
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			Map<String, Object> svInfo = smDao.getStandardVipInfo(orderId);     //查询详细
			List<Map<String, Object>> list = smDao.getAuditInfo(orderId);
			String id = String.valueOf(svInfo.get("ID"));
			List<FsFileuploadPO> fileList=smDao.queryAttachFileInfo(id);
			act.setOutData("fileList", fileList);
			act.setOutData("svInfo", svInfo);
			act.setOutData("auditList", list);
			act.setOutData("flag", request.getParamValue("flag"));
			act.setForword(infoUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP卡修改初始化
	 */
	public void updateStandardVipApply(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");    //工单号
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			//获得附件列表
			Map<String, Object> svInfo = smDao.getStandardVipInfo(orderId);     //查询详细
			String id = String.valueOf(svInfo.get("ID"));
			List<FsFileuploadPO> fileList= smDao.queryAttachFileInfo(id);
			act.setOutData("fileList", fileList);
			act.setOutData("svInfo", svInfo);
			act.setForword(updateUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡修改初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 合格证/VIP卡修改
	 */
	public void updateStandderVip(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = request.getParamValue("id");
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));   //服务中心联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));		    //服务中心联系电话
			String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));	//服务中心邮编
			String customerAdress = CommonUtils.checkNull(request.getParamValue("customerAdress"));	//服务中心地址
			String stType = CommonUtils.checkNull(request.getParamValue("stType"));	 //工单类型
			String stAction = CommonUtils.checkNull(request.getParamValue("stAction"));	  //操作类系那个
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));	//车辆识别码(VIN)
			String dealerId = String.valueOf(logonUser.getDealerId());  //经销商ID 
			String stContent = CommonUtils.checkNull(request.getParamValue("stContent"));	//申请内容
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));	//工单号
			//String phonoName = request.getParamValue("phonoName");  上传的相应的照片 以后会用公共方法
			TtIfStandardPO tp = new TtIfStandardPO();
			tp.setOrderId(orderId);
			TtIfStandardPO po = new TtIfStandardPO();			//把数据放到po里准备更新
			po.setDealerId(new Long(dealerId));
			po.setVin(vin);
			po.setStType(new Integer(stType));
			po.setStAction(new Integer(stAction));
			po.setStStatus(Constant.SERVICEINFO_VIP_STATUS_UNREPORT);
			po.setStContent(stContent);
			po.setLinkMan(linkMan);
			po.setTel(tel);
			po.setZipCode(zipCode);
			po.setAddress(customerAdress);
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date(System.currentTimeMillis()));
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			smDao.updateStandderVip(tp,po);
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId()); //取车系
			//act.setRedirect(initUrl);		//禁止刷新插入，修改操作
			//附近功能：
			String ywzj = id;
			String[] fjids = request.getParamValues("fjid");
			//String[] uploadFjid = request.getParamValues("uploadFjid");
			FileUploadManager.delAllFilesUploadByBusiness(ywzj,fjids);//删除附件
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	//新添加附件
			standardVipApplyInit();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"合格证/VIP卡修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 弹出页显示车辆信息
	 */
	public void showVinList(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			String commond = CommonUtils.checkNull(request.getParamValue("commond"));
			if(commond.equals("1")){
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));//VIN
				String customerName = CommonUtils.checkNull(request.getParamValue("customerName"));//车主姓名
				StandardVipApplyManagerBean smb = new StandardVipApplyManagerBean();
				smb.setVin(vin);
				smb.setCustomerName(customerName);
				StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
				PageResult<Map<String, Object>> ps = smDao.queryVIN(smb,curPage,Constant.PAGE_SIZE);
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}
			act.setRedirect(vinUrl);	
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 根据用户手动输入的VIN查询车辆信息
	 */
	public void queryVchByVin(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String vin = request.getParamValue("vin");
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			PageResult<Map<String,Object>> ps = smDao.queryVchByVIN(vin);
			if(ps.getTotalRecords()>0){
				act.setOutData("flag", true);
				Map<String,Object> map = ps.getRecords().get(0);
				act.setOutData("vin", map.get("VIN"));
				act.setOutData("mobile", map.get("MOBILE")!=null?map.get("MOBILE"):"");
				act.setOutData("ctm_name", map.get("CUSTOMER_NAME")!=null?map.get("CUSTOMER_NAME"):"");
				act.setOutData("product_date", map.get("PRODUCT_DATE")!=null?map.get("PRODUCT_DATE"):"");
				act.setOutData("group_name", map.get("GROUP_NAME")!=null?map.get("GROUP_NAME"):"");
				act.setOutData("address", map.get("ADDRESS_DESC")!=null?map.get("ADDRESS_DESC"):"");
				act.setOutData("cert_no", map.get("CERT_NO")!=null?map.get("CERT_NO"):"");
				act.setOutData("mile", map.get("HISTORY_MILE"));
				act.setOutData("purchased_date", map.get("PURCHASED_DATE")!=null?map.get("PURCHASED_DATE"):"");
				act.setOutData("engine_no", map.get("ENGINE_NO")!=null?map.get("ENGINE_NO"):"");
			}
			else act.setOutData("flag", false);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
