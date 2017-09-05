/**********************************************************************
 * <pre>
 * FILE : MenuDAO.java
 * CLASS : MenuDAO
 * 
 * AUTHOR : ChenLiang
 *
 * FUNCTION : 菜单DAO.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-09-24| ChenLiang  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.dao.menu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.util.BeanUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;

public class MenuDAO {
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static Logger logger = Logger.getLogger(MenuDAO.class);

	public static List<TcFuncPO> getShortCutMenu(Long poseId, Long userId) {
		List<TcFuncPO> list = null;

		String sql = "select a.func_id, b.func_name, b.func_code\n"
				+ "  from TM_QUICK_FUNC a, tc_func b\n"
				+ " where a.func_id = b.func_id\n" + "   and a.user_id = "
				+ userId + "   and a.pose_id = " + poseId
				+ " order by a.func_order";
		
		list = factory.select(sql.toString(), null,
				new DAOCallback<TcFuncPO>() {
					public TcFuncPO wrapper(ResultSet rs, int idx) {
						TcFuncPO bean = new TcFuncPO();
						try {
							bean.setFuncId(rs.getLong("func_id"));
					//		bean.setParFuncId(rs.getLong("par_func_id"));
							bean.setFuncName(rs.getString("func_name"));
							bean.setFuncCode(rs.getString("func_code"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
		return list;
	}

	/**
	 * 通过用户职位ID得到功能列表
	 * 
	 * @param userId
	 *            用户ID
	 */
	public static List<TcFuncPO> getFuncTreeByPoseId(Long poseId) {
		List<TcFuncPO> list = null;
		/**
		 * modified by andy.ten@tom.com
		 * 去掉tr_pose_func关联
		 */
		String sql = 
			"select distinct  * from tc_func c\n" +
			"where c.status = 10011001\n" +
			"connect by prior par_func_id=func_id\n" + 
			"start with func_id in(select b.func_id\n" + 
			//"from tr_role_func b,tr_role_pose trp where trp.pose_id="+poseId+" and b.role_id = trp.role_id) order by c.create_date,c.func_id asc";
			//modify by zjy 2010-07-05 修改菜单排序问题
		  "from tr_role_func b,tr_role_pose trp where trp.pose_id="+poseId+" and b.role_id = trp.role_id) order by c.sort_order asc";
//		String sql = 
//			"select distinct  c.func_id,c.par_func_id,c.func_code,c.func_name,c.func_type,c.create_date from tc_func c,tr_role_pose trp, tr_role_func trf \n" +
//			"where trp.pose_id = " + poseId + " and trp.role_id = trf.role_id and c.func_id = trf.func_id " +
//			"order by c.create_date,c.func_id asc";


		list = factory.select(sql.toString(), null,
				new DAOCallback<TcFuncPO>() {
					public TcFuncPO wrapper(ResultSet rs, int idx) {
						TcFuncPO bean = new TcFuncPO();
						try {
							bean.setFuncId(rs.getLong("func_id"));
							bean.setParFuncId(rs.getLong("par_func_id"));
							bean.setFuncName(rs.getString("func_name"));
							bean.setFuncCode(rs.getString("func_code"));
							bean.setIcon(rs.getString("icon"));
							if( null != rs.getObject( "func_type" ) ){
								bean.setFuncType( rs.getInt( "func_type" ) );
							}
							if( null != rs.getObject( "is_func" ) ){
								bean.setIsFunc(rs.getInt("is_func"));
							}
							
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
		return list;
	}
	/**
	 * 通过用户职位ID得到功能列表
	 * 
	 * @param userId
	 *            用户ID
	 */
	public static List<TcFuncPO> getFuncByPoseId(Long poseId) {
		List<TcFuncPO> list = null;
		/**
		 * modified by andy.ten@tom.com
		 * 去掉关联tr_pose_func
		 */
//		String sql = "select tf.func_id,tf.par_func_id,tf.func_code,tf.func_name from tr_pose_func tpf,tc_func tf where tpf.pose_id = '"
//				+ poseId
//				+ "' and tpf.func_id = tf.func_id order by tf.par_func_id,tf.func_id";
		String sql = "select distinct tf.func_id,tf.par_func_id,tf.func_code,tf.func_name,tf.sort_order from TR_ROLE_POSE TRP,TC_FUNC TF,TR_ROLE_FUNC TRF where trp.pose_id = '"
			+ poseId
			+ "' and TRP.ROLE_ID = TRF.ROLE_ID AND TF.FUNC_ID = TRF.FUNC_ID order by tf.par_func_id,tf.sort_order";
		list = factory.select(sql.toString(), null,
				new DAOCallback<TcFuncPO>() {
					public TcFuncPO wrapper(ResultSet rs, int idx) {
						TcFuncPO bean = new TcFuncPO();
						try {
							bean.setFuncId(rs.getLong("func_id"));
							bean.setParFuncId(rs.getLong("par_func_id"));
							bean.setFuncName(rs.getString("func_name"));
							bean.setFuncCode(rs.getString("func_code"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
		return list;
	}

	/**
	 * 得到系统功能ID
	 */
	public static List<TcFuncPO> getFuncs() {
		List<TcFuncPO> list = null;
		String sql = "select func_id,par_func_id,func_name from tc_func";
		list = factory.select(sql.toString(), null,
				new DAOCallback<TcFuncPO>() {
					public TcFuncPO wrapper(ResultSet rs, int idx) {
						TcFuncPO bean = new TcFuncPO();
						try {
							bean.setFuncId(rs.getLong("func_id"));
							bean.setParFuncId(rs.getLong("par_func_id"));
							bean.setFuncName(rs.getString("func_name"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
		return list;
	}
	/**
	 * 扩展查询菜单
	 * @param tcFuncPO 查询参数
	 * @param noticeFlag 是否是提醒的菜单
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public static List<TcFuncPO> getFuncsExtend(TcFuncPO tcFuncPO,boolean noticeFlag) {
		List<TcFuncPO> list = null;
		StringBuffer buff = new StringBuffer();
		buff.append(" select t.func_id,t.par_func_id,t.func_name,t.is_func,t.func_tablename ");
		buff.append(" from tc_func t ");
		buff.append(" where 1=1 ");
		if (null != tcFuncPO) {
			if(StringUtils.isNotEmpty(tcFuncPO.getFuncName())){
				buff.append(" and t.func_name like '%"+tcFuncPO.getFuncName()+"%'");
			}
			if(null!=tcFuncPO.getFuncId()){
				buff.append(" and t.func_id like '%"+tcFuncPO.getFuncId()+"%'");
			}
			if(null!=tcFuncPO.getIsFunc()){
				buff.append(" and t.is_func = '"+tcFuncPO.getIsFunc()+"'");
			}
			if(noticeFlag){
				buff.append(" and t.func_tablename is not null ");
			}
		}
		
		list = factory.select(buff.toString(), null,
				new DAOCallback<TcFuncPO>() {
					public TcFuncPO wrapper(ResultSet rs, int idx) {
						TcFuncPO bean = new TcFuncPO();
						try {
							bean.setFuncId(rs.getLong("func_id"));
							bean.setParFuncId(rs.getLong("par_func_id"));
							bean.setFuncName(rs.getString("func_name"));
							bean.setIsFunc(rs.getInt("is_func"));
							bean.setFuncTablename(rs.getString("func_tablename"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
		return list;
	}
	
	public static List<Map<String, Object>> getTableInfo(String tableName){
		if (StringUtils.isEmpty(tableName)) {
			return null;
		}
		List<Map<String, Object>> list = null;
		StringBuffer buff = new StringBuffer();
		buff.append(" select * ");
		buff.append(" from user_col_comments ");
		buff.append(" where Table_Name='" + tableName.toUpperCase() + "' ");
		buff.append(" order by column_name ");
		list = factory.select(buff.toString(), null, new DAOCallback<Map<String, Object>>() {
			public Map<String, Object> wrapper(ResultSet rs, int idx) {
				Map<String, Object> map = null;
				try {
					map = BeanUtils.rs2Map(rs);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return map;
			}
		});
		return list;
	}
	

	/**
	 * 获取菜单集合
	 * @param isFunc 是否功能菜单 =
	 * @param funcId  菜单id like
	 * @param funcName  菜单名称  like
	 * @param tableFlag tablename字段是否为空
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public static List<Map<String, Object>> getFuncList(String isFunc,
			String funcId, String funcName, boolean tableFlag) {
		TcFuncPO tcPO = new TcFuncPO();
		tcPO.setFuncId(NumberUtils.toLong(funcId));
		tcPO.setFuncName(funcName);
		tcPO.setIsFunc(NumberUtils.toInt(isFunc));
		List<TcFuncPO> list = getFuncsExtend(tcPO,tableFlag);
		List<Map<String, Object>> pageList = new LinkedList<Map<String,Object>>();
		if(!CommonUtils.isNullList(list)){
			for (TcFuncPO tcFuncPO : list) {
				pageList.add(BeanUtils.bean2Map(tcFuncPO));
			}
		}
		return pageList;
	}
}
