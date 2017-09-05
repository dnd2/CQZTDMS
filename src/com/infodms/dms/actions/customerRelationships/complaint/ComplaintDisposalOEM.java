package com.infodms.dms.actions.customerRelationships.complaint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ComplaintDisposalBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.customerRelationships.ComplaintAuditDao;
import com.infodms.dms.dao.customerRelationships.ComplaintDisposalDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrComplaintsAuditPO;
import com.infodms.dms.po.TtCrComplaintsPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:ComplaintDisposalOEM.java</p>
 *
 * <p>Description: 客户投诉处理(总部)</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-5-31</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ComplaintDisposalOEM {
	
	private static Logger logger = Logger.getLogger(ComplaintDisposalOEM.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 客户投诉处理(总部)初始化页面
	private final String complainDisposalOEMInitUrl = "/jsp/customerRelationships/complaintDisposal/complaintDisposalOEM.jsp";
	
	// 客户投诉处理(总部)新增初始化页面
	private final String complainAddInitUrl = "/jsp/customerRelationships/complaintDisposal/complaintDisposalAddOEM.jsp";
	
	// 客户投诉处理(处理)处理页面(未分配)
	private final String complainDisposalNotAssignUrl = "/jsp/customerRelationships/complaintDisposal/complaintDisposalEditOEM.jsp";
	
	// 客户投诉处理(处理)处理页面
	private final String complainDisposalAssignUrl = "/jsp/customerRelationships/complaintDisposal/complaintDisposalAssignOEM.jsp";
	
	// vin 弹出页面
	private final String vinUrl = "/jsp/customerRelationships/complaintDisposal/selectVinInfo.jsp";
	
	/**
	 * 客户投诉处理(总部)初始化
	 */
	public void complaintDisposalOemInit(){
		try{
			act.setForword(complainDisposalOEMInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 客户投诉处理(总部)查询
	 */
	public void queryComplaintDisposalOem(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  				//客户姓名
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));	            		//客户电话
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));   		//投诉时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));       		//投诉时间
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   		//车型
			String ownOrgId = CommonUtils.checkNull(request.getParamValue("ownOrgId"));     		//所属区域
			String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId")); 	//投诉经销商id
			String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); //投诉经销商code
			String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); 		//投诉来源
			String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   		//投诉等级
			String compType = CommonUtils.checkNull(request.getParamValue("compType"));     		//投诉类型
			String compType2 = CommonUtils.checkNull(request.getParamValue("compType2"));     		//投诉类型2
			String compStatus = CommonUtils.checkNull(request.getParamValue("compStatus")); 		//投诉状态
			String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult")); 		//处理中状态
			String compCode = CommonUtils.checkNull(request.getParamValue("compCode")); 			//投诉编号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo")); 			//车牌号
			
			ComplaintDisposalBean cdbean = new ComplaintDisposalBean();
			cdbean.setLinkman(linkman);
			cdbean.setTel(tel);
			cdbean.setBeginTime(beginTime);
			cdbean.setEndTime(endTime);
			cdbean.setModelCode(modelCode);
			cdbean.setOwnOrgId(ownOrgId);
			cdbean.setCompDealerId(compDealerId);
			cdbean.setCompDealerCode(compDealerCode);
			cdbean.setCompSource(compSource);
			cdbean.setCompLevel(compLevel);
			cdbean.setCompType(compType);
			cdbean.setCompType2(compType2);
			cdbean.setStatus(compStatus);
			cdbean.setAuditResult(auditResult);
			cdbean.setCompCode(compCode);
			cdbean.setLicenseNo(licenseNo);
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = cddao.queryComplaintDisposal(cdbean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
		}catch(Exception e) {
			//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉处理(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增客户投诉初始化
	 */
	public void complaintAdd(){
		try{
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			List<Map<String, Object>> listCheckBox = cddao.getCheckBox();   //多选框
			act.setOutData("ListCheckBox", listCheckBox);
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId()); //取车系
			act.setForword(complainAddInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增客户投诉
	 */
	public void saveComplaint(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			// 客户基本信息
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  				//联系人姓名
			String sex = CommonUtils.checkNull(request.getParamValue("sex"));  		        		//性别
			String birthday = CommonUtils.checkNull(request.getParamValue("birthday"));     		//生日
			String age = CommonUtils.checkNull(request.getParamValue("age"));               		//年龄
			String ownOrgId = CommonUtils.checkNull(request.getParamValue("ownOrgId"));     		//所属区域id
			String ownOrgCode = CommonUtils.checkNull(request.getParamValue("ownOrgCode"));         //所属区域code
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));	            		//客户电话
			String province = CommonUtils.checkNull(request.getParamValue("province"));       		//省份
			String email = CommonUtils.checkNull(request.getParamValue("email"));           		//e_mail
			String city = CommonUtils.checkNull(request.getParamValue("city"));             		//市
			String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));       		//邮编
			String district = CommonUtils.checkNull(request.getParamValue("district"));     		//县
			String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); //投诉经销商Code
			String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId"));     //投诉经销商Id
			String address = CommonUtils.checkNull(request.getParamValue("address"));       		//家庭住址
			String compId = SequenceManager.getSequence("");
			String compCode = SequenceManager.getSequence("TSBH");
			
			// 车辆信息
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));               //vin
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   //车型
			String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));     //发动机号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo"));   //车牌号
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate")); //购车日期
			
			// 投诉内容
			String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); //投诉来源
			String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   //投诉等级
			String comType = CommonUtils.checkNull(request.getParamValue("compType"));           //投诉大类
			String compType = CommonUtils.checkNull(request.getParamValue("compType2"));    //投诉小类
			String compContent = CommonUtils.checkNull(request.getParamValue("compContent"));//投诉内容
			
			// 处理明细
			String strAction = "";
			String[] auditActions = request.getParamValues("auditAction");                     //取得页面的checkbox动作
			if(auditActions!=null){
				for(int i=0;i<auditActions.length;i++){
					strAction+=","+auditActions[i];
				}
			}
			
			String actions = strAction.replaceFirst(",", "");
			String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult"));  //处理结果
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));        //备件编码
			String supplier = CommonUtils.checkNull(request.getParamValue("supplier"));        //上级保供单位
			String auditContent = CommonUtils.checkNull(request.getParamValue("auditContent"));//跟进结果描述       
			
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			Date currTime = new Date(System.currentTimeMillis());
			
			TtCrComplaintsPO po = new TtCrComplaintsPO();
			po.setCompId(new Long(compId));
			po.setCompCode(compCode);
			po.setLinkMan(linkman);
			po.setSex(new Integer(sex));
			po.setAge(new Integer(age));
			po.setAddress(address);
			if(!"".equals(birthday)){
				Date brthdy = formatter.parse(birthday);
				po.setBirthday(brthdy);
			}
			if(!"".equals(purchasedDate)){
				Date purDate = formatter.parse(purchasedDate);
				po.setPurchasedDate(purDate);
			}
			po.setCompContent(compContent);

			if("".equals(compDealerId)&&!"".equals(compDealerCode)){
				compDealerId = cddao.getDealerIdByCode(compDealerCode);
			}
			
			if(!"".equals(compDealerId)){
				po.setCompDealer(new Long(compDealerId));
			}
			
			if(!"".equals(province)){
				po.setProvince(new Integer(province));
			}
			
			if(!"".equals(city)){
				po.setCity(new Integer(city));
			}
			
			if(!"".equals(district)){
				po.setDistrict(new Integer(district));
			}
			
			po.setCompLevel(new Integer(compLevel));
			po.setCompSource(new Integer(compSource));
			po.setCompType(new Integer(compType));
			po.setComType(new Integer(comType));
			po.setEMail(email);
			po.setTel(tel);
			po.setEngineNo(engineNo);
			po.setLicenseNo(licenseNo);
			po.setModelCode(modelCode);
			
			if("".equals(ownOrgId)&&!"".equals(ownOrgCode)){
				ownOrgId = cddao.getOrgIdByCode(ownOrgCode);
			}
			po.setOwnOrgId(ownOrgId);
			po.setVin(vin);
			po.setZipCode(zipCode);
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(currTime);
			po.setStatus(Constant.COMP_STATUS_TYPE_01);
			
			// 插入客户投诉表TT_CR_COMPLAINTS
			cddao.insert(po);
			
			if(!"".equals(actions)&&!"".equals(auditResult)){
				String auditId = SequenceManager.getSequence("");
				ComplaintAuditDao cadao = new ComplaintAuditDao();
				TtCrComplaintsAuditPO capo = new TtCrComplaintsAuditPO(); 
				capo.setAuditAction(actions);
				capo.setAuditResult(new Integer(auditResult));
				capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_01);
				capo.setAuditContent(auditContent);
				capo.setCompId(new Long(compId));
				capo.setCreateBy(logonUser.getUserId());
				capo.setCreateDate(currTime);
				capo.setAuditDate(currTime);
				capo.setId(new Long(auditId));
				capo.setPartCode(partCode);
				capo.setSupplier(supplier);
			     
				// 插入客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT
				cadao.insert(capo);
			}
			
	        
			act.setRedirect(complainDisposalOEMInitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"客户投诉处理(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 确认关闭投诉
	 */
	public void closeComplaint(){
		try{
			String flag = request.getParamValue("flag");
			/*String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  					//联系人姓名
			String sex = CommonUtils.checkNull(request.getParamValue("sex"));  		        			//性别
			String birthday = CommonUtils.checkNull(request.getParamValue("birthday"));     			//生日
			String age = CommonUtils.checkNull(request.getParamValue("age"));               			//年龄
			String ownOrgId = CommonUtils.checkNull(request.getParamValue("ownOrgId"));     			//所属区域
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));	            			//客户电话
			String province = CommonUtils.checkNull(request.getParamValue("province"));     			//省份
			String email = CommonUtils.checkNull(request.getParamValue("email"));           			//e_mail
			String city = CommonUtils.checkNull(request.getParamValue("city"));             			//市
			String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));       			//邮编
			String district = CommonUtils.checkNull(request.getParamValue("district"));     			//县
			String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId")); 		//投诉经销商Id
			String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); 	//投诉经销商code
			String address = CommonUtils.checkNull(request.getParamValue("address"));       			//家庭住址*/
			
			// 车辆信息
			/*String vin = CommonUtils.checkNull(request.getParamValue("vin"));               			//vin
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   			//车型
			String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));     			//发动机号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo"));   			//车牌号
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate")); 		//购车日期*/
			
			// 投诉内容
			/*String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); 			//投诉来源
			String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   			//投诉等级
			String compType = CommonUtils.checkNull(request.getParamValue("compType2"));     			//投诉类型
			String compContent = CommonUtils.checkNull(request.getParamValue("compContent"));			//投诉内容*/
			
			// 处理明细
			/*String strAction = "";
			String[] auditActions = request.getParamValues("auditAction");                     			//取得页面的checkbox动作
			if(auditActions!=null){
				for(int i=0;i<auditActions.length;i++){
					strAction+=","+auditActions[i];
				}
			}
			String actions = strAction.replaceFirst(",", "");
			String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult"));  //处理结果
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));        //备件编码
			String supplier = CommonUtils.checkNull(request.getParamValue("supplier"));        //上级保供单位
//			String auditContent = CommonUtils.checkNull(request.getParamValue("auditContent"));//跟进结果描述*/       
			
			String isReturn = CommonUtils.checkNull(request.getParamValue("callBack"));		   // 是否需要回访
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			
			TtCrComplaintsPO po = new TtCrComplaintsPO();
			/*po.setLinkMan(linkman);
			po.setSex(new Integer(sex));
			po.setAge(new Integer(age));
			po.setAddress(address);
			if(!"".equals(birthday)){
				Date brthdy = formatter.parse(birthday);
				po.setBirthday(brthdy);
			}
			if(!"".equals(purchasedDate)){
				Date purDate = formatter.parse(purchasedDate);
				po.setPurchasedDate(purDate);
			}
			
			if("".equals(compDealerId)&&!"".equals(compDealerCode)){
				compDealerId = cddao.getDealerIdByCode(compDealerCode);
			}
			
			if(!"".equals(compDealerId)){
				po.setCompDealer(new Long(compDealerId));
			}*/
			
			if((Constant.IF_TYPE_NO).equals(isReturn)){
				po.setIsReturn(new Integer(isReturn));
			}else{
				po.setIsReturn(new Integer(Constant.IF_TYPE_YES));
			}
			
			/*po.setCompContent(compContent);
			
			if(!"".equals(province)){
				po.setProvince(new Integer(province));
			}
			
			if(!"".equals(city)){
				po.setCity(new Integer(city));
			}
			
			if(!"".equals(district)){
				po.setDistrict(new Integer(district));
			}
			
			po.setCompLevel(new Integer(compLevel));
			po.setCompSource(new Integer(compSource));
			po.setCompType(new Integer(compType));
			po.setEMail(email);
			po.setTel(tel);
			po.setEngineNo(engineNo);
			po.setLicenseNo(licenseNo);
			po.setModelCode(modelCode);
			po.setOwnOrgId(ownOrgId);
			po.setVin(vin);
			po.setZipCode(zipCode);*/
			
			po.setStatus(Constant.COMP_STATUS_TYPE_03);
			
			
			
			ComplaintAuditDao cadao = new ComplaintAuditDao();
			TtCrComplaintsAuditPO capo = new TtCrComplaintsAuditPO(); 
			//capo.setAuditAction(actions);
			//capo.setAuditResult(new Integer(auditResult));
			capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_05);
			capo.setAuditContent("总部关闭");
			//capo.setPartCode(partCode);
			//capo.setSupplier(supplier);
			
			Date currTime = new Date(System.currentTimeMillis());
			// 标识是0表示新增页面的确认关闭，需插入数据
			/*if(flag.equals("0")){
				// 新增取得投诉表ID和CODE
				String compId = SequenceManager.getSequence("");
				String compCode = SequenceManager.getSequence("TSBH");
				
				// 新增取得投诉明细表的ID
				String auditId = SequenceManager.getSequence("");
				
				// 插入客户投诉表TT_CR_COMPLAINTS
				po.setCompId(new Long(compId));
				po.setCompCode(compCode);
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(currTime);
				
				cddao.insert(po);
				
				
				// 插入客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT
				capo.setCompId(new Long(compId));
				capo.setCreateBy(logonUser.getUserId());
				capo.setCreateDate(currTime);
				capo.setAuditDate(currTime);
				capo.setId(new Long(auditId));
				
				cadao.insert(capo);
				
			}else{*/
				// 取得页面的id和code
				String compId = CommonUtils.checkNull(request.getParamValue("compId")); 
				String compCode = CommonUtils.checkNull(request.getParamValue("compCode")); 
				
				po.setCompCode(compCode);
				po.setUpdateBy(logonUser.getUserId());
				po.setUpdateDate(currTime);
				
				//根据compId更新状态
				TtCrComplaintsPO po1 = new TtCrComplaintsPO();
				po1.setCompId(new Long(compId));
				cddao.update(po1, po);
				
				
				String auditId = SequenceManager.getSequence("");
				capo.setCompId(new Long(compId));
				capo.setCreateBy(logonUser.getUserId());
				capo.setCreateDate(currTime);
				capo.setAuditDate(currTime);
				capo.setId(new Long(auditId));
				
				cadao.insert(capo);
				
			//}
			
			act.setRedirect(complainDisposalOEMInitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 客户投诉处理(总部)处理初始化
	 */
	public void disposalComplaintOem(){
		try{
			String compId = CommonUtils.checkNull(request.getParamValue("compId"));
			
			String status = CommonUtils.checkNull(request.getParamValue("status"));
						
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			
			// 取得客户投诉表的基本信息
			Map<String, Object> complaintMap = cddao.getComplaintById(compId); 
			String comType = "";//投诉大类
			if(null!=complaintMap&&complaintMap.size()>0){
				String compType2 = CommonUtils.checkNull(complaintMap.get("COMP_TYPE"));
				comType = CommonUtils.checkNull(complaintMap.get("COM_TYPE"));
			}
			
			ComplaintAuditDao  cadao = new ComplaintAuditDao();
			
			Map<String, Object> detailMap =null;
			String auditAction = "";
			
			// 根据取得的compId和状态取得相应的明细信息
			if(status.equals(String.valueOf(Constant.COMP_STATUS_TYPE_01))){
				// 取得明细信息
				detailMap = cadao.getAuditDetailByCompId(compId,String.valueOf(Constant.AUDIT_STATUS_TYPE_01));
			}else if(status.equals(String.valueOf(Constant.COMP_STATUS_TYPE_04))){
				detailMap = cadao.getAuditDetailByCompId(compId,String.valueOf(Constant.AUDIT_STATUS_TYPE_04));
			}else{
				detailMap = cadao.getAuditDetailByCompId(compId,String.valueOf(Constant.AUDIT_STATUS_TYPE_03));
				if(null==detailMap){
					detailMap = cadao.getAuditDetailByCompId(compId,String.valueOf(Constant.AUDIT_STATUS_TYPE_02));
				}
			}
			if(null!=detailMap&&detailMap.size()!=0){
				// 解析发生动作的字符串
				auditAction = (String)detailMap.get("AUDIT_ACTION");
				if(auditAction!=null&&!auditAction.equals("")){
					String[] actionArr = auditAction.split(",");
					List actionList =  new ArrayList();
					for (int i = 0;i<actionArr.length;i++){
						actionList.add(actionArr[i]);
						act.setOutData("actionList", actionList);
					}
				}
			}
			
			
			
			List<Map<String, Object>> listCheckBox = cddao.getCheckBox();   //多选框
			
			List<Map<String, Object>> detailList = cadao.getAllDetailByCompId(compId); // 通过compId查询所有的处理明细历史
			
			act.setOutData("ListCheckBox", listCheckBox);
			act.setOutData("detailList", detailList);
			
			List<Map<String, Object>> seriesList = cddao.getSeriesList();
			//act.setOutData("seriesList",seriesList); //取车系
			act.setOutData("seriesList",seriesList); //取车型
			act.setOutData("complaintMap", complaintMap);
			act.setOutData("compType", comType);
			if(detailMap!=null){
				act.setOutData("detailMap", detailMap);
			}
			if(status.equals(String.valueOf(Constant.COMP_STATUS_TYPE_01))){
				act.setForword(complainDisposalNotAssignUrl);
			}else{
				act.setOutData("status", status);
				act.setForword(complainDisposalAssignUrl);
			}
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉处理(总部)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 处理页面中的保存按钮响应
	 */
	public void saveModify(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			// 客户基本信息
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  		//联系人姓名
			String sex = CommonUtils.checkNull(request.getParamValue("sex"));  		        //性别
			String birthday = CommonUtils.checkNull(request.getParamValue("birthday"));     //生日
			String age = CommonUtils.checkNull(request.getParamValue("age"));               //年龄
			String ownOrgId = CommonUtils.checkNull(request.getParamValue("ownOrgId"));     //所属区域
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));	            //客户电话
			String province = CommonUtils.checkNull(request.getParamValue("province"));     //省份
			String email = CommonUtils.checkNull(request.getParamValue("email"));           //e_mail
			String city = CommonUtils.checkNull(request.getParamValue("city"));             //市
			String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));       //邮编
			String district = CommonUtils.checkNull(request.getParamValue("district"));     //县
			String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); //投诉经销商code
			String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId"));     //投诉经销商id
			String address = CommonUtils.checkNull(request.getParamValue("address"));       //家庭住址
			String compId = CommonUtils.checkNull(request.getParamValue("compId"));         //投诉id
			String compCode = CommonUtils.checkNull(request.getParamValue("compCode"));     //投诉编号
			
			// 车辆信息
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));               //vin
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   //车型
			String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));     //发动机号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo"));   //车牌号
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate")); //购车日期
			
			// 投诉内容
			String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); //投诉来源
			String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   //投诉等级
			String compType = CommonUtils.checkNull(request.getParamValue("compType2"));     //投诉小类
			String comType = CommonUtils.checkNull(request.getParamValue("compType"));     //投诉大类
			String compContent = CommonUtils.checkNull(request.getParamValue("compContent"));//投诉内容
			
			// 处理明细
			String strAction = "";
			String[] auditActions = request.getParamValues("auditAction");                     //取得页面的checkbox动作
			if(auditActions!=null){
				for(int i=0;i<auditActions.length;i++){
					strAction+=","+auditActions[i];
				}
			}
			
			String actions = strAction.replaceFirst(",", "");
			String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult"));  //处理结果
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));        //备件编码
			String supplier = CommonUtils.checkNull(request.getParamValue("supplier"));        //上级保供单位
			String auditContent = CommonUtils.checkNull(request.getParamValue("auditContent"));//跟进结果描述    
			String auditId = CommonUtils.checkNull(request.getParamValue("auditId"));          //处理明细id   
			
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			Date currTime = new Date(System.currentTimeMillis());
			// 客户投诉表TT_CR_COMPLAINTS保存更新后的信息
			TtCrComplaintsPO po = new TtCrComplaintsPO();
			po.setCompCode(compCode);
			po.setLinkMan(linkman);
			po.setSex(new Integer(sex));
			po.setAge(new Integer(age));
			po.setAddress(address);
			if(!"".equals(birthday)){
				Date brthdy = formatter.parse(birthday);
				po.setBirthday(brthdy);
			}
			if(!"".equals(purchasedDate)){
				Date purDate = formatter.parse(purchasedDate);
				po.setPurchasedDate(purDate);
			}
			if(!"".equals(compDealerId)){
				po.setCompDealer(new Long(compDealerId));
			}else if("".equals(compDealerId)&&!"".equals(compDealerCode)){
				compDealerId=cddao.getDealerIdByCode(compDealerCode);
				po.setCompDealer(new Long(compDealerId));
			}
			po.setCompContent(compContent);
			
			if(!"".equals(province)){
				po.setProvince(new Integer(province));
			}
			
			if(!"".equals(city)){
				po.setCity(new Integer(city));
			}
			
			if(!"".equals(district)){
				po.setDistrict(new Integer(district));
			}
			
			po.setCompLevel(new Integer(compLevel));
			po.setCompSource(new Integer(compSource));
			po.setCompType(new Integer(compType));
			po.setComType(new Integer(comType));
			po.setEMail(email);
			po.setTel(tel);
			po.setEngineNo(engineNo);
			po.setLicenseNo(licenseNo);
			po.setModelCode(modelCode);
			po.setOwnOrgId(ownOrgId);
			po.setVin(vin);
			po.setZipCode(zipCode);
			po.setUpdateDate(currTime);
			TtCrComplaintsPO po1 = new TtCrComplaintsPO();
			po1.setCompId(new Long(compId));
			
			cddao.update(po1, po);
			
			
			if(!"".equals(actions)&&!"".equals(auditResult)){
				ComplaintAuditDao cadao = new ComplaintAuditDao();
				TtCrComplaintsAuditPO capo = new TtCrComplaintsAuditPO();
				capo.setAuditAction(actions);
				capo.setAuditResult(new Integer(auditResult));
				capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_01);
				capo.setAuditContent(auditContent);
				capo.setCompId(new Long(compId));
				capo.setPartCode(partCode);
				capo.setSupplier(supplier);
				capo.setAuditDate(currTime);
				capo.setCreateBy(logonUser.getUserId());
				capo.setCreateDate(currTime);
				auditId = SequenceManager.getSequence("");
				capo.setId(new Long(auditId));
				// 插入客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT
				//每次操作插入投诉子表 modify by liuqiang.
				cadao.insert(capo);
				
//				if(null==auditId||"".equals(auditId)){
//					auditId = SequenceManager.getSequence("");
//					capo.setCreateBy(logonUser.getUserId());
//					capo.setCreateDate(currTime);
//					
//					capo.setId(new Long(auditId));
//					// 插入客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT
//					cadao.insert(capo);
//				}else{
//					TtCrComplaintsAuditPO capo1 = new TtCrComplaintsAuditPO();
//					capo1.setId(Long.parseLong(auditId));
//					
//					capo.setUpdateBy(logonUser.getUserId());
//					capo.setUpdateDate(currTime);
//					
//					cadao.updateAudit(capo1, capo);
//				}
			}
			
			act.setRedirect(complainDisposalOEMInitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"客户投诉处理(总部)保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 总部批量分配客户投诉到大区
	 */
	public void assignComplaintToArea(){
		try{
			// 获取页面投诉的ID
			String[] compIds = request.getParamValues("compIds");
			
			// 取得页面分配的区域ID
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			ComplaintAuditDao cadao = new ComplaintAuditDao();
			
			Date currTime = new Date(System.currentTimeMillis());
			// 通过ID进行状态的更改同时往TT_CR_COMPLAINTS_AUDIT表中插入记录，实现分配
			if(compIds != null && !compIds.equals("")){
				for(int i=0;i<compIds.length;i++){
					TtCrComplaintsPO po = new TtCrComplaintsPO();
					TtCrComplaintsPO newPo = new TtCrComplaintsPO();
					
					//更新TT_CR_COMPLAINTS中的状态
					po.setCompId(new Long(compIds[i]));
					newPo.setOrgId(new Long(orgId));
					newPo.setStatus(Constant.COMP_STATUS_TYPE_02);
					newPo.setUpdateDate(currTime);
					newPo.setUpdateBy(logonUser.getUserId());
					newPo.setSendDate(new Date());//总部下发到区域的时间
					cddao.update(po, newPo);
					/*
					 * 点击分配不插入子表
					Map<String, Object> auditMap = cadao.getAuditDetailByCompId(compIds[i], String.valueOf(Constant.AUDIT_STATUS_TYPE_01));
					
					
					if(null!=auditMap&&auditMap.size()!=0){
						// 往TT_CR_COMPLAINTS_AUDIT表中插入记录
						TtCrComplaintsAuditPO auditPO = new TtCrComplaintsAuditPO();
						auditPO.setAuditAction(CommonUtils.checkNull(auditMap.get("AUDIT_ACTION")));
						auditPO.setAuditContent(CommonUtils.checkNull(auditMap.get("AUDIT_CONTENT")));
						String auditResult = CommonUtils.checkNull(auditMap.get("AUDIT_RESULT"));
						if(!"".equals(auditResult)){
							auditPO.setAuditResult(Integer.valueOf(auditResult));
						}
						auditPO.setPartCode(CommonUtils.checkNull(auditMap.get("PART_CODE")));
						auditPO.setSupplier(CommonUtils.checkNull(auditMap.get("SUPPLIER")));
						String intStatus = CommonUtils.checkNull(auditMap.get("INT_STATUS"));
						if(!"".equals(intStatus)){
							auditPO.setIntStatus(Integer.valueOf(intStatus));
						}
						
						auditPO.setCreateBy(logonUser.getUserId());
						auditPO.setCreateDate(currTime);
						auditPO.setAuditDate(currTime);
						auditPO.setOrgId(new Long(orgId));
						auditPO.setCompId(new Long(compIds[i]));
						auditPO.setId(Long.valueOf(SequenceManager.getSequence("")));
						auditPO.setAuditStatus(Constant.AUDIT_STATUS_TYPE_02);
						cadao.insert(auditPO);
					}
					*/
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"客户投诉处理(总部)批量分配");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 客户投诉处理(总部)处理和新增页面中的分配
	 */
	public void assignComplaint(){
		
		try{	
			String flag = request.getParamValue("flag");
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  				//联系人姓名
			String sex = CommonUtils.checkNull(request.getParamValue("sex"));  		        		//性别
			String birthday = CommonUtils.checkNull(request.getParamValue("birthday"));     		//生日
			String age = CommonUtils.checkNull(request.getParamValue("age"));               		//年龄
			String ownOrgId = CommonUtils.checkNull(request.getParamValue("ownOrgId"));         	//所属区域
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));	            		//客户电话
			String province = CommonUtils.checkNull(request.getParamValue("province"));       		//省份
			String email = CommonUtils.checkNull(request.getParamValue("email"));           		//e_mail
			String city = CommonUtils.checkNull(request.getParamValue("city"));             		//市
			String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));       		//邮编
			String district = CommonUtils.checkNull(request.getParamValue("district"));     		//县
			String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId"));		//投诉经销商id
			String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); //投诉经销商code
			String address = CommonUtils.checkNull(request.getParamValue("address"));       		//家庭住址
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));       			//分配的区域
			
			// 车辆信息
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));               //vin
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   //车型
			String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));     //发动机号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo"));   //车牌号
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate")); //购车日期
			
			// 投诉内容
			String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); //投诉来源
			String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   //投诉等级
			String compType = CommonUtils.checkNull(request.getParamValue("compType2"));     //投诉类型
			String compContent = CommonUtils.checkNull(request.getParamValue("compContent"));//投诉内容
			
			// 处理明细
			String strAction = "";
			String[] auditActions = request.getParamValues("auditAction");                     //取得页面的checkbox动作
			if(auditActions!=null){
				for(int i=0;i<auditActions.length;i++){
					strAction+=","+auditActions[i];
				}
			}
			String actions = strAction.replaceFirst(",", "");
			String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult"));  //处理结果
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));        //备件编码
			String supplier = CommonUtils.checkNull(request.getParamValue("supplier"));        //上级保供单位
			String auditContent = CommonUtils.checkNull(request.getParamValue("auditContent"));//跟进结果描述       
			
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			TtCrComplaintsPO po = new TtCrComplaintsPO();
			po.setLinkMan(linkman);
			po.setSex(new Integer(sex));
			po.setAge(new Integer(age));
			po.setAddress(address);
			if(!"".equals(birthday)){
				Date brthdy = formatter.parse(birthday);
				po.setBirthday(brthdy);
			}
			if(!"".equals(purchasedDate)){
				Date purDate = formatter.parse(purchasedDate);
				po.setPurchasedDate(purDate);
			}
			
			if(Utility.testString(compDealerId)){
				po.setCompDealer(new Long(compDealerId));
			}else if(Utility.testString(compDealerCode)){
				compDealerId=cddao.getDealerIdByCode(compDealerCode);
				if ("".equals(compDealerId)) {
					throw new IllegalArgumentException("Cann't find dealerId by dealerCode == " + compDealerCode);
				}
				po.setCompDealer(new Long(compDealerId));
			}
			
			po.setCompContent(compContent);
			
			if(!"".equals(province)){
				po.setProvince(new Integer(province));
			}
			
			if(!"".equals(city)){
				po.setCity(new Integer(city));
			}
			
			if(!"".equals(district)){
				po.setDistrict(new Integer(district));
			}
			
			po.setCompLevel(new Integer(compLevel));
			po.setCompSource(new Integer(compSource));
			po.setCompType(new Integer(compType));
			po.setEMail(email);
			po.setTel(tel);
			po.setEngineNo(engineNo);
			po.setLicenseNo(licenseNo);
			po.setModelCode(modelCode);
			po.setOwnOrgId(ownOrgId);
			po.setVin(vin);
			po.setZipCode(zipCode);
			po.setStatus(Constant.COMP_STATUS_TYPE_02);
			po.setOrgId(new Long(orgId));
			
			ComplaintAuditDao cadao = new ComplaintAuditDao();
			TtCrComplaintsAuditPO capo = new TtCrComplaintsAuditPO(); 
			
			if(!"".equals(actions)&&!"".equals(auditResult)){
				capo.setAuditAction(actions);
				capo.setAuditResult(Integer.parseInt(auditResult));
				capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_02);
				capo.setAuditContent(auditContent);
				capo.setPartCode(partCode);
				capo.setSupplier(supplier);
				capo.setOrgId(new Long(orgId));
			}
			
			
			Date currTime = new Date(System.currentTimeMillis());
			// 标识是0表示新增页面的分配，需插入数据
			if(flag.equals("0")){
				// 新增取得投诉表ID和CODE
				String compId = SequenceManager.getSequence("");
				String compCode = SequenceManager.getSequence("TSBH");
				
				// 新增取得投诉明细表的ID
				String auditId = SequenceManager.getSequence("");
				
				// 插入客户投诉表TT_CR_COMPLAINTS
				po.setCompId(new Long(compId));
				po.setCompCode(compCode);
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(currTime);
				po.setSendDate(new Date());//总部下发到区域的时间
				cddao.insert(po);
				
				if(!"".equals(actions)&&!"".equals(auditResult)&&!"".equals(auditContent)){
					// 插入客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT
					capo.setCompId(new Long(compId));
					capo.setCreateBy(logonUser.getUserId());
					capo.setCreateDate(currTime);
					capo.setAuditDate(currTime);
					capo.setId(new Long(auditId));
					//cadao.insert(capo);
				}
				
				
			}else{
				// 未分配页面的分配处理，需更新主表状态，取得页面的id和code
				String compId = CommonUtils.checkNull(request.getParamValue("compId")); 
				String compCode = CommonUtils.checkNull(request.getParamValue("compCode")); 
				
				po.setCompCode(compCode);
				po.setUpdateBy(logonUser.getUserId());
				po.setUpdateDate(currTime);
				po.setSendDate(new Date());//总部下发到区域的时间
				//根据compId更新状态
				TtCrComplaintsPO po1 = new TtCrComplaintsPO();
				po1.setCompId(new Long(compId));
				cddao.update(po1, po);
				
				
				if(!"".equals(actions)&&!"".equals(auditResult)&&!"".equals(auditContent)){
					String auditId = SequenceManager.getSequence("");
					capo.setCompId(new Long(compId));
					capo.setCreateBy(logonUser.getUserId());
					capo.setCreateDate(currTime);
					capo.setAuditDate(currTime);
					capo.setId(new Long(auditId));
					// 插入客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT
					//cadao.insert(capo);
				}
				
			}
			
			act.setRedirect(complainDisposalOEMInitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(总部)分配");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询车系列表
	 */
	public void getModel(){
		try{
			
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId()); 
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"车系列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
//	public void getSeriesList(){
//		try{
//			
//			
//			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
//		
//			List<Map<String, Object>> serieList = cddao.getSeriesList();
//		
//			act.setOutData("seriesList",serieList); 
//		}catch(Exception e){
//			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"车系列表");
//			logger.error(logonUser,e1);
//			act.setException(e1);
//		}
//	}
	
	// 通过VIN查询车辆其他信息
	public void showVinList(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			String commond = CommonUtils.checkNull(request.getParamValue("commond"));
			if(commond.equals("1")){
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));//VIN
				
				ComplaintDisposalDao cddao = new ComplaintDisposalDao();
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
				PageResult<Map<String, Object>> ps = cddao.queryVehicleInfo(vin,curPage,Constant.PAGE_SIZE);
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}
			act.setForword(vinUrl);	
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
