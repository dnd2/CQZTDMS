package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcRolePO;
import com.infodms.dms.po.TtAsWrKnowledgebasePO;
import com.infodms.dms.po.TtAsWrNewsOrgPO;
import com.infodms.dms.po.TtAsWrNewsPO;
import com.infodms.dms.po.TtAsWrNewsReadPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class HomePageNewsDao extends BaseDao {
	public static Logger logger = Logger.getLogger(HomePageNewsDao.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final HomePageNewsDao dao = new HomePageNewsDao();
	
	public static final HomePageNewsDao getInstance()
	{
		return dao;
	}
	
	/********
	 * 获取userName和公司ID
	 * 
	 * @param
	 * @return
	 */
	public List<Map<String, Object>> getUserName(String userId)
	{
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select u.name VOICE_PERSON,c.company_name ORG_NAME,c.company_id ORG_ID from tm_company c ,tc_user u where 1=1 and c.company_id=u.company_id  ");
		if (Utility.testString(userId))
		{
			sb.append(" and u.user_id= ?");
			params.add(userId);
		}
		List<Map<String, Object>> list = pageQuery(sb.toString(), params, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> dutyTypeDQ()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" select o.*, 1 as is_checked from  tm_org o where o.duty_type=" + Constant.DUTY_TYPE_LARGEREGION + " and o.status=" + Constant.STATUS_ENABLE + " order by name_sort");
		
		List<Map<String, Object>> list = pageQuery(sb.toString(), null, getFunName());
		return list;
	}
	
	
	public Long queryNewsNum(AclUserBean logonUser, String dealerId){
		StringBuilder sql = new StringBuilder();
		if (dealerId == null || dealerId.equals("")) {
			// 判断当前用户是不是大区或区域用户
			if(logonUser.getOrgId().longValue() != 2010010100070674L) {
				sql.append("SELECT count(*) newsNum FROM (");
				sql.append("SELECT OO.*,r.dealer_id FROM (");
				sql.append("SELECT OO.NEWS_ID ");
				sql.append("FROM TT_AS_WR_NEWS OO ");
				sql.append("WHERE OO.STATUS = 11741001 ");
				sql.append("AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) ");
				sql.append("AND EXISTS (SELECT 1 FROM TT_AS_WR_NEWS_ORG A, TM_DEALER B WHERE A.DEALER_ID = B.DEALER_ID ");
				if(logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_VS) {
					// 如果接收方是销售经销商，使用销售的省份限制条件
					sql.append("AND B.DEALER_TYPE = "+Constant.DEALER_TYPE_DVS+" ");
					sql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("B.DEALER_ID", logonUser));
				} else if(logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WR) {
					// 如果接收方是售后经销商，使用售后的省份限制条件
					sql.append("AND B.DEALER_TYPE = "+Constant.DEALER_TYPE_DWR+" ");
					sql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByService("B", logonUser));
				} else if(logonUser.getPoseBusType().intValue() != Constant.POSE_BUS_TYPE_SYS){
					// 如果不属于上面两种接收类型，则强制查询条件无效
					sql.append("AND 1 = 2 ");
				}	
				sql.append(" AND A.NEWS_ID = OO.NEWS_ID) ");
			    sql.append("union ");
			    sql.append("SELECT OO.NEWS_ID ");
			    sql.append("FROM TT_AS_WR_NEWS OO, TT_AS_WR_NEWS_ORG TAWNO ");
			    sql.append("WHERE OO.STATUS = 11741001 ");
				sql.append("AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) ");
				sql.append("AND OO.NEWS_ID = TAWNO.NEWS_ID AND TAWNO.DEALER_ID = " + logonUser.getUserId() + " AND TAWNO.MSG_TYPE in ('1','-1') ");
				sql.append("union ");
			    sql.append("SELECT OO.NEWS_ID ");
				sql.append("FROM TT_AS_WR_NEWS OO ");
				sql.append("WHERE (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE) >= TRUNC(SYSDATE)  AND OO.STATUS = 11741001 ) ");
				sql.append("and (OO.DUTY_TYPE IN '11781003' OR OO.MSG_TYPE = '11991003') ");
				sql.append(") OO ");
				sql.append(" left join TT_AS_WR_NEWS_READ r on OO.NEWS_ID = r.news_id and r.dealer_id = "+dealerId);
				sql.append(" ) s where dealer_id is null");
			}else{
				sql.append("SELECT count(*) newsNum FROM (");
				sql.append("SELECT OO.*,r.dealer_id from ");
				sql.append("( ");
				sql.append("SELECT OO.NEWS_ID ");
				sql.append("FROM TT_AS_WR_NEWS OO, TT_AS_WR_NEWS_ORG TAWNO ");
				sql.append("WHERE OO.STATUS = 11741001  ");
				sql.append("AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) ");
				sql.append("AND OO.NEWS_ID = TAWNO.NEWS_ID ");
				sql.append("AND TAWNO.DEALER_ID = " + logonUser.getUserId() + " ");
				sql.append("AND TAWNO.MSG_TYPE in ('1','-1' ) ");
				sql.append("UNION ");
				sql.append("SELECT OO.NEWS_ID ");
				sql.append("FROM TT_AS_WR_NEWS OO ");
				sql.append("WHERE OO.STATUS = 11741001 ");
				sql.append(" AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) ");
				sql.append(" AND OO.DUTY_TYPE=11781003 ");
				sql.append(") OO ");
				sql.append(" left join TT_AS_WR_NEWS_READ r on OO.NEWS_ID = r.news_id and r.dealer_id = "+dealerId);
				sql.append(" ) s where dealer_id is null");
			}
		}else{
			sql.append("SELECT count(*) newsNum FROM (");
			sql.append(" SELECT OO.*,r.dealer_id FROM (");
			sql.append(" (");
			sql.append(" SELECT OO.NEWS_ID");
			sql.append(" FROM TT_AS_WR_NEWS OO, TT_AS_WR_NEWS_ORG N, TM_DEALER D, TT_AS_WR_NEWS_READ R");
			sql.append(" WHERE OO.STATUS = 11741001");
			sql.append(" AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) ");
			sql.append(" AND OO.NEWS_ID = N.NEWS_ID");
			sql.append(" AND OO.NEWS_ID = R.NEWS_ID(+)");
			sql.append(" AND R.DEALER_ID(+) = "+ dealerId );
			sql.append(" AND N.DEALER_ID = D.DEALER_ID");
			sql.append(" AND D.DEALER_ID = "+ dealerId );
			sql.append(" AND NOT EXISTS (");
			sql.append(" SELECT 1");
			sql.append(" FROM TR_ROLE_POSE C, TT_AS_WR_NEWS_ROLE R");
			sql.append(" WHERE C.POSE_ID = " + logonUser.getPoseId());
			sql.append(" AND C.ROLE_ID = R.ROLE_ID");
		    sql.append(" AND R.NEWS_ID = OO.NEWS_ID");
		    sql.append(" )");
		    sql.append(" )");
		    sql.append(" union");
		    sql.append(" (");
		    sql.append(" SELECT OO.NEWS_ID");
		    sql.append(" FROM ");
		    sql.append(" (");
		    sql.append(" select * from TT_AS_WR_NEWS  ");  
		    sql.append(" WHERE DUTY_TYPE = '11781003'   ");  
		  	sql.append(" AND MSG_TYPE = '11991003'");
		  	sql.append(" ) OO, TT_AS_WR_NEWS_READ R  ");
		  	sql.append(" WHERE OO.NEWS_ID = R.NEWS_ID(+) ");
			sql.append(" AND R.DEALER_ID(+) =  "+ dealerId );
			sql.append(" AND OO.STATUS = 11741001 ");
			sql.append(" AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE) >= TRUNC(SYSDATE)  )");
			sql.append(" )");
		    sql.append(" ) OO ");
		    sql.append(" left join TT_AS_WR_NEWS_READ r on OO.NEWS_ID = r.news_id and r.dealer_id = "+dealerId);
			sql.append(" ) s where dealer_id is null");
		}
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if(map.get("NEWSNUM") != null){
			return Long.parseLong(map.get("NEWSNUM").toString());
		}
		return 0l;
	}
	
	
	public PageResult<Map<String, Object>> homepageNewsList(AclUserBean logonUser, int pageSize, int curPage, String dealerId, String newsCode, String person, String startDate, String endDate,
					String newsStatus, String comman) throws Exception
	{
		PageResult<Map<String, Object>> ps = null;
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( ");
		sql.append("select t.news_id, \n");
		sql.append("       t.news_code, \n");
		sql.append("       t.news_title, \n");
		sql.append("       t.news_type, \n");
		sql.append("       t.voice_person, \n");
		sql.append("       t.is_top, \n");
		sql.append("       t.status, \n");
		sql.append("       t.create_date, \n");
		sql.append("       t.news_date \n");
		sql.append("  from tt_as_wr_news t \n");
		sql.append(" where t.status = 11741001 \n");
		sql.append("   and t.duty_type = 11781003 \n");
		sql.append("   and (t.expiry_date is null or trunc (t.expiry_date) >= trunc (sysdate)) \n");
		if (dealerId == null || dealerId.equals("")) {
			sql.append("union all \n");
			sql.append("-- 如果是OEM端 \n");
			sql.append("select t.news_id, \n");
			sql.append("       t.news_code, \n");
			sql.append("       t.news_title, \n");
			sql.append("       t.news_type, \n");
			sql.append("       t.voice_person, \n");
			sql.append("       t.is_top, \n");
			sql.append("       t.status, \n");
			sql.append("       t.create_date, \n");
			sql.append("       t.news_date \n");
			sql.append("  from tt_as_wr_news t \n");
			sql.append(" where t.status = 11741001 \n");
			sql.append("   and t.duty_type = 11781002 \n");
			sql.append("   and (t.expiry_date is null or trunc (t.expiry_date) >= trunc (sysdate)) \n");
			sql.append("   -- 根据渠道进行限制用户职位 \n");
			sql.append("   and exists \n");
			sql.append("          (select 1 \n");
			sql.append("             from tt_as_wr_news_channels c \n");
			sql.append("            where c.news_id = t.news_id ");
			if (logonUser.getDutyType().equals("10781001")) {
				sql.append(" and (c.channel_id = ? or c.channel_id = ? or c.channel_id = ?)");
				params.add("11991001");
				params.add("11991002");
				params.add("11991003");
			} else if(logonUser.getDutyType().equals("10781002")) {
				sql.append(" and c.channel_id = ?");
				params.add("11991001");
			} else if(logonUser.getDutyType().equals("10781003")) {
				sql.append(" and c.channel_id = ?");
				params.add("11991002");
			} else if(logonUser.getDutyType().equals("10781007")) {
				sql.append(" and c.channel_id = ?");
				params.add("11991003");
			}
			sql.append(")\n");
			sql.append("   -- 根据发送范围进行数据限制 \n");
			if(logonUser.getOrgId().longValue() != 2010010100070674L) {
				sql.append("   and exists \n");
				sql.append("          (select * \n");
				sql.append("             from tt_as_wr_news_send s \n");
				sql.append("            where s.news_id = t.news_id \n");
				sql.append("              and t.org_id in (select p.org_id \n");
				sql.append("                                 from tc_pose p \n");
				sql.append("                                where p.pose_type = 10021001 \n");
				sql.append("                                  and p.pose_id = ?)) \n");
				params.add(logonUser.getPoseId());
			}
		} else {
			sql.append("union all \n");
			sql.append("-- 如果是DEALER端 \n");
			sql.append("select t.news_id, \n");
			sql.append("       t.news_code, \n");
			sql.append("       t.news_title, \n");
			sql.append("       t.news_type, \n");
			sql.append("       t.voice_person, \n");
			sql.append("       t.is_top, \n");
			sql.append("       t.status, \n");
			sql.append("       t.create_date, \n");
			sql.append("       t.news_date \n");
			sql.append("  from tt_as_wr_news t \n");
			sql.append(" where t.status = 11741001 \n");
			sql.append("   and t.duty_type = 11781001 \n");
			sql.append("   and (t.expiry_date is null or trunc (t.expiry_date) >= trunc (sysdate)) \n");
			sql.append("   -- 根据渠道进行限制用户职位 \n");
			sql.append("   and exists \n");
			sql.append("          (select 1 \n");
			sql.append("             from tt_as_wr_news_channels c \n");
			sql.append("            where c.news_id = t.news_id ");
			if (logonUser.getDealerType().intValue() == Constant.DEALER_TYPE_DVS) {
				sql.append(" and c.channel_id = ?");
				params.add("11991001");
			} else if(logonUser.getDealerType().intValue() == Constant.DEALER_TYPE_DWR) {
				sql.append(" and c.channel_id = ?");
				params.add("11991002");
			}
			sql.append(")\n");
//			sql.append("   -- 根据发送范围进行数据限制 \n");
//			sql.append("   and exists \n");
//			sql.append("          (select * \n");
//			sql.append("             from tt_as_wr_news_send s \n");
//			sql.append("            where s.news_id = t.news_id \n");
//			sql.append("              and t.org_id in (select p.org_id \n");
//			sql.append("                                 from tc_pose p \n");
//			sql.append("                                where p.pose_type = 10021001 \n");
//			sql.append("                                  and p.pose_id = ?)) \n");
//			
//			params.add(logonUser.getPoseId());
			
		}
		sql.append(") oo order by oo.is_top desc,oo.status,oo.create_date desc");
		ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> homepageNewsList3(AclUserBean logonUser, int pageSize, int curPage, String dealerId, String newsCode, String person, String startDate, String endDate,
			String newsStatus, String comman,String newsTitle) throws Exception
	{
	PageResult<Map<String, Object>> ps = null;
	StringBuffer sbSql = new StringBuffer();
	
	if (dealerId == null || dealerId.equals("")) {
		// 判断当前用户是不是大区或区域用户
		if(logonUser.getOrgId().longValue() != 2010010100070674L) {
			sbSql.append("SELECT * FROM (");
			sbSql.append("SELECT OO.NEWS_ID,\n");
			sbSql.append("       OO.NEWS_CODE,\n");
			sbSql.append("       OO.NEWS_TITLE,\n");
			sbSql.append("       OO.NEWS_TYPE,\n");
			sbSql.append("       OO.NEWS_DATE,\n");
			sbSql.append("       OO.ORG_ID,\n");
			sbSql.append("       OO.ORG_NAME,\n");
			sbSql.append("       OO.OEM_COMPANY_ID,\n");
			sbSql.append("       OO.CREATE_DATE,\n");
			sbSql.append("       OO.CREATE_BY,\n");
			sbSql.append("       OO.UPDATE_DATE,\n");
			sbSql.append("       OO.UPDATE_BY,\n");
			sbSql.append("       OO.STATUS,\n");
			sbSql.append("       OO.DUTY_TYPE,\n");
			sbSql.append("       OO.ORG_TYPE_ID,\n");
			sbSql.append("       OO.VOICE_PERSON,\n");
			sbSql.append("       OO.MSG_TYPE,\n");
			sbSql.append("       OO.IS_PRIVATE,\n");
			sbSql.append("       OO.IS_TOP\n");
			sbSql.append("  FROM TT_AS_WR_NEWS OO\n");
			//sbSql.append(" WHERE OO.STATUS <> 11741003\n");
			sbSql.append(" WHERE OO.STATUS = 11741001\n");
			sbSql.append("   AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) \n");
			sbSql.append("   AND EXISTS (SELECT 1\n");
			sbSql.append("          FROM TT_AS_WR_NEWS_ORG A, TM_DEALER B\n");
			sbSql.append("         WHERE A.DEALER_ID = B.DEALER_ID\n");
			if(logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_VS) {
				// 如果接收方是销售经销商，使用销售的省份限制条件
				sbSql.append("           AND B.DEALER_TYPE = "+Constant.DEALER_TYPE_DVS+"\n");
				sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("B.DEALER_ID", logonUser));
			} else if(logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WR) {
				// 如果接收方是售后经销商，使用售后的省份限制条件
				sbSql.append("           AND B.DEALER_TYPE = "+Constant.DEALER_TYPE_DWR+"\n");
				sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByService("B", logonUser));
			} else if(logonUser.getPoseBusType().intValue() != Constant.POSE_BUS_TYPE_SYS){
				// 如果不属于上面两种接收类型，则强制查询条件无效
				sbSql.append("           AND 1 = 2\n");
			}
			sbSql.append("           AND A.NEWS_ID = OO.NEWS_ID)");
			sbSql.append(" union \n");
			sbSql.append(" SELECT OO.NEWS_ID,\n");
			sbSql.append("       OO.NEWS_CODE,\n");
			sbSql.append("       OO.NEWS_TITLE,\n");
			sbSql.append("       OO.NEWS_TYPE,\n");
			sbSql.append("       OO.NEWS_DATE,\n");
			sbSql.append("       OO.ORG_ID,\n");
			sbSql.append("       OO.ORG_NAME,\n");
			sbSql.append("       OO.OEM_COMPANY_ID,\n");
			sbSql.append("       OO.CREATE_DATE,\n");
			sbSql.append("       OO.CREATE_BY,\n");
			sbSql.append("       OO.UPDATE_DATE,\n");
			sbSql.append("       OO.UPDATE_BY,\n");
			sbSql.append("       OO.STATUS,\n");
			sbSql.append("       OO.DUTY_TYPE,\n");
			sbSql.append("       OO.ORG_TYPE_ID,\n");
			sbSql.append("       OO.VOICE_PERSON,\n");
			sbSql.append("       OO.MSG_TYPE,\n");
			sbSql.append("       OO.IS_PRIVATE,\n");
			sbSql.append("       OO.IS_TOP\n");
			sbSql.append("  FROM TT_AS_WR_NEWS OO, TT_AS_WR_NEWS_ORG TAWNO\n");
			//sbSql.append(" WHERE OO.STATUS <> 11741003\n");
			sbSql.append(" WHERE OO.STATUS = 11741001\n");
			sbSql.append("   AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) \n");
			sbSql.append("   AND OO.NEWS_ID = TAWNO.NEWS_ID AND TAWNO.DEALER_ID = "+logonUser.getUserId()+" AND TAWNO.MSG_TYPE = '1'"); 
			sbSql.append(" union \n");
			sbSql.append(" SELECT OO.NEWS_ID,\n");
			sbSql.append("      OO.NEWS_CODE,\n");
			sbSql.append("      OO.NEWS_TITLE,\n");
			sbSql.append("      OO.NEWS_TYPE,\n");
			sbSql.append("      OO.NEWS_DATE,\n");
			sbSql.append("      OO.ORG_ID,\n");
			sbSql.append("      OO.ORG_NAME,\n");
			sbSql.append("      OO.OEM_COMPANY_ID,\n");
			sbSql.append("      OO.CREATE_DATE,\n");
			sbSql.append("      OO.CREATE_BY,\n");
			sbSql.append("      OO.UPDATE_DATE,\n");
			sbSql.append("      OO.UPDATE_BY,\n");
			sbSql.append("      OO.STATUS,\n");
			sbSql.append("      OO.DUTY_TYPE,\n");
			sbSql.append("      OO.ORG_TYPE_ID,\n");
			sbSql.append("      OO.VOICE_PERSON,\n");
			sbSql.append("      OO.MSG_TYPE,\n");
			sbSql.append("      OO.IS_PRIVATE,\n");
			sbSql.append("      OO.IS_TOP\n");
			sbSql.append(" FROM TT_AS_WR_NEWS OO, TT_AS_WR_NEWS_ORG TAWNO\n");
			sbSql.append(" WHERE OO.STATUS = 11741001\n");
			//sbSql.append(" WHERE OO.STATUS <> 11741003\n");
			sbSql.append("   AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) \n");
			sbSql.append("   AND OO.NEWS_ID = TAWNO.NEWS_ID AND TAWNO.DEALER_ID = "+logonUser.getUserId()+" AND TAWNO.MSG_TYPE = '-1'"); 
			
			//added by wizard_lee 2014-08-08 经销商可以查看公共新闻和经销商公共新闻
			sbSql.append("	union \n");
			sbSql.append("SELECT AA.NEWS_ID,\n");
			sbSql.append("       AA.NEWS_CODE,\n");
			sbSql.append("       AA.NEWS_TITLE,\n");
			sbSql.append("       AA.NEWS_TYPE,\n");
			sbSql.append("       AA.NEWS_DATE,\n");
			sbSql.append("       AA.ORG_ID,\n");
			sbSql.append("       AA.ORG_NAME,\n");
			sbSql.append("       AA.OEM_COMPANY_ID,\n");
			sbSql.append("       AA.CREATE_DATE,\n");
			sbSql.append("       AA.CREATE_BY,\n");
			sbSql.append("       AA.UPDATE_DATE,\n");
			sbSql.append("       AA.UPDATE_BY,\n");
			sbSql.append("       AA.STATUS,\n");
			sbSql.append("       AA.DUTY_TYPE,\n");
			sbSql.append("       AA.ORG_TYPE_ID,\n");
			sbSql.append("       AA.VOICE_PERSON,\n");
			sbSql.append("       AA.MSG_TYPE,\n");
			sbSql.append("       AA.IS_PRIVATE,\n");
			sbSql.append("       AA.IS_TOP\n");
			sbSql.append("            from (select * from TT_AS_WR_NEWS  " +
					"    WHERE DUTY_TYPE IN '11781003'" +
					"    OR MSG_TYPE = '11991003' ) AA" +
					" WHERE (AA.EXPIRY_DATE IS NULL OR TRUNC(AA.EXPIRY_DATE) >= TRUNC(SYSDATE)  AND AA.STATUS = 11741001 )");
			sbSql.append(" ) OO \n");
			sbSql.append("GROUP BY OO.NEWS_ID,\n");
			sbSql.append("         OO.NEWS_CODE,\n");
			sbSql.append("         OO.NEWS_TITLE,\n");
			sbSql.append("         OO.NEWS_TYPE,\n");
			sbSql.append("         OO.NEWS_DATE,\n");
			sbSql.append("         OO.ORG_ID,\n");
			sbSql.append("         OO.ORG_NAME,\n");
			sbSql.append("         OO.OEM_COMPANY_ID,\n");
			sbSql.append("         OO.CREATE_DATE,\n");
			sbSql.append("         OO.CREATE_BY,\n");
			sbSql.append("         OO.UPDATE_DATE,\n");
			sbSql.append("         OO.UPDATE_BY,\n");
			sbSql.append("         OO.STATUS,\n");
			sbSql.append("         OO.DUTY_TYPE,\n");
			sbSql.append("         OO.ORG_TYPE_ID,\n");
			sbSql.append("         OO.VOICE_PERSON,\n");
			sbSql.append("         OO.MSG_TYPE,\n");
			sbSql.append("         OO.IS_PRIVATE,\n");
			sbSql.append("         OO.IS_TOP\n"); 
			sbSql.append("order by oo.is_top desc,oo.status,oo.create_date desc ");
			
			ps =  pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		} else {
			sbSql.append(" SELECT * from (\n");
			sbSql.append(" SELECT OO.NEWS_ID,\n");
			sbSql.append("       OO.NEWS_CODE,\n");
			sbSql.append("       OO.NEWS_TITLE,\n");
			sbSql.append("       OO.NEWS_TYPE,\n");
			sbSql.append("       OO.NEWS_DATE,\n");
			sbSql.append("       OO.ORG_ID,\n");
			sbSql.append("       OO.ORG_NAME,\n");
			sbSql.append("       OO.OEM_COMPANY_ID,\n");
			sbSql.append("       OO.CREATE_DATE,\n");
			sbSql.append("       OO.CREATE_BY,\n");
			sbSql.append("       OO.UPDATE_DATE,\n");
			sbSql.append("       OO.UPDATE_BY,\n");
			sbSql.append("       OO.STATUS,\n");
			sbSql.append("       OO.DUTY_TYPE,\n");
			sbSql.append("       OO.ORG_TYPE_ID,\n");
			sbSql.append("       OO.VOICE_PERSON,\n");
			sbSql.append("       OO.MSG_TYPE,\n");
			sbSql.append("       OO.IS_PRIVATE,\n");
			sbSql.append("       OO.IS_TOP\n");
			sbSql.append("  FROM TT_AS_WR_NEWS OO, TT_AS_WR_NEWS_ORG TAWNO\n");
			//sbSql.append(" WHERE OO.STATUS <> 11741003\n");
			sbSql.append(" WHERE OO.STATUS = 11741001\n");
			sbSql.append("   AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) \n");
			sbSql.append("   AND OO.NEWS_ID = TAWNO.NEWS_ID \n"); 
			sbSql.append(" union \n");
			sbSql.append(" SELECT OO.NEWS_ID,\n");
			sbSql.append("      OO.NEWS_CODE,\n");
			sbSql.append("      OO.NEWS_TITLE,\n");
			sbSql.append("      OO.NEWS_TYPE,\n");
			sbSql.append("      OO.NEWS_DATE,\n");
			sbSql.append("      OO.ORG_ID,\n");
			sbSql.append("      OO.ORG_NAME,\n");
			sbSql.append("      OO.OEM_COMPANY_ID,\n");
			sbSql.append("      OO.CREATE_DATE,\n");
			sbSql.append("      OO.CREATE_BY,\n");
			sbSql.append("      OO.UPDATE_DATE,\n");
			sbSql.append("      OO.UPDATE_BY,\n");
			sbSql.append("      OO.STATUS,\n");
			sbSql.append("      OO.DUTY_TYPE,\n");
			sbSql.append("      OO.ORG_TYPE_ID,\n");
			sbSql.append("      OO.VOICE_PERSON,\n");
			sbSql.append("      OO.MSG_TYPE,\n");
			sbSql.append("      OO.IS_PRIVATE,\n");
			sbSql.append("      OO.IS_TOP\n");
			sbSql.append(" FROM TT_AS_WR_NEWS OO, TT_AS_WR_NEWS_ORG TAWNO\n");
			//sbSql.append(" WHERE OO.STATUS <> 11741003\n");
			//只显示正常状态的新闻
			sbSql.append(" WHERE OO.STATUS = 11741001\n");
			sbSql.append("   AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) \n");
			sbSql.append("   AND OO.NEWS_ID = TAWNO.NEWS_ID"); 
			//sbSql.append(" ) OO \n");
			//sbSql.append(" OR OO.DUTY_TYPE=11781003 ) OO \n");
			//add by wizard_lee
			sbSql.append(" UNION SELECT OO.NEWS_ID,\n");
			sbSql.append("      OO.NEWS_CODE,\n");
			sbSql.append("      OO.NEWS_TITLE,\n");
			sbSql.append("      OO.NEWS_TYPE,\n");
			sbSql.append("      OO.NEWS_DATE,\n");
			sbSql.append("      OO.ORG_ID,\n");
			sbSql.append("      OO.ORG_NAME,\n");
			sbSql.append("      OO.OEM_COMPANY_ID,\n");
			sbSql.append("      OO.CREATE_DATE,\n");
			sbSql.append("      OO.CREATE_BY,\n");
			sbSql.append("      OO.UPDATE_DATE,\n");
			sbSql.append("      OO.UPDATE_BY,\n");
			sbSql.append("      OO.STATUS,\n");
			sbSql.append("      OO.DUTY_TYPE,\n");
			sbSql.append("      OO.ORG_TYPE_ID,\n");
			sbSql.append("      OO.VOICE_PERSON,\n");
			sbSql.append("      OO.MSG_TYPE,\n");
			sbSql.append("      OO.IS_PRIVATE,\n");
			sbSql.append("      OO.IS_TOP\n");
			sbSql.append(" FROM TT_AS_WR_NEWS OO\n");
			sbSql.append(" WHERE OO.STATUS = 11741001\n");
			sbSql.append("   AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE))\n");
			sbSql.append("    AND OO.DUTY_TYPE=11781003) OO"); 
			
			sbSql.append(" WHERE OO.STATUS=11741001 \n");
			if (newsCode != null && !newsCode.equals(""))
			{
				sbSql.append(" and OO.NEWS_CODE like '%"+newsCode+"%'\n");
			}
			if (newsTitle != null && !newsTitle.equals(""))
			{
				sbSql.append(" and OO.NEWS_TITLE like '%"+newsTitle+"%'\n");
			}
			if (startDate != null && !startDate.equals(""))
			{
				sbSql.append(" and trunc(oo.NEWS_DATE)>=to_date('" + startDate + "','yyyy-mm-dd')");
			}
			if (endDate != null && !endDate.equals(""))
			{
				sbSql.append(" and trunc(oo.NEWS_DATE)<=to_date('" + endDate + "','yyyy-mm-dd')");
			}
			sbSql.append("order by oo.is_top desc,oo.status,oo.create_date desc ");
	
			ps =  pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		}
	}
	else
	{
		sbSql.append("SELECT * FROM ((SELECT OO.NEWS_ID,\n");
		sbSql.append("       OO.NEWS_CODE,\n");
		sbSql.append("       OO.NEWS_TITLE,\n");
		sbSql.append("       OO.NEWS_TYPE,\n");
		sbSql.append("       OO.NEWS_DATE,\n");
		sbSql.append("       OO.ORG_ID,\n");
		sbSql.append("       OO.ORG_NAME,\n");
		sbSql.append("       OO.OEM_COMPANY_ID,\n");
		sbSql.append("       OO.CREATE_DATE,\n");
		sbSql.append("       OO.CREATE_BY,\n");
		sbSql.append("       OO.UPDATE_DATE,\n");
		sbSql.append("       OO.UPDATE_BY,\n");
		sbSql.append("       OO.STATUS,\n");
		sbSql.append("       OO.DUTY_TYPE,\n");
		sbSql.append("       OO.ORG_TYPE_ID,\n");
		sbSql.append("       OO.VOICE_PERSON,\n");
		sbSql.append("       OO.MSG_TYPE,\n");
		sbSql.append("       OO.IS_PRIVATE,\n");
		sbSql.append("       OO.IS_TOP,\n");
		sbSql.append("       DECODE(R.READ_DATE,null,'未读','已读') READ_DATE\n");
		sbSql.append("  FROM TT_AS_WR_NEWS OO, TT_AS_WR_NEWS_ORG N, TM_DEALER D, TT_AS_WR_NEWS_READ R\n");
		//sbSql.append(" WHERE OO.STATUS <> 11741003\n");
		//只显示正常状态的新闻
		sbSql.append(" WHERE OO.STATUS = 11741001\n");
		sbSql.append("   AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE)>=TRUNC(SYSDATE)) \n");
		sbSql.append("   AND OO.NEWS_ID = N.NEWS_ID\n");
		sbSql.append("   AND OO.NEWS_ID = R.NEWS_ID(+)\n");
		sbSql.append("   AND R.DEALER_ID(+) = "+dealerId+"\n");
		sbSql.append("   AND N.DEALER_ID = D.DEALER_ID\n");
		sbSql.append("   AND D.DEALER_ID = "+dealerId+"\n");
		sbSql.append("   AND NOT EXISTS (SELECT 1\n");
		sbSql.append("          FROM TR_ROLE_POSE C, TT_AS_WR_NEWS_ROLE R\n");
		sbSql.append("         WHERE C.POSE_ID = "+logonUser.getPoseId()+"\n");
		sbSql.append("           AND C.ROLE_ID = R.ROLE_ID\n");
		sbSql.append("           AND R.NEWS_ID = OO.NEWS_ID)"); 
		sbSql.append("GROUP BY OO.NEWS_ID,\n");
		sbSql.append("         OO.NEWS_CODE,\n");
		sbSql.append("         OO.NEWS_TITLE,\n");
		sbSql.append("         OO.NEWS_TYPE,\n");
		sbSql.append("         OO.NEWS_DATE,\n");
		sbSql.append("         OO.ORG_ID,\n");
		sbSql.append("         OO.ORG_NAME,\n");
		sbSql.append("         OO.OEM_COMPANY_ID,\n");
		sbSql.append("         OO.CREATE_DATE,\n");
		sbSql.append("         OO.CREATE_BY,\n");
		sbSql.append("         OO.UPDATE_DATE,\n");
		sbSql.append("         OO.UPDATE_BY,\n");
		sbSql.append("         OO.STATUS,\n");
		sbSql.append("         OO.DUTY_TYPE,\n");
		sbSql.append("         OO.ORG_TYPE_ID,\n");
		sbSql.append("         OO.VOICE_PERSON,\n");
		sbSql.append("         OO.MSG_TYPE,\n");
		sbSql.append("         OO.IS_PRIVATE,\n");
	/*	sbSql.append("         OO.IS_TOP,R.READ_DATE\n"); 
		sbSql.append("order by oo.is_top desc,oo.status,oo.create_date desc) \n");*/
		sbSql.append("         OO.IS_TOP,R.READ_DATE )\n"); 
		//added by wizard_lee 2014-08-08 经销商可以查看公共新闻和经销商公共新闻
		sbSql.append("	union \n");
		sbSql.append("(SELECT OO.NEWS_ID,\n");
		sbSql.append("       OO.NEWS_CODE,\n");
		sbSql.append("       OO.NEWS_TITLE,\n");
		sbSql.append("       OO.NEWS_TYPE,\n");
		sbSql.append("       OO.NEWS_DATE,\n");
		sbSql.append("       OO.ORG_ID,\n");
		sbSql.append("       OO.ORG_NAME,\n");
		sbSql.append("       OO.OEM_COMPANY_ID,\n");
		sbSql.append("       OO.CREATE_DATE,\n");
		sbSql.append("       OO.CREATE_BY,\n");
		sbSql.append("       OO.UPDATE_DATE,\n");
		sbSql.append("       OO.UPDATE_BY,\n");
		sbSql.append("       OO.STATUS,\n");
		sbSql.append("       OO.DUTY_TYPE,\n");
		sbSql.append("       OO.ORG_TYPE_ID,\n");
		sbSql.append("       OO.VOICE_PERSON,\n");
		sbSql.append("       OO.MSG_TYPE,\n");
		sbSql.append("       OO.IS_PRIVATE,\n");
		sbSql.append("       OO.IS_TOP,\n");
		sbSql.append("       DECODE(R.READ_DATE,null,'未读','已读') READ_DATE\n");
		sbSql.append("  from (select *	\n");
		sbSql.append("            from TT_AS_WR_NEWS" +
				"    WHERE DUTY_TYPE = '11781003' " +
				"    AND MSG_TYPE = '11991003') OO," +
				" TT_AS_WR_NEWS_READ R " +
				" WHERE OO.NEWS_ID = R.NEWS_ID(+)" +
				" AND R.DEALER_ID(+) = "+dealerId +
				" AND OO.STATUS = 11741001" +
				" AND (OO.EXPIRY_DATE IS NULL OR TRUNC(OO.EXPIRY_DATE) >= TRUNC(SYSDATE) " +
				" ))) order by is_top desc,status,create_date desc");
			ps =  pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		}
	return ps;
	}
	
	// YH 2010.12.21
	public PageResult<Map<String, Object>> homepageNewsList2(AclUserBean logonUser, int pageSize, int curPage, String dealerId, String newsCode, String person, String startDate, String endDate,
					String newsStatus, String comman, String title ,RequestWrapper request) throws Exception
	{
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer col = new StringBuffer(); // 列
		col.append("  oo.news_id,\n");
		col.append("  oo.news_code,\n");
		col.append("  oo.news_title,\n");
		col.append("  oo.news_type,\n");
		col.append("  oo.news_date,\n");
		col.append("  oo.org_id,\n");
		col.append("  oo.org_name,\n");
		// col.append("  to_char(oo.contents) contents,\n" );
		col.append("  oo.oem_company_id,\n");
		col.append("  oo.create_date,\n");
		col.append("  oo.create_by,\n");
		col.append("  oo.update_date,\n");
		col.append("  oo.update_by,\n");
		col.append("  oo.status,\n");
		col.append("  oo.duty_type,\n");
		col.append("  oo.org_type_id,\n");
		col.append("  oo.voice_person,\n");
		col.append("  oo.msg_type,\n");
		col.append("  oo.is_private,\n");
		col.append("  oo.is_top");
		
		if (dealerId == null || dealerId.equals(""))
		{// 如果是OEM端
			sql.append(" select " + col.toString() + " from tt_as_wr_news  oo where 1=1 "); // YH
																							// 2010.12.21
			
			if (newsCode != null && !newsCode.equals(""))
			{
				sql.append(" and oo.NEWS_code like '%" + newsCode + "%'");
			}
			if (person != null && !person.equals(""))
			{
				sql.append(" and oo.voice_person like '%" + person + "%'");
			}
			if (startDate != null && !startDate.equals(""))
			{
				sql.append(" and trunc(oo.NEWS_DATE)>=to_date('" + startDate + "','yyyy-mm-dd')");
			}
			if (endDate != null && !endDate.equals(""))
			{
				sql.append(" and trunc(oo.NEWS_DATE)<=to_date('" + endDate + "','yyyy-mm-dd')");
			}
			if (newsStatus != null && !newsStatus.equals(""))
			{
				sql.append(" and oo.status=" + newsStatus);
			}
			if (title != null && !title.equals(""))
			{
				sql.append(" and oo.news_title like '%" + title + "%'");
			}
			DaoFactory.getsql(sql, "oo.news_type", CommonUtils.checkNull(request.getParamValue("NEWS_TYPE")), 1);
			// sql.append(" and oo.archive_flag=0 order by is_top desc,status,create_date desc ");
			// 2013.09.06艾春修改
			sql.append(" order by is_top desc,status,create_date desc ");
		}
		else
		{// 如果是经销商端
			sql.append("select distinct " + col.toString() + " from ( select " + col.toString() + " from tt_as_wr_news  oo where 1=1");
			sql.append(" and oo.duty_type=" + Constant.VIEW_NEWS_type_3 + "\n"); // YH
																					// 2010.12.21
			
			sql.append(" union\n");
			sql.append(" select " + col.toString() + "\n"); // 经销商没关闭指定区域指定售后或销售
			sql.append(" from tt_as_wr_news oo\n");
			sql.append(" where 1 = 1\n");
			sql.append(" and oo.duty_type = " + Constant.VIEW_NEWS_type_1 + "\n");
			if (dealerId != null)
			{
				List<Map<String, Object>> list = viewOrgIdDealerId(dealerId);
				StringBuffer str = new StringBuffer("");
				for (int i = 0; i < list.size(); i++)
				{
					str.append(list.get(0).get("ORG_ID").toString());
					if (i != list.size() - 1)
					{
						str.append(",");
					}
					String realstr = str.toString().substring(0, str.toString().length() - 1);
					sql.append(" and oo.org_type_id like'%" + realstr + "%'");
					sql.append(" and oo.is_private ='0'");
					
					if (null != logonUser.getDealerType() && Constant.DEALER_TYPE_DWR == logonUser.getDealerType())
					{
						sql.append(" and oo.msg_type ='" + Constant.MSG_TYPE_2 + "'");
					}
					else
					{
						sql.append(" and oo.msg_type ='" + Constant.MSG_TYPE_1 + "' \n");
					}
					sql.append("union\n");
					sql.append("select " + col.toString() + "\n");
					sql.append("from tt_as_wr_news oo\n");
					sql.append("where 1 = 1\n");
					sql.append("and oo.duty_type = " + Constant.VIEW_NEWS_type_1 + " \n");
					sql.append("and oo.org_type_id like '%" + realstr + "%' \n");
					sql.append("and oo.msg_type = '" + Constant.MSG_TYPE_3 + "'");
					
				}
				sql.append("union\n"); // 经销商没关闭全部区域 指定售后或销售
				sql.append("  select " + col.toString() + "\n");
				sql.append(" from tt_as_wr_news oo\n");
				sql.append("  where 1 = 1\n");
				sql.append("  and oo.duty_type = " + Constant.VIEW_NEWS_type_1 + " \n");
				sql.append("  and oo.org_type_id is null\n");
				if (null != logonUser.getDealerType() && Constant.DEALER_TYPE_DWR == logonUser.getDealerType())
				{
					sql.append(" and oo.msg_type ='" + Constant.MSG_TYPE_2 + "'");
				}
				else
				{
					sql.append(" and oo.msg_type ='" + Constant.MSG_TYPE_1 + "' \n");
				}
				sql.append(" union\n");
				sql.append(" select " + col.toString() + "\n"); // 经销商没关闭全部区域消息
				sql.append(" from tt_as_wr_news oo\n");
				sql.append(" where 1 = 1\n");
				sql.append(" and oo.duty_type = " + Constant.VIEW_NEWS_type_1 + "\n");
				sql.append(" and oo.org_type_id  is null");
				sql.append(" and oo.msg_type ='" + Constant.MSG_TYPE_3 + "'");
				sql.append(" union select " + col.toString() + "\n"); // 经销商没关闭指定了经销商指定了经销商类型
				sql.append(" from tt_as_wr_news oo where oo.news_id in (\n");
				sql.append("select O.NEWS_ID from tt_as_wr_news_org o where o.dealer_id like '%" + logonUser.getDealerId() + "%' ");
				if (null != logonUser.getDealerType() && Constant.DEALER_TYPE_DWR == logonUser.getDealerType())
				{
					sql.append(" and o.msg_type ='" + Constant.MSG_TYPE_2 + "'");
				}
				else
				{
					sql.append(" and o.msg_type ='" + Constant.MSG_TYPE_1 + "'");
				}
				sql.append(" )) oo where 1=1 "); // YH 2010.12.21
				
				if (newsCode != null && !newsCode.equals(""))
				{
					sql.append(" and oo.NEWS_code like '%" + newsCode + "%'");
				}
				if (person != null && !person.equals(""))
				{
					sql.append(" and oo.voice_person='" + person + "'");
				}
				if (startDate != null && !startDate.equals(""))
				{
					sql.append(" and trunc(oo.NEWS_DATE)>=to_date('" + startDate + "','yyyy-mm-dd')");
				}
				if (endDate != null && !endDate.equals(""))
				{
					sql.append(" and trunc(oo.NEWS_DATE)<=to_date('" + endDate + "','yyyy-mm-dd')");
				}
				if (newsStatus != null && !newsStatus.equals(""))
				{
					sql.append(" and oo.status=" + newsStatus);
				}
				if (comman != null && !comman.equals(""))
				{
					sql.append(" and oo.status=" + Constant.NEWS_STATUS_1 + "");
				}
				if (title != null && !title.equals(""))
				{
					sql.append(" and oo.news_title like '%" + title + "%'");
				}
				DaoFactory.getsql(sql, "oo.news_type", CommonUtils.checkNull(request.getParamValue("NEWS_TYPE")), 1);
				
			}
			sql.append(" order by is_top desc,status,create_date desc ");
		}
		logger.info("--------------" + sql.toString());
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	/**
	 * Iverson add By 2010-12-09
	 * 
	 * @param pageSize
	 * @param curPage
	 * @param dealerId
	 * @param newsCode
	 * @param person
	 * @param startDate
	 * @param endDate
	 * @param newsStatus
	 * @param comman
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> homepageNewsList1(int pageSize, int curPage, String newsCode, String person, String startDate, String endDate, String newsStatus, String comman,
					String title, String modelGroupId, String modelPart) throws Exception
	{
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select oo.id,oo.code,oo.title,oo.published_date,oo.create_date,oo.create_by,oo.status,oo.voice_person,oo.org_id,oo.org_name,oo.model_part, \n");
		sql.append("nvl((select c.wrgroup_name from tt_as_wr_model_group c where c.wrgroup_id=oo.wrgroup_id),0) as wrgroup_id from tt_as_wr_KnowledgeBase  oo where 1=1\n");
		if (newsCode != null && !newsCode.equals(""))
		{
			sql.append(" and oo.code like '%" + newsCode + "%'");
		}
		if (person != null && !person.equals(""))
		{
			sql.append(" and oo.voice_person='" + person + "'");
		}
		if (startDate != null && !startDate.equals(""))
		{
			sql.append(" and trunc(oo.Published_DATE)>=to_date('" + startDate + "','yyyy-mm-dd')");
		}
		if (endDate != null && !endDate.equals(""))
		{
			sql.append(" and trunc(oo.Published_DATE)<=to_date('" + endDate + "','yyyy-mm-dd')");
		}
		if (newsStatus != null && !newsStatus.equals(""))
		{
			sql.append(" and oo.status=" + newsStatus);
		}
		if (title != null && !title.equals(""))
		{
			sql.append(" and oo.title like '%" + title + "%'");
		}
		if (comman != null && !comman.equals(""))
		{
			sql.append(" and oo.status=" + Constant.NEWS_STATUS_1 + "");
		}
		if (modelGroupId != null && !modelGroupId.equals(""))
		{
			sql.append(" and oo.wrgroup_id=" + modelGroupId + "\n");
		}
		if (modelPart != null && !modelPart.equals(""))
		{
			sql.append(" and oo.model_part='" + modelPart + "'\n");
		}
		
		sql.append(" order by status,create_date desc ");
		logger.info("--------------" + sql.toString());
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/******
	 * 插入首页新闻
	 * 
	 * @param po
	 */
	public static void insertNewsPO(TtAsWrNewsPO po)
	{
		String contents_temp = po.getContents();
		po.setContents(null);
		factory.insert(po);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("news_id", po.getNewsId());
		factory.updateClob(contents_temp, "TT_AS_WR_NEWS", "CONTENTS", params);
	}
	
	/**
	 * Iverson add By 2010-12-09 插入应用知识库
	 * 
	 * @param po
	 */
	public static void insert(TtAsWrKnowledgebasePO po)
	{
		
		factory.insert(po);
		
	}
	
	/*****
	 * 插入首页新闻权限表
	 * 
	 * @param po
	 */
	public static void insertNewsOrgPO(TtAsWrNewsOrgPO po)
	{
		
		factory.insert(po);
		
	}
	
	/*******
	 * 修改首页新闻
	 * 
	 * @param po
	 * @param po1
	 */
	public static void updateNewsPO(TtAsWrNewsPO oldpo, TtAsWrNewsPO po)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("news_id", oldpo.getNewsId());
		factory.updateClob(po.getContents(), "TT_AS_WR_NEWS", "CONTENTS", params);
		po.setContents(null);
		factory.update(oldpo, po);
	}
	
	/*******
	 * 修改首页新闻 YH 2011.5.17
	 * 
	 * @param po
	 * @param po1
	 */
	public static void updateNewsPO2(TtAsWrNewsPO po, TtAsWrNewsPO po1)
	{
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE Tt_As_Wr_News\n");
		sql.append("   SET Status      = " + po1.getStatus() + ", \n");
		sql.append("       Update_Date = sysdate ,\n");
		sql.append("       Update_By   = " + po1.getUpdateBy() + ",\n");
		sql.append("       Org_Id      = " + po1.getOrgId() + ",\n");
		sql.append("       Org_Name    = '" + po1.getOrgName() + "' ,\n");
		sql.append("       Duty_Type   = " + po1.getDutyType() + ",\n");
		sql.append("       Contents    =  ? ,\n");
		sql.append("       News_Code   = '" + po1.getNewsCode() + "' ,\n");
		sql.append("       News_Title  =  ? ,\n");
		sql.append("       News_Type   = '" + po1.getNewsType() + "'\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND News_Id =" + po.getNewsId());
		
		List<Object> params = new ArrayList<Object>();
		params.add(po1.getContents());
		params.add(po1.getNewsTitle());
		factory.update(sql.toString(), params);
	}
	
	/**
	 * Iverson add By 2010-12-09 修改
	 * 
	 * @param po
	 * @param po1
	 */
	public static void update(TtAsWrKnowledgebasePO po, TtAsWrKnowledgebasePO po1)
	{
		factory.update(po, po1);
	}
	
	public List<Map<String, Object>> newslist(String newsId, int type)
	{
		List<Object> params = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		StringBuffer col = new StringBuffer(); // 列
		col.append("  oo.news_id,\n");
		col.append("  oo.news_code,\n");
		col.append("  oo.news_title,\n");
		col.append("  oo.news_type,\n");
		col.append("  oo.news_date,\n");
		col.append("  oo.org_id,\n");
		col.append("  oo.org_name,\n");
		// col.append("  to_char(oo.contents) contents,\n" );
		col.append("  oo.oem_company_id,\n");
		col.append("  oo.create_date,\n");
		col.append("  oo.create_by,\n");
		col.append("  oo.update_date,\n");
		col.append("  oo.update_by,\n");
		col.append("  oo.status,\n");
		col.append("  oo.duty_type,\n");
		col.append("  oo.org_type_id,\n");
		col.append("  oo.voice_person,\n");
		col.append("  oo.msg_type,\n");
		col.append("  oo.is_private,\n");
		col.append("  to_char(oo.expiry_date,'yyyy-mm-dd') expiry_date,\n");
		col.append("  oo.is_top,oo.archive_flag ");
		
		sql.append(" select " + col.toString() + " from tt_as_wr_news oo where 1=1  ");
		if (Utility.testString(newsId))
		{
			sql.append(" and oo.NEWS_Id= ?");
			params.add(newsId);
		}
		logger.info(sql.toString());
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		/*** 彻底解决首页新闻内容限制 YH 2011.5.30 **/
		Map<String, Object> contents_params = new HashMap<String, Object>();
		contents_params.put("NEWS_Id", newsId);
		String contents = "";
		if (type == 1)
		{
			contents = factory.readClob("TT_AS_WR_NEWS", "CONTENTS", contents_params); // 取出内容
																						// YH
																						// 2011.5.30
		}
		
		List<Map<String, Object>> list_f = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++)
		{
			Map<String, Object> map = (Map) list.get(i);
			map.put("CONTENTS", contents);
			list_f.add(map);
		}
		return list_f;
	}
	
	public List<Map<String, Object>> newslist(String newsId)
	{
		List<Object> params = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		StringBuffer col = new StringBuffer(); // 列
		col.append("  oo.news_id,\n");
		col.append("  oo.news_code,\n");
		col.append("  oo.news_title,\n");
		col.append("  oo.news_type,\n");
		col.append("  oo.news_date,\n");
		col.append("  oo.org_id,\n");
		col.append("  oo.org_name,\n");
		// col.append("  to_char(oo.contents) contents,\n" );
		col.append("  oo.oem_company_id,\n");
		col.append("  oo.create_date,\n");
		col.append("  oo.create_by,\n");
		col.append("  oo.update_date,\n");
		col.append("  oo.update_by,\n");
		col.append("  oo.status,\n");
		col.append("  oo.duty_type,\n");
		col.append("  oo.org_type_id,\n");
		col.append("  oo.voice_person,\n");
		col.append("  oo.msg_type,\n");
		col.append("  oo.is_private,\n");
		col.append("  to_char(oo.expiry_date,'yyyy-MM-dd') expiry_date,\n");
		col.append("  oo.is_top,oo.archive_flag ");
		
		sql.append(" select " + col.toString() + " from tt_as_wr_news oo where 1=1  ");
		if (Utility.testString(newsId))
		{
			sql.append(" and oo.NEWS_Id= ?");
			params.add(newsId);
		}
		logger.info(sql.toString());
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		/*** 彻底解决首页新闻内容限制 YH 2011.5.30 **/
		Map<String, Object> contents_params = new HashMap<String, Object>();
		contents_params.put("NEWS_Id", newsId);
		String contents = factory.readClob("TT_AS_WR_NEWS", "CONTENTS", contents_params); // 取出内容
																							// YH
																							// 2011.5.30
		List<Map<String, Object>> list_f = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++)
		{
			Map<String, Object> map = (Map) list.get(i);
			map.put("CONTENTS", contents);
			list_f.add(map);
		}
		return list_f;
	}
	
	/**
	 * Iverson add By 2010-12-09
	 * 
	 * @param newsId
	 * @return
	 */
	public List<Map<String, Object>> newslist1(String newsId)
	{
		List<Object> params = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from tt_as_wr_KnowledgeBase where 1=1  ");
		if (Utility.testString(newsId))
		{
			sql.append(" and Id= ?");
			params.add(newsId);
		}
		logger.info(sql.toString());
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> viewDealer(String dealerCode)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" select dealer_id from tm_dealer  where dealer_code='" + dealerCode + "' ");
		logger.info(sql.toString());
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/********
	 * 关闭操作
	 * 
	 * @param newsId
	 */
	public void closeNews(String newsId)
	{
		String sql = " update tt_as_wr_news set status =" + Constant.NEWS_STATUS_3 + " where news_id=" + newsId;
		factory.update(sql, null);
	}
	
	/**
	 * Iverson add By 2010-12-09 关闭操作
	 * 
	 * @param newsId
	 */
	public void closeNews1(String newsId)
	{
		String sql = " update tt_as_wr_KnowledgeBase set status =" + Constant.NEWS_STATUS_3 + " where id=" + newsId;
		factory.update(sql, null);
	}
	
	/********
	 * 关闭操作
	 * 
	 * @param newsId
	 */
	public void cancelNews(String newsId)
	{
		String sql = " update tt_as_wr_news set status =" + Constant.NEWS_STATUS_2 + " where news_id=" + newsId;
		factory.update(sql, null);
	}
	
	/******
	 * 删除新闻权限表
	 */
	public void delNewsOrg(String newsId)
	{
		String sql = "delete tt_as_wr_news_org o where o.news_id=" + newsId;
		factory.delete(sql, null);
	}
	
	/******
	 * 设置新闻置顶
	 */
	public void setTop(String newsId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("update tt_as_wr_news wn set wn.is_Top=  (select max(topnum)+1 topnum  from (\n");
		sql.append("  select max(wn.is_top)  topnum  from tt_as_wr_news wn   group by wn.is_top )),\n");
		sql.append("  wn.update_date = sysdate where wn.news_id=" + newsId);
		factory.delete(sql.toString(), null);
	}
	
	/******
	 * 设置取消新闻置顶
	 */
	public void dropTop(String newsId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("update tt_as_wr_news wn set wn.is_Top = 0 , \n");
		sql.append("  wn.update_date = sysdate where wn.news_id=" + newsId);
		factory.delete(sql.toString(), null);
	}
	
	/******
	 * 删除新闻权限表
	 */
	public void updateNews(String newsId)
	{
		String sql = "update tt_as_wr_news wn set wn.org_type_id='',wn.duty_type=''  where wn.news_id=" + newsId;
		factory.delete(sql, null);
	}
	
	/******
	 * 删除新闻主体 YH 2011.3.23
	 */
	public void delNews(String newsId)
	{
		String sql = "delete tt_as_wr_news n where n.news_id=" + newsId;
		factory.delete(sql, null);
	}
	
	/******
	 * 根据用户查询出机构ID
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> viewOrgIdUserId(String userId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select org_id from tm_org where company_id=(select company_id from tc_user where user_id=" + userId + ")");
		logger.info(sb.toString());
		List<Map<String, Object>> list = pageQuery(sb.toString(), null, getFunName());
		return list;
	}
	
	/********
	 * 根据经销商ID查询出ORG信息
	 * 
	 * @param dealerId
	 * @return
	 */
	public List<Map<String, Object>> viewOrgIdDealerId(String dealerId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select ROOT_ORG_ID,ORG_ID from VW_ORG_DEALER_SERVICE where dealer_id in (" + dealerId + ")");
		sb.append(" UNION\n");
		sb.append("SELECT ROOT_ORG_ID,ORG_ID FROM VW_ORG_DEALER VOD WHERE VOD.DEALER_ID IN (" + dealerId + ")");
		logger.info(sb.toString());
		List<Map<String, Object>> list = pageQuery(sb.toString(), null, getFunName());
		return list;
	}
	
	/*******
	 * 根据新闻ID 查询出经销商编码
	 * 
	 * @param newsId
	 * @return
	 */
	public List<Map<String, Object>> listNewsParentOrgs(String newsId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select dealer_code from tm_dealer where dealer_id in( select dealer_id from tt_as_wr_news_org where  news_id=" + newsId + ")");
		logger.info(sb.toString());
		List<Map<String, Object>> list = pageQuery(sb.toString(), null, getFunName());
		return list;
	}
	
	/********
	 * 根据orgId查询出ORG_CODE
	 * 
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> listNewsParentOrgsCode(String orgId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select org_code from tm_org where org_id=" + orgId + "");
		logger.info(sb.toString());
		List<Map<String, Object>> list = pageQuery(sb.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 记录经销商阅读记录
	 * 
	 * @param newsId
	 * @param logonUser
	 */
	public void recordDealerRead(String newsId, String newsback, AclUserBean logonUser)
	{
		if(null!=logonUser.getDealerId()){
			TtAsWrNewsReadPO selectPo = new TtAsWrNewsReadPO();
			selectPo.setNewsId(Long.parseLong(newsId));
			selectPo.setDealerId(Long.parseLong(logonUser.getDealerId()));
			
			List<TtAsWrNewsReadPO> list = dao.select(selectPo);
			
			if (list.size() == 0)
			{
				TtAsWrNewsReadPO tswnrPo = new TtAsWrNewsReadPO();
				
				tswnrPo.setNewsId(Long.parseLong(newsId));
				tswnrPo.setDealerId(Long.parseLong(logonUser.getDealerId()));
				tswnrPo.setReadDate(new Date());
				tswnrPo.setNewsback(newsback);
				
				dao.insert(tswnrPo);
			}
		}else{
			TtAsWrNewsReadPO selectPo = new TtAsWrNewsReadPO();
			selectPo.setNewsId(Long.parseLong(newsId));
			selectPo.setReadUserId(logonUser.getUserId());
			
			List<TtAsWrNewsReadPO> list = dao.select(selectPo);
			
			if (list.size() == 0)
			{
				TtAsWrNewsReadPO tswnrPo = new TtAsWrNewsReadPO();
				
				tswnrPo.setNewsId(Long.parseLong(newsId));
				tswnrPo.setReadUserId(logonUser.getUserId());
				tswnrPo.setReadDate(new Date());
				tswnrPo.setNewsback(newsback);
				
				dao.insert(tswnrPo);
			}
		}
	}

	/**
	 * 创建经销商新闻纪录读
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param newsId
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerNewsReadPage(Integer curPage, Integer pageSize, Map<String, Object> paramsMap)
	{
		String newsCode= CommonUtils.checkNull(paramsMap.get("newsCode"));
		String person= CommonUtils.checkNull(paramsMap.get("person"));
		String applyDateStart= CommonUtils.checkNull(paramsMap.get("applyDateStart"));
		String applyDateEnd= CommonUtils.checkNull(paramsMap.get("applyDateEnd"));
		String title= CommonUtils.checkNull(paramsMap.get("title" ));
		String newsStatus= CommonUtils.checkNull(paramsMap.get("newsStatus"));
		String dealerCode= CommonUtils.checkNull(paramsMap.get("dealerCode"));
		
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sbSql.append("SELECT A.NEWS_CODE,\n");
		sbSql.append("       A.NEWS_ID,\n");
		sbSql.append("       A.NEWS_TITLE,\n");
		sbSql.append("       A.NEWS_DATE,\n");
		sbSql.append("       A.NEWS_TYPE,\n");
		sbSql.append("       A.VOICE_PERSON,\n");
		sbSql.append("       A.STATUS,\n");
		sbSql.append("       A.MSG_TYPE,\n");
		sbSql.append("       A.DUTY_TYPE,\n");
		sbSql.append("       (SELECT COUNT(1) FROM TT_AS_WR_NEWS_ORG T1 WHERE T1.NEWS_ID = a.NEWS_ID) NEED_READ,\n");
		sbSql.append("       (SELECT COUNT(1) FROM TT_AS_WR_NEWS_READ T2 WHERE T2.NEWS_ID = a.NEWS_ID) READ_PASS\n");
		sbSql.append("  FROM TT_AS_WR_NEWS A\n");
		//sbSql.append(" WHERE A.DUTY_TYPE = 11781001"); 
		sbSql.append(" WHERE 1=1 "); 
		
		if(!newsCode.equals("")) {
			sbSql.append("   AND    A.NEWS_CODE = ?\n");
			params.add(newsCode);
		}
		
		if(!person.equals("")) {
			sbSql.append("   AND    A.VOICE_PERSON LIKE ?\n");		
			params.add("%" + person + "%");
		}
		
		if(!applyDateStart.equals("")) {
			sbSql.append("   AND    trunc(A.NEWS_DATE) >= to_date(?, 'yyyy-mm-dd')\n");	
			params.add(applyDateStart);
		}
		
		if(!applyDateEnd.equals("")) {
			sbSql.append("   AND    trunc(A.NEWS_DATE) <= to_date(?, 'yyyy-mm-dd')\n");	
			params.add(applyDateEnd);
		}
		
		if(!title.equals("")) {
			sbSql.append("   AND    A.NEWS_TITLE LIKE ?\n");		
			params.add("%"+title+"%");
		}
		
		if(!newsStatus.equals("")) {
			sbSql.append("   AND    A.STATUS = ?\n");		
			params.add(newsStatus);
		}
		
//		if(!dealerCode.equals("")) {
//			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "D", "DEALER_CODE"));
//		}

		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}

	/**
	 * 方法描述：经销商阅读明细查询
	 * @param curPage
	 * @param pageSize
	 * @param paramsMap
	 * @return
	 *
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getDealerNewsDetailReadPage(Integer curPage, Integer pageSize, HashMap<String, Object> paramsMap)
	{
		String newsId = CommonUtils.checkNull(paramsMap.get("newsId"));
		String msgType = CommonUtils.checkNull(paramsMap.get("msgType"));
		String dutyType = CommonUtils.checkNull(paramsMap.get("viewNewsType"));
		String code = CommonUtils.checkNull(paramsMap.get("code"));
		String pName = CommonUtils.checkNull(paramsMap.get("pName"));
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sbSql.append("SELECT * FROM ( SELECT * FROM (SELECT  T1.DEALER_ID,T3.DEALER_CODE,TD.DEALER_NAME,T2.READ_DATE,CASE  WHEN  T2.NEWSBACK  IS  NULL  THEN  '未阅'  ELSE  T2.NEWSBACK  END  NEWSBACK  ,T3.ROOT_ORG_NAME,T3.ORG_NAME  FROM  TT_AS_WR_NEWS_ORG  T1 \n");
		sbSql.append(" LEFT  JOIN  TT_AS_WR_NEWS_READ  T2  ON  T1.NEWS_ID  =  T2.NEWS_ID  AND  T1.DEALER_ID  =  T2.DEALER_ID \n");
		sbSql.append(" LEFT  JOIN  TM_DEALER  TD  ON  T1.DEALER_ID  =  TD.DEALER_ID \n");
		sbSql.append(" LEFT  JOIN  VW_ORG_DEALER_SERVICE  T3  ON  T3.DEALER_ID  =  T1.DEALER_ID \n");
		sbSql.append(" WHERE  T1.NEWS_ID  =  ?  \n");
		sbSql.append(" AND  T3.DEALER_CODE IS NOT NULL  \n");
		DaoFactory.getsql(sbSql, "T3.ROOT_ORG_ID", (String)paramsMap.get("rootOrgId"), 1);
		DaoFactory.getsql(sbSql, "T3.ORG_ID", (String)paramsMap.get("orgId"), 1);
		DaoFactory.getsql(sbSql, "T1.DEALER_ID", (String)paramsMap.get("dealerIds"), 6);
		if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
			if ("1".equals((String)paramsMap.get("isRead"))) {
				sbSql.append(" AND T2.NEWSBACK IS NULL  \n");
			}else {
				sbSql.append(" AND T2.NEWSBACK IS NOT NULL  \n");
			}
		}
		sbSql.append(" ORDER  BY  T2.NEWSBACK  ,T2.READ_DATE  DESC ,T3.ROOT_ORG_ID,T3.ORG_ID )");
		if(Constant.MSG_TYPE_3.equals(msgType) && !Constant.VIEW_NEWS_type_2.toString().equals(dutyType)){
			sbSql.append(" union \n");
			sbSql.append(" select  *  from  ( \n");
			sbSql.append(" select  c.dealer_id, \n");
			sbSql.append("               c.dealer_code, \n");
			sbSql.append("               c.dealer_name, \n");
			sbSql.append("               d.read_date, \n");
			sbSql.append("               CASE \n");
			sbSql.append("                   WHEN  d.NEWSBACK  IS  NULL  THEN \n");
			sbSql.append("                     '未阅' \n");
			sbSql.append("                   ELSE \n");
			sbSql.append("                     d.NEWSBACK \n");
			sbSql.append("               END  NEWSBACK, \n");
			sbSql.append("               c.root_org_name, \n");
			sbSql.append("               c.org_name \n");
			sbSql.append("     from  vw_org_dealer_service  c  left  join  (select  b.*  from  Tt_As_Wr_News  a,TT_AS_WR_NEWS_READ  b \n");
			sbSql.append("     where  a.news_id=b.news_id  and  a.news_id=?)  d \n");
			sbSql.append("     on  c.dealer_id=d.dealer_id \n");
			sbSql.append("     where  c.dealer_level=10851001 \n");
			DaoFactory.getsql(sbSql, "c.ROOT_ORG_ID", (String)paramsMap.get("rootOrgId"), 1);
			DaoFactory.getsql(sbSql, "c.ORG_ID", (String)paramsMap.get("orgId"), 1);
			DaoFactory.getsql(sbSql, "c.DEALER_ID", (String)paramsMap.get("dealerIds"), 6);
			if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
				if ("1".equals((String)paramsMap.get("isRead"))) {
					sbSql.append(" AND d.NEWSBACK IS NULL  \n");
				}else {
					sbSql.append(" AND d.NEWSBACK IS NOT NULL  \n");
				}
			}
			sbSql.append("     order  by  d.newsback,c.root_org_id,c.org_id \n");
			sbSql.append("     ) \n");
			params.add(newsId);
		}
		params.add(newsId);
		//新增车厂阅读明细
		sbSql.append("union\n");
		sbSql.append(" SELECT *\n");
		sbSql.append("  FROM (SELECT T1.DEALER_ID,\n");
		sbSql.append("               T3.ACNT DEALER_CODE,\n");
		sbSql.append("               T3.NAME DEALER_NAME,\n");
		sbSql.append("               T2.READ_DATE,\n");
		sbSql.append("               CASE\n");
		sbSql.append("                 WHEN T2.NEWSBACK IS NULL THEN\n");
		sbSql.append("                  '未阅'\n");
		sbSql.append("                 ELSE\n");
		sbSql.append("                  T2.NEWSBACK\n");
		sbSql.append("               END NEWSBACK,\n");
		sbSql.append("               '' ROOT_ORG_NAME,\n");
		sbSql.append("               '' ORG_NAME\n");
		sbSql.append("          FROM TT_AS_WR_NEWS_ORG T1\n");
		sbSql.append("          LEFT JOIN TT_AS_WR_NEWS_READ T2 ON T1.NEWS_ID = T2.NEWS_ID\n");
		sbSql.append("                                         AND T1.DEALER_ID = T2.READ_USER_ID\n");
		sbSql.append("          LEFT JOIN TC_USER T3 ON T3.USER_ID = T1.DEALER_ID\n");
		sbSql.append("         WHERE T1.NEWS_ID = ?\n");
		sbSql.append(" 	and T3.ACNT is not null \n");
		if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
			if ("1".equals((String)paramsMap.get("isRead"))) {
				sbSql.append(" AND T2.NEWSBACK IS NULL  \n");
			}else {
				sbSql.append(" AND T2.NEWSBACK IS NOT NULL  \n");
			}
		}
		sbSql.append("         ORDER BY T2.NEWSBACK, T2.READ_DATE DESC)\n");
		params.add(newsId);
		if(Constant.VIEW_NEWS_type_3.toString().equals(dutyType)){
			//公共新闻关联
			sbSql.append("  union\n");
			sbSql.append("       select * from (select c.user_id dealer_id,\n");
			sbSql.append("               c.acnt dealer_code,\n");
			sbSql.append("               c.name dealer_name,\n");
			sbSql.append("               d.read_date,\n");
			sbSql.append("               CASE\n");
			sbSql.append("                 WHEN d.NEWSBACK IS NULL THEN\n");
			sbSql.append("                  '未阅'\n");
			sbSql.append("                 ELSE\n");
			sbSql.append("                  d.NEWSBACK\n");
			sbSql.append("               END NEWSBACK,\n");
			sbSql.append("               '' root_org_name,\n");
			sbSql.append("               '' org_name\n");
			sbSql.append("          from tc_user c\n");
			sbSql.append("          left join (select b.*,a.duty_type\n");
			sbSql.append("                      from Tt_As_Wr_News a, TT_AS_WR_NEWS_READ b\n");
			sbSql.append("                     where a.news_id = b.news_id\n");
			sbSql.append("                       and a.duty_type=11781003\n");
			sbSql.append("                       and a.news_id = ?) d on c.user_id = d.read_user_id\n");
			sbSql.append("         where c.user_type=10021001 \n");
			if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
				if ("1".equals((String)paramsMap.get("isRead"))) {
					sbSql.append(" AND d.NEWSBACK IS NULL  \n");
				}else {
					sbSql.append(" AND d.NEWSBACK IS NOT NULL  \n");
				}
			}
			sbSql.append("         order by d.newsback)"); 
			
			params.add(newsId);
		}
		sbSql.append("     ) T where 1=1"); 
		if(!"".equals(code)){
			sbSql.append(" AND T.DEALER_CODE  LIKE '%"+code+"%' \n");
		}
		if(!"".equals(pName)){
			sbSql.append(" AND T.DEALER_NAME  LIKE '%"+pName+"%' \n");
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 方法描述：经销商阅读明细查询
	 * @param curPage
	 * @param pageSize
	 * @param paramsMap
	 * @return
	 *
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getDealerNewsDetailExport(Integer curPage, Integer pageSize, HashMap<String, Object> paramsMap)
	{
		/*String newsId = CommonUtils.checkNull(paramsMap.get("newsId"));
		String msgType = CommonUtils.checkNull(paramsMap.get("msgType"));
		
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sbSql.append(" SELECT * FROM (SELECT  T1.DEALER_ID,T3.DEALER_CODE,TD.DEALER_NAME,T2.READ_DATE,CASE  WHEN  T2.NEWSBACK  IS  NULL  THEN  '未阅'  ELSE  T2.NEWSBACK  END  NEWSBACK  ,T3.ROOT_ORG_NAME,T3.ORG_NAME  FROM  TT_AS_WR_NEWS_ORG  T1 \n");
		sbSql.append(" LEFT  JOIN  TT_AS_WR_NEWS_READ  T2  ON  T1.NEWS_ID  =  T2.NEWS_ID  AND  T1.DEALER_ID  =  T2.DEALER_ID \n");
		sbSql.append(" LEFT  JOIN  TM_DEALER  TD  ON  T1.DEALER_ID  =  TD.DEALER_ID \n");
		sbSql.append(" LEFT  JOIN  VW_ORG_DEALER_SERVICE  T3  ON  T3.DEALER_ID  =  T1.DEALER_ID \n");
		sbSql.append(" WHERE  T1.NEWS_ID  =  ?  \n");
		DaoFactory.getsql(sbSql, "T3.ROOT_ORG_ID", (String)paramsMap.get("rootOrgId"), 1);
		DaoFactory.getsql(sbSql, "T3.ORG_ID", (String)paramsMap.get("orgId"), 1);
		DaoFactory.getsql(sbSql, "T1.DEALER_ID", (String)paramsMap.get("dealerIds"), 6);
		if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
			if ("1".equals((String)paramsMap.get("isRead"))) {
				sbSql.append(" AND T2.NEWSBACK IS NULL  \n");
			}else {
				sbSql.append(" AND T2.NEWSBACK IS NOT NULL  \n");
			}
		}
		sbSql.append(" ORDER  BY  T2.NEWSBACK  ,T2.READ_DATE  DESC ,T3.ROOT_ORG_ID,T3.ORG_ID )");
		if(Constant.MSG_TYPE_3.equals(msgType)){
			sbSql.append(" union \n");
			sbSql.append(" select  *  from  ( \n");
			sbSql.append(" select  c.dealer_id, \n");
			sbSql.append("               c.dealer_code, \n");
			sbSql.append("               c.dealer_name, \n");
			sbSql.append("               d.read_date, \n");
			sbSql.append("               CASE \n");
			sbSql.append("                   WHEN  d.NEWSBACK  IS  NULL  THEN \n");
			sbSql.append("                     '未阅' \n");
			sbSql.append("                   ELSE \n");
			sbSql.append("                     d.NEWSBACK \n");
			sbSql.append("               END  NEWSBACK, \n");
			sbSql.append("               c.root_org_name, \n");
			sbSql.append("               c.org_name \n");
			sbSql.append("     from  vw_org_dealer_service  c  left  join  (select  b.*  from  Tt_As_Wr_News  a,TT_AS_WR_NEWS_READ  b \n");
			sbSql.append("     where  a.news_id=b.news_id  and  a.news_id=?)  d \n");
			sbSql.append("     on  c.dealer_id=d.dealer_id \n");
			sbSql.append("     where  c.dealer_level=10851001 \n");
			DaoFactory.getsql(sbSql, "c.ROOT_ORG_ID", (String)paramsMap.get("rootOrgId"), 1);
			DaoFactory.getsql(sbSql, "c.ORG_ID", (String)paramsMap.get("orgId"), 1);
			DaoFactory.getsql(sbSql, "c.DEALER_ID", (String)paramsMap.get("dealerIds"), 6);
			if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
				if ("1".equals((String)paramsMap.get("isRead"))) {
					sbSql.append(" AND d.NEWSBACK IS NULL  \n");
				}else {
					sbSql.append(" AND d.NEWSBACK IS NOT NULL  \n");
				}
			}
			sbSql.append("     order  by  d.newsback,c.root_org_id,c.org_id \n");
			sbSql.append("     ) \n");
			params.add(newsId);
		}
		params.add(newsId);*/
		String newsId = CommonUtils.checkNull(paramsMap.get("newsId"));
		String msgType = CommonUtils.checkNull(paramsMap.get("msgType"));
		String dutyType = CommonUtils.checkNull(paramsMap.get("viewNewsType"));
		String code = CommonUtils.checkNull(paramsMap.get("code"));
		String pName = CommonUtils.checkNull(paramsMap.get("pName"));
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sbSql.append("SELECT * FROM ( SELECT * FROM (SELECT  T1.DEALER_ID,T3.DEALER_CODE,TD.DEALER_NAME,T2.READ_DATE,CASE  WHEN  T2.NEWSBACK  IS  NULL  THEN  '未阅'  ELSE  T2.NEWSBACK  END  NEWSBACK  ,T3.ROOT_ORG_NAME,T3.ORG_NAME  FROM  TT_AS_WR_NEWS_ORG  T1 \n");
		sbSql.append(" LEFT  JOIN  TT_AS_WR_NEWS_READ  T2  ON  T1.NEWS_ID  =  T2.NEWS_ID  AND  T1.DEALER_ID  =  T2.DEALER_ID \n");
		sbSql.append(" LEFT  JOIN  TM_DEALER  TD  ON  T1.DEALER_ID  =  TD.DEALER_ID \n");
		sbSql.append(" LEFT  JOIN  VW_ORG_DEALER_SERVICE  T3  ON  T3.DEALER_ID  =  T1.DEALER_ID \n");
		sbSql.append(" WHERE  T1.NEWS_ID  =  ?  \n");
		sbSql.append(" AND  T3.DEALER_CODE IS NOT NULL  \n");
		DaoFactory.getsql(sbSql, "T3.ROOT_ORG_ID", (String)paramsMap.get("rootOrgId"), 1);
		DaoFactory.getsql(sbSql, "T3.ORG_ID", (String)paramsMap.get("orgId"), 1);
		DaoFactory.getsql(sbSql, "T1.DEALER_ID", (String)paramsMap.get("dealerIds"), 6);
		if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
			if ("1".equals((String)paramsMap.get("isRead"))) {
				sbSql.append(" AND T2.NEWSBACK IS NULL  \n");
			}else {
				sbSql.append(" AND T2.NEWSBACK IS NOT NULL  \n");
			}
		}
		sbSql.append(" ORDER  BY  T2.NEWSBACK  ,T2.READ_DATE  DESC ,T3.ROOT_ORG_ID,T3.ORG_ID )");
		if(Constant.MSG_TYPE_3.equals(msgType) && !Constant.VIEW_NEWS_type_2.toString().equals(dutyType)){
			sbSql.append(" union \n");
			sbSql.append(" select  *  from  ( \n");
			sbSql.append(" select  c.dealer_id, \n");
			sbSql.append("               c.dealer_code, \n");
			sbSql.append("               c.dealer_name, \n");
			sbSql.append("               d.read_date, \n");
			sbSql.append("               CASE \n");
			sbSql.append("                   WHEN  d.NEWSBACK  IS  NULL  THEN \n");
			sbSql.append("                     '未阅' \n");
			sbSql.append("                   ELSE \n");
			sbSql.append("                     d.NEWSBACK \n");
			sbSql.append("               END  NEWSBACK, \n");
			sbSql.append("               c.root_org_name, \n");
			sbSql.append("               c.org_name \n");
			sbSql.append("     from  vw_org_dealer_service  c  left  join  (select  b.*  from  Tt_As_Wr_News  a,TT_AS_WR_NEWS_READ  b \n");
			sbSql.append("     where  a.news_id=b.news_id  and  a.news_id=?)  d \n");
			sbSql.append("     on  c.dealer_id=d.dealer_id \n");
			sbSql.append("     where  c.dealer_level=10851001 \n");
			DaoFactory.getsql(sbSql, "c.ROOT_ORG_ID", (String)paramsMap.get("rootOrgId"), 1);
			DaoFactory.getsql(sbSql, "c.ORG_ID", (String)paramsMap.get("orgId"), 1);
			DaoFactory.getsql(sbSql, "c.DEALER_ID", (String)paramsMap.get("dealerIds"), 6);
			if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
				if ("1".equals((String)paramsMap.get("isRead"))) {
					sbSql.append(" AND d.NEWSBACK IS NULL  \n");
				}else {
					sbSql.append(" AND d.NEWSBACK IS NOT NULL  \n");
				}
			}
			sbSql.append("     order  by  d.newsback,c.root_org_id,c.org_id \n");
			sbSql.append("     ) \n");
			params.add(newsId);
		}
		params.add(newsId);
		//新增车厂阅读明细
		sbSql.append("union\n");
		sbSql.append(" SELECT *\n");
		sbSql.append("  FROM (SELECT T1.DEALER_ID,\n");
		sbSql.append("               T3.ACNT DEALER_CODE,\n");
		sbSql.append("               T3.NAME DEALER_NAME,\n");
		sbSql.append("               T2.READ_DATE,\n");
		sbSql.append("               CASE\n");
		sbSql.append("                 WHEN T2.NEWSBACK IS NULL THEN\n");
		sbSql.append("                  '未阅'\n");
		sbSql.append("                 ELSE\n");
		sbSql.append("                  T2.NEWSBACK\n");
		sbSql.append("               END NEWSBACK,\n");
		sbSql.append("               '' ROOT_ORG_NAME,\n");
		sbSql.append("               '' ORG_NAME\n");
		sbSql.append("          FROM TT_AS_WR_NEWS_ORG T1\n");
		sbSql.append("          LEFT JOIN TT_AS_WR_NEWS_READ T2 ON T1.NEWS_ID = T2.NEWS_ID\n");
		sbSql.append("                                         AND T1.DEALER_ID = T2.READ_USER_ID\n");
		sbSql.append("          LEFT JOIN TC_USER T3 ON T3.USER_ID = T1.DEALER_ID\n");
		sbSql.append("         WHERE T1.NEWS_ID = ?\n");
		sbSql.append(" 	and T3.ACNT is not null \n");
		if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
			if ("1".equals((String)paramsMap.get("isRead"))) {
				sbSql.append(" AND T2.NEWSBACK IS NULL  \n");
			}else {
				sbSql.append(" AND T2.NEWSBACK IS NOT NULL  \n");
			}
		}
		sbSql.append("         ORDER BY T2.NEWSBACK, T2.READ_DATE DESC)\n");
		params.add(newsId);
		if(Constant.VIEW_NEWS_type_3.toString().equals(dutyType)){
			//公共新闻关联
			sbSql.append("  union\n");
			sbSql.append("       select * from (select c.user_id dealer_id,\n");
			sbSql.append("               c.acnt dealer_code,\n");
			sbSql.append("               c.name dealer_name,\n");
			sbSql.append("               d.read_date,\n");
			sbSql.append("               CASE\n");
			sbSql.append("                 WHEN d.NEWSBACK IS NULL THEN\n");
			sbSql.append("                  '未阅'\n");
			sbSql.append("                 ELSE\n");
			sbSql.append("                  d.NEWSBACK\n");
			sbSql.append("               END NEWSBACK,\n");
			sbSql.append("               '' root_org_name,\n");
			sbSql.append("               '' org_name\n");
			sbSql.append("          from tc_user c\n");
			sbSql.append("          left join (select b.*,a.duty_type\n");
			sbSql.append("                      from Tt_As_Wr_News a, TT_AS_WR_NEWS_READ b\n");
			sbSql.append("                     where a.news_id = b.news_id\n");
			sbSql.append("                       and a.duty_type=11781003\n");
			sbSql.append("                       and a.news_id = ?) d on c.user_id = d.read_user_id\n");
			sbSql.append("         where c.user_type=10021001 \n");
			if(!"".equals(CommonUtils.checkNull((String)paramsMap.get("isRead")))){
				if ("1".equals((String)paramsMap.get("isRead"))) {
					sbSql.append(" AND d.NEWSBACK IS NULL  \n");
				}else {
					sbSql.append(" AND d.NEWSBACK IS NOT NULL  \n");
				}
			}
			sbSql.append("         order by d.newsback)"); 
			
			params.add(newsId);
		}
		sbSql.append("     ) T where 1=1"); 
		if(!"".equals(code)){
			sbSql.append(" AND T.DEALER_CODE  LIKE '%"+code+"%' \n");
		}
		if(!"".equals(pName)){
			sbSql.append(" AND T.DEALER_NAME  LIKE '%"+pName+"%' \n");
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}

	/**
	 * 删除新闻发送区域数据
	 * 
	 * @param newsId
	 * 		新闻表ID
	 */
	@SuppressWarnings("unchecked")
	public int deleteNewsSendOrgs(String newsId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "delete from tt_as_wr_news_send where news_id = ?";
		params.add(newsId);
		return this.delete(sql, params);
	}

	/**
	 * 删除新闻渠道数据
	 * 
	 * @param newsId
	 * 		新闻表ID
	 */
	@SuppressWarnings("unchecked")
	public int deleteNewsChannel(String newsId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "delete from tt_as_wr_news_channels where news_id = ?";
		params.add(newsId);
		return this.delete(sql, params);
	}

	/**
	 * 保存新闻渠道数据
	 * @param newsId 
	 * 		新闻表ID
	 * @param channel
	 * 		渠道数据 type: array
	 */
	@SuppressWarnings("unchecked")
	public void saveNewsChannels(String newsId, String[] channel) {
		List<Object> params = new ArrayList<Object>();
		String sql = "insert into tt_as_wr_news_channels(news_id, channel_id) values (?,?)";
		for(String channelId : channel) {
			params.clear();
			params.add(newsId);
			params.add(channelId);
			this.insert(sql, params);
		}
	}

	/**
	 * 保存新闻发送组织数据
	 * @param newsId 
	 * 		新闻表ID 
	 * @param sendOrgList
	 * 		大区、小区组织ID type： array
	 */
	@SuppressWarnings("unchecked")
	public void saveNewsSendOrgs(String newsId, String[] sendOrgList) {
		List<Object> params = new ArrayList<Object>();
		String sql = "insert into tt_as_wr_news_send(news_id,org_id) values (?,?)";
		for(String sendOrgId : sendOrgList) {
			params.clear();
			params.add(newsId);
			params.add(sendOrgId);
			this.insert(sql, params);
		}
	}

	/**
	 * 根据新闻ID获取新闻发布的业务渠道
	 * @param newsId
	 * 		新闻表ID 
	 * @return 
	 * 		List&lt;Map&lt;String, Object&gt;&gt;
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNewsChannelListById(String newsId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from tt_as_wr_news_channels where news_id = ?";
		params.add(newsId);
		return this.pageQuery(sql, params, getFunName());
	}

	/**
	 * 根据新闻ID获取新闻发送组织范围
	 * @param newsId
	 * 		新闻表ID 
	 * @return 
	 * 		List&lt;Map&lt;String, Object&gt;&gt;
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNewsSendOrgListById(String newsId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from tt_as_wr_news_send where news_id = ?";
		params.add(newsId);
		return this.pageQuery(sql, params, getFunName());
	}
}
