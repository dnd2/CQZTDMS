package com.infodms.dms.actions.sales.fleetmanage.fleetSupport;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetInfoAppDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.dao.sales.marketmanage.planissued.ActivitiesPlanIssuedDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户支持查询Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-25
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class FleetSupportSearch {
	
	public Logger logger = Logger.getLogger(FleetSupportSearch.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportSearch.jsp";
	private final String detailUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportSearchDetail.jsp";
	
	/**
	 * 集团客户支持查询页面初始化
	 */
	public void fleetSupportSearchInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商集团客户支持查询
	 */
	public void fleetSupportSearch(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");			//客户名称
			String fleetType = request.getParamValue("fleetType");			//客户类型
			String startDate = request.getParamValue("startDate");			//起始时间
			String endDate = request.getParamValue("endDate");				//结束时间
			String supportStatus = request.getParamValue("supportStatus");	//申请状态
			String companyId = logonUser.getCompanyId().toString();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetSupportSearch(fleetName, fleetType, startDate, endDate, companyId,supportStatus,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持明细查询
	 */
	public void fleetSupportSearchDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
			//List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,"");
			//act.setOutData("intentList", list1);
			//act.setOutData("intentMap", map2);
			//act.setOutData("intentId", intentId);
			
			
			String fleetId = request.getParamValue("fleetId");		//客户Id
			String intentId = null;	//意向Id
			//查询客户相关信息
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
			act.setOutData("checkList", list2);
			// 根据fleetId查询上传附件
			ActivitiesPlanIssuedDao dao = ActivitiesPlanIssuedDao.getInstance();
			List<Map<String, Object>> attachList = dao.getAttachInfos(fleetId);
			if (null != attachList && attachList.size() != 0) {
				act.setOutData("attachList", attachList);
			}
			//根据集团客户主表的id查询子表需求说明中的内容
			FleetInfoAppDao appDao=new FleetInfoAppDao();
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			//根据集团客户主表的id查询子表商务支持中的内容
			List<Map<String,Object>> supportInfoList=appDao.getSupportInfoByFleetId(fleetId);
			act.setOutData("supportInfoList", supportInfoList);
			act.setOutData("tfrdMapList", tfrdMapList);
			act.setOutData("fleetMap", map);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
