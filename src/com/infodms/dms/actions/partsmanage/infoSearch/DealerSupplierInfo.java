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
import com.infodms.dms.po.TmDealerDcRelationPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: DealerSupplierInfo.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-5
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class DealerSupplierInfo implements PTConstants {
	public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
	
	/**
	 * 配送中心与经销商列表初始化
	 */
	public void DealerSupplierInfoInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(dsinitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配送中心与经销商关系维护初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配送中心与经销商关系列表
	 */
	public void queryDCRelactionInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dcCode = CommonUtils.checkNull(request.getParamValue("dcCode"));//供货方代码
			String dcName = CommonUtils.checkNull(request.getParamValue("dcName"));//供货方名称

			PartinfoBean bean = new PartinfoBean();
			bean.setDcCode(dcCode);
			bean.setDcName(dcName);
			
			PartinfoDao dao = new PartinfoDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDCRelactionInfo(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配送中心与经销商关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配送中心与经销商关系维护初始化
	 */
	public void updateRelactionInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			String dcId = request.getParamValue("dcId");//供货方ID
			PartinfoDao dao = new PartinfoDao();
			Map<String, Object> dcInfo = dao.getDcInfo(dcId);
			act.setOutData("dcInfo", dcInfo);
			act.setForword(relactionUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配送中心与经销商关系维护初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配送中心与经销商关系维护
	 */
	public void updateRelaction(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dcId = request.getParamValue("dcId");
			PartinfoDao dao = new PartinfoDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryRelactionInfo(dcId,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配送中心与经销商关系维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 查询经销商初始化
	 */
	public void queryDealerInfoListInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dcId = request.getParamValue("dcId");//供货方ID
			act.setOutData("dcId", dcId);
			act.setForword(dealerUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配送中心与经销商关系维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询经销商
	 */
	public void queryDealerInfoList(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dcId = request.getParamValue("dcId");//供货方ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//经销商ID
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商名称
			String dealerLevel = CommonUtils.checkNull(request.getParamValue("dealerLevel"));//经销商级别
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			PartinfoBean bean = new PartinfoBean();
			bean.setDcId(dcId);
			bean.setDealerCode(dealerCode);
			bean.setDealerName(dealerName);
			bean.setDealerLevel(dealerLevel);
			
			PartinfoDao dao = new PartinfoDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDealerInfo(bean,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("dcId", dcId);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商关");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询经销商
	 */
	public void conserveReaction(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dcId = request.getParamValue("dcId");//供货方ID
			String[] dealerIds = request.getParamValues("dealerIds");//经销商ID （多个）
			Long createBy = logonUser.getUserId();
			PartinfoDao dao = new PartinfoDao();
			dao.conserveReaction(dcId,dealerIds,createBy);
			act.setOutData("returnValue", 1);
			//act.setForword(relactionUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"配送中心与经销商关系维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 删除配送中心和经销商对应关系
	 */
	public void delReaction(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dcId = request.getParamValue("dcId");//供货方ID
			String[] dealerIds = request.getParamValues("dealerIds");//经销商ID（多个）
			PartinfoDao dao = new PartinfoDao();
			dao.deleteReaction(dcId, dealerIds);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"配送中心与经销商关系");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
