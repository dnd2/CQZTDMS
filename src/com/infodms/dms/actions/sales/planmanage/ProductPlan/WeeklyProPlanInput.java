package com.infodms.dms.actions.sales.planmanage.ProductPlan;

import java.util.Date;
import java.util.HashMap;
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
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtVsWeelyProdPlanPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class WeeklyProPlanInput {

	
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String weeklyProPlanInputSearchInit = "/jsp/sales/planmanage/productplan/weeklyproplaninput.jsp";
	private final String weeklyProPlanInputSaveUrl = "/jsp/sales/planmanage/productplan/weeklyproplaninputsave.jsp";
	private final String weeklyProPlanInputCompleteUrl = "/jsp/sales/planmanage/productplan/weeklyproplaninputcomplete.jsp";

	/*
	 * 初始化页面，查询确认汇总查询
	 */
	public void weeklyProPlanInputSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			String curdate=PlanUtil.getCurrentYear()+"年"+PlanUtil.getCurrentMonth()+"月"+PlanUtil.getCurrentDay()+"日";
			String curWeek=PlanUtil.getCurrentWeek();
			List<String> dlist=PlanUtil.getAimDate(1);
			List<Map<String, Object>> mateGroup__ = MaterialGroupManagerDao.getInstance().MateGroupQuery() ;
			int intweek=PlanUtil.getCurIntWeek();
			act.setOutData("mateGroup__2", mateGroup__) ;
			act.setOutData("intweek", intweek);
			act.setOutData("curdate", curdate);
			act.setOutData("curWeek", curWeek);
			act.setOutData("dlist", dlist);
		    List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		    act.setOutData("areaBusList", areaBusList);
			act.setForword(weeklyProPlanInputSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 查询到配置
	 */
	public void weeklyProPlanInputSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			
			String cyear=PlanUtil.getCurrentYear();
			String cmonth=PlanUtil.getCurrentMonth().length()>1?PlanUtil.getCurrentMonth():"0"+PlanUtil.getCurrentMonth();
			String cday=PlanUtil.getCurrentDay()+"";
			cday=cday.length()>1?cday:"0"+cday;
			//当前年月日yyyymmdd
			String setDate=cyear+cmonth+cday;
			Map<String, Object> curWeekMap=PlanUtil.getSetDateCurWeek(setDate,logonUser.getCompanyId().toString());
			
			String curdate=cyear+"年"+cmonth+"月"+cday+"日";
			String curWeek=PlanUtil.getCurrentWeek();
			//下周次的年周
			Map<String, Object> nextWeekMap=PlanUtil.getSetDateNextWeekMap(cyear, curWeekMap.get("WEEK").toString(),logonUser.getCompanyId().toString());
			
			RequestWrapper request=act.getRequest();
			
			String areaId=request.getParamValue("areaId");
			
			String mateGroupId = request.getParamValue("mateGroup") ;
			if (mateGroupId == null || "".equals(mateGroupId)) {
				mateGroupId = "" ;
			}
			System.out.println("1111"+mateGroupId) ;
			if (areaId == null || "".equals(areaId)) {
				areaId = "" ;
			} else {
				areaId = areaId.split(",")[0];
				
			}
			
			/*if(areaId.indexOf(",")!=-1){
				areaId=areaId.substring(0,areaId.lastIndexOf(","));
			}*/
			
			/*if(request.getParamValue("myAreaId")!=null&&request.getParamValue("myAreaId")!=""){
				areaId=request.getParamValue("myAreaId");
			}*/
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("year", nextWeekMap.get("YEAR").toString());
			map.put("month", nextWeekMap.get("MONTH").toString());
			map.put("week", nextWeekMap.get("WEEK").toString());
			map.put("companyId", logonUser.getCompanyId().toString());
			map.put("areaId", areaId+"");
			map.put("companyId", logonUser.getCompanyId());
			map.put("mateGroupId", mateGroupId) ;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao.selectWeeklyProPlanConfig(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("myAreaId", areaId);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 周滚动计划保存查询，查询到物料
	 */
	public void weeklyProPlanInputSearchForSave(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			String cyear=PlanUtil.getCurrentYear();
			String cmonth=PlanUtil.getCurrentMonth().length()>1?PlanUtil.getCurrentMonth():"0"+PlanUtil.getCurrentMonth();
			String cday=PlanUtil.getCurrentDay()+"";
			cday=cday.length()>1?cday:"0"+cday;
			//当前年月日yyyymmdd
			String setDate=cyear+cmonth+cday;
			Map<String, Object> curWeekMap=PlanUtil.getSetDateCurWeek(setDate,logonUser.getCompanyId().toString());
			
			String curdate=cyear+"年"+cmonth+"月"+cday+"日";
			String curWeek=PlanUtil.getCurrentWeek();
			//下周次的年周
			Map<String, Object> nextWeekMap=PlanUtil.getSetDateNextWeekMap(cyear, curWeekMap.get("WEEK").toString(),logonUser.getCompanyId().toString());
			TmDateSetPO cpo=new TmDateSetPO();
			cpo.setSetYear(nextWeekMap.get("YEAR").toString());
			cpo.setSetWeek(nextWeekMap.get("WEEK").toString());
			cpo.setCompanyId(logonUser.getCompanyId());
			//年周七天日期
			List<TmDateSetPO> dList=dao.selectDateSetDateList(cpo);
			//下个周次的，YYYYMMDD
			String dateSet=nextWeekMap.get("YEAR").toString()+","+nextWeekMap.get("MONTH").toString()+","+nextWeekMap.get("WEEK").toString();
			
			String n7Date=PlanUtil.getNextNDay(7);
			
			act.setOutData("n7Date", n7Date);
			act.setOutData("dateSet", dateSet);
			act.setOutData("curdate", curdate);
			act.setOutData("curWeek", curWeek);
			act.setOutData("dlist", dList);
			
			RequestWrapper request=act.getRequest();
			request.setAttribute("sumAmount", request.getParamValue("sumAmount")) ;
			String groupId=request.getParamValue("groupId");
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("groupId", groupId+"");
			map.put("year", nextWeekMap.get("YEAR").toString());
			map.put("month", nextWeekMap.get("MONTH").toString());
			map.put("week", nextWeekMap.get("WEEK").toString());
			map.put("companyId", logonUser.getCompanyId().toString());
			
			List<Map<String, Object>> list=dao.selectWeeklyProPlanToSave(map);
			List<Map<String, Object>> mateGroup__ = MaterialGroupManagerDao.getInstance().MateGroupQuery() ;
			act.setOutData("mateGroup__2", mateGroup__) ;
			String mateGro = request.getParamValue("mateGroup") ;
			act.setOutData("mateGro", mateGro) ;
			act.setOutData("myAreaId",request.getParamValue("myAreaId"));
			act.setOutData("list", list);
			act.setOutData("groupId", groupId);
			act.setForword(weeklyProPlanInputSaveUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 保存滚动计划
	 */
	public void weeklyProPlanInputSave(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			
			RequestWrapper request=act.getRequest();
			String dateSet=request.getParamValue("dateSet");
			String[] arr=dateSet.split(",");
			String year=arr[0];
			String month=arr[1];
			String week=arr[2];
			String groupId=request.getParamValue("groupId");
			//Enumeration<String> materailIds=request.getParamNames();
			String[] materialIds = request.getParamValues("materialId");
			String materialId="";
			for(int i=0;i<materialIds.length;i++){
				Long sumWeek=new Long(0);
				materialId=materialIds[i];
				//删除某一物料的周滚动计划
				TtVsWeelyProdPlanPO clrpo=new TtVsWeelyProdPlanPO();
				clrpo.setPlanYear(new Integer(year));
				clrpo.setPlanMonth(new Integer(month));
				clrpo.setPlanWeek(new Integer(week));
				clrpo.setMaterialId(new Long(materialId));
				dao.delete(clrpo);
				//添加一条物料滚动计划
				String oneAmt=request.getParamValue(materialId+"ONE");
				String twoAmt=request.getParamValue(materialId+"TWO");
				String threeAmt=request.getParamValue(materialId+"THREE");
				String fourAmt=request.getParamValue(materialId+"FOUR");
				String fiveAmt=request.getParamValue(materialId+"FIVE");
				String sixAmt=request.getParamValue(materialId+"SIX");
				String sevenAmt=request.getParamValue(materialId+"SEVEN");
				
				TtVsWeelyProdPlanPO npo=new TtVsWeelyProdPlanPO();
				npo.setPlanId(new Long(SequenceManager.getSequence("")));
				npo.setCompanyId(logonUser.getCompanyId());
				npo.setPlanYear(new Integer(year));
				npo.setPlanMonth(new Integer(month));
				npo.setPlanWeek(new Integer(week));
				npo.setGroupId(new Long(groupId));
				npo.setMaterialId(new Long(materialId));
				npo.setOneAmt(new Integer(oneAmt));
				sumWeek+=new Integer(oneAmt);
				npo.setTwoAmt(new Integer(twoAmt));
				sumWeek+=new Integer(twoAmt);
				npo.setThreeAmt(new Integer(threeAmt));
				sumWeek+=new Integer(threeAmt);
				npo.setFourAmt(new Integer(fourAmt));
				sumWeek+=new Integer(fourAmt);
				npo.setFiveAmt(new Integer(fiveAmt));
				sumWeek+=new Integer(fiveAmt);
				
				npo.setSixAmt(new Integer(sixAmt));
				sumWeek+=new Integer(sixAmt);
				npo.setSevenAmt(new Integer(sevenAmt));
				sumWeek+=new Integer(sevenAmt);
				npo.setWeekAmt(new Long(sumWeek));
				npo.setCreateBy(logonUser.getUserId());
				npo.setCreateDate(new Date());
				
				dao.insert(npo);
			}
			String areaId=request.getParamValue("myAreaId");
			//act.setOutData("myNewAreaId",request.getParamValue("myAreaId"));
			String myareaId=areaId.substring(0,areaId.lastIndexOf(","));
			act.setOutData("myNewAreaId",myareaId);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("week", week);
			try {
				String curdate=PlanUtil.getCurrentYear()+"年"+PlanUtil.getCurrentMonth()+"月"+PlanUtil.getCurrentDay()+"日";
				String curWeek=PlanUtil.getCurrentWeek();
				List<String> dlist=PlanUtil.getAimDate(1);
				int intweek=PlanUtil.getCurIntWeek();
				act.setOutData("intweek", intweek);
				act.setOutData("curdate", curdate);
				act.setOutData("curWeek", curWeek);
				act.setOutData("dlist", dlist);
			    List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			    act.setOutData("areaBusList", areaBusList);
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
			System.out.println("-0000-"+request.getParamValue("mateGro"));
			act.setOutData("mateGro", request.getParamValue("mateGro")) ;
			List<Map<String, Object>> mateGroup__ = MaterialGroupManagerDao.getInstance().MateGroupQuery() ;
			act.setOutData("mateGroup__2", mateGroup__) ;
			act.setForword(weeklyProPlanInputSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
