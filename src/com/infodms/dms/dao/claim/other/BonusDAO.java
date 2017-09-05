package com.infodms.dms.dao.claim.other;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrFinePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class BonusDAO extends BaseDao{
	
	
	
	public static Logger logger = Logger.getLogger(BonusDAO.class);
    private static final BonusDAO dao = null;
	
	public static final BonusDAO getInstance() {
	   if(dao==null) return new BonusDAO();
	   return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 查询经销商
	 * 注：加入经销商级别限制，只查询一级经销商
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryDealer(Map<String,String> map,int pageSize,int curPage) {
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from tm_dealer where 1=1 ");
		sqlStr.append(" AND OEM_COMPANY_ID =");
		sqlStr.append(map.get("oemCompanyId"));
		sqlStr.append(" AND STATUS ="+Constant.STATUS_ENABLE+"\n");
		sqlStr.append(" AND DEALER_TYPE =");
		sqlStr.append(Constant.DEALER_TYPE_DWR);
		sqlStr.append("\n");
		if (Utility.testString(map.get("dealerCode"))){
			DaoFactory.getsql(sqlStr, "DEALER_CODE", map.get("dealerCode"), 6);
		}
		if (Utility.testString(map.get("dealerName"))){
			sqlStr.append(" and  dealer_name like '%"+map.get("dealerName")+"%' ");
		}
		sqlStr.append(" ORDER BY DEALER_ID DESC ");
		PageResult<Map<String, Object>> ps = this.pageQuery(sqlStr.toString(), null, this.getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 导出查询
	 * 注：加入经销商级别限制，只查询一级经销商
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryOemDealer(Map<String,String> map,int pageSize,int curPage) {
		StringBuffer sqlStr=new StringBuffer();
		List params = new LinkedList();
		sqlStr.append("select a.DEALER_CODE,A.DEALER_NAME,b.LABOUR_BH , to_char(b.fine_date,'yyyy-MM-dd') fine_date, b.FINE_TYPE,b.datum_sum,b.labour_sum,b.REMARK,b.FINE_REASON from tm_dealer a,TT_AS_WR_FINE b where a.DEALER_ID=b.DEALER_ID ");
		sqlStr.append(" AND a.OEM_COMPANY_ID =");
		sqlStr.append(map.get("oemCompanyId"));
//		sqlStr.append(" AND DEALER_LEVEL = " +Constant.DEALER_LEVEL_01+ "\n");
		sqlStr.append(" AND a.STATUS ="+Constant.STATUS_ENABLE+"\n");
		sqlStr.append(" AND a.DEALER_TYPE =");
		sqlStr.append(Constant.DEALER_TYPE_DWR);
		sqlStr.append("\n");

		if (null != map.get("dealerCode") && !"".equals(map.get("dealerCode"))) {
			String[] array = map.get("dealerCode").split(",");
			sqlStr.append("   AND a.DEALER_CODE IN ( \n");
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
		
		if (Utility.testString(map.get("deductStartDate"))) {//提报开始日期
			sqlStr.append("   AND b.fine_date >= TO_DATE('").append(map.get("deductStartDate").trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(map.get("deductEndDate"))) {//提报结束日期
			sqlStr.append("   AND b.fine_date <= TO_DATE('").append(map.get("deductEndDate").trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(map.get("dealerName"))){
			sqlStr.append(" and  a.dealer_name like '%"+map.get("dealerName")+"%' ");
		}
		if (Utility.testString(map.get("ButieBh"))){
			sqlStr.append(" and  b.LABOUR_BH like '%"+map.get("ButieBh")+"%' ");
		}
		if (Utility.testString(map.get("SubsidiesType"))){
			sqlStr.append(" and  b.REMARK like '%"+map.get("SubsidiesType")+"%' "); //补贴类型
		}
		sqlStr.append(" ORDER BY a.DEALER_ID DESC ");
		PageResult<Map<String, Object>> ps= pageQuery(sqlStr.toString(), null, getFunName(),pageSize, curPage);
		return ps;
	}
	
	/**
	 * excel导出查询
	 * 注：加入经销商级别限制，只查询一级经销商
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> exportqueryOemDealer(Map<String,String> map,int pageSize,int curPage) {
		StringBuffer sqlStr=new StringBuffer();
		List params = new LinkedList();
		sqlStr.append("select\n" );
		sqlStr.append("       t.name,\n" );
		sqlStr.append("       tba.area_name,\n" );
		sqlStr.append("       a.DEALER_CODE,\n" );
		sqlStr.append("       b.DEALER_NAME,\n" );
		sqlStr.append("       to_char(b.fine_date, 'yyyy-MM-dd') fine_date,\n" );
		sqlStr.append("       b.FINE_TYPE,\n" );
		sqlStr.append("       b.datum_sum,\n" );
		sqlStr.append("       b.labour_sum,\n" );
		sqlStr.append("       b.REMARK,\n" );
		sqlStr.append("       b.labour_bh,\n" );
		sqlStr.append("       b.balance_oder,\n" );
		sqlStr.append("       b.FINE_REASON\n" );
		sqlStr.append("  from tm_dealer a, TT_AS_WR_FINE b,tc_user t,tm_business_area tba\n" );
		sqlStr.append(" where a.DEALER_ID = b.DEALER_ID\n" );
		sqlStr.append(" and t.user_id=b.create_by\n" );
		sqlStr.append(" and tba.area_id=b.yieldly");
		sqlStr.append(" AND a.OEM_COMPANY_ID =");
		sqlStr.append(map.get("oemCompanyId"));
//		sqlStr.append(" AND DEALER_LEVEL = " +Constant.DEALER_LEVEL_01+ "\n");
		sqlStr.append(" AND a.STATUS ="+Constant.STATUS_ENABLE+"\n");
		sqlStr.append(" AND a.DEALER_TYPE =");
		sqlStr.append(Constant.DEALER_TYPE_DWR);
		sqlStr.append("\n");

		if (null != map.get("dealerCode") && !"".equals(map.get("dealerCode"))) {
			String[] array = map.get("dealerCode").split(",");
			sqlStr.append("   AND a.DEALER_CODE IN ( \n");
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
		
		if (Utility.testString(map.get("deductStartDate"))) {//提报开始日期
			sqlStr.append("   AND b.fine_date >= TO_DATE('").append(map.get("deductStartDate").trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(map.get("deductEndDate"))) {//提报结束日期
			sqlStr.append("   AND b.fine_date <= TO_DATE('").append(map.get("deductEndDate").trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(map.get("dealerName"))){
			sqlStr.append(" and  b.dealer_name like '%"+map.get("dealerName")+"%' ");
		}
		if (Utility.testString(map.get("ButieBh"))){
			sqlStr.append(" and  b.LABOUR_BH like '%"+map.get("ButieBh")+"%' ");
		}
		if (Utility.testString(map.get("SubsidiesType"))){
			sqlStr.append(" and  b.REMARK like '%"+map.get("SubsidiesType")+"%' "); //补贴类型
		}
		sqlStr.append(" ORDER BY a.DEALER_ID DESC ");
		PageResult<Map<String, Object>> ps= pageQuery(sqlStr.toString(), null, getFunName(),pageSize, curPage);
		return ps;
	}
	/**
	 * 
	* @Title: punishQuery 
	* @Description: TODO(处罚历史查询) 
	* @param @param dealerId
	* @param @param pageSize
	* @param @param curPage
	* @param @return    设定文件 
	* @return PageResult<TtAsWrFinePO>    返回类型 
	* @throws
	 */
	public PageResult<Map<String,Object>> punishQuery(String dealerId,
			String deductStartDate,String deductEndDate,String status,String yieldly,
			int pageSize,int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.fine_id,\n");
		sql.append("       a.dealer_id,\n");
		sql.append("       (select b.dealer_code from tm_dealer b where b.dealer_id=a.dealer_id) as dealer_code,\n");
		sql.append("       (select b.dealer_code from tm_dealer b where b.dealer_id=a.dealer_id) as dealer_name,\n");
		sql.append("       a.fine_sum,\n");
		sql.append("       B.AREA_NAME AS AREA_NAME,\n");
		sql.append("       a.fine_date,\n");
		sql.append("       a.fine_reason,\n");
		sql.append("       a.remark,\n");
		sql.append("       a.create_by,\n");
		sql.append("       TO_CHAR(a.CREATE_DATE, 'YYYY-MM-dd') AS CREATE_DATE,\n");
		sql.append("       a.pay_status,\n");
		sql.append("       a.pay_date,\n");
		sql.append("       a.claimbalance_id,\n");
		sql.append("  a.datum_sum,  ");
		sql.append("  a.labour_sum,");
		sql.append("       a.yieldly,A.FINE_TYPE\n");
		sql.append("  from tt_as_wr_fine a , Tm_Business_Area B\n");
		sql.append(" where a.dealer_id = "+dealerId+"\n");
		sql.append("  AND a.YIELDLY = B.AREA_ID");
		if (Utility.testString(deductStartDate)) {//提报开始日期
			sql.append("   AND a.pay_date >= TO_DATE('").append(deductStartDate.trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(deductEndDate)) {//提报结束日期
			sql.append("   AND a.pay_date <= TO_DATE('").append(deductEndDate.trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(status)) {
			sql.append("  AND a.pay_status="+status+" \n");
		}
		if (Utility.testString(yieldly)) {
			sql.append("  AND a.yieldly="+yieldly+" \n");
		}
		sql.append(" order by a.create_date desc");
		PageResult<Map<String,Object>> ps = this.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public List<Map<String,Object>> Queryg() {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.fine_id,\n");
		sql.append("       a.dealer_id,\n");
		sql.append("       (select b.dealer_code from tm_dealer b where b.dealer_id=a.dealer_id) as dealer_code,\n");
		sql.append("       (select b.dealer_code from tm_dealer b where b.dealer_id=a.dealer_id) as dealer_name,\n");
		sql.append("       a.fine_sum,\n");
		sql.append("       B.AREA_NAME AS AREA_NAME,\n");
		sql.append("       a.fine_date,\n");
		sql.append("       a.fine_reason,\n");
		sql.append("       a.remark,\n");
		sql.append("       a.create_by,\n");
		sql.append("       TO_CHAR(a.CREATE_DATE, 'YYYY-MM-dd') AS CREATE_DATE,\n");
		sql.append("       a.pay_status,\n");
		sql.append("       a.pay_date,\n");
		sql.append("       a.claimbalance_id,\n");
		sql.append("  a.datum_sum,  ");
		sql.append("  a.labour_sum,");
		sql.append("       a.yieldly,A.FINE_TYPE\n");
		sql.append("  from tt_as_wr_fine a , Tm_Business_Area B\n");
		sql.append(" where a.CLAIMBALANCE_ID is not null\n");
		sql.append("  AND a.YIELDLY = B.AREA_ID");
		sql.append(" order by a.create_date desc");
		List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	* @Title: punishQuery 
	* @Description: TODO(查询一条首页新闻) 
	* @param @param dealerId
	* @param @param pageSize
	* @param @param curPage
	* @param @return    设定文件 
	* @return PageResult<TtAsWrFinePO>    返回类型 
	* @throws
	 */
	public Map<String,Object> queryNewsById(String newsId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.NEWS_CODE,A.NEWS_TITLE,A.NEWS_ID FROM tt_as_wr_news A WHERE A.NEWS_ID="+newsId+"\n");

		Map<String,Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	/**
	 * 经销商查询正负激励
	 * 注：加入经销商级别限制，只查询一级经销商
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<TmDealerPO> dlrQueryEncourage(Map<String,String> map,int pageSize,int curPage) {
		StringBuffer sqlStr=new StringBuffer();
		List params = new LinkedList();
		sqlStr.append("select * from tm_dealer where 1=1 ");
		sqlStr.append(" AND OEM_COMPANY_ID =");
		sqlStr.append(map.get("oemCompanyId"));
		sqlStr.append(" AND DEALER_LEVEL = " +Constant.DEALER_LEVEL_01+ "\n");
		sqlStr.append(" AND DEALER_TYPE =");
		sqlStr.append(Constant.DEALER_TYPE_DWR);
		sqlStr.append(" AND DEALER_ID IN ("+map.get("dealerId")+")");
		sqlStr.append("\n");
		if (Utility.testString(map.get("dealerCode"))){
			sqlStr.append(Utility.getConSqlByParamForEqual(map.get("dealerCode"), params, "tm_dealer", "dealer_code"));
		}
		if (Utility.testString(map.get("dealerName"))){
			sqlStr.append(" and  dealer_name like '%"+map.get("dealerName")+"%' ");
		}
		sqlStr.append(" ORDER BY DEALER_ID DESC ");
		PageResult<TmDealerPO> ps = pageQuery(TmDealerPO.class,sqlStr.toString(),params,pageSize,curPage);
		return ps;
	}
	/**
	 * 经销商查询正负激励明细
	 * 注：加入经销商级别限制，只查询一级经销商
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public Map<String,Object> dlrQueryEncourageDetail(String fineId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.FINE_ID,\n");
		sql.append("       A.DEALER_ID,\n");
		sql.append("       A.FINE_TYPE,\n");
		sql.append("       (SELECT B.DEALER_CODE FROM TM_DEALER B WHERE A.DEALER_ID=B.DEALER_ID) AS DEALER_CODE,\n");
		sql.append("       (SELECT B.DEALER_NAME FROM TM_DEALER B WHERE A.DEALER_ID=B.DEALER_ID) AS DEALER_NAME,\n");
		sql.append("       A.FINE_SUM,\n");
		sql.append("       A.FINE_DATE,\n");
		sql.append("       B.AREA_NAME,\n");
		sql.append("       A.FINE_REASON,\n");
		sql.append("       A.REMARK,\n");
		sql.append("       A.LABOUR_BH,\n");
		sql.append("  A.datum_sum, ");
		sql.append("  A.labour_sum,   ");
		sql.append("       A.PAY_STATUS,\n");
		sql.append("       (select y.name from tc_user y where y.user_id=A.CREATE_BY) AS CREATE_BY,\n");
		sql.append("       A.PAY_DATE,\n");
		sql.append("       A.YIELDLY,\n");
		sql.append("       A.fine_type\n");
		sql.append("  FROM TT_AS_WR_FINE A , Tm_Business_Area B \n");
		sql.append(" WHERE A.FINE_ID = "+fineId+"\n");
		sql.append("  AND A.YIELDLY = B.AREA_ID");
		Map<String,Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 经销商查询正负激励新闻明细
	 * 注：加入经销商级别限制，只查询一级经销商
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public List<Map<String,Object>> dlrQueryEncourageNewsDetail(String fineId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.NEWS_ID,B.NEWS_CODE,B.NEWS_TITLE\n");
		sql.append("  FROM TM_FINE_NEWS A, tt_as_wr_news B\n");
		sql.append(" WHERE A.NEWS_ID = B.NEWS_ID\n");
		sql.append("   AND A.FINE_ID = "+fineId+"\n");


		List<Map<String,Object>> listNews = pageQuery(sql.toString(),null,getFunName());
		return listNews;
	}
	public List<Map<String,Object>> dlrQueryEncourageNewsDetail(long newsid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.NEWS_ID,B.NEWS_CODE,B.NEWS_TITLE\n");
		sql.append("  FROM TM_FINE_NEWS A, TT_AS_WR_NEWS B\n");
		sql.append(" WHERE A.NEWS_ID = B.NEWS_ID\n");
		sql.append("   AND A.FINE_NEWS_ID = "+newsid+"\n");


		List<Map<String,Object>> listNews = pageQuery(sql.toString(),null,getFunName());
		return listNews;
	}
	
	//查询导入临时表的数据
	public List<Map<String, Object>> QueryFineTemporary(){
		StringBuffer sql= new StringBuffer();
		sql.append("select tba.area_name,\n" );
		sql.append("       td.dealer_code,\n" );
		sql.append("       td.dealer_name,\n" );
		sql.append("       t.fine_reason,\n" );
		sql.append("       t.remark,\n" );
		sql.append("       t.datum_sum,\n" );
		sql.append("       t.LABOUR_BH,\n" );
		sql.append("       t.fine_type,\n" );
		sql.append("       t.labour_sum\n" );
		sql.append("  from tt_as_wr_fine_temporary t, tm_dealer td, tm_business_area tba\n" );
		sql.append(" where t.dealer_id = td.dealer_id\n" );
		sql.append("   and t.yieldly = tba.area_id    order by t.fine_id desc");
		return pageQuery(sql.toString(), null, getFunName());
	}
	
	//插入
	public int insertPlan(){
		StringBuffer sql= new StringBuffer();
		sql.append("insert into tt_as_wr_fine  select * from tt_as_wr_fine_temporary");
		return update(sql.toString(), null);
		
	}
	
}
