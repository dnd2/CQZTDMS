package com.infodms.dms.actions.sales.fleetmanage.fleetInfoManage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.usermng.UserManage;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.FleetInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetEditLogDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetInfoAppDao;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmFleetRequestDetailPO;
import com.infodms.dms.po.TtFleetEditLogPO;
import com.infodms.dms.po.TtFleetLogDetailPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:FleetInfoModifyApp.java</p>
 *
 * <p>Description: 集团客户报备更改申请业务逻辑层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-13</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class FleetInfoModifyApp {

	private static Logger logger = Logger.getLogger(FleetInfoModifyApp.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 集团客户报备更改申请初始化页面
	private final String fleetModifyAppInitUrl = "/jsp/sales/fleetmanage/fleetinfomanage/fleetInfoModifyAppDLR.jsp";
	//车厂修改集团客户信息
	private final String oemfleetModifyAppInitUrl = "/jsp/sales/fleetmanage/fleetinfomanage/oemFleetInfoModifyAppDLR.jsp";
	
	// 集团客户报备更改申请初始化页面
	private final String fleetModifyAppUrl = "/jsp/sales/fleetmanage/fleetinfomanage/modifyFleetInfoDLR.jsp";
	//集团客户报备修改(oem)
	private final String oemfleetModifyAppUrl = "/jsp/sales/fleetmanage/fleetinfomanage/oemModifyFleetInfoDLR.jsp";
	
	/**
	 * 集团客户报备更改申请初始化
	 */
	public void modifyAppInit(){
		try{
			act.setForword(fleetModifyAppInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"集团客户报备更改申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户报备更改申请查询
	 */
	public void queryFleetInfoForModify(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));  	    //客户名称
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));	    //客户类型
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));       //提报开始日期
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));           //提报结束日期
//			String appStatus = CommonUtils.checkNull(request.getParamValue("appStatus"));       //报备状态
			String dlrCompanyId = CommonUtils.checkNull(logonUser.getCompanyId());              //经销商公司
			
			
			// 传入查询条件
			FleetInfoBean fibean = new FleetInfoBean();
			fibean.setFleetName(fleetName);
			fibean.setFleetType(fleetType);
			fibean.setBeginTime(beginTime);
			fibean.setEndTime(endTime);
			fibean.setDlrCompanyId(dlrCompanyId);
			fibean.setStatus(String.valueOf(Constant.FLEET_INFO_TYPE_03));
			
			FleetInfoAppDao appdao = new FleetInfoAppDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = appdao.queryFleetInfoForModify(fibean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
			act.setForword(fleetModifyAppInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备更改申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void oemQueryFleetInfoForModify(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));  	    //客户名称
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));	    //客户类型
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));       //提报开始日期
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));           //提报结束日期
			String dlrCompanyId = CommonUtils.checkNull(logonUser.getCompanyId());              //经销商公司
			
			// 传入查询条件
			FleetInfoBean fibean = new FleetInfoBean();
			fibean.setFleetName(fleetName);
			fibean.setFleetType(fleetType);
			fibean.setBeginTime(beginTime);
			fibean.setEndTime(endTime);
			fibean.setDlrCompanyId(dlrCompanyId);
			fibean.setStatus(String.valueOf(Constant.FLEET_INFO_TYPE_03));
			
			FleetInfoAppDao appdao = new FleetInfoAppDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = appdao.oemQueryFleetInfoForModify(fibean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);
			
			act.setForword(oemfleetModifyAppInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备更改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户报备更改初始化
	 */
	public void modifyFleetInfo(){
		try{
			// 取得页面传入的需更新对象的Id
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));  
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			
			//查询车系  add by lishuai
			String poseId = String.valueOf(logonUser.getPoseId());
			String level = Constant.GROUP_LEVEL_02;
			String companyId = String.valueOf(logonUser.getOemCompanyId());
			List<Map<String, Object>> list = MaterialGroupManagerDao.getGroupDropDownBox(poseId, level, companyId);
			
			// 根据ID查询集团客户信息
			Map<String, Object> fleetMap = appDao.getFleetInfobyId(fleetId);
			
			//根据集团客户主表的id查询子表需求说明中的内容
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			act.setOutData("tfrdMapList", tfrdMapList);
			act.setOutData("fleetMap", fleetMap);
			act.setOutData("list", list);
			act.setForword(fleetModifyAppUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备更改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户报备更改初始化(oem)
	 */
	public void oemModifyFleetInfo(){
		try{
			// 取得页面传入的需更新对象的Id
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));  
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			
			
			// 根据ID查询集团客户信息
			Map<String, Object> fleetMap = appDao.getFleetInfobyId(fleetId);
			
			act.setOutData("fleetMap", fleetMap);

			act.setForword(oemfleetModifyAppUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备更改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户报备更改
	 */
	public void saveModifyInfo(){
		try{
			
			//CommonUtils.checkNull() 校验是否为空
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));  	        //客户ID
			
			
			// 从页面取得更新后的信息
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
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));         //车系名称  
			//String seriesCount = CommonUtils.checkNull(request.getParamValue("seriesCount"));   //需求数量
			//String visitDate = CommonUtils.checkNull(request.getParamValue("visitDate"));       //拜访日期
			
			
			//需求说明
			String visitDate = CommonUtils.checkNull(request.getParamValue("visitDate"));//拜访日期
			String marketInfo = CommonUtils.checkNull(request.getParamValue("marketInfo"));//市场信息
			String configRequire = CommonUtils.checkNull(request.getParamValue("configRequire"));//配置要求
			String fleetDiscount = CommonUtils.checkNull(request.getParamValue("fleetDiscount"));//大客户要求折让
			String ocandfp = CommonUtils.checkNull(request.getParamValue("ocandfp"));//其他竞争车型和优惠政策
			
			String isPact = CommonUtils.checkNull(request.getParamValue("isPact"));       
			String pactId = CommonUtils.checkNull(request.getParamValue("pactId"));  
			
			String pactManage = CommonUtils.checkNull(request.getParamValue("pactManage"));       
			String pactManagePhone = CommonUtils.checkNull(request.getParamValue("pactManagePhone"));
			
			String editId = "";
			
			// 根据fleetId查询日志表中是否存在审核状态为未确认的记录
			FleetEditLogDao logDao = new FleetEditLogDao();
			Map<String, Object> logMap = logDao.getRecordByFleetId(fleetId);
			
			// 如果存在记录则更新新纪录
			if(null!=logMap&&logMap.size()!=0){
				editId = CommonUtils.checkNull(logMap.get("EDIT_ID"));
				TtFleetEditLogPO po1 = new TtFleetEditLogPO();
				po1.setEditId(new Long(editId));
				
				TtFleetEditLogPO po2 = new TtFleetEditLogPO();
				if(null!=fleetType&&!"".equals(fleetType)){
					po2.setFleetType(new Integer(fleetType));
				}
				
				po2.setDataType(Constant.DATA_TYPE_01);
				po2.setSeriesId(Long.parseLong(seriesId));//需求车系
				//po2.setSeriesCount(Integer.parseInt(seriesCount));//需求数量
				//市场信息
				if(!"".equals(marketInfo)){
					po2.setMarketInfo(marketInfo);
				}
				//配置要求
				if(!"".equals(configRequire)){
					po2.setConfigRquire(configRequire);
				}
				//大客户要求折让
				if(!"".equals(fleetDiscount)){
					po2.setFleetreqDiscount(fleetDiscount);
				}
				//其他竞争车型和优惠政策
				if(!"".equals(ocandfp)){
					po2.setOthercompFavorpol(ocandfp);
				}
				
				po2.setIsPact(Integer.parseInt(isPact)) ;
				
				if(Constant.IF_TYPE_YES.toString().equals(isPact)) {
					po2.setPactId(Long.parseLong(pactId)) ;
				} else {
					po2.setPactId(new Long("-1")) ;
				}
				
				po2.setPactManage(pactManage) ;
				po2.setPactManagePhone(pactManagePhone) ;
				
				if(null!=mainBusiness&&!"".equals(mainBusiness)){
					po2.setMainBusiness(new Integer(mainBusiness));
				}
				
				if(null!=fundSize&&!"".equals(fundSize)){
					po2.setFundSize(new Integer(fundSize));
				}
				
				if(null!=staffSize&&!"".equals(staffSize)){
					po2.setStaffSize(new Integer(staffSize));
				}
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if(!"".equals(visitDate)){
					po2.setVisitDate(formatter.parse(visitDate));
				}
				
				if(null!=purpose&&!"".equals(purpose)){
					po2.setPurpose(new Integer(purpose));
				}
				
				if(null!=region&&!"".equals(region)){
					po2.setRegion(region);
				}
				
				po2.setZipCode(zipCode);
				po2.setAddress(address);
				po2.setMainLinkman(mainLinkman);
				if(null!=mainJob&&!"".equals(mainJob)){
					po2.setMainJob(mainJob);
				}
				po2.setMainPhone(mainPhone);
				
				po2.setMainEmail(mainEmail);
				
				if(null!=otherLinkman&&!"".equals(otherLinkman)){
					po2.setOtherLinkman(otherLinkman);
				}
				
				
				if(null!=otherJob&&!"".equals(otherJob)){
					po2.setOtherJob(otherJob);
				}
				
				if(null!=otherPhone&&!"".equals(otherPhone)){
					po2.setOtherPhone(otherPhone);
				}
				
				if(null!=otherEmail&&!"".equals(otherEmail)){
					po2.setOtherEmail(otherEmail);
				}
				
				po2.setStatus(Constant.FLEET_INFO_TYPE_02);
				
				if(null!=reqRemark&&!"".equals(reqRemark)){
					po2.setReqRemark(reqRemark);
				}
				
				po2.setUpdateBy(logonUser.getUserId());
				po2.setUpdateDate(new Date(System.currentTimeMillis()));
				
				logDao.updatelog(po1,po2);
				
				//start yinshunhui
				//获取明细表中的数据并且保存
				String[] materialIds = request.getParamValues("materialIds");
				String[] amounts=request.getParamValues("amounts");
				String[]describes=request.getParamValues("describes");
				for(int i=0;i<materialIds.length;i++){
					TtFleetLogDetailPO tfrd=new TtFleetLogDetailPO();
					UserManageDao dao=UserManageDao.getInstance();
					tfrd.setDetailId(new Long(SequenceManager.getSequence("")));
					tfrd.setMaterId(new Long(materialIds[i]));
					tfrd.setTtEditLogId(po2.getEditId());
					tfrd.setAmount(new Long(amounts[i]));
					tfrd.setDiscribe(describes[i]);
					dao.insert(tfrd);
				}
				//end
				
			}else{
				// 如果日志表中不存在则同时插入新旧两条记录
				FleetInfoAppDao appDao = new FleetInfoAppDao();
				Map<String, Object> fleetMap = appDao.getFleetInfobyId(fleetId);
				
				// 取出TM_FLEET中对应的信息插入TT_FLEET_EDIT_LOG日志表中作为旧记录
				String oemCompanyId1 = CommonUtils.checkNull(fleetMap.get("OEM_COMPANY_ID"));     //车厂公司
				String dlrCompanyId1 = CommonUtils.checkNull(fleetMap.get("DLR_COMPANY_ID"));     //经销商公司
				String fleetName1 = CommonUtils.checkNull(fleetMap.get("FLEET_NAME"));            //集团客户名称
				String fleetType1 = CommonUtils.checkNull(fleetMap.get("FLEET_TYPE"));            //集团客户类型
				String mainBusiness1 = CommonUtils.checkNull(fleetMap.get("MAIN_BUSINESS"));      //主要业务
				String fundSize1 = CommonUtils.checkNull(fleetMap.get("FUND_SIZE"));			  //资金规模
				String staffSize1 = CommonUtils.checkNull(fleetMap.get("STAFF_SIZE"));            //人员规模
				String purpose1 = CommonUtils.checkNull(fleetMap.get("PURPOSE"));                 //购车用途
				String region1 = CommonUtils.checkNull(fleetMap.get("REGION"));                   //区域
				String zipCode1 = CommonUtils.checkNull(fleetMap.get("ZIP_CODE"));                //邮编
				String address1 = CommonUtils.checkNull(fleetMap.get("ADDRESS"));                 //详细地址
				String mainLinkman1 = CommonUtils.checkNull(fleetMap.get("MAIN_LINKMAN"));        //主要联系人
				String mainJob1 = CommonUtils.checkNull(fleetMap.get("MAIN_JOB"));                //主要联系人职务
				String mainPhone1 = CommonUtils.checkNull(fleetMap.get("MAIN_PHONE"));            //主要联系人电话
				String mainEmail1 = CommonUtils.checkNull(fleetMap.get("MAIN_EMAIL"));            //主要联系人邮件
				String otherLinkman1 = CommonUtils.checkNull(fleetMap.get("OTHER_LINKMAN"));      //其他联系人
				String otherJob1 = CommonUtils.checkNull(fleetMap.get("OTHER_JOB"));              //其他联系人职务
				String otherPhone1 = CommonUtils.checkNull(fleetMap.get("OTHER_PHONE"));          //其他联系人电话
				String otherEmail1 = CommonUtils.checkNull(fleetMap.get("OTHER_EMAIL"));          //其他联系人邮件
				String reqRemark1 = CommonUtils.checkNull(fleetMap.get("REQ_REMARK"));            //需求说明
				String submitUser1 = CommonUtils.checkNull(fleetMap.get("SUBMIT_USER"));          //提报人
				String isDel1 = CommonUtils.checkNull(fleetMap.get("IS_DEL"));                    //是否删除标识
				String sId = CommonUtils.checkNull(fleetMap.get("SERIES_ID"));
				String sCount = CommonUtils.checkNull(fleetMap.get("SERIES_COUNT")); 
				
				Date submitDate1 = new Date();
				if(null!=fleetMap.get("SUBMIT_DATE")){
					 submitDate1 = (Date)fleetMap.get("SUBMIT_DATE");							  //提报时间
				}
				
				TtFleetEditLogPO logPo = new TtFleetEditLogPO();
				
				editId = SequenceManager.getSequence(""); 
				logPo.setEditId(new Long(editId));
				logPo.setFleetId(new Long(fleetId));
				
				logPo.setIsPact(Integer.parseInt(isPact)) ;
				
				if(Constant.IF_TYPE_YES.toString().equals(isPact)) {
					logPo.setPactId(Long.parseLong(pactId)) ;
				}
				
				logPo.setPactManage(pactManage) ;
				logPo.setPactManagePhone(pactManagePhone) ;
				
				if(null!=oemCompanyId1&&!"".equals(oemCompanyId1)){
					logPo.setOemCompanyId(new Long(oemCompanyId1));
				}
				if(null!=dlrCompanyId1&&!"".equals(dlrCompanyId1)){
					logPo.setDlrCompanyId(new Long(dlrCompanyId1));
				}
				
				logPo.setFleetName(fleetName1);
				
				if(null!=fleetType1&&!"".equals(fleetType1)){
					logPo.setFleetType(new Integer(fleetType1));
				}
				
				logPo.setDataType(Constant.DATA_TYPE_02);
				logPo.setSeriesId(Long.parseLong(sId));
				logPo.setSeriesCount(Integer.parseInt(sCount));
				
				if(null!=mainBusiness1&&!"".equals(mainBusiness1)){
					logPo.setMainBusiness(new Integer(mainBusiness1));
				}
				
				if(null!=fundSize1&&!"".equals(fundSize1)){
					logPo.setFundSize(new Integer(fundSize1));
				}
				
				if(null!=staffSize1&&!"".equals(staffSize1)){
					logPo.setStaffSize(new Integer(staffSize1));
				}
				
				if(null!=purpose1&&!"".equals(purpose1)){
					logPo.setPurpose(new Integer(purpose1));
				}
				
				if(null!=region1&&!"".equals(region1)){
					logPo.setRegion(String.valueOf(region1));
				}
				
				logPo.setZipCode(zipCode1);
				
				if(null!=address1&&!"".equals(address1)){
					logPo.setAddress(address1);
				}
				
				logPo.setMainLinkman(mainLinkman1);
				
				if(null!=mainJob1&&!"".equals(mainJob1)){
					logPo.setMainJob(mainJob1);
				}
				
				logPo.setMainPhone(mainPhone1);
				
				if(null!=mainEmail1&&!"".equals(mainEmail1)){
					logPo.setMainEmail(mainEmail1);
				}
				
				if(null!=otherLinkman1&&!"".equals(otherLinkman1)){
					logPo.setOtherLinkman(otherLinkman1);
				}
				
				
				if(null!=otherJob1&&!"".equals(otherJob1)){
					logPo.setOtherJob(otherJob1);
				}
				
				
				if(null!=otherPhone1&&!"".equals(otherPhone1)){
					logPo.setOtherPhone(otherPhone1);
				}
				
				
				if(null!=otherEmail1&&!"".equals(otherEmail1)){
					logPo.setOtherEmail(otherEmail1);
				}
				
				logPo.setStatus(Constant.FLEET_INFO_TYPE_02);
				
				if(null!=reqRemark1&&!"".equals(reqRemark1)){
					logPo.setReqRemark(reqRemark1);
				}
				
				if(null!=submitDate1&&!"".equals(submitDate1)){
					logPo.setSubmitDate(submitDate1);
				}
				
				if(null!=submitUser1&&!"".equals(submitUser1)){
					logPo.setSubmitUser(new Long(submitUser1));
				}
				
				logPo.setIsDel(new Integer(isDel1));
				logPo.setCreateBy(logonUser.getUserId());
				logPo.setCreateDate(new Date(System.currentTimeMillis()));
				
				logDao.inserLog(logPo);
				
				// 将页面取得的信息作为新纪录存入日志表中
				TtFleetEditLogPO elpo = new TtFleetEditLogPO();
				elpo.setEditId(new Long(SequenceManager.getSequence("")));
				elpo.setFleetId(new Long(fleetId));
				
				elpo.setIsPact(Integer.parseInt(isPact)) ;
				
				if(Constant.IF_TYPE_YES.toString().equals(isPact)) {
					elpo.setPactId(Long.parseLong(pactId)) ;
				}
				
				elpo.setPactManage(pactManage) ;
				elpo.setPactManagePhone(pactManagePhone) ;
				
				if(null!=oemCompanyId1&&!"".equals(oemCompanyId1)){
					elpo.setOemCompanyId(new Long(oemCompanyId1));
				}
				if(null!=dlrCompanyId1&&!"".equals(dlrCompanyId1)){
					elpo.setDlrCompanyId(new Long(dlrCompanyId1));
				}
				
				elpo.setFleetName(fleetName1);
				
				if(null!=fleetType&&!"".equals(fleetType)){
					elpo.setFleetType(new Integer(fleetType));
				}
				
				elpo.setDataType(Constant.DATA_TYPE_01);
				elpo.setSeriesId(Long.parseLong(seriesId));
				//elpo.setSeriesCount(Integer.parseInt(seriesCount));
				
				if(null!=mainBusiness&&!"".equals(mainBusiness)){
					elpo.setMainBusiness(new Integer(mainBusiness));
				}
				
				if(null!=fundSize&&!"".equals(fundSize)){
					elpo.setFundSize(new Integer(fundSize));
				}
				
				if(null!=staffSize&&!"".equals(staffSize)){
					elpo.setStaffSize(new Integer(staffSize));
				}
				
				if(null!=purpose&&!"".equals(purpose)){
					elpo.setPurpose(new Integer(purpose));
				}
				
				if(null!=region&&!"".equals(region)){
					elpo.setRegion(String.valueOf(region));
				}
				
				elpo.setZipCode(zipCode);
				
				if(null!=address&&!"".equals(address)){
					elpo.setAddress(address);
				}
				
				elpo.setMainLinkman(mainLinkman);
				
				if(null!=mainJob&&!"".equals(mainJob)){
					elpo.setMainJob(mainJob);
				}
				elpo.setMainPhone(mainPhone);
				
				if(null!=mainEmail&&!"".equals(mainEmail)){
					elpo.setMainEmail(mainEmail);
				}
				
				if(null!=otherLinkman&&!"".equals(otherLinkman)){
					elpo.setOtherLinkman(otherLinkman);
				}
				
				
				if(null!=otherJob&&!"".equals(otherJob)){
					elpo.setOtherJob(otherJob);
				}
				
				if(null!=otherPhone&&!"".equals(otherPhone)){
					elpo.setOtherPhone(otherPhone);
				}
				
				
				if(null!=otherEmail&&!"".equals(otherEmail)){
					elpo.setOtherEmail(otherEmail);
				}
				
				elpo.setStatus(Constant.FLEET_INFO_TYPE_02);
				
				if(null!=reqRemark&&!"".equals(reqRemark)){
					elpo.setReqRemark(reqRemark);
				}
				
				if(null!=submitDate1&&!"".equals(submitDate1)){
					elpo.setSubmitDate(submitDate1);
				}
				
				if(null!=submitUser1&&!"".equals(submitUser1)){
					elpo.setSubmitUser(new Long(submitUser1));
				}
				
				elpo.setIsDel(new Integer(isDel1));
				elpo.setCreateBy(logonUser.getUserId());
				elpo.setCreateDate(new Date(System.currentTimeMillis()));
				
				logDao.inserLog(elpo);
			}
			
			act.setRedirect(fleetModifyAppInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"集团客户报备更改申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * (oem)修改集团客户信息
	 */
	public void oemSaveModifyInfo(){
		try{
			String fleetName = request.getParamValue("fleetName");	//集团客户名称
			String fleetId = request.getParamValue("fleetId");		//集团客户ID
			TmFleetPO po = new TmFleetPO();
			TmFleetPO tp = new TmFleetPO();
			po.setFleetName(fleetName);
			tp.setFleetId(Long.parseLong(fleetId));
			FleetInfoAppDao dao = new FleetInfoAppDao();
			dao.update(tp, po);
			act.setRedirect(oemfleetModifyAppInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"集团客户报备更改申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void oemModifyAppInit(){
		try{
			act.setForword(oemfleetModifyAppInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"集团客户报备更改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
}
