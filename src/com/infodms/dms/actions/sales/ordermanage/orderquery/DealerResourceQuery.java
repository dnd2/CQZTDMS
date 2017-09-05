package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.DealerResourceQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerResourceQuery extends BaseDao{

	public Logger logger = Logger.getLogger(DlvryAmountQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final DealerResourceQueryDao dao = new DealerResourceQueryDao ();
	public static final DealerResourceQueryDao getInstance() {
		return dao;
	}
	private final String  resourceQueryInitUrl = "/jsp/sales/ordermanage/orderquery/resourceQueryInit.jsp";
	
	public void resourceQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR); 
			int month = calendar.get(Calendar.MONTH)+1; 
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String startDate = year+"-"+month+"-"+"1";
			String endDate = year+"-"+month+"-"+day;
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("areaList", areaList);
			act.setForword(resourceQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "发运单数量统计页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void resourceQueryList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String orderOrgCode = CommonUtils.checkNull(request.getParamValue("orderOrgCode"));//定货方经销商
			String billingOrgCode = CommonUtils.checkNull(request.getParamValue("billingOrgCode"));//开票方经销商
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1 ; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.getRsourceList(orgId, areaId,orderOrgCode, billingOrgCode, startDate, endDate, Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "发运单数量统计显示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void resourceQueryListDownLoad(){
		AclUserBean logonUser = null;
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			Map map = new HashMap();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String orderOrgCode = CommonUtils.checkNull(request.getParamValue("orderOrgCode"));//定货方经销商
			String billingOrgCode = CommonUtils.checkNull(request.getParamValue("billingOrgCode"));//开票方经销商
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			// 导出的文件名
			String fileName = "发运订单汇总查询.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getRsourceList(orgId, areaId,orderOrgCode, billingOrgCode, startDate, endDate, 9999, curPage);
			listTemp.add("开票经销商区域");
			listTemp.add("开票经销商代码");
			listTemp.add("开票经销商名称");
			listTemp.add("订货经销商代码");
			listTemp.add("订货经销商名称");
			listTemp.add("配置代码");
			listTemp.add("已提报");
			listTemp.add("代交车审核");
			listTemp.add("经销商待确认");
			listTemp.add("初审完成");
			listTemp.add("审核完成");
			list.add(listTemp);
			List<Map<String, Object>> rslist = ps.getRecords();
			for (int i = 0; i < rslist.size(); i++) {
				map = rslist.get(i);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("ORG_NAME") != null ? map.get("ORG_NAME") : "");
				listValue.add(map.get("BILLINGORGCODE") != null ? map.get("BILLINGORGCODE") : "");
				listValue.add(map.get("BILLINGORGNAME") != null ? map.get("BILLINGORGNAME") : "");
				listValue.add(map.get("ORDERORGCODE") != null ? map.get("ORDERORGCODE") : "");
				listValue.add(map.get("ORDERORGNAME") != null ? map.get("ORDERORGNAME") : "");
				listValue.add(map.get("STATUSCODE") != null ? map.get("STATUSCODE") : "");
				listValue.add(map.get("HAS_COMMIT") != null ? map.get("HAS_COMMIT") : "");
				listValue.add(map.get("DJC_CHECK") != null ? map.get("DJC_CHECK") : "");
				listValue.add(map.get("DEALERCOMMIT") != null ? map.get("DEALERCOMMIT") : "");
				listValue.add(map.get("PRE_CHECK") != null ? map.get("PRE_CHECK") : "");
				listValue.add(map.get("HAS_CHECK") != null ? map.get("HAS_CHECK") : "");
				list.add(listValue);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "发运订单汇总查询:下载");
			logger.error(logonUser,e1);
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
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
