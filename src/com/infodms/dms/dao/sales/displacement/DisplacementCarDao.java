package com.infodms.dms.dao.sales.displacement;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ConstantUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.po.TtVsCarDisplacementCekPO;
import com.infodms.dms.po.TtVsCarDisplacementDjPO;
import com.infodms.dms.po.TtVsCarDisplacementDlrPO;
import com.infodms.dms.po.TtVsCarDisplacementPrcPO;
import com.infodms.dms.po.TtVsCostPO;
import com.infodms.dms.po.TtVsUsedCarDisplacementPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.infox.commons.httpclient.util.DateParseException;
import com.infoservice.infox.commons.httpclient.util.DateUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DisplacementCarDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(DealerInfoDao.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final DisplacementCarDao dao = new DisplacementCarDao ();
	public static final DisplacementCarDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/***
	 *查询"当前登录经销商实销的"的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public  PageResult<Map<String, Object>> getDisplancementCar(String vin,String dealerIds, int pageSize,int curPage){
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT VOD.ROOT_ORG_NAME,\n");
		sql.append("VOD.REGION_NAME,\n");
		sql.append(" TMV.VIN,\n");
		sql.append("TO_CHAR(TDAS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,\n");
		sql.append(" TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE,\n");
		sql.append("TMV.VEHICLE_ID,\n");
		sql.append("TMV.ENGINE_NO,\n");
		sql.append("TDAS.dealer_id\n");
		sql.append(" FROM TT_DEALER_ACTUAL_SALES TDAS, TM_VEHICLE TMV, vw_org_dealer VOD,tt_customer TTC\n");
		sql.append(" WHERE TDAS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append(" AND TDAS.CTM_ID = TTC.CTM_ID AND TTC.IS_SECOND = 10041001 \n");	 
		sql.append(" AND TDAS.DEALER_ID = VOD.DEALER_ID AND tdas.displacement_status = ").append(Constant.STATUS_DISABLE).append(" \n");
		if(null!=dealerIds && !"".equals(dealerIds)){
		sql.append(" AND TMV.DEALER_ID IN ("+dealerIds+")\n");
		}
		sql.append(" AND TMV.LIFE_CYCLE="+Constant.VEHICLE_LIFE_04+"\n");
		sql.append(" AND TMV.DEALER_ID=TDAS.DEALER_ID  AND TDAS.IS_RETURN ="+Constant.IF_TYPE_NO+"\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}

		sql.append("and not exists\n") ;
		sql.append(" (select tvucd.displacement_id\n") ;
		sql.append("          from tt_vs_used_car_displacement tvucd\n") ;
		sql.append("         where tvucd.vehicle_id = tmv.vehicle_id\n") ;
		sql.append("           and tvucd.operate_status in (").append(Constant.DisplancementCarrequ_cek_1).append(",\n") ;
		sql.append("                                        ").append(Constant.DisplancementCarrequ_cek_2).append(",\n") ;
		sql.append("                                        ").append(Constant.DisplancementCarrequ_cek_3).append(",\n") ;
		sql.append("                                        ").append(Constant.DisplancementCarrequ_cek_4).append(",\n") ;
		sql.append("                                        ").append(Constant.DisplancementCarrequ_cek_5).append(",\n") ;
		sql.append("                                        ").append(Constant.DisplancementCarrequ_cek_SYBQS).append("))") ;

		return dao.pageQuery(sql.toString(),null,"com.infodms.dms.dao.sales.displacement.DisplacementCarDao.getDisplancementCar",pageSize, curPage);
		
	}
	public  int insertDisplace(Map map){
		try {
		TtVsUsedCarDisplacementPO po=new TtVsUsedCarDisplacementPO();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		po.setDisplacementId(Long.parseLong(map.get("disId").toString()));
		
		if (map.get("OEM_COMPANY_ID")!=null && Utility.testString(map.get("OEM_COMPANY_ID").toString())) {
		po.setOemCompanyId(Long.parseLong(map.get("OEM_COMPANY_ID").toString())) ;
		}
		if (map.get("ORDER_ID")!=null && Utility.testString(map.get("ORDER_ID").toString())) {
			po.setSalesOrderId(Long.parseLong(map.get("ORDER_ID").toString()));
		}
		if (map.get("VEHICLE_ID")!=null && Utility.testString(map.get("VEHICLE_ID").toString())) {
			po.setVehicleId(Long.parseLong(map.get("VEHICLE_ID").toString()));
		}
		if (map.get("DEALERID")!=null && Utility.testString(map.get("DEALERID").toString())) {
			po.setDealerId(Long.parseLong(map.get("DEALERID").toString()));
		}
		if (map.get("displacement_type")!=null && Utility.testString(map.get("displacement_type").toString())) {
			po.setDisplacementType(Integer.parseInt(map.get("displacement_type").toString()));
		}
		if (map.get("OPERATE_STATUS")!=null && Utility.testString(map.get("OPERATE_STATUS").toString())) {
			po.setOperateStatus(Integer.parseInt(map.get("OPERATE_STATUS").toString()));
		}
		if (map.get("OLD_BRAND_NAME")!=null && Utility.testString(map.get("OLD_BRAND_NAME").toString())) {
		po.setOldBrandName(map.get("OLD_BRAND_NAME").toString());
		}
		if (map.get("OLD_VIN")!=null && Utility.testString(map.get("OLD_VIN").toString())) {
		po.setOldVin(map.get("OLD_VIN").toString());
		}
		if (map.get("OLD_MODEL_NAME")!=null && Utility.testString(map.get("OLD_MODEL_NAME").toString())) {
		po.setOldModelName(map.get("OLD_MODEL_NAME").toString());
		}
		if (map.get("OLD_SLES_DATE")!=null && Utility.testString(map.get("OLD_SLES_DATE").toString())) {
		po.setOldSlesDate(df.parse(map.get("OLD_SLES_DATE").toString()));
		}		//fanliPrc
		if(map.get("fanliPrc")!=null && Utility.testString(map.get("fanliPrc").toString())){
			po.setPriceAmount(Double.parseDouble(map.get("fanliPrc").toString()));
		}
		if(map.get("CTM_NAME")!=null && Utility.testString(map.get("CTM_NAME").toString())){
			po.setHostName(map.get("CTM_NAME").toString());
		}
		if(map.get("IS_CHANA")!=null && Utility.testString(map.get("IS_CHANA").toString())){
			po.setIsChana(Long.parseLong(map.get("IS_CHANA").toString()));
		}
		if(map.get("remark")!=null && Utility.testString(map.get("remark").toString())){
			po.setRemark(map.get("remark").toString());
		}
		if(map.get("SCRAP_CERTIFY_NO")!=null && Utility.testString(map.get("SCRAP_CERTIFY_NO").toString())){
			po.setScrapCertifyNo(map.get("SCRAP_CERTIFY_NO").toString());
		}
		if(map.get("SCRAP_DATE")!=null && Utility.testString(map.get("SCRAP_DATE").toString())){
			po.setScrapDate(df.parse(map.get("SCRAP_DATE").toString()));
		}
		po.setOperateStatus(Constant.DisplancementCarrequ_cek_1);
		po.setCreateBy(Long.parseLong(map.get("useid").toString()));
		po.setCreateDate(new Date());
		po.setDisplacementNo(map.get("displacement_no").toString());
		dao.insert(po);	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 1;
	}
	/**
	 * 查询该车2手车置换车辆信息
	 */
	
	public  Map<String, Object> getDisplanceNewCarInfo(String vin,String dealerId) {
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT TVMG.GROUP_CODE,TO_DATE(TO_CHAR(TDAS.SALES_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') salesdate,\n");
		sql.append(" DECODE(TBA.ERP_CODE,726,'重庆',82,'重庆',142,'河北',197,'南京') CODE_DESC,das.order_id,TMV.OEM_COMPANY_ID,\n");
		sql.append(" c.ctm_name,TMV.VEHICLE_ID ");
        sql.append(" FROM TM_VEHICLE  TMV,\n");
        sql.append("TT_DEALER_ACTUAL_SALES TDAS,tt_dealer_actual_sales das,tt_customer c,\n");
        sql.append("TM_VHCL_MATERIAL_GROUP TVMG,\n");
        sql.append("TM_BUSINESS_AREA TBA\n");
        sql.append("WHERE TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n");
        sql.append("AND TMV.MODEL_ID = TVMG.GROUP_ID\n");
        sql.append("AND TMV.AREA_ID=TBA.AREA_ID AND TMV.VEHICLE_ID = das.vehicle_id AND das.ctm_id = c.ctm_id\n");
        sql.append("AND TMV.LIFE_CYCLE=10321004\n");
        sql.append("AND TMV.VIN='"+vin+"'\n");
        if(!CommonUtils.isNullString(dealerId)) {
        	sql.append("AND TMV.DEALER_ID IN("+dealerId+")");
        }
		
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 查询该车的 车辆信息 客户信息
	 * 在进行二手车置换申请
	 * @param vehicleId
	 * @return
	 */
	
	public  Map<String, Object> getDisplanceMentInfo(String vehicleId) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VEHICLE_ID, G3.GROUP_NAME BRAND, --品牌               \n");         
		sql.append("TMV.VIN,                   --VIN      \n");
		sql.append(" TMV.ENGINE_NO, --发动机号\n");
		sql.append(" SALES.CTM_ID,  --客户信息ID\n");
		sql.append("G1.GROUP_NAME SERIES_NAME,    --车系 \n");
		sql.append("G2.GROUP_NAME MODEL_NAME,       --车型 \n");
		sql.append(" TMVM.MATERIAL_CODE,        --物料代码    \n");
		sql.append("TMVM.MATERIAL_NAME,        --物料名称 \n");
		sql.append("TMVM.COLOR_NAME AS COLOR,   --颜色  \n");
       sql.append("TO_CHAR(SALES.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,    --实销日期 \n");
       sql.append("TC.CTM_NAME,  --客户名称\n");
       sql.append("TC.PROFESSION,  --职业\n");
       sql.append("TC.JOB,  --职务\n");
       sql.append("TC.COMPANY_NAME,--公司名称");
       sql.append("TC.CARD_TYPE,  --证件类型\n");
       sql.append("TC.CARD_NUM,  --证件编号\n");
       sql.append("TC.MAIN_PHONE, --主要联系电话\n");
       sql.append("TC.OTHER_PHONE, --其他联系电话\n");
       sql.append(" TC.COMPANY_S_NAME, --公司简称\n");
       sql.append(" TC.COMPANY_PHONE, --公司电话\n");
       sql.append("TC.LEVEL_ID,--公司规模");
       sql.append("TC.KIND, --公司性质\n");
       sql.append("TC.VEHICLE_NUM,  --目前车辆数\n");
       sql.append("TC.CTM_FORM, --客户来源\n");
       sql.append("TC.PROVINCE, --省\n");
       sql.append("TC.CITY,  --市\n");
       sql.append("TC.TOWN,  --县\n");
       sql.append("TC.SEX, --性别\n");
       sql.append(" TC.ADDRESS, --地址\n");
       sql.append(" TC.IS_SECOND, --是否二手车置换\n");
       sql.append("TC.CTM_TYPE, --客户类型\n");
       sql.append("SALES.IS_OLD_CTM,               \n");
       sql.append("SALES.CAR_CHARACTOR,             \n");
       sql.append("SALES.ORDER_ID,                 \n");
       sql.append("G2.GROUP_ID,  TBA.PRODUCE_BASE, \n");
       sql.append("SALES.ORDER_ID,TMV.OEM_COMPANY_ID,SALES.DEALER_ID  FROM TM_VEHICLE TMV,TM_BUSINESS_AREA TBA, \n");
       sql.append("TM_VHCL_MATERIAL_GROUP G1, \n");
       sql.append("TM_VHCL_MATERIAL_GROUP G2, TM_VHCL_MATERIAL_GROUP G3,\n");
       sql.append("TM_VHCL_MATERIAL TMVM, \n");
       sql.append("TT_DEALER_ACTUAL_SALES SALES,\n");
       sql.append("TT_CUSTOMER TC\n");
       sql.append("WHERE     TMV.SERIES_ID = G1.GROUP_ID AND TMV.AREA_ID = TBA.AREA_ID \n");
       sql.append(" AND TMV.MODEL_ID = G2.GROUP_ID AND G1.PARENT_GROUP_ID=G3.GROUP_ID\n");
       sql.append(" AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID \n");
       sql.append("AND TMV.VEHICLE_ID = SALES.VEHICLE_ID(+)\n");
       sql.append(" AND SALES.CTM_ID=TC.CTM_ID AND SALES.IS_RETURN =10041002\n");
       sql.append("  AND TMV.VEHICLE_ID = "+vehicleId+" \n");
       sql.append("  ORDER BY SALES.ORDER_ID DESC \n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
			
	/**
	 * 查询该车的 车辆信息 客户信息
	 * 在进行二手车置换申请
	 * @param vehicleId
	 * @return
	 */
	
	public  Map<String, Object> getDisplanceMentVinInfo(String vin) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VEHICLE_ID, TMV.DEALER_ID,G3.GROUP_NAME BRAND, --品牌               \n");         
		sql.append("TMV.VIN,                   --VIN      \n");
		sql.append(" TMV.ENGINE_NO, --发动机号\n");
		sql.append(" SALES.CTM_ID,  --客户信息ID\n");
		sql.append("G1.GROUP_NAME SERIES_NAME,    --车系 \n");
		sql.append("G2.GROUP_NAME MODEL_NAME,       --车型 \n");
		sql.append(" TMVM.MATERIAL_CODE,        --物料代码    \n");
		sql.append("TMVM.MATERIAL_NAME,        --物料名称 \n");
		sql.append("TMVM.COLOR_NAME AS COLOR,   --颜色  \n");
       sql.append("TO_CHAR(SALES.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,    --实销日期 \n");
       sql.append("TC.CTM_NAME,  --客户名称\n");
       sql.append("TC.PROFESSION,  --职业\n");
       sql.append("TC.JOB,  --职务\n");
       sql.append("TC.COMPANY_NAME,--公司名称");
       sql.append("TC.CARD_TYPE,  --证件类型\n");
       sql.append("TC.CARD_NUM,  --证件编号\n");
       sql.append("TC.MAIN_PHONE, --主要联系电话\n");
       sql.append("TC.OTHER_PHONE, --其他联系电话\n");
       sql.append(" TC.COMPANY_S_NAME, --公司简称\n");
       sql.append(" TC.COMPANY_PHONE, --公司电话\n");
       sql.append("TC.LEVEL_ID,--公司规模");
       sql.append("TC.KIND, --公司性质\n");
       sql.append("TC.VEHICLE_NUM,  --目前车辆数\n");
       sql.append("TC.CTM_FORM, --客户来源\n");
       sql.append("TC.PROVINCE, --省\n");
       sql.append("TC.CITY,  --市\n");
       sql.append("TC.TOWN,  --县\n");
       sql.append("TC.SEX, --性别\n");
       sql.append(" TC.ADDRESS, --地址\n");
       sql.append(" TC.IS_SECOND, --是否二手车置换\n");
       sql.append("TC.CTM_TYPE, --客户类型\n");
       sql.append("SALES.IS_OLD_CTM,               \n");
       sql.append("SALES.CAR_CHARACTOR,             \n");
       sql.append("SALES.ORDER_ID,                 \n");
       sql.append("G2.GROUP_ID,                    \n");
       sql.append("SALES.ORDER_ID  FROM TM_VEHICLE TMV, \n");
       sql.append("TM_VHCL_MATERIAL_GROUP G1, \n");
       sql.append("TM_VHCL_MATERIAL_GROUP G2, TM_VHCL_MATERIAL_GROUP G3,\n");
       sql.append("TM_VHCL_MATERIAL TMVM, \n");
       sql.append("TT_DEALER_ACTUAL_SALES SALES,\n");
       sql.append("TT_CUSTOMER TC\n");
       sql.append("WHERE     TMV.SERIES_ID = G1.GROUP_ID \n");
       sql.append(" AND TMV.MODEL_ID = G2.GROUP_ID AND G1.PARENT_GROUP_ID=G3.GROUP_ID\n");
       sql.append(" AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID \n");
       sql.append("AND TMV.VEHICLE_ID = SALES.VEHICLE_ID(+)\n");
       sql.append(" AND SALES.CTM_ID=TC.CTM_ID\n");
       sql.append("  AND TMV.VIN= '"+vin+"' \n");
       sql.append("  ORDER BY SALES.ORDER_ID DESC \n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
			
	public  PageResult<Map<String, Object>> getDisplancementQueryCar(String userId,String displacement_type, int pageSize,int curPage){
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT TVUCD.OLD_VIN,\n");
		sql.append("TVUCD.NEW_VIN,\n");
		sql.append("TO_DATE(TO_CHAR(TVUCD.NEW_SALES_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') NEW_SALES_DATE,\n");
		sql.append("TVUCD.DISPLACEMENT_TYPE,\n");
		sql.append("TVUCD.OPERATE_STATUS\n");
		sql.append("FROM TT_VS_USED_CAR_DISPLACEMENT TVUCD\n");
		sql.append(" WHERE TVUCD.DISPLACEMENT_TYPE="+displacement_type+"\n");
		sql.append(" AND TVUCD.CREATE_BY="+userId+"\n");
		return dao.pageQuery(sql.toString(),null,dao.getFunName(),pageSize, curPage);
	}
			
	public  PageResult<Map<String, Object>> getDisplancementQueryOrgCar(String orgId,String userId,String displacement_type, int pageSize,int curPage){
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT DISTINCT TVUCD.DISPLACEMENT_ID,TVUCD.OLD_BRAND_NAME,\n");
		sql.append(" TVUCD.OLD_MODEL_NAME,\n");
		sql.append(" TVUCD.OLD_VIN,\n");
       sql.append(" TO_DATE(TO_CHAR(TVUCD.OLD_SLES_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') OLD_SLES_DATE,\n");
       sql.append(" TVUCD.NEW_VIN,\n");
       sql.append(" TO_DATE(TO_CHAR(TVUCD.NEW_SALES_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') NEW_SALES_DATE,\n");
       sql.append(" TVUCD.NEW_AREA,\n");
       sql.append(" TVUCD.NEW_MODEL_NAME,\n");
       sql.append(" TO_DATE(TO_CHAR(TVUCD.SCRAP_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') SCRAP_DATE,\n");
       sql.append(" TVUCD.SCRAP_CERTIFY_NO,\n");
       sql.append(" TVUCD.OPERATE_STATUS,\n");
       sql.append(" TMO.ORG_ID\n");
       sql.append(" FROM TC_USER                     TCU,\n");
       sql.append(" TM_DEALER                   TMD,\n");
       sql.append(" TM_ORG                      TMO,\n");
       sql.append("TM_DEALER_ORG_RELATION      TDOR,\n");
       sql.append("TT_VS_USED_CAR_DISPLACEMENT TVUCD\n");
       sql.append("WHERE  TVUCD.DISPLACEMENT_TYPE="+displacement_type+"\n");
       if(!orgId.equals(Constant.OEM_COM_SVC)){
       sql.append("AND TMO.ORG_ID="+orgId+" AND TVUCD.OPERATE_STATUS="+Constant.DisplancementCarrequ_cek_1+"\n");
       }else{
    	   sql.append("AND TVUCD.OPERATE_STATUS="+Constant.DisplancementCarrequ_cek_2+"\n");
       }
       sql.append("AND TMD.COMPANY_ID = TCU.COMPANY_ID \n");
       sql.append(" AND TVUCD.CREATE_BY = TCU.USER_ID\n");
       sql.append(" AND TMD.DEALER_ID = TDOR.DEALER_ID\n");
       sql.append(" AND TMO.ORG_ID = TDOR.ORG_ID\n");
		return dao.pageQuery(sql.toString(),null,dao.getFunName(),pageSize, curPage);
	}	
			
	public  Map<String, Object> getDisplanceQueryOrgInfo(String displacementId) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT DISTINCT TVUCD.OLD_BRAND_NAME,\n");
		sql.append(" TVUCD.DISPLACEMENT_ID,TVUCD.DISPLACEMENT_TYPE,\n");
		sql.append(" TVUCD.OLD_MODEL_NAME,TMV.VEHICLE_ID,\n");
		sql.append(" TVUCD.OLD_VIN,\n");
		sql.append(" TO_DATE(TO_CHAR(TVUCD.OLD_SLES_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') OLD_SLES_DATE,\n");
		sql.append(" TVUCD.NEW_VIN,\n");
		sql.append("  TO_DATE(TO_CHAR(TVUCD.NEW_SALES_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') NEW_SALES_DATE,\n");
		sql.append("  TVUCD.NEW_AREA,\n");
		sql.append("  TVUCD.NEW_MODEL_NAME,\n");
		sql.append("  TO_DATE(TO_CHAR(TVUCD.SCRAP_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') SCRAP_DATE,\n");
		sql.append(" TVUCD.SCRAP_CERTIFY_NO,\n");
		sql.append(" TVUCD.OPERATE_STATUS\n");
		sql.append("  FROM TT_VS_USED_CAR_DISPLACEMENT TVUCD,TM_VEHICLE TMV  \n");
		sql.append("  WHERE TVUCD.DISPLACEMENT_ID="+displacementId+"  AND    TMV.VIN(+)=TVUCD.OLD_VIN\n");
			
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
			
	public  int insertDisplaceCek(String checkres,String cekremark,String userid,String DISPLACEMENT_ID){
		TtVsCarDisplacementCekPO po=new TtVsCarDisplacementCekPO();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		po.setCheckId (Long.parseLong(SequenceManager.getSequence("")));
		po.setDisplacementId(Long.parseLong(DISPLACEMENT_ID));
		if(checkres.equals("1")){
			po.setStatus(Constant.DisplancementCarrequ_cek_2);
		}else{
			po.setStatus(Constant.DisplancementCarrequ_cek_3);
		}
		po.setCreateBy(Long.parseLong(userid));
		po.setOpinion(cekremark);
		po.setCreateDate(new Date());
		dao.insert(po);
		return 1;
	}
	/***
	 *查询"当前的价格"
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public  PageResult<Map<String, Object>> getDisplancementCarPrc(String DISPLACEMENT_TYPE, int pageSize,int curPage){
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT TVCDP.DISPLACEMENT_PRC,TVCDP.DISPLACEMENT_PRICE_ID,TVCDP.DISPLACEMENT_TYPE,TVCDP.PRICE,TO_CHAR(TVCDP.CREATE_DATE, 'yyyy-MM-dd') CREATE_DATE,TO_CHAR(TVCDP.UPDATE_DATE, 'yyyy-MM-dd') UPDATE_DATE FROM TT_VS_CAR_DISPLACEMENT_PRC TVCDP\n");
		if (null != DISPLACEMENT_TYPE && !"".equals(DISPLACEMENT_TYPE)) {
			sql.append(" TVCDP.DISPLACEMENT_TYPE LIKE '%"+DISPLACEMENT_TYPE+"%'\n");
		}
		return dao.pageQuery(sql.toString(),null,dao.getFunName(),pageSize, curPage);
		
	}
	/**
	 * 插入资格价格		
	 * @param checkres
	 * @param cekremark
	 * @param userid
	 * @param DISPLACEMENT_ID
	 * @return
	 */
	public  int insertDisplacePrc(String prcId,Long userId,String displacementType,String price,String displacementPrc){
		TtVsCarDisplacementPrcPO po =new TtVsCarDisplacementPrcPO();
		po.setCreateBy(userId);
		po.setCreateDate(new Date());
		po.setDisplacementType(displacementType);
		po.setPrice(Double.parseDouble(price));
		po.setDisplacementPrc(displacementPrc);
		po.setDisplacementPriceId(Long.parseLong(prcId));
		dao.insert(po);
		return 1;
	}
			
	/**
	 * 修改资格类型价格
	 */
	public  int updateDisplacePrc(String displacementPriceId,Long userId,String displacementType,String price,String displacementPrc){
		TtVsCarDisplacementPrcPO po1=new TtVsCarDisplacementPrcPO();
		po1.setDisplacementPriceId(Long.parseLong(displacementPriceId));
		TtVsCarDisplacementPrcPO po2=new TtVsCarDisplacementPrcPO();
		po2.setUpdateBy(userId);
		po2.setUpdateDate(new Date());
		po2.setDisplacementType(displacementType);
		po2.setDisplacementPrc(displacementPrc);
		po2.setPrice(Double.parseDouble( price));
		dao.update(po1, po2);
		return 1;
	}
			
	public  Map<String, Object> getCarPrcOneInfo(String displacementPriceId) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT TVCDP.DISPLACEMENT_TYPE,TVCDP.PRICE,TVCDP.DISPLACEMENT_PRC,TVCDP.DEALER_ID FROM TT_VS_CAR_DISPLACEMENT_PRC TVCDP  WHERE  TVCDP.DISPLACEMENT_PRICE_ID="+displacementPriceId+" \n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
			
	public  PageResult<Map<String, Object>> getDisplancementOpenQuery(String yieldly,int pageSize,int curPage){
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT TVUCD.NEW_VIN,TVUCD.DISPLACEMENT_ID,\n");
		sql.append("TO_DATE(TO_CHAR(TVUCD.NEW_SALES_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') NEW_SALES_DATE,\n");
		sql.append("TVUCD.NEW_AREA,\n");
		sql.append("TVUCD.NEW_MODEL_NAME\n");
		sql.append("FROM TT_VS_USED_CAR_DISPLACEMENT TVUCD,TT_VS_CAR_DISPLACEMENT_CEK TVCDC,TC_CODE TC\n");
		sql.append("WHERE TVUCD.DISPLACEMENT_ID=TVCDC.DISPLACEMENT_ID AND TC.CODE_DESC = TVUCD.NEW_AREA\n");
		sql.append("AND TVCDC.STATUS="+Constant.DisplancementCarrequ_cek_4+"\n");
		sql.append("AND TVUCD.STATUS=0\n");
		sql.append("AND TC.CODE_ID="+yieldly);
		return dao.pageQuery(sql.toString(),null,dao.getFunName(),pageSize, curPage);
	}
	public  int saveDisplanment(String reportCode,String userName, String date,String [] displacementId,String reportId,String yieldly){
		for(int i=0;i<displacementId.length;i++){
		TtVsCarDisplacementDjPO po=new TtVsCarDisplacementDjPO();
		po.setDjId(Long.parseLong(reportId));
		po.setDjCode(reportCode);
		po.setDisplacementId(Long.parseLong(displacementId[i]));
		po.setOrderCreateDate(CommonUtils.parseDate(date));
		po.setOrderCreateName(userName);
		po.setOrderArea(yieldly);
		dao.insert(po);
		}
		return 1;
	}
	
	public  PageResult<Map<String, Object>> getDisplancementDjQuery(String reportCode,String dealerName,String yieldly,int pageSize,int curPage){
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT DISTINCT TVCDD.DJ_CODE,TVCDD.DJ_ID,\n");
		sql.append(" TVCDD.ORDER_CREATE_NAME,\n");
		sql.append("TVCDD.ORDER_AREA\n");
		sql.append("FROM TT_VS_CAR_DISPLACEMENT_DJ TVCDD,\n");
		sql.append(" TT_VS_USED_CAR_DISPLACEMENT TVUCD\n");
		sql.append("WHERE TVUCD.DISPLACEMENT_ID = TVCDD.DISPLACEMENT_ID\n");
		sql.append("AND TVUCD.STATUS=1\n");
		if(null!=reportCode && !"".equals(reportCode)){
			sql.append("AND TVCDD.DJ_CODE like '%"+reportCode+"%'\n");
		}
		if(null!=dealerName && !"".equals(dealerName)){
			sql.append("AND TVCDD.ORDER_CREATE_NAME LIKE '%"+dealerName+"%'\n");
		}
		if(null!=yieldly && !"".equals(yieldly)){
			sql.append("AND TVCDD.ORDER_AREA="+yieldly+"\n");
		}
		return dao.pageQuery(sql.toString(),null,dao.getFunName(),pageSize, curPage);
	}
	
	public  Map<String, Object> getDisplanceDj(String djId) {
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT TVCDD.DJ_CODE,\n");
		sql.append("TVCDD.ORDER_CREATE_NAME,\n");
		sql.append("TVCDD.ORDER_AREA,\n");
		sql.append("TVCDD.ORDER_CREATE_DATE\n");
		sql.append("FROM TT_VS_CAR_DISPLACEMENT_DJ TVCDD\n");
		sql.append("WHERE TVCDD.DJ_ID ="+djId+"\n");
		
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	
	public  List<Map<String, Object>> getDisplanceDjList(String djId) {
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT TVUCD.NEW_VIN,\n");
		sql.append("TO_DATE(TO_CHAR(TVUCD.NEW_SALES_DATE,'YYYY-MM-DD'),'YYYY-MM-DD') NEW_SALES_DATE,\n");
		sql.append(" TVUCD.NEW_AREA,\n");
		sql.append("TVUCD.NEW_MODEL_NAME\n");
		sql.append("FROM TT_VS_CAR_DISPLACEMENT_DJ TVCDD, TT_VS_USED_CAR_DISPLACEMENT TVUCD\n");
		sql.append("WHERE TVCDD.DISPLACEMENT_ID = TVUCD.DISPLACEMENT_ID \n");
		sql.append("AND TVCDD.DJ_ID ="+djId+"\n");
		
		return dao.pageQuery(sql.toString(),null,dao.getFunName());
	}
	
	public  String getReturnPrice(String dealerId, String disType) {
		String price = "-1" ;
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select nvl(tvcdp.price, -1) price\n");
		sql.append("  from TT_VS_CAR_DISPLACEMENT_PRC tvcdp, TT_VS_CAR_DISPLACEMENT_DLR tvcdd\n");  
		sql.append(" where tvcdp.displacement_prc = tvcdd.displacement_prc\n");  
		sql.append("   and tvcdd.dealer_id = ").append(dealerId).append("\n");  
		sql.append("   and tvcdp.displacement_type = ").append(disType).append("\n");  
		
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(map)) {
			price = map.get("PRICE").toString() ;
		}	
		return price ;
	}			
	
	public PageResult<Map<String, Object>> headInfoQuery(Map<String, String> map, int pageSize,int curPage) {
		String orgId = map.get("orgId") ;
		String disNo = map.get("disNo") ;
		String status = map.get("status") ;
		String type = map.get("type") ;
		String region = map.get("region") ;
		String dealerId = map.get("dealerId") ;
		String base = map.get("base") ;
		String newVin = map.get("newVin") ;
		String oldVin = map.get("oldVin") ;
		String clientName = map.get("clientName") ;
		String startDate = map.get("startDate");
		String endDate = map.get("endDate");
		String areas = map.get("areas") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select distinct tvu.displacement_no,\n");
		sql.append("       tvu.displacement_id,\n");  
		sql.append("       tvu.vehicle_id,\n");  
		sql.append("       vod.root_org_name,\n");  
		sql.append("       tmr.region_name,\n");  
		sql.append("       vod.dealer_name root_dealer_name,\n");  
		sql.append("       tvu.displacement_type,\n");  
		sql.append("       tvu.operate_status,\n");  
		sql.append("       tmv.vin new_vin,\n");  
		sql.append("       tba.produce_base,\n");  
		sql.append("       tvu.host_name,\n");
		sql.append("	   tdas.sales_date,\n");
		sql.append("		tvu.old_vin\n");
		sql.append("  from tt_vs_used_car_displacement tvu,\n");  
		sql.append("       vw_org_dealer               vod,\n");  
		sql.append("       tm_dealer_business_area     tdba,\n");  
		sql.append("       tm_dealer                   tmd,\n");  
		sql.append("       tm_region                   tmr,\n");  
		sql.append("       tm_vehicle                  tmv,\n");  
		sql.append("       tm_business_area            tba,\n");  
		sql.append("	   tt_dealer_actual_sales      tdas\n");
		sql.append(" where tvu.dealer_id = vod.dealer_id\n");  
		sql.append("   and vod.dealer_id = tmd.dealer_id\n");  
		sql.append("   and tmd.dealer_id = tdba.dealer_id\n");  
		sql.append("   and tmd.province_id = tmr.region_code(+)\n");  
		sql.append("   and tvu.vehicle_id = tmv.vehicle_id\n");  
		sql.append("   and tmv.area_id = tba.area_id\n");
		sql.append("   and tdas.vehicle_id = tvu.vehicle_id\n");
		
		//modify by WHX,2012.09.14
		//=======================================================================Start
		sql.append("   and tdas.order_id = tvu.sales_order_id\n");
		/*sql.append("   and tdas.is_return = ").append(Constant.IF_TYPE_NO).append("\n");*/
		//=======================================================================End
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   and vod.root_org_id = ").append(orgId).append("\n");
		}
		
		if(!CommonUtils.isNullString(disNo)) {
			sql.append("   and tvu.displacement_no like '%").append(disNo).append("%'\n");
		}

		if(!CommonUtils.isNullString(status)) {
			sql.append("   and tvu.operate_status = ").append(status).append("\n");
		}

		if(!CommonUtils.isNullString(type)) {
			sql.append("   and tvu.displacement_type = ").append(type).append("\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("   and tmd.province_id = ").append(region).append("\n");
		}
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("   and vod.dealer_id in (").append(dealerId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(base)) {
			sql.append("   and tba.produce_base = ").append(base).append("\n");
		}
		
		if(!CommonUtils.isNullString(newVin)) {
			sql.append(GetVinUtil.getVins(newVin, "tmv"));
		}
		if(!CommonUtils.isNullString(oldVin)) {
			sql.append(GetVinUtil.getVinsNew(oldVin, "tvu"));
		}
		
		if(!CommonUtils.isNullString(clientName)) {
			sql.append("	and tvu.host_name like '%");
			sql.append(clientName);
			sql.append("%'\n");
		}
		
		if(!CommonUtils.isNullString(endDate)) {
			if(CommonUtils.isNullString(startDate)) {
				sql.append("	and tdas.sales_date <= to_date('");
				sql.append(endDate);
				sql.append("', 'yyyy-mm-dd')\n");
			} else {
				sql.append("	and tdas.sales_date >= to_date('");
				sql.append(startDate);
				sql.append("', 'yyyy-mm-dd')\n");
				sql.append("	and tdas.sales_date <= to_date('");
				sql.append(endDate);
				sql.append("', 'yyyy-mm-dd')\n");
			}
		}
		
		sql.append("   and tdba.area_id in (").append(areas).append(")\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage) ;
	}
	
	public Map<String, Object> getDisInfo(String disId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvucd.displacement_id,\n");
		sql.append("       tvucd.vehicle_id,\n");  
		sql.append("       tvucd.displacement_type,\n");  
		sql.append("       tvucd.old_vin,\n");  
		sql.append("       tvucd.old_brand_name,\n"); 
		sql.append("       tvucd.old_model_name,\n");  
		sql.append("       to_char(tvucd.old_sles_date, 'yyyy-mm-dd') old_sles_date,\n"); 
		sql.append("       tvucd.price_amount,\n"); 
		sql.append("       to_char(tvucd.scrap_date, 'yyyy-mm-dd') scrap_date,\n"); 
		sql.append("       tvucd.scrap_certify_no,\n"); 
		sql.append("       tvucd.host_name,\n"); 
		sql.append("       tvucd.remark\n");  
		sql.append("  from TT_VS_USED_CAR_DISPLACEMENT tvucd\n");  
		sql.append(" where tvucd.displacement_id = ").append(disId).append("\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName()) ;
	}
	
	public List<Map<String, Object>> getCheckLog(String disId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvc.status,\n");
		sql.append("       tvc.opinion,\n");  
		sql.append("       tcu.name,\n");  
		sql.append("       to_char(tvc.check_date, 'yyyy-mm-dd') check_date,\n");  
		sql.append("       tvc.check_id\n");  
		sql.append("  from TT_VS_CAR_DISPLACEMENT_CEK tvc, tc_user tcu\n");  
		sql.append(" where tvc.create_by = tcu.user_id and tvc.displacement_id =").append(disId).append("\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
	
	public List<Map<String, Object>> getCheckIdByStatus(String disId, String status) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select check_id\n");  
		sql.append("  from TT_VS_CAR_DISPLACEMENT_CEK tvc\n");  
		sql.append(" where tvc.displacement_id =").append(disId).append("\n");
		sql.append("   and tvc.status =").append(status).append("\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
	
	
	public PageResult<Map<String, Object>> headInfoDealerQuery(Map<String, String> map, int pageSize,int curPage) {
		
		
		String orgId = map.get("orgId") ;
		String disNo = map.get("disNo") ;
		String status = map.get("status") ;
		String type = map.get("type") ;
		String region = map.get("region") ;
		String dealerId = map.get("dealerId") ;
		String base = map.get("base") ;
		String newVin = map.get("newVin") ;
		String oldVin = map.get("oldVin") ;
		String areas = map.get("areas") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select distinct tvu.displacement_no,\n");
		sql.append("       tvu.displacement_id,\n");  
		sql.append("       tvu.vehicle_id,\n");  
		sql.append("       vod.root_org_name,\n");  
		sql.append("       tmr.region_name,\n");  
		sql.append("       vod.dealer_name root_dealer_name,\n");  
		sql.append("       tvu.displacement_type,\n");  
		sql.append("       tvu.operate_status,\n");  
		sql.append("       tmv.vin new_vin,\n");  
		sql.append("       tba.produce_base,\n");  
		sql.append("       tvu.old_vin\n");  
		sql.append("  from tt_vs_used_car_displacement tvu,\n");  
		sql.append("       vw_org_dealer               vod,\n");  
		sql.append("       tm_org_business_area        toba,\n");  
		sql.append("       tm_dealer                   tmd,\n");  
		sql.append("       tm_region                   tmr,\n");  
		sql.append("       tm_vehicle                  tmv,\n");  
		sql.append("       tm_business_area            tba\n");  
		sql.append(" where tvu.dealer_id = vod.dealer_id\n");  
		sql.append("   and vod.dealer_id = tmd.dealer_id\n");  
		sql.append("   and vod.root_org_id = toba.org_id\n");  
		sql.append("   and tmd.province_id = tmr.region_code(+)\n");  
		sql.append("   and tvu.vehicle_id = tmv.vehicle_id\n");  
		sql.append("   and tmv.area_id = tba.area_id\n");
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   and vod.root_org_id = ").append(orgId).append("\n");
		}
		
		if(!CommonUtils.isNullString(disNo)) {
			sql.append("   and tvu.displacement_no like '%").append(disNo).append("%'\n");
		}

		if(!CommonUtils.isNullString(status)) {
			sql.append("   and tvu.operate_status = ").append(status).append("\n");
		}

		if(!CommonUtils.isNullString(type)) {
			sql.append("   and tvu.displacement_type = ").append(type).append("\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("   and tmd.province_id = ").append(region).append("\n");
		}
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("   and tvu.dealer_id  in (").append(dealerId).append(") \n");
		}
		
		if(!CommonUtils.isNullString(base)) {
			sql.append("   and tba.produce_base = ").append(base).append("\n");
		}
		
		if(!CommonUtils.isNullString(newVin)) {
			sql.append(GetVinUtil.getVins(newVin, "tmv"));
		}
		
		if(!CommonUtils.isNullString(oldVin)) {
			sql.append(GetVinUtil.getVinsNew(oldVin, "tvu"));
		}
		
		sql.append("   and toba.area_id in (").append(areas).append(")\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage) ;
	}
	
	
	public String checkLogInsert(Map<String, String> map) {
		String opinion = map.get("opinion") ;
		String status = map.get("status") ;
		String userId = map.get("userId") ;
		String disId = map.get("disId") ;
		
		TtVsCarDisplacementCekPO chk = new TtVsCarDisplacementCekPO() ;
		String checkId =SequenceManager.getSequence("");
		
		chk.setCheckId(Long.parseLong(checkId)) ;
		chk.setDisplacementId(Long.parseLong(disId)) ;
		chk.setOpinion(opinion) ;
		chk.setStatus(Integer.parseInt(status)) ;
		chk.setCreateBy(Long.parseLong(userId)) ;
		chk.setCreateDate(new Date(System.currentTimeMillis())) ;
		chk.setCheckDate(new Date(System.currentTimeMillis())) ;
		
		dao.insert(chk) ;
		return checkId ;
	}
	
	public void statusUpdate(String disId, String status, String userId) {
		TtVsUsedCarDisplacementPO oldTvu = new TtVsUsedCarDisplacementPO() ;
		oldTvu.setDisplacementId(Long.parseLong(disId)) ;
		TtVsUsedCarDisplacementPO newTvu = new TtVsUsedCarDisplacementPO() ;
		newTvu.setOperateStatus(Integer.parseInt(status)) ;
		newTvu.setUpdateDate(new Date(System.currentTimeMillis())) ;
		newTvu.setUpdateBy(Long.parseLong(userId)) ;
		
		dao.update(oldTvu, newTvu) ;
	}
	
	public int checkTypeExt(String disType, String priceType) {
		String count = "0" ;
			
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select count(*) count\n");
		sql.append("  from tt_vs_car_displacement_prc tvc\n");  
		sql.append(" where tvc.displacement_type = ").append(disType).append("\n");  
		sql.append("   and tvc.displacement_prc = ").append(priceType).append("\n");

		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(map)) {
			count = map.get("COUNT").toString() ;
		}
		
		return Integer.parseInt(count) ;
	}
	
	public void DisplacementPrcUpdateCarInfo(String disType, String priceType, String price, String userId) {
		TtVsCarDisplacementPrcPO oldTvc = new TtVsCarDisplacementPrcPO() ;
		oldTvc.setDisplacementType(disType) ;
		oldTvc.setDisplacementPrc(priceType) ;
		TtVsCarDisplacementPrcPO newTvc = new TtVsCarDisplacementPrcPO() ;
		newTvc.setPrice(Double.parseDouble(price)) ;
		newTvc.setUpdateDate(new Date(System.currentTimeMillis())) ;
		newTvc.setUpdateBy(Long.parseLong(userId)) ;
		
		dao.update(oldTvc, newTvc) ;
	}
	
	public PageResult<Map<String, Object>> DisplacementPrcDelaer(Map<String, String> map, int pageSize,int curPage) {
		String disPrc = map.get("disPrc") ;
		String orgId = map.get("orgId") ;
		String dlrId = map.get("dlrId") ;
		String areas = map.get("areas") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select distinct vod.dealer_id,\n");
		sql.append("       vod.dealer_code,\n");  
		sql.append("       vod.dealer_name,\n");  
		sql.append("       vod.root_org_name,\n");  
		sql.append("       '1' ischecked\n");  
		sql.append("  from tt_vs_car_displacement_dlr tvd, vw_org_dealer vod, tm_dealer_business_area tdba\n");  
		sql.append(" where tvd.dealer_id = vod.dealer_id\n");  
		sql.append("   and tvd.dealer_id = tdba.dealer_id\n");  
		sql.append("   and tvd.displacement_prc = ").append(disPrc).append("\n");  
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(" and vod.root_org_id = ").append(orgId).append(" \n");
		}
		
		if(!CommonUtils.isNullString(dlrId)) {
			sql.append("   and vod.dealer_id in (").append(dlrId).append(")\n");  
		}
		
		if(!CommonUtils.isNullString(areas)) {
			sql.append("   and tdba.area_id in (").append(areas).append(")\n");  
		}
		
		sql.append("union all\n");  
		sql.append("select distinct vod.dealer_id,\n");  
		sql.append("       vod.dealer_code,\n");  
		sql.append("       vod.dealer_name,\n");  
		sql.append("       vod.root_org_name,\n");  
		sql.append("       '0' ischecked\n");  
		sql.append("  from vw_org_dealer vod, tm_dealer_business_area tdba\n");  
		sql.append(" where vod.dealer_id = tdba.dealer_id\n");  
		sql.append("   and not exists (select 1\n");  
		sql.append("          from tt_vs_car_displacement_dlr tvd\n");  
		sql.append("         where tvd.dealer_id = vod.dealer_id)\n");
		sql.append(" and vod.dealer_type <> ").append(Constant.DEALER_TYPE_DWR).append(" \n");
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(" and vod.root_org_id = ").append(orgId).append(" \n");
		}
		
		if(!CommonUtils.isNullString(dlrId)) {
			sql.append("   and vod.dealer_id in (").append(dlrId).append(")\n");  
		}
		
		if(!CommonUtils.isNullString(areas)) {
			sql.append("   and tdba.area_id in (").append(areas).append(")\n");  
		}

		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage) ;
	}
	
	public void insertDlrPrc(String dealerId, String disPrc) {
		TtVsCarDisplacementDlrPO tvc = new TtVsCarDisplacementDlrPO() ;
		String headId = SequenceManager.getSequence("");
		
		tvc.setDisplacementDlrId(Long.parseLong(headId)) ;
		tvc.setDealerId(Long.parseLong(dealerId)) ;
		tvc.setDisplacementPrc(Long.parseLong(disPrc)) ;
		
		dao.insert(tvc) ;
	}
	
	public boolean chkDlr(String dealerId) {
		boolean flag = false ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select count(*) count from tt_vs_car_displacement_dlr tvd where tvd.dealer_id = ").append(dealerId).append("\n");

		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(map)) {
			if(!"0".equals(map.get("COUNT").toString())) {
				flag = true ;
			}
		}
		
		return flag ;
	}
	
	public void delDlrPrc(String dealerIds) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("delete tt_vs_car_displacement_dlr tvd where tvd.dealer_id in (").append(dealerIds).append(")\n");

		dao.delete(sql.toString(), null) ;
	}
	
	public void retSalesStatus(String disId) {
		TtVsUsedCarDisplacementPO tvu = new TtVsUsedCarDisplacementPO() ;
		tvu.setDisplacementId(Long.parseLong(disId)) ;
		
		tvu = (TtVsUsedCarDisplacementPO)dao.select(tvu).get(0) ;
		Long salesId = tvu.getSalesOrderId() ;
		
		TtDealerActualSalesPO oldTda = new TtDealerActualSalesPO() ;
		oldTda.setOrderId(salesId) ;
		TtDealerActualSalesPO newTda = new TtDealerActualSalesPO() ;
		newTda.setDisplacementStatus(Constant.STATUS_DISABLE) ;
		
		dao.update(oldTda, newTda) ;
		
		Map<String ,Object> disInfoList = getDisInfo(disId) ;
		
		Long vehicleId = Long.parseLong(disInfoList.get("VEHICLE_ID").toString()) ;
		
		TmVehiclePO oldTvPO = new TmVehiclePO() ;
		oldTvPO.setVehicleId(vehicleId) ;
		TmVehiclePO newTvPO = new TmVehiclePO() ;
		newTvPO.setLockStatus(Constant.LOCK_STATUS_01) ; 
		
		dao.update(oldTvPO, newTvPO) ;
	}
	
	public List<Map<String, Object>> CashTotalQuery(String con) {
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("select region_name,root_dealer_name,dealer_name,count(displacement_id) amount ,price,sum(price_amount) price_amount from (\n" );
		sql.append("   select distinct tvu.displacement_no,\n" );
		sql.append("       tvu.displacement_id,\n" );
		sql.append("       tvu.vehicle_id,\n" );
		sql.append("       vod.root_org_name ,\n" );
		sql.append("       tmr.region_name,\n" );
		sql.append("       vod.dealer_name root_dealer_name,\n" );
		sql.append("       vod.dealer_name,\n" );
		sql.append("       tvu.displacement_type,\n" );
		sql.append("       tvu.operate_status,\n" );
		sql.append("       tmv.vin new_vin,\n" );
		sql.append("       tba.produce_base,\n" );
		sql.append("       tvu.old_vin,\n" );
		sql.append("       tvu.price_amount,\n" );
		sql.append("       cdp.price,\n" );
		sql.append("       cdd.displacement_prc\n" );
		sql.append("  from tt_vs_used_car_displacement tvu,\n" );
		sql.append("       tt_vs_car_displacement_dlr cdd,\n" );
		sql.append("       tt_vs_car_displacement_prc cdp,\n" );
		sql.append("       vw_org_dealer               vod,\n" );
		sql.append("       tm_dealer_business_area     tdba,\n" );
		sql.append("       tm_dealer                   tmd,\n" );
		sql.append("       tm_region                   tmr,\n" );
		sql.append("       tm_vehicle                  tmv,\n" );
		sql.append("       tm_business_area            tba\n" );
		sql.append(" where tvu.dealer_id = vod.dealer_id\n" );
		sql.append("   and tvu.dealer_id = cdd.dealer_id\n" );
		sql.append("   and cdd.displacement_prc = cdp.displacement_prc\n" );
		sql.append("   and vod.dealer_id = tmd.dealer_id\n" );
		sql.append("   and tmd.dealer_id = tdba.dealer_id\n" );
		sql.append("   and tvu.displacement_type = cdp.displacement_type\n" );
		sql.append("   and tmd.province_id = tmr.region_code(+)\n" );
		sql.append("   and tvu.vehicle_id = tmv.vehicle_id\n" );
		sql.append("   and tmv.area_id = tba.area_id\n" );
		sql.append("   and tvu.price_amount is not null\n" );
		/*sql.append("   and tvu.operate_status = 80201006");*/
        sql.append(con);
        sql.append(" ) z");
        sql.append(" group by z.region_name,z.root_dealer_name,z.dealer_name,z.price");
		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}

	public List<Map<String, Object>> DetailTotalQuery(String con) {
		StringBuffer sql= new StringBuffer();
		sql.append("select rownum,\n");
		sql.append("       temp.root_org_code,\n");  
		sql.append("       temp.root_org_name,\n");  
		sql.append("       temp.region_name,\n");  
		sql.append("       temp.root_dealer_name,\n");  
		sql.append("       temp.CTM_NAME,\n");  
		sql.append("       decode(temp.CARD_NUM, null, ' ', temp.CARD_NUM) CARD_NUM,\n");  
		sql.append("       temp.license_no,\n");  
		sql.append("       temp.old_vin,\n");  
		sql.append("       temp.scrap_date,\n");  
		sql.append("       temp.scrap_certify_no,\n");  
		sql.append("       temp.purchased_date,\n");  
		sql.append("       temp.produce_base,\n");  
		sql.append("       temp.group_name,\n");  
		sql.append("       temp.new_vin,\n");  
		sql.append("       temp.price\n");  
		sql.append("  from (\n");

		sql.append(" select distinct vod.root_org_code,\n" );
		sql.append("      vod.root_org_name,\n" );
		sql.append("      tmr.region_name,\n" );
		sql.append("      vod.dealer_name root_dealer_name,\n" );
		sql.append("      C.CTM_NAME,\n" );
		sql.append("      C.CARD_NUM,\n" );
		sql.append("      nvl(tmv2.license_no,'  ') license_no,\n" );
		sql.append("      tvu.old_vin,\n" );
		sql.append("      nvl(TO_CHAR(tvu.scrap_date,'YYYY-MM-DD'),' ') scrap_date,\n" );
		sql.append("      nvl(tvu.scrap_certify_no,' ') scrap_certify_no,\n" );
		sql.append("      TO_CHAR(tmv.purchased_date,'YYYY-MM-DD') purchased_date,\n" );
		sql.append("      decode(tba.produce_base,11311001,'重庆',11311002,'河北',11311003,'南京',11311004,'昌河') produce_base,\n" );
		sql.append("      tvmg.group_name,\n" );
		sql.append("      tmv.vin new_vin,\n" );
		sql.append("      cdp.price\n" );
		sql.append(" from tt_vs_used_car_displacement tvu,\n" );
		sql.append("      tt_vs_car_displacement_dlr cdd,\n" );
		sql.append("      tt_vs_car_displacement_prc cdp,\n" );
		sql.append("      TT_DEALER_ACTUAL_SALES      DAS,\n" );
		sql.append("      TT_CUSTOMER                 C,\n" );
		sql.append("      vw_org_dealer               vod,\n" );
		sql.append("      tm_dealer_business_area     tdba,\n" );
		sql.append("      tm_dealer                   tmd,\n" );
		sql.append("      tm_region                   tmr,\n" );
		sql.append("      tm_vhcl_material_group      tvmg,\n" );
		sql.append("      tm_vehicle                  tmv,\n" );
		sql.append("      tm_vehicle                  tmv2,\n" );
		sql.append("      tm_business_area            tba\n" );
		sql.append("where tvu.dealer_id = vod.dealer_id\n" );
		sql.append("  and tvu.dealer_id = cdd.dealer_id\n" );
		sql.append("  and cdd.displacement_prc = cdp.displacement_prc\n" );
		sql.append("  and vod.dealer_id = tmd.dealer_id\n" );
		sql.append("  and tmd.dealer_id = tdba.dealer_id\n" );
		sql.append("  and tvu.displacement_type = cdp.displacement_type\n" );
		sql.append("  and tmd.province_id = tmr.region_code(+)\n" );
		sql.append("  and tvu.vehicle_id = tmv.vehicle_id\n" );
		sql.append("  and tvu.old_vin = tmv2.vin(+)\n" );
		sql.append("  and tmv.model_id = tvmg.group_id\n" );
		sql.append("  and tvu.sales_order_id = das.order_id\n" );
		sql.append("  and das.ctm_id = c.ctm_id\n" );
		sql.append("  and tmv.area_id = tba.area_id\n" );
		/*sql.append("  and tvu.operate_status = 80201006");*/
        sql.append(con);
        sql.append(") temp\n");
        sql.append("    order by rownum asc ");
		return dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
	}
}
