package com.infodms.dms.dao.claim.basicData;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AreaProvinceBean;
import com.infodms.dms.bean.PriceAdjustBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsWrLabourPricePO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrPriceAdjustPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class ClaimBasicLaborPriceDao extends BaseDao {
	private ClaimBasicLaborPriceDao(){}
	public static ClaimBasicLaborPriceDao getInstance(){
		return new ClaimBasicLaborPriceDao();
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/*
	 * 查询工时大类
	 */
	@SuppressWarnings("unchecked")
	public PageResult<TtAsWrModelGroupPO> getLaborList01(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select distinct * from tt_as_wr_model_group p\n");
		sql.append("where p.wrgroup_type=").append(Constant.WR_MODEL_GROUP_TYPE_01).append("\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append("order by p.wrgroup_code\n");
		return this.pageQuery(TtAsWrModelGroupPO.class, sql.toString(), null, pageSize, curPage);		
	}
	
	/*
	 * 查询工时大类
	 */
	@SuppressWarnings("unchecked")
	public PageResult<TmVhclMaterialGroupPO> getLaborList(String con,int pageSize,int curPage){
		 
		 StringBuffer sql= new StringBuffer();
				sql.append("SELECT * from TM_VHCL_MATERIAL_GROUP t where t.GROUP_LEVEL = 2");
				if(StringUtil.notNull(con))
					sql.append(con);
				sql.append(" order by t.GROUP_CODE\n");
		return this.pageQuery(TmVhclMaterialGroupPO.class, sql.toString(), null, pageSize, curPage);		
	}
	
	/*
	 * 查询表中的所有工时大类
	 * 生成动态列
	 */
	@SuppressWarnings("unchecked")
	public List<TtAsWrLabourPricePO> getExistLaborList(Long companyId){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select distinct mode_type\n");
		sql.append("from tt_as_wr_labour_price\n");
		if(companyId!=null)
			sql.append("where 1=1\n");
		sql.append("order by mode_type asc\n");
		return this.select(TtAsWrLabourPricePO.class, sql.toString(), null);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TtAsWrLabourPricePO> getExistLaborList01(Long companyId){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select distinct SERIES_CODE\n");
		sql.append("from tt_as_wr_labour_price\n");
		if(companyId!=null)
			sql.append("where 1=1\n");
		sql.append("order by SERIES_CODE asc\n");
		return this.select(TtAsWrLabourPricePO.class, sql.toString(), null);
	}
	
	/*
	 * 查询工时单价设定主数据
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> mainQuery(String sql,int pageSize,int curPage){
		return (PageResult<Map<String, Object>>)this.pageQuery(sql, null, this.getFunName(), pageSize, curPage);
	}
	
	/*
	 * 判断数据是否已经存在数据库中
	 */
	@SuppressWarnings("unchecked")
	public TtAsWrLabourPricePO existLaborPrice(TtAsWrLabourPricePO po){
		List<TtAsWrLabourPricePO> lists = this.select(po);
		if(lists.size()>0) return lists.get(0);
		return null ;
	}
	
	/****************** 微车 ********************/
	/*
	 * 主页面主查询
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> wcMainQuery(String con,int pageSize,int curPage){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select distinct a.wrgroup_code,\n" );
		List<AreaProvinceBean> list = this.getList() ;
		for(int i=0;i<list.size();i++){
			sql.append("  max(decode(area_level,'").append(list.get(i).getAreaLevel());
			sql.append("',basic_labour_price,'')) \"").append(list.get(i).getAreaLevel()).append("\",\n");
		}
		sql.append("                a.wrgroup_name\n" );
		sql.append("  from tt_as_wr_model_group a, tt_as_wr_labour_price b\n" );
		sql.append(" where a.wrgroup_code = b.mode_type\n" );
		sql.append("   and a.wrgroup_type=").append(Constant.WR_MODEL_GROUP_TYPE_01).append("\n");
		if(StringUtil.notNull(con))
			sql.append(con);
		sql.append(" group by a.wrgroup_code, a.wrgroup_name\n");
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}

	/*
	 * 查询大区列表
	 */
	@SuppressWarnings("unchecked")
	public List<AreaProvinceBean> getList(){
		String sql = " select distinct area_level,code_desc from tt_as_wr_labour_price,tc_code c where c.code_id=area_level order by area_level asc\n" ;
		return this.select(AreaProvinceBean.class, sql, null) ;
	}
	
	@SuppressWarnings("unchecked")
	public List<PriceAdjustBean> getAdjustPrice(String mode){
        StringBuffer sql=new StringBuffer("");
        sql.append("select p.id,p.adjust_mode,p.type_adjust,p.adjust_price,c.code_desc from TT_AS_WR_PRICE_ADJUST p,tc_code c where p.type_adjust=c.code_id\n");
		if(StringUtil.notNull(mode))
		{
			sql.append("and p.adjust_mode='"+mode+"'");
		}
        return this.select(PriceAdjustBean.class, sql.toString(), null) ;
	}
	
	@SuppressWarnings("unchecked")
	public PriceAdjustBean getAdjustById(String id)
	{
		String sql="select p.id,p.adjust_mode,p.type_adjust,p.adjust_price,c.code_desc from TT_AS_WR_PRICE_ADJUST p,tc_code c where p.type_adjust=c.code_id and p.id='"+id+"'";
		List<PriceAdjustBean> list= this.select(PriceAdjustBean.class, sql.toString(), null) ;
		if(list!=null)
		{
			if(list.size()>0)
			{
				return list.get(0);
			}
			else return null;
		}
		else return null;
	}
	
	@SuppressWarnings("unchecked")
	public void adjustUpdate(String id,String adjustPrice,Long userId)
	{		
		TtAsWrPriceAdjustPO sel=new TtAsWrPriceAdjustPO();
		TtAsWrPriceAdjustPO upd=new TtAsWrPriceAdjustPO();
		sel.setId(Long.parseLong(id));
		upd.setAdjustPrice(Double.parseDouble(adjustPrice));
		upd.setUpdateBy(userId);
		upd.setUpdateDate(new Date());
		this.update(sel, upd);
	}
	
	@SuppressWarnings("unchecked")
	public void updatePrice(TtAsWrLabourPricePO po,Long userId)
	{
		TtAsWrLabourPricePO sel=new TtAsWrLabourPricePO();
		TtAsWrLabourPricePO upd=new TtAsWrLabourPricePO();
		sel.setId(po.getId());
		upd.setLabourPrice(po.getLabourPrice());
		upd.setUpdateBy(userId);
		upd.setUpdateDate(new Date());
		this.update(sel,upd);		
	}
	
	@SuppressWarnings("unchecked")
	public List<TtAsWrLabourPricePO> getLabourPrice()
	{
		StringBuffer sql=new StringBuffer("");	
		sql.append("select lp.id,(nvl(lp.basic_labour_price, 0) +\n" );
		sql.append("       nvl((select lvpa.adjust_price\n" ); 
		sql.append("              from TT_AS_WR_PRICE_ADJUST lvpa, tm_dealer lvd\n" ); 
		sql.append("             where lvd.dealer_id = lp.dealer_id\n" ); 
		sql.append("               and lvpa.type_adjust = lvd.service_level),\n" ); 
		sql.append("            0) + nvl((select clspa.adjust_price\n" );
		sql.append("                        from TT_AS_WR_PRICE_ADJUST clspa, tm_dealer clsd\n" ); 
		sql.append("                       where clsd.dealer_id = lp.dealer_id\n" ); 
	    sql.append("                         and clspa.type_adjust = clsd.dealer_class),\n" ); 
		sql.append("                      0)) labour_price\n" ); 
		sql.append("  from tt_as_wr_labour_price lp\n" ); 		
		return this.select(TtAsWrLabourPricePO.class, sql.toString(),null);		
		
	}
	
	@SuppressWarnings("unchecked")
	public List checkDealer(String delCode){
		List<Map<String,Object>> list = null;
		String sql = "SELECT * FROM TM_DEALER TD WHERE TD.DEALER_CODE = '"+delCode+"'";
		list =(List<Map<String,Object>>) pageQuery(sql, null, getFunName());
		return list;
	}
	@SuppressWarnings("unchecked")
	public List checkVeh(String code){
		List<Map<String,Object>> list = null;
		String sql = "SELECT * from TM_VHCL_MATERIAL_GROUP t where t.GROUP_LEVEL = 3 and t.GROUP_CODE = '"+code+"'";
		list =(List<Map<String,Object>>) pageQuery(sql, null, getFunName());
		return list;
	}
	
	
	
	public PageResult<Map<String,Object>> getDealerInfo(String dealerCode,String dealerName,String dealerType,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select d.dealer_code,d.dealer_name\n");
		sql.append("from tm_dealer d\n");
		if("1".equalsIgnoreCase(dealerType)){
			sql.append("left join tt_as_wr_labour_price p on p.dealer_id = d.dealer_id\n");
		}else{
			sql.append("left join tm_down_parameter p on p.dealer_id = d.dealer_id\n");
		}
		sql.append("where p.dealer_id is null\n");
		sql.append("and d.dealer_type="+Constant.DEALER_TYPE_DWR); 
		if(Utility.testString(dealerCode)){
			sql.append("and d.dealer_code like '%"+dealerCode+"%'"); 
		}
		if(Utility.testString(dealerName)){
			sql.append("and d.dealer_name like '%"+dealerName+"%'"); 
		}
		return (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);	
	}
	public PageResult<Map<String,Object>> getDealerInfo2(String dealerCode,String dealerName,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select d.dealer_id,d.dealer_code,d.dealer_name\n");
		sql.append("from tm_dealer d\n");
		sql.append("where d.dealer_type="+Constant.DEALER_TYPE_DWR+"\n"); 
		sql.append(" and  d.dealer_level="+Constant.DEALER_LEVEL_01+"\n"); 
		if(Utility.testString(dealerCode)){
			sql.append("	and d.dealer_code like '%"+dealerCode.toUpperCase()+"%'"); 
		}
		if(Utility.testString(dealerName)){
			sql.append("	and d.dealer_name like '%"+dealerName+"%'"); 
		}
		return (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);	
	}
	
	public PageResult<Map<String,Object>> getMailPrintDetail(String dealerCode,String dealerName,String bDate,String eDate,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  TO_CHAR(D.PRINT_DATE,'YYYY-MM-DD HH24:MI') PRINT_TIME,U.NAME,DD.DEALER_CODE,DD.DEALER_NAME,D.PRINT_REMARK,D.PRINT_IN_DEPT,\n");
		sql.append("D.PRINT_TYPE\n");
		sql.append("FROM   TT_AS_MAIL_PRINT_DETAIL  D ,\n");
		sql.append(" TM_DEALER DD ,TC_USER U\n");
		sql.append("WHERE DD.DEALER_ID = D.PRINT_TO_DEALER\n");
		sql.append("AND D.PRINT_BY = U.USER_ID\n"); 

		if(Utility.testString(dealerCode)){
			sql.append(" and dd.dealer_code like '%"+dealerCode.toUpperCase()+"%'");
		}
		if(Utility.testString(dealerName)){
			sql.append(" and dd.dealer_name like '%"+dealerName+"%'");
		}
		if(Utility.testString(bDate)){
			sql.append(" and d.print_date>=to_date('"+bDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		}
		if(Utility.testString(eDate)){
			sql.append(" and d.print_date<=to_date('"+eDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		return (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	} 
	public Map<String, Object> getRoDetail(String roNo){
		StringBuffer sql = new StringBuffer();
		sql.append("select o.ro_no,o.in_mileage,v.mileage,d.dealer_name,decode(a.ro_no,null,'否','是') has_claim,d.dealer_id,v.vin\n");
		sql.append("from Tt_As_Repair_Order o\n");
		sql.append("left join  tm_vehicle v on o.vin = v.vin\n");
		sql.append("left join tm_dealer d on nvl(o.second_dealer_id,o.dealer_id) = d.dealer_id\n");
		sql.append("left join Tt_As_Wr_Application a on a.ro_no = o.ro_no\n");
		sql.append("where o.create_date>=to_date('2013-08-26','yyyy-mm-dd')\n");
		sql.append("and o.ro_no='"+roNo+"'"); 
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	} 
	
	public PageResult<Map<String,Object>> getModDetail(String roNo,String vin,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select c.ro_no,c.mod_before,c.mod_after,u.name,to_char(c.mod_date,'yyyy-mm-dd hh24:mi') mod_time,c.mod_system,c.vin,d.dealer_shortname dealer_name\n");
		sql.append("from Tt_As_Mileage_Change c ,tm_dealer d ,tc_user u\n");
		sql.append("where c.mod_by = u.user_id and c.dealer_id = d.dealer_id\n");
		if(Utility.testString(roNo)){
			sql.append("and c.ro_no like '%"+roNo.toUpperCase()+"%'\n");
		}
		if(Utility.testString(vin)){
			sql.append("and c.vin like '%"+vin.toUpperCase()+"%'\n");
		}
		return (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);	
	}
	public PageResult<Map<String,Object>> getModelList(String modelCode,String modelName,String status,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select  p.id ,p.model_code,p.model_name,p.model_price,p.status,u.name ,u1.name name1 ,to_char(p.create_date,'yyyy-mm-dd') create_time,to_char(p.update_date,'yyyy-mm-dd') update_time\n");
		sql.append("from tt_AS_wr_model_price p\n");
		sql.append("left join tc_user u on u.user_id = p.create_by\n");
		sql.append("left join tc_user u1 on u1.user_id = p.update_by\n"); 

		sql.append(" where 1=1 "); 

		if(Utility.testString(modelCode)){
			sql.append("and p.mode_code like '%"+modelCode.toUpperCase()+"%'\n");
		}
		if(Utility.testString(modelName)){
			sql.append("and p.model_name  like '%"+modelName.toUpperCase()+"%'\n");
		}
		if(Utility.testString(status)){
			sql.append("and p.status = "+status+"\n");
		}
		return (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);	
	}
}
