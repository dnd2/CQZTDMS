package com.infodms.dms.actions.parts.baseManager.mainData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.mainDate.mainDataMaintenanceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartGiftDefinePO;
import com.infodms.dms.po.TtPartStoDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;

import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 配件赠品维护
 */
public class mainDataMaintenance {
	public Logger logger = Logger.getLogger(mainDataMaintenance.class);
	mainDataMaintenanceDao dao = mainDataMaintenanceDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/parts/baseManager/mainDate/mainDataMaintenanceInit.jsp";
	private final String updateUrl = "/jsp/parts/baseManager/mainDate/mainDataMaintenanceUpate.jsp";
	private final String addUrl = "/jsp/parts/baseManager/mainDate/addmainDataMaintenanceInit.jsp";
	private final String PART_SELECT_MOD = "/jsp/parts/baseManager/mainDate/partSelectForMod.jsp";

	/**
	 * 加载赠品维护
	 */
	public void mainDataMaintenanceInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			act.setForword(initUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"加载赠品维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 新增
	 */
	public void addMainDataMaintenance(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			act.setForword(addUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"加载赠品维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	}
	/**
	 * 修改
	 */
	public void updateMainDataMaintenance(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String giftType = CommonUtils.checkNull(request.getParamValue("giftTypeM"));
			String sqlStr = "";
			if(!("".equals(giftType)||giftType == null)){
				sqlStr = " AND T.GIFT_TYPE = '"+ giftType +"' ";
			}
			Map<String, Object> list=dao.UpdatemainDataMaintenanceList(sqlStr);
			
			String way = "ZDWay";
			if(Constant.PART_GIFT_WAY_02 == Integer.parseInt(list.get("GIFT_WAY").toString()))
			{
				way = "PZWay";
				list.put("GIFT_WAY", "品种方式");
			}
			else
			{
				way = "ZDWay";
				list.put("GIFT_WAY", "整单方式");
			}
			act.setOutData("way", way);
			act.setOutData("defid", list.get("DEF_ID"));
			act.setOutData("list", list);
			act.setForword(updateUrl);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"加载赠品维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 
	 * @Title      : 赠品维护 ->配件选择页面初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-18
	 */
	public void goPartQueryMod(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String defid = CommonUtils.checkNull(request.getParamValue("prvDefid"));
			String sqlStr = "";
			if(!("".equals(defid)||defid == null)){
				sqlStr = " AND T.DEF_ID = '"+ defid +"' ";
			}
			Map<String, Object> map = dao.UpdatemainDataMaintenanceList(sqlStr);
			
			String giftType = map.get("GIFT_TYPE").toString();
			String condition = map.get("CONDITION_FM").toString();
			String startDate = map.get("START_DATE").toString();
			String endDate = map.get("END_DATE").toString();
			String isOemStart = map.get("IS_OEM_START").toString();
			String giftWay = map.get("GIFT_WAY").toString();
			
			
			act.setOutData("giftType", giftType);
			act.setOutData("condition", condition);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("isOemStart", isOemStart);
			act.setOutData("giftWay", giftWay);
			act.setForword(PART_SELECT_MOD);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"加载赠品维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 查询
	 */
	public void aMainDataMaintenanceQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=dao.getmainDataMaintenanceList(request, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"加载赠品维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	}
	
	public void detailQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String GIFT_TYPE_SEARCH = CommonUtils.checkNull(request.getParamValue("GIFT_TYPE_SEARCH"));// FOR SEARCH
			String GIFT_TYPE = CommonUtils.checkNull(request.getParamValue("GIFT_TYPE"));// FOR MODIFY 
			String PART_OLDCODE = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
			String PART_NAME = CommonUtils.checkNull(request.getParamValue("PART_NAME"));
			String giftWay = CommonUtils.checkNull(request.getParamValue("giftWay"));

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=dao.getmainDataMaintenanceList(GIFT_TYPE_SEARCH, GIFT_TYPE, PART_OLDCODE, PART_NAME, giftWay, curPage, 	Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"加载赠品维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 保存数据
	 */
	public void saveMainDataMaintenance(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String errorExist = "";
		try {
			String perRemark = "";
			String condition = "0";
			if(null != request.getParamValue("PER_NAME") && !"".equals(request.getParamValue("PER_NAME")))
			{
				condition = request.getParamValue("PER_NAME").trim().replace(",", "");//最小销售额
			}
			String giftWay = CommonUtils.checkNull(request.getParamValue("giftWay"));
			String isOemStart = CommonUtils.checkNull(request.getParamValue("isOemStart"));
			String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 制单开始时间
			String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 制单截止时间
			Date date = new Date();
			
			if(null == isOemStart || "".equals(isOemStart))
			{
				isOemStart = Constant.IF_TYPE_NO + "";
			}
			if(Constant.PART_GIFT_WAY_02 == Integer.parseInt(giftWay))
			{
				perRemark = request.getParamValue("PER_REMARK2").trim();
			}
		 	else
		 	{
		 		perRemark = request.getParamValue("PER_REMARK1").trim();
		 	}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			String sqlStr = " AND T.GIFT_TYPE <> '" + perRemark + "' ";
			List<Map<String, Object>> latestDefList = dao.getLatestDef(sqlStr);
			if(null != latestDefList && latestDefList.size() > 0)
			{
				String latEdDate = latestDefList.get(0).get("END_DATE_FM").toString();
				String latStDate = latestDefList.get(0).get("START_DATE_FM").toString();
				
				if(sdf.parse(latEdDate).getTime() < (sdf.parse(checkSDate)).getTime())
				{
					errorExist = "";
				}
				else if(sdf.parse(latEdDate).getTime() >= (sdf.parse(checkSDate)).getTime() && sdf.parse(latEdDate).getTime() <= (sdf.parse(checkEDate)).getTime())
				{
					errorExist = "维护的赠品活动时间不能与前一个活动结束时间重复!";
				}
				else if(sdf.parse(latStDate).getTime() >= (sdf.parse(checkSDate)).getTime() && sdf.parse(latStDate).getTime() <= (sdf.parse(checkEDate)).getTime())
				{
					errorExist = "维护的赠品活动时间不能与前一个活动结束时间重复!";
				}
				else if(sdf.parse(latStDate).getTime() > (sdf.parse(checkEDate)).getTime())
				{
					errorExist = "";
				}
				else
				{
					errorExist = "维护的赠品活动时间不能与前一个活动结束时间重复!";
				}
			}
			if("".equals(errorExist))
			{
				String [] dpId= request.getParamValues("partIds");
				String [] dpCode= request.getParamValues("DP_CODE");//配件编码
				String [] partCode = request.getParamValues("partCode");//配件件号
				String [] dpName= request.getParamValues("DP_NAME");
				String [] Amount= request.getParamValues("Amount");
				String [] meetQty = null;
				if(Constant.PART_GIFT_WAY_02 == Integer.parseInt(giftWay))
				{
					meetQty = request.getParamValues("meetQty");
				}
//				String [] sTatus= request.getParamValues("sTatus");
				
				for (int i = 0; i < dpId.length; i++) {
					List<Map<String, Object>> existlist = dao.getExistPO(perRemark, dpId[i].toString());
					if (existlist != null && existlist.size() > 0) {
						String partName = existlist.get(0).get("PART_NAME").toString();
						errorExist += partName + " ";
					}
				}
				TtPartGiftDefinePO po = null;
				
				if ("".equals(errorExist)){
					for(int i=0;i<dpId.length;i++){
						String chPrId= SequenceManager.getSequence(null);
						po = new TtPartGiftDefinePO();
					 	po.setDefId(Long.parseLong(chPrId));
					 	po.setGiftType(perRemark);
					 	po.setCondition(Double.parseDouble(condition));
					 	po.setPartId(Long.parseLong(dpId[i]));
					 	po.setPartCode(partCode[i]);
					 	po.setPartOldcode(dpCode[i]);
					 	po.setPartName(dpName[i]);
					 	po.setGiftQty(Long.parseLong(Amount[i]));
					 	if(Constant.PART_GIFT_WAY_02 == Integer.parseInt(giftWay))
						{
					 		po.setCondNum(Integer.parseInt(meetQty[i]));
						}
					 	else
					 	{
					 		po.setCondNum(1);
					 	}
					 	po.setGiftWay(Integer.parseInt(giftWay));
					 	po.setIsOemStart(Integer.parseInt(isOemStart));
					 	po.setStartDate(sdf.parse(checkSDate));
					 	po.setEndDate(sdf.parse(checkEDate));
					 	po.setState(Constant.STATUS_ENABLE);
					 	po.setState(Constant.STATUS_ENABLE);
					 	po.setCreateBy(logonUser.getUserId());
					 	po.setCreateDate(date);
						dao.insert(po);

					}
				}
				
				act.setOutData("success", "true");
				if(!"".equals(errorExist))
				{
					act.setOutData("errorExist", "配件：【" + errorExist + "】已添加，不能重复添加!");
				}
			}
			else
			{
				act.setOutData("errorExist", errorExist);
			}
			
			String curPage = CommonUtils.checkNull(request
					.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			
			act.setOutData("curPage", curPage);

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE,
					"新增赠品失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		 }
	}	
	
	/**
	 * 
	 * @Title      : 配件赠品修改
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-12
	 */
	public void UpdateDateMainDataMaintenance(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String errorExist = "";
		try {
			String defineIds[] = request.getParamValues("defineIds");// 关系ID
			String giftWay = request.getParamValue("giftWay").trim();//Old 赠品方式
			String perRemark = request.getParamValue("PER_REMARK").trim();//配件赠品描述
			String condition = "0";
			if("ZDWay".equals(giftWay))
			{
				condition = request.getParamValue("PER_NAME").trim().replace(",", "");//最小销售额
			}
			String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 制单开始时间
			String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 制单截止时间
			String isOemStart = CommonUtils.checkNull(request.getParamValue("isOemStart"));
			if(null == isOemStart || "".equals(isOemStart))
			{
				isOemStart = Constant.IF_TYPE_NO + "";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String sqlStr = " AND T.GIFT_TYPE <> '" + perRemark + "' ";
			List<Map<String, Object>> latestDefList = dao.getLatestDef(sqlStr);
			if(null != latestDefList && latestDefList.size() > 0)
			{
				String latEdDate = latestDefList.get(0).get("END_DATE_FM").toString();
				String latStDate = latestDefList.get(0).get("START_DATE_FM").toString();
				
				if(sdf.parse(latEdDate).getTime() < (sdf.parse(checkSDate)).getTime())
				{
					errorExist = "";
				}
				else if(sdf.parse(latEdDate).getTime() >= (sdf.parse(checkSDate)).getTime() && sdf.parse(latEdDate).getTime() <= (sdf.parse(checkEDate)).getTime())
				{
					errorExist = "维护的赠品活动时间不能与前一个活动结束时间重复!";
				}
				else if(sdf.parse(latStDate).getTime() >= (sdf.parse(checkSDate)).getTime() && sdf.parse(latStDate).getTime() <= (sdf.parse(checkEDate)).getTime())
				{
					errorExist = "维护的赠品活动时间不能与前一个活动结束时间重复!";
				}
				else if(sdf.parse(latStDate).getTime() > (sdf.parse(checkEDate)).getTime())
				{
					errorExist = "";
				}
				else
				{
					errorExist = "维护的赠品活动时间不能与前一个活动结束时间重复!";
				}
					
			}
			
			if("".equals(errorExist))
			{
				TtPartGiftDefinePO selPo = null;
				TtPartGiftDefinePO updPo = null;
				
				if(null != defineIds && defineIds.length  > 0)
				{
					for(int i = 0; i < defineIds.length; i ++)
					{
						String deftId = defineIds[i];// 序列ID
						String qty = request.getParamValue("qty_"+deftId);//赠送数量
						String meetNum = "0";
						if(!"ZDWay".equals(giftWay))
						{
							meetNum = request.getParamValue("meetNum_"+deftId);//满足数量
						}
						selPo = new TtPartGiftDefinePO();
						updPo = new TtPartGiftDefinePO();
						
						selPo.setDefId(Long.parseLong(deftId));
						
						if("ZDWay".equals(giftWay))
						{
							updPo.setCondition(Double.parseDouble(condition));
						}
						updPo.setGiftQty(Long.parseLong(qty));
						updPo.setCondNum(Integer.parseInt(meetNum));
						updPo.setStartDate(sdf.parse(checkSDate));
						updPo.setEndDate(sdf.parse(checkEDate));
						updPo.setIsOemStart(Integer.parseInt(isOemStart));
						
						dao.update(selPo, updPo);
					}
				}
				act.setOutData("success", "true");
			}
			else
			{
				act.setOutData("errorExist", errorExist);
			}
			
			String curPage = CommonUtils.checkNull(request
					.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			act.setOutData("curPage", curPage);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE,
					"维护赠品失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	
	}
	
	/**
	 * 
	 * @Title      : 维护 ->新增配件
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-12
	 */
	public void insrtPartMaintenance(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String errorExist = "";
		try {
			Long userId = logonUser.getUserId();// 操作用户ID
			Date date = new Date();
			String partIdsStr = request.getParamValue("ids");// 配件ID
			String giftType = request.getParamValue("giftType").trim();//赠品描述
			String condition = "0";
			if(null != request.getParamValue("condition") && !"".equals(request.getParamValue("condition")))
			{
				condition = request.getParamValue("condition").trim().replace(",", "");//最小销售额
			}
			String giftWay = CommonUtils.checkNull(request.getParamValue("giftWay"));
			String isOemStart = CommonUtils.checkNull(request.getParamValue("isOemStart"));
			String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 制单开始时间
			String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 制单截止时间
			
			if(null == isOemStart || "".equals(isOemStart))
			{
				isOemStart = Constant.IF_TYPE_NO + "";
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			String partIdsArr [] = null;
			TtPartGiftDefinePO insertPo = null;
			
			if(null != partIdsStr && !"".equals(partIdsStr))
			{
				partIdsArr = partIdsStr.trim().split(",");
			}
			
			for (int i = 0; i < partIdsArr.length; i++) {
				List<Map<String, Object>> existlist = dao.getExistPO(giftType, partIdsArr[i].toString());
				if (existlist != null && existlist.size() > 0) {
					String partName = existlist.get(0).get("PART_NAME").toString();
					errorExist += partName + " ";
				}
			}
			if ("".equals(errorExist)){
				for (int i = 0; i < partIdsArr.length; i++) {
					String partId = partIdsArr[i];
					String sqlStr = " AND PD.PART_ID = '" + partId + "' ";
					List<Map<String, Object>> partList = dao.getPartList(sqlStr);
					String partCode = partList.get(0).get("PART_CODE").toString();
					String partOldcode = partList.get(0).get("PART_OLDCODE").toString();
					String partName = partList.get(0).get("PART_CNAME").toString();
					
					
					insertPo = new TtPartGiftDefinePO();
					insertPo.setDefId(Long.parseLong(SequenceManager.getSequence("")));
					insertPo.setGiftType(giftType);
					insertPo.setCondition(Double.parseDouble(condition));
					insertPo.setPartCode(partCode);
					insertPo.setPartId(Long.parseLong(partId));
					insertPo.setPartName(partName);
					insertPo.setPartOldcode(partOldcode);
					insertPo.setCondNum(1);
					insertPo.setGiftQty(Long.parseLong(1 + ""));
					insertPo.setGiftWay(Integer.parseInt(giftWay));
					insertPo.setIsOemStart(Integer.parseInt(isOemStart));
					insertPo.setStartDate(sdf.parse(checkSDate));
					insertPo.setEndDate(sdf.parse(checkEDate));
					insertPo.setState(Constant.STATUS_ENABLE);
					insertPo.setCreateBy(userId);
					insertPo.setCreateDate(date);
					
					dao.insert(insertPo);
				}
			}
			
			String curPage = CommonUtils.checkNull(request
					.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			
			act.setOutData("errorExist", errorExist);
			act.setOutData("success", "true");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE,
					"维护赠品失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}	
	
	}
	
	/**
	 * 
	 * @Title      : 设置有效或无效
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-12
	 */
	public void celOrEnablePart() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String defineId = CommonUtils.checkNull(request.getParamValue("defId"));// 关系ID
			String optionType = CommonUtils.checkNull(request.getParamValue("optionType"));// 操作类型
			Long userId = logonUser.getUserId();// 操作用户ID

			//TtPartGiftDefinePO
			TtPartGiftDefinePO selPO = new TtPartGiftDefinePO();
			TtPartGiftDefinePO updatePO = new TtPartGiftDefinePO();

			selPO.setDefId(Long.parseLong(defineId));

			updatePO.setUpdateBy(userId);
			updatePO.setUpdateDate(new Date());
			updatePO.setDisableBy(userId);
			updatePO.setDisableDate(new Date());
			if("disable".equalsIgnoreCase(optionType))
			{
				updatePO.setState(Constant.STATUS_DISABLE);
			}
			else
			{
				updatePO.setState(Constant.STATUS_ENABLE);
			}

			dao.update(selPO, updatePO);

			String curPage = CommonUtils.checkNull(request
					.getParamValue("curPage"));// 当前页
			if ("".equals(curPage)) {
				curPage = "1";
			}
			act.setOutData("success", "true");
			act.setOutData("curPage", curPage);

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "赠品状态设置失败 ");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
