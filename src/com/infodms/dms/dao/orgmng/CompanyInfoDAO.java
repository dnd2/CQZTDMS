/**********************************************************************
 * <pre>
 * FILE : DlrInfoDAO.java
 * CLASS : DlrInfoDAO
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
 * $DlrInfoDAO,v0.1 2009/08/18  lax  经销商信息维护$
 */

package com.infodms.dms.dao.orgmng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.po.TcCompanyLogPO;
import com.infodms.dms.po.TmBrandPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.infox.util.StringUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.POCallBack;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

/**
 * Function : 经销商信息维护
 * 
 * @author : LAX CreateDate : 2009-08-18
 * @version : 0.1
 */
public class CompanyInfoDAO {
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CheckVehicleDAO dao = CheckVehicleDAO.getInstance();
	private static CompanyInfoDAO companyInfoDAO = new CompanyInfoDAO ();
	public static final CompanyInfoDAO getInstance() {
		if (companyInfoDAO == null) {
			companyInfoDAO = new CompanyInfoDAO();
		}
		return companyInfoDAO;
	}
	private CompanyInfoDAO() {}
	/**
	 * Function : 根据条件查询经销商的明细信息
	 * 
	 * @param : 经销商ID
	 * @return : 经销商的明细信息
	 * @throws : Exception LastUpdate : 2009-09-08
	 */
	public static List<CompanyBean> getCompanyInfoItem(Long companyId)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		List<CompanyBean> list = null;
		try {
			sql.append("select \n");
			sql.append("	c1.company_id oem_company_id,c1.company_shortname oem_company_shortname,c.company_id,c.company_code,c.company_shortname, \n");
			sql.append("	c.province_id,  \n");
			sql.append("	c.city_id,  \n");
			sql.append("	c.phone,  \n");
			sql.append("	c.fax,  \n");
			sql.append("	c.address,  \n");
			sql.append("	c.zip_code,  \n");
			sql.append("	c.company_name,c.status,c.company_type  \n");
			sql.append("  from tm_company c,tm_company c1 \n");
			sql.append("  		where c.company_id='" + companyId + "' \n");
			sql.append("  		  and c.oem_company_id=c1.company_id \n");

			list = factory.select(sql.toString(), null,
					new DAOCallback<CompanyBean>() {
						public CompanyBean wrapper(ResultSet rs, int arg1) {
							CompanyBean companyBean = new CompanyBean();
							try {
								companyBean.setCompanyId(rs
										.getString("company_id"));
								companyBean.setCompanyCode(rs
										.getString("company_code"));
								companyBean.setCompanyShortname(rs
										.getString("company_shortname"));
								companyBean.setProvinceId(rs
										.getString("province_id"));
								companyBean.setCityId(rs.getString("city_id"));
								companyBean.setPhone(rs.getString("phone"));
								companyBean.setCompanyName(rs
										.getString("company_name"));
								companyBean.setFax(rs.getString("fax"));
								companyBean.setAddress(rs.getString("address"));
								companyBean.setZipCode(rs.getString("zip_code"));
								companyBean.setStatus(rs.getString("status"));
								companyBean.setCompanyType(rs
										.getString("company_type"));
								companyBean.setOemCompanyId(rs
										.getString("oem_company_id"));
//								companyBean.setOemCompanyShortname(rs
//										.getString("oem_company_shortname"));
//								companyBean.setIsSamePerson(rs
//										.getString("is_same_person"));
//								companyBean.setIsBeforeAfter(rs
//										.getString("is_before_after"));
//								companyBean.setHandleByHandle(rs
//										.getString("handle_by_handle"));
//								// companyBean.setIsWhole(rs.getString("is_whole"));
//								companyBean.setRelationCompany(rs
//										.getString("relation_company"));
								return companyBean;
							} catch (SQLException e) {
								throw new DAOException(e);
							}
						}
					});

		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	public static List<Map<String,Object>> getDealerInfoByS(Long companyId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_dealer where company_id = ").append(companyId);
		sql.append(" AND DEALER_TYPE = 10771001");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	public static PageResult<Map<String,Object>> getDtl(String companyId, String ver) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from TC_COMPANY_LOG where company_id = ").append(companyId);
		if(StringUtil.isEmpty(ver)){
			sql.append(" AND status = 1");
		}else{
			sql.append(" AND ver = ").append(ver);
		}
		
		sql.append(" order by log_id ");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(), 15, 1);
	}
	
	public static List<Map<String,Object>> getDealerInfoByF(Long companyId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_dealer where company_id = ").append(companyId);
		sql.append(" AND DEALER_TYPE = 10771002");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	public static Map<String,Object> getDealerStatusF(Long companyId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_dealer where company_id = ").append(companyId);
		sql.append(" AND DEALER_TYPE = 10771002");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	public static Map<String,Object> getDealerStatusS(Long companyId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_dealer where company_id = ").append(companyId);
		sql.append(" AND DEALER_TYPE = 10771001");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}

	/**
	 * Function : 根据条件查询系统中所有经销商信息
	 * 
	 * @param : 经销商代码
	 * @param : 经销商名称
	 * @param : 公司类型
	 * @param : 所属部门ID
	 * @param : 当前页码
	 * @param : 每页显示记录数
	 * @param : 排序的字段名
	 * @param : 升序ASC\降序DESC
	 * @return : 满足条件的经销商详细，包含分页信息
	 * @throws : Exception LastUpdate : 2009-09-08
	 */
	public static PageResult<CompanyBean> getAllCompanyInfo(
			String oemCompanyId, String companyCode, String companyName,
			String companyType, String status, int pageSize, int curPage, String orderName,
			String da) throws Exception {
		StringBuffer sql = new StringBuffer();
		PageResult<CompanyBean> list = null;
		try {
			sql.append("select \n");
			sql.append("	c.company_id,c.company_code,c.company_shortname, \n");
			sql.append("	c.company_name,c.status  \n");
			sql.append("  from tm_company c  \n");
			sql.append("  	where  1=1  \n");

			if (!"".equals(oemCompanyId) && oemCompanyId != null) {
				sql.append("  	    and c.oem_company_id = " + oemCompanyId
						+ "  \n");
			}
			
			if(!"".equals(status) && status != null) {
				sql.append("  	    and c.status = " + status
						+ "  \n");
			}

			if (!"".equals(companyCode) && companyCode != null) {
				if ("%".equals(companyCode)) {
					sql.append("			and c.company_code ='%' \n");
				} else {
					sql.append("			and c.company_code like '%" + companyCode
							+ "%' \n");
				}
			}
			if (!"".equals(companyName) && companyName != null) {
				if ("%".equals(companyName)) {
					sql.append("			and c.company_name = '%' \n");
				} else {
					sql.append("			and c.company_name like '%" + companyName
							+ "%' \n");
				}
			}
			if (!"".equals(companyType) && companyType != null
					&& !"0".equals(companyType)) {
				sql.append("			and c.company_type = '" + companyType + "' \n");
			}
			sql.append(" order by company_code desc \n");
			String sb = OrderUtil.addOrderBy(sql.toString(), orderName, da);

			list = factory.pageQuery(sb, null, new DAOCallback<CompanyBean>() {
				public CompanyBean wrapper(ResultSet rs, int idx) {
					CompanyBean bean = new CompanyBean();
					try {
						bean.setCompanyId(rs.getString("company_id"));
						bean.setCompanyCode(rs.getString("company_code"));
						bean.setCompanyShortname(rs
								.getString("company_shortname"));
//						bean.setProvince(rs.getString("province_id"));
//						bean.setCity(rs.getString("city_id"));
						bean.setCompanyName(rs.getString("company_name"));
						bean.setStatus(rs.getString("status"));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return bean;
				}
			}, pageSize, curPage);

		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	/**
	 * Function : 查询系统中所有SGM认证品牌
	 * 
	 * @return : 满足条件的SGM认证品牌
	 * @throws : Exception LastUpdate : 2009-09-08
	 */
	public static List<TmBrandPO> getSGMBrand() throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmBrandPO> list = null;
		try {
			sql.append("select \n");
			sql.append("	b.brand_id,b.brand_name \n");
			sql.append("  from tm_brand b \n");
			// sql.append("	where b.sgm_brand_yn =('"+ Constant.IF_TYPE_YES
			// +"') \n");

			list = factory.select(sql.toString(), null,
					new DAOCallback<TmBrandPO>() {
						public TmBrandPO wrapper(ResultSet rs, int arg1) {
							TmBrandPO tmbrand = new TmBrandPO();
							try {
								tmbrand.setBrandId(rs.getLong("brand_id"));
								tmbrand.setBrandName(rs.getString("brand_name"));
								return tmbrand;
							} catch (SQLException e) {
								throw new DAOException(e);
							}
						}
					});

		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	// /**
	// * Function : 根据条件查询其对应的所有SGM认证品牌
	// * @param : 经销商ID
	// * @return : 满足条件的SGM认证品牌
	// * @throws : Exception
	// * LastUpdate : 2009-09-08
	// */
	// public static List<TrDlrBrandPO> getDlrSGMBrand(String dlrId) throws
	// Exception{
	// StringBuffer sql = new StringBuffer();
	// List<TrDlrBrandPO> list = null;
	// try {
	// sql.append("select \n");
	// sql.append("	a.dlr_id,a.brand_id \n");
	// sql.append("  from tr_dlr_brand a \n");
	// sql.append("	where a.dlr_id =('"+ dlrId +"') \n");
	// //得到SGM认证品牌
	// list = factory.select(sql.toString(), null, new
	// DAOCallback<TrDlrBrandPO>(){
	// public TrDlrBrandPO wrapper(ResultSet rs, int arg1) {
	// TrDlrBrandPO tmdlrbrand = new TrDlrBrandPO();
	// try {
	// tmdlrbrand.setDlrId(rs.getString("dlr_id"));
	// tmdlrbrand.setBrandId(rs.getString("brand_id"));
	// return tmdlrbrand;
	// } catch (SQLException e) {
	// throw new DAOException(e);
	// }
	// }});
	//
	// } catch (Exception e) {
	// throw e;
	// }
	// return list;
	// }

	/**
	 * Function : 查询修改的经销商信息是否已经存在
	 * 
	 * @param : 经销商ID
	 * @param : 经销商名称
	 * @param : 经销商简称
	 * @return : 满足条件的经销商信息
	 * @throws : Exception LastUpdate : 2009-08-10
	 */
	public static List<TmCompanyPO> queryCompanyInfo(String companyCode/*
																		 * ,String
																		 * companyName
																		 * ,
																		 * String
																		 * companyShortname
																		 */)
			throws Exception {// 去掉公司名称和简称的效验判断
		StringBuffer sql = new StringBuffer();
		List<TmCompanyPO> list = null;
		try {
			sql.append("select * \n");
			sql.append("	FROM tm_company a \n");
			sql.append("	WHERE a.company_code in \n");
			sql.append("		(SELECT b.company_code \n");
			sql.append("			FROM tm_company b WHERE (b.company_code = '"
					+ companyCode
					+ /* "' OR b.company_name ='"+ companyName + */"' \n");
			sql.append(/* "				OR b.company_shortname ='"+ companyShortname +"' */" ) and b.status = "
					+ Constant.STATUS_ENABLE + ") \n");
			list = factory.select(sql.toString(), null, new POCallBack(factory,
					TmCompanyPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	/**
	 * Function : 查询新增的经销商信息是否已经存在
	 * 
	 * @param : 经销商ID
	 * @param : 经销商名称
	 * @param : 经销商简称
	 * @param : SAP账号
	 * @return : 满足条件的经销商信息
	 * @throws : Exception LastUpdate : 2009-08-10
	 */
	public static List<TmCompanyPO> getDlr(String companyCode, String companyId)
			throws Exception {// 去掉公司名称和简称的效验判断
		StringBuffer sql = new StringBuffer();
		List<TmCompanyPO> list = null;
		List<Object> params = new ArrayList<Object>();
		try {
			sql.append("select * \n");
			sql.append("	from tm_company a \n");
			sql.append("	where a.company_code=?\n");
			params.add(companyCode);
			if (!companyId.equals("")) {
				sql.append(" and a.company_id <> ?");
				params.add(companyId);
			}

			list = factory.select(sql.toString(), params, new POCallBack(factory,
					TmCompanyPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	public static List<TmCompanyPO> getDlrName(String companyCode,String companyId)
			throws Exception {// 去掉公司名称和简称的效验判断
		StringBuffer sql = new StringBuffer();
		List<TmCompanyPO> list = null;
		List<Object> params = new ArrayList<Object>();
		try 
		{
			sql.append("select * \n");
			sql.append("	from tm_company a \n");
			sql.append("	where  a.company_name=?\n");
			sql.append("	  and  a.STATUS ='" + Constant.STATUS_ENABLE + "'\n");

			params.add(companyCode);
			if(!companyId.equals("")) {
				sql.append("	and a.company_id <> ?");
				params.add(companyId);
			}
			list = factory.select(sql.toString(), params, new POCallBack(factory,
					TmCompanyPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	/**
	 * Function : 根据车厂公司ID查询对应的部门ID
	 * 
	 * @param : 公司ID
	 * @return : 满足条件的部门ID
	 * @throws : Exception LastUpdate : 2009-10-13
	 */
	public static List<TmOrgPO> queryOrgId(String companyId) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<TmOrgPO> list = null;
		try {
			sql.append("select tc.org_id ");
			sql.append("	from tr_company_dept tcd,tm_org tc ");
			sql.append("	WHERE tcd.org_id=tc.org_id and tcd.dept_id=tc.parent_org_id \n");
			sql.append("		and tcd.company_id='" + companyId + "' \n");

			list = factory.select(sql.toString(), null,
					new DAOCallback<TmOrgPO>() {
						public TmOrgPO wrapper(ResultSet rs, int arg1) {
							TmOrgPO dealerBean = new TmOrgPO();
							try {
								dealerBean.setOrgId(rs.getLong("org_id"));
								return dealerBean;
							} catch (SQLException e) {
								throw new DAOException(e);
							}
						}
					});

		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	public static List<TcCompanyLogPO> history(String companyId)
			throws Exception {// 去掉公司名称和简称的效验判断
		StringBuffer sql = new StringBuffer();
		List<TcCompanyLogPO> list = null;
		try 
		{
			//(DECODE(A.STATUS, '1', '待审核', '2', '审核通过', '3', '审核驳回')) AS STATUS,,(SELECT NAME FROM TC_USER WHERE USER_ID = A.CREATE_BY) AS CREATE_BY  
			sql.append("SELECT  A.COMPANY_ID,a.remark ,a.ver,A.OLD_COMPANY_NAME,A.NEW_COMPANY_NAME, A.COMPANY_CODE,(DECODE(A.STATUS, '1', '待审核', '2', '审核通过', '3', '审核驳回')) AS STATUS1,A.STATUS,(SELECT NAME FROM TC_USER WHERE USER_ID = A.CREATE_BY) AS CREATE_BY1 \n");
			sql.append("	FROM TC_COMPANY_LOG A \n");
			sql.append("	WHERE  1=1\n");
//			sql.append("	  AND  A.STATUS ='2'\n");
			if(!companyId.equals("")) {
				sql.append("	AND A.COMPANY_ID =").append(companyId);
			}
			sql.append("	group BY a.STATUS ,a.remark ,A.OLD_COMPANY_NAME,A.NEW_COMPANY_NAME, A.COMPANY_CODE, a.CREATE_BY, A.COMPANY_ID,a.ver");
			sql.append("	ORDER BY a.ver  DESC");
			list = factory.select(sql.toString(), null, new POCallBack(factory,
					TcCompanyLogPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
}
