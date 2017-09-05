package com.infodms.dms.dao.crm.customer;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**
 * 
 * <p>ComplaintAuditDao.java</p>
 *
 * <p>Description: 客户投诉处理明细持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-2</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class CustomerManageDao extends BaseDao<PO>{
	
	private static final CustomerManageDao dao = new CustomerManageDao();
	
	public static final CustomerManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * FUNCTION		:	查询活跃客户信息
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getCustomerQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String customerName=map.get("customerName");
		String phone=map.get("phone");
		String vechile=map.get("vechile");
		String ctmNo=map.get("ctmNo");
		String ctmType=map.get("ctmType");
		String dealerId=map.get("dealerId");
		String logonId=map.get("logonId");
		String adviserId=map.get("adviserId");
		String groupId=map.get("groupId");
		String ctmRank=map.get("ctmRank");
		String jcway=map.get("jcway");//集客方式
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT tpc.customer_id,\n" );
		sql.append("            tpc.customer_code,\n" );
		sql.append("            tpc.customer_name,\n" );
		sql.append("            c3.code_desc as jc_way,\n" );
		sql.append("            c3.code_id as jc_way_code,\n" );
		sql.append("            tpc.telephone,\n" );
		sql.append("            tu.name,\n" );
		sql.append("            tpc.address,\n" );
		sql.append("            tpc.CTM_TYPE,\n" );
		sql.append("            tpc.ctm_rank,\n" );
		sql.append("            tpiv.series_Name intent_vehicle,\n" );
		sql.append("            tpc.status\n" );
		sql.append("       FROM t_pc_customer tpc,TC_CODE c3,t_pc_intent_vehicle tpiv ,tc_user tu  where tpc.intent_vehicle=tpiv.series_id(+) and tu.user_id(+)=tpc.adviser and tpc.jc_way = c3.CODE_ID(+)  \n" );
		if(null!=customerName&&!"".equals(customerName)){
			sql.append(" and tpc.customer_name like '%"+customerName+"%'\n");
		}
		if(null!=phone&&!"".equals(phone)){
			sql.append(" and tpc.telephone like '%"+phone+"%'\n");
		}
		if(null!=startDate&&!"".equals(startDate)){
			sql.append(" and tpc.create_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') ");
		}
		if(null!=endDate&&!"".equals(endDate)){
			sql.append(" and tpc.create_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
		}
		if(null!=ctmRank&&!"".equals(ctmRank)){
			sql.append(" and tpc.ctm_rank="+ctmRank+"\n");
		}
		if(null!=ctmNo&&!"".equals(ctmNo)){
			sql.append(" and tpc.customer_code like '%"+ctmNo+"%'\n");
		}
		if(null!=ctmType&&!"".equals(ctmType)){
			sql.append(" and tpc.ctm_Type = '"+ctmType+"'\n");
		}
		if(null!=vechile&&!"".equals(vechile)){
			sql.append(" and tpiv.series_name  like '%"+vechile+"%'\n" );
		}
		if(Utility.testString(adviserId)){
			sql.append(" and tpc.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(jcway)){
			sql.append(" and tpc.jc_way = '"+jcway+"' \n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
				if(logonId!=null&&!"".equals(logonId)){
					sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
					sql.append("  CONNECT BY   nocycle  PRIOR tcu.user_id = tcu.par_user_iD)");
				}
			
		}
		
		
		sql.append("      order by tpc.create_date desc");
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * FUNCTION		:	查询所有客户信息
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getCustomerAllList(
			Map<String, String> map, int pageSize, int curPage) {
		String customerName=map.get("customerName");
		String phone=map.get("phone");
		String vechile=map.get("vechile");
		String ctmNo=map.get("ctmNo");
		String dealerId=map.get("dealerId");
		String logonId=map.get("logonId");
		String adviserId=map.get("adviserId");
		String groupId=map.get("groupId");
		String jcway=map.get("jcway");//集客方式
		System.out.println("所有客户信息");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT tpc.customer_id,\n" );
		sql.append("            tpc.customer_code,\n" );
		sql.append("            tpc.customer_name,\n" );
		sql.append("            c3.code_desc as jc_way,\n" );
		sql.append("            c3.code_id as jc_way_code,\n" );
		sql.append("            tpc.telephone,\n" );
		sql.append("            tu.name,\n" );
		sql.append("            tpc.address,\n" );
		sql.append("            tpiv.series_Name intent_vehicle,\n" );
		sql.append(" tpc.ctm_rank,\n");
		sql.append(" tpc.ctm_type,\n");
		sql.append("            tpc.status\n" );
		sql.append("       FROM t_pc_customer tpc,TC_CODE c3,t_pc_intent_vehicle tpiv,tc_user tu where tpc.intent_vehicle=tpiv.series_id(+) and tu.user_id(+)=tpc.adviser and tpc.jc_way = c3.CODE_ID(+) \n" );
		if(null!=customerName&&!"".equals(customerName)){
			sql.append(" and tpc.customer_name like '%"+customerName+"%'\n");
		}
		if(null!=phone&&!"".equals(phone)){
			sql.append(" and tpc.telephone like '%"+phone+"%'\n");
		}
		if(null!=ctmNo&&!"".equals(ctmNo)){
			sql.append(" and tpc.customer_code like '%"+ctmNo+"%'\n");
		}
		if(null!=vechile&&!"".equals(vechile)){
			sql.append(" and tpiv.series_name  like '%"+vechile+"%'\n" );
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
		}
		if(Utility.testString(adviserId)){
			sql.append(" and tpc.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(jcway)){
			sql.append(" and tpc.jc_way = '"+jcway+"' \n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
			sql.append("  CONNECT BY nocycle PRIOR tcu.user_id = tcu.par_user_iD)");
		}
		sql.append("      order by tpc.create_date desc ");
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 加载试乘试驾信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> driveList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpd.driving_id,\n" );
		sql.append("       tpd.card_no,\n" );
		sql.append("       to_char(tpd.driving_date,'yyyy-MM-dd') driving_date,\n" );
		sql.append("       tpv.series_name driving_vechile,\n" );
		sql.append("       tpd.driving_man,\n" );
		sql.append("       tpd.driving_road,\n" );
		sql.append("       tpd.first_mile,\n" );
		sql.append("       tpd.end_mile\n" );
		sql.append("  from t_pc_driving tpd,t_pc_intent_vehicle tpv\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("   and tpd.status = 10011001");
		sql.append("   and tpv.series_id(+)= tpd.driving_vechile");
		sql.append("   and tpd.ctm_id = "+ctmId);
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 加载其他联系人
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> linkList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpl.link_id,\n" );
		sql.append("       1 op_type,\n" );
		sql.append("       tpl.link_man,\n" );
		sql.append("       tpl.link_phone,\n" );
		sql.append("       tc.code_desc card_type,\n" );
		sql.append("       tpl.card_code,\n" );
		sql.append("       tpl.relationship,\n" );
		sql.append("       tpl.OLD_VEHICLE_ID \n" );
		sql.append("  from t_pc_link_man tpl,tc_code tc\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("   and tpl.status = 10011001\n" );
		sql.append("   and tc.code_id(+)=tpl.card_type\n" );
		sql.append("   and tpl.ctm_id = "+ctmId+"\n" );
		sql.append("UNION\n" );
		sql.append("select null,\n" );
		sql.append("       0,\n" );
		sql.append("       ttc.ctm_name,\n" );
		sql.append("       ttc.main_phone,\n" );
		sql.append("       tc.code_desc card_type,\n" );
		sql.append("       ttc.card_num,\n" );
		sql.append("       '车主',\n" );
		sql.append("       null \n" );
		sql.append("  from t_pc_customer          tpc,\n" );
		sql.append("       t_pc_delvy             tpd,\n" );
		sql.append("       tt_dealer_actual_sales tdas,\n" );
		sql.append("       tt_customer            ttc,\n" );
		sql.append("       tc_code                tc\n" );
		sql.append(" where tpc.customer_id = tpd.customer_id\n" );
		sql.append("   and tdas.vehicle_id = tpd.vehicle_id\n" );
		sql.append("   and ttc.ctm_id = tdas.ctm_id\n" );
		sql.append("   and tc.code_id(+)=ttc.ctm_type\n" );
		sql.append("   and tdas.is_return = 10041002\n" );
		sql.append("   and tpc.customer_id = "+ctmId+"");



		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 加载装饰装潢
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> docList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpd.decoration_id,\n" );
		sql.append("       tpd.exproject,\n" );
		sql.append("       tpd.exname,\n" );
		sql.append("       tpd.amount,\n" );
		sql.append("       tpd.price,\n" );
		sql.append("       tpd.MONEY,\n" );
		sql.append("       tpd.giveorbuy\n" );
		sql.append("  from t_pc_decoration tpd\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("   and tpd.status = 10011001\n" );
		sql.append("   and tpd.ctm_id = "+ctmId);

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 加载保险信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> insureList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpi.insurence_id,\n" );
		sql.append("       nvl(tc.code_desc,' ') insurence_company,\n" );
		sql.append("       to_char(tpi.insurence_date,'yyyy-mm-dd') insurence_date,\n" );
		sql.append("       tpi.insurence_money,\n" );
		sql.append("       tpi.insurence_var,\n" );
		sql.append("       tpi.remark\n" );
		sql.append("  from t_pc_insurence tpi,tc_code tc\n" );
		sql.append(" where tc.code_id(+) = tpi.insurence_company\n" );
		sql.append("   and tpi.status = 10011001");
		sql.append("   and tpi.ctm_id = "+ctmId);

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 加载接触点信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> contactList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select ROWNUM NUM,tpp.point_id,\n" );
		sql.append(" to_char(tpp.point_date,'yyyy-mm-dd hh24:mi:ss') point_date,\n" );
		sql.append(" tc.code_desc point_way,\n" );
		sql.append("  tpp.point_content\n" );
		sql.append("  from t_pc_contact_point tpp,tc_code tc\n" );
		sql.append(" where tc.code_id = tpp.point_way");
		sql.append("   and tpp.ctm_id = "+ctmId);
		sql.append("   order by tpp.point_date desc ");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 查询车两信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> vehicleList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpv.vechile_id,\n" );
		sql.append("       1 op_type,\n" );
		sql.append("       nvl(tpv.vin,' ') vin,\n" );
		sql.append("       nvl(tpv.model_code,' ') model_code,\n" );
		sql.append("       nvl(tpv.model_name,' ') model_name,\n" );
		sql.append("       to_char(tpv.buy_date, 'YYYY-MM-dd') buy_date,\n" );
		sql.append("       nvl(tc.code_desc,' ') vechile_color,\n" );
		sql.append("       nvl(tpv.low_vin,' ')  low_vin,\n" );
		sql.append("       tpv.price,\n" );
		sql.append("       nvl(tpv.car_number,'') car_number,\n" );
		sql.append("       to_char(tpv.board_date, 'YYYY-MM-Dd') board_date,\n" );
		sql.append("       nvl(tpv.pin,' ')pin,\n" );
		sql.append("       to_char(tpv.product_date, 'yyyy-MM-dd') product_date\n" );
		sql.append("  from t_pc_vechile tpv,tc_code tc\n" );
		sql.append(" where tc.code_id(+) = to_char(tpv.vechile_color)\n" );
		sql.append("   and tpv.status = 10011001\n" );
		sql.append("   and tpv.ctm_id = "+ctmId+"\n" );
		sql.append("   union\n" );
		sql.append("   select tpd.vehicle_id,0,tv.vin,tvm.material_code model_code,\n" );
		sql.append("   tvm.material_name model_name,to_char(tv.purchased_date,'YYYY-MM-DD') buy_date,\n" );
		sql.append("   tv.color vechile_color, tv.vn low_vin,tdas.price, tdas.vehicle_no car_number,null,null,\n" );
		sql.append("   to_char(tv.product_date, 'yyyy-MM-dd') product_date\n" );
		sql.append("    from t_pc_customer tpc,tt_dealer_actual_sales tdas,t_pc_delvy tpd ,tm_vehicle tv ,tm_vhcl_material tvm\n" );
		sql.append("   where tpd.vehicle_id=tdas.vehicle_id\n" );
		sql.append("   and tpc.customer_id=tpd.customer_id\n" );
		sql.append("   and tv.vehicle_id=tpd.vehicle_id\n" );
		sql.append("   and tv.material_id=tvm.material_id\n" );
		sql.append("   and tdas.is_return=10041002\n" );
		sql.append("   and tpc.customer_id="+ctmId);


		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	public PageResult<Map<String, Object>> getIntentVechileList(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		String seriesName=map.get("seriesName");
		String seriesCode=map.get("seriesCode");
		StringBuilder sql= new StringBuilder();
		sql.append("select TPV.series_id,\n" );
		sql.append("       TPV.series_code,\n" );
		sql.append("       TPV.series_name,\n" );
		sql.append("       TPV.status,\n" );
		sql.append("       TPV1.series_code           UP_CODE,\n" );
		sql.append("       TPV1.series_name           UP_NAME\n" );
		sql.append("  from t_pc_intent_vehicle TPV, t_pc_intent_vehicle TPV1\n" );
		sql.append(" WHERE TPV.up_series_id = TPV1.series_id");
		if(seriesCode!=null&&!"".equals(seriesCode)){
			sql.append(" and tpv.series_code like '%"+seriesCode+"%'\n");
		}
		if(seriesName!=null&&!"".equals(seriesName)){
			sql.append(" and tpv.series_name like '%"+seriesName+"%'\n");
		}
		PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return ps ;
	}
	public int lookList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		String poseRank=map.get("poseRank");
		StringBuilder sql= new StringBuilder();
		sql.append("select count(1) total\n" );
		sql.append("          from t_pc_look tpc, tc_user tu\n" );
		sql.append("         where tpc.user_id = tu.user_id\n" );
		sql.append("           and tpc.ctm_id = "+ctmId+"\n" );
		sql.append("           and tu.pose_rank="+poseRank);
		int count=0;
			List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		count=Integer.parseInt((list.get(0).get("TOTAL").toString()));
		
		return count;
	}
	public PageResult<Map<String, Object>> getCompetVechileList(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		String competLevel=map.get("competLevel");
		String competCode=map.get("competCode");
		String competName=map.get("competName");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpv.series_id    compet_id,\n" );
		sql.append("       tpv.series_code  compet_code,\n" );
		sql.append("       tpv.series_name  compet_name,\n" );
		sql.append("       tpv.status,\n" );
		sql.append("       tpv.up_series_id par_id\n" );
		sql.append("  from t_pc_defeat_vehicle tpv");
		sql.append(" where 1=1\n" );
		sql.append("  and tpv.status=10011001 \n" );
		if("2".equals(competLevel)){
			sql.append(" and tpv.up_series_id is not null");
		}else{
			sql.append(" and tpv.up_series_id is  null");
		}
		if(competCode!=null&&!"".equals(competCode)){
			sql.append("  and tpv.series_code like '%"+competCode+"%'\n" );
		}
		if(competName!=null&&!"".equals(competName)){
			sql.append("  and tpv.series_name like '%"+competName+"%'\n" );
		}
		sql.append(" order by tpv.series_id");

		PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return ps ;
	}
	public List<Map<String, Object>> getRankVarList(Map<String, String> map) {
		String ctmRank=map.get("ctmRank");
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select nvl(ts.count, 0) count, ts.max_date\n" );
		sql.append("  from (select count(1) count, to_char(max(t.create_date),'yyyy-mm-dd') max_date, t.new_level\n" );
		sql.append("          from (select tpi.create_date, tpi.new_level, tpi.customer_id\n" );
		sql.append("                  from t_pc_invite tpi, tc_code tc1, tc_code tc2\n" );
		sql.append("                 where tc1.code_id(+) = tpi.old_level\n" );
		sql.append("                   and tc2.code_id(+) = tpi.new_level\n" );
		sql.append("                   and tpi.task_status = 60171002\n" );
		sql.append("                   and tpi.customer_id="+ctmId+"\n" );
		sql.append("                union\n" );
		sql.append("                select tps.create_date, tps.new_level, tps.customer_id\n" );
		sql.append("                  from t_pc_invite_shop tps, tc_code tc1, tc_code tc2\n" );
		sql.append("                 where tc1.code_id(+) = tps.old_level\n" );
		sql.append("                   and tc2.code_id(+) = tps.new_level\n" );
		sql.append("                   and tps.task_status = 60171002\n" );
		sql.append("                   and tps.customer_id="+ctmId+"\n" );
		sql.append("                union\n" );
		sql.append("                select tpf.create_date, tpf.new_level, tpf.customer_id\n" );
		sql.append("                  from t_pc_follow tpf, tc_code tc1, tc_code tc2\n" );
		sql.append("                 where tc1.code_id(+) = tpf.old_level\n" );
		sql.append("                   and tc2.code_id(+) = tpf.new_level\n" );
		sql.append("                   and tpf.task_status = 60171002\n" );
		sql.append("                   and tpf.customer_id="+ctmId+"\n" );
		sql.append("union\n" );
		sql.append("             select tpf.create_date, tpf.new_level, tpf.customer_id\n" );
		sql.append("               from t_pc_order tpf, tc_code tc1, tc_code tc2\n" );
		sql.append("              where tc1.code_id(+) = tpf.old_level\n" );
		sql.append("                and tc2.code_id(+) = tpf.new_level\n" );
		sql.append("                and tpf.task_status = 60171002\n" );
		sql.append("                and tpf.customer_id="+ctmId+"\n" );
		sql.append("                union\n" );
		sql.append("             select tpf.create_date, tpf.new_level, tpf.customer_id\n" );
		sql.append("               from t_pc_order tpf, tc_code tc1, tc_code tc2\n" );
		sql.append("              where tc1.code_id(+) = tpf.old_level\n" );
		sql.append("                and tc2.code_id(+) = tpf.new_level\n" );
		sql.append("                and tpf.customer_id="+ctmId+"\n");
		sql.append("                   ) t\n" );
		sql.append("         group by t.new_level) ts,\n" );
		sql.append("       tc_code tc\n" );
		sql.append(" where ts.new_level(+) = tc.code_id\n" );
		sql.append("   and tc.type = 6010\n" );

		sql.append("   and tc.code_id = "+ctmRank+"\n" );
		sql.append(" order by tc.code_id");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	public List<Map<String, Object>> getVarList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select to_char(tt.create_date,'yyyy-mm-dd')||','||tt.flow as flow from (select ts.create_date  ,\n" );
		sql.append("      ts.op_type ||','||ts.old_level || '→' || ts.new_level ||'<br/>'||ts.old_proc||'→'||ts.new_proc||'<br/>'||ts.remark as flow\n" );
		sql.append("  from (select t.create_date,\n" );
		sql.append("               t.new_level,\n" );
		sql.append("               t.old_level,\n" );
		sql.append("               t.old_proc,\n" );
		sql.append("               t.new_proc,\n" );
		sql.append("               t.remark,\n" );
		sql.append("               t.customer_id,\n" );
		sql.append("               t.op_type\n" );
		sql.append("          from (select tpi.PLAN_INVITE_DATE create_date,\n" );
		sql.append("                       nvl(tc2.code_desc, '无') new_level,\n" );
		sql.append("                       nvl(tc1.code_desc, '无') old_level,\n" );
		sql.append("                        nvl(tc3.code_desc, '无') new_proc,\n" );
		sql.append("                       nvl(tc4.code_desc, '无') old_proc,\n" );
		sql.append("                       nvl(tpi.remark, '') remark,\n" );
		sql.append("                       '邀约' op_type,\n" );
		sql.append("                       tpi.customer_id\n" );
		sql.append("                  from t_pc_invite tpi, tc_code tc1, tc_code tc2,tc_code tc3,tc_code tc4\n" );
		sql.append("                 where tc1.code_id(+) = tpi.old_level\n" );
		sql.append("                   and tc2.code_id(+) = tpi.new_level\n" );
		sql.append("                   and tc3.code_id(+)=tpi.sales_progress\n" );
		sql.append("                   and tc4.code_id(+)=tpi.old_sales_progress\n" );
		sql.append("                   and tpi.task_status=60171002\n" );
		sql.append("                union all\n" );
		sql.append("                select tps.INVITE_SHOP_DATE create_date,\n" );
		sql.append("                       nvl(tc2.code_desc, '无') new_level,\n" );
		sql.append("                       nvl(tc1.code_desc, '无') old_level,\n" );
		sql.append("                         nvl(tc3.code_desc, '无') new_proc,\n" );
		sql.append("                       nvl(tc4.code_desc, '无') old_proc,\n" );
		sql.append("                       nvl(tps.remark, '') remark,\n" );
		sql.append("                       '邀约到店' op_type,\n" );
		sql.append("                       tps.customer_id\n" );
		sql.append("                  from t_pc_invite_shop tps, tc_code tc1, tc_code tc2,tc_code tc3,tc_code tc4\n" );
		sql.append("                 where tc1.code_id(+) = tps.old_level\n" );
		sql.append("                   and tc2.code_id(+) = tps.new_level\n" );
		sql.append("                     and tc3.code_id(+)=tps.sales_progress\n" );
		sql.append("                   and tc4.code_id(+)=tps.old_sales_progress\n" );
		sql.append("                    and tps.task_status=60171002\n" );
		sql.append("                union all\n" );
		sql.append("\n" );
		sql.append("                select tpf.follow_date create_date,\n" );
		sql.append("                       nvl(tc2.code_desc, '无') new_level,\n" );
		sql.append("                       nvl(tc1.code_desc, '无') old_level,\n" );
		sql.append("                         nvl(tc3.code_desc, '无') new_proc,\n" );
		sql.append("                       nvl(tc4.code_desc, '无') old_proc,\n" );
		sql.append("                       nvl(tpf.remark, '') remark,\n" );
		sql.append("                       '跟进' op_type,\n" );
		sql.append("                       tpf.customer_id\n" );
		sql.append("                  from t_pc_follow tpf, tc_code tc1, tc_code tc2,tc_code tc3,tc_code tc4\n" );
		sql.append("                 where tc1.code_id(+) = tpf.old_level\n" );
		sql.append("                   and tc2.code_id(+) = tpf.new_level\n" );
		sql.append("                     and tc3.code_id(+)=tpf.sales_progress\n" );
		sql.append("                   and tc4.code_id(+)=tpf.old_sales_progress\n" );
		sql.append("                    and tpf.task_status=60171002\n" );
		sql.append("                    union all\n" );
		sql.append("                     select tpf.order_date create_date,\n" );
		sql.append("                       nvl(tc2.code_desc, '无') new_level,\n" );
		sql.append("                       nvl(tc1.code_desc, '无') old_level,\n" );
		sql.append("                         nvl(tc3.code_desc, '无') new_proc,\n" );
		sql.append("                       nvl(tc4.code_desc, '无') old_proc,\n" );
		sql.append("                       nvl(tpf.remark, '') remark,\n" );
		sql.append("                       '订单' op_type,\n" );
		sql.append("                       tpf.customer_id\n" );
		sql.append("                  from t_pc_order tpf, tc_code tc1, tc_code tc2,tc_code tc3,tc_code tc4\n" );
		sql.append("                 where tc1.code_id(+) = tpf.old_level\n" );
		sql.append("                   and tc2.code_id(+) = tpf.new_level\n" );
		sql.append("                     and tc3.code_id(+)=tpf.sales_progress\n" );
		sql.append("                   and tc4.code_id(+)=tpf.old_sales_progress\n" );
		sql.append("                    and tpf.task_status=60171002\n" );
		sql.append("                    union all\n" );
		sql.append("                     select tpf.MGR_CON_DATE create_date,\n" );
		sql.append("                       nvl(tc2.code_desc, '无') new_level,\n" );
		sql.append("                       nvl(tc1.code_desc, '无') old_level,\n" );
		sql.append("                         nvl(tc3.code_desc, '无') new_proc,\n" );
		sql.append("                       nvl(tc4.code_desc, '无') old_proc,\n" );
		sql.append("                       nvl(tpf.REASON_ANALYSIS, '') remark,\n" );
		sql.append("                       '战败' op_type,\n" );
		sql.append("                       tpf.customer_id\n" );
		sql.append("                  from t_pc_defeatfailure tpf, tc_code tc1, tc_code tc2,tc_code tc3,tc_code tc4\n" );
		sql.append("                 where tc1.code_id(+) = tpf.old_level\n" );
		sql.append("                   and tc2.code_id(+) = tpf.new_level\n" );
		sql.append("                     and tc3.code_id(+)=tpf.sales_progress\n" );
		sql.append("                   and tc4.code_id(+)=tpf.old_sales_progress\n" );
		sql.append("                   and tpf.defeatfailure_type=60391001\n" );
		sql.append("                    union all\n" );
		sql.append("                     select tpf.MGR_CON_DATE create_date,\n" );
		sql.append("                       nvl(tc2.code_desc, '无') new_level,\n" );
		sql.append("                       nvl(tc1.code_desc, '无') old_level,\n" );
		sql.append("                         nvl(tc3.code_desc, '无') new_proc,\n" );
		sql.append("                       nvl(tc4.code_desc, '无') old_proc,\n" );
		sql.append("                       nvl(tpf.REASON_ANALYSIS, '') remark,\n" );
		sql.append("                       '失效' op_type,\n" );
		sql.append("                       tpf.customer_id\n" );
		sql.append("                  from t_pc_defeatfailure tpf, tc_code tc1, tc_code tc2,tc_code tc3,tc_code tc4\n" );
		sql.append("                 where tc1.code_id(+) = tpf.old_level\n" );
		sql.append("                   and tc2.code_id(+) = tpf.new_level\n" );
		sql.append("                     and tc3.code_id(+)=tpf.sales_progress\n" );
		sql.append("                   and tc4.code_id(+)=tpf.old_sales_progress\n" );
		sql.append("                   and tpf.defeatfailure_type=60391002\n" );
		sql.append("                   union all\n" );
		sql.append("                   select tpc.delivery_date create_date,\n" );
		sql.append("                       nvl(tc2.code_desc, '无') new_level,\n" );
		sql.append("                       nvl(tc1.code_desc, '无') old_level,\n" );
		sql.append("                         nvl(tc3.code_desc, '无') new_proc,\n" );
		sql.append("                       nvl(tc4.code_desc, '无') old_proc,\n" );
		sql.append("                       nvl(tpc.remark, '') remark,\n" );
		sql.append("                       '交车' op_type,\n" );
		sql.append("                       tpf.customer_id\n" );
		sql.append("                  from t_pc_delvy tpc, t_pc_customer tpf,tc_code tc1, tc_code tc2,tc_code tc3,tc_code tc4\n" );
		sql.append("                 where tc1.code_id(+) = tpf.ctm_rank\n" );
		sql.append("                   and tc2.code_id(+) = tpf.ctm_rank\n" );
		sql.append("                     and tc3.code_id=60371007\n" );
		sql.append("                   and tc4.code_id=60371006\n" );
		sql.append("                   and tpf.customer_id=tpc.customer_id\n" );
		sql.append("                   union all\n" );
		sql.append("                   select tpc.revisit_date create_date,\n" );
		sql.append("                       nvl(tc2.code_desc, '无') new_level,\n" );
		sql.append("                       nvl(tc1.code_desc, '无') old_level,\n" );
		sql.append("                         nvl(tc3.code_desc, '无') new_proc,\n" );
		sql.append("                       nvl(tc4.code_desc, '无') old_proc,\n" );
		sql.append("                       nvl(tpc.remark, '') remark,\n" );
		sql.append("                       '回访' op_type,\n" );
		sql.append("                       tpf.customer_id\n" );
		sql.append("                  from t_pc_revisit tpc, t_pc_customer tpf,tc_code tc1, tc_code tc2,tc_code tc3,tc_code tc4\n" );
		sql.append("                 where tc1.code_id(+) = tpf.ctm_rank\n" );
		sql.append("                   and tc2.code_id(+) = tpf.ctm_rank\n" );
		sql.append("                     and tc3.code_id(+)=tpf.sales_progress\n" );
		sql.append("                   and tc4.code_id(+)=tpf.sales_progress\n" );
		sql.append("                   and tpf.customer_id=tpc.customer_id\n" );
		sql.append("                    and tpc.task_status=60171002\n" );
		sql.append("                    ) t\n" );
		sql.append("         order by t.create_date desc) ts\n" );
		sql.append("         where ts.customer_id="+ctmId+"\n" );
		sql.append("         union\n" );
		sql.append("   select tpl.collect_date create_date, tc1.code_desc ||','|| tc.code_desc\n" );
		sql.append("  from t_pc_customer tpc, t_pc_leads tpl, tc_code tc, tc_code tc1\n" );
		sql.append(" where tpc.customer_id = tpl.customer_id\n" );
		sql.append("   and tc.code_id(+) = tpl.leads_origin\n" );
		sql.append("   and tc1.code_id(+) = tpl.jc_way\n" );
		sql.append("   and tpc.customer_id = "+ctmId+") tt order by tt.create_date desc");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 获取接下来该客户对应的任务
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getNextTask(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select a.sort_id, a.task_id, a.customer_id, a.optype,to_char(a.plan_date,'yyyy-mm-dd') plan_date, a.status\n" );
		sql.append("  from (select tpi.invite_id sort_id,\n" );
		sql.append("               tpi.invite_id task_id,\n" );
		sql.append("               tpi.customer_id,\n" );
		sql.append("               null status,\n" );
		sql.append("               tpi.plan_invite_date plan_date,\n" );
		sql.append("               '邀约' opType\n" );
		sql.append("          from t_pc_invite tpi\n" );
		sql.append("         where tpi.task_status = 60171001\n" );
		sql.append("        union\n" );
		sql.append("        select tpis.invite_id      SORT_ID,\n" );
		sql.append("               tpis.invite_shop_id,\n" );
		sql.append("               tpis.customer_id,\n" );
		sql.append("               null                status,\n" );
		sql.append("               tpis.invite_shop_date plan_date,\n" );
		sql.append("               '邀约到店'\n" );
		sql.append("          from t_pc_invite_shop tpis\n" );
		sql.append("         where tpis.task_status = 60171001\n" );
		sql.append("        union\n" );
		sql.append("        select tpf.follow_id SORT_ID,\n" );
		sql.append("               tpf.follow_id,\n" );
		sql.append("               tpf.customer_id,\n" );
		sql.append("\n" );
		sql.append("               null,\n" );
		sql.append("               tpf.follow_date  plan_date,\n" );
		
		sql.append("               '跟进'\n" );
		sql.append("          from t_pc_follow tpf\n" );
		sql.append("         where tpf.task_status = 60171001\n" );
		sql.append("        union\n" );
		sql.append("        select tpo.order_id SORT_ID,\n" );
		sql.append("               tpo.order_id,\n" );
		sql.append("               tpo.customer_id,\n" );
		sql.append("               tpo.order_status,\n" );
		sql.append("                tpo.order_date plan_date,\n" );
		sql.append("               '订单'\n" );
		sql.append("          from t_pc_order tpo\n" );
		sql.append("         where tpo.task_status = 60171001\n" );
		sql.append("        union\n" );
		sql.append("        select tpod.order_id SORT_ID,\n" );
		sql.append("               tpod.order_detail_id,\n" );
		sql.append("               tpod.customer_id,\n" );
		sql.append("               null,\n" );
		sql.append("              tpod.delivery_date plan_date,\n" );
		sql.append("               '交车'\n" );
		sql.append("          from t_pc_order_detail tpod\n" );
		sql.append("         where tpod.task_status = 60171001\n" );
		sql.append("        union\n" );
		sql.append("        select null, tpr.revisit_id, tpr.customer_id, null,tpr.revisit_date plan_date, '回访'\n" );
		sql.append("          from t_pc_revisit tpr) a,\n" );
		sql.append("       t_pc_customer tpc\n" );
		sql.append(" where tpc.customer_id=a.customer_id\n");
		sql.append("and tpc.customer_id="+ctmId);

		return  dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	//加载客户信息对应的下拉列表
	public List<Map<String, Object>> loadDataList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpc.customer_id,\n" );
		sql.append("tc1.code_desc paper_type, tc1.code_id paper_type_id,\n" );
		sql.append("tc2.code_desc car_frum, tc2.code_id car_frum_id,\n" );
		sql.append("tc3.code_desc defeat_type,tc3.code_id defeat_type_id,\n" );
		sql.append("tc4.code_desc defeat_reason,tc4.code_id defeat_reason_id,\n" );
		sql.append("tc5.code_desc fit_area,tc5.code_id fit_area_id,\n" );
		sql.append("tc6.code_desc fit_time,tc6.code_id fit_time_id,\n" );
		sql.append("tc7.code_desc concern,tc7.code_id concern_id,\n" );
		sql.append("tc8.code_desc buy_use, tc8.code_id buy_use_id,\n" );
		sql.append("tc9.code_desc color_like,tc9.code_id color_like_id,\n" );
		sql.append("tc10.code_desc if_marry,tc10.code_id if_marry_id,\n" );
		sql.append("tc11.code_desc political,tc11.code_id political_id,\n" );
		sql.append("tc12.code_desc gender,tc12.code_id gender_id,\n" );
		sql.append("tc13.code_desc income,tc13.code_id income_id,\n" );
		sql.append("tc14.code_desc industry, tc14.code_id industry_id,\n" );
		sql.append("tc15.code_desc job,tc15.code_id job_id,\n" );
		sql.append("tc16.code_desc interest_one, tc6.code_id interest_one_id,\n" );
		sql.append("tc17.code_desc interest_two, tc17.code_id interest_two_id,\n" );
		sql.append("tc18.code_desc interest_three,tc18.code_id interest_three_id,\n" );
		sql.append("tc19.code_desc ctm_prop,tc19.code_id ctm_prop_id,\n" );
		sql.append("tc20.code_desc intent_color,tc20.code_id intent_color_id,\n" );
		sql.append("tc21.code_desc buy_type,tc21.code_id buy_type_id,\n" );
		sql.append("tc22.code_desc buyBudget,tc22.code_id buyBudget_id,\n" );
		sql.append("tc23.code_desc buy_way, tc13.code_id buy_way_id,\n" );
		sql.append("tc24.code_desc ctm_rank,tc23.code_id ctm_rank_id,\n" );
		sql.append("tc25.code_desc come_reason,tc25.code_id come_reason_id,\n" );
		sql.append("tc26.code_desc jcway,tc26.code_id jcway_id,\n" );
		sql.append("tc27.code_desc if_driving,tc27.code_id if_driving_id,\n" );
		sql.append("tc28.code_desc degree,tc28.code_id degree_id\n" );
		sql.append("  from t_pc_customer tpc, tc_code       tc1,\n" );
		sql.append("       tc_code       tc2,tc_code       tc3,\n" );
		sql.append("       tc_code       tc4,tc_code       tc5,\n" );
		sql.append("       tc_code       tc6,tc_code       tc7,\n" );
		sql.append("       tc_code       tc8,tc_code       tc9,\n" );
		sql.append("       tc_code       tc10, tc_code       tc11,\n" );
		sql.append("        tc_code       tc12, tc_code       tc13,\n" );
		sql.append("       tc_code       tc14,tc_code       tc15,\n" );
		sql.append("       tc_code       tc16, tc_code       tc17,\n" );
		sql.append("       tc_code       tc18,tc_code       tc19,\n" );
		sql.append("       tc_code       tc20,tc_code       tc21,\n" );
		sql.append("       tc_code       tc22, tc_code       tc23,\n" );
		sql.append("       tc_code       tc24 ,tc_code       tc25,\n" );
		sql.append("       tc_code       tc26,  tc_code       tc27,\n" );
		sql.append("       tc_code       tc28\n" );
		sql.append("       where tpc.paper_type=tc1.code_id(+)\n" );
		sql.append("      and tpc.car_frum=tc2.code_id(+)\n" );
		sql.append("       and tpc.defeat_reason=tc4.code_id(+)\n" );
		sql.append("        and tpc.defeat_type=tc3.code_id(+)\n" );
		sql.append("         and tpc.fit_area=tc5.code_id(+)\n" );
		sql.append("         and tpc.fit_time=tc6.code_id(+)\n" );
		sql.append("         and tpc.concern=tc7.code_id(+)\n" );
		sql.append("       and tpc.buy_use=tc8.code_id(+)\n" );
		sql.append("        and tpc.color_like=tc9.code_id(+)\n" );
		sql.append("         and tpc.if_marry=tc10.code_id(+)\n" );
		sql.append("         and tpc.political=tc11.code_id(+)\n" );
		sql.append("         and tpc.gender=tc12.code_id(+)\n" );
		sql.append("       and tpc.income=tc13.code_id(+)\n" );
		sql.append("        and tpc.industry=tc14.code_id(+)\n" );
		sql.append("         and tpc.job=tc15.code_id(+)\n" );
		sql.append("         and tpc.interest_one=tc16.code_id(+)\n" );
		sql.append("         and tpc.interest_two=tc17.code_id(+)\n" );
		sql.append("       and tpc.interest_three=tc18.code_id(+)\n" );
		sql.append("        and tpc.ctm_prop=tc19.code_id(+)\n" );
		sql.append("         and tpc.intentcolor=tc20.code_id(+)\n" );
		sql.append("         and tpc.buy_type=tc21.code_id(+)\n" );
		sql.append("         and tpc.buy_budget=tc22.code_id(+)\n" );
		sql.append("       and tpc.buy_way=tc23.code_id(+)\n" );
		sql.append("        and tpc.ctm_rank=tc24.code_id(+)\n" );
		sql.append("         and tpc.come_reason=tc25.code_id(+)\n" );
		sql.append("         and tpc.jc_way=tc26.code_id(+)\n" );
		sql.append("         and tpc.if_drive=tc27.code_id(+)\n" );
		sql.append("         and tpc.degree=tc28.code_id(+)\n" );
		sql.append("     and tpc.customer_id="+ctmId);
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLeadsList(Map<String, String> map) {
		String ctmId=map.get("ctmId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tc1.code_desc jc_way,\n" );
		sql.append("       tc.code_desc LEADS_ORIGIN,\n" );
		sql.append("       to_char(tpl.collect_date, 'yyyy-mm-dd') collect_date\n" );
		sql.append("  from t_pc_customer tpc, t_pc_leads tpl, tc_code tc, tc_code tc1\n" );
		sql.append(" where tpc.customer_id = tpl.customer_id\n" );
		sql.append("   and tc.code_id(+) = tpl.leads_origin\n" );
		sql.append("   and tc1.code_id(+) = tpl.jc_way\n" );
		sql.append("     and tpc.customer_id="+ctmId); 
	    return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
}
