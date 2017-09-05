package com.infodms.dms.dao.claim.dealerClaimMng;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.repairOrder.WrRuleUtil;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ActivityPartDetailBean;
import com.infodms.dms.bean.ClaimListBean;
import com.infodms.dms.bean.DealerCoverPrintBean;
import com.infodms.dms.bean.LabourPartBean;
import com.infodms.dms.bean.NewPartBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.bean.TtAsActivityProjectBean;
import com.infodms.dms.bean.TtAsRepairOrderExtBean;
import com.infodms.dms.bean.TtAsWrApplicationBean;
import com.infodms.dms.bean.TtAsWrApplicationExtBean;
import com.infodms.dms.bean.TtAsWrForeapprovalBean;
import com.infodms.dms.bean.TtAsWrLabouritemBean;
import com.infodms.dms.bean.TtAsWrPartsitemBean;
import com.infodms.dms.bean.TtAsWrWinterMaintenBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmBusinessChngCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDownParameterPO;
import com.infodms.dms.po.TmPtPartBaseExtPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TmVehicleExtendPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVehiclePinRequestPO;
import com.infodms.dms.po.TrBalanceClaimPO;
import com.infodms.dms.po.TtAsActivityNetitemPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityPartsPO;
import com.infodms.dms.po.TtAsActivityRelationPO;
import com.infodms.dms.po.TtAsActivityRepairitemExtPO;
import com.infodms.dms.po.TtAsRepairOrderBackupPO;
import com.infodms.dms.po.TtAsRepairOrderExtPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoLabourBean;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoManagePO;
import com.infodms.dms.po.TtAsRoRepairPartBean;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrAppauthitemPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrFeeRulePO;
import com.infodms.dms.po.TtAsWrFeeWarrantyPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;
import com.infodms.dms.po.TtAsWrForeapprovalitemPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrGamefeePO;
import com.infodms.dms.po.TtAsWrInformationqualityExtPO;
import com.infodms.dms.po.TtAsWrInformationqualityPO;
import com.infodms.dms.po.TtAsWrKeyDesignPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrMalfunctionBean;
import com.infodms.dms.po.TtAsWrMalfunctionPO;
import com.infodms.dms.po.TtAsWrMalfunctionPositionPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrModelItemPO;
import com.infodms.dms.po.TtAsWrNetitemExtPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infodms.dms.po.TtAsWrPartsitemExtPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrQamaintainPO;
import com.infodms.dms.po.TtAsWrQualityDamagePO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrVinPartRepairTimesPO;
import com.infodms.dms.po.TtAsWrVinPartRepairTimesvalPO;
import com.infodms.dms.po.TtAsWrVinRepairDaysPO;
import com.infodms.dms.po.TtAsWrVinRulePO;
import com.infodms.dms.po.TtAsWrWarrantyPO;
import com.infodms.dms.po.TtAsWrWarrantyRecordPO;
import com.infodms.dms.po.TtAsWrWrauthorizationPO;
import com.infodms.dms.po.TtAsWrWrlabinfoExtPO;
import com.infodms.dms.po.TtBillNoPO;
import com.infodms.dms.po.TtCustomerPO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.po.VwMaterialGroupPO;
import com.infodms.dms.po.VwMaterialGroupServicePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.constant.MyConstant;
import com.infoservice.dms.chana.vo.WarrantyPartVO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

/**
 * 
 * @ClassName: ClaimBillMaintainDAO
 * @Description: TODO(经销商索赔单维护DAO)
 * @author wangchao
 * @date Jun 5, 2010 3:37:38 PM
 * 
 */
@SuppressWarnings("unchecked")
public class ClaimBillMaintainDAO extends BaseDao {

	public static Logger logger = Logger.getLogger(ClaimBillMaintainDAO.class);
	private static final ClaimBillMaintainDAO dao = new ClaimBillMaintainDAO();
	MaterialGroupManagerDao dao1 =MaterialGroupManagerDao.getInstance();
	public static final ClaimBillMaintainDAO getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @Title: getWgroupId
	 * @Description: TODO(通过车型ID取得工时对应的车型组ID)
	 * @param
	 * @return 设定文件
	 * @return List<TtAsWrModelItemPO> 返回类型 
	 * @throws
	 */
	
	public void deletBalance(Date repotDate,Integer balanceYieldly,Long DealerId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select t.REMARK from tt_as_wr_claim_balance t\n" );
		sql.append(" where t.START_DATE <=to_date('"+ new java.sql.Date(repotDate.getTime())+"','yyyy-mm-dd') \n" );
		sql.append("   and t.END_DATE >= to_date('"+ new java.sql.Date(repotDate.getTime())+"','yyyy-mm-dd')\n" );
		sql.append("   and t.BALANCE_YIELDLY = "+balanceYieldly+"\n" );
		sql.append("   and t.DEALER_ID ="+DealerId );
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), null, this.getFunName());
		
		if( map != null && map.get("REMARK") != null)
		{
			sql = new StringBuffer();
			sql.append("UPDATE tt_as_wr_fine t set t.PAY_STATUS = 11511001 ,t.BALANCE_ODER = null  where t.BALANCE_ODER = '"+map.get("REMARK").toString()+"'");
			dao.update(sql.toString(), null);
			sql = new StringBuffer();
			sql.append("UPDATE tt_as_subjiet_evaluate t set t.PAY_STATUS = 11511001 ,t.BALANCE_ODER = null  where t.BALANCE_ODER = '"+map.get("REMARK").toString()+"'");
			dao.update(sql.toString(), null);
			sql = new StringBuffer();
			sql.append("DELETE from tt_as_wr_administrative_charge  t  where t.BALANCE_ODER = '"+map.get("REMARK").toString()+"'");
			dao.update(sql.toString(), null);
		}
		
		sql= new StringBuffer();
		sql.append("DELETE from TT_AS_WR_CLAIM_BALANCE_TMP t\n" );
		dao.delete(sql.toString(), null);
		
		sql= new StringBuffer();
		sql.append("DELETE from Tt_As_Wr_Claim_Send_Tem t\n" );
		dao.delete(sql.toString(), null);
		
		sql= new StringBuffer();
		sql.append("DELETE from Tt_As_Wr_Claim_Send t\n" );
		sql.append(" where t.STARTDATE <=to_date('"+ new java.sql.Date(repotDate.getTime())+"','yyyy-mm-dd') \n" );
		sql.append("   and t.ENDDATE >= to_date('"+ new java.sql.Date(repotDate.getTime())+"','yyyy-mm-dd')\n" );
		sql.append("   and t.BALANCE_YIELDLY = "+balanceYieldly+"\n" );
		sql.append("   and t.DEALER_ID  in(select d.dealer_id from tm_dealer d start with d.dealer_id = "+DealerId+" connect by PRIOR d.dealer_id = d.parent_dealer_d )" );
		dao.delete(sql.toString(), null);
		
		sql= new StringBuffer();
		sql.append("DELETE from tt_as_wr_claim_balance t\n" );
		sql.append(" where t.START_DATE <=to_date('"+ new java.sql.Date(repotDate.getTime())+"','yyyy-mm-dd') \n" );
		sql.append("   and t.END_DATE >= to_date('"+ new java.sql.Date(repotDate.getTime())+"','yyyy-mm-dd')\n" );
		sql.append("   and t.BALANCE_YIELDLY = "+balanceYieldly+"\n" );
		sql.append("   and t.DEALER_ID ="+DealerId );
		dao.delete(sql.toString(), null);
	}
	
	public PageResult<Map<String,Object>> get_payMent(String dealerCode,String dealerName,String balance_oder,String balance_yieldly,String startDate,String endDate,Long userID,AclUserBean logonUser ,int pageSize,int curPage)
	{
		  MaterialGroupManagerDao managerDao = new MaterialGroupManagerDao();
		  String sql1  =  managerDao.getOrgDealerLimitSqlByServiceA("A", logonUser);
		  String sql2  =  managerDao.getOrgDealerLimitSqlByServiceB("A", logonUser);
		List<TmDealerPO> list= this.select(TmDealerPO.class, sql2, null);
		
		
		StringBuffer sql= new StringBuffer();
		sql.append("select A.REMARK,\n" );
		sql.append("       max(A.DEALER_CODE) DEALER_CODE,\n" );
		sql.append("       max(A.DEALER_NAME) DEALER_NAME,\n" );
		sql.append("       max(to_char(A.START_DATE, 'yyyy-mm')) START_DATE,\n" );
		sql.append("       max(B.STATUS) STATUS,\n" );
		sql.append("       sum(A.AMOUNT_SUM) AMOUNT_SUM,\n" );
		sql.append("  MAX(B.remark) mark ");
		sql.append("  from tt_as_wr_claim_balance A, TT_AS_payment B\n" );
		sql.append(" where A.REMARK = B.BALANCE_ODER  and    A.BALANCE_YIELDLY = "+balance_yieldly+"\n" );
		if(Utility.testString(dealerCode))
		{
			sql.append(" AND A.DEALER_CODE LIKE '"+dealerCode+"'");
		}
		
		if(Utility.testString(startDate))
		{
			sql.append(  "  and B.CREAT_DATE >= to_date('"+startDate+"','yyyy-mm-dd')");
		}
		if(Utility.testString(endDate))
		{
			sql.append(  "  and B.CREAT_DATE <= (to_date('"+endDate+"','yyyy-mm-dd')+1)");		
		}
		if(Utility.testString(dealerName))
		{
			sql.append(" AND A.DEALER_NAME LIKE '"+dealerName+"'");
		}
		if(Utility.testString(balance_oder))
		{
			sql.append(" AND A.REMARK LIKE '"+balance_oder+"'");
		}
		if(list.size() > 0 )
		{
			sql.append(sql1.toString());
		}
		sql.append(" GROUP by A.REMARK");
		sql.append(" order by MAX(B.CREAT_DATE) desc ");
		PageResult<Map<String, Object>> result = (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
		return result;
	}
	
	public List<Map<String,Object>> get_payMent(String dealerCode,String dealerName,String balance_oder,String balance_yieldly,String startDate,String endDate,AclUserBean logonUser)
	{
		  MaterialGroupManagerDao managerDao = new MaterialGroupManagerDao();
		  String sql1  =  managerDao.getOrgDealerLimitSqlByServiceA("A", logonUser);
		  String sql2  =  managerDao.getOrgDealerLimitSqlByServiceB("A", logonUser);
		List<TmDealerPO> list= this.select(TmDealerPO.class, sql2, null);
		
		StringBuffer sql= new StringBuffer();
		sql.append("select min(B.BALANCE_ODER) BALANCE_ODER , min(B.REMARK) REMARK,MAX(B.SERIAL_NUMBER) SERIAL_NUMBER,min(B.TAX_RATE) TAX_RATE, min(B.LABOUR_RECEIPT) LABOUR_RECEIPT,min(B.PART_RECEIPT) PART_RECEIPT ,to_char( min(B.CREAT_DATE), 'yyyy-mm-dd' ) CREAT_DATE ,\n" );
		sql.append("       max(A.DEALER_CODE) DEALER_CODE,\n" );
		sql.append("       max(A.DEALER_NAME) DEALER_NAME,\n" );
		sql.append("   max(A.APPLY_PERSON_NAME) APPLY_PERSON_NAME, ");
		sql.append("       max(to_char(A.START_DATE, 'yyyymm')) START_DATE,\n" );
		sql.append("       sum(A.AMOUNT_SUM) AMOUNT_SUM,\n" );
		
		sql.append("sum(nvl(A.LABOUR_AMOUNT, 0) + nvl(A.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(A.FREE_LABOUR_AMOUNT, 0) + nvl(A.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(A.PLUS_MINUS_LABOUR_SUM, 0) +nvl(A.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(A.PACKGE_CHANGE_SUM, 0)-nvl(A.BARCODE_SUM, 0)+nvl(A.CHECK_DEDUCT, 0) + nvl(A.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(A.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(A.SERVICE_OTHER_AMOUNT, 0) \n" );
		sql.append("           ) LABOUR_AMOUNT\n" );
		sql.append("  from tt_as_wr_claim_balance A, TT_AS_payment B\n" );
		sql.append(" where A.REMARK = B.BALANCE_ODER  and    A.BALANCE_YIELDLY = "+balance_yieldly+"\n" );
		if(Utility.testString(dealerCode))
		{
			sql.append(" AND A.DEALER_CODE LIKE '"+dealerCode+"'");
		}
		
		if(Utility.testString(startDate))
		{
			sql.append(  "  and B.CREAT_DATE >= to_date('"+startDate+"','yyyy-mm-dd')");
		}
		if(Utility.testString(endDate))
		{
			sql.append(  "  and B.CREAT_DATE <= (to_date('"+endDate+"','yyyy-mm-dd')+1)");		
		}
		if(Utility.testString(dealerName))
		{
			sql.append(" AND A.DEALER_NAME LIKE '"+dealerName+"'");
		}
		if(Utility.testString(balance_oder))
		{
			sql.append(" AND A.REMARK LIKE '"+balance_oder+"'");
		}
		if(list.size() > 0 )
		{
			sql.append( sql1.toString() );
		}
		sql.append(" GROUP by A.REMARK");
		sql.append("  ORDER by to_number(MAX(B.SERIAL_NUMBER)),MAX(B.SERIAL_NUMBER) asc ");
		List<Map<String, Object>> result = super.pageQuery(sql.toString(),null, this.getFunName());
		return result;
	}
	
	
	public PageResult<Map<String,Object>> get_payMentOk(String dealerCode,String dealerName,String balance_oder,String balance_yieldly,int pageSize,int curPage)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select A.REMARK,\n" );
		sql.append("       max(A.DEALER_CODE) DEALER_CODE,\n" );
		sql.append("       max(A.DEALER_NAME) DEALER_NAME,\n" );
		sql.append("       max(to_char(A.START_DATE, 'yyyy-mm')) START_DATE,\n" );
		sql.append("       max(B.STATUS) STATUS,\n" );
		sql.append("       sum(A.AMOUNT_SUM) AMOUNT_SUM,\n" );
		sql.append("  MAX(B.remark) mark ");
		sql.append("  from tt_as_wr_claim_balance A, TT_AS_payment B\n" );
		sql.append(" where A.REMARK = B.BALANCE_ODER  and B.STATUS != 0 AND   A.BALANCE_YIELDLY = "+balance_yieldly+"\n" );
		if(Utility.testString(dealerCode))
		{
			sql.append(" AND A.DEALER_CODE LIKE '"+dealerCode+"'");
		}
		if(Utility.testString(dealerName))
		{
			sql.append(" AND A.DEALER_NAME LIKE '"+dealerName+"'");
		}
		if(Utility.testString(balance_oder))
		{
			sql.append(" AND A.REMARK LIKE '"+balance_oder+"'");
		}
		
		sql.append(" GROUP by A.REMARK");
		sql.append("  order by  max(B.STATUS) asc ");
		PageResult<Map<String, Object>> result = (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
		return result;
	}
	
	public BigDecimal getWinterAmount(Long dealerID,String date) {
		StringBuffer sb= new StringBuffer();
		sb.append("select tt.amount\n" );
		sb.append("  from TT_AS_WR_WINTER_MAINTEN tt\n" );
		sb.append("  left join TT_AS_WR_WINTER_MAINTEN_DEALER tt2\n" );
		sb.append("    on tt.id = tt2.id\n" );
		sb.append(" where tt2.dealer_id ="+dealerID+"\n");
		DaoFactory.getsql(sb, "tt.start_date", date, 4);
		DaoFactory.getsql(sb, "tt.end_date", date, 3);
		sb.append(" order by tt.create_date desc");
		BigDecimal bg = new BigDecimal(0);
		List<Map<String,Object>> list = pageQuery(sb.toString(), null,getFunName());
		if (list != null && list.size()>0) {
			bg= (BigDecimal) list.get(0).get("AMOUNT");
		}
		return bg;
	}
	
	public List<Map<String,Object>> getMessge(String balance_oder)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" select B.DEALER_CODE,B.DEALER_NAME,B.REMARK,B.DEALER_ID,B.START_DATE,B.CREATE_BY");
		sql.append(" ,(B.AMOUNT_SUM + nvl(D.datum_sum,0) - nvl(E.datum_sum,0) + nvl(D.labour_sum,0) -  nvl(E.labour_sum,0)) AMOUNT_SUM  ");
		sql.append(" ,(B.LABOUR_AMOUNT + nvl(D.labour_sum,0) -  nvl(E.labour_sum,0) ) LABOUR_AMOUNT ");
		sql.append(" from ");
		sql.append("  (select min(t.DEALER_CODE) DEALER_CODE,\n" );
		sql.append("       min(t.DEALER_NAME) DEALER_NAME,\n" );
		sql.append("       t.REMARK, min(t.DEALER_ID) DEALER_ID, \n" );
		sql.append("       min(to_char(t.START_DATE, 'yyyy-mm')) START_DATE,\n" );
		sql.append("       min(t.CREATE_BY) CREATE_BY,\n" );
		sql.append("       min(t.charge_id) charge_id,");
		sql.append("       sum(t.AMOUNT_SUM) AMOUNT_SUM,\n" );
		sql.append("sum(nvl(t.LABOUR_AMOUNT, 0) + nvl(t.OTHER_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.FREE_LABOUR_AMOUNT, 0) + nvl(t.RETURN_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.PLUS_MINUS_LABOUR_SUM, 0) +nvl(t.SPECIAL_LABOUR_SUM, 0)\n" );
		sql.append("           - nvl(t.PACKGE_CHANGE_SUM, 0)-nvl(t.BARCODE_SUM, 0)+nvl(t.CHECK_DEDUCT, 0) + nvl(t.SERVICE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("           nvl(t.SERVICE_TOTAL_AMOUNT_MARKET, 0) +\n" );
		sql.append("           nvl(t.SERVICE_OTHER_AMOUNT, 0) \n" );
		sql.append("           ) LABOUR_AMOUNT\n" );
		sql.append("  from tt_as_wr_claim_balance t\n" );
		sql.append(" where t.REMARK = '"+balance_oder+"'\n" );
		sql.append(" GROUP by t.REMARK\n");
		sql.append( "UNION ALL\n");
		sql.append( "         SELECT MIN(T.DEALER_CODE) DEALER_CODE,\n" ); 
		sql.append( "               MIN(T.DEALER_NAME) DEALER_NAME,\n" );
		sql.append(	"               T.REMARK,\n" );
		sql.append(	"               MIN(T.DEALER_ID) DEALER_ID,\n" );
		sql.append(	"               MIN(TO_CHAR(T.CREATE_DATE, 'yyyy-mm')) START_DATE,\n" );
		sql.append(	"               MIN(T.CREATE_BY) CREATE_BY,\n" );
		sql.append(	"               MIN(T.CHARGE_ID) CHARGE_ID,\n" );
		sql.append(	"               SUM(T.AMOUNT_SUM) AMOUNT_SUM,\n" );
		sql.append(	"               SUM(NVL(T.PLUS_MINUS_LABOUR_SUM, 0)) LABOUR_AMOUNT\n" );
		sql.append(	"          FROM Tt_As_Wr_Claim_Balance_Fine t\n" );
		sql.append(	"         WHERE T.REMARK = '"+balance_oder+"'\n" );
		sql.append( "         GROUP BY T.REMARK) B");
		sql.append(" LEFT join tt_as_wr_administrative_charge D on D.BALANCE_ODER = B.REMARK ");
		sql.append(" LEFT join tt_as_wr_administrative_charge E on E.id = B.charge_id ");
		return this.pageQuery(sql.toString(), null,this.getFunName());
	}
	
	public String number_ap(String acent)
	{
		Date date = new Date();
		String year = (""+date.getYear()).substring((""+date.getYear()).length()-2);
		String month = ""+date.getMonth();
		TtBillNoPO ttBillNoPO = new TtBillNoPO();
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from tt_bill_no t where t.YEAR = '"+year+"' and  t.MONTH= '"+month+"' and  t.DEALER_ID= '"+acent+"' ");
		List<TtBillNoPO> list= dao.select(TtBillNoPO.class,sql.toString(),null);
		if(list.size() > 0)
		{
			ttBillNoPO = list.get(0);
			String num = ""+ttBillNoPO.getId();
			for(int i = num.length() ;i < 5;i++)
			{
				num = "0"+num;
			}
			TtBillNoPO ttBillNoPO1 = new TtBillNoPO();
			ttBillNoPO1.setBillNoId(ttBillNoPO.getBillNoId());
			TtBillNoPO ttBillNoPO2 = new TtBillNoPO();
			ttBillNoPO2.setId(1l+Long.parseLong(num));
			dao.update(ttBillNoPO1, ttBillNoPO2);
			return acent+year+month +num;
			
		}else
		{
			ttBillNoPO.setBillNoId(Long.parseLong(SequenceManager.getSequence("")));
			ttBillNoPO.setMonth(month);
			ttBillNoPO.setYear(year);
			ttBillNoPO.setDealerId(acent);
			ttBillNoPO.setId(2l);
			dao.insert(ttBillNoPO);
		    return acent+year+month +"00001";
		}
		
		
	}                  
	
	public void updateaplcount( String Date , String dealerid ,String balance_yieldly)
	{
		StringBuffer sql= new StringBuffer();
		sql.append(" update tt_as_wr_application t set t.IS_LETTER = 1 ");
		sql.append(" where to_char(t.REPORT_DATE , 'yyyymm') = '"+Date+"' \n" );
		sql.append("   and t.BALANCE_YIELDLY = "+balance_yieldly+"\n" );
		sql.append("   and t.CLAIM_TYPE = 10661006\n" );
		sql.append(" and t.DEALER_ID =" + dealerid);
		sql.append("   and t.CAMPAIGN_CODE in\n" );
		sql.append("       (SELECT D.ACTIVITY_CODE\n" );
		sql.append("          from TT_AS_ACTIVITY D, TT_AS_ACTIVITY_SUBJECT E\n" );
		sql.append("         where D.SUBJECT_ID = E.SUBJECT_ID\n" );
		sql.append("           and E.ACTIVITY_TYPE = 10561001)\n" );
		dao.update(sql.toString(), null);
		sql = new StringBuffer();
		sql.append(" update tt_as_wr_application t set t.IS_LETTER = 1 ");
		sql.append(" where to_char(t.REPORT_DATE , 'yyyymm') = '"+Date+"' \n" );
		sql.append("   and t.BALANCE_YIELDLY = "+balance_yieldly+"\n" );
		sql.append(" and t.DEALER_ID =" + dealerid);
		sql.append("   and t.CLAIM_TYPE != 10661006 ");
		dao.update(sql.toString(), null);
		
		
	}
	public Double[] gettickets(String[] goodsnums)
	{
		String goodsnum = "(";
		for(int i = 0 ; i < goodsnums.length ; i++ )
		{
			if( i == goodsnums.length-1 )
			{
				goodsnum = goodsnum+ "'"+goodsnums[i] + "')";
			}else
			{
				goodsnum =goodsnum+ "'"+goodsnums[i]+"',";
			}
		}
		StringBuffer sql= new StringBuffer();
		sql.append("select sum(t.CARRIAGE) CARRIAGE, sum(t.SUM_CARRIAGE) SUM_CARRIAGE, sum(t.DA_CARRIAGE) DA_CARRIAGE \n" );
		sql.append("  from tt_as_wr_tickets t\n" );
		sql.append(" where t.GOODSNUM in "+goodsnum+" \n" );
		sql.append("   and t.BALANCE_YIELDLY = 95411001");
		Map<String,Object> map= pageQueryMap(sql.toString(), null, this.getFunName());
		Double[] d = new Double[3];
		if(map != null && map.get("CARRIAGE") != null)
		{
			d[0] = Double.parseDouble(map.get("CARRIAGE").toString());
			d[1] = Double.parseDouble(map.get("SUM_CARRIAGE").toString());
			d[2] = Double.parseDouble(map.get("DA_CARRIAGE").toString());
		}else
		{
			d[0] = Double.parseDouble("0");
			d[1] = Double.parseDouble("0");
			d[2] = Double.parseDouble("0");
		}
		return d;
	}
	
	public Double[] getyieldly(String dealerid,String sumcarriage,String[] goodsnums)
	{
		String date = "(";
		for(int i = 0 ; i < goodsnums.length; i++ )
		{
			if(i == goodsnums.length-1 )
			{
				 date = date+"'"+ goodsnums[i].substring(goodsnums[i].length()-6)+"')";
			}else
			{
				 date = date+"'"+ goodsnums[i].substring(goodsnums[i].length()-6)+"',";
			}
		}
		 
		
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT sum(A.BALANCE_AMOUNT) BALANCE_AMOUNT,A.BALANCE_YIELDLY   from  \n" );
		sql.append("(select sum(t.BALANCE_AMOUNT) BALANCE_AMOUNT,t.BALANCE_YIELDLY \n" );
		sql.append("  from tt_as_wr_application t\n" );
		sql.append(" where to_char(t.REPORT_DATE,'yyyymm') in "+ date );
		sql.append("   and t.CLAIM_TYPE = 10661006\n" );
		sql.append(" and t.DEALER_ID =" + dealerid);
		sql.append("   and t.CAMPAIGN_CODE in\n" );
		sql.append("       (SELECT D.ACTIVITY_CODE\n" );
		sql.append("          from TT_AS_ACTIVITY D, TT_AS_ACTIVITY_SUBJECT E\n" );
		sql.append("         where D.SUBJECT_ID = E.SUBJECT_ID\n" );
		sql.append("           and E.ACTIVITY_TYPE = 10561001)\n" );
		sql.append("  GROUP by t.BALANCE_YIELDLY ");
		sql.append(" UNION all\n" );
		sql.append(" select sum(t.BALANCE_AMOUNT) BALANCE_AMOUNT,t.BALANCE_YIELDLY \n" );
		sql.append("  from tt_as_wr_application t\n" );
		sql.append(" where to_char(t.REPORT_DATE,'yyyymm') in "+ date );
		sql.append(" and t.DEALER_ID =" + dealerid);
		sql.append("   and t.CLAIM_TYPE != 10661006 GROUP by t.BALANCE_YIELDLY ) A");
		sql.append(" GROUP by A.BALANCE_YIELDLY");
		List<Map<String,Object>>  list= pageQuery(sql.toString(), null, getFunName());
		Double[] yieldlysum = new Double[2];
		yieldlysum[0]  = 0d;
		yieldlysum[1]  = 0d;
		if(list.size() > 0)
		{
			
			for(Map<String,Object> map : list )
			{
				if(map.get("BALANCE_YIELDLY").toString().equals("95411001"))
				{
					yieldlysum[0] = Double.parseDouble(map.get("BALANCE_AMOUNT").toString());
				}
				if(map.get("BALANCE_YIELDLY").toString().equals("95411002"))
				{
					yieldlysum[1] = Double.parseDouble(map.get("BALANCE_AMOUNT").toString());
				}
			}
			
			Double y=  Arith.div (yieldlysum[0] , yieldlysum[0]+yieldlysum[1],2) ;
			yieldlysum[0] =Arith.round( Arith.mul( Double.parseDouble(sumcarriage),y),2);
			yieldlysum[1] = Arith.sub(Double.parseDouble(sumcarriage), yieldlysum[0]) ;
		}else
		{
			yieldlysum[0] = 0d;
			yieldlysum[1] = 0d;
		}
		
		return yieldlysum;
	}
	
	public boolean boolticket(String dealerid,String balance_yieldly,String create_date)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select * from\n" );
		sql.append("tt_as_wr_old_returned d,\n" );
		sql.append("Tt_As_Wr_Old_Returned_Detail dd\n"); 
		sql.append("where d.dealer_id="+dealerid+" \n" );
		sql.append("and d.yieldly ="+balance_yieldly+" \n" );
		sql.append("and d.status !=10811005");
		sql.append("and d.id= dd.return_id\n");
		sql.append("and dd.claim_no in (\n");
		sql.append("    select  t.claim_no  from tt_as_wr_application t where  to_char(t.report_date,'yyyy-mm')  = '"+create_date+"'\n");
		sql.append("    and t.dealer_id = "+dealerid+"\n");
		sql.append(" )"); 

		List list= dao.pageQuery(sql.toString(), null, this.getFunName());
		
		sql= new StringBuffer();
		sql.append("select * from\n");
		sql.append("tt_as_wr_old_returned d,\n");
		sql.append("Tt_As_Wr_Old_Returned_Detail dd\n");
		sql.append("where d.dealer_id="+dealerid+"\n");
		sql.append("and d.id= dd.return_id\n");
		sql.append("and d.yieldly ="+balance_yieldly+"\n");
		sql.append("and dd.claim_no in (\n");
		sql.append("    select  t.claim_no  from tt_as_wr_application t where  to_char(t.report_date,'yyyy-mm')  =  '"+create_date+"'\n");
		sql.append("    and t.dealer_id = "+dealerid+"\n");
		sql.append(" )"); 
		
		List list1= dao.pageQuery(sql.toString(), null, this.getFunName());
		if(list.size() > 0 || list1.size() == 0  )
		{
			return false;
		}else
		{
			return true;
		}
	}
	
	
	public String  aplcount(String date , String dealerid ,String balance_yieldly)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT Sum(A.aplcount) as aplcount from  \n" );
		sql.append("(select count(*) as aplcount\n" );
		sql.append("  from tt_as_wr_application t\n" );
		sql.append(" where to_char(t.REPORT_DATE,'yyyymm') = '"+date+"'" );
		sql.append("   and t.BALANCE_YIELDLY = "+balance_yieldly+"\n" );
		sql.append("   and t.CLAIM_TYPE = 10661006\n" );
		sql.append(" and t.DEALER_ID =" + dealerid);
		sql.append("   and t.CAMPAIGN_CODE in\n" );
		sql.append("       (SELECT D.ACTIVITY_CODE\n" );
		sql.append("          from TT_AS_ACTIVITY D, TT_AS_ACTIVITY_SUBJECT E\n" );
		sql.append("         where D.SUBJECT_ID = E.SUBJECT_ID\n" );
		sql.append("           and E.ACTIVITY_TYPE = 10561001)\n" );
		sql.append(" UNION all\n" );
		sql.append(" select count(*) aplcount\n" );
		sql.append("  from tt_as_wr_application t\n" );
		sql.append(" where to_char(t.REPORT_DATE,'yyyymm') = '"+date+"'" );
		sql.append("   and t.BALANCE_YIELDLY = "+balance_yieldly+"\n" );
		sql.append(" and t.DEALER_ID =" + dealerid);
		sql.append("   and t.CLAIM_TYPE != 10661006) A");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list.size() > 0)
		{
			String aplcount = list.get(0).get("APLCOUNT").toString();
			return aplcount;
		}else
		{
			return "0";
		}
	}
	
	
	public List<TtAsWrModelItemPO> getWgroupId(String modelId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tt_as_wr_MODEL_ITEM\n");
		sql.append("WHERE model_id=" + modelId + "\n");
		sql
				.append("and WRGROUP_ID in (select WRGROUP_ID from tt_as_wr_model_group where wrgroup_type="
						+ Constant.WR_MODEL_GROUP_TYPE_01 + ")");
		List<TtAsWrModelItemPO> list = select(TtAsWrModelItemPO.class, sql.toString(), null);
		return list;
	}
    public List<String> getpart(List<String> codelist)
    {
    	ArrayList<String> list = new ArrayList<String>();
    	StringBuffer sb= new StringBuffer();
		sb.append("(");
		if(codelist != null && codelist.size()>0)
		{
			for(int i = 0 ; i < codelist.size(); i++)
			{
				if( i == codelist.size()-1)
				{
					sb.append("'"+codelist.get(i)+"')");
				}else
				{
					sb.append("'"+codelist.get(i)+"',");
				}
			}
			StringBuffer sql= new StringBuffer();
	    	sql.append(" select t.pos_id\n" );
	    	sql.append("  from tm_pt_part_base t,\n" );
	    	sql.append("       (select max(t.part_id) as part_id, t.pos_id\n" );
	    	sql.append("          from tm_pt_part_base t\n" );
	    	sql.append("         where t.pos_id is not null\n" );
	    	sql.append("         group by t.pos_id) m\n" );
	    	sql.append("  where t.part_id = m.part_id\n" );
	    	sql.append("   and t.part_code in\n" );
	    	sql.append(sb.toString());
	    	List<TmPtPartBasePO> baselist= dao.select(TmPtPartBasePO.class, sql.toString(), null); 
	    	if(baselist != null && baselist.size()>0)
	    	{
	    		StringBuffer sbPos= new StringBuffer();
	    		for(int j = 0 ;j < baselist.size(); j++)
	    		{
	    		   if(j == baselist.size()-1)
	    		   {
	    			   sbPos.append(""+baselist.get(0).getPosId());
	    		   }else{
	    			   sbPos.append(""+baselist.get(0).getPosId()+",");
	    		   }	
	    		}
	    		StringBuffer sqlPos= new StringBuffer();
	    		sqlPos.append("   select t.POS_CODE from tt_as_wr_malfunction_position t where t.POS_ID in("+sbPos.toString()+")");
	    		List<TtAsWrMalfunctionPositionPO> poslist = dao.select(TtAsWrMalfunctionPositionPO.class,sqlPos.toString(),null);
	    		if(poslist != null && poslist.size()>0)
	    		{
	    			for(TtAsWrMalfunctionPositionPO positionPO : poslist)
	    			{
	    				list.add(positionPO.getPosCode());
	    			}
	    		}
	    	}
		}
    	return list;
    }
	
	public List<TtAsWrVinPartRepairTimesvalPO> getCodes(String vin ,List<String> codelist)
	{
		StringBuffer sb= new StringBuffer();
		if(codelist != null && codelist.size()>0)
		{
			sb.append("(");
			for(int i = 0 ; i < codelist.size(); i++)
			{
				if( i == codelist.size()-1)
				{
					sb.append("'"+codelist.get(i).trim()+"')");
				}else
				{
					sb.append("'"+codelist.get(i).trim()+"',");
				}
			}
		}
		if(sb.toString().length()>0){
			StringBuffer sql= new StringBuffer();
			sql.append("select t.PART_CODE,t.VIN, t.PART_NAME,t.WR_END_DATE,(nvl(t.PART_WR_TYPE,"+Constant.PART_WR_TYPE_1+")) as PART_WR_TYPE,t.CUR_MILEAGE,t.PART_WR_TYPE,(nvl(t.CUR_TIMES, 0) ) as CUR_TIMES ,(nvl(t.CUR_TIMES, 0) + 1) as NEXT_CUR_TIMES\n" );
			sql.append("  from tt_as_wr_vin_part_repair_times t\n" );
			sql.append(" where t.VIN = '"+vin+"'\n" );
			sql.append("   and t.PART_CODE in "+sb.toString());
			List<TtAsWrVinPartRepairTimesvalPO> list = dao.select(TtAsWrVinPartRepairTimesvalPO.class, sql.toString(), null);
			if(list != null && list.size()>0)
			{
				for( int i = 0;i< list.size(); i++)
				{
					 TtAsWrVinPartRepairTimesvalPO timesvalPO = list.get(i);
					  int CurTimes= timesvalPO.getCurTimes();
					  int nextCurTimes = timesvalPO.getNextCurTimes();
					  StringBuffer sqlCur= new StringBuffer();
					  StringBuffer sqlCur1= new StringBuffer();
					  sqlCur.append("select * from tt_as_wr_vin_rule t where t.PART_WR_TYPE="+ timesvalPO.getPartWrType() + " and t.VR_WARRANTY <="+ CurTimes);
					  sqlCur1.append("select * from tt_as_wr_vin_rule t where t.PART_WR_TYPE="+ timesvalPO.getPartWrType() + " and t.VR_WARRANTY <="+ nextCurTimes);
					if((""+timesvalPO.getPartWrType()).equals(Constant.PART_WR_TYPE_2))
					{
						sqlCur.append(" and t.VR_PART_CODE = '"+timesvalPO.getPartCode()+"'");
						sqlCur1.append(" and t.VR_PART_CODE = '"+timesvalPO.getPartCode()+"'");
					}
					sqlCur.append("   order by t.VR_WARRANTY desc");
					sqlCur1.append("  order by t.VR_WARRANTY desc ");
					List<TtAsWrVinRulePO> listr = dao.select(TtAsWrVinRulePO.class, sqlCur.toString(), null);
					List<TtAsWrVinRulePO> listr1 = dao.select(TtAsWrVinRulePO.class, sqlCur1.toString(), null);
					boolean falg = false;
					if(listr != null && listr.size() >0)
					{
						timesvalPO.setLevel(listr.get(0).getVrLevel());
						falg = true;
					}
					if(listr1 != null && listr1.size() >0)
					{
						System.out.println(listr1.get(0).getVrLevel());
						timesvalPO.setNextLevel(listr1.get(0).getVrLevel());
						falg = true;
					}
					if(falg)
					{
						list.set(i, timesvalPO);
					}
				}
			}
			
			return list;
		}
		return null;
	}
	
	public  PageResult<Map<String,Object>> querytickets(String balance_yieldly,String dealerName,String startDate,String endDate,String DealerId,AclUserBean logonUser,Integer curPage,Integer pageSize)
	{
		
		StringBuffer sql= new StringBuffer();
		sql.append("select t.CARRIAGE,\n" );
		sql.append(" t.sum_CARRIAGE ,\n" );
		sql.append(" t.da_CARRIAGE ,\n" );
		sql.append("       t.DEALERNAME,\n" );
		sql.append("       t.LETTER,\n" );
		sql.append("       t.GOODSNUM,\n" );
		sql.append("       t.APLCOUNT,\n" );
		sql.append("       to_char(t.STARTDATE,'yyyy-mm-dd') as STARTDATE,\n" );
		sql.append("       to_char(t.ENDDATE,'yyyy-mm-dd') as ENDDATE,\n" );
		sql.append("       t.ID\n" );
		sql.append("  from tt_as_wr_tickets t\n" );
		sql.append("  where t.BALANCE_YIELDLY =" + balance_yieldly);
		if(Utility.testString(dealerName))
		{
			sql.append(" and t.DEALERNAME like '%"+dealerName+"%'");
		}
		if(Utility.testString(startDate))
		{
			sql.append(" and t.STARTDATE >= to_date('"+startDate+"','yyyy-mm-dd') ");
		}
		if(Utility.testString(endDate))
		{
			sql.append(" and t.ENDDATE <= to_date('"+endDate+"','yyyy-mm-dd') ");
		}
		if(balance_yieldly.equals("95411001"))
		{
			  MaterialGroupManagerDao managerDao = new MaterialGroupManagerDao();
			 	sql.append(managerDao.getOrgDealerLimitSqlByServiceC("t", logonUser));

		}
		
		
		
		sql.append(" order by id desc ");
		
		PageResult<Map<String, Object>> result = (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
		return result;

	}
	
	
	
	
	public List<TtAsWrVinPartRepairTimesvalPO> getCodesNull(String vin ,List<String> codelist, int type)
	{
		StringBuffer sb= new StringBuffer();
		if(codelist != null && codelist.size()>0)
		{
			sb.append("(");
			for(int i = 0 ; i < codelist.size(); i++)
			{
				if( i == codelist.size()-1)
				{
					sb.append("'"+codelist.get(i)+"')");
				}else
				{
					sb.append("'"+codelist.get(i)+"',");
				}
			}
		}
		StringBuffer sql= new StringBuffer();
		sql.append("select t.PART_CODE,t.VIN, t.PART_NAME,t.WR_END_DATE,(nvl(t.PART_WR_TYPE,"+Constant.PART_WR_TYPE_1+")) as PART_WR_TYPE,t.CUR_MILEAGE, (nvl(t.CUR_TIMES, 0)) as CUR_TIMES\n" );
		sql.append("  from tt_as_wr_vin_part_repair_times t\n" );
		sql.append(" where t.VIN = '"+vin+"'\n" );
		
		if(sb.toString().length()>0)
		{
			sql.append("   and t.PART_CODE not in "+sb.toString());
		}
		if(type == 1)
		{
			sql.append("   and (t.PART_WR_TYPE="+ Constant.PART_WR_TYPE_1 +" or t.PART_WR_TYPE = "+ Constant.PART_WR_TYPE_2 + ")");
		}else if(type == 2)
		{
			sql.append("   and t.PART_WR_TYPE is null");
		}else{
			sql.append("   and t.PART_WR_TYPE="+ Constant.PART_WR_TYPE_3 );
		}
		List<TtAsWrVinPartRepairTimesvalPO> list = dao.select(TtAsWrVinPartRepairTimesvalPO.class, sql.toString(), null);
		
		if(list != null && list.size()>0)
		{
			for( int i = 0;i< list.size(); i++)
			{
				 TtAsWrVinPartRepairTimesvalPO timesvalPO = list.get(i);
				  int CurTimes= timesvalPO.getCurTimes();
				  StringBuffer sqlCur= new StringBuffer();
				  sqlCur.append("select * from tt_as_wr_vin_rule t where t.PART_WR_TYPE="+ timesvalPO.getPartWrType() + " and t.VR_WARRANTY <="+ CurTimes);
				if((""+timesvalPO.getPartWrType()).equals(Constant.PART_WR_TYPE_2))
				{
					sqlCur.append(" and t.VR_PART_CODE = '"+timesvalPO.getPartCode()+"'");
				}
				sqlCur.append("   order by t.VR_WARRANTY desc");
				List<TtAsWrVinRulePO> listr = dao.select(TtAsWrVinRulePO.class, sqlCur.toString(), null);
				if(listr != null && listr.size() >0)
				{
					timesvalPO.setLevel(listr.get(0).getVrLevel());
					list.set(i, timesvalPO);
				}
			}
		}
		return list;
	}
	
	
	
	/**
	 * 
	 * @Title: getVin
	 * @Description: TODO(取得VIN码)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public PageResult<TmVehicleExtendPO> getVin(Map<String, String> map,
			int pageSize, int curPage) {

		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT V.license_no,V.VIN,V.IS_PDI,V.engine_no,V.mileage,V.purchased_date,V.product_date,V.color,V.package_id,V.model_id,V.vehicle_id,to_char(vi.create_date,'yyyy-mm-dd hh24:mi') in_store_date,c.ctm_name as customer_name,wu.rule_code,a.order_id, c.main_phone,a.car_charactor car_use_type,TC.CODE_DESC car_use_desc,  c. ctm_name,c.OTHER_PHONE,c.Address,vw.brand_name as brand_name,vw.brand_code as brand_code,vw.series_name AS series_name,vw.series_code AS series_code,vw.model_name AS model_name,vw.model_code AS model_code,a.CONSIGNATION_DATE as purchased_date_act,vw.package_name,ba.area_name yieldly_Name FROM TM_VEHICLE V \n");
		sql.append(" LEFT OUTER JOIN VW_MATERIAL_GROUP_service vw ON vw.package_id=v.package_id \n");
		sql.append(" LEFT OUTER JOIN TT_DEALER_ACTUAL_SALES A ON V.VEHICLE_ID=A.VEHICLE_ID and a.is_return="+Constant.IF_TYPE_NO+"\n");
		sql.append(" LEFT OUTER JOIN TT_CUSTOMER C ON A.CTM_ID=C.CTM_ID \n");
		sql.append(" LEFT JOIN tm_business_area ba on ba.area_id=V.YIELDLY");
		sql.append(" LEFT JOIN TC_CODE TC ON TC.CODE_ID=a.car_charactor");
		sql.append(" LEFT JOIN Tt_As_Wr_Game wg on wg.id = v.claim_tactics_id");
		sql.append(" LEFT JOIN tt_as_wr_rule wu on wu.id=wg.rule_id");
		sql.append("	LEFT JOIN (SELECT max(vi.create_date) create_date ,vi.vehicle_id\n");
		sql.append("	FROM TT_VS_INSPECTION vi GROUP BY vi.vehicle_id)vi\n");
		sql.append("	ON  vi.vehicle_id = v.vehicle_id AND v.life_cycle IN ("+Constant.VEHICLE_LIFE_03+","+Constant.VEHICLE_LIFE_04+","+Constant.VEHICLE_LIFE_07+")"); 

		//sql.append(" LEFT OUTER JOIN TT_DEALER_ACTUAL_SALES A ON A.VEHICLE_ID=V.VEHICLE_ID \n");
		/*sql
				.append(" LEFT OUTER JOIN TT_ACTUAL_SALES A on A.VEHILCE_ID=V.VEHICLE_ID \n");*/
		sql.append(" WHERE 1=1 ");
		
		boolean flag = false;
		if (Utility.testString(map.get("vinParent"))) {
			sql.append(" and v.VIN  = '" + map.get("vinParent")
					+ "' ");
			flag = true;
		}
		if (Utility.testString(map.get("vin"))) {
			sql.append(" and v.VIN LIKE '%" + map.get("vin") + "%' ");
			flag = true;
		}
		/*if (Utility.testString(map.get("customer"))) {
			sql.append(" and a.CUSTOMER_NAME LIKE '%" + map.get("customer")
					+ "%' ");
		}*/
		if(flag){
			PageResult<TmVehicleExtendPO> ps = super.pageQuery(TmVehicleExtendPO.class, sql
					.toString(), null, pageSize, curPage);
			System.out.println(ps.getTotalRecords());
			return ps;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public TtAsWrInformationqualityPO getQualityCard(String claimId){
		List<Object> params = new LinkedList<Object>();
		String sql="select * from TT_AS_WR_INFORMATIONQUALITY where CLAIM_ID=?"; 
		params.add(claimId);
		List<TtAsWrInformationqualityPO> resList = this.select(TtAsWrInformationqualityPO.class, sql.toString(), params);
		if(resList!=null)
		{
			if(resList.size()>0) return resList.get(0);
			else return null;
		}
		else return null;
	}
	
	@SuppressWarnings("unchecked")
	public void qualityUpdate(TtAsWrInformationqualityPO po)
	{
		TtAsWrInformationqualityPO selpo=new TtAsWrInformationqualityPO();
		selpo.setId(po.getId());
		update(selpo,po);
	}

	/**
	 * 
	 * @Title: queryChngCodeByType
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param
	 * @param typeCode
	 * @param
	 * @return 设定文件
	 * @return List<TmBusinessChngCodePO> 返回类型
	 * @throws
	 */
	public List<TmBusinessChngCodePO> queryChngCodeByType(int typeCode,
			Long companyId,String code,String codeName) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select t.* from tm_business_chng_code t where 1=1 ");
		sqlStr.append(" and T.type_code = " + typeCode + "");
		if (Utility.testString(code)) {
			sqlStr.append(" and T.CODE LIKE '%" + code + "%' \n");
		}
		if (Utility.testString(codeName)) {
			sqlStr.append(" and T.CODE_NAME LIKE '%" + codeName + "%' \n");
		}
		List<TmBusinessChngCodePO> list = select(TmBusinessChngCodePO.class,
				sqlStr.toString(), null);
		return list;
	}
	/**
	 * 
	* @Title: queryChngCodeByType 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param typeCode
	* @param @param companyId
	* @param @param code
	* @param @param codeName
	* @param @param curPage
	* @param @param pageSize
	* @param @return    设定文件 
	* @return PageResult<TmBusinessChngCodePO>    返回类型 
	* @throws
	 */
	public PageResult<TmBusinessChngCodePO> queryChngCodeByType(int typeCode,
			Long companyId,String code,String codeName,int curPage,int pageSize) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select t.* from tm_business_chng_code t where 1=1 ");
		sqlStr.append(" and T.type_code = " + typeCode + "");
		if (Utility.testString(code)) {
			sqlStr.append(" and T.CODE LIKE '%" + code + "%' \n");
		}
		if (Utility.testString(codeName)) {
			sqlStr.append(" and T.CODE_NAME LIKE '%" + codeName + "%' \n");
		}
		PageResult<TmBusinessChngCodePO> list = pageQuery(TmBusinessChngCodePO.class,
				sqlStr.toString(), null,pageSize,curPage);
		return list;
	}
	
	//zhumingwei 2011-02-12
	public PageResult<Map<String, Object>> queryChngCodeByType111(String code,String codeName,String CUSTOMERS_PROBLEM,int curPage,int pageSize) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select T.* from tm_ccc T where 1=1 and t.status='"+Constant.IF_TYPE_YES+"' ");
		if (Utility.testString(code)) {
			sqlStr.append(" and T.vrt_code LIKE '%" + code + "%' \n");
		}
		if (Utility.testString(codeName)) {
			sqlStr.append(" and T.vfg_NAME LIKE '%" + codeName + "%' \n");
		}
		if (Utility.testString(CUSTOMERS_PROBLEM)) {
			sqlStr.append(" and T.ccc_name LIKE '%" + CUSTOMERS_PROBLEM + "%' \n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//zhumingwei 2011-03-08
	public List<Map<String, Object>> queryCon(String code) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select distinct a.vfg_name from tm_ccc a where a.vrt_code='"+code+"' and a.status='"+Constant.IF_TYPE_YES+"'");
		List<Map<String, Object>> list = pageQuery(sqlStr.toString(), null, getFunName());
		return list;
	}
	//zhumingwei add 2011-06-17 根据车系查询车型组
	public List<Map<String, Object>> queryCon1(String groupId) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select  distinct w.wrgroup_id,w.wrgroup_code,w.wrgroup_name from tt_as_wr_model_group w,TT_AS_WR_MODEL_ITEM I \n");
		sqlStr.append("where 1=1 and i.wrgroup_id=w.wrgroup_id and w.wrgroup_type ='10451001' and \n");
		sqlStr.append("i.model_id in (SELECT mg.group_id FROM TM_VHCL_MATERIAL_GROUP mg where mg.parent_group_id = '"+groupId+"')\n");
		sqlStr.append("order by  w.wrgroup_id desc");
		System.out.println(sqlStr.toString());
		List<Map<String, Object>> list = pageQuery(sqlStr.toString(), null, getFunName());
		return list;
	}

	/**
	 * 
	 * @Title: queryPartCodeByItemId
	 * @Description: TODO(根据工时ID取故障代码，下拉框联动)
	 * @param
	 * @param user
	 * @param
	 * @param con
	 * @param
	 * @param pageSize
	 * @param
	 * @param curPage
	 * @param
	 * @return 设定文件
	 * @return PageResult<TmPtPartBaseExtPO> 返回类型
	 * @throws
	 */
	public PageResult<TmBusinessChngCodePO> queryPartCodeByItemId(int typeCode,
			String itemId,String code,String codeName,int curPage,int pageSize) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select * from tm_business_chng_code b  ");
		//sqlStr
		//		.append(" left outer join TT_AS_WR_TROBLE_MAP m on b.business_code_id = m.trouble_id ");
		//sqlStr
		//		.append(" left outer join TT_AS_WR_WRLABINFO l on m.labor_id=l.id ");
		sqlStr.append(" where 1=1 ");
		//sqlStr.append(" and l.tree_code = '3' ");
		sqlStr.append(" and b.type_code = " + typeCode + " ");
		//sqlStr.append(" and l.labour_code ='" + itemId + "' ");
		if (Utility.testString(code)) {
			sqlStr.append(" and b.CODE LIKE '%" + code + "%' \n");
		}
		if (Utility.testString(codeName)) {
			sqlStr.append(" and b.CODE_NAME LIKE '%" + codeName + "%' \n");
		}
		
		StringBuffer sqlStr0 = new StringBuffer();
		sqlStr0.append(" select * from tm_business_chng_code b  ");
		//sqlStr0
		//		.append(" left outer join TT_AS_WR_TROBLE_MAP m on b.business_code_id = m.trouble_id ");
		//sqlStr0
		//		.append(" left outer join TT_AS_WR_WRLABINFO l on m.labor_id=l.id ");
		sqlStr0.append(" where 1=1 ");
		//sqlStr0.append(" and l.tree_code = '3' ");
		//sqlStr0.append(" and b.type_code = " + typeCode + " ");
		//sqlStr.append(" and l.labour_code ='" + itemId + "' ");
		if (Utility.testString(code)) {
			sqlStr0.append(" and b.CODE LIKE '%" + code + "%' \n");
		}
		if (Utility.testString(codeName)) {
			sqlStr0.append(" and b.CODE_NAME LIKE '%" + codeName + "%' \n");
		}
		PageResult<TmBusinessChngCodePO> list = pageQuery(TmBusinessChngCodePO.class,
				sqlStr.toString(), null,pageSize,curPage);
		if (list.getTotalRecords()>0) {
			
		}else {
			list = pageQuery(TmBusinessChngCodePO.class,
					sqlStr0.toString(), null,pageSize,curPage);
		}
		return list;
	}
	
	/**
	 * 
	 * @Title: queryPartCodeByItemId
	 * @Description: TODO(根据工时ID取故障代码，下拉框联动)
	 * @param
	 * @param user
	 * @param
	 * @param con
	 * @param
	 * @param pageSize
	 * @param
	 * @param curPage
	 * @param
	 * @return 设定文件
	 * @return PageResult<TmPtPartBaseExtPO> 返回类型
	 * @throws
	 */
	public List<TmBusinessChngCodePO> queryPartCodeByItemId(int typeCode,
			String itemId,String code,String codeName) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select * from tm_business_chng_code b  ");
		sqlStr
				.append(" left outer join TT_AS_WR_TROBLE_MAP m on b.business_code_id = m.trouble_id ");
		sqlStr
				.append(" left outer join TT_AS_WR_WRLABINFO l on m.labor_id=l.id ");
		sqlStr.append(" where 1=1 ");
		sqlStr.append(" and l.tree_code = '3' ");
		sqlStr.append(" and b.type_code = " + typeCode + " ");
		sqlStr.append(" and l.labour_code ='" + itemId + "' ");
		if (Utility.testString(code)) {
			sqlStr.append(" and b.CODE LIKE '%" + code + "%' \n");
		}
		if (Utility.testString(codeName)) {
			sqlStr.append(" and b.CODE_NAME LIKE '%" + codeName + "%' \n");
		}
		List<TmBusinessChngCodePO> list = select(TmBusinessChngCodePO.class,
				sqlStr.toString(), null);
		return list;
	}
	/**
	 * 
	* @Title: querySupplier 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param map
	* @param @param curPage
	* @param @param pageSize
	* @param @return    设定文件 
	* @return PageResult<TmPtSupplierPO>    返回类型 
	* @throws
	 */
	public PageResult<TmPtSupplierPO> querySupplier(Map<String,String> map,int curPage,int pageSize) {
		StringBuffer sql= new StringBuffer("\n");
		sql.append("select c.maker_id   as supplier_id,  c.maker_code supplier_code, c.maker_shotname supplier_name\n" );
		sql.append("    from tt_part_maker_define  c \n");
		
		
		
		if (Integer.valueOf(map.get("len"))==0) {
			sql.append(" where 1=2\n");
		}
		if (Integer.valueOf(map.get("len"))!=0) {
			sql.append(" where 1=1\n");
		}	
		
		if(Utility.testString(map.get("DealerId")))
		{
			sql.append("   and   c.maker_code not in ('902306','902309','902311') \n");
		}
		
		if (Integer.valueOf(map.get("len"))!=0) {
			if (Utility.testString(map.get("partCode"))) {
				System.out.println(map.get("partCode"));
		    if(!((map.get("partCode").equals("00-000") || map.get("partCode").equals("undefined") ) ))
		    {
		    	sql.append("and (c.maker_id in (\n");
				sql.append("  select t.maker_id  from tt_part_maker_relation t  where t.part_id in (\n" );
				sql.append("	select pb.part_id  from tm_pt_part_base pb  \n" );
				
				if (Integer.valueOf(map.get("count"))==1) {
					String  partCode =    map.get("partCode").toString();
					String  partCode1 =    map.get("partCode").toString();
					if(partCode.length() >14)
					{
						if(map.get("partCode").substring(13, 14).equals("B"))
						{
							partCode1 = partCode.substring(0, 13) + "0"+ partCode.substring(14,partCode.length() );
							partCode = partCode.substring(0, 13) +"000";
							
							if(partCode.length() > 16)
							{
								partCode = partCode + partCode.substring(16,partCode.length() );
							}
							partCode.substring(13, partCode.length());
						}
					}
					sql.append("  where pb.part_code in('"+partCode+"','"+partCode1+"','"+map.get("partCode").toString()+"')\n" );
				}
				sql.append(")\n" );
				sql.append(")  or c.MAKER_ID = (select c.maker_id from tt_part_maker_define  c where c.maker_code='902307' ) ) ");
		    }
				
			}
		}
		
		if (Utility.testString(map.get("supplierCode"))) {
			sql.append("  AND lower(c.maker_code) like '%"+map.get("supplierCode").toLowerCase()+"%' \n");
		}
		if (Utility.testString(map.get("supplierName"))) {
			sql.append("  AND c.maker_name like '%"+map.get("supplierName")+"%' \n");
		}
		PageResult<TmPtSupplierPO> ps = pageQuery(TmPtSupplierPO.class,sql.toString(), null, pageSize, curPage);
		return ps;
	}

	/**
	 * 
	 * @Title: queryChngCodeByType
	 * @Description: TODO(车型组下拉框)
	 * @param
	 * @param typeCode
	 * @param
	 * @return 设定文件
	 * @return List<TmBusinessChngCodePO> 返回类型
	 * @throws
	 */
	public List<TtAsWrModelGroupPO> queryGroupName() {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select t.* from TT_AS_WR_MODEL_GROUP t where 1=1 ");
		sqlStr.append(" and WRGROUP_TYPE=" + Constant.WR_MODEL_GROUP_TYPE_01
				+ " "); // 索赔工时车型组
		// sqlStr.append(" and T.type_code = "+typeCode+"");
		List<TtAsWrModelGroupPO> list = select(TtAsWrModelGroupPO.class, sqlStr
				.toString(), null);
		return list;
	}

	/**
	 * 
	 * @Title: queryOtherFee
	 * @Description: TODO(其他费用下拉框)
	 * @param
	 * @return 设定文件
	 * @return List<TtAsWrModelGroupPO> 返回类型 
	 * @throws
	 */
	public List<TtAsWrOtherfeePO> queryOtherFee(Long companyId) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr .append("select t.* from TT_AS_WR_OTHERFEE t where 1=1 AND t.IS_DEL=0");
		sqlStr .append( " and t.fee_code not in('QT006','QT008','QT009','QT007','QT010','QT011') ORDER BY FEE_ID ASC");
		// sqlStr.append(" and T.type_code = "+typeCode+"");
		List<TtAsWrOtherfeePO> list = select(TtAsWrOtherfeePO.class, sqlStr
				.toString(), null);
		return list;
	}

	/**
	 * 
	 * @Title: queryOtherFee
	 * @Description: TODO(其他费用下拉框)
	 * @param
	 * @return 设定文件
	 * @return List<TtAsWrModelGroupPO> 返回类型 
	 * @throws
	 */
	public List<TtAsWrOtherfeePO> queryOtherFee(Long companyId,String fee_code) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr
				.append("select t.* from TT_AS_WR_OTHERFEE t where 1=1 AND t.IS_DEL=0 and fee_code='"+fee_code+"'  ORDER BY FEE_ID ASC");
		// sqlStr.append(" and T.type_code = "+typeCode+"");
		List<TtAsWrOtherfeePO> list = select(TtAsWrOtherfeePO.class, sqlStr
				.toString(), null);
		return list;
	}
	
	//zhumingwei 2012-07-06
	public List<Map<String,Object>> queryOtherFee1(String id) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select b.item_desc,sum(nvl(b.amount,0)) as amount from tt_as_wr_netitem b where b.id="+id+" group by b.item_code,b.item_desc");
		return this.pageQuery(sqlStr.toString(), null, this.getFunName());
	}
	//zhumingwei 2012-07-12
	public String queryOtherFee2(String id) {
		String a="0";
		String sql ="select sum(nvl(b.amount,0)) as amount1 from tt_as_wr_netitem b where b.id="+id+"";
		Map<String, Object> map = pageQueryMap(sql, null, getFunName());
		if (map != null && map.size() > 0) {
			a = String.valueOf(map.get("AMOUNT1"));
		}else{
			a="0";
		}
		return a;
	}
	/**
	 * 
	* @Title: changeOtherFore 
	* @Description: TODO(查询其他项目是否授权) 
	* @param @param map
	* @param @return    设定文件 
	* @return List    返回类型 
	* @throws
	 */
	public List changeOtherFore(Map<String,String> map) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select count(*) as is_del \n" );
		sqlStr.append("from TT_AS_WR_FOREAPPROVAL t1,tm_dealer td,TT_AS_WR_FOREAPPROVALITEM t2\n" );
		sqlStr.append("where t1.dealer_id = td.dealer_id\n" );
		sqlStr.append("and t2.status is not null\n" );
		sqlStr.append("and t1.id = t2.fid\n" );
		sqlStr.append("and t1.ro_no='"+map.get("roNo")+"'\n" );
		sqlStr.append("and t1.vin='"+map.get("vin")+"'\n" );
		sqlStr.append("and t2.status="+Constant.PRECLAIM_AUDIT_01+"\n" ); //已同意
		sqlStr.append("and t2.item_type="+Constant.PRE_AUTH_ITEM_03+"\n" ); //其他项目
		sqlStr.append("and t2.item_code='"+map.get("itemCode")+"'\n" );
		List<TtAsWrOtherfeePO> list = select(TtAsWrOtherfeePO.class, sqlStr
				.toString(), null);
		return list;
	}
	/**
	 * 
	* @Title: getLabourParameter 
	* @Description: TODO(取工时单价) 
	* @param @param dealerId
	* @param @param modelId
	* @param @param companyId
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getLabourParameter(String dealerId,String modelId,String companyId) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT pr.labour_price as labour_price \n" );
		sql.append(" from tt_as_wr_labour_price pr\n" );
		sql.append("left outer join tm_dealer td on td.dealer_id = pr.dealer_id\n" );	
		sql.append("where 1=1 \n" );
		sql.append("AND pr.DEALER_ID="+dealerId+"\n" );
		sql.append(" and pr.mode_type =\n" );
		sql.append(" (SELECT WRGROUP_CODE FROM tt_as_wr_model_group\n" );
		sql.append(" WHERE WRGROUP_ID in (SELECT WRGROUP_ID FROM tt_as_wr_model_item WHERE MODEL_ID="+modelId+") and wrgroup_type="+Constant.WR_MODEL_GROUP_TYPE_01+")");
		return sql.toString();
	}
	
	public String getLabourParameter1(String dealerId,String modelId,String companyId) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT sum(pr.change_labour_price) as labour_price \n" );
		sql.append(" from tt_as_labour_price_info pr\n" );
		sql.append("left outer join tm_dealer td on td.dealer_id = pr.dealer_id\n" );	
		sql.append("where 1=1   and sysdate>=pr.policy_date\n" );
		sql.append("AND pr.DEALER_ID="+dealerId+"\n" );
		sql.append(" and pr.group_id =\n" );
		sql.append(" (SELECT WRGROUP_ID FROM tt_as_wr_model_group\n" );
		sql.append(" WHERE WRGROUP_ID in (SELECT WRGROUP_ID FROM tt_as_wr_model_item WHERE MODEL_ID="+modelId+") and wrgroup_type="+Constant.WR_MODEL_GROUP_TYPE_01+")");
		return sql.toString();
	}
	/**
	 * 
	* @Title: getLabourPrice 
	* @Description: TODO(取工时单价) 
	* @param @param dealerId
	* @param @param modelId
	* @param @param companyId
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getLabourPrice(String dealerId,String modelCode,String companyId) {
		String modelId = "";
		if (Utility.testString(modelCode)) {

			VwMaterialGroupServicePO vmgp = new VwMaterialGroupServicePO();
			vmgp.setModelCode(modelCode);
			List<VwMaterialGroupServicePO> ls = dao.select(vmgp);
			if (ls != null) {
				if (ls.size() > 0) {
					vmgp = ls.get(0);
					modelId = vmgp.getModelId() == null ? "" : vmgp
							.getModelId().toString();
				}
			}
			String a = "0";
			String sql = "select nvl(labour_price,0) as labour_price from ("
					+ getLabourParameter(dealerId, modelId, companyId) + ") t";
			Map<String, Object> map = pageQueryMap(sql, null, getFunName());
			if (map != null && map.size() > 0) {
				a = String.valueOf(map.get("LABOUR_PRICE"));
			}
			return a;
		} else {
			return "0";
		}
	}
	/**
	 * 
	 * @Title: queryTime
	 * @Description: TODO(查询工时)
	 * @param
	 * @param user
	 * @param
	 * @param con
	 * @param
	 * @param pageSize
	 * @param
	 * @param curPage
	 * @param
	 * @return 设定文件
	 * @return PageResult<TtAsWrWrlabinfoExtPO> 返回类型 
	 * @throws
	 */
//	public PageResult<TtAsWrWrlabinfoExtPO> queryTime(AclUserBean user,
//			String con,String roNo,String vin ,String modelId,String companyId,int pageSize, int curPage) {
//		StringBuffer sqlStr = new StringBuffer();
//		sqlStr.append(" select g.WRGROUP_NAME AS wrgroup_name,t.*,t.labour_quotiety*t.labour_hour as labour_fix, " );
//		sqlStr.append("(case when instr(t.labour_code,'"+Constant.SPEC_LABOUR_CODE+"')=1 then 1 else 0 end ) as is_spec,");
//		List<Map<String,Object>> jc = dao.queryTcCode();
//		int jc_1 = 0;
//		if(jc.size()>0){
//			jc_1 = Integer.valueOf(jc.get(0).get("CODE_ID").toString());
//		}
//		if(jc_1==Constant.chana_wc){
//			sqlStr.append("nvl(("+getLabourParameter1(user.getDealerId(),modelId,companyId)+" ),0) AS parameter_value from TT_AS_WR_WRLABINFO t ");
//			
//		}
//		else{
//			sqlStr.append("nvl(("+getLabourParameter(user.getDealerId(),modelId,companyId)+" ),0) AS parameter_value from TT_AS_WR_WRLABINFO t ");
//			
//		}
//		sqlStr.append(" LEFT OUTER JOIN TT_AS_WR_MODEL_GROUP G ON T.WRGROUP_ID=G.WRGROUP_ID  ");
//		sqlStr.append(" where 1=1  ");
//		sqlStr.append(" and t.is_send=" + Constant.DOWNLOAD_CODE_STATUS_03
//				+ " ");
//		sqlStr.append(" and t.IS_DEL=0 ");
//		// sqlStr.append(" and d.DEALER_ID="+user.getDealerId()+" ");
//		// sqlStr.append(" and d.PARAMETER_CODE='"+10421001+"' ");
//		sqlStr.append(con);
//		PageResult<TtAsWrWrlabinfoExtPO> ps = pageQuery(
//				TtAsWrWrlabinfoExtPO.class, sqlStr.toString(), null, pageSize,
//				curPage);
//		return ps;
//	}
	/**
	 * 2013-04-07@author yuchanghong
	 * 获得工时单价
	 */
	public PageResult<TtAsWrWrlabinfoExtPO> queryTime(AclUserBean user,
			String con,String roNo,String vin ,String modelId,String companyId,int pageSize, int curPage) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select g.WRGROUP_NAME AS wrgroup_name,t.*,t.labour_quotiety*t.labour_hour as labour_fix, " );
		sqlStr.append(" case WHEN b.ID IS NOT NULL  THEN\n");
		sqlStr.append("  10041001 else 10041002 end fore,\n");
		sqlStr.append("(case when instr(t.labour_code,'"+Constant.SPEC_LABOUR_CODE+"')=1 then 1 else 0 end ) as is_spec,");
		sqlStr.append("nvl(("+getLabourParameter(user.getDealerId(),modelId,companyId)+" ),0) AS parameter_value,b.approval_level from TT_AS_WR_WRLABINFO t ");
		sqlStr.append(" LEFT OUTER JOIN TT_AS_WR_MODEL_GROUP G ON T.WRGROUP_ID=G.WRGROUP_ID  ");
		sqlStr.append(" left join tt_as_wr_authmonitorlab b on b.labour_operation_no = t.labour_code and b.model_group=t.wrgroup_id\n");
		sqlStr.append(" where 1=1  ");
		sqlStr.append(" and t.IS_DEL=0 ");
		// sqlStr.append(" and d.DEALER_ID="+user.getDealerId()+" ");
		// sqlStr.append(" and d.PARAMETER_CODE='"+10421001+"' ");
		sqlStr.append(con);
		PageResult<TtAsWrWrlabinfoExtPO> ps = pageQuery(
				TtAsWrWrlabinfoExtPO.class, sqlStr.toString(), null, pageSize,
				curPage);
		return ps;
	}
	/**
	 * 
	 * @Title: queryPartCode
	 * @Description: TODO(查询配件)
	 * @param
	 * @param user
	 * @param
	 * @param con
	 * @param
	 * @param pageSize
	 * @param
	 * @param curPage
	 * @param
	 * @return 设定文件
	 * @return PageResult<TmPtPartBaseExtPO> 返回类型
	 * @throws
	 */
	public PageResult<TmPtPartBaseExtPO> queryPartCode(AclUserBean user,
		      Map<String, String> map,String str, int pageSize, int curPage) {
		    StringBuffer sqlStr = new StringBuffer();
		    sqlStr.append("select A.PART_code as pars,\n");
		    sqlStr.append("                a.*,\n" );
		    sqlStr.append("case when A.IS_DEL = 0 and s.is_del=0 AND S.PART_CODE IS NOT NULL THEN 10041001 ELSe 10041002 ");
		    sqlStr.append("  end fore ,\n");
		    sqlStr.append("case\n");
		    sqlStr.append("        when A.IS_DEL = 0 and s.is_del=0 AND S.PART_CODE IS NOT NULL THEN\n");
		    sqlStr.append("        s.approval_level\n");
		    sqlStr.append("        ELSe\n");
		    sqlStr.append("         null\n");
		    sqlStr.append("      end part_Level,"); 

		    sqlStr.append("CASE\n");
		    sqlStr.append("        WHEN (  trunc(sysdate) >= sp.valid_date and  sp.history_flag =1) or  sp.history_flag =0 THEN\n");
		    sqlStr.append("       round(nvl(sp.sale_price1, 0) * (1 + nvl(m.parameter_value, 0) / 100),  2)\n");
		    sqlStr.append("       ELSE\n");
		    sqlStr.append("        round(nvl(decode(sp.history_price,null,sp.sale_price1,sp.history_price), 0) * (1 + nvl(m.parameter_value, 0) / 100),  2)\n");
		    sqlStr.append("     END claim_Price_param\n"); 

		    sqlStr.append("		from TM_PT_PART_BASE a\n");
		    sqlStr.append(" left join tt_part_sales_price sp  on sp.part_id = a.part_id\n");

		    sqlStr.append(" LEFT JOIN TT_AS_WR_AUTHMONITORPART s ON s.part_code = a.part_code\n");
		    sqlStr.append("                                     and a.is_del = 0,\n");
		    sqlStr.append("tm_down_parameter m"); 

		    if(Utility.testString(map.get("seriesId"))){
		    	sqlStr.append(" ,TT_AS_RELATION_PART_SERIES ps \n");
		    }
		    sqlStr.append(" where 1 = 1 ");
		    sqlStr.append(" and m.dealer_id="+user.getDealerId()+"");
		   
		    if(Constant.REPAIR_TYPE_07.equalsIgnoreCase(map.get("repairType"))){//如果是急件工单类型，则只能查询出设置了是急件的单子
		    	  sqlStr.append(" and a.is_spjj= "+Constant.IF_TYPE_YES+"\n");
		    }
		    if(Constant.PART_IS_CHANGHE_01.toString().equalsIgnoreCase(map.get("yiedlyType"))){
		    	sqlStr.append(" and m.parameter_code ="+Constant.CLAIM_BASIC_PARAMETER_09);
		    }else{
		    	sqlStr.append(" and m.parameter_code ="+Constant.CLAIM_BASIC_PARAMETER_08);
		    }
		    // if (Utility.testString(map.get("groupId"))) {
		    /* 此处屏蔽掉车型过滤 */
		    // sqlStr.append(" AND A.GROUP_ID =" + map.get("groupId") + " ");
		    // }
		    if (Utility.testString(map.get("yiedlyType"))) {
		      sqlStr.append(" AND A.Part_Is_Changhe = '" + map.get("yiedlyType")+ "' ");
		    }
		    if(Utility.testString(map.get("seriesId"))){
		    	sqlStr.append(" and  ps.part_id = a.part_id and ps.series_id="+map.get("seriesId")+"\n");
		    }
		    if (Utility.testString(map.get("modelId"))) {
		    	sqlStr.append(" and ps.model_id="+map.get("modelId")+"\n");
			}
		    if (Utility.testString(map.get("partCode"))) {
		      sqlStr.append(" AND A.PART_CODE LIKE '%" + map.get("partCode").toUpperCase()
		          + "%' ");
		    }
		    if (Utility.testString(map.get("partName"))) {
		      sqlStr.append(" AND A.PART_NAME LIKE '%" + map.get("partName")
		          + "%' ");
		    }
		    if (Utility.testString(str)) {
			      sqlStr.append("  AND A.PART_CODE in("+str+") ");
			    }
		    //sqlStr.append(" GROUP ");
		    // sqlStr.append(con);
		    PageResult<TmPtPartBaseExtPO> ps = pageQuery(TmPtPartBaseExtPO.class,
		        sqlStr.toString(), null, pageSize, curPage);
		    return ps;
		  }
	
	public PageResult<TmPtPartBaseExtPO> queryPartCode5(AclUserBean user,
		      Map<String, String> map,String str, int pageSize, int curPage) {
		    StringBuffer sqlStr = new StringBuffer();
		    sqlStr.append("select aa.ro_no,A.PART_code as pars,\n");
		    sqlStr.append("                a.*,\n" );
		    sqlStr.append("case when A.IS_DEL = 0 and s.is_del=0 AND S.PART_CODE IS NOT NULL THEN 10041001 ELSe 10041002 ");
		    sqlStr.append("  end fore ,\n");
		    sqlStr.append("case\n");
		    sqlStr.append("        when A.IS_DEL = 0 and s.is_del=0 AND S.PART_CODE IS NOT NULL THEN\n");
		    sqlStr.append("        s.approval_level\n");
		    sqlStr.append("        ELSe\n");
		    sqlStr.append("         null\n");
		    sqlStr.append("      end part_Level,"); 

		    sqlStr.append("CASE\n");
		    sqlStr.append("        WHEN (  trunc(sysdate) >= sp.valid_date and  sp.history_flag =1) or  sp.history_flag =0 THEN\n");
		    sqlStr.append("       round(nvl(sp.sale_price1, 0) * (1 + nvl(m.parameter_value, 0) / 100),  2)\n");
		    sqlStr.append("       ELSE\n");
		    sqlStr.append("        round(nvl(decode(sp.history_price,null,sp.sale_price1,sp.history_price), 0) * (1 + nvl(m.parameter_value, 0) / 100),  2)\n");
		    sqlStr.append("     END claim_Price_param\n"); 

		    sqlStr.append("		from TM_PT_PART_BASE a\n");
		    sqlStr.append(" left join tt_part_sales_price sp  on sp.part_id = a.part_id\n");

		    sqlStr.append(" LEFT JOIN TT_AS_WR_AUTHMONITORPART s ON s.part_code = a.part_code\n");
		    sqlStr.append("                                     and a.is_del = 0,\n");
		    sqlStr.append("tm_down_parameter m,tt_as_repair_order aa,tt_as_ro_repair_part bb"); 

		    if(Utility.testString(map.get("seriesId"))){
		    	sqlStr.append(" ,TT_AS_RELATION_PART_SERIES ps \n");
		    }
		    sqlStr.append(" where 1 = 1 and aa.id = bb.ro_id and a.part_code = bb.part_no and bb.pay_type = 11801001");
		    sqlStr.append(" and m.dealer_id="+user.getDealerId()+"");
		    sqlStr.append(" and aa.dealer_id="+user.getDealerId()+"");
		    if(Constant.REPAIR_TYPE_07.equalsIgnoreCase(map.get("repairType"))){//如果是急件工单类型，则只能查询出设置了是急件的单子
		    	  sqlStr.append(" and a.is_spjj= "+Constant.IF_TYPE_YES+"\n");
		    }
		    if(Constant.PART_IS_CHANGHE_01.toString().equalsIgnoreCase(map.get("yiedlyType"))){
		    	sqlStr.append(" and m.parameter_code ="+Constant.CLAIM_BASIC_PARAMETER_09);
		    }else{
		    	sqlStr.append(" and m.parameter_code ="+Constant.CLAIM_BASIC_PARAMETER_08);
		    }
		    if (Utility.testString(map.get("yiedlyType"))) {
		      sqlStr.append(" AND A.Part_Is_Changhe = '" + map.get("yiedlyType")+ "' ");
		    }
		    if(Utility.testString(map.get("seriesId"))){
		    	sqlStr.append(" and  ps.part_id = a.part_id and ps.series_id="+map.get("seriesId")+"\n");
		    }
		    if (Utility.testString(map.get("modelId"))) {
		    	sqlStr.append(" and ps.model_id="+map.get("modelId")+"\n");
			}
		    if (Utility.testString(map.get("partCode"))) {
		      sqlStr.append(" AND A.PART_CODE LIKE '%" + map.get("partCode").toUpperCase()
		          + "%' ");
		    }
		    if (Utility.testString(map.get("partName"))) {
		      sqlStr.append(" AND A.PART_NAME LIKE '%" + map.get("partName")
		          + "%' ");
		    }
		    if (Utility.testString(str)) {
			      sqlStr.append("  AND A.PART_CODE in("+str+") ");
			}
		    PageResult<TmPtPartBaseExtPO> ps = pageQuery(TmPtPartBaseExtPO.class,
		        sqlStr.toString(), null, pageSize, curPage);
		    return ps;
		  }
	
	//旧件入库需修改的旧件，选择查询
	public PageResult<TmPtPartBaseExtPO> queryPartCodes(AclUserBean user,
		      Map<String, String> map, int pageSize, int curPage) {
		    StringBuffer sql = new StringBuffer();
		    sql.append("SELECT T.PART_ID,\n");
		    sql.append("       T.Part_Oldcode part_Code, t.part_cname part_Name,\n");
		    sql.append("       T.IS_REPLACED,\n");
		    sql.append("       T.REPART_ID,\n");
		    sql.append("       T.REPART_CODE,\n");
		    sql.append("       vd.maker_code supplier_code,\n");
		    sql.append("       vd.maker_name supplier_name\n");
		    sql.append("  FROM TT_PART_DEFINE T, tt_part_maker_define vd, tt_part_maker_relation p\n");
		    sql.append(" where vd.maker_id = p.maker_id\n");
		    sql.append("   and p.part_id = t.part_id\n");
		    sql.append("CONNECT BY PRIOR T.REPART_ID = T.PART_ID\n");
		    sql.append(" START WITH T.Part_Oldcode = '"+map.get("partCode")+"'"); 

		    PageResult<TmPtPartBaseExtPO> ps = pageQuery(TmPtPartBaseExtPO.class,
		        sql.toString(), null, pageSize, curPage);
		    return ps;
		  }
	
	/**
	 * 根据VIN获取车辆信息
	 * @param vin
	 * @return
	 */
	public Map<String, Object> queryVehicleByVin(String vin){
		String sql = "select * from tm_vehicle where vin = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(vin);
		return pageQueryMap(sql, params, this.getFunName());
	}
	//活动配件赠送明细
	public PageResult<ActivityPartDetailBean> activityPartDetail(Map<String, String> map, int pageSize, int curPage) {
		    StringBuffer sql = new StringBuffer();
		    sql.append("select a.activity_code,a.activity_name,r.project_code part_code,r.project_name part_name,\n");
		    
		   // sql.append("round(p.sale_price1*(1+(dp.parameter_value)/100),2) price\n");
		    sql.append("CASE\n");
		    sql.append("        WHEN (  trunc(sysdate) >= p.valid_date and  p.history_flag =1) or  p.history_flag =0 THEN\n");
		    sql.append("         round(nvl(p.sale_price1,0)*(1+(dp.parameter_value)/100),2)\n");
		    sql.append("        ELSE\n");
		    sql.append("       round(NVL(decode(p.history_price,null,p.sale_price1,p.history_price), 0)*(1+(dp.parameter_value)/100),2)  \n");
		    sql.append("      END price \n"); 

		    sql.append("from  TT_AS_ACTIVITY a ,tm_down_parameter dp,TT_AS_ACTIVITY_RELATION r,\n");
		    sql.append(" tt_part_define d   , tt_part_sales_price p\n");

		    sql.append("where  d.part_oldcode = r.project_code\n");
		    sql.append("and  p.part_id = d.part_id\n");
		    sql.append("and a.activity_id = r.activity_id\n");
		    sql.append("and a.activity_id='"+map.get("id")+"'\n");
		    sql.append("and d.part_is_changhe="+map.get("yiedily")+"\n");
		    
		    if(Constant.PART_IS_CHANGHE_01.toString().equalsIgnoreCase(map.get("yiedily"))){
				 sql.append(" and dp.parameter_code ="+Constant.CLAIM_BASIC_PARAMETER_09);
			    }else{
			    	sql.append(" and dp.parameter_code ="+Constant.CLAIM_BASIC_PARAMETER_08);
			    }
		    sql.append(" and dp.dealer_id = "+map.get("dealerId")+"\n");
		    sql.append("AND  R.LARGESS_TYPE ="+Constant.SERVICEACTIVITY_CAR_cms_05); 


		    PageResult<ActivityPartDetailBean> ps = pageQuery(ActivityPartDetailBean.class,
		        sql.toString(), null, pageSize, curPage);
		    return ps;
		  }
	public PageResult<TmPtPartBaseExtPO> queryPartCode2(AclUserBean user,
		      Map<String, String> map, int pageSize, int curPage) {
		    StringBuffer sqlStr = new StringBuffer();
		    sqlStr.append("select * From TM_PT_PART_BASE a where 1=1 and a.IS_CLIAM = 10011002 \n");
		  
		    // if (Utility.testString(map.get("groupId"))) {
		    /* 此处屏蔽掉车型过滤 */
		    // sqlStr.append(" AND A.GROUP_ID =" + map.get("groupId") + " ");
		    // }
		    if (Utility.testString(map.get("partCode"))) {
		      sqlStr.append(" AND A.PART_CODE LIKE '%" + map.get("partCode")
		          + "%' ");
		    }
		    if (Utility.testString(map.get("erpdCode"))) {
			      sqlStr.append(" AND A.ERPD_CODE LIKE '%" + map.get("erpdCode")
			          + "%' ");
			    }
		    if (Utility.testString(map.get("partName"))) {
		      sqlStr.append(" AND A.PART_NAME LIKE '%" + map.get("partName")
		          + "%' ");
		    }
		 
		    //sqlStr.append(" GROUP ");
		    // sqlStr.append(con);
		    PageResult<TmPtPartBaseExtPO> ps = pageQuery(TmPtPartBaseExtPO.class,
		        sqlStr.toString(), null, pageSize, curPage);
		    return ps;
		  }
	public PageResult<TmPtPartBaseExtPO> queryPartCode1(AclUserBean user,
		      Map<String, String> map, int pageSize, int curPage,String downPartCode) {
		    StringBuffer sqlStr = new StringBuffer();
		    sqlStr.append("select DISTINCT (A.PART_code) as pars,\n");
		    sqlStr.append("                a.*,\n" );
		   
		    sqlStr.append("CASE\n");
		    sqlStr.append("        WHEN (  trunc(sysdate) >= sp.valid_date and  sp.history_flag =1) or  sp.history_flag =0 THEN\n");
		    sqlStr.append("                nvl(sp.sale_price1, 0) *\n" );
		    sqlStr.append("                (1 + nvl(m.parameter_value, 0) / 100) \n" );
		    sqlStr.append("       ELSE\n");
		    sqlStr.append("                NVL(decode(sp.history_price,null,sp.sale_price1,sp.history_price), 0)*\n" );
		    sqlStr.append("                (1 + nvl(m.parameter_value, 0) / 100) \n" );
		    sqlStr.append("        \n");
		    sqlStr.append("     END claim_Price_param\n"); 
		    
		    sqlStr.append("		from TM_PT_PART_BASE a\n");
		    sqlStr.append("  left join tt_part_sales_price sp  on sp.part_id = a.part_id\n");

		    sqlStr.append(" ,");
		    sqlStr.append(" tm_down_parameter m,"); 
		    sqlStr.append("TT_AS_RELATION_PART_SERIES ps"); 

		    sqlStr.append(" where 1 = 1");
		    sqlStr.append(" and m.dealer_id="+user.getDealerId()+"");
		    if(Constant.PART_IS_CHANGHE_01.toString().equalsIgnoreCase(map.get("yiedily"))){
		    	sqlStr.append("  and m.parameter_code="+Constant.CLAIM_BASIC_PARAMETER_09);
		    }else  if(Constant.PART_IS_CHANGHE_02.toString().equalsIgnoreCase(map.get("yiedily"))){
		    	sqlStr.append("  and m.parameter_code="+Constant.CLAIM_BASIC_PARAMETER_08);
		    }
		    sqlStr.append("and ps.part_id = a.part_id\n");
		    sqlStr.append("  and ps.series_id = "+map.get("seriesId")+"\n"); 

		    // if (Utility.testString(map.get("groupId"))) {
		    /* 此处屏蔽掉车型过滤 */
		    // sqlStr.append(" AND A.GROUP_ID =" + map.get("groupId") + " ");
		    // }
		    if (Utility.testString(map.get("yiedily"))) {
			      sqlStr.append(" AND a.part_is_changhe= '" + map.get("yiedily")
			          + "' ");
			    }
		    if (Utility.testString(map.get("partCode"))) {
		      sqlStr.append(" AND A.PART_CODE LIKE '%" + map.get("partCode").toUpperCase()
		          + "%' ");
		    }
		    if (Utility.testString(map.get("partName"))) {
		      sqlStr.append(" AND A.PART_NAME LIKE '%" + map.get("partName")
		          + "%' ");
		    }
		   // List<Map<String,Object>> ListCode =   dao.queryTcCode();
		   // Integer code = Integer.valueOf(ListCode.get(0).get("CODE_ID").toString());
		  //  if(code==Constant.chana_wc){
		      sqlStr.append("  and exists (select s.part_type_id from TM_PT_PART_BASE s where s.part_code='"+downPartCode+"' and a.part_type_id = s.part_type_id)  ");
		  //  }
		    //sqlStr.append(" GROUP ");
		    // sqlStr.append(con);
		    PageResult<TmPtPartBaseExtPO> ps = pageQuery(TmPtPartBaseExtPO.class,
		        sqlStr.toString(), null, pageSize, curPage);
		    return ps;
		  }

	
	
	
	  public List<Map<String,Object>> queryTcCode() {
			String sql = "select * from tc_code where type=8008";
			List<Map<String,Object>> ps = this.pageQuery(sql, null, this.getFunName());
			return ps;
		}
	public List<Map<String,Object>> queryDealerApplicationTotal(
			AclUserBean user, Map<String, String> map, List params) {
    	 
    	String yieldly = map.get("yieldly");
		StringBuffer sqlStr = new StringBuffer(); 

		sqlStr.append("SELECT count(1) count,sum(nvl(A.balance_amount,0)) totalBalanceAmount\n" );
		sqlStr.append("  FROM TT_AS_REPAIR_ORDER V, TT_AS_WR_APPLICATION A, TM_DEALER B,tm_business_area ba \n" );
		sqlStr.append(" WHERE 1 = 1 and ba.area_id(+) = a.yieldly\n" );
		sqlStr.append("   AND B.DEALER_ID = A.DEALER_ID(+)\n" );
		sqlStr.append("   AND A.RO_NO = V.RO_NO(+)\n" );
		//====================2015-6-1只能查询之前
		sqlStr.append("  AND A.CREATE_DATE <= to_date(' 2015-05-25 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		
		if(yieldly!=null){
			sqlStr.append("   AND A.yieldly = '"+yieldly+"'\n" );
		}
		if (Utility.testString(map.get("dealerName"))) {
			sqlStr.append(" AND a.DEALER_name like'%" + map.get("dealerName")
					+ "%' \n");
		}
		if (Utility.testString(map.get("dealerCode"))) {
			sqlStr.append(Utility.getConSqlByParamForEqual(map
					.get("dealerCode"), params, "b", "dealer_code"));
		}
		// 经销商代码
		if (Utility.testString(map.get("dealerId"))) {
			//sqlStr.append(" AND A.DEALER_ID='" + map.get("dealerId") + "' \n");
		//	sqlStr.append(" AND (A.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+map.get("dealerId")+"   connect by PRIOR d.dealer_id = d.parent_dealer_d) or a.second_dealer_id = "+map.get("dealerId")+" )\n");
			sqlStr.append("AND DECODE(a.SECOND_DEALER_ID, null, b.DEALER_ID, a.SECOND_DEALER_ID) IN\n");
			sqlStr.append("      (SELECT d.DEALER_ID\n");
			sqlStr.append("         FROM TM_DEALER D\n");
			sqlStr.append("        START WITH d.DEALER_ID = "+map.get("dealerId")+"\n");
			sqlStr.append("       CONNECT BY PRIOR d.DEALER_ID = d.PARENT_DEALER_D)"); 

		}
		// 索赔单号
		if (Utility.testString(map.get("claimNo"))) {
			sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map.get("claimNo")
					+ "%' \n");
		}
		// 工单号
		if (Utility.testString(map.get("roNo"))) {
			sqlStr.append(" AND A.RO_NO LIKE '%" + map.get("roNo") + "%' \n");
		}
		// 行号
		if (Utility.testString(map.get("lineNo"))) {
			sqlStr.append(" AND A.LINE_NO='" + map.get("lineNo") + "' \n");
		}
		// 索赔类型
		DaoFactory.getsql(sqlStr, "A.CLAIM_TYPE", map.get("claimType"), 6);
		
		if(Utility.testString(map.get("partCode"))){
			sqlStr.append("   AND A.ID in ( SELECT TAWP.ID FROM TT_AS_WR_PARTSITEM TAWP WHERE TAWP.PART_CODE LIKE '%"+map.get("partCode")+"%' GROUP BY TAWP.ID )  \n");
		}
		if(Utility.testString(map.get("wrLabourCode"))){
			sqlStr.append("   AND A.ID in ( SELECT TAWL.ID FROM TT_AS_WR_LABOURITEM TAWL WHERE TAWL.WR_LABOURCODE LIKE '%"+map.get("wrLabourCode")+"%' GROUP BY TAWL.ID )  \n");
		}
//		if (Utility.testString(map.get("claimType"))) {
//			sqlStr
//					.append(" AND A.CLAIM_TYPE='" + map.get("claimType")
//							+ "' \n");
//		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN涓嶄负绌 
			sqlStr.append(" and a.vin in ("+map.get("vin")+")\n");
//			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
//					"\''"), "A"));
		}
		// 工单开始时间
		if (Utility.testString(map.get("roStartdate"))) {
			sqlStr
					.append(" AND A.CREATE_DATE >= to_date('"
							+ map.get("roStartdate")
							+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 工单结束时间
		if (Utility.testString(map.get("roEnddate"))) {
			sqlStr.append(" AND A.CREATE_DATE <= to_date('"
					+ map.get("roEnddate")
					+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 审核时间查询条件
		if(Utility.testString(map.get("approveDate"))){
			sqlStr.append(" AND a.auditing_date >= to_date('"
					+map.get("approveDate")
					+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("approveDate2"))){
			sqlStr.append(" AND a.auditing_date <= to_date('"
					+map.get("approveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("balanceApproveDate"))){
			sqlStr.append(" AND a.account_date >= to_date('"
					+map.get("balanceApproveDate")
					+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("balanceApproveDate2"))){
			sqlStr.append(" AND a.account_date <= to_date('"
					+map.get("balanceApproveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		// 艾春 13.11.20 添加是否二级查询条件
		if(Utility.testString(map.get("isSecond"))){
			if("1".equals(map.get("isSecond"))){
				sqlStr.append(" AND a.second_dealer_id is not null \n");
			}else{
				sqlStr.append(" AND a.second_dealer_id is null \n");
			}
		}
		// 艾春 13.11.20 添加是否二级查询条件
		
		// 申请状态
		if (Utility.testString(map.get("status"))) {
			sqlStr.append(" AND A.STATUS='" + map.get("status") + "' \n");

		} else if (!"track".equals(map.get("track"))) {
			sqlStr.append(" AND (A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_01
					+ "'  \n");
			sqlStr.append(" OR A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_06
					+ "')  \n");

		} else if (!"isApp".equals(map.get("isApp"))) {
			sqlStr.append(" AND (A.STATUS<" + Constant.CLAIM_APPLY_ORD_TYPE_01
					+ " OR A.STATUS >" + Constant.CLAIM_APPLY_ORD_TYPE_01 +") \n");
		}
		if (Utility.testString(map.get("model"))) {
			sqlStr.append(" and v.model = '"+map.get("model")+"'");
		}
		if (Utility.testString(map.get("deliverer"))) {
			sqlStr.append(" and v.deliverer like '%"+map.get("deliverer")+"%' ");
		}
		if (Utility.testString(map.get("delivererPhone"))) {
			sqlStr.append(" and v.deliverer_phone like '%"+map.get("delivererPhone")+"%' ");
		}
		if (Utility.testString(map.get("isImport"))) {
			sqlStr.append(" and a.is_import = '"+map.get("isImport")+"'");
		}
		/**屏蔽工单索赔单查询*/
		if(Utility.testString(map.get("dealerId"))){
	    	sqlStr.append(" and a.ro_no not in (select v.ro_no from tt_as_wr_repair_claim v where v.dealer_id='"+map.get("dealerId")+"')");
		}
		sqlStr.append(" ORDER BY A.ID DESC ");
		System.out.println("sqlsql=="+sqlStr.toString());
		logger.info("-----------------------"+sqlStr.toString());

		return this.pageQuery(sqlStr.toString(), params, this.getFunName());
	}
	
	public PageResult<TtAsWrApplicationExtBean> queryDealerApplication(
			AclUserBean user, Map<String, String> map, List params,
			int pageSize, int curPage) {
    	 
    	String yieldly = map.get("yieldly");
		StringBuffer sqlStr = new StringBuffer(); 

		sqlStr.append("SELECT A.*,\n" );
		
		sqlStr.append("(select b.dealer_shortname\n" );
		sqlStr.append("       from TM_DEALER b\n" );
		sqlStr.append("      where b.dealer_id = NVL(A.SECOND_DEALER_ID, A.DEALER_ID)) as dealer_shortname1,\n" );
		sqlStr.append("\n" );
		sqlStr.append("\t\t(select b.dealer_code\n" );
		sqlStr.append("       from TM_DEALER b\n" );
		sqlStr.append("      where b.dealer_id = NVL(A.SECOND_DEALER_ID, A.DEALER_ID)) as dealer_code1,");

	//	sqlStr.append("       B.DEALER_CODE AS DEALER_CODE,\n" );
		
		
		
		//sqlStr.append("       B.DEALER_SHORTNAME,\n" );
		sqlStr.append("       tvm.GROUP_NAME,V.DELIVERER,\n" );
		sqlStr.append("       V.DELIVERER_PHONE || ' ' || v.deliverer_mobile DELIVERER_PHONE,ba.area_name as yieldly_name\n" );
		sqlStr.append("  FROM TT_AS_REPAIR_ORDER V, TT_AS_WR_APPLICATION A, tm_business_area ba,tm_vhcl_material_group tvm \n" );
		sqlStr.append(" WHERE 1 = 1 and ba.area_id(+) = a.yieldly\n" );
	//	sqlStr.append("   AND B.DEALER_ID = A.DEALER_ID(+)\n" );
		sqlStr.append("   AND A.RO_NO = V.RO_NO(+)\n" );
		DaoFactory.getsql(sqlStr, "a.create_date", MyConstant.onlineDate, 4);//加入时间新分单节点
		sqlStr.append("   AND V.MODEL = tvm.GROUP_CODE(+)\n" );
		if(yieldly!=null){
			sqlStr.append("   AND A.yieldly = '"+yieldly+"'\n" );
		}
		if (Utility.testString(map.get("dealerName"))) {
			sqlStr.append(" AND a.DEALER_name like'%" + map.get("dealerName")
					+ "%' \n");
		}
		if (Utility.testString(map.get("dealerCode"))) {
			sqlStr.append(Utility.getConSqlByParamForEqual(map
					.get("dealerCode"), params, "b", "dealer_code"));
		}
		// 经销商代码
		if (Utility.testString(map.get("dealerId"))) {
			//sqlStr.append(" AND A.DEALER_ID='" + map.get("dealerId") + "' \n");
		//	sqlStr.append(" AND (A.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+map.get("dealerId")+"   connect by PRIOR d.dealer_id = d.parent_dealer_d) or a.second_dealer_id = "+map.get("dealerId")+" )\n");
			sqlStr.append(" AND a.DEALER_ID = " + map.get("dealerId")+ "\n");

		}
		// 索赔单号
		if (Utility.testString(map.get("claimNo"))) {
			sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map.get("claimNo")
					+ "%' \n");
		}
		// 工单号
		if (Utility.testString(map.get("roNo"))) {
			sqlStr.append(" AND A.RO_NO LIKE '%" + map.get("roNo") + "%' \n");
		}
		// 行号
		if (Utility.testString(map.get("lineNo"))) {
			sqlStr.append(" AND A.LINE_NO='" + map.get("lineNo") + "' \n");
		}
		// 索赔类型
		
		DaoFactory.getsql(sqlStr, "A.CLAIM_TYPE", map.get("claimType"), 6);
		if(Utility.testString(map.get("partCode"))){
			sqlStr.append("   AND A.ID in ( SELECT TAWP.ID FROM TT_AS_WR_PARTSITEM TAWP WHERE TAWP.PART_CODE LIKE '%"+map.get("partCode")+"%' GROUP BY TAWP.ID )  \n");
		}
		if(Utility.testString(map.get("wrLabourCode"))){
			sqlStr.append("   AND A.ID in ( SELECT TAWL.ID FROM TT_AS_WR_LABOURITEM TAWL WHERE TAWL.WR_LABOURCODE LIKE '%"+map.get("wrLabourCode")+"%' GROUP BY TAWL.ID )  \n");
		}
//		if (Utility.testString(map.get("claimType"))) {
//			sqlStr
//					.append(" AND A.CLAIM_TYPE='" + map.get("claimType")
//							+ "' \n");
//		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN涓嶄负绌 
			sqlStr.append(" and a.vin in ("+map.get("vin")+")\n");
//			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
//					"\''"), "A"));
		}
		// 工单开始时间
		if (Utility.testString(map.get("roStartdate"))) {
			sqlStr
					.append(" AND A.CREATE_DATE >= to_date('"
							+ map.get("roStartdate")
							+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 工单结束时间
		if (Utility.testString(map.get("roEnddate"))) {
			sqlStr.append(" AND A.CREATE_DATE <= to_date('"
					+ map.get("roEnddate")
					+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 审核时间查询条件
		if(Utility.testString(map.get("approveDate"))){
			sqlStr.append(" AND a.auditing_date >= to_date('"
					+map.get("approveDate")
					+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("approveDate2"))){
			sqlStr.append(" AND a.auditing_date <= to_date('"
					+map.get("approveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("balanceApproveDate"))){
			sqlStr.append(" AND a.account_date >= to_date('"
					+map.get("balanceApproveDate")
					+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("balanceApproveDate2"))){
			sqlStr.append(" AND a.account_date <= to_date('"
					+map.get("balanceApproveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		// 艾春 13.11.20 添加是否二级查询条件
		if(Utility.testString(map.get("isSecond"))){
			if("1".equals(map.get("isSecond"))){
				sqlStr.append(" AND a.second_dealer_id is not null \n");
			}else{
				sqlStr.append(" AND a.second_dealer_id is null \n");
			}
		}
		// 艾春 13.11.20 添加是否二级查询条件
		
		// 申请状态
		if (Utility.testString(map.get("status"))) {
			sqlStr.append(" AND A.STATUS='" + map.get("status") + "' \n");

		} else if (!"track".equals(map.get("track"))) {
			sqlStr.append(" AND (A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_01
					+ "'  \n");
			sqlStr.append(" OR A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_06
					+ "')  \n");

		} else if (!"isApp".equals(map.get("isApp"))) {
			sqlStr.append(" AND (A.STATUS<" + Constant.CLAIM_APPLY_ORD_TYPE_01
					+ " OR A.STATUS >" + Constant.CLAIM_APPLY_ORD_TYPE_01 +") \n");
		}
		if (Utility.testString(map.get("model"))) {
			sqlStr.append(" and v.model = '"+map.get("model")+"'");
		}
		if (Utility.testString(map.get("deliverer"))) {
			sqlStr.append(" and v.deliverer like '%"+map.get("deliverer")+"%' ");
		}
		if (Utility.testString(map.get("delivererPhone"))) {
			sqlStr.append(" and v.deliverer_phone like '%"+map.get("delivererPhone")+"%' ");
		}
		if (Utility.testString(map.get("isImport"))) {
			sqlStr.append(" and a.is_import = '"+map.get("isImport")+"'");
		}
		 //该段代码为屏蔽服务站不看到的工单
	    if (null!=user.getDealerId()) {
	    	sqlStr.append(" and a.ro_no not in (select v.ro_no from tt_as_wr_repair_claim v where v.dealer_id='"+user.getDealerId()+"')");
		}
		sqlStr.append(" ORDER BY A.ID DESC ");
		System.out.println("sqlsql=="+sqlStr.toString());
		logger.info("-----------------------"+sqlStr.toString());
		PageResult<TtAsWrApplicationExtBean> ps = pageQuery(
				TtAsWrApplicationExtBean.class, sqlStr.toString(), params,
				pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> barcodePrintQuery(
			AclUserBean user, Map<String, String> map, List params,
			int pageSize, int curPage) {
    	 
    	String yieldly = map.get("yieldly");
		StringBuffer sqlStr = new StringBuffer();

		sqlStr.append("SELECT A.*,\n" );
		sqlStr.append("       B.DEALER_CODE AS DEALER_CODE,\n" );
		sqlStr.append("       B.DEALER_NAME AS DEALER_NAME,\n" );
		sqlStr.append("       V.DELIVERER,\n" );
		sqlStr.append("       V.DELIVERER_PHONE || ' ' || v.deliverer_mobile DELIVERER_PHONE\n" );
		sqlStr.append("  FROM TT_AS_REPAIR_ORDER V, TT_AS_WR_APPLICATION A, TM_DEALER B\n" );
		sqlStr.append(" WHERE 1 = 1\n" );
		sqlStr.append("   AND B.DEALER_ID = A.DEALER_ID + 0\n" );
		sqlStr.append("   AND A.RO_NO = V.RO_NO(+)\n" );
		
		//只能看见5月1日以后创建的单子
		sqlStr.append("   AND A.Report_Date >= to_date('2012-05-01', 'yyyy-mm-dd hh24:mi:ss')" );
		
		
		if(yieldly!=null){
			sqlStr.append("   AND A.yieldly = '"+yieldly+"'\n" );
		}
		
		if (Utility.testString(map.get("is_print"))) {
			sqlStr.append(" AND A.is_print='" + map.get("is_print")
					+ "' \n");
		}
		
		
		if (Utility.testString(map.get("dealerName"))) {
			sqlStr.append(" AND b.DEALER_name like'%" + map.get("dealerName")
					+ "%' \n");
		}
		if (Utility.testString(map.get("dealerCode"))) {
			sqlStr.append(Utility.getConSqlByParamForEqual(map
					.get("dealerCode"), params, "b", "dealer_code"));
		}
		// 经销商代码
		if (Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" AND A.DEALER_ID='" + map.get("dealerId") + "' \n");
		}
		// 索赔单号
		if (Utility.testString(map.get("claimNo"))) {
			sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map.get("claimNo")
					+ "%' \n");
		}
		// 工单号
		if (Utility.testString(map.get("roNo"))) {
			sqlStr.append(" AND A.RO_NO LIKE '%" + map.get("roNo") + "%' \n");
		}
		// 行号
		if (Utility.testString(map.get("lineNo"))) {
			sqlStr.append(" AND A.LINE_NO='" + map.get("lineNo") + "' \n");
		}
		// 索赔类型
		if (Utility.testString(map.get("claimType"))) {
			sqlStr
					.append(" AND A.CLAIM_TYPE='" + map.get("claimType")
							+ "' \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN涓嶄负绌 
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "A"));
		}
		// 工单开始时间
		if (Utility.testString(map.get("roStartdate"))) {
			sqlStr
					.append(" AND A.CREATE_DATE >= to_date('"
							+ map.get("roStartdate")
							+ "', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 工单结束时间
		if (Utility.testString(map.get("roEnddate"))) {
			sqlStr.append(" AND A.CREATE_DATE <= to_date('"
					+ map.get("roEnddate")
					+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 审核时间查询条件
		if(Utility.testString(map.get("approveDate"))){
			sqlStr.append(" AND a.auditing_date >= to_date('"
					+map.get("approveDate")
					+"','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("approveDate2"))){
			sqlStr.append(" AND a.auditing_date <= to_date('"
					+map.get("approveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("balanceApproveDate"))){
			sqlStr.append(" AND a.account_date >= to_date('"
					+map.get("balanceApproveDate")
					+"','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("balanceApproveDate2"))){
			sqlStr.append(" AND a.account_date <= to_date('"
					+map.get("balanceApproveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		// 申请状态
		
		sqlStr.append(" AND A.STATUS  in (" + Constant.CLAIM_APPLY_ORD_TYPE_04+"," +Constant.CLAIM_APPLY_ORD_TYPE_07+","+Constant.CLAIM_APPLY_ORD_TYPE_08 +") \n");
		
		if (Utility.testString(map.get("status"))) {
			sqlStr.append(" AND A.STATUS='" + map.get("status") + "' \n");

		} else if (!"track".equals(map.get("track"))) {
			sqlStr.append(" AND (A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_01
					+ "'  \n");
			sqlStr.append(" OR A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_06
					+ "')  \n");

		} else if (!"isApp".equals(map.get("isApp"))) {
			sqlStr.append(" AND (A.STATUS<" + Constant.CLAIM_APPLY_ORD_TYPE_01
					+ " OR A.STATUS >" + Constant.CLAIM_APPLY_ORD_TYPE_01 +") \n");
		}
		if (Utility.testString(map.get("deliverer"))) {
			sqlStr.append(" and v.deliverer like '%"+map.get("deliverer")+"%' ");
		}
		if (Utility.testString(map.get("delivererPhone"))) {
			sqlStr.append(" and v.deliverer_phone like '%"+map.get("delivererPhone")+"%' ");
		}
		sqlStr.append(" ORDER BY A.ID DESC ");
		System.out.println("sqlsql=="+sqlStr.toString());
		logger.info("-----------------------"+sqlStr.toString());
		return dao.pageQuery(sqlStr.toString(), null, dao.getFunName(),pageSize , curPage);
	}
	
	
	public PageResult<TtAsWrApplicationExtBean> queryDealerApplicationCVS(
			AclUserBean user, Map<String, String> map, List params,
			int pageSize, int curPage) {
    	 
    	String yieldly = map.get("yieldly");
		StringBuffer sqlStr = new StringBuffer();

		sqlStr.append("SELECT A.*,\n" );
		sqlStr.append("       B.DEALER_CODE AS DEALER_CODE,\n" );
		sqlStr.append("       B.DEALER_NAME AS DEALER_NAME,\n" );
		sqlStr.append("       V.DELIVERER,\n" );
		sqlStr.append("       V.DELIVERER_PHONE\n" );
		sqlStr.append("  FROM TT_AS_REPAIR_ORDER V, TT_AS_WR_APPLICATION A, TM_DEALER B\n" );
		sqlStr.append(" WHERE 1 = 1\n" );
		sqlStr.append("   AND B.DEALER_ID = A.DEALER_ID + 0\n" );
		sqlStr.append("   AND A.RO_NO = V.RO_NO(+)\n" );
		if(yieldly!=null){
			sqlStr.append("   AND A.yieldly = '"+yieldly+"'\n" );
		}
		if (Utility.testString(map.get("dealerName"))) {
			sqlStr.append(" AND b.DEALER_name like'%" + map.get("dealerName")
					+ "%' \n");
		}
		if (Utility.testString(map.get("dealerCode"))) {
			sqlStr.append(Utility.getConSqlByParamForEqual(map
					.get("dealerCode"), params, "b", "dealer_code"));
		}
		// 经销商代码
		if (Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" AND A.DEALER_ID='" + map.get("dealerId") + "' \n");
		}
		// 索赔单号
		if (Utility.testString(map.get("claimNo"))) {
			sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map.get("claimNo")
					+ "%' \n");
		}
		// 工单号
		if (Utility.testString(map.get("roNo"))) {
			sqlStr.append(" AND A.RO_NO LIKE '%" + map.get("roNo") + "%' \n");
		}
		// 行号
		if (Utility.testString(map.get("lineNo"))) {
			sqlStr.append(" AND A.LINE_NO='" + map.get("lineNo") + "' \n");
		}
		// 索赔类型
		if (Utility.testString(map.get("claimType"))) {
			sqlStr
					.append(" AND A.CLAIM_TYPE='" + map.get("claimType")
							+ "' \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN涓嶄负绌 
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "A"));
		}
		// 工单开始时间
		if (Utility.testString(map.get("roStartdate"))) {
			sqlStr
					.append(" AND A.CREATE_DATE >= to_date('"
							+ map.get("roStartdate")
							+ "', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 工单结束时间
		if (Utility.testString(map.get("roEnddate"))) {
			sqlStr.append(" AND A.CREATE_DATE <= to_date('"
					+ map.get("roEnddate")
					+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 审核时间查询条件
		if(Utility.testString(map.get("approveDate"))){
			sqlStr.append(" AND a.account_date >= to_date('"
					+map.get("approveDate")
					+"','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("approveDate2"))){
			sqlStr.append(" AND a.account_date <= to_date('"
					+map.get("approveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		// 申请状态
		if (Utility.testString(map.get("status"))) {
			sqlStr.append(" AND A.STATUS='" + map.get("status") + "' \n");

		} else if (!"track".equals(map.get("track"))) {
			sqlStr.append(" AND (A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_01
					+ "'  \n");
			sqlStr.append(" OR A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_06
					+ "')  \n");

		} else if (!"isApp".equals(map.get("isApp"))) {
			sqlStr.append(" AND (A.STATUS<" + Constant.CLAIM_APPLY_ORD_TYPE_01
					+ " OR A.STATUS >" + Constant.CLAIM_APPLY_ORD_TYPE_01 +") \n");
		}
		if (Utility.testString(map.get("deliverer"))) {
			sqlStr.append(" and v.deliverer like '%"+map.get("deliverer")+"%' ");
		}
		if (Utility.testString(map.get("delivererPhone"))) {
			sqlStr.append(" and v.deliverer_phone like '%"+map.get("delivererPhone")+"%' ");
		}
		sqlStr.append(" ORDER BY A.ID DESC ");
		System.out.println("sqlsql=="+sqlStr.toString());
		logger.info("-----------------------"+sqlStr.toString());
		PageResult<TtAsWrApplicationExtBean> ps = pageQuery(
				TtAsWrApplicationExtBean.class, sqlStr.toString(), params,
				pageSize, curPage);
		return ps;
	}
    
    public PageResult<TtAsWrApplicationExtBean> queryDealerDeleteApplication(
			AclUserBean user, Map<String, String> map, List params,
			int pageSize, int curPage) {
    	 
    	String yieldly = map.get("yieldly");
		StringBuffer sqlStr = new StringBuffer();

		sqlStr.append("SELECT A.*,\n" );
		sqlStr.append("       B.DEALER_CODE AS DEALER_CODE,\n" );
		//sqlStr.append("       B.DEALER_SHORTNAME,\n" );
		sqlStr.append("       V.DELIVERER,\n" );
		sqlStr.append("       V.DELIVERER_PHONE\n" );
		sqlStr.append("  FROM TT_AS_REPAIR_ORDER V, TT_AS_WR_APPLICATION A, TM_DEALER B\n" );
		//sqlStr.append(" WHERE 1 = 1 and v.ORDER_VALUABLE_TYPE='"+Constant.RO_PRO_STATUS_02+"' and A.status in('"+Constant.CLAIM_APPLY_ORD_TYPE_01+"','"+Constant.CLAIM_APPLY_ORD_TYPE_06+"')\n" );
		sqlStr.append(" WHERE 1 = 1  and A.status in('"+Constant.CLAIM_APPLY_ORD_TYPE_01+"','"+Constant.CLAIM_APPLY_ORD_TYPE_06+"')\n" );
		sqlStr.append("   AND B.DEALER_ID = A.DEALER_ID(+)\n" );
		sqlStr.append("   AND A.RO_NO = V.RO_NO(+)\n" );
		if (Utility.testString(yieldly)) {
			sqlStr.append(" AND A.YIELDLY='" + yieldly + "' \n");
		}
	
		// 经销商代码
		if (Utility.testString(map.get("dealerId"))) {
			//sqlStr.append(" AND A.DEALER_ID='" + map.get("dealerId") + "' \n");
			sqlStr.append(" AND (A.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+map.get("dealerId")+"   connect by PRIOR d.dealer_id = d.parent_dealer_d)or a.second_dealer_id =  "+map.get("dealerId")+" ) \n");
		}
		// 索赔单号
		if (Utility.testString(map.get("claimNo"))) {
			sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map.get("claimNo")
					+ "%' \n");
		}
		// 工单号
		if (Utility.testString(map.get("roNo"))) {
			sqlStr.append(" AND A.RO_NO LIKE '%" + map.get("roNo") + "%' \n");
		}
		
		// 索赔类型
		if (Utility.testString(map.get("claimType"))) {
			sqlStr
					.append(" AND A.CLAIM_TYPE='" + map.get("claimType")
							+ "' \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN涓嶄负绌�
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "A"));
		}
		// 工单开始时间
		if (Utility.testString(map.get("roStartdate"))) {
			sqlStr
					.append(" AND A.CREATE_DATE >= to_date('"
							+ map.get("roStartdate")
							+ "', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 工单结束时间
		if (Utility.testString(map.get("roEnddate"))) {
			sqlStr.append(" AND A.CREATE_DATE <= to_date('"
					+ map.get("roEnddate")
					+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 审核时间查询条件
		if(Utility.testString(map.get("approveDate"))){
			sqlStr.append(" AND a.auditing_date >= to_date('"
					+map.get("approveDate")
					+"','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("approveDate2"))){
			sqlStr.append(" AND a.auditing_date <= to_date('"
					+map.get("approveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		sqlStr.append(" ORDER BY A.ID DESC ");
		logger.info("-----------------------"+sqlStr.toString());
		System.out.println("sql=="+sqlStr);
		PageResult<TtAsWrApplicationExtBean> ps = pageQuery(
				TtAsWrApplicationExtBean.class, sqlStr.toString(), params,
				pageSize, curPage);
		return ps;
	}
    public PageResult<Map<String, Object>> query1(String[] code,int pageSize, int currPage) {
    	
    	StringBuffer sb= new StringBuffer();
    	sb.append("select tm.dealer_id,\n" );
    	sb.append("       tm.dealer_code,\n" );
    	sb.append("       tm.dealer_name,\n" );
    	sb.append("       vod.root_org_name,\n" );
    	sb.append("       vod.org_name\n" );
    	sb.append("  from tm_dealer tm\n" );
    	sb.append("  left join vw_org_dealer_service vod\n" );
    	sb.append("    on tm.dealer_id = vod.dealer_id\n" );
    	sb.append(" where 1=1 \n");
		DaoFactory.getsql(sb, "tm.dealer_code", DaoFactory.getSqlByarrIn(code), 6);
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		
		return list;

    }
    
 public PageResult<Map<String, Object>> query2(Map<String, String> map,int pageSize, int currPage) {
    	
    	StringBuffer sb= new StringBuffer();
    	String dealerCode = map.get("dealerCode");
    	String dealerName = map.get("dealerName");
    	
    	sb.append("select a.dealer_code, a.dealer_shortname, decode(b.part_num,'',0,b.part_num) as part_num,b.id,a.dealer_id\n" );
    	sb.append("  from tm_dealer a\n" );
    	sb.append("  left join tt_AS_wr_Negative_inventory b\n" );
    	sb.append("    on a.dealer_id = b.dealer_id\n" );
    	sb.append(" where a.dealer_type = '10771002'");
    	
    	if(Utility.testString(dealerCode)){
			sb.append(" AND a.dealer_code like '%" + dealerCode + "%' \n");
		}
    	
    	if(Utility.testString(dealerName)){
			sb.append(" AND a.dealer_name like '%" + dealerName + "%' \n");
		}
    	
    	sb.append(" ORDER BY a.dealer_id DESC ");
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		
		return list;

    }
 
	public PageResult<TtAsWrWinterMaintenBean> queryWinterMainten(AclUserBean user, Map<String, String> map, List params,
			int pageSize, int curPage) {
		
		StringBuffer sb= new StringBuffer();
		sb.append("select tt.id,\n" );
		sb.append("       tt.amount,\n" );
		sb.append("       tt.start_date,\n" );
		sb.append("       tt.end_date,\n" );
		sb.append("       tt.create_by,\n" );
		sb.append("       tt.create_date,\n" );
		sb.append("       tt.status \n" );
		sb.append("  from TT_AS_WR_WINTER_MAINTEN tt\n" );
		sb.append(" 	where 1=1");
		if(Utility.testString(map.get("amount"))){
			sb.append(" AND tt.amount='" + map.get("amount") + "' \n");
		}
		if(Utility.testString(map.get("startDate"))){
			sb
			.append(" AND tt.start_date >= to_date('"
					+ map.get("startDate")
					+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("endDate"))){
			sb
			.append(" AND tt.end_date <= to_date('"
					+ map.get("endDate")
					+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		sb.append(" ORDER BY tt.id DESC ");
		logger.info("-----------------------"+sb.toString());
		PageResult<TtAsWrWinterMaintenBean> ps = pageQuery(
				TtAsWrWinterMaintenBean.class, sb.toString(), params,
				pageSize, curPage);
		return ps;
	}
			
    public PageResult<TtAsWrApplicationExtBean> queryApplication1(
			AclUserBean user, Map<String, String> map, List params,
			int pageSize, int curPage) {
    	 
    	String yieldly = map.get("yieldly");
    	String yieldlys = map.get("yieldlys");
		StringBuffer sqlStr = new StringBuffer();

		sqlStr.append("SELECT A.*,\n" );
		sqlStr.append("       B.DEALER_CODE AS DEALER_CODE,\n" );
		//sqlStr.append("       B.dealer_shortname,\n" );
		sqlStr.append("       V.DELIVERER,\n" );
		sqlStr.append("       V.DELIVERER_PHONE,\n" );
		sqlStr.append("(SELECT max(WA.Audit_Date) Audit_Date\n");
		sqlStr.append("            FROM tt_AS_wr_app_AUDIT_DETAIL WA\n");
		sqlStr.append("              WHERE WA.Claim_Id = A.ID AND Rownum=1) AS audit_Date,\n"); 
		sqlStr.append("(SELECT MAX(tu.NAME) NAME\n");
		sqlStr.append("                 FROM TT_AS_WR_APP_AUDIT_DETAIL AP\n");
		sqlStr.append("                 LEFT  JOIN TC_USER TU ON AP.Audit_By = TU.USER_ID\n");
		sqlStr.append("          WHERE   a.ID = ap.claim_id AND Rownum=1\n");
		if(Utility.testString(map.get("person"))){
			sqlStr.append(" and u.name like '%"+map.get("person")+"%'  \n");	
		}
		sqlStr.append("          ) AS FORE_AUTH_PERSON"); 

		sqlStr.append("  FROM TT_AS_REPAIR_ORDER V,TT_AS_WR_APPLICATION A, TM_DEALER B\n" );
		sqlStr.append("  WHERE 1 = 1\n" );
		if(Utility.testString(map.get("person"))){
			//sqlStr.append("	and a.id in(\n");
			//sqlStr.append("	select  id  from  tt_as_wr_appauthitem wa where wa.create_by in(\n" );
			//sqlStr.append(" select u.user_id from tc_user u where u.name like '%"+map.get("person")+"%' ))\n");
			sqlStr.append("and exists (\n");
			sqlStr.append("      select 1 from tt_as_wr_appauthitem wa where wa.id = a.id and exists(\n");  
			sqlStr.append("          select 1 from tc_user u where u.name like '%"+map.get("person")+"%' and u.user_id = wa.create_by\n");  
			sqlStr.append("      )\n");  
			sqlStr.append("  )\n");
		}
		sqlStr.append(" AND A.STATUS in (" + Constant.CLAIM_APPLY_ORD_TYPE_01+ "," + Constant.CLAIM_APPLY_ORD_TYPE_06+","+Constant.CLAIM_APPLY_ORD_TYPE_03+")  \n");
		sqlStr.append("   AND B.DEALER_ID = nvl(a.second_dealer_id,a.dealer_id)\n" );
		sqlStr.append("   AND A.RO_NO = V.RO_NO(+)\n" );
		if (Utility.testString(yieldly)) {
			sqlStr.append(" AND A.YIELDLY='" + yieldly + "' \n");
		} else if(Utility.testString(yieldlys)){
			sqlStr.append(" AND A.YIELDLY IN (").append(yieldlys).append(")\n");
		}
		if (Utility.testString(map.get("dealerName"))) {
			sqlStr.append(" AND b.DEALER_name like'%" + map.get("dealerName")
					+ "%' \n");
		}
		if (Utility.testString(map.get("dealerCode"))) {
			sqlStr.append(Utility.getConSqlByParamForEqual(map
					.get("dealerCode"), params, "b", "dealer_code"));
		}
		// 经销商代码
		if (Utility.testString(map.get("dealerId"))) {
//			sqlStr.append("AND DECODE(a.SECOND_DEALER_ID, null, b.DEALER_ID, a.SECOND_DEALER_ID) IN\n");
//			sqlStr.append("      (SELECT d.DEALER_ID\n");
//			sqlStr.append("         FROM TM_DEALER D\n");
//			sqlStr.append("        START WITH d.DEALER_ID = "+map.get("dealerId")+"\n");
//			sqlStr.append("       CONNECT BY PRIOR d.DEALER_ID = d.PARENT_DEALER_D)"); 
			sqlStr.append(" and nvl(a.SECOND_DEALER_ID,b.DEALER_ID) ="+map.get("dealerId")+"\n");
		}
		
		
		
		// 行号
		if (Utility.testString(map.get("lineNo"))) {
			sqlStr.append(" AND A.LINE_NO='" + map.get("lineNo") + "' \n");
		}
		// 索赔类型
		if (Utility.testString(map.get("claimType"))) {
			sqlStr
					.append(" AND A.CLAIM_TYPE='" + map.get("claimType")
							+ "' \n");
		}
		// 工单开始时间
		if (Utility.testString(map.get("roStartdate"))) {
			sqlStr
					.append(" AND A.CREATE_DATE >= to_date('"
							+ map.get("roStartdate")
							+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 工单结束时间
		if (Utility.testString(map.get("roEnddate"))) {
			sqlStr.append(" AND A.CREATE_DATE <= to_date('"
					+ map.get("roEnddate")
					+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		// 审核时间
		if(Utility.testString(map.get("approveDate"))){
			sqlStr.append(" and a.auditing_date >= to_date('"
					+map.get("approveDate")
					+"00:00:00','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("approveDate2"))){
			sqlStr.append(" and a.auditing_date <= to_date('"
					+map.get("approveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}
		// 申请状态
		if (Utility.testString(map.get("status"))) {
			sqlStr.append(" AND A.STATUS=" + map.get("status") + " \n");

		} 
		// 索赔单号
		if (Utility.testString(map.get("claimNo"))) {
			sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map.get("claimNo")
					+ "%' \n");
		}
		// 工单号
		if (Utility.testString(map.get("roNo"))) {
			sqlStr.append(" AND A.RO_NO LIKE '%" + map.get("roNo") + "%' \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "A"));
		}
		if (Utility.testString(map.get("deliverer"))) {
			sqlStr.append(" and v.deliverer like '%"+map.get("deliverer")+"%' ");
		}
		if (Utility.testString(map.get("delivererPhone"))) {
			sqlStr.append(" and v.deliverer_phone like '%"+map.get("delivererPhone")+"%' ");
		}
		if(Utility.testString(map.get("isImport"))){
			sqlStr.append(" and A.is_import = '"+map.get("isImport")+"'");
		}
		sqlStr.append(" ORDER BY A.ID DESC ");
		logger.info("-----------------------"+sqlStr.toString());
		// sqlStr.append(con);
		PageResult<TtAsWrApplicationExtBean> ps = pageQuery(
				TtAsWrApplicationExtBean.class, sqlStr.toString(), params,
				pageSize, curPage);
		return ps;
	}
    /**
     * 车厂索赔单查询 zyw 重构修改2014-8-8  删减代码80%
     */
   public PageResult<TtAsWrApplicationExtBean> queryApplication2(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
	   	String dealerCode = DaoFactory.getParam(request,"DEALER_CODE");
		String dealerName = DaoFactory.getParam(request,"DEALER_NAME");
		String roNo = DaoFactory.getParam(request,"RO_NO");
		String lineNo = DaoFactory.getParam(request,"LINE_NO");
		String claimNo = DaoFactory.getParam(request,"CLAIM_NO");
		String claimType = DaoFactory.getParam(request,"CLAIM_TYPE");
		String vin = DaoFactory.getParam(request,"VIN");
		String roStartdate = DaoFactory.getParam(request,"RO_STARTDATE");
		String roEnddate = DaoFactory.getParam(request,"RO_ENDDATE");
		String approveDate = DaoFactory.getParam(request,"approve_date");// 审核时间
		String approveDate2 = DaoFactory.getParam(request,"approve_date2");
		String status = DaoFactory.getParam(request,"STATUS");
		String person = DaoFactory.getParam(request,"PERSON");
		String yieldly = DaoFactory.getParam(request,"YIELDLY");// 查询条件--产地
		String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());// 该用户拥有的产地权限
		String isImport = DaoFactory.getParam(request,"is_import");
		String foreAuthPerson = DaoFactory.getParam(request,"foreAuthPerson");
		String model = DaoFactory.getParam(request,"model");
		String partCode = DaoFactory.getParam(request,"partCode");
		String wrLabourCode = DaoFactory.getParam(request,"wrLabourCode");
		
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select t.* from (SELECT A.*,\n" );
		sqlStr.append("       B.DEALER_CODE AS DEALER_CODE,\n" );
		sqlStr.append("       V.DELIVERER,\n" );
		sqlStr.append("       V.DELIVERER_PHONE,\n" );
		sqlStr.append("       C.GROUP_NAME,(select  case when  max(h.id) is null then ' ' else '扣件' end   from tt_as_wr_partsitem H where  h.id = A.id and h.BALANCE_QUANTITY < h.QUANTITY and  h.IS_RETURN  = 95361001 ) kou_jian,\n" );
		sqlStr.append("       A.Auditing_Date AS audit_Date,\n");
		sqlStr.append("       tc.name,\n");
//		sqlStr.append("(SELECT max(WA.Audit_Date) Audit_Date\n");
//		sqlStr.append("            FROM tt_AS_wr_app_AUDIT_DETAIL WA\n");
//		sqlStr.append("              WHERE WA.Claim_Id = A.ID ) AS audit_Date,\n"); 
//		sqlStr.append("(select tu.name from TC_USER tu where tu.user_id =(SELECT max(WA.AUDIT_BY) AUDIT_BY\n" );
//		sqlStr.append("            FROM tt_AS_wr_app_AUDIT_DETAIL WA\n" );
//		sqlStr.append("              WHERE WA.Claim_Id = A.ID  and WA.Audit_Date=(SELECT max(WA.Audit_Date) Audit_Date\n" );
//		sqlStr.append("            FROM tt_AS_wr_app_AUDIT_DETAIL WA\n" );
//		sqlStr.append("              WHERE WA.Claim_Id = A.ID ))) as  FORE_AUTH_PERSON");
		sqlStr.append("           tc.name    as  FORE_AUTH_PERSON");
		sqlStr.append("  FROM TT_AS_REPAIR_ORDER V,TT_AS_WR_APPLICATION A, TM_DEALER B,TM_VHCL_MATERIAL_GROUP C\n," );
		sqlStr.append(" tc_user tc \n ");
		sqlStr.append("  WHERE 1 = 1\n" );
		sqlStr.append(" and A.Auditing_Man =  tc.user_id(+)\n");
		if(Utility.testString(person)){
			sqlStr.append("and exists (\n");
			sqlStr.append("      select 1 from tt_as_wr_appauthitem wa where wa.id = a.id and exists(\n");  
			sqlStr.append("          select 1 from tc_user u where u.name like '%"+person+"%' and u.user_id = wa.create_by\n");  
			sqlStr.append("      )\n");  
			sqlStr.append("  )\n");
		}
		sqlStr.append("   AND V.MODEL = C.GROUP_CODE(+)\n" );
		sqlStr.append("   AND A.STATUS not in (" + Constant.CLAIM_APPLY_ORD_TYPE_01+ ")  \n");
		sqlStr.append("   AND B.DEALER_ID = nvl(a.second_dealer_id,a.dealer_id)\n" );
		sqlStr.append("   AND A.RO_NO = V.RO_NO(+)\n" );
		DaoFactory.getsql(sqlStr, "A.YIELDLY", yieldly, 1);
		DaoFactory.getsql(sqlStr, "A.YIELDLY", yieldlys, 6);
		DaoFactory.getsql(sqlStr, "b.dealer_name", dealerName, 2);
		DaoFactory.getsql(sqlStr, "b.dealer_Code", dealerCode, 6);
		DaoFactory.getsql(sqlStr, "A.LINE_NO", lineNo, 1);
		DaoFactory.getsql(sqlStr, "A.CLAIM_TYPE", claimType, 1);
		DaoFactory.getsql(sqlStr, "A.CREATE_DATE", roStartdate, 3);
		DaoFactory.getsql(sqlStr, "A.CREATE_DATE", roEnddate, 4);
		DaoFactory.getsql(sqlStr, "A.STATUS", status, 1);
		DaoFactory.getsql(sqlStr, "A.claim_No", claimNo.toUpperCase(), 2);
		DaoFactory.getsql(sqlStr, "A.RO_NO", roNo.toUpperCase(), 2);
		DaoFactory.getsql(sqlStr, "V.MODEL", model, 1);
		if(Utility.testString(partCode)){
			sqlStr.append("   AND A.ID in ( SELECT TAWP.ID FROM TT_AS_WR_PARTSITEM TAWP WHERE TAWP.PART_CODE LIKE '%"+partCode+"%' GROUP BY TAWP.ID )  \n");
		}
		if(Utility.testString(wrLabourCode)){
			sqlStr.append("   AND A.ID in ( SELECT TAWL.ID FROM TT_AS_WR_LABOURITEM TAWL WHERE TAWL.WR_LABOURCODE LIKE '%"+wrLabourCode+"%' GROUP BY TAWL.ID )  \n");
		}
		// 车辆VIN码
		if (Utility.testString(vin)) {sqlStr.append(GetVinUtil.getVins(vin.replaceAll("'","\''"), "A"));
		}
		DaoFactory.getsql(sqlStr, "A.is_import", isImport, 1);
		sqlStr.append(" ORDER BY A.ID DESC ) t where 1=1 \n");
		DaoFactory.getsql(sqlStr, "t.FORE_AUTH_PERSON", foreAuthPerson, 2);
		DaoFactory.getsql(sqlStr, "t.Audit_Date", approveDate, 3);
		DaoFactory.getsql(sqlStr, "t.Audit_Date", approveDate2, 4);
		logger.info("-----------------------"+sqlStr.toString());
		PageResult<TtAsWrApplicationExtBean> ps = pageQuery(TtAsWrApplicationExtBean.class, sqlStr.toString(), null,pageSize, currPage);
		return ps;
	}
   
   
   /**
    * 二次入库索赔单明细查询
    */
  public PageResult<Map<String,Object>> queryOldPartReinDetail(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
		String claimNo = DaoFactory.getParam(request,"CLAIM_NO");
		String roStartdate = DaoFactory.getParam(request,"RO_STARTDATE");
		String roEnddate = DaoFactory.getParam(request,"RO_ENDDATE");
		String dealerCode = DaoFactory.getParam(request,"dealerCode");
		String is_bc = DaoFactory.getParam(request,"is_bc");
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select distinct tas.id,\n" );
		sql.append("                tas.claim_id,\n" );
		sql.append("                taw.claim_no claim_no,\n" );
		sql.append("                to_char(tas.create_date, 'yyyy-mm-dd') create_date,\n" );
		sql.append("                td.dealer_name,\n" );
		sql.append("                tas.dealer_code,\n" );
		sql.append("                tas.part_code,\n" );
		sql.append("                tas.is_main_code,\n" );
		sql.append("                tas.amount,\n" );
		sql.append("                tas.balance_no,(select c.name from tc_user c where c.user_id=tas.create_by) as create_by,tas.is_Bc,\n" );
		sql.append("                tas.remark,\n" );
		sql.append("                (select max(dd.in_warhouse_date)\n" );
		sql.append("                   from tt_as_wr_old_returned dd\n" );
		sql.append("                  where dd.id = d.return_id) in_warhouse_date\n" );
		sql.append("  from tt_as_second_in_store_detail tas\n" );
		sql.append("  left join tt_as_wr_application taw\n" );
		sql.append("    on tas.claim_id = taw.id\n" );
		sql.append("  left join tm_dealer td\n" );
		sql.append("    on tas.dealer_id = td.dealer_id\n" );
		sql.append("  left join tt_as_wr_old_returned_detail d\n" );
		sql.append("    on tas.claim_id = d.claim_id\n" );
		sql.append(" WHERE 1 = 1");

		DaoFactory.getsql(sql, "tas.CREATE_DATE", roStartdate, 3);
		DaoFactory.getsql(sql, "tas.CREATE_DATE", roEnddate, 4);
		DaoFactory.getsql(sql, "td.dealer_code", dealerCode, 6);
		DaoFactory.getsql(sql, "tas.is_bc", is_bc, 1);
		DaoFactory.getsql(sql, "taw.claim_No", claimNo.toUpperCase(), 2);
		sql.append(" ORDER BY ID DESC  \n");
		PageResult<Map<String, Object>> ps=pageQuery(sql.toString(), null,getFunName(), pageSize, currPage);
		return ps;
	}
	/**
	 * 
	 * @Title: queryApplication
	 * @Description: TODO(查询索赔单)
	 * @param
	 * @param user
	 * @param
	 * @param con
	 * @param
	 * @param pageSize
	 * @param
	 * @param curPage
	 * @param
	 * @return 设定文件
	 * @return PageResult<TtAsWrApplicationExtPO> 返回类型 
	 * @throws
	 */
	public PageResult<Map<String, Object>> querydate(
			AclUserBean user, TtAsWrApplicationBean po, List params,
			int pageSize, int curPage) {
    	 
		StringBuffer sql= new StringBuffer();
		sql.append("select t.ID, t.CLAIM_NO,\n" );
		sql.append("      to_char(t.INVOICE_DATE,'yyyy-mm-dd') as INVOICE_DATE,\n" );
		sql.append("        to_char(t.POST_DATE,'yyyy-mm-dd') as POST_DATE,\n" );
		sql.append("       to_char(t.TICK_DATE,'yyyy-mm-dd') as TICK_DATE,\n" );
		sql.append("       to_char(t.RO_STARTDATE,'yyyy-mm-dd') as RO_STARTDATE ,\n" );
		sql.append("        to_char(t.RO_ENDDATE,'yyyy-mm-dd') as RO_ENDDATE ,\n" );
		sql.append("        to_char(t.PROOF_DATE,'yyyy-mm-dd') as PROOF_DATE ,\n" );
		sql.append("       t.CLAIM_TYPE,\n" );
		sql.append("       c.AREA_NAME as YIELDLY,\n" );
		sql.append("       d.DEALER_NAME,\n" );
		sql.append("       b.GROUP_NAME,\n" );
		sql.append("       t.VIN,\n" );
		sql.append("       t.REPAIR_TOTAL,\n" );
		sql.append("       t.GROSS_CREDIT,\n" );
		sql.append("       t.STATUS\n" );
		sql.append("  from TT_AS_WR_APPLICATION t\n" );
		sql.append("  left JOIN TM_DEALER d on d.DEALER_ID = t.DEALER_ID\n" );
		sql.append("  left JOIN TM_BUSINESS_AREA c on c.AREA_ID = t.YIELDLY\n" );
		sql.append(" left JOIN TM_VHCL_MATERIAL_GROUP b on t.MODEL_CODE = b.GROUP_CODE ");
		sql.append("  where ( t.STATUS ="+Constant.CLAIM_APPLY_ORD_TYPE_07+" or t.STATUS ="+Constant.CLAIM_APPLY_ORD_TYPE_08+" OR t.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_09+" OR t.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_10+"  )");
		if (Utility.testString(po.getClaimNo())) {
			sql.append(" AND t.CLAIM_NO like '%"+po.getClaimNo()+"%'");
		}  
		if(Utility.testString(po.getModelCode())){
			String[] mds = po.getModelCode().split(",");
			StringBuffer sb = new StringBuffer();
			for(int j = 0 ;j<mds.length;j++)
			{
				if(j == mds.length-1)
				{
					sb.append("'"+mds[j]+"'");
				}else {
					sb.append("'"+mds[j]+"',");
				}
				
			}
			
			sql.append(" AND t.MODEL_CODE IN (").append(sb.toString()).append(")\n");
		}
		if (Utility.testString(po.getVin())) {
			sql.append(" AND t.vin like '%"+po.getVin()+"%'");
		}
		
		if (Utility.testString(""+po.getDealerId())) {
			sql.append(" AND t.DEALER_ID IN ( "+po.getDealerId() +")");
		}
		
		if (Utility.testString(po.getDealerName())) {
			sql.append(" AND d.DEALER_NAME like'%" + po.getDealerName()
					+ "%' \n");
		}
		if (Utility.testString(""+po.getClaimType())) {
			sql.append(" AND t.CLAIM_TYPE = "+po.getClaimType());
		}
		// 经销商代码
		if (Utility.testString(po.getYieldly())) {
			sql.append(" AND c.AREA_ID='" + po.getYieldly() + "' \n");
		}
		
		
		if (Utility.testString(""+po.getLastStatus())) {
			sql.append(" AND t.STATUS=" + po.getLastStatus()
							+ " \n");
		}
		// 工单开始时间
		if (Utility.testString(po.getStartDate())) {
			sql.append(" AND t.RO_STARTDATE >= to_date('"
							+ po.getStartDate()
							+ "', 'yyyy-mm-dd') \n");
		}
		// 工单结束时间
		if (Utility.testString(po.getEndDate())) {
			sql.append(" AND t.RO_STARTDATE <= to_date('"
					+ po.getStartDate()
					+ "', 'yyyy-mm-dd') \n");
		}
		// 审核时间
		// 工单开始时间
		if (Utility.testString(po.getStartDate2())) {
			sql.append(" AND t.RO_ENDDATE >= to_date('"
							+ po.getStartDate2()
							+ "', 'yyyy-mm-dd') \n");
		}
		// 工单结束时间
		if (Utility.testString(po.getEndDate2())) {
			sql.append(" AND t.RO_ENDDATE <= to_date('"
					+ po.getStartDate2()
					+ "', 'yyyy-mm-dd') \n");
		}
		
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>)pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 
	 * @Title: queryApplicationById
	 * @Description: TODO(查询索赔单信息)
	 * @param
	 * @param id
	 * @param
	 * @return 设定文件
	 * @return TtAsWrApplicationExtPO 返回类型
	 * @throws
	 */
	public TtAsWrApplicationExtPO queryApplicationById(String id) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select rr.deliverer,rr.deliverer_phone,c.address ctm_address,c.ctm_name,c.main_phone ctm_phone,g.group_name package_Name,g.group_code package_code,c.code_desc car_Use_Type,v.mileage,nvl(v.free_times,0)free_Times,a.*,aa.activity_name as campaign_name,v.model_id as model_id,v.PURCHASED_DATE as purchased_date,v.product_date,v.color,b.dealer_code as dealer_code, a.dealer_name, \n");
		sqlStr.append("o.start_time as start_time,o.end_time as end_time,o.out_person as out_person,\n");
		sqlStr.append("o.out_site as out_site,o.out_licenseno as out_licenseno,o.from_adress as from_adress,\n");
		sqlStr.append("o.end_adress as end_adress,o.out_mileage as out_mileages, V.VEHICLE_ID,C.CTM_ID,\n");
		sqlStr.append("rr.in_mileage as in_mileage , ba.area_name yieldly_name, tdd.claim_director_telphone,a.serve_advisor ");
		sqlStr.append(" from TT_AS_WR_APPLICATION a   \n");
		sqlStr.append(" left outer join tt_as_wr_outrepair o on o.ro_no=a.ro_no \n");
		sqlStr.append(" left outer join TM_DEALER b on nvl(a.second_dealer_id,a.dealer_id)=b.dealer_id \n");
		sqlStr.append(" left join tm_dealer_detail tdd on b.dealer_id = tdd.fk_dealer_id\n");
		sqlStr.append(" left outer join TM_VEHICLE v on v.vin=a.vin \n");
		sqlStr.append(" LEFT JOIN tt_dealer_actual_sales s ON v.vehicle_id = s.vehicle_id AND s.is_return=10041002\n");
		sqlStr.append(" LEFT JOIN tt_customer c ON c.ctm_id = s.ctm_id\n"); 

		sqlStr.append(" LEFT JOIN TM_VHCL_MATERIAL_GROUP g ON g.group_id = v.package_id AND g.group_level=4\n"); 
		sqlStr.append("   left join tm_business_area ba on ba.area_id=v.yieldly \n");
		sqlStr.append(" left outer join TT_AS_ACTIVITY AA ON AA.ACTIVITY_CODE=A.CAMPAIGN_CODE \n");
		sqlStr.append(" left outer join TT_AS_REPAIR_ORDER RR ON RR.RO_NO=A.RO_NO \n");
		sqlStr.append("  LEFT JOIN tc_code c ON c.code_id = rr.car_use_type\n"); 

		sqlStr.append(" where 1=1 ");
		sqlStr.append(" AND a.ID='" + id + "' ");
		TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
		List<TtAsWrApplicationExtPO> ls = select(TtAsWrApplicationExtPO.class,
				sqlStr.toString(), null);
		if (ls != null) {
			if (ls.size() > 0) {
				tawep = ls.get(0);
			}
		}
		return tawep;
	}
	
	/**
	 * 
	* @Title: queryPrintById 
	* @Description: TODO(打印页面查询) 
	* @param @param id
	* @param @return    设定文件 
	* @return TtAsWrApplicationExtPO    返回类型 
	* @throws
	 */
	public TtAsWrApplicationExtPO queryPrintById(String id) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select a.*,aa.activity_name as campaign_name,v.model_id as model_id,v.PURCHASED_DATE as purchased_date,b.dealer_code as dealer_code, b.dealer_name as dealer_name, \n");
		sqlStr.append("o.start_time as start_time,o.end_time as end_time,o.out_person as out_person,\n");
		sqlStr.append("o.out_site as out_site,o.out_licenseno as out_licenseno,o.from_adress as from_adress,\n");
		sqlStr.append("o.end_adress as end_adress,o.out_mileage as out_mileages, \n");
		sqlStr.append("rr.in_mileage as in_mileage, (select count(*) from tt_as_repair_order where vin=rr.vin ) as repair_times, \n");
		sqlStr.append("/*tc.ctm_name*/rr.deliverer as customer_name,/*tc.main_phone*/rr.deliverer_phone||' '||rr.deliverer_mobile as customer_phone,tc.address as customer_address, \n");
		sqlStr.append("rr.free_times as free_times, ");
		/********Iverson add By 2010-11-22 添加接待员字段**************/
		sqlStr.append("RR.service_advisor as service_advisor, ");
		/*******Iverson add By 2010-11-22 添加接待员字段******/
		sqlStr.append("(select sum(amount) from tt_as_wr_netitem where id="+id+" and item_code='"+Constant.ROOM_CHARGE+"') as room_charge,\n"); //住宿费
		sqlStr.append("(select sum(amount) from tt_as_wr_netitem where id="+id+" and item_code='"+Constant.EATUP_FEE+"') as eatup_fee,\n"); //餐补费
		sqlStr.append("(select sum(amount) from tt_as_wr_netitem where id="+id+" and item_code='"+Constant.TRANSPORTATION+"') as transportation,\n"); //交通费
		sqlStr.append("(select sum(amount) from tt_as_wr_netitem where id="+id+" and item_code='"+Constant.FAX_FEE+"') as fax_fee,\n"); //电话传真费
		sqlStr.append("(select sum(amount) from tt_as_wr_netitem where id="+id+" and item_code='"+Constant.SUBSIDIES_FEE+"') as subsidies_fee,\n"); //工时补助费
		//sqlStr.append(" max(ap.id),ap.approval_person as auth_person,ap.remark as auth_content \n");
		sqlStr.append("(select name from (select * from tt_as_wr_appauthitem ap left outer join tc_user tu on ap.create_by=tu.user_id order by approval_date desc)  where id=a.id and approval_person like '%授权审核%' and rownum=1) as auth_person,\n" );
		sqlStr.append("(select remark from (select * from tt_as_wr_appauthitem order by approval_date desc) where id=a.id and approval_person like '%授权审核%' and rownum=1 ) as auth_content,");
		sqlStr.append(" vw.package_code as package_code,ts.name as fore_auth_person,fp.opinion as fore_auth_content ");
		sqlStr.append(" from TT_AS_WR_APPLICATION a  \n");
		sqlStr.append(" left outer join tt_as_wr_outrepair o on o.ro_no=a.ro_no \n");
		sqlStr.append(" left outer join TM_DEALER b on a.dealer_id=b.dealer_id \n");
		sqlStr.append(" left outer join TM_VEHICLE v on v.vin=a.vin \n");
		sqlStr.append(" left outer join vw_material_group_service vw on v.package_id=vw.package_id \n");
		sqlStr.append(" left outer join TT_AS_ACTIVITY AA ON AA.ACTIVITY_CODE=A.CAMPAIGN_CODE \n");
		sqlStr.append(" left outer join TT_AS_REPAIR_ORDER RR ON RR.RO_NO=A.RO_NO \n");
		sqlStr.append(" left outer join TT_AS_WR_FOREAPPROVAL FP ON RR.RO_NO=FP.RO_NO AND REPORT_STATUS="+Constant.RO_FORE_02+" \n");
		sqlStr.append(" left outer join TC_USER TS ON FP.AUDIT_PERSON=TS.USER_ID \n");
		sqlStr.append(" left outer join TT_DEALER_ACTUAL_SALES DA ON DA.VEHICLE_ID=V.VEHICLE_ID \n");
		sqlStr.append(" left outer join tt_customer tc on tc.ctm_id=da.ctm_id \n");
		//sqlStr.append(" left outer join tt_as_wr_appauthitem ap on a.id=ap.id and ap.approval_person like '%授权审核%'\n");
		sqlStr.append(" where 1=1 ");
		sqlStr.append(" AND a.ID='" + id + "' ");
		TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
		List<TtAsWrApplicationExtPO> ls = select(TtAsWrApplicationExtPO.class,sqlStr.toString(), null);
		if (ls != null) {
			if (ls.size() > 0) {
				tawep = ls.get(0);
			}
		}
		return tawep;
	}
	
	//zhumingwei 2012-07-06
	public TtAsWrApplicationExtPO queryPrintById1(String id) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select a.*,aa.activity_name as campaign_name,v.model_id as model_id,v.PURCHASED_DATE as purchased_date,b.dealer_code as dealer_code, b.dealer_name as dealer_name, \n");
		sqlStr.append("o.start_time as start_time,o.end_time as end_time,o.out_person as out_person,\n");
		sqlStr.append("o.out_site as out_site,o.out_licenseno as out_licenseno,o.from_adress as from_adress,\n");
		sqlStr.append("o.end_adress as end_adress,o.out_mileage as out_mileages, \n");
		sqlStr.append("rr.in_mileage as in_mileage, (select count(*) from tt_as_repair_order where vin=rr.vin ) as repair_times, \n");
		sqlStr.append("/*tc.ctm_name*/rr.deliverer as customer_name,/*tc.main_phone*/rr.deliverer_phone||' '||rr.deliverer_mobile as customer_phone,tc.address as customer_address, \n");
		sqlStr.append("rr.free_times as free_times, ");
		/********Iverson add By 2010-11-22 添加接待员字段**************/
		sqlStr.append("RR.service_advisor as service_advisor, ");
		/*******Iverson add By 2010-11-22 添加接待员字段******/
		//sqlStr.append(" max(ap.id),ap.approval_person as auth_person,ap.remark as auth_content \n");
		sqlStr.append("(select name from (select * from tt_as_wr_appauthitem ap left outer join tc_user tu on ap.create_by=tu.user_id order by approval_date desc)  where id=a.id and approval_person like '%授权审核%' and rownum=1) as auth_person,\n" );
		sqlStr.append("(select remark from (select * from tt_as_wr_appauthitem order by approval_date desc) where id=a.id and approval_person like '%授权审核%' and rownum=1 ) as auth_content,");
		sqlStr.append(" vw.package_code as package_code,ts.name as fore_auth_person,fp.opinion as fore_auth_content ");
		sqlStr.append(" from TT_AS_WR_APPLICATION a  \n");
		sqlStr.append(" left outer join tt_as_wr_outrepair o on o.ro_no=a.ro_no \n");
		sqlStr.append(" left outer join TM_DEALER b on a.dealer_id=b.dealer_id \n");
		sqlStr.append(" left outer join TM_VEHICLE v on v.vin=a.vin \n");
		sqlStr.append(" left outer join vw_material_group_service vw on v.package_id=vw.package_id \n");
		sqlStr.append(" left outer join TT_AS_ACTIVITY AA ON AA.ACTIVITY_CODE=A.CAMPAIGN_CODE \n");
		sqlStr.append(" left outer join TT_AS_REPAIR_ORDER RR ON RR.RO_NO=A.RO_NO \n");
		sqlStr.append(" left outer join TT_AS_WR_FOREAPPROVAL FP ON RR.RO_NO=FP.RO_NO AND REPORT_STATUS="+Constant.RO_FORE_02+" \n");
		sqlStr.append(" left outer join TC_USER TS ON FP.AUDIT_PERSON=TS.USER_ID \n");
		sqlStr.append(" left outer join TT_DEALER_ACTUAL_SALES DA ON DA.VEHICLE_ID=V.VEHICLE_ID \n");
		sqlStr.append(" left outer join tt_customer tc on tc.ctm_id=da.ctm_id \n");
		//sqlStr.append(" left outer join tt_as_wr_appauthitem ap on a.id=ap.id and ap.approval_person like '%授权审核%'\n");
		sqlStr.append(" where 1=1 ");
		sqlStr.append(" AND a.ID='" + id + "' ");
		TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
		List<TtAsWrApplicationExtPO> ls = select(TtAsWrApplicationExtPO.class,sqlStr.toString(), null);
		if (ls != null) {
			if (ls.size() > 0) {
				tawep = ls.get(0);
			}
		}
		return tawep;
	}
	
	/**
	 * 
	* @Title: queryPartLabour 
	* @Description: TODO(查询配件和工时在一个列表中) 
	* @param @param id
	* @param @return    设定文件 
	* @return List    返回类型 
	* @throws
	 */
	public List<TtAsWrPartsitemExtPO> queryPartLabour(String id) {
		StringBuffer sql= new StringBuffer();
		sql.append("select p.part_Id,\n");
		sql.append("       p.part_code as part_code,\n");  
		sql.append("       p.part_name as part_name,\n");  
		sql.append("       p.amount as amount,\n");  
		sql.append("       p.quantity as quantity,\n");  
		sql.append("       p.remark as remark,\n");  
		sql.append("       p.down_part_code as down_part_code,\n");  
		sql.append("       p.down_part_name as down_part_name,\n");  
		sql.append("       l.wr_labourcode as wr_labourcode,\n");  
		sql.append("       l.wr_labourname as wr_labourname,\n");  
		sql.append("       l.labour_hours as labour_hours,\n");  
		sql.append("       l.labour_amount as labour_amount,\n");  
		sql.append("       l.first_part as first_part,l.trouble_type\n");  
		sql.append("  from tt_as_wr_partsitem p\n");
		sql.append("left outer join tt_as_wr_labouritem l on p.wr_labourcode=l.wr_labourcode and p.id=l.id\n" );
		sql.append("where 1=1\n" );
		sql.append("and p.id="+id+"");
		List<TtAsWrPartsitemExtPO> ls = select(TtAsWrPartsitemExtPO.class,sql.toString(), null);
		return ls;
	}
	/**
	 * 
	 * @Title: queryApplicationDetailById
	 * @Description: TODO(明细查询)
	 * @param
	 * @param id
	 * @param
	 * @return 设定文件
	 * @return TtAsWrApplicationExtPO 返回类型
	 * @throws
	 */
	public TtAsWrApplicationExtPO queryApplicationDetailById(String id) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr
				.append(" select a.*,(select brand_name from VW_MATERIAL_GROUP_service vw where vw.package_id=v.package_id) as brand_name,g1.group_name as series_name,g2.group_name as model_name,b.dealer_code as dealer_code, b.dealer_name as dealer_name, \n");
		sqlStr
				.append(" v.ENGINE_NO as engine_no,v.GEARBOX_NO as gearbox_no,v.REARAXLE_NO as rearaxle_no,v.TRANSFER_NO as transfer_no,v.LICENSE_NO as license_no, \n");
		sqlStr
				.append(" b1.code_name as damage_degree_name,b2.code_name as damage_area_name,b3.code_name as damage_type_name,b4.code_name as trouble_name, \n");
		sqlStr.append(" c.code_desc as claim_name ");
		sqlStr.append(" from TT_AS_WR_APPLICATION a   \n");
		sqlStr
				.append(" left outer join TM_DEALER b on a.dealer_id=b.dealer_id \n");
		sqlStr.append(" left outer join TM_VEHICLE v on v.vin=a.vin \n");
		sqlStr
				.append(" left outer join TM_VHCL_MATERIAL_GROUP g1 on v.series_id=g1.group_id \n");
		sqlStr
				.append(" left outer join TM_VHCL_MATERIAL_GROUP g2 on v.model_id=g2.group_id \n");
		sqlStr
				.append(" left outer join tm_business_chng_code b1 on b1.type_code='"
						+ Constant.BUSINESS_CHNG_CODE_01
						+ "' and b1.code=a.damage_degree \n");
		sqlStr
				.append(" left outer join tm_business_chng_code b2 on b2.type_code='"
						+ Constant.BUSINESS_CHNG_CODE_02
						+ "' and b2.code=a.damage_area \n");
		sqlStr
				.append(" left outer join tm_business_chng_code b3 on b3.type_code='"
						+ Constant.BUSINESS_CHNG_CODE_03
						+ "' and b3.code=a.damage_type \n");
		sqlStr
				.append(" left outer join tm_business_chng_code b4 on b4.type_code='"
						+ Constant.BUSINESS_CHNG_CODE_04
						+ "' and b4.code=a.trouble_code \n");
		sqlStr
				.append(" left outer join tc_code c on c.code_id=a.claim_type \n");
		sqlStr.append(" where 1=1 ");
		sqlStr.append(" AND a.ID='" + id + "' ");
		TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
		List<TtAsWrApplicationExtPO> ls = select(TtAsWrApplicationExtPO.class,
				sqlStr.toString(), null);
		if (ls != null) {
			if (ls.size() > 0) {
				tawep = ls.get(0);
			}
		}
		return tawep;
	}

	/**
	 * 
	 * @Title: queryItemById
	 * @Description: TODO(查询工时通过索赔单ID)
	 * @param
	 * @param id
	 * @param
	 * @return 设定文件
	 * @return List 返回类型
	 * @throws
	 */
	public List<ClaimListBean> queryItemById(String id) {
	    StringBuffer sql = new StringBuffer();
	    List<ClaimListBean> allLs = new LinkedList<ClaimListBean>();
	    // Map map = new HashMap();
	    // 取主工时
	    sql.append("select l.labour_id,decode(g.id,null,1,0) UPDATE_BY, \n");
	    sql.append("       l.id,\n");  
	    sql.append("       l.wr_labourcode,\n");  
	    sql.append("       l.wr_labourname,\n");  
	    sql.append("       l.labour_code,\n");  
	    sql.append("       l.labour_name,\n");  
	    sql.append("       l.labour_hours,\n");  
	    sql.append("       l.labour_quantity,\n");  
	    sql.append("       l.labour_price,\n");  
	    sql.append("       l.labour_amount,\n");  
	    sql.append("       l.is_mainlabour,\n");  
	    sql.append("       l.auth_code,\n");  
	    sql.append("       l.create_date,\n");  
	    sql.append("       l.create_by,\n");  
	    sql.append("       l.debit_labour_id,\n");  
	    sql.append("       l.balance_amount,\n");  
	    sql.append("       l.deduct_amount,\n");  
	    sql.append("       l.b_ml_code,\n");  
	    sql.append("       l.trouble_code,\n");  
	    sql.append("       l.damage_type,\n");  
	    sql.append("       l.damage_area,\n");  
	    sql.append("       l.damage_degree,\n");  
	    sql.append("       l.labour_count,\n");  
	    sql.append("       l.is_agree,\n");  
	    sql.append("       l.trouble_code_name,\n");  
	    sql.append("       l.damage_type_name,\n");  
	    sql.append("       l.damage_area_name,\n");  
	    sql.append("       l.damage_degree_name,\n"); 
	    sql.append("       l.pay_type,\n");  
	    sql.append("       l.balance_quantity,\n");  
	    sql.append("       l.first_part,\n");  
	    sql.append("       l.auth_remark,\n");  
	    sql.append("       l.is_claim,\n");  
	    sql.append("       l.labour_quantity_hidden,\n");  
	    sql.append("       l.balance_price,\n");  
	    sql.append("       l.apply_quantity,\n");  
	    sql.append("       l.apply_price,\n");  
	    sql.append("       l.apply_amount,l.trouble_type,f.mal_code||'--'||f.mal_name mal_name,\n");  
	    sql.append("       p.down_part_code part_code,p.main_part_code,decode(p.main_part_code,'-1',p.down_part_code,'无',p.down_part_code,p.main_part_code,'次因件') as show_Main_Part\n");  
	    sql.append("  from TT_AS_WR_LABOURITEM l left join tt_as_wr_malfunction f on f.mal_id = l.trouble_type  left join  tt_as_wr_labouritem_counter g on g.id = l.id and  g.LABOUR_ID = l.LABOUR_ID \n");
	    sql.append(" LEFT JOIN Tt_As_Wr_Partsitem p ON p.ID = l.ID AND p.wr_labourcode = l.wr_labourcode\n"); 
		sql.append(" WHERE l.ID='" + id + "' \n");
	    
	    sql.append(" order by l.id,l.wr_labourcode,l.first_part");
	    List<TtAsWrLabouritemBean> ls = select(TtAsWrLabouritemBean.class, sql
	        .toString(), null);
	    if (ls == null) {

	    } else {
	      if (ls.size() == 0) {
	        // return null;
	      } else {
	        for (int i = 0; i < ls.size(); i++) {

	          ClaimListBean clb = new ClaimListBean();
	          TtAsWrLabouritemBean tawp = new TtAsWrLabouritemBean();
	          tawp = ls.get(i);
	          clb.setMain(tawp);
	          allLs.add(clb);
	        }
	        }
	    }
	    // 返回合并工时
	    return allLs;
  }

	/**
	 * 
	 * @Title: queryPartById
	 * @Description: TODO(查询配件通过索赔单ID)
	 * @param
	 * @param id
	 * @param
	 * @return 设定文件
	 * @return List 返回类型
	 * @throws
	 */
	public List<ClaimListBean> queryPartById(String id) {
		StringBuffer sql = new StringBuffer();
		List<TtAsWrApplicationPO> lsTawp = new ArrayList<TtAsWrApplicationPO>();
		List<TtAsWrPartsitemPO> ls = new ArrayList<TtAsWrPartsitemPO>();
		List<TtAsWrPartsitemPO> ls1 = new ArrayList<TtAsWrPartsitemPO>();
		List<TtAsWrPartsitemPO> ls2 = new ArrayList<TtAsWrPartsitemPO>();
		List<ClaimListBean> allLs = new LinkedList<ClaimListBean>();
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// Map map = new HashMap();
		 TtAsWrApplicationPO tawp = new TtAsWrApplicationPO(); 
		try {
			
			  tawp.setId(Utility.getLong(id)); 
			  lsTawp = select(tawp);
			  Date date = format.parse("2012-05-24 17:00:00");
				System.out.println(lsTawp.get(0).getCreateDate());
				String date2 = format.format(lsTawp.get(0).getCreateDate());
				Date date1 = format.parse(date2);
				sql = new StringBuffer();
				sql.append("select a.part_id,\n");
				sql.append("       a.id,\n");  
				sql.append("   d.DEDUCT_REMARK,a.main_part_code,cc.code_id part_use_type,cc.code_desc part_use_name,");
				sql.append("       a.part_code,\n");  
				sql.append("       a.part_name,\n");  
				sql.append("       a.down_part_code,\n");  
				sql.append("       a.down_part_name,\n");  
				sql.append("       a.quantity,\n");  
				sql.append("       a.price,\n");  
				sql.append("       a.amount,\n");  
				sql.append("       a.producer_code,\n");  
				sql.append("       a.producer_name,\n");  
				sql.append("       a.is_mainpart,\n");  
				sql.append("       a.remark,\n");  
				sql.append("       a.auth_code,\n");  
				sql.append("       a.create_date,\n");  
				sql.append("       a.create_by,\n");  
				sql.append("       a.debit_part_id,\n");  
				sql.append("       a.return_num,\n");  
				sql.append("       a.old_part_code,\n");  
				sql.append("       a.balance_amount,\n");  
				sql.append("       a.deduct_amount,\n");  
				sql.append("       a.b_mp_code,\n");  
				sql.append("       a.wr_labourcode,\n");  
				sql.append("       a.down_product_code,\n");  
				sql.append("       a.down_product_name,\n");  
				sql.append("       nvl(a.is_gua,0) as is_gua,\n");  
				sql.append("       a.is_agree,\n");  
				sql.append("       a.pay_type,\n");   
				sql.append("       a.balance_quantity,\n");  
				sql.append("       a.auth_remark,\n");  
				sql.append("       a.is_claim,\n");  
				sql.append("       a.balance_price,\n");  
				sql.append("       a.apply_quantity,\n");  
				sql.append("       a.apply_price,\n");  
				sql.append("       a.apply_amount,\n");  
				sql.append("       a.responsibility_type,a.real_part_id,  c.code_id, c.code_desc, l.wr_labourname,decode(a.ZF_RONO,'','无',a.ZF_RONO) ZF_RONO,\n");  
				sql.append("       (SELECT COUNT(1) FROM TM_PT_PART_TYPE T, TM_PT_PART_BASE B WHERE T.IS_MAX = 1 AND a.PART_ID = B.PART_ID AND B.PART_TYPE = T.ID) as is_fore\n");  
				sql.append(" from tt_as_wr_partsitem a left join tc_code c on c.code_id=a.responsibility_type \n");
				sql.append("LEFT JOIN tc_code cc ON cc.code_id = decode(a.part_use_type,1,"+Constant.PART_USE_TYPE_02+","+Constant.PART_USE_TYPE_01+")\n"); 
				sql.append("  left join TT_AS_WR_APPLICATION f on f.ID = a.ID  ");
				sql.append("  left join (  SELECT gg.PART_ID,gg.CLAIM_NO,max(gg.DEDUCT_REMARK)  DEDUCT_REMARK  from  tt_as_wr_old_returned_detail gg   group by gg.PART_ID,gg.CLAIM_NO )  d  on d.PART_ID = a.PART_ID and d.CLAIM_NO = f.CLAIM_NO ");
				sql.append(" ,tt_as_wr_labouritem l  ");
				sql.append(" WHERE 1=1 and l.wr_labourcode=a.wr_labourcode and l.id=a.id ");
				sql.append(" AND A.ID='" + id + "'");
				sql.append(" order by a.id	");
				if(date1.after(date)){
					sql.append(" , a.wr_labourcode,a.part_id  ");
				}else{
				sql.append(" , a.wr_labourcode,a.part_id desc ");
				}
				ls = select(TtAsWrPartsitemBean.class, sql.toString(), null);
				if (ls == null) {

				} else {
					
					List<Object> lsTm = new ArrayList<Object>();
					if (ls.size() > 0) {
						for (int i = 0; i < ls.size(); i++) {
							ClaimListBean clb = new ClaimListBean();
							if(ls2 != null && ls2.size() > 0)
							{
								for(int k = 0; k < ls2.size();k++)
								{
									if(ls2.get(k).getPartCode().equals(ls.get(i).getPartCode()) )
									{
										ls.get(i).setBalanceQuantity(ls2.get(k).getBalanceQuantity());
										ls.get(i).setBalanceAmount(ls2.get(k).getBalanceAmount());
										clb.setMain(ls.get(i));
									}
								}
							}else
							{
								clb.setMain(ls.get(i));
							}
							
							
							if(clb != null && clb.getMain() != null )
							{
								allLs.add(clb);
							}
							
						}
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return allLs;
	}
	
	//查询三包单据审核界面要显示零件的换件次数
	public int getCount(String id,String partName){
		String sql = null;
		//zhumingwei 2012-10-30  此方法用于区分轿车和微车
		TcCodePO codePo= new TcCodePO();
		codePo.setType(Constant.chana+"");
		TcCodePO poValue = (TcCodePO)dao.select(codePo).get(0);
		String codeId = poValue.getCodeId();
		//zhumingwei 2012-03-30
		if("80081001".equals(codeId)){
			sql = "select count(*) as count from Tt_As_Wr_Partsitem p where p.id in (select id from tt_as_wr_application where vin = (select vin from tt_as_wr_application where id = '"+id+"') and status not in ('"+Constant.CLAIM_APPLY_ORD_TYPE_05+"', '"+Constant.CLAIM_APPLY_ORD_TYPE_06+"')) and p.part_code='"+partName+"' and p.part_name not like '%无零件%'";
		}else{
			sql = "select count(*) as count from Tt_As_Wr_Partsitem p where p.id in (select id from tt_as_wr_application where vin = (select vin from tt_as_wr_application where id = '"+id+"') and status in ('"+Constant.CLAIM_APPLY_ORD_TYPE_07+"', '"+Constant.CLAIM_APPLY_ORD_TYPE_08+"')) and p.part_code='"+partName+"' and p.part_name not like '%无零件%'";
		}
		List<Map<String, Object>> list = this.pageQuery(sql, null, this.getFunName());
		return ((BigDecimal)list.get(0).get("COUNT")).intValue();
	}

	/**
	 * 
	 * @Title: queryOtherByid
	 * @Description: TODO(查询其他项目通过ID)
	 * @param
	 * @param id
	 * @param
	 * @return 设定文件
	 * @return List 返回类型
	 * @throws
	 */
	public List<TtAsWrNetitemExtPO> queryOtherByid(String id) {
		StringBuffer sql = new StringBuffer();
		List<TtAsWrNetitemExtPO> ls = new ArrayList<TtAsWrNetitemExtPO>();
		sql.append(" SELECT A.*,B.FEE_NAME as fee_name ");
		sql.append(" FROM TT_AS_WR_NETITEM A ");
		sql
				.append(" LEFT OUTER JOIN TT_AS_WR_OTHERFEE B ON B.FEE_CODE = A.ITEM_CODE ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND A.ID='" + id + "'");
		sql.append(" order by a.id	");
		ls = select(TtAsWrNetitemExtPO.class, sql.toString(), null);
		return ls;
	}

	/**
	 * 
	 * @Title: queryAttById
	 * @Description: TODO(通过ID查询附件)
	 * @param
	 * @param id
	 * @param
	 * @return 设定文件
	 * @return List<FsFileuploadPO> 返回类型
	 * @throws
	 */
	public List<FsFileuploadPO> queryAttById(String id) {
		StringBuffer sql = new StringBuffer();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		// sql.append(" LEFT OUTER JOIN FS_FILEUPLOAD B ON B.YWZJ = A.ID ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND A.YWZJ='" + id + "'");
		ls = select(FsFileuploadPO.class, sql.toString(), null);
		return ls;
	}

	/**
	 * 
	 * @Title: queryActivity
	 * @Description: TODO(查询服务活动的费用)
	 * @param
	 * @param id
	 * @param
	 * @return 设定文件
	 * @return List<TtAsActivityPO> 返回类型 
	 * @throws
	 */
	public List<TtAsActivityPO> queryActivity(Long id) {
		StringBuffer sql = new StringBuffer();
		List<TtAsActivityPO> ls = new ArrayList<TtAsActivityPO>();
		sql.append(" SELECT A.* FROM  TT_AS_WR_APPLICATION W ");
		sql
				.append(" LEFT OUTER JOIN TT_AS_ACTIVITY A ON W.CAMPAIGN_CODE = A.ACTIVITY_CODE ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND W.ID='" + id + "'");
		ls = select(TtAsActivityPO.class, sql.toString(), null);
		return ls;
	}

	/**
	 * 
	 * @Title: feeQuery
	 * @Description: TODO(查询免费保养费用)
	 * @param
	 * @param pageSize
	 * @param
	 * @param curPage
	 * @param
	 * @param whereSql
	 * @param
	 * @param params
	 * @param
	 * @param type
	 * @param
	 * @param flagStr
	 * @param
	 * @return
	 * @param
	 * @throws Exception
	 *             设定文件
	 * @return PageResult<Map<String,Object>> 返回类型
	 * @throws
	 */
	public PageResult<Map<String, Object>> feeQuery(int pageSize, int curPage,
			String whereSql, List<Object> params, String type, String flagStr)
			throws Exception {
		PageResult<Map<String, Object>> result = null;
		List list = dao.getFeeType(type);
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.group_id,t.group_code,t.group_name ");
		for (int i = 0; i < list.size(); i++) {
			HashMap tcpo = (HashMap) list.get(i);
			sb.append(" ,max(decode(t.fee_type," + tcpo.get("CODE_ID")
					+ ",t.fee)) as \"" + tcpo.get("CODE_ID") + "\" ");
		}
		sb.append(" from ");
		sb
				.append(" (select tvmg.group_id,tvmg.group_code,tvmg.group_name,tawmf.fee_id,tawmf.fee,tawmf.fee_type ");
		sb.append(" from TM_VHCL_MATERIAL_GROUP tvmg ");
		sb
				.append(" left outer join TT_AS_WR_MODEL_FEE tawmf on tvmg.group_id = tawmf.model_id ");
		sb.append(" where tvmg.group_level = 3 ");
		sb.append(" and tvmg.group_id ");
		sb.append(flagStr);
		sb
				.append(" ( select a.model_id from TT_AS_WR_MODEL_FEE a group by a.model_id ) ");
		if (whereSql != null && !"".equals(whereSql.trim())) {
			sb.append(whereSql);
		}
		sb.append(" ) t ");
		sb.append(" group by t.group_id,t.group_code,t.group_name ");
		sb.append(" order by t.group_id ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(),
				params, getFunName(), pageSize, curPage);
		return result;
	}

	/**
	 * 
	 * @Title: getFeeType
	 * @Description: TODO(获得保养费用的tc_code列表)
	 * @param
	 * @param type
	 * @param
	 * @return
	 * @return List
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List getFeeType(String type) {
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select tc.code_id,tc.code_desc,tc.type from tc_code tc ");
		sb.append(" where 1=1 ");
		if (Utility.testString(type)) {
			sb.append(" and tc.type = ? ");
			params.add(type);
		}
		sb.append(" and tc.status = 10011001");
		sb.append(" order by tc.num");
		List<Map<String, Object>> list = pageQuery(sb.toString(), params,
				getFunName());
		return list;
	}

	public void insertTtAsWrLabouritem(TtAsWrLabouritemPO t) {
		insert(t);
	}

	/**
	 * 查询对应索赔申请单授权信息
	 * 
	 * @param claimId
	 *            索赔申请单ID
	 * @return List<TtAsWrAppauthitemPO>
	 */
	public List<TtAsWrAppauthitemPO> queryAppAuthInfo(String claimId) {
		List<Object> condition = new ArrayList<Object>();
		condition.add(Long.parseLong(claimId));

		StringBuffer sql = new StringBuffer();
		sql
				.append("SELECT A.ID,A.APPROVAL_PERSON,A.APPROVAL_LEVEL_CODE,A.APPROVAL_DATE,\n");
		sql
				.append("    B.CODE_DESC APPROVAL_RESULT,A.AUTHORIZED_CODE,A.REMARK,A.CREATE_BY,\n");
		sql.append("    A.CREATE_DATE,A.UPDATE_BY,A.UPDATE_DATE\n");
		sql.append("    FROM TT_AS_WR_APPAUTHITEM A,TC_CODE B\n");
		sql.append("    WHERE 1=1\n");
		sql.append("    AND A.APPROVAL_RESULT = B.CODE_ID(+)\n");
		sql.append("    AND A.ID = ?\n");
		sql.append("    ORDER BY A.CREATE_DATE DESC");

		List<TtAsWrAppauthitemPO> resList = this.select(
				TtAsWrAppauthitemPO.class, sql.toString(), condition);

		return resList;
	}
	
	public List<Map<String, Object>> queryWinterDetail(String id) {
		
		StringBuffer sb= new StringBuffer();
		sb.append("select tt.id,\n" );
		sb.append("       tt.amount,\n" );
		sb.append("       tt.start_date,\n" );
		sb.append("       tt.end_date,\n" );
		sb.append("       tt.create_by,\n" );
		sb.append("       tt.create_date,\n" );
		sb.append("       tt.status,\n" );
		sb.append("       tm.dealer_code,\n" );
		sb.append("       vw.root_org_name,\n" );
		sb.append("       vw.org_name,\n" );
		sb.append("       tm.dealer_name\n" );
		sb.append("  from TT_AS_WR_WINTER_MAINTEN tt\n" );
		sb.append("  left join TT_AS_WR_WINTER_MAINTEN_DEALER ttd\n" );
		sb.append(" on tt.id = ttd.id \n");
		sb.append("  left join vw_org_dealer_service vw \n" );
		sb.append(" on ttd.dealer_id = vw.dealer_id \n");
		sb.append("  left join tm_dealer tm\n" );
		sb.append(" on ttd.dealer_id = tm.dealer_id where tt.id=" + id);
		
		List<Map<String, Object>> resList = pageQuery(sb.toString(), null,
				getFunName());
		
		return resList;
	}
	public List<Map<String, Object>> queryAppAuthDetail(String claimId) {
		List<Object> condition = new ArrayList<Object>();
		condition.add(Long.parseLong(claimId));

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.NAME,TO_CHAR(D.AUDIT_DATE,'YYYY-MM-DD hh24:mi') AUDIT_TIME,C.CODE_DESC,D.AUDIT_REMARK\n");
		sql.append("FROM TT_AS_WR_APP_AUDIT_DETAIL D,TC_USER U,TC_CODE C\n");
		sql.append("WHERE D.AUDIT_BY = U.USER_ID\n");
		sql.append("AND D.AUDIT_RESULT = C.CODE_ID\n");
		sql.append("AND D.CLAIM_ID="+claimId+"\n");
		sql.append("ORDER BY D.CREATE_DATE DESC"); 


		List<Map<String, Object>> resList = pageQuery(sql.toString(), null,
				getFunName());

		return resList;
	}
	/**
	 * 得到索赔单主因件集合
	 * @param claimId
	 * @return
	 */
	public List<Map<String, Object>> queryMainList(String claimId) {
		List<Object> condition = new ArrayList<Object>();
		condition.add(Long.parseLong(claimId));

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT P.Down_Part_Code PART_CODE,P.PART_ID,u.name,to_char(p.audit_date,'yyyy-mm-dd hh24:mm') audit_time,\n");
		sql.append("  P.Down_Part_Name PART_NAME,\n");
		sql.append("  P.WR_LABOURCODE,\n");
		sql.append("  P.DOWN_PRODUCT_CODE,\n");
		sql.append("  P.DOWN_PRODUCT_NAME,\n");
		sql.append("  P.AUDIT_STATUS,\n");
		sql.append("  P.REMARK,P.TROUBLE_REASON,P.DEAL_METHOD\n"); 
		sql.append(" FROM  TT_AS_WR_PARTSITEM P LEFT JOIN TC_USER U ON   P.AUDIT_BY = U.USER_ID\n");
		sql.append(" WHERE P.RESPONSIBILITY_TYPE = 94001001 \n");
		sql.append("  AND p.ID ="+claimId); 


		List<Map<String, Object>> resList = pageQuery(sql.toString(), null,
				getFunName());

		return resList;
	}
	//得到工单主因件集合
	public List<Map<String, Object>> queryMainList2(String roId) {

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  p.part_no PART_CODE,p.part_name,p.Id PART_ID,p.labour_code,\n");
		sql.append(" p.trouble_describe,p.trouble_reason,p.deal_method\n");
		sql.append(" FROM TT_AS_RO_REPAIR_PART P\n");
		sql.append(" WHERE P.RESPONS_NATURE = 94001001\n");
		sql.append(" AND P.RO_ID = "+roId+""); 

		List<Map<String, Object>> resList = pageQuery(sql.toString(), null,
				getFunName());

		return resList;
	}
	/**
	 * 查询对应索赔申请单需要审核的原因
	 * 
	 * @param claimId
	 *            索赔申请单ID
	 * @return TtAsWrWrauthorizationPO
	 */
	public TtAsWrWrauthorizationPO queryAuthReason(String claimId) {

		String sql = "SELECT * FROM TT_AS_WR_WRAUTHORIZATION "
				+ " WHERE ID = ? " + " AND ROWNUM < 2";

		List<Object> paraList = new ArrayList<Object>();
		paraList.add(claimId);

		TtAsWrWrauthorizationPO resultPO = null;

		List<TtAsWrWrauthorizationPO> resultList = this.select(
				TtAsWrWrauthorizationPO.class, sql, paraList);
		if (resultList != null && resultList.size() > 0) {
			resultPO = resultList.get(0);
		}
		return resultPO;
	}

	/**
	 * 
	 * @Title: queryBrand
	 * @Description: TODO(取品牌)
	 * @param
	 * @return 设定文件
	 * @return List<VwMaterialGroupPO> 返回类型
	 * @throws
	 */
	public List<VwMaterialGroupPO> queryBrand() {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct brand_id,brand_name,brand_code \n");
		sql.append("    from vw_material_group t\n");
		sql.append(" where 1=1 ");
		List<VwMaterialGroupPO> resList = this.select(VwMaterialGroupPO.class,
				sql.toString(), null);

		return resList;
	}

	/**
	 * 
	 * @Title: querySeries
	 * @Description: TODO(取车系)
	 * @param
	 * @return 设定文件
	 * @return List<VwMaterialGroupPO> 返回类型
	 * @throws
	 */
	public List<VwMaterialGroupPO> querySeries(String brand) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct series_id,series_name,series_code \n");
		sql.append("    from vw_material_group t\n");
		sql.append(" where 1=1 ");
		if (Utility.testString(brand)) {
			sql.append(" and brand_code='" + brand + "' ");
		}
		List<VwMaterialGroupPO> resList = this.select(VwMaterialGroupPO.class,
				sql.toString(), null);

		return resList;
	}

	/**
	 * 
	 * @Title: queryModel
	 * @Description: TODO(取车型)
	 * @param
	 * @return 设定文件
	 * @return List<VwMaterialGroupPO> 返回类型
	 * @throws
	 */
	public List<VwMaterialGroupPO> queryModel(String series) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct model_id,model_name,model_code \n");
		sql.append("    from vw_material_group t\n");
		sql.append(" where 1=1 ");
		if (Utility.testString(series)) {
			sql.append(" and series_code='" + series + "'");
		}
		List<VwMaterialGroupPO> resList = this.select(VwMaterialGroupPO.class,
				sql.toString(), null);

		return resList;
	}

	/**
	 * 
	 * @Title: queryGame
	 * @Description: TODO(查询免费保养次数下拉框)
	 * @param
	 * @param modelId
	 * @param
	 * @return 设定文件
	 * @return List<TtAsWrGamefeePO> 返回类型 
	 * @throws
	 */
	public List<TtAsWrGamefeePO> queryGame(Long modelId) {
		StringBuffer sql = new StringBuffer();
		sql
				.append("select distinct(MAINTAINFEE_ORDER),MANINTAIN_FEE from TT_AS_WR_GAMEFEE t left outer join  TT_AS_WR_GAMEMODEL m on t.game_id=m.game_id\n");
		sql.append("     where 1=1\n");
		if (modelId != null) {
			sql.append("  and m.model_id=" + modelId + "");
		}
		sql.append(" order by maintainfee_order asc ");
		List<TtAsWrGamefeePO> resList = this.select(TtAsWrGamefeePO.class, sql
				.toString(), null);
		return resList;

	}

	/**
	 * 
	 * @Title: queryActivityCombo
	 * @Description: TODO(服务活动下拉框)
	 * @param
	 * @param modelId
	 * @param
	 * @return 设定文件
	 * @return List<TtAsActivityPO> 返回类型 
	 * @throws
	 */
	public List<TtAsActivityPO> queryActivityCombo() {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT A.*  FROM TT_AS_ACTIVITY A  \n");
		sql.append("    WHERE A.STATUS in( "
				+ Constant.SERVICEACTIVITY_STATUS_02 + ") AND A.IS_DEL="
				+ Constant.IS_DEL_00 + " \n");// 服务活动管理--服务活动活动状态:[10681001:尚未发布;10681002:已经发布;10681003:重新发布]
		// sql.append(" AND A.ACTIVITY_CODE NOT IN (SELECT CAMPAIGN_CODE FROM
		// WHERE ) ");
		sql.append("    ORDER BY trim(A.ACTIVITY_CODE) desc  \n");

		List<TtAsActivityPO> resList = this.select(TtAsActivityPO.class, sql
				.toString(), null);
		return resList;
	}
	/**
	 * 
	* @Title: queryActivityCombo 
	* @Description: TODO(根据车型查询对应服务活动) 
	* @param @param modelId
	* @param @return    设定文件 
	* @return List<TtAsActivityPO>    返回类型 
	* @throws
	 */
	public List<TtAsActivityPO> queryActivityCombo(String modelId) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT A.*  FROM TT_AS_ACTIVITY A  \n");
		sql.append("LEFT OUTER JOIN TT_AS_ACTIVITY_MGROUP M ON A.activity_id=M.activity_id \n");
		sql.append("    WHERE A.STATUS in( "
				+ Constant.SERVICEACTIVITY_STATUS_02 + ") AND A.IS_DEL="
				+ Constant.IS_DEL_00 + " \n");// 服务活动管理--服务活动活动状态:[10681001:尚未发布;10681002:已经发布;10681003:重新发布]
		// sql.append(" AND A.ACTIVITY_CODE NOT IN (SELECT CAMPAIGN_CODE FROM
		// WHERE ) ");
		if (Utility.testString(modelId)) {
			sql.append(" and  MATERIAL_GROUP_ID="+modelId+"");
		}
		sql.append("    ORDER BY trim(A.ACTIVITY_CODE) desc  \n");

		List<TtAsActivityPO> resList = this.select(TtAsActivityPO.class, sql
				.toString(), null);
		return resList;
	}
	/**
	 * 查询厂家切换件的
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<TtAsActivityPO> queryActivityComboByVin(Map<String,String> map,int curPage,int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT B.ACTIVITY_NUM AS ACTIVITY_NUM,A.ACTIVITY_ID,A.ACTIVITY_FEE,A.IS_FIXFEE,A.IS_CLAIM,A.TROUBLE_DESC,A.TROUBLE_REASON,A.REPAIR_METHOD,A.APP_REMARK,A.ACTIVITY_CODE,A.ACTIVITY_NAME,A.ACTIVITY_TYPE  FROM TT_AS_ACTIVITY A  JOIN  TT_AS_ACTIVITY_SUBJECT B ON B.SUBJECT_ID=A.SUBJECT_ID WHERE 1=1\n");
		sql.append(" and a.is_del=0 and a.status="+Constant.SERVICEACTIVITY_STATUS_02+" \n");
		sql.append(" and (((select count(*) from tt_as_repair_ORDER R  where R.CAM_CODE=a.activity_code AND VIN='"+map.get("vin")+"')<A.SINGLE_CAR_NUM) OR A.SINGLE_CAR_NUM IS NULL)\n" );
		List params = new LinkedList();
		params.add(map.get("vin"));
		params.add(map.get("dealerCode"));
		params.add(map.get("inMileage"));
		
		String ss = callFunction("f_get_activity_id", java.sql.Types.VARCHAR,params )==null?"-1":callFunction("f_get_activity_id", java.sql.Types.VARCHAR,params ).toString();
		if (Utility.testString(map.get("vin"))) {
			sql.append(" and a.activity_id in ("+ss+")");
		}
		if (Utility.testString(map.get("activityCode"))) {
			sql.append(" and  A.ACTIVITY_CODE LIKE '%"+map.get("activityCode")+"%' \n");
		}
		if (Utility.testString(map.get("activityName"))) {
			sql.append(" and  A.ACTIVITY_NAME LIKE '%"+map.get("activityName")+"%' \n");
		}
		sql.append(" and trunc(sysdate) between a.startdate and a.enddate\n");
		sql.append("    ORDER BY trim(A.ACTIVITY_CODE) desc  \n");

		PageResult<TtAsActivityPO> resList = pageQuery(TtAsActivityPO.class, sql
				.toString(), null,pageSize,curPage);
		return resList;
	}
	
	/**
	 * 根据活动ID查询维修工时信息
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryLabouritemReplace(String id){
		String sql = "select * from tt_as_wr_labouritem_raplce where id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		return pageQuery(sql, params, this.getFunName());
	}
	
	/**
	 * 根据活动ID查询维修配件信息
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryPartsitemReplace(String id){
		String sql = "select * from tt_as_wr_partsitem_raplce left join Tt_As_Activity on activity_id=id  where id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		return pageQuery(sql, params, this.getFunName());
	}
	
	/**
	 * 
	 * @Title: queryActivityRepair
	 * @Description: TODO(查询服务活动维修项目)
	 * @param
	 * @return 设定文件
	 * @return List<TtAsActivityPO> 返回类型 
	 * @throws
	 */
	public List<TtAsActivityRepairitemExtPO> queryActivityRepair(String code,
			String dealerId,String modelId,String companyId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.activity_code,t.*,("+getLabourParameter(dealerId,modelId,companyId)+" ) AS parameter_value,\n");
		sql.append("  ("+getLabourParameter(dealerId,modelId,companyId)+" )*normal_labor as sum\n");
		sql.append("  FROM TT_AS_ACTIVITY_REPAIRITEM t left outer join tt_as_activity a on t.activity_id=a.activity_id\n");
		sql.append("  WHERE  a.ACTIVITY_CODE = '" + code + "'");

		List<TtAsActivityRepairitemExtPO> resList = this.select(TtAsActivityRepairitemExtPO.class, sql.toString(), null);
		return resList;
	}

	/**
	 * 
	 * @Title: queryActivityPart
	 * @Description: TODO(查询服务活动配件)
	 * @param
	 * @return 设定文件
	 * @return List<TtAsActivityPartsPO> 返回类型 
	 * @throws
	 */
	public List<TtAsActivityPartsPO> queryActivityPart(String code,String vin,Double mileage,
			String dealerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT part_no,part_name,part_quantity,part_price,supplier_code,supplier_name,t.part_price*t.part_quantity as part_amount \n");
		sql.append("FROM TT_AS_ACTIVITY_PARTS t left outer join tt_as_activity a on t.activity_id=a.activity_id \n");
		sql.append("WHERE  a.ACTIVITY_CODE = '" + code + "' \n");
		// sql.append("order by T.ACTIVITY_ID desc ;");
		TmVehiclePO tvp = new TmVehiclePO();
		int month = 999999;  //相隔月数
		Date now = new Date();
		
		/*****add by liuxh 20131108判断车架号不能为空*****/
		CommonUtils.jugeVinNull(vin);
		/*****add by liuxh 20131108判断车架号不能为空*****/
		
		if (Utility.testString(vin)) {
			tvp.setVin(vin);
			List<TmVehiclePO> lsv = select(tvp);
			if (lsv!=null) {
				if (lsv.size()>0) {
					tvp = lsv.get(0);
					Date purchasedDate = tvp.getPurchasedDate();
					if (purchasedDate!=null) {
						String formatStyle ="yyyy-MM-dd";  
						SimpleDateFormat df = new SimpleDateFormat(formatStyle);  
						String d1 = df.format(purchasedDate);
						String d2 = df.format(now);
						month  = Utility.compareDate(d1,d2,1); //取得今日和保养开始时间的插值
					}
				}else {
					
				}
			}else {
				
			}
		}else {
			
		}
		List<TtAsActivityPartsPO> resList = this.select(TtAsActivityPartsPO.class, sql.toString(), null);
		List<TtAsActivityPartsPO> myList = new ArrayList<TtAsActivityPartsPO>();
		List<TtAsWrRuleListPO> ls = getPartGuaListByVin(vin,code);
		for (TtAsActivityPartsPO po : resList) {
			for(TtAsWrRuleListPO por : ls) {
				if (po.getPartNo().trim().equals(por.getPartCode().trim()) ) {
					if (mileage<por.getClaimMelieage()&&month<por.getClaimMonth()) {
						po.setIsGua(1);
					}else {
						po.setIsGua(0);
					}
				}
			}
			po.setIsGua(0);
			myList.add(po);
		}
		return myList;
	}

	/**
	 * 
	 * @Title: queryActivityOther
	 * @Description: TODO(查询服务活动其他项目)
	 * @param
	 * @return 设定文件
	 * @return List<TtAsActivityNetitemPO> 返回类型 
	 * @throws
	 */
	public List<TtAsActivityNetitemPO> queryActivityOther(String code,
			String dealerId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*\n");
		sql
				.append("  FROM TT_AS_ACTIVITY_NETITEM t left outer join tt_as_activity a on t.activity_id=a.activity_id\n");
		sql.append("  WHERE  a.ACTIVITY_CODE = '" + code + "'\n");
		// sql.append(" ORDER BY T.ACTIVITY_ID desc");

		List<TtAsActivityNetitemPO> resList = this.select(
				TtAsActivityNetitemPO.class, sql.toString(), null);
		return resList;
	}
	
	public List queryActivityOtherNew(String code,
			String dealerId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.* ,c.code_desc pro_name, c2.code_desc paid_name,a.activity_code\n");
		sql.append("  FROM TT_AS_ACTIVITY_PROJECT t left outer join tt_as_activity a on t.activity_id=a.activity_id left join tc_code c2 on c2.code_id = t.paid ,tc_code c \n");
		sql.append(" WHERE 1=1   and c.code_id = t.pro_code\n");
		sql.append("  and  a.ACTIVITY_CODE = '" + code + "'\n");
		// sql.append(" ORDER BY T.ACTIVITY_ID desc");

		List  resList = this.select(
				TtAsActivityProjectBean.class, sql.toString(), null);
		return resList;
	}
	public List<TtAsActivityRelationPO> getReList(String code,String vin, Integer type,String yiedily) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT R.*, g.SERIES_ID,v.series_id\n");
		sql.append("  FROM TT_AS_ACTIVITY A, TT_AS_ACTIVITY_RELATION R\n");
		sql.append("  left join tt_part_define d on d.part_oldcode = r.project_code\n");
		sql.append(" , tt_as_relation_part_series p   ,  tm_vehicle v ,\n");
		sql.append("  vw_material_group  g\n");
		sql.append(" WHERE A.ACTIVITY_ID = R.ACTIVITY_ID\n");
		sql.append("       and g.PACKAGE_ID = v.package_id\n");
		sql.append("       and d.part_is_changhe = "+yiedily+"\n");
		sql.append("       and v.vin='"+vin+"'\n");
		sql.append("       AND A.ACTIVITY_CODE = '"+code+"'\n");
		sql.append("       AND R.LARGESS_TYPE = "+type+"\n");
		sql.append("       and p.part_id = d.part_id\n");
		sql.append("       and g.SERIES_ID = p.series_id"); 

		List<TtAsActivityRelationPO>  resList = this.select(
				TtAsActivityRelationPO.class, sql.toString(), null);
		return resList;
	}
	public String getSeries(String vin) {
		String sql = "select t.series_id id from vw_material_group_service t ,tm_vehicle v  where v.package_id = t.PACKAGE_ID and v.vin='"+vin+"'";
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return ps.get(0).get("ID").toString();
	}
	public Double getAmount(String code, String yiedily ,String dealerId) {
		StringBuffer sql = new StringBuffer();
		//sql.append("select p.sale_price1*(1+ nvl(dp.parameter_value,0)/100) num,\n");
		
		sql.append(" select CASE\n");
		sql.append("         WHEN (  trunc(sysdate) >= p.valid_date and  p.history_flag =1) or  p.history_flag =0 THEN\n");
		sql.append("         p.sale_price1*(1+ nvl(dp.parameter_value,0)/100)\n");
		sql.append("        ELSE\n");
		sql.append("       NVL(decode(p.history_price,null,p.sale_price1,p.history_price), 0)*(1+ nvl(dp.parameter_value,0)/100)   \n");
		sql.append("      END num\n"); 

		sql.append("	from tt_part_sales_price p\n");
		sql.append("left join  Tt_Part_Define d on d.part_id = p.part_id , tm_down_parameter dp\n");
		sql.append("where  d.part_oldcode ='"+code+"'\n");
		sql.append("and d.part_is_changhe="+yiedily+"\n");
		
		 if(Constant.PART_IS_CHANGHE_01.toString().equalsIgnoreCase(yiedily)){
			 sql.append(" and dp.parameter_code ="+Constant.CLAIM_BASIC_PARAMETER_09);
		    }else{
		    	sql.append(" and dp.parameter_code ="+Constant.CLAIM_BASIC_PARAMETER_08);
		    }
		
		sql.append("and dp.dealer_id = "+dealerId+"\n"); 
		List<Map<String,Object>> ps=this.pageQuery(sql.toString(), null, this.getFunName());
		return Double.valueOf(ps.get(0).get("NUM").toString());
	}
	/**
	 * 
	 * @Title: getRoItemWorkHoursQuery
	 * @Description: TODO(取得工单带有的工时)
	 * @param
	 * @param MantainBean
	 * @param
	 * @param curPage
	 * @param
	 * @param pageSize
	 * @param
	 * @return
	 * @param
	 * @throws Exception
	 *             设定文件
	 * @return PageResult<Map<String,Object>> 返回类型
	 * @throws
	 */
	public PageResult<Map<String, Object>> getRoItemWorkHoursQuery(
			TtAsActivityBean MantainBean,Map<String,String> map,AclUserBean user, int curPage, int pageSize)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql
				.append("	select ww.ID as labourId,  ww.WR_LABOURCODE AS LABOUR_CODE, ww.WR_LABOURNAME AS CN_DES, ww.STD_LABOUR_HOUR AS LABOUR_HOUR,  \n");
		sql.append("( select count(*)\n" );
		sql.append("from TT_AS_WR_FOREAPPROVAL t1,tm_dealer td,TT_AS_WR_FOREAPPROVALITEM t2\n" );
		sql.append("where t1.dealer_id = td.dealer_id\n" );
		sql.append("and t2.status is not null\n" );
		sql.append("and t1.id = t2.fid\n" );
		sql.append("and t1.ro_no='"+map.get("roNo")+"'\n" );
		sql.append("and t1.vin='"+map.get("vin")+"'\n" );
		sql.append("and t2.status="+Constant.PRECLAIM_AUDIT_01+"\n" ); //已同意
		sql.append("and t2.item_type="+Constant.PRE_AUTH_ITEM_01+"\n" ); //工时
		sql.append("and t2.item_code=ww.WR_LABOURCODE\n" );
		sql.append(") as fore, \n");
		sql.append("(case when instr(ww.WR_LABOURCODE,'"+Constant.SPEC_LABOUR_CODE+"')=1 then 1 else 0 end ) as is_spec \n");
		sql.append("    from TT_AS_RO_LABOUR ww \n");
		sql.append(" where 1=1 ");
		sql.append(" AND (IS_SEL='0' OR IS_SEL IS NULL) ");

		if (Utility.testString(MantainBean.getActivityId())) {// 活动ID
			sql.append("    AND  RO_ID=" + MantainBean.getActivityId() + " \n");
		}
		if (Utility.testString(MantainBean.getLabourCode())) {// 工时代码
			sql.append("    and  UPPER(ww.WR_LABOURCODE) like  UPPER('%"
					+ MantainBean.getLabourCode() + "%') \n");
		}
		if (Utility.testString(MantainBean.getCnDes())) {// 工时名称
			sql.append("    and  ww.WR_LABOURNAME like '%"
					+ MantainBean.getCnDes() + "%' \n");
		}
		sql.append("order by ww.WR_LABOURCODE desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(
				sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 
	 * @Title: getRoPartsQuery
	 * @Description: TODO(取得工单带有的配件)
	 * @param
	 * @param MantainBean
	 * @param
	 * @param curPage
	 * @param
	 * @param pageSize
	 * @param
	 * @return
	 * @param
	 * @throws Exception
	 *             设定文件
	 * @return PageResult<Map<String,Object>> 返回类型
	 * @throws
	 */
	public PageResult<Map<String, Object>> getRoPartsQuery(
			TtAsActivityBean MantainBean, Map<String,String> map,AclUserBean user,int curPage, int pageSize)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select A.ID AS PART_ID, A.PART_NO AS PART_CODE, PART_NAME ,a.PART_COST_PRICE AS CLAIM_PRICE,PART_QUANTITY  \n");
//		sql.append("( select count(*)\n" );
//		sql.append("from TT_AS_WR_FOREAPPROVAL t1,tm_dealer td,TT_AS_WR_FOREAPPROVALITEM t2\n" );
//		sql.append("where t1.dealer_id = td.dealer_id\n" );
//		sql.append("and t2.status is not null\n" );
//		sql.append("and t1.id = t2.fid\n" );
//		sql.append("and t1.ro_no='"+map.get("roNo")+"'\n" );
//		sql.append("and t1.vin='"+map.get("vin")+"'\n" );
//		sql.append("and t2.status="+Constant.PRECLAIM_AUDIT_01+"\n" ); //已同意
//		sql.append("and t2.item_type="+Constant.PRE_AUTH_ITEM_02+"\n" ); //配件
//		sql.append("and t2.item_code=a.PART_NO\n" );
//		sql.append(") as fore \n");
		sql.append("    from TT_AS_RO_REPAIR_PART a \n");
		sql.append(" where 1=1 ");
		sql.append(" AND (IS_SEL='0' OR IS_SEL IS NULL) ");
		if (Utility.testString(MantainBean.getActivityId())) {// 活动ID
			sql.append("    and  RO_ID=" + MantainBean.getActivityId() + " \n");
		}
		if (Utility.testString(MantainBean.getPartNo())) {// 配件代码
			sql.append("    and  UPPER(a.PART_NO) like  UPPER('%"
					+ MantainBean.getPartNo() + "%') \n");
		}
		if (Utility.testString(MantainBean.getPartName())) {// 配件名称
			sql.append("    and  a.PART_NAME like '%"
					+ MantainBean.getPartName() + "%' \n");
		}
		sql.append(" order by a.PART_NO desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(
				sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 
	 * @Title: getRoOthersQuery
	 * @Description: TODO(取得工单带有的其他项目)
	 * @param
	 * @param MantainBean
	 * @param
	 * @param curPage
	 * @param
	 * @param pageSize
	 * @param
	 * @return
	 * @param
	 * @throws Exception
	 *             设定文件
	 * @return PageResult<Map<String,Object>> 返回类型
	 * @throws
	 */
	public PageResult<Map<String, Object>> getRoOthersQuery(
			TtAsActivityBean MantainBean, int curPage, int pageSize)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql
				.append("	select ID AS FEE_ID, ADD_ITEM_CODE AS FEE_CODE, ADD_ITEM_NAME AS FEE_NAME,ADD_ITEM_AMOUNT AS FEE_AMOUNT,REMARK AS FEE_REMARK  \n");
		sql.append("    from TT_AS_RO_ADD_ITEM  \n");
		sql.append(" where 1=1 ");
		sql.append(" AND IS_SEL<>1 ");
		if (Utility.testString(MantainBean.getActivityId())) {// 活动ID
			sql.append("   AND RO_ID=" + MantainBean.getActivityId() + " \n");
		}
		sql.append("order by ADD_ITEM_CODE desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(
				sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 
	 * @Title: getWorkingHoursInfoList
	 * @Description: TODO(工单带有的工时查询)
	 * @param
	 * @param roId
	 * @param
	 * @return 设定文件
	 * @return List<TtAsActivityBean> 返回类型 
	 * @throws
	 */
	public List<TtAsActivityBean> getWorkingHoursInfoList(String roId,Map<String,String> map,AclUserBean user) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
			
		sql.append("  SELECT (CASE WHEN  L.CHARGE_PARTITION_CODE='S' THEN "+Constant.PAY_TYPE_02+" ELSE "+Constant.PAY_TYPE_01+" END) AS PAY_TYPE,\n");
		sql.append("  L.UPDATE_DATE,   L.CREATE_DATE,  L.CREATE_BY,  L.UPDATE_BY, L.WR_LABOURCODE AS ITEM_CODE,  L.ID AS ITEM_ID, L.RO_ID AS  ACTIVITY_ID,\n");
		sql.append("  L.WR_LABOURNAME AS ITEM_NAME,  L.LABOUR_AMOUNT AS LABOR_FEE,nvl(to_char( L.STD_LABOUR_HOUR,'FM9999990.0099'),'0.00') AS NORMAL_LABOR,");
		sql.append("  L.LABOUR_PRICE AS PARAMETER_VALUE,  \n");
		sql.append("(case when instr( L.WR_LABOURCODE,'"+Constant.SPEC_LABOUR_CODE+"')=1 then 1 else 0 end ) as is_spec ,M.MAL_ID,m.mal_code||'--'||M.MAL_NAME MAL_FUNCTION,\n");
		sql.append(" case when L.Wr_Labourcode in(select p.labour from tt_as_ro_repair_part p where p.ro_id="+roId+"  AND p.part_no NOT IN ('00-000','00-0000')  and p.charge_partition_code='S') then 'Y' else 'N' end has_part\n");
		sql.append("  FROM TT_AS_RO_LABOUR L LEFT JOIN TT_AS_WR_MALFUNCTION M ON M.MAL_ID=MAL_FUNCTION  \n");
		sql.append(" where 1=1");
		sql.append(" AND PAY_TYPE="+Constant.PAY_TYPE_02+" \n"); //OEM
		//sql.append(" AND (IS_CLAIM=0 OR IS_CLAIM IS NULL) \n");
		
		if (Utility.testString(roId)) {
			sql.append("  AND  RO_ID = ? \n");
			params.add(roId);
		}
		if (Utility.testString(map.get("itemIds"))) {
			sql.append(" AND ID IN ("+map.get("itemIds")+") \n");
		}
		sql.append(" order by ID  ");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}

	/**
	 * 
	 * @Title: getPartsList
	 * @Description: TODO(工单带有的配件查询)
	 * @param
	 * @param activityId
	 * @param
	 * @return 设定文件
	 * @return List<TtAsActivityBean> 返回类型 
	 * @throws
	 */
	public List<TtAsActivityBean> getPartsList(String roId,Map<String,String> map,AclUserBean user) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT (CASE WHEN a.CHARGE_PARTITION_CODE='S' THEN "+Constant.PAY_TYPE_02+" ELSE "+Constant.PAY_TYPE_01+" END) AS PAY_TYPE,a.IS_GUA,a.UPDATE_DATE,  a.CREATE_DATE, a.CREATE_BY, a.UPDATE_BY, a.PART_NO, a.PART_NAME, a.RO_ID AS ACTIVITY_ID,  a.PART_QUANTITY, a.ID AS PARTS_ID ,  \n");
		sql.append("  a.part_cost_price AS part_price ,a.main_part_code,a.real_part_id,decode(a.main_part_code,'-1','无',a.main_part_code)main_part_name, \n");
		sql.append(" PART_COST_AMOUNT as PART_AMOUNT,a.part_use_type,cc.code_desc part_use_name, a.Labour,c.code_id prat_Respons_Id, c.code_desc prat_Respons_Desc\n");
		sql.append("  FROM TT_AS_RO_REPAIR_PART a LEFT JOIN tc_code cc ON cc.code_id = a.part_use_type left join tc_code c on c.code_id=a.respons_nature,tm_down_parameter m \n");
			sql.append("  WHERE  1=1  \n");
			sql.append("and a.PART_NO NOT IN ('00-000','00-0000')"); 
			//sql.append("and a.part_use_type="+Constant.PART_USE_TYPE_02+"\n"); 
			sql.append(" AND PAY_TYPE="+Constant.PAY_TYPE_02+" \n"); //OEM
			if (Utility.testString(roId)) {
				sql.append("  AND  RO_ID = ? \n");
				params.add(roId);
			}
			if (Utility.testString(map.get("partIds"))) {
				sql.append(" AND ID IN ("+map.get("partIds")+") \n");
			}
			sql.append(" and m.dealer_id = "+user.getDealerId()+" ");
			if(map.get("yieldly").equalsIgnoreCase(Constant.PART_IS_CHANGHE_01.toString())){
				sql.append(" and m.parameter_code="+Constant.CLAIM_BASIC_PARAMETER_09);
			}else if(map.get("yieldly").equalsIgnoreCase(Constant.PART_IS_CHANGHE_02.toString())){
				sql.append(" and m.parameter_code="+Constant.CLAIM_BASIC_PARAMETER_08);
			}
			sql.append(" order by ID  ");
		
		List list2 = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list2;
	}

	/**
	 * 
	 * @Title: getNetItemList
	 * @Description: TODO(工单带有的其他项目查询)
	 * @param
	 * @param activityId
	 * @param
	 * @return 设定文件
	 * @return List<TtAsActivityBean> 返回类型 
	 * @throws
	 */
	public List<TtAsActivityBean> getNetItemList(String roId,Map<String,String> map,AclUserBean user) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select (CASE WHEN CHARGE_PARTITION_CODE='S' THEN "+Constant.PAY_TYPE_02+" ELSE "+Constant.PAY_TYPE_01+" END) AS PAY_TYPE,ID,  UPDATE_DATE, REMARK,  CREATE_DATE, CREATE_BY, UPDATE_BY, ADD_ITEM_CODE AS ITEM_CODES, ADD_ITEM_NAME AS ITEM_DESC, ADD_ITEM_AMOUNT AS AMOUNT,REMARK, RO_ID AS ACTIVITY_ID ,DISCOUNT,main_part_code \n");
//		sql.append("( select count(*)\n" );
//		sql.append("from TT_AS_WR_FOREAPPROVAL t1,tm_dealer td,TT_AS_WR_FOREAPPROVALITEM t2\n" );
//		sql.append("where t1.dealer_id = td.dealer_id\n" );
//		sql.append("and t2.status is not null\n" );
//		sql.append("and t1.id = t2.fid\n" );
//		sql.append("and t1.ro_no='"+map.get("roNo")+"'\n" );
//		sql.append("and t1.vin='"+map.get("vin")+"'\n" );
//		sql.append("and t2.status="+Constant.PRECLAIM_AUDIT_01+"\n" ); //已同意
//		sql.append("and t2.item_type="+Constant.PRE_AUTH_ITEM_03+"\n" ); //其他项目
//		sql.append("and t2.item_code=TT_AS_RO_ADD_ITEM.ADD_ITEM_CODE \n" );
//		sql.append(") as fore \n");
		sql.append("  FROM TT_AS_RO_ADD_ITEM  \n");
		sql.append(" WHERE 1=1 \n");
		sql.append(" AND PAY_TYPE="+Constant.PAY_TYPE_02+" \n"); //OEM
		if (Utility.testString(map.get("otherIds"))) {
			sql.append(" AND ID IN ("+map.get("otherIds")+") \n");
		}
		if (Utility.testString(roId)) {
			sql.append("  AND  RO_ID = ? \n");
			params.add(roId);
			//sql.append(" and add_item_code not in('3537006','3537007')\n");
			sql.append("  ORDER BY item_codes desc \n");
		}
		
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}

	/**
	 * 
	 * @Title: roItemWorkHoursOption
	 * @Description: TODO(工单新增工时)
	 * @param
	 * @param groupIdsArray
	 * @param
	 * @param labourCodeArray
	 * @param
	 * @param cnDesArray
	 * @param
	 * @param labourHourArray
	 * @param
	 * @param RepairitemPO
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roItemWorkHoursOption(String roId,String[] groupIdsArray,AclUserBean logonUser) {
		Date date = new Date();
		try{
		for (int i = 0; i < groupIdsArray.length; i++) {
			TtAsRoLabourPO tarp = new TtAsRoLabourPO();
			tarp.setId(Utility.getLong(groupIdsArray[i]));
			TtAsRoLabourPO tarpUp = new TtAsRoLabourPO();
			tarpUp.setId(Utility.getLong(groupIdsArray[i]));
			//tarpUp.setRoId(Utility.getLong(roId));
			tarpUp.setUpdateBy(logonUser.getUserId());
			tarpUp.setUpdateDate(date);
			tarpUp.setIsSel("1"); //1为被选择
			//RepairitemPO.setLabourId(Long.parseLong(SequenceManager
					//.getSequence("")));
			//RepairitemPO.setWrLabourcode((labourCodeArray[i]));
			//RepairitemPO.setWrLabourname((cnDesArray[i]));
			//RepairitemPO.setNormalLabor(Float.parseFloat(labourHourArray[i]));
			//insert(RepairitemPO);
			update(tarp,tarpUp);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @Title: deleteItemWorkHoursOption
	 * @Description: TODO(工单删除工时)
	 * @param
	 * @param RepairitemPO
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void deleteItemWorkHoursOption(TtAsRoLabourPO RepairitemPO) {
		TtAsRoLabourPO tup = new TtAsRoLabourPO();
		tup.setId(RepairitemPO.getId());
		tup.setIsSel("");
		update(RepairitemPO,tup);
	}

	/**
	 * 
	 * @Title: roItemPartsOption
	 * @Description: TODO(工单增加配件)
	 * @param
	 * @param partsIdArray
	 * @param
	 * @param partNoArray
	 * @param
	 * @param partNameArray
	 * @param
	 * @param partsQuantityArray
	 * @param
	 * @param claimPriceArray
	 * @param
	 * @param supplierCodeArray
	 * @param
	 * @param supplierNameArray
	 * @param
	 * @param PartsPO
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roItemPartsOption(String roId,String[] partsIdArray,AclUserBean logonUser) {
		Date date = new Date();
		try {
		for (int i = 0; i < partsIdArray.length; i++) {
			TtAsRoRepairPartPO tarrpp = new TtAsRoRepairPartPO();
			tarrpp.setId(Utility.getLong(partsIdArray[i]));
			TtAsRoRepairPartPO tarrppUp = new TtAsRoRepairPartPO();
			//tarrppUp.setId(Utility.getLong(partsIdArray[i]));
			tarrppUp.setUpdateBy(logonUser.getUserId());
			tarrppUp.setUpdateDate(date);
			tarrppUp.setIsSel("1");
			
			update(tarrpp,tarrppUp);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: deleteItemPartsOption
	 * @Description: TODO(工单删除配件)
	 * @param
	 * @param PartsPO
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void deleteItemPartsOption(TtAsRoRepairPartPO PartsPO) {
		TtAsRoRepairPartPO tup = new TtAsRoRepairPartPO();
		tup.setId(PartsPO.getId());
		tup.setIsSel("");
		update(PartsPO,tup);
	}

	/**
	 * 
	 * @Title: roItemOthersOption
	 * @Description: TODO(工单增加其他项目)
	 * @param
	 * @param idArray
	 * @param
	 * @param itemCodeArray
	 * @param
	 * @param itemDescArray
	 * @param
	 * @param NetitemPO
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roItemOthersOption(String roId,String[] idArray,AclUserBean logonUser) {
		Date date = new Date();
		try {
		for (int i = 0; i < idArray.length; i++) {
			TtAsRoAddItemPO taraip = new TtAsRoAddItemPO();
			taraip.setId(Utility.getLong(idArray[i]));
			TtAsRoAddItemPO taraipUp = new TtAsRoAddItemPO();
			taraipUp.setId(Utility.getLong(idArray[i]));
			taraipUp.setRoId(Utility.getLong(roId));
			taraipUp.setUpdateBy(logonUser.getUserId());
			taraipUp.setUpdateDate(date);
			taraipUp.setIsSel(1);
			
			update(taraip,taraipUp);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: deleteItemOthersOption
	 * @Description: TODO(工单删除其他项目)
	 * @param
	 * @param NetitemPO
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void deleteItemOthersOption(TtAsRoAddItemPO NetitemPO) {
		TtAsRoAddItemPO tup = new TtAsRoAddItemPO();
		tup.setId(NetitemPO.getId());
		tup.setIsSel(0);
		update(NetitemPO,tup);
	}

	/**
	 * @throws Exception 
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(查询工单)
	 * @param
	 * @param map
	 * @param
	 * @return 设定文件
	 * @return List<TtAsRepairOrderExtPO> 返回类型 
	 * @throws
	 */
	public PageResult<TtAsRepairOrderExtPO> queryRepairOrder(
			Map<String, String> map, int pageSize, int curPage) throws Exception {
		String curCompanyCode=CommonUtils.getCurCompanyCode();
		//如果是服务活动的话，刚执行费用查询区分判断
		String con = "select * from tt_as_activity a where a.activity_code = (select o.cam_code from tt_as_repair_order o where o.id ="+map.get("roId")+")\n";
		List<TtAsActivityPO> list = this.select(TtAsActivityPO.class, con, null);
		boolean b = true ;
		//得到时间限制天数
		TcCodePO c = new TcCodePO();
		c.setCodeId("94051003");
		c = (TcCodePO) this.select(c).get(0);
		int day = Integer.parseInt(c.getCodeDesc());
		if(list.size()>0){
			//如果是服务活动，并且是免费保养，则费用从这里带出。
			if((((TtAsActivityPO)list.get(0)).getActivityKind()).toString().equals(Constant.SERVICEACTIVITY_KIND_02.toString())){
				b = false ;
			}
		}
//		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select 0 as in_claim,v.claim_tactics_id as claim_tactics_id,v1.model_id as model_id,r.ro_no,r.id,r.vin,r.owner_name,r.repair_type_code,r.license,r.in_mileage,r.for_balance_time,ac.activity_name as campaign_name,nvl(ac.is_fixfee,0) as cam_fix,nvl(v.license_no,'') as license_no,v1.BRAND_CODE as brand_code,v1.SERIES_CODE as series_code,v1.MODEL_CODE as model_code,\n" );
		//sqlStr.append("select 0 as in_claim,v.claim_tactics_id as claim_tactics_id,v1.model_id as model_id,r.*,ac.activity_name as campaign_name,nvl(ac.is_fixfee,0) as cam_fix,nvl(v.license_no,'') as license_no,v1.BRAND_CODE as brand_code,v1.SERIES_CODE as series_code,v1.MODEL_CODE as model_code,\n" );
		sqlStr.append("v1.BRAND_NAME as brand_name,v1.SERIES_NAME as series_name,nvl(v1.PACKAGE_Name,'') as model_name,nvl(v1.MODEL_NAME,'') as model_names,v.engine_no as engine_no_v,\n" );
		sqlStr.append("nvl(v.rearaxle_no,'') as rearaxle_no,v.yieldly as yieldly,nvl(v.gearbox_no,'') as gearbox_no,nvl(v.transfer_no,'') as transfer_no,v.purchased_date as purchased_date,v.product_date, \n" );
		sqlStr.append("o.start_time as start_time,o.end_time as end_time,o.out_person as out_person,\n");
		sqlStr.append("o.out_site as out_site,o.out_licenseno as out_licenseno,o.from_adress as from_adress,\n");
		sqlStr.append("o.end_adress as end_adress,o.out_mileage as out_mileages,to_char(r.for_balance_time,'yyyy-mm-dd HH24:mi') end_date,to_char(r.ro_create_date, 'yyyy-mm-dd HH24:mi') start_date,\n");
		//if(b){
			sqlStr.append("ac.activity_fee as campaign_fee, BA.AREA_NAME yieldly_name\n");
//		}else{
//			sqlStr.append("(SELECT wg.FREE\n");
//			sqlStr.append("          FROM TM_VHCL_MATERIAL_GROUP mg,\n");  
//			sqlStr.append("               TT_AS_WR_MODEL_ITEM    wm,\n");  
//			sqlStr.append("               TT_AS_WR_MODEL_GROUP   wg,\n");  
//			sqlStr.append("               TM_VHCL_MATERIAL_GROUP mg2\n");  
//			sqlStr.append("         WHERE mg.group_id = wm.model_id\n");  
//			sqlStr.append("           and mg.group_id = v.model_id\n");  
//			sqlStr.append("           AND wm.wrgroup_id = wg.wrgroup_id\n");  
//			sqlStr.append("           and mg.parent_group_id = mg2.group_id\n");  
//			sqlStr.append("           and wg.wrgroup_type = '10451001') campaign_fee\n");
//		}
		sqlStr.append(" from tt_as_repair_order r left outer join tm_vehicle v on r.vin=v.vin\n" );
		sqlStr.append("left outer join vw_material_group_SERVICE v1 on v.package_id=v1.package_ID \n");
		sqlStr.append("left outer join tt_as_wr_outrepair o on r.ro_no=o.ro_no \n");
		sqlStr.append("left outer join tm_dealer d on r.dealer_id=d.dealer_id \n"); //YH 2010.11.30
		//sqlStr.append("left outer join tt_dealer_actual_sales da on da.vehicle_id");
		sqlStr.append("LEFT OUTER JOIN TT_AS_ACTIVITY AC ON AC.ACTIVITY_CODE=R.CAM_CODE AND AC.IS_DEL=0 \n");
		sqlStr.append("   LEFT JOIN TM_BUSINESS_AREA BA ON BA.AREA_ID = V.YIELDLY\n");
		sqlStr.append("LEFT JOIN TT_AS_WR_APPLICATION A ON A.RO_NO = R.RO_NO\n"); 
		sqlStr.append("left join (\n");
		sqlStr.append("     select  sum(decode(ra.add_item_code,'"+Constant.SERVICEACTIVITY_CAR_cms_02+"',ra.discount-nvl(ra.add_item_amount,0),nvl(ra.add_item_amount,0)))+\n");
		sqlStr.append("     sum(decode(rp.pay_type,"+Constant.PAY_TYPE_01+",nvl(rp.part_quantity,0)*nvl(rp.part_cost_price,0)-nvl(rp.part_cost_amount,0),nvl(rp.part_quantity,0)*nvl(rp.part_cost_price,0))) +\n");
		sqlStr.append("     sum(decode(lp.pay_type,"+Constant.PAY_TYPE_01+",nvl(lp.labour_price,0)*nvl(lp.std_labour_hour,0)-nvl(lp.labour_amount,0),nvl(lp.labour_price,0)*nvl(lp.std_labour_hour,0)) )labour_price , ra.ro_id\n");
		sqlStr.append("from Tt_As_Ro_Add_Item ra\n");
		sqlStr.append("left  join Tt_As_Ro_Repair_Part  rp on ra.ro_id = rp.ro_id\n");
		sqlStr.append("left   join Tt_As_Ro_Labour lp  on lp.ro_id = ra.ro_id\n");
		sqlStr.append("where  ra.pay_type="+Constant.PAY_TYPE_02+" group by ra.ro_id\n");
		sqlStr.append(") ra  on ra.ro_id = r.id\n"); 

		sqlStr.append(" where 1=1   AND A.RO_NO IS NULL\n");
		sqlStr.append("and r.is_customer_in_asc=1\n"); 
		//过滤掉没有小项的活动或者费用为0的工单
		//sqlStr.append("and ((r.repair_type_code="+Constant.REPAIR_TYPE_05+" and  ra.labour_price>0) or r.repair_type_code<>"+Constant.REPAIR_TYPE_05+" )\n");

		sqlStr.append(" AND (((R.APPROVAL_YN = 1 OR R.IS_CLAIM_FORE = 1) AND\n");
		sqlStr.append("       R.REPAIR_TYPE_CODE <> "+Constant.REPAIR_TYPE_04+") OR\n");
		sqlStr.append("       (R.REPAIR_TYPE_CODE = "+Constant.REPAIR_TYPE_04+" AND R.ACCREDIT_AMOUNT = 0 AND\n");
		sqlStr.append("       R.FREE_TIMES = 1 AND (R.APPROVAL_YN = 1 OR R.IS_CLAIM_FORE = 1)))"); 

		//判断登陆系统(工单是否配件大类判断)
		if(Utility.testString(map.get("roId"))) {
			sqlStr.append(" and r.ID="+map.get("roId")+" \n");
		}
		if(Utility.testString(map.get("dealerId"))) {
			//sqlStr.append(" and d.DEALER_ID="+map.get("dealerId")+" \n");
			//sqlStr.append(" and (d.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+map.get("dealerId")+"   connect by PRIOR d.dealer_id = d.parent_dealer_d) or r.second_dealer_id="+map.get("dealerId")+" ) \n");
//			sqlStr.append("AND DECODE(r.SECOND_DEALER_ID, null, b.DEALER_ID, r.SECOND_DEALER_ID) IN\n");
//			sqlStr.append("      (SELECT d.DEALER_ID\n");
//			sqlStr.append("         FROM TM_DEALER D\n");
//			sqlStr.append("        START WITH d.DEALER_ID = "+map.get("dealerId")+"\n");
//			sqlStr.append("       CONNECT BY PRIOR d.DEALER_ID = d.PARENT_DEALER_D)"); 
			sqlStr.append(" and nvl(r.SECOND_DEALER_ID, d.DEALER_ID)="+map.get("dealerId")+"\n");

		}
		if(Utility.testString(map.get("roNo"))) {
			sqlStr.append(" and r.RO_NO LIKE '%"+map.get("roNo")+"%' \n");
		}
		if(Utility.testString(map.get("repairType"))) {
			sqlStr.append(" and r.REPAIR_TYPE_CODE = '"+map.get("repairType")+"' \n");
		}
		
		//结算开始时间
		if(Utility.testString(map.get("balanceDateStart"))) {
			sqlStr.append(" and r.UPDATE_DATE >= to_date('"+map.get("balanceDateStart")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		//结算结束时间
		if(Utility.testString(map.get("balanceDateEnd"))) {
			sqlStr.append(" AND r.UPDATE_DATE <= to_date('"+map.get("balanceDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		
		
		//车牌号
		if(Utility.testString(map.get("licenseNo"))) {
			sqlStr.append(" and v.LICENSE_NO LIKE '%"+map.get("licenseNo")+"%' \n");
		}
		//车型--去掉
		if(Utility.testString(map.get("modelId"))) {
			sqlStr.append(" and V.MODEL_ID="+map.get("modelId")+" \n");
		}
		if (Utility.testString(map.get("customerName"))) {
			sqlStr.append(" and r.owner_name like '%"+map.get("customerName")+"%' \n");
		}
		if(Utility.testString(map.get("isForl"))) {
			sqlStr.append(" and r.FORL_STATUS = "+map.get("isForl")+" \n");
		}
		if(Utility.testString(map.get("roStatus"))) {
			sqlStr.append(" and r.RO_STATUS = "+map.get("roStatus")+" \n");
		}
		sqlStr.append("and r.order_valuable_type="+Constant.RO_PRO_STATUS_01+"\n");
//		if(Utility.testString(map.get("isApp"))) {
//			sqlStr.append(" and NOT EXISTS (SELECT RO_NO FROM TT_AS_WR_APPLICATION A WHERE A.RO_NO=R.RO_NO) \n");
//		}
		/*if(Utility.testString(map.get("vin"))) {
			sqlStr.append(" and r.VIN LIKE '%"+map.get("vin")+"%' \n");
		}*/
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN不为空
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "r"));
		}
		if (Utility.testString(map.get("createDateStr"))) {
			sqlStr.append(" and r.CREATE_DATE >= to_date('"+map.get("createDateStr")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if (Utility.testString(map.get("createDateEnd"))) {
			sqlStr.append(" AND r.CREATE_DATE <= to_date('"+map.get("createDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
//		if(Utility.testString(map.get("isApp"))) {
//			sqlStr.append(" and NOT EXISTS (SELECT RO_NO FROM tt_as_repair_order ro WHERE ro.RO_NO=R.RO_NO and RO.FREE_TIMES>1 AND RO.REPAIR_TYPE_CODE="+Constant.REPAIR_TYPE_04+" ) \n");
//		}
		//sqlStr.append("    and NOT EXISTS (SELECT RO_NO  FROM tt_as_repair_order ro  WHERE ro.RO_NO = R.RO_NO  and RO.Accredit_Amount=1 and RO.FREE_TIMES>1 and ro.approval_yn=1  AND RO.REPAIR_TYPE_CODE = "+Constant.REPAIR_TYPE_04+")");

	    sqlStr.append(" and r.for_balance_time>=sysdate-"+day+" \n");
	  
		sqlStr.append(" and r.IS_CANCEL is null   order by r.update_date desc ");
		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
				pageSize, curPage);
	}
	
	/**
	 * Iverson update By 2010-12-16
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	@SuppressWarnings("static-access")
	public PageResult<TtAsWrForeapprovalBean> queryRepairOrder1(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
		String roNo =DaoFactory.getParam(request,"RO_NO");
		String vin = DaoFactory.getParam(request,"VIN");
		String repairType = DaoFactory.getParam(request,"REPAIR_TYPE");
		//String roCreateDate = DaoFactory.getParam(request,"RO_CREATE_DATE");
		//String deliveryDate = DaoFactory.getParam(request,"DELIVERY_DATE");
		String isForl = DaoFactory.getParam(request,"RO_FORE");
		//String roStatus = DaoFactory.getParam(request,"RO_STATUS");
		String dealerId = DaoFactory.getParam(request,"dealerId");// 经销商代码
		//String orgCode = DaoFactory.getParam(request,"orgCode");
		//String province = DaoFactory.getParam(request,"province");// 省份CODE
		String isWarning = DaoFactory.getParam(request,"IS_WARNING");//是否预警
		String part_code = DaoFactory.getParam(request,"part_code");//主因件
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append(" select  w.*,decode(tu.name, null, t.dealer_name,tu.name) as create_name,  t.dealer_shortname as dealer_name, ro.trouble_descriptions,ro.trouble_reason,ro.remarks,ro.repair_method,ro.is_warning,\n");
		sqlStr.append("case when substr(W.NEED_AUDITOR,instr(W.NEED_AUDITOR,',',-1,1)+1) = w.current_auditor and\n");
		sqlStr.append("              w.report_status = 11561002 then\n");
		sqlStr.append("          '--'\n");
		sqlStr.append("         else\n");
		sqlStr.append("          (select af.approval_level_name\n");
		sqlStr.append("             from tt_as_wr_authinfo af\n");
		sqlStr.append("            where af.approval_level_code =\n");
		sqlStr.append("                  substr(W.NEED_AUDITOR,instr(W.NEED_AUDITOR,',',-1,1)+1))\n");
		sqlStr.append("       end last_name,"); 
		sqlStr.append("NVL(ro.repair_part_amount, 0) repair_part_amount, /*材料总费用*/\n");
		sqlStr.append("      NVL(ro.labour_amount, 0) labour_amount, /*工时总费用*/\n");
		sqlStr.append("      NVL(ro.add_item_amount, 0) add_item_amount, /*其他总费用*/\n");
		sqlStr.append("      NVL(ro.repair_amount, 0) repair_amount, /*最终汇总金额*/"); 
		sqlStr.append("      NVL(ro.accessories_price, 0) accessories_price /*辅料总金额*/ \n");


		sqlStr.append("   from Tt_As_Wr_Foreapproval  w JOIN tt_as_repair_order ro ON w.ro_no = ro.ro_no left join tc_user tu on tu.user_id = ro.create_by left join tm_dealer t on t.dealer_code = ro.dealer_code \n");
		sqlStr.append(" where 1=1    and w.dealer_id=t.dealer_id \n");
		sqlStr.append("AND ro.create_date >= to_date('2013-08-26', 'yyyy-mm-dd') /*时间控制*/\n"); 
		sqlStr.append("  and( w.current_auditor in(select u.approval_level_code from tc_user u where u.User_Id = "+loginUser.getUserId()+" )or w.current_auditor is null)\n");
		DaoFactory.getsql(sqlStr, "w.ro_no", roNo, 2);
		DaoFactory.getsql(sqlStr, "w.APPROVAL_TYPE", repairType, 1);
		DaoFactory.getsql(sqlStr, "w.vin", vin, 2);
		DaoFactory.getsql(sqlStr, "w.REPORT_STATUS", isForl, 1);
		DaoFactory.getsql(sqlStr, "ro.is_warning", isWarning, 1);
		DaoFactory.getsql(sqlStr, "w.dealer_id", dealerId, 6);
		sqlStr.append(" and w.REPORT_STATUS="+Constant.RO_FORE_01+""); 
		sqlStr.append(dao1.getOrgDealerLimitSqlByService("t", loginUser));
		if(!"".equals(part_code)){
			sqlStr.append(" and ro.id in (select p.ro_id from tt_as_ro_repair_part p where 1=1 ");
			DaoFactory.getsql(sqlStr, "p.part_no", part_code, 2);
			sqlStr.append(" )");
		}
		sqlStr.append(" order by w.create_date desc  ");
		System.out.println("sqlsql=="+sqlStr.toString());
		return pageQuery(TtAsWrForeapprovalBean.class, sqlStr.toString(), null,pageSize, currPage);
	}
	@SuppressWarnings("static-access")
	public PageResult<TtAsWrForeapprovalBean> queryRepairOrderOEM(
			Map<String, String> map,AclUserBean logonUser, int pageSize, int curPage) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select w.*, to_char(w.update_Date,'yyyy-MM-dd hh24:mi') update_Dat,org.org_name orgname,\n");
		sqlStr.append("       org2.org_name orgname2,ro.is_warning,\n");
		sqlStr.append("       d.dealer_shortname dealer_name,\n");
		sqlStr.append("       u.name as audit_name,decode(w.report_status,'11561002','--','11561003','--',decode(w.audit_person, -1, '', wa.approval_level_name)) approval_level_name,\n");
		sqlStr.append("case when substr(W.NEED_AUDITOR,instr(W.NEED_AUDITOR,',',-1,1)+1)= w.current_auditor and w.report_status=11561002\n");
		sqlStr.append("      then '--'\n");
		sqlStr.append("      else\n");
		sqlStr.append("      (select af.approval_level_name from  tt_as_wr_authinfo af where af.approval_level_code =substr(W.NEED_AUDITOR,instr(W.NEED_AUDITOR,',',-1,1)+1) )  end last_name,\n");
		sqlStr.append("       NVL(ro.repair_part_amount, 0) repair_part_amount, /*材料总费用*/ \n");
		sqlStr.append("       NVL(ro.labour_amount, 0) labour_amount, /*工时总费用*/\n");
		sqlStr.append("       NVL(ro.add_item_amount, 0) add_item_amount, /*其他总费用*/\n");
		sqlStr.append("       NVL(ro.repair_amount, 0) repair_amount, /*最终汇总金额*/\n");
		sqlStr.append("       NVL(ro.accessories_price, 0) accessories_price /*辅料费总金额*/\n");
		sqlStr.append("  from Tt_As_Wr_Foreapproval w  left join tc_user u on  w.audit_person = u.user_id\n");
		sqlStr.append(" left join tm_dealer d on d.dealer_id = w.dealer_id\n"); 
		sqlStr.append(" JOIN tt_as_repair_order ro ON w.ro_no = ro.ro_no /*维修工单*/\n"); 
		sqlStr.append(" left join tm_dealer_org_relation re on re.dealer_id=d.dealer_id \n");
		sqlStr.append(" left join tm_org org on org.org_id=re.org_id \n");
		sqlStr.append(" left join tm_org org2 on org2.org_id = org.parent_org_id \n");
		sqlStr.append("  left join tt_as_wr_authinfo wa on wa.approval_level_code =  w.current_auditor and wa.type = 0\n");
		sqlStr.append(" where 1 = 1"); 
		sqlStr.append("  AND ro.create_date >= to_date('2013-08-26','yyyy-mm-dd') /*时间控制*/"); 
		

		if(Utility.testString(map.get("roNo"))) {
		sqlStr.append(" and w.ro_no like '%"+map.get("roNo")+"%' ");
		}
		if(Utility.testString(map.get("repairType"))) {
		sqlStr.append(" and w.APPROVAL_TYPE="+map.get("repairType")+" ");
		}
		if(Utility.testString(map.get("vin"))) {
		sqlStr.append(" and w.vin like '%"+map.get("vin")+"%' ");
		}
		if(Utility.testString(map.get("isForl"))) {
		sqlStr.append(" and w.REPORT_STATUS="+map.get("isForl")+" ");
		}
		if(Utility.testString(map.get("auditName"))){
			sqlStr.append(" and u.name like '%").append(map.get("auditName")).append("%'\n");
		}
		if(Utility.testString(map.get("isWarning"))) {
			sqlStr.append(" and ro.is_warning = "+map.get("isWarning")+" ");
		}
		if(Utility.testString(map.get("approveDate"))){
			sqlStr.append(" and w.APPROVAL_DATE >= to_date('"
					+map.get("approveDate")
					+" 00:00:00','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("approveDate2"))){
			sqlStr.append(" and w.APPROVAL_DATE <= to_date('"
					+map.get("approveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		DaoFactory.getsql(sqlStr, "w.update_Date",map.get("audit_date_start") , 3);
		DaoFactory.getsql(sqlStr, "w.update_Date",map.get("audit_date_end") , 4);
		DaoFactory.getsql(sqlStr, "org2.org_name",map.get("org_name") , 2);
		if(Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" and w.dealer_id in ("+map.get("dealerId")+") ");
			}
//		sqlStr.append("and w.dealer_id in (\n");
//		sqlStr.append("   select r.dealer_id from tm_dealer_org_relation r, (select org_id from tm_org o where o.org_level in (2,3) start with  o.org_id="+map.get("orgId")+"  connect by prior  o.org_id = o.parent_org_id) xo\n");
//		sqlStr.append("where xo.org_id = r.org_id\n");
//		sqlStr.append(")"); 
		sqlStr.append(dao1.getOrgDealerLimitSqlByService("w", logonUser));
		sqlStr.append(" order by w.create_date desc  ");
		return pageQuery(TtAsWrForeapprovalBean.class, sqlStr.toString(), null,
				pageSize, curPage);
	}
	public PageResult<TtAsWrForeapprovalBean> queryRepairOrderDealer(
			Map<String, String> map, int pageSize, int curPage) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select w.*,o.is_warning,\n");
		sqlStr.append("       (select dealer_name from tm_dealer d where d.dealer_id = w.dealer_id) as dealer_name,\n");
		sqlStr.append("       (select name from tc_user where user_id = w.audit_person) as audit_name,\n");
		sqlStr.append("       decode(w.report_status,'11561002','--','11561003','--',decode(w.audit_person, -1, '', wa.approval_level_name)) approval_level_name,\n");
		sqlStr.append("	case when substr(W.NEED_AUDITOR,instr(W.NEED_AUDITOR,',',-1,1)+1)= w.current_auditor and w.report_status=11561002\n");
		sqlStr.append("      then '--'\n");
		sqlStr.append("      else\n");
		sqlStr.append("      (select af.approval_level_name from  tt_as_wr_authinfo af where af.approval_level_code =substr(W.NEED_AUDITOR,instr(W.NEED_AUDITOR,',',-1,1)+1) )  end last_name\n");

		sqlStr.append("  from Tt_As_Wr_Foreapproval w\n");
		sqlStr.append("  left join tt_as_wr_authinfo wa on wa.approval_level_code =  w.current_auditor and wa.type = 0 ,tt_as_repair_order o"); 
		sqlStr.append(" where 1=1  and o.ro_no = w.ro_no ");
		sqlStr.append("and o.is_customer_in_asc=1\n"); 
		if(Utility.testString(map.get("roNo"))) {
		sqlStr.append(" and w.ro_no like '%"+map.get("roNo")+"%' ");
		}
		if(Utility.testString(map.get("repairType"))) {
		sqlStr.append(" and w.APPROVAL_TYPE="+map.get("repairType")+" ");
		}
		if(Utility.testString(map.get("vin"))) {
		sqlStr.append(" and w.vin='"+map.get("vin")+"' ");
		}
		if(Utility.testString(map.get("isForl"))) {
		sqlStr.append(" and w.REPORT_STATUS="+map.get("isForl")+" ");
		}
		if(Utility.testString(map.get("isWarning"))) {
		sqlStr.append(" and o.IS_WARNING="+map.get("isWarning")+" ");
		}
		if(Utility.testString(map.get("approveDate"))){
			sqlStr.append(" and w.APPROVAL_DATE >= to_date('"
					+map.get("approveDate")
					+" 00:00:00','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("approveDate2"))){
			sqlStr.append(" and w.APPROVAL_DATE <= to_date('"
					+map.get("approveDate2")
					+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" and (w.dealer_id="+map.get("dealerId")+" or o.second_dealer_id="+map.get("dealerId")+" )\n");
			}
		//sqlStr.append(" and w.REPORT_STATUS="+Constant.RO_FORE_01+"");
		sqlStr.append(" order by w.create_date desc  ");
		return pageQuery(TtAsWrForeapprovalBean.class, sqlStr.toString(), null,
				pageSize, curPage);
	}
	
	
	
	
	/*
	 * 根据工单号查询 维修项目细则－附加项目
	 * @param roNo
	 * @return List<TtAsRoAddItemPO>
	 */
	public List<TtAsRoAddItemPO> queryAddItem(String roNo,String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select * \n");
		sql.append(" from tt_as_ro_add_item i \n");
		sql.append("where 1=1 \n");
		// 工单号必须唯一
		if(roNo!=null && !("").equals(roNo)){
			sql.append(" and i.ro_id=");
			sql.append(" (select id from tt_as_repair_order where ro_no='"+roNo+"' \n)");
		}
		if(Utility.testString(id)){
			sql.append(" and i.ro_id="+id+" \n");
		}
		//sql.append("  and i.add_item_code not in('3537007','3537006') \n");
		sql.append(" order by i.add_item_code desc \n");
		return dao.select(TtAsRoAddItemPO.class,sql.toString(),null);
	}
	
	/*******************************Iverson add 2010-11-14 此功能是判断如果工单生成了索赔单就必须先删除索赔单再删除工单*****************/
	/**
	 * 根据工单ID查询工单号
	 */
	public List getRoNoById(TtAsRepairOrderPO po){
		List list = dao.select(po);
		return list;
	}
	/**
	 * Iverson add with 2010-11-14
	 * 查询工单下面有没生成有索赔单
	 * @param roNo
	 * @return 
	 */
	public long queryTtAsWrApplication(String roNo) throws Exception{
		POFactory factory = POFactoryBuilder.getInstance();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as count\n");
		sql.append(" from tt_as_wr_application i \n");
		sql.append("where i.ro_no='"+roNo+"' \n");
		List list=factory.select(sql.toString(), null,new DAOCallback(){
			public Long wrapper(ResultSet rs, int idx){
				Long count=0L;
				try{
					count=rs.getBigDecimal("count").longValue();
				}catch(Exception e){
					e.printStackTrace();
				}
				return count;	
			}
		}); 
		return (Long)list.get(0);
	}
	/************************Iverson add 2010-11-14 此功能是判断如果工单生成了索赔单就必须先删除索赔单再删除工单***********************************************/
	
	/*
	 * 根据工单号查询 维修项目细则－维修项目
	 * @param roNo
	 * @return List<TtAsRoAddItemPO>
	 */
	public List<TtAsRoLabourPO> queryRepairitem(String roNo,String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select * \n");
		//sql.append("select l.labour_name,l.std_labour_hour,l.assign_labour_hour, \n");
		//sql.append(" l.worker_type_code,l.charge_partition_code \n");
		sql.append(" from tt_as_ro_labour l \n");
		sql.append("where 1=1 \n");
		// 工单号必须唯一
		if(roNo!=null && !("").equals(roNo)){
			sql.append(" and l.ro_id=");
			sql.append(" (select id from tt_as_repair_order where ro_no='"+roNo+"' \n)");
		}
		if(Utility.testString(id)){
			sql.append(" and l.ro_id="+id+" \n");
		}
		sql.append(" order by l.id  \n");
		return dao.select(TtAsRoLabourPO.class,sql.toString(),null);
	}
	/*
	 * 根据工单号查询 维修项目细则－维修项目
	 * @param roNo
	 * @return List<TtAsRoAddItemPO>
	 */
	public List<TtAsRoLabourBean> queryRepairitem2(String roNo,String id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT L.*,m.mal_code||'--'||m.mal_name mal_name\n");
		sql.append("  FROM TT_AS_RO_LABOUR L\n");
		sql.append("  LEFT JOIN TT_AS_WR_MALFUNCTION M ON M.MAL_ID = L.MAL_FUNCTION\n"); 
		sql.append("where 1=1 \n");
		// 工单号必须唯一
		if(roNo!=null && !("").equals(roNo)){
			sql.append(" and l.ro_id=");
			sql.append(" (select id from tt_as_repair_order where ro_no='"+roNo+"' \n)");
		}
		if(Utility.testString(id)){
			sql.append(" and l.ro_id="+id+" \n");
		}
		sql.append(" order by l.id  \n");
		return dao.select(TtAsRoLabourBean.class,sql.toString(),null);
	}
	/*
	 * 根据工单号查询 维修项目细则－维修零件
	 * @param roNo
	 * @return List<TtAsRoAddItemPO>
	 */
	public List<TtAsRoRepairPartPO> queryRepairPart(String roNo,String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select * \n");
		//sql.append("select p.part_name,p.part_quantity,p.unit_code,p.charge_partition_code \n");
		sql.append(" from tt_as_ro_repair_part p \n");
		sql.append("where 1=1 \n");
		// 工单号必须唯一
		if(roNo!=null && !("").equals(roNo)){
			sql.append(" and p.ro_id=");
			sql.append(" (select id from tt_as_repair_order where ro_no='"+roNo+"' \n)");
		}
		if(Utility.testString(id)){
			sql.append(" and p.ro_id="+id+" \n");
		}
		sql.append(" order by p.id  \n");
		return dao.select(TtAsRoRepairPartPO.class,sql.toString(),null);
	}
	/*
	 * 根据工单号查询 维修项目细则－维修零件
	 * @param roNo
	 * @return List<TtAsRoAddItemPO>
	 */
	public List<TtAsRoRepairPartBean> queryRepairPart2(String roNo,String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select l.wr_labourcode labour_code, l.wr_labourname labour_name,l.labour_amount,p.*,d.unit\n");
		sql.append(" from tt_as_ro_repair_part p,tt_as_ro_labour l,tt_part_define d\n");
		sql.append("where 1=1 and l.ro_id = p.ro_id  and l.wr_labourcode = p.labour and p.PART_NO=d.part_oldcode\n");
		// 工单号必须唯一
		if(roNo!=null && !("").equals(roNo)){
			sql.append(" and p.ro_id=");
			sql.append(" (select id from tt_as_repair_order where ro_no='"+roNo+"' \n)");
		}
		if(Utility.testString(id)){
			sql.append(" and p.ro_id="+id+" \n");
		}
		sql.append(" order by l.wr_labourcode   \n");
		return dao.select(TtAsRoRepairPartBean.class,sql.toString(),null);
	}
	
	/**
	 * 
	* @Title: queryRoById 
	* @Description: TODO(根据ID查询工单信息) 
	* @param @param id
	* @param @return    设定文件 
	* @return TtAsRepairOrderExtPO    返回类型 
	* @throws
	 */
	public TtAsRepairOrderExtBean queryRoById(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select to_char(p.ro_create_date,'yyyy-MM-dd HH24:mi') CRE_DATE, p.*,ba.area_name as yieldly_Name,v.product_date,ac.activity_name as campaign_name,nvl(ac.is_fixfee,0) as cam_fix,g.model_id as model_id,g.series_id as series_id,g.brand_id as brand_id, \n");
		sql.append("g.model_name as model_name,g.series_name as series_name, g.PACKAGE_ID,g.brand_name as brand_name,g.package_name, \n");
		sql.append("d.phone dealer_phone,  d.dealer_code dealer_code_s, d.dealer_id as dealer_id_s,\n");
		sql.append("o.start_time as start_time,o.end_time as end_time,o.out_person as out_person,\n");
		sql.append("o.out_site as out_site,o.out_licenseno as out_licenseno,o.from_adress as from_adress,\n");
		sql.append("o.end_adress as end_adress,o.out_mileage as out_mileages ,v.purchased_date,v.Color\n");
		sql.append(" from tt_as_repair_order p \n");
		sql.append(" left outer join tt_as_wr_outrepair o on p.ro_no=o.ro_no \n");
		sql.append(" left outer join tm_vehicle v on p.vin=v.vin \n ");
		sql.append(" left join tm_business_area ba on ba.area_id = v.yieldly");
		sql.append(" left outer join VW_MATERIAL_GROUP_SERVICE g on v.package_id=g.package_id \n");
		sql.append(" left outer join tm_dealer d on nvl(p.Second_Dealer_Id,p.Dealer_Id) =d.Dealer_Id \n");
		sql.append(" LEFT OUTER JOIN TT_AS_ACTIVITY AC ON AC.ACTIVITY_CODE=p.CAM_CODE AND AC.IS_DEL=0 \n");
		// 工单ID
		if(Utility.testString(id)){
			sql.append(" where p.id="+id+"\n");
		}
		List<TtAsRepairOrderExtBean> ls = dao.select(TtAsRepairOrderExtBean.class,sql.toString(),null);
		TtAsRepairOrderExtBean extPo = new TtAsRepairOrderExtBean();
		if (ls!=null) {
			if(ls.size()>0) {
				extPo = ls.get(0);
			}
		}
		return extPo;
	}
	
	public String frLevel(String vin ,String roNO)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT min(B.FR_LEVEL) AS FR_LEVEL from Tt_As_Wr_Fee_Warranty A ,tt_as_wr_fee_rule B where A.VIN = '"+vin+"' AND A.RO_NO = '"+roNO+"'  AND A.CUR_WR_DAYS >=B.FR_WR_DAYS");
		List<TtAsWrFeeRulePO>  listFee= dao.select(TtAsWrFeeRulePO.class, sql.toString(), null);
		sql = new StringBuffer(); 
		
		sql.append("SELECT min(B.VR_LEVEL) AS VR_LEVEL \n" );
		sql.append("  from Tt_As_Wr_Vin_Repair_Days A, TT_AS_WR_VIN_RULE B\n" );
		sql.append(" where A.VIN = '"+vin+"'\n" );
		sql.append("   AND A.CUR_WR_DAYS >= B.VR_WARRANTY\n" );
		sql.append("   AND B.VR_TYPE = "+Constant.VR_TYPE_1);
		
		List<TtAsWrVinRulePO>  listVin = dao.select(TtAsWrVinRulePO.class,sql.toString(),null);
		sql = new StringBuffer();
		sql.append("SELECT min(B.VR_LEVEL)  AS VR_LEVEL \n" );
		sql.append("  from Tt_As_Wr_Vin_Part_Repair_Times A, TT_AS_WR_VIN_RULE B\n" );
		sql.append(" where A.VIN = '"+vin+"'\n" );
		sql.append("   AND A.CUR_TIMES >= B.VR_WARRANTY\n" );
		sql.append("   AND B.VR_TYPE = "+Constant.VR_TYPE_2+"\n" );
		sql.append("   AND A.PART_WR_TYPE = "+Constant.PART_WR_TYPE_1+"\n" );
		sql.append("   AND A.PART_WR_TYPE = B.PART_WR_TYPE\n" );
		sql.append("UNION\n" );
		sql.append("SELECT min(B.VR_LEVEL)  AS VR_LEVEL \n" );
		sql.append("  from Tt_As_Wr_Vin_Part_Repair_Times A, TT_AS_WR_VIN_RULE B\n" );
		sql.append(" where A.VIN = '"+vin+"'\n" );
		sql.append("   AND A.CUR_TIMES >= B.VR_WARRANTY\n" );
		sql.append("   AND B.VR_TYPE = "+Constant.VR_TYPE_2+"\n" );
		sql.append("   AND A.PART_WR_TYPE = "+Constant.PART_WR_TYPE_2+"\n" );
		sql.append("   AND A.PART_WR_TYPE = B.PART_WR_TYPE\n" );
		sql.append("   AND A.PART_CODE = B.VR_PART_CODE\n" );
		sql.append("UNION\n" );
		sql.append("SELECT min(B.VR_LEVEL)  AS VR_LEVEL \n" );
		sql.append("  from Tt_As_Wr_Vin_Part_Repair_Times A, TT_AS_WR_VIN_RULE B\n" );
		sql.append(" where A.VIN = '"+vin+"'\n" );
		sql.append("   AND A.CUR_TIMES >= B.VR_WARRANTY\n" );
		sql.append("   AND B.VR_TYPE = "+Constant.VR_TYPE_2+"\n" );
		sql.append("   AND A.PART_WR_TYPE = "+Constant.PART_WR_TYPE_3+"\n" );
		sql.append("   AND A.PART_WR_TYPE = B.PART_WR_TYPE");
		List<TtAsWrVinRulePO>  listVinPart = dao.select(TtAsWrVinRulePO.class,sql.toString(),null);
		int VR_LEVEL = 0;
		if(listFee != null && listFee.size()>0)
		{
			VR_LEVEL = listFee.get(0).getFrLevel();
		}
		if(listVin != null && listVin.size()>0)
		{
			if(VR_LEVEL == 0)
			{
				VR_LEVEL = listVin.get(0).getVrLevel();
			}else
			{
				if(listVin.get(0).getVrLevel() < VR_LEVEL && listVin.get(0).getVrLevel() != 0)
				{
					VR_LEVEL = listVin.get(0).getVrLevel();
				}
			}
			
			
		}
		if(listVinPart != null && listVinPart.size()>0)
		{
			if(VR_LEVEL == 0)
			{
				VR_LEVEL = listVinPart.get(0).getVrLevel();
			}else
			{
				if(listVinPart.get(0).getVrLevel() < VR_LEVEL  && listVin.get(0).getVrLevel() != 0)
				{
					VR_LEVEL = listVinPart.get(0).getVrLevel();
				}
			}
		}
        if(VR_LEVEL !=0)
        {
        	if(Constant.VR_LEVEL_1.equals("" + VR_LEVEL))
        	{
        		return "一级";
        	}else if(Constant.VR_LEVEL_2.equals("" + VR_LEVEL)){
        		return "二级";
        	}else
        	{
        		return "三级";
        	}
        	
        }else
        {
        	return "三包判定";
        }
		
	}
	/**
	 * 
	* @Title: queryInfoById 
	* @Description: TODO(根据ID查询质量信息跟踪卡) 
	* @param @return    设定文件 
	* @return TtAsWrInformationqualityExtPO    返回类型 
	* @throws
	 */
	public TtAsWrInformationqualityExtPO queryInfoByClaimId (String id) {
		StringBuffer sql= new StringBuffer();
		sql.append("select * from tt_as_wr_informationquality\n" );
		sql.append("where 1=1 \n");
		//if(Utility.testString(id)) {
			sql.append(" and claim_id="+id+"");
		//}
		TtAsWrInformationqualityExtPO tawiep = new  TtAsWrInformationqualityExtPO();
		List<TtAsWrInformationqualityExtPO> ls = select(TtAsWrInformationqualityExtPO.class,sql.toString(),null);
		if (ls!=null) {
			if (ls.size()>0) {
				tawiep = ls.get(0);
			}
		}
		return tawiep;
	}
	
	/**
	 * 
	* @Title: queryInfoByClaimInfoId 
	* @Description: TODO(根据质量信息卡ID查询质量信息卡) 
	* @param @param id
	* @param @return    设定文件 
	* @return TtAsWrInformationqualityExtPO    返回类型 
	* @throws
	 */
	public TtAsWrInformationqualityExtPO queryInfoByClaimInfoId (String id) { 
		StringBuffer sql= new StringBuffer();
		sql.append("select * from tt_as_wr_informationquality\n" );
		sql.append("where 1=1 \n");
		//if(Utility.testString(id)) {
			sql.append(" and id="+id+"");
		//}
		TtAsWrInformationqualityExtPO tawiep = new  TtAsWrInformationqualityExtPO();
		List<TtAsWrInformationqualityExtPO> ls = select(TtAsWrInformationqualityExtPO.class,sql.toString(),null);
		if (ls!=null) {
			if (ls.size()>0) {
				tawiep = ls.get(0);
			}
		}
		return tawiep;
	}
	/**
	 * add by liuqiang
	* @Title: queryManage 
	* @Description: TODO(根据工单ID查询维修工单辅料) 
	* @param @param id 工单ID
	* @param @return    设定文件 
	* @return List<TtAsRoManagePO>    返回类型 
	* @throws
	 */
	public List<TtAsRoManagePO> queryManage(Long id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * \n");
		sql.append(" from tt_as_ro_manage p \n");
		sql.append("where 1=1 \n");
		sql.append(" and p.ro_id="+id+" \n");
		return dao.select(TtAsRoManagePO.class, sql.toString(), null);
	}
	public List<LabourPartBean> queryLabourPartBean(
			String vin, int pageSize, int curPage) {
		StringBuilder sql= new StringBuilder();
		sql.append("select a.RO_CREATE_DATE,a.DEALER_CODE,a.TOTAL_MILEAGE,a.OWNER_NAME,b.PURCHASED_DATE,c.WR_LABOURCODE,c.WR_LABOURNAME,d.PART_NO,d.PART_NAME \n" );
		sql.append("from tt_as_repair_order a,tm_vehicle b,tt_as_ro_labour c,tt_as_ro_repair_part d\n" );
		sql.append("where a.id=d.ro_id\n" );
		sql.append("and a.id=c.ro_id\n" );
		sql.append("and a.id=d.ro_id\n" );
		sql.append("and a.vin = b.vin\n");
		sql.append("and d.labour_code = c.wr_labourcode\n");
		//sql.append("and c.labour_code=d.labour_code\n");
		sql.append("and a.vin='"+vin+"'");
		//List<Object> param =new ArrayList<Object>();
		return this.select(LabourPartBean.class, sql.toString(), null);
		//return pageQuery(LabourPartBean.class, sql.toString(), null,
		//		pageSize, curPage);
	}
	/**
	 * 
	* @Title: getFree 
	* @Description: TODO(根据申请的免费保养次数取得免费保养规则表中的数据) 
	* @param @return    设定文件 
	* @return List<TtAsWrQamaintainPO>    返回类型 
	* @throws
	 */
	public List<TtAsWrQamaintainPO> getFree(int times,Long OemCompanyId,int month,int day,Double mileage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select * from TT_AS_WR_QAMAINTAIN\n" );
		sql.append("where 1=1\n" );
		sql.append("and free_times="+times+" \n");
		sql.append(" and MIN_DAYS<="+day+" \n");
		sql.append(" and MAX_DAYS>="+day+" \n");
		//sql.append("and month>="+month+"\n"); //保修月份小于
		//sql.append("and start_mileage<="+mileage+""); //行驶里程数大于开始里程数
		sql.append("and start_mileage<="+mileage+" \n"); //行驶里程数小于结束里程数
		sql.append("and end_mileage>="+mileage+""); //行驶里程数小于结束里程数
		return select(TtAsWrQamaintainPO.class,sql.toString(),null);
		
	}
	public Double getFreeForActivity(String vin ) {
		StringBuffer sql= new StringBuffer();
		sql.append("select  mg.free\n");
		sql.append("from  tm_vehicle v\n");
		sql.append(" left join TT_AS_WR_MODEL_ITEM mi on mi.model_id = v.package_id\n");
		sql.append(" left join  tt_as_wr_model_group mg on mg.wrgroup_id = mi.wrgroup_id\n");
		sql.append(" where v.vin='"+vin+"'"); 
		List<TtAsWrModelGroupPO> list =  this.select(TtAsWrModelGroupPO.class,sql.toString(),null);
		if(list!=null && list.size()>0){
			return list.get(0).getFree();
		} return 0.0;
	}
	public List<TtAsWrQamaintainPO> getFree2(int times,Long OemCompanyId,int month,int day,Double mileage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select * from TT_AS_WR_QAMAINTAIN\n" );
		sql.append("where 1=1\n" );
		sql.append("and free_times="+times+" \n");
		//sql.append(" and MIN_DAYS<="+day+" \n");
		sql.append(" and MAX_DAYS>="+day+" \n");
		//sql.append("and month>="+month+"\n"); //保修月份小于
		//sql.append("and start_mileage<="+mileage+""); //行驶里程数大于开始里程数
		//sql.append("and start_mileage<="+mileage+" \n"); //行驶里程数小于结束里程数
		sql.append("and end_mileage>="+mileage+""); //行驶里程数小于结束里程数
		return select(TtAsWrQamaintainPO.class,sql.toString(),null);
		
	}
	
	/**
	 * 工单保存时, 如果是强保,判断是否满足强保要求.并进行预授权申请
	 */
	public List<TtAsWrQamaintainPO> getFree11(int times,Long OemCompanyId,int month,int day,Double mileage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select * from TT_AS_WR_QAMAINTAIN\n" );
		sql.append("where 1=1\n" );
		sql.append("and free_times="+times+" \n");
		if(times==1){
		sql.append(" and MIN_DAYS<="+day+" \n");
		sql.append(" and MAX_DAYS>="+day+" \n");
		}
		sql.append("and start_mileage<="+mileage+" \n"); //行驶里程数小于结束里程数
		sql.append("and end_mileage>="+mileage+""); //行驶里程数小于结束里程数
		return select(TtAsWrQamaintainPO.class,sql.toString(),null);
		
	}
	/**
	 * 
	* @Title: getActFree 
	* @Description: TODO(取得车辆在服务活动中的保养次数) 
	* @param @param vin
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	public Integer getActFree(String vin) {
		int times  = 0;
		StringBuffer sql= new StringBuffer();
		sql.append("select count(*) as upload_pre_period from TT_AS_ACTIVITY_VEHICLE v\n" );
		sql.append("left outer join tt_as_activity a on v.activity_id = a.activity_id\n" );
		sql.append("where 1=1\n" );
		sql.append("and a.activity_type="+Constant.SERVICEACTIVITY_KIND_02+"\n" );
		sql.append("and v.vin=''");
		
		List<TtAsActivityPO> ls = select(TtAsActivityPO.class,sql.toString(),null);
		if (ls!=null) {
			if (ls.size()>0) {
				times = ls.get(0).getUploadPrePeriod();
			}
		}
		 
		return times;
	}
	
	/**
	 * 
	* @Title: getPartGuaListByVin 
	* @Description: TODO(根据VIN得到配件三包列表) 
	* @param @param vin
	* @param @return    设定文件 
	* @return List    返回类型 
	* @throws
	 */
	public List<TtAsWrRuleListPO> getPartGuaListByVin (String vin,String partCode) {
		StringBuffer sql= new StringBuffer();
		sql.append("select c.PART_CODE as part_code, max(c.CLAIM_MONTH) as claim_month,MAX(c.CLAIM_MELIEAGE) as claim_melieage from tm_vehicle v\n" );
		sql.append("left outer join TT_AS_WR_GAME a  on v.claim_tactics_id=a.id\n" );
		sql.append("left outer join TT_AS_WR_RULE b on a.rule_id = b.id\n" );
		sql.append("left outer join TT_AS_WR_RULE_LIST c on b.id=c.rule_id\n" );
		sql.append("where 1=1\n" );
		sql.append("and V.VIN = '"+vin+"'\n" );
		sql.append("and PART_CODE='"+partCode+"'");
		sql.append("GROUP BY PART_CODE");
		List<TtAsWrRuleListPO> ls = select(TtAsWrRuleListPO.class,sql.toString(),null);
		return ls;
	}
	
	/**
	 * 
	* @Title: getPartGuaCommonListByVin 
	* @Description: TODO(通用配件三包查询) 
	* @param @param partCode
	* @param @return    设定文件 
	* @return List<TtAsWrRuleListPO>    返回类型 
	* @throws
	 */
	public List<TtAsWrRuleListPO> getPartGuaCommonListByVin (String partCode) {
		StringBuffer sql= new StringBuffer();
		sql.append("select c.PART_CODE as part_code, max(c.CLAIM_MONTH) as claim_month,MAX(c.CLAIM_MELIEAGE) as claim_melieage from \n" );
		sql.append(" TT_AS_WR_GAME a \n" );
		sql.append("left outer join TT_AS_WR_RULE b on a.rule_id = b.id\n" );
		sql.append("left outer join TT_AS_WR_RULE_LIST c on b.id=c.rule_id\n" );
		sql.append("where 1=1\n" );
		sql.append("and PART_CODE='"+partCode+"' \n");
		sql.append("and b.rule_code='"+Constant.COMMON_RULE+"' \n");
		sql.append("GROUP BY PART_CODE");
		List<TtAsWrRuleListPO> ls = select(TtAsWrRuleListPO.class,sql.toString(),null);
		return ls;
	}
	/**
	 * 
	* @Title: getOutGua 
	* @Description: TODO(里程数判断之前应该做过第几次保养:根据里程) 
	* @param @param mileage
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int getOutGuaMile(String mileage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select (min(free_times)-1) as free_times From tt_as_wr_qamaintain\n" );
		sql.append("where 1=1\n" );
		sql.append("and end_mileage >= "+mileage+" ");
		List<TtAsWrQamaintainPO> ls = select(TtAsWrQamaintainPO.class,sql.toString(),null);
		int times = 0;
		if (ls!=null&&ls.size()>0) {
		TtAsWrQamaintainPO tawqp = ls.get(0);
		times = tawqp.getFreeTimes();
		}
		return times<0?0:times;
	}
	/**
	 * 
	* @Title: getOutGuaTime 
	* @Description: TODO(里程数判断之前应该做过第几次保养:根据时间) 
	* @param @param duration
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int getOutGuaTime(int duration) {
		StringBuffer sql= new StringBuffer();
		sql.append("select (min(free_times)-1) as free_times From tt_as_wr_qamaintain\n" );
		sql.append("where 1=1\n" );
		sql.append("and max_days >= "+duration+" ");
		List<TtAsWrQamaintainPO> ls = select(TtAsWrQamaintainPO.class,sql.toString(),null);
		int times = 0;
		if (ls!=null&&ls.size()>0) {
		TtAsWrQamaintainPO tawqp = ls.get(0);
		times = tawqp.getFreeTimes();
		}
		return times<0?0:times;
	}

	/*****
	 * 
	 * 根据VIN获取车主姓名电话
	 * @param vin
	 * @return list
	 */
	public  List<Map<String, Object>> getVinUserName(String vin){
		StringBuffer sql = new StringBuffer();
		//sql.append(" select tc.main_phone,tc.ctm_name from tt_customer tc where tc.ctm_id in ( \n");
		//sql.append("select a.ctm_id from tt_dealer_actual_sales a where a.vehicle_id in \n");
		//sql.append("(select c.vehicle_id from tm_vehicle c where c.vin='"+vin+"')) \n");
		
		sql.append("SELECT tc.main_phone, tc.ctm_name,TC.OTHER_PHONE,TC.ADDRESS\n");
		sql.append("  FROM tt_customer tc\n" );
		sql.append("      ,tt_dealer_actual_sales a\n" );
		sql.append("    ,tm_vehicle c\n" );
		sql.append(" where tc.ctm_id=a.ctm_id\n" );
		sql.append("   and a.vehicle_id=c.vehicle_id\n" );
		sql.append("   and c.vin ='"+vin+"'");
		sql.append("  and  A.Is_Return="+Constant.IF_TYPE_NO+" ");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	//显示送修人信息
	public List<Map<String, Object>> getVinUserName2(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select o.deliverer ctm_name,o.deliverer_phone||' '||o.deliverer_mobile main_phone from tt_as_wr_application a ,tt_as_repair_order o\n");
		sql.append(" where a.ro_no = o.ro_no\n");  
		sql.append(" and a.id = "+id+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	/**********
	 * 
	 * 校验里程
	 * @param vin
	 * @return
	 */
	public  List<Map<String, Object>> getMileage(String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("select v.mileage from tm_vehicle v where v.vin='"+vin+"'");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	/*******
	 * 根据工单编码查询工单ID
	 * @param vin
	 * @return
	 */
	public  List<Map<String, Object>> getID(String roNo){
		StringBuffer sql = new StringBuffer();
		sql.append("select id from TT_AS_REPAIR_ORDER where ro_no='"+roNo+"'");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	//得到物料组品牌
	public  List<Map<String, Object>> getBrand(){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.group_id,t.group_code,t.group_name from tm_vhcl_material_group t where t.group_level=1");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	public  String getStr(List<Map<String, Object>> list){
		String str = "<select class=\"short_sel\" id=\"series\" name=\"series\" onchange=\"checkData();\" >";
		str+=" <option value=\"\">--请选择--</option>";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				str+=" <option value=\""+list.get(i).get("GROUP_ID")+"\">"+list.get(i).get("GROUP_CODE")+"--"+list.get(i).get("GROUP_NAME")+"</option>";
			}
		}
		return str;
	}
	
	public  List<Map<String, Object>> showSeriesList(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.group_id,t.group_code,t.group_name\n");
		sql.append("from tm_vhcl_material_group t\n");
		sql.append("where t.group_level =2\n");
		sql.append("and t.group_id in (\n");
		sql.append("select TVMG.group_id  from tm_vhcl_material_group TVMG   start with TVMG.Group_id="+id+"\n");
		sql.append("       connect by prior TVMG.group_id =TVMG.parent_group_id\n");
		sql.append(")"); 
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	/******
	 * 更新里程
	 */
	public void updateMileage(TmVehiclePO po,TmVehiclePO po1){
		factory.update(po, po1);
	}
	/*****
	 * 插入预授权申请单
	 * @param po
	 */
	public void insertTtAsWrForeapprovalitemPO(TtAsWrForeapprovalitemPO po){
		factory.insert(po);
	}
	public void insetFile(Long userId,Long appId,Long roId){
		StringBuilder sql= new StringBuilder();
		sql.append("insert into fs_fileupload(fjid,Fileurl,filename,status,create_date,create_by,ywzj,fileid,pjid)  \n");
		sql.append("SELECT F_GETID() fjid,a.fileurl,a.filename,a.status,sysdate create_date,"+userId+ " create_by,"+appId+" ywzj,a.fileid,a.pjid\n");
		sql.append("  from FS_FILEUPLOAD a WHERE 1=1  AND A.YWZJ='"+roId+"'\n");
		this.update(sql.toString(), null);
	}
	/*****
	 * 插入预授权申请明细
	 * @param po
	 */
	public void insertTtAsWrForeapprovalPO(TtAsWrForeapprovalPO po){
		factory.insert(po);
	}
	/*******
	 * 预授权审核状态更新
	 * @param fid
	 */
	public void updateTtAsWrForeapproval(String fid,String opintion){
		StringBuffer sql = new StringBuffer();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		sql.append("update tt_as_wr_foreapproval wf set wf.report_status="+Constant.RO_FORE_02+" ,WF.AUDIT_PERSON="+logonUser.getUserId()+",wf.opinion='"+opintion+"' where id="+fid);
		factory.update(sql.toString(), null);
	}
	/*******
	 * 预授权审核状态更新
	 * @param fid
	 * 添加一个是否同意费用标识后调用的此方法
	 */
	public void updateTtAsWrForeapproval2(String fid,String opintion,Integer accreditAmount){
		StringBuffer sql = new StringBuffer();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		sql.append("update tt_as_wr_foreapproval wf set wf.report_status="+Constant.RO_FORE_02+",wf.update_date=sysdate ,WF.AUDIT_PERSON="+logonUser.getUserId()+",wf.accredit_amount="+accreditAmount+",wf.opinion='"+opintion+"' where id="+fid);
		factory.update(sql.toString(), null);
	}
	/**
	 * 预授权审核状态更新
	 * @param id
	 * 更新维修工单中的一个字段,accredit_amount  0/1
	 */
	public void updateTtAsRepairOrder(String fid,Integer accreditAmount,AclUserBean user){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("update tt_as_repair_order ro\n" );
		sql.append("   set ro.accredit_amount = "+accreditAmount+",\n" );
		sql.append("       ro.update_by = "+user.getUserId()+",\n");
		sql.append("       ro.update_date = sysdate\n");
		sql.append(" where ro.ro_no = (select f.ro_no\n" );
		sql.append("                     from tt_as_wr_foreapproval f\n" );
		sql.append("                    where f.id = "+fid+")");
		factory.update(sql.toString(), null);
	}
	public void updateOrder(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("update TT_AS_REPAIR_ORDER ro set ro.forl_status="+Constant.RO_FORE_02+" where id="+id);
		factory.update(sql.toString(), null);
	}
	public void backTtAsWrForeapproval(String fid,String opinion){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		StringBuffer sql = new StringBuffer();
		sql.append("update tt_as_wr_foreapproval wf set wf.report_status="+Constant.RO_FORE_03+",wf.update_date = sysdate, wf.AUDIT_PERSON="+logonUser.getUserId()+",wf.opinion='"+opinion+"' where id="+fid);
		factory.update(sql.toString(), null);
	}
	public void backOrder(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("update TT_AS_REPAIR_ORDER ro set ro.forl_status="+Constant.RO_FORE_03+" where id="+id);
		factory.update(sql.toString(), null);
	}
	public void backOrderRono(String roNo){
		StringBuffer sql = new StringBuffer();
		sql.append("update TT_AS_REPAIR_ORDER ro set ro.forl_status="+Constant.RO_FORE_01+" where ro_no='"+roNo+"'");
		factory.update(sql.toString(), null);
	}
	public  List<Map<String, Object>> getGuranteeCode(String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tt_as_wr_rule where id=(SELECT rule_id FROM Tt_As_Wr_Game where\n");
		sql.append(" id=(SELECT v.claim_tactics_id FROM TM_VEHICLE v\n" );
		sql.append(" where v.vin='"+vin+"'))");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	
	//zhumingwei 2012-12-25
	public int getCount1(String id,String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as COUNT FROM TM_AREA_GROUP AG, TM_VHCL_MATERIAL_GROUP TG\n");
		sql.append("WHERE AG.MATERIAL_GROUP_ID = TG.GROUP_ID AND AG.AREA_ID in(\n" );
		sql.append("select ba.area_id from tm_pose_business_area pa, tm_business_area ba\n");
		sql.append("where pa.area_id = ba.area_id\n");
		sql.append("and pa.pose_id = (select dd.pose_id from tr_user_pose dd where dd.user_id = "+id+"))\n");
		sql.append("and tg.group_id=(select a.series_id from tm_vehicle  a  where a.vin='"+vin+"')");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return ((BigDecimal)list.get(0).get("COUNT")).intValue();
	}
	
	/***
	 * 根据工单ID 查询出预授权申请单的审核内容和预授权内容
	 */
	public  List<Map<String, Object>> getOpinion(String fid){
		StringBuffer sql = new StringBuffer();
		sql.append("select wf.opinion,wf.remark from TT_AS_WR_FOREAPPROVAL wf where WF.id="+fid);
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}//  select * from TT_AS_WR_APPLICATION where ro_no='N8980100RO090100291'
	public  List<Map<String, Object>> getApplication(String roNo){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from TT_AS_WR_APPLICATION where ro_no='"+roNo+"'");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	  /**
	   * 维修历史记录
	   * @param vin 车辆VIN码
	   * @param claimTypes 索赔单类型集合（如：TYPE1,TYPE2...）
	   */
	  public List<Map<String,Object>> maintaimHistory(String vin,String claimTypes){ //YH 2010.11.29
 		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb= new StringBuffer();
	    sb.append("select o.id,\n" );
	    sb.append("       o.ro_no,\n" );
	    sb.append("       o.vin,\n" );
	    sb.append("       tm.dealer_shortname as DEALER_NAME,\n" );
	    sb.append("       o.owner_name,\n" );
	    sb.append("       o.in_mileage,\n" );
	    sb.append("       a.status as app_status,\n" );
	    sb.append("       o.deliverer,\n" );
	    sb.append("       o.repair_type_code,\n" );
	    sb.append("       to_char(o.ro_create_date, 'yyyy-mm-dd hh24:mm') ro_create_date,\n" );
	    sb.append("       to_char(o.for_balance_time, 'yyyy-mm-dd hh24:MM') for_balance_time,\n" );
	    sb.append("        a.trouble_reason,\n" );
	    sb.append("       a.trouble_desc AS TROUBLE_DESC,\n" );
	    sb.append("       o.repair_method,\n" );
	    sb.append("       a.claim_no,\n" );
	    sb.append("       to_char(a.report_date, 'yyyy-mm-dd hh24:mm') report_date,\n" );
	    sb.append("       a.reporter,o.ro_status as status\n" );
	    sb.append("  from tt_as_repair_order o, tt_as_wr_application a, tm_dealer tm\n" );
	    sb.append(" where a.ro_no = o.ro_no(+)\n" );
	    sb.append("   and tm.dealer_id = o.dealer_id\n" );
	    sb.append("   and o.is_customer_in_asc = 1\n" );
	    sb.append("   and o.ro_status = 11591002\n");
	    DaoFactory.getsql(sb, "o.vin", vin, 1);
	   //该段代码为屏蔽服务站不看到的工单
	    if (null!=logonUser.getDealerId()) {
		   sb.append(" and o.ro_no not in (select v.ro_no from tt_as_wr_repair_claim v where v.dealer_id='"+logonUser.getDealerId()+"')");
		}
	   
	    sb.append("order by o.ro_create_date desc"); 
	    return this.pageQuery(sb.toString(), null, this.getFunName());
	  }
	  
	  /**
	   * 维修历史记录   艾春9.22 添加维修日期/配件信息/工时信息/问题描述/问题原因/
	   * @param vin 车辆VIN码
	   * @param claimTypes 索赔单类型集合（如：TYPE1,TYPE2...）
	   */
	  public List<Map<String,Object>> maintaimHistoryByVIN(String vin){ 
	    StringBuilder sql= new StringBuilder();
	    sql.append("select o.id,o.ro_no ,o.vin,d.dealer_name,o.owner_name,o.in_mileage,o.deliverer,o.repair_type_code,\n");
	    sql.append("       to_char(o.ro_create_date,'yyyy-mm-dd hh24:mm') ro_create_date,to_char(o.for_balance_time,'yyyy-mm-dd hh24:MM') for_balance_time,\n");
	    sql.append("       o.trouble_reason,  o.trouble_descriptions AS TROUBLE_DESC,o.repair_method,\n");
	    sql.append("       a.claim_no,to_char(a.report_date,'yyyy-mm-dd hh24:mm')report_date,  a.reporter,a.status,c.code_desc status_name,\n");
	    sql.append("       P.PART_NO PART_CODE, P.PART_NAME, L.WR_LABOURCODE,L.WR_LABOURNAME,O.LABOUR_AMOUNT,O.REPAIR_PART_AMOUNT PART_AMOUNT, ");
	    sql.append("       o.repair_amount,NVL(A.BALANCE_PART_AMOUNT,0) BALANCE_PART_AMOUNT,NVL(A.BALANCE_LABOUR_AMOUNT,0) BALANCE_LABOUR_AMOUNT,NVL(A.BALANCE_AMOUNT,0) BALANCE_AMOUNT\n");
	    sql.append("from tt_as_repair_order o\n");
	    sql.append("left join tt_as_wr_application a on a.ro_no=o.ro_no\n");
	    sql.append("LEFT JOIN TT_AS_RO_REPAIR_PART P ON O.ID = P.RO_ID\n");
	    sql.append("LEFT JOIN TT_AS_RO_LABOUR L ON O.ID = L.RO_ID AND P.LABOUR_CODE = L.WR_LABOURCODE\n");
	    sql.append("left join TM_DEALER d on d.dealer_id=o.dealer_id\n");
	    sql.append(" left join tc_code c on c.code_id = o.RO_STATUS \n");
	    sql.append(" where 1=1\n");
	    sql.append(" and o.ro_status=11591002\n");
	    sql.append(" and o.vin= ?\n");
	    sql.append("order by o.ro_create_date desc"); 

	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(CommonUtils.checkNull(vin));
	    return this.pageQuery(sql.toString(), paramList, this.getFunName());
	  }
	  
	  /**
	   * 维修历史记录
	   * @param vin 车辆VIN码
	   * @param claimTypes 索赔单类型集合（如：TYPE1,TYPE2...）
	   */
	  public List<Map<String,Object>> maintaimHistoryAlert(String vin,String claimTypes){ //YH 2010.11.29
		    ActionContext act = ActionContext.getContext();
		    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    StringBuilder sql= new StringBuilder();
	    sql.append("WITH t1 As (Select * From TT_AS_WR_APPLICATION a1 Where A1.STATUS IN (10791004,10791005,10791006,10791007,10791008)\n");
	    if(logonUser.getUserType().equals(Constant.SYS_USER_SGM)){
	    	 sql.append("AND A1.CLAIM_TYPE IN (10661001,10661007,10661009)\n" );
	    }else{
	    	sql.append("AND A1.CLAIM_TYPE IN (10661001,10661009)\n" );
	    }
	    
	    sql.append("AND A1.VIN = ? and (a1.is_return<>10041001 or a1.is_return is null) )\n" );
	    sql.append(",TMP_WRLABINFO AS\n" );
	    sql.append("(select distinct wr_labourcode, wr_labourname,ID from TT_AS_WR_LABOURITEM t2 Where Exists (Select 1 From t1 Where t1.Id  =t2.Id ))\n" );
	    sql.append("SELECT A.AUDITING_DATE,A.ID,A.CLAIM_NO,A.CLAIM_TYPE,TO_CHAR(A.RO_STARTDATE,'YYYY-MM-DD') RO_STARTDATE, A.IN_MILEAGE,(SELECT max(CODE_DESC) from tc_code where CODE_ID = A.STATUS) STATUS\n" );
	    sql.append(",D.DEALER_ID,D.DEALER_CODE,D.DEALER_NAME\n" );
	    sql.append(",WW.WR_LABOURCODE,WW.WR_LABOURNAME\n" );
	    sql.append(",B.PART_CODE,B.PART_NAME,B.IS_AGREE\n" );
	    sql.append(",E.NAME,l.trouble_code_name,l.labour_amount LABOUR_AMOUNT, b.amount PART_AMOUNT,A.REPAIR_TOTAL,A.TROUBLE_DESC,A.TROUBLE_REASON,REPAIR_METHOD\n" );
	    sql.append(",l.trouble_type,l.balance_amount BALANCE_LABOUR_AMOUNT,b.balance_amount BALANCE_PART_AMOUNT,A.BALANCE_AMOUNT,B.AUTH_REMARK\n");
	    sql.append("FROM t1 A\n" );
	    sql.append(",TT_AS_WR_PARTSITEM B\n" );
	    sql.append(",TT_AS_WR_LABOURITEM L\n" );
	    sql.append(",TMP_WRLABINFO WW\n" );
	    sql.append(",TM_DEALER D\n" );
	    sql.append(",TC_USER E\n" );
	    sql.append(",TT_AS_WR_APPAUTHITEM F\n" );
	    sql.append("WHERE 1=1\n" );
	    sql.append("and f.approval_date in (select max(wa.approval_date) from tt_as_wr_appauthitem wa where wa.id=a.id and wa.approval_result in (10791004,10791005,10791006) and wa.approval_person not like '%结算审核%' group by wa.id)\n");
	    sql.append("AND A.ID = B.ID\n" );
	    sql.append("AND B.WR_LABOURCODE = WW.wr_labourcode\n" );
	    sql.append("and l.id=b.id and l.wr_labourcode=b.wr_labourcode\n");
	    sql.append("AND A.ID=F.ID AND A.ID = WW.ID \n" );
	    sql.append("AND F.CREATE_BY = E.USER_ID(+)\n" );
	    sql.append("AND A.DEALER_ID = D.DEALER_ID(+)\n" );
	    sql.append("AND F.APPROVAL_RESULT  in (10791004,10791005,10791006)\n" );
	    sql.append("ORDER BY A.RO_STARTDATE DESC,A.ID DESC");
	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(CommonUtils.checkNull(vin));
	    
	    return this.pageQuery(sql.toString(), paramList, this.getFunName());
	  }
	  
     /**
      * 授权历史记录
      * @param vin 车辆VIN码
      * @param expStatus 不统计的索赔单状态集合（如：STATUS1,STATUS2...）
      */
	     public List<Map<String,Object>> auditingHistory(String vin,String expStatus){
	         
	    	 StringBuffer sql= new StringBuffer();
	    	 sql.append("select * from (SELECT A.ID,\n" );
	    	 sql.append("       A.CLAIM_NO,\n" );
	    	 sql.append("       D.DEALER_CODE,\n" );
	    	 sql.append("       D.DEALER_NAME,\n" );
	    	 sql.append("       E.NAME AUTH_NAME,\n" );
	    	 sql.append("       TO_CHAR(F.approval_date, 'YYYY-MM-DD') AUTH_DATE,\n" );
	    	 sql.append("       A.STATUS,\n" );
	    	 sql.append("       F.REMARK,\n" );
	    	 sql.append("       C.WR_LABOURCODE,\n" );
	    	 sql.append("       C.WR_LABOURNAME,\n" );
	    	 sql.append("       B.PART_CODE,\n" );
	    	 sql.append("       B.PART_NAME,\n" );
	    	 sql.append("       A.CLAIM_TYPE,\n" );
	    	 sql.append("case\n" );
	    	 sql.append("             when f.approval_result=10791005 or f.approval_result=10791006\n" );
	    	 sql.append("             then 10041002\n" );
	    	 sql.append("             else 10041001 end is_agree,");

	    	 sql.append("       f.approval_level_code,\n" );
	    	 sql.append("       f.approval_person,\n" );
	    	 //添加工单类型标识 add by tanv 2012-11-19
	    	 sql.append("  '索赔授权工单' ORDER_TYPE");
	    	 sql.append("  FROM TT_AS_WR_APPLICATION A,\n" );
	    	 sql.append("       TT_AS_WR_PARTSITEM   B,\n" );
	    	 sql.append("       TT_AS_WR_LABOURITEM  C,\n" );
	    	 sql.append("       TM_DEALER            D,\n" );
	    	 sql.append("       TC_USER              E,\n" );
	    	 sql.append("       TT_AS_WR_APPAUTHITEM F\n" );
	    	 sql.append(" WHERE 1 = 1\n" );
	    	 sql.append("   AND A.ID = B.ID\n" );
	    	 sql.append("   AND A.ID = C.ID\n" );
	    	 sql.append("   AND A.ID = F.ID\n" );
	    	 sql.append("   AND A.DEALER_ID = D.DEALER_ID(+)\n" );
	    	 sql.append("   AND F.CREATE_BY = E.USER_ID\n" );
	    	 sql.append("   AND B.WR_LABOURCODE = C.WR_LABOURCODE\n" );
	    	 sql.append("AND A.STATUS NOT IN (10791001)\n" );
	    	 sql.append("   AND f.Approval_Person like '%授权审核%'\n" );
	    	 sql.append("   AND F.APPROVAL_RESULT >= 10791004\n" );
	    	 sql.append("   AND A.VIN = ?\n" );
	    	 sql.append("   and f.approval_level_code != '100'  and (a.is_return<>10041001 or a.is_return is null) \n" );
	    	 sql.append("\n" );
	    	 sql.append("\n" );
	    	 sql.append(" union all\n" );
	    	 
	    	 //添加预授权工单展示 add by tanv 2012-11-20
	    	 sql.append("SELECT A.ID,\n");
	    	 sql.append("       A.FO_NO,\n"); 
	    	 sql.append("       B.DEALER_CODE,\n");  
	    	 sql.append("       B.DEALER_NAME,\n"); 
	    	 sql.append("       C.NAME AUTH_NAME,\n");  
	    	 sql.append("       TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') AUTH_DATE,\n"); 
	    	 sql.append("       A.REPORT_STATUS STATUS,\n"); 
	    	 sql.append("       A.OPINION REMARK,\n"); 
	    	 sql.append("       '' WR_LABOURCODE,\n");  
	    	 sql.append("       '' WR_LABOURNAME,\n");   
	    	 sql.append("       '' PART_CODE,\n");  
	    	 sql.append("       '' PART_NAME,\n"); 
	    	 sql.append("       a.approval_type CLAIM_TYPE,\n");
	    	 sql.append("       1 IS_AGREE,\n"); 
	    	 sql.append("       '' APPROVAL_LEVEL_CODE,\n"); 
	    	 sql.append("       '' APPROVAL_PERSON,\n"); 
	    	 sql.append("      (CASE a.approval_type WHEN  11441004 THEN '保养预授权工单' WHEN 11441002 THEN  '外出维修预授权工单' END) ORDER_TYPE \n");
	    	 sql.append("  FROM TT_AS_WR_FOREAPPROVAL A,\n");
	    	 sql.append("       TM_DEALER             B,\n"); 
	    	 sql.append("       TC_USER               C \n");
	    	 sql.append(" WHERE A.DEALER_ID = B.DEALER_ID \n");
	    	 sql.append("   AND A.AUDIT_PERSON = C.USER_ID \n");
	    	 sql.append("   AND A.APPROVAL_TYPE IN (11441002,11441004) \n");
	    	 sql.append("   AND A.VIN = ? \n");
	    	 //添加预授权工单展示 add by tanv end 2012-11-20
	    	 
	    	 sql.append("\n" );
	    	 sql.append("\n" );
	    	 sql.append(" union all\n" );
	    	 sql.append(" SELECT A.ID,\n" );
	    	 sql.append("       A.CLAIM_NO,\n" );
	    	 sql.append("       D.DEALER_CODE,\n" );
	    	 sql.append("       D.DEALER_NAME,\n" );
	    	 sql.append("       E.NAME AUTH_NAME,\n" );
	    	 sql.append("       TO_CHAR(F.approval_date, 'YYYY-MM-DD') AUTH_DATE,\n" );
	    	 sql.append("       A.STATUS,\n" );
	    	 sql.append("       F.REMARK,\n" );
	    	 sql.append("       C.WR_LABOURCODE,\n" );
	    	 sql.append("       C.WR_LABOURNAME,\n" );
	    	 sql.append("       B.PART_CODE,\n" );
	    	 sql.append("       B.PART_NAME,\n" );
	    	 sql.append("       A.CLAIM_TYPE,\n" );
	    	 sql.append("case\n" );
	    	 sql.append("             when f.approval_result=10791005 or f.approval_result=10791006\n" );
	    	 sql.append("             then 10041002\n" );
	    	 sql.append("             else 10041001 end is_agree,");
	    	 sql.append("       f.approval_level_code,\n" );
	    	 sql.append("       f.approval_person,\n" );
	    	 //添加工单类型标识 add by tanv 2012-11-19
	    	 sql.append("  '索赔授权工单' ORDER_TYPE");
	    	 sql.append("  FROM TT_AS_WR_APPLICATION_BACKUP A,\n" );
	    	 sql.append("       TT_AS_WR_PARTSITEM   B,\n" );
	    	 sql.append("       TT_AS_WR_LABOURITEM  C,\n" );
	    	 sql.append("       TM_DEALER            D,\n" );
	    	 sql.append("       TC_USER              E,\n" );
	    	 sql.append("       TT_AS_WR_APPAUTHITEM F\n" );
	    	 sql.append(" WHERE 1 = 1\n" );
	    	 sql.append("   AND A.ID = B.ID\n" );
	    	 sql.append("   AND A.ID = C.ID\n" );
	    	 sql.append("   AND A.ID = F.ID\n" );
	    	 sql.append("   AND A.DEALER_ID = D.DEALER_ID(+)\n" );
	    	 sql.append("   AND F.CREATE_BY = E.USER_ID\n" );
	    	 sql.append("   AND B.WR_LABOURCODE = C.WR_LABOURCODE\n" );
	    	 sql.append("AND A.STATUS NOT IN (10791001)\n" );
	    	 sql.append("   AND f.Approval_Person like '%授权审核%'\n" );
	    	 sql.append("   AND F.APPROVAL_RESULT >= 10791004\n" );
	    	 sql.append("   AND A.VIN = ?\n" );
	    	 sql.append("   and f.approval_level_code != '100'  and (a.is_return<>10041001 or a.is_return is null)\n" );
	    	 sql.append(") order by AUTH_DATE desc");

	        List<Object> paramList = new ArrayList<Object>();
	        paramList.add(CommonUtils.checkNull(vin));
	        paramList.add(CommonUtils.checkNull(vin));
	        paramList.add(CommonUtils.checkNull(vin));
	      
	        return this.pageQuery(sql.toString(), paramList, this.getFunName());
	     }
	  
	  /**
	   * 车型免费保养历史记录
	   * 注意：1、现在按时间顺序做取巧，通过工单创建的时间排序，取得序号做为保养次数
	   *       2、工单类型：保养
	   *          当工单做过索赔则使用索赔单的结算金额做保养费用，
	   *          如果工单未做过索赔则费用显示为"自费"
	   *       3、工单类型：服务活动（保养）
	   *          使用服务活动金额做为保养金额
	   * @param vin 车辆VIN码
	   */
	  public List<Map<String,Object>> freeMaintainHistory(String vin){
		  StringBuffer sql= new StringBuffer();
		  
		  sql.append("select  tm.dealer_code ,\n");
		  sql.append("        tm.dealer_name,\n");
		  sql.append("        o.ro_no,\n");
		  sql.append("        to_char(o.ro_create_date,'yyyy-mm-dd') ro_create_date,\n");
		  sql.append("        o.repair_type_code,\n");
		  sql.append("        o.ro_status,\n");
		  sql.append("        o.in_mileage,\n");
		  sql.append("        o.free_times,\n");
		  sql.append("        a.claim_no,to_char(a.report_date,'yyyy-mm-dd') report_date,\n");
		  sql.append("        a.status\n");
		  sql.append("from  Tt_As_Repair_Order o,tt_as_wr_application a,tm_dealer tm\n");
		  sql.append("where   o.ro_no = a.ro_no(+)   and tm.dealer_id=o.dealer_id\n");
		  sql.append(" and  o.repair_type_code=11441004\n");
		  sql.append("and o.ro_status=11591002\n");
		  sql.append("  and o.vin ='"+vin+"'\n");
		  sql.append(" union all\n");
		  sql.append(" select  tm.dealer_code ,\n");
		  sql.append("         tm.dealer_name,\n");
		  sql.append("        o.ro_no,to_char(o.ro_create_date,'yyyy-mm-dd') ro_create_date,\n");
		  sql.append("        o.repair_type_code,o.ro_status,o.in_mileage,o.free_times,\n");
		  sql.append("        a.claim_no,to_char(a.report_date,'yyyy-mm-dd') report_date,a.status\n");
		  sql.append("from  Tt_As_Repair_Order o,tt_as_wr_application a,tm_dealer tm\n");
		  sql.append("where  o.ro_no = a.ro_no(+) and tm.dealer_id=o.dealer_id\n");
		  sql.append("and o.ro_status=11591002\n");
		  sql.append("  and o.vin ='"+vin+"'\n");
		  sql.append(" and o.id\n");
		  sql.append(" in (select p.ro_id  from Tt_As_Ro_Repair_Part p\n");
		  sql.append(" where p.repairtypecode is not null  and  p.repairtypecode in (93331002,93331003))"); 




		
		  
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	  }
	  /**
	   * 
	  * @Title: getPartAddPer 
	  * @Description: TODO(取配件加价率) 
	  * @param @param dealerId
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws
	   */
	  public String getPartAddPer(String dealerId) {
			  StringBuffer sql= new StringBuffer();
			  sql.append("select parameter_value  from tm_down_parameter\n" );
			  sql.append("where 1=1\n" );
			  sql.append("and parameter_code="+Constant.CLAIM_BASIC_PARAMETER_09+"\n" );
			  sql.append("and dealer_id="+dealerId+"");
			  TmDownParameterPO tdp = new TmDownParameterPO();
			  List<TmDownParameterPO> ls = dao.select(TmDownParameterPO.class,sql.toString(), null);
			  if (ls!=null) {
				  if (ls.size()>0) {
					  tdp = ls.get(0);
				  }
			  }
			  return tdp.getParameterValue();
	  }
	  
	  /**
	   * 
	  * @Title: getPartPrice 
	  * @Description: TODO(通过partCode,supplierCode 取得配件) 
	  * @param @return    设定文件 
	  * @return TmPtPartBasePO    返回类型 
	  * @throws
	   */
	  public TmPtPartBasePO getPartPrice(Map<String,String> map) {
		  StringBuffer sql= new StringBuffer();
		  sql.append("select claim_price from TM_PT_PART_BASE a\n" );
		  sql.append("left outer join TM_PT_PART_SUP_RELATION b on a.part_id=b.order_id\n" );
		  sql.append("left outer join TM_PT_SUPPLIER c on b.supplier_id=c.supplier_id\n" );
		  sql.append("where 1=1\n" );
		  sql.append("and part_code='"+map.get("partCode")+"'\n" );
		  if (Utility.testString(map.get("supplierCode"))) {
			  sql.append("and supplier_code='"+map.get("supplierCode")+"'");
		  }
		  TmPtPartBasePO tp = new TmPtPartBasePO();
		  List<TmPtPartBasePO> ls = select(TmPtPartBasePO.class,sql.toString(),null);
		  if (ls!=null) {
			  if (ls.size()>0) {
				  tp = ls.get(0);
			  }
		  }
		  return tp;
	  }
	  
	  /**
	   * 查询对应索赔单要打印配件明细
	   * @param claimId 索赔单ID
	   * @return
	   */
	  public List<Map<String, Object>> queryClaimPart(Long claimId){
		  StringBuilder sql= new StringBuilder();
		  
		  TcCodePO po = new TcCodePO();
		  po.setType(Constant.chana+"");
		  TcCodePO poValue = (TcCodePO)this.select(po).get(0);
		  String codeId = poValue.getCodeId();
		  if(codeId==null){
			  codeId="0";
		  }
		  if(Integer.parseInt(codeId)==Constant.chana_jc){
			  sql.append("SELECT B.CLAIM_NO, B.VIN, B.YIELDLY, B.MODEL_CODE, B.ENGINE_NO, B.IN_MILEAGE, B.TROUBLE_DESC,\n" );
			  sql.append("       C.QUANTITY BALANCE_QUANTITY,C.DOWN_PART_CODE, C.DOWN_PART_NAME, T.TROUBLE_CODE_NAME AS REMARK, TO_CHAR(D.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE,\n" );
			  sql.append("       E.DEALER_CODE, E.DEALER_NAME, F.DELIVERER, F.DELIVERER_PHONE, C.DOWN_PRODUCT_NAME DC_NAME,c.RESPONSIBILITY_TYPE,\n" );
			  sql.append("       to_char(B.AUDITING_DATE,'yyyy-MM-dd') AUDITING_DATE,T.trouble_type\n");
			  sql.append("FROM TT_AS_WR_APPLICATION B, TT_AS_WR_PARTSITEM C,\n" );
			  sql.append("TM_VEHICLE D, TM_DEALER E, TT_AS_REPAIR_ORDER F,\n" );
			  sql.append("TM_PT_PART_BASE G,Tt_As_Wr_Labouritem  T\n" );
			  sql.append("WHERE 1=1\n" );
			  sql.append("  AND B.ID = C.ID AND T.ID=B.ID and c.wr_labourcode = t.wr_labourcode\n" );
			  sql.append("  AND B.DEALER_ID = E.DEALER_ID\n" ); 
			  sql.append("  AND B.RO_NO = F.RO_NO\n" );
			  sql.append("  AND B.VIN = D.VIN\n" );
			  sql.append("  AND C.PART_CODE = G.PART_CODE(+)\n" );
			  sql.append("  AND g.is_return=1 \n");
			  sql.append("  AND B.ID = ?");
		  }else{
			  sql.append("SELECT B.CLAIM_NO, B.VIN, B.YIELDLY, B.MODEL_CODE, B.ENGINE_NO, B.IN_MILEAGE, B.TROUBLE_DESC,\n" );
			  sql.append("       C.QUANTITY BALANCE_QUANTITY,C.DOWN_PART_CODE, C.DOWN_PART_NAME, C.REMARK, TO_CHAR(D.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE,\n" );
			  sql.append("       E.DEALER_CODE, E.DEALER_NAME, F.DELIVERER, F.DELIVERER_PHONE, C.DOWN_PRODUCT_NAME DC_NAME,c.RESPONSIBILITY_TYPE,\n" );
			  sql.append("       to_char(B.AUDITING_DATE,'yyyy-MM-dd') AUDITING_DATE,\n");
			  sql.append("       (select aa.is_return from tm_pt_part_type aa where aa.id=g.part_type_id) as is_return\n");
			  sql.append("FROM TT_AS_WR_APPLICATION B, TT_AS_WR_PARTSITEM C,\n" );
			  sql.append("TM_VEHICLE D, TM_DEALER E, TT_AS_REPAIR_ORDER F,\n" );
			  sql.append("TM_PT_PART_BASE G\n" );
			  sql.append("WHERE 1=1\n" );
			  sql.append("  AND B.ID = C.ID\n" );
			  sql.append("  AND B.DEALER_ID = E.DEALER_ID\n" ); 
			  sql.append("  AND B.RO_NO = F.RO_NO\n" );
			  sql.append("  AND B.VIN = D.VIN\n" );
			  sql.append("  AND C.PART_CODE = G.PART_CODE(+)\n" );
			  //sql.append("  AND g.is_return=1 \n");
			  sql.append("  AND B.ID = ?");
		  }
		  
		  List<Object> paramList = new ArrayList<Object>();
		  paramList.add(claimId);
		  
		  return this.pageQuery(sql.toString(), paramList, this.getFunName());
	  }
	  
	  //查询售后车型组保养费用
	  public List<Map<String, Object>> viewFree(String vin){
		  StringBuilder sql= new StringBuilder();
		  sql.append("SELECT WG.FREE FROM TM_VEHICLE  TV,TT_AS_WR_MODEL_ITEM  WM,TT_AS_WR_MODEL_GROUP  WG WHERE TV.PACKAGE_ID = WM.MODEL_ID AND WM.WRGROUP_ID = WG.WRGROUP_ID AND TV.VIN='"+vin+"'");
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	  }
	  
	  //查询系统保养次数
	  public List<Map<String, Object>> viewFreeTime1(String vin){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select tv.free_times,tv.mileage from tm_vehicle tv where tv.vin='"+vin+"'");
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	  }
	  //查询工单的保养次数
	  public List<Map<String, Object>> viewFreeTimeDoNo(String ro_no){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select p.* from tt_as_repair_order p where p.ro_no='"+ro_no+"'");
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	  }
	  //查询工单信息
	  public List<Map<String, Object>> viewOrderReapir(String id){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select p.* from tt_as_repair_order p where p.id="+id+"");
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	  }
	  public List<TtDealerActualSalesPO> checkUnSaleVechile(String vin){
		  StringBuilder sql= new StringBuilder();
		  sql.append("SELECT A.*\n" );
		  sql.append("FROM TT_DEALER_ACTUAL_SALES A,TM_VEHICLE B\n" );
		  sql.append("WHERE 1=1\n" );
		  sql.append("AND A.VEHICLE_ID = B.VEHICLE_ID\n" );
		  sql.append(" and a.is_return="+Constant.IF_TYPE_NO+"\n");
		  sql.append("AND B.VIN = ?");
		  
		  List<Object> param = new ArrayList<Object>();
		  param.add(CommonUtils.checkNull(vin));

		  return this.select(TtDealerActualSalesPO.class, sql.toString(), param);
	  }
	  public List<Map<String, Object>> viewOutRepair(String ro_no){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select p.* from   tt_as_wr_outrepair p where p.ro_no='"+ro_no+"'");
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	  }
	  
	  
	  
	  public List<Map<String,Object>> getMaintainStateSet(String vin){
		  StringBuilder sql= new StringBuilder("\n");
		  sql.append("select distinct tawq.start_mileage,\n");
		  sql.append("                tawq.end_mileage,\n");  
		  sql.append("                tawq.min_days,\n");  
		  sql.append("                tawq.max_days,\n");  
		  sql.append("                tv.vin,\n");  
		  sql.append("                tv.license_no,\n");  
		  sql.append("                tb.AREA_NAME,\n");  
		  sql.append("                tv.engine_no,\n");  
		  sql.append("                tv.life_cycle,\n");  
		  sql.append("                nvl(tv.free_times,0) free_times,\n");  
		  sql.append("                tawg.game_code,\n");  
		  sql.append("                tawg.game_name,\n");  
		  sql.append("                vmg.MODEL_CODE,\n");  
		  sql.append("                vmg.MODEL_NAME,\n");  
		  sql.append("                g.wrgroup_code,\n");  
		  sql.append("                g.wrgroup_name,\n");  
		  sql.append("                to_char(tv.purchased_date,'yyyy-MM-dd hh24:mi') sales_date\n");  
		  sql.append("  from tt_as_wr_qamaintain    tawq,\n");  
		  sql.append("       tm_vehicle             tv,\n");  
		  sql.append("       tm_business_area             tb,\n");  
		  sql.append("       tt_as_wr_game          tawg,\n");  
		  sql.append("       tt_dealer_actual_sales tdas,\n");  
		  sql.append("       tt_as_wr_model_group   g,\n");  
		  sql.append("       tt_as_wr_model_item    i,\n");  
		  sql.append("       vw_material_group      vmg\n");  
		  sql.append(" where tv.claim_tactics_id = tawg.id(+)\n");  
		  sql.append("  and tv.yieldly(+) = tb.AREA_ID ");
		  sql.append("   and i.model_id = tv.package_id\n");  
		  sql.append("   and vmg.MODEL_ID = tv.model_id\n");  
		  sql.append("   and i.wrgroup_id = g.wrgroup_id\n");  
		  sql.append("   and tv.vehicle_id = tdas.vehicle_id(+)\n");  
		  sql.append("   and g.wrgroup_type = ").append(Constant.WR_MODEL_GROUP_TYPE_01).append("\n");  
		  sql.append("   and tawq.free_times = (nvl(tv.free_times,0) + 1)\n");  
		  sql.append("   and tv.vin = '").append(vin).append("'\n");
		  sql.append("  and tdas.is_return="+Constant.IF_TYPE_NO+"\n");
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	  }
	  
	  public List<Map<String,Object>> getMaintainStateSet_tow(String vin,String ENGINE_NO){ //YH 2011.9.20
		  StringBuilder sql= new StringBuilder("\n");
		  sql.append("select distinct tawq.start_mileage,\n");
		  sql.append("                tawq.end_mileage,\n");  
		  sql.append("                tawq.min_days,\n");  
		  sql.append("                tawq.max_days,\n");  
		  sql.append("                tv.vin,\n");  
		  sql.append("                tv.license_no,\n");  
		  sql.append("                tv.yieldly,\n");  
		  sql.append("                tv.engine_no,\n");  
		  sql.append("                tv.life_cycle,\n");  
		  sql.append("                nvl(tv.free_times,0) free_times,\n");  
		  sql.append("                tawg.game_code,\n");  
		  sql.append("                tawg.game_name,\n");  
		  sql.append("                vmg.MODEL_CODE,\n");  
		  sql.append("                vmg.MODEL_NAME,\n");  
		  sql.append("                g.wrgroup_code,\n");  
		  sql.append("                g.wrgroup_name,\n");  
		  sql.append("                to_char(tv.purchased_date,'yyyy-MM-dd') sales_date\n");  
		  sql.append("  from tt_as_wr_qamaintain    tawq,\n");  
		  sql.append("       tm_vehicle             tv,\n");  
		  sql.append("       tt_as_wr_game          tawg,\n");  
		  sql.append("       tt_dealer_actual_sales tdas,\n");  
		  sql.append("       tt_as_wr_model_group   g,\n");  
		  sql.append("       tt_as_wr_model_item    i,\n");  
		  sql.append("       vw_material_group      vmg\n");  
		  sql.append(" where tv.claim_tactics_id = tawg.id(+)\n");  
		  sql.append("   and i.model_id = tv.package_id\n");  
		  sql.append("   and vmg.MODEL_ID = tv.model_id\n");  
		  sql.append("   and i.wrgroup_id = g.wrgroup_id\n");  
		  sql.append("   and tv.vehicle_id = tdas.vehicle_id(+)\n");  
		  sql.append("   and g.wrgroup_type = ").append(Constant.WR_MODEL_GROUP_TYPE_01).append("\n");  
		  sql.append("   and tawq.free_times = (nvl(tv.free_times,0) + 1)\n");  
		  sql.append("   and tv.vin = '").append(vin).append("'\n");
		  sql.append("   and tv.engine_no = '").append(ENGINE_NO).append("'\n");
		  sql.append(" and tdas.is_return="+Constant.IF_TYPE_NO+"\n");
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	  }
	  
	  public List<NewPartBean> getNewPartBean(String code,String vin){
		  StringBuilder sql= new StringBuilder("\n");
		  sql.append("select t.*,pt.parttype_code type_code,pt.parttype_name type_name, l.claim_month, l.claim_melieage\n");
		  sql.append("    from tm_pt_part_base t,tm_pt_part_type pt, TT_AS_WR_RULE_LIST l,tt_as_wr_game g,tm_vehicle v\n");  
		  sql.append("   where t.part_code = l.part_code\n");
		  sql.append("     and t.part_type_id = pt.id(+)\n");
		  sql.append("     and v.vin = '"+vin+"'\n");  
		  sql.append("     and v.claim_tactics_id = g.id\n");  
		  sql.append("     and g.rule_id = l.rule_id\n");  
		  sql.append("     and t.part_code ='"+code+"'\n");
		  return this.select(NewPartBean.class, sql.toString(), null);
	  }
	  
	  public List<NewPartBean> getNewPartBean2(String code){
		  StringBuilder sql= new StringBuilder("\n");
		  sql.append("select l.part_code,\n");
		  sql.append("       l.part_name,\n");  
		  sql.append("       l.claim_month,\n");  
		  sql.append("       l.claim_melieage,\n");  
		  sql.append("       l.rule_id,\n");  
		  sql.append("       t.parttype_name type_name,\n");  
		  sql.append("       t.parttype_code type_code\n");  
		  sql.append("  from tt_as_wr_rule_list l, tt_as_wr_RULE R ,tm_pt_part_base b,tm_pt_part_type t,tc_code c\n");  
		  sql.append(" where b.part_code = l.part_code\n");  
		  sql.append("   and b.part_type_id = t.id(+)\n");  
		  sql.append("   AND C.CODE_DESC = R.RULE_CODE\n");  
		  sql.append("   and l.rule_id = R.ID\n");  
		  sql.append("   and l.part_code = '"+code+"'\n");  
		  sql.append("   and c.code_id = 13811001\n");
		  return this.select(NewPartBean.class, sql.toString(), null);
	  }
	  
    /**
     * 查询结算单使用
     * ADD 2010-10-24  只为工单查询使用
     * @param map 查询条件
     * @param pageSize 
     * @param curPage
     * @return PageResult<TtAsRepairOrderExtPO>
     */
	public PageResult<TtAsRepairOrderExtPO> queryRepairOrderNew(
			Map<String, String> map, int pageSize, int curPage) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select decode(a.ro_no,null,0,1)  as in_claim,r.*\n" );
//		sqlStr.append(" (select y.PART_TYPE_ID from tt_as_ro_repair_part x,Tm_Pt_Part_Base y where x.RO_ID=r.ID AND x.PART_NO=y.PART_CODEE) as PART_TYPE_ID" );
        sqlStr.append(" from tt_as_repair_order r\n" );
		sqlStr.append(" left  join tm_dealer d on r.dealer_ID=d.dealer_ID \n");
		sqlStr.append(" left join Tt_As_Wr_Application a on a.ro_no = r.ro_no\n"); 

		sqlStr.append(" where 1=1");
		sqlStr.append("and r.order_valuable_type ="+Constant.RO_PRO_STATUS_01+"\n"); 
		sqlStr.append("and r.is_customer_in_asc=1\n"); 
		if(Utility.testString(map.get("dealerId"))) {
			//sqlStr.append(" and (d.DEALER_ID in(select d.dealer_id from tm_dealer d start with d.dealer_id = "+map.get("dealerId")+" connect by PRIOR d.dealer_id = d.parent_dealer_d )or r.second_dealer_id = "+map.get("dealerId")+")\n");
		sqlStr.append(" and nvl(r.second_dealer_id,r.dealer_id) = "+map.get("dealerId")+"\n");
//			sqlStr.append(" AND  DECODE(R.SECOND_DEALER_ID,\n");
//			sqlStr.append("                    null,\n");
//			sqlStr.append("                    D.DEALER_ID,\n");
//			sqlStr.append("                    R.SECOND_DEALER_ID) IN\n");
//			sqlStr.append("             (SELECT D.DEALER_ID\n");
//			sqlStr.append("                FROM TM_DEALER D\n");
//			sqlStr.append("               START WITH D.DEALER_ID =  "+map.get("dealerId")+"\n");
//			sqlStr.append("              CONNECT BY PRIOR D.DEALER_ID = D.PARENT_DEALER_D)"); 

		
		}
		if(Utility.testString(map.get("roNo"))) {
			sqlStr.append(" and r.RO_NO LIKE '%"+map.get("roNo")+"%' \n");
		}
		if(Utility.testString(map.get("repairType"))) {
			sqlStr.append(" and r.REPAIR_TYPE_CODE = '"+map.get("repairType")+"' \n");
		}
		if(Utility.testString(map.get("roCreateDate"))) {
			sqlStr.append(" and r.RO_CREATE_DATE >= to_date('"+map.get("roCreateDate")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("deliveryDate"))) {
			sqlStr.append(" AND r.RO_CREATE_DATE <= to_date('"+map.get("deliveryDate")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		//结算开始时间
		if(Utility.testString(map.get("balanceDateStart"))) {
			sqlStr.append(" and r.for_balance_Date >= to_date('"+map.get("balanceDateStart")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		//结算结束时间
		if(Utility.testString(map.get("balanceDateEnd"))) {
			sqlStr.append(" AND r.for_balance_Date <= to_date('"+map.get("balanceDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if (Utility.testString(map.get("customerName"))) {
			sqlStr.append(" and r.owner_name like '%"+map.get("customerName")+"%' \n");
		}
		if(Utility.testString(map.get("isForl"))) {
			sqlStr.append(" and r.FORL_STATUS = '"+map.get("isForl")+"' \n");
		}
		if(Utility.testString(map.get("roStatus"))) {
			sqlStr.append(" and r.RO_STATUS = '"+map.get("roStatus")+"' \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN不为空
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "r"));
		}
		if (Utility.testString(map.get("createDateStr"))) {
			sqlStr.append(" and r.CREATE_DATE >= to_date('"+map.get("createDateStr")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if (Utility.testString(map.get("createDateEnd"))) {
			sqlStr.append(" AND r.CREATE_DATE <= to_date('"+map.get("createDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("isChanghe"))){
			sqlStr.append(" and r.balance_yieldly="+map.get("isChanghe"));
		}
		if(Utility.testString(map.get("isWaring"))){
			sqlStr.append(" and r.is_warning="+map.get("isWaring"));
		}
		/*if(Utility.testString(map.get("orderBy"))){
			sqlStr.append("  order by r."+map.get("orderBy")+" DESC ");
		}else{
		sqlStr.append("  order by r.create_date DESC ");
		}*/
		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
				pageSize, curPage);
	}
	//结算时查询
	public PageResult<TtAsRepairOrderExtPO> queryRepairOrderSelf(
			Map<String, String> map, int pageSize, int curPage) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select decode(a.ro_no,null,0,1)  as in_claim,r.*,to_char(r.ro_create_date,'yyyy-mm-dd hh24:mi') ro_Create_Time,to_char(r.for_balance_time,'yyyy-mm-dd hh24:mi') balance_Time\n" );
//		sqlStr.append(" (select y.PART_TYPE_ID from tt_as_ro_repair_part x,Tm_Pt_Part_Base y where x.RO_ID=r.ID AND x.PART_NO=y.PART_CODEE) as PART_TYPE_ID" );
        sqlStr.append(" from tt_as_repair_order r\n" );
		sqlStr.append(" left  join tm_dealer d on r.dealer_ID=d.dealer_ID \n");
		sqlStr.append(" left join Tt_As_Wr_Application a on a.ro_no = r.ro_no\n"); 

		sqlStr.append(" where 1=1");
		DaoFactory.getsql(sqlStr, "r.create_date", MyConstant.onlineDate, 4);//加入时间新分单节点
		sqlStr.append("and r.order_valuable_type ="+Constant.RO_PRO_STATUS_01+"\n"); 
		sqlStr.append("and r.is_customer_in_asc=1\n"); 
		if(Utility.testString(map.get("dealerId"))) {
			//sqlStr.append(" and (d.DEALER_ID in(select d.dealer_id from tm_dealer d start with d.dealer_id = "+map.get("dealerId")+" connect by PRIOR d.dealer_id = d.parent_dealer_d )or r.second_dealer_id = "+map.get("dealerId")+")\n");
		
//			sqlStr.append(" AND  DECODE(R.SECOND_DEALER_ID,\n");
//			sqlStr.append("                    null,\n");
//			sqlStr.append("                    D.DEALER_ID,\n");
//			sqlStr.append("                    R.SECOND_DEALER_ID) IN\n");
//			sqlStr.append("             (SELECT D.DEALER_ID\n");
//			sqlStr.append("                FROM TM_DEALER D\n");
//			sqlStr.append("               START WITH D.DEALER_ID =  "+map.get("dealerId")+"\n");
//			sqlStr.append("              CONNECT BY PRIOR D.DEALER_ID = D.PARENT_DEALER_D)"); 
			sqlStr.append(" AND  nvl(R.SECOND_DEALER_ID,D.DEALER_ID)="+map.get("dealerId")+"\n");

		
		}
		if(Utility.testString(map.get("roNo"))) {
			sqlStr.append(" and r.RO_NO LIKE '%"+map.get("roNo")+"%' \n");
		}
		if(Utility.testString(map.get("repairType"))) {
			sqlStr.append(" and r.REPAIR_TYPE_CODE = '"+map.get("repairType")+"' \n");
		}
		if(Utility.testString(map.get("roCreateDate"))) {
			sqlStr.append(" and r.RO_CREATE_DATE >= to_date('"+map.get("roCreateDate")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("deliveryDate"))) {
			sqlStr.append(" AND r.RO_CREATE_DATE <= to_date('"+map.get("deliveryDate")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		//结算开始时间
		if(Utility.testString(map.get("balanceDateStart"))) {
			sqlStr.append(" and r.for_balance_Date >= to_date('"+map.get("balanceDateStart")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		//结算结束时间
		if(Utility.testString(map.get("balanceDateEnd"))) {
			sqlStr.append(" AND r.for_balance_Date <= to_date('"+map.get("balanceDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if (Utility.testString(map.get("customerName"))) {
			sqlStr.append(" and r.owner_name like '%"+map.get("customerName")+"%' \n");
		}
		if(Utility.testString(map.get("isForl"))) {
			sqlStr.append(" and r.FORL_STATUS = '"+map.get("isForl")+"' \n");
		}
		if(Utility.testString(map.get("roStatus"))) {
			sqlStr.append(" and r.RO_STATUS = '"+map.get("roStatus")+"' \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN不为空
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "r"));
		}
		if (Utility.testString(map.get("createDateStr"))) {
			sqlStr.append(" and r.CREATE_DATE >= to_date('"+map.get("createDateStr")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if (Utility.testString(map.get("createDateEnd"))) {
			sqlStr.append(" AND r.CREATE_DATE <= to_date('"+map.get("createDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(map.get("isChanghe"))){
			sqlStr.append(" and r.balance_yieldly="+map.get("isChanghe"));
		}
		/**屏蔽工单*/
		if(Utility.testString(map.get("dealerId"))){
	    	sqlStr.append(" and r.ro_no not in (select v.ro_no from tt_as_wr_repair_claim v where v.dealer_id='"+map.get("dealerId")+"')");
		}
//		if(Utility.testString(map.get("orderBy"))){
//			sqlStr.append("  order by r."+map.get("orderBy")+" DESC ");
//		}
		sqlStr.append("  order by r.create_date DESC ");
		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
				pageSize, curPage);
	}
	/**
	 * 预授权申请
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<TtAsRepairOrderExtPO> getForeApply(
			Map<String, String> map, int pageSize, int curPage) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select (select count(1) from tt_as_wr_application where ro_no=r.ro_no) as in_claim,r.*\n" );
//		sqlStr.append(" (select y.PART_TYPE_ID from tt_as_ro_repair_part x,Tm_Pt_Part_Base y where x.RO_ID=r.ID AND x.PART_NO=y.PART_CODEE) as PART_TYPE_ID" );
        sqlStr.append(" from tt_as_repair_order r\n" );
		sqlStr.append("left outer join tm_dealer d on r.dealer_ID=d.dealer_ID \n");
		sqlStr.append(" where 1=1 and  ((r.repair_type_code != "+Constant.REPAIR_TYPE_05+" and r.repair_type_code!= "+Constant.REPAIR_TYPE_04+")");
		sqlStr.append(" or (r.repair_type_code = "+Constant.REPAIR_TYPE_04+" and r.approval_yn = 1 and r.is_claim_fore=0))");
		sqlStr.append(" and  (r.is_del = 0 or r.is_del is null) and r.ro_status ="+Constant.RO_STATUS_01+"\n");
		sqlStr.append(" and  (r.approval_yn = 1 and  (r.forl_status="+Constant.RO_FORE_03+" or r.forl_status is null or r.forl_status =0))");
		sqlStr.append("and r.is_customer_in_asc=1\n"); 
		if(Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" and (d.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+map.get("dealerId")+"   connect by PRIOR d.dealer_id = d.parent_dealer_d)or r.second_dealer_id= "+map.get("dealerId")+") \n");
		}
		if(Utility.testString(map.get("roNo"))) {
			sqlStr.append(" and r.RO_NO LIKE '%"+map.get("roNo")+"%' \n");
		}
		if(Utility.testString(map.get("repairType"))) {
			sqlStr.append(" and r.REPAIR_TYPE_CODE = '"+map.get("repairType")+"' \n");
		}
		if(Utility.testString(map.get("roCreateDate"))) {
			sqlStr.append(" and r.RO_CREATE_DATE >= to_date('"+map.get("roCreateDate")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("deliveryDate"))) {
			sqlStr.append(" AND r.RO_CREATE_DATE <= to_date('"+map.get("deliveryDate")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		//结算开始时间
		if(Utility.testString(map.get("balanceDateStart"))) {
			sqlStr.append(" and r.for_balance_Date >= to_date('"+map.get("balanceDateStart")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		//结算结束时间
		if(Utility.testString(map.get("balanceDateEnd"))) {
			sqlStr.append(" AND r.for_balance_Date <= to_date('"+map.get("balanceDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if (Utility.testString(map.get("customerName"))) {
			sqlStr.append(" and r.owner_name like '%"+map.get("customerName")+"%' \n");
		}
		if(Utility.testString(map.get("isForl"))) {
			sqlStr.append(" and r.FORL_STATUS = '"+map.get("isForl")+"' \n");
		}
		if(Utility.testString(map.get("roStatus"))) {
			sqlStr.append(" and r.RO_STATUS = '"+map.get("roStatus")+"' \n");
		}
		if(Utility.testString(map.get("isWarning"))) {
			sqlStr.append(" and r.IS_WARNING = '"+map.get("isWarning")+"' \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN不为空
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "r"));
		}
		if (Utility.testString(map.get("createDateStr"))) {
			sqlStr.append(" and r.CREATE_DATE >= to_date('"+map.get("createDateStr")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if (Utility.testString(map.get("createDateEnd"))) {
			sqlStr.append(" AND r.CREATE_DATE <= to_date('"+map.get("createDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		
		sqlStr.append("  order by r.ID DESC ");
		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
				pageSize, curPage);
	}
	
	public PageResult<TtAsRepairOrderExtPO> queryRepairOrderSpecial(
			Map<String, String> map, int pageSize, int curPage) {
		TtAsRepairOrderPO po  = new TtAsRepairOrderPO();
		po.setId(Long.valueOf(map.get("roId").toString()));
		List<TtAsRepairOrderPO> listPo = this.select(po);
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select (select count(1) from tt_as_wr_application where ro_no=r.ro_no) as in_claim,r.*\n" );
//		sqlStr.append(" (select y.PART_TYPE_ID from tt_as_ro_repair_part x,Tm_Pt_Part_Base y where x.RO_ID=r.ID AND x.PART_NO=y.PART_CODEE) as PART_TYPE_ID" );
        sqlStr.append(" from tt_as_repair_order r\n" );
		sqlStr.append("left outer join tm_dealer d on r.dealer_ID=d.dealer_ID \n");
		sqlStr.append(" where 1=1");
	    sqlStr.append("and r.in_mileage >="+listPo.get(0).getInMileage()+"-5  ");
	    sqlStr.append("and r.in_mileage <="+listPo.get(0).getInMileage()+"+5  ");
	    sqlStr.append("and r.vin = '"+listPo.get(0).getVin()+"'");
//	    r.in_mileage ,r.create_date
		sqlStr.append("  AND r.remark1 is null and INSURATION_CODE=1 "); //YH 2011.7.5	
		sqlStr.append("and r.forl_status=11561002 ");
		sqlStr.append("  order by r.ID DESC ");
		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
				pageSize, curPage);
	}
	
	public PageResult<TtAsRepairOrderExtPO> queryRepairOrderApply(
			Map<String, String> map, int pageSize, int curPage) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select (select count(1) from tt_as_wr_application where ro_no=r.ro_no) as in_claim,r.*\n" );
//		sqlStr.append(" (select y.PART_TYPE_ID from tt_as_ro_repair_part x,Tm_Pt_Part_Base y where x.RO_ID=r.ID AND x.PART_NO=y.PART_CODEE) as PART_TYPE_ID" );
        sqlStr.append(" from tt_as_repair_order r\n" );
		sqlStr.append("left outer join tm_dealer d on r.dealer_ID=d.dealer_ID \n");
		sqlStr.append(" where 1=1");
		sqlStr.append("    AND ( r.INSURATION_CODE =1 or r.repair_type_code = 11441004)   ");
		if(Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" and d.DEALER_ID="+map.get("dealerId")+" \n");
		}
		if(Utility.testString(map.get("roNo"))) {
			sqlStr.append(" and r.RO_NO LIKE '%"+map.get("roNo")+"%' \n");
		}
		if(Utility.testString(map.get("repairType"))) {
			sqlStr.append(" and r.REPAIR_TYPE_CODE = '"+map.get("repairType")+"' \n");
		}
		if(Utility.testString(map.get("roCreateDate"))) {
			sqlStr.append(" and r.RO_CREATE_DATE >= to_date('"+map.get("roCreateDate")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("deliveryDate"))) {
			sqlStr.append(" AND r.DELIVERY_DATE <= to_date('"+map.get("deliveryDate")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		//结算开始时间
		if(Utility.testString(map.get("balanceDateStart"))) {
			sqlStr.append(" and r.UPDATE_DATE >= to_date('"+map.get("balanceDateStart")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		//结算结束时间
		if(Utility.testString(map.get("balanceDateEnd"))) {
			sqlStr.append(" AND r.UPDATE_DATE <= to_date('"+map.get("balanceDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if (Utility.testString(map.get("customerName"))) {
			sqlStr.append(" and r.owner_name like '%"+map.get("customerName")+"%' \n");
		}
		if(Utility.testString(map.get("isForl"))) {
			sqlStr.append(" and r.FORL_STATUS = '"+map.get("isForl")+"' \n");
		}
		if(Utility.testString(map.get("roStatus"))) {
			sqlStr.append(" and r.RO_STATUS = '"+map.get("roStatus")+"' \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN不为空
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "r"));
		}
		if (Utility.testString(map.get("createDateStr"))) {
			sqlStr.append(" and r.CREATE_DATE >= to_date('"+map.get("createDateStr")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if (Utility.testString(map.get("createDateEnd"))) {
			sqlStr.append(" AND r.CREATE_DATE <= to_date('"+map.get("createDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		sqlStr.append("  AND r.remark1 is null  "); //YH 2011.7.5	
		sqlStr.append("  AND r.is_de=1  and r.approval_yn=1  ");
		
		sqlStr.append("  order by r.ID DESC ");
		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
				pageSize, curPage);
	}
	
	/**
	 * Iverson add with 2010-11-15
     * 查询可删除的工单
     * @param map 查询条件
     * @param pageSize 
     * @param curPage
     * @return PageResult<TtAsRepairOrderExtPO>
     */
	public PageResult<TtAsRepairOrderExtPO> queryDeleteRepairOrderNew(Map<String, String> map, int pageSize, int curPage) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select decode(a.ro_no,null,0,1)  as in_claim,r.*\n" );
        sqlStr.append(" from tt_as_repair_order r\n" );
		sqlStr.append("left  join tm_dealer d on r.dealer_code=d.dealer_code \n");
		sqlStr.append("  left join Tt_As_Wr_Application a on a.ro_no = r.ro_no ");
		//sqlStr.append(" where 1=1 and (r.ORDER_VALUABLE_TYPE='"+Constant.RO_PRO_STATUS_02+"'   or  r.ORDER_VALUABLE_TYPE = '13591001' and a.status is null  and r.approval_yn<>1 )\n");
		sqlStr.append(" where 1=1 and r.ORDER_VALUABLE_TYPE='"+Constant.RO_PRO_STATUS_02+"'   \n");
		sqlStr.append("and r.is_customer_in_asc=1\n"); 
		if(Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" and (d.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+map.get("dealerId")+"   connect by PRIOR d.dealer_id = d.parent_dealer_d)or r.second_dealer_id = "+map.get("dealerId")+" ) \n");
		}
		if(Utility.testString(map.get("roNo"))) {
			sqlStr.append(" and r.RO_NO LIKE '%"+map.get("roNo")+"%' \n");
		}
		if(Utility.testString(map.get("repairType"))) {
			sqlStr.append(" and r.REPAIR_TYPE_CODE = '"+map.get("repairType")+"' \n");
		}
		if(Utility.testString(map.get("roCreateDate"))) {
			sqlStr.append(" and r.RO_CREATE_DATE >= to_date('"+map.get("roCreateDate")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("deliveryDate"))) {
			sqlStr.append(" AND r.RO_CREATE_DATE <= to_date('"+map.get("deliveryDate")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		//缁撶畻寮�鏃堕棿
		if(Utility.testString(map.get("balanceDateStart"))) {
			sqlStr.append(" and r.for_balance_Date >= to_date('"+map.get("balanceDateStart")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		//缁撶畻缁撴潫鏃堕棿
		if(Utility.testString(map.get("balanceDateEnd"))) {
			sqlStr.append(" AND r.for_balance_Date <= to_date('"+map.get("balanceDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if (Utility.testString(map.get("customerName"))) {
			sqlStr.append(" and r.owner_name like '%"+map.get("customerName")+"%' \n");
		}
		if(Utility.testString(map.get("isForl"))) {
			sqlStr.append(" and r.FORL_STATUS = '"+map.get("isForl")+"' \n");
		}
		if(Utility.testString(map.get("roStatus"))) {
			sqlStr.append(" and r.RO_STATUS = '"+map.get("roStatus")+"' \n");
		}
		if(Utility.testString(map.get("isChanghe"))) {
			sqlStr.append(" and r.balance_yieldly= "+map.get("isChanghe")+"\n");
		}
		// 杞﹁締VIN鐮�
		if (Utility.testString(map.get("vin"))) {// VIN涓嶄负绌�
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "r"));
		}
		if (Utility.testString(map.get("createDateStr"))) {
			sqlStr.append(" and r.CREATE_DATE >= to_date('"+map.get("createDateStr")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if (Utility.testString(map.get("createDateEnd"))) {
			sqlStr.append(" AND r.CREATE_DATE <= to_date('"+map.get("createDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		sqlStr.append(" AND (r.INSURATION_CODE=0 or r.INSURATION_CODE is null) ");
		sqlStr.append(" AND r.remark1 is null "); //YH 2011.7.5	
		sqlStr.append("  order by r.ID DESC ");
		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
				pageSize, curPage);
		
		/*//===================
		sqlStr.append("  or ( r.ORDER_VALUABLE_TYPE = '"+Constant.RO_PRO_STATUS_01+"' and  r.ro_status=11591001 and r.forl_status=11561003  \n");
		
		sqlStr.append("and r.is_customer_in_asc=1\n"); 
		if(Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" and (d.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+map.get("dealerId")+"   connect by PRIOR d.dealer_id = d.parent_dealer_d)or r.second_dealer_id = "+map.get("dealerId")+" ) \n");
		}
		if(Utility.testString(map.get("roNo"))) {
			sqlStr.append(" and r.RO_NO LIKE '%"+map.get("roNo")+"%' \n");
		}
		if(Utility.testString(map.get("repairType"))) {
			sqlStr.append(" and r.REPAIR_TYPE_CODE = '"+map.get("repairType")+"' \n");
		}
		if(Utility.testString(map.get("roCreateDate"))) {
			sqlStr.append(" and r.RO_CREATE_DATE >= to_date('"+map.get("roCreateDate")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("deliveryDate"))) {
			sqlStr.append(" AND r.RO_CREATE_DATE <= to_date('"+map.get("deliveryDate")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		//结算开始时间
		if(Utility.testString(map.get("balanceDateStart"))) {
			sqlStr.append(" and r.for_balance_Date >= to_date('"+map.get("balanceDateStart")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		//结算结束时间
		if(Utility.testString(map.get("balanceDateEnd"))) {
			sqlStr.append(" AND r.for_balance_Date <= to_date('"+map.get("balanceDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if (Utility.testString(map.get("customerName"))) {
			sqlStr.append(" and r.owner_name like '%"+map.get("customerName")+"%' \n");
		}
		if(Utility.testString(map.get("isForl"))) {
			sqlStr.append(" and r.FORL_STATUS = '"+map.get("isForl")+"' \n");
		}
		if(Utility.testString(map.get("roStatus"))) {
			sqlStr.append(" and r.RO_STATUS = '"+map.get("roStatus")+"' \n");
		}
		if(Utility.testString(map.get("isChanghe"))) {
			sqlStr.append(" and r.balance_yieldly= "+map.get("isChanghe")+"\n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN不为空
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "r"));
		}
		if (Utility.testString(map.get("createDateStr"))) {
			sqlStr.append(" and r.CREATE_DATE >= to_date('"+map.get("createDateStr")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if (Utility.testString(map.get("createDateEnd"))) {
			sqlStr.append(" AND r.CREATE_DATE <= to_date('"+map.get("createDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		sqlStr.append(" AND (r.INSURATION_CODE=0 or r.INSURATION_CODE is null) ");
		sqlStr.append(" AND r.remark1 is null  )\n"); //YH 2011.7.5	
		//===问题工单‘======================================
*/		/*sqlStr.append("  order by r.ID DESC ");
		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
				pageSize, curPage);*/
	}

	//根据vin查询车辆信息
	public List<TmVehiclePO> getCar(String vin){
		String sql = "select * from tm_vehicle v where v.vin = '"+vin+"'" ;
		return this.select(TmVehiclePO.class, sql.toString(), null);
	}
	/**********
	 * 根据org_id查询是否大区
	 * @param ro_no
	 * @return
	 */
	public List<Map<String, Object>> viewOrgLevel(String org_id){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select org.org_level from tm_org  org where org.org_id="+org_id+"");
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**********
	 * 查询出firstPart
	 * @param claimId
	 * @return
	 */
	public List<Map<String, Object>> viewFirstPart(String claimId){
		  StringBuilder sql= new StringBuilder();
		  sql.append(" select distinct first_part,labour_id,wr_labourcode from TT_AS_WR_LABOURITEM where  id="+claimId+" and first_part is not null ");
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**********
	 * 查询出PART_CODE
	 * @param claimId
	 * @return
	 */
	public List<Map<String, Object>> viewPartCode(Long claimId,String part_code,String wr_labour_code){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select min(p.part_id) as part_id from TT_AS_WR_PARTSITEM p where p.id="+claimId+" and wr_labourcode='"+wr_labour_code+"'");
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**********
	 * 查询出PART_CODE
	 * @param claimId
	 * @return
	 */
	public List<Map<String, Object>> viewSupplier(String partCode){
		  StringBuilder sql= new StringBuilder();
			sql.append("select t.maker_id  from tt_part_maker_relation t  where t.part_id in (\n" );
			sql.append("select pb.part_id from  tm_pt_part_base pb where pb.part_code in('"+partCode+"'))\n" );
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	//zhumingwei add by 2011-02-25
	public List<Map<String, Object>> selectCode(){
		StringBuilder sql= new StringBuilder();
		sql.append("select distinct a.vrt_name,a.vrt_code from tm_ccc a where a.status='"+Constant.IF_TYPE_YES+"'\n" );
		System.out.println("----------------------"+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	//更新
	public void updateFirstCode(Long labourId,long partId){
		  StringBuilder sql= new StringBuilder();
		  sql.append("update  TT_AS_WR_LABOURITEM l set l.first_part='"+partId+"' where l.labour_id="+labourId+"");
		  System.out.println("----------------------"+sql.toString());
		  dao.update(sql.toString(), null);
	}
	/**********
	 * 根据Claim查询出重复的工时
	 * @param claimId
	 * @return
	 */
	public List<Map<String, Object>> viewRepeatedlyLabour(Long ro_id){
		  StringBuilder sql= new StringBuilder();
		  sql.append("SELECT WL.ro_id,COUNT(1),wl.wr_labourcode as code FROM tt_as_ro_labour WL WHERE wl.ro_id="+ro_id+"\n");
		  sql.append("GROUP BY WL.ro_id ,wl.wr_labourcode  HAVING COUNT(WL.Wr_Labourcode)>1");
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**********
	 * 根据查询出重复工时中最大的明细ID
	 * @param claimId
	 * @return
	 */
	public List<Map<String, Object>> viewLabourCode(Long ro_id,String labourCode){
		  StringBuilder sql= new StringBuilder();

		  sql.append("SELECT max(id) as labour_id FROM tt_as_ro_labour l where l.ro_id="+ro_id+" and l.wr_labourcode='"+labourCode+"'");

		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public void deleteLabourItem(String labourId,Long ro_id){
	String sql = "delete from tt_as_ro_labour wl where wl.ro_id="+ro_id+" and wl.id="+labourId;
		factory.delete(sql.toString(), null);
	}
	/**********
	 * 根据查询出重复工时中最大的明细ID
	 * @param claimId
	 * @return
	 */
	public List<Map<String, Object>> viewModelId(String vin ){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select v.model_id from tm_vehicle v where v.vin='"+vin+"'");
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	public List<Map<String, Object>> viewIsNewPart(String ro_id ){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select count(1) as count from tm_pt_part_base pp where pp.is_new_part =1  and\n");
		  sql.append("pp.part_code in (\n" );
		  sql.append("select rp.part_no from TT_AS_RO_REPAIR_PART rp where rp.ro_id="+ro_id+")");
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public List<Map<String, Object>> viewIsNewPartString(String ro_id ){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select pp.part_code as PART_NAME from tm_pt_part_base pp where pp.is_new_part =1  and\n");
		  sql.append("pp.part_code in (\n" );
		  sql.append("select rp.part_no from TT_AS_RO_REPAIR_PART rp where rp.ro_id="+ro_id+")");
		  System.out.println("----------------------"+sql.toString());
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public List<Map<String, Object>> viewRoNoCount(String ro_no ){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select count(1) as COUNT from tt_as_repair_order ro where ro.ro_no='"+ro_no+"'");
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	/**
	 * Iverson add with 2010-11-12
	 * 删除工单信息
	 * @param po
	 */
	public void deleteOrder(TtAsRepairOrderPO po){
		  this.delete(po);
	}
	/**
	 * Iverson add with 2010-11-12
	 * 根据工单ID查询工单信息信息
	 * @param po
	 */
	public List selectRepairOrderById(TtAsRepairOrderPO po){
		List list = new ArrayList();
		list = this.select(po);
		return list;
	}
	/**
	 * Iverson add with 2010-11-12
	 * 插入工单备份表数据
	 * @param po
	 */
	public void insertRepairOrder(TtAsRepairOrderBackupPO po){
		this.insert(po);
	}
	
	/**
	 * Iverson add with 2010-11-12
	 * 根据索赔单ID查询索赔单信息信息
	 * @param po
	 */
	public void deleteApplication(TtAsWrApplicationPO po){
		  this.delete(po);
	}
	
	/**
	 * Iverson add with 2010-11-12
	 * 插入索赔单备份表数据
	 * @param po
	 */
	public int insertApplication(long id){
		int count = 0;
		List list = new ArrayList();
		list.add(id);
		count = update("insert into tt_as_wr_application_backup select * from tt_as_wr_application wa where wa.id=?", list);
		return count;
	}
	/**
	 * Iverson add with 2010-12-15
	 * 根据索赔单ID查询索赔单和结算单的关系表信息
	 * @param po
	 */
	public void deleteTrBalanceClaim(TrBalanceClaimPO po){
		  this.delete(po);
	}
	
	public int updateApplication(long id){
		int count = 0;
		List list = new ArrayList();
		list.add(id);
		count = update("update tt_as_wr_application a set a.application_del='"+Constant.RO_APP_STATUS_03+"' where a.id=?", list);
		return count;
	}
	
	public List selectApplicationById(TtAsWrApplicationPO po){
		List list = new ArrayList();
		  StringBuilder sql= new StringBuilder();
		  sql.append("select * from   tt_as_wr_application p");
		  System.out.println("----------------------"+sql.toString());
		  list = dao.select(po);
		  return list;
	  }
	
	/********add by liuxh 20101117 增加车厂工单查询功能*********/
	  /**
     * 查询结算单使用
     * ADD 2010-10-24  只为工单查询使用
     * @param map 查询条件
     * @param pageSize 
     * @param curPage
     * @return PageResult<TtAsRepairOrderExtPO>
     */
	public PageResult<TtAsRepairOrderExtPO> queryRepairOrderNewQuery(
			Map<String, String> map, int pageSize, int curPage) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("SELECT DECODE(A.RO_NO, NULL, 0, 1) IN_CLAIM,a.id as gdid,SS.SUBJECT_NO,r.cam_code,\n");
		sqlStr.append("       R.id,r.ro_no,r.vin,r.free_times,r.RO_STATUS,r.IN_MILEAGE,r.RO_CREATE_DATE,r.DELIVERER_MOBILE,\n");
		sqlStr.append("       r.DELIVERER_PHONE,r.DELIVERER,r.OWNER_NAME,r.MODEL,r.LICENSE,r.BALANCE_YIELDLY,r.REPAIR_TYPE_CODE,\n");
		sqlStr.append("       D.DEALER_CODE DEALER_CODES,\n");
		sqlStr.append("       D.DEALER_SHORTNAME,\n");
		sqlStr.append("       DS.ROOT_ORG_NAME AREA_NAME,");
		sqlStr.append("       M.MATERIAL_NAME,");
		sqlStr.append("       M.COLOR_NAME,");
		sqlStr.append("       TO_CHAR(V.PURCHASED_DATE, 'yyyy-mm-dd') PURCHASED_DATE,\n");
		sqlStr.append("       DECODE(C.MAIN_PHONE, NULL, C.OTHER_PHONE, C.MAIN_PHONE) OWNER_PHONE,M3.GROUP_NAME\n");
        sqlStr.append(" from tt_as_repair_order r\n" );
        sqlStr.append("left join tm_vehicle v on v.vin = r.vin\n");
        sqlStr.append("left join tt_dealer_actual_sales s on s.vehicle_id = v.vehicle_id and s.is_return="+Constant.IF_TYPE_NO+"\n");
        sqlStr.append("left join tt_customer c on c.ctm_id   = s.ctm_id\n");
        sqlStr.append("left join tt_as_wr_application a on   r.ro_no=a.ro_no\n"); 
//		sqlStr.append("left outer join tm_dealer d on nvl(r.second_dealer_id,r.dealer_id)=d.dealer_id \n");
        sqlStr.append("left outer join tm_dealer d on r.dealer_id=d.dealer_id \n");
		sqlStr.append("LEFT JOIN vw_org_dealer_service ds ON d.dealer_id = ds.dealer_id\n");
		sqlStr.append("LEFT JOIN TM_VHCL_MATERIAL M ON V.MATERIAL_ID = M.MATERIAL_ID\n");
		sqlStr.append("LEFT JOIN TT_AS_ACTIVITY AA ON AA.ACTIVITY_CODE = R.CAM_CODE\n");
		sqlStr.append("LEFT JOIN TT_AS_ACTIVITY_SUBJECT  SS ON SS.SUBJECT_ID = AA.SUBJECT_ID\n");
		sqlStr.append("LEFT JOIN TM_VHCL_MATERIAL_GROUP M2 ON V.SERIES_ID = M2.GROUP_ID AND M2.GROUP_LEVEL=2\n");
		sqlStr.append("LEFT JOIN TM_VHCL_MATERIAL_GROUP M3 ON V.MODEL_ID = M3.GROUP_ID AND M3.GROUP_LEVEL=3\n"); 
		//关联物料组 11.14  wenyudan 
		sqlStr.append("JOIN  tm_vhcl_material_group_r tvmgr on m.material_id=tvmgr.material_id\n" );
		sqlStr.append("JOIN   tm_vhcl_material_group tvmg on tvmgr.group_id=tvmg.group_id");
 


		sqlStr.append(" where 1=1 \n");
	//	sqlStr.append(" where 1=1 and r.RO_STATUS = "+Constant.RO_STATUS_02+"\n");
		//如果职位为车厂售后东安 结算基地只能是东安
		//10.11  wenyudan 
		if(map.get("poseBusType").equals(String.valueOf(Constant.POSE_BUS_TYPE_WRD))) {
			sqlStr.append(" and r.balance_yieldly="+Constant.PART_IS_CHANGHE_02+" \n");
		}
		if (null != map.get("groupCode") && !"".equals(map.get("groupCode"))) {
			String[] array = map.get("groupCode").toString().split(",");
			String code="";
		for (int i = 0; i < array.length; i++) {
			code += "'"+array[i]+"',";
			}
		code = code.substring(0,code.length()-1);
			sqlStr.append("and tvmg.group_id in\n");
			sqlStr.append("   (select TVMG.group_id  from tm_vhcl_material_group TVMG   start with TVMG.Group_Code in("+code+")\n");
			sqlStr.append("     connect by prior TVMG.group_id =TVMG.parent_group_id )"); 
		}
		if (null != map.get("areaCode") && !"".equals(map.get("areaCode"))) {
			String[] array = map.get("areaCode").toString().split(",");
			sqlStr.append("   AND ds.org_code IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sqlStr.append("'"+array[i]+"'");
					if (i != array.length - 1) {
						sqlStr.append(",");
					}	
				}
			}else{
				sqlStr.append("''");//放空置，防止in里面报错
			}
			sqlStr.append(")\n");
		}

		if(Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" and d.DEALER_ID="+map.get("dealerId")+" \n");
		}
		if(Utility.testString(map.get("orderStatus"))) {
			sqlStr.append(" and r.order_valuable_type="+map.get("orderStatus")+" \n");
		}
		if(Utility.testString(map.get("roNo"))) {
			sqlStr.append(" and r.RO_NO LIKE '%"+map.get("roNo")+"%' \n");
		}
		if(Utility.testString(map.get("model"))) {
			sqlStr.append(" and r.model= '"+map.get("model")+"' \n");
		}
		if(Utility.testString(map.get("activityCode"))) {
			sqlStr.append(" and r.CAM_CODE LIKE '%"+map.get("activityCode").toUpperCase()+"%' \n");
		}
		if(Utility.testString(map.get("activityMain"))) {
			sqlStr.append(" and SS.SUBJECT_NO LIKE '%"+map.get("activityMain").toUpperCase()+"%' \n");
		}
		if(Utility.testString(map.get("repairType"))) {
			sqlStr.append(" and r.REPAIR_TYPE_CODE = "+map.get("repairType")+" \n");
		}
		if(Utility.testString(map.get("roCreateDate"))) {
			sqlStr.append(" and r.RO_CREATE_DATE >= to_date('"+map.get("roCreateDate")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("deliveryDate"))) {
			sqlStr.append(" AND r.RO_CREATE_DATE <= to_date('"+map.get("deliveryDate")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		//结算开始时间
		if(Utility.testString(map.get("balanceDateStart"))) {
			sqlStr.append(" and r.for_balance_Date >= to_date('"+map.get("balanceDateStart")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		//结算结束时间
		if(Utility.testString(map.get("balanceDateEnd"))) {
			sqlStr.append(" AND r.for_balance_Date <= to_date('"+map.get("balanceDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if (Utility.testString(map.get("customerName"))) {
			sqlStr.append(" and r.owner_name like '%"+map.get("customerName")+"%' \n");
		}
		if(Utility.testString(map.get("isForl"))) {
			sqlStr.append(" and r.FORL_STATUS = '"+map.get("isForl")+"' \n");
		}
		if(Utility.testString(map.get("roStatus"))) {
			sqlStr.append(" and r.RO_STATUS = "+map.get("roStatus")+" \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN不为空
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "r"));
		}
		if (Utility.testString(map.get("createDateStr"))) {
			sqlStr.append(" and r.CREATE_DATE >= to_date('"+map.get("createDateStr")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if (Utility.testString(map.get("createDateEnd"))) {
			sqlStr.append(" AND r.CREATE_DATE <= to_date('"+map.get("createDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(!(map.get("dealerIdCon").toString().equals(""))){
			sqlStr.append(" and r.dealer_code in(select dea.dealer_code from tm_dealer dea where dea.dealer_id in("+map.get("dealerIdCon").toString()+"))");
		}
		sqlStr.append("  order by r.ID DESC ");
	//	System.out.println("=========>"+sqlStr.toString());
		return this.pageQuery(sqlStr.toString(), null, this.getFunName(), pageSize, curPage);
//		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
//				pageSize, curPage);
	}
	public List<Map<String, Object>> queryRepairOrderNewQueryList(
			Map<String, String> map) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select   r.repair_part_amount,r.labour_amount,r.add_item_amount,DELIVERER,DELIVERER_PHONE,DELIVERER_MOBILE,r.dealer_code,d.dealer_name,r.ro_no,r.repair_type_code,r.balance_yieldly,v.license_no,SS.SUBJECT_NO,\n");
		sqlStr.append("v.vin,  r.model,c.ctm_name owner_name,TO_CHAR(R.RO_CREATE_DATE,'YYYY-MM-DD') start_Date ,\n");
		sqlStr.append("r.in_mileage,r.ro_status,r.CAM_CODE,r.free_times,\n");
		sqlStr.append("d.dealer_shortname,decode(c.main_phone,null,c.other_phone,c.main_phone) owner_phone, M2.GROUP_NAME\n");
		sqlStr.append(",C1.CODE_DESC CODE_DESC1,C2.CODE_DESC CODE_DESC2,C3.CODE_DESC CODE_DESC3,m.material_name, m.color_name,r.remarks,ds.root_org_name\n"); 

		sqlStr.append(" from tt_as_repair_order r\n" );
		sqlStr.append("LEFT JOIN TC_CODE C1  ON C1.CODE_ID =R.REPAIR_TYPE_CODE\n");
		sqlStr.append(" LEFT JOIN TC_CODE C2  ON C2.CODE_ID =R.BALANCE_YIELDLY\n");
		sqlStr.append(" LEFT JOIN TC_CODE C3  ON C3.CODE_ID =R.RO_STATUS\n"); 

        sqlStr.append("left join tm_vehicle v on v.vin = r.vin\n");
        sqlStr.append("left join tt_dealer_actual_sales s on s.vehicle_id = v.vehicle_id   and s.is_return="+Constant.IF_TYPE_NO+"\n");
        sqlStr.append("left join tt_customer c on c.ctm_id   = s.ctm_id\n");
        sqlStr.append("left join tt_as_wr_application a on   r.ro_no=a.ro_no\n"); 
		sqlStr.append("left outer join tm_dealer d on r.dealer_id=d.dealer_id \n");
		sqlStr.append("LEFT JOIN vw_org_dealer_service ds ON d.dealer_id = ds.dealer_id\n");
		sqlStr.append("   LEFT JOIN TM_VHCL_MATERIAL M ON V.MATERIAL_ID = M.MATERIAL_ID\n"); 
		sqlStr.append(" LEFT JOIN tm_vhcl_material_group M2 ON V.Series_Id = M2.Group_Id and m2.group_level=2\n"); 

		sqlStr.append("LEFT JOIN TT_AS_ACTIVITY AA ON AA.ACTIVITY_CODE = R.CAM_CODE\n");
		sqlStr.append("LEFT JOIN TT_AS_ACTIVITY_SUBJECT  SS ON SS.SUBJECT_ID = AA.SUBJECT_ID\n"); 
		
		//关联物料组 11.14  wenyudan 
		sqlStr.append("JOIN  tm_vhcl_material_group_r tvmgr on m.material_id=tvmgr.material_id\n" );
		sqlStr.append("JOIN   tm_vhcl_material_group tvmg on tvmgr.group_id=tvmg.group_id");
 

		sqlStr.append(" where 1=1 and r.RO_STATUS = "+Constant.RO_STATUS_02+"\n");
		if (null != map.get("groupCode") && !"".equals(map.get("groupCode"))) {
			String[] array = map.get("groupCode").toString().split(",");
			sqlStr.append("   AND TVMG.Group_Code IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sqlStr.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							sqlStr.append(",");
						}	
				}
			}else{
				sqlStr.append("''");//放空置，防止in里面报错
			}
			sqlStr.append(")\n");
		}
		if (null != map.get("areaCode") && !"".equals(map.get("areaCode"))) {
			String[] array = map.get("areaCode").toString().split(",");
			sqlStr.append("   AND ds.org_code IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sqlStr.append("'"+array[i]+"'");
					if (i != array.length - 1) {
						sqlStr.append(",");
					}	
				}
			}else{
				sqlStr.append("''");//放空置，防止in里面报错
			}
			sqlStr.append(")\n");
		}

		if(Utility.testString(map.get("dealerId"))) {
			sqlStr.append(" and d.DEALER_ID="+map.get("dealerId")+" \n");
		}
		if(Utility.testString(map.get("orderStatus"))) {
			sqlStr.append(" and r.order_valuable_type="+map.get("orderStatus")+" \n");
		}
		if(Utility.testString(map.get("roNo"))) {
			sqlStr.append(" and r.RO_NO LIKE '%"+map.get("roNo")+"%' \n");
		}
		if(Utility.testString(map.get("activityMain"))) {
			sqlStr.append(" and SS.SUBJECT_NO LIKE '%"+map.get("activityMain").toUpperCase()+"%' \n");
		}
		if(Utility.testString(map.get("repairType"))) {
			sqlStr.append(" and r.REPAIR_TYPE_CODE = "+map.get("repairType")+" \n");
		}
		if(Utility.testString(map.get("roCreateDate"))) {
			sqlStr.append(" and r.RO_CREATE_DATE >= to_date('"+map.get("roCreateDate")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(map.get("activityCode"))) {
			sqlStr.append(" and r.CAM_CODE LIKE '%"+map.get("activityCode").toUpperCase()+"%' \n");
		}
		if(Utility.testString(map.get("deliveryDate"))) {
			sqlStr.append(" AND r.RO_CREATE_DATE <= to_date('"+map.get("deliveryDate")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		//结算开始时间
		if(Utility.testString(map.get("balanceDateStart"))) {
			sqlStr.append(" and r.for_balance_Date >= to_date('"+map.get("balanceDateStart")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		//结算结束时间
		if(Utility.testString(map.get("balanceDateEnd"))) {
			sqlStr.append(" AND r.for_balance_Date <= to_date('"+map.get("balanceDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if (Utility.testString(map.get("customerName"))) {
			sqlStr.append(" and r.owner_name like '%"+map.get("customerName")+"%' \n");
		}
		if(Utility.testString(map.get("isForl"))) {
			sqlStr.append(" and r.FORL_STATUS = '"+map.get("isForl")+"' \n");
		}
		if(Utility.testString(map.get("roStatus"))) {
			sqlStr.append(" and r.RO_STATUS = "+map.get("roStatus")+" \n");
		}
		// 车辆VIN码
		if (Utility.testString(map.get("vin"))) {// VIN不为空
			sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
					"\''"), "r"));
		}
		if (Utility.testString(map.get("createDateStr"))) {
			sqlStr.append(" and r.CREATE_DATE >= to_date('"+map.get("createDateStr")+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if (Utility.testString(map.get("createDateEnd"))) {
			sqlStr.append(" AND r.CREATE_DATE <= to_date('"+map.get("createDateEnd")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(!(map.get("dealerIdCon").toString().equals(""))){
			sqlStr.append(" and r.dealer_code in(select dea.dealer_code from tm_dealer dea where dea.dealer_id in("+map.get("dealerIdCon").toString()+"))");
		}
		sqlStr.append("  order by r.ID DESC ");
		return this.pageQuery(sqlStr.toString(), null, this.getFunName());	
//		return pageQuery(TtAsRepairOrderExtPO.class, sqlStr.toString(), null,
//				pageSize, curPage);
	}
	/********add by liuxh 20101117 增加车厂工单查询功能*********/
	public PageResult<Map> vehicleCusInfoQuery(
			Map<String, String> map, int pageSize, int curPage) {
		StringBuffer sb=new StringBuffer("\n");
		sb.append("   SELECT A.VEHICLE_ID,A.VIN,A.ENGINE_NO,A.LICENSE_NO,A.IS_PDI,A.IS_DOMESTIC,  " );
		sb.append("   g.group_code  AS MODEL_NAME, " );
		sb.append("   (SELECT C.CTM_NAME FROM TT_DEALER_ACTUAL_SALES B,TT_CUSTOMER C WHERE B.CTM_ID=C.CTM_ID AND B.VEHICLE_ID=A.VEHICLE_ID and B.Is_Return=?) AS CUS_NAME, " );
		sb.append("   (SELECT b.invoice_date FROM TT_DEALER_ACTUAL_SALES B WHERE B.VEHICLE_ID=A.VEHICLE_ID AND IS_RETURN=?) AS SALES_DATE, " );
		sb.append("   A.PRODUCT_DATE,B.AREA_NAME,A.MILEAGE,D.DEALER_CODE,to_char(A.PURCHASED_DATE,'yyyy-mm-dd') PURCHASED_DATE,A.FREE_TIMES,A.LIFE_CYCLE    " );
		sb.append("   FROM TM_VEHICLE A,tm_vhcl_material_group g ,Tm_Business_Area B ,tm_dealer D" );
		sb.append("    WHERE a.package_id = g.group_id(+) AND A.YIELDLY = B.AREA_ID and A.dealer_id=D.dealer_id");
		sb.append("   AND A.LIFE_CYCLE IN(?,?,?,?,?) ");
		List par=new ArrayList();
		par.add(Constant.IF_TYPE_NO);
		par.add(Constant.IF_TYPE_NO);
		par.add(Constant.VEHICLE_LIFE_01);
		par.add(Constant.VEHICLE_LIFE_02);
		par.add(Constant.VEHICLE_LIFE_03);
		par.add(Constant.VEHICLE_LIFE_04);
		par.add(Constant.VEHICLE_LIFE_05);
		String vin=map.get("vin").toString();
		String carStatus=map.get("carStatus").toString();
		String yieldly=map.get("yieldly").toString();
		String carNo=map.get("carNo").toString();
		String cusName=map.get("cusName").toString();
		String isPDI=map.get("isPDI").toString();
		if(!vin.equals("")){
			sb.append("  AND A.VIN LIKE ? ");
			par.add("%"+vin+"%");
		}
		if(!carStatus.equals("")){
			if(carStatus.equals(Constant.CAR_STATUS_SALES)){//未售
				sb.append("  AND A.LIFE_CYCLE IN(?,?,?,?) ");
				par.add(Constant.VEHICLE_LIFE_01);
				par.add(Constant.VEHICLE_LIFE_02);
				par.add(Constant.VEHICLE_LIFE_03);
				par.add(Constant.VEHICLE_LIFE_05);
			}
			if(carStatus.equals(Constant.CAR_STATUS_SALES_NO)){//已售
				sb.append("  AND A.LIFE_CYCLE=? ");
				par.add(Constant.VEHICLE_LIFE_04);
			}
		}
		if(!"".equals(yieldly)){
			sb.append("  AND B.AREA_ID=? ");
			par.add(yieldly);
		}
		if(!carNo.equals("")){
			sb.append("  AND A.LICENSE_NO LIKE ? ");
			par.add("%"+carNo+"%");
		}
		if(!cusName.equals("")){
			sb.append("  AND EXISTS (SELECT C.CTM_NAME FROM TT_DEALER_ACTUAL_SALES B,TT_CUSTOMER C WHERE B.CTM_ID=C.CTM_ID AND B.VEHICLE_ID=A.VEHICLE_ID AND C.CTM_NAME LIKE ? )");
			par.add("%"+cusName+"%");
		}
		if(!isPDI.equals("")){
			sb.append("  AND A.IS_PDI LIKE ? ");
			par.add("%"+isPDI+"%");
		}
		
		return this.pageQuery(sb.toString(), par, this.getFunName(), pageSize, curPage);
	}
	public List getVehcleCusDetial(long velId){
			StringBuffer sb=new StringBuffer();
		
		        sb.append("   SELECT A.VEHICLE_ID,A.VIN,A.ENGINE_NO,A.LICENSE_NO, ");
		        sb.append("   (SELECT B.GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP B WHERE B.GROUP_ID=A.SERIES_ID) AS CX_NAME, " );
		        sb.append("   (SELECT B.GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP B WHERE B.GROUP_ID=a.Package_Id) AS MODEL_NAME, " );
		        sb.append("   (SELECT C.CTM_NAME FROM TT_DEALER_ACTUAL_SALES B,TT_CUSTOMER C WHERE B.CTM_ID=C.CTM_ID AND B.VEHICLE_ID=A.VEHICLE_ID) AS CUS_NAME, " );
		        sb.append("   (SELECT TO_CHAR(B.SALES_DATE,'YYYY-MM-DD') FROM TT_DEALER_ACTUAL_SALES B WHERE B.VEHICLE_ID=A.VEHICLE_ID) AS SALES_DATE, ");
		        sb.append("   (SELECT B.IS_RETURN FROM TT_DEALER_ACTUAL_SALES B WHERE B.VEHICLE_ID=A.VEHICLE_ID) AS IS_RETURN, ");
		        sb.append("   TO_CHAR(A.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE,B.AREA_NAME,A.MILEAGE,A.FREE_TIMES,A.LIFE_CYCLE, " );
		        sb.append("   (SELECT C.PROVINCE FROM TT_DEALER_ACTUAL_SALES B,TT_CUSTOMER C WHERE B.CTM_ID=C.CTM_ID AND B.VEHICLE_ID=A.VEHICLE_ID) AS PROVINCE, ");
		        sb.append("   (SELECT C.CITY FROM TT_DEALER_ACTUAL_SALES B,TT_CUSTOMER C WHERE B.CTM_ID=C.CTM_ID AND B.VEHICLE_ID=A.VEHICLE_ID) AS CITY, " );
		        sb.append("   (SELECT C.ADDRESS FROM TT_DEALER_ACTUAL_SALES B,TT_CUSTOMER C WHERE B.CTM_ID=C.CTM_ID AND B.VEHICLE_ID=A.VEHICLE_ID) AS ADDRESS, " );
		        sb.append("   (SELECT B.GAME_CODE FROM TT_AS_WR_GAME B WHERE A.CLAIM_TACTICS_ID=B.ID) AS GAME_CODE ");
		        sb.append( "   FROM TM_VEHICLE A ,Tm_Business_Area B");
		        sb.append("  WHERE A.YIELDLY = B.AREA_ID AND  A.VEHICLE_ID=? ");
		        List par=new ArrayList();
		        par.add(velId);
		       return this.pageQuery(sb.toString(), par, this.getFunName());	
	}
	/********add by liuxh 20101117 增加车厂工单查询功能*********/
	//获得外出维修信息
	public List getOutInfo(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select *\n");
		sql.append("  from tt_as_wr_outrepair\n");  
		sql.append(" where ro_no = (select ro_no from tt_as_wr_application where id ="+id+")\n");
		return this.select(TtAsWrOutrepairPO.class, sql.toString(), null);
	}
	//获取工单的DEALER_ID
		public List<Map<String, Object>> getRepairOrder(String roId){
			StringBuffer sql = new StringBuffer("\n");
		   sql.append(" select r.dealer_id ID  from tt_as_repair_order r where r.id="+roId+" \n");
		   return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	//获取加价率
	public List<Map<String, Object>> selectPrice(){
		  StringBuilder sql= new StringBuilder();
		  sql.append("select * from tm_business_para p where p.type_code="+Constant.SALE_PRICE+" ");
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	  }
	  	
	/********Iverson add by 2010-12-08 判断vin码在库中是否存在*********/
	public int getCount(String vin){
		String sql = "select count(*) as count from tm_vehicle f where f.vin = '"+vin+"'";
		List<Map<String, Object>> list = this.pageQuery(sql, null, this.getFunName());
		return ((BigDecimal)list.get(0).get("COUNT")).intValue();
	}
	/********Iverson add by 2010-12-08 判断vin码在库中是否存在*********/
	
	/********yx add by 2010-12-17 查询经销提报车辆PIN申请*********/
	public PageResult<Map<String,Object>> getVehclePinSubmitRequest(String companyName,String vin,
			String pinCreateDate,String pinEndDate,String status,String pinCode,String dealerCode,Integer curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.PIN_ID,\n");
		sql.append("       A.DEALER_ID,\n");
		sql.append("       A.PIN_CODE,\n");
		sql.append("       A.MAKE_DATE,\n");
		sql.append("       A.VIN,\n");
		sql.append("       A.REMARK,\n");
		sql.append("       A.BACK_REMARK,\n");
		sql.append("       A.STATUS,\n");
		sql.append("       A.CREATE_BY,\n");
		sql.append("       A.CREATE_DATE,\n");
		sql.append("       C.COMPANY_CODE,\n");
		sql.append("       C.COMPANY_SHORTNAME,\n");
		sql.append("       E.DEALER_CODE,\n");
		sql.append("       E.DEALER_SHORTNAME,\n");
		sql.append("       (SELECT D.NAME FROM TC_USER D WHERE D.USER_ID=A.CREATE_BY) AS USER_NAME,\n");
		sql.append("       (select b.name from tc_user b where b.user_id=a.update_by) as user_by,a.update_date\n");
		sql.append("  FROM TM_VEHICLE_PIN_REQUEST A,TM_COMPANY C,TM_DEALER E\n");
		sql.append(" WHERE E.COMPANY_ID = C.COMPANY_ID\n");
		sql.append(" AND E.DEALER_ID=A.DEALER_ID\n");
		sql.append(" AND E.DEALER_TYPE='"+Constant.DEALER_TYPE_DWR+"'\n");
		
		if(Utility.testString(companyName)){
			sql.append(" AND C.COMPANY_NAME LIKE '%"+companyName+"%'\n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND A.VIN LIKE '%"+vin+"%'\n");		
		}
		if(Utility.testString(pinCreateDate)){
			sql.append(" and A.CREATE_DATE >= to_date('"+pinCreateDate+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(Utility.testString(pinEndDate)){
			sql.append(" AND A.CREATE_DATE <= to_date('"+pinEndDate+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(pinCode)){
			sql.append(" AND A.PIN_CODE LIKE '%"+pinCode+"%'\n");
		}
		if(Utility.testString(status)){
			sql.append(" AND A.STATUS="+status+"\n");		
		}
		if(Utility.testString(dealerCode)){
			sql.append(" AND E.DEALER_CODE='"+dealerCode+"'\n");		
		}
		sql.append(" ORDER BY A.CREATE_DATE DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), Constant.PAGE_SIZE, curPage);
		return ps;
	}
	/********yx add by 2010-12-17查询经销提报车辆PIN申请*********/
	/********yx add by 2010-12-17 查询经销提报车辆PIN申请明细*********/
	public Map<String,Object> getVehclePinDetail(String pinId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.PIN_ID,\n");
		sql.append("       A.DEALER_ID,\n");
		sql.append("       A.PIN_CODE,\n");
		sql.append("       A.MAKE_DATE,\n");
		sql.append("       A.VIN,\n");
		sql.append("       A.REMARK,\n");
		sql.append("       A.BACK_REMARK,\n");
		sql.append("       A.STATUS,\n");
		sql.append("       A.CREATE_BY,\n");
		sql.append("       A.CREATE_DATE,\n");
		sql.append("       C.DEALER_CODE,\n");
		sql.append("       C.DEALER_SHORTNAME,\n");
		sql.append("       D.NAME\n");
		sql.append("  FROM TM_VEHICLE_PIN_REQUEST A,TM_DEALER C,TC_USER D\n");
		sql.append(" WHERE A.PIN_ID="+pinId+"\n");
		sql.append(" AND D.USER_ID=A.CREATE_BY\n");
		sql.append(" AND A.DEALER_ID=C.DEALER_ID\n");
		sql.append(" AND C.DEALER_TYPE='"+Constant.DEALER_TYPE_DWR+"'\n");
//		sql.append(" AND A.CREATE_BY="+dealerId+"\n");
		Map<String, Object> ps = (Map<String, Object>) pageQuery(sql.toString(), null, this.getFunName()).get(0); 
		return ps;
	}
	/********yx add by 2010-12-17查询经销提报车辆PIN申请明细*********/
	/********yx add by 2010-12-17 查询经销提报车辆PIN申请明细*********/
	public int replyVehclePin(TmVehiclePinRequestPO pinReq,TmVehiclePinRequestPO pinReqValue){
		return this.update(pinReq, pinReqValue);
	}
	/********yx add by 2010-12-17查询经销提报车辆PIN申请明细*********/
	
	/**
	 * 
	 * @Title: queryPartCode
	 * @Description: TODO(根据三包规则查询配件)
	 * @param
	 * @param user
	 * @param
	 * @param con
	 * @param
	 * @param pageSize
	 * @param
	 * @param curPage
	 * @param
	 * @return 设定文件
	 * @return PageResult<TmPtPartBaseExtPO> 返回类型
	 * @throws
	 */
	public Map<String,Object> queryPartCodeByVin(AclUserBean user,
			Map<String, String> map) {
		String vin = map.get("vin").toString();
		String partCode = map.get("partCode")==null?"":map.get("partCode").toString();
//		String partName = map.get("partName")==null?"":map.get("partName").toString();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.PART_CODE,\n");
		sql.append("       p.PART_NAME,\n");
		sql.append("       D.ID AS LISTID,\n");
		sql.append("       D.CLAIM_MONTH,\n");
		sql.append("       D.CLAIM_MELIEAGE,\n");
		sql.append("       B.GAME_CODE,\n");
		sql.append("       B.GAME_NAME,\n");
		sql.append("       B.ID AS GAMEID,\n");
		sql.append("       C.RULE_CODE,\n");
		sql.append("       C.RULE_NAME,\n");
		sql.append("       C.ID AS RULEID\n");
		sql.append("  FROM TM_VEHICLE         A,\n");
		sql.append("       TT_AS_WR_GAME      B,\n");
		sql.append("       TT_AS_WR_RULE      C,\n");
		sql.append("       TT_AS_WR_RULE_LIST D,\n");
		sql.append("       Tm_Pt_Part_Base    p\n");
		sql.append(" WHERE A.CLAIM_TACTICS_ID = B.ID\n");
		sql.append("   AND B.RULE_ID = C.ID\n");
		sql.append("   AND C.ID = D.RULE_ID\n");
		sql.append("   and d.part_code = p.part_code\n");
		sql.append("   AND A.VIN = '"+vin+"'\n");
		sql.append(" AND D.PART_CODE = '"+partCode+"'\n");
		//sql.append("   AND A.DEALER_ID="+user.getDealerId()+"\n");
//		if(Utility.testString(partCode)){
//			sql.append(" AND D.PART_CODE LIKE '%"+partCode+"%'\n");
//		}
//		if(Utility.testString(partName)){
//			sql.append(" AND D.PART_NAME LIKE '%"+partName+"%'\n");
//		}

		Map<String,Object> ps = this.pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	/**
	 * 
	 * @Title: queryPartCode
	 * @Description: TODO(根据三包规则查询配件)
	 * @param查询通用三包规则配件
	 * @throws
	 */
	public Map<String,Object> queryUsualRulePartCode(String partCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.PART_CODE,\n");
		sql.append("       p.PART_NAME,\n");
		sql.append("       D.ID AS LISTID,\n");
		sql.append("       D.CLAIM_MONTH,\n");
		sql.append("       D.CLAIM_MELIEAGE,\n");
		sql.append("       B.GAME_CODE,\n");
		sql.append("       B.GAME_NAME,\n");
		sql.append("       B.ID AS GAMEID,\n");
		sql.append("       C.RULE_CODE,\n");
		sql.append("       C.RULE_NAME,\n");
		sql.append("       C.ID AS RULEID\n");
		sql.append("  FROM \n");
		sql.append("       TT_AS_WR_GAME      B,\n");
		sql.append("       TT_AS_WR_RULE      C,\n");
		sql.append("       TT_AS_WR_RULE_LIST D,\n");
		sql.append("      Tm_Pt_Part_Base    p\n");
		sql.append(" WHERE C.RULE_CODE='20060101'\n");
		sql.append("   AND B.RULE_ID = C.ID\n");
		sql.append("   AND C.ID = D.RULE_ID\n");
		sql.append("    and d.part_code = p.part_code\n");
		sql.append(" AND D.PART_CODE = '"+partCode+"'\n");
		//sql.append("   AND A.DEALER_ID="+user.getDealerId()+"\n");
//		if(Utility.testString(partCode)){
//			sql.append(" AND D.PART_CODE LIKE '%"+partCode+"%'\n");
//		}
//		if(Utility.testString(partName)){
//			sql.append(" AND D.PART_NAME LIKE '%"+partName+"%'\n");
//		}

		Map<String,Object> ps = this.pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	/**
	 * 
	 * @Title: queryPartCode
	 * @Description: TODO(查询配件)
	 * 通过配件CODE，配件名称查询配件代码（三包信息变更申请查询）
	 * @throws
	 */
	public PageResult<TmPtPartBaseExtPO> queryPartCodeForRuleChange(String partCode,String partName, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT(A.PART_CODE) AS PART_CODE,A.PART_ID,A.PART_NAME FROM TM_PT_PART_BASE A WHERE 1=1");

		if (Utility.testString(partCode)) {
			sql.append(" AND A.PART_CODE LIKE '%" + partCode
					+ "%' ");
		}
		if (Utility.testString(partName)) {
			sql.append(" AND A.PART_NAME LIKE '%" + partName
					+ "%' ");
		}
		//sqlStr.append(" GROUP ");
		// sqlStr.append(con);
		PageResult<TmPtPartBaseExtPO> ps = pageQuery(TmPtPartBaseExtPO.class,
				sql.toString(), null, pageSize, curPage);
		return ps;
	}
	
	public List<Map<String,String>> getRepairOrderPartCheck(String roNo) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.PART_TYPE_ID,A.PART_NO,A.PART_NAME,A.repairtypecode\n");
		sql.append("  FROM tt_as_ro_repair_part A, Tm_Pt_Part_Base B\n");
		sql.append(" WHERE A.RO_ID = '"+roNo+"'\n");
		sql.append("   AND B.PART_CODE=A.PART_NO");

		List<Map<String,String>> ps = this.pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	/**
	 * 查询重新审核索赔单是否正在开票中
	 */
	public List<Map<String,String>> checkClaimIsInvoice(String id) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tr_balance_claim_tmp a where a.claim_id="+id);

		List<Map<String,String>> ps = this.pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	  /*******
	   *   经销商进入DQV规则时删除其他自动审核规则产生的审核记录
	   * 
	   * @param id
	   */
	   public  void delAppStatus(String id){
	    	StringBuffer sql = new StringBuffer();
	    	sql.append(" delete from Tt_As_Wr_Appauthitem where id="+id);
	    	this.delete(sql.toString(), null);
	    }
	   
	   /************
	    *  根据USER_ID来获取经销商ID 与SESSION中取出来的DEALER_ID进行验证
	    * @param userId
	    */
	   public List<Map<String, Object>> validationDealerId(String userId){
		   StringBuffer sql= new StringBuffer();
		   sql.append("select d.dealer_id\n" );
		   sql.append("  from tc_user u, tm_dealer d\n" );
		   sql.append(" where u.user_id = "+userId+"\n" );
		   sql.append("   and u.company_id = d.company_id\n" );
		   sql.append("   and d.dealer_type = 10771002");
		   List<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName());
			return ps;
	   } 
	   
	   /**********
	    * 根据索赔单ID查询经销商ID和产地
	    * @param id
	    * @return
	    */
	   public List<Map<String, Object>> viewClaimDealer(String id){
		   StringBuffer sql= new StringBuffer();
		   sql.append("select d.dealer_id,\n" );
		   sql.append("       d.dealer_code,\n" );
		   sql.append("       d.dealer_name,\n" );
		   sql.append("       wa.yieldly,wa.claim_no as NO,\n" );
		   sql.append("       wa.balance_amount ,wa.repair_total,\n" );
		   sql.append("       nvl(wa.second_deductions,0) second_deductions\n" );
		   sql.append("  from Tt_As_Wr_Application wa, tm_dealer d\n" );
		   sql.append(" where wa.id = ?\n" );
		   sql.append("   and wa.dealer_id = d.dealer_id");
		   List addList = new ArrayList();
		   addList.add(id);
		   List<Map<String, Object>> ps = this.pageQuery(sql.toString(), addList, getFunName());
			return ps;
	   } 
	   
	   public List<Map<String, Object>> viewSpefe(String id){
		   StringBuffer sql= new StringBuffer();
		   sql.append("select d.dealer_id,\n" );
		   sql.append("       d.dealer_code,\n" );
		   sql.append("       d.dealer_name,\n" );
		   sql.append("       s.yield yieldly,\n" );
		   sql.append("       S.FEE_NO AS NO,\n" );
		   sql.append("       S.DECLARE_SUM balance_amount,\n" );
		   sql.append("       S.DECLARE_SUM1 repair_total,\n" );
		   sql.append("       NVL(S.second_deduction,0) second_deductions\n" );
		   sql.append("  from Tt_As_Wr_Spefee s, tm_dealer d\n" );
		   sql.append(" where s.id = ?\n" );
		   sql.append("   and s.dealer_id = d.dealer_id");
		   List addList = new ArrayList();
		   addList.add(id);
		   List<Map<String, Object>> ps = this.pageQuery(sql.toString(), addList, getFunName());
			return ps;
	   } 
	   
	 //获取工单的DEALER_ID
		public List<Map<String, Object>> barcodePrintDoGet(String dtlIds){
			StringBuffer sql = new StringBuffer("\n");
			 
			  
			//2013/1/28 yyh：因为需求微车也不根据配件大类统计所以修改统计方式和轿车一样（注释掉微车不再区分轿车微车）
			
//				  sql.append("SELECT distinct L.CODE_DESC,   case M.Is_Cliam when 10011002 then '是' else('否')  end as Is_Cliam,G.SERIAL_NUMBER,F.remark,P.DELIVERER,P.DELIVERER_PHONE,F.DOWN_PRODUCT_NAME DC_NAME,t.TROUBLE_TYPE,F.DOWN_PART_CODE,f.down_part_name,to_char(A.AUDITING_DATE,'yyyy-MM-dd') AUDITING_DATES ,A.*,  J.CODE_DESC YIELDLY_NAME,  B.DEALER_CODE,  B.DEALER_NAME, E.CODE_DESC CLAIM_NAME,    D.GROUP_CODE PACKAGE_NAME,  to_char(C.PURCHASED_DATE,'yyyy-MM-dd') PURCHASED_DATE ,   G.BARCODE_NO,    case when trim(p.group_id) is not null then '北京轿车' else '重庆轿车' end as JDJW,");
//				   sql.append(" c.product_date,b.is_dqv,b.phone,C.LICENSE_NO,  m.is_return AS is_return1 FROM TT_AS_WR_APPLICATION   A,  TM_DEALER              B,   TM_VEHICLE             C,  TM_VHCL_MATERIAL_GROUP D,  TC_CODE                E,   Tt_As_Wr_Partsitem     F, Tt_As_Wr_Partsitem_Barcode G,  TC_CODE J,Tt_As_Wr_Labouritem  T ,TT_AS_REPAIR_ORDER P, TM_PT_PART_BASE M , TC_CODE L ,tt_as_wr_bj_barcode_material p");
//				   //如果是为无零件就不打印条码
//				   sql.append(" WHERE 1 = 1   AND L.CODE_ID=A.YIELDLY AND  f.DOWN_PART_CODE  not in ('00-000','CV6000-00000','CV8000-00000','CV11000-00000') and  F.PART_CODE = M.PART_CODE(+)  and t.id=a.id AND  A.RO_NO = P.RO_NO and F.wr_labourcode = t.wr_labourcode  AND A.YIELDLY=J.CODE_ID(+) AND F.ID=A.ID AND F.PART_ID=G.PART_ID AND  F.PART_ID=G.PART_ID  AND A.DEALER_ID = B.DEALER_ID(+)  AND A.VIN = C.VIN(+) AND C.PACKAGE_ID = D.GROUP_ID(+)    AND A.CLAIM_TYPE = E.CODE_ID(+)   and a.model_id=p.group_id(+)   and F.QUANTITY>0 AND A.ID in ("+dtlIds+") order by  G.BARCODE_NO");
//				   
			

				  sql.append("SELECT distinct ba.area_name,   case M.Is_Cliam when 10011002 then '是' else('否')  end as Is_Cliam,G.SERIAL_NUMBER,F.remark AS F_REMARK,	f.trouble_reason f_reason,F.producer_code,P.DELIVERER,P.DELIVERER_PHONE,F.DOWN_PRODUCT_CODE DC_NAME,t.TROUBLE_TYPE,F.DOWN_PART_CODE,f.down_part_name,to_char(A.AUDITING_DATE,'yyyy-MM-dd') AUDITING_DATES ,A.*,  F.PRODUCER_NAME YIELDLY_NAME,  B.DEALER_CODE,  B.DEALER_SHORTNAME DEALER_NAME, E.CODE_DESC CLAIM_NAME,    D.GROUP_CODE PACKAGE_NAME,  to_char(C.PURCHASED_DATE,'yy-MM-dd') PURCHASED_DATE ,   G.BARCODE_NO,ba.area_name||'轿车' as  JDJW,");
				   sql.append(" TO_CHAR(C.PRODUCT_DATE, 'YY-MM-DD') PRODUCT_DATE,TO_CHAR(a.ro_startdate, 'YY-MM-DD') RO_CREATE_DATE,b.is_dqv,b.phone,C.LICENSE_NO,  m.is_return AS is_return1 FROM TT_AS_WR_APPLICATION   A,  TM_DEALER              B,   TM_VEHICLE             C,  TM_VHCL_MATERIAL_GROUP D,  TC_CODE                E,   Tt_As_Wr_Partsitem     F, Tt_As_Wr_Partsitem_Barcode G, Tt_As_Wr_Labouritem  T ,TT_AS_REPAIR_ORDER P, TM_PT_PART_BASE M ,tm_business_area   ba ");
				   //如果是为无零件就不打印条码
				   sql.append(" WHERE 1 = 1   AND ba.area_id(+) = A.YIELDLY AND  f.DOWN_PART_CODE  not in ('00-000','CV6000-00000','CV8000-00000','CV11000-00000') and  F.PART_CODE = M.PART_CODE(+)  and t.id(+)=a.id AND  A.RO_NO = P.RO_NO(+) and F.wr_labourcode = t.wr_labourcode AND F.ID=A.ID AND F.PART_ID=G.PART_ID   AND A.DEALER_ID = B.DEALER_ID(+)  AND A.VIN = C.VIN(+) AND C.PACKAGE_ID = D.GROUP_ID(+)    AND A.CLAIM_TYPE = E.CODE_ID(+)    and F.QUANTITY>0 AND A.ID in ("+dtlIds+") order by  G.BARCODE_NO");
			
		  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
		
		
		 //修改索赔单是否打印的状态
		public void updatePrint(String dtlIds){
			StringBuffer sql = new StringBuffer("\n");
		   sql.append("update TT_AS_WR_APPLICATION set is_print  ="+Constant.IF_TYPE_YES+" where id in  ("+dtlIds+") ");
		   dao.update(sql.toString(), null);
	}

	   /***********
	    * 查询旧件扣款
	    * @param claimId
	    * @return
	    */
	   public String viewOldDecude(String claimId){
		   StringBuffer sql= new StringBuffer();
		   sql.append("select nvl(sum(nvl(d.part_amount,0)+nvl(d.manhour_money,0)+nvl(d.material_money,0.0)),0) as old_decude from Tt_As_Wr_Deduct d where d.claim_id=2010102808246957");
		   Map<String, Object>  map = this.pageQueryMap(sql.toString(), null, this.getFunName());
		   if(map.get("OLD_DECUDE")!=null){
			   return map.get("OLD_DECUDE").toString();
		   }
		   else{
			   return "0";
		   }
	   }
	   public void insertMergeAdditem(String roId,String id){
		   StringBuffer sql= new StringBuffer();
		   TtAsRoAddItemPO po = new TtAsRoAddItemPO();
		   po.setRoId(Long.valueOf(id));
		   List<TtAsRoAddItemPO> listPo = this.select(po);
		   if(listPo.size()>0){
			   sql.append("insert into Tt_As_Ro_Add_Item\n" );
			   sql.append("   select F_GETID,\n" );
			   sql.append("          t.manage_sort_code,\n" );
			   sql.append("          t.add_item_code,\n" );
			   sql.append("          t.add_item_name,\n" );
			   sql.append("          t.add_item_amount,\n" );
			   sql.append("          t.charge_partition_code,\n" );
			   sql.append("          t.activity_code,\n" );
			   sql.append("          t.remark,\n" );
			   sql.append("          t.discount,t.create_by,t.create_date,t.update_by,t.update_date,"+roId+",t.is_sel,t.pay_type,t.is_claim\n" );
			   sql.append("     from Tt_As_Ro_Add_Item t\n" );
			   sql.append("    where t.RO_ID = "+id+"");
			   this.insert(sql.toString()); 
		   }
		   TtAsRepairOrderPO repairPo = new TtAsRepairOrderPO();
		   repairPo.setId(Long.valueOf(id));
		 List<TtAsRepairOrderPO> listr=  this.select(repairPo);
		 
		 TtAsRepairOrderPO repairPo1 = new TtAsRepairOrderPO();
		   repairPo1.setId(Long.valueOf(roId));
		 List<TtAsRepairOrderPO> listr1=  this.select(repairPo1);
		  TtAsWrOutrepairPO  poOut = new TtAsWrOutrepairPO();
		   poOut.setRoNo(listr.get(0).getRoNo());
		   List<TtAsWrOutrepairPO> listPoOut = this.select(poOut);
		   if(listPoOut.size()>0){
			   StringBuffer sql2= new StringBuffer();
			   sql2.append("insert into Tt_As_Wr_Outrepair\n" );
			   sql2.append("  select F_GETID,\n" );
			   sql2.append("         '"+listr1.get(0).getRoNo()+"',\n" );
			   sql2.append("         r.start_time,\n" );
			   sql2.append("         r.end_time,\n" );
			   sql2.append("         r.out_person,\n" );
			   sql2.append("         r.out_site,\n" );
			   sql2.append("         r.out_licenseno,\n" );
			   sql2.append("         r.from_adress,\n" );
			   sql2.append("         r.end_adress,\n" );
			   sql2.append("         r.out_mileage,\n" );
			   sql2.append("         r.create_date,\n" );
			   sql2.append("         r.create_by,\n" );
			   sql2.append("         r.update_date,\n" );
			   sql2.append("         r.update_by\n" );
			   sql2.append("    from Tt_As_Wr_Outrepair r\n" );
			   sql2.append("   where r.ro_no = '"+listr.get(0).getRoNo()+"'");
			   System.out.println("sql2");
			   this.insert(sql2.toString());
		   }
	   }
//添加无零件项 add  by tanv 2012-12-26
	public String addNoPartItems(String proId, String noPartItems, int is_gur, Long userId, String vin, String inMileage) {
		List<Object> inParameter = new ArrayList<Object>();// 输入参数
		List outParameter = new ArrayList();// 输出参数
		String rs = "";//执行结果
		if("".equals(proId)||null==proId||"".equals(noPartItems)||null==noPartItems){
			rs = "-1";
		}else{
			Long roid=Long.valueOf(proId);
			int num=Integer.valueOf(noPartItems);
			if(num==0){
				rs = "-2";
			}else{
				inParameter.add(roid);
				inParameter.add(num);
				inParameter.add(is_gur);
				inParameter.add(userId);
				inParameter.add(vin);
				inParameter.add(Double.valueOf(inMileage));
				outParameter.add(Types.VARCHAR);
				outParameter = dao.callProcedure("P_C_WR_ADDNOPARTITEMS", inParameter, outParameter);
				rs = outParameter.get(0)==null?"-1":outParameter.get(0).toString();//返回值
			}
		}
		return rs;
	}
	//判断配件是否三包内的通用规则
	public Integer partIsGua(String purchasedDate,String inMileage,String vin,String partCode)throws Exception{
		int isGua=0;
			WrRuleUtil util = new WrRuleUtil();
			if(purchasedDate==null||purchasedDate.equals("")){
				isGua= 0;
			}
			else{
				try {
					WarrantyPartVO wp = util.wrRuleCompute(inMileage, purchasedDate, vin, partCode);					
					 if(wp.getIsInWarranty() == Constant.IF_TYPE_YES){				 
						 isGua= 1; 
					 } 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		return isGua;
	} 
	
	//查询是否属于北京车型
	public List<Map<String, Object>> barcodePrintDoGetIsBJ(String groupId){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select id  From tt_as_wr_bj_barcode_material where group_id="+groupId);
		
	  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**
	 * @param logonUser
	 * @param tawap
	 * 添加维修工单update日志
	 * add by tanv 2013-03-07
	 */
	public String addUpdateLog(AclUserBean logonUser, TtAsRepairOrderPO tawap) {
		List<Object> inParameter = new ArrayList<Object>();// 输入参数
		List outParameter = new ArrayList();// 输出参数
		String rs = "-1";//执行结果
		inParameter.add(tawap.getId());
		inParameter.add(logonUser.getUserId());
		outParameter.add(Types.VARCHAR);
		outParameter = dao.callProcedure("P_C_RO_UPDATELOG", inParameter, outParameter);
		rs = outParameter.get(0)==null?"-1":outParameter.get(0).toString();//返回值
		return rs;
	}
	//查询工单新增按钮是否开启
	public List<Map<String, Object>> repairButton(String userId){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select b.is_insert From tm_dealer a,tm_company b where a.company_id=b.company_id and a.dealer_id="+userId);
		
	  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	//查询预授权审核明细
	public List<Map<String, Object>> authDetail(String vin,String ro_no){
		StringBuffer sql = new StringBuffer("\n");
		sql.append(" select F.AUDIT_PERSON,F.APPROVAL_LEVEL_CODE,f.AUDIT_LEVEL_NMAE,F.AUDIT_RESULT,fa.ro_no,fa.fo_no,\n");
		sql.append(" F.REMARK,TO_CHAR(F.AUDIT_DATE,'YYYY-MM-DD hh24:mi') AUDIT_DATE \n");
		sql.append("  from TT_AS_WR_FOREAUTHDETAIL f ,tt_as_wr_foreapproval fa \n");
		sql.append(" where 1=1  and f.fid = fa.id \n");
		sql.append(" and fa.vin='"+vin+"' and fa.ro_no='"+ro_no+"'\n");
		sql.append("  order by  f.create_date desc ");
	  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	//查询预授权审核明细
	public List<Map<String, Object>> authDetailVin(String roNo){
		StringBuffer sql = new StringBuffer("\n");
		sql.append(" select F.AUDIT_PERSON,F.APPROVAL_LEVEL_CODE,f.AUDIT_LEVEL_NMAE,F.AUDIT_RESULT,fa.ro_no,fa.fo_no,\n");
		sql.append(" F.REMARK,TO_CHAR(F.AUDIT_DATE,'YYYY-MM-DD') AUDIT_DATE \n");
		sql.append("  from TT_AS_WR_FOREAUTHDETAIL f ,tt_as_wr_foreapproval fa \n");
		sql.append(" where 1=1  and f.fid = fa.id \n");
		sql.append(" and fa.ro_no='"+roNo+"'\n");
		sql.append("  order by  fa.ro_no desc ,f.approval_level_code desc ");
	  return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	//查询故障代码
	public List<TtAsWrMalfunctionPO> getAllMalFunction(){
		StringBuffer sql = new StringBuffer("\n");
		sql.append(" select * from  TT_AS_WR_MALFUNCTION \n");
	  return dao.select(TtAsWrMalfunctionPO.class, sql.toString(), null);
	}
	//查询质损
	public List<TtAsWrQualityDamagePO> getAllQuality(){
		StringBuffer sql = new StringBuffer("\n");
		sql.append(" select d.qud_id,d.qud_code,d.qud_name||'--'||d.qud_son_name qud_name from TT_AS_WR_QUALITY_DAMAGE d  \n");
	  return dao.select(TtAsWrQualityDamagePO.class, sql.toString(), null);
	}
	
	public PageResult<TmPtPartBasePO> getAllPart(String code,String name, String PART_WAR_TYPE,int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select B.PART_ID ,B.PART_CODE,B.PART_NAME,B.PART_WAR_TYPE\n ");
		sql.append("  from TM_PT_PART_BASE B\n");
		sql.append(" WHERE B.IS_DEL=0\n");
		if(Utility.testString(code)){
			sql.append(" and B.PART_CODE Like '%"+code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(name)){
			sql.append(" and B.PART_NAME LIKE '%"+name+"%'\n");
		}
		if(Utility.testString(PART_WAR_TYPE)){
			sql.append(" and B.PART_WAR_TYPE = '"+PART_WAR_TYPE+"'\n");
		}
		sql.append(" order by b.part_id ");
		return pageQuery(TmPtPartBasePO.class, sql.toString(), null,
				pageSize, curPage);
	}
	
	
	public PageResult<TtAsWrMalfunctionPositionPO> getAllPosition(String code,String name, int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * \n ");
		sql.append("  from TT_AS_WR_MALFUNCTION_POSITION B\n");
		sql.append(" WHERE 1=1 \n");
		if(Utility.testString(code)){
			sql.append(" and B.POS_CODE LIKE '%"+code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(name)){
			sql.append(" and B.POS_NAME LIKE '%"+name+"%'\n");
		}
		sql.append(" and B.IS_DEL!=1");
		sql.append(" order by B.POS_ID ");
		return pageQuery(TtAsWrMalfunctionPositionPO.class, sql.toString(), null,
				pageSize, curPage);
	}
	
	
	
	public PageResult<TtAsWrKeyDesignPO> getAllKD(String code,String name, int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * \n ");
		sql.append("  from TT_AS_WR_KEY_DESIGN B\n");
		sql.append(" WHERE 1=1 \n");
		if(Utility.testString(code)){
			sql.append(" and B.KD_CODE like '%"+code.toUpperCase()+"%'\n");
		}
		if(Utility.testString(name)){
			sql.append(" and B.KD_NAME LIKE '%"+name+"%'\n");
		}
		sql.append(" order by B.KD_ID ");
		return pageQuery(TtAsWrKeyDesignPO.class, sql.toString(), null,
				pageSize, curPage);
	}
	
	
	public PageResult<Map<String, Object>> getBase(int pageSize,String PART_CODE,String PART_NAME, int curPage,
			String id) throws Exception {
		PageResult<Map<String, Object>> result = null;
//		StringBuilder sql = new StringBuilder();
//
//		sql.append("select q.*,(select count(*) from TT_AS_WR_MALFUNCTION_POSITION C\n");
//		sql.append(" where C.POS_ID=q.POS_ID and C.POS_ID=" + id
//				+ ") AS A\n");
//		sql.append(" from TM_PT_PART_BASE q where q.PART_WAR_TYPE != 94031002 and ( q.POS_ID = "+id+" or q.POS_ID is null) ");
//		
//		sql.append("  ORDER by A desc");
		
		StringBuffer sql= new StringBuffer();
		sql.append("select q.*, decode(c.pos_id, null, 0, 1) A\n" );
		sql.append("  from TM_PT_PART_BASE q, tt_as_wr_malfunction_position c\n" );
		sql.append(" where q.pos_id = c.POS_ID(+)\n" );
		sql.append("   and q.PART_WAR_TYPE != 94031002\n" );
		sql.append("   and (q.POS_ID = "+id+" or q.POS_ID is null)\n" );
		
		if(Utility.testString(PART_CODE))
		{
			sql.append(" and  q.PART_CODE like '%"+PART_CODE.toUpperCase()+"%'");
		}
		if(Utility.testString(PART_NAME)){
			sql.append(" and q.PART_NAME like '%"+PART_NAME+"%'");
		}
		sql.append(" ORDER by A desc, q.part_code");

		
		
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(),
				null, getFunName(), pageSize, curPage);
		return result;
	}
	
	public PageResult<Map<String, Object>> getBase(int pageSize,String PART_CODE,String PART_NAME,String create_start,String create_end, int curPage) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuilder sql = new StringBuilder();

		sql.append("select q.*\n");
		sql.append(" from TM_PT_PART_BASE q where q.IS_PART_NEW = 0 ");
		if(Utility.testString(PART_CODE))
		{
			sql.append(" and  q.PART_CODE like '%"+PART_CODE.toUpperCase()+"%'");
		}
		if(Utility.testString(PART_NAME)){
			sql.append(" and q.PART_NAME like '%"+PART_NAME+"%'");
		}
		if(Utility.testString(create_start)){
			sql.append(" and q.CREATE_DATE >= TO_DATE('"+create_start+" 00:00:00','YYYY-MM-DD HH24:MI:SS')");
		}
		if(Utility.testString(create_end)){
			sql.append(" and q.CREATE_DATE <= TO_DATE('"+create_end+" 23:59:59','YYYY-MM-DD HH24:MI:SS')");
		}
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(),
				null, getFunName(), pageSize, curPage);
		return result;
	}
	
	
	
	public PageResult<Map<String, Object>> getPosition(int pageSize, int curPage,
			String id) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuilder sql = new StringBuilder();

		sql.append("select q.*,(select count(*) from TT_AS_WR_KEY_DESIGN C\n");
		sql.append(" where C.KD_ID=q.KD_ID and C.KD_ID=" + id
				+ ") AS A\n");
		sql.append(" from TT_AS_WR_MALFUNCTION_POSITION q where (q.KD_ID is null OR q.KD_ID = "+id+") AND q.IS_DEL !=1 ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(),
				null, getFunName(), pageSize, curPage);
		return result;
	}
	
	public PageResult<Map<String, Object>> intercalate(int pageSize, int curPage,String dealerCode,String dealerName) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT (SELECT COUNT(*)\n" );
		sql.append("          from tm_dealer_invoice_intercalate C\n" );
		sql.append("         where C.DEALERID = B.DEALER_ID) mun,\n" );
		sql.append("       B.*\n" );
		sql.append("  from TM_DEALER B,\n" );
		sql.append("       (SELECT DISTINCT (t.PARENT_DEALER_D) PARENT_DEALER_D from TM_DEALER t) A\n" );
		sql.append(" where B.DEALER_ID = A.PARENT_DEALER_D\n" );
		sql.append("   and B.DEALER_TYPE = 10771002");
		if(Utility.testString(dealerCode))
		{
			sql.append(" AND B.DEALER_CODE like '%"+dealerCode+"%'");
		}
		
		if(Utility.testString(dealerName))
		{
			sql.append(" AND B.DEALER_NAME like '%"+dealerName+"%'");
		}

		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(),null, getFunName(), pageSize, curPage);
		return result;
	}
	
	public void basetoposition(String[] base,String[] ALL,String pid)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("update tm_pt_part_base t set POS_ID = "+pid+", t.ver = t.ver +1 where PART_ID in (");
		TmPtPartBasePO basePO= new TmPtPartBasePO();
		basePO.setPosId(Long.parseLong(pid));
		List<TmPtPartBasePO> list  = dao.select(basePO);
		
		List<String> palist = new ArrayList<String>();
		if(list != null && list.size()> 0 )
		{
			
			for(String all: ALL)
			{
				Boolean fag = false ; 
				if(base != null && base.length>0)
				{
					for(String baseid : base )
					{
						if(baseid.split("-")[0].equals(all))
						{
							fag = true;
							break;
						}
					}
				}
				
				if(!fag)
				{
					palist.add(all);
				}
				
			}
		}
		
		StringBuilder sql1 = new StringBuilder();
		sql1.append("update tm_pt_part_base t set POS_ID = null ,t.ver = t.ver +1  where PART_ID in (");
		if(palist != null && palist.size()>0)
		{
			for(int j = 0 ;palist.size()>j;j++)
			{
				sql1.append(palist.get(j)+",");
				if(j==palist.size()-1 )
				{
					sql1.append(palist.get(j)+")");
				}
			}
			dao.update(sql1.toString(), null);
		}
		
		
		
		
		if(base != null && base.length >0)
		{
			for(int i = 0 ;i<base.length; i++)
			{
				sql.append(base[i].split("-")[0]+",");
				if(i == base.length-1)
				{
					sql.append(base[i].split("-")[0]+")");
				}
			}
			dao.update(sql.toString(), null);
		}
		
	}
	
	
	public void positiontokd(String[] base,String pid)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("update TT_AS_WR_MALFUNCTION_POSITION t set KD_ID = "+pid+" , t.ver = t.ver +1 where POS_ID in (");
		TtAsWrMalfunctionPositionPO basePO= new TtAsWrMalfunctionPositionPO();
		basePO.setKdId(Long.parseLong(pid));
		List<TtAsWrMalfunctionPositionPO> list  = dao.select(basePO);
		
		List<String> palist = new ArrayList<String>();
		if(list != null && list.size()> 0 )
		{
			
			for(TtAsWrMalfunctionPositionPO partBasePO: list)
			{
				Boolean fag = false ; 
				for(String baseid : base )
				{
					if(baseid.split("-")[0].equals(""+partBasePO.getPosId()))
					{
						fag = true;
						break;
					}
				}
				if(!fag)
				{
					palist.add(""+partBasePO.getPosId());
				}
				
			}
		}
		
		StringBuilder sql1 = new StringBuilder();
		sql1.append("update TT_AS_WR_MALFUNCTION_POSITION t set KD_ID = null , t.ver = t.ver +1 where POS_ID in (");
		if(palist != null && palist.size()>0)
		{
			for(int j = 0 ;palist.size()>j;j++)
			{
				sql1.append(palist.get(j)+",");
				if(j==palist.size()-1 )
				{
					sql1.append(palist.get(j)+")");
				}
			}
			dao.update(sql1.toString(), null);
		}
		
		
		
		
		if(base != null && base.length >0)
		{
			for(int i = 0 ;i<base.length; i++)
			{
				sql.append(base[i].split("-")[0]+",");
				if(i == base.length-1)
				{
					sql.append(base[i].split("-")[0]+")");
				}
			}
			dao.update(sql.toString(), null);
		}
		
	}
	
	
	//故障代码查询
	public PageResult<TtAsWrMalfunctionBean> getAllMal(String code,String name, int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select m.mal_id, m.mal_code, m.mal_name\n");
		sql.append("  from TT_AS_WR_MALFUNCTION m\n");
		sql.append(" WHERE 1 = 1\n");
		if(Utility.testString(code)){
			sql.append(" and m.MAL_CODE like '%"+code+"%' ");
		}
		if(Utility.testString(name)){
			sql.append(" and m.MAL_NAME LIKE '%"+name+"%'  ");
		}
		sql.append(" order by m.MAL_ID ");
		return pageQuery(TtAsWrMalfunctionBean.class, sql.toString(), null,
				pageSize, curPage);
	}
	
	//质损区域查询
	public PageResult<TtAsWrQualityDamagePO> getAllQud(String code,String name,String sonCode,String sonName ,int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select m.qud_id,m.qud_code,m.qud_name,m.qud_son_code,m.qud_son_name  from TT_AS_WR_QUALITY_DAMAGE m ");
		sql.append(" WHERE 1=1 ");
		if(Utility.testString(code)){
			sql.append(" and m.QUD_CODE LIKE'%"+code+"%' ");
		}
		if(Utility.testString(name)){
			sql.append(" and m.QUD_NAME LIKE '%"+name+"%'  ");
		}
		if(Utility.testString(sonCode)){
			sql.append(" and m.QUD_SON_CODE LIKE '%"+sonCode+"%' ");
		}
		if(Utility.testString(sonName)){
			sql.append(" and m.QUD_SON_NAME LIKE '%"+sonName+"%'  ");
		}
		sql.append(" order by m.QUD_ID ");
		return pageQuery(TtAsWrQualityDamagePO.class, sql.toString(), null,
				pageSize, curPage);
	}
	//得到质损区域按大类分组
	public List<TtAsWrQualityDamagePO> getAllQudByCode(Long id){
		StringBuffer sql = new StringBuffer();
		sql.append(" select m.qud_code,m.qud_name  from TT_AS_WR_QUALITY_DAMAGE m ");
		sql.append(" WHERE 1=1 ");
		if(Utility.testString(id+"")){
			sql.append(" and m.qud_id="+id);
		}
		sql.append(" group by m.qud_code,m.qud_name");
		return this.select(TtAsWrQualityDamagePO.class, sql.toString(), null);
	}
	
	//故障代码新增修改时，级联质损大小类
	public List<Map<String, Object>> getQud(String code){
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct *  from TT_AS_WR_QUALITY_DAMAGE m ");
		sql.append(" WHERE 1=1 ");
		if(Utility.testString(code)){
			sql.append(" and m.qud_code='"+code+"'\n");
		}
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
	}
	//工单新增时，级联故障代码
	public List<Map<String, Object>> getMalQud(String code){
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct *  from tt_as_wr_malfunction m ");
		sql.append(" WHERE 1=1 ");
		if(Utility.testString(code)){
			sql.append(" and m.qud_id="+code+"\n");
		}
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
	}
	/*//取消结算时，更新结算时间为空
	public void updateBalanceDate(String id ){
		StringBuffer sql = new StringBuffer("\n");
		sql.append(" update tt_as_repair_order o set o.for_balance_time=null where o.id="+id);
		factory.update(sql.toString(), null);
	}*/
	
	//zhangyu 加入三包逻辑
 
	//车辆信息表进行比较看是否小于结束时间是否小于三包里程
	
	
   public  void vehicleJude(String vin,String in_mileage,String roCreateDate,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
   {
	   StringBuffer sql = new StringBuffer();
	   sql.append("select t.* from tm_vehicle t,TT_AS_WR_GAME b,tt_as_repair_order c where b.id = t.CLAIM_TACTICS_ID and  c.id = "+taropUp.getId()+"  and  t.vin=c.vin  AND t.WR_END_DATE >= to_date('"+roCreateDate+"','yyyy-mm-dd hh24:mi') AND b.WR_MELIEAGE >="+in_mileage);
	    List<TmVehiclePO> list= dao.select(TmVehiclePO.class, sql.toString(), null);
	    if(list !=null && list.size()>0)
	    {
	    	TmVehiclePO tmVehiclePO = list.get(0);
	    	Date date = tmVehiclePO.getWrEndDate();
	    	wrVinRulejude( date,roCreateDate,taropUp,logonUser);
	    }
   }
	
   
   //比较结束时间-开单时间+ 累计时间是否大于预警时间 如果有者取出最大
   
	public void wrVinRulejude(Date wr_end_date,String roCreateDate,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		try {
			TtAsWrVinRepairDaysPO daysPO1 = new TtAsWrVinRepairDaysPO();
			TtAsWrVinRepairDaysPO daysPO = null;
			 daysPO1.setVin(taropUp.getVin());
			 // 通过VIN查询三包车辆累计维修记录
			 List<TtAsWrVinRepairDaysPO>  listdays= dao.select(daysPO1);
			 if(listdays != null && listdays.size()>0)
			 {
				 daysPO = listdays.get(0);
			 }
			 int date = 0;
			 
			 // 活动维修工单结束时间与开单时间的差值, 如果该车辆已经维修， 有维修累计时间，则Constant.RO_STATUS_02相加 否则相减
			 if(daysPO !=null)
			 {
				 if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_01))
				 {
					 date = daysPO.getCurDays();
				 }else if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_02)){
					 date = Utility.compareDate1(roCreateDate,Utility.handleDate(taropUp.getForBalanceTime()) , 0) + daysPO.getCurDays();
				 }else if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_03)){
					 date = daysPO.getCurDays();
				 }
				
			 }else
			 {
				 date = Utility.compareDate1(roCreateDate,Utility.handleDate(taropUp.getForBalanceTime()) , 0);
			 }
			
			 StringBuffer sql = new StringBuffer();
			 sql.append("select * from TT_AS_WR_VIN_RULE t where t.vr_warranty <="+date+"  And t.vr_type ="+Constant.VR_TYPE_1+"  order by t.vr_warranty desc");
			 List<TtAsWrVinRulePO> list = dao.select(TtAsWrVinRulePO.class, sql.toString(),null );
			 TtAsWrFeeWarrantyPO feeWarrantyPO = new TtAsWrFeeWarrantyPO();
		     feeWarrantyPO.setRoNo(taropUp.getRoNo());
		     List<TtAsWrFeeWarrantyPO> listfee =  dao.select(feeWarrantyPO);
		     TtAsWrFeeWarrantyPO warrantyPO = null;
		     if(listfee != null && listfee.size()>0 )
		     {
		    	 warrantyPO = listfee.get(0);
		     }

			 if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_01))
				{
				    if(warrantyPO != null )
				    {
				    	TtAsWrFeeWarrantyPO feeWarrantyPO2 = new TtAsWrFeeWarrantyPO();
				    	feeWarrantyPO2.setWarId(warrantyPO.getWarId());
				    	TtAsWrFeeWarrantyPO asWrFeeWarrantyPO = new TtAsWrFeeWarrantyPO();
				    	asWrFeeWarrantyPO.setWarCountDays(Utility.compareDate1(roCreateDate, Utility.handleDate(new Date()), 0));
				    	dao.update(feeWarrantyPO2, asWrFeeWarrantyPO);
				    }
					if(daysPO != null)
					{
						TtAsWrVinRepairDaysPO daysPO2 = new TtAsWrVinRepairDaysPO();
						daysPO2.setVrdId(daysPO.getVrdId());

						TtAsWrVinRepairDaysPO asWrVinRepairDaysPO = new TtAsWrVinRepairDaysPO();
						Date beforDate = Utility.beforDate(taropUp.getForBalanceTime());
						
						int balance = Utility.compareDate1(roCreateDate, Utility.handleDate(taropUp.getForBalanceTime()), 0);
						int befor = Utility.compareDate1(roCreateDate, Utility.handleDate(beforDate), 0);
						int CurDays = daysPO.getCurDays();
						if(balance != befor)
						{
							CurDays = CurDays-1;
						}
						CurDays = CurDays + Utility.compareDate(Utility.handleDate(beforDate), Utility.handleDate(new Date()), 0);
						asWrVinRepairDaysPO.setCurDays(CurDays);
						asWrVinRepairDaysPO.setUpdateBy(logonUser.getUserId());
						asWrVinRepairDaysPO.setUpdateDate(new Date());
						dao.update(daysPO2, asWrVinRepairDaysPO);
						
					}
					if(list !=null && list.size() >0)
					 {
						 wrPepairdaysInsert(list.get(0),date,taropUp,logonUser);
					 }
				}else if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_02))
				{
					if(warrantyPO != null )
				    {
				    	TtAsWrFeeWarrantyPO feeWarrantyPO2 = new TtAsWrFeeWarrantyPO();
				    	feeWarrantyPO2.setWarId(warrantyPO.getWarId());
				    	TtAsWrFeeWarrantyPO asWrFeeWarrantyPO = new TtAsWrFeeWarrantyPO();
				    	asWrFeeWarrantyPO.setWarCountDays(Utility.compareDate1(roCreateDate, Utility.handleDate(new Date()), 0));
				    	dao.update(feeWarrantyPO2, asWrFeeWarrantyPO);
				    }
					if(daysPO != null)
					{
						TtAsWrVinRepairDaysPO daysPO2 = new TtAsWrVinRepairDaysPO();
						daysPO2.setVrdId(daysPO.getVrdId());
						//Date beforDate = Utility.beforDate(new Date());
						int balance = Utility.compareDate1(roCreateDate, Utility.handleDate(taropUp.getForBalanceTime()), 0);
					//	int befor = Utility.compareDate1(roCreateDate, Utility.handleDate(beforDate), 0);
//						if(befor < 0)
//						{
//							befor = 0;
//						}
						int curday =balance;
					
						int CurDays= daysPO.getCurDays()+curday;
						TtAsWrVinRepairDaysPO asWrVinRepairDaysPO = new TtAsWrVinRepairDaysPO();
						asWrVinRepairDaysPO.setCurDays(CurDays);
						asWrVinRepairDaysPO.setUpdateBy(logonUser.getUserId());
						asWrVinRepairDaysPO.setUpdateDate(new Date());
						dao.update(daysPO2, asWrVinRepairDaysPO);
					}else
					{
						 TtAsWrVinRepairDaysPO asWrVinRepairDaysPO = new TtAsWrVinRepairDaysPO();
						 long daysId = Utility.getLong(SequenceManager.getSequence(""));
						 asWrVinRepairDaysPO.setVrdId(daysId);
						 asWrVinRepairDaysPO.setVin(taropUp.getVin());
						 TmVehiclePO vehiclePO = new TmVehiclePO();
						 vehiclePO.setVin(taropUp.getVin());
						 
						 /*****add by liuxh 20131108判断车架号不能为空*****/
							CommonUtils.jugeVinNull(taropUp.getVin());
							/*****add by liuxh 20131108判断车架号不能为空*****/
						 
						 List<TmVehiclePO> listv= dao.select(vehiclePO);
						 if(listv!=null && listv.size()>0)
						 {
							 vehiclePO = listv.get(0);
							 long id=  vehiclePO.getClaimTacticsId();
							 TtAsWrGamePO gamePO = new TtAsWrGamePO();
							 gamePO.setId(id);
							 List<TtAsWrGamePO> gamelist= dao.select(gamePO);
							 if(gamelist != null && gamelist.size()>0)
							 {
								 asWrVinRepairDaysPO.setWrMonth(gamelist.get(0).getWrMonth());
								 asWrVinRepairDaysPO.setWrMileage(gamelist.get(0).getWrMelieage());
							 }
							 asWrVinRepairDaysPO.setBuyDate(vehiclePO.getPurchasedDate());
							 asWrVinRepairDaysPO.setWrEndDate(vehiclePO.getWrEndDate());

						 }
						 asWrVinRepairDaysPO.setCurMileage(taropUp.getInMileage());
						 asWrVinRepairDaysPO.setCurDays(date);
						 asWrVinRepairDaysPO.setCreateBy(logonUser.getUserId());
						 asWrVinRepairDaysPO.setCreateDate(new Date());
						 dao.insert(asWrVinRepairDaysPO);
						 
					}
					if(list !=null && list.size() >0)
					 {
						 wrPepairdaysInsert(list.get(0),date,taropUp,logonUser);
					 }
				}else if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_03))
				{
					if(warrantyPO != null )
				    {
				    	TtAsWrFeeWarrantyPO feeWarrantyPO2 = new TtAsWrFeeWarrantyPO();
				    	feeWarrantyPO2.setWarId(warrantyPO.getWarId());
				    	dao.delete(feeWarrantyPO2);
				    }
					if (daysPO != null) 
					{
						TtAsWrVinRepairDaysPO daysPO2 = new TtAsWrVinRepairDaysPO();
						daysPO2.setVrdId(daysPO.getVrdId());

						TtAsWrVinRepairDaysPO asWrVinRepairDaysPO = new TtAsWrVinRepairDaysPO();
						int CurDays= date -Utility.compareDate1(roCreateDate,Utility.handleDate(taropUp.getForBalanceTime()) , 0) ;
						asWrVinRepairDaysPO.setCurDays(CurDays);
						asWrVinRepairDaysPO.setUpdateBy(logonUser.getUserId());
						asWrVinRepairDaysPO.setUpdateDate(new Date());
						dao.update(daysPO2, asWrVinRepairDaysPO);
					}
					if(list !=null && list.size() >0)
					 {
						 wrPepairdaysInsert(list.get(0),date ,taropUp,logonUser);
					 }
				}
				 
			 

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 		
		 
	}
	// 得到车主的ID
	public String getPhone(String vin) throws Exception
	{
		TmVehiclePO tmVehiclePO = new TmVehiclePO();
		tmVehiclePO.setVin(vin);
		
		/*****add by liuxh 20131108判断车架号不能为空*****/
		CommonUtils.jugeVinNull(vin);
		/*****add by liuxh 20131108判断车架号不能为空*****/
		
		 List<TmVehiclePO> list= dao.select(tmVehiclePO);
		 if(list != null && list.size()>0)
		 {
			 tmVehiclePO = list.get(0);
			 long id = tmVehiclePO.getVehicleId();
			 TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
			 actualSalesPO.setVehicleId(id);
			 List<TtDealerActualSalesPO> listPo = dao.select(actualSalesPO);
			 if(listPo != null && listPo.size()>0)
			 {
				 actualSalesPO =  listPo.get(0);
				 long cid= actualSalesPO.getCtmId();
				 TtCustomerPO customerPO = new TtCustomerPO();
				 customerPO.setCtmId(cid);
				 List<TtCustomerPO> listct= dao.select(customerPO);
				 if(listct != null && listct.size()>0)
				 {
					 return listct.get(0).getMainPhone();
				 }
				 
			 }
		 }
		 return "";
	}
	//插入预警流水表
	
	public void wrPepairdaysInsert(TtAsWrVinRulePO asWrVinRulePO,int date,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		TtAsWrWarrantyRecordPO asWrWarrantyRecordPO = new TtAsWrWarrantyRecordPO();
		try {
			 long warId = Utility.getLong(SequenceManager.getSequence(""));
			 asWrWarrantyRecordPO.setWarId(warId);
			 asWrWarrantyRecordPO.setWarNo("YJ"+warId);
			 asWrWarrantyRecordPO.setWarDate(new Date());
			 asWrWarrantyRecordPO.setVin(taropUp.getVin());
			 asWrWarrantyRecordPO.setLicenseNo(taropUp.getLicense());
			 asWrWarrantyRecordPO.setVrId(asWrVinRulePO.getVrId());
			 asWrWarrantyRecordPO.setRoNo(taropUp.getRoNo());
			 asWrWarrantyRecordPO.setDealerId(Long.parseLong(logonUser.getDealerId()));
			 asWrWarrantyRecordPO.setWarLevel(asWrVinRulePO.getVrLevel());
			 asWrWarrantyRecordPO.setWarType(asWrVinRulePO.getVrType());
			 asWrWarrantyRecordPO.setCurLawDays(asWrVinRulePO.getVrLaw());
			 asWrWarrantyRecordPO.setCurWrDays(asWrVinRulePO.getVrWarranty());
			 asWrWarrantyRecordPO.setCurWrStandard(asWrVinRulePO.getVrLawStandard());
			 asWrWarrantyRecordPO.setWarCountDays(date);
			 asWrWarrantyRecordPO.setWarMileage(taropUp.getInMileage());
			 asWrWarrantyRecordPO.setWarCountTimes(1);
			 asWrWarrantyRecordPO.setCtmName(taropUp.getOwnerName());
			 asWrWarrantyRecordPO.setMainPhone(getPhone(taropUp.getVin()));
			 asWrWarrantyRecordPO.setWarRoUserId(logonUser.getUserId());
			 asWrWarrantyRecordPO.setWarRoType(taropUp.getRoStatus());
			 asWrWarrantyRecordPO.setCreateBy(logonUser.getUserId());
			 asWrWarrantyRecordPO.setCreateDate(new Date());
			 dao.insert(asWrWarrantyRecordPO);
			 uporinWarranty(asWrWarrantyRecordPO,date,logonUser);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//插入工单信息表并更新车辆累计维修天数表
	public void uporinWarranty(TtAsWrWarrantyRecordPO asWrWarrantyRecordPO,int date,AclUserBean logonUser)
	{
		try {
			TtAsWrWarrantyPO asWrWarrantyPO = new TtAsWrWarrantyPO();
			StringBuffer sql = new StringBuffer();
			//判断工单信息表中该工单是否存在
			sql.append("select * from tt_as_wr_warranty t where  t.ro_no = '"+asWrWarrantyRecordPO.getRoNo()+"' and t.WAR_PART_ID is null");
			List<TtAsWrWarrantyPO> list = dao.select(TtAsWrWarrantyPO.class,sql.toString(),null);
			boolean fage = false;
			
			if(list != null && list.size()>0)
			{
				fage = true;
				asWrWarrantyPO = list.get(0);
			}
			
			TtAsWrWarrantyPO ttAsWrWarrantyPO = new TtAsWrWarrantyPO();
			
			TtAsWrVinRepairDaysPO daysPO = new TtAsWrVinRepairDaysPO();
			daysPO.setVin(asWrWarrantyRecordPO.getVin());
			 List<TtAsWrVinRepairDaysPO>  listdays= dao.select(daysPO);
			 if(listdays != null && listdays.size()>0)
			 {
				 daysPO = listdays.get(0);
			 }
			 TtAsWrVinRepairDaysPO asWrVinRepairDaysPO =  new TtAsWrVinRepairDaysPO();
			//Constant.RO_STATUS_01 表示取消结算时触发//Constant.RO_STATUS_02 表示结算时触发////Constant.RO_STATUS_03 表示废弃时触发
			if((""+asWrWarrantyRecordPO.getWarRoType()).equals(""+Constant.RO_STATUS_01))
			{
				if(fage)
				{
					TtAsWrWarrantyPO asWrWarrantyPO2 = new TtAsWrWarrantyPO();
					asWrWarrantyPO2.setWarId(asWrWarrantyPO.getWarId());
					ttAsWrWarrantyPO.setUpdateBy(logonUser.getUserId());
					ttAsWrWarrantyPO.setUpdateDate(new Date());
					ttAsWrWarrantyPO.setWarCountDays(date);
					ttAsWrWarrantyPO.setPoType(asWrWarrantyRecordPO.getWarRoType());
					ttAsWrWarrantyPO.setWarCountTimes(asWrWarrantyPO.getWarCountTimes()-1);
					dao.update(asWrWarrantyPO2, ttAsWrWarrantyPO);
				}
			}else if((""+asWrWarrantyRecordPO.getWarRoType()).equals(""+Constant.RO_STATUS_02))
			{
				if(fage)
				{
					TtAsWrWarrantyPO asWrWarrantyPO2 = new TtAsWrWarrantyPO();
					asWrWarrantyPO2.setWarId(asWrWarrantyPO.getWarId());
					
					ttAsWrWarrantyPO.setUpdateBy(logonUser.getUserId());
					ttAsWrWarrantyPO.setUpdateDate(new Date());
					ttAsWrWarrantyPO.setWarCountDays(date);
					ttAsWrWarrantyPO.setPoType(asWrWarrantyRecordPO.getWarRoType());
					ttAsWrWarrantyPO.setWarCountTimes(asWrWarrantyPO.getWarCountTimes()+1);
					dao.update(asWrWarrantyPO2, ttAsWrWarrantyPO);
					TtAsWrVinRepairDaysPO daysPO2 = new TtAsWrVinRepairDaysPO();
					daysPO2.setVrdId(daysPO.getVrdId());
					asWrVinRepairDaysPO.setCurLawDays(asWrWarrantyRecordPO.getCurLawDays());
					asWrVinRepairDaysPO.setCurWrDays(asWrWarrantyRecordPO.getCurWrDays());
					dao.update(daysPO2, asWrVinRepairDaysPO);
				}else
				{
					 long warId = Utility.getLong(SequenceManager.getSequence(""));
					 asWrWarrantyPO.setWarId(warId);
					 asWrWarrantyPO.setWarNo("YJ"+warId);
					 asWrWarrantyPO.setWarDate(new Date());
					 asWrWarrantyPO.setVin(asWrWarrantyRecordPO.getVin());
					 asWrWarrantyPO.setLicenseNo(asWrWarrantyRecordPO.getLicenseNo());
					 asWrWarrantyPO.setVrId(asWrWarrantyRecordPO.getVrId());
					 asWrWarrantyPO.setRoNo(asWrWarrantyRecordPO.getRoNo());
					 asWrWarrantyPO.setDealerId(asWrWarrantyRecordPO.getDealerId());
					 asWrWarrantyPO.setWarLevel(asWrWarrantyRecordPO.getWarLevel());
					 asWrWarrantyPO.setWarType(asWrWarrantyRecordPO.getWarType());
					 asWrWarrantyPO.setCurLawDays(asWrWarrantyRecordPO.getCurLawDays());
					 asWrWarrantyPO.setCurWrDays(asWrWarrantyRecordPO.getCurWrDays());
					 asWrWarrantyPO.setCurWrStandard(asWrWarrantyRecordPO.getCurWrStandard());
					 asWrWarrantyPO.setWarCountDays(date);
					 asWrWarrantyPO.setPoType(asWrWarrantyRecordPO.getWarRoType());
					 asWrWarrantyPO.setWarMileage(asWrWarrantyRecordPO.getWarMileage());
					 asWrWarrantyPO.setWarCountTimes(1);
					 asWrWarrantyPO.setCtmName(asWrWarrantyRecordPO.getCtmName());
					 asWrWarrantyPO.setMainPhone(asWrWarrantyRecordPO.getMainPhone());
					 asWrWarrantyPO.setCreateBy(asWrWarrantyRecordPO.getCreateBy());
					 asWrWarrantyPO.setCreateDate(asWrWarrantyRecordPO.getCreateDate());
					 dao.insert(asWrWarrantyPO);
					 TtAsWrVinRepairDaysPO daysPO2 = new TtAsWrVinRepairDaysPO();
					daysPO2.setVrdId(daysPO.getVrdId());
					asWrVinRepairDaysPO.setCurLawDays(asWrWarrantyRecordPO.getCurLawDays());
					asWrVinRepairDaysPO.setCurWrDays(asWrWarrantyRecordPO.getCurWrDays());
					dao.update(daysPO2, asWrVinRepairDaysPO);
					 
				}
			}else if((""+asWrWarrantyRecordPO.getWarRoType()).equals(""+Constant.RO_STATUS_03))
			{
				if (fage) 
				{
					TtAsWrWarrantyPO asWrWarrantyPO2 = new TtAsWrWarrantyPO();
					asWrWarrantyPO2.setWarId(asWrWarrantyPO.getWarId());
					dao.delete(asWrWarrantyPO2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void baseJude(String[] PayTypePart,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		 StringBuffer sql = new StringBuffer();
		 sql.append("select t.* from tm_vehicle t,TT_AS_WR_GAME b,tt_as_repair_order c where b.id = t.CLAIM_TACTICS_ID and  c.id = "+taropUp.getId()+"   and b.id = 2010093000889551 and  t.vin=c.vin  AND t.WR_END_DATE >= to_date('"+Utility.handleDate(taropUp.getRoCreateDate())+"','yyyy-mm-dd hh24:mi') AND b.WR_MELIEAGE >="+taropUp.getInMileage());
		    List<TmVehiclePO> list= dao.select(TmVehiclePO.class, sql.toString(), null);
		    if(list !=null && list.size()>0)
		    {
		    	TmVehiclePO tmVehiclePO = list.get(0);
		    	Date date = tmVehiclePO.getPurchasedDate();
		    	basePart(date,PayTypePart,taropUp,logonUser);
		    }
	}
	public void bourjude(String[] PayTypePart,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		 StringBuffer sql = new StringBuffer();
		 sql.append("select t.* from tm_vehicle t,TT_AS_WR_GAME b,tt_as_repair_order c where b.id = t.CLAIM_TACTICS_ID and  c.id = "+taropUp.getId()+"   and b.id = 2010093000889551 and  t.vin=c.vin  AND t.WR_END_DATE >= to_date('"+Utility.handleDate(taropUp.getRoCreateDate())+"','yyyy-mm-dd hh24:mi') AND b.WR_MELIEAGE >="+taropUp.getInMileage());
		    List<TmVehiclePO> list= dao.select(TmVehiclePO.class, sql.toString(), null);
		    if(list !=null && list.size()>0)
		    {
		    	TmVehiclePO tmVehiclePO = list.get(0);
		    	Date date = tmVehiclePO.getPurchasedDate();
		    	bourPart(date,PayTypePart,taropUp,logonUser);
		    }
	}
	
	public void bourPart(Date date,String[] PayTypePart,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		List<String> list = new ArrayList<String>();
		for(String PayTypePart1 : PayTypePart)
		{
			if(PayTypePart1.split("-")[0].equals(""+(int)Constant.PAY_TYPE_02))
			{
				list.add(PayTypePart1.split("-")[1]);
			}
		}
		
		tmbourPart(list,date,taropUp,logonUser);
	}
	

	public void tmbourPart(List<String> list ,Date date,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		if(list !=null && list.size()>0)
		{
			StringBuffer sb= new StringBuffer();
			for(int i = 0 ;i < list.size();i++)
			{
				if(list.size()-1 == i)
				{
					sb.append(list.get(i));
				}else
				{
					sb.append(list.get(i)+",");
				}
			}
			StringBuffer sql= new StringBuffer();
			sql.append("select * from tt_as_ro_labour t where t.id in("+sb.toString()+")");
			
			List<TtAsRoLabourPO> listpo = dao.select( TtAsRoLabourPO.class, sql.toString(), null) ;
			for(TtAsRoLabourPO bourPO : listpo )
			{
				//工时预警累计时限取值=当前时间-维修工单开单时间
				int alladte= Utility.compareDate1(Utility.handleDate(new Date()),Utility.handleDate(taropUp.getRoCreateDate()),0 );
					tmbourrule(alladte,bourPO,taropUp,logonUser);
			}
			
		}
	}
	public void tmbourrule(int alladte,TtAsRoLabourPO bourPO,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
			TtAsWrVinRulePO rulePO = new TtAsWrVinRulePO();
			rulePO.setPartWrType(Integer.parseInt(Constant.PART_WR_TYPE_1));
			List<TtAsWrVinRulePO> list= dao.select(rulePO);
			String Labourcode= bourPO.getWrLabourcode();
			TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
			repairTimesPO.setPartCode(Labourcode);
			repairTimesPO.setVin(taropUp.getVin());
			List<TtAsWrVinPartRepairTimesPO> listPo= dao.select(repairTimesPO);
			insertorupdatebour(alladte,list,listPo,bourPO,taropUp,logonUser);
	}
	public void insertorupdatebour(int alladte,List<TtAsWrVinRulePO> list,List<TtAsWrVinPartRepairTimesPO> listPo,TtAsRoLabourPO bourPO,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		try {
			if(list != null && list.size()>0)
			{
				
				if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_01))
				{
					if(listPo != null && listPo.size()> 0)
					{
						TtAsWrVinPartRepairTimesPO timesPO = listPo.get(0);
						TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO.setVrtId(timesPO.getVrtId());
						TtAsWrVinPartRepairTimesPO repairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO1.setUpdateBy(logonUser.getUserId());
						repairTimesPO1.setUpdateDate(new Date());
						repairTimesPO1.setCurTimes(timesPO.getCurTimes()-1);
						timesPO.setCurTimes(timesPO.getCurTimes()-1);
						dao.update(repairTimesPO, repairTimesPO1);
					   juderuleinsertbour(alladte, list, timesPO, bourPO, taropUp, logonUser,2);
					}
				}else if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_02))
				{
					
					if(listPo != null && listPo.size()> 0)
					{
						TtAsWrVinPartRepairTimesPO timesPO = listPo.get(0);
						TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO.setVrtId(timesPO.getVrtId());
						TtAsWrVinPartRepairTimesPO repairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO1.setUpdateBy(logonUser.getUserId());
						repairTimesPO1.setUpdateDate(new Date());
						repairTimesPO1.setCurTimes(timesPO.getCurTimes()+1);
						timesPO.setCurTimes(timesPO.getCurTimes()+1);
						dao.update(repairTimesPO, repairTimesPO1);
					   juderuleinsertbour(alladte, list, timesPO, bourPO, taropUp, logonUser,1);
					}else
					{
						 TtAsWrVinPartRepairTimesPO asWrVinPartRepairTimesPO2 = new TtAsWrVinPartRepairTimesPO();
						 asWrVinPartRepairTimesPO2.setVrtId(Utility.getLong(SequenceManager.getSequence("")));
						 asWrVinPartRepairTimesPO2.setVin(taropUp.getVin());
						 TmVehiclePO vehiclePO = new TmVehiclePO();
						 String sql = "SELECT * FROM Tm_Vehicle WHERE 1=1  AND Vin= '"+taropUp.getVin()+"'";
						 List<TmVehiclePO> listv= dao.select(TmVehiclePO.class,sql,null);
						 if(listv!=null && listv.size()>0)
						 {
							 vehiclePO = listv.get(0);
							 long id=  vehiclePO.getClaimTacticsId();
							 TtAsWrGamePO gamePO = new TtAsWrGamePO();
							 gamePO.setId(id);
							 List<TtAsWrGamePO> gamelist= dao.select(gamePO);
							 if(gamelist != null && gamelist.size()>0)
							 {
								 asWrVinPartRepairTimesPO2.setWrMonth(gamelist.get(0).getWrMonth());
								 asWrVinPartRepairTimesPO2.setWrMileage(gamelist.get(0).getWrMelieage());
							 }
							 asWrVinPartRepairTimesPO2.setBuyDate(vehiclePO.getPurchasedDate());
							 asWrVinPartRepairTimesPO2.setWrEndDate(vehiclePO.getWrEndDate());
						 }
						 asWrVinPartRepairTimesPO2.setCurMileage(taropUp.getInMileage());
						 asWrVinPartRepairTimesPO2.setPartCode(bourPO.getWrLabourcode());
						 asWrVinPartRepairTimesPO2.setPartName(bourPO.getWrLabourname());
						 asWrVinPartRepairTimesPO2.setCurTimes(1);
						 asWrVinPartRepairTimesPO2.setCreateBy(logonUser.getUserId());
						 asWrVinPartRepairTimesPO2.setCreateDate(new Date());
						 dao.insert(asWrVinPartRepairTimesPO2);
						juderuleinsertbour(alladte, list, asWrVinPartRepairTimesPO2, bourPO, taropUp, logonUser,1);
					}
					
					
				}else if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_03))
				{
					if(listPo != null && listPo.size()> 0)
					{
						TtAsWrVinPartRepairTimesPO timesPO = listPo.get(0);
						TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO.setVrtId(timesPO.getVrtId());
						TtAsWrVinPartRepairTimesPO repairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO1.setUpdateBy(logonUser.getUserId());
						repairTimesPO1.setUpdateDate(new Date());
						repairTimesPO1.setCurTimes(timesPO.getCurTimes()-1);
						timesPO.setCurTimes(timesPO.getCurTimes()-1);
						dao.update(repairTimesPO, repairTimesPO1);
						
						TtAsWrWarrantyPO warrantyPO = new TtAsWrWarrantyPO();
						warrantyPO.setRoNo(taropUp.getRoNo());
						warrantyPO.setWarPartId(bourPO.getId());
					    List<TtAsWrWarrantyPO> listwr = dao.select(warrantyPO);
					    if(listwr != null && listwr.size()>0)
					    {
						   warrantyPO = listwr.get(0);
						   TtAsWrWarrantyPO warrantyPO1 = new TtAsWrWarrantyPO();
						   warrantyPO1.setWarId(warrantyPO.getWarId());
						   
						   TtAsWrWarrantyRecordPO recordPO = new TtAsWrWarrantyRecordPO();
						   recordPO.setVin(warrantyPO.getVin());
						   recordPO.setWarPartId(warrantyPO.getWarPartId());
						   if(dao.select(recordPO).size() == 1)
						   {
							   dao.delete(warrantyPO1);
						   }
					    }
					    juderuleinsertbour(alladte, list, timesPO, bourPO, taropUp, logonUser,0);
					}
				}
			}
			
		} catch (Exception e) {
		}
	}
	
	
	
	public void juderuleinsertbour(int alladte,List<TtAsWrVinRulePO> list,TtAsWrVinPartRepairTimesPO basePO,TtAsRoLabourPO bourPO,TtAsRepairOrderPO taropUp,AclUserBean logonUser,int type)
	{
		try {
				if(list != null && list.size()>0)
				{
					TtAsWrVinRulePO asWrVinRulePO = null;
					TtAsWrVinRulePO asWrVinRulePO1 = null;
					int CurTimes= basePO.getCurTimes();
					int i = 0;
					for(TtAsWrVinRulePO rulePO : list)
					{
						if(rulePO.getVrWarranty() == CurTimes)
						{
							asWrVinRulePO = rulePO; 
							i = rulePO.getVrWarranty();
							break;
						}else
						{
							if(rulePO.getVrWarranty() > i)
							{
								i = rulePO.getVrWarranty();
								asWrVinRulePO1 = rulePO; 
							}
						}
					}
					if(!(asWrVinRulePO == null && CurTimes <= i ))
					{
						if(asWrVinRulePO == null)
						{
							asWrVinRulePO = asWrVinRulePO1;
						}
						 TtAsWrWarrantyRecordPO asWrWarrantyRecordPO = new TtAsWrWarrantyRecordPO();
						 long warId = Utility.getLong(SequenceManager.getSequence(""));
						 asWrWarrantyRecordPO.setWarId(warId);
						 asWrWarrantyRecordPO.setWarNo("YJ"+warId);
						 asWrWarrantyRecordPO.setWarDate(new Date());
						 asWrWarrantyRecordPO.setVin(taropUp.getVin());
						 asWrWarrantyRecordPO.setLicenseNo(taropUp.getLicense());
						 asWrWarrantyRecordPO.setVrId(asWrVinRulePO.getVrId());
						 asWrWarrantyRecordPO.setRoNo(taropUp.getRoNo());
						 asWrWarrantyRecordPO.setDealerId(Long.parseLong(logonUser.getDealerId()));
						 asWrWarrantyRecordPO.setWarLevel(asWrVinRulePO.getVrLevel());
						 asWrWarrantyRecordPO.setWarType(asWrVinRulePO.getVrType());
						 asWrWarrantyRecordPO.setWarPartId(bourPO.getId());
						 asWrWarrantyRecordPO.setCurLawTimes(asWrVinRulePO.getVrLaw());
						 asWrWarrantyRecordPO.setCurWrTimes(asWrVinRulePO.getVrWarranty());
						 asWrWarrantyRecordPO.setCurWrStandard(asWrVinRulePO.getVrLawStandard());
						 asWrWarrantyRecordPO.setWarCountDays(alladte);
						 asWrWarrantyRecordPO.setWarMileage(taropUp.getInMileage());
						 asWrWarrantyRecordPO.setWarCountTimes(1);
						 asWrWarrantyRecordPO.setCtmName(taropUp.getOwnerName());
						 asWrWarrantyRecordPO.setMainPhone(getPhone(taropUp.getVin()));
						 asWrWarrantyRecordPO.setWarRoUserId(logonUser.getUserId());
						 asWrWarrantyRecordPO.setWarRoType(taropUp.getRoStatus());
						 asWrWarrantyRecordPO.setCreateBy(logonUser.getUserId());
						 asWrWarrantyRecordPO.setCreateDate(new Date());
						 dao.insert(asWrWarrantyRecordPO);
						 TtAsWrVinPartRepairTimesPO asWrVinPartRepairTimesPO = new TtAsWrVinPartRepairTimesPO();
						 asWrVinPartRepairTimesPO.setVrtId(basePO.getVrtId());
						 TtAsWrVinPartRepairTimesPO asWrVinPartRepairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
						 asWrVinPartRepairTimesPO1.setCurLawTimes(asWrVinRulePO.getVrLaw());
						 asWrVinPartRepairTimesPO1.setCurWrTimes(asWrVinRulePO.getVrWarranty());
						 dao.update(asWrVinPartRepairTimesPO, asWrVinPartRepairTimesPO1);
						 if(type != 0)
						 {
							    TtAsWrWarrantyPO warrantyPO = new TtAsWrWarrantyPO();
								warrantyPO.setRoNo(taropUp.getRoNo());
								warrantyPO.setWarPartId(bourPO.getId());
							    List<TtAsWrWarrantyPO> listwr = dao.select(warrantyPO);
							    if(listwr != null && listwr.size()>0)
							    {
							    	 warrantyPO = listwr.get(0);
							    	 TtAsWrWarrantyPO asWrWarrantyPO = new TtAsWrWarrantyPO();
							    	 asWrWarrantyPO.setWarId(warrantyPO.getWarId());
							    	 TtAsWrWarrantyPO asWrWarrantyPO1 = new TtAsWrWarrantyPO();
							    	 if(type == 2)
							    	 {
							    		 asWrWarrantyPO1.setPoType(Constant.RO_STATUS_01);
							    	 }else
							    	 {
							    		 asWrWarrantyPO1.setPoType(Constant.RO_STATUS_02);
							    	 }
							    	 dao.update(asWrWarrantyPO, asWrWarrantyPO1);
							    }else
							    {
							    	 TtAsWrWarrantyPO asWrWarrantyPO = new TtAsWrWarrantyPO();
							    	long warId1 = Utility.getLong(SequenceManager.getSequence(""));
									 asWrWarrantyPO.setWarId(warId1);
									 asWrWarrantyPO.setWarNo("YJ"+warId1);
									 asWrWarrantyPO.setWarDate(new Date());
									 asWrWarrantyPO.setVin(asWrWarrantyRecordPO.getVin());
									 asWrWarrantyPO.setLicenseNo(asWrWarrantyRecordPO.getLicenseNo());
									 asWrWarrantyPO.setVrId(asWrWarrantyRecordPO.getVrId());
									 asWrWarrantyPO.setRoNo(asWrWarrantyRecordPO.getRoNo());
									 asWrWarrantyPO.setDealerId(asWrWarrantyRecordPO.getDealerId());
									 asWrWarrantyPO.setWarLevel(asWrWarrantyRecordPO.getWarLevel());
									 asWrWarrantyPO.setWarType(asWrWarrantyRecordPO.getWarType());
									 asWrWarrantyPO.setWarPartId(asWrWarrantyRecordPO.getWarPartId());
									 asWrWarrantyPO.setCurLawTimes(asWrWarrantyRecordPO.getCurLawTimes());
									 asWrWarrantyPO.setCurWrTimes(asWrWarrantyRecordPO.getCurWrTimes());
									 asWrWarrantyPO.setCurWrStandard(asWrWarrantyRecordPO.getCurWrStandard());
									 asWrWarrantyPO.setWarCountDays(alladte);
									 asWrWarrantyPO.setWarMileage(asWrWarrantyRecordPO.getWarMileage());
									 asWrWarrantyPO.setWarCountTimes(1);
									 asWrWarrantyPO.setPoType(Constant.RO_STATUS_02);
									 asWrWarrantyPO.setCtmName(asWrWarrantyRecordPO.getCtmName());
									 asWrWarrantyPO.setMainPhone(asWrWarrantyRecordPO.getMainPhone());
									 asWrWarrantyPO.setCreateBy(asWrWarrantyRecordPO.getCreateBy());
									 asWrWarrantyPO.setCreateDate(asWrWarrantyRecordPO.getCreateDate());
									 dao.insert(asWrWarrantyPO);
							    }
						 }
					}
				}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void basePart(Date date,String[] PayTypePart,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		List<String> list = new ArrayList<String>();
		for(String PayTypePart1 : PayTypePart)
		{
			if(PayTypePart1.split("-")[0].equals(""+(int)Constant.PAY_TYPE_02))
			{
				list.add(PayTypePart1.split("-")[1]);
			}
		}
		
		tmBasePart(list,date,taropUp,logonUser);
	}
	
	
	
	
	
	
	
	public void tmBasePart(List<String> list ,Date date,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		if(list !=null && list.size()>0)
		{
			StringBuffer sb= new StringBuffer();
			for(int i = 0 ;i < list.size();i++)
			{
				if(list.size()-1 == i)
				{
					sb.append(list.get(i));
				}else
				{
					sb.append(list.get(i)+",");
				}
			}
			StringBuffer sql= new StringBuffer();
			sql.append(" select t.*,t.pos_id\n" );
			sql.append("  from tm_pt_part_base t,\n" );
			sql.append("       (select max(t.part_id) as part_id, t.pos_id\n" );
			sql.append("          from tm_pt_part_base t\n" );
			sql.append("         where t.pos_id is not null\n" );
			sql.append("   and t.part_code in\n" );
			sql.append("       (select B.Part_No\n" );
			sql.append("          from Tt_As_Ro_Repair_Part b\n" );
			sql.append("         where b.id in ("+sb.toString()+"))\n" );
			sql.append("         group by t.pos_id) m\n" );
			sql.append("  where t.part_id = m.part_id\n" );
			sql.append("  union\n" );
			sql.append(" select t.*,t.pos_id\n" );
			sql.append("  from tm_pt_part_base t\n" );
			sql.append(" where t.pos_id is null\n" );
			sql.append("   and t.part_code in\n" );
			sql.append("       (select B.Part_No\n" );
			sql.append("          from Tt_As_Ro_Repair_Part b\n" );
			sql.append("         where b.id in ("+sb.toString()+"))");
			List<TmPtPartBasePO> listpo = dao.select( TmPtPartBasePO.class, sql.toString(), null) ;
			
			StringBuffer sqlnotnull = new StringBuffer();
			sqlnotnull.append(" select t.*,t.pos_id\n" );
			sqlnotnull.append("  from tm_pt_part_base t\n" );
			sqlnotnull.append(" where t.pos_id is not null\n" );
			sqlnotnull.append("   and t.part_code in\n" );
			sqlnotnull.append("       (select B.Part_No\n" );
			sqlnotnull.append("          from Tt_As_Ro_Repair_Part b\n" );
			sqlnotnull.append("         where b.id in ("+sb.toString()+"))");
			List<TmPtPartBasePO> listbase = dao.select( TmPtPartBasePO.class, sqlnotnull.toString(), null) ;
			if(listbase != null && listbase.size()>0)
			{
				for( TmPtPartBasePO basePO: listbase)
				{
					//得到销售时间+月份 后的日期
					Date pradate = Utility.getdate(date, basePO.getWrMonths());
					//判断车辆的销售时间+月份 是否大于 维修工单开单时间，如果大于，则表示三包期内，否则三包期外，不做三包操作 
					int alladte = Utility.compareDate1(Utility.handleDate(pradate), Utility.handleDate(taropUp.getRoCreateDate()), 0);
					if( alladte > 0)
					{
						if(Constant.PART_WR_TYPE_3.equals(""+basePO.getPartWarType())){
							basePO.setPartWarType(Integer.parseInt(Constant.PART_WR_TYPE_1));
						}
						tmbaserule(alladte,basePO,taropUp,logonUser);
					}
				}
			}
			if(listpo != null && listpo.size()>0)
			{
				
				for(TmPtPartBasePO basePO : listpo )
				{
					int alladte = 0;
					if(!(basePO.getPosId() != null && (""+basePO.getPosId()).length()> 2 ))
					{
						Date pradate = Utility.getdate(date, basePO.getWrMonths());
						//判断车辆的销售时间+月份 是否大于 维修工单开单时间，如果大于，则表示三包期内，否则三包期外，不做三包操作 
						alladte = Utility.compareDate1(Utility.handleDate(pradate), Utility.handleDate(taropUp.getRoCreateDate()), 0);
						if(alladte >0);
						{
							if(Constant.PART_WR_TYPE_3.equals(""+basePO.getPartWarType())){
								basePO.setPartWarType(Integer.parseInt(Constant.PART_WR_TYPE_1));
							}
							tmbaserule(alladte,basePO,taropUp,logonUser);
						}
					}
					else 
					{
						
						TtAsWrMalfunctionPositionPO positionPO = new TtAsWrMalfunctionPositionPO();
						positionPO.setPosId(basePO.getPosId());
						List<TtAsWrMalfunctionPositionPO> listposition= dao.select(positionPO);
						positionPO = listposition.get(0);
						
						TtAsWrVinRulePO vinRulePO = new TtAsWrVinRulePO();
						//判断车辆的销售时间 与维修工单开单时间 的差值如果在关注时间内关注标志就是关注（94041001）否则就是94041002
						int keydays = Utility.compareDate1(Utility.handleDate(date), Utility.handleDate(taropUp.getRoCreateDate()), 0);
						if( keydays <= positionPO.getAttDays())
						{
							vinRulePO.setVrIsAtt(Constant.ALL_DAYS_1);
							vinRulePO = (TtAsWrVinRulePO) dao.select(vinRulePO).get(0);
						}
						else
						{
							vinRulePO.setVrIsAtt(Constant.ALL_DAYS_2);
							vinRulePO = (TtAsWrVinRulePO) dao.select(vinRulePO).get(0);
						}
						
						insertorupdatewr(alladte,vinRulePO,positionPO,taropUp,logonUser);
					}
				}
			}
			
			
			
		}
	}
	
	public void insertorupdatewr (int alladte,TtAsWrVinRulePO vinRulePO,TtAsWrMalfunctionPositionPO positionPO,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		TtAsWrWarrantyRecordPO asWrWarrantyRecordPO = new TtAsWrWarrantyRecordPO();
		 try {
			 long warId = Utility.getLong(SequenceManager.getSequence(""));
			 asWrWarrantyRecordPO.setWarId(warId);
			 asWrWarrantyRecordPO.setWarNo("YJ"+warId);
			 asWrWarrantyRecordPO.setWarDate(new Date());
			 asWrWarrantyRecordPO.setVin(taropUp.getVin());
			 asWrWarrantyRecordPO.setLicenseNo(taropUp.getLicense());
			 asWrWarrantyRecordPO.setVrId(vinRulePO.getVrId());
			 asWrWarrantyRecordPO.setRoNo(taropUp.getRoNo());
			 asWrWarrantyRecordPO.setDealerId(Long.parseLong(logonUser.getDealerId()));
			 asWrWarrantyRecordPO.setWarLevel(vinRulePO.getVrLevel());
			 asWrWarrantyRecordPO.setWarType(vinRulePO.getVrType());
			 asWrWarrantyRecordPO.setPartWrType(vinRulePO.getPartWrType());
			 asWrWarrantyRecordPO.setWarPartId(positionPO.getPosId());
			 asWrWarrantyRecordPO.setCurLawTimes(vinRulePO.getVrLaw());
			 asWrWarrantyRecordPO.setCurWrTimes(vinRulePO.getVrWarranty());
			 asWrWarrantyRecordPO.setCurWrStandard(vinRulePO.getVrLawStandard());
			 asWrWarrantyRecordPO.setWarCountDays(alladte);
			 asWrWarrantyRecordPO.setWarMileage(taropUp.getInMileage());
			 asWrWarrantyRecordPO.setWarCountTimes(1);
			 asWrWarrantyRecordPO.setCtmName(taropUp.getOwnerName());
			 asWrWarrantyRecordPO.setMainPhone(getPhone(taropUp.getVin()));
			 asWrWarrantyRecordPO.setWarRoUserId(logonUser.getUserId());
			 asWrWarrantyRecordPO.setWarRoType(taropUp.getRoStatus());
			 asWrWarrantyRecordPO.setCreateBy(logonUser.getUserId());
			 asWrWarrantyRecordPO.setCreateDate(new Date());
			 dao.insert(asWrWarrantyRecordPO);
			 uporinWarrantywr(positionPO,asWrWarrantyRecordPO,alladte,logonUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void uporinWarrantywr(TtAsWrMalfunctionPositionPO positionPO,TtAsWrWarrantyRecordPO asWrWarrantyRecordPO,int alldate,AclUserBean logonUser)
	{
		try {
			TtAsWrWarrantyPO asWrWarrantyPO = new TtAsWrWarrantyPO();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from tt_as_wr_warranty t where  t.ro_no = '"+asWrWarrantyRecordPO.getRoNo()+"' and t.WAR_PART_ID = "+asWrWarrantyRecordPO.getWarPartId());
			List<TtAsWrWarrantyPO> list = dao.select(TtAsWrWarrantyPO.class,sql.toString(),null);
			boolean fag = false;
			if(list != null && list.size()>0)
			{
				asWrWarrantyPO = list.get(0);
				fag = true;
			}
			TtAsWrVinPartRepairTimesPO asWrVinPartRepairTimesPO = new TtAsWrVinPartRepairTimesPO();
			asWrVinPartRepairTimesPO.setVin(asWrWarrantyRecordPO.getVin());
			asWrVinPartRepairTimesPO.setPartCode(positionPO.getPosCode());
			List<TtAsWrVinPartRepairTimesPO> listtimes= dao.select(asWrVinPartRepairTimesPO);
			if(listtimes != null && listtimes.size()>0)
			{
				asWrVinPartRepairTimesPO = listtimes.get(0);
			}
			
			if((""+asWrWarrantyRecordPO.getWarRoType()).equals(""+Constant.RO_STATUS_01))
			{
				if(fag)
				{
					TtAsWrWarrantyPO warrantyPO = new TtAsWrWarrantyPO();
					warrantyPO.setWarId(asWrWarrantyPO.getWarId());
					TtAsWrWarrantyPO warrantyPO2 = new TtAsWrWarrantyPO();
					warrantyPO2.setPoType(Constant.RO_STATUS_01);
					warrantyPO2.setUpdateBy(logonUser.getUserId());
					warrantyPO2.setUpdateDate(new Date());
					dao.update(warrantyPO, warrantyPO2);
					TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
					repairTimesPO.setVrtId(asWrVinPartRepairTimesPO.getVrtId());
					TtAsWrVinPartRepairTimesPO repairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
					repairTimesPO1.setCurTimes(asWrVinPartRepairTimesPO.getCurTimes()-1);
					repairTimesPO1.setUpdateBy(logonUser.getUserId());
					repairTimesPO1.setUpdateDate(new Date());
					dao.update(repairTimesPO, repairTimesPO1);
					
				}
			}else if((""+asWrWarrantyRecordPO.getWarRoType()).equals(""+Constant.RO_STATUS_02))
			{
				if(fag)
				{
					TtAsWrWarrantyPO warrantyPO = new TtAsWrWarrantyPO();
					warrantyPO.setWarId(asWrWarrantyPO.getWarId());
					TtAsWrWarrantyPO warrantyPO2 = new TtAsWrWarrantyPO();
					warrantyPO2.setPoType(Constant.RO_STATUS_02);
					warrantyPO2.setUpdateBy(logonUser.getUserId());
					warrantyPO2.setUpdateDate(new Date());
					dao.update(warrantyPO, warrantyPO2);
					TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
					repairTimesPO.setVrtId(asWrVinPartRepairTimesPO.getVrtId());
					TtAsWrVinPartRepairTimesPO repairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
					repairTimesPO1.setCurTimes(asWrVinPartRepairTimesPO.getCurTimes()+1);
					repairTimesPO1.setUpdateBy(logonUser.getUserId());
					repairTimesPO1.setUpdateDate(new Date());
					dao.update(repairTimesPO, repairTimesPO1);
				}else
				{
					 long warId = Utility.getLong(SequenceManager.getSequence(""));
					 asWrWarrantyPO.setWarId(warId);
					 asWrWarrantyPO.setWarNo("YJ"+warId);
					 asWrWarrantyPO.setWarDate(new Date());
					 asWrWarrantyPO.setVin(asWrWarrantyRecordPO.getVin());
					 asWrWarrantyPO.setLicenseNo(asWrWarrantyRecordPO.getLicenseNo());
					 asWrWarrantyPO.setVrId(asWrWarrantyRecordPO.getVrId());
					 asWrWarrantyPO.setRoNo(asWrWarrantyRecordPO.getRoNo());
					 asWrWarrantyPO.setDealerId(asWrWarrantyRecordPO.getDealerId());
					 asWrWarrantyPO.setWarLevel(asWrWarrantyRecordPO.getWarLevel());
					 asWrWarrantyPO.setWarType(asWrWarrantyRecordPO.getWarType());
					 asWrWarrantyPO.setPartWrType(asWrWarrantyRecordPO.getPartWrType());
					 asWrWarrantyPO.setWarPartId(asWrWarrantyRecordPO.getWarPartId());
					 asWrWarrantyPO.setCurLawTimes(asWrWarrantyRecordPO.getCurLawTimes());
					 asWrWarrantyPO.setCurWrTimes(asWrWarrantyRecordPO.getCurWrTimes());
					 asWrWarrantyPO.setCurWrStandard(asWrWarrantyRecordPO.getCurWrStandard());
					 asWrWarrantyPO.setWarCountDays(alldate);
					 asWrWarrantyPO.setWarMileage(asWrWarrantyRecordPO.getWarMileage());
					 asWrWarrantyPO.setWarCountTimes(1);
					 asWrWarrantyPO.setPoType(Constant.RO_STATUS_02);
					 asWrWarrantyPO.setCtmName(asWrWarrantyRecordPO.getCtmName());
					 asWrWarrantyPO.setMainPhone(asWrWarrantyRecordPO.getMainPhone());
					 asWrWarrantyPO.setCreateBy(asWrWarrantyRecordPO.getCreateBy());
					 asWrWarrantyPO.setCreateDate(asWrWarrantyRecordPO.getCreateDate());
					 dao.insert(asWrWarrantyPO);
					 
					 TtAsWrVinPartRepairTimesPO asWrVinPartRepairTimesPO2 = new TtAsWrVinPartRepairTimesPO();
					 asWrVinPartRepairTimesPO2.setVrtId(Utility.getLong(SequenceManager.getSequence("")));
					 asWrVinPartRepairTimesPO2.setVin(asWrWarrantyRecordPO.getVin());
					 TmVehiclePO vehiclePO = new TmVehiclePO();
					 vehiclePO.setVin(asWrWarrantyRecordPO.getVin());
					 
					 /*****add by liuxh 20131108判断车架号不能为空*****/
						CommonUtils.jugeVinNull(asWrWarrantyRecordPO.getVin());
						/*****add by liuxh 20131108判断车架号不能为空*****/
					 
					 List<TmVehiclePO> listv= dao.select(vehiclePO);
					 if(listv!=null && listv.size()>0)
					 {
						 vehiclePO = listv.get(0);
						 asWrVinPartRepairTimesPO2.setBuyDate(vehiclePO.getPurchasedDate());
						 asWrVinPartRepairTimesPO2.setWrEndDate(vehiclePO.getWrEndDate());
						 asWrVinPartRepairTimesPO2.setWrMonth(positionPO.getWrMonths());
						 asWrVinPartRepairTimesPO2.setWrMileage(positionPO.getWrMileage());
					 }
					 asWrVinPartRepairTimesPO2.setCurMileage(asWrWarrantyRecordPO.getWarMileage());
					 asWrVinPartRepairTimesPO2.setPartCode(positionPO.getPosCode());
					 asWrVinPartRepairTimesPO2.setPartName(positionPO.getPosName());
					 asWrVinPartRepairTimesPO2.setPartWrType(asWrWarrantyRecordPO.getPartWrType());
					 asWrVinPartRepairTimesPO2.setCurTimes(1);
					 asWrVinPartRepairTimesPO2.setCurLawTimes(asWrWarrantyRecordPO.getCurLawTimes());
					 asWrVinPartRepairTimesPO2.setCurWrTimes(asWrWarrantyRecordPO.getCurWrTimes());
					 asWrVinPartRepairTimesPO2.setCreateBy(asWrWarrantyRecordPO.getCreateBy());
					 asWrVinPartRepairTimesPO2.setCreateDate(asWrWarrantyRecordPO.getCreateDate());
					 dao.insert(asWrVinPartRepairTimesPO2);
					
				}
			} else if((""+asWrWarrantyRecordPO.getWarRoType()).equals(""+Constant.RO_STATUS_03))
			{
				  TtAsWrWarrantyRecordPO recordPO = new TtAsWrWarrantyRecordPO();
				   recordPO.setVin(asWrWarrantyPO.getVin());
				   recordPO.setWarPartId(asWrWarrantyPO.getWarPartId());
				   if(dao.select(recordPO).size() == 1)
				   {
					   dao.delete(asWrWarrantyPO);
				   }
				TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
				repairTimesPO.setVrtId(asWrVinPartRepairTimesPO.getVrtId());
				TtAsWrVinPartRepairTimesPO repairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
				repairTimesPO1.setCurTimes(asWrVinPartRepairTimesPO.getCurTimes()-1);
				repairTimesPO1.setUpdateBy(logonUser.getUserId());
				repairTimesPO1.setUpdateDate(new Date());
				dao.update(repairTimesPO, repairTimesPO1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void tmbaserule(int alladte,TmPtPartBasePO basePO,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		if(Constant.PART_WR_TYPE_1.equals(""+basePO.getPartWarType()) )
		{
			TtAsWrVinRulePO rulePO = new TtAsWrVinRulePO();
			rulePO.setPartWrType(basePO.getPartWarType());
			List<TtAsWrVinRulePO> list= dao.select(rulePO);
			String PartCode= basePO.getPartCode();
			TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
			repairTimesPO.setPartCode(PartCode);
			repairTimesPO.setVin(taropUp.getVin());
			List<TtAsWrVinPartRepairTimesPO> listPo= dao.select(repairTimesPO);
			insertorupdate(alladte,list,listPo,basePO,taropUp,logonUser);
		}else
		{
			TtAsWrVinRulePO rulePO = new TtAsWrVinRulePO();
			rulePO.setPartWrType(basePO.getPartWarType());
			rulePO.setVrPartCode(basePO.getPartCode());
			List<TtAsWrVinRulePO> list= dao.select(rulePO);
			String PartCode= basePO.getPartCode();
			TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
			repairTimesPO.setPartCode(PartCode);
			repairTimesPO.setVin(taropUp.getVin());
			List<TtAsWrVinPartRepairTimesPO> listPo= dao.select(repairTimesPO);
			insertorupdate(alladte,list,listPo,basePO,taropUp,logonUser);
		}
	}
	
	public void juderuleinsert(int alladte,List<TtAsWrVinRulePO> list,TtAsWrVinPartRepairTimesPO basePO,TmPtPartBasePO basePO1,TtAsRepairOrderPO taropUp,AclUserBean logonUser,int type)
	{
		try {
				if(list != null && list.size()>0)
				{
					TtAsWrVinRulePO asWrVinRulePO = null;
					TtAsWrVinRulePO asWrVinRulePO1 = null;
					int CurTimes= basePO.getCurTimes();
					int i = 0;
					for(TtAsWrVinRulePO rulePO : list)
					{
						if(rulePO.getVrWarranty() == CurTimes)
						{
							asWrVinRulePO = rulePO; 
							i = rulePO.getVrWarranty();
							break;
						}else
						{
							if(rulePO.getVrWarranty() > i)
							{
								i = rulePO.getVrWarranty();
								asWrVinRulePO1 = rulePO; 
							}
						}
					}
					if(!(asWrVinRulePO == null && CurTimes <= i ))
					{
						if(asWrVinRulePO == null)
						{
							asWrVinRulePO = asWrVinRulePO1;
						}
						 TtAsWrWarrantyRecordPO asWrWarrantyRecordPO = new TtAsWrWarrantyRecordPO();
						 long warId = Utility.getLong(SequenceManager.getSequence(""));
						 asWrWarrantyRecordPO.setWarId(warId);
						 asWrWarrantyRecordPO.setWarNo("YJ"+warId);
						 asWrWarrantyRecordPO.setWarDate(new Date());
						 asWrWarrantyRecordPO.setVin(taropUp.getVin());
						 asWrWarrantyRecordPO.setLicenseNo(taropUp.getLicense());
						 asWrWarrantyRecordPO.setVrId(asWrVinRulePO.getVrId());
						 asWrWarrantyRecordPO.setRoNo(taropUp.getRoNo());
						 asWrWarrantyRecordPO.setDealerId(Long.parseLong(logonUser.getDealerId()));
						 asWrWarrantyRecordPO.setWarLevel(asWrVinRulePO.getVrLevel());
						 asWrWarrantyRecordPO.setWarType(asWrVinRulePO.getVrType());
						 asWrWarrantyRecordPO.setPartWrType(asWrVinRulePO.getPartWrType());
						 asWrWarrantyRecordPO.setWarPartId(basePO1.getPartId());
						 asWrWarrantyRecordPO.setCurLawTimes(asWrVinRulePO.getVrLaw());
						 asWrWarrantyRecordPO.setCurWrTimes(asWrVinRulePO.getVrWarranty());
						 asWrWarrantyRecordPO.setCurWrStandard(asWrVinRulePO.getVrLawStandard());
						 asWrWarrantyRecordPO.setWarCountDays(alladte);
						 asWrWarrantyRecordPO.setWarMileage(taropUp.getInMileage());
						 asWrWarrantyRecordPO.setWarCountTimes(1);
						 asWrWarrantyRecordPO.setCtmName(taropUp.getOwnerName());
						 asWrWarrantyRecordPO.setMainPhone(getPhone(taropUp.getVin()));
						 asWrWarrantyRecordPO.setWarRoUserId(logonUser.getUserId());
						 asWrWarrantyRecordPO.setWarRoType(taropUp.getRoStatus());
						 asWrWarrantyRecordPO.setCreateBy(logonUser.getUserId());
						 asWrWarrantyRecordPO.setCreateDate(new Date());
						 dao.insert(asWrWarrantyRecordPO);
						 TtAsWrVinPartRepairTimesPO asWrVinPartRepairTimesPO = new TtAsWrVinPartRepairTimesPO();
						 asWrVinPartRepairTimesPO.setVrtId(basePO.getVrtId());
						 TtAsWrVinPartRepairTimesPO asWrVinPartRepairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
						 asWrVinPartRepairTimesPO1.setCurLawTimes(asWrVinRulePO.getVrLaw());
						 asWrVinPartRepairTimesPO1.setCurWrTimes(asWrVinRulePO.getVrWarranty());
						 dao.update(asWrVinPartRepairTimesPO, asWrVinPartRepairTimesPO1);
						 if(type != 0)
						 {
							    TtAsWrWarrantyPO warrantyPO = new TtAsWrWarrantyPO();
								warrantyPO.setRoNo(taropUp.getRoNo());
								warrantyPO.setWarPartId(basePO1.getPartId());
							    List<TtAsWrWarrantyPO> listwr = dao.select(warrantyPO);
							    if(listwr != null && listwr.size()>0)
							    {
							    	 warrantyPO = listwr.get(0);
							    	 TtAsWrWarrantyPO asWrWarrantyPO = new TtAsWrWarrantyPO();
							    	 asWrWarrantyPO.setWarId(warrantyPO.getWarId());
							    	 TtAsWrWarrantyPO asWrWarrantyPO1 = new TtAsWrWarrantyPO();
							    	 if(type == 2)
							    	 {
							    		 asWrWarrantyPO1.setPoType(Constant.RO_STATUS_01);
							    	 }else
							    	 {
							    		 asWrWarrantyPO1.setPoType(Constant.RO_STATUS_02);
							    	 }
							    	 dao.update(asWrWarrantyPO, asWrWarrantyPO1);
							    }else
							    {
							    	if(type == 1)
							    	{
							    		 TtAsWrWarrantyPO asWrWarrantyPO = new TtAsWrWarrantyPO();
								    	 long warId1 = Utility.getLong(SequenceManager.getSequence(""));
										 asWrWarrantyPO.setWarId(warId1);
										 asWrWarrantyPO.setWarNo("YJ"+warId1);
										 asWrWarrantyPO.setVin(asWrWarrantyRecordPO.getVin());
										 asWrWarrantyPO.setLicenseNo(asWrWarrantyRecordPO.getLicenseNo());
										 asWrWarrantyPO.setVrId(asWrWarrantyRecordPO.getVrId());
										 asWrWarrantyPO.setRoNo(asWrWarrantyRecordPO.getRoNo());
										 asWrWarrantyPO.setDealerId(asWrWarrantyRecordPO.getDealerId());
										 asWrWarrantyPO.setWarLevel(asWrWarrantyRecordPO.getWarLevel());
										 asWrWarrantyPO.setWarType(asWrWarrantyRecordPO.getWarType());
										 asWrWarrantyPO.setPartWrType(asWrWarrantyRecordPO.getPartWrType());
										 asWrWarrantyPO.setWarPartId(asWrWarrantyRecordPO.getWarPartId());
										 asWrWarrantyPO.setCurLawTimes(asWrWarrantyRecordPO.getCurLawTimes());
										 asWrWarrantyPO.setCurWrTimes(asWrWarrantyRecordPO.getCurWrTimes());
										 asWrWarrantyPO.setCurWrStandard(asWrWarrantyRecordPO.getCurWrStandard());
										 asWrWarrantyPO.setWarCountDays(alladte);
										 asWrWarrantyPO.setWarMileage(asWrWarrantyRecordPO.getWarMileage());
										 asWrWarrantyPO.setWarCountTimes(1);
										 asWrWarrantyPO.setPoType(Constant.RO_STATUS_02);
										 asWrWarrantyPO.setCtmName(asWrWarrantyRecordPO.getCtmName());
										 asWrWarrantyPO.setMainPhone(asWrWarrantyRecordPO.getMainPhone());
										 asWrWarrantyPO.setCreateBy(asWrWarrantyRecordPO.getCreateBy());
										 asWrWarrantyPO.setCreateDate(asWrWarrantyRecordPO.getCreateDate());
										 dao.insert(asWrWarrantyPO);
							    	}
							    	
							    }
						 }
					}
				}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void insertorupdate(int alladte,List<TtAsWrVinRulePO> list,List<TtAsWrVinPartRepairTimesPO> listPo,TmPtPartBasePO basePO,TtAsRepairOrderPO taropUp,AclUserBean logonUser)
	{
		try {
			if(list != null && list.size()>0)
			{
				
				if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_01))
				{
					if(listPo != null && listPo.size()> 0)
					{
						TtAsWrVinPartRepairTimesPO timesPO = listPo.get(0);
						TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO.setVrtId(timesPO.getVrtId());
						TtAsWrVinPartRepairTimesPO repairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO1.setUpdateBy(logonUser.getUserId());
						repairTimesPO1.setUpdateDate(new Date());
						repairTimesPO1.setCurTimes(timesPO.getCurTimes()-1);
						dao.update(repairTimesPO, repairTimesPO1);
					   juderuleinsert(alladte, list, timesPO, basePO, taropUp, logonUser,2);
					}
				}else if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_02))
				{
					
					if(listPo != null && listPo.size()> 0)
					{
						TtAsWrVinPartRepairTimesPO timesPO = listPo.get(0);
						TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO.setVrtId(timesPO.getVrtId());
						TtAsWrVinPartRepairTimesPO repairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO1.setUpdateBy(logonUser.getUserId());
						repairTimesPO1.setUpdateDate(new Date());
						repairTimesPO1.setCurTimes(timesPO.getCurTimes()+1);
						timesPO.setCurTimes(timesPO.getCurTimes()+1);
						dao.update(repairTimesPO, repairTimesPO1);
					   juderuleinsert(alladte, list, timesPO, basePO, taropUp, logonUser,1);
					}else
					{
						 TtAsWrVinPartRepairTimesPO asWrVinPartRepairTimesPO2 = new TtAsWrVinPartRepairTimesPO();
						 asWrVinPartRepairTimesPO2.setVrtId(Utility.getLong(SequenceManager.getSequence("")));
						 asWrVinPartRepairTimesPO2.setVin(taropUp.getVin());
						 TmVehiclePO vehiclePO = new TmVehiclePO();
						 String sql = "SELECT * FROM Tm_Vehicle WHERE 1=1  AND Vin= '"+taropUp.getVin()+"'";
						 List<TmVehiclePO> listv= dao.select(TmVehiclePO.class,sql,null);
						 if(listv!=null && listv.size()>0)
						 {
							 vehiclePO = listv.get(0);
							 asWrVinPartRepairTimesPO2.setBuyDate(vehiclePO.getPurchasedDate());
							 asWrVinPartRepairTimesPO2.setWrEndDate(vehiclePO.getWrEndDate());
							 asWrVinPartRepairTimesPO2.setWrMonth(basePO.getWrMonths());
							 asWrVinPartRepairTimesPO2.setWrMileage(basePO.getWrMileage());
						 }
						 asWrVinPartRepairTimesPO2.setCurMileage(taropUp.getInMileage());
						 asWrVinPartRepairTimesPO2.setPartCode(basePO.getPartCode());
						 asWrVinPartRepairTimesPO2.setPartName(basePO.getPartName());
						 asWrVinPartRepairTimesPO2.setPartWrType(basePO.getPartWarType());
						 asWrVinPartRepairTimesPO2.setCurTimes(1);
						 asWrVinPartRepairTimesPO2.setCreateBy(logonUser.getUserId());
						 asWrVinPartRepairTimesPO2.setCreateDate(new Date());
						 dao.insert(asWrVinPartRepairTimesPO2);
						juderuleinsert(alladte, list, asWrVinPartRepairTimesPO2, basePO, taropUp, logonUser,1);
					}
					
					
				}else if((""+taropUp.getRoStatus()).equals(""+Constant.RO_STATUS_03))
				{
					if(listPo != null && listPo.size()> 0)
					{
						TtAsWrVinPartRepairTimesPO timesPO = listPo.get(0);
						TtAsWrVinPartRepairTimesPO repairTimesPO = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO.setVrtId(timesPO.getVrtId());
						TtAsWrVinPartRepairTimesPO repairTimesPO1 = new TtAsWrVinPartRepairTimesPO();
						repairTimesPO1.setUpdateBy(logonUser.getUserId());
						repairTimesPO1.setUpdateDate(new Date());
						repairTimesPO1.setCurTimes(timesPO.getCurTimes()-1);
						dao.update(repairTimesPO, repairTimesPO1);
						
						TtAsWrWarrantyPO warrantyPO = new TtAsWrWarrantyPO();
						warrantyPO.setRoNo(taropUp.getRoNo());
						warrantyPO.setWarPartId(basePO.getPartId());
					    List<TtAsWrWarrantyPO> listwr = dao.select(warrantyPO);
					    if(listwr != null && listwr.size()>0)
					    {
						   warrantyPO = listwr.get(0);
						   TtAsWrWarrantyPO warrantyPO1 = new TtAsWrWarrantyPO();
						   warrantyPO1.setWarId(warrantyPO.getWarId());
						   TtAsWrWarrantyRecordPO recordPO = new TtAsWrWarrantyRecordPO();
						   recordPO.setVin(warrantyPO.getVin());
						   recordPO.setWarPartId(warrantyPO.getWarPartId());
						   if(dao.select(recordPO).size() == 1)
						   {
							   dao.delete(warrantyPO1);
						   }
						  
					    }
					    juderuleinsert(alladte, list, timesPO, basePO, taropUp, logonUser,0);
					}
				}
			}
			
		} catch (Exception e) {
		}
	}
	
	
	
	
	
	public List<TmBusinessAreaPO> getAreaName(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.* FROM TM_BUSINESS_AREA A  WHERE 1 = 1 and  status =10011001\n");
		List<TmBusinessAreaPO> list = select(TmBusinessAreaPO.class, sql.toString(), null);
		return list;
	}
	
	/**
	 * 易损件改为常规件：从三包规则表中删除该件
	 * @param partCode 配件代码
	 * @param partType 三包类型
	 */
	public void delNormalPartFromRule(String partCode,Integer partType){
		StringBuffer sql = new StringBuffer();
    	sql.append(" delete from Tt_As_Wr_Vin_Rule where VR_PART_CODE = '"+partCode+"' and PART_WR_TYPE = "+partType);
    	this.delete(sql.toString(), null);
	}
	
	/**
	 * 根据配件代码和三包类型查询三包规表中的配件信息
	 * @param partCode 配件代码
	 * @param partType 三包类型
	 * @return
	 */
	public List<TtAsWrVinRulePO> queryPartInRule(String partCode,Integer partType){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from Tt_As_Wr_Vin_Rule where VR_PART_CODE = '"+partCode+"' and PART_WR_TYPE = "+partType);
		List<TtAsWrVinRulePO> list = this.select(TtAsWrVinRulePO.class, sql.toString(), null);
		return list;
	}
	//s索赔单打印基础数据
	public List<TtAsWrApplicationExtPO> getBaseBean(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select a.balance_Yieldly,g2.group_name series_name, a.claim_type,a.dealer_name, a.dealer_shortname,a.claim_no,to_char(o.ro_create_date,'yyyy-mm-dd hh24:mi') ro_date,d.dealer_code,\n");
		sql.append("a.model_code,to_char(v.purchased_date,'yyyy-mm-dd ') buy_date,v.vin,v.engine_no,v.license_no,\n");
		sql.append("decode(o.deliverer_phone,null,o.deliverer_mobile,o.deliverer_phone) deliver_phone,o.deliverer ,o.service_advisor ,\n");
		sql.append("tc.main_phone customer_phone,tc.ctm_name customer_name,to_char(v.factory_date,'yyyy-mm-dd ') out_date,\n");
		sql.append("to_char(v.product_date,'yyyy-mm-dd hh24:mi') ,to_char(o.for_balance_time,'yyyy-mm-dd hh24:mi') banlance_date,\n");
		sql.append("o.in_mileage,o.trouble_descriptions  miao_shu,o.trouble_reason reason,o.repair_method,o.remarks,f.opinion,u.name audit_name,\n");
		sql.append("ba.area_name,m.material_code,g.group_code,to_char(f.update_date,'yyyy-mm-dd hh24:mi') agree_date,tc.address,a.labour_hours,a.labour_amount,\n");
		sql.append(" a.part_amount,a.gross_credit, o.free_times,a.appendlabour_num,a.appendlabour_amount\n");
		sql.append("from tt_as_wr_application a ,tt_as_repair_order o\n");
		sql.append("left join tt_as_wr_foreapproval f on f.ro_no=o.ro_no\n");
		sql.append("left join tc_user u on u.user_id = f.audit_person, tm_dealer d ,\n");
		sql.append("tm_vehicle v left join  tt_dealer_actual_sales sa on sa.vehicle_id = v.vehicle_id  and sa.is_return="+Constant.IF_TYPE_NO+"\n");
		sql.append("left join tt_customer tc on tc.ctm_id = sa.ctm_id \n");
		sql.append("left join tm_business_area ba on ba.area_id = v.yieldly\n");
		sql.append("left join tm_vhcl_material m on m.material_id = v.material_id\n");
		sql.append("left join tm_vhcl_material_group g on g.group_id = v.package_id\n");
		sql.append("left join tm_vhcl_material_group g2 on g2.group_id = v.series_id\n"); 

		sql.append("where a.ro_no = o.ro_no\n");
		sql.append("and a.vin = v.vin and nvl(a.second_dealer_id,a.dealer_id) = d.dealer_id\n");
		sql.append("and a.id="+id); 

		return this.select(TtAsWrApplicationExtPO.class, sql.toString(), null);
	}
	//正常维修，特殊服务配件明细
	 public List<TtAsWrApplicationExtPO> getDetail(String id){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select p.part_code,p.part_name,p.down_part_code down_code,p.down_part_name down_name,p.quantity, l.wr_labourcode labour_code,\n");
	    	sql.append("l.wr_labourname labour_name,p.remark,p.amount,l.labour_hours,l.labour_amount\n");
	    	sql.append("from tt_as_wr_partsitem p,tt_as_wr_labouritem l\n");
	    	sql.append("where p.id = l.id\n");
	    	sql.append("and p.wr_labourcode = l.wr_labourcode\n");
	    	sql.append("and p.id="+id); 


	    	return this.select(TtAsWrApplicationExtPO.class,sql.toString(), null);
	    }
	//外出人明细明细
	 public List<TtAsWrOutrepairPO> getOutDetail(String id){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select t.* from tt_as_wr_outrepair t,tt_as_wr_application a\n");
	    	sql.append("where a.ro_no = t.ro_no\n");
	    	sql.append("and a.id="+id); 
	    	return this.select(TtAsWrOutrepairPO.class,sql.toString(), null);
	    }
	 public List<TtAsWrNetitemPO> getOther(String id){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select n.*\n");
	    	sql.append(" from tt_as_wr_netitem n,tt_as_wr_application a\n");
	    	sql.append("where a.id = n.id and n.amount >= 0 \n");
	    	//sql.append("and n.item_code in ('QT006','QT007','QT008','QT009','QT010')"); 
	    	sql.append("and a.id="+id); 
	    	sql.append(" order by n.item_code desc  ");
	    	return this.select(TtAsWrNetitemPO.class,sql.toString(), null);
	    }
	 //活动明细
	 public List<TtAsActivityPO> getADetail(String id){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select  a.*\n");
	    	sql.append("from tt_as_activity a,tt_as_repair_order o,tt_as_wr_application ap\n");
	    	sql.append("where  o.cam_code = a.activity_code\n");
	    	sql.append("and ap.ro_no = o.ro_no\n");
	    	sql.append("and ap.id="+id); 
	    	return this.select(TtAsActivityPO.class,sql.toString(), null);
	    }
	 public String getTimes(String vin){
		 String sql = "select max(p.cur_times) num  from tt_as_wr_vin_part_repair_times p where p.vin='"+vin+"'"; 
			List<TtAsWrApplicationExtPO> ps=this.select(TtAsWrApplicationExtPO.class,sql.toString(), null);
			System.out.println(ps.size());
			if(ps!=null&&ps.size()>0){
				if(ps.get(0).getNum()==null){
					return "0";
				}
				return ps.get(0).getNum();
			}
			return "0";
		}
	 public List<TtAsWrApplicationExtPO> getbaoy(String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("select mg.free num ,mg.part_price,mg.labour_price FROM TT_AS_WR_MODEL_ITEM mi, tm_vehicle v ,tt_as_wr_model_group mg\n");
		sql.append("where v.package_id = mi.model_id\n");
		sql.append("and mg.wrgroup_id = mi.wrgroup_id\n");
		sql.append("and v.vin='"+vin+"'  AND MG.WRGROUP_TYPE=10451001"); 
		return this.select(TtAsWrApplicationExtPO.class,sql.toString(), null);
		
		}
	 public PageResult<Map<String, Object>> getLaber(int pageSize,String largess_type,String activityId,String PART_CODE,String PART_NAME, int curPage) throws Exception {
			PageResult<Map<String, Object>> result = null;
			StringBuilder sql = new StringBuilder();

			sql.append("select c.*,p.amount\n");
			sql.append("       from TT_AS_ACTIVITY_RELATION C\n");
			sql.append("      left join  tt_as_activity_project p  on  p.pro_code = c.largess_type and p.activity_id = c.activity_id\n");
			sql.append("      where C.ACTIVITY_ID = "+activityId+"\n");
			sql.append("        and C.largess_type = '"+largess_type+"'"); 

			if(Utility.testString(PART_CODE))
			{
				sql.append(" and  c.project_code like '%"+PART_CODE.toUpperCase()+"%'");
			}
			if(Utility.testString(PART_NAME)){
				sql.append(" and c.project_name like '%"+PART_NAME+"%'");
			}
			result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(),
					null, getFunName(), pageSize, curPage);
			return result;
		}
	 public List<TtAsWrRuleListPO> getNoFamily(Long id,String partCode){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select  l.*  from TT_AS_WR_RULE_LIST l ,tt_as_wr_rule r,tt_as_wr_game g\n");
	    	sql.append("where g.rule_id = r.id\n");
	    	sql.append("and l.rule_id = r.id\n");
	    	sql.append("  and l.part_code='"+partCode+"'\n");
	    	sql.append("and  g.id="+id+"\n"); 

	    	return this.select(TtAsWrRuleListPO.class,sql.toString(), null);
	    }
	 
	 public List<TtAsWrGamePO> isFamily(Long id){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select  * from tt_as_wr_game g where g.id="+id+"\n");
	    	sql.append("and g.game_code='"+Constant.VEHICLE_IS_FAMILY+"'"); 
	    	return this.select(TtAsWrGamePO.class,sql.toString(), null);
	    }
	 
	 public String getCodeDesc(String type){
	    	TcCodePO c = new TcCodePO();
	    	c.setCodeId(type);
	    	c = (TcCodePO) this.select(c).get(0);
	    	return c.getCodeDesc();
	    }
	 //经销商封面打印时间
	 public List<DealerCoverPrintBean> getCoverDetail(String dealerId,String yieldly,String starDate,String endDate){
			StringBuffer sql= new StringBuffer();
			sql.append("select d.dealer_code,\n");
			sql.append("       d.dealer_shortname dealer_name,\n");
			sql.append("       a.baoy_count,--包养次数\n");
			sql.append("       a.shouq_count,--售前维修次数\n");
			sql.append("       a.shouq_amount,--售前维修金额\n");
			sql.append("       a.normal_count,--正常维修次数\n");
			sql.append("       a.normal_amount,--正常维修费用\n");
			sql.append("       a.special_count,--特殊单次数\n");
			sql.append("       a.special_amount,--特殊单金额\n");
			sql.append("       a.out_count,--外出次数\n");
			sql.append("       a.out_amount,--外出金额\n");
			sql.append("       a.activity_count,--活动次数\n");
			sql.append("       a.activity_amount,--活动金额\n");
			sql.append("       a.baoy_amount,--保养金额\n");
			sql.append("       free_m_price,--保养费用\n");
			sql.append("       r.trans_count,--运单数\n");
			sql.append("       nvl(r.price, 0) trans_amount,--运费\n");
			sql.append("       nvl(a.baoy_part,0) + nvl(a.part_amount,0) part_price, --总材料费 = 保养材料费+维修材料费\n");
			//sql.append("       nvl(a.baoy_labour,0) + nvl(a.labour_amount,0) + nvl(a.netitem_amount,0) + nvl(a.part_down,0) +\n");
			//sql.append("       nvl(a.labour_down,0) labour_price, --总工时费 = 保养工时费+维修工时费+外出或者活动的其他项目费用+打折费用\n");
			
			sql.append("  a.shouq_amount+ a.normal_amount+ a.special_amount+ a.out_amount+ a.activity_amount+free_m_price+ nvl(ws.audit_acount3, 0)-nvl(ba.audit_acount, 0) - nvl(ca.audit_acount2, 0)-nvl(a.baoy_part,0) - nvl(a.part_amount,0) labour_price,\n");
			sql.append("       nvl(a.baoy_count,0)+ nvl(a.shouq_count,0)+ nvl(a.normal_count,0)+nvl(a.special_count,0)+nvl(a.out_count,0)+nvl(a.activity_count,0)+nvl(r.trans_count,0)+nvl(ws.count3, 0) + nvl(ba.count1, 0) + nvl(ca.count2, 0)  count_Amount ,--总次数\n");
			sql.append("       a.shouq_amount+ a.normal_amount+ a.special_amount+ a.out_amount+ a.activity_amount+free_m_price+ nvl(ws.audit_acount3, 0)-nvl(ba.audit_acount, 0) - nvl(ca.audit_acount2, 0) amount_total,--总费用\n");

			sql.append("       nvl(ws.audit_acount3, 0)-nvl(ba.audit_acount, 0) - nvl(ca.audit_acount2, 0)  other_amount, --其他费用 = 特殊费用 - 条码补办费用+三包凭证补办费用\n");
			sql.append("       nvl(ws.count3, 0) + nvl(ba.count1, 0) + nvl(ca.count2, 0) other_count\n"); 

			sql.append(" from\n");
			sql.append("( select  d.dealer_id,d.dealer_code,d.dealer_shortname\n");
			sql.append("from tm_dealer d where d.dealer_type="+Constant.DEALER_TYPE_DWR+" and d.dealer_level="+Constant.DEALER_LEVEL_01+" and d.dealer_id=?\n");
			sql.append(") d\n");
			sql.append("left join (\n");
			sql.append("select  a.dealer_id,sum(decode(a.claim_type,"+Constant.CLA_TYPE_02+",1,0))  baoy_count,\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_07+",1,0)) shouq_count,\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_07+", nvl(pa.part_amount, 0) +   nvl(la.labour_amount, 0),   0)) shouq_amount,--售前维修\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_01+",1,0))  normal_count,\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_01+", nvl(pa.part_amount, 0) +    nvl(la.labour_amount, 0),   0)) normal_amount, --正常维修\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_10+",1,0))  special_count,\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_10+", nvl(pa.part_amount, 0) +    nvl(la.labour_amount, 0),    0)) special_amount, --特殊服务\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_09+",1,0))  out_count,\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_09+" , nvl(pa.part_amount, 0) +   nvl(la.labour_amount, 0) + nvl(na.amount, 0),   0)) out_amount, --外派服务\n");
			sql.append("SUM(CASE WHEN a.claim_type="+Constant.CLA_TYPE_06+" AND AA.ACTIVITY_CODE IS NOT NULL\n");
			sql.append("THEN 1 ELSE 0 END) activity_count,\n");
			sql.append("SUM(CASE WHEN a.claim_type="+Constant.CLA_TYPE_06+" AND AA.ACTIVITY_CODE IS NOT NULL\n");
			sql.append("THEN  nvl(a.repair_total,0) ELSE 0 END) activity_amount,--厂家活动\n"); 
			sql.append("SUM(CASE WHEN (a.claim_type="+Constant.CLA_TYPE_06+"  AND AA.ACTIVITY_CODE IS NOT NULL )or a.claim_type="+Constant.CLA_TYPE_09+" \n");
			sql.append("THEN nvl(na.amount, 0) ELSE 0 END) netitem_amount,--厂家活动\n"); 
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_02+",nvl(a.gross_credit,0),0))  baoy_amount,--强保定检\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_02+",nvl(a.free_m_price,0),0))  free_m_price,--强保定检\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_02+",nvl(mg.part_price,0),0))  baoy_part,--保养材料费\n");
			sql.append("sum(decode(a.claim_type,"+Constant.CLA_TYPE_02+",nvl(mg.labour_price,0),0))  baoy_labour,--保养工时费\n");
			sql.append("sum(nvl(pa.part_amount, 0)) part_amount, --维修材料费金额\n");
			sql.append(" sum(nvl(la.labour_amount, 0)) labour_amount, --维修的工时费金额\n");
			sql.append(" sum(nvl(a.part_down, 0)) part_down, --材料费打折金额\n");
			sql.append(" sum(nvl(a.labour_down, 0)) labour_down -- 工时费打折金额\n"); 

			sql.append("from  tt_as_wr_application a\n");
			sql.append(" left join tm_vehicle v on v.vin  = a.vin\n");
			sql.append("left join (\n");
			sql.append("      select mi.model_id,mg.part_price,mg.labour_price  from TT_AS_WR_MODEL_ITEM mi ,tt_as_wr_model_group mg\n");
			sql.append("      where mi.wrgroup_id = mg.wrgroup_id\n");
			sql.append(" )  mg on mg.model_id = v.package_id\n"); 
			sql.append("left join (select pa.id, sum(nvl(pa.amount, 0)) part_amount\n");
			sql.append("                         from Tt_As_Wr_Partsitem pa\n");
			sql.append("                        group by pa.id) pa on pa.id = a.id\n");
			sql.append("             left join (select la.id,\n");
			sql.append("                              sum(nvl(la.labour_amount, 0)) labour_amount\n");
			sql.append("                         from tt_as_wr_labouritem la\n");
			sql.append("                        group by la.id) la on la.id = a.id\n");
			sql.append("             left join (select na.id, sum(nvl(na.amount, 0)) amount\n");
			sql.append("                         from Tt_As_Wr_Netitem na\n");
			sql.append("                        group by na.id) na on na.id = a.id\n"); 

			sql.append("LEFT JOIN (\n");
			sql.append("     SELECT  activity_code FROM  tt_as_activity aa,tt_as_activity_subject SS\n");
			sql.append("     WHERE ss.subject_id = aa.subject_id and SS.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+"\n");
			sql.append(")AA  on a.campaign_code = aa.activity_code\n"); 
			sql.append(" where  a.status !="+Constant.CLAIM_APPLY_ORD_TYPE_01+"\n");
			sql.append(" and a.report_date >=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
			sql.append(" and a.report_date <=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
			sql.append(" and a.create_date >=to_date('2013-08-26 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
			sql.append("  and a.balance_yieldly=?\n");
			sql.append(" group by a.dealer_id\n");
			sql.append(")a  on a.dealer_id = d.dealer_id\n");
			sql.append("left join (\n");
			if(Constant.PART_IS_CHANGHE_01.toString().equalsIgnoreCase(yieldly)){
				sql.append("select decode(dd.dealer_level,"+Constant.DEALER_LEVEL_02+",dd.parent_dealer_d,dd.dealer_id) dealer_ids,sum(nvl(ba.audit_acount,0))audit_acount  ,count(dd.dealer_level) count1\n");
				sql.append("from tt_as_barcode_apply ba ,tm_dealer dd\n");
				sql.append("where ba.apply_status = "+Constant.BARCODE_APPLY_STATUS_04+"  and ba.apply_by = dd.dealer_id\n");
				sql.append(" and ba.audit_date >=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" and ba.audit_date <=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" group by dd.dealer_level,dd.parent_dealer_d,dd.dealer_id\n");
				sql.append(") ba on ba.dealer_ids = d.dealer_id\n");
				sql.append("left join (\n");
				sql.append("select decode(dd.dealer_level,"+Constant.DEALER_LEVEL_02+",dd.parent_dealer_d,dd.dealer_id) dealer_ids,sum(nvl(ca.audit_acount,0)) audit_acount2  ,count(dd.dealer_level) count2\n");
				sql.append("from tt_as_packge_change_apply ca ,tm_dealer dd\n");
				sql.append("where ca.apply_status = "+Constant.PACKGE_CHANGE_STATUS_06+" and ca.apply_by = dd.dealer_id\n");
				sql.append("and ca.update_date >=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" and ca.update_date <=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" group by dd.dealer_level,dd.parent_dealer_d,dd.dealer_id\n");
				sql.append(")  ca on ca.dealer_ids = d.dealer_id\n");
				sql.append("left join (\n");
				sql.append("select decode(dd.dealer_level,"+Constant.DEALER_LEVEL_02+",dd.parent_dealer_d,dd.dealer_id) dealer_ids,sum(nvl(ws.declare_sum1,0) )audit_acount3 ,count(dd.dealer_level) count3\n");
				sql.append("from tt_as_wr_spefee ws ,tm_dealer dd\n");
				sql.append("where ws.status = ws.o_status and ws.dealer_id = dd.dealer_id\n");
				sql.append("and ws.make_date >=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" and ws.make_date <=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" group by dd.dealer_level,dd.parent_dealer_d,dd.dealer_id\n");
				sql.append(")  ws on ws.dealer_ids = d.dealer_id\n");
			}else{
				sql.append("select decode(dd.dealer_level,"+Constant.DEALER_LEVEL_02+",dd.parent_dealer_d,dd.dealer_id) dealer_ids,0 audit_acount  ,0 count1\n");
				sql.append("from tt_as_barcode_apply ba ,tm_dealer dd\n");
				sql.append("where ba.apply_status = "+Constant.BARCODE_APPLY_STATUS_04+"  and ba.apply_by = dd.dealer_id\n");
				sql.append(" and ba.audit_date >=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" and ba.audit_date <=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" group by dd.dealer_level,dd.parent_dealer_d,dd.dealer_id\n");
				sql.append(") ba on ba.dealer_ids = d.dealer_id\n");
				sql.append("left join (\n");
				sql.append("select decode(dd.dealer_level,"+Constant.DEALER_LEVEL_02+",dd.parent_dealer_d,dd.dealer_id) dealer_ids,0 audit_acount2  ,0 count2\n");
				sql.append("from tt_as_packge_change_apply ca ,tm_dealer dd\n");
				sql.append("where ca.apply_status = "+Constant.PACKGE_CHANGE_STATUS_06+" and ca.apply_by = dd.dealer_id\n");
				sql.append("and ca.update_date >=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" and ca.update_date <=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" group by dd.dealer_level,dd.parent_dealer_d,dd.dealer_id\n");
				sql.append(")  ca on ca.dealer_ids = d.dealer_id\n");
				sql.append("left join (\n");
				sql.append("select decode(dd.dealer_level,"+Constant.DEALER_LEVEL_02+",dd.parent_dealer_d,dd.dealer_id) dealer_ids,0 audit_acount3 ,0 count3\n");
				sql.append("from tt_as_wr_spefee ws ,tm_dealer dd\n");
				sql.append("where ws.status = ws.o_status and ws.dealer_id = dd.dealer_id\n");
				sql.append("and ws.make_date >=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" and ws.make_date <=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
				sql.append(" group by dd.dealer_level,dd.parent_dealer_d,dd.dealer_id\n");
				sql.append(")  ws on ws.dealer_ids = d.dealer_id\n");
			}
			sql.append("left join (\n");
			sql.append("select r.dealer_id,sum(r.price)price ,count(r.dealer_id) trans_count from tt_as_wr_old_returned r\n");
			sql.append("where r.return_date>=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
			sql.append("and r.return_date <=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n");
			sql.append("and r.yieldly=?\n"); 
			sql.append("group by r.dealer_id\n");
			sql.append(") r on r.dealer_id = d.dealer_id"); 
			List<Object> paramList = new ArrayList<Object>();
	    	paramList.add(dealerId);
	    	paramList.add(starDate);
	    	paramList.add(endDate);
	    	paramList.add(yieldly);
	    	paramList.add(starDate);
	    	paramList.add(endDate);
	    	paramList.add(starDate);
	    	paramList.add(endDate);
	    	paramList.add(starDate);
	    	paramList.add(endDate);
	    	paramList.add(starDate);
	    	paramList.add(endDate);
	    	paramList.add(yieldly);
	    	return this.select(DealerCoverPrintBean.class, sql.toString(), paramList);
	 }
	    public PageResult<Map<String,Object>> getPrintDetail(String dealerId,Integer curPage,Integer pageSize){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select T.ID,t.print_date,T.LAST_PRINT_DATE,T.BALANCE_YIELDLY,t.star_date,t.end_date,t.labour_price,t.part_price,t.print_times,d.dealer_code,d.dealer_shortname dealer_name\n");
	    	sql.append(" from tt_as_dealer_cover_detail t,tm_dealer d\n");
	    	sql.append(" where d.dealer_id = t.dealer_id\n");
	    	sql.append(" and d.dealer_id="+dealerId+"\n"); 
	    	sql.append( "  order by t.id desc ");

	    	return (PageResult<Map<String, Object>>) this.pageQuery(sql.toString(),
					null,
					this.getClass().getName(),
					pageSize,
					curPage);
	    }
	    public PageResult<Map<String,Object>> getPrintDetail2(String dealerCode,String dealerName,int pos ,Integer curPage,Integer pageSize){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select T.ID,t.print_date,T.LAST_PRINT_DATE,T.BALANCE_YIELDLY,t.star_date,t.end_date,t.labour_price,t.part_price,t.print_times,d.dealer_code,d.dealer_shortname dealer_name\n");
	    	sql.append(" from tt_as_dealer_cover_detail t,tm_dealer d\n");
	    	sql.append(" where d.dealer_id = t.dealer_id\n");

	    	if(Utility.testString(dealerCode)){
	    		sql.append(" and  d.dealer_code like '%"+dealerCode+"%'\n");
	    	}
	    	if(Utility.testString(dealerName)){
	    		sql.append(" and  d.dealer_name like '%"+dealerName+"%'\n");
	    	}
	    	if(Utility.testString(pos+"")){//判断是职位是东安还是昌河
	    		if(pos == Constant.POSE_BUS_TYPE_WRD){
	    			sql.append(" and t.balance_yieldly="+Constant.PART_IS_CHANGHE_02+"\n");
	    		}
	    		
	    	}
	    	sql.append( "  order by t.id desc ");
	
	    	return (PageResult<Map<String, Object>>) this.pageQuery(sql.toString(),
					null,
					this.getClass().getName(),
					pageSize,
					curPage);
	    }
		public List<Map<String,Object>> getLabourDan(String dealerId,String modelId,String oemCompanyId)
		{
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT pr.*\n");
			sql.append("             from tt_as_wr_labour_price pr\n");
			sql.append("             left outer join tm_dealer td on td.dealer_id = pr.dealer_id\n");
			sql.append("            where 1 = 1\n");
			sql.append("              AND pr.DEALER_ID = "+dealerId+"\n");
			sql.append("              and pr.mode_type =\n");
			sql.append("                  (SELECT WRGROUP_CODE\n");
			sql.append("                     FROM tt_as_wr_model_group\n");
			sql.append("                    WHERE WRGROUP_ID in\n");
			sql.append("                          (SELECT WRGROUP_ID\n");
			sql.append("                             FROM tt_as_wr_model_item\n");
			sql.append("                            WHERE MODEL_ID = "+modelId+")\n");
			sql.append("                      and wrgroup_type = "+Constant.WR_MODEL_GROUP_TYPE_01+")"); 

			return this.pageQuery(sql.toString(), null,this.getFunName());
		}
		
		//查询物料组集合
		public PageResult<Map<String,Object>> getGroup(String series,String brand,Integer curPage){
			StringBuffer sql = new StringBuffer();
			sql.append("select t.group_id,t.group_code,t.group_name\n");
			sql.append("from tm_vhcl_material_group t\n");
			sql.append("where 1=1\n");
			
			if(Utility.testString(series)){
				sql.append("and t.group_level =3\n");
				sql.append("and t.group_id in (\n");
				sql.append("select TVMG.group_id  from tm_vhcl_material_group TVMG   start with TVMG.Group_id="+series+"\n");
				sql.append("       connect by prior TVMG.group_id =TVMG.parent_group_id\n");
				sql.append(")"); 

			}else if(Utility.testString(brand)&&!Utility.testString(series)){
				sql.append("and t.group_level =2\n");
				sql.append("and t.group_id in (\n");
				sql.append("select TVMG.group_id  from tm_vhcl_material_group TVMG   start with TVMG.Group_id="+brand+"\n");
				sql.append("       connect by prior TVMG.group_id =TVMG.parent_group_id\n");
				sql.append(")"); 

			}
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), Constant.PAGE_SIZE, curPage);
			return ps;
		}
//根据工单号查询工单工时list
		public List<Map<String,Object>> getLabourList(String roNo)
		{
			StringBuffer sql= new StringBuffer();
			sql.append("select ro_id\n");
			sql.append("from tt_as_ro_labour l\n");
			sql.append("where l.ro_id = (select id from Tt_As_Repair_Order o where o.ro_no='"+roNo+"')\n");
			sql.append("and l.pay_type="+Constant.PAY_TYPE_02+"\n"); 
			List<Map<String, Object>> result = super.pageQuery(sql.toString(),null, this.getFunName());
			return result;
		}
		//根据工单号查询配件list 
		public List<Map<String,Object>> getPartList(String roNo)
		{
			StringBuffer sql= new StringBuffer();
			sql.append("select ro_id from Tt_As_Ro_Repair_Part p\n");
			sql.append("where p.ro_id = (select id from Tt_As_Repair_Order o where o.ro_no='"+roNo+"')\n");
			sql.append("and p.pay_type="+Constant.PAY_TYPE_02+"\n"); 
		//	sql.append("and p.part_use_type="+Constant.PART_USE_TYPE_02+"\n"); 
		//	sql.append("and p.part_no not in ('00-000','00-0000')\n");
		//	sql.append("and p.part_quantity>0"); 

			List<Map<String, Object>> result = super.pageQuery(sql.toString(),null, this.getFunName());
			return result;
		}
		 public List<Map<String,Object>> getApplication(AclUserBean user, Map<String, String> map2){
		    	String yieldly =(String) map2.get("yieldly");
		    	String str = map2.get("dealerId");
				StringBuffer sqlStr = new StringBuffer(); 
				
				sqlStr.append("SELECT\n" );
				sqlStr.append("       nvl(A.Free_m_Price,0)  as FREE_M_PRICE,--保养费\n" );
				sqlStr.append("       case  P.Responsibility_Type when 94001001 then  nvl( (select sum(nvl(com.pass_price,0)) \n" );
				sqlStr.append("          from TT_AS_WR_COMPENSATION_APP com where  com.claim_no = a.claim_no group by com.claim_no),0)  else 0 end as COMPENSATION_MONEY,--补偿费\n" );
				sqlStr.append("       case  P.Responsibility_Type when 94001001 then nvl(p.balance_amount,0)   else 0 end as PART_AMOUNT,  --材料费\n" );
			//	sqlStr.append("       case  P.Responsibility_Type when 94001001 then  nvl(A.NETITEM_AMOUNT,0)   else 0 end as NETITEM_AMOUNT,  --外出费\n" );
				sqlStr.append("       nvl(A.NETITEM_AMOUNT,0)    as NETITEM_AMOUNT,  --外出费\n" );
				sqlStr.append("       case  P.Responsibility_Type when  94001001 then nvl((select sum(nvl(d.price,0)) \n" );
				sqlStr.append("          from Tt_Claim_Accessory_Dtl d where  d.claim_no = a.claim_no group by d.claim_no),0) else 0 end as  ACCESSORIES_PRICE,--辅料费\n" );
				sqlStr.append("       nvl(L.LABOUR_AMOUNT, 0) LABOUR_AMOUNT,--工时费\n" );
				sqlStr.append("\t    nvl（ a.balance_netitem_amount，0）,--其他费用\n" );
				sqlStr.append("      P.Responsibility_Type as RESP_TYPE,\n" );
				sqlStr.append("      (select b.dealer_shortname\n" );
				sqlStr.append("         from TM_DEALER b\n" );
				sqlStr.append("        where b.dealer_id = NVL(A.SECOND_DEALER_ID, A.DEALER_ID)) as dealer_shortname,\n" );
				sqlStr.append("(select b.dealer_code\n" );
				sqlStr.append("         from TM_DEALER b\n" );
				sqlStr.append("        where b.dealer_id = NVL(A.SECOND_DEALER_ID, A.DEALER_ID)) as dealer_code,      A.CLAIM_NO,\n" );
				sqlStr.append("      A.RO_NO,\n" );
				sqlStr.append("      (select cc.CODE_DESC from tc_code cc where cc.code_id = A.Claim_Type) as Claim_Type,\n" );
				sqlStr.append("      A.SERIES_NAME,\n" );
				sqlStr.append("      V.MODEL,\n" );
				sqlStr.append("      V.DELIVERER,\n" );
				sqlStr.append("      V.DELIVERER_PHONE || '  ' || V.DELIVERER_MOBILE DELIVERER_PHONE,\n" );
				sqlStr.append("      A.VIN,\n" );
				sqlStr.append("      (select BA.AREA_NAME\n" );
				sqlStr.append("         from TM_BUSINESS_AREA BA\n" );
				sqlStr.append("        where BA.AREA_ID = A.YIELDLY) AS YIELDLY_NAME,\n" );
				sqlStr.append("      (select cc.CODE_DESC\n" );
				sqlStr.append("         from tc_code cc\n" );
				sqlStr.append("        where cc.code_id = A.BALANCE_YIELDLY) as BAN_NAME,\n" );
				sqlStr.append("      TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD HH24:MI') REPORT_TIME,\n" );
				sqlStr.append("      L.WR_LABOURCODE,\n" );
				sqlStr.append("      L.WR_LABOURNAME,\n" );
				sqlStr.append("      nvl(L.LABOUR_HOURS, 0) LABOUR_HOURS,\n" );
				sqlStr.append("      nvl(L.LABOUR_PRICE, 0) LABOUR_PRICE,\n" );
				sqlStr.append("\n" );
				sqlStr.append("      P.DOWN_PART_CODE,\n" );
				sqlStr.append("      P.DOWN_PART_NAME,\n" );
				sqlStr.append("      nvl(P.QUANTITY, 0) QUANTITY,\n" );
				sqlStr.append("      nvl(P.PRICE, 0) price,\n" );
				sqlStr.append("      nvl(P.AMOUNT, 0) AMOUNT,\n" );
				sqlStr.append("      (select cc.CODE_DESC from tc_code cc where cc.code_id = A.Status) as STATUS_NAME\n" );
				sqlStr.append("   FROM TT_AS_REPAIR_ORDER     V left join tm_vhcl_material_group tvm on   V.MODEL = tvm.GROUP_CODE ,\n" );
				sqlStr.append("        TT_AS_WR_APPLICATION   A \n" );
				sqlStr.append("       left join  tm_business_area   ba on ba.area_id = a.yieldly \n" );
				sqlStr.append("        \n" );
				sqlStr.append("     left join    tt_as_wr_partsitem     p  on a.id = p.id \n" );
				sqlStr.append("\t    left join TT_AS_WR_LABOURITEM    L on L.ID = A.ID  and p.wr_labourcode = l.wr_labourcode \n" );
				sqlStr.append(" WHERE 1 = 1\n" );
				sqlStr.append("   AND A.RO_NO = V.RO_NO\n" );
				if(yieldly!=null){
					sqlStr.append("   AND A.yieldly = '"+yieldly+"'\n" );
				}
				if (Utility.testString(map2.get("dealerName"))) {
					sqlStr.append(" AND b.DEALER_name like'%" + map2.get("dealerName")+ "%' \n");
				}
				// 经销商代码
				if (Utility.testString(map2.get("dealerId"))) {
					//sqlStr.append(" AND A.DEALER_ID='" + map.get("dealerId") + "' \n");
				//	sqlStr.append(" AND (A.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+map.get("dealerId")+"   connect by PRIOR d.dealer_id = d.parent_dealer_d) or a.second_dealer_id = "+map.get("dealerId")+" )\n");
					sqlStr.append(" AND a.DEALER_ID = " + map2.get("dealerId")+ "\n");
//					sqlStr.append("      (SELECT d.DEALER_ID\n");
//					sqlStr.append("         FROM TM_DEALER D\n");
//					sqlStr.append("        START WITH d.DEALER_ID = "+map2.get("dealerId")+"\n");
//					sqlStr.append("       CONNECT BY PRIOR d.DEALER_ID = d.PARENT_DEALER_D)"); 

				}
				// 索赔单号
				if (Utility.testString(map2.get("claimNo"))) {
					sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map2.get("claimNo")
							+ "%' \n");
				}
				// 工单号
				if (Utility.testString(map2.get("roNo"))) {
					sqlStr.append(" AND A.RO_NO LIKE '%" + map2.get("roNo") + "%' \n");
				}
				// 行号
				if (Utility.testString(map2.get("lineNo"))) {
					sqlStr.append(" AND A.LINE_NO='" + map2.get("lineNo") + "' \n");
				}
				// 索赔类型
				DaoFactory.getsql(sqlStr, "A.CLAIM_TYPE", map2.get("claimType"), 6);
				if(Utility.testString(map2.get("partCode"))){
					sqlStr.append("   AND A.ID in ( SELECT TAWP.ID FROM TT_AS_WR_PARTSITEM TAWP WHERE TAWP.PART_CODE LIKE '%"+map2.get("partCode")+"%' GROUP BY TAWP.ID )  \n");
				}
				if(Utility.testString(map2.get("wrLabourCode"))){
					sqlStr.append("   AND A.ID in ( SELECT TAWL.ID FROM TT_AS_WR_LABOURITEM TAWL WHERE TAWL.WR_LABOURCODE LIKE '%"+map2.get("wrLabourCode")+"%' GROUP BY TAWL.ID )  \n");
				}
				// 车辆VIN码
				if (Utility.testString(map2.get("vin"))) {// VIN涓嶄负绌 
					sqlStr.append(" and a.vin in ("+map2.get("vin")+")\n");
//					sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'",
//							"\''"), "A"));
				}
				// 工单开始时间
				if (Utility.testString(map2.get("roStartdate"))) {
					sqlStr
							.append(" AND A.CREATE_DATE >= to_date('"
									+ map2.get("roStartdate")
									+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') \n");
				}
				// 工单结束时间
				if (Utility.testString(map2.get("roEnddate"))) {
					sqlStr.append(" AND A.CREATE_DATE <= to_date('"
							+ map2.get("roEnddate")
							+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
				}
				// 审核时间查询条件
				if(Utility.testString(map2.get("approveDate"))){
					sqlStr.append(" AND a.auditing_date >= to_date('"
							+map2.get("approveDate")
							+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
				}
				if(Utility.testString(map2.get("approveDate2"))){
					sqlStr.append(" AND a.auditing_date <= to_date('"
							+map2.get("approveDate2")
							+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
				}
				if(Utility.testString(map2.get("balanceApproveDate"))){
					sqlStr.append(" AND a.account_date >= to_date('"
							+map2.get("balanceApproveDate")
							+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
				}
				if(Utility.testString(map2.get("balanceApproveDate2"))){
					sqlStr.append(" AND a.account_date <= to_date('"
							+map2.get("balanceApproveDate2")
							+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
				}
				// 艾春 13.11.20 添加是否二级查询条件
				if(Utility.testString(map2.get("isSecond"))){
					if("1".equals(map2.get("isSecond"))){
						sqlStr.append(" AND a.second_dealer_id is not null \n");
					}else{
						sqlStr.append(" AND a.second_dealer_id is null \n");
					}
				}
				// 艾春 13.11.20 添加是否二级查询条件
				
				// 申请状态
				if (Utility.testString(map2.get("status"))) {
					sqlStr.append(" AND A.STATUS='" + map2.get("status") + "' \n");

				} else if (!"track".equals(map2.get("track"))) {
					sqlStr.append(" AND (A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_01
							+ "'  \n");
					sqlStr.append(" OR A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_06
							+ "')  \n");

				} else if (!"isApp".equals(map2.get("isApp"))) {
					sqlStr.append(" AND (A.STATUS<" + Constant.CLAIM_APPLY_ORD_TYPE_01
							+ " OR A.STATUS >" + Constant.CLAIM_APPLY_ORD_TYPE_01 +") \n");
				}
				if (Utility.testString(map2.get("deliverer"))) {
					sqlStr.append(" and v.deliverer like '%"+map2.get("deliverer")+"%' ");
				}
				if (Utility.testString(map2.get("delivererPhone"))) {
					sqlStr.append(" and v.deliverer_phone like '%"+map2.get("delivererPhone")+"%' ");
				}
				 //该段代码为屏蔽服务站不看到的工单
			    if (null!=user.getDealerId()) {
			    	sqlStr.append(" and a.ro_no not in (select v.ro_no from tt_as_wr_repair_claim v where v.dealer_id='"+user.getDealerId()+"')");
				}
				sqlStr.append(" ORDER BY A.ID DESC ");
				System.out.println("sqlsql=="+sqlStr.toString());
		    	return this.pageQuery(sqlStr.toString(), null, this.getFunName());
		    }
		 public List<Map<String,Object>> getFreeAmount(String vin){
		        StringBuffer sql= new StringBuffer();
		        sql.append("SELECT g.labour_price,g.part_price\n");
		        sql.append("FROM tm_vehicle v\n");
		        sql.append("LEFT JOIN VW_MATERIAL_GROUP_service gs on gs.PACKAGE_ID = v.package_id\n");
		        sql.append("left join TT_AS_WR_MODEL_ITEM i on i.model_id = gs.PACKAGE_ID and i.wrgroup_id !=-1\n");
		        sql.append("left join TT_AS_WR_MODEL_GROUP g on g.wrgroup_id = i.wrgroup_id and g.wrgroup_type=10451001\n");
		        sql.append("WHERE v.vin='"+vin+"'"); 

		    	return this.pageQuery(sql.toString(), null, this.getFunName());
		    }
		 
		 public PageResult<Map<String,Object>> selectMalCodeQuery(String malCode,String malName,int pageSize, int curPage){
		        StringBuffer sql= new StringBuffer();
		        sql.append("SELECT  m.mal_id,m.mal_code,m.mal_name\n");
		        sql.append("FROM Tt_As_Wr_Malfunction m\n");
		        sql.append("WHERE 1=1\n");
		        if(Utility.testString(malCode)){
		        	 sql.append("AND upper(m.mal_code) LIKE '%"+malCode.toUpperCase()+"%'\n");
		        }
		        if(Utility.testString(malName)){
		        	  sql.append("AND m.mal_name LIKE '%"+malName+"%'"); 
		        }
		    	return  pageQuery(sql.toString(), null, this.getFunName(),pageSize , curPage);
		    }
		 
		 /**
		  * 服务站人员联系方式修改记录查询
		  */
		 public PageResult<Map<String, Object>> queryContactChangeHistoryList(RequestWrapper req,String dealerId,String status,String contactId,
				 String change_date_start,String change_date_end,
				 int curPage, int pageSize){


				StringBuffer sb = new StringBuffer();
				sb.append("select duir.id,\n");
				sb.append("       duir.change_date,\n");
				sb.append("       tu.name as change_user,\n");
				sb.append("       duir.history_id,\n");
				sb.append("       CASE WHEN duir.old_name<>duir.new_name THEN '<font color=\"red\">'||duir.new_name||'</font>'\n");
				sb.append("       ELSE duir.new_name END");
				sb.append("       as new_name,\n");
				sb.append("       duir.old_name,\n");
				sb.append("       duir.old_pose,\n");
				sb.append("       CASE WHEN duir.old_pose<>duir.new_pose THEN '<font color=\"red\">'||tcp.code_desc||'</font>'\n");
				sb.append("       ELSE tcp.code_desc END");
				sb.append("       as new_pose,\n");
				sb.append("       duir.old_phone,\n");
				sb.append("       CASE WHEN duir.old_phone<>duir.new_phone THEN '<font color=\"red\">'||duir.new_phone||'</font>'\n");
				sb.append("       ELSE duir.new_phone END");
				sb.append("       as new_phone,\n");
				sb.append("       duir.old_status,");
				sb.append("       CASE WHEN duir.old_status<>duir.new_status THEN '<font color=\"red\">'||tcs.code_desc||'</font>'\n");
				sb.append("       ELSE tcs.code_desc END");
				sb.append("       as new_status,\n");
				sb.append("       duir.old_hot_line,\n");
				sb.append("       CASE WHEN duir.old_hot_line<>duir.new_hot_line THEN '<font color=\"red\">'||new_hot_line||'</font>'\n");
				sb.append("       ELSE duir.new_hot_line END");
				sb.append("       as new_hot_line,\n");
				sb.append("       td.dealer_code,\n");
				sb.append("       td.dealer_shortname\n");
				sb.append("  from tm_dealer_user_info_record duir,\n");
				sb.append("       tc_user                    tu,\n");
				sb.append("       tm_claim_contact_phone     ccp,\n");
				sb.append("       tm_dealer     td,\n");
				sb.append("       tc_code tcp,");
				sb.append("       tc_code tcs");
				sb.append(" where duir.change_user = tu.user_id(+)\n");
				sb.append("   and duir.id = ccp.id(+)\n");
				sb.append("   and ccp.dealer_id=td.dealer_id(+)");
				sb.append("   and duir.dealer_id=ccp.dealer_id\n");
				sb.append("   and tcp.code_id(+)=duir.new_pose and tcs.code_id(+)=duir.new_status");

				if(dealerId!=null&&!"".equals(dealerId)){
					
					sb.append(" and ccp.dealer_id="+dealerId);
				}
				/*if(status!=null&&!"".equals(status)){
					
					sb.append(" and ccp.status="+status);
				}*/
				if(contactId!=null&&!"".equals(contactId)){
					
					sb.append(" and ccp.id="+contactId);
				}
				if(change_date_start!=null&&!"".equals(change_date_start))
					sb.append(" and duir.change_date>=to_date('" + change_date_start+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
				if(change_date_end!=null&&!"".equals(change_date_end))
					sb.append(" and duir.change_date<=to_date('" + change_date_end+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");

				sb.append("union all\n");
				sb.append("select uif.id,\n");
				sb.append("       uif.change_date,\n");
				sb.append("       tuu.name as change_user,\n");
				sb.append("       uif.history_id,\n");
				sb.append("       null as new_name,\n");
				sb.append("       null as old_name,\n");
				sb.append("       null as old_pose,\n");
				sb.append("       null as new_pose,\n");
				sb.append("       null as old_phone,\n");
				sb.append("       null as new_phone,\n");
				sb.append("       null as old_status,\n");
				sb.append("       null as new_status,\n");
				sb.append("       uif.old_hot_line,\n");
				sb.append("       CASE\n");
				sb.append("         WHEN uif.old_hot_line <> uif.new_hot_line THEN\n");
				sb.append("          '<font color=\"red\">' || new_hot_line || '</font>'\n");
				sb.append("         ELSE\n");
				sb.append("          uif.new_hot_line\n");
				sb.append("       END as new_hot_line_phone,\n");
				sb.append("       tdd.dealer_code,\n");
				sb.append("       tdd.dealer_shortname\n");
				sb.append("  from tm_dealer_user_info_record uif, tm_dealer tdd, tc_user tuu\n");
				sb.append(" where uif.dealer_id = tdd.dealer_id\n");
				sb.append("   and uif.id is null\n");
				sb.append("   and uif.change_user = tuu.user_id(+)\n");
				if(dealerId!=null&&!"".equals(dealerId)){
					
					sb.append(" and tdd.dealer_id="+dealerId);
				}
				if(change_date_start!=null&&!"".equals(change_date_start))
					sb.append(" and uif.change_date>=to_date('" + change_date_start+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
				if(change_date_end!=null&&!"".equals(change_date_end))
					sb.append(" and uif.change_date<=to_date('" + change_date_end+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
				sb.append(" order by change_date desc\n");

				
				PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), 15, curPage);
		        return ps;
		    }
		 /**
		  * 服务站人员联系方式查询
		  */
		 public List<Map<String, Object>> queryDealerContactList(RequestWrapper req,
				 String dealerId,String perPose,String perPhone,String perName,String condealerId,String status){

				StringBuffer sb = new StringBuffer();
				sb.append("SELECT CCP.ID,\n");
				sb.append("       CCP.PER_PHONE,\n");
				sb.append("       TU.NAME,\n");
				sb.append("       CCP.CREATE_DATE,\n");
				sb.append("       CCP.DEALER_ID,\n");
				sb.append("       CCP.PER_NAME,\n");
				sb.append("       CCP.PER_POSE,\n");
				sb.append("       CCP.PER_REMARK,\n");
				sb.append("       CCP.STATUS,\n");
				sb.append("       TD.HOT_LINE_PHONE,\n");
				sb.append("       TD.DEALER_CODE,\n");
				sb.append("       TD.DEALER_SHORTNAME\n");
				sb.append("  FROM TM_CLAIM_CONTACT_PHONE CCP, TC_USER TU\n,TM_DEALER TD");
				sb.append(" WHERE CCP.USER_ID = TU.USER_ID AND CCP.DEALER_ID=TD.DEALER_ID(+)\n");
				

				if(dealerId!=null&&!"".equals(dealerId)){
					
					sb.append(" and CCP.dealer_id="+dealerId);
				}
				if(condealerId!=null&&!"".equals(condealerId)){
					
					sb.append(" and CCP.dealer_id in ("+condealerId+")");
				}
				if(perName!=null&&!"".equals(perName)){
					
					sb.append(" AND CCP.PER_NAME LIKE '%"+perName+"%'\n");
				}
				if(perPose!=null&&!"".equals(perPose)){
									
					sb.append(" AND CCP.PER_POSE="+perPose+"\n");
				}
				if(perPhone!=null&&!"".equals(perPhone)){
					
					sb.append(" AND CCP.PER_PHONE LIKE '%"+perPhone+"%'\n");
				}
				if(status!=null&&!"".equals(status)){
					
					sb.append(" AND CCP.STATUS="+status+"\n");
				}
				sb.append(" order by CCP.CREATE_BY desc\n");

				List<Map<String, Object>> result = super.pageQuery(sb.toString(),null, this.getFunName());
		        return result;
		    }
		 /**
		  * 服务站人员联系方式修改记录查询(分页车厂)
		  */
		 public PageResult<Map<String, Object>> queryContactChangeHistoryPg(RequestWrapper req,
				 String dealerId,String perPose,String perPhone,String perName,String condealerId,String status,
				 int curPage, int pageSize){


			 StringBuffer sb = new StringBuffer();
				sb.append("SELECT CCP.ID,\n");
				sb.append("       CCP.PER_PHONE,\n");
				sb.append("       TU.NAME,\n");
				sb.append("       CCP.CREATE_DATE,\n");
				sb.append("       CCP.DEALER_ID,\n");
				sb.append("       CCP.PER_NAME,\n");
				sb.append("       CCP.PER_POSE,\n");
				sb.append("       CCP.PER_REMARK,\n");
				sb.append("       CCP.STATUS,\n");
				sb.append("       TD.HOT_LINE_PHONE,\n");
				sb.append("       TD.DEALER_CODE,\n");
				sb.append("       TD.DEALER_SHORTNAME\n");
				sb.append("  FROM TM_CLAIM_CONTACT_PHONE CCP, TC_USER TU\n,TM_DEALER TD");
				sb.append(" WHERE CCP.USER_ID = TU.USER_ID AND CCP.DEALER_ID=TD.DEALER_ID(+)\n");
				

				if(dealerId!=null&&!"".equals(dealerId)){
					
					sb.append(" and CCP.dealer_id="+dealerId);
				}
				if(condealerId!=null&&!"".equals(condealerId)){
					
					sb.append(" and CCP.dealer_id in ("+condealerId+")");
				}
				if(perName!=null&&!"".equals(perName)){
					
					sb.append(" AND CCP.PER_NAME LIKE '%"+perName+"%'\n");
				}
				if(perPose!=null&&!"".equals(perPose)){
									
					sb.append(" AND CCP.PER_POSE="+perPose+"\n");
				}
				if(perPhone!=null&&!"".equals(perPhone)){
					
					sb.append(" AND CCP.PER_PHONE LIKE '%"+perPhone+"%'\n");
				}
				if(status!=null&&!"".equals(status)){
					
					sb.append(" AND CCP.STATUS="+status+"\n");
				}
				sb.append(" order by CCP.CREATE_BY desc\n");

				
				PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), 15, curPage);
		        return ps;
		    }
		 /**
		  * 查询故障代码
		  * @param malCode
		  * @param malName
		  * @param pageSize
		  * @param curPage
		  * @return
		  */
		 public PageResult<Map<String,Object>> queryMal(String malCode,String malName,int pageSize, int curPage){
		        StringBuffer sql= new StringBuffer();
		        sql.append("SELECT  m.mal_id,m.mal_code,m.mal_name\n");
		        sql.append("FROM Tt_As_Wr_Malfunction m\n");
		        sql.append("WHERE 1=1\n");
		        if(Utility.testString(malCode)){
		        	  sql.append("AND m.mal_code LIKE '%"+malCode.toUpperCase()+"%'\n");
		        }
		        if(Utility.testString(malName)){
		        	sql.append("AND m.mal_name LIKE '%"+malName+"%'"); 
		        }
		      
		    	return  pageQuery(sql.toString(), null, this.getFunName(),pageSize , curPage);
		    }
		 
		 /**
		  * 查询工时代码
		  * @param malCode
		  * @param malName
		  * @param pageSize
		  * @param curPage
		  * @return
		  */
		 public PageResult<Map<String,Object>> queryWorkhoursCode(String workhoursCode,String workhoursName,int pageSize, int curPage){
		        StringBuffer sql= new StringBuffer();
		        sql.append("SELECT  *\n");
		        sql.append("FROM tt_accessory_price_maintain \n");
		        sql.append("WHERE 1=1\n");
		        if(Utility.testString(workhoursCode)){
		        	  sql.append("AND WORKHOUR_CODE LIKE '%"+workhoursCode+"%'\n");
		        }
		        if(Utility.testString(workhoursName)){
		        	sql.append("AND WORKHOUR_NAME LIKE '%"+workhoursName+"%'"); 
		        }
		    	return  pageQuery(sql.toString(), null, this.getFunName(),pageSize , curPage);
		    }

		 public PageResult<TtAsWrApplicationExtBean> applicationSystemQuery(AclUserBean user, Map<String, String> map, List params,int pageSize, int curPage) {
		    	String yieldly = map.get("yieldly");
				StringBuffer sqlStr = new StringBuffer(); 
				sqlStr.append("SELECT A.*,\n" );
				sqlStr.append(" (select count(1) from tt_as_wr_application_counter apc ");
				sqlStr.append(" where apc.id=a.id) as is_counter, ");
				sqlStr.append("       B.DEALER_CODE AS DEALER_CODE,\n" );
				//sqlStr.append("       B.DEALER_NAME AS DEALER_NAME,\n" );
				sqlStr.append("       V.DELIVERER,v.model,\n" );
				sqlStr.append("       V.DELIVERER_PHONE || ' ' || v.deliverer_mobile DELIVERER_PHONE,ba.area_name as yieldly_name\n" );
				sqlStr.append("  FROM TT_AS_REPAIR_ORDER V, TT_AS_WR_APPLICATION A, TM_DEALER B,tm_business_area ba \n" );
				sqlStr.append(" WHERE 1 = 1 and A.is_invoice = 1 and A.claim_type not in (10661002,10661011) and ba.area_id(+) = a.yieldly\n" );
				sqlStr.append("   AND B.DEALER_ID = A.DEALER_ID(+)\n" );
				sqlStr.append("   AND A.RO_NO = V.RO_NO(+)\n" );
				if(yieldly!=null){
					sqlStr.append("   AND A.yieldly = '"+yieldly+"'\n" );
				}
				if (Utility.testString(map.get("dealerName"))) {
					sqlStr.append(" AND b.DEALER_name like'%" + map.get("dealerName")
							+ "%' \n");
				}
				
				if (Utility.testString(map.get("dealerCode"))) {
					
					sqlStr.append(" AND A.dealer_id=(select d.dealer_id from tm_dealer d where d.dealer_code='"+map.get("dealerCode")+"') \n");
				}
//				if (Utility.testString(map.get("dealerCode"))) {
//					sqlStr.append(Utility.getConSqlByParamForEqual(map
//							.get("dealerCode"), params, "b", "dealer_code"));
//				}
				// 经销商代码
				if (Utility.testString(map.get("dealerId"))) {
					sqlStr.append("AND DECODE(a.SECOND_DEALER_ID, null, b.DEALER_ID, a.SECOND_DEALER_ID) IN\n");
					sqlStr.append("      (SELECT d.DEALER_ID\n");
					sqlStr.append("         FROM TM_DEALER D\n");
					sqlStr.append("        START WITH d.DEALER_ID = "+map.get("dealerId")+"\n");
					sqlStr.append("       CONNECT BY PRIOR d.DEALER_ID = d.PARENT_DEALER_D)"); 

				}
				// 索赔单号
				if (Utility.testString(map.get("claimNo"))) {
					sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map.get("claimNo")
							+ "%' \n");
				}
				// 工单号
				if (Utility.testString(map.get("roNo"))) {
					sqlStr.append(" AND A.RO_NO LIKE '%" + map.get("roNo") + "%' \n");
				}
				// 行号
				if (Utility.testString(map.get("lineNo"))) {
					sqlStr.append(" AND A.LINE_NO='" + map.get("lineNo") + "' \n");
				}
				// 索赔类型
				if (Utility.testString(map.get("claimType"))) {
					sqlStr
							.append(" AND A.CLAIM_TYPE='" + map.get("claimType")
									+ "' \n");
				}
				// 车辆VIN码
				if (Utility.testString(map.get("vin"))) {// VIN涓嶄负绌 
					sqlStr.append(" and a.vin in ("+map.get("vin")+")\n");
				}
				// 工单开始时间
				if (Utility.testString(map.get("roStartdate"))) {
					sqlStr
							.append(" AND A.CREATE_DATE >= to_date('"
									+ map.get("roStartdate")
									+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') \n");
				}
				// 工单结束时间
				if (Utility.testString(map.get("roEnddate"))) {
					sqlStr.append(" AND A.CREATE_DATE <= to_date('"
							+ map.get("roEnddate")
							+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
				}
				// 审核时间查询条件
				if(Utility.testString(map.get("approveDate"))){
					sqlStr.append(" AND a.auditing_date >= to_date('"
							+map.get("approveDate")
							+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
				}
				if(Utility.testString(map.get("approveDate2"))){
					sqlStr.append(" AND a.auditing_date <= to_date('"
							+map.get("approveDate2")
							+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
				}
				if(Utility.testString(map.get("balanceApproveDate"))){
					sqlStr.append(" AND a.account_date >= to_date('"
							+map.get("balanceApproveDate")
							+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
				}
				if(Utility.testString(map.get("balanceApproveDate2"))){
					sqlStr.append(" AND a.account_date <= to_date('"
							+map.get("balanceApproveDate2")
							+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
				}
				if(Utility.testString(map.get("isSecond"))){
					if("1".equals(map.get("isSecond"))){
						sqlStr.append(" AND a.second_dealer_id is not null \n");
					}else{
						sqlStr.append(" AND a.second_dealer_id is null \n");
					}
				}
				// 申请状态
				if (Utility.testString(map.get("status"))) {
					sqlStr.append(" AND A.STATUS='" + map.get("status") + "' \n");

				} else if (!"track".equals(map.get("track"))) {
					sqlStr.append(" AND (A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_01
							+ "'  \n");
					sqlStr.append(" OR A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_06
							+ "')  \n");

				} else if (!"isApp".equals(map.get("isApp"))) {
					sqlStr.append(" AND (A.STATUS<" + Constant.CLAIM_APPLY_ORD_TYPE_01
							+ " OR A.STATUS >" + Constant.CLAIM_APPLY_ORD_TYPE_01 +") \n");
				}
				if (Utility.testString(map.get("deliverer"))) {
					sqlStr.append(" and v.deliverer like '%"+map.get("deliverer")+"%' ");
				}
				if (Utility.testString(map.get("delivererPhone"))) {
					sqlStr.append(" and v.deliverer_phone like '%"+map.get("delivererPhone")+"%' ");
				}
				sqlStr.append(" ORDER BY A.ID DESC ");
				logger.info("-----------------------"+sqlStr.toString());
				PageResult<TtAsWrApplicationExtBean> ps = pageQuery(
						TtAsWrApplicationExtBean.class, sqlStr.toString(), params,
						pageSize, curPage);
				return ps;
			}
		/**
		 * 根据工时ID添加数据来进行插入
		 * @param labourid
		 * @param claimid 
		 * @param authremark 
		 */
		public void claimSystemLabourids(String labourid, String claimid, String authremark) {
			String labouritem="insert into tt_as_wr_labouritem_counter select * from tt_as_wr_labouritem t where t.labour_id="+labourid+" and t.id="+claimid;
			String partsitem="insert into tt_as_wr_partsitem_counter select * from tt_as_wr_partsitem t where t.id="+claimid+"and t.wr_labourcode=(select a.wr_labourcode from tt_as_wr_labouritem a where a.labour_id="+labourid+" and a.id="+claimid+")";
			
			logger.info("反索赔工时表SQL："+labouritem);
			dao.insert(labouritem);
			logger.info("反索赔配件表SQL："+partsitem);
			dao.insert(partsitem);
			
			/**
			 * 审核意见  AUTH_REMARK
			 */
			String labouritemSql="update tt_as_wr_labouritem_counter t set t.AUTH_REMARK='"+authremark+"' where t.LABOUR_ID="+labourid;
			logger.info("反索赔工时表更新SQL："+labouritemSql);
			dao.update(labouritemSql, null);
		}

		public void claimSystemLabourids(String claimid, Double balance_amount,Double gstemp, Double cltemp, Double accTemp, Double comTemp, String username, String currtime) {
			String application="insert into tt_as_wr_application_counter select * from tt_as_wr_application t where t.id="+claimid;
			//插入三条数据
			logger.info("反索赔单表SQL："+application);
			dao.insert(application);
			/**更新两条（   
				t.balance_amount   扣除总金额   
				t.balance_part_amount  结算配件总金额
				t.balance_labour_amount  结算工时总金额
				t.is_invoice 0 是否开票
				t.report_date创建时间 
			*/
			String applicationSql="update tt_as_wr_application_counter t set t.balance_no=null,t.report_date = sysdate , t.balance_amount = "+balance_amount+" ,t.ATTACHMENT_FILE1 = '"+username+"' ,t.ATTACHMENT_FILE2 = '"+currtime+"' ,t.balance_part_amount ="+cltemp+" ,t.balance_labour_amount = "+gstemp+" ,t.accessories_price = "+accTemp+" ,t.compensation_money = "+comTemp+" , t.is_invoice = 0 where t.id ="+claimid;
			logger.info("反索赔表更新SQL："+applicationSql);
			dao.update(applicationSql, null);
		}

		public PageResult<TtAsWrApplicationExtBean> applicationSelectQuery(AclUserBean user, Map<String, String> map, List params,int pageSize, int curPage) {
	    	String yieldly = map.get("yieldly");
			StringBuffer sqlStr = new StringBuffer(); 
			sqlStr.append("SELECT A.*,\n" );
			sqlStr.append(" case when a.create_date<to_date('2015-12-01','yyyy-mm-dd') ");
			sqlStr.append(" then (select distinct ttu.name from tc_user ttu where ttu.user_id=a.attachment_file1) ");
			sqlStr.append(" else U.NAME end AS USERNAME, ");
			sqlStr.append(" B.DEALER_CODE AS DEALER_CODE,\n" );
			//sqlStr.append("       B.DEALER_NAME AS DEALER_NAME,\n" );
			sqlStr.append("       V.DELIVERER,v.model,\n" );
			sqlStr.append("       V.DELIVERER_PHONE || ' ' || v.deliverer_mobile DELIVERER_PHONE,ba.area_name as yieldly_name\n" );
			sqlStr.append("  FROM tt_as_wr_application_counter A ");
			sqlStr.append(" left join tT_AS_REPAIR_ORDER V on A.RO_NO = V.RO_NO ");
			sqlStr.append(" left join TC_USER U on u.user_id=a.update_by ");
			sqlStr.append(" left join TM_DEALER B on b.dealer_id=a.dealer_id ");
			sqlStr.append(" left join tm_business_area ba on ba.area_id=a.yieldly ");
			sqlStr.append(" WHERE 1 = 1   and  A.claim_type not in (10661002,10661011) \n" );
			if(yieldly!=null){
				sqlStr.append("   AND A.yieldly = '"+yieldly+"'\n" );
			}
			if (Utility.testString(map.get("dealerCode"))) {
				
				sqlStr.append(" AND A.dealer_id=(select d.dealer_id from tm_dealer d where d.dealer_code='"+map.get("dealerCode")+"') \n");
			}
			if (Utility.testString(map.get("dealerName"))) {
				sqlStr.append(" AND A.DEALER_name like'%" + map.get("dealerName")
						+ "%' \n");
			}
//			if (Utility.testString(map.get("dealerCode"))) {
//				sqlStr.append(Utility.getConSqlByParamForEqual(map
//						.get("dealerCode"), params, "b", "dealer_code"));
//			}
			// 经销商代码
			if (Utility.testString(map.get("dealerId"))) {
				sqlStr.append("AND DECODE(a.SECOND_DEALER_ID, null, b.DEALER_ID, a.SECOND_DEALER_ID) IN\n");
				sqlStr.append("      (SELECT d.DEALER_ID\n");
				sqlStr.append("         FROM TM_DEALER D\n");
				sqlStr.append("        START WITH d.DEALER_ID = "+map.get("dealerId")+"\n");
				sqlStr.append("       CONNECT BY PRIOR d.DEALER_ID = d.PARENT_DEALER_D)"); 

			}
			// 索赔单号
			if (Utility.testString(map.get("claimNo"))) {
				sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map.get("claimNo")
						+ "%' \n");
			}
			// 工单号
			if (Utility.testString(map.get("roNo"))) {
				sqlStr.append(" AND A.RO_NO LIKE '%" + map.get("roNo") + "%' \n");
			}
			// 行号
			if (Utility.testString(map.get("lineNo"))) {
				sqlStr.append(" AND A.LINE_NO='" + map.get("lineNo") + "' \n");
			}
			// 索赔类型
			if (Utility.testString(map.get("claimType"))) {
				sqlStr
						.append(" AND A.CLAIM_TYPE='" + map.get("claimType")
								+ "' \n");
			}
			// 车辆VIN码
			if (Utility.testString(map.get("vin"))) {// VIN涓嶄负绌 
				sqlStr.append(" and a.vin in ("+map.get("vin")+")\n");
			}
			// 工单开始时间
			if (Utility.testString(map.get("roStartdate"))) {
				sqlStr
						.append(" AND A.CREATE_DATE >= to_date('"
								+ map.get("roStartdate")
								+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			// 工单结束时间
			if (Utility.testString(map.get("roEnddate"))) {
				sqlStr.append(" AND A.CREATE_DATE <= to_date('"
						+ map.get("roEnddate")
						+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			// 审核时间查询条件
			if(Utility.testString(map.get("approveDate"))){
				sqlStr.append(" AND a.auditing_date >= to_date('"
						+map.get("approveDate")
						+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
			}
			if(Utility.testString(map.get("approveDate2"))){
				sqlStr.append(" AND a.auditing_date <= to_date('"
						+map.get("approveDate2")
						+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
			}
			if(Utility.testString(map.get("balanceApproveDate"))){
				sqlStr.append(" AND a.account_date >= to_date('"
						+map.get("balanceApproveDate")
						+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
			}
			if(Utility.testString(map.get("balanceApproveDate2"))){
				sqlStr.append(" AND a.account_date <= to_date('"
						+map.get("balanceApproveDate2")
						+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
			}
			if(Utility.testString(map.get("isSecond"))){
				if("1".equals(map.get("isSecond"))){
					sqlStr.append(" AND a.second_dealer_id is not null \n");
				}else{
					sqlStr.append(" AND a.second_dealer_id is null \n");
				}
			}
			// 申请状态
			if (Utility.testString(map.get("status"))) {
				sqlStr.append(" AND A.STATUS='" + map.get("status") + "' \n");

			} else if (!"track".equals(map.get("track"))) {
				sqlStr.append(" AND (A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_01
						+ "'  \n");
				sqlStr.append(" OR A.STATUS='" + Constant.CLAIM_APPLY_ORD_TYPE_06
						+ "')  \n");

			} else if (!"isApp".equals(map.get("isApp"))) {
				sqlStr.append(" AND (A.STATUS<" + Constant.CLAIM_APPLY_ORD_TYPE_01
						+ " OR A.STATUS >" + Constant.CLAIM_APPLY_ORD_TYPE_01 +") \n");
			}
			if (Utility.testString(map.get("deliverer"))) {
				sqlStr.append(" and v.deliverer like '%"+map.get("deliverer")+"%' ");
			}
			if (Utility.testString(map.get("delivererPhone"))) {
				sqlStr.append(" and v.deliverer_phone like '%"+map.get("delivererPhone")+"%' ");
			}
			sqlStr.append(" ORDER BY A.ID DESC ");
			logger.info("-----------------------"+sqlStr.toString());
			PageResult<TtAsWrApplicationExtBean> ps = pageQuery(
					TtAsWrApplicationExtBean.class, sqlStr.toString(), params,
					pageSize, curPage);
			return ps;
		}
		/**
		 * 查询反索赔配件
		 * @param id
		 * @return
		 */
		public List<ClaimListBean> queryPartCounterById(String id) {
			StringBuffer sql = new StringBuffer();
			List<TtAsWrApplicationPO> lsTawp = new ArrayList<TtAsWrApplicationPO>();
			// List<TtAsWrPartsitemPO> allLs = new LinkedList<TtAsWrPartsitemPO>();
			List<TtAsWrPartsitemPO> ls = new ArrayList<TtAsWrPartsitemPO>();
			List<TtAsWrPartsitemPO> ls1 = new ArrayList<TtAsWrPartsitemPO>();
			List<TtAsWrPartsitemPO> ls2 = new ArrayList<TtAsWrPartsitemPO>();
			List<ClaimListBean> allLs = new LinkedList<ClaimListBean>();
			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// Map map = new HashMap();
			 TtAsWrApplicationPO tawp = new TtAsWrApplicationPO(); 
			try {
				
				  tawp.setId(Utility.getLong(id)); 
				  lsTawp = select(tawp);
				  int staust= lsTawp.get(0).getStatus();
				  Date date = format.parse("2012-05-24 17:00:00");
					System.out.println(lsTawp.get(0).getCreateDate());
					String date2 = format.format(lsTawp.get(0).getCreateDate());
					Date date1 = format.parse(date2);
					sql = new StringBuffer();
					sql.append("select a.part_id,\n");
					sql.append("       a.id,\n");  
					sql.append("   d.DEDUCT_REMARK,a.main_part_code,");
					sql.append("       a.part_code,\n");  
					sql.append("       a.part_name,\n");  
					sql.append("       a.down_part_code,\n");  
					sql.append("       a.down_part_name,\n");  
					sql.append("       a.quantity,\n");  
					sql.append("       a.price,\n");  
					sql.append("       a.amount,\n");  
					sql.append("       a.producer_code,\n");  
					sql.append("       a.producer_name,\n");  
					sql.append("       a.is_mainpart,\n");  
					sql.append("       a.remark,\n");  
					sql.append("       a.auth_code,\n");  
					sql.append("       a.create_date,\n");  
					sql.append("       a.create_by,\n");  
					sql.append("       a.debit_part_id,\n");  
					sql.append("       a.return_num,\n");  
					sql.append("       a.old_part_code,\n");  
					sql.append("       a.balance_amount,\n");  
					sql.append("       a.deduct_amount,\n");  
					sql.append("       a.b_mp_code,\n");  
					sql.append("       a.wr_labourcode,\n");  
					sql.append("       a.down_product_code,\n");  
					sql.append("       a.down_product_name,\n");  
					sql.append("       nvl(a.is_gua,0) as is_gua,\n");  
					sql.append("       a.is_agree,\n");  
					sql.append("       a.pay_type,\n");   
					sql.append("       a.balance_quantity,\n");  
					sql.append("       a.auth_remark,\n");  
					sql.append("       a.is_claim,\n");  
					sql.append("       a.balance_price,\n");  
					sql.append("       a.apply_quantity,\n");  
					sql.append("       a.apply_price,\n");  
					sql.append("       a.apply_amount,\n");  
					sql.append("       a.responsibility_type,a.real_part_id,  c.code_id, c.code_desc, l.wr_labourname,\n");  
					sql.append("       (SELECT COUNT(1) FROM TM_PT_PART_TYPE T, TM_PT_PART_BASE B WHERE T.IS_MAX = 1 AND a.PART_ID = B.PART_ID AND B.PART_TYPE = T.ID) as is_fore\n");  
					sql.append(" from tt_as_wr_partsitem_counter a left join tc_code c on c.code_id=a.responsibility_type \n");
					sql.append("  left join TT_AS_WR_APPLICATION f on f.ID = a.ID  ");
					sql.append("  left join (  SELECT gg.PART_ID,gg.CLAIM_NO,max(gg.DEDUCT_REMARK)  DEDUCT_REMARK  from  tt_as_wr_old_returned_detail gg   group by gg.PART_ID,gg.CLAIM_NO )  d  on d.PART_ID = a.PART_ID and d.CLAIM_NO = f.CLAIM_NO ");
					sql.append(" ,tt_as_wr_labouritem l  ");
					sql.append(" WHERE 1=1 and l.wr_labourcode=a.wr_labourcode and l.id=a.id ");
					sql.append(" AND A.ID='" + id + "'");
					sql.append(" order by a.id	");
					if(date1.after(date)){
						sql.append(" , a.wr_labourcode,a.part_id  ");
					}else{
					sql.append(" , a.wr_labourcode,a.part_id desc ");
					}
					ls = select(TtAsWrPartsitemBean.class, sql.toString(), null);
					if (ls == null) {

					} else {
						
						List<Object> lsTm = new ArrayList<Object>();
						if (ls.size() > 0) {
							for (int i = 0; i < ls.size(); i++) {
								ClaimListBean clb = new ClaimListBean();
								if(ls2 != null && ls2.size() > 0)
								{
									for(int k = 0; k < ls2.size();k++)
									{
										if(ls2.get(k).getPartCode().equals(ls.get(i).getPartCode()) )
										{
											ls.get(i).setBalanceQuantity(ls2.get(k).getBalanceQuantity());
											ls.get(i).setBalanceAmount(ls2.get(k).getBalanceAmount());
											clb.setMain(ls.get(i));
										}
									}
								}else
								{
									clb.setMain(ls.get(i));
								}
								
								// allLs.add(ls.get(i));
								// 取附属配件
								/*sql1.append(" SELECT A.* FROM tt_as_wr_partsitem_counter A ");
								sql1.append(" WHERE 1=1 ");*/
								StringBuilder sql1 = new StringBuilder();
								sql1.append("select a.part_id,\n");
								sql1.append("       a.id,\n");  
								sql.append("   d.DEDUCT_REMARK,");
								sql1.append("       a.part_code,\n");  
								sql1.append("       a.part_name,\n");  
								sql1.append("       a.down_part_code,\n");  
								sql1.append("       a.down_part_name,\n");  
								sql1.append("       a.quantity,\n");  
								sql1.append("       a.price,\n");  
								sql1.append("       a.amount,\n");  
								sql1.append("       a.producer_code,\n");  
								sql1.append("       a.producer_name,\n");  
								sql1.append("       a.is_mainpart,\n");  
								sql1.append("       a.remark,\n");  
								sql1.append("       a.auth_code,\n");  
								sql1.append("       a.create_date,\n");  
								sql1.append("       a.create_by,\n");  
								sql1.append("       a.debit_part_id,\n");  
								sql1.append("       a.return_num,\n");  
								sql1.append("       a.old_part_code,\n");  
								sql1.append("       a.balance_amount,\n");  
								sql1.append("       a.deduct_amount,\n");  
								sql1.append("       a.b_mp_code,\n");  
								sql1.append("       a.wr_labourcode,\n");  
								sql1.append("       a.down_product_code,\n");  
								sql1.append("       a.down_product_name,\n");  
								sql1.append("       nvl(a.is_gua,0) is_gua,\n");  
								sql1.append("       a.is_agree,\n");  
								sql1.append("       a.pay_type,\n");  
								sql1.append("       a.balance_quantity,\n");  
								sql1.append("       a.auth_remark,\n");  
								sql1.append("       a.is_claim,\n");  
								sql1.append("       a.balance_price,\n");  
								sql1.append("       a.apply_quantity,\n");  
								sql1.append("       a.apply_price,\n");  
								sql1.append("       a.apply_amount,a.real_part_id,c.code_id, c.code_desc, l.wr_labourname,\n");  
								//添加最后一句的目的是为了在页面上显示是否为监控配件！
								sql1.append("       (SELECT COUNT(1) FROM TM_PT_PART_TYPE T, TM_PT_PART_BASE B WHERE T.IS_MAX = 1 AND a.PART_ID = B.PART_ID AND B.PART_TYPE = T.ID) as is_fore\n");  
								sql1.append(" from tt_as_wr_partsitem_counter a left join tc_code c on c.code_id=a.responsibility_type \n");
								sql1.append("  left join TT_AS_WR_APPLICATION f on f.ID = a.ID  ");
								sql1.append("  left join tt_as_wr_old_returned_detail d on d.PART_ID = a.PART_ID and d.CLAIM_NO = f.CLAIM_NO ");
								sql1.append(" ,tt_as_wr_labouritem l  ");
								sql1.append(" where 1=1 and l.wr_labourcode=a.wr_labourcode and l.id=a.id \n");
								sql1.append(" AND A.ID='" + id + "'");
								sql1.append(" AND A.B_MP_CODE = '"
										+ ls.get(i).getPartCode() + "'");
								sql.append(" order by a.id	");
								if(date1.after(date)){
									sql.append(" , a.part_id  ");
								}else{
								sql.append(" , a.part_id desc ");
								}
								ls1 = select(TtAsWrPartsitemBean.class, sql1.toString(),null);
								if (ls1 != null) {
									if (ls1.size() > 0) {
										if(ls2 != null && ls2.size() > 0)
										{
											for(int k = 0; k < ls2.size();k++){
												for (int j = 0; j < ls1.size(); j++) {
													if(ls2.get(k).getPartCode().equals(ls1.get(j).getPartCode()) )
													{
														ls1.get(j).setBalanceQuantity(ls2.get(k).getBalanceQuantity());
														ls1.get(j).setBalanceAmount(ls2.get(k).getBalanceAmount());
														lsTm.add(ls1.get(j));
													}
													
												}
											}
										}else
										{
											for (int j = 0; j < ls1.size(); j++) 
											{
												lsTm.add(ls1.get(j));
											}
										}
										
										
										clb.setAdditional(lsTm);
									}
								}
								if(clb != null && clb.getMain() != null )
								{
									allLs.add(clb);
								}
								
							}
						}
					}
				 
				// 取主配件
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
			return allLs;
		}
		/**
		 * 查询配件信息
		 * @param id
		 * @return
		 */
		public List<ClaimListBean> queryItemCounterById(String id) {
			 StringBuffer sql = new StringBuffer();
			    List<ClaimListBean> allLs = new LinkedList<ClaimListBean>();
			    String bmlCode = "";
			    // 取主工时
			    sql.append("select l.labour_id, \n");
			    sql.append("       l.id,\n");  
			    sql.append("       l.wr_labourcode,\n");  
			    sql.append("       l.wr_labourname,\n");  
			    sql.append("       l.labour_code,\n");  
			    sql.append("       l.labour_name,\n");  
			    sql.append("       l.labour_hours,\n");  
			    sql.append("       l.labour_quantity,\n");  
			    sql.append("       l.labour_price,\n");  
			    sql.append("       l.labour_amount,\n");  
			    sql.append("       l.is_mainlabour,\n");  
			    sql.append("       l.auth_code,\n");  
			    sql.append("       l.create_date,\n");  
			    sql.append("       l.create_by,\n");  
			    sql.append("       l.debit_labour_id,\n");  
			    sql.append("       l.balance_amount,\n");  
			    sql.append("       l.deduct_amount,\n");  
			    sql.append("       l.b_ml_code,\n");  
			    sql.append("       l.trouble_code,\n");  
			    sql.append("       l.damage_type,\n");  
			    sql.append("       l.damage_area,\n");  
			    sql.append("       l.damage_degree,\n");  
			    sql.append("       l.labour_count,\n");  
			    sql.append("       l.is_agree,\n");  
			    sql.append("       l.trouble_code_name,\n");  
			    sql.append("       l.damage_type_name,\n");  
			    sql.append("       l.damage_area_name,\n");  
			    sql.append("       l.damage_degree_name,\n"); 
			    sql.append("       l.pay_type,\n");  
			    sql.append("       l.balance_quantity,\n");  
			    sql.append("       l.first_part,\n");  
			    sql.append("       l.auth_remark,\n");  
			    sql.append("       l.is_claim,\n");  
			    sql.append("       l.labour_quantity_hidden,\n");  
			    sql.append("       l.balance_price,\n");  
			    sql.append("       l.apply_quantity,\n");  
			    sql.append("       l.apply_price,\n");  
			    sql.append("       l.apply_amount,l.trouble_type,f.mal_code||'--'||f.mal_name mal_name,\n");  
			    sql.append("       (select count(1) from tt_as_wr_authmonitorlab ll where ll.labour_operation_no = l.wr_labourcode and ll.is_del=0) as is_fore\n");  
			    sql.append("  from tt_as_wr_labouritem_counter l left join tt_as_wr_malfunction f on f.mal_id = l.trouble_type \n");
			    sql.append(" WHERE l.ID='" + id + "'");
			    
			    sql.append(" order by l.id,l.wr_labourcode,l.first_part");
			    List<TtAsWrLabouritemBean> ls = select(TtAsWrLabouritemBean.class, sql
			        .toString(), null);
			    if (ls == null) {

			    } else {
			      if (ls.size() == 0) {
			        // return null;
			      } else {
			        for (int i = 0; i < ls.size(); i++) {

			          ClaimListBean clb = new ClaimListBean();
			          TtAsWrLabouritemBean tawp = new TtAsWrLabouritemBean();
			          List<Object> lsTm = new ArrayList<Object>();
			          tawp = ls.get(i);
			          bmlCode = tawp.getWrLabourcode();
			          clb.setMain(tawp);
			          // allLs.add(tawp);
			          // 取附属工时
			          StringBuffer sql1 = new StringBuffer();
			          sql1.append("select l.labour_id,\n");
			          sql1.append("       l.id,\n");  
			          sql1.append("       l.wr_labourcode,\n");  
			          sql1.append("       l.wr_labourname,\n");  
			          sql1.append("       l.labour_code,\n");  
			          sql1.append("       l.labour_name,\n");  
			          sql1.append("       l.labour_hours,\n");  
			          sql1.append("       l.labour_quantity,\n");  
			          sql1.append("       l.labour_price,\n");  
			          sql1.append("       l.labour_amount,\n");  
			          sql1.append("       l.is_mainlabour,\n");  
			          sql1.append("       l.auth_code,\n");  
			          sql1.append("       l.create_date,\n");  
			          sql1.append("       l.create_by,\n");  
			          sql1.append("       l.debit_labour_id,\n");  
			          sql1.append("       l.balance_amount,\n");  
			          sql1.append("       l.deduct_amount,\n");  
			          sql1.append("       l.b_ml_code,\n");  
			          sql1.append("       l.trouble_code,\n");  
			          sql1.append("       l.damage_type,\n");  
			          sql1.append("       l.damage_area,\n");  
			          sql1.append("       l.damage_degree,\n");  
			          sql1.append("       l.labour_count,\n");  
			          sql1.append("       l.is_agree,\n");  
			          sql1.append("       l.trouble_code_name,\n");  
			          sql1.append("       l.damage_type_name,\n");  
			          sql1.append("       l.damage_area_name,\n");  
			          sql1.append("       l.damage_degree_name,\n");  
			          sql1.append("       l.pay_type,\n");  
			          sql1.append("       l.balance_quantity,\n");  
			          sql1.append("       l.first_part,\n");  
			          sql1.append("       l.auth_remark,\n");  
			          sql1.append("       l.is_claim,\n");  
			          sql1.append("       l.labour_quantity_hidden,\n");  
			          sql1.append("       l.balance_price,\n");  
			          sql1.append("       l.apply_quantity,\n");  
			          sql1.append("       l.apply_price,\n");  
			          sql1.append("       l.apply_amount,l.trouble_type,f.mal_code||'--'||f.mal_name mal_name,\n");  
			          sql1.append("       (select count(1) from tt_as_wr_authmonitorlab ll where ll.labour_operation_no = l.wr_labourcode) as is_fore\n");  
			          sql1.append("  from tt_as_wr_labouritem_counter l  left join tt_as_wr_malfunction f on f.mal_id = l.trouble_type \n");
			          sql1.append(" WHERE 1=1 ");
			          sql1.append(" AND l.ID='" + id + "'");
			          sql1.append(" and l.B_ML_CODE='" + bmlCode + "'");
			          sql1.append(" order by l.id,l.first_part");
			          List<TtAsWrLabouritemBean> ls1 = select(TtAsWrLabouritemBean.class, sql1.toString(), null);
			          if (ls1 == null) {
			            // return allLs;
			          } else {
			            for (int j = 0; j < ls1.size(); j++) {
			              lsTm.add(ls1.get(j));
			            }
			            clb.setAdditional(lsTm);
			          }
			          allLs.add(clb);
			        }
			      }
			    }
			    // 返回合并工时
			    return allLs;
		  }
		/**
		 * 反索赔主表金额修改
		 * @param balance_amount
		 * @param claimid
		 * @param cltemp 
		 * @param gstemp 
		 * @param comTemp 
		 * @param accTemp 
		 */
		public void updateClaimCounter(Double balance_amount, String claimid, Double gstemp, Double cltemp, Double accTemp, Double comTemp) {
			String sql="update tt_as_wr_application_counter t set t.balance_amount="+(balance_amount-gstemp-cltemp-accTemp-comTemp)+" where t.id="+claimid;
			dao.update(sql, null);
		}
		/**
		 * 插入条形码
		 * @param partid
		 * @param xuhao
		 * @param dealerId
		 * @param dealerCode
		 */
		public void insertBarCode(String partid,String xuhao,Long dealerId,String dealerCode) {
			DealerClaimReportDao dao=new DealerClaimReportDao();
			dao.insertBarcode(partid, null, dealerId, xuhao, dealerCode);
		}
		
		/**
		 * 显示详细
		 * @param id
		 * @return
		 */
		public Map<String, Object> queryDealerConver(String id){
			StringBuffer sql = new StringBuffer("select ID,DEALER_ID," +
					"TO_CHAR(PRINT_DATE,'YYYY-MM-DD HH:MM:SS') PRINT_DATE," +
					"TO_CHAR(STAR_DATE,'YYYY-MM-DD HH:MM:SS') STAR_DATE," +
					"TO_CHAR(END_DATE,'YYYY-MM-DD HH:MM:SS') END_DATE," +
					"LABOUR_PRICE,PART_PRICE,PRINT_BY,BALANCE_YIELDLY," +
					"TO_CHAR(LAST_PRINT_DATE,'YYYY-MM-DD HH:MM') LAST_PRINT_DATE,PRINT_TIMES FROM TT_AS_DEALER_COVER_DETAIL WHERE 1=1");
			List<Object> params = new ArrayList<Object>();
			if (!XHBUtil.IsNull(id)) {
				sql.append(" AND ID = ?");
				params.add(id);
			}
			return pageQueryMap(sql.toString(), params, this.getFunName());
		}
		/**
		 * 
		 * @param request 
		 * @param logonUser
		 * @param pageSize
		 * @param curPage
		 * @return
		 */
		public PageResult<Map<String,Object>> queryPartCode4(RequestWrapper request, AclUserBean user, Integer pageSize, Integer curPage) {
			StringBuffer sqlStr = new StringBuffer();
			String PART_CODE = request.getParamValue("REPART_OLDCODE");
			String PART_NAME = request.getParamValue("REPART_NAME");
			String activity_type = request.getParamValue("activity_type");
			sqlStr.append("select t.* from TT_PART_SPECIAL_DEFINE t where t.isneed_flag=10041001 and t.part_type=95621001 and t.start_date<=sysdate and t.end_date>=sysdate  and t.status=1 and t.state=10011001 ");
		    //append("select t.*,t.part_code as partCode,t.part_name  as partName from TM_PT_PART_BASE t where t.IS_PART_NEW = 0  ");
			if(Utility.testString(activity_type)){
				sqlStr.append(" and t.activity_type="+activity_type);
			}
		    if(Utility.testString(PART_CODE)){
		    	sqlStr.append(" and t.part_code like '%"+PART_CODE+"%'");
			}
		    if(Utility.testString(PART_NAME)){
		    	sqlStr.append(" and t.part_name like '%"+PART_NAME+"%'");
		    }
		    return pageQuery(sqlStr.toString(), null,getFunName(), pageSize, curPage);
		  }
		/**
		 * 查询工时
		 * @param request 
		 * @param logonUser
		 * @param pageSize
		 * @param curPage
		 * @return
		 */
		public PageResult<Map<String, Object>> queryTime1(RequestWrapper request, AclUserBean logonUser, Integer pageSize, Integer curPage) {
			PageResult<Map<String, Object>>  list= null;
			StringBuffer sb= new StringBuffer();
			String LABOUR_CODE = request.getParamValue("LABOUR_CODE");
			String CN_DES = request.getParamValue("CN_DES");
			//sb.append("select m.*,m.labour_quantity*m.labour_hours as labour_fix from tt_as_wr_labouritem m  where m.labour_code is not  null ");
			sb.append("select g.wrgroup_code,\n" );
			sb.append("              taww.id,\n" );
			sb.append("              taww.wrgroup_id,\n" );
			sb.append("              taww.labour_code,\n" );
			sb.append("              taww.cn_des as WR_LABOURNAME,\n" );
			sb.append("              trim(to_char(taww.labour_quotiety, '999999.00')) as labour_quotiety,\n" );
			sb.append("              trim(to_char(taww.labour_hour, '999990.00')) as labour_hour,\n" );
			sb.append("              a.labour_code labour_code_big,\n" );
			sb.append("              a.cn_des cn_des_big\n" );
			sb.append("         from TT_AS_WR_WRLABINFO taww\n" );
			sb.append("         left join TT_AS_WR_WRLABINFO a\n" );
			sb.append("           on taww.pater_id = a.id, tt_as_wr_model_group g\n" );
			sb.append("        where taww.wrgroup_id = g.wrgroup_id\n" );
			sb.append("          and taww.is_del = 0\n" );
			sb.append("          and taww.tree_code = '3'\n" );
			if(Utility.testString(LABOUR_CODE)){
				sb.append(" and taww.labour_code like '%"+LABOUR_CODE+"%' \n");
			}
			if(Utility.testString(CN_DES)){
				sb.append(" and taww.cn_des like '%"+CN_DES+"%' \n");
			}
			sb.append("        order by g.wrgroup_code desc, taww.labour_code");

			
			list= pageQuery(sb.toString(), null,getFunName(), pageSize, curPage);
			 return list;
		}
		
		public PageResult<Map<String,Object>> queryTime2(RequestWrapper request,AclUserBean user,
				int pageSize, int curPage) {
			StringBuffer sqlStr = new StringBuffer();
			String LABOUR_CODE = request.getParamValue("LABOUR_CODE");
			String CN_DES = request.getParamValue("CN_DES");
			sqlStr.append(" select wrgroup_name,labour_fix,fore,is_spec,parameter_value,approval_level,labour_fix*parameter_value as labour_Amount,labour_code,cn_des from(");
			sqlStr.append(" select g.WRGROUP_NAME AS wrgroup_name,t.labour_quotiety*t.labour_hour as labour_fix,t.cn_des,t.labour_code, " );
			sqlStr.append(" case WHEN b.ID IS NOT NULL  THEN\n");
			sqlStr.append("  10041001 else 10041002 end fore,\n");
			sqlStr.append("(case when instr(t.labour_code,'"+Constant.SPEC_LABOUR_CODE+"')=1 then 1 else 0 end ) as is_spec,");
			sqlStr.append("nvl((select nvl(max(labour_price),0) from tt_as_wr_labour_price where mode_type =G.WRGROUP_CODE ),0) AS parameter_value,b.approval_level from TT_AS_WR_WRLABINFO t ");
			sqlStr.append(" LEFT OUTER JOIN TT_AS_WR_MODEL_GROUP G ON T.WRGROUP_ID=G.WRGROUP_ID  ");
			sqlStr.append(" left join tt_as_wr_authmonitorlab b on b.labour_operation_no = t.labour_code and b.model_group=t.wrgroup_id\n");
			sqlStr.append(" where 1=1  ");
			sqlStr.append(" and t.IS_DEL=0 ");
			sqlStr.append(" and g.wrgroup_type=10451001 ");
			if(Utility.testString(LABOUR_CODE)){
				sqlStr.append(" and t.labour_code like '%"+LABOUR_CODE+"%' \n");
			}
			if(Utility.testString(CN_DES)){
				sqlStr.append(" and t.cn_des like '%"+CN_DES+"%' \n");
			}
			sqlStr.append("        order by g.wrgroup_code desc, t.labour_code\n");
			sqlStr.append(" )");
/*			PageResult<Map<String,Object>> ps = pageQuery(
					TtAsWrWrlabinfoExtPO.class, sqlStr.toString(), null, pageSize,
					curPage);*/
			PageResult<Map<String,Object>> ps= pageQuery(sqlStr.toString(), null,getFunName(), pageSize, curPage);
			return ps;
		}
		
	    public List<Map<String, Object>> getWorkhoursCode() {
	        StringBuffer sql = new StringBuffer();
	        sql.append(" SELECT DISTINCT * FROM tt_accessory_price_maintain ");
	        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
	        return list;
	    }
	    public PageResult<Map<String,Object>> queryworkHour(String workCode,String workName,int pageSize, int curPage){
	        StringBuffer sql= new StringBuffer();
	        sql.append("SELECT DISTINCT A.WORKHOUR_CODE,A.WORKHOUR_NAME,A.PRICE\n");
	        sql.append(" FROM TT_ACCESSORY_PRICE_MAINTAIN A\n");
	        sql.append(" WHERE 1=1"); 
	        if(Utility.testString(workCode)){
	        	  sql.append("AND a.WORKHOUR_CODE LIKE '%"+workCode.toUpperCase()+"%'\n");
	        }
	        if(Utility.testString(workName)){
	        	sql.append("AND A.WORKHOUR_NAME LIKE '%"+workName+"%'"); 
	        }
	      
	    	return  pageQuery(sql.toString(), null, this.getFunName(),pageSize , curPage);
	    }
		//查询质损
		public List<Map<String, Object>> getAccessoryDtl(String roNo){
			StringBuffer sql = new StringBuffer("\n");
			sql.append(" select * from tt_accessory_dtl where RO_NO='");
			sql.append(roNo).append("'");
			List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
	        return list;
		}
		
		public List<Map<String, Object>> getAccessoryDtl01(String roNo){
			StringBuffer sql = new StringBuffer("\n");
			sql.append(" select * from tt_claim_accessory_dtl where CLAIM_NO='");
			sql.append(roNo).append("'");
			List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
	        return list;
		}
		
		//查询质损
		public List<Map<String, Object>> getclaimAccessoryDtl(String claim){
			StringBuffer sql = new StringBuffer("\n");
			sql.append(" select * from tt_claim_accessory_dtl where claim_NO='");
			sql.append(claim).append("'");
			List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
	        return list;
		}
		/**
		 * 计算出这台车的活动已经做的活动次数
		 * @param VIN
		 * @return
		 */
		public Map<String, Object> checkActivetyOne(String VIN) {
			StringBuffer sql = new StringBuffer();
			sql.append(" select nvl(count(*),0) as countVin from Tt_As_Repair_Order t where t.VIN='"+VIN+"' and t.repair_type_code=11441005");
			Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
			return ps;
		}
		/**
		 * 查询供应商数据
		 * @param request
		 * @param loginUser 
		 * @param pageSize
		 * @param currPage
		 * @return
		 */
		public PageResult<Map<String, Object>> querySupplierCode(RequestWrapper request, AclUserBean loginUser, Integer pageSize, Integer currPage) {
			String maker_code = DaoFactory.getParam(request, "maker_code");
			String maker_shotname = DaoFactory.getParam(request, "maker_shotname");
			//dealerId为空时，是整车厂；不为空时，是经销商
			String dealerId= CommonUtils.checkNull(loginUser.getDealerId());
			String not_show = "902306,902309,902311";
			String partcode = request.getParamValue("partcode");
			StringBuffer sb= new StringBuffer();
			sb.append("select c.maker_id       as maker_id,\n" );
			sb.append("       c.maker_code     maker_code,\n" );
			sb.append("       c.maker_shotname maker_shotname\n" );
			sb.append("  from tt_part_maker_define c\n" );
			sb.append(" where 1 = 1\n" );
			sb.append("   and (c.maker_id in\n" );
			sb.append("       (select t.maker_id\n" );
			sb.append("           from tt_part_maker_relation t\n" );
			sb.append("          where t.part_id in\n" );
			sb.append("                (select pb.part_id\n" );
			sb.append("                   from tm_pt_part_base pb\n" );
			sb.append("                  where pb.part_code in ('"+partcode+"',\n" );
			sb.append("                                         '"+partcode.substring(0, partcode.length()-3)+"000',\n" );
			sb.append("                                         '"+partcode.substring(0, partcode.length()-3)+"B0Y',\n" );
			sb.append("                                         '"+partcode.substring(0, partcode.length()-3)+"B00'))) or\n" );
			sb.append("       c.MAKER_ID IN  (select c.maker_id\n" );
			sb.append("                        from tt_part_maker_define c\n" );
			sb.append("                      where c.maker_code='902307' ))");
			DaoFactory.getsql(sb, "c.maker_code", maker_code, 2);
			DaoFactory.getsql(sb, "c.maker_shotname", maker_shotname, 2);
			//dealerId为空时，是整车厂;不为空时,是经销商
			if (!"".equals(dealerId)) {
				DaoFactory.getsql(sb, "c.maker_code", not_show, 8);
			}
			
			sb.append(" order by c.maker_code ");
			PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
			return list;
		}
		/**
		 * 查询补偿费的明细
		 * @param roNo
		 * @return
		 */
		public List<Map<String, Object>> getCompensationMoney(String roNo) {
			StringBuffer sql = new StringBuffer("\n");
			sql.append(" SELECT T.* FROM TT_AS_WR_COMPENSATION_MONEY T WHERE T.RO_NO='"+roNo+"'");
			List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
	        return list;
		}

		public List<Map<String, Object>> getCompensationMoneyAPP(String claimNo) {
			StringBuffer sql = new StringBuffer("\n");
			sql.append(" SELECT T.* FROM TT_AS_WR_COMPENSATION_APP T WHERE T.CLAIM_NO='"+claimNo+"'");
			List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
	        return list;
		}
		public List<Map<String, Object>> getCompensationMoneyAPPbyRONO(String ro_no) {
			StringBuffer sql = new StringBuffer("\n");
			sql.append(" SELECT T.* FROM TT_AS_WR_COMPENSATION_APP T WHERE T.RO_NO='"+ro_no+"'");
			List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
			return list;
		}
		public List<Map<String, Object>> isReport(String vin,String mileage,String partNo,String roNo){
			StringBuffer sql = new StringBuffer("\n");
			sql.append("SELECT o.* FROM Tt_As_Repair_Order o,tt_as_ro_repair_part p\n");
			sql.append("WHERE o.ID = p.ro_id\n");
			sql.append("AND o.vin='"+vin+"'\n");
			sql.append("AND o.In_Mileage="+mileage+"\n");
			if(!"'".equals(partNo) && !"".equals(partNo)){
				sql.append("AND p.part_no IN ("+partNo+")\n"); 
			}
			if(Utility.testString(roNo)){
				sql.append("AND o.ro_no not in ('"+roNo+"')"); 
			}
		  return this.pageQuery(sql.toString(), null, this.getFunName());
		}
		/**
		 * 检测是否只是一个人在操作审核该单子 返回-1 为有人在操作，1为没有操作
		 */
		public int findStatusById(RequestWrapper request) {
			int res=-1;
			int resType=0;
			int resTypeTemp=0;
			String id = DaoFactory.getParam(request, "ID");
			String type = DaoFactory.getParam(request, "type");
			if("1".equals(type)){
				resTypeTemp=10791004;//通过
			}
			if("2".equals(type)){
				resTypeTemp=10791006;//拒绝
			}
			if("3".equals(type)){
				resTypeTemp=10791005;//退回
			}
			StringBuffer sb= new StringBuffer();
			sb.append("select a.status from tt_as_wr_application a where 1=1 and a.id="+id);
			List<Map<String, Object>> pageQuery = this.pageQuery(sb.toString(), null, this.getFunName());
			if(pageQuery!=null && pageQuery.size()>0){
				Map<String, Object> map = pageQuery.get(0);
				BigDecimal b= (BigDecimal) map.get("STATUS");
				resType=b.intValue();
			}
			if(resType!=resTypeTemp){
				res=1;
			}else{
				res=-1;
			}
			return res;
		}
		/**
		 * 选择工单后，根据工单ID 得到索赔单新增页面的基础数据
		 * 
		 */
		public TtAsRepairOrderExtPO getDeailBeanById(String id) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT 0 AS IN_CLAIM,g.free,v.mileage,\n");
			sql.append("  c.ctm_name,c.main_phone ctm_phone,c.Address ctm_address,  dd.claim_director_telphone,\n"); 
			sql.append("  V.CLAIM_TACTICS_ID AS CLAIM_TACTICS_ID,\n");
			sql.append("  V1.MODEL_ID AS MODEL_ID,\n");
			sql.append("  R.*,\n");
			sql.append("  AC.ACTIVITY_NAME AS CAMPAIGN_NAME,\n");
			sql.append("  NVL(AC.IS_FIXFEE, 0) AS CAM_FIX,\n");
			sql.append("  NVL(V.LICENSE_NO, '') AS LICENSE_NO,\n");
			sql.append("  V1.BRAND_CODE AS BRAND_CODE,\n");
			sql.append("  V1.SERIES_CODE AS SERIES_CODE,\n");
			sql.append("  V1.MODEL_CODE AS MODEL_CODE,\n");
			sql.append("  V1.BRAND_NAME AS BRAND_NAME,\n");
			sql.append("  V1.SERIES_NAME AS SERIES_NAME,\n");
			sql.append("  NVL(V1.MODEL_NAME, '') AS MODEL_NAME,\n");
			sql.append("  V.ENGINE_NO AS ENGINE_NO_V,\n");
			sql.append("  NVL(V.REARAXLE_NO, '') AS REARAXLE_NO,\n");
			sql.append("  V.YIELDLY AS YIELDLY,\n");
			sql.append("  NVL(V.GEARBOX_NO, '') AS GEARBOX_NO,\n");
			sql.append("  NVL(V.TRANSFER_NO, '') AS TRANSFER_NO,\n");
			sql.append("  V.PURCHASED_DATE AS PURCHASED_DATE,\n");
			sql.append("  V.PRODUCT_DATE,\n");
			sql.append("  O.START_TIME AS START_TIME,\n");
			sql.append("  O.END_TIME AS END_TIME,\n");
			sql.append("  O.OUT_PERSON AS OUT_PERSON,\n");
			sql.append("  O.OUT_SITE AS OUT_SITE,\n");
			sql.append("  O.OUT_LICENSENO AS OUT_LICENSENO,\n");
			sql.append("  O.FROM_ADRESS AS FROM_ADRESS,\n");
			sql.append("  O.END_ADRESS AS END_ADRESS,\n");
			sql.append("  O.OUT_MILEAGE AS OUT_MILEAGES,\n");
			sql.append("  TO_CHAR(R.FOR_BALANCE_TIME, 'yyyy-mm-dd HH24:mi') END_DATE,\n");
			sql.append("  TO_CHAR(R.RO_CREATE_DATE, 'yyyy-mm-dd HH24:mi') START_DATE,\n");
			sql.append("  AC.ACTIVITY_FEE AS CAMPAIGN_FEE,\n");
			sql.append("  BA.AREA_NAME YIELDLY_NAME\n");
			sql.append(" FROM TT_AS_REPAIR_ORDER R\n");
			sql.append(" LEFT OUTER JOIN TM_VEHICLE V ON R.VIN = V.VIN\n");
			sql.append(" LEFT OUTER JOIN VW_MATERIAL_GROUP_SERVICE V1 ON V.PACKAGE_ID =\n");
			sql.append("         V1.PACKAGE_ID\n");
			sql.append(" LEFT OUTER JOIN TT_AS_WR_OUTREPAIR O ON R.RO_NO = O.RO_NO\n");
			sql.append(" LEFT OUTER JOIN TM_DEALER D ON R.DEALER_ID = D.DEALER_ID\n");
			sql.append(" LEFT OUTER JOIN TT_AS_ACTIVITY AC ON AC.ACTIVITY_CODE = R.CAM_CODE\n");
			sql.append("        AND AC.IS_DEL = 0\n");
			sql.append(" LEFT JOIN TM_BUSINESS_AREA BA ON BA.AREA_ID = V.YIELDLY\n");
			sql.append(" LEFT JOIN tt_AS_wr_MODEL_ITEM t ON t.model_id=v.package_id\n");
			sql.append(" LEFT JOIN  tt_as_wr_model_group g ON g.wrgroup_id=t.wrgroup_id AND g.wrgroup_id<>-1\n"); 
			sql.append(" LEFT JOIN tt_dealer_actual_sales s ON s.vehicle_id = v.vehicle_id AND s.is_return=10041002\n");
			sql.append(" LEFT JOIN tt_customer c ON c.ctm_id = s.ctm_id\n");
			sql.append(" LEFT JOIN tm_dealer_detail dd ON dd.fk_dealer_id = nvl(r.second_dealer_id,r.dealer_id)"); 

			sql.append(" WHERE 1 = 1\n");
			sql.append(" AND R.ID = "+id+""); 

			TtAsRepairOrderExtPO tawep = new TtAsRepairOrderExtPO();
			List<TtAsRepairOrderExtPO> ls = select(TtAsRepairOrderExtPO.class,
					sql.toString(), null);
			if (ls != null) {
				if (ls.size() > 0) {
					tawep = ls.get(0);
				}
			}
			return tawep;
		}
		/**
		 * 得到工单索赔工时集合列表
		 */
		public List<Map<String, Object>> queryItemls(String roId) {

			StringBuffer sql= new StringBuffer();
			sql.append("SELECT L.ID,\n" );
			sql.append("       L.WR_LABOURCODE,\n" );
			sql.append("       L.WR_LABOURNAME,\n" );
			sql.append("       L.STD_LABOUR_HOUR LABOUR_HOUR,\n" );
			sql.append("       L.LABOUR_PRICE,\n" );
			sql.append("       L.LABOUR_AMOUNT,\n" );
			sql.append("       L.PAY_TYPE,\n" );
			sql.append("       P.PART_NO,\n" );
			sql.append("       P.MAIN_PART_CODE,\n" );
			sql.append("       M.MAL_ID,\n" );
			sql.append("       M.MAL_CODE || '--' || M.MAL_NAME MAL_CODE\n" );
			sql.append("  FROM TT_AS_RO_LABOUR L, TT_AS_RO_REPAIR_PART P,TT_AS_WR_MALFUNCTION M\n" );
			sql.append("   where  M.MAL_ID = L.MAL_FUNCTION and P.LABOUR_CODE = L.WR_LABOURCODE\n" );
			sql.append("    and l.ro_id=p.ro_id \n");
			//排除次因件与主因件同一个工时的情况
			sql.append("AND NOT  EXISTS (\n");
			sql.append(" SELECT 1 FROM TT_AS_RO_REPAIR_PART t\n");
			sql.append(" WHERE t.ro_id="+roId+" AND t.main_part_code=p.part_no AND t.labour_code=p.labour_code\n");
			sql.append(" )"); 

			sql.append(" and  L.RO_ID="+roId+"\n");
			sql.append("AND L.PAY_TYPE="+Constant.PAY_TYPE_02+""); 

			List<Map<String, Object>> resList = pageQuery(sql.toString(), null,
					getFunName());

			return resList;
		}
		/**
		 * 获取工单索赔的配件列表
		 */
		public List<Map<String, Object>> queryPartLs(String roId) {

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT P.ID,\n");
			sql.append("  P.PART_NO,\n");
			sql.append("  P.PART_NAME,\n");
			sql.append("  P.PART_QUANTITY,\n");
			sql.append("  P.PART_COST_PRICE,\n");
			sql.append("  P.PART_COST_AMOUNT,\n");
			sql.append("  P.LABOUR_CODE,\n");
			sql.append("  P.RESPONS_NATURE RESPONS_TYPE,\n");
			sql.append("  P.PART_USE_TYPE,\n");
			sql.append("  P.MAIN_PART_CODE,\n");
			sql.append("  P.TROUBLE_DESCRIBE,\n");
			sql.append("  P.TROUBLE_REASON,\n");
			sql.append("  P.DEAL_METHOD,decode(p.ZF_RONO,'','无',p.ZF_RONO) ZF_RONO\n");
			sql.append(" FROM TT_AS_RO_REPAIR_PART P\n");
			sql.append(" WHERE 1 = 1\n");
			sql.append("  AND P.PAY_TYPE = "+Constant.PAY_TYPE_02+"\n");
			sql.append("  AND P.RO_ID = "+roId+"\n");
			sql.append(" and (P.PART_USE_TYPE = 95431002 or P.PART_USE_TYPE = 95431001) "); 


			List<Map<String, Object>> resList = pageQuery(sql.toString(), null,
					getFunName());

			return resList;
		}
		/**
		 * 得到其他项目的集合
		 * @param roId
		 * @return
		 */
		public List<Map<String, Object>> queryOtherLs(String roId) {

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.ID,\n");
			sql.append("  A.ADD_ITEM_CODE,\n");
			sql.append("  A.ADD_ITEM_NAME,\n");
			sql.append("  A.ADD_ITEM_AMOUNT,\n");
			sql.append("  A.REMARK,\n");
			sql.append("  A.PAY_TYPE,\n");
			sql.append("  A.MAIN_PART_CODE\n");
			sql.append(" FROM TT_AS_RO_ADD_ITEM A\n");
			sql.append(" WHERE A.RO_ID = "+roId+"\n");
			sql.append("  AND A.PAY_TYPE = "+Constant.PAY_TYPE_02+""); 

			List<Map<String, Object>> resList = pageQuery(sql.toString(), null,
					getFunName());

			return resList;
		}
		/**
		 * 查询工单废弃(车厂) 
		 * @param request
		 * @param dealerid 
		 * @param pageSize
		 * @param currPage
		 * @return
		 */
		public PageResult<Map<String, Object>> repairOrderCancel(RequestWrapper request, Integer pageSize, Integer currPage) {
			String roNo = DaoFactory.getParam(request,"RO_NO");
			String vin = DaoFactory.getParam(request,"VIN");
			String repairType = DaoFactory.getParam(request,"REPAIR_TYPE");
			String roCreateDate = DaoFactory.getParam(request,"RO_CREATE_DATE");
			String deliveryDate = DaoFactory.getParam(request,"DELIVERY_DATE");
			String isForl = DaoFactory.getParam(request,"RO_FORE");
			String roStatus = DaoFactory.getParam(request,"RO_STATUS");
			String createDateStr = DaoFactory.getParam(request,"CREATE_DATE_STR");
			String createDateEnd = DaoFactory.getParam(request,"CREATE_DATE_END");
			String isChanghe = DaoFactory.getParam(request,"YIELDLY_TYPE");
			StringBuffer sb= new StringBuffer();
			sb.append("select decode(a.ro_no,null,0,1)  as in_claim,r.*\n" );
	        sb.append(" from tt_as_repair_order r\n" );
			sb.append("left  join tm_dealer d on r.dealer_code=d.dealer_code \n");
			sb.append("  left join Tt_As_Wr_Application a on a.ro_no = r.ro_no ");
			sb.append(" where 1=1 \n");
			DaoFactory.getsql(sb, "r.RO_NO", roNo, 2);
			DaoFactory.getsql(sb, "r.REPAIR_TYPE_CODE", repairType, 1);
			DaoFactory.getsql(sb, "r.RO_CREATE_DATE", roCreateDate, 3);
			DaoFactory.getsql(sb, "r.RO_CREATE_DATE", deliveryDate, 4);
			DaoFactory.getsql(sb, "r.FORL_STATUS", isForl, 1);
			DaoFactory.getsql(sb, "r.RO_STATUS", roStatus, 1);
			DaoFactory.getsql(sb, "r.balance_yieldly", isChanghe, 1);
			DaoFactory.getsql(sb, "r.vin", vin, 2);
			DaoFactory.getsql(sb, "r.CREATE_DATE", createDateStr, 3);
			DaoFactory.getsql(sb, "r.CREATE_DATE", createDateEnd, 4);
			sb.append(" and  r.is_cancel is not null  \n"); 
			PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
			return list;
		}
		/**
		 * 库存验证
		 * @param partid
		 * @param dealerid
		 * @return
		 */
		public boolean checkStoreBypartCodeAndDealerid(String part_code,Long dealerid) {
			boolean bool=true;
			StringBuffer sb= new StringBuffer();
			sb.append("select c.* from vw_part_dlr_stock_check c where c.DEALER_ID="+dealerid+" and c.PART_CODE='"+part_code+"'");
			List<Map<String,Object>> list=pageQuery(sb.toString(), null,getFunName());
			if(list!=null&&list.size()>0){
				Map<String, Object> map = list.get(0);
				BigDecimal b=(BigDecimal) map.get("KY_QTY");
				if(b.intValue()>=0){
					bool=true;
				}else{
					bool=false;
				}
			}else{
				bool=false;
			}
			return bool;
		}
		/**
		 * 服务站申请废弃
		 * @param request
		 * @param dealerId 
		 * @param pageSize
		 * @param currPage
		 * @return
		 */
		public PageResult<Map<String, Object>> roCancelApply(RequestWrapper request, Long dealerId, Integer pageSize, Integer currPage) {
			String roNo = DaoFactory.getParam(request,"RO_NO");
			String vin = DaoFactory.getParam(request,"VIN");
			String repairType = DaoFactory.getParam(request,"REPAIR_TYPE");
			String roCreateDate = DaoFactory.getParam(request,"RO_CREATE_DATE");
			String deliveryDate = DaoFactory.getParam(request,"DELIVERY_DATE");
			String isForl = DaoFactory.getParam(request,"RO_FORE");
			String roStatus = DaoFactory.getParam(request,"RO_STATUS");
			String createDateStr = DaoFactory.getParam(request,"CREATE_DATE_STR");
			String createDateEnd = DaoFactory.getParam(request,"CREATE_DATE_END");
			String isChanghe = DaoFactory.getParam(request,"YIELDLY_TYPE");
			StringBuffer sb= new StringBuffer();
			sb.append("select decode(a.ro_no,null,0,1)  as in_claim,r.*\n" );
	        sb.append(" from tt_as_repair_order r\n" );
			sb.append("left  join tm_dealer d on r.dealer_code=d.dealer_code \n");
			sb.append("  left join Tt_As_Wr_Application a on a.ro_no = r.ro_no left join Tt_As_Wr_Foreapproval w on w.ro_no=r.ro_no  ");
			sb.append(" where 1=1 \n");
			DaoFactory.getsql(sb, "r.RO_NO", roNo, 2);
			DaoFactory.getsql(sb, "r.REPAIR_TYPE_CODE", repairType, 1);
			DaoFactory.getsql(sb, "r.RO_CREATE_DATE", roCreateDate, 3);
			DaoFactory.getsql(sb, "r.RO_CREATE_DATE", deliveryDate, 4);
			DaoFactory.getsql(sb, "r.FORL_STATUS", isForl, 1);
			DaoFactory.getsql(sb, "r.RO_STATUS", roStatus, 1);
			DaoFactory.getsql(sb, "r.balance_yieldly", isChanghe, 1);
			DaoFactory.getsql(sb, "r.vin", vin, 2);
			DaoFactory.getsql(sb, "r.CREATE_DATE", createDateStr, 3);
			DaoFactory.getsql(sb, "r.CREATE_DATE", createDateEnd, 4);
			if(Utility.testString(dealerId.toString())) {
				sb.append(" and (d.DEALER_ID in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+dealerId+"   connect by PRIOR d.dealer_id = d.parent_dealer_d)or r.second_dealer_id = "+dealerId+" ) \n");
			}
			sb.append(" and (r.ORDER_VALUABLE_TYPE='13591002'   or  r.ORDER_VALUABLE_TYPE = '13591001' and a.status is null and w.report_status not in (11561001) )\n");
			sb.append(" and r.is_customer_in_asc=1 \n"); 
			sb.append(" AND (r.INSURATION_CODE=0 or r.INSURATION_CODE is null) \n");
			sb.append(" AND r.remark1 is null and r.is_cancel is null\n"); 
			sb.append(" order by r.CREATE_DATE DESC \n");
			PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
			return list;
		}
		/**
		 * 
		 * @param request
		 * @param currDealerId
		 * @param pageSize
		 * @param currPage
		 * @return
		 */
		public PageResult<Map<String, Object>> claimCancelApply(RequestWrapper request, Long currDealerId, Integer pageSize,Integer currPage) {
			
			return null;
		}
		
		/**
		 * 索赔蛋申请明细(包含字表)
		 * @param request
		 * @param loginUser
		 * @param pageSize
		 * @param currPage
		 * @return
		 */
		 public PageResult<Map<String, Object>> queryApplicationDeatail(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
			   	String dealerCode = DaoFactory.getParam(request,"DEALER_CODE");
				String dealerName = DaoFactory.getParam(request,"DEALER_NAME");
				String roNo = DaoFactory.getParam(request,"RO_NO");
				String claimNo = DaoFactory.getParam(request,"CLAIM_NO");
				String claimType = DaoFactory.getParam(request,"CLAIM_TYPE");
				String vin = DaoFactory.getParam(request,"VIN");
				String roStartdate = DaoFactory.getParam(request,"RO_STARTDATE");
				String roEnddate = DaoFactory.getParam(request,"RO_ENDDATE");
				//String approveDate = DaoFactory.getParam(request,"approve_date");// 审核时间
				//String approveDate2 = DaoFactory.getParam(request,"approve_date2");
				String status = DaoFactory.getParam(request,"STATUS");
				//String person = DaoFactory.getParam(request,"PERSON");
				String yieldly = DaoFactory.getParam(request,"YIELDLY");// 查询条件--产地
				String isImport = DaoFactory.getParam(request,"is_import");
				String foreAuthPerson = DaoFactory.getParam(request,"foreAuthPerson");
				String model = DaoFactory.getParam(request,"model");
				String partCode=DaoFactory.getParam(request,"PART_CODE");
				String partName=DaoFactory.getParam(request,"PART_NAME");
				String supplyCode=DaoFactory.getParam(request,"SUPP_CODE");
				String supplyName=DaoFactory.getParam(request,"SUPP_NAME");
				String big = DaoFactory.getParam(request,"ORG_ID");
				String isfan=DaoFactory.getParam(request,"IS_FAN");
				
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT S.ROOT_ORG_NAME BIG_NAME,P.REMARK,P.TROUBLE_REASON,P.DEAL_METHOD,\n");
				sql.append("   S.ORG_NAME SMALL_NAME,\n");
				sql.append("   D.DEALER_CODE,\n");
				sql.append("   D.DEALER_SHORTNAME DEALER_NAME,\n");
				sql.append("   A.VIN,a.SUB_DATE,\n");
				sql.append("   A.IS_IMPORT,\n");
				sql.append("   A.CLAIM_NO,\n");
				sql.append("   A.RO_NO,\n");
				sql.append("   A.REPORTER,\n");
				sql.append("   A.REPORT_DATE,\n");
				sql.append("   A.CLAIM_TYPE,\n");
				sql.append("   A.SUBMIT_TIMES,\n");
				sql.append("   A.IN_MILEAGE,\n");
				sql.append("   A.STATUS,\n");
				sql.append("   P.DOWN_PART_CODE,\n");
				sql.append("   P.DOWN_PART_NAME,\n");
				sql.append("   P.DOWN_PRODUCT_CODE MAKER_CODE,\n");
				sql.append("   P.DOWN_PRODUCT_NAME MAKE_NAME,\n");
				sql.append("   A.MODEL_NAME m_name,\n");
				sql.append("   A.GROSS_CREDIT,\n");
				sql.append("   P.AUDIT_STATUS,\n");
				sql.append("   TO_CHAR(P.AUDIT_DATE,'YYYY-MM-DD HH24:MI') PART_AUDIT_TIME,\n");
				sql.append("   U.NAME,\n");
				sql.append("   AP.STATUS BALANCE_FLAG, --结算标识 0 保存 1 上报  2 收票 3验票 4 转账\n");
				sql.append("   UU.NAME BANLANCE_USER, --结算人\n");
				sql.append("   TO_CHAR(CB.CREATE_DATE,'YYYY-MM-DD HH24:MI') BANLANCE_TIME, --结算时间\n");
				sql.append("   DECODE(AC.CLAIM_NO, NULL, '否', '是') BACK_FLAG, --反结算标识\n");
				sql.append("   UUU.NAME BACK_USER, --反结算人\n");
				sql.append("   TO_CHAR(AC.CREATE_DATE,'YYYY-MM-DD HH24:MI') BACK_TIME --反结算时间\n");
				sql.append(" FROM TT_AS_WR_APPLICATION A\n");
				sql.append(" LEFT JOIN TT_AS_WR_PARTSITEM P ON A.ID = P.ID\n");
				sql.append(" LEFT JOIN TT_AS_WR_LABOURITEM L ON A.ID = L.ID\n");
				sql.append("          AND P.WR_LABOURCODE = L.WR_LABOURCODE\n");
				sql.append("           AND P.ID = L.ID\n");
				sql.append("  LEFT JOIN TM_DEALER D ON A.DEALER_ID = D.DEALER_ID\n");
				sql.append(" LEFT JOIN VW_ORG_DEALER_SERVICE S ON D.DEALER_ID = S.DEALER_ID\n");
				sql.append(" LEFT JOIN TT_PART_MAKER_DEFINE MD ON P.DOWN_PRODUCT_CODE = MD.MAKER_CODE\n");
				sql.append(" LEFT JOIN TT_PART_MAKER_RELATION R ON MD.MAKER_ID = R.MAKER_ID AND R.PART_ID = P.REAL_PART_ID\n");
				sql.append(" LEFT JOIN TC_USER U ON P.AUDIT_BY = U.USER_ID\n");
				sql.append(" LEFT JOIN TT_AS_WR_CLAIM_BALANCE CB ON A.BALANCE_NO = CB.REMARK\n");
				sql.append(" LEFT JOIN TT_AS_PAYMENT AP ON AP.BALANCE_ODER = A.BALANCE_NO\n");
				sql.append(" LEFT JOIN TT_AS_WR_APPLICATION_COUNTER AC ON AC.ID = A.ID --AND AC.ID = L.ID AND AC.ID = P.ID\n");
				sql.append(" LEFT JOIN TT_AS_WR_PARTSITEM_COUNTER PC ON AC.ID = PC.ID\n");
				sql.append("              AND P.ID = PC.ID\n");
				sql.append("              AND P.PART_ID = PC.PART_ID\n");
				sql.append("  LEFT JOIN TT_AS_WR_LABOURITEM_COUNTER LC ON AC.ID = LC.ID\n");
				sql.append("              AND PC.ID = LC.ID\n");
				sql.append("              AND PC.WR_LABOURCODE =\n");
				sql.append("               LC.WR_LABOURCODE\n");
				sql.append("              AND L.LABOUR_ID = LC.LABOUR_ID\n");
				sql.append("  LEFT JOIN TC_USER UU ON CB.CREATE_BY = UU.USER_ID --结算人\n");
				sql.append("  LEFT JOIN TC_USER UUU ON AC.CREATE_BY = UUU.USER_ID --反索赔人\n");
				sql.append(" LEFT JOIN TM_VEHICLE V ON A.VIN = V.VIN\n"); 

				sql.append(" WHERE 1=1"); 

				if(Utility.testString(foreAuthPerson)){
					sql.append("and exists (\n");
					sql.append("      select 1 from tt_as_wr_appauthitem wa where wa.id = a.id and exists(\n");  
					sql.append("          select 1 from tc_user u where u.name like '%"+foreAuthPerson+"%' and u.user_id = wa.create_by\n");  
					sql.append("      )\n");  
					sql.append("  )\n");
				}
				sql.append("   AND A.STATUS not in (" + Constant.CLAIM_APPLY_ORD_TYPE_01+ ")  \n");
				 
				DaoFactory.getsql(sql, "A.YIELDLY", yieldly, 1);
				DaoFactory.getsql(sql, "d.dealer_name", dealerName, 2);
				DaoFactory.getsql(sql, "d.dealer_Code", dealerCode.toUpperCase(), 6);
				DaoFactory.getsql(sql, "A.CLAIM_TYPE", claimType, 1);
				DaoFactory.getsql(sql, "A.CREATE_DATE", roStartdate, 3);
				DaoFactory.getsql(sql, "A.CREATE_DATE", roEnddate, 4);
				DaoFactory.getsql(sql, "A.STATUS", status, 1);
				DaoFactory.getsql(sql, "A.claim_No", claimNo.toUpperCase(), 2);
				DaoFactory.getsql(sql, "A.RO_NO", roNo.toUpperCase(), 2);
				DaoFactory.getsql(sql, "a.model_code", model, 1);
				
				DaoFactory.getsql(sql, "P.DOWN_PART_CODE", partCode.toUpperCase(), 2);
				DaoFactory.getsql(sql, "P.DOWN_PART_NAME", partName, 2);
				DaoFactory.getsql(sql, "P.DOWN_PRODUCT_CODE", supplyCode.toUpperCase(), 2);
				DaoFactory.getsql(sql, "P.DOWN_PRODUCT_NAME", supplyName, 2);
				DaoFactory.getsql(sql, "S.ROOT_ORG_ID", big, 1);
				if(Utility.testString(isfan)){
					if("1".equalsIgnoreCase(isfan)){
						sql.append(" and ac.claim_no is not null ");
					}else{
						sql.append(" and ac.claim_no is  null ");
					}
				}
				
				// 车辆VIN码
				if (Utility.testString(vin)) {sql.append(GetVinUtil.getVins(vin.replaceAll("'","\''"), "A"));
				}
				DaoFactory.getsql(sql, "A.is_import", isImport, 1);
				
				sql.append("AND p.responsibility_type="+Constant.RESPONS_NATURE_STATUS_01+"\n"); 
				logger.info("-----------------------"+sql.toString());
				PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage); 
				return ps;
			}
		 /**
		  * 转换类型 工单和索赔单
		  * @param repairType
		  * @return
		  */
		public String changeRoCodeToClaimCode(String repairType) {
			String claimType="";
			if(repairType.equals(Constant.REPAIR_TYPE_01)){
				claimType=String.valueOf(Constant.CLA_TYPE_01);
			}
			if(repairType.equals(Constant.REPAIR_TYPE_02)){
				claimType=String.valueOf(Constant.CLA_TYPE_09);
			}
			if(repairType.equals(Constant.REPAIR_TYPE_03)){
				claimType=String.valueOf(Constant.CLA_TYPE_07);
			}
			if(repairType.equals(Constant.REPAIR_TYPE_04)){
				claimType=String.valueOf(Constant.CLA_TYPE_02);
			}
			if(repairType.equals(Constant.REPAIR_TYPE_05)){
				claimType=String.valueOf(Constant.CLA_TYPE_06);
			}
			if(repairType.equals(Constant.REPAIR_TYPE_06)){
				claimType=String.valueOf(Constant.CLA_TYPE_10);
			}
			if(repairType.equals(Constant.REPAIR_TYPE_07)){
				claimType=String.valueOf(Constant.CLA_TYPE_12);
			}
			if(repairType.equals(Constant.REPAIR_TYPE_08)){
				claimType=String.valueOf(Constant.CLA_TYPE_11);
			}
			if(repairType.equals(Constant.REPAIR_TYPE_09)){
				claimType=String.valueOf(Constant.CLA_TYPE_13);
			}
			return claimType;
		}

		public List<Map<String, Object>> queryItemlsTemp(String roId) {
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT L.ID,\n" );
			sql.append("       L.WR_LABOURCODE,\n" );
			sql.append("       L.WR_LABOURNAME,\n" );
			sql.append("       L.STD_LABOUR_HOUR LABOUR_HOUR,\n" );
			sql.append("       L.LABOUR_PRICE,\n" );
			sql.append("       L.LABOUR_AMOUNT,\n" );
			sql.append("       L.PAY_TYPE,\n" );
			sql.append("       M.MAL_ID,\n" );
			sql.append("       M.MAL_CODE || '--' || M.MAL_NAME MAL_CODE\n" );
			sql.append("  FROM TT_AS_RO_LABOUR L,TT_AS_WR_MALFUNCTION M\n" );
			sql.append("   where  M.MAL_ID = L.MAL_FUNCTION\n" );
			sql.append(" and  L.RO_ID="+roId+"\n");
			sql.append("AND L.PAY_TYPE="+Constant.PAY_TYPE_02+""); 
			List<Map<String, Object>> resList = pageQuery(sql.toString(), null,getFunName());
			return resList;
		}

		public PageResult<Map<String, Object>> timeParameter(
				RequestWrapper request, Integer page, Integer currPage,
				Map params) {
			StringBuffer sb= new StringBuffer();
			sb.append("select p.id,\n" );
			sb.append("       p.parameter_name,\n" );
			sb.append("       p.parameter_code,\n" );
			sb.append("       p.timeout,\n" );
			sb.append("       p.amount,\n" );
			sb.append("       p.create_by,\n" );
			sb.append("       p.create_date,\n" );
			sb.append("\t  ( select c.code_desc from tc_code c where c.code_id=p.parameter_type ) as  parameter_type,\n" );
			sb.append("\t   ( select c.code_desc from tc_code c where c.code_id=p.parameter_status ) as  parameter_status\n" );
			sb.append("  from tm_parameter p");
			sb.append(" where 1=1  ");
			if(params.get("PARAMETER_STATUS")!=null ){
				sb.append(" and p.parameter_status ="+params.get("PARAMETER_STATUS")+" ");
			}
			if(params.get("PARAMETER_TYPE")!=null ){
				sb.append("  and p.parameter_type ="+params.get("PARAMETER_TYPE")+"  ");
			}
			if(params.get("parameterName")!=null ){
				sb.append("  and p.parameter_name like '%"+params.get("parameterName")+"%'  ");
			}
			if(params.get("parameterCode")!=null ){
				sb.append("  and p.parameter_code like '%"+params.get("parameterCode")+"%'  ");
			}
			PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), page, currPage);
			return list;
		}

		public Double findAccByLabourid(String labourid) {
			Double accAmount=0d;
			StringBuffer sb= new StringBuffer();
			sb.append("select acc.app_price\n" );
			sb.append("  from tt_claim_accessory_dtl acc\n" );
			sb.append(" where acc.main_part_code in\n" );
			sb.append("       (select p.part_code\n" );
			sb.append("          from tt_as_wr_partsitem p, tt_as_wr_labouritem l\n" );
			sb.append("         where p.id = l.id\n" );
			sb.append("           and l.wr_labourcode = p.wr_labourcode\n" );
			sb.append("           and l.labour_id = '"+labourid+"')");
			List<Map<String,Object>> list = this.pageQuery(sb.toString(), null, getFunName());
			if(DaoFactory.checkListNull(list)){
				for (Map<String, Object> map : list) {
					BigDecimal b=(BigDecimal) map.get("APP_PRICE");
					accAmount+=b.doubleValue();
				}
			}
			return accAmount;
		}

		public Double findComByLabourid(String labourid) {
			Double comAmount=0d;
			StringBuffer sb= new StringBuffer();
			sb.append("select com.pass_price\n" );
			sb.append("  from tt_as_wr_compensation_app com\n" );
			sb.append(" where com.part_code in (select p.part_code\n" );
			sb.append("  from tt_as_wr_partsitem p, tt_as_wr_labouritem l\n" );
			sb.append("   where p.id = l.id\n" );
			sb.append("  and l.wr_labourcode = p.wr_labourcode\n" );
			sb.append("   and l.labour_id = '"+labourid+"')");
			List<Map<String,Object>> list = this.pageQuery(sb.toString(), null, getFunName());
			if(DaoFactory.checkListNull(list)){
				for (Map<String, Object> map : list) {
					BigDecimal b=(BigDecimal) map.get("PASS_PRICE");
					comAmount+=b.doubleValue();
				}
			}
			return comAmount;
		}


		public Map queryRangeSingle(AclUserBean loginUser,RequestWrapper request, String partCode) {

			Calendar calendar =Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -90);
			Date date = calendar.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = format.format(date);
//			String date1 = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
			StringBuffer sql= new StringBuffer();
			sql.append("select * from (\n" );
			sql.append("select count(1) totalNumber,\n" );
			sql.append("       s.supply_code,\n" );
			sql.append("       s.supply_name,\n" );
			sql.append("       s.part_code,\n" );
			sql.append("       s.part_name\n" );
			sql.append("  from TT_as_WR_range_single s\n" );
			sql.append(" where 1=1 \n" );
			DaoFactory.getsql(sql, "s.part_code", partCode, 2);
            sql.append(" and s.create_date<=sysdate \n");
            sql.append(" and s.create_date>=to_date('"+date1+"','yyyy-mm-dd hh24:mi:ss') \n");
			sql.append(" group by s.supply_code, s.supply_name, s.part_code, s.part_name\n" );
			sql.append(" order by totalNumber desc");
			sql.append(" ) where    rownum <= 1");
			return this.pageQueryMap(sql.toString(), null, getFunName());
		}

}