package com.infodms.dms.actions.sysmng.dealer;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.dealer.DealerAddressInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
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
 * @Company:  www.infoservice.com.cn
 * @Date: 2014-3-4
 *
 * @author yupeng 
 * @mail  
 * @version 1.0
 * @remark 
 */
public class DealerAddressInfo {
	public Logger logger = Logger.getLogger(DealerAddressInfo.class);
	
	private final String queryDealerAddressUrl = "/jsp/systemMng/dealer/dealerAddressVindicate.jsp";
	private final String queryDealerCsAddressUrl = "/jsp/systemMng/dealer/dealerCsAddressVindicate.jsp";
	
	/**
	 * 经销商收货地址维护页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryDealerAddressInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String dealer_id = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String sex = CommonUtils.checkNull(request.getParamValue("SEX"));
			String address_type = CommonUtils.checkNull(request.getParamValue("ADDRESS_TYPE"));
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));
			String link_man = CommonUtils.checkNull(request.getParamValue("LINK_MAN"));
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
			String mobile_phone = CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE"));
			String province = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));
			String city = CommonUtils.checkNull(request.getParamValue("CITY_ID"));
			String area = CommonUtils.checkNull(request.getParamValue("AREA_ID"));
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.valueOf(dealer_id));
			TmDealerPO po = (TmDealerPO) DealerAddressInfoDao.getInstance().select(dealerPO).get(0);
			act.setOutData("dearlerCode", po.getDealerCode());
			act.setOutData("dearlerName", po.getDealerName());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerAddressInfoDao.getInstance()
					.queryDealerAddressInfo(dealer_id, sex, address_type, status, link_man, tel, mobile_phone, province, city, area, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商地址维护查询结果");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void querySHDealerAddressInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String dealer_id = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String sex = CommonUtils.checkNull(request.getParamValue("SEX"));
			String address_type = CommonUtils.checkNull(request.getParamValue("ADDRESS_TYPE"));
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));
			String link_man = CommonUtils.checkNull(request.getParamValue("LINK_MAN"));
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
			String mobile_phone = CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE"));
			String province = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));
			String city = CommonUtils.checkNull(request.getParamValue("CITY_ID"));
			String area = CommonUtils.checkNull(request.getParamValue("AREA_ID"));
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.valueOf(dealer_id));
			TmDealerPO po = (TmDealerPO) DealerAddressInfoDao.getInstance().select(dealerPO).get(0);
			act.setOutData("dearlerCode", po.getDealerCode());
			act.setOutData("dearlerName", po.getDealerName());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerAddressInfoDao.getInstance()
					.querySHDealerAddressInfo(dealer_id, sex, address_type, status, link_man, tel, mobile_phone, province, city, area, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商地址维护查询结果");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商收货地址添加
	 * @param null
	 * @return void
	 * @throws Exception
	 */	
	public void addNewDealerAddress() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String dealer_id =  CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String id = SequenceManager.getSequence("");
			String link_man = CommonUtils.checkNull(request.getParamValue("LINK_MAN"));
			String sex = CommonUtils.checkNull(request.getParamValue("SEX"));
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
			String mobile_phone = CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE"));
			String address_type = CommonUtils.checkNull(request.getParamValue("ADDRESS_TYPE"));
			String address = CommonUtils.checkNull(request.getParamValue("ADDRESS"));
			String province_id = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));
			String city_id = CommonUtils.checkNull(request.getParamValue("CITY_ID"));
			String area_id = CommonUtils.checkNull(request.getParamValue("AREA_ID"));
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));
			
			DealerAddressInfoDao.getInstance().addNewDealerAddressInfo(id, dealer_id, 
					link_man, sex, tel, mobile_phone, address_type, address, province_id, city_id, area_id, status);  
			act.setOutData("DEALER_ID", dealer_id);
			act.setForword(queryDealerAddressUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商地址添加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商地址根据id查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */	
	public void queryDealerAddressInfoById() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String id =  CommonUtils.checkNull(request.getParamValue("ID"));
			String dealer_id = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String address_type = CommonUtils.checkNull(request.getParamValue("ADDRESS_TYPE"));
			
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.valueOf(dealer_id));
			TmDealerPO po = (TmDealerPO) DealerAddressInfoDao.getInstance().select(dealerPO).get(0);
			act.setOutData("dearlerCode", po.getDealerCode());
			act.setOutData("dearlerName", po.getDealerName());
			
			List<Map<String, Object>> list = DealerAddressInfoDao.getInstance().queryDealerAddressInfoById(id, address_type);  
			act.setOutData("list", list);
			act.setOutData("DECLAER_ID", dealer_id);
			act.setForword(queryDealerAddressUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商地址根据id查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商收货地址修改
	 * @param null
	 * @return void
	 * @throws Exception
	 */	
	public void updateDealerAddressInfo() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String dealer_id = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			
			String id = CommonUtils.checkNull(request.getParamValue("ID"));
			String link_man = CommonUtils.checkNull(request.getParamValue("LINK_MAN"));
			String sex = CommonUtils.checkNull(request.getParamValue("SEX"));
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
			String mobile_phone = CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE"));
			String address_type = CommonUtils.checkNull(request.getParamValue("ADDRESS_TYPE"));
			String address = CommonUtils.checkNull(request.getParamValue("ADDRESS"));
			String province_id = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));
			String city_id = CommonUtils.checkNull(request.getParamValue("CITY_ID"));
			String area_id = CommonUtils.checkNull(request.getParamValue("AREA_ID"));
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));
			
			DealerAddressInfoDao.getInstance().updateDealerAddressInfo(id, link_man, sex, tel, mobile_phone,
					address_type, address, province_id, city_id, area_id, status);  

			act.setOutData("DECLAER_ID", dealer_id);
			act.setForword(queryDealerAddressUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商收货地址修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后经销商收货地址维护页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryDealeCsrAddressInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String dealer_id = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String gender = CommonUtils.checkNull(request.getParamValue("GENDER"));
			String linkman = CommonUtils.checkNull(request.getParamValue("LINKMAN"));
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
			String mobile_phone = CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE"));
			String address_type = CommonUtils.checkNull(request.getParamValue("SHOU_ADDRESS_TYPE"));
			String state = CommonUtils.checkNull(request.getParamValue("STATUS"));
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.valueOf(dealer_id));
			TmDealerPO po = (TmDealerPO) DealerAddressInfoDao.getInstance().select(dealerPO).get(0);
			act.setOutData("dearlerCode", po.getDealerCode());
			act.setOutData("dearlerName", po.getDealerName());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = DealerAddressInfoDao.getInstance()
					.queryDealerCsAddressInfo(dealer_id, linkman, gender, tel, mobile_phone, curPage, Constant.PAGE_SIZE,address_type,state);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"售后经销商地址维护查询结果");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后经销商收货地址添加
	 * @param null
	 * @return void
	 * @throws Exception
	 */	
	public void addNewDealerCsAddress() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String dealer_id =  CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String addressType =  CommonUtils.checkNull(request.getParamValue("SHOU_ADDRESS_TYPE"));
			String STATUS =  CommonUtils.checkNull(request.getParamValue("STATUS"));
			String addr_id = SequenceManager.getSequence("");
			String linkman = CommonUtils.checkNull(request.getParamValue("LINKMAN"));
			String gender = CommonUtils.checkNull(request.getParamValue("GENDER"));
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
			String mobile_phone = CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE"));
			String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));
			
			String dearlerCode = CommonUtils.checkNull(request.getParamValue("dearlerCode"));
			String dearlerName = CommonUtils.checkNull(request.getParamValue("dearlerName"));
			
			DealerAddressInfoDao.getInstance().addNewDealerCsAddressInfo(addr_id, dealer_id, 
					linkman, gender, tel, mobile_phone, addr,addressType,STATUS,dearlerCode,dearlerName);  
			act.setOutData("DEALER_ID", dealer_id);
			act.setForword(queryDealerCsAddressUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"售后经销商地址添加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后经销商地址根据id查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */	
	public void queryDealerCsAddressInfoById() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String addr_id =  CommonUtils.checkNull(request.getParamValue("ADDR_ID"));
			String dealer_id = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String address_type = CommonUtils.checkNull(request.getParamValue("address_type"));
			if(address_type.equals("null")){
				address_type="20491001";
			}
			TmDealerPO dealerPO = new TmDealerPO();
			dealerPO.setDealerId(Long.valueOf(dealer_id));
			TmDealerPO po = (TmDealerPO) DealerAddressInfoDao.getInstance().select(dealerPO).get(0);
			act.setOutData("dearlerCode", po.getDealerCode());
			act.setOutData("dearlerName", po.getDealerName());
			List<Map<String, Object>> list = DealerAddressInfoDao.getInstance().queryDealerCsAddressInfoById(addr_id,address_type);  
			act.setOutData("list", list);
			act.setOutData("DECLAER_ID", dealer_id);
			act.setOutData("disabled", "yes");
			act.setForword(queryDealerCsAddressUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"售后经销商地址根据id查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后经销商收件地址修改
	 * @param null
	 * @return void
	 * @throws Exception
	 */	
	public void updateDealerCsAddressInfo() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String dealer_id = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			
			String addr_id = CommonUtils.checkNull(request.getParamValue("ADDR_ID"));
			String linkman = CommonUtils.checkNull(request.getParamValue("LINKMAN"));
			String gender = CommonUtils.checkNull(request.getParamValue("GENDER"));
			String tel = CommonUtils.checkNull(request.getParamValue("TEL"));
			String mobile_phone = CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE"));
			String addr = CommonUtils.checkNull(request.getParamValue("ADDR"));
			String addressType = CommonUtils.checkNull(request.getParamValue("SHOU_ADDRESS_TYPE_HIDDEN"));
			String state = CommonUtils.checkNull(request.getParamValue("STATUS"));
			
			DealerAddressInfoDao.getInstance().updateDealerCsAddressInfo(addr_id, linkman, gender, tel, mobile_phone, addr, addressType,state);  

			act.setOutData("DECLAER_ID", dealer_id);
			act.setForword(queryDealerCsAddressUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"售后经销商配件地址修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
