/**********************************************************************
* <pre>
* FILE : IndividualTaskDAO.java
* CLASS : IndividualTaskDAO
*
* AUTHOR : wry
*
* FUNCTION : 个人任务清单DAO
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-08| wry  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.dao.individualTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OperateRemindBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DataAclUtil;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

/**
 * function:个人任务清单
 * author: wry
 * CreateDate: 2009-09-08
 * @version:0.1
 */
public class IndividualTaskDAO {
	public static Logger logger = Logger.getLogger(IndividualTaskDAO.class);
	public static POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * function:得到回访信息
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-08	
	 */
	public static List<OperateRemindBean> wayOfVisiting(String actionName, AclUserBean user,String funcs) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql3 = new StringBuffer();
			ArrayList list = new ArrayList();
			sql.append(" select a.SUBMITER,tc1.CODE_DESC, tc2.CODE_DESC custType, a.OPERATE_REMIND_ID,a.PLANNED_CONTENT, " +
					  "  a.CUST_NAME, a.MAIN_CONT_MODE, a.PROC_URL, a.TABLE_ID, to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.submiter submitert,a.DEPT_ID adept_id,a.DLR_ID adlr_id	 " +
					  "from TC_OPERATE_REMIND a, TC_CODE tc1, TC_CODE tc2 " );
			sql.append(" where a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
			sql.append(" and a.REMIND_TYPE = " + Constant.RETURN_VISIT);
		    sql.append(" and a.OPERATE_TYPE = tc1.CODE_ID and a.CUST_TYPE = tc2.CODE_ID ") ;
		    sql.append(" and a.OPERATE_TYPE = " + Constant.CLIENT_LOVING);
		    
			sql2.append(" select a.SUBMITER,tc1.CODE_DESC, tc2.CODE_DESC custType, a.OPERATE_REMIND_ID,a.PLANNED_CONTENT, " +
					  "  a.CUST_NAME, a.MAIN_CONT_MODE, a.PROC_URL, a.TABLE_ID, to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.submiter submitert,a.DEPT_ID adept_id,a.DLR_ID adlr_id	 " +
					  "from TC_OPERATE_REMIND a, TC_CODE tc1, TC_CODE tc2 " );
			sql2.append(" where a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
			sql2.append(" and a.REMIND_TYPE = " + Constant.RETURN_VISIT);
		    sql2.append(" and a.OPERATE_TYPE = tc1.CODE_ID and a.CUST_TYPE = tc2.CODE_ID ") ;
		    sql2.append(" and a.OPERATE_TYPE = " + Constant.RPCH_AGRM_DISPOSITION);
		    
			sql3.append(" select a.SUBMITER,tc1.CODE_DESC, tc2.CODE_DESC custType, a.OPERATE_REMIND_ID,a.PLANNED_CONTENT, " +
					  "  a.CUST_NAME, a.MAIN_CONT_MODE, a.PROC_URL, a.TABLE_ID, to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.submiter submitert,a.DEPT_ID adept_id,a.DLR_ID adlr_id	 " +
					  "from TC_OPERATE_REMIND a, TC_CODE tc1, TC_CODE tc2 " );
			sql3.append(" where a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
			sql3.append(" and a.REMIND_TYPE = " + Constant.RETURN_VISIT);
		    sql3.append(" and a.OPERATE_TYPE = tc1.CODE_ID and a.CUST_TYPE = tc2.CODE_ID ") ;
		    sql3.append(" and a.OPERATE_TYPE = " + Constant.SALE_ORDER_DISPOSITION);
		    
		    StringBuffer sqlAll = new StringBuffer("");
			String aclSql = "";
			String aclSql2 = "";
			String aclSql3 = "";
			try{
				aclSql = DataAclUtil.getAclSql(sql.toString(), "submitert", "adept_id", "adlr_id", "/DMSUC/custmng/custcare/CustCareMng/allCustCareNoteSearch.do", user, null, null);
					sqlAll.append(aclSql);
			}catch (BizException e) {
				logger.info("该用户没有客户关怀权限");
			}
			try{
				aclSql2 = DataAclUtil.getAclSql(sql2.toString(), "submitert", "adept_id","adlr_id", "/DMSUC/carpurchase/purchaseintent/CustIntentQueryMng/intentFollowQueryPre.do", user, null, null);
				if(!"".endsWith(sqlAll.toString())){
					sqlAll.append(" UNION " + aclSql2);
				}else{
					sqlAll.append(aclSql2);
				}
			}catch(BizException e){
				logger.info("该用户没有收购意向权限");
			}
			try{
				aclSql3 = DataAclUtil.getAclSql(sql3.toString(), "submitert", "adept_id","adlr_id", "/DMSUC/carsell/saleIntent/ScoutSellIntentFollowSearch/intentFollowQuery.do", user, null, null);
				if(!"".endsWith(sqlAll.toString())){
					sqlAll.append(" UNION " + aclSql3);
				}else{
					sqlAll.append(aclSql3);
				}
			}catch(BizException e){
				logger.info("该用户没有销售意向权限");
			}
		    logger.debug("回访信息---" + sqlAll.toString());
			//拿到拼好数据权限和全局排序的sql
			 List<OperateRemindBean> tcoList = null;
			 if(sqlAll!=null&&!sqlAll.toString().equals("")){
					tcoList = factory.select(sqlAll.toString(), list, new DAOCallback<OperateRemindBean>(){
				public OperateRemindBean wrapper(ResultSet rs, int idx){
					OperateRemindBean bean = new OperateRemindBean();
					try {
						bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
						bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
						bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
						bean.setPlannedContent(CommonUtils.checkNull(rs.getString("PLANNED_CONTENT")));
						bean.setCustType(CommonUtils.checkNull(rs.getString("custType")));
						bean.setCustName(CommonUtils.checkNull(rs.getString("CUST_NAME")));
						bean.setMainContMode(CommonUtils.checkNull(rs.getString("MAIN_CONT_MODE")));
						bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
						bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return bean;
				}
			});
				}
			return tcoList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * function:得到审批信息
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-09	
	 */
	public static List<OperateRemindBean> examineApprove(String actionName, AclUserBean user,String funcs) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql3 = new StringBuffer();
			StringBuffer sql4 = new StringBuffer();
			StringBuffer sql5 = new StringBuffer();
			
			ArrayList list = new ArrayList();
			sql.append(" select tc1.CODE_DESC,tu.NAME, a.OPERATE_TYPE,a.SUBMITER,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC," +
						"a.VIN,a.PRICE,a.PROC_URL,a.TABLE_ID,a.DEPT_ID dept_idt,a.DLR_ID dlr_idt,a.SUBMITER submitert	 " +
						" from TC_OPERATE_REMIND a, TC_CODE tc1, tc_user tu " );
			sql.append(" where a.REMIND_TYPE = " + Constant.EXAMINE_AND_ENDORSE);
			sql.append(" and a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
		    sql.append(" and a.OPERATE_TYPE = tc1.CODE_ID  and a.submiter = tu.user_id " );
		    sql.append(" and a.OPERATE_TYPE = " + Constant.PURCHASE_AGREEMENT);
//		    sql.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs);
//		    sql.append(" UNION ");
			sql2.append(" select tc1.CODE_DESC,tu.NAME, a.OPERATE_TYPE,a.SUBMITER,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC," +
					   " a.VIN,a.PRICE,a.PROC_URL,a.TABLE_ID,a.DEPT_ID dept_idt,a.DLR_ID dlr_idt,a.SUBMITER submitert" +
					   " from TC_OPERATE_REMIND a, TC_CODE tc1, tc_user tu " );
			sql2.append(" where a.REMIND_TYPE = " + Constant.EXAMINE_AND_ENDORSE);
			sql2.append(" and a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
		    sql2.append(" and a.OPERATE_TYPE = tc1.CODE_ID  and a.submiter = tu.user_id " );
		    sql2.append(" and a.OPERATE_TYPE = " + Constant.VEHICLE_CANNIBALIZE);
//		    sql2.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs);
//		    sql.append(" UNION ");
			sql3.append(" select tc1.CODE_DESC,tu.NAME, a.OPERATE_TYPE,a.SUBMITER,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC," +
					   " a.VIN,a.PRICE,a.PROC_URL,a.TABLE_ID,a.DEPT_ID dept_idt,a.DLR_ID dlr_idt,a.SUBMITER submitert " +
					   " from TC_OPERATE_REMIND a, TC_CODE tc1, tc_user tu " );
			sql3.append(" where a.REMIND_TYPE = " + Constant.EXAMINE_AND_ENDORSE);
			sql3.append(" and a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
		    sql3.append(" and a.OPERATE_TYPE = tc1.CODE_ID  and a.submiter = tu.user_id " );
		    sql3.append(" and a.OPERATE_TYPE = " + Constant.VEHICLE_SELL_FOLD );
//		    sql3.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs);
//		    sql.append(" UNION ");
			sql4.append(" select tc1.CODE_DESC,tu.NAME, a.OPERATE_TYPE,a.SUBMITER,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC," +
					   " a.VIN,a.PRICE,a.PROC_URL,a.TABLE_ID,a.DEPT_ID dept_idt,a.DLR_ID dlr_idt,a.SUBMITER submitert " +
					   " from TC_OPERATE_REMIND a, TC_CODE tc1, tc_user tu " );
			sql4.append(" where a.REMIND_TYPE = " + Constant.EXAMINE_AND_ENDORSE);
			sql4.append(" and a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
		    sql4.append(" and a.OPERATE_TYPE = tc1.CODE_ID  and a.submiter = tu.user_id " );
		    sql4.append(" and a.OPERATE_TYPE = " + Constant.VEHICLE_SELL_EXIT );
//		    sql.append(" UNION ");
			sql5.append(" select tc1.CODE_DESC,tu.NAME, a.OPERATE_TYPE,a.SUBMITER,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC," +
					   " a.VIN,a.PRICE,a.PROC_URL,a.TABLE_ID,a.DEPT_ID dept_idt,a.DLR_ID dlr_idt,a.SUBMITER submitert " +
					   " from TC_OPERATE_REMIND a, TC_CODE tc1, tc_user tu " );
			sql5.append(" where a.REMIND_TYPE = " + Constant.EXAMINE_AND_ENDORSE);
			sql5.append(" and a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
		    sql5.append(" and a.OPERATE_TYPE = tc1.CODE_ID  and a.submiter = tu.user_id " );
		    sql5.append(" and a.OPERATE_TYPE = " + Constant.SALE_ORDER );
//		    sql4.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs );
//		    logger.debug("审批信息---" + sql);
		    StringBuffer sqlAll = new StringBuffer("");
			String aclSql = "";
			String aclSql2 = "";
			String aclSql3 = "";
			String aclSql4 = "";
			String aclSql5 = "";
//			判断每个提醒是否有权限，如果权限有的话把sql语句拼写到一起
			try{
				 aclSql = DataAclUtil.getAclSql(sql.toString(), "submitert", "dept_idt","dlr_idt", "/DMSUC/carsell/carallocate/AllocateOut/allocateOutQuery.do", user, null, null);
					sqlAll.append(aclSql);
			}catch (BizException e) {
				logger.info("该用户没有收购协议权限");
			}
			try{
				aclSql2 = DataAclUtil.getAclSql(sql2.toString(), "submitert", null,"dlr_idt", "/DMSUC/carpurchase/purchaseprotocol/PurchaseProtocolApprovalMng/purchaseProtocolQueryPre.do", user, null, null);
				if(!"".endsWith(sqlAll.toString())){
					sqlAll.append(" UNION " + aclSql2);
				}else{
					sqlAll.append(aclSql2);
				}
			}catch(BizException e){
				logger.info("该用户没有车辆调拨调入权限");
			}
			try{
				aclSql3 = DataAclUtil.getAclSql(sql3.toString(), "submitert", null,"dlr_idt", "/DMSUC/carsell/carcnsg/CnsgIn/cnsgInQuery.do", user, null, null);
				if(!"".endsWith(sqlAll.toString())){
					sqlAll.append(" UNION " + aclSql3);
				}else{
					sqlAll.append(aclSql3);
				}
			}catch(BizException e){
				logger.info("该用户没有车辆寄售调入权限");
			}
			try{
				aclSql4 = DataAclUtil.getAclSql(sql4.toString(), "submitert", null,"dlr_idt", "/DMSUC/carsell/carcnsg/CnsgUntreadAffirm/untreadAffirmQuery.do", user, null, null);
				if(!"".endsWith(sqlAll.toString())){
					sqlAll.append(" UNION " + aclSql4);
				}else{
					sqlAll.append(aclSql4);
				}
			}catch(BizException e){
				logger.info("该用户没有车辆寄售退回权限");
			}
			try{
				aclSql5 = DataAclUtil.getAclSql(sql5.toString(), "submitert", "dept_idt","dlr_idt", "/DMSUC/carsell/sellOrder/SaleOrderApprove/querySaleOrderToApprove.do", user, null, null);
				if(!"".endsWith(sqlAll.toString())){
					sqlAll.append(" UNION " + aclSql5);
				}else{
					sqlAll.append(aclSql5);
				}
			}catch(BizException e){
				logger.info("该用户没有销售订单权限");
			}
//			if(!"".endsWith(aclSql)&&!"".endsWith(aclSql2)&&!"".endsWith(aclSql3)&&!"".endsWith(aclSql4)&&!"".endsWith(aclSql5)){
//				sqlAll.append(aclSql+" UNION "+aclSql2+" UNION "+aclSql3+" UNION "+aclSql4+" UNION "+aclSql5);
//			}
			 logger.debug("审批信息---" + sqlAll.toString());
			 List<OperateRemindBean> tcoList = null;
				if(sqlAll!=null&&!sqlAll.toString().equals("")){
					tcoList = factory.select(sqlAll.toString(), list, new DAOCallback<OperateRemindBean>(){
				public OperateRemindBean wrapper(ResultSet rs, int idx){
					OperateRemindBean bean = new OperateRemindBean();
					try {
						bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
						bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
						bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
						bean.setVhclLic(CommonUtils.checkNull(rs.getString("VHCL_LIC")));
						bean.setVin(CommonUtils.checkNull(rs.getString("VIN")));
						bean.setPrice(rs.getDouble("PRICE")/10000);
//						bean.setSubmiter(CommonUtils.checkNull(rs.getString("NAME")));
						bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
						bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
						bean.setSubmiter(CommonUtils.checkNull(rs.getString("NAME")));
//						判断操作类型如果是调拨和寄售，提交人就是经销商,如果不是，就根据提交人id，找user表里的名称
//						if(rs.getString("OPERATE_TYPE").equals(Constant.VEHICLE_CANNIBALIZE) 
//						   || rs.getString("OPERATE_TYPE").endsWith(Constant.VEHICLE_SELL_FOLD)){
//							if(rs.getString("SUBMITER")!=null){
//								TmCompanyPO  tcPO = new TmCompanyPO();
//								tcPO.setCompanyId(rs.getString("SUBMITER"));
//								List tcList = factory.select(tcPO);
//								if(tcList!=null && tcList.size() >0 ){
//									tcPO = (TmCompanyPO)tcList.get(0);
//									bean.setSubmiter(tcPO.getCompanyShortName());
//								}
//						  }
//						}
//						else{
//						    if(rs.getString("SUBMITER")!=null){
//							TcUserPO tuPO = new TcUserPO();
//							tuPO.setUserId(rs.getString("SUBMITER"));
//							List tuList = factory.select(tuPO);
//							if(tuList!=null && tuList.size() >0 ){
//								tuPO = (TcUserPO)tuList.get(0);
//								bean.setSubmiter(tuPO.getName());
//							}
//						}
//						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return bean;
				}
			});
		}
			return tcoList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * function:得到待办信息-整备
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-10	
	 */
	public static List<OperateRemindBean> pendinRequest(String actionName, AclUserBean user, String funcs) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			ArrayList list = new ArrayList();
			sql.append(" select tc1.CODE_DESC,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC,a.VIN,a.PROC_URL,a.TABLE_ID, a.DEPT_ID deptId,a.DLR_ID dlrId, a.SUBMITER submitert from TC_OPERATE_REMIND a, TC_CODE tc1 " );
			sql.append(" where a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
			sql.append(" and a.REMIND_TYPE = " + Constant.WAIT_TO_HANDLE);
//			sql.append(" and a.OPERATE_TYPE in (" + Constant.RPCH_AGRM_DISPOSITION + "," + Constant.CLIENT_LOVING +","+Constant.SALE_ORDER_DISPOSITION+")");
		    sql.append(" and a.OPERATE_TYPE = tc1.CODE_ID and a.DLR_ID = "+user.getCompanyId() );
		    sql.append(" and a.OPERATE_TYPE = " + Constant.EQUIPPEN_NEWSREEL );
//		    sql.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs);
		    logger.debug("待办整备---" + sql);
		    String aclSql = "";
			try{
		         aclSql = DataAclUtil.getAclSql(sql.toString(), "submitert", "deptId", "dlrId", "/DMSUC/dightManager/carDight/DightRecord/dightQuery.do", user, null, null);
			}catch(BizException e){
				logger.info("该用户没有整备记录权限");
			}
			List<OperateRemindBean> tcoList = null;
			if(aclSql!=null&&!aclSql.equals("")){
				tcoList = factory.select(aclSql.toString(), list, new DAOCallback<OperateRemindBean>(){
					public OperateRemindBean wrapper(ResultSet rs, int idx){
						OperateRemindBean bean = new OperateRemindBean();
						try {
							bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
							bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
							bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
							bean.setVhclLic(CommonUtils.checkNull(rs.getString("VHCL_LIC")));
							bean.setVin(CommonUtils.checkNull(rs.getString("VIN")));
							bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
							bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
			}
			return tcoList;
		}catch (Exception e) {
			throw e;
		}
	}
	/**
	 * function:得到待办信息-车辆出库
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-10	
	 */
	public static List<OperateRemindBean> outRequest(String actionName, AclUserBean user, String funcs ) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			ArrayList list = new ArrayList();
			sql.append("SELECT tc1.code_desc, a.operate_remind_id,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.vhcl_lic,a.vin,a.proc_url,a.table_id,a.dept_id deptId,a.dlr_id dlrId, a.SUBMITER submitert \n");
			sql.append("FROM tc_operate_remind a,tc_code tc1, \n");
			sql.append(" (select DISTINCT tdwk.KEEPER, allrs.operate_remind_id \n");
			sql.append("  from TT_VHCL_INV_INOUT tvii, \n");
			sql.append("   TM_DLR_WH_KEEPER  tdwk, \n");
			sql.append("   (select  operate_remind_id,SUBSTR(table_id, instr(table_id,   'vhclId=',   -1,   1) +7 , 10 ) vhclId, \n");
			sql.append("   SUBSTR(table_id, instr(table_id,   'inoutId=',   -1,   1) +8 , 10 ) inoutId, \n");
			sql.append("   SUBSTR(table_id, instr(table_id,   'outType=',   -1,   1) +8 , 10 ) outType \n");
			sql.append("   from tc_operate_remind  \n");
			sql.append("   where operate_type = " + Constant.VEHICLE_COME_ON +"\n"); 
			sql.append("   AND action_stat = " + Constant.ACCEPT_RES_UNACTION +"\n");
			sql.append("   AND remind_type = " + Constant.WAIT_TO_HANDLE +"\n");
			sql.append("   ) allrs \n");
			sql.append("   where tvii.DLR_WH_ID = tdwk.DLR_WH_ID \n");
			sql.append("   and tvii.VHCL_INV_INOUT_ID =allrs.inoutId \n");
			sql.append("   ) keeper_rs ");
			sql.append("   WHERE a.action_stat = " + Constant.ACCEPT_RES_UNACTION +"\n");
			sql.append("   AND a.remind_type = " + Constant.WAIT_TO_HANDLE +"\n");
			sql.append("   AND a.operate_type = tc1.code_id \n");
			sql.append("   AND a.dlr_id = " + user.getCompanyId() +"\n");
			sql.append("   AND a.operate_type = " + Constant.VEHICLE_COME_ON +"\n"); 
			sql.append("   AND keeper_rs.KEEPER  =  " + user.getUserId()+"\n");
			sql.append("   AND keeper_rs.operate_remind_id=A.operate_remind_id \n");
//			sql.append("   AND SUBSTR(a.proc_url,   0,   instr(a.proc_url,   '/',   -1,   1) -1) IN "+funcs );
			logger.debug("车辆出库---" + sql);
			 String aclSql =""; 
			try{																	    
			  aclSql = DataAclUtil.getAclSql(sql.toString(), null, null, "dlrId", "/DMSUC/storageManager/carOutStorage/CarOutStorageSearch/carOutStorageQuery.do", user, null, null);
			}catch(BizException e){
				logger.info("该用户没有车辆出库权限");
			}
			List<OperateRemindBean> tcoList = null;
			if(aclSql!=null&&!aclSql.equals("")){
				tcoList = factory.select(aclSql.toString(), list, new DAOCallback<OperateRemindBean>(){
					public OperateRemindBean wrapper(ResultSet rs, int idx){
						OperateRemindBean bean = new OperateRemindBean();
						try {
							bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
							bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
							bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
							bean.setVhclLic(CommonUtils.checkNull(rs.getString("VHCL_LIC")));
							bean.setVin(CommonUtils.checkNull(rs.getString("VIN")));
							bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
							bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
			}
			return tcoList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * function:得到待办信息-车辆入库
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-10	
	 */
	public static List<OperateRemindBean> getNotInCome(String actionName, AclUserBean user, String funcs) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			ArrayList list = new ArrayList();
			sql.append("SELECT tc1.code_desc,a.operate_remind_id,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.vhcl_lic,a.vin,a.proc_url,a.table_id,a.dept_id deptId,a.dlr_id dlrId, a.SUBMITER submitert ");
			sql.append("	FROM tc_operate_remind a, ");
			sql.append("			  tc_code tc1, ");
			sql.append("		   (select DISTINCT tdwk.KEEPER, allrs.operate_remind_id,outType  ");
			sql.append("	    from TT_VHCL_INV_INOUT tvii, ");
			sql.append("		     TM_DLR_WH_KEEPER  tdwk, ");
			sql.append("			 (select  operate_remind_id,SUBSTR(table_id, instr(table_id,   'vhclId=',   -1,   1) +7 , 10 ) vhclId,");
		    sql.append("			          SUBSTR(table_id, instr(table_id,   'inoutId=',   -1,   1) +8 , 10 ) inoutId, ");
		    sql.append(" 		SUBSTR(table_id, instr(table_id,   'outType=',   -1,   1) +8 , 10 ) outType ");
		    sql.append("	         from tc_operate_remind  ");
		    sql.append("			 where operate_type = " + Constant.VEHICLE_INCOME );
		    sql.append("			 AND action_stat = " + Constant.ACCEPT_RES_UNACTION );
		    sql.append("	         AND remind_type = " + Constant.WAIT_TO_HANDLE );
		    sql.append("	      ) allrs ");
		    sql.append("	    where tvii.DLR_WH_ID = tdwk.DLR_WH_ID ");
		   	sql.append("	 and tvii.VHCL_INV_INOUT_ID =allrs.inoutId ");
		   	sql.append("		   ) keeper_rs ");
		   	sql.append("	WHERE a.action_stat = " + Constant.ACCEPT_RES_UNACTION );
		   	sql.append("	 AND a.remind_type = " + Constant.WAIT_TO_HANDLE );
		   	sql.append("	 AND a.operate_type = tc1.code_id ");
		   	sql.append("	 AND a.dlr_id = " + user.getCompanyId());
		   	sql.append("	 AND a.operate_type = " + Constant.VEHICLE_INCOME );
		   	sql.append("	 AND keeper_rs.KEEPER  = " + user.getUserId());
		   	sql.append("	 AND keeper_rs.operate_remind_id=A.operate_remind_id ");
//			sql.append("     AND SUBSTR(a.proc_url,   0,   instr(a.proc_url,   '/',   -1,   1) -1) IN "+funcs );
			logger.debug("车辆入库---" + sql);
			String aclSql ="";
			try{
				 aclSql = DataAclUtil.getAclSql(sql.toString(), null, null, "dlrId","/DMSUC/storageManager/carInStorage/CarInStorageSearch/carInStorageQuery.do", user, null, null);
			}catch(BizException e){
				logger.info("该用户没有收购入库权限");
			}
			
			List<OperateRemindBean> tcoList = null;
			if(aclSql!=null&&!aclSql.equals("")){
				tcoList = factory.select(aclSql.toString(), list, new DAOCallback<OperateRemindBean>(){
					public OperateRemindBean wrapper(ResultSet rs, int idx){
						OperateRemindBean bean = new OperateRemindBean();
						try {
							bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
							bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
							bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
							bean.setVhclLic(CommonUtils.checkNull(rs.getString("VHCL_LIC")));
							bean.setVin(CommonUtils.checkNull(rs.getString("VIN")));
							bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
							bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
			}
			return tcoList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * function:得到待办信息-销售上报
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-10	
	 */
	public static List<OperateRemindBean> saleRequest(String actionName, AclUserBean user, String funcs) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			ArrayList list = new ArrayList();
			sql.append(" select tc1.CODE_DESC,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC,a.VIN,a.PROC_URL,a.TABLE_ID, a.DEPT_ID deptId,a.DLR_ID dlrId, a.SUBMITER submitert from TC_OPERATE_REMIND a, TC_CODE tc1 " );
			sql.append(" where a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
			sql.append(" and a.REMIND_TYPE = " + Constant.WAIT_TO_HANDLE);
//			sql.append(" and a.OPERATE_TYPE in (" + Constant.RPCH_AGRM_DISPOSITION + "," + Constant.CLIENT_LOVING +","+Constant.SALE_ORDER_DISPOSITION+")");
		    sql.append(" and a.OPERATE_TYPE = tc1.CODE_ID and a.DLR_ID = "+user.getCompanyId() );
		    sql.append(" and a.OPERATE_TYPE = " + Constant.SELL_REPORT );
		    sql.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs);
		    logger.debug("销售上报---" + sql);
		    String aclSql = "";
			try{
				 aclSql = DataAclUtil.getAclSql(sql.toString(), "submitert", "deptId", "dlrId", "/DMSUC/authcar/sale/SalesReportAction/salesReportPre.do", user, null, null);
			}catch(BizException e){
				logger.info("该用户没有销售上报权限");
			}
			
			List<OperateRemindBean> tcoList = null;
			if(aclSql!=null&&!aclSql.equals("")){
				tcoList = factory.select(aclSql.toString(), list, new DAOCallback<OperateRemindBean>(){
					public OperateRemindBean wrapper(ResultSet rs, int idx){
						OperateRemindBean bean = new OperateRemindBean();
						try {
							bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
							bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
							bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
							bean.setVhclLic(CommonUtils.checkNull(rs.getString("VHCL_LIC")));
							bean.setVin(CommonUtils.checkNull(rs.getString("VIN")));
							bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
							bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
			}
			return tcoList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * function:得到待办信息-车辆过户
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-10	
	 */
	public static List<OperateRemindBean> vhclRequest(String actionName, AclUserBean user, String funcs) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			ArrayList list = new ArrayList();
			sql.append(" select tc1.CODE_DESC,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC,a.VIN,a.PROC_URL,a.TABLE_ID, a.DEPT_ID deptId,a.DLR_ID dlrId, a.SUBMITER submitert from TC_OPERATE_REMIND a, TC_CODE tc1 " );
			sql.append(" where a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
			sql.append(" and a.REMIND_TYPE = " + Constant.WAIT_TO_HANDLE);
//			sql.append(" and a.OPERATE_TYPE in (" + Constant.RPCH_AGRM_DISPOSITION + "," + Constant.CLIENT_LOVING +","+Constant.SALE_ORDER_DISPOSITION+")");
		    sql.append(" and a.OPERATE_TYPE = tc1.CODE_ID and a.DLR_ID = "+user.getCompanyId() );
		    sql.append(" and a.OPERATE_TYPE = " + Constant.VEHICLE_TRANSFER );
//		    sql.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs);
		    logger.debug("车辆过户---" + sql);
		    String aclSql="";
			try{
				 aclSql = DataAclUtil.getAclSql(sql.toString(), "submitert", "deptId", "dlrId", "/DMSUC/carsell/transfer/TransferRecordSearch/transferQuery.do", user, null, null);
			}catch(BizException e){
				logger.info("该用户没有车辆过户权限");
			}
		
			List<OperateRemindBean> tcoList = null;
			if(aclSql!=null&&!aclSql.equals("")){
				tcoList = factory.select(aclSql.toString(), list, new DAOCallback<OperateRemindBean>(){
					public OperateRemindBean wrapper(ResultSet rs, int idx){
						OperateRemindBean bean = new OperateRemindBean();
						try {
							bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
							bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
							bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
							bean.setVhclLic(CommonUtils.checkNull(rs.getString("VHCL_LIC")));
							bean.setVin(CommonUtils.checkNull(rs.getString("VIN")));
							bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
							bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
			}
			return tcoList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * function:得到待办信息-预留取消
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-10	
	 */
	public static List<OperateRemindBean> obligateCancel(String actionName, AclUserBean user, String funcs) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			ArrayList list = new ArrayList();
			sql.append(" select tc1.CODE_DESC,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC,a.VIN,a.PROC_URL,a.TABLE_ID, a.DEPT_ID deptId,a.DLR_ID dlrId, a.SUBMITER submitert from TC_OPERATE_REMIND a, TC_CODE tc1 " );
			sql.append(" where a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
			sql.append(" and a.REMIND_TYPE = " + Constant.WAIT_TO_HANDLE);
//			sql.append(" and a.OPERATE_TYPE in (" + Constant.RPCH_AGRM_DISPOSITION + "," + Constant.CLIENT_LOVING +","+Constant.SALE_ORDER_DISPOSITION+")");
		    sql.append(" and a.OPERATE_TYPE = tc1.CODE_ID and a.DLR_ID = "+user.getCompanyId() );
		    sql.append(" and a.OPERATE_TYPE = " + Constant.OBLIGATE_CANCEL );
//		    sql.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs);
		    logger.debug("预留取消---" + sql);
		    String aclSql="";
			try{
				 aclSql = DataAclUtil.getAclSql(sql.toString(), "submitert", "deptId", "dlrId", "/DMSUC/storageManager/carObligate/CarObligate/carObligateQuery.do", user, null, null);
			}catch(BizException e){
				logger.info("该用户没有预留取消权限");
			}
			
			List<OperateRemindBean> tcoList = null;
			if(aclSql!=null&&!aclSql.equals("")){
				tcoList = factory.select(aclSql.toString(), list, new DAOCallback<OperateRemindBean>(){
					public OperateRemindBean wrapper(ResultSet rs, int idx){
						OperateRemindBean bean = new OperateRemindBean();
						try {
							bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
							bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
							bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
							bean.setVhclLic(CommonUtils.checkNull(rs.getString("VHCL_LIC")));
							bean.setVin(CommonUtils.checkNull(rs.getString("VIN")));
							bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
							bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
			}
			return tcoList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * function:得到待办信息-认证申请
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-10	
	 */
	public static List<OperateRemindBean> attestation(String actionName, AclUserBean user, String funcs) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			ArrayList list = new ArrayList();
			sql.append(" select tc1.CODE_DESC,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC,a.VIN,a.PROC_URL,a.TABLE_ID, a.DEPT_ID deptId,a.DLR_ID dlrId, a.SUBMITER submitert from TC_OPERATE_REMIND a, TC_CODE tc1 " );
			sql.append(" where a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
			sql.append(" and a.REMIND_TYPE = " + Constant.WAIT_TO_HANDLE);
//			sql.append(" and a.OPERATE_TYPE in (" + Constant.RPCH_AGRM_DISPOSITION + "," + Constant.CLIENT_LOVING +","+Constant.SALE_ORDER_DISPOSITION+")");
		    sql.append(" and a.OPERATE_TYPE = tc1.CODE_ID and a.DLR_ID = "+user.getCompanyId() );
		    sql.append(" and a.OPERATE_TYPE = " + Constant.OPERATE_AUTH_SUB);
//		    sql.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs);
		    logger.debug("认证申请---" + sql);
		    String aclSql="";
			try{
				 aclSql = DataAclUtil.getAclSql(sql.toString(), "submitert", "deptId", "dlrId", "/DMSUC/storageManager/carObligate/CarObligate/carObligateQuery.do", user, null, null);
			}catch(BizException e){
				logger.info("该用户没有认证申请权限");
			}
			
			List<OperateRemindBean> tcoList = null;
			if(aclSql!=null&&!aclSql.equals("")){
				tcoList = factory.select(aclSql.toString(), list, new DAOCallback<OperateRemindBean>(){
					public OperateRemindBean wrapper(ResultSet rs, int idx){
						OperateRemindBean bean = new OperateRemindBean();
						try {
							bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
							bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
							bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
							bean.setVhclLic(CommonUtils.checkNull(rs.getString("VHCL_LIC")));
							bean.setVin(CommonUtils.checkNull(rs.getString("VIN")));
							bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
							bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
			}
			return tcoList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * function:得到待办信息-认证申请
	 * author: wry
	 * @throws Exception
	 * date: 2009-09-10	
	 */
	public static List<OperateRemindBean> visitingDisposal(String actionName, AclUserBean user, String funcs) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			ArrayList list = new ArrayList();
			sql.append(" select tc1.CODE_DESC,a.OPERATE_REMIND_ID,to_char(a.PLANNED_DATE,'yyyy-mm-dd hh:mm') PLANNED_DATE,a.VHCL_LIC,a.VIN,a.PROC_URL,a.TABLE_ID, a.DEPT_ID deptId,a.DLR_ID dlrId, a.SUBMITER submitert from TC_OPERATE_REMIND a, TC_CODE tc1 " );
			sql.append(" where a.ACTION_STAT ="+ Constant.ACCEPT_RES_UNACTION );
			sql.append(" and a.REMIND_TYPE = " + Constant.WAIT_TO_HANDLE);
//			sql.append(" and a.OPERATE_TYPE in (" + Constant.RPCH_AGRM_DISPOSITION + "," + Constant.CLIENT_LOVING +","+Constant.SALE_ORDER_DISPOSITION+")");
		    sql.append(" and a.OPERATE_TYPE = tc1.CODE_ID and a.DLR_ID = "+user.getCompanyId() );
		    sql.append(" and a.OPERATE_TYPE = " + Constant.OPERATE_VISIT_DEAL);
//		    sql.append(" and substr(a.PROC_URL,0,instr(a.PROC_URL,'/',-1,1)-1) in "+funcs);
		    logger.debug("认证申请---" + sql);
		    String aclSql="";
			try{
				 aclSql = DataAclUtil.getAclSql(sql.toString(), "submitert", "deptId", "dlrId", "/DMSUC/storageManager/carObligate/CarObligate/carObligateQuery.do", user, null, null);
			}catch(BizException e){
				logger.info("该用户没有认证申请权限");
			}
			
			List<OperateRemindBean> tcoList = null;
			if(aclSql!=null&&!aclSql.equals("")){
				tcoList = factory.select(aclSql.toString(), list, new DAOCallback<OperateRemindBean>(){
					public OperateRemindBean wrapper(ResultSet rs, int idx){
						OperateRemindBean bean = new OperateRemindBean();
						try {
							bean.setOperateRemindId(CommonUtils.checkNull(rs.getString("OPERATE_REMIND_ID")));
							bean.setPlannedDatePre(rs.getString("PLANNED_DATE"));
							bean.setOperateType(CommonUtils.checkNull(rs.getString("CODE_DESC")));
							bean.setVhclLic(CommonUtils.checkNull(rs.getString("VHCL_LIC")));
							bean.setVin(CommonUtils.checkNull(rs.getString("VIN")));
							bean.setProcUrl(CommonUtils.checkNull(rs.getString("PROC_URL")));
							bean.setTableId(CommonUtils.checkNull(rs.getString("TABLE_ID")));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
			}
			return tcoList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
