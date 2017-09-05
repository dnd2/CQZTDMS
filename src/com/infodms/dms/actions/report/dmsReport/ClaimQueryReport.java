package com.infodms.dms.actions.report.dmsReport;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PageResult;

public class ClaimQueryReport extends BaseAction{


	private final ClaimQueryDao dao = ClaimQueryDao.getInstance();
	private PageResult<Map<String, Object>> list=null;
	/**
	 * 索赔结算总汇查询
	 */
	public void claimAccountQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			list =dao.claimAccountQuery(request,loginUser,ActionUtil.getPageSize(request),getCurrPage());
			
			List<Map<String,Object>> sumList = dao.sumQuery(request,loginUser);
			this.nowDate();
			act.setOutData("sumAmount", sumList.get(0).get("AMOUNT"));
	        act.setOutData("sumNum", sumList.get(0).get("TS"));
			this.sendDataAndJsp("claimAccountReport", list);
			ActionUtil.setCustomPageSizeFlag(act, true);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动评估");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 索赔结算总汇导出
	 */
	public void expotDataClaimAccount(){
		list =dao.claimAccountQuery(request,loginUser,Constant.PAGE_SIZE_MAX,getCurrPage());
		dao.expotDataClaimAccount(act,list);
		this.sendDataAndJsp("claimAccountReport", list);
	}
	
	/**
	 * 月索赔配件数量TOP20查询
	 */
	public void partMonthlyClaimTOP20Query(){
		
		list =dao.partMonthlyClaimTOP20Query(request,Constant.PAGE_SIZE_MAX,getCurrPage());
		this.nowDate();
		this.sendDataAndJsp("partMonthlyClaimTOP20", list);
	}
	
	/**
	 * 月索赔配件数量TOP20明细查询
	 */
	public void partMonthlyClaimTOP20Dtl(){

		String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
		String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
		String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
		List<Map<String,Object>>  dtlList =dao.partMonthlyClaimTOP20Dtl(request,Constant.PAGE_SIZE_MAX,getCurrPage());
		act.setOutData("dtlList", dtlList);
		request.setAttribute("partCode", partCode);
		request.setAttribute("endTime", endTime);
		request.setAttribute("beginTime", beginTime);
		this.sendDataAndJsp("partMonthlyClaimTOP20Dtl", list);
	}
	
	/**
	 * 月索赔配件数量TOP20导出
	 */
	public void expotDataPartMonthlyClaimTOP20(){
		list =dao.partMonthlyClaimTOP20Query(request,Constant.PAGE_SIZE_MAX,getCurrPage());
		dao.expotDataPartMonthlyClaimTOP20(act,list);
		this.sendDataAndJsp("partMonthlyClaimTOP20", list);
	}
	/**
	 * 月索赔配件数量TOP20明细导出
	 */
	public void expotDataPartMonthlyClaimTOP20Dtl(){
		
		List<Map<String,Object>>  dtlList =dao.partMonthlyClaimTOP20Dtl(request,Constant.PAGE_SIZE_MAX,getCurrPage());
		dao.expotDataPartMonthlyClaimTOP20Dtl(act,dtlList);
		this.sendDataAndJsp("partMonthlyClaimTOP20Dtl", list);
	}
	
	/**
	 * 获取当前日期
	 */
	public void nowDate(){
		Calendar c=Calendar.getInstance();
		c.set(Calendar.DATE, 1);//把日期设置为当月第一天
		c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		act.setOutData("start",c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + "1");
		act.setOutData("end", c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + c.get(Calendar.DATE));
	}
	
	
	/**
	 * 公共的跳转报表
	 * @param jspName
	 */
	public void sendDataAndJsp(String jspName,PageResult<Map<String, Object>> list){
		this.act.setOutData("ps", list);
		String sendUrl = sendUrl(ClaimQueryReport.class, jspName);
		this.sendMsgByUrl(sendUrl, "售后报表");
	}
	
}
