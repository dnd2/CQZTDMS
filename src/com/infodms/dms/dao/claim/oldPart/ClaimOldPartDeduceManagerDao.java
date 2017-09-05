package com.infodms.dms.dao.claim.oldPart;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;


import com.infodms.dms.bean.ClaimDeduceDetailListBean;
import com.infodms.dms.bean.ClaimDeduceOldPartDetailBean;
import com.infodms.dms.bean.ClaimDeductOtherItemListBean;
import com.infodms.dms.bean.ClaimLabourItemListBean;
import com.infodms.dms.bean.ClaimOldPartDeduceListBean;
import com.infodms.dms.bean.ClaimOldPartDeductListBean;
import com.infodms.dms.bean.DeductClaimInfoBean;
import com.infodms.dms.bean.DeductVinInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrDeductDetailPO;
import com.infodms.dms.po.TtAsWrDeductPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

@SuppressWarnings("unchecked")
public class ClaimOldPartDeduceManagerDao extends BaseDao{
	private POFactory fac=POFactoryBuilder.getInstance();
	public static Logger logger = Logger.getLogger(ClaimOldPartDeduceManagerDao.class);
	private static final ClaimOldPartDeduceManagerDao dao = null;
	
	public static final ClaimOldPartDeduceManagerDao getInstance() {
	   if(dao==null) return new ClaimOldPartDeduceManagerDao();
	   return dao;
	}

	/**
	 * Function：索赔旧件抵扣--按条件查询返回列表
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-21
	 */
	public PageResult<ClaimOldPartDeduceListBean> getDeduceByConditionList(Map params,int curPage, int pageSize){
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));
		String dealer_code=ClaimTools.dealParamStr(params.get("dealerCode"));//查询条件--经销商代码
		String dealer_name=ClaimTools.dealParamStr(params.get("dealerName"));//查询条件--经销商简称
		String back_order_no=ClaimTools.dealParamStr(params.get("back_order_no"));//查询条件--回运清单号
		String create_start_date=ClaimTools.dealParamStr(params.get("create_start_date"));//查询条件--建单开始日期
		String create_end_date=ClaimTools.dealParamStr(params.get("create_end_date"));//查询条件--建单结束日期
		String report_start_date=ClaimTools.dealParamStr(params.get("report_start_date"));//查询条件--提报开始日期
		String report_end_date=ClaimTools.dealParamStr(params.get("report_end_date"));//查询条件--提报结束日期
		String store_start_date=ClaimTools.dealParamStr(params.get("store_start_date"));//查询条件--入库开始日期
		String store_end_date=ClaimTools.dealParamStr(params.get("store_end_date"));//查询条件--入库结束日期
		String transport_type=ClaimTools.dealParamStr(params.get("transport_type"));//查询条件--货运方式
		String yieldlys=ClaimTools.dealParamStr(params.get("yieldlys"));//该用户的产地权限
		String yieldly=ClaimTools.dealParamStr(params.get("yieldly"));//查询条件-产地
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("/* Formatted on 2011/5/26 11:34:31 (QP5 v5.114.809.3010) */");
		sqlStr.append("SELECT * FROM (SELECT /*+rule*/\n" );
		sqlStr.append("tawor.id return_id,td.dealer_code,td.dealer_shortname dealer_name,tawor.return_no,tawor.transport_type,\n" );
		sqlStr.append("TO_CHAR(tawor.create_date, 'YYYY-MM-DD') create_date,\n" );
		sqlStr.append("TO_CHAR(tawor.return_date, 'YYYY-MM-DD') report_date,\n" );
		sqlStr.append("TO_CHAR(tawor.in_warhouse_date, 'YYYY-MM-DD') store_date,\n" );
		sqlStr.append("NVL(tawor.wr_amount, 0) wr_amount,\n" );
		sqlStr.append("NVL(tawor.part_amount, 0) part_amount,\n" );
		sqlStr.append("--NVL (tawor.part_amount, 0) - SUM (taword.sign_amount) diff_amount\n" );
		sqlStr.append("NVL(tawor.part_amount, 0) - (select sum(taword.sign_amount) from tt_as_wr_old_returned_detail taword where tawor.id = taword.return_id) diff_amount FROM tm_dealer td, tt_as_wr_old_returned tawor\n" );
		sqlStr.append("--tt_as_wr_old_returned_detail taword,\n" );
		sqlStr.append("where  tawor.dealer_id=td.dealer_id\n" );
		sqlStr.append("and tawor.yieldly in ("+yieldlys+")\n" );
		if(dealer_code!=null&&!"".equals(dealer_code)){
			String[] temp=dealer_code.split(",");
			String str="";
			sqlStr.append(" and (");
			for(int count=0;count<temp.length;count++){
				str+=" td.dealer_code='"+temp[count].replaceAll("'", "\''")+"' or\n";
			}
			sqlStr.append(str.substring(0, str.length()-3));
			sqlStr.append(")\n");
		}
		if(yieldly!=null&&!"".equals(yieldly))
			sqlStr.append(" and tawor.yieldly = '"+yieldly+"'\n");
		if(dealer_name!=null&&!"".equals(dealer_name))
			sqlStr.append(" and td.dealer_shortname like '%"+dealer_name.replaceAll("'", "\''")+"%'\n");
		if(back_order_no!=null&&!"".equals(back_order_no))
			sqlStr.append(" and tawor.return_no like'%"+back_order_no.replaceAll("'", "\''")+"%'\n");
		if(create_start_date!=null&&!"".equals(create_start_date))
			sqlStr.append(" and tawor.create_date>=to_date('" + create_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(create_end_date!=null&&!"".equals(create_end_date))
			sqlStr.append(" and tawor.create_date<=to_date('" + create_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(report_start_date!=null&&!"".equals(report_start_date))
			sqlStr.append(" and tawor.return_date>=to_date('" + report_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(report_end_date!=null&&!"".equals(report_end_date))
			sqlStr.append(" and tawor.return_date<=to_date('" + report_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(store_start_date!=null&&!"".equals(store_start_date))
			sqlStr.append(" and tawor.in_warhouse_date>=to_date('" + store_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		if(store_end_date!=null&&!"".equals(store_end_date))
			sqlStr.append(" and tawor.in_warhouse_date<=to_date('" + store_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		if(transport_type!=null&&!"".equals(transport_type))
			sqlStr.append(" and tawor.transport_type="+transport_type+"\n");
		sqlStr.append("and tawor.status="+Constant.BACK_LIST_STATUS_04+"\n" );
		sqlStr.append("order by tawor.create_date desc)");
		sqlStr.append("where diff_amount>0");
//		PageResult<ClaimOldPartDeduceListBean> pr = pageQuery(ClaimOldPartDeduceListBean.class,
//				sqlStr.toString(),null, pageSize, curPage);
//		return pr;
		return fac.pageQuery(sqlStr.toString(), null, new DAOCallback(){
			public ClaimOldPartDeduceListBean wrapper(ResultSet rs, int rowNo){
				ClaimOldPartDeduceListBean bean=new ClaimOldPartDeduceListBean();
				try {
					bean.setReturn_id(rs.getLong("return_id"));
					bean.setDealer_code(rs.getString("dealer_code"));
					bean.setDealer_name(rs.getString("dealer_name"));
					bean.setReturn_no(rs.getString("return_no"));
					bean.setTransport_type(rs.getInt("transport_type"));
					bean.setCreate_date(rs.getString("create_date"));
					bean.setReport_date(rs.getString("report_date"));
					bean.setStore_date(rs.getString("store_date"));
					bean.setWr_amount(rs.getInt("wr_amount"));
					bean.setPart_amount(rs.getInt("part_amount"));
					int diff=rs.getInt("diff_amount");
					bean.setDiff_amount(diff);
					if(diff<=0){
						bean.setIs_deduct("NO");
					}else{
						POFactory fac=POFactoryBuilder.getInstance();
						StringBuffer sbSql=new StringBuffer();
						
						sbSql.append("select nvl(sum(nvl(aa.deduct_money,0)),0) deduct_amount from Tt_As_Wr_Deduct_Detail aa,tt_as_wr_deduct c where aa.deduct_id=c.id and c.is_del=0 and aa.claim_id in( ");
						sbSql.append("select b.claim_id from tt_as_wr_old_returned_detail b where b.return_id=?)");
						
						List list=new ArrayList();
						list.add(rs.getLong("return_id"));
						List list2=fac.select(sbSql.toString(), list, new DAOCallback(){
							public Map wrapper(ResultSet rs, int rowNo){
								Map map=null;
								try {
									map=new HashMap();
									map.put("DEDUCT_AMOUNT", rs.getBigDecimal("DEDUCT_AMOUNT"));
									//map.put("BALANCE_AMOUNT", rs.getBigDecimal("BALANCE_AMOUNT"));
								} catch (Exception e) {
									e.printStackTrace();
								}
								return map;
							}
						});
						Map map=(Map)list2.get(0);
						double DEDUCT_AMOUNT=((BigDecimal)map.get("DEDUCT_AMOUNT")).doubleValue();
						//double BALANCE_AMOUNT=((BigDecimal)map.get("BALANCE_AMOUNT")).doubleValue();
						bean.setDeduct_amount(String.valueOf(DEDUCT_AMOUNT));
						//bean.setBalance_amount(String.valueOf(BALANCE_AMOUNT));
						
						StringBuffer sbSql1=new StringBuffer();
						sbSql1.append("select nvl(sum(nvl(ap.balance_amount,0)),0) balance_amount from tt_as_wr_application ap where ap.id in( ");
						sbSql1.append("select b.claim_id from tt_as_wr_old_returned_detail b where b.return_id=?)");
						List list1=new ArrayList();
						list1.add(rs.getLong("return_id"));
						List list3=fac.select(sbSql1.toString(), list1, new DAOCallback(){
							public Map wrapper(ResultSet rs, int rowNo){
								Map map=null;
								try {
									map=new HashMap();
									map.put("BALANCE_AMOUNT", rs.getBigDecimal("BALANCE_AMOUNT"));
									
								} catch (Exception e) {
									e.printStackTrace();
								}
								return map;
							}
						});
						Map map1=(Map)list3.get(0);
						double BALANCE_AMOUNT=((BigDecimal)map1.get("BALANCE_AMOUNT")).doubleValue();
						bean.setBalance_amount(String.valueOf(BALANCE_AMOUNT));
						StringBuffer sql2= new StringBuffer();
						sql2.append("\n" );
						sql2.append("select sum(nvl(b.price, 0)) PRICE\n" );
						sql2.append("  from tt_as_wr_old_returned_detail b\n" );
						sql2.append(" where b.return_id = ?\n" );
						sql2.append("   and (b.deductible_reason_code is not null or b.sign_amount=0 )");
						List listPrice=new ArrayList();
						listPrice.add(rs.getLong("return_id"));
						List listPrice1=fac.select(sql2.toString(), listPrice, new DAOCallback(){
							public Map wrapper(ResultSet rs, int rowNo){
								Map map=null;
								try {
									map=new HashMap();
									map.put("PRICE", rs.getBigDecimal("PRICE"));
									
								} catch (Exception e) {
									e.printStackTrace();
								}
								return map;
							}
						});
						
						Map mapPrice=(Map)listPrice1.get(0);
						Double PRICE=((BigDecimal)mapPrice.get("PRICE")).doubleValue();
						bean.setPrice(PRICE);
					
						if(BALANCE_AMOUNT>DEDUCT_AMOUNT){
							bean.setIs_deduct("YES");
						}else{
							bean.setIs_deduct("NO");
						}
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				return bean;
			}
		}, pageSize, curPage);
	}
	/**
	 * Function：索赔旧件抵扣--明细操作的详细信息查询
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-21
	 */
	public ClaimDeduceOldPartDetailBean getClaimApplyDetailInfo(Map params){
		String return_id=ClaimTools.dealParamStr(params.get("return_id"));//参数--回运清单主键
		
		StringBuffer sql= new StringBuffer();

		sql.append("select tawor.id return_id,\n");
		sql.append("       td.dealer_code,\n" );
		sql.append("       td.dealer_shortname dealer_name,\n" );
		sql.append("       tdor.root_org_name attach_area,\n" );
		sql.append("       tawor.transport_type,\n" );
		sql.append("       tc.code_desc transport_desc,\n" );
		sql.append("       tawor.return_no,\n" );
		sql.append("       to_char(tawor.create_date, 'YYYY-MM-DD') create_date,\n" );
		sql.append("       to_char(tawor.return_date, 'YYYY-MM-DD') report_date,\n" );
		sql.append("       to_char(tawor.in_warhouse_date, 'YYYY-MM-DD') store_date,\n" );
		sql.append("       tawor.wr_start_date,\n" );
		sql.append("       nvl(tawor.parkage_amount, 0) parkage_amount,\n" );
		sql.append("       (nvl(tawor.part_amount, 0) - (select sum(nvl(taword.sign_amount,0)) from tt_as_wr_old_returned_detail taword where tawor.id = taword.return_id)) diff_amount,\n" );
		sql.append("       tu.name approve_name,\n" );
		sql.append("       tawor.tran_no\n" );
		sql.append("  from tt_as_wr_old_returned        tawor,\n" );
		sql.append("       tt_as_wr_old_returned_detail taword,\n" );
		sql.append("       tm_dealer                    td,\n" );
		sql.append("       vw_org_dealer_service        tdor,\n" );
		sql.append("       tc_code                      tc,\n" );
		sql.append("       tc_user                      tu\n" );
		sql.append(" where tawor.id = taword.return_id\n" );
		sql.append("   and tawor.dealer_id = td.dealer_id\n" );
		sql.append("   and tawor.dealer_id = tdor.dealer_id\n" );
		sql.append("   and tawor.transport_type = tc.code_id(+)\n" );
		sql.append("   and tawor.update_by = tu.user_id\n" );
		sql.append("   and tawor.id = "+return_id+"\n" );
//		sql.append(" group by tawor.id,\n" );
//		sql.append("          td.dealer_code,\n" );
//		sql.append("          td.dealer_shortname,\n" );
//		sql.append("          tdor.root_org_name,\n" );
//		sql.append("          tawor.transport_type,\n" );
//		sql.append("          tc.code_desc,\n" );
//		sql.append("          tawor.return_no,\n" );
//		sql.append("          tawor.create_date,\n" );
//		sql.append("          tawor.return_date,\n" );
//		sql.append("          tawor.in_warhouse_date,\n" );
//		sql.append("          tawor.wr_start_date,\n" );
//		sql.append("          tawor.parkage_amount,\n" );
//		sql.append("          tawor.part_amount,\n" );
//		sql.append("          tu.name,\n" );
//		sql.append("          tawor.tran_no");

		PageResult<ClaimDeduceOldPartDetailBean> pr = pageQuery(ClaimDeduceOldPartDetailBean.class,
				sql.toString(),null, 10, 1);
	    if(pr!=null){
	    	return (ClaimDeduceOldPartDetailBean)pr.getRecords().get(0);
	    }else{
	    	return null;
	    }
	}
	public PageResult<ClaimDeduceDetailListBean> getDeduceClaimInfoList(Map params,int curPage, int pageSize){
		
		String return_id=ClaimTools.dealParamStr(params.get("return_id"));//参数--回运清单主键
		//MODIFY by XZM 2010-09-12 加入抵扣明细页面数据排序,通过CASE实现
		//排序规则：索赔单已经结算 未抵扣过（抵扣金额0） 且  存在差异数量    排在最上面
		//         索赔单未结算  且 存在差异数量  排在其下   其他的排在最后
		/* 该语句未把工时和其他项目抵扣金额加入
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select m.*,(case when balance_status="+Constant.CLAIM_APPLY_ORD_TYPE_07+" and deduct_amount<=0 and diff_amount>0 then 1\n" );
		sqlStr.append("when balance_status<>"+Constant.CLAIM_APPLY_ORD_TYPE_07+" and diff_amount>0 then 2 else 3 end) ordercol\n");
		sqlStr.append("from(select taword.return_id,taword.vin,taword.claim_no,\n" );
		sqlStr.append("sum(taword.n_return_amount) n_return_amount,\n" );
		sqlStr.append("sum(taword.sign_amount) sign_amount,\n" );
		sqlStr.append("sum(taword.return_amount)-sum(taword.sign_amount) diff_amount,tawa.status balance_status,\n" );
		sqlStr.append("nvl(sum(tawp.deduct_amount),0) deduct_amount,taword.create_date\n" );
		sqlStr.append("from tt_as_wr_old_returned_detail taword,\n");
		sqlStr.append("tt_as_wr_application tawa,\n" );
		sqlStr.append("tt_as_wr_partsitem tawp\n" );
		sqlStr.append("where taword.claim_no=tawa.claim_no\n" );
		sqlStr.append("and taword.part_id=tawp.part_id\n" );
		sqlStr.append("and taword.return_id="+return_id+"\n");
		sqlStr.append("group by taword.return_id,taword.vin,taword.claim_no,taword.create_date,tawa.status) m\n" );
		sqlStr.append("order by ordercol,create_date");
		*/
		
		StringBuilder sql= new StringBuilder();
		sql.append("select m.*,"+return_id+" return_id,(case when balance_status=10791007 and deduct_amount<=0 and diff_amount>0 then 1\n" );
		sql.append("when balance_status<>10791007 and diff_amount>0 then 2 else 3 end) ordercol\n" );
		sql.append("from( select /*+ INDEX(b1 PK_TT_AS_WR_APPLICATION) */a1.*,b1.claim_no,b1.status balance_status,b1.create_date,b1.vin,(b1.repair_total-b1.balance_amount) BALANCE_DEDUCT from\n" );
		sql.append("(select claim_id,sum(n_return_amount) n_return_amount,\n" );
		sql.append("sum(sign_amount) sign_amount,sum(diff_amount) diff_amount,\n" );
		sql.append("sum(deduct_amount) deduct_amount from(\n" );
		sql.append("select taword.claim_id,sum(taword.n_return_amount) n_return_amount,\n" );
		sql.append("sum(taword.sign_amount) sign_amount,\n" );
		sql.append("sum(taword.return_amount)-sum(taword.sign_amount) diff_amount,\n" );
		sql.append("nvl(sum(taword.DEDUCTIBLE_PRICE),0) deduct_amount\n" );
		sql.append("from tt_as_wr_old_returned_detail taword,tt_as_wr_partsitem tawp\n" );
		sql.append("where taword.part_id = tawp.part_id\n" );
		sql.append("and taword.return_id = "+return_id+"\n" );
		sql.append("group by taword.claim_id\n" );
		sql.append("union all\n" );
		
		//zhumingwei 
		//sql.append("select tawa.id claim_id,0 n_return_amount,0 sign_amount,0 diff_amount,nvl(sum(tawl.deduct_amount),0) deduct_amount\n" );
		//sql.append("from tt_as_wr_application tawa,tt_as_wr_labouritem tawl,tt_as_wr_old_returned_detail m1\n" );
		//sql.append("where 1=1\n" );
		//sql.append("and tawa.id = tawl.id\n" );
		//sql.append("and tawl.id = m1.claim_id\n" );
		//sql.append("and m1.return_id= "+return_id+"\n" );
		//sql.append("group by tawa.id)");
		sql.append(" select tawl.id claim_id,0 n_return_amount,0 sign_amount,0 diff_amount,sum(nvl(tawl.deduct_amount, 0)) deduct_amount");
		sql.append(" from tt_as_wr_labouritem tawl");
		sql.append(" where tawl.id in (select m1.claim_id from tt_as_wr_old_returned_detail m1 where m1.return_id="+return_id+")");
		sql.append(" group by tawl.id");
		//zhumingwei 
		
		sql.append(")group by claim_id) a1,tt_as_wr_application b1\n" );
		sql.append("where a1.claim_id = b1.id and  a1.n_return_amount!=a1.sign_amount) m\n" );
		sql.append("order by ordercol,create_date");


		PageResult<ClaimDeduceDetailListBean> pr = pageQuery(ClaimDeduceDetailListBean.class,
				sql.toString(),null, pageSize, curPage);
	    return pr;
	}
	/**
	 * Function：获得抵扣明细导出list
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-29
	 */
	public List<Map<String, Object>> getDeductExportList(Map params){
		String return_id=ClaimTools.dealParamStr(params.get("return_id"));//参数--回运清单主键
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select taword.id detail_id,taword.return_id,taword.vin,taword.claim_no,\n" );
		sqlStr.append("sum(taword.n_return_amount) n_return_amount,\n" );
		sqlStr.append("sum(taword.sign_amount) sign_amount,\n" );
		sqlStr.append("sum(taword.return_amount)-sum(taword.sign_amount) diff_amount\n" );
		sqlStr.append("from tt_as_wr_old_returned_detail taword,\n" );
		sqlStr.append("tt_as_wr_application tawa,\n" );
		sqlStr.append("tt_as_wr_partsitem tawp\n" );
		sqlStr.append("where taword.claim_no=tawa.claim_no\n" );
		sqlStr.append("and taword.part_id=tawp.part_id\n" );
		sqlStr.append("and taword.return_id="+return_id+"\n");
		sqlStr.append("group by taword.id,taword.return_id,taword.vin,taword.claim_no,taword.create_date\n");
		sqlStr.append("order by taword.create_date");
		
		List<Map<String, Object>> list = pageQuery(sqlStr.toString(), null,getFunName());
        return list;
	}
	public DeductClaimInfoBean getDeductClaimInfo(Map params){
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));
		String claim_back_id=ClaimTools.dealParamStr(params.get("claim_back_id"));//参数--回运清单主键
		String vin=ClaimTools.dealParamStr(params.get("vin"));//参数--vin
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		String claim_id=ClaimTools.dealParamStr(params.get("claim_id"));//参数--索赔单id
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawor.dealer_id,td.dealer_code,td.dealer_shortname dealer_name,\n" );
		sqlStr.append("tawapp.id claim_id,tawapp.claim_no,tawapp.ro_no,to_char(tawapp.ro_startdate,'YYYY-MM-DD') ro_startdate,\n" );
		sqlStr.append("to_char(tawapp.ro_enddate,'YYYY-MM-DD') ro_enddate,tawapp.in_mileage,\n" );
		sqlStr.append("to_char(tawapp.guarantee_date,'YYYY-MM-DD') guarantee_date,tawapp.serve_advisor\n" );
		sqlStr.append("from tt_as_wr_old_returned tawor,tt_as_wr_old_returned_detail taword,TT_AS_WR_APPLICATION tawapp,tm_dealer td\n" );
		sqlStr.append("where tawor.id=taword.return_id and taword.claim_id=tawapp.id\n" );
		sqlStr.append("and tawapp.dealer_id=td.dealer_id(+)\n" );
		sqlStr.append("and td.status="+Constant.STATUS_ENABLE+"\n" );
		sqlStr.append("and taword.return_id="+claim_back_id+"\n");
		sqlStr.append("and taword.vin='"+vin+"'\n");
		sqlStr.append("and taword.claim_no='"+claim_no+"'\n");
		
		PageResult<DeductClaimInfoBean> pr = pageQuery(DeductClaimInfoBean.class,
				sqlStr.toString(),null, 1, 1);
		if(pr!=null){
			return (DeductClaimInfoBean)pr.getRecords().get(0);
		}else{
		    return null;
		}
	}
	public DeductVinInfoBean getVinInfo(Map params){
		//String company_id=ClaimTools.dealParamStr(params.get("company_id"));
		//String claim_back_id=ClaimTools.dealParamStr(params.get("claim_back_id"));//参数--回运清单主键
		//String vin=ClaimTools.dealParamStr(params.get("vin"));//参数--vin
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		String claim_id=ClaimTools.dealParamStr(params.get("claim_id"));//参数--索赔单id
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawa.vin,tawa.license_no vehicle_no,tawa.engine_no,\n" );
		sqlStr.append("tawa.series_code series_name,tawa.model_code package_name,\n" );
		sqlStr.append("tawa.gearbox_no,tawa.rearaxle_no,tawa.transfer_no,tc.code_desc yieldly\n" );
		sqlStr.append("from tt_as_wr_application tawa,tc_code tc,tm_vhcl_material_group tg\n" );
		sqlStr.append("where tawa.yieldly=tc.code_id(+) and tawa.brand_code=tg.group_code(+)");
		sqlStr.append("and tawa.id='"+claim_id+"'\n" );
		
		PageResult<DeductVinInfoBean> pr = pageQuery(DeductVinInfoBean.class,
				sqlStr.toString(),null, 1, 1);
		if(pr!=null&&pr.getTotalRecords()>0){
			return (DeductVinInfoBean)pr.getRecords().get(0);
		}else{
		    return null;
		}
	}
	/**
	 * Function：获得索赔单申请内容
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-23
	 */
	public TtAsWrApplicationExtPO queryApplicationDetailById(Map params) {
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append(" select a.*,g1.group_name as series_name,g2.group_name as model_name,b.dealer_code as dealer_code, b.dealer_name as dealer_name, \n");
		sqlStr.append(" v.ENGINE_NO as engine_no,v.GEARBOX_NO as gearbox_no,v.REARAXLE_NO as rearaxle_no,v.TRANSFER_NO as transfer_no,v.LICENSE_NO as license_no, \n");
		sqlStr.append(" b1.code_name as damage_degree_name,b2.code_name as damage_area_name,b3.code_name as damage_type_name,b4.code_name as troubleName, \n");
		sqlStr.append(" c.code_desc as claim_name ");
		sqlStr.append(" from TT_AS_WR_APPLICATION a   \n");
		sqlStr.append(" left outer join TM_DEALER b on a.dealer_id=b.dealer_id \n");
		sqlStr.append(" left outer join TM_VEHICLE v on v.vin=a.vin \n");
		sqlStr.append(" left outer join TM_VHCL_MATERIAL_GROUP g1 on v.series_id=g1.group_id \n");
		sqlStr.append(" left outer join TM_VHCL_MATERIAL_GROUP g2 on v.model_id=g2.group_id \n");
		sqlStr.append(" left outer join tm_business_chng_code b1 on b1.type_code='"+Constant.BUSINESS_CHNG_CODE_01+"' and b1.code=a.damage_degree \n");
		sqlStr.append(" left outer join tm_business_chng_code b2 on b2.type_code='"+Constant.BUSINESS_CHNG_CODE_02+"' and b2.code=a.damage_area \n");
		sqlStr.append(" left outer join tm_business_chng_code b3 on b3.type_code='"+Constant.BUSINESS_CHNG_CODE_03+"' and b3.code=a.damage_type \n");
		sqlStr.append(" left outer join tm_business_chng_code b4 on b4.type_code='"+Constant.BUSINESS_CHNG_CODE_04+"' and b4.code=a.trouble_code \n");
		sqlStr.append(" left outer join tc_code c on c.code_id=a.claim_type \n");
		sqlStr.append(" where 1=1 ");
		sqlStr.append(" AND a.ID=(select distinct(tawapp.id) from tt_as_wr_old_returned_detail tawor,\n");
		sqlStr.append("tt_as_wr_application tawapp\n");
		sqlStr.append("where tawor.claim_no=tawapp.claim_no and tawor.claim_no='"+claim_no+"') ");
		TtAsWrApplicationExtPO tawep = new TtAsWrApplicationExtPO();
		List<TtAsWrApplicationExtPO> ls = select(TtAsWrApplicationExtPO.class,sqlStr.toString(),null);
		if (ls!=null){
			if (ls.size()>0) {
				tawep = ls.get(0);
			}
		}
		return tawep;
	}
	/**
	 * Function：获得索赔配件抵扣列表
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-23
	 */
	@SuppressWarnings("unused")
	public List<ClaimOldPartDeductListBean> getClaimOldPartDeductInfoList(Map params){
		String claim_back_id=ClaimTools.dealParamStr(params.get("claim_back_id"));//参数--回运清单主键
		String vin=ClaimTools.dealParamStr(params.get("vin"));//参数--vin
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select taword.id as part_id,taword.claim_no,tawp.down_part_code,tawp.part_code,tawp.part_name,taword.return_amount return_num,taword.sign_amount quantity,taword.return_amount - taword.sign_amount diff_amount,taword.price,(nvl(taword.price, 0) - nvl(taword.deductible_price, 0)) app_amount,tawp.producer_code,tawp.old_part_code,nvl(taword.price, 0) balance_amount,nvl(taword.deductible_price, 0) deduct_amount,taword.deduct_remark\n" );
		//sqlStr.append("select tawp.part_id,taword.claim_no,tawp.down_part_code,tawp.part_code,\n" );
		//sqlStr.append("tawp.part_name,tawp.return_num return_num,tawp.quantity quantity,\n" );
		//sqlStr.append("taword.return_amount-taword.sign_amount diff_amount,\n" );
		//sqlStr.append("tawp.price,(nvl(tawp.balance_amount,0)-nvl(tawp.deduct_amount,0)) app_amount,--(taword.return_amount-taword.sign_amount)*tawp.price app_amount,\n" );
		//sqlStr.append("tawp.producer_code,tawp.old_part_code,\n" );
		//sqlStr.append("nvl(tawp.balance_amount,0) balance_amount,nvl(tawp.deduct_amount,0) deduct_amount,taword.deduct_remark\n" );
		sqlStr.append("from TT_AS_WR_PARTSITEM tawp,\n" );
		sqlStr.append("Tt_As_Wr_Application tawa,\n" );
		sqlStr.append("tt_as_wr_old_returned_detail taword\n" );
		sqlStr.append("where tawp.id=tawa.id and tawa.id=taword.claim_id\n" );
		sqlStr.append("and tawp.part_id=taword.part_id\n" );
		sqlStr.append("and taword.return_id="+claim_back_id+"\n");
		sqlStr.append("and taword.claim_no='"+claim_no+"'\n");
		sqlStr.append("order by taword.create_date\n");
		System.out.println("sqlsql=="+sqlStr.toString());
		List<ClaimOldPartDeductListBean> list = select(ClaimOldPartDeductListBean.class,sqlStr.toString(),null);
		
		return list;
	}
	/**
	 * Function：获得索赔工时抵扣列表
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-23
	 */
	@SuppressWarnings("unused")
	public List<ClaimLabourItemListBean> getClaimLabourItemList(Map params){
		String claim_back_id=ClaimTools.dealParamStr(params.get("claim_back_id"));//参数--回运清单主键
		String vin=ClaimTools.dealParamStr(params.get("vin"));//参数--vin
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		String claim_id=ClaimTools.dealParamStr(params.get("claim_id"));//参数--索赔单id
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawl.labour_id,tawa.claim_no,tawl.wr_labourcode,tawl.wr_labourname,\n" );
		sqlStr.append("tawl.BALANCE_QUANTITY labour_hours,tawl.labour_price,\n" );
		sqlStr.append("tawl.labour_amount,nvl(tawl.balance_amount,0) balance_amount,nvl(tawl.deduct_amount,0) deduct_amount\n" );
		sqlStr.append("from TT_AS_WR_LABOURITEM tawl,\n" );
		sqlStr.append("Tt_As_Wr_Application tawa\n" );
		sqlStr.append("where tawl.id=tawa.id\n" );
		sqlStr.append("and tawa.id='"+claim_id+"'\n");
		sqlStr.append("order by tawl.create_date");
        List<ClaimLabourItemListBean> list = select(ClaimLabourItemListBean.class,sqlStr.toString(),null);
		
		return list;
	}
	/**
	 * Function：获得索赔其他项目抵扣列表
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-23
	 */
	@SuppressWarnings("unused")
	public List<ClaimDeductOtherItemListBean> getClaimDeductOtherItemList(Map params){
		String claim_back_id=ClaimTools.dealParamStr(params.get("claim_back_id"));//参数--回运清单主键
		String vin=ClaimTools.dealParamStr(params.get("vin"));//参数--vin
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		String claim_id=ClaimTools.dealParamStr(params.get("claim_id"));//参数--索赔单id
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawn.netitem_id,tawa.claim_no,tawn.item_desc,\n" );
		sqlStr.append("tawn.amount,tawn.remark,nvl(tawn.balance_amount,0) balance_amount,nvl(tawn.deduct_amount,0) deduct_amount\n" );
		sqlStr.append("from TT_AS_WR_NETITEM tawn,\n" );
		sqlStr.append("Tt_As_Wr_Application tawa\n" );
		sqlStr.append("where tawn.id=tawa.id\n" );
		sqlStr.append("and tawa.id='"+claim_id+"'\n");
		sqlStr.append("order by tawn.create_date");
        List<ClaimDeductOtherItemListBean> list = select(ClaimDeductOtherItemListBean.class,sqlStr.toString(),null);
		
		return list;

	}
	public String deDuctDataOper(RequestWrapper request,Long user_id,Long companyId,ActionContext act){
		String claim_back_id=request.getParamValue("claim_back_id");//获得回运清单主表id
		String claim_dealer_id=request.getParamValue("claim_dealer_id");//获得索赔单经销商id
		String claim_id=request.getParamValue("claim_id");//获得索赔id
		String deduct_part_select_str=request.getParamValue("deduct_part_select_str");//获得选中的配件抵扣id串 (里面的id实际上是tt_as_wr_old_returned_detail 的ID（主键）)
		String deduct_hour_select_str=request.getParamValue("deduct_hour_select_str");//获得选中的工时抵扣id串
		String deduct_other_select_str=request.getParamValue("deduct_other_select_str");//获得选中的其他项目抵扣id串
		BigDecimal part_total_amount=getTotalMoney(request,deduct_part_select_str);//获得抵扣配件总金额
		BigDecimal hour_total_amount=getTotalMoney(request,deduct_hour_select_str);//获得抵扣工时总金额
		BigDecimal other_total_amount=getTotalMoney(request,deduct_other_select_str);//获得抵扣其他项目总金额
		
		boolean flag2 = DBLockUtil.lock(claim_id, DBLockUtil.BUSINESS_TYPE_16);
		if(!flag2){
			return "LOCK";
		}
		
		//检查当前登录人是否有抵扣权限
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select person_code from tc_user where user_id="+user_id);
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		if(temp==null||"".equals(temp)||"null".equals(temp)){
			return "failure_001";//返回错误代码--当前登录人没有抵扣权限
		}
		//检查参数
		if("".equals(claim_dealer_id)||"".equals(claim_id)){
			return "failure_002";//返回错误代码--获得索赔单的经销商id或索赔单号失败
		}
		//从回运清单明细中获得索赔旧件数和抵扣数
		sqlStr.delete(0, sqlStr.length());
		sqlStr.append("select nvl(sum(taword.return_amount),0) return_amount,nvl(sum(taword.sign_amount),0) sign_amount,\n" );
		sqlStr.append("nvl(sum(taword.return_amount)-sum(taword.sign_amount),0) diff_amount\n" );
		sqlStr.append("from\n" );
		sqlStr.append("tt_as_wr_old_returned_detail taword\n" );
		sqlStr.append("where taword.return_id="+claim_back_id);
		rsList=pageQuery(sqlStr.toString(), null, getFunName());
		temp=rsList.toString();
		temp=temp.substring(temp.indexOf("[{")+2, temp.indexOf("}]"));
		String[] temArr=temp.split(",");
		String RETURN_AMOUNT=temArr[0].substring(temArr[0].indexOf("=")+1);//获得回运数
		String DIFF_AMOUNT=temArr[1].substring(temArr[1].indexOf("=")+1);//获得差异数
		//String SIGN_AMOUNT=temArr[2].substring(temArr[2].indexOf("=")+1);//获得签收数
		
		//向抵扣单主表插入信息
		Long insert_id=Long.parseLong(SequenceManager.getSequence(""));//抵扣单主键
		String ro_id=SequenceManager.getSequence("DO");//抵扣单工单号
		TtAsWrDeductPO insertObj=new TtAsWrDeductPO();
		insertObj.setId(insert_id);
		insertObj.setDeductNo(ro_id);
		insertObj.setClaimId(Long.parseLong(claim_id));
		insertObj.setDealerId(Long.parseLong(claim_dealer_id));
		insertObj.setPartAmount(Integer.parseInt(RETURN_AMOUNT));
		insertObj.setDeductAmount(Integer.parseInt(DIFF_AMOUNT));
		insertObj.setManhourMoney(hour_total_amount.doubleValue());
		insertObj.setMaterialMoney(part_total_amount.doubleValue());
		insertObj.setOtherMoney(other_total_amount.doubleValue());
		insertObj.setOemCompanyId(companyId);
		insertObj.setCreateBy(user_id);
		insertObj.setNoticeDate(new Date());
		insertObj.setCreateDate(new Date());
		
		//zhumingwei 2011-7-11
		insertObj.setIsCount(1L);
		//zhumingwei 2011-7-11
		
		super.insert(insertObj);
		//向抵扣明细表插入信息
		if(deduct_part_select_str!=null&&!"".equals(deduct_part_select_str)){//被选抵扣配件入抵扣明细表
			TtAsWrDeductDetailPO insertDetailObj=new TtAsWrDeductDetailPO();
			String[] partArr=deduct_part_select_str.split(",");
			for(int count=0;count<partArr.length;count++){
				sqlStr.delete(0, sqlStr.length());
				
				TtAsWrOldReturnedDetailPO po = new TtAsWrOldReturnedDetailPO();
				po.setId(Long.parseLong(partArr[count].replace("deduct_part_id", "")));
				TtAsWrOldReturnedDetailPO poValue = (TtAsWrOldReturnedDetailPO)super.select(po).get(0); 
				
	    		insertDetailObj.setId(Long.parseLong(SequenceManager.getSequence("")));
	    		insertDetailObj.setDeductId(insert_id);
	    		insertDetailObj.setClaimId(Long.parseLong(claim_id));
	    		insertDetailObj.setItemType(Constant.PRE_AUTH_ITEM_02);
	    		insertDetailObj.setItemCode(poValue.getPartCode());
	    		insertDetailObj.setItemName(poValue.getPartName());
	    		insertDetailObj.setDeductMoney(Double.parseDouble(request.getParamValue(partArr[count].replace("id", "money")).replace(",", "")));
	    		insertDetailObj.setDeductReason(Integer.parseInt(request.getParamValue(partArr[count].replace("deduct_part_id", "back_type"))));
	    		insertDetailObj.setRemark(ClaimTools.dealParamStr(request.getParamValue(partArr[count].replace("id", "remark"))));
	    		insertDetailObj.setCreateBy(user_id);
	    		insertDetailObj.setCreateDate(new Date());
	    		insertDetailObj.setPartId(poValue.getId());
	    		super.insert(insertDetailObj);
	    		//修改索赔单之配件表的抵扣金额字段
	    		TtAsWrOldReturnedDetailPO updateObj=new TtAsWrOldReturnedDetailPO();
	    		updateObj.setId(poValue.getId());
	    		TtAsWrOldReturnedDetailPO vo=new TtAsWrOldReturnedDetailPO();
	    		vo.setDeductiblePrice(poValue.getDeductiblePrice()+Double.parseDouble(request.getParamValue(partArr[count].replace("id", "money")).replace(",", "")));
	    		vo.setUpdateBy(user_id);
	    		vo.setUpdateDate(new Date());
	    		super.update(updateObj, vo);
			}
		}
		if(deduct_hour_select_str!=null&&!"".equals(deduct_hour_select_str)){//被选抵扣工时入抵扣明细表
			TtAsWrDeductDetailPO insertDetailObj=new TtAsWrDeductDetailPO();
			String[] hourArr=deduct_hour_select_str.split(",");
			for(int count=0;count<hourArr.length;count++){
				sqlStr.delete(0, sqlStr.length());
    			sqlStr.append("select * from TT_AS_WR_LABOURITEM where labour_id="+hourArr[count].replace("deduct_hour_id", ""));
    			PageResult<TtAsWrLabouritemPO> list=pageQuery(TtAsWrLabouritemPO.class, sqlStr.toString(), null, 1, 1);
    			TtAsWrLabouritemPO qryObj=(TtAsWrLabouritemPO)list.getRecords().get(0);
    			
    			List<Map<String, Object>> listPartId = this.getDeductAmount1(qryObj.getLabourId().toString(),claim_id);
    			double deductAmount=0d;
    			if(listPartId.get(0).get("DEDUCT_AMOUNT")!=null){
    			
    				deductAmount = ((BigDecimal)listPartId.get(0).get("DEDUCT_AMOUNT")).doubleValue();
    			}
    			if(qryObj.getBalanceAmount()>=deductAmount+Double.parseDouble(request.getParamValue(hourArr[count].replace("id", "money")).replace(",", ""))){
	    			insertDetailObj.setId(Long.parseLong(SequenceManager.getSequence("")));
	    			insertDetailObj.setDeductId(insert_id);
	    			insertDetailObj.setClaimId(Long.parseLong(claim_id));
	    			insertDetailObj.setItemType(Constant.PRE_AUTH_ITEM_01);
	    			insertDetailObj.setItemCode(qryObj.getWrLabourcode());
	    			insertDetailObj.setItemName(qryObj.getWrLabourname());
	    			insertDetailObj.setDeductMoney(Double.parseDouble(request.getParamValue(hourArr[count].replace("id", "money")).replace(",", "")));
	    			insertDetailObj.setRemark(ClaimTools.dealParamStr(request.getParamValue(hourArr[count].replace("id", "remark"))));
	    			insertDetailObj.setCreateBy(user_id);
	    			insertDetailObj.setCreateDate(new Date());
	    			//zhumingwei 2011-05-31 新加字段把工时id插入抵扣明细信息表
	    			insertDetailObj.setPartId(qryObj.getLabourId());
	    			//zhumingwei 2011-05-31 新加字段把工时id插入抵扣明细信息表 end
	    			super.insert(insertDetailObj);
	    			//修改索赔单之工时表的抵扣金额字段
	    			TtAsWrLabouritemPO updateObj=new TtAsWrLabouritemPO();
	    			updateObj.setLabourId(qryObj.getLabourId());
	    			TtAsWrLabouritemPO vo=new TtAsWrLabouritemPO();
	    			vo.setDeductAmount(qryObj.getDeductAmount()+Double.parseDouble(request.getParamValue(hourArr[count].replace("id", "money")).replace(",", "")));
	    			vo.setUpdateBy(user_id);
	    			vo.setUpdateDate(new Date());
	    			super.update(updateObj, vo);
    			}else{
    				TtAsWrDeductPO insertObj1=new TtAsWrDeductPO();
    				insertObj1.setId(insert_id);
    				super.delete(insertObj1);
    				return "no";
    			}
			}
		}
		if(deduct_other_select_str!=null&&!"".equals(deduct_other_select_str)){//被选抵扣其他项目入抵扣明细表
			TtAsWrDeductDetailPO insertDetailObj=new TtAsWrDeductDetailPO();
			String[] otherArr=deduct_other_select_str.split(",");
			for(int count=0;count<otherArr.length;count++){
				sqlStr.delete(0, sqlStr.length());
    			sqlStr.append("select * from TT_AS_WR_NETITEM where netitem_id="+otherArr[count].replace("deduct_other_id", ""));
    			PageResult<TtAsWrNetitemPO> list=pageQuery(TtAsWrNetitemPO.class, sqlStr.toString(), null, 1, 1);
    			TtAsWrNetitemPO qryObj=(TtAsWrNetitemPO)list.getRecords().get(0);
    			insertDetailObj.setId(Long.parseLong(SequenceManager.getSequence("")));
    			insertDetailObj.setDeductId(insert_id);
    			insertDetailObj.setClaimId(Long.parseLong(claim_id));
    			insertDetailObj.setItemType(Constant.PRE_AUTH_ITEM_03);
    			insertDetailObj.setItemCode(qryObj.getItemCode());
    			insertDetailObj.setItemName(qryObj.getItemDesc());
    			insertDetailObj.setDeductMoney(Double.parseDouble(request.getParamValue(otherArr[count].replace("id", "money")).replace(",", "")));
    			insertDetailObj.setRemark(ClaimTools.dealParamStr(request.getParamValue(otherArr[count].replace("id", "remark"))));
    			insertDetailObj.setCreateBy(user_id);
    			insertDetailObj.setCreateDate(new Date());
    			//zhumingwei 2011-05-31 新加字段把其他项目id插入抵扣明细信息表
    			insertDetailObj.setPartId(qryObj.getNetitemId());
    			//zhumingwei 2011-05-31 新加字段把其他项目id插入抵扣明细信息表 end
    			super.insert(insertDetailObj);
    			//修改索赔单之工时表的抵扣金额字段
    			TtAsWrNetitemPO updateObj=new TtAsWrNetitemPO();
    			updateObj.setNetitemId(qryObj.getNetitemId());
    			TtAsWrNetitemPO vo=new TtAsWrNetitemPO();
    			vo.setDeductAmount(qryObj.getDeductAmount()+Double.parseDouble(request.getParamValue(otherArr[count].replace("id", "money")).replace(",", "")));
    			vo.setUpdateBy(user_id);
    			vo.setUpdateDate(new Date());
    			super.update(updateObj, vo);
			}
		}
		
		return "success";
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * Function：根据选中的字符串获得页面抵扣总金额
	 * @param  ：	
	 * @return:		@param request
	 * @return:		@param selectedStr
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-24
	 */
    private BigDecimal getTotalMoney(RequestWrapper request,String selectedStr){
    	BigDecimal retValue= new BigDecimal("0.00");
    	if(selectedStr==null||"".equals(selectedStr)) return retValue;
    	
    	String[] tempArr=selectedStr.split(",");
    	for(int i=0;i<tempArr.length;i++){
    		BigDecimal addValue=new BigDecimal(request.getParamValue(tempArr[i].replace("id","money")).replace(",", ""));
    		retValue=retValue.add(addValue);
    	}
    	return retValue;
    }
    public String getIsBalanceFlag(Map params){
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		String claim_id=ClaimTools.dealParamStr(params.get("claim_id"));//参数--索赔单号
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select * from tt_as_wr_application where claim_no='"+claim_no+"'" );
        PageResult<TtAsWrApplicationPO> pr=pageQuery(TtAsWrApplicationPO.class, sqlStr.toString(), null, 1, 1);
        List list=pr.getRecords();
        if(list!=null&&list.size()>0){
        	Integer status=((TtAsWrApplicationPO)list.get(0)).getStatus();
        	if(status.equals(Constant.CLAIM_APPLY_ORD_TYPE_07)){//已结算
        		return "1";
        	}else{
        		return "0";
        	}
        }else{
        	return "0";
        }
    }
    
    public List<Map<String, Object>> getDeductAmount(String part_id,String claimId){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select sum(nvl(a.deduct_money,0)) deduct_amount from Tt_As_Wr_Deduct_Detail a\n" );
		sqlStr.append("where a.part_id='"+part_id+"' and claim_id="+claimId+" and a.item_type=10871002\n" );
		List<Map<String, Object>> list = pageQuery(sqlStr.toString(), null,getFunName());
        return list;
	}
    
    public List<Map<String, Object>> getDeductAmount1(String labourId,String claimId){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select sum(nvl(a.deduct_money,0)) deduct_amount from Tt_As_Wr_Deduct_Detail a\n" );
		sqlStr.append("where a.part_id='"+labourId+"' and claim_id="+claimId+" and a.item_type=10871001\n" );
		List<Map<String, Object>> list = pageQuery(sqlStr.toString(), null,getFunName());
        return list;
	}
}
