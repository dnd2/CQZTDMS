package com.infodms.dms.dao.parts.storageManager.partSplitManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PartSplitInOrOutDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartSplitInOrOutDao.class);

    private static final PartSplitInOrOutDao dao = new PartSplitInOrOutDao();

    private PartSplitInOrOutDao() {

    }

    public static final PartSplitInOrOutDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartSpiltApplyList(
            String spcpdCode, String startDate, String endDate,
            AclUserBean logonUser, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.spcpd_id, T1.spcpd_code,T1.org_cname, T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.wh_cname,T3.LOC_NAME,T1.part_code,T1.part_oldcode,T1.part_name,T1.spcpd_type,T1.qty,T1.normal_qty,T1.STATE FROM TT_PART_SPCP_MAIN T1,TC_USER T2,TT_PART_LOACTION_DEFINE T3");
            sql.append(" WHERE T1.CREATE_BY=T2.USER_ID AND T1.LOC_ID=T3.LOC_ID");
            sql.append(" AND T1.org_id=").append(logonUser.getOrgId());
            sql.append(" AND (T1.STATE=").append(Constant.PART_SPCPD_STATUS_03);
            sql.append(" OR T1.STATE=").append(Constant.PART_SPCPD_STATUS_04).append(")");
            if (!"".equals(spcpdCode)) {
                sql.append(" AND T1.spcpd_code LIKE '%")
                        .append(spcpdCode).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(" AND T1.STATUS=1");
            sql.append(" ORDER BY T1.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public void insertPartInfo(String spcpdId, AclUserBean logonUser, long iostock_id, int flag) throws Exception {
        try {
            StringBuffer sql1 = new StringBuffer("");
            int configId = 0;
            sql1.append("insert into TT_PART_SPCP_IOSTOCK_MAIN(iostock_id,spcpd_id,spcpd_code,spcpd_type,Org_Id,Org_Code,Org_Cname,Wh_Id,Wh_Cname,Create_Date,Create_By)");
            sql1.append(" SELECT ").append(iostock_id).append(", T1.SPCPD_ID,T1.SPCPD_CODE,T1.SPCPD_TYPE,T1.ORG_ID,T1.ORG_CODE,T1.ORG_CNAME,T1.WH_ID,T1.WH_CNAME,sysdate,").append(logonUser.getUserId());
            sql1.append(" FROM TT_PART_SPCP_MAIN T1 WHERE T1.SPCPD_ID=").append(spcpdId);

            //向拆合出入库明细表中插入总成件信息
            StringBuffer sql2 = new StringBuffer("");
            sql2.append("INSERT INTO TT_PART_SPCP_IOSTOCK_DTL(dtl_id,IOSTOCK_ID,SPCPDTL_ID,LINE_NO,PART_ID,PART_CODE,PART_OLDCODE,PART_CNAME,UNIT,IS_MB,SPLIT_QTY,SPLIT_RATE,NORMAL_QTY,QTY,LOC_ID,LOC_CODE,Create_Date,Create_By)");
            sql2.append(" SELECT F_GETID(),").append(iostock_id).append(",'',0,T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.Part_Name,T.Unit,").append(Constant.IF_TYPE_YES);
            sql2.append(",0,0,T.NORMAL_QTY,T.Qty,T.LOC_ID,T.LOC_CODE,SYSDATE,").append(logonUser.getUserId());
            sql2.append("  FROM TT_PART_SPCP_MAIN T WHERE T.SPCPD_ID=").append(spcpdId);
            //向拆合出入库明细表中插入分总成件信息
            StringBuffer sql3 = new StringBuffer("");
            sql3.append("INSERT INTO TT_PART_SPCP_IOSTOCK_DTL(dtl_id,IOSTOCK_ID,SPCPDTL_ID,LINE_NO,PART_ID,PART_CODE,PART_OLDCODE,PART_CNAME,UNIT,IS_MB,SPLIT_QTY,SPLIT_RATE,NORMAL_QTY,QTY,LOC_ID,LOC_CODE,Create_Date,Create_By)");
            sql3.append(" SELECT F_GETID(),").append(iostock_id).append(",T.DTL_ID,T.Line_No,T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.UNIT,").append(Constant.IF_TYPE_NO);
            sql3.append(",T.SPLIT_QTY,T.SPLIT_RATE,T.NORMAL_QTY,T.QTY,T.LOC_ID,T.LOC_CODE,SYSDATE,").append(logonUser.getUserId());
            sql3.append("  FROM TT_PART_SPLIT_COMPOUND_DTL T WHERE T.SPCPD_ID=").append(spcpdId);

            //还需要向出入库记录表中插入记录
            //向出入库记录表中插入总成件信息
            StringBuffer sql4 = new StringBuffer("");
            sql4.append("INSERT INTO TT_PART_RECORD(RECORD_ID,ADD_FLAG,PART_ID,PART_CODE,PART_OLDCODE,PART_NAME,PART_BATCH,VENDER_ID,PART_NUM,CONFIG_ID,ORDER_ID,LINE_ID,ORG_ID,ORG_CODE,ORG_NAME,WH_ID,WH_NAME,LOC_ID,LOC_CODE,LOC_NAME,OPT_DATE,CREATE_DATE,PERSON_ID,ORDER_CODE,PART_STATE)");
            sql4.append(" SELECT F_GETID(),").append(flag);
            sql4.append(",T2.PART_ID,T2.PART_CODE,T2.PART_OLDCODE,T2.PART_CNAME,'1306',21799,T2.QTY,");
            if (flag == 1) {//如果总成件是入库
                sql4.append(Constant.PART_CODE_RELATION_04);
            } else {//如果总成件是出库
                sql4.append(Constant.PART_CODE_RELATION_09);
            }
            sql4.append(",T1.SPCPD_ID,T2.DTL_ID,T1.ORG_ID,T1.ORG_CODE,T1.ORG_CNAME,T1.WH_ID,T1.WH_CNAME,T2.LOC_ID,T2.LOC_CODE,T2.LOC_CODE,T1.CREATE_DATE,SYSDATE,T1.CREATE_BY,T1.SPCPD_CODE,1");
            sql4.append(" FROM TT_PART_SPCP_IOSTOCK_MAIN T1,TT_PART_SPCP_IOSTOCK_DTL T2 WHERE T1.IOSTOCK_ID=T2.IOSTOCK_ID AND T1.SPCPD_ID=").append(spcpdId);
            sql4.append(" AND T2.IS_MB=").append(Constant.IF_TYPE_YES);

            //向出入库记录表中插入分总成件信息
            StringBuffer sql5 = new StringBuffer("");
            sql5.append("INSERT INTO TT_PART_RECORD(RECORD_ID,ADD_FLAG,PART_ID,PART_CODE,PART_OLDCODE,PART_NAME,PART_BATCH,VENDER_ID,PART_NUM,CONFIG_ID,ORDER_ID,LINE_ID,ORG_ID,ORG_CODE,ORG_NAME,WH_ID,WH_NAME,LOC_ID,LOC_CODE,LOC_NAME,OPT_DATE,CREATE_DATE,PERSON_ID,ORDER_CODE,PART_STATE)");
            sql5.append(" SELECT F_GETID(),");
            if (flag == 1) {//如果总成件是入库,那么与其关联的分总成件就是出库
                sql5.append(2);
                configId = Constant.PART_CODE_RELATION_09;
            } else {
                sql5.append(1);
                configId = Constant.PART_CODE_RELATION_04;
            }
            sql5.append(",T2.PART_ID,T2.PART_CODE,T2.PART_OLDCODE,T2.PART_CNAME,'1306',21799,T2.QTY,");
            sql5.append(configId);
            sql5.append(",T1.SPCPD_ID,T2.DTL_ID,T1.ORG_ID,T1.ORG_CODE,T1.ORG_CNAME,T1.WH_ID,T1.WH_CNAME,T2.LOC_ID,T2.LOC_CODE,T2.LOC_CODE,T1.CREATE_DATE,SYSDATE,T1.CREATE_BY,T1.SPCPD_CODE,1");
            sql5.append(" FROM TT_PART_SPCP_IOSTOCK_MAIN T1,TT_PART_SPCP_IOSTOCK_DTL T2 WHERE T1.IOSTOCK_ID=T2.IOSTOCK_ID AND T1.SPCPD_ID=").append(spcpdId);
            sql5.append(" AND T2.IS_MB=").append(Constant.IF_TYPE_NO);

            insert(sql1.toString());
            insert(sql2.toString());
            insert(sql3.toString());
            insert(sql4.toString());
            insert(sql5.toString());
        } catch (Exception e) {
            throw e;
        }
    }

    public Map getPartSpcpIoStockMainInfo(String spcpdId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.IOSTOCK_ID,T1.SPCPD_CODE,T1.SPCPD_TYPE,decode(T1.SPCPD_TYPE,")
                    .append(Constant.PART_SPCPD_TYPE_01).append(",'拆件',")
                    .append(Constant.PART_SPCPD_TYPE_02).append(",'合件')")
                    .append(" SPCPD_TYPE1,");
            sql.append("T1.ORG_CNAME,T1.WH_CNAME,T3.NAME FROM TT_PART_SPCP_IOSTOCK_MAIN T1,TT_PART_SPCP_MAIN T2,TC_USER T3 WHERE T1.SPCPD_ID=T2.SPCPD_ID AND T2.CREATE_BY=T3.USER_ID AND T1.SPCPD_ID=");
            sql.append(CommonUtils.parseLong(spcpdId));
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> querySpiltInOrOutDetailList(
            String ioStockId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT t.part_id, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.IS_MB, \n");
            sql.append("        T.SPLIT_QTY, \n");
            sql.append("        T.SPLIT_RATE, \n");
            sql.append("        T.NORMAL_QTY, \n");
            sql.append("        T.QTY, \n");
            sql.append("        TO_CHAR((SELECT NVL((SELECT P.SALE_PRICE3 PLAN_PRICE \n");
            sql.append("                             FROM TT_PART_SALES_PRICE P \n");
            sql.append("                            WHERE P.PART_ID = T.PART_ID \n");
            sql.append("                              AND P.STATE = ").append(Constant.STATUS_ENABLE);
            sql.append("                              AND P.STATUS = 1), \n");
            sql.append("                           0) \n");
            sql.append("                  FROM DUAL), \n");
            sql.append("                'fm999,999,990.00') PLAN_PRICE, \n");
            sql.append("        TO_CHAR((SELECT NVL((SELECT P.SALE_PRICE3 PLAN_PRICE \n");
            sql.append("                             FROM TT_PART_SALES_PRICE P \n");
            sql.append("                            WHERE P.PART_ID = T.PART_ID \n");
            sql.append("                              AND P.STATE = ").append(Constant.STATUS_ENABLE);
            sql.append("                              AND P.STATUS = 1), \n");
            sql.append("                           0) * T.QTY \n");
            sql.append("                  FROM DUAL), \n");
            sql.append("                'fm999,999,990.00') AMOUNT, \n");
            sql.append("        T1.LOC_NAME, \n");
            sql.append("        T.CREATE_DATE \n");
            sql.append("   FROM TT_PART_SPCP_IOSTOCK_DTL T, TT_PART_LOACTION_DEFINE T1 \n");
            sql.append("  WHERE T.LOC_ID = T1.LOC_ID \n");
            sql.append("    AND T.IOSTOCK_ID = ").append(ioStockId);
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> querySpiltInOrOutDetail(String ioStockId) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT t.part_id, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.UNIT, \n");
            sql.append("        T.IS_MB, \n");
            sql.append("        T.SPLIT_QTY, \n");
            sql.append("        T.SPLIT_RATE, \n");
            sql.append("        T.NORMAL_QTY, \n");
            sql.append("        T.QTY, \n");
            sql.append("        TO_CHAR((SELECT NVL((SELECT P.SALE_PRICE3 PLAN_PRICE \n");
            sql.append("                             FROM TT_PART_SALES_PRICE P \n");
            sql.append("                            WHERE P.PART_ID = T.PART_ID \n");
            sql.append("                              AND P.STATE = ").append(Constant.STATUS_ENABLE);
            sql.append("                              AND P.STATUS = 1), \n");
            sql.append("                           0) \n");
            sql.append("                  FROM DUAL), \n");
            sql.append("                'fm999,999,990.00') PLAN_PRICE, \n");
            sql.append("        TO_CHAR((SELECT NVL((SELECT P.SALE_PRICE3 PLAN_PRICE \n");
            sql.append("                             FROM TT_PART_SALES_PRICE P \n");
            sql.append("                            WHERE P.PART_ID = T.PART_ID \n");
            sql.append("                              AND P.STATE = ").append(Constant.STATUS_ENABLE);
            sql.append("                              AND P.STATUS = 1), \n");
            sql.append("                           0) * T.QTY \n");
            sql.append("                  FROM DUAL), \n");
            sql.append("                'fm999,999,990.00') AMOUNT, \n");
            sql.append("        T1.LOC_NAME, \n");
            sql.append("        T.CREATE_DATE \n");
            sql.append("   FROM TT_PART_SPCP_IOSTOCK_DTL T, TT_PART_LOACTION_DEFINE T1 \n");
            sql.append("  WHERE T.LOC_ID = T1.LOC_ID \n");
            sql.append("    AND T.IOSTOCK_ID = ").append(ioStockId);
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

}
