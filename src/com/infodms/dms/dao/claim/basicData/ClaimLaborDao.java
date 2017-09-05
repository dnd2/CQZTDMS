/**   
* @Title: ClaimLaborDao.java 
* @Package com.infodms.dms.dao.claim.basicData 
* @Description: TODO(索赔工时维护DAO) 
* @author Administrator   
* @date 2010-6-1 下午03:02:44 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.yxdms.utils.BaseUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimLaborDao 
 * @Description: TODO(索赔工时维护DAO) 
 * @author Administrator 
 * @date 2010-6-1 下午03:02:44 
 *  
 */
@SuppressWarnings("rawtypes")
public class ClaimLaborDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimLaborDao.class);
	private static final ClaimLaborDao dao = new ClaimLaborDao ();
	public static final ClaimLaborDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: getClaimWrModelGroup 
	* @Description: TODO(获取所有的索赔工时车型组列表) 
	* @param @param type    车型组类型
	* @param @return   
	* @return List<TtAsWrModelGroupPO>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getClaimWrModelGroup(Integer type){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select tawmg.wrgroup_id,tawmg.wrgroup_name,tawmg.wrgroup_type from TT_AS_WR_MODEL_GROUP tawmg ");
		sb.append("	where tawmg.wrgroup_type = ?  ");
		params.add(type);
		sb.append("");
		List<Map<String, Object>> list = pageQuery(sb.toString(), params,getFunName());
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> part_series(int pageSize, int curPage,String PART_OLDCODE, String PART_CNAME, String PART_CODE, String PART_TYPE,String GROUP_CODE,String GROUP_NAME,String PART_IS_CHANGHE,String Byld) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select c.PART_OLDCODE,\n" );
		sql.append("       c.PART_CNAME,\n" );
		sql.append("       c.PART_CODE,\n" );
		sql.append("       c.PART_TYPE,\n" );
		sql.append("       c.PART_IS_CHANGHE,\n" );
		sql.append("       b.GROUP_CODE,\n" );
		sql.append("       b.GROUP_NAME,\n" );
		sql.append("       a.ID,  ");
		sql.append("       (SELECT d.NAME from TC_USER d where d.USER_ID = a.CREATE_BY ) CREATE_BY,\n" );
		sql.append("       to_char(a.CREATE_DATE,'yyyy-mm-dd') CREATE_DATE ,\n" );
		sql.append("        (SELECT d.NAME from TC_USER d where d.USER_ID = a.UPDATE_BY ) UPDATE_BY,\n" );
		sql.append("       to_char(a.UPDATE_DATE,'yyyy-mm-dd') UPDATE_DATE\n" );
		sql.append("  from tt_as_relation_part_series a,\n" );
		sql.append("       tm_vhcl_material_group     b,\n" );
		sql.append("       TT_PART_DEFINE             c\n" );
		sql.append(" where a.PART_ID = c.PART_ID\n" );
		sql.append("   and a.MODEL_ID = b.GROUP_ID");
		sql.append(" and  c.PART_IS_CHANGHE ='"+Byld+"'");
		if(Utility.testString(PART_OLDCODE))
			sql.append(" and  c.PART_OLDCODE like '%"+PART_OLDCODE+"%' ");
		if(Utility.testString(PART_IS_CHANGHE))
			sql.append(" and  c.PART_IS_CHANGHE = '"+PART_IS_CHANGHE+"' ");
		if(Utility.testString(PART_CNAME))
			sql.append(" and  c.PART_CNAME like '%"+PART_CNAME+"%' ");
		if(Utility.testString(PART_CODE))	
			sql.append(" and  c.PART_CODE like '%"+PART_CODE+"%' ");
	    if(Utility.testString(PART_TYPE))
	    	sql.append(" and  c.PART_TYPE = '"+PART_TYPE+"' ");
	    if(Utility.testString(GROUP_CODE))	
	    	sql.append(" and  b.GROUP_CODE like '%"+GROUP_CODE+"%' ");
	    if(Utility.testString(GROUP_NAME))
	    	sql.append(" and  b.GROUP_NAME like '%"+GROUP_NAME+"%' ");
	    sql.append("  order by c.PART_OLDCODE ");
		result =  super.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;
	} 
	
	/**
	 * 
	* @Title: claimLaborQuery 
	* @Description: 索赔工时查询
	* @param  pageSize           ：每页显示条数
	* @param  curPage            ：当前页数
	* @param  whereSql           ：查询条件
	* @param  params             ：查询条件对应的参数
	* @param @throws Exception    设定文件 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> claimLaborQuery(int pageSize, int curPage,Long oemCompanyId, String whereSql,List<Object> params,RequestWrapper request) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select taww.is_del,g.wrgroup_code,taww.id,taww.wrgroup_id,taww.labour_code,");
		sb.append(" taww.cn_des,trim(to_char(taww.labour_quotiety,'999999.00')) as labour_quotiety,");
		sb.append(" trim(to_char(taww.labour_hour,'999990.00')) as labour_hour,a.labour_code labour_code_big ,a.cn_des cn_des_big \n");
		sb.append(" from TT_AS_WR_WRLABINFO taww left join  TT_AS_WR_WRLABINFO a on  taww.pater_id = a.id,tt_as_wr_model_group g where taww.wrgroup_id = g.wrgroup_id   \n   ");
		DaoFactory.getsql(sb, "taww.is_del", DaoFactory.getParam(request, "is_del"), 1);
		sb.append(" and taww.tree_code = '3' ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by g.wrgroup_code desc,taww.labour_code ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	
	//zhumingwei 2011-8-11
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> claimLaborQuery11(int pageSize, int curPage,Long oemCompanyId, String whereSql) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select g.wrgroup_code,taww.id,taww.wrgroup_id,taww.labour_code,");
		sb.append(" taww.cn_des,trim(to_char(taww.labour_quotiety,'999999.00')) as labour_quotiety,");
		sb.append(" trim(to_char(taww.labour_hour,'999990.00')) as labour_hour,(select decode(wa.approval_level,'200','一般授权','400','大区授权','600','主管授权','200,400','一般授权，大区授权','200,600','一般授权，主管授权','400,600','大区授权，主管授权') from tt_as_wr_authmonitorlab wa where taww.labour_code = wa.labour_operation_no and taww.wrgroup_id = wa.model_group and wa.is_del=0) as approval_level,taww.remark");
		sb.append(" from TT_AS_WR_WRLABINFO taww, tt_as_wr_model_group g");
		sb.append(" where taww.wrgroup_id = g.wrgroup_id and taww.is_del = 0  and taww.tree_code = '3'");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by taww.id desc ");
		System.out.println(sb.toString());
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	//zhumingwei 2011-9-28
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> partThreeBagsQuery11(int pageSize, int curPage,Long oemCompanyId, String whereSql) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select b.id,a.part_code,");
		sb.append(" a.part_name,");
		sb.append(" a.claim_price price,");
		sb.append(" c.claim_month,");
		sb.append(" c.claim_melieage,");
		sb.append(" b.rule_code,");
		sb.append(" b.rule_name");
		//sb.append(" decode(d.approval_level,'200','一般授权','400','大区授权','600','主管授权','200,400','一般授权，大区授权','200,600','一般授权，主管授权','400,600','大区授权，主管授权','200,400,600','一般授权,大区授权,主管授权') as approval_level");
		//sb.append(" ,d.model_group");
		sb.append(" from tm_pt_part_base a, tt_as_wr_rule b, tt_as_wr_rule_list c,tt_as_wr_authmonitorpart d");
		sb.append(" WHERE a.part_code = c.part_code");
		sb.append(" and b.id = c.rule_id");
		sb.append(" and a.part_code=d.part_code(+) and d.is_del(+)=0 and a.is_del=0");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		System.out.println(sb.toString());
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	//zhumingwei 2011-9-28
	@SuppressWarnings("unchecked")
	public  PageResult<Map<String, Object>>  partThreeBagsDetail(String ruleId, int curPage,int pageSize) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("  select b.rule_code,e.game_code,e.game_name,f.model_code,f.model_name from tt_as_wr_rule b,tt_as_wr_game e,tt_as_wr_gamemodel f\n");
		sql.append("  where b.id=e.rule_id and e.id=f.game_id\n");
		sql.append("  and b.id='"+ruleId+"'\n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap getClaimLaborById(Long id){
		HashMap hm = null;
//		List<Object> params = new LinkedList<Object>();
//		StringBuilder sb = new StringBuilder();
//		sb.append(" select taww.id,taww.wrgroup_id,taww.labour_code,taww.cn_des, ");
//		sb.append(" trim(to_char(taww.labour_quotiety,'999999.00')) as labour_quotiety,");
//		sb.append(" trim(to_char(taww.labour_hour,'999999.00')) as labour_hour,tawmg.wrgroup_code from TT_AS_WR_WRLABINFO taww ");
//		sb.append(" left outer join TT_AS_WR_MODEL_GROUP tawmg on taww.wrgroup_id = tawmg.wrgroup_id ");
//		sb.append("	where taww.id = ?  ");
//		params.add(id);
		
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,\n" );
		sql.append("       t.pater_id,\n" );
		sql.append("       t.wrgroup_id,\n" );
		sql.append("       t.labour_code,\n" );
		sql.append("       t.cn_des,\n" );
		sql.append("       t.labour_quotiety,\n" );
		sql.append("       t.labour_hour,\n" );
		sql.append("       t.wrgroup_code,\n" );
		sql.append("       t2.labour_code as p_labour_code,\n" );
		sql.append("       t2.cn_des as p_cn_des,\n" );
		sql.append("       t2.remark as p_remark,t.con_remark\n" );
		sql.append("  from (select taww.id,\n" );
		sql.append("               taww.pater_id,\n" );
		sql.append("               taww.wrgroup_id,\n" );
		sql.append("               taww.labour_code,\n" );
		sql.append("               taww.cn_des,\n" );
		sql.append("               trim(to_char(taww.labour_quotiety, '999999.00')) as labour_quotiety,\n" );
		sql.append("               trim(to_char(taww.labour_hour, '999999.00')) as labour_hour,\n" );
		sql.append("               tawmg.wrgroup_code,taww.remark as con_remark\n" );
		sql.append("          from TT_AS_WR_WRLABINFO taww\n" );
		sql.append("          left outer join TT_AS_WR_MODEL_GROUP tawmg on taww.wrgroup_id =\n" );
		sql.append("                                                        tawmg.wrgroup_id) t,\n" );
		sql.append("       TT_AS_WR_WRLABINFO t2\n" );
		sql.append(" where t.pater_id = t2.id\n" );
		sql.append("   and t.id = "+id+" ");

		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list != null && list.size() > 0){
			hm = (HashMap)list.get(0);
		}
		return hm;
	}
	/**
	 * 
	* @Title: getAdddClaimLaborById 
	* @Description: TODO(根据ID查询附加工时信息) 
	* @param @param id
	* @param @return   
	* @return HashMap  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public HashMap getAdddClaimLaborById(Long id){
		HashMap hm = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select ts.id,\n" );
		sql.append("       ts.pater_id,\n" );
		sql.append("       ts.wrgroup_id,\n" );
		sql.append("       ts.labour_code,\n" );
		sql.append("       ts.cn_des,\n" );
		sql.append("       trim(to_char(ts.labour_quotiety, '999999.00')) as labour_quotiety,\n" );
		sql.append("       trim(to_char(ts.labour_hour, '999999.00')) as labour_hour\n" );
		sql.append("        from TT_AS_WR_WRLABINFO ts\n" );
		sql.append("       where ts.id = "+id+" ");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list != null && list.size() > 0){
			hm = (HashMap)list.get(0);
		}
		return hm;
	}	
	
	
	
	/**
	 * 
	* @Title: getBussCodeById 
	* @Description: TODO(根据索赔工时id获取故障代码列表) 
	* @param @param id
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getBussCodeById(Long id){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select tbcc.code,tbcc.code_name,tbcc.business_code_id,tawtm.map_id ");
		sb.append(" from TT_AS_WR_TROBLE_MAP tawtm ");
		sb.append(" left outer join TM_BUSINESS_CHNG_CODE tbcc  ");
		sb.append(" on tawtm.trouble_id = tbcc.business_code_id ");
		sb.append("	where tawtm.is_del = 0 and tawtm.labor_id = ?  ");
		params.add(id);
		List<Map<String, Object>> list = pageQuery(sb.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * 
	* @Title: getAddLabourById 
	* @Description: TODO(根据索赔工时id获取附加工时列表) 
	* @param @param id
	* @param @return   
	* @return List<TtAsWrWrlabinfoPO>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getAddLabourById(Long id){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
//		sb.append(" select * from TT_AS_WR_WRLABINFO  taww ");
//		sb.append(" where exists ");
//		sb.append(" (select 'X' from TT_AS_WR_ADDITIONALITEM  tawa ");
//		sb.append(" where taww.id=tawa.add_id ");
//		sb.append(" and taww.is_del = 0 ");
//		sb.append(" and tawa.is_del = 0 ");
//		sb.append("	and tawa.w_id = ? )");
		sb.append("select tawa.id,taww.wrgroup_id,taww.labour_code,taww.cn_des,taww.id as add_id,");
		sb.append(" trim(to_char(taww.labour_quotiety,'999999.00')) as labour_quotiety, ");
		sb.append(" trim(to_char(taww.labour_hour,'999999.00')) as labour_hour ");
		sb.append(" from TT_AS_WR_ADDITIONALITEM tawa ");
		sb.append(" left outer join TT_AS_WR_WRLABINFO  taww on tawa.add_id = taww.id ");
		sb.append(" where tawa.is_del = 0 and taww.is_del = 0 and tawa.w_id = ? ");
		sb.append("");
		params.add(id);
		List<Map<String, Object>> list = pageQuery(sb.toString(), params, getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getBusinessCodeByType 
	* @Description: TODO(取得所有故障代码的列表) 
	* @param @param type ：10441004
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getBusinessCodeByType(Integer type,String laborId,Long companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("select tbcc.business_code_id,tbcc.code,tbcc.code_name ");
		sb.append(" from TM_BUSINESS_CHNG_CODE tbcc  where tbcc.is_del=0   and tbcc.type_code = ? ");
		params.add(type);
		sb.append(" and  not exists (select 'X' from TT_AS_WR_TROBLE_MAP tawtm ");
		sb.append(" where tbcc.business_code_id=tawtm.trouble_id and tawtm.labor_id = ? ");
		sb.append(" ) order by tbcc.code ");
		params.add(laborId);
		List<Map<String, Object>> list = pageQuery(sb.toString(), params, getFunName());
		return list;
	}
	/**
	 * 
	* @Title: laborUpdate 
	* @Description: TODO(索赔工时/附加工时修改) 
	* @param @param selpo
	* @param @param updatepo   
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void laborUpdate(PO selpo,PO updatepo){
		dao.update(selpo, updatepo);
	}
	
	@SuppressWarnings("unchecked")
	public void claimlaborUpdate(TtAsWrWrlabinfoPO selpo,TtAsWrWrlabinfoPO updatepo){
		dao.update(selpo, updatepo);
	}
	
	@SuppressWarnings("unchecked")
	public void deleteClaimlabor(Long id,TtAsWrWrlabinfoPO selpo,TtAsWrWrlabinfoPO delpo){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = null;
		//删除该索赔工时对应附加工时信息
		sb = new StringBuilder();
		sb = new StringBuilder(" delete from  TT_AS_WR_ADDITIONALITEM where w_id = ? ");
		params.add(id);
		dao.delete(sb.toString(), params);
		params.clear();
		//删除该索赔工时对应故障
		sb = new StringBuilder(" delete from  TT_AS_WR_TROBLE_MAP where trouble_id = ? ");
		params.add(id);
		dao.delete(sb.toString(), params);
		//删除该索赔工时
		dao.update(selpo, delpo);

		
	}

	/* (非 Javadoc) 
	 * <p>Title: wrapperPO</p> 
	 * <p>Description: </p> 
	 * @param rs
	 * @param idx
	 * @return 
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int) 
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成方法存根
		return null;
	}
	
	//zhumingwei 2011-07-06
	public void createPriceDetail(String dealerId,long labourId,double price,String policytrueTime,String policyfalseTime,long userId,String moneyType){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO Tt_as_change_PRICE_detail(ID,LABOUR_ID,DEALER_ID,GROUP_ID,old_LABOUR_PRICE,new_LABOUR_PRICE,change_LABOUR_PRICE,\n" );
		sql.append("POLICY_START_DATE,POLICY_END_DATE,status,create_by,create_date)\n" );
		sql.append("SELECT F_GETID() ID,"+labourId+" LABOUR_ID,d.dealer_id dealer_id,mg.wrgroup_id GROUP_ID,\n");
		sql.append("0 old_LABOUR_PRICE,\n" );
		
		if(Constant.MONEY_TYPE_01.toString().equals(moneyType)){
			sql.append("0+"+price+" new_LABOUR_PRICE,\n" );
			sql.append("0+"+price+"-0 change_LABOUR_PRICE,\n" );
		}
		if(Constant.MONEY_TYPE_02.toString().equals(moneyType)){
			sql.append("0+(labour_price*"+price+") new_LABOUR_PRICE,\n" );
			sql.append("0+(labour_price*"+price+")-0 change_LABOUR_PRICE,\n" );
		}
		
		
		sql.append("to_date('"+policytrueTime+"','yyyy-MM-dd') POLICY_START_DATE,to_date('"+policyfalseTime+"','yyyy-MM-dd') POLICY_END_DATE,0 status,\n" );
		sql.append("'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE\n" );
		sql.append("FROM Tt_As_Wr_Model_Group mg,tm_dealer d where mg.wrgroup_type=10451001\n" );
		sql.append("AND d.dealer_id='"+dealerId+"'\n" );
		this.update(sql.toString(), null);
	}
	//zhumingwei 2011-07-12
	public void createPriceDetail1(String regionCode,long labourId,double price,String policytrueTime,String policyfalseTime,long userId,String moneyType){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO Tt_as_change_PRICE_detail(ID,LABOUR_ID,DEALER_ID,GROUP_ID,old_LABOUR_PRICE,new_LABOUR_PRICE,change_LABOUR_PRICE,\n" );
		sql.append("POLICY_START_DATE,POLICY_END_DATE,status,create_by,create_date)\n" );
		sql.append("SELECT F_GETID() ID,"+labourId+" LABOUR_ID,d.dealer_id dealer_id,mg.wrgroup_id GROUP_ID,\n");
		
		sql.append("0 old_LABOUR_PRICE,\n" );
		
		if(Constant.MONEY_TYPE_01.toString().equals(moneyType)){
			sql.append("0+"+price+" new_LABOUR_PRICE,\n" );
			sql.append("0+"+price+"-0 change_LABOUR_PRICE,\n" );
		}
		if(Constant.MONEY_TYPE_02.toString().equals(moneyType)){
			sql.append("0+(labour_price*"+price+") new_LABOUR_PRICE,\n" );
			sql.append("0+(labour_price*"+price+")-0 change_LABOUR_PRICE,\n" );
		}
		
		sql.append("to_date('"+policytrueTime+"','yyyy-MM-dd') POLICY_START_DATE,to_date('"+policyfalseTime+"','yyyy-MM-dd') POLICY_END_DATE,0 status,\n" );
		sql.append("'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE\n" );
		sql.append("FROM Tt_As_Wr_Model_Group mg,tm_dealer d where mg.wrgroup_type=10451001\n" );
		sql.append("AND d.dealer_id in (select dealer_id from tm_dealer t where t.province_id='"+regionCode+"')\n" );
		
		this.update(sql.toString(), null);
	}
	
	//zhumingwei 2011-07-13
	public void createPriceDetail2(String wrgroup_id,long labourId,double price,String policytrueTime,String policyfalseTime,long userId,String moneyType){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO Tt_as_change_PRICE_detail(ID,LABOUR_ID,DEALER_ID,GROUP_ID,old_LABOUR_PRICE,new_LABOUR_PRICE,change_LABOUR_PRICE,\n" );
		sql.append("POLICY_START_DATE,POLICY_END_DATE,status,create_by,create_date)\n" );
		sql.append("SELECT F_GETID() ID,"+labourId+" LABOUR_ID,d.dealer_id dealer_id,mg.wrgroup_id GROUP_ID,\n");

		sql.append("0 old_LABOUR_PRICE,\n" );
		
		if(Constant.MONEY_TYPE_01.toString().equals(moneyType)){
			sql.append("0+"+price+" new_LABOUR_PRICE,\n" );
			sql.append("0+"+price+"-0 change_LABOUR_PRICE,\n" );
		}
		if(Constant.MONEY_TYPE_02.toString().equals(moneyType)){
			sql.append("0+(labour_price*"+price+") new_LABOUR_PRICE,\n" );
			sql.append("0+(labour_price*"+price+")-0 change_LABOUR_PRICE,\n" );
		}
		
		sql.append("to_date('"+policytrueTime+"','yyyy-MM-dd') POLICY_START_DATE,to_date('"+policyfalseTime+"','yyyy-MM-dd') POLICY_END_DATE,0 status,\n" );
		sql.append("'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE\n" );
		sql.append("FROM Tt_As_Wr_Model_Group mg,tm_dealer d where mg.wrgroup_type=10451001\n" );
		sql.append("AND d.dealer_id in (select dealer_id from Tt_As_Wr_Labour_Price aa where aa.mode_type in (select g.wrgroup_code from Tt_As_Wr_Model_Group g where g.wrgroup_id='"+wrgroup_id+"'))\n" );
		
		this.update(sql.toString(), null);
	}
	
	//zhumingwei 2011-7-7
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> laborPriceQuery(int pageSize, int curPage,String changeType, String policyName,String policyNo,String changeStatus) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select c.*,u.name,to_char(c.ends_date,'yyyy-mm-dd') end_date,to_char(c.Create_Date,'yyyy-mm-dd') CREATE_DATES,to_char(c.Generate_Date,'yyyy-mm-dd') Generate_DateS");
		sb.append(" from Tt_as_change_PRICE c left join tc_user u  on u.user_id = c.ends_by where 1=1");
		
		//条件
		if (Utility.testString(policyNo)) {
			sb.append(" and c.POLICY_no like '%"+policyNo+"%' ");
		}
		if (Utility.testString(changeType)) {
			sb.append(" and c.CHANG_TYPE = '"+changeType+"' ");
		}
		if (Utility.testString(changeStatus)) {
			sb.append(" and c.chang_status = '"+changeStatus+"' ");
		}
		if (Utility.testString(policyName)) {
			sb.append(" and c.POLICY_name like '%"+policyName+"%' ");
		}
		
		sb.append(" order by c.id desc ");
		PageResult<Map<String, Object>> result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	//zhumingwei 2011-7-26
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> changeDetail(long labour) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.dealer_id,B.dealer_code,B.dealer_name\n");
		sql.append("  FROM Tr_change_PRICE_detail A, tm_dealer B\n");
		sql.append(" WHERE A.change_id = B.dealer_ID\n");
		sql.append("   AND A.labour_id = "+labour+"\n");

		List<Map<String,Object>> listNews = pageQuery(sql.toString(),null,getFunName());
		return listNews;
	}
	public List<Map<String,Object>> changeDetail1(long labour) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.region_code,B.region_name\n");
		sql.append("  FROM Tr_change_PRICE_detail A, tm_region B\n");
		sql.append(" WHERE A.change_id = B.region_code\n");
		sql.append("   AND A.labour_id = "+labour+"\n");

		List<Map<String,Object>> listNews = pageQuery(sql.toString(),null,getFunName());
		return listNews;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> changeDetail2(long labour) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.wrgroup_id,B.wrgroup_name\n");
		sql.append("  FROM Tr_change_PRICE_detail A, Tt_As_Wr_Model_Group B\n");
		sql.append(" WHERE A.change_id = B.wrgroup_id\n");
		sql.append("   AND A.labour_id = "+labour+"\n");

		List<Map<String,Object>> listNews = pageQuery(sql.toString(),null,getFunName());
		return listNews;
	}
	
	public void batchUpdateDao(String id,String code,String name,String quantity){
		StringBuffer sql= new StringBuffer();
		sql.append("update Tt_As_Wr_Wrlabinfo  w set w.labour_hour="+quantity+"\n" );
		sql.append("where w.wrgroup_id in ("+id+")\n" );
		if (Utility.testString(code)) {
		sql.append("and w.labour_code like '%"+code+"%'\n" );
		}
		if (Utility.testString(name)) {
		sql.append("and w.cn_des like '%"+name+"%'");
		}
		this.update(sql.toString(), null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> VerificationName(String policyName) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM Tt_as_change_PRICE A\n");
		sql.append(" WHERE 1=1 and  A.POLICY_name = '"+policyName+"'\n");

		List<Map<String,Object>> listNews = pageQuery(sql.toString(),null,getFunName());
		return listNews;
	}
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> claimLaborQueryData(RequestWrapper request, Integer pageSize, Integer currPage) {
		//增加工时大类代码、名称查询条件
		StringBuffer sb = new StringBuffer();
		sb.append(" select g.wrgroup_code,taww.id,taww.wrgroup_id,taww.labour_code,");
		sb.append(" taww.cn_des,trim(to_char(taww.labour_quotiety,'999999.00')) as labour_quotiety,");
		sb.append(" trim(to_char(taww.labour_hour,'999990.00')) as labour_hour,a.labour_code labour_code_big ,a.cn_des cn_des_big ");
		sb.append(" from tt_as_wr_wrlabinfo taww left join  tt_as_wr_wrlabinfo a on  taww.pater_id = a.id,tt_as_wr_model_group g where taww.wrgroup_id = g.wrgroup_id and taww.is_del = 0     ");
		sb.append(" and taww.tree_code = '3' ");
		DaoFactory.getsql(sb, "taww.wrgroup_id", DaoFactory.getParam(request, "WRGROUP_ID"), 6);
		DaoFactory.getsql(sb, "taww.labour_code", DaoFactory.getParam(request, "LABOUR_CODE"), 2);
		DaoFactory.getsql(sb, "taww.cn_des", DaoFactory.getParam(request, "CN_DES"), 2);
		String labourCodebig=DaoFactory.getParam(request, "LABOUR_CODE_BIG");
		String cnDesbig=DaoFactory.getParam(request, "CN_DES_BIG");
		if(Utility.testString(labourCodebig) || Utility.testString(cnDesbig)){
			sb.append(" and taww.pater_id in (select t2.id\n" );
			sb.append(" from tt_as_wr_wrlabinfo t2\n" );
			sb.append(" where 1=1\n" );
			if(Utility.testString(labourCodebig)){
				DaoFactory.getsql(sb, "t2.labour_code", DaoFactory.getParam(request, "LABOUR_CODE_BIG"), 2);
			}
			if(Utility.testString(cnDesbig)){
				DaoFactory.getsql(sb, "t2.labour_code", DaoFactory.getParam(request, "CN_DES_BIG"), 2);
			}
			sb.append(" and t2.tree_code in (1, 2))\n");
		}
		sb.append(" order by g.wrgroup_code desc,taww.labour_code ");
		return pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
	}
	@SuppressWarnings("unchecked")
	public void claimLaborQueryToExecl(ActionContext act,PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[7];
			head[0]="车型组";
			head[1]="工时代码";
			head[2]="工时名称";
			head[3]="工时大类代码";
			head[4]="工时大类名称";
			head[5]="工时系数";
			head[6]="索赔工时";
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[7];
					detail[0]=BaseUtils.checkNull(map.get("WRGROUP_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("LABOUR_CODE"));
					detail[2]=BaseUtils.checkNull(map.get("CN_DES"));
					detail[3]=BaseUtils.checkNull(map.get("LABOUR_CODE_BIG"));
					detail[4]=BaseUtils.checkNull(map.get("CN_DES_BIG"));
					detail[5]=BaseUtils.checkNull(map.get("LABOUR_QUOTIETY"));
					detail[6]=BaseUtils.checkNull(map.get("LABOUR_HOUR"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出工时查询报表"+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 修改工时失效
	 * @param request
	 * @param loginUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int updateclaimLabor(RequestWrapper request, AclUserBean loginUser) {
		
		TtAsWrWrlabinfoPO po1 = new TtAsWrWrlabinfoPO();
		TtAsWrWrlabinfoPO  po = new TtAsWrWrlabinfoPO();
		po1.setId(Long.valueOf(DaoFactory.getParam(request, "id")));
		po.setIsDel(Integer.parseInt(DaoFactory.getParam(request, "uFlag")));
		int res =this.update(po1, po);
		return res;
	}
	/**
	 * 批量失效
	 * @param request
	 * @param loginUser
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public int updateclaimLabors(RequestWrapper request, AclUserBean loginUser) {
		int res=0;
		String ids = DaoFactory.getParam(request, "ids");
		String uflag = DaoFactory.getParam(request, "uflag");
		if (BaseUtils.notNull(ids)) {
		   String[] arr = ids.split(",");
		   if (null!=arr && arr.length>0) {
		        TtAsWrWrlabinfoPO po1=null;
		        TtAsWrWrlabinfoPO po =null;
		      for (int i = 0; i < arr.length; i++) {
			      po1 = new TtAsWrWrlabinfoPO();
			      po = new TtAsWrWrlabinfoPO();
			      po1.setId(Long.valueOf(arr[i]));
			      po.setIsDel(Integer.parseInt(uflag));
			      res =this.update(po1, po);
			      if (res==0) {
				    break; 
			      }
		       }
		  }
		}
		return res;
	}
	
}
