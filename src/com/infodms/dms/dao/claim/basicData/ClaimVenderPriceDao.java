package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class ClaimVenderPriceDao extends BaseDao {

	private ClaimVenderPriceDao(){}
	public static ClaimVenderPriceDao getInstance(){
		return new ClaimVenderPriceDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 查询供应商索赔数据
	 * @param con
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> getClaimList(String part_code,String spy_name,String part_oldCode,String vender_name,String vender_code,String part_name,
			String status ,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select p.Relaion_Id as id, de.part_code,  de.part_oldcode, de.part_cname as part_name, VD.Maker_Code VENDER_CODE, VD.Maker_Name VENDER_NAME,\n");
		sql.append("      P.CLAIM_PRICE,  u.name as spy_name,  to_char(p.claim_date, 'yyyy-mm-dd hh24:mi') as CLAIM_DATE,  p.state as STATUS,P.PART_XS,P.LABOUR_XS\n");
		sql.append("  from TT_PART_MAKER_RELATION  p\n");
		sql.append("  left join tc_user u on p.spy_by = u.user_id, tt_part_maker_define vd,\n");
		sql.append(" tt_part_define de\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and P.MAKER_ID = VD.Maker_Id \n");
		sql.append("   and p.part_id = de.part_id"); 

		if(StringUtil.notNull(part_code)){
			sql.append(" and de.part_code LIKE '%"+part_code.toUpperCase()+"%'\n");
		}
		if(StringUtil.notNull(spy_name)){
			sql.append(" and u.name like'%"+spy_name+"%'\n");
		}
		if(StringUtil.notNull(part_oldCode)){
			sql.append(" and de.part_oldcode LIKE '%"+part_oldCode.toUpperCase()+"%'\n");
		}
		if(StringUtil.notNull(vender_name)){
			sql.append(" and vd.maker_name like'%"+vender_name+"%'\n");
		}
		if(StringUtil.notNull(vender_code)){
			sql.append(" and vd.maker_code like'%"+vender_code+"%'\n");
		}
		if(StringUtil.notNull(part_name)){
			sql.append(" and de.part_cname like'%"+part_name+"%' \n");
		}
		if(StringUtil.notNull(status)){
			sql.append(" and p.state="+status+"\n");
		}
		
		return this.pageQuery( sql.toString(), null,null, pageSize, curPage);		
	}
	//旧件库存查询
	public PageResult<Map<String,Object>> getOldPartList(String part_code,String part_name,String local_war_house,String local_war_shel,String local_war_layer,int pageSize,int curPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select tm.part_id,tm.part_code,tm.part_name,tm.local_war_house,tm.local_war_shel,tm.local_war_layer\n");
		sql.append("  from tm_pt_part_base tm\n");
		sql.append(" where 1 = 1\n");
		

		if(StringUtil.notNull(part_code)){
			sql.append(" and tm.part_code LIKE '%"+part_code.toUpperCase()+"%'\n");
		}
		if(StringUtil.notNull(part_name)){
			sql.append(" and tm.part_name like'%"+part_name+"%' \n");
		}
		if(StringUtil.notNull(local_war_house)){
			sql.append(" and tm.local_war_house LIKE '%"+local_war_house.toUpperCase()+"%'\n");
		}
		if(StringUtil.notNull(local_war_shel)){
			sql.append(" and tm.local_war_shel like'%"+local_war_shel+"%' \n");
		}
		if(StringUtil.notNull(local_war_layer)){
			sql.append(" and tm.local_war_layer like'%"+local_war_layer+"%' \n");
		}
		return this.pageQuery( sql.toString(), null,null, pageSize, curPage);
		
	}
	public List<Map<String,Object>> getReserList( Long id ){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select tm.part_id,tm.part_code,tm.part_name,tm.local_war_house,tm.local_war_shel,tm.local_war_layer\n");
		sql.append("  from tm_pt_part_base tm\n");
		sql.append(" where 1 = 1 and part_id = ?\n");
		params.add(id);
		List<Map<String,Object>> list =pageQuery(sql.toString(), params, getFunName());
		return  list;	
	}
	public PageResult<Map<String,Object>> getOLdReser(String codeOld,String nameOld,String nameType,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select  tt.code_old,tt.name_old \n");
		sql.append("  from tt_as_create_old tt \n");
		sql.append(" where 1=1 and tt.name_type="+nameType);
		if(StringUtil.notNull(codeOld)){
			sql.append(" and tt.code_old like '%"+codeOld+"%' \n");
		}
		if(StringUtil.notNull(nameOld)){
			sql.append(" and tt.name_old like '%"+nameOld+"%'");
		}
		return this.pageQuery( sql.toString(), null,null, pageSize, curPage);		
	}
	/**
	 * 
	 * @param dealerCode
	 * @param dealerName
	 * @param isScan
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
//经销商信息查询
	public PageResult<Map<String,Object>> getDealerList(String dealerCode,String dealerName,String isScan,
			int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("select  d.dealer_id,d.dealer_code,d.dealer_name,d.is_scan,\n");
		sql.append(" decode(d.is_scan,'1','是','否') isScan");
		sql.append(" from tm_dealer d where d.status="+Constant.STATUS_ENABLE); 
		sql.append(" and d.dealer_type="+Constant.DEALER_TYPE_DWR+"\n");
		if(StringUtil.notNull(dealerCode)){
			sql.append(" and d.dealer_code like '%"+dealerCode+"%'\n");
		}
		if(StringUtil.notNull(dealerName)){
			sql.append(" and d.dealer_name like '%"+dealerName+"%'\n");
		}
		if(StringUtil.notNull(isScan)){
			sql.append(" and d.is_scan='"+isScan+"'\n");
		}
		sql.append(" order by d.dealer_id ");
		return this.pageQuery( sql.toString(), null,null, pageSize, curPage);		
	}
	public List getClaimList( Long id ){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT P.Relaion_Id AS ID,\n");
		sql.append("       DE.PART_CODE,\n");
		 sql.append("      DE.PART_OLDCODE,\n");
		 sql.append("      DE.PART_CNAME AS PART_NAME,\n");
		  sql.append("     VD.Maker_Code VENDER_CODE,\n");
		  sql.append("     VD.Maker_Name VENDER_NAME,\n");
		  sql.append("     P.CLAIM_PRICE ,\n");
		  sql.append("     U.NAME AS SPY_NAME,\n");
		  sql.append("     TO_CHAR(P.CLAIM_DATE, 'yyyy-mm-dd hh24:mi') AS CLAIM_DATE,\n");
		   sql.append("    P.STATE AS STATUS,p.part_xs,p.labour_xs\n");
		 sql.append(" FROM tt_part_maker_relation P\n");
		 sql.append(" LEFT JOIN TC_USER U ON P.SPY_BY = U.USER_ID, tt_part_maker_define VD,\n");
		 sql.append("TT_PART_DEFINE DE\n");
		 sql.append("WHERE 1 = 1\n");
		 sql.append("  AND P.Maker_Id = VD.Maker_Id\n");
		 sql.append("  AND P.PART_ID = DE.PART_ID\n");
		  sql.append(" AND P.Relaion_Id = ? \n");
		params.add(id);
		List<Map<String, Object>> list = pageQuery(sql.toString(), params,getFunName());
		return  list;	
	}
	
	public PageResult<Map<String,Object>> getClaimUser(String name,String code,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append(" select  u.user_id,u.acnt,u.name \n");
		sql.append("  from tc_user u \n");
		sql.append(" where 1=1 and u.user_type="+Constant.SYS_USER_SGM+" AND u.company_id=2010010100070674 \n");
		if(StringUtil.notNull(name)){
			sql.append(" and u.name like '%"+name+"%' \n");
		}
		if(StringUtil.notNull(code)){
			sql.append(" and  u.acnt='"+code+"'");
		}
		return this.pageQuery( sql.toString(), null,null, pageSize, curPage);		
	}
}
