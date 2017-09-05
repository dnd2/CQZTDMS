/**********************************************************************
* <pre>
* FILE : CarTypeDao.java
* CLASS : CarTypeDao
*
* AUTHOR : wry
*
* FUNCTION : 销售意向
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-19| wry  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.dao.baseData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.VhclModelInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.claim.application.ClaimManualAuditingDao;
import com.infodms.dms.util.ActionUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;
/**
 * function:基础数据导入DAO 
 * author: wry
 * CreateDate: 2009-08-19
 * @version:0.1
 */
public class CarTypeDao {
	public Logger logger = Logger.getLogger(CarTypeDao.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	private static final CarTypeDao dao = new CarTypeDao();
	public static final CarTypeDao getInstance(){
		if (dao == null) {
			return new CarTypeDao();
		}
		return dao;
	}
	
	/**
	* 查询车型配置
	 * author: wry
	 * @throws Exception
	 * date: 2009-08-20
	 */
	public static PageResult<VhclModelInfoBean> getCarInfo(String modelCode,String startDate,String endDate,String operator,String actionName, 
			AclUserBean user,String orderName, String da,int curPage) throws Exception{
		PageResult<VhclModelInfoBean> rs = null;
		try{
			StringBuffer sb = new StringBuffer();
			System.out.println("-----startDate----"+startDate);
			System.out.println("-----endDate-----"+endDate);
			System.out.println("-----operator----"+operator);
			sb.append("select a.MODEL_PKG_ID,a.STANDARD_PKG,a.MODEL_ID,b.MODEL_CODE from TM_MODEL_PKG a, TM_MODEL b");
			sb.append(" where 1=1 and a.MODEL_ID = b.MODEL_ID ");
			//封装查询语句的sql
			List<Object> params = new ArrayList<Object>();
			if(modelCode!=null&&!"".equals(modelCode)){
				sb.append(" and b.MODEL_CODE  like '%" + (ActionUtil.getUpperString(modelCode)) +"%'");
			}
			if(startDate!=null&&!"".equals(startDate)){
				sb.append(" and (a.create_date >= to_date('"+startDate+"','yyyy-MM-dd HH24:mi:ss') or a.update_date >=to_date('"+startDate+"','yyyy-MM-dd HH24:mi:ss'))");
			}
			if(endDate!=null&&!"".equals(endDate)){
				sb.append(" and (a.create_date <= to_date('"+endDate+"','yyyy-MM-dd HH24:mi:ss') or a.update_date <=to_date('"+endDate+"','yyyy-MM-dd HH24:mi:ss'))");
			}
			if(operator!=null&&!"".equals(operator)){
				sb.append(" and (a.create_by = "+operator+" or a.update_by = "+operator+") ");
			}
			System.out.println("sql------"+sb.toString());
			//拿到拼好数据权限和全局排序的sql
			String aclSql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
//			String aclSql = DataAclUtil.getAclSql(sb.toString(), null, null, "dlr_id", actionName, user, orderName, da);
			rs =  factory.pageQuery(aclSql, params, new DAOCallback<VhclModelInfoBean>() {
				public VhclModelInfoBean wrapper(ResultSet rs, int idx){
					VhclModelInfoBean bean = new VhclModelInfoBean();
					try{
						bean.setModelPkgId(rs.getString("MODEL_PKG_ID"));
						bean.setStandardPkg(rs.getString("STANDARD_PKG"));
						bean.setModelId(rs.getString("MODEL_ID"));
						bean.setModelCode(rs.getString("MODEL_CODE"));
						return bean;
					}catch(SQLException e){
						throw new DAOException(e);
					}
				}
			}, Constant.PAGE_SIZE, curPage);
			
		}catch(Exception e){
			throw e;
		}
		return rs;
	}
	/**
	* 查询车型W
	 * author: wry
	 * @throws Exception
	 * date: 2009-08-20
	 */
	public static PageResult<VhclModelInfoBean> getVhclInfo(String brand,String seriesId, String  vhclType, 
			String dspm, String gearBox, String modelYear, String startDate,String endDate,String operator,String actionName, 
			AclUserBean user,String orderName, String da,int curPage) throws Exception{
		PageResult<VhclModelInfoBean> rs = null;
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select a.MODEL_ID,a.PROD_AREA, b.SERIES_NAME,a.VHCL_TYPE,a.DSPM,a.GEAR_BOX,a.NOTICE_CODE,a.MODEL_NAME,a.MODEL_YEAR,c.brand_name ");
			sb.append(" from TM_MODEL a, TM_SERIES b,tm_brand c where 1=1 and a.SERIES_ID=b.SERIES_ID  AND c.brand_id = b.brand_id");
			//封装查询语句的sql
			List<Object> params = new ArrayList<Object>();
			if(brand!=null&&!"".equals(brand)){
				sb.append(" and c.brand_name ='" + brand +"'");
			}
			if(seriesId!=null&&!"".equals(seriesId)){
				sb.append(" and a.SERIES_ID ='" + seriesId +"'");
			}
			if(vhclType!=null&&!"".equals(vhclType)){
				sb.append(" and a.VHCL_TYPE ='" + vhclType +"'");
			}
			if(dspm!=null&&!"".equals(dspm)){
				sb.append(" and a.DSPM ='" + dspm +"'");
			}
			if(gearBox!=null&&!"".equals(gearBox)){
				sb.append(" and a.GEAR_BOX ='" + gearBox +"'");
			}
			if(modelYear!=null&&!"".equals(modelYear)){
				sb.append(" and a.MODEL_YEAR ='" + modelYear +"'");
			}
			
			if(startDate!=null&&!"".equals(startDate)){
				sb.append(" and (a.create_date >= to_date('"+startDate+"','yyyy-MM-dd HH24:mi:ss') or a.update_date >=to_date('"+startDate+"','yyyy-MM-dd HH24:mi:ss'))");
			}
			if(endDate!=null&&!"".equals(endDate)){
				sb.append(" and (a.create_date <= to_date('"+endDate+"','yyyy-MM-dd HH24:mi:ss') or a.update_date <=to_date('"+endDate+"','yyyy-MM-dd HH24:mi:ss'))");
			}
			if(operator!=null&&!"".equals(operator)){
				sb.append(" and (a.create_by = "+operator+" or a.update_by = "+operator+") ");
			}
			System.out.println("sql------"+sb.toString());
			//拿到拼好数据权限和全局排序的sql
			String aclSql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
//			String aclSql = DataAclUtil.getAclSql(sb.toString(), null, null, "dlr_id", actionName, user, orderName, da);
			rs =  factory.pageQuery(aclSql, params, new DAOCallback<VhclModelInfoBean>() {
				public VhclModelInfoBean wrapper(ResultSet rs, int idx){
					VhclModelInfoBean bean = new VhclModelInfoBean();
					try{						
						bean.setModelId(rs.getString("MODEL_ID"));
						bean.setProdArea(rs.getString("brand_name"));
						bean.setSeriesId(rs.getString("SERIES_NAME"));
						bean.setVhclType(rs.getString("VHCL_TYPE"));
						bean.setGearBox(rs.getString("GEAR_BOX"));
						bean.setDspm(rs.getString("DSPM"));
						bean.setNoticeCode(rs.getString("NOTICE_CODE"));
						bean.setModelName(rs.getString("MODEL_NAME"));
						bean.setModelYear(rs.getString("MODEL_YEAR"));
						return bean;
					}catch(SQLException e){
						throw new DAOException(e);
					}
				}
			}, Constant.PAGE_SIZE, curPage);
			
		}catch(Exception e){
			throw e;
		}
		return rs;
	}
}
