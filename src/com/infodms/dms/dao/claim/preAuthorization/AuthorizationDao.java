package com.infodms.dms.dao.claim.preAuthorization;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class AuthorizationDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(AuthorizationDao.class);
	private static final AuthorizationDao dao = new AuthorizationDao();

	public static final AuthorizationDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	* @Title: authorizationApplyQuery 
	* @author: xyfue
	* @Description: 索赔单申请查询
	* @param @param request
	* @param @param logonUser
	* @param @param curPage
	* @param @param pageSize
	* @param @return
	* @param @throws Exception    设定文件 
	* @date 2014年12月1日 下午5:34:39 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	public PageResult<Map<String, Object>> authorizationApplyQuery(RequestWrapper request,AclUserBean logonUser, int curPage, int pageSize)throws Exception{
		String dealerIds = CommonUtils.checkNull(request.getParamValue("dealerIds")); // 经销商
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  *  FROM  Tt_As_Wr_Foreapproval T1 \n");
		sql.append(" WHERE 1 = 1 \n");
		DaoFactory.getsql(sql, "T1.DEALER_ID", CommonUtils.checkNull(logonUser.getDealerId()), 1);
		DaoFactory.getsql(sql, "T1.FO_NO", CommonUtils.checkNull(request.getParamValue("FO_NO")), 2);
		DaoFactory.getsql(sql, "T1.VIN", CommonUtils.checkNull(request.getParamValue("VIN")), 2);
		DaoFactory.getsql(sql, "T1.APPROVAL_TYPE", CommonUtils.checkNull(request.getParamValue("REPAIR_TYPE")), 1);
		DaoFactory.getsql(sql, "T1.REPORT_STATUS", CommonUtils.checkNull(request.getParamValue("REPORT_STATUS")), 1);
		DaoFactory.getsql(sql, "T1.IS_WARING", CommonUtils.checkNull(request.getParamValue("IS_WARNING")), 1);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("RO_CREATE_DATE")), 3);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("DELIVERY_DATE")), 4);
		
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 
	* @Title: authorizationAuditQuery 
	* @author: xyfue
	* @Description: 索赔单申请审核 
	* @param @param request
	* @param @param logonUser
	* @param @param curPage
	* @param @param pageSize
	* @param @return
	* @param @throws Exception    设定文件 
	* @date 2014年12月1日 下午5:35:02 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	public PageResult<Map<String, Object>> authorizationAuditQuery(RequestWrapper request,AclUserBean logonUser, int curPage, int pageSize)throws Exception{
		String dealerIds = CommonUtils.checkNull(request.getParamValue("dealerIds")); // 经销商
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  *  FROM  Tt_As_Wr_Foreapproval T1 \n");
		sql.append(" WHERE 1 = 1 \n");
		DaoFactory.getsql(sql, "T1.FO_NO", CommonUtils.checkNull(request.getParamValue("FO_NO")), 2);
		DaoFactory.getsql(sql, "T1.VIN", CommonUtils.checkNull(request.getParamValue("VIN")), 2);
		DaoFactory.getsql(sql, "T1.APPROVAL_TYPE", CommonUtils.checkNull(request.getParamValue("REPAIR_TYPE")), 1);
		DaoFactory.getsql(sql, "T1.REPORT_STATUS", CommonUtils.checkNull(request.getParamValue("REPORT_STATUS")), 1);
		DaoFactory.getsql(sql, "T1.IS_WARING", CommonUtils.checkNull(request.getParamValue("IS_WARNING")), 1);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("RO_CREATE_DATE")), 3);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("DELIVERY_DATE")), 4);
		
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 
	* @Title: authorizationQuery 
	* @author: xyfue
	* @Description: 索赔单预授权查询
	* @param @param request
	* @param @param logonUser
	* @param @param curPage
	* @param @param pageSize
	* @param @return
	* @param @throws Exception    设定文件 
	* @date 2014年12月1日 下午5:35:02 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	public PageResult<Map<String, Object>> authorizationQuery(RequestWrapper request,AclUserBean logonUser, int curPage, int pageSize)throws Exception{
		String dealerIds = CommonUtils.checkNull(request.getParamValue("dealerIds")); // 经销商
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  *  FROM  Tt_As_Wr_Foreapproval T1 \n");
		sql.append(" WHERE 1 = 1 \n");
		DaoFactory.getsql(sql, "T1.FO_NO", CommonUtils.checkNull(request.getParamValue("FO_NO")), 2);
		DaoFactory.getsql(sql, "T1.VIN", CommonUtils.checkNull(request.getParamValue("VIN")), 2);
		DaoFactory.getsql(sql, "T1.APPROVAL_TYPE", CommonUtils.checkNull(request.getParamValue("REPAIR_TYPE")), 1);
		DaoFactory.getsql(sql, "T1.REPORT_STATUS", CommonUtils.checkNull(request.getParamValue("REPORT_STATUS")), 1);
		DaoFactory.getsql(sql, "T1.IS_WARING", CommonUtils.checkNull(request.getParamValue("IS_WARNING")), 1);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("RO_CREATE_DATE")), 3);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("DELIVERY_DATE")), 4);
		
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> authorizationQueryCx(RequestWrapper request,AclUserBean logonUser, int curPage, int pageSize)throws Exception{
		String dealerIds = CommonUtils.checkNull(request.getParamValue("dealerIds")); // 经销商
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  *  FROM  Tt_As_Wr_Foreapproval T1 \n");
		sql.append(" WHERE 1 = 1 and t1.report_status = 11561002 \n");
		DaoFactory.getsql(sql, "T1.FO_NO", CommonUtils.checkNull(request.getParamValue("FO_NO")), 2);
		DaoFactory.getsql(sql, "T1.VIN", CommonUtils.checkNull(request.getParamValue("VIN")), 2);
		DaoFactory.getsql(sql, "T1.APPROVAL_TYPE", CommonUtils.checkNull(request.getParamValue("REPAIR_TYPE")), 1);
	   // DaoFactory.getsql(sql, "T1.REPORT_STATUS", CommonUtils.checkNull(request.getParamValue("REPORT_STATUS")), 1);
		DaoFactory.getsql(sql, "T1.IS_WARING", CommonUtils.checkNull(request.getParamValue("IS_WARNING")), 1);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("RO_CREATE_DATE")), 3);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("DELIVERY_DATE")), 4);
		
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 
	* @Title: authorizationQuery 
	* @author: xyfue
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param request
	* @param @param logonUser
	* @param @param curPage
	* @param @param pageSize
	* @param @return
	* @param @throws Exception    设定文件 
	* @date 2014年12月2日 下午11:58:48 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	public PageResult<Map<String, Object>> authorizationDlrQuery(RequestWrapper request,AclUserBean logonUser, int curPage, int pageSize)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  *  FROM  Tt_As_Wr_Foreapproval T1 \n");
		sql.append(" WHERE 1 = 1 \n");
		DaoFactory.getsql(sql, "T1.DEALER_ID",  CommonUtils.checkNull(logonUser.getDealerId()), 1);
		DaoFactory.getsql(sql, "T1.FO_NO", CommonUtils.checkNull(request.getParamValue("FO_NO")), 2);
		DaoFactory.getsql(sql, "T1.VIN", CommonUtils.checkNull(request.getParamValue("VIN")), 2);
		DaoFactory.getsql(sql, "T1.APPROVAL_TYPE", CommonUtils.checkNull(request.getParamValue("REPAIR_TYPE")), 1);
		DaoFactory.getsql(sql, "T1.REPORT_STATUS", CommonUtils.checkNull(request.getParamValue("REPORT_STATUS")), 1);
		DaoFactory.getsql(sql, "T1.IS_WARING", CommonUtils.checkNull(request.getParamValue("IS_WARNING")), 1);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("RO_CREATE_DATE")), 3);
		DaoFactory.getsql(sql, "T1.APPROVAL_DATE", CommonUtils.checkNull(request.getParamValue("DELIVERY_DATE")), 4);
		
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 取到三包等级和是否预警
	 * @param vin
	 * @return
	 */
	public List<Map<String, Object>> getVrLevel(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT (select c.code_desc from tc_code c where c.code_id=t.vr_level) as vr_level from  TT_AS_WR_VIN_RULE t where t.VR_WARRANTY <=\n" );
		sb.append("(select d.cur_days from   Tt_As_Wr_Vin_Repair_Days d where d.vin='"+CommonUtils.checkNull(request.getParamValue("vin"))+"' )\n" );
		sb.append("and t.VR_TYPE =94021001 order by t.VR_WARRANTY desc");
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, this.getFunName());
		return list;
	}
	
	/**
	 * 
	* @Title: isPdi 
	* @author: xyfue
	* @Description: 该VIN是否做过Pdi 
	* @param @param request
	* @param @return    设定文件 
	* @date 2014年11月28日 下午6:59:27 
	* @return List<Map<String,Object>>    返回类型 
	* @throws
	 */
	public boolean isPdi(RequestWrapper request) {
		boolean isPdi = false;
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT  T1.STATUS \n");
		sql.append("     FROM  Tt_As_Wr_Application  T1 \n");
		sql.append("   WHERE  T1.CLAIM_TYPE  =  10661011 \n");
		sql.append("       AND  T1.STATUS  !=  10791001 \n");
		sql.append("       AND  T1.VIN  =  '"+CommonUtils.checkNull(request.getParamValue("vin"))+"' \n");
		
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(null != list && list.size() > 0){
			isPdi = true;
		}
		return isPdi;
	}
	
	public PageResult<Map<String, Object>> authoriChooseOrdQuery(RequestWrapper request,AclUserBean logonUser, int curPage, int pageSize)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  T1.ID,TO_CHAR(T1.RO_CREATE_DATE,'YYYY-MM-DD') RO_CREATE_DATE,T1.RO_NO,T3.PART_NO,T3.PART_NAME,T3.PART_ID,TARR.REAL_PART_ID,T3.IS_RETURN \n");
		sql.append("     FROM  Tt_As_Repair_Order  T1 \n");
		sql.append("     LEFT JOIN TT_AS_RO_REPAIR_PART TARR ON T1.ID = TARR.RO_ID \n");
		sql.append("     LEFT JOIN TM_PT_PART_BASE T3 ON  TARR.REAL_PART_ID = T3.PART_ID \n");
		sql.append("   WHERE  T1.RO_STATUS  =  11591002 \n");
		DaoFactory.getsql(sql, "T1.RO_NO", CommonUtils.checkNull(request.getParamValue("RO_NO")), 2);
		DaoFactory.getsql(sql, "T3.PART_NO", CommonUtils.checkNull(request.getParamValue("PART_NO")), 2);
		DaoFactory.getsql(sql, "T3.PART_NAME", CommonUtils.checkNull(request.getParamValue("PART_NAME")), 2);
		DaoFactory.getsql(sql, "T3.IS_RETURN", CommonUtils.checkNull(request.getParamValue("IS_RETURN")), 1);
		DaoFactory.getsql(sql, "T1.VIN", CommonUtils.checkNull(request.getParamValue("vin")), 1);
		DaoFactory.getsql(sql, "T1.REPAIR_TYPE_CODE", CommonUtils.checkNull(request.getParamValue("repairType")), 1);
		DaoFactory.getsql(sql, "T1.DEALER_ID", CommonUtils.checkNull(logonUser.getDealerId()), 1);
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 插入索赔单预授权申请表
	* @Title: savePreAuthoriza 
	* @author: xyfue
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param request
	* @param @param logonUser    设定文件 
	* @date 2014年11月29日 下午5:24:01 
	* @return void    返回类型 
	* @throws
	 */
	public void savePreAuthoriza(RequestWrapper request ,AclUserBean logonUser) {
		Long pkId = DaoFactory.getPkId();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT  INTO  Tt_As_Wr_Foreapproval \n");
		sql.append("               (ID  ,  DEALER_ID  ,  RO_NO  ,  START_TIME, \n");
		sql.append("               VIN  ,  IN_MILEAGE  ,  APPROVAL_DATE  ,  DEST_CLERK, \n");
		sql.append("               CREATE_DATE  ,  CREATE_BY  ,  UPDATE_DATE  ,  UPDATE_BY, FO_NO, \n");
		sql.append("               REPORT_STATUS  ,  \n");
		sql.append("               APPROVAL_TYPE  ,  OUT_APPLY_AMOUNT  , OUT_AUDIT_AMOUNT  ,  OUT_APPLY_REMARK  , \n");
		sql.append("               BALANCE_YIELDLY  ,  APPLY_AMOUNT  ,AUDIT_AMOUNT  ,  APPLY_REMARK  ,  MAIN_PART_ID, \n");
		sql.append("               MAKER_CODE  ,  MAX_AMOUNT  ,  IS_RETURN  ,  ERROR_DESC  ,ERROR_REASON ,REMARK,IS_WARING,VR_LEVEL) \n");
		sql.append("               VALUES \n");
		sql.append("               ("+pkId+","+logonUser.getDealerId()+",'"+DaoFactory.getParam(request, "RO_NO")+"', \n");
		if (!"".equals(DaoFactory.getParam(request, "RO_CREATE_DATE"))) {
			sql.append("   TO_DATE('"+DaoFactory.getParam(request, "RO_CREATE_DATE")+"','YYYY-MM-DD' )  , \n");
		}else{
			sql.append("   null , \n");
		}
		String MILEAGE = "".equals(DaoFactory.getParam(request, "MILEAGE"))?"0.00":DaoFactory.getParam(request, "MILEAGE");
		sql.append("               '"+DaoFactory.getParam(request, "vin")+"'  ,  "+MILEAGE+"  , SYSDATE ,  '"+logonUser.getDealerCode()+"'  , \n");
		sql.append("               SYSDATE  ,  "+logonUser.getUserId()+"  ,  SYSDATE  ,  "+logonUser.getUserId()+"  ,'"+SequenceManager.getSequence2("YSQ")+"', \n");
		String saveOrComit = "1".equals(DaoFactory.getParam(request, "SAVEORCOMIT"))?"11561004":"11561001";
		sql.append("               "+saveOrComit+" , \n");
		sql.append("               "+DaoFactory.getParam(request, "REPAIR_TYPE")+"  ,   \n");
		
		String isWaring = "";
		if("".equals(DaoFactory.getParam(request, "vrLevel"))){
			isWaring = "10041002";
		}else {
			isWaring = "10041001";
		}
		String OUT_APPLY_AMOUNT = "".equals(DaoFactory.getParam(request, "OUT_APPLY_AMOUNT"))?"0.00":DaoFactory.getParam(request, "OUT_APPLY_AMOUNT");
		String OUT_AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT");
		String APPLY_AMOUNT = "".equals(DaoFactory.getParam(request, "APPLY_AMOUNT"))?"0.00":DaoFactory.getParam(request, "APPLY_AMOUNT");
		String AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "AUDIT_AMOUNT");
		String MAX_AMOUNT = "".equals(DaoFactory.getParam(request, "MAX_AMOUNT"))?"0.00":DaoFactory.getParam(request, "MAX_AMOUNT");
		sql.append("               "+OUT_APPLY_AMOUNT+"  ,  "+OUT_AUDIT_AMOUNT+"  ,'"+DaoFactory.getParam(request, "OUT_APPLY_REMARK")+"'  ,  \n");
		sql.append("               95411001  ,  "+APPLY_AMOUNT+"  ,  "+AUDIT_AMOUNT+"  ,'"+DaoFactory.getParam(request, "APPLY_REMARK")+"'  ,  "+DaoFactory.getParam(request, "PART_ID")+"  , \n");
		sql.append("               '"+DaoFactory.getParam(request, "MAKER_CODE")+"'  ,  "+MAX_AMOUNT+"  ,  "+DaoFactory.getParam(request, "IS_RETURN")+"  ,'"+DaoFactory.getParam(request, "ERROR_DESC")+"'  ,'"+DaoFactory.getParam(request, "ERROR_REASON")+"','"+DaoFactory.getParam(request, "ERROR_RESULT")+"', \n");
		sql.append("               "+isWaring+" ,'"+DaoFactory.getParam(request, "vrLevel")+"'   ) \n");
		
		dao.insert(sql.toString());
		
		String[] fjids=request.getParamValues("fjid");
		try {
			delAndReinsetFile(logonUser, fjids, pkId.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* @Title: updatePreAuthoriza 
	* @author: xyfue
	* @Description: 更新索赔单预授权信息
	* @param @param request
	* @param @param logonUser    设定文件 
	* @date 2014年12月1日 下午3:27:35 
	* @return void    返回类型 
	* @throws
	 */
	public void updatePreAuthoriza(RequestWrapper request ,AclUserBean logonUser) {
		String saveOrComit = DaoFactory.getParam(request, "SAVEORCOMIT");
		String isWaring = "";
		if("".equals(DaoFactory.getParam(request, "vrLevel"))){
			isWaring = "10041002";
		}else {
			isWaring = "10041001";
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  Tt_As_Wr_Foreapproval T1 SET \n");
		sql.append("               T1.RO_NO = '"+DaoFactory.getParam(request, "RO_NO")+"', \n");
		sql.append("               T1.IS_WARING = "+isWaring+", \n");
		sql.append("               T1.VR_LEVEL = '"+DaoFactory.getParam(request, "vrLevel")+"', \n");
		if (!"".equals(DaoFactory.getParam(request, "RO_CREATE_DATE"))) {
			sql.append("   T1.START_TIME = TO_DATE('"+DaoFactory.getParam(request, "RO_CREATE_DATE")+"','YYYY-MM-DD' )  , \n");
		}else{
			sql.append("   T1.START_TIME = null , \n");
		}
		sql.append("               T1.VIN = '"+DaoFactory.getParam(request, "vin")+"' , \n");
		String MILEAGE = "".equals(DaoFactory.getParam(request, "MILEAGE"))?"0.00":DaoFactory.getParam(request, "MILEAGE");
		sql.append("              T1. IN_MILEAGE = "+MILEAGE+" , \n");
		
		if ("2".equals(saveOrComit) || "4".equals(saveOrComit)) {
			sql.append("              T1.REPORT_STATUS = "+Constant.RO_FORE_01+", \n");
		}
		
		sql.append("              T1.UPDATE_DATE = SYSDATE, \n");
		sql.append("              T1.UPDATE_BY = "+logonUser.getUserId()+", \n");
		sql.append("              T1.APPROVAL_TYPE = "+DaoFactory.getParam(request, "REPAIR_TYPE")+" , \n");
		
		String OUT_APPLY_AMOUNT = "".equals(DaoFactory.getParam(request, "OUT_APPLY_AMOUNT"))?"0.00":DaoFactory.getParam(request, "OUT_APPLY_AMOUNT");
		String OUT_AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT");
		String APPLY_AMOUNT = "".equals(DaoFactory.getParam(request, "APPLY_AMOUNT"))?"0.00":DaoFactory.getParam(request, "APPLY_AMOUNT");
		String AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "AUDIT_AMOUNT");
		String MAX_AMOUNT = "".equals(DaoFactory.getParam(request, "MAX_AMOUNT"))?"0.00":DaoFactory.getParam(request, "MAX_AMOUNT");
		
		sql.append("               T1.OUT_APPLY_AMOUNT = "+OUT_APPLY_AMOUNT+" , \n");
		sql.append("               T1.OUT_AUDIT_AMOUNT = "+OUT_AUDIT_AMOUNT+" , \n");
		sql.append("               T1.OUT_APPLY_REMARK = '"+DaoFactory.getParam(request, "OUT_APPLY_REMARK")+"' , \n");
		sql.append("               T1.APPLY_AMOUNT = "+APPLY_AMOUNT+" , \n");
		sql.append("               T1.AUDIT_AMOUNT = "+AUDIT_AMOUNT+" , \n");
		sql.append("               T1.APPLY_REMARK = '"+DaoFactory.getParam(request, "APPLY_REMARK")+"' , \n");
		String partId = DaoFactory.getParam(request, "PART_ID");
		if("".equals(partId)){
			sql.append("           T1.MAIN_PART_ID = null , \n");
		}else {
			sql.append("           T1.MAIN_PART_ID =  "+partId+" , \n");
		}
		
		sql.append("              T1.MAKER_CODE = '"+DaoFactory.getParam(request, "MAKER_CODE")+"' , \n");
		sql.append("              T1.MAX_AMOUNT =  "+MAX_AMOUNT+" , \n");
		sql.append("              T1.IS_RETURN = "+DaoFactory.getParam(request, "IS_RETURN")+" , \n");
		sql.append("              T1.ERROR_DESC = '"+DaoFactory.getParam(request, "ERROR_DESC")+"' , \n");
		sql.append("              T1.ERROR_REASON = '"+DaoFactory.getParam(request, "ERROR_REASON")+"' , \n");
		sql.append("              T1.REMARK = '"+DaoFactory.getParam(request, "ERROR_RESULT")+"' \n");
		sql.append("              WHERE 1 = 1  \n");
		sql.append("              AND T1.ID = "+DaoFactory.getParam(request, "foId")+"  \n");
		
		dao.update(sql.toString(),null);
		
		String[] fjids=request.getParamValues("fjid");
		try {
			delAndReinsetFile(logonUser, fjids, DaoFactory.getParam(request, "foId"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void delAndReinsetFile(AclUserBean loginUser, String[] fjids,
			String pkId) throws SQLException {
		FileUploadManager.delAllFilesUploadByBusiness(pkId, fjids);
		FileUploadManager.fileUploadByBusiness(pkId, fjids, loginUser);
	}

	/**
	 * 
	* @Title: authorizationModifyQuery 
	* @author: xyfue
	* @Description: 获取索赔单预授权信息 
	* @param @param request
	* @param @param logonUser
	* @param @return
	* @param @throws Exception    设定文件 
	* @date 2014年12月1日 下午3:11:42 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public Map<String, Object> authorizationModifyQuery(RequestWrapper request,AclUserBean logonUser)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  *  FROM  Tt_As_Wr_Foreapproval T1 \n");
		sql.append(" WHERE 1 = 1 \n");
		DaoFactory.getsql(sql, "T1.FO_NO", CommonUtils.checkNull(request.getParamValue("FO_NO")), 1);
		DaoFactory.getsql(sql, "T1.VIN", CommonUtils.checkNull(request.getParamValue("vin")), 2);
		DaoFactory.getsql(sql, "T1.APPROVAL_TYPE", CommonUtils.checkNull(request.getParamValue("REPAIR_TYPE")), 1);
		DaoFactory.getsql(sql, "T1.REPORT_STATUS", CommonUtils.checkNull(request.getParamValue("REPORT_STATUS")), 1);
		DaoFactory.getsql(sql, "T1.ID", CommonUtils.checkNull(request.getParamValue("foId")), 1);
		List<Map<String, Object>> maps= dao.pageQuery(sql.toString(), null, getFunName());
		Map<String, Object> map = null ;
		if(null != maps && maps.size() > 0 ){
			map = maps.get(0);
		}
		return map;
	}
	
	/**
	 * 
	* @Title: getPartInfo 
	* @author: xyfue
	* @Description: 获取配件信息 
	* @param @param request
	* @param @param logonUser
	* @param @param params
	* @param @return
	* @param @throws Exception    设定文件 
	* @date 2014年12月1日 下午3:17:24 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public Map<String, Object> getPartInfo(RequestWrapper request,AclUserBean logonUser,Map<String, Object> params)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM TM_PT_PART_BASE T1 \n");
		sql.append(" WHERE 1 = 1 \n");
		DaoFactory.getsql(sql, "T1.PART_ID", CommonUtils.checkNull(params.get("MAIN_PART_ID")), 1);
		List<Map<String, Object>> maps= dao.pageQuery(sql.toString(), null, getFunName());
		Map<String, Object> map = null ;
		if(null != maps && maps.size() > 0 ){
			map = maps.get(0);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> showInfoByVin(String vin) {
		StringBuffer sb= new StringBuffer();
		sb.append("select v.*,a.invoice_date,a.sales_date,\n" );
		sb.append("(select i.wrgroup_id\n" );
		sb.append("          from tt_as_wr_MODEL_ITEM i\n" );
		sb.append("         where i.model_id = v.PACKAGE_ID and i.wrgroup_id in\n" );
		sb.append("               (select WRGROUP_ID\n" );
		sb.append("                  from tt_as_wr_model_group\n" );
		sb.append("                 where wrgroup_type = 10451001)) as wrgroup_id,");
		sb.append("       to_char(vi.create_date, 'yyyy-mm-dd hh24:mi') in_store_date,\n" );
		sb.append("       c.ctm_name as customer_name,\n" );
		sb.append("       wu.rule_code,\n" );
		sb.append("       a.order_id,\n" );
		sb.append("       c.main_phone,\n" );
		sb.append("       a.car_charactor car_use_type,\n" );
		sb.append("       tc.code_desc car_use_desc,\n" );
		sb.append("       c. ctm_name,\n" );
		sb.append("       c.other_phone,\n" );
		sb.append("       c.address,\n" );
		sb.append("       vw.brand_name as brand_name,\n" );
		sb.append("       vw.brand_code as brand_code,\n" );
		sb.append("       vw.series_name as series_name,\n" );
		sb.append("       vw.series_code as series_code,\n" );
		sb.append("       vw.model_name as model_name,\n" );
		sb.append("       vw.model_code as model_code,\n" );
		sb.append("       a.consignation_date as purchased_date_act,\n" );
		sb.append("       vw.package_name,\n" );
		sb.append("       ba.area_name yieldly_name\n" );
		sb.append("  from tm_vehicle v\n" );
		sb.append("  left outer join vw_material_group_service vw\n" );
		sb.append("    on vw.package_id = v.package_id\n" );
		sb.append("  left outer join tt_dealer_actual_sales a\n" );
		sb.append("    on v.vehicle_id = a.vehicle_id\n" );
		sb.append("   and a.is_return = 10041002\n" );
		sb.append("  left outer join tt_customer c\n" );
		sb.append("    on a.ctm_id = c.ctm_id\n" );
		sb.append("  left join tm_business_area ba\n" );
		sb.append("    on ba.area_id = v.yieldly\n" );
		sb.append("  left join tc_code tc\n" );
		sb.append("    on tc.code_id = a.car_charactor\n" );
		sb.append("  left join tt_as_wr_game wg\n" );
		sb.append("    on wg.id = v.claim_tactics_id\n" );
		sb.append("  left join tt_as_wr_rule wu\n" );
		sb.append("    on wu.id = wg.rule_id\n" );
		sb.append("  left join (select max(vi.create_date) create_date, vi.vehicle_id\n" );
		sb.append("               from tt_vs_inspection vi\n" );
		sb.append("              group by vi.vehicle_id) vi\n" );
		sb.append("    on vi.vehicle_id = v.vehicle_id\n" );
		sb.append("   and v.life_cycle in (10321003, 10321004, 10321007)\n" );
		sb.append(" where 1 = 1\n" );
		DaoFactory.getsql(sb, "v.vin", vin, 1);
		Map<String, Object> ps = pageQueryMap(sb.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 
	* @Title: queryAttById 
	* @author: xyfue
	* @Description: 获取文件
	* @param @param ywzj
	* @param @return    设定文件 
	* @date 2014年12月1日 下午5:21:15 
	* @return List<FsFileuploadPO>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<FsFileuploadPO> queryAttById(String ywzj) {
		StringBuffer sql = new StringBuffer();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A WHERE 1=1");
		DaoFactory.getsql(sql, "A.YWZJ", ywzj, 1);
		ls = select(FsFileuploadPO.class, sql.toString(), null);
		return ls;
	}
	
	/**
	 * 
	* @Title: auditPreAuthoriza 
	* @author: xyfue
	* @Description: 审核索赔单预授权
	* @param @param request
	* @param @param logonUser    设定文件 
	* @date 2014年12月1日 下午6:54:54 
	* @return void    返回类型 
	* @throws
	 */
	public void auditPreAuthoriza(RequestWrapper request ,AclUserBean logonUser) {
		//更新索赔单预授权记录为已审核
		String AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "AUDIT_AMOUNT");
		String OUT_AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  Tt_As_Wr_Foreapproval T1 SET \n");
		sql.append("               T1.REPORT_STATUS = "+Constant.RO_FORE_02+", \n");
		sql.append(" 			   T1.AUDIT_PERSON = "+logonUser.getUserId()+", ");
		sql.append(" 			   T1.AUDIT_AMOUNT = "+AUDIT_AMOUNT+", ");
		sql.append(" 			   T1.OUT_AUDIT_AMOUNT = "+OUT_AUDIT_AMOUNT+", ");
		sql.append(" 			   T1.UPDATE_DATE = SYSDATE, ");
		sql.append(" 			   T1.OPINION = '"+DaoFactory.getParam(request, "OPINION")+"', ");
		sql.append(" 			   T1.UPDATE_BY = "+logonUser.getUserId()+" ");
		sql.append("              WHERE 1 = 1  \n");
		sql.append("              AND T1.ID = "+DaoFactory.getParam(request, "foId")+"  \n");
		dao.update(sql.toString(),null);
		
		//插入审核记录表
		sql.delete(0, sql.length());
		sql.append(" INSERT  INTO  TT_AS_WR_FOREAUTHDETAIL( \n");
		sql.append("               ID,FID,AUDIT_PERSON,AUDIT_RESULT, \n");
		sql.append("               AUDIT_DATE,REMARK,CREATE_BY,CREATE_DATE) \n");
		sql.append("               VALUES( \n");
		sql.append("               F_GETID,"+DaoFactory.getParam(request, "foId")+",'"+logonUser.getName()+"',"+Constant.RO_FORE_02+", \n");
		sql.append("               SYSDATE,'"+DaoFactory.getParam(request, "OPINION")+"', "+logonUser.getUserId()+"  ,SYSDATE \n");
		sql.append("               ) \n");
		dao.insert(sql.toString());
		
//		String MILEAGE = "".equals(DaoFactory.getParam(request, "MILEAGE"))?"0.00":DaoFactory.getParam(request, "MILEAGE");
//		//更新里程数
//		sql.delete(0, sql.length());
//		sql.append(" UPDATE  TM_VEHICLE TT SET \n");
//		sql.append("    TT.UPDATE_BY = "+logonUser.getUserId()+", \n");
//		sql.append("    TT.UPDATE_DATE = SYSDATE, \n");
//		sql.append("    TT.MILEAGE = "+MILEAGE+" \n");
//		sql.append("   WHERE TT.VIN = '"+DaoFactory.getParam(request, "vin")+"' \n");
//		dao.update(sql.toString(), null);
		
	}
	
	/**
	 * 
	* @Title: rejectPreAuthoriza 
	* @author: xyfue
	* @Description: 索赔单预授权拒绝
	* @param @param request
	* @param @param logonUser    设定文件 
	* @date 2014年12月1日 下午6:55:51 
	* @return void    返回类型 
	* @throws
	 */
	public void rejectPreAuthoriza(RequestWrapper request ,AclUserBean logonUser) {
		//更新索赔单预授权记录为已审核
		String AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "AUDIT_AMOUNT");
		String OUT_AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  Tt_As_Wr_Foreapproval T1 SET \n");
		sql.append("               T1.REPORT_STATUS = "+Constant.RO_FORE_06+", \n");
		sql.append(" 			   T1.UPDATE_DATE = SYSDATE, ");
		sql.append(" 			   T1.AUDIT_AMOUNT = "+AUDIT_AMOUNT+", ");
		sql.append(" 			   T1.OUT_AUDIT_AMOUNT = "+OUT_AUDIT_AMOUNT+", ");
		sql.append(" 			   T1.OPINION = '"+DaoFactory.getParam(request, "OPINION")+"', ");
		sql.append(" 			   T1.UPDATE_BY = "+logonUser.getUserId()+" ");
		sql.append("              WHERE 1 = 1  \n");
		sql.append("              AND T1.ID = "+DaoFactory.getParam(request, "foId")+"  \n");
		dao.update(sql.toString(),null);
		
	}
	
	/**
	 * 
	* @Title: backPreAuthoriza 
	* @author: xyfue
	* @Description: 索赔单预授权退回
	* @param @param request
	* @param @param logonUser    设定文件 
	* @date 2014年12月1日 下午6:55:51 
	* @return void    返回类型 
	* @throws
	 */
	public void backPreAuthoriza(RequestWrapper request ,AclUserBean logonUser) {
		//更新索赔单预授权记录为已审核
		String AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "AUDIT_AMOUNT");
		String OUT_AUDIT_AMOUNT = "".equals(DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT"))?"0.00":DaoFactory.getParam(request, "OUT_AUDIT_AMOUNT");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  Tt_As_Wr_Foreapproval T1 SET \n");
		sql.append("               T1.REPORT_STATUS = "+Constant.RO_FORE_03+", \n");
		sql.append(" 			   T1.UPDATE_DATE = SYSDATE, ");
		sql.append(" 			   T1.AUDIT_AMOUNT = "+AUDIT_AMOUNT+", ");
		sql.append(" 			   T1.OUT_AUDIT_AMOUNT = "+OUT_AUDIT_AMOUNT+", ");
		sql.append(" 			   T1.OPINION = '"+DaoFactory.getParam(request, "OPINION")+"', ");
		sql.append(" 			   T1.UPDATE_BY = "+logonUser.getUserId()+" ");
		sql.append("              WHERE 1 = 1  \n");
		sql.append("              AND T1.ID = "+DaoFactory.getParam(request, "foId")+"  \n");
		dao.update(sql.toString(),null);
		
	}
	
	/**
	 * 
	* @Title: wastePreAuthoriza 
	* @author: xyfue
	* @Description: 索赔单预授权拒绝
	* @param @param request
	* @param @param logonUser    设定文件 
	* @date 2014年12月1日 下午6:55:51 
	* @return void    返回类型 
	* @throws
	 */
	public void wastePreAuthoriza(RequestWrapper request ,AclUserBean logonUser) {
		//更新索赔单预授权记录为已审核
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  Tt_As_Wr_Foreapproval T1 SET \n");
		sql.append("               T1.REPORT_STATUS = "+Constant.RO_FORE_05+", \n");
		sql.append(" 			   T1.UPDATE_DATE = SYSDATE, ");
		sql.append(" 			   T1.UPDATE_BY = "+logonUser.getUserId()+" ");
		sql.append("              WHERE 1 = 1  \n");
		sql.append("              AND T1.ID = "+DaoFactory.getParam(request, "foId")+"  \n");
		dao.update(sql.toString(),null);
		
	}
	/**
	 * 
	* @Title: getTcCodeList 
	* @author: xyfue
	* @Description: 获取tc_code数据 
	* @param @param request
	* @param @param logonUser
	* @param @param curPage
	* @param @param pageSize
	* @param @return
	* @param @throws Exception    设定文件 
	* @date 2014年12月4日 下午5:22:07 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	public PageResult<Map<String, Object>> getTcCodeList(RequestWrapper request,AclUserBean logonUser, int curPage, int pageSize)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  *  FROM  TC_CODE T1 \n");
		sql.append(" WHERE 1 = 1 AND T1.STATUS = 10011001\n");
		DaoFactory.getsql(sql, "T1.TYPE", CommonUtils.checkNull(request.getParamValue("CODE_TYPE")), 1);
		DaoFactory.getsql(sql, "T1.CODE_DESC", CommonUtils.checkNull(request.getParamValue("CODE_DESC")), 2);
		DaoFactory.getsql(sql, "T1.CODE_ID", CommonUtils.checkNull(request.getParamValue("CODE_ID")), 2);
		DaoFactory.getsql(sql, "T1.CODE_ID", CommonUtils.checkNull(request.getParamValue("NOT_CODE_ID")), 8);
		PageResult<Map<String, Object>> maps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return maps;
	}
}
