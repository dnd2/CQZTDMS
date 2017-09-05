package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName : CityMileageDao
 * @Description : 城市里程数维护DAO
 * @author : ranjian CreateDate : 2013-4-7
 */
public class CityMileageDao extends BaseDao<PO> {
	private static final CityMileageDao dao = new CityMileageDao();

	public static final CityMileageDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 城市里程数信息查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getCityMileageQuery(
			Map<String, Object> map, int curPage, int pageSize)
			throws Exception {

		String countyId = (String) map.get("countyId");// 区县
		String cityId = (String) map.get("CITY_ID");// 地市
		String provinceId = (String) map.get("PROVINCE_ID");// 省份
		String yieldly = (String) map.get("YIELDLY"); // 产地
		String poseId = (String) map.get("poseId");
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql= new StringBuffer();
		sql.append("SELECT K.DIS_ID,\n" );
		sql.append("       K.AREA_ID,\n" );
		sql.append("       tc.code_desc,\n" );
		sql.append("       K.YIELDLY,\n" );
		sql.append("       K.PROVINCE_ID,\n" );
		sql.append("       K.PROVINCE_NAME,\n" );
		sql.append("       K.CITY_ID,\n" );
		sql.append("       K.CITY_NAME,\n" );
		sql.append("       K.REGION_CODE,\n" );
		sql.append("       K.COUNTY_ID,\n" );
		sql.append("       K.COUNTY_CODE,\n" );
		sql.append("       K.COUNTY_NAME,\n" );
		sql.append("       K.DISTANCE,\n" );
		sql.append("       K.ARRIVE_DAYS,\n" );
		sql.append("       K.CAR_TIE_ID,\n" );
		sql.append("       nvl(t.GROUP_NAME, ' ') GROUP_NAME,\n" );
		sql.append("       K.START_PLACE,\n" );
		sql.append("       K.PROVINCE_NAME || '-' || K.CITY_NAME || '-' || K.COUNTY_NAME END_PLACE,\n" );
		sql.append("       K.TRANS_WAY,\n" );
		sql.append("       K.SINGLE_PLACE,\n" );
		sql.append("       K.FUEL_BEGIN_DATE,\n" );
		sql.append("       K.FUEL_END_DATE,\n" );
		sql.append("       K.HAND_PRICE,K.REMARK\n" );
		sql.append("  FROM (SELECT TSCD.DIS_ID,\n" );
		sql.append("               TMP.AREA_ID,\n" );
		sql.append("               TMP.AREA_NAME YIELDLY,\n" );
		sql.append("               TMP.PROVINCE_ID,\n" );
		sql.append("               TMP.PROVINCE_NAME,\n" );
		sql.append("               TMP.CITY_ID,\n" );
		sql.append("               TMP.CITY_NAME,\n" );
		sql.append("               TMP.CITY_CODE,\n" );
		sql.append("               TMP.REGION_CODE,\n" );
		sql.append("               TMP.COUNTY_ID,\n" );
		sql.append("               TMP.COUNTY_CODE,\n" );
		sql.append("               TMP.COUNTY_NAME,\n" );
		sql.append("               TSCD.DISTANCE,\n" );
		sql.append("               TSCD.ARRIVE_DAYS,\n" );
		sql.append("               TSCD.CAR_TIE_ID,\n" );
		sql.append("               TSCD.START_PLACE,\n" );
		sql.append("               TSCD.TRANS_WAY,\n" );
		sql.append("               TSCD.SINGLE_PLACE,\n" );
		sql.append("               TO_CHAR(TSCD.FUEL_BEGIN_DATE, 'YYYY-MM-DD') FUEL_BEGIN_DATE,\n" );
		sql.append("               TO_CHAR(TSCD.FUEL_END_DATE, 'YYYY-MM-DD') FUEL_END_DATE,\n" );
		sql.append("               TSCD.HAND_PRICE,TSCD.REMARK\n" );
		sql.append("          FROM (SELECT TW.WAREHOUSE_ID AREA_ID,\n" );
		sql.append("                       TW.WAREHOUSE_NAME AREA_NAME,\n" );
		sql.append("                       TR1.REGION_CODE PROVINCE_ID,\n" );
		sql.append("                       TR1.REGION_NAME PROVINCE_NAME,\n" );
		sql.append("                       TR1.REGION_CODE REGION_CODE,\n" );
		sql.append("                       TR2.REGION_CODE CITY_ID,\n" );
		sql.append("                       TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("                       TR2.REGION_NAME CITY_NAME,\n" );
		sql.append("                       TR3.REGION_CODE COUNTY_ID,\n" );
		sql.append("                       TR3.REGION_NAME COUNTY_NAME,\n" );
		sql.append("                       TR3.REGION_CODE COUNTY_CODE\n" );
		sql.append("                  FROM TM_REGION        TR1,\n" );
		sql.append("                       TM_REGION        TR2,\n" );
		sql.append("                       TM_REGION        TR3,\n" );
		sql.append("                       TM_WAREHOUSE     TW\n" );
		sql.append("                 WHERE TR2.PARENT_ID = TR1.REGION_ID\n" );
		sql.append("                   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("                   AND TR1.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n" );
		sql.append("                   AND TW.STATUS="+Constant.STATUS_ENABLE+"\n" );
		sql.append("                   AND TW.WAREHOUSE_TYPE="+Constant.SALES_WAREHOUSE_TYPE_IN+") TMP,\n" );
		sql.append("               TT_SALES_CITY_DIS TSCD\n" );
		sql.append("         WHERE TMP.AREA_ID = TSCD.YIELDLY(+)\n" );
		sql.append("           AND TMP.PROVINCE_ID = TSCD.PROVINCE_ID(+)\n" );
		sql.append("           AND TMP.COUNTY_ID = TSCD.CITY_ID(+)\n" );
		sql.append("           ) K\n" );
		sql.append("  left join tm_vhcl_material_group t on K.CAR_TIE_ID = t.GROUP_ID\n" );
		sql.append("  left join tc_code tc on K.TRANS_WAY = tc.code_id\n" );
		sql.append(" where 1 = 1");
		if (yieldly != null && !yieldly.equals("")) {
			sql.append("AND K.AREA_ID = " + yieldly + "\n");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sql.append(" AND K.REGION_CODE =").append(provinceId)
					.append("\n");
		}
		if (cityId != null && !"".equals(cityId)) {
			sql.append(" AND K.CITY_CODE=").append(cityId).append("\n");
		}
		if (countyId != null && !"".equals(countyId)) {
			sql.append(" AND (K.COUNTY_CODE = ").append(countyId)
					.append(")\n");
		}
		sql.append("\n");
		sql.append("ORDER BY K.PROVINCE_ID, K.COUNTY_ID");
		PageResult<Map<String, Object>> ps = null;
		if (yieldly != null) {
			ps = dao.pageQuery(sql.toString(), params, getFunName(),
					pageSize, curPage);
		}
		return ps;
	}

	/**
	 * 根据id查询
	 * 
	 * @param disId
	 * @param poseId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCityMileageById(String disId,
			String poseId) throws Exception {

		List<Object> params = new LinkedList<Object>();

		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT K.DIS_ID,\n");
		sbSql.append("       K.AREA_ID,\n");
		sbSql.append("       K.YIELDLY,\n");
		sbSql.append("       K.PROVINCE_ID,\n");
		sbSql.append("       K.REGION_CODE,\n");
		sbSql.append("       K.PROVINCE_NAME,\n");
		sbSql.append("       K.CITY_ID,\n");
		sbSql.append("       K.CITY_CODE,\n");
		sbSql.append("       K.CITY_NAME,\n");
		sbSql.append("       K.COUNTY_ID,\n");
		sbSql.append("       K.COUNTY_CODE,\n");
		sbSql.append("       K.COUNTY_NAME,\n");
		sbSql.append("       K.DISTANCE,\n");
		sbSql.append("       K.ARRIVE_DAYS,\n");
		sbSql.append("       K.CAR_TIE_ID,\n");
		sbSql.append("       nvl(t.GROUP_NAME,' ') GROUP_NAME,\n");
		sbSql.append("       K.START_PLACE,\n");
		sbSql.append("       K.TRANS_WAY,\n");
		sbSql.append("       K.SINGLE_PLACE,\n");
		sbSql.append("       K.FUEL_BEGIN_DATE,\n");
		sbSql.append("       K.FUEL_END_DATE,\n");
		sbSql.append("       K.FUEL_COEFFICIENT,K.HAND_PRICE,K.REMARK \n");
		sbSql.append("  FROM (");

		sbSql.append("SELECT TSCD.DIS_ID,\n");
		sbSql.append("       TMP.AREA_ID,\n");
		sbSql.append("       TMP.AREA_NAME YIELDLY,\n");
		sbSql.append("       TMP.PROVINCE_ID,\n");
		sbSql.append("       TMP.PROVINCE_NAME,\n");
		sbSql.append("       TMP.CITY_ID,\n");
		sbSql.append("       TMP.CITY_NAME,\n");
		sbSql.append("       TMP.CITY_CODE,\n");
		sbSql.append("       TMP.REGION_CODE,\n");
		sbSql.append("       TMP.COUNTY_ID,\n");
		sbSql.append("       TMP.COUNTY_CODE,\n");
		sbSql.append("       TMP.COUNTY_NAME,\n");
		sbSql.append("       TSCD.DISTANCE,\n");
		sbSql.append("       TSCD.ARRIVE_DAYS,\n");
		sbSql.append("       TSCD.CAR_TIE_ID,\n");
		sbSql.append("       TSCD.START_PLACE,\n");
		sbSql.append("       TSCD.TRANS_WAY,\n");
		sbSql.append("       TSCD.SINGLE_PLACE,\n");
		sbSql.append("       TO_CHAR(TSCD.FUEL_BEGIN_DATE,'YYYY-MM-DD') FUEL_BEGIN_DATE,\n");
		sbSql.append("       TO_CHAR(TSCD.FUEL_END_DATE,'YYYY-MM-DD') FUEL_END_DATE,\n");
		sbSql.append("       TSCD.FUEL_COEFFICIENT,TSCD.HAND_PRICE,TSCD.REMARK\n");
		sbSql.append("FROM (SELECT TW.WAREHOUSE_ID AREA_ID,\n" );
		sbSql.append("                       TW.WAREHOUSE_NAME AREA_NAME,\n" );
		sbSql.append("                       TR1.REGION_CODE PROVINCE_ID,\n" );
		sbSql.append("                       TR1.REGION_NAME PROVINCE_NAME,\n" );
		sbSql.append("                       TR1.REGION_CODE REGION_CODE,\n" );
		sbSql.append("                       TR2.REGION_CODE CITY_ID,\n" );
		sbSql.append("                       TR2.REGION_CODE CITY_CODE,\n" );
		sbSql.append("                       TR2.REGION_NAME CITY_NAME,\n" );
		sbSql.append("                       TR3.REGION_CODE COUNTY_ID,\n" );
		sbSql.append("                       TR3.REGION_NAME COUNTY_NAME,\n" );
		sbSql.append("                       TR3.REGION_CODE COUNTY_CODE\n" );
		sbSql.append("                  FROM TM_REGION        TR1,\n" );
		sbSql.append("                       TM_REGION        TR2,\n" );
		sbSql.append("                       TM_REGION        TR3,\n" );
		sbSql.append("                       TM_WAREHOUSE     TW\n" );
		sbSql.append("                 WHERE TR2.PARENT_ID = TR1.REGION_ID\n" );
		sbSql.append("                   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sbSql.append("                   AND TR1.REGION_TYPE = "+Constant.REGION_TYPE_02+"\n" );
		sbSql.append("                   AND TW.STATUS="+Constant.STATUS_ENABLE+"\n" );
		sbSql.append("                   AND TW.WAREHOUSE_TYPE="+Constant.SALES_WAREHOUSE_TYPE_IN+") TMP,\n" );
		sbSql.append("               TT_SALES_CITY_DIS TSCD\n" );
		sbSql.append("         WHERE TMP.AREA_ID = TSCD.YIELDLY(+)\n" );
		sbSql.append("           AND TMP.PROVINCE_ID = TSCD.PROVINCE_ID(+)\n" );
		sbSql.append("           AND TMP.COUNTY_ID = TSCD.CITY_ID(+)\n" );
		sbSql.append("           ) K\n" );
		sbSql.append("  left join tm_vhcl_material_group t on K.CAR_TIE_ID = t.GROUP_ID\n" );
		sbSql.append("  left join tc_code tc on K.TRANS_WAY = tc.code_id\n" );
		sbSql.append(" where 1 = 1");
		if (disId != null && !disId.equals("")) {
			sbSql.append("AND  K.DIS_ID = ? \n");
			params.add(disId);
		}

		sbSql.append("\n");
		sbSql.append("ORDER BY K.PROVINCE_ID, K.COUNTY_ID");
		return pageQuery(sbSql.toString(), params, getFunName());
	}

	public int updateInfo(TtSalesCityDisPO oldPo, TtSalesCityDisPO tt) {
		return this.update(oldPo, tt);
	}

	public void insertInfo(TtSalesCityDisPO tt) {
		this.insert(tt);
	}

	public void insertTmBusinessArea(TmBusinessAreaPO tt) {
		this.insert(tt);
	}

	/**
	 * 城市里程数信息查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCityMileageForOrderConvert(
			String yieldly, String countyCode) throws Exception {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT K.DIS_ID,\n");
		sbSql.append("       K.AREA_ID,\n");
		sbSql.append("       tc.code_desc,\n");
		sbSql.append("       K.YIELDLY,\n");
		sbSql.append("       K.PROVINCE_ID,\n");
		sbSql.append("       K.PROVINCE_NAME,\n");
		sbSql.append("       K.CITY_ID,\n");
		sbSql.append("       K.CITY_NAME,\n");
		sbSql.append("       K.REGION_CODE,\n");
		sbSql.append("       K.COUNTY_ID,\n");
		sbSql.append("       K.COUNTY_CODE,\n");
		sbSql.append("       K.COUNTY_NAME,\n");
		sbSql.append("       K.DISTANCE,\n");
		sbSql.append("       K.ARRIVE_DAYS,\n");
		sbSql.append("       K.CAR_TIE_ID,\n");
		sbSql.append("       nvl(t.GROUP_NAME,' ') GROUP_NAME,\n");
		sbSql.append("       K.START_PLACE,\n");
		sbSql.append("       K.PROVINCE_NAME || '-' || K.CITY_NAME || '-' || K.COUNTY_NAME  END_PLACE,\n");
		sbSql.append("       K.TRANS_WAY,\n");
		sbSql.append("       K.SINGLE_PLACE,\n");
		sbSql.append("       K.FUEL_COEFFICIENT \n");
		sbSql.append("  FROM (");

		sbSql.append("SELECT TSCD.DIS_ID,\n");
		sbSql.append("       TMP.AREA_ID,\n");
		sbSql.append("       TMP.AREA_NAME YIELDLY,\n");
		sbSql.append("       TMP.PROVINCE_ID,\n");
		sbSql.append("       TMP.PROVINCE_NAME,\n");
		sbSql.append("       TMP.CITY_ID,\n");
		sbSql.append("       TMP.CITY_NAME,\n");
		sbSql.append("       TMP.CITY_CODE,\n");
		sbSql.append("       TMP.REGION_CODE,\n");
		sbSql.append("       TMP.COUNTY_ID,\n");
		sbSql.append("       TMP.COUNTY_CODE,\n");
		sbSql.append("       TMP.COUNTY_NAME,\n");
		sbSql.append("       TSCD.DISTANCE,\n");
		sbSql.append("       TSCD.ARRIVE_DAYS,\n");
		sbSql.append("       TSCD.CAR_TIE_ID,\n");
		sbSql.append("       TSCD.START_PLACE,\n");
		sbSql.append("       TSCD.TRANS_WAY,\n");
		sbSql.append("       TSCD.SINGLE_PLACE,\n");
		sbSql.append("       TSCD.FUEL_COEFFICIENT\n");
		sbSql.append("  FROM (SELECT TBA.AREA_ID,\n");
		sbSql.append("               TBA.AREA_NAME,\n");
		sbSql.append("               TR1.REGION_ID   PROVINCE_ID,\n");
		sbSql.append("               TR1.REGION_NAME PROVINCE_NAME,\n");
		sbSql.append("               TR1.REGION_CODE REGION_CODE,\n");
		sbSql.append("               TR2.REGION_ID   CITY_ID,\n");
		sbSql.append("               TR2.REGION_CODE CITY_CODE,\n");
		sbSql.append("               TR2.REGION_NAME CITY_NAME,\n");
		sbSql.append("               TR3.REGION_ID   COUNTY_ID,\n");
		sbSql.append("               TR3.REGION_NAME COUNTY_NAME,\n");
		sbSql.append("               TR3.REGION_CODE COUNTY_CODE\n");
		sbSql.append("          FROM TM_REGION        TR1,\n");
		sbSql.append("               TM_REGION        TR2,\n");
		sbSql.append("               TM_REGION        TR3,\n");
		sbSql.append("               TM_BUSINESS_AREA TBA \n");
		sbSql.append("         WHERE TR2.PARENT_ID = TR1.REGION_ID\n");
		sbSql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n");
		sbSql.append("           AND TR1.REGION_TYPE =").append(Constant.REGION_TYPE_02).append("\n");
		sbSql.append("           AND TBA.STATUS = ").append(Constant.STATUS_ENABLE).append(") TMP,\n");
		sbSql.append("       TT_SALES_CITY_DIS TSCD \n");
		sbSql.append(" WHERE TMP.AREA_ID = TSCD.YIELDLY(+)\n");
		sbSql.append("   AND TMP.PROVINCE_ID = TSCD.PROVINCE_ID(+)\n");
		sbSql.append("   AND TMP.COUNTY_ID = TSCD.CITY_ID(+)\n");
		sbSql.append(") K left join tm_vhcl_material_group t on K.CAR_TIE_ID = t.GROUP_ID left join tc_code tc on K.TRANS_WAY = tc.code_id where 1=1\n");
		if (yieldly != null && !yieldly.equals("")) {
			sbSql.append(" AND K.AREA_ID = ?\n");
			params.add(yieldly);
		}
		if (!XHBUtil.IsNull(countyCode)) {
			sbSql.append(" AND K.COUNTY_CODE = ?");
			params.add(countyCode);
		}
		sbSql.append("\n");
		sbSql.append("ORDER BY K.PROVINCE_ID, K.COUNTY_ID");
		return pageQuery(sbSql.toString(), params, getFunName());
	}

	public Map<String, Object> queryByReginCode(String regionCode) {
		StringBuffer sql = new StringBuffer("select * from TM_REGION where 1=1");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(regionCode)) {
			params.add(regionCode);
			sql.append(" and REGION_CODE = ?");
		}
		Map<String, Object> maps = this.pageQueryMap(sql.toString(), params,
				this.getFunName());
		return maps;
	}

	// 获取产地方法
	public List<Map<String, Object>> getBussienssArea(String areaName) {
		String sql = "Select AREA_ID,AREA_Name from tm_business_area area where area.area_name=?";
		List<Object> params = new ArrayList<Object>();
		params.add(areaName);
		return dao.pageQuery(sql, params, getFunName());
	}
	// 获取产地方法
	public List<Map<String, Object>> getTmWarehouse(String areaName) {
		StringBuffer sql= new StringBuffer();
		sql.append("select tw.warehouse_id AREA_ID,tw.warehouse_name AREA_NAME\n" );
		sql.append("  from tm_warehouse tw\n" );
		sql.append(" where tw.warehouse_name = ?\n" );
		sql.append("   and tw.warehouse_type = 14011001\n" );
		sql.append("   and tw.status = 10011001");
		List<Object> params = new ArrayList<Object>();
		params.add(areaName);
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/**
	 * 校验出发仓库,返回错误行
	 */
	public List<Map<String, Object>> checkStartWarehouse() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT t.row_number\n" );
		sql.append("  FROM tmp_tt_city_dis t\n" );
		sql.append("  LEFT JOIN tm_warehouse w ON t.from_place = w.warehouse_name\n" );
		sql.append(" WHERE w.warehouse_name IS NULL order by 1");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 获取出发仓库列表
	 */
	public List<Map<String, Object>> getStartWarehouse() {
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct tw.warehouse_id AREA_ID,tw.warehouse_name AREA_NAME\n" );
		sql.append("  from tmp_tt_city_dis t\n" );
		sql.append(" inner join tm_warehouse tw on t.from_place = tw.warehouse_name");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 获取目的省份列表
	 */
	public List<Map<String, Object>> getDesProvince() {
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct tr.region_code PROVINCE_ID, tr.region_name PROVINCE_NAME\n" );
		sql.append("  from tmp_tt_city_dis t\n" );
		sql.append(" inner join tm_region tr on t.des_provice = tr.region_name\n" );
		sql.append("                        and tr.region_type = 10541002");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 获取目的城市列表
	 */
	public List<Map<String, Object>> getDesCity() {
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct trp.region_name PROVINCE_NAME,\n" );
		sql.append("\t\t                tr.region_code    CITY_ID,\n" );
		sql.append("\t\t                tr.region_name  CITY_NAME\n" );
		sql.append("\t\t  from tmp_tt_city_dis t\n" );
		sql.append("\t\t inner join tm_region tr on t.des_city = tr.region_name\n" );
		sql.append("\t\t                        and tr.region_type = 10541003\n" );
		sql.append("     inner join tm_region trp on t.des_provice=trp.region_name\n" );
		sql.append("                          and tr.parent_id = trp.region_id");


		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 获取目的区县列表
	 * @return
	 */
	public List<Map<String, Object>> getDesCounty() {
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct trpp.region_name PROVINCE_NAME,\n" );
		sql.append("                trp.region_name  CITY_NAME,\n" );
		sql.append("                tr.region_code     COUNTY_ID,\n" );
		sql.append("                tr.region_name   COUNTY_NAME\n" );
		sql.append("  from tmp_tt_city_dis t\n" );
		sql.append(" inner join tm_region tr on t.des_county = tr.region_name\n" );
		sql.append("                        and tr.region_type = 10541004\n" );
		sql.append(" inner join tm_region trp on t.des_city = trp.region_name\n" );
		sql.append("                         and tr.parent_id = trp.region_id\n" );
		sql.append(" inner join tm_region trpp on t.des_provice = trpp.region_name\n" );
		sql.append("                          and trp.parent_id = trpp.region_id");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 校验目的地,返回错误行
	 */
	public List<Map<String, Object>> checkEndPlace() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT t.row_number\n" );
		sql.append("  FROM tmp_tt_city_dis t\n" );
		sql.append("  left join (select rg1.region_code   province_id,\n" );
		sql.append("                    rg1.region_name province_name,\n" );
		sql.append("                    rg1.region_code   city_id,\n" );
		sql.append("                    rg2.region_name city_name,\n" );
		sql.append("                    rg3.region_code   county_id,\n" );
		sql.append("                    rg3.region_name county_name\n" );
		sql.append("               from tm_region rg1, tm_region rg2, tm_region rg3\n" );
		sql.append("              where rg1.region_id = rg2.parent_id\n" );
		sql.append("                and rg2.region_id = rg3.parent_id\n" );
		//艾春添加国标城市里程表中的状态为可用的标识
		sql.append("				and rg1.region_type = 10541002\n");
		sql.append("				and rg2.region_type = 10541003\n");
		sql.append("				and rg3.region_type = 10541004"); 
		sql.append("				and rg1.status = 10011001\n");
		sql.append("				and rg2.status = 10011001\n");
		sql.append("				and rg3.status = 10011001"); 
		sql.append("             ) t2 on t.des_provice = t2.province_name\n" );
		sql.append("                 and t.des_city = t2.city_name\n" );
		sql.append("                 and t.des_county = t2.county_name\n" );
		sql.append(" where t2.province_id is null order by 1");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 校验发运方式
	 * @return
	 */
	public List<Map<String, Object>> checkTransWay() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT t.row_number\n" );
		sql.append("  FROM tmp_tt_city_dis t\n" );
		sql.append("  left join tc_code tc on T.TRANS_WAY = TC.CODE_DESC\n" );
		sql.append("                      AND tc.code_id in\n" );
		sql.append("                          (95591001, 95591002, 95591003, 95591004, 95591005, 95591006)\n" );
		sql.append(" WHERE TC.CODE_ID IS NULL order by 1");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 根据地区名称和等级获取地区信息
	 * 
	 * @param regionCode
	 * @return
	 */
	public Map<String, Object> queryByRegionName(String regionName,
			Integer regionType) {
		StringBuffer sql = new StringBuffer(
				"select * from TM_REGION t where 1=1");
		List<Object> params = new ArrayList<Object>();
		params.add(regionType);
		params.add(regionName);
		sql.append(" and t.region_type = ?\n");
		sql.append(" and t.region_name=?");

		Map<String, Object> maps = this.pageQueryMap(sql.toString(), params,
				this.getFunName());
		return maps;
	}

	/**
	 * 根据职位ID和范围ID查询职位业务范围关系
	 * 
	 * @param poseId
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> getTmPoseBusinessAreaByPidAid(String poseId,
			String areaId) {
		StringBuffer sql = new StringBuffer("select t.*\n"
				+ "  from TM_POSE_BUSINESS_AREA t\n" + " where t.pose_id =?\n"
				+ " and t.area_id=?");
		List<Object> params = new ArrayList<Object>();
		params.add(poseId);
		params.add(areaId);
		Map<String, Object> maps = this.pageQueryMap(sql.toString(), params,
				this.getFunName());
		return maps;
	}

	// 或的目的地方法
	public List<Map<String, Object>> getDestinatation(String proviceName,
			String cityName, String contyName, String checkFlag) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select rg1.region_id province_id ,rg1.region_name province_name,rg1.region_id city_id,rg2.region_name city_name,rg3.region_id county_id ,rg3.region_name county_name from tm_region rg1,tm_region rg2 ,tm_region rg3");
		sql.append(" where rg1.region_id = rg2.parent_id and rg2.region_id = rg3.parent_id");

		if ("province".equals(checkFlag)) {
			sql.append(" and rg1.region_name = ? ");
			params.add(proviceName);
		}

		if ("city".equals(checkFlag)) {
			sql.append(" and rg1.region_name = ? ");
			sql.append(" and rg2.region_name = ? ");
			params.add(proviceName);
			params.add(cityName);
		}

		if ("county".equals(checkFlag)) {
			sql.append(" and rg1.region_name = ? ");
			sql.append(" and rg2.region_name = ? ");
			sql.append(" and rg3.region_name = ? ");
			params.add(proviceName);
			params.add(cityName);
			params.add(contyName);
		}

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	public List<Map<String, Object>> getVehSys(String sysName) {
		StringBuffer sql = new StringBuffer(
				" select mat.SERIES_ID,mat.SERIES_NAME from vw_material_group_mat mat where mat.SERIES_NAME = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(sysName);
		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	public List<Map<String, Object>> getTransWays(String transWay) {
		StringBuffer sql = new StringBuffer(
				"   select tc.code_id,tc.code_desc from tc_code tc where tc.code_desc= ? and tc.code_id in (95591001,95591002,95591003,95591004)");
		List<Object> params = new ArrayList<Object>();
		params.add(transWay);
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/**
	 * 判断导入数据中重复数据
	 * @return
	 */
	public List<Map<String, Object>> checkRepeatRow() {
		StringBuffer sql= new StringBuffer();
		sql.append("select count(tt.row_number) as reNum,\n" );
		sql.append("       tt.from_place,\n" );
		sql.append("       tt.des_provice,\n" );
		sql.append("       tt.des_city,\n" );
		sql.append("       tt.des_county\n" );
		sql.append("  from tmp_tt_city_dis tt\n" );
		sql.append(" having  count(tt.row_number)>1\n" );
		sql.append(" group by tt.from_place, tt.des_provice, tt.des_city, tt.des_county");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	public PageResult<Map<String, Object>> importQuery(String userId,
			int pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		//艾春 修改查询临时表顺便关联实际城市里程表中的ID
		sql.append("select dis.row_number,\n" );
		sql.append("    dis.price,\n" );
		sql.append("    dis.des_provice,\n" );
		sql.append("    dis.des_city,\n" );
		sql.append("    dis.des_county,\n" );
		sql.append("    dis.distence,\n" );
		sql.append("    dis.veh_sys,\n" );
		sql.append("    dis.trans_way,\n" );
		sql.append("    dis.from_place,\n" );
		sql.append("    dis.reach_day,\n" );
		sql.append("    dis.fuel_coefficeient,\n" );
		sql.append("    dis.fuel_begin_date,\n" );
		sql.append("    dis.fuel_end_date,dis.hand_price,dis.remark,\n" );
		sql.append("    scd.dis_id\n" );
		sql.append(" from tmp_tt_city_dis dis\n" );
		sql.append(" join tm_warehouse wh on dis.from_place = wh.warehouse_name\n" );
		sql.append(" join (select p.region_name prov_name,\n" );
		sql.append("        p.region_code   prov_id,\n" );
		sql.append("        c.region_name city_name,\n" );
		sql.append("        t.region_name county_name,\n" );
		sql.append("        t.region_code   county_id\n" );
		sql.append("     from tm_region p, tm_region c, tm_region t\n" );
		sql.append("     where p.region_id = c.parent_id\n" );
		sql.append("      and c.region_id = t.parent_id\n" );
		sql.append("      and p.region_type = 10541002\n" );
		sql.append("      and c.region_type = 10541003\n" );
		sql.append("      and t.region_type = 10541004\n" );
		sql.append("      and p.status = 10011001\n" );
		sql.append("      and c.status = 10011001\n" );
		sql.append("      and t.status = 10011001) re on dis.des_provice = re.prov_name\n" );
		sql.append("                   and dis.des_city = re.city_name\n" );
		sql.append("                   and dis.des_county = re.county_name\n" );
		sql.append(" left join tt_sales_city_dis scd on wh.warehouse_id = scd.YIELDLY\n" );
		sql.append("                 and re.prov_id = scd.province_id\n" );
		sql.append("                 and re.county_id = scd.city_id");		
		sql.append("  where dis.user_id = ? \n");
		sql.append("  ORDER BY to_number(row_number) ASC\n");

		List<Object> params = new ArrayList<Object>();
		params.add(userId);
		return dao.pageQuery(sql.toString(), params, getFunName(), pageSize,
				curPage);
	}

	public List<Map<String, Object>> getTtSalesCityDisPO(TtSalesCityDisPO po) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT dis.dis_id from tt_sales_city_dis dis\n");
		sql.append(" WHERE dis.YIELDLY = ?\n");
		sql.append("   AND dis.CITY_ID = ? \n");
		sql.append("   AND car_tie_id = ?\n");
		List<Object> params = new ArrayList<Object>();
		params.add(po.getYieldly());
		params.add(po.getCityId());
		params.add(po.getCarTieId());
		return pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 修改城市里程数验证
	 * 
	 * @param provinceId
	 * @param cityId
	 * @param carTieId
	 * @param oldDisId
	 * @return
	 */
	public List<Map<String, Object>> validUpdateCityDis(String yieldly,
			String cityId, String disId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 from tt_sales_city_dis dis\n");
		sql.append(" WHERE dis.YIELDLY = ?\n");
		sql.append("   AND dis.CITY_ID = ? \n");
		//sql.append("   AND car_tie_id = ?\n");
		sql.append("   AND dis.dis_id != ?\n");
		List<Object> params = new ArrayList<Object>();
		params.add(yieldly);
		params.add(cityId);
		//params.add(carTieId);
		params.add(disId);
		return pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 获得发运方式code值
	 * @return
	 */
	public List<Map<String, Object>> getTransWays() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT t.code_id, t.code_desc FROM tc_code t WHERE t.TYPE = 9559\n");
		return pageQuery(sql.toString(), null, getFunName());
	}
}
