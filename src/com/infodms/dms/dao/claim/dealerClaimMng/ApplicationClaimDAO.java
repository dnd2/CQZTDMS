package com.infodms.dms.dao.claim.dealerClaimMng;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * @Description: TODO(索赔单维护DAO)
 * @author liusw
 */
@SuppressWarnings("rawtypes")
public class ApplicationClaimDAO extends BaseDao {

	public static Logger logger = Logger.getLogger(ApplicationClaimDAO.class);
	private static final ApplicationClaimDAO dao = new ApplicationClaimDAO();
	public static final ApplicationClaimDAO getInstance() {
		return dao;
	}	
	/**
	* 工单查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> orderClaim(Map<String,Object> paraMap,int curPage,int pageSize) throws Exception{
		List<Object> listPar=new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.service_order_id,--服务工单ID\n" );
		sql.append("       t.service_order_code,--服务工单编码\n" );
		sql.append("       t.dealer_id,--经销商id\n" );
		sql.append("       d.dealer_code,--经销商代码\n" );
		sql.append("       d.dealer_name,--经销商名称\n" );
		sql.append("       d.PHONE,--经销商电话\n" );
		sql.append("       t.repair_type,--维修类型\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=t.repair_type) as repair_name,--维修类型\n" );
		sql.append("       TO_CHAR(t.arrival_date, 'YYYY-MM-DD') as arrival_date,--进站时间\n" );
		sql.append("       TO_CHAR(t.repair_date_begin, 'YYYY-MM-DD') as repair_date_begin,--维修开始时间\n" );
		sql.append("       TO_CHAR(t.repair_date_end, 'YYYY-MM-DD') as repair_date_end,--维修结束时间\n" );
		sql.append("       t.mileage,--进站里程数\n" );
		sql.append("       t.receptionist_man,--接待人\n" );
		sql.append("       t.vin,--车架号\n" );
		sql.append("       v.engine_no,--发动机号\n" );
		sql.append("       v.license_no,--车牌号\n" );
		sql.append("B.BRAND_NAME, --品牌\n" );
		sql.append("B.SERIES_NAME, --车系名称\n" );
		sql.append("B.MODEL_NAME, --车型名称\n" );
		sql.append("B.PACKAGE_NAME, --配置\n" );
		sql.append("TO_CHAR(w.E_START_DATE, 'YYYY-MM-DD') as E_START_DATE, --救援开始时间\n" );
		sql.append("TO_CHAR(w.E_END_DATE, 'YYYY-MM-DD') as E_END_DATE, --救援结束时间\n" );
		sql.append("w.E_NUM, --救援人数\n" );
		sql.append("w.E_NAME, --救援人姓名\n" );
		sql.append("w.E_LICENSE_NO, --派车车牌号\n" );
		sql.append("       TO_CHAR(v.PURCHASED_DATE, 'YYYY-MM-DD')as PURCHASED_DATE,--购车日期\n" );
		sql.append("       TO_CHAR(v.PRODUCT_DATE, 'YYYY-MM-DD')as PRODUCT_DATE,--生产日期\n" );
		sql.append("        E.CTM_NAME,--车主姓名\n" );
		sql.append("       G.RULE_NAME,--三包规则名称\n" );		
		sql.append("       t.deliverer_man_name,--送修人姓名\n" );
		sql.append("       t.deliverer_man_phone,--送修人电话\n" );
		sql.append("       a.AREA_NAME, --产地名称\n" );
		sql.append("       t.fault_desc,--故障描述\n" );
		sql.append("       t.fault_reason,--故障原因\n" );
		sql.append("       t.repair_method,--申请内容维修措施\n" );
		sql.append("       t.remark,--申请内容，申请备注\n" );
		sql.append("       t.auth_audit_by,--授权审核人\n" );
		sql.append("       t.auth_audit_date,--授权审核时间\n" );
		sql.append("       t.is_can_claim,--是否可以生成索赔单\n" );
		sql.append("       t.is_build_claim,--是否已生成索赔单\n" );
		sql.append("       t.EGRESS_ID,--外出维修id\n" );
		sql.append("       nvl(t.APPLY_PDI_PRICE, 0) as APPLY_PDI_PRICE,--pdi金额\n" );
		sql.append("       t.PDI_REMARK,--pdi结果\n" );
		sql.append("       nvl(t.APPLY_MAINTAIN_PRICE, 0) as APPLY_MAINTAIN_PRICE,--保养费用\n" );
		sql.append("       t.CUR_FREE_TIMES,--保养次数\n" );
		sql.append("       wa.ACTIVITY_TYPE,--服务活动类型\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=wa.ACTIVITY_TYPE) as ACTIVITY_NAME,--服务活动类型\n" );
		sql.append("       wa.ACTIVITY_DISCOUNT,--服务活动折扣率\n" );
		sql.append("       nvl(t.APPLY_ACTIVITY_PRICE, 0) as APPLY_ACTIVITY_PRICE,--服务活动金额(原价)\n" );
		sql.append("       nvl(round(t.APPLY_ACTIVITY_PRICE * (100-wa.ACTIVITY_DISCOUNT) / 100, 2), 0) as APPLY_ACTIVITY_PRICE_Z,--服务活动金额(折扣价)\n" );
		sql.append("       t.status,--状态\n" );
		sql.append("       t.is_return_hide,\n" );
		sql.append("       TO_CHAR(t.create_date, 'YYYY-MM-DD') as create_date--创建时间\n" );
		sql.append("  from tt_as_service_order t\n" );
		sql.append("  left join tm_dealer d on t.dealer_id=d.dealer_id \n" );
		sql.append("  left join tm_vehicle v on t.vin=v.vin \n" );
		sql.append("  LEFT JOIN TM_BUSINESS_AREA a ON a.AREA_ID = v.AREA_ID \n" );
		sql.append("  LEFT JOIN TT_AS_WR_GAME F  ON  v.CLAIM_TACTICS_ID = F.ID\n" );
		sql.append("  LEFT JOIN TT_AS_WR_RULE G ON F.RULE_ID = G.ID\n" );
		sql.append("LEFT JOIN TT_DEALER_ACTUAL_SALES s ON s.VEHICLE_ID = v.VEHICLE_ID\n" );
		sql.append("LEFT JOIN TT_CUSTOMER E ON s.CTM_ID = E.CTM_ID\n" );
		sql.append("LEFT JOIN TT_AS_EGRESS w ON w.ID = t.EGRESS_ID\n" );//外出维修
		sql.append("LEFT JOIN TT_AS_WR_ACTIVITY wa ON t.ACTIVITY_ID = wa.ACTIVITY_ID\n" );//服务活动		
		sql.append("  LEFT JOIN (SELECT DISTINCT PACKAGE_ID, PACKAGE_NAME, MODEL_ID, MODEL_NAME, SERIES_NAME ,BRAND_NAME FROM VW_MATERIAL_GROUP_MAT) B ON v.PACKAGE_ID = B.PACKAGE_ID\n" );
		sql.append(" where t.status = "+Constant.SERVICE_ORDER_STATUS_08+" \n" );
		sql.append("   and t.is_can_claim = "+Constant.PART_BASE_FLAG_YES+" ");
		sql.append("   and t.is_build_claim !="+Constant.PART_BASE_FLAG_YES+"");
		
		if(paraMap.get("so_id")!=null){
			sql.append(" and t.service_order_id = "+paraMap.get("so_id")+"");
		}
		if(paraMap.get("dealerdId")!=null){
			sql.append(" and t.dealer_id = "+paraMap.get("dealerdId")+"");
		}
		if(paraMap.get("SERVICE_ORDER_CODE")!=null){
			sql.append(" and t.SERVICE_ORDER_CODE like ? ");
			listPar.add("%"+paraMap.get("SERVICE_ORDER_CODE")+"%");
		}
		if(paraMap.get("VIN")!=null){
			sql.append(" and t.VIN like ? ");
			listPar.add("%"+paraMap.get("VIN")+"%");
		}
		if(paraMap.get("creatDate")!=null){
			sql.append(" and to_date(to_char(t.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(paraMap.get("creatDate"));
		}
		if(paraMap.get("outPlantDate")!=null){
			sql.append(" and to_date(to_char(T.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') <= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(paraMap.get("outPlantDate"));
		}
		if(paraMap.get("repairType")!=null){
			sql.append(" and T.REPAIR_TYPE = ? ");
			listPar.add(paraMap.get("repairType"));
		}
		if(paraMap.get("LICENSE_NO")!=null){
			sql.append(" and T.LICENSE_NO like ? ");
			listPar.add("%"+paraMap.get("LICENSE_NO")+"%");
		}
		if(paraMap.get("CTM_NAME")!=null){
			sql.append(" and T.CTM_NAME like ? ");
			listPar.add("%"+paraMap.get("CTM_NAME")+"%");
		}
		sql.append("  ORDER BY T.CREATE_DATE DESC");						
		return dao.pageQuery(sql.toString(), listPar,getFunName(), pageSize, curPage);			
		}
	/**
	* 索赔单查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> applicationClaim(Map<String,Object> paraMap,int curPage,int pageSize) throws Exception{
		List<Object> listPar=new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,--索赔单ID\n" );
		sql.append("       t.APP_CLAIM_NO,--索赔单号\n" );
		sql.append("       t.service_order_code,--服务工单编码\n" );
		sql.append("       t.service_order_id,--服务工单id\n" );
		sql.append("       t.dealer_id,--经销商id\n" );
		sql.append("       d.dealer_code,--经销商代码\n" );
		sql.append("       d.dealer_name,--经销商名称\n" );
		sql.append("       d.PHONE,--经销商电话\n" );
		sql.append("       t.repair_type,--维修类型\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=t.repair_type) as repair_name,--维修类型\n" );
		sql.append("       TO_CHAR(t.arrival_date, 'YYYY-MM-DD') as arrival_date,--进站时间\n" );
		sql.append("       TO_CHAR(t.repair_date_begin, 'YYYY-MM-DD') as repair_date_begin,--维修开始时间\n" );
		sql.append("       TO_CHAR(t.repair_date_end, 'YYYY-MM-DD') as repair_date_end,--维修结束时间\n" );
		sql.append("       nvl(t.mileage, 0) as mileage,--进站里程数\n" );
		sql.append("       t.receptionist_man,--接待人\n" );
		sql.append("       t.vin,--车架号\n" );
		sql.append("       v.engine_no,--发动机号\n" );
		sql.append("       v.license_no,--车牌号\n" );
		sql.append("B.BRAND_NAME, --品牌\n" );
		sql.append("B.SERIES_NAME, --车系名称\n" );
		sql.append("B.MODEL_NAME, --车型名称\n" );
		sql.append("B.PACKAGE_NAME, --配置\n" );
		sql.append("TO_CHAR(w.E_START_DATE, 'YYYY-MM-DD') as E_START_DATE, --救援开始时间\n" );
		sql.append("TO_CHAR(w.E_END_DATE, 'YYYY-MM-DD') as E_END_DATE, --救援结束时间\n" );
		sql.append("w.E_NUM, --救援人数\n" );
		sql.append("w.E_NAME, --救援人姓名\n" );
		sql.append("w.E_LICENSE_NO, --派车车牌号\n" );
		sql.append("       TO_CHAR(v.PURCHASED_DATE, 'YYYY-MM-DD')as PURCHASED_DATE,--购车日期\n" );
		sql.append("       TO_CHAR(v.PRODUCT_DATE, 'YYYY-MM-DD')as PRODUCT_DATE,--生产日期\n" );
		sql.append("        E.CTM_NAME,--车主姓名\n" );
		sql.append("       G.RULE_NAME,--三包规则名称\n" );		
		sql.append("       t.deliverer_man_name,--送修人姓名\n" );
		sql.append("       t.deliverer_man_phone,--送修人电话\n" );
		sql.append("       a.AREA_NAME, --产地名称\n" );
		sql.append("       t.fault_desc,--故障描述\n" );
		sql.append("       t.fault_reason,--故障原因\n" );
		sql.append("       t.repair_method,--申请内容维修措施\n" );
		sql.append("       t.Apply_remark,--申请内容，申请备注\n" );
		sql.append("       t.Apply_remark AS remark,--申请内容，申请备注\n" );
		sql.append("       t.auth_audit_by,--授权审核人\n" );
		sql.append("       t.auth_audit_date,--授权审核时间\n" );
		sql.append("       t.status,\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=t.status) as status_name,--状态\n" );
		sql.append("       nvl(t.OUTWARD_APPLY_AMOUNT, 0) as OUT_AMOUNT,--外出费用\n" );
		sql.append("       nvl(t.PDI_APPLY_AMOUNT, 0) as APPLY_PDI_PRICE,--pdi金额\n" );
		sql.append("       t.PDI_RESULT as PDI_REMARK,--pdi结果\n" );
		sql.append("       wa.ACTIVITY_TYPE,--服务活动类型\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=wa.ACTIVITY_TYPE) as ACTIVITY_NAME,--服务活动类型\n" );
		sql.append("       wa.ACTIVITY_DISCOUNT,--服务活动折扣率\n" );
		sql.append("       nvl(t.ACTIVITIE_APPLY_AMOUNT, 0) as APPLY_ACTIVITY_PRICE_Z,--服务活动金额(折扣价)\n" );
		sql.append("       t.MAINTENANCE_TIME as CUR_FREE_TIMES,--保养次数\n" );
		sql.append("       nvl(t.FIRST_APPLY_AMOUNT, 0) as APPLY_MAINTAIN_PRICE,--保养费用\n" );
		sql.append("       t.EGRESS_ID,--外出维修id\n" );
		sql.append("       nvl(t.PART_APPLY_AMOUNT, 0) as PART_APPLY_AMOUNT,--配件申请费用\n" );
		sql.append("       nvl(t.HOURS_APPLY_AMOUNT, 0) as HOURS_APPLY_AMOUNT,--工时申请费用\n" );
		sql.append("       nvl(t.APPLY_TOTAL_AMOUNT, 0) as APPLY_TOTAL_AMOUNT,--申请总费用\n" );
		sql.append("       TO_CHAR(t.create_date, 'YYYY-MM-DD') as create_date--创建时间\n" );
		sql.append("  from TT_AS_WR_APPLICATION_CLAIM t\n" );
		sql.append("  left join tm_dealer d on t.dealer_id=d.dealer_id \n" );
		sql.append("  left join tm_vehicle v on t.vin=v.vin \n" );
		sql.append("  LEFT JOIN TM_BUSINESS_AREA a ON a.AREA_ID = v.AREA_ID \n" );
		sql.append("  LEFT JOIN TT_AS_WR_GAME F  ON  v.CLAIM_TACTICS_ID = F.ID\n" );
		sql.append("  LEFT JOIN TT_AS_WR_RULE G ON F.RULE_ID = G.ID\n" );
		sql.append("LEFT JOIN TT_DEALER_ACTUAL_SALES s ON s.VEHICLE_ID = v.VEHICLE_ID\n" );
		sql.append("LEFT JOIN TT_CUSTOMER E ON s.CTM_ID = E.CTM_ID\n" );
		sql.append("LEFT JOIN TT_AS_EGRESS w ON w.ID = t.EGRESS_ID\n" );//外出维修
		sql.append("LEFT JOIN TT_AS_WR_ACTIVITY wa ON t.ACTIVITY_ID = wa.ACTIVITY_ID\n" );//服务活动	
		sql.append("  LEFT JOIN (SELECT DISTINCT PACKAGE_ID, PACKAGE_NAME, MODEL_ID, MODEL_NAME, SERIES_NAME ,BRAND_NAME FROM VW_MATERIAL_GROUP_MAT) B ON v.PACKAGE_ID = B.PACKAGE_ID\n" );
		sql.append(" where 1=1 \n" );
		
		if(paraMap.get("id")!=null){
			sql.append(" and t.ID = "+paraMap.get("id")+"");
		}
		if(paraMap.get("dealerdId")!=null){
			sql.append(" and t.dealer_id = "+paraMap.get("dealerdId")+"");
		}
		if(paraMap.get("factory1")!=null){
			sql.append(" and t.status in ("+Constant.APP_CLAIM_TYPE_02+")");
		}
		if(paraMap.get("factory2")!=null){
			sql.append(" and t.status not in ("+Constant.APP_CLAIM_TYPE_01+","+Constant.APP_CLAIM_TYPE_02+")");
		}
		if(paraMap.get("factory3")!=null){
			sql.append(" and t.status not in ("+Constant.APP_CLAIM_TYPE_01+")");
		}
		if(paraMap.get("SERVICE_ORDER_CODE")!=null){
			sql.append(" and t.SERVICE_ORDER_CODE like ? ");
			listPar.add("%"+paraMap.get("SERVICE_ORDER_CODE")+"%");
		}
		if(paraMap.get("APP_CLAIM_NO")!=null){
			sql.append(" and t.APP_CLAIM_NO like ? ");
			listPar.add("%"+paraMap.get("APP_CLAIM_NO")+"%");
		}
		if(paraMap.get("VIN")!=null){
			sql.append(" and t.VIN like ? ");
			listPar.add("%"+paraMap.get("VIN")+"%");
		}
		if(paraMap.get("creatDate")!=null){
			sql.append(" and to_date(to_char(t.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(paraMap.get("creatDate"));
		}
		if(paraMap.get("outPlantDate")!=null){
			sql.append(" and to_date(to_char(T.CREATE_DATE,'yyyy-mm-dd'),'yyyy-mm-dd') <= to_date(?,'yyyy-mm-dd') \n");
			listPar.add(paraMap.get("outPlantDate"));
		}
		if(paraMap.get("repairType")!=null){
			sql.append(" and T.REPAIR_TYPE = ? ");
			listPar.add(paraMap.get("repairType"));
		}
		if(paraMap.get("LICENSE_NO")!=null){
			sql.append(" and T.LICENSE_NO like ? ");
			listPar.add("%"+paraMap.get("LICENSE_NO")+"%");
		}
		sql.append("  ORDER BY t.status asc,T.CREATE_DATE DESC");						
		return dao.pageQuery(sql.toString(), listPar,getFunName(), pageSize, curPage);			
		}
	/**
	* 客户问题查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,String>> cusProblem(Map<String,String> paraMap,int curPage,int pageSize) throws Exception{
		List<String> listPar=new ArrayList<String>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.id,\n" );
		sql.append("       t.ccc_code,\n" );
		sql.append("       t.ccc_name,\n" );
		sql.append("       t.vrt_name,\n" );
		sql.append("       t.vrt_code,\n" );
		sql.append("       t.vfg_code,\n" );
		sql.append("       t.vfg_name,\n" );
		sql.append("       t.create_date,\n" );
		sql.append("       t.ccc_vrt_vfg\n" );
		sql.append("  from tm_ccc t\n" );
		sql.append(" where t.status = 10041001");

		
		if(paraMap.get("VRT_CODE")!=null){
			sql.append(" and T.VRT_CODE = ? ");
			listPar.add(paraMap.get("VRT_CODE"));
		}
		if(paraMap.get("VFG_CODE")!=null){
			sql.append(" and T.VFG_CODE = ? ");
			listPar.add(paraMap.get("VFG_CODE"));
		}
		if(paraMap.get("CCC_NAME")!=null){
			sql.append(" and T.CCC_NAME like ? ");
			listPar.add("%"+paraMap.get("CCC_NAME")+"%");
		}
		sql.append("  ORDER BY T.CREATE_DATE DESC");						
		return dao.pageQuery(sql.toString(), listPar,getFunName(), pageSize, curPage);			
		}
	/**
	* 工单工时查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> seOrderHours(long service_order_id) throws Exception{
		List<String> listPar=new ArrayList<String>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.service_project_id,\n" );
		sql.append("       t.service_order_id,\n" );
		sql.append("       t.labour_id,\n" );
		sql.append("       t.labour_code,\n" );
		sql.append("       t.cn_des,\n" );
		sql.append("       t.labour_hour,\n" );
		sql.append("       t.labour_price,\n" );
		sql.append("round(t.labour_hour * t.labour_price, 2) as labour_amount,\n" );
		sql.append("       t.labour_payment_method\n" );
		sql.append("  from TT_AS_SERVICE_PROJECT t\n" );
		sql.append(" where t.service_order_id = "+service_order_id+"");
		sql.append(" and t.LABOUR_PAYMENT_METHOD="+Constant.PAY_TYPE_02+" ");
						
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	/**
	* 工单外出查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> seOrderOuts(long service_order_id) throws Exception{
		List<String> listPar=new ArrayList<String>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.SERVICE_OUT_PROJECT_ID,\n" );
		sql.append("       t.SERVICE_ORDER_ID,\n" );
		sql.append("       t.FEE_ID,\n" );
		sql.append("       t.FEE_CODE,\n" );
		sql.append("       t.FEE_NAME,\n" );
		sql.append("       nvl(t.FEE_PRICE, 0) as FEE_PRICE,\n" );
		sql.append("       t.FEE_REMARK,\n" );
		sql.append("       (select ba.PART_CODE from Tm_Pt_Part_Base ba where ba.part_id=t.FEE_RELATION_MAIN_PART) as FEE_RELATION_MAIN_PART_CODE,\n" );
		sql.append("       t.FEE_RELATION_MAIN_PART\n" );
		sql.append("  from TT_AS_SERVICE_OUT_PROJECT t\n" );
		sql.append(" where t.service_order_id = "+service_order_id+"");
		sql.append(" and t.FEE_PAYMENT_METHOD="+Constant.PAY_TYPE_02+" ");
						
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	/**
	* 工单工时查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> seAppClaimHoursId(long labourId,long so_id) throws Exception{
		List<String> listPar=new ArrayList<String>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.service_project_id,\n" );
		sql.append("       t.service_order_id,\n" );
		sql.append("       t.labour_id,\n" );
		sql.append("       t.labour_code,\n" );
		sql.append("       t.cn_des,\n" );
		sql.append("       t.labour_hour,\n" );
		sql.append("       t.labour_price,\n" );
		sql.append("round(t.labour_hour * t.labour_price, 2) as labour_amount,\n" );
		sql.append("       t.labour_payment_method\n" );
		sql.append("  from TT_AS_SERVICE_PROJECT t\n" );
		sql.append(" where t.LABOUR_ID = "+labourId+"");
		sql.append(" and t.LABOUR_PAYMENT_METHOD="+Constant.PAY_TYPE_02+" ");
		sql.append(" and t.SERVICE_ORDER_ID="+so_id+" ");
						
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	/**
	* 索赔单工时查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> seAppClaimHours(long APP_CLAIM_ID) throws Exception{
		List<String> listPar=new ArrayList<String>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.CLAIM_PROJECT_ID,\n" );
		sql.append("       t.APP_CLAIM_ID,\n" );
		sql.append("       t.labour_id,\n" );
		sql.append("       t.labour_code,\n" );
		sql.append("       t.cn_des,\n" );
		sql.append("       t.labour_hour,\n" );
		sql.append("       t.labour_price,\n" );
		sql.append("       t.CUSTOMER_PROBLEM,\n" );
		sql.append("       t.HOURS_APPLY_AMOUNT as labour_amount,\n" );
		sql.append("       t.PAYMENT_METHOD\n" );
		sql.append("  from TT_AS_WR_APP_PROJECT t\n" );
		sql.append(" where t.APP_CLAIM_ID = "+APP_CLAIM_ID+"");
						
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	/**
	* 索赔单外出查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> seClaimOuts(long appclaim_id) throws Exception{
		List<String> listPar=new ArrayList<String>();
		StringBuffer sql= new StringBuffer();
		sql.append("select \n" );
		sql.append("       t.FEE_ID,\n" );
		sql.append("       t.FEE_CODE,\n" );
		sql.append("       t.FEE_NAME,\n" );
		sql.append("       nvl(t.FEE_PRICE, 0) as FEE_PRICE,\n" );
		sql.append("       t.FEE_REMARK,\n" );
		sql.append("       (select ba.PART_CODE from Tm_Pt_Part_Base ba where ba.part_id=t.FEE_RELATION_MAIN_PART) as FEE_RELATION_MAIN_PART_CODE,\n" );
		sql.append("       t.FEE_RELATION_MAIN_PART\n" );
		sql.append("  from TT_AS_WR_APP_OUT t\n" );
		sql.append(" where t.appclaim_id = "+appclaim_id+"");
						
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	/**
	* 工单配件查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> seOrderParts(long SERVICE_ORDER_ID) throws Exception{
		List<Object> listPar=new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.service_part_id,\n" );
		sql.append("       t.SERVICE_ORDER_ID,\n" );
		sql.append("       t.is_three_guarantee,\n" );
		sql.append("(SELECT c.code_desc FROM tc_code c where c.code_id=t.is_three_guarantee) as isg,\n" );
		sql.append("       t.part_id,\n" );
		sql.append("       t.part_code,\n" );
		sql.append("       t.part_cname,\n" );
		sql.append("       t.part_num,\n" );
		sql.append("       t.sale_price1,\n" );
		sql.append("       t.part_fare_rate,\n" );
		sql.append("       t.PART_PRICE,\n" );
		sql.append("       t.part_fare_rate,\n" );
		sql.append("       t.failure_mode_code,\n" );
		sql.append("       t.part_payment_method,\n" );
		sql.append("       t.is_main_part,\n" );
		sql.append("(SELECT c.code_desc FROM tc_code c where c.code_id=t.is_main_part) as is_main_part_name,\n" );
		sql.append("(SELECT c.code_desc FROM tc_code c where c.code_id=t.PART_USE_TYPE) as PART_USE_TYPE_NAME,\n" );
		sql.append("       t.PART_USE_TYPE,\n" );
		sql.append("       t.relation_main_part,\n" );
		sql.append("       (select ba.PART_CODE from Tm_Pt_Part_Base ba where ba.part_id=t.relation_main_part) as relation_main_part_code,\n" );
		sql.append("round(t.part_num * t.PART_PRICE, 2) as part_amount,\n" );
		sql.append("       t.relation_labour,\n" );
		sql.append("       (select p.labour_code from TT_AS_SERVICE_PROJECT p where p.service_order_id="+SERVICE_ORDER_ID+" and p.labour_id=t.relation_labour) as relation_labour_code\n" );
		sql.append("  from TT_AS_SERVICE_PART t\n" );
		sql.append(" where t.SERVICE_ORDER_ID ="+SERVICE_ORDER_ID+"");
		sql.append(" and t.PART_PAYMENT_METHOD="+Constant.PAY_TYPE_02+" ");
						
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	/**
	* 工单配件查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> seOrderPartsSoId(long labourId,long so_Id) throws Exception{
		List<Object> listPar=new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.service_part_id,\n" );
		sql.append("       t.SERVICE_ORDER_ID,\n" );
		sql.append("       t.is_three_guarantee,\n" );
		sql.append("(SELECT c.code_desc FROM tc_code c where c.code_id=t.is_three_guarantee) as isg,\n" );
		sql.append("       t.part_id,\n" );
		sql.append("       t.part_code,\n" );
		sql.append("       t.part_cname,\n" );
		sql.append("       t.part_num,\n" );
		sql.append("       t.sale_price1,\n" );
		sql.append("       t.part_fare_rate,\n" );
		sql.append("       t.PART_PRICE,\n" );
		sql.append("       t.part_fare_rate,\n" );
		sql.append("       t.failure_mode_code,\n" );
		sql.append("       t.part_payment_method,\n" );
		sql.append("       t.is_main_part,\n" );
		sql.append("(SELECT c.code_desc FROM tc_code c where c.code_id=t.is_main_part) as is_main_part_name,\n" );
		sql.append("(SELECT c.code_desc FROM tc_code c where c.code_id=t.PART_USE_TYPE) as PART_USE_TYPE_NAME,\n" );
		sql.append("       t.PART_USE_TYPE,\n" );
		sql.append("       t.relation_main_part,\n" );
		sql.append("round(t.part_num * t.PART_PRICE, 2) as part_amount,\n" );
		sql.append("       t.relation_labour\n" );
		sql.append("  from TT_AS_SERVICE_PART t\n" );
		sql.append(" where t.RELATION_LABOUR ="+labourId+"");
		sql.append(" and t.PART_PAYMENT_METHOD="+Constant.PAY_TYPE_02+" ");
		sql.append(" and t.SERVICE_ORDER_ID="+so_Id+" ");
						
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	/**
	* 索赔单配件查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> seAppClaimParts(long APP_CLAIM_ID) throws Exception{
		List<Object> listPar=new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select t.CLAIM_PART_ID,\n" );
		sql.append("       t.APP_CLAIM_ID,\n" );
		sql.append("       t.is_three_guarantee,\n" );
		sql.append("(SELECT c.code_desc FROM tc_code c where c.code_id=t.is_three_guarantee) as isg,\n" );
		sql.append("       t.part_id,\n" );
		sql.append("       t.part_code,\n" );
		sql.append("       t.part_cname,\n" );
		sql.append("       t.part_num,\n" );
		sql.append("       t.sale_price1,\n" );
		sql.append("       t.part_fare_rate,\n" );
		sql.append("       t.sale_price,\n" );
		sql.append("       t.sale_price as PART_PRICE,\n" );
		sql.append("       t.part_fare_rate,\n" );
		sql.append("       t.failure_mode_id,\n" );
		sql.append("       t.is_main_part,\n" );
		sql.append("(SELECT c.code_desc FROM tc_code c where c.code_id=t.is_main_part) as is_main_part_name,\n" );
		sql.append("(SELECT c.code_desc FROM tc_code c where c.code_id=t.PART_USE_TYPE) as PART_USE_TYPE_NAME,\n" );
		sql.append("       t.PART_USE_TYPE,\n" );
		sql.append("       t.relation_main_part,\n" );
		sql.append("       (select ba.PART_CODE from Tm_Pt_Part_Base ba where ba.part_id=t.relation_main_part) as relation_main_part_code,\n" );
		sql.append(" t.PART_APPLY_AMOUNT,\n" );
		sql.append(" t.PART_APPLY_AMOUNT as PART_AMOUNT,\n" );		
		sql.append(" t.OLD_PART_ID,\n" );//旧件id
		sql.append(" t.OLD_PART_CODE,\n" );//旧件代码
		sql.append(" t.OLD_PART_CNAME,\n" );//旧件名称
		sql.append(" t.REPONSE_SUPPLIER_CODE,\n" );//责任供应商代码
		sql.append(" t.REPONSE_SUPPLIER_NAME,\n" );//责任供应商名称
		sql.append(" t.CLAIM_SUPPLIER_CODE,\n" );//索赔供应商代码
		sql.append(" t.CLAIM_SUPPLIER_NAME,\n" );//索赔供应商名称
		sql.append("       t.relation_labour,\n" );
		sql.append("       (select p.labour_code from TT_AS_WR_APP_PROJECT p where p.APP_CLAIM_ID="+APP_CLAIM_ID+" and p.labour_id=t.relation_labour) as relation_labour_code\n" );
		sql.append("  from TT_AS_WR_APP_PART t\n" );
		sql.append(" where t.APP_CLAIM_ID ="+APP_CLAIM_ID+"");
						
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	/**
	* 客户问题大系统查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> cusProblemList1() throws Exception{
		List<String> listPar=new ArrayList<String>();
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct \n" );
		sql.append("       t.vrt_name,\n" );
		sql.append("       t.vrt_code\n" );
		sql.append("  from tm_ccc t\n" );
		sql.append(" where t.status = 10041001");
						
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	/**
	* 客户问题子系统查询
	* @param paraMap
	* @return
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> cusProblemList2(String VRT_CODE) throws Exception{
		List<String> listPar=new ArrayList<String>();
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct \n" );
		sql.append("       t.vfg_code,\n" );
		sql.append("       t.vfg_name\n" );;
		sql.append("  from tm_ccc t\n" );
		sql.append(" where t.status = 10041001 and t.VRT_CODE='"+VRT_CODE+"'");					
		return dao.pageQuery(sql.toString(), listPar,getFunName());			
		}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 生成序列号
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> generateNumber(String po, String obtainName,String createDateName) throws Exception{
		List list =new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT "
		 		+" "+obtainName+" "
		 		+" from "+po+" "
		 		+" where TO_CHAR("+createDateName+",'YYYYMMDD')=TO_CHAR(sysdate,'YYYYMMDD') "
		 		+" order by "+createDateName+" desc "
				);
		return dao.pageQuery(sql.toString(),list, getFunName());
	}
	public List<Map<String, Object>> specialRecord(String id) {
		StringBuffer sql= new StringBuffer();
		sql.append("select g.id,\n" );
		sql.append("       g.APP_CLAIM_ID,\n" );
		sql.append("       g.audit_record,\n" );
		sql.append("       TO_CHAR(g.audit_date, 'YYYY-MM-DD hh24:mi:ss') audit_date,\n" );
		sql.append("       g.audit_by,\n" );
		sql.append("       u.name,\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=g.opera_ststus) as opera_ststus\n" );
		sql.append("  from TT_AS_WR_AUDIT_record g left join tc_user u on g.audit_by=u.user_Id \n" );
		sql.append(" where 1=1 ");

		DaoFactory.getsql(sql, "g.APP_CLAIM_ID", id, 1);
		sql.append(" order by g.audit_date desc ");
		return this.pageQuery(sql.toString(), null, getFunName());
	}
}