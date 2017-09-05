package com.infodms.dms.dao.parts.salesManager.partResaleReceiveManager;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.OrderCodeManager;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-4-22
 * @ClassName : partResRecDao
 */
public class partResRecDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partResRecDao.class);
    private static final partResRecDao dao = new partResRecDao();

    private partResRecDao() {
    }

    public static final partResRecDao getInstance() {
        return dao;
    }

    private static final Integer bzType = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06;
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
     * @throws : LastDate    : 2013-4-22
     * @Title : 配件零售/领用单信息查询
     */
    public PageResult<Map<String, Object>> queryPartSaleOrders(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.RETAIL_ID, TM.RETAIL_CODE, TM.SORG_CNAME, TM.CHG_TYPE, TM.WH_CNAME, U.NAME, TM.CREATE_DATE, TM.STATE, "
                        + " TO_CHAR(TDD.AMOUNTS,'fm999,999,999,990.00') AS AMOUNTS, TDD.OUT_QTYS "
                        + " FROM TT_PART_RETAIL_MAIN TM, TC_USER U, (SELECT TD.RETAL_ID, NVL(SUM(TD.SALE_AMOUNT),'0.00') AS AMOUNTS, SUM(NVL(TD.OUT_QTY, 0)) AS OUT_QTYS FROM TT_PART_RETAIL_DTL TD GROUP BY TD.RETAL_ID ) TDD ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID ");
        sql.append(" AND TM.RETAIL_ID = TDD.RETAL_ID(+)");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CREATE_DATE DESC");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-22
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
     * @throws : LastDate    : 2013-4-27
     * @Title : 返回配件零售/领用单信息List
     */
    public List<Map<String, Object>> queryAllPartSaleOrders(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.RETAIL_ID, TM.RETAIL_CODE, TM.SORG_CNAME, TM.CHG_TYPE, TM.WH_ID, TM.WH_CNAME, U.NAME, TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') AS CREATE_DATE, TM.STATE, "
                        + " TO_CHAR(TDD.AMOUNTS,'fm999,999,999,990.00') AS AMOUNTS, TM.LINKMAN, TM.TEL, TM.PURPOSE, TM.REMARK, TDD.OUT_QTYS "
                        + " FROM TT_PART_RETAIL_MAIN TM, TC_USER U, (SELECT TD.RETAL_ID, NVL(SUM(TD.SALE_AMOUNT),'0.00') AS AMOUNTS, SUM(NVL(TD.OUT_QTY, 0)) AS OUT_QTYS FROM TT_PART_RETAIL_DTL TD GROUP BY TD.RETAL_ID ) TDD ");
        sql.append(" WHERE TM.CREATE_BY = U.USER_ID ");
        sql.append(" AND TM.RETAIL_ID = TDD.RETAL_ID(+)");
        sql.append(sbString);
        sql.append(" ORDER BY TM.CREATE_DATE DESC");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-22
     * @Title : 配件零售/领用单详细信息查询
     */
    public PageResult<Map<String, Object>> queryPartSaleOrderDeatil(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TD.RETAL_ID,"
                + " TD.DTL_ID,"
                + " TD.PART_ID,"
                + " TD.PART_CODE,"
                + " TD.PART_OLDCODE,"
                + " TD.PART_CNAME,"
                + " TD.SALE_PRICE,"
                + " TO_CHAR(TD.SALE_AMOUNT,'fm999,999,999,990.00') AS SALE_AMOUNT,"
                + " VM.NORMAL_QTY AS STOCK_QTY,"
                + " VM.ITEM_QTY,"
                + " VM.BOOKED_QTY,"
                + " VM.LOC_ID,"
                + " VM.LOC_CODE,"
                + " VM.LOC_NAME,"
                + " TD.BATCH_NO,"
                + " TD.UNIT,"
                + " TD.QTY,"
                + " TD.REMARK,"
                + " TD.OUT_QTY,"
                + " (TD.QTY - TD.OUT_QTY) OUTABLE_QTY"
                + " FROM TT_PART_RETAIL_DTL TD, TT_PART_RETAIL_MAIN TM, VW_PART_STOCK VM"
                + " WHERE TD.RETAL_ID = TM.RETAIL_ID"
                + " AND TM.SORG_ID = VM.ORG_ID"
                + " AND TD.PART_ID = VM.PART_ID"
                + " AND TD.LOC_ID = VM.LOC_ID"
                + " AND TD.BATCH_NO = VM.BATCH_NO"
                + " AND TM.WH_ID = VM.WH_ID ");
        sql.append(sbString);

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
     * @throws : LastDate    : 2013-5-2
     * @Title : 配件零售/领用单详细信息List
     */
    public List<Map<String, Object>> queryPartSaleOrderDeatilList(String sbString) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TD.RETAL_ID,"
                + " TD.PART_ID,"
                + " TD.PART_CODE,"
                + " TD.PART_OLDCODE,"
                + " TD.PART_CNAME,"
                + " TO_CHAR(NVL(TD.SALE_PRICE,'0.00'),'fm999,990.00') AS SALE_PRICE,"
                + " TO_CHAR(NVL(TD.SALE_AMOUNT,'0.00'),'fm999,999,999,990.00') AS SALE_AMOUNT,"
                + " VM.NORMAL_QTY AS STOCK_QTY,"
                + " VM.LOC_ID, "
                + " VM.LOC_CODE, "
                + " VM.LOC_NAME, "
                + " VM.BATCH_NO, "
                + " TD.UNIT,"
                + " NVL(TD.QTY,'0') AS QTY,"
                + " TD.REMARK,"
                + " TD.OUT_QTY,"
                + " (TD.QTY - TD.OUT_QTY) OUTABLE_QTY"
                + " FROM TT_PART_RETAIL_DTL TD, TT_PART_RETAIL_MAIN TM, VW_PART_STOCK VM"
                + " WHERE TD.RETAL_ID = TM.RETAIL_ID"
                + " AND TM.SORG_ID = VM.ORG_ID"
                + " AND TD.PART_ID = VM.PART_ID"
                + " AND TD.LOC_ID = VM.LOC_ID"
                + " AND TD.BATCH_NO = VM.BATCH_NO"
                + " AND TM.WH_ID = VM.WH_ID ");
        sql.append(sbString);

        list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-23
     * @Title : 返回仓库配件库存信息
     */
    public PageResult<Map<String, Object>> showPartStockBase(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT TD.*, TO_CHAR(NVL(P.SALE_PRICE1,'0'),'fm999,999,999,990.00') AS  REC_PRICE, ");
        sql.append(" TO_CHAR(NVL(P.SALE_PRICE2,'0'),'fm999,999,999,990.00') AS  RES_PRICE ");
        sql.append(" FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE P ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_ID = P.PART_ID ");
        sql.append(sbString);
//		sql.append(" AND TD.ITEM_QTY > 0 ");
        sql.append(" ORDER BY TD.PART_OLDCODE");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param oemCompanyId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-23
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
     * @throws : LastDate    : 2013-4-23
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
     * LastDate    : 2013-4-15
     * @Title : 验证服务商编码是否存在 并返回服务商ID、Name
     * @Description:
     */
    public List<Map<String, Object>> checkOldCode(String oldCode) {
        List<Map<String, Object>> list = null;
        String sql = "SELECT TD.PART_ID,TD.PART_CODE FROM TT_PART_DEFINE TD " +
                " WHERE  TD.PART_OLDCODE = '" + oldCode + "' ";
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
    public List<Map<String, Object>> getPartStockInfos(String oldCode, String parentOrgId) {
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
    public List<Map<String, Object>> getPartStockInfos(String oldCode, String parentOrgId, String whId, String locId, String batchNo) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TD.*, SP.SALE_PRICE2 AS RES_PRICE, SP.SALE_PRICE1 AS REC_PRICE FROM VW_PART_STOCK TD, TT_PART_SALES_PRICE SP ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND TD.PART_OLDCODE = '" + oldCode +"'");
        sql.append(" AND TD.ORG_ID = '" + parentOrgId + "'");
        sql.append(" AND TD.WH_ID = '" + whId + "' ");
        sql.append(" AND TD.PART_ID = SP.PART_ID ");
        sql.append(" AND TD.LOC_ID = '"+locId+"' ");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }
    
    /**
     * <p>
     * Description: 根据条件查询配件可用库存信息
     * </p>
     * 
     * @param paramMap 查询参数
     * @return
     */
    public List<Map<String, Object>> getPartStockList(Map<String, String> paramMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T1.BOOK_ID, T1.NORMAL_QTY, T1.BOOKED_QTY\n");
        sql.append("  FROM TT_PART_BOOK T1\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND T1.PART_ID = '" + paramMap.get("partId") + "'\n");
        sql.append("   AND T1.LOC_ID = '" + paramMap.get("locId") + "'\n");
        sql.append("   AND T1.BATCH_NO = '" + paramMap.get("batchNo") + "'\n");
        sql.append("   AND T1.WH_ID = '" + paramMap.get("whId") + "'\n");
        sql.append("   AND T1.ORG_ID = '" + paramMap.get("orgId") + "'\n");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-2
     * @Title : 返回当前已出库、可用库存的数量
     */
    public List<Map<String, Object>> getLatestQtys(String retailId) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT TD.DTL_ID,\n");
        sql.append("       TD.RETAL_ID,\n");
        sql.append("       TD.LINE_NO,\n");
        sql.append("       TD.PART_ID,\n");
        sql.append("       TD.PART_OLDCODE,\n");
        sql.append("       TD.PART_CNAME,\n");
        sql.append("       TD.PART_CODE,\n");
        sql.append("       TD.LOC_ID,\n");
        sql.append("       TD.LOC_CODE,\n");
        sql.append("       TD.LOC_NAME,\n");
        sql.append("       TD.BATCH_NO,\n");
        sql.append("       TM.WH_ID,\n");
        sql.append("       TW.WH_CODE,\n");
        sql.append("       TM.WH_CNAME,\n");
        sql.append("       TM.SORG_ID,\n");
        sql.append("       TB.NORMAL_QTY AS STOCK_QTY,\n");
        sql.append("       TB.BOOKED_QTY,\n");
        sql.append("       TIS.IS_LOCKED,\n");
        sql.append("       TD.QTY,\n");
        sql.append("       TD.OUT_QTY,\n");
        sql.append("       TD.VER\n");
        sql.append("  FROM TT_PART_RETAIL_DTL TD, TT_PART_RETAIL_MAIN TM, TT_PART_BOOK TB, TT_PART_ITEM_STOCK TIS, TT_PART_WAREHOUSE_DEFINE TW\n");
        sql.append(" WHERE TD.RETAL_ID = TM.RETAIL_ID\n");
        sql.append("   AND TM.WH_ID = TW.WH_ID\n");
        sql.append("   AND TM.SORG_ID = TB.ORG_ID\n");
        sql.append("   AND TD.PART_ID = TB.PART_ID\n");
        sql.append("   AND TD.LOC_ID = TB.LOC_ID\n");
        sql.append("   AND TD.BATCH_NO = TB.BATCH_NO\n");
        sql.append("   AND TM.WH_ID = TB.WH_ID\n");
        sql.append("   AND TIS.ORG_ID = TB.ORG_ID\n");
        sql.append("   AND TIS.PART_ID = TB.PART_ID\n");
        sql.append("   AND TIS.LOC_ID = TB.LOC_ID\n");
        sql.append("   AND TIS.BATCH_CODE = TB.BATCH_NO\n");
        sql.append("   AND TIS.WH_ID = TB.WH_ID\n");
        sql.append("   AND TIS.STATE = 1\n");
        sql.append("   AND TIS.STATUS = 1\n");
        sql.append("   AND RETAL_ID = '"+retailId+"'\n");
        list = (List<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param oldCode
     * @param : @param parentOrgId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-5-8
     * @Title : 返回仓库、货位信息
     */
    public List<Map<String, Object>> getWareLocaInfos(String whId, String orgId, String partId, String locId) {

        List<Map<String, Object>> list = null;
        /*StringBuffer sql = new StringBuffer("");

		if(Constant.OEM_ACTIVITIES.equals(orgId))
		{
			sql.append("SELECT WD.WH_ID,"
					+" WD.WH_CODE,"
					+" WD.WH_NAME,"
					+" LD.LOC_ID,"
					+" LD.LOC_CODE,"
					+" LD.LOC_NAME"
					+" FROM TT_PART_WAREHOUSE_DEFINE WD, TT_PART_LOACTION_DEFINE LD"
					+" WHERE WD.WH_ID = LD.WH_ID"
					+" AND WD.STATE = '" + enableValue + "' "
					+" AND WD.ORG_ID = LD.ORG_ID ");
			sql.append(sbString);
		}
		else
		{
			TmDealerPO tmDealerPO = new TmDealerPO();
	        tmDealerPO.setDealerId(new Long(orgId));
	        TmDealerPO dealerPO = (TmDealerPO)this.select(tmDealerPO).get(0);

	        if(dealerPO.getPdealerType() == Constant.PART_SALE_PRICE_DEALER_TYPE_01) {
			sql.append("SELECT WD.WH_ID,"
					+" WD.WH_CODE,"
					+" WD.WH_NAME,"
					+" LD.LOC_ID,"
					+" LD.LOC_CODE,"
					+" LD.LOC_NAME"
					+" FROM TT_PART_WAREHOUSE_DEFINE WD, TT_PART_LOACTION_DEFINE LD"
					+" WHERE WD.WH_ID = LD.WH_ID"
					+" AND WD.STATE = '" + enableValue + "' "
					+" AND WD.ORG_ID = LD.ORG_ID ");
			sql.append(sbString);
	        } else {

	            sql.append("SELECT WD.WH_ID,");
	            sql.append("      WD.WH_CODE,");
	            sql.append("      WD.WH_NAME,");
	            sql.append("      LD.LOC_ID,");
	            sql.append("      LD.LOC_CODE,");
	            sql.append("      LD.LOC_NAME ");
	            sql.append(" FROM TT_PART_WAREHOUSE_DEFINE WD, TT_PART_LOACTION_DEFINE LD\n");
	            sql.append(" WHERE wd.state= "+Constant.STATUS_ENABLE);
	            sql.append(" AND wd.status=1 ");
	            sql.append(" AND wd.org_id="+orgId);
	            sql.append(" AND ld.wh_id=99999 ");
	            sql.append(" AND ld.part_id= "+ partId);

	        }
		}
        
		list = pageQuery(sql.toString(), null, getFunName());*/

//        Long locId = OrderCodeManager.getPartLocId(orgId, whId, partId);
        StringBuffer sql = new StringBuffer();
        sql.append("  select * from TT_PART_LOACTION_DEFINE where loc_id ='").append(locId).append("'");
        list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public List<Map<String, Object>> getWarehouseInfos(String sqlStr) {

        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT WD.* FROM TT_PART_WAREHOUSE_DEFINE WD ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);

        list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }
}
