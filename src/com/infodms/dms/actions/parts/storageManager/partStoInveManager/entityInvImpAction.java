package com.infodms.dms.actions.parts.storageManager.partStoInveManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infodms.dms.util.CheckUtil;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.parts.baseManager.mainData.mainDataMaintenance;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.storageManager.partStoInveManager.entityInvImpDao;
import com.infodms.dms.dao.parts.storageManager.partStoInveManager.stockInventoryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartCheckDtlPO;
import com.infodms.dms.po.TtPartCheckMainPO;
import com.infodms.dms.po.TtPartCheckResultDtlPO;
import com.infodms.dms.po.TtPartCheckResultMainPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 处理配件库存盘点调整申请业务
 * 
 * @Date: 2013-5-6
 * 
 * @author huhcao
 * @version 1.0
 * @remark
 */
public class entityInvImpAction extends BaseImport {
	public Logger logger = Logger.getLogger(entityInvImpAction.class);
	private static final entityInvImpDao dao = entityInvImpDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final int PAGE_SIZE = 20;
	private static final int type1 = Constant.PART_STOCK_INVE_TYPE_01;//全部
	private static final int type2 = Constant.PART_STOCK_INVE_TYPE_02;//动态
	private static final int state1 = Constant.PART_STOCK_INVE_STATE_01;//已保存
	private static final int state2 = Constant.PART_STOCK_INVE_STATE_02;//盘点中
	private static final int state3 = Constant.PART_STOCK_INVE_STATE_03;//已盘点
	private static final int resolveType1 = Constant.PART_INVE_RESOLVE_TYPE_01;//封存
	private static final int resolveType2 = Constant.PART_INVE_RESOLVE_TYPE_02;//盈亏出入库
	private static final int orderState1 = Constant.PART_INVE_ORDER_STATE_01;//已保存
	private static final int orderState2 = Constant.PART_INVE_ORDER_STATE_02;//审核中
	private static final int orderState3 = Constant.PART_INVE_ORDER_STATE_03;//处理中
	private static final int orderState4 = Constant.PART_INVE_ORDER_STATE_04;//已驳回
	private static final int orderState5 = Constant.PART_INVE_ORDER_STATE_05;//已关闭
	private static final int orderState6 = Constant.PART_INVE_ORDER_STATE_06;//已完成
	private static final int orderState7 = Constant.PART_INVE_ORDER_STATE_07;//已作废
	private static final int limitLineNum = 300;
	private static final int inveStatusAble = Constant.PART_STATUS_EN_ABLE; //盘点单可用
	private static final int inveStatusUnAble = Constant.PART_STATUS_UN_ABLE; //盘点单不可用
	
	//配件库存盘点调整申请
	private static final String PART_STOCK_MAIN = "/jsp/parts/storageManager/partStoInveManager/partEntityInvImp/partEntityInvImp.jsp";//配件库存盘点调整申请首页
	private static final String PART_STOCK_INVE_VIEW = "/jsp/parts/storageManager/partStoInveManager/partEntityInvImp/partEntityInvImpView.jsp";//库存盘点调整申请查看页面
	private static final String INPUT_ERROR_URL= "/jsp/parts/storageManager/partStoInveManager/partStockInventory/inputError.jsp";//数据导入出错页面

	/**
	 * 
	 * @Title : 跳转至配件库存盘点调整申请页面
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-5-6
	 */
	public void entityInvImpInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String parentOrgId = "";//父机构（销售单位）ID
			String parentOrgCode = "";//父机构（销售单位）编码
			String companyName = ""; //制单单位
			//判断主机厂与服务商
			String comp = logonUser.getOemCompanyId();
			if (null == comp ){
				
				parentOrgId = Constant.OEM_ACTIVITIES;
				parentOrgCode = Constant.ORG_ROOT_CODE;
				companyName = dao.getMainCompanyName(parentOrgId);
			}else {
				parentOrgId = logonUser.getDealerId();
				parentOrgCode = logonUser.getDealerCode();
				companyName = dao.getDealerName(parentOrgId);
			}
			StringBuffer sbString = new StringBuffer();
			sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
			List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
			
			StringBuffer sbStr = new StringBuffer();
			sbStr.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			List<Map<String, Object>> inveList = dao.getInventories(sbStr.toString());
			
			act.setOutData("parentOrgId", parentOrgId);
			act.setOutData("parentOrgCode", parentOrgCode);
			act.setOutData("companyName", companyName);
			act.setOutData("WHList", WHList);
			act.setOutData("inveList", inveList);
			act.setForword(PART_STOCK_MAIN);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件库存盘点调整申请页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 查询配件库存盘点调整申请信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void entityInvImpSearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String inveCode = CommonUtils.checkNull(request
					.getParamValue("inveCode")); // 盘点单号
			String resultCode = CommonUtils.checkNull(request
					.getParamValue("resultCode")); // 申请单号
			String checkSDate = CommonUtils.checkNull(request
					.getParamValue("checkSDate")); // 开始时间
			String checkEDate = CommonUtils.checkNull(request
					.getParamValue("checkEDate")); // 截止时间
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId")); // 仓库ID
			String inveType = CommonUtils.checkNull(request
					.getParamValue("inveType")); // 盘点类型
			String improterName = CommonUtils.checkNull(request
					.getParamValue("improterName")); // 导入人
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			StringBuffer sbString = new StringBuffer();
			if(null != inveCode && !"".equals(inveCode))
			{
				sbString.append(" AND TM.CHANGE_CODE LIKE '%" + inveCode + "%' ");
			}
			if(null != resultCode && !"".equals(resultCode))
			{
				sbString.append(" AND TM.RESULT_CODE LIKE '%" + resultCode + "%' ");
			}
			if(null != improterName && !"".equals(improterName))
			{
				sbString.append(" AND U.NAME LIKE '%" + improterName + "%' ");
			}
			if(null != checkSDate && !"".equals(checkSDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
			}
			if(null != checkEDate && !"".equals(checkEDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
			}
			if(null != whId && !"".equals(whId))
			{
				sbString.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			if(null != inveType && !"".equals(inveType))
			{
				sbString.append(" AND TM.CHECK_TYPE = '" + inveType + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartStockInve(
					sbString.toString(), Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存盘点调整申请信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 跳转至库存盘点调整申请查看页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void viewStockDeatilInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String resultId = CommonUtils.checkNull(request.getParamValue("resultId"));// 盘点结果单ID
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 盘点结果单ID
			StringBuffer sbString = new StringBuffer();
			sbString.append(" AND TM.RESULT_ID = '" + resultId + "' ");
			Map<String, Object> map = dao.queryAllPartStockInve(sbString.toString()).get(0);
			
			int type = Integer.parseInt(map.get("CHECK_TYPE").toString());
			int orederState = Integer.parseInt(map.get("STATE").toString());
			
			if(type1 == type)
			{
				map.put("CHECK_TYPE", "全部");
			}
			else if(type2 == type)
			{
				map.put("CHECK_TYPE", "动态");
			}
			
			if(orderState1 == orederState)
			{
				map.put("STATE", "已保存");
			}
			else if(orderState2 == orederState)
			{
				map.put("STATE", "审核中");
			}
			else if(orderState3 == orederState)
			{
				map.put("STATE", "处理中");
			}
			else if(orderState4 == orederState)
			{
				map.put("STATE", "已驳回");
			}
			else if(orderState5 == orederState)
			{
				map.put("STATE", "已关闭");
			}
			else if(orderState6 == orederState)
			{
				map.put("STATE", "已完成");
			}
			else if(orderState7 == orederState)
			{
				map.put("STATE", "已作废");
			}
			
			act.setOutData("map", map);
			act.setForword(PART_STOCK_INVE_VIEW);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存盘点调整申请查看页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 提交或者作废库存盘点调整申请结果
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void commitOrDisableInveRes(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		RequestWrapper req = act.getRequest();
		String errorExist = "";
	try {
		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
		if("".equals(curPage)){
			curPage = "1";
		}
		String resultId = CommonUtils.checkNull(req.getParamValue("resultId")); //盘点单结果ID
		String optionType = CommonUtils.checkNull(req.getParamValue("optionType")); //操作类型
		Long userId = logonUser.getUserId(); //操作人ID
		Date date = new Date();
		
		TtPartCheckResultMainPO selCMPo = null;
		TtPartCheckResultMainPO updCMPo = null;
		
		StringBuffer sbString = new StringBuffer();
		if(null != resultId && !"".equals(resultId))
		{
			sbString.append(" AND TM.RESULT_ID = '" + resultId + "' ");
		}
		
		List<Map<String, Object>> chkResList = dao.queryChkResList(sbString.toString());
		
		if(null != chkResList && chkResList.size() == 1)
		{
			int state = Integer.parseInt(chkResList.get(0).get("STATE").toString());
			String resCode = chkResList.get(0).get("RESULT_CODE").toString();
			
			if("commit".equalsIgnoreCase(optionType))
			{
				if(orderState1 == state || orderState4 == state)
				{
					selCMPo = new TtPartCheckResultMainPO();
					updCMPo = new TtPartCheckResultMainPO();
					
					selCMPo.setResultId(Long.parseLong(resultId));
					
					updCMPo.setUpdateBy(userId);
					updCMPo.setUpdateDate(date);
					updCMPo.setCommitBy(userId);
					updCMPo.setCommitDate(date);
					updCMPo.setState(orderState2);
					
					dao.update(selCMPo, updCMPo);
				}
				else if(orderState2 == state)
				{
					errorExist = "申请单号【" + resCode + "】审核中,不能再提交!";
				}
				else if(orderState3 == state)
				{
					errorExist = "申请单号【" + resCode + "】处理中,不能再提交!";
				}
				else if(orderState5 == state)
				{
					errorExist = "申请单号【" + resCode + "】已关闭,不能再提交!";
				}
				else if(orderState6 == state)
				{
					errorExist = "申请单号【" + resCode + "】已完成,不能再提交!";
				}
				else if(orderState7 == state)
				{
					errorExist = "申请单号【" + resCode + "】已作废,不能再提交!";
				}
				else
				{
					errorExist = "操作失败,请联系管理员!";
				}
			}
			else if("disable".equalsIgnoreCase(optionType))
			{
				if(orderState1 == state || orderState4 == state)
				{
					selCMPo = new TtPartCheckResultMainPO();
					updCMPo = new TtPartCheckResultMainPO();
					
					selCMPo.setResultId(Long.parseLong(resultId));
					
					updCMPo.setUpdateBy(userId);
					updCMPo.setUpdateDate(date);
					updCMPo.setDisableBy(userId);
					updCMPo.setDisableDate(date);
					updCMPo.setState(orderState7);
					
					dao.update(selCMPo, updCMPo);
					
					//设置盘点单可用
					StringBuffer sbStr = new StringBuffer();
					sbStr.append(" AND TM.RESULT_ID = '" + resultId + "' ");
					List<Map<String, Object>> inveList = dao.getInveId(sbStr.toString());
					String inveId = inveList.get(0).get("CHANGE_ID").toString();
					
					TtPartCheckMainPO selPCMPo = new TtPartCheckMainPO();
					TtPartCheckMainPO updPCMPo = new TtPartCheckMainPO();
					
					selPCMPo.setChangeId(Long.parseLong(inveId));
					
					updPCMPo.setUpdateBy(userId);
					updPCMPo.setUpdateDate(date);
					updPCMPo.setStatus(inveStatusAble); //设置盘点单可用
					
					dao.update(selPCMPo, updPCMPo);
				}
				else if(orderState2 == state)
				{
					errorExist = "申请单号【" + resCode + "】审核中,不能进行作废操作!";
				}
				else if(orderState3 == state)
				{
					errorExist = "申请单号【" + resCode + "】处理中,不能进行作废操作!";
				}
				else if(orderState5 == state)
				{
					errorExist = "申请单号【" + resCode + "】已完成,不能进行作废操作!";
				}
				else if(orderState6 == state)
				{
					errorExist = "申请单号【" + resCode + "】已关闭,不能进行作废操作!";
				}
				else if(orderState7 == state)
				{
					errorExist = "申请单号【" + resCode + "】已作废,不能重复操作!";
				}
				else
				{
					errorExist = "操作失败,请联系管理员!";
				}
			}
		}
		else
		{
			errorExist = "操作失败,请联系管理员!";
		}
		
		act.setOutData("errorExist", errorExist);//返回错误信息
		act.setOutData("success", "true");
		act.setOutData("curPage", curPage);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"提交或作废库存盘点申请结果");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 保存配件库存盘点调整申请信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	private void saveStockInfos(String inveId, List<Map<String,String>> uploadList){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//		act.getResponse().setContentType("application/json");
		RequestWrapper req = act.getRequest();
		try {
			StringBuffer sbStr = new StringBuffer();
			sbStr.append(" AND TM.CHANGE_ID = '" + inveId + "' ");
			List<Map<String, Object>> inveList = dao.getInventories(sbStr.toString());
			
			String parentOrgId = inveList.get(0).get("CHGORG_ID").toString(); //制单单位ID
			String parentOrgCode = inveList.get(0).get("CHGORG_CODE").toString(); //制单单位编码
			String chgorgCname = inveList.get(0).get("CHGORG_CNAME").toString(); //制单单位名称
			String changeCode = inveList.get(0).get("CHANGE_CODE").toString(); //制单单位名称;//获取盘点单号
			String resultCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_24);//获取盘点结果单号
			Long userId = logonUser.getUserId(); //制单人ID
			String whId = inveList.get(0).get("WH_ID").toString(); //仓库ID
			String whName = inveList.get(0).get("WH_CNAME").toString(); //仓库名称
			String inveType = inveList.get(0).get("CHECK_TYPE").toString();; //盘点类型
			
			int upListSize = uploadList.size();
			int markNum = 1;
			
			if(limitLineNum < upListSize)
			{
				markNum = (int) Math.floor(upListSize/limitLineNum) + 1;
			}
			
			Date date = new Date();
			
			TtPartCheckMainPO selCMPo = new TtPartCheckMainPO();
			TtPartCheckMainPO updCMPo = new TtPartCheckMainPO();
			
			selCMPo.setChangeId(Long.parseLong(inveId));
			
			updCMPo.setUpdateBy(userId);
			updCMPo.setUpdateDate(date);
			updCMPo.setStatus(inveStatusUnAble);//盘点单不可用
			
			dao.update(selCMPo, updCMPo);
			
			for(int i = 0; i < markNum; i++)
			{
				Long resultId = Long.parseLong(SequenceManager.getSequence(""));
				
				TtPartCheckResultMainPO inserCMPo = new TtPartCheckResultMainPO();
				
				inserCMPo.setResultId(resultId);
				inserCMPo.setResultCode(resultCode);//申请单号
				inserCMPo.setChangeId(Long.parseLong(inveId));
				inserCMPo.setChangeCode(changeCode);
				inserCMPo.setChgorgId(Long.parseLong(parentOrgId));
				inserCMPo.setChgorgCode(parentOrgCode);
				inserCMPo.setChgorgCname(chgorgCname);
				inserCMPo.setWhId(Long.parseLong(whId));
				inserCMPo.setWhCname(whName);
				inserCMPo.setCheckType(Integer.parseInt(inveType));
				inserCMPo.setRemark("");
				inserCMPo.setCreateBy(userId);
				inserCMPo.setCreateDate(date);
				inserCMPo.setUpdateBy(userId);
				inserCMPo.setUpdateDate(date);
				inserCMPo.setState(orderState1);
				
				dao.insert(inserCMPo);
				
				TtPartCheckResultDtlPO insertCDPo = null;
				
				if((i+1) * limitLineNum > upListSize)
				{
					for(int j = (i * limitLineNum); j < upListSize; j++ )
					{
						
						Long normalQty =  Long.parseLong(uploadList.get(j).get("normalQty")); //可用库存
						Long bookedQty =  Long.parseLong(uploadList.get(j).get("bookedQty")); //占用库存
						Long fcQty =  Long.parseLong(uploadList.get(j).get("fcQty")); //封存库存
						Long itemQty =  Long.parseLong(uploadList.get(j).get("itemQty")); //账面库存
						Long checkQty =  Long.parseLong(uploadList.get(j).get("checkQty")); //盘点库存
						Long diffQty =  itemQty - checkQty; //盈亏库存
						
						
						if( 0 != diffQty)
						{
							insertCDPo = new TtPartCheckResultDtlPO();
							
							Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
							String partId = uploadList.get(j).get("partId");//配件ID
							String partCode = uploadList.get(j).get("partCode");//件号                                                                       
							String partOldcode =  uploadList.get(j).get("partOldcode");  //配件编码                                                         
							String partCname =  uploadList.get(j).get("partCname"); //配件名称
	                        String locId = uploadList.get(j).get("locId"); // 货位id
	                        String locCode = uploadList.get(j).get("locCode"); // 货位编码
	                        String locName = uploadList.get(j).get("locName"); // 货位名称
	                        
	                        String venderId = uploadList.get(j).get("venderId"); // 供应商id
	                        String batchCode = uploadList.get(j).get("batchCode"); // 批次码
	                        
	                        
							int checkResult = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02; //默认 盘盈
							if(0 < diffQty)
							{
								checkResult = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03; //盘亏
							}
							String unit =  uploadList.get(j).get("unit"); //单位
							String deRemark =  uploadList.get(j).get("remark"); //明细备注
							
							
							insertCDPo.setDtlId(dtlId);
							insertCDPo.setResultId(resultId);
							insertCDPo.setLineNo(Long.parseLong((j+1)+""));
							insertCDPo.setPartId(Long.parseLong(partId));
							insertCDPo.setPartCode(partCode);
							insertCDPo.setPartOldcode(partOldcode);
							insertCDPo.setPartCname(partCname);
							insertCDPo.setLocId(Long.parseLong(locId));
							insertCDPo.setLocCode(locCode);
							insertCDPo.setLocName(locName);
							insertCDPo.setUnit(unit);
							insertCDPo.setVenderId(Long.parseLong(venderId));
							insertCDPo.setBatchCode(batchCode);
							insertCDPo.setNormalQty(normalQty);
							insertCDPo.setBookQty(bookedQty);
							insertCDPo.setCheckQty(checkQty);
							insertCDPo.setMbQty(fcQty);
							insertCDPo.setDiffQty(Math.abs(diffQty));
							insertCDPo.setCheckResult(checkResult);
							insertCDPo.setIsOver(0);//0 表示未处理
							insertCDPo.setCreateBy(userId);
							insertCDPo.setCreateDate(date);
							insertCDPo.setUpdateBy(userId);
							insertCDPo.setUpdateDate(date);
							insertCDPo.setRemark(deRemark);
							
							dao.insert(insertCDPo);
						}
					}
				}
				else
				{
					for(int j = (i * limitLineNum); j < (i+1) * limitLineNum; j++ )
					{
						Long normalQty =  Long.parseLong(uploadList.get(j).get("normalQty")); //可用库存
						Long bookedQty =  Long.parseLong(uploadList.get(j).get("bookedQty")); //占用库存
						Long fcQty =  Long.parseLong(uploadList.get(j).get("fcQty")); //封存库存
						Long checkQty =  Long.parseLong(uploadList.get(j).get("checkQty")); //盘点库存
						Long diffQty =  Math.abs(normalQty + bookedQty - checkQty); //盈亏库存
						
						if( 0 != diffQty)
						{
							insertCDPo = new TtPartCheckResultDtlPO();
							
							Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
							String partId = uploadList.get(j).get("partId");//配件ID
							String partCode = uploadList.get(j).get("partCode");//件号                                                                       
							String partOldcode =  uploadList.get(j).get("partOldcode");  //配件编码                                                         
							String partCname =  uploadList.get(j).get("partCname"); //配件名称
	                        String locId = uploadList.get(j).get("locId"); // 货位id
	                        String locCode = uploadList.get(j).get("locCode"); // 货位编码
	                        String locName = uploadList.get(j).get("locName"); // 货位名称
							int checkResult = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02; //默认 盘盈
							if(0 < (normalQty + bookedQty - checkQty))
							{
								checkResult = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03; //盘亏
							}
							String unit =  uploadList.get(j).get("unit"); //单位
							String deRemark =  uploadList.get(j).get("remark"); //明细备注
							
							
							insertCDPo.setDtlId(dtlId);
							insertCDPo.setResultId(resultId);
							insertCDPo.setLineNo(Long.parseLong((j+1)+""));
							insertCDPo.setPartId(Long.parseLong(partId));
							insertCDPo.setPartCode(partCode);
							insertCDPo.setPartOldcode(partOldcode);
							insertCDPo.setPartCname(partCname);
                            insertCDPo.setLocId(Long.parseLong(locId));
                            insertCDPo.setLocCode(locCode);
                            insertCDPo.setLocName(locName);
							insertCDPo.setUnit(unit);
							insertCDPo.setNormalQty(normalQty);
							insertCDPo.setBookQty(bookedQty);
							insertCDPo.setMbQty(fcQty);
							insertCDPo.setCheckQty(checkQty);
							insertCDPo.setDiffQty(diffQty);
							insertCDPo.setCheckResult(checkResult);
							insertCDPo.setIsOver(0);//0 表示未处理
							insertCDPo.setCreateBy(userId);
							insertCDPo.setCreateDate(date);
							insertCDPo.setUpdateBy(userId);
							insertCDPo.setUpdateDate(date);
							insertCDPo.setRemark(deRemark);
							
							dao.insert(insertCDPo);
						}
					}
					
				}
			}
//			act.setOutData("success", "true");
			entityInvImpInit();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"保存配件库存盘点调整申请信息");
			logger.error(logonUser,e1);
			act.setException(e1);
			act.setOutData("errorExist", "保存失败!");
		}
	}
	
	/**
	 * 
	 * @Title      : 导出配件库存盘点调整申请 EXECEL模板
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void exportExcelTemplate(){

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			
		
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();//导出模板第一列
			listHead.add("配件编码");
			listHead.add("货位编码");
			listHead.add("盘点数量 ");
			listHead.add("备注");
			list.add(listHead);
			// 导出的文件名
			String fileName = "配件库存盘点调整申请导入模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @Title      : 配件库存盘点调整申请 -> 导入文件
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void entityInvImpUpload(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper req = act.getRequest();
		try {
			String inveId = CommonUtils.checkNull(req.getParamValue("inveId")); //盘点单ID
			String actionURL = CommonUtils.checkNull(request.getParamValue("actionURL"));// action地址
			StringBuffer sbStr = new StringBuffer();
			sbStr.append(" AND TM.CHANGE_ID = '" + inveId + "' ");
			List<Map<String, Object>> inveList = dao.getInventories(sbStr.toString());
			
			String whId = "";
			int inveType = 0;
			
			String parentOrgId = "";//父机构（销售单位）ID
			//判断主机厂与服务商
			String comp = logonUser.getOemCompanyId();
			if (null == comp ){
				
				parentOrgId = Constant.OEM_ACTIVITIES;
			}else {
				parentOrgId = logonUser.getDealerId();
			}
			List<Map<String,String>> errorInfo = null;
			
			String err="";
			
			if(null != inveList && inveList.size() == 1)
			{
				whId = inveList.get(0).get("WH_ID").toString();
				inveType = Integer.parseInt(inveList.get(0).get("CHECK_TYPE").toString());
				
				errorInfo =  new ArrayList<Map<String,String>>();
				long maxSize=1024*1024*5;
				int errNum = insertIntoTmp(request, "uploadFile",4,1,maxSize);
				
				if(errNum!=0){
					switch (errNum) {
					case 1:
						err+="文件列数过多!";
						break;
					case 2:
						err+="空行不能大于三行!";
						break;
					case 3:
						err+="文件不能为空!";
						break;
					case 4:
						err+="文件不能为空!";
						break;
					case 5:
						err+="文件不能大于!";
						break;
					default:
						break;
					}
				}
			}
			else
			{
				err+="盘点单号 " + inveId + " 已使用或已失效!";
			}
			
			if(!"".equals(err)){
				act.setOutData("error", err);
				act.setOutData("actionURL", actionURL);
				act.setForword(INPUT_ERROR_URL);
			}else{
				List<Map> list=getMapList();
				List<Map<String,String>> voList = new ArrayList<Map<String,String>>();
				loadVoList(voList,list, errorInfo, parentOrgId, whId, inveType, inveId);
				if(errorInfo.size()>0){
					act.setOutData("errorInfo", errorInfo);
					act.setOutData("actionURL", actionURL);
					act.setForword(INPUT_ERROR_URL);
				}else{
					saveStockInfos(inveId, voList);
				}
				
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = null;
			if(e instanceof BizException){
				e1 = (BizException)e;
			}else{
				new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			}
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * <p>Description: TODO</p>
	 * @param voList 
	 * @param list excel数据
	 * @param errorInfo 错误信心
	 * @param parentOrgId 父机构id
	 * @param whId 仓库id
	 * @param inveType 盘点类型
	 * @param inveId 盘点单号
	 */
	@SuppressWarnings("unchecked")
    private void loadVoList(List<Map<String,String>> voList,List<Map> list,List<Map<String,String>> errorInfo, String parentOrgId, String whId, int inveType, String inveId){
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				Map<String, String> tempmap = new HashMap<String, String>();
				int cellLength = 1;
				int cellIndex = 0;
				// 配件编码
				String partCode = "";
                if ("".equals(cells[cellIndex].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "配件编码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                    return;
                }else{
                    partCode = cells[cellIndex].getContents().trim();
                }
                
                // 货位编码
                cellLength++;
                cellIndex++;
                String locCode = "";
                if (cells.length < cellLength || "".equals(cells[cellIndex].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "货位编码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                    return;
                }else{
                    locCode = cells[cellIndex].getContents().trim();
                }
				// 根据配件编码获取配件信息
				List<Map<String, Object>> partCheck = dao.checkOldCode(partCode);
				if (partCheck.size() == 1) {
				    // 根据配件编码和盘点单号获取数据
					List<Map<String, Object>> whInInveList = dao.checkPartWhInInve(partCode, inveType, inveId, locCode);
					if(whInInveList.size() == 1)
					{
					    // 根据配件编码、机构id、仓库id、货位获取配件信息
						List<Map<String, Object>> partList = dao.getPartStockInfos(partCode, parentOrgId, whId, locCode);
						if(partList.size() == 1) {
							tempmap.put("partOldcode", cells[0].getContents().trim());
							
							tempmap.put("partCode", CommonUtils.checkNull(partList.get(0).get("PART_CODE")));
							tempmap.put("partCname", CommonUtils.checkNull(partList.get(0).get("PART_CNAME")));
							tempmap.put("unit", CommonUtils.checkNull(partList.get(0).get("UNIT")));
							tempmap.put("partId", partList.get(0).get("PART_ID").toString());
							tempmap.put("normalQty", partList.get(0).get("NORMAL_QTY").toString());
							tempmap.put("bookedQty", partList.get(0).get("BOOKED_QTY").toString());
							tempmap.put("fcQty", partList.get(0).get("FC_QTY").toString());
							tempmap.put("itemQty", partList.get(0).get("ITEM_QTY").toString()); //账面库存
							tempmap.put("locId", partList.get(0).get("LOC_ID").toString());
							tempmap.put("locCode", partList.get(0).get("LOC_CODE").toString());
							tempmap.put("locName", partList.get(0).get("LOC_NAME").toString());
							tempmap.put("venderId", partList.get(0).get("STOCK_VENDER_ID").toString());
							tempmap.put("batchCode", partList.get(0).get("BATCH_NO").toString());
						} else {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "配件编码" + cells[0].getContents().trim());
							errormap.put("3", "不在当前登录用户权限范围!");
							errorInfo.add(errormap);
							return;
						}
					} else {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "配件编码"+ cells[0].getContents().trim());
						errormap.put("3", "不在当前盘点配件范围内!");
						errorInfo.add(errormap);
                        return;
					}
					
				} else {
					Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
					errormap.put("2", "配件编码" + cells[0].getContents().trim());
					errormap.put("3", "不存在!");
					errorInfo.add(errormap);
					return;
				}

				// 盘点数量
                cellLength++;
                cellIndex++;
				if (cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
					Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
					errormap.put("2", "盘点数量");
					errormap.put("3", "为空!");
					errorInfo.add(errormap);
                    return;
				} else {
					String accTemp = cells[cellIndex].getContents().trim();
					if (null == accTemp || "".equals(accTemp)) {
						accTemp = "0";
					} else {
						accTemp = accTemp.replace(",", "");
					}
					
					String regex = "((^[0]$)|(^[1-9]+\\d*$))";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(accTemp);
					
					if(matcher.find())
					{
						tempmap.put("checkQty", accTemp);
					}
					else
					{
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "盘点数量");
						errormap.put("3", "非法数值!");
						errorInfo.add(errormap);
                        return;
					}
				}
				
				// 备注
                cellLength++;
                cellIndex++;
				tempmap.put("remark", cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents()) ? "" : cells[cellIndex].getContents().trim());
				voList.add(tempmap);
			}
		}
	}
	
	/**
	 * 
	 * @Title      : 库存盘点调整申请详情查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void partStockDetailSearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String resultId = CommonUtils.checkNull(request.getParamValue("resultId")); // 盘点结果单ID
			
			StringBuffer sbString = new StringBuffer();
			if(null != resultId && !"".equals(resultId))
			{
				sbString.append(" AND TM.RESULT_ID = '" + resultId + "' ");
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = null;
			
			ps = dao.queryPartStockDeatil(sbString.toString(), Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件库存盘点调整申请信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :导出配件库存盘点调整申请信息
	 * @param : @return
	 * @return :
	 * @throws : LastDate : 2013-5-3
	 */
	public void exportPartStockStatusExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String inveCode = CommonUtils.checkNull(request
					.getParamValue("inveCode")); // 盘点单号
			String resultCode = CommonUtils.checkNull(request
					.getParamValue("resultCode")); // 申请单号
			String checkSDate = CommonUtils.checkNull(request
					.getParamValue("checkSDate")); // 开始时间
			String checkEDate = CommonUtils.checkNull(request
					.getParamValue("checkEDate")); // 截止时间
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId")); // 仓库ID
			String inveType = CommonUtils.checkNull(request
					.getParamValue("inveType")); // 仓库ID
			String improterName = CommonUtils.checkNull(request
					.getParamValue("improterName")); // 导入人
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			StringBuffer sbString = new StringBuffer();
			if(null != inveCode && !"".equals(inveCode))
			{
				sbString.append(" AND TM.CHANGE_CODE LIKE '%" + inveCode + "%' ");
			}
			if(null != resultCode && !"".equals(resultCode))
			{
				sbString.append(" AND TM.RESULT_CODE LIKE '%" + resultCode + "%' ");
			}
			if(null != improterName && !"".equals(improterName))
			{
				sbString.append(" AND U.NAME LIKE '%" + improterName + "%' ");
			}
			if(null != checkSDate && !"".equals(checkSDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
			}
			if(null != checkEDate && !"".equals(checkEDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
			}
			if(null != whId && !"".equals(whId))
			{
				sbString.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			if(null != inveType && !"".equals(inveType))
			{
				sbString.append(" AND TM.CHECK_TYPE = '" + inveType + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			String[] head = new String[8];
			head[0] = "序号";
			head[1] = "盘点单号";
			head[2] = "申请单号";
			head[3] = "盘点类型";
			head[4] = "盘点仓库";
			head[5] = "导入人";
			head[6] = "导入日期";
			head[7] = "单据状态";
			List<Map<String, Object>> list = dao.queryAllPartStockInve(sbString.toString());
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[9];
						detail[0] = CommonUtils.checkNull(i+1);
						detail[1] = CommonUtils.checkNull(map
								.get("CHANGE_CODE"));
						detail[2] = CommonUtils.checkNull(map
								.get("RESULT_CODE"));
						int type = Integer.parseInt(CommonUtils.checkNull(map.get("CHECK_TYPE")).toString());
						if(type1 == type)
						{
							detail[3] = "全部";
						}
						else if(type2 == type)
						{
							detail[3] = "动态";
						}
						detail[4] = CommonUtils.checkNull(map
								.get("WH_CNAME"));
						detail[5] = CommonUtils
								.checkNull(map.get("NAME"));
						detail[6] = CommonUtils.checkNull(map
								.get("CREATE_DATE"));
						int orderState = Integer.parseInt(CommonUtils.checkNull(map.get("STATE")));
						if(orderState1 == orderState)
						{
							detail[7] = "已保存";
						}
						else if(orderState4 == orderState)
						{
							detail[7] = "已驳回";
						}
						list1.add(detail);
					}
				}
			}
			
			String fileName = "配件库存盘点调整申请信息";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出配件库存盘点调整申请");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	/**
	 * 
	 * @Title      : 文件导出为xls文件
	 * @param      : @param response
	 * @param      : @param request
	 * @param      : @param head
	 * @param      : @param list
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public static Object exportEx(String fileName, ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = fileName + ".xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize = list.size() / 30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
						/*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                     if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                       ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i].replace(",",""))));
                    }else{
                        ws.addCell(new Label(i, z, str[i]));
                    }
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
}
