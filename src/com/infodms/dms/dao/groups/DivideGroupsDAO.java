package com.infodms.dms.dao.groups;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtProductDistributionPO;
import com.infodms.dms.po.TtUnitGroupDtlPO;
import com.infodms.dms.po.TtUnitGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DivideGroupsDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(DivideGroupsDAO.class);
	private static DivideGroupsDAO dao = new DivideGroupsDAO() ;
	
	private DivideGroupsDAO() {
		
	} ;
	
	public static DivideGroupsDAO getInstance() {
		return dao ;
	}
	
	/**插入主表信息
	 * @param map
	 * @return
	 */
	public Long insertGroup(Map<String, String> map) {
		Long headId = Long.parseLong(SequenceManager.getSequence("")) ;
		String groupName = map.get("groupName") ;
		String groupType = map.get("groupType") ;
		String groupArea = map.get("groupArea") ;
		String groupStatus = map.get("groupStatus") ;
		String userId = map.get("userId") ;
		
		TtUnitGroupPO tug = new TtUnitGroupPO() ;
		
		tug.setGroupId(headId) ;
		tug.setGroupName(groupName) ;
		tug.setGroupType(Integer.parseInt(groupType)) ;
		tug.setGroupArea(Integer.parseInt(groupArea)) ;
		tug.setGroupStatus(Integer.parseInt(groupStatus)) ;
		tug.setCreateDate(new Date(System.currentTimeMillis())) ;
		tug.setCreateBy(Long.parseLong(userId)) ;
		
		super.insert(tug) ;
		
		return headId ;
	}
	
	/**
	 * 插入分组明细信息
	 * @param map
	 */
	public void insertGroupDtl(Map<String, String> map) {
		Long dtlId = Long.parseLong(SequenceManager.getSequence("")) ;
		String headId = map.get("headId") ;
		String theId = map.get("theId") ;
		String theCode = map.get("theCode") ;
		String theName = map.get("theName") ;
		String userId = map.get("userId") ;
		
		TtUnitGroupDtlPO tugd = new TtUnitGroupDtlPO() ;
		
		tugd.setGroupDtlId(dtlId) ;
		tugd.setGroupId(Long.parseLong(headId)) ;
		tugd.setTheId(Long.parseLong(theId)) ;
		tugd.setTheCode(theCode) ;
		tugd.setTheName(theName) ;
		tugd.setCreateDate(new Date(System.currentTimeMillis())) ;
		tugd.setCreateBy(Long.parseLong(userId)) ;
		
		super.insert(tugd) ;
	}
	
	//zhumingwei 2011-10-24
	public void insertProductDistribution(Map<String, String> map) {
		Long pdId = Long.parseLong(SequenceManager.getSequence("")) ;
		String productId = map.get("productId") ;
		String theId = map.get("theId") ;
		String theCode = map.get("theCode") ;
		String theName = map.get("theName") ;
		String userId = map.get("userId") ;
		
		TtProductDistributionPO tpd = new TtProductDistributionPO() ;
		
		tpd.setProductDistributionId(pdId);
		tpd.setProductId(Long.parseLong(productId)) ;
		tpd.setCompanyId(Long.parseLong(theId)) ;
		tpd.setCompanyCode(theCode) ;
		tpd.setCompanyName(theName) ;
		tpd.setCreateDate(new Date(System.currentTimeMillis())) ;
		tpd.setCreateBy(Long.parseLong(userId)) ;
		
		super.insert(tpd) ;
	}
	
	/**
	 * 修改主表信息
	 * @param map
	 */
	public void updateGroup(Map<String, String> map) {
		String headId = map.get("headId") ;
		String groupName = map.get("groupName") ;
		String groupType = map.get("groupType") ;
		String groupArea = map.get("groupArea") ;
		String groupStatus = map.get("groupStatus") ;
		String userId = map.get("userId") ;
		
		TtUnitGroupPO oldTug = new TtUnitGroupPO() ;
		oldTug.setGroupId(Long.parseLong(headId)) ;
		
		TtUnitGroupPO tug = new TtUnitGroupPO() ;
		tug.setGroupName(groupName) ;
		tug.setGroupType(Integer.parseInt(groupType)) ;
		tug.setGroupArea(Integer.parseInt(groupArea)) ;
		tug.setGroupStatus(Integer.parseInt(groupStatus)) ;
		tug.setUpdateDate(new Date(System.currentTimeMillis())) ;
		tug.setUpdateBy(Long.parseLong(userId)) ;
		
		super.update(oldTug, tug) ;
	}
	
	/**
	 * 删除明细信息
	 * @param map
	 */
	public void deleteGroupDtl(Map<String, String> map) {
		String headId = map.get("headId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("delete tt_unit_group_dtl where group_id = ").append(headId).append("\n") ;
		
		super.delete(sql.toString(), null) ;
	}
	
	//zhumingwei 2011-10-24
	public void deleteProductDistribution(Map<String, String> map) {
		String productId = map.get("productId") ;
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("delete tt_product_distribution where product_id = ").append(productId).append("\n") ;
		
		super.delete(sql.toString(), null) ;
	}
	
	public PageResult<Map<String, Object>> queryGroup(Map<String, String> map, int pageSize, int curPage) {
		String groupName = map.get("groupName") ;
		String groupType = map.get("groupType") ;
		String groupStatus = map.get("groupStatus") ;
		String groupArea = map.get("groupArea") ;
		String userId = map.get("userId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tug.group_id,\n");
		sql.append("       tug.group_name,\n");  
		sql.append("       tug.group_type,\n");  
		sql.append("       tug.group_area,\n");  
		sql.append("       tug.group_status,\n");  
		sql.append("       to_char(tug.create_date, 'yyyy-mm-dd') create_date\n");  
		sql.append("  from tt_unit_group tug\n");  
		sql.append("  where 1 = 1\n");  
		
		if(!CommonUtils.isNullString(groupName))
			sql.append("  and tug.group_name like '%").append(groupName).append("%'\n");  
		
		if(!CommonUtils.isNullString(groupType))
			sql.append("  and tug.group_type = ").append(groupType).append("\n");  
		
		if(!CommonUtils.isNullString(groupStatus))
			sql.append("  and tug.group_status = ").append(groupStatus).append("\n");
		
		if(!CommonUtils.isNullString(groupArea))
			sql.append("  and tug.group_area = ").append(groupArea).append("\n");
			
		sql.append("  and tug.create_by = ").append(userId).append("\n");

		return super.pageQuery(sql.toString(), null, super.getFunName(), pageSize, curPage) ;
	}
	
	public PageResult<Map<String, Object>> queryGroupDtlDlrCompayAll(Map<String, String> map, int pageSize, int curPage) {
		String headId = map.get("headId") ;
		String dlrType = map.get("dlrType") ;
		String queryName = map.get("queryName") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select *\n");
		sql.append("  from (select tugd.group_dtl_id,\n");  
		sql.append("               tugd.the_id,\n");  
		sql.append("               tugd.the_name,\n");  
		sql.append("               tugd.the_code,\n");  
		sql.append("               1 is_check\n");  
		sql.append("          from tt_unit_group_dtl tugd\n");  
		sql.append("         where 1 = 1\n");  
		sql.append("           and tugd.group_id = ").append(headId).append("\n");  
		sql.append("        union all\n");  
		sql.append("        select 0000000000000000 group_dtl_id,\n");  
		sql.append("               tmc.company_id   the_id,\n");  
		sql.append("               tmc.company_name the_name,\n");  
		sql.append("               tmc.company_code the_code,\n");  
		sql.append("               0                is_check\n");  
		sql.append("          from tm_company tmc\n");  
		sql.append("         where 1 = 1\n");  
		sql.append("           and tmc.status = ").append(Constant.STATUS_ENABLE).append("\n"); 
		sql.append("           and tmc.company_id not in\n");  
		sql.append("               (select tugd1.the_id\n");  
		sql.append("                  from tt_unit_group_dtl tugd1\n");  
		sql.append("                 where 1 = 1\n");  
		sql.append("                   and tugd1.group_id = ").append(headId).append(")\n");  
		sql.append("           and tmc.company_id in\n");  
		sql.append("               (select tmd.company_id\n");  
		sql.append("                  from tm_dealer tmd\n");  
		sql.append("                 where 1 = 1\n");  
		sql.append("                   and tmd.status = ").append(Constant.STATUS_ENABLE).append("\n"); 
		sql.append("                   and tmd.dealer_type in (").append(dlrType).append("))) temp\n");  
		sql.append("                   where 1 = 1\n");  
		
		if(!CommonUtils.isNullString(queryName))
			sql.append("                   and temp.the_name like '%").append(queryName).append("%'\n");

		return super.pageQuery(sql.toString(), null, super.getFunName(), pageSize, curPage) ;
	}
	
	//zhumingwei 2011-10-21
	public PageResult<Map<String, Object>> queryGroupDtlDlrCompayAll111(Map<String, String> map, int pageSize, int curPage) {
		String productId = map.get("productId") ;
		String dlrType = map.get("dlrType") ;
		String regionCode = map.get("regionCode") ;
		String queryName = map.get("queryName") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select *\n");
		sql.append("  from (select tpd.product_distribution_id,\n");  
		sql.append("               tpd.company_id,\n");  
		sql.append("               tpd.company_name,\n");  
		sql.append("               tpd.company_code,\n");  
		sql.append("               1 is_check\n");  
		sql.append("          from tt_product_distribution tpd\n");  
		sql.append("         where 1 = 1\n");  
		sql.append("           and tpd.product_id = ").append(productId).append("\n");  
		sql.append("        union all\n");  
		sql.append("        select 0000000000000000 product_distribution_id,\n");  
		sql.append("               tmc.company_id   company_id,\n");  
		sql.append("               tmc.company_name company_name,\n");  
		sql.append("               tmc.company_code company_code,\n");  
		sql.append("               0                is_check\n");  
		sql.append("          from tm_company tmc\n");  
		sql.append("         where 1 = 1\n");  
		sql.append("           and tmc.province_id in (").append(regionCode).append(")\n");  
		sql.append("           and tmc.status = ").append(Constant.STATUS_ENABLE).append("\n"); 
		sql.append("           and tmc.company_id not in\n");  
		sql.append("               (select tpd1.company_id\n");  
		sql.append("                  from tt_product_distribution tpd1\n");  
		sql.append("                 where 1 = 1\n");  
		sql.append("                   and tpd1.product_id = ").append(productId).append(")\n");  
		sql.append("           and tmc.company_id in\n");  
		sql.append("               (select tmd.company_id\n");  
		sql.append("                  from tm_dealer tmd\n");  
		sql.append("                 where 1 = 1\n");  
		sql.append("                   and tmd.status = ").append(Constant.STATUS_ENABLE).append("\n"); 
		sql.append("                   and tmd.dealer_type in (").append(dlrType).append("))) temp\n");  
		sql.append("                   where 1 = 1\n");  
		
		if(!CommonUtils.isNullString(queryName))
			sql.append("                   and temp.company_name like '%").append(queryName).append("%'\n");

		return super.pageQuery(sql.toString(), null, super.getFunName(), pageSize, curPage) ;
	}
	
	public PageResult<Map<String, Object>> queryGroupDtl(Map<String, String> map, int pageSize, int curPage) {
		String headId = map.get("headId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tugd.group_dtl_id, tugd.the_id, tugd.the_name, tugd.the_code\n");
		sql.append("  from tt_unit_group_dtl tugd\n");  
		sql.append(" where tugd.group_id =").append(headId).append("\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName(), pageSize, curPage) ;
	}
	
	public List<Map<String, Object>> getGroupDtl(Map<String, String> map) {
		String headId = map.get("headId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tugd.group_dtl_id, tugd.the_id, tugd.the_name, tugd.the_code\n");
		sql.append("  from tt_unit_group_dtl tugd\n");  
		sql.append(" where tugd.group_id =").append(headId).append("\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> queryGroupMap(Map<String, String> map) {
		String headId = map.get("headId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tug.group_id,\n");
		sql.append("       tug.group_name,\n");  
		sql.append("       tug.group_type,\n");  
		sql.append("       tug.group_area,\n");  
		sql.append("       tug.group_status,\n");  
		sql.append("       to_char(tug.create_date, 'yyyy-mm-dd') create_date\n");  
		sql.append("  from tt_unit_group tug\n");  
		sql.append("  where 1 = 1\n");  
		
		if(!CommonUtils.isNullString(headId))
			sql.append("  and tug.group_id = ").append(headId).append("\n");  
		
		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public PageResult<Map<String, Object>> queryDlrCompany(Map<String, String> map, int pageSize, int curPage) {
		String dlrType = map.get("dlrType") ;
		String queryName = map.get("queryName") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmc.company_id the_id, tmc.company_code the_code, tmc.company_name the_name\n");
		sql.append("  from tm_company tmc\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and tmc.status = ").append(Constant.STATUS_ENABLE).append("\n"); 
		
		if(!CommonUtils.isNullString(queryName)) 
			sql.append("   and tmc.company_name like '%").append(queryName).append("%'\n"); 
		
		sql.append("   and tmc.company_id in\n");  
		sql.append("       (select tmd.company_id\n");  
		sql.append("          from tm_dealer tmd\n");  
		sql.append("         where 1 = 1\n");  
		sql.append("           and tmd.status = ").append(Constant.STATUS_ENABLE).append("\n");  
		sql.append("           and tmd.dealer_type in (").append(dlrType).append("))\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName(), pageSize, curPage) ;
	}
	
	//--//
	public List<Map<String, Object>> getGroupByUser(Map<String, String> map) {
		String groupType = map.get("groupType") ;
		String groupArea = map.get("groupArea") ;
		String groupStatus = map.get("groupStatus") ;
		String userId = map.get("userId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tug.group_id,\n");
		sql.append("       tug.group_name,\n");  
		sql.append("       tug.group_type,\n");  
		sql.append("       tug.group_area,\n");  
		sql.append("       tug.group_status\n");  
		sql.append("  from tt_unit_group tug\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and tug.group_type = ").append(groupType).append("\n");  
		sql.append("   and tug.group_area = ").append(groupArea).append("\n");  
		sql.append("   and tug.group_status = ").append(groupStatus).append("\n");  
		sql.append("   and tug.create_by = ").append(userId).append("\n");

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	/**
	 * 经销商产品套餐分配-维护-根据套餐信息经销商名称和代码列表
	 * @author HXY
	 * @update 2013-01-29
	 * */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getAllDealerOfPackage(Map<String, String> map, int pageSize, int curPage) {
		String productId = map.get("productId") ;
		String dlrType = map.get("dlrType") ;
		//String regionCode = map.get("regionCode") ;
		String queryName = map.get("queryName") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select *\n");
		sql.append("  from (select tpd.product_distribution_id,\n");  
		sql.append("               tpd.company_id,\n");  
		sql.append("               tpd.company_name,\n");  
		sql.append("               tpd.company_code,\n");  
		sql.append("               1 is_check\n");  
		sql.append("          from tt_product_distribution tpd\n");  
		sql.append("         where 1 = 1\n");  
		sql.append("           and tpd.product_id = ").append(productId).append("\n");  
		sql.append("        union all\n");  
		sql.append("        select 0000000000000000 product_distribution_id,\n");  
		sql.append("               td.dealer_id   company_id,\n");  
		sql.append("               td.dealer_name company_name,\n");  
		sql.append("               td.dealer_code company_code,\n");  
		sql.append("               0                is_check\n");  
		sql.append("          from tm_dealer td\n");  
		sql.append("         where 1 = 1\n");  
		//sql.append("           and td.province_id in (").append(regionCode).append(")\n");  
		sql.append("           and td.status = ").append(Constant.STATUS_ENABLE).append("\n"); 
		sql.append("           and td.company_id not in\n");  
		sql.append("               (select tpd1.company_id\n");  
		sql.append("                  from tt_product_distribution tpd1\n");  
		sql.append("                 where 1 = 1\n");  
		sql.append("                   and tpd1.product_id = ").append(productId).append(")\n");  
		sql.append("           and td.company_id in\n");  
		sql.append("               (select tmd.company_id\n");  
		sql.append("                  from tm_dealer tmd\n");  
		sql.append("                 where 1 = 1\n");  
		sql.append("                   and tmd.status = ").append(Constant.STATUS_ENABLE).append("\n"); 
		sql.append("                   and tmd.dealer_type in (").append(dlrType).append("))) temp\n");  
		sql.append("                   where 1 = 1\n");  
		
		if(!CommonUtils.isNullString(queryName))
			sql.append("                   and temp.company_name like '%").append(queryName).append("%'\n");

		return dao.pageQuery(sql.toString(), null, super.getFunName(), pageSize, curPage) ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
