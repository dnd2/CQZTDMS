package com.infodms.dms.dao.parts.storageManager.partShelfMgr;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PartShelfMgrDao extends BaseDao<PO> {

    private static final PartShelfMgrDao dao = new PartShelfMgrDao();

    private PartShelfMgrDao() {
    }

    public static final PartShelfMgrDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {

        return null;
    }

    public PageResult<Map<String, Object>> getPartShelfQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

        String shelf_code = (String) map.get("SHELF_CODE");
        String line_code = (String) map.get("LINE_CODE");
        String whId = (String) map.get("WH_ID");

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT TL.SHELF_ID,TL.SHELF_CODE,TLL.LINE_CODE,TLL.LINE_NAME,TL.IN_STATUS,TL.OUT_STATUS,TL.STATUS,TL.STATE,");
        sql.append(" (SELECT COUNT(TF.FLOOR_ID) FROM TT_PART_LOCATION_FLOOR TF WHERE TF.SHELF_ID = TL.SHELF_ID AND TLL.LINE_ID = TL.LINE_ID) FLOOR_COUNT,");
        sql.append(" (SELECT COUNT(TP.POSITION_ID) FROM TT_PART_LOCATION_FLOOR TF ,TT_PART_LOCATION_POSITION TP ");
        sql.append(" WHERE TP.FLOOR_ID = TF.FLOOR_ID AND TF.SHELF_ID = TL.SHELF_ID AND TL.LINE_ID = TLL.LINE_ID) POSITION_COUNT");
        sql.append(" FROM TT_PART_LOCATION_SHELF TL , TT_PART_LOCATION_LINE TLL WHERE TL.LINE_ID = TLL.LINE_ID");

        if (shelf_code != null && !"".equals(shelf_code)) {
            sql.append("   AND TL.SHELF_CODE  LIKE '%" + shelf_code + "%'\n");
        }
        if (line_code != null && !"".equals(line_code)) {
            sql.append("   AND TLL.LINE_CODE LIKE '%" + line_code + "%'\n");
        }
        if (whId != null && !"".equals(whId)) {
            sql.append("   AND TLL.WH_ID LIKE '%" + whId + "%'\n");
        }
        sql.append("  ORDER BY TL.SHELF_CODE ASC");
        List<Object> params = new LinkedList<Object>();
        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getLineCodeList(String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT T.LINE_ID,T.LINE_CODE,T.LINE_NAME FROM TT_PART_LOCATION_LINE T WHERE T.WH_ID = '" + whId + "'");
        List<Map<String, Object>> rs = super.pageQuery(sql.toString(), null, getFunName());
        return rs;
    }


    public int getMaxCode(String table, String parentId, String v1, String codeColumn) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT MAX(T." + codeColumn + ") MAXCODE FROM " + table + " T WHERE T." + parentId + " = " + v1);
        List<Map<String, Object>> list = super.pageQuery(sql.toString(), null, getFunName());
        if (list != null && list.size() > 0) {
            Object maxCode = list.get(0).get("MAXCODE");
            return Integer.parseInt(maxCode != null ? maxCode.toString() : "0");
        } else {
            return 0;
        }
    }
}
