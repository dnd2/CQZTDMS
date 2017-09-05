package com.infodms.dms.actions.sales.planmanage.ProductPlan;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.ProductPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TtVsProductionPlanPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class ProductPlanConfirm {
	
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String productPlanConfrimSearchInit = "/jsp/sales/planmanage/productplan/productplanconfrimsearchinit.jsp";
	private final String productPlanConfrimSearch = "/jsp/sales/planmanage/productplan/productplanconfrimsearch.jsp";
	private final String productPlanConfrimCompleteUrl = "/jsp/sales/planmanage/productplan/productplanconfirmcomplete.jsp";

	/*
	 * 初始化页面，查询确认汇总查询
	 */
	public void productPlanConfirmSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
		    List<Map<String, Object>> list=dao.selectUnconfirmProductPlan(logonUser.getPoseId().toString(),logonUser.getCompanyId().toString());
		    act.setOutData("list", list);
		    List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		    act.setOutData("areaBusList", areaBusList);
			act.setForword(productPlanConfrimSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/*
	 * 未确认月生产计划查询
	 */
	public void productPlanConfirmSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String year=request.getParamValue("year");
			String month=request.getParamValue("month");
			String areaId=request.getParamValue("areaId");
			//查询工作日历是年月的周次
			TmDateSetPO conPo=new TmDateSetPO();
			conPo.setSetYear(year);
			conPo.setSetMonth(month);
			conPo.setCompanyId(logonUser.getCompanyId());
			List<Map<String, Object>> weekList=PlanUtil.selectDateSetWeekList(conPo);
			act.setOutData("weekList", weekList);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("areaId", areaId);
			List<Map<String, Object>> list=dao.selectUnconfirmProductPlanDetail(year, month, areaId,weekList,logonUser.getCompanyId().toString());
			act.setOutData("list", list);
			Integer planVer=dao.selectMaxPlanVer(year, month,areaId,logonUser.getCompanyId().toString());
			act.setOutData("planVer", planVer+1);
			act.setForword(productPlanConfrimSearch);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 月度任务确认
	 */
	public void productTargetConfirmSubmint(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String year=request.getParamValue("year");
			String month=request.getParamValue("month");
			String areaId=request.getParamValue("areaId");
			String plan_ver=request.getParamValue("plan_ver");
			String plan_desc=request.getParamValue("plan_desc");
			
			Integer maxVer=dao.selectMaxPlanVer(year, month, areaId,logonUser.getCompanyId().toString());
			if(maxVer.intValue()>=new Integer(plan_ver)){
				throw new Exception("更新冲突");
			}
			
			TtVsProductionPlanPO conPo=new TtVsProductionPlanPO();
			conPo.setPlanYear(new Integer(year));
			conPo.setPlanMonth(new Integer(month));
			conPo.setAreaId(new Long(areaId));
			conPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
			conPo.setCompanyId(logonUser.getCompanyId());
			
			TtVsProductionPlanPO valPo=new TtVsProductionPlanPO();
			valPo.setCompanyId(logonUser.getCompanyId());
			valPo.setPlanVer(new Integer(plan_ver));
			valPo.setPlanDesc(plan_desc);
			valPo.setStatus(Constant.PLAN_MANAGE_CONFIRM);
			
			dao.update(conPo, valPo);
			act.setOutData("year", year);
			act.setOutData("month", month);
			
			act.setForword(productPlanConfrimCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
