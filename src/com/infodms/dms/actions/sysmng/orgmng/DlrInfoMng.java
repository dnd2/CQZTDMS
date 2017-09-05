/**********************************************************************
* <pre>
* FILE : DlrInfoMng.java
* CLASS : DlrInfoMng
*
* AUTHOR : LAX
*
* FUNCTION : 经销商信息维护.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-18| LAX  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: DlrInfoMng.java,v 1.6 2010/12/01 03:51:49 yangz Exp $
 */
package com.infodms.dms.actions.sysmng.orgmng;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.dao.orgmng.CompanyInfoDAO;
import com.infodms.dms.dao.orgmng.DlrInfoMngDAO;
import com.infodms.dms.dao.orgmng.SgmOrgMngDAO;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.exception.UserException;
import com.infodms.dms.po.TcCompanyLogPO;
import com.infodms.dms.po.TmBrandPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.Validate;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.infox.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  经销商信息维护
 * @author        :  LAX
 * CreateDate     :  2009-08-18
 * @version       :  0.1
 */
public class DlrInfoMng {
	private Logger logger = Logger.getLogger(DlrInfoMng.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private static final CheckVehicleDAO dao = CheckVehicleDAO.getInstance();
	/**
	 * Function       :  根据条件查询系统中经销商明细信息 
	 * @param         :  request-经销商ID
	 * @return        :  经销商明细
	 * @throws        :  Exception
	 * LastUpdate     :  2009-08-18
	 */
	public void querySingleDlrInfo() throws Exception {
		Long dealerId = null;          //经销商ID
		String flag = "";
		try {
			RequestWrapper request = act.getRequest();
			if(request.getParamValue("dlrId") == null){
				dealerId = logonUser.getCompanyId();
			}else{
				flag = "1";
				dealerId = new Long(request.getParamValue("dlrId"));
			}
			
			//得到认证品牌
			List<TmBrandPO> sgmlist = CompanyInfoDAO.getSGMBrand();
//			//得到经销商对应的SGM认证品牌
//			List<TrDlrBrandPO> dlrsgmlist = DlrInfoDAO.getDlrSGMBrand(dealerId);
			//得到经销商明细信息
			CompanyBean bean = new CompanyBean();
			List<CompanyBean> companyList = CompanyInfoDAO.getCompanyInfoItem(dealerId);
			bean = companyList.get(0);
			
			act.setOutData("flag", flag);
			act.setOutData("SGM_LIST", sgmlist);
//			act.setOutData("DLR_SGM_LIST", dlrsgmlist);
			act.setOutData("DEALER_BEAN", bean);
			
			act.setForword("/jsp/systemMng/orgMng/dealerInfoDetail.jsp");
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商明细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  经销商信息维护－根据条件查询系统中所有经销商信息 
	 * @param         :  request-经销商代码、经销商名称、公司类型、所属部门ID、排序字段名、排序参数
	 * @return        :  系统中经销商明细
	 * @throws        :  Exception
	 * LastUpdate     :  2009-08-18
	 */
	public void queryAllDlrInfo() throws Exception {
		String companyCode = null;        //公司代码
		String companyName = null;        //公司名称
		String companyType = null;       //公司类型
		String oemCompanyId=null;        //公司ID
		String status = null;

		try {
			RequestWrapper request = act.getRequest();
			if("1".equals(request.getParamValue("command"))){ //json请求
				/**
				* 得到页面参数
				*/
				companyCode = CommonUtils.checkNull(request.getParamValue("COMPANY_CODE"));	
				companyName = CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));
				status = CommonUtils.checkNull(request.getParamValue("STATUS"));
				//companyType = request.getParamValue("COMPANY_TYPE");
				//这里写死为经销商公司
				companyType=Constant.COMPANY_TYPE_DEALER;
				// 后台验证经销商代码、经销商名称
				oemCompanyId=logonUser.getCompanyId().toString();
				List<MsgCarrier> list = new ArrayList<MsgCarrier>();
				list.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"公司代码",0,Constant.Length_Check_Char_10,companyCode));
				list.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"公司名称",0,Constant.Length_Check_Char_30,companyName));
//				Validate.doValidate(act, list);
				//获取排序字段和排序类型
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
				
				Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				//得到系统中所有经销商
				PageResult<CompanyBean> ps = CompanyInfoDAO.getAllCompanyInfo(oemCompanyId,companyCode, companyName, companyType, status,  ActionUtil.getPageSize(request), curPage,orderName,da);
				act.setOutData("ps", ps);
				ActionUtil.setCustomPageSizeFlag(act, true);
			}
			act.setForword("/jsp/systemMng/orgMng/dealerInfo.jsp");			
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  经销商信息查询－根据条件查询系统中所有经销商信息 
	 * @param         :  request-经销商代码、经销商名称、公司类型、所属部门ID、排序字段名、排序参数
	 * @return        :  系统中经销商明细
	 * @throws        :  Exception
	 * LastUpdate     :  2009-08-18
	 */
	public void dlrInfoSearch() throws Exception {
		String dealerCode = null;        //经销商代码
		String dealerName = null;        //经销商名称
		String companyType = null;       //公司类型
		String oemCompanyId=null;        //车厂公司ID
		
		PageResult<CompanyBean> ps = null;
		try {
			RequestWrapper request = act.getRequest();
			if("1".equals(request.getParamValue("command"))){ //json请求
				/**
				* 得到页面参数
				*/
				dealerCode = CommonUtils.checkNull(request.getParamValue("DLR_CODE"));	
				dealerName = CommonUtils.checkNull(request.getParamValue("DLR_NAME"));
				companyType = request.getParamValue("COMPANY_TYPE");
				if(logonUser.getOrgType().toString().equals(Constant.ORG_TYPE_OEM)){
					oemCompanyId=logonUser.getCompanyId().toString();
				}else{
					oemCompanyId=logonUser.getOemCompanyId();
				}
				
				// 后台验证经销商代码、经销商名称
				List<MsgCarrier> list = new ArrayList<MsgCarrier>();
				list.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"经销商代码",0,Constant.Length_Check_Char_10,dealerCode));
				list.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"经销商名称",0,Constant.Length_Check_Char_30,dealerName));
				Validate.doValidate(act, list);
				//获取排序字段和排序类型
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
					
				Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				//得到系统中所有经销商
				ps = CompanyInfoDAO.getAllCompanyInfo(oemCompanyId,dealerCode, dealerName, companyType, null, Constant.PAGE_SIZE, curPage,orderName,da);
				act.setOutData("ps", ps);
			}
			act.setForword("/jsp/systemMng/orgMng/dealerInfoSearch.jsp");			
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  查询系统中SGM认证品牌 
	 * @param         :  request
	 * @return        :  SGM认证品牌
	 * @throws        :  Exception
	 * LastUpdate     :  2009-08-19
	 */
	public void queryDlrBrand() throws Exception {
		try {
			//得到SGM认证品牌
			//List<TmBrandPO> list = CompanyInfoDAO.getSGMBrand();
			List<TmBrandPO> list = null;
			act.setOutData("list", list);
			act.setForword("/jsp/systemMng/orgMng/companyInfoAdd.jsp");			
		/*}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);*/
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM认证品牌信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  新增经销商信息
	 * @param         :  request-经销商名称、经销商代码、所属部门ID、经销商简称、省份、城市、开业时间、SAP账号
	 * @param         :  request-公司类型、传真、负责人、电话、手机、地址、邮编、邮箱、状态、品牌ID
	 * @return        :  新增经销商成功或失败
	 * @throws        :  Exception
	 * LastUpdate     :  2009-08-19
	 */
	public void addCompanyInfo() throws Exception {
//		String companyName = null;          //名称
//		String companyCode = null;          //代码
//		String companyShortname = null;  //简称
//		String companyType = null;      //公司类型
//		String fax = null;              //传真
//		String phone = null;          //电话
//		String address = null;         //地址
//		String zipCode = null;          //邮编
//		String status = null;          //状态
//		String province = null;
//		String city = null;
//		String brandId = null;          //品牌ID
//		String[] brandList = null;
		try {
			RequestWrapper request = act.getRequest();
			/**
			 * 得到页面参数
			 */
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			
			String companyId = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
			String companyCode = CommonUtils.checkNull(request.getParamValue("COMPANY_CODE"));
			String companyName = CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));
			String companyShortName = CommonUtils.checkNull(request.getParamValue("COMPANY_SHORTNAME"));
			
			String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));
			String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID"));
			String phone = CommonUtils.checkNull(request.getParamValue("PHONE"));
			String zipCode = CommonUtils.checkNull(request.getParamValue("ZIP_CODE"));
			String fax = CommonUtils.checkNull(request.getParamValue("FAX"));
			String address = CommonUtils.checkNull(request.getParamValue("ADDRESS"));
			
			paramMap.put("COMPANY_ID", companyId);
			paramMap.put("COMPANY_CODE", companyCode);
			paramMap.put("COMPANY_NAME", companyName);
			paramMap.put("COMPANY_SHORTNAME", companyShortName);
			paramMap.put("PROVINCE_ID", provinceId);
			paramMap.put("CITY_ID", cityId);
			paramMap.put("PHONE", phone);
			paramMap.put("ZIP_CODE", zipCode);
			paramMap.put("FAX", fax);
			paramMap.put("ADDRESS", address);
			if(StringUtil.isEmpty(CommonUtils.checkNull(request.getParamValue("STATUS")))){
				paramMap.put("STATUS", "10011002");
			}else{
				paramMap.put("STATUS", CommonUtils.checkNull(request.getParamValue("STATUS")));
			}
			
			
//			companyName = CommonUtils.checkNull(request.getParamValue("DLR_NAME"));
//			companyCode = CommonUtils.checkNull(request.getParamValue("DLR_CODE"));
//			companyShortname = CommonUtils.checkNull(request.getParamValue("DLRNAMEFORSHORT"));
//			fax = CommonUtils.checkNull(request.getParamValue("FAX"));
//			phone = CommonUtils.checkNull(request.getParamValue("CONT_TEL"));
//			address = CommonUtils.checkNull(request.getParamValue("DETL_ADDR"));
//			zipCode = CommonUtils.checkNull(request.getParamValue("ZIP_CODE"));
//			status = CommonUtils.checkNull(request.getParamValue("DLR_STAT"));
//			companyType = CommonUtils.checkNull(request.getParamValue("COMPANY_TYPE"));
//			province = CommonUtils.checkNull(request.getParamValue("PROVINCE"));
//			city = CommonUtils.checkNull(request.getParamValue("CITY"));
//			String IS_SAME_PERSON = CommonUtils.checkNull(request.getParamValue("IS_SAME_PERSON"));  //是否同一法人
//			String IS_BEFORE_AFTER = CommonUtils.checkNull(request.getParamValue("IS_BEFORE_AFTER")); //是否前店后厂
//			String HANDLE_BY_HANDLE = CommonUtils.checkNull(request.getParamValue("HANDLE_BY_HANDLE")); //是否手拉手
//			String IS_WHOLE = CommonUtils.checkNull(request.getParamValue("IS_WHOLE"));  //是否一体化
//			String COMPANY_ID  = CommonUtils.checkNull(request.getParamValue("COMPANY_ID")); //关联公司
			
//			 后台验证经销商代码、经销商名称、经销商简称、负责人、地址、邮编、邮箱
//			List<MsgCarrier> msglist = new ArrayList<MsgCarrier>();
//			msglist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"经销商代码",0,Constant.Length_Check_Char_10,companyCode));
//			msglist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"经销商名称",0,Constant.Length_Check_Char_30,companyName));
//			msglist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"经销商简称",0,Constant.Length_Check_Char_15,companyShortname));
//			msglist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"地址",0,Constant.Length_Check_Char_30,address));
			//Validate.doValidate(act, msglist);
			//从SESSION中查询出oemCompanyId
			Long oemCompanyId=logonUser.getCompanyId();
			//获取排序字段和排序类型
			
//			Enumeration enu = request.getParamNames();
//			while(enu.hasMoreElements()){
//				String brand = (String) enu.nextElement();
//				if(brand.startsWith("BRAND")){
//					brandList = request.getParamValues(brand);
//				}
//			}
			
			//判断新增的经销商信息是否已经存在相应的经销商表中
			List <TmCompanyPO> list = CompanyInfoDAO.getDlr(companyCode, companyId);//去掉经销商公司名称、简称重名判断
			if(list.size() > 0) throw new UserException("公司编码重复!请确认编码是否被占用!");
			
			List <TmCompanyPO> list1 = CompanyInfoDAO.getDlrName(companyName,companyId);//判断名字是否重复
			if(list1.size() > 0) throw new UserException("公司名称重复!请确认公司名称是否被占用!");

			StringBuffer sbSql = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			if(companyId.equals(""))
			{
				companyId = SequenceManager.getSequence("");
				sbSql.append("insert into tm_company(COMPANY_ID,COMPANY_TYPE,OEM_COMPANY_ID");
				for(String key : paramMap.keySet()) {
					System.out.println(key + ":" + paramMap.get(key));
					if(!"".equals(paramMap.get(key))) {
						sbSql.append("," + key);
					}
				}
				sbSql.append(") values (?,?,?");
				params.add(companyId);
				params.add(Constant.COMPANY_TYPE_DEALER);
				params.add(2010010100070674L);
				for(String key : paramMap.keySet()) {
					System.out.println(key + ":" + paramMap.get(key));
					if(!"".equals(paramMap.get(key))) {
						sbSql.append(",?"); params.add(paramMap.get(key));
					}
				}
				sbSql.append(")");
				
				factory.insert(sbSql.toString(), params);
				
				TmOrgPO tmorgPO = new TmOrgPO();
				String orgId=SequenceManager.getSequence("");
				if(orgId!=""&&orgId!=null){
					tmorgPO.setOrgId(new Long(orgId));
				}
				//tmorgPO.setParentOrgId(new Long(orgId));
				tmorgPO.setOrgCode(companyCode);
				tmorgPO.setOrgName(companyShortName);
				tmorgPO.setStatus(Integer.valueOf(Constant.STATUS_ENABLE));
				tmorgPO.setCreateBy(logonUser.getUserId());
				tmorgPO.setCreateDate(new Date(System.currentTimeMillis()));
				if(companyId!=null&&companyId!=""){
					tmorgPO.setCompanyId(new Long(companyId));
				}
				tmorgPO.setOrgType(Integer.valueOf(Constant.ORG_TYPE_DEALER));
				tmorgPO.setDutyType(Constant.DUTY_TYPE_DEALER);
				
				//将新增的部门信息插入部门表
				factory.insert(tmorgPO);
			}
			else
			{
				sbSql.append("update tm_company set UPDATE_DATE = SYSDATE");
				for(String key : paramMap.keySet()) {
					if(!"".equals(paramMap.get(key))) {
						sbSql.append(", " + key + "=?"); 
						params.add(paramMap.get(key));
					}
				}
				sbSql.append(" where COMPANY_ID = ?"); params.add(companyId);
				factory.update(sbSql.toString(), params);
				
				//修改经销商名称(公司变更需修改经销商的 全称，简称)
				StringBuffer sbSqlDealer = new StringBuffer();
				sbSqlDealer.append("update tm_Dealer x\r\n");
				sbSqlDealer.append("   set x.UPDATE_DATE = SYSDATE, x.dealer_name = ?,x.dealer_shortname=?\r\n");
				sbSqlDealer.append(" where x.company_id = ?"); 
				List<Object> paramsDealer = new ArrayList<Object>();
				paramsDealer.add(paramMap.get("COMPANY_NAME"));
				paramsDealer.add(paramMap.get("COMPANY_SHORTNAME"));
				paramsDealer.add(paramMap.get("COMPANY_ID"));
				factory.update(sbSqlDealer.toString(), paramsDealer);
				
				//修改经销商状态
				if(!CommonUtils.checkNull(request.getParamValue("shouhou_STATUS")).equals("")){
					StringBuffer SqlDealer = new StringBuffer();
					SqlDealer.append("update tm_Dealer x\r\n");
					SqlDealer.append("   set x.UPDATE_DATE = SYSDATE, x.STATUS = ");
					if(CommonUtils.checkNull(request.getParamValue("STATUS")).equals("10011001")){
						SqlDealer.append(CommonUtils.checkNull(request.getParamValue("shouhou_STATUS"))); 
					}else{
						SqlDealer.append("10011002"); 
					}
					SqlDealer.append(" where x.company_id = ").append(paramMap.get("COMPANY_ID")); 
					SqlDealer.append(" and x.dealer_type = ").append("10771002");
					factory.update(SqlDealer.toString(), null);
				}
				
				//修改经销商状态
				if(!CommonUtils.checkNull(request.getParamValue("xiaoshou_STATUS")).equals("")){
					StringBuffer SqlDealer_xiaoshou = new StringBuffer();
					SqlDealer_xiaoshou.append("update tm_Dealer x\r\n");
					SqlDealer_xiaoshou.append("   set x.UPDATE_DATE = SYSDATE, x.STATUS = ");
					if(CommonUtils.checkNull(request.getParamValue("STATUS")).equals("10011001")){
						SqlDealer_xiaoshou.append(CommonUtils.checkNull(request.getParamValue("xiaoshou_STATUS"))); 
					}else{
						SqlDealer_xiaoshou.append("10011002"); 
					}
					SqlDealer_xiaoshou.append(" where x.company_id = ").append(paramMap.get("COMPANY_ID")); 
					SqlDealer_xiaoshou.append(" and x.dealer_type = ").append("10771001");
					factory.update(SqlDealer_xiaoshou.toString(), null);
				}
			}
			
			act.setOutData("message", "处理成功!");
		}
		catch(UserException _ex) {
			logger.error(logonUser, _ex);
			BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, _ex.getMessage());
			act.setException(e1);
		}
		catch (Exception _ex)
		{
			BizException e1 = new BizException(act,_ex,ErrorCodeConstant.SPECIAL_MEG, "经销商公司保存出错！");
			logger.error(logonUser, _ex);
			act.setException(e1);
		}
	}
	
	/**
	 * 公司名称更名
	 * @throws Exception
	 */
	public void alterCompanyInfo() throws Exception {
		try {
			RequestWrapper request = act.getRequest();
			/**
			 * 得到页面参数
			 */
			String message = null;
			
			String companyId = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
			
			TcCompanyLogPO log1 = new TcCompanyLogPO();
			log1.setCompanyId(Long.valueOf(companyId));
			log1.setStatus("1");
			if(dao.select(log1).size()>0){
				message="已有更名审核中，请等待审核";
				act.setOutData("message", message);
				return;
			}
			Integer var = 0;
			//取最新版本号
			if(StringUtil.isEmpty(dao.getVar(companyId))){
				var = 1;
			}else{
				var = Integer.valueOf(dao.getVar(companyId)) + 1;
			}
			
			
			String COMPANY_NAME = CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));
			
			String COMPANY_NAME_NEW = CommonUtils.checkNull(request.getParamValue("COMPANY_NAME_NEW"));
			
			String COMPANY_SHORTNAME = CommonUtils.checkNull(request.getParamValue("COMPANY_SHORTNAME"));
			
			String COMPANY_SHORTNAME_NEW = CommonUtils.checkNull(request.getParamValue("COMPANY_SHORTNAME_NEW"));
			
			String REMARK_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("REMARK_XIAOSHOU"));
			
			String INVOICE_PERSION_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("INVOICE_PERSION_XIAOSHOU"));
			String BEGIN_BANK_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("BEGIN_BANK_XIAOSHOU"));
			String INVOICE_TELPHONE_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("INVOICE_TELPHONE_XIAOSHOU"));
			String TAX_INVOICE_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("TAX_INVOICE_XIAOSHOU"));
			String INVOICE_ACCOUNT_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT_XIAOSHOU"));
			String TAXPAYER_NO_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("TAXPAYER_NO_XIAOSHOU"));
			String INVOICE_PHONE_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE_XIAOSHOU"));
			String TAXES_NO_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("TAXES_NO_XIAOSHOU"));
			String ERP_CODE_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("ERP_CODE_XIAOSHOU"));
			String INVOICE_ADD_XIAOSHOU = CommonUtils.checkNull(request.getParamValue("INVOICE_ADD_XIAOSHOU"));
			//旧值
			String INVOICE_PERSION_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("INVOICE_PERSION_XIAOSHOU_OLD"));
			String BEGIN_BANK_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("BEGIN_BANK_XIAOSHOU_OLD"));
			String INVOICE_TELPHONE_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("INVOICE_TELPHONE_XIAOSHOU_OLD"));
			String TAX_INVOICE_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("TAX_INVOICE_XIAOSHOU_OLD"));
			String INVOICE_ACCOUNT_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT_XIAOSHOU_OLD"));
			String TAXPAYER_NO_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("TAXPAYER_NO_XIAOSHOU_OLD"));
			String INVOICE_PHONE_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE_XIAOSHOU_OLD"));
			String TAXES_NO_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("TAXES_NO_XIAOSHOU_OLD"));
			String ERP_CODE_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("ERP_CODE_XIAOSHOU_OLD"));
			String INVOICE_ADD_XIAOSHOU_OLD = CommonUtils.checkNull(request.getParamValue("INVOICE_ADD_XIAOSHOU_OLD"));
			
			
		    Map<String,String> xiaoShou = new LinkedHashMap<String, String>();
		    xiaoShou.put("公司全称",COMPANY_NAME_NEW);
		    xiaoShou.put("公司简称",COMPANY_SHORTNAME_NEW);
		    xiaoShou.put("开票联系人",INVOICE_PERSION_XIAOSHOU);
		    xiaoShou.put("开户行",BEGIN_BANK_XIAOSHOU);
		    xiaoShou.put("开票联系人手机",INVOICE_TELPHONE_XIAOSHOU);
			if(TAX_INVOICE_XIAOSHOU.equals("10041001")){
				xiaoShou.put("增值税发票","是");
			}else{
				xiaoShou.put("增值税发票","否");
			}
		    xiaoShou.put("账号",INVOICE_ACCOUNT_XIAOSHOU);
		    xiaoShou.put("纳税人识别号",TAXPAYER_NO_XIAOSHOU);
		    xiaoShou.put("电话",INVOICE_PHONE_XIAOSHOU);
		    xiaoShou.put("税号",TAXES_NO_XIAOSHOU);
		    xiaoShou.put("开票名称",ERP_CODE_XIAOSHOU);
		    xiaoShou.put("开票地址",INVOICE_ADD_XIAOSHOU);
		    
		    Map<String,String> xiaoShouOld = new HashMap<String, String>();
		    xiaoShouOld.put("公司全称",COMPANY_NAME);
		    xiaoShouOld.put("公司简称",COMPANY_SHORTNAME);
		    xiaoShouOld.put("开票联系人",INVOICE_PERSION_XIAOSHOU_OLD);
		    xiaoShouOld.put("开户行",BEGIN_BANK_XIAOSHOU_OLD);
		    xiaoShouOld.put("开票联系人手机",INVOICE_TELPHONE_XIAOSHOU_OLD);
			if(TAX_INVOICE_XIAOSHOU_OLD.equals("10041001")){
				xiaoShouOld.put("增值税发票","是");
			}else{
				xiaoShouOld.put("增值税发票","否");
			}
		    xiaoShouOld.put("账号",INVOICE_ACCOUNT_XIAOSHOU_OLD);
		    xiaoShouOld.put("纳税人识别号",TAXPAYER_NO_XIAOSHOU_OLD);
		    xiaoShouOld.put("电话",INVOICE_PHONE_XIAOSHOU_OLD);
		    xiaoShouOld.put("税号",TAXES_NO_XIAOSHOU_OLD);
		    xiaoShouOld.put("开票名称",ERP_CODE_XIAOSHOU_OLD);
		    xiaoShouOld.put("开票地址",INVOICE_ADD_XIAOSHOU_OLD);
		    
		    Map<String,String> xiaoShouKey = new HashMap<String, String>();
		    xiaoShouKey.put("公司全称","COMPANY_NAME");
		    xiaoShouKey.put("公司简称","COMPANY_SHORTNAME");
		    xiaoShouKey.put("开票名称","ERP_CODE");
		    xiaoShouKey.put("开票地址","INVOICE_ADD");
		    xiaoShouKey.put("开户行","BEGIN_BANK");
		    xiaoShouKey.put("账号","INVOICE_ACCOUNT");
		    xiaoShouKey.put("开票联系人","INVOICE_PERSION");
		    xiaoShouKey.put("开票联系人手机","INVOICE_TELPHONE");
		    xiaoShouKey.put("增值税发票","TAX_INVOICE");
		    xiaoShouKey.put("纳税人识别号","TAXPAYER_NO");
		    xiaoShouKey.put("税号","TAXES_NO");
		    xiaoShouKey.put("电话","INVOICE_PHONE");
		    
		    
		    
		    String TAXPAYER_NATURE_SHOUHOU = CommonUtils.checkNull(request.getParamValue("TAXPAYER_NATURE_SHOUHOU"));
			String INVOICE_ACCOUNT_SHOUHOU = CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT_SHOUHOU"));
			String TAX_DISRATE_SHOUHOU = CommonUtils.checkNull(request.getParamValue("TAX_DISRATE_SHOUHOU"));
			String INVOICE_POST_ADD_SHOUHOU = CommonUtils.checkNull(request.getParamValue("INVOICE_POST_ADD_SHOUHOU"));
			String TAXES_NO_SHOUHOU = CommonUtils.checkNull(request.getParamValue("TAXES_NO_SHOUHOU"));
			String TAXPAYER_NO_SHOUHOU = CommonUtils.checkNull(request.getParamValue("TAXPAYER_NO_SHOUHOU"));
			String TAX_INVOICE_SHOUHOU = CommonUtils.checkNull(request.getParamValue("TAX_INVOICE_SHOUHOU"));
			String INVOICE_ADD_SHOUHOU = CommonUtils.checkNull(request.getParamValue("INVOICE_ADD_SHOUHOU"));
			String INVOICE_PHONE_SHOUHOU = CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE_SHOUHOU"));
			String BEGIN_BANK_SHOUHOU = CommonUtils.checkNull(request.getParamValue("BEGIN_BANK_SHOUHOU"));
			String ERP_CODE_SHOUHOU = CommonUtils.checkNull(request.getParamValue("ERP_CODE_SHOUHOU"));
			
		    String TAXPAYER_NATURE_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("TAXPAYER_NATURE_SHOUHOU_OLD"));
			String INVOICE_ACCOUNT_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("INVOICE_ACCOUNT_SHOUHOU_OLD"));
			String TAX_DISRATE_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("TAX_DISRATE_SHOUHOU_OLD"));
			String INVOICE_POST_ADD_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("INVOICE_POST_ADD_SHOUHOU_OLD"));
			String TAXES_NO_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("TAXES_NO_SHOUHOU_OLD"));
			String TAXPAYER_NO_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("TAXPAYER_NO_SHOUHOU_OLD"));
			String TAX_INVOICE_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("TAX_INVOICE_SHOUHOU_OLD"));
			String INVOICE_ADD_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("INVOICE_ADD_SHOUHOU_OLD"));
			String INVOICE_PHONE_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("INVOICE_PHONE_SHOUHOU_OLD"));
			String BEGIN_BANK_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("BEGIN_BANK_SHOUHOU_OLD"));
			String ERP_CODE_SHOUHOU_OLD = CommonUtils.checkNull(request.getParamValue("ERP_CODE_SHOUHOU_OLD"));
			
			String REMARK_SHOUHOU = CommonUtils.checkNull(request.getParamValue("REMARK_SHOUHOU"));
			
			
		    Map<String,String> ShouHouOld = new LinkedHashMap<String, String>();
		    ShouHouOld.put("公司全称",COMPANY_NAME);
			ShouHouOld.put("公司简称",COMPANY_SHORTNAME);
			ShouHouOld.put("服务站地址",INVOICE_ADD_SHOUHOU_OLD);
			ShouHouOld.put("纳税人识别号",TAXPAYER_NO_SHOUHOU_OLD);
			ShouHouOld.put("税号",TAXES_NO_SHOUHOU_OLD);
			if(TAX_INVOICE_SHOUHOU_OLD.equals("10041001")){
				ShouHouOld.put("增值税发票","是");
			}else{
				ShouHouOld.put("增值税发票","否");
			}
			ShouHouOld.put("开票税率",TAX_DISRATE_SHOUHOU_OLD);
			ShouHouOld.put("发票邮寄地址",INVOICE_POST_ADD_SHOUHOU_OLD);
			ShouHouOld.put("开户行",BEGIN_BANK_SHOUHOU_OLD);
			ShouHouOld.put("开票名称",ERP_CODE_SHOUHOU_OLD);
			ShouHouOld.put("账号",INVOICE_ACCOUNT_SHOUHOU_OLD);
			ShouHouOld.put("电话",INVOICE_PHONE_SHOUHOU_OLD);
			ShouHouOld.put("纳税人性质",TAXPAYER_NATURE_SHOUHOU_OLD);
			
		    Map<String,String> ShouHouKey = new HashMap<String, String>();
		    ShouHouKey.put("公司全称","COMPANY_NAME");
			ShouHouKey.put("公司简称","COMPANY_SHORTNAME");
			ShouHouKey.put("账号","INVOICE_ACCOUNT");
			ShouHouKey.put("开票税率","TAX_DISRATE");
			ShouHouKey.put("发票邮寄地址","INVOICE_POST_ADD");
			ShouHouKey.put("税号","TAXES_NO");
			ShouHouKey.put("纳税人识别号","TAXPAYER_NO");
			ShouHouKey.put("增值税发票","TAX_INVOICE");
			ShouHouKey.put("服务站地址","INVOICE_ADD");
			ShouHouKey.put("电话","INVOICE_PHONE");
			ShouHouKey.put("开户行","BEGIN_BANK");
			ShouHouKey.put("开票名称","ERP_CODE");
			ShouHouKey.put("纳税人性质","TAXPAYER_NATURE");
			
		    Map<String,String> ShouHou = new HashMap<String, String>();
		    ShouHou.put("公司全称",COMPANY_NAME_NEW);
			ShouHou.put("公司简称",COMPANY_SHORTNAME_NEW);
			ShouHou.put("账号",INVOICE_ACCOUNT_SHOUHOU);
			ShouHou.put("开票税率",TAX_DISRATE_SHOUHOU);
			ShouHou.put("发票邮寄地址",INVOICE_POST_ADD_SHOUHOU);
			ShouHou.put("税号",TAXES_NO_SHOUHOU);
			ShouHou.put("纳税人识别号",TAXPAYER_NO_SHOUHOU);
			if(TAX_INVOICE_SHOUHOU.equals("10041001")){
				ShouHou.put("增值税发票","是");
			}else{
				ShouHou.put("增值税发票","否");
			}
			
			ShouHou.put("服务站地址",INVOICE_ADD_SHOUHOU);
			ShouHou.put("电话",INVOICE_PHONE_SHOUHOU);
			ShouHou.put("开户行",BEGIN_BANK_SHOUHOU);
			ShouHou.put("开票名称",ERP_CODE_SHOUHOU);
			ShouHou.put("纳税人性质",TAXPAYER_NATURE_SHOUHOU);
			
			String changeFlag = CommonUtils.checkNull(request.getParamValue("changeFlag"));
			ShouHou.put("changeFlag", changeFlag);
			xiaoShou.put("changeFlag",changeFlag);
			
			TmCompanyPO temp = new TmCompanyPO();
			temp.setCompanyId(Long.parseLong(companyId));
			List<TmCompanyPO> list = factory.select(temp);
			
			if(list != null && list.size() == 1) {
				TmCompanyPO oldPo = list.get(0);

				if(StringUtil.isEmpty(changeFlag)){
					
				}else{
					if(changeFlag.equals("10771002")){
						for(Map.Entry<String, String> entry : ShouHou.entrySet()){
							if(!StringUtil.isEmpty(entry.getValue())){
								if(entry.getKey().equals("changeFlag")){
									continue;
								}
								TcCompanyLogPO log = new TcCompanyLogPO();
								log.setLogId(Long.parseLong(SequenceManager.getSequence("")));
								log.setCreateBy(logonUser.getUserId());
								log.setCreateDate(new Date());
								log.setCompanyCode(oldPo.getCompanyCode());
								log.setCompanyId(oldPo.getCompanyId());
								log.setNewCompanyName(COMPANY_NAME_NEW);
								log.setOldCompanyName(COMPANY_NAME);
								
								log.setKey(entry.getKey());//字段中文
								log.setVer(var);
								log.setOldValue(ShouHouOld.get(entry.getKey()));//旧值
								log.setKeyGbk(ShouHouKey.get(entry.getKey()));//字段英语
								log.setValue(entry.getValue());//值
								log.setRemark(REMARK_SHOUHOU);
								dao.insert(log);
								message = "修改成功，请等待审核！";
							}
						}
					}else{
						for(Map.Entry<String, String> entry : xiaoShou.entrySet()){
							if(!StringUtil.isEmpty(entry.getValue())){
								if(entry.getKey().equals("changeFlag")){
									continue;
								}
								TcCompanyLogPO log = new TcCompanyLogPO();
								log.setLogId(Long.parseLong(SequenceManager.getSequence("")));
								log.setCreateBy(logonUser.getUserId());
								log.setCreateDate(new Date());
								log.setCompanyCode(oldPo.getCompanyCode());
								log.setCompanyId(oldPo.getCompanyId());
								
								log.setNewCompanyName(COMPANY_NAME_NEW);
								log.setOldCompanyName(COMPANY_NAME);
								
								log.setKey(entry.getKey());//字段
								log.setOldValue(xiaoShouOld.get(entry.getKey()));//值
								log.setKeyGbk(xiaoShouKey.get(entry.getKey()));//值
								log.setValue(entry.getValue());//值
								log.setVer(var);
								log.setRemark(REMARK_XIAOSHOU);
								dao.insert(log);
								message = "修改成功，请等待审核！";
							} 
						}
					}
				}
				
				if(message == null) {
					message = "未做任何修改！";
				}
				
				act.setOutData("message", message);
			} else {
				throw new UserException("经销商公司查询数据异常！");
			}
			
		} catch(UserException _ex) {
			logger.error(logonUser, _ex);
			BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, _ex.getMessage());
			act.setException(e1);
		} catch (Exception _ex) {
			BizException e1 = new BizException(act,_ex,ErrorCodeConstant.SPECIAL_MEG, "经销商公司保存出错！");
			logger.error(logonUser, _ex);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商公司更名初始化
	 */
	public void queryAllDlrInfo2() {
		try {
			act.setForword("/jsp/systemMng/orgMng/companyInfoUpdateInit.jsp");
		}
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司更名初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 经销商公司更名初始化
	 */
	public void dealerCompanyUpdateAuditInit() {
		try {
			act.setForword("/jsp/systemMng/orgMng/dealerCompanyUpdateAuditInit.jsp");
		}
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司更名初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商公司更名审核
	 */
	public void queryCompanyLogInfo() {
		RequestWrapper request = act.getRequest();
		try {
			String companyCode = CommonUtils.checkNull(request.getParamValue("COMPANY_CODE"));
			String companyName = CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));
			String status = CommonUtils.checkNull(request.getParamValue("STATUS"));
			
			StringBuilder sb = new StringBuilder(100);
			List<Object> params = new ArrayList<Object>();
			sb.append("select  t.COMPANY_ID ,t.company_code ,t.OLD_COMPANY_NAME,t.NEW_COMPANY_NAME,t.STATUS from tc_company_log t where 1 = 1\n");
			
			if(!"".equals(companyName)) {
				sb.append(" and t.old_company_name like ?\n");
				params.add("%"+companyName+"%");
			}
			
			if(!"".equals(companyCode)) {
				sb.append(" and t.company_code = ?\n");
				params.add(companyCode);
			}
			
			if(!"".equals(status)) {
				sb.append(" and t.status = ?\n");
				params.add(status);
			}
			sb.append(" group by t.company_code ,t.OLD_COMPANY_NAME,t.NEW_COMPANY_NAME,t.STATUS,t.COMPANY_ID ");
//			sb.append(" order by t.create_date desc");
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			SgmOrgMngDAO dao = new SgmOrgMngDAO();
			PageResult<Map<String, Object>> ps = dao.pageQuery(sb.toString(), params, dao.getFunName(), 15, curPage);
			
			act.setOutData("ps", ps);
		}
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司更名初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商公司修改审核
	 */
	public void companyLogAudit() {
		RequestWrapper request = act.getRequest();
		try {
			String COMPANY_ID = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));
			
			if("".equals(COMPANY_ID) || (!flag.equals("1") && !flag.equals("2"))) {
				act.setOutData("errCode", "1");
				act.setOutData("msg", "审核失败，参数异常，请联系管理员！");
				return;
			}
			
			TcCompanyLogPO tmp = new TcCompanyLogPO();
			tmp.setCompanyId(Long.parseLong(COMPANY_ID));
			tmp.setStatus("1");
			List<TcCompanyLogPO> list = factory.select(tmp);
			
			TmCompanyPO updateCompany = new TmCompanyPO();
			TmCompanyPO conditionCompany = new TmCompanyPO();
			TmDealerPO dealerCondition = new TmDealerPO();
			TmDealerPO dealerUpdate = new TmDealerPO();
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getKeyGbk()==null){
					continue;
				}
				if(list.get(i).getKeyGbk().equals("COMPANY_SHORTNAME")){
					updateCompany.setCompanyShortname(list.get(i).getValue());
					dealerUpdate.setDealerShortname(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("COMPANY_NAME")){
					updateCompany.setCompanyName(list.get(i).getValue());
					dealerUpdate.setDealerName(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("INVOICE_POST_ADD")){
					dealerUpdate.setInvoicePostAdd(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("TAXPAYER_NATURE")){
					dealerUpdate.setTaxpayerNature(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("TAXES_NO")){
					dealerUpdate.setTaxesNo(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("BEGIN_BANK")){
					dealerUpdate.setBeginBank(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("INVOICE_ACCOUNT")){
					dealerUpdate.setInvoiceAccount(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("TAX_DISRATE")){
					dealerUpdate.setTaxDisrate(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("INVOICE_ADD")){
					dealerUpdate.setInvoiceAdd(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("TAXPAYER_NO")){
					dealerUpdate.setTaxpayerNo(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("ERP_CODE")){
					dealerUpdate.setErpCode(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("TAX_INVOICE")){
					dealerUpdate.setTaxInvoice(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("changeFlag")){
					dealerCondition.setDealerType(Integer.valueOf(list.get(i).getValue()));
					continue;
				}
				if(list.get(i).getKeyGbk().equals("INVOICE_PHONE")){
					dealerUpdate.setInvoicePhone(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("INVOICE_PERSION")){
					dealerUpdate.setInvoicePersion(list.get(i).getValue());
					continue;
				}
				if(list.get(i).getKeyGbk().equals("INVOICE_TELPHONE")){
					dealerUpdate.setInvoiceTelphone(list.get(i).getValue());
					continue;
				}
			}
			if(list != null && list.size() > 0) {
				if(flag.equals("1")) {
					//修改公司信息
					updateCompany.setUpdateBy(logonUser.getUserId());
					updateCompany.setUpdateDate(new Date());
					conditionCompany.setCompanyId(list.get(0).getCompanyId());
					factory.update(conditionCompany, updateCompany);
					
					// == add by chenyu 2015年9月6日 14:27:45
					// 更新经销商前,校验如果修改了经销商名,需要生成修改记录
					dealerUpdate.setUpdateBy(logonUser.getUserId());
					dealerUpdate.setUpdateDate(new Date());
					dealerCondition.setCompanyId(list.get(0).getCompanyId());
					DlrInfoMngDAO dlrDao = new DlrInfoMngDAO();
					dlrDao.createDealerNameChgHis(dealerCondition, dealerUpdate.getDealerName());
					// == add by chenyu 2015年9月6日 14:27:45
					//修改经销商信息
					factory.update(dealerCondition, dealerUpdate);
					
					//同事修改另外一个类型的经销商名称
					TmDealerPO dealerCondition1 = new TmDealerPO();
					TmDealerPO dealerUpdate1 = new TmDealerPO();
					dealerCondition1.setCompanyId(list.get(0).getCompanyId());
					
					dealerUpdate1.setDealerName(dealerUpdate.getDealerName());
					dealerUpdate1.setDealerShortname(dealerUpdate.getDealerShortname());
					factory.update(dealerCondition1, dealerUpdate1);
					
					//修改更名审核状态
					TcCompanyLogPO update = new TcCompanyLogPO();
					update.setStatus("2");
					update.setUpdateBy(logonUser.getUserId());
					update.setUpdateDate(new Date());
					factory.update(tmp, update);
				}else{
					TcCompanyLogPO update = new TcCompanyLogPO();
					update.setStatus("3");
					update.setUpdateBy(logonUser.getUserId());
					update.setUpdateDate(new Date());
					factory.update(tmp, update);
				}
				act.setOutData("errCode", "0");
				act.setOutData("msg", "操作成功！");
				
			} else {
				act.setOutData("errCode", "1");
				act.setOutData("msg", "审核失败，数据异常，请联系管理员！");
			}
		}
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商公司更名初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * Function       :  根据公司ID查询其对应的信息
	 * @param         :  request-公司ID
	 * @return        :  公司信息
	 * @throws        :  Exception
	 * LastUpdate     :  2009-08-19
	 */
	public void getCompanyInfo(){
		Long companyId = null;          //公司ID
		try {
			RequestWrapper request = act.getRequest();
			/**
			 * 得到页面参数
			 */
			companyId = new Long(CommonUtils.checkNull(request.getParamValue("companyId")));
			
			//得到SGM认证品牌
			//List<TmBrandPO> sgmlist = CompanyInfoDAO.getSGMBrand();
			
//			//得到公司信息
//			CompanyBean companyBean = new CompanyBean();
//			List<CompanyBean> companyList = CompanyInfoDAO.getCompanyInfoItem(companyId);
//			companyBean = companyList.get(0);
//			//act.setOutData("SGM_LIST", sgmlist);
//			String relation_com_id = companyBean.getRelationCompany()==null?"":companyBean.getRelationCompany();
//			if(!"".equals(relation_com_id) && !"0".equals(relation_com_id)){
//				TmCompanyPO po = new TmCompanyPO();
//				po.setCompanyId(Long.valueOf(relation_com_id));
//				po = factory.select(po).get(0);
//				act.setOutData("po", po);
//			}
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("select * from tm_company where company_id = " + companyId);
			
			List list = CompanyInfoDAO.getCompanyInfoItem(companyId);
			if(CompanyInfoDAO.getDealerStatusF(companyId)!=null){
				act.setOutData("cbF", CompanyInfoDAO.getDealerStatusF(companyId).get("STATUS"));
			}else{
				act.setOutData("cbF1", "noDate");
			}
			if(CompanyInfoDAO.getDealerStatusS(companyId)!=null){
				act.setOutData("cbS", CompanyInfoDAO.getDealerStatusS(companyId).get("STATUS"));
			}else{
				act.setOutData("cbS1", "noDate");
			}
//			act.setOutData("cbS", CompanyInfoDAO.getDealerStatusS(companyId).get("STATUS"));

			act.setOutData("cb", list.get(0));
			
			act.setForword("/jsp/systemMng/orgMng/companyInfoAdd.jsp");
		}
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM认证品牌信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * Function       :  根据公司ID查询其对应的信息
	 * @param         :  request-公司ID
	 * @return        :  公司信息
	 * @throws        :  Exception
	 * LastUpdate     :  2009-08-19
	 */
	public void getCompanyInfo2(){
		Long companyId = null;          //公司ID
		try {
			RequestWrapper request = act.getRequest();
			/**
			 * 得到页面参数
			 */
			companyId = new Long(CommonUtils.checkNull(request.getParamValue("companyId")));
			
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("select * from tm_company where company_id = " + companyId);
			
			List list = CompanyInfoDAO.getCompanyInfoItem(companyId);
			List FDealer = CompanyInfoDAO.getDealerInfoByF(companyId);
			List SDealer = CompanyInfoDAO.getDealerInfoByS(companyId);

			act.setOutData("cb", list.get(0));
			if(FDealer.size()>0){
				act.setOutData("FDealer", FDealer.get(0));
			}
			if(SDealer.size()>0){
				act.setOutData("SDealer", SDealer.get(0));
			}
			
			
			act.setForword("/jsp/systemMng/orgMng/companyInfoUpdate.jsp");
		}
		catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM认证品牌信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  更新公司信息
	 * @param         :  request-经销商ID、经销商名称、经销商代码、所属部门ID、经销商简称、省份、城市、开业时间、SAP账号
	 * @param         :  request-公司类型、传真、负责人、电话、手机、地址、邮编、邮箱、状态、品牌ID
	 * @return        :  更新经销商成功或失败
	 * @throws        :  Exception
	 * LastUpdate     :  2009-08-20
	 */
	public void updateCompanyInfo(){
		String companyId = null;            //经销商ID
		String companyName = null;          //经销商名称
		String companyCode = null;          //经销商代码
		String companyShortname = null;  //经销商简称
		String companyType = null;      //公司类型
		String fax = null;              //传真
		String phone = null;          //电话
		String address = null;         //地址
		String zipCode = null;          //邮编
		String status = null;          //状态
		String provinceId=null; //省份
		String cityId=null;     //城市
		try {
			RequestWrapper request = act.getRequest();
			/**
			 * 得到页面参数
			 */
			companyId = CommonUtils.checkNull(request.getParamValue("D_COMPANY_ID"));
			companyName = CommonUtils.checkNull(request.getParamValue("D_COMPANY_NAME"));
			companyCode = CommonUtils.checkNull(request.getParamValue("D_COMPANY_CODE"));
			companyShortname = CommonUtils.checkNull(request.getParamValue("COMPANY_SHORTNAME"));
			fax = CommonUtils.checkNull(request.getParamValue("FAX"));
			phone = CommonUtils.checkNull(request.getParamValue("PHONE"));
			address = CommonUtils.checkNull(request.getParamValue("ADDRESS"));
			zipCode = CommonUtils.checkNull(request.getParamValue("ZIP_CODE"));
			status = CommonUtils.checkNull(request.getParamValue("STATUS"));
			companyType = CommonUtils.checkNull(request.getParamValue("COMPANY_TYPE"));
			provinceId= CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));
			cityId= CommonUtils.checkNull(request.getParamValue("CITY_ID"));
			
			String IS_SAME_PERSON = CommonUtils.checkNull(request.getParamValue("IS_SAME_PERSON"));  //是否同一法人
			String IS_BEFORE_AFTER = CommonUtils.checkNull(request.getParamValue("IS_BEFORE_AFTER")); //是否前店后厂
			String HANDLE_BY_HANDLE = CommonUtils.checkNull(request.getParamValue("HANDLE_BY_HANDLE")); //是否手拉手
//			String IS_WHOLE = CommonUtils.checkNull(request.getParamValue("IS_WHOLE"));  //是否一体化
			String COMPANY_ID  = CommonUtils.checkNull(request.getParamValue("COMPANY_ID")); //关联公司
			
//			 后台验证经销商代码、经销商名称、经销商简称、负责人、地址、电话、邮箱
			List<MsgCarrier> msglist = new ArrayList<MsgCarrier>();
			msglist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"公司代码",0,Constant.Length_Check_Char_10,companyCode));
			msglist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"公司名称",0,Constant.Length_Check_Char_30,companyName));
			msglist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"公司简称",0,Constant.Length_Check_Char_15,companyShortname));
			msglist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"地址",0,Constant.Length_Check_Char_30,address));
			msglist.add(MsgCarrier.getInstance(ValidateCodeConstant.PHONE_PATTERN,"联系电话",0,Constant.Length_Check_Char_20,phone));
			//Validate.doValidate(act, msglist);
			//获取排序字段和排序类型
			
			
			
			//判断修改的经销商信息是否已经存在相应的经销商表中
			List <TmCompanyPO> list = CompanyInfoDAO.queryCompanyInfo(companyCode/*,companyName,companyShortname*/); //去掉经销商公司名称、简称重名判断
			long len = list.size() ;
			// 若将公司的状态修改为无效，则不用验证
			if(status.equals(Constant.STATUS_DISABLE.toString())) {
				len = 0 ;
			}
			if(len > 1){
				act.setOutData("ACTION_RESULT","2");
			}else{
				TmCompanyPO tmcompanyPO = new TmCompanyPO();
				tmcompanyPO.setCompanyName(companyName);
				tmcompanyPO.setCompanyCode(companyCode);
				tmcompanyPO.setCompanyShortname(companyShortname);
				tmcompanyPO.setFax(fax);
//				tmcompanyPO.setPhone(phone);
				tmcompanyPO.setCompanyType(Integer.valueOf(companyType));
				tmcompanyPO.setStatus(Integer.valueOf(status));
				tmcompanyPO.setUpdateBy(logonUser.getUserId());
				tmcompanyPO.setUpdateDate(new Date(System.currentTimeMillis()));
				TmCompanyPO tmcompanyPO1 = new TmCompanyPO();
				tmcompanyPO1.setCompanyId(new Long(companyId));
				TmCompanyPO temp = new TmCompanyPO();
				temp = factory.select(tmcompanyPO1).get(0);
				if(!(temp.getRelationCompany() == null || "0".equals(temp.getRelationCompany().toString()) || "".equals(temp.getRelationCompany().toString()))){
					String sql = "UPDATE TM_COMPANY SET RELATION_COMPANY = 0 WHERE COMPANY_ID = "+temp.getRelationCompany();
					factory.update(sql, null);
				}
				//根据经销商ID在数据库中更新经销商信息
				/***************************add by xieyj***********************/
				if(!"".equals(IS_SAME_PERSON)){
					tmcompanyPO.setIsSamePerson(Integer.valueOf(IS_SAME_PERSON));
				}
				if(!"".equals(IS_BEFORE_AFTER)){
					tmcompanyPO.setIsBeforeAfter(Integer.valueOf(IS_BEFORE_AFTER));
				}
				if(!"".equals(HANDLE_BY_HANDLE)){
					tmcompanyPO.setHandleByHandle(Integer.valueOf(HANDLE_BY_HANDLE));
				}
//				if(!"".equals(IS_WHOLE)){
//					tmcompanyPO.setIsWhole(Integer.valueOf(IS_WHOLE));
//				}
				if(!"".equals(COMPANY_ID)){
					tmcompanyPO.setRelationCompany(Long.valueOf(COMPANY_ID));
					//双向更新经销商关联公司
					TmCompanyPO po_1 = new TmCompanyPO();
					po_1.setCompanyId(Long.valueOf(COMPANY_ID));
					po_1 = factory.select(po_1).get(0);
					Long com_id = po_1.getRelationCompany();
					TmCompanyPO  po_2 = new TmCompanyPO();
					po_2.setRelationCompany(new Long(companyId));
					TmCompanyPO  po_3 = new TmCompanyPO();
					po_3.setCompanyId(Long.valueOf(COMPANY_ID));
					factory.update(po_3, po_2);
					if(!(com_id == null || "0".equals(com_id.toString()) || "".equals(com_id.toString()))){
						String sql = "UPDATE TM_COMPANY SET RELATION_COMPANY = 0 WHERE COMPANY_ID = "+com_id;
						factory.update(sql, null);
					}
				}
				/**************************************************************/
				factory.update(tmcompanyPO1, tmcompanyPO);
//				if(companyType.equals(Constant.COMPANY_TYPE_SGM))
//				{
//					//获取车厂公司ID对应的部门ID
//					TmOrgPO orgPO2 = new TmOrgPO();
//					List<TmOrgPO> orgList = CompanyInfoDAO.queryOrgId(companyId);
//					orgPO2 = orgList.get(0);
//					Long orgId = orgPO2.getOrgId();
//					
//					TmOrgPO deptPO1 = new TmOrgPO();
//					deptPO1.setOrgId(orgId);
//					TmOrgPO deptPO = new TmOrgPO();
//					deptPO.setOrgName(companyShortname);
//					deptPO.setUpdateBy(logonUser.getUserId());
//					deptPO.setUpdateDate(new Date(System.currentTimeMillis()));
//					//根据部门ID在数据库中更新部门信息 
//					factory.update(deptPO1, deptPO);
//				}
				
				
				//判断是否选择了认证品牌
//				if(brandList != null){
//					TrDlrBrandPO dlrbrandPO = new TrDlrBrandPO();
//					dlrbrandPO.setDlrId(dlrId);
//					factory.delete(dlrbrandPO);
//					for(int i = 0;i < brandList.length;i++){
//						brandId=brandList[i];
//						TrDlrBrandPO trdlrbrandPO = new TrDlrBrandPO();
//						trdlrbrandPO.setDlrBrandId(factory.getStringPK(trdlrbrandPO));
//						trdlrbrandPO.setDlrId(dlrId);
//						trdlrbrandPO.setCreateBy(logonUser.getUserId());
//						trdlrbrandPO.setCreateDate(new Date(System.currentTimeMillis()));
//						trdlrbrandPO.setBrandId(brandId);
//						//将经销商与其对应的SGM认证品牌插入经销商认证品牌表
//						factory.insert(trdlrbrandPO);
//					}
//				}else{
//					TrDlrBrandPO trdlrbrandPO1 = new TrDlrBrandPO();
//					trdlrbrandPO1.setDlrId(dlrId);
//					factory.delete(trdlrbrandPO1);
//				}		
				act.setOutData("ACTION_RESULT","1");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"公司信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void updateNameHisQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String companyId = act.getRequest().getParamValue("companyId");
			List <TcCompanyLogPO> Dtl = CompanyInfoDAO.history(companyId);
			act.setOutData("Dtl", Dtl);
			if(Dtl.size()==0){
				act.setOutData("length", 0);
			}
			act.setForword("/jsp/systemMng/orgMng/companyUpdateNameHis.jsp");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "更名历史明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void detailInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String COMPANY_ID = act.getRequest().getParamValue("COMPANY_ID");
			String ver = act.getRequest().getParamValue("ver");
			act.setOutData("COMPANY_ID", COMPANY_ID);
			act.setOutData("ver", ver);
			act.setForword("/jsp/systemMng/orgMng/companyModDtl.jsp");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"更名修改明细查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void detailQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String COMPANY_ID = act.getRequest().getParamValue("COMPANY_ID");
			String ver = act.getRequest().getParamValue("ver");
			//得到系统中所有经销商
			PageResult<Map<String,Object>> ps = CompanyInfoDAO.getDtl(COMPANY_ID,ver);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"更名修改明细查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
