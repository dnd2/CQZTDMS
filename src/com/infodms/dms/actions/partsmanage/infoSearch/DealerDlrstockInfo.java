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
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: DealerDlrstockInfo.java
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
public class DealerDlrstockInfo implements PTConstants {
	public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
	
	/**
	 * 初始化
	 */
	public void dealerDlrstockInfoInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(stockInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商库存初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 分页查询经销商库存
	 */
	public void queryDealerDlrstockInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//经销商代码
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商名称
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));//配件代码
			String partName = CommonUtils.checkNull(request.getParamValue("partName"));//配件名称
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));//组织代码
			String odtype = request.getParamValue("odtype");
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
			if(!orgCode.equals("")){
				String[] org = orgCode.split(",");
				orgCode = "";
				for(int j=0;j<org.length;j++){
					org[j] = "'"+org[j]+"'";
					if(!orgCode.equals("")){
						orgCode += "," + org[j];
					}else{
						orgCode = org[j];
					}
				}
			}
			PartinfoBean bean = new PartinfoBean();
			bean.setDealerCode(dealerCode);
			bean.setPartCode(partCode);
			bean.setPartName(partName);
			bean.setDealerName(dealerName);
			bean.setOrgCode(orgCode);
			PartinfoDao dao = new PartinfoDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = null;
			
			
			if(odtype.equals("0")){
				ps = dao.queryOrgDlrstockList(bean,curPage,Constant.PAGE_SIZE);
			}else{
				ps = dao.queryDealerDlrstockList(bean,curPage,Constant.PAGE_SIZE);
			}
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商库存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询配件出入库明细初始化
	 */	
	public void queryPartDlrmoveDetail(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = request.getParamValue("partId");//配件ID
			String dealerId = request.getParamValue("dealerId");//经销商ID
			act.setOutData("partId", partId);
			act.setOutData("dealerId", dealerId);
			act.setForword(stockInfoUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商库存初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询配件出入库明细
	 */	
	public void queryPartDlrmoveDetailInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = request.getParamValue("partId");//配件ID
			String dealerId = request.getParamValue("dealerId");//经销商ID
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
			String doStatus = CommonUtils.checkNull(request.getParamValue("doStatus"));//发生状态（出库或入库）
			PartinfoBean bean = new PartinfoBean();
			bean.setPartId(partId);
			bean.setDealerId(dealerId);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setDoStatus(doStatus);
			PartinfoDao dao = new PartinfoDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPartDlrmoveDetailInfo(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商库存明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}

