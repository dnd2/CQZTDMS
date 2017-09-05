package com.infodms.dms.actions.sales.fleetmanage.fleetSupport;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetInfoAppDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetsupport.FleetSupportDao;
import com.infodms.dms.dao.sales.marketmanage.planissued.ActivitiesPlanIssuedDao;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtFleetIntentDetailPO;
import com.infodms.dms.po.TtFleetIntentPO;
import com.infodms.dms.po.TtFleetSupportInfoPO;
import com.infodms.dms.po.TtFleetSupportPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * @Title: 集团客户支持申请Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-22
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
public class FleetSupportApply {
	
	public Logger logger = Logger.getLogger(FleetSupportApply.class);   
	FleetSupportDao dao  = FleetSupportDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportApply.jsp";
	private final String detailUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportApplyDetail.jsp";
	private final String fleetInfo = "/jsp/sales/fleetmanage/fleetSupport/fleetInfoDetail.jsp";
	private final String pactInfo = "/jsp/sales/fleetmanage/fleetSupport/pactInfoDetail.jsp";
	private final String printInitUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportPrintInit.jsp";
	private final String printDetailUrl = "/jsp/sales/fleetmanage/fleetSupport/fleetSupportPrint.jsp";
	/**
	 * 集团客户支持申请页面初始化
	 */
	public void fleetSupportApplyInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持申请查询
	 */
	public void fleetSupportApplyQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dlrCompanyId = logonUser.getCompanyId().toString();
			String fleetName = request.getParamValue("fleetName");	//客户名称
			String fleetType = request.getParamValue("fleetType");	//客户类型
			String startDate = request.getParamValue("startDate");	//起始时间
			String endDate = request.getParamValue("endDate");		//结束时间
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetSupportApplyQuery(fleetName, fleetType, startDate, endDate, dlrCompanyId, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户信息明细
	 */
	public void fleetInfoDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户ID
			String intendId=null;
			Map<String, Object> map1 = dao.getFleetInfobyId(fleetId);
			FleetInfoAppDao appDao=new FleetInfoAppDao();
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			act.setOutData("tfrdMapList", tfrdMapList);
			act.setOutData("fleetMap", map1);
			List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intendId);;
			act.setOutData("checkList", list2);

			act.setForword(fleetInfo);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 批售项目明细
	 */
	public void fleetInfoDetailQuery1(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户ID
			Map<String, Object> map1 = dao.getCompanyPactInfoById(fleetId);
			act.setOutData("fleetMap", map1);
			act.setForword(pactInfo);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"批售项目明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户支持申请明细查询
	 */
	public void fleetSupportApplyDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户ID
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			//Map<String, Object> map2 = dao.getFleetIntentInfobyId(fleetId);
//			String intentId="";
//			if(!"".equals(map2)&&map2!=null){
//				intentId= map2.get("INTENT_ID").toString();
//				List<Map<String, Object>> list1 = dao.getFleetIntentDetailInfobyId(fleetId, intentId,"");
//				List<Map<String, Object>> list2 = dao.getFleetIntentCheckInfobyId(fleetId, intentId);
//				act.setOutData("intentList", list1);
//				act.setOutData("checkList", list2);
//			}
//			act.setOutData("intentMap", map2);
//			act.setOutData("intentId", intentId);
			// 根据fleetId查询上传附件
			ActivitiesPlanIssuedDao dao = ActivitiesPlanIssuedDao.getInstance();
			List<Map<String, Object>> attachList = dao.getAttachInfos(fleetId);
			if (null != attachList && attachList.size() != 0) {
				act.setOutData("attachList", attachList);
			}
			act.setOutData("fleetMap", map);
			//根据集团客户主表的id查询子表需求说明中的内容
			FleetInfoAppDao appDao=new FleetInfoAppDao();
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			//根据集团客户主表的id查询子表商务支持中的内容
			List<Map<String,Object>> supportInfoList=appDao.getSupportInfoByFleetId(fleetId);
			act.setOutData("supportInfoList", supportInfoList);
			act.setOutData("tfrdMapList", tfrdMapList);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 页面物料组树查询
	 */
	public void materialGroupQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long poseId=logonUser.getPoseId();
		try{
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			List<TmVhclMaterialGroupPO> list=dao.getMaterialGroupTree(poseId,groupLevel);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"页面物料组树查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 物料组树查询结果
	 */
	public void groupListQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long poseId=logonUser.getPoseId();
		try{
			String groupIds = CommonUtils.checkNull(request.getParamValue("GROUPIDS"));	//not in GROUPIDS
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=dao.getGroupList(poseId,groupIds,groupId,groupCode,groupName,groupLevel,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("GROUPIDS", groupIds);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"页面物料组树查询结果");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持申请意向录入保存
	 */
	public void fleetSupportApplySave(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String supportRemark= CommonUtils.checkNull(request.getParamValue("supportRemark"));//商务支持信息备注
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
			//String originalPrices = CommonUtils.checkNull(request.getParamValue("originalPrices"));	//零售价格
			//String discounts = CommonUtils.checkNull(request.getParamValue("discounts"));			//支持点位
			//String discountPrices = CommonUtils.checkNull(request.getParamValue("discountPrices"));	//支持金额
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));					//客户ID
//			String intentId = CommonUtils.checkNull(request.getParamValue("intentId"));				//意向ID
//			String purchaseDate = CommonUtils.checkNull(request.getParamValue("purchaseDate"));		//预计采购日期
//			String purendDate = CommonUtils.checkNull(request.getParamValue("purendDate"));			//预计采购结束日期
//			String infoGivingMan = CommonUtils.checkNull(request.getParamValue("infoGivingMan"));		//信息提供人员
//			String competeRemark = CommonUtils.checkNull(request.getParamValue("competeRemark"));		//竞争情况说明
//			String infoRemark = CommonUtils.checkNull(request.getParamValue("infoRemark"));			//信息情况说明
//			SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
			//向tt_fleet_support表中加入supportRemark的值
			TtFleetSupportPO tfpo=new TtFleetSupportPO() ;
			TtFleetSupportPO tfpo1=new TtFleetSupportPO();
			
			tfpo.setFleetId(new Long(fleetId));
			List<PO> tfsList=dao.select(tfpo);
			if(tfsList.size()>0){
				tfpo1.setSupportRemark(supportRemark);
				tfpo1.setUpdateBy(new BigDecimal(logonUser.getUserId()));
				tfpo1.setUpdateDate(new java.util.Date());
				tfpo1.setSupportDate(new java.util.Date());
				dao.update(tfpo,tfpo1);
			}else{
				tfpo1.setSupportId(new Long(SequenceManager.getSequence("")));
				tfpo1.setFleetId(new Long(fleetId));
				tfpo1.setDealerId(logonUser.getDealerId()==null?null:new Long(logonUser.getDealerId()));
				tfpo1.setCreateBy(logonUser.getUserId());
				tfpo1.setSupportRemark(supportRemark);
				tfpo1.setCreateDate(new java.util.Date());
				tfpo1.setSupportDate(new java.util.Date());
				dao.insert(tfpo1);
			}
			
			String [] groupId = groupIds.split(",");
			//String [] remark = remarks.split(",");
			String [] amount = amounts.split(",");
			String []price=prices.split(",");
			
			String []depotproprice=depotproprices.split(",");
			
			String []profit=profits.split(",");
			
			String []gandaccept=gandaccepts.split(",");
			String []realprice=realprices.split(",");
			String []marketdevelop=marketdevelops.split(",");
			String []realprofit=realprofits.split(",");
			String []requestsupport=requestsupports.split(",");
			//String [] originalPrice = originalPrices.split(",");
			//String [] discount = discounts.split(",");
			//String [] discountPrice = discountPrices.split(",");
			/*
			TtFleetIntentPO tfip = new TtFleetIntentPO();
			TtFleetIntentPO tfipContion = new TtFleetIntentPO();
			TtFleetIntentPO tfipValue = new TtFleetIntentPO();
			if("".equals(intentId)||intentId==null){
				tfip.setIntentId(Long.parseLong(SequenceManager.getSequence("")));
				tfip.setOemCompanyId(Long.parseLong(logonUser.getOemCompanyId()));
				tfip.setDlrCompanyId(logonUser.getCompanyId());
				tfip.setFleetId(Long.parseLong(fleetId));
				tfip.setPurchaseDate(fmat.parse(purchaseDate));
				tfip.setPurEndDate(fmat.parse(purendDate));
				if(!"".equals(infoGivingMan.trim())&&infoGivingMan.trim()!=null){
					tfip.setInfoGivingMan(infoGivingMan.trim());
				}
				tfip.setCompeteRemark(competeRemark);
				if(!"".equals(infoRemark.trim())&&infoRemark.trim()!=null){
					tfip.setInfoRemark(infoRemark.trim());
				}
				tfip.setStatus(Constant.FLEET_SUPPORT_STATUS_01);
				tfip.setCreateDate(new Date(System.currentTimeMillis()));
				tfip.setCreateBy(logonUser.getUserId());
				dao.insert(tfip);
			}else{
				tfipContion.setIntentId(Long.parseLong(intentId));
				tfipValue.setOemCompanyId(Long.parseLong(logonUser.getOemCompanyId()));
				tfipValue.setDlrCompanyId(logonUser.getCompanyId());
				tfipValue.setPurchaseDate(fmat.parse(purchaseDate));
				if(!"".equals(infoGivingMan.trim())&&infoGivingMan.trim()!=null){
					tfipValue.setInfoGivingMan(infoGivingMan.trim());
				}
				tfipValue.setCompeteRemark(competeRemark);
				if(!"".equals(infoRemark.trim())&&infoRemark.trim()!=null){
					tfipValue.setInfoRemark(infoRemark.trim());
				}
				tfipValue.setUpdateDate(new Date(System.currentTimeMillis()));
				tfipValue.setUpdateBy(logonUser.getUserId());
				dao.update(tfipContion, tfipValue);
				TtFleetIntentDetailPO tfidpContion = new TtFleetIntentDetailPO();
				tfidpContion.setIntentId(Long.parseLong(intentId));
				dao.delete(tfidpContion);
			}*/
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
					dao.insert(supportInfo);
				}
			}
			/**
			for(int i = 0; i< groupId.length; i++){
				if(!"".equals(groupId[i])&&groupId[i]!=null){
					TtFleetIntentDetailPO tfidp = new TtFleetIntentDetailPO();
					tfidp.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
					if(!"".equals(intentId)&&intentId!=null){
						tfidp.setIntentId(Long.parseLong(intentId));
					}else{
						tfidp.setIntentId(tfip.getIntentId());
					}
					tfidp.setFleetId(Long.parseLong(fleetId));
					tfidp.setIntentModel(Long.parseLong(groupId[i]));
//					if(!"null".equals(remark[i].trim())){
//						tfidp.setRemark(remark[i]);
//					}
					tfidp.setAmount(Integer.parseInt(amount[i]));
					//tfidp.setOriginalPrice(Double.parseDouble(originalPrice[i]));
					//tfidp.setDiscount(discount[i]);
					//tfidp.setDiscountPrice(Double.parseDouble(discountPrice[i]));
					tfidp.setCreateDate(new Date(System.currentTimeMillis()));
					tfidp.setCreateBy(logonUser.getUserId());
					dao.insert(tfidp);
				}
			}*/
			
			// 页面删除的附件
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
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"集团客户支持申请意向保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持申请提交
	 */
	public void fleetSupportApplyConfirm(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			/*String groupIds = CommonUtils.checkNull(request.getParamValue("groupIds"));					//物料组IDS
			String remarks = CommonUtils.checkNull(request.getParamValue("remarks"));					//备注
			String amounts = CommonUtils.checkNull(request.getParamValue("amounts"));					//数量
			String discounts = CommonUtils.checkNull(request.getParamValue("discounts"));				//支持点位
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));					//客户ID
			String intentId = CommonUtils.checkNull(request.getParamValue("intentId"));				    //意向ID
			String purchaseDate = CommonUtils.checkNull(request.getParamValue("purchaseDate"));			//预计采购日期
			String purendDate = CommonUtils.checkNull(request.getParamValue("purendDate"));             //预计购车结束日期
			String infoGivingMan = CommonUtils.checkNull(request.getParamValue("infoGivingMan"));		//信息提供人员
			String competeRemark = CommonUtils.checkNull(request.getParamValue("competeRemark"));		//竞争情况说明
			String infoRemark = CommonUtils.checkNull(request.getParamValue("infoRemark"));				//信息情况说明
			SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
			String [] groupId = groupIds.split(",");
			String [] remark = remarks.split(",");
			String [] amount = amounts.split(",");
			String [] discount = discounts.split(",");
			TtFleetIntentPO tfip = new TtFleetIntentPO();
			TtFleetIntentPO tfipContion = new TtFleetIntentPO();
			TtFleetIntentPO tfipValue = new TtFleetIntentPO();
			if("".equals(intentId)||intentId==null){
				tfip.setIntentId(Long.parseLong(SequenceManager.getSequence("")));
				tfip.setOemCompanyId(Long.parseLong(logonUser.getOemCompanyId()));
				tfip.setDlrCompanyId(logonUser.getCompanyId());
				tfip.setFleetId(Long.parseLong(fleetId));
				tfip.setPurchaseDate(fmat.parse(purchaseDate));
				tfip.setPurEndDate(fmat.parse(purendDate));
				if(!"".equals(infoGivingMan.trim())&&infoGivingMan.trim()!=null){
					tfip.setInfoGivingMan(infoGivingMan.trim());
				}
				tfip.setCompeteRemark(competeRemark);
				if(!"".equals(infoRemark.trim())&&infoRemark.trim()!=null){
					tfip.setInfoRemark(infoRemark.trim());
				}
				tfip.setReportDate(new Date(System.currentTimeMillis()));
				tfip.setStatus(Constant.FLEET_SUPPORT_STATUS_02);
				tfip.setCreateDate(new Date(System.currentTimeMillis()));
				tfip.setCreateBy(logonUser.getUserId());
				dao.insert(tfip);
			}else{
				tfipContion.setIntentId(Long.parseLong(intentId));
				tfipValue.setOemCompanyId(Long.parseLong(logonUser.getOemCompanyId()));
				tfipValue.setDlrCompanyId(logonUser.getCompanyId());
				tfipValue.setPurchaseDate(fmat.parse(purchaseDate));
				if(!"".equals(infoGivingMan.trim())&&infoGivingMan.trim()!=null){
					tfipValue.setInfoGivingMan(infoGivingMan.trim());
				}
				tfipValue.setCompeteRemark(competeRemark);
				if(!"".equals(infoRemark.trim())&&infoRemark.trim()!=null){
					tfipValue.setInfoRemark(infoRemark.trim());
				}
				tfipValue.setStatus(Constant.FLEET_SUPPORT_STATUS_02);
				tfipValue.setReportDate(new Date(System.currentTimeMillis()));
				tfipValue.setUpdateDate(new Date(System.currentTimeMillis()));
				tfipValue.setUpdateBy(logonUser.getUserId());
				dao.update(tfipContion, tfipValue);
				TtFleetIntentDetailPO tfidpContion = new TtFleetIntentDetailPO();
				tfidpContion.setIntentId(Long.parseLong(intentId));
				dao.delete(tfidpContion);
			}
			for(int i = 0; i< groupId.length; i++){
				if(!"".equals(groupId[i])&&groupId[i]!=null){
					TtFleetIntentDetailPO tfidp = new TtFleetIntentDetailPO();
					tfidp.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
					if(!"".equals(intentId)&&intentId!=null){
						tfidp.setIntentId(Long.parseLong(intentId));
					}else{
						tfidp.setIntentId(tfip.getIntentId());
					}
					tfidp.setFleetId(Long.parseLong(fleetId));
					tfidp.setIntentModel(Long.parseLong(groupId[i]));
					if(!"null".equals(remark[i].trim())){
						tfidp.setRemark(remark[i]);
					}
					tfidp.setAmount(Integer.parseInt(amount[i]));
					tfidp.setDiscount(discount[i]);
					tfidp.setCreateDate(new Date(System.currentTimeMillis()));
					tfidp.setCreateBy(logonUser.getUserId());
					dao.insert(tfidp);
				}
			}*/
			String supportRemark= CommonUtils.checkNull(request.getParamValue("supportRemark"));//商务支持信息备注
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
			String fleetId = CommonUtils.checkNull(request.getParamValue("fleetId"));	
			
			//向tmfleet表中加入supportRemark的值
			TtFleetSupportPO tfpo=new TtFleetSupportPO() ;
			TtFleetSupportPO tfpo1=new TtFleetSupportPO();
			
			tfpo.setFleetId(new Long(fleetId));
			List<PO> tfsList=dao.select(tfpo);
			if(tfsList.size()>0){
				tfpo1.setSupportRemark(supportRemark);
				tfpo1.setSupportDate(new java.util.Date());
				tfpo1.setSupportStatus(Constant.SUPPORT_INFO_STATUS_01);
				tfpo1.setUpdateBy(new BigDecimal(logonUser.getUserId()));
				tfpo1.setUpdateDate(new java.util.Date());
				dao.update(tfpo,tfpo1);
			}else{
				tfpo1.setSupportId(new Long(SequenceManager.getSequence("")));
				tfpo1.setFleetId(new Long(fleetId));
				tfpo1.setDealerId(logonUser.getDealerId()==null?null:new Long(logonUser.getDealerId()));
				tfpo1.setCreateBy(logonUser.getUserId());
				tfpo1.setSupportStatus(Constant.SUPPORT_INFO_STATUS_01);
				tfpo1.setSupportRemark(supportRemark);
				tfpo1.setSupportDate(new java.util.Date());
				tfpo1.setCreateDate(new java.util.Date());
				dao.insert(tfpo1);
			}
			String [] groupId = groupIds.split(",");
			//String [] remark = remarks.split(",");
			String [] amount = amounts.split(",");
			String []price=prices.split(",");
			
			String []depotproprice=depotproprices.split(",");
			
			String []profit=profits.split(",");
			
			String []gandaccept=gandaccepts.split(",");
			String []realprice=realprices.split(",");
			String []marketdevelop=marketdevelops.split(",");
			String []realprofit=realprofits.split(",");
			String []requestsupport=requestsupports.split(",");
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
					
//					supportInfo.setProfit(Double.parseDouble(profit[i]));
					supportInfo.setMarketDevelop(Double.parseDouble(marketdevelop[i]));
					supportInfo.setRealPrice(Double.parseDouble(realprice[i]));
					supportInfo.setRealProfit(Double.parseDouble(realprofit[i]));
					supportInfo.setRequestSupport(Double.parseDouble(requestsupport[i]));
					supportInfo.setCreateDate(new Date(System.currentTimeMillis()));
					supportInfo.setCreateBy(logonUser.getUserId());
					dao.insert(supportInfo);
				}
			}
			
			// 页面删除的附件
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
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"集团客户支持申请意向保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户信息打印查询页面
	 */
	public void fleetSupportPrintInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(printInitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"页面物料组树查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持申请明细查询
	 */
	public void fleetSupportPrintDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");	//客户ID
			Map<String, Object> map = dao.getFleetInfobyId(fleetId);
			act.setOutData("fleetMap", map);
			//根据集团客户主表的id查询子表需求说明中的内容
			FleetInfoAppDao appDao=new FleetInfoAppDao();
			List<Map<String, Object>>tfrdMapList=appDao.getMaterialByFleetId(fleetId);
			//根据集团客户主表的id查询子表商务支持中的内容
			List<Map<String,Object>> supportInfoList=appDao.getSupportInfoByFleetId(fleetId);
			
			//获取小区审核意见
			Map<String,Object>smallRegionAudit=appDao.getPrintAuditRemark(fleetId, "10431004");
			//获取大区审核意见
			Map<String,Object> largeRegionAudit=appDao.getPrintAuditRemark(fleetId, "10431003");
			act.setOutData("supportInfoList", supportInfoList);
			act.setOutData("tfrdMapList", tfrdMapList);
			act.setOutData("smallAudit", smallRegionAudit);
			act.setOutData("largeAudit", largeRegionAudit);
			act.setForword(printDetailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户支持打印查询
	 */
	public void fleetSupportQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetName = request.getParamValue("fleetName");			//客户名称
			String fleetType = request.getParamValue("fleetType");			//客户类型
			String startDate = request.getParamValue("startDate");			//起始时间
			String endDate = request.getParamValue("endDate");				//结束时间
			String companyId=request.getParamValue("companyId");
			String orgId = logonUser.getOrgId().toString();			//组织ID
			String dutyType=logonUser.getDutyType();
			String supportStatus = request.getParamValue("supportStatus");	//申请状态
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.fleetSupportPrintQuery(fleetName, fleetType, startDate, endDate, companyId,dutyType,orgId, supportStatus,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户支持查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 标记打印
	 */
	public void markAsPrintAlready(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String fleetId = request.getParamValue("fleetId");			//fleetId
			TmFleetPO tf=new TmFleetPO();
			tf.setFleetId(new Long(fleetId));
			TmFleetPO tf1=new TmFleetPO();
			tf1.setIsPrint(new Long(1));
			int count =dao.update(tf, tf1);
			act.setOutData("flag", count);
			
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"标记异常");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
