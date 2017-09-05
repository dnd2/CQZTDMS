/**********************************************************************
* <pre>
* FILE : GeneralOrderCheck.java
* CLASS : GeneralOrderCheck
* AUTHOR : 
* FUNCTION : 订单审核
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-09|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TtVsOrderCheckPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsResourcePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * @Title: 常规订单审核Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-9
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
public class GeneralOrderCheck {
	
	public Logger logger = Logger.getLogger(GeneralOrderCheck.class);   
	OrderAuditDao dao  = OrderAuditDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/orderaudit/generalOrderTotalCheck.jsp";
	private final String totalUrl = "/jsp/sales/ordermanage/orderaudit/generalOrderTotalDlrCheck.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderaudit/generalOrderDetailDlrCheck.jsp";
	private final String detailAuditUrl = "/jsp/sales/ordermanage/orderaudit/generalOrderDetailAudit.jsp";
	/**
	 * 常规订单审核页面初始化
	 */
	public void generalOrderCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String flag = request.getParamValue("flag");					//页面参数
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);

			TmBusinessParaPO tpa = new TmBusinessParaPO(); //是否倒退周   YH 2011.5.23
			tpa.setParaId(50011003);
			tpa = (TmBusinessParaPO)dao.select(tpa).get(0);
		
			List<Map<String,Object>> dateList = dao.getGeneralDateList_New(oemCompanyId,tpa);
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("flag", flag);
			act.setOutData("tpa", tpa);
			act.setOutData("dateList", dateList);
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单审核查询---配置层面
	 */
	public void generalOrderCheckQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYearWeek = request.getParamValue("orderYearWeek"); //订单年周
			String[] array = orderYearWeek.split("-");
			String orderYear = array[0];
			String orderWeek = array[1];
			String areaId = request.getParamValue("areaId");			  //业务范围ID
			String area = request.getParamValue("area");				  //业务范围IDS

            String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));

			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGeneralOrderCheckList(orderYear, orderWeek, areaId, area, oemCompanyId,curPage,Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单审核查询---配置--经销商层面
	 */
	public void generalOrderTotalDlrQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYear = request.getParamValue("orderYear");			//订单年度
			String orderWeek = request.getParamValue("orderWeek");			//订单周度
			String areaId = request.getParamValue("areaId");				//业务范围ID
			String area = request.getParamValue("area");					//业务范围IDS
			String groupId = request.getParamValue("groupId");				//物料组ID
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGeneralOrderDlrCheckList(orderYear, orderWeek, areaId, area, groupId,oemCompanyId,curPage, Constant.PAGE_SIZE);
			List<Map<String, Object>> list = dao.getGeneralOrderGroupDetail(orderYear, orderWeek, areaId, area,groupId,oemCompanyId);
			act.setOutData("ps", ps);
			act.setOutData("list", list);
			act.setOutData("areaId", areaId);
			act.setOutData("area", area);
			act.setForword(totalUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单审核查询---物料--经销商层面
	 */
	public void generalOrderDetailDlrQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYear = request.getParamValue("orderYear");			//订单年度
			String orderWeek = request.getParamValue("orderWeek");			//订单周度
			String areaId = request.getParamValue("areaId");				//业务范围ID
			String area = request.getParamValue("area");					//业务范围IDS
			String groupId = request.getParamValue("groupId");				//物料组ID
			String dealerId= request.getParamValue("dealerId");				//经销商ID
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGeneralOrderDetailDlrList(orderYear, orderWeek, areaId, area, groupId, dealerId,companyId, curPage, Constant.PAGE_SIZE);
			List<Map<String, Object>> list1 = dao.getGeneralOrderGroupDetail(orderYear, orderWeek, areaId, area,groupId,companyId);
			List<Map<String, Object>> list2 = dao.getGeneralOrderDlrList(orderYear, orderWeek, areaId, area,groupId,dealerId,companyId);
			act.setOutData("ps", ps);
			act.setOutData("list1", list1);
			act.setOutData("list2", list2);
			act.setOutData("areaId", areaId);
			act.setOutData("area", area);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单审核---保存
	 */
	public void generalOrderCheck(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		try {
			String detailIds = request.getParamValue("detailIds");			//订单明细ID
			String checkAmounts = request.getParamValue("checkAmounts");	//审核数量
			String vers = request.getParamValue("vers");
			String materialIds = request.getParamValue("materialIds");
			String oldAmounts = request.getParamValue("oldAmounts");
			String orderYear = request.getParamValue("orderYear");
			String orderWeek = request.getParamValue("orderWeek");
			String [] detailId = detailIds.split(",");
			String [] ver = vers.split(",");
			String [] materialId = materialIds.split(",");
			String [] checkAmount = checkAmounts.split(",");
			String [] oldAmount = oldAmounts.split(",");
			boolean verFlag = true;
			for(int m=0; m<detailId.length; m++){
				boolean verFlagTmp = VerFlagDao.getVerFlag("TT_VS_ORDER_DETAIL", "DETAIL_ID", detailId[m], ver[m]);
				if(verFlagTmp==false){
					verFlag = false;
				}
			}
			if(verFlag){
				for(int i=0; i<detailId.length; i++){
					TtVsOrderDetailPO tvodpContion = new TtVsOrderDetailPO();
					TtVsOrderDetailPO tvodpValue = new TtVsOrderDetailPO();
					if(!"".equals(checkAmount[i])&&checkAmount[i]!=null&&Integer.parseInt(checkAmount[i])>=0){
						tvodpContion.setDetailId(Long.parseLong(detailId[i]));
						tvodpValue.setCheckAmount(Integer.parseInt(checkAmount[i]));
						tvodpValue.setUpdateBy(userId);
						tvodpValue.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.update(tvodpContion, tvodpValue);				   //更新订单明细表
					}
					/*Map<String, Object> map = dao.getResource(orderYear, orderWeek, materialId[i]);
					if(map != null){
						TtVsResourcePO tvrpContion = new TtVsResourcePO();
						TtVsResourcePO tvrpValue = new TtVsResourcePO();
						tvrpContion.setResourceId(Long.parseLong(map.get("RESOURCE_ID").toString()));
						tvrpValue.setCheckAmount(Integer.parseInt(map.get("CHECK_AMOUNT").toString())-Integer.parseInt(oldAmount[i])+Integer.parseInt(checkAmount[i]));
						tvrpValue.setResourceAmount(Integer.parseInt(map.get("RESOURCE_AMOUNT").toString())+Integer.parseInt(oldAmount[i])-Integer.parseInt(checkAmount[i]));
						tvrpValue.setUpdateBy(userId);
						tvrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.update(tvrpContion, tvrpValue);//更新资源表
					}*/
				}
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单审核完成---提交
	 */
	public void generalOrderCheckSubmit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYear = request.getParamValue("orderYear");			//订单年度
			String orderWeek = request.getParamValue("orderWeek");			//订单周度
			String areaId = request.getParamValue("areaId");				//业务范围ID
			String area = request.getParamValue("area");					//业务范围IDS
			List<Map<String, Object>> list = dao.getGeneralOrderIdList(orderYear, orderWeek, areaId, area);
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					TtVsOrderPO tvopContion = new TtVsOrderPO();
					TtVsOrderPO tvopValue = new TtVsOrderPO();
					TtVsOrderCheckPO tvocp = new TtVsOrderCheckPO();
					Map<String, Object> map = list.get(i);
					String orderId = map.get("ORDER_ID").toString() ;
					
					List<Map<String, Object>> dtlList = dao.getGeneralOrderDtlList(orderId) ;
					
					if(!CommonUtils.isNullList(dtlList)) {
						int length = dtlList.size() ;
						
						for(int j=0; j<length; j++) {
							Long dtlId = Long.parseLong(dtlList.get(j).get("DETAIL_ID").toString()) ;
							Long chkAmount = Long.parseLong(dtlList.get(j).get("CHECK_AMOUNT").toString()) ;
							Long orderAmount = Long.parseLong(dtlList.get(j).get("ORDER_AMOUNT").toString()) ;
							
							if(chkAmount == -1) {
								dao.updateOrderDtl(dtlId, orderAmount, new Date(System.currentTimeMillis()), logonUser.getUserId()) ;
							}
						}
					}
					
					
					tvopContion.setOrderId(Long.parseLong(orderId));
					tvopValue.setOrderStatus(Constant.ORDER_STATUS_05);
					tvopValue.setVer(Integer.parseInt(map.get("VER").toString())+1);
					tvopValue.setUpdateBy(logonUser.getUserId());
					tvopValue.setUpdateDate(new Date(System.currentTimeMillis()));
					dao.update(tvopContion, tvopValue);						//更新订单表
					
					tvocp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
					tvocp.setOrderId(Long.parseLong(map.get("ORDER_ID").toString()));
					tvocp.setCheckStatus(Constant.CHECK_STATUS_03);
					tvocp.setCheckOrgId(logonUser.getOrgId());
					tvocp.setCheckUserId(logonUser.getUserId());
					tvocp.setCheckDate(new Date(System.currentTimeMillis()));
					tvocp.setCreateDate(new Date(System.currentTimeMillis()));
					tvocp.setCreateBy(logonUser.getUserId());
					dao.insert(tvocp);									 	//插入订单审核表
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单审核逐单审核---查询
	 */
	public void generalOrderDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYear = request.getParamValue("orderYear");			//订单年度
			String orderWeek = request.getParamValue("orderYearWeek");			//订单周度
			orderWeek = orderWeek.substring(orderWeek.indexOf("-")+1);
			String areaId = request.getParamValue("areaId");				//业务范围ID
			String area = request.getParamValue("area");					//业务范围IDS
			String dealerCode= request.getParamValue("dealerCode");
			String orderNo = request.getParamValue("orderNo");

            String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));


			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGeneralOrderDetailList(orderYear, orderWeek, areaId, area,dealerCode,orderNo,oemCompanyId,curPage,Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单审核逐单审核---调整页面
	 */
	public void generalOrderDetailAuditInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");
			Map<String, Object> map = dao.getGeneralOrderInfo(orderId);
			List<Map<String, Object>> list = dao.getGeneralOrderDetailQuery(orderId, logonUser.getCompanyId().toString());
			act.setOutData("list", list);
			act.setOutData("map", map);
			act.setForword(detailAuditUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单逐单审核---保存
	 */
	public void generalOrderDetailCheck(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		try {
			String detailIds = request.getParamValue("detailIds");			//订单明细ID
			String oldAmounts = request.getParamValue("oldAmounts");		//原审核数量
			String vers = request.getParamValue("vers");
			String materialIds = request.getParamValue("materialIds");
			String checkAmounts = request.getParamValue("strAmounts");
			String orderYear = request.getParamValue("orderYear");
			String orderWeek = request.getParamValue("orderWeek");
			String [] detailId = detailIds.split(",");
			String [] ver = vers.split(",");
			String [] materialId = materialIds.split(",");
			String [] checkAmount = checkAmounts.split(",");
			String [] oldAmount = oldAmounts.split(",");
			boolean verFlag = true;
			for(int m=0; m<detailId.length; m++){
				boolean verFlagTmp = VerFlagDao.getVerFlag("TT_VS_ORDER_DETAIL", "DETAIL_ID", detailId[m], ver[m]);
				if(verFlagTmp==false){
					verFlag = false;
				}
			}
			if(verFlag){
				for(int i=0; i<detailId.length; i++){
					TtVsOrderDetailPO tvodpContion = new TtVsOrderDetailPO();
					TtVsOrderDetailPO tvodpValue = new TtVsOrderDetailPO();
					if(!"".equals(checkAmount[i])&&checkAmount[i]!=null){
						tvodpContion.setDetailId(Long.parseLong(detailId[i]));
						tvodpValue.setCheckAmount(Integer.parseInt(checkAmount[i]));
						tvodpValue.setUpdateBy(userId);
						tvodpValue.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.update(tvodpContion, tvodpValue);				   //更新订单明细表
					}
					/*Map<String, Object> map = dao.getResource(orderYear, orderWeek, materialId[i]);
					TtVsResourcePO tvrpContion = new TtVsResourcePO();
					TtVsResourcePO tvrpValue = new TtVsResourcePO();
					tvrpContion.setResourceId(Long.parseLong(map.get("RESOURCE_ID").toString()));
					tvrpValue.setCheckAmount(Integer.parseInt(map.get("CHECK_AMOUNT").toString())-Integer.parseInt(oldAmount[i])+Integer.parseInt(checkAmount[i]));
					tvrpValue.setResourceAmount(Integer.parseInt(map.get("RESOURCE_AMOUNT").toString())+Integer.parseInt(oldAmount[i])-Integer.parseInt(checkAmount[i]));
					tvrpValue.setUpdateBy(userId);
					tvrpValue.setUpdateDate(new Date(System.currentTimeMillis()));
					dao.update(tvrpContion, tvrpValue);//更新资源表
*/				}
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 常规订单逐单审核---提交
	 */
	public void generalOrderDetailCheckSubmit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");					//业务范围IDS
			String ver = request.getParamValue("ver");
			boolean verFlagTmp = VerFlagDao.getVerFlag("TT_VS_ORDER", "ORDER_ID", orderId, ver);
			if(verFlagTmp){
				TtVsOrderPO tvopContion = new TtVsOrderPO();
				TtVsOrderPO tvopValue = new TtVsOrderPO();
				TtVsOrderCheckPO tvocp = new TtVsOrderCheckPO();
				tvopContion.setOrderId(Long.parseLong(orderId));
				tvopValue.setOrderStatus(Constant.ORDER_STATUS_05);
				tvopValue.setVer(Integer.parseInt(ver)+1);
				tvopValue.setUpdateBy(logonUser.getUserId());
				tvopValue.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(tvopContion, tvopValue);						//更新订单表
				
				//ADD 更新订单明细表  2012-01-12 HXY
				TtVsOrderDetailPO tvodp = new TtVsOrderDetailPO();
				tvodp.setOrderId(Long.parseLong(orderId));
				List orderDetailList = dao.select(tvodp);
				//如遇调整数量全为0则将审核数量变成提报数量,否则不作更新
				if(orderDetailList.size() > 0) {
					boolean flag = true;
					for(int i=0; i<orderDetailList.size(); i++) {
						int check_amount = ((TtVsOrderDetailPO)orderDetailList.get(i)).getCheckAmount();
						if(check_amount != 0) {
							flag = false;
							break;
						}
					}
					if(flag) {
						for(int i=0; i<orderDetailList.size(); i++) {
							TtVsOrderDetailPO tvodpValue = new TtVsOrderDetailPO();
							tvodp = (TtVsOrderDetailPO) orderDetailList.get(i);
							TtVsOrderDetailPO tvodpCondition = new TtVsOrderDetailPO();
							tvodpCondition.setOrderId(tvodp.getOrderId());
							tvodpCondition.setDetailId(tvodp.getDetailId());
							tvodpCondition.setMaterialId(tvodp.getMaterialId());
							tvodpValue.setCheckAmount(tvodp.getOrderAmount());
							/*tvodpValue.setOrderAmount(order_amount - check_amount);*/
							dao.update(tvodpCondition, tvodpValue);
						} 
					}
				}
				
				tvocp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
				tvocp.setOrderId(Long.parseLong(orderId));
				tvocp.setCheckStatus(Constant.CHECK_STATUS_03);
				tvocp.setCheckOrgId(logonUser.getOrgId());
				tvocp.setCheckUserId(logonUser.getUserId());
				tvocp.setCheckDate(new Date(System.currentTimeMillis()));
				tvocp.setCreateDate(new Date(System.currentTimeMillis()));
				tvocp.setCreateBy(logonUser.getUserId());
				dao.insert(tvocp);									 	//插入订单审核表
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单逐单审核---驳回
	 */
	public void generalOrderDetailRejectSubmit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");					//业务范围IDS
			String ver = request.getParamValue("ver");
			boolean verFlagTmp = VerFlagDao.getVerFlag("TT_VS_ORDER", "ORDER_ID", orderId, ver);
			if(verFlagTmp){
				TtVsOrderPO tvopContion = new TtVsOrderPO();
				TtVsOrderPO tvopValue = new TtVsOrderPO();
				TtVsOrderCheckPO tvocp = new TtVsOrderCheckPO();
				tvopContion.setOrderId(Long.parseLong(orderId));
				tvopValue.setOrderStatus(Constant.ORDER_STATUS_06);
				tvopValue.setVer(Integer.parseInt(ver)+1);
				tvopValue.setUpdateBy(logonUser.getUserId());
				tvopValue.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(tvopContion, tvopValue);						//更新订单表
				
				/*//ADD 更新订单明细表  2012-01-12 HXY
				TtVsOrderDetailPO tvodp = new TtVsOrderDetailPO();
				tvodp.setOrderId(Long.parseLong(orderId));
				List orderDetailList = dao.select(tvodp);
				if(orderDetailList.size() > 0) {
					for(int i=0; i<orderDetailList.size(); i++) {
						TtVsOrderDetailPO tvodpValue = new TtVsOrderDetailPO();
						tvodp = (TtVsOrderDetailPO) orderDetailList.get(i);
						TtVsOrderDetailPO tvodpCondition = new TtVsOrderDetailPO();
						tvodpCondition.setOrderId(tvodp.getOrderId());
						tvodpCondition.setDetailId(tvodp.getDetailId());
						tvodpCondition.setMaterialId(tvodp.getMaterialId());
						tvodpValue.setCheckAmount(tvodp.getOrderAmount());
						dao.update(tvodpCondition, tvodpValue);
					}
				}*/
				
				tvocp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
				tvocp.setOrderId(Long.parseLong(orderId));
				tvocp.setCheckStatus(Constant.CHECK_STATUS_03);
				tvocp.setCheckOrgId(logonUser.getOrgId());
				tvocp.setCheckUserId(logonUser.getUserId());
				tvocp.setCheckDate(new Date(System.currentTimeMillis()));
				tvocp.setCreateDate(new Date(System.currentTimeMillis()));
				tvocp.setCreateBy(logonUser.getUserId());
				dao.insert(tvocp);									 	//插入订单审核表
				act.setOutData("returnValue", 3);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
