package com.infodms.dms.dao.parts.storageManager.partLineMgr;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartLocationLinePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PartLineMgrDao extends BaseDao<PO> {

    private static final PartLineMgrDao dao = new PartLineMgrDao();

    private PartLineMgrDao() {
    }

    public static final PartLineMgrDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    public PageResult<Map<String, Object>> getPartLineQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

        String line_code = (String) map.get("LINE_CODE");
        String line_name = (String) map.get("LINE_NAME");
        String whId = (String) map.get("WH_ID");
        String type = (String) map.get("TYPE");

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TL.*,(SELECT COUNT(TS.SHELF_ID) FROM TT_PART_LOCATION_SHELF TS WHERE TS.LINE_ID = TL.LINE_ID) SHELF_COUNT,");
        sql.append("(SELECT COUNT(TF.FLOOR_ID) FROM TT_PART_LOCATION_SHELF TS,TT_PART_LOCATION_FLOOR TF ");
        sql.append(" WHERE TF.SHELF_ID = TS.SHELF_ID AND TS.LINE_ID = TL.LINE_ID) FLOOR_COUNT,");
        sql.append("(SELECT COUNT(TP.POSITION_ID) FROM TT_PART_LOCATION_SHELF TS,TT_PART_LOCATION_FLOOR TF,TT_PART_LOCATION_POSITION TP ");
        sql.append(" WHERE TP.FLOOR_ID = TF.FLOOR_ID AND TF.SHELF_ID = TS.SHELF_ID AND TS.LINE_ID = TL.LINE_ID) POSITION_COUNT ");
        sql.append(" FROM TT_PART_LOCATION_LINE TL WHERE 1=1 ");

        if (line_code != null && !"".equals(line_code)) {
            sql.append("   AND TL.LINE_CODE  LIKE '%" + line_code + "%'\n");
        }
        if (line_name != null && !"".equals(line_name)) {
            sql.append("   AND TL.LINE_NAME LIKE '%" + line_name + "%'\n");
        }
        if (type != null && !"".equals(type)) {
            sql.append("   AND TL.TYPE LIKE '%" + type + "%'\n");
        }
        if (whId != null && !"".equals(whId)) {
            sql.append("   AND TL.WH_ID LIKE '%" + whId + "%'\n");
        }
        sql.append("  ORDER BY TL.LINE_ID DESC");
        List<Object> params = new LinkedList<Object>();
        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 新增货位排信息
     *
     * @param partLine
     * @author wucl
     * @date 2014-3-3
     */
    public void addPartLine(TtPartLocationLinePO partLine) {
        dao.insert(partLine);
    }

    /**
     * 货位排信息--根据ID获取
     *
     * @param id
     * @author wucl
     * @date 2014-3-3
     */
    public Map<String, Object> getObjectById(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM TT_PART_LOCATION_LINE TD WHERE TD.LINE_ID = " + id);
        Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
        return map;
    }

    public List<Map<String, Object>> getPartLocation() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM TT_PART_LOCATION");
        List<Map<String, Object>> map = super.pageQuery(sql.toString(), null, getFunName());
        return map;
    }

}
