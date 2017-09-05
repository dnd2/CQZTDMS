package com.infodms.dms.actions.claim.application;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.disthubmq.client.Factory;
import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.application.DealerKpDao;
import com.infodms.dms.dao.claim.application.DealerNewKpUpdateDAO;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrInvoiceChangePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
/***************
 * 
 * @author ray
 *
 */
 
public class DealerNewKpUpdate {
	private Logger logger = Logger.getLogger(DealerBalance.class);
	protected POFactory factory = POFactoryBuilder.getInstance();
	
	/***********
	 * 主页查询跳转
	 */
	public void mainUrl(){
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			act.setForword("/jsp/claim/application/dealerKpUpdate.jsp");
	}
	
	/***********
	 * 主页查询跳转
	 */
	public void OEMmainUrl(){
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			act.setForword("/jsp/claim/application/oemDealerKpUpdate.jsp");
	}
	/**********
	 * 查询SQL
	 */
	public void viewDealerNewKp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper req = act.getRequest();
			String  balanceNo = req.getParamValue("balanceNo");
			String  changeStatus = req.getParamValue("CHANGE_STATUS");
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage"))
					: 1; // 处理当前页
		
			DealerNewKpUpdateDAO dao = new DealerNewKpUpdateDAO();
			PageResult<Map<String, Object>> ps = dao.viewInvoiceMarkChange(balanceNo,logonUser.getDealerId(),changeStatus,curPage,Constant.PAGE_SIZE);
			 act.setOutData("ps",ps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	/**********
	 * OEM查询SQL
	 */
	public void viewDealerNewKpOem(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper req = act.getRequest();
			String  balanceNo = req.getParamValue("balanceNo");
			String  changeStatus = req.getParamValue("CHANGE_STATUS");
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage"))
					: 1; // 处理当前页
		
			DealerNewKpUpdateDAO dao = new DealerNewKpUpdateDAO();
			PageResult<Map<String, Object>> ps = dao.viewInvoiceMarkChangeOem(balanceNo,logonUser.getDealerId(),changeStatus,curPage,Constant.PAGE_SIZE);
			 act.setOutData("ps",ps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/************
	 * 新增跳转
	 */
	public void addDealerKp(){
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
	    	
			act.setForword("/jsp/claim/application/addDealerKpUpdate.jsp");
	}
	
	/*********************
	 * 保存
	 */
	public void addDealerKpSave(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper req = act.getRequest();
			String balanceNo    = req.getParamValue("BALANCE_NO");
			String dealerId   	= req.getParamValue("did");
			String dealerCode 	= req.getParamValue("dealerCode"); 
			String dealerName 	= req.getParamValue("dealerName"); 
			String yilid    	= req.getParamValue("yilidname");
			String invoiceMark  = req.getParamValue("oldDealerName");
			String dealerCodeNew= req.getParamValue("dealerCodeNew");
			String dealerIdNew  = req.getParamValue("dealerIdNew");
			String remark		= req.getParamValue("remark");
			String mouths	    = req.getParamValue("mouths");
			String invoiceCode  = req.getParamValue("dealerC");
			TtAsWrInvoiceChangePO po = new TtAsWrInvoiceChangePO();
			Long id =Utility.getLong(SequenceManager.getSequence(""));
			po.setId( id);
			po.setBalanceNo(balanceNo);
		    po.setChangeNo(SequenceManager.getSequence("BGKP"));
		    po.setDelaerid(Long.valueOf(dealerId));
		    po.setDealercode(dealerCode);
		    po.setDealername(dealerName);
		    po.setInvoiceMark(invoiceMark);
		    po.setNewInvoiceId(Long.valueOf(dealerIdNew));
		    po.setNewInvoiceMark(dealerCodeNew);
		    po.setCreateDate(new Date());
		    po.setCreateBy(logonUser.getUserId().toString());
		    po.setRemark(remark);
		    po.setYieldly(Integer.valueOf(yilid));
		    po.setStatus(Constant.CHANGE_STATUS_1);
		    po.setInvoiceDate(mouths);
		    po.setNewInvoiceCode(invoiceCode);
		    factory.insert(po);
			// 附件功能
			String ywzj = id.toString();
			String[] fjids = req.getParamValues("fjids");
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
		    act.setOutData("success","true");
		    
		    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 act.setOutData("success","false");
		}
		
	}

	/*****
	 * 
	 * 修改页面跳转
	 */
	    
	 public void modifyDealerKp(){
		 ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				RequestWrapper req = act.getRequest();
				String id    = req.getParamValue("ID");
				DealerNewKpUpdateDAO dao = new DealerNewKpUpdateDAO();
				ClaimBillMaintainDAO cDao = new ClaimBillMaintainDAO();
				List<Map<String, Object>> list = dao.modifyInvoiceMarkChange(id);
				List<FsFileuploadPO> attachLs = cDao.queryAttById(id);// 取得附件
				act.setOutData("list", list.get(0));
				act.setOutData("attachLs", attachLs);
				act.setForword("/jsp/claim/application/modifyDealerKpUpdate.jsp");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 
	 public void viewDealerKp(){
		 ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				RequestWrapper req = act.getRequest();
				String id    = req.getParamValue("id");
				DealerNewKpUpdateDAO dao = new DealerNewKpUpdateDAO();
				ClaimBillMaintainDAO cDao = new ClaimBillMaintainDAO();
				List<Map<String, Object>> list = dao.modifyInvoiceMarkChange(id);
				List<FsFileuploadPO> attachLs = cDao.queryAttById(id);// 取得附件
				act.setOutData("list", list.get(0));
				act.setOutData("attachLs", attachLs);
				act.setForword("/jsp/claim/application/viewDealerKpUpdate.jsp");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 
	 public void modifyDealerKpSave(){
		 ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			try {
				RequestWrapper req = act.getRequest();
				String id    = req.getParamValue("id");
				String ywzj = id;
				String[] fjids = req.getParamValues("fjid");
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
				String balanceNo    = req.getParamValue("BALANCE_NO");
				String dealerId   	= req.getParamValue("did");
				String dealerCode 	= req.getParamValue("dealerCode"); 
				String dealerName 	= req.getParamValue("dealerName"); 
				String yilid    	= req.getParamValue("yilidname");
				String invoiceMark  = req.getParamValue("oldDealerName");
				String dealerCodeNew= req.getParamValue("dealerCodeNew");
				String dealerIdNew  = req.getParamValue("dealerIdNew");
				String remark		= req.getParamValue("remark");
				String mouths	    = req.getParamValue("mouths");
				String invoiceCode  = req.getParamValue("dealerC");
				//String changeNo = "BG"+logonUser.getDealerCode()+new Date();
				TtAsWrInvoiceChangePO po = new TtAsWrInvoiceChangePO();
				TtAsWrInvoiceChangePO po1 = new TtAsWrInvoiceChangePO();
				po1.setId( Long.valueOf(id));
				po.setBalanceNo(balanceNo);
			  //  po.setChangeNo(changeNo);
			    po.setDelaerid(Long.valueOf(dealerId));
			    po.setDealercode(dealerCode);
			    po.setDealername(dealerName);
			    po.setInvoiceMark(invoiceMark);
			    po.setNewInvoiceId(Long.valueOf(dealerIdNew));
			    po.setNewInvoiceMark(dealerCodeNew);
			    po.setCreateDate(new Date());
			    po.setCreateBy(logonUser.getUserId().toString());
			    po.setRemark(remark);
			    po.setYieldly(Integer.valueOf(yilid));
			    po.setStatus(Constant.CHANGE_STATUS_1);
			    po.setInvoiceDate(mouths);
			    po.setNewInvoiceCode(invoiceCode);
			    factory.update(po1,po);
			    act.setOutData("success","true");
				act.setForword("/jsp/claim/application/modifyDealerKpUpdate.jsp");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 act.setOutData("success","false");
			}
	 }
	 
	 public void submitKpDealer(){
		 ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
		 try {
			 	RequestWrapper req = act.getRequest();
				String id    = req.getParamValue("ID");
				TtAsWrInvoiceChangePO po = new TtAsWrInvoiceChangePO();
				TtAsWrInvoiceChangePO po1 = new TtAsWrInvoiceChangePO();
				po.setId(Long.valueOf(id));
				po1.setStatus(Constant.CHANGE_STATUS_2);
				po1.setSubmitDate(new Date());
				po1.setSubmitPerson(logonUser.getUserId().toString());
				factory.update(po,po1);
				act.setOutData("success","true");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			act.setOutData("success","false");
		}
	 }
	 
	 
	 public void oemAuditingDelaerKP(){
		 ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER); 
		 
			try {
				RequestWrapper req = act.getRequest();
				String id    = req.getParamValue("ID");
				DealerNewKpUpdateDAO dao = new DealerNewKpUpdateDAO();
				ClaimBillMaintainDAO cDao = new ClaimBillMaintainDAO();
				List<Map<String, Object>> list = dao.modifyInvoiceMarkChange(id);
				List<FsFileuploadPO> attachLs = cDao.queryAttById(id);// 取得附件
				act.setOutData("list", list.get(0));
				act.setOutData("attachLs", attachLs);
				act.setForword("/jsp/claim/application/oemDealerKpAuditing.jsp");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	 }
	 
	 public void auditingDealerNewKP(){
		 ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER); 
			 try {
				 	RequestWrapper req = act.getRequest();
					String id    = req.getParamValue("ID");
					String flag  = req.getParamValue("flag"); 
					String balanceNo = req.getParamValue("BALANCE_NO");
					String dealerCodeNew = req.getParamValue("dealerCodeNew");
					String dealerIdNew = req.getParamValue("dealerIdNew");
					String auditRemark = req.getParamValue("auditingRemark");
					TtAsWrInvoiceChangePO po = new TtAsWrInvoiceChangePO();
					TtAsWrInvoiceChangePO po1 = new TtAsWrInvoiceChangePO();
					po.setId(Long.valueOf(id));
					if(flag.equals("1")) po1.setStatus(Constant.CHANGE_STATUS_3);
					if(flag.equals("2")) po1.setStatus(Constant.CHANGE_STATUS_4);
					po1.setSubmitDate(new Date());
					po1.setSubmitPerson(logonUser.getUserId().toString());
					po1.setAuditingRemark(auditRemark);
					po1.setAuditingDate(new Date());
					po1.setAuditingPerson(logonUser.getUserId().toString());
					if(flag.equals("1")||flag.equals("2")){
					factory.update(po,po1);
			 		}	
					TtAsWrClaimBalancePO bpo = new TtAsWrClaimBalancePO();
					TtAsWrClaimBalancePO bpo1 = new TtAsWrClaimBalancePO();
					bpo.setBalanceNo(balanceNo);
					bpo1.setInvoiceMaker(dealerCodeNew);
					bpo1.setKpDealerId(Long.valueOf(dealerIdNew));
					act.setOutData("success","true");
					if(balanceNo!=null&&!balanceNo.equals("")&&flag.equals("1")){
						factory.update(bpo, bpo1);
					}
					OEMmainUrl();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				act.setOutData("success","false");
			}
	 }
	 

	
}
