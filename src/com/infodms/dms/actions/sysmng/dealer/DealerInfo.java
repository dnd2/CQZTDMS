package com.infodms.dms.actions.sysmng.dealer;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.sales.storageManage.AddressAddApply;
import com.infodms.dms.base.CompanyBase;
import com.infodms.dms.base.OrgBase;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.base.AddressAreaDAO;
import com.infodms.dms.dao.claim.authorization.BalanceMainDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.dao.orgmng.DlrInfoMngDAO;
import com.infodms.dms.dao.parts.baseManager.partServicerManager.PartDlrDao;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmAuthDealerChangePO;
import com.infodms.dms.po.TmBillingInfoPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerBusinessAreaPO;
import com.infodms.dms.po.TmDealerChangePO;
import com.infodms.dms.po.TmDealerCreditPO;
import com.infodms.dms.po.TmDealerDetailPO;
import com.infodms.dms.po.TmDealerOrgRelationPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDealerPriceRelationPO;
import com.infodms.dms.po.TmDealerSecondLevelPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.po.TmpTmDealerDetailPO;
import com.infodms.dms.po.TmpTmDealerPO;
import com.infodms.dms.po.TmpTtProxyAreaPO;
import com.infodms.dms.po.TtAsDealerTypeAuthitemPO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtDealerSecendAuditPO;
import com.infodms.dms.po.TtDownloadLogPO;
import com.infodms.dms.po.TtPartBillDefinePO;
import com.infodms.dms.po.TtProxyAreaPO;
import com.infodms.dms.po.TtViConstructAuditPO;
import com.infodms.dms.po.TtViConstructDetailPO;
import com.infodms.dms.po.TtViConstructMainPO;
import com.infodms.dms.po.TtVsPricePO;
import com.infodms.dms.po.VwOrgDealerPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

import flex.messaging.util.StringUtils;

/**
 * @Title: CHANADMS
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-5
 * 
 * @author zjy
 * @mail zhaojinyu@infoservice.com.cn
 * @version 1.0
 * @remark
 */
public class DealerInfo {
    private final String serviceQueryDealerInitUrl2ndforAudit = "/jsp/systemMng/dealer/serviceDealerInfoQry2ndForAudit.jsp";
    public Logger logger = Logger.getLogger(DealerInfo.class);
    private final String viConstructQry = "/jsp/systemMng/dealer/viConstructQry.jsp";
    
    private final String queryBindingRelationUrl = "/jsp/systemMng/dealer/dealerBindingRelation.jsp";
    private final String addBindingRelationInfoUrl = "/jsp/systemMng/dealer/addBindingRelationInfoUrl.jsp";
    
    private final String viConstructAuditQry = "/jsp/systemMng/dealer/viConstructAuditQry.jsp";
    private final String queryDealerInitUrl = "/jsp/systemMng/dealer/dealerInfo.jsp";
    private final String querySalesDealerInitUrl = "/jsp/systemMng/dealer/salesDealerInfo.jsp";//2017-08-14
    
    private final String dealerAddSetInitUrl = "/jsp/systemMng/orgMng/dealerAddSet.jsp";
    private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
    private final String queryDealerCsInitUrl = "/jsp/systemMng/dealer/dealerCsInfo.jsp";
    private final String secendQueryDealerCsInitUrl = "/jsp/systemMng/dealer/2ndDealerCsInfo.jsp";

    private final String queryDealerInitDetailUrl = "/jsp/systemMng/dealer/dealerInfoDetail.jsp";
    private final String addSalesDealerUrl = "/jsp/systemMng/dealer/addSalesDealer.jsp";       //2017-08-24
    private final String querySalesDealerInitDetailUrl = "/jsp/systemMng/dealer/salesDealerInfoDetail.jsp";//2017-08-14

    private final String addNewDealerUrl = "/jsp/systemMng/dealer/addNewDealer.jsp";
    private final String addNewCsDealerUrl = "/jsp/systemMng/dealer/addNewCsDealer.jsp";
    private final String dealerCsInfoForDealerInit = "/jsp/systemMng/dealer/dealerCsInfoForDealerInit.jsp";
    private final String addNewDealerUrl2nd = "/jsp/systemMng/dealer/addNewDealer2nd.jsp";

    private final String addBusinessUrl = "/jsp/systemMng/dealer/addBusiness.jsp";
    private final String addAddressUrl = "/jsp/systemMng/dealer/addAddress.jsp";
    private final String modifyAddressUrl = "/jsp/systemMng/dealer/modifyAddress.jsp";
    private final String getPriceUrl = "/jsp/systemMng/dealer/getPrice.jsp";
    private final String getMyPriceUtl = "/jsp/systemMng/dealer/myPrice.jsp";
    private final String BALANCE_STOP_URL = "/jsp/systemMng/dealer/balanceStopInit.jsp";
    private final String BALANCE_STOP_UPDATE = "/jsp/systemMng/dealer/balanceStopUpdate.jsp";
    private final String printPage = "/jsp/systemMng/dealer/dealerAddressPrintPage.jsp";
    private final String printPageNew = "/jsp/systemMng/dealer/dealerAddressPrintPageNew.jsp";

    private final String dealerInfoChangeInitUrl = "/jsp/systemMng/dealer/dealerInfoChangeInit.jsp";
    private final String addDealerInfoChangeUrl = "/jsp/systemMng/dealer/addDealerInfoChange.jsp";
    private final String dealerDetailInfo = "/jsp/systemMng/dealer/dealerDetailInfo.jsp";
    private final String updateDealerDetailInfo = "/jsp/systemMng/dealer/updateDealerDetailInfo.jsp";

    private final String authDealerChangeInitUrl = "/jsp/systemMng/dealer/authDealerChangeInit.jsp";
    private final String authDetailInfo = "/jsp/systemMng/dealer/authDetailInfo.jsp";
    private final String dealerChangeInfo = "/jsp/systemMng/dealer/dealerChangeInfo.jsp";
    private final String dealerAddressPrintInit = "/jsp/systemMng/dealer/dealerAddressPrintInit.jsp";

    private final String DealerCreditQuery = "/jsp/systemMng/dealer/DealerCreditQuery.jsp";
    private final String addNewDealerCredit = "/jsp/systemMng/dealer/addNewDealerCredit.jsp";

    private final String addViConstructUrl = "/jsp/systemMng/dealer/addViConstruct.jsp";
    private final String addViConstructMpvUrl = "/jsp/systemMng/dealer/addViConstructMpv.jsp";
    private final String viConstructAudit = "/jsp/systemMng/dealer/viConstructAudit.jsp";
    private final String checkViConstruct = "/jsp/systemMng/dealer/checkViConstruct.jsp";
    private final String checkViConstructDLR = "/jsp/systemMng/dealer/checkViConstructDLR.jsp";
    private final String checkViConstructDivShow = "/jsp/systemMng/dealer/checkViConstructDivShow.jsp";
    private final String updateViConstruct = "/jsp/systemMng/dealer/updateViConstruct.jsp";
    private final String updateViConstructMpvUrl = "/jsp/systemMng/dealer/updateViConstructMpv.jsp";
    
    private final String dealerCsInfoForDealer = "/jsp/systemMng/dealer/dealerCsInfoForDealer.jsp";
    
    private final String dealerChangeInfoQuery = "/jsp/systemMng/dealer/dealerChangeInfoQuery.jsp";
    
    private final String dealerChangeInfoAudit = "/jsp/systemMng/dealer/dealerChangeInfoAudit.jsp";

    private POFactory factory = POFactoryBuilder.getInstance();

    
    
    /**
     * 销售 经销商维护查询页面初始化  2017-08-14新增
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void querySalesDealerInfoInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            act.setOutData("areaList", areaList);

            act.setForword(querySalesDealerInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护pre查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    
    /**
     * 销售 经销商维护查询结果页面 2017-08-14新增
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void querySalesDealerInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerCode = request.getParamValue("DEALER_CODE");
            String dealerName = request.getParamValue("DEALER_NAME");
            String dealerLevel = request.getParamValue("DEALERLEVEL");
            String status = request.getParamValue("DEALERSTATUS");
            String sJdealerCode = request.getParamValue("sJDealerCode");
            String orgCode = request.getParamValue("orgCode");
            String dealerType = request.getParamValue("DEALERTYPE");
            String dealerClass = request.getParamValue("DEALERCLASS");
            String province = request.getParamValue("province");
            String areaId = request.getParamValue("areaId");
            // 经销商ID
            String companyId = request.getParamValue("COMPANY_ID");
            String oemCompanyId = logonUser.getCompanyId().toString();
            DealerInfoDao dao = DealerInfoDao.getInstance();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            // Constant.PAGE_SIZE, curPage
            PageResult<Map<String, Object>> ps = dao.queryDealerInfo(areaId, dealerClass, dealerCode, dealerName, dealerLevel, status, sJdealerCode, orgCode, dealerType, companyId, oemCompanyId, province, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
            act.setForword(queryDealerInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 销售 经销商维护修改页面  2017-08-14新增
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void querySalesDealerInfoDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerId = request.getParamValue("DEALER_ID");
            DealerInfoDao dao = DealerInfoDao.getInstance();
            ProductManageDao productDao = new ProductManageDao();
            //List<Map<String, Object>> types = productDao.getmyPriceTypeList(logonUser.getCompanyId(), dealerId);
            //List<Map<String, Object>> types = new ArrayList();
            List<Map<String, Object>> priceList = productDao.getDealerPrice(logonUser.getCompanyId(), dealerId);
            act.setOutData("types", priceList);
            querySalesDealerInfo(act, dao, dealerId, querySalesDealerInitDetailUrl);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护修改");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 去经销商新增界面 2017-08-24新增
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void goAddSalesDealer() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //RequestWrapper request = act.getRequest();


            act.setForword(addSalesDealerUrl);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护修改");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    
    /**
     * 新增经销商信息  2017-08-24
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void saveSalesDealerInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));// 经销商代码
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));// 经销商名称
            String shortName = CommonUtils.checkNull(request.getParamValue("SHORT_NAME"));// 简称
            String dealerLevel = CommonUtils.checkNull(request.getParamValue("DEALERLEVEL"));// 经销商等级
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));// 上级组织ID
            String sJDealerId = CommonUtils.checkNull(request.getParamValue("sJDealerId"));// 上级经销商ID
            String dealerType = CommonUtils.checkNull(request.getParamValue("DEALERTYPE"));// 经销商类型
            String companyId = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));// 经销商公司ID
            String provinceId = CommonUtils.checkNull(request.getParamValue("province"));// 省
            String cityId = CommonUtils.checkNull(request.getParamValue("city"));// 市
            String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));// 邮编
            String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
            String phone = CommonUtils.checkNull(request.getParamValue("phone"));// 电话
            //String linkManPhone = CommonUtils.checkNull(request.getParamValue("linkManPhone"));// 电话

            String faxNo = CommonUtils.checkNull(request.getParamValue("faxNo"));// 传真
            String email = CommonUtils.checkNull(request.getParamValue("email"));// 邮箱
            String address = CommonUtils.checkNull(request.getParamValue("address"));// 详细地址\
            String billAddress = CommonUtils.checkNull(request.getParamValue("billAddress"));// 开票地址
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));// 备注
            String status = CommonUtils.checkNull(request.getParamValue("DEALERSTATUS"));// 经销商状态
            String dealerClass = CommonUtils.checkNull(request.getParamValue("DEALERCLASS"));// 经销商级别

            String erpCode = CommonUtils.checkNull(request.getParamValue("erpCode"));// 开票单位
            String taxesNo = CommonUtils.checkNull(request.getParamValue("taxesNo"));// 税号
            


            /** *********add by zhumingwei 2011-02-22************* */
            String COUNTIES = CommonUtils.checkNull(request.getParamValue("COUNTIES"));// 区/县
            String TOWNSHIP = CommonUtils.checkNull(request.getParamValue("TOWNSHIP"));// 乡
            String LEGAL = CommonUtils.checkNull(request.getParamValue("LEGAL"));// 法人
            String WEBMASTER_PHONE = CommonUtils.checkNull(request.getParamValue("WEBMASTER_PHONE"));// 站长电话
            String DUTY_PHONE = CommonUtils.checkNull(request.getParamValue("DUTY_PHONE"));// 值班电话
            String BANK = CommonUtils.checkNull(request.getParamValue("BANK"));// 开户行
            String MAIN_RESOURCES = CommonUtils.checkNull(request.getParamValue("MAIN_RESOURCES"));// 维修资源
            //String ADMIN_LEVEL = CommonUtils.checkNull(request.getParamValue("ADMIN_LEVEL"));// 行政级别
            String BALANCE_LEVEL =CommonUtils.checkNull(request.getParamValue("BALANCE_LEVEL"));//结算等级
            String INVOICE_LEVEL =CommonUtils.checkNull(request.getParamValue("INVOICE_LEVEL"));//开票等级
            /** *********add by zhumingwei 2011-02-22************* */

            // 经销商评级 没有
            // 校验新维护的经销商是否存在 开始
            // 结束
            // 插入新的经销商开始
            // 生成dealerId
            String dealerId = SequenceManager.getSequence("");
            // session中取到经销商的车厂公司ID和用户ID
            Long oemCompanyId = logonUser.getCompanyId();
            Long userId = logonUser.getUserId();
            Date currTime = new Date(System.currentTimeMillis());
            // 实例化dao层
            DealerInfoDao dao = DealerInfoDao.getInstance();
            List<TmOrgPO> list = dao.getOrgInfo(new Long(companyId));
            TmOrgPO tmo = new TmOrgPO();
            if (list.size() > 0) {
                tmo = (TmOrgPO) list.get(0);
            }
            TmDealerPO po = new TmDealerPO();
            po.setDealerId(new Long(dealerId));
            po.setDealerCode(dealerCode);


            po.setDealerName(dealerName);
            po.setDealerShortname(shortName);
            po.setCompanyId(new Long(companyId));
            po.setAddress(address);
            po.setBillAddress(billAddress);
            po.setErpCode(erpCode);
            
            if (!"".equals(taxesNo)) {
                po.setTaxesNo(taxesNo.trim());
            }
            if (!"".equals(cityId)) {
                po.setCityId(new Long(cityId));
            }
            if (!"".equals(provinceId)) {
                po.setProvinceId(new Long(provinceId));
            }
            po.setCreateBy(userId);
            po.setCreateDate(currTime);
            po.setDealerLevel(new Integer(dealerLevel));
            po.setDealerOrgId(tmo.getOrgId());
            po.setDealerType(new Integer(dealerType));
            /** *********add by zhumingwei 2011-02-22************* */
            if (!"".equals(COUNTIES)) {
                po.setCounties(Integer.parseInt(COUNTIES));
            }
            po.setTownship(TOWNSHIP);
            po.setLegal(LEGAL);
            po.setWebmasterPhone(WEBMASTER_PHONE);
            po.setDutyPhone(DUTY_PHONE);
            po.setBank(BANK);
            // po.setTaxLevel(TAX_LEVEL);//暂时还未用到
            if (!"".equals(MAIN_RESOURCES)) {
                po.setMainResources(Integer.parseInt(MAIN_RESOURCES));
            }

//          po.setImageDate(new Date());
//          if (!"".equals(ADMIN_LEVEL)) {
//              po.setAdminLevel(Integer.parseInt(ADMIN_LEVEL));
//          }
            /** *********add by zhumingwei 2011-02-22************* */

            if (String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel)) {
                po.setParentDealerD(new Long(-1));
            } else {
                po.setParentDealerD(new Long(sJDealerId));
            }
            if (dealerClass != null && dealerClass != "") {
                po.setDealerClass(Integer.parseInt(dealerClass));
            }
            po.setEmail(email);
            po.setFaxNo(faxNo);
            po.setLinkMan(linkMan);
            po.setOemCompanyId(oemCompanyId);
            po.setPhone(phone);
            po.setRemark(remark);
            po.setStatus(new Integer(status));
            po.setZipCode(zipCode);
            po.setBalanceLevel(BALANCE_LEVEL);
            po.setInvoiceLevel(INVOICE_LEVEL);
            /*
             * if(!"".equals(priceId)){ po.setPriceId(new Long(priceId)); }
             */
            dao.insert(po);
            TmBillingInfoPO tmBillingInfo = new TmBillingInfoPO();
            int deType = new Integer(dealerType);
            //整车销售
            if (deType == Constant.DEALER_TYPE_DVS || deType == Constant.DEALER_TYPE_DP){
            	
                String SALE_BILLING_TYPE =CommonUtils.checkNull(request.getParamValue("SALE_BILLING_TYPE"));
                String SALE_BILLING_UNIT =CommonUtils.checkNull(request.getParamValue("SALE_BILLING_UNIT"));
                String SALE_BANK =CommonUtils.checkNull(request.getParamValue("SALE_BANK"));
                String SALE_TAX_NO =CommonUtils.checkNull(request.getParamValue("SALE_TAX_NO"));
                String SALE_ACCOUNT =CommonUtils.checkNull(request.getParamValue("SALE_ACCOUNT"));
                String SALE_BILLING_ADDRESS =CommonUtils.checkNull(request.getParamValue("SALE_BILLING_ADDRESS"));
                //开票信息ID唯一序列
                String saleBillingInfoId = SequenceManager.getSequence("");
                tmBillingInfo.setBillingInfoId(new Long(saleBillingInfoId));
                tmBillingInfo.setDealerId(new Long(dealerId));
                tmBillingInfo.setDealerType(deType);
                tmBillingInfo.setBillingType(new Integer(SALE_BILLING_TYPE));
                tmBillingInfo.setBillingUnit(SALE_BILLING_UNIT);
                tmBillingInfo.setBank(SALE_BANK);
                tmBillingInfo.setTaxNo(SALE_TAX_NO);
                tmBillingInfo.setAccount(SALE_ACCOUNT);
                tmBillingInfo.setBillingAddress(SALE_BILLING_ADDRESS);
                tmBillingInfo.setCreateDate(currTime);
                tmBillingInfo.setStatus(10011001);
                tmBillingInfo.setCreateBy(new Long(logonUser.getUserId()));
                dao.insert(tmBillingInfo);
                
            }
            //售后服务
            if (deType == Constant.DEALER_TYPE_DWR || deType == Constant.DEALER_TYPE_DP){
            	
            	String AFTE_BILLING_TYPE =CommonUtils.checkNull(request.getParamValue("AFTE_BILLING_TYPE"));
                String AFTE_BILLING_UNIT =CommonUtils.checkNull(request.getParamValue("AFTE_BILLING_UNIT"));
                String AFTE_BANK =CommonUtils.checkNull(request.getParamValue("AFTE_BANK"));
                String AFTE_TAX_NO =CommonUtils.checkNull(request.getParamValue("AFTE_TAX_NO"));
                String AFTE_ACCOUNT =CommonUtils.checkNull(request.getParamValue("AFTE_ACCOUNT"));
                String AFTE_BILLING_ADDRESS =CommonUtils.checkNull(request.getParamValue("AFTE_BILLING_ADDRESS"));
                //开票信息ID唯一序列
                String saleBillingInfoId = SequenceManager.getSequence("");
                tmBillingInfo.setBillingInfoId(new Long(saleBillingInfoId));
                tmBillingInfo.setDealerId(new Long(dealerId));
                tmBillingInfo.setDealerType(deType);
                tmBillingInfo.setBillingType(new Integer(AFTE_BILLING_TYPE));
                tmBillingInfo.setBillingUnit(AFTE_BILLING_UNIT);
                tmBillingInfo.setBank(AFTE_BANK);
                tmBillingInfo.setTaxNo(AFTE_TAX_NO);
                tmBillingInfo.setAccount(AFTE_ACCOUNT);
                tmBillingInfo.setBillingAddress(AFTE_BILLING_ADDRESS);
                tmBillingInfo.setCreateDate(currTime);
                tmBillingInfo.setStatus(10011002);
                tmBillingInfo.setCreateBy(new Long(logonUser.getUserId()));
                dao.insert(tmBillingInfo);
            }
            if (String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel)) {
                String relationId = SequenceManager.getSequence("");
                TmDealerOrgRelationPO tdor = new TmDealerOrgRelationPO();
                tdor.setRelationId(new Long(relationId));
                tdor.setBusinessType(Constant.ORG_TYPE_OEM);
                tdor.setOrgId(new Long(orgId));
                tdor.setDealerId(new Long(dealerId));
                tdor.setCreateBy(userId);
                tdor.setCreateDate(currTime);
                dao.insert(tdor);
            }
            // 结束
            // 生成经销商对应的账户信息 开始

            // 结束
            //ProductManageDao productDao = new ProductManageDao();
            //List<Map<String, Object>> types = productDao.getmyPriceTypeList(logonUser.getCompanyId(), dealerId);
            //act.setOutData("types", types);
            //queryMonth(act, dao, dealerId, queryDealerInitDetailUrl);
            
            List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            act.setOutData("areaList", areaList);

            act.setForword(querySalesDealerInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    
    /**
     * 经销商维护查询页面初始化
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void queryDealerInfoInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            ProductManageDao dao = ProductManageDao.getInstance();
            List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            act.setOutData("areaList", areaList);
            List<Map<String, Object>> brand = dao.getbrandList();// 品牌
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setOutData("brand", brand);
            
            act.setForword(queryDealerInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护pre查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 经销商与售后 绑定关系
     */
    public void queryBindingRelation() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(queryBindingRelationUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护pre查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 查询经销商与售后绑定关系列表
     */
    public void queryBindingRelationInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();
            String dealerId = CommonUtils.checkNull(request.getParamValue("xs_dealerId"));
            String shDealerId = CommonUtils.checkNull(request.getParamValue("sh_dealerId"));
            map.put("dealerId", dealerId);
            map.put("shDealerId", shDealerId);
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().queryBindingRelationInfo(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
            // act.setForword(queryDealerInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 去增加绑定关系页面
     */
    public void addBindingRelationInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("is_Add", true);
            act.setForword(addBindingRelationInfoUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
//  queryDealerInfoDetail
    /**
     * 去修改经销商绑定关系
     */
    public void editBindingRelationInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        List<Object> params = new ArrayList<Object>();
        StringBuffer sbSql = new StringBuffer();
        try {
            String relationId = CommonUtils.checkNull(request.getParamValue("relationId"));
            sbSql.append("select * from TT_DEALER_RELATION where relation_id = ?");
            params.add(relationId);
            ProductManageDao dao = ProductManageDao.getInstance();
            Map<String, Object> dealerData = dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());
            String xs_dealerCode = "";
            String xs_dealerName = "";
            String sh_dealerCode = "";
            String sh_dealerName = "";
            String xs_dealerId = dealerData.get("XS_DEALER_ID").toString();
            String sh_dealerId = dealerData.get("SH_DEALER_ID").toString();
            List<TmDealerPO> list = UserMngDAO.selectDealerById(xs_dealerId+","+sh_dealerId);
            for(TmDealerPO po : list){
                if((po.getDealerId().toString()).equals(xs_dealerId)){
                    xs_dealerCode = po.getDealerCode();
                    xs_dealerName = po.getDealerName();
                }else if((po.getDealerId().toString()).equals(sh_dealerId)){
                    sh_dealerCode = po.getDealerCode();
                    sh_dealerName = po.getDealerName();
                }
            }
            act.setOutData("xs_dealerCode", xs_dealerCode);
            act.setOutData("xs_dealerName", xs_dealerName);
            act.setOutData("sh_dealerCode", sh_dealerCode);
            act.setOutData("sh_dealerName", sh_dealerName);
            act.setOutData("dMap", dealerData);
            act.setOutData("is_Mod", true);
            act.setForword(addBindingRelationInfoUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 保存销售经销商与售后关系
     */
    @SuppressWarnings("unchecked")
    public void saveBindingRelationInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try{
            RequestWrapper request = act.getRequest();
            String relationId = CommonUtils.checkNull(request.getParamValue("relationId"));
            String xs_dealerId = CommonUtils.checkNull(request.getParamValue("xs_dealerId"));
            String sh_dealerId = CommonUtils.checkNull(request.getParamValue("sh_dealerId"));
            
            List<Object> params = new ArrayList<Object>();
            StringBuffer sbSql = new StringBuffer();
            
            List<TmDealerPO> list = UserMngDAO.selectDealerById(xs_dealerId+","+sh_dealerId);
            if(list == null || list.size() <2){
                logger.info("=======销售经销商或者售后经销商未查询到,销售经销商id："+xs_dealerId+"，售后经销商id："+sh_dealerId+"==========");
                new Exception();
            }
            Long xs_company_id = 0l;
            Long sh_company_id = 0l;
            for(TmDealerPO po : list){
                if((po.getDealerId().toString()).equals(xs_dealerId)){
                    xs_company_id = po.getCompanyId();
                }else if((po.getDealerId().toString()).equals(sh_dealerId)){
                    sh_company_id = po.getCompanyId();
                }
            }
            
            if("".equals(relationId)){//表示是增加
                relationId = SequenceManager.getSequence("");
                sbSql.append("insert into TT_DEALER_RELATION(RELATION_ID,Xs_Dealer_Id,Sh_Dealer_Id,Create_By,Create_Date,Xs_Company_Id,Sh_Company_Id,Status) "
                        + "values(?,?,?,?,?,?,?,?)");
                params.add(relationId);
                params.add(xs_dealerId);
                params.add(sh_dealerId);
                params.add(logonUser.getUserId());
                params.add(new Date());
                params.add(xs_company_id);
                params.add(sh_company_id);
                params.add(10011001);
                factory.insert(sbSql.toString(), params);
            }else{//修改
                sbSql.append("update TT_DEALER_RELATION set Xs_Dealer_Id = ?,Sh_Dealer_Id = ?,"
                        + "Update_By = ?,Update_Date = ?,Xs_Company_Id = ?,Sh_Company_Id = ? where RELATION_ID = ?");
                params.add(xs_dealerId);
                params.add(sh_dealerId);
                params.add(logonUser.getUserId());
                params.add(new Date());
                params.add(xs_company_id);
                params.add(sh_company_id);
                params.add(relationId);
                factory.update(sbSql.toString(), params);
            }
            act.setOutData("message", "操作成功!");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售经销商关系维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void addBd(){
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try{
            List<TmDealerPO> xsList = UserMngDAO.selectXsDealer("10771001");
            List<TmDealerPO> shList = UserMngDAO.selectXsDealer("10771002");
            String relationId = "";
            for(int i=0;i<xsList.size();i++){
                TmDealerPO xs = xsList.get(i);
                for(int ii=0;ii<shList.size();ii++){
                    TmDealerPO sh = shList.get(ii);
                    StringBuffer sbSql = new StringBuffer();
                    List<Object> params = new ArrayList<Object>();
                    if(xs.getCompanyId().toString().equals(sh.getCompanyId().toString())){
                        relationId = SequenceManager.getSequence("");
                        sbSql.append("insert into TT_DEALER_RELATION(RELATION_ID,Xs_Dealer_Id,Sh_Dealer_Id,Create_By,Create_Date,Xs_Company_Id,Sh_Company_Id,Status) "
                                + "values(?,?,?,?,?,?,?,?)");
                        params.add(relationId);
                        params.add(xs.getDealerId());
                        params.add(sh.getDealerId());
                        params.add(logonUser.getUserId());
                        params.add(new Date());
                        params.add(xs.getCompanyId());
                        params.add(xs.getCompanyId());
                        params.add(10011001);
                        factory.insert(sbSql.toString(), params);
                    }
                }
            }
            
        }catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售经销商关系维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 校验绑定的经销商和售后是否重复
     */
    public void checkBindingRelation() {
        String msg = "true";
        POFactory factory = POFactoryBuilder.getInstance();
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        String relationId = request.getParamValue("relationId");
        String xs_dealerId = CommonUtils.checkNull(request.getParamValue("xs_dealerId"));
        String sh_dealerId = CommonUtils.checkNull(request.getParamValue("sh_dealerId"));
        if("".equals(xs_dealerId) || "".equals(sh_dealerId)){
            msg = "false";
            logger.info("销售经销商id或者售后经销商id为空！");
        }else{
            List<Object> params = new ArrayList<Object>();
            StringBuffer sbSql = new StringBuffer();
            sbSql.append("select * from TT_DEALER_RELATION where (xs_dealer_id = ? AND sh_dealer_id = ?) ");
            params.add(xs_dealerId);
            params.add(sh_dealerId);
            //如果为空表示是增加
            if(relationId != null && !"".equals(relationId)){//修改
                sbSql.append("  and relation_id <> ?");
                params.add(relationId);
            }
            List list = factory.select(sbSql.toString(), params, new JCDynaBeanCallBack());
            if (list.size() > 0) {
                msg = "false";
            }
        }
        act.setOutData("msg", msg);
    }
    
    
    
    

    /**
     * 经销商地址维护页面初始化
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void dealerAddMaintenanceInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            // act.setOutData("typeList", initTypeTopSelect());
            act.setForword(dealerAddSetInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 售后经销商维护查询页面初始化
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void queryDealerCsInfoInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            ProductManageDao dao = ProductManageDao.getInstance();
            List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            act.setOutData("areaList", areaList);
            List<Map<String, Object>> brand = dao.getbrandList();// 品牌
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setOutData("brand", brand);
            act.setForword(queryDealerCsInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商维护pre查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void query2ndDealerCsInfoInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            if (logonUser.getDealerId() == null) {
                act.setOutData("isDealer", "yes");
            } else {
                act.setOutData("isDealer", "no");
            }
            ProductManageDao dao = ProductManageDao.getInstance();
            List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            act.setOutData("user_id", Constant.DEALER_SECEND_AUDIT_02);
            act.setOutData("areaList", areaList);
            List<Map<String, Object>> brand = dao.getbrandList();// 品牌
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setOutData("brand", brand);
            act.setForword(secendQueryDealerCsInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商维护pre查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商维护新增页面
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void addNewDealer() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            ProductManageDao dao = ProductManageDao.getInstance();
            // List<Map<String, Object>> types = dao.getPriceTypeList(logonUser
            // .getCompanyId());

            List<Map<String, Object>> brand = dao.getbrandList();// 品牌
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            // act.setOutData("types", types);
            act.setOutData("brand", brand);
            act.setOutData("is_Add", true);
            act.setForword(addNewDealerUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 售后经销商维护新增页面
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void addNewCsDealer() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            ProductManageDao dao = ProductManageDao.getInstance();
            List<Map<String, Object>> types = dao.getPriceTypeList(logonUser.getCompanyId());

            List<Map<String, Object>> brand = dao.getbrandList();// 品牌
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setOutData("types", types);
            act.setOutData("brand", brand);
            act.setOutData("is_Add", "true");
            act.setForword(addNewCsDealerUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addNewCsDealer2nd() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            ProductManageDao dao = ProductManageDao.getInstance();
            List<Map<String, Object>> types = dao.getPriceTypeList(logonUser.getCompanyId());

            TmCompanyPO companyPO = new TmCompanyPO();
            companyPO.setCompanyId(logonUser.getCompanyId());
            List<Map<String, Object>> companyInfo = ajaxDao.select(companyPO);
            act.setOutData("companyInfo", companyInfo.get(0));

            TmDealerPO dealerPO = new TmDealerPO();
            dealerPO.setDealerId(Long.parseLong(logonUser.getDealerId()));
            List<Map<String, Object>> dealerInfo = ajaxDao.select(dealerPO);
            act.setOutData("dealerInfo", dealerInfo.get(0));

            VwOrgDealerPO vwOrgDealerPO = new VwOrgDealerPO();
            vwOrgDealerPO.setDealerId(dealerPO.getDealerId());
            List<Map<String, Object>> vwOrgDealerInfo = ajaxDao.select(vwOrgDealerPO);

            List<TmDealerPO> TmDealerPO = ajaxDao.select(dealerPO);
            String dealer_code = TmDealerPO.get(0).getDealerCode();
            act.setOutData("vwOrgDealerInfo", vwOrgDealerInfo.get(0));
            
            List<Object> inParameter = new ArrayList<Object>();// 输入参数
            inParameter.add(TmDealerPO.get(0).getDealerId());
            inParameter.add(logonUser.getUserId());
            List outParameter = new ArrayList();// 输出参数
            outParameter.add(Types.VARCHAR);

            outParameter = factory.callProcedure("P_GET_SNDDEALERCODE",inParameter,outParameter);
            String dealer_code_secend = outParameter.get(0)==null?"-1":outParameter.get(0).toString();
//          DealerInfoDao dealerInfoDao = DealerInfoDao.getInstance();
//
//          String codeNum = dealerInfoDao.getSecendDealerNum(dealer_code.substring(0, 6));
//
//          String dealer_code_secend = dealer_code.substring(0, 6) + codeNum + "-S";

            StringBuffer sb = new StringBuffer();
            sb.append("select * from tm_dealer_detail where fk_dealer_id = " + TmDealerPO.get(0).getDealerId());
            act.setOutData("COMPANY_ZC_CODE", dao.pageQueryMap(sb.toString(), null, dao.getFunName()).get("COMPANY_ZC_CODE"));

            List<Map<String, Object>> brand = dao.getbrandList();// 品牌
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setOutData("types", types);
            act.setOutData("brand", brand);
            act.setOutData("dealer_code_secend", dealer_code_secend);
            act.setOutData("is_Add", "true");
            act.setForword(addNewDealerUrl2nd);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商维护查询结果页面
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void queryDealerInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            map.put("parentOrgCode", CommonUtils.checkNull(request.getParamValue("parentOrgCode")));// 上级组织
            map.put("orgCode", CommonUtils.checkNull(request.getParamValue("orgCode")));// 大区
            map.put("regionCode", CommonUtils.checkNull(request.getParamValue("regionCode")));
            map.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));// 授权类型
            map.put("WORK_TYPE", CommonUtils.checkNull(request.getParamValue("WORK_TYPE")));// 经营类型
            map.put("IMAGE_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL")));// 形象等级
            map.put("IMAGE_COMFIRM_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL")));// 形象等级
            map.put("regionId", CommonUtils.checkNull(request.getParamValue("regionId")));

            map.put("SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("SCREATE_DATE")));// 开始时间
            map.put("ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("ECREATE_DATE")));// 结束时间

            map.put("AUTHORIZATION_SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_SCREATE_DATE")));// 授权开始时间
            map.put("AUTHORIZATION_ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_ECREATE_DATE")));// 授权结束时间

            map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
            map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            map.put("DEALER_LEVEL", CommonUtils.checkNull(request.getParamValue("DEALERLEVEL")));
            map.put("DEALER_STATUS", CommonUtils.checkNull(request.getParamValue("DEALERSTATUS")));
            map.put("ORGCODE", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
            map.put("sJDealerCode", CommonUtils.checkNull(request.getParamValue("sJDealerCode")));
            map.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            map.put("COMPANY_NAME", CommonUtils.checkNull(request.getParamValue("COMPANY_NAME")));
            map.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));// 经销商状态

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            // Constant.PAGE_SIZE, curPage
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().queryDealerInfo(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
            // act.setForword(queryDealerInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void query2ndDealerInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            map.put("parentOrgCode", CommonUtils.checkNull(request.getParamValue("parentOrgCode")));// 上级组织
            map.put("orgCode", CommonUtils.checkNull(request.getParamValue("orgCode")));// 大区
            map.put("regionCode", CommonUtils.checkNull(request.getParamValue("regionCode")));
            map.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));// 授权类型
            map.put("WORK_TYPE", CommonUtils.checkNull(request.getParamValue("WORK_TYPE")));// 经营类型
            map.put("IMAGE_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL")));// 形象等级
            map.put("IMAGE_COMFIRM_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL")));// 形象等级
            map.put("regionId", CommonUtils.checkNull(request.getParamValue("regionId")));

            map.put("SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("SCREATE_DATE")));// 开始时间
            map.put("ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("ECREATE_DATE")));// 结束时间

            map.put("AUTHORIZATION_SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_SCREATE_DATE")));// 授权开始时间
            map.put("AUTHORIZATION_ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_ECREATE_DATE")));// 授权结束时间

            map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
            map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            map.put("DEALER_LEVEL", CommonUtils.checkNull(request.getParamValue("DEALERLEVEL")));
            map.put("DEALER_STATUS", CommonUtils.checkNull(request.getParamValue("DEALERSTATUS")));
            map.put("ORGCODE", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
            map.put("sJDealerCode", CommonUtils.checkNull(request.getParamValue("sJDealerCode")));
            map.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            map.put("COMPANY_NAME", CommonUtils.checkNull(request.getParamValue("COMPANY_NAME")));
            map.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));// 经销商状态
            map.put("status", CommonUtils.checkNull(request.getParamValue("status")));// 经销商状态
            String cPage = CommonUtils.checkNull(request.getParamValue("curPage"));
            Integer curPage;
            if(!cPage.equals("")){
                 curPage = Integer.parseInt(cPage); 
            }else{
                 curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            }
            
//          Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            // Constant.PAGE_SIZE, curPage
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().query2ndDealerInfo(map, curPage, Constant.PAGE_SIZE, logonUser);
            act.setOutData("ps", ps);
            // act.setForword(queryDealerInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryServiceDealerInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
            map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            map.put("DEALER_LEVEL", CommonUtils.checkNull(request.getParamValue("DEALERLEVEL")));
            map.put("DEALER_STATUS", CommonUtils.checkNull(request.getParamValue("DEALERSTATUS")));
            map.put("ORGCODE", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
            map.put("parentOrgCode", CommonUtils.checkNull(request.getParamValue("parentOrgCode")));
            map.put("sJDealerCode", CommonUtils.checkNull(request.getParamValue("sJDealerCode")));
            map.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            map.put("COMPANY_NAME", CommonUtils.checkNull(request.getParamValue("COMPANY_NAME")));
            map.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));

            map.put("regionId", CommonUtils.checkNull(request.getParamValue("regionId")));
            map.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));// 授权类型
            map.put("orgCode", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("SCREATE_DATE")));// 开始时间
            map.put("ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("ECREATE_DATE")));// 结束时间

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().queryServiceDealerInfo(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryServiceDealerInfo2nd() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
            map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            
            map.put("FIRST_DEALER_CODE", CommonUtils.checkNull(request.getParamValue("FIRST_DEALER_CODE")));
            map.put("FIRST_DEALER_NAME", CommonUtils.checkNull(request.getParamValue("FIRST_DEALER_NAME")));
            
            map.put("DEALER_LEVEL", CommonUtils.checkNull(request.getParamValue("DEALERLEVEL")));
            map.put("DEALER_STATUS", CommonUtils.checkNull(request.getParamValue("DEALERSTATUS")));
            map.put("ORGCODE", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
            map.put("parentOrgCode", CommonUtils.checkNull(request.getParamValue("parentOrgCode")));
            map.put("sJDealerCode", CommonUtils.checkNull(request.getParamValue("sJDealerCode")));
            map.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            map.put("COMPANY_NAME", CommonUtils.checkNull(request.getParamValue("COMPANY_NAME")));
            map.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));

            map.put("regionCode", CommonUtils.checkNull(request.getParamValue("regionCode")));
            map.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));// 授权类型
            map.put("orgCode", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("SCREATE_DATE")));// 开始时间
            map.put("ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("ECREATE_DATE")));// 结束时间
            map.put("user_id", CommonUtils.checkNull(request.getParamValue("user_id")));
            map.put("status", CommonUtils.checkNull(request.getParamValue("status")));
            String cPage = CommonUtils.checkNull(request.getParamValue("curPage1"));
            
            map.put("CendDate", CommonUtils.checkNull(request.getParamValue("CendDate")));
            map.put("CstartDate", CommonUtils.checkNull(request.getParamValue("CstartDate")));
            map.put("UstartDate", CommonUtils.checkNull(request.getParamValue("UstartDate")));
            map.put("UendDate", CommonUtils.checkNull(request.getParamValue("UendDate")));
            
            map.put("IS_QUALIFIED_SALES", CommonUtils.checkNull(request.getParamValue("IS_QUALIFIED_SALES")));
            map.put("IS_QUALIFIED_SERVICE", CommonUtils.checkNull(request.getParamValue("IS_QUALIFIED_SERVICE")));
            map.put("logonUser", logonUser);
            Integer curPage;
            if(!cPage.equals("")){
                 curPage = Integer.parseInt(cPage); 
            }else{
                 curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            }
            
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().queryServiceDealerInfo2nd(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void dealerInfoDownload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            ResponseWrapper response = act.getResponse();

            String dealerCode = request.getParamValue("DEALER_CODE");
            String dealerName = request.getParamValue("DEALER_NAME");
            String dealerLevel = request.getParamValue("DEALERLEVEL");
            String status = request.getParamValue("DEALERSTATUS");
            String sJdealerCode = request.getParamValue("sJDealerCode");
            String orgCode = request.getParamValue("orgCode");
            String dealerType = request.getParamValue("DEALERTYPE");
            String dealerClass = request.getParamValue("DEALERCLASS");
            String province = request.getParamValue("province");
            String areaId = request.getParamValue("areaId");
            // 经销商ID
            String companyId = request.getParamValue("COMPANY_ID");
            String oemCompanyId = logonUser.getCompanyId().toString();
            String serviceStatus = request.getParamValue("SERVICE_STATUS");
            DealerInfoDao dao = DealerInfoDao.getInstance();

            Map<String, String> map = new HashMap<String, String>();
            map.put("dealerCode", dealerCode);
            map.put("dealerName", dealerName);
            map.put("dealerLevel", dealerLevel);
            map.put("status", status);
            map.put("sJdealerCode", sJdealerCode);
            map.put("orgCode", orgCode);
            map.put("dealerType", dealerType);
            map.put("dealerClass", dealerClass);
            map.put("province", province);
            map.put("companyId", companyId);
            map.put("oemCompanyId", oemCompanyId);
            map.put("areaId", areaId);
            map.put("serviceStatus", serviceStatus);

            // 导出的文件名
            String fileName = "经销商信息.xls";
            OutputStream os = null;
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            List<List<Object>> contentList = new LinkedList<List<Object>>();

            List<Object> rowList = new LinkedList<Object>();

            // TODO 下载增加“经销商名称”、省份、经销商评级、税号、形象等级、维修资质、经销商等级、上级经销商字段
            rowList.add("序号");
            rowList.add("编码");
            rowList.add("上级经销商全称");
            rowList.add("经销商简称");
            rowList.add("经销商全称");
            rowList.add("省份");
            rowList.add("城市");
            rowList.add("邮编");
            rowList.add("地址");
            rowList.add("联系电话");
            rowList.add("传真");
            rowList.add("联系人");
            rowList.add("电子邮件");
            rowList.add("法人");
            rowList.add("值班电话");
            rowList.add("注册地址");
            rowList.add("昌铃形象地址");
            rowList.add("昌汽形象地址");
            rowList.add("站长姓名");
            rowList.add("是否连锁店 ");
            rowList.add("经销商等级 ");
            contentList.add(rowList);

            List<Map<String, Object>> dealerInfoList = dao.dealerInfoDownload(map);

            if (!CommonUtils.isNullList(dealerInfoList)) {
                int len = dealerInfoList.size();

                for (int i = 0; i < len; i++) {
                    rowList = new LinkedList<Object>();
                    rowList.add(i + 1);
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_CODE")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CDEALER_NAME")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_SHORTNAME")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_NAME")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("P_NAME")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("C_NAME")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZIP_CODE")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ADDRESS")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PHONE")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FAX_NO")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LINK_MAN")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("EMAIL")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DUTY_PHONE")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZZADDRESS")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CH_ADDRESS")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CH_ADDRESS2")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_NAME")));
                    rowList.add(CodeDict.getDictDescById(String.valueOf(Constant.IF_TYPE), CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_LABOUR_TYPE"))));
                    rowList.add(CodeDict.getDictDescById(Constant.DEALER_LEVEL.toString(), CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_LEVEL")))); // 新增经销商评级
                    contentList.add(rowList);
                }
            }

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(contentList, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护下载结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void dealerInfoDownloadNew() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

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
            String workType = CommonUtils.checkNull(request.getParamValue("WORK_TYPE"));
            String AUTHORIZATION_SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_SCREATE_DATE"));
            String AUTHORIZATION_ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_ECREATE_DATE"));
            String IMAGE_COMFIRM_LEVEL = CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL"));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ECREATE_DATE", ECREATE_DATE);
            map.put("SCREATE_DATE", SCREATE_DATE);
            map.put("regionId", regionId);
            map.put("AUTHORIZATION_TYPE", AUTHORIZATION_TYPE);
            map.put("dealerLevel", dealerLevel);
            map.put("sJDealerCode", sJDealerCode);
            map.put("dealerType", dealerType);
            map.put("orgCode", orgCode);
            map.put("dealerName", dealerName);
            map.put("orgId", orgId);
            map.put("serviceStatus", serviceStatus);
            map.put("parentOrgCode", parentOrgCode);
            map.put("dealerCode", dealerCode);
            map.put("companyName", companyName);
            map.put("dealerStatus", dealerStatus);
            map.put("regionCode", regionCode);
            map.put("workType", workType);

            map.put("IMAGE_COMFIRM_LEVEL", IMAGE_COMFIRM_LEVEL);

            map.put("AUTHORIZATION_SCREATE_DATE", AUTHORIZATION_SCREATE_DATE);
            map.put("AUTHORIZATION_ECREATE_DATE", AUTHORIZATION_ECREATE_DATE);
            // 导出的文件名
            String fileName = null;
            if (dealer_type.equals(Constant.DEALER_TYPE_DVS + "")) {
                fileName = "销售经销商信息.xls";
                // 1
                rowList.add("所属区域");
                rowList.add("省份");
                rowList.add("所属行政区域城市");
                rowList.add("所属行政区域区县");
                rowList.add("邮编");
                rowList.add("经销商编码");
                rowList.add("经销商状态");
                rowList.add("所属上级单位");
                // rowList.add("对应服务商编码");
                // rowList.add("对应服务商状态");
                // 2
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
                // 3
                rowList.add("代理其它品牌名称");
                rowList.add("经销商销售热线");
                rowList.add("经销商传真");
                rowList.add("经销商邮箱");
                rowList.add("代理车型");
                // rowList.add("代理区域");
                rowList.add("是否国家品牌授权城市");
                rowList.add("是否国家品牌授权区县");
                rowList.add("国家品牌授权");
                rowList.add("国家品牌授权信息收集时间");
                // 4
                rowList.add("国家品牌授权提交时间");
                rowList.add("国家品牌授权起始时间");
                rowList.add("工商总局公告号");
                rowList.add("工商总局公布日期");
                rowList.add("国家品牌授权截止时间");
                // rowList.add("品牌授权书打印");
                rowList.add("经销企业法人");
                rowList.add("法人办公电话");
                rowList.add("法人手机");
                rowList.add("法人邮箱");
                // 5
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
                // 6
                rowList.add("市场经理手机");
                rowList.add("市场经理邮箱");
                rowList.add("服务经理");
                rowList.add("服务经理办公电话");
                rowList.add("服务经理手机");
                rowList.add("服务经理邮箱");
                rowList.add("财务经理");
                rowList.add("财务经理办公电话");
                rowList.add("财务手机");
                rowList.add("财务邮箱");
                // 7
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
                // 8
                rowList.add("开户行全称");
                rowList.add("开户行账号");
                rowList.add("纳税识别号");
                // rowList.add("信函收件地址");
                // rowList.add("信函收件联系人");
                // rowList.add("信函收件人性别");
                // rowList.add("信函收件联系人办公电话");
                // rowList.add("信函收件联系人手机");
                rowList.add("VI建设申请日期");
                rowList.add("VI建设开工日期");
                // 9
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
                // 10
                rowList.add("首次到车日期");
                rowList.add("首次销售时间");
                rowList.add("备注");
            } else {
                fileName = "售后经销商信息.xls";
                // 1
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
                // 2
                rowList.add("经销商全称");
                rowList.add("经销商简称");
                rowList.add("上级经销商");
                rowList.add("与一级经销商关系");
                rowList.add("企业注册地址");
                rowList.add("企业注册证号");
                rowList.add("主营范围");
                rowList.add("兼营范围");
                rowList.add("组织机构代码");
                rowList.add("维修资质");
                // 3
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
                // 4
                rowList.add("营业时间");
                rowList.add("月平均维修能力(台次)");
                rowList.add("经营类型");
                rowList.add("是否4S店");
                rowList.add("建店类别");
                rowList.add("地址");
                rowList.add("是否有二级服务站");
                rowList.add("企业授权类型");
                rowList.add("授权时间");
                rowList.add("是否经营其他品牌");
                // 5
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
                // 6
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
                // 7
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
                // 8
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
                rowList.add("验收形象等级");
            }
            OutputStream os = null;
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            contentList.add(rowList);

            DealerInfoDao dao = DealerInfoDao.getInstance();
            List<Map<String, Object>> dealerInfoList = dao.queryServiceDealerInfoForXLS(map);

            if (dealer_type.equals(Constant.DEALER_TYPE_DVS + "")) {
                if (dealerInfoList != null && !dealerInfoList.isEmpty()) {
                    for (int i = 0; i < dealerInfoList.size(); i++) {
                        rowList = new ArrayList<Object>();
                        // 1
                        // rowList.add(i + 1);
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ROOT_ORG_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGION_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CITY_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COUNTIES_NAME")));// 区县
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZIP_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_STATUS_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_NAME")));
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//对应服务商编码
                        // // //2
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//对应服务商状态
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGISTERED_CAPITAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ZC_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_SHORTNAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ADDRESS")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ADDRESS")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("UNION_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WORK_TYPE_B")));
                        // //3
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_ACTING_BRAND_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ACTING_BRAND_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("HOTLINE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FAX_NO_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("EMAIL_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_VEHICLE_TYPE")));
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_AREA")))
                        // ;
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_AUTHORIZE_CITY_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_AUTHORIZE_COUNTY_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_BRAND")));
                        // //4
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_GET_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_SUB_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_EFFECT_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ANNOUNCEMENT_NO")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ANNOUNCEMENT_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ANNOUNCEMENT_END_DATE")));
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//品牌授权书打印
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_TELPHONE")));
                        // //5
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_EMAIL_A")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_NAME_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_PHONE_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_TELPHONE_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_EMAIL_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_NAME_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_NAME")));
                        // //6
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_EMAIL")));

                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_EMAIL")));
                        // //7
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_QQ")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ERP_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_PERSION")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ADD")));
                        // //8
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("BEGIN_BANK")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ACCOUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAXPAYER_NO")));
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件地址
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件联系人
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件人性别
                        //
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件联系人办公电话
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件联系人手机
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_APPLAY_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_BEGIN_DATE")));
                        // //9
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_COMPLETED_DATE")));//
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_CONFRIM_DATE")));//
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_COMFIRM_LEVEL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_AMOUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_RATIO")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_END_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIRST_SUB_DATE")));
                        // //10
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIRST_GETCAR_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIRST_SAELS_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REMARK")));

                        contentList.add(rowList);
                    }
                    // 下载日志保存
                    TtDownloadLogPO ttDownloadLogPO = new TtDownloadLogPO();
                    ttDownloadLogPO.setLogId(new Long(SequenceManager.getSequence("")));
                    ttDownloadLogPO.setDownloadTime(new Date());
                    ttDownloadLogPO.setLogDownloadBtn("销售经销商下载");
                    ttDownloadLogPO.setLogDownloadMenu("销售经销商维护");
                    ttDownloadLogPO.setDownloadUser(logonUser.getName());  
                    ttDownloadLogPO.setCreateBy(logonUser.getUserId());
                    ttDownloadLogPO.setCreateDate(new Date());
                    dao.insert(ttDownloadLogPO);
                }
            } else {
                if (dealerInfoList != null && !dealerInfoList.isEmpty()) {
                    for (int i = 0; i < dealerInfoList.size(); i++) {
                        rowList = new ArrayList<Object>();
                        // 1
                        rowList.add(i + 1);
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ROOT_ORG_NAME")));// 大区
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ORG_NAME")));// 所属组织
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGION_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CITY_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COUNTIES_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZIP_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_STATUS_NAME")));
                        // 2
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_SHORTNAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_RELATION")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ADDRESS")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZCCODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZY_SCOPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("JY_SCOPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ZC_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MAIN_RESOURCES_NAME")));
                        // 3
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SITE_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("UNION_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIXED_CAPITAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGISTERED_CAPITAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PEOPLE_NUMBER")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MAIN_AREA")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MEETING_ROOM_AREA")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARTS_AREA")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEPOT_AREA")));
                        // 4
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("OPENING_TIME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ONLY_MONTH_COUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WORK_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_FOUR_S")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ADDRESS")));// 公司地址
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_LOW_SER")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZATION_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZATION_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_ACTING_BRAND_NAME")));
                        // 5
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ACTING_BRAND_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("HOTLINE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_FAX")));
                        // 6
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_DIRECTOR_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_DIRECTOR_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_DIRECTOR_TELHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TECHNOLOGY_DIRECTOR_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TECHNOLOGY_DIRECTOR_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_FAX")));
                        // 7
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARTS_STORE_AMOUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("BEGIN_BANK")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ERP_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ACCOUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ADD")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAXPAYER_NO")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAXPAYER_NATURE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAX_INVOICE_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAX_DISRATE")));
                        // 8
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REMARK")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("BALANCE_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SPY_MAN")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("THE_AGENTS")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_VEHICLE_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_COMFIRM_LEVEL_NAME")));
                        // ;
                        contentList.add(rowList);
                    }

                    // 下载日志保存
                    TtDownloadLogPO ttDownloadLogPO = new TtDownloadLogPO();
                    ttDownloadLogPO.setLogId(new Long(SequenceManager.getSequence("")));
                    ttDownloadLogPO.setDownloadTime(new Date());
                    ttDownloadLogPO.setLogDownloadBtn("售后经销商下载");
                    ttDownloadLogPO.setLogDownloadMenu("售后经销商维护");
                    ttDownloadLogPO.setDownloadUser(logonUser.getName());  
                    ttDownloadLogPO.setCreateBy(logonUser.getUserId());
                    ttDownloadLogPO.setCreateDate(new Date());
                    dao.insert(ttDownloadLogPO);
                }
            }
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(contentList, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商EXCEL下载");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            rowList.clear();
            rowList = null;
            contentList.clear();
            contentList = null;
        }
    }

    public void dealerInfoDownload2nd() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

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
            String workType = CommonUtils.checkNull(request.getParamValue("WORK_TYPE"));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ECREATE_DATE", ECREATE_DATE);
            map.put("DEALER_ID", logonUser.getDealerId());
            map.put("SCREATE_DATE", SCREATE_DATE);
            map.put("regionId", regionId);
            map.put("AUTHORIZATION_TYPE", AUTHORIZATION_TYPE);
            map.put("dealerLevel", dealerLevel);
            map.put("sJDealerCode", sJDealerCode);
            map.put("dealerType", dealerType);
            map.put("orgCode", orgCode);
            map.put("dealerName", dealerName);
            map.put("orgId", orgId);
            map.put("serviceStatus", serviceStatus);
            map.put("parentOrgCode", parentOrgCode);
            map.put("dealerCode", dealerCode);
            map.put("companyName", companyName);
            map.put("dealerStatus", dealerStatus);
            map.put("regionCode", regionCode);
            map.put("workType", workType);
            // 导出的文件名
            String fileName = null;
            if (dealer_type.equals(Constant.DEALER_TYPE_DVS + "")) {
                fileName = "销售经销商信息.xls";
                // 1
                rowList.add("所属区域");
                rowList.add("省份");
                rowList.add("所属行政区域城市");
                rowList.add("所属行政区域区县");
                rowList.add("邮编");
                rowList.add("经销商编码");
                rowList.add("经销商状态");
                rowList.add("所属上级单位");
                // rowList.add("对应服务商编码");
                // rowList.add("对应服务商状态");
                // 2
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
                // 3
                rowList.add("代理其它品牌名称");
                rowList.add("经销商销售热线");
                rowList.add("经销商传真");
                rowList.add("经销商邮箱");
                rowList.add("代理车型");
                // rowList.add("代理区域");
                rowList.add("是否国家品牌授权城市");
                rowList.add("是否国家品牌授权区县");
                rowList.add("国家品牌授权");
                rowList.add("国家品牌授权信息收集时间");
                // 4
                rowList.add("国家品牌授权提交时间");
                rowList.add("国家品牌授权起始时间");
                rowList.add("工商总局公告号");
                rowList.add("工商总局公布日期");
                rowList.add("国家品牌授权截止时间");
                // rowList.add("品牌授权书打印");
                rowList.add("经销企业法人");
                rowList.add("法人办公电话");
                rowList.add("法人手机");
                rowList.add("法人邮箱");
                // 5
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
                // 6
                rowList.add("市场经理手机");
                rowList.add("市场经理邮箱");
                rowList.add("服务经理");
                rowList.add("服务经理办公电话");
                rowList.add("服务经理手机");
                rowList.add("服务经理邮箱");
                rowList.add("财务经理");
                rowList.add("财务经理办公电话");
                rowList.add("财务手机");
                rowList.add("财务邮箱");
                // 7
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
                // 8
                rowList.add("开户行全称");
                rowList.add("开户行账号");
                rowList.add("纳税识别号");
                // rowList.add("信函收件地址");
                // rowList.add("信函收件联系人");
                // rowList.add("信函收件人性别");
                // rowList.add("信函收件联系人办公电话");
                // rowList.add("信函收件联系人手机");
                rowList.add("VI建设申请日期");
                rowList.add("VI建设开工日期");
                // 9
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
                // 10
                rowList.add("首次到车日期");
                rowList.add("首次销售时间");
                rowList.add("备注");
            } else {
                fileName = "售后经销商信息.xls";
                // 1
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
                // 2
                rowList.add("经销商全称");
                rowList.add("经销商简称");
                rowList.add("上级经销商");
                rowList.add("与一级经销商关系");
                rowList.add("企业注册地址");
                rowList.add("企业注册证号");
                rowList.add("主营范围");
                rowList.add("兼营范围");
                rowList.add("组织机构代码");
                rowList.add("维修资质");
                // 3
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
                // 4
                rowList.add("营业时间");
                rowList.add("月平均维修能力(台次)");
                rowList.add("经营类型");
                rowList.add("是否4S店");
                rowList.add("建店类别");
                rowList.add("地址");
                rowList.add("是否有二级服务站");
                rowList.add("企业授权类型");
                rowList.add("授权时间");
                rowList.add("是否经营其他品牌");
                // 5
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
                // 6
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
                // 7
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
                // 8
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
                // rowList.add("代理区域");
            }
            OutputStream os = null;
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            contentList.add(rowList);

            DealerInfoDao dao = DealerInfoDao.getInstance();
            List<Map<String, Object>> dealerInfoList = dao.queryServiceDealerInfoForXLS2nd(map);

            if (dealer_type.equals(Constant.DEALER_TYPE_DVS + "")) {
                if (dealerInfoList != null && !dealerInfoList.isEmpty()) {
                    for (int i = 0; i < dealerInfoList.size(); i++) {
                        rowList = new ArrayList<Object>();
                        // 1
                        // rowList.add(i + 1);
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ROOT_ORG_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGION_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CITY_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COUNTIES_NAME")));// 区县
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZIP_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_STATUS_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_NAME")));
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//对应服务商编码
                        // // //2
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//对应服务商状态
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGISTERED_CAPITAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ZC_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_SHORTNAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ADDRESS")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ADDRESS")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("UNION_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WORK_TYPE_B")));
                        // //3
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_ACTING_BRAND_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ACTING_BRAND_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("HOTLINE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FAX_NO_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("EMAIL_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_VEHICLE_TYPE")));
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_AREA")))
                        // ;
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_AUTHORIZE_CITY_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_AUTHORIZE_COUNTY_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_BRAND")));
                        // //4
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_GET_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_SUB_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZE_EFFECT_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ANNOUNCEMENT_NO")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ANNOUNCEMENT_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ANNOUNCEMENT_END_DATE")));
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//品牌授权书打印
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_TELPHONE")));
                        // //5
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL_EMAIL_A")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_NAME_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_PHONE_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_TELPHONE_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WEBMASTER_EMAIL_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_NAME_B")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MARKET_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_NAME")));
                        // //6
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MANAGER_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_EMAIL")));

                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_EMAIL")));
                        // //7
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_QQ")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MESSAGER_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ERP_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_PERSION")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ADD")));
                        // //8
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("BEGIN_BANK")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ACCOUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAXPAYER_NO")));
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件地址
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件联系人
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件人性别
                        //
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件联系人办公电话
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_D")))
                        // ;//信函收件联系人手机
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_APPLAY_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_BEGIN_DATE")));
                        // //9
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_COMPLETED_DATE")));//
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_CONFRIM_DATE")));//
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_COMFIRM_LEVEL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_AMOUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_RATIO")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("VI_SUPPORT_END_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIRST_SUB_DATE")));
                        // //10
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIRST_GETCAR_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIRST_SAELS_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REMARK")));

                        contentList.add(rowList);
                    }
                }
            } else {
                if (dealerInfoList != null && !dealerInfoList.isEmpty()) {
                    for (int i = 0; i < dealerInfoList.size(); i++) {
                        rowList = new ArrayList<Object>();
                        // 1
                        rowList.add(i + 1);
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ROOT_ORG_NAME")));// 大区
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ORG_NAME")));// 所属组织
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGION_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CITY_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COUNTIES_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZIP_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SERVICE_STATUS_NAME")));
                        // 2
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_SHORTNAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARENT_DEALER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_RELATION")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ADDRESS")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZCCODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ZY_SCOPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("JY_SCOPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ZC_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MAIN_RESOURCES_NAME")));
                        // 3
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SITE_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("LEGAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("UNION_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FIXED_CAPITAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGISTERED_CAPITAL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PEOPLE_NUMBER")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MAIN_AREA")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("MEETING_ROOM_AREA")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARTS_AREA")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEPOT_AREA")));
                        // 4
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("OPENING_TIME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ONLY_MONTH_COUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("WORK_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_FOUR_S")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IMAGE_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("COMPANY_ADDRESS")));// 公司地址
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_LOW_SER")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZATION_TYPE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("AUTHORIZATION_DATE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("IS_ACTING_BRAND_NAME")));
                        // 5
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ACTING_BRAND_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("HOTLINE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_MANAGER_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CLAIM_DIRECTOR_FAX")));
                        // 6
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_DIRECTOR_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_DIRECTOR_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SER_DIRECTOR_TELHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TECHNOLOGY_DIRECTOR_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TECHNOLOGY_DIRECTOR_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FITTINGS_DEC_FAX")));
                        // 7
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PARTS_STORE_AMOUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("BEGIN_BANK")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ERP_CODE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ACCOUNT")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_ADD")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAXPAYER_NO")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAXPAYER_NATURE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAX_INVOICE_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("TAX_DISRATE")));
                        // 8
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_TELPHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("FINANCE_MANAGER_EMAIL")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REMARK")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("BALANCE_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("INVOICE_LEVEL_NAME")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("SPY_MAN")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PHONE")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("THE_AGENTS")));
                        rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_VEHICLE_TYPE")));
                        // rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("PROXY_AREA")))
                        // ;
                        contentList.add(rowList);
                    }
                }
            }
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(contentList, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商EXCEL下载");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            rowList.clear();
            rowList = null;
            contentList.clear();
            contentList = null;
        }
    }

    /**
     * 经销商维护修改页面
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void queryDealerInfoDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerId = request.getParamValue("DEALER_ID");
            String dealerType = request.getParamValue("DEALER_TYPE");

            List<Object> params = new ArrayList<Object>();
            StringBuffer sbSql = new StringBuffer();

            sbSql.append("select a.DEALER_ID,\n");
            sbSql.append("       a.COMPANY_ID,\n");
            sbSql.append("       a.DEALER_TYPE,\n");
            sbSql.append("       a.DEALER_CODE,\n");
            sbSql.append("       a.DEALER_NAME,\n");
            sbSql.append("       a.STATUS,\n");
            sbSql.append("       a.DEALER_LEVEL,\n");
            sbSql.append("       a.DEALER_CLASS,\n");
            sbSql.append("       a.PARENT_DEALER_D,\n");
            sbSql.append("       a.DEALER_ORG_ID,\n");
            sbSql.append("       a.PROVINCE_ID,\n");
            sbSql.append("       a.CITY_ID,\n");
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
            sbSql.append("       a.COUNTIES,\n");
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
            sbSql.append("       a.TAX_RATE_ID,\n");
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

            // sbSql.append("       b.*,\n");
            // sbSql.append("       a.*,\n");

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
            sbSql.append("       b.FAX_NO,\n");
            sbSql.append("       b.EMAIL,\n");
            sbSql.append("       b.THE_AGENTS,\n");
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
            sbSql.append("       d.ORG_CODE,\n");
            sbSql.append("       a.DEALER_ORG_ID,\n");
            sbSql.append("       b.COMPANY_ZC_CODE,\n");
            sbSql.append("       b.MEETING_ROOM_AREA,\n");
            sbSql.append("       b.FITTINGS_DEC_NAME,\n");
            sbSql.append("       b.FITTINGS_DEC_PHONE,\n");
            sbSql.append("       b.FITTINGS_DEC_FAX,\n");
            sbSql.append("       a.INVOICE_POST_ADD,\n");
            sbSql.append("       b.FITTINGS_DEC_TELPHONE,\n");
            sbSql.append("       b.FITTINGS_DEC_EMAIL,\n");

            sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
            sbSql.append("       b.PROXY_VEHICLE_TYPE,\n");
            sbSql.append("       b.PROXY_AREA,\n");
            sbSql.append("       b.IS_LOW_SER,\n");
            sbSql.append("       vwod.root_org_name,\n");
            sbSql.append("       vwod.root_org_id,\n");
            sbSql.append("       (select dealer_name from tm_dealer where dealer_id = a.parent_dealer_d) as PARENT_DEALER_NAME\n");
            sbSql.append("  from tm_dealer a, tm_dealer_detail b, tm_company c, tm_org d,vw_org_dealer_service vwod\n");
            sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
            sbSql.append("   and a.dealer_id = vwod.dealer_id(+)\n");
            sbSql.append("   and a.company_id = c.company_id\n");
            sbSql.append("   and a.dealer_org_id = d.org_id\n");
            sbSql.append("   and a.dealer_id = ?");
            params.add(dealerId);

            ProductManageDao dao = ProductManageDao.getInstance();

            Map<String, Object> dealerData = dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());

            TtProxyAreaPO areaPO = new TtProxyAreaPO();
            areaPO.setDealerId(Long.valueOf(dealerId));
            List<TtProxyAreaPO> list = factory.select(areaPO);
            String ProxyAreaName = "";
            String ProxyArea = "";
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    ProxyAreaName = ProxyAreaName + "," + list.get(i).getProxyAreaName();
                    ProxyArea = ProxyArea + "," + list.get(i).getProxyArea();
                } else {
                    ProxyAreaName = ProxyAreaName + list.get(i).getProxyAreaName();
                    ProxyArea = ProxyArea + list.get(i).getProxyArea();
                }

            }
            TmRegionPO t = new TmRegionPO();
            t.setRegionCode(dealerData.get("CITY_ID") + "");
            TmRegionPO tt = (TmRegionPO) dao.select(t).get(0);
            tt.getRegionName();
            act.setOutData("dMap", dealerData);
            act.setOutData("ProxyArea", ProxyArea);
            act.setOutData("ProxyAreaName", ProxyAreaName);
            act.setOutData("CITY_ID", dealerData.get("CITY_ID"));
            act.setOutData("CITY_NAME", tt.getRegionName());
            act.setOutData("ROOT_ORG_ID", dealerData.get("ROOT_ORG_ID"));
            act.setOutData("ROOT_ORG_NAME", dealerData.get("ROOT_ORG_NAME"));

            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            if (dealerData.get("DEALER_TYPE").toString().equals(String.valueOf(Constant.DEALER_TYPE_DVS))) {
                act.setOutData("is_Mod", true);
                act.setForword(addNewDealerUrl);
            } else {
                act.setOutData("is_Mod", true);
                act.setForword(addNewCsDealerUrl);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护修改");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryDealerInfoDetail2nd() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerId = request.getParamValue("DEALER_ID");
            String dealerType = request.getParamValue("DEALER_TYPE");

            TmDealerPO dealerPO = new TmDealerPO();
            dealerPO.setDealerId(Long.parseLong(dealerId));
            List<TmDealerPO> dealerInfo = ajaxDao.select(dealerPO);
            TmDealerPO dealerInfoMap = dealerInfo.get(0);
            act.setOutData("dealerInfo", dealerInfoMap);
            
            // 二级经销商信息取得
            if (Constant.DEALER_LEVEL_02.equals(dealerInfoMap.getDealerLevel())) {
                TmDealerSecondLevelPO tmDealerSecondLevelPO = new TmDealerSecondLevelPO();
                tmDealerSecondLevelPO.setFkDealerId(dealerInfoMap.getDealerId());
                List<TmDealerSecondLevelPO> tmDealerSecondLevelPOLst = factory.select(tmDealerSecondLevelPO);
                if (tmDealerSecondLevelPOLst != null && !tmDealerSecondLevelPOLst.isEmpty()) {
                    act.setOutData("tmDealerSecondLevelPO", tmDealerSecondLevelPOLst.get(0));
                } else {
                    act.setOutData("tmDealerSecondLevelPO", new TmDealerSecondLevelPO());
                }
            }

            TmDealerPO dealerInfo1 = (TmDealerPO) ajaxDao.select(dealerPO).get(0);

            TmCompanyPO companyPO = new TmCompanyPO();
            companyPO.setCompanyId(dealerInfo1.getCompanyId());
            List<Map<String, Object>> companyInfo = ajaxDao.select(companyPO);
            act.setOutData("companyInfo", companyInfo.get(0));

            VwOrgDealerPO vwOrgDealerPO = new VwOrgDealerPO();
            vwOrgDealerPO.setDealerId(dealerPO.getDealerId());
            List<Map<String, Object>> vwOrgDealerInfo = ajaxDao.select(vwOrgDealerPO);
            VwOrgDealerPO vwOrgDealerInfo1 = new VwOrgDealerPO();
            if (vwOrgDealerInfo != null) {
                vwOrgDealerInfo1 = (VwOrgDealerPO) ajaxDao.select(vwOrgDealerPO).get(0);
            }

            // TmDealerDetailPO dealerDetailPO = new TmDealerDetailPO();
            // dealerDetailPO.setFkDealerId(dealerInfo1.getDealerId());
            // dealerDetailPO = (TmDealerDetailPO)
            // ajaxDao.select(dealerDetailPO).get(0);

            StringBuffer sb = new StringBuffer();
            sb.append("select * from tm_dealer_detail where fk_dealer_id = " + dealerInfo1.getDealerId());
            ProductManageDao dao = ProductManageDao.getInstance();

            List<TmDealerPO> TmDealerPO = ajaxDao.select(dealerPO);
            String dealer_code = TmDealerPO.get(0).getDealerCode();
            act.setOutData("vwOrgDealerInfo", vwOrgDealerInfo.get(0));

            TtProxyAreaPO areaPO = new TtProxyAreaPO();
            areaPO.setDealerId(Long.valueOf(dealerId));
            List<TtProxyAreaPO> list = factory.select(areaPO);
            String ProxyAreaName = "";
            String ProxyArea = "";
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    ProxyAreaName = ProxyAreaName + "," + list.get(i).getProxyAreaName();
                    ProxyArea = ProxyArea + "," + list.get(i).getProxyArea();
                } else {
                    ProxyAreaName = ProxyAreaName + list.get(i).getProxyAreaName();
                    ProxyArea = ProxyArea + list.get(i).getProxyArea();
                }

            }
            TmRegionPO t = new TmRegionPO();
            t.setRegionCode(dealerInfo1.getCityId().toString());
            TmRegionPO tt = (TmRegionPO) dao.select(t).get(0);
            tt.getRegionName();
            act.setOutData("dMap", dao.pageQueryMap(sb.toString(), null, dao.getFunName()));
            act.setOutData("ProxyArea", ProxyArea);
            act.setOutData("ProxyAreaName", ProxyAreaName);
            act.setOutData("CITY_ID", dealerInfo1.getCityId());
            act.setOutData("dealerInfo1", dealerInfo1);
            act.setOutData("CITY_NAME", tt.getRegionName());
            act.setOutData("ROOT_ORG_ID", vwOrgDealerInfo1.getRootOrgId());

            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setOutData("is_Mod", true);

            String sql = "SELECT F.FJID,F.FILEID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ =" + dealerInfo1.getDealerId().toString() + "ORDER BY F.FJID";
            List<Map<String, Object>> attachList = dao.pageQuery(sql, null, dao.getFunName());
            if (attachList != null && attachList.size() > 0) {
                act.setOutData("attachList", attachList);
                act.setOutData("attachListSize", attachList.size());
            }

            act.setForword(addNewDealerUrl2nd);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护修改");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商修改维护
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void modifyDealerPrice() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String erpCode = request.getParamValue("erpCode");
            DealerInfoDao dao = DealerInfoDao.getInstance();
            dao.setDownDealerPrice(erpCode);
            queryDealerInfoInit();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商修改维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商维护保存
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void saveDealerInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        HashMap<String, Object> mapA = new HashMap<String, Object>();
        HashMap<String, Object> mapB = new HashMap<String, Object>();
//      HashMap<String, Object> mapC = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID")).trim();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE")).trim(); // 经销商代码
            String dealerType = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));
            String dealerOrgId = CommonUtils.checkNull(request.getParamValue("DEALER_ORG_ID"));
            String dealerLevel = CommonUtils.checkNull(request.getParamValue("DEALER_LEVEL"));
            String parentDealerD = CommonUtils.checkNull(request.getParamValue("PARENT_DEALER_D"));
            String balanceLevel = CommonUtils.checkNull(request.getParamValue("BALANCE_LEVEL")); // 结算等级
            String invoiceLevel = CommonUtils.checkNull(request.getParamValue("INVOICE_LEVEL")); // 开票等级
            String companyId = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
            
            logger.info(dealerOrgId+"................");
            
            if (dealerLevel.equals(Constant.DEALER_LEVEL_02.toString())) {
                if (parentDealerD.equals("")) {
                    throw new RuntimeException("无上级经销商!");
                } else {
                    // TODO 判断上级经销商是否存在并且有效
                    mapA.put("PARENT_DEALER_D", parentDealerD);
                }
            }

            if (dealerType.equals(String.valueOf(Constant.DEALER_TYPE_DWR))) {
                if (balanceLevel.equals(""))
                    throw new RuntimeException("无结算等级!");
                if (invoiceLevel.equals(""))
                    throw new RuntimeException("无开票等级!");
            }

            // TODO 判断小区是否存在
            // 经销商主数据
            mapA.put("COMPANY_ID", companyId);
            mapA.put("DEALER_CODE", dealerCode);
            mapA.put("DEALER_ORG_ID", dealerOrgId);
            mapA.put("DEALER_LEVEL", dealerLevel);

            mapA.put("DEALER_TYPE", dealerType);
            mapA.put("BALANCE_LEVEL", balanceLevel);
            mapA.put("INVOICE_LEVEL", invoiceLevel);

            mapA.put("DEALER_SHORTNAME", CommonUtils.checkNull(request.getParamValue("DEALER_SHORTNAME")));
            mapA.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            mapA.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));
            mapA.put("PROVINCE_ID", CommonUtils.checkNull(request.getParamValue("PROVINCE_ID")));
            mapA.put("CITY_ID", CommonUtils.checkNull(request.getParamValue("CITY_ID")));
            mapA.put("COUNTIES", CommonUtils.checkNull(request.getParamValue("COUNTIES")));
            mapA.put("ZIP_CODE", CommonUtils.checkNull(request.getParamValue("ZIP_CODE")));
            mapA.put("ADDRESS", CommonUtils.checkNull(request.getParamValue("ADDRESS")));
            mapA.put("ZCCODE", CommonUtils.checkNull(request.getParamValue("ZCCODE")));
            mapA.put("ZY_SCOPE", CommonUtils.checkNull(request.getParamValue("ZY_SCOPE")));
            mapA.put("JY_SCOPE", CommonUtils.checkNull(request.getParamValue("JY_SCOPE")));
            mapA.put("SITEDATE", CommonUtils.checkNull(request.getParamValue("SITEDATE")));
            mapA.put("DESTROYDATE", CommonUtils.checkNull(request.getParamValue("DESTROYDATE")));
            mapA.put("LEGAL", CommonUtils.checkNull(request.getParamValue("LEGAL")));
            mapA.put("LEGAL_PHONE", CommonUtils.checkNull(request.getParamValue("LEGAL_PHONE")));
            mapA.put("LEGAL_TELPHONE", CommonUtils.checkNull(request.getParamValue("LEGAL_TELPHONE")));
            mapA.put("LEGAL_EMAIL", CommonUtils.checkNull(request.getParamValue("LEGAL_EMAIL")));
            mapA.put("INVOICE_PERSION", CommonUtils.checkNull(request.getParamValue("INVOICE_PERSION")));
            mapA.put("INVOICE_TELPHONE", CommonUtils.checkNull(request.getParamValue("INVOICE_TELPHONE")));
            mapA.put("BEGIN_BANK", CommonUtils.checkNull(request.getParamValue("BEGIN_BANK")));
            mapA.put("ERP_CODE", CommonUtils.checkNull(request.getParamValue("ERP_CODE")));
            mapA.put("TAXES_NO", CommonUtils.checkNull(request.getParamValue("TAXES_NO")));
            mapA.put("INVOICE_ACCOUNT", CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT")));
            mapA.put("INVOICE_PHONE", CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE")));
            mapA.put("INVOICE_ADD", CommonUtils.checkNull(request.getParamValue("INVOICE_ADD")));
            mapA.put("TAXPAYER_NO", CommonUtils.checkNull(request.getParamValue("TAXPAYER_NO")));
            mapA.put("TAXPAYER_NATURE", CommonUtils.checkNull(request.getParamValue("TAXPAYER_NATURE")));
            mapA.put("TAX_INVOICE", CommonUtils.checkNull(request.getParamValue("TAX_INVOICE")));
            mapA.put("TAX_DISRATE", CommonUtils.checkNull(request.getParamValue("TAX_DISRATE")));
            mapA.put("REMARK", CommonUtils.checkNull(request.getParamValue("REMARK")));
            mapA.put("MAIN_RESOURCES", CommonUtils.checkNull(request.getParamValue("MAIN_RESOURCES")));
            mapA.put("TAX_RATE_ID", CommonUtils.checkNull(request.getParamValue("TAX_RATE_ID")));

            
            //增加经销商拼音 20150521 冉可
            String pinYinDealerName = request.getParamValue("DEALER_NAME");
            if(pinYinDealerName != null && !"".equals(pinYinDealerName)){
                pinYinDealerName = Utility.hanZiToPinyin(pinYinDealerName);
                mapA.put("PINYIN", pinYinDealerName);
            }
            
            // 经销商备注数据
            mapB.put("COMPANY_ZC_CODE", CommonUtils.checkNull(request.getParamValue("COMPANY_ZC_CODE")));
            mapB.put("WEBMASTER_NAME", CommonUtils.checkNull(request.getParamValue("WEBMASTER_NAME")));
            mapB.put("WEBMASTER_PHONE", CommonUtils.checkNull(request.getParamValue("WEBMASTER_PHONE")));
            mapB.put("WEBMASTER_TELPHONE", CommonUtils.checkNull(request.getParamValue("WEBMASTER_TELPHONE")));
            mapB.put("WEBMASTER_EMAIL", CommonUtils.checkNull(request.getParamValue("WEBMASTER_EMAIL")));
            mapB.put("MARKET_NAME", CommonUtils.checkNull(request.getParamValue("MARKET_NAME")));
            mapB.put("MARKET_PHONE", CommonUtils.checkNull(request.getParamValue("MARKET_PHONE")));
            mapB.put("MARKET_TELPHONE", CommonUtils.checkNull(request.getParamValue("MARKET_TELPHONE")));
            mapB.put("MARKET_EMAIL", CommonUtils.checkNull(request.getParamValue("MARKET_EMAIL")));
            mapB.put("MANAGER_NAME", CommonUtils.checkNull(request.getParamValue("MANAGER_NAME")));
            mapB.put("MANAGER_PHONE", CommonUtils.checkNull(request.getParamValue("MANAGER_PHONE")));
            mapB.put("MANAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("MANAGER_TELPHONE")));
            mapB.put("MANAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("MANAGER_EMAIL")));
            mapB.put("SER_MANAGER_NAME", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_NAME")));
            mapB.put("SER_MANAGER_PHONE", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_PHONE")));
            mapB.put("SER_MANAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_TELPHONE")));
            mapB.put("SER_MANAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_EMAIL")));
            mapB.put("CLAIM_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_NAME")));
            mapB.put("CLAIM_DIRECTOR_PHONE", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_PHONE")));
            mapB.put("CLAIM_DIRECTOR_TELPHONE", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_TELPHONE")));
            mapB.put("CLAIM_DIRECTOR_EMAIL", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_EMAIL")));
            mapB.put("CLAIM_DIRECTOR_FAX", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_FAX")));
            mapB.put("CUSTSER_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_NAME")));
            mapB.put("CUSTSER_DIRECTOR_PHONE", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_PHONE")));
            mapB.put("CUSTSER_DIRECTOR_TELPHONE", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_TELPHONE")));
            mapB.put("CUSTSER_DIRECTOR_EMAIL", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_EMAIL")));
            mapB.put("SER_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_NAME")));
            mapB.put("SER_DIRECTOR_PHONE", CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_PHONE")));
            mapB.put("SER_DIRECTOR_TELHONE", CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_TELHONE")));
            mapB.put("TECHNOLOGY_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_NAME")));
            mapB.put("TECHNOLOGY_DIRECTOR_TELPHONE", CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_TELPHONE")));
            mapB.put("WORKSHOP_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("WORKSHOP_DIRECTOR_NAME")));
            mapB.put("WORKSHOP_DIRECTOR_TELpHONE", CommonUtils.checkNull(request.getParamValue("WORKSHOP_DIRECTOR_TELpHONE")));
            mapB.put("FINANCE_MANAGER_NAME", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_NAME")));
            mapB.put("FINANCE_MANAGER_PHONE", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_PHONE")));
            mapB.put("FINANCE_MANAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_TELPHONE")));
            mapB.put("FINANCE_MANAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_EMAIL")));
            mapB.put("MESSAGER_NAME", CommonUtils.checkNull(request.getParamValue("MESSAGER_NAME")));
            mapB.put("MESSAGER_PHONE", CommonUtils.checkNull(request.getParamValue("MESSAGER_PHONE")));
            mapB.put("MESSAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("MESSAGER_TELPHONE")));
            mapB.put("MESSAGER_QQ", CommonUtils.checkNull(request.getParamValue("MESSAGER_QQ")));
            mapB.put("MESSAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("MESSAGER_EMAIL")));
            mapB.put("VI_APPLAY_DATE", CommonUtils.checkNull(request.getParamValue("VI_APPLAY_DATE")));
            mapB.put("VI_BEGIN_DATE", CommonUtils.checkNull(request.getParamValue("VI_BEGIN_DATE")));
            mapB.put("VI_COMPLETED_DATE", CommonUtils.checkNull(request.getParamValue("VI_COMPLETED_DATE")));
            mapB.put("VI_CONFRIM_DATE", CommonUtils.checkNull(request.getParamValue("VI_CONFRIM_DATE")));
            mapB.put("IMAGE_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL")));
            mapB.put("IMAGE_COMFIRM_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL")));
            mapB.put("VI_SUPPORT_AMOUNT", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_AMOUNT")));
            mapB.put("VI_SUPPORT_RATIO", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_RATIO")));
            mapB.put("VI_SUPPORT_TYPE", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_TYPE")));
            mapB.put("VI_SUPPORT_DATE", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_DATE")));
            mapB.put("VI_SUPPORT_END_DATE", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_END_DATE")));
            mapB.put("FIRST_SUB_DATE", CommonUtils.checkNull(request.getParamValue("FIRST_SUB_DATE")));
            mapB.put("FIRST_GETCAR_DATE", CommonUtils.checkNull(request.getParamValue("FIRST_GETCAR_DATE")));
            mapB.put("FIRST_SAELS_DATE", CommonUtils.checkNull(request.getParamValue("FIRST_SAELS_DATE")));
            mapB.put("UNION_TYPE", CommonUtils.checkNull(request.getParamValue("UNION_TYPE")));
            mapB.put("FIXED_CAPITAL", CommonUtils.checkNull(request.getParamValue("FIXED_CAPITAL")));
            mapB.put("REGISTERED_CAPITAL", CommonUtils.checkNull(request.getParamValue("REGISTERED_CAPITAL")));
            mapB.put("PEOPLE_NUMBER", CommonUtils.checkNull(request.getParamValue("PEOPLE_NUMBER")));
            mapB.put("OFFICE_AREA", CommonUtils.checkNull(request.getParamValue("OFFICE_AREA")));
            mapB.put("PARTS_AREA", CommonUtils.checkNull(request.getParamValue("PARTS_AREA")));
            mapB.put("DEPOT_AREA", CommonUtils.checkNull(request.getParamValue("DEPOT_AREA")));
            mapB.put("MAIN_AREA", CommonUtils.checkNull(request.getParamValue("MAIN_AREA")));
            mapB.put("ONLY_MONTH_COUNT", CommonUtils.checkNull(request.getParamValue("ONLY_MONTH_COUNT")));
            mapB.put("OPENING_TIME", CommonUtils.checkNull(request.getParamValue("OPENING_TIME")));
            mapB.put("WORK_TYPE", CommonUtils.checkNull(request.getParamValue("WORK_TYPE")));
            mapB.put("IS_FOUR_S", CommonUtils.checkNull(request.getParamValue("IS_FOUR_S")));
            mapB.put("IS_LOW_SER", CommonUtils.checkNull(request.getParamValue("IS_LOW_SER")));
            mapB.put("COMPANY_ADDRESS", CommonUtils.checkNull(request.getParamValue("COMPANY_ADDRESS")));
            mapB.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));
            mapB.put("AUTHORIZATION_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_DATE")));
            mapB.put("IS_ACTING_BRAND", CommonUtils.checkNull(request.getParamValue("IS_ACTING_BRAND")));
            mapB.put("ACTING_BRAND_NAME", CommonUtils.checkNull(request.getParamValue("ACTING_BRAND_NAME")));
            mapB.put("PARTS_STORE_AMOUNT", CommonUtils.checkNull(request.getParamValue("PARTS_STORE_AMOUNT")));
            mapB.put("SHOP_TYPE", CommonUtils.checkNull(request.getParamValue("SHOP_TYPE")));
            mapB.put("HOTLINE", CommonUtils.checkNull(request.getParamValue("HOTLINE")));
            mapB.put("FAX_NO", CommonUtils.checkNull(request.getParamValue("FAX_NO")));
            mapB.put("EMAIL", CommonUtils.checkNull(request.getParamValue("EMAIL")));
            mapB.put("IS_AUTHORIZE_CITY", CommonUtils.checkNull(request.getParamValue("IS_AUTHORIZE_CITY")));
            mapB.put("IS_AUTHORIZE_COUNTY", CommonUtils.checkNull(request.getParamValue("IS_AUTHORIZE_COUNTY")));
            mapB.put("AUTHORIZE_BRAND", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_BRAND")));
            mapB.put("AUTHORIZE_GET_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_GET_DATE")));
            mapB.put("AUTHORIZE_SUB_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_SUB_DATE")));
            mapB.put("AUTHORIZE_EFFECT_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_EFFECT_DATE")));
            mapB.put("ANNOUNCEMENT_NO ", CommonUtils.checkNull(request.getParamValue("ANNOUNCEMENT_NO ")));
            mapB.put("ANNOUNCEMENT_DATE", CommonUtils.checkNull(request.getParamValue("ANNOUNCEMENT_DATE")));
            mapB.put("ANNOUNCEMENT_END_DATE", CommonUtils.checkNull(request.getParamValue("ANNOUNCEMENT_END_DATE")));
            mapB.put("THE_AGENTS", CommonUtils.checkNull(request.getParamValue("THE_AGENTS")));

            mapB.put("PROXY_VEHICLE_TYPE", CommonUtils.checkNull(request.getParamValue("PROXY_VEHICLE_TYPE")));

            mapB.put("FITTINGS_DEC_PHONE", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_PHONE")));
            mapB.put("FITTINGS_DEC_NAME", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_NAME")));
            mapB.put("FITTINGS_DEC_EMAIL", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_EMAIL")));
            mapB.put("FITTINGS_DEC_TELPHONE", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_TELPHONE")));

//          mapC.put("ROOT_ORG_ID", orgId);
//          
//          TmOrgPO orgDealerPO = new TmOrgPO();
//          orgDealerPO.setOrgId(Long.valueOf(orgId));
//
//          if (factory.select(orgDealerPO).size() > 0) {
//              mapC.put("ROOT_ORG_NAME", factory.select(orgDealerPO).get(0).getOrgName());
//          }

            List<Object> params = new ArrayList<Object>();
            StringBuffer sbSql = new StringBuffer();

            if (dealerId.equals("")) {
                // 判断当前经销商公司是否已经被占用
                if (companyBeUsed(companyId, dealerType, dealerId))
                    throw new RuntimeException("经销商所属公司不能为空!");
                dealerId = SequenceManager.getSequence("");

                sbSql.append("insert into tm_dealer(DEALER_ID,STATUS,OEM_COMPANY_ID");
                for (String key : mapA.keySet()) {
                    if (!"".equals(mapA.get(key))) {
                        sbSql.append("," + key);
                    }
                }
                sbSql.append(") values (?,?,?");
                params.add(dealerId);
                params.add(Constant.STATUS_ENABLE);
                params.add(2010010100070674L);
                for (String key : mapA.keySet()) {
                    if (!"".equals(mapA.get(key))) {
                        sbSql.append(",");
                        getDateBuffer(key, sbSql);
                        params.add(mapA.get(key));
                    }
                }
                sbSql.append(")");
                factory.insert(sbSql.toString(), params);

                // 经销商主表保存成功后需要往明细表中插入相应的备注数据
                sbSql.delete(0, sbSql.length());
                params.clear();

                sbSql.append("insert into tm_dealer_detail(detail_id, fk_dealer_id");
                for (String key : mapB.keySet()) {
                    if (!"".equals(mapB.get(key))) {
                        sbSql.append("," + key);
                    }
                }
                sbSql.append(") values (f_getid,?");
                params.add(dealerId);
                for (String key : mapB.keySet()) {
                    if (!"".equals(mapB.get(key))) {
                        sbSql.append(",");
                        getDateBuffer(key, sbSql);
                        params.add(mapB.get(key));
                    }
                }
                sbSql.append(")");
                factory.insert(sbSql.toString(), params);

                // 明细表数据也插入成功后就需要往经销商组织关系表中插入数据
                /****** add by liuxh 20130424新增经销商时自动赋予业务范围 ********/
                TmBusinessAreaPO area = new TmBusinessAreaPO();
                List<TmBusinessAreaPO> areaList = factory.select(area);
                for (TmBusinessAreaPO areaSel : areaList) {
                    TmDealerBusinessAreaPO busArea = new TmDealerBusinessAreaPO();// 经销商业务范围
                    busArea.setRelationId(new Long(SequenceManager.getSequence("")));
                    busArea.setDealerId(Long.parseLong(dealerId));
                    busArea.setAreaId(areaSel.getAreaId());
                    busArea.setCreateBy(logonUser.getUserId());
                    busArea.setCreateDate(new Date());
                    busArea.setUpdateBy(logonUser.getUserId());
                    busArea.setUpdateDate(new Date());

                    factory.insert(busArea);
                }
                /****** add by liuxh 20130424新增经销商时自动赋予业务范围 ********/

                if (String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel)) {
                    String relationId = SequenceManager.getSequence("");
                    TmDealerOrgRelationPO tdor = new TmDealerOrgRelationPO();
                    tdor.setRelationId(new Long(relationId));
                    tdor.setBusinessType(Constant.ORG_TYPE_OEM);
                    tdor.setOrgId(new Long(dealerOrgId));
                    tdor.setDealerId(new Long(dealerId));
                    tdor.setCreateBy(logonUser.getUserId());
                    tdor.setCreateDate(new Date());

                    factory.insert(tdor);
                }

                // 售后存储过程调用
                if (dealerType.equals(String.valueOf(Constant.DEALER_TYPE_DWR))) {
                    // 调用存储过程 保存经销商的旧件回运时间
                    // Utility.setDealerType(dealerId,"P_DEALER_DATE", conn);
                    POFactory poFactory = POFactoryBuilder.getInstance();
                    List ins = new LinkedList<Object>();
                    ins.add(dealerId);
                    poFactory.callProcedure("P_DEALER_DATE", ins, null);

                    // 将该经销商的配件加价率默认为0,否则开工单时查不出配件
                    // Utility.setDealerType(dealerId,"P_TM_DOWN_PARAMETER",
                    // conn);
                    // POFactory poFactory = POFactoryBuilder.getInstance();
                    List ins2 = new LinkedList<Object>();
                    ins2.add(dealerId);
                    poFactory.callProcedure("P_DEALER_DATE", ins2, null);
                }

                // 插入默认代理区域
                TtProxyAreaPO areaPO = new TtProxyAreaPO();
                areaPO.setCreateBy(logonUser.getUserId());
                areaPO.setCreateDate(new Date());
                areaPO.setDealerId(new Long(dealerId));
                areaPO.setId(Long.parseLong(SequenceManager.getSequence("")));

                String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));
                if (provinceId.equals("500000") || provinceId.equals("120001") || provinceId.equals("310000") || provinceId.equals("110000")) {
                    areaPO.setProxyArea(CommonUtils.checkNull(request.getParamValue("COUNTIES")));
                    TmRegionPO regionPO = new TmRegionPO();
                    regionPO.setRegionCode(CommonUtils.checkNull(request.getParamValue("COUNTIES")));
                    areaPO.setProxyAreaName(factory.select(regionPO).get(0).getRegionName());
                } else {
                    areaPO.setProxyArea(CommonUtils.checkNull(request.getParamValue("CITY_ID")));
                    TmRegionPO regionPO = new TmRegionPO();
                    regionPO.setRegionCode(CommonUtils.checkNull(request.getParamValue("CITY_ID")));
                    areaPO.setProxyAreaName(factory.select(regionPO).get(0).getRegionName());
                }

                // の誌鴻 2014.10.29下午 9:59:02
                // 新增经销商的时候，插入这个start
                TmDealerBusinessAreaPO businessAreaPO = new TmDealerBusinessAreaPO();
                businessAreaPO.setAreaId(new Long(Constant.areaId));
                businessAreaPO.setDealerId(new Long(dealerId));
                businessAreaPO.setCreateBy(logonUser.getUserId());
                businessAreaPO.setCreateDate(new Date());
                businessAreaPO.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
                factory.insert(businessAreaPO);
                // 新增经销商的时候，插入这个end
            } else {
                sbSql.append("update tm_dealer set is_status=1,is_status1=3,update_date = sysdate");
                for (String key : mapA.keySet()) {
                    if (!"".equals(mapA.get(key))) {
                        sbSql.append("," + key + " = ");
                        getDateBuffer(key, sbSql);
                        params.add(mapA.get(key));
                    }
                }
                sbSql.append(" where dealer_id = ?");
                params.add(dealerId);

                // == add by chenyu 2015年9月6日 14:27:45
                // 更新经销商前,校验如果修改了经销商名,需要生成修改记录
                TmDealerPO dealerCondition = new TmDealerPO();
                dealerCondition.setDealerId(new Long(dealerId));
                DlrInfoMngDAO infoMngDAO = new DlrInfoMngDAO();
                infoMngDAO.createDealerNameChgHis(dealerCondition, String.valueOf(mapA.get("DEALER_NAME")));
                // == add by chenyu 2015年9月6日 14:27:45
                
                factory.update(sbSql.toString(), params);
                params.clear();
//              StringBuffer sbSql1 = new StringBuffer();
//              sbSql1.append("update vw_org_dealer_service set ");
//              int i = 0;
//              for (String key : mapC.keySet()) {
//                  if (!"".equals(mapC.get(key))) {
//                      if (i > 0) {
//                          sbSql1.append("," + key + " = ");
//                      } else {
//                          sbSql1.append("" + key + " = ");
//                      }
//                      getDateBuffer(key, sbSql1);
//                      params.add(mapC.get(key));
//                      i++;
//                  }
//              }
//              sbSql1.append(" where dealer_id = ?");
//              params.add(dealerId);
//
//              factory.update(sbSql1.toString(), params);

                // 检查明细表中是否有数据
                sbSql.delete(0, sbSql.length());
                params.clear();

                TmDealerDetailPO tddp = new TmDealerDetailPO();
                tddp.setFkDealerId(Long.parseLong(dealerId));

                List dList = factory.select(tddp);
                if (dList.size() > 0) {
                    sbSql.append("update tm_dealer_detail set update_date=sysdate");
                    for (String key : mapB.keySet()) {
                        if (!"".equals(mapB.get(key))) {
                            sbSql.append("," + key + " = ");
                            getDateBuffer(key, sbSql);
                            params.add(mapB.get(key));
                        }
                    }
                    sbSql.append(" where fk_dealer_id = ?");
                    params.add(dealerId);

                    factory.update(sbSql.toString(), params);
                } else {
                    sbSql.append("insert into tm_dealer_detail(detail_id, fk_dealer_id");
                    for (String key : mapB.keySet()) {
                        if (!"".equals(mapB.get(key))) {
                            sbSql.append("," + key);
                        }
                    }
                    sbSql.append(") values (f_getid,?");
                    params.add(dealerId);
                    for (String key : mapB.keySet()) {
                        if (!"".equals(mapB.get(key))) {
                            sbSql.append(",");
                            getDateBuffer(key, sbSql);
                            params.add(mapB.get(key));
                        }
                    }
                    sbSql.append(")");
                    factory.insert(sbSql.toString(), params);
                }
            }

//          if (String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel)) {
                TmDealerOrgRelationPO tdor = new TmDealerOrgRelationPO();
                tdor.setDealerId(new Long(dealerId));
                TmDealerOrgRelationPO tdor1 = new TmDealerOrgRelationPO();
                tdor1.setOrgId(new Long(dealerOrgId));

                factory.update(tdor,tdor1);
//          }
            
            // 维护代理区域
            String PROXY_AREA = CommonUtils.checkNull(request.getParamValue("PROXY_AREA"));
            String PROXY_AREA_DEF = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_DEF"));
            String PROXY_AREA_NAME_DEF = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_NAME_DEF"));
            String PROXY_AREA_NAME = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_NAME"));

            if (!PROXY_AREA_NAME.contains(PROXY_AREA_NAME_DEF)) {

                if (PROXY_AREA_NAME.equals("")) {
                    PROXY_AREA_NAME = PROXY_AREA_NAME_DEF;
                    PROXY_AREA = PROXY_AREA_DEF;
                } else {
                    PROXY_AREA_NAME = PROXY_AREA_NAME + "," + PROXY_AREA_NAME_DEF;
                    PROXY_AREA = PROXY_AREA + "," + PROXY_AREA_DEF;
                }
            }
            TtProxyAreaPO PO = new TtProxyAreaPO();
            PO.setDealerId(Long.valueOf(dealerId));
            factory.delete(PO);
            if (!PROXY_AREA.equals("")) {

                String[] proxyArea = PROXY_AREA.split(",");
                String[] proxyAreaName = PROXY_AREA_NAME.split(",");

                for (int j = 0; j < proxyArea.length; j++) {

                    TtProxyAreaPO areaPO = new TtProxyAreaPO();
                    areaPO.setDealerId(Long.valueOf(dealerId));
                    areaPO.setCreateBy(logonUser.getUserId());
                    areaPO.setId(Long.parseLong(SequenceManager.getSequence("")));
                    areaPO.setCreateDate(new Date());
                    areaPO.setProxyArea(proxyArea[j]);
                    areaPO.setProxyAreaName(proxyAreaName[j]);
                    factory.insert(areaPO);

                }
            }
            // TODO 如果是售后经销商
            // zhumingwei add 2013-10-18
            // 这里判断如果是售后经销商的话那么再查询TtPartBillDefinePO表里面有记录没有，如果有就修改，反之就insert
            // if(!"".equals(dealerType) && "10771002".equals(dealerType)){
            // //发票信息
            // TtPartBillDefinePO tarBillPo = new TtPartBillDefinePO();
            // tarBillPo.setBillId(Long.parseLong(SequenceManager.getSequence("")));
            // tarBillPo.setDealerId(Long.parseLong(dealerId));
            // tarBillPo.setDealerCode(dealerCode);
            // tarBillPo.setDealerName(dealerName);
            // tarBillPo.setAccount(CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT")).trim());
            // tarBillPo.setTaxName(CommonUtils.checkNull(request.getParamValue("erpCode")).trim());
            // tarBillPo.setInvType(Constant.DLR_INVOICE_TYPE_01);
            // tarBillPo.setTaxNo(CommonUtils.checkNull(request.getParamValue("taxesNo")).trim());
            // tarBillPo.setBank(CommonUtils.checkNull(request.getParamValue("BANK")).trim());
            // tarBillPo.setTel(CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE")).trim());
            // tarBillPo.setAddr(CommonUtils.checkNull(request.getParamValue("INVOICE_ADD")).trim());
            // tarBillPo.setMailAddr(CommonUtils.checkNull(request.getParamValue("INVOICE_POST_ADD")).trim());
            // tarBillPo.setCreateBy(logonUser.getUserId());
            // tarBillPo.setCreateDate(new Date());
            // tarBillPo.setState(Constant.STATUS_ENABLE);
            // tarBillPo.setStatus(1);
            // dao.insert(tarBillPo);
            // }
            // zhumingwei add 2013-10-18
            // 这里判断如果是售后经销商的话那么再查询TtPartBillDefinePO表里面有记录没有，如果有就修改，反之就insert
            // end
            if (dealerLevel.equals(Constant.DEALER_LEVEL_01.toString())) {
                factory.update("update tm_dealer set parent_dealer_d = null where dealer_id = " + dealerId, null);
            }
            List<Object> inParameter = new ArrayList<Object>();
            List outParameter = new ArrayList();
            factory.callProcedure("p_vw_org_dealer_service", inParameter, outParameter);
            act.setOutData("message", "操作成功!");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void saveDealerInfo2nd() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        HashMap<String, Object> mapA = new HashMap<String, Object>();
        HashMap<String, Object> mapB = new HashMap<String, Object>();
        HashMap<String, Object> mapC = new HashMap<String, Object>();
        
        try {
            RequestWrapper request = act.getRequest();

            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID")).trim();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE")).trim(); // 经销商代码
            String dealerType = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));
            String dealerOrgId = CommonUtils.checkNull(request.getParamValue("DEALER_ORG_ID"));
            String dealerLevel = CommonUtils.checkNull(request.getParamValue("DEALER_LEVEL"));
            String parentDealerD = CommonUtils.checkNull(request.getParamValue("PARENT_DEALER_D"));
            String companyId = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
            ProductManageDao dao = ProductManageDao.getInstance();

            if (dealerLevel.equals(Constant.DEALER_LEVEL_02.toString())) {
                if (parentDealerD.equals("")) {
                    throw new RuntimeException("无上级经销商!");
                } else {
                    // TODO 判断上级经销商是否存在并且有效
                    mapA.put("PARENT_DEALER_D", parentDealerD);
                }
            }

            // 经销商主数据
            mapA.put("COMPANY_ID", companyId);
            mapA.put("DEALER_CODE", dealerCode);
            mapA.put("DEALER_ORG_ID", dealerOrgId);
            mapA.put("DEALER_LEVEL", dealerLevel);

            mapA.put("DEALER_TYPE", dealerType);

            mapA.put("DEALER_SHORTNAME", CommonUtils.checkNull(request.getParamValue("DEALER_SHORTNAME")));
            mapA.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            mapA.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));
            mapA.put("PROVINCE_ID", CommonUtils.checkNull(request.getParamValue("PROVINCE_ID")));
            mapA.put("CITY_ID", CommonUtils.checkNull(request.getParamValue("CITY_ID")));
            mapA.put("COUNTIES", CommonUtils.checkNull(request.getParamValue("COUNTIES")));
            mapA.put("ZIP_CODE", CommonUtils.checkNull(request.getParamValue("ZIP_CODE")));
            mapA.put("ADDRESS", CommonUtils.checkNull(request.getParamValue("ADDRESS")));
            mapA.put("ZCCODE", CommonUtils.checkNull(request.getParamValue("ZCCODE")));
            mapA.put("ZY_SCOPE", CommonUtils.checkNull(request.getParamValue("ZY_SCOPE")));
            mapA.put("JY_SCOPE", CommonUtils.checkNull(request.getParamValue("JY_SCOPE")));
            mapA.put("SITEDATE", CommonUtils.checkNull(request.getParamValue("SITEDATE")));
            mapA.put("DESTROYDATE", CommonUtils.checkNull(request.getParamValue("DESTROYDATE")));
            mapA.put("LEGAL", CommonUtils.checkNull(request.getParamValue("LEGAL")));
            mapA.put("LEGAL_PHONE", CommonUtils.checkNull(request.getParamValue("LEGAL_PHONE")));
            mapA.put("LEGAL_TELPHONE", CommonUtils.checkNull(request.getParamValue("LEGAL_TELPHONE")));
            mapA.put("LEGAL_EMAIL", CommonUtils.checkNull(request.getParamValue("LEGAL_EMAIL")));
            mapA.put("INVOICE_PERSION", CommonUtils.checkNull(request.getParamValue("INVOICE_PERSION")));
            mapA.put("INVOICE_TELPHONE", CommonUtils.checkNull(request.getParamValue("INVOICE_TELPHONE")));
            mapA.put("BEGIN_BANK", CommonUtils.checkNull(request.getParamValue("BEGIN_BANK")));
            mapA.put("ERP_CODE", CommonUtils.checkNull(request.getParamValue("ERP_CODE")));
            mapA.put("TAXES_NO", CommonUtils.checkNull(request.getParamValue("TAXES_NO")));
            mapA.put("INVOICE_ACCOUNT", CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT")));
            mapA.put("INVOICE_PHONE", CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE")));
            mapA.put("INVOICE_ADD", CommonUtils.checkNull(request.getParamValue("INVOICE_ADD")));
            mapA.put("TAXPAYER_NO", CommonUtils.checkNull(request.getParamValue("TAXPAYER_NO")));
            mapA.put("TAXPAYER_NATURE", CommonUtils.checkNull(request.getParamValue("TAXPAYER_NATURE")));
            mapA.put("TAX_INVOICE", CommonUtils.checkNull(request.getParamValue("TAX_INVOICE")));
            mapA.put("TAX_DISRATE", CommonUtils.checkNull(request.getParamValue("TAX_DISRATE")));
            mapA.put("REMARK", CommonUtils.checkNull(request.getParamValue("REMARK")));
            mapA.put("MAIN_RESOURCES", CommonUtils.checkNull(request.getParamValue("MAIN_RESOURCES")));

            // 经销商备注数据
            mapB.put("COMPANY_ZC_CODE", CommonUtils.checkNull(request.getParamValue("COMPANY_ZC_CODE")));
            mapB.put("WEBMASTER_NAME", CommonUtils.checkNull(request.getParamValue("WEBMASTER_NAME")));
            mapB.put("WEBMASTER_PHONE", CommonUtils.checkNull(request.getParamValue("WEBMASTER_PHONE")));
            mapB.put("WEBMASTER_TELPHONE", CommonUtils.checkNull(request.getParamValue("WEBMASTER_TELPHONE")));
            mapB.put("WEBMASTER_EMAIL", CommonUtils.checkNull(request.getParamValue("WEBMASTER_EMAIL")));
            mapB.put("MARKET_NAME", CommonUtils.checkNull(request.getParamValue("MARKET_NAME")));
            mapB.put("MARKET_PHONE", CommonUtils.checkNull(request.getParamValue("MARKET_PHONE")));
            mapB.put("MARKET_TELPHONE", CommonUtils.checkNull(request.getParamValue("MARKET_TELPHONE")));
            mapB.put("MARKET_EMAIL", CommonUtils.checkNull(request.getParamValue("MARKET_EMAIL")));
            mapB.put("MANAGER_NAME", CommonUtils.checkNull(request.getParamValue("MANAGER_NAME")));
            mapB.put("MANAGER_PHONE", CommonUtils.checkNull(request.getParamValue("MANAGER_PHONE")));
            mapB.put("MANAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("MANAGER_TELPHONE")));
            mapB.put("MANAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("MANAGER_EMAIL")));
            mapB.put("SER_MANAGER_NAME", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_NAME")));
            mapB.put("SER_MANAGER_PHONE", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_PHONE")));
            mapB.put("SER_MANAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_TELPHONE")));
            mapB.put("SER_MANAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_EMAIL")));
            mapB.put("CLAIM_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_NAME")));
            mapB.put("CLAIM_DIRECTOR_PHONE", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_PHONE")));
            mapB.put("CLAIM_DIRECTOR_TELPHONE", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_TELPHONE")));
            mapB.put("CLAIM_DIRECTOR_EMAIL", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_EMAIL")));
            mapB.put("CLAIM_DIRECTOR_FAX", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_FAX")));
            mapB.put("CUSTSER_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_NAME")));
            mapB.put("CUSTSER_DIRECTOR_PHONE", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_PHONE")));
            mapB.put("CUSTSER_DIRECTOR_TELPHONE", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_TELPHONE")));
            mapB.put("CUSTSER_DIRECTOR_EMAIL", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_EMAIL")));
            mapB.put("SER_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_NAME")));
            mapB.put("SER_DIRECTOR_PHONE", CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_PHONE")));
            mapB.put("SER_DIRECTOR_TELHONE", CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_TELHONE")));
            mapB.put("TECHNOLOGY_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_NAME")));
            mapB.put("TECHNOLOGY_DIRECTOR_TELPHONE", CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_TELPHONE")));
            mapB.put("WORKSHOP_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("WORKSHOP_DIRECTOR_NAME")));
            mapB.put("WORKSHOP_DIRECTOR_TELpHONE", CommonUtils.checkNull(request.getParamValue("WORKSHOP_DIRECTOR_TELpHONE")));
            mapB.put("FINANCE_MANAGER_NAME", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_NAME")));
            mapB.put("FINANCE_MANAGER_PHONE", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_PHONE")));
            mapB.put("FINANCE_MANAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_TELPHONE")));
            mapB.put("FINANCE_MANAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_EMAIL")));
            mapB.put("MESSAGER_NAME", CommonUtils.checkNull(request.getParamValue("MESSAGER_NAME")));
            mapB.put("MESSAGER_PHONE", CommonUtils.checkNull(request.getParamValue("MESSAGER_PHONE")));
            mapB.put("MESSAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("MESSAGER_TELPHONE")));
            mapB.put("MESSAGER_QQ", CommonUtils.checkNull(request.getParamValue("MESSAGER_QQ")));
            mapB.put("MESSAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("MESSAGER_EMAIL")));
            mapB.put("FIRST_SUB_DATE", CommonUtils.checkNull(request.getParamValue("FIRST_SUB_DATE")));
            mapB.put("FIRST_GETCAR_DATE", CommonUtils.checkNull(request.getParamValue("FIRST_GETCAR_DATE")));
            mapB.put("FIRST_SAELS_DATE", CommonUtils.checkNull(request.getParamValue("FIRST_SAELS_DATE")));
            mapB.put("UNION_TYPE", CommonUtils.checkNull(request.getParamValue("UNION_TYPE")));
            mapB.put("FIXED_CAPITAL", CommonUtils.checkNull(request.getParamValue("FIXED_CAPITAL")));
            mapB.put("REGISTERED_CAPITAL", CommonUtils.checkNull(request.getParamValue("REGISTERED_CAPITAL")));
            mapB.put("PEOPLE_NUMBER", CommonUtils.checkNull(request.getParamValue("PEOPLE_NUMBER")));
            mapB.put("OFFICE_AREA", CommonUtils.checkNull(request.getParamValue("OFFICE_AREA")));
            mapB.put("PARTS_AREA", CommonUtils.checkNull(request.getParamValue("PARTS_AREA")));
            mapB.put("DEPOT_AREA", CommonUtils.checkNull(request.getParamValue("DEPOT_AREA")));
            mapB.put("MAIN_AREA", CommonUtils.checkNull(request.getParamValue("MAIN_AREA")));
            mapB.put("ONLY_MONTH_COUNT", CommonUtils.checkNull(request.getParamValue("ONLY_MONTH_COUNT")));
            mapB.put("OPENING_TIME", CommonUtils.checkNull(request.getParamValue("OPENING_TIME")));
            mapB.put("WORK_TYPE", CommonUtils.checkNull(request.getParamValue("WORK_TYPE")));
            mapB.put("IS_FOUR_S", CommonUtils.checkNull(request.getParamValue("IS_FOUR_S")));
            mapB.put("IS_LOW_SER", CommonUtils.checkNull(request.getParamValue("IS_LOW_SER")));
            mapB.put("COMPANY_ADDRESS", CommonUtils.checkNull(request.getParamValue("COMPANY_ADDRESS")));
            mapB.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));
            mapB.put("AUTHORIZATION_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_DATE")));
            mapB.put("IS_ACTING_BRAND", CommonUtils.checkNull(request.getParamValue("IS_ACTING_BRAND")));
            mapB.put("ACTING_BRAND_NAME", CommonUtils.checkNull(request.getParamValue("ACTING_BRAND_NAME")));
            mapB.put("PARTS_STORE_AMOUNT", CommonUtils.checkNull(request.getParamValue("PARTS_STORE_AMOUNT")));
            mapB.put("SHOP_TYPE", CommonUtils.checkNull(request.getParamValue("SHOP_TYPE")));
            mapB.put("HOTLINE", CommonUtils.checkNull(request.getParamValue("HOTLINE")));
            mapB.put("FAX_NO", CommonUtils.checkNull(request.getParamValue("FAX_NO")));
            mapB.put("EMAIL", CommonUtils.checkNull(request.getParamValue("EMAIL")));
            mapB.put("IS_AUTHORIZE_CITY", CommonUtils.checkNull(request.getParamValue("IS_AUTHORIZE_CITY")));
            mapB.put("IS_AUTHORIZE_COUNTY", CommonUtils.checkNull(request.getParamValue("IS_AUTHORIZE_COUNTY")));
            mapB.put("AUTHORIZE_BRAND", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_BRAND")));
            mapB.put("AUTHORIZE_GET_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_GET_DATE")));
            mapB.put("AUTHORIZE_SUB_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_SUB_DATE")));
            mapB.put("AUTHORIZE_EFFECT_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_EFFECT_DATE")));
            mapB.put("ANNOUNCEMENT_NO ", CommonUtils.checkNull(request.getParamValue("ANNOUNCEMENT_NO ")));
            mapB.put("ANNOUNCEMENT_DATE", CommonUtils.checkNull(request.getParamValue("ANNOUNCEMENT_DATE")));
            mapB.put("ANNOUNCEMENT_END_DATE", CommonUtils.checkNull(request.getParamValue("ANNOUNCEMENT_END_DATE")));
            mapB.put("THE_AGENTS", CommonUtils.checkNull(request.getParamValue("THE_AGENTS")));

            mapB.put("PROXY_VEHICLE_TYPE", CommonUtils.checkNull(request.getParamValue("PROXY_VEHICLE_TYPE")));

            mapB.put("FITTINGS_DEC_PHONE", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_PHONE")));
            mapB.put("FITTINGS_DEC_NAME", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_NAME")));
            mapB.put("FITTINGS_DEC_EMAIL", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_EMAIL")));
            mapB.put("FITTINGS_DEC_TELPHONE", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_TELPHONE")));

            mapB.put("HAVE_SERVICE", CommonUtils.checkNull(request.getParamValue("HAVE_SERVICE")));
            mapB.put("SERVICE_AREA", CommonUtils.checkNull(request.getParamValue("SERVICE_AREA")));
            mapB.put("SERVICE_ADDRESS", CommonUtils.checkNull(request.getParamValue("SERVICE_ADDRESS")));
            mapB.put("SERVICE_HOTLINE", CommonUtils.checkNull(request.getParamValue("SERVICE_HOTLINE")));
            mapB.put("MIN_STOCK", CommonUtils.checkNull(request.getParamValue("MIN_STOCK")));
            mapB.put("OME_AREA", CommonUtils.checkNull(request.getParamValue("OEM_AREA")));
            mapB.put("OME_PEOPLE_TOTAL", CommonUtils.checkNull(request.getParamValue("OEM_PEOPLE_TOTAL")));
            mapB.put("YEAR_PLAN", CommonUtils.checkNull(request.getParamValue("YEAR_PLAN")));

            mapC.put("ROOT_ORG_ID", orgId);
            
            // 其它数据
            TmDealerSecondLevelPO tmDealerSecondLevelPO = new TmDealerSecondLevelPO();
            tmDealerSecondLevelPO.setSecondLevelId(Long.parseLong(SequenceManager.getSequence("")));
            String SECOND_LEVEL_NETWORK_NATURE = CommonUtils.checkNull(request.getParamValue("SECOND_LEVEL_NETWORK_NATURE"));
            if (!StringUtils.isEmpty(SECOND_LEVEL_NETWORK_NATURE)) {
                tmDealerSecondLevelPO.setSecondLevelNetworkNature(Long.parseLong(SECOND_LEVEL_NETWORK_NATURE));
            }
            tmDealerSecondLevelPO.setCompetingBrand(CommonUtils.checkNull(request.getParamValue("COMPETING_BRAND")));
            String AND_COMPETING_RUN_DISTANCE = CommonUtils.checkNull(request.getParamValue("AND_COMPETING_RUN_DISTANCE"));
            if (!StringUtils.isEmpty(AND_COMPETING_RUN_DISTANCE)) {
                tmDealerSecondLevelPO.setAndCompetingRunDistance(Double.parseDouble(AND_COMPETING_RUN_DISTANCE));
            }
            String MONTH_AVERAGE_SALES = CommonUtils.checkNull(request.getParamValue("MONTH_AVERAGE_SALES"));
            if (!StringUtils.isEmpty(MONTH_AVERAGE_SALES)) {
                tmDealerSecondLevelPO.setMonthAverageSales(Long.parseLong(MONTH_AVERAGE_SALES));
            }
//          tmDealerSecondLevelPO.setMarketOccupancy(Double.parseDouble(CommonUtils.checkNull(request.getParamValue("MARKET_OCCUPANCY"))));
            String DOORHEAD_LENGTH = CommonUtils.checkNull(request.getParamValue("DOORHEAD_LENGTH"));
            if (!StringUtils.isEmpty(DOORHEAD_LENGTH)) {
                tmDealerSecondLevelPO.setDoorheadLength(Long.parseLong(DOORHEAD_LENGTH));
            }
            String IS_HAVE_SALES_DOORHEAD = CommonUtils.checkNull(request.getParamValue("IS_HAVE_SALES_DOORHEAD"));
            if (!StringUtils.isEmpty(IS_HAVE_SALES_DOORHEAD)) {
                tmDealerSecondLevelPO.setIsHaveSalesDoorhead(Long.parseLong(IS_HAVE_SALES_DOORHEAD));
            }
            String IS_HAVE_SALES_IMAGE_WALL = CommonUtils.checkNull(request.getParamValue("IS_HAVE_SALES_IMAGE_WALL"));
            if (!StringUtils.isEmpty(IS_HAVE_SALES_IMAGE_WALL)) {
                tmDealerSecondLevelPO.setIsHaveSalesImageWall(Long.parseLong(IS_HAVE_SALES_IMAGE_WALL));
            }
            String AGENT_ZONE_POPULATION_COUNT = CommonUtils.checkNull(request.getParamValue("AGENT_ZONE_POPULATION_COUNT"));
            if (!StringUtils.isEmpty(AGENT_ZONE_POPULATION_COUNT)) {
                tmDealerSecondLevelPO.setAgentZonePopulationCount(Long.parseLong(AGENT_ZONE_POPULATION_COUNT));
            }
            String SALES_CONSULTANT_COUNT = CommonUtils.checkNull(request.getParamValue("SALES_CONSULTANT_COUNT"));
            if (!StringUtils.isEmpty(SALES_CONSULTANT_COUNT)) {
                tmDealerSecondLevelPO.setSalesConsultantCount(Long.parseLong(SALES_CONSULTANT_COUNT));
            }
            String SERVICE_NETWORK_NATURE = CommonUtils.checkNull(request.getParamValue("SERVICE_NETWORK_NATURE"));
            if (!StringUtils.isEmpty(SERVICE_NETWORK_NATURE)) {
                tmDealerSecondLevelPO.setServiceNetworkNature(Long.parseLong(SERVICE_NETWORK_NATURE));
            }
            String REPAIR_APTITUDE = CommonUtils.checkNull(request.getParamValue("REPAIR_APTITUDE"));
            if (!StringUtils.isEmpty(REPAIR_APTITUDE)) {
                tmDealerSecondLevelPO.setRepairAptitude(Long.parseLong(REPAIR_APTITUDE));
            }
            String SERVICE_WORKSHOP_AREA = CommonUtils.checkNull(request.getParamValue("SERVICE_WORKSHOP_AREA"));
            if (!StringUtils.isEmpty(SERVICE_WORKSHOP_AREA)) {
                tmDealerSecondLevelPO.setServiceWorkshopArea(Double.parseDouble(SERVICE_WORKSHOP_AREA));
            }
            String IS_HAVE_SERVICE_DOORHEAD = CommonUtils.checkNull(request.getParamValue("IS_HAVE_SERVICE_DOORHEAD"));
            if (!StringUtils.isEmpty(IS_HAVE_SERVICE_DOORHEAD)) {
                tmDealerSecondLevelPO.setIsHaveServiceDoorhead(Long.parseLong(IS_HAVE_SERVICE_DOORHEAD));
            }
            String IS_HAVE_SERVICE_IMAGE_WALL = CommonUtils.checkNull(request.getParamValue("IS_HAVE_SERVICE_IMAGE_WALL"));
            if (!StringUtils.isEmpty(IS_HAVE_SERVICE_IMAGE_WALL)) {
                tmDealerSecondLevelPO.setIsHaveServiceImageWall(Long.parseLong(IS_HAVE_SERVICE_IMAGE_WALL));
            }
            String SERVICE_SALES_NETWORK_DISTANCE = CommonUtils.checkNull(request.getParamValue("SERVICE_SALES_NETWORK_DISTANCE"));
            if (!StringUtils.isEmpty(SERVICE_SALES_NETWORK_DISTANCE)) {
                tmDealerSecondLevelPO.setServiceSalesNetworkDistance(Double.parseDouble(SERVICE_SALES_NETWORK_DISTANCE));
            }
            String REPAIR_ENGINEER_LOWEST_DEPLOY = CommonUtils.checkNull(request.getParamValue("REPAIR_ENGINEER_LOWEST_DEPLOY"));
            if (!StringUtils.isEmpty(REPAIR_ENGINEER_LOWEST_DEPLOY)) {
                tmDealerSecondLevelPO.setRepairEngineerLowestDeploy(Long.parseLong(REPAIR_ENGINEER_LOWEST_DEPLOY));
            }
            tmDealerSecondLevelPO.setLevelReport(StringUtils.isEmpty(request.getParamValue("LEVEL_REPORT"))?0:Long.parseLong(request.getParamValue("LEVEL_REPORT")));           
            
            VwOrgDealerPO orgDealerPO = new VwOrgDealerPO();
            orgDealerPO.setRootOrgId(Long.valueOf(orgId));

            mapC.put("ROOT_ORG_NAME", factory.select(orgDealerPO).get(0).getRootOrgName());
            List<Object> params = new ArrayList<Object>();
            StringBuffer sbSql = new StringBuffer();

            if (dealerId.equals("")) {

                if (dao.isExistBydealerCode(dealerCode)) {
                    DealerInfoDao dealerInfoDao = DealerInfoDao.getInstance();
                    String codeNum = dealerInfoDao.getSecendDealerNum(dealerCode.substring(0, 6));
                    dealerCode = dealerCode.substring(0, 6) + codeNum + "-S";
                    act.setOutData("isExistBydealerCode", "自动生成的经销商代码已被其他用户使用，系统已为你重新生成了一个新的代码:" + dealerCode);
                }

                dealerId = SequenceManager.getSequence("");

                sbSql.append("insert into tm_dealer(DEALER_ID,STATUS,OEM_COMPANY_ID,create_by,create_date");
                for (String key : mapA.keySet()) {
                    if (!"".equals(mapA.get(key))) {
                        sbSql.append("," + key);
                    }
                }
                sbSql.append(") values (?,?,?,?,?");
                params.add(dealerId);
                params.add(Constant.STATUS_ENABLE);
                params.add(2010010100070674L);
                params.add(logonUser.getUserId());
                params.add(new Date());
                for (String key : mapA.keySet()) {
                    if (!"".equals(mapA.get(key))) {
                        sbSql.append(",");
                        getDateBuffer(key, sbSql);
                        params.add(mapA.get(key));
                    }
                }
                sbSql.append(")");
                factory.insert(sbSql.toString(), params);

                // 经销商主表保存成功后需要往明细表中插入相应的备注数据
                sbSql.delete(0, sbSql.length());
                params.clear();

                sbSql.append("insert into tm_dealer_detail(detail_id, fk_dealer_id");
                for (String key : mapB.keySet()) {
                    if (!"".equals(mapB.get(key))) {
                        sbSql.append("," + key);
                    }
                }
                sbSql.append(") values (f_getid,?");
                params.add(dealerId);
                for (String key : mapB.keySet()) {
                    if (!"".equals(mapB.get(key))) {
                        sbSql.append(",");
                        getDateBuffer(key, sbSql);
                        params.add(mapB.get(key));
                    }
                }
                sbSql.append(")");
                factory.insert(sbSql.toString(), params);

                String relationId = SequenceManager.getSequence("");
                TmDealerOrgRelationPO tdor = new TmDealerOrgRelationPO();
                tdor.setRelationId(new Long(relationId));
                tdor.setBusinessType(Constant.ORG_TYPE_OEM);
                tdor.setOrgId(new Long(orgId));
                tdor.setDealerId(new Long(dealerId));
                tdor.setCreateBy(logonUser.getUserId());
                tdor.setCreateDate(new Date());

                factory.insert(tdor);

                // 插入默认代理区域
                // 维护代理区域
                String PROXY_AREA = CommonUtils.checkNull(request.getParamValue("PROXY_AREA"));
                String PROXY_AREA_DEF = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_DEF"));
                String PROXY_AREA_NAME_DEF = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_NAME_DEF"));
                String PROXY_AREA_NAME = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_NAME"));

                if (!PROXY_AREA_NAME.contains(PROXY_AREA_NAME_DEF)) {

                    if (PROXY_AREA_NAME.equals("")) {
                        PROXY_AREA_NAME = PROXY_AREA_NAME_DEF;
                        PROXY_AREA = PROXY_AREA_DEF;
                    } else {
                        PROXY_AREA_NAME = PROXY_AREA_NAME + "," + PROXY_AREA_NAME_DEF;
                        PROXY_AREA = PROXY_AREA + "," + PROXY_AREA_DEF;
                    }
                }
                TtProxyAreaPO PO = new TtProxyAreaPO();
                PO.setDealerId(Long.valueOf(dealerId));
                factory.delete(PO);
                if (!PROXY_AREA.equals("")) {

                    String[] proxyArea = PROXY_AREA.split(",");
                    String[] proxyAreaName = PROXY_AREA_NAME.split(",");

                    for (int j = 0; j < proxyArea.length; j++) {

                        TtProxyAreaPO areaPO = new TtProxyAreaPO();
                        areaPO.setDealerId(Long.valueOf(dealerId));
                        areaPO.setCreateBy(logonUser.getUserId());
                        areaPO.setId(Long.parseLong(SequenceManager.getSequence("")));
                        areaPO.setCreateDate(new Date());
                        areaPO.setProxyArea(proxyArea[j]);
                        areaPO.setProxyAreaName(proxyAreaName[j]);
                        factory.insert(areaPO);

                    }
                }
                // 更新附件 start
                FsFileuploadPO fileuploadPO = new FsFileuploadPO();
                FsFileuploadPO fileuploadPO1 = new FsFileuploadPO();
                String[] fjids = request.getParamValues("fjid");
                if (fjids != null && fjids.length != 0) {
                    for (int i = 0; i < fjids.length; i++) {
                        fileuploadPO.setFjid(Long.valueOf(fjids[i]));

                        fileuploadPO1.setYwzj(Long.parseLong(dealerId));
                        fileuploadPO1.setUpdateDate(new Date());
                        fileuploadPO1.setUpdateBy(logonUser.getUserId());
                        fileuploadPO1.setStatus(Constant.STATUS_ENABLE);

                        ajaxDao.update(fileuploadPO, fileuploadPO1);
                    }
                }
                // 更新附件 end

                TtDealerSecendAuditPO auditPO = new TtDealerSecendAuditPO();
                auditPO.setCreateBy(logonUser.getUserId());
                auditPO.setCreateDate(new Date());
                auditPO.setDealerId(Long.parseLong(dealerId));
                auditPO.setId(Long.parseLong(SequenceManager.getSequence("")));
                String cmd = CommonUtils.checkNull(request.getParamValue("cmd"));
                if (cmd.equals("1")) {
                    auditPO.setStatus(Constant.DEALER_SECEND_STATUS_01);
                    auditPO.setUserId(Constant.DEALER_SECEND_AUDIT_01);

                    DealerInfoDao.getInstance().insert(auditPO);

                    TmDealerPO dealerPO = new TmDealerPO();
                    TmDealerPO dealerPO1 = new TmDealerPO();
                    dealerPO.setDealerId(auditPO.getDealerId());
                    dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_01);
                    factory.update(dealerPO, dealerPO1);
                } else {
                    auditPO.setStatus(Constant.DEALER_SECEND_STATUS_02);
                    auditPO.setUserId(Constant.DEALER_SECEND_AUDIT_02);

                    DealerInfoDao.getInstance().insert(auditPO);

                    TmDealerPO dealerPO = new TmDealerPO();
                    TmDealerPO dealerPO1 = new TmDealerPO();
                    dealerPO.setDealerId(auditPO.getDealerId());
                    dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_02);
                    factory.update(dealerPO, dealerPO1);
                }
                
                // 其它信息保存
                tmDealerSecondLevelPO.setFkDealerId(Long.parseLong(dealerId));
                factory.insert(tmDealerSecondLevelPO);
                
            } else {
                sbSql.append("update tm_dealer set update_date = sysdate");
                for (String key : mapA.keySet()) {
                    if (!"".equals(mapA.get(key))) {
                        sbSql.append("," + key + " = ");
                        getDateBuffer(key, sbSql);
                        params.add(mapA.get(key));
                    }
                }
                sbSql.append(" where dealer_id = ?");
                params.add(dealerId);

                factory.update(sbSql.toString(), params);
                params.clear();
                StringBuffer sbSql1 = new StringBuffer();
                sbSql1.append("update vw_org_dealer_service set ");
                int i = 0;
                for (String key : mapC.keySet()) {
                    if (!"".equals(mapC.get(key))) {
                        if (i > 0) {
                            sbSql1.append("," + key + " = ");
                        } else {
                            sbSql1.append("" + key + " = ");
                        }
                        getDateBuffer(key, sbSql1);
                        params.add(mapC.get(key));
                        i++;
                    }
                }
                sbSql1.append(" where dealer_id = ?");
                params.add(dealerId);

                factory.update(sbSql1.toString(), params);

                // 检查明细表中是否有数据
                sbSql.delete(0, sbSql.length());
                params.clear();

                TmDealerDetailPO tddp = new TmDealerDetailPO();
                tddp.setFkDealerId(Long.parseLong(dealerId));

                List dList = factory.select(tddp);
                if (dList.size() > 0) {
                    sbSql.append("update tm_dealer_detail set update_date=sysdate");
                    for (String key : mapB.keySet()) {
                        if (!"".equals(mapB.get(key))) {
                            sbSql.append("," + key + " = ");
                            getDateBuffer(key, sbSql);
                            params.add(mapB.get(key));
                        }
                    }
                    sbSql.append(" where fk_dealer_id = ?");
                    params.add(dealerId);

                    factory.update(sbSql.toString(), params);
                } else {
                    sbSql.append("insert into tm_dealer_detail(detail_id, fk_dealer_id");
                    for (String key : mapB.keySet()) {
                        if (!"".equals(mapB.get(key))) {
                            sbSql.append("," + key);
                        }
                    }
                    sbSql.append(") values (f_getid,?");
                    params.add(dealerId);
                    for (String key : mapB.keySet()) {
                        if (!"".equals(mapB.get(key))) {
                            sbSql.append(",");
                            getDateBuffer(key, sbSql);
                            params.add(mapB.get(key));
                        }
                    }
                    sbSql.append(")");
                    factory.insert(sbSql.toString(), params);
                }

                // 更新附件 start
                FsFileuploadPO fileuploadPO = new FsFileuploadPO();
                FsFileuploadPO fileuploadPO1 = new FsFileuploadPO();
                String[] fjids = request.getParamValues("fjid");
                String deleteFjids = request.getParamValue("list");

                ajaxDao.delete("DELETE Fs_Fileupload WHERE FJID IN(" + deleteFjids + ")", null);
                if (fjids != null && fjids.length > 0) {
                    // String[] fjid = fjids.split("_");
                    for (int k = 0; k < fjids.length; k++) {
                        fileuploadPO.setFjid(Long.valueOf(fjids[k]));

                        fileuploadPO1.setYwzj(Long.parseLong(dealerId));
                        fileuploadPO1.setUpdateDate(new Date());
                        fileuploadPO1.setUpdateBy(logonUser.getUserId());
                        fileuploadPO1.setStatus(Constant.STATUS_ENABLE);

                        ajaxDao.update(fileuploadPO, fileuploadPO1);
                    }
                }
                // act.setOutData("FJID", fjids);
                // act.setOutData("ID", dealerId);
                // 更新附件 end

                // 维护代理区域
                String PROXY_AREA = CommonUtils.checkNull(request.getParamValue("PROXY_AREA"));
                String PROXY_AREA_DEF = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_DEF"));
                String PROXY_AREA_NAME_DEF = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_NAME_DEF"));
                String PROXY_AREA_NAME = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_NAME"));

                if (!PROXY_AREA_NAME.contains(PROXY_AREA_NAME_DEF)) {

                    if (PROXY_AREA_NAME.equals("")) {
                        PROXY_AREA_NAME = PROXY_AREA_NAME_DEF;
                        PROXY_AREA = PROXY_AREA_DEF;
                    } else {
                        PROXY_AREA_NAME = PROXY_AREA_NAME + "," + PROXY_AREA_NAME_DEF;
                        PROXY_AREA = PROXY_AREA + "," + PROXY_AREA_DEF;
                    }
                }
                TtProxyAreaPO PO = new TtProxyAreaPO();
                PO.setDealerId(Long.valueOf(dealerId));
                factory.delete(PO);
                if (!PROXY_AREA.equals("")) {

                    String[] proxyArea = PROXY_AREA.split(",");
                    String[] proxyAreaName = PROXY_AREA_NAME.split(",");

                    for (int j = 0; j < proxyArea.length; j++) {

                        TtProxyAreaPO areaPO = new TtProxyAreaPO();
                        areaPO.setDealerId(Long.valueOf(dealerId));
                        areaPO.setCreateBy(logonUser.getUserId());
                        areaPO.setId(Long.parseLong(SequenceManager.getSequence("")));
                        areaPO.setCreateDate(new Date());
                        areaPO.setProxyArea(proxyArea[j]);
                        areaPO.setProxyAreaName(proxyAreaName[j]);
                        factory.insert(areaPO);

                    }
                }
                TtDealerSecendAuditPO auditPO = new TtDealerSecendAuditPO();
                auditPO.setDealerId(new Long(dealerId));
                auditPO.setCreateBy(logonUser.getUserId());
                auditPO.setCreateDate(new Date());
                auditPO.setId(new Long(SequenceManager.getSequence("")));
                String cmd = CommonUtils.checkNull(request.getParamValue("cmd"));
                if (cmd.equals("1")) {
                    auditPO.setStatus(Constant.DEALER_SECEND_STATUS_01);
                    auditPO.setUserId(Constant.DEALER_SECEND_AUDIT_01);

                    DealerInfoDao.getInstance().insert(auditPO);

                    TmDealerPO dealerPO = new TmDealerPO();
                    TmDealerPO dealerPO1 = new TmDealerPO();
                    dealerPO.setDealerId(auditPO.getDealerId());
                    dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_01);
                    factory.update(dealerPO, dealerPO1);
                } else {
                    auditPO.setStatus(Constant.DEALER_SECEND_STATUS_02);
                    auditPO.setUserId(Constant.DEALER_SECEND_AUDIT_02);

                    DealerInfoDao.getInstance().insert(auditPO);

                    TmDealerPO dealerPO = new TmDealerPO();
                    TmDealerPO dealerPO1 = new TmDealerPO();
                    dealerPO.setDealerId(auditPO.getDealerId());
                    dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_02);
                    factory.update(dealerPO, dealerPO1);
                }
                
                // 其它数据保存
                tmDealerSecondLevelPO.setFkDealerId(Long.parseLong(dealerId));
                
                TmDealerSecondLevelPO tmDealerSecondLevelPO1 = new TmDealerSecondLevelPO();
                tmDealerSecondLevelPO1.setFkDealerId(Long.parseLong(dealerId));
                List<TmDealerSecondLevelPO> tmDealerSecondLevelPO1Lst = factory.select(tmDealerSecondLevelPO1);
                if (tmDealerSecondLevelPO1Lst != null && tmDealerSecondLevelPO1Lst.size() == 1) {
                    factory.update(tmDealerSecondLevelPO1Lst.get(0), tmDealerSecondLevelPO);
                } else {
                    factory.insert(tmDealerSecondLevelPO);
                }
            }
            List<Object> inParameter = new ArrayList<Object>();
            List outParameter = new ArrayList();
            factory.callProcedure("p_vw_org_dealer_service", inParameter, outParameter);
            act.setOutData("message", "操作成功!");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 判断当前经销商的公司下是否已经挂了相同业务类型的经销商
     * 
     * @param companyId
     * @param dealerType
     * @param dealerId
     * @return
     */
    public boolean companyBeUsed(String companyId, String dealerType, String dealerId) {
        boolean isUsed = false;
        List<Object> params = new ArrayList<Object>();
        StringBuffer sbSql = new StringBuffer();

        sbSql.append("select a.company_name\n");
        sbSql.append("  from tm_company a, tm_dealer b\n");
        sbSql.append(" where b.company_id = a.company_id\n");
        sbSql.append(" and b.dealer_type = ?\n");
        sbSql.append("   and a.company_id = ?\n");
        params.add(dealerType);
        params.add(companyId);

        if (!dealerId.equals("")) {
            sbSql.append(" and b.dealer_id <> ?");
            params.add(dealerId);
        }

        List list = factory.select(sbSql.toString(), params, new DAOCallback<TmCompanyPO>() {

            public TmCompanyPO wrapper(ResultSet rs, int arg1) {
                TmCompanyPO tcp = new TmCompanyPO();
                try {
                    tcp.setCompanyName(rs.getString("COMPANY_ID"));
                } catch (Exception e) {
                }
                return tcp;
            }
        });

        if (list.size() > 0) {
            isUsed = true;
        }

        return isUsed;
    }

    public void getDateBuffer(String key, StringBuffer sbSql) {
        // wizard_lee ORA-01830: date format picture ends before converting
        // entire input string 2014-06-10
        if (key.equals("VI_APPLAY_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("VI_BEGIN_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("VI_COMPLETED_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("VI_CONFRIM_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("VI_SUPPORT_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("VI_SUPPORT_END_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("FIRST_SUB_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("FIRST_GETCAR_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("FIRST_SAELS_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("AUTHORIZATION_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("AUTHORIZE_GET_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("AUTHORIZE_SUB_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("AUTHORIZE_EFFECT_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("ANNOUNCEMENT_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("ANNOUNCEMENT_END_DATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("SITEDATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else if (key.equals("DESTROYDATE")) {
            sbSql.append("to_date(SUBSTR(?,1,10),'yyyy-mm-dd')");
        } else {
            sbSql.append("?");
        }
    }

    /**
     * 经销商维护修改
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void modifyDealerInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            Date newdate = new Date();
            SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE")).trim();// 经销商代码
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));// 经销商名称
            String shortName = CommonUtils.checkNull(request.getParamValue("SHORT_NAME"));// 简称
            String dealerLevel = CommonUtils.checkNull(request.getParamValue("DEALERLEVEL"));// 经销商等级
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));// 上级组织ID
            String sJDealerId = CommonUtils.checkNull(request.getParamValue("sJDealerId"));// 上级经销商ID
            String dealerType = CommonUtils.checkNull(request.getParamValue("DEALERTYPE"));// 经销商类型
            String companyId = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));// 经销商公司ID
            String provinceId = CommonUtils.checkNull(request.getParamValue("province"));// 省
            String cityId = CommonUtils.checkNull(request.getParamValue("city"));// 市
            String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));// 邮编
            String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
            String phone = CommonUtils.checkNull(request.getParamValue("phone"));// 电话
            
            String faxNo = CommonUtils.checkNull(request.getParamValue("faxNo"));// 传真
            String email = CommonUtils.checkNull(request.getParamValue("email"));// 邮箱
            String address = CommonUtils.checkNull(request.getParamValue("address"));// 详细地址
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));// 备注
            String status = CommonUtils.checkNull(request.getParamValue("DEALERSTATUS"));// 经销商状态
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商Id
            String dealerClass = CommonUtils.checkNull(request.getParamValue("DEALERCLASS"));
            // String
            // priceId=CommonUtils.checkNull(request.getParamValue("priceId"));//价格Id
            String erpCode = CommonUtils.checkNull(request.getParamValue("erpCode"));// 开票单位
            String billAddress = CommonUtils.checkNull(request.getParamValue("billAddress"));//开票地址
            String taxesNo = CommonUtils.checkNull(request.getParamValue("taxesNo"));// 税号
            String labourType = CommonUtils.checkNull(request.getParamValue("DEALER_LABOUR_CODE"));// 经销商作业等级
            //String isDqv = CommonUtils.checkNull(request.getParamValue("IS_DQV"));// 是否为DQV  2017-08-16注释
            String isSpecial = CommonUtils.checkNull(request.getParamValue("IS_SPECIAL"));// 是否特商

            /*********** add by liuxh 20101116增加结算级别和开票级别维护 **************/
            String balanceLevel = CommonUtils.checkNull(request.getParamValue("BALANCE_LEVEL"));// 结算等级别
            String invoiceLevel = CommonUtils.checkNull(request.getParamValue("INVOICE_LEVEL"));// 开票级别
            /*********** add by liuxh 20101116增加结算级别和开票级别维护 **************/
            String service_status = CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS"));
            /*********** add by zhumingwei 2011-02-22 **************/
            String COUNTIES = CommonUtils.checkNull(request.getParamValue("COUNTIES"));// 区/县
            String TOWNSHIP = CommonUtils.checkNull(request.getParamValue("TOWNSHIP"));// 乡
            String LEGAL = CommonUtils.checkNull(request.getParamValue("LEGAL"));// 法人
            String WEBMASTER_PHONE = CommonUtils.checkNull(request.getParamValue("WEBMASTER_PHONE"));// 站长电话
            String DUTY_PHONE = CommonUtils.checkNull(request.getParamValue("DUTY_PHONE"));// 值班电话
            String BANK = CommonUtils.checkNull(request.getParamValue("BANK"));// 开户行
            String MAIN_RESOURCES = CommonUtils.checkNull(request.getParamValue("MAIN_RESOURCES"));// 维修资源
            String ADMIN_LEVEL = CommonUtils.checkNull(request.getParamValue("ADMIN_LEVEL"));// 行政级别
            String IMAGE_LEVEL = CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL"));// 形象等级
            // String TAX_LEVEL =
            // CommonUtils.checkNull(request.getParamValue("TAX_LEVEL"));//纳税级别
            /*********** add by zhumingwei 2011-02-22 **************/

            /*********** add by yuyong 2013-04-10 **************/
            String INVOICE_ACCOUNT = CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT"));// 开票账号
            String INVOICE_ADD = CommonUtils.checkNull(request.getParamValue("INVOICE_ADD"));// 开票地址
            String INVOICE_PHONE = CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE"));// 开票电话
            String INVOICE_POST_ADD = CommonUtils.checkNull(request.getParamValue("INVOICE_POST_ADD"));// 发票邮寄地址
            /*********** add by yuyong 2013-04-10 **************/

            /*********** add by wenyudan 2013-07-28 **************/
            String siteDate = CommonUtils.checkNull(request.getParamValue("siteDate"));// 建站日期
            String destroyDate = CommonUtils.checkNull(request.getParamValue("destroyDate"));// 撤站日期
            String WEBMASTER_NAME = CommonUtils.checkNull(request.getParamValue("WEBMASTER_NAME"));// 站长姓名
            String spy_Man = CommonUtils.checkNull(request.getParamValue("spy_Man"));// 索赔员姓名
            String spy_phone = CommonUtils.checkNull(request.getParamValue("spy_phone"));// 索赔员电话
            String zzaddress = CommonUtils.checkNull(request.getParamValue("zzaddress"));// 注册地址
            String brandq = CommonUtils.checkNull(request.getParamValue("brandq"));// 品牌

            String IMAGE_LEVEL2 = CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL2"));
            String chAddress = CommonUtils.checkNull(request.getParamValue("chAddress"));
            String chAddress2 = CommonUtils.checkNull(request.getParamValue("chAddress2"));

            /*********** add end wenyudan 2013-07-28 **************/

            /*********** add end wenyudan 2013-07-28 **************/
            String legalTel = CommonUtils.checkNull(request.getParamValue("LEGAL_TEL"));// 法人电话
            String marketName = CommonUtils.checkNull(request.getParamValue("MARKET_NAME"));// 销售经理姓名
            String marketTel = CommonUtils.checkNull(request.getParamValue("MARKET_TEL"));// 销售经理电话
            // 经销商评级 没有

            // 校验新维护的经销商是否存在 开始

            // 结束
            // 插入新的经销商开始
            // 生成dealerId
            // session中取到经销商的车厂公司ID和用户ID
            Long oemCompanyId = logonUser.getCompanyId();
            Long userId = logonUser.getUserId();
            // Date currTime = new Date(System.currentTimeMillis());
            Date currTime = newformat.parse(newformat.format(newdate));
            // 实例化dao层
            DealerInfoDao dao = DealerInfoDao.getInstance();
            Map<String, Object> map = dao.getDealerInfobyId(dealerId);
            String yuanDealerLevel = String.valueOf(map.get("DEALER_LEVEL"));
            List<TmOrgPO> list = dao.getOrgInfo(new Long(companyId));
            // TcUserPO tcUser1=new TcUserPO();
            // tcUser1.setUserId(userId);
            // TcUserPO tcUser2=new TcUserPO();
            // tcUser2.setUpdateDate(currTime);
            // dao.update(tcUser1,tcUser2);
            TmOrgPO tmo = new TmOrgPO();
            if (list.size() > 0) {
                tmo = (TmOrgPO) list.get(0);
            }
            TmDealerPO po = new TmDealerPO();
            TmDealerPO conntionPO = new TmDealerPO();
            conntionPO.setDealerId(new Long(dealerId));

            if (!"".equals(siteDate)) {
                po.setSitedate(DateUtil.str2Date(siteDate, "-"));

            }
            if (!"".equals(destroyDate)) {
                po.setDestroydate(DateUtil.str2Date(destroyDate, "-"));

            }
            if (!IMAGE_LEVEL2.equals("")) {
                po.setImageLevel2(Integer.valueOf(IMAGE_LEVEL2));
            }
            po.setChAddress(chAddress);
            po.setChAddress2(chAddress2);
            // po.setWebmasterName(WEBMASTER_NAME);
            po.setSpyMan(spy_Man);
            po.setSpyPhone(spy_phone);
            po.setZzaddress(zzaddress);
            po.setBrand(brandq);
            po.setBalanceLevel(balanceLevel);
            po.setInvoiceLevel(invoiceLevel);

            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setDealerShortname(shortName);
            po.setCompanyId(new Long(companyId));
            po.setAddress(address);
            po.setBillAddress(billAddress);
            // po.setErpCode(erpCode);
            po.setLegalTel(legalTel);
            // po.setMarketName(marketName);
            po.setMarketTel(marketTel);
            if (labourType != null && !labourType.equals("")) {
                po.setDealerLabourType(Integer.valueOf(labourType));
            } else {
                po.setDealerLabourType(Integer.valueOf(0));
            }
            if (!"".equals(taxesNo)) {
                po.setTaxesNo(taxesNo.trim());
            }
            if (!"".equals(cityId)) {
                po.setCityId(new Long(cityId));
            }
            if (!"".equals(provinceId)) {
                po.setProvinceId(new Long(provinceId));
            }
            // po.setCreateBy(userId);
            // po.setCreateDate(currTime);
            po.setUpdateBy(userId);
            po.setUpdateDate(currTime);
            po.setDealerLevel(new Integer(dealerLevel));
            po.setDealerOrgId(tmo.getOrgId());
            po.setDealerType(new Integer(dealerType));

            /*********** add by zhumingwei 2011-02-22 **************/
            if (!"".equals(COUNTIES)) {
                po.setCounties(Integer.parseInt(COUNTIES));
            }
            po.setTownship(TOWNSHIP);
            po.setLegal(LEGAL);
            po.setWebmasterPhone(WEBMASTER_PHONE);
            po.setDutyPhone(DUTY_PHONE);
            po.setBank(BANK);
            // po.setTaxLevel(TAX_LEVEL);//暂时还未用到
            if (!"".equals(MAIN_RESOURCES)) {
                po.setMainResources(Integer.parseInt(MAIN_RESOURCES));
            }
            if (!"".equals(IMAGE_LEVEL)) {
                // po.setImageLevel(Integer.parseInt(IMAGE_LEVEL));
            }
            po.setChangeDate(new Date());
            if (!"".equals(ADMIN_LEVEL)) {
                po.setAdminLevel(Integer.parseInt(ADMIN_LEVEL));
            }
            /*********** add by zhumingwei 2011-02-22 **************/

            if (String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel)) {
                po.setParentDealerD(new Long(-1));
            } else {
                po.setParentDealerD(new Long(sJDealerId));
            }
            if (dealerClass != null && dealerClass != "") {
                po.setDealerClass(Integer.parseInt(dealerClass));
            }
            po.setEmail(email);
            po.setFaxNo(faxNo);
            po.setLinkMan(linkMan);
            po.setOemCompanyId(oemCompanyId);
            po.setPhone(phone);
            po.setRemark(remark);
            po.setStatus(new Integer(status));
            po.setZipCode(zipCode);
            //po.setIsDqv(Integer.parseInt(isDqv)); 2017-08-16注释
            po.setIsSpecial("".equals(isSpecial) ? -1 : Integer.parseInt(isSpecial));
            // if(!"".equals(priceId)){
            // po.setPriceId(new Long(priceId));
            // }
            /*********** add by yuyong 2013-04-10 **************/
            // po.setInvoiceAccount(INVOICE_ACCOUNT);
            // po.setInvoiceAdd(INVOICE_ADD);
            // po.setInvoicePhone(INVOICE_PHONE);
            po.setInvoicePostAdd(INVOICE_POST_ADD);
            /*********** add by yuyong 2013-04-10 **************/
            if (!"".equals(service_status)) {
                po.setServiceStatus(Long.valueOf(service_status));
            }
            dao.update(conntionPO, po);
            if (String.valueOf(Constant.DEALER_LEVEL_01).equals(yuanDealerLevel))// 如果修改之前也是一级经销商则先删除原来与组织的对应关系
            {
                TmDealerOrgRelationPO tdor = new TmDealerOrgRelationPO();
                tdor.setDealerId(new Long(dealerId));
                dao.delete(tdor);
            }
            if (String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel))// 修改为一级时插入与车厂组织的对应关系
            {
                String relationId = SequenceManager.getSequence("");
                TmDealerOrgRelationPO tdor = new TmDealerOrgRelationPO();
                tdor.setRelationId(new Long(relationId));
                tdor.setOrgId(new Long(orgId));
                tdor.setDealerId(new Long(dealerId));
                tdor.setCreateBy(userId);
                tdor.setCreateDate(currTime);
                dao.insert(tdor);
            }

            // zhumingwei add 2013-10-18
            // 这里判断如果是售后经销商的话那么再查询TtPartBillDefinePO表里面有记录没有，如果有就修改，反之就insert
            if (!"".equals(dealerType) && "10771002".equals(dealerType)) {
                // 发票信息
                TtPartBillDefinePO srcBillPo = new TtPartBillDefinePO();
                srcBillPo.setDealerId(Long.parseLong(dealerId));
                PartDlrDao dao1 = PartDlrDao.getInstance();
                List<TtPartBillDefinePO> ct = dao1.select(srcBillPo);
                String invTypeStr = CommonUtils.checkNull(request.getParamValue("dlrInvTpe"));
                int invType = Constant.DLR_INVOICE_TYPE_01;
                if (null != invTypeStr && !"".equals(invTypeStr)) {
                    invType = Integer.parseInt(invTypeStr);
                }

                if (ct.size() > 0) {
                    TtPartBillDefinePO tarBillPo = new TtPartBillDefinePO();
                    tarBillPo.setAccount(CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT")).trim());
                    tarBillPo.setTaxName(CommonUtils.checkNull(request.getParamValue("TAX_NAME")).trim());
                    tarBillPo.setInvType(invType);
                    tarBillPo.setTaxNo(CommonUtils.checkNull(request.getParamValue("TAX_NO")).trim());
                    tarBillPo.setBank(CommonUtils.checkNull(request.getParamValue("BANK")).trim());
                    tarBillPo.setTel(CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE")).trim());
                    tarBillPo.setAddr(CommonUtils.checkNull(request.getParamValue("INVOICE_ADDR")).trim());
                    tarBillPo.setMailAddr(CommonUtils.checkNull(request.getParamValue("MAIL_ADDR")).trim());
                    tarBillPo.setUpdateBy(logonUser.getUserId());
                    tarBillPo.setUpdateDate(new Date());
                    dao.update(srcBillPo, tarBillPo);
                } else {
                    TtPartBillDefinePO tarBillPo = new TtPartBillDefinePO();
                    tarBillPo.setBillId(Long.parseLong(SequenceManager.getSequence("")));
                    tarBillPo.setDealerId(Long.parseLong(dealerId));
                    tarBillPo.setDealerCode(dealerCode);
                    tarBillPo.setDealerName(dealerName);
                    tarBillPo.setAccount(CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT")).trim());
                    tarBillPo.setTaxName(CommonUtils.checkNull(request.getParamValue("TAX_NAME")).trim());
                    tarBillPo.setInvType(invType);
                    tarBillPo.setTaxNo(CommonUtils.checkNull(request.getParamValue("TAX_NO")).trim());
                    tarBillPo.setBank(CommonUtils.checkNull(request.getParamValue("BANK")).trim());
                    tarBillPo.setTel(CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE")).trim());
                    tarBillPo.setAddr(CommonUtils.checkNull(request.getParamValue("INVOICE_ADDR")).trim());
                    tarBillPo.setCreateBy(logonUser.getUserId());
                    tarBillPo.setCreateDate(new Date());
                    tarBillPo.setState(Constant.STATUS_ENABLE);
                    tarBillPo.setStatus(1);
                    dao.insert(tarBillPo);
                }
            }

            // 结束
            act.setForword(querySalesDealerInitUrl);//2017-08-16改为跳转至 查询销售经销商页面
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addBusiness() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 经销商ID
            String DEALERLEVEL = CommonUtils.checkNull(request.getParamValue("DEALERLEVEL"));// 经销商等级
            String DEALERTYPE = CommonUtils.checkNull(request.getParamValue("DEALERTYPE"));// 经销商类型
            String DEALERSTATUS = CommonUtils.checkNull(request.getParamValue("DEALERSTATUS"));// 经销商是否有效
            // 从session中查询公司ID
            Long companyId = logonUser.getCompanyId();
            DealerInfoDao dao = DealerInfoDao.getInstance();
            // 查询经销商业务范围信息
            List<Map<String, Object>> businessList = dao.getBusinessNoInDealer(dealerId, companyId);
            act.setOutData("businessList", businessList);
            act.setOutData("dealerId", dealerId);
            act.setOutData("DEALERLEVEL", DEALERLEVEL);
            act.setOutData("DEALERTYPE", DEALERTYPE);
            act.setOutData("DEALERSTATUS", DEALERSTATUS);
            act.setForword(addBusinessUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    // 验证在同一基地同一经销商公司不为售后的一级经销商是否一级存在

    public void querySameBusiness() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商ID

            // 从session中查询公司ID
            Long companyId = logonUser.getCompanyId();
            DealerInfoDao dao = DealerInfoDao.getInstance();
            // 查询经销商业务范围信息
            List<Map<String, Object>> ls = dao.querySameBusiness(dealerId, companyId);

            if (ls.size() > 0) {
                for (int i = 0; i < ls.size(); i++) {
                    if (Long.valueOf(ls.get(i).get("COUNT").toString()) > 1) {
                        act.setOutData("msg", "false1");

                        break;
                    } else {
                        /*********************** modify xieyj ***************************/
                        // TmDealerPO po = new TmDealerPO();
                        // po.setDealerId(Long.valueOf(dealerId));
                        // List list = factory.select(po);
                        // po = (TmDealerPO)list.get(0);
                        // String company_id =
                        // request.getParamValue("COMPANY_ID");
                        // if(!po.getCompanyId().toString().equals(company_id)){
                        // TmDealerPO po_ = new TmDealerPO();
                        // po_.setCompanyId(Long.valueOf(company_id));
                        // // po_.setStatus(Constant.STATUS_ENABLE);
                        // List list_ = factory.select(po_);
                        // if(list_ != null && list_.size()>0){
                        // act.setOutData("msg", "false3");
                        // }else{
                        // act.setOutData("msg", "true");
                        // }
                        // }else{
                        // act.setOutData("msg", "true");
                        // }
                        act.setOutData("msg", "true");
                        /*******************************************************************/
                    }
                }

                /*
                 * if(ls.size()>1){
                 * 
                 * act.setOutData("msg", "flase1"); } else
                 * if(Long.valueOf(ls.get(0).get("COUNT").toString())>0){
                 * 
                 * act.setOutData("msg", "flase2"); }
                 * 
                 * else{ act.setOutData("msg", "true"); }
                 */
            }

            else {
                /******************************** modify xieyj ********************************/
                // TmDealerPO po = new TmDealerPO();
                // po.setDealerId(Long.valueOf(dealerId));
                // List list = factory.select(po);
                // po = (TmDealerPO)list.get(0);
                // String company_id = request.getParamValue("COMPANY_ID");
                // String dealerType = request.getParamValue("DEALERTYPE");
                // if(!po.getCompanyId().toString().equals(company_id)){
                // TmDealerPO po_ = new TmDealerPO();
                // po_.setCompanyId(Long.valueOf(company_id));
                // po_.setDealerType(Integer.valueOf(dealerType));
                // // po_.setStatus(Constant.STATUS_ENABLE);
                // List list_ = factory.select(po_);
                // if(list_ != null && list_.size()>0){
                // act.setOutData("msg", "false3");
                // }else{
                // act.setOutData("msg", "true");
                // }
                // }else{
                // act.setOutData("msg", "true");
                // }
                act.setOutData("msg", "true");
                /***********************************************************************/
            }

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    public void saveBusiness() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商ID
            String areaIds = CommonUtils.checkNull(request.getParamValue("areaId"));// 多个业务范围ID以","分割
            // 从session中查询公司ID
            Date currTime = new Date(System.currentTimeMillis());
            Long userId = logonUser.getUserId();
            DealerInfoDao dao = DealerInfoDao.getInstance();
            if (!"".equals(areaIds)) {
                String[] s = areaIds.split(",");
                for (int i = 0; i < s.length; i++) {
                    String areaId = s[i];
                    String relationId = SequenceManager.getSequence("");
                    TmDealerBusinessAreaPO po = new TmDealerBusinessAreaPO();
                    po.setAreaId(Long.valueOf(areaId));
                    po.setCreateBy(userId);
                    po.setCreateDate(currTime);
                    po.setDealerId(Long.valueOf(dealerId));
                    po.setRelationId(Long.valueOf(relationId));
                    dao.insert(po);
                }
            }
            // queryMonth(act, dao, dealerId, queryDealerInitDetailUrl);
            act.setOutData("returnValue", 1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void defaultMyPriceView() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        act.setOutData("priceId", request.getParamValue("priceId"));
        act.setOutData("relationId", request.getParamValue("relationId"));
        act.setOutData("dealerId", request.getParamValue("dealerId"));
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.setForword(getMyPriceUtl);
    }

    /*
     * 设置默认价格价格
     */
    public void defaultPrice() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            DealerInfoDao dao = DealerInfoDao.getInstance();
            RequestWrapper request = act.getRequest();
            //String startDate = request.getParamValue("startDate");// 开始和结束日期
            //String endDate = request.getParamValue("endDate");
            String priceId = request.getParamValue("priceId");
            //TtVsPricePO ttVsPricePo1 = new TtVsPricePO();
            //ttVsPricePo1.setPriceId(Long.parseLong(priceId));
            //TtVsPricePO ttVsPricePo2 = new TtVsPricePO();
            //ttVsPricePo2.setStartDate(CommonUtils.parseDate(startDate));// 修改开始和结束日期
            //ttVsPricePo2.setEndDate(CommonUtils.parseDate(endDate));
            //dao.update(ttVsPricePo1, ttVsPricePo2);
            
            //String DEALERSTATUS = request.getParamValue("DEALERSTATUS");
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商ID
            String relationId = CommonUtils.checkNull(request.getParamValue("relationId"));// 经销商与默认价格关系表主键
//          TmDealerPriceRelationPO po5 = new TmDealerPriceRelationPO();
//          po5.setRelationId(Long.parseLong(relationId));
//          po5.setIsDefault(Constant.IF_TYPE_YES);
//
//          if (DEALERSTATUS.equals((Constant.IF_TYPE_NO).toString()) && dao.select(po5).size() > 0) {
//              act.setOutData("returnValue", "2");
//          } else {
                TmDealerPriceRelationPO po1 = new TmDealerPriceRelationPO();
                po1.setDealerId(Long.parseLong(dealerId));
                TmDealerPriceRelationPO po2 = new TmDealerPriceRelationPO();
                po2.setIsDefault(Constant.IF_TYPE_NO);// 把该经销商下所有的价格都设置成不是默认的
                dao.update(po1, po2);
                TmDealerPriceRelationPO po3 = new TmDealerPriceRelationPO();
                po3.setRelationId(Long.parseLong(relationId));
                TmDealerPriceRelationPO po4 = new TmDealerPriceRelationPO();
                po4.setIsDefault(Constant.IF_TYPE_YES);// 设置当前经销商是默认
                dao.update(po3, po4);

            //}
                ProductManageDao productDao = ProductManageDao.getInstance();
                List<Map<String, Object>> priceList = productDao.getDealerPrice(logonUser.getCompanyId(), dealerId);
                act.setOutData("types", priceList);
                querySalesDealerInfo(act, dao, dealerId, querySalesDealerInitDetailUrl);
                
//          // dao.delete(po);
//          ProductManageDao productDao = ProductManageDao.getInstance();
//          List<Map<String, Object>> types = productDao.getmyPriceTypeList(logonUser.getCompanyId(), dealerId);
//          act.setOutData("types", types);
//          // queryMonth(act, dao, dealerId, queryDealerInitDetailUrl);
//          Map<String, Object> map = dao.getDealerInfobyId(dealerId);
//          // 查询经销商业务范围信息
//          List<Map<String, Object>> businessList = dao.getDealerBusinessArea(dealerId);
//          // 查询经销商地址信息
//          List<Map<String, Object>> addressList = dao.getAddress(dealerId);
//          act.setOutData("businessList", businessList);
//          act.setOutData("addressList", addressList);
//          act.setOutData("map", map);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商价格有误！");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /*
     * 删除价格
     */
    public void delPrice() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            RequestWrapper request = act.getRequest();
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商ID
            TmDealerPriceRelationPO po = new TmDealerPriceRelationPO();
            String relationId = CommonUtils.checkNull(request.getParamValue("relationId"));// 经销商与默认价格关系表主键
            DealerInfoDao dao = DealerInfoDao.getInstance();
            po.setRelationId(Long.parseLong(relationId));
            dao.delete(po);
            ProductManageDao productDao = ProductManageDao.getInstance();
//          List<Map<String, Object>> types = productDao.getmyPriceTypeList(logonUser.getCompanyId(), dealerId);
//          act.setOutData("types", types);
//          queryMonth(act, dao, dealerId, queryDealerInitDetailUrl);
            List<Map<String, Object>> priceList = productDao.getDealerPrice(logonUser.getCompanyId(), dealerId);
            act.setOutData("types", priceList);
            querySalesDealerInfo(act, dao, dealerId, querySalesDealerInitDetailUrl);
            
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商价格有误！");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void delBusiness() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商ID
            String relationId = CommonUtils.checkNull(request.getParamValue("relationId"));// 经销商与业务范围关系表主键
            DealerInfoDao dao = DealerInfoDao.getInstance();
            TmDealerBusinessAreaPO po = new TmDealerBusinessAreaPO();
            po.setRelationId(Long.valueOf(relationId));

            /*
             * List areaList = dao.select(po) ;
             * 
             * if(!CommonUtils.isNullList(areaList)) { TmDealerBusinessAreaPO
             * tdba = (TmDealerBusinessAreaPO)areaList.get(0) ;
             * 
             * String areaId = tdba.getAreaId().toString() ;
             * 
             * AddressAreaDAO.getInstance().addressAreaDeleteByDlrAndArea(dealerId
             * , areaId) ; }
             */

            dao.delete(po);

            queryMonth(act, dao, dealerId, queryDealerInitDetailUrl);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void delAdd() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商ID
            String id = CommonUtils.checkNull(request.getParamValue("id"));// 地址表主键
            DealerInfoDao dao = DealerInfoDao.getInstance();
            TmVsAddressPO po = new TmVsAddressPO();
            po.setId(Long.valueOf(id));
            dao.delete(po);
            queryMonth(act, dao, dealerId, queryDealerInitDetailUrl);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /*
     * 价格维护新增页面
     */
    public void addPrice() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 经销商ID
            act.setOutData("dealerId", dealerId);
            // ProductManageDao productDao = new ProductManageDao();
            // List<Map<String, Object>> mytypes =
            // productDao.getmyOtherPriceTypeList(logonUser.getCompanyId(),dealerId);
            // act.setOutData("mytypes",mytypes);
            act.setForword(getPriceUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addPrice_showPrice() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID")); // 经销商ID
            String priceDesc = CommonUtils.checkNull(request.getParamValue("priceDesc")); // 价格类型描述
            // String defaultPrice =
            // CommonUtils.checkNull(request.getParamValue("DEFAULTPRICE"));
            // //默认价格
            ProductManageDao productDao = ProductManageDao.getInstance();

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = productDao.getmyOtherPriceTypeList(priceDesc, logonUser.getCompanyId(), dealerId, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护:新增价格");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 地址维护新增页面
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void addAddress() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 经销商ID
            List<Map<String, Object>> areaList = DealerRelationDAO.getInstance().getAreaByDlr(dealerId);
            act.setOutData("areaList", areaList);
            act.setOutData("dealerId", dealerId);
            act.setForword(addAddressUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /*
     * 添加经销商价格
     */
    public void savePrice() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Date currTime = new Date(System.currentTimeMillis());
        RequestWrapper request = act.getRequest();
        DealerInfoDao dao = DealerInfoDao.getInstance();
        TmDealerPriceRelationPO po = new TmDealerPriceRelationPO();
        String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
        String defaultPrice = Constant.IF_TYPE_YES.toString();
        String defaultPriceN = Constant.IF_TYPE_NO.toString();
        String userPrice = CommonUtils.checkNull(request.getParamValue("priceId"));

        // TmDealerPriceRelationPO num1=new TmDealerPriceRelationPO();
        // num1.setDealerId(Long.parseLong(dealerId));
        // num1.setIsDefault(Integer.parseInt(defaultPriceN));
        //
        // TmDealerPriceRelationPO num2=new TmDealerPriceRelationPO();
        // num2.setDealerId(Long.parseLong(dealerId));
        //
        // dao.update(num2,num1);

        po.setDealerId(Long.parseLong(dealerId));
        po.setPriceId(Long.parseLong(userPrice));
        dao.delete(po);//先删除 后增加
        po.setRelationId(new Long(SequenceManager.getSequence("")));
        po.setPriceId(Long.parseLong(userPrice));
        po.setIsDefault(Integer.parseInt(defaultPriceN));
        po.setCreateBy(logonUser.getUserId());
        po.setCreateDate(currTime);
        // po.setUpdateBy();
        // po.setUpdateDate();
        dao.insert(po);

        // TmDealerPriceRelationPO mypoFirst=new TmDealerPriceRelationPO();
        // mypoFirst.setDealerId(Long.parseLong(dealerId));
        // mypoFirst.setPriceId(Long.parseLong(userPrice));
        // TmDealerPriceRelationPO mypoEnd=new TmDealerPriceRelationPO();
        // mypoEnd.setIsDefault(Integer.parseInt(defaultPrice));
        // act.setOutData("defaultPrice",defaultPrice);
        // dao.update(mypoFirst,mypoEnd);
        act.setOutData("returnValue", 1);
    }

    /*
     * 添加经销商地址
     */
    public void saveAddress() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商ID
            // String addCode =
            // CommonUtils.checkNull(request.getParamValue("ADD_CODE"));// 地址代码
            String address = CommonUtils.checkNull(request.getParamValue("address"));// 地址名称
            String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
            String status = CommonUtils.checkNull(request.getParamValue("ADDRESSSTATUS"));// 地址状态
            String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 电话
            String mobilePhone = CommonUtils.checkNull(request.getParamValue("mobilePhone"));
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));// 备注
            String proviceId = CommonUtils.checkNull(request.getParamValue("province"));// 获取省份
            String cityId = CommonUtils.checkNull(request.getParamValue("city"));// 获取城市
            String areaId = CommonUtils.checkNull(request.getParamValue("area"));// 获取县城
            String myaddress = CommonUtils.checkNull(request.getParamValue("myaddress"));// 获取收获单位
            String[] areaArray = request.getParamValues("addressAreas");
            String theArea = "";
            AddressAddApply aaa = new AddressAddApply();

            address = aaa.getAddDet(proviceId, cityId, areaId) + address;

            // 从session中查询公司ID
            Date currTime = new Date(System.currentTimeMillis());
            Long userId = logonUser.getUserId();
            DealerInfoDao dao = DealerInfoDao.getInstance();

            // if(areaArray != null) {
            // int len = areaArray.length ;

            // for(int i=0; i<len; i++) {
            TmVsAddressPO po = new TmVsAddressPO();
            if (proviceId != "") {
                po.setProvinceId(Long.parseLong(proviceId));
            }
            if (cityId != "") {
                po.setCityId(Long.parseLong(cityId));
            }
            if (areaId != "") {
                po.setAreaId(Long.parseLong(areaId));
            }
            po.setReceiveOrg(myaddress);
            po.setAddCode(aaa.getAddressCode(dealerId));
            po.setAddress(address);
            po.setCreateBy(userId);
            po.setCreateDate(currTime);
            po.setDealerId(Long.valueOf(dealerId));
            po.setId(Long.valueOf(SequenceManager.getSequence("")));
            po.setLinkMan(linkMan);
            po.setStatus(new Integer(status));
            po.setTel(tel);
            po.setRemark(remark);
            po.setMobilePhone(mobilePhone);
            // theArea = areaArray[i].split("\\|")[0] ;
            // po.setBAreaId(Long.parseLong(theArea)) ;

            po.setLimitType(Long.parseLong(Constant.ADDRESS_TIME_LIMIT_PERP.toString()));

            dao.insert(po);
            // }
            // }

            act.setOutData("returnValue", 1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void updateAction() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = null;
        try {
            RequestWrapper request = act.getRequest();
            logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String addressId = CommonUtils.checkNull(request.getParamValue("addressId")); // 地址id
            String address = CommonUtils.checkNull(request.getParamValue("address")).trim();
            String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));
            String tel = CommonUtils.checkNull(request.getParamValue("tel"));
            String mobilePhone = CommonUtils.checkNull(request.getParamValue("mobilePhone"));
            // String remark =
            // CommonUtils.checkNull(request.getParamValue("remark"));
            String receiveOrg = CommonUtils.checkNull(request.getParamValue("myaddress"));
            String province = CommonUtils.checkNull(request.getParamValue("province"));
            String city = CommonUtils.checkNull(request.getParamValue("city"));
            String area = CommonUtils.checkNull(request.getParamValue("area"));
            String status = CommonUtils.checkNull(request.getParamValue("ADDRESSSTATUS"));// 地址状态
            // String addressUse =
            // CommonUtils.checkNull(request.getParamValue("addressUse"));
            // String limitType =
            // CommonUtils.checkNull(request.getParamValue("limit"));
            // String limitStartDate =
            // CommonUtils.checkNull(request.getParamValue("limitStartDate"));
            // String limitEndDate =
            // CommonUtils.checkNull(request.getParamValue("limitEndDate"));
            String[] areas = request.getParamValues("addressAreas");

            DealerInfoDao dao = DealerInfoDao.getInstance();
            AddressAddApply aaa = new AddressAddApply();

            address = aaa.getAddDet(province, city, area) + address;

            if (!CommonUtils.isNullString(addressId)) {
                TmVsAddressPO oldAddress = new TmVsAddressPO();
                oldAddress.setId(Long.parseLong(addressId));

                TmVsAddressPO newAddress = new TmVsAddressPO();

                newAddress.setProvinceId(Long.parseLong(province));
                newAddress.setCityId(Long.parseLong(city));
                newAddress.setAreaId(Long.parseLong(area));
                newAddress.setStatus(Integer.parseInt(status));
                newAddress.setAddress(address);
                newAddress.setReceiveOrg(receiveOrg);
                newAddress.setMobilePhone(mobilePhone);
                newAddress.setLinkMan(linkMan);
                newAddress.setTel(tel);
                newAddress.setUpdateDate(new Date(System.currentTimeMillis()));
                newAddress.setUpdateBy(logonUser.getUserId());

                dao.update(oldAddress, newAddress);
            }

            if (areas != null) {
                TmVsAddressPO addressPO = new TmVsAddressPO();
                int len = areas.length;

                for (int i = 0; i < len; i++) {
                    addressPO.setId(Long.parseLong(SequenceManager.getSequence("")));

                    String areaId = areas[i].split("\\|")[0];
                    String dlrId = areas[i].split("\\|")[1];

                    addressPO.setAddCode(aaa.getAddressCode(dlrId));
                    addressPO.setAddress(address.trim());
                    addressPO.setLinkMan(linkMan.trim());
                    addressPO.setTel(tel.trim());
                    addressPO.setMobilePhone(mobilePhone);
                    addressPO.setReceiveOrg(receiveOrg.trim());

                    if (!"".equals(dlrId)) {
                        addressPO.setDealerId(Long.parseLong(dlrId));
                    }

                    if (!"".equals(province)) {
                        addressPO.setProvinceId(Long.parseLong(province));
                    }

                    if (!"".equals(city)) {
                        addressPO.setCityId(Long.parseLong(city));
                    }

                    if (!"".equals(area)) {
                        addressPO.setAreaId(Long.parseLong(area));
                    }

                    addressPO.setBAreaId(Long.parseLong(areaId));
                    addressPO.setStatus(Integer.parseInt(status));

                    addressPO.setLimitType(Long.parseLong(Constant.ADDRESS_TIME_LIMIT_PERP.toString()));

                    addressPO.setCreateBy(logonUser.getUserId());
                    addressPO.setCreateDate(new Date());

                    dao.insert(addressPO);
                }
            }

            act.setOutData("returnValue", 1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址修改");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 地址维护修改页面pre查询
     * 
     * @param null
     * @return void
     * @throws Exception
     */
    public void modifyAdd() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 经销商ID
            String id = CommonUtils.checkNull(request.getParamValue("id"));// 地址ID

            AddressAddApply aaa = new AddressAddApply();

            String addressStr = aaa.queryAddName(id);

            DealerInfoDao dao = DealerInfoDao.getInstance();
            Map<String, Object> map = dao.getAddressfobyId(id);

            String address = map.get("ADDRESS").toString();

            address = address.replaceFirst(addressStr, "");

            map.put("ADDRESS", address);

            List<Map<String, Object>> areaList = DealerRelationDAO.getInstance().getAreaByDlr(dealerId);
            // List<TtAddressAreaRPO> taarList =
            // AddressAreaDAO.getInstance().addressAreaQuery(id) ;
            act.setOutData("areaList", areaList);
            // act.setOutData("taarList", taarList) ;
            act.setOutData("dealerId", dealerId);
            act.setOutData("map", map);
            act.setForword(modifyAddressUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    // 修改地址功能
    public void modifyAddressInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));// 经销商ID
            String addCode = CommonUtils.checkNull(request.getParamValue("ADD_CODE"));// 地址代码
            String address = CommonUtils.checkNull(request.getParamValue("ADDRESS"));// 地址名称
            String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
            String status = CommonUtils.checkNull(request.getParamValue("ADDRESSSTATUS"));// 地址状态
            String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 电话
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));// 备注
            String id = CommonUtils.checkNull(request.getParamValue("ADDRESS_ID"));// 修改的地址ID
            String proviceId = CommonUtils.checkNull(request.getParamValue("province"));// 修改省份
            String cityId = CommonUtils.checkNull(request.getParamValue("city"));// 修改城市
            String areaId = CommonUtils.checkNull(request.getParamValue("area"));// 修改县城
            String[] areaArray = request.getParamValues("areaList");

            AddressAddApply aaa = new AddressAddApply();

            address = aaa.getAddDet(proviceId, cityId, areaId) + address;

            Date currTime = new Date(System.currentTimeMillis());
            Long userId = logonUser.getUserId();

            DealerInfoDao dao = DealerInfoDao.getInstance();
            TmVsAddressPO po = new TmVsAddressPO();
            TmVsAddressPO contionPo = new TmVsAddressPO();
            contionPo.setId(Long.valueOf(id));
            if (proviceId != "") {
                po.setProvinceId(Long.parseLong(proviceId));
            }
            if (cityId != "") {
                po.setCityId(Long.parseLong(cityId));
            }
            if (areaId != "") {
                po.setAreaId(Long.parseLong(areaId));
            }

            po.setReceiveOrg(request.getParamValue("myaddress"));// 收货单位
            po.setAddCode(addCode);
            po.setAddress(address);
            po.setUpdateBy(userId);
            po.setUpdateDate(currTime);
            po.setDealerId(Long.valueOf(dealerId));
            po.setLinkMan(linkMan);
            po.setStatus(new Integer(status));
            po.setTel(tel);
            po.setRemark(remark);
            dao.update(contionPo, po);

            Map<String, String> map = new HashMap<String, String>();

            map.put("addressId", id);
            map.put("userId", logonUser.getUserId().toString());

            AddressAreaDAO.getInstance().addressAreaDelete(map);

            if (areaArray != null) {
                int len = areaArray.length;

                for (int i = 0; i < len; i++) {
                    map.put("areaId", areaArray[i]);

                    AddressAreaDAO.getInstance().addressAreaInsert(map);
                }
            }

            act.setOutData("returnValue", 1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 查询 销售经销商信息 2017-08-14
     * @param act
     * @param dao
     * @param dealerId
     * @param url
     * @throws Exception
     */
    public void querySalesDealerInfo(ActionContext act, DealerInfoDao dao, String dealerId, String url) throws Exception {
        // 查询插入信息
        Map<String, Object> map = dao.getDealerInfobyId(dealerId);
        // 查询经销商业务范围信息
        List<Map<String, Object>> businessList = dao.getDealerBusinessArea(dealerId);
        // 查询经销商地址信息
        List<Map<String, Object>> addressList = dao.getAddress(dealerId);
        
        List<Map<String, Object>> billingList = dao.getBillingInfo(dealerId);

        // 查询经销商个人信贷信息
        //List<Map<String, Object>> mortgageList = dao.queryMortgageForDealer(dealerId);

        /*
         * if(addressList != null) { int len = addressList.size() ;
         * 
         * AddressArea addressArea = new AddressArea() ;
         * 
         * for(int i=0; i<len; i++) { addressList.get(i).put("areas",
         * addressArea.addressAreaStrGet(addressList.get(i).get("ID").toString())) ; } }
         */

        act.setOutData("businessList", businessList);
        act.setOutData("addressList", addressList);
        act.setOutData("billingList", billingList);
        //act.setOutData("mortgageList", mortgageList);
        act.setOutData("map", map);
        act.setForword(url);
    }

    public void queryMonth(ActionContext act, DealerInfoDao dao, String dealerId, String url) throws Exception {
        // 查询插入信息
        Map<String, Object> map = dao.getDealerInfobyId(dealerId);
        // 查询经销商业务范围信息
        List<Map<String, Object>> businessList = dao.getDealerBusinessArea(dealerId);
        // 查询经销商地址信息
        List<Map<String, Object>> addressList = dao.getAddress(dealerId);

        // 查询经销商个人信贷信息
        List<Map<String, Object>> mortgageList = dao.queryMortgageForDealer(dealerId);

        PartDlrDao dao1 = PartDlrDao.getInstance();
        // 查询经销商地址信息
        List<Map<String, Object>> addressList1 = dao1.getAddress(dealerId);
        act.setOutData("addressList1", addressList1);
        // 查询开票信息
        Map<String, Object> invoiceListMap = dao1.getInvoiceInfo(dealerId);
        act.setOutData("invoiceListMap", invoiceListMap);

        ProductManageDao productDao = ProductManageDao.getInstance();

        act.setOutData("businessList", businessList);
        act.setOutData("addressList", addressList);
        act.setOutData("mortgageList", mortgageList);
        List<Map<String, Object>> brand = productDao.getbrandList();// 品牌
        act.setOutData("brand", brand);

        act.setOutData("map", map);

        act.setForword(url);
    }

    public void chkDlr() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        try {
            RequestWrapper request = act.getRequest();

            String dlrId = request.getParamValue("dlrId");
            String dlrCode = request.getParamValue("dlrCode");

            if (null == dlrCode || "".equals(dlrCode)) {
                return;
            }
            int errInfo = 0;

            TmDealerPO tmd = new TmDealerPO();

            /* 
             */
            if (null != dlrId && !"".equals(dlrId)) {
                TmDealerPO tmd_A = new TmDealerPO();
                tmd_A.setDealerId(Long.parseLong(dlrId));

                TmDealerPO tmdA = (TmDealerPO) DealerInfoDao.getInstance().select(tmd_A).get(0);
                String Code = tmdA.getDealerCode();

                if (null != Code && Code.equals(dlrCode)) {
                    return;
                }
            }

            tmd.setDealerCode(dlrCode);

            long len = DealerInfoDao.getInstance().select(tmd).size();

            if (len >= 1) {
                errInfo = 1;
            }

            act.setOutData("errInfo", errInfo);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护修改操作");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void chkDealer() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        try {
            RequestWrapper request = act.getRequest();

            int errInfo = 0; // 错误信息：0表示正常，1表示dealerId不存在，2表示当前经销商下维护有下级经销商
            String dealerId = request.getParamValue("dealerId"); // 获取经销商id
            String flag = request.getParamValue("flag"); // 获取操作空间标识

            if (null == dealerId || "".equals(dealerId)) {
                errInfo = 1;
            } else {
                TmDealerPO tmd = new TmDealerPO();
                tmd.setParentDealerD(Long.parseLong(dealerId));
                tmd.setStatus(Constant.STATUS_ENABLE);

                long len = DealerInfoDao.getInstance().select(tmd).size(); // 获取所属当前经销商的下级经销商记录条数

                if (len > 0) {
                    errInfo = 2;
                } else {
                    errInfo = 0;
                }
            }

            act.setOutData("flag", flag);
            act.setOutData("errInfo", errInfo);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护修改操作");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**** add by liuxh 201116 增加经销商代码重复判断 *****/
    public void checkDealerCode() {
        String msg = "true";
        POFactory factory = POFactoryBuilder.getInstance();
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));// 经销商代码
        String dealerType = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));// 经销商类型
        String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));

        List<Object> params = new ArrayList<Object>();
        StringBuffer sbSql = new StringBuffer();

        sbSql.append("select * from tm_dealer where dealer_code = ? ");
        params.add(dealerCode);
        if (!dealerId.equals("")) {
            sbSql.append(" and dealer_id <> ?");
            params.add(dealerId);
        }

        List list = factory.select(sbSql.toString(), params, new JCDynaBeanCallBack());
        if (list.size() > 0) {
            msg = "false";
        }
        /*********************** modify xieyj **************************/
        // else{
        // String companyId =
        // CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
        // TmDealerPO po=new TmDealerPO();
        // po.setCompanyId(Long.valueOf(companyId));
        // po.setDealerType(Integer.valueOf(dealerType));
        // po.setStatus(Constant.STATUS_ENABLE);
        // List list = factory.select(po);
        // if(list != null && list.size()>0){
        // msg = "1";
        // }
        // }
        /*************************************************************/
        act.setOutData("msg", msg);
    }

    public void checkDealerCode2nd() {
        String msg = "true";
        POFactory factory = POFactoryBuilder.getInstance();
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));// 经销商代码
        String dealerType = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));// 经销商类型
        String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));

        List<Object> params = new ArrayList<Object>();
        StringBuffer sbSql = new StringBuffer();

        sbSql.append("select * from tm_dealer where dealer_code = ? ");
        params.add(dealerCode);
        if (!dealerId.equals("")) {
            sbSql.append(" and dealer_id <> ?");
            params.add(dealerId);
        }

        List list = factory.select(sbSql.toString(), params, new JCDynaBeanCallBack());
        if (list.size() > 0) {
            msg = "false";
        }
        /*********************** modify xieyj **************************/
        // else{
        // String companyId =
        // CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
        // TmDealerPO po=new TmDealerPO();
        // po.setCompanyId(Long.valueOf(companyId));
        // po.setDealerType(Integer.valueOf(dealerType));
        // po.setStatus(Constant.STATUS_ENABLE);
        // List list = factory.select(po);
        // if(list != null && list.size()>0){
        // msg = "1";
        // }
        // }
        /*************************************************************/
        act.setOutData("msg", msg);
    }

    /*
     * 经销商结算暂停
     */
    public void banlanceStop() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(BALANCE_STOP_URL);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算暂停");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryDealerBS() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String code = req.getParamValue("DEALER_CODE");
            String name = req.getParamValue("DEALER_NAME");
            String status = req.getParamValue("STATUS");
            String yieldly = req.getParamValue("YIELDLY");
            StringBuffer sql = new StringBuffer();

            if (StringUtil.notNull(code))
                sql.append(" and d.dealer_code like '%").append(code).append("%'\n");
            if (StringUtil.notNull(name))
                sql.append(" and d.dealer_name like '%").append(name).append("%'\n");
            if (StringUtil.notNull(status))
                sql.append(" and t.status=").append(status).append("\n");
            if (StringUtil.notNull(yieldly))
                sql.append(" and t.yieldly=").append(yieldly).append("\n");

            DealerInfoDao dao = DealerInfoDao.getInstance();
            int pageSize = 15;
            Integer curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = dao.getDealerBS(sql.toString(), pageSize, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算暂停");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void goUpdateBS() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        POFactory factory = POFactoryBuilder.getInstance();
        try {
            String id = req.getParamValue("id");
            String con = " and t.id=" + id;
            DealerInfoDao dao = DealerInfoDao.getInstance();
            PageResult<Map<String, Object>> ps = dao.getDealerBS(con, 2, 1);
            if (ps.getTotalRecords() > 0) {
                if (ps.getRecords().size() > 0)
                    act.setOutData("map", ps.getRecords().get(0));
            }
            String dealerId = req.getParamValue("DEALER_ID");
            String yilyle = req.getParamValue("YILYLE");
            List<Map<String, Object>> myList = dao.selectDealerUpdateAuthing(dealerId, yilyle);
            act.setOutData("mylist", myList);

            FsFileuploadPO detail = new FsFileuploadPO();
            detail.setYwzj(Long.valueOf(id));
            BalanceMainDao dao1 = BalanceMainDao.getInstance();
            List<FsFileuploadPO> lists = dao1.select(detail);
            act.setOutData("lists", lists);

            act.setForword(BALANCE_STOP_UPDATE);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算暂停");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void dealerBSUpdate() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String id = req.getParamValue("ID");
            String status = req.getParamValue("STATUS");
            TtAsDealerTypePO dealer = new TtAsDealerTypePO();
            TtAsDealerTypePO dealer2 = new TtAsDealerTypePO();
            dealer2.setId(Long.parseLong(id));
            dealer.setStatus(status);
            dealer.setUpdateBy(logonUser.getUserId().toString());
            dealer.setUpdateDate(new Date());

            // 将附件保存
            String ywzj = "";
            if (id != null && !id.equals("")) {
                ywzj = id;
            } else {
                ywzj = String.valueOf(dealer2.getId());
            }
            String[] fjids = req.getParamValues("fjid");// 获取文件ID
            FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
            if (id != null && !id.equals("")) {// 修改的时候
                FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
                FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
            } else {
                FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
            }

            DealerInfoDao dao = DealerInfoDao.getInstance();
            dao.update(dealer2, dealer);
            dealerBSUpdateAuthitem();
            act.setForword(BALANCE_STOP_URL);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算暂停");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /***********
     * 插入经销商暂停功能变更记录
     */
    public void dealerBSUpdateAuthitem() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String id = req.getParamValue("ID");
            String status = req.getParamValue("STATUS");
            String yilie = req.getParamValue("YILIE");
            String remark = req.getParamValue("remark");
            TtAsDealerTypeAuthitemPO po = new TtAsDealerTypeAuthitemPO();
            po.setId(Utility.getLong(SequenceManager.getSequence("")));
            po.setDealerId(Long.valueOf(req.getParamValue("DEALER_ID")));
            po.setYilie(Integer.valueOf(yilie));
            po.setRemark(remark);
            po.setUpdateType(Integer.valueOf(status));
            po.setCreateBy(logonUser.getUserId());
            po.setCreateDate(new Date());
            DealerInfoDao dao = DealerInfoDao.getInstance();
            dao.insert(po);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算暂停");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    // zhumingwei add by 2011-02-24
    public void dealerInfoChangeInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(dealerInfoChangeInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void authDealerChangeInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(authDealerChangeInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryAuthDealerChange() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String code = request.getParamValue("code");
            String status = request.getParamValue("status");
            String startDate = request.getParamValue("startDate");
            String endDate = request.getParamValue("endDate");
            if (Utility.testString(startDate))
                startDate = startDate + " 00:00:00";
            if (Utility.testString(endDate))
                endDate = endDate + " 23:59:59";
            DealerInfoDao dao = DealerInfoDao.getInstance();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            // Constant.PAGE_SIZE, curPage
            PageResult<Map<String, Object>> ps = dao.queryAuthDealerChange(code, status, startDate, endDate, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    // zhumingwei add by 2011-02-24
    public void queryDealerInfoChange() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String code = request.getParamValue("code");
            String status = request.getParamValue("status");
            String dealerId = logonUser.getDealerId();
            String startDate = request.getParamValue("startDate");
            String endDate = request.getParamValue("endDate");
            if (Utility.testString(startDate))
                startDate = startDate + " 00:00:00";
            if (Utility.testString(endDate))
                endDate = endDate + " 23:59:59";
            DealerInfoDao dao = DealerInfoDao.getInstance();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            // Constant.PAGE_SIZE, curPage
            PageResult<Map<String, Object>> ps = dao.queryDealerInfoChange(dealerId, code, status, startDate, endDate, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addDealerInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dealer_id = logonUser.getDealerId();
            DealerInfoDao dao = DealerInfoDao.getInstance();
            List<Map<String, Object>> list = dao.selectDealerInfo(Long.parseLong(dealer_id));
            String dealerNum = SequenceManager.getSequence("BG");
            Date time = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time1 = sdf.format(time);
            act.setOutData("time1", time1);
            act.setOutData("list", list.get(0));
            act.setOutData("dealerNum", dealerNum);
            int DEALER_LEVEL_02 = Constant.DEALER_LEVEL_02;
            act.setOutData("DEALER_LEVEL_02", DEALER_LEVEL_02);
            act.setForword(addDealerInfoChangeUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    // zhumingwei add by 2011-02-25
    public void saveDealerChangeInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            // CommonUtils.checkNull() 校验是否为空
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));// 原本经销商ID
            String dealerNum = CommonUtils.checkNull(request.getParamValue("dealerNum"));// 单据编号
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));// 经销商代码
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));// 经销商名称
            String dealerType = CommonUtils.checkNull(request.getParamValue("dealerType"));// 经销商类别
            Date changeDate = new Date();// 变更时间
            String rootOrgCode = CommonUtils.checkNull(request.getParamValue("rootOrgCode"));// 上级组织(备份)
            // String orgCode =
            // CommonUtils.checkNull(request.getParamValue("orgCode"));//上级组织
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));// 上级组织
            String dealerClass = CommonUtils.checkNull(request.getParamValue("dealerClass"));// 经销商类型(备份)
            String DEALERCLASS = CommonUtils.checkNull(request.getParamValue("DEALERCLASS"));// 经销商类型
            String companyShortName = CommonUtils.checkNull(request.getParamValue("companyShortName"));// 经销商公司(备份)
            // String COMPANY_NAME =
            // CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));//经销商公司
            String COMPANY_ID = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));// 经销商公司
            String province = CommonUtils.checkNull(request.getParamValue("province"));// 省份(备份)
            String province1 = CommonUtils.checkNull(request.getParamValue("province1"));// 省份
            String city = CommonUtils.checkNull(request.getParamValue("city"));// 地级市(备份)
            String city1 = CommonUtils.checkNull(request.getParamValue("city1"));// 地级市
            String COUNTIES = CommonUtils.checkNull(request.getParamValue("COUNTIES"));// 区/县(备份)
            String COUNTIES1 = CommonUtils.checkNull(request.getParamValue("COUNTIES1"));// 区/县
            String township = CommonUtils.checkNull(request.getParamValue("township"));// 乡(备份)
            String TOWNSHIP = CommonUtils.checkNull(request.getParamValue("TOWNSHIP"));// 乡
            String erpCode = CommonUtils.checkNull(request.getParamValue("erpCode"));// 开票单位(备份)
            String erpCode1 = CommonUtils.checkNull(request.getParamValue("erpCode1"));// 开票单位
            String address = CommonUtils.checkNull(request.getParamValue("address"));// 详细地址(备份)
            String address1 = CommonUtils.checkNull(request.getParamValue("address1"));// 详细地址
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));// 备注(备份)
            String remark1 = CommonUtils.checkNull(request.getParamValue("remark1"));// 备注
            String imageLevel = CommonUtils.checkNull(request.getParamValue("imageLevel"));// 形象等级(备份)
            String IMAGE_LEVEL = CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL"));// 形象等级
            String dealerLevel = CommonUtils.checkNull(request.getParamValue("dealerLevel"));// 经销商等级(备份)
            String DEALER_LEVEL = CommonUtils.checkNull(request.getParamValue("DEALER_LEVEL"));// 经销商等级
            String parentName = CommonUtils.checkNull(request.getParamValue("parentName"));// 上级经销商(备份)
            // String sJDealerCode =
            // CommonUtils.checkNull(request.getParamValue("sJDealerCode"));//上级经销商
            String sJDealerId = CommonUtils.checkNull(request.getParamValue("sJDealerId"));// 上级经销商
            String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));// 邮编(备份)
            String zipCode1 = CommonUtils.checkNull(request.getParamValue("zipCode1"));// 邮编
            String legal = CommonUtils.checkNull(request.getParamValue("legal"));// 法人(备份)
            String LEGAL = CommonUtils.checkNull(request.getParamValue("LEGAL"));// 法人
            String personCharge = CommonUtils.checkNull(request.getParamValue("personCharge"));// 负责人(备份)
            String personCharge1 = CommonUtils.checkNull(request.getParamValue("personCharge1"));// 负责人
            String webmasterPhone = CommonUtils.checkNull(request.getParamValue("webmasterPhone"));// 站长电话(备份)
            String WEBMASTER_PHONE = CommonUtils.checkNull(request.getParamValue("WEBMASTER_PHONE"));// 站长电话
            String dutyPhone = CommonUtils.checkNull(request.getParamValue("dutyPhone"));// 值班电话(备份)
            String DUTY_PHONE = CommonUtils.checkNull(request.getParamValue("DUTY_PHONE"));// 值班电话
            String phone = CommonUtils.checkNull(request.getParamValue("phone"));// 电话(备份)
            String phone1 = CommonUtils.checkNull(request.getParamValue("phone1"));// 电话
            String faxNo = CommonUtils.checkNull(request.getParamValue("faxNo"));// 传真(备份)
            String faxNo1 = CommonUtils.checkNull(request.getParamValue("faxNo1"));// 传真
            String email = CommonUtils.checkNull(request.getParamValue("email"));// Email(备份)
            String email1 = CommonUtils.checkNull(request.getParamValue("email1"));// Email

            DealerInfoDao dao = DealerInfoDao.getInstance();// 实例化

            TmDealerChangePO po = new TmDealerChangePO();
            po.setDealerChangeId(Long.parseLong(SequenceManager.getSequence("")));
            po.setDealerId(Long.parseLong(dealerId));
            po.setDealerNum(dealerNum);
            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setDealerCategory(Long.parseLong(dealerType));
            po.setDealerTime(changeDate);
            po.setDealerLevelBck(Long.parseLong(dealerLevel));
            if (!"".equals(DEALER_LEVEL)) {
                po.setDealerLevel(Long.parseLong(DEALER_LEVEL));
            }
            if (!"".equals(parentName)) {
                po.setDealerHigherBck(parentName);
            }
            if (!"".equals(sJDealerId)) {
                po.setDealerHigher(sJDealerId);
            }
            po.setZipCodeBck(zipCode);
            po.setZipCode(zipCode1);
            po.setLegalBck(legal);
            po.setLegal(LEGAL);
            po.setPersonChargeBck(personCharge);
            po.setPersonCharge(personCharge1);
            po.setWebmasterPhoneBck(webmasterPhone);
            po.setWebmasterPhone(WEBMASTER_PHONE);
            po.setDutyPhoneBck(dutyPhone);
            po.setDutyPhone(DUTY_PHONE);
            po.setPhoneBck(phone);
            po.setPhone(phone1);
            po.setFaxBck(faxNo);
            po.setFax(faxNo1);
            po.setEmailBck(email);
            po.setEmail(email1);

            po.setDealerHigherOrgBck(rootOrgCode);
            po.setDealerHigherOrg(orgId);
            if (!"".equals(dealerClass)) {
                po.setDealerTypeBck(Long.parseLong(dealerClass));
            }
            if (!"".equals(DEALERCLASS)) {
                po.setDealerType(Long.parseLong(DEALERCLASS));
            }
            po.setDealerCompanyBck(companyShortName);
            po.setDealerCompany(COMPANY_ID);
            po.setDealerProvincesBck(Long.parseLong(province));
            if (!"".equals(province1)) {
                po.setDealerProvinces(Long.parseLong(province1));
            }
            if (!"".equals(city)) {
                po.setDealerCityBck(Long.parseLong(city));
            }
            if (!"".equals(city1)) {
                po.setDealerCity(Long.parseLong(city1));
            }
            if (!"".equals(COUNTIES)) {
                po.setCountiesBck(Long.parseLong(COUNTIES));
            }
            if (!"".equals(COUNTIES1)) {
                po.setCounties(Long.parseLong(COUNTIES1));
            }
            po.setTownshipBck(township);
            po.setTownship(TOWNSHIP);
            po.setBillingUnitBck(erpCode);
            po.setBillingUnit(erpCode1);
            po.setDetailsAddressBck(address);
            po.setDetailsAddress(address1);
            po.setDealerRemakeBck(remark);
            po.setDealerRemake(remark1);
            if (!"".equals(imageLevel)) {
                po.setImageLevelBck(Long.parseLong(imageLevel));
            }
            if (!"".equals(IMAGE_LEVEL)) {
                po.setImageLevel(Long.parseLong(IMAGE_LEVEL));
            }

            if (!"".equals(orgId) || !"".equals(DEALERCLASS) || !"".equals(COMPANY_ID) || !"".equals(province1) || !"".equals(city1) || !"".equals(COUNTIES1)
                    || !"".equals(TOWNSHIP) || !"".equals(erpCode1) || !"".equals(address1) || !"".equals(remark1) || !"".equals(IMAGE_LEVEL)) {
                po.setStatus(Long.parseLong(Constant.DEALER_CHANGE_01.toString()));
            } else {
                po.setStatus(Long.parseLong(Constant.DEALER_CHANGE_03.toString()));
                // 如果不需要审核的，那么直接更新经销商表的数据
                TmDealerPO dealerPo = new TmDealerPO();
                dealerPo.setDealerId(Long.parseLong(dealerId));
                TmDealerPO dealerPoValue = new TmDealerPO();
                if (!"".equals(DEALER_LEVEL)) {
                    dealerPoValue.setDealerLevel(Integer.parseInt(DEALER_LEVEL));// 经销商等级
                }
                if (!"".equals(sJDealerId)) {
                    dealerPoValue.setParentDealerD(Long.parseLong(sJDealerId));// 上级经销商
                }
                if (!"".equals(zipCode1)) {
                    dealerPoValue.setZipCode(zipCode1);
                }
                if (!"".equals(LEGAL)) {
                    dealerPoValue.setLegal(LEGAL);
                }
                if (!"".equals(personCharge1)) {
                    dealerPoValue.setPersonCharge(personCharge1);
                }
                if (!"".equals(WEBMASTER_PHONE)) {
                    // dealerPoValue.setWebmasterPhone(WEBMASTER_PHONE);
                }
                if (!"".equals(DUTY_PHONE)) {
                    dealerPoValue.setDutyPhone(DUTY_PHONE);
                }
                if (!"".equals(phone1)) {
                    dealerPoValue.setPhone(phone1);
                }
                if (!"".equals(faxNo1)) {
                    // dealerPoValue.setFaxNo(faxNo1);
                }
                if (!"".equals(email1)) {
                    // dealerPoValue.setEmail(email1);
                }
                dao.update(dealerPo, dealerPoValue);
            }
            po.setCreateBy(logonUser.getUserId());
            po.setCreateDate(new Date());

            // 附近功能：
            String[] fjids = request.getParamValues("fjid");
            FileUploadManager.fileUploadByBusiness(po.getDealerChangeId().toString(), fjids, logonUser);

            dao.insert(po);
            act.setForword(dealerInfoChangeInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商变更新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void dealerDetailInfo() {
        ActionContext act = ActionContext.getContext();
        // AclUserBean logonUser =
        // (AclUserBean)act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        // CommonUtils.checkNull() 校验是否为空
        String id = CommonUtils.checkNull(request.getParamValue("id"));
        DealerInfoDao dao = DealerInfoDao.getInstance();// 实例化
        TmDealerChangePO po = new TmDealerChangePO();
        po.setDealerChangeId(Long.parseLong(id));
        TmDealerChangePO poValue = (TmDealerChangePO) dao.select(po).get(0);
        act.setOutData("poValue", poValue);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(poValue.getDealerTime());
        act.setOutData("time", time);
        StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
        List<FsFileuploadPO> fileList = smDao.queryAttachFileInfo(id);
        act.setOutData("fileList", fileList);

        if (!"".equals(CommonUtils.checkNull(poValue.getDealerHigherOrg()))) {
            TmOrgPO popo = new TmOrgPO();
            popo.setOrgId(Long.parseLong(poValue.getDealerHigherOrg()));
            TmOrgPO popoValue = (TmOrgPO) dao.select(popo).get(0);
            act.setOutData("orgCode", popoValue.getOrgCode());
        }

        if (!"".equals(CommonUtils.checkNull(poValue.getDealerCompany()))) {
            TmCompanyPO popopo = new TmCompanyPO();
            popopo.setCompanyId(Long.parseLong(poValue.getDealerCompany()));
            TmCompanyPO popopoValue = (TmCompanyPO) dao.select(popopo).get(0);
            String companyName = popopoValue.getCompanyShortname();
            act.setOutData("companyName", companyName);
        }

        act.setForword(dealerDetailInfo);
    }

    public void authInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        // CommonUtils.checkNull() 校验是否为空
        String id = CommonUtils.checkNull(request.getParamValue("id"));
        DealerInfoDao dao = DealerInfoDao.getInstance();// 实例化
        TmDealerChangePO po = new TmDealerChangePO();
        po.setDealerChangeId(Long.parseLong(id));
        TmDealerChangePO poValue = (TmDealerChangePO) dao.select(po).get(0);
        act.setOutData("poValue", poValue);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(poValue.getDealerTime());
        act.setOutData("time", time);
        StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
        List<FsFileuploadPO> fileList = smDao.queryAttachFileInfo(id);
        act.setOutData("fileList", fileList);
        act.setOutData("ID", id);

        if (!"".equals(CommonUtils.checkNull(poValue.getDealerHigherOrg()))) {
            TmOrgPO popo = new TmOrgPO();
            popo.setOrgId(Long.parseLong(poValue.getDealerHigherOrg()));
            TmOrgPO popoValue = (TmOrgPO) dao.select(popo).get(0);
            act.setOutData("orgCode", popoValue.getOrgCode());
        }

        if (!"".equals(CommonUtils.checkNull(poValue.getDealerCompany()))) {
            TmCompanyPO popopo = new TmCompanyPO();
            popopo.setCompanyId(Long.parseLong(poValue.getDealerCompany()));
            TmCompanyPO popopoValue = (TmCompanyPO) dao.select(popopo).get(0);
            String companyName = popopoValue.getCompanyShortname();
            act.setOutData("companyName", companyName);
        }
        if (!"".equals(CommonUtils.checkNull(poValue.getDealerHigher()))) {
            TmDealerPO popopopo = new TmDealerPO();
            popopopo.setDealerId(Long.parseLong(poValue.getDealerHigher()));
            TmDealerPO popopopoValue = (TmDealerPO) dao.select(popopopo).get(0);
            String dealerName = popopopoValue.getDealerShortname();
            act.setOutData("dealerName", dealerName);
        }

        act.setForword(authDetailInfo);
    }

    public void updateDealerDetailInfo() {
        ActionContext act = ActionContext.getContext();
        // AclUserBean logonUser =
        // (AclUserBean)act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        String id = CommonUtils.checkNull(request.getParamValue("id"));
        DealerInfoDao dao = DealerInfoDao.getInstance();// 实例化
        TmDealerChangePO po = new TmDealerChangePO();
        po.setDealerChangeId(Long.parseLong(id));
        TmDealerChangePO poValue = (TmDealerChangePO) dao.select(po).get(0);
        act.setOutData("poValue", poValue);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(poValue.getDealerTime());
        act.setOutData("time", time);
        act.setOutData("dealerChangeId", id);
        StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
        List<FsFileuploadPO> fileList = smDao.queryAttachFileInfo(id);
        act.setOutData("fileList", fileList);

        // 得到上级组织代码
        if (!"".equals(CommonUtils.checkNull(poValue.getDealerHigherOrg()))) {
            TmOrgPO popo = new TmOrgPO();
            popo.setOrgId(Long.parseLong(poValue.getDealerHigherOrg()));
            TmOrgPO popoValue = (TmOrgPO) dao.select(popo).get(0);
            String orgCode = popoValue.getOrgCode();
            act.setOutData("dealerHigherOrgId", poValue.getDealerHigherOrg());
            act.setOutData("orgCode", orgCode);
        }
        if (!"".equals(CommonUtils.checkNull(poValue.getDealerCompany()))) {
            TmCompanyPO popopo = new TmCompanyPO();
            popopo.setCompanyId(Long.parseLong(poValue.getDealerCompany()));
            TmCompanyPO popopoValue = (TmCompanyPO) dao.select(popopo).get(0);
            String companyName = popopoValue.getCompanyShortname();
            act.setOutData("companyName", companyName);
            act.setOutData("companyId", poValue.getDealerCompany());
        }
        if (!"".equals(CommonUtils.checkNull(poValue.getDealerHigher()))) {
            TmDealerPO popopopo = new TmDealerPO();
            popopopo.setDealerId(Long.parseLong(poValue.getDealerHigher()));
            TmDealerPO popopopoValue = (TmDealerPO) dao.select(popopopo).get(0);
            String dealerCode = popopopoValue.getDealerCode();
            act.setOutData("dealerCode", dealerCode);
            act.setOutData("dealerId", poValue.getDealerHigher());
        }

        act.setForword(updateDealerDetailInfo);
    }

    public void updateDealerChangeInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        DealerInfoDao dao = DealerInfoDao.getInstance();// 实例化
        String dealerChangeId = CommonUtils.checkNull(request.getParamValue("dealerChangeId"));
        // String orgCode =
        // CommonUtils.checkNull(request.getParamValue("orgCode"));//上级组织
        String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));// 上级组织
        String DEALERCLASS = CommonUtils.checkNull(request.getParamValue("DEALERCLASS"));// 经销商类型
        // String COMPANY_NAME =
        // CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));//经销商公司
        String COMPANY_ID = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));// 经销商公司
        String province1 = CommonUtils.checkNull(request.getParamValue("province1"));// 省份
        String city1 = CommonUtils.checkNull(request.getParamValue("city1"));// 地级市
        String COUNTIES1 = CommonUtils.checkNull(request.getParamValue("COUNTIES1"));// 区/县
        String TOWNSHIP = CommonUtils.checkNull(request.getParamValue("TOWNSHIP"));// 乡
        String erpCode1 = CommonUtils.checkNull(request.getParamValue("erpCode1"));// 开票单位
        String address1 = CommonUtils.checkNull(request.getParamValue("address1"));// 详细地址
        String remark1 = CommonUtils.checkNull(request.getParamValue("remark1"));// 备注
        String IMAGE_LEVEL = CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL"));// 形象等级
        String DEALER_LEVEL = CommonUtils.checkNull(request.getParamValue("DEALER_LEVEL"));// 经销商等级
        // String sJDealerCode =
        // CommonUtils.checkNull(request.getParamValue("sJDealerCode"));//上级经销商
        String sJDealerId = CommonUtils.checkNull(request.getParamValue("sJDealerId"));// 上级经销商
        String zipCode1 = CommonUtils.checkNull(request.getParamValue("zipCode1"));// 邮编
        String LEGAL = CommonUtils.checkNull(request.getParamValue("LEGAL"));// 法人
        String personCharge1 = CommonUtils.checkNull(request.getParamValue("personCharge1"));// 负责人
        String WEBMASTER_PHONE = CommonUtils.checkNull(request.getParamValue("WEBMASTER_PHONE"));// 站长电话
        String DUTY_PHONE = CommonUtils.checkNull(request.getParamValue("DUTY_PHONE"));// 值班电话
        String phone1 = CommonUtils.checkNull(request.getParamValue("phone1"));// 电话
        String faxNo1 = CommonUtils.checkNull(request.getParamValue("faxNo1"));// 传真
        String email1 = CommonUtils.checkNull(request.getParamValue("email1"));// Email
        TmDealerChangePO po = new TmDealerChangePO();
        po.setDealerChangeId(Long.parseLong(dealerChangeId));
        TmDealerChangePO poValue = new TmDealerChangePO();

        if (!"".equals(DEALER_LEVEL)) {
            poValue.setDealerLevel(Long.parseLong(DEALER_LEVEL));
        }
        if (!"".equals(sJDealerId)) {
            poValue.setDealerHigher(sJDealerId);
        }
        poValue.setZipCode(zipCode1);
        poValue.setLegal(LEGAL);
        poValue.setPersonCharge(personCharge1);
        poValue.setWebmasterPhone(WEBMASTER_PHONE);
        poValue.setDutyPhone(DUTY_PHONE);
        poValue.setPhone(phone1);
        poValue.setFax(faxNo1);
        poValue.setEmail(email1);

        poValue.setDealerHigherOrg(orgId);
        if (!"".equals(DEALERCLASS)) {
            poValue.setDealerType(Long.parseLong(DEALERCLASS));
        }
        poValue.setDealerCompany(COMPANY_ID);
        if (!"".equals(province1)) {
            poValue.setDealerProvinces(Long.parseLong(province1));
        }
        if (!"".equals(city1)) {
            poValue.setDealerCity(Long.parseLong(city1));
        }
        if (!"".equals(COUNTIES1)) {
            poValue.setCounties(Long.parseLong(COUNTIES1));
        }
        poValue.setTownship(TOWNSHIP);
        poValue.setBillingUnit(erpCode1);
        poValue.setDetailsAddress(address1);
        poValue.setDealerRemake(remark1);
        if (!"".equals(IMAGE_LEVEL)) {
            poValue.setImageLevel(Long.parseLong(IMAGE_LEVEL));
        }
        if (!"".equals(orgId) || !"".equals(DEALERCLASS) || !"".equals(COMPANY_ID) || !"".equals(province1) || !"".equals(city1) || !"".equals(COUNTIES1) || !"".equals(TOWNSHIP)
                || !"".equals(erpCode1) || !"".equals(address1) || !"".equals(remark1) || !"".equals(IMAGE_LEVEL)) {
            poValue.setStatus(Long.parseLong(Constant.DEALER_CHANGE_01.toString()));
        } else {
            poValue.setStatus(Long.parseLong(Constant.DEALER_CHANGE_03.toString()));
        }
        poValue.setUpdateBy(logonUser.getUserId());
        poValue.setUpdateDate(new Date());

        try {
            // 附近功能：
            String ywzj = dealerChangeId;
            String[] fjids = request.getParamValues("fjid");
            FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);// 删除附件
            FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser); // 新添加附件
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dao.update(po, poValue);
        act.setForword(dealerInfoChangeInitUrl);
    }

    public void reported() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        DealerInfoDao dao = DealerInfoDao.getInstance();// 实例化
        String id = CommonUtils.checkNull(request.getParamValue("id"));
        TmDealerChangePO po = new TmDealerChangePO();
        po.setDealerChangeId(Long.parseLong(id));
        TmDealerChangePO poValue = new TmDealerChangePO();
        poValue.setStatus(Long.parseLong(Constant.DEALER_CHANGE_02.toString()));
        int count = dao.update(po, poValue);
        if (count > 0) {
            TmAuthDealerChangePO authPo = new TmAuthDealerChangePO();
            authPo.setId(Long.parseLong(SequenceManager.getSequence("")));
            authPo.setAuthDealer(Long.parseLong(logonUser.getDealerId()));
            authPo.setAuthTime(new Date());
            authPo.setAuthBy(logonUser.getUserId());
            authPo.setAuthStatus(Long.parseLong(Constant.DEALER_CHANGE_02.toString()));
            authPo.setCreateBy(logonUser.getUserId());
            authPo.setCreateDate(new Date());
            dao.insert(authPo);
            act.setOutData("ACTION_RESULT", "1");
        } else {
            act.setOutData("ACTION_RESULT", "2");
        }
    }

    public void authDealerChange() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        DealerInfoDao dao = DealerInfoDao.getInstance();// 实例化
        String dealerHigherOrg = CommonUtils.checkNull(request.getParamValue("dealerHigherOrg"));
        String dealerType = CommonUtils.checkNull(request.getParamValue("dealerType"));
        String companyId = CommonUtils.checkNull(request.getParamValue("companyId"));
        String dealerProvinces = CommonUtils.checkNull(request.getParamValue("dealerProvinces"));
        String dealerCity = CommonUtils.checkNull(request.getParamValue("dealerCity"));
        String counties = CommonUtils.checkNull(request.getParamValue("counties"));
        String township = CommonUtils.checkNull(request.getParamValue("township"));
        String billingUnit = CommonUtils.checkNull(request.getParamValue("billingUnit"));
        String detailsAddress = CommonUtils.checkNull(request.getParamValue("detailsAddress"));
        String dealerRemake = CommonUtils.checkNull(request.getParamValue("dealerRemake"));
        String imageLevel = CommonUtils.checkNull(request.getParamValue("imageLevel"));
        String dealerLevel = CommonUtils.checkNull(request.getParamValue("dealerLevel"));
        String dealerHigher = CommonUtils.checkNull(request.getParamValue("dealerHigher"));
        String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));
        String legal = CommonUtils.checkNull(request.getParamValue("legal"));
        String personCharge = CommonUtils.checkNull(request.getParamValue("personCharge"));
        String webmasterPhone = CommonUtils.checkNull(request.getParamValue("webmasterPhone"));
        String dutyPhone = CommonUtils.checkNull(request.getParamValue("dutyPhone"));
        String phone = CommonUtils.checkNull(request.getParamValue("phone"));
        String fax = CommonUtils.checkNull(request.getParamValue("fax"));
        String email = CommonUtils.checkNull(request.getParamValue("email"));
        String info = CommonUtils.checkNull(request.getParamValue("info"));

        String ID = CommonUtils.checkNull(request.getParamValue("ID"));

        String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
        TmDealerPO po = new TmDealerPO();
        po.setDealerId(Long.parseLong(dealerId));
        TmDealerPO poValue = new TmDealerPO();
        if (!"0".equals(dealerHigherOrg) && !"".equals(dealerHigherOrg)) {
            poValue.setDealerOrgId(Long.parseLong(dealerHigherOrg));
        }
        if (!"0".equals(dealerType) && !"".equals(dealerHigherOrg)) {
            poValue.setDealerClass(Integer.parseInt(dealerType));
        }
        if (!"0".equals(companyId) && !"".equals(dealerHigherOrg)) {
            poValue.setCompanyId(Long.parseLong(companyId));
        }
        if (!"0".equals(dealerProvinces) && !"".equals(dealerHigherOrg)) {
            poValue.setProvinceId(Long.parseLong(dealerProvinces));
        }
        if (!"0".equals(dealerCity) && !"".equals(dealerHigherOrg)) {
            poValue.setCityId(Long.parseLong(dealerCity));
        }
        if (!"0".equals(counties) && !"".equals(dealerHigherOrg)) {
            poValue.setCounties(Integer.parseInt(counties));
        }
        if (!"0".equals(counties) && !"".equals(dealerHigherOrg)) {
            poValue.setCounties(Integer.parseInt(counties));
        }
        if (!"".equals(township)) {
            poValue.setTownship(township);
        }
        if (!"".equals(billingUnit)) {
            poValue.setAddress(billingUnit);
        }
        if (!"".equals(detailsAddress)) {
            poValue.setAddress(detailsAddress);
        }
        if (!"".equals(dealerRemake)) {
            // poValue.setRemark(dealerRemake);
        }
        if (!"0".equals(imageLevel) && !"".equals(dealerHigherOrg)) {
            // poValue.setImageLevel(Integer.parseInt(imageLevel));
        }
        if (!"0".equals(dealerLevel) && !"".equals(dealerHigherOrg)) {
            poValue.setDealerLevel(Integer.parseInt(dealerLevel));
        }
        if (!"0".equals(dealerHigher) && !"".equals(dealerHigher)) {
            poValue.setParentDealerD(Long.parseLong(dealerHigher));
        }
        if (!"".equals(zipCode)) {
            poValue.setZipCode(zipCode);
        }
        if (!"".equals(legal)) {
            poValue.setLegal(legal);
        }
        if (!"".equals(personCharge)) {
            poValue.setPersonCharge(personCharge);
        }
        if (!"".equals(webmasterPhone)) {
            // poValue.setWebmasterPhone(webmasterPhone);
        }
        if (!"".equals(dutyPhone)) {
            poValue.setDutyPhone(dutyPhone);
        }
        if (!"".equals(phone)) {
            poValue.setPhone(phone);
        }
        if (!"".equals(fax)) {
            // poValue.setFaxNo(fax);
        }
        if (!"".equals(email)) {
            // poValue.setEmail(email);
        }
        int count = dao.update(po, poValue);
        if (count > 0) {
            TmDealerChangePO poChange = new TmDealerChangePO();
            poChange.setDealerChangeId(Long.parseLong(ID));
            TmDealerChangePO poChangeValue = new TmDealerChangePO();
            poChangeValue.setStatus(Long.parseLong(Constant.DEALER_CHANGE_03.toString()));
            poChangeValue.setAuthRemark(info);
            poChangeValue.setUpdateBy(logonUser.getUserId());
            poChangeValue.setUpdateDate(new Date());
            int count1 = dao.update(poChange, poChangeValue);
            if (count1 > 0) {
                TmAuthDealerChangePO popo = new TmAuthDealerChangePO();
                popo.setId(Long.parseLong(SequenceManager.getSequence("")));
                popo.setAuthDealer(Long.parseLong(dealerId));
                popo.setAuthTime(new Date());
                popo.setAuthBy(logonUser.getUserId());
                popo.setAuthStatus(Long.parseLong(Constant.DEALER_CHANGE_03.toString()));
                popo.setAuthRemark(info);
                popo.setCreateBy(logonUser.getUserId());
                popo.setCreateDate(new Date());
                dao.insert(popo);
            }

            this.authDealerChangeInit();
        }
    }

    public void rejectDealerChange() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        DealerInfoDao dao = DealerInfoDao.getInstance();// 实例化
        String ID = CommonUtils.checkNull(request.getParamValue("ID"));
        String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
        String info = CommonUtils.checkNull(request.getParamValue("info"));
        TmDealerChangePO po = new TmDealerChangePO();
        po.setDealerChangeId(Long.parseLong(ID));
        TmDealerChangePO poValue = new TmDealerChangePO();
        poValue.setStatus(Long.parseLong(Constant.DEALER_CHANGE_04.toString()));
        poValue.setUpdateBy(logonUser.getUserId());
        poValue.setUpdateDate(new Date());
        poValue.setAuthRemark(info);
        int count = dao.update(po, poValue);
        if (count > 0) {
            TmAuthDealerChangePO popo = new TmAuthDealerChangePO();
            popo.setId(Long.parseLong(SequenceManager.getSequence("")));
            popo.setAuthDealer(Long.parseLong(dealerId));
            popo.setAuthTime(new Date());
            popo.setAuthBy(logonUser.getUserId());
            popo.setAuthStatus(Long.parseLong(Constant.DEALER_CHANGE_04.toString()));
            popo.setAuthRemark(info);
            popo.setCreateBy(logonUser.getUserId());
            popo.setCreateDate(new Date());
            dao.insert(popo);

            this.authDealerChangeInit();
        }
    }

    public void dealerInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        // CommonUtils.checkNull() 校验是否为空
        String id = CommonUtils.checkNull(request.getParamValue("id"));
        DealerInfoDao dao = DealerInfoDao.getInstance();// 实例化
        TmDealerChangePO po = new TmDealerChangePO();
        po.setDealerChangeId(Long.parseLong(id));
        TmDealerChangePO poValue = (TmDealerChangePO) dao.select(po).get(0);
        act.setOutData("poValue", poValue);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(poValue.getDealerTime());
        act.setOutData("time", time);
        StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
        List<FsFileuploadPO> fileList = smDao.queryAttachFileInfo(id);
        act.setOutData("fileList", fileList);
        act.setOutData("ID", id);

        // 得到上级组织代码
        if (!"".equals(CommonUtils.checkNull(poValue.getDealerHigherOrg()))) {
            TmOrgPO popo = new TmOrgPO();
            popo.setOrgId(Long.parseLong(poValue.getDealerHigherOrg()));
            TmOrgPO popoValue = (TmOrgPO) dao.select(popo).get(0);
            String orgCode = popoValue.getOrgCode();
            act.setOutData("dealerHigherOrgId", poValue.getDealerHigherOrg());
            act.setOutData("orgCode", orgCode);
        }
        if (!"".equals(CommonUtils.checkNull(poValue.getDealerCompany()))) {
            TmCompanyPO popopo = new TmCompanyPO();
            popopo.setCompanyId(Long.parseLong(poValue.getDealerCompany()));
            TmCompanyPO popopoValue = (TmCompanyPO) dao.select(popopo).get(0);
            String companyName = popopoValue.getCompanyShortname();
            act.setOutData("companyName", companyName);
            act.setOutData("companyId", poValue.getDealerCompany());
        }
        if (!"".equals(CommonUtils.checkNull(poValue.getDealerHigher()))) {
            TmDealerPO popopopo = new TmDealerPO();
            popopopo.setDealerId(Long.parseLong(poValue.getDealerHigher()));
            TmDealerPO popopopoValue = (TmDealerPO) dao.select(popopopo).get(0);
            String dealerName = popopopoValue.getDealerShortname();
            act.setOutData("dealerName", dealerName);
        }

        act.setForword(dealerChangeInfo);
    }

    public void getCompanyByOtherQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
            String regionCode = CommonUtils.checkNull(request.getParamValue("regionCode"));
            String dealerType = CommonUtils.checkNull(request.getParamValue("dealerType"));
            String companyCode = CommonUtils.checkNull(request.getParamValue("companyCode"));
            String companyName = CommonUtils.checkNull(request.getParamValue("companyName"));

            String dutyType = logonUser.getDutyType();

            if (Constant.DUTY_TYPE_COMPANY != Integer.parseInt(dutyType)) {
                orgId = logonUser.getOrgId().toString();
            }

            Map<String, String> map = new HashMap<String, String>();
            map.put("orgId", orgId);
            map.put("regionCode", regionCode);
            map.put("dealerType", dealerType);
            map.put("companyCode", companyCode);
            map.put("companyName", companyName);

            CompanyBase cb = new CompanyBase();

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;

            PageResult<Map<String, Object>> ps = cb.getCompanyByOtherQuery(map, Constant.PAGE_SIZE_MAX, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商公司");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    // 经销商个人信贷查询
    public void DealerCreditQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(DealerCreditQuery);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商结算暂停");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void DealerCreditInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
            String mortgageType = CommonUtils.checkNull(request.getParamValue("MORTGAGETYPE"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
            DealerInfoDao dao = DealerInfoDao.getInstance();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            // Constant.PAGE_SIZE, curPage
            PageResult<Map<String, Object>> ps = dao.DealerCreditInfo(dealerCode, dealerName, mortgageType, startDate, endDate, endDate, endDate, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
            act.setForword(DealerCreditQuery);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商个人信贷查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    public void addNewDealerCredit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            DealerInfoDao dao = DealerInfoDao.getInstance();
            List<Map<String, Object>> ls = dao.queryMortgageType();
            act.setOutData("ls", ls);
            act.setForword(addNewDealerCredit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商个人信贷新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addNewDealerCreditInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orgId = null;
            if (CommonUtils.isNullString(orgId)) {
                OrgBase ob = new OrgBase();

                orgId = ob.getOrgIdByDuty(logonUser);
            }

            DealerInfoDao dao = DealerInfoDao.getInstance();
            List<Map<String, Object>> ls = dao.queryMortgageType();
            act.setOutData("ls", ls);
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            // Constant.PAGE_SIZE, curPage
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
            PageResult<Map<String, Object>> ps = dao.addNewDealerCreditInfo(orgId, dealerCode, dealerName, curPage, Constant.PAGE_SIZE);
            List<Map<String, Object>> lis = ps.getRecords();

            if (!CommonUtils.isNullList(lis)) {
                StringBuffer theId = new StringBuffer("");
                int len = lis.size();
                for (int i = 0; i < len; i++) {
                    int strLen = theId.length();

                    if (strLen == 0) {
                        theId.append(lis.get(i).get("THE_ID"));

                    } else {
                        theId.append(",").append(lis.get(i).get("THE_ID"));

                    }
                }

                act.setOutData("theId", theId);
            }
            act.setOutData("ps", ps);
            act.setForword(addNewDealerCredit);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商个人信贷新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addNewDealerCreditDo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            System.out.println("asdsad22:" + CommonUtils.checkNull(request.getParamValue("startDate")));
            String date = CommonUtils.checkNull(request.getParamValue("startDate"));
            String dtlIds = CommonUtils.checkNull(request.getParamValue("dtlIds"));
            String[] ids = dtlIds.split(",");
            DealerInfoDao dao = DealerInfoDao.getInstance();
            String[] values = request.getParamValues("MORTGAGETYPE");
            SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 0; i < ids.length; i++) {
                // 删除个人信贷
                dao.deleteDealerCredit(ids[i]);
                // 新增个人信贷

                for (int j = 0; j < values.length; j++) {

                    TmDealerCreditPO tdc = new TmDealerCreditPO();
                    tdc.setDealerId(Long.valueOf(ids[i]));
                    tdc.setType(Long.valueOf(values[j]));
                    tdc.setCreateDate(new Date());

                    tdc.setCreditDate(fmat.parse(date));
                    tdc.setCreateBy(logonUser.getUserId());
                    dao.addDealerCredit(tdc);
                }
            }
            act.setOutData("flag", "success");

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商个人信贷新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void dealerCreditDownload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            ResponseWrapper response = act.getResponse();

            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
            String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
            String dlrCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
            String dlrName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String iType = CommonUtils.checkNull(request.getParamValue("MORTGAGETYPE"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));

            if (CommonUtils.isNullString(orgId)) {
                OrgBase ob = new OrgBase();

                orgId = ob.getOrgIdByDuty(logonUser);
            }

            Map<String, String> map = new HashMap<String, String>();
            map.put("orgId", orgId);
            map.put("regionId", regionId);
            map.put("dlrCode", dlrCode);
            map.put("dlrName", dlrName);
            map.put("iType", iType);
            map.put("startDate", startDate);
            map.put("endDate", endDate);

            DealerInfoDao dao = DealerInfoDao.getInstance();
            PageResult<Map<String, Object>> ps = dao.getIndividualTrust(map, 66666, 1);

            // 导出的文件名
            String fileName = "经销商个人信贷信息.xls";
            OutputStream os = null;
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            List<List<Object>> contentList = new LinkedList<List<Object>>();

            List<Object> rowList = new LinkedList<Object>();

            rowList.add("大区");
            rowList.add("省份");
            rowList.add("经销商代码");
            rowList.add("经销商简称");
            rowList.add("个人信贷业务类型");
            rowList.add("信贷日期");
            contentList.add(rowList);

            List<Map<String, Object>> dealerInfoList = ps.getRecords();

            if (!CommonUtils.isNullList(dealerInfoList)) {
                int len = dealerInfoList.size();

                for (int i = 0; i < len; i++) {
                    rowList = new LinkedList<Object>();

                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("ROOT_ORG_NAME")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("REGION_NAME")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_CODE")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("DEALER_SHORTNAME")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CODE_DESC")));
                    rowList.add(CommonUtils.checkNull(dealerInfoList.get(i).get("CREDIT_DATE")));

                    contentList.add(rowList);
                }
            }

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(contentList, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护下载结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void individualTrustQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = null;

        try {
            logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

            RequestWrapper request = act.getRequest();

            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
            String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
            String dlrCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
            String dlrName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String iType = CommonUtils.checkNull(request.getParamValue("MORTGAGETYPE"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));

            if (CommonUtils.isNullString(orgId)) {
                OrgBase ob = new OrgBase();

                orgId = ob.getOrgIdByDuty(logonUser);
            }

            DealerInfoDao dao = DealerInfoDao.getInstance();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

            Map<String, String> map = new HashMap<String, String>();
            map.put("orgId", orgId);
            map.put("regionId", regionId);
            map.put("dlrCode", dlrCode);
            map.put("dlrName", dlrName);
            map.put("iType", iType);
            map.put("startDate", startDate);
            map.put("endDate", endDate);

            PageResult<Map<String, Object>> ps = dao.getIndividualTrust(map, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商个人信贷查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商地址打印初始化
     */
    public void dealerAddressPrintInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        try {
            act.setForword(dealerAddressPrintInit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址打印初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商地址打印查询
     */
    public void dealerAddressQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        DealerInfoDao dao = DealerInfoDao.getInstance();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            map.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            map.put("ADDRESS_TYPE", CommonUtils.checkNull(request.getParamValue("ADDRESS_TYPE")));
            map.put("ADDRESS_TYPE_SH", CommonUtils.checkNull(request.getParamValue("ADDRESS_TYPE_SH")));

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryDealerAddressInit(map, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址打印初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商地址打印查询
     */
    public void queryDealerAddressPrint() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        DealerInfoDao dao = DealerInfoDao.getInstance();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("LINK_MAN", CommonUtils.checkNull(request.getParamValue("LINK_MAN")));
            map.put("TEL", CommonUtils.checkNull(request.getParamValue("TEL")));
            map.put("MOBILE_PHONE", CommonUtils.checkNull(request.getParamValue("MOBILE_PHONE")));
            map.put("ADDRESS_TYPE", CommonUtils.checkNull(request.getParamValue("ADDRESS_TYPE")));
            map.put("SEX", CommonUtils.checkNull(request.getParamValue("SEX")));
            map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryDealerAddressQuery(map, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址打印初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 经销商地址查询byid
     */
    public void queryDealerAddressById() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        DealerInfoDao dao = DealerInfoDao.getInstance();
        try {
            String id = CommonUtils.checkNull(request.getParamValue("ID"));
            List<Map<String, String>> list = dao.queryDealerAddressQueryById(id);
            if (list != null && list.size() == 1) {
                act.setOutData("map", list.get(0));
            }
            String type = request.getParamValue("type");
            if("newPrint".equals(type)){
                act.setForword(printPageNew);
            }else{
                act.setForword(printPage);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商地址打印初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-3
     * @Title :
     * @Description:替换订单审核导出Excel
     */
    public void exportPartCheckExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[12];
            head[0] = "经销商代码";
            head[1] = "经销商名称 ";
            head[2] = "经销商类型";
            head[3] = "所属公司";
            head[4] = "详细地址";
            head[5] = "地址类型";
            head[6] = "状态";
            head[7] = "联系人姓名";
            head[8] = "性别";
            head[9] = "手机";
            head[10] = "电话";
            DealerInfoDao dao = DealerInfoDao.getInstance();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            params.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            params.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            params.put("ADDRESS_TYPE", CommonUtils.checkNull(request.getParamValue("ADDRESS_TYPE")));
            List<Map<String, Object>> list = dao.queryDealerAddressInit(params);
            List list1 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[12];
                    detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                    detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                    detail[2] = CommonUtils.checkNull(map.get("DEALER_TYPE"));
                    detail[3] = CommonUtils.checkNull(map.get("COMPANY_NAME"));
                    detail[4] = CommonUtils.checkNull(map.get("ADDRESS"));
                    detail[5] = CommonUtils.checkNull(map.get("ADDRESS_TYPE"));
                    detail[6] = CommonUtils.checkNull(map.get("STATUS"));
                    detail[7] = CommonUtils.checkNull(map.get("LINK_MAN"));
                    detail[8] = CommonUtils.checkNull(map.get("SEX"));
                    detail[9] = CommonUtils.checkNull(map.get("MOBILE_PHONE"));
                    detail[10] = CommonUtils.checkNull(map.get("TEL"));
                    list1.add(detail);
                }
            }
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public static Object exportEx(ResponseWrapper response, RequestWrapper request, String[] head, List<String[]> list) throws Exception {

        String name = "经销商地址.xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
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
                    /* ws.addCell(new Label(i, z, str[i])); */// modify by yuan
                    if (CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])) {
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    } else {
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

    private List<Map<String, Object>> initTypeTopSelect() {
        CommonUtilDao dao = CommonUtilDao.getInstance();
        List<Map<String, Object>> list = dao.getCode(Constant.SH_ADDRESS_TYPE.toString());
        return list;
    }

    public void queryViConstructInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            map.put("parentOrgCode", CommonUtils.checkNull(request.getParamValue("parentOrgCode")));// 上级组织
            map.put("orgCode", CommonUtils.checkNull(request.getParamValue("orgCode")));// 大区
            map.put("regionCode", CommonUtils.checkNull(request.getParamValue("regionCode")));
            map.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));// 授权类型
            map.put("WORK_TYPE", CommonUtils.checkNull(request.getParamValue("WORK_TYPE")));// 经营类型
            map.put("IMAGE_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL")));// 形象等级
            map.put("IMAGE_COMFIRM_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL")));// 形象等级
            map.put("regionId", CommonUtils.checkNull(request.getParamValue("regionId")));

            map.put("SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("SCREATE_DATE")));// 开始时间
            map.put("ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("ECREATE_DATE")));// 结束时间

            map.put("AUTHORIZATION_SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_SCREATE_DATE")));// 授权开始时间
            map.put("AUTHORIZATION_ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_ECREATE_DATE")));// 授权结束时间

            map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
            map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            map.put("DEALER_LEVEL", CommonUtils.checkNull(request.getParamValue("DEALERLEVEL")));
            map.put("DEALER_STATUS", CommonUtils.checkNull(request.getParamValue("DEALERSTATUS")));
            map.put("ORGCODE", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
            map.put("sJDealerCode", CommonUtils.checkNull(request.getParamValue("sJDealerCode")));
            map.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            map.put("COMPANY_NAME", CommonUtils.checkNull(request.getParamValue("COMPANY_NAME")));
            map.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));// 经销商状态
            map.put("STATUS", CommonUtils.checkNull(request.getParamValue("status")));

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            // Constant.PAGE_SIZE, curPage
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().queryViConstructInfo(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
            // act.setForword(queryDealerInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "形象店支持查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryViConstructAudit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            map.put("parentOrgCode", CommonUtils.checkNull(request.getParamValue("parentOrgCode")));// 上级组织
            map.put("orgCode", CommonUtils.checkNull(request.getParamValue("orgCode")));// 大区
            map.put("regionCode", CommonUtils.checkNull(request.getParamValue("regionCode")));
            map.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));// 授权类型
            map.put("WORK_TYPE", CommonUtils.checkNull(request.getParamValue("WORK_TYPE")));// 经营类型
            map.put("IMAGE_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL")));// 形象等级
            map.put("IMAGE_COMFIRM_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL")));// 形象等级
            map.put("regionId", CommonUtils.checkNull(request.getParamValue("regionId")));

            map.put("SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("SCREATE_DATE")));// 开始时间
            map.put("ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("ECREATE_DATE")));// 结束时间

            map.put("AUTHORIZATION_SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_SCREATE_DATE")));// 授权开始时间
            map.put("AUTHORIZATION_ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_ECREATE_DATE")));// 授权结束时间

            map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
            map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            map.put("DEALER_LEVEL", CommonUtils.checkNull(request.getParamValue("DEALERLEVEL")));
            map.put("DEALER_STATUS", CommonUtils.checkNull(request.getParamValue("DEALERSTATUS")));
            map.put("ORGCODE", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
            map.put("sJDealerCode", CommonUtils.checkNull(request.getParamValue("sJDealerCode")));
            map.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            map.put("COMPANY_NAME", CommonUtils.checkNull(request.getParamValue("COMPANY_NAME")));
            map.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));// 经销商状态
            map.put("STATUS", CommonUtils.checkNull(request.getParamValue("status")));
            map.put("user_id", CommonUtils.checkNull(request.getParamValue("user_id")));
            map.put("auditYear", CommonUtils.checkNull(request.getParamValue("auditYear")));

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().queryViConstructAudit(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "形象店支持查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void viConstructInfoQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();
            map.put("user_id", CommonUtils.checkNull(request.getParamValue("user_id")));
            map.put("dealer_id", CommonUtils.checkNull(request.getParamValue("dealer_id")));
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().viConstructInfoQuery1(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "形象店支持维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryViConstructInfoById() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String cmd = CommonUtils.checkNull(request.getParamValue("cmd"));
            String suvOrMpv = CommonUtils.checkNull(request.getParamValue("suvOrMpv"));
            String dealer_id = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
            String isDivShow = CommonUtils.checkNull(request.getParamValue("isDivShow"));
            TmDealerPO dealerPO = new TmDealerPO();
            dealerPO.setDealerId(Long.parseLong(dealer_id));
            String mainId = "";
            // List attachList;
            List<Map<String, Object>> detailLst = new ArrayList<Map<String, Object>>();
            if (cmd.equals("2")) {
                detailLst = DealerInfoDao.getInstance().getConstructInfo(dealer_id, "");
            } else {
                detailLst = DealerInfoDao.getInstance().getConstructInfo(dealer_id, suvOrMpv);
            }
            if (detailLst.size() > 0) {
                Object id = detailLst.get(0).get("ID");
                mainId = id.toString();
            }

            // 提车支持款取得
            String sql = "select p.policy_name ,to_char(p.deploy_id)||'_'||to_char(p.dismoney) as deploy_id  from tt_sales_policy_deploy p where p.deploy_status = 1   and trunc(sysdate) >= trunc(start_date) and trunc(sysdate) <= trunc(stop_date)";
            List<Map<String, Object>> amountList = DealerInfoDao.getInstance().pageQuery(sql, null, DealerInfoDao.getInstance().getFunName());
            act.setOutData("amountList", amountList);

            act.setOutData("dealerInfo", (TmDealerPO) DealerInfoDao.getInstance().select(dealerPO).get(0));
            if (detailLst != null && detailLst.size() > 0) {
                act.setOutData("dMap_main", detailLst.get(0));
            }
            if (!StringUtil.isNull(mainId)) {
                sql = "SELECT F.FJID,F.FILEID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ =" + mainId + "ORDER BY F.FJID";
                List<Map<String, Object>> attachList = DealerInfoDao.getInstance().pageQuery(sql, null, DealerInfoDao.getInstance().getFunName());
                if (attachList != null && attachList.size() > 0) {
                    act.setOutData("attachList", attachList);
                    act.setOutData("attachListSize", attachList.size());
                }
            }

            act.setOutData("size", detailLst.size());
            act.setOutData("dMap_detail", detailLst);

            if (cmd.equals("2")) {
                if (StringUtils.isEmpty(isDivShow)) {
                    act.setForword(checkViConstruct);
                } else {
                    act.setForword(checkViConstructDivShow);
                }
            } else if (cmd.equals("1")) {
                if ("2014032694231206".equals(suvOrMpv)) {
                    act.setForword(updateViConstruct);
                } else if ("2015011508783002".equals(suvOrMpv)) {
                    act.setForword(updateViConstructMpvUrl);
                }
            } else {
                if ("2014032694231206".equals(suvOrMpv)) {
                    act.setForword(addViConstructUrl);
                } else if ("2015011508783002".equals(suvOrMpv)) {
                    act.setForword(addViConstructMpvUrl);
                }
            }

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增建店支持");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void queryViConstructInfoByIdDLR() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dealer_id = logonUser.getDealerId();
            TmDealerPO dealerPO = new TmDealerPO();
            dealerPO.setDealerId(Long.parseLong(dealer_id));
            String mainId = "";
            // List attachList;
            List<Map<String, Object>> detailLst = new ArrayList<Map<String, Object>>();
            detailLst = DealerInfoDao.getInstance().getConstructInfo(dealer_id, "");
            if (detailLst.size() > 0) {
                Object id = detailLst.get(0).get("ID");
                mainId = id.toString();
            }

            // 提车支持款取得
            String sql = "select p.policy_name ,to_char(p.deploy_id)||'_'||to_char(p.dismoney) as deploy_id  from tt_sales_policy_deploy p where p.deploy_status = 1   and trunc(sysdate) >= trunc(start_date) and trunc(sysdate) <= trunc(stop_date)";
            List<Map<String, Object>> amountList = DealerInfoDao.getInstance().pageQuery(sql, null, DealerInfoDao.getInstance().getFunName());
            act.setOutData("amountList", amountList);

            act.setOutData("dealerInfo", (TmDealerPO) DealerInfoDao.getInstance().select(dealerPO).get(0));
            if (detailLst != null && detailLst.size() > 0) {
                act.setOutData("dMap_main", detailLst.get(0));
            }
            if (!StringUtil.isNull(mainId)) {
                sql = "SELECT F.FJID,F.FILEID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ =" + mainId + "ORDER BY F.FJID";
                List<Map<String, Object>> attachList = DealerInfoDao.getInstance().pageQuery(sql, null, DealerInfoDao.getInstance().getFunName());
                if (attachList != null && attachList.size() > 0) {
                    act.setOutData("attachList", attachList);
                    act.setOutData("attachListSize", attachList.size());
                }
            }

            act.setOutData("size", detailLst.size());
            act.setOutData("dMap_detail", detailLst);
            act.setForword(checkViConstructDLR);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "形象店");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    public void queryViConstructDealer() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();

            String dealer_id = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
            String user_id = CommonUtils.checkNull(request.getParamValue("user_id"));
            act.setOutData("dealer_id", dealer_id);
            act.setOutData("user_id", user_id);
            act.setForword(viConstructAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增建店支持");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void addVi() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String[] STATUS = request.getParamValues("STATUS");
            String idStr = request.getParamValue("ID");
            Long ID;
            if (idStr == null || idStr == "") {
                ID = new Long(SequenceManager.getSequence(""));
            } else {
                ID = Long.parseLong(idStr);
            }

            String[] year = request.getParamValues("year");
            // String remark =
            // CommonUtils.checkNull(request.getParamValue("textarea"));
            String DEALER_ID = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
            String CONSTRUCT_EDATE = CommonUtils.checkNull(request.getParamValue("CONSTRUCT_EDATE"));
            String CONSTRUCT_SDATE = CommonUtils.checkNull(request.getParamValue("CONSTRUCT_SDATE"));
            String ACCEPT_DATE = CommonUtils.checkNull(request.getParamValue("ACCEPT_DATE"));
            String[] amount = request.getParamValues("AMOUNT");
            String[] yearRent = request.getParamValues("YEAR_RENT");
            String[] supportEdate = request.getParamValues("SUPPORT_EDATE");
            String[] supportSdate = request.getParamValues("SUPPORT_SDATE");
            String[] scale = request.getParamValues("SCALE");
            String[] remark = request.getParamValues("REMARK");

            String checkedDate = CommonUtils.checkNull(request.getParamValue("VI_CONFIRM_DATE"));
            String checkedImageLevel = CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL"));
            String imageLevel = CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL"));
            String PAGE_TYPE = CommonUtils.checkNull(request.getParamValue("PAGE_TYPE"));
            String VI_CONSTRUCT_TYPE = CommonUtils.checkNull(request.getParamValue("VI_CONSTRUCT_TYPE"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            if (PAGE_TYPE.equals("1")) {

                // 先删除旧数据
                // TtViConstructMainPO oldPo=new TtViConstructMainPO();
                // oldPo.setDealerId(Long.parseLong(DEALER_ID));
                // DealerInfoDao.getInstance().delete(oldPo);

                TtViConstructMainPO po = new TtViConstructMainPO();

                // 插入主表
                po.setDealerId(Long.parseLong(DEALER_ID));

                po.setId(new Long(SequenceManager.getSequence("")));
                po.setAcceptDate(sdf.parse(ACCEPT_DATE));
                po.setCreateBy(logonUser.getUserId());
                po.setCreateDate(date);
                po.setDealerId(Long.parseLong(DEALER_ID));
                po.setConstructSdate(sdf.parse(CONSTRUCT_SDATE));
                po.setConstructEdate(sdf.parse(CONSTRUCT_EDATE));
                po.setCheckedDate(sdf.parse(checkedDate));
                po.setCheckedImageLevel(Integer.parseInt(checkedImageLevel));
                po.setImageLevel(Integer.parseInt(imageLevel));
                po.setCheckedType(Integer.parseInt(VI_CONSTRUCT_TYPE));

                // 保存或修改主表
                TtViConstructMainPO oldPo = new TtViConstructMainPO();
                oldPo.setDealerId(Long.parseLong(DEALER_ID));
                List mainPos = DealerInfoDao.getInstance().select(oldPo);
                if (mainPos != null && mainPos.size() > 0) {
                    TtViConstructMainPO oldPo1 = (TtViConstructMainPO) mainPos.get(0);
                    po.setId(oldPo1.getId());
                    DealerInfoDao.getInstance().update(oldPo, po);
                } else {
                    DealerInfoDao.getInstance().insert(po);
                }

                // DealerInfoDao.getInstance().insert(po);

                // 插入明细表
                TtViConstructDetailPO detailPO = new TtViConstructDetailPO();
                detailPO.setDealerId(Long.parseLong(DEALER_ID));
                detailPO.setId(po.getId());
                detailPO.setCreateBy(logonUser.getUserId());
                detailPO.setCreateDate(date);

                TtViConstructAuditPO auditPO = new TtViConstructAuditPO();
                auditPO.setDealerId(Long.parseLong(DEALER_ID));
                for (int i = 0; i < amount.length; i++) {

                    detailPO.setAmount(new Double(amount[i].split("_")[1]));
                    detailPO.setDeployId(new Long(amount[i].split("_")[0]));
                    detailPO.setDetailId(new Long(SequenceManager.getSequence("")));
                    detailPO.setScale(Integer.parseInt(scale[i]));
                    detailPO.setSupportSdate(sdf.parse(supportSdate[i]));
                    detailPO.setSupportEdate(sdf.parse(supportEdate[i]));
                    detailPO.setYearRent(Double.parseDouble(yearRent[i]));
                    detailPO.setYearFlag(i + 1);
                    detailPO.setRemark(remark[i]);

                    // 先删除明细表
                    TtViConstructDetailPO oldDetailPO = new TtViConstructDetailPO();
                    oldDetailPO.setDealerId(Long.parseLong(DEALER_ID));
                    oldDetailPO.setYearFlag(i + 1);
                    List detailPos = DealerInfoDao.getInstance().select(oldDetailPO);
                    if (detailPos != null && detailPos.size() > 0) {
                        // oldDetailPO= (TtViConstructDetailPO)
                        // detailPos.get(0);
                        // Integer status=oldDetailPO.getStatus();
                        // if(status==92861000||status==92861005){
                        DealerInfoDao.getInstance().update(oldDetailPO, detailPO);
                        // }
                    } else {
                        DealerInfoDao.getInstance().insert(detailPO);
                    }

                    // DealerInfoDao.getInstance().insert(detailPO);

                    // 插入审核表
                    auditPO = new TtViConstructAuditPO();

                    auditPO.setDealerId(Long.parseLong(DEALER_ID));
                    auditPO.setId(Long.valueOf(detailPO.getDetailId()));
                    auditPO.setCreateBy(logonUser.getUserId());
                    auditPO.setCreateDate(new Date());
                    auditPO.setStatus(Constant.VI_CONSTRUCT_STATUS_02);
                    auditPO.setUserId(Constant.VI_CONSTRUCT_AUDIT_02);
                    // auditPO.setRemark(remark);
                    auditPO.setYearFlag((String.valueOf(i + 1)));
                    auditPO.setDetailId(new Long(SequenceManager.getSequence("")));

                    // 先删除明细表
                    // oldAduitPO.setDealerId(Long.parseLong(DEALER_ID));
                    // oldAduitPO.setYearFlag((i+1)+"");
                    // List
                    // auditPos=DealerInfoDao.getInstance().select(oldAduitPO);

                    DealerInfoDao.getInstance().insert(auditPO);

                    TtViConstructDetailPO detailPO1 = new TtViConstructDetailPO();
                    TtViConstructDetailPO detailPO3 = new TtViConstructDetailPO();
                    detailPO1.setDealerId(auditPO.getDealerId());
                    detailPO1.setYearFlag(new Integer(auditPO.getYearFlag()));

                    detailPO3.setStatus(Constant.VI_CONSTRUCT_STATUS_02);
                    detailPO3.setUserId(auditPO.getUserId());
                    DealerInfoDao.getInstance().update(detailPO1, detailPO3);
                }
            } else {
                TtViConstructDetailPO detailPO = new TtViConstructDetailPO();
                detailPO.setDealerId(Long.parseLong(DEALER_ID));
                detailPO.setId(ID);
                detailPO.setCreateBy(logonUser.getUserId());
                detailPO.setCreateDate(date);
                TtViConstructAuditPO auditPO = new TtViConstructAuditPO();
                auditPO.setDealerId(Long.parseLong(DEALER_ID));
                for (int i = 0; i < amount.length; i++) {
                    // if(STATUS[i].equals("92861002")||STATUS[i].equals("92861003")||STATUS[i].equals("92861004")){
                    // continue;
                    // }
                    detailPO.setAmount(new Double(amount[i].split("_")[1]));
                    detailPO.setDeployId(new Long(amount[i].split("_")[0]));
                    detailPO.setDetailId(new Long(SequenceManager.getSequence("")));
                    detailPO.setScale(Integer.parseInt(scale[i]));
                    detailPO.setSupportSdate(sdf.parse(supportSdate[i]));
                    detailPO.setSupportEdate(sdf.parse(supportEdate[i]));
                    detailPO.setYearRent(Double.parseDouble(yearRent[i]));
                    detailPO.setYearFlag(i + 1);

                    // 插入明细表
                    // 先删除明细表
                    TtViConstructDetailPO oldDetailPO = new TtViConstructDetailPO();
                    oldDetailPO.setDealerId(Long.parseLong(DEALER_ID));
                    oldDetailPO.setYearFlag(i + 1);
                    List detailPos = DealerInfoDao.getInstance().select(oldDetailPO);
                    if (detailPos != null && detailPos.size() > 0) {
                        // oldDetailPO= (TtViConstructDetailPO)
                        // detailPos.get(0);
                        // Integer status=oldDetailPO.getStatus();
                        // if(status==92861000||status==92861005){
                        DealerInfoDao.getInstance().update(oldDetailPO, detailPO);
                        // }
                    } else {
                        DealerInfoDao.getInstance().insert(detailPO);
                    }

                    // DealerInfoDao.getInstance().insert(detailPO);
                    auditPO = new TtViConstructAuditPO();
                    auditPO.setDealerId(Long.parseLong(DEALER_ID));
                    // 插入审核表
                    auditPO.setId(Long.valueOf(detailPO.getDetailId()));
                    auditPO.setDealerId(Long.parseLong(DEALER_ID));
                    auditPO.setCreateBy(logonUser.getUserId());
                    auditPO.setCreateDate(new Date());
                    auditPO.setStatus(Constant.VI_CONSTRUCT_STATUS_02);
                    auditPO.setUserId(Constant.VI_CONSTRUCT_AUDIT_02);
                    // auditPO.setRemark(remark);
                    auditPO.setDetailId(new Long(SequenceManager.getSequence("")));
                    auditPO.setYearFlag((String.valueOf(i + 1)));

                    // 查询校验表
                    // oldAuditPO.setDealerId(Long.parseLong(DEALER_ID));
                    // oldAuditPO.setYearFlag(i+1+"");
                    // List
                    // auditPos=DealerInfoDao.getInstance().select(oldAuditPO);
                    // if(auditPos!=null&&auditPos.size()>0){
                    // Integer status=oldAPO.getStatus();
                    // if(status==92861000||status==92861005){
                    // DealerInfoDao.getInstance().update(oldAuditPO,auditPO);
                    // }
                    // }else{
                    DealerInfoDao.getInstance().insert(auditPO);

                    TtViConstructDetailPO detailPO1 = new TtViConstructDetailPO();
                    TtViConstructDetailPO detailPO3 = new TtViConstructDetailPO();
                    detailPO1.setDealerId(auditPO.getDealerId());
                    detailPO1.setYearFlag(new Integer(auditPO.getYearFlag()));

                    detailPO3.setStatus(Constant.VI_CONSTRUCT_STATUS_02);
                    detailPO3.setUserId(auditPO.getUserId());
                    DealerInfoDao.getInstance().update(detailPO1, detailPO3);
                    // }
                    // DealerInfoDao.getInstance().insert(auditPO);
                }
            }
            act.setForword(viConstructQry);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void saveVi() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            RequestWrapper request = act.getRequest();
            String[] remark = request.getParamValues("REMARK");
            String[] detailId = request.getParamValues("DETAIL_ID");
            String DEALER_ID = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
            String CONSTRUCT_EDATE = CommonUtils.checkNull(request.getParamValue("CONSTRUCT_EDATE"));
            String CONSTRUCT_SDATE = CommonUtils.checkNull(request.getParamValue("CONSTRUCT_SDATE"));
            String ACCEPT_DATE = CommonUtils.checkNull(request.getParamValue("ACCEPT_DATE"));
            String[] amount = request.getParamValues("AMOUNT");
            String[] yearRent = request.getParamValues("YEAR_RENT");
            String[] supportEdate = request.getParamValues("SUPPORT_EDATE");
            String[] supportSdate = request.getParamValues("SUPPORT_SDATE");
            String[] scale = request.getParamValues("SCALE");
            String[] yearStatus = request.getParamValues("yearStatus");
            String[] SUPPORT_NUMBER = request.getParamValues("SUPPORT_NUMBER");
            String[] fjids = request.getParamValues("fjid");
            String checkedDate = CommonUtils.checkNull(request.getParamValue("VI_CONFIRM_DATE"));
            String checkedImageLevel = CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL"));
            String imageLevel = CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL"));
            String VI_CONSTRUCT_TYPE = CommonUtils.checkNull(request.getParamValue("VI_CONSTRUCT_TYPE"));
            String offline_rebate = CommonUtils.checkNull(request.getParamValue("offline_rebate"));
            String vehicle_series_flag1 = CommonUtils.checkNull(request.getParamValue("vehicle_series_flag1"));
            // 判断是保存 还是上报 1:保存 2：上报
            String buttonType = CommonUtils.checkNull(request.getParamValue("BUTTON_TYPE"));
            String ID1 = CommonUtils.checkNull(request.getParamValue("ID1"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String deleteFjids = CommonUtils.checkNull(request.getParamValue("list"));
            // 插入主表
            TtViConstructMainPO po = new TtViConstructMainPO();
            po.setVehicleSeriesId(Long.parseLong(vehicle_series_flag1));
            po.setDealerId(Long.parseLong(DEALER_ID));
            if (deleteFjids != null && !deleteFjids.equals("")) {
                ajaxDao.delete("DELETE Fs_Fileupload WHERE fjid IN(" + deleteFjids + ")", null);
                po.setId(Long.parseLong(ID1));
            } else {
                po.setId(new Long(SequenceManager.getSequence("")));
            }

            if (!StringUtil.isNull(ACCEPT_DATE)) {
                po.setAcceptDate(sdf.parse(ACCEPT_DATE));
            }

            po.setCreateBy(logonUser.getUserId());
            po.setCreateDate(date);
            po.setDealerId(Long.parseLong(DEALER_ID));
            if (!StringUtil.isNull(CONSTRUCT_SDATE)) {
                po.setConstructSdate(sdf.parse(CONSTRUCT_SDATE));
            }
            if (!StringUtil.isNull(CONSTRUCT_EDATE)) {
                po.setConstructEdate(sdf.parse(CONSTRUCT_EDATE));
            }
            if (!StringUtil.isNull(checkedDate)) {
                po.setCheckedDate(sdf.parse(checkedDate));
            }

            if (!StringUtil.isNull(checkedImageLevel)) {
                po.setCheckedImageLevel(Integer.parseInt(checkedImageLevel));
            }

            if (!StringUtil.isNull(imageLevel)) {
                po.setImageLevel(Integer.parseInt(imageLevel));
            }
            if (!StringUtil.isNull(VI_CONSTRUCT_TYPE)) {
                po.setCheckedType(Integer.parseInt(VI_CONSTRUCT_TYPE));
            }
            if (!StringUtil.isNull(offline_rebate)) {
                po.setOfflineRebate(Double.parseDouble(offline_rebate));
            }
            // 保存或修改主表
            TtViConstructMainPO oldPo = new TtViConstructMainPO();
            oldPo.setDealerId(Long.parseLong(DEALER_ID));
            oldPo.setVehicleSeriesId(Long.parseLong(vehicle_series_flag1));
            List mainPos = DealerInfoDao.getInstance().select(oldPo);
            if (mainPos != null && mainPos.size() > 0) {
                TtViConstructMainPO oldPo1 = (TtViConstructMainPO) mainPos.get(0);
                po.setId(oldPo1.getId());
                DealerInfoDao.getInstance().update(oldPo, po);
            } else {
                DealerInfoDao.getInstance().insert(po);
            }

            // 插入明细表
            TtViConstructDetailPO detailPO = new TtViConstructDetailPO();
            detailPO.setDealerId(Long.parseLong(DEALER_ID));
            detailPO.setCreateBy(logonUser.getUserId());
            detailPO.setDealerId(Long.parseLong(DEALER_ID));
            detailPO.setCreateDate(date);

            // 审核表
            TtViConstructAuditPO auditPO = new TtViConstructAuditPO();
            auditPO.setId(new Long(SequenceManager.getSequence("")));

            if (yearStatus != null && yearStatus.length > 0) {
                for (int i = 0; i < yearStatus.length; i++) {

                    if (!StringUtil.isNull(amount[i])) {
                        if (!StringUtil.isNull(amount[i].split("_")[1])) {
                            detailPO.setAmount(Double.parseDouble(amount[i].split("_")[1]));
                            detailPO.setDeployId(Long.parseLong(amount[i].split("_")[0]));
                        }
                    }
                    detailPO.setRemark(remark[i]);
                    if (!StringUtil.isNull(scale[i])) {
                        detailPO.setScale(Integer.parseInt(scale[i]));
                    }
                    if (!StringUtil.isNull(supportSdate[i])) {
                        detailPO.setSupportSdate(sdf.parse(supportSdate[i]));
                    }
                    if (!StringUtil.isNull(supportEdate[i])) {
                        detailPO.setSupportEdate(sdf.parse(supportEdate[i]));
                    }

                    if (!StringUtil.isNull(supportEdate[i])) {
                        detailPO.setSupportEdate(sdf.parse(supportEdate[i]));
                    }
                    if (!StringUtil.isNull(yearRent[i])) {
                        detailPO.setYearRent(Double.parseDouble(yearRent[i]));
                    }
                    detailPO.setId(po.getId());
                    detailPO.setYearFlag(Integer.parseInt(yearStatus[i]));
                    if (!yearStatus[i].equals("1")) {
                        // 支持数CHECK
                        int supportNumber = StringUtils.isEmpty(SUPPORT_NUMBER[i-1]) ? 0 : Integer.parseInt(SUPPORT_NUMBER[i-1]);
                        BigDecimal yearRentBigDecial = new BigDecimal(yearRent[i]);
                        BigDecimal maxSupportAmount = yearRentBigDecial.subtract(yearRentBigDecial.multiply(new BigDecimal(scale[i]).divide(new BigDecimal(100))));
                        int maxSupportNumber = maxSupportAmount.divide(new BigDecimal(Double.parseDouble(amount[i].split("_")[1]))).setScale(BigDecimal.ROUND_DOWN).intValue();
                        if (maxSupportNumber <= supportNumber) {
                            throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "已支持台数超过最大支持数！");
                        } 
                        detailPO.setSupportNumber(supportNumber);
                    }
                    
                    if (buttonType.equals("1")) {
                        auditPO.setStatus(Constant.VI_CONSTRUCT_STATUS_00);

                        detailPO.setStatus(Constant.VI_CONSTRUCT_STATUS_00);
                        detailPO.setUserId(auditPO.getUserId());
                    } else {
                        auditPO.setStatus(Constant.VI_CONSTRUCT_STATUS_01);

                        detailPO.setStatus(Constant.VI_CONSTRUCT_STATUS_01);
                        detailPO.setUserId(auditPO.getUserId());
                    }
                    
                    // 明细表更新或新增
                    if (!StringUtils.isEmpty(detailId[i])) {
                        TtViConstructDetailPO oldDetailPO = new TtViConstructDetailPO();
                        oldDetailPO.setDetailId(Long.parseLong(detailId[i]));
                        detailPO.setDetailId(Long.parseLong(detailId[i]));
                        DealerInfoDao.getInstance().update(oldDetailPO, detailPO);
                    } else {
                        detailPO.setDetailId(new Long(SequenceManager.getSequence("")));
                        DealerInfoDao.getInstance().insert(detailPO);
                    }

                    auditPO.setDetailId(detailPO.getDetailId());
                    auditPO.setUserId(Constant.VI_CONSTRUCT_AUDIT_02);
                    auditPO.setDealerId(Long.parseLong(DEALER_ID));
                    auditPO.setCreateBy(logonUser.getUserId());
                    auditPO.setCreateDate(new Date());
                    auditPO.setYearFlag((String.valueOf(yearStatus[i])));
                    DealerInfoDao.getInstance().insert(auditPO);
                }
            }

            FileUploadManager.fileUploadByBusiness(String.valueOf(po.getId()), fjids, logonUser);

            act.setForword(viConstructQry);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "保存");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void singleSaveVi() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            Date date = new Date();
            RequestWrapper request = act.getRequest();
            String remark = request.getParamValue("REMARK1");
            String DEALER_ID = CommonUtils.checkNull(request.getParamValue("DEALER_ID1"));
            String CONSTRUCT_EDATE = CommonUtils.checkNull(request.getParamValue("CONSTRUCT_EDATE1"));
            String CONSTRUCT_SDATE = CommonUtils.checkNull(request.getParamValue("CONSTRUCT_SDATE1"));
            String ACCEPT_DATE = CommonUtils.checkNull(request.getParamValue("ACCEPT_DATE1"));
            String amount = request.getParamValue("AMOUNT1");
            String yearRent = request.getParamValue("YEAR_RENT1");
            String supportEdate = request.getParamValue("SUPPORT_EDATE1");
            String supportSdate = request.getParamValue("SUPPORT_SDATE1");
            String scale = request.getParamValue("SCALE1");
            String year = request.getParamValue("SIZE");
            String idStr = request.getParamValue("ID1");
            String SUPPORT_NUMBER = CommonUtils.checkNull(request.getParamValue("SUPPORT_NUMBER"));
            String buttonType = CommonUtils.checkNull(request.getParamValue("BUTTON_TYPE"));
            String DETAIL_ID = CommonUtils.checkNull(request.getParamValue("DETAIL_ID"));
            String fjids = CommonUtils.checkNull(request.getParamValue("fjid1"));

            long id;
            if (idStr == null || idStr == "") {
                id = new Long(SequenceManager.getSequence(""));
            } else {
                id = Long.parseLong(idStr);
            }

            String checkedDate = CommonUtils.checkNull(request.getParamValue("VI_CONFIRM_DATE1"));
            String checkedImageLevel = CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL1"));
            String imageLevel = CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL1"));
            String VI_CONSTRUCT_TYPE = CommonUtils.checkNull(request.getParamValue("VI_CONSTRUCT_TYPE1"));
            String offline_rebate1 = CommonUtils.checkNull(request.getParamValue("offline_rebate1"));
            String vehicle_series_flag = CommonUtils.checkNull(request.getParamValue("vehicle_series_flag"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            TtViConstructMainPO po = new TtViConstructMainPO();
            TtViConstructDetailPO detailPO = new TtViConstructDetailPO();

            // 插入主表
            if (year.equals("1")) {

                po.setDealerId(Long.parseLong(DEALER_ID));
                po.setId(id);
                if (!StringUtil.isNull(ACCEPT_DATE)) {
                    po.setAcceptDate(sdf.parse(ACCEPT_DATE));
                }

                po.setVehicleSeriesId(Long.parseLong(vehicle_series_flag));
                po.setCreateBy(logonUser.getUserId());
                po.setCreateDate(date);
                po.setDealerId(Long.parseLong(DEALER_ID));
                if (!StringUtil.isNull(CONSTRUCT_SDATE)) {
                    po.setConstructSdate(sdf.parse(CONSTRUCT_SDATE));
                }
                if (!StringUtil.isNull(CONSTRUCT_EDATE)) {
                    po.setConstructEdate(sdf.parse(CONSTRUCT_EDATE));
                }
                if (!StringUtil.isNull(checkedDate)) {
                    po.setCheckedDate(sdf.parse(checkedDate));
                }

                if (!StringUtil.isNull(checkedImageLevel)) {
                    po.setCheckedImageLevel(Integer.parseInt(checkedImageLevel));
                }

                if (!StringUtil.isNull(imageLevel)) {
                    po.setImageLevel(Integer.parseInt(imageLevel));
                }
                if (!StringUtil.isNull(VI_CONSTRUCT_TYPE)) {
                    po.setCheckedType(Integer.parseInt(VI_CONSTRUCT_TYPE));
                }
                if (!StringUtil.isNull(offline_rebate1)) {
                    po.setOfflineRebate(Double.parseDouble(offline_rebate1));
                }

                TtViConstructMainPO oldMain = new TtViConstructMainPO();
                oldMain.setDealerId(Long.parseLong(DEALER_ID));
                oldMain.setVehicleSeriesId(Long.parseLong(vehicle_series_flag));
                List mainPos = DealerInfoDao.getInstance().select(oldMain);
                if (mainPos != null && mainPos.size() > 0) {
                    TtViConstructMainPO oldMPO = (TtViConstructMainPO) mainPos.get(0);
                    oldMain.setId(oldMPO.getId());
                    po.setId(oldMPO.getId());
                    DealerInfoDao.getInstance().update(oldMPO, po);
                } else {
                    DealerInfoDao.getInstance().insert(po);
                }

                // 插入明细表
                detailPO = new TtViConstructDetailPO();

                detailPO.setDealerId(Long.parseLong(DEALER_ID));
                detailPO.setYearFlag(Integer.parseInt(year));
                detailPO.setCreateBy(logonUser.getUserId());
                detailPO.setCreateDate(date);

                TtViConstructAuditPO auditPO = new TtViConstructAuditPO();

                if (!StringUtil.isNull(amount)) {
                    if (!StringUtil.isNull(amount.split("_")[1])) {
                        detailPO.setAmount(Double.parseDouble(amount.split("_")[1]));
                        detailPO.setDeployId(Long.parseLong(amount.split("_")[0]));
                    }
                }
                detailPO.setRemark(remark);
                if (!StringUtil.isNull(scale)) {
                    detailPO.setScale(Integer.parseInt(scale));
                }
                if (!StringUtil.isNull(supportSdate)) {
                    detailPO.setSupportSdate(sdf.parse(supportSdate));
                }
                if (!StringUtil.isNull(supportEdate)) {
                    detailPO.setSupportEdate(sdf.parse(supportEdate));
                }

                if (!StringUtil.isNull(supportEdate)) {
                    detailPO.setSupportEdate(sdf.parse(supportEdate));
                }
                if (!StringUtil.isNull(yearRent)) {
                    detailPO.setYearRent(Double.parseDouble(yearRent));
                }
                detailPO.setId(po.getId());

                detailPO.setYearFlag(Integer.parseInt(year));
                if (!year.equals("1")) {
                    // 支持数CHECK
                    int supportNumber = StringUtils.isEmpty(SUPPORT_NUMBER) ? 0 : Integer.parseInt(SUPPORT_NUMBER);
                    BigDecimal yearRentBigDecial = new BigDecimal(yearRent);
                    BigDecimal maxSupportAmount = yearRentBigDecial.subtract(yearRentBigDecial.multiply(new BigDecimal(scale).divide(new BigDecimal(100))));
                    int maxSupportNumber = maxSupportAmount.divide(new BigDecimal(Double.parseDouble(amount.split("_")[1]))).setScale(BigDecimal.ROUND_DOWN).intValue();
                    if (maxSupportNumber <= supportNumber) {
                        throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "已支持台数超过最大支持数！");
                    } 
                    detailPO.setSupportNumber(supportNumber);
                }
                if (buttonType.equals("1")) {
                    auditPO.setStatus(Constant.VI_CONSTRUCT_STATUS_00);

                    detailPO.setStatus(Constant.VI_CONSTRUCT_STATUS_00);
                    detailPO.setUserId(auditPO.getUserId());
                } else {
                    auditPO.setStatus(Constant.VI_CONSTRUCT_STATUS_01);

                    detailPO.setStatus(Constant.VI_CONSTRUCT_STATUS_01);
                    detailPO.setUserId(auditPO.getUserId());
                }
                // 明细表更新或者新增
                TtViConstructDetailPO oldDetailPO = new TtViConstructDetailPO();
                if (!StringUtils.isEmpty(DETAIL_ID)) {
                    oldDetailPO.setDetailId(Long.parseLong(DETAIL_ID));
                    detailPO.setDetailId(Long.parseLong(DETAIL_ID));
                    DealerInfoDao.getInstance().update(oldDetailPO, detailPO);
                } else {
                    detailPO.setDetailId(new Long(SequenceManager.getSequence("")));
                    DETAIL_ID = detailPO.getDetailId().toString();
                    DealerInfoDao.getInstance().insert(detailPO);
                }

                auditPO.setDetailId(detailPO.getDetailId());
                auditPO.setUserId(Constant.VI_CONSTRUCT_AUDIT_01);
                Long auditDetailId = new Long(SequenceManager.getSequence(""));
                auditPO.setId(auditDetailId);
                auditPO.setDealerId(Long.parseLong(DEALER_ID));
                auditPO.setCreateBy(logonUser.getUserId());
                auditPO.setCreateDate(new Date());
                auditPO.setYearFlag(year);

                if(!fjids.equals("")){
                    FileUploadManager.fileUploadByBusiness(String.valueOf(po.getId()), fjids.split(","), logonUser);
                }
                DealerInfoDao.getInstance().insert(auditPO);
                
            } else {

                // 插入明细表
                if(!fjids.equals("")){
                    FileUploadManager.fileUploadByBusiness(idStr, fjids.split(","), logonUser);
                }
                detailPO.setDealerId(Long.parseLong(DEALER_ID));
                detailPO.setYearFlag(Integer.parseInt(year));
                if (!year.equals("1")) {
                    // 支持数CHECK
                    int supportNumber = StringUtils.isEmpty(SUPPORT_NUMBER) ? 0 : Integer.parseInt(SUPPORT_NUMBER);
                    BigDecimal yearRentBigDecial = new BigDecimal(yearRent);
                    BigDecimal maxSupportAmount = yearRentBigDecial.subtract(yearRentBigDecial.multiply(new BigDecimal(scale).divide(new BigDecimal(100))));
                    int maxSupportNumber = maxSupportAmount.divide(new BigDecimal(Double.parseDouble(amount.split("_")[1]))).intValue();
                    if (maxSupportNumber <= supportNumber) {
                        throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "已支持台数超过最大支持数！");
                    } 
                    detailPO.setSupportNumber(supportNumber);
                }
                detailPO.setCreateBy(logonUser.getUserId());
                detailPO.setCreateDate(date);

                TtViConstructAuditPO auditPO = new TtViConstructAuditPO();

                auditPO.setId(new Long(SequenceManager.getSequence("")));
                auditPO.setDealerId(Long.parseLong(DEALER_ID));
                if (!StringUtil.isNull(amount)) {
                    if (!StringUtil.isNull(amount.split("_")[1])) {
                        detailPO.setAmount(Double.parseDouble(amount.split("_")[1]));
                        detailPO.setDeployId(Long.parseLong(amount.split("_")[0]));
                    }
                }
                detailPO.setRemark(remark);
                if (!StringUtil.isNull(scale)) {
                    detailPO.setScale(Integer.parseInt(scale));
                }
                if (!StringUtil.isNull(supportSdate)) {
                    detailPO.setSupportSdate(sdf.parse(supportSdate));
                }
                if (!StringUtil.isNull(supportEdate)) {
                    detailPO.setSupportEdate(sdf.parse(supportEdate));
                }

                if (!StringUtil.isNull(supportEdate)) {
                    detailPO.setSupportEdate(sdf.parse(supportEdate));
                }
                if (!StringUtil.isNull(yearRent)) {
                    detailPO.setYearRent(Double.parseDouble(yearRent));
                }
                detailPO.setId(new Long(id));
                detailPO.setYearFlag(Integer.parseInt(year));
                if (buttonType.equals("1")) {
                    auditPO.setStatus(Constant.VI_CONSTRUCT_STATUS_00);

                    detailPO.setStatus(Constant.VI_CONSTRUCT_STATUS_00);
                    detailPO.setUserId(auditPO.getUserId());
                } else {
                    auditPO.setStatus(Constant.VI_CONSTRUCT_STATUS_01);

                    detailPO.setStatus(Constant.VI_CONSTRUCT_STATUS_01);
                    detailPO.setUserId(auditPO.getUserId());
                }
                
                // 先删除明细表
                TtViConstructDetailPO oldDetailPO = new TtViConstructDetailPO();
                if (!StringUtils.isEmpty(DETAIL_ID)) {
                    oldDetailPO.setDetailId(Long.parseLong(DETAIL_ID));
                    detailPO.setDetailId(Long.parseLong(DETAIL_ID));
                    DealerInfoDao.getInstance().update(oldDetailPO, detailPO);
                } else {
                    detailPO.setDetailId(new Long(SequenceManager.getSequence("")));
                    DETAIL_ID = detailPO.getDetailId().toString();
                    DealerInfoDao.getInstance().insert(detailPO);
                }
                auditPO.setUserId(Constant.VI_CONSTRUCT_AUDIT_02);
                Long auditDtailId = new Long(SequenceManager.getSequence(""));
                auditPO.setId(auditDtailId);
                auditPO.setDetailId(Long.parseLong(DETAIL_ID));
                auditPO.setDealerId(Long.parseLong(DEALER_ID));
                auditPO.setCreateBy(logonUser.getUserId());
                auditPO.setCreateDate(new Date());
                auditPO.setYearFlag(year);
                DealerInfoDao.getInstance().insert(auditPO);
            }
            String deleteFjids = request.getParamValue("list");
            if (!StringUtil.isNull(deleteFjids)) {
                ajaxDao.delete("DELETE Fs_Fileupload WHERE fjid IN(" + deleteFjids + ")", null);
            }

            act.setOutData("result", id);
            act.setOutData("DETAIL_ID", DETAIL_ID);
            act.setForword(viConstructQry);
        } catch (BizException e) {
            logger.error(logonUser, e);
            act.setException(e);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "保存");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void stopVi() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String DEALER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("DEALER_ID"));
            String year = CommonUtils.checkNull(act.getRequest().getParamValue("year"));
            String detailId = CommonUtils.checkNull(act.getRequest().getParamValue("DETAIL_ID"));

            TtViConstructAuditPO auditPO2 = new TtViConstructAuditPO();

            auditPO2.setDealerId(Long.parseLong(DEALER_ID));
            auditPO2.setYearFlag(year);
            auditPO2.setDetailId(new Long(SequenceManager.getSequence("")));
            auditPO2.setStatus(Constant.VI_CONSTRUCT_STATUS_04);
            auditPO2.setVioverDate(new Date());
            auditPO2.setCreateBy(logonUser.getUserId());
            auditPO2.setCreateDate(new Date());
            DealerInfoDao.getInstance().insert(auditPO2);

            TtViConstructDetailPO detailPO1 = new TtViConstructDetailPO();
            TtViConstructDetailPO detailPO3 = new TtViConstructDetailPO();
            detailPO1.setDetailId(Long.parseLong(detailId));

            detailPO3.setStatus(Constant.VI_CONSTRUCT_STATUS_04);
            detailPO3.setVioverDate(new Date());
            detailPO3.setDetailId(Long.parseLong(detailId));
            DealerInfoDao.getInstance().update(detailPO1, detailPO3);
            act.setForword(viConstructQry);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "终止");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void recoveryViCheck() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String detailId = CommonUtils.checkNull(act.getRequest().getParamValue("DETAIL_ID"));
            
            // 恢复CHECK
            TtViConstructDetailPO detailPO1 = new TtViConstructDetailPO();
            detailPO1.setDetailId(Long.parseLong(detailId));
            detailPO1.setStatus(Constant.VI_CONSTRUCT_STATUS_04);
            List<PO> detailPO1Lst = DealerInfoDao.getInstance().select(detailPO1);
            if (detailPO1Lst == null || detailPO1Lst.isEmpty()) {
                throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "明细数据状态已被其他人变更!");
            }
            
            detailPO1 = (TtViConstructDetailPO) detailPO1Lst.get(0);
            Date curDate = new Date();
            if (curDate.compareTo(detailPO1.getSupportSdate()) < 0 || DateUtils.truncate(curDate, Calendar.DAY_OF_MONTH).compareTo(detailPO1.getSupportEdate()) > 0) {
                throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "支持时间已过,不能再恢复!");
            }
            
            // 支持台数达到后,不能恢复 
            BigDecimal yearRent = new BigDecimal(detailPO1.getYearRent());
            BigDecimal maxSupportAmount = yearRent.subtract(yearRent.multiply(new BigDecimal(detailPO1.getScale()).divide(new BigDecimal(100))));
            int maxSupportNumber = maxSupportAmount.divide(new BigDecimal(detailPO1.getAmount())).intValue();
            if (maxSupportNumber <= detailPO1.getSupportNumber()) {
                throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "支持数已经达到，不能进行恢复操作!");
            }
            
        } catch (BizException e) {
            logger.error(logonUser, e);
            act.setException(e);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "恢复");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    public void recoveryVi() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String DEALER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("DEALER_ID"));
            String year = CommonUtils.checkNull(act.getRequest().getParamValue("year"));
            String detailId = CommonUtils.checkNull(act.getRequest().getParamValue("DETAIL_ID"));
            String addSupportNumber = CommonUtils.checkNull(act.getRequest().getParamValue("addSupportNumber"));
            
            TtViConstructAuditPO auditPO2 = new TtViConstructAuditPO();
            auditPO2.setDealerId(Long.parseLong(DEALER_ID));
            auditPO2.setYearFlag(year);
            auditPO2.setDetailId(new Long(SequenceManager.getSequence("")));
            auditPO2.setStatus(Constant.VI_CONSTRUCT_STATUS_02);
            auditPO2.setCreateBy(logonUser.getUserId());
            auditPO2.setCreateDate(new Date());
            DealerInfoDao.getInstance().insert(auditPO2);

            TtViConstructDetailPO detailPO1 = new TtViConstructDetailPO();
            TtViConstructDetailPO detailPO3 = new TtViConstructDetailPO();
            detailPO1.setDetailId(Long.parseLong(detailId));

            detailPO3.setStatus(Constant.VI_CONSTRUCT_STATUS_02);
            detailPO3.setDetailId(Long.parseLong(detailId));
            if (year.equals("1")) {
                detailPO3.setSupportNumber(0);
            } else {
                detailPO3.setSupportNumber(Integer.parseInt(addSupportNumber));
            }
            DealerInfoDao.getInstance().update(detailPO1, detailPO3);
            act.setForword(viConstructQry);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "恢复");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 
     */
    public void SHENHETONGGUO() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String DEALER_ID = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
            String USER_ID = CommonUtils.checkNull(request.getParamValue("user_id"));
            String REMARK = CommonUtils.checkNull(request.getParamValue("remark"));
            String YEAR_FALG = CommonUtils.checkNull(request.getParamValue("YEAR_FALG"));
            String DETAIL_ID = CommonUtils.checkNull(request.getParamValue("DETAIL_ID"));
            
            TtViConstructAuditPO auditPO2 = new TtViConstructAuditPO();
            Long auditDetailId = new Long(SequenceManager.getSequence(""));
            auditPO2.setId(auditDetailId);
            auditPO2.setDealerId(Long.parseLong(DEALER_ID));
            auditPO2.setYearFlag(YEAR_FALG);
            auditPO2.setUserId(Integer.parseInt(USER_ID) + 1);
            auditPO2.setStatus(Constant.VI_CONSTRUCT_STATUS_02);
            
            TtViConstructDetailPO detailPO1 = new TtViConstructDetailPO();
            TtViConstructDetailPO detailPO3 = new TtViConstructDetailPO();
            detailPO1.setDetailId(Long.parseLong(DETAIL_ID));

            detailPO3.setStatus(Constant.VI_CONSTRUCT_STATUS_02);
            detailPO3.setUserId(auditPO2.getUserId());
            detailPO3.setDetailId(Long.parseLong(DETAIL_ID));
            DealerInfoDao.getInstance().update(detailPO1, detailPO3);

            auditPO2.setRemark(REMARK);
            auditPO2.setCreateBy(logonUser.getUserId());
            auditPO2.setCreateDate(new Date());
            auditPO2.setDetailId(Long.parseLong(DETAIL_ID));
            act.setOutData("user_id", auditPO2.getUserId() - 1);
            act.setOutData("dealer_id", DEALER_ID);
            DealerInfoDao.getInstance().insert(auditPO2);
            act.setForword(viConstructAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void SHENHETONGGUOForDealerAudit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String DEALER_ID = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
            String USER_ID = CommonUtils.checkNull(request.getParamValue("user_id"));
            String REMARK = CommonUtils.checkNull(request.getParamValue("remark_" + DEALER_ID));
            TtDealerSecendAuditPO auditPO2 = new TtDealerSecendAuditPO();
            auditPO2.setId(new Long(SequenceManager.getSequence("")));
            auditPO2.setUserId(Integer.parseInt(USER_ID) + 1);
            if (auditPO2.getUserId().equals(Constant.DEALER_SECEND_AUDIT_02)) {
                auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_02);

                TmDealerPO dealerPO = new TmDealerPO();
                TmDealerPO dealerPO1 = new TmDealerPO();
                dealerPO.setDealerId(new Long(DEALER_ID));
                dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_02);
                DealerInfoDao.getInstance().update(dealerPO, dealerPO1);

            } else if (auditPO2.getUserId().equals(Constant.DEALER_SECEND_AUDIT_03)) {
                auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_03);

                TmDealerPO dealerPO = new TmDealerPO();
                TmDealerPO dealerPO1 = new TmDealerPO();
                dealerPO.setDealerId(new Long(DEALER_ID));
                dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_03);
                DealerInfoDao.getInstance().update(dealerPO, dealerPO1);
                
            } else if (auditPO2.getUserId().equals(Constant.DEALER_SECEND_AUDIT_04)) {
                auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_07);

                TmDealerPO dealerPO = new TmDealerPO();
                TmDealerPO dealerPO1 = new TmDealerPO();
                dealerPO.setDealerId(new Long(DEALER_ID));
                dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_07);
                DealerInfoDao.getInstance().update(dealerPO, dealerPO1);
                
                TmDealerPO dealerPO2 = new TmDealerPO();
                dealerPO2.setDealerId(new Long(DEALER_ID));
                List<PO> dealerLst = DealerInfoDao.getInstance().select(dealerPO2);
                dealerPO2 = (TmDealerPO) dealerLst.get(0);
                
                TmDealerDetailPO dealerDetailPO = new TmDealerDetailPO();
                TmDealerDetailPO dealerDetailPO1 = new TmDealerDetailPO();
                dealerDetailPO.setFkDealerId(dealerPO.getDealerId());
                String isQualifiedSales = CommonUtils.checkNull(request.getParamValue("IS_QUALIFIED_SALES"));
                String isQualifiedService = CommonUtils.checkNull(request.getParamValue("IS_QUALIFIED_SERVICE"));
                dealerDetailPO1.setIsQualifiedSales(new Integer(isQualifiedSales));
                dealerDetailPO1.setIsQualifiedService(new Integer(isQualifiedService));
                DealerInfoDao.getInstance().update(dealerDetailPO, dealerDetailPO1);
                
                // 自动创建登录账号和密码
                UserMngDAO.batchAddUserByDealerCode(dealerPO2.getDealerCode());

            } else if (auditPO2.getUserId().equals(Constant.DEALER_SECEND_AUDIT_05)) {

                TmDealerPO dealerPO = new TmDealerPO();
                TmDealerPO dealerPO1 = new TmDealerPO();
                dealerPO.setDealerId(new Long(DEALER_ID));
                dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_06);
                DealerInfoDao.getInstance().update(dealerPO, dealerPO1);

                auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_06);
            }
            auditPO2.setRemark(REMARK);
            auditPO2.setDealerId(new Long(DEALER_ID));
            act.setOutData("user_id", auditPO2.getUserId() - 1);
            auditPO2.setCreateBy(logonUser.getUserId());
            auditPO2.setCreateDate(new Date());
            DealerInfoDao.getInstance().insert(auditPO2);
            act.setForword(serviceQueryDealerInitUrl2ndforAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void batchAccept() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String DEALER_ID = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
            String [] DEALER_IDS = DEALER_ID.split(",");
            String USER_ID = CommonUtils.checkNull(request.getParamValue("user_id"));
            String REMARK = CommonUtils.checkNull(request.getParamValue("remark_" + DEALER_ID));
            TtDealerSecendAuditPO auditPO2 = new TtDealerSecendAuditPO();
            auditPO2.setId(new Long(SequenceManager.getSequence("")));
            auditPO2.setUserId(Integer.parseInt(USER_ID) + 1);
            if (auditPO2.getUserId().equals(Constant.DEALER_SECEND_AUDIT_02)) {
                for (int i = 0; i < DEALER_IDS.length; i++) {
                    auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_02);
                    TmDealerPO dealerPO = new TmDealerPO();
                    TmDealerPO dealerPO1 = new TmDealerPO();
                    dealerPO.setDealerId(new Long(DEALER_IDS[i]));
                    dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_02);
                    DealerInfoDao.getInstance().update(dealerPO, dealerPO1);
                }
            } else if (auditPO2.getUserId().equals(Constant.DEALER_SECEND_AUDIT_03)) {
                for (int i = 0; i < DEALER_IDS.length; i++) {
                    auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_03);
                    TmDealerPO dealerPO = new TmDealerPO();
                    TmDealerPO dealerPO1 = new TmDealerPO();
                    dealerPO.setDealerId(new Long(DEALER_IDS[i]));
                    dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_03);
                    DealerInfoDao.getInstance().update(dealerPO, dealerPO1);
                    
                }

            } else if (auditPO2.getUserId().equals(Constant.DEALER_SECEND_AUDIT_04)) {
                for (int i = 0; i < DEALER_IDS.length; i++) {
                    auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_04);
                    TmDealerPO dealerPO = new TmDealerPO();
                    TmDealerPO dealerPO1 = new TmDealerPO();
                    dealerPO.setDealerId(new Long(DEALER_IDS[i]));
                    dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_04);
                    DealerInfoDao.getInstance().update(dealerPO, dealerPO1);
                    
                    TmDealerDetailPO dealerDetailPO = new TmDealerDetailPO();
                    TmDealerDetailPO dealerDetailPO1 = new TmDealerDetailPO();
                    dealerDetailPO.setFkDealerId(dealerPO.getDealerId());
                    String isQualifiedSales = CommonUtils.checkNull(request.getParamValue("IS_QUALIFIED_SALES"));
                    String isQualifiedService = CommonUtils.checkNull(request.getParamValue("IS_QUALIFIED_SERVICE"));
                    dealerDetailPO1.setIsQualifiedSales(new Integer(isQualifiedSales));
                    dealerDetailPO1.setIsQualifiedService(new Integer(isQualifiedService));
                    DealerInfoDao.getInstance().update(dealerDetailPO, dealerDetailPO1);
                }
            } else if (auditPO2.getUserId().equals(Constant.DEALER_SECEND_AUDIT_05)) {
                for (int i = 0; i < DEALER_IDS.length; i++) {
                    auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_06);
                    TmDealerPO dealerPO = new TmDealerPO();
                    TmDealerPO dealerPO1 = new TmDealerPO();
                    dealerPO.setDealerId(new Long(DEALER_IDS[i]));
                    dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_06);
                    DealerInfoDao.getInstance().update(dealerPO, dealerPO1);
                }
            }
            for (int i = 0; i < DEALER_IDS.length; i++) {
                auditPO2.setRemark(REMARK);
                auditPO2.setDealerId(new Long(DEALER_IDS[i]));
                act.setOutData("user_id", auditPO2.getUserId() - 1);
                auditPO2.setCreateBy(logonUser.getUserId());
                auditPO2.setCreateDate(new Date());
                DealerInfoDao.getInstance().insert(auditPO2);
            }
            act.setForword(serviceQueryDealerInitUrl2ndforAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "审核");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void BOHUI() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String YEAR_FALG = CommonUtils.checkNull(act.getRequest().getParamValue("YEAR_FALG"));
            String DEALER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("DEALER_ID"));
            String USER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("user_id"));
            TtViConstructAuditPO auditPO2 = new TtViConstructAuditPO();
            String REMARK = CommonUtils.checkNull(act.getRequest().getParamValue("remark"));
            String DETAIL_ID = CommonUtils.checkNull(act.getRequest().getParamValue("DETAIL_ID"));
            auditPO2.setDealerId(Long.parseLong(DEALER_ID));
            auditPO2.setYearFlag(YEAR_FALG);
            act.setOutData("user_id", USER_ID);
            act.setOutData("dealer_id", DEALER_ID);
            auditPO2.setUserId(new Integer(USER_ID));
            auditPO2.setRemark(REMARK);
            auditPO2.setStatus(Constant.VI_CONSTRUCT_STATUS_03);
            auditPO2.setCreateBy(logonUser.getUserId());
            auditPO2.setCreateDate(new Date());
            Long auditDetailId = new Long(SequenceManager.getSequence(""));
            auditPO2.setId(auditDetailId);
            auditPO2.setDetailId(Long.parseLong(DETAIL_ID));
            DealerInfoDao.getInstance().insert(auditPO2);

            TtViConstructDetailPO detailPO1 = new TtViConstructDetailPO();
            TtViConstructDetailPO detailPO3 = new TtViConstructDetailPO();
            detailPO1.setDetailId(Long.parseLong(DETAIL_ID));

            detailPO3.setStatus(Constant.VI_CONSTRUCT_STATUS_03);
            detailPO3.setUserId(auditPO2.getUserId());
            detailPO3.setDetailId(Long.parseLong(DETAIL_ID));
            DealerInfoDao.getInstance().update(detailPO1, detailPO3);

            act.setForword(viConstructAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "驳回");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void BOHUIForDealerAudit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String DEALER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("DEALER_ID"));
            String USER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("user_id"));
            String curPage = CommonUtils.checkNull(act.getRequest().getParamValue("curPage"));
            TtDealerSecendAuditPO auditPO2 = new TtDealerSecendAuditPO();
            String REMARK = CommonUtils.checkNull(act.getRequest().getParamValue("remark_" + DEALER_ID));
            auditPO2.setDealerId(Long.parseLong(DEALER_ID));
            act.setOutData("user_id", USER_ID);
            auditPO2.setUserId(new Integer(USER_ID) + 1);
            auditPO2.setRemark(REMARK);
            auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_05);
            auditPO2.setId(new Long(SequenceManager.getSequence("")));
            auditPO2.setCreateBy(logonUser.getUserId());
            auditPO2.setCreateDate(new Date());
            DealerInfoDao.getInstance().insert(auditPO2);

            TmDealerPO dealerPO = new TmDealerPO();
            TmDealerPO dealerPO1 = new TmDealerPO();
            dealerPO.setDealerId(new Long(DEALER_ID));
            dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_05);

            DealerInfoDao.getInstance().update(dealerPO, dealerPO1);
            act.setOutData("curPage", curPage);
            act.setForword(serviceQueryDealerInitUrl2ndforAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "驳回");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    public void batchRebut() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String DEALER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("DEALER_ID"));
            
            String[] DEALER_IDS = DEALER_ID.split(",");
            String USER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("user_id"));
            TtDealerSecendAuditPO auditPO2 = new TtDealerSecendAuditPO();
            String REMARK = CommonUtils.checkNull(act.getRequest().getParamValue("remark_" + DEALER_ID));
            
            
            act.setOutData("user_id", USER_ID);
            auditPO2.setUserId(new Integer(USER_ID) + 1);
            auditPO2.setRemark(REMARK);
            auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_05);
            auditPO2.setId(new Long(SequenceManager.getSequence("")));
            auditPO2.setCreateBy(logonUser.getUserId());
            auditPO2.setCreateDate(new Date());
            for (int i = 0; i < DEALER_IDS.length; i++) {
                auditPO2.setDealerId(Long.parseLong(DEALER_IDS[i]));
                DealerInfoDao.getInstance().insert(auditPO2);
            }
            

            TmDealerPO dealerPO = new TmDealerPO();
            TmDealerPO dealerPO1 = new TmDealerPO();
            dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_05);
            for (int i = 0; i < DEALER_IDS.length; i++) {
                dealerPO.setDealerId(new Long(DEALER_IDS[i]));
                DealerInfoDao.getInstance().update(dealerPO, dealerPO1);
            }
            act.setForword(serviceQueryDealerInitUrl2ndforAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "驳回");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    public void BOHUIForDealerDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String DEALER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("DEALER_ID"));
            String USER_ID = CommonUtils.checkNull(act.getRequest().getParamValue("user_id"));
            String curPage = CommonUtils.checkNull(act.getRequest().getParamValue("curPage"));
            TtDealerSecendAuditPO auditPO2 = new TtDealerSecendAuditPO();
            String REMARK = CommonUtils.checkNull(act.getRequest().getParamValue("remark_" + DEALER_ID));
            auditPO2.setDealerId(Long.parseLong(DEALER_ID));
            act.setOutData("user_id", USER_ID);
            auditPO2.setUserId(new Integer(USER_ID) + 1);
            auditPO2.setRemark(REMARK);
            auditPO2.setStatus(Constant.DEALER_SECEND_STATUS_05);
            auditPO2.setId(new Long(SequenceManager.getSequence("")));
            auditPO2.setCreateBy(logonUser.getUserId());
            auditPO2.setCreateDate(new Date());
            DealerInfoDao.getInstance().insert(auditPO2);

            TmDealerPO dealerPO = new TmDealerPO();
            TmDealerPO dealerPO1 = new TmDealerPO();
            dealerPO.setDealerId(new Long(DEALER_ID));
            dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_05);
            if (logonUser.getDealerId() == null) {
                act.setOutData("isDealer", "yes");
            } else {
                act.setOutData("isDealer", "no");
            }
            DealerInfoDao.getInstance().update(dealerPO, dealerPO1);
            act.setOutData("curPage", curPage);
            act.setForword(secendQueryDealerCsInitUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "驳回");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void viConstructQryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setForword(viConstructQry);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "形象店支持查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void ConstructAuditInit1() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setOutData("user_id", Constant.VI_CONSTRUCT_AUDIT_02);
            act.setForword(viConstructAuditQry);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "渠道部审核初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void ConstructAuditInit2() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            List<Map<String, Object>> maxAuditLst = DealerInfoDao.getInstance().getMaxAuditYear();
            if (maxAuditLst == null || maxAuditLst.isEmpty()) {
                act.setOutData("maxAuditYear", 0);
            } else {
                BigDecimal maxAuditYear = (BigDecimal) maxAuditLst.get(0).get("MAX_AUDIT_YEAR"); 
                if (maxAuditYear == null) {
                    act.setOutData("maxAuditYear", 0);
                } else {
                    act.setOutData("maxAuditYear", maxAuditYear.intValue());
                }
            }
            act.setOutData("orglist", orgList);
            act.setOutData("user_id", Constant.VI_CONSTRUCT_AUDIT_03);
            act.setForword(viConstructAuditQry);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售部审核初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void ConstructAuditInit3() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setOutData("user_id", Constant.VI_CONSTRUCT_AUDIT_04);
            act.setForword(viConstructAuditQry);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "总经理审核初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void DealerAuditInit1() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("user_id", Constant.DEALER_SECEND_AUDIT_02);
            ProductManageDao dao = new ProductManageDao();
            List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            act.setOutData("areaList", areaList);
            List<Map<String, Object>> brand = dao.getbrandList();// 品牌
            act.setOutData("brand", brand);
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setForword(serviceQueryDealerInitUrl2ndforAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "大区经理审核初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void DealerAuditInit2() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("user_id", Constant.DEALER_SECEND_AUDIT_03);
            ProductManageDao dao = new ProductManageDao();
            List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            act.setOutData("areaList", areaList);
            List<Map<String, Object>> brand = dao.getbrandList();// 品牌
            act.setOutData("brand", brand);
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setForword(serviceQueryDealerInitUrl2ndforAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售管理中心审核初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void DealerAuditInit3() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("user_id", Constant.DEALER_SECEND_AUDIT_04);
            ProductManageDao dao = new ProductManageDao();
            List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
            act.setOutData("areaList", areaList);
            List<Map<String, Object>> brand = dao.getbrandList();// 品牌
            act.setOutData("brand", brand);
            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setForword(serviceQueryDealerInitUrl2ndforAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "总经理审核初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    public void queryDealerCsInfoForDealer() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dealerId = logonUser.getDealerId();
            String cmd = CommonUtils.checkNull(act.getRequest().getParamValue("cmd"));
            List<Object> params = new ArrayList<Object>();
            StringBuffer sbSql = new StringBuffer();

            sbSql.append("select a.DEALER_ID,\n");
            sbSql.append("       a.COMPANY_ID,\n");
            sbSql.append("       a.DEALER_TYPE,\n");
            sbSql.append("       a.DEALER_CODE,\n");
            sbSql.append("       a.DEALER_NAME,\n");
            sbSql.append("       a.STATUS,\n");
            sbSql.append("       a.DEALER_LEVEL,\n");
            sbSql.append("       a.DEALER_CLASS,\n");
            sbSql.append("       a.PARENT_DEALER_D,\n");
            sbSql.append("       a.DEALER_ORG_ID,\n");
            sbSql.append("       a.PROVINCE_ID,\n");
            sbSql.append("       a.CITY_ID,\n");
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
            sbSql.append("       a.COUNTIES,\n");
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
            sbSql.append("       b.FAX_NO,\n");
            sbSql.append("       b.EMAIL,\n");
            sbSql.append("       b.THE_AGENTS,\n");
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
            sbSql.append("       d.ORG_CODE,\n");
            sbSql.append("       a.DEALER_ORG_ID,\n");
            if(cmd.equals("1")){ 
                sbSql.append("       a.CHANGE_AUTID_STATUS,\n");
            }
            
            sbSql.append("       b.COMPANY_ZC_CODE,\n");
            sbSql.append("       b.MEETING_ROOM_AREA,\n");
            sbSql.append("       b.FITTINGS_DEC_NAME,\n");
            sbSql.append("       b.FITTINGS_DEC_PHONE,\n");
            sbSql.append("       b.FITTINGS_DEC_FAX,\n");
            sbSql.append("       a.INVOICE_POST_ADD,\n");
            sbSql.append("       b.FITTINGS_DEC_TELPHONE,\n");
            sbSql.append("       b.FITTINGS_DEC_EMAIL,\n");

            sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
            sbSql.append("       b.PROXY_VEHICLE_TYPE,\n");
            sbSql.append("       b.PROXY_AREA,\n");
            sbSql.append("       b.IS_LOW_SER,\n");
            sbSql.append("       vwod.root_org_name,\n");
            sbSql.append("       vwod.root_org_id,\n");
            sbSql.append("       (select dealer_name from tm_dealer where dealer_id = a.parent_dealer_d) as PARENT_DEALER_NAME\n");
            if(cmd.equals("1")){
                sbSql.append("  from tmp_tm_dealer a, tmp_tm_dealer_detail b, tm_company c, tm_org d,vw_org_dealer_service vwod\n");
            }else{
                sbSql.append("  from tm_dealer a, tm_dealer_detail b, tm_company c, tm_org d,vw_org_dealer_service vwod\n");
            }
            sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
            sbSql.append("   and a.dealer_id = vwod.dealer_id(+)\n");
            sbSql.append("   and a.company_id = c.company_id\n");
            sbSql.append("   and a.dealer_org_id = d.org_id(+)\n");
            sbSql.append("   and a.dealer_id = ?");
            params.add(dealerId);

            ProductManageDao dao = ProductManageDao.getInstance();

            Map<String, Object> dealerData = dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());

            TtProxyAreaPO areaPO = new TtProxyAreaPO();
            areaPO.setDealerId(Long.valueOf(dealerId));
            List<TtProxyAreaPO> list = factory.select(areaPO);
            String ProxyAreaName = "";
            String ProxyArea = "";
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    ProxyAreaName = ProxyAreaName + "," + list.get(i).getProxyAreaName();
                    ProxyArea = ProxyArea + "," + list.get(i).getProxyArea();
                } else {
                    ProxyAreaName = ProxyAreaName + list.get(i).getProxyAreaName();
                    ProxyArea = ProxyArea + list.get(i).getProxyArea();
                }

            }
            TmRegionPO t = new TmRegionPO();
            t.setRegionCode(dealerData.get("CITY_ID") + "");
            TmRegionPO tt = (TmRegionPO) dao.select(t).get(0);
            tt.getRegionName();
            act.setOutData("dMap", dealerData);
            act.setOutData("ProxyArea", ProxyArea);
            act.setOutData("ProxyAreaName", ProxyAreaName);
            act.setOutData("CITY_ID", dealerData.get("CITY_ID"));
            act.setOutData("CITY_NAME", tt.getRegionName());
            act.setOutData("ROOT_ORG_ID", dealerData.get("ROOT_ORG_ID"));

            List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
            act.setOutData("orglist", orgList);
            act.setForword(dealerCsInfoForDealer);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护修改");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    public void saveDealerInfoForDealer() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        HashMap<String, Object> mapA = new HashMap<String, Object>();
        HashMap<String, Object> mapB = new HashMap<String, Object>();
        HashMap<String, Object> mapC = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID")).trim();
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE")).trim(); // 经销商代码
            String dealerType = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));
            String dealerOrgId = CommonUtils.checkNull(request.getParamValue("DEALER_ORG_ID"));
            String dealerLevel = CommonUtils.checkNull(request.getParamValue("DEALER_LEVEL"));
            String parentDealerD = CommonUtils.checkNull(request.getParamValue("PARENT_DEALER_D"));
            String balanceLevel = CommonUtils.checkNull(request.getParamValue("BALANCE_LEVEL")); // 结算等级
            String invoiceLevel = CommonUtils.checkNull(request.getParamValue("INVOICE_LEVEL")); // 开票等级
            String companyId = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
            String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
            // 判断当前经销商公司是否已经被占用
            if (companyBeUsed(companyId, dealerType, dealerId))
                throw new RuntimeException("经销商所属公司不能为空!");

            if (dealerLevel.equals(Constant.DEALER_LEVEL_02.toString())) {
                if (parentDealerD.equals("")) {
                    throw new RuntimeException("无上级经销商!");
                } else {
                    mapA.put("PARENT_DEALER_D", parentDealerD);
                }
            }

            if (dealerType.equals(String.valueOf(Constant.DEALER_TYPE_DWR))) {
                if (balanceLevel.equals(""))
                    throw new RuntimeException("无结算等级!");
                if (invoiceLevel.equals(""))
                    throw new RuntimeException("无开票等级!");
            }

            // 经销商主数据
            mapA.put("COMPANY_ID", companyId);
            mapA.put("DEALER_CODE", dealerCode);
            mapA.put("DEALER_ORG_ID", dealerOrgId);
            mapA.put("DEALER_LEVEL", dealerLevel);

            mapA.put("DEALER_TYPE", dealerType);
            mapA.put("BALANCE_LEVEL", balanceLevel);
            mapA.put("INVOICE_LEVEL", invoiceLevel);

            mapA.put("DEALER_SHORTNAME", CommonUtils.checkNull(request.getParamValue("DEALER_SHORTNAME")));
            mapA.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            mapA.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));
            mapA.put("PROVINCE_ID", CommonUtils.checkNull(request.getParamValue("PROVINCE_ID")));
            mapA.put("CITY_ID", CommonUtils.checkNull(request.getParamValue("CITY_ID")));
            mapA.put("COUNTIES", CommonUtils.checkNull(request.getParamValue("COUNTIES")));
            mapA.put("ZIP_CODE", CommonUtils.checkNull(request.getParamValue("ZIP_CODE")));
            mapA.put("ADDRESS", CommonUtils.checkNull(request.getParamValue("ADDRESS")));
            mapA.put("ZCCODE", CommonUtils.checkNull(request.getParamValue("ZCCODE")));
            mapA.put("ZY_SCOPE", CommonUtils.checkNull(request.getParamValue("ZY_SCOPE")));
            mapA.put("JY_SCOPE", CommonUtils.checkNull(request.getParamValue("JY_SCOPE")));
            mapA.put("SITEDATE", CommonUtils.checkNull(request.getParamValue("SITEDATE")));
            mapA.put("DESTROYDATE", CommonUtils.checkNull(request.getParamValue("DESTROYDATE")));
            mapA.put("LEGAL", CommonUtils.checkNull(request.getParamValue("LEGAL")));
            mapA.put("LEGAL_PHONE", CommonUtils.checkNull(request.getParamValue("LEGAL_PHONE")));
            mapA.put("LEGAL_TELPHONE", CommonUtils.checkNull(request.getParamValue("LEGAL_TELPHONE")));
            mapA.put("LEGAL_EMAIL", CommonUtils.checkNull(request.getParamValue("LEGAL_EMAIL")));
            mapA.put("INVOICE_PERSION", CommonUtils.checkNull(request.getParamValue("INVOICE_PERSION")));
            mapA.put("INVOICE_TELPHONE", CommonUtils.checkNull(request.getParamValue("INVOICE_TELPHONE")));
            mapA.put("BEGIN_BANK", CommonUtils.checkNull(request.getParamValue("BEGIN_BANK")));
            mapA.put("ERP_CODE", CommonUtils.checkNull(request.getParamValue("ERP_CODE")));
            mapA.put("TAXES_NO", CommonUtils.checkNull(request.getParamValue("TAXES_NO")));
            mapA.put("INVOICE_ACCOUNT", CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT")));
            mapA.put("INVOICE_PHONE", CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE")));
            mapA.put("INVOICE_ADD", CommonUtils.checkNull(request.getParamValue("INVOICE_ADD")));
            mapA.put("TAXPAYER_NO", CommonUtils.checkNull(request.getParamValue("TAXPAYER_NO")));
            mapA.put("TAXPAYER_NATURE", CommonUtils.checkNull(request.getParamValue("TAXPAYER_NATURE")));
            mapA.put("TAX_INVOICE", CommonUtils.checkNull(request.getParamValue("TAX_INVOICE")));
            mapA.put("TAX_DISRATE", CommonUtils.checkNull(request.getParamValue("TAX_DISRATE")));
            mapA.put("REMARK", CommonUtils.checkNull(request.getParamValue("REMARK")));
            mapA.put("MAIN_RESOURCES", CommonUtils.checkNull(request.getParamValue("MAIN_RESOURCES")));
            mapA.put("CHANGE_AUTID_STATUS", Constant.SERVICE_CHANGE_STATUS_01);

            // 经销商备注数据
            mapB.put("COMPANY_ZC_CODE", CommonUtils.checkNull(request.getParamValue("COMPANY_ZC_CODE")));
            mapB.put("WEBMASTER_NAME", CommonUtils.checkNull(request.getParamValue("WEBMASTER_NAME")));
            mapB.put("WEBMASTER_PHONE", CommonUtils.checkNull(request.getParamValue("WEBMASTER_PHONE")));
            mapB.put("WEBMASTER_TELPHONE", CommonUtils.checkNull(request.getParamValue("WEBMASTER_TELPHONE")));
            mapB.put("WEBMASTER_EMAIL", CommonUtils.checkNull(request.getParamValue("WEBMASTER_EMAIL")));
            mapB.put("MARKET_NAME", CommonUtils.checkNull(request.getParamValue("MARKET_NAME")));
            mapB.put("MARKET_PHONE", CommonUtils.checkNull(request.getParamValue("MARKET_PHONE")));
            mapB.put("MARKET_TELPHONE", CommonUtils.checkNull(request.getParamValue("MARKET_TELPHONE")));
            mapB.put("MARKET_EMAIL", CommonUtils.checkNull(request.getParamValue("MARKET_EMAIL")));
            mapB.put("MANAGER_NAME", CommonUtils.checkNull(request.getParamValue("MANAGER_NAME")));
            mapB.put("MANAGER_PHONE", CommonUtils.checkNull(request.getParamValue("MANAGER_PHONE")));
            mapB.put("MANAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("MANAGER_TELPHONE")));
            mapB.put("MANAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("MANAGER_EMAIL")));
            mapB.put("SER_MANAGER_NAME", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_NAME")));
            mapB.put("SER_MANAGER_PHONE", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_PHONE")));
            mapB.put("SER_MANAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_TELPHONE")));
            mapB.put("SER_MANAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("SER_MANAGER_EMAIL")));
            mapB.put("CLAIM_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_NAME")));
            mapB.put("CLAIM_DIRECTOR_PHONE", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_PHONE")));
            mapB.put("CLAIM_DIRECTOR_TELPHONE", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_TELPHONE")));
            mapB.put("CLAIM_DIRECTOR_EMAIL", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_EMAIL")));
            mapB.put("CLAIM_DIRECTOR_FAX", CommonUtils.checkNull(request.getParamValue("CLAIM_DIRECTOR_FAX")));
            mapB.put("CUSTSER_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_NAME")));
            mapB.put("CUSTSER_DIRECTOR_PHONE", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_PHONE")));
            mapB.put("CUSTSER_DIRECTOR_TELPHONE", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_TELPHONE")));
            mapB.put("CUSTSER_DIRECTOR_EMAIL", CommonUtils.checkNull(request.getParamValue("CUSTSER_DIRECTOR_EMAIL")));
            mapB.put("SER_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_NAME")));
            mapB.put("SER_DIRECTOR_PHONE", CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_PHONE")));
            mapB.put("SER_DIRECTOR_TELHONE", CommonUtils.checkNull(request.getParamValue("SER_DIRECTOR_TELHONE")));
            mapB.put("TECHNOLOGY_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_NAME")));
            mapB.put("TECHNOLOGY_DIRECTOR_TELPHONE", CommonUtils.checkNull(request.getParamValue("TECHNOLOGY_DIRECTOR_TELPHONE")));
            mapB.put("WORKSHOP_DIRECTOR_NAME", CommonUtils.checkNull(request.getParamValue("WORKSHOP_DIRECTOR_NAME")));
            mapB.put("WORKSHOP_DIRECTOR_TELpHONE", CommonUtils.checkNull(request.getParamValue("WORKSHOP_DIRECTOR_TELpHONE")));
            mapB.put("FINANCE_MANAGER_NAME", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_NAME")));
            mapB.put("FINANCE_MANAGER_PHONE", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_PHONE")));
            mapB.put("FINANCE_MANAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_TELPHONE")));
            mapB.put("FINANCE_MANAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("FINANCE_MANAGER_EMAIL")));
            
            mapB.put("MESSAGER_NAME", CommonUtils.checkNull(request.getParamValue("MESSAGER_NAME")));
            mapB.put("MESSAGER_PHONE", CommonUtils.checkNull(request.getParamValue("MESSAGER_PHONE")));
            mapB.put("MESSAGER_TELPHONE", CommonUtils.checkNull(request.getParamValue("MESSAGER_TELPHONE")));
            mapB.put("MESSAGER_QQ", CommonUtils.checkNull(request.getParamValue("MESSAGER_QQ")));
            mapB.put("MESSAGER_EMAIL", CommonUtils.checkNull(request.getParamValue("MESSAGER_EMAIL")));
            mapB.put("VI_APPLAY_DATE", CommonUtils.checkNull(request.getParamValue("VI_APPLAY_DATE")));
            mapB.put("VI_BEGIN_DATE", CommonUtils.checkNull(request.getParamValue("VI_BEGIN_DATE")));
            mapB.put("VI_COMPLETED_DATE", CommonUtils.checkNull(request.getParamValue("VI_COMPLETED_DATE")));
            mapB.put("VI_CONFRIM_DATE", CommonUtils.checkNull(request.getParamValue("VI_CONFRIM_DATE")));
            mapB.put("IMAGE_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL")));
            mapB.put("IMAGE_COMFIRM_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL")));
            mapB.put("VI_SUPPORT_AMOUNT", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_AMOUNT")));
            mapB.put("VI_SUPPORT_RATIO", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_RATIO")));
            mapB.put("VI_SUPPORT_TYPE", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_TYPE")));
            mapB.put("VI_SUPPORT_DATE", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_DATE")));
            mapB.put("VI_SUPPORT_END_DATE", CommonUtils.checkNull(request.getParamValue("VI_SUPPORT_END_DATE")));
            mapB.put("FIRST_SUB_DATE", CommonUtils.checkNull(request.getParamValue("FIRST_SUB_DATE")));
            mapB.put("FIRST_GETCAR_DATE", CommonUtils.checkNull(request.getParamValue("FIRST_GETCAR_DATE")));
            mapB.put("FIRST_SAELS_DATE", CommonUtils.checkNull(request.getParamValue("FIRST_SAELS_DATE")));
            mapB.put("UNION_TYPE", CommonUtils.checkNull(request.getParamValue("UNION_TYPE")));
            mapB.put("FIXED_CAPITAL", CommonUtils.checkNull(request.getParamValue("FIXED_CAPITAL")));
            mapB.put("REGISTERED_CAPITAL", CommonUtils.checkNull(request.getParamValue("REGISTERED_CAPITAL")));
            mapB.put("PEOPLE_NUMBER", CommonUtils.checkNull(request.getParamValue("PEOPLE_NUMBER")));
            mapB.put("OFFICE_AREA", CommonUtils.checkNull(request.getParamValue("OFFICE_AREA")));
            mapB.put("PARTS_AREA", CommonUtils.checkNull(request.getParamValue("PARTS_AREA")));
            mapB.put("DEPOT_AREA", CommonUtils.checkNull(request.getParamValue("DEPOT_AREA")));
            mapB.put("MAIN_AREA", CommonUtils.checkNull(request.getParamValue("MAIN_AREA")));
            mapB.put("ONLY_MONTH_COUNT", CommonUtils.checkNull(request.getParamValue("ONLY_MONTH_COUNT")));
            mapB.put("OPENING_TIME", CommonUtils.checkNull(request.getParamValue("OPENING_TIME")));
            mapB.put("WORK_TYPE", CommonUtils.checkNull(request.getParamValue("WORK_TYPE")));
            mapB.put("IS_FOUR_S", CommonUtils.checkNull(request.getParamValue("IS_FOUR_S")));
            mapB.put("IS_LOW_SER", CommonUtils.checkNull(request.getParamValue("IS_LOW_SER")));
            mapB.put("COMPANY_ADDRESS", CommonUtils.checkNull(request.getParamValue("COMPANY_ADDRESS")));
            mapB.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));
            mapB.put("AUTHORIZATION_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_DATE")));
            mapB.put("IS_ACTING_BRAND", CommonUtils.checkNull(request.getParamValue("IS_ACTING_BRAND")));
            mapB.put("ACTING_BRAND_NAME", CommonUtils.checkNull(request.getParamValue("ACTING_BRAND_NAME")));
            mapB.put("PARTS_STORE_AMOUNT", CommonUtils.checkNull(request.getParamValue("PARTS_STORE_AMOUNT")));
            mapB.put("SHOP_TYPE", CommonUtils.checkNull(request.getParamValue("SHOP_TYPE")));
            mapB.put("HOTLINE", CommonUtils.checkNull(request.getParamValue("HOTLINE")));
            mapB.put("FAX_NO", CommonUtils.checkNull(request.getParamValue("FAX_NO")));
            mapB.put("EMAIL", CommonUtils.checkNull(request.getParamValue("EMAIL")));
            mapB.put("IS_AUTHORIZE_CITY", CommonUtils.checkNull(request.getParamValue("IS_AUTHORIZE_CITY")));
            mapB.put("IS_AUTHORIZE_COUNTY", CommonUtils.checkNull(request.getParamValue("IS_AUTHORIZE_COUNTY")));
            mapB.put("AUTHORIZE_BRAND", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_BRAND")));
            mapB.put("AUTHORIZE_GET_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_GET_DATE")));
            mapB.put("AUTHORIZE_SUB_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_SUB_DATE")));
            mapB.put("AUTHORIZE_EFFECT_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZE_EFFECT_DATE")));
            mapB.put("ANNOUNCEMENT_NO ", CommonUtils.checkNull(request.getParamValue("ANNOUNCEMENT_NO ")));
            mapB.put("ANNOUNCEMENT_DATE", CommonUtils.checkNull(request.getParamValue("ANNOUNCEMENT_DATE")));
            mapB.put("ANNOUNCEMENT_END_DATE", CommonUtils.checkNull(request.getParamValue("ANNOUNCEMENT_END_DATE")));
            mapB.put("THE_AGENTS", CommonUtils.checkNull(request.getParamValue("THE_AGENTS")));

            mapB.put("PROXY_VEHICLE_TYPE", CommonUtils.checkNull(request.getParamValue("PROXY_VEHICLE_TYPE")));

            mapB.put("FITTINGS_DEC_PHONE", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_PHONE")));
            mapB.put("FITTINGS_DEC_NAME", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_NAME")));
            mapB.put("FITTINGS_DEC_EMAIL", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_EMAIL")));
            mapB.put("FITTINGS_DEC_TELPHONE", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_TELPHONE")));
            mapB.put("FITTINGS_DEC_FAX", CommonUtils.checkNull(request.getParamValue("FITTINGS_DEC_FAX")));
            
            mapC.put("ROOT_ORG_ID", orgId);
            TmOrgPO orgDealerPO = new TmOrgPO();
            orgDealerPO.setOrgId(Long.valueOf(orgId));

            if (factory.select(orgDealerPO).size() > 0) {
                mapC.put("ROOT_ORG_NAME", factory.select(orgDealerPO).get(0).getOrgName());
            }

            List<Object> params = new ArrayList<Object>();
            StringBuffer sbSql = new StringBuffer();

            //修改
            String CHANGE_AUTID_STATUS = CommonUtils.checkNull(request.getParamValue("CHANGE_AUTID_STATUS"));
            if(CHANGE_AUTID_STATUS.equals("20051004")){
                sbSql.append("update tmp_tm_dealer set update_date = sysdate");
                for (String key : mapA.keySet()) {
                    if (!"".equals(mapA.get(key))) {
                        sbSql.append("," + key + " = ");
                        getDateBuffer(key, sbSql);
                        params.add(mapA.get(key));
                    }
                }
                sbSql.append(" where dealer_id = ?");
                params.add(dealerId);

                factory.update(sbSql.toString(), params);
                
                // 经销商主表保存成功后需要往明细表中插入相应的备注数据
                sbSql.delete(0, sbSql.length());
                params.clear();
                
                TmDealerDetailPO tddp = new TmDealerDetailPO();
                tddp.setFkDealerId(Long.parseLong(dealerId));

                List dList = factory.select(tddp);
                if (dList.size() > 0) {
                    sbSql.append("update tmp_tm_dealer_detail set update_date=sysdate");
                    for (String key : mapB.keySet()) {
                        if (!"".equals(mapB.get(key))) {
                            sbSql.append("," + key + " = ");
                            getDateBuffer(key, sbSql);
                            params.add(mapB.get(key));
                        }
                    }
                    sbSql.append(" where fk_dealer_id = ?");
                    params.add(dealerId);

                    factory.update(sbSql.toString(), params);
                } else {
                    sbSql.append("insert into tmp_tm_dealer_detail(detail_id, fk_dealer_id");
                    for (String key : mapB.keySet()) {
                        if (!"".equals(mapB.get(key))) {
                            sbSql.append("," + key);
                        }
                    }
                    sbSql.append(") values (f_getid,?");
                    params.add(dealerId);
                    for (String key : mapB.keySet()) {
                        if (!"".equals(mapB.get(key))) {
                            sbSql.append(",");
                            getDateBuffer(key, sbSql);
                            params.add(mapB.get(key));
                        }
                    }
                    sbSql.append(")");
                    factory.insert(sbSql.toString(), params);
                }
            }else{
                sbSql.append("insert into tmp_tm_dealer(ID,DEALER_ID,STATUS,OEM_COMPANY_ID");
                for (String key : mapA.keySet()) {
                    if (!"".equals(mapA.get(key))) {
                        sbSql.append("," + key);
                    }
                }
                sbSql.append(") values (?,?,?,?");
                Long id = new Long(SequenceManager.getSequence(""));
                params.add(id);
                params.add(dealerId);
                params.add(Constant.STATUS_ENABLE);
                params.add(2010010100070674L);
                for (String key : mapA.keySet()) {
                    if (!"".equals(mapA.get(key))) {
                        sbSql.append(",");
                        getDateBuffer(key, sbSql);
                        params.add(mapA.get(key));
                    }
                }
                sbSql.append(")");
                factory.insert(sbSql.toString(), params);

                // 经销商主表保存成功后需要往明细表中插入相应的备注数据
                sbSql.delete(0, sbSql.length());
                params.clear();

                sbSql.append("insert into tmp_tm_dealer_detail(detail_id, fk_dealer_id");
                for (String key : mapB.keySet()) {
                    if (!"".equals(mapB.get(key))) {
                        sbSql.append("," + key);
                    }
                }
                sbSql.append(") values (?,?");
                params.add(id);
                params.add(dealerId);
                for (String key : mapB.keySet()) {
                    if (!"".equals(mapB.get(key))) {
                        sbSql.append(",");
                        getDateBuffer(key, sbSql);
                        params.add(mapB.get(key));
                    }
                }
                sbSql.append(")");
                factory.insert(sbSql.toString(), params);
                // 新增经销商的时候，插入这个end
            }

            // 维护代理区域
            String PROXY_AREA = CommonUtils.checkNull(request.getParamValue("PROXY_AREA"));
            String PROXY_AREA_DEF = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_DEF"));
            String PROXY_AREA_NAME_DEF = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_NAME_DEF"));
            String PROXY_AREA_NAME = CommonUtils.checkNull(request.getParamValue("PROXY_AREA_NAME"));

            if (!PROXY_AREA_NAME.contains(PROXY_AREA_NAME_DEF)) {

                if (PROXY_AREA_NAME.equals("")) {
                    PROXY_AREA_NAME = PROXY_AREA_NAME_DEF;
                    PROXY_AREA = PROXY_AREA_DEF;
                } else {
                    PROXY_AREA_NAME = PROXY_AREA_NAME + "," + PROXY_AREA_NAME_DEF;
                    PROXY_AREA = PROXY_AREA + "," + PROXY_AREA_DEF;
                }
            }
            TmpTtProxyAreaPO PO = new TmpTtProxyAreaPO();
            PO.setDealerId(Long.valueOf(dealerId));
            factory.delete(PO);
            if (!PROXY_AREA.equals("")) {

                String[] proxyArea = PROXY_AREA.split(",");
                String[] proxyAreaName = PROXY_AREA_NAME.split(",");

                for (int j = 0; j < proxyArea.length; j++) {

                    TmpTtProxyAreaPO areaPO = new TmpTtProxyAreaPO();
                    areaPO.setDealerId(Long.valueOf(dealerId));
                    areaPO.setCreateBy(logonUser.getUserId());
                    areaPO.setId(Long.parseLong(SequenceManager.getSequence("")));
                    areaPO.setCreateDate(new Date());
                    areaPO.setProxyArea(proxyArea[j]);
                    areaPO.setProxyAreaName(proxyAreaName[j]);
                    factory.insert(areaPO);
                }
            }
            if (dealerLevel.equals(Constant.DEALER_LEVEL_01.toString())) {
                factory.update("update tmp_tm_dealer set parent_dealer_d = null where dealer_id = " + dealerId, null);
            }
            act.setOutData("message", "操作成功!");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    public void queryDealerCsInfoChangeAuditInit1() {
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
            
            act.setOutData("isFac", "no");
            
            act.setForword(dealerChangeInfoAudit);
            } catch (Exception e) {
                BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护pre查询");
                logger.error(logonUser,e1);
                act.setException(e1);
            }
    }
    
    public void queryDealerCsInfoChangeAuditInit2() {
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
            
            act.setOutData("isFac", "yes");
            
            act.setForword(dealerChangeInfoAudit);
            } catch (Exception e) {
                BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护pre查询");
                logger.error(logonUser,e1);
                act.setException(e1);
            }
    }
    
    public void queryServiceDealerChangeInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            map.put("isFac", CommonUtils.checkNull(request.getParamValue("isFac")));
            map.put("isQuery", CommonUtils.checkNull(request.getParamValue("isQuery")));
            
            map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
            map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            map.put("DEALER_LEVEL", CommonUtils.checkNull(request.getParamValue("DEALERLEVEL")));
            map.put("DEALER_STATUS", CommonUtils.checkNull(request.getParamValue("DEALERSTATUS")));
            map.put("ORGCODE", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
            map.put("parentOrgCode", CommonUtils.checkNull(request.getParamValue("parentOrgCode")));
            map.put("sJDealerCode", CommonUtils.checkNull(request.getParamValue("sJDealerCode")));
            map.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            map.put("COMPANY_NAME", CommonUtils.checkNull(request.getParamValue("COMPANY_NAME")));
            map.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));

            map.put("regionId", CommonUtils.checkNull(request.getParamValue("regionId")));
            map.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));// 授权类型
            map.put("orgCode", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("SCREATE_DATE")));// 开始时间
            map.put("ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("ECREATE_DATE")));// 结束时间

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().queryServiceDealerChangeInfo(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    public void queryDealerCsInfoChangeQueryInit() {
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
            act.setForword(dealerChangeInfoQuery);
            } catch (Exception e) {
                BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护pre查询");
                logger.error(logonUser,e1);
                act.setException(e1);
            }
    }
    
    public void queryDealerCsInfoForDealerInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
          ProductManageDao dao = new ProductManageDao();
          List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
          act.setOutData("areaList", areaList);
          List<Map<String, Object>> brand = dao.getbrandList();// 品牌
                List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
                act.setOutData("orglist", orgList);
                act.setOutData("brand", brand);
                act.setForword(dealerCsInfoForDealerInit);
            } catch (Exception e) {
                BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商维护pre查询");
                logger.error(logonUser, e1);
                act.setException(e1);
            }
    }
    
    public void batchAcc() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TmpTmDealerPO dealerPO1 = new TmpTmDealerPO();
            TmpTmDealerPO dealerPO2 = new TmpTmDealerPO();
            String dealerId = CommonUtils.checkNull(request.getParamValue("id"));
            String isFac = CommonUtils.checkNull(request.getParamValue("isFac"));
            String [] dealerIds = dealerId.split(",");
            ProductManageDao dao = new ProductManageDao();
            for (int i = 0; i < dealerIds.length; i++) {
                dealerPO1.setDealerId(new Long(dealerIds[i]));
                dealerPO1.setDeleteFlag("0");
                if(isFac.equals("yes")){
                    act.setOutData("isFac", "yes");
//                  dealerPO2.setChangeAutidStatus(Constant.SERVICE_CHANGE_STATUS_03);
                    //审核通过 修改 经销商表和经销商明细表
                    settmDealer(dealerIds[i]);
                    settmDealerDetail(dealerIds[i]);
                    setttProxyArea(dealerIds[i]);
                }else{
                    act.setOutData("isFac", "no");
                    dealerPO2.setChangeAutidStatus(Constant.SERVICE_CHANGE_STATUS_02);
                    dao.update(dealerPO1, dealerPO2);
                }
            }
            act.setForword(dealerChangeInfoAudit);
            } catch (Exception e) {
                BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商维护pre查询");
                logger.error(logonUser,e1);
                act.setException(e1);
            }
    }

    private void setttProxyArea(String DealerId) {
        TmpTtProxyAreaPO areaPO = new TmpTtProxyAreaPO(); 
        areaPO.setDealerId(new Long(DealerId));
        TtProxyAreaPO areaPO2 = new TtProxyAreaPO();
        areaPO2.setDealerId(new Long(DealerId));
        factory.delete(areaPO2);
        List<TmpTtProxyAreaPO> list = factory.select(areaPO);
        for (int i = 0; i < list.size(); i++) {
            areaPO2.setDealerId(list.get(i).getDealerId());
            areaPO2.setCreateBy(list.get(i).getCreateBy());
            areaPO2.setCreateDate(list.get(i).getCreateDate());
            areaPO2.setId(list.get(i).getId());
            areaPO2.setProxyArea(list.get(i).getProxyArea());
            areaPO2.setProxyAreaName(list.get(i).getProxyAreaName());
            factory.insert(areaPO2);
        }
        TmpTtProxyAreaPO areaPO3 = new TmpTtProxyAreaPO(); 
        areaPO3.setDeleteFlag("1");
        factory.update(areaPO,areaPO3);
    }

    public void batchRe() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TmpTmDealerPO dealerPO1 = new TmpTmDealerPO();
            TmpTmDealerPO dealerPO2 = new TmpTmDealerPO();
            String dealerId = CommonUtils.checkNull(request.getParamValue("id"));
            String isFac = CommonUtils.checkNull(request.getParamValue("isFac"));
            String[] dealerIds = dealerId.split(",");
            ProductManageDao dao = new ProductManageDao();
            for (int i = 0; i < dealerIds.length; i++) {
                dealerPO1.setDealerId(new Long(dealerIds[i]));
                if (isFac.equals("yes")) {
                    act.setOutData("isFac", "yes");
                    dealerPO2.setChangeAutidStatus(Constant.SERVICE_CHANGE_STATUS_04);
                } else {
                    act.setOutData("isFac", "no");
                    dealerPO2.setChangeAutidStatus(Constant.SERVICE_CHANGE_STATUS_04);
                }
                dao.update(dealerPO1, dealerPO2);
            }

            act.setForword(dealerChangeInfoAudit);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护pre查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    private void settmDealerDetail(String DealerId) {
        TmpTmDealerDetailPO dealerDetailPO = new TmpTmDealerDetailPO(); 
        dealerDetailPO.setFkDealerId(new Long(DealerId));
        ProductManageDao dao = new ProductManageDao();
        dealerDetailPO = (TmpTmDealerDetailPO) dao.select(dealerDetailPO).get(0);
        HashMap<String, Object> mapB = new HashMap<String, Object>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mapB.put("COMPANY_ZC_CODE", dealerDetailPO.getCompanyZcCode());
        mapB.put("WEBMASTER_NAME", dealerDetailPO.getWebmasterName());
        mapB.put("WEBMASTER_PHONE", dealerDetailPO.getWebmasterPhone());
        mapB.put("WEBMASTER_TELPHONE", dealerDetailPO.getWebmasterTelphone());
        mapB.put("WEBMASTER_EMAIL", dealerDetailPO.getWebmasterEmail());
        mapB.put("MARKET_NAME", dealerDetailPO.getMarketName());
        mapB.put("MARKET_PHONE", dealerDetailPO.getMarketTelphone());
        mapB.put("MARKET_TELPHONE", dealerDetailPO.getMarketTelphone());
        mapB.put("MARKET_EMAIL", dealerDetailPO.getMarketEmail());
        mapB.put("MANAGER_NAME", dealerDetailPO.getManagerName());
        mapB.put("MANAGER_PHONE", dealerDetailPO.getManagerPhone());
        mapB.put("MANAGER_TELPHONE", dealerDetailPO.getManagerTelphone());
        mapB.put("MANAGER_EMAIL", dealerDetailPO.getManagerEmail());
        mapB.put("SER_MANAGER_NAME", dealerDetailPO.getSerManagerName());
        mapB.put("SER_MANAGER_PHONE", dealerDetailPO.getSerManagerPhone());
        mapB.put("SER_MANAGER_TELPHONE", dealerDetailPO.getSerManagerTelphone());
        mapB.put("SER_MANAGER_EMAIL", dealerDetailPO.getSerManagerEmail());
        mapB.put("CLAIM_DIRECTOR_NAME", dealerDetailPO.getClaimDirectorName());
        mapB.put("CLAIM_DIRECTOR_PHONE", dealerDetailPO.getClaimDirectorPhone());
        mapB.put("CLAIM_DIRECTOR_TELPHONE", dealerDetailPO.getClaimDirectorTelphone());
        mapB.put("CLAIM_DIRECTOR_EMAIL", dealerDetailPO.getClaimDirectorEmail());
        mapB.put("CLAIM_DIRECTOR_FAX", dealerDetailPO.getClaimDirectorFax());
        mapB.put("CUSTSER_DIRECTOR_NAME", dealerDetailPO.getCustserDirectorName());
        mapB.put("CUSTSER_DIRECTOR_PHONE", dealerDetailPO.getCustserDirectorPhone());
        mapB.put("CUSTSER_DIRECTOR_TELPHONE", dealerDetailPO.getCustserDirectorTelphone());
        mapB.put("CUSTSER_DIRECTOR_EMAIL", dealerDetailPO.getCustserDirectorEmail());
        mapB.put("SER_DIRECTOR_NAME", dealerDetailPO.getSerDirectorName());
        mapB.put("SER_DIRECTOR_PHONE", dealerDetailPO.getSerDirectorPhone());
        mapB.put("SER_DIRECTOR_TELHONE", dealerDetailPO.getSerDirectorTelhone());
        mapB.put("TECHNOLOGY_DIRECTOR_NAME", dealerDetailPO.getTechnologyDirectorName());
        mapB.put("TECHNOLOGY_DIRECTOR_TELPHONE", dealerDetailPO.getTechnologyDirectorTelphone());
        mapB.put("WORKSHOP_DIRECTOR_NAME", dealerDetailPO.getWorkshopDirectorName());
        mapB.put("WORKSHOP_DIRECTOR_TELPHONE", dealerDetailPO.getWorkshopDirectorTelphone());
        mapB.put("FINANCE_MANAGER_NAME", dealerDetailPO.getFinanceManagerName());
        mapB.put("FINANCE_MANAGER_PHONE", dealerDetailPO.getFinanceManagerPhone());
        mapB.put("FINANCE_MANAGER_TELPHONE", dealerDetailPO.getFinanceManagerTelphone());
        mapB.put("FINANCE_MANAGER_EMAIL", dealerDetailPO.getFinanceManagerEmail());
        mapB.put("MESSAGER_NAME", dealerDetailPO.getMessagerName());
        mapB.put("MESSAGER_PHONE", dealerDetailPO.getMessagerPhone());
        mapB.put("MESSAGER_TELPHONE", dealerDetailPO.getMessagerTelphone());
        mapB.put("MESSAGER_QQ", dealerDetailPO.getMessagerQq());
        mapB.put("MESSAGER_EMAIL", dealerDetailPO.getMessagerEmail());
        if(dealerDetailPO.getViApplayDate()!=null){
            mapB.put("VI_APPLAY_DATE", format.format(dealerDetailPO.getViApplayDate()));
        }
        
        if(dealerDetailPO.getViBeginDate()!=null){
            mapB.put("VI_BEGIN_DATE", format.format(dealerDetailPO.getViBeginDate()));
        }
        
        if(dealerDetailPO.getViCompletedDate()!=null){
            mapB.put("VI_COMPLETED_DATE", format.format(dealerDetailPO.getViCompletedDate()));
        }
        
        if(dealerDetailPO.getViConfrimDate()!=null){
            mapB.put("VI_CONFRIM_DATE", format.format(dealerDetailPO.getViConfrimDate()));
        }
        
        mapB.put("IMAGE_LEVEL", dealerDetailPO.getImageLevel());
        mapB.put("IMAGE_COMFIRM_LEVEL", dealerDetailPO.getImageComfirmLevel());
        mapB.put("VI_SUPPORT_AMOUNT", dealerDetailPO.getViSupportAmount());
        mapB.put("VI_SUPPORT_RATIO", dealerDetailPO.getViSupportRatio());
        mapB.put("VI_SUPPORT_TYPE", dealerDetailPO.getViSupportType());
        if(dealerDetailPO.getViSupportDate()!=null){
            
            mapB.put("VI_SUPPORT_DATE", format.format(dealerDetailPO.getViSupportDate()));
        }
        if(dealerDetailPO.getViSupportEndDate()!=null){
            
            mapB.put("VI_SUPPORT_END_DATE", format.format(dealerDetailPO.getViSupportEndDate()));
        }
        if(dealerDetailPO.getFirstSubDate()!=null){
            
            mapB.put("FIRST_SUB_DATE", format.format(dealerDetailPO.getFirstSubDate()));
        }
        if(dealerDetailPO.getFirstGetcarDate()!=null){
            
            mapB.put("FIRST_GETCAR_DATE", format.format(dealerDetailPO.getFirstGetcarDate()));
        }
        if(dealerDetailPO.getFirstSaelsDate()!=null){
            
            mapB.put("FIRST_SAELS_DATE", format.format(dealerDetailPO.getFirstSaelsDate()));
        }
        mapB.put("UNION_TYPE", dealerDetailPO.getUnionType());
        mapB.put("FIXED_CAPITAL", dealerDetailPO.getFixedCapital());
        mapB.put("REGISTERED_CAPITAL", dealerDetailPO.getRegisteredCapital());
        mapB.put("PEOPLE_NUMBER", dealerDetailPO.getPeopleNumber());
        mapB.put("OFFICE_AREA", dealerDetailPO.getOfficeArea());
        mapB.put("PARTS_AREA", dealerDetailPO.getPartsArea());
        mapB.put("DEPOT_AREA", dealerDetailPO.getDepotArea());
        mapB.put("MAIN_AREA", dealerDetailPO.getMainArea());
        mapB.put("ONLY_MONTH_COUNT", dealerDetailPO.getOnlyMonthCount());
        mapB.put("OPENING_TIME", dealerDetailPO.getOpeningTime());
        mapB.put("WORK_TYPE", dealerDetailPO.getWorkType());
        mapB.put("IS_FOUR_S", dealerDetailPO.getIsFourS());
        mapB.put("IS_LOW_SER", dealerDetailPO.getIsLowSer());
        mapB.put("COMPANY_ADDRESS", dealerDetailPO.getCompanyAddress());
        mapB.put("AUTHORIZATION_TYPE", dealerDetailPO.getAuthorizationType());
        if(dealerDetailPO.getAuthorizationDate()!=null){
            
            mapB.put("AUTHORIZATION_DATE", format.format(dealerDetailPO.getAuthorizationDate()));
        }
        mapB.put("IS_ACTING_BRAND", dealerDetailPO.getIsActingBrand());
        mapB.put("ACTING_BRAND_NAME", dealerDetailPO.getActingBrandName());
        mapB.put("PARTS_STORE_AMOUNT", dealerDetailPO.getPartsStoreAmount());
        mapB.put("SHOP_TYPE", dealerDetailPO.getShopType());
        mapB.put("HOTLINE", dealerDetailPO.getHotline());
        mapB.put("FAX_NO", dealerDetailPO.getFaxNo());
        mapB.put("EMAIL", dealerDetailPO.getEmail());
        mapB.put("IS_AUTHORIZE_CITY", dealerDetailPO.getIsAuthorizeCity());
        mapB.put("IS_AUTHORIZE_COUNTY", dealerDetailPO.getIsAuthorizeCounty());
        mapB.put("AUTHORIZE_BRAND", dealerDetailPO.getAuthorizeBrand());
        if(dealerDetailPO.getAuthorizeGetDate()!=null){
            
            mapB.put("AUTHORIZE_GET_DATE", format.format(dealerDetailPO.getAuthorizeGetDate()));
        }
        if(dealerDetailPO.getAuthorizeSubDate()!=null){
            
            mapB.put("AUTHORIZE_SUB_DATE", format.format(dealerDetailPO.getAuthorizeSubDate()));
        }
        if(dealerDetailPO.getAuthorizeEffectDate()!=null){
            
            mapB.put("AUTHORIZE_EFFECT_DATE", format.format(dealerDetailPO.getAuthorizeEffectDate()));
        }
            
            mapB.put("ANNOUNCEMENT_NO ", dealerDetailPO.getAnnouncementNo());
        if(dealerDetailPO.getAnnouncementDate()!=null){
            
            mapB.put("ANNOUNCEMENT_DATE", format.format(dealerDetailPO.getAnnouncementDate()));
        }
        if(dealerDetailPO.getAnnouncementEndDate()!=null){
            
            mapB.put("ANNOUNCEMENT_END_DATE", format.format(dealerDetailPO.getAnnouncementEndDate()));
        }
        mapB.put("THE_AGENTS", dealerDetailPO.getTheAgents());
        mapB.put("PROXY_VEHICLE_TYPE", dealerDetailPO.getProxyVehicleType());
        mapB.put("FITTINGS_DEC_PHONE", dealerDetailPO.getFittingsDecPhone());
        mapB.put("FITTINGS_DEC_NAME", dealerDetailPO.getFittingsDecName());
        mapB.put("FITTINGS_DEC_EMAIL", dealerDetailPO.getFittingsDecEmail());
        mapB.put("FITTINGS_DEC_TELPHONE", dealerDetailPO.getFittingsDecTelphone());
        mapB.put("FITTINGS_DEC_FAX", dealerDetailPO.getFittingsDecFax());
        StringBuffer sbSql = new StringBuffer();
        TmDealerDetailPO tddp = new TmDealerDetailPO();
        tddp.setFkDealerId(Long.parseLong(DealerId));
        List<Object> params = new ArrayList<Object>();
        List dList = factory.select(tddp);
        if (dList.size() > 0) {
            sbSql.append("update tm_dealer_detail set update_date=sysdate");
            for (String key : mapB.keySet()) {
                if (!"".equals(mapB.get(key))) {
                    sbSql.append("," + key + " = ");
                    getDateBuffer(key, sbSql);
                    params.add(mapB.get(key));
                }
            }
            sbSql.append(" where fk_dealer_id = ?");
            params.add(DealerId);

            factory.update(sbSql.toString(), params);
        } else {
            sbSql.append("insert into tm_dealer_detail(detail_id, fk_dealer_id");
            for (String key : mapB.keySet()) {
                if (!"".equals(mapB.get(key))) {
                    sbSql.append("," + key);
                }
            }
            sbSql.append(") values (f_getid,?");
            params.add(DealerId);
            for (String key : mapB.keySet()) {
                if (!"".equals(mapB.get(key))) {
                    sbSql.append(",");
                    getDateBuffer(key, sbSql);
                    params.add(mapB.get(key));
                }
            }
            sbSql.append(")");
            factory.insert(sbSql.toString(), params);
        }
        
        TmpTmDealerDetailPO tmpTmDealerPO1 = new TmpTmDealerDetailPO(); 
        TmpTmDealerDetailPO tmpTmDealerPO2 = new TmpTmDealerDetailPO(); 
        tmpTmDealerPO1.setFkDealerId((new Long(DealerId)));
        tmpTmDealerPO2.setDeleteFlag("1");
        factory.update(tmpTmDealerPO1,tmpTmDealerPO2);
    }

    private void settmDealer(String dealerId) {
        TmpTmDealerPO tmpTmDealerPO = new TmpTmDealerPO(); 
        tmpTmDealerPO.setDealerId(new Long(dealerId));
        HashMap<String, Object> mapA = new HashMap<String, Object>();
        ProductManageDao dao = new ProductManageDao();
        tmpTmDealerPO = (TmpTmDealerPO) dao.select(tmpTmDealerPO).get(0);
        
//      mapA.put("COMPANY_ID", companyId);
//      mapA.put("DEALER_CODE", dealerCode);
//      mapA.put("DEALER_ORG_ID", dealerOrgId);
//      mapA.put("DEALER_LEVEL", dealerLevel);

//      mapA.put("DEALER_TYPE", dealerType);
        mapA.put("BALANCE_LEVEL", tmpTmDealerPO.getBalanceLevel());
        mapA.put("INVOICE_LEVEL", tmpTmDealerPO.getInvoiceLevel());

//      mapA.put("DEALER_SHORTNAME", tmpTmDealerPO);
//      mapA.put("DEALER_NAME", );
        mapA.put("SERVICE_STATUS", tmpTmDealerPO.getServiceStatus());
        mapA.put("PROVINCE_ID", tmpTmDealerPO.getProvinceId());
        mapA.put("CITY_ID", tmpTmDealerPO.getCityId());
        mapA.put("COUNTIES", tmpTmDealerPO.getCounties());
        mapA.put("ZIP_CODE", tmpTmDealerPO.getZipCode());
        mapA.put("ADDRESS", tmpTmDealerPO.getAddress());
        mapA.put("ZCCODE", tmpTmDealerPO.getZccode());
        mapA.put("ZY_SCOPE", tmpTmDealerPO.getZyScope());
        mapA.put("JY_SCOPE", tmpTmDealerPO.getJyScope());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(tmpTmDealerPO.getDestroydate()!=null){
            mapA.put("SITEDATE", format.format(tmpTmDealerPO.getSitedate()));
        }
        
        if(tmpTmDealerPO.getDestroydate()!=null){
            mapA.put("DESTROYDATE", format.format(tmpTmDealerPO.getDestroydate()));
        }
        mapA.put("LEGAL", tmpTmDealerPO.getLegal());
        mapA.put("LEGAL_PHONE", tmpTmDealerPO.getLegalPhone());
        mapA.put("LEGAL_TELPHONE", tmpTmDealerPO.getLegalTelphone());
        mapA.put("LEGAL_EMAIL", tmpTmDealerPO.getLegalEmail());
        mapA.put("INVOICE_PERSION", tmpTmDealerPO.getInvoicePersion());
        mapA.put("INVOICE_TELPHONE", tmpTmDealerPO.getInvoiceTelphone());
        mapA.put("BEGIN_BANK", tmpTmDealerPO.getBeginBank());
        mapA.put("ERP_CODE", tmpTmDealerPO.getErpCode());
        mapA.put("TAXES_NO", tmpTmDealerPO.getTaxesNo());
        mapA.put("INVOICE_ACCOUNT", tmpTmDealerPO.getInvoiceAccount());
        mapA.put("INVOICE_PHONE", tmpTmDealerPO.getInvoicePhone());
        mapA.put("INVOICE_ADD", tmpTmDealerPO.getInvoicePostAdd());
        mapA.put("TAXPAYER_NO", tmpTmDealerPO.getTaxpayerNo());
        mapA.put("TAXPAYER_NATURE", tmpTmDealerPO.getTaxpayerNature());
        mapA.put("TAX_INVOICE", tmpTmDealerPO.getTaxInvoice());
        mapA.put("TAX_DISRATE", tmpTmDealerPO.getTaxDisrate());
        mapA.put("REMARK", tmpTmDealerPO.getRemark());
        mapA.put("MAIN_RESOURCES", tmpTmDealerPO.getMainResources());
        StringBuffer sbSql = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
        sbSql.append("update tm_dealer set update_date = sysdate");
        for (String key : mapA.keySet()) {
            if (!"".equals(mapA.get(key))) {
                sbSql.append("," + key + " = ");
                getDateBuffer(key, sbSql);
                params.add(mapA.get(key));
            }
        }
        sbSql.append(" where dealer_id = ?");
        params.add(dealerId);
        factory.update(sbSql.toString(), params);
        
        TmpTmDealerPO tmpTmDealerPO1 = new TmpTmDealerPO(); 
        TmpTmDealerPO tmpTmDealerPO2 = new TmpTmDealerPO(); 
        tmpTmDealerPO1.setDealerId(new Long(dealerId));
        tmpTmDealerPO2.setDeleteFlag("1");
        tmpTmDealerPO2.setChangeAutidStatus(Constant.SERVICE_CHANGE_STATUS_03);
        factory.update(tmpTmDealerPO1,tmpTmDealerPO2);
    }
    
    public void queryDealerInfoForDealer() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            map.put("parentOrgCode", CommonUtils.checkNull(request.getParamValue("parentOrgCode")));// 上级组织
            map.put("orgCode", CommonUtils.checkNull(request.getParamValue("orgCode")));// 大区
            map.put("regionCode", CommonUtils.checkNull(request.getParamValue("regionCode")));
            map.put("AUTHORIZATION_TYPE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE")));// 授权类型
            map.put("WORK_TYPE", CommonUtils.checkNull(request.getParamValue("WORK_TYPE")));// 经营类型
            map.put("IMAGE_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_LEVEL")));// 形象等级
            map.put("IMAGE_COMFIRM_LEVEL", CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL")));// 形象等级
            map.put("regionId", CommonUtils.checkNull(request.getParamValue("regionId")));

            map.put("SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("SCREATE_DATE")));// 开始时间
            map.put("ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("ECREATE_DATE")));// 结束时间

            map.put("AUTHORIZATION_SCREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_SCREATE_DATE")));// 授权开始时间
            map.put("AUTHORIZATION_ECREATE_DATE", CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_ECREATE_DATE")));// 授权结束时间

            map.put("DEALER_ID", CommonUtils.checkNull(request.getParamValue("DEALER_ID")));
            map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
            map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
            map.put("DEALER_LEVEL", CommonUtils.checkNull(request.getParamValue("DEALERLEVEL")));
            map.put("DEALER_STATUS", CommonUtils.checkNull(request.getParamValue("DEALERSTATUS")));
            map.put("ORGCODE", CommonUtils.checkNull(request.getParamValue("orgCode")));
            map.put("orgId", CommonUtils.checkNull(request.getParamValue("orgId")));
            map.put("sJDealerCode", CommonUtils.checkNull(request.getParamValue("sJDealerCode")));
            map.put("DEALER_TYPE", CommonUtils.checkNull(request.getParamValue("DEALER_TYPE")));
            map.put("COMPANY_NAME", CommonUtils.checkNull(request.getParamValue("COMPANY_NAME")));
            map.put("SERVICE_STATUS", CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS")));// 经销商状态

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = DealerInfoDao.getInstance().queryDealerInfoForDealer(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    public void checkEx() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            RequestWrapper request = act.getRequest();

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            act.setOutData("checkEx", DealerInfoDao.getInstance().checkEx(logonUser.getDealerId()));
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商维护查询结果");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 形象店建设-销售部审核-批量审核
     */
    public void doBatchAudit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String yearFlagSuvs = CommonUtils.checkNull(request.getParamValue("yearFlagSuvs"));
            String yearFlagMpvs = CommonUtils.checkNull(request.getParamValue("yearFlagMpvs"));
            String auditResultFlag = CommonUtils.checkNull(request.getParamValue("auditResultFlag"));
            String dealerIds = CommonUtils.checkNull(request.getParamValue("dealerIds"));
            
            if (StringUtils.isEmpty(dealerIds) || dealerIds.split(",").length == 0) {
                throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "审核数据提交失败,请重新提交!");
            }

            String[] dealerIdArray = dealerIds.split(",");
            String[] yearFlagSuv = yearFlagSuvs.split(",");
            String[] yearFlagMpv = yearFlagMpvs.split(",");
            TtViConstructDetailPO po1 = null;   
            TtViConstructDetailPO po2 = null;
            Integer auditStatus = auditResultFlag.equals("pass") ? Constant.VI_CONSTRUCT_STATUS_02 : Constant.VI_CONSTRUCT_STATUS_03;
            for (int i = 0; i < dealerIdArray.length; i++) {
                po1 = new TtViConstructDetailPO();
                po2 = new TtViConstructDetailPO();
                // SUV审核状态更新
                if (!yearFlagSuv[i].equals("0")) {
                    
                    // 形象店明细ID取得
                    String detailId = DealerInfoDao.getInstance().getDetailIdByVehicleSeriesId("2014032694231206", dealerIdArray[i], yearFlagSuv[i]);
                    po1.setDetailId(Long.parseLong(detailId));
                    
                    // 审核数据保存
                    saveTtViConstructAudit(dealerIdArray[i], auditStatus, logonUser.getUserId(), yearFlagSuv[i], detailId);
                    po2.setStatus(auditStatus);
                    po2.setUserId(Constant.VI_CONSTRUCT_AUDIT_03);
                    po2.setDetailId(Long.parseLong(detailId));
                    DealerInfoDao.getInstance().update(po1, po2);
                }
                
                // MPV审核状态更新
                if (!yearFlagMpv[i].equals("0")) {

                    // 形象店明细ID取得
                    String detailId = DealerInfoDao.getInstance().getDetailIdByVehicleSeriesId("2015011508783002", dealerIdArray[i], yearFlagMpv[i]);
                    po1.setDetailId(Long.parseLong(detailId));
                    
                    // 审核明细保存
                    saveTtViConstructAudit(dealerIdArray[i], auditStatus, logonUser.getUserId(), yearFlagMpv[i], detailId);
                    po2.setStatus(auditStatus);
                    po2.setUserId(Constant.VI_CONSTRUCT_AUDIT_03);
                    po2.setDetailId(Long.parseLong(detailId));
                    DealerInfoDao.getInstance().update(po1, po2);
                }
            }
            
            act.setRedirect("/sysmng/dealer/DealerInfo/ConstructAuditInit2.do");
        } catch (BizException e) {
            logger.error(logonUser, e);
            act.setException(e);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "形象店支持维护审核状态");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 审核明细保存
     * @return detailId
     */
    private void saveTtViConstructAudit(String dealerId, Integer auditStatus, Long userid, String yearFlag, String detailId) {
        TtViConstructAuditPO po1 = new TtViConstructAuditPO();
        String id = SequenceManager.getSequence("");
        po1.setId(Long.parseLong(id));
        po1.setDetailId(Long.parseLong(detailId));
        po1.setDealerId(Long.parseLong(dealerId));
        po1.setStatus(auditStatus);
        po1.setCreateBy(userid);
        po1.setCreateDate(new Date());
        po1.setUserId(Constant.VI_CONSTRUCT_AUDIT_03);
        po1.setYearFlag(yearFlag);
        DealerInfoDao.getInstance().insert(po1);
    }
    
    /**
     * 验证形象店保存
     */
    public void singleSaveViForAfterSubmit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String IMAGE_COMFIRM_LEVEL = CommonUtils.checkNull(request.getParamValue("IMAGE_COMFIRM_LEVEL"));
            String mainId = CommonUtils.checkNull(request.getParamValue("mainId"));
            
            TtViConstructMainPO po1 = new TtViConstructMainPO();
            TtViConstructMainPO po2 = new TtViConstructMainPO();
            po1.setId(Long.parseLong(mainId));
            List<PO> list = DealerInfoDao.getInstance().select(po1);
            if (list == null || list.isEmpty()) {
                throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "形象店信息不存在");
            }
            
            po2.setId(Long.parseLong(mainId));
            po2.setCheckedImageLevel(Integer.parseInt(IMAGE_COMFIRM_LEVEL));
            DealerInfoDao.getInstance().update(po1, po2);
        } catch (BizException e) {
            logger.error(logonUser, e);
            act.setException(e);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "验证形象店");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}