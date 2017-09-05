package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CustomerCareActivityDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(CustomerCareActivityDao.class);
	public static final CustomerCareActivityDao dao = new CustomerCareActivityDao();
	public static final CustomerCareActivityDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成的方法存根
		return null;
	}
	public List<Map<String, Object>> QueryCustomerCareActivity(Map<String,Object> map) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DS.ROOT_ORG_NAME,\n" );
		sql.append("       DS.DEALER_CODE,\n" );
		sql.append("       DS.DEALER_NAME,\n" );
		sql.append("       S.SUBJECT_NO,\n" );
		sql.append("       S.SUBJECT_NAME,\n" );
		sql.append("       T.EVALUATE,\n" );
		sql.append("       T.MEASURES,\n" );
		sql.append("       /*进站量（台）*/\n" );
		sql.append("       T.PULL_IN_NUM, /*提升目标值*/\n" );
		sql.append("       T.PULL_IN_MEAN, /*活动前平均数据*/\n" );
		sql.append("       T.PULL_IN_REGION, /*活动区间数据*/\n" );
		sql.append("       T.PULL_IN_INCRE, /*增长%*/\n" );
		sql.append("\n" );
		sql.append("       /*老客户返厂量（台）*/\n" );
		sql.append("       T.CUSTOMER_NUM, /*提升目标值*/\n" );
		sql.append("       T.CUSTOMER_MEAN, /*活动前平均数据*/\n" );
		sql.append("       T.CUSTOMER_REGION, /*活动区间数据*/\n" );
		sql.append("       T.CUSTOMER_INCRE, /*增长%*/\n" );
		sql.append("\n" );
		sql.append("       /*客单价（元）*/\n" );
		sql.append("       T.PRICE_NUM, /*提升目标值*/\n" );
		sql.append("       T.PRICE_MEAN, /*活动前平均数据*/\n" );
		sql.append("       T.PRICE_REGION, /*活动区间数据*/\n" );
		sql.append("       T.PRICE_INCRE, /*增长%*/\n" );
		sql.append("\n" );
		sql.append("       /*营业额（元）*/\n" );
		sql.append("       T.OPEN_NUM, /*提升目标值*/\n" );
		sql.append("       T.OPEN_MEAN, /*活动前平均数据*/\n" );
		sql.append("       T.OPEN_REGION, /*活动区间数据*/\n" );
		sql.append("       T.OPEN_INCRE /*增长%*/\n" );
		sql.append("  FROM TT_AS_ACTIVITY_EVALUATE T,\n" );
		sql.append("       VW_ORG_DEALER_SERVICE   DS,\n" );
		sql.append("       TT_AS_ACTIVITY_SUBJECT  S\n" );
		sql.append(" WHERE T.SUBJECT_ID = S.SUBJECT_ID\n" );
		sql.append("   AND DS.DEALER_ID = T.DEALER_ID");

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
		return pageQuery(sql.toString(), null, getFunName());


	}
	public  PageResult<Map<String, Object>> RqueryCustomerCareActivity(Map<String,Object> map,int pageSize,int curPage) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DS.ROOT_ORG_NAME,\n" );
		sql.append("       DS.DEALER_CODE,\n" );
		sql.append("       DS.DEALER_NAME,\n" );
		sql.append("       S.SUBJECT_NO,\n" );
		sql.append("       S.SUBJECT_NAME,\n" );
		sql.append("       T.EVALUATE,\n" );
		sql.append("       T.MEASURES,\n" );
		sql.append("       /*进站量（台）*/\n" );
		sql.append("       T.PULL_IN_NUM, /*提升目标值*/\n" );
		sql.append("       T.PULL_IN_MEAN, /*活动前平均数据*/\n" );
		sql.append("       T.PULL_IN_REGION, /*活动区间数据*/\n" );
		sql.append("       T.PULL_IN_INCRE, /*增长%*/\n" );
		sql.append("\n" );
		sql.append("       /*老客户返厂量（台）*/\n" );
		sql.append("       T.CUSTOMER_NUM, /*提升目标值*/\n" );
		sql.append("       T.CUSTOMER_MEAN, /*活动前平均数据*/\n" );
		sql.append("       T.CUSTOMER_REGION, /*活动区间数据*/\n" );
		sql.append("       T.CUSTOMER_INCRE, /*增长%*/\n" );
		sql.append("\n" );
		sql.append("       /*客单价（元）*/\n" );
		sql.append("       T.PRICE_NUM, /*提升目标值*/\n" );
		sql.append("       T.PRICE_MEAN, /*活动前平均数据*/\n" );
		sql.append("       T.PRICE_REGION, /*活动区间数据*/\n" );
		sql.append("       T.PRICE_INCRE, /*增长%*/\n" );
		sql.append("\n" );
		sql.append("       /*营业额（元）*/\n" );
		sql.append("       T.OPEN_NUM, /*提升目标值*/\n" );
		sql.append("       T.OPEN_MEAN, /*活动前平均数据*/\n" );
		sql.append("       T.OPEN_REGION, /*活动区间数据*/\n" );
		sql.append("       T.OPEN_INCRE /*增长%*/\n" );
		sql.append("  FROM TT_AS_ACTIVITY_EVALUATE T,\n" );
		sql.append("       VW_ORG_DEALER_SERVICE   DS,\n" );
		sql.append("       TT_AS_ACTIVITY_SUBJECT  S\n" );
		sql.append(" WHERE T.SUBJECT_ID = S.SUBJECT_ID\n" );
		sql.append("   AND DS.DEALER_ID = T.DEALER_ID");
		sql.append("   AND DS.DEALER_TYPE="+Constant.MSG_TYPE_2);
		sql.append("  AND S.ACTIVITY_TYPE IN ("+Constant.SERVICEACTIVITY_TYPE_02+","+Constant.SERVICEACTIVITY_TYPE_03 +") ");

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
