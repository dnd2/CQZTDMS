package com.infodms.dms.actions.sales.accountsmanage;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.accountsmanage.DlrPayInquiryDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtDlrPayDetailsPO;
import com.infodms.dms.po.TtPurchaseInvoicePO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.po.TtVsDealerAccountPo;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DlrPayInquiry extends BaseDao{
	public Logger logger = Logger.getLogger(InvoiceManage.class);
	private ActionContext act = ActionContext.getContext();
	private DlrPayInquiryDAO dao = DlrPayInquiryDAO.getInstance();

	private final String dlrPayInquiryInitUrl = "/jsp/sales/accountsmanage/dlrPayInquiry.jsp";
	private final String dlrPayInfoInquiryUrl = "/jsp/sales/accountsmanage/dlrPayInfoQuery.jsp";
	private final String dlrPayFindInfoInquiryUrl = "/jsp/sales/accountsmanage/dlrPayFindInfoQuery.jsp";
	private final String dlrPayInfoDlrInquiryUrl = "/jsp/sales/accountsmanage/dlrPayInfoDlrQuery.jsp";
	private final String dlrPayAdd = "/jsp/sales/accountsmanage/dlrPayAdd.jsp";
	private final String dlrPayInquiryInitFindUrl = "/jsp/sales/accountsmanage/dlrPayInquiryFind.jsp";
	private final String dlrPayInquiryDlrInitUrl = "/jsp/sales/accountsmanage/dlrPayDlrInquiry.jsp";
	//经销商付款查询 页面初始化 2013-3-5
	public void dlrPayInquiryInit(){
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
			
			List<Map<String, Object>> accountType =dao.getAccountType();//zxf				
			act.setOutData("accountsTypeList", accountType);
			
			act.setForword(dlrPayInquiryInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dlrPayInquiryDlrInit(){
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
			
			List<Map<String, Object>> accountType =dao.getAccountType();//zxf				
			act.setOutData("dutyType", Constant.DUTY_TYPE_DEALER);
			act.setOutData("accountsTypeList", accountType);
			act.setForword(dlrPayInquiryDlrInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dlrPayInquiryInitFind(){
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
			
			List<Map<String, Object>> accountType =dao.getAccountType();//zxf				
			act.setOutData("accountsTypeList", accountType);
			
			act.setForword(dlrPayInquiryInitFindUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//经销商付款查询 初始页面查询 2013-3-5
	public void dlrPayInquiryInitQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");	
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			String dealerID = CommonUtils.checkNull(logonUser.getDealerId()); //得到经销商ID
			StringBuffer con = new StringBuffer();
			Long orgId= logonUser.getOrgId(); //组织ID
			String dutyType = logonUser.getDutyType(); //组织类型
			String	dealerId = dealerID.replace(",", "','"); //处理经销商id,由于存在二级经销商
			String dealerCodes=""; //单个经销商code
			String dealerCode=""; //多个经销商code
			String ticketNo = request.getParamValue("ticketNo");
			String payDate= request.getParamValue("payDate");
			String accountsType= request.getParamValue("accountsType");//得到帐户类型			
			String dutyDealerType= request.getParamValue("dutyType");
			//处理经销商code
			//if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
			if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())||Constant.DUTY_TYPE_DEALER.toString().equals(dutyDealerType)){
				dealerCodes=logonUser.getDealerCode();
			}else{
				dealerCode=act.getRequest().getParamValue("dealerCode");
				if("".equals(dealerCode)||dealerCode==null){
					dealerCodes=dealerCode;
				}else{
					dealerCodes=dealerCode.replace(",", "','");
				}
			}
			
			//票号
			if (ticketNo != null && !"".equals(ticketNo)) {
				con.append(" and tdpd.TICKET_NO like '%" + ticketNo +"%'");  
			}
			//付款日期
			if (payDate != null && !"".equals(payDate)) {
				//con.append(" and tdpd.PAY_DATE = TO_DATE('" + payDate +"','yyyy-MM-dd')\n");  
				con.append(" and TO_CHAR(tdpd.PAY_DATE,'yyyy-MM-dd') ='" +payDate.toString()+"'");
			}

			PageResult<Map<String, Object>> ps = new PageResult<Map<String, Object>>();
			if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())||Constant.DUTY_TYPE_DEALER.toString().equals(dutyDealerType)){
				ps = dao.dlrPayInquiryInitQuery(con.toString(),dealerId,curPage, 10,accountsType); //经销商端
			}else{
				ps = dao.dlrPayInquiryDeptInitQuery(con.toString(),dutyType,orgId,dealerCodes,curPage, 10,accountsType);//车厂端
			}
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	//经销商付款新增 2017-7-18
	public void dlrPayAdd(){
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try{	
				List<Map<String, Object>> accountType =dao.getAccountTypeNoReturn();//zxf				
				act.setOutData("accountsTypeList", accountType);
				act.setForword(dlrPayAdd);
			}catch(Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
		
	
	//经销商付款新增确认 2017-7-18
	public void dlrPayConfirm(){
				AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
				try{
					
					RequestWrapper request = act.getRequest();
					//String dealerCode = request.getParamValue("dealerCode");//2014070595975910
					//String dealerName = request.getParamValue("dealerName");
					String dealerId = request.getParamValue("dealerId");//经销商ID
					String ticketNumber = request.getParamValue("ticketNumber");
					String customerName = request.getParamValue("customerName");
					String accountsType = request.getParamValue("accountsType");
					String howMuch = request.getParamValue("howMuch");
					String payDay = request.getParamValue("payDate");
					String remark = request.getParamValue("remark");
					Double muchNumber;
					if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
						//ps = dao.dlrPayInquiryInitQuery(con.toString(),dealerId,curPage, 10); //经销商端
					}else{
						//ps = dao.dlrPayInquiryDeptInitQuery(con.toString(),dutyType,orgId,dealerCodes,curPage, 10);//车厂端
					}
					
					if(dealerId!=null&&accountsType!=null&&payDay!=null){
						//List<Map<String,Object>> dealerList = dao.getPayDetail(dealerId,accountsType);					
						TtDlrPayDetailsPO payDetail = new TtDlrPayDetailsPO();
						Long mseq=new Long(SequenceManager.getSequence(""));
						payDetail.setTicketId(mseq);						
						if(payDay!=null){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							payDetail.setPayDate(sdf.parse(payDay));
						}						
						payDetail.setContactDeptId(Long.parseLong(dealerId));					
						payDetail.setAccountTypeId(Long.parseLong(accountsType));
						if(howMuch==null){
							muchNumber =0.0;
						}
						else{
							muchNumber = Double.parseDouble(howMuch);														
						}
						payDetail.setPaySum(muchNumber);
						payDetail.setTicketNo(ticketNumber);
						payDetail.setRemark(remark);
						
//						if(dealerList!=null&&dealerList.size()>0){
//							TtDlrPayDetailsPO payDetail_ = new TtDlrPayDetailsPO();
//							payDetail_.setContactDeptId(Long.parseLong(dealerId));					
//							payDetail_.setAccountTypeId(Long.parseLong(accountsType));
//							dao.update(payDetail_, payDetail);
//						}
//						else
						{																
							dao.insert(payDetail);
						}
						
						//余额表
						Long userId = logonUser.getUserId();
						
						TtVsAccountTypePO accountInfo = getAccountInfo(accountsType);
						
						TtVsAccountPO vsAccount = new TtVsAccountPO();
						vsAccount.setDealerId(Long.parseLong(dealerId));
						vsAccount.setAccountTypeId(Long.parseLong(accountsType));
						List<Object> accountList = dao.select(vsAccount);
						//List<Map<String, Object>> accountList =dao.getDealerAccount(dealerId,accountsType);
						if(accountList!=null&&accountList.size()>0){
							TtVsAccountTypePO accountType = new TtVsAccountTypePO();
							
							accountType.setTypeId(Long.parseLong(accountsType));
							List<Object> accountTypeList =dao.select(accountType);								
							TtVsAccountPO vcAcountData = (TtVsAccountPO)accountList.get(0);								
							Double balance = vcAcountData.getBalanceAmount()+muchNumber;
							
							TtVsAccountPO vcAcountData2 = new TtVsAccountPO();
							vcAcountData2.setBalanceAmount(balance);
							vcAcountData2.setAccountCode(accountInfo.getTypeCode());
							vcAcountData2.setAccountName(accountInfo.getTypeName());
							vcAcountData2.setUpdateBy(userId);
							vcAcountData2.setUpdateDate(new Date());
							dao.update(vsAccount, vcAcountData2);
							
//							Double douNumber1=Double.parseDouble(accountList.get(0).get("ACCOUNT_BALANCE").toString());
//							//Double balance = douNumber1 + muchNumber;		
//							TtVsDealerAccountPo accountPo_ = new TtVsDealerAccountPo();
//							accountPo_.setAccountId(Long.parseLong(accountsType));
//							accountPo_.setDealerId(Long.parseLong(dealerId));
//							TtVsDealerAccountPo accountPo = new TtVsDealerAccountPo();
//							accountPo.setAccountBalance(balance);
//							accountPo.setUpdateBy(userId);
//							accountPo.setUpdateDate(new Date());
//							dao.update(accountPo_, accountPo);
						}
						else{
							Long accountId=new Long(SequenceManager.getSequence(""));  //发票单号
							vsAccount.setAccountId(accountId);
							vsAccount.setBalanceAmount(muchNumber);
							vsAccount.setAccountCode(accountInfo.getTypeCode());
							vsAccount.setAccountName(accountInfo.getTypeName());
							vsAccount.setCreateBy(userId);
							vsAccount.setCreateDate(new Date());
							vsAccount.setUpdateBy(userId);
							vsAccount.setUpdateDate(new Date());
							dao.insert(vsAccount);
//							Double balance =  muchNumber;	
//							TtVsDealerAccountPo accountPo = new TtVsDealerAccountPo();
//							accountPo.setAccountId(Long.parseLong(accountsType));
//							accountPo.setDealerId(Long.parseLong(dealerId));
//							accountPo.setAccountBalance(balance);
//							accountPo.setCreateDate(new Date());
//							accountPo.setCreateBy(userId);
//							accountPo.setUpdateBy(userId);
//							accountPo.setUpdateDate(new Date());
//							dao.insert(accountPo);
						}
					}
										
				}catch(Exception e){
					BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
					logger.error(logonUser,e1);
					act.setException(e1);
				}
	}
	
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
	
	public void dlrPayInfoDlrInquiry(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String ticketId = CommonUtils.checkNull(request.getParamValue("TICKET_ID"));
			TtDlrPayDetailsPO tdpd = new TtDlrPayDetailsPO(); //经销商付款查询 信息对象
			tdpd.setTicketId(Long.parseLong(ticketId));
			List<Object> list = new ArrayList<Object>();
			list = dao.select(tdpd);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
			}
			act.setForword(dlrPayInfoDlrInquiryUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//经销商付款查询 信息查询 2013-3-6
	public void dlrPayInfoInquiry(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String ticketId = CommonUtils.checkNull(request.getParamValue("TICKET_ID"));
			TtDlrPayDetailsPO tdpd = new TtDlrPayDetailsPO(); //经销商付款查询 信息对象
			tdpd.setTicketId(Long.parseLong(ticketId));
			List<Object> list = new ArrayList<Object>();
			list = dao.select(tdpd);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
			}
			act.setForword(dlrPayInfoInquiryUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dlrPayFindInfoInquiryUrl(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String ticketId = CommonUtils.checkNull(request.getParamValue("TICKET_ID"));
			TtDlrPayDetailsPO tdpd = new TtDlrPayDetailsPO(); //经销商付款查询 信息对象
			tdpd.setTicketId(Long.parseLong(ticketId));
			List<Object> list = new ArrayList<Object>();
			list = dao.select(tdpd);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
			}
			act.setForword(dlrPayFindInfoInquiryUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//根据往来单位ID获取往来单位名称
	public String getContactDeptName(){
		ActionContext act = ActionContext.getContext();
		TtDlrPayDetailsPO tdpd = new TtDlrPayDetailsPO();
		tdpd = (TtDlrPayDetailsPO)act.getOutData("map");
		Long contactDeptId = tdpd.getContactDeptId();
		return dao.getContactDeptNameById(contactDeptId);
	}
	
	//根据账户类型ID获得账户类型名称
	public String getAccountName(){
		ActionContext act = ActionContext.getContext();
		TtDlrPayDetailsPO tpi = new TtDlrPayDetailsPO();  
		tpi = (TtDlrPayDetailsPO)act.getOutData("map");
		Long accountTypeId = tpi.getAccountTypeId();
		return dao.getAccountNameById(accountTypeId);
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
