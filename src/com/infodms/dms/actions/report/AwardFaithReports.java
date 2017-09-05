package com.infodms.dms.actions.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.AwardFaithDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 /**
 * 
 * @ClassName : DateTimeCheckManage
 * @Description : 授信详细信息
 * @author : ranjian
 *         CreateDate : 2013-9-12
 */
public class AwardFaithReports {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final AwardFaithDao adDao = AwardFaithDao.getInstance();
	private final String AWARD_FAITH_INIT_UTL = "/jsp/report/awardFaithReports.jsp";

	/**
	 * 
	 * @Title :
	 * @Description: 授信详细信息初始化
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-9-12
	 */
	public void awardFaithInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			List<Map<String, Object>> list_fund = adDao.getFundType();
			act.setOutData("list", list_fund);//产地LIST
			act.setForword(AWARD_FAITH_INIT_UTL);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "授信详细信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title :
	 * @Description: 授信详细信息查询
	 * @param :
	 * @return :
	 * @throws :
	 *         LastDate : 2013-9-12
	 */
	public void awardFaithQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			/****************************** 页面查询字段start **************************/
			String common = CommonUtils.checkNull(request.getParamValue("common"));//处理类型
			String staType = CommonUtils.checkNull(request.getParamValue("STA_TYPE"));//统计类型
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 物流商名称
			String ORDER_STARTDATE = CommonUtils.checkNull(request.getParamValue("ORDER_STARTDATE")); // 日期开始
			String ORDER_ENDDATE = CommonUtils.checkNull(request.getParamValue("ORDER_ENDDATE")); // 日期结束

			/****************************** 页面查询字段end ***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dealerCode", dealerCode);
			map.put("staType", staType);
			map.put("ORDER_STARTDATE", ORDER_STARTDATE);
			map.put("ORDER_ENDDATE", ORDER_ENDDATE);
			if ("2".equals(common))
			{//导出 调用
				List<Map<String, Object>> mapList = adDao.getAwardFaithExport(map);
				if (!"".equals(staType) && staType.equals(Constant.ACCOUNT_TYPE_04.toString()))
				{
					
			        String[] head={"序号","经销商","开票日期","发票号","车型","内部型号","车架号","颜色","提车价格(元)"};
			        String[] cols={"ROWNUM","DEALER_NAME","INVO_DATE","INVOICE_NO","MODEL_CODE","ERP_PACKAGE","VIN","COLOR_NAME","PRICE_COUNT"};
					ToExcel.toReportExcel(act.getResponse(), request, "授信详细信息(深发展银行).xls", head, cols, mapList);
				}
				else if (!"".equals(staType) && staType.equals(Constant.ACCOUNT_TYPE_03.toString()))
				{
				    String[] head={"序号","经销商","开票日期","发票号","内部型号","颜色","车型代码","车架号","金额(元)"};
			        String[] cols={"ROWNUM","DEALER_NAME","INVO_DATE","INVOICE_NO","ERP_PACKAGE","COLOR_NAME","MODEL_CODE","VIN","PRICE_COUNT"};
					ToExcel.toReportExcel(act.getResponse(), request, "授信详细信息(中信银行).xls", head, cols, mapList);
				}
				else
				{
					String[] head={"序号","经销商","开票日期","发票号","内部型号","合格证编号","发动机号","底盘号","车型","单价"};
				    String[] cols={"ROWNUM","DEALER_NAME","INVO_DATE","INVOICE_NO","ERP_PACKAGE","HEGEZHENG_CODE","ENGINE_NO","VIN","MODEL_CODE","PRICE_COUNT"};
					ToExcel.toReportExcel(act.getResponse(), request, "授信详细信息(招行融资).xls", head, cols, mapList);
				}
			}
			else
			{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = adDao.getAwardFaithQuery(map, curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		}
		catch (Exception e)
		{// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "授信详细信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
