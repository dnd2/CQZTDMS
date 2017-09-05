package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CustomerCareActivityDelDao extends BaseDao {
	public static final Logger logger = Logger.getLogger(CustomerCareActivityDelDao.class);
	public static final CustomerCareActivityDelDao dao = new CustomerCareActivityDelDao();
	public static final CustomerCareActivityDelDao getInstance(){
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成的方法存根
		return null;
	}
	
public  PageResult<Map<String, Object>> RqueryCustomerCareActivityDel(Map<String,Object> map,int pageSize,int curPage) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DS.ROOT_ORG_NAME, /*大区*/\n" );
		sql.append("       DS.DEALER_CODE,/*服务站代码*/\n" );
		sql.append("       DS.DEALER_NAME,/*服务站名称*/\n" );
		sql.append("       S.SUBJECT_NO,/*主题编号*/\n" );
		sql.append("       S.SUBJECT_NAME,/*活动主题*/\n" );
		sql.append("       A.ACTIVITY_CODE,/*活动代码*/\n" );
		sql.append("       A.ACTIVITY_NAME,/*活动名称*/\n" );
		sql.append("       RO.RO_NO,/*工单号码*/\n" );
		sql.append("       RO.MODEL,/*车型*/\n" );
		sql.append("       RO.VIN,/*VIN号码*/\n" );
		sql.append("       to_char(RO.Ro_Create_Date,'yyyy-mm-dd hh24:mi') as RO_CREATE_DATE,/*开单日期*/\n" );
		sql.append("       RO.IN_MILEAGE,/*公里数*/\n" );
		sql.append("       RO.OWNER_NAME,/*车主*/\n" );
		sql.append("       c.main_phone,/*车主电话*/\n" );
		sql.append("       ro.deliverer,/*送修人*/\n" );
		sql.append("       ro.deliverer_phone,/*送修人电话*/\n" );
		sql.append("       nvl(p.part_amount,0) part_amount,/*材料优惠费用*/\n" );
		sql.append("       nvl(l.labour_amount,0) labour_amount,/*工时优惠费用*/\n" );
		sql.append("       ri.part_zs_amount,/*赠送配件费用*/\n" );
		sql.append("       ri.free_jc_amount,/*免费检测费用*/\n" );
		sql.append("       ri.baoyang_zs_amount,/*赠送保养费用*/\n" );
		sql.append("       nvl(ap.lp_amount,0)lp_amount,/*赠送礼品费用*/\n" );
		sql.append("       nvl(ap.zs_amount,0) zs_amount/*赠送金额*/\n" );
		sql.append("  FROM TT_AS_REPAIR_ORDER RO\n" );
		sql.append("  JOIN VW_ORG_DEALER_SERVICE DS ON NVL(RO.SECOND_DEALER_ID, RO.DEALER_ID) = DS.DEALER_ID\n" );
		sql.append("  LEFT JOIN TT_AS_ACTIVITY A ON RO.CAM_CODE = A.ACTIVITY_CODE\n" );
		sql.append("  LEFT JOIN TT_AS_ACTIVITY_SUBJECT S ON A.SUBJECT_ID = S.SUBJECT_ID\n" );
		sql.append("  LEFT JOIN tt_crm_customer c ON ro.vin = c.vin\n" );
		sql.append("  LEFT JOIN (SELECT ro_id, SUM(rp.part_quantity*NVL(rp.part_cost_price,0)-NVL(rp.part_cost_amount,0)) part_amount FROM TT_AS_RO_REPAIR_PART rp GROUP BY rp.ro_id) p  ON ro.ID = p.ro_ID\n" );
		sql.append("  LEFT JOIN (SELECT rl.ro_id, SUM(NVL(rl.labour_price,0)*NVL(rl.std_labour_hour,0) - NVL(rl.labour_amount,0)) labour_amount FROM tt_as_ro_labour rl GROUP BY rl.ro_id) l ON ro.ID = l.ro_id\n" );
		sql.append("  LEFT JOIN  (\n");
		sql.append("  select max(decode(ri.add_item_code, '"+Constant.SERVICEACTIVITY_CAR_cms_05+"', NVL(ri.add_item_amount,0), 0)) part_zs_amount,\n");
		sql.append("          max(decode(ri.add_item_code, '"+Constant.SERVICEACTIVITY_CAR_cms_01+"', NVL(ri.add_item_amount,0), 0)) free_jc_amount,\n");
		sql.append("          max(decode(ri.add_item_code,'"+Constant.SERVICEACTIVITY_CAR_cms_02+"', NVL(ri.discount,0)-NVL(ri.add_item_amount,0), 0)) baoyang_zs_amount,ri.ro_id\n");
		sql.append("from tt_as_ro_add_item ri where  ri.pay_type="+Constant.PAY_TYPE_02+" group by  ri.ro_id  ) ri on ri.ro_id = ro.id"); 

		sql.append("  LEFT JOIN (SELECT P.ACTIVITY_ID,\n" );
		sql.append("       SUM(DECODE(AR.GIFT_NAME, "+Constant.TAX_RATE_GIFT_01+",AR.Project_Amount+nvl(ar.gife_amount,0), 0)) LP_AMOUNT,\n" );
		sql.append("       SUM(DECODE(AR.GIFT_NAME, "+Constant.TAX_RATE_GIFT_02+", AR.Project_Amount+nvl(ar.gife_amount,0), 0)) ZS_AMOUNT\n" );
		sql.append("  FROM TT_AS_ACTIVITY_RELATION AR, TT_AS_ACTIVITY_PROJECT P\n" );
		sql.append(" WHERE AR.ACTIVITY_ID = P.ACTIVITY_ID\n" );
		sql.append("   AND AR.LARGESS_TYPE = P.PRO_CODE\n" );
		sql.append("   AND AR.LARGESS_TYPE = "+Constant.SERVICEACTIVITY_CAR_cms_04 +"\n" );
		sql.append(" GROUP BY P.ACTIVITY_ID) ap ON a.activity_id = ap.activity_id\n" );
		sql.append("  WHERE RO.REPAIR_TYPE_CODE = 11441005\n" );
		sql.append("  AND S.ACTIVITY_TYPE IN ("+Constant.SERVICEACTIVITY_TYPE_02+","+Constant.SERVICEACTIVITY_TYPE_03 +") ");
		sql.append("    AND ro.create_date >= to_date('2013-08-26','yyyy-mm-dd')");

		if(map.get("serviceactivityType")!=null && !"".equals(map.get("serviceactivityType"))){
			sql.append(" AND S.ACTIVITY_TYPE ="+map.get("serviceactivityType"));
		}
		if(map.get("ButieBh")!=null && !"".equals(map.get("ButieBh"))){
			sql.append(" AND S.SUBJECT_NO like '%"+map.get("ButieBh")+"%'");
		}
		if(map.get("ButieName")!=null && !"".equals(map.get("ButieName"))){
			sql.append("  AND S.SUBJECT_NAME like '%"+map.get("ButieName")+"%'");
		}
		if (null != map.get("dealerCode") && !"".equals(map.get("dealerCode"))) {
			String[] array = map.get("dealerCode").toString().split(",");
			sql.append("   AND DS.DEALER_CODE IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							sql.append(",");
						}	
				}
			}else{
				sql.append("''");//放空置，防止in里面报错
			}
			sql.append(")\n");
		}	
		if(map.get("bigorgId")!=null && !"".equals(map.get("bigorgId"))){
			sql.append("  AND DS.ROOT_ORG_ID ="+map.get("bigorgId"));
		}
		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;


	}
	
}
