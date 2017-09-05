

package com.infodms.dms.dao.sales.usermng;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class MiniBlogMngDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(MiniBlogMngDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final  MiniBlogMngDAO dao = new MiniBlogMngDAO();
	
	public static MiniBlogMngDAO getInstance() {
		return dao;
	}

	
@Override
protected PO wrapperPO(ResultSet rs, int idx) {
	// TODO Auto-generated method stub
	return null;
}

public PageResult<Map<String, Object>> queryBlogAppInfo(
		Map<String, String> map, Integer pageSize, int curPage) {
	String blogNo=map.get("blogNo");
	String dealerId=map.get("dealerId");
	String year=map.get("year");
	String integ_month=map.get("integ_month");
	StringBuilder sql= new StringBuilder();
	sql.append("SELECT TD.DEALER_CODE,\n" );
	sql.append("       TD.DEALER_NAME,\n" );
	sql.append("       TVB.BLOG_ID,\n" );
	sql.append("       TVB.BLOG_NO,\n" );
	sql.append("       TVB.INTEG_MONTH,\n" );
	sql.append("       TVB.YEAR,\n" );
	sql.append("       TVB.DEALER_ID,\n" );
	sql.append("       TVB.STATUS\n" );
	sql.append("  FROM TT_VS_BLOG TVB, TM_DEALER TD\n" );
	sql.append(" WHERE TD.DEALER_ID = TVB.DEALER_ID");
	sql.append(" 	AND (TVB.STATUS ="+Constant.WEB_TYPE_01+"\n");
	sql.append(" 	OR TVB.STATUS ="+Constant.WEB_TYPE_04+")\n");
	if(blogNo!=null&&!"".equals(blogNo)){
		sql.append(" AND TVB.BLOG_NO='"+blogNo+"'\n");
	}
	if(dealerId!=null&&!"".equals(dealerId)){
		sql.append(" AND TD.DEALER_ID="+dealerId+"\n");
	}
	if(year!=null&&!"".equals(year)){
		sql.append(" AND TVB.YEAR="+year+"\n");
	}
	if(integ_month!=null&&!"".equals(integ_month)){
		sql.append(" AND TVB.INTEG_MONTH="+integ_month+"\n");
	}
	PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
	return ps;
}
public PageResult<Map<String, Object>> queryBlogAuditInfo(
		Map<String, String> map, Integer pageSize, int curPage) {
	String blog_no=map.get("blogNo");
	String year=map.get("year");
	String integ_month=map.get("integ_month");
	StringBuilder sql= new StringBuilder();
	sql.append("SELECT TD.DEALER_CODE,\n" );
	sql.append("       TD.DEALER_NAME,\n" );
	sql.append("       TVB.BLOG_ID,\n" );
	sql.append("       TVB.BLOG_NO,\n" );
	sql.append("       TVB.INTEG_MONTH,\n" );
	sql.append("       TVB.YEAR,\n" );
	sql.append("       TVB.DEALER_ID,\n" );
	sql.append("       TVB.STATUS\n" );
	sql.append("  FROM TT_VS_BLOG TVB, TM_DEALER TD\n" );
	sql.append(" WHERE TD.DEALER_ID = TVB.DEALER_ID");
	sql.append(" 	AND TVB.STATUS ="+Constant.WEB_TYPE_02+"\n");
	if(blog_no!=null&&!"".equals(blog_no)){
		sql.append(" AND TVB.BLOG_NO='"+blog_no+"'\n");
	}
	if(year!=null&&!"".equals(year)){
		sql.append(" AND TVB.YEAR="+year+"\n");
	}
	if(integ_month!=null&&!"".equals(integ_month)){
		sql.append(" AND TVB.INTEG_MONTH="+integ_month+"\n");
	}
	PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
	return ps;
}
/**
 * 查询人员
 * @param map
 * @param pageSize
 * @param curPage
 * @return
 */
public PageResult<Map<String, Object>> getSalesManList(Map<String, String> map,
		Integer pageSize, Integer curPage) {
	String selectedMan=map.get("selectedMan");
	String dealerId=map.get("dealerId");
	StringBuilder sql= new StringBuilder();
	sql.append("SELECT TVP.PERSON_ID,TVP.NAME, TVP.MOBILE, TVP.EMAIL\n" );
	sql.append("  FROM TT_VS_PERSON TVP\n" );
	sql.append(" WHERE TVP.POSITION_STATUS = 99941001\n" );
	sql.append("   AND TVP.DEALER_ID = "+dealerId);
	if(selectedMan!=null&&!"".equals(selectedMan)){
		selectedMan=CommonUtils.getSplitStringForIn(selectedMan);
		sql.append("   AND TVP.PERSON_ID NOT IN("+selectedMan+")");
	}
	PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
	return ps;
}
/**
 * 获取先前产生的子表数据
 * @param blogId
 * @return
 */
public List<Map<String,Object>> selectDetailList(String blogId) {
	StringBuilder sql= new StringBuilder();
	sql.append("SELECT TVBD.DETAIL_ID,\n" );
	sql.append("       TVP.NAME,\n" );
	sql.append("       TVBD.BLOG_ONE,\n" );
	sql.append("       TVBD.BLOG_TWO,\n" );
	sql.append("       TVBD.BLOG_THREE,\n" );
	sql.append("       TVBD.BLOG_INTEG,\n" );
	sql.append("       TVBD.SALES_ID\n" );
	sql.append("  FROM TT_VS_PERSON TVP, TT_VS_BLOG_DETAIL TVBD, TT_VS_BLOG TVB\n" );
	sql.append(" WHERE TVP.PERSON_ID = TVBD.SALES_ID\n" );
	sql.append("   AND TVB.BLOG_ID = TVBD.BLOG_ID\n");
	sql.append("   AND TVB.BLOG_ID ="+blogId);
	List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
	return list;
}
public PageResult<Map<String, Object>> queryBlogQueryInfo(
		Map<String, String> map, Integer pageSize, int curPage) {
	String blogNo=map.get("blogNo");
	String dealerId=map.get("dealerId");
	String year=map.get("year");
	String integ_month=map.get("integ_month");
	StringBuilder sql= new StringBuilder();
	sql.append("SELECT TD.DEALER_CODE,\n" );
	sql.append("       TD.DEALER_NAME,\n" );
	sql.append("       TVB.BLOG_ID,\n" );
	sql.append("       TVB.BLOG_NO,\n" );
	sql.append("       TVB.INTEG_MONTH,\n" );
	sql.append("       TVB.YEAR,\n" );
	sql.append("       TVB.DEALER_ID,\n" );
	sql.append("       TVB.STATUS\n" );
	sql.append("  FROM TT_VS_BLOG TVB, TM_DEALER TD\n" );
	sql.append(" WHERE TD.DEALER_ID = TVB.DEALER_ID");
	if(blogNo!=null&&!"".equals(blogNo)){
		sql.append(" AND TVB.BLOG_NO LIKE'%"+blogNo+"%'\n");
	}
	if(dealerId!=null&&!"".equals(dealerId)){
		sql.append(" AND TD.DEALER_ID="+dealerId+"\n");
	}
	if(year!=null&&!"".equals(year)){
		sql.append(" AND TVB.YEAR="+year+"\n");
	}
	if(integ_month!=null&&!"".equals(integ_month)){
		sql.append(" AND TVB.INTEG_MONTH="+integ_month+"\n");
	}
	PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
	return ps;
}
}
