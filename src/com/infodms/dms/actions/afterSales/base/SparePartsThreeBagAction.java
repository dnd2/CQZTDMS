package com.infodms.dms.actions.afterSales.base;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.afterSales.SparePartsThreeBagDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class SparePartsThreeBagAction {
	 private Logger logger = Logger.getLogger(SparePartsThreeBagAction.class);
	  private ActionContext act = ActionContext.getContext();
	  RequestWrapper request = act.getRequest();
	  AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	  private SparePartsThreeBagDao dao=SparePartsThreeBagDao.getInstance();
	  private static final String SparePartsThreeBagInit_URL = "/jsp/afterSales/base/sparePartsThreeBag.jsp";

	  
	  /**
		 * 备件三包期维护初始化
		 * 
		 * @param null
		 * @return void
		 * @throws Exception
		 */
	  public void getSparePartsThreeBagInit(){
	    try{
	    	act.setForword(SparePartsThreeBagInit_URL);
	    } catch (Exception e) {
	    	BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "备件三包期维护初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
	    }
	  } 
	  
	  /**
		 * 备件三包期查询
		 * 
		 * @param null
		 * @return void
		 * @throws Exception
		 */

	public void querySparePartsThreeBagInit(){
	  try
	  {
	    Integer curPage = Integer.valueOf(request.getParamValue("curPage") != null ? 
	      Integer.parseInt(this.request.getParamValue("curPage")) : 
	      1);
	    String wr_months = CommonUtils.checkNull(request.getParamValue("wr_months"));//三包天数
	    String wr_mileage = CommonUtils.checkNull(request.getParamValue("wr_mileage"));//三包里程
	    String part_code = CommonUtils.checkNull(request.getParamValue("part_code")); // 备件代码
	    String part_name = CommonUtils.checkNull(request.getParamValue("part_name")); // 备件名称
			
		  Map<String, Object> map = new HashMap<String, Object>();
			map.put("wr_months", wr_months);
			map.put("wr_mileage", wr_mileage);
			map.put("part_code", part_code);
			map.put("part_name", part_name);
	    PageResult<Map<String, Object>> ps =  dao.querySparePartsThreeBag(map,Constant.PAGE_SIZE.intValue(), curPage.intValue());
	    act.setOutData("ps", ps);
	  }
	  catch (Exception e) {
	  	BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "首保备件查询");
			logger.error(logonUser, e1);
			act.setException(e1);
	  		}
		}
	
	/**
	 * 备件三包期修改
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */

	public void updateSparePartsThreeBag(){
	  try {
			String p_id = CommonUtils.checkNull(request.getParamValue("aty"));//id
			String wr_months = CommonUtils.checkNull(request.getParamValue("wm"));//三包天数
			String wr_mileage = CommonUtils.checkNull(request.getParamValue("wg"));//三包里程
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("p_id", p_id);
			map.put("wr_months", wr_months);
			map.put("wr_mileage", wr_mileage);
			map.put("userId", logonUser.getUserId());
			dao.updateThreeBag(map);
			act.setOutData("succeed", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "备件三包期修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	 
}
