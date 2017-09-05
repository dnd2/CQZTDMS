package com.infodms.dms.dao.dealermanage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.DealerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBrandPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgBusinessAreaPO;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.POCallBack;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

public class SysDealerDAO {
	public static Logger logger = Logger.getLogger(SysDealerDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	
	public static PageResult<TmDealerPO> sysDealerQuery(String dealerName,
			String dealerCode, String dealerType, int curPage, int pageSize,
			String orderName, String da) throws BizException {
		StringBuffer query = new StringBuffer("select DEALER_ID,DEALER_CODE,DEALER_NAME,DEALER_LNAME,STATUS from TM_DEALER where 1=1 ");
		if (dealerName != null && !dealerName.equals("")) { // 拼查询角色的SQL
			query.append("  and DEALER_NAME like '%");
			query.append(dealerName);
			query.append("%'");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			query.append("  and DEALER_CODE like '%");
			query.append(dealerCode);
			query.append("%'");
		}
		if (dealerType != null && !dealerType.equals("")) {
			query.append("  and DEALER_TYPE = ");
			query.append(dealerType);
		}
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.pageQuery(OrderUtil.addOrderBy(query.toString(), orderName, da), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setStatus(rs.getInt("STATUS"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);
	}
	
	/**
	 * Function         : 查询系统中品牌
	 * @return          : 满足条件的品牌
	 * @throws          : Exception
	 * LastUpdate       : 2009-12-16
	 */
	public static List<TmBrandPO> getAllBrand() throws Exception{
		StringBuffer sql = new StringBuffer();
		List<TmBrandPO> list = null;
		try {
			sql.append("select \n");
			sql.append("	b.brand_id,b.brand_name \n");
			sql.append("  from tm_brand b \n");
			sql.append(" where status = ");
			sql.append(Constant.STATUS_ENABLE);
			list = factory.select(sql.toString(), null, new DAOCallback<TmBrandPO>(){
				public TmBrandPO wrapper(ResultSet rs, int arg1) {
					TmBrandPO tmbrand = new TmBrandPO();
					try {
						tmbrand.setBrandId(rs.getLong("brand_id"));
						tmbrand.setBrandName(rs.getString("brand_name"));
						return tmbrand;
					} catch (SQLException e) {
						throw new DAOException(e);
					}
				}});
			
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	/**
	 * Function         : 查询新增的经销商信息是否已经存在
	 * @param           : 经销商ID
	 * @param           : 经销商名称
	 * @param           : 经销商简称
	 * @return          : 满足条件的经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2009-08-10
	 */
	public static List<TmDealerPO> getDealer(String dealerCode,String dealerName,String dealerShortname) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmDealerPO> list = null;
		try {
			sql.append("select * 	from tm_dealer a 	where a.dealer_code='"+ dealerCode +"'\n");
			sql.append("		  or a.dealer_name='"+ dealerName +"'\n");
			sql.append("		  or a.dealer_shortname ='"+ dealerShortname +"'\n");
			

			list = factory.select(sql.toString(), null, new POCallBack(factory,TmDealerPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	/**
	 * Function         : 查询新增的经销商信息是否已经存在
	 * @param           : 经销商ID
	 * @param           : 经销商名称
	 * @param           : 经销商简称
	 * @return          : 满足条件的经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2009-08-10
	 */
	public static List<TmDealerPO> getDealer(String dealerId,String dealerCode,String dealerName,String dealerShortname) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmDealerPO> list = null;
		try {
			sql.append("select * 	from tm_dealer a 	where (a.dealer_code='"+ dealerCode +"'\n");
			sql.append("		  or a.dealer_name='"+ dealerName +"'\n");
			sql.append("		  or a.dealer_shortname ='"+ dealerShortname +"' ) and dealer_id != \n");
			sql.append(dealerId);
			

			list = factory.select(sql.toString(), null, new POCallBack(factory,TmDealerPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	
	/**
	 * Function         : 查询新增的代理商信息该公司下是否已经存在代理商
	 * @param           : 经销商ID
	 * @param           : 经销商名称
	 * @param           : 经销商简称
	 * @return          : 满足条件的经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2009-08-10
	 */
	public static List<TmOrgPO> getAgentOrgByCompany(String companyId) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmOrgPO> list = null;
		try {
			sql.append("select * 	from tm_org a 	where company_id = ");
			sql.append(companyId);
			sql.append(" and org_type = ");
			sql.append(Constant.ORG_TYPE_DEALER);

			list = factory.select(sql.toString(), null, new POCallBack(factory,TmOrgPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	/**
	 * Function         : 查询新增的代理商信息该公司下是否已经存在指定代理商
	 * @param           : 经销商ID
	 * @param           : 经销商名称
	 * @param           : 经销商简称
	 * @return          : 满足条件的经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2009-08-10
	 */
	public static List<TmOrgPO> getAgentOrgByCompany(String companyId,String agentId) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmOrgPO> list = null;
		try {
			sql.append("select a.* 	from tm_org a 	where a.company_id = ");
			sql.append(companyId);
			sql.append(" and a.org_type = ");
			sql.append(Constant.ORG_TYPE_DEALER);
			sql.append(" and a.org_id = ");
			sql.append(agentId);

			list = factory.select(sql.toString(), null, new POCallBack(factory,TmOrgPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	
	/**
	 * Function         : 查询新增的公司下是否有别的渠道代理了选择的品牌
	 * @param           : 经销商ID
	 * @param           : 经销商名称
	 * @param           : 经销商简称
	 * @return          : 满足条件的经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2009-08-10
	 */
	public static List<TmOrgBusinessAreaPO> getBusinessAreaByCompany(String companyId,String orgId,String[] brandSql) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmOrgBusinessAreaPO> list = null;
		try {
			sql.append("SELECT * ");
			sql.append("FROM TM_ORG_BUSINESS_AREA ");
			sql.append(" WHERE ORG_ID IN (SELECT ORG_ID FROM TM_ORG WHERE COMPANY_ID = ");
			sql.append(companyId);
			if(orgId != null && !"".equals(orgId)){
				sql.append(" AND ORG_ID <> ");
				sql.append(orgId);
			}
			sql.append(" )AND BRAND_ID IN (");
			for(int i = 0 ; i < brandSql.length ; i ++)
			{
				sql.append(brandSql[i]);
				if(i < brandSql.length - 1)
				{
					sql.append(",");
				}
			}
			
			sql.append(")");

			list = factory.select(sql.toString(), null, new POCallBack(factory,TmOrgBusinessAreaPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	
	/**
	 * Function         : 查询选择的上级代理商是否包含所勾选的业务范围
	 * @param           : 经销商ID
	 * @param           : 经销商名称
	 * @param           : 经销商简称
	 * @return          : 满足条件的经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-03-03
	 */
	public static List<TmBrandPO> getBusinessAreaByAgent(String orgId,String[] brandSql) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmBrandPO> list = null;
		try {
			sql.append("SELECT * ");
			sql.append("FROM TM_BRAND BRAND ");
			sql.append(" WHERE BRAND.BRAND_ID IN (");
			for(int i = 0 ; i < brandSql.length ; i ++)
			{
				sql.append(brandSql[i]);
				if(i < brandSql.length - 1)
				{
					sql.append(",");
				}
			}
			
			sql.append(") AND NOT EXISTS (");
			sql.append(" SELECT BRAND_ID FROM TM_ORG_BUSINESS_AREA AREA ");
			sql.append(" WHERE AREA.ORG_ID = ");
			sql.append(orgId);
			sql.append(" AND AREA.BRAND_ID = BRAND.BRAND_ID ");
			sql.append(" )");
			

			list = factory.select(sql.toString(), null, new POCallBack(factory,TmBrandPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	/**
	 * Function         : 查询选择的上级代理商折点是否大于所输入的折点
	 * @param           : 经销商ID
	 * @param           : 经销商名称
	 * @param           : 经销商简称
	 * @return          : 满足条件的经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-03-03
	 */
	public static List<TmBrandPO> getBusinessAreaByAgentPoint(String orgId,String brandId,String point) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmBrandPO> list = null;
		try {
			sql.append("SELECT 1 BRAND_ID ");
			sql.append("FROM DUAL ");
			sql.append(" WHERE EXISTS (");
			sql.append(" SELECT BRAND_ID FROM TM_ORG_BUSINESS_AREA AREA ");
			sql.append(" WHERE AREA.ORG_ID = ");
			sql.append(orgId);
			sql.append(" AND AREA.BRAND_ID = ");
			sql.append(brandId);
			sql.append(" AND AREA.POINT < ");
			sql.append(point);
			sql.append(" )");
			

			list = factory.select(sql.toString(), null, new POCallBack(factory,TmBrandPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	/**
	 * Function         : 查询新增的分销商上级代理关系是否重复
	 * @param           : 经销商ID
	 * @param           : 经销商名称
	 * @param           : 经销商简称
	 * @return          : 满足条件的经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2009-08-10
	 */
	public static List<TmOrgPO> getDistributionAgent(String companyId,String distritionId,String agentId) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmOrgPO> list = null;
		try {
			sql.append("SELECT DIS.* ");
			sql.append(" FROM TM_ORG DIS,TM_DEALER_ORG_RELATION DISREL ");
			sql.append(" WHERE DIS.ORG_ID = DISREL.ORG_ID AND DIS.PARENT_ORG_ID = ");
			sql.append(agentId);
			sql.append(" AND DIS.COMPANY_ID = ");
			sql.append(companyId);
			if(distritionId != null && !"".equals(distritionId))
			{
				sql.append(" AND DISREL.DEALER_ID <> ");
				sql.append(distritionId);
			}

			list = factory.select(sql.toString(), null, new POCallBack(factory,TmOrgPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	public static List<TmCompanyPO> getAgentCompany() throws BizException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT COMPANY_ID,COMPANY_SHORTNAME ||'(' || COMPANY_CODE ||')' COMPANY_NAME FROM TM_COMPANY WHERE STATUS = ");
		query.append(Constant.STATUS_ENABLE);
		query.append(" and COMPANY_TYPE = ");
		query.append(Constant.COMPANY_TYPE_DEALER);
		return factory.select(query.toString(), null, new DAOCallback<TmCompanyPO>() {
			public TmCompanyPO wrapper(ResultSet rs, int idx) {
				TmCompanyPO bean = new TmCompanyPO();
				try {
					bean.setCompanyId(rs.getLong("COMPANY_ID"));
					bean.setCompanyName(rs.getString("COMPANY_NAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	
	
	public static List<TmCompanyPO> getAllDealerCompany() throws BizException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT COMPANY_ID,COMPANY_SHORTNAME ||'(' || COMPANY_CODE ||')' COMPANY_NAME FROM TM_COMPANY WHERE STATUS = ");
		query.append(Constant.STATUS_ENABLE);
		query.append(" and COMPANY_TYPE IN (");
		query.append(Constant.COMPANY_TYPE_DEALER);
		//query.append(",");
		//query.append(Constant.COMPANY_TYPE_DEALER_DMC);
		query.append(")");
		return factory.select(query.toString(), null, new DAOCallback<TmCompanyPO>() {
			public TmCompanyPO wrapper(ResultSet rs, int idx) {
				TmCompanyPO bean = new TmCompanyPO();
				try {
					bean.setCompanyId(rs.getLong("COMPANY_ID"));
					bean.setCompanyName(rs.getString("COMPANY_NAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	
	
	public static List<DealerBean> getDealerForUpdate(Long dealerId) throws BizException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT DEL.DEALER_ID,DEL.DEALER_CODE,DEL.DEALER_NAME,DEL.DEALER_SHORTNAME,DEL.EMAIL ");
		query.append(" ,DEL.COMPANY_ID,DEL.STATUS,DEL.PROVINCE_ID,DEL.CITY_ID,DEL.ZIP_CODE,DEL.DEALER_TYPE ");
		query.append(" ,DEL.ADDRESS,DEL.PHONE,DEL.FAX_NO,DEL.LINK_MAN,DEL.TAX_ACCOUNTS,PAR.ORG_ID PARENT_ORG_ID,PAR.ORG_NAME,ORG.ORG_ID ");
		query.append(" ,DEL.QAD_CODE,DEL.SALE_AREA,DEL.STOCK_SET,DEL.COM_LEVEL,DEL.IS_MONOPOLY ");
		query.append(" FROM TM_DEALER DEL,TM_ORG ORG,TM_DEALER_ORG_RELATION REL,TM_ORG PAR ");
		query.append(" WHERE REL.DEALER_ID = DEL.DEALER_ID AND REL.ORG_ID = ORG.ORG_ID AND ORG.PARENT_ORG_ID = PAR.ORG_ID AND DEL.DEALER_ID = ");
		query.append(dealerId);
		
		return factory.select(query.toString(), null, new DAOCallback<DealerBean>() {
			public DealerBean wrapper(ResultSet rs, int idx) {
				DealerBean bean = new DealerBean();
				try {
					bean.setCompanyId(rs.getLong("COMPANY_ID"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerType(rs.getInt("DEALER_TYPE"));
					bean.setStatus(rs.getInt("STATUS"));
					bean.setProvinceId(rs.getInt("PROVINCE_ID"));
					bean.setCityId(rs.getInt("CITY_ID"));
					bean.setZipCode(rs.getString("ZIP_CODE"));
					bean.setAddress(rs.getString("ADDRESS"));
					bean.setPhone(rs.getString("PHONE"));
					bean.setFaxNo(rs.getString("FAX_NO"));
					bean.setLinkMan(rs.getString("LINK_MAN"));
					bean.setTaxAccounts(rs.getString("TAX_ACCOUNTS"));
					bean.setParentOrgId(rs.getString("PARENT_ORG_ID"));
					bean.setParentOrgName(rs.getString("ORG_NAME"));
					bean.setQadCode(rs.getString("QAD_CODE"));
					bean.setSaleArea(rs.getString("SALE_AREA"));
					bean.setStockSet(rs.getString("STOCK_SET"));
					bean.setOrgId(rs.getLong("ORG_ID"));
					bean.setComLevel(rs.getInt("COM_LEVEL"));
					bean.setIsMonopoly(rs.getInt("IS_MONOPOLY"));
					bean.setEmail(rs.getString("EMAIL"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	
	
	public static List<DealerBean> getUpdateAgentForDistritution(String orgId) throws BizException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT DEL.DEALER_ID,DEL.DEALER_CODE,DEL.DEALER_NAME,DEL.DEALER_SHORTNAME ");
		query.append(" FROM TM_DEALER DEL,TM_DEALER_ORG_RELATION REL ");
		query.append(" WHERE REL.DEALER_ID = DEL.DEALER_ID AND REL.ORG_ID = ");
		query.append(orgId);
		
		return factory.select(query.toString(), null, new DAOCallback<DealerBean>() {
			public DealerBean wrapper(ResultSet rs, int idx) {
				DealerBean bean = new DealerBean();
				try {					
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
}
