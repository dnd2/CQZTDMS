package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartDiscountRateDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDiscountDefineDtlPO;
import com.infodms.dms.po.TtPartDiscountDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : chenjunjiang
 *         CreateDate     : 2013-5-16
 * @ClassName : PartDiscountRateManager
 * @Description : 配件折扣率维护
 */
public class PartDiscountRateManager implements PTConstants {
    public Logger logger = Logger.getLogger(PartDiscountRateManager.class);
    private PartDiscountRateDao dao = PartDiscountRateDao.getInstance();

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-16
     * @Title :
     * @Description: 配件折扣率查询初始化
     */
    public void queryPartDiscountRateInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_DISCOUNT_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件折扣率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-16
     * @Title :
     * @Description: 查询配件折扣率
     */
    public void queryPartDiscountInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String discountType = CommonUtils.checkNull(request.getParamValue("DISCOUNT_TYPE"));//折扣类型
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//有效开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//有效结束时间
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否有效

            TtPartDiscountDefinePO po = new TtPartDiscountDefinePO();
            if (!"".equals(discountType)) {
                po.setDiscountType(CommonUtils.parseInteger(discountType));
            }
            if (!"".equals(state)) {
                po.setState(CommonUtils.parseInteger(state));
            }

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDiscountList(po, startDate, endDate, logonUser, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件折扣率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-16
     * @Title :
     * @Description: 折扣率明细查询
     */
    public void queryPartDiscountDtlInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String discountType = CommonUtils.checkNull(request.getParamValue("DISCOUNT_TYPE"));//折扣类型
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//有效开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//有效结束时间
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//服务商名称
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_LODCODE"));//配件编码
            partOldCode = partOldCode.toUpperCase();
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否有效

            TtPartDiscountDefinePO po = new TtPartDiscountDefinePO();
            if (!"".equals(discountType)) {
                po.setDiscountType(CommonUtils.parseInteger(discountType));
            }
            if (!"".equals(state)) {
                po.setState(CommonUtils.parseInteger(state));
            }

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDiscountDtlList(po, startDate, endDate, dealerName, partOldCode, logonUser, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件折扣率明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-16
     * @Title :
     * @Description: 配件折扣率新增初始化
     */
    public void addPartDiscountInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List list = dao.queryDearlerType();//获取服务商类型列表
            act.setOutData("list", list);
            act.setForword(PART_DISCOUNT_ADD_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "配件折扣率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-17
     * @Title :
     * @Description: 保存配件折扣率
     */
    public void savePartDiscount() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String discountType = CommonUtils.checkNull(request.getParamValue("DISCOUNT_TYPE"));//折扣类型
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//有效开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//有效结束时间
            String discountRate = CommonUtils.checkNull(request.getParamValue("DISCOUNT_RATE"));//折扣率
            String dealerType = CommonUtils.checkNull(request.getParamValue("FIX_TYPE"));//服务商类型
            String orderAmount = CommonUtils.checkNull(request.getParamValue("ORDER_AMOUNT"));//金额

            String[] dpIds = request.getParamValues("DP_ID");//id
            String[] dpCodes = request.getParamValues("DP_CODE");//编码
            String[] dpNames = request.getParamValues("DP_NAME");//名称
            String[] partAmount = request.getParamValues("AMOUNT");//数量

            TtPartDiscountDefinePO mainPo = new TtPartDiscountDefinePO();
            mainPo.setDiscountId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            mainPo.setDiscountType(CommonUtils.parseInteger(discountType));
            mainPo.setDiscountRate(CommonUtils.parseDouble(discountRate));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            mainPo.setValidFrom(format.parse(startDate));
            mainPo.setValidTo(format.parse(endDate));

            Integer disType = CommonUtils.parseInteger(discountType).intValue();

            if (disType.intValue() == Constant.PART_DISCOUNT_TYPE_01.intValue()) {
                mainPo.setDealerType(CommonUtils.parseInteger(dealerType));
            }
            if (disType.intValue() == Constant.PART_DISCOUNT_TYPE_04.intValue()
                    ||disType.intValue() == Constant.PART_DISCOUNT_TYPE_01.intValue()) {//如果是订单金额类型
                mainPo.setOrderAmount(CommonUtils.parseDouble(orderAmount));
            }
            mainPo.setCreateDate(new Date());
            mainPo.setCreateBy(logonUser.getUserId());

            dao.insert(mainPo);
            if (disType.intValue() != Constant.PART_DISCOUNT_TYPE_01.intValue() && disType.intValue() != Constant.PART_DISCOUNT_TYPE_04.intValue()) {
                if (dpIds != null && dpIds.length > 0) {
                    for (int i = 0; i < dpIds.length; i++) {
                        TtPartDiscountDefineDtlPO dtlPO = new TtPartDiscountDefineDtlPO();
                        dtlPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                        dtlPO.setDiscountId(mainPo.getDiscountId());
                        dtlPO.setDpId(CommonUtils.parseLong(dpIds[i]));
                        dtlPO.setDpCode(dpCodes[i]);
                        dtlPO.setDpName(dpNames[i]);
                        String dis_rate = request.getParamValue("DIS_RATE"+(i+1));
                        dtlPO.setRate(Float.parseFloat(dis_rate));
                        dtlPO.setAmount(Long.parseLong(partAmount[i]));
                        dtlPO.setCreateDate(new Date());
                        dtlPO.setCreateBy(logonUser.getUserId());

                        dao.insert(dtlPO);
                    }
                }
            }
            act.setOutData("success", "保存成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "配件折扣率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-17
     * @Title :
     * @Description: 配件折扣率维护初始化
     */
    public void updateDiscountInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String discountId = CommonUtils.checkNull(request.getParamValue("discountId"));
            String discountType = CommonUtils.checkNull(request.getParamValue("discountType"));
            Map map = dao.getPartDiscountInfo(discountId);
            List list = dao.queryDearlerType();//获取服务商类型列表
            act.setOutData("list", list);
            request.setAttribute("po", map);
            if (discountType.equals(Constant.PART_DISCOUNT_TYPE_01.toString())) {//如果服务商类型
                act.setForword(PART_DISCOUNT_MOD1_URL);
            } else if (discountType.equals(Constant.PART_DISCOUNT_TYPE_02.toString())) {//如果是服务商
                act.setForword(PART_DISCOUNT_MOD2_URL);
            } else if (discountType.equals(Constant.PART_DISCOUNT_TYPE_03.toString())) {//如果是配件种类
                act.setForword(PART_DISCOUNT_MOD3_URL);
            } else {
                act.setForword(PART_DISCOUNT_MOD4_URL);
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件折扣率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-17
     * @Title :
     * @Description: 查询折扣率明细
     */
    public void queryPartDiscountDtlInfoById() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String discountId = CommonUtils.checkNull(request.getParamValue("DISCOUNT_ID"));//折扣id
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartDiscountDtlByIdList(discountId, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            List<Map<String, Object>> list = ps.getRecords();

            if (list != null) {
                String[] dpIds = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    Map map = list.get(i);
                    long dpId = ((BigDecimal) map.get("DP_ID")).longValue();
                    dpIds[i] = String.valueOf(dpId);
                }
                act.setOutData("dpIds", dpIds);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件折扣率明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-19
     * @Title :
     * @Description: 保存在修改折扣率时新增的明细
     */
    public void savePartDiscountDtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String discountId = CommonUtils.checkNull(request.getParamValue("DISCOUNT_ID"));
            String dpIdStr = CommonUtils.checkNull(request.getParamValue("dpIds"));
            String dpCodeStr = CommonUtils.checkNull(request.getParamValue("dpCodes"));
            String dpNameStr = CommonUtils.checkNull(request.getParamValue("dpNames"));

            String[] partrate = request.getParamValues("DIS_RATE2");//名称
            String[] partAmount = request.getParamValues("AMOUNT");//数量

            String[] dpIds = dpIdStr.split(",");
            String[] dpCodes = dpCodeStr.split(",");
            String[] dpNames = dpNameStr.split(",");
            if (dpIds != null && dpIds.length > 0) {
                for (int i = 0; i < dpIds.length; i++) {
                    TtPartDiscountDefineDtlPO po = new TtPartDiscountDefineDtlPO();
                    po.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                    po.setDiscountId(CommonUtils.parseLong(discountId));
                    po.setDpId(CommonUtils.parseLong(dpIds[i]));
                    po.setDpCode(dpCodes[i]);
                    po.setDpName(dpNames[i]);
                    po.setRate(Float.parseFloat(partrate[i]));
                    po.setAmount(Long.parseLong(partAmount[i]));
                    po.setCreateDate(new Date());
                    po.setCreateBy(logonUser.getUserId());

                    dao.insert(po);
                }
            }
            act.setOutData("success", "添加成功!");
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "配件折扣率明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-19
     * @Title :
     * @Description: 修改折扣率
     */
    public void updatePartDiscount() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String discountId = CommonUtils.checkNull(request.getParamValue("DISCOUNT_ID"));//折扣率id
            String discountType = CommonUtils.checkNull(request.getParamValue("DISCOUNT_TYPE"));//折扣类型
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//有效开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//有效结束时间
            String discountRate = CommonUtils.checkNull(request.getParamValue("DISCOUNT_RATE"));//折扣率
            String dealerType = CommonUtils.checkNull(request.getParamValue("FIX_ID"));//服务商类型
            String orderAmount = CommonUtils.checkNull(request.getParamValue("ORDER_AMOUNT"));//金额
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否有效

            String[] partrate = request.getParamValues("DIS_RATE2");//折扣率
            String[] partAmount = request.getParamValues("AMOUNT");//数量
            String[] pd_id = request.getParamValues("DTL_ID");

            if (pd_id != null && pd_id.length > 0) {
                for (int i = 0; i < pd_id.length; i++) {
                    TtPartDiscountDefineDtlPO defineDtlPO = new TtPartDiscountDefineDtlPO();
                    defineDtlPO.setDtlId(Long.valueOf(pd_id[i]));

                    TtPartDiscountDefineDtlPO defineDtlPO1 = new TtPartDiscountDefineDtlPO();
                    if (partAmount[i] !=null&&!"".equals(partAmount[i])) {
                        defineDtlPO1.setAmount(Long.valueOf(partAmount[i]));
                    } else {
                        defineDtlPO1.setAmount(0l);
                    }
                    if (partrate[i] !=null&&!"".equals(partrate[i])) {
                        defineDtlPO1.setRate(Float.valueOf(partrate[i]));
                    } else {
                        defineDtlPO1.setRate(0f);
                    }
                    dao.update(defineDtlPO, defineDtlPO1);

                }
            }


            TtPartDiscountDefinePO smainPo = new TtPartDiscountDefinePO();
            smainPo.setDiscountId(CommonUtils.parseLong(discountId));
            TtPartDiscountDefinePO mainPo = new TtPartDiscountDefinePO();
            mainPo.setDiscountRate(CommonUtils.parseDouble(discountRate));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            mainPo.setValidFrom(format.parse(startDate));
            mainPo.setValidTo(format.parse(endDate));
            if (CommonUtils.parseInteger(discountType).intValue() == Constant.PART_DISCOUNT_TYPE_01.intValue()) {//如果折扣类型是服务商类型
                mainPo.setDealerType(CommonUtils.parseInteger(dealerType));
            }
            if (CommonUtils.parseInteger(discountType).intValue() == Constant.PART_DISCOUNT_TYPE_04.intValue()
                    || CommonUtils.parseInteger(discountType).intValue() == Constant.PART_DISCOUNT_TYPE_01.intValue()) {//如果折扣类型是订单金额
                mainPo.setOrderAmount(CommonUtils.parseDouble(orderAmount));
            }
            mainPo.setUpdateDate(new Date());
            mainPo.setUpdateBy(logonUser.getUserId());
            mainPo.setState(CommonUtils.parseInteger(state));

            dao.update(smainPo, mainPo);
            act.setOutData("success", "修改成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件折扣率");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-19
     * @Title :
     * @Description: 失效
     */
    public void disablePartDiscountDtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dtlId = CommonUtils.checkNull(request.getParamValue("dtlId"));//折扣率明细id
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            TtPartDiscountDefineDtlPO spo = new TtPartDiscountDefineDtlPO();
            spo.setDtlId(CommonUtils.parseLong(dtlId));
            TtPartDiscountDefineDtlPO po = new TtPartDiscountDefineDtlPO();
            po.setState(Constant.STATUS_DISABLE);
            dao.update(spo, po);
            act.setOutData("curPage", curPage);
            act.setOutData("success", "失效成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件折扣率明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-19
     * @Title :
     * @Description: 有效
     */
    public void enablePartDiscountDtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dtlId = CommonUtils.checkNull(request.getParamValue("dtlId"));//折扣率明细id
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            TtPartDiscountDefineDtlPO spo = new TtPartDiscountDefineDtlPO();
            spo.setDtlId(CommonUtils.parseLong(dtlId));
            TtPartDiscountDefineDtlPO po = new TtPartDiscountDefineDtlPO();
            po.setState(Constant.STATUS_ENABLE);
            dao.update(spo, po);
            act.setOutData("curPage", curPage);
            act.setOutData("success", "有效成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件折扣率明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}