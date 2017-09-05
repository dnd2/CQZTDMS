package com.infodms.dms.actions.report.feedbackmngreport;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.repairOrder.RoMaintainMain;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.MarketBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.feedbackmngreport.WorkSummaryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 市场处理工单处理汇总报表
 * @author 
 *
 */
public class WorkSummary {
	private Logger logger = Logger.getLogger(RoMaintainMain.class);
	private final WorkSummaryDao dao = WorkSummaryDao.getInstance();
	private final String WORK_SUMMARY_INIT = "/jsp/report/workSummary.jsp";// 主页面（查询）
	
	/**
	 * 报表初始页面跳转
	 */
	public void workSummaryInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(WORK_SUMMARY_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "市场处理工单处理汇总报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 报表查询
	 */
	public void query()
	{
		ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	try{
    		RequestWrapper request = act.getRequest();  
    		Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
    		String startDate = request.getParamValue("startDate");
    		String endDate = request.getParamValue("endDate");
    		String orgId = request.getParamValue("orgId");
    		String dealerId = request.getParamValue("dealerId");
    		String status = request.getParamValue("status");
    		PageResult<Map<String,Object>> rs=dao.query(startDate,endDate,orgId,dealerId,status,15,curPage);
    		act.setOutData("ps", rs);
    		act.setOutData("flag",true);
    	}catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "市场处理工单处理汇总报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 报表导出
	 */
	public void download()
	{
		OutputStream os = null;
		ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); 
    	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");    	  
    	try{
    		RequestWrapper request = act.getRequest();      		
    		String startDate = request.getParamValue("startDate");
    		String endDate = request.getParamValue("endDate");
    		String orgId = request.getParamValue("orgId");
    		String dealerId = request.getParamValue("dealerId");
    		String status = request.getParamValue("status");
    		List<MarketBean> ps=dao.download(startDate,endDate,orgId,dealerId,status);
    		
    		//---导出-----------------------------------------
    		ResponseWrapper response = act.getResponse();
    		// 导出文件名称
    		String fileName = "市场处理工单处理情况汇总表.csv";
    		// 导出的文字编码
    		fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
    		response.setContentType("Application/text/csv");
    		response.addHeader("Content-Disposition", "attachment;filename="+ fileName);			
    		List<List<Object>> list = new LinkedList<List<Object>>();
    		List<Object> listTemp = new LinkedList<Object>();
    		listTemp.add("大区");
    		listTemp.add("服务中心");
    		listTemp.add("申请日期");
    		listTemp.add("车型");
    		listTemp.add("车架号");
    		listTemp.add("客户姓名");
    		listTemp.add("投诉类型");
    		listTemp.add("涉及金额");
    		listTemp.add("审批通过人（区域服务经理姓名）");
    		listTemp.add("审批通过人（大区经理姓名）");
    		listTemp.add("客户服务部审批情况（审批通过人姓名）");    		
    		list.add(listTemp);		
    		if(ps!=null)
    		{
    			for(int i=0;i<ps.size();i++)
    			{
    				listTemp = new LinkedList<Object>();
    				MarketBean po=ps.get(i);
    				listTemp.add(po.getOrgName());
    				listTemp.add(po.getDealerName());
    				listTemp.add(formatter.format(po.getOrderDate()));
    				listTemp.add(po.getModelCode());
    				listTemp.add(po.getVin());
    				listTemp.add(po.getCtmName());
    				listTemp.add(po.getCompType());
    				listTemp.add(po.getMoney());
    				listTemp.add(po.getAuditByQy());
    				listTemp.add(po.getAuditByDq());
    				listTemp.add(po.getAuditByKf());
    				list.add(listTemp);
    			}
    		}
    		os = response.getOutputStream();
    		CsvWriterUtil.writeCsv(list, os);
    		os.flush();
    	}catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "市场处理工单处理汇总报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

}
