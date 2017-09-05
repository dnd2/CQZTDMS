package com.infodms.dms.dao.claim.application;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.sparta.xpath.TrueExpr;
import com.infodms.dms.bean.DealerBalanceBean;
import com.infodms.dms.bean.auditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TtAsPartBorrowPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrSpefeePO;
import com.infodms.dms.po.TtPartTransPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

@SuppressWarnings("unchecked")
public class DealerBalanceDao extends BaseDao {
	
	private static final DealerBalanceDao blanceDao = new DealerBalanceDao();
	
	public static final DealerBalanceDao getInstance(){
		//if(blanceDao==null)
		//	blanceDao = new DealerBalanceDao();
		return blanceDao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
     
	public boolean judeSpefee(String endBalanceDate, String CON_END_DAY,String DEALER_ID,String yieldly)
	{
			StringBuffer sql= new StringBuffer();
			sql.append("select t.DECLARE_SUM,t.STATUS from tt_as_wr_spefee t\n" );
			sql.append(" where t.DEALER_ID = "+DEALER_ID+"  and   t.MAKE_DATE >= to_date('"+endBalanceDate+"','yyyy-mm-dd')" +
					" and t.STATUS != t.O_STATUS  and  t.MAKE_DATE <= to_date('"+CON_END_DAY+"  23:59:59','yyyy-mm-dd HH24:mi:ss')");
			 List<TtAsWrSpefeePO> list= super.select(TtAsWrSpefeePO.class , sql.toString(), null);
			 if(list != null && list.size() >0)
			 {
				return false;
			 }
			 return true;
	}
	
	
	
	public boolean Application(String endBalanceDate, String CON_END_DAY,String DEALER_ID,String yieldly)
	{
		StringBuffer sql= new StringBuffer();
		//SQL中查询到 CON_END_DAY 为结束时间， endBalanceDate 为开始时间
		sql.append("  select * from tt_as_wr_application t where 1=1 \n" );
		sql.append("  and t.STATUS in (10791008) ");
		sql.append("  and t.CLAIM_TYPE != 10661006 and t.IS_IMPORT=10041002 ");
		DaoFactory.getsql(sql, "t.DEALER_ID", DEALER_ID, 1);
		DaoFactory.getsql(sql, "t.report_date", CON_END_DAY, 3);
		DaoFactory.getsql(sql, "t.report_date", CON_END_DAY, 4);
		DaoFactory.getsql(sql, "t.BALANCE_YIELDLY", yieldly, 1);
		sql.append("  UNION select * from tt_as_wr_application t where 1=1 \n" );
		sql.append("  and t.STATUS in (10791008) ");
		sql.append("  and t.CLAIM_TYPE = 10661006  and  t.IS_IMPORT=10041002");
		sql.append("  AND t.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		DaoFactory.getsql(sql, "t.DEALER_ID", DEALER_ID, 1);
		DaoFactory.getsql(sql, "t.report_date", CON_END_DAY, 3);
		DaoFactory.getsql(sql, "t.report_date", CON_END_DAY, 4);
		DaoFactory.getsql(sql, "t.BALANCE_YIELDLY", yieldly, 1);
		
		List<TtAsWrApplicationPO> list= this.pageQuery(sql.toString(), null,getFunName());//                 select(TtAsWrApplicationPO.class , sql.toString(), null);
		if(list != null && list.size() >0){
			 return false;
		}
		return true;
	}
	
	
	/**
	 * 经销商结算统计查询
	 * @param condition 查询条件
	 * @return PageResult<Map<String,Object>>
	 */
	public PageResult<Map<String,Object>> dealerBalanceStatisQuery(DealerBalanceBean condition){
		
		List<Object> paramList = new ArrayList<Object>();
		
		StringBuilder sql= new StringBuilder();

		sql.append("SELECT YIELDLY,STATUS,SUM(CLAIMCOUNT) CLAIMCOUNT,SUM(LABOUR_AMOUNT) LABOUR_AMOUNT,SUM(PART_AMOUNT) PART_AMOUNT,\n" );
		sql.append("SUM(NETITEM_AMOUNT) NETITEM_AMOUNT,SUM(FREE_M_PRICE) FREE_M_PRICE,SUM(CAMPAIGN_FEE) CAMPAIGN_FEE,\n" );
		sql.append("SUM(REPAIR_TOTAL) REPAIR_TOTAL,SUM(BALANCE_AMOUNT) BALANCE_AMOUNT\n" );
		//查询非服务活动可结算金额
		paramList.add(condition.getDealerId());
		paramList.add(condition.getCompanyId());
		sql.append("FROM (SELECT A.YIELDLY,A.STATUS,COUNT(*) CLAIMCOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) NETITEM_AMOUNT,NVL(SUM(A.FREE_M_PRICE),0) FREE_M_PRICE,NVL(SUM(A.CAMPAIGN_FEE),0) CAMPAIGN_FEE,\n" );
		sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE <> "+Constant.CLA_TYPE_06+"\n" );
		if(Utility.testString(condition.getYieldly())){
			paramList.add(condition.getYieldly());
			sql.append("AND A.YIELDLY = ?\n" );
		}
		if(Utility.testString(condition.getEndBalanceDate())){
			paramList.add(condition.getEndBalanceDate());
			sql.append("AND A.AUDITING_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(condition.getConEndDay())){
			paramList.add(condition.getConEndDay());
			sql.append("AND A.AUDITING_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append("GROUP BY A.YIELDLY,A.STATUS\n" );
		sql.append("UNION ALL\n" );
		//查询服务活动可结算的金额
		paramList.add(condition.getDealerId());
		paramList.add(condition.getCompanyId());
		sql.append("SELECT A.YIELDLY,A.STATUS,COUNT(*) CLAIMCOUNT,0 LABOUR_AMOUNT,0 PART_AMOUNT,\n" );
		sql.append("0 NETITEM_AMOUNT,0 FREE_M_PRICE,(SUM(NVL(A.CAMPAIGN_FEE,0)+NVL(A.BALANCE_LABOUR_AMOUNT,0)+"); 
		sql.append("NVL(A.BALANCE_PART_AMOUNT,0)+NVL(A.BALANCE_NETITEM_AMOUNT,0))+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) CAMPAIGN_FEE,\n" );
		sql.append("NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
		if(Utility.testString(condition.getYieldly())){
			paramList.add(condition.getYieldly());
			sql.append("AND A.YIELDLY = ?\n" );
		}
		if(Utility.testString(condition.getEndBalanceDate())){
			paramList.add(condition.getEndBalanceDate());
			sql.append("AND A.AUDITING_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(condition.getConEndDay())){
			paramList.add(condition.getConEndDay());
			sql.append("AND A.AUDITING_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append("GROUP BY A.YIELDLY,A.STATUS)\n" );
		sql.append("GROUP BY YIELDLY,STATUS");
		
		System.out.println("sqlsql==="+sql);
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName(), condition.getPageSize(), condition.getCurPage());
	}
	
	/**
	 * 查询开票经销商
	 * @param dealerId 经销商ID
	 * @return TmDealerPO
	 */
	public TmDealerPO queryInvoiceMaker(Long dealerId) throws Exception{
		
		TmDealerPO resultPO = null;
		/*******add by liuxh 20101115 根据经销商开票级别取得经销商信息******/
		TmDealerPO dealerCon=new TmDealerPO();
		dealerCon.setDealerId(dealerId);
		dealerCon=factory.select(dealerCon).get(0);
		String dealerLevel=dealerCon.getDealerLevel().toString();//经销商等级
		String invoiceLevel=CommonUtils.checkNull(dealerCon.getInvoiceLevel()); //经销商开票级别
		if(dealerLevel.equals(Constant.DEALER_LEVEL_01.toString())) //一级经销商
		{
			resultPO=dealerCon;//一级经销商开票单位为自己
		}
		else//非一级经销商
		{		
			if(invoiceLevel.equals(Constant.INVOICE_LEVEL_SELF)){//独立开票
				resultPO=dealerCon;
			}else if(invoiceLevel.equals(Constant.INVOICE_LEVEL_HIGH)){//上级开票
				StringBuilder sql= new StringBuilder();
				sql.append("SELECT A.* FROM TM_DEALER A\n" );
				sql.append("WHERE A.DEALER_TYPE =? \n");
				sql.append("START WITH A.DEALER_ID = ?\n" );
				sql.append("CONNECT BY A.DEALER_ID = PRIOR A.PARENT_DEALER_D"); //向上层遍历
				
				List<Object> paramList = new ArrayList<Object>();
				paramList.add(Constant.DEALER_TYPE_DWR);
				paramList.add(dealerId);
				List<TmDealerPO> resultList = this.select(TmDealerPO.class,sql.toString(),paramList);
				for(TmDealerPO dealer:resultList){
					String curInvoiceLevel=dealer.getInvoiceLevel();
					if(curInvoiceLevel.equals(Constant.INVOICE_LEVEL_SELF)){
						resultPO=dealer;
						break;
					}else{
						String curDealerLevel=dealer.getDealerLevel().toString();
						if(curDealerLevel.equals(Constant.DEALER_LEVEL_01.toString())){//如果一级经销商设置为上级开票 取一级经销商为开票单位
							resultPO=dealer;
							break;
						}
						continue;
					}
				}
				
			}else{
				throw new BizException("开票级别值设置错误!"+invoiceLevel);
			}
			/*******add by liuxh 20101115 根据经销商开票级别取得经销商信息******/
		}
		return resultPO;
	}
	/**
	 * 查询结算经销商
	 * @param dealerId 经销商ID
	 * @return TmDealerPO
	 */
	/******add by liuxh 20101115 增加查询结算单位******/
	public TmDealerPO queryBalanceMaker(Long dealerId) throws Exception{
		
		TmDealerPO resultPO = null;
		TmDealerPO dealerCon=new TmDealerPO();
		dealerCon.setDealerId(dealerId);
		dealerCon=factory.select(dealerCon).get(0);
		String invoiceLevel=CommonUtils.checkNull(dealerCon.getBalanceLevel()); //经销商结算
		if(invoiceLevel.equals(Constant.BALANCE_LEVEL_SELF)){//独立结算
			resultPO=dealerCon;
		}else if(invoiceLevel.equals(Constant.BALANCE_LEVEL_HIGH)){//上级结算
			StringBuilder sql= new StringBuilder();
			sql.append("SELECT A.* FROM TM_DEALER A\n" );
			sql.append("WHERE 1=1\n" );
			sql.append("AND A.DEALER_LEVEL = ?\n" );
			sql.append("START WITH A.DEALER_ID = ?\n" );
			sql.append("CONNECT BY A.DEALER_ID = PRIOR A.PARENT_DEALER_D");
			
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(Constant.DEALER_LEVEL_01);//查询一级经销商
			paramList.add(dealerId);

			List<TmDealerPO> resultList = this.select(TmDealerPO.class,sql.toString(),paramList);
			if(resultList!=null && resultList.size()>0)
				resultPO = resultList.get(0);
		}else{
			throw new BizException("结算级别值设置错误!"+invoiceLevel);
		}
		
		return resultPO;
	}
	/******add by liuxh 20101115 增加查询结算单位******/
	
	/**
	 * 查询结算单信息
	 * 注意：只统计索赔单状态为"审核通过"
	 * @param dealerId 经销商ID
	 * @param yieldly 产地ID
	 * @param startTime 结算单统计开始时间
	 * @param endTime 结算单统计终止时间
	 * @param companyId 区分微车和轿车
     * @param freeLabourAmount 免费保养类型索赔单中工时费用(系统中业务参数中设定)
	 * @return List<Map<String,Object>>
	 *         [DEALER_CODE:经销商代码; DEALER_NAME:经销商名称; CODE_DESC:产地名称; YIELDLY:产地代码; SERVICE_TOTAL_AMOUNT:服务活动总金额;
	 *         DEALER_ID:经销商ID; CLAIMCOUNT:索赔单数; REPAIR_TOTAL:申请总金额; FREE_M_PRICE:保养费用; SERVICE_LABOUR_AMOUNT:服务活动工时费用;
	 *         SERVICE_PART_AMOUNT:服务活动配件费用; SERVICE_NETITEM_AMOUNT:服务活动其他费用;  CAMPAIGN_FEE:服务活动固定费用;
	 *         LABOUR_AMOUNT:结算工时费用; PART_AMOUNT:结算材料费; NETITEM_AMOUNT:结算其他费用(救急费); BALANCE_AMOUNT:结算总金额;
	 *         APPEND_AMOUNT:追加金额; APPENDLABOUR_AMOUNT:追加工时金额;TOTAL_LABOUR_AMOUNT:工时总费用(工时费用:预留);
	 *         FREE_LABOUR_AMOUNT:保养工时费用; FREE_PART_AMOUNT:保养配件费用;]
	 */
	public List<Map<String,Object>> queryBalanceStatis(Long dealerId,String yieldly,
			String startTime,String endTime,Long companyId,Double freeLabourAmount){
		
		String status = Constant.CLAIM_APPLY_ORD_TYPE_04.toString();
		StringBuilder sql= new StringBuilder();

		sql.append("SELECT A2.*,B1.DEALER_CODE,B1.DEALER_NAME,C1.CODE_DESC\n" );
		sql.append(",(A2.SERVICE_LABOUR_AMOUNT+A2.SERVICE_PART_AMOUNT+A2.SERVICE_NETITEM_AMOUNT+CAMPAIGN_FEE) SERVICE_TOTAL_AMOUNT\n" );
		sql.append(",A2.LABOUR_AMOUNT TOTAL_LABOUR_AMOUNT\n" );
		sql.append("FROM(SELECT "+dealerId+" DEALER_ID,A1.YIELDLY,SUM(A1.CLAIMCOUNT) CLAIMCOUNT,SUM(A1.REPAIR_TOTAL) REPAIR_TOTAL,");
		sql.append("SUM(A1.FREE_M_PRICE) FREE_M_PRICE,\n" );
		sql.append("SUM(A1.SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(A1.SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,\n" );
		sql.append("SUM(A1.SERVICE_NETITEM_AMOUNT) SERVICE_NETITEM_AMOUNT,SUM(A1.CAMPAIGN_FEE) CAMPAIGN_FEE,\n" );
		sql.append("SUM(A1.LABOUR_AMOUNT) LABOUR_AMOUNT,SUM(A1.PART_AMOUNT) PART_AMOUNT,\n" );
		sql.append("SUM(A1.NETITEM_AMOUNT) NETITEM_AMOUNT,SUM(BALANCE_AMOUNT) BALANCE_AMOUNT,\n" );
		sql.append("SUM(A1.APPEND_AMOUNT) APPEND_AMOUNT,SUM(A1.APPENDLABOUR_AMOUNT) APPENDLABOUR_AMOUNT,\n" );
		sql.append("SUM(A1.FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(A1.FREE_M_PRICE)-SUM(A1.FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n");
		sql.append("FROM(\n" );
		//统计免费保养对应费用信息
		sql.append("SELECT A.YIELDLY,COUNT(*) CLAIMCOUNT,0 LABOUR_AMOUNT,0 PART_AMOUNT,0 NETITEM_AMOUNT,\n" );
		sql.append("0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,0 SERVICE_NETITEM_AMOUNT,\n" );
		sql.append("0 CAMPAIGN_FEE,(NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_M_PRICE,NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,\n");
		sql.append("NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT,"+freeLabourAmount+" FREE_LABOUR_AMOUNT\n");
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.STATUS = "+status+"\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_02+")\n" );
		sql.append("AND A.AUDITING_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.AUDITING_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.YIELDLY\n" );
		sql.append("UNION ALL\n" );
		//统计服务活动对应费用信息
		sql.append("SELECT A.YIELDLY,COUNT(*) CLAIMCOUNT,0 LABOUR_AMOUNT,0 PART_AMOUNT,0 NETITEM_AMOUNT,\n" );
		sql.append("(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_NETITEM_AMOUNT,\n" );
		sql.append("NVL(SUM(A.CAMPAIGN_FEE),0) CAMPAIGN_FEE,0 FREE_M_PRICE,NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,\n");
		sql.append("NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT,0 FREE_LABOUR_AMOUNT\n");
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.STATUS = "+status+"\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_06+")\n" );
		sql.append("AND A.AUDITING_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.AUDITING_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.DEALER_ID,A.YIELDLY\n" );
		sql.append("UNION ALL\n" );
		//统计除免费保养和服务活动之外索赔单费用
		sql.append("SELECT A.YIELDLY,COUNT(*) CLAIMCOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(APPENDLABOUR_AMOUNT),0)) LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) NETITEM_AMOUNT,\n" );
		sql.append("0 FREE_M_PRICE,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,0 SERVICE_NETITEM_AMOUNT,\n" );
		sql.append("0 CAMPAIGN_FEE,NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT,NVL(SUM(APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,\n");
		sql.append("NVL(SUM(APPEND_AMOUNT),0) APPEND_AMOUNT,0 FREE_LABOUR_AMOUNT\n");
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.STATUS = "+status+"\n" );
		sql.append("AND A.CLAIM_TYPE NOT IN ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+")\n" );
		sql.append("AND A.AUDITING_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.AUDITING_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.YIELDLY) A1\n" );
		sql.append("GROUP BY YIELDLY) A2,TM_DEALER B1,TC_CODE C1\n" );
		sql.append("WHERE A2.DEALER_ID = B1.DEALER_ID(+)\n" );
		sql.append("AND A2.YIELDLY = C1.CODE_ID(+)");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		
		return this.pageQuery(sql.toString(),paramList,this.getFunName());
	}
	
	/**
	 * 查询省份信息
	 * @param regionCode 地区代码
	 * @return
	 */
	public TmRegionPO queryProvince(String regionCode){
		
		String sql = "SELECT REGION_ID,REGION_CODE,REGION_NAME\n" +
				" FROM TM_REGION WHERE REGION_CODE = ?";
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(regionCode==null?"":regionCode);
		
		TmRegionPO resultPO = new TmRegionPO();
		List<TmRegionPO> regionList = this.select(TmRegionPO.class, sql, paramList);
		
		if(regionList!=null && regionList.size()>0)
			resultPO = regionList.get(0);
		return resultPO;
	}
	
	/**
	 * 按车系统计索赔单明细信息
	 * 注意：只统计索赔单状态为"审核通过"
	 * @param dealerId 经销商ID
	 * @param yieldly 产地ID
	 * @param startTime 结算单统计开始时间
	 * @param endTime 结算单统计终止时间
     * @param companyId 区分微车和轿车
     * @param freeLabourAmount 免费保养类型索赔单中工时费用(系统中业务参数中设定)
	 * @return
	 */
	public List<Map<String,Object>> queryBalanceGroupSeries(Long dealerId,String yieldly,
			String startTime,String endTime,Long companyId,Double freeLabourAmount){
		
		String status = Constant.CLAIM_APPLY_ORD_TYPE_04.toString();
		StringBuilder sql= new StringBuilder();

		sql.append("SELECT SERIES_CODE,NVL(SERIES_NAME,'--') SERIES_NAME,SUM(BEFORE_LABOUR_AMOUNT) BEFORE_LABOUR_AMOUNT,SUM(BEFORE_PART_AMOUNT) BEFORE_PART_AMOUNT,\n" );
		sql.append("SUM(BEFORE_OTHER_AMOUNT) BEFORE_OTHER_AMOUNT,SUM(AFTER_LABOUR_AMOUNT) AFTER_LABOUR_AMOUNT,SUM(AFTER_PART_AMOUNT) AFTER_PART_AMOUNT,\n" );
		sql.append("SUM(AFTER_OTHER_AMOUNT) AFTER_OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(FREE_COUNT) FREE_COUNT,SUM(SERVICE_COUNT) SERVICE_COUNT,\n" );
		sql.append("SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,SUM(BEFORE_CLAIM_COUNT) BEFORE_CLAIM_COUNT,SUM(AFTER_CLAIM_COUNT) AFTER_CLAIM_COUNT,\n" );
		sql.append("SUM(TOTAL_AMOUNT) TOTAL_AMOUNT,SUM(TOTAL_COUNT) TOTAL_COUNT\n" );
		sql.append(",SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_AMOUNT)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql.append(",(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_OTHER_AMOUNT)+SUM(SERVICE_FIXED_AMOUNT)) SERVICE_TOTAL_AMOUNT\n" );
		//免费保养结算统计（保养费用，保养次数）
		sql.append("FROM(SELECT A.SERIES_CODE,A.SERIES_NAME,0 BEFORE_LABOUR_AMOUNT, 0 BEFORE_PART_AMOUNT,");
		sql.append("0 BEFORE_OTHER_AMOUNT, 0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("(NVL(SUM(FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_AMOUNT,COUNT(*) FREE_COUNT,0 SERVICE_COUNT, 0 SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("0 SERVICE_PART_AMOUNT, 0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,\n" );
		sql.append("0 AFTER_CLAIM_COUNT,NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(","+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_02+")\n" );
		sql.append("AND A.STATUS = "+status+"\n" );
		sql.append("AND A.AUDITING_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.AUDITING_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.SERIES_CODE,A.SERIES_NAME\n" );
		sql.append("UNION ALL\n" );
		//PDI索赔结算统计（售前信息统计）
		sql.append("SELECT A.SERIES_CODE,A.SERIES_NAME,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) BEFORE_LABOUR_AMOUNT,");
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) BEFORE_PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,COUNT(*) BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_07+")\n" );
		sql.append("AND A.STATUS = "+status+"\n" );
		sql.append("AND A.AUDITING_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.AUDITING_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.SERIES_CODE,A.SERIES_NAME\n" );
		sql.append("UNION ALL\n" );
		//服务活动结算统计（统计服务活动费用）
		sql.append("SELECT A.SERIES_CODE,A.SERIES_NAME,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("0 BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,COUNT(*) SERVICE_COUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("NVL(SUM(A.CAMPAIGN_FEE),0) SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_06+")\n" );
		sql.append("AND A.STATUS = "+status+"\n" );
		sql.append("AND A.AUDITING_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.AUDITING_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.SERIES_CODE,A.SERIES_NAME\n" );
		sql.append("UNION ALL\n" );
		//统计除以上其他类型索赔单结算费用
		sql.append("SELECT A.SERIES_CODE,A.SERIES_NAME,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("0 BEFORE_OTHER_AMOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) AFTER_LABOUR_AMOUNT,");
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) AFTER_PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,COUNT(*) AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.YIELDLY = ?\n" );
		sql.append("AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("AND A.CLAIM_TYPE NOT IN ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+","+Constant.CLA_TYPE_07+")\n" );
		sql.append("AND A.STATUS = "+status+"\n" );
		sql.append("AND A.AUDITING_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.AUDITING_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("GROUP BY A.SERIES_CODE,A.SERIES_NAME)\n" );
		sql.append("GROUP BY SERIES_CODE,SERIES_NAME");
		
		System.out.println("sqlsql"+sql);
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}

	/**
	 * 保存索赔单信息
	 * @param balancePO
	 */
	public void saveBalanceOrder(TtAsWrClaimBalancePO balancePO){
		
		if(balancePO==null)
			return;
		this.insert(balancePO);
	}

	/**
	 * 查询对应选择时间范围和产地内可以结算的索赔单保存到索赔单同结算单关系表中
	 * 注意：只统计索赔单状态为"审核通过",默认STATUS为无效，即未审核通过
	 * @param dealerId 经销商ID
	 * @param yieldly 产地ID
	 * @param startTime 结算单统计开始时间
	 * @param endTime 结算单统计终止时间
	 * @param companyId 轿车和微车公司ID
	 * @param userId 操作用户
	 * @param balanceId 结算单ID
	 */
	public void saveRelaBetweenClaimAndBalance(Long dealerId,String yieldly,
			String startTime,String endTime,Long companyId,Long userId,String balanceId){
		
		String status = Constant.CLAIM_APPLY_ORD_TYPE_04.toString();
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TR_BALANCE_CLAIM\n" );
		sql.append("  (ID, CLAIM_ID, BALANCE_ID, UPDATE_BY, UPDATE_DATE, CREATE_BY, CREATE_DATE,STATUS)\n" );
		sql.append("  SELECT F_GETID() ID,A.ID CLAIM_ID,'"+balanceId+"' BALANCE_ID,\n" );
		sql.append("         '"+userId.toString()+"',SYSDATE,'"+userId.toString()+"',SYSDATE,'"+Constant.STATUS_DISABLE+"'\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("   WHERE 1 = 1\n" );
		sql.append("	 AND NOT EXISTS (SELECT B.ID FROM TR_BALANCE_CLAIM B WHERE B.CLAIM_ID = A.ID)\n");
		sql.append("	 AND A.DEALER_ID = ?\n" );
		sql.append("     AND A.YIELDLY = ?\n" );
		sql.append("     AND A.OEM_COMPANY_ID = ?\n" );
		sql.append("     AND A.STATUS = "+status+"\n" );
		sql.append("     AND A.AUDITING_DATE >= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("     AND A.AUDITING_DATE <= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')\n");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);paramList.add(yieldly);paramList.add(companyId);paramList.add(startTime);paramList.add(endTime);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 将生成过结算单的索赔单状态修改为“结算审核中”
	 * @param balanceId 结算单ID
	 */
	public void updateClaimStatus(Long balanceId,Integer status){
		
		if(balanceId==null || status==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_APPLICATION A\n" );
		sql.append("   SET A.STATUS = "+status+",CLAIM_BLANCE_ID=?\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND EXISTS (SELECT B.ID\n" );
		sql.append("          FROM TR_BALANCE_CLAIM B\n" );
		sql.append("         WHERE B.CLAIM_ID = A.ID\n" );
		sql.append("           AND B.BALANCE_ID = ?)");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		this.update(sql.toString(), paramList);
	}
	/**
	 * 经销商删除结算单
	 * @param balanceId 结算单ID
	 */
	public void updateClaimStatusDel(Long balanceId,Integer status){
		
		if(balanceId==null || status==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_APPLICATION A\n" );
		sql.append("   SET A.STATUS = "+status+",CLAIM_BLANCE_ID=NULL \n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND EXISTS (SELECT B.ID\n" );
		sql.append("          FROM TR_BALANCE_CLAIM B\n" );
		sql.append("         WHERE B.CLAIM_ID = A.ID\n" );
		sql.append("           AND B.BALANCE_ID = ?)");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
	}
	/**
	 * 保存结算单明细
	 * @param balanceId 结算单ID
	 * @param userId 用户Id 
	 * @param freeLabourAmount 免费保养类型索赔单中工时费用(系统中业务参数中设定)
	 */
	public void saveBalanceDetail(Long balanceId,Long userId,Double freeLabourAmount){
		
		if(balanceId==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_CLAIM_BALANCE_DETAIL(ID,BALANCE_ID,series_id,SERIES_CODE,SERIES_NAME,BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,\n" );
		sql.append("BEFORE_OTHER_AMOUNT,AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,FREE_CLAIM_COUNT,FREE_CLAIM_AMOUNT,\n" );
		sql.append("SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,BEFORE_CLAIM_COUNT,\n" );
		sql.append("AFTER_CLAIM_COUNT,SERVICE_CLAIM_COUNT,UPDATE_BY,UPDATE_DATE,CREATE_BY,CREATE_DATE,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,TOTAL_AMOUNT)\n" );
		//查询需要插入的数据
		sql.append("SELECT F_GETID() ID,"+balanceId+" BALANCE_ID,SERIES_ID,mg.group_CODE,mg.group_NAME,BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,\n" );
		sql.append("BEFORE_OTHER_AMOUNT,AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,FREE_COUNT FREE_CLAIM_COUNT,FREE_AMOUNT FREE_CLAIM_AMOUNT,\n" );
		sql.append("SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,BEFORE_CLAIM_COUNT,\n" );
		sql.append("AFTER_CLAIM_COUNT,SERVICE_COUNT SERVICE_CLAIM_COUNT,'"+userId+"' UPDATE_BY,SYSDATE UPDATE_DATE,'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE\n" );
		sql.append(",FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,TOTAL_AMOUNT\n");
		sql.append("FROM(SELECT SERIES_ID,SUM(BEFORE_LABOUR_AMOUNT) BEFORE_LABOUR_AMOUNT,SUM(BEFORE_PART_AMOUNT) BEFORE_PART_AMOUNT,\n" );
		sql.append("SUM(BEFORE_OTHER_AMOUNT) BEFORE_OTHER_AMOUNT,SUM(AFTER_LABOUR_AMOUNT) AFTER_LABOUR_AMOUNT,SUM(AFTER_PART_AMOUNT) AFTER_PART_AMOUNT,\n" );
		sql.append("SUM(AFTER_OTHER_AMOUNT) AFTER_OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(FREE_COUNT) FREE_COUNT,SUM(SERVICE_COUNT) SERVICE_COUNT,\n" );
		sql.append("SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,SUM(BEFORE_CLAIM_COUNT) BEFORE_CLAIM_COUNT,SUM(AFTER_CLAIM_COUNT) AFTER_CLAIM_COUNT,\n" );
		sql.append("SUM(TOTAL_AMOUNT) TOTAL_AMOUNT,SUM(TOTAL_COUNT) TOTAL_COUNT\n" );
		sql.append(",SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_AMOUNT)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql.append(",(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_OTHER_AMOUNT)+SUM(SERVICE_FIXED_AMOUNT)) SERVICE_TOTAL_AMOUNT\n" );
		//统计免费保养费用
		sql.append("FROM(SELECT a.SERIES_ID,0 BEFORE_LABOUR_AMOUNT, 0 BEFORE_PART_AMOUNT,0 BEFORE_OTHER_AMOUNT,");
		sql.append(" 0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("(NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_AMOUNT,COUNT(*) FREE_COUNT,0 SERVICE_COUNT, 0 SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("0 SERVICE_PART_AMOUNT, 0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,\n" );
		sql.append("0 AFTER_CLAIM_COUNT,NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(","+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_02+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.SERIES_ID\n" );
		sql.append("UNION ALL\n" );
		//统计售前维修费用
		sql.append("SELECT A.SERIES_ID,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) BEFORE_LABOUR_AMOUNT,");
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) BEFORE_PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,COUNT(*) BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_07+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.SERIES_ID\n" );
		//统计服务活动维修费用
		sql.append("UNION ALL\n" );
		sql.append("SELECT A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("0 BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,COUNT(*) SERVICE_COUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("NVL(SUM(A.CAMPAIGN_FEE),0) SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_06+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.SERIES_ID\n" );
		//统计除以上三中类型索赔的费用
		sql.append("UNION ALL\n" );
		sql.append("SELECT A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("0 BEFORE_OTHER_AMOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) AFTER_LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) AFTER_PART_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) AFTER_OTHER_AMOUNT,\n" );
		sql.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,COUNT(*) AFTER_CLAIM_COUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE NOT IN ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+","+Constant.CLA_TYPE_07+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.SERIES_ID)\n" );
		sql.append(" GROUP BY SERIES_ID), tm_vhcl_material_group mg  where mg.group_id = SERIES_ID ");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
		/****add xiongchuan 2011-03-14****插入结算申请明细***/
		StringBuilder sql1= new StringBuilder();
		sql1.append("INSERT INTO TT_AS_WR_BALANCE_DETAIL_BAK (ID,BALANCE_ID,series_id,SERIES_CODE,SERIES_NAME,BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,\n" );
		sql1.append("BEFORE_OTHER_AMOUNT,AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,FREE_CLAIM_COUNT,FREE_CLAIM_AMOUNT,\n" );
		sql1.append("SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,BEFORE_CLAIM_COUNT,\n" );
		sql1.append("AFTER_CLAIM_COUNT,SERVICE_CLAIM_COUNT,UPDATE_BY,UPDATE_DATE,CREATE_BY,CREATE_DATE,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,TOTAL_AMOUNT)\n" );
		//查询需要插入的数据
		sql1.append("SELECT F_GETID() ID,"+balanceId+" BALANCE_ID,SERIES_ID,mg.group_CODE,mg.group_NAME,BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,\n" );
		sql1.append("BEFORE_OTHER_AMOUNT,AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,FREE_COUNT FREE_CLAIM_COUNT,FREE_AMOUNT FREE_CLAIM_AMOUNT,\n" );
		sql1.append("SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,BEFORE_CLAIM_COUNT,\n" );
		sql1.append("AFTER_CLAIM_COUNT,SERVICE_COUNT SERVICE_CLAIM_COUNT,'"+userId+"' UPDATE_BY,SYSDATE UPDATE_DATE,'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE\n" );
		sql1.append(",FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,TOTAL_AMOUNT\n");
		sql1.append("FROM(SELECT SERIES_ID,SUM(BEFORE_LABOUR_AMOUNT) BEFORE_LABOUR_AMOUNT,SUM(BEFORE_PART_AMOUNT) BEFORE_PART_AMOUNT,\n" );
		sql1.append("SUM(BEFORE_OTHER_AMOUNT) BEFORE_OTHER_AMOUNT,SUM(AFTER_LABOUR_AMOUNT) AFTER_LABOUR_AMOUNT,SUM(AFTER_PART_AMOUNT) AFTER_PART_AMOUNT,\n" );
		sql1.append("SUM(AFTER_OTHER_AMOUNT) AFTER_OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(FREE_COUNT) FREE_COUNT,SUM(SERVICE_COUNT) SERVICE_COUNT,\n" );
		sql1.append("SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,\n" );
		sql1.append("SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,SUM(BEFORE_CLAIM_COUNT) BEFORE_CLAIM_COUNT,SUM(AFTER_CLAIM_COUNT) AFTER_CLAIM_COUNT,\n" );
		sql1.append("SUM(TOTAL_AMOUNT) TOTAL_AMOUNT,SUM(TOTAL_COUNT) TOTAL_COUNT\n" );
		sql1.append(",SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_AMOUNT)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql1.append(",(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_OTHER_AMOUNT)+SUM(SERVICE_FIXED_AMOUNT)) SERVICE_TOTAL_AMOUNT\n" );
		//统计免费保养费用
		sql1.append("FROM(SELECT a.SERIES_ID,0 BEFORE_LABOUR_AMOUNT, 0 BEFORE_PART_AMOUNT,0 BEFORE_OTHER_AMOUNT,");
		sql1.append(" 0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql1.append("(NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_AMOUNT,COUNT(*) FREE_COUNT,0 SERVICE_COUNT, 0 SERVICE_LABOUR_AMOUNT,\n" );
		sql1.append("0 SERVICE_PART_AMOUNT, 0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,\n" );
		sql1.append("0 AFTER_CLAIM_COUNT,NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql1.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql1.append(","+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql1.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = B.CLAIM_ID\n" );
		sql1.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_02+")\n" );
		sql1.append("AND B.BALANCE_ID = ?\n" );
		sql1.append("GROUP BY A.SERIES_ID\n" );
		sql1.append("UNION ALL\n" );
		//统计售前维修费用
		sql1.append("SELECT A.SERIES_ID,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) BEFORE_LABOUR_AMOUNT,");
		sql1.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) BEFORE_PART_AMOUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql1.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql1.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,COUNT(*) BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql1.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql1.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql1.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = B.CLAIM_ID\n" );
		sql1.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_07+")\n" );
		sql1.append("AND B.BALANCE_ID = ?\n" );
		sql1.append("GROUP BY A.SERIES_ID\n" );
		//统计服务活动维修费用
		sql1.append("UNION ALL\n" );
		sql1.append("SELECT A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql1.append("0 BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql1.append("0 FREE_AMOUNT,0 FREE_COUNT,COUNT(*) SERVICE_COUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_OTHER_AMOUNT,\n" );
		sql1.append("NVL(SUM(A.CAMPAIGN_FEE),0) SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql1.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql1.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql1.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = B.CLAIM_ID\n" );
		sql1.append("AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_06+")\n" );
		sql1.append("AND B.BALANCE_ID = ?\n" );
		sql1.append("GROUP BY A.SERIES_ID\n" );
		//统计除以上三中类型索赔的费用
		sql1.append("UNION ALL\n" );
		sql1.append("SELECT A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql1.append("0 BEFORE_OTHER_AMOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) AFTER_LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) AFTER_PART_AMOUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) AFTER_OTHER_AMOUNT,\n" );
		sql1.append("0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql1.append("0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,COUNT(*) AFTER_CLAIM_COUNT,\n" );
		sql1.append("NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql1.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql1.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql1.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = B.CLAIM_ID\n" );
		sql1.append("AND A.CLAIM_TYPE NOT IN ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+","+Constant.CLA_TYPE_07+")\n" );
		sql1.append("AND B.BALANCE_ID = ?\n" );
		sql1.append("GROUP BY A.SERIES_ID)\n" );
		sql1.append(" GROUP BY SERIES_ID), tm_vhcl_material_group mg  where mg.group_id = SERIES_ID ");
		List<Object> paramList1 = new ArrayList<Object>();
		paramList1.add(balanceId);
		paramList1.add(balanceId);
		paramList1.add(balanceId);
		paramList1.add(balanceId);
		this.update(sql1.toString(), paramList1);
		/****add xiongchuan 2011-03-14*******/
	}
	
	/**
	 * 将备注信息写入到结算单中
	 * @param seriesCode 车系代码
	 * @param remark 备注
	 * @param balanceId 结算单ID
	 */
	public void modifyBalanceDetail(String seriesCode,String remark,Long balanceId){
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(CommonUtils.checkNull(remark));
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_DETAIL\n" );
		sql.append("SET REMARK = ?\n" );
		sql.append("WHERE 1=1\n" );
		if(seriesCode==null || "".equals(seriesCode)){
			sql.append("AND SERIES_CODE IS NULL\n");
		}else{
			sql.append("AND SERIES_CODE = ?\n");
			paramList.add(CommonUtils.checkNull(seriesCode));
		}
		sql.append("AND BALANCE_ID = ?\n" );
		
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 根据结算单ID重新计算对应结算单的个项费用信息
	 * @param balanceId
	 * @param freeLabourAmount 免费保养类型索赔单中工时费用(系统中业务参数中设定)
	 */
	public void reCheckBalanceAmount(Long balanceId,Double freeLabourAmount){
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE\n" );
		sql.append("SET (LABOUR_AMOUNT,PART_AMOUNT,OTHER_AMOUNT,FREE_AMOUNT,SERVICE_FIXED_AMOUNT,\n" );
		sql.append("SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,AMOUNT_SUM,CLAIM_COUNT,BALANCE_AMOUNT,");
		sql.append("APPEND_LABOUR_AMOUNT,APPEND_AMOUNT,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,SERVICE_TOTAL_AMOUNT) = (\n" );
		
		sql.append("SELECT LABOUR_AMOUNT,PART_AMOUNT,NETITEM_AMOUNT OTHER_AMOUNT,FREE_M_PRICE FREE_AMOUNT,\n" );
		sql.append("CAMPAIGN_FEE SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,\n" );
		sql.append("SERVICE_NETITEM_AMOUNT SERVICE_OTHER_AMOUNT,BALANCE_AMOUNT AMOUNT_SUM,CLAIMCOUNT CLAIM_COUNT,BALANCE_AMOUNT\n" );
		sql.append(",APPENDLABOUR_AMOUNT,APPEND_AMOUNT,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT,SERVICE_TOTAL_AMOUNT\n");
		sql.append("FROM(SELECT A1.YIELDLY,SUM(A1.CLAIMCOUNT) CLAIMCOUNT,SUM(A1.REPAIR_TOTAL) REPAIR_TOTAL,\n" );
		sql.append("SUM(A1.FREE_M_PRICE) FREE_M_PRICE,\n" );
		sql.append("SUM(A1.SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(A1.SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,\n" );
		sql.append("SUM(A1.SERVICE_NETITEM_AMOUNT) SERVICE_NETITEM_AMOUNT,SUM(A1.CAMPAIGN_FEE) CAMPAIGN_FEE,\n" );
		sql.append("SUM(A1.LABOUR_AMOUNT) LABOUR_AMOUNT,SUM(A1.PART_AMOUNT) PART_AMOUNT,\n" );
		sql.append("SUM(A1.NETITEM_AMOUNT) NETITEM_AMOUNT,SUM(A1.BALANCE_AMOUNT) BALANCE_AMOUNT\n" );
		sql.append(",SUM(APPENDLABOUR_AMOUNT) APPENDLABOUR_AMOUNT,SUM(APPEND_AMOUNT) APPEND_AMOUNT\n" );
		sql.append(",SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_M_PRICE)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql.append(",(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_NETITEM_AMOUNT)+SUM(CAMPAIGN_FEE)) SERVICE_TOTAL_AMOUNT\n" );
		sql.append("FROM(\n" );
		//免费保养
		sql.append("SELECT A.YIELDLY,COUNT(*) CLAIMCOUNT,0 LABOUR_AMOUNT,0 PART_AMOUNT,0 NETITEM_AMOUNT,\n" );
		sql.append("0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,0 SERVICE_NETITEM_AMOUNT,\n" );
		sql.append("0 CAMPAIGN_FEE,(NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_M_PRICE,NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(","+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_02+"\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.YIELDLY\n" );
		sql.append("UNION ALL\n" );
		//服务活动
		sql.append("SELECT A.YIELDLY,COUNT(*) CLAIMCOUNT,0 LABOUR_AMOUNT,0 PART_AMOUNT,0 NETITEM_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_NETITEM_AMOUNT,\n" );
		sql.append("NVL(SUM(A.CAMPAIGN_FEE),0) CAMPAIGN_FEE,0 FREE_M_PRICE,NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.YIELDLY\n" );
		sql.append("UNION ALL\n" );
		//除以上两种类型
		sql.append("SELECT A.YIELDLY,COUNT(*) CLAIMCOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) LABOUR_AMOUNT,\n" );
		sql.append("NVL(SUM(A.BALANCE_PART_AMOUNT),0) PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) NETITEM_AMOUNT,\n" );
		sql.append("0 FREE_M_PRICE,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,0 SERVICE_NETITEM_AMOUNT,\n" );
		sql.append("0 CAMPAIGN_FEE,NVL(SUM(A.REPAIR_TOTAL),0) REPAIR_TOTAL,NVL(SUM(A.BALANCE_AMOUNT),0) BALANCE_AMOUNT\n" );
		sql.append(",NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append(",0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.CLAIM_ID\n" );
		sql.append("AND A.CLAIM_TYPE  IN ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_07+","+Constant.CLA_TYPE_09+")\n" );
		sql.append("AND B.BALANCE_ID = ?\n" );
		sql.append("GROUP BY A.YIELDLY) A1\n" );
		sql.append("GROUP BY YIELDLY) A2\n" );
		sql.append("WHERE ROWNUM = 1)\n" );
		sql.append("WHERE ID = ?");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
		
		StringBuilder sql1= new StringBuilder();
		sql1.append("UPDATE TT_AS_WR_CLAIM_BALANCE A\n" );
		sql1.append("SET A.BALANCE_AMOUNT = (NVL(A.BALANCE_AMOUNT,0)+NVL(A.RETURN_AMOUNT,0)+NVL(A.MARKET_AMOUNT,0)+NVL(A.SPEOUTFEE_AMOUNT,0)),\n" );
		sql1.append("A.AMOUNT_SUM = (NVL(A.BALANCE_AMOUNT,0)+NVL(A.RETURN_AMOUNT,0)+NVL(A.MARKET_AMOUNT,0)+NVL(A.SPEOUTFEE_AMOUNT,0))\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A.ID = ?");
		
		List<Object> paramList2 = new ArrayList<Object>();
		paramList2.add(balanceId);
		
		this.update(sql1.toString(), paramList2);
		
		/********add by liuxh 20101127 更新结算单结算总金额  BALANCE_AMOUNT(结算金额) AMOUNT_SUM(索赔单金额)*******/
		StringBuffer sbSqlBlance=new StringBuffer();
		sbSqlBlance.append("UPDATE TT_AS_WR_CLAIM_BALANCE SET BALANCE_AMOUNT=(NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
		sbSqlBlance.append("+NVL(RETURN_AMOUNT,0)+NVL(MARKET_AMOUNT,0)+NVL(SPEOUTFEE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0)), ");
		sbSqlBlance.append("AMOUNT_SUM=(NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
		sbSqlBlance.append("+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0)) ");
		sbSqlBlance.append("WHERE ID=?");
		List<Object> paramList3 = new ArrayList<Object>();
		paramList3.add(balanceId);
		this.update(sbSqlBlance.toString(), paramList3);
		/********add by liuxh 20101127 更新结算单结算总金额*******/
		/*****add by liuxh 20101213 重新计算索赔单数量******/
		this.setClaimCount(balanceId);//重新统计CLAIM_COUNT
	
	}
	/*****add by liuxh 20101213 增加统计索赔单数量******/
	public void setClaimCount(Long balanceId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("UPDATE TT_AS_WR_CLAIM_BALANCE A SET A.CLAIM_COUNT=(SELECT COUNT(B.CLAIM_ID) FROM TR_BALANCE_CLAIM B WHERE A.ID=B.BALANCE_ID) \n");
		sbSql.append("WHERE A.ID=? ");
		List listPar=new ArrayList();
		listPar.add(balanceId);
		this.update(sbSql.toString(), listPar);
	}
	/*****add by liuxh 20101213 增加统计索赔单数量******/
	
	/**
	 * 查询对应结算单下的索赔单数
	 * @param balanceId 结算单ID
	 * @return List<Map<String,Object>>
	 *         键名：CLAIM_ID
	 */
	public List<Map<String,Object>> queryClaimByBalanceId(Long balanceId){
		
		StringBuilder sql= new StringBuilder();
		/*****mod by liuxh 20101212 审核结算单下的索赔单时,只审核标志为0(未审核)的索赔单******/
//		sql.append("SELECT CLAIM_ID\n" );
//		sql.append("FROM TR_BALANCE_CLAIM\n" );
//		sql.append("WHERE 1=1\n" );
//		sql.append("AND BALANCE_ID = ? AND TASK_FLAG='0' ");

		sql.append("SELECT B.CLAIM_ID FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B \n");
		sql.append("WHERE A.ID=B.CLAIM_ID AND B.BALANCE_ID=? AND A.TASK_FLAG='0' ");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
		/*****mod by liuxh 20101212 审核结算单下的索赔单时,只审核标志为0(未审核)的索赔单******/
	}
	/*****add by liuxh 20101212 判断结算单下是否还有未审核完成的索赔单*****/
	public long getClaimCount(Long balanceId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT COUNT(A.ID) AS COUNT FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B \n");
		sbSql.append("WHERE A.ID=B.CLAIM_ID AND B.BALANCE_ID=? AND A.TASK_FLAG='0' \n");
		List listPar=new ArrayList();
		listPar.add(balanceId);
		List list=this.pageQuery(sbSql.toString(), listPar, this.getFunName());
		Map map=(Map)list.get(0);
		Long count=((BigDecimal)map.get("COUNT")).longValue();
		return count;
	}
	/*****add by liuxh 20101212 判断汇总单下是否还有未自动审核完的结算单*****/
	public long getGatherCount(Long gatherId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT COUNT(B.ID) AS COUNT FROM TR_GATHER_BALANCE A,TT_AS_WR_CLAIM_BALANCE B WHERE A.BALANCE_ID=B.ID \n");
		sbSql.append("AND A.GATHER_ID=? AND B.STATUS=? ");
		List listPar=new ArrayList();
		listPar.add(gatherId);
		listPar.add(Constant.ACC_STATUS_06);
		List list=this.pageQuery(sbSql.toString(), listPar, this.getFunName());
		Map map=(Map)list.get(0);
		Long count=((BigDecimal)map.get("COUNT")).longValue();
		return count;
	}
	/*********add by liuxh 统计申请金额**********/
	public void balancrApplyAmount(Long balanceId){
		StringBuilder sql= new StringBuilder();
		sql.append("update Tt_As_Wr_Claim_Balance c\n");
		sql.append("   set c.apply_amount = nvl(c.LABOUR_AMOUNT,0) + nvl(PART_AMOUNT,0) + nvl(OTHER_AMOUNT,0) +\n" );
		sql.append("                        nvl(FREE_AMOUNT,0) + nvl(RETURN_AMOUNT,0) + nvl(MARKET_AMOUNT,0) +\n" );
		sql.append("                        nvl(SPEOUTFEE_AMOUNT,0) + \n" );
		sql.append("                        nvl(APPEND_AMOUNT,0) + nvl(SERVICE_TOTAL_AMOUNT,0)\n" );
		sql.append("\n" );
		sql.append(" where c.id =? ");
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
	    this.update(sql.toString(), paramList);
	}
	/******add by liuxh 20101129 增加经销商新增结算单时保存金额备份字段*******/
	public void updateBalanceBak(Long balanceId){
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE SET LABOUR_AMOUNT_BAK=LABOUR_AMOUNT,PART_AMOUNT_BAK=PART_AMOUNT,\n " );
		sql.append("OTHER_AMOUNT_BAK=OTHER_AMOUNT,FREE_AMOUNT_BAK=FREE_AMOUNT,RETURN_AMOUNT_BAK=RETURN_AMOUNT,\n " );
		sql.append("MARKET_AMOUNT_BAK=MARKET_AMOUNT,SPEOUTFEE_AMOUNT_BAK=SPEOUTFEE_AMOUNT,APPEND_AMOUNT_BAK=APPEND_AMOUNT,\n ");
	    sql.append("SERVICE_TOTAL_AMOUNT_BAK=SERVICE_TOTAL_AMOUNT,APPEND_LABOUR_AMOUNT_BAK=APPEND_LABOUR_AMOUNT WHERE ID=? ") ;

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
	    this.update(sql.toString(), paramList);
	}
	/******add by liuxh 20101129 增加经销商新增结算单时保存金额备份字段*******/
	/**
	 * 查询对应结算单下的索赔单数
	 * @param balanceId 结算单ID
	 * @param status 索赔单状态
	 * @param authCode 需要授权级别
	 * @return List<Map<String,Object>>
	 *         键名：CLAIM_ID
	 */
	public List<Map<String,Object>> queryClaimByBalanceId(Long balanceId,String status,String authCode){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT CLAIM_ID\n" );
		sql.append("FROM TR_BALANCE_CLAIM A,TT_AS_WR_APPLICATION B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIM_ID = B.ID\n");
		sql.append("AND B.STATUS = ?\n");
		sql.append("AND B.AUTH_CODE = ?\n");
		sql.append("AND A.BALANCE_ID = ?");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(CommonUtils.checkNull(status));
		paramList.add(CommonUtils.checkNull(authCode));
		paramList.add(balanceId);
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}
	
	/**
	 * 根据结算单ID重新计算对应结算单的个项费用信息
	 */
	public void writeBackBalanceAmount(Long balanceId){
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE\n" );
		sql.append("   SET BALANCE_AMOUNT = (SELECT SUM(A.BALANCE_AMOUNT)\n" );
		sql.append("                           FROM TT_AS_WR_APPLICATION A, TR_BALANCE_CLAIM B\n" );
		sql.append("                          WHERE A.ID = B.CLAIM_ID\n" );
		sql.append("                            AND B.BALANCE_ID = ?)\n" );
		sql.append(" WHERE ID = ?");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 查询在指定的月份限制内，是否该经销商存在未回应配件
	 * 注：1、如果存在，则只返回1条记录
	 *        不存在，记录为空
	 *     2、只检测需要回运的配件记录
	 * @param dealerId
	 * @param limitMonths
	 * @return List
	 */
	public List loadReturnPartRecord(String dealerId,Integer limitMonths){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.ID\n" );
		sql.append("  FROM TT_AS_WR_APPLICATION C, TT_AS_WR_PARTSITEM A, TM_PT_PART_BASE B\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND C.ID = A.ID\n" );
		sql.append("   AND A.PART_CODE = B.PART_CODE\n" );
		sql.append("   AND NVL(A.RETURN_NUM,0) < NVL(A.BALANCE_QUANTITY,0)\n" );
		sql.append("   AND B.IS_RETURN = "+Constant.IS_NEED_RETURN+"\n" );
		sql.append("   AND C.REPORT_DATE < ADD_MONTHS(SYSDATE, 0 - ?)\n" );
		sql.append("   AND C.DEALER_ID = ?\n");
		sql.append("   AND ROWNUM<2");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(limitMonths);
		paramList.add(dealerId);
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}
	
	/**
	 * 经销商查询对应自己的结算单
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> queryAccAuditList(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		/*****mod by liuxh 20101115 经销商只能查本身  和 下级经销商 为 上级结算的经销商*****/
		sql.append("WITH TT_DEALER_SET AS(\n" );
		sql.append("SELECT A.DEALER_ID,A.PARENT_DEALER_D,A.DEALER_LEVEL\n" );
		sql.append("FROM TM_DEALER A\n" );
		sql.append("WHERE 1=1\n");
		sql.append("AND A.DEALER_ID=?\n");
		sql.append("UNION\n");
		sql.append("SELECT A.DEALER_ID,A.PARENT_DEALER_D,A.DEALER_LEVEL\n" );
		sql.append("FROM TM_DEALER A\n" );
		sql.append("WHERE 1=1\n" );
		
		sql.append("AND A.BALANCE_LEVEL=?\n");
		sql.append("START WITH A.DEALER_ID = ?\n" );
		sql.append("CONNECT BY PRIOR A.DEALER_ID = A.PARENT_DEALER_D)\n");
		/*****mod by liuxh 20101115 经销商只能查本身  和 下级经销商 为 上级结算的经销商*****/
		
		
		sql.append("SELECT ROWNUM NUM, A.DEALER_ID,A.ID, A.BALANCE_NO, A.DEALER_CODE, A.DEALER_NAME, A.CLAIM_COUNT,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS, A.YIELDLY,A.BALANCE_AMOUNT,a.apply_amount,A.AMOUNT_SUM\n" );
		sql.append("       ,TO_CHAR(A.START_DATE, 'YYYY-MM-DD') START_DATE, TO_CHAR(A.END_DATE, 'YYYY-MM-DD') END_DATE,\n" );
		sql.append("(SELECT C.STATUS FROM TR_GATHER_BALANCE B,TT_AS_WR_GATHER_BALANCE C WHERE B.GATHER_ID=C.ID AND B.BALANCE_ID=A.ID) GATHERSTATUS\n");
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TT_DEALER_SET B \n");
		sql.append("WHERE 1=1\n");
		sql.append("AND a.OEM_COMPANY_ID = ").append(bean.getOemCompanyId()).append("\n");
		sql.append("AND A.DEALER_ID = B.DEALER_ID\n");
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND YIELDLY = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getStatus())){
			sql.append("AND STATUS = ").append(bean.getStatus()).append("\n");
		}
		if(Utility.testString(bean.getBalanceNo())){
			sql.append("AND BALANCE_NO LIKE '%").append(bean.getBalanceNo()).append("%'\n");
		}

		sql.append("ORDER BY ID DESC");
		
		List<Object> paramList = new ArrayList<Object>();
		//paramList.add(Constant.BALANCE_LEVEL_SELF);//独立结算
		paramList.add(bean.getDealerId());
		paramList.add(Constant.BALANCE_LEVEL_HIGH);//上级结算
		paramList.add(bean.getDealerId());
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), paramList, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 结算室审核过后，更新结算单子表数据
	 * @param balanceId
	 * @param freeLabourAmount 免费保养类型索赔单中工时费用(系统中业务参数中设定)
	 */
	public void reCheckBalanceDetail(Long balanceId,Double freeLabourAmount){
		if(balanceId==null)
			return;
		StringBuilder sql= new StringBuilder();
//		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_DETAIL A1\n" );
//		sql.append("   SET (A1.BEFORE_LABOUR_AMOUNT, A1.BEFORE_PART_AMOUNT, A1.BEFORE_OTHER_AMOUNT,\n" );
//		sql.append("   A1.AFTER_LABOUR_AMOUNT, A1.AFTER_PART_AMOUNT, A1.AFTER_OTHER_AMOUNT,\n" );
//		sql.append("   A1.FREE_CLAIM_COUNT, A1.FREE_CLAIM_AMOUNT,\n" );
//		sql.append("   A1.SERVICE_FIXED_AMOUNT, A1.SERVICE_LABOUR_AMOUNT, A1.SERVICE_PART_AMOUNT, A1.SERVICE_OTHER_AMOUNT,\n" );
//		sql.append("   A1.BEFORE_CLAIM_COUNT, A1.AFTER_CLAIM_COUNT, A1.SERVICE_CLAIM_COUNT,A1.FREE_LABOUR_AMOUNT,A1.FREE_PART_AMOUNT,A1.TOTAL_AMOUNT) = (\n" );
//		
//		sql.append("SELECT BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,BEFORE_OTHER_AMOUNT,\n" );
//		sql.append("     AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,\n" );
//		sql.append("     FREE_COUNT FREE_CLAIM_COUNT,FREE_AMOUNT FREE_CLAIM_AMOUNT,\n" );
//		sql.append("    SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,\n" );
//		sql.append("    BEFORE_CLAIM_COUNT, AFTER_CLAIM_COUNT,SERVICE_COUNT SERVICE_CLAIM_COUNT,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT, TOTAL_AMOUNT\n" );
//		sql.append("    FROM(SELECT BALANCE_ID,SERIES_ID,SUM(BEFORE_LABOUR_AMOUNT) BEFORE_LABOUR_AMOUNT,SUM(BEFORE_PART_AMOUNT) BEFORE_PART_AMOUNT,\n" );
//		sql.append("    SUM(BEFORE_OTHER_AMOUNT) BEFORE_OTHER_AMOUNT,SUM(AFTER_LABOUR_AMOUNT) AFTER_LABOUR_AMOUNT,SUM(AFTER_PART_AMOUNT) AFTER_PART_AMOUNT,\n" );
//		sql.append("    SUM(AFTER_OTHER_AMOUNT) AFTER_OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(FREE_COUNT) FREE_COUNT,SUM(SERVICE_COUNT) SERVICE_COUNT,\n" );
//		sql.append("    SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,\n" );
//		sql.append("    SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,SUM(BEFORE_CLAIM_COUNT) BEFORE_CLAIM_COUNT,SUM(AFTER_CLAIM_COUNT) AFTER_CLAIM_COUNT,\n" );
//		sql.append("    SUM(TOTAL_AMOUNT) TOTAL_AMOUNT,SUM(TOTAL_COUNT) TOTAL_COUNT\n" );
//		sql.append("   ,SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_AMOUNT)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
//		sql.append("   ,(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_OTHER_AMOUNT)+SUM(SERVICE_FIXED_AMOUNT)) SERVICE_TOTAL_AMOUNT\n" );
//		
//		//统计免费保养费用
//		sql.append("    FROM(SELECT B.BALANCE_ID,A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT, 0 BEFORE_PART_AMOUNT,0 BEFORE_OTHER_AMOUNT,\n" );
//		sql.append("     0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
//		sql.append("    (NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_AMOUNT,COUNT(*) FREE_COUNT,0 SERVICE_COUNT, 0 SERVICE_LABOUR_AMOUNT,\n" );
//		sql.append("    0 SERVICE_PART_AMOUNT, 0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,\n" );
//		sql.append("    0 AFTER_CLAIM_COUNT,NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
//		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
//		sql.append("    ,"+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
//		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
//		sql.append("    WHERE 1=1\n" );
//		sql.append("    AND A.ID = B.CLAIM_ID\n" );
//		sql.append("    AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_02+"\n" );
//		sql.append("    AND B.BALANCE_ID = ?\n" );
//		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID\n" );
//		sql.append("    UNION ALL\n" );
//		//统计售前维修费用
//		sql.append("    SELECT B.BALANCE_ID,A.SERIES_ID,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) BEFORE_LABOUR_AMOUNT,\n" );
//		sql.append("    NVL(SUM(A.BALANCE_PART_AMOUNT),0) BEFORE_PART_AMOUNT,\n" );
//		sql.append("    NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
//		sql.append("    0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
//		sql.append("    0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,COUNT(*) BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
//		sql.append("    NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
//		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
//		sql.append("    ,0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
//		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
//		sql.append("    WHERE 1=1\n" );
//		sql.append("    AND A.ID = B.CLAIM_ID\n" );
//		sql.append("    AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_07+"\n" );
//		sql.append("    AND B.BALANCE_ID = ?\n" );
//		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID\n" );
//		sql.append("    UNION ALL\n" );
//		//统计服务活动维修费用
//		sql.append("    SELECT B.BALANCE_ID,A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
//		sql.append("    0 BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
//		sql.append("    0 FREE_AMOUNT,0 FREE_COUNT,COUNT(*) SERVICE_COUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
//		sql.append("    NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_OTHER_AMOUNT,\n" );
//		sql.append("    NVL(SUM(A.CAMPAIGN_FEE),0) SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
//		sql.append("    NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
//		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
//		sql.append("    ,0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
//		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
//		sql.append("    WHERE 1=1\n" );
//		sql.append("    AND A.ID = B.CLAIM_ID\n" );
//		sql.append("    AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
//		sql.append("    AND B.BALANCE_ID = ?\n" );
//		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID\n" );
//		sql.append("    UNION ALL\n" );
//		//统计除以上三中类型索赔的费用
//		sql.append("    SELECT B.BALANCE_ID,A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
//		sql.append("    0 BEFORE_OTHER_AMOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) AFTER_LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) AFTER_PART_AMOUNT,\n" );
//		sql.append("    NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) AFTER_OTHER_AMOUNT,\n" );
//		sql.append("    0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
//		sql.append("    0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,COUNT(*) AFTER_CLAIM_COUNT,\n" );
//		sql.append("    NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
//		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
//		sql.append("    ,0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
//		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
//		sql.append("    WHERE 1=1\n" );
//		sql.append("    AND A.ID = B.CLAIM_ID\n" );
//		sql.append("    AND A.CLAIM_TYPE  IN ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+")\n" );
//		sql.append("    AND B.BALANCE_ID = ?\n" );
//		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID)\n" );
//		sql.append("GROUP BY BALANCE_ID,SERIES_ID) B1\n" );
//		sql.append("WHERE B1.BALANCE_ID = A1.BALANCE_ID\n" );
//		sql.append("AND B1.SERIES_ID = A1.SERIES_ID)\n");
//		sql.append("WHERE 1=1\n" );
//		sql.append("AND A1.BALANCE_ID = ?");
//
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(balanceId);
//		paramList.add(balanceId);
//		paramList.add(balanceId);
//		paramList.add(balanceId);
//		paramList.add(balanceId);
//		
//		this.update(sql.toString(), paramList);
//		
		
		/********add by liuxh 20110510*********/
		sql.append("INSERT INTO TT_AS_WR_CLAIM_BALANCE_AMOUNT\n" );
		sql.append("SELECT BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,BEFORE_OTHER_AMOUNT,\n" );
		sql.append("     AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT,\n" );
		sql.append("     FREE_COUNT FREE_CLAIM_COUNT,FREE_AMOUNT FREE_CLAIM_AMOUNT,\n" );
		sql.append("    SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,\n" );
		sql.append("    BEFORE_CLAIM_COUNT, AFTER_CLAIM_COUNT,SERVICE_COUNT SERVICE_CLAIM_COUNT,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT, TOTAL_AMOUNT,\n" );
		sql.append("    SERIES_ID,"+balanceId+" AS BALANCE_ID \n");
		sql.append("    FROM(SELECT BALANCE_ID,SERIES_ID,SUM(BEFORE_LABOUR_AMOUNT) BEFORE_LABOUR_AMOUNT,SUM(BEFORE_PART_AMOUNT) BEFORE_PART_AMOUNT,\n" );
		sql.append("    SUM(BEFORE_OTHER_AMOUNT) BEFORE_OTHER_AMOUNT,SUM(AFTER_LABOUR_AMOUNT) AFTER_LABOUR_AMOUNT,SUM(AFTER_PART_AMOUNT) AFTER_PART_AMOUNT,\n" );
		sql.append("    SUM(AFTER_OTHER_AMOUNT) AFTER_OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(FREE_COUNT) FREE_COUNT,SUM(SERVICE_COUNT) SERVICE_COUNT,\n" );
		sql.append("    SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("    SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,SUM(BEFORE_CLAIM_COUNT) BEFORE_CLAIM_COUNT,SUM(AFTER_CLAIM_COUNT) AFTER_CLAIM_COUNT,\n" );
		sql.append("    SUM(TOTAL_AMOUNT) TOTAL_AMOUNT,SUM(TOTAL_COUNT) TOTAL_COUNT\n" );
		sql.append("   ,SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,(SUM(FREE_AMOUNT)-SUM(FREE_LABOUR_AMOUNT)) FREE_PART_AMOUNT\n" );
		sql.append("   ,(SUM(SERVICE_LABOUR_AMOUNT)+SUM(SERVICE_PART_AMOUNT)+SUM(SERVICE_OTHER_AMOUNT)+SUM(SERVICE_FIXED_AMOUNT)) SERVICE_TOTAL_AMOUNT\n" );
		
		//统计免费保养费用
		sql.append("    FROM(SELECT B.BALANCE_ID,A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT, 0 BEFORE_PART_AMOUNT,0 BEFORE_OTHER_AMOUNT,\n" );
		sql.append("     0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("    (NVL(SUM(A.FREE_M_PRICE),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) FREE_AMOUNT,COUNT(*) FREE_COUNT,0 SERVICE_COUNT, 0 SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("    0 SERVICE_PART_AMOUNT, 0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,\n" );
		sql.append("    0 AFTER_CLAIM_COUNT,NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append("    ,"+freeLabourAmount+"*(COUNT(*)) FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("    WHERE 1=1\n" );
		sql.append("    AND A.ID = B.CLAIM_ID\n" );
		sql.append("    AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_02+"\n" );
		sql.append("    AND B.BALANCE_ID = ?\n" );
		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID\n" );
		sql.append("    UNION ALL\n" );
		//统计售前维修费用
		sql.append("    SELECT B.BALANCE_ID,A.SERIES_ID,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) BEFORE_LABOUR_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_PART_AMOUNT),0) BEFORE_PART_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("    0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("    0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,COUNT(*) BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append("    ,0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("    WHERE 1=1\n" );
		sql.append("    AND A.ID = B.CLAIM_ID\n" );
		sql.append("    AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_07+"\n" );
		sql.append("    AND B.BALANCE_ID = ?\n" );
		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID\n" );
		sql.append("    UNION ALL\n" );
		//统计服务活动维修费用
		sql.append("    SELECT B.BALANCE_ID,A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("    0 BEFORE_OTHER_AMOUNT,0 AFTER_LABOUR_AMOUNT,0 AFTER_PART_AMOUNT,0 AFTER_OTHER_AMOUNT,\n" );
		sql.append("    0 FREE_AMOUNT,0 FREE_COUNT,COUNT(*) SERVICE_COUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) SERVICE_LABOUR_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_PART_AMOUNT),0) SERVICE_PART_AMOUNT,NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) SERVICE_OTHER_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.CAMPAIGN_FEE),0) SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,0 AFTER_CLAIM_COUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append("    ,0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("    WHERE 1=1\n" );
		sql.append("    AND A.ID = B.CLAIM_ID\n" );
		sql.append("    AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
		sql.append("    AND B.BALANCE_ID = ?\n" );
		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID\n" );
		sql.append("    UNION ALL\n" );
		//统计除以上三中类型索赔的费用
		sql.append("    SELECT B.BALANCE_ID,A.SERIES_ID,0 BEFORE_LABOUR_AMOUNT,0 BEFORE_PART_AMOUNT,\n" );
		sql.append("    0 BEFORE_OTHER_AMOUNT,(NVL(SUM(A.BALANCE_LABOUR_AMOUNT),0)+NVL(SUM(A.APPENDLABOUR_AMOUNT),0)) AFTER_LABOUR_AMOUNT,NVL(SUM(A.BALANCE_PART_AMOUNT),0) AFTER_PART_AMOUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_NETITEM_AMOUNT),0) AFTER_OTHER_AMOUNT,\n" );
		sql.append("    0 FREE_AMOUNT,0 FREE_COUNT,0 SERVICE_COUNT,0 SERVICE_LABOUR_AMOUNT,0 SERVICE_PART_AMOUNT,\n" );
		sql.append("    0 SERVICE_OTHER_AMOUNT,0 SERVICE_FIXED_AMOUNT,0 BEFORE_CLAIM_COUNT,COUNT(*) AFTER_CLAIM_COUNT,\n" );
		sql.append("    NVL(SUM(A.BALANCE_AMOUNT),0) TOTAL_AMOUNT,COUNT(*) TOTAL_COUNT\n" );
		sql.append("    ,NVL(SUM(A.APPENDLABOUR_AMOUNT),0) APPENDLABOUR_AMOUNT,NVL(SUM(A.APPEND_AMOUNT),0) APPEND_AMOUNT\n" );
		sql.append("    ,0 FREE_LABOUR_AMOUNT,0 FREE_PART_AMOUNT\n" );
		sql.append("    FROM TT_AS_WR_APPLICATION A,TR_BALANCE_CLAIM B\n" );
		sql.append("    WHERE 1=1\n" );
		sql.append("    AND A.ID = B.CLAIM_ID\n" );
		sql.append("    AND A.CLAIM_TYPE  IN ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+")\n" );
		sql.append("    AND B.BALANCE_ID = ?\n" );
		sql.append("    GROUP BY B.BALANCE_ID,A.SERIES_ID)\n" );
		sql.append("GROUP BY BALANCE_ID,SERIES_ID) B1\n" );
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		paramList.add(balanceId);
		this.update(sql.toString(), paramList);
		  
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_DETAIL A1 \n" );
		sbSql.append("   SET (A1.BEFORE_LABOUR_AMOUNT, A1.BEFORE_PART_AMOUNT, A1.BEFORE_OTHER_AMOUNT, \n");
		sbSql.append("   A1.AFTER_LABOUR_AMOUNT, A1.AFTER_PART_AMOUNT, A1.AFTER_OTHER_AMOUNT, \n");
		sbSql.append("   A1.FREE_CLAIM_COUNT, A1.FREE_CLAIM_AMOUNT, \n");
		sbSql.append("   A1.SERVICE_FIXED_AMOUNT, A1.SERVICE_LABOUR_AMOUNT, A1.SERVICE_PART_AMOUNT, A1.SERVICE_OTHER_AMOUNT, \n");
		sbSql.append("   A1.BEFORE_CLAIM_COUNT, A1.AFTER_CLAIM_COUNT, A1.SERVICE_CLAIM_COUNT,A1.FREE_LABOUR_AMOUNT,A1.FREE_PART_AMOUNT,A1.TOTAL_AMOUNT) = ( \n");
		sbSql.append("SELECT BEFORE_LABOUR_AMOUNT,BEFORE_PART_AMOUNT,BEFORE_OTHER_AMOUNT, \n");
		sbSql.append("     AFTER_LABOUR_AMOUNT,AFTER_PART_AMOUNT,AFTER_OTHER_AMOUNT, \n");
		sbSql.append("     FREE_CLAIM_COUNT,FREE_CLAIM_AMOUNT, \n");
		sbSql.append("    SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT, \n");
		sbSql.append("    BEFORE_CLAIM_COUNT, AFTER_CLAIM_COUNT,SERVICE_CLAIM_COUNT,FREE_LABOUR_AMOUNT,FREE_PART_AMOUNT, TOTAL_AMOUNT \n");
		sbSql.append("    FROM TT_AS_WR_CLAIM_BALANCE_AMOUNT TT \n");
		sbSql.append("WHERE TT.SERIES_ID = A1.SERIES_ID AND TT.BALANCE_ID=A1.BALANCE_ID) \n");
		sbSql.append("WHERE 1=1 \n");
		sbSql.append("AND A1.BALANCE_ID = ? \n");
		List<Object> paramList22 = new ArrayList<Object>();
		paramList22.add(balanceId);
		this.update(sbSql.toString(), paramList22);
		/***********add by liuxh 20110510*************/
		
		
	}
	
	/**
	 * 取得设定的保养索赔单中索赔工时费用占用比例
	 * @param paraId
	 * @return
	 */
	public Double queryFreeClaimFixedAmount(Integer paraId){
		
		Double result = 0.0;
		TmBusinessParaPO conditionPO = new TmBusinessParaPO();
		conditionPO.setParaId(paraId);
		
		List<TmBusinessParaPO> businessList = this.select(conditionPO);
		if(businessList!=null && businessList.size()>0){
			TmBusinessParaPO resultPO = businessList.get(0);
			try {
				result = Utility.getDouble(resultPO.getParaValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	/**
	 * 经销商查询结算汇总单信息
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> queryGatherBalanceOrder(auditBean bean,int curPage, int pageSize){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT r.region_name,A.*,TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATEDATE,\n" );
		sql.append("  B.DEALER_CODE,B.DEALER_SHORTNAME DEALER_NAME,to_char(B.BEGIN_BALANCE_DATE,'yyyy-MM-dd') as BEGIN_BALANCE_DATE,to_char(B.END_BALANCE_DATE,'yyyy-MM-dd') as END_BALANCE_DATE,\n" );
		sql.append("  to_char(d.START_DATE,'yyyy-MM-dd') as START_DATE,to_char(d.END_DATE,'yyyy-MM-dd') as END_DATE\n" );
		sql.append("  FROM TT_AS_WR_GATHER_BALANCE A,TM_DEALER B,TR_GATHER_BALANCE c,tt_as_wr_claim_balance d,tm_region r\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("   AND c.GATHER_ID = A.ID\n" );
		sql.append("   AND c.balance_ID = d.ID\n" );
		sql.append("   and r.region_code = b.province_id\n");
		sql.append("   AND A.OEM_COMPANY_ID = ?\n" );

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(bean.getOemCompanyId());
		if(Utility.testString(bean.getDealerId())){//经销商ID(经销商端使用)
			sql.append("   AND A.DEALER_ID = ?\n" );
			paramList.add(bean.getDealerId());
		}
		if(Utility.testString(bean.getDealerCode())){//经销商代码(车厂端使用)
			//用于同时查询多个经销商(不能模糊)sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), paramList, "B", "DEALER_CODE")).append("\n");
			sql.append("   AND B.DEALER_CODE LIKE '%"+bean.getDealerCode()+"%'\n" );//用于模糊查询一个经销商
		}
		if(Utility.testString(bean.getBalanceNo())){//结算汇总单号
			sql.append("   AND A.TOTAL_NO LIKE '%"+bean.getBalanceNo()+"%'\n" );
		}
		if(Utility.testString(bean.getStatus())){//结算汇总单号
			sql.append("   AND A.STATUS = ?\n" );
			paramList.add(bean.getStatus());
		}
		if(Utility.testString(bean.getYieldlys())){//结算产地
			sql.append("   AND A.YIELDLY IN("+bean.getYieldlys()+")\n" );
		}
		if(Utility.testString(bean.getYieldly())){//结算产地
			sql.append("   AND A.YIELDLY = ?\n" );
			paramList.add(bean.getYieldly());
		}
		if(Utility.testString(bean.getStartDate())){//结算汇总单创建开始日期
			sql.append("   AND A.CREATE_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			paramList.add(bean.getStartDate());
		}
		if(Utility.testString(bean.getEndDate())){//结算汇总单创建结束日期
			sql.append("   AND A.CREATE_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			paramList.add(bean.getEndDate());
		}
		if(Utility.testString(bean.getStartReportDate())){//结算汇总单上报开始日期
			sql.append("   AND A.REPORT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			paramList.add(bean.getStartReportDate());
		}
		if(Utility.testString(bean.getEndReportDate())){//结算汇总单上报结束日期
			sql.append("   AND A.REPORT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			paramList.add(bean.getEndReportDate());
		}
		
		sql.append(" ORDER BY A.ID DESC \n");
		
		return this.pageQuery(sql.toString(), paramList, this.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 查询该经销商可以生成结算汇总单的结算单
	 * 注：结算单状态为 "已完成"
	 * @return
	 */
	public PageResult<Map<String, Object>> queryBalanceOrderForGather(auditBean bean,int curPage, int pageSize){
	    StringBuffer sql= new StringBuffer();
	    /******add by liuxh 20101115 经销商可汇总自身 独立结算 和下级为上级结算的单子*******/
	    sql.append("WITH TT_DEALER_SET AS(\n" );
	    sql.append("SELECT A.DEALER_ID,A.PARENT_DEALER_D,A.DEALER_LEVEL\n" );
		sql.append("FROM TM_DEALER A\n" );
		sql.append("WHERE 1=1\n");
		sql.append("AND A.BALANCE_LEVEL=?\n");
		sql.append("AND A.DEALER_ID=?\n");
		sql.append("UNION\n");
	    
	    sql.append("SELECT A.DEALER_ID,A.PARENT_DEALER_D,A.DEALER_LEVEL\n" );
	    sql.append("FROM TM_DEALER A\n" );
	    sql.append("WHERE 1=1\n" );
	    sql.append("AND A.BALANCE_LEVEL=?\n");
	    sql.append("START WITH A.DEALER_ID = ?\n" );
	    sql.append("CONNECT BY PRIOR A.DEALER_ID = A.PARENT_DEALER_D)\n");
	    
	    sql.append("SELECT ROWNUM NUM, A.DEALER_ID,A.ID, A.BALANCE_NO, A.DEALER_CODE, A.DEALER_NAME, A.CLAIM_COUNT,\n" );
	    sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS, A.YIELDLY,A.BALANCE_AMOUNT,A.AMOUNT_SUM\n" );
	    sql.append("       ,A.DEALER_LEVEL,TO_CHAR(A.START_DATE, 'YYYY-MM-DD') START_DATE, TO_CHAR(A.END_DATE, 'YYYY-MM-DD') END_DATE\n" );
	    sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TT_DEALER_SET B\n");
	    sql.append("WHERE 1=1\n");
	    sql.append("AND A.OEM_COMPANY_ID = ?\n");
	    sql.append("AND A.DEALER_ID = B.DEALER_ID\n");
	    sql.append("AND A.YIELDLY = ?\n");
	    sql.append("AND A.STATUS = "+Constant.ACC_STATUS_09+"\n");
	    
	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(Constant.BALANCE_LEVEL_SELF);
	    paramList.add(bean.getDealerId());
	    paramList.add(Constant.BALANCE_LEVEL_HIGH);
	    paramList.add(bean.getDealerId());
	    paramList.add(bean.getOemCompanyId());
	    paramList.add(CommonUtils.checkNull(bean.getYieldly()));
		return this.pageQuery(sql.toString(), paramList, this.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 根据汇总单通结算单关系，生成汇总单
	 */
	public void createGatherBalanceOrder(auditBean bean,Long userId){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_GATHER_BALANCE(ID,TOTAL_NO,YIELDLY,DEALER_ID,STATUS,LABOUR_AMOUNT,PART_AMOUNT,\n" );
		sql.append("OTHER_AMOUNT,FREE_AMOUNT,SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,\n" );
		sql.append("RETURN_AMOUNT,AMOUNT_SUM,CLAIM_COUNT,APPEND_LABOUR_AMOUNT,APPEND_AMOUNT,FREE_LABOUR_AMOUNT,\n" );
		sql.append("FREE_PART_AMOUNT,SERVICE_TOTAL_AMOUNT,BALANCE_COUNT,BALANCE_AMOUNT,OEM_COMPANY_ID,CREATE_BY,CREATE_DATE)\n" );
		sql.append("SELECT '"+bean.getId()+"' ID,'"+bean.getBalanceNo()+"' TOTAL_NO,'"+bean.getYieldly()+"' YIELDLY,'");
		sql.append(bean.getDealerId()+"' DEALER_ID,'"+bean.getStatus()+"' STATUS,LABOUR_AMOUNT,PART_AMOUNT,\n" );
		sql.append("OTHER_AMOUNT,FREE_AMOUNT,SERVICE_FIXED_AMOUNT,SERVICE_LABOUR_AMOUNT,SERVICE_PART_AMOUNT,SERVICE_OTHER_AMOUNT,\n" );
		sql.append("RETURN_AMOUNT,AMOUNT_SUM,CLAIM_COUNT,APPEND_LABOUR_AMOUNT,APPEND_AMOUNT,FREE_LABOUR_AMOUNT,\n" );
		sql.append("FREE_PART_AMOUNT,SERVICE_TOTAL_AMOUNT,BALANCE_COUNT,BALANCE_AMOUNT,\n" );
		sql.append("'"+bean.getOemCompanyId()+"' OEM_COMPANY_ID,'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE\n" );
		sql.append("FROM(\n" );
		sql.append("SELECT SUM(LABOUR_AMOUNT) LABOUR_AMOUNT,SUM(PART_AMOUNT) PART_AMOUNT,\n" );
		sql.append("SUM(OTHER_AMOUNT) OTHER_AMOUNT,SUM(FREE_AMOUNT) FREE_AMOUNT,SUM(SERVICE_FIXED_AMOUNT) SERVICE_FIXED_AMOUNT,\n" );
		sql.append("SUM(SERVICE_LABOUR_AMOUNT) SERVICE_LABOUR_AMOUNT,SUM(SERVICE_PART_AMOUNT) SERVICE_PART_AMOUNT,\n" );
		sql.append("SUM(SERVICE_OTHER_AMOUNT) SERVICE_OTHER_AMOUNT,SUM(RETURN_AMOUNT) RETURN_AMOUNT,SUM(APPLY_AMOUNT) AMOUNT_SUM,\n" );
		sql.append("SUM(CLAIM_COUNT) CLAIM_COUNT,SUM(APPEND_LABOUR_AMOUNT) APPEND_LABOUR_AMOUNT,SUM(APPEND_AMOUNT) APPEND_AMOUNT,\n" );
		sql.append("SUM(FREE_LABOUR_AMOUNT) FREE_LABOUR_AMOUNT,SUM(FREE_PART_AMOUNT) FREE_PART_AMOUNT,\n" );
		sql.append("SUM(SERVICE_TOTAL_AMOUNT) SERVICE_TOTAL_AMOUNT,COUNT(*) BALANCE_COUNT,SUM(BALANCE_AMOUNT) BALANCE_AMOUNT\n" );
		sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TR_GATHER_BALANCE B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = B.BALANCE_ID\n" );
		sql.append("AND B.GATHER_ID = "+bean.getId()+")");
		
		this.update(sql.toString(), null);
	}
	
	/**
	 * 查询该经销商可以生成结算汇总单的结算单
	 * 注：结算单状态为 "已完成"
	 * @return
	 */
	public PageResult<Map<String, Object>> queryGatherBalanceOrderDetail(Long gatherId,int curPage, int pageSize){
	    StringBuffer sql= new StringBuffer();

	    sql.append("SELECT ROWNUM NUM,r.region_name, A.DEALER_ID,A.ID, A.BALANCE_NO, A.DEALER_CODE, A.DEALER_NAME, A.CLAIM_COUNT,B.REMARK,\n" );
	    sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS, A.YIELDLY,A.BALANCE_AMOUNT,A.APPLY_AMOUNT,A.AMOUNT_SUM\n" );
	    sql.append("       ,A.DEALER_LEVEL,TO_CHAR(A.START_DATE, 'YYYY-MM-DD') START_DATE, TO_CHAR(A.END_DATE, 'YYYY-MM-DD') END_DATE\n" );
	    sql.append("FROM TT_AS_WR_CLAIM_BALANCE A,TR_GATHER_BALANCE B,tm_region r,tm_dealer d\n");
	    sql.append("WHERE 1=1\n");
	    sql.append("AND A.ID = B.BALANCE_ID\n");
	    sql.append("and a.dealer_id = d.dealer_id\n");
	    sql.append("and d.province_id = r.region_code\n");
	    sql.append("AND B.GATHER_ID = ?\n");
	    
	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(CommonUtils.checkNull(gatherId));
		return this.pageQuery(sql.toString(), paramList, this.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 根据结算汇总单ID和经销商级别，通过汇总单通结算单关系表，更加对应结算单状态
	 * 注：经销商级别为该级别
	 * @param gatherId 结算汇总单ID
	 * @param status 需要修改到状态
	 * @param dealerLevel 经销商级别=dealerLevel
	 */
	public void modifyBalanceOrderByGatherId(Long gatherId,Integer status,Integer dealerLevel){
		
		if(gatherId==null || status==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE A\n" );
		sql.append("SET A.STATUS = ?\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID IN (SELECT B.BALANCE_ID FROM TR_GATHER_BALANCE B\n" );
		sql.append("WHERE B.GATHER_ID = ?)\n");
		sql.append("AND A.DEALER_LEVEL = ?");

		List<Object> param = new ArrayList<Object>();
		param.add(status);
		param.add(gatherId);
		param.add(dealerLevel);
		this.update(sql.toString(), param);
	}
	
	/**
	 * 根据结算汇总单ID和经销商级别，通过汇总单通结算单关系表，更加对应结算单状态
	 * 注：经销商级别不是该级别
	 * @param gatherId 结算汇总单ID
	 * @param status 需要修改到状态
	 * @param dealerLevel 经销商级别<>dealerLevel
	 */
	public void modifyBalanceOrderByGatherId2(Long gatherId,Integer status,Integer dealerLevel){
		
		if(gatherId==null || status==null)
			return;
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE A\n" );
		sql.append("SET A.STATUS = ?\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID IN (SELECT B.BALANCE_ID FROM TR_GATHER_BALANCE B\n" );
		sql.append("WHERE B.GATHER_ID = ?)\n");
		sql.append("AND (A.DEALER_LEVEL >?");
		sql.append("OR A.DEALER_LEVEL <?)");

		List<Object> param = new ArrayList<Object>();
		param.add(status);
		param.add(gatherId);
		param.add(dealerLevel);
		param.add(dealerLevel);
		this.update(sql.toString(), param);
	}
	
	/**
	 * 标识特殊费用到对应结算单中
	 * 注：现在只标识 市场工单费用 和 特殊外出费用(通过FEE_TYPE)
	 *     特殊费用状态为 "已提报" 的
	 *     其他未被其他索赔单标识的 
	 * @param dealerId 经销商ID
	 * @param balanceId 结算单ID
	 */
	public void markSpecialFeeToBalanceOrder(Long dealerId,Long balanceId,String startTime,String endTime,String yieldly){
		/**********mod by liuxh 20101116 修改特殊费用根据时间类型进行修改**************/
		//////市场公单费用
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_SPEFEE A\n" );
		sql.append("SET A.CLAIMBALANCE_ID = ?\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("and a.yield="+yieldly+"\n");
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE = "+Constant.FEE_TYPE_01+"\n" );
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_02+"\n");
		sql.append("AND A.DEALER_ID = ? \n");
		sql.append("AND A.MAKE_DATE>=TO_DATE('"+startTime+"','YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.MAKE_DATE<=TO_DATE('"+endTime+"','YYYY-MM-DD HH24:MI:SS')\n");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(dealerId);
		this.update(sql.toString(), paramList);
	    //////市场公单费用
		
		//////市场公单费用
		
		StringBuilder sqlSp= new StringBuilder();
		sqlSp.append("UPDATE TT_AS_WR_SPEFEE A\n" );
		sqlSp.append("SET A.CLAIMBALANCE_ID = ?\n" );
		sqlSp.append("WHERE 1=1\n" );
		sqlSp.append("and a.yield="+yieldly+"\n");
		sqlSp.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sqlSp.append("AND A.FEE_TYPE = "+Constant.FEE_TYPE_02+"\n" );
		sqlSp.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_04+"\n");
		sqlSp.append("AND A.DEALER_ID = ? \n");
		sqlSp.append("AND EXISTS (SELECT B.STATUS FROM TT_AS_WR_SPEFEE_AUDITING B\n");
		sqlSp.append("WHERE B.FEE_ID=A.ID AND B.STATUS="+Constant.SPEFEE_STATUS_04+"\n ");
		sqlSp.append("AND B.AUDITING_DATE>=TO_DATE('"+startTime+"','YYYY-MM-DD HH24:MI:SS')\n ");
		sqlSp.append("AND B.AUDITING_DATE<=TO_DATE('"+endTime+"','YYYY-MM-DD HH24:MI:SS')\n ");
		sqlSp.append(")");
		
		List<Object> paramListSp = new ArrayList<Object>();
		paramListSp.add(balanceId);
		paramListSp.add(dealerId);
		this.update(sqlSp.toString(), paramListSp);
	    //////市场公单费用
		
		/**********mod by liuxh 20101116 修改特殊费用根据时间类型进行修改**************/
		
	}
	/*******add by liuxh 20101211 增加特殊费用列表显示*******/
	/**
	 * 查询特殊费用列表
	 */
	public List getSpecialFeeToBalanceOrder(Long dealerId,String startTime,String endTime,String yieldly){
		//////市场公单费用
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(A.DECLARE_SUM),0) AS DECLARE_SUM,A.YIELD,COUNT(*) AS COUNT,A.FEE_TYPE FROM TT_AS_WR_SPEFEE A\n" );
		sql.append("WHERE A.YIELD=? \n");
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE = ? \n" );
		sql.append("AND A.STATUS = ? \n");
		sql.append("AND A.DEALER_ID = ? \n");
		sql.append("AND A.Balance_Audit_Date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.Balance_Audit_Date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("GROUP BY A.YIELD,A.FEE_TYPE \n");
		
		sql.append("UNION ALL \n");
		
		sql.append("SELECT NVL(SUM(A.DECLARE_SUM),0) AS DECLARE_SUM ,A.YIELD,COUNT(*) AS COUNT,A.FEE_TYPE FROM TT_AS_WR_SPEFEE A\n" );
		sql.append("WHERE A.YIELD=? \n");
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE = ? \n" );
		sql.append("AND A.STATUS = ? \n");
		sql.append("AND A.DEALER_ID = ? \n");
		sql.append("AND EXISTS (SELECT B.STATUS FROM TT_AS_WR_SPEFEE_AUDITING B\n");
		sql.append("WHERE B.FEE_ID=A.ID AND B.STATUS=? \n ");
		sql.append("AND B.AUDITING_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append("AND B.AUDITING_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append(")\n");
		sql.append("GROUP BY A.YIELD,A.FEE_TYPE ");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(Long.valueOf(yieldly));
		paramList.add(Constant.FEE_TYPE_01);
		paramList.add(Constant.SPEFEE_STATUS_02);
		paramList.add(dealerId);
		paramList.add(startTime);
		paramList.add(endTime);
		
		paramList.add(Long.valueOf(yieldly));
		paramList.add(Constant.FEE_TYPE_02);
		paramList.add(Constant.SPEFEE_STATUS_04);
		paramList.add(Long.valueOf(dealerId));
		paramList.add(Constant.SPEFEE_STATUS_04);
		paramList.add(startTime);
		paramList.add(endTime);
		List list=this.pageQuery(sql.toString(), paramList, this.getFunName());

		return list;
	}
	/*******add by liuxh 20101211 增加特殊费用列表显示*******/
	/**
	 * 统计指定结算单标识的特殊费用
	 * @param balanceId 结算单ID
	 * @return List<Map<String,Object>> 
	 *         KEY : (FEE_TYPE:费用类型  DECLARE_SUM:审核申报金额  DECLARE_SUM1:申请申报金额  FEETYPECOUNT:对应类型工单数)
	 */
	public List<Map<String,Object>> getSpecialFeeByBalanceId(Long balanceId){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT FEE_TYPE,NVL(SUM(DECLARE_SUM),0) DECLARE_SUM,NVL(SUM(DECLARE_SUM1),0) DECLARE_SUM1\n" );
		sql.append(",COUNT(*) FEETYPECOUNT\n" );
		sql.append("FROM TT_AS_WR_SPEFEE\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND CLAIMBALANCE_ID = ?\n" );
		sql.append("GROUP BY FEE_TYPE");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		List<Map<String,Object>> resultList = this.pageQuery(sql.toString(), paramList, this.getFunName());
		return resultList;
	}
	
	/**
	 * 统计指定结算单标识的特殊费用
	 * @param balanceId 结算单ID
	 * @return List<Map<String,Object>> 
	 *         KEY : (FEE_TYPE:费用类型  DECLARE_SUM:审核申报金额  DECLARE_SUM1:申请申报金额  FEETYPECOUNT:对应类型工单数)
	 */
	public List<Map<String,Object>> getSpecialFeeByBalanceIdStatus(Long balanceId){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT FEE_TYPE,NVL(SUM(DECLARE_SUM),0) DECLARE_SUM,NVL(SUM(DECLARE_SUM1),0) DECLARE_SUM1\n" );
		sql.append(",COUNT(*) FEETYPECOUNT\n" );
		sql.append("FROM TT_AS_WR_SPEFEE\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND CLAIMBALANCE_ID = ? AND STATUS=?\n" );
		sql.append("GROUP BY FEE_TYPE");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		paramList.add(Constant.SPEFEE_STATUS_06);
		List<Map<String,Object>> resultList = this.pageQuery(sql.toString(), paramList, this.getFunName());
		return resultList;
	}
	/**
	 * 将特殊费用统计对应结算单中，同时标识对应特殊费用合计到该结算单
	 * @param balanceId 结算单ID
	 * @param marketFee 市场工单特殊费用
	 * @param outFee 外出特殊费用
	 * @param feeCount 特殊费用工单数
	 */
	public void addSpecialFeeToBalanceOrder(Long balanceId,Double marketFee,Double outFee,Integer feeCount){
		
		if(marketFee==null)
			marketFee = 0d;
		if(outFee==null)
			outFee = 0d;
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_CLAIM_BALANCE\n" );
		sql.append("SET MARKET_AMOUNT = "+marketFee+",SPEOUTFEE_AMOUNT = "+outFee+",\n" );
		sql.append("BALANCE_AMOUNT = BALANCE_AMOUNT + ("+marketFee+"+"+outFee+"),\n" );
		sql.append("SPEC_FEE_COUNT = "+feeCount+"\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND ID = "+balanceId);
		
		this.update(sql.toString(), null);
	}
	/*****add by xiongchuan 20110221 判断汇总单下是否还有未自动审核完的结算单*****/
	public long getBalCount(Long gatherId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("select count(1) as COUNT from Tt_As_Wr_Claim_Balance c,tr_gather_balance g where c.status="+Constant.ACC_STATUS_09+" \n");
		sbSql.append(" and g.balance_id = c.id  and g.gather_id="+gatherId+" \n");
		List list=this.pageQuery(sbSql.toString(), null, this.getFunName());
		Map map=(Map)list.get(0);
		Long count=((BigDecimal)map.get("COUNT")).longValue();
		return count;
	}
	
	public void updateBalanceStatus(Long gatherId){
		StringBuffer sql=new StringBuffer();
		sql.append("  update Tt_As_Wr_Claim_Balance c set c.status="+Constant.ACC_STATUS_06+" where  \n");
		sql.append("  c.id in (select gb.balance_id from tr_gather_balance gb where gb.gather_id="+gatherId+") \n");
		this.update(sql.toString(), null);
	}
	/**
	 * 标识特殊费用到对应结算单中
	 * 注：现在只标识 市场工单费用 和 特殊外出费用(通过FEE_TYPE)
	 *     特殊费用状态为 "已提报" 的
	 *     其他未被其他索赔单标识的 
	 * @param dealerId 经销商ID
	 * 
	 * 
	 * 市场公单费用
	 */
	/******add by liuxh 20101116 修改市场公单费用*********/
	public Map<String, Object> queryMarketFeeToBalanceOrder(Long dealerId,String startTime,String endTime){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_01+",DECLARE_SUM,0)),0) MARKETFEE,NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_02+",DECLARE_SUM,0)),0) OUTFEE\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE ="+Constant.FEE_TYPE_01+"\n" );
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_02+"\n");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.MAKE_DATE>=TO_DATE('"+startTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.MAKE_DATE<=TO_DATE('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);
		
		return this.pageQueryMap(sql.toString(), paramList, this.getFunName());
	}
	public Map<String, Object> queryMarketFeeToBalanceOrder2(Long dealerId,String startTime,String endTime,String yieldly){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_01+",DECLARE_SUM,0)),0) MARKETFEE,NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_02+",DECLARE_SUM,0)),0) OUTFEE\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE ="+Constant.FEE_TYPE_01+"\n" );
		sql.append("and a.yield="+yieldly+"\n");
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_02+"\n");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND A.MAKE_DATE>=TO_DATE('"+startTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		sql.append("AND A.MAKE_DATE<=TO_DATE('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);
		
		return this.pageQueryMap(sql.toString(), paramList, this.getFunName());
	}
	/******add by liuxh 20101116 修改市场公单费用*********/
	
	/**
	 * 标识特殊费用到对应结算单中
	 * 注：现在只标识 市场工单费用 和 特殊外出费用(通过FEE_TYPE)
	 *     特殊费用状态为 "已提报" 的
	 *     其他未被其他索赔单标识的 
	 * @param dealerId 经销商ID
	 * 
	 * 
	 * 特殊公单费用
	 */
	/******add by liuxh 20101116 修改特殊公单费用*********/
	public Map<String, Object> querySpecialFeeToBalanceOrder(Long dealerId,String startTime,String endTime){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_01+",DECLARE_SUM,0)),0) MARKETFEE,NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_02+",DECLARE_SUM,0)),0) OUTFEE\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("AND A.FEE_TYPE ="+Constant.FEE_TYPE_02+"\n" );
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_04+"\n");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND EXISTS\n");
		sql.append("(SELECT B.STATUS FROM TT_AS_WR_SPEFEE_AUDITING B\n");
		sql.append("WHERE B.FEE_ID=A.ID AND B.STATUS="+Constant.SPEFEE_STATUS_04+"\n ");
		sql.append("AND B.AUDITING_DATE>=TO_DATE('"+startTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append("AND B.AUDITING_DATE<=TO_DATE('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append(")");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);
		
		return this.pageQueryMap(sql.toString(), paramList, this.getFunName());
	}
	public Map<String, Object> querySpecialFeeToBalanceOrder2(Long dealerId,String startTime,String endTime,String yieldly){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_01+",DECLARE_SUM,0)),0) MARKETFEE,NVL(SUM(DECODE(FEE_TYPE,"+Constant.FEE_TYPE_02+",DECLARE_SUM,0)),0) OUTFEE\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.CLAIMBALANCE_ID IS NULL\n" );
		sql.append("and a.yield="+yieldly+"\n");
		sql.append("AND A.FEE_TYPE ="+Constant.FEE_TYPE_02+"\n" );
		sql.append("AND A.STATUS = "+Constant.SPEFEE_STATUS_04+"\n");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND EXISTS\n");
		sql.append("(SELECT B.STATUS FROM TT_AS_WR_SPEFEE_AUDITING B\n");
		sql.append("WHERE B.FEE_ID=A.ID AND B.STATUS="+Constant.SPEFEE_STATUS_04+"\n ");
		sql.append("AND B.AUDITING_DATE>=TO_DATE('"+startTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append("AND B.AUDITING_DATE<=TO_DATE('"+endTime+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n ");
		sql.append(")");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);
		
		return this.pageQueryMap(sql.toString(), paramList, this.getFunName());
	}
	/******add by liuxh 20101116 修改特殊公单费用*********/
	
	/**
	 * 删除结算单通特殊费用关系,同时将特殊费用工单状态修改为"已提报"
	 * 原因：结算单只统计"已提报"的特殊费用，防止该笔费用审核过后，不能再次使用。
	 */
	public void deleteRelationOfSpecFeeAndBalance(Long balanceId){
		/********mod by liuxh 20101127 删除结算单时根据不同类型更新状态不同   市场工单 更新为 已提报 特殊外出更新为 大区审核通过********/
//		StringBuilder sql= new StringBuilder();
//		sql.append("UPDATE TT_AS_WR_SPEFEE\n" );
//		sql.append("   SET CLAIMBALANCE_ID = NULL,\n" );
//		sql.append("   STATUS = "+Constant.SPEFEE_STATUS_02+"\n" );
//		sql.append(" WHERE CLAIMBALANCE_ID = ?");
//		
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(balanceId);
//		
//		this.update(sql.toString(), paramList);
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_SPEFEE\n" );
		sql.append("   SET CLAIMBALANCE_ID = NULL,\n" );
		sql.append("   STATUS = ?\n" );
		sql.append(" WHERE FEE_TYPE=? AND CLAIMBALANCE_ID = ?");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(Constant.SPEFEE_STATUS_02);
		paramList.add(Constant.FEE_TYPE_01);
		paramList.add(balanceId);
		this.update(sql.toString(), paramList);
		
		List<Object> paramList2 = new ArrayList<Object>();
		paramList2.add(Constant.SPEFEE_STATUS_04);
		paramList2.add(Constant.FEE_TYPE_02);
		paramList2.add(balanceId);
		this.update(sql.toString(), paramList2);
		
		/********mod by liuxh 20101127 删除结算单时根据不同类型更新状态不同   市场工单 更新为 已提报 特殊外出更新为 大区审核通过********/
	}
	
	/**
	 * 查询结算汇总单明细
	 * @param gatherId 结算汇总单ID
	 * @return
	 */
	public List<Map<String,Object>> printBalanceGatherOrderDetail(String gatherId){
		if(gatherId==null)
			return null;
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.GATHER_ID,TO_CHAR(B.START_DATE,'YYYY-MM-DD') F_START_DATE,\n" );
		sql.append("TO_CHAR(B.END_DATE,'YYYY-MM-DD') F_END_DATE,B.*\n" );
		sql.append("FROM TR_GATHER_BALANCE A,TT_AS_WR_CLAIM_BALANCE B\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.BALANCE_ID = B.ID\n" );
		sql.append("AND A.GATHER_ID = ?");

	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(gatherId);
	    return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}
	/*****
	 * add by liuxh 20101227 增加特殊费用 市场公单 和活 动公单字段。审核完成动作更新字段
	 */
	public int updateMarkSpeeActiveFee(long balanceId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("UPDATE   TT_AS_WR_CLAIM_BALANCE K \n" );
		sbSql.append("   SET   K.MARKET_MARKET_AMOUNT = \n");
		sbSql.append("            (SELECT   NVL(SUM (NVL (L.DECLARE_SUM, 0)),0) \n");
		sbSql.append("               FROM   TT_AS_WR_SPEFEE L \n");
		sbSql.append("              WHERE       L.FEE_TYPE = ? \n");         //市场公单费用
		sbSql.append("                      AND L.FEE_CHANNEL IN (?,?) \n");   //市场公单
		sbSql.append("                      AND L.STATUS = ? \n");            //审核通过
		sbSql.append("                      AND K.ID = L.CLAIMBALANCE_ID), \n");
		sbSql.append("         K.MARKET_ACTIVITY_AMOUNT = \n");
		sbSql.append("            (SELECT   NVL(SUM (NVL (L.DECLARE_SUM, 0)),0) \n");
		sbSql.append("               FROM   TT_AS_WR_SPEFEE L \n");
		sbSql.append("              WHERE       L.FEE_TYPE = ? \n");        //市场公单费用
		sbSql.append("                      AND L.FEE_CHANNEL = ? \n");       //市场活动公单费用
		sbSql.append("                      AND L.STATUS = ? \n");            //审核通过
		sbSql.append("                      AND K.ID = L.CLAIMBALANCE_ID) \n");
		sbSql.append("   WHERE K.ID=? ");
		List listPar=new ArrayList();
		listPar.add(Constant.FEE_TYPE_01);
		listPar.add(Constant.FEE_TYPE1_01);
		listPar.add(Constant.FEE_TYPE1_03);
		listPar.add(Constant.SPEFEE_STATUS_06);
		
		listPar.add(Constant.FEE_TYPE_01);
		listPar.add(Constant.FEE_TYPE1_02);
		listPar.add(Constant.SPEFEE_STATUS_06);
		
		listPar.add(balanceId);
		return this.update(sbSql.toString(), listPar);
	}
	
	/*********Iverson add By 2010-11-18 通过索赔汇总单ID查询结算开始和结束时间以及哪个经销商结算的*************************/
	public List<Map<String, Object>> selectTime(String orderId) {
		String sql="select c.start_date,c.end_date ,c.dealer_id from Tt_As_Wr_Claim_Balance c where c.id in(select b.balance_id from tr_gather_balance b where b.gather_id='"+orderId+"')";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-11-22 
	 * 取出最大的时间
	 * @param dealerId
	 * @return
	 */
	public String getMaxDate(String dealerId,long yieldly){
		String startTime="";
		String endTime="";
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select a.end_date from tt_as_wr_claim_balance a where a.dealer_id =? and a.yieldly=? order by a.end_date desc");
		List parList=new ArrayList();
		parList.add(Long.parseLong(dealerId));
		parList.add(yieldly);
		List<Map<String,Object>> rsList= pageQuery(sqlStr.toString(), parList, getFunName());
		if(rsList.size()>0){
			endTime = rsList.get(0).get("END_DATE").toString();
		}else{
			//取得原始旧件起止时间
			TmBusinessParaPO po = new TmBusinessParaPO();
			po.setTypeCode(Constant.TYPE_CODE);
			TmBusinessParaPO poValue = (TmBusinessParaPO)this.select(po).get(0);
			startTime = poValue.getParaValue().substring(0, 10);
			endTime = poValue.getParaValue().substring(11,poValue.getParaValue().length());
		}
		return endTime;
	}
	/*********Iverson add By 2010-11-18 通过索赔汇总单ID查询结算开始和结束时间以及哪个经销商结算的*************************/
	/*********Iverson add By 2010-11-18 通过索赔汇总单ID查询结算开始和结束时间以及哪个经销商结算的*************************/
	
	//zhumingwei 2011-11-18
	public String getMaxDate1(String dealerId,long yieldly){
		String startTime="";
		String endTime="";
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select a.end_date from tt_as_wr_claim_balance a where a.dealer_id =? and a.yieldly=? order by a.end_date desc");
		List parList=new ArrayList();
		parList.add(Long.parseLong(dealerId));
		parList.add(yieldly);
		List<Map<String,Object>> rsList= pageQuery(sqlStr.toString(), parList, getFunName());
		if(rsList.size()>0){
			endTime = rsList.get(0).get("END_DATE").toString();
		}else{
			//取得原始时间
			endTime = Constant.FRIST_TIME;
		}
		return endTime;
	}
	
	/*********add by liuxh 20101126 判断结算单下的索赔单和特殊费用单是否已全部审核完成**********/
	/**
	 * 索赔单是否全部审核完成
	 */
	public boolean queryClaimByBanlanceId(long banlanceId) {
		boolean flag=true;
		String sql="select a.status from TT_AS_WR_APPLICATION a,TR_BALANCE_CLAIM b where a.id=b.balance_id and b.balance_id=?";
		List parList=new ArrayList();
		parList.add(banlanceId);
		List<Map> list= pageQuery(sql, parList, this.getFunName());
		for(Map map:list){
			String status=((BigDecimal)map.get("STATUS")).toString();
			if(!status.equals(Constant.CLAIM_APPLY_ORD_TYPE_07)){
				flag=false;
				break;
			}
		}
		return flag;
	}
	/**
	 * 特殊费用全完审核判断
	 * @param banlanceId
	 * @return
	 */
	public boolean querySpecByBanlanceId(long banlanceId) {
		boolean flag=true;
		String sql="SELECT D.STATUS FROM TT_AS_WR_SPEFEE D WHERE D.CLAIMBALANCE_ID=?";// AND D.STATUS="+Constant.SPEFEE_STATUS_06+")";
		List parList=new ArrayList();
		parList.add(banlanceId);
		List<Map> list= pageQuery(sql, parList, this.getFunName());
		for(Map map:list){
			String status=((BigDecimal)map.get("STATUS")).toString();
			if(!status.equals(Constant.SPEFEE_STATUS_06)&&!status.equals(Constant.SPEFEE_STATUS_05)){
				flag=false;
				break;
			}
		}
		return flag;
	}
	/*********add by liuxh 20101126 判断结算单下的索赔单和特殊费用单是否已全部审核完成**********/
	
	/*********Iverson add 2010-12-01 根据经销商ID和基地查询吃此经销商是否被停止结算*********/
	public String getDealerStatus(long dealerId,long yieldly) throws Exception{
		String sql="SELECT STATUS FROM TT_AS_DEALER_TYPE WHERE DEALER_ID=? AND YIELDLY=?";
		List parList=new ArrayList();
		parList.add(Long.valueOf(dealerId));
		parList.add(Long.valueOf(yieldly));
		List list=this.pageQuery(sql, parList, this.getFunName());
		return (String)((Map)list.get(0)).get("STATUS");
	}
	/*********Iverson add 2010-12-01 根据经销商ID和基地查询吃此经销商是否被停止结算*********/
	
	/**
	 * Iverson add By 2010-12-21
	 * 结算单扣款明细查询
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> queryClaimBalanceOrder(String dealerId ,String balanceNo,String startDate,String endDate,String dealerCode,String dealerName,String yieldlys,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select a.id,b.dealer_code,b.dealer_name,c.AREA_NAME," +
				"to_char(a.start_date,'yyyy-MM-dd') as start_date,to_char(a.end_date,'yyyy-MM-dd')" +
				" as end_date,nvl(a.balance_no,0) as balance_no,nvl(a.apply_amount,0) " +
				"as apply_amount,nvl(a.balance_amount,0) as balance_amount,(nvl(a.apply_amount,0)-nvl(a.balance_amount,0)) as kk\n" );
		sql.append("from tt_as_wr_claim_balance a,tm_dealer " +
				"b,Tm_Business_Area c  where a.dealer_id=b.dealer_id and c.AREA_ID= a.yieldly and a.status IN ("+Constant.ACC_STATUS_04+","+Constant.ACC_STATUS_05+")\n" );
		if(Utility.testString(dealerCode)){
			sql.append("   and b.dealer_code like '%"+dealerCode+"%'\n" );
		}
		if(Utility.testString(dealerName)){
			sql.append("   and b.dealer_name like '%"+dealerName+"%'\n" );
		}
		if(Utility.testString(dealerId)){
			sql.append("   and a.dealer_id='"+dealerId+"'\n" );
		}
		if(Utility.testString(yieldlys)){
			sql.append("   and a.yieldly in ("+yieldlys+")\n" );
		}
		if(Utility.testString(balanceNo)){
			sql.append("   AND a.balance_no LIKE '%"+balanceNo+"%'\n" );//用于模糊查询
		}
		if(Utility.testString(startDate)){
			sql.append("   AND a.start_date>=TO_DATE('"+startDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endDate)){
			sql.append("   AND a.end_date<=TO_DATE('"+endDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" ORDER BY A.ID DESC \n");
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单号查询索赔单明细
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> detailByBalanceId(String balanceId) {
		String sql="select b.id,b.claim_no, nvl(b.repair_total,0) as repair_total,nvl(b.balance_amount,0) as balance_amount,(nvl(b.repair_total,0)-nvl(b.balance_amount,0))as sub_amount from tt_as_wr_application b where b.id in(select d.claim_id from tr_balance_claim d where d.balance_id='"+balanceId+"') order by sub_amount desc";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-12-23
	 * 根据结算单ID查找结算单信息
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> balanceInfoByBalanceId(String balanceId) {
		//String sql="select * from tt_as_wr_claim_balance a where a.id ='"+balanceId+"'";
		String sql="select a.*,t.dealer_code,t.dealer_name,b.AREA_NAME from tt_as_wr_claim_balance a,TM_DEALER t,Tm_Business_Area b where b.AREA_ID = a.YIELDLY and  a.dealer_id = t.dealer_id and a.id ='"+balanceId+"'";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单号查询特殊费用明细
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> spefeeInfoByBalanceId(String balanceId) {
		String sql="select c.id,c.fee_no,c.FEE_TYPE, nvl(c.declare_sum1,0) as declare_sum1,nvl(declare_sum,0) as declare_sum,(nvl(declare_sum1,0)-nvl(declare_sum,0))as fee_amount from tt_as_wr_spefee c where c.claimbalance_id ='"+balanceId+"' order by fee_amount desc";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**********
	 * add xiongchuan 2011-03-10
	 * 将审核人插入到索赔单的状态记录表中
	 * 
	 */
	
	
	public void updateStatusApplication(long balanceId,String name){
		
		StringBuffer sql= new StringBuffer();
		sql.append("update Tt_As_Wr_Appauthitem a set a.approval_person='"+name+"' where a.id in (\n" );
		sql.append("select b.claim_id from tr_balance_claim b ,Tt_As_Wr_Application wa where b.balance_id="+balanceId+"\n" );
		sql.append("and wa.id = b.claim_id and wa.claim_type in ("+Constant.CLA_TYPE_02+","+Constant.CLA_TYPE_06+")  and wa.status="+Constant.CLAIM_APPLY_ORD_TYPE_07+"\n" );
		sql.append("and a.approval_result=10791007 )");
		this.update(sql.toString(), null);
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单ID查询索赔单信息(分页)
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> claimInfo(String balanceId,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select b.id,b.claim_no, nvl(b.repair_total,0) as repair_total,nvl(b.balance_amount,0) as balance_amount,(nvl(b.repair_total,0)-nvl(b.balance_amount,0))as sub_amount from tt_as_wr_application b where b.id in(select d.claim_id from tr_balance_claim d where d.balance_id='"+balanceId+"') " );
		sql.append("order by sub_amount desc,rowid asc");
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单ID查询特殊费用明细(分页)
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> feeInfo(String balanceId,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select c.id,c.fee_no,c.FEE_TYPE, nvl(c.declare_sum1,0) as declare_sum1,nvl(declare_sum,0) as declare_sum,(nvl(declare_sum1,0)-nvl(declare_sum,0))as fee_amount from tt_as_wr_spefee c where c.claimbalance_id ='"+balanceId+"' " );
		sql.append("order by fee_amount desc");
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	/**
	 * Iverson add By 2010-12-23
	 * 根据结算单ID查找旧件扣款信息
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> deductInfoByBalanceId(String balanceId) {
		String sql="select a.id,a.balance_no,a.dealer_code,a.dealer_name,a.DEDUCT_COUNT,nvl(a.TOTAL_AMOUNT,0) as TOTAL_AMOUNT from tt_as_wr_deduct_balance a where a.claimbalance_id ='"+balanceId+"'";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单ID查询考核扣款明细
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> fineByBalanceId(String balanceId) {
		String sql="select b.fine_id,T.DEALER_CODE,T.DEALER_NAME,nvl(b.FINE_SUM,0) as FINE_SUM from tt_as_wr_fine b,TM_DEALER T where T.DEALER_ID=b.dealer_id AND b.claimbalance_id ='"+balanceId+"'";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	/**
	 * Iverson add By 2010-12-21
	 * 根据结算单ID查询行政扣款明细
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> adminDeductInfoByBalanceId(String balanceId) {
		String sql="select c.id,c.dealer_code,c.dealer_name,nvl(c.DEDUCT_AMOUNT,0) as DEDUCT_AMOUNT from tt_as_wr_admin_deduct c where c.claimbalance_id ='"+balanceId+"'";
		List list=(List) pageQuery(sql, null, this.getFunName());
		return list;
	}
	
	/*********Iverson add By 2010-12-25 查看旧件明细(分页)*************************/
	public PageResult<Map<String, Object>> deductInfo(String deductId,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select c.id,c.DEDUCT_NO,c.PART_AMOUNT,c.DEDUCT_AMOUNT,c.MANHOUR_MONEY,c.MATERIAL_MONEY,c.OTHER_MONEY from TT_AS_WR_DEDUCT c where c.deduct_balance_id ='"+deductId+"' " );
		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	/**
	 * Iverson add By 2010-12-31
	 * 三包结算统计表
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> selectClaimBalance(String dealerCode,String DEALER_NAME,String yieldly,String startDate,String endDate,Double fixedAmount,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("select c.*,\n" );
		sql.append("       BY_LABOUR_amount + by_part_amount as FREE_AMOUNT,\n" );
		sql.append("       labour_amount + BY_LABOUR_amount as amount_labour,\n" );
		sql.append("       part_amount + by_part_amount as amount_part,\n" );
		sql.append("       BY_LABOUR_amount + by_part_amount + part_amount + labour_amount as SUM_BALANCE_AMOUNT\n" );
		sql.append("\n" );
		sql.append("  from (select t.dealer_code,\n" );
		sql.append("               t.dealer_name,\n" );
		sql.append("               a.yieldly,\n" );
		sql.append("               sum(nvl(a.balance_labour_amount, 0) +\n" );
		sql.append("                   nvl(a.appendlabour_amount, 0)) as labour_amount, --工时费\n" );
		sql.append("               sum(nvl(a.balance_part_amount, 0)) as part_amount, --材料费,\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                     from Tt_As_Wr_Application aa\n" );
		sql.append("                    where aa.id = a.id\n" );
		sql.append("                      and aa.claim_type = "+Constant.CLA_TYPE_02+")) as con,\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                      from Tt_As_Wr_Application aa\n" );
		sql.append("                     where aa.id = a.id\n" );
		sql.append("                       and aa.claim_type = "+Constant.CLA_TYPE_02+") * "+fixedAmount+") as BY_LABOUR_amount,\n" );
		sql.append("               sum((select SUM(AA.BALANCE_AMOUNT)\n" );
		sql.append("                     from Tt_As_Wr_Application aa\n" );
		sql.append("                    where aa.id = a.id\n" );
		sql.append("                      and aa.claim_type = "+Constant.CLA_TYPE_02+")) -\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                      from Tt_As_Wr_Application aa\n" );
		sql.append("                     where aa.id = a.id\n" );
		sql.append("                       and aa.claim_type = "+Constant.CLA_TYPE_02+") * "+fixedAmount+") as by_part_amount\n" );
		sql.append("          from tt_as_wr_application a, tm_dealer t\n" );
		sql.append("         where 1 = 1 and a.status=10791007 \n" );
		sql.append("           and a.dealer_id = t.dealer_id\n" );
		if(Utility.testString(dealerCode)){
			sql.append("   and t.dealer_code like '%"+dealerCode+"%'\n" );
		}
		if(Utility.testString(DEALER_NAME)){
			sql.append("   and t.dealer_name like '%"+DEALER_NAME+"%'\n" );
		}
		if(Utility.testString(yieldly)){
			sql.append("   and a.yieldly='"+yieldly+"'\n" );
		}
		if(Utility.testString(startDate)){
			sql.append("   AND a.account_date>=TO_DATE('"+startDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endDate)){
			sql.append("   AND a.account_date<=TO_DATE('"+endDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("         group by a.dealer_id, a.yieldly, t.dealer_code, t.dealer_name) c");

		System.out.println("sqlsql=="+sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> selectClaimBalanceExcelList(String dealerCode,String DEALER_NAME,String yieldly,String startDate,String endDate,Double fixedAmount){
		StringBuilder sql= new StringBuilder();
		sql.append("select c.*,\n" );
		sql.append("       BY_LABOUR_amount + by_part_amount as FREE_AMOUNT,\n" );
		sql.append("       labour_amount + BY_LABOUR_amount as amount_labour,\n" );
		sql.append("       part_amount + by_part_amount as amount_part,\n" );
		sql.append("       BY_LABOUR_amount + by_part_amount + part_amount + labour_amount as SUM_BALANCE_AMOUNT\n" );
		sql.append("\n" );
		sql.append("  from (select t.dealer_code,\n" );
		sql.append("               t.dealer_name,\n" );
		sql.append("               (select code.code_desc from tc_code code where code.code_id=a.yieldly) yieldly,\n" );
		sql.append("               sum(nvl(a.balance_labour_amount, 0) +\n" );
		sql.append("                   nvl(a.appendlabour_amount, 0)) as labour_amount, --工时费\n" );
		sql.append("               sum(nvl(a.balance_part_amount, 0)) as part_amount, --材料费,\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                     from Tt_As_Wr_Application aa\n" );
		sql.append("                    where aa.id = a.id\n" );
		sql.append("                      and aa.claim_type = "+Constant.CLA_TYPE_02+")) as con,\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                      from Tt_As_Wr_Application aa\n" );
		sql.append("                     where aa.id = a.id\n" );
		sql.append("                       and aa.claim_type = "+Constant.CLA_TYPE_02+") * "+fixedAmount+") as BY_LABOUR_amount,\n" );
		sql.append("               sum((select SUM(AA.BALANCE_AMOUNT)\n" );
		sql.append("                     from Tt_As_Wr_Application aa\n" );
		sql.append("                    where aa.id = a.id\n" );
		sql.append("                      and aa.claim_type = "+Constant.CLA_TYPE_02+")) -\n" );
		sql.append("               sum((select count(1)\n" );
		sql.append("                      from Tt_As_Wr_Application aa\n" );
		sql.append("                     where aa.id = a.id\n" );
		sql.append("                       and aa.claim_type = "+Constant.CLA_TYPE_02+") * "+fixedAmount+") as by_part_amount\n" );
		sql.append("          from tt_as_wr_application a, tm_dealer t\n" );
		sql.append("         where 1 = 1 and a.status=10791007 \n" );
		sql.append("           and a.dealer_id = t.dealer_id\n" );
		if(Utility.testString(dealerCode)){
			sql.append("   and t.dealer_code like '%"+dealerCode+"%'\n" );
		}
		if(Utility.testString(DEALER_NAME)){
			sql.append("   and t.dealer_name like '%"+DEALER_NAME+"%'\n" );
		}
		if(Utility.testString(yieldly)){
			sql.append("   and a.yieldly='"+yieldly+"'\n" );
		}
		if(Utility.testString(startDate)){
			sql.append("   AND a.account_date>=TO_DATE('"+startDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endDate)){
			sql.append("   AND a.account_date<=TO_DATE('"+endDate+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("         group by a.dealer_id, a.yieldly, t.dealer_code, t.dealer_name) c");
		
		System.out.println("sqlsql=="+sql.toString());
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	/**********
	 * add xionghcuan 
	 * @param id 
	 * 查询汇总单下所有类型为保养和服务活动的索赔单
	 */
	
	public List<Map<String, Object>> auditApplication(long  id){
		StringBuffer sql= new StringBuffer();
		sql.append("select a.id from Tt_As_Wr_Application a where 1=1 and exists (\n" );
		sql.append("select b.claim_id from tr_balance_claim b where 1=1 and exists (\n" );
		sql.append("select gb.balance_id from  tr_gather_balance gb where gb.gather_id="+id+" and gb.balance_id = b.balance_id)\n" );
		sql.append("and a.id = b.claim_id\n" );
		sql.append(")  and a.claim_type in (10661002,10661006)");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	
	//Iverson add by 2011-01-18
	public PageResult<Map<String, Object>> queryDealerInfo(String dealerCode,String dealerName,String DEALER_LEVEL,String STATUS,String area_code,String province,int curPage, int pageSize){
		StringBuilder sql= new StringBuilder();
		sql.append("with tt_balance as (select w.stationer_tel,w.claimer_tel,w.dealer_id from TT_AS_WR_CLAIM_BALANCE  w where   w.id in (\n");
		sql.append("SELECT max(c.id) FROM TT_AS_WR_CLAIM_BALANCE C   group by c.dealer_id))\n");
		sql.append("select b.root_org_name,\n");
		sql.append("       c.region_name,\n");  
		sql.append("       a.dealer_code,\n");  
		sql.append("       a.dealer_name as one_name,\n");  
		sql.append("       a.dealer_level,\n");  
		sql.append("       ac.dealer_name as two_name,\n");  
		sql.append("       a.status,\n");  
		sql.append("       t.stationer_tel,t.claimer_tel\n");   
		sql.append("  from tm_dealer              a,\n");  
		sql.append("       vw_org_dealer_service  b,\n");  
		sql.append("       tm_region              c,\n");  
		sql.append("       tm_dealer              ac,\n"); 
		sql.append("       tt_balance             t\n"); 
		sql.append(" where a.dealer_type= "+Constant.DEALER_TYPE_DWR+" and a.province_id = c.region_code\n");  
		sql.append("   and a.dealer_id = b.dealer_id\n");  
		sql.append("   and a.parent_dealer_d = ac.dealer_id(+) and a.dealer_id = t.dealer_id\n");

		if(Utility.testString(dealerCode)){
			sql.append("   AND a.DEALER_code like '%"+dealerCode+"%'\n" );
		}
		if(Utility.testString(dealerName)){
			sql.append("   AND a.dealer_name LIKE '%"+dealerName+"%'\n" );
		}
		if(Utility.testString(DEALER_LEVEL)){
			sql.append("   AND a.dealer_level = '"+DEALER_LEVEL+"'\n" );
		}
		if(Utility.testString(STATUS)){
			sql.append("   AND a.STATUS = '"+STATUS+"'\n" );
		}
		if(Utility.testString(area_code)){
			sql.append("   AND b.root_org_code like '%"+area_code+"%'\n" );
		}
		if(Utility.testString(province)){
			sql.append("   AND c.region_code = '"+province+"'\n" );
		}
		sql.append(" ORDER BY a.DEALER_code DESC \n");
		System.out.println(sql.toString());
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	//微车查询索赔单
	public List queryAutoClaim(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ID\n");
		sql.append("  FROM TT_AS_WR_APPLICATION A\n");
		sql.append(" WHERE A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_04+"\n");
		sql.append("  AND to_date(to_char(sysdate,'YYYY-MM-DD'),'YYYY-MM-DD') - to_date(to_char(A.AUDITING_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') > 3 and rownum<5000\n");
		//sql.append(" and not exists (select mi.model_id from Tt_As_Wr_Model_Group g,Tt_As_Wr_Model_Item mi ");
		//sql.append(" where g.wrgroup_type=10451001 and g.wrgroup_code in ('M302EA','M302G') ");
		//sql.append(" and g.wrgroup_id=mi.wrgroup_id and mi.model_id=a.model_id) ");
		return this.pageQuery(sql.toString(), null, this.getFunName());
		
		
	}
	
	//zhumingwei 2012-10-16 begion
	public List queryAutolabourcode(String claimId){
		StringBuffer sql = new StringBuffer();
		sql.append("select a.labour_id,a.wr_labourcode from tt_as_wr_labouritem a where a.id="+claimId+"\n");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public String queryCount(String vin,String code){
		StringBuffer sql = new StringBuffer();
		sql.append("select COUNT(1) as count from Tt_As_Wr_Application WA ,tt_as_wr_labouritem L\n");
		sql.append("WHERE WA.VIN='"+vin+"' AND WA.ID=L.ID AND  L.WR_LABOURCODE='"+code+"'\n");
		sql.append("AND WA.STATUS IN (10791008,10791007) AND WA.CLAIM_TYPE IN (10661001,10661007,10661009)\n");
		List<Map<String,Object>> rsList= pageQuery(sql.toString(), null, getFunName());
		
		return rsList.get(0).get("COUNT").toString();
	}
	//轿车查询索赔单
	public List queryAutoClaimJC(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ID\n");
		sql.append("  FROM TT_AS_WR_APPLICATION A\n");
		sql.append(" WHERE A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_04+"\n");
		sql.append("  AND to_date(to_char(sysdate,'YYYY-MM-DD'),'YYYY-MM-DD') - to_date(to_char(A.AUDITING_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') > (SELECT B.PARA_VALUE FROM tm_business_para B WHERE B.PARA_ID='50011002')\n");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	//开票单状态
	public List<TtAsWrClaimBalancePO> openclaimbalanceBystatus(Long dealerid) {
		String sqlfind="select t.remark from TT_AS_WR_CLAIM_BALANCE t where t.ocstatus=0 and t.dealer_id="+dealerid;
		List<TtAsWrClaimBalancePO> list = this.select(TtAsWrClaimBalancePO.class, sqlfind,null);
		return list;
	}

	public List<TtAsPartBorrowPO> findHurryPartBydearid(Long dealerid) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from TT_AS_PART_BORROW t where  t.is_return=0 and t.dealer_id="+dealerid);
		List<TtAsPartBorrowPO> list = this.select(TtAsPartBorrowPO.class, sb.toString(),null);
		return list;
	}

	/**
	 * 广宣接收入库单子
	 * @param dealerid 经销商ID
	 * @return
	 */
	public List<TtPartTransPO> findGxPartBydealerd(Long dealerid) {
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT S.TRANS_CODE\n");
		sql.append("  FROM TT_PART_TRANS S\n");
		sql.append(" WHERE S.ORDER_TYPE = "+Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07+"\n");
		sql.append("   AND S.STATE = "+Constant.CAR_FACTORY_TRANS_STATE_01+"\n");
		sql.append("   AND S.DEALER_ID ="+dealerid+"\n");
		List<TtPartTransPO> list = this.select(TtPartTransPO.class, sql.toString(), null);
		return list;
	}
}
