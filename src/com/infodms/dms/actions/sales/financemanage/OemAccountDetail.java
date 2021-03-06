/**********************************************************************
* <pre>
* FILE : OemAccountDetail.java
* CLASS : OemAccountDetail
* AUTHOR : 
* FUNCTION : 财务管理
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-31|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.financemanage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.delivery.DeliveryApply;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-31
 * @author  
 * @mail   	
 * @version 1.0
 * @remark 账户明细查询
 */
public class OemAccountDetail {
	
	public Logger logger = Logger.getLogger(DeliveryApply.class);   
	AccountBalanceDetailDao dao  = AccountBalanceDetailDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/financemanage/oemAccountDetail.jsp";
	/**
	 * 账户明细查询页面初始化
	 */
	public void oemAccountDetailInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR); 
			int month = calendar.get(Calendar.MONTH)+1; 
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String startDate = year+"-"+month+"-"+1;
			String endDate = year+"-"+month+"-"+day;
			List<Map<String,Object>> list = dao.getAccountType();
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("list", list);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 账户明细查询
	 */
	public void oemAccountDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startDate = request.getParamValue("startDate");			//开始时间
			String endDate = request.getParamValue("endDate");				//结束时间
			String dealerCode = request.getParamValue("dealerCode");		//经销商CODE
			String accountType = request.getParamValue("accountType");		//账户类型
			String changeType = request.getParamValue("changeType");		//变更类型
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 界面上没有此名称对应控件，用以方便以后有必要的时候加入
			String dutyType = logonUser.getDutyType() ;
			
			if(!Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i<areaBusList.size()-1) {
					areaIds.append(",");
				}
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.oemAccountDetailQuery(orgId, areaIds.toString(),areaId, startDate, endDate, dealerCode, accountType, changeType,  curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 账户明细查询
	 */
	public void oemAccountDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startDate = request.getParamValue("startDate");		//开始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String dealerCode = request.getParamValue("dealerCode");	//经销商CODE
			String accountType = request.getParamValue("accountType");	//账户类型
			Map<String, Object> map1 = dao.getOemStartAccount(startDate, dealerCode, accountType);
			Map<String, Object> map2 = dao.getOemEndAccount(endDate, dealerCode, accountType);
			Map<String, Object> map3 = dao.getDealerDetail(dealerCode);
			act.setOutData("returnValue", 1);
			if("".equals(map1.get("AMOUNT1"))||map1.get("AMOUNT1")==null){
				act.setOutData("amount1", "0");
			}else{
				act.setOutData("amount1", map1.get("AMOUNT1").toString());
			}
			if("".equals(map2.get("AMOUNT2"))||map2.get("AMOUNT2")==null){
				act.setOutData("amount2", "0");
			}else{
				act.setOutData("amount2", map2.get("AMOUNT2").toString());
			}
			if(map3.get("DEALERNAME")!=null&&map3.get("DEALERNAME")!=""){
			act.setOutData("dealerName", map3.get("DEALERNAME").toString());
			}
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 账户明细查询--下载
	 */
	public void oemAccountDetailDownLoad() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			String startDate = request.getParamValue("startDate");		//开始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String dealerCode = request.getParamValue("dealerCode");	//经销商CODE
			String accountType = request.getParamValue("accountType");	//账户类型
			String changeType = request.getParamValue("changeType");	//变更类型
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 界面上没有此名称对应控件，用以方便以后有必要的时候加入
			String dutyType = logonUser.getDutyType() ;
			
			if(!Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i<areaBusList.size()-1) {
					areaIds.append(",");
				}
			}
			/*Map<String, Object> map1 = dao.getOemStartAccount(startDate, dealerCode, accountType);
			Map<String, Object> map2 = dao.getOemEndAccount(endDate, dealerCode, accountType);
			Map<String, Object> map3 = dao.getDealerDetail(dealerCode);*/
			Map map = new HashMap();
			// 导出的文件名
			String fileName = "账户明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			PageResult<Map<String,Object>> ps = null;
			ps = dao.oemAccountDetailDownLoad(orgId, areaIds.toString(),areaId, startDate, endDate, dealerCode, accountType, changeType, 1, 99999);
			//设置期初期末余额
			/*List<Object> listTitle = new LinkedList<Object>();
				listTitle.add(" ");
				listTitle.add(""+startDate+"至"+endDate+map3.get("DEALERNAME").toString()+"("+dealerCode+")帐目往来明细");
				list.add(listTitle);
			List<Object> listamount = new LinkedList<Object>();
				listamount.add("期初余额：");
				listamount.add(""+map1.get("AMOUNT1") != null ? map1.get("AMOUNT1").toString() : "0");
				listamount.add("期末余额：");
				listamount.add(""+map2.get("AMOUNT2") != null ? map2.get("AMOUNT2").toString() : "0");
				list.add(listamount);*/
			//标题
			List<Object> listTemp = new LinkedList<Object>();
				listTemp.add("日期");
				listTemp.add("账户类型");
				listTemp.add("账户名称");
				listTemp.add("操作类型");
				listTemp.add("入账金额");
				listTemp.add("扣款金额");
				listTemp.add("凭证号码");
				listTemp.add("金税发票号");
				listTemp.add("外部单据号");
				listTemp.add("描述");
				list.add(listTemp);
			// 将page对象转换成LIST形式
			List<Map<String,Object>> rslist = ps.getRecords();
			if (null != rslist && rslist.size()>0) {
				for (int i = 0; i < rslist.size(); i++) {
					map = rslist.get(i);
					List<Object> listValue = new LinkedList<Object>();
					listValue = new LinkedList<Object>();
					listValue.add(map.get("CHNG_DATE") != null ? map.get("CHNG_DATE") : "");
					listValue.add(map.get("TYPE_NAME") != null ? map.get("TYPE_NAME") : "");
					listValue.add(map.get("ACCOUNT_NAME") != null ? map.get("ACCOUNT_NAME") : "");
					listValue.add(map.get("CHNG_TYPE") != null ? map.get("CHNG_TYPE") : "");
					listValue.add(map.get("CHANG_AMOUNT1") != null ? map.get("CHANG_AMOUNT1") : "");
					listValue.add(map.get("CHANG_AMOUNT2") != null ? map.get("CHANG_AMOUNT2") : "");
					listValue.add(map.get("ERP_DOC_NO") != null ? map.get("ERP_DOC_NO") : "");
					listValue.add(map.get("GOLDEN_INVOICE_NO") != null ? map.get("GOLDEN_INVOICE_NO") : "");
					listValue.add(map.get("EXTERNAL_DOC_NO") != null ? map.get("EXTERNAL_DOC_NO") : "");
					listValue.add(map.get("REMARK") != null ? map.get("REMARK") : "");
					list.add(listValue);
				}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户明细查询下载");
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
}
