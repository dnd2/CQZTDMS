package com.infodms.dms.actions.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsOrderPaymentPriceLogPO;
import com.infodms.dms.po.TtVsOrderPaymentPricePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**   
 * @Title  : ExtendedOrderPriceMaintenance.java
 * @Package: com.infodms.dms.actions.sysbusinesparams.businesparamsmanage
 * @Description: 超期常规订单资源单价维护
 * @date   : 2013-1-22
 * @version: V1.0   
 */
@SuppressWarnings("rawtypes")
public class ExtendedOrderPriceMaintenance extends BaseDao{

	public Logger logger = Logger.getLogger(ExtendedOrderPriceMaintenance.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final ExtendedOrderPriceMaintenance dao = new ExtendedOrderPriceMaintenance ();
	
	private final String  initUrl = "/jsp/sysbusinesparams/businesparamsmanage/extendedOrderPriceManageInit.jsp";
	private final String  detailUrl = "/jsp/sysbusinesparams/businesparamsmanage/extendedOrderPriceInfo.jsp";
	private final String  addUrl = "/jsp/sysbusinesparams/businesparamsmanage/addExtendedOrderPriceInfo.jsp";
	
	/** 
	* @Title	  : extendedOrderPriceManageInit 
	* @Description: 超期常规订单资源单价维护页面初始化
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	public void extendedOrderPriceManageInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List years = dao.queryAllYear();
			act.setOutData("years", years);
			act.setForword(initUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "超期常规订单资源单价维护页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : getCascadeMonth 
	* @Description: 根据年份获取级联月份
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	public void getCascadeMonth() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String year = request.getParamValue("year");
			List months = dao.queryAllMonth(year);
			act.setOutData("months", months);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "超期常规订单资源单价维护页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	


	/** 
	* @Title	  : extendedOrderPriceInfo 
	* @Description: 查询超期常规订单资源单价列表
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	public void extendedOrderPriceList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String year = request.getParamValue("year");
			String month = request.getParamValue("month");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页
			PageResult<TtVsOrderPaymentPricePO> ps = dao.getOrderPriceList(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询超期常规订单资源单价列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	

	/** 
	* @Title	  : extendedOrderPriceInfo 
	* @Description: 查询超期常规订单资源单价
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	@SuppressWarnings("unchecked")
	public void extendedOrderPriceInfo(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String priceId = request.getParamValue("priceId");
			TtVsOrderPaymentPricePO val = null;
			if(priceId != null && priceId.length() != 0) {
				TtVsOrderPaymentPricePO priceCondition = new TtVsOrderPaymentPricePO();
				priceCondition.setPriceId(Long.valueOf(priceId));
				List<TtVsOrderPaymentPricePO> priceList = dao.select(priceCondition);
				if(priceList != null && priceList.size() != 0) {
					val = priceList.get(0);
				}
			}
			//TODO 显示变更历史记录
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", val.getPaymentYear());
			map.put("month", val.getPaymentMonth());
			List logs = dao.getHistoryLog(map);
			act.setOutData("logs", logs);
			act.setOutData("val", val);
			act.setForword(detailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询超期常规订单资源单价");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : extendedOrderPriceInfo 
	* @Description: 更新超期常规订单资源单价
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	@SuppressWarnings("unchecked")
	public void updateExtendedOrderPrice(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String priceId = request.getParamValue("priceId");
			String price = request.getParamValue("price");
			String remark = request.getParamValue("remark");
			TtVsOrderPaymentPricePO val = null;
			if(priceId != null && priceId.length() != 0) {
				TtVsOrderPaymentPricePO priceCondition = new TtVsOrderPaymentPricePO();
				priceCondition.setPriceId(Long.valueOf(priceId));
				List<TtVsOrderPaymentPricePO> priceList = dao.select(priceCondition);
				if(priceList != null && priceList.size() != 0) {
					val = priceList.get(0);
					if(price != null && price.length() != 0) {
						val.setPaymentPrice(Double.valueOf(price));
						val.setUpdateBy(logonUser.getUserId());
						val.setUpdateDate(new Date());
						dao.update(priceCondition, val);
						TtVsOrderPaymentPriceLogPO log = new TtVsOrderPaymentPriceLogPO();
						Long logId = CommonUtils.parseLong(SequenceManager.getSequence(""));
						log.setLogId(logId);
						log.setPaymentPrice(Double.valueOf(price));
						log.setPaymentYear(val.getPaymentYear());
						log.setPaymentMonth(val.getPaymentMonth());
						log.setUpdateBy(logonUser.getUserId());
						log.setRemark(remark);
						log.setUpdateDate(new Date());
						dao.insert(log);
					} else {
						throw new RuntimeException("超期资源单价不能为空!");
					}
					
				} else {
					throw new RuntimeException("选择的年份和月份的超期资源单价不存在!");
				}
			}
			extendedOrderPriceManageInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询超期常规订单资源单价");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/** 
	* @Title	  : extendedOrderPriceInfo 
	* @Description: 新增超期常规订单资源单价初始化
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	@SuppressWarnings("unchecked")
	public void addExtendedOrderPriceInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String year = request.getParamValue("year");
			String month = request.getParamValue("month");
			List<TtVsOrderPaymentPricePO> priceList = null;
			if(year != null && year.length() != 0 && month != null && month.length() != 0) {
				TtVsOrderPaymentPricePO priceCondition = new TtVsOrderPaymentPricePO();
				priceCondition.setPaymentYear(Integer.parseInt(year));
				priceCondition.setPaymentMonth(Integer.parseInt(month));
				priceList = dao.select(priceCondition);
				if(priceList != null && priceList.size() != 0) {
					act.setOutData("msg", "选择的年份和月份已有超期资源单价!");
				} 
			} else {
				act.setOutData("msg", "请选择正确的年份和月份!");
			}
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(addUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增超期常规订单资源单价");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 
	}
	
	/** 
	* @Title	  : extendedOrderPriceInfo 
	* @Description: 新增超期常规订单资源单价保存
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	@SuppressWarnings("unchecked")
	public void addExtendedOrderPrice(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String year = request.getParamValue("year");
			String month = request.getParamValue("month");
			String price = request.getParamValue("price");
			//String remark = request.getParamValue("remark");
			if(year != null && year.length() != 0 && month != null && month.length() != 0 && price != null && price.length() != 0) {
				TtVsOrderPaymentPricePO priceCondition = new TtVsOrderPaymentPricePO();
				Long priceId = CommonUtils.parseLong(SequenceManager.getSequence(""));
				priceCondition.setPaymentPrice(Double.valueOf(price));
				priceCondition.setPriceId(priceId);
				priceCondition.setPaymentYear(Integer.parseInt(year));
				priceCondition.setPaymentMonth(Integer.parseInt(month));
				priceCondition.setCreateBy(logonUser.getUserId());
				priceCondition.setCreateDate(new Date());
				dao.insert(priceCondition);
				/*TtVsOrderPaymentPriceLogPO log = new TtVsOrderPaymentPriceLogPO();
				Long logId = CommonUtils.parseLong(SequenceManager.getSequence(""));
				log.setLogId(logId);
				log.setPaymentPrice(Double.valueOf(price));
				log.setPaymentYear(Integer.valueOf(year));
				log.setPaymentMonth(Integer.valueOf(month));
				log.setUpdateBy(logonUser.getUserId());
				log.setRemark(remark);
				log.setUpdateDate(new Date());
				dao.insert(log);*/
			} else {
				throw new RuntimeException("请填写正确的年份和月份!");
			}
			extendedOrderPriceManageInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增超期常规订单资源单价");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 
	}
	
	/** 
	* @Title	  : delExtendedOrderPriceInfo 
	* @Description: 根据priceId删除资源单价
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	/*
	public void delExtendedOrderPriceInfo() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String priceId = request.getParamValue("priceId");
			if(priceId != null && priceId.length() != 0) {
				TtVsOrderPaymentPricePO priceCondition = new TtVsOrderPaymentPricePO();
				priceCondition.setPriceId(Long.valueOf(priceId));
				dao.delete(priceCondition);
			}
			act.setForword(detailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "删除超期常规订单资源单价");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}*/
	
	/** 
	* @Title	  : getOrderPriceList 
	* @Description: 根据年份和月份查询资源单价列表
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	@SuppressWarnings("unchecked")
	private PageResult<TtVsOrderPaymentPricePO> getOrderPriceList(Map<String, Object> map, int pageSize, int curPage) {
		String year = (String) map.get("year");
		String month = (String) map.get("month");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVOPR.PRICE_ID,\n");
		sql.append("       TVOPR.PAYMENT_YEAR,\n");  
		sql.append("       TVOPR.PAYMENT_MONTH,\n");  
		sql.append("       TVOPR.PAYMENT_PRICE\n");  
		sql.append("  FROM TT_VS_ORDER_PAYMENT_PRICE TVOPR\n");  
		sql.append(" WHERE 1 = 1\n");  
		if(year != null && year.length() != 0) {
			sql.append("   AND TVOPR.PAYMENT_YEAR = ");
			sql.append(year);
			sql.append("\n");  
		}
		if(month != null && month.length() != 0) {
			sql.append("   AND TVOPR.PAYMENT_MONTH = ");
			sql.append(month);
			sql.append("\n");  
		}
		sql.append("ORDER BY TVOPR.PAYMENT_YEAR, TVOPR.PAYMENT_MONTH");
		PageResult<TtVsOrderPaymentPricePO> ps = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	
	/** 
	* @Title	  : queryAllYear 
	* @Description: 查询tm_date_set中所有年份
	* @throws 
	* @LastUpdate :2013-1-23
	* @return List 年份列表
	*/
	@SuppressWarnings("unchecked")
	private List queryAllYear() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT TDS.SET_YEAR \n");
		sql.append("  FROM TM_DATE_SET TDS\n");  
		sql.append(" ORDER BY TDS.SET_YEAR \n");  
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	/** 
	* @Title	  : queryAllMonth 
	* @Description: 根据年份级联查询所有月份
	* @throws 
	* @LastUpdate :2013-1-23
	* @param year 年月
	* @return List 月份列表
	*/
	@SuppressWarnings("unchecked")
	private List queryAllMonth(String year) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT tds.set_month\n");
		sql.append("  FROM TM_DATE_SET TDS\n");  
		sql.append("  WHERE  1 = 1\n");  
		if(year != null && year.length() != 0) {
			sql.append("  AND tds.set_year = ");
			sql.append(year);
			sql.append("\n");
		}
		sql.append(" ORDER BY to_char(to_date(tds.set_month, 'MM'), 'MM')\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	/** 
	* @Title	  : getHistoryLog 
	* @Description: 根据年份和月份查询变更的历史记录
	* @throws 
	* @LastUpdate :2013-1-23
	*/
	@SuppressWarnings("unchecked")
	private List getHistoryLog(Map<String, Object> map) {
		Integer year = (Integer) map.get("year");
		Integer month = (Integer) map.get("month");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT L.PAYMENT_YEAR, L.PAYMENT_MONTH, L.PAYMENT_PRICE, L.REMARK, to_char(L.UPDATE_DATE, 'yyyy-MM-dd HH24:MI:SS') UPDATE_DATE, TU.NAME UPDATE_NAME\n");
		sql.append("  FROM TT_VS_ORDER_PAYMENT_PRICE_LOG L, TC_USER TU\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append(" AND TU.USER_ID = L.UPDATE_BY\n");
		if(year != null) {
			sql.append("   AND L.PAYMENT_YEAR = ");
			sql.append(year);
			sql.append("\n");  
		}
		if(month != null) {
			sql.append("   AND L.PAYMENT_MONTH = ");
			sql.append(month);
			sql.append("\n");  
		}
		sql.append("ORDER BY L.UPDATE_DATE DESC");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	
	/** 
	* @Title	  : validate 
	* @Description: 服务器端参数公共验证方法
	* @throws 
	* @LastUpdate :2013-1-22
	*/
	public void validate() {
		Enumeration<String> names = request.getParamNames();
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getParamValue(name);
			if(value == null || value.length() == 0) {
				act.setOutData("errorField", name);
				break;
			};
		}
	}
}
