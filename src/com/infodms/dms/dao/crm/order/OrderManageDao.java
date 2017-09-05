package com.infodms.dms.dao.crm.order;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infodms.dms.dao.crm.order.OrderManageDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OrderManageDao extends BaseDao<PO>{
	
	private static final OrderManageDao dao = new OrderManageDao();
	
	public static final OrderManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 订单数量审核查询
	 */
	public PageResult<Map<String, Object>> orderAmountFindQuery(
			String customerName,
			String telephone,String orderDate,String orderDateEnd,
			String orderStatus, String userDealerId, String adviser, String adviserId, String groupId,String vin,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT distinct A.ORDER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.TELEPHONE,C1.NAME AS ADVISER,\r\n");
		sbSql.append("       TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE,\r\n");
		sbSql.append("       C.CODE_DESC AS ORDER_STATUS,A.ORDER_STATUS AS ORDER_STATUS2 \r\n");
		sbSql.append("  FROM T_PC_ORDER A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B\r\n");
		sbSql.append("    ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("  LEFT JOIN TC_USER C1 ON B.ADVISER = C1.USER_ID\r\n");
		if(Utility.testString(vin)){
		sbSql.append("  LEFT JOIN t_pc_order_detail D ON D.order_id = A.order_id\r\n");
		sbSql.append("  LEFT JOIN TM_VEHICLE M ON M.vehicle_id = D.vehicle_id\r\n");
		}
		sbSql.append("  LEFT JOIN TC_CODE C\r\n");
		sbSql.append("    ON A.ORDER_STATUS = C.CODE_ID  WHERE 1=1 AND A.ORDER_STATUS NOT IN ('60231006','60231003','60231004') "); 

		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and b.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and b.telephone like '%"+telephone+"%' ");
		}
		if (Utility.testString(orderDate)) {//订单日期
			sbSql.append(" AND a.order_date >=\n" );
			sbSql.append("  TO_DATE('"+orderDate+"' || '00:00:00',\n" );
			sbSql.append("   'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if (Utility.testString(orderDateEnd)) {//订单日期
			sbSql.append("   AND a.order_date <=\n" );
			sbSql.append("  TO_DATE('"+orderDateEnd+"' || '23:59:59',\n" );
			sbSql.append("   'YYYY-MM-DD hh24:mi:ss ')\n" );
			
		}
		if(Utility.testString(orderStatus)){//订单状态
			sbSql.append(" and a.order_Status = '"+orderStatus+"' \n");
		}
		if(Utility.testString(vin)){//车架号VIN
			sbSql.append(" and M.vin like '%"+vin+"%' \n");
		}
		if(Utility.testString(userDealerId)){
			sbSql.append(" and b.dealer_id = '"+userDealerId+"' \n");
		}
		if(Utility.testString(adviser)){
			sbSql.append(" and b.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and b.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and c1.group_id = '"+groupId+"' \n");
		}
		sbSql.append(" order by decode(a.order_status,60231008,1,60231001,2,3),order_date desc \n");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	
	/**
	 * 订单销售经理查询
	 */
	public PageResult<Map<String, Object>> orderFindQuery(
			String customerName,
			String telephone,String orderDate,String orderDateEnd,
			String orderStatus, String userDealerId, String adviser, String adviserId, String groupId,String vin,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT distinct A.ORDER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.TELEPHONE,C1.NAME AS ADVISER,\r\n");
		sbSql.append("       TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE,\r\n");
		sbSql.append("       C.CODE_DESC AS ORDER_STATUS,A.ORDER_STATUS AS ORDER_STATUS2 \r\n");
		sbSql.append("  FROM T_PC_ORDER A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B\r\n");
		sbSql.append("    ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("  LEFT JOIN TC_USER C1 ON B.ADVISER = C1.USER_ID\r\n");
		if(Utility.testString(vin)){
		sbSql.append("  LEFT JOIN t_pc_order_detail D ON D.order_id = A.order_id\r\n");
		sbSql.append("  LEFT JOIN TM_VEHICLE M ON M.vehicle_id = D.vehicle_id\r\n");
		}
		sbSql.append("  LEFT JOIN TC_CODE C\r\n");
		sbSql.append("    ON A.ORDER_STATUS = C.CODE_ID  WHERE 1=1 AND A.ORDER_STATUS NOT IN (60231006) AND (A.TASK_STATUS=60171002 OR A.ORDER_STATUS in('60231007','60231009','60231010'))"); 

		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and b.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and b.telephone like '%"+telephone+"%' ");
		}
		if (Utility.testString(orderDate)) {//订单日期
			sbSql.append(" AND a.order_date >=\n" );
			sbSql.append("  TO_DATE('"+orderDate+"' || '00:00:00',\n" );
			sbSql.append("   'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if (Utility.testString(orderDateEnd)) {//订单日期
			sbSql.append("   AND a.order_date <=\n" );
			sbSql.append("  TO_DATE('"+orderDateEnd+"' || '23:59:59',\n" );
			sbSql.append("   'YYYY-MM-DD hh24:mi:ss ')\n" );
			
		}
		if(Utility.testString(orderStatus)){//订单状态
			sbSql.append(" and a.order_Status = '"+orderStatus+"' \n");
		}
		if(Utility.testString(vin)){//车架号VIN
			sbSql.append(" and M.vin like '%"+vin+"%' \n");
		}
		if(Utility.testString(userDealerId)){
			sbSql.append(" and b.dealer_id = '"+userDealerId+"' \n");
		}
		if(Utility.testString(adviser)){
			sbSql.append(" and b.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and b.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and c1.group_id = '"+groupId+"' \n");
		}
		sbSql.append(" order by order_id desc,decode(a.order_status,60231003,1,60231004,2,3),order_date desc \n");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	

	/**
	 * 订单车厂总部人员查询
	 */
	public PageResult<Map<String, Object>> orderCompanyFindQuery(
			String customerName,
			String telephone,String orderDate,String orderDateEnd,
			String orderStatus, String userDealerId, String dealerCode,String vin,String orgId,String dutyType,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT distinct A.ORDER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.TELEPHONE,C1.NAME AS ADVISER,\r\n");
		sbSql.append("       TMD.DEALER_CODE,TMD.DEALER_SHORTNAME, \r\n");
		sbSql.append("       TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE,\r\n");
		sbSql.append("       C.CODE_DESC AS ORDER_STATUS,A.ORDER_STATUS AS ORDER_STATUS2 \r\n");
		sbSql.append("  FROM T_PC_ORDER A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B\r\n");
		sbSql.append("    ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("  LEFT JOIN TM_DEALER TMD ON TMD.DEALER_ID=B.DEALER_ID \r\n");
		sbSql.append("  LEFT JOIN TC_USER C1 ON B.ADVISER = C1.USER_ID \r\n");
		if(Utility.testString(vin)){
		sbSql.append("  LEFT JOIN t_pc_order_detail D ON D.order_id = A.order_id\r\n");
		sbSql.append("  LEFT JOIN TM_VEHICLE M ON M.vehicle_id = D.vehicle_id\r\n");
		}
		sbSql.append("  LEFT JOIN TC_CODE C\r\n");
		sbSql.append("    ON A.ORDER_STATUS = C.CODE_ID  WHERE 1=1 AND A.ORDER_STATUS IN ('60231009','60231010') "); 

		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and b.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and b.telephone like '%"+telephone+"%' ");
		}
		if (Utility.testString(orderDate)) {//订单日期
			sbSql.append(" AND a.order_date >=\n" );
			sbSql.append("  TO_DATE('"+orderDate+"' || '00:00:00',\n" );
			sbSql.append("   'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if (Utility.testString(orderDateEnd)) {//订单日期
			sbSql.append("   AND a.order_date <=\n" );
			sbSql.append("  TO_DATE('"+orderDateEnd+"' || '23:59:59',\n" );
			sbSql.append("   'YYYY-MM-DD hh24:mi:ss ')\n" );
			
		}
		if(Utility.testString(orderStatus)){//订单状态
			sbSql.append(" and a.order_Status = '"+orderStatus+"' \n");
		}
		if(Utility.testString(vin)){//车架号VIN
			sbSql.append(" and M.vin like '%"+vin+"%' \n");
		}
		if(Utility.testString(dealerCode)){
			sbSql.append(" and  TMD.DEALER_CODE = '"+dealerCode+"' \n");
		}
		/*
		if(Utility.testString(userDealerId)){
			sbSql.append(" and b.dealer_id = '"+userDealerId+"' \n");
		}
		*/
		//大区
	if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
		sbSql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
		//小区
	}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
		sbSql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
	}
		
		sbSql.append(" order by decode(a.order_status,60231009,1,60231010,2,3),order_date desc \n");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	

	/**
	 * 订单区域经理查询
	 */
	public PageResult<Map<String, Object>> orderMannagerFindQuery(
			String customerName,
			String telephone,String orderDate,String orderDateEnd,
			String orderStatus, String userDealerId, String adviser, String adviserId, String groupId,String vin,String orgId,String dutyType,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT distinct A.ORDER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.TELEPHONE,C1.NAME AS ADVISER,\r\n");
		sbSql.append("       TMD.DEALER_CODE,TMD.DEALER_SHORTNAME,\r\n");
		sbSql.append("       TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE,\r\n");
		sbSql.append("       C.CODE_DESC AS ORDER_STATUS,A.ORDER_STATUS AS ORDER_STATUS2 \r\n");
		sbSql.append("  FROM T_PC_ORDER A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B\r\n");
		sbSql.append("    ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("    LEFT JOIN TM_DEALER TMD ON TMD.DEALER_ID=B.DEALER_ID \r\n");
		sbSql.append("  LEFT JOIN TC_USER C1 ON B.ADVISER = C1.USER_ID\r\n");
		if(Utility.testString(vin)){
		sbSql.append("  LEFT JOIN t_pc_order_detail D ON D.order_id = A.order_id\r\n");
		sbSql.append("  LEFT JOIN TM_VEHICLE M ON M.vehicle_id = D.vehicle_id\r\n");
		}
		sbSql.append("  LEFT JOIN TC_CODE C\r\n");
		sbSql.append("    ON A.ORDER_STATUS = C.CODE_ID  WHERE 1=1 "); // AND A.ORDER_STATUS IN ('60231009','60231010')

		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and b.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and b.telephone like '%"+telephone+"%' ");
		}
		if (Utility.testString(orderDate)) {//订单日期
			sbSql.append(" AND a.order_date >=\n" );
			sbSql.append("  TO_DATE('"+orderDate+"' || '00:00:00',\n" );
			sbSql.append("   'YYYY-MM-DD hh24:mi:ss ')\n" );
		}
		if (Utility.testString(orderDateEnd)) {//订单日期
			sbSql.append("   AND a.order_date <=\n" );
			sbSql.append("  TO_DATE('"+orderDateEnd+"' || '23:59:59',\n" );
			sbSql.append("   'YYYY-MM-DD hh24:mi:ss ')\n" );
			
		}
		if(Utility.testString(orderStatus)){//订单状态
			sbSql.append(" and a.order_Status = '"+orderStatus+"' \n");
		}
		if(Utility.testString(vin)){//车架号VIN
			sbSql.append(" and M.vin like '%"+vin+"%' \n");
		}
		if(Utility.testString(userDealerId)){
			sbSql.append(" and b.dealer_id = '"+userDealerId+"' \n");
		}
		//大区
	if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
		sbSql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
		//小区
	}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
		sbSql.append(" AND B.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
	}
		if(Utility.testString(adviser)){
			sbSql.append(" and b.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and b.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and c1.group_id = '"+groupId+"' \n");
		}
		sbSql.append(" order by decode(a.order_status,60231003,1,60231004,2,3),order_date desc \n");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 车厂订车退车记录查询
	 */
	public PageResult<Map<String, Object>> orderAuditCarFindQuery(
			String customerName,
			String telephone,String startDate,String endDate,
			String orderStatus, String userDealerId, String adviser, String adviserId,String seriesId,String groupId,String dealerCode,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		 sbSql.append(" select vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME,A.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,A.up_series_id,tpiv.series_name,A.orderNum,TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE, DECODE(sign(A.orderNum-1),1,'下单',0,'下单',-1,'退单')  task_status,A.adviser adviserId ,tu.name adviser,A.dealer_id from ( ");
		 sbSql.append(" select  B.order_id,B.customer_id,B.up_series_id,order_date,B.dealer_id,B.ADVISER,sum(num) orderNum from ( ");
		 sbSql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,to_number(tpiv.up_series_id) up_series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
		 sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id up_series_id,(select series_name from T_PC_INTENT_VEHICLE t1 where t1.series_id=vwm.pz_intent_series ) series_name,vwm.color_name color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,to_number(tpiv.up_series_id) up_series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id up_series_id,(select series_name from T_PC_INTENT_VEHICLE t1 where t1.series_id=vwm.pz_intent_series ) series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sbSql.append("  ) B where   ");
	     
	     if(Utility.testString(startDate)){
	    	 sbSql.append("   B.order_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(Utility.testString(endDate)){
	    	 sbSql.append("  AND   B.order_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sbSql.append(" group by  B.order_id,B.customer_id,B.up_series_id,order_date,B.dealer_id,B.ADVISER ");
	     
	     sbSql.append("  )A,TC_USER tu,VW_ORG_DEALER_ALL_new vw,t_Pc_Customer tpc,t_pc_intent_vehicle tpiv      ");
	     
	     sbSql.append("  where A.orderNum<>0 and A.ADVISER = tu.USER_ID  and A.dealer_id = vw.dealer_id and A.customer_id=tpc.customer_id  and A.up_series_id=tpiv.series_id");
	    
	       
		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and tpc.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and tpc.telephone like '%"+telephone+"%' ");
		}
		if(Utility.testString(orderStatus)){//订单状态
			if("1".equals(orderStatus)){
				sbSql.append(" and A.orderNum >0  \n");
				}else if("0".equals(orderStatus)){
					sbSql.append(" and A.orderNum <0  \n");
				}
		}
		
		String str="'0',";
		if(!"".equals(dealerCode)&&dealerCode!=null){
            String arr[]=dealerCode.split(",");
            for(int i=0;i<arr.length;i++){
                str+="'"+arr[i]+"',";
            }
		}
		str=str.substring(0,str.lastIndexOf(","));
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		String dutyType=logonUser.getDutyType();
		Long orgId=logonUser.getOrgId();
		
		if(Utility.testString(dealerCode)){
			sbSql.append(" and vw.dealer_code in ("+str+") \n");
		}else{
			if("10431003".equals(dutyType)){
				sbSql.append(" and vw.root_org_id ='"+orgId+"' \n");
			}else if("10431004".equals(dutyType)){
				sbSql.append(" and vw.pq_org_id ='"+orgId+"' \n");
			}
		}
		if(Utility.testString(adviser)){
			sbSql.append(" and A.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and A.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		/*
		sbSql.append(" order by decode(a.order_status,60231003,1,60231004,2,3),order_date desc \n");
		*/
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	

	/**
	 * 经销商订车退车记录查询
	 */
	public PageResult<Map<String, Object>> orderAuditCarFindQuery1(
			String customerName,
			String telephone,String startDate,String endDate,
			String orderStatus, String userDealerId, String adviser, String adviserId,String seriesId,String groupId,String dealerCode,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		 sbSql.append(" select vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME,A.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,A.up_series_id,tpiv.series_name,A.orderNum,TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE, DECODE(sign(A.orderNum-1),1,'下单',0,'下单',-1,'退单')  task_status,A.adviser adviserId ,tu.name adviser,A.dealer_id from ( ");
		 sbSql.append(" select  B.order_id,B.customer_id,B.up_series_id,order_date,B.dealer_id,B.ADVISER,sum(num) orderNum from ( ");
		 sbSql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,to_number(tpiv.up_series_id) up_series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
		 sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
		 if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id up_series_id,(select series_name from T_PC_INTENT_VEHICLE t1 where t1.series_id=vwm.pz_intent_series ) series_name,vwm.color_name color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,to_number(tpiv.up_series_id) up_series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id up_series_id,(select series_name from T_PC_INTENT_VEHICLE t1 where t1.series_id=vwm.pz_intent_series ) series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     
	     sbSql.append("  ) B where   ");
	     
	     if(Utility.testString(startDate)){
	    	 sbSql.append("   B.order_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(Utility.testString(endDate)){
	    	 sbSql.append("  AND   B.order_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     sbSql.append(" group by  B.order_id,B.customer_id,B.up_series_id,order_date,B.dealer_id,B.ADVISER ");
	     
	     sbSql.append("  )A,TC_USER tu,VW_ORG_DEALER_ALL_new vw,t_Pc_Customer tpc,t_pc_intent_vehicle tpiv      ");
	     
	     sbSql.append("  where A.orderNum<>0 and A.ADVISER = tu.USER_ID  and A.dealer_id = vw.dealer_id and A.customer_id=tpc.customer_id  and A.up_series_id=tpiv.series_id");
	    
	       
		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and tpc.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and tpc.telephone like '%"+telephone+"%' ");
		}
		if(Utility.testString(orderStatus)){//订单状态
			if("1".equals(orderStatus)){
			sbSql.append(" and A.orderNum >0  \n");
			}else if("0".equals(orderStatus)){
				sbSql.append(" and A.orderNum <0  \n");
			}
		}
		
		if(Utility.testString(adviser)){
			sbSql.append(" and A.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and A.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and tu.group_id = '"+groupId+"' \n");
		}
	
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
		
	}
	
	
	
	/**
	 * 车厂订车查询
	 */
	public PageResult<Map<String, Object>> orderCarFindQuery(
			String customerName,
			String telephone,String startDate,String endDate,
			String orderStatus, String userDealerId, String adviser, String adviserId,String seriesId,String groupId,String dealerCode,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		 sbSql.append(" select vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME,A.order_id,A.customer_id,A.customer_name,A.telephone,A.SERIES_ID,A.series_name,A.color_name,A.num,TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE, DECODE(A.task_status, '1','下单','0','退单')  task_status,A.adviser adviserId ,tu.name adviser,A.dealer_id from ( ");
		 sbSql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
		 sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,(select series_name from T_PC_INTENT_VEHICLE t1 where t1.series_id=vwm.pz_intent_series ) series_name,vwm.color_name color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,(select series_name from T_PC_INTENT_VEHICLE t1 where t1.series_id=vwm.pz_intent_series ) series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sbSql.append(" ) A,TC_USER tu,VW_ORG_DEALER_ALL_new vw where  A.ADVISER = tu.USER_ID and A.dealer_id=vw.dealer_id  ");
	     
	     if(Utility.testString(startDate)){
	    	 sbSql.append("  AND   A.order_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(Utility.testString(endDate)){
	    	 sbSql.append("  AND   A.order_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and A.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and A.telephone like '%"+telephone+"%' ");
		}
		if(Utility.testString(orderStatus)){//订单状态
			sbSql.append(" and A.task_Status in ("+orderStatus+") \n");
		}
		
		String str="'0',";
		if(!"".equals(dealerCode)&&dealerCode!=null){
            String arr[]=dealerCode.split(",");
            for(int i=0;i<arr.length;i++){
                str+="'"+arr[i]+"',";
            }
		}
		str=str.substring(0,str.lastIndexOf(","));
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		String dutyType=logonUser.getDutyType();
		Long orgId=logonUser.getOrgId();
		
		if(Utility.testString(dealerCode)){
			sbSql.append(" and vw.dealer_code in ("+str+") \n");
		}else{
			if("10431003".equals(dutyType)){
				sbSql.append(" and vw.root_org_id ='"+orgId+"' \n");
			}else if("10431004".equals(dutyType)){
				sbSql.append(" and vw.pq_org_id ='"+orgId+"' \n");
			}
		}
		if(Utility.testString(adviser)){
			sbSql.append(" and A.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and A.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		/*
		sbSql.append(" order by decode(a.order_status,60231003,1,60231004,2,3),order_date desc \n");
		*/
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	
	
	/**
	 * 车厂订车查询是否老客户介绍的
	 */
	public PageResult<Map<String, Object>> orderCarOldSuggestFindQuery(
			String customerName,
			String telephone,String startDate,String endDate,
			String orderStatus, String userDealerId, String adviser, String adviserId,String seriesId,String groupId,String dealerCode,String beStatus,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		 sbSql.append(" select vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME,A.order_id,A.customer_id,A.customer_name,A.telephone,A.SERIES_ID,A.series_name,A.color_name,A.num,TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE, DECODE(A.task_status, '1','下单','0','退单')  task_status,A.adviser adviserId ,tu.name adviser,A.dealer_id from ( ");
		 sbSql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
		 sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     
	     sbSql.append(" ) A,TC_USER tu,VW_ORG_DEALER_ALL_new vw,t_Pc_Link_Man tplm  where  A.ADVISER = tu.USER_ID and A.dealer_id=vw.dealer_id ");
		    
	     sbSql.append(" and A.customer_id=tplm.ctm_id "); 
	  
	     if(Utility.testString(startDate)){
	    	 sbSql.append("  AND   A.order_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(Utility.testString(endDate)){
	    	 sbSql.append("  AND   A.order_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and A.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and A.telephone like '%"+telephone+"%' ");
		}
		if(Utility.testString(orderStatus)){//订单状态
			sbSql.append(" and A.task_Status in ("+orderStatus+") \n");
		}
		
		if("10001".equals(beStatus)){
		    sbSql.append(" and tplm.relation_code='60581001' "); 
		 }else if("10002".equals(beStatus)){
			sbSql.append(" and tplm.relation_code='60581002' "); 
		 }else if("10003".equals(beStatus)){
			sbSql.append(" and tplm.relation_code in('60581001','60581002') "); 
		 }
		
		String str="'0',";
		if(!"".equals(dealerCode)&&dealerCode!=null){
            String arr[]=dealerCode.split(",");
            for(int i=0;i<arr.length;i++){
                str+="'"+arr[i]+"',";
            }
		}
		str=str.substring(0,str.lastIndexOf(","));
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		String dutyType=logonUser.getDutyType();
		Long orgId=logonUser.getOrgId();
		
		if(Utility.testString(dealerCode)){
			sbSql.append(" and vw.dealer_code in ("+str+") \n");
		}else{
			if("10431003".equals(dutyType)){
				sbSql.append(" and vw.root_org_id ='"+orgId+"' \n");
			}else if("10431004".equals(dutyType)){
				sbSql.append(" and vw.pq_org_id ='"+orgId+"' \n");
			}
		}
		if(Utility.testString(adviser)){
			sbSql.append(" and A.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and A.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		/*
		sbSql.append(" order by decode(a.order_status,60231003,1,60231004,2,3),order_date desc \n");
		*/
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	
	
	/**
	 * 经销商订车查询
	 */
	public PageResult<Map<String, Object>> orderCarFindQuery1(
			String customerName,
			String telephone,String startDate,String endDate,
			String orderStatus, String userDealerId, String adviser, String adviserId,String seriesId,String groupId,String dealerCode,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		 sbSql.append(" select vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME,A.order_id,A.customer_id,A.customer_name,A.telephone,A.SERIES_ID,A.series_name,A.color_name,A.num,TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE, DECODE(A.task_status, '1','下单','0','退单')  task_status,A.adviser adviserId ,tu.name adviser,A.dealer_id from ( ");
		 sbSql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
		 sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
		 if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,(select series_name from T_PC_INTENT_VEHICLE t1 where t1.series_id=vwm.pz_intent_series ) series_name,vwm.color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id, (select series_name from T_PC_INTENT_VEHICLE t1 where t1.series_id=vwm.pz_intent_series ) series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     
	     sbSql.append(" ) A,TC_USER tu,VW_ORG_DEALER_ALL_new vw where  A.ADVISER = tu.USER_ID and A.dealer_id=vw.dealer_id ");
	     
	     if(Utility.testString(startDate)){
	    	 sbSql.append("  AND   A.order_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(Utility.testString(endDate)){
	    	 sbSql.append("  AND   A.order_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and A.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and A.telephone like '%"+telephone+"%' ");
		}
		if(Utility.testString(orderStatus)){//订单状态
			sbSql.append(" and A.task_Status in ("+orderStatus+") \n");
		}
		/*
		if(Utility.testString(vin)){//车架号VIN
			sbSql.append(" and M.vin like '%"+vin+"%' \n");
		}
		*/
		
		if(Utility.testString(adviser)){
			sbSql.append(" and A.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and A.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		/*
		sbSql.append(" order by decode(a.order_status,60231003,1,60231004,2,3),order_date desc \n");
		*/
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	
	

	/**
	 * 经销商订车查询是否老客户介绍
	 */
	public PageResult<Map<String, Object>> orderCarSuggestFindQuery1(
			String customerName,
			String telephone,String startDate,String endDate,
			String orderStatus, String userDealerId, String adviser, String adviserId,String seriesId,String groupId,String dealerCode,String beStatus,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		 sbSql.append(" select vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,vw.DEALER_SHORTNAME,A.order_id,A.customer_id,A.customer_name,A.telephone,A.SERIES_ID,A.series_name,A.color_name,A.num,TO_CHAR(A.ORDER_DATE, 'YYYY-MM-DD') AS ORDER_DATE, DECODE(A.task_status, '1','下单','0','退单')  task_status,A.adviser adviserId ,tu.name adviser,A.dealer_id from ( ");
		 sbSql.append(" select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
		 sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
		 sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
		 if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }	
		 if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name,tpod.num,tpod.create_date order_date,'1' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,tc.code_desc color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_customer tpc,T_PC_INTENT_VEHICLE tpiv,t_pc_order_detail tpod left join TC_CODE tc on tpod.Intent_Color = tc.code_id(+) ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.intent_model=tpiv.series_id  ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.UP_SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     sbSql.append(" union all select tpo.order_id,tpc.customer_id,tpc.customer_name,tpc.telephone,tpiv.series_id,tpiv.series_name,vwm.color_name,0-tpod.num num,tpod.orderd_date order_date ,'0' task_status,tpc.adviser,tpc.dealer_id ");
	     sbSql.append(" from t_pc_order tpo,t_pc_order_detail tpod,t_pc_customer tpc,vw_material_info vwm,T_PC_INTENT_VEHICLE tpiv ");
	     sbSql.append(" where tpo.order_id=tpod.order_id and tpo.order_status not in ('60231006','60231008','60231011') and tpod.task_status=60171003 and tpo.customer_id=tpc.customer_id and tpod.material=vwm.material_id and vwm.intent_series=tpiv.series_id ");
	     if(Utility.testString(seriesId)){
			 sbSql.append("  AND tpiv.SERIES_ID in ("+seriesId+")  \n" );
	      }
	     if(Utility.testString(userDealerId)){
			 sbSql.append("  AND tpc.dealer_id='"+userDealerId+"'  \n" );
	      }	
	     
	     sbSql.append(" ) A,TC_USER tu,VW_ORG_DEALER_ALL_new vw,t_Pc_Link_Man tplm  where  A.ADVISER = tu.USER_ID and A.dealer_id=vw.dealer_id ");
		    
	    sbSql.append(" and A.customer_id=tplm.ctm_id "); 
	     
	     if(Utility.testString(startDate)){
	    	 sbSql.append("  AND   A.order_date   >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     if(Utility.testString(endDate)){
	    	 sbSql.append("  AND   A.order_date   <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS') \n");
	       }
	     
		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and A.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and A.telephone like '%"+telephone+"%' ");
		}
		if(Utility.testString(orderStatus)){//订单状态
			sbSql.append(" and A.task_Status in ("+orderStatus+") \n");
		}
		/*
		if(Utility.testString(vin)){//车架号VIN
			sbSql.append(" and M.vin like '%"+vin+"%' \n");
		}
		*/
		
		if("10001".equals(beStatus)){
		    sbSql.append(" and tplm.relation_code='60581001' "); 
		 }else if("10002".equals(beStatus)){
			sbSql.append(" and tplm.relation_code='60581002' "); 
		 }else if("10003".equals(beStatus)){
			sbSql.append(" and tplm.relation_code in ('60581001','60581002') "); 
		 }
		if(Utility.testString(adviser)){
			sbSql.append(" and A.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and A.adviser in ('"+adviserId+"') \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and tu.group_id = '"+groupId+"' \n");
		}
		/*
		sbSql.append(" order by decode(a.order_status,60231003,1,60231004,2,3),order_date desc \n");
		*/
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	
	/*
	 * 获取客户基本信息
	 */
	public List<DynaBean> getCustomerInfo(String orderId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT A.CUSTOMER_CODE,A.CUSTOMER_ID,\r\n");
		sql.append("       A.CUSTOMER_NAME,\r\n");
		sql.append("       A.TELEPHONE,\r\n");
		sql.append("       A.ADDRESS,\r\n");
		sql.append("       B.CODE_DESC AS PAPER_TYPE,\r\n");
		sql.append("       A.PAPER_NO,\r\n");
		sql.append("       C.ORDER_ID\r\n");
		sql.append("  FROM T_PC_CUSTOMER A\r\n");
		sql.append("  LEFT JOIN T_PC_ORDER C ON A.CUSTOMER_ID = C.CUSTOMER_ID\r\n");
		sql.append("  LEFT JOIN TC_CODE B ON A.PAPER_TYPE = B.CODE_ID\r\n");
		sql.append(" WHERE 1=1 \r\n"); 
		sql.append("   AND C.ORDER_ID = "+orderId+"");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取原订单状态
	 */
	public List<DynaBean> getOldOrderStatus(String orderId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT A.ORDER_ID, B.TASK_STATUS\r\n");
		sql.append("  FROM T_PC_ORDER A, T_PC_ORDER_DETAIL B\r\n");
		sql.append(" WHERE A.ORDER_ID = B.ORDER_ID\r\n");
		sql.append("   AND B.DELIVERY_NUMBER > 0 AND B.TASK_STATUS IN('60171001','60171002')"); 
		sql.append("   AND A.ORDER_ID = "+orderId+"");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 判断订单是否完成
	 */
	public List<DynaBean> getOrderComplete(String orderId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT A.ORDER_ID, B.TASK_STATUS\r\n");
		sql.append("  FROM T_PC_ORDER A, T_PC_ORDER_DETAIL B\r\n");
		sql.append(" WHERE A.ORDER_ID = B.ORDER_ID\r\n");
		sql.append("   AND B.TASK_STATUS = "+Constant.TASK_STATUS_01+""); 
		sql.append("   AND A.ORDER_ID = "+orderId+"");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取订单审核详细表数据
	 */
	public List<DynaBean> getOrderDetailAudit(String orderId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT * FROM t_pc_order_detail_audit A WHERE A.ORDER_ID ='"+orderId+"' AND A.STATUS = "+Constant.STATUS_ENABLE+"");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	/*
	 * 更新订单详细表中不包含指定任务状态的数据
	 */
	public List<DynaBean> getOrderDetail(String orderId, Integer taskStus) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT * FROM t_pc_order_detail A WHERE A.ORDER_ID ='"+orderId+"' AND A.TASK_STATUS <> "+taskStus);
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取订单审核信息
	 */
	public List<DynaBean> getOrderAuditInfo(String orderId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT A.REASON_REMARK,A.OLD_CUSTOMER_NAME,A.OLD_TELEPHONE,A.OLD_VEHICLE_ID,A.OLD_RELATION_CODE \r\n");
		sql.append("  FROM T_PC_ORDER_AUDIT A\r\n");
		sql.append(" WHERE 1=1 AND A.MANAGER_AUDIT="+Constant.DIRECTOR_AUDIT_01+" \r\n"); 
		sql.append("   AND A.ORDER_ID = "+orderId+"");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	/**
	 * 查询订单需要打印的信息
	 * @param map 传入的参数
	 * @return
	 */
	public List<Map<String, Object>> getPrintOrderList(Map<String, String> map) {
		String orderId=map.get("orderId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tpo.order_id, tpod.order_detail_id,tpo.owner_name,tpo.owner_address,tpo.delivery_address,tpod.num,tpod.price,tpod.deposit\n" );
		sql.append("  from t_pc_order tpo, t_pc_order_detail tpod,t_pc_customer tpc\n" );
		sql.append(" where tpo.order_id = tpod.order_id\n" );
		sql.append(" and tpc.customer_id=tpo.customer_id\n" );
		sql.append(" and tpo.order_id="+orderId);
		sql.append("   and rownum = 1");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
}
