/**********************************************************************
* <pre>
* FILE : OemAccountBalance.java
* CLASS : OemAccountBalance
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
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.impl.Common;

/**
 * @Title: 
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-31
 * @author  
 * @mail   
 * @version 1.0
 * @remark 账户余额查询
 */
public class OemAccountBalance {
	
	public Logger logger = Logger.getLogger(DeliveryApply.class);   
	AccountBalanceDetailDao dao  = AccountBalanceDetailDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/financemanage/oemAccountBalance.jsp";
	private final String initInfoUrl = "/jsp/sales/financemanage/dlrAccountInfo.jsp";
	/**
	 * 账户余额查询页面初始化
	 */
	public void oemAccountBalanceInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			
			
			List typeList = dao.getAccountTypeByCompanyId(logonUser.getCompanyId());
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("typeList", typeList);
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 账户余额查询
	 */
	public void oemAccountBalanceQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCode = request.getParamValue("dealerCode");		//经销商CODE
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
			Long companyId = logonUser.getCompanyId();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.oemAccountBalanceQuery(orgId, areaIds.toString(),areaId, dealerCode, companyId,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 根据经销商加载
	 */
	public void getDealerInfo(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
		String dealerId=request.getParamValue("dealerId");//经销商编码
		Long companyId = logonUser.getCompanyId();
		List myList1 = dao.oemAccountBalanceQueryInfo(dealerId, companyId);
		List<Map<String, Object>> specialList = dao.getSpecialReqAccountFreezeInfo(dealerId);
		Map<String, Object> map=dao.getAccountHj(dealerId, companyId);
		act.setOutData("map1", map);
		act.setOutData("myList1", myList1);
		act.setOutData("dealerId", dealerId);
		act.setOutData("specialList", specialList);
		act.setForword(initInfoUrl);
	}catch(Exception e) {//异常方法
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询");
		logger.error(logonUser,e1);
		act.setException(e1);
	}
	}
	public void queryFreezeList(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String dealerId=request.getParamValue("dealerId");//经销商编码
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps =dao.getFreezeList(dealerId,Constant.PAGE_SIZE_MAX, curPage);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 账户余额查询--下载
	 */
	public void oemAccountBalanceDownLoad() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");		//经销商CODE
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId")); // 界面上没有此名称对应控件，用以方便以后有必要的时候加入
			String dutyType = logonUser.getDutyType() ;
			
			if(!Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			Long companyId = logonUser.getCompanyId();
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
			ResponseWrapper response = act.getResponse();
			Map map = new HashMap();
			// 导出的文件名
			String fileName = "账户余额.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listTemp = new LinkedList<Object>();
			List typeList = dao.getAccountTypeByCompanyId(logonUser.getCompanyId());
			PageResult<Map<String,Object>> ps = null;
			ps = dao.oemAccountBalanceQuery(orgId, areaIds.toString(),areaId, dealerCode,companyId,1,99999);
				listTemp.add("经销商代码");
				listTemp.add("经销商名称");
				for(int i=0;i<typeList.size();i++){
					TtVsAccountTypePO po=(TtVsAccountTypePO) typeList.get(i);
					listTemp.add(po.getTypeName());
				}
				listTemp.add("可用合计");
				list.add(listTemp);
				// 将page对象转换成LIST形式
				List<Map<String,Object>> rslist = ps.getRecords();
				for (int i = 0; i < rslist.size(); i++) {
					map = rslist.get(i);
					List<Object> listValue = new LinkedList<Object>();
					listValue = new LinkedList<Object>();
					listValue.add(map.get("DEALER_CODE") != null ? map.get("DEALER_CODE") : "");
					listValue.add(map.get("DEALER_NAME") != null ? map.get("DEALER_NAME") : "");
					for(int k=0;k<typeList.size();k++){
						TtVsAccountTypePO po=(TtVsAccountTypePO) typeList.get(k);
						listValue.add(map.get(po.getTypeName()));
					}
					listValue.add(map.get("KYHJ") != null ? map.get("KYHJ") : "");
					list.add(listValue);
				}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"账户余额查询下载");
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
