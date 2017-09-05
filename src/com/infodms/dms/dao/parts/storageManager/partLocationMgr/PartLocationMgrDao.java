package com.infodms.dms.dao.parts.storageManager.partLocationMgr;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PartLocationMgrDao extends BaseDao<PO> {

    private static final PartLocationMgrDao dao = new PartLocationMgrDao();

    private PartLocationMgrDao() {
    }

    public static final PartLocationMgrDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    public PageResult<Map<String, Object>> queryData(Map<String, Object> map, int curPage, int pageSize) throws Exception {

        String loc_code = (String) map.get("LOC_CODE");
        String loc_name = (String) map.get("LOC_NAME");
        String state = (String) map.get("STATE");
        String whId = (String) map.get("WH_ID");

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TP.*, TW.WH_NAME\n");
        sql.append("  FROM TT_PART_LOCATION TP, TT_PART_WAREHOUSE_DEFINE TW\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND TP.WH_ID = TW.WH_ID");

        if (loc_code != null && !"".equals(loc_code)) {
            sql.append("   AND TP.LOC_CODE  LIKE '%" + loc_code + "%'\n");
        }
        if (loc_name != null && !"".equals(loc_name)) {
            sql.append("   AND TP.LOC_NAME LIKE '%" + loc_name + "%'\n");
        }
        if (state != null && !"".equals(state)) {
            sql.append("   AND TP.STATE LIKE '%" + state + "%'\n");
        }
        if (whId != null && !"".equals(whId)) {
            sql.append("   AND TP.WH_ID LIKE '%" + whId + "%'\n");
        }
        sql.append("  ORDER BY TP.LOC_CODE");
        List<Object> params = new LinkedList<Object>();
        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryUpdateLog(Map<String, Object> map, int curPage, int pageSize) throws Exception {

        String loc_code = (String) map.get("LOC_CODE");
        String loc_name = (String) map.get("LOC_NAME");
        String state = (String) map.get("STATE");

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM TT_LOACTION_HISTORY TP WHERE 1=1 ");

        if (loc_code != null && !"".equals(loc_code)) {
            sql.append("   AND TP.LOC_CODE  LIKE '%" + loc_code + "%'\n");
        }
        if (loc_name != null && !"".equals(loc_name)) {
            sql.append("   AND TP.LOC_NAME LIKE '%" + loc_name + "%'\n");
        }
        if (state != null && !"".equals(state)) {
            sql.append("   AND TP.STATUS LIKE '%" + state + "%'\n");
        }
        sql.append("  ORDER BY TP.HS_ID DESC");
        List<Object> params = new LinkedList<Object>();
        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
        return ps;
    }

    /*
     *
     */
    public boolean isRepeatLOC_CODE(String locCode, String whId) {

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.LOC_CODE FROM TT_PART_LOCATION T WHERE T.LOC_CODE =  '" + locCode + "' AND T.WH_ID = '" + whId + "'");
        List<Map<String, Object>> rs = super.pageQuery(sql.toString(), null, getFunName());
        boolean flag = false;
        if (rs != null & rs.size() > 0) {
            if (rs.get(0).get("LOC_CODE") != null) {
                flag = true;
            }
        }
        return flag;

    }

    public boolean updateLocCode(String old_loc_code, String new_loc_code, String whId) {
        String sql = "UPDATE TT_PART_LOCATION SET LOC_CODE='" + new_loc_code + "',LOC_NAME='" + new_loc_code + "' WHERE 1=1  AND LOC_CODE='" + old_loc_code + "' AND WH_ID='" + whId + "'";
        int num = this.update(sql, null);
        return num > 0 ? true : false;
    }

    public List<Map<String, Object>> queryImportLocation(String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM TT_PART_LOCATION T WHERE T.POSITION_ID IS NULL AND T.WH_ID = '" + whId + "'");
        List<Map<String, Object>> rs = super.pageQuery(sql.toString(), null, getFunName());
        return rs;
    }

    public String getPositionId(String lineCode, String shelfCode, String floorCode, String positionCode) {
        StringBuffer sql = new StringBuffer();
        String positionId = null;
        sql.append("SELECT TP.POSITION_ID ");
        sql.append(" FROM TT_PART_LOCATION_LINE TL,TT_PART_LOCATION_SHELF TS,TT_PART_LOCATION_FLOOR TF,TT_PART_LOCATION_POSITION TP  ");
        sql.append(" WHERE TP.FLOOR_ID = TF.FLOOR_ID AND TF.SHELF_ID = TS.SHELF_ID AND TS.LINE_ID = TL.LINE_ID");
        sql.append(" AND TP.POSITION_CODE = '" + positionCode + "' AND TF.FLOOR_CODE = '" + floorCode + "' AND TS.SHELF_CODE = '" + shelfCode + "' AND TL.LINE_CODE = '" + lineCode + "'");
        List<Map<String, Object>> rs = super.pageQuery(sql.toString(), null, getFunName());
        for (Map<String, Object> map : rs) {
            positionId = map.get("POSITION_ID").toString();
        }
        return positionId;
    }
}
