package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ActMater;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.DyncBeanCallBack;
import com.infoservice.po3.callbackimpl.POCallBack;
import com.infoservice.po3.core.callback.DAOCallback;

public abstract class BaseDao<T extends PO> {
	protected POFactory factory = POFactoryBuilder.getInstance();
	protected abstract T wrapperPO(ResultSet rs, int idx);
	protected Object wrapperObject(ResultSet rs, int idx) {
		return null;
	}

	public Integer getIntegerPK(T t) {
		return factory.getIntegerPK(t);
	}

	public Long getLongPK(T t) {
		return factory.getLongPK(t);
	}

	public String getStringPK(T t) {
		return factory.getStringPK(t);
	}

	public List<T> select(T t) {
		return factory.select(t);
	}

	public List<Map<String, Object>> selectDealers(String dealerId) {
		String sql = "select * from tm_dealer tmd where tmd.dealer_id in("
				+ dealerId + ")";
		List list = (List) pageQuery(sql, null, getFunName());
		return list;
	}

	public List<Map<String, Object>> selectAllDealers(String dealerId) {
		String sql = "SELECT * FROM VW_ORG_DEALER VOD WHERE VOD.ROOT_DEALER_ID IN ("
				+ dealerId + ")";
		List list = (List) pageQuery(sql, null, getFunName());
		return list;
	}

	public List<ActMater> selectAllMaters(String areaId) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT f_get_material_pid(TAG.MATERIAL_GROUP_ID) MATERIAL_GROUP_ID FROM TM_AREA_GROUP TAG WHERE 1=1");
		// List list=(List) pageQuery(sql, null, getFunName());
		// POFactory factory = POFactoryBuilder.getInstance();
		if (areaId != null && areaId != "") {
			sql.append("   AND TAG.AREA_ID='" + areaId + "'");
		}
		return factory.select(sql.toString(), null,
				new DAOCallback<ActMater>() {
					public ActMater wrapper(ResultSet rs, int idx) {
						ActMater bean = new ActMater();
						try {
							bean.setMaterId(rs.getString("MATERIAL_GROUP_ID"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
	}

	public List<Map<String, Object>> selectMyDealers(String dealerId) {
		String sql = "select * from VW_ORG_DEALER VOD where VOD.root_dealer_id in("
				+ dealerId + ")";
		List list = (List) pageQuery(sql, null, getFunName());
		return list;
	}

	public List<T> select(T t, int i) {
		return factory.select(t, new DAOCallback<T>() {
			public T wrapper(ResultSet rs, int idx) {
				return wrapperPO(rs, idx);
			}
		});
	}

	public List<T> select(Class<T> t, String sql, List<Object> param) {
		return factory.select(sql, param, new POCallBack<T>(factory, t));
	}

	public void insert(T t) {
		factory.insert(t);
	}

	public void insert(List<T> t) {
		factory.insert(t);
	}

	/**
	 * 鑷繁閲嶅啓wrapperPO鏂规硶锛屾寜闇�瑕佺粍寤篜O
	 * 
	 * @param sql
	 * @param params
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<T> pageQuery(String sql, List<Object> params,
			int pageSize, int curPage) {
		// logger.info(sql);
		PageResult<T> tcs = factory.pageQuery(sql, params,
				new DAOCallback<T>() {
					public T wrapper(ResultSet rs, int idx) {
						return wrapperPO(rs, idx);
					}
				}, pageSize, curPage);
		return tcs;
	}

	/**
	 * 鍙互涓嶇敤閲嶅啓wrapperPO鏂规硶锛屾鏋惰嚜鍔ㄦ槧灏勬墍鏈夊睘鎬�
	 * 
	 * @param t
	 * @param sql
	 * @param params
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<T> pageQuery(Class<T> t, String sql, List<Object> params,
			int pageSize, int curPage) {
		// logger.info(sql);
		PageResult<T> tcs = factory.pageQuery(sql, params, new POCallBack<T>(
				factory, t), pageSize, curPage);
		return tcs;
	}

	/**
	 * 閫傜敤浜庡琛ㄦ煡璇紝鎸夐渶瑕佺粍寤哄涓狿O
	 * 
	 * @param sql
	 * @param params
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Object> pageQueryObject(String sql, List<Object> params,
			int pageSize, int curPage) {
		// logger.info(sql);
		PageResult<Object> tcs = factory.pageQuery(sql, params,
				new DAOCallback<Object>() {
					public Object wrapper(ResultSet rs, int idx) {
						return wrapperObject(rs, idx);
					}
				}, pageSize, curPage);
		return tcs;
	}

	/**
	 * 
	 * @param sql
	 * @param params
	 * @param funName
	 *            瀹屾暣鏂规硶鍚� eq com.jmc.dms.dao.checkInWarehouseManage.
	 *            CheckInWarehouseManageDao.getDealerList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> pageQuery(String sql, List<Object> params,
			final String funName) {
		// logger.info(sql);
		List<DynaBean> tmp = factory.select(sql, params,
				new JCDynaBeanCallBack());
		LinkedList<Map<String, Object>> ret = new LinkedList<Map<String, Object>>();
		for (DynaBean bean : tmp) {
			ret.addLast((Map<String, Object>) bean);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> pageQuery01(String sql,
			List<Object> params, final String funName) {
		// logger.info(sql);
		List<DynaBean> tmp = factory.select(sql, params,
				new JCDynaBeanCallBack());
		LinkedList<Map<String, String>> ret = new LinkedList<Map<String, String>>();
		for (DynaBean bean : tmp) {
			ret.addLast((Map<String, String>) bean);
		}
		return ret;
	}

	public Map<String, Object> pageQueryMap(String sql, List<Object> params,
			final String funName) {
		// logger.info(sql);
		List<Map<String, Object>> list = pageQuery(sql, params, funName);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> pageQuery(String sql,
			List<Object> params, final String funName, int pageSize, int curPage) {
		// logger.info(sql);
		PageResult<DynaBean> tmps = factory.pageQuery(sql, params,
				new JCDynaBeanCallBack(), pageSize, curPage);

		PageResult<Map<String, Object>> ret = new PageResult<Map<String, Object>>();

		ret.setCurPage(tmps.getCurPage());
		ret.setPageSize(tmps.getPageSize());
		ret.setTotalPages(tmps.getTotalPages());
		ret.setTotalRecords(tmps.getTotalRecords());

		List<DynaBean> tmp = tmps.getRecords();
		LinkedList<Map<String, Object>> t1 = new LinkedList<Map<String, Object>>();
		if (tmp != null) {
			for (DynaBean bean : tmp) {
				t1.addLast((Map<String, Object>) bean);
			}
			ret.setRecords(t1);
		}
		return ret;
	}

	public int update(T t1, T t2) {
		return factory.update(t1, t2);
	}

	public int update(String sql, List<?> params) {
		// logger.info(sql);
		return factory.update(sql, params);
	}

	public int delete(T t) {
		return factory.delete(t);
	}

	public int delete(String sql, List<?> params) {
		// logger.info(sql);
		return factory.delete(sql, params);
	}

	public Object callFunction(String arg0, int arg1, List<Object> arg2) {
		return factory.callFunction(arg0, arg1, arg2);
	}

	public List<Object> callProcedure(String arg0, List<Object> arg1,
			List<Integer> arg2) {
		return factory.callProcedure(arg0, arg1, arg2);
	}

	public String getFunName() {
		StackTraceElement stack[] = new Throwable().getStackTrace();
		StackTraceElement ste = stack[1];
		// System.out.println(ste.getClassName() + "." + ste.getMethodName() +
		// ste.getLineNumber());
		StringBuilder strBuilder = new StringBuilder();
		return strBuilder.append(ste.getClassName()).append(".")
				.append(ste.getMethodName()).append(ste.getLineNumber())
				.toString();
	}

	public List<Object> select(String sql, List<Object> params, Class clz) {
		return factory.select(sql, params, new DAOCallback<Object>() {
			public Object wrapper(ResultSet rs, int idx) {
				return wrapperObject(rs, idx);
			}
		});
	}

	public void insert(String sql) {
		factory.insert(sql, null);
	}
	
	public void insert(String sql, List<?> params) {
		factory.insert(sql, params);
	}

	public void selectPg(String areId) {
		String sql = "SELECT F_GET_MATERIAL_PID(TAG.MATERIAL_GROUP_ID) FROM TM_AREA_GROUP TAG  WHERE TAG.AREA_ID='"
				+ areId + "'";
		factory.insert(sql, null);
	}

	/**
	 * 添加公共查询TM_DATA_SET年、月、周度的SQL
	 * 
	 * @author HXY
	 * @update 2013-1-30
	 * */
	public List<String> selectTmDataSet(StringBuffer sql, final String paramName) {
		return factory.select(sql.toString(), null, new DAOCallback<String>() {
			public String wrapper(ResultSet rs, int idx) {
				try {
					return rs.getString(paramName);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	/**
	 * 通过职位ID获取角色ID
	 * 
	 * @param poseId
	 *            职位ID
	 * @return
	 */
	public String getPoseRoleId(String poseId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tr_role_pose p WHERE p.pose_id=" + poseId
				+ "");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null,
				this.getFunName());
		if (null == list || list.size() <= 0 || list.get(0) == null
				|| list.get(0).get("ROLE_ID") == null) {
			return "0";
		}
		return CommonUtils.checkNull(list.get(0).get("ROLE_ID"));
	}

	/***
	 * 获取当前用户所有的经销商ID
	 * 经销商id用逗号分开
	 * */
	public String getDealerIdBySession(AclUserBean loginUser) {
		String dutType = loginUser.getDutyType();// 组织类型
		Integer postType = loginUser.getPoseType();// 职位类型
		if (null == dutType || dutType == "" || null == postType) {
			return "";
		}
		Integer dutInt = Integer.parseInt(dutType);
		String dealerId = loginUser.getDealerId();
		Long orgId = loginUser.getOrgId();
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT to_char(v.DEALER_ID) DEALER_ID ");
		sb.append(" FROM VW_ORG_DEALER_ALL_NEW V  ");
		sb.append(" WHERE V.STATUS=10011001 ");
		// 表示职位属于经销商的
		if (dutInt == 10431001 || dutInt == 10431002) {
			sb.append(" and v.COMPANY_ID=" + orgId + " ");
		} else if (dutInt == 10431003) {
			sb.append(" and v.ROOT_ORG_ID=" + orgId + " ");
		} else if (dutInt == 10431004) {
			sb.append(" and v.PQ_ORG_ID=" + orgId + " ");
		} else {
			sb.append(" and v.DEALER_ID=" + dealerId + " ");
		}
		List<Map<String, Object>> dealerList = pageQuery(sb.toString(), null, getFunName());
		String tmpStr = "";
		for (int i = 0; i < dealerList.size(); i++) {
			Map<String, Object> dMap = dealerList.get(i);
			String dst = dMap.get("DEALER_ID") == null ? "" : dMap.get(
					"DEALER_ID").toString();
			tmpStr = tmpStr + dst + ",";
		}
		if (tmpStr == "") {
			return "";
		}
		String tp = tmpStr.substring(0, tmpStr.length() - 1);
		return tp;
	}

	/***
	 * 获取当前用户所有的经销商ID 纯sql语句
	 * sql 后面拼接and exists(sql)；
	 * */
	public String getDealerIdByPostSql(AclUserBean loginUser) {
		String dutType = loginUser.getDutyType(loginUser.getDealerId());// 组织类型
		Integer postType = loginUser.getPoseType();// 职位类型
		if (null == dutType || dutType == "" || null == postType) {
			return "";
		}
		Integer dutInt = Integer.parseInt(dutType);
		String dealerId = loginUser.getDealerId();
		Long orgId = loginUser.getOrgId();
		Long companyId = loginUser.getCompanyId();
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT 1 ");
		sb.append(" FROM VW_ORG_DEALER_ALL_NEW V  ");
		sb.append(" WHERE V.STATUS=10011001 ");
		if (dutInt == 10431001 || dutInt == 10431002) {
			sb.append(" and v.COMPANY_ID=" + companyId + " ");
		} else if (dutInt == 10431003) {
			sb.append(" and v.ROOT_ORG_ID=" + orgId + " ");
		} else if (dutInt == 10431004) {
			sb.append(" and v.PQ_ORG_ID=" + orgId + " ");
		} else {
			sb.append(" and v.DEALER_ID=" + dealerId + " ");
		}
		return sb.toString();
	}
	


}
