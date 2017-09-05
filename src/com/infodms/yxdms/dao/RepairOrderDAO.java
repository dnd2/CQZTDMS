package com.infodms.yxdms.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsRepairOrderBackupPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoLabourBean;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrFeeWarrantyPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrMalfunctionPositionPO;
import com.infodms.dms.po.TtAsWrVinPartRepairTimesPO;
import com.infodms.dms.po.TtAsWrVinRepairDaysPO;
import com.infodms.dms.po.TtAsWrVinRulePO;
import com.infodms.dms.po.TtAsWrWarrantyPO;
import com.infodms.dms.po.TtAsWrWarrantyRecordPO;
import com.infodms.dms.po.TtCustomerPO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
@SuppressWarnings("rawtypes")
public class RepairOrderDAO extends BaseDao{
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	private static RepairOrderDAO dao = new RepairOrderDAO();
	public static final RepairOrderDAO getInstance(){
		dao = (dao==null)?new RepairOrderDAO():dao;
		return dao;
	}
	
	
   /**
    * 工单删除表查询
    * @param request
    * @param loginUser
    * @param pageSize
    * @param currPage
    * @return
    */
   @SuppressWarnings("unchecked")
   public PageResult<Map<String, Object>> queryRepairOrderDelet(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {

	   StringBuffer sb = new StringBuffer();
	   sb.append("select decode(a.ro_no,null,0,1)  as in_claim,tt.* from  tt_as_repair_order_delete_new  tt left join tm_dealer tm on  tt.dealer_id = tm.dealer_id ");
	   sb.append(" left join Tt_As_Wr_Application a on a.ro_no = tt.ro_no   where 1=1 \n"); 
	   if (!"".equals(loginUser.getDealerId())) {
		   DaoFactory.getsql(sb, "tt.dealer_id", loginUser.getDealerId(), 1);
	   }
	   DaoFactory.getsql(sb, "tt.vin", DaoFactory.getParam(request, "VIN"), 2);
	   DaoFactory.getsql(sb, "tt.ro_no", DaoFactory.getParam(request, "RO_NO"), 2);
	   DaoFactory.getsql(sb, "tt.RO_CREATE_DATE", DaoFactory.getParam(request, "RO_CREATE_DATE"), 3);
	   DaoFactory.getsql(sb, "tt.RO_CREATE_DATE", DaoFactory.getParam(request, "DELIVERY_DATE"), 4);
	   DaoFactory.getsql(sb, "tt.REPAIR_TYPE_CODE", DaoFactory.getParam(request, "REPAIR_TYPE"), 1);
	   DaoFactory.getsql(sb, "tt.balance_yieldly", DaoFactory.getParam(request, "YIELDLY_TYPE"), 1);
	   DaoFactory.getsql(sb, "tt.is_warning", DaoFactory.getParam(request, "IS_WARNING"), 1);
	   DaoFactory.getsql(sb, "tt.RO_STATUS", DaoFactory.getParam(request, "RO_STATUS"), 1);
	   DaoFactory.getsql(sb, "tt.FORL_STATUS", DaoFactory.getParam(request, "RO_FORE"), 1);
	   return this.pageQuery(sb.toString(), null, getFunName(), pageSize, currPage);
   }
   /**
    * 维修工单查询
    * @param request
    * @param loginUser
    * @param pageSize
    * @param currPage
    * @return
    */
    @SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> repairOrderQuery(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("SELECT DECODE(A.RO_NO, NULL, 0, 1) IN_CLAIM,a.id as gdid,SS.SUBJECT_NO,r.cam_code,\n");
			sqlStr.append("       R.id,r.ro_no,r.vin,r.free_times,r.RO_STATUS,r.IN_MILEAGE,r.RO_CREATE_DATE,r.DELIVERER_MOBILE,\n");
			sqlStr.append("       r.DELIVERER_PHONE,r.DELIVERER,r.OWNER_NAME,vw.MODEL_CODE as  MODEL,r.LICENSE,r.BALANCE_YIELDLY,r.REPAIR_TYPE_CODE,\n");
			sqlStr.append("       D.DEALER_CODE DEALER_CODES,a.CAMPAIGN_CODE,\n");
			sqlStr.append("       D.DEALER_SHORTNAME,\n");
			sqlStr.append("       DS.ROOT_ORG_NAME AREA_NAME,");
			sqlStr.append("       M.MATERIAL_NAME,");
			sqlStr.append("       M.COLOR_NAME,");
			sqlStr.append("       TO_CHAR(V.PURCHASED_DATE, 'yyyy-mm-dd') PURCHASED_DATE,\n");
			sqlStr.append("       DECODE(C.MAIN_PHONE, NULL, C.OTHER_PHONE, C.MAIN_PHONE) OWNER_PHONE,M3.GROUP_NAME\n");
	        sqlStr.append(" from tt_as_repair_order r\n" );
	        sqlStr.append("left join tm_vehicle v on v.vin = r.vin\n");
	        sqlStr.append("left join tt_dealer_actual_sales s on s.vehicle_id = v.vehicle_id and s.is_return="+Constant.IF_TYPE_NO+"\n");
	        sqlStr.append("left join tt_customer c on c.ctm_id   = s.ctm_id\n");
	        sqlStr.append("left join tt_as_wr_application a on   r.ro_no=a.ro_no\n"); 
	        sqlStr.append("left outer join tm_dealer d on r.dealer_id=d.dealer_id \n");
			sqlStr.append("LEFT JOIN vw_org_dealer_service ds ON d.dealer_id = ds.dealer_id\n");
			sqlStr.append("LEFT JOIN TM_VHCL_MATERIAL M ON V.MATERIAL_ID = M.MATERIAL_ID\n");
			sqlStr.append("LEFT JOIN TT_AS_ACTIVITY AA ON AA.ACTIVITY_CODE = R.CAM_CODE\n");
			sqlStr.append("LEFT JOIN TT_AS_ACTIVITY_SUBJECT  SS ON SS.SUBJECT_ID = AA.SUBJECT_ID\n");
			sqlStr.append("LEFT JOIN TM_VHCL_MATERIAL_GROUP M2 ON V.SERIES_ID = M2.GROUP_ID AND M2.GROUP_LEVEL=2\n");
			sqlStr.append("LEFT JOIN TM_VHCL_MATERIAL_GROUP M3 ON V.MODEL_ID = M3.GROUP_ID AND M3.GROUP_LEVEL=3\n"); 
			sqlStr.append("left join vw_material_group_service  vw on vw.PACKAGE_ID=v.package_id\n"); 
			//关联物料组 11.14  wenyudan 
			sqlStr.append("JOIN  tm_vhcl_material_group_r tvmgr on m.material_id=tvmgr.material_id\n" );
			sqlStr.append("JOIN   tm_vhcl_material_group tvmg on tvmgr.group_id=tvmg.group_id");
			sqlStr.append(" where 1=1 \n");
			DaoFactory.getsql(sqlStr, "r.RO_CREATE_DATE", "2015-05-25 23:59：59", 3);//查询在2015-5-25之后的数据
			if (Utility.testString(request.getParamValue("groupCode"))) {//物料组
				String[] array = request.getParamValue("groupCode").toString().split(",");
				String code="";
			for (int i = 0; i < array.length; i++) {
				code += "'"+array[i]+"',";
				}
			code = code.substring(0,code.length()-1);
				sqlStr.append("and tvmg.group_id in\n");
				sqlStr.append("   (select TVMG.group_id  from tm_vhcl_material_group TVMG   start with TVMG.Group_Code in("+code+")\n");
				sqlStr.append("     connect by prior TVMG.group_id =TVMG.parent_group_id )"); 
			}
			if (Utility.testString(request.getParamValue("area_code"))) {//区域
				String[] array = request.getParamValue("area_code").toString().split(",");
				sqlStr.append("   AND ds.org_code IN ( \n");
				if(array.length>0){
					for (int i = 0; i < array.length; i++) {
						sqlStr.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							sqlStr.append(",");
						}	
					}
				}else{
					sqlStr.append("''");//放空置，防止in里面报错
				}
				sqlStr.append(")\n");
			}
			if (Utility.testString(request.getParamValue("dealerCode"))) {//经销商
				String[] array = request.getParamValue("dealerCode").toString().split(",");
				sqlStr.append("   AND ds.root_dealer_code IN ( \n");
				if(array.length>0){
					for (int i = 0; i < array.length; i++) {
						sqlStr.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							sqlStr.append(",");
						}	
					}
				}else{
					sqlStr.append("''");//放空置，防止in里面报错
				}
				sqlStr.append(")\n");
			}
			DaoFactory.getsql(sqlStr, "r.order_valuable_type",DaoFactory.getParam(request, "ORDER_STATUS"), 1);
			DaoFactory.getsql(sqlStr, "r.RO_NO",DaoFactory.getParam(request, "RO_NO") , 2);
			DaoFactory.getsql(sqlStr, "r.model",DaoFactory.getParam(request, "model") , 1);
			DaoFactory.getsql(sqlStr, "r.CAM_CODE",DaoFactory.getParam(request, "ACTIVITY_CODE").toUpperCase() , 2);
			
			DaoFactory.getsql(sqlStr, "SS.SUBJECT_NO",DaoFactory.getParam(request, "ACTIVITY_MAIN") , 2);
			DaoFactory.getsql(sqlStr, "r.REPAIR_TYPE_CODE",DaoFactory.getParam(request, "REPAIR_TYPE") , 2);
			DaoFactory.getsql(sqlStr, "r.RO_CREATE_DATE",DaoFactory.getParam(request, "RO_CREATE_DATE") , 3);
			DaoFactory.getsql(sqlStr, "r.RO_CREATE_DATE",DaoFactory.getParam(request, "DELIVERY_DATE") , 4);
			
			DaoFactory.getsql(sqlStr, "r.r.FORL_STATUS",DaoFactory.getParam(request, "RO_FORE") ,1);
			DaoFactory.getsql(sqlStr, "r.RO_STATUS",DaoFactory.getParam(request, "RO_STATUS") , 1);
			DaoFactory.getsql(sqlStr, "r.vin",DaoFactory.getParam(request, "vin") , 2);
			DaoFactory.getsql(sqlStr, "r.CREATE_DATE",DaoFactory.getParam(request, "CREATE_DATE_STR") , 3);
			DaoFactory.getsql(sqlStr, "r.CREATE_DATE",DaoFactory.getParam(request, "CREATE_DATE_END") , 4);
			System.out.println(request.getParamValue("dealerId")+loginUser.getDealerId()+"测试");
			if(Utility.testString(loginUser.getDealerId())){
				sqlStr.append(" and r.dealer_code in(select dea.dealer_code from tm_dealer dea where dea.dealer_id in("+loginUser.getDealerId().toString()+"))");
			}
			sqlStr.append("  order by r.ID DESC ");
			return this.pageQuery(sqlStr.toString(), null, this.getFunName(), pageSize, currPage);
	
	
    }


   @SuppressWarnings("unchecked")
   public List<Map<String, Object>> ModelcodeQuery(RequestWrapper request,AclUserBean loginUser, Integer pageSizeMax, Integer currPage) {
	     StringBuffer sql= new StringBuffer();
	     sql.append("select distinct (s.MODEL_CODE),s.SALES_MODEL_GROUP_CODE from vw_material_group_service s");
	    return this.pageQuery(sql.toString(), null, getFunName());
    }


   @SuppressWarnings("unchecked")
   public PageResult<Map<String, Object>> OrderByVindata(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
	   StringBuffer sql= new StringBuffer();
	   sql.append("select o.*, rr.activity_name,rr.activity_code\n" );
	   sql.append("          from tt_as_repair_order o,tt_as_activity rr \n" );
	   sql.append("         where 1 = 1 and o.cam_code = rr.activity_code(+)\n" );
	   DaoFactory.getsql(sql,"o.vin", DaoFactory.getParam(request, "vin"), 2);
	   sql.append("           and o.cam_code is not null");
	   return this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
    }
   
 	
   }
