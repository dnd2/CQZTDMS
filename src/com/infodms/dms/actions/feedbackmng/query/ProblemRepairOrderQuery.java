package com.infodms.dms.actions.feedbackmng.query;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsRepairOrderExtBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.feedbackMng.RepairOrderProblemDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: RepairOrderQuery 
* @Description: TODO(上报工单--问题工单--经销商端查询) 
* @author yh 
* @date 2011.4.15 
*
 */
public class ProblemRepairOrderQuery {

	public Logger logger = Logger.getLogger(ServiceCarApplyQuery.class);
	private RepairOrderProblemDAO dao = RepairOrderProblemDAO.getInstance();
	private final ClaimAuditingDao daom = ClaimAuditingDao.getInstance();
	private final ClaimBillMaintainDAO daomb = ClaimBillMaintainDAO.getInstance();
	private final String ProblemRepairOrderURL = "/jsp/feedbackMng/query/ProblemRepairOrderQuery.jsp";// 查询页面
	private final String ProblemRoDetailURL = "/jsp/feedbackMng/query/ProblemRepairOrderDetail.jsp";
	private final String PRODetailForAddURL = "/jsp/feedbackMng/query/PRODetailForAddNoPartItems.jsp";
	
	/**
	 * 
	* @Title: ProblemRepairOrderQueryForward 
	* @Description: TODO(查询跳转页面) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void ProblemRepairOrderQueryForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(ProblemRepairOrderURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "上报问题工单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 
	* @Title: applyQuery 
	* @Description: TODO(查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void applyQuery() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			StringBuffer con = new StringBuffer();
			String RoNo = request.getParamValue("RoNo");
			String strDate = request.getParamValue("CON_APPLY_DATE_START");
			String endDate = request.getParamValue("CON_APPLY_DATE_END");
			String modelId = request.getParamValue("MODEL_ID");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String vin = request.getParamValue("VIN");
			// 工单号like
			if (RoNo != null && !"".equals(RoNo)) {
				con.append(" and tarop.RO_NO like'%" + RoNo + "%' "); //
			}
			// 介于开始时间
			if (strDate != null && !"".equals(strDate)) {
				con.append(" and tarop.DELIVERY_DATE >= to_date('" + strDate +" 00:00:00"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			if(vin!=null && !"".equals(vin)){
				con.append(" and tarop.vin like '%" + vin + "%' "); //
			}
			// 结束时间
			if (endDate != null && !"".equals(endDate)) {
				con.append(" and tarop.DELIVERY_DATE <= to_date('" + endDate + " 23:59:59"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}	
			// 车型
			if (modelId != null && !"".equals(modelId)) {
				con.append(" and tvmg.GROUP_ID='" + modelId + "' ");
			}
			// 维修类型
			if (repairType != null && !"".equals(repairType)) {
				con.append(" and tarop.REPAIR_TYPE_CODE ='" + repairType + "' ");
			}
			// 特定经销商
			if (dealerCode != null && !"".equals(dealerCode)) {
				con.append(" and tarop.dealer_code='" + dealerCode + "' ");
			}
			//添加是否全自费查询条件 start 0表示不是全自费的工单 add by tanv 2013-01-30
			String payFlag = request.getParamValue("isSelfPayAll");
			if("1".equals(payFlag)){
				con.append(" and nvl(tarop.is_self_pay_all,'0')='11801001' ");
			}else if("0".equals(payFlag)){
				con.append(" and nvl(tarop.is_self_pay_all,'0')<>'11801001' ");
			}
			//添加是否全自费查询条件 end
			PageResult<Map<String, Object>> ps = dao.applyQuery(con.toString(),curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "上报问题工单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 
	* @Title: roModifyForward 
	* @Description: TODO(问题工单明细) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void queryDetail() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String phone = "";
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			List resList = daom.queryDealerById(Utility.getLong(logonUser
					.getDealerId()));
			//String phone="";
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if(dealerPO.getPhone()!=null){
					phone = dealerPO.getPhone();	
				}
			}
			TtAsRepairOrderExtBean tawep = daomb.queryRoById(id);
			String vin = tawep.getVin();
			String roNo = tawep.getRoNo();
			List<Map<String, Object>> list = daomb.getVinUserName(vin);
			List<Map<String, Object>> list1 = daomb.getGuranteeCode(vin);
			List<TtAsRoRepairPartPO> partls = daomb.queryRepairPart(null,id); // 取配件信息
			List<TtAsRoLabourPO> itemls = daomb.queryRepairitem(null,id); // 取工时
			List<TtAsRoAddItemPO> otherls = daomb.queryAddItem(null,id);// 取其他项目
			act.setOutData("application", tawep);
			TmVehiclePO tvp = new TmVehiclePO();
			tvp.setVin(tawep.getVin());
			
			/*****add by liuxh 20131108判断车架号不能为空*****/
			CommonUtils.jugeVinNull(tawep.getVin());
			/*****add by liuxh 20131108判断车架号不能为空*****/
			
			List<TmVehiclePO> tvps = dao.select(tvp);
			if(tvps.size()>0){
				tvp = tvps.get(0);
			}
			act.setOutData("tvp", tvp); //根据VIN带出车辆信息表中的车 YH
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("phone", phone);
			act.setOutData("list", list);
			act.setOutData("list1", list1);
			act.setForword(ProblemRoDetailURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "问题工单明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void delProblemRo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			//问题工单转历史 添加操作日志 add by tanv 2013-01-10
			String hisFlag = dao.ropToHis(id,logonUser.getUserId());
			int flag = dao.delProblemRo(id);
			act.setOutData("flag", flag);
		}catch(Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "问题工单废弃");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: roModifyForward 
	* @Description: TODO(可以添加无零件项目的问题工单明细) 
	* @param     add by tanv 2012-12-26 
	* @return void    返回类型 
	* @throws
	 */
	public void queryDetailForAddNPItem() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String phone = "";
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			List resList = daom.queryDealerById(Utility.getLong(logonUser
					.getDealerId()));
			//String phone="";
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if(dealerPO.getPhone()!=null){
					phone = dealerPO.getPhone();	
				}
			}
			TtAsRepairOrderExtBean tawep = daomb.queryRoById(id);
			String vin = tawep.getVin();
			String roNo = tawep.getRoNo();
			List<Map<String, Object>> list = daomb.getVinUserName(vin);
			List<Map<String, Object>> list1 = daomb.getGuranteeCode(vin);
			List<TtAsRoRepairPartPO> partls = daomb.queryRepairPart(null,id); // 取配件信息
			List<TtAsRoLabourPO> itemls = daomb.queryRepairitem(null,id); // 取工时
			List<TtAsRoAddItemPO> otherls = daomb.queryAddItem(null,id);// 取其他项目
			act.setOutData("application", tawep);
			TmVehiclePO tvp = new TmVehiclePO();
			tvp.setVin(tawep.getVin());
			
			/*****add by liuxh 20131108判断车架号不能为空*****/
			CommonUtils.jugeVinNull(tawep.getVin());
			/*****add by liuxh 20131108判断车架号不能为空*****/
			
			List<TmVehiclePO> tvps = dao.select(tvp);
			if(tvps.size()>0){
				tvp = tvps.get(0);
			}
			act.setOutData("tvp", tvp); //根据VIN带出车辆信息表中的车 YH
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("phone", phone);
			act.setOutData("list", list);
			act.setOutData("list1", list1);
			act.setForword(PRODetailForAddURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "问题工单明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*add by tanv 2012-12-26添加无零件项
	 * */
	public void addNoPartItems(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String proId = request.getParamValue("proId");
			String noPartItems = request.getParamValue("noPartItems");
			String purchaseDate = request.getParamValue("purchaseDateMy");
			String vin = request.getParamValue("vinMy");
			String inMileage = request.getParamValue("inMileageMy");
			int is_gur = daomb.partIsGua(purchaseDate,inMileage,vin,"00-000");
			String flag = daomb.addNoPartItems(proId,noPartItems,is_gur,logonUser.getUserId(),vin,inMileage);
			act.setOutData("flag", flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/*
	 * 批量删除问题工单
	 * add by tanv 2013-02-19
	 * */
	public void delProblemRoBatch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String ids = request.getParamValue("idStr");
			int flag = 1;
			if(!"".equals(ids) && ids!=null){
				String[] idas = ids.split(",");
				for(int i=0;i<idas.length;i++){
					//问题工单转历史 添加操作日志 add by tanv 2013-01-10
					String hisFlag = dao.ropToHis(idas[i],logonUser.getUserId());
					flag = dao.delProblemRo(idas[i]);
				}
			}
			act.setOutData("flag", flag);
		}catch(Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "问题工单废弃");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
