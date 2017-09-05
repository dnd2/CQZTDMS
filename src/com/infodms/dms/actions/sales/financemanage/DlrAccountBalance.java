/**********************************************************************
* <pre>
* FILE : DlrAccountBalance.java
* CLASS : DlrAccountBalance
* AUTHOR : 
* FUNCTION : 财务管理
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-31|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.financemanage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.delivery.DeliveryApply;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-31
 * @author 
 * @mail  	
 * @version 1.0
 * @remark 账户余额查询
 */
public class DlrAccountBalance {
	
	public Logger logger = Logger.getLogger(DeliveryApply.class);   
	AccountBalanceDetailDao dao  = AccountBalanceDetailDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	//private final String initUrl = "/jsp/sales/financemanage/dlrAccountBalance.jsp";
	private final String initUrl = "/jsp/sales/financemanage/dlrAccountMyInfo.jsp";
	
	/**
	 * 账户余额查询页面初始化
	 */
	public void dlrAccountBalanceInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				String dealerId = logonUser.getDealerId();
				List<Map<String, Object>>  getMyList=dao.getAccountListInfo(dealerId,oemCompanyId);
				List<Map<String, Object>>  getList=dao.getAccountDealer(dealerId, oemCompanyId);
				List<Map<String, Object>> freezeList = dao.getFreezeList(dealerId); 
				
				Integer oemFlag = CommonUtils.getNowSys(Long.valueOf(logonUser.getOemCompanyId()));
				
				act.setOutData("oemFlag", oemFlag);
				act.setOutData("dealerId", dealerId);
				act.setOutData("getMyList",getMyList);
				act.setOutData("getList", getList);
				act.setOutData("freezeList", freezeList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 账户余额查询
	 */
	public void dlrAccountBalanceQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		try {
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			String areaIds="";
			if(areaList.size()>0){
				for(int i=0;i<areaList.size();i++){
					if("".equals(areaIds)){
						areaIds = areaList.get(i).get("AREA_ID").toString();
					}else{
						areaIds = areaList.get(i).get("AREA_ID").toString()+","+areaIds;
					}
				}
				
			}
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			PageResult<Map<String, Object>> ps = dao.dlrAccountBalanceQuery(dealerId, areaIds,oemCompanyId,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void dlrAccountBalanceQueryDetial(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	//	List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
//		String areaIds="";
//		if(areaList.size()>0){
//			for(int i=0;i<areaList.size();i++){
//				if("".equals(areaIds)){
//					areaIds = areaList.get(i).get("AREA_ID").toString();
//				}else{
//					areaIds = areaList.get(i).get("AREA_ID").toString()+","+areaIds;
//				}
//			}
//			
//		}
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
	//	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
		String dealerId = logonUser.getDealerId();
		List<Map<String, Object>>  getMyList=dao.getAccountListInfo(dealerId,oemCompanyId);
		act.setOutData("getMyList",getMyList);
		act.setForword(initUrl);
	}catch(Exception e) {//异常方法
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询失败");
		logger.error(logonUser,e1);
		act.setException(e1);
	}
	}
	
}
