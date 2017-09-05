package com.infodms.dms.actions.sales.displacement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.displacement.DisplacementCarDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class DisplacementCarPrice {
	public Logger logger = Logger.getLogger(DisplacementCarPrice.class);
	private ActionContext act = ActionContext.getContext();
	// private ResponseWrapper response = act.getResponse();
	private RequestWrapper request = act.getRequest() ;
	private DisplacementCarDao dao = new DisplacementCarDao() ;
	
	private final String DISPLACEMENTPRCCARURL = "/jsp/sales/displacement/DisplacementPrcCar.jsp";
	private final String DISPLACEMENT_PRC_DLR_URL = "/jsp/sales/displacement/DisplacementPrcDlr.jsp";
	
	public void checkTypeExt() {
		String disType = CommonUtils.checkNull(request.getParamValue("DISPLACEMENT_TYPE")) ;
		String priceType = CommonUtils.checkNull(request.getParamValue("DISPLACEMENT_PRC")) ;
		
		act.setOutData("count", dao.checkTypeExt(disType, priceType)) ;
	}
	
	public void DisplacementPrcUpdateCarInfo() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String disType = CommonUtils.checkNull(request.getParamValue("DISPLACEMENT_TYPE")) ;
			String priceType = CommonUtils.checkNull(request.getParamValue("DISPLACEMENT_PRC")) ;
			String price = CommonUtils.checkNull(request.getParamValue("PRICE")) ;
			
			dao.DisplacementPrcUpdateCarInfo(disType, priceType, price, logonUser.getUserId().toString()) ;
			
			act.setForword(DISPLACEMENTPRCCARURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换返利价格变更异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
//	public void DisplacementPrcDelaerInIt() {
//		AclUserBean logonUser = null;
//		try {
//			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
//			
//			act.setOutData("areas", areas) ;
//			
//			act.setForword(DISPLACEMENT_PRC_DLR_URL) ;
//		} catch (Exception e) {
//			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换经销商维护页面跳转异常");
//			logger.error(logonUser, e1);
//			act.setException(e1);
//		}
//	}
	
	public void DisplacementPrcDelaerShow() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String disPrc = CommonUtils.checkNull(request.getParamValue("DISPLACEMENT_PRC")) ;
			String dlrId = CommonUtils.checkNull(request.getParamValue("dlrId")) ;
			String areas = CommonUtils.checkNull(request.getParamValue("areas")) ;
			String orgId = null ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("disPrc", disPrc) ;
			map.put("dlrId", dlrId) ;
			map.put("orgId", orgId) ;
			map.put("areas", areas) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> dlrList = dao.DisplacementPrcDelaer(map, Constant.PAGE_SIZE, curPage) ;
			
			act.setOutData("ps", dlrList) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换经销商维护查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dlrPrcOpera() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String disPrc = CommonUtils.checkNull(request.getParamValue("DISPLACEMENT_PRC")) ;
			String[] dealerIds = request.getParamValues("dealerIds") ;
			String delDlr = CommonUtils.checkNull(request.getParamValue("delDlr")) ;
			
			int iLen = delDlr.length() ;
			
			if(iLen > 0) {
				dao.delDlrPrc(delDlr) ;
			}
			
			if(dealerIds != null) {
				int len = dealerIds.length ;
				
				for(int i=0; i<len; i++) {
					if(dao.chkDlr(dealerIds[i])) {
						continue ;
					} else {
						dao.insertDlrPrc(dealerIds[i], disPrc) ;
					}
				}
			}
			
			act.setForword(DISPLACEMENTPRCCARURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二手车置换经销商维护异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
