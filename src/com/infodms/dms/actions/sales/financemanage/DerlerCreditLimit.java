package com.infodms.dms.actions.sales.financemanage;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.accountsmanage.InvoiceManage;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.accountsmanage.DlrPayInquiryDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.financemanage.DerlerCreditDao;
import com.infodms.dms.dao.sales.planmanage.MonthPlanDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtDlrPayDetailsPO;
import com.infodms.dms.po.TtDlrPayDetailsTempPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.po.TtVsDealerAccountPo;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import jxl.Cell;

public class DerlerCreditLimit extends BaseImport{
	
	public Logger logger = Logger.getLogger(InvoiceManage.class);
	private ActionContext act = ActionContext.getContext();
	private DerlerCreditDao dao = DerlerCreditDao.getInstance();
	private final String derlercreditLimitUrl = "/jsp/sales/financemanage/derlercreditLimit.jsp";
	
	private final String derlercreditLimitFailureUrl = "/jsp/sales/financemanage/derlercreditLimitFail.jsp";
	private final String derlercreditLimitSuccessUrl = "/jsp/sales/financemanage/derlercreditLimitSuccess.jsp";
	private final String derlercreditLimitSuccessFinishUrl = "/jsp/sales/financemanage/rebateImportFinish.jsp";
	// DerlerCreditDao 
	//经销商付款查询 页面初始化 2013-3-5
	public void rebateTypeMaintenanceInit(){
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logonUser.getDutyType();//判断是否是经销商端还是车厂端
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("logonUser", logonUser);
			String dealerIds = logonUser.getDealerId();
			List<Map<String,Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds);
			act.setOutData("dealerList", dealerList);
			act.setForword(derlercreditLimitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void ExcelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		//DerlerCreditDao dao=DerlerCreditDao.getInstance();
		
		try{
			RequestWrapper request = act.getRequest();
			Long userId = logonUser.getUserId();
			TtDlrPayDetailsTempPO payDetail = new TtDlrPayDetailsTempPO();
			//清空临时表中目标年度的数据
			dao.delete(payDetail);
			
			int count=4;
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",count,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(derlercreditLimitFailureUrl);
			}
			else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmp(list,count);
				List<Map<String, Object>> temDate = dao.getDetailsTempDate();
				List<ExcelErrors> errorList = checkDate(temDate);
				if(errorList.size()>0){
					act.setOutData("errorList", errorList);
					act.setForword(derlercreditLimitFailureUrl);
				}
				else{
					List<Map<String, Object>> temDate1 = dao.getDetailsTempDate();//读取最新数据
					act.setOutData("dateList", temDate1);
					//checkedSave(temDate1);
					act.setForword(derlercreditLimitSuccessUrl);
				}
			}
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 导入业务表
	 */
	public void importExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			Long userId = logonUser.getUserId();
			List<Map<String, Object>> temDateList = dao.getDetailsTempDate();//读取最新数据
			List<Map<String, Object>> dealerList = dao.getDealerRebateAccount();
			String accountId = dealerList.get(0).get("TYPE_ID").toString();//返利帐唯
			checkedSave(temDateList,accountId);
			for(int i=0;i<temDateList.size();i++){
				Map<String, Object> temDatemap = temDateList.get(i);
				String dealerId= temDatemap.get("CONTACT_DEPT_ID").toString();
				String paySum = temDatemap.get("PAY_SUM").toString();					
				//upDateTtVsDealerAccount(dealerId,accountId,paySum,userId);//	
				upDateTtVsAccount(dealerId,accountId,paySum,userId);
			}				
			//act.setForword(derlercreditLimitUrl);
			act.setForword(derlercreditLimitSuccessFinishUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 更新经销商帐号余额
	 * */
	private void upDateTtVsAccount(String dealerId,String accountsId,String money,Long userId){
		TtVsAccountTypePO accountInfo = getAccountInfo(accountsId);
		
		TtVsAccountPO vsAccount = new TtVsAccountPO();
		vsAccount.setDealerId(Long.parseLong(dealerId));
		vsAccount.setAccountTypeId(Long.parseLong(accountsId));
		List<Object> accountList = dao.select(vsAccount);
		if(accountList!=null&&accountList.size()>0){
			TtVsAccountTypePO accountType = new TtVsAccountTypePO();
			
			accountType.setTypeId(Long.parseLong(accountsId));
			List<Object> accountTypeList =dao.select(accountType);								
			TtVsAccountPO vcAcountData = (TtVsAccountPO)accountList.get(0);								
			Double balance = vcAcountData.getBalanceAmount()+ Double.parseDouble(money);
			
			TtVsAccountPO vcAcountData2 = new TtVsAccountPO();
			vcAcountData2.setBalanceAmount(balance);
			vcAcountData2.setAccountCode(accountInfo.getTypeCode());
			vcAcountData2.setAccountName(accountInfo.getTypeName());
			vcAcountData2.setUpdateBy(userId);
			vcAcountData2.setUpdateDate(new Date());
			dao.update(vsAccount, vcAcountData2);			
		}
		else{
			Long accountId=new Long(SequenceManager.getSequence(""));  //发票单号
			vsAccount.setAccountId(accountId);
			vsAccount.setBalanceAmount(Double.parseDouble(money));
			vsAccount.setAccountCode(accountInfo.getTypeCode());
			vsAccount.setAccountName(accountInfo.getTypeName());
			vsAccount.setCreateBy(userId);
			vsAccount.setCreateDate(new Date());
			vsAccount.setUpdateBy(userId);
			vsAccount.setUpdateDate(new Date());
			dao.insert(vsAccount);
		}
		
	}
	
	/**
	 * 更新经销商帐号余额
	 * */
//	private void upDateTtVsDealerAccount(String dealerId,String accountsId,String money,Long userId){	
//		DlrPayInquiryDAO dao = DlrPayInquiryDAO.getInstance();
//		List<Map<String, Object>> accountList =dao.getDealerAccount(dealerId,accountsId);
//		if(accountList!=null&&accountList.size()>0){
//			Double douNumber1=Double.parseDouble(accountList.get(0).get("ACCOUNT_BALANCE").toString());	
//			TtVsDealerAccountPo accountPo_ = new TtVsDealerAccountPo();
//			accountPo_.setAccountId(Long.parseLong(accountsId));
//			accountPo_.setDealerId(Long.parseLong(dealerId));
//			TtVsDealerAccountPo accountPo = new TtVsDealerAccountPo();
//			accountPo.setAccountBalance(Double.parseDouble(money)+douNumber1);
//			accountPo.setUpdateBy(userId);
//			accountPo.setUpdateDate(new Date());
//			dao.update(accountPo_, accountPo);
//		}
//		else{
//			TtVsDealerAccountPo accountPo = new TtVsDealerAccountPo();
//			accountPo.setAccountId(Long.parseLong(accountsId));
//			accountPo.setDealerId(Long.parseLong(dealerId));
//			accountPo.setAccountBalance(Double.parseDouble(money));
//			accountPo.setCreateDate(new Date());
//			accountPo.setCreateBy(userId);
//			accountPo.setUpdateBy(userId);
//			accountPo.setUpdateDate(new Date());
//			dao.insert(accountPo);
//		}
//	}
//	
	
	/*
	 * 通过帐户类型ID，得到帐户信息
	 * */
	private TtVsAccountTypePO getAccountInfo(String accountTypeId){
		TtVsAccountTypePO accountType = new TtVsAccountTypePO();
		accountType.setTypeId(Long.parseLong(accountTypeId));
		List<Object> accountTypeList =dao.select(accountType);
		if(accountTypeList.size()>0){
			TtVsAccountTypePO accountData = (TtVsAccountTypePO)accountTypeList.get(0);
			return accountData;
		}
		return null;
	}
	
	/*
	 * 检查之后保存数据
	 * */
	private void checkedSave(List<Map<String, Object>> temDateList,String accountId){
		for(int i=0;i<temDateList.size();i++){
			Map<String, Object> temDatemap = temDateList.get(i);
			String dealerId= temDatemap.get("CONTACT_DEPT_ID").toString();
			String paySum = temDatemap.get("PAY_SUM").toString();
			String remark = String.valueOf(temDatemap.get("REMARK"));
			TtDlrPayDetailsPO payDetail= new TtDlrPayDetailsPO();	
			Long mseq=new Long(SequenceManager.getSequence(""));
			payDetail.setTicketId(mseq);
			payDetail.setPayDate(new Date());
			payDetail.setAccountTypeId(Long.parseLong(accountId));
			payDetail.setContactDeptId(Long.parseLong(dealerId));
			payDetail.setPayDate(new Date());
			payDetail.setPaySum(Double.parseDouble(paySum));
			payDetail.setRemark(remark);
			dao.insert(payDetail);
		}
	}
	
	private List<ExcelErrors> checkDate(List<Map<String, Object>> temDate){
		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
		int indexStart=2;
		
		List<Map<String, Object>> repeatCodeList = dao.getTempDealerRepeatData(false);		
		int repeatCodeListSize = repeatCodeList.size();		
		for(int i=0;i<repeatCodeListSize;i++){
			Map<String, Object> map= repeatCodeList.get(i);
			String dealerCode = map.get("CONTACT_DEPT_CODE").toString();
			String lineNumber = (Long.parseLong(map.get("ROM_NUM").toString())+1)+"";
			ExcelErrors erros = new ExcelErrors();
			erros.setRowNum(Integer.parseInt(lineNumber));
			erros.setErrorDesc("经销商代码\""+dealerCode+"\"重复");
			errorList.add(erros);
		}
				
		///检查名字重复情况
//		List<Map<String, Object>> repeatNameList = dao.getTempDealerRepeatData(true);		
//		int repeatNameListSize = repeatNameList.size();		
//		for(int i=0;i<repeatNameListSize;i++){
//			Map<String, Object> map= repeatNameList.get(i);
//			String dealerName = map.get("CONTACT_DEPT_SHORTNAME").toString();
//			String lineNumber = map.get("ROM_NUM").toString();
//			ExcelErrors erros = new ExcelErrors();
//			//erros.setRowNum(i+repeatCodeListSize);
//			erros.setRowNum(Integer.parseInt(lineNumber));
//			erros.setErrorDesc("经销简称\""+dealerName+"\"重复");
//			errorList.add(erros);
//		}
				
		if(repeatCodeListSize>0)//(repeatCodeListSize>0||repeatNameListSize>0){ //先去重
		{	
			return errorList;
		}
				
		int size = temDate.size();
		for(int i=0;i<size;i++){			
			Map<String, Object> map= temDate.get(i);
			// ACCOUNT_TYPE_ID,CONTACT_DEPT_ID,PAY_SUM,REMARK
			String shortName =String.valueOf(map.get("CONTACT_DEPT_SHORTNAME"));
			String dealerCode = map.get("CONTACT_DEPT_CODE").toString();//contact_dept_id
			String dealerPay = map.get("PAY_SUM").toString();
			List<Map<String, Object>> dealerIdList = dao.getDealerId(dealerCode);
			if(dealerIdList.size()!=0){//找到经销商
				updateDealerIdTemp(dealerCode,dealerIdList.get(0).get("DEALER_ID").toString());
				String dealerShortName = String.valueOf(dealerIdList.get(0).get("DEALER_SHORTNAME")); 
//				if(dealerShortName==""||!dealerShortName.equals(shortName)){ 不检查名字
//					ExcelErrors erros = new ExcelErrors();
//					erros.setRowNum(i+indexStart);
//					erros.setErrorDesc("经销商简称不对");
//					errorList.add(erros);
//				}
			}
			else{
				ExcelErrors erros = new ExcelErrors();
				erros.setRowNum(i+indexStart);
				erros.setErrorDesc("经销商代码不成在");
				errorList.add(erros);
			}
			
			
			//数字是否正确
			String[] split =  dealerPay.split("\\.");
			
			if(split.length>1){
				if(!isNumeric(split[0])||!isNumeric(split[1])){
					ExcelErrors erros = new ExcelErrors();
					erros.setRowNum(i+2);
					erros.setErrorDesc("请填正确的数字且精度为2位");
					errorList.add(erros);	
				}
				else{
					if(split[1].length()>1){
						ExcelErrors erros = new ExcelErrors();
						erros.setRowNum(i+2);
						erros.setErrorDesc("返利金额超出精度");
						errorList.add(erros);
					}
				}
			}
			else{
				if(!isNumeric(dealerPay)){
					ExcelErrors erros = new ExcelErrors();
					erros.setRowNum(i+2);
					erros.setErrorDesc("请填正确的数字且精度为2位");
					errorList.add(erros);	
				}
			}
		}	
		
		return errorList;
	}
	
	
	private  boolean isNumeric(String str){
		    for (int i = str.length();--i>=0;){  
		       if (!Character.isDigit(str.charAt(i))){
		    	   if(!".".equals(str.charAt(i))){
		    		   return false;
		    	   }
		        }
		    }
		    return true;
	}
	
	/**
	 * 升级临时表的的经销ID
	 * */
	private void updateDealerIdTemp(String dealerCode,String dealerId){
		TtDlrPayDetailsTempPO payDetail_ = new TtDlrPayDetailsTempPO();	
		TtDlrPayDetailsTempPO payDetail = new TtDlrPayDetailsTempPO();
		payDetail_.setContactDeptCode(dealerCode);
		payDetail.setContactDeptId(Long.parseLong(dealerId));
		dao.update(payDetail_, payDetail);
	}

	private void insertTmp(List<Map> list,int count){		
		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}			
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);			
				insertTempDoing(cells);				
			}
		}

	}	
	private void insertTempDoing(Cell[] cells){
		TtDlrPayDetailsTempPO payDetail = new TtDlrPayDetailsTempPO();	
		//Long mseq=new Long(SequenceManager.getSequence(""));
		//payDetail.setTicketId(mseq);
		//payDetail.setPayDate(new Date());
		
		payDetail.setContactDeptCode(cells[0].getContents().trim()); //cells[0].getContents().trim(); //得到 经销商代码				
		//payDetail.setAccountTypeId(Long.parseLong("123456"));//测试返利帐号
		payDetail.setContactDeptShortname(cells[1].getContents().trim());//经销商简称
		
		payDetail.setPaySum(cells[2].getContents().trim()); //cells[2].getContents().trim();//返利金额 		
		//payDetail.setTicketNo(ticketNumber);
		payDetail.setRemark(cells[3].getContents().trim()); //cells[3].getContents().trim();//返利备注 						
		payDetail.setRomNum(Long.parseLong(cells[0].getRow()+""));//行吗
		dao.insert(payDetail);		

	}
	
	
	
	/**
	 * 导入模板下载
	 * @author zouchao
	 * @since  2010-08-20
	 */
	public void downloadTemple(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 公司ID
			String companyId = CommonUtils.checkNull(logonUser.getCompanyId());
			// String areaId = request.getParamValue("buss_area");//业务范围
//			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();//导出模板第一列
			listHead.add("经销商代码");
			listHead.add("经销商名称");
			listHead.add("返利金额");
			listHead.add("备注");
//			List<Map<String, Object>> orgGroupList = dao.getGroupByOrg("", companyId, null);
//			for(int j=0;j<orgGroupList.size() ;j++){
//				listHead.add(orgGroupList.get(j).get("GROUP_CODE") != null ?orgGroupList.get(j).get("GROUP_NAME")+"("+ orgGroupList.get(j).get("GROUP_CODE")+")" : "");
//			}
			list.add(listHead);
//			List<Map<String, Object>> orgSeriesList = dao.getTempDownloadByOrg("",companyId, dealerId);
//			for(int i=0; i<orgSeriesList.size();i++){
//				List<Object> listValue = new LinkedList<Object>();
//				listValue.add(orgSeriesList.get(i).get("REGION_NAME") != null ? orgSeriesList.get(i).get("REGION_NAME") : "");
//				listValue.add(orgSeriesList.get(i).get("ORG_NAME") != null ? orgSeriesList.get(i).get("ORG_NAME") : "");
//				listValue.add(orgSeriesList.get(i).get("DEALER_CODE") != null ? orgSeriesList.get(i).get("DEALER_CODE") : "");
//				listValue.add(orgSeriesList.get(i).get("DEALER_NAME") != null ? orgSeriesList.get(i).get("DEALER_NAME") : "");
//				for(int j=0;j<orgGroupList.size() ;j++){
//					listValue.add("0");
//				}
//				list.add(listValue);
//			}
			
			// 导出的文件名
			String fileName = "返利导入模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
//			CsvWriterUtil.writeCsv(list, os);
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
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
