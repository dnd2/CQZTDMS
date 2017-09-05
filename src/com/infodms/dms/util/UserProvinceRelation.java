package com.infodms.dms.util;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;

/**
 * 人员省份关系
 * @author nova zuo
 *
 */
public class UserProvinceRelation {
	private Logger logger = Logger.getLogger(UserProvinceRelation.class) ;
	
	// 根据当前用户的ID 和 主表涉及的经销商ID的一个表的别名
	// 返回一条SQL 作为主SQL语句的查询条件
	public static String getDealerIds(Long userId,String name){
		StringBuffer sql = new StringBuffer() ;
		sql.append("  and exists (select 1\n");
		sql.append("         from tc_user                 u11,\n");  
		sql.append("              tc_user_region_relation r11,\n");  
		sql.append("              tm_dealer               d11,\n");  
		sql.append("              tm_region               tr11\n");  
		sql.append("        where u11.user_id = r11.user_id\n");  
		sql.append("          and r11.region_code = tr11.region_code\n");  
		sql.append("          and d11.province_id = tr11.region_code\n");  
		sql.append("          and u11.user_type = ").append(Constant.SYS_USER_SGM).append("\n");    
		sql.append("          and u11.user_id = ").append(userId).append("\n");
		sql.append("          and d11.dealer_id = ").append(name).append(".dealer_id)\n");
		return sql.toString() ;
	}
}
