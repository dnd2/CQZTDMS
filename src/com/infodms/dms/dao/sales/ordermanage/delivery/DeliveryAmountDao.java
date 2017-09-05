package com.infodms.dms.dao.sales.ordermanage.delivery;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DeliveryAmountDao extends BaseDao<PO>{
private static final DeliveryAmountDao dao = new DeliveryAmountDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final DeliveryAmountDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 查询 经销商申请数量
	 * @param map
	 * @return
	 */
	public PageResult<Map<String, Object>> deliveryAmountQuery(Map<String, String> map, int curPage, int pageSize){
		String orderNo=map.get("orderNo");
		String dlvryNo=map.get("dlvryNo");
		String start_date=map.get("start_date");
		String end_date=map.get("end_date");
		String dealerCode=map.get("dealerCode");
		String dealerId=map.get("dealerId");
		String dutyType=map.get("dutyType");
		String orgId=map.get("orgId");
		StringBuilder sql= new StringBuilder();
		List<Object> params = new LinkedList<Object>();
		sql.append(" select *  \n");
	    sql.append(" from (select VW.DEALER_ID, \n");
	    sql.append(" VW.ROOT_ORG_NAME, \n");
	    sql.append(" VW.PQ_ORG_NAME, \n");
	    sql.append(" VW.DEALER_CODE, \n");
	    sql.append(" VW.DEALER_SHORTNAME, \n");
	    sql.append(" tvdr.dlvry_req_no, \n");
	    sql.append(" tvs.order_no, \n");
	    sql.append(" tm.material_code, \n");
	    sql.append(" tm.material_name, \n");
	    sql.append("   nvl(tvdrd.dealer_req_amount,0)-nvl(tvdrd.reserve_amount,0) amount, \n");
	    //sql.append(" tvdrd.reserve_amount, \n");
	    sql.append(" to_char(tvdr.F_AUDIT_TIME,'YYYY/MM/DD') AUDIT_TIME, \n");
	    sql.append(" tvdr.req_status \n");
	    sql.append(" from tt_vs_dlvry_req       tvdr, \n");
	    sql.append("  tt_vs_dlvry_req_dtl   tvdrd, \n");
	    sql.append("  tt_vs_order           tvs, \n");
	    sql.append("  tm_vhcl_material      tm, \n");
	    sql.append("  VW_ORG_DEALER_ALL_NEW vw \n");
	    sql.append(" where tvdr.req_id = tvdrd.req_id \n");
	    sql.append("  and tvdr.order_id = tvs.order_id \n");
	    sql.append("  and tvs.order_org_id = vw.dealer_id \n");
	    sql.append("  and tm.material_id=tvdrd.material_id \n");
	    sql.append("  and tvdr.req_status = 11571014 \n");
	    sql.append("  and tvdrd.dealer_req_amount is not null \n");
	    sql.append(" and tvdrd.dealer_req_amount <> tvdrd.reserve_amount \n");
	    sql.append(" union \n");
	    sql.append("  select VW.DEALER_ID, \n");
	    sql.append("  VW.ROOT_ORG_NAME, \n");
	    sql.append("  VW.PQ_ORG_NAME, \n");
	    sql.append("  VW.DEALER_CODE, \n");
	    sql.append("   VW.DEALER_SHORTNAME, \n");
	    sql.append("  tvdr.dlvry_req_no, \n");
	    sql.append("   tvs.order_no, \n");
	    sql.append("   tm.material_code, \n");
	    sql.append("   tm.material_name, \n");
	    sql.append("   nvl(tvdrd.dealer_req_amount,0)-nvl(tvdrd.reserve_amount,0) amount, \n");
	   // sql.append("  tvdrd.reserve_amount, \n");
	    sql.append("  to_char(tvdr.F_AUDIT_TIME,'YYYY/MM/DD') AUDIT_TIME, \n");
	    sql.append("  tvdr.req_status \n");
	    sql.append("  from tt_vs_dlvry_req       tvdr, \n");
	    sql.append("   tt_vs_dlvry_req_dtl   tvdrd, \n");
	    sql.append("  tt_vs_order           tvs, \n");
	    sql.append("  tm_vhcl_material      tm, \n");
	    sql.append("   VW_ORG_DEALER_ALL_NEW vw \n");
	    sql.append("  where tvdr.req_id = tvdrd.req_id \n");
	    sql.append("  and tvdr.order_id = tvs.order_id \n");
	    sql.append("  and tvs.order_org_id = vw.dealer_id \n");
	    sql.append("  and tm.material_id=tvdrd.material_id \n");
	    sql.append("  and tvdr.req_status = 11571007 and tvdrd.dealer_req_amount is not null  \n");
	    sql.append("  ) tpc where 1=1  \n");
	   
				if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
		 			sql.append("  AND tpc.DEALER_ID ="+dealerId+"\n");
		 			//大区
		 		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
		 			
		 			sql.append(" AND tpc.DEALER_ID IN (SELECT V.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW V WHERE  V.ROOT_ORG_ID="+orgId+")\n");
		 			//小区
		 		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
		 			sql.append(" AND tpc.DEALER_ID IN (SELECT V.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW V WHERE  V.PQ_ORG_ID="+orgId+") \n");
		 		}
				if(!"".equals(dealerCode)&&dealerCode!=null){
		            String arr[]=dealerCode.split(",");
		            String str="";
		            
		            for(int i=0;i<arr.length;i++){
		                str+="'"+arr[i]+"',";
		            }
		            str=str.substring(0,str.lastIndexOf(","));

		            sql.append("     and  tpc.dealer_code in ("+str+") \n");
				}
				if(!CommonUtils.isNullString(start_date)){
		            sql.append("   and to_date(tpc.AUDIT_TIME,'YYYY/MM/DD') >=to_date('"+start_date.replace("-", "/")+"','YYYY/MM/DD') \n" );
				 	}
				if(!CommonUtils.isNullString(end_date)){
		            sql.append("   and to_date(tpc.AUDIT_TIME,'YYYY/MM/DD') <=to_date('"+end_date.replace("-", "/")+"','YYYY/MM/DD') \n" );
				 	}
				if(!CommonUtils.isNullString(dlvryNo)){
		            sql.append("   and tpc.dlvry_req_no like '%"+dlvryNo+"%' \n" );
				 	}
				if(!CommonUtils.isNullString(orderNo)){
		            sql.append("   and tpc.order_no like '%"+orderNo+"%' \n" );
				 	}
				 sql.append("  order by audit_time desc  \n");
				PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
				return ps;
	}
	
	//经销商
	  public List<Map<String,Object>> getNameList(String codeID){
	        StringBuilder sql=new StringBuilder();
	        sql.append("select code_id,code_desc from tc_code a where 1=1 "); 
	        sql.append("   and a.status='10011001' "); 
	        if(!CommonUtils.isNullString(codeID))
	        {
	        	sql.append(" and  type='"+codeID+"' ");
	        	
	        }else{
	        	
	        	sql.append(" and  type in ('6015','6016') "); 
	        	
	        }
	        sql.append(" order by code_id");
	        return dao.pageQuery(sql.toString(),null,null);
	   }
	  //公司
	  public List<Map<String,Object>> getNameCompanyList(String codeID){
		  StringBuilder sql=new StringBuilder();
	        sql.append("select code_id,code_desc from tc_code a where 1=1 "); 
	        sql.append("   and a.status='10011001' "); 
	        if(!CommonUtils.isNullString(codeID))
	        {
	        	sql.append(" and  type in ('"+codeID+"') ");
	        	
	        }else{
	        	
	        	sql.append(" and  type in ('60151001','6016') "); 
	        	
	        }
	        sql.append(" order by code_id");
	        return dao.pageQuery(sql.toString(),null,null);
	   }
	  //合作经销商
	  public PageResult<Map<String, Object>> getDealerCode(Map<String, String> map, Integer pageSize, Integer curPage){
		  StringBuilder sql=new StringBuilder();
		  String userId=map.get("userId");
		  String dealer_id=map.get("dealerId");
		  TcUserPO tc=new TcUserPO();
		  tc.setUserId(Long.parseLong(userId));
		  tc=(TcUserPO) dao.select(tc).get(0);
		  String poseRank=tc.getPoseRank().toString();
		  sql.append("    select b.par_dealer_id,b.dealer_codes,vw.DEALER_SHORTNAME  from t_pc_company_group b LEFT JOIN VW_ORG_DEALER_ALL VW  ");
		  sql.append(" on b.dealer_codes=vw.DEALER_CODE  where 1=1  ");
		  if(CommonUtils.judgeMgrLogin(userId) || "60281001".equals(poseRank)){//ture 为经理 false 
			  sql.append(" and  b.par_dealer_id='"+dealer_id+"'  ");
		  }else {
			  sql.append(" and  b.par_dealer_id='-1'  ");
		  }
		  PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		  return ps;
	  }
	
}
