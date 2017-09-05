/**********************************************************************
* <pre>
* FILE : CarType.java
* CLASS : CarType
*
* AUTHOR : wry
*
* FUNCTION : 基础数据维护
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-19| wry  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sysmng.sysData;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.VhclModelInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.baseData.CarTypeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

/**
 * function:基础数据 
 * author: wry
 * CreateDate: 2009-8-19
 * @version:0.1
 */
public class CarTypeQuery {
	public Logger logger = Logger.getLogger(CarTypeQuery.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	public final static int pageSize = 10;
	
	/**
	 * function:查询车型文件 
	 * @param:request-厂家 车系 车辆类型 排量 变速箱 年款
	 * @return:满足条件的信息
	 * author: wry
	 * @throws BizException 
	 * date: 2009-09-20
	 */
	public void queryCarInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try{
		    logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			if("1".equals(request.getParamValue("COMMAND"))){ //json请求
			String startDate = request.getParamValue("startDate")==null?null:request.getParamValue("startDate").trim();
			String endDate = request.getParamValue("endDate")==null?null:request.getParamValue("endDate").trim();
			String operator = request.getParamValue("operator")==null?null:request.getParamValue("operator").trim();
			//厂家
			String brand = CommonUtils.checkNull(request.getParamValue("brand"));
			//车系
			String seriesId = CommonUtils.checkNull(request.getParamValue("series_id"));
			//车辆类型
			String vhclType = CommonUtils.checkNull(request.getParamValue("VHCL_TYPE"));
			//排量
			String dspm = CommonUtils.checkNull(request.getParamValue("DSPM"));
			//变速箱
			String gearBox = CommonUtils.checkNull(request.getParamValue("GEAR_BOX"));
			//年款
			String modelYear = CommonUtils.checkNull(request.getParamValue("MODEL_YEAR"));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			//获取排序字段和排序类型
			String orderName = request.getParamValue("orderCol");
			String da = request.getParamValue("order");
//			String dealerId = logonUser.getDealerId();
			//查询结果集
			PageResult<VhclModelInfoBean> ps = CarTypeDao.getVhclInfo(brand,seriesId,vhclType,dspm,gearBox,modelYear, startDate, endDate,operator,request.getRequestURI(), logonUser,orderName, da, curPage);
			logger.debug("条数："+ps.getTotalRecords());
			act.setOutData("ps", ps);
			}
//			if("1".equals(request.getParamValue("falt"))){
			act.setForword("/jsp/systemMng/baseData/carTypeInfoSearch.jsp");
//			}else{
//			act.setForword("/jsp/systemMng/baseData/carTypeInfoImprotTxt.jsp");
//			}
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"操作");
			logger.error(logonUser,e);
			act.setException(e1);
		}
	}
}
