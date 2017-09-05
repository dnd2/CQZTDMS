package com.infodms.dms.dao.parts.baseManager.partSalePriceQuery;

import com.infodms.dms.common.Constant;
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

public class PartSalePriceDao extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(PartSalePriceDao.class);
    private static final PartSalePriceDao dao = new PartSalePriceDao();

    private PartSalePriceDao() {

    }

    public static final PartSalePriceDao getInstance() {
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
    public PageResult<Map<String, Object>> queryPartSalePrice(
            RequestWrapper request, int poseBusType, int curPage, int pageSize) {
        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));// 件号
        String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));// 配件编码
        String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));// 配件名称
        String isChanghe = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地 艾春9.11添加
        StringBuffer sql = new StringBuffer();
        sql.append(" select \n");
        sql.append(" a.PRICE_ID ,\n");// 主键标识
        sql.append(" b.PART_CODE ,\n"); // 件号
        sql.append(" b.PART_OLDCODE ,\n");// 配件编码
        sql.append(" b.PART_CNAME ,\n");// 配件名称
        sql.append(" b.PART_IS_CHANGHE ,");//结算基地
        sql.append(" TO_CHAR(a.PRICE_VALID_START_DATE, 'YYYY-MM-DD') PRICE_VALID_START_DATE ,\n");// 价格有效开始日期
        sql.append(" TO_CHAR(a.PRICE_VALID_END_DATE, 'YYYY-MM-DD') PRICE_VALID_END_DATE ,\n");// 价格有效截止日期
        sql.append(" TO_CHAR(a.SALE_PRICE_START_DATE, 'YYYY-MM-DD') SALE_PRICE_START_DATE ,");// 促销调拨价有效开始日期
        sql.append(" TO_CHAR(a.SALE_PRICE_END_DATE, 'YYYY-MM-DD') SALE_PRICE_END_DATE ,");// 促销调拨价有效结束日期
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
        sql.append(" a.SALE_PRICE14 ,\n");// 价格14
        sql.append(" a.SALE_PRICE15 ,\n");// 价格15
        sql.append("  A.UPDATE_DATE,\n" + "      U.NAME\n"
                + " FROM TT_PART_SALES_PRICE A, TC_USER U, TT_PART_DEFINE B\n"
                + "  WHERE A.PART_ID = B.PART_ID\n"
                + "  AND A.UPDATE_BY = U.USER_ID(+)");
        // 艾春 10.08 添加职位控制
        if (poseBusType == Constant.POSE_BUS_TYPE_WRD) {
            sql.append(" and b.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
        }
        if (!"".equals(partCode) && partCode != null)
            sql.append(" and UPPER(b.PART_CODE) like '%").append(partCode.toUpperCase()).append("%'");
        if (!"".equals(partOldcode) && partOldcode != null)
            sql.append(" and UPPER(b.PART_OLDCODE) like '%").append(partOldcode.toUpperCase()).append("%'");
        if (!"".equals(partCname) && partCname != null)
            sql.append(" and UPPER(b.PART_CNAME) like '%").append(partCname.toUpperCase()).append("%'");
        if (!"".equals(isChanghe) && isChanghe != null)
            sql.append(" and b.part_is_changhe =").append(Integer.valueOf(isChanghe)); // 结算基地 艾春9.11添加
        sql.append(" and A.status =1");
        sql.append(" order by PART_OLDCODE");
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
        
        sql.append(" TO_CHAR(A.PRICE_VALID_START_DATE, 'YYYY-MM-DD') PRICE_VALID_START_DATE,\n");// 实际调拨价有效开始日期
        sql.append(" TO_CHAR(A.PRICE_VALID_END_DATE, 'YYYY-MM-DD') PRICE_VALID_END_DATE,\n");// 实际调拨价有效结束日期
        sql.append(" TO_CHAR(A.OLD_PRICE_VALID_START_DATE, 'YYYY-MM-DD') OLD_PRICE_VALID_START_DATE,\n");// 旧实际调拨价有效开始日期
        sql.append(" TO_CHAR(A.OLD_PRICE_VALID_END_DATE, 'YYYY-MM-DD') OLD_PRICE_VALID_END_DATE,\n");// 旧实际调拨价有效开始日期
        sql.append(" TO_CHAR(A.SALE_PRICE_START_DATE, 'YYYY-MM-DD') SALE_PRICE_START_DATE,\n");// 促销调拨价有效开始日期
        sql.append(" TO_CHAR(A.SALE_PRICE_END_DATE, 'YYYY-MM-DD') SALE_PRICE_END_DATE,\n");// 促销调拨价有效结束日期
        sql.append(" TO_CHAR(A.OLD_SALE_PRICE_START_DATE, 'YYYY-MM-DD') OLD_SALE_PRICE_START_DATE,\n");// 旧信促销调拨价有效开始日期
        sql.append(" TO_CHAR(A.OLD_SALE_PRICE_END_DATE, 'YYYY-MM-DD') OLD_SALE_PRICE_END_DATE,\n");// 旧促销调拨价有效开始日期
        
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
    public List<Map<String, Object>> queryPartSalePriceForExport(
            RequestWrapper request) {
        String partCode = CommonUtils.checkNull(request
                .getParamValue("PART_CODE"));// 件号
        String partOldcode = CommonUtils.checkNull(request
                .getParamValue("PART_OLDCODE"));// 配件编码
        String partCname = CommonUtils.checkNull(request
                .getParamValue("PART_CNAME"));// 配件名称
        String isChanghe = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地 艾春9.11添加
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" a.PRICE_ID ,");// 主键标识
        sql.append(" b.PART_CODE ,"); // 件号
        sql.append(" b.PART_OLDCODE ,");// 配件编码
        sql.append(" b.PART_CNAME ,");// 配件名称
        sql.append(" TO_CHAR(a.PRICE_VALID_START_DATE, 'YYYY-MM-DD') PRICE_VALID_START_DATE ,");// 实际调拨价有效开始日期
        sql.append(" TO_CHAR(a.PRICE_VALID_END_DATE, 'YYYY-MM-DD') PRICE_VALID_END_DATE ,");// 实际调拨价有效结束日期
        sql.append(" TO_CHAR(a.SALE_PRICE_START_DATE, 'YYYY-MM-DD') SALE_PRICE_START_DATE ,");// 促销调拨价有效开始日期
        sql.append(" TO_CHAR(a.SALE_PRICE_END_DATE, 'YYYY-MM-DD') SALE_PRICE_END_DATE ,");// 促销调拨价有效结束日期
//		sql.append(" decode(B.PART_IS_CHANGHE, "+ Constant.PART_IS_CHANGHE_01 + ",'昌河',"+ Constant.PART_IS_CHANGHE_02 +",'东安') PART_IS_CHANGHE, ");
        sql.append(" a.SALE_PRICE1 ,");// 调拨价
        sql.append(" a.SALE_PRICE2 ,");// 建议零售价
        sql.append(" a.SALE_PRICE3 ,");// 计划价
        sql.append(" a.SALE_PRICE4 ,");// 团购价
        sql.append(" a.SALE_PRICE5 ,");// 价格5
        sql.append(" a.SALE_PRICE6 ,");// 价格6
        sql.append(" a.SALE_PRICE7 ,");// 价格7
        sql.append(" a.SALE_PRICE8 ,");// 价格8
        sql.append(" a.SALE_PRICE9 ,");// 价格9
        sql.append(" a.SALE_PRICE10 ,");// 价格10
        sql.append(" a.SALE_PRICE11 ,");// 价格11
        sql.append(" a.SALE_PRICE12 ,");// 价格12
        sql.append(" a.SALE_PRICE13 ,");// 价格13
        sql.append(" a.SALE_PRICE14 ,");// 价格14
        sql.append(" a.SALE_PRICE15 ");// 价格15
        sql.append(" FROM TT_PART_SALES_PRICE A, TT_PART_DEFINE B\n");
        sql.append(" WHERE A.PART_ID = B.PART_ID\n");
        if (!"".equals(partCode))
            sql.append(" and UPPER(b.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
        if (!"".equals(partOldcode))
            sql.append(" and UPPER(b.PART_OLDCODE) LIKE '%").append(partOldcode.toUpperCase()).append("%'");
        if (!"".equals(partCname))
            sql.append(" and UPPER(b.PART_CNAME) LIKE '%").append(partCname.toUpperCase()).append("%'");
        if (!"".equals(isChanghe) && isChanghe != null)
            sql.append(" and b.part_is_changhe =").append(Integer.valueOf(isChanghe)); // 结算基地 艾春9.11添加
        sql.append(" and A.status =1");
        sql.append(" order by B.PART_OLDCODE");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null,
                getFunName());
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
