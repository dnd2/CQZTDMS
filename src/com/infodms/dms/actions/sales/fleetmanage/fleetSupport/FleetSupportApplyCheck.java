
package com.infodms.dms.actions.sales.fleetmanage.fleetSupport;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetInfoAppDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.dao.sales.marketmanage.planissued.ActivitiesPlanIssuedDao;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtFleetIntentAuditPO;
import com.infodms.dms.po.TtFleetIntentPO;
import com.infodms.dms.po.TtFleetSupportInfoPO;
import com.infodms.dms.po.TtFleetSupportPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 集团客户支持审核Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-23
 * @author 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class FleetSupportApplyCheck {
	
	public Logger logger = Logger.getLogger(FleetSupportApplyCheck.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportApplyCheck.jsp";
	private final String detailUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportApplyCheckDetail.jsp";
	private final String dlrCompayUrl = "/jsp/sales/fleetmanage/fleetSupport/queryDealerCompany.jsp";
	/**
	 * 集团客户支持审核页面初始化
	 */
	public void fleetSupportApplyCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持审核页面初始化");
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
	 * 集团客户支持审核查询
	 */
	public void fleetSupportApplyCheckQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			String companyId = request.getParamValue("companyId");	//经销商公司ID
			String dutyType = logonUser.getDutyType();
			String orgId = String.valueOf(logonUser.getOrgId());
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetSupportApplyCheckQuery(orgId, dutyType, fleetName, fleetType, startDate, endDate, companyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持审核明细查询
	 */
	public void fleetSupportApplyCheckDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
			//List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,"");
			//act.setOutData("intentList", list1);
			//act.setOutData("intentMap", map2);
			//act.setOutData("intentId", intentId);
			String fleetId = request.getParamValue("fleetId");	//客户Id
			String intentId = null;	//意向Id
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
			act.setOutData("checkList", list2);
			// 根据fleetId查询上传附件
			ActivitiesPlanIssuedDao dao = ActivitiesPlanIssuedDao.getInstance();
			List<Map<String, Object>> attachList = dao.getAttachInfos(fleetId);
			if (null != attachList && attachList.size() != 0) {
				act.setOutData("attachList", attachList);
			}
			//根据集团客户主表的id查询子表需求说明中的内容
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			act.setOutData("tfrdMapList", tfrdMapList);
			//根据集团客户主表的id查询子表商务支持中的内容
			List<Map<String,Object>> supportInfoList=appDao.getSupportInfoByFleetId(fleetId);
			act.setOutData("supportInfoList", supportInfoList);
			act.setOutData("fleetMap", map);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持审核明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持审核提交
	 */
	public void fleetSupportApplyCheckConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//String supportRemark= CommonUtils.checkNull(request.getParamValue("supportRemark"));//商务支持信息备注
			String groupIds = CommonUtils.checkNull(request.getParamValue("groupIds"));				//物料组IDS
			//String remarks = CommonUtils.checkNull(request.getParamValue("remarks"));				//备注
			String amounts = CommonUtils.checkNull(request.getParamValue("amounts"));				//数量
			String prices=CommonUtils.checkNull(request.getParamValue("prices"));
			String depotproprices=CommonUtils.checkNull(request.getParamValue("depotproprices"));
			String profits=CommonUtils.checkNull(request.getParamValue("profits"));
			String gandaccepts=CommonUtils.checkNull(request.getParamValue("gandaccepts"));
			String realprices=CommonUtils.checkNull(request.getParamValue("realprices"));
			String marketdevelops=CommonUtils.checkNull(request.getParamValue("marketdevelops"));
			String realprofits=CommonUtils.checkNull(request.getParamValue("realprofits"));
			String requestsupports=CommonUtils.checkNull(request.getParamValue("requestsupports"));
			String auditmoneys=CommonUtils.checkNull(request.getParamValue("auditmoneys"));
			String fleetId = request.getParamValue("fleetId");		//客户Id
			//String intentId = request.getParamValue("intentId");	//意向Id
			String flag = request.getParamValue("flag");			//页面参数
			//String isCheck = request.getParamValue("chktag");	    //是否财务会签
			String remark = request.getParamValue("remark");		//审核意见
//			TtFleetIntentPO tfipContion = new TtFleetIntentPO();
//			TtFleetIntentPO tfipValue = new TtFleetIntentPO();
			//tfipContion.setIntentId(Long.parseLong(intentId));
			String dutyType = logonUser.getDutyType();
//			if("1".equals(flag)){//驳回
//				tfipValue.setStatus(Constant.FLEET_SUPPORT_STATUS_05);
//			}else{
//				if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){//大区通过
//					tfipValue.setStatus(Constant.FLEET_SUPPORT_STATUS_08);
//				}else{//审核通过
//					tfipValue.setStatus(Constant.FLEET_SUPPORT_STATUS_03);
//				}
//			}
//			tfipValue.setUpdateDate(new Date(System.currentTimeMillis()));
//			tfipValue.setUpdateBy(logonUser.getUserId());
//			dao.update(tfipContion, tfipValue);
//			TtFleetIntentAuditPO tficp = new TtFleetIntentAuditPO();
//			tficp.setAuditId(Long.parseLong(SequenceManager.getSequence("")));
//			tficp.setAuditRemark(remark);
//			if("1".equals(flag)){//支持审核驳回
//				tficp.setAuditResult(Constant.FLEET_SUPPORT_CHECK_STATUS_02);
//			}else{
//				if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){//支持审核大区通过
//					tficp.setAuditResult(Constant.FLEET_SUPPORT_CHECK_STATUS_03);
//				}else{//支持审核通过
//					tficp.setAuditResult(Constant.FLEET_SUPPORT_CHECK_STATUS_01);
//				}
//			}
//			tficp.setAuditDate(new Date(System.currentTimeMillis()));
//			tficp.setOrgId(logonUser.getOrgId());
//			tficp.setIntentId(Long.parseLong(intentId));
//			tficp.setFleetId(Long.parseLong(fleetId));
//			tficp.setAuditUserId(logonUser.getUserId());
//			tficp.setCreateDate(new Date(System.currentTimeMillis()));
//			tficp.setCreateBy(logonUser.getUserId());
//			dao.insert(tficp);
			TtFleetIntentAuditPO tfia=new TtFleetIntentAuditPO();
			tfia.setAuditId(Long.parseLong(SequenceManager.getSequence("")));
			tfia.setAuditUserId(logonUser.getUserId());
			tfia.setCreateDate(new java.util.Date());
			tfia.setFleetId(new Long(fleetId));
			tfia.setCreateBy(logonUser.getUserId());
			tfia.setOrgId(new Long(logonUser.getOrgId()));
			//查询集团客户信息
			TmFleetPO tfponew1=new TmFleetPO();
			tfponew1.setFleetId(new Long(fleetId));
			TmFleetPO tfponew=new TmFleetPO();
			tfponew.setFleetId(new Long(fleetId));
			tfponew=(TmFleetPO) dao.select(tfponew).get(0);
			//获取原来的大客户代码
			String fleetCode=tfponew.getFleetCode();
			TtFleetSupportPO tfpo=new TtFleetSupportPO() ;
			TtFleetSupportPO tfpo1=new TtFleetSupportPO();
			tfpo.setFleetId(new Long(fleetId));
			
			String [] groupId = groupIds.split(",");
			String [] amount = amounts.split(",");
			String []price=prices.split(",");
			
			String []depotproprice=depotproprices.split(",");
			
			String []profit=profits.split(",");
			
			String []gandaccept=gandaccepts.split(",");
			String []realprice=realprices.split(",");
			String []marketdevelop=marketdevelops.split(",");
			String []realprofit=realprofits.split(",");
			String []requestsupport=requestsupports.split(",");
			String []auditmoney=auditmoneys.split(",");
			
			
			//如果审核通过
			if(!"1".equals(flag)){
				//小区审核通过改为待大区审核
				if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
					tfia.setAuditResult(Integer.parseInt(Constant.SUPPORT_INFO_STATUS_02.toString()));
					tfpo1.setSupportStatus(Constant.SUPPORT_INFO_STATUS_02);
				}
				//大区审核通过改为待车厂审核
				if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
					tfia.setAuditResult(Integer.parseInt(Constant.SUPPORT_INFO_STATUS_03.toString()));
					tfpo1.setSupportStatus(Constant.SUPPORT_INFO_STATUS_03);
					
				}
				//审核通过
				if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)){
					tfia.setAuditResult(Integer.parseInt(Constant.SUPPORT_INFO_STATUS_04.toString()));
					tfpo1.setSupportStatus(Constant.SUPPORT_INFO_STATUS_04);
				//	tfponew.setFleetCode("S"+fleetCode.substring(1));
					dao.update(tfponew1,tfponew);
					
				}
				//删除数据
				UserManageDao appDao=new UserManageDao();
				TtFleetSupportInfoPO supportInfo0=new TtFleetSupportInfoPO();
				supportInfo0.setFleetId(new Long(fleetId));
				appDao.delete(supportInfo0);
				for(int i = 0; i< groupId.length; i++){
					if(!"".equals(groupId[i])&&groupId[i]!=null){
						TtFleetSupportInfoPO supportInfo=new TtFleetSupportInfoPO();
						supportInfo.setSupportInfoId(Long.parseLong(SequenceManager.getSequence("")));
						supportInfo.setFleetId(Long.parseLong(fleetId));
						supportInfo.setPrice(Double.parseDouble(price[i]));
						supportInfo.setAmount(Long.parseLong(amount[i]));
						supportInfo.setIntentSeries(Long.parseLong(groupId[i]));
						supportInfo.setDepotProPrice(Double.parseDouble(depotproprice[i]));
						supportInfo.setGiveAndAccept(Double.parseDouble(gandaccept[i]));
						
						//supportInfo.setProfit(Double.parseDouble(profit[i]));
						supportInfo.setMarketDevelop(Double.parseDouble(marketdevelop[i]));
						supportInfo.setRealPrice(Double.parseDouble(realprice[i]));
						supportInfo.setRealProfit(Double.parseDouble(realprofit[i]));
						supportInfo.setRequestSupport(Double.parseDouble(requestsupport[i]));
						supportInfo.setCreateDate(new Date(System.currentTimeMillis()));
						supportInfo.setCreateBy(logonUser.getUserId());
						if(auditmoney[0]!=null&&!"".equals(auditmoney[0])){
							supportInfo.setAuditMoney(Double.parseDouble(auditmoney[i]));
						}
						
						dao.insert(supportInfo);
					}
				}
				
				// 页面删除的附件(同意后修改附件)
				String delAttachs = CommonUtils.checkNull(request.getParamValue("delAttachs"));
				String delAttachIds = delAttachs.replaceFirst(",", "");
				String[] delAttachArr = delAttachIds.split(",");
				if (null != delAttachArr && 0 != delAttachArr.length) {
					for (int i = 0; i < delAttachArr.length; i++) {
						FileUploadManager.delfileUploadByBusiness(delAttachArr[i], logonUser);
					}
				}
				// 附件ID
				String[] fjids = request.getParamValues("fjids");
				// 附件添加
				FileUploadManager.fileUploadByBusiness(fleetId, fjids, logonUser);
			}else{
				tfia.setAuditResult(Integer.parseInt(Constant.SUPPORT_INFO_STATUS_05.toString()));
				tfpo1.setSupportStatus(Constant.SUPPORT_INFO_STATUS_05);
			}
			tfpo1.setSupportAuditRemark(remark);
			//更新商务支持审核表
			dao.update(tfpo,tfpo1);
			tfia.setAuditDate(new java.util.Date());
			tfia.setAuditRemark(remark);
			//审核记录表中插入记录
			dao.insert(tfia);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,"集团客户支持审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
