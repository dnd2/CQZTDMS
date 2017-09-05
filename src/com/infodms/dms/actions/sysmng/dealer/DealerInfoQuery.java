package com.infodms.dms.actions.sysmng.dealer;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtDownloadLogPO;
import com.infodms.dms.po.TtProxyAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

/**
 * @Title: CHANADMS
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company:  www.infoservice.com.cn
 * @Date: 2010-7-5
 *
 * @author zjy 
 * @mail   zhaojinyu@infoservice.com.cn
 * @version 1.0
 * @remark 
 */
public class DealerInfoQuery {
	public Logger logger = Logger.getLogger(DealerInfoQuery.class);
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
	private final String queryDealerInitUrl = "/jsp/systemMng/dealer/dealerInfoQry.jsp";
	private final String serviceQueryDealerInitUrl = "/jsp/systemMng/dealer/serviceDealerInfoQry.jsp";
	private final String serviceQueryDealerInitUrl2nd = "/jsp/systemMng/dealer/serviceDealerInfoQry2nd.jsp";
	private final String queryDealerCsInitUrl = "/jsp/systemMng/dealer/dealerCsInfoQry.jsp";
	private final String queryDealerInitDetailUrl="/jsp/systemMng/dealer/dealerInfoQryDetail.jsp";
	private final String queryDealerInitDetail2ndUrl="/jsp/systemMng/dealer/dealerInfoQryDetail2nd.jsp";
	private final String queryDealerCsInitDetailUrl="/jsp/systemMng/dealer/dealerCsInfoQryDetail.jsp";
	private POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * 经销商维护查询页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryDealerInfoInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ProductManageDao dao = new ProductManageDao();
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			List<Map<String, Object>> brand=dao.getbrandList();//品牌
			act.setOutData("brand", brand);		
			List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
			String dealerFlag = "0";
			if(null!=logonUser && null !=logonUser.getDealerId()){
				dealerFlag = "1";
			}
			act.setOutData("dealerFlag", dealerFlag);
			act.setForword(queryDealerInitUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护pre查询");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
	
	public void queryServiceDealerInfoInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			if(logonUser.getDealerId()==null){
				act.setOutData("isDealer", "yes");
			}else{
				act.setOutData("isDealer", "no");
			}
			ProductManageDao dao = new ProductManageDao();
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			List<Map<String, Object>> brand=dao.getbrandList();//品牌
			act.setOutData("brand", brand);		
			List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
			act.setForword(serviceQueryDealerInitUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护pre查询");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
	
	public void queryServiceDealerInfo2ndInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			if(logonUser.getDealerId()==null){
				act.setOutData("isDealer", "yes");
			}else{
				act.setOutData("isDealer", "no");
			}
			
			act.setOutData("isQuery", "yes");
			
			ProductManageDao dao = new ProductManageDao();
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			List<Map<String, Object>> brand=dao.getbrandList();//品牌
			act.setOutData("brand", brand);		
			List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
			act.setForword(serviceQueryDealerInitUrl2nd);
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护pre查询");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
	
	
	/**
	 * 经销商维护查询页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryDealerCsInfoInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ProductManageDao dao = new ProductManageDao();
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			List<Map<String, Object>> brand=dao.getbrandList();//品牌
			act.setOutData("brand", brand);		
			act.setForword(queryDealerCsInitUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护pre查询");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}

		/**
	 * 经销商维护修改页面
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryDealerInfoDetail1(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("DEALER_ID");
			DealerInfoDao dao=DealerInfoDao.getInstance();
			ProductManageDao productDao = new ProductManageDao();
			List<Map<String, Object>> types = productDao.getmyPriceTypeList(logonUser.getCompanyId(),dealerId);
				Map map=productDao.getMyMap(logonUser.getCompanyId(), dealerId);
				types.add(map);
				act.setOutData("types", types);
				List<Map<String, Object>> brand=productDao.getbrandList();//品牌
	
				act.setOutData("brand", brand);
			queryMonth(act, dao, dealerId, queryDealerInitDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商详细信息导出
	 */
	public void dealerInfoDownloadNew(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		List<List<Object>> contentList = new ArrayList<List<Object>>();
		List<Object> rowList = new ArrayList<Object>();
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			String dealer_type = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));
			
			String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
			String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
			String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
			String AUTHORIZATION_TYPE = CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE"));
			String dealerLevel = CommonUtils.checkNull(request.getParamValue("DEALERLEVEL"));
			String sJDealerCode = CommonUtils.checkNull(request.getParamValue("sJDealerCode"));
			String dealerType = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String serviceStatus = CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS"));
			String parentOrgCode = CommonUtils.checkNull(request.getParamValue("parentOrgCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
			String companyName = CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));
			String dealerStatus = CommonUtils.checkNull(request.getParamValue("DEALERSTATUS"));
			String regionCode = CommonUtils.checkNull(request.getParamValue("regionCode"));
			
			Map<String, Object> map = new HashMap<String, Object>() ;
			map.put("ECREATE_DATE", ECREATE_DATE) ;
			map.put("SCREATE_DATE", SCREATE_DATE) ;
			map.put("regionId", regionId) ;
			map.put("AUTHORIZATION_TYPE", AUTHORIZATION_TYPE) ;
			map.put("dealerLevel", dealerLevel) ;
			map.put("sJDealerCode", sJDealerCode) ;
			map.put("dealerType", dealerType) ;
			map.put("orgCode", orgCode) ;
			map.put("dealerName", dealerName) ;
			map.put("orgId", orgId) ;
			map.put("serviceStatus", serviceStatus) ;
			map.put("parentOrgCode", parentOrgCode) ;
			map.put("dealerCode", dealerCode) ;
			map.put("companyName", companyName) ;
			map.put("dealerStatus", dealerStatus) ;
			map.put("regionCode", regionCode) ;
			// 导出的文件名
			String fileName = null ;
			if(dealer_type.equals(Constant.DEALER_TYPE_DVS + "")) {
				fileName = "销售经销商信息.xls";
				rowList.add("所属区域");
				rowList.add("省份");
				rowList.add("所属行政区域城市");
				rowList.add("所属行政区域区县");
				rowList.add("邮编");
				rowList.add("经销商编码");
				rowList.add("经销商状态");
				rowList.add("所属上级单位");
				rowList.add("对应服务商编码");
				rowList.add("对应服务商状态");
				//2
				rowList.add("经销商全称");
				rowList.add("经销企业注册资金");
				rowList.add("经销企业组织机构代码");
				rowList.add("经销商简称");
				rowList.add("企业注册地址");
				rowList.add("销售展厅地址");
				rowList.add("单位性质");
				rowList.add("代理级别");
				rowList.add("经营类型");
				rowList.add("是否经营其他品牌");
				//3
				rowList.add("代理其它品牌名称");
				rowList.add("经销商销售热线");
				rowList.add("经销商传真");
				rowList.add("经销商邮箱");
				rowList.add("代理车型");
				rowList.add("代理区域");
				rowList.add("是否国家品牌授权城市");
				rowList.add("是否国家品牌授权区县");
				rowList.add("国家品牌授权");
				rowList.add("国家品牌授权信息收集时间");
	//4
				rowList.add("国家品牌授权提交时间");
				rowList.add("国家品牌授权起始时间");
				rowList.add("工商总局公告号");
				rowList.add("工商总局公布日期");
				rowList.add("国家品牌授权截止时间");
//				rowList.add("品牌授权书打印");
				rowList.add("经销企业法人");
				rowList.add("法人办公电话");
				rowList.add("法人手机");
				rowList.add("法人邮箱");
				//5
				rowList.add("总经理");
				rowList.add("总经理办公电话");
				rowList.add("总经理手机");
				rowList.add("总经理邮箱");
				rowList.add("销售经理");
				rowList.add("销售经理办公电话");
				rowList.add("销售经理手机");
				rowList.add("销售经理邮箱");
//				rowList.add("市场经理");
//				rowList.add("市场经理办公电话");
//				//6
//				rowList.add("市场经理手机");
//				rowList.add("市场经理邮箱");
				rowList.add("服务经理");
				rowList.add("服务经理办公电话");
				rowList.add("服务经理手机");
				rowList.add("服务经理邮箱");
				rowList.add("财务经理");
				rowList.add("财务经理办公电话");
				rowList.add("财务手机");
				rowList.add("财务邮箱");
				//7
				rowList.add("信息员");
				rowList.add("信息员办公电话");
				rowList.add("信息员手机");
				rowList.add("信息员QQ");
				rowList.add("信息员邮箱");
				rowList.add("开票公司名称");
				rowList.add("开票联系人");
				rowList.add("开票联系人办公电话");
				rowList.add("开票联系人手机");
				rowList.add("开票信息地址");
				//8
				rowList.add("开户行全称");
				rowList.add("开户行账号");
				rowList.add("纳税识别号");
//				rowList.add("信函收件地址");
//				rowList.add("信函收件联系人");
//				rowList.add("信函收件人性别");
//				rowList.add("信函收件联系人办公电话");
//				rowList.add("信函收件联系人手机");
				rowList.add("VI建设申请日期");
				rowList.add("VI建设开工日期");
				//9
				rowList.add("VI建设竣工日期");
				rowList.add("VI形象验收日期");
				rowList.add("拟建店类别");
				rowList.add("VI形象验收确定级别");
				rowList.add("VI支持总金额");
				rowList.add("VI支持首批比例");
				rowList.add("VI支持后续支持方式");
				rowList.add("VI支持起始时间");
				rowList.add("VI支持截止时间");
				rowList.add("首次提车时间");
				//10
				rowList.add("首次到车日期");
				rowList.add("首次销售时间");
				rowList.add("备注");
			} else {
				fileName = "售后经销商信息.xls";
				//1
				rowList.add("序号");
				rowList.add("大区");
				rowList.add("所属组织");
				rowList.add("省份");
				rowList.add("所属行政区域城市");
				rowList.add("所属行政区域区县");
				rowList.add("邮编");
				rowList.add("经销商代码");
				rowList.add("经销商等级");
				rowList.add("状态");
				//2
				rowList.add("经销商全称");
				rowList.add("经销商简称");
				rowList.add("上级经销商");
				rowList.add("与一级经销商关系");
				rowList.add("企业注册地址");
				rowList.add("注册证号");
				rowList.add("主营范围");
				rowList.add("兼营范围");
				rowList.add("组织机构代码");
				rowList.add("维修资质");
				//3
				rowList.add("建厂时间");
				rowList.add("法人代表");
				rowList.add("单位性质");
				rowList.add("固定资产（万元）");
				rowList.add("注册资金（万元）");
				rowList.add("服务站人数");
				rowList.add("维修车间面积（平方米）");
				rowList.add("接待室面积（平方米）");
				rowList.add("配件库面积（平方米）");
				rowList.add("停车场面积（平方米）");
				//4
				rowList.add("营业时间");
				rowList.add("月平均维修能力(台次)");
				rowList.add("经营类型");
				rowList.add("是否4S店");
				rowList.add("建店类别");
				rowList.add("地址");
//				rowList.add("是否有二级服务站");
				rowList.add("企业授权类型");
				rowList.add("授权时间");
				rowList.add("是否经营其他品牌");
				//5
				rowList.add("代理其它品牌名称");
				rowList.add("24小时服务热线");
				rowList.add("服务经理");
				rowList.add("服务经理手机");
				rowList.add("服务经理邮箱");
				rowList.add("索赔主管");
				rowList.add("索赔主管办公电话");
				rowList.add("索赔主管手机");
				rowList.add("索赔主管邮箱");
				rowList.add("索赔传真");
				//6
				rowList.add("服务主管");
				rowList.add("服务主管办公电话");
				rowList.add("服务主管手机");
				rowList.add("技术主管");
				rowList.add("技术主管手机");
				rowList.add("配件主管");
				rowList.add("配件主管办公电话");
				rowList.add("配件主管手机");
				rowList.add("配件主管邮箱");
				rowList.add("配件传真");
				//7
				rowList.add("配件储备金额（万元）");
				rowList.add("开户行");
				rowList.add("开票名称");
				rowList.add("银行帐号");
				rowList.add("开票电话");
				rowList.add("开票地址");
				rowList.add("纳税人识别号");
				rowList.add("纳税人性质");
				rowList.add("增值税发票");
				rowList.add("开票税率");
				//8
				rowList.add("财务经理");
				rowList.add("财务经理办公电话");
				rowList.add("财务手机");
				rowList.add("财务邮箱");
				rowList.add("备注");
				rowList.add("结算等级");
				rowList.add("开票等级");
				rowList.add("索赔员");
				rowList.add("服务商电话");
				rowList.add("辐射区域");
				rowList.add("代理车型");
				rowList.add("代理区域");
				
			}
			OutputStream os = null ;
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1") ;
			
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName) ;
			
			contentList.add(rowList);
			
			DealerInfoDao dao=DealerInfoDao.getInstance();
			List<Map<String, Object>> dealerInfoList = dao.queryServiceDealerInfoForXLS(map) ; 
			
			if(dealer_type.equals(Constant.DEALER_TYPE_DVS + "")) {
				if(dealerInfoList != null && !dealerInfoList.isEmpty()) {
					for(int i = 0; i < dealerInfoList.size(); i++) {
						rowList = new ArrayList<Object>();
						//1
//						rowList.add(i + 1);
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ROOT_ORG_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGION_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CITY_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COUNTIES_NAME"))) ;//区县
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZIP_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_STATUS_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;//对应服务商编码
//						//2
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;//对应服务商状态
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGISTERED_CAPITAL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ZC_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_SHORTNAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ADDRESS"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ADDRESS"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("UNION_TYPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_LEVEL_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WORK_TYPE"))) ;
//						//3
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_ACTING_BRAND_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ACTING_BRAND_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("HOTLINE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FAX_NO")));
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("EMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_VEHICLE_TYPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_AREA"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_AUTHORIZE_CITY_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_AUTHORIZE_COUNTY_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_BRAND"))) ;
//						//4
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_GET_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_SUB_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_EFFECT_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ANNOUNCEMENT_NO"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ANNOUNCEMENT_DATE")));
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ANNOUNCEMENT_END_DATE"))) ;
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;//品牌授权书打印
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_TELPHONE"))) ;
//						//5
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGALEMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEB_MASTER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEB_MASTER_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEB_MASTER_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEB_MASTER_EMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_EMAIL"))) ;
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_NAME"))) ;
////						//6
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_PHONE"))) ;
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_TELPHONE"))) ;
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_EMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_EMAIL"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_EMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_FAX"))) ;
//						//7
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_QQ"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_EMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ERP_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_PERSION"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ADD"))) ;
//						//8
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("BEGIN_BANK"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ACCOUNT"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAXPAYER_NO"))) ;
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;//信函收件地址
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;//信函收件联系人
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;//信函收件人性别
//						
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;//信函收件联系人办公电话
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;//信函收件联系人手机
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_APPLAY_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_BEGIN_DATE"))) ;
//						//9
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_COMPLETED_DATE"))) ;//
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_CONFRIM_DATE"))) ;//
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_LEVEL_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_COMFIRM_LEVEL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_AMOUNT"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_RATIO"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_TYPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_END_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIRST_SUB_DATE"))) ;
//						//10
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIRST_GETCAR_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIRST_SAELS_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REMARK"))) ;
						
						contentList.add(rowList);
					}
				}
			}else{
				if(dealerInfoList != null && !dealerInfoList.isEmpty()) {
					for(int i = 0; i < dealerInfoList.size(); i++) {
						rowList = new ArrayList<Object>();
						//1
						rowList.add(i + 1);
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ROOT_ORG_NAME"))) ;//大区
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ORG_NAME"))) ;//所属组织
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGION_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CITY_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COUNTIES_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZIP_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_LEVEL_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_STATUS_NAME"))) ;
						//2
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_SHORTNAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_RELATION"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ADDRESS"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZC_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZY_SCOPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("JY_SCOPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ZC_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MAIN_RESOURCES_NAME"))) ;
						//3
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SITE_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("UNION_TYPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIXED_CAPITAL")));
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGISTERED_CAPITAL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PEOPLE_NUMBER"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MAIN_AREA"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MEETING_ROOM_AREA"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARTS_AREA"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEPOT_AREA"))) ;
						//4
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("OPENING_TIME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ONLY_MONTH_COUNT"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WORK_TYPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_FOUR_S"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_LEVEL_NAME")));
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ADDRESS"))) ;//公司地址
//						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_LOW_SER"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZATION_TYPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZATION_DATE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_ACTING_BRAND_NAME"))) ;
						//5
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ACTING_BRAND_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("HOTLINE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_EMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_EMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_FAX"))) ;
						//6
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_DIRECTOR_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_DIRECTOR_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_DIRECTOR_TELHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TECHNOLOGY_DIRECTOR_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TECHNOLOGY_DIRECTOR_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_EMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_FAX"))) ;
						//7
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARTS_STORE_AMOUNT"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("BEGIN_BANK"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ERP_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ACCOUNT"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ADD"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAXPAYER_NO"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAXPAYER_NATURE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAX_INVOICE_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAX_DISRATE"))) ;
						//8
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_EMAIL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REMARK"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("BALANCE_LEVEL_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_LEVEL_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SPY_MAN"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("THE_AGENTS"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_VEHICLE_TYPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_AREA"))) ;
						contentList.add(rowList);
					}
				}
			}
			os = response.getOutputStream() ;
			CsvWriterUtil.createXlsFile(contentList, os) ;		
			os.flush() ;
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商EXCEL下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			rowList.clear();
			rowList = null;
			contentList.clear();
			contentList = null;
		}
	}
	
	public void dealerInfoDownloadNew2nd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		List<List<Object>> contentList = new ArrayList<List<Object>>();
		List<Object> rowList = new ArrayList<Object>();
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			String dealer_type = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));
			String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
			String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
			String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
			String AUTHORIZATION_TYPE = CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE"));
			String dealerLevel = CommonUtils.checkNull(request.getParamValue("DEALERLEVEL"));
			String sJDealerCode = CommonUtils.checkNull(request.getParamValue("sJDealerCode"));
			String dealerType = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String serviceStatus = CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS"));
			String parentOrgCode = CommonUtils.checkNull(request.getParamValue("parentOrgCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
			String companyName = CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));
			String dealerStatus = CommonUtils.checkNull(request.getParamValue("DEALERSTATUS"));
			String regionCode = CommonUtils.checkNull(request.getParamValue("regionCode"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			
			
			Map<String, Object> map = new HashMap<String, Object>() ;
			map.put("ECREATE_DATE", ECREATE_DATE) ;
			map.put("DEALER_ID", logonUser.getDealerId()) ;
			map.put("SCREATE_DATE", SCREATE_DATE) ;
			map.put("regionId", regionId) ;
			map.put("AUTHORIZATION_TYPE", AUTHORIZATION_TYPE) ;
			map.put("dealerLevel", dealerLevel) ;
			map.put("sJDealerCode", sJDealerCode) ;
			map.put("dealerType", dealerType) ;
			map.put("orgCode", orgCode) ;
			map.put("dealerName", dealerName) ;
			map.put("orgId", orgId) ;
			map.put("status", status) ;
			map.put("serviceStatus", serviceStatus) ;
			map.put("parentOrgCode", parentOrgCode) ;
			map.put("dealerCode", dealerCode) ;
			map.put("companyName", companyName) ;
			map.put("dealerStatus", dealerStatus) ;
			map.put("regionCode", regionCode) ;
			// 导出的文件名
				
				String fileName = null ;
				fileName = "二级经销商销售经销商信息.xls";
				rowList.add("所属区域");
				rowList.add("省份");
				rowList.add("所属行政区域城市");
				rowList.add("所属行政区域区县");
				rowList.add("所属一级经销商名称");
				rowList.add("所属一级经销商代码");
				
				rowList.add("邮编");
				rowList.add("分销网点编码");
				rowList.add("分销网点状态");
				rowList.add("分销网点全称");
				rowList.add("分销网点简称");
				rowList.add("审核状态");
				rowList.add("经销企业注册资金");
				rowList.add("经销企业组织机构代码");
				rowList.add("企业注册地址");
				rowList.add("销售展厅地址");
				rowList.add("单位性质");
				rowList.add("代理级别");
				rowList.add("经营类型");
				rowList.add("是否经营其他品牌");
				rowList.add("代理其它品牌名称");
				rowList.add("分销网店销售热线");
				rowList.add("分销网店邮箱");
				
				rowList.add("代理车型");
				rowList.add("最低库存");
				rowList.add("北汽幻速专营面积（平方米）");
				rowList.add("北汽幻速专营销售人员数");
				rowList.add("全年任务量");
				rowList.add("代理区域");
				
				rowList.add("经销企业法人");
				rowList.add("法人办公电话");
				rowList.add("法人手机");
				rowList.add("法人邮箱");
				
				rowList.add("总经理");
				rowList.add("总经理办公电话");
				rowList.add("总经理手机");
				rowList.add("总经理邮箱");
				
				rowList.add("销售经理");
				rowList.add("销售经理办公电话");
				rowList.add("销售经理手机");
				rowList.add("销售经理邮箱");
				
				rowList.add("市场经理");
				rowList.add("市场经理办公电话");
				rowList.add("市场经理手机");
				rowList.add("市场经理邮箱");
				
				rowList.add("财务经理");
				rowList.add("财务经理办公电话");
				rowList.add("财务手机");
				rowList.add("财务邮箱");
				
				rowList.add("信息员");
				rowList.add("信息员办公电话");
				rowList.add("信息员手机");
				rowList.add("信息员QQ");
				rowList.add("信息员邮箱");
				
				rowList.add("是否具备自有服务网点");
				rowList.add("服务站面积");
				rowList.add("服务站地址");
				rowList.add("24小时服务热线");
				
				rowList.add("二级网络性质");
				rowList.add("竞品品牌");
				rowList.add("与竞品行驶距离（米）");
				
				rowList.add("月均销量");
				rowList.add("市占率（%）");
				rowList.add("门头长度");
				rowList.add("是否具有销售销售门头");
				
				rowList.add("是否具有销售形象墙");
				rowList.add("代理区域人口数量");
				rowList.add("销售顾问（人员数量）");
				rowList.add("服务网点性质");
				rowList.add("维修资质");
				
				rowList.add("服务车间面积");
				rowList.add("是否具有服务门头");
				rowList.add("是否具有服务形象墙");
				rowList.add("服务离销售网点距离");
				rowList.add("维修技术师最低配备 （人员数量）");
				rowList.add("星级申报");
				
			OutputStream os = null ;
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1") ;
			
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName) ;
			
			contentList.add(rowList);
			
			DealerInfoDao dao=DealerInfoDao.getInstance();
			List<Map<String, Object>> dealerInfoList = dao.queryServiceDealerInfoForXLS2nd(map) ; 
			
			if(dealer_type.equals(Constant.DEALER_TYPE_DVS + "")) {
				if(dealerInfoList != null && !dealerInfoList.isEmpty()) {
					for(int i = 0; i < dealerInfoList.size(); i++) {
						rowList = new ArrayList<Object>();
						//1
//						rowList.add(i + 1);
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ROOT_ORG_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGION_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CITY_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COUNTIES_NAME"))) ;//区县
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("UP_DEALER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("UP_DEALER_CODE"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZIP_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_CODE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_STATUS_NAME"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_SHORTNAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SECEND_AUTID_STATUS_NAME"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGISTERED_CAPITAL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ZC_CODE"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ADDRESS"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ADDRESS"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("UNION_TYPE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_LEVEL_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WORK_TYPE"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_ACTING_BRAND_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ACTING_BRAND_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("HOTLINE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("EMAIL"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_VEHICLE_TYPE"))) ;//代理车型
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MIN_STOCK"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("OME_AREA"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("OME_PEOPLE_TOTAL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("YEAR_PLAN"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_AREA"))) ;//代理区域
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL"))) ;//
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_EMAIL_A"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_NAME_B"))) ;//zongjinli
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_PHONE_B"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_TELPHONE_B"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_EMAIL_B"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_NAME_B"))) ;//
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_EMAIL"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_EMAIL"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_EMAIL"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_NAME"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_PHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_TELPHONE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_QQ"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_EMAIL"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("HAVE_SERVICE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_AREA"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_ADDRESS"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_HOTLINE"))) ;
						
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SECOND_LEVEL_NETWORK_NATURE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPETING_BRAND"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AND_COMPETING_RUN_DISTANCE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MONTH_AVERAGE_SALES"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_OCCUPANCY"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DOORHEAD_LENGTH"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_HAVE_SALES_DOORHEAD"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_HAVE_SALES_IMAGE_WALL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AGENT_ZONE_POPULATION_COUNT"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SALES_CONSULTANT_COUNT"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_NETWORK_NATURE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REPAIR_APTITUDE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_WORKSHOP_AREA"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_HAVE_SERVICE_DOORHEAD"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_HAVE_SERVICE_IMAGE_WALL"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_SALES_NETWORK_DISTANCE"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REPAIR_ENGINEER_LOWEST_DEPLOY"))) ;
						rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEVEL_REPORT"))) ;
						contentList.add(rowList);
					}

					// 下载日志保存
					TtDownloadLogPO ttDownloadLogPO = new TtDownloadLogPO();
					ttDownloadLogPO.setLogId(new Long(SequenceManager.getSequence("")));
					ttDownloadLogPO.setDownloadTime(new Date());
					ttDownloadLogPO.setLogDownloadBtn("二级销售经销商下载");
					ttDownloadLogPO.setLogDownloadMenu("二级销售经销商查询");
					ttDownloadLogPO.setDownloadUser(logonUser.getName());  
					ttDownloadLogPO.setCreateBy(logonUser.getUserId());
					ttDownloadLogPO.setCreateDate(new Date());
					dao.insert(ttDownloadLogPO);
				}
			}
			os = response.getOutputStream() ;
			CsvWriterUtil.createXlsFile(contentList, os) ;		
			os.flush() ;
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商EXCEL下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			rowList.clear();
			rowList = null;
			contentList.clear();
			contentList = null;
		}
	}
	
	
	/**
	 * 经销商查看页面
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryDealerInfoDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("DEALER_ID");
			String dealerType = request.getParamValue("DEALER_TYPE");
			
			List<Object> params = new ArrayList<Object>();
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("select a.DEALER_ID,\n");
			sbSql.append("       (select DEALER_NAME from tm_dealer where dealer_id = a.parent_dealer_d ) DEALER_NAME_S ,\n");
			sbSql.append("       a.COMPANY_ID,\n");
			sbSql.append("       a.DEALER_TYPE,\n");
			sbSql.append("       a.DEALER_CODE,\n");
			sbSql.append("       a.DEALER_NAME,\n");
			sbSql.append("       a.STATUS,\n");
			sbSql.append("       a.DEALER_LEVEL,\n");
			sbSql.append("       a.DEALER_CLASS,\n");
			sbSql.append("       a.PARENT_DEALER_D,\n");
			sbSql.append("       a.DEALER_ORG_ID,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.province_id) PROVINCE_ID,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.city_id) CITY_ID,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.counties) COUNTIES,\n");
			sbSql.append("       a.ZIP_CODE,\n");
			sbSql.append("       a.ADDRESS,\n");
			sbSql.append("       a.PHONE,\n");
			sbSql.append("       a.LINK_MAN,\n");
			sbSql.append("       a.TREE_CODE,\n");
			sbSql.append("       to_char(a.CREATE_DATE,'yyyy-mm-dd') CREATE_DATE,\n");
			sbSql.append("       a.CREATE_BY,\n");
			sbSql.append("       a.UPDATE_DATE,\n");
			sbSql.append("       a.UPDATE_BY,\n");
			sbSql.append("       a.DEALER_SHORTNAME,\n");
			sbSql.append("       a.OEM_COMPANY_ID,\n");
			sbSql.append("       a.PRICE_ID,\n");
			sbSql.append("       a.DEALER_STAR,\n");
			sbSql.append("       a.TAXES_NO,\n");
			sbSql.append("       a.AREA_LEVEL,\n");
			sbSql.append("       a.SERVICE_LEVEL,\n");
			sbSql.append("       a.DEALER_LABOUR_TYPE,\n");
			sbSql.append("       a.IS_DQV,\n");
			sbSql.append("       a.BALANCE_LEVEL,\n");
			sbSql.append("       a.INVOICE_LEVEL,\n");
			sbSql.append("       to_char(a.BEGIN_BALANCE_DATE,'yyyy-mm-dd') BEGIN_BALANCE_DATE,\n");
			sbSql.append("       to_char(a.END_BALANCE_DATE,'yyyy-mm-dd') END_BALANCE_DATE,\n");
			sbSql.append("       to_char(a.BEGIN_OLD_DATE,'yyyy-mm-dd') BEGIN_OLD_DATE,\n");
			sbSql.append("       to_char(a.END_OLD_DATE,'yyyy-mm-dd') END_OLD_DATE,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.counties) COUNTIES,\n");
			sbSql.append("       a.TOWNSHIP,\n");
			sbSql.append("       a.LEGAL,\n");
			sbSql.append("       a.DUTY_PHONE,\n");
			sbSql.append("       a.BANK,\n");
			sbSql.append("       a.TAX_LEVEL,\n");
			sbSql.append("       a.CHANGE_DATE,\n");
			sbSql.append("       a.MAIN_RESOURCES,\n");
			sbSql.append("       a.IMAGE_DATE,\n");
			sbSql.append("       a.ADMIN_LEVEL,\n");
			sbSql.append("       a.PERSON_CHARGE,\n");
			sbSql.append("       a.IS_SCAN,\n");
			sbSql.append("       a.INVOICE_POST_ADD,\n");
			sbSql.append("       a.PDEALER_TYPE,\n");
			sbSql.append("       a.SERVICE_STATUS,\n");
			sbSql.append("       to_char(a.SITEDATE,'yyyy-mm-dd') SITEDATE,\n");
			sbSql.append("       to_char(a.DESTROYDATE,'yyyy-mm-dd') DESTROYDATE,\n");
			sbSql.append("       a.SPY_MAN,\n");
			sbSql.append("       a.SPY_PHONE,\n");
			sbSql.append("       a.BRAND,\n");
			sbSql.append("       a.ZZADDRESS,\n");
			sbSql.append("       a.IMAGE_LEVEL2,\n");
			sbSql.append("       a.CH_ADDRESS,\n");
			sbSql.append("       a.CH_ADDRESS2,\n");
			sbSql.append("       a.OLD_DEALER_CODE,\n");
			sbSql.append("       a.CL_CQ_FLAG,\n");
			sbSql.append("       a.OLD_DEALER_CODE2,\n");
			sbSql.append("       a.IS_YTH,\n");
			sbSql.append("       a.IS_NBDW,\n");
			sbSql.append("       a.IS_SPECIAL,\n");
			sbSql.append("       a.LEGAL_TEL,\n");
			sbSql.append("       a.MARKET_TEL,\n");
			sbSql.append("       a.LEGAL_PHONE,\n");
			sbSql.append("       a.LEGAL_TELPHONE,\n");
			sbSql.append("       a.LEGAL_EMAIL,\n");
			sbSql.append("       a.ZCCODE,\n");
			sbSql.append("       a.ZY_SCOPE,\n");
			sbSql.append("       a.JY_SCOPE,\n");
			sbSql.append("       a.INVOICE_PERSION,\n");
			sbSql.append("       a.INVOICE_TELPHONE,\n");
			sbSql.append("       a.BEGIN_BANK,\n");
			sbSql.append("       a.ERP_CODE,\n");
			sbSql.append("       a.INVOICE_ACCOUNT,\n");
			sbSql.append("       a.INVOICE_PHONE,\n");
			sbSql.append("       a.INVOICE_ADD,\n");
			sbSql.append("       a.TAXPAYER_NO,\n");
			sbSql.append("       a.TAXPAYER_NATURE,\n");
			sbSql.append("       a.TAX_INVOICE,\n");
			sbSql.append("       a.TAX_DISRATE,\n");
			sbSql.append("       b.COMPANY_ZC_CODE,\n");
			sbSql.append("       b.DETAIL_ID,\n");
			sbSql.append("       b.FK_DEALER_ID,\n");
			sbSql.append("       b.WEBMASTER_NAME,\n");
			sbSql.append("       b.WEBMASTER_PHONE,\n");
			sbSql.append("       b.WEBMASTER_TELPHONE,\n");
			sbSql.append("       b.WEBMASTER_EMAIL,\n");
			sbSql.append("       b.MARKET_NAME,\n");
			sbSql.append("       b.MARKET_PHONE,\n");
			sbSql.append("       b.MARKET_TELPHONE,\n");
			sbSql.append("       b.MARKET_EMAIL,\n");
			sbSql.append("       b.MANAGER_NAME,\n");
			sbSql.append("       b.MANAGER_PHONE,\n");
			sbSql.append("       b.MANAGER_TELPHONE,\n");
			sbSql.append("       b.MANAGER_EMAIL,\n");
			sbSql.append("       b.SER_MANAGER_NAME,\n");
			sbSql.append("       b.SER_MANAGER_PHONE,\n");
			sbSql.append("       b.SER_MANAGER_TELPHONE,\n");
			sbSql.append("       b.SER_MANAGER_EMAIL,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_NAME,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_PHONE,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_EMAIL,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_FAX,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_NAME,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_PHONE,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_EMAIL,\n");
			sbSql.append("       b.SER_DIRECTOR_NAME,\n");
			sbSql.append("       b.SER_DIRECTOR_PHONE,\n");
			sbSql.append("       b.SER_DIRECTOR_TELHONE,\n");
			sbSql.append("       b.TECHNOLOGY_DIRECTOR_NAME,\n");
			sbSql.append("       b.TECHNOLOGY_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.WORKSHOP_DIRECTOR_NAME,\n");
			sbSql.append("       b.WORKSHOP_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.FINANCE_MANAGER_NAME,\n");
			sbSql.append("       b.FINANCE_MANAGER_PHONE,\n");
			sbSql.append("       b.FINANCE_MANAGER_TELPHONE,\n");
			sbSql.append("       b.FINANCE_MANAGER_EMAIL,\n");
			sbSql.append("       b.MESSAGER_NAME,\n");
			sbSql.append("       b.MESSAGER_PHONE,\n");
			sbSql.append("       b.MESSAGER_TELPHONE,\n");
			sbSql.append("       b.MESSAGER_QQ,\n");
			sbSql.append("       b.MESSAGER_EMAIL,\n");
			sbSql.append("       to_char(b.VI_APPLAY_DATE,'yyyy-mm-dd') VI_APPLAY_DATE,\n");
			sbSql.append("       to_char(b.VI_BEGIN_DATE,'yyyy-mm-dd') VI_BEGIN_DATE,\n");
			sbSql.append("       to_char(b.VI_COMPLETED_DATE,'yyyy-mm-dd') VI_COMPLETED_DATE,\n");
			sbSql.append("       to_char(b.VI_CONFRIM_DATE,'yyyy-mm-dd') VI_CONFRIM_DATE,\n");
			sbSql.append("       b.IMAGE_LEVEL,\n");
			sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
			sbSql.append("       b.VI_SUPPORT_AMOUNT,\n");
			sbSql.append("       b.VI_SUPPORT_RATIO,\n");
			sbSql.append("       b.VI_SUPPORT_TYPE,\n");
			sbSql.append("       to_char(b.VI_SUPPORT_DATE,'yyyy-mm-dd') VI_SUPPORT_DATE,\n");
			sbSql.append("       to_char(b.VI_SUPPORT_END_DATE,'yyyy-mm-dd') VI_SUPPORT_END_DATE,\n");
			sbSql.append("       to_char(b.FIRST_SUB_DATE,'yyyy-mm-dd') FIRST_SUB_DATE,\n");
			sbSql.append("       to_char(b.FIRST_GETCAR_DATE,'yyyy-mm-dd') FIRST_GETCAR_DATE,\n");
			sbSql.append("       to_char(b.FIRST_SAELS_DATE,'yyyy-mm-dd') FIRST_SAELS_DATE,\n");
			sbSql.append("       b.UNION_TYPE,\n");
			sbSql.append("       b.FIXED_CAPITAL,\n");
			sbSql.append("       b.REGISTERED_CAPITAL,\n");
			sbSql.append("       b.PEOPLE_NUMBER,\n");
			sbSql.append("       b.OFFICE_AREA,\n");
			sbSql.append("       b.PARTS_AREA,\n");
			sbSql.append("       b.DEPOT_AREA,\n");
			sbSql.append("       b.MAIN_AREA,\n");
			sbSql.append("       vods.root_org_id,\n");
			sbSql.append("       vods.root_org_name,\n");
			sbSql.append("       b.ONLY_MONTH_COUNT,\n");
			sbSql.append("       b.OPENING_TIME,\n");
			sbSql.append("       b.WORK_TYPE,\n");
			sbSql.append("       b.IS_FOUR_S,\n");
			sbSql.append("       b.COMPANY_ADDRESS,\n");
			sbSql.append("       b.AUTHORIZATION_TYPE,\n");
			sbSql.append("       b.AUTHORIZATION_DATE,\n");
			sbSql.append("       b.IS_ACTING_BRAND,\n");
			sbSql.append("       b.ACTING_BRAND_NAME,\n");
			sbSql.append("       b.PARTS_STORE_AMOUNT,\n");
			sbSql.append("       b.REMARK,\n");
			sbSql.append("       b.SHOP_TYPE,\n");
			sbSql.append("       b.HOTLINE,\n");
			
			sbSql.append("       b.FITTINGS_DEC_NAME,\n");
			sbSql.append("       b.FITTINGS_DEC_TELPHONE,\n");
			sbSql.append("       b.FITTINGS_DEC_PHONE,\n");
			sbSql.append("       b.FITTINGS_DEC_EMAIL,\n");
			sbSql.append("       b.FITTINGS_DEC_FAX,\n");
			sbSql.append("       b.*,\n");
			sbSql.append("       A.*,\n");
			
			sbSql.append("       b.FAX_NO,\n");
			sbSql.append("       b.EMAIL,\n");
			sbSql.append("       b.IS_AUTHORIZE_CITY,\n");
			sbSql.append("       b.IS_AUTHORIZE_COUNTY,\n");
			sbSql.append("       b.AUTHORIZE_BRAND,\n");
			sbSql.append("       to_char(b.AUTHORIZE_GET_DATE,'yyyy-mm-dd') AUTHORIZE_GET_DATE,\n");
			sbSql.append("       to_char(b.AUTHORIZE_SUB_DATE,'yyyy-mm-dd') AUTHORIZE_SUB_DATE,\n");
			sbSql.append("       to_char(b.AUTHORIZE_EFFECT_DATE,'yyyy-mm-dd') AUTHORIZE_EFFECT_DATE,\n");
			sbSql.append("       b.ANNOUNCEMENT_NO,\n");
			sbSql.append("       to_char(b.ANNOUNCEMENT_DATE,'yyyy-mm-dd') ANNOUNCEMENT_DATE,\n");
			sbSql.append("       to_char(b.ANNOUNCEMENT_END_DATE,'yyyy-mm-dd') ANNOUNCEMENT_END_DATE,\n");
			sbSql.append("       c.COMPANY_NAME,\n");
			sbSql.append("       c.COMPANY_ID,\n");
			sbSql.append("       (select org_name from tm_org where org_code = d.ORG_CODE) org_code,\n");
			sbSql.append("       a.DEALER_ORG_ID,\n");
			sbSql.append("       (select dealer_name from tm_dealer where dealer_id = a.parent_dealer_d) as PARENT_DEALER_NAME\n");
			sbSql.append("  from tm_dealer a, tm_dealer_detail b, tm_company c, tm_org d,vw_org_dealer_service vods\n");
			sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
			sbSql.append("   and a.dealer_id = vods.dealer_id(+)\n");
			sbSql.append("   and a.company_id = c.company_id\n"); 
			sbSql.append("   and a.dealer_org_id = d.org_id\n"); 
			sbSql.append("	 and a.dealer_id = ?");
			params.add(dealerId);
			
			ProductManageDao dao = ProductManageDao.getInstance();
			
			Map<String, Object> dealerData = dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());
			
			act.setOutData("dMap", dealerData);
			
			if(dealerData.get("DEALER_TYPE").toString().equals(String.valueOf(Constant.DEALER_TYPE_DVS))) {
				act.setForword(queryDealerInitDetailUrl);
			} else {
				act.setForword(queryDealerCsInitDetailUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void queryDealerInfoDetail2nd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = request.getParamValue("DEALER_ID");
			String dealerType = request.getParamValue("DEALER_TYPE");
			
			List<Object> params = new ArrayList<Object>();
			StringBuffer sbSql = new StringBuffer();
			act.setOutData("user_id", Constant.DEALER_SECEND_AUDIT_02);
			sbSql.append("select a.DEALER_ID,\n");
			sbSql.append("       a.DEALER_NAME as DEALER_NAME_S,\n");
			sbSql.append("       a.COMPANY_ID,\n");
			sbSql.append("       a.DEALER_TYPE,\n");
			sbSql.append("       a.DEALER_CODE,\n");
			sbSql.append("       a.DEALER_NAME,\n");
			sbSql.append("       a.STATUS,\n");
			sbSql.append("       a.DEALER_LEVEL,\n");
			sbSql.append("       a.DEALER_CLASS,\n");
			sbSql.append("       a.PARENT_DEALER_D,\n");
			sbSql.append("       a.DEALER_ORG_ID,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.province_id) PROVINCE_ID,\n");
//			sbSql.append("       a.city_id,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.city_id) CITY_ID,\n");
			sbSql.append("       a.ZIP_CODE,\n");
			sbSql.append("       a.ADDRESS,\n");
			sbSql.append("       a.PHONE,\n");
			sbSql.append("       a.LINK_MAN,\n");
			sbSql.append("       a.TREE_CODE,\n");
			sbSql.append("       to_char(a.CREATE_DATE,'yyyy-mm-dd') CREATE_DATE,\n");
			sbSql.append("       a.CREATE_BY,\n");
			sbSql.append("       a.UPDATE_DATE,\n");
			sbSql.append("       a.UPDATE_BY,\n");
			sbSql.append("       a.DEALER_SHORTNAME,\n");
			sbSql.append("       a.OEM_COMPANY_ID,\n");
			sbSql.append("       a.PRICE_ID,\n");
			sbSql.append("       a.DEALER_STAR,\n");
			sbSql.append("       a.TAXES_NO,\n");
			sbSql.append("       a.AREA_LEVEL,\n");
			sbSql.append("       a.SERVICE_LEVEL,\n");
			sbSql.append("       a.DEALER_LABOUR_TYPE,\n");
			sbSql.append("       a.IS_DQV,\n");
			sbSql.append("       a.BALANCE_LEVEL,\n");
			sbSql.append("       a.INVOICE_LEVEL,\n");
			sbSql.append("       to_char(a.BEGIN_BALANCE_DATE,'yyyy-mm-dd') BEGIN_BALANCE_DATE,\n");
			sbSql.append("       to_char(a.END_BALANCE_DATE,'yyyy-mm-dd') END_BALANCE_DATE,\n");
			sbSql.append("       to_char(a.BEGIN_OLD_DATE,'yyyy-mm-dd') BEGIN_OLD_DATE,\n");
			sbSql.append("       to_char(a.END_OLD_DATE,'yyyy-mm-dd') END_OLD_DATE,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.counties) COUNTIES,\n");
			sbSql.append("       a.TOWNSHIP,\n");
			sbSql.append("       a.LEGAL,\n");
			sbSql.append("       a.DUTY_PHONE,\n");
			sbSql.append("       a.BANK,\n");
			sbSql.append("       a.TAX_LEVEL,\n");
			sbSql.append("       a.CHANGE_DATE,\n");
			sbSql.append("       a.MAIN_RESOURCES,\n");
			sbSql.append("       a.IMAGE_DATE,\n");
			sbSql.append("       a.ADMIN_LEVEL,\n");
			sbSql.append("       a.PERSON_CHARGE,\n");
			sbSql.append("       a.IS_SCAN,\n");
			sbSql.append("       a.INVOICE_POST_ADD,\n");
			sbSql.append("       a.PDEALER_TYPE,\n");
			sbSql.append("       a.SERVICE_STATUS,\n");
			sbSql.append("       to_char(a.SITEDATE,'yyyy-mm-dd') SITEDATE,\n");
			sbSql.append("       to_char(a.DESTROYDATE,'yyyy-mm-dd') DESTROYDATE,\n");
			sbSql.append("       a.SPY_MAN,\n");
			sbSql.append("       a.SPY_PHONE,\n");
			sbSql.append("       a.BRAND,\n");
			sbSql.append("       a.ZZADDRESS,\n");
			sbSql.append("       a.IMAGE_LEVEL2,\n");
			sbSql.append("       a.CH_ADDRESS,\n");
			sbSql.append("       a.CH_ADDRESS2,\n");
			sbSql.append("       a.OLD_DEALER_CODE,\n");
			sbSql.append("       a.CL_CQ_FLAG,\n");
			sbSql.append("       a.OLD_DEALER_CODE2,\n");
			sbSql.append("       a.IS_YTH,\n");
			sbSql.append("       a.IS_NBDW,\n");
			sbSql.append("       a.IS_SPECIAL,\n");
			sbSql.append("       a.LEGAL_TEL,\n");
			sbSql.append("       a.MARKET_TEL,\n");
			sbSql.append("       a.LEGAL_PHONE,\n");
			sbSql.append("       a.LEGAL_TELPHONE,\n");
			sbSql.append("       a.LEGAL_EMAIL,\n");
			sbSql.append("       a.ZCCODE,\n");
			sbSql.append("       a.ZY_SCOPE,\n");
			sbSql.append("       a.JY_SCOPE,\n");
			sbSql.append("       a.INVOICE_PERSION,\n");
			sbSql.append("       a.INVOICE_TELPHONE,\n");
			sbSql.append("       a.BEGIN_BANK,\n");
			sbSql.append("       a.ERP_CODE,\n");
			sbSql.append("       a.INVOICE_ACCOUNT,\n");
			sbSql.append("       a.INVOICE_PHONE,\n");
			sbSql.append("       a.INVOICE_ADD,\n");
			sbSql.append("       a.TAXPAYER_NO,\n");
			sbSql.append("       a.TAXPAYER_NATURE,\n");
			sbSql.append("       a.TAX_INVOICE,\n");
			sbSql.append("       a.TAX_DISRATE,\n");
			sbSql.append("       b.COMPANY_ZC_CODE,\n");
			sbSql.append("       b.DETAIL_ID,\n");
			sbSql.append("       b.FK_DEALER_ID,\n");
			sbSql.append("       b.WEBMASTER_NAME,\n");
			sbSql.append("       b.WEBMASTER_PHONE,\n");
			sbSql.append("       b.WEBMASTER_TELPHONE,\n");
			sbSql.append("       b.WEBMASTER_EMAIL,\n");
			sbSql.append("       b.MARKET_NAME,\n");
			sbSql.append("       b.MARKET_PHONE,\n");
			sbSql.append("       b.MARKET_TELPHONE,\n");
			sbSql.append("       b.MARKET_EMAIL,\n");
			sbSql.append("       b.MANAGER_NAME,\n");
			sbSql.append("       b.MANAGER_PHONE,\n");
			sbSql.append("       b.MANAGER_TELPHONE,\n");
			sbSql.append("       b.MANAGER_EMAIL,\n");
			sbSql.append("       b.SER_MANAGER_NAME,\n");
			sbSql.append("       b.SER_MANAGER_PHONE,\n");
			sbSql.append("       b.SER_MANAGER_TELPHONE,\n");
			sbSql.append("       b.SER_MANAGER_EMAIL,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_NAME,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_PHONE,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_EMAIL,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_FAX,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_NAME,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_PHONE,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_EMAIL,\n");
			sbSql.append("       b.SER_DIRECTOR_NAME,\n");
			sbSql.append("       b.SER_DIRECTOR_PHONE,\n");
			sbSql.append("       b.SER_DIRECTOR_TELHONE,\n");
			sbSql.append("       b.TECHNOLOGY_DIRECTOR_NAME,\n");
			sbSql.append("       b.TECHNOLOGY_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.WORKSHOP_DIRECTOR_NAME,\n");
			sbSql.append("       b.WORKSHOP_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.FINANCE_MANAGER_NAME,\n");
			sbSql.append("       b.FINANCE_MANAGER_PHONE,\n");
			sbSql.append("       b.FINANCE_MANAGER_TELPHONE,\n");
			sbSql.append("       b.FINANCE_MANAGER_EMAIL,\n");
			sbSql.append("       b.MESSAGER_NAME,\n");
			sbSql.append("       b.MESSAGER_PHONE,\n");
			sbSql.append("       b.MESSAGER_TELPHONE,\n");
			sbSql.append("       b.MESSAGER_QQ,\n");
			sbSql.append("       b.MESSAGER_EMAIL,\n");
			sbSql.append("       b.IMAGE_LEVEL,\n");
			sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
			sbSql.append("       b.UNION_TYPE,\n");
			sbSql.append("       b.FIXED_CAPITAL,\n");
			sbSql.append("       b.REGISTERED_CAPITAL,\n");
			sbSql.append("       b.PEOPLE_NUMBER,\n");
			sbSql.append("       b.OFFICE_AREA,\n");
			sbSql.append("       b.PARTS_AREA,\n");
			sbSql.append("       b.DEPOT_AREA,\n");
			sbSql.append("       b.MAIN_AREA,\n");
			sbSql.append("       vods.root_org_id,\n");
			sbSql.append("       vods.root_org_name,\n");
			sbSql.append("       b.ONLY_MONTH_COUNT,\n");
			sbSql.append("       b.OPENING_TIME,\n");
			sbSql.append("       b.WORK_TYPE,\n");
			sbSql.append("       b.IS_FOUR_S,\n");
			sbSql.append("       b.COMPANY_ADDRESS,\n");
			sbSql.append("       b.AUTHORIZATION_TYPE,\n");
			sbSql.append("       b.AUTHORIZATION_DATE,\n");
			sbSql.append("       b.IS_ACTING_BRAND,\n");
			sbSql.append("       b.ACTING_BRAND_NAME,\n");
			sbSql.append("       b.PARTS_STORE_AMOUNT,\n");
			sbSql.append("       b.REMARK,\n");
			sbSql.append("       b.SHOP_TYPE,\n");
			sbSql.append("       b.HOTLINE,\n");
			sbSql.append("       b.FAX_NO,\n");
			sbSql.append("       b.EMAIL,\n");
			sbSql.append("       b.IS_AUTHORIZE_CITY,\n");
			sbSql.append("       b.IS_AUTHORIZE_COUNTY,\n");
			sbSql.append("       b.AUTHORIZE_BRAND,\n");
			sbSql.append("       to_char(b.AUTHORIZE_GET_DATE,'yyyy-mm-dd') AUTHORIZE_GET_DATE,\n");
			sbSql.append("       to_char(b.AUTHORIZE_SUB_DATE,'yyyy-mm-dd') AUTHORIZE_SUB_DATE,\n");
			sbSql.append("       to_char(b.AUTHORIZE_EFFECT_DATE,'yyyy-mm-dd') AUTHORIZE_EFFECT_DATE,\n");
			sbSql.append("       b.ANNOUNCEMENT_NO,\n");
			sbSql.append("       to_char(b.ANNOUNCEMENT_DATE,'yyyy-mm-dd') ANNOUNCEMENT_DATE,\n");
			sbSql.append("       to_char(b.ANNOUNCEMENT_END_DATE,'yyyy-mm-dd') ANNOUNCEMENT_END_DATE,\n");
			sbSql.append("       c.COMPANY_NAME,\n");
			sbSql.append("       c.COMPANY_ID,\n");
			sbSql.append("       a.DEALER_ORG_ID,\n");
			
			sbSql.append("       b.MIN_STOCK,\n");
			sbSql.append("       b.OME_AREA,\n");
			sbSql.append("       b.OME_PEOPLE_TOTAL,\n");
			sbSql.append("       b.YEAR_PLAN,\n");
			sbSql.append("       b.HAVE_SERVICE,\n");
			sbSql.append("       b.SERVICE_AREA,\n");
			sbSql.append("       b.SERVICE_ADDRESS,\n");
			sbSql.append("       b.SERVICE_HOTLINE,\n");
			sbSql.append("       b.proxy_area,\n");
			sbSql.append("       b.proxy_vehicle_type,\n");
			
			
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.SECOND_LEVEL_NETWORK_NATURE) SECOND_LEVEL_NETWORK_NATURE,\n");
			sbSql.append("       tdsl.COMPETING_BRAND,\n");
			sbSql.append("       tdsl.AND_COMPETING_RUN_DISTANCE,\n");
			sbSql.append("       tdsl.MONTH_AVERAGE_SALES,\n");
			sbSql.append("       tdsl.MARKET_OCCUPANCY,\n");
			sbSql.append("       tdsl.DOORHEAD_LENGTH,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.IS_HAVE_SALES_DOORHEAD) IS_HAVE_SALES_DOORHEAD,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.IS_HAVE_SALES_IMAGE_WALL) IS_HAVE_SALES_IMAGE_WALL,\n");
			sbSql.append("       tdsl.AGENT_ZONE_POPULATION_COUNT,\n");
			sbSql.append("       tdsl.SALES_CONSULTANT_COUNT,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.SERVICE_NETWORK_NATURE) SERVICE_NETWORK_NATURE,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.REPAIR_APTITUDE) REPAIR_APTITUDE,\n");
			sbSql.append("       tdsl.SERVICE_WORKSHOP_AREA,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.IS_HAVE_SERVICE_DOORHEAD) IS_HAVE_SERVICE_DOORHEAD,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.IS_HAVE_SERVICE_IMAGE_WALL) IS_HAVE_SERVICE_IMAGE_WALL,\n");
			sbSql.append("       tdsl.SERVICE_SALES_NETWORK_DISTANCE,\n");
			sbSql.append("       tdsl.REPAIR_ENGINEER_LOWEST_DEPLOY,\n");
			sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.LEVEL_REPORT) LEVEL_REPORT,\n");
			
			sbSql.append("       (select dealer_name from tm_dealer where dealer_id = a.parent_dealer_d) as PARENT_DEALER_NAME\n");
			sbSql.append("  from tm_dealer a, tm_dealer_detail b, tm_company c,vw_org_dealer_service vods, TM_DEALER_SECOND_LEVEL tdsl \n");
			sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
			sbSql.append("   and a.parent_dealer_d = vods.dealer_id(+)\n");
			sbSql.append("   and a.company_id = c.company_id\n"); 
			sbSql.append("   and a.dealer_id = tdsl.fk_dealer_id(+)\n");
			sbSql.append("   and a.dealer_level = 10851002\n"); 
			sbSql.append("   and a.dealer_type = 10771001\n"); 
			sbSql.append("	 and a.dealer_id = ?");
			params.add(dealerId);
			
			ProductManageDao dao = ProductManageDao.getInstance();
			
			Map<String, Object> dealerData = dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());
			
			 params = new ArrayList<Object>();
			 sbSql = new StringBuffer();
			
			sbSql.append(" select id,decode(a.STATUS,\n");
			sbSql.append("'92901001',\n");
			sbSql.append("'已保存',\n");
			sbSql.append("'92901002',\n");
			sbSql.append(" '已提交',\n");
			sbSql.append(" '92901003',\n");
			sbSql.append(" '区域经理审核通过',\n");
			sbSql.append(" '92901004',\n");
			sbSql.append(" '销售管理中心审核通过',\n");
			sbSql.append(" '92901005',\n");
			sbSql.append(" '驳回',\n");
			sbSql.append(" '92901007',\n");
			sbSql.append(" '审核通过',\n");
			sbSql.append("'92901006',\n");
			sbSql.append(" '总经理审核通过') as status， (select name from tc_user where user_id = a.create_by) as user_id,\n");
			sbSql.append("  to_char(create_date, 'yyyy-mm-dd hh24:mi:ss') as create_date,\n");
			sbSql.append(" remark\n");
			sbSql.append(" from TT_DEALER_SECEND_AUDIT a where dealer_id = ? order by id\n");
			params.add(dealerId);
			List<Map<String, Object>> flowInfo = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
			
			act.setOutData("dMap", dealerData);
			act.setOutData("flowInfo", flowInfo);
			
			TtProxyAreaPO areaPO = new TtProxyAreaPO();
			areaPO.setDealerId(new Long(dealerData.get("DEALER_ID").toString()));
			List<TtProxyAreaPO> list = dao.select(areaPO);
			String proArea = "";
			for (int i = 0; i < list.size(); i++) {
				if(i>0){
					proArea = proArea+","+list.get(i).getProxyAreaName();
				}else{
					proArea = list.get(i).getProxyAreaName();
				}
			}
			act.setOutData("proArea", proArea);
			
			String sql = "SELECT F.FJID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ =" + dealerData.get("DEALER_ID").toString() + "ORDER BY F.FJID";
			List<Map<String, Object>> attachList = dao.pageQuery(sql, null, dao.getFunName());
			if(attachList != null && attachList.size()>0){
				act.setOutData("attachList", attachList);
			}
			if(logonUser.getDealerId()==null){
				act.setOutData("isDealer", "no");
			}else{
				act.setOutData("isDealer", "yes");
			}
			String curPage = request.getParamValue("curPage");
			act.setOutData("curPage", curPage);
//			if(dealerData.get("DEALER_TYPE").toString().equals(String.valueOf(Constant.DEALER_TYPE_DVS))) {
				act.setForword(queryDealerInitDetail2ndUrl);
//			} else {
//				act.setForword(queryDealerCsInitDetailUrl);
//			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}


	public void queryMonth(ActionContext act, DealerInfoDao dao,
			String dealerId, String url) throws Exception {
		// 查询插入信息
		Map<String, Object> map = dao.getDealerInfobyId1(dealerId);
		// 查询经销商业务范围信息
		List<Map<String, Object>> businessList = dao
				.getDealerBusinessArea(dealerId);
		// 查询经销商地址信息
		List<Map<String, Object>> addressList = dao.getAddress(dealerId);
		
		//查询经销商个人信贷信息
		List<Map<String, Object>> mortgageList=dao.queryMortgageForDealer(dealerId);
		
		/*if(addressList != null) {
			int len = addressList.size() ;
			
			AddressArea addressArea = new AddressArea() ;
			
			for(int i=0; i<len; i++) {
				addressList.get(i).put("areas", addressArea.addressAreaStrGet(addressList.get(i).get("ID").toString())) ;
			}
		}*/
		ProductManageDao productDao = new ProductManageDao();
	
	
		act.setOutData("businessList", businessList);
		act.setOutData("addressList", addressList);
		act.setOutData("mortgageList", mortgageList);
		List<Map<String, Object>> brand=productDao.getbrandList();//品牌
		act.setOutData("brand", brand);
	
		act.setOutData("map", map);
	
		act.setForword(url);
	}
	public void queryDealerChangeInfoDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String ID = request.getParamValue("ID");
			String dealerType = request.getParamValue("DEALER_TYPE");
			
			List<Object> params = new ArrayList<Object>();
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("select a.DEALER_ID,\n");
			sbSql.append("       (select DEALER_NAME from tm_dealer where dealer_id = a.parent_dealer_d ) DEALER_NAME_S ,\n");
			sbSql.append("       a.COMPANY_ID,\n");
			sbSql.append("       a.DEALER_TYPE,\n");
			sbSql.append("       a.DEALER_CODE,\n");
			sbSql.append("       a.DEALER_NAME,\n");
			sbSql.append("       a.STATUS,\n");
			sbSql.append("       a.DEALER_LEVEL,\n");
			sbSql.append("       a.DEALER_CLASS,\n");
			sbSql.append("       a.PARENT_DEALER_D,\n");
			sbSql.append("       a.DEALER_ORG_ID,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.province_id) PROVINCE_ID,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.city_id) CITY_ID,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.counties) COUNTIES,\n");
			sbSql.append("       a.ZIP_CODE,\n");
			sbSql.append("       a.ADDRESS,\n");
			sbSql.append("       a.PHONE,\n");
			sbSql.append("       a.LINK_MAN,\n");
			sbSql.append("       a.TREE_CODE,\n");
			sbSql.append("       to_char(a.CREATE_DATE,'yyyy-mm-dd') CREATE_DATE,\n");
			sbSql.append("       a.CREATE_BY,\n");
			sbSql.append("       a.UPDATE_DATE,\n");
			sbSql.append("       a.UPDATE_BY,\n");
			sbSql.append("       a.DEALER_SHORTNAME,\n");
			sbSql.append("       a.OEM_COMPANY_ID,\n");
			sbSql.append("       a.PRICE_ID,\n");
			sbSql.append("       a.DEALER_STAR,\n");
			sbSql.append("       a.TAXES_NO,\n");
			sbSql.append("       a.AREA_LEVEL,\n");
			sbSql.append("       a.SERVICE_LEVEL,\n");
			sbSql.append("       a.DEALER_LABOUR_TYPE,\n");
			sbSql.append("       a.IS_DQV,\n");
			sbSql.append("       a.BALANCE_LEVEL,\n");
			sbSql.append("       a.INVOICE_LEVEL,\n");
			sbSql.append("       to_char(a.BEGIN_BALANCE_DATE,'yyyy-mm-dd') BEGIN_BALANCE_DATE,\n");
			sbSql.append("       to_char(a.END_BALANCE_DATE,'yyyy-mm-dd') END_BALANCE_DATE,\n");
			sbSql.append("       to_char(a.BEGIN_OLD_DATE,'yyyy-mm-dd') BEGIN_OLD_DATE,\n");
			sbSql.append("       to_char(a.END_OLD_DATE,'yyyy-mm-dd') END_OLD_DATE,\n");
			sbSql.append("       (select region_name from tm_region where region_code = a.counties) COUNTIES,\n");
			sbSql.append("       a.TOWNSHIP,\n");
			sbSql.append("       a.LEGAL,\n");
			sbSql.append("       a.DUTY_PHONE,\n");
			sbSql.append("       a.BANK,\n");
			sbSql.append("       a.TAX_LEVEL,\n");
			sbSql.append("       a.CHANGE_DATE,\n");
			sbSql.append("       a.MAIN_RESOURCES,\n");
			sbSql.append("       a.IMAGE_DATE,\n");
			sbSql.append("       a.ADMIN_LEVEL,\n");
			sbSql.append("       a.PERSON_CHARGE,\n");
			sbSql.append("       a.IS_SCAN,\n");
			sbSql.append("       a.INVOICE_POST_ADD,\n");
			sbSql.append("       a.PDEALER_TYPE,\n");
			sbSql.append("       a.SERVICE_STATUS,\n");
			sbSql.append("       to_char(a.SITEDATE,'yyyy-mm-dd') SITEDATE,\n");
			sbSql.append("       to_char(a.DESTROYDATE,'yyyy-mm-dd') DESTROYDATE,\n");
			sbSql.append("       a.SPY_MAN,\n");
			sbSql.append("       a.SPY_PHONE,\n");
			sbSql.append("       a.BRAND,\n");
			sbSql.append("       a.ZZADDRESS,\n");
			sbSql.append("       a.IMAGE_LEVEL2,\n");
			sbSql.append("       a.CH_ADDRESS,\n");
			sbSql.append("       a.CH_ADDRESS2,\n");
			sbSql.append("       a.OLD_DEALER_CODE,\n");
			sbSql.append("       a.CL_CQ_FLAG,\n");
			sbSql.append("       a.OLD_DEALER_CODE2,\n");
			sbSql.append("       a.IS_YTH,\n");
			sbSql.append("       a.IS_NBDW,\n");
			sbSql.append("       a.IS_SPECIAL,\n");
			sbSql.append("       a.LEGAL_TEL,\n");
			sbSql.append("       a.MARKET_TEL,\n");
			sbSql.append("       a.LEGAL_PHONE,\n");
			sbSql.append("       a.LEGAL_TELPHONE,\n");
			sbSql.append("       a.LEGAL_EMAIL,\n");
			sbSql.append("       a.ZCCODE,\n");
			sbSql.append("       a.ZY_SCOPE,\n");
			sbSql.append("       a.JY_SCOPE,\n");
			sbSql.append("       a.INVOICE_PERSION,\n");
			sbSql.append("       a.INVOICE_TELPHONE,\n");
			sbSql.append("       a.BEGIN_BANK,\n");
			sbSql.append("       a.ERP_CODE,\n");
			sbSql.append("       a.INVOICE_ACCOUNT,\n");
			sbSql.append("       a.INVOICE_PHONE,\n");
			sbSql.append("       a.INVOICE_ADD,\n");
			sbSql.append("       a.TAXPAYER_NO,\n");
			sbSql.append("       a.TAXPAYER_NATURE,\n");
			sbSql.append("       a.TAX_INVOICE,\n");
			sbSql.append("       a.TAX_DISRATE,\n");
			sbSql.append("       b.COMPANY_ZC_CODE,\n");
			sbSql.append("       b.DETAIL_ID,\n");
			sbSql.append("       b.FK_DEALER_ID,\n");
			sbSql.append("       b.WEBMASTER_NAME,\n");
			sbSql.append("       b.WEBMASTER_PHONE,\n");
			sbSql.append("       b.WEBMASTER_TELPHONE,\n");
			sbSql.append("       b.WEBMASTER_EMAIL,\n");
			sbSql.append("       b.MARKET_NAME,\n");
			sbSql.append("       b.MARKET_PHONE,\n");
			sbSql.append("       b.MARKET_TELPHONE,\n");
			sbSql.append("       b.MARKET_EMAIL,\n");
			sbSql.append("       b.MANAGER_NAME,\n");
			sbSql.append("       b.MANAGER_PHONE,\n");
			sbSql.append("       b.MANAGER_TELPHONE,\n");
			sbSql.append("       b.MANAGER_EMAIL,\n");
			sbSql.append("       b.SER_MANAGER_NAME,\n");
			sbSql.append("       b.SER_MANAGER_PHONE,\n");
			sbSql.append("       b.SER_MANAGER_TELPHONE,\n");
			sbSql.append("       b.SER_MANAGER_EMAIL,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_NAME,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_PHONE,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_EMAIL,\n");
			sbSql.append("       b.CLAIM_DIRECTOR_FAX,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_NAME,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_PHONE,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.CUSTSER_DIRECTOR_EMAIL,\n");
			sbSql.append("       b.SER_DIRECTOR_NAME,\n");
			sbSql.append("       b.SER_DIRECTOR_PHONE,\n");
			sbSql.append("       b.SER_DIRECTOR_TELHONE,\n");
			sbSql.append("       b.TECHNOLOGY_DIRECTOR_NAME,\n");
			sbSql.append("       b.TECHNOLOGY_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.WORKSHOP_DIRECTOR_NAME,\n");
			sbSql.append("       b.WORKSHOP_DIRECTOR_TELPHONE,\n");
			sbSql.append("       b.FINANCE_MANAGER_NAME,\n");
			sbSql.append("       b.FINANCE_MANAGER_PHONE,\n");
			sbSql.append("       b.FINANCE_MANAGER_TELPHONE,\n");
			sbSql.append("       b.FINANCE_MANAGER_EMAIL,\n");
			sbSql.append("       b.MESSAGER_NAME,\n");
			sbSql.append("       b.MESSAGER_PHONE,\n");
			sbSql.append("       b.MESSAGER_TELPHONE,\n");
			sbSql.append("       b.MESSAGER_QQ,\n");
			sbSql.append("       b.MESSAGER_EMAIL,\n");
			sbSql.append("       to_char(b.VI_APPLAY_DATE,'yyyy-mm-dd') VI_APPLAY_DATE,\n");
			sbSql.append("       to_char(b.VI_BEGIN_DATE,'yyyy-mm-dd') VI_BEGIN_DATE,\n");
			sbSql.append("       to_char(b.VI_COMPLETED_DATE,'yyyy-mm-dd') VI_COMPLETED_DATE,\n");
			sbSql.append("       to_char(b.VI_CONFRIM_DATE,'yyyy-mm-dd') VI_CONFRIM_DATE,\n");
			sbSql.append("       b.IMAGE_LEVEL,\n");
			sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
			sbSql.append("       b.VI_SUPPORT_AMOUNT,\n");
			sbSql.append("       b.VI_SUPPORT_RATIO,\n");
			sbSql.append("       b.VI_SUPPORT_TYPE,\n");
			sbSql.append("       to_char(b.VI_SUPPORT_DATE,'yyyy-mm-dd') VI_SUPPORT_DATE,\n");
			sbSql.append("       to_char(b.VI_SUPPORT_END_DATE,'yyyy-mm-dd') VI_SUPPORT_END_DATE,\n");
			sbSql.append("       to_char(b.FIRST_SUB_DATE,'yyyy-mm-dd') FIRST_SUB_DATE,\n");
			sbSql.append("       to_char(b.FIRST_GETCAR_DATE,'yyyy-mm-dd') FIRST_GETCAR_DATE,\n");
			sbSql.append("       to_char(b.FIRST_SAELS_DATE,'yyyy-mm-dd') FIRST_SAELS_DATE,\n");
			sbSql.append("       b.UNION_TYPE,\n");
			sbSql.append("       b.FIXED_CAPITAL,\n");
			sbSql.append("       b.REGISTERED_CAPITAL,\n");
			sbSql.append("       b.PEOPLE_NUMBER,\n");
			sbSql.append("       b.OFFICE_AREA,\n");
			sbSql.append("       b.PARTS_AREA,\n");
			sbSql.append("       b.DEPOT_AREA,\n");
			sbSql.append("       b.MAIN_AREA,\n");
			sbSql.append("       vods.root_org_id,\n");
			sbSql.append("       vods.root_org_name,\n");
			sbSql.append("       b.ONLY_MONTH_COUNT,\n");
			sbSql.append("       b.OPENING_TIME,\n");
			sbSql.append("       b.WORK_TYPE,\n");
			sbSql.append("       b.IS_FOUR_S,\n");
			sbSql.append("       b.COMPANY_ADDRESS,\n");
			sbSql.append("       b.AUTHORIZATION_TYPE,\n");
			sbSql.append("       b.AUTHORIZATION_DATE,\n");
			sbSql.append("       b.IS_ACTING_BRAND,\n");
			sbSql.append("       b.ACTING_BRAND_NAME,\n");
			sbSql.append("       b.PARTS_STORE_AMOUNT,\n");
			sbSql.append("       a.REMARK,\n");
			sbSql.append("       b.SHOP_TYPE,\n");
			sbSql.append("       b.HOTLINE,\n");
			
			sbSql.append("       b.FITTINGS_DEC_NAME,\n");
			sbSql.append("       b.FITTINGS_DEC_TELPHONE,\n");
			sbSql.append("       b.FITTINGS_DEC_PHONE,\n");
			sbSql.append("       b.FITTINGS_DEC_EMAIL,\n");
			sbSql.append("       b.FITTINGS_DEC_FAX,\n");
			sbSql.append("       b.*,\n");
			sbSql.append("       A.*,\n");
			
			sbSql.append("       b.FAX_NO,\n");
			sbSql.append("       b.EMAIL,\n");
			sbSql.append("       b.IS_AUTHORIZE_CITY,\n");
			sbSql.append("       b.IS_AUTHORIZE_COUNTY,\n");
			sbSql.append("       b.AUTHORIZE_BRAND,\n");
			sbSql.append("       to_char(b.AUTHORIZE_GET_DATE,'yyyy-mm-dd') AUTHORIZE_GET_DATE,\n");
			sbSql.append("       to_char(b.AUTHORIZE_SUB_DATE,'yyyy-mm-dd') AUTHORIZE_SUB_DATE,\n");
			sbSql.append("       to_char(b.AUTHORIZE_EFFECT_DATE,'yyyy-mm-dd') AUTHORIZE_EFFECT_DATE,\n");
			sbSql.append("       b.ANNOUNCEMENT_NO,\n");
			sbSql.append("       to_char(b.ANNOUNCEMENT_DATE,'yyyy-mm-dd') ANNOUNCEMENT_DATE,\n");
			sbSql.append("       to_char(b.ANNOUNCEMENT_END_DATE,'yyyy-mm-dd') ANNOUNCEMENT_END_DATE,\n");
			sbSql.append("       c.COMPANY_NAME,\n");
			sbSql.append("       c.COMPANY_ID,\n");
			sbSql.append("       (select org_name from tm_org where org_code = d.ORG_CODE) org_code,\n");
			sbSql.append("       a.DEALER_ORG_ID,\n");
			sbSql.append("       (select dealer_name from tm_dealer where dealer_id = a.parent_dealer_d) as PARENT_DEALER_NAME\n");
			sbSql.append("  from tmp_tm_dealer a, tmp_tm_dealer_detail b, tm_company c, tm_org d,vw_org_dealer_service vods\n");
			sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
			sbSql.append("   and a.dealer_id = vods.dealer_id\n");
			sbSql.append("   and a.company_id = c.company_id\n"); 
			sbSql.append("   and a.dealer_org_id = d.org_id(+)\n"); 
//			sbSql.append("	 and a.delete_flag = 0");
			sbSql.append("	 and a.ID = ?");
			params.add(ID);
			
			ProductManageDao dao = ProductManageDao.getInstance();
			
			Map<String, Object> dealerData = dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());
			
			act.setOutData("dMap", dealerData);
			
			act.setForword(queryDealerCsInitDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}