package com.infodms.dms.actions.partsmanage.infoSearch;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: PartInfoSearch.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-3
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class PartInfoSearch implements PTConstants {
	public Logger logger = Logger.getLogger(PartInfoSearch.class);
	public void partInfoSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(piinitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void partInfoEditInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_INFO_MOD);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryPartInfoSearch(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件代码
			String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME")); //配件名称
			String supplierCode = CommonUtils.checkNull(request.getParamValue("SUPPLIER_CODE"));//供应商代码
			String supplierName = CommonUtils.checkNull(request.getParamValue("SUPPLIER_NAME"));//供应商名称
			if(!supplierCode.equals("")){////截串加单引号
				String[] supp = supplierCode.split(",");
				supplierCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!supplierCode.equals("")){
						supplierCode += "," + supp[i];
					}else{
						supplierCode = supp[i];
					}
				}
			}
			PartinfoBean bean = new PartinfoBean();
			bean.setPartCode(partCode);
			bean.setPartName(partName);
			bean.setSupplierCode(supplierCode);
			bean.setSupplierName(supplierName);
			
			PartinfoDao dao = new PartinfoDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = null;
			if(bean.getSupplierCode().equals("") && bean.getSupplierName().equals("")){
			    ps = dao.queryPartInfoListOnly(bean,curPage,Constant.PAGE_SIZE);
			}else{
			    ps = dao.queryPartInfoList(bean,curPage,Constant.PAGE_SIZE);
			}
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 配件详细信息
	 */
	public void queryPartDetail(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId")); //配件Id
			PartinfoDao dao = new PartinfoDao();
			Map<String, Object> dealerInfo = dao.getPartDetail(partId);//配件详细
			act.setOutData("dealerInfo", dealerInfo);
			act.setForword(infoUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 配件详细信息-修改页面
	 */
	public void queryPartDetailMod(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId")); //配件Id
			PartinfoDao dao = new PartinfoDao();
			Map<String, Object> dealerInfo = dao.getPartDetail(partId);//配件详细
			act.setOutData("dealerInfo", dealerInfo);
		
			act.setForword(infoUrlMod);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 分页查询配件与供货方关系
	 */
	public void getDealerInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = request.getParamValue("partId");
			PartinfoDao dao = new PartinfoDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.getPartRelation(partId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: modReturn 
	* @Description: TODO(修改配件主信息) 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void modReturn() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = request.getParamValue("partId");
			String isReturn = request.getParamValue("isReturn");
			String isNewPart = request.getParamValue("IS_NEW_PART");
			PartinfoDao dao = new PartinfoDao();
			TmPtPartBasePO spo = new TmPtPartBasePO();//源po
			spo.setPartId(Long.parseLong(partId));
			TmPtPartBasePO dpo = new TmPtPartBasePO();//要更新的po
			dpo.setIsReturn(Integer.parseInt(isReturn));
			dpo.setIsNewPart(Integer.parseInt(isNewPart));
			dao.update(spo, dpo);
			Map<String, Object> dealerInfo = dao.getPartDetail(partId);//配件详细
			act.setOutData("dealerInfo", dealerInfo);
			partInfoEditInit();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
