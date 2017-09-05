/**********************************************************************
* <pre>
* FILE : SpecialNeedPriceCheck.java
* CLASS : SpecialNeedPriceCheck
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedDao;
import com.infodms.dms.dao.sales.storageManage.VehicleDispatchDAO;
import com.infodms.dms.exception.BizException;
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
 * @Title: 订做车需求价格核定Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-17
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
public class SpecialNeedPriceCheck {

	public Logger logger = Logger.getLogger(SpecialNeedPriceCheck.class);   
	SpecialNeedDao dao  = SpecialNeedDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/orderaudit/specialNeedPriceCheckQuery.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderaudit/specialNeedPriceDetailCheck.jsp";
	/**
	 * 订做车需求价格核定页面初始化
	 */
	public void specialNeedPriceCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求价格核定页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获得价格类型列表
	 */
	public void getPriceList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String str = CommonUtils.checkNull(request
					.getParamValue("str"));
			String companyId = GetOemcompanyId.getOemCompanyId(logonUser)
					.toString();
			String dealerId = str.split(",")[0] ;
			String reqId = str.split(",")[1] ;
			map.put("dealerId", dealerId);
			map.put("companyId", companyId);
			
			getPriceId(reqId) ;

			List<Map<String, Object>> priceList = OrderReportDao.getInstance().getPriceList(map);
			
			act.setOutData("priceList", priceList);
			act.setOutData("reqId", reqId) ;

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获得priceId
	 */
	public void getPriceId(String reqId) {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			List<Map<String, Object>> priceIdA = SpecialNeedDao.getInstance().getPriceId(reqId);
			String priceId = "" ;
			if(null != priceIdA && priceIdA.size() > 0) {
				priceId = priceIdA.get(0).get("PRICE_ID").toString() ;
			}
			act.setOutData("priceId", priceId);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "补充订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求价格核定查询
	 */
	public void specialNeedPriceCheckQuery(){
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
			
			PageResult<Map<String, Object>> ps = dao.getSpecialNeedPriceCheckList(orgId, areaId, area, dealerCode, startDate, endDate, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订做车需求价格核定查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订做车需求价格核定明细查询
	 */
	public void specialNeedPriceDetailCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");				//需求ID
			String dealerId = request.getParamValue("dealerId");		//经销商ID
			String parentDlrId = null ;
			
			if(dao.viewDealerLever(dealerId)) {
				parentDlrId = VehicleDispatchDAO.getParentDealerId(dealerId).get("PARENT_DEALER_D").toString() ;
			} 
			
			TtVsSpecialReqPO tvsrpContion = new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue = new TtVsSpecialReqPO();
			/*TtVsSpecialReqChkPO tvsrc = new TtVsSpecialReqChkPO() ;
			tvsrc.setReqId(Long.parseLong(reqId)) ;
			List<PO> datalist2 = dao.select(tvsrc);
			if(datalist2!=null&&datalist2.size()>0){
				tvsrc =(TtVsSpecialReqChkPO)datalist2.get(0);
			}
			String remark2 = tvsrc.getChkDesc() ;*/
			tvsrpContion.setReqId(Long.parseLong(reqId));
			List<PO> datalist = dao.select(tvsrpContion);
			if(datalist!=null&&datalist.size()>0){
				tvsrpValue =(TtVsSpecialReqPO)datalist.get(0);
			}
			String remark = tvsrpValue.getRefitDesc();
			
			
			List<Map<String, Object>> list = dao.getSpecialNeedDetailPriceCheckList(reqId ,dealerId);
			
			Map<String, Object> map = new HashMap<String, Object>();
			String companyId = GetOemcompanyId.getOemCompanyId(logonUser).toString();
			map.put("dealerId", dealerId);
			map.put("companyId", companyId);
			getPriceId(reqId) ;
			List<Map<String, Object>> priceList = OrderReportDao.getInstance().getPriceList(map);
			List<Map<String, Object>> attachList = dao.getAttachInfos(reqId);
			if(null!=attachList&&attachList.size()!=0){
				act.setOutData("attachList", attachList);
			}

			List<Map<String, Object>> checkList = dao.getSpecialNeedCheck(reqId);
			
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
			
			ProductManageDao productDao = new ProductManageDao();
			List<Map<String, Object>> types = productDao.getmyPriceTypeList(logonUser.getCompanyId(),dealerId);
				Map mymap=productDao.getMyMap(logonUser.getCompanyId(), dealerId);
				types.add(mymap);
				act.setOutData("types", types);
			
			if(CommonUtils.isNullString(parentDlrId)) {
				parentDlrId = dealerId ;
			}	
			
			act.setOutData("parentDlrId", parentDlrId) ;
			act.setOutData("ifTypeYes", Constant.IF_TYPE_YES);
			act.setOutData("MaxMoney", Constant.MATERIAL_PRICE_MAX);
			//act.setOutData("priceList", priceList);
			act.setOutData("priceList", types);
			act.setOutData("reqId", reqId) ;
			act.setOutData("dealerId", dealerId) ;
			//act.setOutData("remark2", remark2) ;
			act.setOutData("list", list);
			act.setOutData("reqId", reqId);
			act.setOutData("remark", remark);
			act.setOutData("checkList", checkList);
			act.setOutData("fleetName", fleetName);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求价格核定明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订做车需求价格明细核定保存
	 */
	public void specialNeedPriceDetailCheck(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String detailIds = request.getParamValue("detailIds");			//需求明细ID
			String prices = request.getParamValue("prices");				//价格变动
			String salePrices = request.getParamValue("salePrices");	    //标准价格
			String flag = request.getParamValue("flag");					//页面参数
			String reqId = request.getParamValue("reqId");					//需求ID
			String remark = request.getParamValue("remark");				//审核描述
			String preAmount = request.getParamValue("preAmount");			//预付款金额
			String fundType = request.getParamValue("fundType");			//资金类型
			String[] detailId = detailIds.split(",");
			String[] price = prices.split(",");
			String[] salePrice = salePrices.split(",");
			String priceId = request.getParamValue("priceId") ;				//获取价格类型
			//Double PreAmountP = SpecialNeedDao.getInstance().getPreAmountP() ; //预扣百分比
			//Double preAmountA = Double.parseDouble(preAmount) * PreAmountP;
			TtVsSpecialReqPO tvsrpContion =  new TtVsSpecialReqPO();
			TtVsSpecialReqPO tvsrpValue =  new TtVsSpecialReqPO();
			tvsrpContion.setReqId(Long.parseLong(reqId));
			if("0".equals(flag)){
				tvsrpValue.setAccountTypeId(new Long(fundType));
				tvsrpValue.setPriceId(Long.parseLong(priceId)) ;
				tvsrpValue.setPreAmount(new Double(preAmount)) ;
				tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_04);
			}
			if("1".equals(flag)){
				tvsrpValue.setReqStatus(Constant.SPECIAL_NEED_STATUS_05);
			}
			tvsrpValue.setUpdateBy(logonUser.getUserId());
			tvsrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
			dao.update(tvsrpContion, tvsrpValue);			//更新订做车需求表
			
			TtVsSpecialReqChkPO tvsrcp =  new TtVsSpecialReqChkPO();
			tvsrcp.setChkId(Long.parseLong(SequenceManager.getSequence("")));
			tvsrcp.setReqId(Long.parseLong(reqId));
			tvsrcp.setChkDesc(remark);
			tvsrcp.setChkDate(new Date(System.currentTimeMillis()));
			tvsrcp.setChkOrgId(logonUser.getOrgId());
			tvsrcp.setChkUserId(logonUser.getUserId());
			if("0".equals(flag)){
				tvsrcp.setChkStatus(Constant.SPECIAL_NEED_CHECK_STATUS_03);
			}
			if("1".equals(flag)){
				tvsrcp.setChkStatus(Constant.SPECIAL_NEED_CHECK_STATUS_04);
			}
			tvsrcp.setCreateBy(logonUser.getOrgId());
			tvsrcp.setCreateDate(new Date(System.currentTimeMillis()));
			dao.insert(tvsrcp);								//插入订做车需求审核表
			if("0".equals(flag)){
				for(int i=0; i<detailId.length; i++){
					if(!"".equals(price[i].trim())&&price[i].trim()!=null){
						TtVsSpecialReqDtlPO tvsrdpContion = new TtVsSpecialReqDtlPO();
						TtVsSpecialReqDtlPO tvsrdpValue = new TtVsSpecialReqDtlPO();
						tvsrdpContion.setDtlId(Long.parseLong(detailId[i]));
						tvsrdpValue.setSalesPrice(Double.parseDouble(salePrice[i])+Double.parseDouble(price[i]));
						tvsrdpValue.setChangePrice(Double.parseDouble(price[i]));
						tvsrdpValue.setUpdateBy(logonUser.getUserId());
						tvsrdpValue.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.update(tvsrdpContion,tvsrdpValue);	//更新订做车需求明细表
					}
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"订做车需求价格核定保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订做车需求价格核定明细查询,价格类型改变时，调用此方法
	 */
	public void specialNeedPriceDetailCheckInitA(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String str = request.getParamValue("str") ;
			String reqId = str.split(",")[0] ;				//需求ID
			String priceId = str.split(",")[1];		//经销商ID
			String dtlId = str.split(",")[2] ;	//明细ID
			
			TtVsSpecialReqPO condition = new TtVsSpecialReqPO();
			TtVsSpecialReqPO result = null;
			condition.setReqId(Long.parseLong(reqId));
			List<PO> specialReqs = dao.select(condition);
			if(specialReqs != null && specialReqs.size() != 0) {
				result = (TtVsSpecialReqPO) specialReqs.get(0);
			}
			
			Map<String, Object> firstLevelDealer = DealerRelationDAO.getInstance().getFirstDlr(String.valueOf(result.getDealerId()));
			BigDecimal firstLevelDealerId = (BigDecimal) firstLevelDealer.get("DEALER_ID");
			Map<String, Object> priceList = dao.getSpecialNeedDetailPriceList(firstLevelDealerId.toString());
			BigDecimal price_id = BigDecimal.ZERO;
			if(priceList != null && priceList.size() != 0) {
				price_id = (BigDecimal) priceList.get("PRICE_ID");
			}
			List<Map<String, Object>> listA = dao.getSpecialNeedDetailPriceCheckListA(reqId ,price_id.toString(), dtlId);
			String priceA = listA.get(0).get("PRICE").toString() ;
			String delId = listA.get(0).get("DTL_ID").toString() ;
			act.setOutData("priceA", priceA);
			act.setOutData("delId", delId);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"订做车需求价格核定明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
