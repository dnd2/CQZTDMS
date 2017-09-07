package com.infodms.dms.dao.parts.baseManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartFixcodeDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.object.SqlAndParams;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.tool.TypeHierarchyPrinter;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartBaseQueryDao extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(PartinfoDao.class);
    private static final PartBaseQueryDao dao = new PartBaseQueryDao();

    private PartBaseQueryDao() {
    }

    public static final PartBaseQueryDao getInstance() {
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
     * @throws : LastDate    : 2013-4-2
     * @Title : 查询配件主数据
     * @Description: TODO
     */
    public String queryPartBaseSql(RequestWrapper request, int poseBusType) {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
        String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
        String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
        String isPermanent = CommonUtils.checkNull(request.getParamValue("IS_PERMANENT"));//是否常备
        String mobility = CommonUtils.checkNull(request.getParamValue("MOBILITY"));//流动性
        String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
        String partMaterial = CommonUtils.checkNull(request.getParamValue("PART_MATERIAL"));//配件属性
        String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
        String isReplaced = CommonUtils.checkNull(request.getParamValue("IS_REPLACED"));//是否可替代
        String buyState = CommonUtils.checkNull(request.getParamValue("BUY_STATE"));//采购状态
        String buyerId = CommonUtils.checkNull(request.getParamValue("BUYER_ID"));//采购员
        String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否有效
        String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));//结算基地
        String cartType = CommonUtils.checkNull(request.getParamValue("cartType"));//车型
        String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注
        StringBuffer sql = new StringBuffer();

//        sql.append(" select a.PART_ID,a.PART_CODE,a.PART_OLDCODE,a.PART_CNAME,a.PART_TYPE, ");
//        sql.append(" a.MOBILITY,a.unit,a.BUY_STATE,a.MIN_PACK1,a.MIN_PACK2, a.CREATE_DATE, ");
//        sql.append(" A.PKG_SIZE, A.OEM_MIN_PKG, A.MIN_PKG, A.REMARK, ");
//        sql.append(" a.IS_REPLACED,a.IS_PERMANENT,C.Name PLANER_ID,B.Name BUYER_ID,a.state,");
//        sql.append(" A.MODEL_NAME, A.SERIES_NAME, A.PART_IS_CHANGHE, ");//艾春9.11添加
//        sql.append(" (SELECT SP.SALE_PRICE1\n");
//        sql.append("   FROM TT_PART_SALES_PRICE SP\n");
//        sql.append("  WHERE SP.PART_ID = A.PART_ID) SALE_PRICE1\n");
        List<TcCodePO> flagList = CodeDict.dictMap.get(Constant.PART_BASE_FLAG.toString());  //配件中所有是否
        List<TcCodePO> stateList = CodeDict.dictMap.get(Constant.STATUS.toString());// 状态类型 有效、无效
        List<TcCodePO> partTypeList = CodeDict.dictMap.get(Constant.PART_PRODUCE_STATE.toString()); //配件主数据维护-配件种类-9263
        List<TcCodePO> partFitList = CodeDict.dictMap.get(Constant.ZT_PB_PART_FIT.toString()); //配件主数据维护-装配-9570
        List<TcCodePO> partCategoryList = CodeDict.dictMap.get(Constant.ZT_PB_PART_CATEGORY.toString()); //配件主数据维护-配件类别-9571

        String partTypeDecode = this.loadDecodeSql2(partTypeList, "A.PRODUCE_STATE"); // 配件种类
        String isProtocolPackDecode = this.loadDecodeSql2(flagList, "A.IS_PROTOCOL_PACK"); // 是否协议包装
        String isMagBatchDecode = this.loadDecodeSql2(flagList, "A.IS_MAG_BATCH"); // 是否批次管理
        String isPartDisable = this.loadDecodeSql2(flagList, "A.IS_PART_DISABLE"); // 是否停用
        String isReplacedDecode = this.loadDecodeSql2(flagList, "A.IS_PART_DISABLE"); // 是否替换
        String stateDecode = this.loadDecodeSql2(stateList, "A.STATE"); // 状态，有效or无效
        String isSaleDisable = this.loadDecodeSql2(flagList, "A.IS_SALE_DISABLE"); // 是否售完停用
        String isStopLoad = this.loadDecodeSql2(flagList, "A.IS_STOP_LOAD"); //  是否停止装车
        String partFitDecode = this.loadDecodeSql2(partFitList, "A.PART_FIT"); // 装配
        String partCategoryDecode = this.loadDecodeSql2(partCategoryList, "A.PART_CATEGORY"); // 配件类别
        String cccFlagDecode = this.loadDecodeSql2(flagList, "A.CCC_FLAG"); // 是否3C标识
        String isEntrusrPackDecode = this.loadDecodeSql2(flagList, "A.IS_ENTRUSR_PACK"); // 是否委托发货

        sql.append("SELECT A.PART_ID,\n");
        sql.append("       A.PART_CODE,\n");
        sql.append("       A.PART_OLDCODE,\n");
        sql.append("       A.PART_CNAME,\n");
        sql.append("       A.PART_ENAME,\n");
        sql.append("       A.PRODUCE_FAC,\n");
        sql.append("       A.VEHICLE_VOLUME,\n");
        sql.append("       A.PART_TYPE,\n");
        sql.append("       A.PRODUCE_STATE,\n");
        sql.append("       A.MODEL_CODE,\n");
        sql.append("       "+partTypeDecode+" PART_TYPE_DESC,\n");
        sql.append("       A.UNIT,\n");
        sql.append("       A.MODEL_NAME,\n");
        sql.append("       "+isProtocolPackDecode+" IS_PROTOCOL_PACK_DESC,\n");
        sql.append("       A.IS_PROTOCOL_PACK,\n");
        sql.append("       "+isMagBatchDecode+" IS_MAG_BATCH_DESC,\n");
        sql.append("       A.IS_MAG_BATCH,\n");
        
        sql.append("       A.IS_PART_DISABLE,\n");
        sql.append("       "+isPartDisable+" IS_PART_DISABLE_DESC,\n");
        sql.append("       TO_CHAR(A.PART_DISABLE_DATE, 'YYYY-MM-DD') PART_DISABLE_DATE,\n");
        
        sql.append("       A.IS_SALE_DISABLE,\n");
        sql.append("       "+isSaleDisable+" IS_SALE_DISABLE_DESC,\n");
        sql.append("       TO_CHAR(A.SALE_DISABLE_DATE, 'YYYY-MM-DD') SALE_DISABLE_DATE,\n");
        
        sql.append("       A.IS_STOP_LOAD,\n");
        sql.append("       "+isStopLoad+" IS_STOP_LOAD_DESC,\n");
        sql.append("       TO_CHAR(A.STOP_LOAD_DATE, 'YYYY-MM-DD') STOP_LOAD_DATE,\n");

        sql.append("       "+partFitDecode+" PART_FIT_DESC,\n");
        sql.append("       A.PART_FIT,\n");
        sql.append("       "+partCategoryDecode+" PART_CATEGORY_DESC,\n");
        sql.append("       A.PART_CATEGORY,\n");
        sql.append("       TO_CHAR(A.FIRST_WARNHOUSE_DATE, 'YYYY-MM-DD HH:MI:SS') FIRST_WARNHOUSE_DATE, "); 
        sql.append("       "+cccFlagDecode+" CCC_FLAG_DESC,\n");
        sql.append("       A.CCC_FLAG, ");
        sql.append("       "+isEntrusrPackDecode+" IS_ENTRUSR_PACK_DESC,\n");
        sql.append("       A.IS_ENTRUSR_PACK, ");

        sql.append("       A.PACK_SPECIFICATION, "); 
        sql.append("       A.LENGTH, "); 
        sql.append("       A.WIDTH, "); 
        sql.append("       A.HEIGHT, "); 
        sql.append("       A.VOLUME, "); 
        sql.append("       A.WEIGHT, "); 
        sql.append("       A.MIN_SALE,\n");
        sql.append("       A.MAX_SALE_VOLUME,\n");
        sql.append("       A.MIN_PURCHASE,\n");
        sql.append("       "+isReplacedDecode+" IS_REPLACED_DESC,\n");
        sql.append("       A.IS_REPLACED,\n");
        sql.append("       "+stateDecode+" STATE_DESC,\n");
        sql.append("       A.STATE,\n");
        sql.append("       A.REMARK,\n");
        sql.append("       C.NAME         PLANER_ID_USER,\n");
        sql.append("       B.NAME         BUYER_ID_USER\n");
//        sql.append("       SP.SALE_PRICE1,\n");
//        sql.append("       SP.SALE_PRICE2\n");
//        sql.append("       SP.SALE_PRICE1,\n"); // 实际代理价
//        sql.append("       SP.SALE_PRICE2,\n"); // 实际调拨价
//        sql.append("       SP.SALE_PRICE3,\n"); // 实际零售价
//        sql.append("       SP.SALE_PRICE4,\n"); // 促销价
//        sql.append("       SP.SALE_PRICE5,\n"); // 促销代理价
//        sql.append("       SP.SALE_PRICE6,\n"); // 促销调拨价
//        sql.append("       SP.SALE_PRICE7,\n"); // 促销零售价
//        sql.append("       TO_CHAR(SP.PRICE_VALID_START_DATE, 'YYYY-MM-DD HH:MI:SS') PRICE_VALID_START_DATE,\n"); // 价格有效开始日期
//        sql.append("       TO_CHAR(SP.PRICE_VALID_END_DATE, 'YYYY-MM-DD HH:MI:SS') PRICE_VALID_END_DATE\n"); // 价格有效截止日期
//        sql.append("       BP.BUY_PRICE \n"); // 暂估价
        sql.append("  FROM TT_PART_DEFINE A\n");
//        sql.append("  JOIN TT_PART_SALES_PRICE SP\n");
//        sql.append("    ON SP.PART_ID = A.PART_ID\n");
//        sql.append("  LEFT JOIN TT_PART_BUY_PRICE BP");
//        sql.append("    ON BP.PART_ID = A.PART_ID\n");
//        sql.append("   AND BP.IS_GUARD = "+Constant.PART_BASE_FLAG_YES+"\n");
        sql.append("  LEFT JOIN TC_USER B\n");
        sql.append("    ON A.PLANER_ID = B.USER_ID\n");
        sql.append("  LEFT JOIN TC_USER C\n");
        sql.append("    ON A.PLANER_ID = C.USER_ID");

        //提升效率  分拼SQL
        if (!"".equals(buyerId) && null != buyerId) {
            sql.append(" join tc_user b on a.planer_id = b.user_id and UPPER(b.ACNT) LIKE '%").append(planerId.trim().toUpperCase()).append("%'");
        } else {
            sql.append(" left join tc_user b on a.planer_id = b.user_id ");
        }

        if (!"".equals(planerId) && null != planerId) {
            sql.append(" join tc_user c on a.planer_id = c.user_id and c.user_id =").append(planerId.trim());
        } else {
            sql.append(" left join tc_user c on a.planer_id = c.user_id ");
        }

        sql.append(" where 1=1 ");
        // 艾春 10.08 添加职位控制
        if (poseBusType == Constant.POSE_BUS_TYPE_WRD) {
            sql.append(" and a.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
        }

        if (!"".equals(partCode) && null != partCode) {
            sql.append(" and upper(a.part_Code) like upper('%").append(partCode).append("%')");
        }

        if (!"".equals(partOldcode) && null != partOldcode) {
            sql.append(" and upper(a.part_Oldcode) like upper('%").append(partOldcode).append("%')");
        }

        if (!"".equals(partCname) && null != partCname) {
            sql.append(" and a.part_Cname like '%").append(partCname).append("%'");
        }

        if (!"".equals(isPermanent) && null != isPermanent) {
            sql.append(" and a.is_Permanent='").append(isPermanent).append("'");
        }

        if (!"".equals(mobility) && null != mobility) {
            sql.append(" and a.mobility='").append(mobility).append("'");
        }

        if (!"".equals(partType) && null != partType) {
            sql.append(" and a.part_Type='").append(partType).append("'");
        }

        if (!"".equals(partMaterial) && null != partMaterial) {
            sql.append(" and a.part_Material='").append(partMaterial).append("'");
        }

        if (!"".equals(isReplaced) && null != isReplaced) {
            sql.append(" and a.is_Replaced='").append(isReplaced).append("'");
        }

        if (!"".equals(buyState) && null != buyState) {
            sql.append(" and a.buy_State='").append(buyState).append("'");
        }
        if (!"".equals(state) && null != state) {
            sql.append(" and a.state=").append(state);
        }
        // 艾春9.11添加
        if (!"".equals(yieldly) && null != yieldly) {
            sql.append(" and a.PRODUCE_FAC=").append(yieldly);
        }
        if (!"".equals(cartType) && null != cartType) {
            sql.append(" and a.MODEL_NAME LIKE '%").append(cartType.trim().toUpperCase() + "%' ");
        }
        if (!"".equals(remark) && null != remark) {
            sql.append(" and A.REMARK LIKE '%").append(remark.trim() + "%' ");
        }
        //分网SQL
        if (null != logonUser.getDealerId()) {
            sql.append(CommonUtils.bindPartSQL(logonUser.getDealerId(), "a"));
        }
        sql.append(" ORDER BY A.CREATE_DATE DESC, A.PART_OLDCODE ASC");
        return sql.toString();

    }
    
    /**
     * <p>Description: 分页查询配件主数据列表</p>
     * @param request
     * @param curPage 页码
     * @param pageSize 每页数量
     * @param poseBusType 
     * @return
     */
    public PageResult<Map<String, Object>> queryPartBase(RequestWrapper request, int curPage, int pageSize, int poseBusType) {
        String sql = this.queryPartBaseSql(request, poseBusType);
        PageResult<Map<String, Object>> ps = this.pageQuery(sql, null, this.getFunName(), pageSize, curPage);
        return ps;
    }
    
    /**
     * <p>Description: 不分页查询配件主数据列表</p>
     * @param request
     * @param poseBusType
     * @return
     */
    public List<Map<String, Object>> queryPartBaseList(RequestWrapper request, int poseBusType) {
        String sql = this.queryPartBaseSql(request, poseBusType);
        List<Map<String, Object>> ps = this.pageQuery(sql, null, this.getFunName());
        return ps;
    }
    

    /**
     * 查询详细配件主数据
     *
     * @param : @param partId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description: TODO
     */
    public Map<String, Object> queryPartDetail(String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*, ");
        sql.append(" TO_CHAR(A.STOP_LOAD_DATE, 'YYYY-MM-DD') STOP_LOAD_DATE_F, ");
        sql.append(" TO_CHAR(A.SALE_DISABLE_DATE, 'YYYY-MM-DD') SALE_DISABLE_DATE_F, ");
        sql.append(" TO_CHAR(A.PART_DISABLE_DATE, 'YYYY-MM-DD') PART_DISABLE_DATE_F, ");
        sql.append(" TO_CHAR(A.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE_F ");
        sql.append(" from tt_part_define a ");
        sql.append(" where 1=1 ");
        sql.append(" and a.part_id='").append(partId).append("'");
        Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
        return map;
    }

    public List<Map<String, Object>> queryPartBase(RequestWrapper request, int poseBusType) {
        List<Map<String, Object>> list = null;
        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
        String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
        String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
        String isPermanent = CommonUtils.checkNull(request.getParamValue("IS_PERMANENT"));//是否常备
        String mobility = CommonUtils.checkNull(request.getParamValue("MOBILITY"));//流动性
        String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
        String partMaterial = CommonUtils.checkNull(request.getParamValue("PART_MATERIAL"));//配件属性
        String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
        String isReplaced = CommonUtils.checkNull(request.getParamValue("IS_REPLACED"));//是否可替代
        String buyState = CommonUtils.checkNull(request.getParamValue("BUY_STATE"));//采购状态
        String buyerId = CommonUtils.checkNull(request.getParamValue("BUYER_ID"));//采购员
        String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否有效
        String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));//结算基地
        String cartType = CommonUtils.checkNull(request.getParamValue("cartType"));//车系
        String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注

        // 17.7.11 张磊修改
//        List<Map<String, Object>> mobilityList = this.getTcCode(Constant.PART_MOBILITY);
//        List<Map<String, Object>> partTypeList = this.getTcCode(Constant.PART_BASE_PART_TYPES);
//        List<Map<String, Object>> buyTypeList = this.getTcCode(Constant.PART_BASE_BUY_STATE);
//        List<Map<String, Object>> isChangeList = this.getTcCode(Constant.PART_IS_CHANGHE);
//        List<Map<String, Object>> produce_stateList = this.getTcCode(Constant.PART_PRODUCE_STATE);
//        List<Map<String, Object>> pack_stateList = this.getTcCode(Constant.PART_PACK_STATE);
//        List<Map<String, Object>> partMaterialList = this.getTcCode(Constant.PART_BASE_MATERIAL);//配件主数据维护中配件材料
//        List<Map<String, Object>> produce_facList = this.getTcCode(Constant.YIELDLY); //配件品牌
//        List<Map<String, Object>> positionList = this.getTcCode(Constant.PART_BASE_POSITION); //配件主数据维护中配件结构状态
        List<Map<String, Object>> flagList = this.getTcCode(Constant.PART_BASE_FLAG);  //配件中所有是否
        List<Map<String, Object>> stateList = this.getTcCode(Constant.STATUS);// 状态类型 有效、无效
        List<Map<String, Object>> partTypeList = this.getTcCode(Constant.PART_PRODUCE_STATE); //配件主数据维护-配件种类-9263
        List<Map<String, Object>> partFitList = this.getTcCode(Constant.ZT_PB_PART_FIT); //配件主数据维护-装配-9570
        List<Map<String, Object>> partCategoryList = this.getTcCode(Constant.ZT_PB_PART_CATEGORY); //配件主数据维护-配件类别-9571
        List<Map<String, Object>> partProcurementSiteList = this.getTcCode(Constant.PURCHASE_WAY); //采购方式(配件主数据维护-所属基地)-9281PURCHASE_WAY

        String produceFacDecode = this.loadDecodeSql(partProcurementSiteList, "A.PRODUCE_FAC"); // 采购方式
        String producStateDecode = this.loadDecodeSql(partTypeList, "A.PRODUCE_STATE"); // 配件种类
        String partFitDecode = this.loadDecodeSql(partFitList, "A.PART_FIT"); // 装配
        String partCategoryDecode = this.loadDecodeSql(partCategoryList, "A.PART_CATEGORY"); // 配件类别
        String isPartDisable = this.loadDecodeSql(flagList, "A.IS_PART_DISABLE"); // 是否停用
        String isSaleDisable = this.loadDecodeSql(flagList, "A.IS_SALE_DISABLE"); // 是否售完停用
        String isStopLoad = this.loadDecodeSql(flagList, "A.IS_STOP_LOAD"); //  是否停止装车
        String isProtocolPackDecode = this.loadDecodeSql(flagList, "A.IS_PROTOCOL_PACK"); // 是否协议包装
        String isEntrusrPackDecode = this.loadDecodeSql(flagList, "A.IS_ENTRUSR_PACK"); // 是否委托发货
        String cccFlagDecode = this.loadDecodeSql(flagList, "A.CCC_FLAG"); // 是否3C标识
        String isMagBatchDecode = this.loadDecodeSql(flagList, "A.IS_MAG_BATCH"); // 是否批次管理
        String stateDecode = this.loadDecodeSql(stateList, "A.STATE"); // 是否有效
        
        
        // 17.7.11 张磊修改
//        String mobilityDecode = this.loadDecodeSql(mobilityList, "MOBILITY");
//        String buyStateDecode = this.loadDecodeSql(buyTypeList, "BUY_STATE");
//        String isChange = this.loadDecodeSql(isChangeList, "PART_IS_CHANGHE");
//        String produce_state = this.loadDecodeSql(produce_stateList, "PRODUCE_STATE");
//        String pack_state = this.loadDecodeSql(pack_stateList, "PRODUCE_FAC");
//        String isPermanentDecode = this.loadDecodeSql(flagList, "IS_PERMANENT");
//        String isReplacedDecode = this.loadDecodeSql(flagList, "IS_REPLACED");
//        String partMaterialDecode = this.loadDecodeSql(partMaterialList, "PART_MATERIAL");
//        String stateDecode = this.loadDecodeSql(stateList, "STATE");
//        String is_engine = this.loadDecodeSql(flagList, "IS_ENGINE");
//        String is_export = this.loadDecodeSql(flagList, "IS_EXPORT");
//        String is_gift = this.loadDecodeSql(flagList, "IS_GIFT");
//        String produce_fac = this.loadDecodeSql(produce_facList, "PRODUCE_FAC");
//        String is_assembly = this.loadDecodeSql(flagList, "IS_ASSEMBLY");
//        String position = this.loadDecodeSql(positionList, "POSITION");
//        String is_sc = this.loadDecodeSql(flagList, "IS_SC");
//        String is_gxp = this.loadDecodeSql(flagList, "IS_GXP");
//        //20140307 郑志强 新增 是否三包索赔急件
//        String is_spjj = this.loadDecodeSql(flagList, "IS_SPJJ");

        StringBuffer sql = new StringBuffer();
//        sql.append(" select a.PART_ID,a.PART_CODE,a.PART_OLDCODE,a.PART_CNAME, a.CREATE_DATE, A.REMARK, ");
//        sql.append(" A.PKG_SIZE, A.OEM_MIN_PKG, A.MIN_PKG, ");
        sql.append(" select ");
        // 17.7.11 张磊新增
        sql.append(produceFacDecode).append(" PRODUCE_FAC, ");
//        sql.append(partTypeDecode).append(" PART_TYPE, ");
        sql.append(producStateDecode).append(" PRODUCE_STATE, ");
        sql.append(partCategoryDecode).append(" PART_CATEGORY, ");
        sql.append(partFitDecode).append(" PART_FIT, ");
        sql.append(cccFlagDecode).append(" CCC_FLAG, ");
        sql.append(isPartDisable).append(" IS_PART_DISABLE, ");
        sql.append(isSaleDisable).append(" IS_SALE_DISABLE, ");
        sql.append(isProtocolPackDecode).append(" IS_PROTOCOL_PACK, ");
        sql.append(isEntrusrPackDecode).append(" IS_ENTRUSR_PACK, ");
        sql.append(isStopLoad).append(" IS_STOP_LOAD, ");
        sql.append(isMagBatchDecode).append(" IS_MAG_BATCH, ");
        sql.append(stateDecode).append(" STATE, ");
        sql.append(" A.PART_CODE, ");
        sql.append(" A.PART_OLDCODE, ");
        sql.append(" A.PART_CNAME, ");
        sql.append(" A.PART_ENAME, ");
        sql.append(" A.VEHICLE_VOLUME, "); 
        sql.append(" TO_CHAR(A.PART_DISABLE_DATE, 'YYYY-MM-DD') PART_DISABLE_DATE, "); 
        sql.append(" TO_CHAR(A.SALE_DISABLE_DATE, 'YYYY-MM-DD') SALE_DISABLE_DATE, "); 
        sql.append(" TO_CHAR(A.STOP_LOAD_DATE, 'YYYY-MM-DD') STOP_LOAD_DATE, "); 
        sql.append(" A.MODEL_CODE, "); 
        sql.append(" A.MODEL_NAME, "); 
        sql.append(" TO_CHAR(A.FIRST_WARNHOUSE_DATE, 'YYYY-MM-DD HH:MI:SS') FIRST_WARNHOUSE_DATE, "); 
        sql.append(" A.MIN_SALE, "); 
        sql.append(" A.MAX_SALE_VOLUME, "); 
        sql.append(" A.MIN_PURCHASE, "); 
        sql.append(" A.PACK_SPECIFICATION, "); 
        sql.append(" A.LENGTH, "); 
        sql.append(" A.WIDTH, "); 
        sql.append(" A.HEIGHT, "); 
        sql.append(" A.VOLUME, "); 
        sql.append(" A.WEIGHT, "); 
        sql.append(" A.OTHER_PART_ID, "); 
        sql.append(" TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD HH:MI:SS') CREATE_DATE "); 
        
        // 17.7.11 张磊修改
//        sql.append(mobilityDecode).append(" MOBILITY_C,");
//        sql.append(buyStateDecode).append(" BUY_STATE_C ,");
//        sql.append(isChange).append(" PART_IS_CHANGHE_C, ");
//        sql.append(produce_state).append(" PRODUCE_STATE_C, ");
//        sql.append(pack_state).append(" PACK_STATE_C, ");
//        
//        sql.append(partTypeDecode).append(" PART_TYPE_C ,");
//        sql.append(isReplacedDecode).append(" IS_REPLACED_C,");
//        sql.append(isPermanentDecode).append(" IS_PERMANENT_C, ");
//        sql.append(partMaterialDecode).append(" PART_MATERIAL_C ,");
//        sql.append(stateDecode).append(" STATE_C ,");
//        sql.append(is_engine).append(" IS_ENGINE_C, ");
//        sql.append(is_export).append(" IS_EXPORT_C, ");
//        sql.append(is_gift).append(" IS_GIFT_C, ");
//        sql.append(produce_fac).append(" PRODUCE_FAC_C, ");
//        sql.append(is_assembly).append(" IS_ASSEMBLY_C, ");
//        sql.append(position).append(" POSITION_C, ");
//        sql.append(is_sc).append(" IS_SC_C, ");
//        sql.append(is_gxp).append(" IS_GXP_C, ");
        //20140307 郑志强 新增 是否三包索赔急件
//        sql.append(is_spjj).append(" IS_SPJJ_C, ");
//        sql.append(" a.MOBILITY,a.unit,a.MIN_PACK1,a.MIN_PACK2, ");
//        sql.append(" A.PKG_SIZE, A.OEM_MIN_PKG, A.MIN_PKG, ");
//        sql.append(" A.MODEL_NAME, A.SERIES_NAME,MODEL_NAME, A.MODEL_NAME, ");
//        sql.append(" C.Name PLANER_ID,B.Name BUYER_ID  ");
//        sql.append("(SELECT SP.SALE_PRICE1\n");
//        sql.append("         FROM TT_PART_SALES_PRICE SP\n");
//        sql.append("        WHERE SP.PART_ID = A.PART_ID) SALE_PRICE1\n");
        sql.append(" from tt_part_define a ");

        //提升效率  分拼SQL
        if (!"".equals(buyerId) && null != buyerId) {
            sql.append(" join tc_user b on a.planer_id = b.user_id and UPPER(b.ACNT) LIKE '%").append(planerId.trim().toUpperCase()).append("%'");
        } else {
            sql.append(" left join tc_user b on a.planer_id = b.user_id ");
        }

        if (!"".equals(planerId) && null != planerId) {
            sql.append(" join tc_user c on a.planer_id = c.user_id and c.user_id =").append(planerId.trim());
        } else {
            sql.append(" left join tc_user c on a.planer_id = c.user_id ");
        }

        sql.append(" where 1=1 ");
        // 艾春 10.08 添加职位控制
        if (poseBusType == Constant.POSE_BUS_TYPE_WRD) {
            sql.append(" and a.part_is_changhe =" + Constant.PART_IS_CHANGHE_02 + "\n");
        }

        if (!"".equals(partCode) && null != partCode) {
            sql.append(" and upper(a.part_Code) like upper('%").append(partCode).append("%')");
        }

        if (!"".equals(partOldcode) && null != partOldcode) {
            sql.append(" and upper(a.part_Oldcode) like upper('%").append(partOldcode).append("%')");
        }

        if (!"".equals(partCname) && null != partCname) {
            sql.append(" and a.part_Cname like '%").append(partCname).append("%'");
        }

        if (!"".equals(isPermanent) && null != isPermanent) {
            sql.append(" and a.is_Permanent='").append(isPermanent).append("'");
        }

        if (!"".equals(mobility) && null != mobility) {
            sql.append(" and a.mobility='").append(mobility).append("'");
        }

        if (!"".equals(partType) && null != partType) {
            sql.append(" and a.part_Type='").append(partType).append("'");
        }

        if (!"".equals(partMaterial) && null != partMaterial) {
            sql.append(" and a.part_Material='").append(partMaterial).append("'");
        }

        if (!"".equals(isReplaced) && null != isReplaced) {
            sql.append(" and a.is_Replaced='").append(isReplaced).append("'");
        }

        if (!"".equals(buyState) && null != buyState) {
            sql.append(" and a.buy_State='").append(buyState).append("'");
        }
        if (!"".equals(state) && null != state) {
            sql.append(" and a.state=").append(state);
        }

        if (!"".equals(yieldly) && null != yieldly) {
            sql.append(" and a.PRODUCE_FAC=").append(yieldly);
        }
        if (!"".equals(cartType) && null != cartType) {
            sql.append(" and a.model_name LIKE '%").append(cartType.trim().toUpperCase() + "%' ");
        }
        if (!"".equals(remark) && null != remark) {
            sql.append(" and A.REMARK LIKE '%").append(remark.trim() + "%' ");
        }
        sql.append(" ORDER BY A.PART_OLDCODE ");
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    private String loadDecodeSql(List<Map<String, Object>> list, String code) {
        String decodeSql = "decode(" + code + "";
        for (Map<String, Object> map : list) {
            decodeSql += ",'" + map.get("CODE_ID").toString() + "','" +
                    map.get("CODE_DESC").toString() + "'";
        }
        if (decodeSql == "docode(" + code + "") {
            return "'' " + code;
        }
        decodeSql += ",'')";
        return decodeSql;
    }
    
    
    private String loadDecodeSql2(List<TcCodePO> list, String code) {
        String decodeSql = "decode(" + code + "";
        for (TcCodePO tc : list) {
            decodeSql += ",'" + tc.getCodeId() + "','" + tc.getCodeDesc() + "'";
        }
        if (decodeSql == "docode(" + code + "") {
            return "'' " + code;
        }
        decodeSql += ",'')";
        return decodeSql;
    }

    public List<Map<String, Object>> getTcCode(Integer code) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" select CODE_ID,CODE_DESC from tc_code ");
        sql.append(" where 1=1 and type='").append(code).append("'");
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @param partOldcode
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 校验配件编码
     * @Description: TODO
     */
    public List<Map<String, Object>> validatePartOldcode(String partOldcode) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" select PART_ID, PART_OLDCODE from tt_part_define where  PART_OLDCODE='");
        sql.append(partOldcode);
        sql.append("'");
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
    
    /**
     * @param value 值
     * @param columnName 字段名
     * @return :
     * @Title : 校验TT_PART_DEFINE表中某字段的唯一值
     * @Description: 
     */
    public List<Map<String, Object>> validateUniqueVal(String value, String columnName) {
        List<Map<String, Object>> list = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT PART_ID \n");
        sql.append("   FROM TT_PART_DEFINE \n");
        sql.append("  WHERE "+columnName+" = '"+value+"' \n");
        list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * 配件可变参数查询
     *
     * @param type
     * @return
     * @throws Exception
     */
    public List<TtPartFixcodeDefinePO> getPartUnitList(Integer type) throws Exception {
        try {
            TtPartFixcodeDefinePO po = new TtPartFixcodeDefinePO();
            po.setFixGouptype(type);
            List list = select(po);
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> getBaseHis(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.* ,b.name as update_by_name from tt_part_define_history a");
        sql.append(" left join tc_user b on b.user_id = a.update_by ");
        sql.append(" where part_id='").append(id).append("'");
        sql.append(" order by A.UPDATE_date desc");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
    
    /**
     * <p>Description: 获取配件主数据修改记录</p>
     * @param request 
     * @param curPage 页码
     * @param pageSize 每页数量
     * @param partId 配件ID
     * @return 
     * @throws Exception
     */
    public PageResult<Map<String, Object>> getBaseHisListPage(RequestWrapper request, int curPage, int pageSize, String partId) throws Exception{
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.* ,b.name as update_by_name from tt_part_define_history a");
        sql.append(" left join tc_user b on b.user_id = a.update_by ");
        sql.append(" where part_id='"+partId+"'");
        sql.append(" order by A.UPDATE_date desc");
        PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getSortList() throws Exception {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PF.sort_id, PF.sort_type\n");
        sql.append("  FROM Tt_Part_Palnsort_Define PF\n");
        sql.append(" WHERE status = 1  AND PF.state = " + Constant.STATUS_ENABLE);
        sql.append(" ORDER by pf.sort_type");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-11-6
     * @Title : 获取供应商信息
     */
    public List<Map<String, Object>> getVenderList(String sqlStr) throws Exception {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT VD.*\n");
        sql.append("  FROM TT_PART_VENDER_DEFINE VD\n");
        sql.append(" WHERE 1 = 1 ");
        sql.append(sqlStr);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public PageResult<Map<String, Object>> queryPartSalesBase(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {

            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String packState = CommonUtils.checkNull(request.getParamValue("PACK_STATE")); //包装发运方式
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否有效
            String isLack = CommonUtils.checkNull(request.getParamValue("IS_LACK"));//是否紧缺
            String isPlan = CommonUtils.checkNull(request.getParamValue("IS_PLAN")); //是否计划
            String isDirect = CommonUtils.checkNull(request.getParamValue("IS_DIRECT")); //是否计划
            String ofFlag = CommonUtils.checkNull(request.getParamValue("OF_FLAG"));//是否流失关注件
            String IS_SPECIAL = CommonUtils.checkNull(request.getParamValue("IS_SPECIAL"));//是否特殊配件
            String isGx = CommonUtils.checkNull(request.getParamValue("IS_GX")); //是否计划

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.IS_PLAN, \n");
            sql.append("        T.IS_DIRECT, t.is_gxp,\n");
            sql.append("        T.IS_LACK, \n");
            sql.append("        T.OF_FLAG, \n");
            sql.append("        T.PACK_STATE, \n");
            sql.append("        T.STATE, \n");
            sql.append("        T.IS_SPECIAL \n");
            sql.append("   FROM TT_PART_DEFINE T \n");
            sql.append("  WHERE T.STATE = 10011001 \n");
            sql.append("    AND T.STATUS = 1 \n");

            if (null != partCode && !partCode.equals("")) {
                sql.append(" and UPPER(T.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
            }

            if (null != partOldcode && !partOldcode.equals("")) {
                sql.append(" and UPPER(T.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != partCname && !partCname.equals("")) {
                sql.append(" and UPPER(T.PART_CNAME) LIKE '%" + partCname.toUpperCase() + "%'");
            }

            if (null != state && !state.equals("")) {
                sql.append(" and T.STATE = " + Integer.parseInt(state));
            }

            if (null != isLack && !isLack.equals("")) {
                sql.append(" and T.IS_LACK = " + Integer.parseInt(isLack));
            }

            if (null != IS_SPECIAL && !IS_SPECIAL.equals("")) {
                sql.append(" and T.IS_SPECIAL = " + Integer.parseInt(IS_SPECIAL));
            }

            if (null != isPlan && !isPlan.equals("")) {
                sql.append(" and T.IS_PLAN = " + Integer.parseInt(isPlan));
            }

            if (null != packState && !packState.equals("")) {
                sql.append(" and T.PACK_STATE = " + Integer.parseInt(packState));
            }

            if (null != ofFlag && !ofFlag.equals("")) {
                sql.append(" and T.OF_FLAG = " + Integer.parseInt(ofFlag));
            }

            if (null != isDirect && !isDirect.equals("")) {
                sql.append(" and T.is_direct = " + Integer.parseInt(isDirect));
            }
            if (null != isGx && !isGx.equals("")) {
                sql.append(" and T.IS_GXP = " + Integer.parseInt(isGx));
            }

            sql.append("  ORDER BY T.PART_OLDCODE, T.PART_CNAME, T.PART_CODE \n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> partSalesBaseList(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //件号
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String packState = CommonUtils.checkNull(request.getParamValue("PACK_STATE")); //包装发运方式
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否有效
            String isLack = CommonUtils.checkNull(request.getParamValue("IS_LACK"));//是否紧缺
            String isPlan = CommonUtils.checkNull(request.getParamValue("IS_PLAN")); //是否计划
            String ofFlag = CommonUtils.checkNull(request.getParamValue("OF_FLAG"));//是否流失关注件
            String IS_SPECIAL = CommonUtils.checkNull(request.getParamValue("IS_SPECIAL"));//是否特殊配件

            String isDirect = CommonUtils.checkNull(request.getParamValue("IS_DIRECT")); //是否精品
            String isGx = CommonUtils.checkNull(request.getParamValue("IS_GX")); //是否计划

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T.PART_ID, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.IS_PLAN, \n");
            sql.append("        T.IS_LACK, \n");
            sql.append("        T.OF_FLAG, \n");
            sql.append("        T.PACK_STATE, \n");
            sql.append("        T.STATE, \n");
            sql.append("        T.IS_SPECIAL,t.IS_DIRECT,T.is_gxp \n");
            sql.append("   FROM TT_PART_DEFINE T \n");
            sql.append("  WHERE T.STATE = 10011001 \n");
            sql.append("    AND T.STATUS = 1 \n");

            if (null != partCode && !partCode.equals("")) {
                sql.append(" and UPPER(T.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
            }

            if (null != partOldcode && !partOldcode.equals("")) {
                sql.append(" and UPPER(T.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
            }
            if (null != partCname && !partCname.equals("")) {
                sql.append(" and UPPER(T.PART_CNAME) LIKE '%" + partCname.toUpperCase() + "%'");
            }

            if (null != state && !state.equals("")) {
                sql.append(" and T.STATE = " + Integer.parseInt(state));
            }

            if (null != isLack && !isLack.equals("")) {
                sql.append(" and T.IS_LACK = " + Integer.parseInt(isLack));
            }

            if (null != IS_SPECIAL && !IS_SPECIAL.equals("")) {
                sql.append(" and T.IS_SPECIAL = " + Integer.parseInt(IS_SPECIAL));
            }

            if (null != isPlan && !isPlan.equals("")) {
                sql.append(" and T.IS_PLAN = " + Integer.parseInt(isPlan));
            }

            if (null != packState && !packState.equals("")) {
                sql.append(" and T.PACK_STATE = " + Integer.parseInt(packState));
            }

            if (null != ofFlag && !ofFlag.equals("")) {
                sql.append(" and T.OF_FLAG = " + Integer.parseInt(ofFlag));
            }

            if (null != isDirect && !isDirect.equals("")) {
                sql.append(" and t.IS_DIRECT = " + Integer.parseInt(isDirect));
            }

            if (null != isGx && !isGx.equals("")) {
                sql.append(" and T.is_gxp = " + Integer.parseInt(isGx));
            }

            sql.append("  ORDER BY T.PART_OLDCODE, T.PART_CNAME, T.PART_CODE \n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * @param partId
     * @return
     */
    public List<Map<String, Object>> getPartPicURLId(String partId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT DISTINCT PD.PART_ID, PD.PART_OLDCODE, PD.PART_CNAME, PD.PIC_URL TPLJ\n");
        sql.append("  FROM TT_PART_DEFINE PD\n");
        sql.append(" WHERE PD.PART_ID = '" + partId + "'\n");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    /**
     * @param \
     * @return
     */
    public void updatePartCarType() {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE TT_PART_DEFINE T\n");
        sql.append("   SET T.MODEL_NAME =\n");
        sql.append("       (SELECT X.FIX_NAME\n");
        sql.append("          FROM (SELECT T.PART_ID, ZA_CONCAT(FD.FIX_NAME) FIX_NAME\n");
        sql.append("                  FROM TT_PART_BRAND_CARTYPE  BC,\n");
        sql.append("                       TT_PART_DEFINE         T,\n");
        sql.append("                       TT_PART_FIXCODE_DEFINE FD\n");
        sql.append("                 WHERE BC.PART_ID = T.PART_ID\n");
        sql.append("                   AND BC.CAR_TYPE = FD.FIX_VALUE\n");
        sql.append("                   AND FD.FIX_GOUPTYPE = '92251010'\n");
        sql.append("                   --AND T.PART_OLDCODE = '10004L15-C02-B01'\n");
        sql.append("                 GROUP BY T.PART_ID) X\n");
        sql.append("         WHERE X.PART_ID = T.PART_ID)\n");
        sql.append(" WHERE EXISTS (SELECT 1\n");
        sql.append("          FROM (SELECT T.PART_ID, ZA_CONCAT(FD.FIX_NAME) FIX_NAME\n");
        sql.append("                  FROM TT_PART_BRAND_CARTYPE  BC,\n");
        sql.append("                       TT_PART_DEFINE         T,\n");
        sql.append("                       TT_PART_FIXCODE_DEFINE FD\n");
        sql.append("                 WHERE BC.PART_ID = T.PART_ID\n");
        sql.append("                   AND BC.CAR_TYPE = FD.FIX_VALUE\n");
        sql.append("                   AND FD.FIX_GOUPTYPE = '92251010'\n");
        sql.append("                   --AND T.PART_OLDCODE = '10004L15-C02-B01'\n");
        sql.append("                 GROUP BY T.PART_ID) X\n");
        sql.append("         WHERE X.PART_ID = T.PART_ID)\n");
        this.update(sql.toString(), null);
    }

    /**
     * 适用车型
     *
     * @param partId
     * @return
     */
    public String getPartCarTypes(String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT za_concat(DC.CAR_TYPE) CAR_TYPE\n");
        sql.append("  FROM TT_PART_BRAND_CARTYPE DC\n");
        sql.append(" WHERE DC.PART_ID = '" + partId + "'\n");
        List<Map<String, Object>> dlrCarTypeList = this.pageQuery(sql.toString(), null, getFunName());

        if (dlrCarTypeList != null && dlrCarTypeList.size() > 0) {
            return dlrCarTypeList.get(0).get("CAR_TYPE") + "";
        }

        return "";
    }

    /**
     * @param ids 车型ID
     * @return
     */
    public String getCarTypeNames(String ids) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ZA_CONCAT(T.FIX_NAME) FIX_NAME\n");
        sql.append("  FROM TT_PART_FIXCODE_DEFINE T\n");
        sql.append(" WHERE T.STATE = 10011001\n");
        sql.append("   AND T.FIX_GOUPTYPE = 92251010\n");
        sql.append("   AND T.FIX_VALUE IN (" + ids + ")\n");
        List<Map<String, Object>> CarTypeList = this.pageQuery(sql.toString(), null, getFunName());

        if (CarTypeList != null && CarTypeList.size() > 0) {
            return CarTypeList.get(0).get("FIX_NAME") + "";
        }

        return "";
    }

  //将配件主数据写入历史记录表
    public void saveHistory(String id) {
        StringBuffer sql=new StringBuffer();
        
        sql.append("insert into tt_part_define_history\n") ; 
        sql.append("  (part_id,\n") ;  
        sql.append("   part_code,\n") ;  
        sql.append("   part_oldcode,\n") ;  
        sql.append("   part_cname,\n") ;  
        sql.append("   part_ename,\n") ;  
        sql.append("   part_type,\n") ;  
        sql.append("   planer_id,\n") ;  
        sql.append("   buyer_id,\n") ;  
        sql.append("   is_replaced,\n") ;  
        sql.append("   repart_id,\n") ;  
        sql.append("   repart_code,\n") ;  
        sql.append("   en_unit,\n") ;  
        sql.append("   unit,\n") ;  
        sql.append("   min_pack1,\n") ;  
        sql.append("   min_pack2,\n") ;  
        sql.append("   is_assembly,\n") ;  
        sql.append("   series_id,\n") ;  
        sql.append("   series_name,\n") ;  
        sql.append("   model_id,\n") ;  
        sql.append("   model_name,\n") ;  
        sql.append("   width,\n") ;  
        sql.append("   height,\n") ;  
        sql.append("   weight,\n") ;  
        sql.append("   volume,\n") ;  
        sql.append("   is_direct,\n") ;  
        sql.append("   is_plan,\n") ;  
        sql.append("   is_lack,\n") ;  
        sql.append("   guarantees_mile,\n") ;  
        sql.append("   guarantees_time,\n") ;  
        sql.append("   note,\n") ;  
        sql.append("   remark,\n") ;  
        sql.append("   repart_date,\n") ;  
        sql.append("   repart_by,\n") ;  
        sql.append("   repart_mby,\n") ;  
        sql.append("   repart_mdate,\n") ;  
        sql.append("   part_origin,\n") ;  
        sql.append("   create_date,\n") ;  
        sql.append("   create_by,\n") ;  
        sql.append("   update_date,\n") ;  
        sql.append("   update_by,\n") ;  
        sql.append("   disable_date,\n") ;  
        sql.append("   disable_by,\n") ;  
        sql.append("   delete_date,\n") ;  
        sql.append("   delete_by,\n") ;  
        sql.append("   state,\n") ;  
        sql.append("   status,\n") ;  
        sql.append("   produce_state,\n") ;  
        sql.append("   produce_fac,\n") ;  
        sql.append("   pack_state,\n") ;  
        sql.append("   whman_id,\n") ;  
        sql.append("   is_special,\n") ;  
        sql.append("   of_flag,\n") ;  
        sql.append("   is_aszy,\n") ;  
        sql.append("   pic_url,\n") ;  
        sql.append("   partshort_id,\n") ;  
        sql.append("   part_price,\n") ;  
        sql.append("   partshort_name,\n") ;  
        sql.append("   factory_id,\n") ;  
        sql.append("   is_forbidden,\n") ;  
        sql.append("   lack_state,\n") ;  
        sql.append("   pack_specs,\n") ;  
        sql.append("   structure_type,\n") ;  
        sql.append("   min_pack,\n") ;  
        sql.append("   is_gauge,\n") ;  
        sql.append("   frame_id,\n") ;  
        sql.append("   ahead_period,\n") ;  
        sql.append("   safe_coef,\n") ;  
        sql.append("   type,\n") ;  
        sql.append("   sellpart_property,\n") ;  
        sql.append("   out_plan,\n") ;  
        sql.append("   is_sale,\n") ;  
        sql.append("   car_type,\n") ;  
        sql.append("   warn_qty,\n") ;  
        sql.append("   sale_property,\n") ;  
        sql.append("   part_discount,\n") ;  
        sql.append("   is_discount,\n") ;  
        sql.append("   is_recover,\n") ;  
        sql.append("   articles_type,\n") ;  
        sql.append("   lc,\n") ;  
        sql.append("   in_plan,\n") ;  
        sql.append("   part_model,\n") ;  
        sql.append("   deliver_period,\n") ;  
        sql.append("   plan_period,\n") ;  
        sql.append("   bearlier_date,\n") ;  
        sql.append("   is_vulnerability,\n") ;  
        sql.append("   storage_date,\n") ;  
        sql.append("   relation_code,\n") ;  
        sql.append("   buy_remark,\n") ;  
        sql.append("   sale_remark,\n") ;  
        sql.append("   wh_remark,\n") ;  
        sql.append("   buy_whid,\n") ;  
        sql.append("   plan_model,\n") ;  
        sql.append("   is_dunnage,\n") ;  
        sql.append("   wh_id,\n") ;  
        sql.append("   start_date,\n") ;  
        sql.append("   supply_frame_id,\n") ;  
        sql.append("   supply_type,\n") ;  
        sql.append("   barcode_id,\n") ;  
        sql.append("   start_shipment_way,\n") ;  
        sql.append("   length,\n") ;  
        sql.append("   frequency,\n") ;  
        sql.append("   org_id,\n") ;  
        sql.append("   part_sort,\n") ;  
        sql.append("   supply_remark,\n") ;  
        sql.append("   is_sending,\n") ;  
        sql.append("   assembly_level,\n") ;  
        sql.append("   module_classification,\n") ;  
        sql.append("   home_made,\n") ;  
        sql.append("   superior_purchasing,\n") ;  
//        sql.append("   pdc_transtype,\n") ;  
//        sql.append("   rdc_transtype,\n") ;  
        sql.append("   buy_min_pkg,\n") ;  
        sql.append("   sales_min_pkg,\n") ;  
//        sql.append("   buyer_feedback,\n") ;  
//        sql.append("   buyer_up_remark,\n") ;  
        sql.append("   bill_place,\n") ;  
        sql.append("   is_warn,\n") ;  
//        sql.append("   vender_min_qty,\n") ;  
//        sql.append("   taxcode,\n") ;  
//        sql.append("   hs_id,\n") ;  
        sql.append("   middle_package,\n") ;  
        sql.append("   box_num,\n") ;  
        sql.append("   pallet_num,\n") ;  
        sql.append("   pallet_box)\n") ;  
        sql.append("  select  part_id,\n") ;  
        sql.append("         part_code,\n") ;  
        sql.append("         part_oldcode,\n") ;  
        sql.append("         part_cname,\n") ;  
        sql.append("         part_ename,\n") ;  
        sql.append("         part_type,\n") ;  
        sql.append("         planer_id,\n") ;  
        sql.append("         buyer_id,\n") ;  
        sql.append("         is_replaced,\n") ;  
        sql.append("         repart_id,\n") ;  
        sql.append("         repart_code,\n") ;  
        sql.append("         en_unit,\n") ;  
        sql.append("         unit,\n") ;  
        sql.append("         min_pack1,\n") ;  
        sql.append("         min_pack2,\n") ;  
        sql.append("         is_assembly,\n") ;  
        sql.append("         series_id,\n") ;  
        sql.append("         series_name,\n") ;  
        sql.append("         model_id,\n") ;  
        sql.append("         model_name,\n") ;  
        sql.append("         width,\n") ;  
        sql.append("         height,\n") ;  
        sql.append("         weight,\n") ;  
        sql.append("         volume,\n") ;  
        sql.append("         is_direct,\n") ;  
        sql.append("         is_plan,\n") ;  
        sql.append("         is_lack,\n") ;  
        sql.append("         guarantees_mile,\n") ;  
        sql.append("         guarantees_time,\n") ;  
        sql.append("         note,\n") ;  
        sql.append("         remark,\n") ;  
        sql.append("         repart_date,\n") ;  
        sql.append("         repart_by,\n") ;  
        sql.append("         repart_mby,\n") ;  
        sql.append("         repart_mdate,\n") ;  
        sql.append("         part_origin,\n") ;  
        sql.append("         create_date,\n") ;  
        sql.append("         create_by,\n") ;  
        sql.append("         update_date,\n") ;  
        sql.append("         update_by,\n") ;  
        sql.append("         disable_date,\n") ;  
        sql.append("         disable_by,\n") ;  
        sql.append("         delete_date,\n") ;  
        sql.append("         delete_by,\n") ;  
        sql.append("         state,\n") ;  
        sql.append("         status,\n") ;  
        sql.append("         produce_state,\n") ;  
        sql.append("         produce_fac,\n") ;  
        sql.append("         pack_state,\n") ;  
        sql.append("         whman_id,\n") ;  
        sql.append("         is_special,\n") ;  
        sql.append("         of_flag,\n") ;  
        sql.append("         is_aszy,\n") ;  
        sql.append("         pic_url,\n") ;  
        sql.append("         partshort_id,\n") ;  
        sql.append("         part_price,\n") ;  
        sql.append("         partshort_name,\n") ;  
        sql.append("         factory_id,\n") ;  
        sql.append("         is_forbidden,\n") ;  
        sql.append("         lack_state,\n") ;  
        sql.append("         pack_specs,\n") ;  
        sql.append("         structure_type,\n") ;  
        sql.append("         min_pack,\n") ;  
        sql.append("         is_gauge,\n") ;  
        sql.append("         frame_id,\n") ;  
        sql.append("         ahead_period,\n") ;  
        sql.append("         safe_coef,\n") ;  
        sql.append("         type,\n") ;  
        sql.append("         sellpart_property,\n") ;  
        sql.append("         out_plan,\n") ;  
        sql.append("         is_sale,\n") ;  
        sql.append("         car_type,\n") ;  
        sql.append("         warn_qty,\n") ;  
        sql.append("         sale_property,\n") ;  
        sql.append("         part_discount,\n") ;  
        sql.append("         is_discount,\n") ;  
        sql.append("         is_recover,\n") ;  
        sql.append("         articles_type,\n") ;  
        sql.append("         lc,\n") ;  
        sql.append("         in_plan,\n") ;  
        sql.append("         part_model,\n") ;  
        sql.append("         deliver_period,\n") ;  
        sql.append("         plan_period,\n") ;  
        sql.append("         bearlier_date,\n") ;  
        sql.append("         is_vulnerability,\n") ;  
        sql.append("         storage_date,\n") ;  
        sql.append("         relation_code,\n") ;  
        sql.append("         buy_remark,\n") ;  
        sql.append("         sale_remark,\n") ;  
        sql.append("         wh_remark,\n") ;  
        sql.append("         buy_whid,\n") ;  
        sql.append("         plan_model,\n") ;  
        sql.append("         is_dunnage,\n") ;  
        sql.append("         wh_id,\n") ;  
        sql.append("         start_date,\n") ;  
        sql.append("         supply_frame_id,\n") ;  
        sql.append("         supply_type,\n") ;  
        sql.append("         barcode_id,\n") ;  
        sql.append("         start_shipment_way,\n") ;  
        sql.append("         length,\n") ;  
        sql.append("         frequency,\n") ;  
        sql.append("         org_id,\n") ;  
        sql.append("         part_sort,\n") ;  
        sql.append("         supply_remark,\n") ;  
        sql.append("         is_sending,\n") ;  
        sql.append("         assembly_level,\n") ;  
        sql.append("         module_classification,\n") ;  
        sql.append("         home_made,\n") ;  
        sql.append("         superior_purchasing,\n") ;  
//        sql.append("         pdc_transtype,\n") ;  
//        sql.append("         rdc_transtype,\n") ;  
        sql.append("         buy_min_pkg,\n") ;  
        sql.append("         sales_min_pkg,\n") ;  
//        sql.append("         buyer_feedback,\n") ;  
//        sql.append("         buyer_up_remark,\n") ;  
        sql.append("         bill_place,\n") ;  
        sql.append("         is_warn,\n") ;  
//        sql.append("         vender_min_qty,\n") ;  
//        sql.append("         taxcode,\n") ;  
//        sql.append("         hs_id,\n") ;  
        sql.append("         middle_package,\n") ;  
        sql.append("         box_num,\n") ;  
        sql.append("         pallet_num,\n") ;  
        sql.append("         pallet_box\n") ;  
        sql.append("    from tt_part_define t\n") ;  
        sql.append("   where t.part_id="+id);

        dao.insert(sql.toString());
    }

}
