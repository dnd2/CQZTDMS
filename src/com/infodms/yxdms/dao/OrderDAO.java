package com.infodms.yxdms.dao;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAccessoryDtlPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.yxdms.constant.MyConstant;
import com.infodms.yxdms.entity.order.TtAsRoOutDataPO;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class OrderDAO extends IBaseDao{

	private static final OrderDAO dao = new OrderDAO();
	public static final OrderDAO getInstance(){
		if (dao == null) {
			return new OrderDAO();
		}
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public String checkActivityByVin(String vin) {
		StringBuffer res=new StringBuffer();
		StringBuffer sb= new StringBuffer();
		sb.append("select v.vin, a.activity_code, a.activity_name\n" );
		sb.append("  from TT_AS_ACTIVITY_VEHICLE v, TT_AS_ACTIVITY a\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and a.activity_id = v.activity_id\n" );
		sb.append("   and a.activity_type = 10561005\n" );
		sb.append("   and v.vin = '"+vin+"'\n" );
		sb.append("   and a.activity_code not in\n" );
		sb.append("       (select decode(o.cam_code,'','0',o.cam_code)\n" );
		sb.append("          from Tt_As_Repair_Order o\n" );
		sb.append("         where 1=1 --and o.repair_type_code = 11441005\n" );
		sb.append("           and o.vin = '"+vin+"') ");

		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		if(DaoFactory.checkListNull(list)){
			res.append("提示：");
			for (Map<String, Object> map : list) {
				String vinc = map.get("VIN").toString();
				String activityName = map.get("ACTIVITY_NAME").toString();
				res.append(" [该"+vinc+"没有做"+activityName+"] ");
			}
			res.append(" 请先做切换件服务活动！");
		}
		return res.toString();
	}

	@SuppressWarnings("unchecked")
	public String checkActivity(String vin, String partCode) {
		StringBuffer sb= new StringBuffer();
		sb.append("select r.part_code,r.part_name,a.activity_code,a.activity_name,v.vin\n" );
		sb.append("  from Tt_As_Wr_Partsitem_Raplce r, TT_AS_ACTIVITY a,TT_AS_ACTIVITY_VEHICLE v\n" );
		sb.append(" where r.id = a.activity_id\n" );
		sb.append(" and a.activity_id = v.activity_id\n" );
		sb.append("   and a.activity_type = 10561005\n" );
		sb.append("   and v.vin = '"+vin+"'\n" );
		sb.append("   and r.part_code='"+partCode+"'\n" );
		sb.append("   and a.activity_code not in\n" );
		sb.append("      (select o.cam_code\n" );
		sb.append("         from Tt_As_Repair_Order o\n" );
		sb.append("        where o.repair_type_code = 11441005\n" );
		sb.append("          and o.vin = '"+vin+"')");
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		StringBuffer res=new StringBuffer();
		if(DaoFactory.checkListNull(list)){
			res.append(" 该车辆有未完成的服务活动，请做服务活动工单 ,提示： ");
			for (Map<String, Object> map : list) {
				String vinc = map.get("VIN").toString();
				String partName = map.get("PART_NAME").toString();
				String activityName = map.get("ACTIVITY_NAME").toString();
				res.append(" [该"+vinc+"下选择的配件："+partName+"和切换件活动："+activityName+"有关联] ");
			}
		}
		return res.toString();
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> orderList(AclUserBean loginUser, RequestWrapper request,Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select o.id,o.ro_no,\n" );
		sb.append("       o.balance_yieldly,\n" );
		sb.append("       o.license,\n" );
		sb.append("       o.vin,\n" );
		sb.append("       o.model_name,\n" );
		sb.append("       o.owner_name,\n" );
		sb.append("       to_char(o.ro_create_date,'yyyy-mm-dd') as ro_create_date,\n" );
		sb.append("       o.in_mileage,\n" );
		sb.append("       o.free_times,\n" );
		sb.append("       o.ro_status,\n" );
		sb.append("       o.is_warning,\n" );
		sb.append("       o.balance_amount,(select count(1) from Tt_As_Wr_Application a where a.ro_no in (o.ro_no)) as is_app \n" );
		sb.append("  from Tt_As_Repair_Order o\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and o.order_valuable_type = 13591001\n" );
		sb.append("   and o.dealer_id ="+loginUser.getDealerId());
		DaoFactory.getsql(sb, "o.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
		DaoFactory.getsql(sb, "o.ro_no", DaoFactory.getParam(request, "ro_no"), 2);
		DaoFactory.getsql(sb, "o.vin", DaoFactory.getParam(request, "vin"), 2);
		DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "beginTime"), 3);
		DaoFactory.getsql(sb, "o.ro_create_date", DaoFactory.getParam(request, "endTime"), 4);
		DaoFactory.getsql(sb, "o.ro_status", DaoFactory.getParam(request, "ro_status"), 1);
		DaoFactory.getsql(sb, "o.is_warning", DaoFactory.getParam(request, "is_warning"), 1);
		DaoFactory.getsql(sb, "o.balance_yieldly", DaoFactory.getParam(request, "balance_yieldly"), 1);
		DaoFactory.getsql(sb, "o.license", DaoFactory.getParam(request, "license"), 2);
		String ro_type = DaoFactory.getParam(request, "ro_type");
		//该段代码为屏蔽服务站不看到的工单
		sb.append(" and o.ro_no not in (select v.ro_no from tt_as_wr_repair_claim v where v.dealer_id='"+loginUser.getDealerId()+"')");
		if(!"".equals(ro_type)){
			sb.append("and o.id in (select p.ro_id from tt_as_ro_repair_part p where p.repairtypecode='"+ro_type+"')");
		}
		sb.append("   order by o.create_date desc \n" );
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> chooseRoType(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select tc.code_id,tc.code_desc from  tc_code tc where tc.type='9333' " );
		DaoFactory.getsql(sb, "tc.code_id", DaoFactory.getParam(request, "ro_type"), 8);
		sb.append("order by tc.code_id  " );
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, this.getFunName());
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> addPart(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select distinct A.PART_code as pars,\n" );
		sb.append("       a.*,\n" );
		sb.append("       case\n" );
		sb.append("         when A.IS_DEL = 0 and s.is_del = 0 AND S.PART_CODE IS NOT NULL THEN\n" );
		sb.append("          10041001\n" );
		sb.append("         ELSe\n" );
		sb.append("          10041002\n" );
		sb.append("       end fore,\n" );
		sb.append("       case\n" );
		sb.append("         when A.IS_DEL = 0 and s.is_del = 0 AND S.PART_CODE IS NOT NULL THEN\n" );
		sb.append("          s.approval_level\n" );
		sb.append("         ELSe\n" );
		sb.append("          null\n" );
		sb.append("       end part_Level,\n" );
		sb.append("       CASE\n" );
		sb.append("         WHEN (trunc(sysdate) >= sp.valid_date and sp.history_flag = 1) or\n" );
		sb.append("              sp.history_flag = 0 THEN\n" );
		sb.append("          round(nvl(sp.sale_price1, 0) *\n" );
		sb.append("                (1 + nvl(m.parameter_value, 0) / 100),\n" );
		sb.append("                2)\n" );
		sb.append("         ELSE\n" );
		sb.append("          round(nvl(decode(sp.history_price,\n" );
		sb.append("                           null,\n" );
		sb.append("                           sp.sale_price1,\n" );
		sb.append("                           sp.history_price),\n" );
		sb.append("                    0) * (1 + nvl(m.parameter_value, 0) / 100),\n" );
		sb.append("                2)\n" );
		sb.append("       END claim_Price_param\n" );
		sb.append("  from TM_PT_PART_BASE a\n" );
		sb.append("  left join tt_part_sales_price sp\n" );
		sb.append("    on sp.part_id = a.part_id\n" );
		sb.append("  LEFT JOIN TT_AS_WR_AUTHMONITORPART s\n" );
		sb.append("    ON s.part_code = a.part_code\n" );
		sb.append("   and a.is_del = 0, tm_down_parameter m, TT_AS_RELATION_PART_SERIES ps\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and m.parameter_code = 10421009\n" );
		sb.append("   AND A.Part_Is_Changhe = '95411001'\n" );
		sb.append("   and ps.part_id = a.part_id\n" );
		DaoFactory.getsql(sb, "m.dealer_id", DaoFactory.getParam(request, "dealer_id"), 1);
		DaoFactory.getsql(sb, "ps.series_id", DaoFactory.getParam(request, "series_id"), 1);
		DaoFactory.getsql(sb, "ps.model_id", DaoFactory.getParam(request, "model_id"), 1);
		DaoFactory.getsql(sb, "a.part_code", DaoFactory.getParam(request, "part_code"), 2);
		DaoFactory.getsql(sb, "a.part_name", DaoFactory.getParam(request, "part_name"), 2);
		DaoFactory.getsql(sb, "a.IS_SPJJ", DaoFactory.getParam(request, "IS_SPJJ"), 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> addLabour(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		sb.append("select c.* from (select g.WRGROUP_NAME AS wrgroup_name,\n" );
		sb.append("       t.*,\n" );
		sb.append("       t.labour_quotiety * t.labour_hour as labour_fix,\n" );
		sb.append("       case\n" );
		sb.append("         WHEN b.ID IS NOT NULL THEN\n" );
		sb.append("          10041001\n" );
		sb.append("         else\n" );
		sb.append("          10041002\n" );
		sb.append("       end fore,\n" );
		sb.append("       (case\n" );
		sb.append("         when instr(t.labour_code, '9999') = 1 then\n" );
		sb.append("          1\n" );
		sb.append("         else\n" );
		sb.append("          0\n" );
		sb.append("       end) as is_spec,\n" );
		sb.append("       nvl((SELECT max(pr.labour_price) as labour_price\n" );
		sb.append("             from tt_as_wr_labour_price pr\n" );
		sb.append("             left outer join tm_dealer td\n" );
		sb.append("               on td.dealer_id = pr.dealer_id\n" );
		sb.append("            where 1 = 1\n" );
		sb.append("              AND pr.DEALER_ID = "+DaoFactory.getParam(request, "dealer_id")+"\n" );
		/*sb.append("              and pr.mode_type =\n" );
		sb.append("                  (SELECT WRGROUP_CODE\n" );
		sb.append("                     FROM tt_as_wr_model_group\n" );
		sb.append("                    WHERE WRGROUP_ID in\n" );
		sb.append("                          (SELECT WRGROUP_ID\n" );
		sb.append("                             FROM tt_as_wr_model_item\n" );
		sb.append("                            WHERE MODEL_ID = "+DaoFactory.getParam(request, "package_id")+") and wrgroup_type = 10451001)\n" );*/
		sb.append("                      ),0) AS parameter_value,\n" );
		sb.append("       b.approval_level\n" );
		sb.append("  from TT_AS_WR_WRLABINFO t\n" );
		sb.append("  LEFT OUTER JOIN TT_AS_WR_MODEL_GROUP G\n" );
		sb.append("    ON T.WRGROUP_ID = G.WRGROUP_ID\n" );
		sb.append("  left join tt_as_wr_authmonitorlab b\n" );
		sb.append("    on b.labour_operation_no = t.labour_code\n" );
		sb.append("   and b.model_group = t.wrgroup_id\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and t.IS_DEL = 0\n" );
		sb.append("   and T.TREE_CODE = '3'\n" );
		DaoFactory.getsql(sb, "g.wrgroup_id", DaoFactory.getParam(request, "wrgroup_id"), 1);
		sb.append(" order by g.wrgroup_code desc, t.labour_code) c where 1=1 ");

		//==========================
		//sb.append(" order by g.wrgroup_code desc, t.labour_code) c , tt_as_lab_part_relation t where t.lab_code = c.labour_code\n");
		//DaoFactory.getsql(sb, "t.part_id", DaoFactory.getParam(request, "part_id_1"), 6);
		//DaoFactory.getsql(sb, "t.part_id", DaoFactory.getParam(request, "part_id_3"), 6); //暂时去掉了配件与工时的关系
		//==========================
		DaoFactory.getsql(sb, "c.labour_code", DaoFactory.getParam(request, "labour_code"), 2);
		DaoFactory.getsql(sb, "c.cn_des", DaoFactory.getParam(request, "cn_des"), 2);
		DaoFactory.getsql(sb, "c.labour_code", DaoFactory.getParam(request, "labour_codes"), 8);
		//DaoFactory.getsql(sb, "c.labour_code", DaoFactory.getParam(request, "labour_codes_1"), 8);
		//DaoFactory.getsql(sb, "c.labour_code", DaoFactory.getParam(request, "labour_codes_3"), 8);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> showInfoByVin(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select v.*,a.invoice_date,a.sales_date,\n" );
		sb.append("(select i.wrgroup_id\n" );
		sb.append("          from tt_as_wr_MODEL_ITEM i\n" );
		sb.append("         where i.model_id = v.PACKAGE_ID and i.wrgroup_id in\n" );
		sb.append("               (select WRGROUP_ID\n" );
		sb.append("                  from tt_as_wr_model_group\n" );
		//sb.append("                 where wrgroup_type = 10451001)) as wrgroup_id,");
		sb.append("                 )) as wrgroup_id,");
		sb.append("       to_char(vi.create_date, 'yyyy-mm-dd hh24:mi') in_store_date,\n" );
		sb.append("       c.ctm_name as customer_name,\n" );
		sb.append("       wu.rule_code,\n" );
		sb.append("       a.order_id,\n" );
		sb.append("       c.main_phone,\n" );
		sb.append("       a.car_charactor car_use_type,\n" );
		sb.append("       tc.code_desc car_use_desc,\n" );
		sb.append("       c.ctm_name,\n" );
		sb.append("       c.other_phone,\n" );
		sb.append("       c.address,\n" );
		sb.append("       vw.brand_name as brand_name,\n" );
		sb.append("       vw.brand_code as brand_code,\n" );
		sb.append("       vw.series_name as series_name,\n" );
		sb.append("       vw.series_code as series_code,\n" );
		sb.append("       vw.model_name as model_name,\n" );
		sb.append("       vw.model_code as model_code,\n" );
		sb.append("       a.consignation_date as purchased_date_act,\n" );
		sb.append("       vw.package_name,\n" );
		sb.append("       ba.area_name yieldly_name\n" );
		sb.append("  from tm_vehicle v\n" );
		sb.append("  left outer join vw_material_group_service vw\n" );
		sb.append("    on vw.package_id = v.package_id\n" );
		sb.append("  left outer join tt_dealer_actual_sales a\n" );
		sb.append("    on v.vehicle_id = a.vehicle_id\n" );
		sb.append("   and a.is_return = 10041002\n" );
		sb.append("  left outer join tt_customer c\n" );
		sb.append("    on a.ctm_id = c.ctm_id\n" );
		sb.append("  left join tm_business_area ba\n" );
		sb.append("    on ba.area_id = v.yieldly\n" );
		sb.append("  left join tc_code tc\n" );
		sb.append("    on tc.code_id = a.car_charactor\n" );
		sb.append("  left join tt_as_wr_game wg\n" );
		sb.append("    on wg.id = v.claim_tactics_id\n" );
		sb.append("  left join tt_as_wr_rule wu\n" );
		sb.append("    on wu.id = wg.rule_id\n" );
		sb.append("  left join (select max(vi.create_date) create_date, vi.vehicle_id\n" );
		sb.append("               from tt_vs_inspection vi\n" );
		sb.append("              group by vi.vehicle_id) vi\n" );
		sb.append("    on vi.vehicle_id = v.vehicle_id\n" );
		sb.append("   and v.life_cycle in (10321003, 10321004, 10321007)\n" );
		sb.append(" where 1 = 1\n" );
		DaoFactory.getsql(sb, "v.vin", DaoFactory.getParam(request, "vin"), 1);
		Map<String, Object> ps = pageQueryMap(sb.toString(), null, getFunName());
		return ps;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findLoginUserInfo(Long userId) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,tm.dealer_code,tm.dealer_shortname from tc_user t,tm_dealer tm where tm.dealer_id=t.dealer_id ");
		DaoFactory.getsql(sb, "t.user_id", userId.toString(), 1);
		Map<String, Object> ps = pageQueryMap(sb.toString(), null, getFunName());
		return ps;
	}

	@SuppressWarnings("unchecked")
	public int roInsertSure(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		try {
			//配件的 （三种类型）
			String[] part_id_1 = DaoFactory.getParams(request, "part_id_1");
			String[] part_id_2 = DaoFactory.getParams(request, "part_id_2");
			String[] part_id_3 = DaoFactory.getParams(request, "part_id_3");
			String[] part_code_1 = DaoFactory.getParams(request, "part_code_1");
			String[] part_code_2 = DaoFactory.getParams(request, "part_code_2");
			String[] part_code_3 = DaoFactory.getParams(request, "part_code_3");
			String[] part_name_1 = DaoFactory.getParams(request, "part_name_1");
			String[] part_name_2 = DaoFactory.getParams(request, "part_name_2");
			String[] part_name_3 = DaoFactory.getParams(request, "part_name_3");
			String[] part_quotiety_1 = DaoFactory.getParams(request, "part_quotiety_1");
			String[] part_quotiety_2 = DaoFactory.getParams(request, "part_quotiety_2");
			String[] part_quotiety_3 = DaoFactory.getParams(request, "part_quotiety_3");
			String[] claim_price_param_1 = DaoFactory.getParams(request, "claim_price_param_1");
			String[] claim_price_param_2 = DaoFactory.getParams(request, "claim_price_param_2");
			String[] claim_price_param_3 = DaoFactory.getParams(request, "claim_price_param_3");
			String[] part_amont_1 = DaoFactory.getParams(request, "part_amont_1");
			String[] part_amont_2 = DaoFactory.getParams(request, "part_amont_2");
			String[] part_amont_3 = DaoFactory.getParams(request, "part_amont_3");
			String[] pay_type_1 = DaoFactory.getParams(request, "pay_type_1");
			String[] pay_type_2 = DaoFactory.getParams(request, "pay_type_2");
			String[] pay_type_3 = DaoFactory.getParams(request, "pay_type_3");
			String[] part_use_type_1 = DaoFactory.getParams(request, "part_use_type_1");
			String[] part_use_type_2 = DaoFactory.getParams(request, "part_use_type_2");
			String[] part_use_type_3 = DaoFactory.getParams(request, "part_use_type_3");
			String[] is_gua_1 = DaoFactory.getParams(request, "is_gua_1");
			String[] part_camcode_1 = DaoFactory.getParams(request, "part_camcode");
			//工时的(三种类型)
			String[] labour_code_1 = DaoFactory.getParams(request, "labour_code_1");
			String[] labour_code_2 = DaoFactory.getParams(request, "labour_code_2");
			String[] labour_code_3 = DaoFactory.getParams(request, "labour_code_3");
			String[] cn_des_1 = DaoFactory.getParams(request, "cn_des_1");
			String[] cn_des_2 = DaoFactory.getParams(request, "cn_des_2");
			String[] cn_des_3 = DaoFactory.getParams(request, "cn_des_3");
			String[] labour_quotiety_1 = DaoFactory.getParams(request, "labour_quotiety_1");
			String[] labour_quotiety_2 = DaoFactory.getParams(request, "labour_quotiety_2");
			String[] labour_quotiety_3 = DaoFactory.getParams(request, "labour_quotiety_3");
			String[] parameter_value_1 = DaoFactory.getParams(request, "parameter_value_1");
			String[] parameter_value_2 = DaoFactory.getParams(request, "parameter_value_2");
			String[] parameter_value_3 = DaoFactory.getParams(request, "parameter_value_3");
			String[] labour_fix_1 = DaoFactory.getParams(request, "labour_fix_1");
			String[] labour_fix_2 = DaoFactory.getParams(request, "labour_fix_2");
			String[] labour_fix_3 = DaoFactory.getParams(request, "labour_fix_3");
			String[] pay_type_labour_1 = DaoFactory.getParams(request, "pay_type_labour_1");
			String[] pay_type_labour_2 = DaoFactory.getParams(request, "pay_type_labour_2");
			String[] pay_type_labour_3 = DaoFactory.getParams(request, "pay_type_labour_3");
			String[] lab_camcode_1 = DaoFactory.getParams(request, "lab_camcode");

			//辅料
			String[] workHourCode = DaoFactory.getParams(request, "workHourCode");
			String[] workhour_name = DaoFactory.getParams(request, "workhour_name");
			String[] accessoriesPrice = DaoFactory.getParams(request, "accessoriesPrice");
			String[] accessoriesOutMainPart = DaoFactory.getParams(request, "accessoriesOutMainPart");
			
			//外出服务
			String[] address = DaoFactory.getParams(request, "address");
			String[] out_mileage = DaoFactory.getParams(request, "out_mileage");
			String[] pay_type = DaoFactory.getParams(request, "pay_type");
			String[] out_amount = DaoFactory.getParams(request, "out_amount");

			//工单信息
			String license_no = DaoFactory.getParam(request, "license_no");
			String brand_name = DaoFactory.getParam(request, "brand_name");
			String model_id = DaoFactory.getParam(request, "model_id");
			String model = DaoFactory.getParam(request, "model");
			String model_name = DaoFactory.getParam(request, "model_name");
			String package_id = DaoFactory.getParam(request, "package_id");
			String wrgroup_id = DaoFactory.getParam(request, "wrgroup_id");
			String series_id = DaoFactory.getParam(request, "series_id");
			String ro_no = DaoFactory.getParam(request, "ro_no");
			String car_use_type = DaoFactory.getParam(request, "car_use_type");
			String dealer_code = DaoFactory.getParam(request, "dealer_code");
			String dealer_shortname = DaoFactory.getParam(request, "dealer_shortname");
			String vin = DaoFactory.getParam(request, "vin");
			String package_name = DaoFactory.getParam(request, "package_name");
			String in_mileage = DaoFactory.getParam(request, "in_mileage");
			String color = DaoFactory.getParam(request, "color");
			String engine_no = DaoFactory.getParam(request, "engine_no");
			String car_use_desc = DaoFactory.getParam(request, "car_use_desc");
			//String warning = DaoFactory.getParam(request, "warning");
			String guarantee_date = DaoFactory.getParam(request, "guarantee_date");
			String free_times = DaoFactory.getParam(request, "free_times");
			String ctm_name = DaoFactory.getParam(request, "ctm_name");
			String main_phone = DaoFactory.getParam(request, "main_phone");
			String deliverer_adress = DaoFactory.getParam(request, "deliverer_adress");
			String deliverer = DaoFactory.getParam(request, "deliverer");
			String deliverer_phone = DaoFactory.getParam(request, "deliverer_phone");
			String repairText = DaoFactory.getParam(request, "repairText");
			String camCode = DaoFactory.getParam(request, "cam_code");
			String keep_fit_no = DaoFactory.getParam(request, "keep_fit_no");
			String discount_amount = DaoFactory.getParam(request, "discount_amount");
			String is_visit = DaoFactory.getParam(request, "is_visit");
			
			String id = DaoFactory.getParam(request, "id");
			Long userId = loginUser.getUserId();
			Double mileage = Double.valueOf(in_mileage);
			Date dateTemp = new Date();
			if(!dealer_code.equals(loginUser.getDealerCode())){
				throw new RuntimeException("提示:请刷新页面或重新登录再试！");
			}
			if(!"".equals(id)){//修改
				this.delete("delete from Tt_As_Ro_Labour l where l.ro_id="+DaoFactory.getParam(request, "id"), null);
				this.delete("delete from tt_as_ro_repair_part p where p.ro_id="+DaoFactory.getParam(request, "id"), null);
				this.delete("delete from tt_accessory_dtl d where d.ro_no='"+DaoFactory.getParam(request, "ro_no")+"'", null);
				this.delete("delete from Tt_As_Ro_Out_Data d where d.ro_no='"+DaoFactory.getParam(request, "ro_no")+"'", null);
				TtAsRepairOrderPO t1=new TtAsRepairOrderPO();
				Long roId = BaseUtils.ConvertLong(id);
				t1.setId(roId);
				TtAsRepairOrderPO t2=new TtAsRepairOrderPO();
				t2.setInMileage(mileage);//里程
				t2.setDeliverer(deliverer);//送修人
				t2.setLicense(license_no);//车牌号
				t2.setDelivererPhone(deliverer_phone);//送修人电话
				t2.setRemarks(repairText);//内容
				t2.setUpdateBy(userId);
				t2.setUpdateDate(dateTemp);
				t2.setRoNo(ro_no);
				if(!"请选择".equals(camCode)&& !"".equals(camCode)){
					t2.setCamCode(camCode);
				}
				t2.setKeepFitNo(keep_fit_no);
				if(!"".equals(is_visit)){
					t2.setIsVisit( Integer.parseInt(is_visit));
				}
				isnertPartsOrLabours(discount_amount,address,out_mileage,pay_type,out_amount,part_camcode_1,lab_camcode_1,is_gua_1,vin,part_id_1, part_id_2, part_id_3,
						part_code_1, part_code_2, part_code_3, part_name_1,
						part_name_2, part_name_3, part_quotiety_1,
						part_quotiety_2, part_quotiety_3, claim_price_param_1,
						claim_price_param_2, claim_price_param_3, part_amont_1,
						part_amont_2, part_amont_3,
						pay_type_1,pay_type_2,pay_type_3,
						part_use_type_1,part_use_type_2,part_use_type_3,
						labour_code_1,labour_code_2, labour_code_3, cn_des_1, cn_des_2,
						cn_des_3, labour_quotiety_1, labour_quotiety_2,
						labour_quotiety_3, parameter_value_1,
						parameter_value_2, parameter_value_3, labour_fix_1,
						labour_fix_2, labour_fix_3, 
						pay_type_labour_1,pay_type_labour_2,pay_type_labour_3,
						userId, dateTemp, roId, t2,
						workHourCode,workhour_name,
						accessoriesPrice,accessoriesOutMainPart
						);
				this.update(t1, t2);
			}else{
				Long roId = DaoFactory.getPkId();
				if(!dealer_code.equals(loginUser.getDealerCode())){
					throw new RuntimeException("提示:请刷新页面或重新登录再试！");
				}
				ro_no = Utility.GetBillNo("", dealer_code,"RO");//工单号无类型，取一般的
				TtAsRepairOrderPO t=new TtAsRepairOrderPO();
				// 将行驶里程插入tm_vehicle
				List<TtAsRepairOrderPO> checkList = this.select(TtAsRepairOrderPO.class, "select t.* from Tt_As_Repair_Order t where t.ro_no='"+ro_no+"'", null);
				if (checkList!=null && checkList.size()==0) {
					this.update("update tm_vehicle t set t.Mileage='"+mileage+"',t.Update_By='"+userId+"',t.License_No='"+license_no+"',t.Update_Date=sysdate where t.vin='"+vin+"'",null);
				}
				
				t.setCreateBy(userId);
				t.setId(roId);
				t.setRoNo(ro_no);
				t.setRemarks(repairText);//内容
				if(!"请选择".equals(camCode)&& !"".equals(camCode)){
					t.setCamCode(camCode);
				}
				t.setKeepFitNo(keep_fit_no);
				t.setCarUseType(BaseUtils.ConvertInt(car_use_type));
				t.setDealerShortname(dealer_shortname);
				t.setDealerCode(dealer_code);
				t.setDealerId(BaseUtils.ConvertLong(loginUser.getDealerId()));
				t.setModel(model);//车型
				t.setRoModelId(model_id);//车型ID
				t.setModelName(model_name);//车型name
				t.setEngineNo(engine_no);//发动机
				t.setVin(vin);
				t.setRoCreateDate(dateTemp);
				t.setCreateDate(dateTemp);
				t.setDeliverer(deliverer);//送修人
				t.setDelivererPhone(deliverer_phone);//送修人电话
				t.setGuaranteeDate(BaseUtils.getDate(guarantee_date, 1));//购车日期
				t.setInMileage(mileage);//里程
				t.setColor(color);//颜色
				t.setOwnerName(ctm_name);//车主
				t.setLicense(license_no);//车牌号
				t.setDelivererAdress(deliverer_adress);//车主地址
				t.setOwnerPhone(main_phone);//车主电话
				t.setPackageName(package_name);//配置
				Integer free_time=0;
				if(!"".equals(free_times)){
					free_time=Integer.valueOf(free_times);
				}
				t.setFreeTimes(free_time);//保养次数
				t.setCarUseDesc(car_use_desc);
				t.setBrand(brand_name);//品牌
				t.setSeries(series_id);//车系
				t.setRoPackageId(package_id);
				t.setWrgroupId(wrgroup_id);
				t.setWarningLevel("无");
				List<Map<String, Object>> vrLevel = this.getVrLevel(vin);
				if(DaoFactory.checkListNull(vrLevel)){
					t.setWarningLevel(vrLevel.get(0).get("VR_LEVEL").toString());//预警等级
					t.setIsWarning(10041001);//是预警
				}else{
					t.setIsWarning(10041002);//否预警
				}
				t.setRoStatus(11591001);
				if(!"".equals(is_visit)){
					t.setIsVisit( Integer.parseInt(is_visit));
				}
				
				isnertPartsOrLabours(discount_amount,address,out_mileage,pay_type,out_amount,part_camcode_1,lab_camcode_1,is_gua_1,vin,part_id_1, part_id_2, part_id_3,
						part_code_1, part_code_2, part_code_3, part_name_1,
						part_name_2, part_name_3, part_quotiety_1,
						part_quotiety_2, part_quotiety_3, claim_price_param_1,
						claim_price_param_2, claim_price_param_3, part_amont_1,
						part_amont_2, part_amont_3,
						pay_type_1,pay_type_2,pay_type_3,
						part_use_type_1,part_use_type_2,part_use_type_3,
						labour_code_1,labour_code_2, labour_code_3, cn_des_1, cn_des_2,
						cn_des_3, labour_quotiety_1, labour_quotiety_2,
						labour_quotiety_3, parameter_value_1,
						parameter_value_2, parameter_value_3, labour_fix_1,
						labour_fix_2, labour_fix_3, 
						pay_type_labour_1,pay_type_labour_2,pay_type_labour_3,
						userId, dateTemp, roId, t,
						workHourCode,workhour_name,
						accessoriesPrice,accessoriesOutMainPart
						);
				this.insert(t);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}
	/**
	 * 取到三包等级和是否预警
	 * @param vin
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getVrLevel(String vin) {
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT (select c.code_desc from tc_code c where c.code_id=t.vr_level) as vr_level from  TT_AS_WR_VIN_RULE t where t.VR_WARRANTY <=\n" );
		sb.append("(select d.cur_days from   Tt_As_Wr_Vin_Repair_Days d where d.vin='"+vin+"' )\n" );
		sb.append("and t.VR_TYPE =94021001 order by t.VR_WARRANTY desc");
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, this.getFunName());
		return list;
	}

	/**
	 * 插入工时和配件
	 * @param is_gua_1 
	 * @param lab_camcode_1 
	 */
	@SuppressWarnings("unchecked") 
	private void isnertPartsOrLabours(String discount_amount,String[] address,String[] out_mileage,String[] pay_type,String[] out_amount,String[] part_camcode_1,String[] lab_camcode_1,String[] is_gua_1, String vin,String[] part_id_1, String[] part_id_2,
			String[] part_id_3, String[] part_code_1, String[] part_code_2,
			String[] part_code_3, String[] part_name_1, String[] part_name_2,
			String[] part_name_3, String[] part_quotiety_1,
			String[] part_quotiety_2, String[] part_quotiety_3,
			String[] claim_price_param_1, String[] claim_price_param_2,
			String[] claim_price_param_3, String[] part_amont_1,
			String[] part_amont_2, String[] part_amont_3,
			String[]pay_type_1,String[]pay_type_2,String[]pay_type_3,
			String[]part_use_type_1,String[]part_use_type_2,String[]part_use_type_3,
			String[] labour_code_1, String[] labour_code_2,
			String[] labour_code_3, String[] cn_des_1, String[] cn_des_2,
			String[] cn_des_3, String[] labour_quotiety_1,
			String[] labour_quotiety_2, String[] labour_quotiety_3,
			String[] parameter_value_1, String[] parameter_value_2,
			String[] parameter_value_3, String[] labour_fix_1,
			String[] labour_fix_2, String[] labour_fix_3, 
			String[] pay_type_labour_1,String[] pay_type_labour_2,String[] pay_type_labour_3,
			Long userId,
			Date dateTemp, Long roId, TtAsRepairOrderPO t,
			String[] workHourCode,String[] workhour_name,
			String[] accessoriesPrice,String[] accessoriesOutMainPart) {
		
		Double claimAmont=0.0d; 
		Double freeAmont=0.0d; 
		try {
			Double labourAmont=0.0d;
			if(labour_code_1!=null && labour_code_1.length>0){
				for (int i = 0; i < labour_code_1.length; i++) {
					TtAsRoLabourPO l=new TtAsRoLabourPO();
					l.setId(DaoFactory.getPkId());
					l.setRoId(roId);
					l.setRepairTypeCode("93331001");
					l.setLabourCode(labour_code_1[i]);
					l.setLabourName(cn_des_1[i]);
					l.setLabourPrice(Float.valueOf(parameter_value_1[i]));
					l.setStdLabourHour(Double.valueOf(labour_quotiety_1[i]));
					l.setCreateBy(userId);
					l.setCreateDate(dateTemp);
					Integer pay_type_labour = Integer.valueOf(pay_type_labour_1[i]);
					l.setPayType(pay_type_labour);
					l.setLabCamcode(lab_camcode_1[i]);
					Double labourAmont_1 = Double.valueOf(labour_fix_1[i]);
					l.setLabourAmount(labourAmont_1);
					labourAmont+=labourAmont_1;
					if(11801002==pay_type_labour){//索赔
						claimAmont+=labourAmont_1;
					}
					if(11801001==pay_type_labour){//自费
						freeAmont+=labourAmont_1;
					}
					this.insert(l);
				}
			}
			if(labour_code_2!=null && labour_code_2.length>0){
				for (int i = 0; i < labour_code_2.length; i++) {
					TtAsRoLabourPO l=new TtAsRoLabourPO();
					l.setId(DaoFactory.getPkId());
					l.setRoId(roId);
					l.setRepairTypeCode("93331002");
					l.setLabourCode(labour_code_2[i]);
					l.setLabourName(cn_des_2[i]);
					l.setLabourPrice(Float.valueOf(parameter_value_2[i]));
					l.setStdLabourHour(Double.valueOf(labour_quotiety_2[i]));
					Integer pay_type_labour = Integer.valueOf(pay_type_labour_2[i]);
					l.setPayType(pay_type_labour);
					Double labourAmont_2 = Double.valueOf(labour_fix_2[i]);
					l.setLabourAmount(labourAmont_2);
					l.setCreateBy(userId);
					l.setCreateDate(dateTemp);
					labourAmont+=labourAmont_2;
					if(11801002==pay_type_labour){//索赔
						claimAmont+=labourAmont_2;
					}
					if(11801001==pay_type_labour){//自费
						freeAmont+=labourAmont_2;
					}
					this.insert(l);
				}
			}
			if(labour_code_3!=null && labour_code_3.length>0){
				for (int i = 0; i < labour_code_3.length; i++) {
					TtAsRoLabourPO l=new TtAsRoLabourPO();
					l.setId(DaoFactory.getPkId());
					l.setRoId(roId);
					l.setRepairTypeCode("93331003");
					l.setLabourCode(labour_code_3[i]);
					l.setLabourName(cn_des_3[i]);
					l.setLabourPrice(Float.valueOf(parameter_value_3[i]));
					l.setStdLabourHour(Double.valueOf(labour_quotiety_3[i]));
					Double labourAmont_3 = Double.valueOf(labour_fix_3[i]);
					Integer pay_type_labour = Integer.valueOf(pay_type_labour_3[i]);
					l.setPayType(pay_type_labour);
					l.setLabourAmount(labourAmont_3);
					l.setCreateBy(userId);
					l.setCreateDate(dateTemp);
					labourAmont+=labourAmont_3;
					if(11801002==pay_type_labour){//索赔
						claimAmont+=labourAmont_3;
					}
					if(11801001==pay_type_labour){//自费
						freeAmont+=labourAmont_3;
					}
					this.insert(l);
				}
			}
			
			Double partAmont=0.0d;
			if(part_code_1!=null && part_code_1.length>0){
				for (int i = 0; i < part_code_1.length; i++) {
					TtAsRoRepairPartPO p=new TtAsRoRepairPartPO();
					p.setId(DaoFactory.getPkId());
					p.setRoId(roId);
					p.setPartNo(part_code_1[i]);
					p.setPartName(part_name_1[i]);
					p.setPartCostPrice(Double.valueOf(claim_price_param_1[i]));
					p.setRealPartId(Long.valueOf(part_id_1[i]));
					p.setRepairtypecode("93331001");
					Integer payType = Integer.valueOf(pay_type_1[i]);
					p.setPayType(payType);
					Integer part_use_type = Integer.valueOf(part_use_type_1[i]);
					p.setPartUseType(part_use_type);
					Double part_Amont_1 =0.0d;
					if(95431002==part_use_type){//更换
						part_Amont_1 = Double.valueOf(part_amont_1[i]);
						p.setPartCostAmount(part_Amont_1);
						p.setPartQuantity(Float.valueOf(part_quotiety_1[i]));
					}else{
						p.setPartCostAmount(part_Amont_1);
						p.setPartQuantity(0.0f);
					}
					p.setIsGua(Integer.valueOf(is_gua_1[i]));
					p.setPartCamcode(part_camcode_1[i]);
					partAmont+=part_Amont_1;
					if(11801002==payType){//索赔
						claimAmont+=part_Amont_1;
					}
					if(11801001==payType){//自费
						freeAmont+=part_Amont_1;
					}
					this.insert(p);
				}
			}
			if(part_code_2!=null && part_code_2.length>0){
				for (int i = 0; i < part_code_2.length; i++) {
					TtAsRoRepairPartPO p=new TtAsRoRepairPartPO();
					p.setId(DaoFactory.getPkId());
					p.setRoId(roId);
					p.setPartNo(part_code_2[i]);
					p.setPartName(part_name_2[i]);
					p.setPartCostPrice(Double.valueOf(claim_price_param_2[i]));
					p.setPartQuantity(Float.valueOf(part_quotiety_2[i]));
					p.setRealPartId(Long.valueOf(part_id_2[i]));
					p.setRepairtypecode("93331002");
					Integer payType = Integer.valueOf(pay_type_2[i]);
					p.setPayType(payType);
					Integer part_use_type = Integer.valueOf(part_use_type_2[i]);
					p.setPartUseType(part_use_type);
					p.setIsGua(0);
					Double part_Amont_2 =0.0d;
					if(95431002==part_use_type){//更换
						part_Amont_2 = Double.valueOf(part_amont_2[i]);
						p.setPartCostAmount(part_Amont_2);
						p.setPartQuantity(Float.valueOf(part_quotiety_2[i]));
					}else{
						p.setPartCostAmount(part_Amont_2);
						p.setPartQuantity(0.0f);
					}
					
					partAmont+=part_Amont_2;
					if(11801002==payType){//索赔
						claimAmont+=part_Amont_2;
					}
					if(11801001==payType){//自费
						freeAmont+=part_Amont_2;
					}
					this.insert(p);
				}
			}
			if(part_code_3!=null && part_code_3.length>0){
				for (int i = 0; i < part_code_3.length; i++) {
					TtAsRoRepairPartPO p=new TtAsRoRepairPartPO();
					p.setId(DaoFactory.getPkId());
					p.setRoId(roId);
					p.setPartNo(part_code_3[i]);
					p.setPartName(part_name_3[i]);
					p.setPartCostPrice(Double.valueOf(claim_price_param_3[i]));
					p.setRealPartId(Long.valueOf(part_id_3[i]));
					p.setRepairtypecode("93331003");
					Integer payType = Integer.valueOf(pay_type_3[i]);
					p.setPayType(payType);
					Integer part_use_type = Integer.valueOf(part_use_type_3[i]);
					p.setPartUseType(part_use_type);
					p.setIsGua(0);
					
					Double part_Amont_3 =0.0d;
					if(95431002==part_use_type){//更换
						part_Amont_3 = Double.valueOf(part_amont_3[i]);
						p.setPartCostAmount(part_Amont_3);
						p.setPartQuantity(Float.valueOf(part_quotiety_3[i]));
					}else{
						p.setPartCostAmount(part_Amont_3);
						p.setPartQuantity(0.0f);
					}
					partAmont+=part_Amont_3;
					if(11801002==payType){//索赔
						claimAmont+=part_Amont_3;
					}
					if(11801001==payType){//自费
						freeAmont+=part_Amont_3;
					}
					this.insert(p);
				}
			}
			
			t.setLabourAmount(labourAmont);
			t.setRepairAmount(partAmont);
			t.setRepairAmount(Arith.add(labourAmont,partAmont));
			double accPrice = 0d;//辅料费总计
			if (workHourCode != null && workHourCode.length>0) {
				for (int i = 0; i < workHourCode.length; i++) {
					TtAccessoryDtlPO accPo=new TtAccessoryDtlPO();
					accPo.setId(DaoFactory.getPkId());
					accPo.setRoNo(t.getRoNo());
					accPo.setWorkhourCode(workHourCode[i]);
					accPo.setWorkhourName(workhour_name[i]);
					Double accprice = Double.valueOf(accessoriesPrice[i]);
					accPo.setPrice(accprice);
					String mainPartCode = accessoriesOutMainPart[i];
					StringBuffer sb= new StringBuffer();
					sb.append("select p.pay_type\n" );
					sb.append("          from tt_as_ro_repair_part p where p.repairtypecode=93331001 and p.part_no='"+mainPartCode+"'  and  p.ro_id="+roId );
					Map<String,Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
					accPo.setMainPartCode(mainPartCode);
					accPo.setId(DaoFactory.getPkId());
					accPrice += accprice;
					String  payType = BaseUtils.checkNull(map.get("PAY_TYPE"));
					if("11801002".equals(payType)){//索赔
						claimAmont+=accprice;
					}
					if("11801001".equals(payType)){//自费
						freeAmont+=accprice;
					}
					this.insert(accPo);
				}
			}
			double outPrice = 0d;//外出总计
			if(address!=null && address.length>0){
				for (int i = 0; i < address.length; i++) {
					TtAsRoOutDataPO to=new TtAsRoOutDataPO();
					to.setAddress(address[i]);
					to.setMileage(out_mileage[i]);
					String payType = pay_type[i];
					to.setPayType(payType);
					to.setRoNo(t.getRoNo());
					to.setOutAmount(out_amount[i]);
					to.setId(DaoFactory.getPkId());
					double outAmount = Double.parseDouble(out_amount[i]);
					outPrice+=outAmount;
					if("索赔".equals(payType)){//索赔
						claimAmont+=outAmount;
					}
					if("自费".equals(payType)){//自费
						freeAmont+=outAmount;
					}
					this.insert(to);
				}
			}
			
			Double allAmont=0.0d;
			allAmont=Arith.add(allAmont,labourAmont);
			allAmont=Arith.add(allAmont,partAmont);
			allAmont=Arith.add(allAmont,accPrice);
			allAmont=Arith.add(allAmont,outPrice);
			t.setAccessoriesPrice(accPrice);
			t.setBalanceAmount(allAmont);
			double parseDouble =0.0d;
			if(!"".equals(discount_amount)){
				 parseDouble = Double.parseDouble(discount_amount);
			}
			t.setDiscountAmount(parseDouble);
			t.setRealAmount(freeAmont-parseDouble);//实收金额=自费金额—折扣金额 
			t.setEstimateAmount(claimAmont);//索赔
			t.setDerateAmount(freeAmont);//自费
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<TtAsWrRuleListPO> getNoFamily(Long id,String partCode){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select  l.*  from TT_AS_WR_RULE_LIST l ,tt_as_wr_rule r,tt_as_wr_game g\n");
    	sql.append("where g.rule_id = r.id\n");
    	sql.append("and l.rule_id = r.id\n");
    	sql.append("  and l.part_code='"+partCode+"'\n");
    	sql.append("and  g.id="+id+"\n"); 
    	return this.select(TtAsWrRuleListPO.class,sql.toString(), null);
	 }
		 
	@SuppressWarnings("unchecked")
	public List<TtAsWrGamePO> isFamily(Long id){
    	StringBuffer sql= new StringBuffer();
    	sql.append("select  * from tt_as_wr_game g where g.id="+id+"\n");
    	sql.append("and g.game_code='"+Constant.VEHICLE_IS_FAMILY+"'"); 
    	return this.select(TtAsWrGamePO.class,sql.toString(), null);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAccById(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("\n" );
		sb.append("select t.*,\n" );
		sb.append("       (select min(p.pay_type)\n" );
		sb.append("          from tt_as_ro_repair_part p\n" );
		sb.append("         where p.part_no = t.main_part_code and p.repairtypecode= 93331001\n" );
		sb.append("           and p.ro_id =\n" );
		sb.append("               (select o.id from Tt_As_Repair_Order o where o.ro_no = t.ro_no)) pay_type\n" );
		sb.append("  from tt_accessory_dtl t where 1=1");
		sb.append(" and t.ro_no=(select o.ro_no from tt_as_repair_order o where o.id ="+DaoFactory.getParam(request, "id")+")");
		return pageQuery(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> accList(RequestWrapper request,Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
        sb.append("select distinct a.workhour_code,a.workhour_name,a.price\n");
        sb.append(" from tt_accessory_price_maintain a\n");
        sb.append(" where 1=1"); 
        DaoFactory.getsql(sb, "a.workhour_code", DaoFactory.getParam(request, "workhour_code"), 2);
        DaoFactory.getsql(sb, "a.workhour_name", DaoFactory.getParam(request, "workhour_name"), 2);
    	return  pageQuery(sb.toString(), null, this.getFunName(),pageSize , currPage);
	}
	
	@SuppressWarnings("unchecked")
	public String roBalanceCheck(RequestWrapper request, AclUserBean loginUser) throws ParseException{
		//配件的 （三种类型）
		//part_code_1 --正常维修
		//part_code_2 --免费保养
		//part_code_3 --自费保养
		
		String vin = DaoFactory.getParam(request, "vin");
		String date = DaoFactory.getParam(request, "BALANCE_DATE");//工单结算时间 第一次结算时设置为当前时间 （taropUp）
		
		String id = DaoFactory.getParam(request, "id");
		StringBuffer res = new StringBuffer();
		//todo 
		//针对保养的工单，只更新状态
		//针对正常维修的工单,结算的时候更新索赔件的三包(售前车不更新),更新单据状态
		//取消结算，正常维修的工单备份,重新结算的时候获取备份数据取消结算,结算的时候重新跑三包 ,更新单据状态
		
		TmVehiclePO tvPO = new TmVehiclePO();
		tvPO.setVin(vin);
		List<PO> list = this.select(tvPO);
		
		TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
		tarop.setId(Long.parseLong(id));
		List<TtAsRepairOrderPO> listData = this.select(tarop);
		
		if(null!=list && list.size()>0){
			//todo 三包规则更新
			TmVehiclePO tempPO = (TmVehiclePO)list.get(0);
			if(tempPO.getLifeCycle()==Constant.VEHICLE_LIFE_04){
				//实销车辆三包规则开始
				for(int i=0;i<listData.size();i++){
					
				}
			}else{
				if(tempPO.getLifeCycle()==Constant.VEHICLE_LIFE_06){
					res.append("该车辆目前是无效状态,工单无法结算,请联系厂家确认!");
				}else{//售前车辆 更新单据状态
					//单据类型
					TtAsRepairOrderPO aopPO = new TtAsRepairOrderPO();
					aopPO.setId(Long.parseLong(id));
					TtAsRepairOrderPO aRoPO = new TtAsRepairOrderPO();
					aRoPO.setRoStatus(Constant.RO_STATUS_02);
					Date forBalanceTime = listData.get(0).getForBalanceTime() ;
					if (forBalanceTime== null || "".equalsIgnoreCase(forBalanceTime.toString())) {
						aRoPO.setForBalanceTime(Utility.parseString2DateTime(date, "yyyy-MM-dd HH:mm"));// 结算时间 第一次
					}else{
						aRoPO.setForBalanceTime(forBalanceTime); //设置 为 第一次结算的时间
					}
					aRoPO.setId(Long.parseLong(id));
					aRoPO.setRoStatus(Constant.RO_STATUS_02); // 以结算
					listData.get(0).setRoStatus(Constant.RO_STATUS_02);
					aRoPO.setVer(listData.get(0).getVer() + 1);// 将版本号加一
					aRoPO.setPrintRoTime(listData.get(0).getCreateDate());
					this.update(aopPO, aRoPO);
				}
			}
		}else{
			res.append("查询不到该车辆的信息,请确认车价后信息是否有误!");
		}
		return res.toString();
	}
	@SuppressWarnings("unchecked")
	public String checkMileage(RequestWrapper request) {
		String res="";
		StringBuffer sb= new StringBuffer();
		sb.append("select v.mileage from tm_vehicle v where v.vin='"+DaoFactory.getParam(request, "vin")+"'");
		List<Map<String, Object>> list = pageQuery(sb.toString(), null, this.getFunName());
		Double mileage=0.0d;
		if(DaoFactory.checkListNull(list)){
			Map<String, Object> map = list.get(0);
			BigDecimal object = (BigDecimal) map.get("MILEAGE");
			if(object==null){
				mileage=0.0d;
			}else{
				mileage=object.doubleValue();
			}
		}
		Double mileageNumTemp=0.0d;
		String mileageNum = DaoFactory.getParam(request, "mileage");
		if(!"".equals(mileageNum)){
			mileageNumTemp=Double.valueOf(mileageNum);
		}else{
			mileageNumTemp=0.0d;
		}
		mileageNumTemp=Double.valueOf(mileageNumTemp);
		if(mileageNumTemp<mileage){
			res="提示：不能填写为空或进厂行驶里程不能小于系统行驶里程";
		}
    	return  res;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> showInfoBylicenseNo(RequestWrapper request, AclUserBean loginUser) {
		List<Map<String, Object>> list=null;
		try {
			String license=new String(DaoFactory.getParam(request, "license_no").getBytes("iso8859_1"), "GBK");
			StringBuffer sb= new StringBuffer();
			sb.append("select o.* from Tt_As_Repair_Order o where o.license is not null ");
			DaoFactory.getsql(sb, "o.license", license, 2);
			DaoFactory.getsql(sb, "o.dealer_id", DaoFactory.getParam(request, loginUser.getDealerId()), 1);
			list = pageQuery(sb.toString(), null,getFunName());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findVinListByVin(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select * from ( select v.vin,rownum rn  from tm_vehicle v where 1=1 ");
		String vin = DaoFactory.getParam(request, "vin");
		if("".equals(vin) || vin.length()<8){
			return null;
		}else{
			DaoFactory.getsql(sb, "v.vin", vin, 2);
		}
		sb.append(" ) where   rn between 1 and 10");
		List<Map<String, Object>> list = pageQuery(sb.toString(), null, this.getFunName());
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getGuaFlag(String partCode,String vin,String inMileage,String purchasedDate) {
		purchasedDate = purchasedDate + " 00:00";
		String noNo = "";
		String notice = "";
		boolean flag = true;
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			/**
			 * 开始判断是否是配件三包期
			 */
			TmVehiclePO vp = new TmVehiclePO();
			vp.setVin(vin);
			/***** add by liuxh 20131108判断车架号不能为空 *****/
			CommonUtils.jugeVinNull(vin);
			/***** add by liuxh 20131108判断车架号不能为空 *****/
			vp = (TmVehiclePO) this.select(vp).get(0);
			if (vp.getClaimTacticsId() != null && vp.getPurchasedDate() != null) {
				// 比较购车日期和当前日期之间的差值
				String d2 = DateTimeUtil.parseDateToString2(new Date());// 将当前时间转化为
																		// 字符串
				int month = Utility.compareDate(purchasedDate, d2, 1); // 获取相差月数
				int day = Utility.compareDate(purchasedDate, d2, 0);// 获取相差天数
				if (month % 12 == 0 && day > (month % 12) * 365) {
					month = month + 1;
				}
				TtAsWrRuleListPO lp = new TtAsWrRuleListPO();
				List list = this.getNoFamily(vp.getClaimTacticsId(), partCode);
				List<TtAsWrGamePO> gList = this.isFamily(vp.getClaimTacticsId());
				if (gList != null && gList.size() > 0) {// 如果是家用车
					TtAsWrGamePO gp = (TtAsWrGamePO) gList.get(0);
					// 家用车三包判定
					TmPtPartBasePO bp = new TmPtPartBasePO();
					bp.setPartCode(partCode);
					bp = (TmPtPartBasePO) this.select(bp).get(0);
					if (Constant.PART_WR_TYPE_2.equalsIgnoreCase(bp
							.getPartWarType().toString())) {// 如果是易损易耗件
						if (month <= bp.getWrMonths()&& Double.valueOf(inMileage) <= bp.getWrMileage()) {// 在配件三包内
							flag = true;
							noNo = "in";
						} else {
							flag = false;
							notice += "该配件已经超配件三包期!\n";
							noNo = "out";// 超过配件三包期
						}
					} else {// 不是易损易耗件,则按常规件判定，包括：保修期,配件三包规则中的较大者
						if (list != null && list.size() > 0) {// 如果在三包规则里面有维护,则取
																// 保修期,配件三包规则中的较大者
																// 进行比较
							lp = (TtAsWrRuleListPO) list.get(0);
							int month2 = Utility.compareIntMax(
									gp.getClaimMonth(), lp.getClaimMonth());
							double melieage = Utility.compareDoubleMax(
									gp.getClaimMelieage(),
									lp.getClaimMelieage());
							if (month <= month2&& Double.valueOf(inMileage) <= melieage) {// 在配件三包内
								flag = true;
								noNo = "in";
							} else {
								flag = false;
								notice += "该配件已经超配件三包期!\n";
								noNo = "out";// 超过配件三包期
							}

						} else {// 如果没有维护三包规则,直接取三包策略的包修期
							if (month <= gp.getClaimMonth()&& Double.valueOf(inMileage) <= gp.getClaimMelieage()) {// 在配件三包内
								flag = true;
								noNo = "in";
							} else {
								flag = false;
								notice += "该配件已经超配件三包期!\n";
								noNo = "out";// 超过配件三包期
							}
						}
					}
				} else {// 如果不是家用车,则直接按照配件三包规则判断
					if (list != null && list.size() > 0) {
						lp = (TtAsWrRuleListPO) list.get(0);
						if (month <= lp.getClaimMonth()&& Integer.parseInt(inMileage) <= lp.getClaimMelieage()) {// 在配件三包内
							flag = true;
							noNo = "in";
						} else {
							flag = false;
							notice += "该配件已经超配件三包期!\n";
							noNo = "out";// 超过配件三包期
						}
					} else {
						flag = false;
						notice += "该配件没有维护配件三包规则!\n";
						noNo = "NoGame";// 非家用车,没有维护配件三包规则
					}
				}
			} else {
				flag = true;
			}
			map.put("flag", flag);
			map.put("notice", notice);
			map.put("noNo", noNo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return map;
	}
	/**
	 * 查询有的服务活动
	 * @param request
	 * @param loginUser
	 * @param pageSize
	 * @param currPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> doActivity(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		try {
			String vin = DaoFactory.getParam(request, "vin");
			String in_mileage = DaoFactory.getParam(request, "in_mileage");
			sb.append("select s.activity_num,\n" );
			sb.append("       s.subject_no,\n" );
			sb.append("       s.subject_name,\n" );
			sb.append("       t.id as templet_id,t.templet_no,t.is_tips,t.is_return,\n" );
			sb.append("       t.templet_name, a.activity_type,\n" );
			sb.append("       a.activity_code,\n" );
			sb.append("       a.activity_name,\n" );
			sb.append("       a.activity_id\n" );
			sb.append("  from tt_as_activity_subject    s,\n" );
			sb.append("       tt_as_activity            a,\n" );
			sb.append("       tt_as_wr_activity_templet t\n" );
			sb.append(" where s.subject_id = a.subject_id\n" );
			sb.append("   and t.subject_id = s.subject_id\n" );
			sb.append("   and a.templet_id=t.id and a.is_del = 0\n" );
			sb.append("   and a.status = 10681002\n" );
			sb.append("   and (((select count(*)\n" );
			sb.append("            from tt_as_repair_order r\n" );
			sb.append("           where r.cam_code = a.activity_code\n" );
			sb.append("             and r.vin = '"+vin+"') < a.single_car_num) or\n" );
			sb.append("       a.single_car_num is null)\n" );
			sb.append("   and trunc(sysdate) between a.startdate and a.enddate\n" );
			DaoFactory.getsql(sb, "a.activity_code", DaoFactory.getParam(request, "activityCode"), 2);
			DaoFactory.getsql(sb, "a.activity_name", DaoFactory.getParam(request, "activityName"), 2);
			String activity_id = this.getActivityId(in_mileage,vin,loginUser.getDealerCode());//根据vin 经销商代码 里程进行取值服务活动ID
			//TODO 根据VIN，里程，和经销商代码进行判断，然后根据条件查询出该服务活动ID
			DaoFactory.getsql(sb, "a.activity_id", activity_id, 6);
			sb.append(" and trunc(sysdate) between a.startdate and a.enddate\n");
			sb.append("    order by trim(a.activity_code) desc  \n");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		PageResult<Map<String, Object>> list=this.pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	/**
	 * 调用存储过程
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getActivityId(String in_mileage,String vin,String dealer_code) {
		List params = new LinkedList();
		params.add(vin);
		params.add(dealer_code);
		params.add(in_mileage);
		String activity_id = callFunction("f_get_temp_activity_id", java.sql.Types.VARCHAR,params )==null?"-1":callFunction("f_get_temp_activity_id", java.sql.Types.VARCHAR,params ).toString();
		return activity_id;
	}

	@SuppressWarnings("unchecked")
	public int deleteTrPart(RequestWrapper request) {
		int res=1;
		String partId = DaoFactory.getParam(request, "partId");
		String claim_no = DaoFactory.getParam(request, "claim_no");
		String ro_no = DaoFactory.getParam(request, "ro_no");
		try {
			if(!"".equals(partId) && !"".equals(ro_no) && !"".equals(claim_no)){
				this.delete("update  tt_as_ro_repair_part p set p.is_use=null where p.real_part_id='"+partId+"' and p.ro_id=(select o.id from tt_as_repair_order o where o.ro_no='"+ro_no+"')",null);
				this.delete("delete from tt_as_wr_partsitem p where p.id=(select a.id from tt_as_wr_application a where a.claim_no='"+claim_no+"') and p.real_part_id="+partId, null);
			}else{
				res=-1;
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAccData(RequestWrapper request) {
		String part_code = DaoFactory.getParam(request, "part_code");
		String ro_no = DaoFactory.getParam(request, "ro_no");
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*  from tt_accessory_dtl t where 1=1 ");
		DaoFactory.getsql(sb, "t.ro_no", ro_no, 1);
		DaoFactory.getsql(sb, "t.main_part_code", part_code, 1);
		return this.pageQuery(sb.toString(), null, getFunName());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findOutById(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*\n" );
		sb.append("  from tt_as_ro_out_data t\n" );
		sb.append(" where 1 = 1\n" );
		sb.append("  and t.ro_no = (select o.ro_no from Tt_As_Repair_Order o where o.id ="+DaoFactory.getParam(request, "id")+")");
		return this.pageQuery(sb.toString(), null, getFunName());
	}

	@SuppressWarnings({ "unchecked" })
	public String findFreeRoByPartId(String ro_no, String partId) {
		StringBuffer sbBase= new StringBuffer();
		sbBase.append("select o.ro_no, o.in_mileage,o.create_date,p.part_no, rl.claim_month, rl.claim_melieage\n" );
		sbBase.append("  from tt_as_repair_order   o,\n" );
		sbBase.append("       tt_as_ro_repair_part p,\n" );
		sbBase.append("       TT_AS_WR_BACKUP_LIST rl,\n" );
		sbBase.append("       tm_pt_part_base      b\n" );
		sbBase.append(" where o.id = p.ro_id(+)\n" );
		sbBase.append("   and rl.part_code = b.part_code\n" );
		sbBase.append("   and p.part_no = b.part_code\n" );
		sbBase.append("   and p.is_use is null\n" );
		sbBase.append("   and p.is_gua = 0\n" );
		sbBase.append("   and p.pay_type = 11801002\n" );
		sbBase.append("   and p.repairtypecode in (93331001, 93331003)");
		DaoFactory.getsql(sbBase, "p.real_part_id", partId, 1);
		DaoFactory.getsql(sbBase, "o.ro_no", ro_no, 1);
		List<Map<String, Object>> listBase = this.pageQuery(sbBase.toString(), null, getFunName());
		String in_mileage_base = "";
		String create_date_base = "";
		String claim_melieage = "";
		String claim_month = "";
		if(DaoFactory.checkListNull(listBase)){
			Map<String, Object> map = listBase.get(0);
		    in_mileage_base = BaseUtils.checkNull(map.get("IN_MILEAGE"));
		    create_date_base = BaseUtils.checkNull(map.get("CREATE_DATE"));
		    claim_melieage = BaseUtils.checkNull(map.get("CLAIM_MELIEAGE"));
		    claim_month = BaseUtils.checkNull(map.get("CLAIM_MONTH"));
		}
		StringBuffer sb= new StringBuffer();
		sb.append("select o.ro_no, o.in_mileage,o.create_date,p.part_no, rl.claim_month, rl.claim_melieage\n" );
		sb.append("  from tt_as_repair_order   o,\n" );
		sb.append("       tt_as_ro_repair_part p,\n" );
		sb.append("       TT_AS_WR_BACKUP_LIST rl,\n" );
		sb.append("       tm_pt_part_base      b\n" );
		sb.append(" where o.id = p.ro_id(+)\n" );
		sb.append("   and rl.part_code = b.part_code\n" );
		sb.append("   and p.part_no = b.part_code\n" );
		sb.append("   and p.is_use is null\n" );
		sb.append("   and p.is_gua = 0\n" );
		sb.append("   and p.pay_type = 11801001\n" );
		sb.append("   and p.repairtypecode in (93331001, 93331003)");
		DaoFactory.getsql(sb, "p.real_part_id", partId, 1);
		DaoFactory.getsql(sb, "o.ro_no", ro_no, 8);
		sb.append(" order by o.create_date desc");
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
		StringBuffer sbRo= new StringBuffer();
		sbRo.append("<select class='short_sel' name='free_ro'><option value=''>-请选择-</option>");
		if(DaoFactory.checkListNull(list)){
			if(list.size()==1){
				for (Map<String, Object> map : list) {
					String roNo = BaseUtils.checkNull(map.get("RO_NO"));
					String in_mileage = BaseUtils.checkNull(map.get("IN_MILEAGE"));
					String create_date = BaseUtils.checkNull(map.get("CREATE_DATE"));
					IsSBCheck(in_mileage_base, create_date_base,claim_melieage, claim_month, sbRo, roNo,in_mileage, create_date);
				}
			}
			if(list.size()>1){
				String in_mileage="";
				String create_date="";
				String ro_no_1="";
				for (int i = 0; i < list.size(); i++) {//从第一个开始，依次递减查询出符合条件的数据
					if(i==0){
						Map<String, Object> mapTemp = list.get(0);
						ro_no_1 = BaseUtils.checkNull(mapTemp.get("RO_NO"));
						in_mileage = BaseUtils.checkNull(mapTemp.get("IN_MILEAGE"));
						create_date = BaseUtils.checkNull(mapTemp.get("CREATE_DATE"));
					}else{
						Map<String, Object> map = list.get(i);
						ro_no_1 = BaseUtils.checkNull(map.get("RO_NO"));
						in_mileage = BaseUtils.checkNull(map.get("IN_MILEAGE"));
						create_date = BaseUtils.checkNull(map.get("CREATE_DATE"));
					}
					IsSBCheck(in_mileage_base, create_date_base,claim_melieage, claim_month, sbRo, ro_no_1,in_mileage, create_date);
				}
			}
		}
		sbRo.append("</select>");
		return sbRo.toString();
	}
	/**
	 * 根据业务判断是否拼接数据
	 * @param in_mileage_base
	 * @param create_date_base
	 * @param claim_melieage
	 * @param claim_month
	 * @param sbRo
	 * @param roNo
	 * @param in_mileage
	 * @param create_date
	 */
	@SuppressWarnings("deprecation")
	private void IsSBCheck(String in_mileage_base, String create_date_base,String claim_melieage, String claim_month, StringBuffer sbRo,String roNo, String in_mileage, String create_date) {
		double melieageSub = Double.parseDouble(in_mileage_base)-Double.parseDouble(in_mileage);
		Calendar c=Calendar.getInstance();
		c.setTime(CommonUtils.parseDate(create_date));
		long date_1 = c.getTimeInMillis();
		c.setTime(CommonUtils.parseDate(create_date_base));
		long date_2 = c.getTimeInMillis();
		Date d=new Date();
		d.setMonth(Integer.parseInt(claim_month));
		long time = d.getTime();
		if((Math.abs(date_1-date_2)<time)&&(Math.abs(melieageSub)<Double.parseDouble(claim_melieage))){//大于了维护的日期 && //大于了维护的公里数
			sbRo.append("<option value='"+roNo+"'>"+roNo+"</option>");
		}
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> showInfoByroNo(RequestWrapper request) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from tt_as_repair_order r where 1=1");
		DaoFactory.getsql(sb, "r.ro_no", DaoFactory.getParam(request, "ro_no"), 1);
		return this.pageQueryMap(sb.toString(),null, getFunName());
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> checkaddPart(RequestWrapper request,AclUserBean loginUser) {
		StringBuffer sql= new StringBuffer();
		sql.append("select *\n" );
		sql.append("  from TT_AS_WR_RULE_LIST r\n" );
		sql.append(" where 1 = 1\n" );
        DaoFactory.getsql(sql, "r.part_code", DaoFactory.getParam(request, "partCode"), 1);
		return this.pageQuery(sql.toString(), null, getFunName());
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> showInfoWinterByVin(RequestWrapper request,String str,AclUserBean loginUser) {
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct a.amount, a.model_code, p.package_code,vw.package_name\n" );
		sql.append("  from TT_AS_WR_WINTER_MAINTEN        a,\n" );
		sql.append("       vw_material_group_service      vw,\n" );
		sql.append("       tm_vehicle                     v,\n" );
		sql.append("       TT_AS_WR_WINTER_PACKAGE        p\n" );
		sql.append(" where 1=1 and a.status=10681002\n" );
		sql.append(" and a.id=p.wintwe_id(+)\n" );
		sql.append("   and p.package_code = vw.PACKAGE_CODE(+)\n" );
		sql.append("   and vw.package_id = v.package_id(+)\n" );
		DaoFactory.getsql(sql, "v.vin", DaoFactory.getParam(request, "vin"), 1);
		sql.append("   and p.dealer_id='" +loginUser.getDealerId()+"'");
		sql.append("   and a.model_code='" +str+"'");
		sql.append("   and a.start_date<=sysdate\n" );
		DaoFactory.getsql(sql, "a.model_code",DaoFactory.getParam(request, "model_code"), 1);//车型
		sql.append("   and a.end_date>=sysdate");
	   List<Map<String,Object>> list = 	this.pageQuery(sql.toString(), null, getFunName());
	   if (DaoFactory.checkListNull(list)) {
		     return list.get(0);
	      }else {
			return null;
		}
	}

}
