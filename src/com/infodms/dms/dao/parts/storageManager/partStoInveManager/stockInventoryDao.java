package com.infodms.dms.dao.parts.storageManager.partStoInveManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import javax.xml.registry.infomodel.LocalizedString;

/**
 * @author : huchao
 *         CreateDate     : 2013-5-3
 * @ClassName : stockInventoryDao
 */
public class stockInventoryDao extends BaseDao {
    public static Logger logger = Logger.getLogger(stockInventoryDao.class);
    private static final stockInventoryDao dao = new stockInventoryDao();

    private stockInventoryDao() {
    }

    public static final stockInventoryDao getInstance() {
        return dao;
    }

    private static final int type1 = Constant.PART_STOCK_INVE_TYPE_01;
    private static final int type2 = Constant.PART_STOCK_INVE_TYPE_02;
    private static final int enableValue = Constant.STATUS_ENABLE;//有效


    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 配件库存盘点信息查询
     */
    public PageResult<Map<String, Object>> queryPartStockInve(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.CHANGE_ID, TM.CHANGE_CODE, TM.CHGORG_CNAME, TM.WH_CNAME, U.NAME, TM.CREATE_DATE, TM.STATE, TM.CHECK_TYPE "
                        + "FROM TT_PART_CHECK_MAIN TM, TC_USER U ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID  ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CHANGE_CODE DESC");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回仓库信息List
     */
    public List<Map<String, Object>> getWareHouses(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.WH_ID, TM.WH_NAME AS WH_CNAME "
                        + " FROM TT_PART_WAREHOUSE_DEFINE TM "
                        + " WHERE 1 = 1  ");
        sql.append(" AND TM.STATE = '" + enableValue + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.WH_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param dealerName
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回配件库存盘点信息List
     */
    public List<Map<String, Object>> queryAllPartStockInve(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.CHANGE_ID, TM.CHANGE_CODE, TM.CHGORG_CNAME, TM.WH_ID, TM.WH_CNAME, U.NAME, "
                        + " TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') AS CREATE_DATE, TM.REMARK, TM.CHANGE_ID, TM.STATE, TM.CHECK_TYPE "
                        + " FROM TT_PART_CHECK_MAIN TM, TC_USER U ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CHANGE_CODE DESC");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public List<Map<String, Object>> selectData(String partCode, String orgId) {
        String sql = "SELECT S.* FROM TT_PART_DEFINE D,TT_PART_ITEM_STOCK S WHERE D.PART_ID=S.PART_ID AND D.PART_CODE = '" + partCode + "' AND S.ORG_ID=" + orgId;
        List<Map<String, Object>> list = pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 配件库存盘点详细信息List
     */
    public List<Map<String, Object>> queryPartStockDeatil(int invType, String sbString) {
        StringBuffer sql = new StringBuffer("");
        if (type1 != invType) {
            sql.append("SELECT TM.CHANGE_CODE,TM.WH_CNAME, TD.DTL_ID, TD.PART_ID, TD.PART_CODE, TD.PART_OLDCODE,TD.PART_CNAME, TD.UNIT, TD.REMARK, "
                            + "TD.LOC_ID, TD.LOC_CODE, TD.LOC_NAME, TD.VENDER_ID, TD.BATCH_CODE, VM.ITEM_QTY, VM.NORMAL_QTY, VM.BOOKED_QTY, VM.ZCFC_QTY, VM.FC_QTY, VM.LOC_NAME, VM.PKFC_QTY, VM.PDFC_QTY "
                            + " FROM TT_PART_CHECK_DTL TD, TT_PART_CHECK_MAIN TM, VW_PART_STOCK VM ");
            sql.append(" WHERE 1 = 1 AND TM.CHANGE_ID = TD.CHECK_ID ");
            sql.append(" AND TM.CHGORG_ID = VM.ORG_ID ");
            sql.append(" AND TM.WH_ID = VM.WH_ID ");
            sql.append(" AND TD.PART_ID = VM.PART_ID ");
            sql.append(" AND TD.LOC_ID = VM.LOC_ID ");
        } else {
            sql .append("SELECT TM.CHANGE_CODE,TM.WH_CNAME, VM.PART_ID, VM.PART_CODE, VM.PART_OLDCODE, VM.PART_CNAME, VM.UNIT, "
                            + " VM.ITEM_QTY, VM.NORMAL_QTY, VM.BOOKED_QTY, VM.ZCFC_QTY, VM.FC_QTY,  VM.LOC_NAME "
                            + " FROM TT_PART_CHECK_MAIN TM, VW_PART_STOCK VM ");
            sql.append(" WHERE 1 = 1 ");
            sql.append(" AND TM.CHGORG_ID = VM.ORG_ID ");
            sql.append(" AND TM.WH_ID = VM.WH_ID ");
        }

        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 配件库存盘点详细信息查询
     */
    public PageResult<Map<String, Object>> queryPartStockDeatil(int invType, String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        if (type1 != invType) {
            sql.append("SELECT TM.CHANGE_CODE,TM.WH_CNAME, TD.DTL_ID, TD.PART_ID, TD.PART_CODE, TD.PART_OLDCODE,TD.PART_CNAME, TD.UNIT, TD.REMARK, TD.LOC_NAME, "
                            + " TD.BATCH_CODE, TD.VENDER_ID, VM.ITEM_QTY, VM.NORMAL_QTY, VM.BOOKED_QTY, VM.ZCFC_QTY, VM.FC_QTY, VM.PKFC_QTY, VM.PDFC_QTY "
                            + " FROM TT_PART_CHECK_DTL TD, TT_PART_CHECK_MAIN TM, VW_PART_STOCK VM  ");
            sql.append(" WHERE 1 = 1 AND TM.CHANGE_ID = TD.CHECK_ID ");
//			sql.append(" AND TM.CHGORG_ID = VM.ORG_ID ");
            sql.append(" AND TM.WH_ID = VM.WH_ID ");
            sql.append(" AND TD.PART_ID = VM.PART_ID ");
//			sql.append(" AND TM.CHGORG_ID = LD.ORG_ID ");
            sql.append(" AND VM.LOC_ID = TD.LOC_ID ");
        } else {
            sql
                    .append("SELECT TM.CHANGE_CODE,TM.WH_CNAME, VM.PART_ID, VM.PART_CODE, VM.PART_OLDCODE, VM.PART_CNAME, VM.UNIT, LD.LOC_NAME, "
                            + " VM.ITEM_QTY, VM.NORMAL_QTY, VM.BOOKED_QTY, VM.ZCFC_QTY, VM.FC_QTY "
                            + " FROM TT_PART_CHECK_MAIN TM, VW_PART_STOCK VM , TT_PART_LOACTION_DEFINE LD ");
            sql.append(" WHERE 1 = 1 ");
//			sql.append(" AND TM.CHGORG_ID = VM.ORG_ID ");
            sql.append(" AND TM.WH_ID = VM.WH_ID ");
            sql.append(" AND TM.WH_ID = LD.WH_ID ");
//			sql.append(" AND TM.CHGORG_ID = LD.ORG_ID ");
            sql.append(" AND VM.PART_ID = LD.PART_ID ");

        }
        sql.append(sbString);
        sql.append(" ORDER BY VM.PART_OLDCODE, VM.PART_CNAME, VM.PART_CODE ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param invType
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-27
     * @Title : 获取盘点的配件ID （TT_PART_ITEM_STOCK）
     */
    public List<Map<String, Object>> getPartStockIdsList(int invType, String sbString) {
        StringBuffer sql = new StringBuffer("");
        if (type1 != invType) {
            sql
                    .append("SELECT VM.STOCK_ID, VM.IS_LOCKED "
                            + " FROM TT_PART_CHECK_DTL TD, TT_PART_CHECK_MAIN TM, VW_PART_STOCK VM ");
            sql.append(" WHERE 1 = 1 AND TM.CHANGE_ID = TD.CHECK_ID ");
            sql.append(" AND TM.CHGORG_ID = VM.ORG_ID ");
            sql.append(" AND TM.WH_ID = VM.WH_ID ");
            sql.append(" AND TD.PART_ID = VM.PART_ID ");
        } else {
            sql
                    .append("SELECT VM.STOCK_ID "
                            + " FROM TT_PART_CHECK_MAIN TM, VW_PART_STOCK VM ");
            sql.append(" WHERE 1 = 1 ");
            sql.append(" AND TM.CHGORG_ID = VM.ORG_ID ");
            sql.append(" AND TM.WH_ID = VM.WH_ID ");
        }
        sql.append(sbString);
        sql.append(" ORDER BY VM.PART_OLDCODE, VM.PART_CNAME, VM.PART_CODE ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回仓库配件库存信息
     */
    public PageResult<Map<String, Object>> showPartStockBase(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT TD.*, DL.DEALER_NAME, DL.DEALER_CODE, (TD.BOOKED_QTY + TD.ZCFC_QTY + TD.PKFC_QTY) AS BOOKED_QTY_NEW, ");
        sql.append(" CASE ");
        sql.append(" WHEN TD.ORG_ID = '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append(" (SELECT TO_CHAR(S.SALE_PRICE3, 'FM999,999,990.00') ");
        sql.append(" FROM TT_PART_SALES_PRICE S ");
        sql.append(" WHERE S.PART_ID = TD.PART_ID) ");
        sql.append(" WHEN TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append(" TO_CHAR(PKG_PART.F_GETPRICE(DL.DEALER_ID, TD.PART_ID), 'FM999,999,990.00') ");
        sql.append(" END AS PRICE, ");
        sql.append(" CASE");
        sql.append("       WHEN TD.ORG_ID = '2010010100070674' THEN");
        sql.append("        (SELECT 1");
        sql.append("           FROM TT_PART_PLAN_DEFINE D");
        sql.append("          WHERE D.PART_ID = TD.PART_ID");
        sql.append("            AND D.WH_ID = TD.WH_ID");
        sql.append("            AND D.PLAN_TYPE = 1");
        //sql.append("            AND TD.NORMAL_QTY < (D.SAFETY_STOCK + D.AVG_QTY * 30 / 2))");
        sql.append("            AND TD.NORMAL_QTY < ( D.AVG_QTY * 30 *1.5))");//低于月均销量1.5倍预警
        sql.append("     END AS FLAG");
        sql.append(" FROM VW_PART_STOCK TD, TM_DEALER DL \n");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.ORG_ID = DL.DEALER_ID(+) \n");
        sql.append(sbString);
        sql.append(" ORDER BY TD.PART_OLDCODE \n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> showPartStockBaseAll(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT A.*, \n");
        sql.append("        CASE \n");
        sql.append("          WHEN A.ORG_ID = '2010010100070674' THEN \n");
        sql.append("           (SELECT 1 \n");
        sql.append("              FROM TT_PART_PLAN_DEFINE D \n");
        sql.append("             WHERE D.PART_ID = A.PART_ID \n");
        sql.append("               AND D.WH_ID = A.WH_ID \n");
        sql.append("               AND D.PLAN_TYPE = 1 \n");
        sql.append("               AND A.NORMAL_QTY < (D.AVG_QTY * 30 * 1.5)) \n");
        sql.append("        END AS FLAG \n");
        sql.append("   FROM (SELECT TD.PART_ID, \n");
        sql.append("                TD.PART_CODE, \n");
        sql.append("                TD.PART_OLDCODE, \n");
        sql.append("                TD.PART_CNAME, \n");
        sql.append("                TD.OEM_MIN_PKG, \n");
        sql.append("                TD.UNIT, \n");
        sql.append("                TD.BATCH_NO, \n");
        sql.append("                SUM(TD.NORMAL_QTY) NORMAL_QTY, \n");
        sql.append("                (SUM(TD.BOOKED_QTY) + SUM(TD.ZCFC_QTY) + SUM(TD.PKFC_QTY)) AS BOOKED_QTY_NEW, \n");
        sql.append("                SUM(TD.ITEM_QTY) ITEM_QTY, \n");
        sql.append("                SUM(TD.FC_QTY) FC_QTY, \n");
        sql.append("                TD.ZT_QTY ZT_QTY, \n");
        sql.append("                TD.IS_LOCKED, \n");
        sql.append("                TD.PDSTATE, \n");
        sql.append("                TD.WH_ID, \n");
        sql.append("                TD.WH_NAME, \n");
        sql.append("                TD.REMARK, \n");
        sql.append("                DL.DEALER_NAME, \n");
        sql.append("                DL.DEALER_CODE, \n");
        sql.append("                TD.ORG_ID, \n");
        sql.append("                TD.HALFY_QTY, \n");
        sql.append("                TD.SafetY_QTY, \n");
        sql.append("                TD.MAX_QTY, \n");
        sql.append("                CASE \n");
        sql.append(" WHEN TD.ORG_ID = '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append("                   (SELECT TO_CHAR(S.SALE_PRICE3, 'FM999,999,990.00') \n");
        sql.append("                      FROM TT_PART_SALES_PRICE S \n");
        sql.append("                     WHERE S.PART_ID = TD.PART_ID) \n");
        sql.append(" WHEN TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append("                   TO_CHAR(PKG_PART.F_GETPRICE(DL.DEALER_ID, TD.PART_ID), \n");
        sql.append("                           'FM999,999,990.00') \n");
        sql.append("                END AS PRICE \n");
        sql.append("          \n");
        sql.append("           FROM VW_PART_STOCK TD, TM_DEALER DL \n");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.ORG_ID = DL.DEALER_ID(+) ");
        sql.append(sbString);
        sql.append("          GROUP BY TD.PART_ID, \n");
        sql.append("                   TD.PART_CODE, \n");
        sql.append("                   TD.PART_OLDCODE, \n");
        sql.append("                   TD.PART_CNAME, \n");
        sql.append("                   TD.OEM_MIN_PKG, \n");
        sql.append("                   TD.UNIT, \n");
        sql.append("                   TD.BATCH_NO, \n");
        sql.append("                   TD.IS_LOCKED, \n");
        sql.append("                   TD.PDSTATE, \n");
        sql.append("                   TD.WH_NAME, \n");
        sql.append("                   TD.REMARK, \n");
        sql.append("                   TD.WH_ID, \n");
        sql.append("                   DL.DEALER_ID, \n");
        sql.append("                   DL.DEALER_NAME, \n");
        sql.append("                   DL.DEALER_CODE, \n");
        sql.append("                   TD.HALFY_QTY, \n");
        sql.append("                   TD.SafetY_QTY, \n");
        sql.append("                   TD.MAX_QTY, \n");
        sql.append("                   TD.ORG_ID，TD.ZT_QTY ) A \n");
        sql.append("   ORDER BY　A.PART_OLDCODE\n");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param orgId
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-17
     * @Title : 配件占用资金汇总查询
     */
    public PageResult<Map<String, Object>> showPartGroupAmount(String orgId, String whId, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT NVL(TD.DEALER_CODE, '10000') DEALER_CODE,\n");
        sql.append("       NVL(TD.DEALER_NAME, '本部') DEALER_NAME,\n");
        sql.append("       WD.WH_NAME,\n");
        sql.append("       TO_CHAR(SUM(TS.ITEM_QTY * SP.SALE_PRICE1), 'FM999,999,999,990.00') AS GROUP_AMOUNT,\n");
        sql.append("       TO_CHAR(NVL(SUM(TS.ITEM_QTY), '0'), 'FM999,999,999,990.00') AS GROUP_COUNT\n");
        sql.append("  FROM TT_PART_ITEM_STOCK       TS,\n");
        sql.append("       TT_PART_SALES_PRICE      SP,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE WD,\n");
        sql.append("       TM_DEALER                TD\n");
        sql.append(" WHERE TS.PART_ID = SP.PART_ID\n");
        sql.append("   AND TS.WH_ID = WD.WH_ID\n");
        sql.append("   AND WD.ORG_ID = TD.DEALER_ID(+)\n");
        if (!"".equals(orgId) && null != orgId) {
//            sql.append("   AND TS.ORG_ID = DECODE('" + orgId + "', '" + Constant.OEM_ACTIVITIES + "', TS.ORG_ID, " + orgId + ")\n");
            sql.append("   AND TS.ORG_ID = '" + orgId + "'\n");
        }
        if (!"".equals(whId) && null != whId) {
            sql.append("   AND TS.WH_ID = " + whId + "\n");
        }
        sql.append(" GROUP BY TD.DEALER_CODE, TD.DEALER_NAME, WD.WH_NAME\n");
        sql.append(" ORDER BY TD.DEALER_CODE DESC\n");

      /*  if(Constant.OEM_ACTIVITIES.equals(orgId))
        {
			sql.append(" SELECT substr(TD.PART_OLDCODE,1,1) AS GROUP_TYPE, ");
			sql.append(" TO_CHAR(SUM(TD.ITEM_QTY * SP.SALE_PRICE3),'FM999,999,999,990.00') AS GROUP_AMOUNT, ");
			sql.append(" NVL(SUM(TD.ITEM_QTY),'0') AS GROUP_COUNT ");
			sql.append(" FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE SP ");
			sql.append(" WHERE TD.PART_ID = SP.PART_ID ");
			sql.append(" AND TD.ORG_ID = '" + orgId +"'");
			if(null != groupType && !"".equals(groupType))
			{
				sql.append(" AND UPPER(SUBSTR(TD.PART_OLDCODE,1,1)) = '"+ groupType.trim().toUpperCase() +"'");
			}
			sql.append(" GROUP BY SUBSTR(TD.PART_OLDCODE,1,1) ");
			sql.append(" ORDER BY SUBSTR(TD.PART_OLDCODE,1,1) ");
		}
		else
		{
			sql.append(" SELECT substr(TD.PART_OLDCODE,1,1) AS GROUP_TYPE, ");
			sql.append(" TO_CHAR(NVL(SUM(TD.ITEM_QTY * (PKG_PART.F_GETPRICE('"+ orgId +"', D.PART_ID))),'0'),'FM999,999,999,990.00') AS GROUP_AMOUNT, ");
			sql.append(" NVL(SUM(TD.ITEM_QTY),'0') AS GROUP_COUNT ");
			sql.append(" FROM VW_PART_STOCK TD, TT_PART_DEFINE D ");
			sql.append(" WHERE TD.PART_ID = D.PART_ID ");
			sql.append(" AND TD.ORG_ID = '" + orgId +"'");
			if(null != groupType && !"".equals(groupType))
			{
				sql.append(" AND UPPER(SUBSTR(TD.PART_OLDCODE,1,1)) = '"+ groupType.trim().toUpperCase() +"'");
			}
			sql.append(" GROUP BY SUBSTR(TD.PART_OLDCODE,1,1) ");
			sql.append(" ORDER BY SUBSTR(TD.PART_OLDCODE,1,1) ");
		}*/

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title : 返回盘点封存详情
     */
    public PageResult<Map<String, Object>> showPartPDStockDT(String sbString, String sqlStr, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT FC.* FROM ( ");

        sql.append(" SELECT SM.CHANGE_CODE, ");
        sql.append(" U.NAME, ");
        sql.append(" SD.DTL_ID, ");
        sql.append(" SD.PART_ID, ");
        sql.append(" SD.PART_CODE, ");
        sql.append(" SD.PART_OLDCODE, ");
        sql.append(" SD.PART_CNAME, ");
        sql.append(" SD.CHANGE_REASON, ");
        sql.append(" SD.CHANGE_TYPE, ");
        sql.append(" SD.REMARK, ");
        sql.append(" SD.CREATE_DATE, ");
        sql.append(" NVL(SD.RETURN_QTY,'0') - NVL(SD.COLSE_QTY,'0') AS UN_CLOSE_QTY ");
        sql.append(" FROM TT_PART_CHG_STATE_MAIN SM, TT_PART_CHG_STATE_DTL SD, TC_USER U ");
        sql.append(" WHERE SM.CHANGE_ID = SD.CHANGE_ID ");
        sql.append(" AND SM.STATE = '1' ");
        sql.append(" AND SD.STATUS = '1' ");
        sql.append(" AND SM.CREATE_BY = U.USER_ID(+) ");
        sql.append(sbString);
        sql.append(sqlStr);

        sql.append(" UNION ");

        sql.append(" SELECT BM.BO_CODE AS CHANGE_CODE, ");
        sql.append(" U.NAME, ");
        sql.append(" SD.BOLINE_ID AS DTL_ID, ");
        sql.append(" SD.PART_ID, ");
        sql.append(" SD.PART_CODE, ");
        sql.append(" SD.PART_OLDCODE, ");
        sql.append(" SD.PART_CNAME, ");
        sql.append(" " + Constant.PART_STOCK_STATUS_BUSINESS_TYPE_07 + " AS CHANGE_REASON, ");
        sql.append(" " + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + " AS CHANGE_TYPE, ");
        sql.append(" SD.REMARK, ");
        sql.append(" SD.CREATE_DATE, ");
        sql.append(" NVL(SD.LOC_BO_ODDQTY, SD.BO_QTY) AS UN_CLOSE_QTY ");
        sql.append(" FROM TT_PART_BO_MAIN BM, TT_PART_BO_DTL SD, TT_PART_SO_MAIN SM, TC_USER U ");
        sql.append(" WHERE BM.BO_ID = SD.BO_ID ");
        sql.append(" AND BM.BO_TYPE = '2' ");//现场BO
        sql.append(" AND SD.LOC_STATUS = '1' ");//未处理
        sql.append(" AND BM.LOC_STATE IN ('" + Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_01 + "','" + Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_02 + "') ");
        sql.append(" AND BM.SO_ID = SM.SO_ID ");
        sql.append(" AND BM.CREATE_BY = U.USER_ID ");
        sql.append(sbString);
        sql.append(" ) FC ");
        sql.append(" ORDER BY FC.CREATE_DATE DESC ");


        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> showPartZYStockDT(RequestWrapper request, String OrgId, int pageSize, int curPage) {

        String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
        String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
        String locId = CommonUtils.checkNull(request.getParamValue("locId")); // 仓库ID
        String zyCode = CommonUtils.checkNull(request.getParamValue("zyCode")); // 占用单号
        String zyType = CommonUtils.checkNull(request.getParamValue("zyType")); //占用类型
        String zyCompany = CommonUtils.checkNull(request.getParamValue("zyCompany")); //占用单位
        String fcFlag = CommonUtils.checkNull(request.getParamValue("fcFlag")); //查看封存
//		String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 封存开始时间
//		String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 封存截止时间

        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT ZY.* FROM VW_PART_ZYSTOCK_DTL ZY ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND ZY.WH_ID = '" + whId + "' ");
        sql.append(" AND ZY.PART_ID = '" + partId + "' ");
        if (!"".equals(locId)) {
            sql.append(" AND ZY.LOC_ID = '" + locId + "' ");
        }
        if (null != zyCode && !"".equals(zyCode)) {
            sql.append(" AND UPPER(ZY.BILL_CODE) LIKE '%" + zyCode.trim().toUpperCase() + "%' ");
        }
        if (null != zyCompany && !"".equals(zyCompany)) {
            sql.append(" AND ZY.ORG_NAME LIKE '%" + zyCompany.trim() + "%' ");
        }
        /*if(null != checkSDate && !"".equals(checkSDate))
		{
			sql.append(" AND TO_CHAR(ZY.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
		}
		if(null != checkEDate && !"".equals(checkEDate))
		{
			sql.append(" AND TO_CHAR(ZY.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
		}*/
        if (null != zyType && !"".equals(zyType)) {
            sql.append(" AND ZY.PRV_TYPE LIKE '%" + zyType.trim() + "%' ");
        }
        if ("1".equals(fcFlag)) {
            sql.append(" AND ZY.PRV_TYPE LIKE '%封存%'\n");
        }
        sql.append(" ORDER BY ZY.CREATE_DATE DESC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-11
     * @Title : 返回配件入库详情
     */
    public PageResult<Map<String, Object>> showPartInStockDT(String sbString, String checkCode, String inCode, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT R.* FROM VW_PART_INSTOCK_DTL R ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(R.CHECK_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(R.IN_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }
        sql.append(" ORDER BY R.CREATE_DATE DESC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param checkCode
     * @param : @param inCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-10-14
     * @Title : 入库信息统计查询List
     */
    public List<Map<String, Object>> countPartInStockDT(String sbString, String checkCode, String inCode) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT COUNT(R.RECORD_ID) AS DET_COUNT, SUM(R.PART_NUM) AS IN_QTY FROM VW_PART_INSTOCK_DTL R ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(R.CHECK_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(R.IN_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-12
     * @Title : 返回配件出库详情
     */
    public PageResult<Map<String, Object>> showPartOutStockDT(String sbString, String soCode, String outCode, String dealerName, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT R.* FROM VW_PART_OUTSTOCK_DTL R ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(R.SO_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(R.OUT_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }
        if (null != dealerName && !"".equals(dealerName)) {
            sql.append(" AND R.dealer_Name LIKE '%" + dealerName + "%' ");
        }
        sql.append(" ORDER BY R.CREATE_DATE DESC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param soCode
     * @param : @param outCode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-10-14
     * @Title :  出库信息统计查询List
     */
    public List<Map<String, Object>> countPartOutStockDT(String sbString, String soCode, String outCode) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT COUNT(R.RECORD_ID) AS DET_COUNT, SUM(R.PART_NUM) AS OUT_OTY FROM VW_PART_OUTSTOCK_DTL R ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(R.SO_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(R.OUT_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-4
     * @Title : 返回仓库配件库存信息LIST
     */
    public List<Map<String, Object>> showPartStockBase(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TD.*, DL.DEALER_CODE, DL.DEALER_NAME\n");
        sql.append("  FROM VW_PART_STOCK TD, TM_DEALER DL\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND TD.ORG_ID = DL.DEALER_ID(+)\n");
        sql.append(sbString);
        sql.append(" ORDER BY TD.PART_OLDCODE\n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public List<Map<String, Object>> showPartStockBaseAll(String sbString) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT A.*, \n");
        sql.append("        CASE \n");
        sql.append("          WHEN A.ORG_ID = '2010010100070674' THEN \n");
        sql.append("           (SELECT 1 \n");
        sql.append("              FROM TT_PART_PLAN_DEFINE D \n");
        sql.append("             WHERE D.PART_ID = A.PART_ID \n");
        sql.append("               AND D.WH_ID = A.WH_ID \n");
        sql.append("               AND D.PLAN_TYPE = 1 \n");
        sql.append("               AND A.NORMAL_QTY < (D.AVG_QTY * 30 * 1.5)) \n");
        sql.append("        END AS FLAG \n");
        sql.append("   FROM (SELECT TD.PART_ID, \n");
        sql.append("                TD.PART_CODE, \n");
        sql.append("                TD.PART_OLDCODE, \n");
        sql.append("                TD.PART_CNAME, \n");
        sql.append("                TD.OEM_MIN_PKG, \n");
        sql.append("                TD.UNIT, \n");
        sql.append("                SUM(TD.NORMAL_QTY) NORMAL_QTY, \n");
        sql.append("                (SUM(TD.BOOKED_QTY) + SUM(TD.ZCFC_QTY) + SUM(TD.PKFC_QTY)) AS BOOKED_QTY_NEW, \n");
        sql.append("                SUM(TD.ITEM_QTY) ITEM_QTY, \n");
        sql.append("                SUM(TD.FC_QTY) FC_QTY, \n");
        sql.append("                TD.ZT_QTY ZT_QTY, \n");
        sql.append("                TD.IS_LOCKED, \n");
        sql.append("                TD.PDSTATE, \n");
        sql.append("                TD.WH_ID, \n");
        sql.append("                TD.WH_NAME, \n");
        sql.append("                TD.REMARK, \n");
        sql.append("                DL.DEALER_NAME, \n");
        sql.append("                DL.DEALER_CODE, \n");
        sql.append("                TD.ORG_ID, \n");
        sql.append("                TD.SAFETY_QTY, \n");
        sql.append("                TD.MAX_QTY, \n");
        sql.append("                TD.HALFY_QTY, \n");
        sql.append("                CASE \n");
        sql.append(" WHEN TD.ORG_ID = '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append("                   (SELECT TO_CHAR(S.SALE_PRICE3, 'FM999,999,990.00') \n");
        sql.append("                      FROM TT_PART_SALES_PRICE S \n");
        sql.append("                     WHERE S.PART_ID = TD.PART_ID) \n");
        sql.append(" WHEN TD.ORG_ID != '" + Constant.OEM_ACTIVITIES + "' THEN ");
        sql.append("                   TO_CHAR(PKG_PART.F_GETPRICE(DL.DEALER_ID, TD.PART_ID), \n");
        sql.append("                           'FM999,999,990.00') \n");
        sql.append("                END AS PRICE \n");
        sql.append("          \n");
        sql.append("           FROM VW_PART_STOCK TD, TM_DEALER DL \n");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.ORG_ID = DL.DEALER_ID(+) ");
        sql.append(sbString);
        sql.append("          GROUP BY TD.PART_ID, \n");
        sql.append("                   TD.PART_CODE, \n");
        sql.append("                   TD.PART_OLDCODE, \n");
        sql.append("                   TD.PART_CNAME, \n");
        sql.append("                   TD.OEM_MIN_PKG, \n");
        sql.append("                   TD.UNIT, \n");
        sql.append("                   TD.IS_LOCKED, \n");
        sql.append("                   TD.PDSTATE, \n");
        sql.append("                   TD.WH_NAME, \n");
        sql.append("                   TD.REMARK, \n");
        sql.append("                   TD.WH_ID, \n");
        sql.append("                   DL.DEALER_ID, \n");
        sql.append("                   DL.DEALER_NAME, \n");
        sql.append("                   DL.DEALER_CODE, \n");
        sql.append("                   TD.SAFETY_QTY, \n");
        sql.append("                   TD.MAX_QTY, \n");
        sql.append("                   TD.HALFY_QTY, \n");
        sql.append("                   TD.ORG_ID,TD.ZT_QTY) A \n");
        sql.append("   ORDER　BY　A.PART_OLDCODE\n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param orgId
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-17
     * @Title : 配件占用资金汇总查询
     */
    public List<Map<String, Object>> showPartGroupAmount(String orgId, String groupType) {
        StringBuffer sql = new StringBuffer("");

        if (Constant.OEM_ACTIVITIES.equals(orgId)) {
            sql.append(" SELECT substr(TD.PART_OLDCODE,1,1) AS GROUP_TYPE, ");
            sql.append(" TO_CHAR(NVL(SUM(TD.ITEM_QTY * SP.SALE_PRICE3),'0'),'FM999,999,999,990.00') AS GROUP_AMOUNT, ");
            sql.append(" NVL(SUM(TD.ITEM_QTY),'0') AS GROUP_COUNT ");
            sql.append(" FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE SP ");
            sql.append(" WHERE TD.PART_ID = SP.PART_ID ");
            sql.append(" AND TD.ORG_ID = '" + orgId + "'");
            if (null != groupType && !"".equals(groupType)) {
                sql.append(" AND UPPER(SUBSTR(TD.PART_OLDCODE,1,1)) = '" + groupType.trim().toUpperCase() + "'");
            }
            sql.append(" GROUP BY substr(TD.PART_OLDCODE,1,1) ");
            sql.append(" ORDER BY substr(TD.PART_OLDCODE,1,1) ");
        } else {
            sql.append(" SELECT substr(TD.PART_OLDCODE,1,1) AS GROUP_TYPE, ");
            sql.append(" TO_CHAR(NVL(SUM(TD.ITEM_QTY * (PKG_PART.F_GETPRICE('" + orgId + "', D.PART_ID))),'0'),'FM999,999,999,990.00') AS GROUP_AMOUNT, ");
            sql.append(" NVL(SUM(TD.ITEM_QTY),'0') AS GROUP_COUNT ");
            sql.append(" FROM VW_PART_STOCK TD, TT_PART_DEFINE D ");
            sql.append(" WHERE TD.PART_ID = D.PART_ID ");
            sql.append(" AND TD.ORG_ID = '" + orgId + "'");
            if (null != groupType && !"".equals(groupType)) {
                sql.append(" AND UPPER(SUBSTR(TD.PART_OLDCODE,1,1)) = '" + groupType.trim().toUpperCase() + "'");
            }
            sql.append(" GROUP BY substr(TD.PART_OLDCODE,1,1) ");
            sql.append(" ORDER BY substr(TD.PART_OLDCODE,1,1) ");
        }

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回主机厂名称
     */
    public String getMainCompanyName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.COMPANY_NAME "
                        + " FROM TM_COMPANY TM "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TM.COMPANY_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("COMPANY_NAME").toString();
        }

        return companyName;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 返回服务商名称
     */
    public String getDealerName(String oemCompanyId) {
        String companyName = "";
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TD.DEALER_NAME "
                        + " FROM TM_DEALER TD "
                        + " WHERE 1 = 1  ");

        sql.append("  AND TD.DEALER_ID = '" + oemCompanyId + "' ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        if (list.size() > 0) {
            companyName = list.get(0).get("DEALER_NAME").toString();
        }

        return companyName;
    }

    /**
     * @param : @param delCode
     * @param : @return
     * @return :
     * LastDate    : 2013-5-3
     * @Title : 验证服务商编码是否存在 并返回服务商ID、Name
     * @Description:
     */
    public List checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE FROM TT_PART_DEFINE TD " +
                " WHERE TD.PART_OLDCODE = '" + oldCode + "' ";
        list = (List<Map<String, Object>>) pageQuery(sql, null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title : 返回配件库存状态信息
     */
    public List getPartStockInfos(String oldCode, String parentOrgId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-25
     * @Title : 返回精确的配件库存状态信息
     */
    public List getPartStockInfos(String oldCode, String parentOrgId, String whId, String locCode) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM VW_PART_STOCK TD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode + "' AND TD.ORG_ID = '" + parentOrgId + "' ");
        sql.append(" AND TD.WH_ID = '" + whId + "' ");
        sql.append(" AND TD.LOC_CODE = '" + locCode + "' ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-17
     * @Title : 主机厂库存占用总资金
     */
    public List<Map<String, Object>> getOEMPartsAmount() {
        //modify by yuan 20130804
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT TO_CHAR(nvl(SUM(TD.ITEM_QTY * SP.SALE_PRICE3),0),'FM999,999,999,990.00') AS PARTS_AMOUNT, NVL(SUM(TD.ITEM_QTY),'0') AS ITEM_QTY ");
        sql.append(" FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE SP ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_ID = SP.PART_ID ");
        sql.append(" AND TD.ORG_ID = '" + Constant.OEM_ACTIVITIES + "'");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param delId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-17
     * @Title : 服务商库存占用总资金
     */
    public List<Map<String, Object>> getDLRPartsAmount(String dealerId) {
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT TO_CHAR(NVL(SUM(TD.ITEM_QTY * (PKG_PART.F_GETPRICE(" + dealerId + ", D.PART_ID))),'0'),'FM999,999,999,990.00') AS PARTS_AMOUNT, NVL(SUM(TD.ITEM_QTY),'0') AS ITEM_QTY ");
        sql.append(" FROM VW_PART_STOCK TD, TT_PART_DEFINE D ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_ID = D.PART_ID ");
        sql.append(" AND TD.ORG_ID = '" + dealerId + "'");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * ：zhumingwei
     *
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-9-17
     * @Title : 配件入库详情到处
     */
    public List<Map<String, Object>> showDetailExcel(String sbString, String checkCode, String inCode) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT R.* FROM VW_PART_INSTOCK_DTL R ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

        if (null != checkCode && !"".equals(checkCode)) {
            sql.append(" AND UPPER(R.CHECK_CODE) LIKE '%" + checkCode.toUpperCase() + "%' ");
        }
        if (null != inCode && !"".equals(inCode)) {
            sql.append(" AND UPPER(R.IN_CODE) LIKE '%" + inCode.toUpperCase() + "%' ");
        }
        sql.append(" ORDER BY R.CREATE_DATE DESC ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * ：zhumingwei
     *
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-9-17
     * @Title : 配件出库详情到处
     */
    public List<Map<String, Object>> showSoCodeDetailExcel(String sbString, String soCode, String outCode) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT R.* FROM VW_PART_OUTSTOCK_DTL R ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sbString);

        if (null != soCode && !"".equals(soCode)) {
            sql.append(" AND UPPER(R.SO_CODE) LIKE '%" + soCode.toUpperCase() + "%' ");
        }
        if (null != outCode && !"".equals(outCode)) {
            sql.append(" AND UPPER(R.OUT_CODE) LIKE '%" + outCode.toUpperCase() + "%' ");
        }
        sql.append(" ORDER BY R.CREATE_DATE DESC ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public PageResult<Map<String, Object>> showPartZTStockDT(RequestWrapper request, String OrgId, int pageSize, int curPage) {

        String partId = CommonUtils.checkNull(request.getParamValue("partId")); // 配件ID
        String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 仓库ID
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); //
        String ztType = CommonUtils.checkNull(request.getParamValue("ztType")); // 仓库ID

        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT *\n");
        sql.append("  FROM VW_PART_OEM_PO_ONROAD_DTL VV\n");
        sql.append(" WHERE VV.PART_ID = " + partId + "\n");
        sql.append("   AND VV.WH_ID =" + whId + "\n");
        if (!"".equals(orderCode)) {
            sql.append("AND vv.ORDER_CODE LIKE upper('%").append(orderCode).append("%')\n");
        }
        if (!"".equals(ztType)) {
            sql.append("AND vv.btype ='").append(ztType).append("'");
        }
        sql.append(" ORDER BY VV.CREATE_DATE DESC\n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> showPartRCDtl(String sbString, String orderCode, String outCode, String dealerName, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT R.CREATE_DATE,\n");
        sql.append("       R.IN_TYPE,\n");
        sql.append("       R.CODE,\n");
        sql.append("       TD.DEALER_CODE,\n");
        sql.append("       NVL(TD.DEALER_NAME, R.DEALER_NAME) DEALER_NAME,\n");
        sql.append("       --SIGN((R.OUT_QTY + R.IN_QTY)),\n");
        sql.append("       DECODE(SIGN((R.OUT_QTY + R.IN_QTY)), 1, R.IN_QTY, NULL) IN_QTY,\n");
        sql.append("       DECODE(SIGN((R.OUT_QTY + R.IN_QTY)), -1,ABS(R.OUT_QTY), NULL) OUT_QTY,\n");
        sql.append("       R.ITEM_QTY\n");
        sql.append("  FROM VW_PART_STOCK_HIS R, TM_DEALER TD\n");
        sql.append(" WHERE 1 = 1\n");
        if (!"".equals(orderCode) && null != orderCode) {
            sql.append("   AND R.CODE LIKE '%" + orderCode.toUpperCase() + "%'\n");
        }
        sql.append("   AND R.DEALER_ID = TD.DEALER_ID(+)\n");
        sql.append(sbString);
        sql.append(" ORDER BY R.CREATE_DATE DESC\n");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param sbString
     * @param soCode
     * @param outCode
     * @return
     */
    public List<Map<String, Object>> countPartIOSum(String sbString, String orderCode, String outCode) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT SUM(DECODE(SIGN((R.OUT_QTY + R.IN_QTY)), 1, R.IN_QTY, NULL)) IN_QTY,\n");
        sql.append("       SUM(DECODE(SIGN((R.OUT_QTY + R.IN_QTY)), -1, ABS(R.OUT_QTY), NULL)) OUT_QTY\n");
        sql.append("  FROM VW_PART_STOCK_HIS R, TM_DEALER TD\n");
        sql.append(" WHERE 1 = 1\n");
        if (!"".equals(orderCode) && null != orderCode) {
            sql.append("AND R.CODE LIKE '%" + orderCode.toUpperCase() + "%'\n");
        }
        sql.append("   AND R.DEALER_ID = TD.DEALER_ID(+)\n");
        sql.append(sbString);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
