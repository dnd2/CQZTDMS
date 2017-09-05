package com.infodms.dms.actions.sales.financemanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.accountsmanage.InvoiceManage;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.accountsmanage.DlrPayInquiryDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.financemanage.RebateDetailQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtDlrPayDetailsPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class RebateDetailQuery extends BaseDao{

	public Logger logger = Logger.getLogger(RebateDetailQuery.class);
	private ActionContext act = ActionContext.getContext();
	private RebateDetailQueryDao dao = RebateDetailQueryDao.getInstance();
	private final String rebateDetailQueryInitUrl = "/jsp/sales/financemanage/rebateDetailQueryInit.jsp";
	private final String rebateInfoQuery = "/jsp/sales/financemanage/rebateInfoQuery.jsp";
	public void rebateDetailInit(){
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
				act.setForword(rebateDetailQueryInitUrl);
			}catch(Exception e){
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	    }

		//返利查询
		public void rebateInquiryInitQuery(){
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
				
				//处理经销商code
				if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
					dealerCodes=logonUser.getDealerCode();
				}else{
					dealerCode=act.getRequest().getParamValue("dealerCode");;
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
				if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
					ps = dao.dlrPayInquiryInitQuery(con.toString(),dealerId,curPage, 10); //经销商端
				}else{
					ps = dao.dlrPayInquiryDeptInitQuery(con.toString(),dutyType,orgId,dealerCodes,curPage, 10);//车厂端
				}
				act.setOutData("ps", ps);
			}catch(Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," ");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}

		//经销商返利查询 信息查询 2013-3-6
		public void rebateInfoInquiry(){
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
				act.setForword(rebateInfoQuery);
			}catch(Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		
		
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
