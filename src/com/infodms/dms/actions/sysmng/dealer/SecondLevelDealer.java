package com.infodms.dms.actions.sysmng.dealer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.dealer.SecondLevelDealerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class SecondLevelDealer {
	public Logger logger = Logger.getLogger(SecondLevelDealer.class);
	
	private SecondLevelDealerDao dao = SecondLevelDealerDao.getInstance();
	
	/**
	 * 二级经销商申请初始化
	 */
	public void secondLevelDealerApplyInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			RequestWrapper request = act.getRequest();
			if ("1".equals(request.getParamValue("queryFlag"))) {
				map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
				map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
				map.put("IMAGE_COMFIRM_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL")));// 形象等级
				map.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));// 授权类型
				map.put("WORK_TYPE", CommonUtils.checkNull(request.getParamValue("WORK_TYPE")));// 经营类型
				map.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));// 经销商状态
				map.put("AUTHORIZATION_SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_SCREATE_DATE")));// 授权开始时间
				map.put("AUTHORIZATION_ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_ECREATE_DATE")));// 授权结束时间

				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				map.put("curPage", curPage);
				map.put("pageSize", Constant.PAGE_SIZE);
				map.put("logonUserDealerId", logonUser.getDealerId());
				
				PageResult<Map<String, Object>> ps = dao.secondLevelDealerApplyInitQuery(map);
				act.setOutData("ps", ps);
			} else {
				act.setForword("/jsp/systemMng/dealer/secondLeveldealerApplyInit.jsp");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "二级经销商");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 增加二级经销商初始化
	 */
	public void addSecondLevelDealerInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("is_Add", true);
			act.setForword("/jsp/systemMng/dealer/addSecondLevelDealer.jsp");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE, "新增二级经销商初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
