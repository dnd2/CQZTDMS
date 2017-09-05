package com.infodms.dms.actions.sales.planmanage.YearTarget;

import java.util.Calendar;
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
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtVsYearlyPlanPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class SubYearTargetConfirm {
	
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String yearPlanConfrimSearchInit = "/jsp/sales/planmanage/yearplan/subyearplanconfrimsearchinit.jsp";
	private final String yearPlanConfrimSearch = "/jsp/sales/planmanage/yearplan/subyearplanconfrimsearch.jsp";
	private final String yearPlanConfrimCompleteUrl = "/jsp/sales/planmanage/yearplan/subyearlyplanconfirmcomplete.jsp";

	public void yearTargetConfirmSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			String options=getPlanYearString("");
			act.setOutData("options", options);
			act.setForword(yearPlanConfrimSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 取得当年和下一年的option列表
	 * year，如果需要确认select位置传入参数，否则为""
	 * @return
	 */
	private  String getPlanYearString(String year){
		StringBuffer sbf=new StringBuffer("");
		Calendar calendar=Calendar.getInstance();
		int s=calendar.get(Calendar.YEAR);
		sbf.append("<option value='"+calendar.get(Calendar.YEAR)+"'");
		if(!"".equals(year)&&year.equals(new Integer(s).toString())){
			sbf.append("selected='true'");
		}
		sbf.append(">");
		sbf.append(calendar.get(Calendar.YEAR)+"");
		sbf.append("</option>");
		calendar.add(Calendar.YEAR, 1);
		int ss=calendar.get(Calendar.YEAR);
		sbf.append("<option value='"+calendar.get(Calendar.YEAR)+"'");
		if(!"".equals(year)&&year.equals(new Integer(ss).toString())){
			sbf.append("selected='true'");
		}
		sbf.append(">");
		sbf.append(calendar.get(Calendar.YEAR)+"");
		sbf.append("</option>");
		return sbf.toString();
	}
	/*
	 * 未确认年度目标查询
	 * 按区域汇总车系数量
	 * 取出最大版本号，返回页面时+1
	 * 
	 */
	public void yearTargetConfirmSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			//确认目标年份
			String year =request.getParamValue("year");
			String areaId=request.getParamValue("buss_area");
			String planType=request.getParamValue("planType");
			TmVhclMaterialGroupPO conPo=new TmVhclMaterialGroupPO();
			conPo.setGroupLevel(new Integer(2));
			//查询所有车系
			List<Map<String, Object>> groupList=PlanUtil.selectAreaGroup( logonUser.getCompanyId().toString(),  2);
			//查询未确认年度目标汇总
			List<Map<String,Object>> list=dao.selectSubYearPlanUnConfirmed(groupList,year,areaId,planType, logonUser.getUserId(),logonUser.getOrgId(),logonUser.getCompanyId().toString());
			//将查询组装到二维数组中
			String[][] tableArr=orgData(list, groupList);
			act.setOutData("tableArr", tableArr);
			if(tableArr.length>0){
				//查询最大版本号
				Integer maxVer=dao.selectMaxPlanVer(year, logonUser.getUserId(),Constant.PLAN_MANAGE_CONFIRM,logonUser.getCompanyId().toString(),Constant.ORG_TYPE_DEALER.toString(),areaId,planType);
				act.setOutData("maxVer", maxVer+1);
			}
			 //取当前年和下一年的下拉列表
			String options=getPlanYearString(year);
			act.setOutData("options", options);
			act.setOutData("year", year);
			act.setOutData("areaId", areaId);
			act.setOutData("planType", planType);
			act.setForword(yearPlanConfrimSearch);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 组装一个二维数组，来存放年度目标汇总结果
	 */
	public String[][] orgData(List<Map<String, Object>> list,List<Map<String, Object>> groupList){
		String[][] tableArr=null;
		if(null==list||list.size()==0){
			tableArr=new String[0][0];
			return tableArr;
		}
		//所有车系
		if(null==groupList||groupList.size()==0){
			tableArr=new String[0][0];
			return tableArr;
		}
		int row=list.size()+2;
		int column=groupList.size()+3;
		String[] keyArr=new String[groupList.size()];
		tableArr=new String[row][column];
		tableArr[0][0]="区域";
		for(int i=1;i<=groupList.size();i++){
			Map<String, Object> map=groupList.get(i-1);
			tableArr[0][i]=map.get("GROUP_NAME").toString();
			keyArr[i-1]="A"+map.get("GROUP_ID").toString();
		}
		tableArr[0][column-2]="合计";
		tableArr[0][column-1]="贡献率";
		
		tableArr[row-1][0]="合计";
		Long total=new Long(0);
		for(int l=0;l<keyArr.length;l++){
			String key=keyArr[l];
			tableArr[row-1][l+1]=getTotalAmt(list, key);
			total+=new Long(tableArr[row-1][l+1]);
		}
		tableArr[row-1][column-2]=total.toString();
		tableArr[row-1][column-1]="100%";
		
		
		for(int j=0;j<list.size();j++){
			Map<String, Object> map=list.get(j);
			tableArr[j+1][0]=(String)map.get("DEALER_SHORTNAME");
			Long sum=new Long(0);
			for(int k=0;k<keyArr.length;k++){
				String key=keyArr[k];
				if(map.containsKey(key)){
					tableArr[j+1][k+1]=map.get(key.toString()).toString();
					sum+=new Long(tableArr[j+1][k+1]);
				}else{
					tableArr[j+1][k+1]="0";
					sum+=new Long(0);
				}
			}
			tableArr[j+1][column-2]=sum.toString();//合计
			tableArr[j+1][column-1]=total.doubleValue()==0?"0%":subStr((sum.doubleValue()/total.doubleValue()*100)+"")+"%";//贡献率
		}
		return tableArr;
	}

	/*
	 * 截取贡献率，取小数点后2位
	 */
	private String subStr(String key){
		if(null==key||"".equals(key)){
			return "0";
		}else if(key.indexOf(".")==-1){
			return key;
		}else{
			 key=key.substring(0,key.indexOf(".")+2);
			 return key;
		}
	}
	/*
	 * 计算所有车系汇总全计
	 */
	private String getTotalAmt(List<Map<String, Object>> list,String key){
		String amt="0";
		Long sum=new Long(0);
		for(int i=0;i<list.size();i++){
			Map map=(Map)list.get(i);
			if(map.containsKey(key)){
				amt=map.get(key).toString();
				sum+=new Long(amt);
			}
		}
		return sum.toString();
	}
	/*
	 * 区域年度目标确认
	 */
	public void ConfrimYearPlan(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String areaId=CommonUtils.checkNull(request.getParamValue("areaId"));
			String planType=CommonUtils.checkNull(request.getParamValue("planType"));
			String ver=CommonUtils.checkNull(request.getParamValue("plan_ver"));
			String desc=CommonUtils.checkNull(request.getParamValue("plan_desc"));
			//查询最大VER
			Integer maxVer=dao.selectMaxPlanVer(year, logonUser.getUserId(),Constant.PLAN_MANAGE_CONFIRM,logonUser.getCompanyId().toString(),Constant.ORG_TYPE_DEALER.toString(),areaId,planType);
			if(maxVer>=new Integer(ver)){
				throw new Exception("更新冲突");
			}
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("year", year);
			map.put("areaId", areaId);
			map.put("orgId", logonUser.getOrgId().toString());
			map.put("planType", planType);
			map.put("companyId", logonUser.getCompanyId().toString());
			
			
		/*	//条件PO
			TtVsYearlyPlanPO conPo=new TtVsYearlyPlanPO();
			conPo.setPlanYear(new Integer(year));
			conPo.setStatus(Constant.PLAN_MANAGE_UNCONFIRM);
			conPo.setOrgType(Constant.ORG_TYPE_DEALER);*/
			//更新值
			TtVsYearlyPlanPO valPo=new TtVsYearlyPlanPO();
			valPo.setPlanVer(new Integer(ver));
			valPo.setPlanDesc(desc);
			valPo.setStatus(Constant.PLAN_MANAGE_CONFIRM);
			valPo.setUpdateBy(logonUser.getUserId());
			valPo.setUpdateDate(new Date());
			//更新TT_YEARLY_PLAN确认年度目标
			//dao.update(conPo, valPo);
			dao.updateForConfrim(map, valPo);
			act.setOutData("year", year);
			act.setForword(yearPlanConfrimCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
