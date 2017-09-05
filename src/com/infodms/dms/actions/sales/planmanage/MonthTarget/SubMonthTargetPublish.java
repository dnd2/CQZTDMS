package com.infodms.dms.actions.sales.planmanage.MonthTarget;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.planmanage.MonthPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsMonthlyPlanDetailPO;
import com.infodms.dms.po.TtVsMonthlyPlanPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class SubMonthTargetPublish {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String monthPlanExcelImplortUrl = "/jsp/sales/planmanage/monthplan/submonthplanpublish.jsp";
	private final String monthPlanCheckSuccessUrl = "/jsp/sales/planmanage/monthplan/monthplanchecksuccess.jsp";
	private final String monthPlanCheckFailureUrl = "/jsp/sales/planmanage/monthplan/monthplancheckfailure.jsp";
	private final String monthPlanPublistCompleteUrl = "/jsp/sales/planmanage/monthplan/submonthplanpublistcomplete.jsp";
	
	/*
	 * 下发查询初始化
	 */
	public void subMonthPlanPublistInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		try {
			/*String month=PlanUtil.getRadomDate(1,"month");
			String year=PlanUtil.getRadomDate(1, "year");
			act.setOutData("year", year);
			act.setOutData("month", month);
		    List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);*/
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("orgId", logonUser.getOrgId().toString());
			map.put("companyId", logonUser.getCompanyId().toString());
			List<Map<String, Object>> list=dao.selectUnPublistDate(map);
			act.setOutData("list", list);
			act.setForword(monthPlanExcelImplortUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 月度任务下发查询
	 * 注：月度任务的查询，只查询已保存的记录（即未确认),目标月份如果有已下发版本也不查询出来。这里应该查询工作日历，如果不在下发日期内就不能查询
	 */
	public void monthPlanPublistQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String paras=request.getParamValue("areaId");
			String[] arr=paras.split(",");
			String year=arr[0];
			String month=arr[1];
			String areaId=arr[2];
			String planType=arr[3];
			String companyId=logonUser.getCompanyId().toString();
			//查询业务范围内的车系
			List<Map<String, Object>> areaGroupList=dao.selectAreaGroup(areaId,companyId);
			Map<String, Object> subInfo=dao.selectMonthPublistMapVer1(logonUser.getOrgId(), year, month, areaGroupList, areaId,companyId,planType);
			List<Map<String, Object>> planList=dao.selectPublishPlanVer1(areaGroupList, logonUser.getOrgId(), year, month, areaId,companyId,planType);
			System.out.println("33333"+subInfo.get("ORG_Name"));
			act.setOutData("areaGroupList", areaGroupList);
			act.setOutData("planList", planList);
			act.setOutData("subInfo", subInfo);
			act.setOutData("areaId", areaId);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("planType", planType);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 下发按时间控制是否可以操作，如果目标月份已经存在下发任务，那么当次下发版本号+1
	 */
	public void monthPlanPublishOpe(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();			
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String month=CommonUtils.checkNull(request.getParamValue("month"));
			String areaId=CommonUtils.checkNull(request.getParamValue("areaId"));
			String planType=CommonUtils.checkNull(request.getParamValue("planType"));
			String actType=CommonUtils.checkNull(request.getParamValue("actType"));
			//取得当年最大版本号
			String maxVer=dao.selectMaxSubPlanVer(year, month, areaId, logonUser.getUserId().toString(),planType);
			if(null==maxVer||"".equals(maxVer)){
				maxVer="0";
			}
			/*//清空当前组织ORGID保存的未下发月度任务,通过ORGID,ORGTYPE来确定是否是区域下发的月度任务
			TtVsMonthlyPlanPO clrConPo=new TtVsMonthlyPlanPO();
			clrConPo.setAreaId(new Long(areaId));
			clrConPo.setOrgType(Constant.ORG_TYPE_DEALER);
			clrConPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
			clrConPo.setPlanYear(new Integer(year));
			clrConPo.setPlanMonth(new Integer(month));
			clrConPo.setPlanType(new Integer(planType));
			clrConPo.setIsFlag(new Integer(1));
			//clrConPo.setOrgId(logonUser.getOrgId());
			clrConPo.setCompanyId(logonUser.getCompanyId());
			List<TtVsMonthlyPlanPO> clrList=dao.selectMonthClrList(clrConPo);
			for(int i=0;i<clrList.size();i++){
				TtVsMonthlyPlanPO po=clrList.get(i);
				TtVsMonthlyPlanDetailPO dconPo=new TtVsMonthlyPlanDetailPO();
				dconPo.setPlanId(po.getPlanId());
				dao.delete(dconPo);
				TtVsMonthlyPlanPO conPo=new TtVsMonthlyPlanPO();
				conPo.setPlanId(po.getPlanId());
				dao.delete(conPo);
			}*/
			
			//新增月度任务
			Enumeration<String> enumeration=request.getParamNames();
			StringBuffer strDeaId__ = new StringBuffer("") ;
			String dealerId="";
			String groupId="";
			String amt="";
			while(enumeration.hasMoreElements()){
				String eleName=enumeration.nextElement();
				if(eleName.indexOf("amt")!=-1){
					dealerId=eleName.substring(0,eleName.indexOf("amt"));
					strDeaId__.append(dealerId+",") ;
					groupId=eleName.substring(eleName.lastIndexOf("amt")+3,eleName.length());
					amt=request.getParamValue(eleName);
					
					List<Map<String,Object>> clrList__=dao.selectMonthClrDetailList(logonUser.getCompanyId().toString(),areaId,year,month,Constant.ORG_TYPE_DEALER.toString(),dealerId,planType,1,"",Constant.PLAN_MANAGE_UNCONFIRM.toString());
					List<Map<String,Object>> clrDeList__=dao.selectMonthClrDetailList(logonUser.getCompanyId().toString(),areaId,year,month,Constant.ORG_TYPE_DEALER.toString(),dealerId,planType,1,groupId,Constant.PLAN_MANAGE_UNCONFIRM.toString());
								
					System.out.println("----"+(clrList__.size()>0)) ;
										
					if (clrList__ != null && clrList__.size() > 0) {
						String planId__ = clrList__.get(0).get("PLAN_ID").toString();
						if (clrDeList__ != null && clrDeList__.size() > 0) {
							String detailId = clrDeList__.get(0).get("DETAIL_ID").toString() ;
							TtVsMonthlyPlanDetailPO detailPo=new TtVsMonthlyPlanDetailPO();
							TtVsMonthlyPlanDetailPO detailPo__=new TtVsMonthlyPlanDetailPO();
							
							detailPo.setDetailId(new Long(detailId));
							detailPo__.setDetailId(new Long(detailId));
							detailPo__.setSaleAmount(new Integer(amt)) ;
							detailPo__.setUpdateBy(logonUser.getUserId());
							detailPo__.setUpdateDate(new Date()) ;
							
							dao.update(detailPo, detailPo__);
						} else {
							TtVsMonthlyPlanDetailPO detailPo=new TtVsMonthlyPlanDetailPO();
							detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
							detailPo.setMaterialGroupid(new Long(groupId));
							detailPo.setPlanId(new Long(planId__));
							detailPo.setSaleAmount(new Integer(amt));
							detailPo.setCreateBy(logonUser.getUserId());
							detailPo.setCreateDate(new Date());
							
							dao.insert(detailPo);
						}
					} else {
						//插入主表
						TtVsMonthlyPlanPO mPo=new TtVsMonthlyPlanPO();
						String planId=SequenceManager.getSequence("");
						mPo.setPlanId(new Long(planId));
						mPo.setCompanyId(logonUser.getCompanyId());
						mPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
						mPo.setAreaId(new Long(areaId));
						mPo.setIsFlag(new Integer(1));
						mPo.setDealerId(new Long(dealerId));
						mPo.setOrgType(Constant.ORG_TYPE_DEALER);
						mPo.setPlanYear(new Integer(year));
						mPo.setPlanMonth(new Integer(month));
						mPo.setPlanType(new Integer(planType));
						mPo.setCreateBy(logonUser.getUserId());
						mPo.setCreateDate(new Date());
						
						dao.insert(mPo);
						
						//插入明细表
						TtVsMonthlyPlanDetailPO detailPo=new TtVsMonthlyPlanDetailPO();
						detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
						detailPo.setMaterialGroupid(new Long(groupId));
						detailPo.setPlanId(new Long(planId));
						detailPo.setSaleAmount(new Integer(amt));
						detailPo.setCreateBy(logonUser.getUserId());
						detailPo.setCreateDate(new Date());
						
						dao.insert(detailPo);
					}
					
					/*//插入主表
					TtVsMonthlyPlanPO mPo=new TtVsMonthlyPlanPO();
					String planId=SequenceManager.getSequence("");
					mPo.setPlanId(new Long(planId));
					mPo.setCompanyId(logonUser.getCompanyId());
					mPo.setAreaId(new Long(areaId));
					mPo.setIsFlag(new Integer(1));
					mPo.setDealerId(new Long(dealerId));
					mPo.setOrgType(Constant.ORG_TYPE_DEALER);
					mPo.setPlanYear(new Integer(year));
					mPo.setPlanMonth(new Integer(month));
					if("2".equals(actType)){
						mPo.setStatus(Constant.PLAN_MANAGE_CONFIRM);
						mPo.setPlanVer(new Integer(maxVer)+1);
					}else{
						mPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
					}
					mPo.setPlanType(new Integer(planType));
					mPo.setCreateBy(logonUser.getUserId());
					mPo.setCreateDate(new Date());
					dao.insert(mPo);
					//插入明细表
					TtVsMonthlyPlanDetailPO detailPo=new TtVsMonthlyPlanDetailPO();
					detailPo.setDetailId(new Long(SequenceManager.getSequence("")));
					detailPo.setMaterialGroupid(new Long(groupId));
					detailPo.setPlanId(new Long(planId));
					detailPo.setSaleAmount(new Integer(amt));
					detailPo.setCreateBy(logonUser.getUserId());
					detailPo.setCreateDate(new Date());
					dao.insert(detailPo);*/
				}
			}
			String str = strDeaId__.substring(0, strDeaId__.length()-2);
			System.out.println("~~~~~~~~~~"+str) ;
			if("2".equals(actType)){
				// System.out.println("+++++++++++++++++++++++" + "2".equals(actType));
				List<Map<String,Object>> clrList__A=dao.selectMonthClrDetailList(logonUser.getCompanyId().toString(),areaId,year,month,Constant.ORG_TYPE_DEALER.toString(),str,planType,1,"",Constant.PLAN_MANAGE_UNCONFIRM.toString());
				/*List<Map<String,Object>> clrList__B=dao.selectMonthClrDetailList(logonUser.getCompanyId().toString(),areaId,year,month,Constant.ORG_TYPE_DEALER.toString(),dealerId,planType,1,"",Constant.PLAN_MANAGE_CONFIRM.toString());
				if(clrList__B != null &&clrList__B.size()>0) {
					TtVsMonthlyPlanPO mPo__Del=new TtVsMonthlyPlanPO();
					mPo__Del.setPlanId(new Long(clrList__B.get(0).get("PLAN_ID").toString())) ;
				
					dao.delete(mPo__Del);									// 删除已提报相应数据
				}*/
				TtVsMonthlyPlanPO mPo__=new TtVsMonthlyPlanPO();
				TtVsMonthlyPlanPO mPo=new TtVsMonthlyPlanPO();
				for (int i=0; i<clrList__A.size(); i++) {
					System.out.println("+++++++++++++++++++++++" + "2".equals(actType) + i);
					if(clrList__A != null &&clrList__A.size()>0) {
						mPo.setPlanId(new Long(clrList__A.get(i).get("PLAN_ID").toString())) ;
						mPo__.setPlanId(new Long(clrList__A.get(i).get("PLAN_ID").toString())) ;
						mPo__.setStatus(Constant.PLAN_MANAGE_CONFIRM);
						mPo__.setPlanVer(new Integer(maxVer)+1);
					
						dao.update(mPo, mPo__) ;
					}
				}
			}
			
			String msg="";
			String flag ="" ;
			if("2".equals(actType)){
				msg=year+"年"+month+"月 月度任务下发已完成";
			}else{
				flag = "1" ;
				msg=year+"年"+month+"月 月度任务已保存";
			}
			act.setOutData("flag", flag) ;
			act.setOutData("msg", msg); 
			if("2".equals(actType)){
				act.setForword(monthPlanPublistCompleteUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
