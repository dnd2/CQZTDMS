package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtIfStandardAuditPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.UserProvinceRelation;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 
 *
 * @Description:InfoFrame3.0.V01
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-17
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class StandardVipApplyManagerDao extends BaseDao {
	
	public static Logger logger = Logger.getLogger(StandardVipApplyManagerDao.class);
	
	protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//分页查询申请表
	public PageResult<Map<String, Object>> queryStandardVip(StandardVipApplyManagerBean smb,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.id,A.ORDER_ID, A.VIN, A.ST_ACTION,A.ST_TYPE, ");
		sql.append("	   TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,A.ST_STATUS,C.GROUP_NAME ");
		sql.append("FROM TT_IF_STANDARD A,TM_VEHICLE B, ");
		sql.append("	 TM_VHCL_MATERIAL_GROUP C ");
		sql.append("WHERE A.VIN = B.VIN ");
		sql.append("AND B.SERIES_ID = C.GROUP_ID ");
		if(!smb.getOrderId().equals("")){
			sql.append("AND A.ORDER_ID like '%").append(smb.getOrderId()).append("%' ");
		}
		if(!smb.getVin().equals("")){
			sql.append("AND A.VIN like '%").append(smb.getVin()).append("%' ");
		}
		if(!smb.getStType().equals("")){
			sql.append("AND A.ST_TYPE = ").append(smb.getStType()).append(" ");
		}
		if(!smb.getStAction().equals("")){
			sql.append("AND A.ST_ACTION = ").append(smb.getStAction()).append(" ");
		}
		if(!smb.getBeginTime().equals("")){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(smb.getBeginTime()).append("','YYYY-MM-DD hh24:mi:ss') ");
		}
		if(!smb.getEndTime().equals("")){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(smb.getEndTime()).append("','YYYY-MM-DD hh24:mi:ss') ");
		}
		if(!smb.getVehicleModel().equals("")){
			sql.append("AND C.GROUP_ID = ").append(smb.getVehicleModel()).append(" ");
		}
		sql.append("AND A.ST_STATUS IN (").append(Constant.SERVICEINFO_VIP_STATUS_UNREPORT).append(",").append(Constant.SERVICEINFO_VIP_AREA_STATUS_REJECT).append(",").append(Constant.SERVICEINFO_VIP_SERVICE_STATUS_REJECT).append(") ");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append(" ");
		sql.append("AND A.DEALER_ID = ").append(smb.getDealerId()).append(" ");
		sql.append("ORDER BY A.CREATE_DATE DESC ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询车辆信息
	public PageResult<Map<String, Object>> queryVIN(StandardVipApplyManagerBean smb,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT B.ENGINE_NO, B.COLOR, B.VIN, B.HISTORY_MILE, C.GROUP_NAME, ");
		sql.append("	   TO_CHAR(B.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE, ");
		sql.append("	   TO_CHAR(B.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE, ");
		//sql.append("	   D.CUSTOMER_NAME, D.CERT_NO, D.MOBILE, D.ADDRESS_DESC ");
		sql.append(" TC.CTM_NAME AS CUSTOMER_NAME,TC.CARD_NUM AS CERT_NO,TC.MAIN_PHONE AS MOBILE,TC.ADDRESS AS ADDRESS_DESC ");
		sql.append("FROM TM_VEHICLE B,TM_VHCL_MATERIAL_GROUP C, TT_DEALER_ACTUAL_SALES D,TT_CUSTOMER TC ");
		sql.append("WHERE B.SERIES_ID = C.GROUP_ID ");
		sql.append("AND B.VEHICLE_ID = D.VEHICLE_ID ");
		sql.append("AND D.CTM_ID = TC.CTM_ID ");
		if(!smb.getVin().equals("")){
			sql.append("AND B.VIN LIKE '%").append(smb.getVin()).append("%' ");
		}
		if(!smb.getCustomerName().equals("")){
			sql.append("AND D.CUSTOMER_NAME LIKE '%").append(smb.getCustomerName()).append("%' ");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//根据VIN查询车辆信息
	public PageResult<Map<String, Object>> queryVchByVIN(String vin){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT B.ENGINE_NO, B.COLOR, B.VIN, B.HISTORY_MILE, C.GROUP_NAME, ");
		sql.append("	   TO_CHAR(B.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE, ");
		sql.append("	   TO_CHAR(B.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE, ");
		//sql.append("	   D.CUSTOMER_NAME, D.CERT_NO, D.MOBILE, D.ADDRESS_DESC ");
		sql.append(" TC.CTM_NAME AS CUSTOMER_NAME,TC.CARD_NUM AS CERT_NO,TC.MAIN_PHONE AS MOBILE,TC.ADDRESS AS ADDRESS_DESC ");
		sql.append("FROM TM_VEHICLE B,TM_VHCL_MATERIAL_GROUP C, TT_DEALER_ACTUAL_SALES D,TT_CUSTOMER TC ");
		sql.append("WHERE B.SERIES_ID = C.GROUP_ID ");
		sql.append("AND B.VEHICLE_ID = D.VEHICLE_ID(+) ");
		sql.append("AND D.CTM_ID = TC.CTM_ID(+) ");
		sql.append("AND B.VIN ='").append(vin).append("' ");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), 5,1);
		return ps;
	}
	
	//分页查询申请表(经销商端)
	public PageResult<Map<String, Object>> queryDealerStandardVip(StandardVipApplyManagerBean smb,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ORDER_ID, A.VIN, A.ST_ACTION,A.ST_TYPE, ");
		sql.append("	   TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,A.ST_STATUS,C.GROUP_NAME ");
		sql.append("FROM TT_IF_STANDARD A,TM_VEHICLE B, ");
		sql.append("	 TM_VHCL_MATERIAL_GROUP C ");
		sql.append("WHERE A.VIN = B.VIN ");
		sql.append("AND B.SERIES_ID = C.GROUP_ID ");
		if(!smb.getOrderId().equals("")){
			sql.append("AND A.ORDER_ID like '%").append(smb.getOrderId()).append("%' ");
		}
		if(!smb.getVin().equals("")){
			sql.append("AND A.VIN like '%").append(smb.getVin()).append("%' ");
		}
		if(!smb.getStType().equals("")){
			sql.append("AND A.ST_TYPE = ").append(smb.getStType()).append(" ");
		}
		if(!smb.getStAction().equals("")){
			sql.append("AND A.ST_ACTION = ").append(smb.getStAction()).append(" ");
		}
		if(!smb.getBeginTime().equals("")){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(smb.getBeginTime()).append("','YYYY-MM-DD hh24:mi:ss') ");
		}
		if(!smb.getEndTime().equals("")){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(smb.getEndTime()).append("','YYYY-MM-DD hh24:mi:ss') ");
		}
		if(!smb.getVehicleModel().equals("")){
			sql.append("AND C.GROUP_ID = ").append(smb.getVehicleModel()).append(" ");
		}
		sql.append("AND A.ST_STATUS <> ").append(Constant.SERVICEINFO_VIP_STATUS_UNREPORT).append(" ");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append(" ");
		sql.append("AND A.DEALER_ID = ").append(smb.getDealerId()).append(" ");
		sql.append("ORDER BY A.CREATE_DATE DESC ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询大区审批列表
	public PageResult<Map<String, Object>> queryStandardVipApprove(Long userId,StandardVipApplyManagerBean smb,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("with das as (select tdas.vehicle_id,tc.ctm_name from tt_customer tc,tt_dealer_actual_sales tdas where tdas.ctm_id(+) = tc.ctm_id)\n");
		sql.append("select s.ORDER_ID, s.VIN, s.ST_ACTION,s.ST_TYPE, ");
		sql.append("	   TO_CHAR(s.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,s.ST_STATUS,g.GROUP_NAME, ");
		sql.append("	   D.DEALER_CODE, D.DEALER_NAME, das.CTM_NAME AS CUSTOMER_NAME ");  
		sql.append("  from tt_if_standard         s,\n");  
		sql.append("       tm_dealer              d,\n");  
		sql.append("       tm_vehicle             v,\n");    
		sql.append("       das,\n");  
		sql.append("       tm_dealer_org_relation r,\n");  
		sql.append("       tm_vhcl_material_group g\n");  
		sql.append(" where 1=1\n");  
		sql.append("   and s.dealer_id = d.dealer_id\n");  
		sql.append("   and s.vin = v.vin\n");  
		sql.append("   and v.series_id = g.group_id\n");  
		sql.append("   and s.dealer_id = r.dealer_id\n");  
		sql.append("   and das.vehicle_id(+) = v.vehicle_id\n");  
		if(!smb.getOrderId().equals("")){
			sql.append("AND s.ORDER_ID like '%").append(smb.getOrderId()).append("%'\n");
		}
		if(!smb.getVin().equals("")){
			sql.append("AND s.VIN like '%").append(smb.getVin()).append("%'\n");
		}
		if(!smb.getStType().equals("")){
			sql.append("AND s.ST_TYPE = ").append(smb.getStType()).append("\n");
		}
		if(!smb.getStAction().equals("")){
			sql.append("AND s.ST_ACTION = ").append(smb.getStAction()).append("\n");
		}
		if(!smb.getBeginTime().equals("")){
			sql.append("AND s.CREATE_DATE >= TO_DATE('").append(smb.getBeginTime()).append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		if(!smb.getEndTime().equals("")){
			sql.append("AND s.CREATE_DATE <= TO_DATE('").append(smb.getEndTime()).append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		if(null!=smb.getCompanyId()&&!"".equals(smb.getCompanyId())){
			sql.append("AND s.company_id = "+smb.getCompanyId()).append("\n");
		}
		if(!smb.getVehicleModel().equals("")){
			sql.append("AND v.GROUP_ID = ").append(smb.getVehicleModel()).append("\n");
		}
		if(!smb.getDealerName().equals("")){
			sql.append("AND D.DEALER_NAME LIKE '%").append(smb.getDealerName()).append("%'\n");
		}
		if(!smb.getDealerCode().equals("")){
			sql.append("AND D.DEALER_CODE IN (").append(smb.getDealerCode()).append(") ");
		}
		sql.append("AND s.ST_STATUS = ").append(Constant.SERVICEINFO_VIP_STATUS_REPORTED).append("\n");
		sql.append("AND s.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		if(!Constant.DUTY_TYPE_DEPT.toString().equals(smb.getDutyType())){
			sql.append("AND r.ORG_ID IN ( ");
			sql.append(" SELECT ORG_ID FROM TM_ORG ");
			sql.append(" START WITH ORG_ID = ").append(smb.getOrgId()); 
			sql.append(" CONNECT BY PRIOR  ORG_ID = PARENT_ORG_ID ) ");
		}
		sql.append(UserProvinceRelation.getDealerIds(userId, "d"));
		sql.append(" order by s.order_id desc");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询申请（车厂端）
	public PageResult<Map<String, Object>> queryFinalStandardVip(StandardVipApplyManagerBean smb,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("\n");
		List<Object> params = new LinkedList<Object>();
		/*sql.append("SELECT A.ORDER_ID, A.VIN, A.ST_ACTION,A.ST_TYPE, ");
		sql.append("	   TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,A.ST_STATUS,C.GROUP_NAME, ");
		//sql.append("	   D.DEALER_CODE, D.DEALER_NAME, F.CUSTOMER_NAME ");
		sql.append("	   D.DEALER_CODE, D.DEALER_NAME, TC.CTM_NAME AS CUSTOMER_NAME ");
		sql.append("FROM TT_IF_STANDARD A,TM_VEHICLE B, ");
		sql.append("	 TM_VHCL_MATERIAL_GROUP C, TM_DEALER D, ");
		sql.append("	 TM_DEALER_ORG_RELATION E, ");
		sql.append("	 TT_DEALER_ACTUAL_SALES F, ");
		sql.append("	 TT_CUSTOMER TC ");
		sql.append("WHERE A.VIN = B.VIN ");
		sql.append("AND B.SERIES_ID = C.GROUP_ID ");
		sql.append("AND A.DEALER_ID = D.DEALER_ID ");
		sql.append("AND A.DEALER_ID = E.DEALER_ID ");
		sql.append("AND B.VEHICLE_ID = F.VEHICLE_ID(+) ");
		sql.append("AND TC.CTM_ID = F.CTM_ID ");
		if(!smb.getOrderId().equals("")){
			sql.append("AND A.ORDER_ID like '%").append(smb.getOrderId()).append("%' ");
		}
		if(!smb.getVin().equals("")){
			sql.append("AND A.VIN like '%").append(smb.getVin()).append("%' ");
		}
		if(!smb.getStType().equals("")){
			sql.append("AND A.ST_TYPE = ").append(smb.getStType()).append(" ");
		}
		if(!smb.getStAction().equals("")){
			sql.append("AND A.ST_ACTION = ").append(smb.getStAction()).append(" ");
		}
		if(!smb.getBeginTime().equals("")){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(smb.getBeginTime()).append("','YYYY-MM-DD hh24:mi:ss') ");
		}
		if(!smb.getEndTime().equals("")){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(smb.getEndTime()).append("','YYYY-MM-DD hh24:mi:ss') ");
		}
		if(!smb.getVehicleModel().equals("")){
			sql.append("AND C.GROUP_ID = ").append(smb.getVehicleModel()).append(" ");
		}
		if(!smb.getDealerName().equals("")){
			sql.append("AND D.DEALER_NAME LIKE '%").append(smb.getDealerName()).append("%' ");
		}
		if(null!=smb.getCompanyId()&&!"".equals(smb.getCompanyId())){
			sql.append(" AND A.company_id = "+smb.getCompanyId());
		}
		if(Utility.testString(smb.getDealerCode())){//经销商代码
			sql.append(Utility.getConSqlByParamForEqual(smb.getDealerCode(), params, "D", "dealer_code")); 
		}
		sql.append("AND A.ST_STATUS <> ").append(Constant.SERVICEINFO_VIP_STATUS_UNREPORT).append(" ");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append(" ");
		sql.append("AND E.ORG_ID IN ( ");
		sql.append(" SELECT ORG_ID FROM TM_ORG ");
		sql.append(" START WITH ORG_ID = ").append(smb.getOrgId()); 
		sql.append(" CONNECT BY PRIOR  ORG_ID = PARENT_ORG_ID ) ");
		sql.append("ORDER BY A.CREATE_DATE DESC ");*/
		sql.append("with das as (select tdas.vehicle_id,tc.ctm_name from tt_customer tc,tt_dealer_actual_sales tdas where tdas.ctm_id(+) = tc.ctm_id)\n");
		sql.append("select s.ORDER_ID, s.VIN, s.ST_ACTION,s.ST_TYPE, ");
		sql.append("	   TO_CHAR(s.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,s.ST_STATUS,g.GROUP_NAME, ");
		//sql.append("	   D.DEALER_CODE, D.DEALER_NAME, F.CUSTOMER_NAME ");
		sql.append("	   D.DEALER_CODE, D.DEALER_NAME, das.CTM_NAME AS CUSTOMER_NAME ");  
		sql.append("  from tt_if_standard         s,\n");  
		sql.append("       tm_dealer              d,\n");  
		sql.append("       tm_vehicle             v,\n");    
		sql.append("       das,\n");  
		sql.append("       tm_dealer_org_relation r,\n");  
		sql.append("       tm_vhcl_material_group g\n");  
		sql.append(" where 1=1\n");  
		sql.append("   and s.dealer_id = d.dealer_id\n");  
		sql.append("   and s.vin = v.vin\n");  
		sql.append("   and v.series_id = g.group_id\n");  
		sql.append("   and s.dealer_id = r.dealer_id\n");  
		sql.append("   and das.vehicle_id(+) = v.vehicle_id\n");  
		if(!smb.getOrderId().equals("")){
			sql.append("AND s.ORDER_ID like '%").append(smb.getOrderId()).append("%'\n");
		}
		if(!smb.getVin().equals("")){
			sql.append("AND s.VIN like '%").append(smb.getVin()).append("%'\n");
		}
		if(!smb.getStType().equals("")){
			sql.append("AND s.ST_TYPE = ").append(smb.getStType()).append("\n");
		}
		if(!smb.getStAction().equals("")){
			sql.append("AND s.ST_ACTION = ").append(smb.getStAction()).append("\n");
		}
		if(!smb.getBeginTime().equals("")){
			sql.append("AND s.CREATE_DATE >= TO_DATE('").append(smb.getBeginTime()).append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		if(!smb.getEndTime().equals("")){
			sql.append("AND s.CREATE_DATE <= TO_DATE('").append(smb.getEndTime()).append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		if(null!=smb.getCompanyId()&&!"".equals(smb.getCompanyId())){
			sql.append("AND s.company_id = "+smb.getCompanyId()).append("\n");
		}
		if(!smb.getVehicleModel().equals("")){
			sql.append("AND g.GROUP_ID = ").append(smb.getVehicleModel()).append("\n");
		}
		if(!smb.getDealerName().equals("")){
			sql.append("AND D.DEALER_NAME LIKE '%").append(smb.getDealerName()).append("%'\n");
		}
		if(Utility.testString(smb.getDealerCode())){//经销商代码
			sql.append("and d.dealer_code in ('").append(smb.getDealerCode().replace(",","','")).append("')\n"); 
		}
		sql.append("AND s.ST_STATUS <> ").append(Constant.SERVICEINFO_VIP_STATUS_UNREPORT).append("\n");
		sql.append("AND s.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		if(!Constant.DUTY_TYPE_DEPT.toString().equals(smb.getDutyType())){
			sql.append("AND r.ORG_ID IN ( ");
			sql.append(" SELECT ORG_ID FROM TM_ORG ");
			sql.append(" START WITH ORG_ID = ").append(smb.getOrgId()); 
			sql.append(" CONNECT BY PRIOR  ORG_ID = PARENT_ORG_ID ) ");
		}
		sql.append(" order by s.order_id desc");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//分页查询售后服务部审批列表
	public PageResult<Map<String, Object>> queryStandardVipFinalApprove(StandardVipApplyManagerBean smb,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer("\n");
		List<Object> params = new LinkedList<Object>();
		/*sql.append("SELECT A.ORDER_ID, A.VIN, A.ST_ACTION,A.ST_TYPE, ");
		sql.append("	   TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,A.ST_STATUS,C.GROUP_NAME, ");
		//sql.append("	   D.DEALER_CODE, D.DEALER_NAME, F.CUSTOMER_NAME ");
		sql.append("	   D.DEALER_CODE, D.DEALER_NAME, TC.CTM_NAME AS CUSTOMER_NAME ");
		sql.append("FROM TT_IF_STANDARD A,TM_VEHICLE B, ");
		sql.append("	 TM_VHCL_MATERIAL_GROUP C, TM_DEALER D, ");
		sql.append("	 TT_DEALER_ACTUAL_SALES F, ");
		sql.append(" 	 TT_CUSTOMER TC ");
		sql.append("WHERE A.VIN = B.VIN ");
		sql.append("AND B.SERIES_ID = C.GROUP_ID ");
		sql.append("AND A.DEALER_ID = D.DEALER_ID ");
		sql.append("AND B.VEHICLE_ID = F.VEHICLE_ID(+) ");
		sql.append("AND TC.CTM_ID = F.CTM_ID ");
		if(!smb.getOrderId().equals("")){
			sql.append("AND A.ORDER_ID like '%").append(smb.getOrderId()).append("%' ");
		}
		if(!smb.getVin().equals("")){
			sql.append("AND A.VIN like '%").append(smb.getVin()).append("%' ");
		}
		if(!smb.getStType().equals("")){
			sql.append("AND A.ST_TYPE = ").append(smb.getStType()).append(" ");
		}
		if(Utility.testString(smb.getDealerCode())){//经销商代码
			sql.append("and d.dealer_code in ('").append(smb.getDealerCode().replace(",","','")).append("')\n"); 
		}
		if(!smb.getStAction().equals("")){
			sql.append("AND A.ST_ACTION = ").append(smb.getStAction()).append(" ");
		}
		if(!smb.getBeginTime().equals("")){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(smb.getBeginTime()).append("','YYYY-MM-DD hh24:mi:ss') ");
		}
		if(!smb.getEndTime().equals("")){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(smb.getEndTime()).append("','YYYY-MM-DD hh24:mi:ss') ");
		}
		if(!smb.getVehicleModel().equals("")){
			sql.append("AND C.GROUP_ID = ").append(smb.getVehicleModel()).append(" ");
		}
		if(null!=smb.getCompanyId()&&!"".equals(smb.getCompanyId())){
			sql.append("AND A.company_id = "+smb.getCompanyId());
		}
		if(!smb.getDealerName().equals("")){
			sql.append("AND D.DEALER_NAME LIKE '%").append(smb.getDealerName()).append("%' ");
		}
		sql.append("AND A.ST_STATUS = ").append(Constant.SERVICEINFO_VIP_AREA_STATUS_PASS).append(" ");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append(" ");
		sql.append("ORDER BY A.CREATE_DATE DESC ");*/
		sql.append("with das as (select tdas.vehicle_id,tc.ctm_name from tt_customer tc,tt_dealer_actual_sales tdas where tdas.ctm_id(+) = tc.ctm_id)\n");
		sql.append("select s.ORDER_ID, s.VIN, s.ST_ACTION,s.ST_TYPE, ");
		sql.append("	   TO_CHAR(s.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE,s.ST_STATUS,g.GROUP_NAME, ");
		//sql.append("	   D.DEALER_CODE, D.DEALER_NAME, F.CUSTOMER_NAME ");
		sql.append("	   D.DEALER_CODE, D.DEALER_NAME, das.CTM_NAME AS CUSTOMER_NAME ");  
		sql.append("  from tt_if_standard         s,\n");  
		sql.append("       tm_dealer              d,\n");  
		sql.append("       tm_vehicle             v,\n");    
		sql.append("       das,\n");  
		sql.append("       tm_dealer_org_relation r,\n");  
		sql.append("       tm_vhcl_material_group g\n");  
		sql.append(" where 1=1\n");  
		sql.append("   and s.dealer_id = d.dealer_id\n");  
		sql.append("   and s.vin = v.vin\n");  
		sql.append("   and v.series_id = g.group_id\n");  
		sql.append("   and s.dealer_id = r.dealer_id\n");  
		sql.append("   and das.vehicle_id(+) = v.vehicle_id\n");  
		if(!smb.getOrderId().equals("")){
			sql.append("AND s.ORDER_ID like '%").append(smb.getOrderId()).append("%'\n");
		}
		if(!smb.getVin().equals("")){
			sql.append("AND s.VIN like '%").append(smb.getVin()).append("%'\n");
		}
		if(!smb.getStType().equals("")){
			sql.append("AND s.ST_TYPE = ").append(smb.getStType()).append("\n");
		}
		if(!smb.getStAction().equals("")){
			sql.append("AND s.ST_ACTION = ").append(smb.getStAction()).append("\n");
		}
		if(!smb.getBeginTime().equals("")){
			sql.append("AND s.CREATE_DATE >= TO_DATE('").append(smb.getBeginTime()).append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		if(!smb.getEndTime().equals("")){
			sql.append("AND s.CREATE_DATE <= TO_DATE('").append(smb.getEndTime()).append("','YYYY-MM-DD hh24:mi:ss')\n");
		}
		if(null!=smb.getCompanyId()&&!"".equals(smb.getCompanyId())){
			sql.append("AND s.company_id = "+smb.getCompanyId()).append("\n");
		}
		if(!smb.getVehicleModel().equals("")){
			sql.append("AND g.GROUP_ID = ").append(smb.getVehicleModel()).append("\n");
		}
		if(!smb.getDealerName().equals("")){
			sql.append("AND D.DEALER_NAME LIKE '%").append(smb.getDealerName()).append("%'\n");
		}
		if(Utility.testString(smb.getDealerCode())){//经销商代码
			sql.append("and d.dealer_code in ('").append(smb.getDealerCode().replace(",","','")).append("')\n"); 
		}
		sql.append("AND s.ST_STATUS = ").append(Constant.SERVICEINFO_VIP_AREA_STATUS_PASS).append("\n");
		sql.append("AND s.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		/*if(!Constant.DUTY_TYPE_DEPT.toString().equals(smb.getDutyType())){
			sql.append("AND r.ORG_ID IN ( ");
			sql.append(" SELECT ORG_ID FROM TM_ORG ");
			sql.append(" START WITH ORG_ID = ").append(smb.getOrgId()); 
			sql.append(" CONNECT BY PRIOR  ORG_ID = PARENT_ORG_ID ) ");
		}*/
		sql.append(" order by s.order_id desc");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//提报申请
	public void putFOrwordStandardVip(TtIfStandardPO po, TtIfStandardPO tp, TtIfStandardAuditPO ap){
		update(po,tp);   //修改业务表状态
		ap.setId(Long.parseLong(SequenceManager.getSequence("")));
		insert(ap);     //在审批明细表里添加log数据
	}
	
	//删除申请
	public void deleteStandardVip(TtIfStandardPO po, TtIfStandardPO tp){
		update(po,tp);
	}
	
	//添加申请
	public String insertStandderVip(TtIfStandardPO po){
		po.setOrderId(SequenceManager.getSequence("HVAO"));
		String activityId =SequenceManager.getSequence(""); 
		po.setId(Long.parseLong(activityId));
		insert(po);		//插入TT_IF_STANDARD
		return activityId;
	}
	
	//大区审核操作
	public void insertApprovePass(TtIfStandardAuditPO po){
		po.setId(Long.parseLong(SequenceManager.getSequence("")));
		insert(po);		//插入TT_IF_STANDARD_AUDIT
	}
	
	//更新申请
	public void updateStandderVip(TtIfStandardPO tp,TtIfStandardPO po){
		update(tp, po);  //第一个是条件，第二个是要修改的内容
	}
	
	//大区驳回更改主表状态
	public void updateOrderStatus(TtIfStandardPO ts, TtIfStandardPO tp){
		update(ts, tp);
	}
	/**
     * Function：获得附件信息列表
     * @param  ：	
     * @return:		@param id
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-15
     */
    public List<FsFileuploadPO> queryAttachFileInfo(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		sql.append(" WHERE A.YWZJ='"+id+"'");
		List<FsFileuploadPO> ls= select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}
	//查询详细
	public Map<String, Object> getStandardVipInfo(String orderId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.ID,A.ORDER_ID, A.VIN, A.ST_TYPE, A.ST_ACTION, A.ST_CONTENT,A.LINK_MAN, ");
		sql.append("	   A.TEL, A.ZIP_CODE, A.ADDRESS, C.GROUP_NAME, B.ENGINE_NO, B.COLOR, ");
		sql.append("	   TO_CHAR(B.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE, ");
		sql.append("	   TO_CHAR(B.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE, ");
		//sql.append("	   D.CUSTOMER_NAME, D.CERT_NO, D.MOBILE, D.ADDRESS_DESC, ");
		sql.append(" TC.CTM_NAME AS CUSTOMER_NAME,TC.CARD_NUM AS CERT_NO,TC.MAIN_PHONE AS MOBILE,TC.ADDRESS AS ADDRESS_DESC, ");
		sql.append("  	   E.DEALER_CODE, E.DEALER_NAME ");
		sql.append("FROM TT_IF_STANDARD A, TM_VEHICLE B, TM_VHCL_MATERIAL_GROUP C, TT_DEALER_ACTUAL_SALES D, TM_DEALER E,TT_CUSTOMER TC ");
		sql.append("WHERE A.VIN = B.VIN ");
		sql.append("AND B.SERIES_ID = C.GROUP_ID ");
		sql.append("AND A.DEALER_ID = E.DEALER_ID ");
		sql.append("AND B.VEHICLE_ID = D.VEHICLE_ID(+) ");
		sql.append(" AND D.CTM_ID=TC.CTM_ID(+)");
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.ORDER_ID = '").append(orderId).append("' ");
		Map<String, Object> map = 
		pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//查询经销商信息
	public Map<String, Object> getDealerInfo(String dealerId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT DEALER_ID, LINK_MAN, PHONE, ZIP_CODE, ADDRESS FROM TM_DEALER ");
		sql.append("WHERE DEALER_ID = ").append(dealerId);
		Map<String, Object> map = 
		pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	//查询审批详细
	public List<Map<String, Object>> getAuditInfo(String orderId){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TO_CHAR(A.AUDIT_DATE, 'YYYY-MM-DD') AUDIT_DATE, B.NAME, A.AUDIT_STATUS, A.AUDIT_CONTENT, C.ORG_NAME ");
		sql.append("FROM TT_IF_STANDARD_AUDIT A, TC_USER B, TM_ORG C ");
		sql.append("WHERE A.AUDIT_BY = B.USER_ID ");
		sql.append("AND A.ORG_ID = C.ORG_ID ");
		sql.append("AND A.AUDIT_STATUS IN(").append(Constant.SERVICEINFO_VIP_AREA_STATUS_PASS).append(", "); 
		sql.append(Constant.SERVICEINFO_VIP_AREA_STATUS_REJECT).append(", ");
		sql.append(Constant.SERVICEINFO_VIP_STATUS_REPORTED).append(", ");
		sql.append(Constant.SERVICEINFO_VIP_SERVICE_STATUS_PASS).append(", ");
		sql.append(Constant.SERVICEINFO_VIP_SERVICE_STATUS_REJECT).append(") ") ;
		sql.append("AND A.ORDER_ID = '").append(orderId).append("' "); 
		sql.append("order by A.AUDIT_DATE desc");
		List<Map<String, Object>> MVList = 
		pageQuery(sql.toString(), null, getFunName());
		return MVList;
	}
	//修改时批量删除附件信息
	public void delfilesUploadByBusiness(String ywzj,String ids){
		StringBuffer sql = new StringBuffer("");
		sql.append("DELETE FROM  FS_FILEUPLOAD A");
		sql.append(" WHERE A.YWZJ = "+ywzj);
		if(null!=ids&&!"".equals(ids))
		sql.append(" AND A.FJID not in ("+ids+")");
		delete(sql.toString(),null);
	}
	
}
