package com.infodms.dms.dao.parts.baseManager.partSalePriceQuery;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartSalePriceChangeDao extends BaseDao<PO> {

    public static Logger logger = Logger.getLogger(PartSalePriceChangeDao.class);
    private static final PartSalePriceChangeDao dao = new PartSalePriceChangeDao();

    private PartSalePriceChangeDao() {

    }

    public static final PartSalePriceChangeDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title :
     * @Description: 查询配件销售信息
     */
    public PageResult<Map<String, Object>> queryPartSalePrice(RequestWrapper request, int poseBusType, int curPage, int pageSize) {
        String applyNo = CommonUtils.checkNull(request.getParamValue("APPLY_NO"));// 件号
        String checkSDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 开始时间
        String checkEDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 截止时间
        String state = CommonUtils.checkNull(request.getParamValue("state")); // 截止时间

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.APPLY_NO,\n");
        sql.append("       MAX(C.CREATE_DATE) CREATE_DATE,\n");
        sql.append("       MAX(U.NAME) NAME,\n");
        sql.append("       MAX(C.STATE) STATE,\n");
        sql.append("       MAX(C.REMARK) REMARK,");
        sql.append("	   MAX(C.VALID_FROM) VALID_FROM,\n");
        sql.append("       MAX(C.VALID_TO) VALID_TO");
        sql.append("  FROM TT_PART_SALES_PRICE_CHG C, TC_USER U\n");
        sql.append(" WHERE C.CREATE_BY = U.USER_ID\n");
        sql.append("   AND C.STATUS = 1\n");
        //sql.append("   AND C.STATE = 92801001\n");

        if (null != applyNo && !applyNo.equals("")) {
            sql.append("AND UPPER(C.APPLY_NO) like'%" + applyNo.trim().toUpperCase() + "%'  \n");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(C.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(C.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }
        if (null != state && !"".equals(state)) {
            sql.append(" AND STATE = " + state);
        }
        sql.append(" GROUP BY C.APPLY_NO\n");
        sql.append(" ORDER BY MAX(C.CREATE_DATE) DESC");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title :
     * @Description: 查询配件销售信息
     */
    public PageResult<Map<String, Object>> queryPartSalePriceHis(RequestWrapper request, int curPage, int pageSize) {
        String priceId = CommonUtils.checkNull(request
                .getParamValue("priceId"));// 件号

        StringBuffer sql = new StringBuffer();
        sql.append(" select \n");
        sql.append(" a.PRICE_ID ,\n");// 主键标识
        sql.append(" b.PART_CODE ,\n"); // 件号
        sql.append(" b.PART_OLDCODE ,\n");// 配件编码
        sql.append(" b.PART_CNAME ,\n");// 配件名称
        sql.append(" a.SALE_PRICE1 ,\n");// 调拨价
        sql.append(" a.SALE_PRICE2 ,\n");// 建议零售价
        sql.append(" a.SALE_PRICE3 ,\n");// 计划价
        sql.append(" a.SALE_PRICE4 ,\n");// 团购价
        sql.append(" a.SALE_PRICE5 ,\n");// 价格5
        sql.append(" a.SALE_PRICE6 ,\n");// 价格6
        sql.append(" a.SALE_PRICE7 ,\n");// 价格7
        sql.append(" a.SALE_PRICE8 ,\n");// 价格8
        sql.append(" a.SALE_PRICE9 ,\n");// 价格9
        sql.append(" a.SALE_PRICE10 ,\n");// 价格10
        sql.append(" a.SALE_PRICE11 ,\n");// 价格11
        sql.append(" a.SALE_PRICE12 ,\n");// 价格12
        sql.append(" a.SALE_PRICE13 ,\n");// 价格13
        sql.append(" a.SALE_PRICE14 ,\n");// 领用价
        sql.append(" a.SALE_PRICE15 ,\n");// 价格15
        sql.append(" a.OLD_SALE_PRICE1 ,\n");// 原调拨价
        sql.append(" a.OLD_SALE_PRICE2 ,\n");// 原建议零售价
        sql.append(" a.OLD_SALE_PRICE3 ,\n");// 原原计划价
        sql.append(" a.OLD_SALE_PRICE4 ,\n");// 原团购价
        sql.append(" a.OLD_SALE_PRICE5 ,\n");// 原价格5
        sql.append(" a.OLD_SALE_PRICE6 ,\n");// 原价格6
        sql.append(" a.OLD_SALE_PRICE7 ,\n");// 原价格7
        sql.append(" a.OLD_SALE_PRICE8 ,\n");// 原价格8
        sql.append(" a.OLD_SALE_PRICE9 ,\n");// 原价格9
        sql.append(" a.OLD_SALE_PRICE10 ,\n");// 原价格10
        sql.append(" a.OLD_SALE_PRICE11 ,\n");// 原价格11
        sql.append(" a.OLD_SALE_PRICE12 ,\n");// 原价格12
        sql.append(" a.OLD_SALE_PRICE13 ,\n");// 原价格13
        sql.append(" a.OLD_SALE_PRICE14 ,\n");// 原领用价
        sql.append(" a.OLD_SALE_PRICE15 ,\n");// 原价格15
        sql.append("  A.CREATE_DATE,\n" + "      U.NAME\n"
                + " FROM tt_part_sale_price_history A, TC_USER U, TT_PART_DEFINE B\n"
                + "  WHERE A.PART_ID = B.PART_ID(+)\n"
                + "  AND A.CREATE_BY = U.USER_ID(+)");
        sql.append(" and A.price_Id='").append(priceId).append("'");
        sql.append(" order by a.create_date");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title :
     * @Description: 查询设置列表
     */
    public PageResult<Map<String, Object>> queryPartPriceSetting(
            RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_PRICE_SETTING where 1=1  ORDER BY type_id ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-15
     * @Title : 查询设置列表List
     */
    public List<Map<String, Object>> queryPartPriceSettingList() {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_PRICE_SETTING where 1=1  ORDER BY type_id ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null,
                getFunName());
        return list;
    }

    /**
     * @param : @param request
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title :
     * @Description: 导出数据
     */
    public List<Map<String, Object>> queryPartSalePriceForExport(RequestWrapper request) {

        String chgId = CommonUtils.checkNull(request.getParamValue("chgId"));
        String flag = CommonUtils.checkNull(request.getParamValue("flag"));// 1：价格列表导出 2：申请明细导出

        StringBuffer sql = new StringBuffer();
        if ("1".equals(flag)) {
            sql.append("SELECT B.PART_ID,\n");
            sql.append("       B.PART_CODE,\n");
            sql.append("       B.PART_OLDCODE,\n");
            sql.append("       B.PART_CNAME,\n");
            sql.append("       A.PRICE_ID,\n");
            sql.append("       A.SALE_PRICE1  SVC_PRICE,\n");
            sql.append("       A.SALE_PRICE2  RETAIL_PRICE,\n");
            sql.append("       NULL           SVC_PRICE_C,\n");
            sql.append("       NULL           RETAIL_PRICE_C\n");
            sql.append("  FROM TT_PART_SALES_PRICE A, TT_PART_DEFINE B\n");
            sql.append(" WHERE A.PART_ID = B.PART_ID\n");
            sql.append("   AND B.STATE = 10011001\n");
            sql.append(" ORDER BY PART_OLDCODE\n");
        } else {
            sql.append("SELECT CG.PART_OLDCODE,\n");
            sql.append("       CG.PART_CNAME,\n");
            sql.append("       CG.SVC_PRICE,\n");
            sql.append("       CG.RETAIL_PRICE,\n");
            sql.append("       CG.SVC_PRICE_C,\n");
            sql.append("       CG.RETAIL_PRICE_C\n");
            sql.append("  FROM TT_PART_SALES_PRICE_CHG CG\n");
            sql.append(" WHERE CG.APPLY_NO = '" + chgId + "'\n");
        }
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param code
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-8
     * @Title :
     * @Description: 查询配件PARTID
     */
    public String getPartId(String code) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select b.part_id from TT_PART_SALES_PRICE a, TT_PART_DEFINE b ");
        sql.append(" where 1=1 AND a.PART_ID(+) = b.PART_ID ");
        sql.append(" and UPPER(b.PART_OLDCODE)='").append(code.toUpperCase()).append("'");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null,
                getFunName());
        if (list == null || list.size() <= 0) {
            return "";
        }
        return ((Map) list.get(0)).get("PART_ID").toString();
    }

    /**
     * @param : @param sqlStr
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-8-20
     * @Title : 验证销售单价是否存在
     */
    public List<Map<String, Object>> CheckPriceExist(String sqlStr) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT SP.* "
                        + " FROM TT_PART_SALES_PRICE SP "
                        + " WHERE 1 = 1  ");
        sql.append(sqlStr);

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }

    public PageResult<Map<String, Object>> applyDetail(Map<String, Object> map, int curPage, int pageSize) {

        String applyNO = map.get("APPLY_NO").toString();

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT C.*, U.NAME\n");
        sql.append("  FROM TT_PART_SALES_PRICE_CHG C, TC_USER U\n");
        sql.append(" WHERE C.CREATE_BY = U.USER_ID\n");
        sql.append("   AND C.STATUS = 1\n");
//		sql.append("   AND C.STATE = "+Constant.PART_PRICE_CHG_STATE_01+"\n");
        sql.append("   AND C.APPLY_NO = '" + applyNO + "'");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> partList(Map<String, Object> map, int curPage, int pageSize) {
        String partCname = map.get("partCname").toString();
        String partOldcode = map.get("partOldcode").toString();
        String partCode = map.get("partCode").toString();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT B.PART_ID,\n");
        sql.append("       B.PART_CODE,\n");
        sql.append("       B.PART_OLDCODE,\n");
        sql.append("       B.PART_CNAME,\n");
        sql.append("       A.PRICE_ID,\n");
        sql.append("       A.SALE_PRICE1,\n");
        sql.append("       A.SALE_PRICE2\n");
        sql.append("  FROM TT_PART_SALES_PRICE A, TT_PART_DEFINE B\n");
        sql.append(" WHERE A.PART_ID = B.PART_ID\n");

        if (partCname != null && !partCname.equals("")) {
            sql.append("	AND B.PART_CNAME LIKE '%" + partCname + "%'");
        }
        if (partOldcode != null && !partOldcode.equals("")) {
            sql.append("	AND B.PART_OLDCODE LIKE '%" + partOldcode + "%'");
        }
        if (partCode != null && !partCode.equals("")) {
            sql.append("	AND B.PART_CODE LIKE '%" + partCode + "%'");
        }
        sql.append(" AND B.STATUS = 1\n");
        sql.append(" ORDER BY PART_OLDCODE");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryDealers(RequestWrapper request, int poseBusType, int curPage, int pageSize) {
        String typeId = CommonUtils.checkNull(request.getParamValue("typeId"));//
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//
        StringBuffer sql = new StringBuffer();
        sql.append(" select DEALER_ID from  TT_PART_PRICE_SETTING where type_id='").append(typeId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        sql.setLength(0);
        String ids = "";
        if (null != list && list.size() > 0) {
            ids = CommonUtils.checkNull(list.get(0).get("DEALER_ID"));
        }
        ids = "'" + ids.replaceAll(",", "','") + "'";
        sql.append(" SELECT d.dealer_code,d.dealer_name FROM tm_dealer d WHERE d.dealer_code IN (");
        sql.append(ids).append(")");
        if (!"".equals(dealerName)) {
            sql.append(" and dealer_Name like '%").append(dealerName).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and dealer_code like '%").append(dealerCode).append("%'");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getPrice(String typeId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_PRICE_SETTING where type_id='").append(typeId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public void updateDealer(String typeId, String dealerIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update TT_PART_PRICE_SETTING set dealer_id='").append(dealerIds).append("' where type_id='").append(typeId).append("'");
        this.update(sql.toString(), null);
    }
}
