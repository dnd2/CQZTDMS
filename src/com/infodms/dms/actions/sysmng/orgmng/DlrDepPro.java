/**********************************************************************
* <pre>
* FILE : DlrDepPro.java
* CLASS : DlrDepPro
*
* AUTHOR : LiuSha
*
* FUNCTION : 推荐人部门维护.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-02| LiuSha | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: DlrDepPro.java,v 1.1 2010/08/16 01:44:23 yuch Exp $
 */

package com.infodms.dms.actions.sysmng.orgmng;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.orgmng.DlrDepProDAO;
import com.infodms.dms.dao.orgmng.DlrDepProEmpDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDlrDeptEmpPO;
import com.infodms.dms.po.TmDlrDeptPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.POCallBack;
 

public class DlrDepPro {
	public ActionContext act = ActionContext.getContext();
	public RequestWrapper request = act.getRequest();
	public Logger logger = Logger.getLogger(DlrDepPro.class); 
	public POFactory factory = POFactoryBuilder.getInstance();
    /**
     * Function       :  分页显示所有的部门信息  
     * @author        :  LiuSha
     * CreateDate     :  2009-09-07
     * @version       :  0.1
     */ 
	public void queryAllDep() {
		AclUserBean  logonUser = null;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);  
			String deptname = CommonUtils.checkNull(request.getParamValue("DEPT_NAME"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			String orderName = request.getParamValue("orderCol");
			String da = request.getParamValue("order");
			PageResult<TmDlrDeptPO> deptlist = DlrDepProDAO.getDepInfo(deptname, curPage,logonUser,request.getRequestURI() ,orderName,da);
			act.setOutData("ps", deptlist);
			act.setForword("/jsp/systemMng/orgMng/recommentDpt.jsp");
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"查询部门信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 
	}

	 /**
     * Function       :  删除部门信息(将部门状态设为失效) 
     * @author        :  LiuSha
     * CreateDate     :  2009-09-02
     * @version       :  0.1
     */ 
	public void deptDelete() { 
		AclUserBean  logonUser = null;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String dlrids= (String) act.getSession().get(String.valueOf(Constant.DLR_BR));
			
			String deptId = CommonUtils.checkNull(request.getParamValue("dlrDeptId"));
			int row = DlrDepProDAO.updateDeptStat(deptId, logonUser, request.getRequestURI(), dlrids);
			if (row != 0) {
				act.setOutData("ACTION_RESULT", 1);
			} else {
				act.setOutData("ACTION_RESULT", 0);
			} 
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"删除部门信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 
	} 
	/**
    * Function       :  查询单个部门下的所有推荐人信息  
    * CreateDate     :  2009-09-02
    * @version       :  0.1
    */
	public void findDeptAndEmp() {
		AclUserBean  logonUser = null;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String deptId = CommonUtils.checkNull(request.getParamValue("dlrDeptId"));
			TmDlrDeptPO dept = DlrDepProDAO.findDeptById(deptId); 
			List<TmDlrDeptEmpPO> emplist = DlrDepProEmpDAO .selectEmpListBydeptId(deptId); 
			request.setAttribute("DEPT", dept);
			act.setOutData("EMP_LIST", emplist); 
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE," 查询单个部门下的所有推荐人信息 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 
		act.setForword("/jsp/systemMng/orgMng/cmdDptMdf.jsp"); 
	}
	/**
    * Function       :  添加推荐人部门和推荐人  
    * CreateDate     :  2009-09-02
    * @version       :  0.1
    */ 
	public void addDeptAndEmp() {
		AclUserBean logonUser = null;
		try {  
		    logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logger.info(logonUser.getUserId()); 
			TmDlrDeptPO deptobj = new TmDlrDeptPO();
			String deptCode = CommonUtils.checkNull(request.getParamValue("DEPT_CODE"));//部门编号
			String deptName = CommonUtils.checkNull(request.getParamValue("DEPT_NAME"));//部门名称
			TmDlrDeptPO t =	new TmDlrDeptPO();
			t.setDeptName(deptName); 
			List<TmDlrDeptPO> list = factory.select(t); //检测经销商名称是否重复
			String sql = "select  * from  TM_DLR_DEPT a where a.DEPT_CODE ='"+deptCode+"' "; 
        	List<TmDlrDeptPO> listcode = factory.select(sql.toString(), null, new POCallBack(factory,TmDlrDeptPO.class)); 
			if(listcode.size()!=0)
			{
				listcode.clear();  
				act.setForword("/jsp/systemMng/orgMng/cmdDptAdd.jsp");
				throw new BizException(act ,ErrorCodeConstant.ALREADY_EXIST_CODE,"经销商代码",deptCode);
			}else
			{
				if(list.size()==0){ 
					deptobj.setDlrDeptId(factory.getStringPK(deptobj));
					deptobj.setDlrId(String.valueOf(logonUser.getCompanyId()));
					deptobj.setDeptCode(deptCode);
					deptobj.setDeptName(deptName);
					deptobj.setDeptStat(String.valueOf(Constant.STATUS_ENABLE));
					deptobj.setCreateDate(new Date());
					factory.insert(deptobj);
			
					String deptid = deptobj.getDlrDeptId();
					String[] empnames = request.getParamValues("EMP_NAME");
					if (empnames.length != 0) {
						for (int i = 0; i < empnames.length; i++) {
							TmDlrDeptEmpPO empobj = new TmDlrDeptEmpPO();
							empobj.setDeptEmpId(factory.getStringPK(empobj));
							empobj.setEmpName(empnames[i].toString());
							empobj.setDlrDeptId(deptid);
							empobj.setCreateDate(new Date());
							empobj.setEmpStat(String.valueOf(Constant.STATUS_ENABLE));
							factory.insert(empobj);
						}
					} 
					act.setForword("/jsp/systemMng/orgMng/recommentDpt.jsp.jsp"); 
					}
					else
					{
						list.clear(); 
					 	act.setForword("/jsp/systemMng/orgMng/cmdDptAdd.jsp");
						throw new BizException(act ,ErrorCodeConstant.ALREADY_EXIST_CODE,"经销商名称",deptName);
					}
			}
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e)
		{
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE);
			logger.error(logonUser,e1);
			act.setException(e1);
		}  
	} 
 
   /**
    * Function       :  修改部门和部门下推荐人的信息 
    * CreateDate     :  2009-09-02
    * @version       :  0.1
    */ 
	public void upateDeptInfo() {
		AclUserBean logonUser  =  null;
		try {   
		    logonUser = (AclUserBean) act.getSession().get( Constant.LOGON_USER);
			logger.info(logonUser.getUserId()); 
			String deptId = CommonUtils.checkNull(request.getParamValue("deptId"));      // 部门id
			String deptCode = CommonUtils.checkNull(request.getParamValue("DEPT_CODE")); // 部门代码
			String deptName = CommonUtils.checkNull(request.getParamValue("DEPT_NAME")); // 部门名称
			TmDlrDeptPO t =	new TmDlrDeptPO();
			t.setDeptName(deptName);  
			//判断部门名称是否已经存在
			List<TmDlrDeptPO> list =DlrDepProEmpDAO.selectdlrdept(deptId, deptName);
			// 部门信息无重复情况下,对部门下更新该部门和推荐人信息
        	if(list.size()==0){  
				TmDlrDeptPO dept = new TmDlrDeptPO();
				dept.setDlrDeptId(deptId);
				dept.setDeptCode(deptCode);
				dept.setDeptName(deptName);   
				DlrDepProDAO.updateDept(dept); // 修改部门信息     
				//DlrDepProEmpDAO.updatedeptempstate(deptId);//先把所有的推荐人都失效 
				String[]empids = request.getParamValues("EMP_ID");  //得到员工的ID集合
				String[] empnames = request.getParamValues("EMP_NAME"); //得到员工的姓名
				  
				List<TmDlrDeptEmpPO> dbEmps = DlrDepProEmpDAO.findEmpbydeptid(deptId); 
				System.out.println("--------- dbEmps  :"+dbEmps.size()); 
					for(int i=0;i<dbEmps.size();i++)
					{
						 TmDlrDeptEmpPO empobj =dbEmps.get(i); 
						 for(int j =0 ;j<empids.length;j++)
						 { 
							 if(empobj.getDeptEmpId().equals(empids[j]))
							 {
								 DlrDepProEmpDAO.updateEmpname(empids[j], empnames[j]) ;
							 }
						 }
					} 
					if(empids.length!=0)
					{ 
				    	String empidstr = ""; 
						for(int i=0;i<empids.length;i++ )
						{
							System.out.println(i);
							empidstr +="'"+empids[i]+"',"; 
						}
						DlrDepProEmpDAO.updateEmpstate(deptId ,empidstr.substring(0,empidstr.length()-1));
					}
				// 除了有Id的以外,没Id 的都是新增加的
				for(int j =empids.length ;j<empnames.length;j++)
				 {
						TmDlrDeptEmpPO empinto = new TmDlrDeptEmpPO();
						empinto.setDeptEmpId(factory.getStringPK(empinto)); 
						empinto.setDlrDeptId(deptId);
						empinto.setEmpName(empnames[j].trim());
						empinto.setEmpStat(String.valueOf(Constant.STATUS_ENABLE)); 
						DlrDepProEmpDAO.insert(empinto);
				 }
				 act.setOutData("ACTION_RESULT", 1);
        	}
			else
			{
				//act.setOutData("ACTION_RESULT", 0);
				throw new BizException(act ,ErrorCodeConstant.ALREADY_EXIST_CODE,"推荐人部门名称",deptName);
			} 
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e)
		{e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE);
			logger.error(logonUser,e1);
			act.setException(e1);
		}  
		}
}
