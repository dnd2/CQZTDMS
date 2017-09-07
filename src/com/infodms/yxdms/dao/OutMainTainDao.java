package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.yxdms.entity.maintain.TtAsEgressPO;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OutMainTainDao extends IBaseDao{
	private static POFactory fac=POFactoryBuilder.getInstance();
	private static final OutMainTainDao dao = new OutMainTainDao();
	
	/**
	 * 打开外出维修查询
	 * @param paraMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> Maintainselect(Map<String, Object> paraMap, int curPage, int pageSize) {
		List<Object> listPar=new ArrayList<Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT TAE.EGRESS_NO,\n") ; //外出维修单据编码
		sql.append("	   TAE.ID,\n") ; //ID
		sql.append("NVL((select Group_Name from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL=3 and group_id=tv.model_id), ' ') Group_Name,\n") ; //车型
		sql.append("NVL((select Group_Name from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL=2 and group_id=tv.SERIES_ID), ' ') SE_Name,\n") ; //车系
		sql.append("NVL((select Group_Name from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL=4 and group_id=tv.PACKAGE_ID), ' ') PZ_Name,\n") ; //配置
		sql.append("	   F_GET_TC_CODE(TAE.STATUS) STATUS,\n") ; //单据状态
		sql.append("	   F_GET_USERNAME(TAE.CREATE_BY) CREATE_BY,\n") ; //制单人
		sql.append("	   TO_CHAR(TAE.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE,\n") ; //制单日期
		sql.append("	   TAE.MILEAGE,\n") ; //行驶里程
		sql.append("       F_GET_TC_CODE(TAE.CAR_NATURE) CAR_NATURE,\n") ; //购车性质
		sql.append("       TAE.CAR_NATURE CAR_NATURE_CODE,\n") ; //购车性质code
		sql.append("	   TAE.RELIEF_MILEAGE,\n") ; //单程救急里程
		sql.append("	   (SELECT TR.REGION_NAME as RREGION_NAME FROM TM_REGION TR WHERE TR.REGION_CODE =TAE.PROVINCE ) as PROVINCE,\n") ; //救急所在省
		sql.append("	   (SELECT TR.REGION_NAME as RREGION_NAME FROM TM_REGION TR WHERE TR.REGION_CODE =TAE.CITY ) as CITY,\n") ; //救急所在市
		sql.append("       (SELECT TR.REGION_NAME as RREGION_NAME FROM TM_REGION TR WHERE TR.REGION_CODE =TAE.COUNTY ) as COUNTY,\n") ; //救急所在县		
		sql.append("	   TAE.PROVINCE PROVINCECODE,\n") ; //救急所在省CODE
		sql.append("	   TAE.CITY CITYCODE,\n") ; //救急所在市
		sql.append("       TAE.COUNTY COUNTYCODE,\n") ; //救急所在县
		sql.append("	   TAE.TOWN,\n") ; //救急所在镇
		sql.append("	   TO_CHAR(TAE.E_START_DATE,'yyyy-MM-dd HH24:mi') E_START_DATE,\n") ; //救急开始时间
		sql.append("	   TO_CHAR(TAE.E_END_DATE,'yyyy-MM-dd HH24:mi') E_END_DATE,\n") ; //急救结束时间
		sql.append("	   TO_CHAR(TAE.MINISTER_AUDITING_DATE,'yyyy-MM-dd HH24:mi:ss') MINISTER_AUDITING_DATE,\n") ; //审核时间
		sql.append("	   TAE.E_NUM,\n") ; //急救人数
		sql.append("	   TAE.E_NAME,\n") ; //急救人姓名
		sql.append("	   TAE.E_LICENSE_NO,\n") ; //急救车牌号
		sql.append("	   TAE.IS_MAKE_UP,\n") ; //是否已补录
		sql.append("	   TAE.IS_RLATION_ORDER,\n") ; //是否已关联工单
		sql.append("	   TAE.EGRESS_REAMRK,\n") ; //申请内容
		sql.append("	   (select u.name from tc_user u where u.USER_ID=TAE.MINISTER_AUDITING_BY) as MINISTER_AUDITING_BY,\n") ; //审核人
		sql.append("	   TAE.MINISTER_AUDITING_REAMRK,\n") ; //审核备注
		sql.append("       TD.DEALER_CODE,\n") ;  //服务站编码
		sql.append("       TD.DEALER_NAME,\n") ;  //服务站名称
		sql.append("       TD.PHONE,\n") ;  //服务站电话
		sql.append("       TV.VIN,\n") ;  //VIN码
		sql.append("       TV.ENGINE_NO,\n") ;  //发动机号
		sql.append("       TV.MODEL_ID,\n") ; 
		sql.append("       TO_CHAR(TV.PURCHASED_DATE,'yyyy-MM-dd') PURCHASED_DATE,\n" );//购车日期
		sql.append("       TO_CHAR(TV.FACTORY_DATE,'yyyy-MM-dd') FACTORY_DATE,\n" );//出厂日期
		sql.append("       TV.LICENSE_NO,\n" );//车牌号
		sql.append("       ord.SERVICE_ORDER_CODE,\n" );//工单号
		sql.append("       TAE.CUSTOMER_NAME, \n") ; //联系人
		sql.append("       TAE.TELEPHONE,\n") ;  //联系电话
		sql.append("       NVL(c.ctm_name, ' ') CTM_NAME, \n") ;  //车主
		sql.append("       NVL(c.address, ' ') ADDRESS \n") ;  //用户地址
		sql.append("  FROM TT_AS_EGRESS TAE\n" );
		sql.append("  LEFT JOIN TM_DEALER TD ON TAE.DEALER_ID=TD.DEALER_ID\n" );
		sql.append("  LEFT JOIN TM_VEHICLE TV ON TAE.VIN = TV.VIN\n" );
		sql.append("  left join tt_dealer_actual_sales t on tv.vehicle_id=t.vehicle_id\n" );
		sql.append("  left join TT_CUSTOMER_SERVICE c on c.ctm_id=t.ctm_id\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP TVMG ON TV.MODEL_ID = TVMG.GROUP_ID\n" );
		sql.append("  left join TM_VHCL_MATERIAL TVM ON TVM.MATERIAL_ID = TV.MATERIAL_ID\n" );
		sql.append("  left join TT_AS_SERVICE_ORDER ord ON TAE.ORDER_ID = ord.SERVICE_ORDER_ID\n" );
		sql.append("  WHERE 1=1 ");
		if(paraMap.size()!=0){
			if(StringUtils.isNotBlank((String) paraMap.get("id"))){
				sql.append("and TAE.ID = ?");
				listPar.add((String) paraMap.get("id"));
			}
			
			if(StringUtils.isNotBlank((String) paraMap.get("egressNo"))){
				sql.append("and TAE.EGRESS_NO like ?");
				listPar.add("%"+(String) paraMap.get("egressNo")+"%");
			}
			
			if(StringUtils.isNotBlank((String) paraMap.get("startDate"))){
				sql.append(" AND TAE.CREATE_DATE>=to_date('"+(String) paraMap.get("startDate")+"','yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank((String) paraMap.get("endDate"))){
				sql.append("AND TAE.CREATE_DATE<to_date('"+(String) paraMap.get("endDate")+"','yyyy-MM-dd')");
			}
			
			if(StringUtils.isNotBlank((String) paraMap.get("vin"))){
				sql.append("and TAE.VIN like ?");
				listPar.add("%"+(String) paraMap.get("vin")+"%");
			}
			
			if(StringUtils.isNotBlank((String) paraMap.get("status"))){
				sql.append("and TAE.STATUS = ?");
				listPar.add((String) paraMap.get("status"));
			}
			
			if(StringUtils.isNotBlank((String) paraMap.get("ftype"))){
				sql.append("and TAE.STATUS != "+Constant.OUT_MAINTAIN_01+"");
			}
			
			if(StringUtils.isNotBlank((String) paraMap.get("dealerId"))){
				sql.append("and TAE.DEALER_ID in ("+paraMap.get("dealerId")+")");	
			}
		}
		sql.append("  ORDER BY TAE.STATUS ");
		return dao.pageQuery(sql.toString(), listPar, getFunName(), pageSize, curPage);
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*public void insertTACSP(TtAsCustomerServicePO po1) throws Exception {
		fac.insert(po1);
	}*/


	public void insertTAEP(TtAsEgressPO po) throws Exception {
		fac.insert(po);
	}


	@SuppressWarnings("unchecked")
	public void updateStatus(Long id, String status) throws Exception {
		String sql = "update TT_AS_EGRESS set status="+status+" where id="+id+"";
		dao.update(sql, null);
	}


	/*@SuppressWarnings("unchecked")
	public void addData(TtAsWrAuditingPO po) throws Exception {
		dao.insert(po);
	}*/


	@SuppressWarnings("unchecked")
	public void updateTAEP(TtAsEgressPO po) throws Exception {
		TtAsEgressPO po1 = new TtAsEgressPO();
		po1.setId(po.getId());
		dao.update(po1, po);
	}


	@SuppressWarnings("unchecked")
	public void updateStatus(Long id, String status,String ministerAuditingReamrk) throws Exception {
		String sql = "update TT_AS_EGRESS set status="+status+",minister_auditing_reamrk='"+ministerAuditingReamrk+"' where id="+id+"";
		dao.update(sql, null);
		
	}


	@SuppressWarnings("unchecked")
	/*public void updateTACSP(TtAsCustomerServicePO po) throws Exception {
		TtAsCustomerServicePO po1 = new TtAsCustomerServicePO();
		po1.setVin(po.getVin());
		dao.update(po1, po);
	}*/
	public List<Map<String, Object>> getListUserName(String userId) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object>pars = new ArrayList<Object>();
		pars.add(userId);
		sql.append("SELECT TU.NAME,TU.ACNT,TU.PHONE FROM TC_USER TU WHERE TU.USER_ID = ?");
		return dao.pageQuery(sql.toString(), pars, getFunName());
	}
	public List<Map<String, Object>> getDealerInfos(String dealerId)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object>pars = new ArrayList<Object>();
		pars.add(dealerId);
		sql.append("SELECT TD.DEALER_CODE,TD.DEALER_NAME, TD.PHONE FROM TM_DEALER TD WHERE TD.DEALER_ID = ?");
		return dao.pageQuery(sql.toString(), pars, getFunName());
	}
	public List<Map<String, Object>> getName(Long userId) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object>pars = new ArrayList<Object>();
		pars.add(userId);
		sql.append("SELECT TU.NAME,TU.ACNT,TU.PHONE FROM TC_USER TU WHERE TU.USER_ID = ?");
		return dao.pageQuery(sql.toString(), pars, getFunName());
	}

	public List<Map<String, Object>> getDealerStationInfos(String dealerId)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object>pars = new ArrayList<Object>();
		pars.add(dealerId);
		sql.append("SELECT TD.DEALER_CODE,TD.DEALER_NAME, TD.PHONE FROM TM_DEALER TD WHERE TD.DEALER_ID = ?");
		return dao.pageQuery(sql.toString(), pars, getFunName());
	}
	/**
	 * 生成序列号
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> generateNumber(String dealer_Id_name,String dealer_Id_value,String po, String obtainName,String createDateName) throws Exception{
		List list =new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT "
		 		+" "+obtainName+" "
		 		+" from "+po+" "
		 		+" where "+dealer_Id_name+"="+dealer_Id_value+""
		 		+" and TO_CHAR("+createDateName+",'YYYYMMDD')=TO_CHAR(sysdate,'YYYYMMDD') "
		 		+" order by "+createDateName+" desc "
				);
		return dao.pageQuery(sql.toString(),list, getFunName());
	};
	public List<Map<String, Object>> judgeIsVin(Map<String, Object> params)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object>pars = new ArrayList<Object>();
		pars.add(params.get("vin").toString());
		sql.append("select\n" );
		sql.append("t.ctm_id,\n" );
		sql.append("NVL(TV.ENGINE_NO, ' ') ENGINE_NO,\n" );
		sql.append("NVL(c.ctm_name, ' ') CTM_NAME,\n" );
		sql.append("NVL(c.address, ' ') ADDRESS,\n" );
		sql.append("NVL((select Group_Name from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL=3 and group_id=tv.model_id), ' ') Group_Name,\n" );
		sql.append("NVL((select Group_Name from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL=2 and group_id=tv.SERIES_ID), ' ') SE_Name,\n" );
		sql.append("NVL((select Group_Name from TM_VHCL_MATERIAL_GROUP where GROUP_LEVEL=4 and group_id=tv.PACKAGE_ID), ' ') PZ_Name,\n" );
		sql.append("NVL(TO_CHAR(TV.FACTORY_DATE, 'YYYY-MM-DD'), ' ') FACTORY_DATE,\n" );
		sql.append("NVL(TO_CHAR(TV.PURCHASED_DATE, 'YYYY-MM-DD'), ' ') PURCHASED_DATE,\n" );
		sql.append("NVL(TO_CHAR(TV.MILEAGE), ' ') HISTORY_MILE,\n" );
		sql.append("NVL(TV.LICENSE_NO, ' ') LICENSE_NO\n" );
		sql.append(" from TM_VEHICLE tv\n" );
		sql.append("left join tt_dealer_actual_sales t on tv.vehicle_id=t.vehicle_id\n" );
		sql.append("left join TM_VHCL_MATERIAL tm on tm.material_id=tv.material_id\n" );
		sql.append("left join TT_CUSTOMER_SERVICE c on c.ctm_id=t.ctm_id\n" );
		sql.append("where 1=1 ");
		sql.append("   AND TV.VIN = ?");

		return dao.pageQuery(sql.toString(), pars, getFunName());
	}
	public List<Map<String, Object>> specialRecord(String id) {
		StringBuffer sql= new StringBuffer();
		sql.append("select g.id,\n" );
		sql.append("       g.EGRESS_ID,\n" );
		sql.append("       g.audit_record,\n" );
		sql.append("       TO_CHAR(g.audit_date, 'YYYY-MM-DD hh24:mi:ss') audit_date,\n" );
		sql.append("       g.audit_by,\n" );
		sql.append("       u.name,\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=g.opera_ststus) as opera_ststus\n" );
		sql.append("  from tt_as_wr_egress_record g left join tc_user u on g.audit_by=u.user_Id \n" );
		sql.append(" where 1=1 ");

		DaoFactory.getsql(sql, "g.EGRESS_ID", id, 1);
		return this.pageQuery(sql.toString(), null, getFunName());
	}
}
