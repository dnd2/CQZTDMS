package com.infodms.dms.dao.parts.storageManager.partDistributeMgr;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartLocationFloorPO;
import com.infodms.dms.po.TtPartLocationLinePO;
import com.infodms.dms.po.TtPartLocationShelfPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PartDistributeMgrDao extends BaseDao<PO> {

    private static final PartDistributeMgrDao dao = new PartDistributeMgrDao();

    private PartDistributeMgrDao() {
    }

    public static final PartDistributeMgrDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    public PageResult<Map<String, Object>> getPartLineQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

        String part_code = (String) map.get("PART_CODE");
        String part_name = (String) map.get("PART_NAME");
        String TstartDate = (String) map.get("TstartDate");
        String TendDate = (String) map.get("TendDate");

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT TD.*,twd.wh_name\n");
        sql.append("  FROM TT_PART_LOCATION_DISTRIBUTE TD, TT_PART_WAREHOUSE_DEFINE twd\n");
        sql.append(" WHERE TD.Wh_Id = TwD.Wh_Id\n");
        sql.append("   AND NVL(TD.WAIT_STORAGE_NUM, 0) > 0\n");
        sql.append("   AND TD.STATUS = 1\n");
        sql.append("   AND TD.STATE = 10011001");

        if (part_code != null && !"".equals(part_code)) {
            sql.append("   AND TD.PART_CODE  like '%" + part_code + "%'\n");
        }
        if (part_name != null && !"".equals(part_name)) {
            sql.append("   AND TD.PART_NAME LIKE '%" + part_name + "%'\n");
        }
        if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND TD.STORAGE_DATE BETWEEN TO_DATE('" + TstartDate + "','YYYY-MM-DD') AND TO_DATE('" + TendDate + "','YYYY-MM-DD')");
        } else if ((TstartDate == null || "".equals(TstartDate)) && (TendDate != null && !"".equals(TendDate))) {
            sql.append("   AND TD.STORAGE_DATE < TO_DATE('" + TendDate + "','YYYY-MM-DD')");
        } else if ((TstartDate != null && !"".equals(TstartDate)) && (TendDate == null || "".equals(TendDate))) {
            sql.append("   AND TD.STORAGE_DATE > TO_DATE('" + TstartDate + "','YYYY-MM-DD') ");
        }
        sql.append("  ORDER BY TD.STORAGE_DATE DESC");
        List<Object> params = new LinkedList<Object>();
        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);

        return ps;
    }

    /**
     * 货位查询SQL
     *
     * @param map
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> queryLocation(Map<String, Object> map, int curPage, int pageSize) {
        String floor_id = (String) map.get("FLOOR_ID");
        String shelf_id = (String) map.get("SHELF_ID");
        String line_id = (String) map.get("LINE_ID");
        String position_code = (String) map.get("POSITION_CODE");
        String locCode = (String) map.get("loc_Code");
        String partOldCode = (String) map.get("part_OldCode");
        String loc_code = "";
        String whId = (String) map.get("whId");
        if (line_id != null && !"".equals(line_id)) {
            String line_code = "";
            TtPartLocationLinePO tlpo = new TtPartLocationLinePO();
            tlpo.setLineId(Long.parseLong(line_id));
            List<PO> tlList = this.select(tlpo);
            if (tlList != null && tlList.size() > 0 && tlList.get(0) != null) {
                tlpo = (TtPartLocationLinePO) tlList.get(0);
                line_code = tlpo.getLineCode();
            }
            loc_code += line_code + "-";
        } else {
//            loc_code += "%-";
        }
        if (shelf_id != null && !"".equals(shelf_id)) {
            String shelf_code = "";
            TtPartLocationShelfPO tlpo = new TtPartLocationShelfPO();
            tlpo.setShelfId(Long.parseLong(shelf_id));
            List<PO> tlList = this.select(tlpo);
            if (tlList != null && tlList.size() > 0 && tlList.get(0) != null) {
                tlpo = (TtPartLocationShelfPO) tlList.get(0);
                shelf_code = tlpo.getShelfCode();
            }
            loc_code += shelf_code + "-";
        } else {
//            loc_code += "%-";
        }
        if (floor_id != null && !"".equals(floor_id)) {
            String floor_code = "";
            TtPartLocationFloorPO tlpo = new TtPartLocationFloorPO();
            tlpo.setFloorId(Long.parseLong(floor_id));
            List<PO> tlList = this.select(tlpo);
            if (tlList != null && tlList.size() > 0 && tlList.get(0) != null) {
                tlpo = (TtPartLocationFloorPO) tlList.get(0);
                floor_code = tlpo.getFloorCode();
            }
            loc_code += floor_code + "-";
        } else {
//            loc_code += "%-";
        }
        if (position_code != null && !"".equals(position_code)) {
            loc_code += position_code;
        } else {
//            loc_code += "%";
        }

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT L.LOC_ID,\n");
        sql.append("       L.LOC_CODE,\n");
        sql.append("       L.LOC_CODE LOC_NAME\n");
        sql.append("  FROM TT_PART_LOCATION L\n");
        sql.append(" WHERE L.STATE = 10011001\n");
        sql.append("   AND L.STATUS = 1");
        sql.append("   AND L.WH_ID = " + whId + "\n");

        if (locCode != null && !"".equals(locCode)) {
            sql.append("   AND upper(L.LOC_CODE) LIKE upper('" + locCode + "')\n");
        } else if (loc_code != null && !"".equals(loc_code)) {
            sql.append("   AND upper(L.LOC_CODE) LIKE upper('" + loc_code + "')\n");
        }
        if (!"".equals(partOldCode) && null != partOldCode) {
            sql.append("AND EXISTS((SELECT 1\n");
            sql.append("             FROM TT_PART_LOACTION_DEFINE D, TT_PART_DEFINE PD\n");
            sql.append("            WHERE D.RELOC_ID = L.LOC_ID\n");
            sql.append("              AND D.PART_ID = PD.PART_ID\n");
            sql.append("              AND UPPER(PD.PART_OLDCODE) LIKE\n");
            sql.append("                  UPPER('%" + partOldCode + "%')))");
        }
        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public Map<String, Object> getWaitStorageNumTotal() {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT SUM(TD.WAIT_STORAGE_NUM) WAIT_STORAGE_NUM_TOTAL\n");
        sql.append("  FROM TT_PART_LOCATION_DISTRIBUTE TD\n");
        sql.append(" WHERE NVL(TD.WAIT_STORAGE_NUM, 0) > 0\n");
        sql.append("   AND TD.STATUS = 1\n");
        sql.append("   AND TD.STATE = 10011001");

        Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
        return map;
    }

    public Map<String, Object> getObjectById(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM TT_PART_LOCATION_DISTRIBUTE TD WHERE TD.DISTRIBUTE_ID = " + id + " AND TD.STATUS = 1 AND TD.STATE = " + Constant.STATUS_ENABLE);
        Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
        return map;
    }

    public List<Map<String, Object>> getPartLocation() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM TT_PART_LOCATION T ORDER BY T.LOC_CODE");
        List<Map<String, Object>> map = super.pageQuery(sql.toString(), null, getFunName());
        return map;
    }

    public PageResult<Map<String, Object>> getPartLog(String distributeId, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT DL.*, W.WH_NAME\n");
        sql.append("  FROM TT_PART_LOCATION_DISTR_LOG DL, TT_PART_WAREHOUSE_DEFINE W\n");
        sql.append(" WHERE DL.WH_ID = W.WH_ID\n");
        sql.append("   AND DL.STATE =" + Constant.STATUS_ENABLE + "\n");
        sql.append("   AND DL.DISTRIBUTE_ID =" + distributeId + "\n");
        sql.append(" ORDER BY DL.STORAGE_DATE DESC");

        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 入库记录查询
     *
     * @param
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> getStorageRecord(RequestWrapper request, int curPage, int pageSize) {
        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//经销商编码
        String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//经销商编码
        String tstartDate = CommonUtils.checkNull(request.getParamValue("TstartDate"));//发运开始
        String tendDate = CommonUtils.checkNull(request.getParamValue("TendDate"));//发运结束

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT DL.*, W.WH_NAME\n");
        sql.append("  FROM TT_PART_LOCATION_DISTR_LOG DL, TT_PART_WAREHOUSE_DEFINE W\n");
        sql.append(" WHERE DL.WH_ID = W.WH_ID\n");
        sql.append("   AND DL.STATE =" + Constant.STATUS_ENABLE + "\n");
        if (!"".equals(partCode) && null != partCode) {
            sql.append("   AND DL.PART_CODE LIKE '%" + partCode + "%'\n");
        }
        if (!"".equals(partName) && null != partName) {
            sql.append("   AND DL.PART_NAME LIKE '%" + partName + "%'\n");
        }
        if (!"".equals(tstartDate) && tstartDate != null) {
            sql.append("   AND TRUNC(DL.STORAGE_DATE) >= TO_DATE('" + tstartDate + "','YYYY-MM-DD')\n");
        }
        if (!"".equals(tendDate) && tendDate != null) {
            sql.append("   AND TRUNC(DL.STORAGE_DATE) <= TO_DATE('" + tendDate + "','YYYY-MM-DD')\n");
        }
        sql.append(" ORDER BY DL.STORAGE_DATE DESC");

        PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getLocId(String partId, String relocId, String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM TT_PART_LOACTION_DEFINE T WHERE T.WH_ID = " + whId + " AND T.PART_ID = " + partId + " AND T.RELOC_ID = " + relocId);
        List<Map<String, Object>> map = super.pageQuery(sql.toString(), null, getFunName());
        return map;
    }

    /**
     * 取库存
     *
     * @param distri_id
     * @return
     */
    public Map<String, Object> getITEM_QTY(String distri_id) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT S.*\n");
        sql.append("  FROM VW_PART_STOCK              S,\n");
        sql.append("       TT_PART_LOCATION_DISTR_LOG L,\n");
        sql.append("       TT_PART_LOACTION_DEFINE    D\n");
        sql.append(" WHERE S.PART_ID = D.PART_ID\n");
        sql.append("   AND S.WH_ID = D.WH_ID\n");
        sql.append("   AND S.LOC_ID = D.LOC_ID\n");
        sql.append("   AND L.LOC_ID = D.RELOC_ID\n");
        sql.append("   AND S.STATE = 10011001\n");
        sql.append("   AND S.STATUS = 1\n");
        sql.append("   AND L.DISTRI_LOG_ID = " + distri_id + "\n");

        Map<String, Object> map = super.pageQueryMap(sql.toString(), null, getFunName());
        return map;
    }

    /**
     * 入库重复校验
     *
     * @param distr_id ID
     * @return
     */

    public List<Map<String, Object>> PartDistributeCheck(String distr_id) {
        StringBuffer sql = new StringBuffer();

        sql.append("WITH TT_PART_LOCATION_DISTR_L AS\n");
        sql.append(" (SELECT DL.DISTRIBUTE_ID, SUM(DL.PART_NUM) PART_NUM\n");
        sql.append("    FROM TT_PART_LOCATION_DISTR_LOG DL\n");
        sql.append("   WHERE DL.STATE = 10011001\n");
        sql.append("     AND DL.STATUS = 1\n");
        sql.append("   GROUP BY DL.DISTRIBUTE_ID)\n");
        sql.append("SELECT LD.DISTRIBUTE_ID, LD.ERP_STORAGE_NUM, DL.PART_NUM\n");
        sql.append("  FROM TT_PART_LOCATION_DISTRIBUTE LD, TT_PART_LOCATION_DISTR_L DL\n");
        sql.append(" WHERE LD.DISTRIBUTE_ID = DL.DISTRIBUTE_ID(+)\n");
        sql.append("   AND LD.ERP_STORAGE_NUM < NVL(DL.PART_NUM,0)\n");
        sql.append("   AND LD.DISTRIBUTE_ID = " + distr_id + "\n");

        List<Map<String, Object>> map = super.pageQuery(sql.toString(), null, getFunName());
        return map;
    }
}
