package com.infodms.dms.actions.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.XpKxztsjPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.vehicle.AsSqlUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

/**
 * <ul>
 * <li>文件名称: AjaxSelectAction.java</li>
 * <li>文件描述:</li>
 * <li>版权所有: 版权所有(C)2012-2013</li>
 * <li>内容摘要:</li>
 * <li>完成日期: 2013-4-27 上午10:43:57</li>
 * <li>修改记录:</li>
 * </ul>
 * 
 * @version 1.0
 * @author wangsongwei
 */
public class AjaxSelectAction {
	
	private Logger logger = Logger.getLogger(AjaxSelectAction.class);
	
	private AjaxSelectDao dao = AjaxSelectDao.getInstance();
	
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	
	/**
	 * 方法描述 : 获取当前用户的职位下的所有产地列表
	 * 
	 * @author wangsongwei
	 */
	public void getYieldByPose()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try
		{
			List<Map<String, Object>> list = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString()); // 查询产地
			
			act.setOutData("info", list);
		}
		catch (Exception e)
		{
			// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述 ： 获取服务器时间<br/>
	 * 
	 * @author wangsongwei
	 */
	public void getServerTime()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try
		{
			String date = dao.getCurrentServerTime();
			act.setOutData("sysdate", date);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取服务器时间");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述 : 根据经销商ID获取联系地址列表
	 * 
	 * @author wangsongwei
	 */
	public void getAddressListByDealerId()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try
		{
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			
			if (dealerId.equals(""))
			{
				dealerId = logonUser.getDealerId();
			}
			
			List<Map<String, Object>> addressList = dao.getDealerAddressList(dealerId);
			act.setOutData("info", addressList);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getAddressListShowByDealerId(){
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try
		{
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			if (dealerId.equals(""))
			{
				dealerId = logonUser.getDealerId();
			}
			
			List<Map<String, Object>> addressList = dao.getAddressListShowByDealerId(dealerId,orderId);
			act.setOutData("info", addressList);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述 : 获取经销商地址的详细信息
	 * 
	 * @author wangsongwei
	 */
	public void getAddressInfo()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String addressId = CommonUtils.checkNull(request.getParamValue("addressId"));
			Map<String, Object> map = dao.getAddressInfo(addressId);
			
			act.setOutData("info", map);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述 : 获得经销商的某个产地所有资金类型总和
	 * 
	 * @author wangsongwei
	 */
	public void accountTotalInfo()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String dealerId = logonUser.getDealerId();
			if (CommonUtils.isEmpty(dealerId))
			{
				dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 　经销商ID
			}
			String yieldId = CommonUtils.checkNull(request.getParamValue("yieldly")); // 　产地ID
			Map<String, Object> ps = dao.countFinAccount(dealerId, yieldId);
			
			act.setOutData("info", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 方法描述 :　获取经销商具体某个产地的某个账户的资金信息
	 * 
	 * @author wangsongwei
	 */
	public void accountInfo()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String yieldId = CommonUtils.checkNull(request.getParamValue("yieldly")); // 　产地ID
			String finType = CommonUtils.checkNull(request.getParamValue("finType")); // 　账户类型
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 　账户类型
			
			dealerId = dealerId.equals("") ? logonUser.getDealerId().toString() : dealerId;
			Map<String, Object> ps = null;
			// 专款账户类型（银翔信贷）
			if (finType.equals("10251014")) {
				ps = dao.getAccCountInfoH2E(dealerId, yieldId, finType);
			} else {
				ps = dao.getAccCountInfo(dealerId, yieldId, finType);
			}
			
			act.setOutData("info", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取三方信贷银行账号
	 * 
	 * @author wangsongwei
	 */
	public void getThreedBank()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 　账户类型
			
			dealerId = dealerId.equals("") ? logonUser.getDealerId().toString() : dealerId;
			
			List<Map<String, Object>> ps = dao.getThreedBankInfo(dealerId);
			
			act.setOutData("info", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取三方信贷银行汇票
	 * 
	 * @author wangsongwei
	 */
	
	public void getBankBook()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String bankId = CommonUtils.checkNull(request.getParamValue("bankId")); // 　账户类型
			
			List<Map<String, Object>> ps = dao.getBankBookInfo(bankId);
			
			act.setOutData("info", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取品牌数据
	 */
	public void getBrandList()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			Map<String, Object> hsMap = new HashMap<String, Object>();
			hsMap.put("dealerId", logonUser.getDealerId());
			
			List<Map<String, Object>> list = dao.getOrderBrandList(hsMap);
			
			act.setOutData("info", list);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取物料组数据
	 */
	public void getGroupList()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String type = CommonUtils.checkNull(request.getParamValue("type"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String level = CommonUtils.checkNull(request.getParamValue("level"));
			String queryFlag = CommonUtils.checkNull(request.getParamValue("queryFlag"));
			
			Map<String, Object> hsMap = new HashMap<String, Object>();
			String dealerId = logonUser.getDealerId();
			hsMap.put("dealerId", dealerId);
			hsMap.put("groupId", groupId);
			hsMap.put("queryFlag", queryFlag);
			
			if (level.equals("1"))
			{
				List<Map<String, Object>> list = dao.getOrderBrandList(hsMap);
				act.setOutData("info", list);
			}
			else if (level.equals("2"))
			{
				List<Map<String, Object>> list = dao.getOrderSeriesList(hsMap);
				act.setOutData("info", list);
			}
			else if (level.equals("3"))
			{
				List<Map<String, Object>> list = dao.getOrderModelList(hsMap);
				act.setOutData("info", list);
			}
			else if (level.equals("4"))
			{
				List<Map<String, Object>> list = dao.getOrderPackageList(hsMap);
				act.setOutData("info", list);
			}
			else if (level.equals("5"))
			{
				List<Map<String, Object>> list = dao.getXpColorList(groupId, dealerId, queryFlag);
				act.setOutData("info", list);
			}
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询颜色和内饰
	 */
	public void getColorAndTrimList()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String type = CommonUtils.checkNull(request.getParamValue("type"));
			String packageId = CommonUtils.checkNull(request.getParamValue("packageId"));
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
			String colorCode = CommonUtils.checkNull(request.getParamValue("colorCode"));
			
			if (type.equals("0"))
			{
				List<Map<String, Object>> list = dao.getColorList(packageId);
				act.setOutData("info", list);
			}
			else if (type.equals("1"))
			{
				List<Map<String, Object>> list = dao.getTrimList(packageId, colorCode);
				act.setOutData("info", list);
			}
			
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询物料价格
	 */
	public void getMaterialPirce()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String packageId = CommonUtils.checkNull(request.getParamValue("packageId"));
			
			act.setOutData("PACKAGE_PRICE", dao.getOrderMaterialPrice(packageId, logonUser.getDealerId()));
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 加载物料资源数据
	 */
	public void getMaterialResource()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String materialId = CommonUtils.checkNull(request.getParamValue("materialId"));
			
			if (!materialId.equals(""))
			{
				act.setOutData("MATERIAL_RESOURCE", dao.getOrderMaterialResource(materialId));
			}
			else
			{
				act.setOutData("MATERIAL_RESOURCE", 0);
			}
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取物料信息，主要包括 ： 物料结算单价 = 经销商结算价 + 经销商折扣 物料的资源数量 = 车厂实际库存 - 已提报的订单明细汇总数
	 */
	public void getMaterialMessage()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String packageId = CommonUtils.checkNull(request.getParamValue("packageId"));
			String colorCode = CommonUtils.checkNull(request.getParamValue("colorCode"));
			
			List<Map<String, Object>> list = dao.getMaterialMessage(packageId, colorCode, logonUser.getDealerId());
			act.setOutData("info", list);
			// AsSqlUtils.getAsSqlVehicleResourceBuffer();
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取物料信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 获取组织关系数据
	 */
	public void getOrgList() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String level = CommonUtils.checkNull(request.getParamValue("level"));
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("orgid"));
			
			List<Map<String, Object>> list = dao.getOrgList(level, parentOrgId);
			
			act.setOutData("info", list);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "算产地资金类型总和提车单提报DLR");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void  getSndDealerList(){
		//DealerInfoDao dealerInfoDao = new DealerInfoDao();
		String dealer_code = CommonUtils.checkNull(request.getParamValue("dealer_code"));
		List<Map<String, Object>> dealerList = DealerInfoDao.getInstance().getDelByCode(dealer_code);
		act.setOutData("info", dealerList);
	}
	
	/**
	 * 取得结算价格-修改前使用（新版本发布前使用）
	 */
	public void getPackagePrice()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String packageId = CommonUtils.checkNull(request.getParamValue("packageId"));

			List<Map<String, Object>> list = dao.getDealerPackagePrice(packageId, logonUser.getDealerId());
			act.setOutData("info", list);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取配置价格");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 取得结算价格
	 */
	public void getPackageXpPrice() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String packageId = CommonUtils.checkNull(request.getParamValue("packageId"));
			String newCodes = CommonUtils.checkNull(request.getParamValue("newCodes"));
			
			// 物料组KSID取得
			TmVhclMaterialGroupPO tmVhclMaterialGroupPO = new TmVhclMaterialGroupPO();
			tmVhclMaterialGroupPO.setGroupId(Long.parseLong(packageId));
			tmVhclMaterialGroupPO.setStatus(Constant.STATUS_ENABLE);
			List<PO> tmVhclMaterialGroupPOLst = dao.select(tmVhclMaterialGroupPO);
			if (tmVhclMaterialGroupPOLst == null || tmVhclMaterialGroupPOLst.isEmpty()) {
				throw new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取配置价格");
			}
			String ksid = ((TmVhclMaterialGroupPO)tmVhclMaterialGroupPOLst.get(0)).getKsid();
			
			// 默认选配项CODES取得
			StringBuffer defaultItCodes = new StringBuffer();
			XpKxztsjPO xpKxztsjPO = new XpKxztsjPO();
			xpKxztsjPO = new XpKxztsjPO(); 
			xpKxztsjPO.setKsid(ksid);
			xpKxztsjPO.setStatus("1");
			xpKxztsjPO.setIsdef("1");
			List<PO> xpKxztsjPOLst = dao.select(xpKxztsjPO);
			if (xpKxztsjPOLst == null || xpKxztsjPOLst.isEmpty()) {
				throw new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取配置价格");
			}
			for (PO po : xpKxztsjPOLst) {
				defaultItCodes.append(((XpKxztsjPO)po).getItcode()).append(",");
			}
			
			// 默认选配价格取得
			List<Map<String, Object>> list = dao.getDealerPackagePrice(packageId, logonUser.getDealerId());
			if (list == null || list.isEmpty()) {
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "获取默认配置价格失败,请联系车厂维护默认选配的价格!");
			}
			
			// 该车结算价对应的商务政策是否有效CHECK
			// 结算价对应的商务政策查询
			String disrateId = (String) list.get(0).get("DISRATE_ID");
			List<Map<String, Object>> deployIdLst = dao.getDeployId(disrateId);
			// 商务政策是否有效
			if (deployIdLst != null && !deployIdLst.isEmpty()) {
				for (Map<String, Object> deployMap : deployIdLst) {
					String deployId = (String) deployMap.get("DEPLOY_ID");
					List<Map<String, Object>> deployLst = dao.getDeploy(deployId);
					if (deployLst == null || deployLst.isEmpty()) {
						throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "结算价对应的商务政策过期或者未发布,请联系车厂维护!");
					}
				}
			}
			
			
			// 当前选配项是否和默认选配项相同,如果相同则按默认选配价格标识，如果不同则重新计算选配价格
			String newItCodeArray[] = newCodes.split(",");
			List<String> newItCodes = new ArrayList<String>();
			boolean flag = true;
			for (int i = 0; i < newItCodeArray.length; i++) {
				if (defaultItCodes.indexOf(newItCodeArray[i]) == -1 && !StringUtils.equals(newItCodeArray[i].substring(0, 2), "97")) {
					newItCodes.add(newItCodeArray[i]);
					flag = false;
				}
			}
			if (flag) {
				act.setOutData("info", list);
			
			// 重新计算结算价
			} else {
				Map<String, Object> params = null;
				BigDecimal settlePrice = (BigDecimal) list.get(0).get("LAST_SETTLE_AMOUNT");
//				BigDecimal disRate = (BigDecimal) list.get(0).get("DIS_RATE");
//				BigDecimal discount = (new BigDecimal(100).subtract(disRate)).divide(new BigDecimal(100));
				for (String itcode : newItCodes) {
					
					// 取默认配置的价格
					String iccode = itcode.substring(0, 2);
					params = new HashMap<String, Object>();
					params.put("dealerId", logonUser.getDealerId());
					params.put("ksid", ksid);
					params.put("iccode", iccode);
					List<Map<String, Object>> xpPriceLst = dao.getSalesXpPrice(params);
					if (xpPriceLst == null || xpPriceLst.isEmpty()) {
						throw new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取默认配置价格");
					}
					BigDecimal defaultXpPrice = (BigDecimal) xpPriceLst.get(0).get("AMOUNT");
					if (defaultXpPrice == null) {
						throw new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取默认配置价格");
					}
					
					// 取新配置的价格
					params.clear();
					params.put("dealerId", logonUser.getDealerId());
					params.put("ksid", ksid);
					params.put("xpcode", itcode);
					xpPriceLst = dao.getSalesXpPrice(params);
					if (xpPriceLst == null || xpPriceLst.isEmpty()) {
						throw new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取新配置价格");
					}
					BigDecimal newXpPrice = (BigDecimal) xpPriceLst.get(0).get("AMOUNT");
					if (newXpPrice == null) {
						throw new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取新配置价格");
					}
					// 计算最终结算价格
					settlePrice = settlePrice.add(newXpPrice.subtract(defaultXpPrice));
				}
				list.get(0).put("LAST_SETTLE_AMOUNT", settlePrice);
				act.setOutData("info", list);
			}
			
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取配置价格");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取选配系统中的外饰颜色列表
	 */
	public void getXpColorInfo()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String packageId = CommonUtils.checkNull(request.getParamValue("packageId"));
			String colorCode = CommonUtils.checkNull(request.getParamValue("colorCode"));
			String queryFlag = CommonUtils.checkNull(request.getParamValue("queryFlag"));
			
			List<Map<String, Object>> list = dao.getXpColorList(packageId, logonUser.getUserId().toString(), queryFlag);
			act.setOutData("info", list);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取外饰颜色");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取选配默认选配代码
	 */
	public void getXpDefaultInfo()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String packageId = CommonUtils.checkNull(request.getParamValue("packageId"));
			String colorCode = CommonUtils.checkNull(request.getParamValue("colorCode"));
			String queryFlag = CommonUtils.checkNull(request.getParamValue("queryFlag"));
			String dealerId = logonUser.getDealerId();
			
			List<Map<String, Object>> list = dao.getXpDefaultList(packageId, colorCode, queryFlag, dealerId);
			String xpcode = "", xpname = "";
			for(Map<String, Object> map : list) {
				xpcode += map.get("ITCODE").toString() + ",";
				xpname += map.get("ITNAME").toString() + "/";
			}
			xpcode = xpcode.equals("") ? xpcode : xpcode.substring(0, xpcode.length() - 1);
			xpname = xpname.equals("") ? xpname : xpname.substring(0, xpname.length() - 1);
			
			act.setOutData("xpcode", xpcode);
			act.setOutData("xpname", xpname);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取外饰颜色");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取选配限制代码
	 */
	public void getXpLimitCodes() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String ksid = CommonUtils.checkNull(request.getParamValue("ksid"));
			String xpCode = CommonUtils.checkNull(request.getParamValue("xpCode"));
			
			List<Map<String, Object>> list = dao.getXpLimitCodesList(ksid, xpCode);
			act.setOutData("xzzt", list);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取选配限制代码");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取选配明细信息查询
	 */
	public void getXpDetailSet() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

		try
		{
			Map<String, Object> bigMap = new HashMap<String, Object>();
			Map<String, Object> smallMap = new HashMap<String, Object>();
			Map<String, Object> detailMap = new HashMap<String, Object>();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			
			String packageId = CommonUtils.checkNull(request.getParamValue("packageId"));
			String xpCode = CommonUtils.checkNull(request.getParamValue("xpCode"));
			String gysCode = CommonUtils.checkNull(request.getParamValue("gysCode"));
			String timeId = CommonUtils.checkNull(request.getParamValue("timeId"));
			String dealerId = logonUser.getDealerId();

			StringBuffer htmlBuf = new StringBuffer();
			StringBuffer bufTmp1 = new StringBuffer();
			StringBuffer bufTmp2 = new StringBuffer();
			
			List<Object> params = new ArrayList<Object>();
			StringBuffer sbSql = new StringBuffer();
			
			// 通过配置ID查询出配置编码
			sbSql.append("SELECT A.GROUP_CODE, A.KSID FROM TM_VHCL_MATERIAL_GROUP A WHERE A.GROUP_LEVEL = 4 AND a.GROUP_ID = ?"); 
			params.add(packageId);
			String ksid = CommonUtils.checkNull(dao.pageQueryMap(sbSql.toString(), params, dao.getFunName()).get("KSID"));
			
			// 通过配置编码得到选配系统配置唯一主键
			if(!ksid.equals("")) {
				/* 得到配置的最大类型项，如动力类、空调系统等 */
				sbSql.delete(0, sbSql.length()); params.clear();
				sbSql.append("SELECT BIGCODE, BIGNAME\n");
				sbSql.append("  FROM XP_KXZTSJ\n");
				sbSql.append(" WHERE KSID = ? AND ICNAME <> '外饰颜色' \n");
				sbSql.append(AsSqlUtils.getDealerPackageXpBuffer(null, dealerId));
				sbSql.append(" GROUP BY BIGCODE, BIGNAME\n");
				sbSql.append(" ORDER BY BIGCODE"); 
				params.add(ksid);
				List<Map<String, Object>> bigTypeList = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
				
				/* 获取选配数据表 */
				sbSql.delete(0, sbSql.length());
				sbSql.append("-- 配置小类\n");
				sbSql.append("SELECT BIGNAME, BIGCODE, ICCODE, ICNAME\n");
				sbSql.append("  FROM XP_KXZTSJ\n");
				sbSql.append(" WHERE KSID = ? AND ICNAME <> '外饰颜色'\n");
				sbSql.append(AsSqlUtils.getDealerPackageXpBuffer(null, dealerId));
				sbSql.append(" GROUP BY BIGNAME, BIGCODE, ICCODE, ICNAME\n");
				sbSql.append(" ORDER BY BIGCODE, ICCODE"); 
				List<Map<String, Object>> smallList = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
				
				/* 得到选配数据的明细项 */
				sbSql.delete(0, sbSql.length());
				sbSql.append("-- 配置明细\n");
				sbSql.append("SELECT *\n");
				sbSql.append("  FROM XP_KXZTSJ\n");
				sbSql.append(" WHERE KSID = ? AND ICNAME <> '外饰颜色' \n");
				sbSql.append(AsSqlUtils.getDealerPackageXpBuffer(null, dealerId));
				sbSql.append(" ORDER BY BIGCODE, ICCODE, ITCODE"); 
				List<Map<String, Object>> detailList = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
				
				/* 选配销售部限制项 */
				String xpxz = dao.pageQueryMap("SELECT za_concat(ICCODE) ICCODE FROM XP_SALES_LIMIT WHERE IS_ENABLE = '0'", null, dao.getFunName()).get("ICCODE").toString();
				
				/* 选配状态限制表 */
				String bxzt = xpCode.equals("") ? "" : xpCode + ",", wxzt = ""; 
				
				/* 数据分析前，先根据已选的xpCode、可选状态的默认项查询出所有必选和受限制的选配数据 */
				String bxcodes = "", disabledcodes = "";
				
				for(Map<String, Object> bMap : bigTypeList) {
					String bTypeCode = bMap.get("BIGCODE").toString();
					String bTypeName = bMap.get("BIGNAME").toString();
					boolean bTypeDisplay = true;
					
					bufTmp1.delete(0, bufTmp1.length());  // 清除临时页面代码
					for(Map<String, Object> sMap : smallList) {
						String sTypeCode = sMap.get("ICCODE").toString();
						String sTypeName = sMap.get("ICNAME").toString();
						String compareBTypeCode = sMap.get("BIGCODE").toString();
						boolean sTypeDisplay = true;
						boolean ischecked = false;
						if(compareBTypeCode.equals(bTypeCode)) {
							bufTmp2.delete(0, bufTmp2.length());	// 清除临时页面代码
							for(Map<String, Object> dMap : detailList) {
								String compareSTypeCode = dMap.get("ICCODE").toString();
								if(compareSTypeCode.equals(sTypeCode)) {
									sTypeDisplay = false;
									
									String dcode = dMap.get("ITCODE").toString(); // 选配代码
									String dname = dMap.get("ITNAME").toString(); // 选配名称
									String ismulit = dMap.get("ICMULIT").toString(); 
									String type = ismulit.equals("1") ? "checkbox" : "radio"; // 是否多选
									String isdef = dMap.get("ISDEF").toString(); // 是否默认选中
									String checked = ""; // 选中状态
									
									/* --------------------------------------------
									 * 1、判断当前选配是否在页面的选择项中
									 * 2、判断当前选配是否受到限制
									 * 2、如果没受限制，再判断是否该状态已选或默认选中
									 * -------------------------------------------*/
									if(xpCode.contains(dcode) || (isdef.equals("1") && !ischecked)) {
										checked = "checked"; 
										ischecked = true;
										// 查询该选配项是否限制了其他状态
										sbSql.delete(0, sbSql.length()); params.clear();
										params.add(ksid); params.add(dcode);
										sbSql.append("SELECT * FROM XP_ZTXZSJ WHERE KSID = ? ITCODE = ? GROUP BY XZCODE"); 
										List<Map<String, Object>> zList = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
										
										for(Map<String, Object> zMap : zList) {
											String xzcode = zMap.get("XZCODE").toString();
											String flag = zMap.get("FLAG").toString();
											
											if(flag.equals("1")) {
												bxcodes += xzcode + ",";
											} else {
												disabledcodes += disabledcodes + ",";
											}
										}
									}
									
									bufTmp2.append("<td width='20'>");
									bufTmp2.append("	<input type='"+type+"' <!--"+dcode+"_disabled--> <!--"+dcode+"_is_bx--> "+checked+" name='"+sTypeCode+"' ondblclick=\"alert('1');\" onclick=\"getItCodeLimit('"+dcode+"','"+ksid+"')\" value='"+dcode+","+dname+"'/>");
									bufTmp2.append("</td>");
								}
							}
						}
						if(!sTypeDisplay) {
							bTypeDisplay = false;
							smallMap.put(bTypeCode, new String[]{sTypeCode, sTypeName});
							
							bufTmp1.append("<tr>");
							bufTmp1.append("	<td>");
							bufTmp1.append("		<table class='table_query' bordercolor='#DAE0EE'>");
							bufTmp1.append("			<tr>");
							bufTmp1.append("				<td width='90' style='background-color:#cccccc; text-align:center;'>"+sTypeName+"</td>");
							bufTmp1.append("				<td>");
							bufTmp1.append("					<table class='table_query' bordercolor='#DAE0EE'>");
							bufTmp1.append(bufTmp2);
							bufTmp1.append("					</table>");
							bufTmp1.append("				</td>");
							bufTmp1.append("			</tr>");
							bufTmp1.append("		</table>");
							bufTmp1.append("	</td>");
							bufTmp1.append("</tr>");
						}
					}
					if(!bTypeDisplay) {
						bigMap.put("xpMap", new String[]{bTypeCode, bTypeName});
						
						htmlBuf.append("<tr>");
						htmlBuf.append("	<th align='left' colspan='2'><img class='nav' src='"+request.getContextPath()+"/img/subNav.gif' />");
						htmlBuf.append("		" + bTypeName);
						htmlBuf.append("	</th>");
						htmlBuf.append("</tr>");
						htmlBuf.append(bufTmp1);
					}
				}
				
				// 供应商类别
				sbSql.delete(0, sbSql.length()); params.clear();
				sbSql.append("SELECT PPCODE, PPNAME FROM XP_GYSSJ WHERE STATUS = '1' AND KSID = ? GROUP BY PPCODE, PPNAME");
				params.add(ksid);
				List<Map<String, Object>> gyslbList = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
				
				// 供应商明细
				sbSql.delete(0, sbSql.length()); params.clear();
				sbSql.append("SELECT * FROM XP_GYSSJ WHERE STATUS = '1' AND KSID = ?");
				params.add(ksid);
				List<Map<String, Object>> gysList = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
				
				htmlBuf.append("<tr>");
				htmlBuf.append("	<th align='left' colspan='2'><img class='nav' src='"+request.getContextPath()+"/img/subNav.gif' />");
				htmlBuf.append("		指定供应商");
				htmlBuf.append("	</th>");
				htmlBuf.append("</tr>");
				htmlBuf.append("<tr><td><table class='table_query' bordercolor='#DAE0EE'>");
				for(Map<String, Object> map : gyslbList) {
					htmlBuf.append("<tr><td width='90' style='background-color:#cccccc; text-align:center;'>"+map.get("PPNAME")+"</td>");
					htmlBuf.append("<td><select name='g"+map.get("PPCODE")+"'>");
					for(Map<String, Object> map1 : gysList) {
						if(map.get("PPCODE").toString().equals(map1.get("PPCODE").toString())) {
							String selected = gysCode.contains(map1.get("PPDECODE").toString() + ",") ? "selected" : "";
							htmlBuf.append("<option value='"+map1.get("PPDECODE")+"' "+selected+"/>"+map1.get("PPDENAME")+"</option>");
						}
					}
					htmlBuf.append("</td></tr>");
				}
				htmlBuf.append("</table></td></tr>");
				
				String html = htmlBuf.toString();
				
				html = html.equals("") ? "无选配项" : html;
				
				for(String bxcode : bxcodes.split(",")) {
					html.toString().replaceAll("<!--"+bxcode+"_is_bx-->", "is_bx = '1'");
				}
				
				for(String disabledcode : disabledcodes.split(",")) {
					html.toString().replaceAll("<!--"+disabledcode+"_disabled-->", "disabled = 'disabled'");
				}
				
				act.setOutData("packageId", packageId);
				act.setOutData("timeId", timeId);
				act.setOutData("wscolor", xpCode);
				act.setOutData("bxzt", bxzt);
				act.setOutData("wxzt", wxzt);
				act.setOutData("html", html);
			}
			else
			{
				act.setOutData("html", "无选配项");
			}
			act.setForword("/jsp/sales/ordermanage/extractionofvehicle/xpselect/xpdetail.jsp");
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "选配明细信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getModelTypePrice()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));	
			
			act.setOutData("price", dao.getModelTypePrice(seriesId));
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "乘商用车价格查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询经销商可用保证金以及额度
	 */
	public void getDealerEdAndBzj() 
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			act.setOutData("info", dao.getDealerEdAndBzj(logonUser.getDealerId()));
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "乘商用车价格查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
