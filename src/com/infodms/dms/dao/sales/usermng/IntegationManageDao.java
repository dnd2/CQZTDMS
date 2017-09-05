package com.infodms.dms.dao.sales.usermng;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;



public class IntegationManageDao extends BaseDao<PO>{
	public Logger logger = Logger.getLogger(UserManageDao.class);
	private static final IntegationManageDao dao=new IntegationManageDao();
	
	public static IntegationManageDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 积分变动查询
	 * @param parm
	 * @param dutyType
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String,Object>> integChangeSelect(Map<String,Object> parm, String dutyType,
			int pageSize,int curPage)throws Exception {
		String name=CommonUtils.checkNull(parm.get("name"));
		String idNo=CommonUtils.checkNull(parm.get("idNo"));
		String status=CommonUtils.checkNull(parm.get("status"));
		String integType=CommonUtils.checkNull(parm.get("integType"));
		String orgId=CommonUtils.checkNull(parm.get("orgId"));
		String dealerId=CommonUtils.checkNull(parm.get("dealerId"));
		String positionStatus=CommonUtils.checkNull(parm.get("positionStatus"));
		String position=CommonUtils.checkNull(parm.get("position"));
		String bankCardNo=CommonUtils.checkNull(parm.get("bankCardNo"));
		String dealerCode=CommonUtils.checkNull(parm.get("dealerCode"));
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TD.DEALER_CODE,TD.DEALER_SHORTNAME, TVIC.DEALER_ID,TVIC.ID_NO,TVIC.NAME,TVIC.INTEG_BEFORE, TVIC.CHANGE_TYPE,\n" );
		sql.append(" TVIC.INTEG_AFTER,TVIC.INTEG_TYPE,TVIC.STATUS,TVIC.RELATION_ID,TVIC.CREATE_DATE,TVIC.THIS_CHANGE_INTEG,VT.DEALER_NAME \n");
		sql.append("  FROM TT_VS_INTEGRATION_CHANGE TVIC,vw_org_dealer VT,TM_DEALER TD,TT_VS_PERSON TVP\n" );
		sql.append(" WHERE TVIC.DEALER_ID = VT.DEALER_ID AND TD.DEALER_ID=TVIC.DEALER_ID AND TVIC.ID_NO=TVP.ID_NO\n ");

		//经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID ="+dealerId+"");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.ROOT_ORG_ID="+orgId+")");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.PQ_ORG_ID="+orgId+")");
			
		}
		//姓名检索
		if(name!=null&&!"".equals(name)){
			sql.append(" AND TVIC.NAME LIKE '%"+name+"%'");
		}
		//身份证
		if(idNo!=null&&!"".equals(idNo)){
			sql.append(" AND TVIC.ID_NO LIKE '%"+idNo+"%'");
		}
		//状态
		if(status!=null&&!"".equals(status)){
			sql.append(" AND TVIC.STATUS="+status+"");
		}
		//积分类型
		if(integType!=null&&!"".equals(integType)){
			sql.append(" AND TVIC.INTEG_TYPE="+integType+"");
		}
		//职位状态
		if(positionStatus!=null&&!"".equals(positionStatus)){
			sql.append(" AND TVP.POSITION_STATUS="+positionStatus+"");
		}
		//职位
		if(position!=null&&!"".equals(position)){
			sql.append(" AND TVP.POSITION="+position+"");
		}
		//银行卡号
		if(bankCardNo!=null&&!"".equals(bankCardNo)){
			sql.append(" AND TVP.BANK_CARDNO="+bankCardNo+"");
		}
		//经销商代码
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String dealerCodes=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append("  AND VT.DEALER_CODE IN("+dealerCodes+")");
		}
		sql.append(" ORDER BY TVIC.CREATE_DATE DESC");
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 积分综合评价查询
	 * @param parm
	 * @param dutyType
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> integTotalSelect(
			Map<String, Object> parm, String dutyType, int curPage, int pageSize)throws Exception {
		String name=CommonUtils.checkNull(parm.get("name"));
		String idNo=CommonUtils.checkNull(parm.get("idNo"));
		String status=CommonUtils.checkNull(parm.get("status"));
		String orgId=CommonUtils.checkNull(parm.get("orgId"));
		String dealerId=CommonUtils.checkNull(parm.get("dealerId"));
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TVIC.DEALER_ID,TVIC.ID_NO,TVIC.NAME,\n" );
		sql.append(" TVIC.GET_INTEG,TVIC.CREATE_DATE,TVIC.REMARK,TVIC.STATUS,VT.DEALER_NAME \n");
		sql.append("  FROM TT_VS_INTEGRATION_TOTAL TVIC,vw_org_dealer VT\n" );
		sql.append(" WHERE VT.DEALER_ID = TVIC.DEALER_ID\n");

		//经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID ="+dealerId+"");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.ROOT_ORG_ID="+orgId+")");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.PQ_ORG_ID="+orgId+")");
			
		}
		//姓名检索
		if(name!=null&&!"".equals(name)){
			sql.append(" AND TVIC.NAME LIKE '%"+name+"%'");
		}
		//身份证
		if(idNo!=null&&!"".equals(idNo)){
			sql.append(" AND TVIC.ID_NO LIKE'%"+idNo+"%'");
		}
		//状态
		if(status!=null&&!"".equals(status)){
			sql.append(" AND TVIC.STATUS="+status+"");
		}
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 人员月度积分查询
	 * @param parm
	 * @param dutyType
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> integMonthSelect(
			Map<String, Object> parm, String dutyType, int curPage, int pageSize)throws Exception {
		String name=CommonUtils.checkNull(parm.get("name"));
		String idNo=CommonUtils.checkNull(parm.get("idNo"));
		String status=CommonUtils.checkNull(parm.get("status"));
		String orgId=CommonUtils.checkNull(parm.get("orgId"));
		String dealerId=CommonUtils.checkNull(parm.get("dealerId"));
		String positionStatus=CommonUtils.checkNull(parm.get("positionStatus"));
		String month=CommonUtils.checkNull(parm.get("month"));
		String year=CommonUtils.checkNull(parm.get("year"));
		String position=CommonUtils.checkNull(parm.get("position"));
		String bankCardNo=CommonUtils.checkNull(parm.get("bankCardNo"));
		String dealerCode=CommonUtils.checkNull(parm.get("dealerCode"));
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TVIC.CREATE_DATE,TVIC.ID_NO,TVP.NAME,TVIC.MONTH,TD.DEALER_CODE,TD.DEALER_SHORTNAME,\n" );
		sql.append(" TVIC.MONTH_PERFORMANCE_INTEG,TVIC.MONTH_AUTHENTICATION_INTEG,TVIC.MONTH_YEAR_INTEG,");
		sql.append(" TVIC.MONTH_INTEG_TOATL,TVIC.MONTH_INTEG,TVIC.REMAIN_INTEG,TVIC.THREE_MONTH_ZERO,TVIC.POSITION_STATUS,TVIC.POSITION,");
		sql.append(" TVIC.STATUS,VT.DEALER_NAME,TVIC.YEAR");
		sql.append("  FROM TT_VS_INTEGRATION_MONTH TVIC,vw_org_dealer VT,TM_DEALER TD,TT_VS_PERSON TVP\n" );
		sql.append(" WHERE TVIC.DEALER_ID = VT.DEALER_ID AND TD.DEALER_ID=TVIC.DEALER_ID AND TVP.ID_NO=TVIC.ID_NO \n");

		//经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID ="+dealerId+"\n");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.PQ_ORG_ID="+orgId+")\n");
			
		}
		//姓名检索
		if(name!=null&&!"".equals(name)){
			sql.append(" AND TVIC.NAME LIKE '%"+name+"%'");
		}
		//身份证
		if(idNo!=null&&!"".equals(idNo)){
			sql.append(" AND TVIC.ID_NO LIKE'%"+idNo+"%'");
		}
		//状态
		if(status!=null&&!"".equals(status)){
			sql.append(" AND TVIC.STATUS="+status+"");
		}
		//职位状态
		if(positionStatus!=null&&!"".equals(positionStatus)){
			sql.append(" AND TVIC.POSITION_STATUS="+positionStatus+"");
		}
		//月份
		if(month!=null&&!"".equals(month)){
			sql.append(" AND TVIC.MONTH="+month+"");
		}
		//月份
		if(year!=null&&!"".equals(year)){
			sql.append(" AND TVIC.YEAR="+year+"");
		}
		//职位
		if(position!=null&&!"".equals(position)){
			sql.append(" AND TVIC.POSITION="+position+"");
		}
		//银行卡号
		if(bankCardNo!=null&&!"".equals(bankCardNo)){
			sql.append(" AND TVP.BANK_CARDNO="+bankCardNo+"");
		}
		//经销商代码
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String dealerCodes=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append("  AND VT.DEALER_CODE IN("+dealerCodes+")");
		}
		sql.append(" ORDER BY TVIC.CREATE_DATE DESC");
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 人员年度积分查询
	 * @param parm
	 * @param dutyType
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> integYearSelect (
			Map<String, Object> parm, String dutyType, int curPage, int pageSize)throws Exception {
		String name=CommonUtils.checkNull(parm.get("name"));
		String idNo=CommonUtils.checkNull(parm.get("idNo"));
		String status=CommonUtils.checkNull(parm.get("status"));
		String orgId=CommonUtils.checkNull(parm.get("orgId"));
		String dealerId=CommonUtils.checkNull(parm.get("dealerId"));
		String year=CommonUtils.checkNull(parm.get("year"));
		String positionStatus=CommonUtils.checkNull(parm.get("positionStatus"));
		String position=CommonUtils.checkNull(parm.get("position"));
		String bankCardNo=CommonUtils.checkNull(parm.get("bankCardNo"));
		String dealerCode=CommonUtils.checkNull(parm.get("dealerCode"));
		
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TD.DEALER_CODE,TD.DEALER_SHORTNAME,TVIC.CREATE_DATE,TVIC.ID_NO,TVIC.NAME,TVIC.YEAR,\n" );
		sql.append(" TVIC.YEAR_PERFORMANCE_INTEG,TVIC.YEAR_AUTHENTICATION_INTEG,TVIC.POSITION_YEAR_INTEG,\n");
		sql.append(" TVIC.YEAR_INTEG_MIXTURETOATL,TVIC.FIRST_AGAINST_PERFORMANCE,TVIC.THREE_MONTH_ZERO,\n");
		sql.append(" TVIC.SECOND_AGAINST_PERFORMANCE,TVIC.THIRD_AGAINST_PERFORMANCE,TVIC.FIRST_AGAINST_AUTHENTICATION,\n");
		sql.append(" TVIC.SECOND_AGAINST_AUTHENTICATION,TVIC.THIRD_AGAINST_AUTHENTICATION,TVIC.FIRST_AGAINST_YEAR,\n");
		sql.append(" TVIC.SECOND_AGAINST_YEAR,TVIC.YEAR_INTEG_TOTAL,\n");
		sql.append(" TVIC.THIRD_AGAINST_YEAR,VT.DEALER_NAME \n");
		sql.append("  FROM TT_VS_INTEGRATION_YEAR TVIC,vw_org_dealer VT,TT_VS_PERSON TVP,TM_DEALER TD\n" );
		sql.append(" WHERE TVIC.DEALER_ID = VT.DEALER_ID AND TVP.ID_NO=TVIC.ID_NO AND TVIC.DEALER_ID=TD.DEALER_ID\n");

		//经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID ="+dealerId+"");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.ROOT_ORG_ID="+orgId+")");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.PQ_ORG_ID="+orgId+")");
			
		}
		//姓名检索
		if(name!=null&&!"".equals(name)){
			sql.append(" AND TVIC.NAME LIKE '%"+name+"%'");
		}
		//身份证
		if(idNo!=null&&!"".equals(idNo)){
			sql.append(" AND TVIC.ID_NO Like '%"+idNo+"%'");
		}
		//状态
		if(status!=null&&!"".equals(status)){
			sql.append(" AND TVIC.STATUS="+status+"");
		}
		
		//年份
		if(year!=null&&!"".equals(year)){
			sql.append(" AND TVIC.YEAR="+year+"");
		}
		//职位状态
		if(positionStatus!=null&&!"".equals(positionStatus)){
			sql.append(" AND TVP.POSITION_STATUS="+positionStatus+"");
		}
		//职位
		if(position!=null&&!"".equals(position)){
			sql.append(" AND TVP.POSITION="+position+"");
		}
		//银行卡号
		if(bankCardNo!=null&&!"".equals(bankCardNo)){
			sql.append(" AND TVP.BANK_CARDNO="+bankCardNo+"");
		}
		//经销商代码
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String dealerCodes=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append("  AND VT.DEALER_CODE IN("+dealerCodes+")");
		}
		
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 人员积分兑现查询
	 * @param parm
	 * @param dutyType
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> integAgainstSelect (
			Map<String, Object> parm, String dutyType, int curPage, int pageSize)throws Exception {
		String name=CommonUtils.checkNull(parm.get("name"));
		String idNo=CommonUtils.checkNull(parm.get("idNo"));
		String status=CommonUtils.checkNull(parm.get("status"));
		String orgId=CommonUtils.checkNull(parm.get("orgId"));
		String dealerId=CommonUtils.checkNull(parm.get("dealerId"));
		String year=CommonUtils.checkNull(parm.get("year"));
		String positionStatus=CommonUtils.checkNull(parm.get("positionStatus"));
		String position=CommonUtils.checkNull(parm.get("position"));
		String bankCardNo=CommonUtils.checkNull(parm.get("bankCardNo"));
		String dealerCode=CommonUtils.checkNull(parm.get("dealerCode"));
		String isAgainst=CommonUtils.checkNull(parm.get("isAgainst"));
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT vt.DEALER_CODE,TVIC.CREATE_DATE,TVIC.ID_NO,TVIC.NAME,TVIC.BANK,TVIC.BANK_CARDNO,\n" );
		sql.append(" TVIC.THIRD_PERFORMANCE_AGAINST,TVIC.SENCOND_PERFORMANCE_AGAINST,DECODE(TVP.POSITION,99961004,0,TVIC.FIRST_PERFORMANCE_AGAINST) FIRST_PERFORMANCE_AGAINST, \n");
		sql.append(" TVIC.THIRD_AUTHENTICATION_AGAINST,TVIC.SECOND_AUTHENTICATION_AGAINST,TVIC.FIRST_AUTHENTICATION_AGAINST, \n");
		sql.append(" TVIC.THIRD_YEAR,TVIC.SENCOND_YEAR,TVIC.FIRST_YEAR,TVIC.THIS_AGAINST_INTEG, \n");
		sql.append(" TVIC.THIS_AGAINST_MONEY,TVIC.IS_AGAINST,TVIC.AGAINST_DATE, TVIC.INTEGRATION_AGAINST_ID,\n");
		sql.append(" VT.DEALER_NAME,TVIC.STATUS,TVIC.POSITION ,TVIC.CURRENT_YEARS YEAR \n");
		sql.append("  FROM TT_VS_INTEGRATION_AGAINST TVIC,vw_org_dealer_all VT,TT_VS_PERSON TVP \n" );
		sql.append(" WHERE vt.DEALER_ID=tvp.DEALER_ID AND TVP.ID_NO=TVIC.ID_NO\n");

		//经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID ="+dealerId+"");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.ROOT_ORG_ID="+orgId+")");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.PQ_ORG_ID="+orgId+")");
			
		}
		//姓名检索
		if(name!=null&&!"".equals(name)){
			sql.append(" AND TVIC.NAME LIKE '%"+name+"%'");
		}
		//身份证
		if(idNo!=null&&!"".equals(idNo)){
			sql.append(" AND TVIC.ID_NO like '%"+idNo+"%'");
		}
		//状态
		if(status!=null&&!"".equals(status)){
			sql.append(" AND TVIC.STATUS="+status+"");
		}
		//是否兑现
		if(isAgainst!=null&&!"".equals(isAgainst)){
			sql.append(" AND TVIC.IS_AGAINST="+isAgainst+"");
		}
		//月份
		if(year!=null&&!"".equals(year)){
			 int years=Integer.parseInt(year)-1;
			sql.append(" AND TVIC.CURRENT_YEARS="+years+"");
		}
		//职位状态
		if(positionStatus!=null&&!"".equals(positionStatus)){
			sql.append(" AND TVP.POSITION_STATUS="+positionStatus+"");
		}
		//职位
		if(position!=null&&!"".equals(position)){
			sql.append(" AND TVP.POSITION="+position+"");
		}
		//银行卡号
		if(bankCardNo!=null&&!"".equals(bankCardNo)){
			sql.append(" AND TVP.BANK_CARDNO="+bankCardNo+"");
		}
		//经销商代码
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String dealerCodes=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append("  AND VT.DEALER_CODE IN("+dealerCodes+")");
		}
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	
	public List<Map<String, Object>> getPersonIntegAgaist(Map<String, String> parm){
			String name=CommonUtils.checkNull(parm.get("name"));
			String idNo=CommonUtils.checkNull(parm.get("idNo"));
			String status=CommonUtils.checkNull(parm.get("status"));
			String orgId=CommonUtils.checkNull(parm.get("orgId"));
			String dealerId=CommonUtils.checkNull(parm.get("dealerId"));
			String year=CommonUtils.checkNull(parm.get("year"));
			String positionStatus=CommonUtils.checkNull(parm.get("positionStatus"));
			String position=CommonUtils.checkNull(parm.get("position"));
			String bankCardNo=CommonUtils.checkNull(parm.get("bankCardNo"));
			String dealerCode=CommonUtils.checkNull(parm.get("dealerCode"));
			String dutyType=CommonUtils.checkNull(parm.get("dutyType"));
			StringBuilder sql= new StringBuilder();
			sql.append("SELECT VT.PQ_ORG_NAME,\n" );
			sql.append("       VT.DEALER_CODE,\n" );
			sql.append("       VT.DEALER_NAME,\n" );
			sql.append("       TC1.CODE_DESC POSITION,\n" );
			sql.append("       TC3.CODE_DESC POSITION_STATUS,\n" );
			sql.append("DECODE(VT.STATUS, 10011001, '有效', 10011002, '无效') DEALER_STATUS,\n" );
			sql.append("      TVIC.CURRENT_YEARS YEAR,");
			sql.append("       TVP.NAME,\n" );
			sql.append("       TC2.CODE_DESC BANK,\n" );
			sql.append("       TVP.ID_NO,\n" );
			sql.append("       TVP.BANK_CARDNO,\n" );
			sql.append("       TVIC.THIRD_PERFORMANCE_AGAINST,\n" );
			sql.append("       TVIC.SENCOND_PERFORMANCE_AGAINST,\n" );
			sql.append("       DECODE(TVP.POSITION,99961004,0,TVIC.FIRST_PERFORMANCE_AGAINST) FIRST_PERFORMANCE_AGAINST,\n" );
			sql.append("       TVIC.THIRD_AUTHENTICATION_AGAINST,\n" );
			sql.append("       TVIC.SECOND_AUTHENTICATION_AGAINST,\n" );
			sql.append("       TVIC.FIRST_AUTHENTICATION_AGAINST,\n" );
			sql.append("       TVIC.THIRD_YEAR,\n" );
			sql.append("       TVIC.SENCOND_YEAR,\n" );
			sql.append("       TVIC.FIRST_YEAR,\n" );
			sql.append("       NVL(TVIC.SENCOND_PERFORMANCE_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.THIRD_PERFORMANCE_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.FIRST_PERFORMANCE_AGAINST, 0) PERFORMANCE_AGAINST,\n" );
			sql.append("       NVL(TVIC.THIRD_AUTHENTICATION_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.SECOND_AUTHENTICATION_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.FIRST_AUTHENTICATION_AGAINST, 0) AUTH_AGAINST,\n" );
			sql.append("       NVL(TVIC.THIRD_YEAR, 0) + NVL(TVIC.SENCOND_YEAR, 0) +\n" );
			sql.append("       NVL(TVIC.FIRST_YEAR, 0) YEAR_AGAINST,\n" );
			sql.append("       NVL(TVIC.SENCOND_PERFORMANCE_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.THIRD_PERFORMANCE_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.FIRST_PERFORMANCE_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.THIRD_AUTHENTICATION_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.SECOND_AUTHENTICATION_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.FIRST_AUTHENTICATION_AGAINST, 0) + NVL(TVIC.THIRD_YEAR, 0) +\n" );
			sql.append("       NVL(TVIC.SENCOND_YEAR, 0) + NVL(TVIC.FIRST_YEAR, 0) TOTAL,\n" );
			sql.append("       DECODE(TVP.POSITION,99961004,0,0.7*(NVL(TVIC.SENCOND_PERFORMANCE_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.THIRD_PERFORMANCE_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.FIRST_PERFORMANCE_AGAINST, 0) )+\n" );
			sql.append("       0.15*(NVL(TVIC.THIRD_AUTHENTICATION_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.SECOND_AUTHENTICATION_AGAINST, 0) +\n" );
			sql.append("       NVL(TVIC.FIRST_AUTHENTICATION_AGAINST, 0) )+\n" );
			sql.append("      0.15*( NVL(TVIC.THIRD_YEAR, 0) + NVL(TVIC.SENCOND_YEAR, 0) +\n" );
			sql.append("       NVL(TVIC.FIRST_YEAR, 0))) AllTOTAL\n" );
			sql.append("  FROM TT_VS_INTEGRATION_AGAINST TVIC,\n" );
			sql.append("       VW_ORG_DEALER_ALL         VT,\n" );
			sql.append("       TT_VS_PERSON              TVP,\n" );
			sql.append("       TC_CODE                   TC1,\n" );
			sql.append("       TC_CODE                   TC2,\n" );
			sql.append("       TC_CODE                   TC3\n" );
			sql.append(" WHERE TVIC.ID_NO = TVP.ID_NO\n" );
			sql.append("   AND VT.DEALER_ID = TVP.DEALER_ID\n" );
			sql.append("   AND TC1.CODE_ID = TVP.POSITION\n" );
			sql.append("   AND TC2.CODE_ID = TVP.BANK\n" );
			sql.append("   AND TC3.CODE_ID = TVP.POSITION_STATUS");
			//经销商
			if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
				sql.append(" AND TVIC.DEALER_ID ="+dealerId+"");
				//大区
			}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
				sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.ROOT_ORG_ID="+orgId+")");
				//小区
			}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
				sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.PQ_ORG_ID="+orgId+")");
				
			}

			//姓名检索
			if(name!=null&&!"".equals(name)){
				sql.append(" AND TVIC.NAME LIKE '%"+name+"%'");
			}
			//身份证
			if(idNo!=null&&!"".equals(idNo)){
				sql.append(" AND TVIC.ID_NO like '%"+idNo+"%'");
			}
			//状态
			if(status!=null&&!"".equals(status)){
				sql.append(" AND TVIC.STATUS="+status+"");
			}
			//月份
			if(year!=null&&!"".equals(year)){
				int years=Integer.parseInt(year)-1;
				sql.append(" AND TVIC.CURRENT_YEARS="+years+"");
			}
			//职位状态
			if(positionStatus!=null&&!"".equals(positionStatus)){
				sql.append(" AND TVP.POSITION_STATUS="+positionStatus+"");
			}
			//职位
			if(position!=null&&!"".equals(position)){
				sql.append(" AND TVP.POSITION="+position+"");
			}
			//银行卡号
			if(bankCardNo!=null&&!"".equals(bankCardNo)){
				sql.append(" AND TVP.BANK_CARDNO="+bankCardNo+"");
			}
			//经销商代码
			if(dealerCode!=null&&!"".equals(dealerCode)){
				String dealerCodes=CommonUtils.getSplitStringForIn(dealerCode);
				sql.append("  AND VT.DEALER_CODE IN("+dealerCodes+")");
			}
			sql.append("  ORDER BY VT.PQ_ORG_CODE, VT.DEALER_CODE");

			List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
			
			return list;
		}
	public List<Map<String, Object>> getPersonIntegMonth(Map<String, String> parm){
		String name=CommonUtils.checkNull(parm.get("name"));
		String idNo=CommonUtils.checkNull(parm.get("idNo"));
		String status=CommonUtils.checkNull(parm.get("status"));
		String orgId=CommonUtils.checkNull(parm.get("orgId"));
		String dealerId=CommonUtils.checkNull(parm.get("dealerId"));
		String year=CommonUtils.checkNull(parm.get("year"));
		String month=CommonUtils.checkNull(parm.get("month"));
		String positionStatus=CommonUtils.checkNull(parm.get("positionStatus"));
		String position=CommonUtils.checkNull(parm.get("position"));
		String bankCardNo=CommonUtils.checkNull(parm.get("bankCardNo"));
		String dealerCode=CommonUtils.checkNull(parm.get("dealerCode"));
		String dutyType=CommonUtils.checkNull(parm.get("dutyType"));
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT  VOD.PQ_ORG_NAME,\n" );
		sql.append("       VOD.DEALER_CODE,\n" );
		sql.append("       VOD.DEALER_NAME,\n" );
		sql.append("       TVIC.YEAR,\n" );
		sql.append("       TVIC.MONTH,\n" );
		sql.append("       TVP.POSITION,\n" );
		sql.append("       TVP.POSITION_STATUS,\n" );
		sql.append("       TVP.NAME,\n" );
		sql.append("       TVP.BANK,\n" );
		sql.append("       TVP.BANK_CARDNO,\n" );
		sql.append("       TVP.ID_NO,\n" );
		sql.append("       NVL(TVIC.MONTH_PERFORMANCE_INTEG,0) MONTH_PERFORMANCE_INTEG,\n" );
		sql.append("       NVL(TVIC.MONTH_AUTHENTICATION_INTEG,0) MONTH_AUTHENTICATION_INTEG,\n" );
		sql.append("       NVL(TVIC.MONTH_YEAR_INTEG,0)MONTH_YEAR_INTEG,\n" );
		sql.append("       TVIC.MONTH_INTEG\n" );
		sql.append("  FROM TT_VS_INTEGRATION_MONTH TVIC, VW_ORG_DEALER VOD, TT_VS_PERSON TVP\n" );
		sql.append(" WHERE VOD.DEALER_ID = TVIC.DEALER_ID\n" );
		sql.append("   AND TVP.ID_NO = TVIC.ID_NO");
		//经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID ="+dealerId+"");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.ROOT_ORG_ID="+orgId+")");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND TVIC.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.PQ_ORG_ID="+orgId+")");
			
		}
		//姓名检索
		if(name!=null&&!"".equals(name)){
			sql.append(" AND TVIC.NAME LIKE '%"+name+"%'");
		}
		//身份证
		if(idNo!=null&&!"".equals(idNo)){
			sql.append(" AND TVIC.ID_NO like '%"+idNo+"%'");
		}
		//状态
		if(status!=null&&!"".equals(status)){
			sql.append(" AND TVIC.STATUS="+status+"");
		}
		//年份
		if(year!=null&&!"".equals(year)){
			int years=Integer.parseInt(year);
			sql.append(" AND TVIC.YEAR="+years+"");
		}
		//月份
		if(month!=null&&!"".equals(month)){
			
			sql.append(" AND TVIC.MONTH="+month+"");
		}
		//职位状态
		if(positionStatus!=null&&!"".equals(positionStatus)){
			sql.append(" AND TVP.POSITION_STATUS="+positionStatus+"");
		}
		//职位
		if(position!=null&&!"".equals(position)){
			sql.append(" AND TVIC.POSITION="+position+"");
		}
		//银行卡号
		if(bankCardNo!=null&&!"".equals(bankCardNo)){
			sql.append(" AND TVP.BANK_CARDNO="+bankCardNo+"");
		}
		//经销商代码
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String dealerCodes=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append("  AND VOD.DEALER_CODE IN("+dealerCodes+")");
		}
		sql.append("  ORDER BY VOD.PQ_ORG_CODE, VOD.DEALER_CODE");

		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		
		return list;
	}


}
