package com.infodms.dms.dao.sales.usermng;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsPersonChangePO;
import com.infodms.dms.po.TtVsPersonPO;
import com.infodms.dms.po.TtVsPersonRegistPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class UserManageDao extends BaseDao<PO> {
	public Logger logger = Logger.getLogger(UserManageDao.class);
	
	private static final UserManageDao dao = new UserManageDao();

	public static final UserManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 人员注册
	 */
	public void userRegist(TtVsPersonRegistPO tvprp) throws Exception{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb=new StringBuffer();
		sb.append("INSERT INTO TT_VS_PERSON_REGIST\n");
		sb.append(" (REGIST_ID,\n");
		sb.append(" DEALER_ID,\n");
		sb.append(" CREATE_USER_ID,\n");
		sb.append(" CREATE_DATE,\n");
		sb.append(" ID_NO,\n");
		sb.append(" GENDER,\n");
		sb.append(" NAME,\n");
		sb.append(" EMAIL,\n");
		sb.append(" MOBILE,\n");
		sb.append(" POSITION,\n");
		sb.append(" IS_INVESTOR,\n");
		sb.append("ENTRY_DATE,\n");
		sb.append(" BANK,\n");
		sb.append(" BANKCARD_NO,\n");
		sb.append("REMARK,STATUS)\n");
		sb.append("VALUES\n");
		sb.append("( F_GETID(),\n");
		sb.append(""+tvprp.getDealerId()+",\n");
		sb.append(""+tvprp.getCreateUserId()+",\n");
		sb.append("SYSDATE,\n");
		sb.append("'"+tvprp.getIdNo()+"',\n");
		sb.append(""+tvprp.getGender()+",\n");
		sb.append("'"+tvprp.getName()+"',\n");
		sb.append(" '"+tvprp.getEmail()+"',\n");
		sb.append(""+tvprp.getMobile()+",\n");
		sb.append(""+tvprp.getPosition()+",\n");
		sb.append(""+tvprp.getIsInvestor()+",\n");
		sb.append("to_date('"+df.format(tvprp.getEntryDate())+"','yyyy-MM-dd'),\n");
		sb.append(""+tvprp.getBank()+",\n");
		sb.append(" "+tvprp.getBankcardNo()+",\n");
		sb.append("'"+tvprp.getRemark()+"',\n");
		sb.append(""+tvprp.getStatus()+"\n");
		sb.append(")");
		dao.insert(sb.toString());
		
	}
	/**
	 * 注册人员的信息查询
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> userSelect(String dealerCodes,TtVsPersonRegistPO tvprp,String flagType ,int pageSize,int curPage) throws Exception{

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT R.REGIST_ID,\n");
		sql.append(" TD.DEALER_NAME,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append(" R.DEALER_ID,\n");
		sql.append(" R.CREATE_USER_ID,\n");
		sql.append("  R.CREATE_DATE,\n");
		sql.append(" R.ID_NO,\n");
		sql.append("  R.GENDER,\n");
		sql.append(" R.NAME,\n");
		sql.append("  R.EMAIL,\n");
		sql.append(" R.MOBILE,\n");
		sql.append("  R.POSITION,\n");
		sql.append(" R.IS_INVESTOR,\n");
		sql.append("  R.ENTRY_DATE,\n");
		sql.append(" R.BANK,\n");
		sql.append(" R.BANKCARD_NO,\n");
		sql.append(" R.STATUS,\n");
		sql.append(" R.REMARK\n");
		sql.append(" FROM TT_VS_PERSON_REGIST R,\n");
		sql.append(" TM_DEALER TD\n");
		sql.append("WHERE TD.DEALER_ID= R.DEALER_ID ");
		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND R.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND R.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND R.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append(" AND R.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append(" AND R.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append(" AND R.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append(" AND R.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("AND R.BANK ="+tvprp.getBank());
		}
		//如果条件中有所属银行
		if(tvprp.getStatus()!=null&&!"".equals(tvprp.getStatus())){
			sql.append("AND R.STATUS ="+tvprp.getStatus());
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("AND R.DEALER_ID ="+tvprp.getDealerId());
		}
		if(flagType!=null&&!"".equals(flagType)){
			sql.append(" AND R.STATUS IN(99991001,99991004)");
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String dealerCode=CommonUtils.getSplitStringForIn(dealerCodes);
			sql.append("  AND TD.DEALER_CODE IN("+dealerCode+")");
		}
		sql.append("  ORDER BY R.CREATE_DATE DESC");
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 大区注册人员的信息查询
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> userLargeSelect(String dealerCodes,TtVsPersonRegistPO tvprp,long orgId,String flagType,int pageSize,int curPage) throws Exception{

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT R.REGIST_ID,\n");
		sql.append(" TD.DEALER_NAME,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append(" R.DEALER_ID,\n");
		sql.append(" R.CREATE_USER_ID,\n");
		sql.append("  R.CREATE_DATE,\n");
		sql.append(" R.ID_NO,\n");
		sql.append("  R.GENDER,\n");
		sql.append(" R.NAME,\n");
		sql.append("  R.EMAIL,\n");
		sql.append(" R.MOBILE,\n");
		sql.append("  R.POSITION,\n");
		sql.append(" R.IS_INVESTOR,\n");
		sql.append("  R.ENTRY_DATE,\n");
		sql.append(" R.BANK,\n");
		sql.append(" R.BANKCARD_NO,\n");
		sql.append(" R.STATUS,\n");
		sql.append(" R.REMARK\n");
		sql.append(" FROM TT_VS_PERSON_REGIST R,\n");
		sql.append(" TM_DEALER TD\n");
		sql.append("WHERE R.DEALER_ID=TD.DEALER_ID ");
		sql.append("AND R.DEALER_ID IN");
		sql.append("  (SELECT DEALER_ID FROM vw_org_dealer ");
		sql.append("WHERE ROOT_ORG_ID="+orgId+")");

		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND R.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND R.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND R.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append(" AND R.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append(" AND GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append(" AND R.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append(" AND R.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("AND R.BANK ="+tvprp.getBank());
		}
		//如果条件中有所属银行
		if(tvprp.getStatus()!=null&&!"".equals(tvprp.getStatus())){
			sql.append("AND R.STATUS ="+tvprp.getStatus());
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("AND R.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String dealerCode=CommonUtils.getSplitStringForIn(dealerCodes);
			sql.append("  AND TD.DEALER_CODE IN("+dealerCode+")");
		}
		//新增页面只是可以查询未提报和驳回的数据
		if(flagType!=null&&!"".equals(flagType)){
			sql.append(" AND R.STATUS IN(99991001,99991004)");
		}
		sql.append("  ORDER BY R.CREATE_DATE DESC");
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 小区注册人员的信息查询
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> userSmallSelect(String dealerCodes,TtVsPersonRegistPO tvprp,Long orgId,String flagType,int pageSize,int curPage) throws Exception{

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT R.REGIST_ID,\n");
		sql.append(" TD.DEALER_NAME,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append(" R.DEALER_ID,\n");
		sql.append(" R.CREATE_USER_ID,\n");
		sql.append("  R.CREATE_DATE,\n");
		sql.append(" R.ID_NO,\n");
		sql.append("  R.GENDER,\n");
		sql.append(" R.NAME,\n");
		sql.append("  R.EMAIL,\n");
		sql.append(" R.MOBILE,\n");
		sql.append("  R.POSITION,\n");
		sql.append(" R.IS_INVESTOR,\n");
		sql.append("  R.ENTRY_DATE,\n");
		sql.append(" R.BANK,\n");
		sql.append(" R.BANKCARD_NO,\n");
		sql.append(" R.STATUS,\n");
		sql.append(" R.REMARK\n");
		sql.append(" FROM TT_VS_PERSON_REGIST R,\n");
		sql.append(" TM_DEALER TD\n");
		sql.append("WHERE TD.DEALER_ID=R.DEALER_ID ");
		sql.append("AND R.DEALER_ID IN");
		sql.append("  (SELECT DEALER_ID FROM vw_org_dealer ");
		sql.append("WHERE PQ_ORG_ID="+orgId+")");

		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND R.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND R.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND R.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append(" AND R.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append(" AND GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append(" AND R.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append(" AND R.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("AND R.BANK ="+tvprp.getBank());
		}
		//如果条件中有所属银行
		if(tvprp.getStatus()!=null&&!"".equals(tvprp.getStatus())){
			sql.append("AND R.STATUS ="+tvprp.getStatus());
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("AND R.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String dealerCode=CommonUtils.getSplitStringForIn(dealerCodes);
			sql.append("  AND TD.DEALER_CODE IN("+dealerCode+")");
		}
		if(flagType!=null&&!"".equals(flagType)){
			sql.append(" AND R.STATUS IN(99991001,99991004)");
		}
		sql.append("  ORDER BY R.CREATE_DATE DESC");
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 验证身份证
	 * @param idNo
	 */
	public int userIdNoCheck(String idNo,String flag,String personId) throws Exception {
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT COUNT(1) COUNT FROM TT_VS_PERSON WHERE ID_NO='"+idNo+"' AND POSITION_STATUS=99941001");
		if(flag!=null&&!"".equals(flag)){
			sql.append("   and person_id!="+personId+"");
		}
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, getFunName());
		String count=list.get(0).get("COUNT").toString();
		StringBuilder sql1= new StringBuilder();
		sql1.append("SELECT COUNT(*) COUNT1 \n" );
		sql1.append("  FROM T_ORDER_MAST_29101@SCSDBLINK VCE\n" );
		sql1.append(" WHERE VCE.MASTCHAR40 = '"+idNo+"'\n" );
		sql1.append("   AND VCE.MASTNUM33 = 1");
		List<Map<String,Object>> list1=dao.pageQuery(sql1.toString(), null, getFunName());
		String count1=list1.get(0).get("COUNT1").toString();
		return Integer.parseInt(count1)+Integer.parseInt(count);
		
	}
	/**
	 * 验证银行卡号是否重复
	 * @param idNo
	 */
	public int userBankCardCheck(String bankCardNo,String flag,String personId) throws Exception {
		int count=0;
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT COUNT(1) COUNT FROM TT_VS_PERSON  TVP WHERE  TVP.BANK_CARDNO='"+bankCardNo+"'\n");
		sql.append(" AND TVP.POSITION_STATUS = 99941001");
		if(flag!=null&&!"".equals(flag)){
			sql.append("   and tvp.person_id!="+personId+"");
		}
		Map<String,Object>m=dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		count=Integer.parseInt(m.get("COUNT").toString());
		return count;
		
	}
	/**
	 * 验证职位否重复
	 * @param idNo
	 */
	public int userPositionCount(Long dealerId,Long position,String id_no) throws Exception {
		int count=0;
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT COUNT(1) COUNT \n" );
		sql.append("  FROM TT_VS_PERSON TVP\n" );
		sql.append(" WHERE TVP.POSITION_STATUS = 99941001\n" );
		sql.append("   AND TVP.POSITION = "+position+"\n" );
		sql.append("   AND TVP.POSITION!="+99961001+"\n");
		sql.append("   AND TVP.DEALER_ID = "+dealerId+"\n");
		sql.append("   AND TVP.ID_NO! = '"+id_no+"'\n");
		Map<String,Object>m=dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		count=Integer.parseInt(m.get("COUNT").toString());
		int count1=0;
		if(position.longValue()==99961004){
			StringBuilder sql1= new StringBuilder();
			sql1.append("SELECT  COUNT(*) COUNT1\n" );
			sql1.append("  FROM TM_DEALER TD, CSC.T_ORG_DEFINE TOD, CSC.T_ORDER_MAST_29101 VCE\n" );
			sql1.append(" WHERE TD.OLD_DEALERCODE = TOD.EABBR\n" );
			sql1.append("   AND VCE.MASTCHAR12 = TOD.ORG_CODE\n" );
			sql1.append("   AND VCE.MASTNUM31 = '10'\n" );
			sql1.append("   AND VCE.MASTNUM33 = 1\n" );
			sql1.append("   AND TD.DEALER_ID = "+dealerId+"");

			List<Map<String,Object>> list1=dao.pageQuery(sql1.toString(), null, getFunName());
			String counts=list1.get(0).get("COUNT1").toString();
			count1=Integer.parseInt(counts);
		}
		
		return count+count1;
		
	}
	/**
	 * 机构人员表数据插入
	 * @param 人员对象
	 */
	public void personInsert(TtVsPersonPO tvprp) throws Exception{
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb=new StringBuffer();
		sb.append("INSERT INTO TT_VS_PERSON\n");
		sb.append(" (PERSON_ID,\n");
		sb.append(" DEALER_ID,\n");
		sb.append(" CREATE_DATE,\n");
		sb.append(" ID_NO,\n");
		sb.append(" GENDER,\n");
		sb.append(" NAME,\n");
		sb.append(" EMAIL,\n");
		sb.append(" MOBILE,\n");
		sb.append(" POSITION,\n");
		sb.append(" IS_INVESTOR,\n");
		sb.append("ENTRY_DATE,\n");
		sb.append(" BANK,\n");
		sb.append(" BANK_CARDNO,\n");
		sb.append(" DEGREE,\n");
		sb.append("REMARK,THREE_MONTH_ZERO,\n");
		sb.append(" POSITION_STATUS )\n");
		sb.append("VALUES\n");
		sb.append("( F_GETID(),\n");
		sb.append(""+tvprp.getDealerId()+",\n");
		sb.append("SYSDATE,\n");
		sb.append("'"+tvprp.getIdNo()+"',\n");
		sb.append(""+tvprp.getGender()+",\n");
		sb.append("'"+tvprp.getName()+"',\n");
		sb.append(" '"+tvprp.getEmail()+"',\n");
		sb.append(""+tvprp.getMobile()+",\n");
		sb.append(""+tvprp.getPosition()+",\n");
		sb.append(""+tvprp.getIsInvestor()+",\n");
		sb.append("to_date('"+df.format(tvprp.getEntryDate())+"','yyyy-MM-dd'),\n");
		sb.append(""+tvprp.getBank()+",\n");
		sb.append(" '"+tvprp.getBankCardno()+"',\n");
		sb.append(" "+tvprp.getDegree()+",\n");
		sb.append("'"+tvprp.getRemark()+"',\n");
		sb.append(""+tvprp.getThreeMonthZero()+",\n");
		sb.append(""+tvprp.getPositionStatus()+"");
		sb.append(")");
		dao.insert(sb.toString());
		
	}
	/**
	 * 大区人员正式表的信息查询
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> personLargeSelect(String dealerCode,TtVsPersonPO tvprp,long orgId,int pageSize,int curPage) throws Exception{

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT R.PERSON_ID,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append("  TD.DEALER_NAME,\n");
		sql.append(" R.DEALER_ID,\n");
		sql.append("  R.CREATE_DATE,\n");
		sql.append(" R.ID_NO,\n");
		sql.append("  R.GENDER,\n");
		sql.append(" R.NAME,\n");
		sql.append("  R.EMAIL,\n");
		sql.append(" R.MOBILE,\n");
		sql.append("  R.POSITION,\n");
		sql.append("  R.POSITION_STATUS,\n");
		sql.append(" R.IS_INVESTOR,\n");
		sql.append("  R.ENTRY_DATE,\n");
		sql.append(" R.BANK,\n");
		sql.append(" R.BANK_CARDNO,\n");
		sql.append(" R.AUTHENTICATION_LEVEL,\n");
		sql.append(" R.REMARK\n");
		sql.append(" FROM TT_VS_PERSON R ,TM_DEALER TD \n");
		sql.append("WHERE TD.DEALER_ID=R.DEALER_ID ");
		sql.append("AND R.DEALER_ID IN");
		sql.append("  (SELECT DEALER_ID FROM vw_org_dealer ");
		sql.append("WHERE ROOT_ORG_ID="+orgId+")");

		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND R.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND R.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND R.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append(" AND R.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append(" AND R.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append(" AND R.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append(" AND R.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("AND R.BANK ="+tvprp.getBank());
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String str=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append(" AND TD.DEALER_CODE IN("+str+")");
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("AND R.DEALER_ID ="+tvprp.getDealerId());
		}
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 小区人员正式表的信息查询
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> personSmallSelect(String dealerCode,TtVsPersonPO tvprp,Long orgId,int pageSize,int curPage) throws Exception{

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT R.PERSON_ID,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append("  TD.DEALER_NAME,\n");
		sql.append(" R.DEALER_ID,\n");
		sql.append("  R.CREATE_DATE,\n");
		sql.append(" R.ID_NO,\n");
		sql.append("  R.GENDER,\n");
		sql.append(" R.NAME,\n");
		sql.append("  R.EMAIL,\n");
		sql.append(" R.MOBILE,\n");
		sql.append("  R.POSITION,\n");
		sql.append("  R.POSITION_STATUS,\n");
		sql.append(" R.IS_INVESTOR,\n");
		sql.append("  R.ENTRY_DATE,\n");
		sql.append(" R.BANK,\n");
		sql.append(" R.BANK_CARDNO,\n");
		sql.append(" R.AUTHENTICATION_LEVEL,\n");
		sql.append(" R.REMARK\n");
		sql.append(" FROM TT_VS_PERSON R,TM_DEALER TD\n");
		sql.append("WHERE R.DEALER_ID=TD.DEALER_ID \n");
		sql.append("AND R.DEALER_ID IN");
		sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW ");
		sql.append("WHERE VW.PQ_ORG_ID="+orgId+")");

		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND R.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND R.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND R.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append(" AND R.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append(" AND R.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append(" AND R.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append(" AND R.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("AND R.BANK ="+tvprp.getBank());
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("AND R.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String str=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append(" AND TD.DEALER_CODE IN("+str+")");
		}
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 机构人员表的信息查询
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> personSelect(String dealerCode,TtVsPersonPO tvprp,String dealerCodes,int pageSize,int curPage) throws Exception{
		
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT P.PERSON_ID,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append("  TD.DEALER_NAME,\n");
		sql.append(" P.DEALER_ID,\n");
		sql.append("  P.CREATE_DATE,\n");
		sql.append(" P.ID_NO,\n");
		sql.append("  P.GENDER,\n");
		sql.append(" P.NAME,\n");
		sql.append("  P.EMAIL,\n");
		sql.append(" P.MOBILE,\n");
		sql.append("  P.POSITION,\n");
		sql.append("  P.POSITION_STATUS,\n");
		sql.append(" P.IS_INVESTOR,\n");
		sql.append("  P.ENTRY_DATE,\n");
		sql.append(" P.BANK,\n");
		sql.append(" P.BANK_CARDNO,\n");
		sql.append(" P.AUTHENTICATION_LEVEL,\n");
		sql.append("     (SELECT T.CODE_DESC\n") ;
		sql.append("        FROM TC_CODE T\n") ;
		sql.append("       WHERE T.CODE_ID = P.POSITION_STATUS) AS POSITION_STATUS_NAME,\n") ;
		sql.append(" P.REMARK\n");
		sql.append(" FROM TT_VS_PERSON P,TM_DEALER TD\n");
		sql.append("WHERE P.DEALER_ID=TD.DEALER_ID \n ");
		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND P.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND P.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND P.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND P.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND P.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append("  AND P.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append("  AND P.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND P.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("  AND P.BANK ="+tvprp.getBank());
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("  AND P.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String str=CommonUtils.getSplitStringForIn(dealerCodes);
			boolean flag=dealerCodes.contains(",");
			if(flag){
				sql.append(" AND  TD.DEALER_CODE IN("+str+")");
			}
		}
//		if(dealerCode!=null&&!"".equals(dealerCode)){
//			String str=CommonUtils.getSplitStringForIn(dealerCode);
//			sql.append(" AND TD.DEALER_CODE IN("+str+")");
//		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			boolean flag=dealerCodes.contains(",");
			if(!flag){
				sql.append("  AND  TD.DEALER_ID IN (SELECT TD1.DEALER_ID\n" );
				sql.append("                         FROM TM_DEALER TD1\n" );
				sql.append("                        WHERE 1 = 1\n" );
				sql.append("                        START WITH TD1.DEALER_CODE IN ('"+dealerCodes+"')\n" );
				sql.append("                       CONNECT BY PRIOR TD1.DEALER_ID = TD1.PARENT_DEALER_D\n" );
				sql.append("                       )\n" );
			}
		}
		sql.append("   ORDER BY P.CREATE_DATE DESC");

		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	
	
	public PageResult<Map<String,Object>> personSelectInitQuery(String dealerCode,TtVsPersonPO tvprp,int pageSize,int curPage) throws Exception{

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT P.PERSON_ID,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append("  TD.DEALER_NAME,\n");
		sql.append(" P.DEALER_ID,\n");
		sql.append("  P.CREATE_DATE,\n");
		sql.append(" P.ID_NO,\n");
		sql.append("  P.GENDER,\n");
		sql.append(" P.NAME,\n");
		sql.append("  P.EMAIL,\n");
		sql.append(" P.MOBILE,\n");
		sql.append("  P.POSITION,\n");
		sql.append("  P.POSITION_STATUS,\n");
		sql.append(" P.IS_INVESTOR,\n");
		sql.append("  P.ENTRY_DATE,\n");
		sql.append(" P.BANK,\n");
		sql.append(" P.BANK_CARDNO,\n");
		sql.append(" P.AUTHENTICATION_LEVEL,\n");
		sql.append("     (SELECT T.CODE_DESC\n") ;
		sql.append("        FROM TC_CODE T\n") ;
		sql.append("       WHERE T.CODE_ID = P.POSITION_STATUS) AS POSITION_STATUS_NAME,\n") ;
		sql.append(" P.REMARK\n");
		sql.append(" FROM TT_VS_PERSON P,TM_DEALER TD\n");
		sql.append("WHERE P.DEALER_ID=TD.DEALER_ID \n ");
		sql.append("  AND P.POSITION_STATUS IN (99941001,99941003)");
		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND P.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND P.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND P.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND P.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND P.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append("  AND P.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append("  AND P.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("  AND P.BANK ="+tvprp.getBank());
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("  AND P.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String str=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append(" AND TD.DEALER_CODE IN("+str+")");
		}
		sql.append("   ORDER BY P.CREATE_DATE DESC");
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	
	/**
	 * 大区查询机构人员变动表的信息
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> personChangeLargeSelect(String dealerCode,TtVsPersonChangePO tvprp,long orgId,int pageSize,int curPage) throws Exception{

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT R.PERSON_CHANGE_ID,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append("  TD.DEALER_NAME,\n");
		sql.append(" R.DEALER_ID,\n");
		sql.append("  R.CREATE_DATE,\n");
		sql.append(" R.ID_NO,\n");
		sql.append(" R.NAME,\n");
		sql.append("  R.EMAIL,\n");
		sql.append(" R.MOBILE,\n");
		sql.append("  R.POSITION,\n");
		sql.append("  R.POSITION_STATUS,\n");
		sql.append(" R.IS_INVESTOR,\n");
		sql.append("  R.ENTRY_DATE,\n");
		sql.append(" R.BANK,\n");
		sql.append(" R.BANK_CARDNO,\n");
		sql.append(" R.AUTHENTICATION_LEVEL,\n");
		sql.append(" R.REMARK\n");
		sql.append(" FROM TT_VS_PERSON_CHANGE R,TM_DEALER TD\n");
		sql.append("WHERE R.DEALER_ID=TD.DEALER_ID ");
		sql.append("AND R.DEALER_ID IN");
		sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW ");
		sql.append("WHERE VW.ROOT_ORG_ID="+orgId+")");

		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND R.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND R.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND R.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append(" AND R.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append(" AND R.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append(" AND R.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append(" AND R.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("AND R.BANK ="+tvprp.getBank());
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("AND R.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String str=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append(" AND TD.DEALER_CODE IN("+str+")");
		}
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 小区查询机构人员变动表信息
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> personChangeSmallSelect(String dealerCode,TtVsPersonChangePO tvprp,Long orgId,int pageSize,int curPage) throws Exception{

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT R.PERSON_CHANGE_ID,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append("  TD.DEALER_NAME,\n");
		sql.append(" R.DEALER_ID,\n");
		sql.append("  R.CREATE_DATE,\n");
		sql.append(" R.ID_NO,\n");
		sql.append("  R.GENDER,\n");
		sql.append(" R.NAME,\n");
		sql.append("  R.EMAIL,\n");
		sql.append(" R.MOBILE,\n");
		sql.append("  R.POSITION,\n");
		sql.append("  R.POSITION_STATUS,\n");
		sql.append(" R.IS_INVESTOR,\n");
		sql.append("  R.ENTRY_DATE,\n");
		sql.append(" R.BANK,\n");
		sql.append(" R.BANK_CARDNO,\n");
		sql.append(" R.AUTHENTICATION_LEVEL,\n");
		sql.append(" R.CHANGE_TYPE,\n");
		sql.append(" R.REMARK\n");
		sql.append(" FROM TT_VS_PERSON_CHANGE R,TM_DEALER TD\n");
		sql.append("WHERE R.DEALER_ID=TD.DEALER_ID ");
		sql.append("AND R.DEALER_ID IN");
		sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
		sql.append("WHERE VW.PQ_ORG_ID="+orgId+")");

		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND R.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND R.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND R.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append(" AND R.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append(" AND GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append(" AND R.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append(" AND R.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("AND R.BANK ="+tvprp.getBank());
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("AND R.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String str=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append(" AND TD.DEALER_CODE IN("+str+")");
		}
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 机构人员变动表查询
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> personChangeSelect(String dealerCode ,TtVsPersonChangePO tvprp,int pageSize,int curPage) throws Exception{

		StringBuffer sql=new StringBuffer();
		sql.append("SELECT R.PERSON_CHANGE_ID,\n");
		sql.append(" TD.DEALER_CODE,\n");
		sql.append("  TD.DEALER_NAME,\n");
		sql.append(" R.DEALER_ID,\n");
		sql.append("  R.CREATE_DATE,\n");
		sql.append(" R.ID_NO,\n");
		sql.append(" R.NAME,\n");
		sql.append("  R.EMAIL,\n");
		sql.append("  R.GENDER,\n");
		sql.append(" R.MOBILE,\n");
		sql.append("  R.POSITION,\n");
		sql.append("  R.POSITION_STATUS,\n");
		sql.append(" R.IS_INVESTOR,\n");
		sql.append("  R.ENTRY_DATE,\n");
		sql.append(" R.BANK,\n");
		sql.append(" R.BANK_CARDNO,\n");
		sql.append(" R.AUTHENTICATION_LEVEL,\n");
		sql.append(" R.REMARK,\n");
		sql.append(" R.CHANGE_TYPE\n");
		sql.append(" FROM TT_VS_PERSON_CHANGE R,TM_DEALER TD\n");
		sql.append("WHERE R.DEALER_ID=TD.DEALER_ID ");
		//如果条件中有名字
		if(tvprp.getName()!=null&&!"".equals(tvprp.getName())){
			sql.append(" AND R.NAME LIKE '%"+tvprp.getName()+"%'");
		}
		//如果条件中有身份证
		if(tvprp.getIdNo()!=null&&!"".equals(tvprp.getIdNo())){
			sql.append(" AND R.ID_NO ='"+tvprp.getIdNo()+"'");
		}
		//如果条件中有电子邮件
		if(tvprp.getEmail()!=null&&!"".equals(tvprp.getEmail())){
			sql.append(" AND R.EMAIL ='"+tvprp.getEmail()+"'");
		}
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append(" AND R.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append(" AND R.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getIsInvestor()!=null&&!"".equals(tvprp.getIsInvestor())){
			sql.append(" AND R.IS_INVESTOR ="+tvprp.getIsInvestor());
		}
		//如果条件中有职位
		if(tvprp.getPosition()!=null&&!"".equals(tvprp.getPosition())){
			sql.append(" AND R.POSITION ="+tvprp.getPosition());
		}
		//如果条件中有所属银行
		if(tvprp.getBank()!=null&&!"".equals(tvprp.getBank())){
			sql.append("AND R.BANK ="+tvprp.getBank());
		}
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("AND R.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String str=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append(" AND TD.DEALER_CODE IN("+str+")");
		}
		
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 渠道人员数量汇总的信息查询
	 * @param tvprp
	 */
	public List<Map<String,Object>> personCountDownLoad(AclUserBean logonUser,String dealerCode,TtVsPersonPO tvprp,String dealerCodes) throws Exception{
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT VOD.PQ_ORG_CODE,\n" );
		sql.append("       VOD.PQ_ORG_NAME,\n" );
		sql.append("       VOD.DEALER_CODE,\n" );
		sql.append("       VOD.DEALER_NAME,\n" );
		sql.append("       TK.MGR,\n" );
		sql.append("       TK.SALE_MGR,\n" );
		sql.append("       TK.MARK_MGR,\n" );
		sql.append("       TK.SAL_MAN,\n" );
		sql.append("       TK.TOTAL\n" );
		sql.append("  FROM (SELECT TS.DEALER_ID,\n" );
		sql.append("               SUM(MGR) MGR,\n" );
		sql.append("               SUM(SALE_MGR) SALE_MGR,\n" );
		sql.append("               SUM(MARK_MGR) MARK_MGR,\n" );
		sql.append("               SUM(SAL_MAN) SAL_MAN,\n" );
		sql.append("               SUM(TOTAL) TOTAL\n" );
		sql.append("          FROM (SELECT VOD.DEALER_ID,\n" );
		sql.append("                       COUNT(1) MGR,\n" );
		sql.append("                       0 SALE_MGR,\n" );
		sql.append("                       0 MARK_MGR,\n" );
		sql.append("                       0 SAL_MAN,\n" );
		sql.append("                       0 TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		sql.append("                   AND TVP.POSITION = 99961004\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT VOD.DEALER_ID,\n" );
		sql.append("                       0 MGR,\n" );
		sql.append("                       COUNT(1) SALE_MGR,\n" );
		sql.append("                       0 MARK_MGR,\n" );
		sql.append("                       0 SAL_MAN,\n" );
		sql.append("                       0 TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		sql.append("                   AND TVP.POSITION = 99961003\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT VOD.DEALER_ID,\n" );
		sql.append("                       0 MGR,\n" );
		sql.append("                       0 SALE_MGR,\n" );
		sql.append("                       COUNT(1) MARK_MGR,\n" );
		sql.append("                       0 SAL_MAN,\n" );
		sql.append("                       0 TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		sql.append("                   AND TVP.POSITION = 99961002\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT VOD.DEALER_ID,\n" );
		sql.append("                       0 MGR,\n" );
		sql.append("                       0 SALE_MGR,\n" );
		sql.append("                       0 MARK_MGR,\n" );
		sql.append("                       COUNT(1) SAL_MAN,\n" );
		sql.append("                       0 TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		sql.append("                   AND TVP.POSITION = 99961001\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT VOD.DEALER_ID,\n" );
		sql.append("                       0 MGR,\n" );
		sql.append("                       0 SALE_MGR,\n" );
		sql.append("                       0 MARK_MGR,\n" );
		sql.append("                       0 SAL_MAN,\n" );
		sql.append("                       COUNT(1) TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID) TS\n" );
		sql.append("         GROUP BY DEALER_ID) TK,\n" );
		sql.append("       VW_ORG_DEALER_ALL VOD\n" );
		sql.append(" WHERE TK.DEALER_ID = VOD.DEALER_ID\n" );
		
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("  AND VOD.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String str=CommonUtils.getSplitStringForIn(dealerCodes);
			boolean flag=dealerCodes.contains(",");
			if(flag){
				sql.append(" AND  VOD.DEALER_CODE IN("+str+")");
			}
		}
		
		// 大区机构人员变动查询
		if ("10431003".equals(logonUser.getDutyType())) {
			sql.append("AND vod.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.ROOT_ORG_ID="+logonUser.getOrgId()+")");
			// 小区机构人员变动查询
		} else if ("10431004".equals(logonUser.getDutyType())) {
			sql.append("AND vod.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.PQ_ORG_ID="+logonUser.getOrgId()+")");
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			boolean flag=dealerCodes.contains(",");
			if(!flag){
				sql.append("  AND  TD.DEALER_ID IN (SELECT TD1.DEALER_ID\n" );
				sql.append("                         FROM TM_DEALER TD1\n" );
				sql.append("                        WHERE 1 = 1\n" );
				sql.append("                        START WITH TD1.DEALER_CODE IN ('"+dealerCodes+"')\n" );
				sql.append("                       CONNECT BY PRIOR TD1.DEALER_ID = TD1.PARENT_DEALER_D\n" );
				sql.append("                       )\n" );
			}
		}
		sql.append(" order by vod.PQ_ORG_CODE,vod.DEALER_CODE");
		List<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	/**
	 * 渠道人员数量汇总的信息查询
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> personCountSelect(AclUserBean logonUser,String dealerCode,TtVsPersonPO tvprp,String dealerCodes,int pageSize,int curPage) throws Exception{
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT VOD.PQ_ORG_CODE,\n" );
		sql.append("       VOD.PQ_ORG_NAME,\n" );
		sql.append("       VOD.DEALER_CODE,\n" );
		sql.append("       VOD.DEALER_NAME,\n" );
		sql.append("       TK.MGR,\n" );
		sql.append("       TK.SALE_MGR,\n" );
		sql.append("       TK.MARK_MGR,\n" );
		sql.append("       TK.SAL_MAN,\n" );
		sql.append("       TK.TOTAL\n" );
		sql.append("  FROM (SELECT TS.DEALER_ID,\n" );
		sql.append("               SUM(MGR) MGR,\n" );
		sql.append("               SUM(SALE_MGR) SALE_MGR,\n" );
		sql.append("               SUM(MARK_MGR) MARK_MGR,\n" );
		sql.append("               SUM(SAL_MAN) SAL_MAN,\n" );
		sql.append("               SUM(TOTAL) TOTAL\n" );
		sql.append("          FROM (SELECT VOD.DEALER_ID,\n" );
		sql.append("                       COUNT(1) MGR,\n" );
		sql.append("                       0 SALE_MGR,\n" );
		sql.append("                       0 MARK_MGR,\n" );
		sql.append("                       0 SAL_MAN,\n" );
		sql.append("                       0 TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		sql.append("                   AND TVP.POSITION = 99961004\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT VOD.DEALER_ID,\n" );
		sql.append("                       0 MGR,\n" );
		sql.append("                       COUNT(1) SALE_MGR,\n" );
		sql.append("                       0 MARK_MGR,\n" );
		sql.append("                       0 SAL_MAN,\n" );
		sql.append("                       0 TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		sql.append("                   AND TVP.POSITION = 99961003\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT VOD.DEALER_ID,\n" );
		sql.append("                       0 MGR,\n" );
		sql.append("                       0 SALE_MGR,\n" );
		sql.append("                       COUNT(1) MARK_MGR,\n" );
		sql.append("                       0 SAL_MAN,\n" );
		sql.append("                       0 TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		sql.append("                   AND TVP.POSITION = 99961002\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT VOD.DEALER_ID,\n" );
		sql.append("                       0 MGR,\n" );
		sql.append("                       0 SALE_MGR,\n" );
		sql.append("                       0 MARK_MGR,\n" );
		sql.append("                       COUNT(1) SAL_MAN,\n" );
		sql.append("                       0 TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		sql.append("                   AND TVP.POSITION = 99961001\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID\n" );
		sql.append("                UNION ALL\n" );
		sql.append("                SELECT VOD.DEALER_ID,\n" );
		sql.append("                       0 MGR,\n" );
		sql.append("                       0 SALE_MGR,\n" );
		sql.append("                       0 MARK_MGR,\n" );
		sql.append("                       0 SAL_MAN,\n" );
		sql.append("                       COUNT(1) TOTAL\n" );
		sql.append("                  FROM TT_VS_PERSON TVP, VW_ORG_DEALER_ALL VOD\n" );
		sql.append("                 WHERE TVP.DEALER_ID = VOD.DEALER_ID\n" );
		//如果条件中有联系电话
		if(tvprp.getMobile()!=null&&!"".equals(tvprp.getMobile())){
			sql.append("  AND 	TVP.MOBILE ='"+tvprp.getMobile()+"'");
		}
		//如果条件中有性别
		if(tvprp.getGender()!=null&&!"".equals(tvprp.getGender())){
			sql.append("  AND TVP.GENDER ="+tvprp.getGender());
		}
		//如果条件中有是否投资人
		if(tvprp.getDegree()!=null&&!"".equals(tvprp.getDegree())){
			sql.append("  AND TVP.degree ="+tvprp.getDegree());
		}
		//如果条件中有职位状态
		if(tvprp.getPositionStatus()!=null&&!"".equals(tvprp.getPositionStatus())){
			sql.append("  AND TVP.POSITION_STATUS ="+tvprp.getPositionStatus());
		}
		sql.append("                 GROUP BY VOD.DEALER_ID) TS\n" );
		sql.append("         GROUP BY DEALER_ID) TK,\n" );
		sql.append("       VW_ORG_DEALER_ALL VOD\n" );
		sql.append(" WHERE TK.DEALER_ID = VOD.DEALER_ID\n" );
		
		if(tvprp.getDealerId()!=null&&!"".equals(tvprp.getDealerId())){
			sql.append("  AND VOD.DEALER_ID ="+tvprp.getDealerId());
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String str=CommonUtils.getSplitStringForIn(dealerCodes);
			boolean flag=dealerCodes.contains(",");
			if(flag){
				sql.append(" AND  VOD.DEALER_CODE IN("+str+")");
			}
		}
		
		// 大区机构人员变动查询
		if ("10431003".equals(logonUser.getDutyType())) {
			sql.append("AND vod.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.ROOT_ORG_ID="+logonUser.getOrgId()+")");
			// 小区机构人员变动查询
		} else if ("10431004".equals(logonUser.getDutyType())) {
			sql.append("AND vod.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.PQ_ORG_ID="+logonUser.getOrgId()+")");
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			boolean flag=dealerCodes.contains(",");
			if(!flag){
				sql.append("  AND  TD.DEALER_ID IN (SELECT TD1.DEALER_ID\n" );
				sql.append("                         FROM TM_DEALER TD1\n" );
				sql.append("                        WHERE 1 = 1\n" );
				sql.append("                        START WITH TD1.DEALER_CODE IN ('"+dealerCodes+"')\n" );
				sql.append("                       CONNECT BY PRIOR TD1.DEALER_ID = TD1.PARENT_DEALER_D\n" );
				sql.append("                       )\n" );
			}
		}
		sql.append(" order by vod.PQ_ORG_CODE,vod.DEALER_CODE");
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	
	/**
	 * 人员关键岗位明细信息查询
	 * @param tvprp
	 */
	public PageResult<Map<String,Object>> personDetailSelect(AclUserBean logonUser,Map<String,String> m,String dealerCodes,int pageSize,int curPage) throws Exception{
		String leaveStartDate=m.get("leaveStartDate");
		String leaveEndDate=m.get("leaveEndDate");
		String auditStartDate= m.get("auditStartDate");
		String auditEndDate=m.get("auditEndDate");
		
		String degree=m.get("degree");
		String position= m.get("position");
		String position_status=m.get("position_status");
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT VOD.PQ_ORG_NAME,\n" );
		sql.append("       VOD.DEALER_CODE,\n" );
		sql.append("       VOD.DEALER_NAME,\n" );
		sql.append("       TC1.CODE_DESC POSITION,\n" );
		sql.append("       TVP.NAME,\n" );
		sql.append("       TC2.CODE_DESC GENDER,\n" );
		sql.append("       TVP.ENTRY_DATE,\n" );
		sql.append("       TVP.LEAVE_DATE,\n" );
		sql.append("       TVP.LAST_AUDIT_TIME,\n" );
		sql.append("       TC3.CODE_DESC POSITION_STATUS,\n" );
		sql.append("       TVP.MOBILE,\n" );
		sql.append("       TVP.EMAIL,\n" );
		sql.append("       TC4.CODE_DESC BANK,\n" );
		sql.append("       TVP.BANK_CARDNO,\n" );
		sql.append("       TVP.ID_NO,\n" );
		sql.append("       SUBSTR(TVP.ID_NO, 7, 4) || '-' || SUBSTR(TVP.ID_NO, 11, 2) || '-' ||\n" );
		sql.append("       SUBSTR(TVP.ID_NO, 13, 2) BIRTHDAY,\n" );
		sql.append("\n" );
		sql.append("       TC5.CODE_DESC DEGREE,\n" );
		sql.append("       TVP.PERFORMANCE_INTEG,\n" );
		sql.append("       TVP.AUTHENTICATION_INTEG,\n" );
		sql.append("       TVP.YEAR_INTEG,\n" );
		sql.append("       TO_NUMBER(NVL(TVP.PERFORMANCE_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.AUTHENTICATION_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.YEAR_INTEG, 0)) allPerfor,\n" );
		sql.append("       0 READY,\n" );
		sql.append("       TO_NUMBER(NVL(TVP.PERFORMANCE_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.AUTHENTICATION_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.YEAR_INTEG, 0)) UNREADY,\n" );
		sql.append("       TO_NUMBER(NVL(TVP.PERFORMANCE_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.AUTHENTICATION_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.YEAR_INTEG, 0)) ALLTOTAL\n" );
		sql.append("  FROM TT_VS_PERSON      TVP,\n" );
		sql.append("       TM_DEALER         TD,\n" );
		sql.append("       VW_ORG_DEALER_ALL VOD,\n" );
		sql.append("       TC_CODE           TC1,\n" );
		sql.append("       TC_CODE           TC2,\n" );
		sql.append("       TC_CODE           TC3,\n" );
		sql.append("       TC_CODE           TC4,\n" );
		sql.append("       TC_CODE           TC5\n" );
		sql.append("\n" );
		sql.append(" WHERE TD.DEALER_ID(+) = TVP.DEALER_ID\n" );
		sql.append("   AND VOD.DEALER_ID(+) = TD.DEALER_ID\n" );
		sql.append("   AND TC1.CODE_ID(+) = TVP.POSITION\n" );
		sql.append("   AND TC2.CODE_ID(+) = TVP.GENDER\n" );
		sql.append("   AND TC4.CODE_ID(+) = TVP.BANK\n" );
		sql.append("   AND TC5.CODE_ID(+) = TVP.DEGREE\n" );
		sql.append("   AND TC3.CODE_ID(+) = TVP.POSITION_STATUS\n" );
		
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String str=CommonUtils.getSplitStringForIn(dealerCodes);
				sql.append(" AND  VOD.DEALER_CODE IN("+str+") \n");
		}
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append(" AND TVP.LEAVE_DATE >=TO_DATE('"+leaveStartDate+"','YYYY-MM-DD') \n");
		}
		if(leaveEndDate!=null&&!"".equals(leaveEndDate)){
			sql.append(" AND TVP.LEAVE_DATE <=TO_DATE('"+leaveEndDate+"','YYYY-MM-DD') \n");
		}
		if(auditStartDate!=null&&!"".equals(auditStartDate)){
			sql.append(" AND TVP.LAST_AUDIT_TIME >=TO_DATE('"+auditStartDate+"','YYYY-MM-DD') \n");
		}
		if(auditEndDate!=null&&!"".equals(auditEndDate)){
			sql.append(" AND TVP.LAST_AUDIT_TIME <=TO_DATE('"+auditEndDate+"','YYYY-MM-DD') \n");
		}
		
		if(position!=null&&!"".equals(position)){
			sql.append(" AND TVP.position ="+position+" \n");
		}
		if(degree!=null&&!"".equals(degree)){
			sql.append(" AND TVP.degree ="+degree+" \n");
		}
		if(position_status!=null&&!"".equals(position_status)){
			sql.append(" AND TVP.position_status ="+position_status+" \n");
		}
		
		
		sql.append(" ORDER BY VOD.PQ_ORG_CODE, VOD.DEALER_SHORTNAME");
		PageResult<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return list;
	}
	/**
	 * 人员关键岗位明细信息查询下载
	 * @param tvprp
	 */
	public List<Map<String,Object>> personDetailDownLoad(AclUserBean logonUser,Map<String,String> m,String dealerCodes) throws Exception{
		String leaveStartDate=m.get("leaveStartDate");
		String leaveEndDate=m.get("leaveEndDate");
		String auditStartDate= m.get("auditStartDate");
		String auditEndDate=m.get("auditEndDate");
		
		String degree=m.get("degree");
		String position= m.get("position");
		String position_status=m.get("position_status");
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT VOD.PQ_ORG_NAME,\n" );
		sql.append("       VOD.DEALER_CODE,\n" );
		sql.append("       VOD.DEALER_NAME,\n" );
		sql.append("       TC1.CODE_DESC POSITION,\n" );
		sql.append("       TVP.NAME,\n" );
		sql.append("       TC2.CODE_DESC GENDER,\n" );
		sql.append("       TVP.ENTRY_DATE,\n" );
		sql.append("       TVP.LEAVE_DATE,\n" );
		sql.append("       TVP.LAST_AUDIT_TIME,\n" );
		sql.append("       TC3.CODE_DESC POSITION_STATUS,\n" );
		sql.append("       TVP.MOBILE,\n" );
		sql.append("       TVP.EMAIL,\n" );
		sql.append("       TC4.CODE_DESC BANK,\n" );
		sql.append("       TVP.BANK_CARDNO,\n" );
		sql.append("       TVP.ID_NO,\n" );
		sql.append("       SUBSTR(TVP.ID_NO, 7, 4) || '-' || SUBSTR(TVP.ID_NO, 11, 2) || '-' ||\n" );
		sql.append("       SUBSTR(TVP.ID_NO, 13, 2) BIRTHDAY,\n" );
		sql.append("\n" );
		sql.append("       TC5.CODE_DESC DEGREE,\n" );
		sql.append("       TVP.PERFORMANCE_INTEG,\n" );
		sql.append("       TVP.AUTHENTICATION_INTEG,\n" );
		sql.append("       TVP.YEAR_INTEG,\n" );
		sql.append("       TO_NUMBER(NVL(TVP.PERFORMANCE_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.AUTHENTICATION_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.YEAR_INTEG, 0)) ALLINTEG,\n" );
		sql.append("       0 READY,\n" );
		sql.append("       TO_NUMBER(NVL(TVP.PERFORMANCE_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.AUTHENTICATION_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.YEAR_INTEG, 0)) UNREADY,\n" );
		sql.append("       TO_NUMBER(NVL(TVP.PERFORMANCE_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.AUTHENTICATION_INTEG, 0)) +\n" );
		sql.append("       TO_NUMBER(NVL(TVP.YEAR_INTEG, 0)) ALLTOTAL\n" );
		sql.append("  FROM TT_VS_PERSON      TVP,\n" );
		sql.append("       TM_DEALER         TD,\n" );
		sql.append("       VW_ORG_DEALER_ALL VOD,\n" );
		sql.append("       TC_CODE           TC1,\n" );
		sql.append("       TC_CODE           TC2,\n" );
		sql.append("       TC_CODE           TC3,\n" );
		sql.append("       TC_CODE           TC4,\n" );
		sql.append("       TC_CODE           TC5\n" );
		sql.append("\n" );
		sql.append(" WHERE TD.DEALER_ID(+) = TVP.DEALER_ID\n" );
		sql.append("   AND VOD.DEALER_ID(+) = TD.DEALER_ID\n" );
		sql.append("   AND TC1.CODE_ID(+) = TVP.POSITION\n" );
		sql.append("   AND TC2.CODE_ID(+) = TVP.GENDER\n" );
		sql.append("   AND TC4.CODE_ID(+) = TVP.BANK\n" );
		sql.append("   AND TC5.CODE_ID(+) = TVP.DEGREE\n" );
		sql.append("   AND TC3.CODE_ID(+) = TVP.POSITION_STATUS\n" );
		
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			String str=CommonUtils.getSplitStringForIn(dealerCodes);
				sql.append(" AND  VOD.DEALER_CODE IN("+str+") \n");
		}
		if(leaveStartDate!=null&&!"".equals(leaveStartDate)){
			sql.append(" AND TVP.LEAVE_DATE >=TO_DATE('"+leaveStartDate+"','YYYY-MM-DD') \n");
		}
		if(leaveEndDate!=null&&!"".equals(leaveEndDate)){
			sql.append(" AND TVP.LEAVE_DATE <=TO_DATE('"+leaveEndDate+"','YYYY-MM-DD') \n");
		}
		if(auditStartDate!=null&&!"".equals(auditStartDate)){
			sql.append(" AND TVP.LAST_AUDIT_TIME >=TO_DATE('"+auditStartDate+"','YYYY-MM-DD') \n");
		}
		if(auditEndDate!=null&&!"".equals(auditEndDate)){
			sql.append(" AND TVP.LAST_AUDIT_TIME <=TO_DATE('"+auditEndDate+"','YYYY-MM-DD') \n");
		}
		
		if(position!=null&&!"".equals(position)){
			sql.append(" AND TVP.position ="+position+" \n");
		}
		if(degree!=null&&!"".equals(degree)){
			sql.append(" AND TVP.degree ="+degree+" \n");
		}
		if(position_status!=null&&!"".equals(position_status)){
			sql.append(" AND TVP.position_status ="+position_status+" \n");
		}
		
		
		sql.append(" ORDER BY VOD.PQ_ORG_CODE, VOD.DEALER_SHORTNAME");
		List<Map<String,Object>> list=pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
}
