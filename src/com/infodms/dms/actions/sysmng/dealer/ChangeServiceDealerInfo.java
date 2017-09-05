package com.infodms.dms.actions.sysmng.dealer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.dealer.ChangeServiceDealerInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerDetailPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmpTmDealerNewPO;
import com.infodms.dms.po.TtPartAddrDefinePO;
import com.infodms.dms.po.TtPartAddressMorePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: CHANADMS
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-5
 * 
 * @author zjy
 * @mail zhaojinyu@infoservice.com.cn
 * @version 1.0
 * @remark
 */
public class ChangeServiceDealerInfo {
	private final String changeServiceDealerInfoInit = "/jsp/systemMng/dealer/changeServiceDealerInfoInit.jsp";
	
	private final String changeServiceDealerInfoInitforOEM = "/jsp/systemMng/dealer/changeServiceDealerInfoInit_OEM.jsp";
	
	private final String DealerCsInfoChangeAuditInit1 = "/jsp/systemMng/dealer/DealerCsInfoChangeAuditInit1.jsp";
	
	private final String DealerCsInfoChangeAuditInit2 = "/jsp/systemMng/dealer/DealerCsInfoChangeAuditInit2.jsp";
	
	private final String updateServiceDealerInfoInit = "/jsp/systemMng/dealer/updateServiceDealerInfoInit.jsp";
	
	private final String updateServiceDealerInfo = "/jsp/systemMng/dealer/updateServiceDealerInfo.jsp";
	
	public Logger logger = Logger.getLogger(ChangeServiceDealerInfo.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	ChangeServiceDealerInfoDao dao = ChangeServiceDealerInfoDao.getInstance();
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
	/**
	 * 经销商维护查询页面初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryDealerCsInfoForDealerNewInit() {
	    try {
	    		if(logonUser.getDealerId()==null){
	    			List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
	    			act.setOutData("orglist", orgList);
	    			act.setForword(changeServiceDealerInfoInitforOEM);
	    		}else{
	    			act.setForword(changeServiceDealerInfoInit);
	    		}
			} catch (Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理初始化");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
	}
	
	public void queryDealerCsInfoChangeAuditInit1() {
	    try {
		    	List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
				act.setOutData("orglist", orgList);
				act.setForword(DealerCsInfoChangeAuditInit1);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理初始化");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
	}
	
	public void queryDealerCsInfoChangeAuditInit2() {
	    try {
		    	List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
				act.setOutData("orglist", orgList);
				act.setForword(DealerCsInfoChangeAuditInit2);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理初始化");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
	}
	public void queryServiceDealerInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
			map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
			map.put("itemName", CommonUtils.checkNull(request.getParamValue("itemName")));
			map.put("dealStart", CommonUtils.checkNull(request.getParamValue("dealStart")));
			map.put("dealEnd", CommonUtils.checkNull(request.getParamValue("dealEnd")));
			map.put("STATUS", CommonUtils.checkNull(request.getParamValue("STATUS")));
			map.put("__large_org", CommonUtils.checkNull(request.getParamValue("__large_org")));
			map.put("__province_org", CommonUtils.checkNull(request.getParamValue("__province_org")));
			String cmd = CommonUtils.checkNull(request.getParamValue("cmd"));
			 
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			if("1".equals(cmd)){
				ApplicationDao application = new ApplicationDao();
				List<Map<String, Object>> mapList = dao.queryServiceDealerInfo(map, curPage, Constant.PAGE_SIZE_MAX,logonUser).getRecords();
				String[] head={"经销商代码","经销商名称","状态","版本号","修改项目","原姓名","新姓名","原手机","新手机","原座机","新座机"
						,"原邮箱","新邮箱","原传真","新传真","原接收地址","新接收地址","原发票地址","新发票地址","服务站变更提交时间","服务经理审核人","审核时间","主机厂审核人","审核时间"};
				
				List<Map<String, Object>> records = mapList;
				List params=new ArrayList();
				if(records!=null &&records.size()>0){
					for (Map<String, Object> map1 : records) {
						String DEALER_CODE = BaseUtils.checkNull(map1.get("DEALER_CODE"));
						String DEALER_NAME = BaseUtils.checkNull(map1.get("DEALER_NAME"));
						String AUDIT_STATUS = BaseUtils.checkNull(map1.get("AUDIT_STATUS_NAME"));
						String VAR = BaseUtils.checkNull(map1.get("VAR"));
						String ITEM_NAME = BaseUtils.checkNull(map1.get("ITEM_NAME"));
						String OLD_NAME = BaseUtils.checkNull(map1.get("OLD_NAME"));
						String NAME = BaseUtils.checkNull(map1.get("NAME"));
						String OLD_MOBIL = BaseUtils.checkNull(map1.get("OLD_MOBIL"));
						String MOBIL = BaseUtils.checkNull(map1.get("MOBIL"));
						String OLD_PHONE = BaseUtils.checkNull(map1.get("OLD_PHONE"));
						String PHONE = BaseUtils.checkNull(map1.get("PHONE"));
						String OLD_EMAIL = BaseUtils.checkNull(map1.get("OLD_EMAIL"));
						String EMAIL = BaseUtils.checkNull(map1.get("EMAIL"));
						String OLD_FAX = BaseUtils.checkNull(map1.get("OLD_FAX"));
						String FAX = BaseUtils.checkNull(map1.get("FAX"));
						String OLD_ADDRESS = BaseUtils.checkNull(map1.get("OLD_ADDRESS"));
						String ADDRESS = BaseUtils.checkNull(map1.get("ADDRESS"));
						String OLD_FAPIAO_ADDRESS = BaseUtils.checkNull(map1.get("OLD_FAPIAO_ADDRESS"));
						String FAPIAO_ADDRESS = BaseUtils.checkNull(map1.get("FAPIAO_ADDRESS"));
						String CREATE_DATE = BaseUtils.checkNull(map1.get("CREATE_DATE"));
						String AUDIT_BY_DEALER = BaseUtils.checkNull(map1.get("AUDIT_BY_DEALER_NAME"));
						String AUDIT_DATE_DEALER = BaseUtils.checkNull(map1.get("AUDIT_DATE_DEALER"));
						String AUDIT_BY = BaseUtils.checkNull(map1.get("AUDIT_BY_NAME"));
						String AUDIT_DATE = BaseUtils.checkNull(map1.get("AUDIT_DATE"));
						String[] detail={DEALER_CODE,DEALER_NAME,AUDIT_STATUS,VAR,ITEM_NAME,OLD_NAME,NAME,
								OLD_MOBIL,MOBIL,OLD_PHONE,PHONE,OLD_EMAIL,EMAIL,OLD_FAX,
								FAX,OLD_ADDRESS,ADDRESS,OLD_FAPIAO_ADDRESS,FAPIAO_ADDRESS,CREATE_DATE,AUDIT_BY_DEALER,AUDIT_DATE_DEALER,AUDIT_BY,AUDIT_DATE};
						params.add(detail);
					}
				}
				application.toExcel(act, head, params,null,"服务站备案信息更改下载");
			}else{
				PageResult<Map<String, Object>> ps = dao.queryServiceDealerInfo(map, curPage, Constant.PAGE_SIZE,logonUser);
				act.setOutData("ps", ps);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryAuditServiceDealerInfo1() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
			map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
			map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
			map.put("dealStart", CommonUtils.checkNull(request.getParamValue("dealStart")));
			map.put("dealEnd", CommonUtils.checkNull(request.getParamValue("dealEnd")));
			map.put("STATUS", CommonUtils.checkNull(request.getParamValue("STATUS")));
			map.put("__large_org", CommonUtils.checkNull(request.getParamValue("__large_org")));
			map.put("__province_org", CommonUtils.checkNull(request.getParamValue("__province_org")));
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			map.put("itemName", CommonUtils.checkNull(request.getParamValue("itemName")));
			PageResult<Map<String, Object>> ps = dao.queryAuditServiceDealerInfo1(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryAuditServiceDealerInfo2() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
			map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
			map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
			map.put("__large_org", CommonUtils.checkNull(request.getParamValue("__large_org")));
			map.put("__province_org", CommonUtils.checkNull(request.getParamValue("__province_org")));
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			map.put("dealStart", CommonUtils.checkNull(request.getParamValue("dealStart")));
			map.put("dealEnd", CommonUtils.checkNull(request.getParamValue("dealEnd")));
			map.put("itemName", CommonUtils.checkNull(request.getParamValue("itemName")));
			map.put("STATUS", CommonUtils.checkNull(request.getParamValue("STATUS")));
			PageResult<Map<String, Object>> ps = dao.queryAuditServiceDealerInfo2(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void updateServiceDealerInfoInit() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			TmDealerDetailPO dealerPO = new TmDealerDetailPO();
			dealerPO.setFkDealerId(new Long(logonUser.getDealerId()));
			
			dealerPO = (TmDealerDetailPO)dao.select(dealerPO).get(0);
			
			TtPartAddrDefinePO addrDefinePO = new TtPartAddrDefinePO();
			addrDefinePO.setDealerId(new Long(logonUser.getDealerId()));
			addrDefinePO.setState(10011001);
			addrDefinePO = (TtPartAddrDefinePO)dao.select(addrDefinePO).get(0);
			
			TtPartAddressMorePO addrMorePO = new TtPartAddressMorePO();
			addrMorePO.setDealerId(new Long(logonUser.getDealerId()));
			addrMorePO.setState(10011001);
			List<TtPartAddressMorePO> addrMoreList = dao.select(addrMorePO);
			if(addrMoreList!=null&&addrMoreList.size()>0){
				act.setOutData("addrMoreList", addrMoreList.get(0));
			}
			
			act.setOutData("dealerPO", dealerPO);
			act.setOutData("addrDefinePO", addrDefinePO);
			act.setForword(updateServiceDealerInfoInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void reUpdateServiceDealerInfoInit() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParamValue("id");
			String itemName = request.getParamValue("itemName");
			
			if("服务经理".equals(itemName)){
				//服务经理
				String SER_MANAGER_NAME = CommonUtils.checkNull(request.getParamValue("SER_MANAGER_NAME"));
				String SER_MANAGER_TELPHONE = CommonUtils.checkNull(request.getParamValue("SER_MANAGER_TELPHONE"));
				String SER_MANAGER_PHONE = CommonUtils.checkNull(request.getParamValue("SER_MANAGER_PHONE"));
				String SER_MANAGER_EMAIL = CommonUtils.checkNull(request.getParamValue("SER_MANAGER_EMAIL"));
				
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(id));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setName(SER_MANAGER_NAME);
				auditPO2.setMobil(SER_MANAGER_TELPHONE);
				auditPO2.setPhone(SER_MANAGER_PHONE);
				auditPO2.setEmail(SER_MANAGER_EMAIL);
				auditPO2.setUpdateBy(logonUser.getUserId());
				auditPO2.setUpdateDate(new Date());
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				
				dao.update(auditPO,auditPO2);
			}else if("服务主管".equals(itemName)){
				//服务主管
				String SER_DIRECTOR_NAME = CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_NAME"));
				String SER_DIRECTOR_TELHONE = CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_TELHONE"));
				String SER_DIRECTOR_PHONE = CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_PHONE"));
				
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(id));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setName(SER_DIRECTOR_NAME);
				auditPO2.setMobil(SER_DIRECTOR_TELHONE);
				auditPO2.setPhone(SER_DIRECTOR_PHONE);
				auditPO2.setUpdateBy(logonUser.getUserId());
				auditPO2.setUpdateDate(new Date());
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				
				dao.update(auditPO,auditPO2);
			}else if("备件主管".equals(itemName)){
				//备件主管
				String FITTINGS_DEC_NAME = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_NAME"));
				String FITTINGS_DEC_PHONE = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_PHONE"));
				String FITTINGS_DEC_TELPHONE = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_TELPHONE"));
				String FITTINGS_DEC_EMAIL = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_EMAIL"));
				String FITTINGS_DEC_FAX = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_FAX"));
				
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(id));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setName(FITTINGS_DEC_NAME);
				auditPO2.setMobil(FITTINGS_DEC_TELPHONE);
				auditPO2.setPhone(FITTINGS_DEC_PHONE);
				auditPO2.setEmail(FITTINGS_DEC_EMAIL);
				auditPO2.setFax(FITTINGS_DEC_FAX);
				auditPO2.setUpdateBy(logonUser.getUserId());
				auditPO2.setUpdateDate(new Date());
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				
				dao.update(auditPO,auditPO2);
			}else if("技术主管".equals(itemName)){
				//技术主管
				String TECHNOLOGY_DIRECTOR_NAME = CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_NAME"));
				String TECHNOLOGY_DIRECTOR_TELPHONE = CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_TELPHONE"));
				
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(id));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setName(TECHNOLOGY_DIRECTOR_NAME);
				auditPO2.setMobil(TECHNOLOGY_DIRECTOR_TELPHONE);
				auditPO2.setUpdateBy(logonUser.getUserId());
				auditPO2.setUpdateDate(new Date());
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				
				dao.update(auditPO,auditPO2);
			}else if("索赔主管".equals(itemName)){
				//索赔主管
				String CLAIM_DIRECTOR_NAME = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_NAME"));
				String CLAIM_DIRECTOR_TELPHONE = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_TELPHONE"));
				String CLAIM_DIRECTOR_PHONE = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_PHONE"));
				String CLAIM_DIRECTOR_EMAIL = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_EMAIL"));
				String CLAIM_DIRECTOR_FAX = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_FAX"));
				
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(id));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setName(CLAIM_DIRECTOR_NAME);
				auditPO2.setMobil(CLAIM_DIRECTOR_TELPHONE);
				auditPO2.setPhone(CLAIM_DIRECTOR_PHONE);
				auditPO2.setEmail(CLAIM_DIRECTOR_EMAIL);
				auditPO2.setFax(CLAIM_DIRECTOR_FAX);
				auditPO2.setUpdateBy(logonUser.getUserId());
				auditPO2.setUpdateDate(new Date());
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				
				dao.update(auditPO,auditPO2);
			}else if("24小时热线".equals(itemName)){
				//24小时热线
				String HOTLINE = CommonUtils.checkNull(request.getParamValue("SERVICE_HOTLINE"));
				
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(id));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setPhone(HOTLINE);
				auditPO2.setUpdateBy(logonUser.getUserId());
				auditPO2.setUpdateDate(new Date());
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				
				dao.update(auditPO,auditPO2);
			}else if("备件接收地址".equals(itemName)){
				//备件接收地址
				String ADDRESS = CommonUtils.checkNull(request.getParamValue("ADDRESS"));
				
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(id));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setAddress(ADDRESS);
				auditPO2.setUpdateBy(logonUser.getUserId());
				auditPO2.setUpdateDate(new Date());
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				
				dao.update(auditPO,auditPO2);
			}else if("备件接收人".equals(itemName)){
				//备件接收人
				String LINKMAN = CommonUtils.checkNull(request.getParamValue("LINK_MAN"));
				String MOBILE_PHONE = CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE"));
				String TEL = CommonUtils.checkNull(request.getParamValue("TEL_"));
				
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(id));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setName(LINKMAN);
				auditPO2.setMobil(MOBILE_PHONE);
				auditPO2.setPhone(TEL);
				auditPO2.setUpdateBy(logonUser.getUserId());
				auditPO2.setUpdateDate(new Date());
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				
				dao.update(auditPO,auditPO2);
			}
			act.setForword(changeServiceDealerInfoInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void updateServiceDealerInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			TmDealerDetailPO dealerPO = new TmDealerDetailPO();
			dealerPO.setFkDealerId(new Long(logonUser.getDealerId()));
			
			dealerPO = (TmDealerDetailPO)dao.select(dealerPO).get(0);
			
			TtPartAddrDefinePO addrDefinePO = new TtPartAddrDefinePO();
			addrDefinePO.setDealerId(new Long(logonUser.getDealerId()));
			
			addrDefinePO = (TtPartAddrDefinePO)dao.select(addrDefinePO).get(0);
			
			String id = request.getParamValue("ID");
			TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
			dealerNewPO.setId(new Long(id));
			dealerNewPO = (TmpTmDealerNewPO)dao.select(dealerNewPO).get(0);
			
			act.setOutData("dealerNewPO", dealerNewPO);
			act.setOutData("dealerPO", dealerPO);
			act.setOutData("addrDefinePO", addrDefinePO);
			act.setForword(updateServiceDealerInfo);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void insertServiceDealerInfoInit() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Date date = new Date();
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(new Long(logonUser.getDealerId()));
			dealerPO = (TmDealerPO) dao.select(dealerPO).get(0);
			String dealerName = dealerPO.getDealerName();
			
			String var = dao.getMaxVar(logonUser.getDealerId());
			//服务经理
			String SER_MANAGER_NAME = CommonUtils.checkNull(request.getParamValue("SER_MANAGER_NAME"));
			String SER_MANAGER_TELPHONE = CommonUtils.checkNull(request.getParamValue("SER_MANAGER_TELPHONE"));
			String SER_MANAGER_PHONE = CommonUtils.checkNull(request.getParamValue("SER_MANAGER_PHONE"));
			String SER_MANAGER_EMAIL = CommonUtils.checkNull(request.getParamValue("SER_MANAGER_EMAIL"));
			
			String OLD_SER_MANAGER_NAME = CommonUtils.checkNull(request.getParamValue("serManagerName"));
			String OLD_SER_MANAGER_TELPHONE = CommonUtils.checkNull(request.getParamValue("serManagerTelphone"));
			String OLD_SER_MANAGER_PHONE = CommonUtils.checkNull(request.getParamValue("serManagerPhone"));
			String OLD_SER_MANAGER_EMAIL = CommonUtils.checkNull(request.getParamValue("serManagerEmail"));
			
			if (!StringUtil.isNull(SER_MANAGER_NAME) || !StringUtil.isNull(SER_MANAGER_TELPHONE) || !StringUtil.isNull(SER_MANAGER_PHONE) || !StringUtil.isNull(SER_MANAGER_EMAIL)) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("服务经理");
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				if (!StringUtil.isNull(SER_MANAGER_NAME)){
					dealerNewPO.setOldName(OLD_SER_MANAGER_NAME);
					dealerNewPO.setName(SER_MANAGER_NAME);
				}
				
				if (!StringUtil.isNull(SER_MANAGER_TELPHONE)){
					dealerNewPO.setOldMobil(OLD_SER_MANAGER_TELPHONE);
					dealerNewPO.setMobil(SER_MANAGER_TELPHONE);
				}
				
				if (!StringUtil.isNull(SER_MANAGER_PHONE)){
					dealerNewPO.setOldPhone(OLD_SER_MANAGER_PHONE);
					dealerNewPO.setPhone(SER_MANAGER_PHONE);
				}
				
				if (!StringUtil.isNull(SER_MANAGER_EMAIL)){
					dealerNewPO.setOldEmail(OLD_SER_MANAGER_EMAIL);
					dealerNewPO.setEmail(SER_MANAGER_EMAIL);
				}
				
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}

			//服务主管
			String SER_DIRECTOR_NAME = CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_NAME"));
			String SER_DIRECTOR_TELHONE = CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_TELHONE"));
			String SER_DIRECTOR_PHONE = CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_PHONE"));
			
			String OLD_SER_DIRECTOR_NAME = CommonUtils.checkNull(request.getParamValue("serDirectorName"));
			String OLD_SER_DIRECTOR_TELHONE = CommonUtils.checkNull(request.getParamValue("serDirectorTelhone"));
			String OLD_SER_DIRECTOR_PHONE = CommonUtils.checkNull(request.getParamValue("serDirectorPhone"));
			
			if (!StringUtil.isNull(SER_DIRECTOR_NAME) || !StringUtil.isNull(SER_DIRECTOR_TELHONE) || !StringUtil.isNull(SER_DIRECTOR_PHONE)) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("服务主管");
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				if (!StringUtil.isNull(SER_DIRECTOR_NAME)){
					dealerNewPO.setOldName(OLD_SER_DIRECTOR_NAME);
					dealerNewPO.setName(SER_DIRECTOR_NAME);
				}

				if (!StringUtil.isNull(SER_DIRECTOR_TELHONE)){
					dealerNewPO.setOldMobil(OLD_SER_DIRECTOR_TELHONE);
					dealerNewPO.setMobil(SER_DIRECTOR_TELHONE);
				}
				
				if (!StringUtil.isNull(SER_DIRECTOR_PHONE)){
					dealerNewPO.setOldPhone(OLD_SER_DIRECTOR_PHONE);
					dealerNewPO.setPhone(SER_DIRECTOR_PHONE);
				}
				
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}
			//备件主管
			String FITTINGS_DEC_NAME = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_NAME"));
			String FITTINGS_DEC_PHONE = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_PHONE"));
			String FITTINGS_DEC_TELPHONE = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_TELPHONE"));
			String FITTINGS_DEC_EMAIL = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_EMAIL"));
			String FITTINGS_DEC_FAX = CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_FAX"));
			
			String OLD_FITTINGS_DEC_NAME = CommonUtils.checkNull(request.getParamValue("fittingsDecName"));
			String OLD_FITTINGS_DEC_PHONE = CommonUtils.checkNull(request.getParamValue("fittingsDecPhone"));
			String OLD_FITTINGS_DEC_TELPHONE = CommonUtils.checkNull(request.getParamValue("fittingsDecTelphone"));
			String OLD_FITTINGS_DEC_EMAIL = CommonUtils.checkNull(request.getParamValue("fittingsDecEmail"));
			String OLD_FITTINGS_DEC_FAX = CommonUtils.checkNull(request.getParamValue("fittingsDecFax"));
			
			if (!StringUtil.isNull(FITTINGS_DEC_NAME) || !StringUtil.isNull(FITTINGS_DEC_PHONE) || !StringUtil.isNull(FITTINGS_DEC_TELPHONE) || !StringUtil.isNull(FITTINGS_DEC_EMAIL)
					|| !StringUtil.isNull(FITTINGS_DEC_FAX)) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("备件主管");
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				if (!StringUtil.isNull(FITTINGS_DEC_NAME)){
					dealerNewPO.setOldName(OLD_FITTINGS_DEC_NAME);
					dealerNewPO.setName(FITTINGS_DEC_NAME);
				}

				if (!StringUtil.isNull(FITTINGS_DEC_PHONE)){
					dealerNewPO.setOldMobil(OLD_FITTINGS_DEC_TELPHONE);
					dealerNewPO.setMobil(FITTINGS_DEC_PHONE);
				}
				
				if (!StringUtil.isNull(FITTINGS_DEC_TELPHONE)){
					dealerNewPO.setOldPhone(OLD_FITTINGS_DEC_PHONE);
					dealerNewPO.setPhone(FITTINGS_DEC_TELPHONE);
				}
				
				if (!StringUtil.isNull(FITTINGS_DEC_EMAIL)){
					dealerNewPO.setOldEmail(OLD_FITTINGS_DEC_EMAIL);
					dealerNewPO.setEmail(FITTINGS_DEC_EMAIL);
				}

				if (!StringUtil.isNull(FITTINGS_DEC_FAX)){
					dealerNewPO.setOldFax(OLD_FITTINGS_DEC_FAX);
					dealerNewPO.setFax(FITTINGS_DEC_FAX);
				}
				
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}
			//技术主管
			String TECHNOLOGY_DIRECTOR_NAME = CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_NAME"));
			String TECHNOLOGY_DIRECTOR_TELPHONE = CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_TELPHONE"));
			
			String OLD_TECHNOLOGY_DIRECTOR_NAME = CommonUtils.checkNull(request.getParamValue("technologyDirectorName"));
			String OLD_TECHNOLOGY_DIRECTOR_TELPHONE = CommonUtils.checkNull(request.getParamValue("technologyDirectorTelphone"));
			
			if (!StringUtil.isNull(TECHNOLOGY_DIRECTOR_NAME) || !StringUtil.isNull(TECHNOLOGY_DIRECTOR_TELPHONE)) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("技术主管");
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				if (!StringUtil.isNull(TECHNOLOGY_DIRECTOR_NAME)){
					dealerNewPO.setOldName(OLD_TECHNOLOGY_DIRECTOR_NAME);
					dealerNewPO.setName(TECHNOLOGY_DIRECTOR_NAME);
				}
				
				if (!StringUtil.isNull(TECHNOLOGY_DIRECTOR_TELPHONE)){
					dealerNewPO.setOldMobil(OLD_TECHNOLOGY_DIRECTOR_TELPHONE);
					dealerNewPO.setMobil(TECHNOLOGY_DIRECTOR_TELPHONE);
				}
				
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}
			//索赔主管
			String CLAIM_DIRECTOR_NAME = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_NAME"));
			String CLAIM_DIRECTOR_TELPHONE = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_TELPHONE"));
			String CLAIM_DIRECTOR_PHONE = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_PHONE"));
			String CLAIM_DIRECTOR_EMAIL = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_EMAIL"));
			String CLAIM_DIRECTOR_FAX = CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_FAX"));
			
			String OLD_CLAIM_DIRECTOR_NAME = CommonUtils.checkNull(request.getParamValue("claimDirectorName"));
			String OLD_CLAIM_DIRECTOR_TELPHONE = CommonUtils.checkNull(request.getParamValue("claimDirectorTelphone"));
			String OLD_CLAIM_DIRECTOR_PHONE = CommonUtils.checkNull(request.getParamValue("claimDirectorPhone"));
			String OLD_CLAIM_DIRECTOR_EMAIL = CommonUtils.checkNull(request.getParamValue("claimDirectorEmail"));
			String OLD_CLAIM_DIRECTOR_FAX = CommonUtils.checkNull(request.getParamValue("claimDirectorFax"));
			
			if (!StringUtil.isNull(CLAIM_DIRECTOR_NAME) || !StringUtil.isNull(CLAIM_DIRECTOR_TELPHONE) || !StringUtil.isNull(CLAIM_DIRECTOR_PHONE)
					|| !StringUtil.isNull(CLAIM_DIRECTOR_EMAIL) || !StringUtil.isNull(CLAIM_DIRECTOR_FAX)) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("索赔主管");
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				if (!StringUtil.isNull(CLAIM_DIRECTOR_NAME)){
					dealerNewPO.setOldName(OLD_CLAIM_DIRECTOR_NAME);
					dealerNewPO.setName(CLAIM_DIRECTOR_NAME);
				}
				
				if (!StringUtil.isNull(CLAIM_DIRECTOR_TELPHONE)){
					dealerNewPO.setOldMobil(OLD_CLAIM_DIRECTOR_TELPHONE);
					dealerNewPO.setMobil(CLAIM_DIRECTOR_TELPHONE);
				}
				
				if (!StringUtil.isNull(CLAIM_DIRECTOR_PHONE)){
					dealerNewPO.setOldPhone(OLD_CLAIM_DIRECTOR_PHONE);
					dealerNewPO.setPhone(CLAIM_DIRECTOR_PHONE);
				}

				if (!StringUtil.isNull(CLAIM_DIRECTOR_EMAIL)){
					dealerNewPO.setOldEmail(OLD_CLAIM_DIRECTOR_EMAIL);
					dealerNewPO.setEmail(CLAIM_DIRECTOR_EMAIL);
				}
				
				if (!StringUtil.isNull(CLAIM_DIRECTOR_FAX)){
					dealerNewPO.setOldFax(OLD_CLAIM_DIRECTOR_FAX);
					dealerNewPO.setFax(CLAIM_DIRECTOR_FAX);
				}
				
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}
			//财务主管
			String financeManagerName = CommonUtils.checkNull(request.getParamValue("financeManagerName"));
			String financeManagerTelphone = CommonUtils.checkNull(request.getParamValue("financeManagerTelphone"));
			String financeManagerPhone = CommonUtils.checkNull(request.getParamValue("financeManagerPhone"));
			String financeManagerEmail = CommonUtils.checkNull(request.getParamValue("financeManagerEmail"));
			
			String FINANCE_MANAGER_NAME = CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_NAME"));
			String FINANCE_MANAGER_TELPHONE = CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_TELPHONE"));
			String FINANCE_MANAGER_PHONE = CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_PHONE"));
			String FINANCE_MANAGER_EMAIL = CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_EMAIL"));
			
			if (!StringUtil.isNull(FINANCE_MANAGER_NAME) || !StringUtil.isNull(FINANCE_MANAGER_TELPHONE) || !StringUtil.isNull(FINANCE_MANAGER_PHONE)
					|| !StringUtil.isNull(FINANCE_MANAGER_EMAIL) ) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("财务主管");
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				if (!StringUtil.isNull(FINANCE_MANAGER_NAME)){
					dealerNewPO.setOldName(financeManagerName);
					dealerNewPO.setName(FINANCE_MANAGER_NAME);
				}
				
				if (!StringUtil.isNull(FINANCE_MANAGER_TELPHONE)){
					dealerNewPO.setOldMobil(financeManagerTelphone);
					dealerNewPO.setMobil(FINANCE_MANAGER_TELPHONE);
				}
				if (!StringUtil.isNull(FINANCE_MANAGER_PHONE)){
					dealerNewPO.setOldPhone(financeManagerPhone);
					dealerNewPO.setPhone(FINANCE_MANAGER_PHONE);
				}

				if (!StringUtil.isNull(FINANCE_MANAGER_EMAIL)){
					dealerNewPO.setOldEmail(financeManagerEmail);
					dealerNewPO.setEmail(FINANCE_MANAGER_EMAIL);
				}
				
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}
			
			//24小时热线
			String SERVICE_HOTLINE = CommonUtils.checkNull(request.getParamValue("SERVICE_HOTLINE"));
			
			String OLD_SERVICE_HOTLINE = CommonUtils.checkNull(request.getParamValue("serviceHotline"));
			if (!StringUtil.isNull(SERVICE_HOTLINE)) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("24小时热线");
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				
				dealerNewPO.setOldPhone(OLD_SERVICE_HOTLINE);
				dealerNewPO.setPhone(SERVICE_HOTLINE);
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}
			//备件接收地址
			String ADDRESS = CommonUtils.checkNull(request.getParamValue("ADDRESS"));
			String OLD_ADDRESS = CommonUtils.checkNull(request.getParamValue("addr"));
			if (!StringUtil.isNull(ADDRESS)) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("备件接收地址");
				dealerNewPO.setOldAddress(OLD_ADDRESS);
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				dealerNewPO.setAddress(ADDRESS);
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}
			//备件fapiao地址
			String FAPIAO_ADDRESS = CommonUtils.checkNull(request.getParamValue("FAPIAO_ADDRESS"));
			String OLD_FAPIAO_ADDRESS = CommonUtils.checkNull(request.getParamValue("FAPIAO_ADDR"));
			if (!StringUtil.isNull(FAPIAO_ADDRESS)) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("备件发票地址");
				dealerNewPO.setOldFapiaoAddress(OLD_FAPIAO_ADDRESS);
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				dealerNewPO.setFapiaoAddress(FAPIAO_ADDRESS);
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}
			//备件接收人
			String LINKMAN = CommonUtils.checkNull(request.getParamValue("LINK_MAN"));
			String MOBILE_PHONE = CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE"));
			String TEL = CommonUtils.checkNull(request.getParamValue("TEL_"));
			
			String OLD_LINKMAN = CommonUtils.checkNull(request.getParamValue("linkman"));
			String OLD_MOBILE_PHONE = CommonUtils.checkNull(request.getParamValue("mobilePhone"));
			String OLD_TEL = CommonUtils.checkNull(request.getParamValue("tel"));
			if (!StringUtil.isNull(LINKMAN)|| !StringUtil.isNull(MOBILE_PHONE)|| !StringUtil.isNull(TEL)) {
				TmpTmDealerNewPO dealerNewPO = new TmpTmDealerNewPO();
				dealerNewPO.setItemName("备件接收人");
				dealerNewPO.setId(new Long(SequenceManager.getSequence("")));
				if (!StringUtil.isNull(LINKMAN)){
					dealerNewPO.setName(LINKMAN);
					dealerNewPO.setOldName(OLD_LINKMAN);
				}
				
				if (!StringUtil.isNull(TEL)){
					dealerNewPO.setOldPhone(OLD_TEL);
					dealerNewPO.setPhone(TEL);
				}
				
				if (!StringUtil.isNull(MOBILE_PHONE)){
					dealerNewPO.setOldMobil(OLD_MOBILE_PHONE);
					dealerNewPO.setMobil(MOBILE_PHONE);
				}
				
				dealerNewPO.setCreateBy(logonUser.getUserId());
				dealerNewPO.setCreateDate(date);
				dealerNewPO.setDealerCode(logonUser.getDealerCode());
				dealerNewPO.setDealerId(logonUser.getDealerId());
				dealerNewPO.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_01);
				dealerNewPO.setDealerName(dealerName);
				dealerNewPO.setVar(new Integer(var));
				dao.insert(dealerNewPO);
			}
			
			act.setForword(changeServiceDealerInfoInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商管理查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void batchAccept1() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			Date date = new Date();
			String ID = CommonUtils.checkNull(request.getParamValue("ID"));
			String [] IDS = ID.split(",");
			
			for (int i = 0; i < IDS.length; i++) {
				String remark = CommonUtils.checkNull(request.getParamValue("remark_"+IDS[i]));
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(IDS[i]));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_02);
				auditPO2.setAuditByDealer(logonUser.getUserId());
				auditPO2.setAuditDateDealer(date);
				auditPO2.setRemark(remark);
				dao.update(auditPO,auditPO2);
			}
			act.setForword(DealerCsInfoChangeAuditInit1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void batchAccept2() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			Date date = new Date();
			String ID = CommonUtils.checkNull(request.getParamValue("ID"));
			String [] IDS = ID.split(",");
			
			for (int i = 0; i < IDS.length; i++) {
				String remark = CommonUtils.checkNull(request.getParamValue("remark_"+IDS[i]));
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(IDS[i]));
				TmpTmDealerNewPO newPO = (TmpTmDealerNewPO) dao.select(auditPO).get(0);
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_03);
				auditPO2.setAuditBy(logonUser.getUserId());
				auditPO2.setAuditDate(date);
				auditPO2.setRemark(remark);
				dao.update(auditPO,auditPO2);
				
				TmDealerDetailPO dealerPO = new TmDealerDetailPO(); 
				TmDealerDetailPO dealerPO2 = new TmDealerDetailPO(); 
				dealerPO.setFkDealerId(new Long(newPO.getDealerId()));
				
				TtPartAddrDefinePO addrDefinePO = new TtPartAddrDefinePO();
				TtPartAddrDefinePO addrDefinePO2 = new TtPartAddrDefinePO();
				addrDefinePO.setDealerId(new Long(newPO.getDealerId()));
				
				TtPartAddressMorePO addressMorePO = new TtPartAddressMorePO();
				TtPartAddressMorePO addressMorePO2 = new TtPartAddressMorePO();
				addressMorePO.setDealerId(new Long(newPO.getDealerId()));
				addressMorePO.setAddressType(20491002);
				boolean dealerUpdateFlag = false;
				boolean addrDefineUpdateFlag = false;
				boolean addressMoreFalg = false;
				if(newPO.getItemName().equals("服务经理")){
					if(!StringUtil.isNull(newPO.getName())){
						dealerPO2.setSerManagerName(newPO.getName());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getPhone())){
						dealerPO2.setSerManagerPhone(newPO.getPhone());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getMobil())){
						dealerPO2.setSerManagerTelphone(newPO.getMobil());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getEmail())){
						dealerPO2.setSerManagerEmail(newPO.getEmail());
						dealerUpdateFlag = true;
					}
				}else if (newPO.getItemName().equals("服务主管")){
					if(!StringUtil.isNull(newPO.getName())){
						dealerPO2.setSerDirectorName(newPO.getName());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getPhone())){
						dealerPO2.setSerDirectorPhone(newPO.getPhone());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getMobil())){
						dealerPO2.setSerDirectorTelhone(newPO.getMobil());
						dealerUpdateFlag = true;
					}
				}else if (newPO.getItemName().equals("备件主管")){
					if(!StringUtil.isNull(newPO.getName())){
						dealerPO2.setFittingsDecName(newPO.getName());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getPhone())){
						dealerPO2.setFittingsDecTelphone(newPO.getPhone());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getMobil())){
						dealerPO2.setFittingsDecPhone(newPO.getMobil());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getEmail())){
						dealerPO2.setFittingsDecEmail(newPO.getEmail());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getFax())){
						dealerPO2.setFittingsDecFax(newPO.getFax());
						dealerUpdateFlag = true;
					}
				}else if (newPO.getItemName().equals("技术主管")){
					if(!StringUtil.isNull(newPO.getName())){
						dealerPO2.setTechnologyDirectorName(newPO.getName());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getMobil())){
						dealerPO2.setTechnologyDirectorTelphone(newPO.getMobil());
						dealerUpdateFlag = true;
					}
				}else if (newPO.getItemName().equals("索赔主管")){
					if(!StringUtil.isNull(newPO.getName())){
						dealerPO2.setClaimDirectorName(newPO.getName());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getPhone())){
						dealerPO2.setClaimDirectorPhone(newPO.getPhone());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getMobil())){
						dealerPO2.setClaimDirectorTelphone(newPO.getMobil());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getEmail())){
						dealerPO2.setClaimDirectorEmail(newPO.getEmail());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getFax())){
						dealerPO2.setClaimDirectorFax(newPO.getFax());
						dealerUpdateFlag = true;
					}
				}else if (newPO.getItemName().equals("财务主管")){
					if(!StringUtil.isNull(newPO.getName())){
						dealerPO2.setFinanceManagerName(newPO.getName());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getPhone())){
						dealerPO2.setFinanceManagerPhone(newPO.getPhone());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getMobil())){
						dealerPO2.setFinanceManagerTelphone(newPO.getMobil());
						dealerUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getEmail())){
						dealerPO2.setFinanceManagerEmail(newPO.getEmail());
						dealerUpdateFlag = true;
					}
				}else if (newPO.getItemName().equals("24小时热线")){
					if(!StringUtil.isNull(newPO.getPhone())){
						dealerPO2.setHotline(newPO.getPhone());
						dealerUpdateFlag = true;
					}
				}else if (newPO.getItemName().equals("备件接收地址")){
					if(!StringUtil.isNull(newPO.getAddress())){
						addrDefinePO2.setAddr(newPO.getAddress());
						addrDefineUpdateFlag = true;
					}
				}else if (newPO.getItemName().equals("备件发票地址")){
					if(!StringUtil.isNull(newPO.getFapiaoAddress())){
						addressMorePO2.setAddr(newPO.getFapiaoAddress());
						addressMoreFalg = true;
					}
				}else if (newPO.getItemName().equals("备件接收人")){
					if(!StringUtil.isNull(newPO.getName())){
						addrDefinePO2.setLinkman(newPO.getName());
						addrDefineUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getPhone())){
						addrDefinePO2.setTel(newPO.getPhone());
						addrDefineUpdateFlag = true;
					}
					if(!StringUtil.isNull(newPO.getMobil())){
						addrDefinePO2.setMobilePhone(newPO.getMobil());
						addrDefineUpdateFlag = true;
					}
				}
				if(dealerUpdateFlag){
					dao.update(dealerPO, dealerPO2);
				}
				if(addrDefineUpdateFlag){
					dao.update(addrDefinePO, addrDefinePO2);
				}
				if(addressMoreFalg){
					dao.update(addressMorePO, addressMorePO2);
				}
			}
			act.setForword(DealerCsInfoChangeAuditInit2);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void updateServiceDealerInfoCheck() {
		try {
			if(dao.checkAuditStatus(logonUser.getDealerId())){
				act.setOutData("returnValue", "exists");
			}else{
				act.setOutData("returnValue", "nonExistent");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void batchRebut1() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			Date date = new Date();
			String ID = CommonUtils.checkNull(request.getParamValue("ID"));
			String [] IDS = ID.split(",");
			
			for (int i = 0; i < IDS.length; i++) {
				String REMARK = CommonUtils.checkNull(request.getParamValue("remark_"+IDS[i]));
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(IDS[i]));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_04);
				auditPO2.setAuditBy(logonUser.getUserId());
				auditPO2.setAuditDate(date);
				auditPO2.setRemark(REMARK);
				dao.update(auditPO,auditPO2);
			}
			act.setForword(DealerCsInfoChangeAuditInit1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void batchRebut2() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			Date date = new Date();
			String ID = CommonUtils.checkNull(request.getParamValue("ID"));
			String [] IDS = ID.split(",");
			
			for (int i = 0; i < IDS.length; i++) {
				String REMARK = CommonUtils.checkNull(request.getParamValue("remark_"+IDS[i]));
				TmpTmDealerNewPO auditPO = new TmpTmDealerNewPO();
				auditPO.setId(new Long(IDS[i]));
				TmpTmDealerNewPO auditPO2 = new TmpTmDealerNewPO();
				auditPO2.setAuditStatus(Constant.SERVICE_CHANGE_STATUS_04);
				auditPO2.setAuditBy(logonUser.getUserId());
				auditPO2.setAuditDate(date);
				auditPO2.setRemark(REMARK);
				dao.update(auditPO,auditPO2);
			}
			act.setForword(DealerCsInfoChangeAuditInit2);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
