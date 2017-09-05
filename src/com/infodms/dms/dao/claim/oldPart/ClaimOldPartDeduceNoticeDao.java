package com.infodms.dms.dao.claim.oldPart;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import bsh.ParseException;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimDeductOtherItemListBean;
import com.infodms.dms.bean.ClaimLabourItemListBean;
import com.infodms.dms.bean.ClaimOldPartAgainDeductListBean;
import com.infodms.dms.bean.ClaimOrderBean;
import com.infodms.dms.bean.DeduceNoticeBean;
import com.infodms.dms.bean.DeductClaimInfoBean;
import com.infodms.dms.bean.DeductClaimInfoListBean;
import com.infodms.dms.bean.DeductDelDetailListBean;
import com.infodms.dms.bean.DeductDetailListBean;
import com.infodms.dms.bean.DeductVinInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrDeductDetailPO;
import com.infodms.dms.po.TtAsWrDeductPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 索赔旧件抵扣
 * @author XZM
 */
@SuppressWarnings("unchecked")
public class ClaimOldPartDeduceNoticeDao extends BaseDao {
	
	private static final ClaimOldPartDeduceNoticeDao dao = null;
	
	/**
	 * 取得DAO实例
	 * @return
	 */
	public static final ClaimOldPartDeduceNoticeDao getInstance() {
	   if(dao==null) return new ClaimOldPartDeduceNoticeDao();
	   return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * 查询对应经销商的索赔抵扣通知单
	 * （经销商端功能：需要限制用户查询对应经销商数据）
	 * @param conditionBean 查询条件
	 * @param curPage 当前页码
	 * @param pageSize 每页数据量
	 * @return
	 */
	public PageResult<Map<String,Object>> queryOldPartDeduceNotice(DeduceNoticeBean conditionBean,
			Integer curPage,Integer pageSize){
		
		StringBuilder sBuilder = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();
		
		this.createWhereMap("DEDUCT_NO", "LIKE", conditionBean.getTransportNO(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("IS_BAL", "=", conditionBean.getDeductStatus(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("NOTICE_DATE", ">=", conditionBean.getNoticeDateS(), sBuilder, paramList, 1, "YYYY-MM-DD HH24:MI:SS", "A");
		this.createWhereMap("NOTICE_DATE", "<=", conditionBean.getNoticeDateE(), sBuilder, paramList, 1, "YYYY-MM-DD HH24:MI:SS", "A");
		this.createWhereMap("DEALER_ID", "=", conditionBean.getDealerId(), sBuilder, paramList, 2, "", "A");
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.ID, A.IS_BAL,B.DEALER_CODE, B.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       A.DEDUCT_NO,TO_CHAR(A.NOTICE_DATE, 'YYYY-MM-DD') NOTICE_DATE,\n" );
		sql.append("       A.PART_AMOUNT PART_COUNT,A.DEDUCT_AMOUNT DEDUCT_COUNT,\n" );
		sql.append("       NVL(A.MATERIAL_MONEY, 0) PART_AMOUNT,NVL(A.MANHOUR_MONEY, 0) LABOUR_AMOUNT,\n" );
		sql.append("       NVL(A.OTHER_MONEY, 0) OTHER_AMOUNT,\n" );
		sql.append("       NVL(A.MATERIAL_MONEY + A.MANHOUR_MONEY + A.OTHER_MONEY, 0) TOTALAMOUNT ,\n" );
		sql.append("       C.ID BALANCE_ID,C.BALANCE_NO\n" );
		sql.append("  FROM TT_AS_WR_DEDUCT                A,\n" );
		sql.append("       TM_DEALER                      B,\n" );
		sql.append("       TT_AS_WR_CLAIM_BALANCE         C,\n" );
		sql.append("       TT_AS_WR_DEDUCT_BALANCE        D,\n" );
		sql.append("       TT_AS_WR_DEDUCT_BALANCE_DETAIL E\n" );
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+)\n" );
		sql.append("   AND A.ID = E.DEDUCT_ID(+)\n" );
		sql.append("   AND E.BALANCE_ID = D.ID(+)\n" );
		sql.append("   AND D.CLAIMBALANCE_ID = C.ID(+)\n");
		sql.append(" AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");//查询未标示删除的抵扣单
		
		String sqlTemp = sql.toString();
		sqlTemp = sqlTemp + 
			  sBuilder.toString() +
			  " ORDER BY A.ID DESC";
		
	    PageResult<Map<String,Object>> resultList = this.pageQuery(sqlTemp, paramList,
	    		this.getClass().getName()+".queryOldPartDeduceNotice()", pageSize, curPage);
		
		return resultList;
	}
	
	/**
	 * 查询对应经销商的索赔抵扣单信息（车厂端）
	 * @param conditionBean 查询条件
	 * @param curPage 当前页码
	 * @param pageSize 每页数据量
	 * @return
	 */
	public PageResult<Map<String,Object>> queryOldPartDeduce(DeduceNoticeBean conditionBean,
			Integer curPage,Integer pageSize){
		
		StringBuilder sBuilder = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();
		
		this.createWhereMap("DEDUCT_NO", "LIKE", conditionBean.getTransportNO(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("IS_BAL", "=", conditionBean.getDeductStatus(), sBuilder, paramList, 2, "", "A");
		this.createWhereMap("NOTICE_DATE", ">=", conditionBean.getNoticeDateS(), sBuilder, paramList, 1, "YYYY-MM-DD HH24:MI:SS", "A");
		this.createWhereMap("NOTICE_DATE", "<=", conditionBean.getNoticeDateE(), sBuilder, paramList, 1, "YYYY-MM-DD HH24:MI:SS", "A");
		this.createWhereMap("DEALER_CODE", "IN", conditionBean.getDealerCodes(), sBuilder, paramList, 2, "", "B");
		this.createWhereMap("DEALER_SHORTNAME", "LIKE", conditionBean.getDealerName(), sBuilder, paramList, 2, "", "B");
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.ID,A.IS_BAL,B.DEALER_CODE,B.DEALER_SHORTNAME DEALER_NAME,A.DEDUCT_NO,TO_CHAR(A.NOTICE_DATE,'YYYY-MM-DD') NOTICE_DATE,A.PART_AMOUNT PART_COUNT,A.DEDUCT_AMOUNT DEDUCT_COUNT,\n" );
		sql.append(" NVL(A.MATERIAL_MONEY,0) PART_AMOUNT,NVL(A.MANHOUR_MONEY,0) LABOUR_AMOUNT,NVL(A.OTHER_MONEY,0) OTHER_AMOUNT,\n" );
		sql.append(" NVL(A.MATERIAL_MONEY+A.MANHOUR_MONEY+A.OTHER_MONEY,0) TOTALAMOUNT\n" );
		sql.append(" FROM TT_AS_WR_DEDUCT A,TT_AS_WR_APPLICATION C,TM_DEALER B\n" );
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID\n");
		sql.append(" AND A.CLAIM_ID=C.ID\n");
		sql.append(" AND C.YIELDLY IN (").append(conditionBean.getYieldlys()).append(")\n");
		sql.append(" AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");//查询未标示删除的抵扣单
		
		String sqlTemp = sql.toString();
		sqlTemp = sqlTemp + 
			  sBuilder.toString() +
			  " ORDER BY A.ID DESC";
		
	    PageResult<Map<String,Object>> resultList = this.pageQuery(sqlTemp, paramList,
	    		this.getClass().getName()+".queryOldPartDeduce()", pageSize, curPage);
		
		return resultList;
	}
	
	/**
	 * 查询对应抵扣单明细
	 * @param deduceId 抵扣单ID
	 * @param curPage 当前页码
	 * @param pageSize 每页数据量
	 * @return
	 */
	public PageResult<Map<String,Object>> queryDeduceNoticeDetail(String deduceId,
			Integer curPage,Integer pageSize){
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(Long.parseLong(deduceId));
		
		StringBuilder sBuilder= new StringBuilder();
		sBuilder.append("SELECT B.ID CLAIM_ID,B.CLAIM_NO,B.VIN,A.ITEM_TYPE,A.ITEM_CODE,A.ITEM_NAME,\n" );
		sBuilder.append(" A.DEDUCT_MONEY,A.DEDUCT_REASON,A.REMARK\n" );
		sBuilder.append(" FROM TT_AS_WR_DEDUCT_DETAIL A,TT_AS_WR_APPLICATION B\n" );
		sBuilder.append(" WHERE 1=1\n" );
		sBuilder.append(" AND A.CLAIM_ID = B.ID");
		sBuilder.append(" AND A.DEDUCT_ID = ?");
		sBuilder.append(" ORDER BY A.ID DESC");

		String sql = sBuilder.toString();
		
		return this.pageQuery(sql, paramList,
				this.getClass().getName()+".queryDeduceNoticeDetail()",pageSize, curPage);
	}
	
	//Iverson add BY 2011-01-20
	public PageResult<Map<String,Object>> queryDeductClaim(ClaimOrderBean orderBean,
			Integer curPage,Integer pageSize){
		//StringBuilder sBuilder = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();
		
		//初始化索赔申请单查询条件
		//this.createWhereMap("YIELDLY" ,"IN" ,orderBean.getYieldlys(), sBuilder, paramList,2,"","A");
		//this.createWhereMap("CLAIM_NO" ,"LIKE" ,orderBean.getClaimNo() , sBuilder, paramList,2,"","A");
		//this.createWhereMap("RO_NO" ,"LIKE" ,orderBean.getRoNo() , sBuilder, paramList,2,"","A");
		//this.createWhereMap("LINE_NO","=" ,orderBean.getLineNo(), sBuilder, paramList,2,"","A");
		//this.createWhereMap("CLAIM_TYPE","=" ,orderBean.getClaimType() , sBuilder, paramList,2,"","A");
		//this.createWhereMap("STATUS", "=" ,orderBean.getClaimStatus() , sBuilder, paramList,2,"","A");
		//this.createWhereMap("DEALER_CODE" ,"IN" ,orderBean.getDealerCodes(), sBuilder, paramList,2,"","C");
		//this.createWhereMap("DEALER_SHORTNAME" ,"LIKE" ,orderBean.getDealerName(), sBuilder, paramList,2,"","C");
		//this.createWhereMap("CREATE_DATE", ">=" ,orderBean.getApplyStartDate(), sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
		//this.createWhereMap("CREATE_DATE","<=" ,orderBean.getApplyEndDate() , sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
		
		//String whereMap = sBuilder.toString();
		
		StringBuilder sqltemp= new StringBuilder();
		sqltemp.append("SELECT A.ID,A.CLAIM_NO,A.DEALER_ID,C.DEALER_CODE,C.DEALER_NAME DEALER_NAME,\n" );
		sqltemp.append(" A.VIN,\n" );
		sqltemp.append(" A.RO_NO RO_NO,\n" );
		sqltemp.append("(SELECT SUM(NVL(E.BALANCE_QUANTITY,0)) FROM TT_AS_WR_PARTSITEM E WHERE E.ID=A.ID GROUP BY E.ID) AS QUANTITY,\n");
		sqltemp.append("      (SELECT SUM(NVL(E.RETURN_NUM,0)) FROM TT_AS_WR_PARTSITEM E WHERE E.ID=A.ID GROUP BY E.ID) AS RETURN_NUM,\n");  
		sqltemp.append("      (SELECT SUM(NVL(E.BALANCE_QUANTITY,0))-SUM(NVL(E.RETURN_NUM,0)) FROM TT_AS_WR_PARTSITEM E WHERE E.ID=A.ID GROUP BY E.ID) AS MISTAKECOUNT,\n");
		sqltemp.append(" A.CLAIM_TYPE,TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE\n" );
		sqltemp.append(" FROM TT_AS_WR_APPLICATION A,\n" );
		//sqltemp.append("(SELECT ID,SUM(BALANCE_QUANTITY) QUANTITY,SUM(RETURN_NUM) RETURN_NUM\n" );
		//sqltemp.append(" FROM TT_AS_WR_PARTSITEM where 1=1\n" );
		//if(Utility.testString(orderBean.getApplyStartDate())){
			//sqltemp.append(" and CREATE_DATE >= TO_DATE('"+orderBean.getApplyStartDate()+"','YYYY-MM-DD HH24:MI:SS')\n");
		//}
		//if(Utility.testString(orderBean.getApplyEndDate())){
			//sqltemp.append(" and CREATE_DATE <= TO_DATE('"+orderBean.getApplyEndDate()+"','YYYY-MM-DD HH24:MI:SS')\n");
		//}
		//sqltemp.append(" GROUP BY ID) B,TM_DEALER C\n" );
		sqltemp.append(" TM_DEALER C WHERE 1=1\n" );
		//sqltemp.append(" AND A.ID = B.ID\n" );
		sqltemp.append(" AND A.DEALER_ID = C.DEALER_ID\n");
		if(Utility.testString(orderBean.getClaimStatus())){
			sqltemp.append(" AND A.STATUS = "+orderBean.getClaimStatus()+"\n");
		}
		if(Utility.testString(orderBean.getYieldlys())){
			sqltemp.append(" AND A.YIELDLY in ("+orderBean.getYieldlys()+")\n");
		}
		if(Utility.testString(orderBean.getApplyStartDate())){
			sqltemp.append(" and A.CREATE_DATE >= TO_DATE('"+orderBean.getApplyStartDate()+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(orderBean.getApplyEndDate())){
			sqltemp.append(" and A.CREATE_DATE <= TO_DATE('"+orderBean.getApplyEndDate()+"','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(orderBean.getClaimNo())){
			sqltemp.append(" AND A.CLAIM_NO like '%"+orderBean.getClaimNo()+"%'\n");
		}
		if(Utility.testString(orderBean.getRoNo())){
			sqltemp.append(" AND A.RO_NO like '%"+orderBean.getRoNo()+"%'\n");
		}
		if(Utility.testString(orderBean.getLineNo())){
			sqltemp.append(" AND A.LINE_NO = '%"+orderBean.getLineNo()+"%'\n");
		}
		if(Utility.testString(orderBean.getClaimType())){
			sqltemp.append(" AND A.CLAIM_TYPE = "+orderBean.getClaimType()+"\n");
		}
		if(Utility.testString(orderBean.getDealerCodes())){
			sqltemp.append(" AND C.DEALER_CODE IN ('"+orderBean.getDealerCodes()+"')\n");
		}
		if(Utility.testString(orderBean.getDealerName())){
			sqltemp.append(" AND C.DEALER_NAME LIKE '%"+orderBean.getDealerName()+"%'\n");
		}
		PageResult<Map<String, Object>> result = null;
		//String sql = sqltemp.toString() +
					    //whereMap +
					//    GetVinUtil.getVins(orderBean.getVin(), "A") +
					//    "  and exists(select 1 from Tt_As_Wr_Old_Returned p, Tt_As_Wr_Old_Returned_Detail od where p.id = od.return_id and p.status = 10811004 and od.claim_id = a.id" 
					
					
					   // ")  AND EXISTS(SELECT 1 FROM TT_AS_WR_PARTSITEM WHERE ID=A.ID) ORDER BY A.ID DESC";
					sqltemp.append( GetVinUtil.getVins(orderBean.getVin(), "A")+"  and exists(select 1 from Tt_As_Wr_Old_Returned p, Tt_As_Wr_Old_Returned_Detail od where p.id = od.return_id and p.status = 10811004 and od.claim_id = a.id" );
					
					if(Utility.testString(orderBean.getBarcodeNo())){
						sqltemp.append("  and od.barcode_no like '%"+orderBean.getBarcodeNo()+"%'\n");
					}
					sqltemp.append( ")  AND EXISTS(SELECT 1 FROM TT_AS_WR_PARTSITEM WHERE ID=A.ID) ORDER BY A.ID DESC");
					
		
		result = (PageResult<Map<String, Object>>)this.pageQuery(sqltemp.toString(),
																paramList,
																this.getClass().getName()+".queryDeductClaim()",
																pageSize,
																curPage);
		return result;
	}
	
	/**
	 * 拼查询条件，如果页面查询过来不为空，则拼装到查询条件中
	 * @param param 参数列 对应数据库中字段
	 * @param value 参数值
	 * @param oper 操作符
	 * @param sBuilder 拼装条件容器
	 * @param paramList 参数列表
	 * @param dataType 数据类型
	 *        1 : 时间
	 *        2 ：其他
	 * @param dataFormat 数据格式，现在只有时间类型需要添加格式
	 * @param table 标明表名或别名
	 * @return
	 */
	private void createWhereMap(String param,String oper,String value,
			StringBuilder sBuilder,List<Object> paramList,int dataType,
			String dataFormat,String table){
		if(Utility.testString(value)){
			param = table + "." + param;
			if(dataType==1) {//时间
				sBuilder.append(" AND ").append(param).append(" ")
				.append(oper).append(" TO_DATE(" +"?" + ",'" + dataFormat + "')");
				paramList.add(value);
			}else if("IN".equalsIgnoreCase(oper)){
				StringTokenizer st = new StringTokenizer(value,",");
				
				boolean flag = true;
				while(st.hasMoreTokens()){
					String tempValue = st.nextToken();
					if(Utility.testString(tempValue)){
						if(flag)//保证只加一次AND COL IN (
							sBuilder.append(" AND ").append(param).append(" ").append(oper).append(" (");
						flag = false;
						sBuilder.append("?,");
						paramList.add(tempValue);
					}
				}
				if(value.split(",").length>0){
					sBuilder.append("'')");//加入后半个括号，同时多加一个空''
				}
			}else{//其他
				sBuilder.append(" AND ").append(param).append(" ").append(oper).append(" ?");
				if("LIKE".equalsIgnoreCase(oper)){//模糊查询
					paramList.add("%" +value +"%");
				} else{
					paramList.add(value);
				}
			}
		}
	}
	/**
	 * Function：通过索赔工单号获得工单基本信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-25
	 */
	public DeductClaimInfoBean getDeductClaimInfo(Map params){
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawa.dealer_id,td.dealer_code,td.dealer_shortname dealer_name,\n" );
		sqlStr.append("tawa.id claim_id,tawa.claim_no,tawa.ro_no,\n" );
		sqlStr.append("to_char(tawa.ro_startdate,'YYYY-MM-DD') ro_startdate,\n" );
		sqlStr.append("to_char(tawa.ro_enddate,'YYYY-MM-DD') ro_enddate,\n" );
		sqlStr.append("tawa.in_mileage,to_char(tawa.guarantee_date,'YYYY-MM-DD') guarantee_date,\n" );
		sqlStr.append("tawa.serve_advisor\n" );
		sqlStr.append("from tt_as_wr_application tawa,tm_dealer td\n" );
		sqlStr.append("where tawa.dealer_id=td.dealer_id(+)");
		sqlStr.append("and td.status="+Constant.STATUS_ENABLE+"\n" );
		sqlStr.append("and tawa.claim_no='"+claim_no+"'\n");
		
		PageResult<DeductClaimInfoBean> pr = pageQuery(DeductClaimInfoBean.class,
				sqlStr.toString(),null, 1, 1);
		if(pr!=null){
			return (DeductClaimInfoBean)pr.getRecords().get(0);
		}else{
		    return null;
		}
	}
	public DeductVinInfoBean getVinInfo(Map params){
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawa.vin,tawa.license_no vehicle_no,tawa.engine_no,\n" );
		sqlStr.append("tawa.series_code series_name,tawa.model_code package_name,\n" );
		sqlStr.append("tawa.gearbox_no,tawa.rearaxle_no,tawa.transfer_no,tc.code_desc yieldly\n" );
		sqlStr.append("from tt_as_wr_application tawa,tc_code tc,tm_vhcl_material_group tg\n" );
		sqlStr.append("where tawa.yieldly=tc.code_id(+) and tawa.brand_code=tg.group_code(+)");
		sqlStr.append("and tawa.claim_no='"+claim_no+"'\n" );
		
		PageResult<DeductVinInfoBean> pr = pageQuery(DeductVinInfoBean.class,
				sqlStr.toString(),null, 1, 1);
		if(pr!=null&&pr.getTotalRecords()>0){
			return (DeductVinInfoBean)pr.getRecords().get(0);
		}else{
		    return null;
		}
	}
	/**
	 * Function：获得索赔配件抵扣列表
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-23
	 */
	public List<ClaimOldPartAgainDeductListBean> getClaimOldPartDeductInfoList(Map params){
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		
		StringBuffer sqlStr= new StringBuffer();
		/*sqlStr.append("select tawp.part_id,tawa.claim_no,tawp.down_part_code,tawp.part_code,\n" );
		sqlStr.append("tawp.part_name,tawp.BALANCE_QUANTITY quantity,tawp.return_num return_num,tawp.price,tawp.producer_code,\n" );
		sqlStr.append("tawp.old_part_code,nvl(tawp.balance_amount,0) balance_amount,\n" );
		sqlStr.append("nvl(tawp.deduct_amount,0) deduct_amount\n" );
		sqlStr.append("from TT_AS_WR_PARTSITEM tawp,tt_as_wr_application tawa\n" );
		sqlStr.append("where tawp.id=tawa.id \n");
		sqlStr.append("and tawa.claim_no='"+claim_no+"'\n" );
		sqlStr.append("order by tawp.create_date");*/
		
		sqlStr.append("select taword.id as part_id,taword.claim_no,tawp.down_part_code,tawp.part_code,\n" );
		sqlStr.append("tawp.part_name,taword.sign_amount quantity,taword.return_amount return_num,tawp.price,tawp.producer_code,\n" );
		sqlStr.append("tawp.old_part_code,nvl(taword.price, 0) balance_amount,\n" );
		sqlStr.append("nvl(taword.deductible_price, 0) deduct_amount\n" );
		sqlStr.append("from TT_AS_WR_PARTSITEM tawp,tt_as_wr_application tawa,tt_as_wr_old_returned_detail taword\n" );
		sqlStr.append("where tawp.id=tawa.id \n");
		sqlStr.append("and tawa.id=taword.claim_id and tawp.part_id=taword.part_id and tawa.claim_no='"+claim_no+"'\n" );
		sqlStr.append("order by tawp.create_date");
		
		List<ClaimOldPartAgainDeductListBean> list = select(ClaimOldPartAgainDeductListBean.class,sqlStr.toString(),null);
		
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
	public List<ClaimLabourItemListBean> getClaimLabourItemList(Map params){
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawl.labour_id,tawa.claim_no,tawl.wr_labourcode,tawl.wr_labourname,\n" );
		sqlStr.append("tawl.BALANCE_QUANTITY labour_hours,tawl.labour_price,\n" );
		sqlStr.append("tawl.labour_amount,nvl(tawl.balance_amount,0) balance_amount,nvl(tawl.deduct_amount,0) deduct_amount\n" );
		sqlStr.append("from TT_AS_WR_LABOURITEM tawl,\n" );
		sqlStr.append("Tt_As_Wr_Application tawa\n" );
		sqlStr.append("where tawl.id=tawa.id\n" );
		sqlStr.append("and tawa.claim_no='"+claim_no+"'\n");
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
	public List<ClaimDeductOtherItemListBean> getClaimDeductOtherItemList(Map params){
		String claim_no=ClaimTools.dealParamStr(params.get("claim_no"));//参数--索赔单号
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawn.netitem_id,tawa.claim_no,tawn.item_desc,\n" );
		sqlStr.append("tawn.amount,tawn.remark,nvl(tawn.balance_amount,0) balance_amount,nvl(tawn.deduct_amount,0) deduct_amount\n" );
		sqlStr.append("from TT_AS_WR_NETITEM tawn,\n" );
		sqlStr.append("Tt_As_Wr_Application tawa\n" );
		sqlStr.append("where tawn.id=tawa.id\n" );
		sqlStr.append("and tawa.claim_no='"+claim_no+"'\n");
		sqlStr.append("order by tawn.create_date");
        List<ClaimDeductOtherItemListBean> list = select(ClaimDeductOtherItemListBean.class,sqlStr.toString(),null);
		
		return list;
	}
	/**
	 * Function：二次抵扣
	 * @param  ：	
	 * @return:		@param request
	 * @return:		@param user_id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-28
	 */
	public String againDeDuctDataOper(RequestWrapper request,Long user_id,Long companyId){
		String claim_no=request.getParamValue("claim_no");//获得回运清单主表id
		String claim_dealer_id=request.getParamValue("claim_dealer_id");//获得索赔单经销商id
		String claim_id=request.getParamValue("claim_id");//获得索赔id
		String deduct_part_select_str=request.getParamValue("deduct_part_select_str");//获得选中的配件抵扣id串
		String deduct_hour_select_str=request.getParamValue("deduct_hour_select_str");//获得选中的工时抵扣id串
		String deduct_other_select_str=request.getParamValue("deduct_other_select_str");//获得选中的其他项目抵扣id串
		BigDecimal part_total_amount=getTotalMoney(request,deduct_part_select_str,"deduct_part_id","deduct_part_money");//获得抵扣配件总金额
		BigDecimal hour_total_amount=getTotalMoney(request,deduct_hour_select_str,"deduct_hour_id","deduct_hour_money");//获得抵扣工时总金额
		BigDecimal other_total_amount=getTotalMoney(request,deduct_other_select_str,"deduct_other_id","deduct_other_money");//获得抵扣其他项目总金额
		
		//检查当前登录人是否有抵扣权限
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select person_code from tc_user where user_id="+user_id);
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		if(temp==null||"".equals(temp)){
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
		sqlStr.append("where taword.claim_no='"+claim_no+"'");
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
		insertObj.setNoticeDate(new Date());
		insertObj.setCreateBy(user_id);
		insertObj.setCreateDate(new Date());
		
		//zhumingwei 2011-7-11
		insertObj.setIsCount(2L);
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
	    		insertDetailObj.setDeductMoney(Double.parseDouble(request.getParamValue(partArr[count].replace("id", "money"))));
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
    			if(qryObj.getBalanceAmount()>=deductAmount+Double.parseDouble(request.getParamValue(hourArr[count].replace("id", "money")))){
	    			insertDetailObj.setId(Long.parseLong(SequenceManager.getSequence("")));
	    			insertDetailObj.setDeductId(insert_id);
	    			insertDetailObj.setClaimId(Long.parseLong(claim_id));
	    			insertDetailObj.setItemType(Constant.PRE_AUTH_ITEM_01);
	    			insertDetailObj.setItemCode(qryObj.getWrLabourcode());
	    			insertDetailObj.setItemName(qryObj.getWrLabourname());
	    			insertDetailObj.setDeductMoney(Double.parseDouble(request.getParamValue(hourArr[count].replace("id", "money"))));
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
	    			vo.setDeductAmount(qryObj.getDeductAmount()+Double.parseDouble(request.getParamValue(hourArr[count].replace("id", "money"))));
	    			vo.setUpdateBy(user_id);
	    			vo.setUpdateDate(new Date());
	    			super.update(updateObj, vo);
    			}else{
    				return "no";
    			}
			}
		}
		if(deduct_other_select_str!=null&&!"".equals(deduct_other_select_str)){//被选抵扣工时入抵扣明细表
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
    			insertDetailObj.setDeductMoney(Double.parseDouble(request.getParamValue(otherArr[count].replace("id", "money"))));
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
    			vo.setDeductAmount(qryObj.getDeductAmount()+Double.parseDouble(request.getParamValue(otherArr[count].replace("id", "money"))));
    			insertDetailObj.setRemark(ClaimTools.dealParamStr(request.getParamValue(otherArr[count].replace("id", "remark"))));
    			vo.setUpdateBy(user_id);
    			vo.setUpdateDate(new Date());
    			super.update(updateObj, vo);
			}
		}
		
		return "success";
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
    private BigDecimal getTotalMoney(RequestWrapper request,String selectedStr,String documentId,String repalceid){
    	BigDecimal retValue= new BigDecimal("0.00");
    	if(selectedStr==null||"".equals(selectedStr)) return retValue;
    	
    	String[] tempArr=selectedStr.split(",");
    	for(int count=0;count<tempArr.length;count++){
    		retValue=retValue.add(new BigDecimal(request.getParamValue(tempArr[0].replace(documentId,repalceid))));
    	}
    	return retValue;
    }
    /**
	 * Function：获得索赔抵扣列表
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-23
	 */
	public List<DeductDetailListBean> getOldPartDeductInfoList(Map params,Integer itemType){
		String deduct_id=ClaimTools.dealParamStr(params.get("deduct_id"));//参数--抵扣单主键
		StringBuffer sqlStr= new StringBuffer();
		if(itemType.equals(Constant.PRE_AUTH_ITEM_02)){//配件
			sqlStr.append("select tawdd.id deduct_id,tawd.claim_id,tawdd.item_code,tawdd.item_name,rd.price balance_amount,rd.deductible_price deduct_amount,tawdd.deduct_money,tawdd.deduct_reason,tawdd.remark\n" );
			sqlStr.append("from tt_as_wr_deduct tawd,tt_as_wr_deduct_detail tawdd,Tt_As_Wr_Old_Returned_Detail rd where tawd.id = tawdd.deduct_id and rd.id = tawdd.part_id and tawdd.item_type = 10871002 and tawd.id = 4012092740266336\n" );
			/*sqlStr.append("select tawdd.id deduct_id,tawd.claim_id,\n" );
			sqlStr.append("tawdd.item_code,tawdd.item_name,\n" );
			sqlStr.append("tawp.balance_amount,tawp.deduct_amount,tawdd.deduct_money,\n" );
			sqlStr.append("tawdd.deduct_reason,tawdd.remark\n" );
			sqlStr.append("from tt_as_wr_deduct tawd,tt_as_wr_deduct_detail tawdd,\n" );
			sqlStr.append("tt_as_wr_application tawa,\n" );
			sqlStr.append("tt_as_wr_partsitem tawp\n" );
			sqlStr.append("where tawd.id=tawdd.deduct_id and tawd.claim_id=tawa.id\n" );
			sqlStr.append("and tawa.id=tawp.id and tawdd.item_code=tawp.part_code\n" );
			sqlStr.append("and tawdd.item_type="+itemType+"\n" );
			sqlStr.append("and tawd.id="+deduct_id+"\n");*/
			sqlStr.append("order by tawd.create_date");
		}else if(itemType.equals(Constant.PRE_AUTH_ITEM_01)){//工时
			sqlStr.append("select tawdd.id deduct_id,tawd.claim_id,\n" );
			sqlStr.append("tawdd.item_code,tawdd.item_name,\n" );
			sqlStr.append("tawp.balance_amount,tawp.deduct_amount,tawdd.deduct_money,\n" );
			sqlStr.append("tawdd.deduct_reason,tawdd.remark\n" );
			sqlStr.append("from tt_as_wr_deduct tawd,tt_as_wr_deduct_detail tawdd,\n" );
			sqlStr.append("tt_as_wr_application tawa,\n" );
			sqlStr.append("TT_AS_WR_LABOURITEM tawp\n" );
			sqlStr.append("where tawd.id=tawdd.deduct_id and tawd.claim_id=tawa.id\n" );
			sqlStr.append("and tawa.id=tawp.id and tawdd.item_code=tawp.wr_labourcode\n" );
			sqlStr.append("and tawdd.item_type="+itemType+"\n" );
			sqlStr.append("and tawd.id="+deduct_id+"\n");
			sqlStr.append("order by tawd.create_date");
		}else if(itemType.equals(Constant.PRE_AUTH_ITEM_03)){//其他项目
			sqlStr.append("select tawdd.id deduct_id,tawd.claim_id,\n" );
			sqlStr.append("tawdd.item_code,tawdd.item_name,\n" );
			sqlStr.append("tawp.balance_amount,tawp.deduct_amount,tawdd.deduct_money,\n" );
			sqlStr.append("tawdd.deduct_reason,tawdd.remark\n" );
			sqlStr.append("from tt_as_wr_deduct tawd,tt_as_wr_deduct_detail tawdd,\n" );
			sqlStr.append("tt_as_wr_application tawa,\n" );
			sqlStr.append("TT_AS_WR_NETITEM tawp\n" );
			sqlStr.append("where tawd.id=tawdd.deduct_id and tawd.claim_id=tawa.id\n" );
			sqlStr.append("and tawa.id=tawp.id and tawdd.item_code=tawp.item_code\n" );
			sqlStr.append("and tawdd.item_type="+itemType+"\n" );
			sqlStr.append("and tawd.id="+deduct_id+"\n");
			sqlStr.append("order by tawd.create_date");
		}else{
			return null;
		}
		
		

		List<DeductDetailListBean> list = select(DeductDetailListBean.class,sqlStr.toString(),null);
		
		return list;
	}
	/**
     * Function：通过抵扣主键获得抵扣信息
     * @param  ：	
     * @return:		@param params
     * @return:		@param curPage
     * @return:		@param pageSize
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-6-26
     */
    public PageResult<DeductClaimInfoListBean> getClaimDeductOrdInfo(Map params,int curPage, int pageSize){
    	String deduct_id=ClaimTools.dealParamStr(params.get("deduct_id"));//参数--抵扣主键
    	
    	StringBuffer sqlStr= new StringBuffer();
    	sqlStr.append("select tawd.id deduct_id,tawd.deduct_no,tawd.claim_id,tawa.claim_no,tawa.vin,\n" );
    	sqlStr.append("tawd.part_amount,tawd.deduct_amount,\n" );
    	sqlStr.append("tawd.material_money part_deduct_money,\n" );
    	sqlStr.append("tawd.manhour_money hour_deduct_money,\n" );
    	sqlStr.append("tawd.other_money other_deduct_money\n" );
    	sqlStr.append("from tt_as_wr_deduct tawd,tt_as_wr_application tawa\n" );
    	sqlStr.append("where tawd.claim_id=tawa.id\n" );
    	sqlStr.append("and tawd.id="+deduct_id+"\n" );
    	//sqlStr.append("group by tawd.id,tawd.deduct_no,tawa.claim_no,tawd.part_amount,\n" );
    	//sqlStr.append("tawd.deduct_amount,tawa.vin,tawd.claim_id\n" );
    	sqlStr.append("order by tawd.create_date desc");


    	PageResult<DeductClaimInfoListBean> pr=pageQuery(DeductClaimInfoListBean.class, sqlStr.toString(), null, pageSize, curPage);
    	
    	return pr;
    }
    /**
	 * Function：抵扣维护--抵扣修改操作
	 * @param  ：	
	 * @return:		@param request 当前request对象
	 * @return:		@param user_id 当前操作人id
	 * @return:		@return Map 装载抵扣操作的结果信息
	 *                      retCode返回success代表成功
	 *                      
	 *                      retCode错误代码列表
	 *                      failure_999 没有权限;
	 *                      failure_201 页面获得抵扣编号参数失败;
	 *                      failure_301 获得索赔配件信息不唯一;failure_302 没找到抵扣配件信息;failure_303 修改索赔配件表抵扣金额失败
	 *                      failure_401 获得索赔工时信息不唯一;failure_402 没找到抵扣工时信息;failure_403 修改索赔工时表抵扣金额失败
	 *                      failure_501 获得索赔其他项目信息不唯一;failure_502 没找到抵扣其他项目信息;failure_503 修改索赔其他项目表抵扣金额失败
	 *                      failure_601 查找抵扣信息失败;
	 *                      
	 *                      failure_item_code装载着操作失败的项目代码
	 * @throw：	
	 * LastUpdate：	2010-6-28 ZhaoLunDa
	 */
	public Map deductMaintainDataOper(RequestWrapper request,Long user_id){
		Map retMap=new HashMap();
		int totalUpdateNum=0;
		int updateSuccess=0;
		String deduct_id=request.getParamValue("deduct_id");//获得抵扣id
		String deduct_part_select_str=request.getParamValue("deduct_part_select_str");//获得选中的配件抵扣id串
		String deduct_hour_select_str=request.getParamValue("deduct_hour_select_str");//获得选中的工时抵扣id串
		String deduct_other_select_str=request.getParamValue("deduct_other_select_str");//获得选中的其他项目抵扣id串
		BigDecimal part_total_amount=new BigDecimal("0.00");
		BigDecimal hour_total_amount=new BigDecimal("0.00");
		BigDecimal other_total_amount=new BigDecimal("0.00");
		
		BigDecimal part_history_amount=new BigDecimal("0.00");
		BigDecimal hour_history_amount=new BigDecimal("0.00");
		BigDecimal other_history_amount=new BigDecimal("0.00");
		//检查当前登录人是否有抵扣权限
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select person_code from tc_user where user_id="+user_id);
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		if(temp==null||"".equals(temp)){
			retMap.put("retCode", "failure_999");
			return retMap;//返回错误代码--当前登录人没有抵扣权限
		}
		//检查参数
		if("".equals(deduct_id)){
			retMap.put("retCode", "failure_201");
			return retMap;//返回错误代码--获得抵扣编号失败
		}
		//根据前台传来的配件拼接串，循环更新配件抵扣信息，同时更新相应的索赔配件信息中的抵扣金额信息
		if(deduct_part_select_str!=null&&!"".equals(deduct_part_select_str)){//配件
			part_total_amount=getTotalMoney(request,deduct_part_select_str,"deduct_part_id","deduct_part_money");//获得抵扣配件总金额
			part_history_amount=getTotalMoney(request,deduct_part_select_str,"deduct_part_id","deducted_part_money");//获得配件历史抵扣总金额
			String[] strArr=deduct_part_select_str.split(",");
			totalUpdateNum+=strArr.length;
			for(int count=0;count<strArr.length;count++){
				String selectedId=strArr[count];
				//抵扣前检查该配件抵扣是否超过结算额
				sqlStr.delete(0,sqlStr.length());
				sqlStr.append("select count(1) from tt_as_wr_partsitem a,tt_as_wr_deduct_detail b\n" );
				sqlStr.append("where a.id=b.claim_id and a.part_code=b.item_code\n" );
				sqlStr.append("and b.id="+Long.parseLong(selectedId.replace("deduct_part_id","")));
				List list=pageQuery(sqlStr.toString(), null, getFunName());
		        String countStr=list.get(0).toString();
		        countStr=countStr.substring(countStr.indexOf("="),countStr.indexOf("}")).replace("=","");
		        if(!"1".equals(countStr)){//返回不为1则说明对应的索赔配件不存在或者多条记录
		        	retMap.put("retCode", "failure_301");//查询索赔配件信息不唯一
		        	return retMap;
		        }
		        TtAsWrDeductDetailPO qryInfo=new TtAsWrDeductDetailPO();
		        sqlStr.delete(0,sqlStr.length());
				sqlStr.append("select * from tt_as_wr_deduct_detail\n");
				sqlStr.append("where id="+Long.parseLong(selectedId.replace("deduct_part_id",""))+"\n");
				PageResult<TtAsWrDeductDetailPO> pr=pageQuery(TtAsWrDeductDetailPO.class, sqlStr.toString()
						, null, 1, 1);
				if(pr!=null&&pr.getTotalRecords()==1){
					qryInfo=pr.getRecords().get(0);
				}else{
					retMap.put("retCode", "failure_302");//没找到抵扣配件信息
		        	return retMap;
				}
				//修改索赔配件信息表的抵扣金额信息
		        String part_deduct_money=request.getParamValue(strArr[count].replace("id", "money"));
		        sqlStr.delete(0,sqlStr.length());
				sqlStr.append("update tt_as_wr_partsitem u \n");
				sqlStr.append("set u.deduct_amount=u.deduct_amount-"+qryInfo.getDeductMoney()+"+"+part_deduct_money+"\n");
				sqlStr.append("where u.id="+qryInfo.getClaimId()+" and u.part_code='"+qryInfo.getItemCode()+"'\n");
				int checkUpdateNum=super.update(sqlStr.toString(), null);
				if(checkUpdateNum!=1){
					retMap.put("retCode", "failure_303");//修改索赔配件抵扣金额失败
					retMap.put("failure_item_code", qryInfo.getItemCode());//将失败的配件代码显示给前台
		        	return retMap;
				}
				TtAsWrDeductDetailPO updateObj=new TtAsWrDeductDetailPO();
				updateObj.setId(Long.parseLong(selectedId.replace("deduct_part_id","")));
				//获得配件抵扣钱数、原因、备注
				TtAsWrDeductDetailPO vo=new TtAsWrDeductDetailPO();
				vo.setDeductMoney(Double.parseDouble(part_deduct_money));
				vo.setDeductReason(Integer.parseInt(request.getParamValue(strArr[count].replace("deduct_part_id", "back_type"))));
				vo.setRemark(request.getParamValue(strArr[count].replace("id", "remark")));
				vo.setUpdateBy(user_id);
				vo.setUpdateDate(new Date());
				//修改
				updateSuccess+=update(updateObj, vo);
			}
		}
		//根据前台传来的工时拼接串，循环更新工时抵扣信息，同时更新相应的索赔工时信息中的抵扣金额信息
		if(deduct_hour_select_str!=null&&!"".equals(deduct_hour_select_str)){//工时
			hour_total_amount=getTotalMoney(request,deduct_hour_select_str,"deduct_hour_id","deduct_hour_money");//获得抵扣工时总金额
			hour_history_amount=getTotalMoney(request,deduct_hour_select_str,"deduct_hour_id","deducted_hour_money");//获得工时历史抵扣总金额
			String[] strArr=deduct_hour_select_str.split(",");
			totalUpdateNum+=strArr.length;
			for(int count=0;count<strArr.length;count++){
				String selectedId=strArr[count];
				//抵扣前检查该配件抵扣是否超过结算额
				sqlStr.delete(0,sqlStr.length());
				sqlStr.append("select count(1) from TT_AS_WR_LABOURITEM a,tt_as_wr_deduct_detail b\n" );
				sqlStr.append("where a.id=b.claim_id and a.wr_labourcode=b.item_code\n" );
				sqlStr.append("and b.id="+Long.parseLong(selectedId.replace("deduct_hour_id","")));
				List list=pageQuery(sqlStr.toString(), null, getFunName());
		        String countStr=list.get(0).toString();
		        countStr=countStr.substring(countStr.indexOf("="),countStr.indexOf("}")).replace("=","");
		        if(!"1".equals(countStr)){//返回不为1则说明对应的索赔工时不存在或者多条记录
		        	retMap.put("retCode", "failure_401");//查询索赔工时信息不唯一
		        	return retMap;
		        }
		        TtAsWrDeductDetailPO qryInfo=new TtAsWrDeductDetailPO();
		        sqlStr.delete(0,sqlStr.length());
				sqlStr.append("select * from tt_as_wr_deduct_detail\n");
				sqlStr.append("where id="+Long.parseLong(selectedId.replace("deduct_hour_id",""))+"\n");
				PageResult<TtAsWrDeductDetailPO> pr=pageQuery(TtAsWrDeductDetailPO.class, sqlStr.toString()
						, null, 1, 1);
				if(pr!=null&&pr.getTotalRecords()==1){
					qryInfo=pr.getRecords().get(0);
				}else{
					retMap.put("retCode", "failure_402");//没找到抵扣工时信息
		        	return retMap;
				}
				//修改索赔配件信息表的抵扣金额信息
		        String part_hour_money=request.getParamValue(strArr[count].replace("id", "money"));
		        sqlStr.delete(0,sqlStr.length());
				sqlStr.append("update TT_AS_WR_LABOURITEM u \n");
				sqlStr.append("set u.deduct_amount=u.deduct_amount-"+qryInfo.getDeductMoney()+"+"+part_hour_money+"\n");
				sqlStr.append("where u.id="+qryInfo.getClaimId()+" and u.wr_labourcode='"+qryInfo.getItemCode()+"'\n");
				int checkUpdateNum=super.update(sqlStr.toString(), null);
				if(checkUpdateNum!=1){
					retMap.put("retCode", "failure_403");//修改索赔工时表抵扣金额失败
					retMap.put("failure_item_code", qryInfo.getItemCode());//将失败的工时代码显示给前台
		        	return retMap;
				}
				TtAsWrDeductDetailPO updateObj=new TtAsWrDeductDetailPO();
				updateObj.setId(Long.parseLong(selectedId.replace("deduct_hour_id","")));
				//获得工时抵扣钱数、原因、备注
				TtAsWrDeductDetailPO vo=new TtAsWrDeductDetailPO();
				vo.setDeductMoney(Double.parseDouble(part_hour_money));
				vo.setUpdateBy(user_id);
				vo.setUpdateDate(new Date());
				//修改
				updateSuccess+=update(updateObj, vo);
			}
		}
		//根据前台传来的其他项目拼接串，循环更新其他项目抵扣信息，同时更新相应的索赔其他项目信息中的抵扣金额信息
		if(deduct_other_select_str!=null&&!"".equals(deduct_other_select_str)){
			other_total_amount=getTotalMoney(request,deduct_other_select_str,"deduct_other_id","deduct_other_money");//获得页面所有抵扣其他项目总金额
			other_history_amount=getTotalMoney(request,deduct_other_select_str,"deduct_other_id","deducted_other_money");//获得其他项目历史抵扣总金额
			String[] strArr=deduct_other_select_str.split(",");
			totalUpdateNum+=strArr.length;
			for(int count=0;count<strArr.length;count++){
				String selectedId=strArr[count];
				//抵扣前检查该配件抵扣是否超过结算额
				sqlStr.delete(0,sqlStr.length());
				sqlStr.append("select count(1) from Tt_As_Wr_Netitem a,tt_as_wr_deduct_detail b\n" );
				sqlStr.append("where a.id=b.claim_id and a.item_code=b.item_code\n" );
				sqlStr.append("and b.id="+Long.parseLong(selectedId.replace("deduct_other_id","")));
				List list=pageQuery(sqlStr.toString(), null, getFunName());
		        String countStr=list.get(0).toString();
		        countStr=countStr.substring(countStr.indexOf("="),countStr.indexOf("}")).replace("=","");
		        if(!"1".equals(countStr)){//返回不为1则说明对应的索赔其他项目不存在或者多条记录
		        	retMap.put("retCode", "failure_501");//查询索赔其他项目信息不唯一
		        	return retMap;
		        }
		        TtAsWrDeductDetailPO qryInfo=new TtAsWrDeductDetailPO();
		        sqlStr.delete(0,sqlStr.length());
				sqlStr.append("select * from tt_as_wr_deduct_detail\n");
				sqlStr.append("where id="+Long.parseLong(selectedId.replace("deduct_other_id",""))+"\n");
				PageResult<TtAsWrDeductDetailPO> pr=pageQuery(TtAsWrDeductDetailPO.class, sqlStr.toString()
						, null, 1, 1);
				if(pr!=null&&pr.getTotalRecords()==1){
					qryInfo=pr.getRecords().get(0);
				}else{
					retMap.put("retCode", "failure_502");//没找到抵扣其他项目信息
		        	return retMap;
				}
				//修改索赔配件信息表的抵扣金额信息
		        String part_hour_money=request.getParamValue(strArr[count].replace("id", "money"));
		        sqlStr.delete(0,sqlStr.length());
				sqlStr.append("update Tt_As_Wr_Netitem u \n");
				sqlStr.append("set u.deduct_amount=u.deduct_amount-"+qryInfo.getDeductMoney()+"+"+part_hour_money+"\n");
				sqlStr.append("where u.id="+qryInfo.getClaimId()+" and u.item_code='"+qryInfo.getItemCode()+"'\n");
				int checkUpdateNum=super.update(sqlStr.toString(), null);
				if(checkUpdateNum!=1){
					retMap.put("retCode", "failure_503");//修改索赔其他项目表抵扣金额失败
					retMap.put("failure_item_code", qryInfo.getItemCode());//将失败的工时代码显示给前台
		        	return retMap;
				}
				TtAsWrDeductDetailPO updateObj=new TtAsWrDeductDetailPO();
				updateObj.setId(Long.parseLong(selectedId.replace("deduct_other_id","")));
				//获得其他项目抵扣钱数、原因、备注
				TtAsWrDeductDetailPO vo=new TtAsWrDeductDetailPO();
				vo.setDeductMoney(Double.parseDouble(request.getParamValue(strArr[count].replace("id", "money"))));
				vo.setUpdateBy(user_id);
				vo.setUpdateDate(new Date());
				//修改
				updateSuccess+=update(updateObj, vo);
			}
		}
		//根据抵扣id修改抵扣表中的信息
		sqlStr.delete(0,sqlStr.length());
		sqlStr.append("select * from tt_as_wr_deduct where id="+deduct_id);
        PageResult<TtAsWrDeductPO> pr=pageQuery(TtAsWrDeductPO.class,sqlStr.toString(),null, 10, 1);
        if(pr!=null&&pr.getTotalRecords()!=1){
        	retMap.put("retCode", "failure_601");//查找抵扣信息失败
			return retMap;
        }
        TtAsWrDeductPO queryObj=(TtAsWrDeductPO)pr.getRecords().get(0);
		TtAsWrDeductPO updateObj=new TtAsWrDeductPO();
		updateObj.setId(Long.parseLong(deduct_id));
		totalUpdateNum+=1;
		TtAsWrDeductPO vo=new TtAsWrDeductPO();
		vo.setMaterialMoney(queryObj.getMaterialMoney()-part_history_amount.doubleValue()+part_total_amount.doubleValue());
		vo.setManhourMoney(queryObj.getManhourMoney()-hour_history_amount.doubleValue()+hour_total_amount.doubleValue());
		vo.setOtherMoney( queryObj.getOtherMoney()-other_history_amount.doubleValue()+other_total_amount.doubleValue());
		vo.setUpdateBy(user_id);
		vo.setUpdateDate(new Date());
		updateSuccess+=update(updateObj, vo);
		retMap.put("retCode", "success");
		retMap.put("part_total_amount", queryObj.getMaterialMoney()-part_history_amount.doubleValue()+part_total_amount.doubleValue());
		retMap.put("hour_total_amount", queryObj.getManhourMoney()-hour_history_amount.doubleValue()+hour_total_amount.doubleValue());
		retMap.put("other_total_amount", queryObj.getOtherMoney()-other_history_amount.doubleValue()+other_total_amount.doubleValue());
		return retMap;
		
	}
	/**
	 * Function：删除单条抵扣明细
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-28
	 */
	public String delSingleDeductInfoDataOper(Map params){
		@SuppressWarnings("unused")
		int updateNum=0;
		String deduct_id=ClaimTools.dealParamStr(params.get("deduct_id"));//参数--抵扣主键
		AclUserBean logonUserBean=(AclUserBean)params.get("logonUserBean");
		StringBuffer sqlStr= new StringBuffer();
		if(isBalanceByDeductId(deduct_id)){
			return "failure_001";//该抵扣单已经结算，不能进行取消业务
		}
		if(deduct_id==null||"".equals(deduct_id)){
			return "failure_002";//获取参数失败
		}
		//获取抵扣明细表中索赔配件信息
		
		sqlStr.append("select tawd.part_id,tawd.id,tawd.deduct_id,tawd.claim_id,\n" );
		sqlStr.append("tawd.item_type,tawd.item_code,sum(tawd.deduct_money) deduct_money\n" );
		sqlStr.append("from tt_as_wr_deduct_detail tawd\n" );
		sqlStr.append("where tawd.deduct_id="+deduct_id+"\n" );
		sqlStr.append("group by tawd.part_id,tawd.id,tawd.deduct_id,tawd.claim_id,\n" );
		sqlStr.append("tawd.item_type,tawd.item_code");

		List<DeductDelDetailListBean> detailList=pageQuery(DeductDelDetailListBean.class, sqlStr.toString(), null, 100, 1).getRecords();
		if(detailList==null||detailList.size()<=0){
			return "failure_003";//没查询到抵扣明细信息
		}
		for(int count=0;count<detailList.size();count++){
			DeductDelDetailListBean queryObj=(DeductDelDetailListBean)detailList.get(count);
			//项目类型为配件
			if(Constant.PRE_AUTH_ITEM_02.equals(queryObj.getItem_type())){
				//从索赔配件表中获得抵扣配件信息
				sqlStr.delete(0, sqlStr.length());
				sqlStr.append("select * from tt_as_wr_partsitem\n" );
				sqlStr.append("where id="+queryObj.getClaim_id()+" and part_code='"+queryObj.getItem_code()+"'");
				PageResult<TtAsWrPartsitemPO> pr=pageQuery(TtAsWrPartsitemPO.class, sqlStr.toString(), null, 10, 1);
				
				if(pr!=null&&pr.getTotalRecords()>=1){
					//修改索赔配件表的抵扣金额信息
					TtAsWrPartsitemPO data=new TtAsWrPartsitemPO();
					data=(TtAsWrPartsitemPO)pr.getRecords().get(0);
					sqlStr.delete(0, sqlStr.length());
					//检验该配件是否超过配件表
					sqlStr.append("update tt_as_wr_partsitem set deduct_amount="+
							(data.getDeductAmount()-queryObj.getDeduct_money())+" where id="+data.getId()
							+" and part_code='"+queryObj.getItem_code()+"'");
					updateNum=super.update(sqlStr.toString(), null);
				}
			}
			//项目类型为工时
			if(Constant.PRE_AUTH_ITEM_01.equals(queryObj.getItem_type())){
				//从索赔工时表中获得抵扣配件信息
				sqlStr.delete(0, sqlStr.length());
				sqlStr.append("select * from TT_AS_WR_LABOURITEM\n" );
				sqlStr.append("where id="+queryObj.getClaim_id()+" and wr_labourcode='"+queryObj.getItem_code()+"'");
				PageResult<TtAsWrLabouritemPO> pr=pageQuery(TtAsWrLabouritemPO.class, sqlStr.toString(), null, 10, 1);
				
				if(pr!=null&&pr.getTotalRecords()>=1){
					//修改索赔配件表的抵扣金额信息
					TtAsWrLabouritemPO data=new TtAsWrLabouritemPO();
					data=(TtAsWrLabouritemPO)pr.getRecords().get(0);
					sqlStr.delete(0, sqlStr.length());
					sqlStr.append("update TT_AS_WR_LABOURITEM set deduct_amount="+
							(data.getDeductAmount()-queryObj.getDeduct_money())+" where id="+data.getId()
							+" and wr_labourcode='"+queryObj.getItem_code()+"'");
					updateNum=super.update(sqlStr.toString(), null);
				}
			}
			//项目类型为其他项目
			if(Constant.PRE_AUTH_ITEM_01.equals(queryObj.getItem_type())){
				//从索赔工时表中获得抵扣配件信息
				sqlStr.delete(0, sqlStr.length());
				sqlStr.append("select * from TT_AS_WR_NETITEM\n" );
				sqlStr.append("where id="+queryObj.getClaim_id()+" and item_code='"+queryObj.getItem_code()+"'");
				PageResult<TtAsWrNetitemPO> pr=pageQuery(TtAsWrNetitemPO.class, sqlStr.toString(), null, 10, 1);
				
				if(pr!=null&&pr.getTotalRecords()>=1){
					//修改索赔配件表的抵扣金额信息
					TtAsWrNetitemPO data=new TtAsWrNetitemPO();
					data=(TtAsWrNetitemPO)pr.getRecords().get(0);
					sqlStr.delete(0, sqlStr.length());
					sqlStr.append("update TT_AS_WR_NETITEM set deduct_amount="+
							(data.getDeductAmount()-queryObj.getDeduct_money())+" where id="+data.getId()
							+" and item_code='"+queryObj.getItem_code()+"'");
					updateNum=super.update(sqlStr.toString(), null);
				}
			}
			
			//查询判断是否在10月10日前
			String sql11="select a.in_warhouse_date From Tt_As_Wr_Old_Returned a ,Tt_As_Wr_Old_Returned_Detail b where a.id=b.return_id and b.id="+queryObj.getPart_id();
			
			List<TtAsWrOldReturnedPO> detailList1=pageQuery(TtAsWrOldReturnedPO.class, sql11, null, 100, 1).getRecords();
			
			TtAsWrOldReturnedPO tor=new TtAsWrOldReturnedPO();
			
			try {
				
			
			if(detailList1.size()>0){
			tor=detailList1.get(0);
			
			 
		        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String dateString = formatter.format(tor.getInWarhouseDate());

System.out.println("sqlDate:"+dateString);
			
			String t1="2012-10-18 23:59:59";
				String t2=tor.getInWarhouseDate().toString();
				
		   
		            Calendar c1=Calendar.getInstance();   
		           Calendar c2=Calendar.getInstance();   
		       
		        	          try {
								c1.setTime(formatter.parse(t1));
								c2.setTime(formatter.parse(dateString));   
							} catch (java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}   
		        	               
							int result=c1.compareTo(c2);   

							System.out.println("asdsad:+"+queryObj.getPart_id());
							System.out.println("asdsad:+"+result);
							if(result>0){
								
								
							}
							else{
								//回运单修改为正常并入库
								String sql1="update Tt_As_Wr_Old_Returned_Detail b set b.sign_amount=1,b.deduct_remark='',b.deductible_reason_code='' where b.id="+queryObj.getPart_id();
								
								update(sql1, null);
								
								String sql2="insert into tt_as_wr_barcode_part_stock(id,return_id,part_id,part_name,is_library,barcode_no,create_by,create_date,status,part_code,is_cliam) select f_getid(),c.return_id,c.part_id,c.part_name,10011001,c.barcode_no,"+logonUserBean.getUserId()+",sysdate,10011001,c.part_code,c.is_cliam From Tt_As_Wr_Old_Returned_Detail c where  c.id="+queryObj.getPart_id();
								
								insert(sql2);
							}
							
			}}
			catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
		//修改抵扣主表信息为删除状态
		TtAsWrDeductPO updateObj=new TtAsWrDeductPO();
		updateObj.setId(Long.parseLong(deduct_id));
		
		TtAsWrDeductPO vo=new TtAsWrDeductPO();
		vo.setIsDel(1);//置为删除状态
		vo.setUpdateBy(logonUserBean.getUserId());
		vo.setUpdateDate(new Date());
		updateNum=update(updateObj,vo);
		
		
		
		return "success";
		
		
		
	}
	/**
	 * Function：根据抵扣主键获得是否结算标识
	 * @param  ：	
	 * @return:		@param deduct_id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-28
	 */
	public boolean isBalanceByDeductId(String deduct_id){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select nvl(is_bal,0) is_bal from tt_as_wr_deduct where id="+deduct_id);
        List list=pageQuery(sqlStr.toString(), null, getFunName());
        String temp=list.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
	    if(temp!=null&&!"".equals(temp)){
	    	if("0".equals(temp)){
	    		return false;
	    	}else{
	    		return true;
	    	}
	    }else{
	    	return true;
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
