package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage.DealerOrderQueryDAO;
import com.infodms.dms.dao.sales.financemanage.DealerQuotaDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class DealerDaQuotaQuery {
	private Logger logger = Logger.getLogger(OemSalesOrderQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final String initUrl="/jsp/sales/ordermanage/orderquery/dealerOrderQuery.jsp"; 
	private final DealerQuotaDao dao=DealerQuotaDao.getInstance();
	//private final OrderQueryDao dao = OrderQueryDao.getInstance();
	//private final OrderReportDao reportDao = OrderReportDao.getInstance();
	public void dealerSalesOrderQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.setForword(initUrl);
	}
	
	/**
	 * 发运指令查询
	 */
	public void commandQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealers=logonUser.getDealerId();							//获取经销商
			String startDate = request.getParamValue("startDate");			//发运起始时间
			String endDate = request.getParamValue("endDate");				//发运结束时间
			String orderNo = request.getParamValue("orderNo");				//发运订单号码
			String dealerCode =request.getParamValue("dealerCode");			//采购经销商CODE
			Long companyId = logonUser.getCompanyId();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getDealerQueryList(dealers,startDate, endDate,dealerCode,orderNo,companyId,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"发运指令查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void commandQueryDownLoad(){
		OutputStream os = null;
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerIds=logonUser.getDealerId();							//获取经销商
			String startDate = request.getParamValue("startDate");			//发运起始时间
			String endDate = request.getParamValue("endDate");				//发运结束时间
			String orderNo = request.getParamValue("orderNo");				//发运订单号码
			String dealerCode =request.getParamValue("dealerCode");			//采购经销商CODE
			Long companyId = logonUser.getCompanyId();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			// 导出的文件名
			String fileName = "代交车模板下载.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("采购经销商代码");
			listTemp.add("采购单位");
			listTemp.add("发运日期");
			listTemp.add("发运数量");
			listTemp.add("发运订单号");
			listTemp.add("状态");
			listTemp.add("集团客户");
			listTemp.add("集团客户地址");
			list.add(listTemp);
			
			List<Map<String, Object>> results = dao.getDealerQueryLoad(dealerIds, startDate, endDate, dealerCode, orderNo, companyId, curPage, 99999);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("CODE_DESC")));
				listTemp.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("FLEET_ADDRESS")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"发运指令查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
