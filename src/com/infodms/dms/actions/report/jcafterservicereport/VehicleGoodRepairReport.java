package com.infodms.dms.actions.report.jcafterservicereport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

/**
 *车辆保养记录
 */
public class VehicleGoodRepairReport {
	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/vehicleGoodRepairReport.jsp";
	private final String dealerAppDetail = "/jsp/report/jcafterservicereport/dealerAppDetail.jsp";
	private final String OEM_BALANCE_DEDUCT = "/jsp/report/jcafterservicereport/oemBalanceDeductReport.jsp";
	private final String DLR_BALANCE_DEDUCT = "/jsp/report/jcafterservicereport/dlrBalanceDeductReport.jsp";
	
	public void vehicleGoodRepairReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆保养记录");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void vehicleGoodRepairReportReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String vin = request.getParamValue("vin");//车型大类
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.vehicleGoodRepairReportReport(vin,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆维修记录");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryVehicleGoodRepairReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String vin = request.getParamValue("vin");
			String[] head=new String[10];
			head[0]="序号";
			head[1]="VIN码";
			head[2]="发动机号";
			head[3]="用户姓名";
			head[4]="维修站";
			head[5]="购车日期";
			head[6]="保养时间";
			head[7]="行驶里程";
			head[8]="出厂日期";
			head[9]="保养次数";
			List<Map<String, Object>> list= dao.queryVehicleGoodRepairReportExcel(vin);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[10];
						detail[0] = String.valueOf(map.get("NUM"));
						detail[1] = String.valueOf(map.get("VIN"));
						detail[2] = String.valueOf(map.get("ENGINE_NO")==null?"":map.get("ENGINE_NO"));
						detail[3] = String.valueOf(map.get("OWNER_NAME")==null?"":map.get("OWNER_NAME"));
						detail[4] = String.valueOf(map.get("DEALER_SHORTNAME")==null?"":map.get("DEALER_SHORTNAME"));
						detail[5] = String.valueOf(map.get("PURCHASED_DATE")==null?"":map.get("PURCHASED_DATE"));
						detail[6] = String.valueOf(map.get("RO_CREATE_DATE")==null?"":map.get("RO_CREATE_DATE"));
						detail[7] = String.valueOf(map.get("MILEAGE")==null?"":map.get("MILEAGE"));
						detail[8] = String.valueOf(map.get("PRODUCT_DATE")==null?"":map.get("PRODUCT_DATE"));
						detail[9] = String.valueOf(map.get("FREE_TIMES")==null?"":map.get("FREE_TIMES"));
						list1.add(detail);
			    	}
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toExcel(ActionContext.getContext().getResponse(), request, head, list1);
		    }
		    act.setForword(initUrl);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"轿车保养VIN报表");
				logger.error(logonUser,e1);
				act.setException(e1);
			}	
	}
	/**
	 * Iverson add By 2010-12-10
	 * 经销商索赔申报审核明细
	 */
	public void dealerAppDetailInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(dealerAppDetail);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商索赔申报审核明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Iverson add By 2010-12-10
	 * 经销商索赔申报审核明细(分页)
	 */
	public void queryDealerAppDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String balanceNo = request.getParamValue("balanceNo");//结算单号
			String claimType = request.getParamValue("claimType");//索赔类型
			String STATUS = request.getParamValue("STATUS");//索赔单据状态
			String START_DATE = request.getParamValue("beginTime");//审核通过开始时间
			String END_DATE = request.getParamValue("endTime");//审核通过结束时间
			String YIELDLY = request.getParamValue("YIELDLY");//基地
			//当开始时间和结束时间相同时
			if(null!=START_DATE&&!"".equals(START_DATE)&&null!=END_DATE&&!"".equals(END_DATE)){
				START_DATE = START_DATE+" 00:00:00";
				END_DATE = END_DATE+" 23:59:59";
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("balanceNo", balanceNo);
			map.put("claimType", claimType);
			map.put("STATUS", STATUS);
			map.put("START_DATE", START_DATE);
			map.put("END_DATE", END_DATE);
			map.put("YIELDLY", YIELDLY);
			map.put("dealerId", logonUser.getDealerId());
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDealerAppDetailDao(map,curPage,400);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用申报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * yx add By 2010-12-21
	 * 车厂结算单扣款查询
	 */
	public void queryOemBalanceDeduct(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String balanceNo = request.getParamValue("balanceNo");//结算单号
				String dealerCode = request.getParamValue("dealerCode"); 
				String yieldly = request.getParamValue("yieldly"); 
				String beginTime = request.getParamValue("beginTime"); 
				String endTime = request.getParamValue("endTime"); 
				//当开始时间和结束时间相同时
				if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
				}
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
				PageResult<Map<String, Object>> ps = dao.getBalanceDeduct(balanceNo,dealerCode,
						yieldly,beginTime,endTime,curPage,400);
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}else{
				act.setForword(OEM_BALANCE_DEDUCT);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单扣款报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * yx add By 2010-12-21
	 * 经销商结算单扣款查询
	 */
	public void queryDlrBalanceDeduct(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String balanceNo = request.getParamValue("balanceNo");//结算单号
				String yieldly = request.getParamValue("yieldly"); 
				String beginTime = request.getParamValue("beginTime"); 
				String endTime = request.getParamValue("endTime"); 
				//当开始时间和结束时间相同时
				if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
				}
				//分页方法 begin
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页	
				PageResult<Map<String, Object>> ps = dao.getBalanceDeduct(balanceNo,logonUser.getDealerCode(),
						yieldly,beginTime,endTime,curPage,400);
				//分页方法 end
				act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
			}else{
				act.setForword(DLR_BALANCE_DEDUCT);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"结算单扣款报表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
