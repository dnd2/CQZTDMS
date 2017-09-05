package com.infodms.dms.dao.crm.delivery;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
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
public class DelvManageDao extends BaseDao<PO>{
	
	private static final DelvManageDao dao = new DelvManageDao();
	
	public static final DelvManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * FUNCTION		:	查询经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getDelvCheckQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String ctmName=map.get("ctmName");
		String telephone=map.get("telephone");
		String orderDate=map.get("orderDate");
		String preDlvDate=map.get("preDlvDate");
		String preDlvDateEnd=map.get("preDlvDateEnd");
		StringBuilder sql= new StringBuilder();
		String dealerId=map.get("dealerId");
		String logonId=map.get("logonId");
		String vin=map.get("vin");
		sql.append("SELECT TPC.customer_id,\n" );
		sql.append("       tv.vehicle_id,\n" );
		sql.append("       tpd.order_detail_id,\n" );
		sql.append("       tpd.delv_detail_id,\n" );
		sql.append("       TPC.customer_name,\n" );
		sql.append("       TPC.telephone,\n" );
		sql.append("       TV.vin,\n" );
		sql.append("       TV.material_id,\n" );
		sql.append("       tvm.material_code,\n" );
		sql.append("       tvm.color_name,\n" );
		sql.append("       tpd.price,\n" );
		sql.append("       tv.life_cycle,\n");
		sql.append("       tpd.delivery_status,\n");
		sql.append("       tpd.delivery_date\n" );
		sql.append("  FROM t_pc_customer    TPC,\n" );
		sql.append("       T_PC_DELVY       TPD,\n" );
		sql.append("       tm_vehicle       Tv,\n" );
		sql.append("       tc_user     tu,\n" );
		sql.append("       tm_vhcl_material tvm\n" );
		sql.append(" WHERE TPC.customer_id = TPD.customer_id\n" );
		sql.append("   AND TPD.vehicle_id = TV.vehicle_id\n" );
		sql.append("  and tu.user_id(+)=tpc.adviser \n");
		sql.append("   AND TV.material_id = TVM.material_id");
		sql.append("   AND TPD.STATUS = "+Constant.STATUS_ENABLE+"\n" );
	//	sql.append("  and tv.life_cycle="+Constant.VEHICLE_LIFE_09);
		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=vin&&!"".equals(vin)){
			sql.append(" and tv.vin like '%"+vin+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone  like '%"+telephone+"%'");
		}
		if(null!=preDlvDate&&!"".equals(preDlvDate)){
			sql.append("                           AND TPd.delivery_date >=\n" );
			sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND Tpd.delivery_date <=\n" );
			sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			if(logonId!=null&&!"".equals(logonId)){
				sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
				sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
			}
			
		}
		sql.append("    order by tpd.delivery_status,TPd.delivery_date desc  ");
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * FUNCTION		:	查询所有的交车和未交车的数据
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getDelvAllQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String ctmName=map.get("ctmName");
		String telephone=map.get("telephone");
		String preDlvDate=map.get("preDlvDate");
		String preDlvDateEnd=map.get("preDlvDateEnd");
		String dealerId=map.get("dealerId");
		String logonId=map.get("logonId");
		String ifDelv=map.get("ifDelv");
		String adviserId=map.get("adviserId");
		String groupId=map.get("groupId");
		String vin=map.get("vin");
		StringBuilder sql= new StringBuilder();
		
			sql.append("SELECT TPC.customer_id,\n" );
			sql.append("       TPC.customer_name,\n" );
			sql.append("       TPC.telephone,\n" );
			sql.append("       TV.vin,\n" );
			sql.append("       tvm.material_code,\n" );
			sql.append("       tvm.color_name,\n" );
			sql.append("       tpd.price,\n" );
			sql.append("       1 NUMS ,\n" );
			sql.append("       tv.vehicle_id,\n" );
			sql.append("        tO_char(tpd.DELIVERY_DATE, 'yyyy-mm-dd') DELIVERY_DATE,\n" );
			sql.append("       1 delivery_number,\n" );
			sql.append("       TPD.delivery_status types\n" );
			sql.append("  FROM t_pc_customer    TPC,\n" );
			sql.append("       T_PC_DELVY       TPD,\n" );
			sql.append("       tm_vehicle       Tv,\n" );
			sql.append("       tc_user     tu,\n" );
			sql.append("       tm_vhcl_material tvm \n" );
			sql.append(" WHERE TPC.customer_id = TPD.customer_id\n" );
			sql.append("   AND TPD.vehicle_id = TV.vehicle_id\n" );
			sql.append("  and tu.user_id(+)=tpc.adviser\n" );
			sql.append("   AND TV.material_id = TVM.material_id\n" );
			
			if(null!=ctmName&&!"".equals(ctmName)){
				sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
			}
			if(null!=telephone&&!"".equals(telephone)){
				sql.append(" and tpc.telephone  like '%"+telephone+"%'");
			}
			if(null!=vin&&!"".equals(vin)){
				sql.append(" and tv.vin like '%"+vin+"%'\n");
			}
			if(null!=ifDelv && !"".equals(ifDelv)){
				sql.append(" and TPD.delivery_status='"+ifDelv+"'\n");
			}
			if(null!=preDlvDate&&!"".equals(preDlvDate)){
				sql.append("                           AND TPd.delivery_date >=\n" );
				sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
				sql.append("                           AND Tpd.delivery_date <=\n" );
				sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			}
			if(null!=dealerId&&!"".equals(dealerId)){
				sql.append(" and tpc.dealer_id="+dealerId);
				if(logonId!=null&&!"".equals(logonId)){
					sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
					sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
				}
				
			}
			return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
	
		
		/*
		if("1".equals(ifDelv)){
			sql.append("SELECT TPC.customer_id,\n" );
			sql.append("       tpo.order_id,\n" );
			sql.append("       tpod.order_detail_id,\n" );
			sql.append("       TPC.customer_name,\n" );
			sql.append("       TPC.telephone,\n" );
			sql.append("       TV.vin,\n" );
			sql.append("       tvm.material_code,\n" );
			sql.append("       tvm.color_name,\n" );
			sql.append("       tpd.price,\n" );
			sql.append("       1 NUMS ,\n" );
			sql.append("       tv.vehicle_id,\n" );
			sql.append("        tO_char(tpd.DELIVERY_DATE, 'yyyy-mm-dd') DELIVERY_DATE,\n" );
			sql.append("       1 delivery_number,\n" );
			sql.append("       1 types\n" );
			sql.append("  FROM t_pc_customer    TPC,\n" );
			sql.append("       T_PC_DELVY       TPD,\n" );
			sql.append("       tm_vehicle       Tv,\n" );
			sql.append("       tc_user     tu,\n" );
			sql.append("       tm_vhcl_material tvm,\n" );
			sql.append("       t_pc_order tpo,\n" );
			sql.append("       t_pc_order_detail tpod\n" );
			sql.append(" WHERE TPC.customer_id = TPD.customer_id\n" );
			sql.append("   AND TPD.vehicle_id = TV.vehicle_id\n" );
			sql.append("  and tu.user_id(+)=tpc.adviser\n" );
			sql.append("   AND TV.material_id = TVM.material_id\n" );
			sql.append("   and tpo.order_id=tpod.order_id\n" );
			sql.append("   and tpd.order_detail_id=tpod.order_detail_id");
			sql.append(" AND TPD.STATUS=10011001 \n");
			if(null!=ctmName&&!"".equals(ctmName)){
				sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
			}
			if(null!=telephone&&!"".equals(telephone)){
				sql.append(" and tpc.telephone  like '%"+telephone+"%'");
			}
			if(null!=vin&&!"".equals(vin)){
				sql.append(" and tv.vin like '%"+vin+"%'\n");
			}
			if(null!=preDlvDate&&!"".equals(preDlvDate)){
				sql.append("                           AND TPd.delivery_date >=\n" );
				sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
				sql.append("                           AND Tpd.delivery_date <=\n" );
				sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			}
			if(null!=dealerId&&!"".equals(dealerId)){
				sql.append(" and tpc.dealer_id="+dealerId);
				if(logonId!=null&&!"".equals(logonId)){
					sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
					sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
				}
				
			}
			return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		}
		if("0".equals(ifDelv)){
			sql.append("select tpc.customer_id,\n" );
			sql.append("       tpo.order_id,\n" );
			sql.append("       tpod.order_detail_id,\n" );
			sql.append("       tpc.customer_name,\n" );
			sql.append("       tpc.telephone,\n" );
			sql.append("       null vin,\n" );
			sql.append("       tvm.material_code material_code,\n" );
			sql.append("       tc.code_desc color_name,\n" );
			sql.append("       tpod.price,\n" );
			sql.append("       tpod.num NUMS,\n" );
			sql.append("       null VEHICLE_ID,\n" );
			sql.append("       tO_char(tpod.DELIVERY_DATE, 'yyyy-mm-dd') delivery_date,\n" );
			sql.append("       tpod.delivery_number,\n" );
			sql.append("       0\n" );
			sql.append("  from t_pc_order        tpo,\n" );
			sql.append("       t_pc_order_detail tpod,\n" );
			sql.append("       t_pc_customer     tpc,\n" );
			sql.append("       tc_code     tc,\n" );
			sql.append("       tc_user     tu,\n" );
			sql.append("       tm_vhcl_material  tvm\n" );
			sql.append(" where tpod.order_id = tpo.order_id\n" );
			sql.append("   and tpc.customer_id = tpo.customer_id\n" );
			sql.append("   and tvm.material_id(+) = tpod.intent_model\n" );
			sql.append("   and tc.code_id(+) = tpod.intent_color\n" );
			sql.append("  and tu.user_id(+)=tpc.adviser");
			sql.append("  and tpo.order_status in("+Constant.TPC_ORDER_STATUS_02+","+Constant.TPC_ORDER_STATUS_01+")");
			sql.append("   and tpod.task_status="+Constant.TASK_STATUS_01);
			sql.append("   and tpod.vehicle_id is null\n" );
			sql.append("   AND (TO_NUMBER(TPOD.NUM) - NVL(TPOD.delivery_number, 0)) > 0");
			if(null!=ctmName&&!"".equals(ctmName)){
				sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
			}
			if(null!=telephone&&!"".equals(telephone)){
				sql.append(" and tpc.telephone ="+telephone);
			}
			if(null!=vin&&!"".equals(vin)){
				sql.append(" and 1=2 ");
			}
			if(null!=preDlvDate&&!"".equals(preDlvDate)){
				sql.append("                           AND TPod.delivery_date >=\n" );
				sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
				sql.append("                           AND Tpod.delivery_date <=\n" );
				sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			}
			if(Utility.testString(adviserId)){
				sql.append(" and tpc.adviser in ('"+adviserId+"') \n");
			}
			if(Utility.testString(groupId)){
				sql.append(" and tu.group_id = '"+groupId+"' \n");
			}
			if(null!=dealerId&&!"".equals(dealerId)){
				sql.append(" and tpc.dealer_id="+dealerId);
				if(logonId!=null&&!"".equals(logonId)){
					sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
					sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
				}
			}
			sql.append("   union all \n" );
			sql.append("   select tpc.customer_id,\n" );
			sql.append("       tpo.order_id,\n" );
			sql.append("       tpod.order_detail_id,\n" );
			sql.append("       tpc.customer_name,\n" );
			sql.append("       tpc.telephone,\n" );
			sql.append("       tv.vin,\n" );
			sql.append("     tvm.material_code model,\n" );
			sql.append("       tvm.color_name color,\n" );
			sql.append("       tpod.price,\n" );
			sql.append("       tpod.num NUMS ,\n" );
			sql.append("       tv.VEHICLE_ID,");
			sql.append("       tO_char(tpod.ACT_DELV_DATE,'yyyy-mm-dd') delivery_date,\n" );
			sql.append("       tpod.delivery_number,\n" );
			sql.append("       0\n" );
			sql.append("  from t_pc_order tpo, t_pc_order_detail tpod, t_pc_customer tpc,tm_vehicle tv,tc_user tu,tm_vhcl_material tvm\n" );
			sql.append(" where tpod.order_id = tpo.order_id\n" );
			sql.append("   and tpc.customer_id = tpo.customer_id\n" );
			sql.append("   and tvm.material_id=tv.material_id\n" );
			sql.append("   and tv.vehicle_id=tpod.vehicle_id\n" );
			sql.append("  and tu.user_id(+)=tpc.adviser");
			sql.append("  and tpo.order_status in("+Constant.TPC_ORDER_STATUS_02+","+Constant.TPC_ORDER_STATUS_01+")");
			sql.append("   and tpod.task_status="+Constant.TASK_STATUS_01);
			sql.append("   AND (TO_NUMBER(TPOD.NUM) - NVL(TPOD.delivery_number, 0)) > 0");

			if(null!=ctmName&&!"".equals(ctmName)){
				sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
			}
			if(null!=telephone&&!"".equals(telephone)){
				sql.append(" and tpc.telephone ="+telephone);
			}
			if(null!=vin&&!"".equals(vin)){
				sql.append(" and tv.vin like '%"+vin+"%'\n");
			}
			if(null!=preDlvDate&&!"".equals(preDlvDate)){
				sql.append("                           AND TPod.delivery_date >=\n" );
				sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
				sql.append("                           AND Tpod.delivery_date <=\n" );
				sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
				sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			}
			if(null!=dealerId&&!"".equals(dealerId)){
				sql.append(" and tpc.dealer_id="+dealerId);
				if(logonId!=null&&!"".equals(logonId)){
					sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
					sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
				}
			}
			return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		}
		
		sql.append("SELECT TPC.customer_id,\n" );
		sql.append("       tpo.order_id,\n" );
		sql.append("       tpod.order_detail_id,\n" );
		sql.append("       TPC.customer_name,\n" );
		sql.append("       TPC.telephone,\n" );
		sql.append("       TV.vin,\n" );
		sql.append("       tvm.material_code,\n" );
		sql.append("       tvm.color_name,\n" );
		sql.append("       tpd.price,\n" );
		sql.append("       1 NUMS ,\n" );
		sql.append("       tv.vehicle_id,\n" );
		sql.append("        tO_char(tpd.DELIVERY_DATE, 'yyyy-mm-dd') DELIVERY_DATE,\n" );
		sql.append("       1 delivery_number,\n" );
		sql.append("       decode(tpd.status,10011001,1,2) types\n" );
		sql.append("  FROM t_pc_customer    TPC,\n" );
		sql.append("       T_PC_DELVY       TPD,\n" );
		sql.append("       tm_vehicle       Tv,\n" );
		sql.append("       tc_user     tu,\n" );
		sql.append("       tm_vhcl_material tvm,\n" );
		sql.append("       t_pc_order tpo,\n" );
		sql.append("       t_pc_order_detail tpod\n" );
		sql.append(" WHERE TPC.customer_id = TPD.customer_id\n" );
		sql.append("   AND TPD.vehicle_id = TV.vehicle_id\n" );
		sql.append("  and tu.user_id(+)=tpc.adviser\n" );
		sql.append("   AND TV.material_id = TVM.material_id\n" );
		sql.append("   and tpo.order_id=tpod.order_id\n" );
		sql.append("   and tpd.order_detail_id=tpod.order_detail_id");
		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone  like '%"+telephone+"%'");
		}
		if(null!=vin&&!"".equals(vin)){
			sql.append(" and tv.vin like '%"+vin+"%'\n");
		}
		if(null!=preDlvDate&&!"".equals(preDlvDate)){
			sql.append("                           AND TPd.delivery_date >=\n" );
			sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND Tpd.delivery_date <=\n" );
			sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(Utility.testString(adviserId)){
			sql.append(" and tpc.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			if(logonId!=null&&!"".equals(logonId)){
				sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
				sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
			}
		}
		sql.append("   union all \n" );
		sql.append("select tpc.customer_id,\n" );
		sql.append("       tpo.order_id,\n" );
		sql.append("       tpod.order_detail_id,\n" );
		sql.append("       tpc.customer_name,\n" );
		sql.append("       tpc.telephone,\n" );
		sql.append("       null vin,\n" );
		sql.append("       tvm.material_code model,\n" );
		sql.append("       tc.code_desc color,\n" );
		sql.append("       tpod.price,\n" );
		sql.append("       tpod.num NUMS,\n" );
		sql.append("       null VEHICLE_ID,\n" );
		sql.append("       tO_char(tpod.DELIVERY_DATE, 'yyyy-mm-dd') delivery_date,\n" );
		sql.append("       tpod.delivery_number,\n" );
		sql.append("       0\n" );
		sql.append("  from t_pc_order        tpo,\n" );
		sql.append("       t_pc_order_detail tpod,\n" );
		sql.append("       t_pc_customer     tpc,\n" );
		sql.append("       tc_code     tc,\n" );
		sql.append("       tc_user     tu,\n" );
		sql.append("       tm_vhcl_material  tvm\n" );
		sql.append(" where tpod.order_id = tpo.order_id\n" );
		sql.append("   and tpc.customer_id = tpo.customer_id\n" );
		sql.append("   and tvm.material_id(+) = tpod.intent_model\n" );
		sql.append("   and tc.code_id(+) = tpod.intent_color\n" );
		sql.append("  and tu.user_id(+)=tpc.adviser");
		sql.append("  and tpo.order_status in("+Constant.TPC_ORDER_STATUS_02+","+Constant.TPC_ORDER_STATUS_01+")");
		sql.append("   and tpod.task_status="+Constant.TASK_STATUS_01);
		sql.append("   and tpod.vehicle_id is null\n" );
		sql.append("   AND (TO_NUMBER(TPOD.NUM) - NVL(TPOD.delivery_number, 0)) > 0");
		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone ="+telephone);
		}
		if(null!=vin&&!"".equals(vin)){
			sql.append(" and 1=2 ");
		}
		if(null!=preDlvDate&&!"".equals(preDlvDate)){
			sql.append("                           AND TPod.delivery_date >=\n" );
			sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND Tpod.delivery_date <=\n" );
			sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(Utility.testString(adviserId)){
			sql.append(" and tpc.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			if(logonId!=null&&!"".equals(logonId)){
				sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
				sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
			}
		}
		sql.append("   union all \n" );
		sql.append("   select tpc.customer_id,\n" );
		sql.append("       tpo.order_id,\n" );
		sql.append("       tpod.order_detail_id,\n" );
		sql.append("       tpc.customer_name,\n" );
		sql.append("       tpc.telephone,\n" );
		sql.append("       tv.vin,\n" );
		sql.append("     tvm.material_code model,\n" );
		sql.append("       tvm.color_name color,\n" );
		sql.append("       tpod.price,\n" );
		sql.append("       tpod.num NUMS ,\n" );
		sql.append("       tv.VEHICLE_ID,");
		sql.append("       tO_char(tpod.ACT_DELV_DATE,'yyyy-mm-dd') delivery_date,\n" );
		sql.append("       tpod.delivery_number,\n" );
		sql.append("       0\n" );
		sql.append("  from t_pc_order tpo, t_pc_order_detail tpod, t_pc_customer tpc,tm_vehicle tv,tc_user tu,tm_vhcl_material tvm\n" );
		sql.append(" where tpod.order_id = tpo.order_id\n" );
		sql.append("   and tpc.customer_id = tpo.customer_id\n" );
		sql.append("   and tvm.material_id=tv.material_id\n" );
		sql.append("   and tv.vehicle_id=tpod.vehicle_id\n" );
		sql.append("  and tu.user_id(+)=tpc.adviser");
		sql.append("  and tpo.order_status in("+Constant.TPC_ORDER_STATUS_02+","+Constant.TPC_ORDER_STATUS_01+")");
		sql.append("   and tpod.task_status="+Constant.TASK_STATUS_01);
		sql.append("   AND (TO_NUMBER(TPOD.NUM) - NVL(TPOD.delivery_number, 0)) > 0");

		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone ="+telephone);
		}
		if(null!=vin&&!"".equals(vin)){
			sql.append(" and tv.vin like '%"+vin+"%'\n");
		}
		if(null!=preDlvDate&&!"".equals(preDlvDate)){
			sql.append("                           AND TPod.delivery_date >=\n" );
			sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND Tpod.delivery_date <=\n" );
			sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(Utility.testString(adviserId)){
			sql.append(" and tpc.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			if(logonId!=null&&!"".equals(logonId)){
				sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
				sql.append("  CONNECT BY PRIOR tcu.user_id = tcu.par_user_iD)");
			}
		}
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		
		return ps;
		*/
	}
	/**
	 * FUNCTION		:	查询经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getDelvQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String ctmName=map.get("ctmName");
		String telephone=map.get("telephone");
		String orderDate=map.get("orderDate");
		String orderDateEnd=map.get("orderDateEnd");
		String preDlvDate=map.get("preDlvDate");
		String preDlvDateEnd=map.get("preDlvDateEnd");
		String dealerId=map.get("dealerId");
		String logonId=map.get("logonId");
		String vin=map.get("vin");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpc.customer_id,\n" );
		sql.append("       tpo.order_id,\n" );
		sql.append("       tpod.order_detail_id,\n" );
		sql.append("       tpc.customer_name,\n" );
		sql.append("       tpc.telephone,\n" );
		sql.append("       null vin,\n" );
		sql.append("       tvm.material_code model,\n" );
		sql.append("       tc.code_desc color,\n" );
		sql.append("       tpod.price,\n" );
		sql.append("       tpod.num,\n" );
		sql.append("       null VEHICLE_ID,\n" );
		sql.append("       tO_char(tpod.DELIVERY_DATE, 'yyyy-mm-dd') delivery_date,\n" );
		sql.append("       tpod.delivery_number\n" );
		sql.append("  from t_pc_order        tpo,\n" );
		sql.append("       t_pc_order_detail tpod,\n" );
		sql.append("       t_pc_customer     tpc,\n" );
		sql.append("       tc_code     tc,\n" );
		sql.append("       tc_user     tu,\n" );
		sql.append("       tm_vhcl_material  tvm\n" );
		sql.append(" where tpod.order_id = tpo.order_id\n" );
		sql.append("   and tpc.customer_id = tpo.customer_id\n" );
		sql.append("   and tvm.material_id(+) = tpod.intent_model\n" );
		sql.append("   and tc.code_id(+) = tpod.intent_color\n" );
		sql.append("  and tu.user_id(+)=tpc.adviser");
		sql.append("  and tpo.order_status in("+Constant.TPC_ORDER_STATUS_02+","+Constant.TPC_ORDER_STATUS_01+")");
		sql.append("   and tpod.vehicle_id is null\n" );
		sql.append("   AND (TO_NUMBER(TPOD.NUM) - NVL(TPOD.delivery_number, 0)) > 0");
		sql.append("   and tpod.task_status="+Constant.TASK_STATUS_01);
		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone ="+telephone);
		}
		if(null!=vin&&!"".equals(vin)){
			sql.append(" and 1=2");
		}
		if(null!=orderDate&&!"".equals(orderDate)){
			sql.append("                           AND TPo.order_date >=\n" );
			sql.append("                               TO_DATE('"+orderDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND Tpo.order_date <=\n" );
			sql.append("                               TO_DATE('"+orderDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=preDlvDate&&!"".equals(preDlvDate)){
			sql.append("                           AND TPod.delivery_date >=\n" );
			sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND Tpod.delivery_date <=\n" );
			sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
			sql.append("  CONNECT BY nocycle PRIOR tcu.user_id = tcu.par_user_iD)");
		}
		sql.append("   union\n" );
		sql.append("   select tpc.customer_id,\n" );
		sql.append("       tpo.order_id,\n" );
		sql.append("       tpod.order_detail_id,\n" );
		sql.append("       tpc.customer_name,\n" );
		sql.append("       tpc.telephone,\n" );
		sql.append("       tv.vin,\n" );
		sql.append("     tvm.material_code model,\n" );
		sql.append("       tvm.color_name color,\n" );
		sql.append("       tpod.price,\n" );
		sql.append("       tpod.num ,\n" );
		sql.append("       tv.VEHICLE_ID,");
		sql.append("       tO_char(tpod.ACT_DELV_DATE,'yyyy-mm-dd') delivery_date,\n" );
		sql.append("       tpod.delivery_number\n" );
		sql.append("  from t_pc_order tpo, t_pc_order_detail tpod, t_pc_customer tpc,tm_vehicle tv,tc_user tu,tm_vhcl_material tvm\n" );
		sql.append(" where tpod.order_id = tpo.order_id\n" );
		sql.append("   and tpc.customer_id = tpo.customer_id\n" );
		sql.append("   and tvm.material_id=tv.material_id\n" );
		sql.append("   and tv.vehicle_id=tpod.vehicle_id\n" );
		sql.append("  and tu.user_id(+)=tpc.adviser");
		sql.append("  and tpo.order_status in("+Constant.TPC_ORDER_STATUS_02+","+Constant.TPC_ORDER_STATUS_01+")");
		sql.append("   and tpod.task_status="+Constant.TASK_STATUS_01);
		sql.append("   AND (TO_NUMBER(TPOD.NUM) - NVL(TPOD.delivery_number, 0)) > 0");

		if(null!=ctmName&&!"".equals(ctmName)){
			sql.append(" and tpc.customer_name like '%"+ctmName+"%'\n");
		}
		if(null!=telephone&&!"".equals(telephone)){
			sql.append(" and tpc.telephone ="+telephone);
		}
		if(null!=vin&&!"".equals(vin)){
			sql.append(" and tv.vin like '%"+vin+"%'\n");
		}
		if(null!=orderDate&&!"".equals(orderDate)){
			sql.append("                           AND TPo.order_date >=\n" );
			sql.append("                               TO_DATE('"+orderDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND Tpo.order_date <=\n" );
			sql.append("                               TO_DATE('"+orderDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=preDlvDate&&!"".equals(preDlvDate)){
			sql.append("                           AND TPod.delivery_date >=\n" );
			sql.append("                               TO_DATE('"+preDlvDate+"' || '00:00:00',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
			sql.append("                           AND Tpod.delivery_date <=\n" );
			sql.append("                               TO_DATE('"+preDlvDateEnd+"' || '23:59:59',\n" );
			sql.append("                                       'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if(null!=dealerId&&!"".equals(dealerId)){
			sql.append(" and tpc.dealer_id="+dealerId);
			sql.append(" and   tu.user_id in(select tcu.user_id from tc_user tcu where 1=1 start with tcu.user_id in("+logonId+")  \n");
			sql.append("  CONNECT BY nocycle PRIOR tcu.user_id = tcu.par_user_iD)");
		}
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return ps;
	}
	public List<Map<String, Object>> queryDataList(Map<String, String> map) {
		StringBuilder sql= new StringBuilder();
		sql.append("select td.code_id ,\n" );
		sql.append("       td.type ,\n" );
		sql.append("       td.code_desc ,\n" );
		sql.append("       td.is_down code_level,\n" );
		sql.append("       (select count(1) from tc_code tc where tc.type = td.code_id) next_count\n" );
		sql.append("  from tc_code td\n" );
		sql.append(" where td.code_id != 6002\n" );
		sql.append("   and td.status = '10011001'\n" );
		sql.append(" START WITH TD.code_id = 6002\n" );
		sql.append("CONNECT BY  nocycle PRIOR TD.code_id = TD.type");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
		/**
		 * 交车列表弹出框中的查询
		 * @param map
		 * @param pageSize
		 * @param curPage
		 * @return
		 */
	public PageResult<Map<String, Object>> getVinList(
			Map<String, String> map,String ldealerId, Integer pageSize, Integer curPage) {
		String dealerId=map.get("dealerId");
		String vin=map.get("vin");
		String modelCode=map.get("modelCode");
		String qkOrderDetailId=map.get("qkOrderDetailId");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT tv.VEHICLE_ID,\n" );
		sql.append("       tv.vin,\n" );
		sql.append("       tvmg.group_code,\n" );
		sql.append("       tvmg.group_name,\n" );
		sql.append("       tvm.color_name\n" );
		sql.append("  FROM tm_vhcl_material TVM, tm_vehicle tv,tm_vhcl_material_group tvmg,tm_vhcl_material_group_r tvmgr\n" );
		sql.append(" where tv.material_id = tvm.material_id\n" );
		sql.append(" and tvmg.group_id=tvmgr.group_id\n" );
		sql.append(" and tvmgr.material_id=tvm.material_id\n" );
		//sql.append(" and tvm.status=10011001\n" );
		//sql.append(" and tvmg.status=10011001\n" );
		sql.append("and tv.life_cycle=10321003\n" );
		sql.append(" and tv.lock_status=10241001\n");
		
		if(qkOrderDetailId!=null&&!"".equals(qkOrderDetailId)){
			   sql.append(" and tvm.material_id in( select material_id from vw_material_info where model_id in( "+//pz_intent_series 
			  " select series_id from t_pc_intent_vehicle tpiv where tpiv.series_id in ( "+
		      " select tpod.intent_model from t_pc_order_detail tpod where tpod.order_detail_id='"+qkOrderDetailId+"' ))) \n" );
		}
		
		if(dealerId!=null&&!"".equals(dealerId)) {
			sql.append("and tv.dealer_id="+dealerId);
			sql.append("AND tv.DEALER_ID IN\n" );
			sql.append("      (SELECT TD1.DEALER_ID\n" );
			sql.append("         FROM TM_DEALER TD1\n" );
			sql.append("        WHERE 1 = 1\n" );
			sql.append("        START WITH TD1.DEALER_ID IN ("+dealerId+")\n" );
			sql.append("       CONNECT BY PRIOR TD1.DEALER_ID = TD1.PARENT_DEALER_D)\n" );
		} 
		
		if(modelCode!=null&&!"".equals(modelCode)){
			sql.append("  and tvmg.group_code like '%"+modelCode+"%'\n" );
		}
		if(vin!=null&&!"".equals(vin)){
			sql.append("  and tv.vin like '%"+vin+"%'\n" );
		}
		
		

		PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return ps ;
	}


	
	
	
	
}
