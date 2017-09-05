package com.infodms.dms.actions.sales.planmanage.MonthTarget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.MonthPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsMonthlyPlanPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class MonthTargetConfirm {
	
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String monthPlanConfrimSearchInit = "/jsp/sales/planmanage/monthplan/monthplanconfrimsearchinit.jsp";
	private final String monthPlanConfrimSearch = "/jsp/sales/planmanage/monthplan/monthplanconfrimsearch.jsp";
	private final String monthPlanConfrimCompleteUrl = "/jsp/sales/planmanage/monthplan/monthplanconfirmcomplete.jsp";

	/*
	 * 初始化页面，查询确认汇总查询
	 */
	public void monthTargetConfirmSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		try {
		   /* List<Map<String, Object>> list=dao.selectUnconfirmMonthPlan(logonUser.getCompanyId().toString());
		    act.setOutData("list", list);*/
		    List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		    act.setOutData("areaBusList", areaBusList);
			act.setForword(monthPlanConfrimSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/***
	 * 确认的时候查询列表
	 * */
	public void monthTargetConfirmSummarySearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		try {
			RequestWrapper request=act.getRequest();
			// String areaId=request.getParamValue("buss_area");
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("companyId", logonUser.getCompanyId().toString());
			// map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps =dao.selectUnconfirmMonthPlan(map,curPage,Constant.PAGE_SIZE_MAX);
		    act.setOutData("ps", ps);
/*		    List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		    act.setOutData("areaBusList", areaBusList);*/
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 未确认月度任务查询
	 */
	public void monthTargetConfirmSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		try {
			RequestWrapper request=act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String month=CommonUtils.checkNull(request.getParamValue("month"));
			// String areaId=CommonUtils.checkNull(request.getParamValue("areaId"));
			// String planType=CommonUtils.checkNull(request.getParamValue("planType"));
			act.setOutData("year", year);
			act.setOutData("month", month);
			// act.setOutData("areaId", areaId);
			// act.setOutData("planType", planType);
			//未确认的数据
			List<Map<String, Object>> list=dao.oemSelectUnconfirmMonthPlan(year, month, logonUser.getUserId(),logonUser.getCompanyId().toString(),dealerId,null);
			act.setOutData("list", list);
			//确认的最大版本号
			Integer planVer=dao.selectMaxPlanVer(year, month, logonUser.getUserId(),logonUser.getCompanyId().toString(),dealerId,null);
			act.setOutData("planVer", planVer+1);
			act.setForword(monthPlanConfrimSearch);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 月度任务确认
	 */
	public void monthTargetConfirmSubmint(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		try {
			RequestWrapper request=act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String month=CommonUtils.checkNull(request.getParamValue("month"));
			// String areaId=CommonUtils.checkNull(request.getParamValue("areaId"));
			String plan_ver=CommonUtils.checkNull(request.getParamValue("plan_ver"));
			String plan_desc=CommonUtils.checkNull(request.getParamValue("plan_desc"));
			//String planType=CommonUtils.checkNull(request.getParamValue("planType"));
			Integer maxVer=dao.selectMaxPlanVer(year, month, logonUser.getUserId(),logonUser.getCompanyId().toString(),dealerId,null);
			if(maxVer.intValue()>=new Integer(plan_ver)){
				throw new Exception("更新冲突");
			}
			dao.setUnEnable(year, month, logonUser.getUserId(),logonUser.getCompanyId().toString(),dealerId,null);
			TtVsMonthlyPlanPO conPo=new TtVsMonthlyPlanPO();
			conPo.setPlanYear(new Integer(year));
			conPo.setPlanMonth(new Integer(month));
			conPo.setCompanyId(logonUser.getCompanyId());
			// conPo.setAreaId(new Long(areaId));
			conPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
			// conPo.setPlanType(new Integer(planType));
			conPo.setIsFlag(new Integer(0));
			TtVsMonthlyPlanPO valPo=new TtVsMonthlyPlanPO();
			valPo.setCompanyId(logonUser.getCompanyId());
			valPo.setPlanVer(new Integer(plan_ver));
			valPo.setPlanDesc(plan_desc);
			valPo.setStatus(Constant.PLAN_MANAGE_CONFIRM);
			dao.update(conPo, valPo);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(monthPlanConfrimCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
}
