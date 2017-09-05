package com.infodms.dms.dao.common;

import java.security.SecureRandom;
import java.security.Security;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.BrandAndSeriesBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.bean.DlrBean;
import com.infodms.dms.bean.ModelBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.bean.ProductBean;
import com.infodms.dms.bean.TmModelBean;
import com.infodms.dms.bean.VehicleSeriesBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.MD5Util;
import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.po.TcFuncActionPO;
import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.po.TcOperateRemindPO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserOnlinePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmBrandPO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmModelPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmSeriesPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.dms.chana.po.TtAsClientPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.POCallBack;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

public class CommonDAO {
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * @return 登录成功后用户信息
	 * @throws Exception
	 * @update 修改password的存取方式 by lishuai103@yahoo.com.cn
	 */
	public static AclUserBean getLogon(String user, String password) throws Exception
	{
		AclUserBean aclUser = null;
		ActionContext atx = ActionContext.getContext();
		if (atx.getSession().get("loginCount") == null)
		{
			Integer loginCount = 0;
			atx.getSession().set("loginCount", loginCount);
		}
		try
		{
			TcUserPO po = new TcUserPO();
			po.setAcnt(user);
			List<TcUserPO> usr = factory.select(po);
			if (usr != null && usr.size() > 0)
			{
				po = usr.get(0);
				String s = MD5Util.MD5Encryption(password);
				//added by andy.ten@tom.com
				String pwd = po.getPassword() == null ? "" : po.getPassword();
				if (!s.equals(pwd))
				{
					return null;
				}
				aclUser = new AclUserBean();
				aclUser.setUserId(po.getUserId());
				aclUser.setName(po.getName());
				/**********modify xieyj**************/
				String useType = po.getUserType()==null?"":po.getUserType().toString();
				//判断用户类型
				if(Constant.SYS_USER_SGM.toString().equals(useType)){
					aclUser.setCompanyId(po.getCompanyId());
				}else{
					TmDealerPO dpo = new TmDealerPO();
					dpo.setDealerId(po.getDealerId());
					dpo = factory.select(dpo).get(0);
					aclUser.setCompanyId(dpo.getCompanyId());
				}
				/***********************************/
			//	aclUser.setCompanyId(po.getCompanyId());// modify xieyj
				UserMngDAO ud = new UserMngDAO();
				Map<String, Object> ps = ud.queryPoseBusTypeByUserid(po.getUserId());
				Map<String, Object> ps2 = ud.queryPoseTypeByUserid(po.getUserId());
				aclUser.setPoseBusType(Integer.parseInt(ps.get("POSE_BUS_TYPE").toString()));
				if (null != ps2)
				{
					aclUser.setPoseType(Integer.parseInt(ps2.get("COMPANY_TYPE").toString()));
				}//判断是车厂用户还是公司用户
				/***************************modify xieyj ************************/
				//判断用户类型
				TmCompanyPO com = new TmCompanyPO();
				if(Constant.SYS_USER_SGM.toString().equals(useType)){
					com.setCompanyId(po.getCompanyId());
				}else{
					TmDealerPO dpo = new TmDealerPO();
					dpo.setDealerId(po.getDealerId());
					dpo = factory.select(dpo).get(0);
					com.setCompanyId(dpo.getCompanyId());
				}
				/****************************************************************/
//				com.setCompanyId(po.getCompanyId());//modify xieyj
				Integer comType = factory.select(com).get(0).getCompanyType();
				if (comType.equals(Constant.COMPANY_TYPE_SGM))
				{
					aclUser.setUserType(Constant.SYS_USER_SGM);
				}
				else
				{
					aclUser.setUserType(Constant.SYS_USER_DEALER);
				}
			}
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return aclUser;
	}
	
	public static AclUserBean getRpcLogon(String anct, String id, String clientIp) throws Exception
	{
		AclUserBean aclUser = null;
		ActionContext atx = ActionContext.getContext();
		try
		{
			TtAsClientPO tcpPO = new TtAsClientPO();
			tcpPO.setId(Long.parseLong(id));
			List<TtAsClientPO> tcp = factory.select(tcpPO);
			if (tcp != null && tcp.size() > 0)
			{
				tcpPO = tcp.get(0);
				anct = CommonDAO.decryptor(anct.replaceAll(" ", "+"), tcpPO.getSecretKey());
				clientIp = CommonDAO.decryptor(clientIp.replaceAll(" ", "+"), tcpPO.getSecretKey());
				/* if(anct==null||clientIp==null||!clientIp.equals(tcpPO.getClientIp())){
				 * return null;
				 * } */
				if (anct == null)
				{
					return null;
				}
			}
			else
			{
				return null;
			}
			TcUserPO po = new TcUserPO();
			po.setAcnt(anct.toUpperCase());
			List<TcUserPO> usr = factory.select(po);
			if (usr != null && usr.size() > 0)
			{
				po = usr.get(0);
				aclUser = new AclUserBean();
				aclUser.setUserId(po.getUserId());
				aclUser.setName(po.getName());
				aclUser.setCompanyId(po.getCompanyId());
				UserMngDAO ud = new UserMngDAO();
				Map<String, Object> ps = ud.queryPoseBusTypeByUserid(po.getUserId());
				Map<String, Object> ps2 = ud.queryPoseTypeByUserid(po.getUserId());
				aclUser.setPoseBusType(Integer.parseInt(ps.get("POSE_BUS_TYPE").toString()));
				if (null != ps2)
				{
					aclUser.setPoseType(Integer.parseInt(ps2.get("COMPANY_TYPE").toString()));
				}//判断是车厂用户还是公司用户
				
				TmCompanyPO com = new TmCompanyPO();
				com.setCompanyId(po.getCompanyId());
				Integer comType = factory.select(com).get(0).getCompanyType();
				if (comType.equals(Constant.COMPANY_TYPE_SGM))
				{
					aclUser.setUserType(Constant.SYS_USER_SGM);
				}
				else
				{
					aclUser.setUserType(Constant.SYS_USER_DEALER);
				}
			}
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return aclUser;
	}
	
	/**
	 * @return 切换用户登陆方式检查用户信息
	 * @throws Exception
	 * @update 修改password的存取方式 by yh 2010.11.24
	 */
	public static AclUserBean getUserChangeLogon(String user, String password) throws Exception
	{
		AclUserBean aclUser = null;
		try
		{
			TcUserPO po = new TcUserPO();
			po.setAcnt(user);
			List<TcUserPO> usr = factory.select(po);
			if (usr != null && usr.size() > 0)
			{
				po = usr.get(0);
				/* String s = password;
				 * String pwd = po.getPassword() == null ? "":po.getPassword();
				 * if(!s.equals(pwd)){
				 * return null;
				 * } */
				aclUser = new AclUserBean();
				aclUser.setUserId(po.getUserId());
				aclUser.setName(po.getName());
				aclUser.setCompanyId(po.getCompanyId());
				UserMngDAO ud = new UserMngDAO();
				Map<String, Object> ps = ud.queryPoseBusTypeByUserid(po.getUserId());
				Map<String, Object> ps2 = ud.queryPoseTypeByUserid(po.getUserId());
				if (null != ps)
				{
					aclUser.setPoseBusType(Integer.parseInt(ps.get("POSE_BUS_TYPE").toString()));
				}
				if (null != ps2)
				{
					aclUser.setPoseType(Integer.parseInt(ps2.get("COMPANY_TYPE").toString()));
				}//判断是车厂用户还是公司用户
				
				TmCompanyPO com = new TmCompanyPO();
				com.setCompanyId(po.getCompanyId());
				Integer comType = factory.select(com).get(0).getCompanyType();
				if (comType.equals(Constant.COMPANY_TYPE_SGM))
				{
					aclUser.setUserType(Constant.SYS_USER_SGM);
				}
				else
				{
					aclUser.setUserType(Constant.SYS_USER_DEALER);
				}
			}
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return aclUser;
	}
	
	/**
	 * @return 登录成功后用户信息
	 * @throws Exception
	 */
	public static AclUserBean getLogonUser(String acnt) throws Exception
	{
		AclUserBean aclUser = null;
		try
		{
			TcUserPO po = new TcUserPO();
			po.setAcnt(acnt); 								// 认证中心帐号
			List<TcUserPO> usr = factory.select(po);
			if (usr != null && usr.size() > 0)
			{
				po = usr.get(0);
				aclUser = new AclUserBean();
				aclUser.setUserId(po.getUserId());
				aclUser.setName(po.getName());
				aclUser.setCompanyId(po.getCompanyId());
				TmCompanyPO com = new TmCompanyPO();
				com.setCompanyId(po.getCompanyId());
				Integer comType = factory.select(com).get(0).getCompanyType();
				if (comType.equals(Constant.COMPANY_TYPE_SGM))
				{
					aclUser.setUserType(Constant.SYS_USER_SGM);
				}
				else
				{
					aclUser.setUserType(Constant.SYS_USER_DEALER);
				}
				// added by andy.ten@tom.com 增加登录账户 dealer_id
//				if(Constant.SYS_USER_DEALER == aclUser.getUserType())
//				{
//					TmDealerPO 
//					Long dealerId = factory.select(arg0).
//					aclUser.setDealerId(dealerId);
//				}
				
			}
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return aclUser;
	}
	
	/**
	 * function 记录用户登录信息
	 * 
	 * @author witti
	 * @exception Exception
	 * @return void
	 * @throws Exception
	 */
	public static void addUserOnlineInfo(AclUserBean usr, String ip) throws Exception
	{
		try
		{
			TcUserOnlinePO usrOnline = new TcUserOnlinePO();
			usrOnline.setUserOnlineId(factory.getLongPK(usrOnline));
			usrOnline.setCreateBy(new Long(-1));
			usrOnline.setOrgId(usr.getOrgId());
			usrOnline.setLoginDate(new Date(System.currentTimeMillis()));
			usrOnline.setUserId(usr.getUserId());
			usrOnline.setUserIp(ip);
			usrOnline.setUserOnlineStatus(Constant.IF_TYPE_YES);
			factory.insert(usrOnline);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * function 更新用户登录信息
	 * 
	 * @author witti
	 * @exception Exception
	 * @return void
	 * @throws Exception
	 */
	public static void updateUserOnlineInfo(AclUserBean usr, Long orgId) throws Exception
	{
		try
		{
			TcUserOnlinePO usrOnline = new TcUserOnlinePO();
			usrOnline.setUserId(usr.getUserId());
			TcUserOnlinePO value = new TcUserOnlinePO();
			value.setOrgId(orgId);
			factory.update(usrOnline, value);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * function 注销用户登录信息
	 * 
	 * @author witti
	 * @exception Exception
	 * @return void
	 * @throws Exception
	 */
	public static void disableUserOnlineInfo(Long userId) throws Exception
	{
		try
		{
			TcUserOnlinePO usrOnline = new TcUserOnlinePO();
			usrOnline.setUserId(userId);
			usrOnline.setUserOnlineStatus(Constant.IF_TYPE_YES);
			TcUserOnlinePO value = new TcUserOnlinePO();
			value.setUserOnlineStatus(Constant.IF_TYPE_NO);
			factory.update(usrOnline, value);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * function 记录用户登录时间
	 * 
	 * @author witti
	 * @exception Exception
	 * @return void
	 * @throws Exception
	 */
	public static void logUserOnlineTime(Long userId) throws Exception
	{
		try
		{
			TcUserPO usr = new TcUserPO();
			usr.setUserId(userId);
			TcUserPO value = new TcUserPO();
			value.setLastsigninTime(new Date(System.currentTimeMillis()));
			factory.update(usr, value);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * function 查询用户登录是否登录
	 * 
	 * @author witti
	 * @exception Exception
	 * @return void
	 * @throws Exception
	 */
	public static boolean isLogUserOnline(Long userId) throws Exception
	{
		boolean bo = false;
		try
		{
			TcUserOnlinePO usrOnline = new TcUserOnlinePO();
			usrOnline.setUserId(userId);
			usrOnline.setUserOnlineStatus(Constant.IF_TYPE_YES);
			List<TcUserOnlinePO> user = factory.select(usrOnline);
			if (user != null && user.size() > 0)
			{
				bo = true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return bo;
	}
	
	/**
	 * 厂家车系选择公用模块-查询品牌
	 * 
	 * @param brandName
	 * @return
	 */
	public static Map<String, List<TmBrandPO>> selBrands(String brandName)
	{
		String sql = "select * from tm_brand b where b.brand_name like '%"
						+ (brandName != null ? brandName : "")
						+ "%' order by nlssort(b.brand_name,'NLS_SORT=SCHINESE_PINYIN_M')";
		List<TmBrandPO> brandList = factory.select(sql, null,
						new POCallBack<TmBrandPO>(factory, TmBrandPO.class));
		Map<String, List<TmBrandPO>> map = new HashMap<String, List<TmBrandPO>>();
		List<TmBrandPO> _List = null;
		for (TmBrandPO po : brandList)
		{
			if (!map.containsKey(StringUtil
							.getAllFirstLetter(po.getBrandName())))
			{
				_List = new ArrayList<TmBrandPO>();
				map.put(StringUtil.getAllFirstLetter(po.getBrandName()), _List);
				_List.add(po);
			}
			else
			{
				_List.add(po);
			}
		}
		return map;
	}
	
	/**
	 * 厂家车系选择公用模块-查询品牌(根据经销商与品牌关系进行过滤)
	 * 
	 * @param brandName
	 * @param dealerId
	 * @return
	 */
	public static List<TmBrandPO> selBrandsByDlr(Long company)
	{
		TmBrandPO condition = new TmBrandPO();
//		condition.setSgmBrandYn(Constant.IF_TYPE_YES);
		List<TmBrandPO> brandList = factory.select(condition);
		return brandList;
	}
	
	/**
	 * 厂家车系选择公用模块-查询车系
	 * 
	 * @param brandId
	 * @return
	 */
	public static List<TmSeriesPO> selSeries(Long brandId)
	{
		TmSeriesPO po = new TmSeriesPO();
		po.setBrandId(brandId);
		List<TmSeriesPO> list = factory.select(po);
		return list;
	}
	
	/**
	 * 根据经销商代码获取经销商
	 * 
	 * @param dlrCode
	 * @return
	 */
	public static TmCompanyPO getDlrByCode(String dlrCode)
	{
		TmCompanyPO po = new TmCompanyPO();
		po.setCompanyCode(dlrCode);
		List<TmCompanyPO> list = factory.select(po);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	/**
	 * 根据经销商ID获取经销商
	 * 
	 * @param id
	 * @return
	 */
	public static TmCompanyPO getDlrByID(Long id)
	{
		TmCompanyPO po = new TmCompanyPO();
		po.setCompanyId(id);
		List<TmCompanyPO> list = factory.select(po);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	/**
	 * 查询所有组织
	 */
	public static List<OrgBean> selOrgs()
	{
		StringBuffer sql = new StringBuffer();
		// modified by andy.ten@tom.com
		sql.append(" select ORG_ID,PARENT_ORG_ID,ORG_NAME  from (SELECT ORG_ID, PARENT_ORG_ID,ORG_NAME ");
		sql.append(" FROM TM_ORG  WHERE DUTY_TYPE in ( ");
		sql.append(Constant.DUTY_TYPE_DEPT);
		sql.append(",");
		sql.append(Constant.DUTY_TYPE_LARGEREGION);
		sql.append(",");
		sql.append(Constant.DUTY_TYPE_SMALLREGION);
		sql.append(") UNION  SELECT ORG_ID, 1000000000 PARENT_ORG_ID,ORG_NAME   FROM TM_ORG ");
		sql.append(" WHERE ORG_TYPE = ");
		sql.append(Constant.ORG_TYPE_OEM);
		sql.append(" )  START WITH PARENT_ORG_ID = 1000000000");
		sql.append(" connect by prior ORG_ID = PARENT_ORG_ID ");
		// end
		List<OrgBean> list = factory.select(sql.toString(), null,
						new DAOCallback<OrgBean>() {
							public OrgBean wrapper(ResultSet rs, int idx)
							{
								OrgBean bean = new OrgBean();
								try
								{
									bean.setId(rs.getString("org_id"));
									bean.setName(rs.getString("org_name"));
									bean.setPid(rs.getString("parent_org_id"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * 查询所有树型组织
	 */
	public static List<OrgBean> selTreeOrgs()
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select ORG_ID,PARENT_ORG_ID,ORG_NAME  from (SELECT ORG_ID, PARENT_ORG_ID,ORG_NAME ");
		// modified by andy.ten@tom.com
		sql.append(" FROM TM_ORG  WHERE DUTY_TYPE in ( ");
		sql.append(Constant.DUTY_TYPE_LARGEREGION);
		sql.append(",");
		sql.append(Constant.DUTY_TYPE_SMALLREGION);
		sql.append(") UNION  SELECT ORG_ID,-1 PARENT_ORG_ID,ORG_NAME   FROM TM_ORG ");
		sql.append(" WHERE ORG_CODE = '" + Constant.ORG_ROOT_CODE + "' ");
		sql.append(" )  START WITH PARENT_ORG_ID = -1");
		sql.append(" connect by prior ORG_ID = PARENT_ORG_ID ");
		// end
		List<OrgBean> list = factory.select(sql.toString(), null,
						new DAOCallback<OrgBean>() {
							public OrgBean wrapper(ResultSet rs, int idx)
							{
								OrgBean bean = new OrgBean();
								try
								{
									bean.setId(rs.getString("org_id"));
									bean.setName(rs.getString("org_name"));
									bean.setPid(rs.getString("parent_org_id"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * 查询所有组织
	 */
	public static List<ModelBean> selProduct()
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT PRO.PRO_ID ,PRO.PRO_CODE,PRO.PRO_NAME,PRO.PARENT_PRO_ID ");
		sql.append(" FROM VW_PRODUCT_TREE PRO WHERE  PRO.PRO_ID <> PRO.PARENT_PRO_ID ");
		
		List<ModelBean> list = factory.select(sql.toString(), null,
						new DAOCallback<ModelBean>() {
							public ModelBean wrapper(ResultSet rs, int idx)
							{
								ModelBean bean = new ModelBean();
								try
								{
									bean.setId(rs.getString("PRO_ID"));
									bean.setName(rs.getString("PRO_NAME"));
									bean.setPid(rs.getString("PARENT_PRO_ID"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * 根据组织查询经销商信息
	 * modify by xiayanpeng 经销商表结构修改DEALER_SHORTNAME改为DEALER_NAME
	 */
	public static PageResult<DlrBean> selDlr(Long companyId, String orgId, String dealerCode,
					String dealerName, int pageSize, int curPage)
	{
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		List<TmOrgPO> zDept = getXjbmOrg(orgId, companyId, list);
		String indept = "";
		if (zDept != null && zDept.size() > 0)
		{
			for (int i = 0; i < zDept.size(); i++)
			{
				indept += zDept.get(i).getOrgId() + ",";
			}
			indept = indept.substring(0, indept.length() - 1) + "," + orgId;
		}
		
		if ("".equals(indept))
		{
			indept = String.valueOf(orgId);
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select t.DEALER_ID,ORG.ORG_ID, t.DEALER_CODE, t.DEALER_NAME, t.DEALER_NAME,t.DEALER_NAME||'('||t.DEALER_CODE||')' dept_name from TM_DEALER t,TM_DEALER_ORG_RELATION REL ");
		sql.append(" ,TM_ORG ORG where  t.DEALER_TYPE = ");
		sql.append(Constant.DUTY_TYPE_DEALER);
		sql.append(" and t.STATUS = ");
		sql.append(Constant.STATUS_ENABLE);
		sql.append(" AND t.DEALER_ID = REL.DEALER_ID AND ORG.ORG_ID = REL.ORG_ID ");
		sql.append(dealerCode != null ? "		and t.DEALER_CODE like '%"
						+ dealerCode + "%'" : "");
		sql.append(dealerName != null ? "		and t.DEALER_NAME like '%"
						+ dealerName + "%'" : "");
		if (zDept != null && !zDept.equals(""))
		{
			
			sql.append(" AND ORG.PARENT_ORG_ID IN (");
			sql.append(indept);
			sql.append(" ) ");
		}
		PageResult<DlrBean> rs = factory.pageQuery(sql.toString(), null,
						new DAOCallback<DlrBean>() {
							public DlrBean wrapper(ResultSet rs, int idx)
							{
								DlrBean bean = new DlrBean();
								try
								{
									//bean.setDlrId(rs.getString("ORG_ID"));  修改orgId为dealerId   lishuai103@yahoo.com.cn
									bean.setDlrId(rs.getString("DEALER_ID"));
									bean.setDlrCode(rs.getString("DEALER_CODE"));
									bean.setDlrName(rs.getString("DEALER_NAME"));
									bean.setDlrShortName(rs.getString("DEALER_NAME"));
									bean.setCodeName(rs.getString("dept_name"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						}, pageSize, curPage);
		return rs;
	}
	
	/**
	 * 根据组织查询经销商信息
	 */
	public static PageResult<DlrBean> selDlrs(Long companyId, String orgId, String brandIds, String dealerIds,
					String dealerCode,
					String dealerName, int pageSize, int curPage)
	{
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		List<TmOrgPO> zDept = getXjbmOrg(orgId, companyId, list);
		String indept = "";
		if (zDept != null && zDept.size() > 0)
		{
			for (int i = 0; i < zDept.size(); i++)
			{
				indept += zDept.get(i).getOrgId() + ",";
			}
			indept = indept.substring(0, indept.length() - 1) + "," + orgId;
		}
		
		if ("".equals(indept))
		{
			indept = String.valueOf(orgId);
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select t.DEALER_ID,ORG.ORG_ID, t.DEALER_CODE, t.DEALER_NAME, t.DEALER_SHORTNAME,t.DEALER_SHORTNAME||'('||t.DEALER_CODE||')' dept_name from TM_DEALER t,TM_DEALER_ORG_RELATION REL ");
		sql.append(" ,TM_ORG ORG where  t.DEALER_TYPE = ");
		sql.append(Constant.DUTY_TYPE_DEALER);
		sql.append(" and t.STATUS = ");
		sql.append(Constant.STATUS_ENABLE);
		sql.append(" AND t.DEALER_ID = REL.DEALER_ID AND ORG.ORG_ID = REL.ORG_ID ");
		sql.append(dealerCode != null ? "		and t.DEALER_CODE like '%"
						+ dealerCode + "%'" : "");
		sql.append(dealerName != null ? "		and t.DEALER_NAME like '%"
						+ dealerName + "%'" : "");
		if (zDept != null && !zDept.equals(""))
		{
			
			sql.append(" AND ORG.PARENT_ORG_ID IN (");
			sql.append(indept);
			sql.append(" ) ");
		}
		if (dealerIds != null && !"".equals(dealerIds))
		{
			sql.append(" AND ORG.ORG_ID NOT IN (");
			sql.append(dealerIds);
			sql.append(" ) ");
		}
		if (brandIds != null && !"".equals(brandIds))
		{
			sql.append(" AND ORG.ORG_ID IN (");
			sql.append(" SELECT DISTINCT  ORK.ORG_ID FROM TM_ORG ORK,TM_ORG_BUSINESS_AREA AEE  ");
			sql.append(" WHERE AEE.ORG_ID = ORK.ORG_ID AND ORK.DUTY_TYPE = ");
			sql.append(Constant.DUTY_TYPE_DEALER);
			sql.append(" AND ORK.STATUS = ");
			sql.append(Constant.STATUS_ENABLE);
			sql.append(" AND AEE.BRAND_ID IN (");
			sql.append(brandIds);
			sql.append(" )) ");
		}
		PageResult<DlrBean> rs = factory.pageQuery(sql.toString(), null,
						new DAOCallback<DlrBean>() {
							public DlrBean wrapper(ResultSet rs, int idx)
							{
								DlrBean bean = new DlrBean();
								try
								{
									bean.setDlrId(rs.getString("ORG_ID"));
									bean.setDlrCode(rs.getString("DEALER_CODE"));
									bean.setDlrName(rs.getString("DEALER_NAME"));
									bean.setDlrShortName(rs.getString("DEALER_SHORTNAME"));
									bean.setCodeName(rs.getString("dept_name"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						}, pageSize, curPage);
		return rs;
	}
	
	/**
	 * 根据组织查询经销商信息
	 */
	public static List<DlrBean> selDlrs(Long companyId, String orgId, String brandIds, String dealerIds,
					String dealerCode, String dealerName)
	{
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		List<TmOrgPO> zDept = getXjbmOrg(orgId, companyId, list);
		String indept = "";
		if (zDept != null && zDept.size() > 0)
		{
			for (int i = 0; i < zDept.size(); i++)
			{
				indept += zDept.get(i).getOrgId() + ",";
			}
			indept = indept.substring(0, indept.length() - 1) + "," + orgId;
		}
		
		if ("".equals(indept))
		{
			indept = String.valueOf(orgId);
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select t.DEALER_ID,ORG.ORG_ID, t.DEALER_CODE, t.DEALER_NAME, t.DEALER_SHORTNAME,t.DEALER_SHORTNAME||'('||t.DEALER_CODE||')' dept_name from TM_DEALER t,TM_DEALER_ORG_RELATION REL ");
		sql.append(" ,TM_ORG ORG where  t.DEALER_TYPE = ");
		sql.append(Constant.DUTY_TYPE_DEALER);
		sql.append(" and t.STATUS = ");
		sql.append(Constant.STATUS_ENABLE);
		sql.append(" AND t.DEALER_ID = REL.DEALER_ID AND ORG.ORG_ID = REL.ORG_ID ");
		sql.append(dealerCode != null ? "		and t.DEALER_CODE like '%"
						+ dealerCode + "%'" : "");
		sql.append(dealerName != null ? "		and t.DEALER_NAME like '%"
						+ dealerName + "%'" : "");
		if (zDept != null && !zDept.equals(""))
		{
			
			sql.append(" AND ORG.PARENT_ORG_ID IN (");
			sql.append(indept);
			sql.append(" ) ");
		}
		if (dealerIds != null && !"".equals(dealerIds))
		{
			sql.append(" AND ORG.ORG_ID NOT IN (");
			sql.append(dealerIds);
			sql.append(" ) ");
		}
		if (brandIds != null && !"".equals(brandIds))
		{
			//modified by andy.ten@tom.com
			sql.append(" AND ORG.ORG_ID IN (");
			sql.append(" SELECT DISTINCT  ORK.ORG_ID FROM TM_ORG ORK,TM_ORG_BUSINESS_AREA AEE  ");
			sql.append(" WHERE AEE.ORG_ID = ORK.ORG_ID AND ORK.DUTY_TYPE = ");
			sql.append(Constant.DUTY_TYPE_DEALER);
			sql.append(" AND ORK.STATUS = ");
			sql.append(Constant.STATUS_ENABLE);
			sql.append(" AND AEE.BRAND_ID IN (");
			sql.append(brandIds);
			sql.append(" )) ");
			//end
		}
		List<DlrBean> rs = factory.select(sql.toString(), null,
						new DAOCallback<DlrBean>() {
							public DlrBean wrapper(ResultSet rs, int idx)
							{
								DlrBean bean = new DlrBean();
								try
								{
									bean.setDlrId(rs.getString("ORG_ID"));
									bean.setDlrCode(rs.getString("DEALER_CODE"));
									bean.setDlrName(rs.getString("DEALER_NAME"));
									bean.setDlrShortName(rs.getString("DEALER_SHORTNAME"));
									bean.setCodeName(rs.getString("dept_name"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return rs;
	}
	
	public static List<DlrBean> getDealerQueryByIDealerIds(String dealerIds)
	{
		
		StringBuilder sql = new StringBuilder();
		sql.append("select t.DEALER_ID,ORG.ORG_ID, t.DEALER_CODE, t.DEALER_SHORTNAME, PAR.ORG_NAME from TM_DEALER t,TM_DEALER_ORG_RELATION REL ");
		sql.append(" ,TM_ORG ORG,TM_ORG PAR where  t.DEALER_TYPE = ");
		sql.append(Constant.DUTY_TYPE_DEALER);
		sql.append(" and t.STATUS = ");
		sql.append(Constant.STATUS_ENABLE);
		sql.append(" AND t.DEALER_ID = REL.DEALER_ID AND ORG.ORG_ID = REL.ORG_ID AND ORG.PARENT_ORG_ID = PAR.ORG_ID ");
		if (dealerIds != null && !"".equals(dealerIds))
		{
			sql.append(" AND ORG.ORG_ID IN (");
			sql.append(dealerIds);
			sql.append(" ) ");
		}
		return factory.select(sql.toString(), null, new DAOCallback<DlrBean>() {
			public DlrBean wrapper(ResultSet rs, int idx)
			{
				DlrBean bean = new DlrBean();
				try
				{
					bean.setDlrId(rs.getString("ORG_ID"));
					bean.setDlrCode(rs.getString("DEALER_CODE"));
					bean.setDlrShortName(rs.getString("DEALER_SHORTNAME"));
					bean.setCodeName(rs.getString("ORG_NAME"));
				}
				catch (SQLException e)
				{
					throw new DAOException(e);
				}
				return bean;
			}
		});
		
	}
	
	public static PageResult<CompanyBean> selCom(String companyCode,
					String companyName, int pageSize, int curPage, Long companyId)
	{
		
		StringBuilder sql = new StringBuilder();
		sql.append("select t.COMPANY_ID, t.COMPANY_CODE, t.COMPANY_NAME, t.COMPANY_SHORTNAME from TM_COMPANY t ");
		sql.append(" where  t.COMPANY_TYPE <> ");
		sql.append(Constant.COMPANY_TYPE_SGM);
		sql.append(" and t.STATUS = ");
		sql.append(Constant.STATUS_ENABLE);
		sql.append(" and t.OEM_COMPANY_ID = " + companyId);
		sql.append(companyCode != null ? "		and t.COMPANY_CODE like '%"
						+ companyCode + "%'" : "");
		sql.append(companyName != null ? "		and t.COMPANY_NAME like '%"
						+ companyName + "%'" : "");
		
		PageResult<CompanyBean> rs = factory.pageQuery(sql.toString(), null,
						new DAOCallback<CompanyBean>() {
							public CompanyBean wrapper(ResultSet rs, int idx)
							{
								CompanyBean bean = new CompanyBean();
								try
								{
									bean.setCompanyId(rs.getString("COMPANY_ID"));
									bean.setCompanyCode(rs.getString("COMPANY_CODE"));
									bean.setCompanyName(rs.getString("COMPANY_NAME"));
									bean.setCompanyShortname(rs.getString("COMPANY_SHORTNAME"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						}, pageSize, curPage);
		return rs;
	}
	
	/**
	 * 通过PK拿到唯一PO对象
	 * 
	 * @param po
	 * @return
	 */
	public static <T extends PO> T getPoByPk(T po)
	{
		List<T> list = factory.select(po);
		return list != null && list.size() > 0 ? list.get(0) : po;
	}
	
	/**
	 * 
	 * @param vhclId
	 * @param holdStat
	 */
	public static void setHold(String vhclId, String holdStat)
	{
		factory.update("update tm_vhcl set hold_stat = ? where vhcl_id = ?",
						Arrays.asList(new Object[] { holdStat, vhclId }));
	}
	
	/**
	 * create by LAX create_date 2009-08-31
	 * 
	 * @param orgId
	 *            组织ID 根据组织ID判断其是否有子组织
	 */
	public static List<TmOrgPO> getOrgChild(String orgId)
	{
		StringBuffer sql = new StringBuffer();
		List<TmOrgPO> list = null;
		try
		{
			sql.append("select ");
			sql.append("	distinct(t.org_id) as org_id, parent_org_id, org_name, ");
			sql.append("	org_code,create_by,update_by,create_date,update_date,status,org_desc ");
			sql.append("  from tm_org t start with t.org_id = '" + orgId
							+ "' ");
			sql
							.append("	connect by  nocycle prior t.org_id =  t.parent_org_id ");
			sql.append("	order by t.parent_org_id,t.org_id ");
			list = factory.select(sql.toString(), null, new POCallBack(factory,
							TmOrgPO.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 部门树 createBy ChenLiang createDate 2009-08-31
	 * 
	 * @param companyId
	 *            公司ID
	 * @param parDeptId
	 *            上级部门ID
	 * @param userType
	 *            部门类型
	 * @param isStat
	 *            是否有效
	 */
	public static List<TmOrgPO> createDeptTree(Long companyId, String parOrgId,
			Integer userType, boolean isStat) throws Exception {
		List<TmOrgPO> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select td.ORG_ID,td.PARENT_ORG_ID,td.ORG_desc,td.ORG_name,td.ORG_code,td.name_sort"
				+ " from tm_ORG td where  td.status = " + Constant.STATUS_ENABLE);
		sql.append(" and td.org_type = " + Constant.ORG_TYPE_OEM);
		
		if (userType != null && !userType.equals("") && userType != 0 && userType.equals(Constant.SYS_USER_SGM)) {
			sql.append(" and td.company_id = " + companyId + "");
		} else if (userType != null && !userType.equals("") && userType != 0 && userType.equals(Constant.SYS_USER_DEALER)) {
			sql.append(" and td.company_id = " + Constant.OEM_ACTIVITIES + "");
		}
		
		if (isStat) {
			sql.append(" and td.status = '" + Constant.STATUS_ENABLE + "' ");
		}
		if (parOrgId == null || "".equals(parOrgId) || "null".equals(parOrgId)) {
		} else {
			sql.append(" and td.PARENT_ORG_ID = 2010010100070674");
		}

		list = factory.select(sql.toString(), null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(Long.valueOf(rs.getString("ORG_ID")));
					// modify by zhaojinyu 上级为空时设置为-100解决报错问题。
					if (rs.getString("PARENT_ORG_ID") != null) {
						bean.setParentOrgId(Long.valueOf(rs
								.getString("PARENT_ORG_ID")));
					} else {
						bean.setParentOrgId(new Long(-100));
					}
					// end
					bean.setOrgDesc(rs.getString("ORG_DESC"));
					bean.setOrgCode(rs.getString("ORG_CODE"));
					bean.setOrgName(rs.getString("ORG_NAME"));
				} catch (SQLException e) {
					throw new DAOException(e);
				}
				return bean;
			}
		});
		return list;
	}
	
	/**
	 * 部门树 createBy ChenLiang createDate 2009-08-31
	 * 
	 * @param companyId
	 *            公司ID
	 * @param parDeptId
	 *            上级部门ID
	 * @param userType
	 *            部门类型
	 * @param isStat
	 *            是否有效
	 */
	public static List<TmOrgPO> createRootDeptTree(Long companyId, String parOrgId,
			Integer userType, boolean isStat, String unorgid) throws Exception {
		List<TmOrgPO> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select td.ORG_ID,td.PARENT_ORG_ID,td.ORG_desc,td.ORG_name,td.ORG_code,td.name_sort"
				+ " from tm_org td where td.status = " + Constant.STATUS_ENABLE);
		sql.append(" and td.org_type = " + Constant.ORG_TYPE_OEM);
		
		if(!"".equals(unorgid)) {
			sql.append(" and td.org_level <> 3 ");
		}
		
		if (userType != null && !userType.equals("") && userType != 0 && userType.equals(Constant.SYS_USER_SGM)) {
			sql.append(" and td.company_id = " + companyId + "");
		} else if (userType != null && !userType.equals("") && userType != 0 && userType.equals(Constant.SYS_USER_DEALER)) {
			sql.append(" and td.company_id = " + Constant.OEM_ACTIVITIES + "");
		}
		
		if (isStat) {
			sql.append(" and td.status = '" + Constant.STATUS_ENABLE + "' ");
		}
		if (parOrgId == null || "".equals(parOrgId) || "null".equals(parOrgId)) {
		} else {
			sql.append(" and td.PARENT_ORG_ID = 2010010100070674");
		}

		list = factory.select(sql.toString(), null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(Long.valueOf(rs.getString("ORG_ID")));
					// modify by zhaojinyu 上级为空时设置为-100解决报错问题。
					if (rs.getString("PARENT_ORG_ID") != null) {
						bean.setParentOrgId(Long.valueOf(rs
								.getString("PARENT_ORG_ID")));
					} else {
						bean.setParentOrgId(new Long(-100));
					}
					// end
					bean.setOrgDesc(rs.getString("ORG_DESC"));
					bean.setOrgCode(rs.getString("ORG_CODE"));
					bean.setOrgName(rs.getString("ORG_NAME"));
				} catch (SQLException e) {
					throw new DAOException(e);
				}
				return bean;
			}
		});
		return list;
	}
	
	public static List<TmOrgPO> createDealerOrgTree(Long companyId) throws Exception
	{
		List<TmOrgPO> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select td.ORG_ID,td.ORG_ID PARENT_ORG_ID,td.ORG_desc,td.ORG_name,td.ORG_code ");
		sql.append(" from tm_ORG td where TD.company_id = ");
		sql.append(companyId);
		sql.append(" and td.status = ");
		sql.append(Constant.STATUS_ENABLE);
		
		list = factory.select(sql.toString(), null,
						new DAOCallback<TmOrgPO>() {
							public TmOrgPO wrapper(ResultSet rs, int idx)
							{
								TmOrgPO bean = new TmOrgPO();
								try
								{
									bean.setOrgId(Long.valueOf(rs.getString("ORG_ID")));
									bean.setParentOrgId(Long.valueOf(rs.getString("PARENT_ORG_ID")));
									bean.setOrgDesc(rs.getString("ORG_DESC"));
									bean.setOrgCode(rs.getString("ORG_CODE"));
									bean.setOrgName(rs.getString("ORG_NAME"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	public static List<ProductBean> createProductTree(String parOrgId) throws Exception
	{
		List<ProductBean> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT PRO.PRO_ID ,PRO.PRO_CODE,PRO.PRO_NAME,PRO.PARENT_PRO_ID ");
		sql.append(" FROM VW_PRODUCT_TREE PRO WHERE 1 = 1 ");
		if (parOrgId == null || "".equals(parOrgId))
		{
			sql.append("           and PRO.PARENT_PRO_ID =");
			sql.append("               (select ty.PRO_ID");
			sql
							.append("                  from VW_PRODUCT_TREE ty ");
			sql.append("                 where  ty.PRO_ID = ty.PARENT_PRO_ID )");
		}
		else
		{
			sql.append(" and PRO.PARENT_PRO_ID = '" + parOrgId);
			sql.append("' and PRO.PRO_ID <> PRO.PARENT_PRO_ID");
		}
		
		list = factory.select(sql.toString(), null,
						new DAOCallback<ProductBean>() {
							public ProductBean wrapper(ResultSet rs, int idx)
							{
								ProductBean bean = new ProductBean();
								try
								{
									bean.setProId(rs.getString("PRO_ID"));
									bean.setParentProId(rs.getString("PARENT_PRO_ID"));
									bean.setProCode(rs.getString("PRO_CODE"));
									bean.setProName(rs.getString("PRO_NAME"));
									
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	public static PageResult<TmModelPO> getModelByProId(String proId, String modelCode, String modelName, int curPage,
					int pageSize, String orderName, String da) throws Exception
	{
		List<ProductBean> list = new ArrayList<ProductBean>();
		List<ProductBean> zPro = getProXjbm(proId, list);
		String indept = "";
		if (zPro != null && zPro.size() > 0)
		{
			for (int i = 0; i < zPro.size(); i++)
			{
				indept += "'" + zPro.get(i).getProId() + "',";
			}
			indept = indept.substring(0, indept.length() - 1);
		}
		
		if ("".equals(indept))
		{
			indept = String.valueOf(proId);
		}
		
		StringBuffer query = new StringBuffer();
		query.append(" SELECT MODEL_ID,MODEL_NAME,MODEL_CODE ");
		query.append(" FROM TM_MODEL  WHERE  STATUS = ");
		query.append(Constant.STATUS_ENABLE);
		if (zPro != null && !zPro.equals(""))
		{
			query.append(" AND 'T'||CAR_TYPE_ID IN (");
			query.append(indept);
			query.append(" ) ");
		}
		if (modelCode != null && !modelCode.equals(""))
		{
			query.append(" AND MODEL_CODE LIKE '%");
			query.append(modelCode);
			query.append("%' ");
		}
		if (modelName != null && !modelName.equals(""))
		{
			query.append(" and MODEL_NAME like '%");
			query.append(modelName);
			query.append("%' ");
		}
		return factory.pageQuery(query.toString(), null, new DAOCallback<TmModelPO>() {
			public TmModelPO wrapper(ResultSet rs, int idx)
			{
				TmModelPO bean = new TmModelPO();
				try
				{
					bean.setModelCode(rs.getString("MODEL_CODE"));
					bean.setModelId(rs.getLong("MODEL_ID"));
					bean.setModelName(rs.getString("MODEL_NAME"));
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}
	
	public static PageResult<TmModelBean> getModelByProIdWholeSale(String proId, String modelCode, String modelName,
					int curPage,
					int pageSize, String orderName, String da) throws Exception
	{
		List<ProductBean> list = new ArrayList<ProductBean>();
		List<ProductBean> zPro = getProXjbm(proId, list);
		String indept = "";
		if (zPro != null && zPro.size() > 0)
		{
			for (int i = 0; i < zPro.size(); i++)
			{
				indept += "'" + zPro.get(i).getProId() + "',";
			}
			indept = indept.substring(0, indept.length() - 1);
		}
		
		if ("".equals(indept))
		{
			indept = String.valueOf(proId);
		}
		
		StringBuffer query = new StringBuffer();
		query.append(" SELECT E.MODEL_ID,E.MODEL_NAME,E.MODEL_CODE,A.BRAND_ID,A.BRAND_NAME,E.PRICE_ONE");
		query.append("   FROM TM_BRAND  A,TM_SERIES B, ");
		query.append("   TM_CAR_NAME   C,  TM_CAR_SHORT_NAME D, ");
		query.append("   TM_MODEL E WHERE A.BRAND_ID = B.BRAND_ID");
		query.append("    AND B.SERIES_ID = C.SERIES_ID");
		query.append("    AND C.CAR_NAME_ID = D.CAR_NAME_ID");
		query.append("    AND D.CAR_TYPE_ID = E.CAR_TYPE_ID");
		query.append("   AND  E.STATUS = ");
		query.append(Constant.STATUS_ENABLE);
		if (zPro != null && !zPro.equals(""))
		{
			query.append(" AND 'T'||E.CAR_TYPE_ID IN (");
			query.append(indept);
			query.append(" ) ");
		}
		if (modelCode != null && !modelCode.equals(""))
		{
			query.append(" AND E.MODEL_CODE LIKE '%");
			query.append(modelCode);
			query.append("%' ");
		}
		if (modelName != null && !modelName.equals(""))
		{
			query.append(" and E.MODEL_NAME like '%");
			query.append(modelName);
			query.append("%' ");
		}
		return factory.pageQuery(query.toString(), null, new DAOCallback<TmModelBean>() {
			public TmModelBean wrapper(ResultSet rs, int idx)
			{
				TmModelBean bean = new TmModelBean();
				try
				{
					bean.setModelCode(rs.getString("MODEL_CODE"));
					bean.setModelId(rs.getLong("MODEL_ID"));
					bean.setModelName(rs.getString("MODEL_NAME"));
					bean.setPriceOne(rs.getDouble("PRICE_ONE"));
					bean.setBrandName(rs.getString("BRAND_NAME"));
					bean.setBrandId(rs.getLong("BRAND_ID"));
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}
	
	private static List<ProductBean> getProXjbm(String proId, List<ProductBean> retLst)
	{
		if (proId == null || "".equals(proId))
		{
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select PRO_ID,PARENT_PRO_ID  from (SELECT PRO_ID, PARENT_PRO_ID ");
		sql.append(" FROM VW_PRODUCT_TREE  WHERE  PRO_ID <> PARENT_PRO_ID ");
		sql.append(" UNION  SELECT PRO_ID, 'A' || 1000000000 PARENT_PRO_ID   FROM VW_PRODUCT_TREE ");
		sql.append(" WHERE PRO_ID = PARENT_PRO_ID ");
		sql.append(" )  START WITH PRO_ID = '");
		sql.append(proId);
		sql.append("' connect by prior PRO_ID = PARENT_PRO_ID ");
		retLst = factory.select(sql.toString(), null,
						new DAOCallback<ProductBean>() {
							public ProductBean wrapper(ResultSet rs, int idx)
							{
								ProductBean bean = new ProductBean();
								try
								{
									bean.setProId(rs.getString("PRO_ID"));
									bean.setParentProId(rs.getString("PARENT_PRO_ID"));
								}
								catch (SQLException e)
								{
									e.printStackTrace();
								}
								return bean;
							}
						});
		
		return retLst;
	}
	
	/**
	 * 通过用户ID得到功能列表 createBy ChenLiang createDate 2009-09-23
	 * 
	 * @param userId
	 *            用户ID
	 */
	public static List<TcFuncPO> getFuncByUserId(Long userId) throws Exception
	{
		List<TcFuncPO> list = null;
		StringBuffer sql = new StringBuffer();
		sql
						.append("select DISTINCT tpf.func_id from tr_user_pose tup,tc_pose tp,tr_pose_func tpf where tup.user_id = '");
		sql.append(userId);
		sql.append("' ");
		sql.append("and tup.pose_id=tp.pose_id and tp.pose_id = tpf.pose_id");
		list = factory.select(sql.toString(), null,
						new DAOCallback<TcFuncPO>() {
							public TcFuncPO wrapper(ResultSet rs, int idx)
							{
								TcFuncPO bean = new TcFuncPO();
								try
								{
									bean.setFuncId(rs.getLong("func_id"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * 个人任务快捷方式 createBy ChenLiang createDate 2009-09-26
	 * 
	 * @param taskType
	 *            任务类型
	 * @param userBean
	 *            登录用户
	 */
	public static List<TcOperateRemindPO> getTaskLink(String taskType,
					AclUserBean userBean) throws Exception
	{
		List<TcOperateRemindPO> list = null;
		StringBuffer sql = new StringBuffer();
		sql
						.append(" select t.create_date, tt.code_desc from tc_operate_remind t,tc_code tt ");
		sql.append(" where tt.code_id = t.operate_type and t.remind_type = '"
						+ taskType + "' and t.action_stat = '"
						+ Constant.ACCEPT_RES_UNACTION + "' and t.dlr_id = '"
						+ userBean.getCompanyId() + "' and t.submiter = '"
						+ userBean.getUserId() + "' and rownum < 6 ");
		
		list = factory.select(sql.toString(), null,
						new DAOCallback<TcOperateRemindPO>() {
							public TcOperateRemindPO wrapper(ResultSet rs, int idx)
							{
								TcOperateRemindPO bean = new TcOperateRemindPO();
								try
								{
									bean.setCreateDate(rs.getTimestamp("create_date"));
									bean.setOperateType(rs.getString("code_desc"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * 个人任务快捷方式 createBy ChenLiang createDate 2009-09-26
	 * 
	 * @param depId
	 *            用户当前部门ID
	 * @param companyId
	 *            用户当前公司ID
	 * @param retLst
	 *            存放下级部门
	 */
	public static List<TmOrgPO> getXjbm(Long orgId, Long companyId, List<TmOrgPO> retLst)
	{
		if (orgId == null || "".equals(orgId) || orgId == 0)
		{
			return null;
		}
		String sql = " select td.ORG_ID,td.PARENT_ORG_ID from tm_org td where "
						+ "  td.company_id = '"
						+ companyId
						+ "' and  td.PARENT_ORG_ID = '"
						+ orgId + "' ";
		List<TmOrgPO> lst = factory.select(sql, null,
						new DAOCallback<TmOrgPO>() {
							public TmOrgPO wrapper(ResultSet rs, int idx)
							{
								TmOrgPO bean = new TmOrgPO();
								try
								{
									bean.setOrgId(Long.valueOf(rs.getString("ORG_ID")));
									bean.setParentOrgId(Long.valueOf(rs.getString("PARENT_ORG_ID")));
								}
								catch (SQLException e)
								{
									e.printStackTrace();
								}
								return bean;
							}
						});
		
		Iterator<TmOrgPO> it = lst.iterator();
		while (it.hasNext())
		{
			TmOrgPO dept = it.next();
			retLst.add(dept);
			//		getXjbm(String.valueOf(dept.getOrgId()),companyId, retLst);
		}
		return retLst;
	}
	
	private static List<TmOrgPO> getXjbmOrg(String orgId, Long companyId, List<TmOrgPO> retLst)
	{
		if (orgId == null || "".equals(orgId))
		{
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select ORG_ID,PARENT_ORG_ID  from (SELECT ORG_ID, PARENT_ORG_ID ");
		sql.append(" FROM TM_ORG  WHERE ORG_TYPE <> ");
		sql.append(Constant.ORG_TYPE_OEM);
		sql.append(" AND COMPANY_ID = ");
		sql.append(companyId);
		sql.append(" UNION  SELECT ORG_ID, 1000000000 PARENT_ORG_ID   FROM TM_ORG ");
		sql.append(" WHERE ORG_TYPE = ");
		sql.append(Constant.ORG_TYPE_OEM);
		sql.append(" AND COMPANY_ID = ");
		sql.append(companyId);
		sql.append(" )  START WITH ORG_ID = ");
		sql.append(orgId);
		sql.append(" connect by prior ORG_ID = PARENT_ORG_ID ");
		List<TmOrgPO> lst = factory.select(sql.toString(), null,
						new DAOCallback<TmOrgPO>() {
							public TmOrgPO wrapper(ResultSet rs, int idx)
							{
								TmOrgPO bean = new TmOrgPO();
								try
								{
									bean.setOrgId(rs.getLong("ORG_ID"));
									bean.setParentOrgId(rs.getLong("PARENT_ORG_ID"));
								}
								catch (SQLException e)
								{
									e.printStackTrace();
								}
								return bean;
							}
						});
		
		Iterator<TmOrgPO> it = lst.iterator();
		while (it.hasNext())
		{
			TmOrgPO dept = it.next();
			retLst.add(dept);
		}
		return retLst;
	}
	
	/**
	 * 功能树 createBy ChenLiang createDate 2009-10-21
	 * 
	 * @param iid
	 *            上级部门ID
	 */
	public static List<TcFuncPO> getFunc(String iid)
	{
		String sql = " select func_id,par_func_id,func_code,func_name,sort_order from tc_func t ";
		if (iid != null && !"".equals(iid))
		{
			sql += " and par_func_id = '" + iid + "' order by par_func_id,sort_order asc";
		}
		else
		{
			sql += "order by par_func_id,sort_order asc";
		}
		List<TcFuncPO> lst = factory.select(sql, null,
						new DAOCallback<TcFuncPO>() {
							public TcFuncPO wrapper(ResultSet rs, int idx)
							{
								TcFuncPO bean = new TcFuncPO();
								try
								{
									bean.setFuncId(rs.getLong("func_id"));
									bean.setParFuncId(rs.getLong("par_func_id"));
									bean.setFuncCode(rs.getString("func_code"));
									bean.setFuncName(rs.getString("func_name"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		
		return lst;
	}
	
	/**
	 * 功能树 createBy ChenLiang createDate 2009-10-28
	 * 
	 * @param iid
	 *            上级部门ID
	 */
	public static List<TcFuncPO> getFunc(String iid, String funcType)
	{
		String sql = " select func_id,par_func_id,func_code,func_name from tc_func t where (t.func_type = '"
						+ funcType
						+ "' or t.func_type = '" + Constant.SYS_USER + "' )";
		
		sql += " and status = '" + Constant.STATUS_ENABLE + "'";
		if (iid != null && !"".equals(iid))
		{
			sql += " and par_func_id = '" + iid + "' order by par_func_id,func_id";
		}
		else
		{
			sql += "order by sort_order,par_func_id,func_id";
		}
		List<TcFuncPO> lst = factory.select(sql, null,
						new DAOCallback<TcFuncPO>() {
							public TcFuncPO wrapper(ResultSet rs, int idx)
							{
								TcFuncPO bean = new TcFuncPO();
								try
								{
									bean.setFuncId(rs.getLong("func_id"));
									bean.setParFuncId(rs.getLong("par_func_id"));
									bean.setFuncCode(rs.getString("func_code"));
									bean.setFuncName(rs.getString("func_name"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		
		return lst;
	}
	
	/**
	 * Function ：根据用户ID查询职位信息
	 * 
	 * @param ：用户ID
	 * @return ：满足条件的职位信息列表
	 * @throws ：DAOException LastUpdate ：2009-09-15
	 */
	public static List<TcPosePO> queryUserPosition(Long userId)
	{
		//modified by andy.ten@tom.com 2010-05-19 修改查询职位功能，增加POSE_TYPE、ORG_TYPE查询列，作为判断经销商/组织依据
		StringBuffer sql = new StringBuffer("");
		sql
						.append("SELECT D.ORG_ID,B.POSE_ID,B.POSE_NAME,B.POSE_TYPE,B.POSE_BUS_TYPE, D.ORG_NAME, D.ORG_TYPE FROM TC_USER A,TC_POSE B,TR_USER_POSE C,TM_ORG D");
		sql
						.append(" WHERE A.USER_STATUS=? AND B.POSE_STATUS=? AND D.STATUS=? AND A.USER_ID=? AND A.USER_ID=C.USER_ID");
		sql.append(" AND B.POSE_ID=C.POSE_ID AND B.ORG_ID=D.ORG_ID");
		List<Object> params = new ArrayList<Object>();
		params.add(Constant.STATUS_ENABLE);
		params.add(Constant.STATUS_ENABLE);
		params.add(Constant.STATUS_ENABLE);
		params.add(userId);
		List<TcPosePO> list = factory.select(sql.toString(), params,
						new DAOCallback<TcPosePO>() {
							public TcPosePO wrapper(ResultSet rs, int idx)
							{
								TcPosePO bean = new TcPosePO();
								try
								{
									bean.setOrgId(rs.getLong("ORG_ID"));
									bean.setPoseId(rs.getLong("POSE_ID"));
									bean.setPoseName(rs.getString("POSE_NAME"));
									bean.setPoseCode(rs.getString("ORG_NAME"));
									bean.setPoseType(rs.getInt("POSE_TYPE"));
									bean.setPoseBusType(rs.getInt("POSE_BUS_TYPE"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * 功能说明：厂家车系查询 根据车辆表的model_ID
	 * 
	 * @param model_id
	 * @return
	 * @throws Exception
	 *             最后修改时间：2009-9-18
	 */
	public static BrandAndSeriesBean getBrandAndSeriesBean(String model_id)
					throws Exception
	{
		List<Object> params = new ArrayList<Object>();
		String sql = "select t.brand_name as brand,t.series_name as series from vw_tm_model t where t.model_id="
						+ model_id;
		List<BrandAndSeriesBean> rs = factory.select(sql, params,
						new DAOCallback<BrandAndSeriesBean>() {
							public BrandAndSeriesBean wrapper(ResultSet rs, int idx)
							{
								BrandAndSeriesBean bean = new BrandAndSeriesBean();
								try
								{
									bean.setBrand(rs.getString("brand"));
									bean.setSeries(rs.getString("series"));
									return bean;
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
							}
						});
		return rs != null && rs.size() > 0 ? rs.get(0) : null;
	}
	
	/**
	 * 功能说明：根据经销商ID拿到部门ID
	 * 
	 * @param companyId
	 * @return
	 *         最后修改时间：2009-11-2
	 */
	public static Long getDeptIdByCompany(Long companyId)
	{
		TmOrgPO po = new TmOrgPO();
		po.setCompanyId(companyId);
		po = getPoByPk(po);
		return po.getOrgId();
	}
	
	/**
	 * Function ：自动生成文档编号
	 * 
	 * @param ：request-意向明细ID
	 * @return ：void
	 * @throws ：Exception
	 *         LastUpdate ：2009-09-26
	 */
	public static String getorderNumber(String dealerId)
	{
		SimpleDateFormat sp = new SimpleDateFormat("yyyyMM");
		
		String date = sp.format(new Date(System.currentTimeMillis()));
		String sql = "SELECT COUNT(*) TOTALNUM FROM TT_VHCL_EVA WHERE DLR_ID='" + dealerId + "' " +
						" AND TO_CHAR(APRS_DATE,'YYYYMM')='" + date + "' ";
		String result = "";
		List<String> list = factory.select(sql, null, new DAOCallback<String>() {
			public String wrapper(ResultSet rs, int idx)
			{
				try
				{
					return rs.getString("TOTALNUM");
				}
				catch (Exception e)
				{
					e.printStackTrace();
					throw new DAOException(e);
				}
			}
		});
		String num = list.get(0) == null ? "0" : list.get(0);
		num = String.valueOf(Integer.valueOf(num) + 1);
		if (num.length() == 1)
		{
			result = "000" + num;
		}
		else if (num.length() == 2)
		{
			result = "00" + num;
		}
		else if (num.length() == 3)
		{
			result = "0" + num;
		}
		else if (num.length() >= 4)
		{
			result = num;
		}
		return date + result;
	}
	
//	/**
//	* Function   ：查询文档编号是否已存在
//	* @param	 ：request-意向明细ID
//	* @return	 ：void
//	* @throws	 ：Exception
//	* LastUpdate ：2009-09-26
//	*/
//	public static boolean checkDocNumIsOnly(String docNumber,String dlrId){
//		TtVhclEvaPO evapo = new TtVhclEvaPO();
//		evapo.setDlrId(dlrId);
//		evapo.setFileNum(docNumber);
//		boolean count = false;
//		List<TtVhclEvaPO> eva = factory.select(evapo);
//		if(eva!=null&&eva.size()>0) {
//			count=true;
//		}
//		return count;
//	}
	/**
	 * @param proId
	 * @param proType
	 * @param tbName
	 *            table name
	 * @param tbAlias
	 *            table alias
	 * @return String sql for product query
	 *         K, root
	 *         B,TM_BRAND 品牌
	 *         S,TM_SERIES 车系
	 *         C,TM_CAR_NAME 车型名称
	 *         T,TM_CAR_SHORT_NAME 车型简称
	 *         TM_MODEL 车型
	 */
	public static String getProductConditionSql(String proId, String proType, String tbName, String tbAlias)
	{
		String sql = " and ";
		final String tb = tbAlias + ".";
		if ("B".equals(proType))
		{
			sql += tb + "brand_id=" + proId;
		}
		else if ("S".equals(proType))
		{
			sql += tb + "series_id=" + proId;
		}
		else if ("C".equals(proType))
		{
			sql += tb + "car_name_id=" + proId;
		}
		else if ("T".equals(proType))
		{
			sql += tb + "car_type_id=" + proId;
		}
		else
		{
			//default 查车型
			sql += tb + "model_id=" + proId;
		}
		return sql + " \n";
	}
	
	/* 需要优化，加入缓存机制，否则影响系统性能 */
	public static boolean authCheck(AclUserBean usr, String str)
	{
		boolean result = true;
		
		//	if(str.equals("/common/MenuShow/menuDisplay")||str.equals("/common/UserManager/logout")||str.equals("/common/UserManager/login")) return true;
		
		String sqlf = "select count(*) num from tc_func where func_code='" + str + "'";
		
		String sql =
						"select count(*) num from tr_role_pose a,tr_role_func b,tc_func c\n" +
										"where b.func_id=c.func_id and a.role_id =  b.role_id and c.status=" + Constant.STATUS_ENABLE + " and a.pose_id=" + usr
														.getPoseId() + " and ( c.func_code='" + str + "'   or c.func_code like '" + str + "%')";
		
		try
		{
			List<Integer> list = factory.select(sqlf.toString(), null,
							new DAOCallback<Integer>() {
								public Integer wrapper(ResultSet rs, int idx)
								{
									int num;
									try
									{
										num = rs.getInt("num");
									}
									catch (SQLException e)
									{
										throw new DAOException(e);
									}
									return num;
								}
							});
			if (list == null || list.size() < 1)
			{
				return true;
			}
			else
			{
				if (list.get(0).intValue() < 1)
				{
					return true;
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			result = false;
		}
		
		try
		{
			List<Integer> list = factory.select(sql.toString(), null,
							new DAOCallback<Integer>() {
								public Integer wrapper(ResultSet rs, int idx)
								{
									int num;
									try
									{
										num = rs.getInt("num");
									}
									catch (SQLException e)
									{
										throw new DAOException(e);
									}
									return num;
								}
							});
			if (list == null || list.size() < 1)
			{
				result = false;
			}
			else
			{
				if (list.get(0).intValue() < 1)
				{
					result = false;
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public static List<VehicleSeriesBean> queryVehicleSeriesByDealerId(String dealerId)
	{
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select group_id,group_name from tm_vhcl_material_group where status='10011001' and group_level='2' and tree_code like 'A01%'");
		List<VehicleSeriesBean> list = factory.select(sqlStr.toString(), null,
						new DAOCallback<VehicleSeriesBean>() {
							public VehicleSeriesBean wrapper(ResultSet rs, int idx)
							{
								VehicleSeriesBean bean = new VehicleSeriesBean();
								try
								{
									bean.setGroup_id(String.valueOf(rs.getLong("group_id")));
									bean.setVehicle_series(rs.getString("group_name"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * @Title: getInterUser
	 * @Description: TODO(根据经销商公司取接口用户)
	 * @param @param dcsCode 上端经销商公司code
	 * @return List<TcUserPO> 返回类型
	 * @throws
	 */
	public static List<TcUserPO> getInterUser(String dcsCode)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ACNT,A.USER_ID\n");
		sql.append("  FROM TC_USER A, TM_COMPANY B\n");
		sql.append(" WHERE B.COMPANY_CODE = '").append(dcsCode).append("'\n");
		sql.append("   AND A.COMPANY_ID = B.COMPANY_ID");
		List<TcUserPO> list = factory.select(sql.toString(), null,
						new DAOCallback<TcUserPO>() {
							public TcUserPO wrapper(ResultSet rs, int idx)
							{
								TcUserPO bean = new TcUserPO();
								try
								{
									bean.setAcnt(rs.getString("ACNT"));
									bean.setUserId(rs.getLong("USER_ID"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
		
	}
	
	public static String getPara(String constant)
	{
		String para = "0";
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("select tbp.para_value from tm_business_para tbp where tbp.para_id = ?");
		params.add(constant);
		
		List<TmBusinessParaPO> paraList = factory.select(sql.toString(), params,
						new DAOCallback<TmBusinessParaPO>() {
							public TmBusinessParaPO wrapper(ResultSet rs, int idx)
							{
								TmBusinessParaPO bean = new TmBusinessParaPO();
								try
								{
									bean.setParaValue(rs.getString("PARA_VALUE"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		
		if (!CommonUtils.isNullList(paraList))
		{
			para = paraList.get(0).getParaValue();
		}
		else
		{
			throw new RuntimeException("Constant中不存在对应参数！");
		}
		
		return para;
	}
	
	/**
	 * 通过用户ID得到功能列表
	 * 
	 * @param userId
	 *            用户ID
	 *            2012-04-26
	 *            hxy
	 */
	public static List<TcFuncPO> getFuncListByUserId(Long userId) throws Exception
	{
		List<TcFuncPO> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select DISTINCT tf.func_id, tf.func_code\n");
		sql.append("  from tr_user_pose tup, tr_role_func trf, tr_role_pose trp, tc_func tf\n");
		sql.append(" where tup.user_id = '");
		sql.append(userId);
		sql.append("'\n ");
		sql.append("and tup.pose_id = trp.pose_id and trp.role_id = trf.role_id and tf.func_id = trf.func_id\n");
		list = factory.select(sql.toString(), null,
						new DAOCallback<TcFuncPO>() {
							public TcFuncPO wrapper(ResultSet rs, int idx)
							{
								TcFuncPO bean = new TcFuncPO();
								try
								{
									bean.setFuncId(rs.getLong("func_id"));
									bean.setFuncCode(rs.getString("func_code"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * 通过职位ID得到操作列表
	 * 
	 * @param userId
	 *            用户ID
	 *            2013-05-14
	 *            andyzhou
	 */
	public static List<TcFuncActionPO> getActionList(Long poseId) throws Exception
	{
		List<TcFuncActionPO> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select B.ACTION_ID,B.FUNC_ID,B.ACTION_CODE,B.ACTION_NAME,B.PARA_CODE from TR_ROLE_ACTION A,TC_FUNC_ACTION B,TR_ROLE_POSE C   \n");
		sql.append("where A.ACTION_ID=B.ACTION_ID and A.ROLE_ID=C.ROLE_ID and C.POSE_ID=" + poseId + " \n");
		sql.append("order by SORT_ORDER  \n");
		list = factory.select(sql.toString(), null,
						new DAOCallback<TcFuncActionPO>() {
							public TcFuncActionPO wrapper(ResultSet rs, int idx)
							{
								TcFuncActionPO bean = new TcFuncActionPO();
								try
								{
									bean.setActionId(rs.getLong("ACTION_ID"));
									bean.setFuncId(rs.getLong("FUNC_ID"));
									bean.setActionCode(rs.getString("ACTION_CODE"));
									bean.setActionName(rs.getString("ACTION_NAME"));
									bean.setParaCode(rs.getString("PARA_CODE"));
								}
								catch (SQLException e)
								{
									throw new DAOException(e);
								}
								return bean;
							}
						});
		return list;
	}
	
	/**
	 * 对字符串解密
	 * 
	 * @param buff
	 * @return
	 * @throws Exception
	 */
	public static String decryptor(String str, String secretKey)
	{
		String str_ = null;
		str = str.replaceAll(" ", "+");
		// 实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
		try
		{
			KeyGenerator keygen = KeyGenerator.getInstance("DES");
			// 生成Cipher对象,指定其支持的DES算法
			Cipher c = Cipher.getInstance("DES");
			//生成密钥
			keygen.init(new SecureRandom(secretKey.getBytes()));
			Security.addProvider(new sun.security.provider.Sun());
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
			sr.setSeed(secretKey.getBytes());
			keygen.init(sr);
			// 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
			c.init(Cipher.DECRYPT_MODE, keygen.generateKey());
			//base64将不可见字符串转成可见字符串
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] buff = decoder.decodeBuffer(str);
			byte[] cipherByte = c.doFinal(buff);
			str_ = new String(cipherByte);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return str_;
	}
	
	/**
	 * 部门树 createBy ChenLiang createDate 2009-08-31
	 *
	 * @param companyId
	 *            公司ID
	 * @param parDeptId
	 *            上级部门ID
	 * @param userType
	 *            部门类型
	 * @param isStat
	 *            是否有效
	 */
	public static List<TmOrgPO> createDeptTree3(Long companyId,
			String parOrgId ,Integer userType, boolean isStat) throws Exception {
		List<TmOrgPO> list = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select td.ORG_ID,td.PARENT_ORG_ID,td.ORG_desc,td.ORG_name,td.ORG_code"
						+ " from tm_ORG td where TD.company_id = '"
						+ companyId + "' ");
		if(userType != null && !userType.equals("") && userType != 0 && userType.equals(Constant.SYS_USER_SGM))
		{
			//modified by andy.ten@tom.com
			sql.append(" and td.org_type = " + Constant.ORG_TYPE_OEM);
			//sql.append(" and td.ORG_TYPE  in('" + Constant.ORG_TYPE_OEM + "','" + Constant.ORG_TYPE_DEPT + "','" + Constant.ORG_TYPE_B_AREA + "','" + Constant.ORG_TYPE_S_AREA + "')");
			//end
		}else if(userType != null && !userType.equals("") && userType != 0 && userType.equals(Constant.SYS_USER_DEALER))
		{
			//modified by andy.ten@tom.com
			//sql.append(" and td.ORG_TYPE  in('" + Constant.ORG_TYPE_AGENT + "','" + Constant.ORG_TYPE_DISTRIBUTION + "')");
			sql.append(" and td.org_type = " + Constant.ORG_TYPE_DEALER);
			//end
		}
		if(isStat) {
			sql.append(" and td.status = '" + Constant.STATUS_ENABLE + "' ");
		}
		if (parOrgId == null || "".equals(parOrgId)||"null".equals(parOrgId)) {

		} else {
			sql.append(" START WITH TD.ORG_ID ="+parOrgId+"\n");
			sql.append("CONNECT BY PRIOR   TD.ORG_ID=TD.PARENT_ORG_ID \n");
		}
            sql.append("union\n" );
            sql.append("SELECT TD.ORG_ID, TD.PARENT_ORG_ID, TD.ORG_DESC, TD.ORG_NAME, TD.ORG_CODE\n" );
            sql.append("  FROM TM_ORG TD\n" );
            sql.append(" WHERE TD.COMPANY_ID = "+companyId+"\n");
            sql.append(" AND TD.PARENT_ORG_ID=-1\n");
System.out.println("sql========"+sql.toString());
		list = factory.select(sql.toString(), null,
				new DAOCallback<TmOrgPO>() {
					public TmOrgPO wrapper(ResultSet rs, int idx) {
						TmOrgPO bean = new TmOrgPO();
						try {
							bean.setOrgId(Long.valueOf(rs.getString("ORG_ID")));
							//modify by zhaojinyu 上级为空时设置为-100解决报错问题。
							if(rs.getString("PARENT_ORG_ID")!=null)
								bean.setParentOrgId(Long.valueOf(rs.getString("PARENT_ORG_ID")));
							else
								bean.setParentOrgId(new Long(-100));
							//end
							bean.setOrgDesc(rs.getString("ORG_DESC"));
							bean.setOrgCode(rs.getString("ORG_CODE"));
							bean.setOrgName(rs.getString("ORG_NAME"));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
		return list;
	}
	/**
	 * 部门树 createBy ChenLiang createDate 2009-08-31
	 *
	 * @param companyId
	 *            公司ID
	 * @param parDeptId
	 *            上级部门ID
	 * @param userType
	 *            部门类型
	 * @param isStat
	 *            是否有效
	 */
	public static List<TmOrgPO> createDeptTree4(Long companyId,
			String parOrgId ,Integer userType, boolean isStat) throws Exception {
		List<TmOrgPO> list = null;

        TmOrgPO toPO=new TmOrgPO();
        toPO.setOrgId(new Long(parOrgId));
        toPO=(TmOrgPO)factory.select(toPO).get(0);

        Long provinceId=toPO.getParentOrgId();

		StringBuffer sql = new StringBuffer();
		sql.append("select td.ORG_ID,td.PARENT_ORG_ID,td.ORG_desc,td.ORG_name,td.ORG_code"
						+ " from tm_ORG td where TD.company_id = '"
						+ companyId + "' ");
		if(userType != null && !userType.equals("") && userType != 0 && userType.equals(Constant.SYS_USER_SGM))
		{
			//modified by andy.ten@tom.com
			sql.append(" and td.org_type = " + Constant.ORG_TYPE_OEM);
			//sql.append(" and td.ORG_TYPE  in('" + Constant.ORG_TYPE_OEM + "','" + Constant.ORG_TYPE_DEPT + "','" + Constant.ORG_TYPE_B_AREA + "','" + Constant.ORG_TYPE_S_AREA + "')");
			//end
		}else if(userType != null && !userType.equals("") && userType != 0 && userType.equals(Constant.SYS_USER_DEALER))
		{
			//modified by andy.ten@tom.com
			//sql.append(" and td.ORG_TYPE  in('" + Constant.ORG_TYPE_AGENT + "','" + Constant.ORG_TYPE_DISTRIBUTION + "')");
			sql.append(" and td.org_type = " + Constant.ORG_TYPE_DEALER);
			//end
		}
		if(isStat) {
			sql.append(" and td.status = '" + Constant.STATUS_ENABLE + "' ");
		}
		if (parOrgId == null || "".equals(parOrgId)||"null".equals(parOrgId)) {

		} else {
			sql.append(" START WITH TD.ORG_ID ="+parOrgId+"\n");
			sql.append("CONNECT BY PRIOR   TD.ORG_ID=TD.PARENT_ORG_ID \n");
		}
            sql.append("union\n" );
            sql.append("SELECT TD.ORG_ID, TD.PARENT_ORG_ID, TD.ORG_DESC, TD.ORG_NAME, TD.ORG_CODE\n" );
            sql.append("  FROM TM_ORG TD\n" );
            sql.append(" WHERE TD.COMPANY_ID = "+companyId+"\n");
            sql.append(" AND TD.PARENT_ORG_ID=-1\n");
            sql.append("union\n" );
            sql.append("SELECT TD.ORG_ID, TD.PARENT_ORG_ID, TD.ORG_DESC, TD.ORG_NAME, TD.ORG_CODE\n" );
            sql.append("  FROM TM_ORG TD\n" );
            sql.append(" WHERE TD.COMPANY_ID = "+companyId+"\n");
            sql.append(" AND TD.ORG_ID="+provinceId+"\n");
System.out.println("sql========"+sql.toString());
		list = factory.select(sql.toString(), null,
				new DAOCallback<TmOrgPO>() {
					public TmOrgPO wrapper(ResultSet rs, int idx) {
						TmOrgPO bean = new TmOrgPO();
						try {
							bean.setOrgId(Long.valueOf(rs.getString("ORG_ID")));
							//modify by zhaojinyu 上级为空时设置为-100解决报错问题。
							if(rs.getString("PARENT_ORG_ID")!=null)
								bean.setParentOrgId(Long.valueOf(rs.getString("PARENT_ORG_ID")));
							else
								bean.setParentOrgId(new Long(-100));
							//end
							bean.setOrgDesc(rs.getString("ORG_DESC"));
							bean.setOrgCode(rs.getString("ORG_CODE"));
							bean.setOrgName(rs.getString("ORG_NAME"));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
		return list;
	}
}
