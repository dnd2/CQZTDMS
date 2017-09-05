package com.infodms.dms.dao.claim.oldPart;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.infodms.dms.bean.DeductBalanceBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 抵扣单结算DAO
 * @author XZM
 */
@SuppressWarnings("unchecked")
public class ClaimOldPartDeductBalanceDao extends BaseDao {
	
   private static final ClaimOldPartDeductBalanceDao dao = null;
	
	/**
	 * 取得DAO实例
	 * @return
	 */
	public static final ClaimOldPartDeductBalanceDao getInstance() {
	   if(dao==null) return new ClaimOldPartDeductBalanceDao();
	   return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 统计指定经销商的抵扣单结算信息
	 * @param conditionBean 统计条件
	 * @param curPage 当前页
	 * @param pageSize 每页中的记录数
	 * @return PageResult<Map<String,Object>>
	 */
	public PageResult<Map<String,Object>> queryDeductBalance(DeductBalanceBean conditionBean,
			Integer curPage,Integer pageSize){
		
		List<Object> paramList = new ArrayList<Object>();
		//初始化经销商过滤条件统计条件
		StringBuilder sBuilder2 = new StringBuilder();
		this.createWhereMap("NOTICE_DATE" ,"<=" ,conditionBean.getLastDay(), sBuilder2,paramList,1,"YYYY-MM-DD HH24:MI:SS","A1");
		this.createWhereMap("IS_DEL" ,"=" ,conditionBean.getIsDel() , sBuilder2,paramList,2,"","A1");
		this.createWhereMap("IS_BAL" ,"=" ,conditionBean.getDeductStatus() , sBuilder2,paramList,2,"","A1");
		this.createWhereMap("YIELDLY" ,"IN" ,conditionBean.getYieldlys() , sBuilder2,paramList,2,"","B1");
		this.createWhereMap("YIELDLY" ,"=" ,conditionBean.getYieldly() , sBuilder2,paramList,2,"","B1");

		StringBuilder sBuilder = new StringBuilder();
		this.createWhereMap("DEALER_CODE" ,"IN" ,conditionBean.getDealerCodes(),sBuilder,paramList,2,"","B");
		this.createWhereMap("DEALER_SHORTNAME" ,"LIKE" ,conditionBean.getDealerName() , sBuilder,paramList,2,"","B");
		
		//统计对应经销商抵扣单信息
		StringBuilder sql1= new StringBuilder();
		sql1.append("SELECT B1.YIELDLY,A1.DEALER_ID,NVL(SUM(A1.MANHOUR_MONEY),0) MANHOUR_MONEY,\n" );
		sql1.append("NVL(SUM(A1.MATERIAL_MONEY),0) MATERIAL_MONEY, NVL(SUM(A1.OTHER_MONEY),0) OTHER_MONEY,\n" );
		sql1.append("MAX(A1.DEDUCT_NO) MAX_NO,MIN(A1.DEDUCT_NO) MIN_NO,COUNT(*) NO_COUNT\n" );
		sql1.append("FROM TT_AS_WR_DEDUCT A1,TT_AS_WR_APPLICATION B1\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A1.CLAIM_ID = B1.ID(+)\n" );
		sql1.append(sBuilder2.toString());//加入抵扣单查询条件
		sql1.append(" GROUP BY B1.YIELDLY,A1.DEALER_ID");

		StringBuilder sql2= new StringBuilder();
		sql2.append("SELECT A.YIELDLY,A.DEALER_ID,MIN_NO||'-'||MAX_NO NOBOUND,B.DEALER_CODE,B.DEALER_SHORTNAME DEALER_NAME,A.MANHOUR_MONEY,\n" );
		sql2.append(" A.MATERIAL_MONEY,A.OTHER_MONEY,(A.MANHOUR_MONEY+A.MATERIAL_MONEY+A.OTHER_MONEY) TOTALMONEY,\n" );
		sql2.append(" A.NO_COUNT\n" );
		sql2.append(" FROM (\n");
		sql2.append(sql1.toString());//内嵌入抵扣单统计信息
		sql2.append(") A,TM_DEALER B\n" );
		sql2.append(" WHERE 1=1\n" );
		sql2.append(" AND A.DEALER_ID = B.DEALER_ID\n");
		sql2.append(sBuilder.toString());//嵌入经销商查询条件
		sql2.append(" ORDER BY A.YIELDLY,A.DEALER_ID");

		PageResult<Map<String,Object>> result = this.pageQuery(sql2.toString(), paramList,
	    		this.getClass().getName()+".queryDeductBalance()", pageSize, curPage);
		
		return result;
	}
	
	public List<Map<String, Object>> newLoadDeductByDealerId(DeductBalanceBean conditionBean){
		
		List<Object> paramList = new ArrayList<Object>();
		//初始化经销商过滤条件统计条件
		StringBuilder sBuilder2 = new StringBuilder();
		this.createWhereMap("NOTICE_DATE" ,"<=" ,conditionBean.getLastDay(), sBuilder2,paramList,1,"YYYY-MM-DD HH24:MI:SS","A1");
		this.createWhereMap("IS_DEL" ,"=" ,conditionBean.getIsDel() , sBuilder2,paramList,2,"","A1");
		this.createWhereMap("IS_BAL" ,"=" ,conditionBean.getDeductStatus() , sBuilder2,paramList,2,"","A1");
		this.createWhereMap("YIELDLY" ,"IN" ,conditionBean.getYieldlys() , sBuilder2,paramList,2,"","B1");
		this.createWhereMap("YIELDLY" ,"=" ,conditionBean.getYieldly() , sBuilder2,paramList,2,"","B1");

		StringBuilder sBuilder = new StringBuilder();
		this.createWhereMap("DEALER_CODE" ,"IN" ,conditionBean.getDealerCodes(),sBuilder,paramList,2,"","B");
		this.createWhereMap("DEALER_SHORTNAME" ,"LIKE" ,conditionBean.getDealerName() , sBuilder,paramList,2,"","B");
		
		//统计对应经销商抵扣单信息
		StringBuilder sql1= new StringBuilder();
		sql1.append("SELECT B1.YIELDLY,A1.DEALER_ID,NVL(SUM(A1.MANHOUR_MONEY),0) MANHOUR_MONEY,\n" );
		sql1.append("NVL(SUM(A1.MATERIAL_MONEY),0) MATERIAL_MONEY, NVL(SUM(A1.OTHER_MONEY),0) OTHER_MONEY,\n" );
		sql1.append("MAX(A1.DEDUCT_NO) MAX_NO,MIN(A1.DEDUCT_NO) MIN_NO,COUNT(*) NO_COUNT\n" );
		sql1.append("FROM TT_AS_WR_DEDUCT A1,TT_AS_WR_APPLICATION B1\n" );
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND A1.CLAIM_ID = B1.ID(+)\n" );
		sql1.append(sBuilder2.toString());//加入抵扣单查询条件
		sql1.append("GROUP BY B1.YIELDLY,A1.DEALER_ID");

		StringBuilder sql2= new StringBuilder();
		sql2.append("SELECT A.YIELDLY,A.DEALER_ID,MIN_NO||'-'||MAX_NO NOBOUND,B.DEALER_CODE,B.DEALER_SHORTNAME DEALER_NAME,A.MANHOUR_MONEY,\n" );
		sql2.append(" A.MATERIAL_MONEY,A.OTHER_MONEY,(A.MANHOUR_MONEY+A.MATERIAL_MONEY+A.OTHER_MONEY) TOTALMONEY,\n" );
		sql2.append(" A.NO_COUNT\n" );
		sql2.append(" FROM (\n");
		sql2.append(sql1.toString());//内嵌入抵扣单统计信息
		sql2.append(") A,TM_DEALER B\n" );
		sql2.append(" WHERE 1=1\n" );
		sql2.append(" AND A.DEALER_ID = B.DEALER_ID\n");
		sql2.append(sBuilder.toString());//嵌入经销商查询条件
		sql2.append(" ORDER BY A.YIELDLY,A.DEALER_ID");
		
		List<Map<String, Object>> list = pageQuery(sql2.toString(), paramList, getFunName());
		return list;
	}
	
	/**
	 * 查询存在未结算抵扣单的经销商信息，经销商状态为有效
	 * @param conditionBean （经销商代码集合，名称）
	 * @return
	 */
	public List<TmDealerPO> loadTmDealer(DeductBalanceBean conditionBean){
		
		List<Object> paramList = new ArrayList<Object> ();
		
		//初始化经销商过滤条件统计条件
		StringBuilder sBuilder2 = new StringBuilder();
		this.createWhereMap("NOTICE_DATE" ,"<=" ,conditionBean.getLastDay(), sBuilder2,paramList,1,"YYYY-MM-DD","B");

		StringBuilder sBuilder = new StringBuilder();
		this.createWhereMap("DEALER_CODE" ,"IN" ,conditionBean.getDealerCodes(),sBuilder,paramList,2,"","A");
		this.createWhereMap("DEALER_SHORTNAME" ,"LIKE" ,conditionBean.getDealerName() , sBuilder,paramList,2,"","A");
		
		StringBuilder sqlTemp= new StringBuilder();
		sqlTemp.append("SELECT *\n" );
		sqlTemp.append(" FROM TM_DEALER A\n" );
		sqlTemp.append(" WHERE 1=1\n" );
		sqlTemp.append(" AND EXISTS (SELECT B.ID\n" );
		sqlTemp.append(" FROM TT_AS_WR_DEDUCT B\n" );
		sqlTemp.append(" WHERE B.DEALER_ID = A.DEALER_ID\n" );
		sqlTemp.append(sBuilder2.toString());//限制抵扣单日期
		sqlTemp.append(" AND B.IS_BAL = 0" );//未结算的抵扣单
		sqlTemp.append(" AND B.IS_DEL = 0)");//未删除的抵扣单
		sqlTemp.append(" AND A.STATUS = ").append(Constant.STATUS_ENABLE);//经销商状态有效
		
		String sql = sqlTemp.toString() + sBuilder.toString();
		
		return this.select(TmDealerPO.class, sql, paramList);
	}
	
	/**
	 * 根据经销商ID统计对应抵扣单信息
	 * @param conditionBean 查询条件
	 * @param dealerId 经销商ID
	 * @return List<Map<String,Object>>
	 *         MAP信息：DEALER_ID[经销商ID]、CLAIM_ID[索赔单ID]、MANHOUR_MONEY[工时抵扣金额]、
	 *                  MATERIAL_MONEY[配件抵扣金额]、OTHER_MONEY[其他项目抵扣金额]、TOTAL_MONEY[总金额]
	 *                  ISCLAIM[0为按索赔单统计]、ISID[0为按抵扣单统计]
	 *                  MAX_NO[最大抵扣单号]、MIN_NO[最小抵扣单号]
	 */
	public List<Map<String,Object>> loadDeductByDealerId(DeductBalanceBean conditionBean,Long dealerId){
		
		List<Map<String,Object>> resultList = null;
		List<Object> paramsList = new ArrayList<Object>();
		StringBuilder sBuilder = new StringBuilder();
		
		if(dealerId==null){
			return resultList;
		}
		
		paramsList.add(dealerId);
		
		this.createWhereMap("NOTICE_DATE" ,"<=" ,conditionBean.getLastDay(), sBuilder,paramsList,1,"YYYY-MM-DD","A1");
		this.createWhereMap("YIELDLY" ,"=" ,conditionBean.getYieldly(), sBuilder,paramsList,2,"","B1");
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT B1.YIELDLY,A1.CLAIM_ID,A1.ID,NVL(SUM(A1.MANHOUR_MONEY),0) MANHOUR_MONEY,NVL(SUM(A1.MATERIAL_MONEY),0) MATERIAL_MONEY,\n" );
		sql.append("NVL(SUM(A1.OTHER_MONEY),0) OTHER_MONEY,NVL(SUM(A1.MANHOUR_MONEY+A1.MATERIAL_MONEY+A1.OTHER_MONEY),0) TOTAL_MONEY,\n" );
		sql.append("GROUPING(A1.CLAIM_ID) ISCLAIM,GROUPING(A1.ID) ISID,GROUPING(B1.YIELDLY) ISYIELDLY,MAX(A1.DEDUCT_NO) MAX_NO,MIN(A1.DEDUCT_NO) MIN_NO\n" );
		sql.append("FROM TT_AS_WR_DEDUCT A1,TT_AS_WR_APPLICATION B1\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A1.DEALER_ID = ?\n" );
		sql.append("AND A1.CLAIM_ID = B1.ID(+)\n" );
		sql.append("AND A1.IS_BAL=0\n" );
		sql.append("AND A1.IS_DEL=0\n" );
		sql.append(sBuilder.toString());
		sql.append("GROUP BY CUBE(B1.YIELDLY,A1.CLAIM_ID,A1.ID)");

		
		resultList = this.pageQuery(sql.toString(), paramsList, this.getFunName());
		return resultList;
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
	
	public List<Map<String, Object>> getTMList(String dealerId, String yieldly, String lastdate, String status){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.ID\n" );
		sql.append("FROM TT_AS_WR_DEDUCT A, TT_AS_WR_APPLICATION B\n" );
		sql.append("WHERE A.CLAIM_ID = B.ID(+)\n" );
		sql.append("AND A.DEALER_ID = ").append(dealerId).append("\n");
		sql.append("AND B.YIELDLY = ").append(yieldly).append("\n");
		sql.append("AND A.NOTICE_DATE <= TO_DATE('").append(lastdate).append("','YYYY-MM-DD HH24:MI:SS')\n" );
		sql.append("AND A.IS_BAL = ").append(status).append("\n");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");

		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
