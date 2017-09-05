/**********************************************************************
* <pre>
* FILE : OnlineUserDAO.java
* CLASS : OnlineUserDAO
*
* AUTHOR : LAX
*
* FUNCTION : 在线用户查询.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-02| LAX  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: OnlineUserDAO.java,v 1.1 2010/08/16 01:44:52 yuch Exp $
 */

package com.infodms.dms.dao.onlineusermng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.infodms.dms.bean.OnlineUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

/**
 * Function       :  在线用户查询
 * @author        :  LAX
 * CreateDate     :  2009-09-02
 * @version       :  0.1
 */
public class OnlineUserDAO {
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * Function         : 查询在线用户信息
	 * @param           : 经销商ID
	 * @param           : 组织ID
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @param           : 排序的字段名
	 * @param           : 升序ASC\降序DESC
	 * @return          : 满足条件的在线用户信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 2009-09-02
	 */
	public static PageResult<OnlineUserBean> getAllOnlineUser(String dlrId, String orgId,String check, int pageSize, int curPage, String orderName,String da) throws Exception {
		StringBuffer sql = new StringBuffer();
		PageResult<OnlineUserBean> list = null;
		try {
			sql.append("select \n");
			sql.append("	a.user_online_id,a.user_id,b.emp_num, \n");
			sql.append("	b.name,e.dept_name,f.company_short_name,to_char(a.login_date,'yyyy-MM-dd HH24:mi:ss')as login_date,a.user_ip \n");
			sql.append("  from tc_user_online a ,tc_user b,tm_dept e,tm_company f  \n");
			sql.append("  	   where a.user_id=b.user_id and a.user_online_stat='"+ Constant.IF_TYPE_YES +"' \n");
			sql.append("  	   		AND a.dept_id = e.dept_id and b.company_id=f.company_id \n");
			
			if(!"".equals(check) && check != null){
				if("1".equals(check)){
					sql.append("  	   	and f.company_type ='"+ Constant.COMPANY_TYPE_SGM+"' \n");
					if(!"".equals(orgId) && orgId != null){
						sql.append("  	   	and f.dept_id in( \n");
						sql.append("			select d.dept_id ");
						sql.append("				from tm_dept d ");
						sql.append("					start with d.dept_id='"+ orgId +"' ");
						sql.append("					connect by prior d.dept_id=d.par_dept_id ) ");
					}
				}else{
					sql.append("  	   	and f.company_type ='"+ Constant.COMPANY_TYPE_DEALER+"' \n");
					if(!"".equals(dlrId) && dlrId != null){
						sql.append("  	   	AND f.company_id = '"+ dlrId +"' \n");
					}
				}
			}
//System.out.println("sql================="+sql.toString());
			String sb = OrderUtil.addOrderBy(sql.toString(), orderName, da);

			List<Object> params = new ArrayList<Object>();
			list =  factory.pageQuery(sb, params, new DAOCallback<OnlineUserBean>() {
				public OnlineUserBean wrapper(ResultSet rs, int idx){
					OnlineUserBean userbean = new OnlineUserBean();
					try {
						userbean.setUserOnlineId(rs.getString("user_online_id"));
						userbean.setUserId(rs.getString("user_id"));
						userbean.setEmpNum(rs.getString("emp_num"));
						userbean.setUserName(rs.getString("name"));
						userbean.setOrgName(rs.getString("dept_name"));
						userbean.setCompanyName(rs.getString("company_short_name"));
						userbean.setLoginDate(rs.getString("login_date"));
						userbean.setUserIP(rs.getString("user_ip"));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return userbean;
				}
			}, pageSize, curPage);
			
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	
	/**
	 * Function         : 查询在线用户情况
	 * @param           : 年
	 * @param           : 月
	 * @param           : 日
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的在线用户情况信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 2009-09-03
	 */
	public static PageResult<OnlineUserBean> getAllOnlineUserCircs(String year, String month,String day, int pageSize, int curPage, String orderName,String da) throws Exception {
		StringBuffer sql = new StringBuffer();
		PageResult<OnlineUserBean> list = null;
		try {
			sql.append("with a as ( \n");
			sql.append("	select distinct u.user_id, \n");
			//年份不为空、月份与日期为空时查询当年的每周在线人数；年份与月份不为空、日期为空时查询当月在线人数；
			//年份、月份、日期不为空时查询当日在线人数。
			if("".equals(year) || (!"".equals(year) && "".equals(month) && "".equals(day))){
				sql.append("		to_char(to_date(to_char(u.login_date, 'yyyy-mm-dd'), 'yyyy-mm-dd'),'iw') as week, \n");
				sql.append("  		to_char(u.login_date, 'yyyy') as year  \n");
			}if(!"".equals(year) && !"".equals(month) && "".equals(day)){
				sql.append("		to_char(to_date(to_char(u.login_date, 'yyyy-mm-dd'), 'yyyy-mm-dd'),'iw') as week, \n");
				sql.append("  		to_char(u.login_date, 'yyyy-mm-dd') as year  \n");
			}if(!"".equals(year) && !"".equals(month) && !"".equals(day)){
				sql.append("		to_char(to_date(to_char(u.login_date, 'yyyy-mm-dd'), 'yyyy-mm-dd'),'iw') as week, \n");
				sql.append("  		to_char(u.login_date, 'hh24') as year  \n");
			}
			
			sql.append("  			,c.company_type \n");
			sql.append("  	from tc_user_online u, tc_user b, tm_company c \n");
			//modified by andy.ten@tom.com 去掉分销商
			sql.append("  	where u.user_id = b.user_id and b.company_id = c.company_id '\n");
			//sql.append("  	where u.user_id = b.user_id and b.company_id = c.company_id and c.company_type != '"+ Constant.COMPANY_TYPE_DEALER_DMC +"'\n");
			if(!"".equals(year) && "".equals(month) && "".equals(day)){
				sql.append("	and to_char(u.login_date, 'yyyy')='"+ year +"' \n");
			}if(!"".equals(year) && !"".equals(month) && "".equals(day)){
				sql.append("	and to_char(u.login_date, 'yyyy-mm')='"+ year + "-" + month + "' \n");
			}if(!"".equals(year) && !"".equals(month) && !"".equals(day)){
				sql.append("	and to_char(u.login_date, 'yyyy-mm-dd')='"+ year + "-" + month + "-" + day + "' \n");
			}
			sql.append("	) \n");
			sql.append("select \n");
			sql.append("  	a.year,a.week,count(*) sumall, \n");
			sql.append("  	sum(case when a.company_type = '"+ Constant.COMPANY_TYPE_SGM +"' then 1 else 0 end) sumsgm, \n");
			sql.append("  	sum(case when a.company_type = '"+ Constant.COMPANY_TYPE_DEALER +"' then 1 else 0 end) sumdlr \n");
			sql.append("from a group by a.year,a.week \n");
			
//			System.out.println("sql======11111111============"+sql.toString());
			String sb = OrderUtil.addOrderBy(sql.toString(), orderName, da);
			
			List<Object> params = new ArrayList<Object>();
			list =  factory.pageQuery(sb, params, new DAOCallback<OnlineUserBean>() {
				public OnlineUserBean wrapper(ResultSet rs, int idx){
					OnlineUserBean userbean = new OnlineUserBean();
					try {
						if(rs.getString("year").length()==2){
							userbean.setYear(rs.getString("year")+":00");
						}else{
							userbean.setYear(rs.getString("year"));
						}
						
						userbean.setWeek(rs.getString("year")+"第"+rs.getString("week")+"周");
						userbean.setSumCount(rs.getString("sumall"));
						userbean.setSgmCount(rs.getString("sumsgm"));
						userbean.setDlrCount(rs.getString("sumdlr"));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return userbean;
				}
			}, pageSize, curPage);
			
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

}
