/**********************************************************************
 * <pre>
 * FILE : DlrDepProDAO.java
 * CLASS : DlrDepProDAO
 *
 * AUTHOR : LiuSha
 *
 * FUNCTION : 经销商信息维护.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-09-02| LiuSha  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * $Id: DlrDepProDAO.java,v 1.1 2010/08/16 01:43:36 yuch Exp $
 */

package com.infodms.dms.dao.orgmng;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DataAclUtil;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDlrDeptPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.POCallBack;
/**
 * Function : 车辆失效设定
 * 
 * @author : LiuSha CreateDate : 2009-08-31
 * @version : 0.1
 */
public class DlrDepProDAO {
	public static Logger logger = Logger.getLogger(DlrDepProDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * Function : 分页显示所有的部门信息
	 * @param : deptname 部门名称
	 * @throws : Exception
	 * @return : 满足条件的车辆信息
	 * LastUpdate : 2009-09-07
	 */
	public static PageResult<TmDlrDeptPO> getDepInfo(String deptname,
			int curPage,AclUserBean logonUser,String actionname ,String orderName,String da ) throws BizException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from TM_DLR_DEPT d where 1=1   ");
		sql.append("   and  d.DEPT_STAT	='" + Constant.STATUS_ENABLE + "'  ");
		if (deptname != null && !"".equals(deptname)) {
			sql.append("  and   d.DEPT_NAME like '%" + deptname.trim() + "%'  ");
		} 
		String sb = DataAclUtil.getAclSql(sql.toString(), null, null, "dlr_id", actionname, logonUser, orderName, da);  
		return factory.pageQuery(sb, null, new POCallBack(factory,
				TmDlrDeptPO.class), Constant.PAGE_SIZE, curPage);
	}
	/**
	 * Function :将部门的状态该为无效: 删除部门 
	 * @param : deptId
	 * @throws :Exception
	 * @return : 满足条件的车辆信息 LastUpdate : 2009-09-02
	 */
	public static int updateDeptStat(String deptId,AclUserBean logonUser,String actionname,String dlrid  ) throws BizException{
		StringBuffer sql = new StringBuffer();
	    sql.append(" update TM_DLR_DEPT d  set  d.DEPT_STAT ='" + Constant.STATUS_DISABLE + "' ");
	    sql.append(	"  where d.DLR_DEPT_ID ='" + deptId + "' "); 
		return factory.update(sql.toString(), null);
	}

	/**
	 * 查询单个的部门
	 * @param deptId
	 * @return LastUpdate : 2009-09-02
	 */
	public static TmDlrDeptPO findDeptById(String deptId) {
		TmDlrDeptPO po = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from TM_DLR_DEPT d " ); 
			sql.append("where d.dlr_dept_id ='" + deptId.trim() + "'");
			po = (TmDlrDeptPO) factory.select(sql.toString(), null,
					new POCallBack(factory, TmDlrDeptPO.class)).get(0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return po;
	}
	/**
	 * 修改该部门信息
	 * @param dept
	 * LastUpdate : 2009-09-02
	 */
	public static void updateDept(TmDlrDeptPO dept ) throws BizException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" update TM_DLR_DEPT d set d.DEPT_CODE='" + dept.getDeptCode() + "' ");
			sql.append(" ,d.DEPT_NAME ='" + dept.getDeptName() + "' ");
			sql.append(" where d.DLR_DEPT_ID='" + dept.getDlrDeptId().trim() + "' ");
			//String sb = DataAclUtil.getAclSql(sql.toString(), null, null, "dlr_id", actionname, logonUser, null, null);  
			factory.update(sql.toString(), null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
 
}
