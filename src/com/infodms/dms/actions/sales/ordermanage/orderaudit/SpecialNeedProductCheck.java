/**********************************************************************
* <pre>
* FILE : SpecialNeedProductCheck.java
* CLASS : SpecialNeedProductCheck
* AUTHOR : 
* FUNCTION : 订单审核
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-17|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtVsSpecialReqChkPO;
import com.infodms.dms.po.TtVsSpecialReqDtlPO;
import com.infodms.dms.po.TtVsSpecialReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 订做车需求产品审核Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-17
 * @author 
 * @mail   	
 * @version 1.0
 * @remark 
 */
public class SpecialNeedProductCheck {
	public Logger logger = Logger.getLogger(SpecialNeedProductCheck.class);   
	SpecialNeedDao dao  = SpecialNeedDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/orderaudit/specialNeedProductCheckQuery.jsp";
	private final String initUrlLever = "/jsp/sales/ordermanage/orderaudit/specialNeedProductCheckQueryLever.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderaudit/specialNeedProductDetailCheck.jsp";
	private final String detailUrlLever = "/jsp/sales/ordermanage/orderaudit/specialNeedProductDetailCheckLever.jsp";
	/**
	 * 订做车需求产品审核页面初始化
	 */
	public void specialNeedProductCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求产品审核页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订做车需求产品审核页面初始化
	 */
	public void specialNeedProductCheckInitLever(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			act.setForword(initUrlLever);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求产品审核页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求产品审核查询
	 */
	public void specialNeedProductCheckQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String areaId = request.getParamValue("areaId");			//业务范围ID
			String area = request.getParamValue("area");				//业务范围IDS
			String dealerCode = request.getParamValue("dealerCode");	//经销商CODE
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = dao.getSpecialNeedCheckList(orgId, areaId, area, dealerCode, startDate, endDate, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求产品审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 下级订做车需求产品审核查询
	 */
	public void specialNeedProductCheckQueryLever(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String areaId = request.getParamValue("areaId");			//业务范围ID
			String area = request.getParamValue("area");				//业务范围IDS
			
			Long companyId = logonUser.getCompanyId() ;
			Long poseId = logonUser.getPoseId() ;
			// String dealerCode = request.getParamValue("dealerCode");	//经销商CODE
			
			/*String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;*/
			
			/*if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}*/
			DealerRelation dr = new DealerRelation();
			String dealerIds = dr.getDealerIdByPose(companyId, poseId);
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = dao.getSpecialNeedCheckListLever(dealerIds, areaId, area, startDate, endDate, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求产品审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 订做车需求产品审核明细查询
	 */
	public void specialNeedProductDetailCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");				//需求ID
			String ver = request.getParamValue("ver");
			TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
			tvsrpContion.setReqId(Long.parseLong(reqId));
			List<PO> datalist = dao.select(tvsrpContion);
			if(datalist!=null&&datalist.size()>0){
				tvsrpValue =(TtVsSpecialReqPO)datalist.get(0);
			}
			String remark = tvsrpValue.getRefitDesc();
			List<Map<String, Object>> list = dao.getSpecialNeedDetailCheckList(reqId);
			List<Map<String, Object>> checkList = dao.getSpecialNeedCheck(reqId);
			List<Map<String, Object>> attachList = dao.getAttachInfos(reqId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			String fleetName = "";
			if(tvsrpValue.getFleetId() != null){
				TmFleetPO fleet = new TmFleetPO();
				fleet.setFleetId(tvsrpValue.getFleetId());
				List<PO> fleetList = dao.select(fleet);
				if(fleetList.size() != 0){
					fleet = (TmFleetPO) fleetList.get(0);
					fleetName = fleet.getFleetName();
				}
			}
			act.setOutData("checkList", checkList) ;
			act.setOutData("list", list);
			act.setOutData("reqId", reqId);
			act.setOutData("ver", ver);
			act.setOutData("remark", remark);
			act.setOutData("fleetName", fleetName);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求产品审核明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 下级订做车需求产品审核明细查询
	 */
	public void specialNeedProductDetailCheckInitLever(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");				//需求ID
			String ver = request.getParamValue("ver");
			TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
			tvsrpContion.setReqId(Long.parseLong(reqId));
			List<PO> datalist = dao.select(tvsrpContion);
			if(datalist!=null&&datalist.size()>0){
				tvsrpValue =(TtVsSpecialReqPO)datalist.get(0);
			}
			String remark = tvsrpValue.getRefitDesc();
			List<Map<String, Object>> list = dao.getSpecialNeedDetailCheckList(reqId);
			List<Map<String, Object>> checkList = dao.getSpecialNeedCheck(reqId);
			List<Map<String, Object>> attachList = dao.getAttachInfos(reqId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}
			String fleetName = "";
			if(tvsrpValue.getFleetId() != null){
				TmFleetPO fleet = new TmFleetPO();
				fleet.setFleetId(tvsrpValue.getFleetId());
				List<PO> fleetList = dao.select(fleet);
				if(fleetList.size() != 0){
					fleet = (TmFleetPO) fleetList.get(0);
					fleetName = fleet.getFleetName();
				}
			}
			act.setOutData("checkList", checkList) ;
			act.setOutData("list", list);
			act.setOutData("reqId", reqId);
			act.setOutData("ver", ver);
			act.setOutData("remark", remark);
			act.setOutData("fleetName", fleetName);
			act.setForword(detailUrlLever);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求产品审核明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求产品明细审核保存
	 */
	public void specialNeedProductDetailCheck(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String detailIds = request.getParamValue("detailIds");				//需求明细ID
			String batchNos = CommonUtils.checkNull(request.getParamValue("batchNos"));				//批次号
			//String expectedDates = request.getParamValue("expectedDates");	//预计交付日期
			String flag = request.getParamValue("flag");						//页面参数
			String reqId = request.getParamValue("reqId");						//需求ID
			String ver = request.getParamValue("ver");							//需求ID
			String remark = request.getParamValue("remark");					//审核描述
			String isAudit = request.getParamValue("isAudit");					//是否需要财务审核 add 2012-05-11 hxy
			String[] fjid = request.getParamValues("fjid");						//新增的附件id add 2012-05-11 hxy
			String[] expectedPeriod = request.getParamValues("expectedPeriod"); //预计交付周期
			SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
			String dealerId = logonUser.getDealerId();
			boolean verFlag = VerFlagDao.getVerFlag("TT_VS_SPECIAL_REQ", "REQ_ID", reqId, ver);
			if(verFlag){
				String[] detailId = detailIds.split(",");
				String[] batchNo = batchNos.split(",");
				//String[] expectedDate = expectedDates.split(",");
				TtVsSpecialReqPO tvsrpContion =  new TtVsSpecialReqPO();
				TtVsSpecialReqPO tvsrpValue =  new TtVsSpecialReqPO();
				tvsrpContion.setReqId(Long.parseLong(reqId));
				BigDecimal priceId = null;
				if("0".equals(flag)){
					tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_03);
				}
				if("1".equals(flag)){
					tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_05);
				}
				//TODO 2012-05-11 新增是否需要财务审核，如果是保持原来流程不变,如果否则将预扣款更新为0,对应单据状态改为“待需求者确认”
				if("0".equals(isAudit)) {
					tvsrpValue.setPreAmount(0D);
					tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_04);
					//获取销售价格
					Map<String, Object> result = dao.getSalePriceId(reqId, dealerId);
					if(result != null) {
						priceId = (BigDecimal)result.get("PRICE_ID");
					} 
					tvsrpValue.setPriceId(priceId != null ? priceId.longValue() : -1L);
				}
				tvsrpValue.setVer(Integer.parseInt(ver)+1);
				tvsrpValue.setUpdateBy(logonUser.getUserId());
				tvsrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(tvsrpContion, tvsrpValue);			//更新订做车需求表
				
				//添加附件 add 2012-05-14
				if (null != fjid && fjid.length>0) {
					for (int i = 0; i < fjid.length; i++) {
						if (null != fjid[i] && !"".equals(fjid[i])) {
							FsFileuploadPO tempFileuploadPO = new FsFileuploadPO();
							tempFileuploadPO.setFjid(Long.parseLong(fjid[i]));
							FsFileuploadPO valueFileuploadPO = new FsFileuploadPO();
							valueFileuploadPO.setYwzj(Long.parseLong(reqId));
							dao.update(tempFileuploadPO, valueFileuploadPO);
						}
					}
				}
				
				TtVsSpecialReqChkPO tvsrcp =  new TtVsSpecialReqChkPO();
				tvsrcp.setChkId(Long.parseLong(SequenceManager.getSequence("")));
				tvsrcp.setReqId(Long.parseLong(reqId));
				tvsrcp.setChkDesc(remark);
				tvsrcp.setChkDate(new Date(System.currentTimeMillis()));
				tvsrcp.setChkOrgId(logonUser.getOrgId());
				tvsrcp.setChkUserId(logonUser.getUserId());
				if("0".equals(flag)){
					tvsrcp.setChkStatus(Constant.SPECIAL_NEED_CHECK_STATUS_01);
				}
				if("1".equals(flag)){
					tvsrcp.setChkStatus(Constant.SPECIAL_NEED_CHECK_STATUS_02);
				}
				tvsrcp.setCreateBy(logonUser.getOrgId());
				tvsrcp.setCreateDate(new Date(System.currentTimeMillis()));
				dao.insert(tvsrcp);								//插入订做车需求审核表
				if("0".equals(flag)){
					//TODO 2012-05-11 新增是否需要财务审核，如果是保持原来流程不变,如果否则将配置单价更新
					if("1".equals(isAudit)) {
						for(int i=0; i<detailId.length; i++){
							if(!"".equals(batchNo[i].trim())&&batchNo[i].trim()!=null){
								TtVsSpecialReqDtlPO tvsrdpContion = new TtVsSpecialReqDtlPO();
								TtVsSpecialReqDtlPO tvsrdpValue = new TtVsSpecialReqDtlPO();
								tvsrdpContion.setDtlId(Long.parseLong(detailId[i]));
								if(!batchNo[i].equals("...")){
									tvsrdpValue.setSpecialBatchNo(batchNo[i]);
								}
								//tvsrdpValue.setExpectedDate(fmat.parse(expectedDate[i]));
								tvsrdpValue.setExpectedPeriod(Integer.parseInt(expectedPeriod[i].trim()));
								tvsrdpValue.setUpdateBy(logonUser.getUserId());
								tvsrdpValue.setUpdateDate(new Date(System.currentTimeMillis()));
								dao.update(tvsrdpContion,tvsrdpValue);	//更新订做车需求明细表
							}
						}
					} else if("0".equals(isAudit)) {
						for(int i=0; i<detailId.length; i++){
							if(!"".equals(batchNo[i].trim())&&batchNo[i].trim()!=null){
								//获取销售价格
								Map<String, Object> result = dao.getSalesPrice(detailId[i], dealerId, priceId.toString());
								BigDecimal salesPrice = null;
								if(result != null) {
									salesPrice = (BigDecimal) result.get("SALES_PRICE");
									if(salesPrice == null) {
										act.setOutData("returnValue", 3);
										throw new RuntimeException("价格未维护,请联系业务人员！");
									}
								} else {
									act.setOutData("returnValue", 3);
									throw new RuntimeException("价格未维护,请联系业务人员！");
								}
								
								TtVsSpecialReqDtlPO tvsrdpContion = new TtVsSpecialReqDtlPO();
								TtVsSpecialReqDtlPO tvsrdpValue = new TtVsSpecialReqDtlPO();
								tvsrdpContion.setDtlId(Long.parseLong(detailId[i]));
								
								if(!batchNo[i].equals("...")){
									tvsrdpValue.setSpecialBatchNo(batchNo[i]);
								}
								//tvsrdpValue.setExpectedDate(fmat.parse(expectedDate[i]));
								tvsrdpValue.setExpectedPeriod(Integer.parseInt(expectedPeriod[i].trim()));
								tvsrdpValue.setUpdateBy(logonUser.getUserId());
								tvsrdpValue.setSalesPrice(salesPrice.doubleValue());
								tvsrdpValue.setChangePrice(0D);
								tvsrdpValue.setUpdateDate(new Date(System.currentTimeMillis()));
								dao.update(tvsrdpContion,tvsrdpValue);	//更新订做车需求明细表
							}
						}
					}
				}
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"订做车需求产品明细审核保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 上级审核订做车需求产品明细审核保存
	 */
	public void specialNeedProductDetailCheckLever(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String detailIds = request.getParamValue("detailIds");				//需求明细ID
			String flag = request.getParamValue("flag");						//页面参数
			String reqId = request.getParamValue("reqId");						//需求ID
			String ver = request.getParamValue("ver");							//需求ID
			String remark = request.getParamValue("remark");					//审核描述
			SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
			boolean verFlag = VerFlagDao.getVerFlag("TT_VS_SPECIAL_REQ", "REQ_ID", reqId, ver);
			if(verFlag){
				TtVsSpecialReqPO seltvs =  new TtVsSpecialReqPO();
				TtVsSpecialReqPO tvsrpContion =  new TtVsSpecialReqPO();
				TtVsSpecialReqPO tvsrpValue =  new TtVsSpecialReqPO();
				tvsrpContion.setReqId(Long.parseLong(reqId));
				seltvs = (TtVsSpecialReqPO)dao.select(tvsrpContion).get(0) ;
				
				tvsrpValue.setUpdateBy(logonUser.getUserId());
				tvsrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
				if("0".equals(flag)){
					// 获得是否需要大客户审核开关
					String para = CommonDAO.getPara(Constant.SPECIAL_NEED_FLEET_CHECK_PARA.toString()) ;
					
					if("1".equals(para)) {
						if(seltvs.getFleetId() == null || seltvs.getFleetId() == 0) {
							tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_02);//待大客户部审核
						} else {
							tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_09);//待大客户部审核
						}
					} else {
						tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_02);
					}
				} else{
					tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_05);
				}
				dao.update(tvsrpContion, tvsrpValue);			//更新订做车需求表
				
				TtVsSpecialReqChkPO tvsrcp =  new TtVsSpecialReqChkPO();
				tvsrcp.setChkId(Long.parseLong(SequenceManager.getSequence("")));
				tvsrcp.setReqId(Long.parseLong(reqId));
				tvsrcp.setChkDesc(remark);
				tvsrcp.setChkDate(new Date(System.currentTimeMillis()));
				tvsrcp.setChkOrgId(logonUser.getOrgId());
				tvsrcp.setChkUserId(logonUser.getUserId());
				if("0".equals(flag)){
					tvsrcp.setChkStatus(Constant.SPECIAL_NEED_CHECK_STATUS_01);
				}
				if("1".equals(flag)){
					tvsrcp.setChkStatus(Constant.SPECIAL_NEED_CHECK_STATUS_02);
				}
				tvsrcp.setCreateBy(logonUser.getOrgId());
				tvsrcp.setCreateDate(new Date(System.currentTimeMillis()));
				dao.insert(tvsrcp);								//插入订做车需求审核表

				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"订做车需求产品明细审核保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
