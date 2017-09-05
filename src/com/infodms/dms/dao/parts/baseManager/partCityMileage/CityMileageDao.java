package com.infodms.dms.dao.parts.baseManager.partCityMileage;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : ranjian
 *         CreateDate     : 2013-4-7
 * @ClassName : CityMileageDao
 * @Description : 城市里程数维护DAO
 */
public class CityMileageDao extends BaseDao<PO> {
    private static final CityMileageDao dao = new CityMileageDao();

    private CityMileageDao() {
    }

    public static final CityMileageDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isRepeat(String logiId, String endPlaceId) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT CD.*\n");
        sql.append("  FROM TT_PART_CITY_DIS CD\n");
        sql.append(" WHERE CD.LOGI_ID = '" + logiId + "'\n");
//		sql.append("   AND CD.START_PLACE_ID = '"+startPlaceId+"'\n");
        sql.append("   AND CD.END_PLACE_ID = '" + endPlaceId + "'");

        Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, this.getFunName());
        if (map == null) {
            return false;
        } else {
            return true;
        }
    }

    public Map<String, Object> getplaceInfo(String region_name) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT TR2.REGION_ID   PROVINCE_ID,\n");
        sql.append("       TR2.REGION_NAME PROVINCE_NAME,\n");
        sql.append("       TR1.REGION_ID   CITY_ID,\n");
        sql.append("       TR1.REGION_NAME CITY_NAME\n");
        sql.append("  FROM TM_REGION TR1, TM_REGION TR2\n");
        sql.append(" WHERE TR1.PARENT_ID = TR2.REGION_ID\n");
        sql.append("   AND TR1.REGION_NAME LIKE '%" + region_name + "%'");

        Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, this.getFunName());
        return map;
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
    public PageResult<Map<String, Object>> getCityMileageQuery(RequestWrapper request, int curPage, int pageSize) throws Exception {

        String countyId = CommonUtils.checkNull(request.getParamValue("COUNTY_ID")); // 区县
        String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID")); // 地市
        String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); // 省份
        String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));// 产地
        String transportOrg = CommonUtils.checkNull(request.getParamValue("transportOrg"));// 承运物流
        String transType = CommonUtils.checkNull(request.getParamValue("transType"));// 发运方式

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT FD2.FIX_NAME TRANS_ORG,\n");
        sql.append("       FD1.FIX_NAME TRANS_TYPE,\n");
        sql.append("       CP.PROVICE_NAME || '--' || CP.CITY_NAME END_NAME,\n");
        sql.append("       CD.ARRIVE_DAYS,\n");
        sql.append("       CD.FIRST_WEIGHT,\n");
        sql.append("       CD.ADDITIONAL_WEIGHT,\n");
        sql.append("       CD.MINI_WEIGHT,\n");
        sql.append("       CD.DIS_ID, \n");
        sql.append("       CD.REMARK\n");
        sql.append("  FROM TT_PART_CITY_DIS       CD,\n");
        sql.append("       TT_PART_FIXCODE_DEFINE FD1,\n");
        sql.append("       TT_PART_FIXCODE_DEFINE FD2,\n");
        sql.append("       VW_PART_CITY_PROVICE   CP\n");
        sql.append(" WHERE CD.TRANS_TYPE = FD1.FIX_VALUE\n");
        sql.append("   AND CD.LOGI_ID = FD2.FIX_VALUE\n");
        sql.append("   AND FD1.FIX_GOUPTYPE = '92251006'\n");
        sql.append("   AND FD2.FIX_GOUPTYPE = '92251008'\n");
        sql.append("   AND CD.END_PLACE_ID = CP.CITY_ID\n");
        if (!"".equals(provinceId) && null != provinceId) {
            sql.append("\t AND cp.PROVICE_CODE LIKE '%" + provinceId + "%'");
        }
        if (!"".equals(cityId) && null != cityId) {
            sql.append(" AND cp.CITY_CODE  LIKE '%" + cityId + "%'");
        }
        if (!"".equals(transportOrg) && null != transportOrg) {
            sql.append("  AND CD.LOGI_ID ='" + transportOrg + "'\n");
        }
        if (!"".equals(transType) && null != transType) {
            sql.append("  AND CD.TRANS_TYPE ='" + transType + "'\n");
        }
        sql.append("ORDER BY FD2.FIX_NAME,FD1.FIX_NAME,CP.PROVICE_NAME");

        PageResult<Map<String, Object>> ps = null;
        ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 城市里程数信息查询导出
     *
     * @param map
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getCityMileageExport(Map<String, Object> map) throws Exception {

        String countyId = (String) map.get("countyId");// 区县
        String cityId = (String) map.get("CITY_ID");// 地市
        String provinceId = (String) map.get("PROVINCE_ID");// 省份

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT FD2.FIX_NAME TRANS_ORG,\n");
        sql.append("       FD1.FIX_NAME TRANS_TYPE,\n");
        sql.append("       CP.PROVICE_NAME,\n");
        sql.append("       CP.CITY_NAME,\n");
        sql.append("       CD.ARRIVE_DAYS,\n");
        sql.append("       CD.FIRST_WEIGHT,\n");
        sql.append("       CD.ADDITIONAL_WEIGHT,\n");
        sql.append("       CD.MINI_WEIGHT,\n");
        sql.append("       CD.DIS_ID\n");
        sql.append("  FROM TT_PART_CITY_DIS       CD,\n");
        sql.append("       TT_PART_FIXCODE_DEFINE FD1,\n");
        sql.append("       TT_PART_FIXCODE_DEFINE FD2,\n");
        sql.append("       VW_PART_CITY_PROVICE   CP\n");
        sql.append(" WHERE CD.TRANS_TYPE = FD1.FIX_VALUE\n");
        sql.append("   AND CD.LOGI_ID = FD2.FIX_VALUE\n");
        sql.append("   AND FD1.FIX_GOUPTYPE = '92251006'\n");
        sql.append("   AND FD2.FIX_GOUPTYPE = '92251008'\n");
        sql.append("   AND CD.END_PLACE_ID = CP.CITY_ID\n");

        String regionCode = "";
        if (countyId != null && !countyId.equals("")) {
            regionCode = countyId;
        } else {
            if (cityId != null && !cityId.equals("")) {
                regionCode = cityId;
            } else {
                if (provinceId != null && !provinceId.equals("")) {
                    regionCode = provinceId;
                }
            }
        }
        if (regionCode != "") {
            sql.append(" AND CD.END_PLACE_ID IN\n");
            sql.append("    (SELECT TR.REGION_ID\n");
            sql.append("       FROM TM_REGION TR\n");
            sql.append("      START WITH TR.REGION_CODE = '" + regionCode + "'\n");
            sql.append("     CONNECT BY PRIOR TR.REGION_ID = TR.PARENT_ID)");
        }

        sql.append("ORDER BY CP.PROVICE_NAME");

        List<Map<String, Object>> list = null;
        list = (List<Map<String, Object>>) dao.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public PageResult<Map<String, Object>> addCityQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

        String disIds = (String) map.get("disIds");// 里程IDS

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT CP.PROVICE_ID,\n");
        sql.append("       CP.PROVICE_NAME PROVINCE_NAME,\n");
        sql.append("       CP.CITY_NAME    CITY_NAME,\n");
        sql.append("       CP.CITY_ID      CITY_ID\n");
        sql.append("  FROM VW_PART_CITY_PROVICE CP\n");
        sql.append(" WHERE 1 = 1\n");

        if (null != disIds && !"".equals(disIds)) {
            sql.append(" AND CP.CITY_ID IN  (" + disIds + ")");
        }
        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public void saveCityMileage(Map<String, Object> map) {
        String userId = (String) map.get("userId");
        String cityId = (String) map.get("CITY_ID");
        String province = (String) map.get("PROVINCE_ID");
        String distance = (String) map.get("DISTANCE");
        String arriveDays = (String) map.get("ARRIVE_DAYS");
        String disId = (String) map.get("DIS_ID");
        String areaId = (String) map.get("AREA_ID");
        if (distance == null) {
            distance = "";
        }
        if (arriveDays == null) {
            arriveDays = "";
        }

        StringBuffer sql = new StringBuffer();
        sql.append("merge into TT_PART_CITY_DIS TD \n");
        sql.append("using (SELECT TMP.AREA_ID,\n");
        sql.append("TMP.AREA_NAME YIELDLY,\n");
        sql.append("TMP.PROVINCE_ID,\n");
        sql.append("TMP.PROVINCE_NAME,\n");
        sql.append("TMP.CITY_ID,\n");
        sql.append("TMP.CITY_NAME,\n");
        sql.append("TSCD.DISTANCE,\n");
        sql.append("TSCD.ARRIVE_DAYS\n");
        sql.append("FROM (SELECT TBA.AREA_ID,\n");
        sql.append("TBA.AREA_NAME,\n");
        sql.append("TR1.REGION_ID   PROVINCE_ID,\n");
        sql.append("TR1.REGION_NAME PROVINCE_NAME,\n");
        sql.append("TR3.REGION_ID   CITY_ID,\n");
        sql.append("TR3.REGION_NAME CITY_NAME\n");
        sql.append("FROM TM_REGION TR1, TM_REGION TR2,TM_REGION TR3,TM_BUSINESS_AREA TBA\n");
        sql.append("WHERE TR2.PARENT_ID = TR1.REGION_ID\n");
        sql.append("AND TR2.REGION_ID = TR3.PARENT_ID\n");
        sql.append("AND TR1.REGION_TYPE =").append(Constant.REGION_TYPE_02).append("\n");
//		sql.append("AND TBA.STATUS =  ").append(Constant.STATUS_ENABLE).append("\n");
//		
//		sql.append("UNION\n"); 	
//		
//		sql.append("SELECT TBA.AREA_ID,\n");
//		sql.append("       TBA.AREA_NAME,\n");
//		sql.append("       TR1.REGION_ID   PROVINCE_ID,\n");
//		sql.append("       TR1.REGION_NAME PROVINCE_NAME,\n");
//		sql.append("       TR3.REGION_ID   CITY_ID,\n");
//		sql.append("       TR3.REGION_NAME   CITY_NAME"); 
//		sql.append("          FROM TM_REGION        TR1,\n");
//		sql.append("               TM_REGION        TR2,\n");
//		sql.append("               TM_BUSINESS_AREA TBA\n");
//		sql.append("         WHERE TR2.PARENT_ID = TR1.REGION_ID\n");
//		sql.append("           AND TR1.REGION_TYPE = ").append(Constant.REGION_TYPE_02).append("\n");
        sql.append("           AND TBA.STATUS = ").append(Constant.STATUS_ENABLE).append(") TMP,\n");

        sql.append("TT_PART_CITY_DIS TSCD\n");
        sql.append("WHERE TMP.AREA_ID = TSCD.YIELDLY(+)\n");
        sql.append("AND TMP.PROVINCE_ID = TSCD.PROVINCE_ID(+)\n");
        sql.append("AND TMP.CITY_ID = TSCD.CITY_ID(+)\n");
        sql.append("AND TMP.AREA_ID = " + areaId + "\n");
        sql.append("AND TMP.PROVINCE_ID = " + province + "\n");
        sql.append("AND TMP.CITY_ID = " + cityId + "\n");
        sql.append(") TD1\n");
        sql.append("on (TD.YIELDLY = TD1.AREA_ID and TD.PROVINCE_ID = TD1.PROVINCE_ID and TD.CITY_ID = TD1.CITY_ID)\n");
        sql.append("when matched then\n");
        sql.append(" update\n");
        sql.append("set TD.DISTANCE = '" + distance + "',\n");
        sql.append(" TD.ARRIVE_DAYS = '" + arriveDays + "',\n");
        sql.append(" TD.UPDATE_BY   = " + userId + ",\n");
        sql.append(" TD.UPDATE_DATE = sysdate\n");
        sql.append("when not matched then\n");
        sql.append("insert\n");
        sql.append("(TD.DIS_ID,\n");
        sql.append("TD.YIELDLY,\n");
        sql.append("TD.PROVINCE_ID,\n");
        sql.append("TD.CITY_ID,\n");
        sql.append("TD.DISTANCE,\n");
        sql.append("TD.ARRIVE_DAYS,\n");
        sql.append("TD.CREATE_BY,\n");
        sql.append(" TD.CREATE_DATE)\n");
        sql.append("  values\n");
        sql.append("(" + disId + ",\n");
        sql.append("TD1.AREA_ID,\n");
        sql.append("TD1.PROVINCE_ID,\n");
        sql.append("TD1.CITY_ID,\n");
        sql.append("'" + distance + "',\n");
        sql.append("'" + arriveDays + "',\n");
        sql.append("" + userId + ",\n");
        sql.append("sysdate)\n");
        dao.update(sql.toString(), null);
    }


    /**
     * @param : @param fixName
     * @param : @return
     * @return :
     * @Title : 验证承运物流是否存在 并返回承运物流ID
     * @Description:
     */
    public List<Map<String, Object>> checkFixName(String fixName, String type) {
        List<Map<String, Object>> list = null;
        String sql = "select d.FIX_VALUE " +
                "from tt_part_fixcode_define d " +
                "where d.fix_gouptype =" + type +
                " and d.fix_name = '" + fixName + "'";

        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }


    /**
     * @param : @param fixName
     * @param : @return
     * @return :
     * @Title : 验证省份是否存在 并返回其ID
     * @Description:
     */
    public List<Map<String, Object>> checkProvice(String proviceName) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append("select * from (select distinct p.PROVICE_NAME, p.PROVICE_ID from vw_part_city_provice p) r \n");
        sql.append("where r.PROVICE_NAME like '%" + proviceName + "%' \n");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param fixName
     * @param : @return
     * @return :
     * @Title : 验证城市是否存在 并返回其ID
     * @Description:
     */
    public List<Map<String, Object>> checkCtiy(String cityName, String proviceName) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append("select r.CITY_ID from vw_part_city_provice r \n");
        sql.append(" where r.CITY_NAME like '%" + cityName + "%' \n");
        sql.append(" and r.PROVICE_ID = '" + proviceName + "' \n");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param fixName
     * @param : @return
     * @return :
     * @Title : 获取当前省份所有城市
     * @Description:
     */
    public List<Map<String, Object>> ctiyQuery(String proviceId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();

        sql.append("select r.CITY_ID from vw_part_city_provice r \n");
        sql.append("where r.provice_id='" + proviceId + "' \n");

        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public void insertCtiyMileage(long disId, String exportSel) {
        StringBuffer sql = new StringBuffer();


        if (exportSel.equals("1")) {
            sql.append(" MERGE INTO TT_PART_CITY_DIS PD \n");
            sql.append(" USING (SELECT CP.CITY_ID, \n");
            sql.append("               CP.PROVICE_ID, \n");
            sql.append("               CD.ARRIVE_DAYS, \n");
            sql.append("               CD.TRANS_TYPE, \n");
            sql.append("               CD.LOGI_ID, \n");
            sql.append("               CD.FIRST_WEIGHT, \n");
            sql.append("               CD.ADDITIONAL_WEIGHT, \n");
            sql.append("               CD.MINI_WEIGHT, \n");
            sql.append("               CD.REMARK \n");
            sql.append("          FROM TMP_PART_CITY_DIS CD, VW_PART_CITY_PROVICE CP \n");
            sql.append("         WHERE CD.START_PLACE_ID = CP.PROVICE_ID \n");
            sql.append("          AND CD.DIS_ID = " + disId + ") XX \n");

            sql.append(" ON (PD.START_PLACE_ID = XX.PROVICE_ID  \n");
            sql.append(" 	    AND PD.END_PLACE_ID = XX.CITY_ID  \n");
            sql.append(" 	    AND PD.TRANS_TYPE = XX.TRANS_TYPE  \n");
            sql.append(" 	    AND PD.LOGI_ID = XX.LOGI_ID) \n");

            sql.append(" WHEN MATCHED THEN \n");
            sql.append("   UPDATE \n");
//			sql.append("      SET PD.ARRIVE_DAYS       = XX.ARRIVE_DAYS, \n");
            sql.append("      SET PD.FIRST_WEIGHT      = XX.FIRST_WEIGHT, \n");
            sql.append("          PD.ADDITIONAL_WEIGHT = XX.ADDITIONAL_WEIGHT, \n");
            sql.append("          PD.MINI_WEIGHT       = XX.MINI_WEIGHT, \n");
            sql.append("          PD.REMARK            = XX.REMARK, \n");
            sql.append("          PD.UPDATE_DATE       = SYSDATE, \n");
            sql.append("          PD.UPDATE_BY         = -1 \n");

            sql.append(" WHEN NOT MATCHED THEN \n");
            sql.append("   INSERT \n");
            sql.append("     (PD.DIS_ID, \n");
            sql.append("      PD.START_PLACE_ID, \n");
            sql.append("      PD.END_PLACE_ID, \n");
            sql.append("      PD.ARRIVE_DAYS, \n");
            sql.append("      PD.CREATE_DATE, \n");
            sql.append("      PD.CREATE_BY, \n");
            sql.append("      PD.TRANS_TYPE, \n");
            sql.append("      PD.LOGI_ID, \n");
            sql.append("      PD.FIRST_WEIGHT, \n");
            sql.append("      PD.ADDITIONAL_WEIGHT, \n");
            sql.append("      PD.MINI_WEIGHT, \n");
            sql.append("      PD.REMARK) \n");
            sql.append("   VALUES \n");
            sql.append("     (F_GETID, \n");
            sql.append("      XX.PROVICE_ID, \n");
            sql.append("      XX.CITY_ID, \n");
            sql.append("      XX.ARRIVE_DAYS, \n");
            sql.append("      SYSDATE, \n");
            sql.append("      -1, \n");
            sql.append("      XX.TRANS_TYPE, \n");
            sql.append("      XX.LOGI_ID, \n");
            sql.append("      XX.FIRST_WEIGHT, \n");
            sql.append("      XX.ADDITIONAL_WEIGHT, \n");
            sql.append("      XX.MINI_WEIGHT, \n");
            sql.append("      XX.REMARK) \n");
        } else {
            sql.append(" MERGE INTO TT_PART_CITY_DIS PD \n");
            sql.append(" USING (SELECT CD.END_PLACE_ID, \n");
            sql.append("           CD.START_PLACE_ID, \n");
            sql.append("           CD.ARRIVE_DAYS, \n");
            sql.append("           CD.TRANS_TYPE, \n");
            sql.append("           CP.LOGI_ID, \n");
            sql.append("           CP.FIRST_WEIGHT, \n");
            sql.append("           CP.ADDITIONAL_WEIGHT, \n");
            sql.append("           CP.MINI_WEIGHT, \n");
            sql.append("           CP.REMARK \n");
            sql.append("      FROM TMP_PART_CITY_DIS CD, TT_PART_CITY_DIS CP \n");
            sql.append("     WHERE CD.START_PLACE_ID = CP.START_PLACE_ID \n");
            sql.append("       AND CD.END_PLACE_ID = CP.END_PLACE_ID  \n");
            sql.append("       AND CD.TRANS_TYPE = CP.TRANS_TYPE  \n");
            sql.append("       AND CD.DIS_ID = '" + disId + "') XX \n");

            sql.append(" ON (PD.START_PLACE_ID = XX.START_PLACE_ID  \n");
            sql.append(" 	    AND PD.END_PLACE_ID = XX.END_PLACE_ID  \n");
            sql.append(" 	    AND PD.TRANS_TYPE = XX.TRANS_TYPE  \n");
            sql.append(" 	    AND PD.LOGI_ID = XX.LOGI_ID) \n");

            sql.append(" WHEN MATCHED THEN \n");
            sql.append("   UPDATE \n");
            sql.append("      SET PD.ARRIVE_DAYS       = XX.ARRIVE_DAYS, \n");
            sql.append("          PD.UPDATE_DATE       = SYSDATE, \n");
            sql.append("          PD.UPDATE_BY         = -1 \n");
            sql.append(" WHEN NOT MATCHED THEN \n");

            sql.append("   INSERT \n");
            sql.append("     (PD.DIS_ID, \n");
            sql.append("      PD.START_PLACE_ID, \n");
            sql.append("      PD.END_PLACE_ID, \n");
            sql.append("      PD.ARRIVE_DAYS, \n");
            sql.append("      PD.CREATE_DATE, \n");
            sql.append("      PD.CREATE_BY, \n");
            sql.append("      PD.TRANS_TYPE, \n");
            sql.append("      PD.LOGI_ID, \n");
            sql.append("      PD.FIRST_WEIGHT, \n");
            sql.append("      PD.ADDITIONAL_WEIGHT, \n");
            sql.append("      PD.MINI_WEIGHT, \n");
            sql.append("      PD.REMARK) \n");
            sql.append("   VALUES \n");
            sql.append("     (F_GETID, \n");
            sql.append("      XX.START_PLACE_ID, \n");
            sql.append("      XX.END_PLACE_ID, \n");
            sql.append("      XX.ARRIVE_DAYS, \n");
            sql.append("      SYSDATE, \n");
            sql.append("      -1, \n");
            sql.append("      XX.TRANS_TYPE, \n");
            sql.append("      XX.LOGI_ID, \n");
            sql.append("      XX.FIRST_WEIGHT, \n");
            sql.append("      XX.ADDITIONAL_WEIGHT, \n");
            sql.append("      XX.MINI_WEIGHT, \n");
            sql.append("      XX.REMARK) \n");

        }


        insert(sql.toString());
    }

}
