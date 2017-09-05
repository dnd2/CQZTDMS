/**********************************************************************
 * <pre>
 * FILE : DlrDepProEmpDAO.java
 * CLASS : DlrDepProEmpDAO
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
 * $Id: DlrDepProEmpDAO.java,v 1.1 2010/08/16 01:43:36 yuch Exp $
 */

package com.infodms.dms.dao.orgmng;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TmDlrDeptEmpPO;
import com.infodms.dms.po.TmDlrDeptPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.callbackimpl.POCallBack;
 
public class DlrDepProEmpDAO {

	private static POFactory factory = POFactoryBuilder.getInstance();
	private static Logger logger = Logger.getLogger(DlrDepProEmpDAO.class);

	/**
	 * Function    : 显示所有的部门下推荐人的信息  
	 * @param	   : deptId 部门ID
	 * @throws     : Exception
	 * @return     : 满足条件的车辆信息
	 * LastUpdate  : 2009-09-02
	 */
	public static List<TmDlrDeptEmpPO> selectEmpListBydeptId(String deptId
				) throws Exception { 
		List<TmDlrDeptEmpPO> list  =null;
		try {
			String sql = "  select * from  TM_DLR_DEPT_EMP  where DLR_DEPT_ID ='"
				+ deptId+ "' and  EMP_STAT='"+Constant.STATUS_ENABLE+"'";   
			System.out.println("sssssssssssssssssssss"+sql);
			list = factory.select(sql, null, new POCallBack(factory,TmDlrDeptEmpPO.class));
		} catch (Exception e) {
			throw e;
		}
		return list;
	}
	/**
	 * Function    : 删除部门下推荐人信息
	 * @param	   : deptId 部门ID
	 * @throws     : Exception
	 * @return     : 所影响的行数
	 * LastUpdate  : 2009-09-02
	 */
	public static int delete(String deptId) { 
	    int row =0;
		try {
	    	String sql = "delete from TM_DLR_DEPT_EMP e where e.DLR_DEPT_ID='"
				+ deptId.trim() + "'";
	    	row  = factory.delete(sql, null);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return row;
	}
	/**
	 * 判断部门名称是否已经存在
	 * @param deptId
	 * @param deptName
	 * @return
	 */
     public static List<TmDlrDeptPO> selectdlrdept(String deptId,String deptName)
     {
     	String sql = "select * from tm_dlr_dept a where a.dlr_dept_id!='"+deptId.trim()
							+"' and a.dlr_dept_id in(select a.dlr_dept_id from TM_DLR_DEPT a  "
							+" where a.dlr_dept_id= '"+deptId.trim()+"' or a.DEPT_NAME = '"+deptName+"')";
     	return factory.select(sql, null, new POCallBack(factory,TmDlrDeptPO.class)); 
     }
    /**
     * 查询单个部门下的推荐人
     * @param deptId
     * @return
     */
     public static List<TmDlrDeptEmpPO> findEmpbydeptid(String deptId)
     {
    	 String sql = "SELECT * FROM tm_dlr_dept_emp WHERE dlr_dept_id = '"+deptId+"' ";
    	 System.out.println("sql111111============"+sql.toString());
    	 return factory.select(sql, null, new POCallBack(factory,TmDlrDeptEmpPO.class));  
     }
     /**
      * 修改推荐人的名称
      * @param empid
      * @param empName
      */
     public static void updateEmpname(String empid,String empName)
     {
         String sql ="update tm_dlr_dept_emp set EMP_NAME = '"+empName+"'  where DEPT_EMP_ID	='"+empid+"'";//,set EMP_STAT = '"+Constant.STATUS_DISABLE +"'
     //   System.out.println("修改名称dao-----"+sql);
         factory.update(sql, null);
     }
     /**
      * 修改推荐人的状态
      * @param empid
      * @param empName
      */
     public static void updateEmpstate(String deptId,String empids)
     { 
    	 StringBuffer sb = new StringBuffer();
    	 sb.append(" update TM_DLR_DEPT_EMP set EMP_STAT='"+Constant.STATUS_DISABLE +"' ");
    	 
         sb.append(" where DLR_DEPT_ID='"+deptId+"' ");
         
         sb.append("and DEPT_EMP_ID not in("+empids+")");  
         
    	 System.out.println("修改状态--------------:"+sb.toString());
    	 factory.update(sb.toString(), null);

     }
     public static void updatedeptempstate(String deptid)
     {  
    	 String sql = "update tm_dlr_dept_emp set EMP_STAT = '"+Constant.STATUS_DISABLE +"'where DLR_DEPT_ID ='"+deptid+"'";
         System.out.println("aaaaaaaaaaaaaa"+sql);
    	 factory.update(sql, null);

     }
     /**
      * 添加推荐人
      * @param emp
      */
	public static void insert(TmDlrDeptEmpPO emp) {
		 System.out.println("添加dao");
	     factory.insert(emp);
	}
}
