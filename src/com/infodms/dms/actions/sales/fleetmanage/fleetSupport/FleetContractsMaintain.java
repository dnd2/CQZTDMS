package com.infodms.dms.actions.sales.fleetmanage.fleetSupport;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtFleetContractPO;
import com.infodms.dms.po.TtFleetIntentNewPO;
import com.infodms.dms.po.TtFleetIntentPO;
import com.infodms.dms.po.TtIfStandardAuditPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户合同维护Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-25
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class FleetContractsMaintain {
	
	public Logger logger = Logger.getLogger(FleetContractsMaintain.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetContractsMaintain.jsp";
	private final String initDealerUrl = "/jsp/sales/fleetmanage/fleetSupport/dealerContractsMaintain.jsp";
	private final String detailUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetContractsMaintainDetail.jsp";
	private final String detaiDealerlUrl = "/jsp/sales/fleetmanage/fleetSupport/dealerContractsMaintainDetail.jsp";
	private final String detailUpdateUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetContractUpdate.jsp";
	private final String firstdetailUpdateUrl = "/jsp/sales/fleetmanage/fleetSupport/firstContractUpdate.jsp";
	private final String dlrCompayUrl = "/jsp/sales/fleetmanage/fleetSupport/queryDealerCompany.jsp";
	private final String addVModel = "/jsp/sales/fleetmanage/fleetSupport/addvehiclemodel.jsp";
	private final String firstUrl = "/jsp/sales/fleetmanage/fleetSupport/firstcontractmain.jsp";

	/**
	 * 集团客户合同维护页面初始化
	 */
	public void fleetContractsMaintainInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同维护页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商公司查询初始化
	 */
	public void queryCompany() throws Exception{
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(dlrCompayUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商公司查询
	 */
	public void queryCom(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String companyCode = request.getParamValue("companyCode");
			String companyName = request.getParamValue("companyName");
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<CompanyBean> ps = dao.selectCompany(companyCode, companyName, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户合同维护查询
	 */
	public void fleetContractsMaintainQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String checkSDate = request.getParamValue("checkSDate");//签订开始时间
			String checkEDate = request.getParamValue("checkEDate");//签订结束时间
			String companyId = request.getParamValue("companyId");	//经销商公司ID
			String conStatus = request.getParamValue("conStatus");	//合同状态
			String oemCompanyId = String.valueOf(GetOemcompanyId.getOemCompanyId(logonUser));
			String dutyType = logonUser.getDutyType();
			String orgId = String.valueOf(logonUser.getOrgId());
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetContractsMaintainQuery(conStatus, oemCompanyId, dutyType, orgId, checkSDate,checkEDate, fleetName, fleetType, startDate, endDate, companyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同修改查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户合同初审
	 */
	public void firstContractsMaintainQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String checkSDate = request.getParamValue("checkSDate");//签订开始时间
			String checkEDate = request.getParamValue("checkEDate");//签订结束时间
			String companyId = request.getParamValue("companyId");	//经销商公司ID
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.firstContractsMaintainQuery(checkSDate,checkEDate, fleetName, fleetType, startDate, endDate, companyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同修改查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dealerContractsMaintainQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String checkSDate = request.getParamValue("checkSDate");//签订开始时间
			String checkEDate = request.getParamValue("checkEDate");//签订结束时间
			String companyId = String.valueOf(logonUser.getCompanyId());	//经销商公司ID
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.dealerContractsMaintainQuery(checkSDate, checkEDate, fleetName, fleetType, startDate, endDate, companyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同维护查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户合同维护明细查询
	 */
	public void fleetContractsMaintainDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户Id
			String intentId = request.getParamValue("intentId");//意向Id
			String contractId=request.getParamValue("contractId");//合同Id
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
			Map<String, Object> cmap = dao.getContractInfobyId(fleetId, intentId,contractId);
			List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,contractId);
			List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
			act.setOutData("intentList", list1);
			act.setOutData("checkList", list2);
			act.setOutData("intentMap", map2);
			act.setOutData("cmap", cmap);
			act.setOutData("intentId", intentId);
			act.setOutData("fleetMap", map);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同维护明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dealerContractsMaintainDetailQuery(){
		StringBuffer myContractCode=new StringBuffer("");
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			 Long dearCommpanyId=logonUser.getCompanyId();//经销商公司ID
			String fleetId = request.getParamValue("fleetId");	//客户Id
			//String intentId = request.getParamValue("intentId");	//意向Id
			TmDealerPO myTmDealer=new TmDealerPO();
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			//Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
			//Map<String, Object> cmap = dao.getContractInfobyId(fleetId, intentId);
			//List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId);
			//List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
			if(logonUser.getDealerId()!=null&&logonUser.getDealerId()!=""){
				TmDealerPO tmDealer=new TmDealerPO();
				tmDealer.setDealerId(Long.parseLong(logonUser.getDealerId().split(",")[0]));
				myTmDealer=(TmDealerPO) dao.select(tmDealer).get(0);
				}
				Calendar now = Calendar.getInstance();
				myContractCode.append(myTmDealer.getErpCode());
				myContractCode.append(now.get(Calendar.YEAR));
				if((now.get(Calendar.MONTH) + 1)>=10){
				myContractCode.append(now.get(Calendar.MONTH) + 1);
				}else{
					myContractCode.append(0);
					myContractCode.append(now.get(Calendar.MONTH) + 1);
				}
			
			int num=dao.getContractCode(myContractCode, dearCommpanyId);
			num+=1;
			for(int j=1;j<4;j++){
				if(num/(j*10)==0){
					myContractCode.append(0);
				}
			}
			myContractCode.append(num);
			act.setOutData("fleetContract",myContractCode); //合同编号
			act.setOutData("fleetMap", map);
			act.setForword(detaiDealerlUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同维护明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户合同维护修改初始化
	 */
	public void fleetContractsUpdate(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户Id
			String intentId = request.getParamValue("intentId");	//意向Id
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			String contractId=request.getParamValue("contractId");//合同Id
			String sys = "" ;
			//Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
			//List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
			
			Map<String, Object> cmap = dao.getContractInfobyId(fleetId, intentId,contractId);
			List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,contractId);
			String id = String.valueOf(cmap.get("CONTRACT_ID"));
			List<Map<String, Object>> fileList = dao.queryAttachFileInfo(id);//查询附件
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				sys = Constant.COMPANY_CODE_JC ;
			} else if(Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				sys = Constant.COMPANY_CODE_CVS ;
			}
			
			act.setOutData("sys", sys) ;
			act.setOutData("intentList", list1);
			act.setOutData("cmap", cmap);
			act.setOutData("intentId", intentId);
			act.setOutData("fleetMap", map);
			act.setOutData("fileList", fileList);
			act.setForword(detailUpdateUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同维护修改初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 初审详细
	 */
	public void firstContractsUpdate(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户Id
			String intentId = request.getParamValue("intentId");	//意向Id
			String contractId=request.getParamValue("contractId");//合同Id
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			Map<String, Object> cmap = dao.getContractInfobyId(fleetId, intentId,contractId);
			List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,contractId);
			String id = String.valueOf(cmap.get("CONTRACT_ID"));
			List<Map<String, Object>> fileList = dao.queryAttachFileInfo(id);//查询附件
			act.setOutData("intentList", list1);
			act.setOutData("cmap", cmap);
			act.setOutData("intentId", intentId);
			act.setOutData("fleetMap", map);
			act.setOutData("fileList", fileList);
			act.setForword(firstdetailUpdateUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户初审");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户合同维护提交
	 */
	public void fleetContractsMaintainConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");				//客户Id
			String intentId = request.getParamValue("intentId");			//意向Id
			String cou = dao.getCount(fleetId,intentId);					//查看车厂是否维护合同
			if(cou.equals("0")){
				String dlrCompanyId = request.getParamValue("dlrCompanyId");	//经销商公司ID
				String contractNo = request.getParamValue("contractNo");		//合同编号
				String contractAmount = request.getParamValue("contractAmount");//合同数量
				String discount = request.getParamValue("discount");			//支持点位
				String checkDate = CommonUtils.checkNull(request.getParamValue("checkDate"));//合同签订日期
				String startDate = request.getParamValue("startDate");			//有效期起
				String endDate = request.getParamValue("endDate");				//有效期止
				Date sysDate = new Date(System.currentTimeMillis());			//系统时间
				SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
				TtFleetContractPO tfcp = new TtFleetContractPO();
				tfcp.setContractId(Long.parseLong(SequenceManager.getSequence("")));
				tfcp.setContractNo(contractNo.trim());
				tfcp.setContractAmount(Integer.parseInt(contractAmount.trim()));
				tfcp.setDiscount(discount.trim());
				tfcp.setDlrCompanyId(Long.parseLong(dlrCompanyId));
				tfcp.setFleetId(Long.parseLong(fleetId));
				tfcp.setIntentId(Long.parseLong(intentId));
				tfcp.setCheckDate(fmat.parse(checkDate));
				tfcp.setStartDate(fmat.parse(startDate));
				tfcp.setEndDate(fmat.parse(endDate));
				tfcp.setOemCompanyId(logonUser.getCompanyId());
				tfcp.setCreateDate(sysDate);
				tfcp.setCreateBy(logonUser.getUserId());
				dao.insert(tfcp);
				
				TtFleetIntentPO tfip = new TtFleetIntentPO();
				TtFleetIntentPO tfip1 = new TtFleetIntentPO();
				tfip.setIntentId(Long.parseLong(intentId));
				tfip1.setStatus(Constant.FLEET_SUPPORT_STATUS_06);
				tfip1.setUpdateDate(sysDate);
				tfip1.setUpdateBy(logonUser.getUserId());
				dao.update(tfip, tfip1);
			act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"集团客户合同维护提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dealerContractsMaintainConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");				//客户Id
			String intentId = request.getParamValue("intentId");			//意向Id
			String cou = dao.getCount(fleetId,intentId);					//查看车厂是否维护合同
			if(cou.equals("0")){
				String dlrCompanyId = request.getParamValue("dlrCompanyId");	//经销商公司ID
				String contractNo = request.getParamValue("fleetContract");		//合同编号
				String buyP = request.getParamValue("buyP");				    //买方
				String sellP = request.getParamValue("sellP");					//卖方
				
				String checkDate = CommonUtils.checkNull(request.getParamValue("checkTime"));//合同签订日期
				String startDate = request.getParamValue("beginTime");			//有效期起
				String endDate = request.getParamValue("endTime");				//有效期止
				String otherRequirement = CommonUtils.checkNull(request.getParamValue("otherRequirement")); //特殊需求
				String[] groupIds = request.getParamValues("groupId");	//需求车系
				String[] remarks = request.getParamValues("remark");	//备注
				String[] as = request.getParamValues("a");				//数量
				//String[] bs = request.getParamValues("b");				//单价
				String[] ds = request.getParamValues("d");				//支持点位
				
				Date sysDate = new Date(System.currentTimeMillis());	//系统时间
				SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
				TtFleetContractPO tfcp = new TtFleetContractPO();
				tfcp.setContractId(Long.parseLong(SequenceManager.getSequence("")));
				tfcp.setContractNo(contractNo.trim());
				tfcp.setDlrCompanyId(Long.parseLong(dlrCompanyId));
				tfcp.setFleetId(Long.parseLong(fleetId));
				tfcp.setCheckDate(fmat.parse(checkDate));
				tfcp.setStartDate(fmat.parse(startDate));
				tfcp.setEndDate(fmat.parse(endDate));
				tfcp.setOemCompanyId(Long.parseLong(logonUser.getOemCompanyId()));
				tfcp.setCreateDate(sysDate);
				tfcp.setCreateBy(logonUser.getUserId());
				tfcp.setBuyFrom(buyP);
				tfcp.setSellTo(sellP);
				tfcp.setStatus(Integer.parseInt(Constant.FLEET_CON_STATUS_01));
				if(!"".equals(otherRequirement)){
					tfcp.setOtherRemark(otherRequirement);
				}
				dao.insert(tfcp);//合同添加
				
				if(groupIds!=null || !groupIds.equals("")){
					for(int i=0;i<groupIds.length;i++){
						TtFleetIntentNewPO po = new TtFleetIntentNewPO();
						po.setIntentId(Long.parseLong(SequenceManager.getSequence("")));
						po.setFleetId(Long.parseLong(fleetId));
						po.setSeriesId(Long.parseLong(groupIds[i]));//车系
						po.setIntentCount(Long.parseLong(as[i]));//数量
						//po.setIntentAmount(Double.parseDouble(bs[i]));//单价
						po.setContractId(tfcp.getContractId());
						//Double d = po.getIntentAmount()*Integer.parseInt(as[i]);
						//po.setCountAmount(d);//合计
						if(ds!=null&&ds.length>0){
							if(ds[i]!=null){
								po.setIntentPoint(Double.parseDouble(ds[i]));//支持点位
							}
						}
						if(remarks!=null&&remarks.length>0){
							if(remarks[i]!=null){
								po.setRemark(remarks[i]);
							}
						}
						dao.insert(po);//添加意向
					}
				}
				
				TmFleetPO tp = new TmFleetPO();
				TmFleetPO tf = new TmFleetPO();
				tp.setFleetId(Long.parseLong(fleetId));
				tf.setConStatus(1);
				dao.update(tp, tf);
				
				//附件
				String ywzj = String.valueOf(tfcp.getContractId());
				String[] fjids = request.getParamValues("fjid");
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	
				act.setOutData("returnValue", 1);
			}else{
//				TtFleetContractPO tfcp1 = new TtFleetContractPO();
//				tfcp1.setFleetId(Long.parseLong(fleetId));
				String dlrCompanyId = request.getParamValue("dlrCompanyId");	//经销商公司ID
				String contractNo = request.getParamValue("fleetContract");		//合同编号
				String buyP = request.getParamValue("buyP");				    //买方
				String sellP = request.getParamValue("sellP");					//卖方
				
				String checkDate = CommonUtils.checkNull(request.getParamValue("checkTime"));//合同签订日期
				String startDate = request.getParamValue("beginTime");			//有效期起
				String endDate = request.getParamValue("endTime");				//有效期止
				String otherRequirement = CommonUtils.checkNull(request.getParamValue("otherRequirement")); //特殊需求
				String[] groupIds = request.getParamValues("groupId");	//需求车系
				String[] remarks = request.getParamValues("remark");	//备注
				String[] as = request.getParamValues("a");				//数量
				//String[] bs = request.getParamValues("b");				//单价
				String[] ds = request.getParamValues("d");				//支持点位
				
				Date sysDate = new Date(System.currentTimeMillis());	//系统时间
				SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
				TtFleetContractPO tfcp = new TtFleetContractPO();
				tfcp.setContractId(Long.parseLong(SequenceManager.getSequence("")));
				tfcp.setContractNo(contractNo.trim());
				tfcp.setDlrCompanyId(Long.parseLong(dlrCompanyId));
				tfcp.setFleetId(Long.parseLong(fleetId));
				tfcp.setCheckDate(fmat.parse(checkDate));
				tfcp.setStartDate(fmat.parse(startDate));
				tfcp.setEndDate(fmat.parse(endDate));
				tfcp.setOemCompanyId(Long.parseLong(logonUser.getOemCompanyId()));
				tfcp.setCreateDate(sysDate);
				tfcp.setCreateBy(logonUser.getUserId());
				tfcp.setBuyFrom(buyP);
				tfcp.setSellTo(sellP);
				tfcp.setStatus(Integer.parseInt(Constant.FLEET_CON_STATUS_01));
				if(!"".equals(otherRequirement)){
					tfcp.setOtherRemark(otherRequirement);
				}
				dao.insert(tfcp);
				//dao.update(tfcp1,tfcp);
				
				if(groupIds!=null || !groupIds.equals("")){
//					TtFleetIntentNewPO po1 = new TtFleetIntentNewPO();
//					po1.setFleetId(Long.parseLong(fleetId));
//					dao.delete(po1);
					for(int i=0;i<groupIds.length;i++){
						TtFleetIntentNewPO po = new TtFleetIntentNewPO();
						po.setIntentId(Long.parseLong(SequenceManager.getSequence("")));
						po.setFleetId(Long.parseLong(fleetId));
						po.setSeriesId(Long.parseLong(groupIds[i]));//车系
						po.setIntentCount(Long.parseLong(as[i]));//数量
						//po.setIntentAmount(Double.parseDouble(bs[i]));//单价
						po.setContractId(tfcp.getContractId());
						//Double d = po.getIntentAmount()*Integer.parseInt(as[i]);
						//po.setCountAmount(d);//合计
						if(ds!=null&&ds.length>0){
							if(ds[i]!=null){
								po.setIntentPoint(Double.parseDouble(ds[i]));//支持点位
							}
						}
						if(remarks!=null&&remarks.length>0){
							if(remarks[i]!=null){
								po.setRemark(remarks[i]);
							}
						}
						dao.insert(po);//添加意向
					}
					String ywzj = String.valueOf(tfcp.getContractId());
					String[] fjids = request.getParamValues("fjid");
					FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	
				act.setOutData("returnValue", 2);
			}
			
			}
			}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"集团客户合同维护提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 集团客户合同驳回
	 */
	public void fleetContractsBackConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtFleetContractPO tfcp = new TtFleetContractPO();
		TtFleetContractPO te = new TtFleetContractPO();
		Date sysDate = new Date(System.currentTimeMillis());			//系统时间
		String fleetId = request.getParamValue("fleetId");				//客户Id
		String contractId = request.getParamValue("contractId");		//合同Id
		String backReson = request.getParamValue("checkReson");			//审批意见
		te.setContractId(Long.parseLong(contractId));
		tfcp.setAuditDate(sysDate);
		tfcp.setContractId(Long.parseLong(contractId));
		tfcp.setFleetId(Long.parseLong(fleetId));
		tfcp.setStatus(Integer.parseInt(Constant.FLEET_CON_STATUS_04));
		tfcp.setBackReson(backReson);
		tfcp.setUpdateDate(sysDate);
		tfcp.setUpdateBy(logonUser.getUserId());
		tfcp.setCheckUser(logonUser.getUserId());
		dao.update(te, tfcp);											//合同修改
		act.setOutData("returnValue", 1);
	}
	
	/**
	 * 集团客户合同维护修改
	 */
	public void fleetContractsUpdateConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");				//客户Id
			String contractId = request.getParamValue("contractId");		//合同Id
			String buyP = request.getParamValue("buyP");				    //买方
			String sellP = request.getParamValue("sellP");					//卖方
			String checkDate = CommonUtils.checkNull(request.getParamValue("checkTime"));//合同签订日期
			String startDate = request.getParamValue("beginTime");			//有效期起
			String endDate = request.getParamValue("endTime");				//有效期止
			String otherRequirement = CommonUtils.checkNull(request.getParamValue("otherRequirement")); //特殊需求
			String otherAmount = request.getParamValue("otherAmount")==null?"0":request.getParamValue("otherAmount");//特殊金额
			String[] groupIds = request.getParamValues("groupId");	//需求车系
			String[] remarks = request.getParamValues("remark");	//备注
			String[] as = request.getParamValues("a");				//数量
			//String[] bs = request.getParamValues("b");			//单价
			String[] cs = request.getParamValues("c");				//标准价
			String[] ds = request.getParamValues("d");				//支持点位
			String disc = request.getParamValue("disc");
			String backReson =request.getParamValue("checkReson");   //退出 原因 
			TtFleetIntentNewPO tn = new TtFleetIntentNewPO();
			tn.setContractId(Long.parseLong(contractId));
			dao.delete(tn);//清空
			
			if(groupIds!=null && !"".equals(groupIds)){
				for(int i=0;i<groupIds.length;i++){
					TtFleetIntentNewPO po = new TtFleetIntentNewPO();
					po.setIntentId(Long.parseLong(SequenceManager.getSequence("")));
					po.setFleetId(Long.parseLong(fleetId));
					po.setSeriesId(Long.parseLong(groupIds[i]));//车系
					po.setIntentCount(Long.parseLong(as[i]));//数量
					//po.setIntentAmount(Double.parseDouble(bs[i]));//单价
					po.setNormAmount(Double.parseDouble(cs[i]));//基本价
					po.setContractId(Long.parseLong(contractId));
					Double d = po.getNormAmount()*Integer.parseInt(as[i]);
					po.setCountAmount(d);//合计
					po.setIntentPoint(Double.parseDouble(ds[i]));//支持点位
					
					if(remarks!=null&&remarks.length>0){
						if(remarks[i]!=null){
							po.setRemark(remarks[i]);
						}
					}
					
					dao.insert(po);//添加意向
				}
			}
			
			Date sysDate = new Date(System.currentTimeMillis());			//系统时间
			SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
			TtFleetContractPO tfcp = new TtFleetContractPO();
			TtFleetContractPO te = new TtFleetContractPO();
			te.setContractId(Long.parseLong(contractId));
			tfcp.setBuyFrom(buyP);
			tfcp.setSellTo(sellP);
			tfcp.setAuditDate(sysDate);
			tfcp.setCheckDate(fmat.parse(checkDate));
			tfcp.setOtherAmount(Double.parseDouble(otherAmount));
			tfcp.setStartDate(fmat.parse(startDate));
			tfcp.setEndDate(fmat.parse(endDate));
			tfcp.setDisAmount(Double.parseDouble(disc));
			tfcp.setBackReson(backReson);
			if(!"".equals(otherRequirement)){
				tfcp.setOtherRemark(otherRequirement);
			}
			tfcp.setStatus(Integer.parseInt(Constant.FLEET_CON_STATUS_03));
			
			tfcp.setUpdateDate(sysDate);
			tfcp.setUpdateBy(logonUser.getUserId());
			tfcp.setCheckUser(logonUser.getUserId()) ;
			
			dao.update(te, tfcp);//合同修改
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"集团客户合同维护提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户合同初审
	 */
	public void firstContractsUpdateConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");				//客户Id
			String contractId = request.getParamValue("contractId");		//合同Id
			String buyP = request.getParamValue("buyP");				    //买方
			String sellP = request.getParamValue("sellP");					//卖方
			String checkDate = CommonUtils.checkNull(request.getParamValue("checkTime"));//合同签订日期
			String startDate = request.getParamValue("beginTime");			//有效期起
			String endDate = request.getParamValue("endTime");				//有效期止
			String otherRequirement = CommonUtils.checkNull(request.getParamValue("otherRequirement")); //特殊需求
			String otherAmount = request.getParamValue("otherAmount")==null?"0":request.getParamValue("otherAmount");//特殊金额
			String[] groupIds = request.getParamValues("groupId");	//需求车系
			String[] remarks = request.getParamValues("remark");	//备注
			String[] as = request.getParamValues("a");				//数量
			//String[] bs = request.getParamValues("b");			//单价
			String[] cs = request.getParamValues("c");				//标准价
			String[] ds = request.getParamValues("d");				//支持点位
			String disc = request.getParamValue("disc");			//折让总额
			
			TtFleetIntentNewPO tn = new TtFleetIntentNewPO();
			tn.setContractId(Long.parseLong(contractId));
			dao.delete(tn);//清空 
			
			if(groupIds!=null || !groupIds.equals("")){
				for(int i=0;i<groupIds.length;i++){
					TtFleetIntentNewPO po = new TtFleetIntentNewPO();
					po.setIntentId(Long.parseLong(SequenceManager.getSequence("")));
					po.setFleetId(Long.parseLong(fleetId));
					po.setSeriesId(Long.parseLong(groupIds[i]));//车系
					po.setIntentCount(Long.parseLong(as[i]));//数量
					//po.setIntentAmount(Double.parseDouble(bs[i]));//单价
					po.setNormAmount(Double.parseDouble(cs[i]));//基本价
					po.setContractId(Long.parseLong(contractId));
					Double d = po.getNormAmount()*Integer.parseInt(as[i]);
					po.setCountAmount(d);//合计
					po.setIntentPoint(Double.parseDouble(ds[i]));//支持点位
					
					if(remarks!=null&&remarks.length>0){
						if(remarks[i]!=null){
							po.setRemark(remarks[i]);
						}
					}
					dao.insert(po);//添加意向
				}
			}
			
			Date sysDate = new Date(System.currentTimeMillis());			//系统时间
			SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
			TtFleetContractPO tfcp = new TtFleetContractPO();
			TtFleetContractPO te = new TtFleetContractPO();
			te.setContractId(Long.parseLong(contractId));
			tfcp.setBuyFrom(buyP);
			tfcp.setSellTo(sellP);
			tfcp.setCheckDate(fmat.parse(checkDate));
			tfcp.setOtherAmount(Double.parseDouble(otherAmount));
			tfcp.setStartDate(fmat.parse(startDate));
			tfcp.setEndDate(fmat.parse(endDate));
			tfcp.setDisAmount(Double.parseDouble(disc));
			
			if(!"".equals(otherRequirement)){
				tfcp.setOtherRemark(otherRequirement);
			}
			tfcp.setStatus(Integer.parseInt(Constant.FLEET_CON_STATUS_02));
			
			tfcp.setUpdateDate(sysDate);
			tfcp.setUpdateBy(logonUser.getUserId());
			
			dao.update(te, tfcp);//合同修改
			
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"集团客户合同维护提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 经销商端集团客户合同维护
	 */
	public void dealerContractsMaintainInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initDealerUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同维护页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 新增车型
	 */
	public void addVehicleModel(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addVModel);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增车型");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void addVModel(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//查询车系  add by lishuai
			String poseId = String.valueOf(logonUser.getPoseId());
			String level = Constant.GROUP_LEVEL_02;
			String companyId = String.valueOf(GetOemcompanyId.getOemCompanyId(logonUser));
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.getGroupDropDownBox(poseId, level, companyId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增车型");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 合同初审
	 */
	public void fleetContractsMaintainFirstInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(firstUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户合同初审");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
